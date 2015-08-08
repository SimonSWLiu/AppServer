package com.onemenu.server.service;

import java.util.List;

import com.onemenu.server.javabean.DishCategoryBean;
import com.onemenu.server.javabean.NDishCategoryBean;

public interface DishCategoryService extends AbstractServiceInf {

    public List<DishCategoryBean> getDishCategoryListByRestaurantId(long restaurantId);

	public List<NDishCategoryBean> getDishCategorysByRestaurantId(long restaurantId);
}
