package com.fdel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fdel.entity.Store;

public interface StoreRepository 
		extends JpaRepository<Store, Long>{
}
