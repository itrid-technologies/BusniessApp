package com.itridtechnologies.resturantapp.models.historyNew;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("totalCount")
	private int totalCount;

	@SerializedName("results")
	private List<ResultsItem> results;

	public int getTotalCount(){
		return totalCount;
	}

	public List<ResultsItem> getResults(){
		return results;
	}
}