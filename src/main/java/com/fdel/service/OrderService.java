package com.fdel.service;

import com.fdel.entity.Order;
import com.fdel.entity.Menu;
import com.fdel.entity.OrderMenu;
import com.fdel.entity.User;
import com.fdel.repository.MenuRepository;
import com.fdel.repository.OrderRepository;
import com.fdel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;
  private final UserRepository userRepository;
  private final MenuRepository menuRepository;

  @Transactional
  public Long registOrder(Long userId, Long menuId, Integer count) {

    User user = userRepository
        .findById(userId)
        .orElseThrow();

    Menu menu = menuRepository
        .findById(menuId)
        .orElseThrow();

    // 주문메뉴 생성
    OrderMenu orderMenu = OrderMenu.createOrderMenu(menu, menu.getPrice(), count);
    //주문 생성
    Order order = Order.createOrder(user, orderMenu);
    //주문 저장
    orderRepository.save(order);

    return order.getId();
  }

  /**
   *
   * 주문 취소
   */
  @Transactional
  public void cancelOrder(Long orderId) {
    Order order = orderRepository
        .findById(orderId)
        .orElseThrow();
    //주문 취소
    order.cancelOrder();
  }
}
