package com.fdel.exception.message.authentication;

public enum ProviderMessage {
	
	NOT_OAUTH2_PROVIDER("OAuth2 프로바이더가 아닙니다.");
	
	private final String message;
	
	ProviderMessage(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
}
