package com.fdel.entity;

import static com.fdel.exception.message.EntityMessage.*;
import static com.fdel.exception.message.StoreMessage.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fdel.dto.store.StoreDto;
import com.nimbusds.oauth2.sdk.util.StringUtils;

import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
public class Store extends BaseTimeEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "store_id")
	private Long id;
	
	private String name;
	private String address;
	private Integer zipcode;
  
	@Builder
	public Store(Long id, String name, String address, Integer zipcode) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.zipcode = zipcode;
		validateIntegrity();
	}
	
	public void update(StoreDto storeDto) {
		this.name = storeDto.getName();
		this.address = storeDto.getAddress();
		this.zipcode = storeDto.getZipcode();
		validateIntegrity();
	}
	
  	/**
  	 * 스스로 각 필드의 무결성을 검증합니다.
  	 */
	private void validateIntegrity() {
		if(StringUtils.isBlank(name)
				||StringUtils.isBlank(address)
				||zipcode <= 0) {
			throw new IllegalStateException(
				INTEGRITY_OF_THE_STORE_HAS_BEEN_VIOLATED.getMessage());
		}
				
	}
	
	/**
	 * menu 객체가 영속화되기 전에 초기화하고
	 * 무결성 검사를 합니다.
	 */
	public void init() {
		validateIntegrity();
	}
	
	//테스트를 위해 추가
	public void setId(Long id) {
		if(this.id != null) {
			throw new IllegalStateException(ID_ALREADY_EXISTS.getMessage());
		}
		this.id = id;
	}

}
