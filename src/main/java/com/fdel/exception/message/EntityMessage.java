package com.fdel.exception.message;

public enum EntityMessage {
	
	ID_ALREADY_EXISTS("id가 이미 존재합니다.");
	
	private String message;
	
	private EntityMessage(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
}
