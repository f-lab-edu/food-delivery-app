package com.fdel.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class StoreStoreCategory {
	
	  @Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  @Column(name = "store_storecategory_id")
	  private Long id;
	  
	  @NotNull
	  @ManyToOne
	  @JoinColumn(name = "store_id")
	  private Store store;
	  
	  @NotNull
	  @ManyToOne
	  @JoinColumn(name = "storecategory_id")
	  private StoreCategory storeCategory;
	  
	  public StoreStoreCategory(Store store, StoreCategory storeCategory) {
		  this.store = store;
		  this.storeCategory = storeCategory;
	  }
	  
	  public Store getStore() {
		  return store;
	  }
	  
	  public StoreCategory getStoreCategory() {
		  return storeCategory;
	  }
	  
}
