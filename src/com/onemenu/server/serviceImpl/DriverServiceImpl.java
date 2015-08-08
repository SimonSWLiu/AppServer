package com.onemenu.server.serviceImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.onemenu.server.constant.ParameterConstant;
import com.onemenu.server.dao.DriverDao;
import com.onemenu.server.dao.OrderFormDAO;
import com.onemenu.server.javabean.delivery.DDriverBean;
import com.onemenu.server.model.Driver;
import com.onemenu.server.model.Region;
import com.onemenu.server.service.DriverService;
import com.onemenu.server.util.StringUtil;
import com.onemenu.server.util.awsnotification.SNSMobilePush;

@Service("driverService")
public class DriverServiceImpl extends AbstractServiceImpl implements DriverService {

	private static final Logger logger = Logger.getLogger(DriverServiceImpl.class);
	@Resource(name = "driverDao")
	private DriverDao driverDao;
	private OrderFormDAO	mOrderFormDAO;
	@Autowired
	public void setmOrderFormDAO(OrderFormDAO mOrderFormDAO) {
		this.mOrderFormDAO = mOrderFormDAO;
	}
	
	@Override
	public List<Driver> getDriversByStatus(Integer status) {
		DetachedCriteria driverCri = DetachedCriteria.forClass(Driver.class);
		driverCri.add(Restrictions.eq("mStatus", status));
		List<Driver> list = getBaseDAO().findByCriteria(driverCri, Driver.class);
		return list;
	}
	
	public void pushToEndpoint(Driver driver, String message) throws Exception {
		if(StringUtil.isEmpty(driver.getDeviceToken()))
			return ;
		Map<String, Object> customData = new HashMap<String, Object>();
		customData.put("driverId",String.valueOf(driver.getmId()));
		SNSMobilePush.pushIOSNotification(message, driver.getDeviceToken(), customData,0);
	}

	@Override
	public void pushToEndpoints(Collection<Driver> drivers, String message) throws Exception {
		for(Driver r:drivers){
			pushToEndpoint(r, message);
		}
	}

	@Override
	public Object pushToEndpoint(String driverId, String orderId, String sign) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void pushToEndpoint(Driver driver, String message, int orderSize) throws Exception{
		if(StringUtil.isEmpty(driver.getDeviceToken())){
			logger.error("driver token is null ,driver name is :"+driver.getmName());
			return ;
		}
			
		
		
		
		
		Map<String, Object> customData = new HashMap<String, Object>();
		customData.put("driverId",String.valueOf(driver.getmId()));
		
		/*
		Set<DeviceToken>  tokens =  driver.getmAccount().getDeviceTokenSet();
		
		if(tokens==null||tokens.isEmpty())
			return;
		
		for(DeviceToken token :tokens){
			if(ParameterConstant.LOGIN.equals(token.getLogin())){
				
				if(ParameterConstant.DEVICE_IOS.equals(token.getDevice())){
					SNSMobilePush.pushAndroidNotification(message, token.getToken(), new HashMap<String, Object>(customData));
				}else{
					SNSMobilePush.pushIOSNotification(message, token.getToken(), new HashMap<String, Object>(customData),orderSize);
				}
				
				
			}
			
		}*/
		SNSMobilePush.pushIOSNotification(message, driver.getDeviceToken(), customData,orderSize);
	}

	@Override
	@Transactional
	public boolean updateOnDuty(long driverId, String isOnDuty) {
		
		Driver driver = getBaseDAO().findById(Driver.class, driverId);
		if(ParameterConstant.DRIVER_ONDUTY_YES.equals(isOnDuty)){
			//获取driver 手上派送单的数量
			int  count = mOrderFormDAO.getDeliveryOrderCountByDriverId(driverId);
			if(count==0)
				driver.setmStatus(ParameterConstant.DRIVER_STATUS_AVAILIABLE);
			else
				driver.setmStatus(ParameterConstant.DRIVER_STATUS_ON_DELIVERY);
			
			 getBaseDAO().update(driver);
			 return true;	
		}else if(ParameterConstant.DRIVER_ONDUTY_NO.equals(isOnDuty)){
			 driver.setmStatus(ParameterConstant.DRIVER_STATUS_UNAVAILIABLE);
			 getBaseDAO().update(driver);
			 return true;
		}
		
		
		/*
		
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(DeliveryTimeSheet.class);
		
		Timestamp curTime = TimestampUtil.getCurrentTimestamp();
		
		String time = TimestampUtil.dateToStr(curTime, DateFormatType.Time24);
		String date = TimestampUtil.dateToStr(curTime, DateFormatType.Date);
		
		detachedCriteria.add(Restrictions.ge("mAvailableStartTime", time));
		detachedCriteria.add(Restrictions.le("mAvailableEndTime", time));
		detachedCriteria.add(Restrictions.eq("mAvailableDate", date));
				
		List<DeliveryTimeSheet> timeSheets = getBaseDAO().findByCriteria(detachedCriteria , DeliveryTimeSheet.class);
		
		if(timeSheets == null||timeSheets.isEmpty())
			return false;
		
		 //Set<Driver> drivers = new HashSet<Driver>();
		 
		 for(DeliveryTimeSheet timeSheet:timeSheets){
			 
			 for(Driver driver:timeSheet.getmDriverSet()){
				 //driver 的状态是UNAVAILIABLE
				 if(driver.getmId()==driverId && ParameterConstant.DRIVER_STATUS_UNAVAILIABLE == driver.getmStatus()){
					 //上班
					 if(ParameterConstant.DRIVER_ONDUTY_YES.equals(isOnDuty)){
						 driver.setmStatus(ParameterConstant.DRIVER_STATUS_AVAILIABLE);
						 getBaseDAO().update(driver);
						 return true;
					 }
				 }else if(driver.getmId()==driverId){
					 //下班
					 if(ParameterConstant.DRIVER_ONDUTY_NO.equals(isOnDuty)){
						 driver.setmStatus(ParameterConstant.DRIVER_STATUS_UNAVAILIABLE);
						 getBaseDAO().update(driver);
						 return true;
					 }
				 }
			 }
			 
		 }
		 */
		 
		 
		return false;
	}

	@Override
	@Transactional
	public List<DDriverBean> getAllDrivers() {
	
		List<Driver> drivers = findAll(Driver.class);
		
		//List<DDriverBean> ddrivers = driverDao.getAllDrivers();
		
		if(drivers==null||drivers.isEmpty())
			return null;
		List<DDriverBean> ddrivers = new ArrayList<DDriverBean>(drivers.size());
		
		for(Driver driver:drivers){
			DDriverBean _driver = new DDriverBean();
			
			if(driver.getmStatus()== ParameterConstant.DRIVER_STATUS_RESIGNED||driver.getmStatus()==ParameterConstant.DRIVER_STATUS_UNAVAILIABLE){
				_driver.isOnDuty = ParameterConstant.DRIVER_ONDUTY_NO;
			}else{
				_driver.isOnDuty = ParameterConstant.DRIVER_ONDUTY_YES;
			}
			_driver.driverId = String.valueOf(driver.getmId());
			_driver.driverName = driver.getmName();
			Region region = driver.getmRegion();
			if(region!=null){
				_driver.regionId = String.valueOf(region.getId());
				_driver.regionName = region.getName();
			}
			ddrivers.add(_driver);
		}
		
		return ddrivers;
	}

	@Override
	@Transactional
	public void updataLocation(long driverId, double longitude, double latitude) {
		
		Driver driver = getBaseDAO().findById(Driver.class, driverId);
		if(driver==null)
			return;
		driver.setLatitude(latitude);
		driver.setLongitude(longitude);
		getBaseDAO().update(driver);
	}

	@Override
	@Transactional
	public void checkDriversStatus(Collection<Driver> unconfimDrivers) {
		if(unconfimDrivers==null||unconfimDrivers.isEmpty())
			return;
		for(Driver driver:unconfimDrivers){
			//获取driver 手上派送单的数量
			int  count = mOrderFormDAO.getDeliveryOrderCountByDriverId(driver.getmId());
			if(count==0){
				driver.setmStatus(ParameterConstant.DRIVER_STATUS_AVAILIABLE);
				getBaseDAO().update(driver);
			}			
		}
		
	}


}
