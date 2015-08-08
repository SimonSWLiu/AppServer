package com.onemenu.server.daoImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.onemenu.server.dao.DishRankDAO;
import com.onemenu.server.model.Dish;
import com.onemenu.server.model.DishRank;


public class DishRankDAOImpl extends BaseDAOSupport implements DishRankDAO {
    protected HibernateTemplate mHibernateTemplate;

    @Autowired
    public void setmHibernateTemplate(HibernateTemplate mHibernateTemplate) {
        this.mHibernateTemplate = mHibernateTemplate;
    }

    @Override
    @Transactional
    public void addPageView(long dishId) throws RuntimeException {

        Dish dish = mHibernateTemplate.get(Dish.class, dishId);
        if (dish != null) {
            DishRank dishRank = dish.getmDishRank();
            if (dishRank == null) {
                dishRank = new DishRank();
                dishRank.setmPageView(1);
                dish.setmDishRank(dishRank);
                mHibernateTemplate.save(dish);
            } else {
                long pageView = dishRank.getmPageView();
                pageView++;
                dishRank.setmPageView(pageView);
                mHibernateTemplate.update(dishRank);
            }
        }
    }
}
