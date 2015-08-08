package com.onemenu.server.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.util.TextUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.onemenu.server.cache.ConfigCache;
import com.onemenu.server.constant.ParameterConstant;
import com.onemenu.server.javabean.AppConfigBean;
import com.onemenu.server.protocol.ErrorCode;
import com.onemenu.server.util.ResponseUtils;

@Controller
@RequestMapping("/config")
public class ConfigAppController {

    @RequestMapping(value = "/appConfig", method = RequestMethod.POST,
            produces = "application/json; charset=utf-8")
    @ResponseBody
    public Object appConfig(String appVersion) {

        String statusCode = ParameterConstant.STATUS_CODE_ERROR;
        String msg = ParameterConstant.MSG_OPERATION_ERROR;
        Map<String, Object> data = new HashMap<String, Object>();
        
        //校验参数为空    add  by file on 2015-04-26
        if(TextUtils.isEmpty(appVersion)){
        	statusCode = ErrorCode.ParamError.getCode();
        	msg = ErrorCode.ParamError.getMsgForName("appVersion");
        	return ResponseUtils.setStatusJSON(statusCode, msg, data);
        }
        
        //String appConfigProperties = "/AppConfig.properties";
        if(appVersion==null)
        	appVersion = ConfigCache.getValue("appVersion");
        
        AppConfigBean appConfigBean = new AppConfigBean();
        appConfigBean.tax = ConfigCache.getValue("tax") ;//PropertiesUtils.getProperties("tax", appConfigProperties);
        appConfigBean.appVersion =ConfigCache.getValue("appVersion") ; //PropertiesUtils.getProperties("appVersion", appConfigProperties);
        appConfigBean.appVersionDesc = ConfigCache.getValue("appVersionDesc") ;//PropertiesUtils.getProperties("appVersionDesc", appConfigProperties);
        appConfigBean.forceUpdate =appVersion.compareTo(ConfigCache.getValue("forceUpdateVersion"))>0?"0":"1" ;//PropertiesUtils.getProperties("forceUpdate", appConfigProperties);
        
        statusCode = ParameterConstant.STATUS_CODE_OPERATION_SUCCESS;
        msg = ParameterConstant.MSG_OPERATION_SUCCESS;
        data.put("appConfig", appConfigBean);
        
        return ResponseUtils.setStatusJSON(statusCode, msg, data);
    }
    
}
