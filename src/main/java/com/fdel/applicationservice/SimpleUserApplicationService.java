package com.fdel.applicationservice;

import org.springframework.stereotype.Service;

import com.fdel.entity.User;
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

	@Override
	public User findByUserName(String userName) {
		return userRepository.findByUsername(userName);
	}
}
