package com.itridtechnologies.resturantapp.models.OrderSubItems;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class DataItem{

	@SerializedName("item_subtotal")
	private String itemSubtotal;

	@SerializedName("item_id")
	private int itemId;

	@SerializedName("item_discount_type")
	private int itemDiscountType;

	@SerializedName("item_price")
	private String itemPrice;

	@SerializedName("item_price_after_discount")
	private String itemPriceAfterDiscount;

	@SerializedName("item_name")
	private String itemName;

	@SerializedName("item_total")
	private String itemTotal;

	@SerializedName("orderAddons")
	private List<OrderAddonsItem> orderAddons;

	@SerializedName("date_added")
	private String dateAdded;

	@SerializedName("date_modified")
	private Object dateModified;

	@SerializedName("item_discount_value")
	private String itemDiscountValue;

	@SerializedName("order_id")
	private int orderId;

	@SerializedName("age_restriction")
	private int ageRestriction;

	@SerializedName("item_qty")
	private int itemQty;

	public String getItemSubtotal(){
		return itemSubtotal;
	}

	public int getItemId(){
		return itemId;
	}

	public int getItemDiscountType(){
		return itemDiscountType;
	}

	public String getItemPrice(){
		return itemPrice;
	}

	public String getItemPriceAfterDiscount(){
		return itemPriceAfterDiscount;
	}

	public String getItemName(){
		return itemName;
	}

	public String getItemTotal(){
		return itemTotal;
	}

	public List<OrderAddonsItem> getOrderAddons(){
		return orderAddons;
	}

	public String getDateAdded(){
		return dateAdded;
	}

	public Object getDateModified(){
		return dateModified;
	}

	public String getItemDiscountValue(){
		return itemDiscountValue;
	}

	public int getOrderId(){
		return orderId;
	}

	public int getAgeRestriction(){
		return ageRestriction;
	}

	public int getItemQty(){
		return itemQty;
	}
}