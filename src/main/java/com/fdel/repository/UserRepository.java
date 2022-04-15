package com.fdel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fdel.entity.User;

public interface UserRepository 
		extends JpaRepository<User, Integer>{
	public User findByUsername(String username); //Jpa Query methods
}
