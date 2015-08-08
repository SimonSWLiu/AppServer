package com.onemenu.server.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * <br>
 * 类描述: 对日期进行相关操作的类 <br>
 * 功能详细描述:
 * 
 * @author linhang
 * @date [2012-10-25]
 */
public class DateCommonUtils {
    private static final SimpleDateFormat sYYDDMM = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat sHHMMSS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static Date strToDate(String dateStr, FormatType type) {
        Date date = null;
        SimpleDateFormat sm = null;
        if (type == FormatType.HHMMSS) {
            sm = sHHMMSS;
        } else if (type == FormatType.YYMMDD) {
            sm = sYYDDMM;
        }

        try {
            date = sm.parse(dateStr);
            return date;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return date;

    }

    public static String dateToStr(Date date, FormatType type) {
        SimpleDateFormat sm = null;
        if (type == FormatType.HHMMSS) {
            sm = sHHMMSS;
        } else if (type == FormatType.YYMMDD) {
            sm = sYYDDMM;
        }
        String dateStr = sm.format(date);

        return dateStr;
    }

    /**
     * <br>
     * 功能简述: 显示day天前的日期，如今天为2012-11-20,返回两天前的日期即为2012-11-18 00:00:00 <br>
     * 功能详细描述: <br>
     * 注意:
     * 
     * @param day
     * @return
     */
    public static String getRecentlyDay(int day) {
        long time = 24 * 3600 * 1000 * day;
        Date today = new Date();
        Date thatDay = new Date(today.getTime() - time);

        StringBuffer sb = new StringBuffer(dateToStr(thatDay, FormatType.YYMMDD));
        sb.append(" 00:00:00");

        return sb.toString();
    }

    /**
     * 
     * <br>
     * 类描述: <br>
     * 功能详细描述:
     * 
     * @author linhang
     * @date [2012-10-25]
     */
    public enum FormatType {
        HHMMSS, YYMMDD
    }

}
