package com.itridtechnologies.resturantapp.models.login;

import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("results")
	private Results results;

	@SerializedName("token")
	private String token;

	public Results getResults(){
		return results;
	}

	public String getToken(){
		return token;
	}
}