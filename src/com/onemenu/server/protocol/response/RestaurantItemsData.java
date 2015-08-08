package com.onemenu.server.protocol.response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.onemenu.server.javabean.NRestaurantItemBean;

/**
 * 餐馆item响应信息
 * @author file
 * @since 2015年5月3日 下午5:35:09
 * @version 1.0
 * @copyright  by onemenu
 */
public class RestaurantItemsData extends BaseData {
	
	public Map<String,List<NRestaurantItemBean>>  data = new HashMap<String, List<NRestaurantItemBean>>(1);

}
