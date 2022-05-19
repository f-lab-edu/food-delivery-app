package com.fdel.exception.message;

public enum MenuMessage {

	MENU_ENTITY_NOT_FOUND("Menu Entity를 찾을 수 없습니다."),
	NOT_ENOUGH_STOCK("재고량이 충분하지 않습니다.");
	
	private final String message;
	
	MenuMessage(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
}
