package com.fdel.service;

import static com.fdel.exception.message.StoreMessage.*;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fdel.dto.store.StoreDto;
import com.fdel.entity.Store;
import com.fdel.entity.StoreCategory;
import com.fdel.entity.StoreStoreCategory;
import com.fdel.repository.StoreCategoryRepository;
import com.fdel.repository.StoreRepository;
import com.fdel.repository.StoreStoreCategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StoreService {

	private final StoreRepository storeRepository;
	private final StoreCategoryRepository storeCategoryRepository;
	private final StoreStoreCategoryRepository storeStoreCategoryRepository;

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
  	public void addAndRegistCategory(Long storeId, StoreCategory.Name categoryname) {
  		Store store = findStoreEntityById(storeId);
  		List<StoreStoreCategory> storeStoreCategoryList = store.getStoreStoreCategoryList();
  		//같은 카테고리가 이미 존재하면 예외를 발생시킵니다.
  		storeStoreCategoryList.stream()
  			.filter(e->e.getStoreCategory()
				.getName()
				.equals(categoryname))
  			.findAny().ifPresent(e->{throw new IllegalStateException(SAME_CATEGORY_ALREADY_EXISTS.getMessage());});
  		StoreCategory storeCategory = new StoreCategory(categoryname);
  		storeCategoryRepository.save(storeCategory);
  		StoreStoreCategory storeStoreCategory = new StoreStoreCategory(store, storeCategory);
  		storeStoreCategoryRepository.save(storeStoreCategory);
  	}
  	
	private Store findStoreEntityById(Long storeId) {
		return storeRepository
			.findById(storeId)
			.orElseThrow(() -> 
				new EntityNotFoundException(STORE_ENTITY_NOT_FOUND.getMessage()));
	}

}
