package com.fdel.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreRestaurantType {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	  
	@NotNull
	@ManyToOne
	@JoinColumn(name = "store_id")
	private Store store;
	  
	@NotNull
	@ManyToOne
	@JoinColumn(name = "restaurant_type_id")
	private RestaurantType restaurantType;
	  
	public StoreRestaurantType(Store store, RestaurantType restaurantType) {
		this.store = store;
		this.restaurantType = restaurantType;
	}
	  
	public Store getStore() {
		return store;
	}
	  
	public RestaurantType getStoreCategory() {
		return restaurantType;
	}
	  
}
