package com.fdel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fdel.entity.StoreRestaurantType;

public interface StoreRestaurantTypeRepository 
		extends JpaRepository<StoreRestaurantType, Long> {
}
