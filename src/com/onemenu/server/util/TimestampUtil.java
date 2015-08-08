package com.onemenu.server.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * 日期时间工具类
 * @author file
 * @since 2015年4月5日 下午6:52:50
 * @version 1.0
 * @copyright  by onemenu
 * TimeZone timeZoneNY = TimeZone.getTimeZone("America/New_York");
 */
public class TimestampUtil {
	//日志
	private static final Logger logger = Logger.getLogger(TimestampUtil.class);
	//每秒的毫秒数
	private static final int MILLIS_PER_SECOND = 1000;
	//每分钟的毫秒数
	private static final int MILLIS_PER_MINUTE = 60 * MILLIS_PER_SECOND;
	//每小时的毫秒数
	private static final int MILLIS_PER_HOUR = 60 * MILLIS_PER_MINUTE;
	//每天的毫秒数
	private static final long MILLIS_PER_DAY = 24 * MILLIS_PER_HOUR;
	
	/**
	 * String 类型的日期格式转为日期类型，转换失败时返回null
	 * @param dateStr  String 类型的日期
	 * @param type   日期格式
	 * @return 日期类型
	 */
    public static Date strToDate(String dateStr, DateFormatType type) {
        
        try {
        	Date  date = type.getType().parse(dateStr);
            return date;
        } catch (ParseException e) {
            logger.error("", e);
           
        }
        return null;

    }
    /**
     * 日期格式转换为String类型的格式
     * @param date 日期
     * @param type 目标格式
     * @return String类型的日期
     */
    public static String dateToStr(Date date, DateFormatType type) {
        return type.getType().format(date);
    }

    /**
     * <br>
     * 功能简述: 显示day天前的日期，如今天为2012-11-20,返回两天前的日期即为2012-11-18 00:00:00 <br>
     * 功能详细描述: <br>
     * 注意:
     * @param day
     * @return
     */
    public static String getRecentlyDay(int day) {
        long time = MILLIS_PER_DAY * day;
        Date thatDay = new Date(System.currentTimeMillis() - time);
        return dateToStr(thatDay, DateFormatType.Date) + " 00:00:00";
    }
    
    /**
     * 获取Timestamp类型的当前时间
     * @return
     */
    public static Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }
    
    
    // 0:Sun, 1:Mon, 2:Tue, 3:Wed, 4:Thu, 5:Fri, 6:Sat
    public static int getCurrentWeek() {
        Calendar cal = Calendar.getInstance();
        int week = cal.get(Calendar.DAY_OF_WEEK) - 1;
        return week;
    }
    
    /**
     * 获取Timestamp类型的当前时间
     * @return
     */
    public static Timestamp getMinIntervalTimestamp(int minInterval) {
        long time = MILLIS_PER_MINUTE * minInterval;
        return new Timestamp(System.currentTimeMillis() + time);
    }
    
    /**
     * 把当前日期转成yyyy-MM-dd HH:mm:ss格式
     * @return
     */
    public static String formatTimestamp(){
    	return formatTimestamp(getCurrentTimestamp());
    }
    
    
    /**
     * 把日期转成yyyy-MM-dd HH:mm:ss格式
     * @param timestamp  日期
     * @return
     */
    public static String formatTimestamp(Date timestamp) {
        return dateToStr(timestamp, DateFormatType.DateTime);
    }
    
    
    
    //TODO  by file 2015-04-05
    public static String formatDate(Date date, String strFormat) {

        String dateString = null;
        
        try {
            SimpleDateFormat format = new SimpleDateFormat(strFormat);
            dateString = format.format(date);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        
        return dateString;
    }
    /**
     * 把yyyy-MM-dd格式String 转成日期
     * @param dateString  yyyy-MM-dd格式日期
     * @return
     */
    public static Date formatStringToDate(String dateString) {
        return strToDate(dateString, DateFormatType.Date);
    }
    /**
     * 把hh:mm格式String 转成日期
     * @param dateString  yyyy-MM-dd格式日期
     * @return
     */
    public static Date formatStringToHourMinute12(String timeString) {
        return strToDate(timeString, DateFormatType.HourMinute12);
    }
    
    
    /**
     * 两个时间点时间间隔用分钟表示
     * @param before
     * @param after
     * @return 时间间隔用分钟表示
     */
    public static String timeIntervalInMin(Timestamp before,Timestamp after){
    	if(before==null||after==null)
    		return "0";
    	return timeIntervalInMin(before.getTime(), after.getTime());
    }
    /**
     * 两个时间点时间间隔用分钟表示
     * @param before
     * @param after
     * @return 时间间隔用分钟表示
     */
    public static String timeIntervalInMin(long before,long after){
    	return String.valueOf((after-before)/MILLIS_PER_MINUTE);
    }
    
    
    
    
    
    

    /**
     * 日期格式枚举
     * @author file
     * @since 2015年4月5日 下午7:01:41
     * @version 1.0
     * @copyright  by onemenu
     */
    public enum DateFormatType {
    	HourMinute12,
    	HourMinute24,
        DateTime,
        DateTimeChinese,
        Date,
        DateNoHyphen,
        DateChinese,
        SimpleDateChinese,
        Time24,
        Time12,
        Year,
        Month,
        Day,
        Hour24,
        Hour12,
        Minute,
        Second,
        DateTimeToMin;
        
    	public SimpleDateFormat getType(){
        	switch (this) {
        	case Date:
				return new SimpleDateFormat("yyyy-MM-dd");
			case DateTime:
				return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			case DateNoHyphen:
        		return new SimpleDateFormat("yyyyMMdd");
			case HourMinute12:
        		return new SimpleDateFormat("hh:mm");
			case Year:
        		return new SimpleDateFormat("yyyy");
			case Month:
        		return new SimpleDateFormat("mm");
			case Day:
        		return new SimpleDateFormat("dd");
			case Hour24:
        		return new SimpleDateFormat("HH");
			case Hour12:
        		return new SimpleDateFormat("hh");
			case Minute:
        		return new SimpleDateFormat("mm");
			case Second:
        		return new SimpleDateFormat("ss");
			case DateTimeChinese:
        		return new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
			case DateChinese:
        		return new SimpleDateFormat("yyyy年MM月dd日");
			case SimpleDateChinese:
        		return new SimpleDateFormat("MM月dd日");
        	case Time24:
        		return new SimpleDateFormat("HH:mm:ss");
        	case Time12:
        		return new SimpleDateFormat("hh:mm:ss");
        	case HourMinute24:
        		return new SimpleDateFormat("HH:mm");
        	case DateTimeToMin:
        		return new SimpleDateFormat("yyyy-MM-dd HH:mm");
			default:
				 return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			}
        }
    }
    
    
    //测试代码
    public static void main(String[] arg){
    	System.out.println(strToDate("2015-04-05", DateFormatType.Date));
    	System.out.println(strToDate("2015-04-05 12:00:00", DateFormatType.DateTime));
    	System.out.println(dateToStr(new Date(), DateFormatType.Date));
    	System.out.println(dateToStr(new Date(), DateFormatType.DateTime));
    	System.out.println(dateToStr(new Date(), DateFormatType.HourMinute12));
    	System.out.println(dateToStr(new Date(), DateFormatType.HourMinute24));
    	System.out.println(formatTimestamp(getCurrentTimestamp()));
    	System.out.println(formatStringToDate("2015-04-05"));
    	System.out.println(formatStringToHourMinute12("12:00:00"));
    	
    	System.out.println(dateToStr(new Date(), DateFormatType.Time12));
    	System.out.println(dateToStr(new Date(), DateFormatType.Time24));
    	
    	
    	Timestamp before = getCurrentTimestamp();
		Timestamp after = new Timestamp(System.currentTimeMillis() + MILLIS_PER_HOUR);
		System.out.println(timeIntervalInMin(before, after));
    	
    }
    
    
    

}
