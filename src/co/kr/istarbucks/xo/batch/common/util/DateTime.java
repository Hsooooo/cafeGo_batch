/*
 * @(#) $Id: DateTime.java,v 1.2 2015/09/11 08:14:50 soonwoo Exp $
 * Starbucks SmartOrder
 * Copyright 2013 eZENsolution Co., Ltd. All rights reserved.
 * 601, Daerung Post Tower II, 182-13, Guro 3-dong, Guro-gu
 * Seoul, Korea
 */
package co.kr.istarbucks.xo.batch.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * ��¥, �ð��� ����ȭ�� ���� util Ŭ�����̴�. KOREA Locale�� ���� ?
 * java.uti.Date ��ü�� ����ȭ�� String���� ��ȯ, ���� String�� ����
 * ��¥, �ð� ����ȭ� ���� method���� �����Ǿ� �ִ�.
 * DateTimeŬ�������� ���Ǵ� ����ȭ�� ���� ��Ʈ���� ���Ǵ� Ư�����ڴ�
 * �Ʒ��� ������ ��� �ƽ�Ű ���ڴ� ���� ��Ʈ���� ���Ǵ� Ư�� �����̴�.
 * 
 * <pre>
 *  Symbol   Meaning                 Presentation      Example
 *  ------   -------                 ------------      -------
 * G        era designator          (Text)            AD
 * y        year                    (Number)          1996
 * M        month in year           (Text & Number)   July & 07
 * d        day in month            (Number)          10
 * h        hour in am/pm (1~12)    (Number)          12
 * H        hour in day (0~23)      (Number)          0
 * m        minute in hour          (Number)          30
 * s        second in minute        (Number)          55
 * S        millisecond             (Number)          978
 * E        day in week             (Text)            Tuesday
 * D        day in year             (Number)          189
 * F        day of week in month    (Number)          2 (2nd Wed in July)
 * w        week in year            (Number)          27
 * W        week in month           (Number)          2
 * a        am/pm marker            (Text)            PM
 * k        hour in day (1~24)      (Number)          24
 * K        hour in am/pm (0~11)    (Number)          0
 * z        time zone               (Text)            Pacific Standard Time
 * '        escape for text         (Delimiter)
 * ''       single quote            (Literal)           '
 * 
 * </pre>
 * 
 * @version 1.0
 * @author õ����
 */
public final class DateTime {
	private static Log logger = LogFactory.getLog(DateTime.class); 
	
	/**
	 * Don't let anyone instantiate this class
	 */
	private DateTime () {}
	
	
	/************************************************
	 * �ð� ����
	 *************************************************/
	public static final String[] HOUR_NAME = {"00", "01", "02", "03", "04", "05", "06",
	                                                "07", "08", "09", "10", "11", "12",
	                                                "13", "14", "15", "16", "17", "18",
	                                                "19", "20", "21", "22", "23"};
	public static final String[] HOUR_CODE = {"00", "01", "02", "03", "04", "05", "06",
	                                                "07", "08", "09", "10", "11", "12",
	                                                "13", "14", "15", "16", "17", "18",
	                                                 "19", "20", "21", "22", "23"};
	

	/************************************************
	 * �� ����
	 *************************************************/
	public static final String[] MINUTE_NAME = {"00", "01", "02", "03", "04", "05", "06",
	                                                  "07", "08", "09", "10", "11", "12",
	                                                  "13", "14", "15", "16", "17", "18",
	                                                  "19", "20", "21", "22", "23", "24",
	                                                  "25", "26", "27", "28", "29", "30",
	                                                  "31", "32", "33", "34", "35", "36",
	                                                  "37", "38", "39", "40", "41", "42",
	                                                  "43", "44", "45", "46", "47", "48",
	                                                  "49", "50", "51", "52", "53", "54",
	                                                  "55", "56", "57", "58", "59"};
	public static final String[] MINUTE_CODE = {"00", "01", "02", "03", "04", "05", "06",
	                                                  "07", "08", "09", "10", "11", "12",
	                                                  "13", "14", "15", "16", "17", "18",
	                                                  "19", "20", "21", "22", "23", "24",
	                                                  "25", "26", "27", "28", "29", "30",
	                                                  "31", "32", "33", "34", "35", "36",
	                                                  "37", "38", "39", "40", "41", "42",
	                                                  "43", "44", "45", "46", "47", "48",
	                                                  "49", "50", "51", "52", "53", "54",
	                                                  "55", "56", "57", "58", "59"};
	
	/************************************************
	 * �� ����
	 *************************************************/
	public static final String[] MONTH_CODE = {"1", "2", "3", "4", "5", "6",
	                                                "7", "8", "9", "10", "11", "12",
	                                                "13", "14", "15", "16", "17", "18",
	                                                "19", "20", "21", "22", "23", "24",
	                                                "25", "26", "27", "28", "29", "30",
	                                                "31"};
	
	public static String getDayOfWeekSet ( String dayOfWeekset ) {
		StringBuffer str = new StringBuffer ();
		
		String monCheck = "";
		String tueCheck = "";
		String wedCheck = "";
		String thuCheck = "";
		String friCheck = "";
		String satCheck = "";
		String sunCheck = "";
		
		String[] dayOfWeekSetArr = null;
		boolean characterCheck = OnmUtil.characterCheck (dayOfWeekset, ",");
		if ( characterCheck ) {
			dayOfWeekSetArr = dayOfWeekset.split (",", -1);
		} else {
			dayOfWeekSetArr = new String[] {dayOfWeekset};
		}
		for ( int i = 0; i < dayOfWeekSetArr.length; i++ ) {
			if ( StringUtils.equals ("MON", dayOfWeekSetArr[i]) ) {
				monCheck = "checked";
			}
			if ( StringUtils.equals ("TUE", dayOfWeekSetArr[i]) ) {
				tueCheck = "checked";
			}
			if ( StringUtils.equals ("WED", dayOfWeekSetArr[i]) ) {
				wedCheck = "checked";
			}
			if ( StringUtils.equals ("THU", dayOfWeekSetArr[i]) ) {
				thuCheck = "checked";
			}
			if ( StringUtils.equals ("FRI", dayOfWeekSetArr[i]) ) {
				friCheck = "checked";
			}
			if ( StringUtils.equals ("SAT", dayOfWeekSetArr[i]) ) {
				satCheck = "checked";
			}
			if ( StringUtils.equals ("SUN", dayOfWeekSetArr[i]) ) {
				sunCheck = "checked";
			}
		}
		str.append ("<input type=checkbox name=dayOfWeekSet value=MON ").append (monCheck).append ("> �� ");
		str.append ("<input type=checkbox name=dayOfWeekSet value=TUE ").append (tueCheck).append ("> ȭ ");
		str.append ("<input type=checkbox name=dayOfWeekSet value=WED ").append (wedCheck).append ("> �� ");
		str.append ("<input type=checkbox name=dayOfWeekSet value=THU ").append (thuCheck).append ("> �� ");
		str.append ("<input type=checkbox name=dayOfWeekSet value=FRI ").append (friCheck).append ("> �� ");
		str.append ("<input type=checkbox name=dayOfWeekSet value=SAT ").append (satCheck).append ("> �� ");
		str.append ("<input type=checkbox name=dayOfWeekSet value=SUN ").append (sunCheck).append ("> �� ");
		return str.toString ();
	}
	
	
	/**
	 * check date string validation with the default format "yyyy-MM-dd".
	 * 
	 * @param s date string you want to check with default format "yyyy-MM-dd".
	 */
	public static boolean check ( String s ) throws Exception {
		return DateTime.check (s, "yyyy-MM-dd");
	}
	
	/**
	 * check date string validation with an user defined format.
	 * 
	 * @param s date string you want to check.
	 * @param format string representation of the date format.
	 *        For example, "yyyy-MM-dd".
	 */
	public static boolean check ( String s, String format ) throws java.text.ParseException {
//		if ( s == null ) throw new NullPointerException("date string to check is null");
//		if ( format == null ) throw new NullPointerException("format string to check date is null");
		
		if ( s == null ) return false;
		if ( format == null ) return false;
		
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat (format, java.util.Locale.KOREA);
		java.util.Date date = null;
		try {
			date = formatter.parse (s);
		} catch ( java.text.ParseException e ) {
//			throw new java.text.ParseException(	e.getMessage() + " with format \"" + format + "\"",	e.getErrorOffset());
			return false;
		}
		
		if ( !formatter.format (date).equals (s) ) { throw new java.text.ParseException ("Out of bound date:\"" + s + "\" with format \"" + format + "\"", 0); }
		
		return true;
	}
	
	/**
	 * check date string validation with the default format "yyyyMMdd".
	 * 
	 * @param s date string you want to check with default format "yyyyMMdd"
	 * @return boolean true ��¥ ������ �°�, �����ϴ� ��¥�� ��
	 *         false ��¥ ������ ���� �ʰų�, �������� �ʴ� ��¥�� ��
	 */
	public static boolean isValid ( String s ) throws Exception {
		return DateTime.isValid (s, "yyyyMMdd");
	}
	
	/**
	 * check date string validation with an user defined format.
	 * 
	 * @param s date string you want to check.
	 * @param format string representation of the date format.
	 *        For example, "yyyy-MM-dd".
	 * @return boolean true ��¥ ������ �°�, �����ϴ� ��¥�� ��
	 *         false ��¥ ������ ���� �ʰų�, �������� �ʴ� ��¥�� ��
	 */
	public static boolean isValid ( String s, String format ) {
		
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat (format, java.util.Locale.KOREA);
		java.util.Date date = null;
		try {
			date = formatter.parse (s);
		} catch ( java.text.ParseException e ) {
			return false;
		}
		
		if ( !formatter.format (date).equals (s) ) return false;
		
		return true;
	}
	
	/**
	 * @return formatted string representation of current day with
	 *         "yyyy-MM-dd".
	 */
	public static String getDateString () {
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat ("yyyy-MM-dd", java.util.Locale.KOREA);
		return formatter.format (new java.util.Date ());
	}
	
	/**
	 * For example, String time =
	 * DateTime.getFormatString("yyyy-MM-dd HH:mm:ss");
	 * 
	 * @param java.lang.String pattern "yyyy, MM, dd, HH, mm, ss and more"
	 * @return formatted string representation of current day and
	 *         time with your pattern.
	 */
	public static String getFormatString ( String pattern ) {
		return getFormatString (new java.util.Date (), pattern);
	}
	
	/**
	 * @param java.lang.String pattern "yyyy, MM, dd, HH, mm, ss and more"
	 * @return formatted string representation of current day and
	 *         time with your pattern.
	 */
	public static String getFormatString ( java.util.Date date, String pattern ) {
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat (pattern, java.util.Locale.KOREA);
		String dateString = formatter.format (date);
		return dateString;
	}
	
	
	/**
	 * @return formatted string representation of current day with
	 *         "yyyyMMdd".
	 */
	public static String getShortDateString () {
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat ("yyyyMMdd", java.util.Locale.KOREA);
		return formatter.format (new java.util.Date ());
	}
	
	/**
	 * @return formatted string representation of current time with
	 *         "HHmmss".
	 */
	public static String getShortTimeString () {
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat ("HHmmss", java.util.Locale.KOREA);
		return formatter.format (new java.util.Date ());
	}
	
	/**
	 * @return formatted string representation of current time with
	 *         "yyyy-MM-dd-HH:mm:ss".
	 */
	public static String getTimeStampString () {
		java.text.SimpleDateFormat formatter =
		    new java.text.SimpleDateFormat ("yyyy-MM-dd-HH:mm:ss:SSS", java.util.Locale.KOREA);
		return formatter.format (new java.util.Date ());
	}
	
	/**
	 * @return formatted string representation of current time with
	 *         "yyyy-MM-dd-HH:mm:ss".
	 */
	public static String getFileTimeStampString () {
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat ("yyyyMMddHHmmss", java.util.Locale.KOREA);
		return formatter.format (new java.util.Date ());
	}
	
	/**
	 * @return formatted string representation of current time with
	 *         "HH:mm:ss".
	 */
	public static String getTimeString () {
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat ("HH:mm:ss", java.util.Locale.KOREA);
		return formatter.format (new java.util.Date ());
	}
	
	public static java.util.Date getDate ( String value, String format ) throws ParseException {
		if ( value == null ) return new java.util.Date ();
		if ( format == null ) return getDate (value);
		
		SimpleDateFormat fmt = null;
		fmt = new SimpleDateFormat (format, java.util.Locale.KOREA);
		return fmt.parse (value);
	}
	
	/**
	 * fmt: yyyy-MM-dd-HH:mm:ss:SSS" 23
	 * fmt: yyyy-MM-dd-HH:mm:ss" 19
	 * fmt: yyyy-MM-dd-HH:mm" 16
	 * fmt: yyyy/MM/dd" 10
	 * fmt: yyyyMMddHHmmss" 14
	 * fmt: yyyyMMddHHmm" 12
	 * fmt: yyyyMMddHH" 10
	 * fmt: yyyyMMdd" 8
	 */
	@SuppressWarnings ( "null" )
	public static java.util.Date getDate ( String format ) throws ParseException {
		
		boolean isCHAR = false;
		if ( format == null || format.length () < 1 ) return new java.util.Date ();
		
		if ( format.indexOf ('-') != -1 || format.indexOf ('/') != -1 || format.indexOf (':') != -1 ) isCHAR = true;
		
		SimpleDateFormat fmt = null;
		int flength = format.length ();
		switch ( flength ) {
			case 23:
				fmt = new SimpleDateFormat ("yyyy-MM-dd-HH:mm:ss:SSS", java.util.Locale.KOREA);
				break;
			case 21:
				fmt = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss.S", java.util.Locale.KOREA);
				break;
			case 19:
				fmt = new SimpleDateFormat ("yyyy-MM-dd-HH:mm:ss", java.util.Locale.KOREA);
				break;
			case 16:
				fmt = new SimpleDateFormat ("yyyy-MM-dd-HH:mm", java.util.Locale.KOREA);
				break;
			case 14:
				fmt = new SimpleDateFormat ("yyyyMMddHHmmss", java.util.Locale.KOREA);
				break;
			case 12:
				fmt = new SimpleDateFormat ("yyyyMMddHHmm", java.util.Locale.KOREA);
				break;
			case 10:
				if ( isCHAR ) fmt = new SimpleDateFormat ("yyyy/MM/dd", java.util.Locale.KOREA);
				else fmt = new SimpleDateFormat ("yyyyMMddHH", java.util.Locale.KOREA);
				break;
			case 8:
				fmt = new SimpleDateFormat ("yyyyMMdd", java.util.Locale.KOREA);
				break;
			default:
				throw new ParseException ("not supported format !!! " + format, 0);
		}
		return fmt.parse (format);
	}
	
	/**
	 * @return formatted string representation of current time with
	 *         "HHmmss".
	 */
	public static String getShortTimeHHString ( java.util.Date date ) {
		if ( date == null ) return "";
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat ("HH", java.util.Locale.KOREA);
		return formatter.format (date);
	}
	
	/**
	 * @return formatted string representation of current time with
	 *         "mm".
	 */
	public static String getShortTimeMMString ( java.util.Date date ) {
		if ( date == null ) return "";
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat ("mm", java.util.Locale.KOREA);
		return formatter.format (date);
	}
	
	/**
	 * @return formatted string representation of current day with
	 *         "yyyyMMdd".
	 */
	public static String getShortDateString ( java.util.Date date ) {
		if ( date == null ) return "";
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat ("yyyy-MM-dd", java.util.Locale.KOREA);
		return formatter.format (date);
	}
	
	/**
	 * @return formatted string representation of current day with
	 *         "yyyy-MM-dd HH:mm:ss".
	 */
	public static String getDatetimeString ( java.util.Date date ) {
		if ( date == null ) return "";
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat ("yyyy-MM-dd HH:mm:ss", java.util.Locale.KOREA);
		return formatter.format (date);
	}
	
	public static String getDatetimeString ( String format ) {
		if ( format == null ) return "yyyyMMddHHmmss";
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat (format, java.util.Locale.KOREA);
		return formatter.format (new Date ());
	}
	
	
	public static String makeTowDigit ( int num ) {
		return ( num < 10 ? "0" : "" ) + num;
	}
	
	/**
	 * ��Ż� ���γ�¥�� DB ����� ��¥�� ��ȯ
	 * @param telAppDate
	 * @return
	 */
	public static String getTelApplDateToDbApplDate(String telAppDate){
		String inTelAppdate = telAppDate;
		if(!StringUtils.isBlank(inTelAppdate)){
			inTelAppdate = inTelAppdate.substring(2);							//�� �⵵ 2�ڸ� ����
			inTelAppdate = DateTime.getCurrentDate(6) + inTelAppdate;			//4�ڸ� �⵵ �߰�
			inTelAppdate = inTelAppdate.substring(0, inTelAppdate.length()-1);	//�ǵ��� �������� ����
		}else{
			return "";
		}
		
		return inTelAppdate;
	}
	
	/**
	 * DB ����� ��¥�� ��Ż� ���γ�¥�� ��ȯ
	 * @param dbApplDate
	 * @return
	 */
	public static String getDbApplDateToTelApplDate(String dbApplDate){
		String inDbApplDate = dbApplDate;
		if(!StringUtils.isBlank(inDbApplDate)){
			try{
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREAN);
				Date date = sdf.parse(inDbApplDate);
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				
				//1:�Ͽ��� ~ 7:�����
				int dayIdx = cal.get(Calendar.DAY_OF_WEEK);
				
				inDbApplDate = inDbApplDate.substring(2);				//�� �⵵ 2�ڸ� ����
				inDbApplDate = inDbApplDate + (dayIdx - 1) + "";		//�ǵڿ� �������� �߰�(0:�Ͽ��� ~ 6:�����)
			}catch(Exception ex){
				//System.out.println("ex : " + ex);
				logger.error(ex.getMessage(), ex);
			}
		}else{
			return "";
		}
		
		return inDbApplDate;
	}
	
	
	public static String getCurrentDate(int caseCode){
		String format = null;
		
		switch(caseCode){
			case 1:
				format = "yyyy-MM-dd HH:mm:ss";
				break;
			case 2:
				format = "yyyy-MM-dd HH:mm";
				break;
			case 3:
				format = "yyyy-MM-dd HH";
				break;
			case 4:
				format = "yyyy-MM-dd";
				break;
			case 5:
				format = "yyyy-MM";
				break;
			case 6:
				format = "yyyy";
				break;
			case 7:
				format = "HH:mm:ss";
				break;
			case 8:
				format = "HH:mm";
				break;
			case 9:
				format = "HH";
				break;
			case 10:
				format = "MM/dd";
				break;
			case 11:
				format = "yyyyMMddHH:mm:ss";
				break;
			case 12:
				format = "yyyy�� MM�� dd��";
				break;
			case 13:
				format = "yyyyMMddHHmmss";
				break;					
			case 14:
				format = "yyyyMMdd";
				break;	
			case 15:
				format = "HHmmss";
				break;
			default:
				format = "yyyy-MM-dd HH:mm:ss";
				break;
		}
			
		FastDateFormat fdf = FastDateFormat.getInstance(format, Locale.KOREA);
		return fdf.format(new Date());
	}
}
