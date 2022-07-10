package com.fdel.repository;

import com.fdel.entity.Cart;
import com.fdel.exception.domain.cart.CartIsEmptyException;
import com.fdel.exception.domain.cart.CartNotFoundException;

public interface CartRepository {

	void save(Cart cart) throws CartNotFoundException, CartIsEmptyException;

	Cart find(String sessionId) throws CartNotFoundException;
	
	void clear(String sessionId);
}
