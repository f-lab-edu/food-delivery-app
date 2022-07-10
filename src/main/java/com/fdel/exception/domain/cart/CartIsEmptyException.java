package com.fdel.exception.domain.cart;

public class CartIsEmptyException extends Exception {
	public CartIsEmptyException(String message) {
		super(message);
	}
}
