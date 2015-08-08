package com.onemenu.server.util;

import java.security.MessageDigest;

import org.apache.log4j.Logger;

/**
 * <br>
 * 类描述: MD5加密 <br>
 * 功能详细描述:
 * 
 * @author linhang
 * @date [2012-9-19]
 */
public class MD5Utils {
	private static final Logger logger = Logger.getLogger(MD5Utils.class);
	
    public final static String encode(String s) {
        char hexDigits[] =
                {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            logger.error("", e);
            return null;
        }
    }

    /**
     * 用于生成hashcode前缀
     * 
     * @param name
     * @return
     */
    public static String getHashCodePrefix(String name) {
        return encode(name).substring(0, 4) + "-";
    }

}
