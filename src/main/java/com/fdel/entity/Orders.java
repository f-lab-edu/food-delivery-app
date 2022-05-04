package com.fdel.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Entity
public class Orders extends BaseEntity{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "orders_id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @OneToMany(mappedBy = "orders")
  private List<OrderMenu> orderMenuList = new ArrayList<>();

  @Enumerated(EnumType.STRING)
  private OrderStatus status;

  public void addOrderMenu(OrderMenu orderMenu) {
    orderMenuList.add(orderMenu);
    orderMenu.setOrder(this);
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public OrderStatus getStatus() {
    return status;
  }

  public void setStatus(OrderStatus status) {
    this.status = status;
  }
}
