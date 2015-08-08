package com.onemenu.server.daoImpl;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.onemenu.server.dao.AccountDAO;
import com.onemenu.server.javabean.NMenuItemReviewBean;


public class AccountDAOImpl extends BaseDAOSupport implements AccountDAO {
	
	private final static Logger logger = Logger.getLogger(AccountDAOImpl.class);

    protected HibernateTemplate mHibernateTemplate;

    @Autowired
    public void setmHibernateTemplate(HibernateTemplate mHibernateTemplate) {
        this.mHibernateTemplate = mHibernateTemplate;
    }
    
    final String  token_delete = " delete  from  login_token ";

	@Override
	@Transactional
	public void deleteAllToken() {
		Session session = mHibernateTemplate.getSessionFactory().getCurrentSession();
		try {
			Query query = session.createSQLQuery(token_delete);
			query.executeUpdate();
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			// session.close();
		}
	}

}
