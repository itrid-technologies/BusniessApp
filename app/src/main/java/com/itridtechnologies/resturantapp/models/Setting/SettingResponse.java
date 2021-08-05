package com.itridtechnologies.resturantapp.models.Setting;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class SettingResponse{

	@SerializedName("data")
	private Data data;

	@SerializedName("success")
	private boolean success;

	@SerializedName("message")
	private List<MessageItem> message;

	public Data getData(){
		return data;
	}

	public boolean isSuccess(){
		return success;
	}

	public List<MessageItem> getMessage(){
		return message;
	}
}