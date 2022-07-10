package com.fdel.exception.domain;

public class IdAlreadyAssignedException extends RuntimeException{
	public IdAlreadyAssignedException(String message) {
		super(message);
	}
}
