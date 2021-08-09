package com.itridtechnologies.resturantapp.models.feedbacktags;

import com.google.gson.annotations.SerializedName;

public class DataItem{

	@SerializedName("date_added")
	private String dateAdded;

	@SerializedName("date_modified")
	private Object dateModified;

	@SerializedName("for_type")
	private int forType;

	@SerializedName("tag_type")
	private int tagType;

	@SerializedName("id")
	private int id;

	@SerializedName("display_name")
	private String displayName;

	@SerializedName("by_type")
	private int byType;

	@SerializedName("slug")
	private String slug;

	public String getDateAdded(){
		return dateAdded;
	}

	public Object getDateModified(){
		return dateModified;
	}

	public int getForType(){
		return forType;
	}

	public int getTagType(){
		return tagType;
	}

	public int getId(){
		return id;
	}

	public String getDisplayName(){
		return displayName;
	}

	public int getByType(){
		return byType;
	}

	public String getSlug(){
		return slug;
	}
}