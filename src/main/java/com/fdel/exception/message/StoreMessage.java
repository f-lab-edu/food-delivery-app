package com.fdel.exception.message;

public enum StoreMessage {

	STORE_ENTITY_NOT_FOUND("Store Entity를 찾을 수 없습니다."),
	INTEGRITY_OF_THE_STORE_HAS_BEEN_VIOLATED("Store Entity의 무결성이 위배되었습니다."),
	SAME_CATEGORY_ALREADY_EXISTS("같은 카테고리가 이미 존재합니다.");
	
	private String message;
	
	private StoreMessage(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
}
