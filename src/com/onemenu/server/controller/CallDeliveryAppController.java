package com.onemenu.server.controller;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.onemenu.server.cache.LoginTokenCache;
import com.onemenu.server.constant.ParameterConstant;
import com.onemenu.server.entity.LoginTokenObj;
import com.onemenu.server.javabean.calldelivery.CDOrderBean;
import com.onemenu.server.javabean.calldelivery.CDRestaurantOrdersBean;
import com.onemenu.server.javabean.delivery.DLocationBean;
import com.onemenu.server.model.Account;
import com.onemenu.server.model.OrderForm;
import com.onemenu.server.model.Restaurant;
import com.onemenu.server.protocol.ErrorCode;
import com.onemenu.server.protocol.req.BaseRequestData;
import com.onemenu.server.protocol.req.calldelivery.BaseCallDeliveryPageReqData;
import com.onemenu.server.protocol.req.calldelivery.BaseCallDeliveryReqData;
import com.onemenu.server.protocol.req.calldelivery.ResetTipsFeeData;
import com.onemenu.server.protocol.response.BaseData;
import com.onemenu.server.protocol.response.ListBaseData;
import com.onemenu.server.protocol.response.ObjectBaseData;
import com.onemenu.server.service.AccountService;
import com.onemenu.server.service.OrderFormService;
import com.onemenu.server.util.GenerateIdUtil;
import com.onemenu.server.util.ResponseUtils;
import com.onemenu.server.util.StringUtil;
import com.onemenu.server.util.TimestampUtil;

@Controller
@RequestMapping("/calldelivery")
public class CallDeliveryAppController {

	private static final Logger logger = Logger.getLogger(CallDeliveryAppController.class);
	private static final String SESSION_KEY = "sessionKey";
	private AccountService	mAccountService;

	@Autowired
	public void setAccountService(AccountService service) {
		this.mAccountService = service;
	}
	
	
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public Object login(HttpSession session, Account account,Integer device,String notificationDeviceToken) throws Exception {

		String statusCode = ParameterConstant.STATUS_CODE_ERROR;
		String msg = ParameterConstant.MSG_OPERATION_ERROR;
		Map<String, Object> data = new HashMap<String, Object>();
		
		if(device==null){
			statusCode = ErrorCode.ParamError.getCode();
			msg = ErrorCode.ParamError.getMsgForName("device");
			return JSON.toJSON(ResponseUtils.setStatusJSON(statusCode, msg, data));
		}/*
		else if(StringUtil.isEmpty(notificationDeviceToken)){
			statusCode = ErrorCode.ParamError.getCode();
			msg = ErrorCode.ParamError.getMsgForName("notificationDeviceToken");
			return JSON.toJSON(ResponseUtils.setStatusJSON(statusCode, msg, data));
		}*/
		else if(StringUtil.isEmpty(account.getmEmail())){
			statusCode = ErrorCode.ParamError.getCode();
			msg = ErrorCode.ParamError.getMsgForName("mEmail");
			return JSON.toJSON(ResponseUtils.setStatusJSON(statusCode, msg, data));
		}else if(StringUtil.isEmpty(account.getmPassword())){
			statusCode = ErrorCode.ParamError.getCode();
			msg = ErrorCode.ParamError.getMsgForName("mPassword");
			return JSON.toJSON(ResponseUtils.setStatusJSON(statusCode, msg, data));
		}
		
		
		device = device == null ? ParameterConstant.DEVICE_IOS : device;
		
		Account login = mAccountService.loginUpdateToken(account.getmEmail(), account.getmPassword(), device, notificationDeviceToken, ParameterConstant.LOGIN_MERCHANT);

		if (login != null) {

			Restaurant restaurant = login.getmMerchant().getmRestaurant();

			if (restaurant != null) {
				if (restaurant.getmStatus().equals(ParameterConstant.RESTAURANT_STATUS_PENDING)) {
					msg = "Your restaurant is pending approval...";
				} else {
					
					statusCode = ParameterConstant.STATUS_CODE_OPERATION_SUCCESS;
					msg = ParameterConstant.MSG_OPERATION_SUCCESS;
					session.setAttribute(SESSION_KEY, login);
					String loginToken = LoginTokenCache.generateLoginToken(account.getmId());
					LoginTokenObj loginTokenObj = new LoginTokenObj();
					loginTokenObj.account = login;
					loginTokenObj.loginTimestamp = TimestampUtil.getCurrentTimestamp(); 
					LoginTokenCache.loginTokenMap.put(loginToken, loginTokenObj);
					
					data.put("loginToken", loginToken);
					//其他信息
					data.put("restId", String.valueOf(restaurant.getmId()));
					data.put("longitude", String.valueOf(restaurant.getmLongitude()));
					data.put("latitude", String.valueOf(restaurant.getmLatitude()));
					data.put("distance", String.valueOf(restaurant.getmDeliveryDistance()));
				}
			} else {
				msg = "Your have not restaurant";
			}

		} else {
			msg =  "Account or passwrod error." ;
		}
		
		return JSON.toJSON(ResponseUtils.setStatusJSON(statusCode, msg, data));
	}

	@RequestMapping(value = "/autoLogin", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public Object autoLogin(HttpSession session, String loginToken, String notificationDeviceToken, Integer device) {
		
		String statusCode = ParameterConstant.STATUS_CODE_ERROR;
		String msg = ParameterConstant.MSG_OPERATION_ERROR;
		Map<String, Object> data = new HashMap<String, Object>();
		
		if(device==null){
			statusCode = ErrorCode.ParamError.getCode();
			msg = ErrorCode.ParamError.getMsgForName("device");
			return JSON.toJSON(ResponseUtils.setStatusJSON(statusCode, msg, data));
		}/*
		else if(StringUtil.isEmpty(notificationDeviceToken)){
			statusCode = ErrorCode.ParamError.getCode();
			msg = ErrorCode.ParamError.getMsgForName("notificationDeviceToken");
			return JSON.toJSON(ResponseUtils.setStatusJSON(statusCode, msg, data));
		}*/

		if (LoginTokenCache.loginTokenMap.containsKey(loginToken)) {

			LoginTokenObj loginTokenObj = (LoginTokenObj) LoginTokenCache.loginTokenMap.get(loginToken);
			LoginTokenCache.loginTokenMap.remove(loginToken);

			Timestamp loginTimestamp = loginTokenObj.loginTimestamp;
			Timestamp currentTimestamp = TimestampUtil.getCurrentTimestamp();
			// check token valid time
			int days = 7;
			if ((currentTimestamp.getTime() - loginTimestamp.getTime()) < days * 24 * 60 * 60 * 1000) {

				device = device == null ? ParameterConstant.DEVICE_IOS : device;
				Account account = mAccountService.loginUpdateToken(loginTokenObj.account.getmEmail(), loginTokenObj.account.getmPassword(), device, notificationDeviceToken, ParameterConstant.LOGIN_MERCHANT);
				if(account==null){
					statusCode = ParameterConstant.STATUS_CODE_DATA_NOT_EXIST;
					msg = ParameterConstant.MSG_DATA_NOT_EXIST;
				}else{
					session.setAttribute(SESSION_KEY, account);
					String newLoginToken = LoginTokenCache.generateLoginToken(account.getmId());
					LoginTokenCache.loginTokenMap.put(newLoginToken, loginTokenObj);

					statusCode = ParameterConstant.STATUS_CODE_OPERATION_SUCCESS;
					msg = ParameterConstant.MSG_OPERATION_SUCCESS;
					
					data.put("loginToken", newLoginToken);
					data.put("restId",String.valueOf(account.getmMerchant().getmRestaurant().getmId()));
					data.put("longitude", String.valueOf(account.getmMerchant().getmRestaurant().getmLongitude()));
					data.put("latitude", String.valueOf(account.getmMerchant().getmRestaurant().getmLatitude()));
					data.put("distance", String.valueOf(account.getmMerchant().getmRestaurant().getmDeliveryDistance()));
				}
				
			}

		} else {
			statusCode = ParameterConstant.STATUS_CODE_DATA_NOT_EXIST;
			msg = ParameterConstant.MSG_DATA_NOT_EXIST;
		}

		return JSON.toJSON(ResponseUtils.setStatusJSON(statusCode, msg, data));
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
			
			mAccountService.logoutUpdateToken(account.getmId(),Integer.valueOf(view.device) , view.notificationDeviceToken, ParameterConstant.LOGIN_MERCHANT);

			if (account != null) {
				_data.status = ParameterConstant.STATUS_CODE_OPERATION_SUCCESS;
				_data.msg = ParameterConstant.MSG_OPERATION_SUCCESS;

			}

			return _data.toJson();

		}
	
	private OrderFormService mOrderFormService;

	@Autowired
	public void setmOrderFormService(OrderFormService mOrderFormService) {
		this.mOrderFormService = mOrderFormService;
	}

	@RequestMapping(value = "/addThirdPartyOrderForm", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public Object addThirdPartyOrderForm(HttpSession session, OrderForm orderForm, int minInterval,String restId) {

		String statusCode = ParameterConstant.STATUS_CODE_ERROR;
		String msg = ParameterConstant.MSG_OPERATION_ERROR;
		Map<String, Object> data = new HashMap<String, Object>();
		Account account = (Account) session.getAttribute(SESSION_KEY);
		Restaurant restaurant = null;
		if (account == null && !StringUtil.isNumber(restId)){
			msg = "login please ";
			return JSON.toJSON(ResponseUtils.setStatusJSON(statusCode, msg, data));
		}else if(account==null){
			restaurant = mOrderFormService.findById(Restaurant.class,Long.valueOf(restId));
		}else{
			restaurant = account.getmMerchant().getmRestaurant();
		}

		try {
			orderForm.setmCode(GenerateIdUtil.getThirdPartyOrderCode());
			orderForm.setmRestaurant(restaurant);
			orderForm.setmStatus(ParameterConstant.ORDER_FORM_STATUS_CALL_DELIVERY);
			orderForm.setmCallDeliveryTimestamp(TimestampUtil.getMinIntervalTimestamp(minInterval));
			orderForm.setmCreateTimestamp(TimestampUtil.getCurrentTimestamp());
			orderForm.setmIsOneMenu(ParameterConstant.ORDER_FORM_IS_NOT_ONE_MENU);
			mOrderFormService.saveTrd(orderForm);
			statusCode = ParameterConstant.STATUS_CODE_OPERATION_SUCCESS;
			msg = ParameterConstant.MSG_OPERATION_SUCCESS;
			//新增order监听器
			mOrderFormService.addOrderListener(orderForm);
			data.put("orderId", String.valueOf(orderForm.getmId()));
			data.put("orderCode", orderForm.getmCode());
		} catch (Exception e) {
			logger.error("", e);
		}
		return JSON.toJSON(ResponseUtils.setStatusJSON(statusCode, msg, data));
	}
	
	@RequestMapping(value = "/callDeliveryForOrderForm", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public Object callDeliveryForOrderForm(Long orderFormId,int minInterval){
		
		BaseData _data = new BaseData();
		
		boolean result = mOrderFormService.callDeliveryByMerchant(orderFormId,minInterval);
		if(result){
			_data.status = ErrorCode.RequestSuccess.getCode();
			_data.msg = ErrorCode.RequestSuccess.getMsg();
		}
		
		return _data.toJson();
	}
	
	@RequestMapping(value = "/getLocation", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public Object getLocationOfDriver(Long orderFormId){
		
		ObjectBaseData<DLocationBean> _data = new ObjectBaseData<DLocationBean>();
		
		DLocationBean location = mOrderFormService.getLocationByOrderId(orderFormId);
		if(location==null){
			_data.status = ErrorCode.NoData.getCode();
			_data.msg = ErrorCode.NoData.getMsg();
			return _data.toJson();
		}
		_data.status = ErrorCode.RequestSuccess.getCode();
		_data.msg = ErrorCode.RequestSuccess.getMsg();
		_data.data.put("location", location);
		return _data.toJson();
	}
	
	@RequestMapping(value = "/oneMenuOrders", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public Object getOneMenuOrdersByRestId(BaseCallDeliveryReqData data){
		
		ListBaseData<CDRestaurantOrdersBean> _data = new ListBaseData<CDRestaurantOrdersBean>();
		if(!StringUtil.isNumber(data.restId)){
			_data.status = ErrorCode.ParamError.getCode();
			_data.status= ErrorCode.ParamError.getMsgForName("restId");
			return _data.toJson();
		}
		
		List<CDRestaurantOrdersBean> orders = mOrderFormService.getNewOrdersByRestId(Long.valueOf(data.restId));
		
		if(orders==null||orders.isEmpty()){
			_data.status = ErrorCode.NoData.getCode();
			_data.msg = ErrorCode.NoData.getMsg();
			return _data.toJson();
		}
		
		_data.status = ErrorCode.RequestSuccess.getCode();
		_data.msg = ErrorCode.RequestSuccess.getMsg();
		_data.data.put("orderList", orders);
		
		return _data.toJson();
	}
	
	
	@RequestMapping(value = "/callDriverOrders", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public Object getCallDriverOrdersByRestId(BaseCallDeliveryReqData data){
		
		ListBaseData<CDOrderBean> _data = new ListBaseData<CDOrderBean>();
		if(!StringUtil.isNumber(data.restId)){
			_data.status = ErrorCode.ParamError.getCode();
			_data.status= ErrorCode.ParamError.getMsgForName("restId");
			return _data.toJson();
		}
		
		List<CDOrderBean> orders = mOrderFormService.getCallDriverOrdersByRestId(Long.valueOf(data.restId));
		
		if(orders==null||orders.isEmpty()){
			_data.status = ErrorCode.NoData.getCode();
			_data.msg = ErrorCode.NoData.getMsg();
			return _data.toJson();
		}
		
		_data.status = ErrorCode.RequestSuccess.getCode();
		_data.msg = ErrorCode.RequestSuccess.getMsg();
		_data.data.put("orderList", orders);
		
		return _data.toJson();
	}
	
	@RequestMapping(value = "/driverOrders", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public Object getDriverOrdersByRestId(BaseCallDeliveryReqData data){
		
		ListBaseData<CDOrderBean> _data = new ListBaseData<CDOrderBean>();
		if(!StringUtil.isNumber(data.restId)){
			_data.status = ErrorCode.ParamError.getCode();
			_data.status= ErrorCode.ParamError.getMsgForName("restId");
			return _data.toJson();
		}
		
		List<CDOrderBean> orders = mOrderFormService.getDriverOrdersByRestId(Long.valueOf(data.restId));
		
		if(orders==null||orders.isEmpty()){
			_data.status = ErrorCode.NoData.getCode();
			_data.msg = ErrorCode.NoData.getMsg();
			return _data.toJson();
		}
		
		_data.status = ErrorCode.RequestSuccess.getCode();
		_data.msg = ErrorCode.RequestSuccess.getMsg();
		_data.data.put("orderList", orders);
		
		return _data.toJson();
	}
	
	@RequestMapping(value = "/allCallOrders", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public Object getAllCallOrdersByRestId(BaseCallDeliveryPageReqData data){
		
		ListBaseData<CDOrderBean> _data = new ListBaseData<CDOrderBean>();
		if(!StringUtil.isNumber(data.restId)){
			_data.status = ErrorCode.ParamError.getCode();
			_data.status= ErrorCode.ParamError.getMsgForName("restId");
			return _data.toJson();
		}else if(!StringUtil.isNumber(data.startNum)){
			_data.status = ErrorCode.ParamError.getCode();
			_data.status= ErrorCode.ParamError.getMsgForName("startNum");
			return _data.toJson();
		}else if(!StringUtil.isNumber(data.range)){
			_data.status = ErrorCode.ParamError.getCode();
			_data.status= ErrorCode.ParamError.getMsgForName("range");
			return _data.toJson();
		}
		
		List<CDOrderBean> orders = mOrderFormService.getAllCallOrdersByRestId(Long.valueOf(data.restId),Integer.valueOf(data.startNum),Integer.valueOf(data.range));
		
		if(orders==null||orders.isEmpty()){
			_data.status = ErrorCode.NoData.getCode();
			_data.msg = ErrorCode.NoData.getMsg();
			return _data.toJson();
		}
		
		_data.status = ErrorCode.RequestSuccess.getCode();
		_data.msg = ErrorCode.RequestSuccess.getMsg();
		_data.data.put("orderList", orders);
		
		return _data.toJson();
	}
	
	@RequestMapping(value = "/resetTipsFee", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public Object resetTipsFeeByOrderId(ResetTipsFeeData data){
		
		BaseData _data = new BaseData();
		
		if(!StringUtil.isNumber(data.orderFormId)){
			_data.status = ErrorCode.ParamError.getCode();
			_data.msg= ErrorCode.ParamError.getMsgForName("orderFormId");
			return _data.toJson();
		}else if(!StringUtil.isNumber(data.restTipsFee)){
			_data.status = ErrorCode.ParamError.getCode();
			_data.msg= ErrorCode.ParamError.getMsgForName("restTipsFee");
			return _data.toJson();
		}
		
		boolean result = mOrderFormService.resetTipsFeeByOrderId(Long.valueOf(data.orderFormId),new BigDecimal(data.restTipsFee));
		
		if(result){
			_data.status = ErrorCode.RequestSuccess.getCode();
			_data.msg = ErrorCode.RequestSuccess.getMsg();
		}
		
		
		return _data.toJson();
	}
	
	@RequestMapping(value = "/restCancelOrder", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public String restCancelOrder(ResetTipsFeeData data ){
		BaseData _data = new BaseData();
		if(!StringUtil.isNumber(data.orderFormId)){
			_data.status = ErrorCode.ParamError.getCode();
			_data.msg= ErrorCode.ParamError.getMsgForName("orderFormId");
			return _data.toJson();
		}
		
		boolean result = mOrderFormService.restCancelOrderByOrderId(Long.valueOf(data.orderFormId));
		
		if(result){
			_data.status = ErrorCode.RequestSuccess.getCode();
			_data.msg = ErrorCode.RequestSuccess.getMsg();
		}
		return _data.toJson();
	}

}
