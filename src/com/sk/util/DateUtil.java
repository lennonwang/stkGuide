package com.sk.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang.StringUtils;

/**
 * 通用日期工具类
 *  
 * 
 */
public class DateUtil {

	public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String DAY_DATETIME_FORMAT = "yyyy-MM-dd";
	public static final String DAY_MINUTE_DATETIME_FORMAT = "yyyy-MM-dd HH:mm";
    public static final String DAY_MINUTE_DATETIME_CHINESE_FORMAT = "yyyy年MM月dd日 HH:mm";

	/**
     * 取当前日期的字符形式（yyyy年xx月xx日）
     * 
     * @return
     */
	public static String getDateChStr() {
		Calendar calendar = new GregorianCalendar();
        return calendar.get(Calendar.YEAR) + "年"
                + (calendar.get(Calendar.MONTH) + 1) + "月"
                + calendar.get(Calendar.DAY_OF_MONTH) + "日";
	}

    /**
     * 将指定格式的日期字符串转换成毫秒数
     * 
     * @param date
     * @param pattern
     * @return
     */
	public static long paseDateLong(String date, String pattern) {
		long result = -1;
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		try {
			result = format.parse(date).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

    /**
     * 取当前时间的前一天时间
     * 
     * @return
     */
	public static String getPreDay() {
		Date d = new Date(System.currentTimeMillis() - 24 * 3600 * 1000);
		SimpleDateFormat format = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT);
		return format.format(d);
	}

    /**
     * 得到当前日期
     * 
     * @param format
     *            默认 yyyy-MM-dd HH:mm:ss
     * @return
     */
	public static final String getDateStr() {
		return getDate("");
	}

    /**
     * 得到当前日期
     * 
     * @param format
     *            默认 yyyy-MM-dd HH:mm:ss
     * @return
     */
	public static final String getDate(String format) {
		if (format.equals("")) {
			format = DEFAULT_DATETIME_FORMAT;
		}
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String cdate = sdf.format(cal.getTime());
		return cdate;
	}

    /**
     * 把毫秒时间转换成指定格式的时间字符串
     * 
     * @param time
     * @param format
     * @return
     */
	public static final String getDate(long time, String format) {
		if (format.equals("")) {
			format = DEFAULT_DATETIME_FORMAT;
		}
		Date d = new Date(time);
		SimpleDateFormat fm = new SimpleDateFormat(format);
		return fm.format(d);
	}

    /**
     * 返回短日期，yyyy-MM-dd
     * 
     * @param time
     * @param format
     * @return
     */
    public static final Date getShortDate(long time) {
        Date date = new Date(time);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 判断一天是否为周末
     * 
     * @param sDate
     * @return
     */
	public static boolean isWeekEnd(String sDate) {
		if ("".equals(sDate)) {
			return false;
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		Date date = null;
		try {
			date = df.parse(sDate);
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}

		Calendar cd = Calendar.getInstance();
		cd.setTime(date);
		int mydate = cd.get(Calendar.DAY_OF_WEEK);

		if (mydate == 1 || mydate == 7)
			return true;
		else
			return false;
	}

    /**
     * 取两个时间间隔的分钟数
     * 
     * @param date1
     * @param date2
     * @return
     */
	public static int getMinutesNumberNew(Date date1, Date date2) {
		return (int) (date2.getTime() - date1.getTime()) / 60000;
	}

	public static Date getTomorrow() {
		return getDate(1);
	}

    // 获取next天后的日期，如果next为负数，则表示几天前的date
	public static Date getDate(int next) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, next);
		return calendar.getTime();
	}

	public static Date getToday() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	public static Date getTomorrowBegin() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public static long todayLeftTimes() {
		Date now = new Date();

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		System.out.println(calendar.getTime());

		return calendar.getTimeInMillis() - now.getTime();
	}

	public static Date dateAddDays(Date date, int addDays) {
		Date newDate = null;
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.DATE, addDays);
			newDate = cal.getTime();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return newDate;
	}

	public static String solarDateWeek() {
		Calendar calendar = new GregorianCalendar();
        return (calendar.get(Calendar.MONTH) + 1) + "月"
                + calendar.get(Calendar.DAY_OF_MONTH) + "日";
	}

    /**
     * 根据当前日期生成简单密钥<BR>
     * 规则： 年份后2位 + 月份 + 日期
     * 
     * @return
     */
	public static int getDateCipher() {
        Calendar cal = Calendar.getInstance();// 使用日历类
		return cal.get(Calendar.YEAR) % 100 + (cal.get(Calendar.MONTH) + 1) + cal.get(Calendar.DAY_OF_MONTH);
	}

    /**
     * 返回当天的中文星期几
     * 
     * @return
     */
	public static String dayInWeek() {
		return dayInWeek(new Date());
	}

    /**
     * 返回中文星期几
     * 
     * @param d
     *            传入的日期
     * @return
     */
	public static String dayInWeek(Date d) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		int time = c.get(Calendar.DAY_OF_WEEK);
        String[] weekNames = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		String weekName = weekNames[time - 1];
		return weekName;
	}

    /**
     * 将日期转换成字符串形式
     * 
     * @param date
     * @return
     */
	public static String dateToString(Date date) {
		return dateToString(date, DEFAULT_DATETIME_FORMAT, null);
	}

    /**
     * 将日期转换成字符串形式
     * 
     * @param date
     * @param format
     * @return
     */
	public static String dateToString(Date date, String format) {
		return dateToString(date, format, null);
	}

    /**
     * 将日期转换成字符串形式
     * 
     * @param date
     * @param format
     * @param defVal
     * @return
     */
	public static String dateToString(Date date, String format, String defVal) {
		if (date == null || StringUtils.isEmpty(format)) {
			return defVal;
		}
		String result;
		try {
			result = new SimpleDateFormat(format).format(date);
		} catch (Exception e) {
			result = defVal;
		}
		return result;
	}

    /**
     * 将时间转换成字符串形式
     * 
     * @param datetime
     * @return
     */
	public static String timeToString(long datetime) {
		return timeToString(datetime, DEFAULT_DATETIME_FORMAT, null);
	}

    /**
     * 将时间转换成字符串形式
     * 
     * @param datetime
     * @param format
     * @return
     */
	public static String timeToString(long datetime, String format) {
		return timeToString(datetime, format, null);
	}

    /**
     * 将时间转换成字符串形式
     * 
     * @param datetime
     * @param format
     * @param defVal
     * @return
     */
	public static String timeToString(long datetime, String format, String defVal) {
		Date d = new Date(datetime);
		return new SimpleDateFormat(format).format(d);
	}

    /**
     * 将字符串转换成日期对象
     * 
     * @param strDate
     * @return
     */
	public static Date stringToDate(String strDate) {
		if(strDate.length() == DAY_DATETIME_FORMAT.length()){
			return stringToDate(strDate, DAY_DATETIME_FORMAT, new Date());
		}
		if(strDate.length() == DAY_MINUTE_DATETIME_FORMAT.length()){
			return stringToDate(strDate, DAY_MINUTE_DATETIME_FORMAT, new Date());
		}
		return stringToDate(strDate, DEFAULT_DATETIME_FORMAT, new Date());
	}

    /**
     * 将字符串转换成日期对象
     * 
     * @param strDate
     * @param format
     * @return
     */
	public static Date stringToDate(String strDate, String format) {
		return stringToDate(strDate, format, new Date());
	}

    /**
     * 将字符串转换成日期对象
     * 
     * @param strDate
     * @param format
     * @param defVal
     * @return
     */
	public static Date stringToDate(String strDate, String format, Date defVal) {
		if (StringUtils.isEmpty(strDate) || StringUtils.isEmpty(format)) {
			return null;
		}
		Date d;
		try {
			d = new SimpleDateFormat(format).parse(strDate);
		} catch (ParseException e) {
			d = defVal;
		}
		return d;
	}

    /**
     * 将字符串转换成时间
     * 
     * @param strDate
     * @return
     */
	public static long stringToTime(String strDate) {
		return stringToTime(strDate, DEFAULT_DATETIME_FORMAT, null);
	}

    /**
     * 将字符串转换成时间
     * 
     * @param strDate
     * @param format
     * @return
     */
	public static long stringToTime(String strDate, String format) {
		return stringToTime(strDate, format, null);
	}

    /**
     * 将字符串转换成时间
     * 
     * @param strDate
     * @param format
     * @param defVal
     * @return
     */
	public static long stringToTime(String strDate, String format, Date defVal) {
		Date d = stringToDate(strDate, format, defVal);
		if (d == null) {
			return 0L;
		} else {
			return d.getTime();
		}
	}

    /**
     * 将时间调整到当天的00：00：00
     * 
     * @param date
     * @return
     */
	public static Date setToDayStartTime(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date.getTime());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

    /**
     * 获得两个日期之间相差的天数(返回值去掉了小数部分，即只返回)。（date1 - date2）
     * 
     * @param date1
     * @param date2
     * @return
     */
	public static int intervalDays(Date date1, Date date2) {
		long intervalMillSecond = setToDayStartTime(date1).getTime() - setToDayStartTime(date2).getTime();
        return (int) (intervalMillSecond / (24 * 3600 * 1000));
	}

    /**
     * 获得两个日期之间相差的分钟数。（date1 - date2）
     * 
     * @param date1
     * @param date2
     * @return
     */
	public static int intervalMinutes(Date date1, Date date2) {
		long intervalMillSecond = date1.getTime() - date2.getTime();
		return (int) (intervalMillSecond / 60 * 1000 + (intervalMillSecond % 60 * 1000 > 0 ? 1 : 0));
	}

    /**
     * 取两个日期（yyyy-MM-dd HH:mm:ss）字符串相差的分钟数
     * 
     * @param dateStr1
     * @param dateStr2
     * @return
     */
	public static int intervalMinutes(String dateStr1, String dateStr2) {
		long minNumber = 0;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date d1 = df.parse(dateStr1);
			Date d2 = df.parse(dateStr2);
			return intervalMinutes(d1, d2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (int) minNumber;
	}

    /**
     * 获得两个日期之间相差的秒数。（date1 - date2）
     * 
     * @param date1
     * @param date2
     * @return
     */
	public static int intervalSeconds(Date date1, Date date2) {
		long intervalMillSecond = date1.getTime() - date2.getTime();
		return (int) (intervalMillSecond / 1000 + (intervalMillSecond % 1000 > 0 ? 1 : 0));
	}
 
    /** 根据日期获取字符串,yyyy-MM-dd */
    public static String getDateString(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(date);
	}
	
    // 获取一天当中的开始时间
	public static java.util.Date getFirstTimeInDay(Date dateTime) {
		Date date = null;
		String dateString = getDateString(dateTime);
		date = stringSimpleToDate(dateString);
		return date;
	}

    // 获取一天当中的结束时间
	public static java.util.Date getLastTimeInDay(Date dateTime) {
		Date date = null;
		String dateString = getDateString(dateTime);
		dateString = dateString.concat(" 23:59:59");
		date = stringToDate(dateString);
		return date;
	}

    // 字符串转换时间
    public static Date stringSimpleToDate(String date) {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			return (java.util.Date) format.parseObject(date);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

    /***
     * 通过时间长短比较，输入日期
     * 
     * @param d1
     * @return
     */
	public static  String  getCompareTimeByNow(String d1){ 
		if(StringUtils.isBlank(d1)){
			return "";
		}
		return getCompareTimeByNow(DateUtil.stringToDate(d1));
	}

    /**
     * 个性化显示时间
     * 
     * @param d1
     * @return
     */
	public static String getCompareTimeByNow(Date d1) {
		if (d1 == null) {
			return "";
		}
		long tmp = d1.getTime();
		long today = new Date().getTime();
        // 分钟
		long minutes = (today - tmp) / (1000 * 60);
		StringBuilder sb = new StringBuilder();
		if (minutes > 60 * 24) {
			long days = minutes / (60 * 24);
            sb.append(days + "天前");
			if (days >= 1) {
				return DateUtil.dateToString(d1, "MM.dd HH:mm");
			}
		}
		if (minutes > 60) {
			long hours = minutes / (60);
            sb.append(hours + "小时前");
			if (hours >= 1) {
				return sb.toString();
			}
		}
		if (minutes < 2) {
            return "刚刚";
		} else {
            return (minutes + "分钟前");
		}
		
	}

	public static void main(String[] args) {
		try {
			String result = getCompareTimeByNow(DateUtil.stringToDate( "2012-12-16 10:33:00" ) );
			System.out.println(result);
			result = getCompareTimeByNow(DateUtil.stringToDate("2012-12-18 23:55:33"));
			System.out.println(result);
			result = getCompareTimeByNow(DateUtil.stringToDate("2013-1-5 14:16:33"));
			System.out.println(result);
			result = getCompareTimeByNow(DateUtil.stringToDate("2014-1-7 12:00:33"));
			System.out.println(result);
			result = getCompareTimeByNow(DateUtil.stringToDate("2013-1-5 12:00:33"));
			System.out.println(result); 
			result = getCompareTimeByNow(DateUtil.stringToDate("2013-1-5 14:00:33"));
			System.out.println(result);
			String dateStr = getDate(new Date().getTime(), DAY_MINUTE_DATETIME_CHINESE_FORMAT);
            System.out.println(dateStr+"     "+StringUtils.substring(dateStr, 5));
            System.out.println(getShortDate(new Date().getTime()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
