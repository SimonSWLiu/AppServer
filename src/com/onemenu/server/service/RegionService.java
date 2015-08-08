package com.onemenu.server.service;

import java.util.List;

import com.onemenu.server.javabean.admin.ARegionBean;
import com.onemenu.server.javabean.admin.ARestaurantBean;
import com.onemenu.server.model.Region;

public interface RegionService extends AbstractServiceInf {
	
	public Region saveRegion(String name);

	public List<ARegionBean> getAllRegions(Integer startNum, Integer range);

	public List<ARegionBean> getRestRegions(Integer startNum, Integer range);

	public boolean updateRestRegion(Long restId, Long regionId);

	public List<ARestaurantBean> getRests(Integer startNum, Integer range);

	public boolean updateDriverRegion(Long driverId, Long regionId);

}
