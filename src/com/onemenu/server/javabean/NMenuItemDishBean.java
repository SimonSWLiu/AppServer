package com.onemenu.server.javabean;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.onemenu.server.util.FormatUtils;

/**
 * 菜式排序响应消息内容
 * @author file
 * @since 2015年4月11日 下午6:27:51
 * @version 1.0
 * @copyright  by onemenu
 */
public class NMenuItemDishBean extends NMenuItemBean {
	
	//菜式id
	public String dishId;
	
	//菜式名称
	public String dishName;
	//菜式价格
	public String dishPrice;
	//菜式图片url
	public String dishImageUrl;
	
	public void setDishId(BigInteger dishId) {
		this.dishId = String.valueOf(dishId);
	}

	public void setDishPrice(BigDecimal dishPrice) {
		this.dishPrice = FormatUtils.formatPrice(dishPrice);
	}
	
	@Override
	public String toString() {
		return "NMenuItemDishBean [dishId=" + dishId + ", dishName=" + dishName + ", dishPrice=" + dishPrice + ", dishImageUrl=" + dishImageUrl + "]";
	}

}
