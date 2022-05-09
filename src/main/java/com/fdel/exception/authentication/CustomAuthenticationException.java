package com.fdel.exception.authentication;


/* 
 * 처음에는 시큐리티에서 제공하는 AuthenticationException을 사용하였다.
 * 하지만 예외가 발생하면 라이브러리에 있는 AuthenticationException 캐치문에서 예외가 먹혀버려서
 * 예외가 발생했을 때 어디서 문제가 있는지 확인이 어려웠다.
 * 그래서 RuntimeException을 상속한 CustomAuthenticationException을 만들어서 사용한다.
 */
public class CustomAuthenticationException extends RuntimeException{
	public CustomAuthenticationException(String message) {
		super(message);
	}
}
