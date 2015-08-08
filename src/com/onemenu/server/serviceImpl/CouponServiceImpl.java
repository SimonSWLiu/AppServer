package com.onemenu.server.serviceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.onemenu.server.constant.ParameterConstant;
import com.onemenu.server.dao.CouponDAO;
import com.onemenu.server.javabean.CouponBean;
import com.onemenu.server.javabean.CouponSearchConditionBean;
import com.onemenu.server.javabean.DishBean;
import com.onemenu.server.javabean.NCouponBean;
import com.onemenu.server.javabean.NCouponMarketBean;
import com.onemenu.server.javabean.NMenuItemDishBean;
import com.onemenu.server.javabean.NRestaurantItemBean;
import com.onemenu.server.javabean.OrderFormBean;
import com.onemenu.server.javabean.RestaurantBean;
import com.onemenu.server.javabean.TradeBean;
import com.onemenu.server.model.Coupon;
import com.onemenu.server.model.Dish;
import com.onemenu.server.model.Restaurant;
import com.onemenu.server.service.CouponService;
import com.onemenu.server.service.DishService;
import com.onemenu.server.util.FormatUtils;
import com.onemenu.server.util.MapUtils;
import com.onemenu.server.util.StringUtil;

public class CouponServiceImpl extends AbstractServiceImpl implements CouponService {

	private static final Logger	logger	= Logger.getLogger(CouponServiceImpl.class);

	private CouponDAO			mCouponDAO;

	@Autowired
	public void setmCouponDAO(CouponDAO mCouponDAO) {
		this.mCouponDAO = mCouponDAO;
	}

	private DishService	mDishService;

	@Autowired
	public void setmCouponService(DishService mDishService) {
		this.mDishService = mDishService;
	}

	public int addCouponToCustomer(long customerId, long coouponId) {

		return mCouponDAO.addCouponToCustomer(customerId, coouponId);
	}

	public Boolean delCouponFromCustomer(long customerId, long coouponId) {

		return mCouponDAO.delCouponFromCustomer(customerId, coouponId);
	}

	@Override
	public List<CouponBean> getMyChest(long customerId) {

		List<CouponBean> couponBeanList = new ArrayList<CouponBean>();

		List<Coupon> couponList = mCouponDAO.getCouponListByCustomerId(customerId);
		for (Coupon coupon : couponList) {

			CouponBean couponBean = new CouponBean();

			couponBean.couponId = String.valueOf(coupon.getmId());
			if (coupon.getmDescription() != null)
				couponBean.couponDesc = coupon.getmDescription();
			if (coupon.getmMaturityDate() != null)
				couponBean.maturityDate = coupon.getmMaturityDate().toString();
			if (coupon.getmType() != null)
				couponBean.type = coupon.getmType();
			if (coupon.getmTypeAmount() != null)
				couponBean.typeAmount = StringUtil.removeLastZero(coupon.getmTypeAmount());
			if (coupon.getmTargetType() != null)
				couponBean.targetType = coupon.getmTargetType();

			for (Dish dish : coupon.getmCouponTargetDishsSet()) {
				DishBean dishBean = new DishBean();
				dishBean.dishId = String.valueOf(dish.getmId());
				dishBean.dishName = dish.getmName();
				dishBean.dishImageUrl = mDishService.getDishImageUrl(dish);
				dishBean.dishPrice = FormatUtils.formatPrice(dish.getmPrice());
				couponBean.targetDishArray.add(dishBean);
			}
			couponBean.extraCriType = coupon.getmExtraCriType();
			for (Dish dish : coupon.getmCouponExtraCriDishsSet()) {
				DishBean dishBean = new DishBean();
				dishBean.dishId = String.valueOf(dish.getmId());
				dishBean.dishName = dish.getmName();
				dishBean.dishImageUrl = mDishService.getDishImageUrl(dish);
				dishBean.dishPrice = FormatUtils.formatPrice(dish.getmPrice());
				couponBean.extraCriDishArray.add(dishBean);
			}
			couponBean.weeks = coupon.getmWeeks();

			// add by file on 2015-05-05
			if (couponBean.extraCriType.equals(ParameterConstant.COUPON_CRI_TYPE_ROW_BILL)) {
				couponBean.extraCriAmount = String.valueOf(coupon.getmExtraCriAmount());
			}

			RestaurantBean restaurantBean = new RestaurantBean();
			restaurantBean.restaurantId = String.valueOf(coupon.getmRestaurant().getmId());
			if (coupon.getmRestaurant().getmName() != null)
				restaurantBean.restaurantName = coupon.getmRestaurant().getmName();
			if (coupon.getmRestaurant().getmLogo() != null)
				restaurantBean.restaurantLogoUrl = coupon.getmRestaurant().getmLogo();
			couponBean.restaurant = restaurantBean;

			couponBeanList.add(couponBean);
		}

		return couponBeanList;

	}

	@Override
	public List<CouponBean> getCouponMarket(CouponSearchConditionBean condition) {

		List<CouponBean> couponBeanList = new ArrayList<CouponBean>();

		List<Coupon> couponList = mCouponDAO.getCouponListByConditon(condition);

		for (Coupon coupon : couponList) {

			CouponBean couponBean = new CouponBean();
			couponBean.couponId = String.valueOf(coupon.getmId());
			if (coupon.getmDescription() != null)
				couponBean.couponDesc = coupon.getmDescription();
			if (coupon.getmMaturityDate() != null)
				couponBean.maturityDate = coupon.getmMaturityDate().toString();
			if (coupon.getmType() != null)
				couponBean.type = coupon.getmType();
			if (coupon.getmTypeAmount() != null)
				couponBean.typeAmount = StringUtil.removeLastZero(coupon.getmTypeAmount());
			if (coupon.getmTargetType() != null)
				couponBean.targetType = coupon.getmTargetType();
			for (Dish dish : coupon.getmCouponTargetDishsSet()) {
				DishBean dishBean = new DishBean();
				dishBean.dishId = String.valueOf(dish.getmId());
				dishBean.dishName = dish.getmName();
				dishBean.dishImageUrl = mDishService.getDishImageUrl(dish);
				dishBean.dishPrice = FormatUtils.formatPrice(dish.getmPrice());
				couponBean.targetDishArray.add(dishBean);
			}

			if (coupon.getmExtraCriType() != null)
				couponBean.extraCriType = coupon.getmExtraCriType();
			if (couponBean.extraCriType.equals(ParameterConstant.COUPON_CRI_TYPE_DISHS)) {

				for (Dish dish : coupon.getmCouponExtraCriDishsSet()) {
					DishBean dishBean = new DishBean();
					dishBean.dishId = String.valueOf(dish.getmId());
					dishBean.dishName = dish.getmName();
					dishBean.dishImageUrl = mDishService.getDishImageUrl(dish);
					dishBean.dishPrice = FormatUtils.formatPrice(dish.getmPrice());
					couponBean.extraCriDishArray.add(dishBean);
				}
			} else if (couponBean.extraCriType.equals(ParameterConstant.COUPON_CRI_TYPE_ROW_BILL)) {
				couponBean.extraCriAmount = String.valueOf(coupon.getmExtraCriAmount());
			}
			if (coupon.getmWeeks() != null)
				couponBean.weeks = coupon.getmWeeks();
			couponBean.rank = MapUtils.getDistance(condition.latitude, condition.longitude, coupon.getmRestaurant().getmLatitude(), coupon.getmRestaurant()
					.getmLongitude());

			RestaurantBean restaurantBean = new RestaurantBean();
			restaurantBean.restaurantId = String.valueOf(coupon.getmRestaurant().getmId());
			if (coupon.getmRestaurant().getmName() != null)
				restaurantBean.restaurantName = coupon.getmRestaurant().getmName();
			if (coupon.getmRestaurant().getmLogo() != null)
				restaurantBean.restaurantLogoUrl = coupon.getmRestaurant().getmLogo();
			couponBean.restaurant = restaurantBean;

			couponBeanList.add(couponBean);
		}

		return couponBeanList;
	}

	@Override
	public Boolean delCouponsFromCustomer(TradeBean tradeBean) {

		String customerId = tradeBean.customer.customerId;
		if (!StringUtil.isNumber(customerId)) {
			logger.error("customerid is not number ! tradebean:" + tradeBean);
			return false;
		}

		List<Long> couponIds = new ArrayList<Long>();
		String tradeCouponId = tradeBean.tradeCouponId;
		if (StringUtil.isNumber(tradeCouponId))
			couponIds.add(Long.valueOf(tradeCouponId));

		List<OrderFormBean> orders = tradeBean.orderFormArray;
		if (orders != null) {
			for (OrderFormBean order : orders) {
				String orderCouponId = order.orderFormCouponId;
				if (StringUtil.isNumber(orderCouponId))
					couponIds.add(Long.valueOf(orderCouponId));

			}
		}
		if (orders.isEmpty()) {
			return false;
		}

		return mCouponDAO.delCouponsFromCustomer(Long.valueOf(customerId), couponIds);
	}

	@Override
	public List<NCouponBean> getMyChests(long customerId) {

		List<NCouponBean> couponBeanList = new ArrayList<NCouponBean>();
		List<Coupon> couponList = mCouponDAO.getCouponListByCustomerId(customerId);
		for (Coupon coupon : couponList) {

			NCouponBean couponBean = new NCouponBean();

			couponBean.couponId = String.valueOf(coupon.getmId());
			couponBean.couponDesc = coupon.getmDescription();
			couponBean.maturityDate = coupon.getmMaturityDate();
			couponBean.type = coupon.getmType();
			couponBean.typeAmount = StringUtil.removeLastZero(coupon.getmTypeAmount());
			couponBean.targetType = coupon.getmTargetType();

			couponBean.targetDishArray = new ArrayList<NMenuItemDishBean>();

			for (Dish dish : coupon.getmCouponTargetDishsSet()) {
				NMenuItemDishBean dishBean = new NMenuItemDishBean();
				dishBean.dishId = String.valueOf(dish.getmId());
				dishBean.dishName = dish.getmName();
				dishBean.dishImageUrl = mDishService.getDishImageUrl(dish);
				dishBean.dishPrice = FormatUtils.formatPrice(dish.getmPrice());
				couponBean.targetDishArray.add(dishBean);
			}
			couponBean.extraCriType = coupon.getmExtraCriType();
			couponBean.extraCriDishArray = new ArrayList<NMenuItemDishBean>();

			for (Dish dish : coupon.getmCouponExtraCriDishsSet()) {
				NMenuItemDishBean dishBean = new NMenuItemDishBean();
				dishBean.dishId = String.valueOf(dish.getmId());
				dishBean.dishName = dish.getmName();
				couponBean.extraCriDishArray.add(dishBean);
			}
			couponBean.weeks = coupon.getmWeeks();

			// add by file on 2015-05-05
			if (couponBean.extraCriType.equals(ParameterConstant.COUPON_CRI_TYPE_ROW_BILL)) {
				couponBean.extraCriAmount = String.valueOf(coupon.getmExtraCriAmount());
			}

			NRestaurantItemBean restaurantBean = new NRestaurantItemBean();
			restaurantBean.restaurantId = String.valueOf(coupon.getmRestaurant().getmId());
			restaurantBean.restaurantName = coupon.getmRestaurant().getmName();
			restaurantBean.restaurantLogoUrl = coupon.getmRestaurant().getmLogo();
			couponBean.restaurant = restaurantBean;

			couponBeanList.add(couponBean);
		}

		return couponBeanList;
	}

	@Override
	@Transactional
	public List<NCouponMarketBean> getCouponMarkets(final CouponSearchConditionBean condition) {

		List<NCouponMarketBean> couponBeanList = new ArrayList<NCouponMarketBean>();

		List<Restaurant> restaurants = mCouponDAO.getRestaurantsByConditon(condition);

		Collections.sort(restaurants, new Comparator<Restaurant>() {

			public int compare(Restaurant arg0, Restaurant arg1) {
				Double d0 = MapUtils.getDistance(condition.latitude, condition.longitude, arg0.getmLatitude(), arg0.getmLongitude());
				Double d1 = MapUtils.getDistance(condition.latitude, condition.longitude, arg1.getmLatitude(), arg1.getmLongitude());
				return d1.compareTo(d0);
			}
		});

		for (Restaurant restaurant : restaurants) {

			NCouponMarketBean bean = new NCouponMarketBean();
			bean.restaurantId = String.valueOf(restaurant.getmId());
			bean.restaurantName = restaurant.getmName();
			bean.restaurantLogoUrl = restaurant.getmLogo();
			couponBeanList.add(bean);
			bean.couponArray = new ArrayList<NCouponBean>();
			for (Coupon coupon : restaurant.getmCouponSet()) {

				NCouponBean couponBean = new NCouponBean();
				couponBean.couponId = String.valueOf(coupon.getmId());
				couponBean.couponDesc = coupon.getmDescription();
				couponBean.maturityDate = coupon.getmMaturityDate().toString();
				couponBean.type = coupon.getmType();
				couponBean.typeAmount = StringUtil.removeLastZero(coupon.getmTypeAmount());
				couponBean.targetType = coupon.getmTargetType();

				couponBean.targetDishArray = new ArrayList<NMenuItemDishBean>();
				for (Dish dish : coupon.getmCouponTargetDishsSet()) {
					NMenuItemDishBean dishBean = new NMenuItemDishBean();
					dishBean.dishId = String.valueOf(dish.getmId());
					dishBean.dishName = dish.getmName();
					couponBean.targetDishArray.add(dishBean);
				}

				couponBean.extraCriType = coupon.getmExtraCriType();

				couponBean.extraCriDishArray = new ArrayList<NMenuItemDishBean>();

				if (couponBean.extraCriType.equals(ParameterConstant.COUPON_CRI_TYPE_DISHS)) {

					for (Dish dish : coupon.getmCouponExtraCriDishsSet()) {
						NMenuItemDishBean dishBean = new NMenuItemDishBean();
						dishBean.dishId = String.valueOf(dish.getmId());
						dishBean.dishName = dish.getmName();
						couponBean.extraCriDishArray.add(dishBean);
					}
				} else if (couponBean.extraCriType.equals(ParameterConstant.COUPON_CRI_TYPE_ROW_BILL)) {
					couponBean.extraCriAmount = String.valueOf(coupon.getmExtraCriAmount());
				}

				couponBean.weeks = coupon.getmWeeks();

				NRestaurantItemBean restaurantBean = new NRestaurantItemBean();
				restaurantBean.restaurantId = String.valueOf(restaurant.getmId());
				restaurantBean.restaurantName = restaurant.getmName();
				restaurantBean.restaurantLogoUrl = restaurant.getmLogo();
				couponBean.restaurant = restaurantBean;
				bean.couponArray.add(couponBean);
			}
			
		}

		return couponBeanList;
	}
}
