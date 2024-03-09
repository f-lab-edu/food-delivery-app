package com.fdel.exception.message;

public enum CartMessage {
	
	FAILED_TO_CONVERT_CAR_INFO_TO_JSON("장바구니 정보를 JSON으로 변환하지 못했습니다."),
	CART_IS_EMPTY("카트가 비었습니다."),
	CART_NOT_FOUND("카트가 만료되었거나 존재하지 않습니다.");
	
	private String message;
	
	private CartMessage(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	
}
