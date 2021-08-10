package com.itridtechnologies.resturantapp.models.MenuAddOns;

import com.google.gson.annotations.SerializedName;

public class AddonItem{

	@SerializedName("date_added")
	private String dateAdded;

	@SerializedName("price")
	private String price;

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private int id;

	@SerializedName("addon_id")
	private int addonId;

	public String getDateAdded(){
		return dateAdded;
	}

	public String getPrice(){
		return price;
	}

	public String getName(){
		return name;
	}

	public int getId(){
		return id;
	}

	public int getAddonId(){
		return addonId;
	}
}