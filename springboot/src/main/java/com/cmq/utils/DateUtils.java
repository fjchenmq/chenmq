package com.cmq.utils;

import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateUtils {
	 // 长日期格式
    public static String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static String DATE_FORMAT = "yyyyMMdd";

	public static String formatDate(Date date, String formater) {
		SimpleDateFormat bartDateFormat = new SimpleDateFormat(formater);
		if (date == null) {
			return null;
		} else {
			String datestring = bartDateFormat.format(date);
			return datestring;
		}
	}

	public static String formatDate(Date date, String formater, Locale locale) {
		SimpleDateFormat bartDateFormat = new SimpleDateFormat(formater, locale);
		if (date == null) {
			return null;
		} else {
			String datestring = bartDateFormat.format(date);
			return datestring;
		}
	}

	public static Date getDate(String value, String formater) {
		Date date = null;
		if (value == null) {
			return new Date();
		}
		SimpleDateFormat fordate = new SimpleDateFormat(formater);
		try {
			date = fordate.parse(value);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return date;
	}

	/**
	 * 将长整型数字转换为日期格式的字符串
	 * 
	 * @param time
	 * @param format
	 * @return
	 */
	public static String convert2String(long time, String format) {
		if (time > 0l) {
			if (StringUtils.isBlank(format))
				format = DateUtils.TIME_FORMAT;
			SimpleDateFormat sf = new SimpleDateFormat(format);
			Date date = new Date(time);
			return sf.format(date);
		}
		return "";
	}

	public static Date nextMonth(Date date) {
		Date next = new Date(date.getTime());
		int month = date.getMonth();
		if (month == 11) {
			next.setYear(date.getYear() + 1);
			next.setMonth(0);
		} else {
			next.setMonth(date.getMonth() + 1);
		}
		return next;
	}

	public static Date preMonth(Date date) {
		Date pre = new Date(date.getTime());
		int month = date.getMonth();
		if (month == 0) {
			pre.setMonth(11);
			pre.setYear(date.getYear() - 1);
		} else {
			pre.setMonth(date.getMonth() - 1);
		}
		return pre;
	}
	public static long getDateTime(String dateStr, String formater) throws Exception {
		Calendar c = Calendar.getInstance();
		c.setTime(new SimpleDateFormat(formater).parse(dateStr));
		
		return c.getTimeInMillis();
//		SimpleDateFormat sdf = new SimpleDateFormat(formater);
//		return sdf.parse(dateStr).getTime();
	}
	
	public static String getLongToDate(long dateTime, String formater){
		Date dat=new Date(dateTime);
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(dat);  
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return format.format(gc.getTime());
	}
	
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {
		/**
		 * 将字符串数据转化为毫秒数
		 */
		String dateTime="2015-01-05 10:13:14";

		   Calendar c = Calendar.getInstance();
		  
		c.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateTime));

		System.out.println("时间转化后的毫秒数为："+c.getTimeInMillis());
		/**
		* 将毫秒数转化为时间
		*/
	   long sd=1415153594000L;
       Date dat=new Date(sd);
       GregorianCalendar gc = new GregorianCalendar();
       gc.setTime(dat);  
       SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       String sb=format.format(gc.getTime());
       System.out.println(sb);
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
