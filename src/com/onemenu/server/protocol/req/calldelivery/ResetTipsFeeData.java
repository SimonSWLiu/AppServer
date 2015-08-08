package com.onemenu.server.protocol.req.calldelivery;


public class ResetTipsFeeData extends BaseCallDeliveryReqData {
	
	public String orderFormId;
	public String restTipsFee;
	
	public void setOrderFormId(String orderFormId) {
		this.orderFormId = orderFormId;
	}
	public void setRestTipsFee(String restTipsFee) {
		this.restTipsFee = restTipsFee;
	}
	
	

}
