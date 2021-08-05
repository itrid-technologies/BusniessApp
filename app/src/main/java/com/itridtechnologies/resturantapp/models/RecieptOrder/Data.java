package com.itridtechnologies.resturantapp.models.RecieptOrder;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("orderAddress")
	private List<OrderAddressItem> orderAddress;

	@SerializedName("orderTotals")
	private List<Object> orderTotals;

	@SerializedName("orderItems")
	private List<OrderItemsItem> orderItems;

	@SerializedName("order")
	private List<OrderItem> order;

	public void setOrderAddress(List<OrderAddressItem> orderAddress){
		this.orderAddress = orderAddress;
	}

	public List<OrderAddressItem> getOrderAddress(){
		return orderAddress;
	}

	public void setOrderTotals(List<Object> orderTotals){
		this.orderTotals = orderTotals;
	}

	public List<Object> getOrderTotals(){
		return orderTotals;
	}

	public void setOrderItems(List<OrderItemsItem> orderItems){
		this.orderItems = orderItems;
	}

	public List<OrderItemsItem> getOrderItems(){
		return orderItems;
	}

	public void setOrder(List<OrderItem> order){
		this.order = order;
	}

	public List<OrderItem> getOrder(){
		return order;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"orderAddress = '" + orderAddress + '\'' + 
			",orderTotals = '" + orderTotals + '\'' + 
			",orderItems = '" + orderItems + '\'' + 
			",order = '" + order + '\'' + 
			"}";
		}
}