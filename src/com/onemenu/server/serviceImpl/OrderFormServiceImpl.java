package com.onemenu.server.serviceImpl;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.onemenu.server.constant.ParameterConstant;
import com.onemenu.server.dao.OrderFormDAO;
import com.onemenu.server.javabean.OrderFormQueryCondition;
import com.onemenu.server.javabean.OrderSequenceCondition;
import com.onemenu.server.javabean.calldelivery.CDCustomerBean;
import com.onemenu.server.javabean.calldelivery.CDDishBean;
import com.onemenu.server.javabean.calldelivery.CDDishIngredientBean;
import com.onemenu.server.javabean.calldelivery.CDOrderBean;
import com.onemenu.server.javabean.calldelivery.CDRestaurantOrdersBean;
import com.onemenu.server.javabean.delivery.DCustomerBean;
import com.onemenu.server.javabean.delivery.DDishBean;
import com.onemenu.server.javabean.delivery.DDriverOrderBean;
import com.onemenu.server.javabean.delivery.DDriverStatisticsBean;
import com.onemenu.server.javabean.delivery.DHistoryDayOrderBean;
import com.onemenu.server.javabean.delivery.DLocationBean;
import com.onemenu.server.javabean.delivery.DOrderBean;
import com.onemenu.server.javabean.delivery.DOrderQueryBean;
import com.onemenu.server.javabean.delivery.DRestaurantBean;
import com.onemenu.server.javabean.delivery.DRestaurantOrderBean;
import com.onemenu.server.model.Customer;
import com.onemenu.server.model.Driver;
import com.onemenu.server.model.OrderForm;
import com.onemenu.server.model.OrderItem;
import com.onemenu.server.model.OrderItemIngredient;
import com.onemenu.server.model.Restaurant;
import com.onemenu.server.model.Trade;
import com.onemenu.server.protocol.req.delivery.BaseDeliveryPageReqData;
import com.onemenu.server.service.DriverService;
import com.onemenu.server.service.OrderFormService;
import com.onemenu.server.util.AwsS3Utils;
import com.onemenu.server.util.EmailUtils;
import com.onemenu.server.util.ImageUtils;
import com.onemenu.server.util.MD5Utils;
import com.onemenu.server.util.MimeUtils;
import com.onemenu.server.util.StringUtil;
import com.onemenu.server.util.TimestampUtil;
import com.onemenu.server.util.TimestampUtil.DateFormatType;

public class OrderFormServiceImpl extends AbstractServiceImpl implements OrderFormService {
	
	private static final Logger logger = Logger.getLogger(OrderFormServiceImpl.class);
	@Resource(name = "driverService")
	private DriverService		mDriverService;
	
	private OrderFormDAO	mOrderFormDAO;

	@Autowired
	public void setmOrderFormDAO(OrderFormDAO mOrderFormDAO) {
		this.mOrderFormDAO = mOrderFormDAO;
	}

	@Override
	public List<OrderForm> orderByStatus(OrderForm orderForm) {
		DetachedCriteria orderFormCri = DetachedCriteria.forClass(OrderForm.class);

		orderFormCri.add(Restrictions.eq("mStatus", orderForm.getmStatus()));

		List<OrderForm> list = getBaseDAO().findByCriteria(orderFormCri, OrderForm.class);

		if (list.size() != 0) {
			return list;
		} else if (list.size() == 0) {
			return null;
		}

		return null;
	}

	@Override
    @Transactional
    public boolean assignOrderFormToDriver(long orderFromId, long driverId) {

        boolean result = false;

        try {
            
            OrderForm orderForm = findById(OrderForm.class, orderFromId);
            String status = orderForm.getmStatus();
            if(ParameterConstant.ORDER_FORM_STATUS_CALL_DELIVERY.equals(status)||ParameterConstant.ORDER_FORM_STATUS_DRIVER_PENDING.equals(status)){
                orderForm.setmStatus(ParameterConstant.ORDER_FORM_STATUS_DRIVER_PENDING);
                Driver driver = new Driver();
                driver.setmId(driverId);
                orderForm.setmDriver(driver);
                orderForm.setmDriverPendingTimestamp(TimestampUtil.getCurrentTimestamp());
                update(orderForm);
                result = true;
            }
        } catch (Exception e) {
            logger.error("", e);
        }

        return result;
    }
	
	@Override
	@Transactional
	public boolean confirmOrderByDriver(long orderFromId,long driverId) {

		boolean result = false;

		try {
			
			OrderForm orderForm = findById(OrderForm.class, orderFromId);
			String status = orderForm.getmStatus();
			Driver driver = orderForm.getmDriver();
			if(driver!=null){
				//只能确认自己的单
				if(ParameterConstant.ORDER_FORM_STATUS_DRIVER_PENDING.equals(status) && driverId==driver.getmId()){
					orderForm.setmStatus(ParameterConstant.ORDER_FORM_STATUS_DELIVERY_CONFIRMED);
					orderForm.setmDriverConfirmedTimestamp(TimestampUtil.getCurrentTimestamp());
					update(orderForm);
					result = true;
				}	
			}
			
		} catch (Exception e) {
			logger.error("", e);
		}

		return result;
	}

	@Override
	@Transactional
	public boolean cancelOrderByDriver(long orderFromId,long driverId,boolean isValidate) {

		boolean result = false;

		try {
			OrderForm orderForm = findById(OrderForm.class, orderFromId);
			String status = orderForm.getmStatus();
			if(ParameterConstant.ORDER_FORM_STATUS_DRIVER_PENDING.equals(status)){
			    orderForm.setmStatus(ParameterConstant.ORDER_FORM_STATUS_CALL_DELIVERY);
			    orderForm.setmDriver(null);
				orderForm.setmCancelTimestamp(TimestampUtil.getCurrentTimestamp());
				update(orderForm);
				if(isValidate){
					int count = mOrderFormDAO.getDeliveryOrderCountByDriverId(driverId);
					//driver 已无需派送的单
					if(count==1){
						Driver driver = findById(Driver.class,driverId);
						driver.setmStatus(ParameterConstant.DRIVER_STATUS_AVAILIABLE);
						update(driver);
					}	
				}
				result = true;
			}
		} catch (Exception e) {
			logger.error("", e);
		}

		return result;
	}

	@Override
	@Transactional
	public boolean finishOrderByDriver(long orderFromId,long driverId,String tipsFee, String tipsType) {

		boolean result = false;

		try {
			OrderForm orderForm = findById(OrderForm.class, orderFromId);
			String status = orderForm.getmStatus();
			if(ParameterConstant.ORDER_FORM_STATUS_PICKED_UP.equals(status)){
			    orderForm.setmStatus(ParameterConstant.ORDER_FORM_STATUS_FINISHED);
				orderForm.setmArrivedTimestamp(TimestampUtil.getCurrentTimestamp());
				orderForm.setTipsType(tipsType);
				if(ParameterConstant.ORDER_FORM_TIPS_TYPE_NONCASH.equals(tipsType)){
					orderForm.setmTipsFee(new BigDecimal(tipsFee));
				}
				update(orderForm);
				int count = mOrderFormDAO.getDeliveryOrderCountByDriverId(driverId);
				//driver 已无需派送的单
				if(count==1){
					Driver driver = findById(Driver.class,driverId);
					driver.setmStatus(ParameterConstant.DRIVER_STATUS_AVAILIABLE);
					update(driver);
				}
				
				
				result = true;
			}else{
				logger.error("orderForm status is error ! status:"+status);
			}
		} catch (Exception e) {
			logger.error("tipsFee:"+tipsFee+",tipsType:"+tipsType, e);
		}

		return result;
	}

	@Override
	public DOrderBean getOrderFormById(long id) {
		OrderForm orderOrg = getBaseDAO().findById(OrderForm.class, id);
		if(orderOrg==null)
			return null;
		DOrderBean bean = new DOrderBean();
		Trade trade = orderOrg.getmTrade();
		bean.customer = new DCustomerBean();
		
		if(trade!=null){
			bean.customer.custAddr = trade.getmCustomerAddress();
			//bean.customer.custLatitude = trade.getmCustomer().getmAccount().
			bean.customer.custName = trade.getmCustomerName();
			bean.customer.custPhone= trade.getmCustomerPhone();
		}
		
		
		
		
		bean.orderCode = orderOrg.getmCode();
		bean.isOMOrder = String.valueOf(orderOrg.getmIsOneMenu());
		bean.status = orderOrg.getmStatus();
		bean.paymentType = orderOrg.getmPaymentType();
		
		bean.billPrice = String.valueOf(orderOrg.getmTotalFee());
		
		
		 Set<OrderItem>  orderItems = orderOrg.getmOrderItemSet();
		 
		 bean.dishArray = new ArrayList<DDishBean>(orderItems.size());
		 
		 for(OrderItem orderItem:orderItems){
			 DDishBean _bean = new DDishBean();
			 _bean.dishName = orderItem.getmDishName();
			 _bean.dishPrice = orderItem.getmDishPrice();
			 
			 Set<OrderItemIngredient> ingredients = orderItem.getmOrderItemIngredientSet();
			 
			 if(ingredients!=null){
				 _bean.ingredientArray = new ArrayList<String>(ingredients.size());
				 for(OrderItemIngredient ingredient:ingredients){
					 _bean.ingredientArray.add(ingredient.getmName()); 
				 }
			 }
			 bean.dishArray.add(_bean);
		 }
		
		
		return bean;
	}

	@Override
	public List<DRestaurantOrderBean> getDeliveryOrderFormListByDriverId(OrderFormQueryCondition condition) {

		List<DOrderQueryBean> orderFormList = mOrderFormDAO.getDeliveryOrderFormListByDriverId(condition);

		List<DRestaurantOrderBean> orderFormBeanList = new ArrayList<DRestaurantOrderBean>();
		

		if (orderFormList == null || orderFormList.isEmpty())
			return orderFormBeanList;

		Timestamp requestTime = TimestampUtil.getCurrentTimestamp();
		BigInteger lastRestId = null;
		DRestaurantOrderBean lastRestOrder = null;
		for (DOrderQueryBean order : orderFormList) {
			BigInteger restId = order.restId;
			//
			if (!restId.equals(lastRestId)) {
				lastRestId = restId;
				DRestaurantBean restbean = new DRestaurantBean();
				restbean.restId = String.valueOf(restId);
				restbean.restName = order.restName;
				DRestaurantOrderBean rOrder = new DRestaurantOrderBean();
				rOrder.restaurant = restbean;
				rOrder.orders = new ArrayList<DOrderBean>();
				lastRestOrder = rOrder;
				orderFormBeanList.add(rOrder);
			}

			DOrderBean bean = new DOrderBean();
			bean.billPrice = String.valueOf(order.billPrice);
			bean.isOMOrder = String.valueOf(order.isOMOrder);
			bean.orderId = String.valueOf(order.orderId);
			bean.paymentType = order.paymentType;
			bean.status = order.status;
			bean.customer = new DCustomerBean();
			bean.customer.custAddr = order.custAddr;

			bean.orderReadyTime = TimestampUtil.timeIntervalInMin(requestTime, order.callDeliveryTime);

			lastRestOrder.orders.add(bean);

		}

		return orderFormBeanList;
	}

	@Override
    public List<DRestaurantOrderBean> getCallDeliveryOrderFormList(OrderFormQueryCondition condition) {

        List<OrderForm> orderFormList = mOrderFormDAO.getOrderFormListByCondition(condition);

        List<DRestaurantOrderBean> orderFormBeanList = new ArrayList<DRestaurantOrderBean>();
        
        if (orderFormList == null || orderFormList.isEmpty())
            return orderFormBeanList;

        Timestamp requestTime = TimestampUtil.getCurrentTimestamp();
        BigInteger lastRestId = null;
        DRestaurantOrderBean lastRestOrder = null;
        for (OrderForm orderForm : orderFormList) {
            
            BigInteger restId = BigInteger.valueOf(orderForm.getmRestaurant().getmId());
            //
            if (!restId.equals(lastRestId)) {
                lastRestId = restId;
                DRestaurantBean restbean = new DRestaurantBean();
                restbean.restId = String.valueOf(restId);
                restbean.restName = orderForm.getmRestaurant().getmName();
                DRestaurantOrderBean rOrder = new DRestaurantOrderBean();
                rOrder.restaurant = restbean;
                rOrder.orders = new ArrayList<DOrderBean>();
                lastRestOrder = rOrder;
                orderFormBeanList.add(rOrder);
            }

            DOrderBean bean = new DOrderBean();
            bean.billPrice = String.valueOf(orderForm.getmTotalFee());
            bean.isOMOrder = String.valueOf(orderForm.getmIsOneMenu());
            bean.orderId = String.valueOf(orderForm.getmId());
            bean.status = orderForm.getmStatus();
            bean.customer = new DCustomerBean();
            bean.customer.custAddr = orderForm.getmTrade().getmCustomerAddress();

            bean.orderReadyTime = TimestampUtil.timeIntervalInMin(requestTime, (Timestamp)orderForm.getmCallDeliveryTimestamp());

            lastRestOrder.orders.add(bean);

        }

        return orderFormBeanList;
    }

	@Override
	public List<OrderForm> orderByStatus(String status) {
		
		DetachedCriteria orderFormCri = DetachedCriteria.forClass(OrderForm.class);
		orderFormCri.add(Restrictions.eq("mStatus", status));
		List<OrderForm> list = getBaseDAO().findByCriteria(orderFormCri, OrderForm.class);
		return list;
	}

	@Override
	public boolean modifyBillPriceByDriver(long orderId, String billPrice) {
		boolean result = false;

		try {
			OrderForm orderForm = findById(OrderForm.class, orderId);
			orderForm.setDriverBillPrice(BigDecimal.valueOf(Double.valueOf(billPrice)));
			update(orderForm);
			result = true;
		} catch (Exception e) {
			logger.error("", e);
		}

		return result;
	}

	@Override
	public List<DDriverOrderBean> getAllOrders() {
		
		List<DDriverOrderBean> allOrders = mOrderFormDAO.getAllOrders();
		Timestamp requestTime = TimestampUtil.getCurrentTimestamp();
		for(DDriverOrderBean bean:allOrders){
			bean.inReadyTime = TimestampUtil.timeIntervalInMin(requestTime, bean.callDeliveryTime);
			bean.callDeliveryTime = null;	
			bean.restId = String.valueOf(bean._restId);
			bean.orderId = String.valueOf(bean._orderId);
			bean._orderId = null;
			bean._restId = null;
		}
		
		return allOrders;
	}

	@Override
	@Transactional
	public DLocationBean getLocationByOrderId(Long orderFormId) {
		if(orderFormId==null)
			return null;
		OrderForm orderForm = findById(OrderForm.class, orderFormId);
		if(orderForm==null)
			return null;
		Driver driver = orderForm.getmDriver();
		if(driver==null)
			return null;
		
		DLocationBean bean  = new DLocationBean();
		bean.setLatitude(driver.getLatitude());
		bean.setLongitude(driver.getLongitude());
		
		return bean;
	}

	@Override
	public List<CDRestaurantOrdersBean> getNewOrdersByRestId(Long restId) {
		OrderFormQueryCondition condition = new OrderFormQueryCondition();
		condition.setRestaurantId(restId);
		condition.setStatus(ParameterConstant.ORDER_FORM_STATUS_RESTAURANT_PENDING);
		condition.setIsOneMenu(ParameterConstant.ORDER_FORM_IS_ONE_MENU);
		List<OrderForm> orderFormList = mOrderFormDAO.getOrderFormListByCondition(condition);
		
		if(orderFormList==null||orderFormList.isEmpty())
			return null;
		
		List<CDRestaurantOrdersBean> data = transform(orderFormList);
		return data;
	}

	@Override
	@Transactional
	public boolean callDeliveryByMerchant(Long orderFormId, int minInterval) {
		boolean result = false;

		try {
			
			OrderForm orderForm = findById(OrderForm.class, orderFormId);
			//String status = orderForm.getmStatus();
			orderForm.setmStatus(ParameterConstant.ORDER_FORM_STATUS_CALL_DELIVERY);
			orderForm.setmCallDeliveryTimestamp(TimestampUtil.getMinIntervalTimestamp(minInterval));
			update(orderForm);
			result = true;
		} catch (Exception e) {
			logger.error("", e);
		}

		return result;
	}
	
	
	
	
	
	@Override
	public List<CDOrderBean> getCallDriverOrdersByRestId(Long restId) {
		OrderFormQueryCondition condition = new OrderFormQueryCondition();
		condition.setRestaurantId(restId);
		condition.setStatus(ParameterConstant.ORDER_FORM_STATUS_CALL_DELIVERY);
		//condition.setIsOneMenu(ParameterConstant.ORDER_FORM_IS_ONE_MENU);
		List<OrderForm> orderFormList = mOrderFormDAO.getOrderFormListByCondition(condition);
		
		if(orderFormList==null||orderFormList.isEmpty())
			return null;
		
		List<CDOrderBean> data = new ArrayList<CDOrderBean>(orderFormList.size());
		for(OrderForm order:orderFormList){
			CDOrderBean _order = new CDOrderBean();
			_order.orderId = String.valueOf(order.getmId());
			_order.orderCode = order.getmCode();
			_order.orderPrice = String.valueOf(order.getmTotalFee());
			_order.orderCreateTimestamp = TimestampUtil.dateToStr(order.getmCreateTimestamp(), DateFormatType.DateTime);
			data.add(_order);
		}
		return data;
	}

	private List<CDRestaurantOrdersBean> transform(List<OrderForm> orderFormList) {
		
		List<CDRestaurantOrdersBean> data = new ArrayList<CDRestaurantOrdersBean>(orderFormList.size());
		
		for(OrderForm order:orderFormList){
			CDRestaurantOrdersBean _restOrder = new CDRestaurantOrdersBean();
			data.add(_restOrder);
			Trade trade = order.getmTrade();
			CDOrderBean _order = new CDOrderBean();
			_order.orderCode = order.getmCode();
			_order.orderCouponFee = String.valueOf(order.getmDiscountFee());
			_order.orderDeliveryFee = String.valueOf(order.getmDeliveryFee());
			_order.orderId = String.valueOf(order.getmId());
			
			_order.orderSubtotalFee = String.valueOf(order.getmSubtotalFee());
			_order.orderTipsFee = String.valueOf(order.getmTipsFee());
			_order.orderTotalFee = String.valueOf(order.getmTotalFee());
			_order.orderTime = String.valueOf(order.getmCreateTimestamp());
			
			_restOrder.order = _order;
			
			CDCustomerBean customer = new CDCustomerBean();
			
			if(trade!=null){
				_order.orderPaymentType = String.valueOf(trade.getmPaymentType());
				_order.orderServiceType =String.valueOf(trade.getmGetType());
				customer.customerAddr = trade.getmCustomerAddress();
				Customer _customer = trade.getmCustomer();
				customer.customerId = String.valueOf(_customer==null?"":_customer.getmId());
				customer.customerName = trade.getmCustomerName();
				customer.customerPhone = trade.getmCustomerPhone();
			}
			
			_restOrder.customer = customer;
			
			Set<OrderItem> orderItems = order.getmOrderItemSet();
			
			if(orderItems==null||orderItems.isEmpty()){
				_restOrder.dishArray = new ArrayList<CDDishBean>(1);
				continue;
			}
				
			
			_restOrder.dishArray = new ArrayList<CDDishBean>(orderItems.size());
			
			for(OrderItem item:orderItems){
				CDDishBean dish = new CDDishBean();
				_restOrder.dishArray.add(dish);
				dish.dishAmount = item.getmDishAmount();
				dish.dishId = item.getmDishId();
				dish.dishName = item.getmDishName();
				dish.dishPrice = item.getmDishPrice();
				dish.dishTotalPrice = String.valueOf(item.getmPrice());
				
				 Set<OrderItemIngredient>  itemIngs =item.getmOrderItemIngredientSet();
				
				if(itemIngs==null || itemIngs.isEmpty()){
					dish.dishIngredientArray = new ArrayList<CDDishIngredientBean>(1);
					continue;
				}
				
				dish.dishIngredientArray = new ArrayList<CDDishIngredientBean>(itemIngs.size());
				
				for(OrderItemIngredient itemIng:itemIngs){
					CDDishIngredientBean dishIng = new CDDishIngredientBean();
					dish.dishIngredientArray.add(dishIng);
					dishIng.ingredientId = String.valueOf(itemIng.getmId());
					dishIng.ingredientName = itemIng.getmName();
					dishIng.ingredientPrice = String.valueOf(itemIng.getmPrice());
				}
			}
			
		}
		return data;
	}

	@Override
	public List<CDOrderBean> getDriverOrdersByRestId(Long restId) {
		OrderFormQueryCondition condition = new OrderFormQueryCondition();
		condition.setRestaurantId(restId);
		condition._status = Arrays.asList(ParameterConstant.ORDER_FORM_STATUS_DRIVER_PENDING,ParameterConstant.ORDER_FORM_STATUS_DELIVERY_CONFIRMED,ParameterConstant.ORDER_FORM_STATUS_PICKED_UP);
		//condition.setIsOneMenu(ParameterConstant.ORDER_FORM_IS_ONE_MENU);
		List<OrderForm> orderFormList = mOrderFormDAO.getOrderFormListByCondition(condition);
		
		if(orderFormList==null||orderFormList.isEmpty())
			return null;
		
		List<CDOrderBean> data = new ArrayList<CDOrderBean>(orderFormList.size());
		for(OrderForm order:orderFormList){
			CDOrderBean _order = new CDOrderBean();
			_order.orderId = String.valueOf(order.getmId());
			_order.orderCode = order.getmCode();
			_order.orderPrice = String.valueOf(order.getmTotalFee());
			_order.orderCreateTimestamp = TimestampUtil.dateToStr(order.getmCreateTimestamp(), DateFormatType.DateTime);
			Driver driver = order.getmDriver();
			if(driver==null){
				_order.driverId = "";
				_order.driverName = "";
			}else{
				_order.driverId = String.valueOf(driver.getmId());
				_order.driverName = driver.getmName();
			}
			data.add(_order);
		}
		return data;
	}

	@Override
	public List<CDRestaurantOrdersBean> getAllCallOrdersByRestId(Long restId) {
		OrderFormQueryCondition condition = new OrderFormQueryCondition();
		condition.setRestaurantId(restId);
		condition._status = Arrays.asList(ParameterConstant.ORDER_FORM_STATUS_CALL_DELIVERY,ParameterConstant.ORDER_FORM_STATUS_DRIVER_PENDING,ParameterConstant.ORDER_FORM_STATUS_DELIVERY_CONFIRMED);
		//condition.setIsOneMenu(ParameterConstant.ORDER_FORM_IS_ONE_MENU);
		List<OrderForm> orderFormList = mOrderFormDAO.getOrderFormListByCondition(condition);
		
		if(orderFormList==null||orderFormList.isEmpty())
			return null;
		
		List<CDRestaurantOrdersBean> data = transform(orderFormList);
		return data;
	}

	@Override
	public List<CDOrderBean> getAllCallOrdersByRestId(Long restId, Integer startNum, Integer range) {
		OrderFormQueryCondition condition = new OrderFormQueryCondition();
		condition.setRestaurantId(restId);
		condition._status = Arrays.asList(ParameterConstant.ORDER_FORM_STATUS_RESTAURANT_CANCEL,ParameterConstant.ORDER_FORM_STATUS_RESTAURANT_CANCEL_AFTER_PICK_UP,ParameterConstant.ORDER_FORM_STATUS_CALL_DELIVERY,ParameterConstant.ORDER_FORM_STATUS_DRIVER_PENDING,ParameterConstant.ORDER_FORM_STATUS_DELIVERY_CONFIRMED,ParameterConstant.ORDER_FORM_STATUS_PICKED_UP,ParameterConstant.ORDER_FORM_STATUS_FINISHED);
		//condition.setIsOneMenu(ParameterConstant.ORDER_FORM_IS_ONE_MENU);
		condition.startNum = startNum;
		condition.range = range;
		
		List<OrderSequenceCondition> orderSequenceCondition = new ArrayList<OrderSequenceCondition>(1);
		orderSequenceCondition.add(new OrderSequenceCondition(ParameterConstant.ORDER_DESC,"mId"));
		condition.setOrderSequenceCondition(orderSequenceCondition );
		List<OrderForm> orderFormList = mOrderFormDAO.getOrderFormListByCondition(condition);
		
		if(orderFormList==null||orderFormList.isEmpty())
			return null;
		
		List<CDOrderBean> data = new ArrayList<CDOrderBean>(orderFormList.size());
		for(OrderForm order:orderFormList){
			CDOrderBean _order = new CDOrderBean();
			_order.orderId = String.valueOf(order.getmId());
			_order.orderCode = order.getmCode();
			_order.orderPrice = String.valueOf(order.getmTotalFee());
			_order.orderTipsFee = String.valueOf(order.getmTipsFee());
			_order.orderTipsType = String.valueOf(order.getTipsType());
			_order.paymentType = order.getmPaymentType();
			_order.status = order.getmStatus();	
			_order.receiptImageUrl = StringUtil.isEmpty(order.getmReceiptImageUrl())?null:AwsS3Utils.getAwsS3OrderResUrl() + order.getmReceiptImageUrl();
			data.add(_order);
		}
		return data;
	}

	@Override
	@Transactional
	public boolean resetTipsFeeByOrderId(Long orderFromId, BigDecimal tipsFee) {
		boolean result = false;
		try {
			OrderForm orderForm = findById(OrderForm.class, orderFromId);
			if(orderForm==null)
				return false;
			BigDecimal oldTipsFee = orderForm.getmTipsFee();
			orderForm.setmTipsFee(tipsFee);
			update(orderForm);
			
			EmailUtils.sendOrderFormToDriver(orderForm, oldTipsFee);
			
			result  = true;
		} catch (Exception e) {
			logger.error("tipsFee:"+tipsFee, e);
		}

		return result;
	}

	@Override
	@Transactional
	public boolean pickedUpOrderByDriver(long orderFromId, long driverId) {
		boolean result = false;

		try {
			OrderForm orderForm = findById(OrderForm.class, orderFromId);
			String status = orderForm.getmStatus();
			if(ParameterConstant.ORDER_FORM_STATUS_DELIVERY_CONFIRMED.equals(status)){
			    orderForm.setmStatus(ParameterConstant.ORDER_FORM_STATUS_PICKED_UP);
				orderForm.setmDriverPickedUpTimestamp(TimestampUtil.getCurrentTimestamp());
				update(orderForm);
				result = true;
			}
		} catch (Exception e) {
			logger.error("", e);
		}

		return result;
	}

	@Override
	@Transactional
	public void uploadRecieptImage(Long orderFromId, String recieptImage, String width, String height) throws Exception {
		// generate a file name
        String folderName =
                MD5Utils.getHashCodePrefix(String.valueOf(System.currentTimeMillis()))
                        + System.currentTimeMillis();
        // Generate image name
        String mimetype = AwsS3Utils.getMineType(recieptImage);
        String extension = MimeUtils.guessExtensionFromMimeType(mimetype);
        String imageName = AwsS3Utils.generateImageName(width, height, extension);

        // upload image to s3
        InputStream in = ImageUtils.getInputStreamFromBase64(recieptImage);
        ObjectMetadata metaddata = new ObjectMetadata();
        metaddata.setContentType(mimetype);
        metaddata.setContentLength(in.available());
        AwsS3Utils.uploadFileToAwsS3OrderRes(folderName, imageName, in, metaddata);
        OrderForm orderForm = findById(OrderForm.class, orderFromId);
        orderForm.setmReceiptImageUrl(folderName+"/"+imageName);
        updateTrd(orderForm);        
		
	}

	@Override
	public List<OrderForm> getCallDeliveryOrders() {
		
		DetachedCriteria orderFormCri = DetachedCriteria.forClass(OrderForm.class);
		
		
		Timestamp time = new Timestamp(System.currentTimeMillis()-180*1000);
		LogicalExpression  unconfimExp = Restrictions.and(Restrictions.eq("mStatus", ParameterConstant.ORDER_FORM_STATUS_DRIVER_PENDING), Restrictions.le("mDriverPendingTimestamp", time));
		
		orderFormCri.add(Restrictions.or(Restrictions.eq("mStatus", ParameterConstant.ORDER_FORM_STATUS_CALL_DELIVERY), unconfimExp));
		
		return getBaseDAO().findByCriteria(orderFormCri, OrderForm.class);
	}

	@Override
	@Transactional
	public void addOrderListener(OrderForm order) {
		
		Restaurant restaurant = order.getmRestaurant();
		if(restaurant==null)
			return;
		long restId = restaurant.getmId();
		
		OrderFormQueryCondition condition = new OrderFormQueryCondition();
		condition.setRestaurantId(restId);
		condition.setStatus(ParameterConstant.ORDER_FORM_STATUS_DELIVERY_CONFIRMED);
		List<OrderForm> orderFormList = mOrderFormDAO.getOrderFormListByCondition(condition);
		if(orderFormList==null||orderFormList.isEmpty())
			return;
		
		Driver driver = null;
		//加入当前送餐者当前送餐的数量校验
		for(int i=0;i<orderFormList.size();i++){
			
			driver = orderFormList.get(i).getmDriver();
			
			if(driver==null)
				continue;
			
			int count = mOrderFormDAO.getDeliveryOrderCountByDriverId(driver.getmId());
			
			if(count<4)
				break;
			
			driver = null;
		}
		
		if(driver==null)
			return;
		
		order.setmDriver(driver);
		order.setmStatus(ParameterConstant.ORDER_FORM_STATUS_DRIVER_PENDING);
		order.setmDriverPendingTimestamp(TimestampUtil.getCurrentTimestamp());
        saveOrUpdate(order);
        
        try {
			mDriverService.pushToEndpoint(driver, "you have new order", 1);
		} catch (Exception e) {
			logger.error("", e);
		}
		
	}

	@Override
	public void addOrderListener(Trade trade) {
		if(trade==null)
			return;
		Set<OrderForm> orders = trade.getmOrderFormSet();
		
		if(orders==null)
			return ;
		
		for(OrderForm order:orders){
			addOrderListener(order);
		}
		
	}

	@Override
	public boolean checkOrderCode(String orderCode) {
		long count = findRowCount(OrderForm.class, "mCode", orderCode);
		return count==0;
	}

	@Override
	public List<DOrderBean> getHistoryOrdersByDriver(BaseDeliveryPageReqData data) {
		OrderFormQueryCondition condition = new OrderFormQueryCondition();
		condition.setDriverId(Long.valueOf(data.driverId));;
		condition.startNum = Integer.valueOf(data.startNum);
		condition.range = Integer.valueOf(data.range);
		condition.setStatus(ParameterConstant.ORDER_FORM_STATUS_FINISHED);
		
		List<OrderSequenceCondition> orderSequenceCondition = new ArrayList<OrderSequenceCondition>(1);
		orderSequenceCondition.add(new OrderSequenceCondition(ParameterConstant.ORDER_DESC,"mId"));
		condition.setOrderSequenceCondition(orderSequenceCondition );
		List<OrderForm> orderFormList = mOrderFormDAO.getOrderFormListByCondition(condition);
		if(orderFormList==null||orderFormList.isEmpty())
			return null;
		
		List<DOrderBean> forms = new ArrayList<DOrderBean>(orderFormList.size());
		
		for(OrderForm order:orderFormList){
			DOrderBean bean = new DOrderBean();
			bean.billPrice = String.valueOf(order.getmTotalFee());
			bean.isOMOrder = String.valueOf(order.getmIsOneMenu());
			bean.orderId = String.valueOf(order.getmId());
			bean.orderCode =order.getmCode();
			bean.paymentType = order.getmPaymentType();
			bean.tipsType = order.getTipsType();
			bean.tipsFee = String.valueOf(order.getmTipsFee());
			
			bean.customer = new DCustomerBean();
			Trade trade = order.getmTrade();
			if(trade!=null){
				bean.customer.custAddr = trade.getmCustomerAddress();
				bean.customer.custName = trade.getmCustomerName();
			}
			
			bean.restaurant = new DRestaurantBean();
			Restaurant restaurant = order.getmRestaurant();
			if(restaurant!=null){
				bean.restaurant.restId = String.valueOf(restaurant.getmId());
				bean.restaurant.restName = restaurant.getmName();
				bean.restaurant.restLatitude = String.valueOf(restaurant.getmLatitude());
				bean.restaurant.restLongitude = String.valueOf(restaurant.getmLongitude());
			}
			forms.add(bean);
		}
		
		return forms;
	}

	@Override
	public DOrderBean getHistoryOrderDetailById(long orderId) {
		
		OrderForm orderOrg = getBaseDAO().findById(OrderForm.class, orderId);
		if(orderOrg==null)
			return null;
		DOrderBean bean = new DOrderBean();
		Trade trade = orderOrg.getmTrade();
		bean.customer = new DCustomerBean();
		
		if(trade!=null){
			bean.customer.custAddr = trade.getmCustomerAddress();
			bean.customer.custName = trade.getmCustomerName();
			bean.customer.custPhone= trade.getmCustomerPhone();
			
			/*
			Customer customer = trade.getmCustomer();
			if(customer!=null){
				bean.customer.custLatitude = customer.getmAccount().
			}*/
			

		}
		bean.restaurant = new DRestaurantBean();
		Restaurant restaurant = orderOrg.getmRestaurant();
		if(restaurant!=null){
			bean.restaurant.restId = String.valueOf(restaurant.getmId());
			bean.restaurant.restName = restaurant.getmName();
			bean.restaurant.restLatitude = String.valueOf(restaurant.getmLatitude());
			bean.restaurant.restLongitude = String.valueOf(restaurant.getmLongitude());
		}
		
		bean.orderCode = orderOrg.getmCode();
		
		bean.tipsFee = String.valueOf(orderOrg.getmTipsFee());
		bean.tipsType = orderOrg.getTipsType();
		bean.paymentType = orderOrg.getmPaymentType();
		
		bean.billPrice = String.valueOf(orderOrg.getmTotalFee());
		
		
		 Set<OrderItem>  orderItems = orderOrg.getmOrderItemSet();
		 
		 bean.dishArray = new ArrayList<DDishBean>(orderItems.size());
		 
		 for(OrderItem orderItem:orderItems){
			 DDishBean _bean = new DDishBean();
			 _bean.dishName = orderItem.getmDishName();
			 _bean.dishPrice = orderItem.getmDishPrice();
			 bean.dishArray.add(_bean);
		 }
		
		
		return bean;
	}

	@Override
	public List<DHistoryDayOrderBean> getHistoryOrdersGroupByDay(BaseDeliveryPageReqData data) {
		OrderFormQueryCondition condition = new OrderFormQueryCondition();
		condition.setDriverId(Long.valueOf(data.driverId));
		Date toDate = getToTime(Integer.valueOf(data.startNum));
		condition.setFromTimestamp(getFromTime(toDate,Integer.valueOf(data.range)));
		condition.setToTimestamp(toDate);
		condition.setStatus(ParameterConstant.ORDER_FORM_STATUS_FINISHED);
		
		List<OrderSequenceCondition> orderSequenceCondition = new ArrayList<OrderSequenceCondition>(1);
		orderSequenceCondition.add(new OrderSequenceCondition(ParameterConstant.ORDER_DESC,"mId"));
		condition.setOrderSequenceCondition(orderSequenceCondition );
		List<OrderForm> orderFormList = mOrderFormDAO.getOrderFormListByCondition(condition);
		if(orderFormList==null||orderFormList.isEmpty())
			return null;
		
		List<DHistoryDayOrderBean> forms = new ArrayList<DHistoryDayOrderBean>();
		
		String lastDay = null;
		DHistoryDayOrderBean historyBean = null;
		for(OrderForm order:orderFormList){
			
			if(order.getmCreateTimestamp()==null)
				continue;
			
			String DateString = TimestampUtil.dateToStr(order.getmCreateTimestamp(),DateFormatType.Date);
			
			if(!DateString.equals(lastDay)){
				lastDay = DateString;
				historyBean = new DHistoryDayOrderBean();
				
				forms.add(historyBean);
				
				historyBean.date = lastDay;
				historyBean.orders = new ArrayList<DOrderBean>();
			}
			
			DOrderBean bean = new DOrderBean();
			bean.billPrice = String.valueOf(order.getmTotalFee());
			bean.isOMOrder = String.valueOf(order.getmIsOneMenu());
			bean.orderId = String.valueOf(order.getmId());
			bean.orderCode =order.getmCode();
			bean.paymentType = order.getmPaymentType();
			bean.tipsType = order.getTipsType();
			bean.tipsFee = String.valueOf(order.getmTipsFee());
			bean.timestamp = TimestampUtil.dateToStr(order.getmCreateTimestamp(),DateFormatType.DateTimeToMin);
			
			bean.customer = new DCustomerBean();
			Trade trade = order.getmTrade();
			if(trade!=null){
				bean.customer.custAddr = trade.getmCustomerAddress();
				bean.customer.custName = trade.getmCustomerName();
			}
			
			bean.restaurant = new DRestaurantBean();
			Restaurant restaurant = order.getmRestaurant();
			if(restaurant!=null){
				bean.restaurant.restId = String.valueOf(restaurant.getmId());
				bean.restaurant.restName = restaurant.getmName();
				bean.restaurant.restLatitude = String.valueOf(restaurant.getmLatitude());
				bean.restaurant.restLongitude = String.valueOf(restaurant.getmLongitude());
			}
			historyBean.orders.add(bean);
		}
		
		return forms;
	}
	
	private Date getToTime(int start){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, start*(-1)+1);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	private Date getFromTime(Date toDate,int range){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(toDate);
		calendar.add(Calendar.DATE, range*(-1));
		return calendar.getTime();
	}

	@Override
	public List<Long> confirmOrdersByDriver(List<Long> orderFromIds, long driverId) {
		
		if(orderFromIds==null||orderFromIds.isEmpty())
			return null;
		
		List<Long>  unconfirmIds = new ArrayList<Long>();
		for(Long orderId:orderFromIds){
			//确认失败
			if(!confirmOrderByDriver(orderId, driverId))
				unconfirmIds.add(orderId);
		}
		return unconfirmIds;
	}

	@Override
	public List<Long> cancelOrdersByDriver(List<Long> orderFromIds, long driverId) {
		
		if(orderFromIds==null||orderFromIds.isEmpty())
			return null;
		
		List<Long>  uncancelIds = new ArrayList<Long>();
		
		int size = orderFromIds.size();
		
		for(int i=0;i<size-1;i++){
			if(!cancelOrderByDriver(orderFromIds.get(i), driverId,false))
				uncancelIds.add(orderFromIds.get(i));
		}
		
		if(!cancelOrderByDriver(orderFromIds.get(size-1), driverId,true))
			uncancelIds.add(orderFromIds.get(size-1));
		

		return uncancelIds;
	}

	@Override
	public DDriverStatisticsBean getStatisticsDataByDriverId(long driverId) {
		return mOrderFormDAO.getStatisticsDataByDriverId(driverId);
	}

	@Override
	@Transactional
	public boolean restCancelOrderByOrderId(Long orderFromId) {
		
		boolean result = false;

		try {
			
			OrderForm orderForm = findById(OrderForm.class, orderFromId);
			String status = orderForm.getmStatus();
			if(ParameterConstant.ORDER_FORM_STATUS_CALL_DELIVERY.equals(status)||ParameterConstant.ORDER_FORM_STATUS_DRIVER_PENDING.equals(status)||ParameterConstant.ORDER_FORM_STATUS_DELIVERY_CONFIRMED.equals(status)){
				orderForm.setmStatus(ParameterConstant.ORDER_FORM_STATUS_RESTAURANT_CANCEL);
				orderForm.setmCancelTimestamp(TimestampUtil.getCurrentTimestamp());
				update(orderForm);
				result = true;
			}else if(ParameterConstant.ORDER_FORM_STATUS_PICKED_UP.equals(status)||ParameterConstant.ORDER_FORM_STATUS_FINISHED.equals(status)){
				orderForm.setmStatus(ParameterConstant.ORDER_FORM_STATUS_RESTAURANT_CANCEL_AFTER_PICK_UP);
				orderForm.setmCancelTimestamp(TimestampUtil.getCurrentTimestamp());
				update(orderForm);
				result = true;
			}
			Driver driver = orderForm.getmDriver();
			
			if(driver!=null)
				mDriverService.pushToEndpoint(driver, "restaurant cancel order!!", 1);
			
			
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return result;
	}
	
	
	
}
