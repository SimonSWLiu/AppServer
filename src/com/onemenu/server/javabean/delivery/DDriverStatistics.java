package com.onemenu.server.javabean.delivery;

public interface DDriverStatistics {
	
	public void setOrderAmount(String orderAmount);
	public void setOrderPrice(String orderPrice);
	public void setCashOrderPrice(String cashOrderPrice);
	public void setDeliveryFee(String deliveryFee);
	public void setNonCashTipsFee(String nonCashTipsFee);
	public void setIncome(String income);

}
