package com.onemenu.server.dao;


/**
 * 
 * @author lin
 *
 */
public interface RestaurantRankDAO extends BaseDAOInf {

    public void addPageView(long restaurantId) throws RuntimeException;

}
