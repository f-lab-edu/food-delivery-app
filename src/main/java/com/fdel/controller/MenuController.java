package com.fdel.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fdel.dto.menu.MenuDto;
import com.fdel.service.MenuService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/menus")
@RequiredArgsConstructor
@RestController
public class MenuController {

  private final MenuService menuService;

  @PostMapping
  public void save(@RequestBody MenuDto menuDto) {
    menuService.regist(menuDto);
  }

  @PatchMapping("/{id}")
  public void update(@RequestBody MenuDto menuDto) {
    menuService.update(menuDto);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) {
    menuService.delete(id);
  }

  @GetMapping
  public List<MenuDto> findAll() {
    return menuService.findAll();
  }

  @GetMapping("/{id}")
  public MenuDto findById(@PathVariable Long id) {
    return menuService.findById(id);
  }

}
