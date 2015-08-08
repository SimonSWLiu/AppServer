package com.onemenu.server.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.onemenu.server.javabean.admin.ARegionBean;
import com.onemenu.server.javabean.admin.ARestaurantBean;
import com.onemenu.server.model.Region;
import com.onemenu.server.protocol.ErrorCode;
import com.onemenu.server.protocol.req.BasePageRequestData;
import com.onemenu.server.protocol.response.ListBaseData;
import com.onemenu.server.protocol.response.OnlyObjectBaseData;
import com.onemenu.server.service.RegionService;
import com.onemenu.server.util.StringUtil;

@Controller
@RequestMapping("/region")
public class RegionAppController {
	
	@Resource(name="regionService")
	private RegionService regionService;
	
	@RequestMapping(value = "/allRegion", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public String getAllRegions(BasePageRequestData data) throws Exception {
		ListBaseData<ARegionBean> _data = new ListBaseData<ARegionBean>();
		
		if(!StringUtil.isNumber(data.startNum)){
			_data.status = ErrorCode.ParamError.getCode();
			_data.msg = ErrorCode.ParamError.getMsgForName("startNum");
			return _data.toJson();
		}else if(!StringUtil.isNumber(data.range)){
			_data.status = ErrorCode.ParamError.getCode();
			_data.msg = ErrorCode.ParamError.getMsgForName("range");
			return _data.toJson();
		}
		
		
		List<ARegionBean> beans = regionService.getAllRegions(Integer.valueOf(data.startNum),Integer.valueOf(data.range));
		if(beans==null||beans.isEmpty()){
			_data.status = ErrorCode.NoData.getCode();
			_data.msg = ErrorCode.NoData.getMsg();
			return _data.toJson();
		}
		_data.status = ErrorCode.RequestSuccess.getCode();
		_data.msg = ErrorCode.RequestSuccess.getMsg();
		_data.data.put("regionList", beans);
		
		return _data.toJson();
	}
	
	@RequestMapping(value = "/addRegion", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public String  addRegion(String regionName){
		OnlyObjectBaseData<ARegionBean> _data = new OnlyObjectBaseData<ARegionBean>();
		if(StringUtil.isEmpty(regionName)){
			_data.status = ErrorCode.ParamError.getCode();
			_data.msg = ErrorCode.ParamError.getMsgForName("regionName");
			return _data.toJson();
		}
		
		Region region = regionService.saveRegion(regionName);
		
		if(region==null)
			return _data.toJson();
		
		_data.status = ErrorCode.RequestSuccess.getCode();
		_data.msg = ErrorCode.RequestSuccess.getMsg();
		
		ARegionBean bean = new ARegionBean();
		bean.regionId = String.valueOf(region.getId());
		bean.regionName = region.getName();
		
		_data.data = bean;
		
		return _data.toJson();
	}
	
	@RequestMapping(value = "/updateRegion", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public String  updateRegion(String regionId,String restId){
		OnlyObjectBaseData<ARegionBean> _data = new OnlyObjectBaseData<ARegionBean>();
		if(!StringUtil.isNumber(regionId)){
			_data.status = ErrorCode.ParamError.getCode();
			_data.msg = ErrorCode.ParamError.getMsgForName("regionId");
			return _data.toJson();
		}else if(!StringUtil.isNumber(restId)){
			_data.status = ErrorCode.ParamError.getCode();
			_data.msg = ErrorCode.ParamError.getMsgForName("restId");
			return _data.toJson();
		} 
		
		boolean result = regionService.updateRestRegion(Long.valueOf(restId),Long.valueOf(regionId));
		
		if(result){
			_data.status = ErrorCode.RequestSuccess.getCode();
			_data.msg = ErrorCode.RequestSuccess.getMsg();
		}
			
		
		
		
		return _data.toJson();
	}
	
	@RequestMapping(value = "/updateDriverRegion", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public String  updateDriverRegion(String regionId,String driverId){
		OnlyObjectBaseData<ARegionBean> _data = new OnlyObjectBaseData<ARegionBean>();
		if(!StringUtil.isNumber(regionId)){
			_data.status = ErrorCode.ParamError.getCode();
			_data.msg = ErrorCode.ParamError.getMsgForName("regionId");
			return _data.toJson();
		}else if(!StringUtil.isNumber(driverId)){
			_data.status = ErrorCode.ParamError.getCode();
			_data.msg = ErrorCode.ParamError.getMsgForName("driverId");
			return _data.toJson();
		} 
		
		boolean result = regionService.updateDriverRegion(Long.valueOf(driverId),Long.valueOf(regionId));
		
		if(result){
			_data.status = ErrorCode.RequestSuccess.getCode();
			_data.msg = ErrorCode.RequestSuccess.getMsg();
		}
			
		
		return _data.toJson();
	}
	
	@RequestMapping(value = "/restRegion", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public String getRestRegions(BasePageRequestData data) throws Exception {
		
		ListBaseData<ARestaurantBean> _data = new ListBaseData<ARestaurantBean>();
		if(!StringUtil.isNumber(data.startNum)){
			_data.status = ErrorCode.ParamError.getCode();
			_data.msg = ErrorCode.ParamError.getMsgForName("startNum");
			return _data.toJson();
		}else if(!StringUtil.isNumber(data.range)){
			_data.status = ErrorCode.ParamError.getCode();
			_data.msg = ErrorCode.ParamError.getMsgForName("range");
			return _data.toJson();
		}
		
		
		List<ARestaurantBean> beans = regionService.getRests(Integer.valueOf(data.startNum),Integer.valueOf(data.range));
		if(beans==null||beans.isEmpty()){
			_data.status = ErrorCode.NoData.getCode();
			_data.msg = ErrorCode.NoData.getMsg();
			return _data.toJson();
		}
		_data.status = ErrorCode.RequestSuccess.getCode();
		_data.msg = ErrorCode.RequestSuccess.getMsg();
		_data.data.put("restList", beans);
		
		return _data.toJson();
	}
	
	

}
