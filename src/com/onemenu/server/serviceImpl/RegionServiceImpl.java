package com.onemenu.server.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.onemenu.server.dao.RegionDao;
import com.onemenu.server.javabean.admin.ARegionBean;
import com.onemenu.server.javabean.admin.ARestaurantBean;
import com.onemenu.server.model.Driver;
import com.onemenu.server.model.Region;
import com.onemenu.server.model.Restaurant;
import com.onemenu.server.service.RegionService;
import com.onemenu.server.util.TimestampUtil;

/**
 * 
 * @author file
 * @since 2015-7-24 下午4:57:52
 * @version 1.0
 */
@Service("regionService")
public class RegionServiceImpl extends AbstractServiceImpl implements RegionService {
	
	private static final Logger logger = Logger.getLogger(RegionServiceImpl.class);
	@Resource(name = "regionDao")
	private RegionDao regionDao;
	@Override
	@Transactional
	public Region saveRegion(String name) {
		Region region = null;
		try{
			region = new Region();
			region.setName(name);
			region.setCreateTimestamp(TimestampUtil.getCurrentTimestamp());
			save(region);
		}catch(Exception e){
			logger.error("", e);
			region = null;
		}
		return region;
	}

	@Override
	public List<ARegionBean> getAllRegions(Integer startNum, Integer range) {
		
		List<Region> regions = findByPage(startNum,range,Region.class);
		if(regions==null||regions.isEmpty())
			return null;
		List<ARegionBean> beans = new ArrayList<ARegionBean>(regions.size());
		
		for(Region r :regions){
			ARegionBean bean = new ARegionBean();
			bean.regionId = String.valueOf(r.getId());
			bean.regionName = r.getName();
			beans.add(bean);
		}
		
		return beans;
	}

	@Override
	@Transactional
	public List<ARegionBean> getRestRegions(Integer startNum, Integer range) {
		List<Region> regions = findByPage(startNum, range, Region.class);
		if(regions==null||regions.isEmpty())
			return null;
		List<ARegionBean> beans = new ArrayList<ARegionBean>(regions.size());
		
		for(Region r :regions){
			ARegionBean bean = new ARegionBean();
			bean.regionId = String.valueOf(r.getId());
			bean.regionName = r.getName();
			beans.add(bean);
			
			Set<Restaurant> restaurants = r.getRestaurants();
		
			if(restaurants==null||restaurants.isEmpty()){
				bean.restaurants = new ArrayList<ARestaurantBean>();
				continue;
			}
			
			bean.restaurants = new ArrayList<ARestaurantBean>(restaurants.size());
			for(Restaurant rest:restaurants){
				ARestaurantBean  restBean = new ARestaurantBean();
				restBean.restId = String.valueOf(rest.getmId());
				restBean.restName = rest.getmName();
				bean.restaurants.add(restBean);
			}			
		}
		
		return beans;
	}

	@Override
	@Transactional
	public boolean updateRestRegion(Long restId, Long regionId) {
		boolean result = false;
		try{
			Restaurant restaurant = findById(Restaurant.class, restId);
			if(restaurant==null)
				return result;
			Region region = findById(Region.class, regionId);
			if(region==null)
				return result;
			//restaurant.setmRegion(region);
			//update(restaurant);
			regionDao.updateRestRegion(restId, regionId);
			result = true;
		}catch(Exception e){
			logger.error("", e);
		}
		return result;
	}

	@Override
	public List<ARestaurantBean> getRests(Integer startNum, Integer range) {
		
		DetachedCriteria cri = DetachedCriteria.forClass(Restaurant.class);
		cri.addOrder(Order.asc("mRegion.id"));
        List<Restaurant> restaurants =  getBaseDAO().findByCriteria(cri, startNum, range, Restaurant.class);
		if(restaurants==null||restaurants.isEmpty()){
			return null;
		}
        List<ARestaurantBean>  _restaurants = new ArrayList<ARestaurantBean>(restaurants.size());
		for(Restaurant rest:restaurants){
			ARestaurantBean  restBean = new ARestaurantBean();
			restBean.restId = String.valueOf(rest.getmId());
			restBean.restName = rest.getmName();
			restBean.restLatitude = String.valueOf(rest.getmLatitude());
			restBean.restLongitude = String.valueOf(rest.getmLongitude());
			Region region = rest.getmRegion();
			if(region!=null){
				restBean.regionId = String.valueOf(region.getId());
				restBean.regionName = region.getName();
			}
			_restaurants.add(restBean);
		}			
        return _restaurants;
	}

	@Override
	@Transactional
	public boolean updateDriverRegion(Long driverId, Long regionId) {
		boolean result = false;
		try{
			Driver driver = findById(Driver.class, driverId);
			if(driver==null)
				return result;
			Region region = findById(Region.class, regionId);
			if(region==null)
				return result;
			regionDao.updateDriverRegion(driverId, regionId);
			result = true;
		}catch(Exception e){
			logger.error("", e);
		}
		return result;
	}

	

}
