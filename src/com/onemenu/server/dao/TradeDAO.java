package com.onemenu.server.dao;

import java.util.List;

import com.onemenu.server.model.OrderItem;



public interface TradeDAO extends BaseDAOInf {

    public List<OrderItem> getOrderItemListByCustomerId(long customerId) throws RuntimeException;
    
    public Long findTodayTradeRowCount() throws RuntimeException;
    
    public Long findTodayOrderFormRowCount() throws RuntimeException;

}
