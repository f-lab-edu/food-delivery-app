package com.fdel.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "orders") // "order" 는 MYSQL 예약어로 사용 불가
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "orders_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
  private List<OrderMenu> orderMenuList = new ArrayList<>();

  private LocalDateTime orderDate; //주문시간

  @Enumerated(EnumType.STRING)
  private OrderStatus orderStatus; //주문상태 (ORDER, CANCEL)

  public void changeUser(User user) {
    this.user = user;
    user.getOrders().add(this);
  }
  public void addOrderMenu(OrderMenu orderMenu) {
    orderMenuList.add(orderMenu);
    orderMenu.changeOrder(this);
  }

  public void changeStatus(OrderStatus orderStatus) {
    this.orderStatus = orderStatus;
  }

  public void changeOrderDate(LocalDateTime time) {
    this.orderDate = time;
  }

  /**
   * Order 생성
   */
  public static Order createOrder(User user, OrderMenu... orderMenus) {
    Order order = new Order();
    order.changeUser(user);
    Arrays.stream(orderMenus).forEach(order::addOrderMenu);
    order.changeStatus(OrderStatus.ORDER);
    order.changeOrderDate(LocalDateTime.now());
    return order;
  }

  /**
   * 주문 취소
   */
  public void orderCancel() {
    // 배달 중일땐 취소 안되야 할것 같음..(배달관련 추후 추가 고민)
    this.changeStatus(OrderStatus.CANCEL);
    orderMenuList.forEach(OrderMenu::cancel);
  }

  /**
   * 전체 주문 가격
   */
  public int getTotalPrice() {
    return orderMenuList.stream().mapToInt(OrderMenu::getTotalPrice).sum();
  }
}
