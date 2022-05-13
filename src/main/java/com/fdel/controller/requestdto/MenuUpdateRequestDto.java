package com.fdel.controller.requestdto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MenuUpdateRequestDto {

  private String name;
  private Long price;

  @Builder
  public MenuUpdateRequestDto(String name, Long price) {
    this.name = name;
    this.price = price;
  }
}
