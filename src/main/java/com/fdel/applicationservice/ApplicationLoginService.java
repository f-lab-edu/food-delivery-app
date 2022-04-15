package com.fdel.applicationservice;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fdel.applicationservice.auth.provider.Provider;
import com.fdel.controller.dto.JoinDto;
import com.fdel.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApplicationLoginService {
	
	private final PasswordEncoder passwordEncoder;
	private final UserApplicationService userApplicationService;
	
	public void registByJoinDto(JoinDto joinDto) {
		User user = joinDto.toUser(passwordEncoder);
		user.setProvider(Provider.NONE);
		user.validateIntegrity();
		userApplicationService.regist(user);
	}

}
