package com.onemenu.server.dao;

import java.util.List;

import com.onemenu.server.javabean.RestaurantSearchConditionBean;
import com.onemenu.server.model.Restaurant;


public interface RestaurantDAO extends BaseDAOInf {

    public List<Restaurant> getRestaurantListByConditon(RestaurantSearchConditionBean condition);
}
