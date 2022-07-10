package com.fdel.dto.cart;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import lombok.EqualsAndHashCode;

//구매하고 싶은 메뉴 정보를 전달합니다.
@EqualsAndHashCode
public class MenuWantToPurchaseDto {
	
	private Long id;
	@NotBlank
	private String name;
	@PositiveOrZero
	private Integer price;
	@Positive
	private Integer numberOfWantToBuy;
	
	/* 
	 * JSON 정보를 역직렬화 할 때 Reflection 기술을 사용해 객체를 생성하는데
	 * Refelction을 사용해서 객체를 생성하기 위해서는 기본 생성자 필요
	 * 단 접근 제어자는 private이라도 상관없다.
	 */
	private MenuWantToPurchaseDto() {
	}
	
	public MenuWantToPurchaseDto(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public Integer getPrice() {
		return price;
	}
	
	public void setPrice(Integer price) {
		this.price = price;
	}
	
	public Integer getNumberOfWantToBuy() {
		return numberOfWantToBuy;
	}
	
	public void setNumberOfWantToBuy(Integer numberOfWantToBuy) {
		this.numberOfWantToBuy = numberOfWantToBuy;
	}
	
}
