package com.onemenu.server.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.onemenu.server.constant.ParameterConstant;
import com.onemenu.server.javabean.NDishCategoryBean;
import com.onemenu.server.javabean.NRestaurantDetailBean;
import com.onemenu.server.javabean.NRestaurantItemBean;
import com.onemenu.server.javabean.RestaurantSearchConditionBean;
import com.onemenu.server.protocol.ErrorCode;
import com.onemenu.server.protocol.req.toptags.PostTopTagsData;
import com.onemenu.server.protocol.req.toptags.TopTagsType;
import com.onemenu.server.protocol.response.DishCategorysData;
import com.onemenu.server.protocol.response.OnlyObjectBaseData;
import com.onemenu.server.protocol.response.RestaurantItemsData;
import com.onemenu.server.service.DishCategoryService;
import com.onemenu.server.service.RestaurantService;
import com.onemenu.server.service.TopTagsService;

@Controller
@RequestMapping("/restaurant")
public class RestaurantAppController {

	private RestaurantService mRestaurantService;
	private DishCategoryService mDishCategoryService;
	/** 用户热门标签收集 add by file on 2015-04-22 **/
	@Resource(name = "topTagsService")
	private TopTagsService topTagsService;

	@Autowired
	public void setRestaurantService(RestaurantService mRestaurantService) {
		this.mRestaurantService = mRestaurantService;
	}

	@Autowired
	public void setRestaurantService(DishCategoryService mDishCategoryService) {
		this.mDishCategoryService = mDishCategoryService;
	}

	@RequestMapping("/testApp")
	@ResponseBody
	public void testApp() {
		System.out.println("testApp");
	}

	@RequestMapping(value = "/listRestaurantItem", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public Object listRestaurantItem(HttpSession session, Integer startNum, Integer range, String tags, Double latitude, Double longitude, Double distance) throws Exception {
		// String statusCode = ParameterConstant.STATUS_CODE_ERROR;
		// String msg = ParameterConstant.MSG_OPERATION_ERROR;
		// Map<String, Object> data = new HashMap<String, Object>();

		RestaurantItemsData data = new RestaurantItemsData();

		// 校验参数为空 add by file on 2015-04-26
		if (startNum == null) {
			data.status = ErrorCode.ParamError.getCode();
			data.msg = ErrorCode.ParamError.getMsgForName("startNum");
			return data.toJson();
		} else if (range == null) {
			data.status = ErrorCode.ParamError.getCode();
			data.msg = ErrorCode.ParamError.getMsgForName("range");
			return data.toJson();
		}

		RestaurantSearchConditionBean condition = new RestaurantSearchConditionBean();
		condition.distance = 50000; // distance
		condition.latitude = latitude;
		condition.longitude = longitude;
		condition.tags = tags;
		condition.startNum = startNum;
		condition.range = range;

		// tag不为空，加入用户tag of Restaurant add by file on 2015-04-22
		if (!TextUtils.isEmpty(tags)) {
			PostTopTagsData _data = new PostTopTagsData();
			_data.latitude = latitude.toString();
			_data.longitude = longitude.toString();
			_data.type = TopTagsType.Restaurant.getValue();
			_data.tags = tags;
			topTagsService.postTopTags(_data);
		}

		// List<RestaurantBean> restaurantBeanList =
		// mRestaurantService.listRestaurantBean(condition);
		List<NRestaurantItemBean> restaurantBeanList = mRestaurantService.restaurantItems(condition);

		int restaurantBeanListSize = restaurantBeanList.size();
		if (restaurantBeanListSize == 0 || restaurantBeanListSize <= startNum) {
			data.status = ParameterConstant.STATUS_CODE_DATA_EMPTY;
			data.msg = ParameterConstant.MSG_DATA_EMPTY;
		} else {

			int endNum = startNum + range;
			if (endNum > restaurantBeanListSize) {
				endNum = restaurantBeanListSize;
			}

			data.status = ParameterConstant.STATUS_CODE_OPERATION_SUCCESS;
			data.msg = ParameterConstant.MSG_OPERATION_SUCCESS;
			data.data.put("restaurantArray", restaurantBeanList.subList(startNum, endNum));
		}

		return data.toJson();
	}

	@RequestMapping(value = "/getDishCategoryListByRestaurantId", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public Object getDishCategoryListByRestaurantId(long restaurantId) throws Exception {

		// String statusCode = ParameterConstant.STATUS_CODE_ERROR;
		// String msg = ParameterConstant.MSG_OPERATION_ERROR;
		// Map<String, Object> data = new HashMap<String, Object>();
		DishCategorysData _data = new DishCategorysData();

		// 校验参数为空 add by file on 2015-04-26
		if (restaurantId == 0l) {
			_data.status = ErrorCode.ParamError.getCode();
			_data.msg = ErrorCode.ParamError.getMsgForName("restaurantId");
			return _data.toJson();
		}

		// RestaurantBean restaurantDetailBean = new RestaurantBean();
		// restaurantDetailBean.restaurantId = String.valueOf(restaurantId);

		// List<DishCategoryBean> dishCategoryDetailBeanList =
		// mDishCategoryService.getDishCategoryListByRestaurantId(restaurantId);

		List<NDishCategoryBean> dishCategoryDetailBeanList = mDishCategoryService.getDishCategorysByRestaurantId(restaurantId);

		if (dishCategoryDetailBeanList.size() == 0) {
			_data.status = ParameterConstant.STATUS_CODE_DATA_EMPTY;
			_data.msg = ParameterConstant.MSG_DATA_EMPTY;
		} else {
			_data.status = ParameterConstant.STATUS_CODE_OPERATION_SUCCESS;
			_data.msg = ParameterConstant.MSG_OPERATION_SUCCESS;
			// data.put("restaurant", restaurantDetailBean);
			_data.data.put("dishCategoryArray", dishCategoryDetailBeanList);
		}

		return _data.toJson();
	}

	@RequestMapping(value = "/getRestaurantDetailByRestaurantId", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public Object getRestaurantDetailByRestaurantId(long restaurantId) {
		
		OnlyObjectBaseData<NRestaurantDetailBean> _data = new OnlyObjectBaseData<NRestaurantDetailBean>();
		if (restaurantId == 0l) {
			_data.status = ErrorCode.ParamError.getCode();
			_data.msg = ErrorCode.ParamError.getMsgForName("restaurantId");
			return _data.toJson();
		}

		NRestaurantDetailBean bean = mRestaurantService.getRestaurantDetailInfo(restaurantId);
		
		if(bean==null){
			_data.status = ErrorCode.DataNoExist.getCode();
			_data.msg = ErrorCode.DataNoExist.getMsg();
			return _data.toJson();
		}
		
		_data.status = ErrorCode.RequestSuccess.getCode();
		_data.status = ErrorCode.RequestSuccess.getMsg();
		_data.data = bean;
		return _data.toJson();
		
		
	}

}
