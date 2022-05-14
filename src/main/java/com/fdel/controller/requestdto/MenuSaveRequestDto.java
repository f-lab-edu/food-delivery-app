package com.fdel.controller.requestdto;

import com.fdel.entity.Menu;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MenuSaveRequestDto {

  private String name;
  private Long price;
  private Long stockQuantity;

  @Builder
  public MenuSaveRequestDto(String name, Long price, Long stockQuantity) {
    this.name = name;
    this.price = price;
    this.stockQuantity = stockQuantity;
  }

  public Menu toEntity() {
    return Menu.builder()
        .name(name)
        .price(price)
        .stockQuantity(stockQuantity)
        .build();
  }
}
