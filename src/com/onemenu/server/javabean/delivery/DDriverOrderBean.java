package com.onemenu.server.javabean.delivery;

import java.math.BigInteger;
import java.sql.Timestamp;

/**
 * 获取所有订单信息
 * @author file
 * @since 2015年6月21日 下午10:08:14
 * @version 1.0
 * @copyright  by onemenu
 */
public class DDriverOrderBean {
	
	public String orderId;
	public String restId;
	public BigInteger _orderId;
	public BigInteger _restId;
    public String restName;	
	public String orderCode;
	public String status;
	public BigInteger driverId;
	public String driverName;
	//呼叫外卖时间
	public Timestamp  callDeliveryTime;
	
	public String  inReadyTime;
	

}
