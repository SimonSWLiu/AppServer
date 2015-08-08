package com.onemenu.server.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.onemenu.server.protocol.ErrorCode;
import com.onemenu.server.protocol.req.useranalysis.InOrOutView;
import com.onemenu.server.protocol.response.BaseData;
import com.onemenu.server.service.UserAnalysisService;
import com.onemenu.server.util.StringUtil;

@Controller
@RequestMapping("/userAnalysis")
public class UserAnalysisController {

	@Resource(name = "userAnalysisService")
	private UserAnalysisService	userAnalysisService;

	@RequestMapping(value = "/in", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public String intoView(InOrOutView view) {

		BaseData _data = new BaseData();

		if (StringUtil.isEmpty(view.deviceId)) {
			_data.status = ErrorCode.ParamError.getCode();
			_data.msg = ErrorCode.ParamError.getMsgForName("deviceId");
			return _data.toJson();
		} else if (StringUtil.isEmpty(view.viewControllerName)) {
			_data.status = ErrorCode.ParamError.getCode();
			_data.msg = ErrorCode.ParamError.getMsgForName("viewControllerName");
			return _data.toJson();
		}
		
		boolean isSuccess = userAnalysisService.saveIn(view);
		if(isSuccess){
			_data.status = ErrorCode.RequestSuccess.getCode();
			_data.msg = ErrorCode.RequestSuccess.getMsg();
		}
		return _data.toJson();
	}

	@RequestMapping(value = "/out", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public String getOutView(InOrOutView view) {
		BaseData _data = new BaseData();

		if (StringUtil.isEmpty(view.deviceId)) {
			_data.status = ErrorCode.ParamError.getCode();
			_data.msg = ErrorCode.ParamError.getMsgForName("deviceId");
			return _data.toJson();
		} else if (StringUtil.isEmpty(view.viewControllerName)) {
			_data.status = ErrorCode.ParamError.getCode();
			_data.msg = ErrorCode.ParamError.getMsgForName("viewControllerName");
			return _data.toJson();
		}
		boolean isSuccess = userAnalysisService.saveOut(view);
		if(isSuccess){
			_data.status = ErrorCode.RequestSuccess.getCode();
			_data.msg = ErrorCode.RequestSuccess.getMsg();
		}
		return _data.toJson();
	}

}
