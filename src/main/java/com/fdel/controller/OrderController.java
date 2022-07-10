package com.fdel.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fdel.service.OrderService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/orders")
@RequiredArgsConstructor
@RestController
public class OrderController {

  private final OrderService orderService;

  @PostMapping
  public void addOrder(@RequestParam("userId") Long userId,
      @RequestParam("menuId") Long menuId,
      @RequestParam("count") int count) {

    orderService.registOrder(userId, menuId, count);
  }

  @PostMapping("/{orderId}/cancel")
  public void cancelOrder(@PathVariable("orderId") Long orderId) {
    orderService.cancelOrder(orderId);
  }

}
