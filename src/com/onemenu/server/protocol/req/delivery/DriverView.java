package com.onemenu.server.protocol.req.delivery;

/**
 * driver 注册项
 * @author file
 * @since 2015-6-11 下午5:11:02
 * @version 1.0
 */
public class DriverView extends BaseDeliveryReqData {
	//姓名
	public String name;
	//电话
	public String phone;
	//地址
	public String address;
	//email
	public String email;
	//密码
	public String password;
	//账号
	public String account;

	public void setName(String name) {
		this.name = name;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setAccount(String account) {
		this.account = account;
	}

}
