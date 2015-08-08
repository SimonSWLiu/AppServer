package com.onemenu.server.service;

import java.util.List;

import com.onemenu.server.javabean.CouponBean;
import com.onemenu.server.javabean.CouponSearchConditionBean;
import com.onemenu.server.javabean.NCouponBean;
import com.onemenu.server.javabean.NCouponMarketBean;
import com.onemenu.server.javabean.TradeBean;

public interface CouponService extends AbstractServiceInf {

    public int addCouponToCustomer(long customerId, long coouponId);
    
    public Boolean delCouponFromCustomer(long customerId, long coouponId);
    
    public Boolean delCouponsFromCustomer(TradeBean tradeBean);
        
    public List<CouponBean> getMyChest(long customerId);
    
    public List<CouponBean> getCouponMarket(CouponSearchConditionBean condition);

	public List<NCouponBean> getMyChests(long customerId);

	public List<NCouponMarketBean> getCouponMarkets(CouponSearchConditionBean condition);
    
}
