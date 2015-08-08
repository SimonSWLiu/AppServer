package com.onemenu.server.listener;

import javax.servlet.ServletContextEvent;

import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.quartz.Scheduler;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ContextLoaderListener;

import com.onemenu.server.cache.LoginTokenMgr;
import com.onemenu.server.dao.DishDAO;
import com.onemenu.server.service.AccountService;
import com.onemenu.server.service.ConfigService;
import com.onemenu.server.service.OrderFormService;
import com.onemenu.server.util.GenerateIdUtil;
import com.onemenu.server.util.TimestampUtil;
/**
 * onemenu app server的上下文监听器
 * @author file
 * @since 2015-4-7 下午3:59:01
 * @version 1.0
 */
public class OneMenuContextLoaderListener extends ContextLoaderListener {
	
	private static final Logger logger = Logger.getLogger(OneMenuContextLoaderListener.class);

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		
		ConfigService  configService = ContextHelp.context.getBean("configService", ConfigService.class);
		GenerateIdUtil.shutdown(configService);
		
		//停服序列化loginToken并保存
		AccountService accountService = ContextHelp.context.getBean("accountService", AccountService.class);
		LoginTokenMgr.shutdown(accountService);
		
		
		 // Get a reference to the Scheduler and shut it down
        Scheduler scheduler = (Scheduler) ContextHelp.context.getBean("schedulerFactory",Scheduler.class);
        try {
			scheduler.shutdown(true);
			 // Sleep for a bit so that we don't get any errors
	        Thread.sleep(1000);
		} catch (Exception e) {
			logger.error("", e);			
		}
       
		
		
		super.contextDestroyed(event);
		
		
		
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		if(logger.isDebugEnabled())
			logger.debug("begin~~~~~ time:"+TimestampUtil.formatTimestamp());
		super.contextInitialized(event);
		if(logger.isDebugEnabled())
			logger.debug("end~~~~~");
		
		//加载配置数据
		ConfigService  configService = ContextHelp.context.getBean("configService", ConfigService.class);
		configService.loadAll();
		
		//启动初始化loginToken
		AccountService accountService = ContextHelp.context.getBean("accountService", AccountService.class);
		LoginTokenMgr.startUp(accountService);
		
		
		
		//修复数据，只需修复一次
		String fixedData = event.getServletContext().getInitParameter("fixedData");
		if(!TextUtils.isEmpty(fixedData)){
			boolean isFixedData  = Boolean.parseBoolean(fixedData) ;
			if(isFixedData){
				DishDAO  dishDAO = ContextHelp.context.getBean("dishDao",DishDAO.class);
				dishDAO.fixedData();
			}
		}
		
		String app = event.getServletContext().getInitParameter("app");
		
		//校验orderCode
		OrderFormService  orderService = ContextHelp.context.getBean("orderFormService", OrderFormService.class);
		GenerateIdUtil.startUp(orderService,app);
		
		
		
		
	}
	
	protected static class ContextHelp{
		public static final ApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
	}

}
