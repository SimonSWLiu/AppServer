package com.onemenu.server.javabean;

import java.util.ArrayList;
import java.util.List;

import com.onemenu.server.constant.ParameterConstant;


public class ReviewBean extends MenuItemBean {

    public String reviewId;
    public String summary;
    public String customerId;
    public List<ReviewItemBean> reviewItemArray;

    public String timestamp;
    
    public CustomerBean customer;

    public Double reviewRank;

    public ReviewBean() {

        this.type = ParameterConstant.MENU_ITEM_TYPE_REVIEW;

        this.reviewId = "";
        this.summary = "";
        this.customerId = "";
        this.reviewItemArray = new ArrayList<ReviewItemBean>();
        this.customer = new CustomerBean();

        this.rank = 0.00;
    }

}
