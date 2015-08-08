package com.onemenu.server.protocol.req.delivery;

/**
 * BaseDeliveryPageReqData
 * @author file
 * @since 2015年6月20日 下午9:36:26
 * @version 1.0
 * @copyright  by onemenu
 */
public class BaseDeliveryPageReqData extends BaseDeliveryReqData{
	
	public String startNum;
	public String range;
	
	public void setStartNum(String startNum) {
		this.startNum = startNum;
	}
	
	public void setRange(String range) {
		this.range = range;
	}
	
	
	
}
