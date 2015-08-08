package com.onemenu.server.javabean;

import java.util.ArrayList;
import java.util.List;

public class OrderFormBean {
    
    public String orderFormId;//-
    public String customerRemark;
    public String orderFormCouponId;
    public String orderFormCouponDesc;
    public List<OrderItemBean> orderItemArray;
    public String taxFee;
    public String tipsFee; //-1 : cash
    public String deliveryFee;
    public String discountFee;
    public String orderFormSubtotal;
    public String orderFormTotal;
    public String orderFormPrice;
    
    public String orderFormCode;
    
    public String status;//-
    
    public RestaurantBean restaurant;
    
    public OrderFormBean() {
        
        this.orderFormId = "";//-
        this.customerRemark = "";
        this.orderFormCouponId = "";
        this.orderFormCouponDesc = "";
        this.orderItemArray = new ArrayList<OrderItemBean>();
        this.taxFee = "";
        this.tipsFee = ""; //-1 : cash
        this.deliveryFee = "";
        this.discountFee = "";
        this.orderFormSubtotal = "";
        this.orderFormTotal = "";
        this.orderFormPrice = "";
        
        this.status = "";//-
        
        this.restaurant = new RestaurantBean();
    }
    
}
