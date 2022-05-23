package com.fdel.exception.message;

public enum SimpleMessage {
	NO_MATCHING_TYPES_FOUND("일치하는 타입이 없습니다.");
	
	private final String message;
	
	SimpleMessage(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
}
