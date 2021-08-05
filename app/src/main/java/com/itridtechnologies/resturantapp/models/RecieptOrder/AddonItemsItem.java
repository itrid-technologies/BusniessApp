package com.itridtechnologies.resturantapp.models.RecieptOrder;

import com.google.gson.annotations.SerializedName;

public class AddonItemsItem{

	@SerializedName("addon_item_price")
	private String addonItemPrice;

	@SerializedName("addon_item_name")
	private String addonItemName;

	@SerializedName("qty")
	private int qty;

	public void setAddonItemPrice(String addonItemPrice){
		this.addonItemPrice = addonItemPrice;
	}

	public String getAddonItemPrice(){
		return addonItemPrice;
	}

	public void setAddonItemName(String addonItemName){
		this.addonItemName = addonItemName;
	}

	public String getAddonItemName(){
		return addonItemName;
	}

	public void setQty(int qty){
		this.qty = qty;
	}

	public int getQty(){
		return qty;
	}

	@Override
 	public String toString(){
		return 
			"AddonItemsItem{" + 
			"addon_item_price = '" + addonItemPrice + '\'' + 
			",addon_item_name = '" + addonItemName + '\'' + 
			",qty = '" + qty + '\'' + 
			"}";
		}
}