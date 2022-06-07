package com.fdel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fdel.entity.RestaurantType;

public interface RestaurantTypeRepository 
		extends JpaRepository<RestaurantType, Long>{
}
