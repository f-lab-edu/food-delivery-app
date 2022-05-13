package com.fdel.controller.responsedto;

import com.fdel.entity.Menu;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class MenuListResponseDto {

  private Long id;
  private String name;
  private LocalDateTime updatedAt;

  public MenuListResponseDto(Menu entity) {
    this.id = entity.getId();
    this.name = entity.getName();
    this.updatedAt = entity.getUpdatedAt();
  }
}
