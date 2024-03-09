package com.fdel.entity;

import static com.fdel.exception.message.EntityMessage.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fdel.dto.cart.MenuWantToPurchaseDto;
import com.fdel.exception.domain.IdAlreadyAssignedException;

import lombok.EqualsAndHashCode;


public class Cart {

	//user의 session-id를 id로 사용
	private final String id;
	
	/*
	 * menu 정보를 string 으로 저장
	 * key : menuId
	 * value : (menuId를 제외한) CartMenuDto 필드 정보
	 */
	private Map<String, String> menuMap;
	
	public Cart(String sessionId) {
		this.id = sessionId;
		menuMap = new HashMap<>();
	}
	
	public void setMenuMap(Map<String, String> menuMap) {
		this.menuMap = menuMap;
	}
	
	//기존에 같은 메뉴의 정보가 있으면 덮어쓰기 합니다.
	public void putMenu(CartMenuDto cartMenuDto, ObjectMapper objectMapper) 
			throws JsonProcessingException {
		String menuId = cartMenuDto.id.toString();
		menuMap.put(menuId, objectMapper.writeValueAsString(cartMenuDto));
	}
	
	public void emptyCart() {
		menuMap = new HashMap<>();
	}
	
	public boolean isCartEmpty() {
		return menuMap.isEmpty();
	}
	
	public String getId() {
		return id;
	}
	
	public void deleteMenu(Long menuId) {
		menuMap.remove(menuId.toString());
	}
	
	public Map<String, String> getMenuMapView(){
		return Collections.unmodifiableMap(menuMap);
	}
	
	public List<CartMenuDto> getMenuListView(ObjectMapper objectMapper) 
		throws JsonMappingException, JsonProcessingException {
		List<CartMenuDto> menuList = new ArrayList<>();
		Set<Entry<String, String>> menuSet = menuMap.entrySet();
		for(Entry<String,String> menuEntry : menuSet) {
			CartMenuDto cartMenuDto = 
				objectMapper.readValue(menuEntry.getValue(), CartMenuDto.class);
			cartMenuDto.setId(Long.valueOf(menuEntry.getKey()));
			menuList.add(cartMenuDto);
		}
		return Collections.unmodifiableList(menuList);
	}
	

	
	//장바구니에 담긴 메뉴 정보 DTO 입니다.
	@EqualsAndHashCode
	public static class CartMenuDto {

		@JsonIgnore //id는 json 정보에서 제외
		private Long id;
		
		private String name;
		
		private Integer price;
		
		private Integer numberOfWantToBuy;
		
		
		/* 
		 * JSON 정보를 역직렬화 할 때 Reflection 기술을 사용해 객체를 생성하는데
		 * Refelction을 사용해서 객체를 생성하기 위해서는 기본 생성자 필요
		 * 단 접근 제어자는 private이라도 상관없다.
		 */
		private CartMenuDto() {}
		
		public CartMenuDto(Long id) {
			this.id = id;
		}
		
		//Menu 엔터티의 정보를 사용해서 생성합니다.
		public CartMenuDto(Menu menuInfo, Integer numberOfWantToBuy) {
			this.id = menuInfo.getId();
			this.name = menuInfo.getName();
			this.price = menuInfo.getPrice();
			this.numberOfWantToBuy = numberOfWantToBuy;
		}
		
		//view DTO를 받아서 CartMenuDto 생성
		public static CartMenuDto of(MenuWantToPurchaseDto menuWantToPurchaseDto) {
			CartMenuDto cartMenuDto = new CartMenuDto(menuWantToPurchaseDto.getId());
			cartMenuDto.setName(menuWantToPurchaseDto.getName());
			cartMenuDto.setPrice(menuWantToPurchaseDto.getPrice());
			cartMenuDto.setNumberOfWantToBuy(menuWantToPurchaseDto.getNumberOfWantToBuy());
			return cartMenuDto;
		}
		
		public MenuWantToPurchaseDto toMenuWantToPurchaseDto() {
			MenuWantToPurchaseDto menuWantToPurchaseDto = new MenuWantToPurchaseDto(id);
			menuWantToPurchaseDto.setName(name);
			menuWantToPurchaseDto.setPrice(price);
			menuWantToPurchaseDto.setNumberOfWantToBuy(numberOfWantToBuy);
			return menuWantToPurchaseDto;
		}

		public Long getId() {
			return id;
		}

		public String getName() {
			return name;
		}

		public Integer getPrice() {
			return price;
		}

		public Integer getNumberOfWantToBuy() {
			return numberOfWantToBuy;
		}
		
		/*
		 * 장바구니에서 역직렬화하여 만든 CartMenuDto객체에 id를 주입하기 위해 사용
		 * (장바구니 entry의 value 값에는 id 필드가 들어가지 않는다.
		 * 따라서 entry의 value를 역직렬화 했을 때 id가 없는 CartMenuDto가 만들어진다.)
		 */
		public void setId(Long id) {
			if(this.id != null) {
				throw new IdAlreadyAssignedException(ID_ALREADY_EXISTS.getMessage());
			}
			this.id = id;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setPrice(Integer price) {
			this.price = price;
		}

		public void setNumberOfWantToBuy(Integer numberOfWantToBuy) {
			this.numberOfWantToBuy = numberOfWantToBuy;
		}
		
	}
}
