package com.onemenu.server.service;

import java.util.Collection;
import java.util.List;

import com.onemenu.server.javabean.delivery.DDriverBean;
import com.onemenu.server.model.Driver;



public interface DriverService extends AbstractServiceInf {

    /**
     * 推送消息
     * @param driver  接受者
     * @param message 消息
     */
	public void pushToEndpoint(Driver driver,String message) throws Exception;

	
	public List<Driver> getDriversByStatus(Integer status);

	/**
	 * 群推送
	 * @param drivers
	 * @param message
	 * @throws Exception
	 */
	public void pushToEndpoints(Collection<Driver> drivers,String message) throws Exception;
	
	/**
	 * 推送消息
	 * @param driverId  driver
	 * @param orderId   新单
	 * @param sign      加密校验
	 * @return 返回数据
	 * @throws Exception 
	 */
	public Object pushToEndpoint(String driverId,String orderId,String sign) throws Exception;


	public void pushToEndpoint(Driver driver, String message, int orderSize) throws Exception;
	
	/**
	 * 更新driver状态
	 * @param driverId
	 * @param isOnDuty
	 * @return
	 */
	public boolean updateOnDuty(long driverId, String isOnDuty);
	
	/**
	 * 获取所有driver
	 * @return
	 */
	public List<DDriverBean> getAllDrivers();
	
	
	public void updataLocation(long driverId,double longitude,double latitude);

	/**
	 * check 状态
	 * @param unconfimDrivers
	 */
	public void checkDriversStatus(Collection<Driver> unconfimDrivers);

}
