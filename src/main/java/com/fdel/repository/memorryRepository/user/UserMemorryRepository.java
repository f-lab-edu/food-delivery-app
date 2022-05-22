package com.fdel.repository.memorryRepository.user;

import static com.fdel.exception.message.EntityMessage.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import com.fdel.entity.User;

/*
 * 병렬로 테스트 하기 위해 만든 메모리 레포지토리입니다.
 */
@Primary
@Profile("test")
@Repository
public class UserMemorryRepository extends UserBaseMemorryRepository{

	private final ThreadLocal<Map<Long, User>> localMap = ThreadLocal.withInitial(HashMap::new);
	private final ThreadLocal<Long> localSequence = ThreadLocal.withInitial(()->1L);
	
	@Override
	public List<User> findAll() {
		Map<Long, User> map = localMap.get();
		return map.entrySet().stream()
			.map(e->e.getValue())
			.sorted((e1, e2)->(int) (e1.getId()-e2.getId()))
			.collect(Collectors.toList());
	}

	@Override
	public <S extends User> S save(S user) {
		Map<Long, User> map = localMap.get();
		if(user.getId()!=null) {
			throw new IllegalStateException(
				ID_ALREADY_EXISTS.getMessage());
		}
		
		Long nextSequence = localSequence.get();
		user.setId(nextSequence);
		
		map.put(nextSequence, user);
		
		nextSequence++;
		localSequence.set(nextSequence);
				
		return user;
	}

	@Override
	public Optional<User> findById(Long id) {
		Map<Long, User> map = localMap.get();
		User user = map.get(id);
		return Optional.ofNullable(user);
	}

	@Override
	public void delete(User user) {
		Map<Long, User> map = localMap.get();
		map.remove(user.getId());
	}

	@Override
	public void deleteAll() {
		localMap.set(new HashMap<Long, User>());
		localSequence.set(1L);
	}

	@Override
	public Optional<User> findByUsername(String username) {
		Map<Long, User> map = localMap.get();
		return map.entrySet().stream()
			.map(e->e.getValue())
			.filter(e->e.getUsername().equals(username))
			.findAny();
	}

}
