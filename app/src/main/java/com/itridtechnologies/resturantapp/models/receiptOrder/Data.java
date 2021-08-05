package com.itridtechnologies.resturantapp.models.receiptOrder;

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
	private List<MainOrdersDetails> order;

	public List<OrderAddressItem> getOrderAddress(){
		return orderAddress;
	}

	public List<Object> getOrderTotals(){
		return orderTotals;
	}

	public List<OrderItemsItem> getOrderItems(){
		return orderItems;
	}

	public List<MainOrdersDetails> getOrder(){
		return order;
	}
}