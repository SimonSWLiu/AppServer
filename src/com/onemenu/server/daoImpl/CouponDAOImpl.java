package com.onemenu.server.daoImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.onemenu.server.dao.CouponDAO;
import com.onemenu.server.javabean.CouponSearchConditionBean;
import com.onemenu.server.model.Coupon;
import com.onemenu.server.model.Customer;
import com.onemenu.server.model.Restaurant;
import com.onemenu.server.util.MapUtils;

public class CouponDAOImpl extends BaseDAOSupport implements CouponDAO {

	protected HibernateTemplate	mHibernateTemplate;

	@Autowired
	public void setmHibernateTemplate(HibernateTemplate mHibernateTemplate) {
		this.mHibernateTemplate = mHibernateTemplate;
	}

	@Override
	@Transactional
	public int addCouponToCustomer(long customerId, long coouponId) {

		int result = 0;

		Customer customer = mHibernateTemplate.get(Customer.class, customerId);
		Coupon coupon = mHibernateTemplate.get(Coupon.class, coouponId);

		if (customer != null && coupon != null) {
			Set<Coupon> couponSet = customer.getmCouponSet();

			for (Coupon c : couponSet) {
				if (c.getmId() == coupon.getmId())
					return 2;
			}

			couponSet.add(coupon);
			customer.setmCouponSet(couponSet);
			mHibernateTemplate.update(customer);
			return 1;
		}

		return result;
	}

	@Override
	@Transactional
	public Boolean delCouponFromCustomer(long customerId, long coouponId) {

		Boolean result = false;

		Customer customer = mHibernateTemplate.get(Customer.class, customerId);
		Coupon coupon = mHibernateTemplate.get(Coupon.class, coouponId);

		if (customer != null && coupon != null) {
			Set<Coupon> couponSet = customer.getmCouponSet();
			couponSet.remove(coupon);
			customer.setmCouponSet(couponSet);
			mHibernateTemplate.update(customer);
			result = true;
		}

		return result;
	}

	@Override
	@Transactional
	public List<Coupon> getCouponListByCustomerId(long customerId) {

		List<Coupon> couponList = new ArrayList<Coupon>();

		Customer customer = mHibernateTemplate.get(Customer.class, customerId);

		if (customer != null) {

			Set<Coupon> couponSet = customer.getmCouponSet();

			for (Coupon coupon : couponSet) {

				couponList.add(coupon);
			}
		}

		return couponList;
	}

	@Override
	@Transactional
	public List<Coupon> getCouponListByConditon(CouponSearchConditionBean condition) {

		List<Coupon> couponList = new ArrayList<Coupon>();

		DetachedCriteria restaurantCri = DetachedCriteria.forClass(Restaurant.class);

		// 计算出 distance km 所对应的经纬度范围：
		double range = 180 / Math.PI * condition.distance / MapUtils.EARTH_RADIUS; // 单位km
		double lngR = range / Math.cos(condition.latitude * Math.PI / 180.0);

		double maxLat = condition.latitude + range;
		double minLat = condition.latitude - range;
		double maxLng = condition.longitude + lngR;
		double minLng = condition.longitude - lngR;

		// 搜索范围
		restaurantCri.add(Restrictions.conjunction().add(Restrictions.between("mLatitude", minLat, maxLat))
				.add(Restrictions.between("mLongitude", minLng, maxLng)));

		List<Restaurant> restaurantList = mHibernateTemplate.findByCriteria(restaurantCri);

		for (Restaurant restaurant : restaurantList) {

			for (Coupon coupon : restaurant.getmCouponSet()) {

				couponList.add(coupon);
			}
		}

		return couponList;
	}

	@Override
	@Transactional
	public Boolean delCouponsFromCustomer(long customerId, List<Long> couponIds) throws RuntimeException {

		Boolean result = false;

		Customer customer = mHibernateTemplate.get(Customer.class, customerId);

		if (customer != null) {
			Set<Coupon> couponSet = customer.getmCouponSet();
			if (couponSet != null) {
				List<Coupon> removeSet = new ArrayList<Coupon>();
				for (Coupon coupon : couponSet) {
					if (couponIds.contains(coupon.getmId())) {
						removeSet.add(coupon);
					}
				}
				if (!removeSet.isEmpty())
					couponSet.removeAll(removeSet);
				mHibernateTemplate.update(customer);
				result = true;
			}

		}

		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Restaurant> getRestaurantsByConditon(CouponSearchConditionBean condition) {
		
		DetachedCriteria restaurantCri = DetachedCriteria.forClass(Restaurant.class);

		// 计算出 distance km 所对应的经纬度范围：
		double range = 180 / Math.PI * condition.distance / MapUtils.EARTH_RADIUS; // 单位km
		double lngR = range / Math.cos(condition.latitude * Math.PI / 180.0);

		double maxLat = condition.latitude + range;
		double minLat = condition.latitude - range;
		double maxLng = condition.longitude + lngR;
		double minLng = condition.longitude - lngR;

		// 搜索范围
		restaurantCri.add(Restrictions.conjunction().add(Restrictions.between("mLatitude", minLat, maxLat))
				.add(Restrictions.between("mLongitude", minLng, maxLng)));

		return mHibernateTemplate.findByCriteria(restaurantCri);
	}

}
