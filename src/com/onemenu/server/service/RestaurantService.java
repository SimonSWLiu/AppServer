package com.onemenu.server.service;

import java.util.List;

import com.onemenu.server.javabean.NRestaurantDetailBean;
import com.onemenu.server.javabean.NRestaurantItemBean;
import com.onemenu.server.javabean.RestaurantBean;
import com.onemenu.server.javabean.RestaurantSearchConditionBean;
import com.onemenu.server.model.Restaurant;

/**
 * 
 * @author lin
 *
 */
public interface RestaurantService extends AbstractServiceInf {

    public List<RestaurantBean> listRestaurantBean(RestaurantSearchConditionBean condition);
    
    public String getRestaurantLogoUrl(Restaurant restaurant);
    
    public void addPageView(long restaurantId);
    /**
     * 获取餐馆列表
     * @param condition
     * @return
     */
	public List<NRestaurantItemBean> restaurantItems(RestaurantSearchConditionBean condition);
	/**
	 * 获取餐馆详细信息
	 * @param restaurantId
	 * @return
	 */
	public NRestaurantDetailBean getRestaurantDetailInfo(long restaurantId);

}
