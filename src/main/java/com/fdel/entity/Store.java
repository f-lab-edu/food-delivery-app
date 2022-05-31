package com.fdel.entity;

import static com.fdel.exception.message.StoreMessage.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.nimbusds.oauth2.sdk.util.StringUtils;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor //이게 없으면 org.springframework.orm.jpa.JpaSystemException: No default constructor for entity 애러 발생
public class Store extends BaseTimeEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "store_id")
	private Long id;
	
	private String name;
	private String address;
	private Integer zipcode;
	
	@OneToMany(mappedBy = "store")
	private List<StoreStoreCategory> storeStoreCategoryList = new ArrayList<>();
  
	@Builder
	public Store(Long id, String name, String address, Integer zipcode) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.zipcode = zipcode;
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
	
	private void setName(String name) {
		this.name = name;
	}
	private void setAddress(String address) {
		this.address = address;
	}
	private void setZipcode(Integer zipcode) {
		this.zipcode = zipcode;
	}
	
	public Updater updater() {
		return new Updater(this);
	}
	
	/*
	 * 업데이트가 필요한 필드에 따라
	 * 동적으로 update api를 제공하기 위해서
	 * updater를 만들었습니다.
	 */
	public class Updater {
		private Store store;
		private String name;
		private String address;
		private Integer zipcode;
		
		private Updater(Store store) {
			this.store = store;
		}
		
		public Updater name(String name) {
			this.name = name;
			return this;
		}
		public Updater address(String address) {
			this.address = address;
			return this;
		}
		public Updater zipcode(Integer zipcode) {
			this.zipcode = zipcode;
			return this;
		}
		
		public void update() {
			store.setName(name);
			store.setAddress(address);
			store.setZipcode(zipcode);
			store.validateIntegrity();
		}
	}

}
