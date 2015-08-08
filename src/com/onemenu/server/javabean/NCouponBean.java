package com.onemenu.server.javabean;

import java.util.List;

public final class NCouponBean {
	
	public String couponId;
	public String couponImageUrl;
	public String couponDesc;
	public String maturityDate;
	public String availableStartTime;
	public String availableEndTime;
	public String weeks;
	public String type;
	public String typeAmount;
	public String targetType;
	public List<NMenuItemDishBean> targetDishArray;
	public String extraCriType;
	public List<NMenuItemDishBean> extraCriDishArray;
	public String extraCriAmount;
	public NRestaurantItemBean restaurant;
	

}
