package com.fdel.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fdel.entity.User;
import com.fdel.exception.message.UserMessage;
import com.fdel.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * 유저와 관련된 가장 기본적인 어플리케이션 서비스를 제공한다.
 */
@Service
@RequiredArgsConstructor
public class SimpleUserApplicationService implements UserApplicationService{

	private final UserRepository userRepository;

	@Override
	public void regist(User user) {
		userRepository.save(user);
	}

	/*
	 * null User 처리에 대한 주의점
	 * OAuth2 로그인시 UserApplicationService.findByUserName이 호출되어서
	 * DB에 유저가 등록되어 있는지 확인하고, 가입되지 않은 사용자라면 가입시키는 로직이 존재한다.
	 * 따라서 유저가 없을 경우 UserApplicationService에서(여기에서) 예외를 발생시키고 싶다면 
	 * 그 예외를 PrincipalOauth2UserService에서 잡아서 user를 null인 경우의 처리를 해줘야한다.
	 * 
	 * 하지만 예외를 발생시키는 것보다 user가 null인 경우 빈optional 객체를 반환하고 
	 * 그것에 대한 처리를 PrincipalOauth2UserService에서 해주는 것이 자연스럽다.
	 * 따라서 repository에서 받은 optional 객체 그대로 반환해주도록 한다.
	 */
	@Override
	public Optional<User> findByUserName(String username) {
		return userRepository.findByUsername(username);
	}
}
