package com.onemenu.server.daoImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.onemenu.server.dao.CustomerDAO;
import com.onemenu.server.dao.ThirdPartyAccountDAO;
import com.onemenu.server.model.Customer;


public class ThirdPartyAccountDAOImpl extends BaseDAOSupport implements ThirdPartyAccountDAO {

    protected HibernateTemplate mHibernateTemplate;

    @Autowired
    public void setmHibernateTemplate(HibernateTemplate mHibernateTemplate) {
        this.mHibernateTemplate = mHibernateTemplate;
    }

}
