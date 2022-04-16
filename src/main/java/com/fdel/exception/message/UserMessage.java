package com.fdel.exception.message;

public enum UserMessage {
	USER_NOT_FOUND_MATCHING_THE_USERNAME("이름이 일치하는 유저를 찾을 수 없습니다."),
	INTEGRITY_OF_THE_USER_HAS_BEEN_VIOLATED("유저 엔터티의 무결성이 위배되었습니다.");
	
	private String message;
	
	private UserMessage(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
}
