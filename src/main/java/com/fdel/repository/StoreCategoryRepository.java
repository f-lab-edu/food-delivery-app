package com.fdel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fdel.entity.StoreCategory;

public interface StoreCategoryRepository 
		extends JpaRepository<StoreCategory, Long>{
}
