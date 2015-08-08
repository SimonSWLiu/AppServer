package com.onemenu.server.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.onemenu.server.constant.ParameterConstant;
import com.onemenu.server.javabean.DishBean;
import com.onemenu.server.javabean.DishValidationConditionBean;
import com.onemenu.server.javabean.MenuSearchConditionBean;
import com.onemenu.server.javabean.NMenuItemBean;
import com.onemenu.server.javabean.NMenuItemDishBean;
import com.onemenu.server.javabean.ReviewBean;
import com.onemenu.server.javabean.TasteSearchConditionBean;
import com.onemenu.server.javabean.TradeSearchConditionBean;
import com.onemenu.server.model.Review;
import com.onemenu.server.protocol.ErrorCode;
import com.onemenu.server.protocol.req.toptags.PostTopTagsData;
import com.onemenu.server.protocol.req.toptags.TopTagsType;
import com.onemenu.server.protocol.response.ListBaseData;
import com.onemenu.server.protocol.response.MenuItemsData;
import com.onemenu.server.service.CustomerService;
import com.onemenu.server.service.DishService;
import com.onemenu.server.service.ReviewService;
import com.onemenu.server.service.TopTagsService;
import com.onemenu.server.service.TradeService;
import com.onemenu.server.util.JsonFormatUtils;
import com.onemenu.server.util.ResponseUtils;

@Controller
@RequestMapping("/menu")
public class MenuAppController {

    private CustomerService mCustomerService;
    
    private DishService mDishService;
    private ReviewService mReviewService;
    private TradeService mTradeService;
    /**用户热门标签收集   add by file on 2015-04-22 **/
    @Resource(name="topTagsService")
	private TopTagsService topTagsService;
    
    private static final Logger logger = Logger.getLogger(MenuAppController.class);

    @Autowired
    public void setCustomerService(CustomerService mCustomerService) {
        this.mCustomerService = mCustomerService;
    }

    @Autowired
    public void setDishService(DishService mDishService) {
        this.mDishService = mDishService;
    }

    @Autowired
    public void setReviewService(ReviewService service) {
        this.mReviewService = service;
    }

    @Autowired
    public void setmTradeService(TradeService mTradeService) {
        this.mTradeService = mTradeService;
    }

    /**
     * 
     * @param session 用户session
     * @param startNum
     * @param range
     * @param type R D A
     * @param tags ***,***,***
     * @param latitude 纬度
     * @param longitude 经度
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/listMenuItem", method = RequestMethod.POST,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    public String listMenuItem(String type, Integer startNum, Integer range, String tags,
            Double latitude, Double longitude) throws Exception {
    	
        MenuItemsData _data = new MenuItemsData();
        
        //校验参数为空    add  by file on 2015-04-26
        if(TextUtils.isEmpty(type)){
        	_data.status = ErrorCode.ParamError.getCode();
        	_data.msg = ErrorCode.ParamError.getMsgForName("type");
        	return _data.toJson();
        }else if(startNum==null){
        	_data.status = ErrorCode.ParamError.getCode();
        	_data.msg = ErrorCode.ParamError.getMsgForName("startNum");
        	return _data.toJson();
        }else if(range==null){
        	_data.status = ErrorCode.ParamError.getCode();
        	_data.msg = ErrorCode.ParamError.getMsgForName("range");
        	return _data.toJson();
        }
        
        
        if (type.equals(ParameterConstant.MENU_ITEM_TYPE_DISH)
                || type.equals(ParameterConstant.MENU_ITEM_TYPE_REVIEW) 
                || type.equals(ParameterConstant.MENU_ITEM_TYPE_ALL)) {
        	if(latitude==null)
        		latitude = 0.0;
        	if(longitude==null)
        		longitude = 0.0;
        	
            MenuSearchConditionBean condition = new MenuSearchConditionBean();
            condition.keyWord = tags;
            condition.startNum = startNum;
            condition.range = range;
            condition.latitude = latitude;
            condition.longitude = longitude;
            condition.distance = 50000; // km
            
           //tag不为空，加入用户tag of menu  add by file on 2015-04-22
           if(!TextUtils.isEmpty(tags)){
        	   PostTopTagsData data = new PostTopTagsData();
        	   data.latitude = latitude.toString();
        	   data.longitude = longitude.toString();
        	   data.type=TopTagsType.Menu.getValue();
        	   data.tags = tags;
			   topTagsService.postTopTags(data);
           }
            
            List<NMenuItemBean> menuItemList = null;

            if (type.equals(ParameterConstant.MENU_ITEM_TYPE_DISH)) {
            	//menuItemList.addAll(mDishService.listDishMenuItemByCondition(condition));
            	menuItemList = mDishService.menuItemDishsByCondition(condition);
            } else if (type.equals(ParameterConstant.MENU_ITEM_TYPE_REVIEW)) {
            	menuItemList = mReviewService.menuItemReviewsByCondition(condition);
            } else if (type.equals(ParameterConstant.MENU_ITEM_TYPE_ALL)) {
               // menuItemList.addAll(mDishService.listDishMenuItemByCondition(condition));
               //menuItemList.addAll(mReviewService.listReviewMenuItemByCondition(condition));
            	menuItemList = mDishService.menuItemDishAndReviewsByCondition(condition);
            }
            if(menuItemList==null||menuItemList.isEmpty()){
            	_data.status = ParameterConstant.STATUS_CODE_DATA_EMPTY;
            	_data.msg = ParameterConstant.MSG_DATA_EMPTY;
            }else{
            	_data.status = ParameterConstant.STATUS_CODE_OPERATION_SUCCESS;
            	_data.msg = ParameterConstant.MSG_OPERATION_SUCCESS;
            	_data.data .put("menuItemArray", menuItemList);
            }
           
            /*
            Collections.sort(menuItemList, new Comparator<MenuItemBean>() {

                public int compare(MenuItemBean arg0, MenuItemBean arg1) {

                    return arg1.rank.compareTo(arg0.rank);
                }
            });*/
            /*
            int orderItemBeanListSize = menuItemList.size();
            if (orderItemBeanListSize == 0 || orderItemBeanListSize <= startNum) {

                statusCode = ParameterConstant.STATUS_CODE_DATA_EMPTY;
                msg = ParameterConstant.MSG_DATA_EMPTY;
            } else {

                int endNum = startNum + range;
                if (endNum > orderItemBeanListSize) {
                    endNum = orderItemBeanListSize;
                }

                statusCode = ParameterConstant.STATUS_CODE_OPERATION_SUCCESS;
                msg = ParameterConstant.MSG_OPERATION_SUCCESS;
                data.put("menuItemArray", menuItemList.subList(startNum, endNum));
            }*/
        }
          
        return _data.toJson();
    }

    @RequestMapping(value = "/getMenuItemById", method = RequestMethod.POST,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object getMenuItemById(Double latitude, Double longitude, long id, String type) {

        String statusCode = ParameterConstant.STATUS_CODE_ERROR;
        String msg = ParameterConstant.MSG_OPERATION_ERROR;
        Map<String, Object> data = new HashMap<String, Object>();
        
        //校验参数为空    add  by file on 2015-04-26
        if(id==0l){
        	statusCode = ErrorCode.ParamError.getCode();
        	msg = ErrorCode.ParamError.getMsgForName("id");
        	return ResponseUtils.setStatusJSON(statusCode, msg, data);
        }else if(TextUtils.isEmpty(type)){
        	statusCode = ErrorCode.ParamError.getCode();
        	msg = ErrorCode.ParamError.getMsgForName("type");
        	return ResponseUtils.setStatusJSON(statusCode, msg, data);
        }

        MenuSearchConditionBean condition = new MenuSearchConditionBean();
        condition.latitude = latitude;
        condition.longitude = longitude;

        if (type.equals(ParameterConstant.MENU_ITEM_TYPE_DISH)) {
            DishBean detailItemJsonBean = mDishService.getDishBeanById(condition, id);
            if (detailItemJsonBean != null) {
                detailItemJsonBean.type = ParameterConstant.MENU_ITEM_TYPE_DISH;
                data.put("menuItem", detailItemJsonBean);
                statusCode = ParameterConstant.STATUS_CODE_OPERATION_SUCCESS;
                msg = ParameterConstant.MSG_OPERATION_SUCCESS;
            } else {
                //
                statusCode = ParameterConstant.STATUS_CODE_DATA_EMPTY;
                msg = ParameterConstant.MSG_DATA_EMPTY;
            }

        } else if (type.equals(ParameterConstant.MENU_ITEM_TYPE_REVIEW)) {
            ReviewBean reviewBean = mReviewService.getReviewBean(id);
            if (reviewBean != null) {
                data.put("menuItem", reviewBean);
                statusCode = ParameterConstant.STATUS_CODE_OPERATION_SUCCESS;
                msg = ParameterConstant.MSG_OPERATION_SUCCESS;
            } else {
                //
                statusCode = ParameterConstant.STATUS_CODE_DATA_EMPTY;
                msg = ParameterConstant.MSG_DATA_EMPTY;
            }
        }

        return ResponseUtils.setStatusJSON(statusCode, msg, data);
    }

    @RequestMapping(value = "/uploadReviewContent", method = RequestMethod.POST,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object uploadReviewContent(long customerId, String reviewJsonStr) {

        String statusCode = ParameterConstant.STATUS_CODE_ERROR;
        String msg = ParameterConstant.MSG_OPERATION_ERROR;
        Map<String, Object> data = new HashMap<String, Object>();
        
        //校验参数为空    add  by file on 2015-04-26
        if(customerId==0l){
        	statusCode = ErrorCode.ParamError.getCode();
        	msg = ErrorCode.ParamError.getMsgForName("customerId");
        	return ResponseUtils.setStatusJSON(statusCode, msg, data);
        }else if(TextUtils.isEmpty(reviewJsonStr)){
        	statusCode = ErrorCode.ParamError.getCode();
        	msg = ErrorCode.ParamError.getMsgForName("reviewJsonStr");
        	return ResponseUtils.setStatusJSON(statusCode, msg, data);
        }

        try {

            ReviewBean reviewBean = JsonFormatUtils.getReview(customerId, reviewJsonStr);

            //Review review =
            		mReviewService.saveReviewBean(reviewBean);

            statusCode = ParameterConstant.STATUS_CODE_OPERATION_SUCCESS;
            msg = ParameterConstant.MSG_OPERATION_SUCCESS;

        } catch (Exception e) {
            logger.error("", e);
        }

        return ResponseUtils.setStatusJSON(statusCode, msg, data);
    }

    @RequestMapping(value = "/getOrderItemDish", method = RequestMethod.POST,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object getOrderItemDish(long customerId, Integer startNum, Integer range) {

       
    	/**
        String statusCode = ParameterConstant.STATUS_CODE_ERROR;
        String msg = ParameterConstant.MSG_OPERATION_ERROR;
        Map<String, Object> data = new HashMap<String, Object>();
        **/
    	ListBaseData<NMenuItemDishBean> _data = new ListBaseData<NMenuItemDishBean>();
        
        //校验参数为空    add  by file on 2015-04-26
        if(customerId==0l){
        	_data.status = ErrorCode.ParamError.getCode();
        	_data.msg = ErrorCode.ParamError.getMsgForName("customerId");
        	return _data.toJson();
        }else if(startNum==null){
        	_data.status = ErrorCode.ParamError.getCode();
        	_data.msg = ErrorCode.ParamError.getMsgForName("startNum");
        	return _data.toJson();
        }else if(range==null){
        	_data.status = ErrorCode.ParamError.getCode();
        	_data.msg = ErrorCode.ParamError.getMsgForName("range");
        	return _data.toJson();
        }
        
        TradeSearchConditionBean condition = new TradeSearchConditionBean();
        condition.startNum = startNum;
        condition.range = range;
        

        //List<OrderItemBean> orderItemBeanList = mTradeService.listOrderItemByCustomerId(customerId);
        List<NMenuItemDishBean> orderItemBeanList = mTradeService.orderItemsByCustomerId(customerId);

        int orderItemBeanListSize = orderItemBeanList.size();
        if (orderItemBeanListSize == 0 || orderItemBeanListSize <= startNum) {
        	_data.status = ParameterConstant.STATUS_CODE_DATA_EMPTY;
        	_data.msg = ParameterConstant.MSG_DATA_EMPTY;
        } else {

            int endNum = startNum + range;
            if (endNum > orderItemBeanListSize) {
                endNum = orderItemBeanListSize;
            }

            _data.status = ParameterConstant.STATUS_CODE_OPERATION_SUCCESS;
            _data.msg = ParameterConstant.MSG_OPERATION_SUCCESS;
            _data.data.put("orderItemArray", orderItemBeanList.subList(startNum, endNum));
        }

        return _data.toJson();
    }

    @RequestMapping(value = "/getMyTaste", method = RequestMethod.POST,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object getMyTaste(long customerId, Integer startNum, Integer range) {

       

//        String statusCode = ParameterConstant.STATUS_CODE_ERROR;
//        String msg = ParameterConstant.MSG_OPERATION_ERROR;
//        Map<String, Object> data = new HashMap<String, Object>();
    	
    	ListBaseData<NMenuItemBean> _data = new ListBaseData<NMenuItemBean>(); 
        
        
        //校验参数为空    add  by file on 2015-04-26
        if(customerId==0l){
        	_data.status = ErrorCode.ParamError.getCode();
        	_data.msg = ErrorCode.ParamError.getMsgForName("customerId");
        	return _data.toJson();
        }else if(startNum==null){
        	_data.status = ErrorCode.ParamError.getCode();
        	_data.msg = ErrorCode.ParamError.getMsgForName("startNum");
        	return _data.toJson();
        }else if(range==null){
        	_data.status = ErrorCode.ParamError.getCode();
        	_data.msg = ErrorCode.ParamError.getMsgForName("range");
        	return _data.toJson();
        }
        
        TasteSearchConditionBean condition = new TasteSearchConditionBean();
        condition.startNum = startNum;
        condition.range = range;

        //List<OrderItemBean> orderItemBeanList = mTradeService.listOrderItemByCustomerId(customerId);
        //List<ReviewBean> reviewBeanList = mReviewService.listReviewBeanByCustomerId(customerId);
        
        List<NMenuItemBean> myTasteList = mTradeService.getTastesByCustomerId(customerId);
  

        int myTasteListSize = myTasteList.size();
        if (myTasteListSize == 0 || myTasteListSize <= startNum) {
        	_data.status = ParameterConstant.STATUS_CODE_DATA_EMPTY;
        	_data.msg = ParameterConstant.MSG_DATA_EMPTY;
        } else {

            int endNum = startNum + range;
            if (endNum > myTasteListSize) {
                endNum = myTasteListSize;
            }

            _data.status = ParameterConstant.STATUS_CODE_OPERATION_SUCCESS;
            _data.msg = ParameterConstant.MSG_OPERATION_SUCCESS;
            _data.data.put("myTasteArray", myTasteList.subList(startNum, endNum));
        }

        return _data.toJson()          ;
    }


    @RequestMapping(value = "/validateDish", method = RequestMethod.POST,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object validateDish(Double latitude, Double longitude, String dishIdListStr) {

        /*String statusCode = ParameterConstant.STATUS_CODE_ERROR;
        String msg = ParameterConstant.MSG_OPERATION_ERROR;
        Map<String, Object> data = new HashMap<String, Object>();*/
    	
    	ListBaseData<NMenuItemDishBean> _data = new ListBaseData<NMenuItemDishBean>();
        
        
        //校验参数为空    add  by file on 2015-04-26
        if(TextUtils.isEmpty(dishIdListStr)){
        	_data.status = ErrorCode.ParamError.getCode();
        	_data.msg = ErrorCode.ParamError.getMsgForName("dishIdListStr");
        	return _data.toJson();
        }
        /**else if(latitude==null){
        	statusCode = ErrorCode.ParamError.getCode();
        	msg = ErrorCode.ParamError.getMsgForName("latitude");
        	return ResponseUtils.setStatusJSON(statusCode, msg, data);
        }else if(longitude==null){
        	statusCode = ErrorCode.ParamError.getCode();
        	msg = ErrorCode.ParamError.getMsgForName("longitude");
        	return ResponseUtils.setStatusJSON(statusCode, msg, data);
        }**/
        
        

        DishValidationConditionBean condition = new DishValidationConditionBean();
        condition.latitude = latitude;
        condition.longitude = longitude;

        String dishIdArray[] = dishIdListStr.split(",");
        List<Long> dishIdList = new ArrayList<Long>();
        for (String dishId : dishIdArray) {

            dishIdList.add(Long.parseLong(dishId));
        }

        
        //List<DishBean> dishBeanList= mDishService.validateDish(condition, dishIdList);
		try {
			List<NMenuItemDishBean> dishBeanList = mDishService.validateDishs(condition, dishIdList);
			
			if (dishBeanList.isEmpty()) {
				_data.status = ParameterConstant.STATUS_CODE_OPERATION_SUCCESS;
				_data.msg = ParameterConstant.MSG_OPERATION_SUCCESS;
			}else{
				_data.data.put("dishArray", dishBeanList);
				//_data.status = ParameterConstant.STATUS_CODE_OPERATION_SUCCESS;
				//O_data.msg = ParameterConstant.MSG_OPERATION_SUCCESS;
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		

        return _data.toJson();
    }

    @RequestMapping(value = "/delReviewByReviewId", method = RequestMethod.POST,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object delReviewByReviewId(long reviewId) {

        String statusCode = ParameterConstant.STATUS_CODE_ERROR;
        String msg = ParameterConstant.MSG_OPERATION_ERROR;
        Map<String, Object> data = new HashMap<String, Object>();
        
        //校验参数为空    add  by file on 2015-04-26
        if(reviewId==0l){
        	statusCode = ErrorCode.ParamError.getCode();
        	msg = ErrorCode.ParamError.getMsgForName("reviewId");
        	return ResponseUtils.setStatusJSON(statusCode, msg, data);
        }


        Review review = mReviewService.findById(Review.class, reviewId);
        mReviewService.deleteTrd(review);

        statusCode = ParameterConstant.STATUS_CODE_OPERATION_SUCCESS;
        msg = ParameterConstant.MSG_OPERATION_SUCCESS;

        return ResponseUtils.setStatusJSON(statusCode, msg, data);
    }

}
