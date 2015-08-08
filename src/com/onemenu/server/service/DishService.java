package com.onemenu.server.service;

import java.text.ParseException;
import java.util.List;

import com.onemenu.server.javabean.DishBean;
import com.onemenu.server.javabean.DishValidationConditionBean;
import com.onemenu.server.javabean.MenuSearchConditionBean;
import com.onemenu.server.javabean.NMenuItemBean;
import com.onemenu.server.javabean.NMenuItemDishBean;
import com.onemenu.server.model.Dish;

public interface DishService extends AbstractServiceInf {

    public void addPageView(long dishId);
    
    public List<DishBean> validateDish(DishValidationConditionBean condition, List<Long> dishIdList)  throws ParseException;

    public List<DishBean> listDishItemByCondition(MenuSearchConditionBean condition);

    public DishBean getDishBeanById(MenuSearchConditionBean condition, long id);
    
    public List<DishBean> listDishMenuItemByCondition(MenuSearchConditionBean condition);

    public String getDishImageUrl(Dish dish);

	public List<NMenuItemBean> menuItemDishsByCondition(MenuSearchConditionBean condition);

	public List<NMenuItemBean> menuItemDishAndReviewsByCondition(MenuSearchConditionBean condition);
	
	public List<NMenuItemDishBean> validateDishs(DishValidationConditionBean condition, List<Long> dishIdList) throws Exception;
    
}
