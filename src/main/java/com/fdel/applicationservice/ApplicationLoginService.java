package com.fdel.applicationservice;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fdel.applicationservice.auth.provider.Provider;
import com.fdel.controller.dto.JoinDto;
import com.fdel.entity.User;

import lombok.RequiredArgsConstructor;

/**
 * 가입 화면으로 부터 받은 form 정보를 받아 
 * 어플리케이션 가입 서비스를 제공한다.
 */
@Service
@RequiredArgsConstructor
public class ApplicationLoginService {
	
	private final PasswordEncoder passwordEncoder;
	private final UserApplicationService userApplicationService;
	
	/**
	 * 가입 form으로부터 받아온 dto객체를 받아서 유저를 어플리케이션에 등록한다.
	 * 
	 * @param joinDto form으로부터 받아온 정보다.
	 */
	public void registByJoinDto(JoinDto joinDto) {
		User user = joinDto.toUser(passwordEncoder);
		user.setProvider(Provider.NONE);
		user.validateIntegrity();
		userApplicationService.regist(user);
	}

}
