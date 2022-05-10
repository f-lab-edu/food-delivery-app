package com.fdel.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Menu extends BaseEntity{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "menu_id")
  private Long id;

  private String name;
  private Long price;
  private Long stockQuantity;

  @Builder
  public Menu(Long id, String name, Long price, Long stockQuantity) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.stockQuantity = stockQuantity;
  }
  
  public void addStock(Long quantity) {
    this.stockQuantity += quantity;
  }
  
  public void removeStock(Long quantity) {
    long remaining = this.stockQuantity - quantity;
    if (remaining < 0) {
      //Exeption message=Not Enough Stock
    }
    this.stockQuantity = remaining;
  }

  public void update(String name, Long price) {
    this.name = name;
    this.price = price;
  }
}
