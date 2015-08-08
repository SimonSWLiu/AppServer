package com.onemenu.server.protocol;
/**
 * 错误码
 * @author file
 * @since 2015年4月26日 下午8:44:46
 * @version 1.0
 * @copyright  by onemenu
 */
public enum ErrorCode {
	RequestSuccess("1","Operation success."),
	RequestFailed("0","Operation error."),
	//公共错误码 100000 +
    NoData("100001","No data."),
    DataNoExist("100002","data is not exist."),
    ParamError("100003","param %s is error "),
    //OneMenu 错误码 200000+
    //Restaurant 错误码  300000+
    //ShoppingCart 错误码400000+
    //AboutMe      错误码 500000+
    CouponsHaveReceived("510001","Coupon already in your chest."),
    CouponsNotLocation("510002","Cannot get coupon info near by you due to location issues."),
	;
	
	private String  code;
	private String  msg;
	
	private ErrorCode(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	
	public String getCode(){
		return code;
	}
	
	public String getMsg(){
		return msg;
	}
	
	public String getMsgForName(Object ... params){
		return String.format(msg, params);
	}
	

}
