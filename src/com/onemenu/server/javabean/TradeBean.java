package com.onemenu.server.javabean;

import java.util.ArrayList;
import java.util.List;

public class TradeBean {
    
    public String tradeId;
    public String tradeCode;
    public List<OrderFormBean> orderFormArray;
    public String tradeGetType; //1:take out , 2:delivery
    public String tradePaymentType; //1:credit card, 2:paypel, 3:cash
    //
    public String tradeCouponId;
    public String tradeCouponDesc;
    public String tradePrice;
    public String tradeCustomerName;
    public String tradeCustomerPhone;
    public String tradeCustomerAddress;
    public String tradeCustomerApt;
    public String tradeCustomerStreet;
    public String tradeCustomerCity;
    public String tradeCustomerZipCode;
    
    public CustomerBean customer;
    
    public TradeBean() {
        
        this.tradeId = "";
        this.tradeCode = "";
        this.orderFormArray = new ArrayList<OrderFormBean>();
        this.tradePaymentType = ""; //1:credit card, 2:paypel, 3:cash
        //
        this.tradeCouponId = "";
        this.tradeCouponDesc = "";
        this.tradePrice = "";
        this.tradeCustomerName = "";
        this.tradeCustomerPhone = "";
        this.tradeCustomerAddress = "";
        this.tradeCustomerApt = "";
        this.tradeCustomerStreet = "";
        this.tradeCustomerCity = "";
        this.tradeCustomerZipCode = "";
        
        this.customer = new CustomerBean();
    }
    
}
