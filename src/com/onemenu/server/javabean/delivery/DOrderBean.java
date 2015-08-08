package com.onemenu.server.javabean.delivery;

import java.util.List;

public class DOrderBean {
	
	public DCustomerBean customer;
	public String billPrice;
	public String isOMOrder;
	public String orderId;
	public String orderReadyTime;
	public String status;
	
	public String orderCode;
	
	public String paymentType;
	
	public List<DDishBean>  dishArray;
	
	public DRestaurantBean restaurant;
	public String tipsFee;
	public String tipsType;
	public String timestamp;
	
}
