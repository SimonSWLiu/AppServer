package com.onemenu.server.constant;

import java.math.BigDecimal;

/**
 * 保存状态常量
 * 
 * @author Simon
 *
 */
public class ParameterConstant {

	
	public static final String APP_DEV = "dev";
	public static final String APP_USER = "user";
	public static final String APP_DELIVERY ="delivery";
	
    public static final String ORDER_ASC = "ASC";
    public static final String ORDER_DESC = "DESC";
    
    // Account Status
    public static final String ACCOUNT_STATUS_INAVTIVE = "0";
    public static final String ACCOUNT_STATUS_ACTIVE = "1";
    public static final String ACCOUNT_STATUS_ADMIN = "9";

    // Login type
    public static final String LOGIN_TYPE_FACEBOOK = "1"; // facebook账户类型
    public static final String LOGIN_TYPE_TWITTER = "2"; // twitter账户类型
    public static final String LOGIN_TYPE_WECHAT = "3"; // wechat账户类型

    // Dish status
    public static final String DISH_STATUS_DISABLE = "0";
    public static final String DISH_STATUS_ENABLE = "1";

    // Restaurant status
    public static final Integer RESTAURANT_STATUS_DISABLE = 0;
    public static final Integer RESTAURANT_STATUS_ENABLE = 1;
    public static final Integer RESTAURANT_STATUS_PENDING = 2;

    // Menu item types
    public static final String MENU_ITEM_TYPE_ALL = "A";
    public static final String MENU_ITEM_TYPE_DISH = "D";
    public static final String MENU_ITEM_TYPE_REVIEW = "R";

    // Coupon status
    public static final String COUPON_STATUS_DISABLE = "0";
    public static final String COUPON_STATUS_ENABLE = "1";

    // Coupon types
    public static final String COUPON_TYPE_SPECIAL_PRICE = "1";
    public static final String COUPON_TYPE_AMOUNT_DISCOUNT = "2";
    public static final String COUPON_TYPE_PERCENTAGE_DISCOUNT = "3";

    // Coupon target types
    public static final String COUPON_TARGER_TYPE_DISH = "1";
    public static final String COUPON_TARGER_TYPE_ROW_BILL = "2";
    public static final String COUPON_TARGER_TYPE_TRADE = "3";

    // Coupon extra critiria type
    public static final String COUPON_CRI_TYPE_DISHS = "1";
    public static final String COUPON_CRI_TYPE_ROW_BILL = "2";

    // Trade get type
    public static final String TRADE_GET_TYPE_TAKEOUT = "1";
    public static final String DESC_TRADE_GET_TYPE_TAKEOUT = "Take out";
    public static final String TRADE_GET_TYPE_DELIVERY = "2";
    public static final String DESC_TRADE_GET_TYPE_DELIVERY = "Delivery";

    // Trader payment type
    public static final String DESC_TRADE_PAYMENT_TYPE_OTHERS = "Others";
    public static final String TRADE_PAYMENT_TYPE_CREDIT_CARD = "1";
    public static final String DESC_TRADE_PAYMENT_TYPE_CREDIT_CARD = "Credit Card Prepaid";
    public static final String TRADE_PAYMENT_TYPE_PAYPAL = "2";
    public static final String DESC_TRADE_PAYMENT_TYPE_PAYPAL = "Paypal Prepaid";
    public static final String TRADE_PAYMENT_TYPE_CASH = "3";
    public static final String DESC_TRADE_PAYMENT_TYPE_CASH = "Cash Unpaid";

    // Order Form Decimal
    public static final BigDecimal ORDER_FORM_PAYMENT_GATWAY_CHARGE = new BigDecimal(0.029);
    public static final BigDecimal ORDER_FORM_PAYMENT_GATWAY_CHARGE_PER_ORDER =
            new BigDecimal(0.30);

    // Order Form status
    public static final String ORDER_FORM_STATUS_CANCEL = "0";
    public static final String ORDER_FORM_STATUS_FINISHED = "1"; // user get the order
    public static final String ORDER_FORM_STATUS_RESTAURANT_PENDING = "2"; // after User order, this order is pending to restaurant to caonfirm or cancel
    public static final String ORDER_FORM_STATUS_RESTAURANT_CONFIRMED = "3"; // after restaurant confirmed
    public static final String ORDER_FORM_STATUS_CALL_DELIVERY = "4"; // after restaurant call delivery service, waiting for delivering to driver
    public static final String ORDER_FORM_STATUS_DRIVER_PENDING = "5"; // after the order is delivered to driver, waiting for driver's confirmation or cancel
    public static final String ORDER_FORM_STATUS_DELIVERY_CONFIRMED = "6"; // after driver pick out the order
    public static final String ORDER_FORM_STATUS_PICKED_UP = "7"; //  driver picked up the order
    public static final String ORDER_FORM_STATUS_RESTAURANT_CANCEL = "8"; // after restaurant CANCEL
    public static final String ORDER_FORM_STATUS_RESTAURANT_CANCEL_AFTER_PICK_UP = "9"; // after restaurant CANCEL
    
    // Order Form Is From One Menu
    public static final int ORDER_FORM_IS_ONE_MENU = 1;
    public static final int ORDER_FORM_IS_NOT_ONE_MENU = 0;

    // Order Form Is From One Menu
    public static final int DRIVER_STATUS_RESIGNED = -1;
    public static final int DRIVER_STATUS_UNAVAILIABLE = 0;
    public static final int DRIVER_STATUS_AVAILIABLE = 1;
    public static final int DRIVER_STATUS_PENDING = 2;
    public static final int DRIVER_STATUS_ON_DELIVERY = 3;
    
    
    public static final String ORDER_FORM_TIPS_TYPE_CASH = "1";
    public static final String ORDER_FORM_TIPS_TYPE_NONCASH = "2";
    
    
    public static final String DRIVER_ONDUTY_YES = "1";
    public static final String DRIVER_ONDUTY_NO = "0";
    //Device
    public static final Integer DEVICE_IOS = 1;
    public static final Integer DEVICE_ANDROID = 2;
    //login or logout
    public static final Integer LOGIN = 1;
    public static final Integer LOGOUT = 2;
    
    //user type
    public static final Integer LOGIN_USER = 1;
    public static final Integer LOGIN_DRIVER = 2;
    public static final Integer LOGIN_MERCHANT = 3;
    

    // Common Status Code
    public static final String STATUS_CODE_ERROR = "0";
    public static final String MSG_OPERATION_ERROR = "Operation error.";

    public static final String STATUS_CODE_OPERATION_SUCCESS = "1";
    public static final String MSG_OPERATION_SUCCESS = "Operation success.";

    public static final String STATUS_CODE_DATA_EMPTY = "100001";
    public static final String MSG_DATA_EMPTY = "No data.";

    public static final String STATUS_CODE_DATA_NOT_EXIST = "100002";
    public static final String MSG_DATA_NOT_EXIST = "data is not exist.";
    
    
    public static class ConfigKey{
    	public static final String SERVER_KEY = "server_key";
    	//gmailAdderss
    	public static final String GMAIL_USER_NAME ="gmailAddress";
    	//gmail password
    	public static final String GMAIL_PASSWORD = "gmailPassword";
    	//是否发送fax
    	public static final String IS_SEND_FAX = "sendFax";
    	//OrderFormCode
    	public static final String ORDER_FORM_CODE = "orderFormCode";
    	//certificate
    	public static final String IOS_CERTIFICATE ="certificate";
    	//privateKey
    	public static final String IOS_PRIVATE_KEY = "privateKey";
    	//applicationName
    	public static final String APPLICATION_NAME = "applicationName";
    	//is sand box
    	public static final String IS_SAND_BOX = "isSandBox";
    	
    }
}
