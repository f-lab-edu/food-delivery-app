package com.fdel.exception.domain.user;

import com.fdel.exception.authentication.CustomAuthenticationException;

/**
 * 유저를 찾지 못할 때 발생하는 예외다.
 */
public class UserNotFoundException extends CustomAuthenticationException {
	public UserNotFoundException(String message) {
		super(message);
	}
}
