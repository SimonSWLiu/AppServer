package com.onemenu.server.javabean;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public class OrderFormQueryCondition {

    private Long restaurantId;
    private Long driverId;
    private String status;
    private Date fromTimestamp;
    private Date toTimestamp;
    private Integer isOneMenu;
    //支持查多个状态
    public  Collection<String> _status;
    //支持分页
    public  Integer startNum;
    public  Integer range;
    

    private List<OrderSequenceCondition> orderSequenceCondition;

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getFromTimestamp() {
        return fromTimestamp;
    }

    public void setFromTimestamp(Date fromTimestamp) {
        this.fromTimestamp = fromTimestamp;
    }

    public Date getToTimestamp() {
        return toTimestamp;
    }

    public void setToTimestamp(Date toTimestamp) {
        this.toTimestamp = toTimestamp;
    }

    public Integer getIsOneMenu() {
        return isOneMenu;
    }

    public void setIsOneMenu(Integer isOneMenu) {
        this.isOneMenu = isOneMenu;
    }

    public List<OrderSequenceCondition> getOrderSequenceCondition() {
        return orderSequenceCondition;
    }

    public void setOrderSequenceCondition(List<OrderSequenceCondition> orderSequenceCondition) {
        this.orderSequenceCondition = orderSequenceCondition;
    }


}
