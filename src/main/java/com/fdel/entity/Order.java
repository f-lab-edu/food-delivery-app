package com.fdel.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "orders") // order는 MYSQL 예약어로 사용 불가
public class Order extends BaseTimeEntity{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "orders_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
  private List<OrderMenu> orderMenuList = new ArrayList<>();

  @Enumerated(EnumType.STRING)
  private OrderStatus status;

  public void addOrderMenu(OrderMenu orderMenu) {
    orderMenuList.add(orderMenu);
    orderMenu.setOrder(this);
  }
  
  public enum OrderStatus {
	  ORDER, CANCEL
	}
}
