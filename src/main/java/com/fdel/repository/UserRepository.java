package com.fdel.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fdel.entity.User;

/**
 * JpaRepository를 구현한 User entity 저장소입니다.
 * 인터페이스지만 spring data jpa에 의해 구체타입으로 변환됩니다.
 * 쿼리는 메서드 이름을 통해 약속된 방식으로 만들어집니다. 
 */
public interface UserRepository 
		extends JpaRepository<User, Integer>{
	
	Optional<User> findByUsername(String username); //Jpa Query methods
}
