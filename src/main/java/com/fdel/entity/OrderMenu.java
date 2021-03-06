package com.fdel.entity;

import javax.persistence.*;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderMenu {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name ="order_id")
	private Order order;
	
	@ManyToOne
	@JoinColumn(name = "menu_id")
	private Menu menu;
	
	private Integer orderPrice;
	private Integer count;
	
	public void changeOrder(Order order) {
	  this.order = order;
	}

	/**
	 * OrderMenu 생성
	 */
	public static OrderMenu createOrderMenu(Menu menu, Integer orderPrice, Integer count) {
	    OrderMenu orderMenu = new OrderMenu();
	    orderMenu.changeMenu(menu);
	    orderMenu.changeOrderPrice(orderPrice);
	    orderMenu.changeCount(count);
	    
	    menu.removeStock(count);
	    return orderMenu;
	}

	private void changeCount(Integer count) {
		this.count = count;
	}
	
	private void changeOrderPrice(Integer orderPrice) {
		this.orderPrice = orderPrice;
	}
	
	private void changeMenu(Menu menu) {
		this.menu = menu;
	}

	public void cancel() {
		getMenu().addStock(count);
	}

	public int getTotalPrice() {
		return getOrderPrice() * getCount();
	}

	public Long getId() {
		return id;
	}

	public Order getOrder() {
		return order;
	}

	public Menu getMenu() {
		return menu;
	}

	public Integer getOrderPrice() {
		return orderPrice;
	}

	public Integer getCount() {
		return count;
	}
	
}
