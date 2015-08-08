package com.onemenu.server.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.onemenu.server.constant.ParameterConstant;
import com.onemenu.server.javabean.TopTagsBean;
import com.onemenu.server.protocol.req.toptags.GetTopTagsData;
import com.onemenu.server.protocol.req.toptags.PostTopTagsData;
import com.onemenu.server.protocol.response.BaseData;
import com.onemenu.server.protocol.response.TopTagsData;
import com.onemenu.server.service.TopTagsService;

/**
 * 热门标签处理类
 * 
 * @author file
 * @since 2015-4-21 下午4:17:30
 * @version 1.0
 */
@Controller
@RequestMapping("/toptags")
public class TopTagsAppController {
	
	@Resource(name="topTagsService")
	private TopTagsService topTagsService;
	
	
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public String listTopTags(GetTopTagsData resData) {
		List<TopTagsBean> list = topTagsService.getTopTagsData(resData);
		//respond data
		TopTagsData data = new TopTagsData();
		if(list==null||list.isEmpty()){
			data.status = ParameterConstant.STATUS_CODE_DATA_EMPTY;
			data.msg = ParameterConstant.MSG_DATA_EMPTY;
		}else{
			data.status = ParameterConstant.STATUS_CODE_OPERATION_SUCCESS;
			data.msg = ParameterConstant.MSG_OPERATION_SUCCESS;
			data.data.put("topTags", list);
		}
		return data.toJson();
	}

	@RequestMapping(value = "/listMenu", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public String listMenuTopTags(GetTopTagsData data) {
		data.type = "1";
		return listTopTags(data);
	}
	
	
	@RequestMapping(value = "/listRestaurant", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public String listRestaurantTopTags(GetTopTagsData data) {
		data.type = "2";
		return listTopTags(data);
	}
	
	@RequestMapping(value = "/post", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public String postTopTags(PostTopTagsData resData){
		
		boolean isSuccess = topTagsService.postTopTags(resData);
		
		BaseData data = new BaseData();
		if(isSuccess){
			data.status=ParameterConstant.STATUS_CODE_OPERATION_SUCCESS;
			data.msg = ParameterConstant.MSG_OPERATION_SUCCESS;
		}
		return JSON.toJSONString(data);
	}
	
	@RequestMapping(value = "/postMenu", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public String postMenuTopTags(PostTopTagsData data) {
		data.type = "1";
		return postTopTags(data);
	}
	
	
	@RequestMapping(value = "/postRestaurant", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public String postRestaurantTopTags(PostTopTagsData data) {
		data.type = "2";
		return postTopTags(data);
	}
	

}
