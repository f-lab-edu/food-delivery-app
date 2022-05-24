package com.fdel.repository.memoryRepository.menu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import com.fdel.entity.Menu;
import com.fdel.exception.message.EntityMessage;
import com.fdel.repository.memoryRepository.PrivateSetter;

/*
 * 병렬로 테스트 하기 위해 만든 메모리 레포지토리입니다.
 */
@Profile("test")
@Primary
@Repository
public class MenuMemoryRepository extends MenuBaseMemoryRepository{
	
	private final ThreadLocal<Map<Long, Menu>> localMap = ThreadLocal.withInitial(HashMap::new);
	private final ThreadLocal<Long> localSequence = ThreadLocal.withInitial(()->1L);
	
	@Override
	public List<Menu> findAll() {
		Map<Long, Menu> map = localMap.get();
		return map.entrySet().stream()
			.map(e->e.getValue())
			.sorted((e1, e2)->(int) (e1.getId()-e2.getId()))
			.collect(Collectors.toList());
	}

	@Override
	public <S extends Menu> S save(S menu) {
		Map<Long, Menu> map = localMap.get();
		if(menu.getId()!=null) {
			throw new IllegalStateException(
				EntityMessage.ID_ALREADY_EXISTS.getMessage());
		}
		
		Long nextSequence = localSequence.get();
		PrivateSetter.setId(menu, nextSequence);
		
		map.put(nextSequence, menu);
		
		nextSequence++;
		localSequence.set(nextSequence);
				
		return menu;
	}

	@Override
	public Optional<Menu> findById(Long id) {
		Map<Long, Menu> map = localMap.get();
		Menu menu = map.get(id);
		return Optional.ofNullable(menu);
	}

	@Override
	public void delete(Menu menu) {
		Map<Long, Menu> map = localMap.get();
		map.remove(menu.getId());
	}

	@Override
	public void deleteAll() {
		localMap.set(new HashMap<Long, Menu>());
		localSequence.set(1L);
	}
	
}
