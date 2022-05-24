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

import com.fdel.dto.store.StoreDto;
import com.fdel.service.StoreService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/stores")
@RequiredArgsConstructor
public class StoreController {
	
	  private final StoreService storeService;

	  @PostMapping
	  public void regist(@RequestBody StoreDto storeDto) {
		  storeService.regist(storeDto);
	  }

	  @PatchMapping("/{id}")
	  public void update(@RequestBody StoreDto storeDto) {
		  storeService.update(storeDto);
	  }

	  @DeleteMapping("/{id}")
	  public void delete(@PathVariable Long id) {
		  storeService.delete(id);
	  }

	  @GetMapping
	  public List<StoreDto> findAll() {
	    return storeService.findAll();
	  }

	  @GetMapping("/{id}")
	  public StoreDto findById(@PathVariable Long id) {
	    return storeService.findById(id);
	  }
	
}
