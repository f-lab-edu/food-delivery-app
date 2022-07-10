package com.fdel.controller;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fdel.dto.cart.MenuWantToPurchaseDto;
import com.fdel.service.CartService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/cart")
@RequiredArgsConstructor
@RestController
public class CartController {

	private final CartService cartService;
	
	@GetMapping("/menus")
	public List<MenuWantToPurchaseDto> getCart(HttpSession session) {
		return cartService.getCartMenuList(session);
	}
	
	@PatchMapping("/menus")
	public void addMenu(
			@Valid @RequestBody MenuWantToPurchaseDto menuWantToPurchaseDto, 
			HttpSession session) {
		cartService.addOrUpdateMenuOfCart(menuWantToPurchaseDto, session);
	}
	
	@DeleteMapping("/menus")
	public void clearCart(HttpSession session) {
		cartService.clearCart(session);
	}
	
	@DeleteMapping("/menus/{menuId}")
	public void deleteCartMenu(HttpSession session, @PathVariable long menuId) {
		cartService.deleteCartMenu(session, menuId);
	}
	
}
