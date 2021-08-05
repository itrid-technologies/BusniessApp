package com.itridtechnologies.resturantapp.models.newOrder;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("orderAddress")
	private List<OrderAddressItem> orderAddress;

	@SerializedName("orderTotals")
	private List<OrderTotalsItem> orderTotals;

	@SerializedName("orderItems")
	private List<OrderItemsItem> orderItems;

	@SerializedName("order")
	private List<OrderItem> order;

	public List<OrderAddressItem> getOrderAddress(){
		return orderAddress;
	}

	public List<OrderTotalsItem> getOrderTotals(){
		return orderTotals;
	}

	public List<OrderItemsItem> getOrderItems(){
		return orderItems;
	}

	public List<OrderItem> getOrder(){
		return order;
	}
}