package com.onemenu.server.daoImpl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.onemenu.server.dao.BaseDAOInf;
import com.onemenu.server.model.PersistenceEntity;


/**
 * 
 * <br>
 * 类描述: <br>
 * 功能详细描述:
 * 
 * @param <T>
 * @author linhang
 * @date [2012-11-12]
 */
public class BaseDAOSupport implements BaseDAOInf {
    protected HibernateTemplate mHibernateTemplate;

    @Autowired
    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.mHibernateTemplate = hibernateTemplate;
    }

    @Override
    public void save(PersistenceEntity object) throws RuntimeException {
        this.mHibernateTemplate.save(object);

    }

    @Override
    public void delete(PersistenceEntity object) throws RuntimeException {
        this.mHibernateTemplate.delete(object);
    }

    @Override
    public void update(PersistenceEntity object) throws RuntimeException {
        this.mHibernateTemplate.update(object);
    }

    @Override
    public void saveOrUpdate(PersistenceEntity objcet) {
        mHibernateTemplate.saveOrUpdate(objcet);

    }

    @SuppressWarnings("unchecked")
	@Override
    public <T extends PersistenceEntity> List<T> findByCriteria(DetachedCriteria detachedCriteria,
            Class<T> c) {
        return (List<T>) this.mHibernateTemplate.findByCriteria(detachedCriteria);
    }

    @SuppressWarnings("unchecked")
	@Override
    public <T extends PersistenceEntity> List<T> findByCriteria(DetachedCriteria detachedCriteria,
            int first, int range, Class<T> c) {
        return (List<T>) this.mHibernateTemplate.findByCriteria(detachedCriteria, first, range);
    }

    @Override
    public <T extends PersistenceEntity> T findById(Class<T> entityClass, long id) {
        return (T) mHibernateTemplate.get(entityClass, id);
    }

    @SuppressWarnings("unchecked")
	@Override
    public <T extends PersistenceEntity> List<T> findByExample(T object) {
        return (List<T>) mHibernateTemplate.findByExample(object);
    }

    public HibernateTemplate getHibernateTemplate() {
        return mHibernateTemplate;
    }

}
