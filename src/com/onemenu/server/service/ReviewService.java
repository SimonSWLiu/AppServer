package com.onemenu.server.service;

import java.util.List;

import com.onemenu.server.javabean.MenuSearchConditionBean;
import com.onemenu.server.javabean.NMenuItemBean;
import com.onemenu.server.javabean.ReviewBean;
import com.onemenu.server.model.Review;

/**
 * 
 * @author lin
 *
 */
public interface ReviewService extends AbstractServiceInf {

    public List<ReviewBean> listReviewMenuItemByCondition(MenuSearchConditionBean condition);
    
    public List<NMenuItemBean> menuItemReviewsByCondition(MenuSearchConditionBean condition);

    public List<ReviewBean> listReviewBeanByCustomerId(long customerId);
    
    public ReviewBean getReviewBean(long id);
    
    public Review saveReviewBean(ReviewBean reviewBean);
    
    public void addPageView(long reviewId);
    
}
