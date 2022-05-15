package com.fdel.exception.domain.menu;

public class NotEnoughStockException extends RuntimeException{
	public NotEnoughStockException(String message) {
		super(message);
	}
}
