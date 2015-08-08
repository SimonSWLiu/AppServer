package com.onemenu.server.daoImpl;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.onemenu.server.dao.RegionDao;

@Repository("regionDao")
public class RegionDaoImpl extends BaseDAOSupport implements RegionDao {
	
	private Logger logger = Logger.getLogger(RegionDaoImpl.class);

	private final String updateRestRegionSql = "UPDATE  `restaurant`  SET  `region_id` = :regionId  WHERE `id` = :id ";
	
	@Override
	@Transactional
	public void updateRestRegion(long restId, long regionId) {
		Session session = mHibernateTemplate.getSessionFactory().getCurrentSession();
		try {
			Query query = session.createSQLQuery(updateRestRegionSql);
			query.setParameter("id", restId);
			query.setParameter("regionId", regionId);
			query.executeUpdate();
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			// session.close();
		}

	}
	
	private final String updateDriverRegionSql = "UPDATE  `driver`  SET  `region_id` = :regionId  WHERE `id` = :id ";
	
	@Override
	@Transactional
	public void updateDriverRegion(long driverId, long regionId) {
		Session session = mHibernateTemplate.getSessionFactory().getCurrentSession();
		try {
			Query query = session.createSQLQuery(updateDriverRegionSql);
			query.setParameter("id", driverId);
			query.setParameter("regionId", regionId);
			query.executeUpdate();
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			// session.close();
		}
	}

}
