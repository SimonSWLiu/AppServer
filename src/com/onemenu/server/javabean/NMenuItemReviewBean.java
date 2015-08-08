package com.onemenu.server.javabean;

import java.math.BigInteger;
import java.util.List;
/**
 * 按评论排序响应消息内容
 * @author file
 * @since 2015年4月11日 下午6:30:50
 * @version 1.0
 * @copyright  by onemenu
 */
public class NMenuItemReviewBean extends NMenuItemBean {
	//评论id
	public String reviewId;
	//评论内容
	public String summary;
	//菜式的图片url组合
	public List<NMenuItemReviewItemBean> reviewItemArray;
	//顾客图像url
	public String avatarUrl;
	//顾客名字
	public String customerName;
	//顾客公司
	public String customerCompany;
	//顾客的行业
	public String customerProfession;
	
	public NMenuItemReviewCustomerBean customer;
	
	
	public void setReviewId(BigInteger reviewId) {
		this.reviewId = String.valueOf(reviewId);
	}


	@Override
	public String toString() {
		return "NMenuItemReviewBean [reviewId=" + reviewId + ", summary=" + summary + ", reviewItemArray=" + reviewItemArray + ", avatarUrl=" + avatarUrl
				+ ", customerName=" + customerName + ", customerCompany=" + customerCompany + ", customerProfession=" + customerProfession + ", customer="
				+ customer + "]";
	}
}
