package com.fdel.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class StoreCategory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "category_id")
  private Long id;
  
  @Enumerated(EnumType.STRING)
  private Name name;

  @OneToMany(mappedBy = "storeCategory")
  private List<StoreStoreCategory> storeStoreCategoryList = new ArrayList<>();
  
  public StoreCategory(Name name) {
	  this.name = name;
  }
  
  public Name getName() {
	  return name;
  }

  public static enum Name {
	  CAFE_DESSERT("카페/디저트"),
	  PORK_CURLET_SASHIMI_JAPANESE("돈까스/회/일식"),
	  CKICKEN("치킨"),
	  RICE_PORRIDGE_NOODLE("백반/죽/국수"),
	  PIZZA("피자");
	  
	  String name;
	  
	  private Name(String name) {
		  this.name = name;
	  }
	  
	  public String getName() {
		  return name;
	  }
  }

}
