package com.onemenu.server.javabean;
/**
 * 为协议，无实际作用
 * @author file
 * @since 2015年4月11日 下午9:11:01
 * @version 1.0
 * @copyright  by onemenu
 */
public class NMenuItemReviewItemBean {
	//菜式图像url
	public String dishImageUrl;
	public String dishId;
	public String dishName;
	public String comment;
	public String dishPrice;
	

	public NMenuItemReviewItemBean(String dishImageUrl) {
		super();
		this.dishImageUrl = dishImageUrl;
	}
	
	

	public NMenuItemReviewItemBean(String dishImageUrl, String dishId, String dishName, String comment) {
		super();
		this.dishImageUrl = dishImageUrl;
		this.dishId = dishId;
		this.dishName = dishName;
		this.comment = comment;
	}



	public NMenuItemReviewItemBean() {
		super();
	}

	@Override
	public String toString() {
		return "NMenuItemReviewItemBean [dishImageUrl=" + dishImageUrl + "]";
	}
	

}
