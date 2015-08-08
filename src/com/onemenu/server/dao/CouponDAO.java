package com.onemenu.server.dao;

import java.util.List;

import com.onemenu.server.javabean.CouponSearchConditionBean;
import com.onemenu.server.model.Coupon;
import com.onemenu.server.model.Restaurant;


public interface CouponDAO extends BaseDAOInf {

    public int addCouponToCustomer(long customerId, long coouponId) throws RuntimeException;

    public Boolean delCouponFromCustomer(long customerId, long coouponId) throws RuntimeException;
    
    public Boolean delCouponsFromCustomer(long customerId, List<Long> couponIds) throws RuntimeException;
    
    public List<Coupon> getCouponListByCustomerId(long customerId) throws RuntimeException;
    
    public List<Coupon> getCouponListByConditon(CouponSearchConditionBean condition) throws RuntimeException;

	public List<Restaurant> getRestaurantsByConditon(CouponSearchConditionBean condition);
}
