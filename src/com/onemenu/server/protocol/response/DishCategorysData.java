package com.onemenu.server.protocol.response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.onemenu.server.javabean.NDishCategoryBean;

/**
 * 餐馆菜式分类返回信息
 * @author file
 * @since 2015年5月10日 下午12:20:13
 * @version 1.0
 * @copyright  by onemenu
 */
public class DishCategorysData extends BaseData {
	
	public Map<String,List<NDishCategoryBean>>  data = new HashMap<String, List<NDishCategoryBean>>(1);
}
