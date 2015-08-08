package com.onemenu.server.dao;

import java.util.List;

import com.onemenu.server.javabean.delivery.DDriverBean;

public interface DriverDao extends BaseDAOInf{
	
	public List<DDriverBean>  getAllDrivers();

}
