package com.onemenu.server.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.onemenu.server.constant.ParameterConstant;
import com.onemenu.server.javabean.CouponSearchConditionBean;
import com.onemenu.server.javabean.NCouponBean;
import com.onemenu.server.javabean.NCouponMarketBean;
import com.onemenu.server.protocol.ErrorCode;
import com.onemenu.server.protocol.response.ListBaseData;
import com.onemenu.server.service.CouponService;
import com.onemenu.server.util.ResponseUtils;

@Controller
@RequestMapping("/coupon")
public class CouponAppController {

    private CouponService mCouponService;

    @Autowired
    public void setmCouponService(CouponService mCouponService) {
        this.mCouponService = mCouponService;
    }
    
    @RequestMapping(value = "/getMyChest", method = RequestMethod.POST,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object getMyChest(long customerId) {

//        String statusCode = ParameterConstant.STATUS_CODE_ERROR;
//        String msg = ParameterConstant.MSG_OPERATION_ERROR;
//        Map<String, Object> data = new HashMap<String, Object>();
    	
    	ListBaseData<NCouponBean> _data = new ListBaseData<NCouponBean>();
        
        //校验参数为空    add  by file on 2015-04-26
        if(customerId==0l){
        	_data.status = ErrorCode.ParamError.getCode();
        	_data.msg = ErrorCode.ParamError.getMsgForName("customerId");
        	return _data.toJson();
        }

       // List<CouponBean> couponBeanList = mCouponService.getMyChest(customerId);
        List<NCouponBean> couponBeanList = mCouponService.getMyChests(customerId);
        if (couponBeanList.size() == 0) {
        	_data.status = ParameterConstant.STATUS_CODE_DATA_EMPTY;
        	_data.msg = ParameterConstant.MSG_DATA_EMPTY;
        } else {
        	_data.status = ParameterConstant.STATUS_CODE_OPERATION_SUCCESS;
        	_data.msg = ParameterConstant.MSG_OPERATION_SUCCESS;
        	_data.data.put("myChestArray", couponBeanList);
        }

        return _data.toJson();
    }

    @RequestMapping(value = "/getCouponMarket", method = RequestMethod.POST,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object getCouponMarket(Integer startNum, Integer range, Double latitude, Double longitude) {// location

       

//        String statusCode = ParameterConstant.STATUS_CODE_ERROR;
//        String msg = ParameterConstant.MSG_OPERATION_ERROR;
//        Map<String, Object> data = new HashMap<String, Object>();
    	
    	ListBaseData<NCouponMarketBean> _data = new ListBaseData<NCouponMarketBean>();
    	
        
        //校验参数为空    add  by file on 2015-04-26
        if(startNum==null){
        	_data.status = ErrorCode.ParamError.getCode();
        	_data.msg = ErrorCode.ParamError.getMsgForName("startNum");
        	return _data.toJson();
        }else if(range==null){
        	_data.status = ErrorCode.ParamError.getCode();
        	_data.msg = ErrorCode.ParamError.getMsgForName("range");
        	return _data.toJson();
        }else if(latitude==null||longitude==null){
        	_data.status = ErrorCode.CouponsNotLocation.getCode();
        	_data.msg = ErrorCode.CouponsNotLocation.getMsg();
        	return _data.toJson();
        }
        
        CouponSearchConditionBean condition = new CouponSearchConditionBean();
        condition.startNum = startNum;
        condition.range = range;
        condition.latitude = latitude;
        condition.longitude = longitude;
        condition.distance = 50000;

        //List<CouponBean> couponBeanList = mCouponService.getCouponMarket(condition);
        List<NCouponMarketBean> couponBeanList = mCouponService.getCouponMarkets(condition);
        
        int couponBeanListSize = couponBeanList.size();
        if (couponBeanListSize == 0 || couponBeanListSize <= startNum) {
        	_data.status = ParameterConstant.STATUS_CODE_DATA_EMPTY;
        	_data.msg = ParameterConstant.MSG_DATA_EMPTY;
        } else {

            int endNum = startNum + range;
            if (endNum > couponBeanListSize) {
                endNum = couponBeanListSize;
            }

            _data.status = ParameterConstant.STATUS_CODE_OPERATION_SUCCESS;
            _data.msg = ParameterConstant.MSG_OPERATION_SUCCESS;
            _data.data.put("couponMarketArray", couponBeanList.subList(startNum, endNum));
        }

        return _data.toJson();
    }

    @RequestMapping(value = "/addCouponToCustomer", method = RequestMethod.POST,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object addCouponToCustomer(long customerId, long couponId) {

        String statusCode = ParameterConstant.STATUS_CODE_ERROR;
        String msg = ParameterConstant.MSG_OPERATION_ERROR;
        Map<String, Object> data = new HashMap<String, Object>();
        
        //校验参数为空    add  by file on 2015-04-26
        if(customerId==0l){
        	statusCode = ErrorCode.ParamError.getCode();
        	msg = ErrorCode.ParamError.getMsgForName("customerId");
        	return ResponseUtils.setStatusJSON(statusCode, msg, data);
        }else if(couponId==0l){
        	statusCode = ErrorCode.ParamError.getCode();
        	msg = ErrorCode.ParamError.getMsgForName("couponId");
        	return ResponseUtils.setStatusJSON(statusCode, msg, data);
        }

        
        int result = mCouponService.addCouponToCustomer(customerId, couponId);
        
        switch (result) {
		case 1:
			 statusCode = ParameterConstant.STATUS_CODE_OPERATION_SUCCESS;
	         msg = ParameterConstant.MSG_OPERATION_SUCCESS;
			break;
		case 2:
			statusCode = ErrorCode.CouponsHaveReceived.getCode();
			msg = ErrorCode.CouponsHaveReceived.getMsg();
			break;
		default:
			break;
		}

        return ResponseUtils.setStatusJSON(statusCode, msg, data);
    }
    
    @RequestMapping(value = "/delCouponFromCustomer", method = RequestMethod.POST,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object delCouponFromCustomer(long customerId, long couponId) {

        String statusCode = ParameterConstant.STATUS_CODE_ERROR;
        String msg = ParameterConstant.MSG_OPERATION_ERROR;
        Map<String, Object> data = new HashMap<String, Object>();
        
        //校验参数为空    add  by file on 2015-04-26
        if(customerId==0l){
        	statusCode = ErrorCode.ParamError.getCode();
        	msg = ErrorCode.ParamError.getMsgForName("customerId");
        	return ResponseUtils.setStatusJSON(statusCode, msg, data);
        }else if(couponId==0l){
        	statusCode = ErrorCode.ParamError.getCode();
        	msg = ErrorCode.ParamError.getMsgForName("couponId");
        	return ResponseUtils.setStatusJSON(statusCode, msg, data);
        }

        if (mCouponService.delCouponFromCustomer(customerId, couponId)) {

            statusCode = ParameterConstant.STATUS_CODE_OPERATION_SUCCESS;
            msg = ParameterConstant.MSG_OPERATION_SUCCESS;
        }

        return ResponseUtils.setStatusJSON(statusCode, msg, data);
    }
    
}
