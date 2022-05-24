package com.fdel.entity;

import static com.fdel.exception.message.MenuMessage.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fdel.exception.domain.menu.NotEnoughStockException;
import com.fdel.exception.message.MenuMessage;
import com.nimbusds.oauth2.sdk.util.StringUtils;

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
    validateIntegrity();
  }
  
  public void addStock(Integer quantity) {
    this.stockQuantity += quantity;
    validateIntegrity();
  }
  
  public void removeStock(Integer quantity) {
	Integer remain = stockQuantity - quantity; 
    if (remain < 0) {
    	throw new NotEnoughStockException(NOT_ENOUGH_STOCK.getMessage() + " 남은양 : " + stockQuantity);
    }
    this.stockQuantity = remain;
  }

  public void update(String name, Integer price, Integer stockQuantity) {
    this.name = name;
    this.price = price;
    this.stockQuantity = stockQuantity;
    validateIntegrity();
  }

  	/**
  	 * 스스로 각 필드의 무결성을 검증합니다.
  	 */
	private void validateIntegrity() {
		if(StringUtils.isBlank(name)
				||price < 0
				||stockQuantity < 0) {
			throw new IllegalStateException(
				MenuMessage.INTEGRITY_OF_THE_MENU_HAS_BEEN_VIOLATED.getMessage());
		}
				
	}
	
	/**
	 * menu 객체가 영속화되기 전에 초기화하고
	 * 무결성 검사를 합니다.
	 */
	public void init() {
		validateIntegrity();
	}
  
}
