package com.fdel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fdel.entity.Menu;

public interface MenuRepository 
		extends JpaRepository<Menu, Long> {
}
