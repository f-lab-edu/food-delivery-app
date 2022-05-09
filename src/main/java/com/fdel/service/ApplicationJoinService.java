package com.fdel.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fdel.controller.requestdto.JoinDto;
import com.fdel.entity.User;
import com.fdel.service.auth.provider.Provider;

import lombok.RequiredArgsConstructor;

/**
 * 가입 화면으로 부터 받은 form 정보를 받아 
 * 어플리케이션 가입 서비스를 제공한다.
 */
@Service
@RequiredArgsConstructor
public class ApplicationJoinService {
	
	private final PasswordEncoder passwordEncoder;
	private final UserApplicationService userApplicationService;
	
	/**
	 * 가입 form으로부터 받아온 dto객체를 받아서 유저를 어플리케이션에 등록한다.
	 * 
	 * @param joinDto form으로부터 받아온 정보다.
	 */
	public void regist(JoinDto joinDto) {
		User user = joinDto.toUser(passwordEncoder);
		user.init(Provider.NONE);
		userApplicationService.regist(user);
	}
	
}
