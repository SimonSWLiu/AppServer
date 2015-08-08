package com.onemenu.server.entity;

import java.sql.Timestamp;

import com.onemenu.server.javabean.CustomerBean;
import com.onemenu.server.model.Account;

public class LoginTokenObj {

    public String thirdPartyToken;
    public String thirdPartyTokenContent;
    public Timestamp loginTimestamp;
    public Account account;
}
