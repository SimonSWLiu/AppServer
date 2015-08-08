package com.onemenu.server.protocol.req.calldelivery;


public class BaseCallDeliveryPageReqData extends BaseCallDeliveryReqData {
	public String startNum;
	public String range;
	
	
	public void setStartNum(String startNum) {
		this.startNum = startNum;
	}
	
	public void setRange(String range) {
		this.range = range;
	}
	
	
}
