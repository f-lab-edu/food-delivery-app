package com.fdel.repository;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fdel.entity.Cart;
import com.fdel.entity.Cart.CartMenuDto;
import com.fdel.exception.domain.cart.CartIsEmptyException;
import com.fdel.exception.domain.cart.CartNotFoundException;

@SpringBootTest
public class CartRedisRepositoryTest {
	
	public final String mockSessionId = "mock-session-id";
	private CartMenuDto mockMenuDto0;
	private CartMenuDto mockMenuDto1;
	
	@Autowired
	private CartRepository cartRepository;
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@BeforeEach
	void beforeEach() {
		mockMenuDto0 = new CartMenuDto(1L);
		mockMenuDto0.setName("피자");
		mockMenuDto0.setPrice(30000);
		mockMenuDto0.setNumberOfWantToBuy(2);
		
		mockMenuDto1 = new CartMenuDto(2L);
		mockMenuDto1.setName("치킨");
		mockMenuDto1.setPrice(20000);
		mockMenuDto1.setNumberOfWantToBuy(3);
	}
	
	@AfterEach
	void afterWach() {
		if(cartRepository instanceof CartRedisRepository) {
			CartRedisRepository cartRedisRepository = (CartRedisRepository)cartRepository;
			cartRedisRepository.flushAll();
		}
	}
	
	@Test
	@DisplayName("cart를 Redis에 저장하면 저장되어야 한다.")
	void when_saveCart_then_cartMustBeSavedInRedis() 
		throws JsonProcessingException, CartNotFoundException, CartIsEmptyException {
		//given
		Cart newCart = new Cart(mockSessionId);
		newCart.putMenu(mockMenuDto0, objectMapper);
		newCart.putMenu(mockMenuDto1, objectMapper);
		
		//when
		cartRepository.save(newCart);
		Cart findCart = cartRepository.find(newCart.getId());
		
		List<CartMenuDto> menuListView = findCart.getMenuListView(objectMapper);
		//unmodifiable list라서 sorting을 위해 modifiable list로 재할당
		List<CartMenuDto> menuList = new ArrayList<>(menuListView);
		//sort
		Collections.sort(menuList, (e1, e2)->{
				Long result = e1.getId()-e2.getId();
				return result.intValue();
			});
		
		//then
		assertThat(findCart.getId(), equalTo(newCart.getId()));
		assertThat(menuList, contains(mockMenuDto0, mockMenuDto1));
	}

}
