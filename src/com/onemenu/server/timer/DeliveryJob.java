package com.onemenu.server.timer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.onemenu.server.constant.ParameterConstant;
import com.onemenu.server.model.Driver;
import com.onemenu.server.model.OrderForm;
import com.onemenu.server.model.Region;
import com.onemenu.server.model.Restaurant;
import com.onemenu.server.service.DriverService;
import com.onemenu.server.service.OrderFormService;
/**
 * 定时扫单进行派单并推送
 * @author file
 * @since 2015-6-18 下午12:03:26
 * @version 1.0
 */
public class DeliveryJob extends QuartzJobBean {

	private static final Logger logger = Logger.getLogger(DeliveryJob.class);
	@Resource(name = "driverService")
	private DriverService mDriverService;
	@Resource(name = "orderFormService")
	private OrderFormService mOrderFormService;
	// 开始计时时间
	private static long lastTime = 0;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

		if (logger.isDebugEnabled())
			logger.debug("DeliveryJob  begin");
		List<OrderForm> orders = mOrderFormService.getCallDeliveryOrders(); //mOrderFormService.orderByStatus(ParameterConstant.ORDER_FORM_STATUS_CALL_DELIVERY);
		long curTime = System.currentTimeMillis();
		try {
			int size = orders.size();
			if(lastTime==0 && (size==1||size==2))
				lastTime = System.currentTimeMillis();
			else if(((size==1||size==2) && (curTime - lastTime) > 30 * 1000)||size>2 ){
				//drivers状态
				List<Driver> drivers = mDriverService.getDriversByStatus(ParameterConstant.DRIVER_STATUS_AVAILIABLE);

				// 找不到driver 或订单为空
				if (drivers.isEmpty() || orders.isEmpty()){
					logger.error("drivers size:"+drivers.size() +  ",orders size:"+orders.size());
					return;
				}
				//按照区域保存派送员，方便根据餐厅区域来获取派送员
				Map<Long,List<Driver>> legionDrivers = new HashMap<Long, List<Driver>>();
				for(Driver driver:drivers){
					Region r = driver.getmRegion();
					Long regionId = null;
					if(r!=null){
						regionId = r.getId();
					}
					List<Driver> _drivers = legionDrivers.get(regionId);
					if(_drivers==null){
						_drivers = new ArrayList<Driver>();
						legionDrivers.put(regionId, _drivers);
					}
					_drivers.add(driver);
					
				}
				
				
				//按照区域保存订单
				Map<Long,List<OrderForm>> legionOrders = new HashMap<Long, List<OrderForm>>();
				
				for(OrderForm order:orders){
					Restaurant rest = order.getmRestaurant();
					Long regionId = null;
					if(rest!=null){
						Region r = rest.getmRegion();
						if(r!=null){
							regionId = r.getId();
						}
						
					}
					List<OrderForm> _orders = legionOrders.get(regionId);
					if(_orders==null){
						_orders = new ArrayList<OrderForm>();
						legionOrders.put(regionId, _orders);
					}
					_orders.add(order);
				}
				
				//获取order 区域key
				Set<Long> legionKeys =legionOrders.keySet();
				//保存未确认order的driver，用来修改状态和推送
				Map<Long,Driver> unconfimDrivers = new HashMap<Long,Driver>();
				
				for(Long legionKey:legionKeys){
					
					//获取某个区域的订单
					List<OrderForm> _orders = legionOrders.get(legionKey);
					
					if(_orders==null||_orders.isEmpty())
						continue;
					//获取这个区域的派送员
					List<Driver> _drivers = legionDrivers.get(legionKey);
					
					if(_drivers==null||_drivers.isEmpty())
						continue;
					
					//暂时随机找一个出来
					int index = (int) (Math.random() * (_drivers.size() - 1));
					//随机一个
					Driver driver = _drivers.get(index);
					//循环订单关联
					for (OrderForm order : _orders){
						Driver _driver = order.getmDriver();
						//获取未确认的driver
						if(_driver!=null){
							unconfimDrivers.put(_driver.getmId(),_driver);
						}
						mOrderFormService.assignOrderFormToDriver(order.getmId(), driver.getmId());
					}
					//设置派送员的状态
				   driver.setmStatus(ParameterConstant.DRIVER_STATUS_ON_DELIVERY);
					
				   mDriverService.saveOrUpdate(driver);
					//推送
					try {
						mDriverService.pushToEndpoint(driver, "you have new order",_orders.size());
					} catch (Exception e) {
						logger.error("", e);
					}
					
				}
				
				try {
					mDriverService.checkDriversStatus(unconfimDrivers.values());
					mDriverService.pushToEndpoints(unconfimDrivers.values(), "your unconfim orders be canceled ");
				} catch (Exception e) {
					logger.error("", e);
				}
				
				lastTime = 0;
				
				
			}
			
		} finally {
			if (logger.isDebugEnabled())
				logger.debug("DeliveryJob  end");
		}

	}

	public DriverService getmDriverService() {
		return mDriverService;
	}

	public void setmDriverService(DriverService mDriverService) {
		this.mDriverService = mDriverService;
	}

	public OrderFormService getmOrderFormService() {
		return mOrderFormService;
	}

	public void setmOrderFormService(OrderFormService mOrderFormService) {
		this.mOrderFormService = mOrderFormService;
	}

}
