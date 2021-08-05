package com.itridtechnologies.resturantapp.models.receiptOrder;

import com.google.gson.annotations.SerializedName;

public class RecieptMainObject{

	@SerializedName("data")
	private Data data;

	@SerializedName("success")
	private boolean success;

	@SerializedName("message")
	private String message;

	public Data getData(){
		return data;
	}

	public boolean isSuccess(){
		return success;
	}

	public String getMessage(){
		return message;
	}
}