package com.onemenu.server.controller;

import javax.annotation.Resource;

import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.onemenu.server.cache.ConfigCache;
import com.onemenu.server.service.ConfigService;

@Controller
@RequestMapping("/sys/config")
public class ConfigController {
	
	private static final Logger logger = Logger.getLogger(ConfigController.class);
	private static final String SERVER_KEY = "server_key";
	
	@Resource(name="configService")
	private ConfigService configService;
		
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public String  configList(String key,ModelMap modelMap){
		
		if(logger.isDebugEnabled())
			logger.debug("key:"+key);
		
		modelMap.put("sign", key);
		
		if(TextUtils.isEmpty(key)){
			return "config";
		}
			
		
		String value = ConfigCache.getValue(SERVER_KEY,"");
		if(value.equals(key)){
			modelMap.put("list", ConfigCache.getConfigs());
		}else{
			return "config";
		}
		
		return "config";
	}
	
	@RequestMapping(value="/save",method =RequestMethod.POST)
	public String saveOrUpdate(String sign,String key,String value,ModelMap modelMap){
		if(logger.isDebugEnabled())
			logger.debug("key:"+key  + "  value:" +value );
		
		modelMap.put("sign", key);
		
		if(TextUtils.isEmpty(key)||TextUtils.isEmpty(value)){
			modelMap.put("tip", "key or value is null");
		}else{
			if(configService.saveOrUpdate(key,value)){
				modelMap.put("tip", "save or update config data success");
			}else{
				modelMap.put("tip", "save or update config data failed");
			}
		}	
		modelMap.put("list", ConfigCache.getConfigs());
		return "config";
	}
	
	@RequestMapping(value="/delete",method =RequestMethod.POST)
	public String delete(String sign,String key,ModelMap modelMap){
		if(logger.isDebugEnabled())
			logger.debug("key:"+key );
		
		modelMap.put("sign", key);
		
		if(TextUtils.isEmpty(key)){
			modelMap.put("tip", "key  is null");
		}else{
			if(configService.delete(key)){
				modelMap.put("tip", "delete config data success");
			}else{
				modelMap.put("tip", "delete config data failed");
			}
		}	
		modelMap.put("list", ConfigCache.getConfigs());
		return "config";
	}
	
	
	
	@RequestMapping(value="/updateAppConfig",method =RequestMethod.POST)
	public String updateAppConfig(String sign,String appVersion,String appVersionDesc,String forceUpdateVersion,String forceUpdate,String tax,ModelMap modelMap){
		
		modelMap.put("sign", sign);
		
		if(logger.isDebugEnabled())
			logger.debug("appVersion:"+appVersion  );
		
		if(configService.updateAppConfig(appVersion,appVersionDesc,forceUpdateVersion,forceUpdate,tax)){
			modelMap.put("appTips", "update app config data success");
		}else{
			modelMap.put("appTips", "update app config data failed");
		}
		modelMap.put("list", ConfigCache.getConfigs());
		return "config";
	}
	
	
	

}
