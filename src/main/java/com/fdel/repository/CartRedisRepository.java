package com.fdel.repository;

import static com.fdel.exception.message.CartMessage.*;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.fdel.entity.Cart;
import com.fdel.exception.domain.cart.CartIsEmptyException;
import com.fdel.exception.domain.cart.CartNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class CartRedisRepository implements CartRepository{

	private final long EXPIRATION_INTERVAL = 60*60*2L; //만료 간격 2시간
	
	private final TimeUnit TIME_UNIT = TimeUnit.SECONDS; //만료 간격 단위
	
	private final String KEY_PREFIX = "cart:";
	
	private HashOperations<String, String, String> hashOperations;
	
	private RedisTemplate<String, String> redisTemplate;
   
	@Autowired
	public CartRedisRepository(
			@Qualifier("redisCartTemplate") RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
		this.hashOperations = redisTemplate.<String, String>opsForHash();
	}
   
	/**
	 * 같은 id의 장바구니가 있으면 지웠다가 다시 저장하고
	 * 장바구니가 없으면 새로 저장한다.
	 * @throws CartIsEmptyException 
	 */
	@Override
	public void save(Cart cart) throws CartIsEmptyException {
		/*
		 * key에 저장할 데이터가 없으면 reids에 key가 생성되지 않는다.
		 * 즉 redis에는 빈 장바구니가 저장되지 않는다.
		 */
		if(cart.isCartEmpty()) {
			throw new CartIsEmptyException(CART_IS_EMPTY.getMessage());
		}
		
		String keyWithPreFix = addPreFixTo(cart.getId());
		/*
		 * 키 만료 시간 셋팅
		 * 저장할 때마다 만료 시간이 세팅된다.
		 */ 
		redisTemplate.delete(keyWithPreFix);
		hashOperations.putAll(keyWithPreFix, cart.getMenuMapView());
		redisTemplate.expire(keyWithPreFix, EXPIRATION_INTERVAL, TIME_UNIT);
	
	}
   
	
	/* 
	 * 찾아보고 있으면 장바구니를 가져오고
	 * 없으면 만료된 장바구니라는 예외를 발생시킨다.
	 */
	@Override
	public Cart find(String id) throws CartNotFoundException {
		
		String keyWithPreFix = addPreFixTo(id);
		Map<String, String> menuMap = hashOperations.entries(keyWithPreFix);
		Cart newCart = new Cart(id);
		newCart.setMenuMap(menuMap);
		
		/*
		 * 장바구니가 있는지 체크하는 로직을 먼저하면
		 * 있다는 것이 확인되어서 entry들을 가져오는 순간
		 * key가 만료될 수 있고 그럼 entry 정보가 누락될 수 있다.
		 * 그럼 find 함수는 해당 장바구니에 entry들이 있는데도
		 * 비어있는 장바구니라는 응답을 주게 된다.
		 * 따라서 모든 entry들을 가져오고나서
		 * 아직 key가 만료되지 않고 남아있으면 제대로 entry들을
		 * 가져왔다고 판단할 수 있으므로
		 * Cart가 존재하는지 검사하는 것을 나중에 해준다.
		 */
		//Cart가 존재하는지 검사
		validateExist(keyWithPreFix);
		
		return newCart;
	}
	
	@Override
	public void clear(String sessionId) {
		redisTemplate.delete(addPreFixTo(sessionId));
	}
	

	/*
	 * Redis 서버에 있는 모든 key를 삭제합니다.
	 *
	 * test 용도로만 사용되는 함수이기 때문에
	 * 인터페이스에는 정의되어 있지 않습니다.
	 */
	public void flushAll() {
        if (log.isDebugEnabled())
            log.debug("모든 캐시를 삭제합니다.");
        try {
        	/*
        	 * RedisCallback은 함수형 인터페이스이다. 
        	 * connection을 받아서 필요한 처리를 해주는 역할을 한다.
        	 * redisTemplate은 flushAll api를 제공해주지 않기 때문에
        	 * 이런 식으로 익명 클래스를 작성해서 flushAll 작업을 수행해주는
        	 * 콜백 함수를 주입해준다.
        	 */
            redisTemplate.execute(new RedisCallback<Object>() {
                @Override
                public Object doInRedis(RedisConnection connection) 
                		throws DataAccessException {
                    connection.flushAll();
                    return null;
                }
            });
        } catch (Exception e) {
            log.warn("모든 캐시를 삭제하는데 실패했습니다.", e);
        }
    }

	private void validateExist(String keyWithPreFix) 
			throws CartNotFoundException {
		if(!redisTemplate.hasKey(keyWithPreFix)) {
			throw new CartNotFoundException(CART_NOT_FOUND.getMessage());
		}
	}
	
	private String addPreFixTo(String id) {
		return KEY_PREFIX + id;
	}

}
