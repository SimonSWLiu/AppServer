package com.onemenu.server.service;

import java.math.BigDecimal;
import java.util.List;

import com.onemenu.server.javabean.OrderFormQueryCondition;
import com.onemenu.server.javabean.calldelivery.CDOrderBean;
import com.onemenu.server.javabean.calldelivery.CDRestaurantOrdersBean;
import com.onemenu.server.javabean.delivery.DDriverOrderBean;
import com.onemenu.server.javabean.delivery.DDriverStatisticsBean;
import com.onemenu.server.javabean.delivery.DHistoryDayOrderBean;
import com.onemenu.server.javabean.delivery.DLocationBean;
import com.onemenu.server.javabean.delivery.DOrderBean;
import com.onemenu.server.javabean.delivery.DRestaurantOrderBean;
import com.onemenu.server.model.OrderForm;
import com.onemenu.server.model.Trade;
import com.onemenu.server.protocol.req.delivery.BaseDeliveryPageReqData;

/**
 * 
 * @author lin
 * 
 */
public interface OrderFormService extends AbstractServiceInf {

	public List<OrderForm> orderByStatus(OrderForm orderForm);

	public List<OrderForm> orderByStatus(String status);

	public boolean assignOrderFormToDriver(long orderFromId, long driverId);

	public boolean confirmOrderByDriver(long orderFromId, long driverId);

	public boolean cancelOrderByDriver(long orderFromId, long driverId,boolean isValidate);

	public boolean pickedUpOrderByDriver(long orderFromId, long driverId);

	public boolean finishOrderByDriver(long orderFromId, long driverId, String tipsFee, String tipsType);

	public DOrderBean getOrderFormById(long orderFromId);

	public List<DRestaurantOrderBean> getDeliveryOrderFormListByDriverId(OrderFormQueryCondition condition);

	public List<DRestaurantOrderBean> getCallDeliveryOrderFormList(OrderFormQueryCondition condition);

	public boolean modifyBillPriceByDriver(long orderId, String billPrice);

	/**
	 * 获取相关状态的订单
	 * 
	 * @return
	 */
	public List<DDriverOrderBean> getAllOrders();

	/**
	 * 获取派送订单的位置
	 * 
	 * @param orderFormId
	 *            订单号
	 * @return 位置
	 */
	public DLocationBean getLocationByOrderId(Long orderFormId);

	/**
	 * 根据餐厅id获取新单列表
	 * 
	 * @param restId
	 *            餐厅id
	 * @return
	 */
	public List<CDRestaurantOrdersBean> getNewOrdersByRestId(Long restId);

	/**
	 * 商家确认外卖单
	 * 
	 * @param orderFormId
	 *            外卖单号
	 * @param minInterval
	 *            外卖时间分钟
	 * @return
	 */
	public boolean callDeliveryByMerchant(Long orderFormId, int minInterval);

	/**
	 * 根据餐厅id获取准备推送driver的单
	 * 
	 * @param restId
	 *            餐厅id
	 * @return
	 */
	public List<CDOrderBean> getCallDriverOrdersByRestId(Long restId);

	/**
	 * 获取已经配送的单
	 * 
	 * @param restId
	 * @return
	 */
	public List<CDOrderBean> getDriverOrdersByRestId(Long restId);

	/**
	 * 获取所有未完成的外卖单
	 * 
	 * @param restId
	 *            餐厅id
	 * @return
	 */
	public List<CDRestaurantOrdersBean> getAllCallOrdersByRestId(Long restId);

	/**
	 * 获取所有call的单
	 * 
	 * @param restId
	 * @param startNum
	 * @param range
	 * @return
	 */
	public List<CDOrderBean> getAllCallOrdersByRestId(Long restId, Integer startNum, Integer range);

	/**
	 * 通过orderId设置tipFee
	 * 
	 * @param orderId
	 * @param tipsFee
	 * @return
	 */
	public boolean resetTipsFeeByOrderId(Long orderId, BigDecimal tipsFee);

	/**
	 * 上传图片
	 * 
	 * @param orderId
	 * @param recieptImage
	 * @param width
	 * @param height
	 * @throws Exception
	 */
	public void uploadRecieptImage(Long orderId, String recieptImage, String width, String height) throws Exception;

	public List<OrderForm> getCallDeliveryOrders();

	public void addOrderListener(OrderForm order);

	public void addOrderListener(Trade trade);

	public boolean checkOrderCode(String orderCode);

	/**
	 * 获取driver的历史订单
	 * 
	 * @param data
	 * @return
	 */
	public List<DOrderBean> getHistoryOrdersByDriver(BaseDeliveryPageReqData data);

	/**
	 * 获取订单详情
	 * 
	 * @param orderId
	 * @return
	 */
	public DOrderBean getHistoryOrderDetailById(long orderId);

	/**
	 * 获取按天分类的历史订单列表
	 * 
	 * @param data
	 * @return
	 */
	public List<DHistoryDayOrderBean> getHistoryOrdersGroupByDay(BaseDeliveryPageReqData data);
	
	/**
	 * 批量确认订单
	 * @param orderFromIds
	 * @param driverId
	 * @return
	 */
	public List<Long> confirmOrdersByDriver(List<Long> orderFromIds, long driverId);
	
	/**
	 * 批量取消订单
	 * @param orderFromIds
	 * @param driverId
	 * @return
	 */
	public List<Long> cancelOrdersByDriver(List<Long> orderFromIds, long driverId);
	/**
	 * 获取统计数据
	 * @param driverId driverId
	 * @return
	 */
	public DDriverStatisticsBean getStatisticsDataByDriverId(long driverId);
	/**
	 * 商家取消订单
	 * @param orderId
	 * @return
	 */
	public boolean restCancelOrderByOrderId(Long orderId);

}
