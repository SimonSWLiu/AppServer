package com.onemenu.server.protocol.response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.onemenu.server.javabean.NMenuItemBean;
/**
 * menu item 返回信息
 * @author file
 * @since 2015年5月3日 下午5:35:24
 * @version 1.0
 * @copyright  by onemenu
 */
public class MenuItemsData extends BaseData {
	
	public Map<String,List<NMenuItemBean>>  data = new HashMap<String, List<NMenuItemBean>>(1);
	

}
