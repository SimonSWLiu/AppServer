package com.onemenu.server.protocol.response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 基础列表响应信息
 * @author file
 * @since 2015-5-11 下午4:54:32
 * @version 1.0
 */
public class ListBaseData<T> extends BaseData {
	
	public Map<String,List<T>>  data = new HashMap<String, List<T>>(1);

}
