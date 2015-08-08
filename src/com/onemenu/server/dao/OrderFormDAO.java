package com.onemenu.server.dao;

import java.util.List;

import com.onemenu.server.javabean.OrderFormQueryCondition;
import com.onemenu.server.javabean.delivery.DDriverOrderBean;
import com.onemenu.server.javabean.delivery.DDriverStatisticsBean;
import com.onemenu.server.javabean.delivery.DOrderQueryBean;
import com.onemenu.server.model.OrderForm;



public interface OrderFormDAO extends BaseDAOInf {

    public List<OrderForm> getOrderFormListByRestaurantId(long customerId) throws RuntimeException;
    
    public OrderForm findById(long orderFormId) throws RuntimeException;
    
    public List<OrderForm> getOrderFormListByCondition(OrderFormQueryCondition condition);
    
    public List<DOrderQueryBean> getDeliveryOrderFormListByDriverId(OrderFormQueryCondition condition);
    
    public boolean updateOrderFormStatus(long orderFormId, String status) throws RuntimeException;

    /**
     * 获取driver未完成单的数量
     * @param driverId
     * @return
     */
    public int getDeliveryOrderCountByDriverId(long driverId);
    /**
     * 获取相关状态的订单
     * @return
     */
	public List<DDriverOrderBean> getAllOrders();

	public DDriverStatisticsBean getStatisticsDataByDriverId(long driverId);
    
    
}
