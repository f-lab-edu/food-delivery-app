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

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store extends BaseTimeEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	private String address;
	private Integer zipcode;
	
	@OneToMany(mappedBy = "store")
	private List<StoreRestaurantType> storeRestaurantTypeList = new ArrayList<>();
  
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
		if(StringUtils.isBlank(name)) {
			throw new IllegalStateException(
					INTEGRITY_OF_THE_STORE_HAS_BEEN_VIOLATED.getMessage() 
					+ "name : " + name);
		} else if (StringUtils.isBlank(address)) {
			throw new IllegalStateException(
					INTEGRITY_OF_THE_STORE_HAS_BEEN_VIOLATED.getMessage() 
					+ "address : " + address);
		} else if (zipcode <= 0) {
			throw new IllegalStateException(
					INTEGRITY_OF_THE_STORE_HAS_BEEN_VIOLATED.getMessage() 
					+ "zipcode : " + zipcode);
		}		
	}
	
	/**
	 * 가지고 있는 카테고리인지 검증합니다. 
	 * 만약 같은 카테고리를 이미 가지고 있다면 예외를 발생시킵니다.
	 * 
	 * @param typeName RestaruantType.Name 타입의 enum 객체가 파라미터 값으로 입력됩니다.
	 * @throws IllegalStateException
	 */
	private void checkItIsAnExistingRestaurantType(RestaurantType restaurantType) {
		//같은 카테고리가 이미 존재하면 예외를 발생시킵니다.
  		storeRestaurantTypeList.stream()
  			.filter(e->e.getStoreCategory()
				.getName()
				.equals(restaurantType.getName()))
  			.findAny().ifPresent(e->{throw new IllegalStateException(SAME_CATEGORY_ALREADY_EXISTS.getMessage());});
	}
	
	/**
	 * menu 객체가 영속화되기 전에 초기화하고
	 * 무결성 검사를 합니다.
	 */
	public void init() {
		validateIntegrity();
	}
	/*
	 * private setter
	 */
	private void setName(String name) {
		this.name = name;
	}
	private void setAddress(String address) {
		this.address = address;
	}
	private void setZipcode(Integer zipcode) {
		this.zipcode = zipcode;
	}
	private void setStoreRestaurantTypeList(List<StoreRestaurantType> storeRestaurantTypeList) {
		this.storeRestaurantTypeList = storeRestaurantTypeList;
	}
	private void addStoreRestaurantTypeList(List<StoreRestaurantType> storeRestaurantTypeList) {
		this.storeRestaurantTypeList.addAll(storeRestaurantTypeList);
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
		
		private List<StoreRestaurantType> storeRestaurantTypeList;
		boolean isInitStoreRestaurantTypeList = false;
		
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
		public Updater initStroeRestaurantTypeList() {
			isInitStoreRestaurantTypeList = true;
			return this;
		}
		public Updater addStoreRestaurantType(StoreRestaurantType storeRestaurantType) {
			
			store.checkItIsAnExistingRestaurantType(storeRestaurantType.getStoreCategory());
			if(storeRestaurantTypeList==null) {
				storeRestaurantTypeList = new ArrayList<>();
			}
			storeRestaurantTypeList.add(storeRestaurantType);
			return this;
		}
		public void update() {
			if(name != null) {
				store.setName(name);
			}
			if(address != null) {
				store.setAddress(address);
			}
			if(zipcode != null) {
				store.setZipcode(zipcode);
			}
			if(storeRestaurantTypeList != null) {
				if(isInitStoreRestaurantTypeList) {
					store.setStoreRestaurantTypeList(storeRestaurantTypeList);
				} else {
					store.addStoreRestaurantTypeList(storeRestaurantTypeList);
				}
			}
			store.validateIntegrity();
		}
	}
	
	/*
	 * getter
	 */
	public Long getId() {
		return id;
	}

	public List<StoreRestaurantType> getStoreRestaurantTypeList() {
		return storeRestaurantTypeList;
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public Integer getZipcode() {
		return zipcode;
	}
	
}
