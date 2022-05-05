package com.fdel.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "orders")
public class Order extends BaseEntity{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "orders_id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @OneToMany(mappedBy = "order")
  private List<OrderMenu> orderMenuList = new ArrayList<>();

  @Enumerated(EnumType.STRING)
  private OrderStatus status;

  public void addOrderMenu(OrderMenu orderMenu) {
    orderMenuList.add(orderMenu);
    orderMenu.setOrder(this);
  }
}
