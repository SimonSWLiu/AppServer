package com.onemenu.server.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.onemenu.server.cache.LoginTokenCache;
import com.onemenu.server.constant.ParameterConstant;
import com.onemenu.server.entity.LoginTokenObj;
import com.onemenu.server.javabean.OrderFormQueryCondition;
import com.onemenu.server.javabean.OrderSequenceCondition;
import com.onemenu.server.javabean.delivery.DDriverBean;
import com.onemenu.server.javabean.delivery.DDriverOrderBean;
import com.onemenu.server.javabean.delivery.DDriverStatisticsBean;
import com.onemenu.server.javabean.delivery.DHistoryDayOrderBean;
import com.onemenu.server.javabean.delivery.DLoginBean;
import com.onemenu.server.javabean.delivery.DOrderBean;
import com.onemenu.server.javabean.delivery.DRestaurantOrderBean;
import com.onemenu.server.model.Account;
import com.onemenu.server.model.Driver;
import com.onemenu.server.protocol.ErrorCode;
import com.onemenu.server.protocol.req.BaseRequestData;
import com.onemenu.server.protocol.req.delivery.BaseDeliveryPageReqData;
import com.onemenu.server.protocol.req.delivery.BaseDeliveryReqData;
import com.onemenu.server.protocol.req.delivery.DriverView;
import com.onemenu.server.protocol.response.BaseData;
import com.onemenu.server.protocol.response.ListBaseData;
import com.onemenu.server.protocol.response.ObjectBaseData;
import com.onemenu.server.protocol.response.OnlyObjectBaseData;
import com.onemenu.server.service.AccountService;
import com.onemenu.server.service.DriverService;
import com.onemenu.server.service.OrderFormService;
import com.onemenu.server.util.StringUtil;
import com.onemenu.server.util.TimestampUtil;

/**
 *
 * Title: DeliveryAppController.java
 *
 * @author simonliu
 *
 * @data Jun 9, 2015
 * @time 10:38:54 AM
 * 
 *       Description:
 *
 */
@Controller
@RequestMapping("/delivery")
public class DeliveryAppController {
	
	private static final Logger logger = Logger.getLogger(DeliveryAppController.class);

	@Resource(name = "driverService")
	private DriverService		mDriverService;
	private OrderFormService	mOrderFormService;

	@Autowired
	public void setmOrderFormService(OrderFormService mOrderFormService) {
		this.mOrderFormService = mOrderFormService;
	}

	@RequestMapping(value = "/getDeliveryOrderFormListByDriverId", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public Object getDeliveryOrderFormListByDriverId(long driverId) throws Exception {

		ListBaseData<DRestaurantOrderBean> _data = new ListBaseData<DRestaurantOrderBean>();

		OrderFormQueryCondition condition = new OrderFormQueryCondition();
		condition.setDriverId(driverId);

		List<DRestaurantOrderBean> orderFormBeanList = mOrderFormService.getDeliveryOrderFormListByDriverId(condition);

		if (orderFormBeanList == null || orderFormBeanList.isEmpty()) {
			_data.status = ErrorCode.NoData.getCode();
			_data.msg = ErrorCode.NoData.getMsg();
			return _data.toJson();
		}

		_data.status = ErrorCode.RequestSuccess.getCode();
		_data.msg = ErrorCode.RequestSuccess.getMsg();
		_data.data.put("deliveringListArray", orderFormBeanList);
		return _data.toJson();
	}

	@RequestMapping(value = "/getCallDeliveryOrderFormList", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public Object getCallDeliveryOrderFormList(long driverId) throws Exception {

		ListBaseData<DRestaurantOrderBean> _data = new ListBaseData<DRestaurantOrderBean>();

		OrderFormQueryCondition condition = new OrderFormQueryCondition();
		condition.setDriverId(driverId);
		condition.setStatus(ParameterConstant.ORDER_FORM_STATUS_CALL_DELIVERY);
		List<OrderSequenceCondition> orderSequenceConditionList = new ArrayList<OrderSequenceCondition>();
		OrderSequenceCondition orderSequenceCondition = new OrderSequenceCondition();
		orderSequenceCondition.setOrder(ParameterConstant.ORDER_ASC);
		orderSequenceCondition.setField("mCallDeliveryTimestamp");
		orderSequenceConditionList.add(orderSequenceCondition);

		List<DRestaurantOrderBean> orderFormBeanList = mOrderFormService.getCallDeliveryOrderFormList(condition);

		if (orderFormBeanList == null || orderFormBeanList.isEmpty()) {
			_data.status = ErrorCode.NoData.getCode();
			_data.msg = ErrorCode.NoData.getMsg();
			return _data.toJson();
		}

		_data.status = ErrorCode.RequestSuccess.getCode();
		_data.msg = ErrorCode.RequestSuccess.getMsg();
		_data.data.put("deliveringListArray", orderFormBeanList);
		return _data.toJson();
	}

	@RequestMapping(value = "/getDetailOrder", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public Object getDetailOrder(Long orderFormId) {

		ObjectBaseData<DOrderBean> _data = new ObjectBaseData<DOrderBean>();
		if (orderFormId == null) {
			_data.status = ErrorCode.ParamError.getCode();
			_data.msg = ErrorCode.ParamError.getMsgForName("orderFormId");
			return _data.toJson();
		}
		DOrderBean order = mOrderFormService.getOrderFormById(orderFormId);
		if (order == null) {
			_data.status = ErrorCode.DataNoExist.getCode();
			_data.msg = ErrorCode.DataNoExist.getMsg();
			return _data.toJson();
		}

		_data.status = ErrorCode.RequestSuccess.getCode();
		_data.msg = ErrorCode.RequestSuccess.getMsg();
		_data.data.put("orderDetail", order);

		return _data.toJson();
	}

	@RequestMapping(value = "/getDriverInfoByDriverId", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public Object getDriverInfoByDriverId(long driverId) throws Exception {

		Driver driver = mDriverService.findById(Driver.class, driverId);

		return driver;
	}

	@RequestMapping(value = "/assignOrderFormToDriver", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public Object assignOrderFormToDriver(long orderFormId, long driverId) throws Exception {

		boolean result = mOrderFormService.assignOrderFormToDriver(orderFormId, driverId);
		BaseData _data = new BaseData();

		if (result) {
			_data.status = ErrorCode.RequestSuccess.getCode();
			_data.msg = ErrorCode.RequestSuccess.getMsg();
		}

		return _data.toJson();
	}

	@RequestMapping(value = "/confirmOrderFormByDriver", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public Object confirmOrderFormByDriver(long orderFormId, long driverId) throws Exception {

		boolean result = mOrderFormService.confirmOrderByDriver(orderFormId, driverId);
		BaseData _data = new BaseData();

		if (result) {
			_data.status = ErrorCode.RequestSuccess.getCode();
			_data.msg = ErrorCode.RequestSuccess.getMsg();
		}

		return _data.toJson();
	}

	@RequestMapping(value = "/cancelOrderByDriver", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public Object cancelOrderByDriver(long orderFormId, long driverId) throws Exception {

		boolean result = mOrderFormService.cancelOrderByDriver(orderFormId, driverId,true);
		BaseData _data = new BaseData();
		if (result) {
			_data.status = ErrorCode.RequestSuccess.getCode();
			_data.msg = ErrorCode.RequestSuccess.getMsg();
		}

		return _data.toJson();
	}
	
	
	
	@RequestMapping(value = "/confirmOrderFormsByDriver", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public Object confirmOrderFormsByDriver(String orderFormId, long driverId) throws Exception {

		BaseData _data = new BaseData();
		if(StringUtil.isEmpty(orderFormId)){
			_data.status = ErrorCode.ParamError.getCode();
			_data.msg = ErrorCode.ParamError.getMsgForName("orderFormId");
			return _data.toJson();
		}
		
		String[] orderIds = orderFormId.split(",");
		List<Long> orderIdsLong = new ArrayList<Long>();
		for(String orderId : orderIds){
			try{
				orderIdsLong.add(Long.valueOf(orderId));
			}catch(Exception e){
				logger.error("", e);
			}
		}
		if(orderIdsLong.isEmpty()){
			_data.status = ErrorCode.ParamError.getCode();
			_data.msg = ErrorCode.ParamError.getMsgForName("orderFormId");
			return _data.toJson();
		}
		
		
		
		List<Long> result = mOrderFormService.confirmOrdersByDriver(orderIdsLong, driverId);

		if (result==null||result.isEmpty()) {
			_data.status = ErrorCode.RequestSuccess.getCode();
			_data.msg = ErrorCode.RequestSuccess.getMsg();
		}else{
			_data.msg = result.toString() + " confirm failed ";
		}

		return _data.toJson();
	}

	@RequestMapping(value = "/cancelOrdersByDriver", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public Object cancelOrdersByDriver(String orderFormId, long driverId) throws Exception {

		BaseData _data = new BaseData();
		if(StringUtil.isEmpty(orderFormId)){
			_data.status = ErrorCode.ParamError.getCode();
			_data.msg = ErrorCode.ParamError.getMsgForName("orderFormId");
			return _data.toJson();
		}
		
		String[] orderIds = orderFormId.split(",");
		List<Long> orderIdsLong = new ArrayList<Long>();
		for(String orderId : orderIds){
			try{
				orderIdsLong.add(Long.valueOf(orderId));
			}catch(Exception e){
				logger.error("", e);
			}
		}
		if(orderIdsLong.isEmpty()){
			_data.status = ErrorCode.ParamError.getCode();
			_data.msg = ErrorCode.ParamError.getMsgForName("orderFormId");
			return _data.toJson();
		}
		
		
		
		List<Long> result = mOrderFormService.cancelOrdersByDriver(orderIdsLong, driverId);

		if (result==null||result.isEmpty()) {
			_data.status = ErrorCode.RequestSuccess.getCode();
			_data.msg = ErrorCode.RequestSuccess.getMsg();
		}else{
			_data.msg = result.toString() + " cancel failed ";
		}

		return _data.toJson();
	}
	
	
	
	
	@RequestMapping(value = "/pickedUpOrderByDriver", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public Object pickedUpOrderByDriver(long orderFormId, long driverId) throws Exception {

		boolean result = mOrderFormService.pickedUpOrderByDriver(orderFormId, driverId);
		BaseData _data = new BaseData();
		if (result) {
			_data.status = ErrorCode.RequestSuccess.getCode();
			_data.msg = ErrorCode.RequestSuccess.getMsg();

		}

		return _data.toJson();
	}

	
	@RequestMapping(value = "/finishOrderByDriver", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public Object finishOrderByDriver(long orderFormId, long driverId, String tipsType, String tipsFee) throws Exception {

		boolean result = mOrderFormService.finishOrderByDriver(orderFormId, driverId, tipsFee, tipsType);
		BaseData _data = new BaseData();
		if (result) {
			_data.status = ErrorCode.RequestSuccess.getCode();
			_data.msg = ErrorCode.RequestSuccess.getMsg();

		}

		return _data.toJson();
	}
	

	@RequestMapping(value = "/billPriceByDriver", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public Object modifyBillPriceByDriver(long orderFormId, String billPrice) throws Exception {

		boolean result = mOrderFormService.modifyBillPriceByDriver(orderFormId, billPrice);
		BaseData _data = new BaseData();
		if (result) {
			_data.status = ErrorCode.RequestSuccess.getCode();
			_data.msg = ErrorCode.RequestSuccess.getMsg();

		}

		return _data.toJson();
	}

	@RequestMapping(value = "/allOrders", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public Object getAllOrders() throws Exception {

		List<DDriverOrderBean> orders = mOrderFormService.getAllOrders();

		ListBaseData<DDriverOrderBean> _data = new ListBaseData<DDriverOrderBean>();

		if (orders == null || orders.isEmpty()) {
			_data.status = ErrorCode.NoData.getCode();
			_data.msg = ErrorCode.NoData.getMsg();
		} else {
			_data.status = ErrorCode.RequestSuccess.getCode();
			_data.msg = ErrorCode.RequestSuccess.getMsg();
			_data.data.put("ordersArray", orders);
		}

		return _data.toJson();
	}

	@RequestMapping(value = "/onduty", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public Object onduty(long driverId, String isOnDuty) throws Exception {

		boolean result = mDriverService.updateOnDuty(driverId, isOnDuty);

		BaseData _data = new BaseData();

		if (result) {
			_data.status = ErrorCode.RequestSuccess.getCode();
			_data.msg = ErrorCode.RequestSuccess.getMsg();
		}

		return _data.toJson();
	}

	@RequestMapping(value = "/allDrivers", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public Object getAllDrivers() throws Exception {

		List<DDriverBean> drivers = mDriverService.getAllDrivers();

		ListBaseData<DDriverBean> _data = new ListBaseData<DDriverBean>();

		if (drivers == null || drivers.isEmpty()) {
			_data.status = ErrorCode.NoData.getCode();
			_data.msg = ErrorCode.NoData.getMsg();
		} else {
			_data.status = ErrorCode.RequestSuccess.getCode();
			_data.msg = ErrorCode.RequestSuccess.getMsg();
			_data.data.put("driverArray", drivers);
		}

		return _data.toJson();
	}

	@RequestMapping(value = "/location", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public Object updateLocation(BaseDeliveryReqData data) throws Exception {

		BaseData _data = new BaseData();

		if (!StringUtil.isNumber(data.driverId)) {
			_data.status = ErrorCode.ParamError.getCode();
			_data.msg = ErrorCode.ParamError.getMsgForName("driverId");
			return _data.toJson();
		} else if (!StringUtil.isNumber(data.latitude)) {
			_data.status = ErrorCode.ParamError.getCode();
			_data.msg = ErrorCode.ParamError.getMsgForName("latitude");
			return _data.toJson();
		} else if (!StringUtil.isNumber(data.longitude)) {
			_data.status = ErrorCode.ParamError.getCode();
			_data.msg = ErrorCode.ParamError.getMsgForName("longitude");
			return _data.toJson();
		}

		mDriverService.updataLocation(Long.parseLong(data.driverId), Double.valueOf(data.longitude), Double.valueOf(data.latitude));
		_data.status = ErrorCode.RequestSuccess.getCode();
		_data.msg = ErrorCode.RequestSuccess.getMsg();
		return _data.toJson();
	}
	
	
	@RequestMapping(value = "/uploadRecieptImage", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public Object uploadRecieptImage(String orderFormId, String imageBase64Str,String imageWidth, String imageHeight) throws Exception {

		BaseData _data = new BaseData();

		if (!StringUtil.isNumber(orderFormId)) {
			_data.status = ErrorCode.ParamError.getCode();
			_data.msg = ErrorCode.ParamError.getMsgForName("orderFormId");
			return _data.toJson();
		} else if (StringUtil.isEmpty(imageBase64Str)) {
			_data.status = ErrorCode.ParamError.getCode();
			_data.msg = ErrorCode.ParamError.getMsgForName("imageBase64Str");
			return _data.toJson();
		} else if (!StringUtil.isNumber(imageWidth)) {
			_data.status = ErrorCode.ParamError.getCode();
			_data.msg = ErrorCode.ParamError.getMsgForName("imageWidth");
			return _data.toJson();
		} else if (!StringUtil.isNumber(imageHeight)) {
			_data.status = ErrorCode.ParamError.getCode();
			_data.msg = ErrorCode.ParamError.getMsgForName("imageHeight");
			return _data.toJson();
		}
		
		try{
			mOrderFormService.uploadRecieptImage(Long.valueOf(orderFormId),imageBase64Str,imageWidth,imageHeight);
		}catch(Exception e){
			logger.error("", e);
			_data.msg = e.getLocalizedMessage();
			return _data.toJson();
		}
		
		_data.status = ErrorCode.RequestSuccess.getCode();
		_data.msg = ErrorCode.RequestSuccess.getMsg();
		return _data.toJson();
	}
	
	@RequestMapping(value = "/historyOrders", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public String  getHistoryOrdersByDriver(BaseDeliveryPageReqData data){
		
		ListBaseData<DOrderBean> _data = new ListBaseData<DOrderBean>();
		if (!StringUtil.isNumber(data.driverId)) {
			_data.status = ErrorCode.ParamError.getCode();
			_data.msg = ErrorCode.ParamError.getMsgForName("driverId");
			return _data.toJson();
		} else if (!StringUtil.isNumber(data.startNum)) {
			_data.status = ErrorCode.ParamError.getCode();
			_data.msg = ErrorCode.ParamError.getMsgForName("startNum");
			return _data.toJson();
		} else if (!StringUtil.isNumber(data.range)) {
			_data.status = ErrorCode.ParamError.getCode();
			_data.msg = ErrorCode.ParamError.getMsgForName("range");
			return _data.toJson();
		}
		
		List<DOrderBean> orderFormBeanList = mOrderFormService.getHistoryOrdersByDriver(data);

		if (orderFormBeanList == null || orderFormBeanList.isEmpty()) {
			_data.status = ErrorCode.NoData.getCode();
			_data.msg = ErrorCode.NoData.getMsg();
			return _data.toJson();
		}

		_data.status = ErrorCode.RequestSuccess.getCode();
		_data.msg = ErrorCode.RequestSuccess.getMsg();
		_data.data.put("historyList", orderFormBeanList);
		return _data.toJson();
	}
	
	@RequestMapping(value = "/historyOrderDetail", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public String  getHistoryOrderDetailById(String  orderFormId){
		
		ObjectBaseData<DOrderBean> _data = new ObjectBaseData<DOrderBean>();
		if (!StringUtil.isNumber(orderFormId)) {
			_data.status = ErrorCode.ParamError.getCode();
			_data.msg = ErrorCode.ParamError.getMsgForName("orderFormId");
			return _data.toJson();
		} 
		
		DOrderBean bean = mOrderFormService.getHistoryOrderDetailById(Long.valueOf(orderFormId));

		if (bean == null) {
			_data.status = ErrorCode.DataNoExist.getCode();
			_data.msg = ErrorCode.DataNoExist.getMsg();
			return _data.toJson();
		}

		_data.status = ErrorCode.RequestSuccess.getCode();
		_data.msg = ErrorCode.RequestSuccess.getMsg();
		_data.data.put("orderDetail", bean);
		return _data.toJson();
	}
	
	
	@RequestMapping(value = "/historyDayOrders", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public String  getHistoryOrdersGroupDay(BaseDeliveryPageReqData data){
		
		ListBaseData<DHistoryDayOrderBean> _data = new ListBaseData<DHistoryDayOrderBean>();
		if (!StringUtil.isNumber(data.driverId)) {
			_data.status = ErrorCode.ParamError.getCode();
			_data.msg = ErrorCode.ParamError.getMsgForName("driverId");
			return _data.toJson();
		} else if (!StringUtil.isNumber(data.startNum)) {
			_data.status = ErrorCode.ParamError.getCode();
			_data.msg = ErrorCode.ParamError.getMsgForName("startNum");
			return _data.toJson();
		} else if (!StringUtil.isNumber(data.range)) {
			_data.status = ErrorCode.ParamError.getCode();
			_data.msg = ErrorCode.ParamError.getMsgForName("range");
			return _data.toJson();
		}
		
		List<DHistoryDayOrderBean> orderFormBeanList = mOrderFormService.getHistoryOrdersGroupByDay(data);

		if (orderFormBeanList == null || orderFormBeanList.isEmpty()) {
			_data.status = ErrorCode.NoData.getCode();
			_data.msg = ErrorCode.NoData.getMsg();
			return _data.toJson();
		}

		_data.status = ErrorCode.RequestSuccess.getCode();
		_data.msg = ErrorCode.RequestSuccess.getMsg();
		_data.data.put("historyList", orderFormBeanList);
		return _data.toJson();
	}
	
	@RequestMapping(value = "/statisticsData", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public String statisticsData(BaseDeliveryReqData data){
		
		ObjectBaseData<DDriverStatisticsBean> _data = new ObjectBaseData<DDriverStatisticsBean>();
		if(!StringUtil.isNumber(data.driverId)){
			_data.status = ErrorCode.ParamError.getCode();
			_data.status = ErrorCode.ParamError.getMsgForName("driverId");
			return _data.toJson();
		}
		
		DDriverStatisticsBean bean = mOrderFormService.getStatisticsDataByDriverId(Long.valueOf(data.driverId));
		
		_data.status = ErrorCode.RequestSuccess.getCode();
		_data.msg = ErrorCode.RequestSuccess.getMsg();
		_data.data.put("driverStatistics", bean);
		
		return _data.toJson();
	}

	@RequestMapping(value = "/appSignUp", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public String appSignUp(DriverView view) {

		OnlyObjectBaseData<DLoginBean> _data = new OnlyObjectBaseData<DLoginBean>();

		if (TextUtils.isEmpty(view.email)) {
			_data.status = ErrorCode.ParamError.getCode();
			_data.msg = ErrorCode.ParamError.getMsgForName("email");
			return _data.toJson();
		} else if (TextUtils.isEmpty(view.password)) {
			_data.status = ErrorCode.ParamError.getCode();
			_data.msg = ErrorCode.ParamError.getMsgForName("password");
			return _data.toJson();
		}

		if (mAccountService.validateEmail(view.email)) {

			// 当前时间
			Timestamp ts = TimestampUtil.getCurrentTimestamp();

			Account account = new Account();
			account.setmEmail(view.email);
			account.setmPassword(view.password);
			account.setmCreateTimestamp(ts);
			// Create driver
			Driver driver = new Driver();

			driver.setmAddress(view.address);
			driver.setmEmail(view.email);
			driver.setmName(view.name);
			driver.setmPhone(view.phone);
			driver.setmCreateTimestamp(ts);
			driver.setDeviceToken(view.notificationDeviceToken);
			driver.setmStatus(ParameterConstant.DRIVER_STATUS_PENDING);

			account.setmDriver(driver);

			mAccountService.saveTrd(account);

			_data.status = ErrorCode.RequestSuccess.getCode();
			_data.msg = ErrorCode.RequestSuccess.getMsg();
		} else {
			_data.msg = " email is exist";
		}

		return _data.toJson();

	}

	@RequestMapping(value = "/inSignup", method = RequestMethod.GET)
	public String enterSignup() {
		return "driverSignup";
	}

	@RequestMapping(value = "/signUp", method = RequestMethod.POST)
	public ModelAndView signUp(DriverView view) {

		ModelAndView _data = new ModelAndView("driverSignup");

		if (TextUtils.isEmpty(view.email)) {
			_data.addObject("message", "email is null ");
			return _data;
		} else if (TextUtils.isEmpty(view.password)) {
			_data.addObject("message", "password  is null ");
			return _data;
		}

		if (mAccountService.validateEmail(view.email)) {

			// 当前时间
			Timestamp ts = TimestampUtil.getCurrentTimestamp();

			Account account = new Account();
			account.setmEmail(view.email);
			account.setmPassword(view.password);
			account.setmCreateTimestamp(ts);
			// Create driver
			Driver driver = new Driver();

			driver.setmAddress(view.address);
			driver.setmEmail(view.email);
			driver.setmName(view.name);
			driver.setmPhone(view.phone);
			driver.setmCreateTimestamp(ts);
			driver.setmStatus(ParameterConstant.DRIVER_STATUS_PENDING);
			driver.setLatitude(0.0);
			driver.setLongitude(0.0);
			
			account.setmDriver(driver);

			mAccountService.saveTrd(account);

			// EmailUtils.sendActiveEmail(account);

			_data.addObject("message", "sign up success");
		}

		return _data;

	}

	// 登陆
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public String login(DriverView view) {

		OnlyObjectBaseData<DLoginBean> _data = new OnlyObjectBaseData<DLoginBean>();

		if (TextUtils.isEmpty(view.account)) {
			_data.status = ErrorCode.ParamError.getCode();
			_data.msg = ErrorCode.ParamError.getMsgForName("account");
			return _data.toJson();
		} else if (TextUtils.isEmpty(view.password)) {
			_data.status = ErrorCode.ParamError.getCode();
			_data.msg = ErrorCode.ParamError.getMsgForName("password");
			return _data.toJson();
		}

		String loginToken = "";

		Integer device;
		if (StringUtil.isNumber(view.device)) {
			device = Integer.valueOf(view.device);
		} else
			device = ParameterConstant.DEVICE_IOS;

		Account account = mAccountService.loginUpdateToken(view.account, view.password, device, view.notificationDeviceToken, ParameterConstant.LOGIN_DRIVER);

		if (account != null) {

			Driver driver = account.getmDriver();
			
			if(!StringUtil.isEmpty(view.notificationDeviceToken)){
				driver.setDeviceToken(view.notificationDeviceToken);
				mAccountService.updateTrd(driver);
				
			}
				
			
			DLoginBean bean = new DLoginBean();

			loginToken = LoginTokenCache.generateLoginToken(account.getmId());
			LoginTokenObj loginTokenObj = new LoginTokenObj();
			loginTokenObj.account = account;
			loginTokenObj.loginTimestamp = TimestampUtil.getCurrentTimestamp();
			LoginTokenCache.loginTokenMap.put(loginToken, loginTokenObj);
			bean.loginToken = loginToken;
			bean.driverId = String.valueOf(driver.getmId());
			if(driver.getmStatus()== ParameterConstant.DRIVER_STATUS_RESIGNED||driver.getmStatus()==ParameterConstant.DRIVER_STATUS_UNAVAILIABLE){
				bean.isOnDuty = ParameterConstant.DRIVER_ONDUTY_NO;
			}else{
				bean.isOnDuty = ParameterConstant.DRIVER_ONDUTY_YES;
			}
			
			
			_data.data = bean;
			_data.status = ParameterConstant.STATUS_CODE_OPERATION_SUCCESS;
			_data.msg = ParameterConstant.MSG_OPERATION_SUCCESS;

			return _data.toJson();

		}

		_data.msg = "Account or password error.";

		return _data.toJson();

	}

	@RequestMapping(value = "/autoLogin", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public Object autoLogin(String loginToken, String notificationDeviceToken, Integer device) {

		OnlyObjectBaseData<DLoginBean> _data = new OnlyObjectBaseData<DLoginBean>();

		if (TextUtils.isEmpty(loginToken)) {
			_data.status = ErrorCode.ParamError.getCode();
			_data.msg = ErrorCode.ParamError.getMsgForName("loginToken");
			return _data.toJson();
		}

		if (LoginTokenCache.loginTokenMap.containsKey(loginToken)) {

			LoginTokenObj loginTokenObj = (LoginTokenObj) LoginTokenCache.loginTokenMap.get(loginToken);
			LoginTokenCache.loginTokenMap.remove(loginToken);

			Timestamp loginTimestamp = loginTokenObj.loginTimestamp;
			Timestamp currentTimestamp = TimestampUtil.getCurrentTimestamp();

			// check token valid time
			int days = 7;
			if ((currentTimestamp.getTime() - loginTimestamp.getTime()) < days * 24 * 60 * 60 * 1000) {
				device = device == null ? ParameterConstant.DEVICE_IOS : device;
				Account account = mAccountService.loginUpdateToken(loginTokenObj.account.getmEmail(), loginTokenObj.account.getmPassword(), device,
						notificationDeviceToken, ParameterConstant.LOGIN_DRIVER);
				if (account != null) {
					Driver driver = account.getmDriver();
					
					if(!StringUtil.isEmpty(notificationDeviceToken)){
						driver.setDeviceToken(notificationDeviceToken);
						mAccountService.updateTrd(driver);
						
					}
					DLoginBean bean = new DLoginBean();
					String newLoginToken = LoginTokenCache.generateLoginToken(account.getmId());
					loginTokenObj.loginTimestamp = currentTimestamp;
					LoginTokenCache.loginTokenMap.put(newLoginToken, loginTokenObj);
					_data.status = ParameterConstant.STATUS_CODE_OPERATION_SUCCESS;
					_data.msg = ParameterConstant.MSG_OPERATION_SUCCESS;
					bean.loginToken = newLoginToken;
					bean.driverId = String.valueOf(driver.getmId());
					if(driver.getmStatus()== ParameterConstant.DRIVER_STATUS_RESIGNED||driver.getmStatus()==ParameterConstant.DRIVER_STATUS_UNAVAILIABLE){
						bean.isOnDuty = ParameterConstant.DRIVER_ONDUTY_NO;
					}else{
						bean.isOnDuty = ParameterConstant.DRIVER_ONDUTY_YES;
					}
					_data.data = bean;
					return _data.toJson();
				}

			}

		}

		_data.status = ParameterConstant.STATUS_CODE_DATA_NOT_EXIST;
		_data.msg = ParameterConstant.MSG_DATA_NOT_EXIST;

		return _data.toJson();
	}

	// 登出
	@RequestMapping(value = "/logout", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public String logout(BaseRequestData view,String loginToken) {

		BaseData _data = new BaseData();

		if (TextUtils.isEmpty(loginToken)) {
			_data.status = ErrorCode.ParamError.getCode();
			_data.msg = ErrorCode.ParamError.getMsgForName("loginToken");
			return _data.toJson();
		} else if (!StringUtil.isNumber(view.device)) {
			_data.status = ErrorCode.ParamError.getCode();
			_data.msg = ErrorCode.ParamError.getMsgForName("device");
			return _data.toJson();
		} else if (StringUtil.isEmpty(view.notificationDeviceToken)) {
			_data.status = ErrorCode.ParamError.getCode();
			_data.msg = ErrorCode.ParamError.getMsgForName("notificationDeviceToken");
			return _data.toJson();
		}

		LoginTokenObj loginTokenObj = (LoginTokenObj) LoginTokenCache.loginTokenMap.get(loginToken);
		if(loginTokenObj==null){
			_data.msg = "loginToken is invalidate";
			return _data.toJson();
		}
	

		Account account =  loginTokenObj.account;
		
		mAccountService.logoutUpdateToken(account.getmId(),Integer.valueOf(view.device) , view.notificationDeviceToken, ParameterConstant.LOGIN_DRIVER);

		if (account != null) {
			_data.status = ParameterConstant.STATUS_CODE_OPERATION_SUCCESS;
			_data.msg = ParameterConstant.MSG_OPERATION_SUCCESS;

		}

		return _data.toJson();

	}

	private AccountService	mAccountService;

	@Autowired
	public void setAccountService(AccountService service) {
		this.mAccountService = service;
	}

}
