package com.onemenu.server.daoImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.onemenu.server.dao.RestaurantRankDAO;
import com.onemenu.server.model.Dish;
import com.onemenu.server.model.DishRank;
import com.onemenu.server.model.Restaurant;
import com.onemenu.server.model.RestaurantRank;


public class RestaurantRankDAOImpl extends BaseDAOSupport implements RestaurantRankDAO {
    protected HibernateTemplate mHibernateTemplate;
    
    @Autowired
    public void setmHibernateTemplate(HibernateTemplate mHibernateTemplate) {
        this.mHibernateTemplate = mHibernateTemplate;
    }
    
    @Override
    @Transactional
    public void addPageView(long restaurantId)  throws RuntimeException {
        
        Restaurant restaurant = mHibernateTemplate.get(Restaurant.class, restaurantId);
        if (restaurant != null) {
            RestaurantRank restaurantRank = restaurant.getmRestaurantRank();
            if (restaurantRank == null) {
                restaurantRank = new RestaurantRank();
                restaurantRank.setmPageView(1);
                restaurant.setmRestaurantRank(restaurantRank);
                mHibernateTemplate.save(restaurantRank);
            } else {
                long pageView = restaurantRank.getmPageView();
                pageView++;
                restaurantRank.setmPageView(pageView);
                mHibernateTemplate.update(restaurantRank);
            }
        }
        
    }
}
