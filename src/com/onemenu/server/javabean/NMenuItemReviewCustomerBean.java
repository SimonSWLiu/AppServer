package com.onemenu.server.javabean;

/**
 * 为了协议加一层
 * @author file
 * @since 2015年4月11日 下午9:08:48
 * @version 1.0
 * @copyright  by onemenu
 */
public class NMenuItemReviewCustomerBean {
	
	public  String customerId;
	private String avatarUrl;
	private String customerName;
	private String customerCompany;
	private String customerProfession;
	
	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl==null?"":avatarUrl;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName==null?"":customerName;
	}

	public String getCustomerCompany() {
		return customerCompany;
	}

	public void setCustomerCompany(String customerCompany) {
		this.customerCompany = customerCompany==null?"":customerCompany;
	}

	public String getCustomerProfession() {
		return customerProfession;
	}

	public void setCustomerProfession(String customerProfession) {
		this.customerProfession = customerProfession==null?"":customerProfession;
	}

	@Override
	public String toString() {
		return "NMenuItemReviewCustomerBean [avatarUrl=" + avatarUrl + ", customerName=" + customerName + ", customerCompany=" + customerCompany
				+ ", customerProfession=" + customerProfession + "]";
	}

}
