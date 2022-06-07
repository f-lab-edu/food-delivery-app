package com.fdel.service;

import static com.fdel.exception.message.StoreMessage.*;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fdel.dto.store.StoreDto;
import com.fdel.entity.Store;
import com.fdel.entity.RestaurantType;
import com.fdel.entity.StoreRestaurantType;
import com.fdel.repository.RestaurantTypeRepository;
import com.fdel.repository.StoreRepository;
import com.fdel.repository.StoreRestaurantTypeRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StoreService {

	private final StoreRepository storeRepository;
	private final RestaurantTypeRepository restaurantTypeRepository;
	private final StoreRestaurantTypeRepository storeRestaurantTypeRepository;

  	@Transactional
  	public void regist(StoreDto storeDto) {
  		Store store = storeDto.toEntity();
  		store.init(); //영속화 전 초기화 및 무결성 검사
	  	storeRepository.save(store);
  	}

  	@Transactional
  	public void update(StoreDto storeDto) {
  		Store store = findStoreEntityById(storeDto.getId());
  		store.updater()
  			.name(storeDto.getName())
  			.address(storeDto.getAddress())
  			.zipcode(storeDto.getZipcode())
  			.update();
  	}

  	@Transactional
  	public void delete (Long storeId) {
  		Store store = findStoreEntityById(storeId);
    	storeRepository.delete(store);
  	}

  	public List<StoreDto> findAll() {
  		return storeRepository
		  .findAll()
		  .stream()
		  .map(StoreDto::new)
		  .collect(Collectors.toList());
  	}

  	public StoreDto findById(Long storeId) {
  		Store store = findStoreEntityById(storeId);
    	return new StoreDto(store);
  	}

  	@Transactional
  	public void addAndRegistRestaurantType(Long storeId, RestaurantType.Name restaruantTypeName) {
  		Store store = findStoreEntityById(storeId);
  		RestaurantType restaurantType = new RestaurantType(restaruantTypeName);
  		restaurantTypeRepository.save(restaurantType);
  		StoreRestaurantType storeRestaurantType = new StoreRestaurantType(store, restaurantType);
  		storeRestaurantTypeRepository.save(storeRestaurantType);
 		store.updater().addStoreRestaurantType(storeRestaurantType).update();
  	}
  	
	private Store findStoreEntityById(Long storeId) {
		return storeRepository
			.findById(storeId)
			.orElseThrow(() -> 
				new EntityNotFoundException(STORE_ENTITY_NOT_FOUND.getMessage()));
	}

}
