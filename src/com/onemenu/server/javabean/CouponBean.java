package com.onemenu.server.javabean;

import java.util.ArrayList;
import java.util.List;

public class CouponBean {
    
    public String couponId;
    public String couponImageUrl;
    public String couponQuantity;
    public String couponDesc;
    public String maturityDate;
    public String type;// Special price:1,Discount price:2,Discount percentage:3
    public String typeAmount;
    public String targetType;
    public List<DishBean> targetDishArray;
    public String extraCriType;
    public List<DishBean> extraCriDishArray;
    public String extraCriAmount;
    public String availableStartTime;
    public String availableEndTime;
    public String weeks;
    
    public RestaurantBean restaurant;
    
    public Double rank;
    
    public CouponBean() {
        
        this.couponId = "";
        this.couponImageUrl = "";
        this.couponQuantity = "";
        this.couponDesc = "";
        this.maturityDate = "";
        this.type = "";
        this.typeAmount = "";
        this.targetType = "";
        this.targetDishArray = new ArrayList<DishBean>();
        this.extraCriType = "";
        this.extraCriDishArray = new ArrayList<DishBean>();
        this.extraCriAmount = "";
        this.availableStartTime = "";
        this.availableEndTime = "";
        this.weeks = "";
        
        this.restaurant = new RestaurantBean();
        
        this.rank = 0.00;
    }
}
