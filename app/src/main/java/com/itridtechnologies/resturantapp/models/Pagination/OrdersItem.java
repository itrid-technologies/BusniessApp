package com.itridtechnologies.resturantapp.models.Pagination;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "order_details")
public class OrdersItem {

    public void setPickuptime(String pickuptime) {
        this.pickuptime = pickuptime;
    }

    public void setBusinessTax(String businessTax) {
        this.businessTax = businessTax;
    }

    public void setActionDate(String actionDate) {
        this.actionDate = actionDate;
    }

    public void setMinPreTime(int minPreTime) {
        this.minPreTime = minPreTime;
    }

    public void setMaxPreTime(int maxPreTime) {
        this.maxPreTime = maxPreTime;
    }

    public void setCourierNotes(String courierNotes) {
        this.courierNotes = courierNotes;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setBusinessRevShare(String businessRevShare) {
        this.businessRevShare = businessRevShare;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public void setBusinessNotes(String businessNotes) {
        this.businessNotes = businessNotes;
    }

    public void setPaymentStatus(int paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setOtp(int otp) {
        this.otp = otp;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public void setPaymentType(int paymentType) {
        this.paymentType = paymentType;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setBusinessId(int businessId) {
        this.businessId = businessId;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    @SerializedName("pickuptime")
    private String pickuptime = "";

//
//    @SerializedName("remainingtime")
//    private String remainingtime = "";
//
//
//    @SerializedName("savingtime")
//    private String savingtime = "";

    @SerializedName("business_tax")
    private String businessTax = "";

    @SerializedName("action_date")
    private String actionDate = "";

    @SerializedName("min_pre_time")
    private int minPreTime;

    @SerializedName("max_pre_time")
    private int maxPreTime;

    @SerializedName("courier_notes")
    private String courierNotes = "";

    @SerializedName("action")
    private int action;

    @PrimaryKey
    @SerializedName("id")
    private int id;

    @SerializedName("state")
    private String state = "";

    @ColumnInfo(name = "order_type")
    @SerializedName("order_type")
    private int orderType;

    @SerializedName("first_name")
    private String firstName = "";

    @SerializedName("business_rev_share")
    private String businessRevShare = "";

    @SerializedName("item_count")
    private int itemCount;

    @SerializedName("business_name")
    private String businessName = "";

    @SerializedName("business_notes")
    private String businessNotes = "";

    @SerializedName("payment_status")
    private int paymentStatus;

    @SerializedName("last_name")
    private String lastName = "";

    @SerializedName("otp")
    private int otp;

    @SerializedName("date_added")
    private String dateAdded = "";

    @SerializedName("payment_type")
    private int paymentType;

    @SerializedName("delay")
    private int delay;

    @SerializedName("date_modified")
    private String dateModified = "";

    @SerializedName("phone_number")
    private String phoneNumber = "";

    @SerializedName("customer_id")
    private int customerId;

    @SerializedName("business_id")
    private int businessId;

    @SerializedName("status")
    private String status = "";

    public OrdersItem(String pickuptime, String businessTax, String actionDate, int minPreTime, int maxPreTime, String courierNotes, int action, int id, String state, int orderType, String firstName, String businessRevShare, int itemCount, String businessName, String businessNotes, int paymentStatus, String lastName, int otp, String dateAdded, int paymentType, int delay, String dateModified, String phoneNumber, int customerId, int businessId, String status) {
        this.pickuptime = pickuptime;
        this.businessTax = businessTax;
        this.actionDate = actionDate;
        this.minPreTime = minPreTime;
        this.maxPreTime = maxPreTime;
        this.courierNotes = courierNotes;
        this.action = action;
        this.id = id;
        this.state = state;
        this.orderType = orderType;
        this.firstName = firstName;
        this.businessRevShare = businessRevShare;
        this.itemCount = itemCount;
        this.businessName = businessName;
        this.businessNotes = businessNotes;
        this.paymentStatus = paymentStatus;
        this.lastName = lastName;
        this.otp = otp;
        this.dateAdded = dateAdded;
        this.paymentType = paymentType;
        this.delay = delay;
        this.dateModified = dateModified;
        this.phoneNumber = phoneNumber;
        this.customerId = customerId;
        this.businessId = businessId;
        this.status = status;
    }

    public String getPickuptime() {
        return pickuptime;
    }

    public String getBusinessTax() {
        return businessTax;
    }

    public String getActionDate() {
        return actionDate;
    }

    public int getMinPreTime() {
        return minPreTime;
    }

    public int getMaxPreTime() {
        return maxPreTime;
    }

    public String getCourierNotes() {
        return courierNotes;
    }

    public int getAction() {
        return action;
    }

    public int getId() {
        return id;
    }

    public String getState() {
        return state;
    }

    public int getOrderType() {
        return orderType;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getBusinessRevShare() {
        return businessRevShare;
    }

    public int getItemCount() {
        return itemCount;
    }

    public String getBusinessName() {
        return businessName;
    }

    public String getBusinessNotes() {
        return businessNotes;
    }

    public int getPaymentStatus() {
        return paymentStatus;
    }

    public String getLastName() {
        return lastName;
    }

    public int getOtp() {
        return otp;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public int getPaymentType() {
        return paymentType;
    }

    public int getDelay() {
        return delay;
    }

    public String getDateModified() {
        return dateModified;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getCustomerId() {
        return customerId;
    }

    public int getBusinessId() {
        return businessId;
    }

    public String getStatus() {
        return status;
    }


    @Override
    public String toString() {
        return "OrdersItem{" +
                "pickuptime='" + pickuptime + '\'' +
                ", businessTax='" + businessTax + '\'' +
                ", actionDate='" + actionDate + '\'' +
                ", minPreTime=" + minPreTime +
                ", maxPreTime=" + maxPreTime +
                ", courierNotes='" + courierNotes + '\'' +
                ", action=" + action +
                ", id=" + id +
                ", state='" + state + '\'' +
                ", orderType=" + orderType +
                ", firstName='" + firstName + '\'' +
                ", businessRevShare='" + businessRevShare + '\'' +
                ", itemCount=" + itemCount +
                ", businessName='" + businessName + '\'' +
                ", businessNotes='" + businessNotes + '\'' +
                ", paymentStatus=" + paymentStatus +
                ", lastName='" + lastName + '\'' +
                ", otp=" + otp +
                ", dateAdded='" + dateAdded + '\'' +
                ", paymentType=" + paymentType +
                ", delay=" + delay +
                ", dateModified='" + dateModified + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", customerId=" + customerId +
                ", businessId=" + businessId +
                ", status='" + status + '\'' +
                '}';
    }
}