package com.fdel.controller;

import com.fdel.controller.requestdto.MenuSaveRequestDto;
import com.fdel.controller.requestdto.MenuUpdateRequestDto;
import com.fdel.controller.response.MenuListResponseDto;
import com.fdel.controller.response.MenuResponseDto;
import com.fdel.service.MenuService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MenuController {

  private final MenuService menuService;

  @PostMapping("/menu")
  public Long save(@RequestBody MenuSaveRequestDto requestDto) {
    return menuService.save(requestDto);
  }

  @PutMapping("/menu/{id}")
  public Long update(@PathVariable Long id, @RequestBody MenuUpdateRequestDto requestDto) {
    return menuService.update(id, requestDto);
  }

  @DeleteMapping("/menu/{id}")
  public Long delete(@PathVariable Long id) {
    menuService.delete(id);
    return id;
  }

  @GetMapping("menu/list")
  public List<MenuListResponseDto> findAll() {
    return menuService.findAll();
  }

  @GetMapping("menu/{id}")
  public MenuResponseDto findById(@PathVariable Long id) {
    return menuService.findById(id);
  }
}
