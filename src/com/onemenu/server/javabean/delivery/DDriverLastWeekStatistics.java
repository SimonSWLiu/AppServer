package com.onemenu.server.javabean.delivery;

public class DDriverLastWeekStatistics implements DDriverStatistics {
	
	public String lastWeekIncome;
	public String lastWeekOrderAmount;
	public String lastWeekOrderPrice;
	public String lastWeekCashOrderPrice;
	public String lastWeekDeliveryFee;
	public String lastWeekNonCashTipsFee;
	
	@Override
	public void setOrderAmount(String orderAmount) {
		this.lastWeekOrderAmount = orderAmount;
	}
	@Override
	public void setOrderPrice(String orderPrice) {
		this.lastWeekOrderPrice = orderPrice;
	}
	@Override
	public void setCashOrderPrice(String cashOrderPrice) {
		this.lastWeekCashOrderPrice = cashOrderPrice;
	}
	@Override
	public void setDeliveryFee(String deliveryFee) {
		this.lastWeekDeliveryFee = deliveryFee;
	}
	@Override
	public void setNonCashTipsFee(String nonCashTipsFee) {
		this.lastWeekNonCashTipsFee = nonCashTipsFee;
	}
	@Override
	public void setIncome(String income) {
		this.lastWeekIncome = income ;
	}
	
	
	
	
}
