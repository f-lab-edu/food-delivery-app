package com.fdel.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
public class OrderMenu {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "order_menu_id")
  private Long id;

  @ManyToOne
  @JoinColumn(name ="order_id")
  private Order order;

  @ManyToOne
  @JoinColumn(name = "menu_id")
  private Menu menu;

  private Long orderPrice;
  private Long count;

}
