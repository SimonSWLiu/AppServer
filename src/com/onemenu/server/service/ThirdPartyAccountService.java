package com.onemenu.server.service;

import com.onemenu.server.model.ThirdPartyAccount;


public interface ThirdPartyAccountService extends AbstractServiceInf {

    public boolean isSignUp(String thirdPartyAccountId, String loginType);
    
    public ThirdPartyAccount login(String thirdPartyAccountId, String loginType);
}
