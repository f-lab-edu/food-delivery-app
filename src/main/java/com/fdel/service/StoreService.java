package com.fdel.service;

import static com.fdel.exception.message.StoreMessage.*;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fdel.dto.store.StoreDto;
import com.fdel.entity.Store;
import com.fdel.repository.StoreRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StoreService {

	private final StoreRepository storeRepository;

  	@Transactional
  	public void regist(StoreDto storeDto) {
  		Store store = storeDto.toEntity();
  		store.init(); //영속화 전 초기화 및 무결성 검사
	  	storeRepository.save(store);
  	}

  	@Transactional
  	public void update(StoreDto storeDto) {
  		Store store = storeRepository
			.findById(storeDto.getId())
			.orElseThrow(() -> 
				new EntityNotFoundException(STORE_ENTITY_NOT_FOUND.getMessage()));
  		store.update(storeDto);
  	}

  	@Transactional
  	public void delete (Long storeId) {
  		Store store = storeRepository
			.findById(storeId)
			.orElseThrow(() -> 
        		new EntityNotFoundException(STORE_ENTITY_NOT_FOUND.getMessage()));
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
  		Store store = storeRepository
			.findById(storeId)
			.orElseThrow(() -> 
				new EntityNotFoundException(STORE_ENTITY_NOT_FOUND.getMessage()));
    	return new StoreDto(store);
  	}

}
