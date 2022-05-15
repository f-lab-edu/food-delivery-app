package com.fdel.entity;

import static com.fdel.exception.message.MenuMessage.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fdel.dto.menu.MenuDto;
import com.fdel.exception.domain.menu.NotEnoughStockException;
import static com.fdel.exception.message.MenuMessage.*;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Menu extends BaseTimeEntity{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "menu_id")
  private Long id;

  private String name;
  private Integer price;
  private Integer stockQuantity;

  @Builder
  public Menu(Long id, String name, Integer price, Integer stockQuantity) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.stockQuantity = stockQuantity;
  }
  
  public void addStock(Integer quantity) {
    this.stockQuantity += quantity;
  }
  
  public void removeStock(Integer quantity) {
	Integer remain = stockQuantity - quantity; 
    if (remain < 0) {
    	throw new NotEnoughStockException(NOT_ENOUGH_STOCK.getMessage() + " 남은양 : " + remain);
    }
    this.stockQuantity = remain;
  }

  public void update(String name, Integer price, Integer stockQuantity) {
    this.name = name;
    this.price = price;
    this.stockQuantity = stockQuantity;
  }
  
}
