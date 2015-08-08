package com.onemenu.server.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.onemenu.server.model.PersistenceEntity;


/**
 * 
 * <br>
 * 类描述: 所有Service接口的公共接口 <br>
 * 功能详细描述:
 * 
 * @param <T>
 * @author linhang
 * @date [2012-9-4]
 */
public interface AbstractServiceInf {
    /**
     * 公共日志
     */
    Logger LOGGER = LoggerFactory.getLogger(AbstractServiceInf.class);

    public void save(PersistenceEntity object) throws RuntimeException;

    public void saveTrd(PersistenceEntity object) throws RuntimeException;

    public void delete(PersistenceEntity object) throws RuntimeException;

    public void deleteTrd(PersistenceEntity object) throws RuntimeException;

    /* T find(T object) throws RuntimeException; */

    public void update(PersistenceEntity object) throws RuntimeException;

    public void updateTrd(PersistenceEntity object) throws RuntimeException;

    public <T extends PersistenceEntity> T findById(Class<T> entityClass, long id);

    public void saveOrUpdate(PersistenceEntity object);

    public <T extends PersistenceEntity> List<T> findByExample(T object);

    public <T extends PersistenceEntity> List<T> findByPage(int start, int range, Class<T> class_);

    public <T extends PersistenceEntity> List<T> findAll(Class<T> class_);

    public <T extends PersistenceEntity> long findRowCount(Class<T> class_, String key, Object value);
    
    public <T extends PersistenceEntity> long findRowCount(Class<T> class_, Map<String,Object> criMap);
}
