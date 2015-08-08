package com.onemenu.server.daoImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.onemenu.server.dao.TopTagsDao;
import com.onemenu.server.javabean.TopTagsBean;
import com.onemenu.server.model.MenuTopTags;
import com.onemenu.server.model.PersistenceEntity;
import com.onemenu.server.model.RestaurantTopTags;
import com.onemenu.server.protocol.req.toptags.GetTopTagsData;
/**
 * 热门标签数据库操作实现类
 * @author file
 * @since 2015-4-22 下午4:11:48
 * @version 1.0
 */
@Repository("topTagsDao")
public class TopTagsDaoImpl extends BaseDAOSupport implements TopTagsDao {
	
	private static final Logger logger = Logger.getLogger(TopTagsDaoImpl.class);
	
	//菜单热门标签的查询sql前缀
	final String menuTopTagsPrefixSql = "SELECT tag,SUM(`count`) AS `count`  FROM  menu_top_tags  WHERE  1=1 ";
	//餐馆热门标签的查询sql前缀
	final String restaurantTopTagsPrefixSql = "SELECT tag,SUM(`count`) AS `count`  FROM  restaurant_top_tags  WHERE  1=1    ";
	//sql后缀
	final String suffixSql = " GROUP BY tag ORDER BY  `count` DESC ";
	//最大的数量
	final int maxCount = 6;
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<TopTagsBean> getMenuTopTagsData(GetTopTagsData resData) {
		
		Session session = mHibernateTemplate.getSessionFactory().getCurrentSession();
		String sql = menuTopTagsPrefixSql + suffixSql;
		try {
			Query query = session.createSQLQuery(sql);
			query.setResultTransformer(new AliasToBeanResultTransformer(TopTagsBean.class));
			return query.list();
		} catch (Exception e) {
			logger.error("", e);
		} finally {
		}
		return new ArrayList<TopTagsBean>(1);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<TopTagsBean> getRestaurantTopTagsData(GetTopTagsData resData) {
		
		Session session = mHibernateTemplate.getSessionFactory().getCurrentSession();
		String sql = restaurantTopTagsPrefixSql + suffixSql;
		try {
			Query query = session.createSQLQuery(sql);
			query.setResultTransformer(new AliasToBeanResultTransformer(TopTagsBean.class));
			return query.list();
		} catch (Exception e) {
			logger.error("", e);
		} finally {
		}
		return new ArrayList<TopTagsBean>(1);
	}
	
	
	public <T extends PersistenceEntity> long getCount(Class<T> clazz,Map<String,Object> params){
		DetachedCriteria cri = DetachedCriteria.forClass(clazz);
	    cri.setProjection(Projections.rowCount());
	    if(params!=null&&!params.isEmpty())
	    	for(Entry<String, Object> entry:params.entrySet())
	    		cri.add(Restrictions.eq(entry.getKey(), entry.getValue()));
        List<?> results = findByCriteria(cri, clazz);
        long count = ((Long) results.get(0)).intValue();
        return count;
	}
	
	final String updateTopTagsOfMenu = "UPDATE menu_top_tags  SET `count` = `count`+1  WHERE latitude=:latitude AND longitude=:longitude AND tag=:tag";
	
	final String updateTopTagsOfRestaurant = "UPDATE restaurant_top_tags  SET `count` = `count`+1  WHERE latitude=:latitude AND longitude=:longitude AND tag=:tag";

	@Override
	@Transactional
	public int updateTagCount(MenuTopTags topTag) {
		Session session = mHibernateTemplate.getSessionFactory().getCurrentSession();
		try {
			Query query = session.createSQLQuery(updateTopTagsOfMenu);
			query.setParameter("latitude", topTag.getLatitude()).setParameter("tag", topTag.getTag()).setParameter("longitude", topTag.getLongitude());
			return query.executeUpdate();
		} catch (Exception e) {
			logger.error("", e);
		} finally {
		}
		return 0;
	}

	@Override
	@Transactional
	public int updateTagCount(RestaurantTopTags topTag) {
		Session session = mHibernateTemplate.getSessionFactory().getCurrentSession();
		try {
			Query query = session.createSQLQuery(updateTopTagsOfRestaurant);
			query.setParameter("latitude", topTag.getLatitude()).setParameter("tag", topTag.getTag()).setParameter("longitude", topTag.getLongitude());
			return query.executeUpdate();
		} catch (Exception e) {
			logger.error("", e);
		} finally {
		}
		return 0;
		
	}
	
	 @Override
	 @Transactional
	 public void save(PersistenceEntity object) throws RuntimeException {
		 mHibernateTemplate.save(object);
	 }
	
}
