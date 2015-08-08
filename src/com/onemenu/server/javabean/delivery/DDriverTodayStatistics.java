package com.onemenu.server.javabean.delivery;

public class DDriverTodayStatistics implements DDriverStatistics {
	
	public String todayIncome;
	public String todayOrderAmount;
	public String todayOrderPrice;
	public String todayCashOrderPrice;
	public String todayDeliveryFee;
	public String todayNonCashTipsFee;
	
	@Override
	public void setOrderAmount(String orderAmount) {
		this.todayOrderAmount = orderAmount;
	}
	@Override
	public void setOrderPrice(String orderPrice) {
		this.todayOrderPrice = orderPrice; 
	}
	@Override
	public void setCashOrderPrice(String cashOrderPrice) {
		this.todayCashOrderPrice = cashOrderPrice;
	}
	@Override
	public void setDeliveryFee(String deliveryFee) {
		this.todayDeliveryFee = deliveryFee;
	}
	@Override
	public void setNonCashTipsFee(String nonCashTipsFee) {
		this.todayNonCashTipsFee = nonCashTipsFee;
	}
	@Override
	public void setIncome(String income) {
		this.todayIncome = income;
	}
	
}
