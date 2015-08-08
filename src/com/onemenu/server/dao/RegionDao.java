package com.onemenu.server.dao;

public interface RegionDao extends BaseDAOInf {
	
	public void updateRestRegion(long restId,long regionId);

	public void updateDriverRegion(long driverId, long regionId);

}
