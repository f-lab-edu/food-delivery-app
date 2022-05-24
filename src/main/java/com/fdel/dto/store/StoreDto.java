package com.fdel.dto.store;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import com.fdel.entity.Store;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StoreDto {
	
	private Long id;
	@NotBlank
	private String name;
	@NotBlank
	private String address;
	@Positive
	private Integer zipcode;
	
	public StoreDto(Store store) {
		this.id = store.getId();
		this.name = store.getName();
		this.address = store.getAddress();
		this.zipcode = store.getZipcode();
	}
	
	@Builder
	public StoreDto(Long id, String name, String address, Integer zipcode) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.zipcode = zipcode;
	}
	
	public Store toEntity() {
		return Store.builder()
	        .name(name)
	        .address(address)
	        .zipcode(zipcode)
	        .build();
	}
	
}
