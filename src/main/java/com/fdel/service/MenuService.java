package com.fdel.service;


import com.fdel.controller.requestdto.MenuUpdateRequestDto;
import com.fdel.entity.Menu;
import com.fdel.repository.MenuRepository;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MenuService {

  private final MenuRepository menuRepository;

  @Transactional
  public void saveMenu(Menu menu) {
    menuRepository.save(menu);
  }

  @Transactional
  public Long updateItem(Long menuId, MenuUpdateRequestDto requestDto) {
    Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new EntityNotFoundException("Menu not found"));
    menu.update(requestDto.getName(),requestDto.getPrice());
    return menuId;
  }

  public List<Menu> findAllMenu() {
    return menuRepository.findAll();
  }

  public Menu findOneMenu(Long menuId) {
    return menuRepository.findById(menuId).orElseThrow(() -> new EntityNotFoundException("Menu not found"));
  }

}
