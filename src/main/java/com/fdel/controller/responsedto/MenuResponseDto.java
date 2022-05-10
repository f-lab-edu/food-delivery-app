package com.fdel.controller.responsedto;

import com.fdel.entity.Menu;
import lombok.Getter;

@Getter
public class MenuResponseDto {

  private Long id;
  private String name;
  private Long price;
  private Long stockQuantity;

  public MenuResponseDto(Menu entity) {
    this.id = entity.getId();
    this.name = entity.getName();
    this.price = entity.getPrice();
    this.stockQuantity = entity.getStockQuantity();
  }

}
