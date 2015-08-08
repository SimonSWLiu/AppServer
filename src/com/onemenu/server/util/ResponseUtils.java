package com.onemenu.server.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Simon
 *
 */
public class ResponseUtils {

    private static final String STATUS = "status";
    private static final String MSG = "msg";
    private static final String DATA = "data";

//    public static final Map<String, Object> EMPTY_DATA = new HashMap<String, Object>();
//    public static final List<Object> EMPTY_LIST = new ArrayList<Object>();

    public static Map<String, Object> setStatusJSON(String statusCode, String msg,
            Map<String, Object> data) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(STATUS, statusCode);
        map.put(MSG, msg);
        map.put(DATA, data);
        return map;
    }
    
    
}
