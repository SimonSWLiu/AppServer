package com.onemenu.server.javabean.delivery;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
/**
 * 查询数据bean
 * @author file
 * @since 2015年6月13日 下午11:24:03
 * @version 1.0
 * @copyright  by onemenu
 */
public class DOrderQueryBean {
	
	public BigInteger restId;
	public String restName;
	//用户地址
	public String		custAddr;
	//订单价格
	public BigDecimal	billPrice;
	//商家确定时间
	public Timestamp	restTime;
	//呼叫外卖时间
	public Timestamp	callDeliveryTime;
	//收到时间
	public Timestamp	createdTime;
	//是否是onemenu的订单
	public Integer		isOMOrder;
	//订单id
	public BigInteger	orderId;
	//状态
	public String       status;
	//付款方式
	public String  paymentType;

}
