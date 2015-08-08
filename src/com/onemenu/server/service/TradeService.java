package com.onemenu.server.service;

import java.util.List;

import com.onemenu.server.javabean.NMenuItemBean;
import com.onemenu.server.javabean.NMenuItemDishBean;
import com.onemenu.server.javabean.OrderItemBean;
import com.onemenu.server.javabean.TradeBean;
import com.onemenu.server.model.Trade;


/**
 * 
 * @author lin
 *
 */
public interface TradeService extends AbstractServiceInf {

    public List<OrderItemBean> listOrderItemByCustomerId(long customerId);
    
    public Trade saveTradeBean(TradeBean tradeBean);

	public void payOrderByBraintree(TradeBean tradeBean, String nonce, String amount);
	/**
	 * 获取客户的订单信息
	 * @param customerId
	 * @return
	 */
	public List<NMenuItemDishBean> orderItemsByCustomerId(long customerId);
	
	
	public int  getAllOrderCount();

	public List<NMenuItemBean> getTastesByCustomerId(long customerId);
    
}
