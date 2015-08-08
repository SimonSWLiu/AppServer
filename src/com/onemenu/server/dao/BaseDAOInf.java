package com.onemenu.server.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import com.onemenu.server.model.PersistenceEntity;

/**
 * 
 * <br>
 * 类描述: <br>
 * 功能详细描述:
 * 
 * @param <T>
 * @author linhang
 * @date [2012-9-4]
 */
public interface BaseDAOInf {
    
    public void save(PersistenceEntity object) throws RuntimeException;

    public void delete(PersistenceEntity object) throws RuntimeException;

    /* T find(T object) throws RuntimeException; */

    public void update(PersistenceEntity object) throws RuntimeException;

    public <T extends PersistenceEntity> T findById(Class<T> entityClass, long id);

    public void saveOrUpdate(PersistenceEntity objcet);

    public <T extends PersistenceEntity> List<T> findByCriteria(DetachedCriteria detachedCriteria,
            Class<T> c);

    public <T extends PersistenceEntity> List<T> findByCriteria(DetachedCriteria detachedCriteria,
            int first, int range, Class<T> c);

    public <T extends PersistenceEntity> List<T> findByExample(T object);
}
