package com.onemenu.server.daoImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.onemenu.server.dao.DishCategoryDAO;
import com.onemenu.server.model.DishCategory;
import com.onemenu.server.model.Restaurant;


public class DishCategoryDAOImpl extends BaseDAOSupport implements DishCategoryDAO {
    protected HibernateTemplate mHibernateTemplate;

    @Autowired
    public void setmHibernateTemplate(HibernateTemplate mHibernateTemplate) {
        this.mHibernateTemplate = mHibernateTemplate;
    }

    @Override
    @Transactional
    public List<DishCategory> getDishCategoryListByRestaurantId(long restaurantId) {

        List<DishCategory> dishCategorieList = new ArrayList<DishCategory>();
        
        Restaurant restaurant = mHibernateTemplate.get(Restaurant.class,restaurantId);
        
        for (DishCategory dishCategory : restaurant.getmDishCategorySet()) {
            
            dishCategorieList.add(dishCategory);
        }

        return dishCategorieList;
    }
    
}
