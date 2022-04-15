package com.fdel.applicationservice;

import org.springframework.stereotype.Service;

import com.fdel.entity.User;
import com.fdel.repository.UserRepository;

import lombok.RequiredArgsConstructor;

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
