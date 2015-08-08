package com.onemenu.server.dao;

import java.util.List;

import com.onemenu.server.javabean.MenuSearchConditionBean;
import com.onemenu.server.javabean.NMenuItemReviewBean;
import com.onemenu.server.model.Review;


public interface ReviewDAO extends BaseDAOInf {

    public void addPageView(long reviewId) throws RuntimeException;

    public List<Review> getReviewListByCondition(MenuSearchConditionBean condition)
            throws RuntimeException;

    public Review findByReviewId(long reviewId) throws RuntimeException;

    public List<Review> findByCustomerId(long customerId) throws RuntimeException;
    
    public List<NMenuItemReviewBean> getReviewsByCondition(MenuSearchConditionBean condition);

	public NMenuItemReviewBean getReviewMenuItemById(String id);

}
