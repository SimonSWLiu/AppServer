package com.onemenu.server.daoImpl;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.onemenu.server.constant.ParameterConstant;
import com.onemenu.server.dao.DishDAO;
import com.onemenu.server.javabean.DishValidationConditionBean;
import com.onemenu.server.javabean.MenuSearchConditionBean;
import com.onemenu.server.javabean.NMenuItemDishBean;
import com.onemenu.server.model.Dish;
import com.onemenu.server.model.DishCategory;
import com.onemenu.server.model.Restaurant;
import com.onemenu.server.util.FormatUtils;
import com.onemenu.server.util.MapUtils;
import com.onemenu.server.util.StringUtil;
import com.onemenu.server.util.TimeUtils;
import com.onemenu.server.util.TimestampUtil;
import com.onemenu.server.util.TimestampUtil.DateFormatType;

public class DishDAOImpl extends BaseDAOSupport implements DishDAO {

	private static final Logger	logger	= Logger.getLogger(DishDAOImpl.class);

	protected HibernateTemplate	mHibernateTemplate;

	@Autowired
	public void setmHibernateTemplate(HibernateTemplate mHibernateTemplate) {
		this.mHibernateTemplate = mHibernateTemplate;
	}

	@Override
	@Transactional
	public List<Dish> getDishListByRestaturantId(long restaurantId) {

		List<Dish> dishList = new ArrayList<Dish>();

		Restaurant restaurant = mHibernateTemplate.get(Restaurant.class, restaurantId);

		for (DishCategory dishCategory : restaurant.getmDishCategorySet()) {

			for (Dish dish : dishCategory.getmDishSet()) {

				if (dish.getmStatus().equals(ParameterConstant.DISH_STATUS_ENABLE)) {
					dishList.add(dish);
				}
			}
		}

		return dishList;
	}

	@Override
	@Transactional
	public List<Dish> validateDish(DishValidationConditionBean condition, List<Long> dishIdList) throws ParseException {

		List<Dish> dishList = new ArrayList<Dish>();

		String date = TimestampUtil.dateToStr(new Date(), DateFormatType.HourMinute12); // modify
																						// by
																						// file
																						// 统一使用新的时间工具类

		DetachedCriteria cri = DetachedCriteria.forClass(Dish.class);
		cri.add(Restrictions.in("mId", dishIdList));
		List<Dish> list = mHibernateTemplate.findByCriteria(cri);

		for (Dish dish : list) {
			
			if(condition.latitude==null||condition.longitude==null){
				dishList.add(dish);
				continue;
			}

			Restaurant restaurant = dish.getmDishCategory().getmRestaurant();
			int week = FormatUtils.getCurrentWeek();
			Double distance = MapUtils.getDistance(condition.latitude, condition.longitude, dish.getmDishCategory().getmRestaurant().getmLatitude(), dish
					.getmDishCategory().getmRestaurant().getmLongitude());

			// check disable restaurant
			if (!restaurant.getmStatus().equals(ParameterConstant.RESTAURANT_STATUS_ENABLE)) {

				dishList.add(dish);
				continue;
			}
			// check disable dish
			else if (!dish.getmStatus().equals(ParameterConstant.DISH_STATUS_ENABLE)) {

				dishList.add(dish);
				continue;
			} else if (restaurant.getmDeliveryDistance() < distance) {

				dishList.add(dish);
				continue;
			} else {
				// check open time and delivery time
				switch (week) {

				case 0:
					if (!TimeUtils.isInTime(restaurant.getmSunOpenStartTime(), restaurant.getmSunOpenEndTime())) {

						dishList.add(dish);
						continue;
					}
					if (!TimeUtils.isInTime(restaurant.getmSunDeliveryStartTime(), restaurant.getmSunDeliveryEndTime())) {

						dishList.add(dish);
						continue;
					}
					break;

				case 1:
					if (!TimeUtils.isInTime(restaurant.getmMonOpenStartTime(), restaurant.getmMonOpenEndTime())) {

						dishList.add(dish);
						continue;
					}
					if (!TimeUtils.isInTime(restaurant.getmMonDeliveryStartTime(), restaurant.getmMonDeliveryEndTime())) {

						dishList.add(dish);
						continue;
					}
					break;

				case 2:
					if (!TimeUtils.isInTime(restaurant.getmTueOpenStartTime(), restaurant.getmTueOpenEndTime())) {

						dishList.add(dish);
						continue;
					}
					if (!TimeUtils.isInTime(restaurant.getmTueDeliveryStartTime(), restaurant.getmTueDeliveryEndTime())) {

						dishList.add(dish);
						continue;
					}
					break;

				case 3:
					if (!TimeUtils.isInTime(restaurant.getmWedOpenStartTime(), restaurant.getmWedOpenEndTime())) {

						dishList.add(dish);
						continue;
					}
					if (!TimeUtils.isInTime(restaurant.getmWedDeliveryStartTime(), restaurant.getmWedDeliveryEndTime())) {

						dishList.add(dish);
						continue;
					}
					break;

				case 4:
					if (!TimeUtils.isInTime(restaurant.getmThuOpenStartTime(), restaurant.getmThuOpenEndTime())) {

						dishList.add(dish);
						continue;
					}
					if (!TimeUtils.isInTime(restaurant.getmThuDeliveryStartTime(), restaurant.getmThuDeliveryEndTime())) {

						dishList.add(dish);
						continue;
					}
					break;

				case 5:
					if (!TimeUtils.isInTime(restaurant.getmFriOpenStartTime(), restaurant.getmFriOpenEndTime())) {

						dishList.add(dish);
						continue;
					}
					if (!TimeUtils.isInTime(restaurant.getmFriDeliveryStartTime(), restaurant.getmFriDeliveryEndTime())) {

						dishList.add(dish);
						continue;
					}
					break;

				case 6:
					if (!TimeUtils.isInTime(restaurant.getmSatOpenStartTime(), restaurant.getmSatOpenEndTime())) {

						dishList.add(dish);
						continue;
					}
					if (!TimeUtils.isInTime(restaurant.getmSatDeliveryStartTime(), restaurant.getmSatDeliveryEndTime())) {

						dishList.add(dish);
						continue;
					}
					break;

				default:
					break;
				}
			}

		}

		return dishList;
	}

	@Override
	@Transactional
	public List<Dish> getDishMenuItemListByConditon(MenuSearchConditionBean condition) {

		List<Dish> dishList = new ArrayList<Dish>();

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

		if (restaurantList.size() == 0) {
			DetachedCriteria extraRestaurantCri = DetachedCriteria.forClass(Restaurant.class);
			extraRestaurantCri.add(Restrictions.not(Restrictions.conjunction().add(Restrictions.between("mLatitude", minLat, maxLat))
					.add(Restrictions.between("mLongitude", minLng, maxLng))));
			// extraRestaurantCri.add(Restrictions.disjunction()
			// .add(Restrictions.lt("mLatitude", minLat))
			// .add(Restrictions.gt("mLatitude",maxLat))
			// .add(Restrictions.lt("mLongitude", minLng))
			// .add(Restrictions.gt("mLongitude", maxLng)));
			restaurantList = mHibernateTemplate.findByCriteria(extraRestaurantCri);
		}

		List<Long> dishIdList = new ArrayList<Long>();

		for (Restaurant restaurant : restaurantList) {

			for (DishCategory dishCategory : restaurant.getmDishCategorySet()) {

				for (Dish dish : dishCategory.getmDishSet()) {

					if (dish.getmStatus() != null && dish.getmStatus().equals(ParameterConstant.DISH_STATUS_ENABLE)) {

						dishIdList.add(dish.getmId());
					}
				}
			}
		}

		if (dishIdList.size() != 0) {

			DetachedCriteria dishCri = DetachedCriteria.forClass(Dish.class);

			dishCri.add(Restrictions.in("mId", dishIdList));

			if (!TextUtils.isEmpty(condition.keyWord)) {
				String[] keyWords = condition.keyWord.split(",");
				Junction junction = Restrictions.disjunction();
				for (int i = 0; i < keyWords.length; i++) {
					String keyWord = keyWords[i];
					junction.add(Restrictions.ilike("mTags", keyWord, MatchMode.ANYWHERE));
					junction.add(Restrictions.ilike("mName", keyWord, MatchMode.ANYWHERE));
				}
				dishCri.add(junction);
			}

			dishCri.addOrder(Order.asc("mId"));

			dishList = mHibernateTemplate.findByCriteria(dishCri);
		}

		// DetachedCriteria dishCri = DetachedCriteria.forClass(Dish.class);
		// DetachedCriteria dcCri = dishCri.createAlias("mDishCategory", "dc");
		// dcCri.add(Restrictions.eq("dc.mRestaurant.mId", new Long(19)));
		// dishList = mHibernateTemplate.findByCriteria(dishCri);

		return dishList;
	}

	final String sql	= "SELECT  d.*,(SQRT((POW(%s-r.latitude,2) + POW(%s-r.longitude,2)))/%s + 5)  AS distance,((dr.page_view + 20 * dr.purchase_volume)/%s)   AS pv, r.delivery_distance,r.folderName FROM dish d "
								+ " LEFT OUTER JOIN dish_category dc  ON d.dish_category_id = dc.id  LEFT OUTER JOIN restaurant r  ON r.id = dc.restaurant_id   LEFT OUTER JOIN dish_rank dr  ON d.dish_rank_id = dr.id "
								+ " WHERE d.status = 1 and d.image_name is not null  AND r.status = 1 AND r.latitude BETWEEN :minLat  AND :maxLat   AND r.longitude BETWEEN :minLng  AND :maxLng  %s ";
	
	final String maxPageViewSql	= "SELECT  max(dr.page_view)  FROM dish d "
			+ " LEFT OUTER JOIN dish_category dc  ON d.dish_category_id = dc.id  LEFT OUTER JOIN restaurant r  ON r.id = dc.restaurant_id   LEFT OUTER JOIN dish_rank dr  ON d.dish_rank_id = dr.id "
			+ " WHERE d.status = 1 and d.image_name is not null AND r.status = 1 AND r.latitude BETWEEN :minLat  AND :maxLat   AND r.longitude BETWEEN :minLng  AND :maxLng  %s ";

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<NMenuItemDishBean> getDishMenuItemsByConditon(MenuSearchConditionBean condition) {

		// sql %s-->curlatitude ,%s --->curlongitude, %s ---> distanceContant *
		// distanceContant, %s -->pageViewContant ,%s --->purchaseContant ,%s
		// --> and d.name like '%:name1%' or d.tags like '%:tag1%'

		// 计算出 distance km 所对应的经纬度范围：
		double range = 180 / Math.PI * condition.distance / MapUtils.EARTH_RADIUS; // 单位km
		double lngR = range / Math.cos(condition.latitude * Math.PI / 180.0);

		double maxLat = condition.latitude + range;
		double minLat = condition.latitude - range;
		double maxLng = condition.longitude + lngR;
		double minLng = condition.longitude - lngR;
		
		// 距离常量 
		double distanceContant = 10;
		// 最大浏览次数  default
		int maxPageView = 1000;

		StringBuilder sqlBuilder = new StringBuilder(" ");
		Map<String, String> params = null;
		// 构造tags搜索相关的条件
		if (!TextUtils.isEmpty(condition.keyWord)) {
			String[] keyWords = condition.keyWord.split(",");
			sqlBuilder = new StringBuilder(" and  ( ");
			int len = keyWords.length;
			params = new HashMap<String, String>(len);
			for (int i = 0; i < len - 1; i++) {
				String paramName = "p" + i;
				String paramTag = "t" +i;
				sqlBuilder.append("d.top_tags_name like :" + paramName).append("  or  ").append("d.tags like :" + paramTag).append("  or  ").append("r.top_tags_name like :" + paramName).append("  or  ");
				params.put(paramName, "%"+keyWords[i].toLowerCase()+"%");
				params.put(paramTag, "%"+keyWords[i]+"%");
			}

			String paramName = "p" + (len - 1);
			String paramTag = "t" +(len - 1);
			sqlBuilder.append("d.top_tags_name like :" + paramName).append("  or  ").append("d.tags like :" + paramTag).append("  or  ").append("r.top_tags_name like :" + paramName).append(" )");
			params.put(paramName, "%"+keyWords[len - 1].toLowerCase()+"%");
			params.put(paramTag, "%"+keyWords[len - 1]+"%");
		}
		Session session = mHibernateTemplate.getSessionFactory().getCurrentSession();
		
		String _maxPageViewSql = String.format(maxPageViewSql, sqlBuilder.toString());
		
		try {
			Query query = session.createSQLQuery(_maxPageViewSql);
			query.setParameter("minLat", minLat).setParameter("maxLat", maxLat).setParameter("maxLng", maxLng).setParameter("minLng", minLng);
			if (params != null) {
				for (Entry<String, String> entry : params.entrySet())
					query.setParameter(entry.getKey(), entry.getValue());
			}
			Object obj = query.uniqueResult();
			if(obj!=null){
				maxPageView = Integer.valueOf(obj.toString());
			}
			
		} catch (Exception e) {
			logger.error("", e);
		}
		
		
		
		String _sql = String.format(sql, condition.latitude, condition.longitude, distanceContant, maxPageView,
				sqlBuilder.toString());
		// TODO 加入查询条件限制
		_sql = "select 'D' as type,t.id as dishId,t.name as dishName,t.price as dishPrice,concat(t.folderName,'/',t.image_name) as dishImageUrl  from (" + _sql
				+ ")  t  order by (t.distance*t.pv) desc ";

		if (logger.isDebugEnabled())
			logger.debug("sql:" + _sql);

		
		try {
			Query query = session.createSQLQuery(_sql);
			query.setParameter("minLat", minLat).setParameter("maxLat", maxLat).setParameter("maxLng", maxLng).setParameter("minLng", minLng);

			if (params != null) {
				for (Entry<String, String> entry : params.entrySet())
					query.setParameter(entry.getKey(), entry.getValue());
			}
			query.setFirstResult(condition.startNum).setMaxResults(condition.range)
					.setResultTransformer(new AliasToBeanResultTransformer(NMenuItemDishBean.class));

			return query.list();

		} catch (Exception e) {
			logger.error("", e);
		} finally {
			// session.close();
		}
		return new ArrayList<NMenuItemDishBean>(1);
	}

	final String	review_sql	= "SELECT 'R' as type, r.id AS id, rr.`page_view` as rank "
										+ "FROM review r  LEFT OUTER JOIN   review_rank rr  ON r.review_rank_id=rr.id  %s  ";

	@Override
	@Transactional
	public Map<String, String> getDishAndReviewsMenuItemsByConditon(MenuSearchConditionBean condition) {
		// 计算出 distance km 所对应的经纬度范围：
		double range = 180 / Math.PI * condition.distance / MapUtils.EARTH_RADIUS; // 单位km
		double lngR = range / Math.cos(condition.latitude * Math.PI / 180.0);

		double maxLat = condition.latitude + range;
		double minLat = condition.latitude - range;
		double maxLng = condition.longitude + lngR;
		double minLng = condition.longitude - lngR;
		// 距离常量 
		double distanceContant = 10;
		// 最大浏览次数  default
		int maxPageView = 1000;
		// 默认最大评论数
		int maxReview =  100;

		// 构造dish排序sql
		StringBuilder sqlBuilder = new StringBuilder(" ");
		Map<String, String> params = null;
		// 构造tags搜索相关的条件
		if (!TextUtils.isEmpty(condition.keyWord)) {
			String[] keyWords = condition.keyWord.split(",");
			sqlBuilder = new StringBuilder(" and (");
			int len = keyWords.length;
			params = new HashMap<String, String>(len);
			for (int i = 0; i < len - 1; i++) {
				String paramName = "p" + i;
				String paramTag = "t" +i;
				sqlBuilder.append("d.top_tags_name like :" + paramName).append("  or  ").append("d.tags like :" + paramTag).append("  or  ").append("r.top_tags_name like :" + paramName).append("  or  ");
				params.put(paramName, "%"+keyWords[i].toLowerCase()+"%");
				params.put(paramTag, "%"+keyWords[i]+"%");
			}

			String paramName = "p" + (len - 1);
			String paramTag = "t" +(len - 1);
			sqlBuilder.append("d.top_tags_name like :" + paramName).append("  or  ").append("d.tags like :" + paramTag).append("  or  ").append("r.top_tags_name like :" + paramName).append(" )");
			params.put(paramName, "%"+keyWords[len - 1].toLowerCase()+"%");
			params.put(paramTag, "%"+keyWords[len - 1]+"%");
		}
		
		Session session = mHibernateTemplate.getSessionFactory().getCurrentSession();
		
		String _maxPageViewSql = String.format(maxPageViewSql, sqlBuilder.toString());
		
		try {
			Query query = session.createSQLQuery(_maxPageViewSql);
			query.setParameter("minLat", minLat).setParameter("maxLat", maxLat).setParameter("maxLng", maxLng).setParameter("minLng", minLng);
			if (params != null) {
				for (Entry<String, String> entry : params.entrySet())
					query.setParameter(entry.getKey(), entry.getValue());
			}
			Object obj = query.uniqueResult();
			if(obj!=null){
				maxPageView = Integer.valueOf(obj.toString());
			}
			
		} catch (Exception e) {
			logger.error("", e);
		}
		

		String _sql = String.format(sql, condition.latitude, condition.longitude,distanceContant, maxPageView,sqlBuilder.toString());

		
		String maxDishRankSql = "select max(t.distance * t.pv) from (" + _sql + ")  t  ";
		
		_sql = "select 'D' as type,t.id as id,(t.distance * t.pv)/%s  as rank  from (" + _sql + ")  t  ";
		
		

		// 构造review排序
		StringBuilder _sqlBuilder = new StringBuilder(" ");
		Map<String, String> _params = null;
		// 构造tags搜索相关的条件
		if (!TextUtils.isEmpty(condition.keyWord)) {
			String[] keyWords = condition.keyWord.split(",");
			_sqlBuilder = new StringBuilder("where  ");
			int len = keyWords.length;
			_params = new HashMap<String, String>(len);
			for (int i = 0; i < len - 1; i++) {
				String paramName = "_p" + i;
				_sqlBuilder.append("r.summary like :" + paramName).append("  or  ");
				_params.put(paramName, "%"+keyWords[i]+"%");
			}

			String paramName = "_p" + (len - 1);
			_sqlBuilder.append("r.summary like :" + paramName).append(" ");
			_params.put(paramName, "%"+keyWords[len - 1]+"%");
		}
		
		String _reviewSql = String.format(review_sql, _sqlBuilder.toString());
		
		String maxReviewSql = "select max(t.rank) from (" + _reviewSql + ")  t  ";
		
		
		try {
			Query maxDishQuery = session.createSQLQuery(maxDishRankSql);
			maxDishQuery.setParameter("minLat", minLat).setParameter("maxLat", maxLat).setParameter("maxLng", maxLng).setParameter("minLng", minLng);
			if (params != null) {
				for (Entry<String, String> entry : params.entrySet())
					maxDishQuery.setParameter(entry.getKey(), entry.getValue());
			}
			
			Double maxDishRank = ((Double)maxDishQuery.uniqueResult());
			
			if(logger.isDebugEnabled())
				logger.debug("maxDishRank:"+maxDishRank);
			
			_sql = String.format(_sql, maxDishRank);
			if (logger.isDebugEnabled())
				logger.debug("_sql:" + _sql);
			
			
			Query maxReviewQuery = session.createSQLQuery(maxReviewSql);
			if (_params != null) {
				for (Entry<String, String> entry : _params.entrySet())
					maxReviewQuery.setParameter(entry.getKey(), entry.getValue());
			}
			
			Object obj = maxReviewQuery.uniqueResult();
			if(obj!=null)
				maxReview = ((BigInteger)obj).intValue();
			
			int maxReviewRank = maxReview;
			
			if(logger.isDebugEnabled())
				logger.debug("maxReviewRank:"+maxReviewRank);
			
			_reviewSql = "select g.type,g.id,g.rank/%s as rank  from (" + _reviewSql + ")  g  ";
			_reviewSql = String.format(_reviewSql , maxReviewRank);
			
			String totalSql = "select k.type,k.id,k.rank  from ( " + _sql + " union all " + _reviewSql + " ) k order by k.rank desc";
			if (logger.isDebugEnabled())
				logger.debug("totalSql:" + totalSql);
			Query query = session.createSQLQuery(totalSql);
			query.setParameter("minLat", minLat).setParameter("maxLat", maxLat).setParameter("maxLng", maxLng).setParameter("minLng", minLng);
			if (params != null) {
				for (Entry<String, String> entry : params.entrySet())
					query.setParameter(entry.getKey(), entry.getValue());
			}
			
			if (_params != null) {
				for (Entry<String, String> entry : _params.entrySet())
					query.setParameter(entry.getKey(), entry.getValue());
			}
			query.setFirstResult(condition.startNum).setMaxResults(condition.range);
			@SuppressWarnings("unchecked")
			List<Object[]> list = query.list();
			Map<String, String> map = new LinkedHashMap<String, String>(list.size());
			for (Object[] objs : list) {
				if (objs.length >= 2) {
					map.put(String.valueOf(objs[0]) + String.valueOf(objs[1]), String.valueOf(objs[0]));
				}
			}
			return map;
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			// session.close();
		}
		return new HashMap<String, String>(1);
	}
	
	final String	sqlById	= "SELECT   'D' as type,d.id as dishId,d.name as dishName,d.price as dishPrice,concat(r.folderName,'/',d.image_name) as dishImageUrl FROM dish d "
			+ " LEFT OUTER JOIN dish_category dc  ON d.dish_category_id = dc.id  LEFT OUTER JOIN restaurant r  ON r.id = dc.restaurant_id "
			+ " WHERE d.status = 1  AND r.status = 1 AND d.id=:id ";

	@Override
	@Transactional
	public NMenuItemDishBean getDishMenuItemById(String id) {
		
		Session session = mHibernateTemplate.getSessionFactory().getCurrentSession();
		try {
			Query query = session.createSQLQuery(sqlById);
			query.setParameter("id", id);
			query.setResultTransformer(new AliasToBeanResultTransformer(NMenuItemDishBean.class));
			return (NMenuItemDishBean) query.uniqueResult();
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			// session.close();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public void fixedData() {
		DetachedCriteria dishCri = DetachedCriteria.forClass(Dish.class);
		List<Dish> dishs = mHibernateTemplate.findByCriteria(dishCri);
		for(Dish dish:dishs){
			dish.setTopTagsName(StringUtil.removalSpace(dish.getmName()));
		}
		mHibernateTemplate.saveOrUpdateAll(dishs);
		DetachedCriteria restautantCri = DetachedCriteria.forClass(Restaurant.class);
		List<Restaurant> restautants = mHibernateTemplate.findByCriteria(restautantCri);
		for(Restaurant restautant:restautants){
			restautant.setTopTagsName(StringUtil.removalSpace(restautant.getmName()));
		}
		mHibernateTemplate.saveOrUpdateAll(restautants);
		
	}

}
