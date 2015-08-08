package com.onemenu.server.protocol.response;

import java.util.HashMap;
import java.util.Map;

/**
 * 基础对象响应信息
 * @author file
 * @since 2015-5-11 下午5:07:34
 * @version 1.0
 */
public class ObjectBaseData<T> extends BaseData {
	public Map<String,T>  data = new HashMap<String,T>(1);
}
