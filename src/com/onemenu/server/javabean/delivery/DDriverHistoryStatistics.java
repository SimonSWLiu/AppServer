package com.onemenu.server.javabean.delivery;

public class DDriverHistoryStatistics implements DDriverStatistics {
	public String historyIncome;
	public String historyOrderAmount;
	public String historyOrderPrice;
	public String historyCashOrderPrice;
	public String historyDeliveryFee;
	public String historyNonCashTipsFee;
	
	@Override
	public void setOrderAmount(String orderAmount) {
		this.historyOrderAmount = orderAmount; 
	}
	@Override
	public void setOrderPrice(String orderPrice) {
		this.historyOrderPrice = orderPrice;
	}
	@Override
	public void setCashOrderPrice(String cashOrderPrice) {
		this.historyCashOrderPrice = cashOrderPrice;
		
	}
	@Override
	public void setDeliveryFee(String deliveryFee) {
		this.historyDeliveryFee = deliveryFee;
	}
	@Override
	public void setNonCashTipsFee(String nonCashTipsFee) {
		this.historyNonCashTipsFee = nonCashTipsFee;
	}
	@Override
	public void setIncome(String income) {
		this.historyIncome = income;
	}
	

}
