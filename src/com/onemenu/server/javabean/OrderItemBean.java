package com.onemenu.server.javabean;

import java.util.ArrayList;
import java.util.List;

import com.onemenu.server.constant.ParameterConstant;

public class OrderItemBean extends MenuItemBean {
    
    public String dishId;
    public String dishName;
    public String dishImageUrl;
    public String dishAmount;
    public String dishPrice;
    public String orderItemPrice; //dishPrice*dishaAmount
    public List<IngredientBean> orderItemIngredientArray;
    
    
    public String timestamp;
    
    public OrderFormBean orderForm;

    public OrderItemBean(){
        
        this.type = ParameterConstant.MENU_ITEM_TYPE_DISH;
        
        this.dishId = "";
        this.dishName = "";
        this.dishImageUrl = "";
        this.dishAmount = "";
        this.dishPrice = "";
        this.orderItemPrice = ""; //dishPrice*dishaAmount
        this.orderItemIngredientArray = new ArrayList<IngredientBean>();
        
        this.timestamp = "";
        
//        this.orderForm = new OrderFormBean();
    }
}