package com.fdel.service;


import com.fdel.controller.requestdto.MenuSaveRequestDto;
import com.fdel.controller.requestdto.MenuUpdateRequestDto;
import com.fdel.controller.responsedto.MenuListResponseDto;
import com.fdel.controller.responsedto.MenuResponseDto;
import com.fdel.entity.Menu;
import com.fdel.repository.MenuRepository;
import java.util.List;
import java.util.stream.Collectors;
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
  public Long save(MenuSaveRequestDto requestDto) {
    return menuRepository.save(requestDto.toEntity()).getId();
  }

  @Transactional
  public Long update(Long menuId, MenuUpdateRequestDto requestDto) {
    Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new EntityNotFoundException("Menu not found"));
    menu.update(requestDto.getName(),requestDto.getPrice());
    return menuId;
  }

  @Transactional
  public void delete (Long menuId) {
    Menu menu = menuRepository.findById(menuId)
        .orElseThrow(() -> new EntityNotFoundException("Menu not found"));

    menuRepository.delete(menu);
  }

  public Long addStock(Long menuId, Long quantity) {
    Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new EntityNotFoundException("Menu not found"));
    menu.addStock(quantity);
    return menuId;
  }

  public Long removeStock(Long menuId, Long quantity) {
    Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new EntityNotFoundException("Menu not found"));
    menu.removeStock(quantity);
    return menuId;
  }

  public List<MenuListResponseDto> findAll() {
    return menuRepository.findAll().stream().map(MenuListResponseDto::new).collect(Collectors.toList());
  }

  public MenuResponseDto findById(Long menuId) {
    Menu entity = menuRepository.findById(menuId).orElseThrow(() -> new EntityNotFoundException("Menu not found"));
    return new MenuResponseDto(entity);
  }

}
