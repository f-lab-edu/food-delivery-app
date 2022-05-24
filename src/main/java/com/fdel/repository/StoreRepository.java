package com.fdel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fdel.entity.Store;

@Repository
public interface StoreRepository 
		extends JpaRepository<Store, Long>{
}
