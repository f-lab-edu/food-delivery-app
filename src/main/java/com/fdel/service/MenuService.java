package com.fdel.service;


import static com.fdel.exception.message.MenuMessage.*;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fdel.dto.menu.MenuDto;
import com.fdel.entity.Menu;
import com.fdel.repository.MenuRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MenuService {

	private final MenuRepository menuRepository;

  	@Transactional
  	public void regist(MenuDto menuDto) {
	  	menuRepository.save(menuDto.toEntity()).getId();
  	}

  	@Transactional
  	public void update(MenuDto menuDto) {
	  Menu menu = menuRepository
		.findById(menuDto.getId())
		.orElseThrow(() -> 
			new EntityNotFoundException(MENU_ENTITY_NOT_FOUND.getMessage()));
		menu.update(menuDto.getName(),menuDto.getPrice(), menuDto.getStockQuantity());
  	}

  	@Transactional
  	public void delete (Long menuId) {
	  Menu menu = menuRepository
		.findById(menuId)
        .orElseThrow(() -> 
        	new EntityNotFoundException(MENU_ENTITY_NOT_FOUND.getMessage()));

    	menuRepository.delete(menu);
  	}

  	@Transactional
  	public void addStock(Long menuId, Integer quantity) {
	  	Menu menu = menuRepository
			.findById(menuId)
			.orElseThrow(() -> 
				new EntityNotFoundException(MENU_ENTITY_NOT_FOUND.getMessage()));
    	menu.addStock(quantity);
  	}

  	@Transactional
  	public void removeStock(Long menuId, Integer quantity) {
	  	Menu menu = menuRepository
			.findById(menuId)
			.orElseThrow(() -> 
				new EntityNotFoundException(MENU_ENTITY_NOT_FOUND.getMessage()));
    	menu.removeStock(quantity);
  	}

  	public List<MenuDto> findAll() {
  		return menuRepository
		  .findAll()
		  .stream()
		  .map(menuEntity->
		  		new MenuDto(menuEntity))
		  .collect(Collectors.toList());
  	}

  	public MenuDto findById(Long menuId) {
  		Menu menuEntity = menuRepository
			.findById(menuId)
			.orElseThrow(() -> 
				new EntityNotFoundException(MENU_ENTITY_NOT_FOUND.getMessage()));
    	return new MenuDto(menuEntity);
  	}

}
