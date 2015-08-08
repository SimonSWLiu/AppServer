package com.onemenu.server.javabean;

import java.util.List;

/**
 * 返回餐馆数据
 * @author file
 * @since 2015年5月3日 下午5:24:19
 * @version 1.0
 * @copyright  by onemenu
 */
public class NRestaurantItemBean {
	//餐馆id
	public String restaurantId;
	//餐馆logo
	public String restaurantLogoUrl;
	//餐馆名字
	public String restaurantName;
	//距离
	public String distance;
	//dish图片
	public List<String> dishImageArray;
	
	public void setRestaurantName(String restaurantName) {
		this.restaurantName = restaurantName==null?"":restaurantName;
	}
	
	
	

}
