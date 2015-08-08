package com.onemenu.server.serviceImpl;



import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.onemenu.server.daoImpl.BaseDAOSupport;
import com.onemenu.server.model.PersistenceEntity;
import com.onemenu.server.service.AbstractServiceInf;



/**
 * 
 * <br>
 * 类描述:所有Service类的父类，负责实现一些公共的方法 <br>
 * 功能详细描述:
 * 
 * @author linhang
 * @date [2012-9-4]
 * @param <T> 具体的实现子类
 */
public class AbstractServiceImpl implements AbstractServiceInf {

    private BaseDAOSupport mBaseDAO;

    @Autowired
    public void setBaseDAO(BaseDAOSupport mbaseDAO) {
        this.mBaseDAO = mbaseDAO;
    }

    public BaseDAOSupport getBaseDAO() {
        return mBaseDAO;
    }
    
    @Override
    @Transactional
    public void saveTrd(PersistenceEntity object) throws RuntimeException {
        mBaseDAO.save(object);
    }


    @Override
    public void save(PersistenceEntity object) throws RuntimeException {
        mBaseDAO.save(object);
    }

    @Override
    @Transactional
    public void saveOrUpdate(PersistenceEntity object) throws RuntimeException {
        mBaseDAO.saveOrUpdate(object);
    }

    @Override
    @Transactional
    public void deleteTrd(PersistenceEntity object) throws RuntimeException {
        mBaseDAO.delete(object);
    }

    @Override
    @Transactional
    public void updateTrd(PersistenceEntity object) throws RuntimeException {
        mBaseDAO.update(object);
    }

    @Override
    public <T extends PersistenceEntity> T findById(Class<T> entityClass, long id) {
        return mBaseDAO.findById(entityClass, id);
    }

    @Override
    public <T extends PersistenceEntity> List<T> findByExample(T object) {
        // TODO Auto-generated method stub
        return mBaseDAO.findByExample(object);
    }


    @Override
    public <T extends PersistenceEntity> List<T> findByPage(int start, int range, Class<T> class_) {
        DetachedCriteria cri = DetachedCriteria.forClass(class_);
        return mBaseDAO.findByCriteria(cri, start, range, class_);
    }


    @Override
    public <T extends PersistenceEntity> List<T> findAll(Class<T> class_) {
        DetachedCriteria cri = DetachedCriteria.forClass(class_);
        return mBaseDAO.findByCriteria(cri, class_);
    }

    @Override
    public void delete(PersistenceEntity object) throws RuntimeException {
        mBaseDAO.delete(object);
    }


    @Override
    @Transactional
    public void update(PersistenceEntity object) throws RuntimeException {
        mBaseDAO.update(object);
    }

    @Override
    public <T extends PersistenceEntity> long findRowCount(Class<T> class_, String key, Object value) {
        DetachedCriteria cri = DetachedCriteria.forClass(class_);
        cri.setProjection(Projections.rowCount());

        cri.add(Restrictions.eq(key, value));

        List<?> results = mBaseDAO.findByCriteria(cri, class_);

        long count = ((Long) results.get(0)).intValue();

        return count;
    }

    @Override
    public <T extends PersistenceEntity> long findRowCount(Class<T> class_,
            Map<String, Object> criMap) {

        DetachedCriteria cri = DetachedCriteria.forClass(class_);
        cri.setProjection(Projections.rowCount());

        cri.add(Restrictions.allEq(criMap));

        List<?> results = mBaseDAO.findByCriteria(cri, class_);

        long count = ((Long) results.get(0)).intValue();

        return count;
    }

}
