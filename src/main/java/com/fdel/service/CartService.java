package com.fdel.service;

import static com.fdel.exception.message.CartMessage.*;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fdel.dto.cart.MenuWantToPurchaseDto;
import com.fdel.entity.Cart;
import com.fdel.entity.Cart.CartMenuDto;
import com.fdel.exception.domain.cart.CartIsEmptyException;
import com.fdel.exception.domain.cart.CartNotFoundException;
import com.fdel.repository.CartRedisRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {

	private final CartRedisRepository cartRepository;
	private final ObjectMapper objectMapper;
	
	/**
	 * 해당 세션 ID의 장바구니에 주문하려는 메뉴 정보를 추가합니다.
	 * 
	 * @param menuWantToPurchaseDto 주문자가 장바구니에 담은 메뉴 정보
	 * @param session
	 */
	public void addOrUpdateMenuOfCart(
			MenuWantToPurchaseDto menuWantToPurchaseDto, 
			HttpSession session) {
		
		Cart cart;
		String sessionId = session.getId();
		
		try {
			cart = cartRepository.find(sessionId);
		} catch (CartNotFoundException e) { //만약 만료된 카트라면 새롭게 만들어줍니다.
			cart = new Cart(sessionId);
		}
		
		CartMenuDto cartMenuDto = CartMenuDto.of(menuWantToPurchaseDto);
		
		try {
			//기존에 같은 메뉴의 정보가 있으면 덮어쓰기 합니다.
			cart.putMenu(cartMenuDto, objectMapper);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(FAILED_TO_CONVERT_CAR_INFO_TO_JSON.getMessage(), e);
		}
		
		try {
			cartRepository.save(cart);
		} catch (CartIsEmptyException e) {
			//메뉴가 빈 카트라면 저장할 필요가 없다.
			return;
		}
	}
	
	/**
	 * @param session 
	 * @return 카트에 담긴 메뉴 정보 리스트를 반환, 카트가 비어있다면 빈 리스트를 반환
	 */
	public List<MenuWantToPurchaseDto> getCartMenuList(HttpSession session){
		String sessionId = session.getId();
		Cart findCart;
		
		try {
			findCart = cartRepository.find(sessionId);
		} catch (CartNotFoundException e) {
			//장바구니가 만료되었다면 빈 카트를 생성한다.
			findCart = new Cart(sessionId);
		}
		
		List<CartMenuDto> menuListView;
		try {
			menuListView = findCart.getMenuListView(objectMapper);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(FAILED_TO_CONVERT_CAR_INFO_TO_JSON.getMessage(), e);
		}
		
		return menuListView.stream()
			.map(e->e.toMenuWantToPurchaseDto())
			.collect(Collectors.toList());
	}
	
	/**
	 * Redis에서 장바구니를 삭제합니다.
	 * 
	 * @param httpSession
	 */
	public void clearCart(HttpSession httpSession) {
		cartRepository.clear(httpSession.getId());
	}
	
	public void deleteCartMenu(HttpSession httpSession, Long menuId) {
		
		try {
			Cart findCart = cartRepository.find(httpSession.getId());
			findCart.deleteMenu(menuId);
			cartRepository.save(findCart); //장바구니가 있으면 덮어쓴다.
		} catch (CartIsEmptyException e) {
			//메뉴가 비었으므로 카트 자체를 redis에서 삭제한다.
			cartRepository.clear(httpSession.getId());
		} catch (CartNotFoundException e) {
			//장바구니가 만료되었을 경우 삭제할 메뉴가 없으므로 return
			return;
		}
	}
	
}
