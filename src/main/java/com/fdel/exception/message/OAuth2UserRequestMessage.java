package com.fdel.exception.message;

public enum OAuth2UserRequestMessage {
	NOT_SUPPORTED_PROVIDER_REQUEST("지원하는 provider의 요청이 아닙니다.");
	
	private String message;
	
	private OAuth2UserRequestMessage(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
}
