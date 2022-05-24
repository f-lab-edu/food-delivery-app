package com.fdel.repository.memoryRepository.store;

import static com.fdel.exception.message.EntityMessage.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import com.fdel.entity.Store;
import com.fdel.repository.memoryRepository.PrivateSetter;

@Primary
@Profile("test")
@Repository
public class StoreMemoryRepository extends StoreBaseMemoryRepository {
	
	private final ThreadLocal<Map<Long, Store>> localMap = ThreadLocal.withInitial(HashMap::new);
	private final ThreadLocal<Long> localSequence = ThreadLocal.withInitial(()->1L);
	
	@Override
	public List<Store> findAll() {
		Map<Long, Store> map = localMap.get();
		return map.entrySet().stream()
			.map(e->e.getValue())
			.sorted((e1, e2)->(int) (e1.getId()-e2.getId()))
			.collect(Collectors.toList());
	}

	@Override
	public <S extends Store> S save(S store) {
		Map<Long, Store> map = localMap.get();
		if(store.getId()!=null) {
			throw new IllegalStateException(
				ID_ALREADY_EXISTS.getMessage());
		}
		
		Long nextSequence = localSequence.get();
		PrivateSetter.setId(store, nextSequence);
		map.put(nextSequence, store);
		
		nextSequence++;
		localSequence.set(nextSequence);
				
		return store;
	}

	@Override
	public Optional<Store> findById(Long id) {
		Map<Long, Store> map = localMap.get();
		Store store = map.get(id);
		return Optional.ofNullable(store);
	}

	@Override
	public void delete(Store store) {
		Map<Long, Store> map = localMap.get();
		map.remove(store.getId());
	}

	@Override
	public void deleteAll() {
		localMap.set(new HashMap<Long, Store>());
		localSequence.set(1L);
	}

}
