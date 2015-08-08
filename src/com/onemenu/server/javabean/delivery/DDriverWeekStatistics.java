package com.onemenu.server.javabean.delivery;

public class DDriverWeekStatistics implements DDriverStatistics {
	
	public String weekIncome;
	public String weekOrderAmount;
	public String weekOrderPrice;
	public String weekCashOrderPrice;
	public String weekDeliveryFee;
	public String weekNonCashTipsFee;
	
	@Override
	public void setOrderAmount(String orderAmount) {
		this.weekOrderAmount = orderAmount;
	}
	@Override
	public void setOrderPrice(String orderPrice) {
		this.weekOrderPrice = orderPrice;
	}
	@Override
	public void setCashOrderPrice(String cashOrderPrice) {
		this.weekCashOrderPrice = cashOrderPrice;
	}
	@Override
	public void setDeliveryFee(String deliveryFee) {
		this.weekDeliveryFee = deliveryFee;
	}
	@Override
	public void setNonCashTipsFee(String nonCashTipsFee) {
		this.weekNonCashTipsFee = nonCashTipsFee;
	}
	@Override
	public void setIncome(String income) {
		this.weekIncome = income ;
	}
	
	
	
	
}
