package com.itridtechnologies.resturantapp.models.FeedbackReviewGiven;

import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("partner_review")
	private boolean partnerReview;

	@SerializedName("customer_review")
	private boolean customerReview;

	public boolean isPartnerReview(){
		return partnerReview;
	}

	public boolean isCustomerReview(){
		return customerReview;
	}
}