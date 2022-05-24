package com.fdel.dto.menu;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import com.fdel.entity.Menu;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MenuDto {

	private Long id;
	@NotBlank
	private String name;
	@PositiveOrZero
	private Integer price;
	@Positive
	private Integer stockQuantity;
	
	public MenuDto(Menu menu) {
		this.id = menu.getId();
		this.name = menu.getName();
		this.price = menu.getPrice();
		this.stockQuantity = menu.getStockQuantity();
	}
	
	@Builder
	public MenuDto(Long id, String name, Integer price, Integer stockQuantity) {
		this.id = id;  
	    this.name = name;
	    this.price = price;
	    this.stockQuantity = stockQuantity;
	}

	public Menu toEntity() {
		return Menu.builder()
	        .name(name)
	        .price(price)
	        .stockQuantity(stockQuantity)
	        .build();
	}
}
