package com.itridtechnologies.resturantapp.models.orderHistory;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class OrdersItem{

	@SerializedName("addons")
	private List<AddonsItem> addons;

	@SerializedName("items")
	private List<ItemsItem> items;

	@SerializedName("order")
	private Order order;

	public List<AddonsItem> getAddons(){
		return addons;
	}

	public List<ItemsItem> getItems(){
		return items;
	}

	public Order getOrder(){
		return order;
	}
}