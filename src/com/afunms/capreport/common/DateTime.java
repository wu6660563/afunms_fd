/**
 * @author sunqichang/������
 * Created on May 13, 2011 4:41:35 PM
 */
package com.afunms.capreport.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * @author sunqichang ���õ�����ʱ�䴦����
 * 
 */
public class DateTime {

	/**
	 * ʱ����ƫ������Ĭ��Ϊ�й����ڵ�ʱ������������
	 */
	private String timeZoneOffSet = "GMT+08:00";

	/**
	 * ����
	 */
	private GregorianCalendar calendar;

	/**
	 * ����/��
	 */
	public final static long oneDayValue = 24 * 60 * 60 * 1000;

	/**
	 * ����/ʱ
	 */
	public final static long oneHourValue = 60 * 60 * 1000;

	/**
	 * ����/��
	 */
	public final static long oneMinuteValue = 60 * 1000;

	/**
	 * ����/��
	 */
	public final static long oneSecondValue = 1000;

	/**
	 * ����ʱ��_��ʽ_1��20100101130101
	 */
	public final static String Datetime_Format_1 = "yyyyMMddHHmmss";

	/**
	 * ����ʱ��_��ʽ_2��2010-01-01 13��01��01
	 */
	public final static String Datetime_Format_2 = "yyyy-MM-dd HH:mm:ss";

	/**
	 * ����ʱ��_��ʽ_3��2010��01��01�� 13ʱ01��01��
	 */
	public final static String Datetime_Format_3 = "yyyy��MM��dd�� HHʱmm��ss��";

	/**
	 * ����ʱ��_��ʽ_4��20100101
	 */
	public final static String Datetime_Format_4 = "yyyyMMdd";

	/**
	 * ����ʱ��_��ʽ_14��2010010122
	 */
	public final static String Datetime_Format_14 = "yyyyMMddHH";

	/**
	 * ����ʱ��_��ʽ_5��2010-01-01
	 */
	public final static String Datetime_Format_5 = "yyyy-MM-dd";

	/**
	 * ����ʱ��_��ʽ_6��2010��01��01��
	 */
	public final static String Datetime_Format_6 = "yyyy��MM��dd��";

	/**
	 * ����ʱ��_��ʽ_7��13ʱ01��01��
	 */
	public final static String Datetime_Format_7 = "HHʱmm��ss��";

	/**
	 * ����ʱ��_��ʽ_8��2010��
	 */
	public final static String Datetime_Format_8 = "yyyy";

	/**
	 * ����ʱ��_��ʽ_9��08��
	 */
	public final static String Datetime_Format_9 = "MM";

	/**
	 * ����ʱ��_��ʽ_10��22��
	 */
	public final static String Datetime_Format_10 = "dd";

	/**
	 * ����ʱ��_��ʽ_11��13ʱ
	 */
	public final static String Datetime_Format_11 = "HH";

	/**
	 * ����ʱ��_��ʽ_12��24��
	 */
	public final static String Datetime_Format_12 = "mm";

	/**
	 * ����ʱ��_��ʽ_13��20��
	 */
	public final static String Datetime_Format_13 = "ss";

	/**
	 * ���캯�� Ĭ�ϵ�ǰʱ��
	 */
	public DateTime() {
		this.calendar = new GregorianCalendar();
		this.calendar.setTime(new Date());
		this.buildTimeZone();
	}

	/**
	 * ���캯����
	 * 
	 * @param date
	 */
	public DateTime(Date date) {
		this.calendar = new GregorianCalendar();
		this.calendar.setTime(date);
		this.buildTimeZone();
	}

	/**
	 * ����ʱ������
	 */
	private void buildTimeZone() {
		this.calendar.setTimeZone(TimeZone.getTimeZone(timeZoneOffSet));
	}

	public String getTimeZoneOffSet() {
		return timeZoneOffSet;
	}

	public void setTimeZoneOffSet(String timeZoneOffSet) {
		this.timeZoneOffSet = timeZoneOffSet;
	}

	/**
	 * �������ַ�����
	 * 
	 * @param pattern
	 *            ���������ʱ��_��ʽ_��
	 * @return �����ַ���
	 */
	public String toString(String pattern) {
		DateFormat df = new SimpleDateFormat(pattern);
		return df.format(this.calendar.getTime());
	}

	/**
	 * ��ָ����ʱ��������
	 * 
	 * @param dateValue
	 *            ָ����ʱ�������ֵ��
	 * @return ������
	 */
	public int distanceDayFrom(long dateValue) {
		long dintanceValue = this.getValue() - dateValue;
		int distanceDay = (int) (dintanceValue / oneDayValue);
		return distanceDay;

	}

	/**
	 * ��ȡʱ�������ֵ��
	 */
	public long getValue() {
		return this.calendar.getTimeInMillis();
	}

	/**
	 * ��ȡ��ʱ���ڴ����еĵڼ��ܡ�
	 * 
	 * @return �ڴ����еĵڼ���
	 */
	public int getWeekOfYear() {
		return this.calendar.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * ��ȡ�꣬��Χ1900-9999��
	 * 
	 * @return Returns the year.
	 */
	public int getYear() {
		return this.calendar.get(Calendar.YEAR);
	}

	/**
	 * ��ȡ�·ݣ���Χ1-12��
	 * 
	 * @return Returns the month.
	 */
	public int getMonth() {
		return this.calendar.get(Calendar.MONTH) + 1;
	}

	/**
	 * ��ȡһ���еĵڼ���ȡֵ��Χ1-31��
	 * 
	 * @return Returns the date.
	 */
	public int getDate() {
		return this.calendar.get(Calendar.DATE);
	}

	/**
	 * ȡһ�����ڵĵڼ��졣 ��ĩΪһ�����ڵĵ�һ�죻
	 * 
	 * @return һ�����ڵĵڼ���
	 */
	public int getDay() {
		return this.calendar.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * ��ȡСʱ����Χ0-23��
	 * 
	 * @return Returns the hours.
	 */
	public int getHours() {
		return this.calendar.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * ��ȡ���ӣ���Χ0-59��
	 * 
	 * @return Returns the minutes.
	 */
	public int getMinutes() {
		return this.calendar.get(Calendar.MINUTE);
	}

	/**
	 * ��ȡ�룬��Χ0-59��
	 * 
	 * @return Returns the seconds.
	 */
	public int getSeconds() {
		return this.calendar.get(Calendar.SECOND);
	}

	/**
	 * ��ȡ������ʱ�����е�һ��ֵ��
	 * 
	 * @return ������ʱ�����е�һ��ֵ
	 */
	public int get(String style) {
		return Integer.parseInt(this.toString(style));
	}

	/**
	 * ����startDateǰһ��
	 * 
	 * @return
	 */
	public String getLastDay() {
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		start.setTimeInMillis(this.calendar.getTimeInMillis() - oneDayValue);
		end.setTimeInMillis(this.calendar.getTimeInMillis());
		return DateTime.getMyDateTime(start.getTime(), DateTime.Datetime_Format_5) + "~"
				+ DateTime.getMyDateTime(end.getTime(), DateTime.Datetime_Format_5);
	}

	/**
	 * ����startDateǰһ��
	 * 
	 * @param startweekday
	 *            0�������տ�ʼ��1������һ��ʼ
	 * @return ���磺2009-03-01~2009-03-08��startDate:2009-03-11��
	 */
	public String getLastWeek(int startweekday) {
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		int dayofweek = this.calendar.get(Calendar.DAY_OF_WEEK);
		if (0 == startweekday) {
			start.setTimeInMillis(this.calendar.getTimeInMillis() - (dayofweek + 6) * oneDayValue);
			end.setTimeInMillis(this.calendar.getTimeInMillis() - (dayofweek - 1) * oneDayValue);
		} else if (1 == startweekday) {
			start.setTimeInMillis(this.calendar.getTimeInMillis() - (dayofweek + 5) * oneDayValue);
			end.setTimeInMillis(this.calendar.getTimeInMillis() - (dayofweek - 2) * oneDayValue);
		}
		return DateTime.getMyDateTime(start.getTime(), DateTime.Datetime_Format_5) + "~"
				+ DateTime.getMyDateTime(end.getTime(), DateTime.Datetime_Format_5);
	}

	/**
	 * ����startDate�ϸ��µ�ʱ��
	 * 
	 * @return ���磺2009-02-01~2009-03-01��startDate��2009-03-11��
	 */
	public String getLastMonth() {
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		int year = this.calendar.get(Calendar.YEAR);
		int month = this.calendar.get(Calendar.MONTH);
		end.set(year, month, 1);
		if (month < 1) {
			year = this.calendar.get(Calendar.YEAR) - 1;
			month = 12;
		}
		start.set(year, month - 1, 1);
		return DateTime.getMyDateTime(start.getTime(), DateTime.Datetime_Format_5) + "~"
				+ DateTime.getMyDateTime(end.getTime(), DateTime.Datetime_Format_5);
	}

	/**
	 * ����startDate��ǰһ���ȵ�ʱ��
	 * 
	 * @return startDate��ǰһ���ȵ�ʱ�� ���磺2008-10-01~2009-01-01��endDate:2009-03-11��
	 */
	public String getLastSeason() {
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		int month = this.calendar.get(Calendar.MONTH);
		int year = this.calendar.get(Calendar.YEAR);
		if (month == Calendar.JANUARY || month == Calendar.FEBRUARY || month == Calendar.MARCH) {
			// ����10-01 ~~ ����01-01
			start.set(year - 1, Calendar.OCTOBER, 1);
			end.set(year, Calendar.JANUARY, 1);
		} else if (month == Calendar.APRIL || month == Calendar.MAY || month == Calendar.JUNE) {
			// ����01-01 ~~ ����04-01
			start.set(year, Calendar.JANUARY, 1);
			end.set(year, Calendar.APRIL, 1);
		} else if (month == Calendar.JULY || month == Calendar.AUGUST || month == Calendar.SEPTEMBER) {
			// ����04-01 ~~ ����07-01
			start.set(year, Calendar.APRIL, 1);
			end.set(year, Calendar.JULY, 1);
		} else {
			// ����07-01 ~~ ����10-01
			start.set(year, Calendar.JULY, 1);
			end.set(year, Calendar.OCTOBER, 1);
		}
		return DateTime.getMyDateTime(start.getTime(), DateTime.Datetime_Format_5) + "~"
				+ DateTime.getMyDateTime(end.getTime(), DateTime.Datetime_Format_5);
	}

	/**
	 * ����startDate����һ��һ�ŵ�����һ��һ��
	 * 
	 * @return ���磺2008-01-01~2009-01-01��startDate:2009-03-11��
	 */
	public String getLastYear() {
		int year = this.calendar.get(Calendar.YEAR);
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		start.set(year - 1, 0, 1);
		end.set(year, 0, 1);
		return DateTime.getMyDateTime(start.getTime(), DateTime.Datetime_Format_5) + "~"
				+ DateTime.getMyDateTime(end.getTime(), DateTime.Datetime_Format_5);
	}

	/**
	 * ����ת������Ȼ������������3661�����Ȼ������1Сʱ1��1�롣
	 * 
	 * @param ms
	 *            ����
	 * @param isShowZero
	 *            �Ƿ���ʾΪ0. �棺0�룻�٣�����1��
	 * @return ����ʱ����֤���ظ�ʱ��ֵ
	 */
	public static String getTimeExpression(long ms, boolean isShowZero) {
		long day;
		long hour;
		long minutes;
		long second;
		long ONE_SECOND = 1000;

		long ONE_MINUTE = ONE_SECOND * 60;
		long ONE_HOUR = ONE_MINUTE * 60;
		long ONE_DAY = ONE_HOUR * 24;

		day = (long) (ms / ONE_DAY);
		long leftHour = ms - ONE_DAY * day;

		hour = (long) (leftHour / ONE_HOUR);
		long leftMinutes = leftHour - hour * ONE_HOUR;

		minutes = (long) (leftMinutes / ONE_MINUTE);
		long leftSecond = (long) (leftMinutes - minutes * ONE_MINUTE);

		second = (long) (leftSecond / ONE_SECOND);
		StringBuffer exp = new StringBuffer();
		if (day != 0) {
			exp.append(day + "��");
		}
		if (hour != 0) {
			exp.append(hour + "Сʱ");
		}
		if (minutes != 0) {
			exp.append(minutes + "��");
		}
		if (second != 0) {
			exp.append(second + "��");
		} else {
			if (exp.length() == 0) {
				if (isShowZero) {
					exp.append("0��");
				} else {
					exp.append("����1��");
				}
			}
		}
		return exp.toString();
	}

	/**
	 * �����Զ����ʽ��ʱ��
	 * 
	 * @param ʱ���ʽ
	 * @return �Զ����ʽ��ʱ��
	 */
	public String getMyDateTime(String pattern) {
		DateFormat df = new SimpleDateFormat(pattern);
		return df.format(this.calendar.getTime());
	}

	/**
	 * �����Զ����ʽ��ʱ��
	 * 
	 * @param date
	 *            ���ڶ���
	 * @param pattern
	 *            ʱ���ʽ
	 * @return �Զ����ʽ��ʱ��
	 */
	public static String getMyDateTime(Date date, String pattern) {
		DateFormat df = new SimpleDateFormat(pattern);
		return df.format(date);
	}

	public void setYear(int year, int month, int date) {
		this.calendar.set(year, month - 1, date);
	}

	public void setHour(int hour, int minute, int second) {
		this.calendar.set(getYear(), getMonth() - 1, getDate(), hour, minute, second);
	}

	/**
	 * ��ָ���ĸ�ʽȥ��ʽ������
	 * 
	 * @param value
	 * @param format
	 * @return
	 */
	public static String format(String value, String format) {
		if (value == null || format == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date date = null;
		try {
			date = sdf.parse(value);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sdf.format(date);
	}

	/**
	 * ��ȡһ�������е�ĳ��(������)��
	 * 
	 * @return
	 */
	public String getDayOfWeek() {
		int week = this.calendar.get(Calendar.DAY_OF_WEEK);
		String strweek = null;
		switch (week) {
		case 0:
			strweek = "һ";
			break;
		case 1:
			strweek = "��";
			break;
		case 2:
			strweek = "��";
			break;
		case 3:
			strweek = "��";
			break;
		case 4:
			strweek = "��";
			break;
		case 5:
			strweek = "��";
			break;
		case 6:
			strweek = "��";
			break;
		default:
		}
		return strweek;
	}

	/**
	 * ���ַ���ת��Ϊ����
	 * 
	 * @param value
	 * @param format
	 * @return
	 */
	public static Date stringToDate(String value, String format) {
		Date date = null;
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
			date = simpleDateFormat.parse(value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * ������ת��Ϊ�ַ���
	 * 
	 * @param value
	 * @param format
	 * @return
	 */
	public static String dateToString(Date value, String format) {
		String date = "";
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
			date = simpleDateFormat.format(value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * �Ƿ�����Ч��ʱ��
	 * 
	 * @param time
	 * @param format
	 * @return
	 */
	public static boolean isValidTime(String time, String format) {
		boolean valid;
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
			simpleDateFormat.parse(time);
			valid = true;
		} catch (Exception e) {
			valid = false;
		}
		return valid;
	}

	/**
	 * �Ƚ�realTime�Ƿ��baseTime��,������򷵻�true,������򷵻�false
	 * 
	 * @param Calendar
	 *            baseTime ��׼ʱ��
	 * @param Calendar
	 *            realTime �Ƚ�ʱ��
	 * @return
	 */
	public static boolean after(Calendar baseTime, Calendar realTime) {
		boolean after = false;
		try {
			if (realTime.after(baseTime)) {
				after = true;
			}
		} catch (Exception e) {
			after = false;
		}
		return after;
	}

	/**
	 * �Ƚ�realTime�Ƿ��baseTime��,������򷵻�true,������򷵻�false
	 * 
	 * @param Date
	 *            baseTime ��׼ʱ��
	 * @param Date
	 *            realTime �Ƚ�ʱ��
	 * @return
	 */
	public static boolean after(Date baseTime, Date realTime) {
		boolean after = false;
		try {
			if (realTime.after(baseTime)) {
				after = true;
			}
		} catch (Exception e) {
			after = false;
		}
		return after;
	}

	/**
	 * ���ڻ�õ�ǰʱ���13λ����ֵ
	 * 
	 * @return long ��ǰʱ���13λ����ֵ
	 * 
	 */
	public static long currentTimeMillis() {
		return System.currentTimeMillis();
	}

	/**
	 * ����������������ʱ�䣨�졢ʱ���롢������
	 * 
	 * @param begin
	 *            ��ʼʱ��
	 * @param end
	 *            ����ʱ��
	 * @param format
	 *            ʱ���ʽ
	 * @param type
	 *            ���ʱ��ĵ�λ
	 * @return
	 */
	public static int getBetweenDays(String begin, String end, String format, long type) {
		int days = 0;
		if (begin == null || end == null) {
			return days;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			Date sdate = sdf.parse(begin);
			Date edate = sdf.parse(end);
			long times = edate.getTime() - sdate.getTime();
			days = (int) (times / type);
		} catch (ParseException pe) {
			pe.printStackTrace();
		}
		return days;
	}

	/**
	 * @param date
	 *            ʱ��
	 * @param type
	 *            ��ʽ
	 * @return 13λ����ֵ
	 */
	public static long getMillisecond(String date, String type) {
		long millisecond = 0;
		String typedefult = "yyyy-MM-dd";
		try {
			if (type != null && "".equals(type)) {
				type = typedefult;
			} else if ("YYYYMMDD".equals(type)) {
				date = date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8);
				type = "yyyy-MM-dd";
			}
			SimpleDateFormat formatter = new SimpleDateFormat(type);
			millisecond = formatter.parse(date).getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return millisecond;
	}

	/**
	 * @param Millisecond
	 *            13λ����ֵ
	 * @param type
	 *            Ҫ���ص�ʱ���ʽ
	 * @return ʱ���ַ���
	 */
	public static String getDateFromMillisecond(String Millisecond, String type) {
		long millisecond = 0;
		String result = "";
		String typedefult = "yyyy-MM-dd";
		try {
			if (type != null && "".equals(type)) {
				type = typedefult;
			}
			SimpleDateFormat formatter = new SimpleDateFormat(type);
			millisecond = Long.parseLong(Millisecond);
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(millisecond);
			result = formatter.format(calendar.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * *************************************************************************
	 * ************************************* ���ڲ���
	 * *******************************
	 * *******************************************
	 * ************************************
	 */
	// public static void main(String[] args) {
	// DateTime cdt = new DateTime(new Date());
	//
	// int str;
	// str = cdt.getWeekOfYear();
	// System.out.println(str);
	//
	// int date = cdt.getDate();
	// int day = cdt.getDay();
	// int horus = cdt.getHours();
	// int minutes = cdt.getMinutes();
	// int seconds = cdt.getSeconds();
	// System.out.println(date + "|" + day + "|" + horus + "|" + minutes + "|" +
	// seconds);
	// int x = cdt.get("MM");
	// System.out.println(x);
	//
	// System.out.println(DateTime.getTimeExpression(-111110, true));
	//
	// System.out.println(cdt.distanceDayFrom(cdt.getValue() - oneDayValue *
	// 2));
	//
	// System.out.println(cdt.get(DateTime.Datetime_Format_10));
	//
	// System.out.println(cdt.getMyDateTime("yyyy"));
	// System.out.println(DateTime.getBetweenDays("2010��01��01��", "2010��01��02��",
	// DateTime.Datetime_Format_6,
	// DateTime.oneHourValue));
	// }
}
