package co.kr.cafego.common.util;


import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.time.FastDateFormat;

public final class DateTime {
	
	/**
	 * 현재 시간정보 반환 						<br/>
	 * caseCode								<br/>
	 * 		1: yyyy-MM-dd HH:mm:ss			<br/>
	 * 		2: yyyy-MM-dd HH:mm				<br/>
	 * 		3: yyyy-MM-dd HH				<br/>
	 * 		4: yyyy-MM-dd					<br/>
	 * 		5: yyyy-MM						<br/>
	 * 		6: yyyy							<br/>
	 * 		7: HH:mm:ss						<br/>
	 * 		8: HH:mm						<br/>
	 * 		9: HH							<br/>
	 * 	 	10: MM/dd						<br/>
	 *      11: yyyyMMddHH:mm:ss            <br/>
	 *      12: yyyy년 MM월 dd일           	<br/>
	 *      13: yyyyMMddHHmmss              <br/>
	 *      14: yyyyMMdd		            <br/>
	 *      15: HHmmss			            <br/>
	 *      16: HH:mmssyyddMM				<br/>
	 * 		default: yyyy-MM-dd HH:mm:ss	<br/>
	 * @param caseCode
	 * @return
	 */
	public static String getCurrentDate(int caseCode){
		String format = null;
		
		switch(caseCode){
			case 1:
				format = "yyyyMMddHHmm";
				break;
			case 14:
				format = "yyyyMMdd";
				break;	
			case 15:
				format = "HHmmss";
				break;
			case 16:
				format = "HH:mmssyyddMM";
				break;
			default:
				format = "yyyy-MM-dd HH:mm:ss";
				break;
		}
			
		FastDateFormat fdf = FastDateFormat.getInstance(format, Locale.KOREA);
		return fdf.format(new Date());
	}
	
	public static String getBeforeDate(int beforeDay) {
		DecimalFormat df = new DecimalFormat("00");
		Calendar currentCalendar = Calendar.getInstance();
		
		currentCalendar.add(Calendar.DATE, -(beforeDay));
		String strYear  = Integer.toString(currentCalendar.get(Calendar.YEAR));
		String strMonth = df.format(currentCalendar.get(Calendar.MONTH) + 1);
		String strDay   = df.format(currentCalendar.get(Calendar.DATE));
		String strDate  = strYear + strMonth + strDay;
		
		return strDate;
	}
	
	/**
	 * check date string validation with the default format "yyyyMMdd".
	 * 
	 * @param s date string you want to check with default format "yyyyMMdd"
	 * @return boolean true 날짜 형식이 맞고, 존재하는 날짜일 때
	 *         false 날짜 형식이 맞지 않거나, 존재하지 않는 날짜일 때
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
	 * @return boolean true 날짜 형식이 맞고, 존재하는 날짜일 때
	 *         false 날짜 형식이 맞지 않거나, 존재하지 않는 날짜일 때
	 */
	public static boolean isValid ( String s, String format ) {
		
		SimpleDateFormat formatter = new SimpleDateFormat (format, Locale.KOREA);
		Date date = null;
		try {
			date = formatter.parse (s);
		} catch ( java.text.ParseException e ) {
			return false;
		}
		
//		if ( !formatter.format (date).equals (s) ) return false;
//		
//		return true;
		return formatter.format (date).equals (s);
	}
	
	/**
	 * @param java.lang.String pattern "yyyy, MM, dd, HH, mm, ss and more"
	 * @return formatted string representation of current day and
	 *         time with your pattern.
	 */
	public static String getFormatString ( Date date, String pattern ) {
		SimpleDateFormat formatter = new SimpleDateFormat (pattern, Locale.KOREA);
		String dateString = formatter.format (date);
		return dateString;
	}
	
	
	/**
	 * 리턴 Date 형식 yyyy.MM.dd
	 * @param date
	 * @return
	 */
	public static String getStringDate(Date date) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREA);
		
		if(date != null ) {
			return sdf.format(date);
		} else {
			return "";
		}
	}
	
	
	
	/**
	 * 네이버에서 전달된 시간타입을
	 * yyyyMMddHHmmss로 변경
	 * 
	 * 2020-05-13T15:38:25.264+09:00 -> 20200513153825
	 * @param srcDate
	 * @return
	 */
	public static String naverDateToString(String srcDate) {
		if(srcDate != null) {
			StringBuffer sb = new StringBuffer(srcDate);
			sb.delete(srcDate.indexOf('.'), srcDate.length());
			
			String dateSplitT[] = sb.toString().split("T");
			
			String date = dateSplitT[0].replaceAll("-", "");
			String time = dateSplitT[1].replaceAll(":","");
			
			return date+time;
		}else {
			return "";
		}
		
	}
	
}

