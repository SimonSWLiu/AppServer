package com.onemenu.server.daoImpl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.onemenu.server.dao.DriverDao;
import com.onemenu.server.javabean.delivery.DDriverBean;

@Repository("driverDao")
public class DriverDaoImpl extends BaseDAOSupport implements DriverDao {
	
	private final String ALL_DRIVERS_SQL= "SELECT id AS driverId,`name` AS  driverName,`status` AS status  FROM `driver`";
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<DDriverBean> getAllDrivers() {
		Session session = mHibernateTemplate.getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(ALL_DRIVERS_SQL);
		return query.setResultTransformer(new AliasToBeanResultTransformer(DDriverBean.class)).list();
	}

}
