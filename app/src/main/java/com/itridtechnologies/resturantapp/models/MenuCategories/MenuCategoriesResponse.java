package com.itridtechnologies.resturantapp.models.MenuCategories;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class MenuCategoriesResponse{

	@SerializedName("data")
	private List<DataItem> data;

	@SerializedName("success")
	private boolean success;

	@SerializedName("message")
	private Message message;

	public List<DataItem> getData(){
		return data;
	}

	public boolean isSuccess(){
		return success;
	}

	public Message getMessage(){
		return message;
	}
}