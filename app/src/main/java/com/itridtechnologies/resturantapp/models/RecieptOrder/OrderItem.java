package com.itridtechnologies.resturantapp.models.RecieptOrder;

import com.google.gson.annotations.SerializedName;

public class OrderItem{

	@SerializedName("pickuptime")
	private String pickuptime;

	@SerializedName("business_tax")
	private String businessTax;

	@SerializedName("action_date")
	private Object actionDate;

	@SerializedName("min_pre_time")
	private int minPreTime;

	@SerializedName("max_pre_time")
	private int maxPreTime;

	@SerializedName("courier_notes")
	private String courierNotes;

	@SerializedName("action")
	private Object action;

	@SerializedName("id")
	private int id;

	@SerializedName("state")
	private String state;

	@SerializedName("order_type")
	private int orderType;

	@SerializedName("first_name")
	private String firstName;

	@SerializedName("business_rev_share")
	private String businessRevShare;

	@SerializedName("item_count")
	private int itemCount;

	@SerializedName("business_name")
	private String businessName;

	@SerializedName("business_notes")
	private String businessNotes;

	@SerializedName("payment_status")
	private int paymentStatus;

	@SerializedName("last_name")
	private String lastName;

	@SerializedName("otp")
	private int otp;

	@SerializedName("date_added")
	private String dateAdded;

	@SerializedName("payment_type")
	private int paymentType;

	@SerializedName("delay")
	private int delay;

	@SerializedName("date_modified")
	private String dateModified;

	@SerializedName("phone_number")
	private String phoneNumber;

	@SerializedName("customer_id")
	private int customerId;

	@SerializedName("business_id")
	private int businessId;

	@SerializedName("status")
	private String status;

	public void setPickuptime(String pickuptime){
		this.pickuptime = pickuptime;
	}

	public String getPickuptime(){
		return pickuptime;
	}

	public void setBusinessTax(String businessTax){
		this.businessTax = businessTax;
	}

	public String getBusinessTax(){
		return businessTax;
	}

	public void setActionDate(Object actionDate){
		this.actionDate = actionDate;
	}

	public Object getActionDate(){
		return actionDate;
	}

	public void setMinPreTime(int minPreTime){
		this.minPreTime = minPreTime;
	}

	public int getMinPreTime(){
		return minPreTime;
	}

	public void setMaxPreTime(int maxPreTime){
		this.maxPreTime = maxPreTime;
	}

	public int getMaxPreTime(){
		return maxPreTime;
	}

	public void setCourierNotes(String courierNotes){
		this.courierNotes = courierNotes;
	}

	public String getCourierNotes(){
		return courierNotes;
	}

	public void setAction(Object action){
		this.action = action;
	}

	public Object getAction(){
		return action;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setState(String state){
		this.state = state;
	}

	public String getState(){
		return state;
	}

	public void setOrderType(int orderType){
		this.orderType = orderType;
	}

	public int getOrderType(){
		return orderType;
	}

	public void setFirstName(String firstName){
		this.firstName = firstName;
	}

	public String getFirstName(){
		return firstName;
	}

	public void setBusinessRevShare(String businessRevShare){
		this.businessRevShare = businessRevShare;
	}

	public String getBusinessRevShare(){
		return businessRevShare;
	}

	public void setItemCount(int itemCount){
		this.itemCount = itemCount;
	}

	public int getItemCount(){
		return itemCount;
	}

	public void setBusinessName(String businessName){
		this.businessName = businessName;
	}

	public String getBusinessName(){
		return businessName;
	}

	public void setBusinessNotes(String businessNotes){
		this.businessNotes = businessNotes;
	}

	public String getBusinessNotes(){
		return businessNotes;
	}

	public void setPaymentStatus(int paymentStatus){
		this.paymentStatus = paymentStatus;
	}

	public int getPaymentStatus(){
		return paymentStatus;
	}

	public void setLastName(String lastName){
		this.lastName = lastName;
	}

	public String getLastName(){
		return lastName;
	}

	public void setOtp(int otp){
		this.otp = otp;
	}

	public int getOtp(){
		return otp;
	}

	public void setDateAdded(String dateAdded){
		this.dateAdded = dateAdded;
	}

	public String getDateAdded(){
		return dateAdded;
	}

	public void setPaymentType(int paymentType){
		this.paymentType = paymentType;
	}

	public int getPaymentType(){
		return paymentType;
	}

	public void setDelay(int delay){
		this.delay = delay;
	}

	public int getDelay(){
		return delay;
	}

	public void setDateModified(String dateModified){
		this.dateModified = dateModified;
	}

	public String getDateModified(){
		return dateModified;
	}

	public void setPhoneNumber(String phoneNumber){
		this.phoneNumber = phoneNumber;
	}

	public String getPhoneNumber(){
		return phoneNumber;
	}

	public void setCustomerId(int customerId){
		this.customerId = customerId;
	}

	public int getCustomerId(){
		return customerId;
	}

	public void setBusinessId(int businessId){
		this.businessId = businessId;
	}

	public int getBusinessId(){
		return businessId;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"OrderItem{" + 
			"pickuptime = '" + pickuptime + '\'' + 
			",business_tax = '" + businessTax + '\'' + 
			",action_date = '" + actionDate + '\'' + 
			",min_pre_time = '" + minPreTime + '\'' + 
			",max_pre_time = '" + maxPreTime + '\'' + 
			",courier_notes = '" + courierNotes + '\'' + 
			",action = '" + action + '\'' + 
			",id = '" + id + '\'' + 
			",state = '" + state + '\'' + 
			",order_type = '" + orderType + '\'' + 
			",first_name = '" + firstName + '\'' + 
			",business_rev_share = '" + businessRevShare + '\'' + 
			",item_count = '" + itemCount + '\'' + 
			",business_name = '" + businessName + '\'' + 
			",business_notes = '" + businessNotes + '\'' + 
			",payment_status = '" + paymentStatus + '\'' + 
			",last_name = '" + lastName + '\'' + 
			",otp = '" + otp + '\'' + 
			",date_added = '" + dateAdded + '\'' + 
			",payment_type = '" + paymentType + '\'' + 
			",delay = '" + delay + '\'' + 
			",date_modified = '" + dateModified + '\'' + 
			",phone_number = '" + phoneNumber + '\'' + 
			",customer_id = '" + customerId + '\'' + 
			",business_id = '" + businessId + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}