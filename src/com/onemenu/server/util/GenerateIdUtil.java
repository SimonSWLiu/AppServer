package com.onemenu.server.util;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import com.onemenu.server.cache.ConfigCache;
import com.onemenu.server.constant.ParameterConstant;
import com.onemenu.server.constant.ParameterConstant.ConfigKey;
import com.onemenu.server.service.ConfigService;
import com.onemenu.server.service.OrderFormService;
import com.onemenu.server.util.TimestampUtil.DateFormatType;
/**
 * id生成工具类
 * @author file
 * @since 2015年5月10日 下午4:56:10
 * @version 1.0
 * @copyright  by onemenu
 */
public class GenerateIdUtil {
	
	private static AtomicInteger orderCode;
	//private static final Logger logger = Logger.getLogger(GenerateIdUtil.class);
	//private static final String FILE_NAME = "GenerateIdUtil.txt";
	//private static final String ENCODING = "UTF-8";
	//private static final String ORDER_CODE_KEY = "order_code";
	
	
	public static void startUp(OrderFormService orderFormService, String app){
		/*
		File file = new File(FILE_NAME);
		if(file.exists()){
			try {
				String content = FileUtils.readFileToString(file, ENCODING);
				JSONObject jo = JSON.parseObject(content);
				orderCode = new AtomicInteger(jo.getIntValue(ORDER_CODE_KEY));
				
			} catch (IOException e) {
				logger.error("", e);
			}
		}*/
		/*
		if(orderCode==null)
			orderCode = new AtomicInteger();
		*/
		
		orderCode = new AtomicInteger(Integer.valueOf(ConfigCache.getValue(ConfigKey.ORDER_FORM_CODE, "0")));
		if(StringUtil.isEmpty(app)||ParameterConstant.APP_DEV.equals(app))
			return;
		
		
		
		
		while(true){
			String _orderCode = "";
			if(ParameterConstant.APP_DELIVERY.equals(app)){
				_orderCode = getThirdPartyOrderCode();
			}else if(ParameterConstant.APP_USER.equals(app))
				_orderCode =getOneMenuOrderCode();
			
			boolean result = orderFormService.checkOrderCode(_orderCode);
			if(result){
				orderCode.decrementAndGet();
				break;
			}
		}
		
	}
	
	public static void shutdown(ConfigService configService){
		
		configService.saveOrUpdate(ConfigKey.ORDER_FORM_CODE, String.valueOf(orderCode.get()));
		
		/*Map<String,Object>  content = new HashMap<String,Object>();
		content.put(ORDER_CODE_KEY, orderCode.get());
		File file = new File(FILE_NAME);
		if(!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e) {
				logger.error("", e);
			}
		if(file.exists()){
			try {
				FileUtils.writeStringToFile(file, JSON.toJSONString(content), ENCODING);
			} catch (IOException e) {
				logger.error("", e);
			}
		}*/
		
	}
	
	public static void enterNextDay(){
		orderCode.set(0);
	}
	
	
	public static String getOrderFromCode(){
		 String dateStr = TimestampUtil.dateToStr(new Date(), DateFormatType.DateNoHyphen);//modify by file  统一使用新的时间工具类
	     int serial = orderCode.incrementAndGet();
		 String serialStr = FormatUtils.formatNumStr(serial, "000000");
	     return dateStr+serialStr;
	}
	
	
	public static String getThirdPartyOrderCode(){
		return "0" + getOrderFromCode();
	}
	
	public static String getOneMenuOrderCode(){
		return "1" + getOrderFromCode();
	}
	

}
