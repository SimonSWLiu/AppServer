package com.onemenu.server.javabean;

import java.util.ArrayList;
import java.util.List;

public class RestaurantBean {
    
    public String restaurantId;
    public String restaurantName;
    public String restaurantLogoUrl;
    public String freeDeliveryLimit;
    public String deliveryDistance;
    public String minDeliveryTotal;
    public String deliveryFee;
    public String openingStartTime;
    public String openingEndTime;
    public String restaurantStatus;
    
    public List dishImageArray; // 最多3张图片
    public List<RestaurantDeliveryTimeBean> deliveryTimeArray;
    
    public String distance;
    
    public String isOutOfRange ="1";
    //是否在营业时间内  0-》是，1-》否
    public String isOutOfBusinessHour="0";
    //是否在送餐时间内  0-》是，1-》否
    public String isOutOfDeliveryTime="0";
    
    //Sort
    public Double distanceOrder;
    
    public RestaurantBean() {
        
        this.restaurantId = "";
        this.restaurantName = "";
        this.restaurantLogoUrl = "";
        this.freeDeliveryLimit = "0.00";
        this.deliveryFee = "0.00";
        this.openingStartTime = "";
        this.openingEndTime = "";
        this.restaurantStatus = "";
        
        this.dishImageArray = new ArrayList(); // 最多3张图片
        this.deliveryTimeArray = new ArrayList();
        
        this.distance = "";
    }
    
}
