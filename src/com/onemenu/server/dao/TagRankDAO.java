package com.onemenu.server.dao;


/**
 * 
 * @author lin
 *
 */
public interface TagRankDAO extends BaseDAOInf {

    public void addPageView(String tags) throws RuntimeException;

}
