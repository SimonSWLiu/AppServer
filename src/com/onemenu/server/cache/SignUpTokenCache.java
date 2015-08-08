package com.onemenu.server.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.onemenu.server.entity.SignUpTokenObj;
import com.onemenu.server.util.MD5Utils;


/**
 * 保存状态常量
 * 
 * @author Simon
 *
 */
public class SignUpTokenCache {

    public static Map<String, SignUpTokenObj> signUpTokenMap =
            new ConcurrentHashMap<String, SignUpTokenObj>();

    public static String generateSignUpToken(long accountId) {

        return System.currentTimeMillis() + MD5Utils.encode(String.valueOf(accountId))
                + MD5Utils.encode(String.valueOf(System.currentTimeMillis()));

    }

}
