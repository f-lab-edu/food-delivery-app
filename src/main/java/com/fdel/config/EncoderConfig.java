package com.fdel.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 유저 비밀번호를 인코딩할 인코더를 설정합니다.
 */
@Configuration
public class EncoderConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {		
		return new BCryptPasswordEncoder();
	}
}
