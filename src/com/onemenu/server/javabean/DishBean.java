package com.onemenu.server.javabean;

import java.util.ArrayList;
import java.util.List;

import com.onemenu.server.constant.ParameterConstant;

public class DishBean extends MenuItemBean {

    public String dishId;
    public String dishName;
    public String dishPrice;
    public String desc; // 描述
    public String dishImageUrl;
    public List customization;
    public String dishStatus;
    
    public RestaurantBean restaurant;

    //
    public int pageView;
    public int purchaseVolume;

    public Double dishRank;


    public DishBean() {

        this.type = ParameterConstant.MENU_ITEM_TYPE_DISH;
        
        this.dishId = "";
        this.dishName = "";
        this.dishPrice = "";
        this.desc = ""; // 描述
        this.dishImageUrl = "";
        this.customization = new ArrayList();
        this.dishStatus = "";
        
        this.restaurant = new RestaurantBean();

        //
        this.pageView = 0;
        this.purchaseVolume = 0;

        this.dishRank = 0.00;
    }
}
