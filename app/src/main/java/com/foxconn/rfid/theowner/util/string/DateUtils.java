package com.foxconn.rfid.theowner.util.string;

import android.annotation.SuppressLint;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class DateUtils {
	
	private static Calendar calendar = Calendar.getInstance();
	
	public static String formatDate(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String result = sdf.format(date);
        return result;
    }
	
	public static Date formatDate2String(String dateString, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date result = sdf.parse(dateString);
        return result;
    }

	public static String formatDate(Date date) {
        return formatDate(date, "yyyy-MM-dd");
    }
	
    public static String formatDateTime(Date date) {
        return formatDate(date, "yyyy-MM-dd HH:mm:ss");
    }
    public static String formatDateHour(Date date) {
        return formatDate(date, "yyyy-MM-dd HH:00:00");
    }
    public static String formatTime(Date date) {
        return formatDate(date, "HH:mm:ss");
    }
    
    public static Date formatDate(String dateString) throws ParseException {
        return formatDate2String(dateString, "yyyy-MM-dd");
    }
    public static Date formatTime(String dateString) throws ParseException {
        return formatDate2String(dateString, "HH:mm:ss");
    }
	
    public static Date formatDateTime(String dateString) throws ParseException {
        return formatDate2String(dateString, "yyyy-MM-dd HH:mm:ss");
    }

    public static String getCurrentDate() {
        return formatDate(new Date());
    }   
    
    public static String getCurrentDateTime() {
        return formatDateTime(new Date());
    }
    public static Long getCurrentDateHour() {
        return getLongTime(formatDateHour(new Date()));
    }
    public static Long getCurrentDateLong() {
        return getLongTime(formatDate(new Date(), "yyyy-MM-dd 00:00:00"));
    }
    public static String getStringFromLong(long longtime,String dateFormat) {
        return formatDate(getDateFromLong(longtime), dateFormat);
    }
    public static Date addDay(Date date, int days) {
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return calendar.getTime();
    }

    public static Date addMonth(Date date, int months) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, months);
        return cal.getTime();
    }
    
    public static String getWeekStartDate(Date date){
    	calendar.setTime(date);
    	calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
    	return formatDate(calendar.getTime());
    }
    public static String getWeekEndDate(Date date){
    	calendar.setTime(date);
    	calendar.setFirstDayOfWeek(Calendar.MONDAY);
    	calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
    	calendar.add(Calendar.DAY_OF_WEEK, 6);
    	return formatDate(calendar.getTime());
    }
    
    public static String getMonthStartDate(Date date){
    	calendar.setTime(date);
    	calendar.set(Calendar.DAY_OF_MONTH, 1);
    	return formatDate(calendar.getTime());
    }
    public static String getMonthEndDate(Date date){
    	calendar.setTime(date);
    	calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, 0);
    	return formatDate(calendar.getTime());
    }
    
    public static String getQuarterStartDate(Date date){
    	calendar.setTime(date);
    	int month = getQuarterInMonth(calendar.get(Calendar.MONTH), true);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
    	return formatDate(calendar.getTime());
    }
    public static String getQuarterEndDate(Date date){
    	calendar.setTime(date);
		int month = getQuarterInMonth(calendar.get(Calendar.MONTH), false);
		calendar.set(Calendar.MONTH, month + 1);
		calendar.set(Calendar.DAY_OF_MONTH, 0);
    	return formatDate(calendar.getTime());
    }
    
    public static String getYearStartDate(Date date){
    	calendar.setTime(date);
    	calendar.set(Calendar.MONTH, 0);
    	calendar.set(Calendar.DAY_OF_YEAR, 1);
    	return formatDate(calendar.getTime());
    }
    public static String getYearEndDate(Date date){
    	calendar.setTime(date);
    	calendar.set(Calendar.MONTH, 11);
    	calendar.set(Calendar.DAY_OF_MONTH, 31);
    	return formatDate(calendar.getTime());
    }
    
    // 返回第几个月份，不是几月
 	// 季度一年四季， 第一季度：1月-3月， 第二季度：4月-6月， 第三季度：7月-9月， 第四季度：10月-12月
 	private static int getQuarterInMonth(int month, boolean isQuarterStart) {
 		int months[] = { 0, 3, 6, 9 };
 		if (!isQuarterStart) {
 			months = new int[] { 2, 5, 8, 11 };
 		}
 		if (month >= 0 && month <= 2)
 			return months[0];
 		else if (month >= 3 && month <= 5)
 			return months[1];
 		else if (month >= 6 && month <= 8)
 			return months[2];
 		else
 			return months[3];
 	}
 	
	public static String changeTime(int total_time,int unit){
		int h=total_time/(unit*60*60);
		int min=(total_time%(unit*60*60))/(unit*60);
		int end=(total_time/unit)%60;
		String str="";
		if(h<10){
			str+="0"+h+":";
		}else{
			str+=h+":";
		}
		if(min<10){
			str+="0"+min+":";
		}else{
			str+=min+":";
		}
		if(end<10){
			str+="0"+end;
		}else{
			str+=end;
		}
		return str;
	}
	public static long getLongTime(String strSource)
	{
		SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date date;
			date = simpleDateFormat .parse(strSource);
			long timeStemp = date.getTime();
			return timeStemp;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	     

	}

	public static Date getDateFromLong( long time )
    {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis( time );
        return c.getTime();
    }
	public static long getLongFromDate( Date dtSource )
    {
		 return getLongTime(formatDate(dtSource, "yyyy-MM-dd HH:mm:ss"));
    }
	public static String secToTime(int time ) {
		String timeStr = null;
		int hour = 0;
		int minute = 0;
		int second = 0;
		if (time <= 0)
			return "00:00:00";
		else {
			minute = time / 60;
			if (minute < 60) {
				second = time % 60;
				timeStr = "00:"+unitFormat(minute) + ":" + unitFormat(second);
			} else {
				hour = minute / 60;
//	                if (hour > 99)
//	                    return "99:59:59";
				minute = minute % 60;
				second = time - hour * 3600 - minute * 60;

				timeStr =Integer.toString(hour)  + ":" + unitFormat(minute) + ":" + unitFormat(second);


			}
		}
		return timeStr;
	}
	public static String unitFormat(int i) {
		String retStr = null;
		if (i >= 0 && i < 10)
			retStr = "0" + Integer.toString(i);
		else
			retStr = "" + i;
		return retStr;
	}
	public static String formatTime(long ms) {

		int ss = 1000;
		int mi = ss * 60;
		int hh = mi * 60;
		int dd = hh * 24;

		long day = ms / dd;
		long hour = (ms - day * dd) / hh;
		long minute = (ms - day * dd - hour * hh) / mi;
		long second = (ms - day * dd - hour * hh - minute * mi) / ss;
		long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

		String strDay = day < 10 ? "0" + day : "" + day; //天
		String strHour = hour < 10 ? "0" + hour : "" + hour;//小时
		String strMinute = minute < 10 ? "0" + minute : "" + minute;//分钟
		String strSecond = second < 10 ? "0" + second : "" + second;//秒
		String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : "" + milliSecond;//毫秒
		strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : "" + strMilliSecond;
		if (day == 0 && hour != 0) {
			return strHour+"时 "+strMinute+"分";
		}
		if (day == 0 && hour == 0 && minute != 0) {
			return strMinute+"分";
		}

		return strDay+"天 "+strHour+"时 "+strMinute+"分";
	}

}
