package com.onemenu.server.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.onemenu.server.entity.LoginTokenObj;
import com.onemenu.server.util.MD5Utils;


/**
 * 保存状态常量
 * 
 * @author Simon
 *
 */
public class LoginTokenCache {

    public static Map<String, LoginTokenObj> loginTokenMap =
            new ConcurrentHashMap<String, LoginTokenObj>();

    public static String generateLoginToken(long accountId) {

        return System.currentTimeMillis() + "-" + String.valueOf(accountId) + "-"
                + MD5Utils.encode(String.valueOf(System.currentTimeMillis()));
    }
}
