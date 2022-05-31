package com.fdel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fdel.entity.StoreStoreCategory;

public interface StoreStoreCategoryRepository 
		extends JpaRepository<StoreStoreCategory, Long> {
}
