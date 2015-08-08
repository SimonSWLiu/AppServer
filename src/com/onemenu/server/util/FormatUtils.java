package com.onemenu.server.util;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class FormatUtils {
	@Deprecated
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
	@Deprecated
    public static String formatTimestamp(Date timestamp) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = format.format(timestamp);
        return dateString;
    }
	@Deprecated
    public static Date formatStringToDate(String dataString) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Date data = null;
        try {
            data = format.parse(dataString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return data;
    }
	@Deprecated
    public static Date formatStringToTime(String timeString) {

        SimpleDateFormat format = new SimpleDateFormat("hh:mm");

        Date time = null;
        try {
            time = format.parse(timeString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return time;
    }

    public static String formatNumStr(Object num, String strFormat) {

        if (num != null) {

            DecimalFormat df = new DecimalFormat(strFormat);

            return String.valueOf(df.format(num));
        }

        return null;

    }

    public static String formatPrice(Object price) {

        if (price != null) {

            DecimalFormat df = new DecimalFormat("#0.00");

            return String.valueOf(df.format(price));
        }

        return "0.00";

    }
    @Deprecated
    public static Timestamp getCurrentTimestamp() {

        Date date = new Date();

        return new Timestamp(date.getTime());
    }

    // 0:Sun, 1:Mon, 2:Tue, 3:Wed, 4:Thu, 5:Fri, 6:Sat
    public static Integer getCurrentWeek() {

        Date date = new Date();

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week = cal.get(Calendar.DAY_OF_WEEK) - 1;

        return week;
    }
}
