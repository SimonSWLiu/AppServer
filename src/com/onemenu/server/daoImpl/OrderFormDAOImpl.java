package com.onemenu.server.daoImpl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.onemenu.server.constant.ParameterConstant;
import com.onemenu.server.dao.OrderFormDAO;
import com.onemenu.server.javabean.OrderFormQueryCondition;
import com.onemenu.server.javabean.OrderSequenceCondition;
import com.onemenu.server.javabean.delivery.DDriverHistoryStatistics;
import com.onemenu.server.javabean.delivery.DDriverLastWeekStatistics;
import com.onemenu.server.javabean.delivery.DDriverOrderBean;
import com.onemenu.server.javabean.delivery.DDriverStatistics;
import com.onemenu.server.javabean.delivery.DDriverStatisticsBean;
import com.onemenu.server.javabean.delivery.DDriverTodayStatistics;
import com.onemenu.server.javabean.delivery.DDriverWeekStatistics;
import com.onemenu.server.javabean.delivery.DOrderQueryBean;
import com.onemenu.server.model.OrderForm;
import com.onemenu.server.model.OrderItem;
import com.onemenu.server.model.Restaurant;
import com.onemenu.server.util.FormatUtils;

public class OrderFormDAOImpl extends BaseDAOSupport implements OrderFormDAO {

	private static final Logger	logger	= Logger.getLogger(OrderFormDAOImpl.class);

	protected HibernateTemplate	mHibernateTemplate;

	@Autowired
	public void setmHibernateTemplate(HibernateTemplate mHibernateTemplate) {
		this.mHibernateTemplate = mHibernateTemplate;
	}

	@Override
	@Transactional
	public List<OrderForm> getOrderFormListByRestaurantId(long restaurantId) {

		List<OrderForm> orderFormList = new ArrayList<OrderForm>();

		Restaurant restaurant = mHibernateTemplate.get(Restaurant.class, restaurantId);

		for (OrderForm orderForm : restaurant.getmOrderFormSet()) {

			Set<OrderItem> orderItemSet = new HashSet<OrderItem>();
			for (OrderItem orderItem : orderForm.getmOrderItemSet()) {

				orderItemSet.add(orderItem);
			}

			orderForm.setmOrderItemSet(orderItemSet);
			orderFormList.add(orderForm);
		}

		return orderFormList;
	}

	public OrderForm findById(long orderFormId) {

		OrderForm orderForm = mHibernateTemplate.get(OrderForm.class, orderFormId);

		Set<OrderItem> orderItemSet = new HashSet<OrderItem>();
		for (OrderItem orderItem : orderForm.getmOrderItemSet()) {

			orderItemSet.add(orderItem);
		}

		orderForm.setmOrderItemSet(orderItemSet);

		return orderForm;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<OrderForm> getOrderFormListByCondition(OrderFormQueryCondition condition) {

		DetachedCriteria orderFormCri = DetachedCriteria.forClass(OrderForm.class);

		if (condition.getOrderSequenceCondition() != null) {
			for (OrderSequenceCondition orderSequenceCondition : condition.getOrderSequenceCondition()) {

				if (orderSequenceCondition.getOrder().equals(ParameterConstant.ORDER_ASC)) {
					orderFormCri.addOrder(Order.asc(orderSequenceCondition.getField()));
				} else if (orderSequenceCondition.getOrder().equals(ParameterConstant.ORDER_DESC)) {
					orderFormCri.addOrder(Order.desc(orderSequenceCondition.getField()));
				}
			}
		}

		orderFormCri.setFetchMode("mOrderItemSet", FetchMode.SELECT);
		if(condition.getRestaurantId()!=null)
			orderFormCri.add(Restrictions.eq("mRestaurant.mId", condition.getRestaurantId()));
		
		if(condition.getDriverId()!=null)
			orderFormCri.add(Restrictions.eq("mDriver.mId", condition.getDriverId()));
		
		if (!TextUtils.isEmpty(condition.getStatus())) {
			orderFormCri.add(Restrictions.eq("mStatus", condition.getStatus()));
		}

		if (condition._status != null) {
			orderFormCri.add(Restrictions.in("mStatus", condition._status));
		}

		if (condition.getFromTimestamp() != null) {
			orderFormCri.add(Restrictions.ge("mCreateTimestamp", condition.getFromTimestamp()));
		}
		if (condition.getToTimestamp() != null) {
			orderFormCri.add(Restrictions.le("mCreateTimestamp", condition.getToTimestamp()));
		}
		if (condition.getIsOneMenu() != null) {
			orderFormCri.add(Restrictions.eq("mIsOneMenu", condition.getIsOneMenu()));
		}
		if (condition.startNum == null) {
			return mHibernateTemplate.findByCriteria(orderFormCri);
		}

		return mHibernateTemplate.findByCriteria(orderFormCri, condition.startNum, condition.range);
	}

	final String	pending_delivery_order_form_sql	= "SELECT of.`payment_type` as paymentType , of.`status` as status ,of.`id` AS orderId,of.`total_fee` as billPrice ,of.`restaurant_id` AS restId,r.`name` AS restName,t.`customer_address` AS custAddr,of.`create_timestamp` AS createdTime,of.`call_delivery_timestamp` AS callDeliveryTime,of.`is_one_menu` AS isOMOrder,of.`rest_confirmed_timestamp` AS restTime  FROM order_form of LEFT JOIN  `restaurant` r  ON r.`id`= of.`restaurant_id`  LEFT JOIN  `trade` t  ON t.`id` = of.`trade_id`  WHERE of.driver_id = :driverId AND of.status in (:orderFormStatus)  order by of.`restaurant_id`,of.`is_one_menu` desc ,of.status desc  ";

	@Override
	@Transactional
	public List<DOrderQueryBean> getDeliveryOrderFormListByDriverId(OrderFormQueryCondition condition) {

		Session session = mHibernateTemplate.getSessionFactory().getCurrentSession();

		Query query = session.createSQLQuery(pending_delivery_order_form_sql);

		query.setParameter("driverId", condition.getDriverId());
		query.setParameterList("orderFormStatus", deliveryOrderStatus());

		@SuppressWarnings("unchecked")
		List<DOrderQueryBean> list = query.setResultTransformer(new AliasToBeanResultTransformer(DOrderQueryBean.class)).list();

		return list;
	}

	@Override
	@Transactional
	public boolean updateOrderFormStatus(long orderFormId, String status) {

		boolean result = false;

		OrderForm orderForm = mHibernateTemplate.get(OrderForm.class, orderFormId);

		if (orderForm != null) {

			orderForm.setmStatus(status);
			mHibernateTemplate.update(orderForm);

			result = true;
		}

		return result;
	}

	final String	pending_delivery_order_form_count_sql	= "SELECT count(1)  FROM order_form of   WHERE of.driver_id = :driverId AND of.status in (:orderFormStatus)  ";

	@Override
	@Transactional
	public int getDeliveryOrderCountByDriverId(long driverId) {
		if (logger.isDebugEnabled())
			logger.debug("driverId:" + driverId);

		Session session = mHibernateTemplate.getSessionFactory().getCurrentSession();

		Query query = session.createSQLQuery(pending_delivery_order_form_count_sql);
		query.setParameter("driverId", driverId);
		query.setParameterList("orderFormStatus", deliveryOrderStatus());

		int count = Integer.valueOf(query.uniqueResult().toString());
		if (logger.isDebugEnabled())
			logger.debug("count:" + count);
		return count;
	}

	private List<String> deliveryOrderStatus() {
		return Arrays.asList(ParameterConstant.ORDER_FORM_STATUS_DRIVER_PENDING, ParameterConstant.ORDER_FORM_STATUS_DELIVERY_CONFIRMED,
				ParameterConstant.ORDER_FORM_STATUS_PICKED_UP);
	}

	final String	all_order_of_sql	= "SELECT of.`id` AS _orderId,of.`restaurant_id` AS _restId,r.`name` AS restName,of.`code` AS orderCode ,of.`status` AS `status`,of.`driver_id` AS driverId ,d.`name` AS driverName,of.`call_delivery_timestamp` AS callDeliveryTime FROM `order_form`  of LEFT JOIN  `restaurant` r  ON r.`id`= of.`restaurant_id`  LEFT JOIN `driver` d ON of.`driver_id`= d.`id` WHERE of.`status` IN (:orderFormStatus)";

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<DDriverOrderBean> getAllOrders() {

		Session session = mHibernateTemplate.getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(all_order_of_sql);
		query.setParameterList("orderFormStatus", Arrays.asList(ParameterConstant.ORDER_FORM_STATUS_CALL_DELIVERY,
				ParameterConstant.ORDER_FORM_STATUS_DRIVER_PENDING, ParameterConstant.ORDER_FORM_STATUS_DELIVERY_CONFIRMED,ParameterConstant.ORDER_FORM_STATUS_PICKED_UP));

		return query.setResultTransformer(new AliasToBeanResultTransformer(DDriverOrderBean.class)).list();
	}

	@Override
	@Transactional
	public DDriverStatisticsBean getStatisticsDataByDriverId(long driverId) {
		
		DDriverStatisticsBean bean = new DDriverStatisticsBean();
		bean.today = getStatisticsData(driverId, StatisticsType.Today);
		bean.week = getStatisticsData(driverId, StatisticsType.Week);
		bean.history = getStatisticsData(driverId, StatisticsType.All);
		bean.lastWeek= getStatisticsData(driverId, StatisticsType.LastWeek);
		return bean;
	}
	
	
	final String order_sql = "SELECT COUNT(1) AS  `OrderAmount` ,SUM(of.total_fee) AS  `OrderPrice`,SUM(of.delivery_fee) AS  `DeliveryFee`  FROM order_form of WHERE  of.driver_id = :driverId AND of.status=1 "; 
	
	final String cash_order_price_sql = "SELECT SUM(of.total_fee)  AS  `OrderPrice`  FROM order_form of WHERE  of.driver_id = :driverId  AND of.status=1 AND of.payment_type = 1 "; 
	
	final String noncash_tips_fee_sql = " SELECT SUM(of.tips_fee)  AS  `tipsFee`  FROM order_form of WHERE  of.driver_id = :driverId  AND of.status=1 AND of.tips_type = 2 ";
	
	final String create_time_part_sql = " AND of.create_timestamp >= :fromTime AND of.create_timestamp < :toTime  ";
	
	
	private DDriverStatistics getStatisticsData(long driverId,StatisticsType type){
		
		DDriverStatistics data = createStatisticsObject(type);
		String orderSql = order_sql;
		String cashOrderPriceSql = cash_order_price_sql;
		String noncashTipsFeeSql = noncash_tips_fee_sql;
		Date fromDate = null;
		Date toDate = null;
		if(type == StatisticsType.Today || type ==StatisticsType.Week ||type==StatisticsType.LastWeek ){
			orderSql +=create_time_part_sql;
			cashOrderPriceSql+=create_time_part_sql;
			noncashTipsFeeSql += create_time_part_sql;
			fromDate = getFromTime(type);
			toDate = getToTime(type);
		}
		
		Session session = mHibernateTemplate.getSessionFactory().getCurrentSession();
		Query orderQuery = session.createSQLQuery(orderSql);
		orderQuery.setParameter("driverId", driverId);
		if(type == StatisticsType.Today || type ==StatisticsType.Week ||type==StatisticsType.LastWeek){
			orderQuery.setParameter("fromTime", fromDate);
			orderQuery.setParameter("toTime", toDate);
		}
		
		Object[]  orderReqsult = (Object[]) orderQuery.uniqueResult();
		BigInteger orderAmount = (BigInteger) orderReqsult[0];
		BigDecimal orderPrice = (BigDecimal) orderReqsult[1];
		Double delivery_fee =(Double) orderReqsult[2];
		
		Query cashOrderPriceQuery = session.createSQLQuery(cashOrderPriceSql);
		cashOrderPriceQuery.setParameter("driverId", driverId);
		if(type == StatisticsType.Today || type ==StatisticsType.Week ||type==StatisticsType.LastWeek){
			cashOrderPriceQuery.setParameter("fromTime", fromDate);
			cashOrderPriceQuery.setParameter("toTime", toDate);
		}
		
		BigDecimal  cashOrderPriceReqsult = (BigDecimal) cashOrderPriceQuery.uniqueResult();
		
		
		Query noncashTipsFeeQuery = session.createSQLQuery(noncashTipsFeeSql);
		noncashTipsFeeQuery.setParameter("driverId", driverId);
		if(type == StatisticsType.Today || type ==StatisticsType.Week ||type==StatisticsType.LastWeek){
			noncashTipsFeeQuery.setParameter("fromTime", fromDate);
			noncashTipsFeeQuery.setParameter("toTime", toDate);
		}
		
		Double  noncashTipsFeeReqsult = (Double) noncashTipsFeeQuery.uniqueResult();
		
		data.setOrderPrice(FormatUtils.formatPrice(orderPrice));
		data.setCashOrderPrice(FormatUtils.formatPrice(cashOrderPriceReqsult));
		data.setDeliveryFee(FormatUtils.formatPrice(delivery_fee));
		if(delivery_fee==null&&noncashTipsFeeReqsult==null)
			data.setIncome("0.00");
		else if(delivery_fee==null){
			data.setIncome(FormatUtils.formatPrice(noncashTipsFeeReqsult));
		}else if(noncashTipsFeeReqsult==null){
			data.setIncome(FormatUtils.formatPrice(delivery_fee));
		}else{
			data.setIncome(FormatUtils.formatPrice(delivery_fee+noncashTipsFeeReqsult));
		}
		data.setNonCashTipsFee(FormatUtils.formatPrice(noncashTipsFeeReqsult));
		data.setOrderAmount(orderAmount==null?"0":String.valueOf(orderAmount));
		return data;
	}
	
	
	private DDriverStatistics createStatisticsObject(StatisticsType type) {
		switch (type) {
		case Today:
			return new DDriverTodayStatistics();
		case Week:
			return new DDriverWeekStatistics();
		case LastWeek:
			return new DDriverLastWeekStatistics();
		default:
			return new DDriverHistoryStatistics();
		}
	}

	private Date getToTime(StatisticsType type){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		if(type==StatisticsType.LastWeek){
			//1:Sun, 2:Mon, 3:Tue, 4:Wed, 5:Thu, 6:Fri, 7:Sat
			int week = calendar.get(Calendar.DAY_OF_WEEK);
			int val = 2 - week;
			calendar.add(Calendar.DATE,(val > 0 ? (val-7) : val ) );
		}else{
			calendar.add(Calendar.DATE, 1);
		}
		
		return calendar.getTime();
	}
	
	private Date getFromTime(StatisticsType type){
		
		Calendar calendar = Calendar.getInstance();
		if(type==StatisticsType.Week){
			//1:Sun, 2:Mon, 3:Tue, 4:Wed, 5:Thu, 6:Fri, 7:Sat
			int week = calendar.get(Calendar.DAY_OF_WEEK);
			int val = 2 - week;
			calendar.add(Calendar.DATE,(val > 0 ? (val-7) : val ) );
		}else if(type==StatisticsType.LastWeek){
			int week = calendar.get(Calendar.DAY_OF_WEEK);
			int val = 2 - week;
			calendar.add(Calendar.DATE,(val > 0 ? (val-14) : val-7 ) );
		}
			
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public enum StatisticsType{
		Today,Week,LastWeek,All;
	}

}
