package com.onemenu.server.dao;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.onemenu.server.javabean.DishValidationConditionBean;
import com.onemenu.server.javabean.MenuSearchConditionBean;
import com.onemenu.server.javabean.NMenuItemBean;
import com.onemenu.server.javabean.NMenuItemDishBean;
import com.onemenu.server.model.Dish;


public interface DishDAO extends BaseDAOInf {

    public List<Dish> getDishListByRestaturantId(long restaurantId);
    
    public List<Dish> validateDish(DishValidationConditionBean condition, List<Long> dishIdList) throws RuntimeException, ParseException;

    public List<Dish> getDishMenuItemListByConditon(MenuSearchConditionBean condition);
    
    public List<NMenuItemDishBean> getDishMenuItemsByConditon(MenuSearchConditionBean condition);

	public Map<String, String> getDishAndReviewsMenuItemsByConditon(MenuSearchConditionBean condition);

	public NMenuItemDishBean getDishMenuItemById(String id);
	
	public void fixedData();
}
