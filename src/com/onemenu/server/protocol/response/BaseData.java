package com.onemenu.server.protocol.response;

import com.alibaba.fastjson.JSON;
import com.onemenu.server.constant.ParameterConstant;

public class BaseData {
	public String status = ParameterConstant.STATUS_CODE_ERROR;
	public String msg = ParameterConstant.MSG_OPERATION_ERROR;
	
	public String toJson(){
		return JSON.toJSONString(this);
	}
}
