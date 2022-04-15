package com.fdel.applicationservice;

import org.springframework.stereotype.Service;

import com.fdel.entity.User;

@Service
public interface UserApplicationService {
	void regist(User user);
	User findByUserName(String userName);
}
