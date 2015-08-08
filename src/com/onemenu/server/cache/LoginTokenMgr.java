package com.onemenu.server.cache;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSON;
import com.onemenu.server.entity.LoginTokenObj;
import com.onemenu.server.model.Account;
import com.onemenu.server.model.LoginToken;
import com.onemenu.server.service.AccountService;

public class LoginTokenMgr {
	
	public static void startUp(AccountService accountService){
				
		List<LoginToken> tokens = accountService.findAll(LoginToken.class);
		
		for (LoginToken loginToken : tokens) {
			String tokenId = loginToken.getTokenId();
			@SuppressWarnings("unchecked")
			Map<String,Object> tokenContent = JSON.parseObject(loginToken.getContent(), Map.class);
			long accountId =  Long.valueOf(tokenContent.get("accountId").toString());
			long loginTime = Long.valueOf(tokenContent.get("loginTime").toString());
			String thirdPartyToken = (String) tokenContent.get("thirdPartyToken");
			String  thirdPartyTokenContent = (String) tokenContent.get("thirdPartyTokenContent");
			
			LoginTokenObj obj = new LoginTokenObj();
			obj.account = accountService.findById(Account.class, accountId);
			if(obj.account==null)
				continue;
			obj.loginTimestamp = new Timestamp(loginTime);
			obj.thirdPartyToken = thirdPartyToken;
			obj.thirdPartyTokenContent = thirdPartyTokenContent;
			
			LoginTokenCache.loginTokenMap.put(tokenId, obj);
		}
		
	}
	
	
	public static void shutdown(AccountService accountService){
		
		//停止服务器时，先清空token数据
		accountService.deleteAllToken();
		
		
		Map<String, LoginTokenObj> tokens =  LoginTokenCache.loginTokenMap;
		
		Map<String,Object>  tokenContent = new HashMap<String,Object>(4);
		
		
		for(Entry<String, LoginTokenObj> token:tokens.entrySet()){
			
			LoginToken loginToken = new LoginToken();
			LoginTokenObj obj = token.getValue();
			
			loginToken.setTokenId(token.getKey());
			
			tokenContent.put("accountId", obj.account.getmId());
			tokenContent.put("loginTime", obj.loginTimestamp.getTime());
			tokenContent.put("thirdPartyToken", obj.thirdPartyToken);
			tokenContent.put("thirdPartyTokenContent", obj.thirdPartyTokenContent);
			
			loginToken.setContent(JSON.toJSONString(tokenContent));
			
			accountService.saveOrUpdate(loginToken);
			
		}
		
		
		
		
		
	}
	
	
	
	
	
	

}
