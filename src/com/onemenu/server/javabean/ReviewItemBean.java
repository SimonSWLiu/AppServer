package com.onemenu.server.javabean;


public class ReviewItemBean {
    
    public String reviewItemId;
    public String comment;
    public String dishId;
    public String dishName;
    public String dishImageUrl;
    
    //used for upload review
    public String base64Str;
    public String width;
    public String height;
    
    public ReviewItemBean() {

        this.reviewItemId = "";
        this.comment = "";
        this.dishId = "";
        this.dishName = "";
        this.dishImageUrl = "";
        
        this.base64Str = "";
        this.width = "";
        this.height = "";
    }
}