package co.kr.istarbucks.xo.batch.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import co.kr.istarbucks.xo.batch.exception.XOException;

/**
 * TODO Insert type comment for DateUtil.
 *
 * @author 
 * @version $Revision: 1.1 $
 */

public class DateUtil {

	/**
	 * 
	 */
	public DateUtil() {
	}
    public static boolean isDate(final String textDate) {
        try {
            dateCheck(textDate);
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }

    private static void dateCheck(final String textDate) throws Exception {
        if (textDate.length() != 8) {
            throw new XOException("[" + textDate + "] is not date value");
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.KOREAN);

        try {
            sdf.setLenient(false);
            sdf.parse(textDate);
        }
        catch (Exception e) {
            throw new XOException("[" + textDate + "] is not date value");
        }
        return;
    }

    public static Calendar getCalendar(final String textDate) throws Exception {
        // dateCheck(textDate);
        int year = Integer.parseInt(textDate.substring(0, 4));
        int month = Integer.parseInt(textDate.substring(4, 6));
        int date = Integer.parseInt(textDate.substring(6, 8));

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));

        if (textDate.length() == 14) {
            int hour = Integer.parseInt(textDate.substring(8, 10));
            int minute = Integer.parseInt(textDate.substring(10, 12));
            int second = Integer.parseInt(textDate.substring(12, 14));
            cal.set(year, month - 1, date, hour, minute, second);
        }
        else {
            cal.set(year, month - 1, date);
        }

        return cal;
    }

    public static Date getDate(final String textDate) throws Exception {
        return getCalendar(textDate).getTime();
    }

    public static String getDateString(final Date date, final String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.KOREAN);
        return sdf.format(date);
    }

    public static String getDateString(final Date date) throws Exception {
        return getDateString(date, "yyyyMMdd");
    }

    public static String getDateString(final String textDate, final String pattern) throws Exception {
        String date = null;
        if (textDate != null && !textDate.equals("")) {
            date = getDateString(getDate(textDate), pattern);
        }
        else {
            date = "";
        }
        return date;
    }

    public static String getToday(final String pattern) {
        return getDateString(new Date(), pattern);
    }

    public static String getToday() {
        return getToday("yyyyMMdd");
    }

    public static String getTodayLog() {
    	return getToday("[yyyy-MM-dd HH:mm:ss]");
    }

    public static Date getTodayDate(final String pattern) {
        return new Date();
    }

    public static Date getTodayDate() {
        return new Date();
    }

    public static String getTime() {
        return getToday("HHmmss");
    }

    public static String getTime(final char delmt) {
        return getToday("HH" + delmt + "mm" + delmt + "ss");
    }

    public static String getToDate(final String fromDate, int termDays, final boolean both) throws Exception {
    	int inTermDays = termDays;
        if (both) {
        	inTermDays = inTermDays - 1;
        }
        Calendar cal = getCalendar(fromDate);
        cal.add(Calendar.DATE, inTermDays);
        return getDateString(cal.getTime(), "yyyyMMdd");
    }

    public static String getToMonth(final String fromDate, int termMonths, final boolean both) throws Exception {
    	int inTermMonths = termMonths;
        if (both) {
        	inTermMonths = inTermMonths - 1;
        }
        Calendar cal = getCalendar(fromDate);
        cal.add(Calendar.MONTH, inTermMonths);
        return getDateString(cal.getTime(), "yyyyMMdd");
    }

    public static String getLastDayOfMonth(final String date) throws Exception {
        Calendar cal = getCalendar(date);
        cal.roll(Calendar.MONTH, true);

        String firstDate = getDateString(cal.getTime(), "yyyyMM01");
        Calendar cal2 = getCalendar(firstDate);
        cal2.add(Calendar.DATE, -1);

        return getDateString(cal2.getTime(), "yyyyMMdd");

    }

    public static String getToDate(final String fromDate, final int termDays) throws Exception {
        return getToDate(fromDate, termDays, false);
    }

    public static int getDiffDays(final Date fromDate, final Date toDate, final boolean both) {
        long diffDays = toDate.getTime() - fromDate.getTime();
        long days = diffDays / (24 * 60 * 60 * 1000);
        if (both) {
            if (days >= 0) {
                days += 1;
            }
            else {
                days -= 1;
            }
        }
        return new Long(days).intValue();
    }

    public static int getDiffDays(final Date fromDate, final Date toDate) {
        return getDiffDays(fromDate, toDate, false);
    }

    public static int getDiffDays(final String fromDate, final String toDate, final boolean both) throws Exception {
        return getDiffDays(getDate(fromDate), getDate(toDate), both);
    }

    public static int getDiffDays(final String fromDate, final String toDate) throws Exception {
        return getDiffDays(getDate(fromDate), getDate(toDate), false);
    }

    public static String getDifferDays(final String pattern, final int differDay) {
        Date today = new Date();
        Date yesterday = new Date();
        yesterday.setTime(today.getTime() - ((long) 1000 * 60 * 60 * 24 * differDay));
        return getDateString(yesterday, pattern);
    }

}
