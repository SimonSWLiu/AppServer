package com.onemenu.server.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.onemenu.server.constant.ParameterConstant;
import com.onemenu.server.protocol.ErrorCode;
import com.onemenu.server.service.AccountService;
import com.onemenu.server.service.DishService;
import com.onemenu.server.service.RestaurantService;
import com.onemenu.server.service.ReviewService;
import com.onemenu.server.service.TradeService;
import com.onemenu.server.util.ResponseUtils;

@Controller
@RequestMapping("/statistics")
public class StatisticsAppController {

    private DishService mDishService;
    private RestaurantService mRestaurantService;
    private ReviewService mReviewService;
    private TradeService mTradeService;
    private AccountService mAccountService;

   
    @RequestMapping(value = "/addDishPageView", //method = RequestMethod.POST,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object addDishPageView(long dishId) {


        String statusCode = ParameterConstant.STATUS_CODE_ERROR;
        String msg = ParameterConstant.MSG_OPERATION_ERROR;
        Map<String, Object> data = new HashMap<String, Object>();
        //校验参数为空    add  by file on 2015-04-26
        if(dishId==0l){
        	statusCode = ErrorCode.ParamError.getCode();
        	msg = ErrorCode.ParamError.getMsgForName("dishId");
        	return ResponseUtils.setStatusJSON(statusCode, msg, data);
        }

        mDishService.addPageView(dishId);

        statusCode = ParameterConstant.STATUS_CODE_OPERATION_SUCCESS;
        msg = ParameterConstant.MSG_OPERATION_SUCCESS;

        return ResponseUtils.setStatusJSON(statusCode, msg, data);
    }

    @RequestMapping(value = "/addRestaurantPageView", //method = RequestMethod.POST,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object addRestaurantPageView(long restaurantId) {

        String statusCode = ParameterConstant.STATUS_CODE_ERROR;
        String msg = ParameterConstant.MSG_OPERATION_ERROR;
        Map<String, Object> data = new HashMap<String, Object>();
        
        //校验参数为空    add  by file on 2015-04-26
        if(restaurantId==0l){
        	statusCode = ErrorCode.ParamError.getCode();
        	msg = ErrorCode.ParamError.getMsgForName("restaurantId");
        	return ResponseUtils.setStatusJSON(statusCode, msg, data);
        }

        mRestaurantService.addPageView(restaurantId);

        statusCode = ParameterConstant.STATUS_CODE_OPERATION_SUCCESS;
        msg = ParameterConstant.MSG_OPERATION_SUCCESS;

        return ResponseUtils.setStatusJSON(statusCode, msg, data);
    }

    @RequestMapping(value = "/addReviewPageView", //method = RequestMethod.POST,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object addReviewPageView(long reviewId) {

        String statusCode = ParameterConstant.STATUS_CODE_ERROR;
        String msg = ParameterConstant.MSG_OPERATION_ERROR;
        Map<String, Object> data = new HashMap<String, Object>();
        
        //校验参数为空    add  by file on 2015-04-26
        if(reviewId==0l){
        	statusCode = ErrorCode.ParamError.getCode();
        	msg = ErrorCode.ParamError.getMsgForName("reviewId");
        	return ResponseUtils.setStatusJSON(statusCode, msg, data);
        }

        mReviewService.addPageView(reviewId);

        statusCode = ParameterConstant.STATUS_CODE_OPERATION_SUCCESS;
        msg = ParameterConstant.MSG_OPERATION_SUCCESS;

        return ResponseUtils.setStatusJSON(statusCode, msg, data);
    }
    
    
    @RequestMapping(value="/data")
    public String  statisticsData(ModelMap modelMap){
    	
    	modelMap.put("accountCount", mAccountService.getAllAcountCount());
    	modelMap.put("orderCount", mTradeService.getAllOrderCount());
    	    	
    	return "statisticsdata";
    }
    
    
    
    

    @Autowired
    public void setmDishRankService(DishService mDishService) {
        this.mDishService = mDishService;
    }

    @Autowired
    public void setmRestaurantRankService(RestaurantService mRestaurantService) {
        this.mRestaurantService = mRestaurantService;
    }

    @Autowired
    public void setmReviewRankService(ReviewService mReviewService) {
        this.mReviewService = mReviewService;
    }

    @Autowired
    public void setmTradeService(TradeService mTradeService) {
        this.mTradeService = mTradeService;
    }

    @Autowired
    public void setAccountService(AccountService service) {
        this.mAccountService = service;
    }

}
