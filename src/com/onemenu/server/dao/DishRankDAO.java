package com.onemenu.server.dao;


/**
 * 
 * @author lin
 *
 */
public interface DishRankDAO extends BaseDAOInf {

    public void addPageView(long dishId) throws RuntimeException;

}
