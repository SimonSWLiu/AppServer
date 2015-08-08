package com.onemenu.server.dao;

import java.util.List;

import com.onemenu.server.model.DishCategory;

public interface DishCategoryDAO extends BaseDAOInf {

    public List<DishCategory> getDishCategoryListByRestaurantId(long restaurantId)  throws RuntimeException;
    
}
