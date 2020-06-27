package com.rolvatech.cgc.utils;

/**
 * Created by viya on 20-10-2017.
 */

import android.content.Context;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class DateUtils {
//    protected static Log logger = LogFactory.getLog(DateUtil.class);

    public static final String FORMAT_ONE = "yyyy-MM-dd HH:mm:ss";

    public static final String FORMAT_YYYYMMDDTHHMMSS = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String FORMAT_YYYYMMDDTHHMMSSZ = "yyyy-MM-dd'T'hh:mm:ssZ";

    public static final String FORMAT_SIMPLE= "dd/MM/yyyy HH:mm";


    public static final String FORMAT_TWO = "yyyy-MM-dd HH:mm";

    public static final String FORMAT_THREE = "yyyyMMdd-HHmmss";

    public static final String LONG_DATE_FORMAT = "yyyy-MM-dd";

    public static final String SHORT_DATE_FORMAT = "MM-dd";

    public static final String LONG_TIME_FORMAT_SS = "HH:mm:ss";

    public static final String LONG_TIME_FORMAT = "HH:mm";

    public static final String MONTG_DATE_FORMAT = "yyyy-MM";

    public static final int SUB_YEAR = Calendar.YEAR;

    public static final int SUB_MONTH = Calendar.MONTH;

    public static final int SUB_DAY = Calendar.DATE;

    public static final int SUB_HOUR = Calendar.HOUR;

    public static final int SUB_MINUTE = Calendar.MINUTE;

    public static final int SUB_SECOND = Calendar.SECOND;

    static final String dayNames[] = {"", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday",
            "Saturday", "Sunday"};

    @SuppressWarnings("unused")
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    public DateUtils() {
    }

    /**
     * @param dateStr
     * @return
     */
    public static Date stringtoDate(String dateStr, String format) {
        Date d = null;
        SimpleDateFormat formater = new SimpleDateFormat(format);
        try {
            formater.setLenient(false);
            d = formater.parse(dateStr);
        } catch (Exception e) {
            // log.error(e);
            d = null;
        }
        return d;
    }

    /**
     *
     */
    public static Date stringtoDate(String dateStr, String format,
                                              ParsePosition pos) {
        Date d = null;
        SimpleDateFormat formater = new SimpleDateFormat(format);
        try {
            formater.setLenient(false);
            d = formater.parse(dateStr, pos);
        } catch (Exception e) {
            d = null;
        }
        return d;
    }

    /**
     * @param date
     * @return
     */
    public static String dateToString(Date date, String format) {
        String result = "";
        SimpleDateFormat formater = new SimpleDateFormat(format);
        try {
            result = formater.format(date);
        } catch (Exception e) {
            // log.error(e);
        }
        return result;
    }

    public static String getDate(int year, int month, int date, String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, date);
        return dateToString(calendar.getTime(), format);
    }

    /**
     * @param format
     * @return
     */
    public static String getCurrDate(String format) {
        return getCurrentUTCDate(format);
    }
    public static String getCurrDeviceDate(String format) {
        return getCurrentDate(format);
    }


    public static String getFormmatedDate(String date, String currentFormat, String expectedFormat) {
        try {
            SimpleDateFormat e = new SimpleDateFormat(currentFormat);
            e.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date dateObj = e.parse(date);
            return dateToString(dateObj, expectedFormat);
        } catch (Exception var5) {
            var5.printStackTrace();
            return "";
        }
    }
    public static String getCurrentDate(String expectedFormat) {
        DateFormat dateFormat = new SimpleDateFormat(expectedFormat);
        return dateFormat.format(new Date());
    }
    public static String getCurrentUTCDate(String expectedFormat) {
        DateFormat dateFormat = new SimpleDateFormat(expectedFormat);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(new Date());
    }

    /**
     * @param dateStr
     * @param amount
     * @return
     */
    public static String dateSub(int dateKind, String dateStr, int amount) {
        Date date = stringtoDate(dateStr, FORMAT_ONE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(dateKind, amount);
        return dateToString(calendar.getTime(), FORMAT_ONE);
    }

    /**
     * @param firstTime
     * @param secTime
     * @return
     */
    public static long timeSub(String firstTime, String secTime) {
        long first = stringtoDate(firstTime, FORMAT_ONE).getTime();
        long second = stringtoDate(secTime, FORMAT_ONE).getTime();
        return (second - first) / 1000;
    }

    /**
     * @param year  int
     * @param month int
     * @return int
     */
    public static int getDaysOfMonth(String year, String month) {
        int days = 0;
        if (month.equals("1") || month.equals("3") || month.equals("5")
                || month.equals("7") || month.equals("8") || month.equals("10")
                || month.equals("12")) {
            days = 31;
        } else if (month.equals("4") || month.equals("6") || month.equals("9")
                || month.equals("11")) {
            days = 30;
        } else {
            if ((Integer.parseInt(year) % 4 == 0 && Integer.parseInt(year) % 100 != 0)
                    || Integer.parseInt(year) % 400 == 0) {
                days = 29;
            } else {
                days = 28;
            }
        }

        return days;
    }

    /**
     * @param year  int
     * @param month int 月份[1-12]
     * @return int
     */
    public static int getDaysOfMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * @return int
     */
    public static int getToday() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DATE);
    }

    /**
     * @return int
     */
    public static int getToMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * @return int
     */
    public static int getToYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    /**
     * @param date Date
     * @return int
     */
    public static int getDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DATE);
    }

    /**
     * @param date Date
     * @return int
     */
    public static int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * @param date Date
     * @return int
     */
    public static int getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * @param date1 Date
     * @param date2 Date
     * @return long
     */
    public static long dayDiff(Date date1, Date date2) {
        return (date2.getTime() - date1.getTime()) / 86400000;
    }

    /**
     * @param before
     * @param after
     * @return
     */
    public static int yearDiff(String before, String after) {
        Date beforeDay = stringtoDate(before, LONG_DATE_FORMAT);
        Date afterDay = stringtoDate(after, LONG_DATE_FORMAT);
        return getYear(afterDay) - getYear(beforeDay);
    }

    /**
     * @param after
     * @return
     */
    public static int yearDiffCurr(String after) {
        Date beforeDay = new Date();
        Date afterDay = stringtoDate(after, LONG_DATE_FORMAT);
        return getYear(beforeDay) - getYear(afterDay);
    }

    /**
     * @param before
     * @return
     * @author chenyz
     */
    public static long dayDiffCurr(String before) {
        Date currDate = DateUtils.stringtoDate(currDay(), LONG_DATE_FORMAT);
        Date beforeDate = stringtoDate(before, LONG_DATE_FORMAT);
        return (currDate.getTime() - beforeDate.getTime()) / 86400000;

    }

    /**
     * @param before
     * @return
     * @author chenyz
     */
    public static long getDiffInMinutes(String before) {
        Date currDate = DateUtils.stringtoDate(getCurrDate(FORMAT_YYYYMMDDTHHMMSSZ), FORMAT_YYYYMMDDTHHMMSSZ);
        Date beforeDate = stringtoDate(before, FORMAT_YYYYMMDDTHHMMSSZ);
        return (currDate.getTime() - beforeDate.getTime()) / 60000;

    }

    /**
     * @param year
     * @param month
     * @return
     * @author chenyz
     */
    public static int getFirstWeekdayOfMonth(int year, int month) {
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.SATURDAY); // 星期天为第一天
        c.set(year, month - 1, 1);
        return c.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * @param year
     * @param month
     * @return
     * @author chenyz
     */
    public static int getLastWeekdayOfMonth(int year, int month) {
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.SATURDAY); // 星期天为第一天
        c.set(year, month - 1, getDaysOfMonth(year, month));
        return c.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * "yyyy_MM_dd_HH_mm_ss"
     *
     * @return
     */
    public static String getCurrent() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        StringBuffer sb = new StringBuffer();
        sb.append(year).append("_").append(addzero(month, 2))
                .append("_").append(addzero(day, 2)).append("_")
                .append(addzero(hour, 2)).append("_").append(
                addzero(minute, 2)).append("_").append(
                addzero(second, 2));
        return sb.toString();
    }

    /**
     * "yyyy-MM-dd HH:mm:ss"
     *
     * @return
     */
    public static String getNow() {
        Calendar today = Calendar.getInstance();
        return dateToString(today.getTime(), FORMAT_ONE);
    }

    /**
     * @param birth YYYY-mm-dd
     * @return
     */
    public static String getAstro(String birth) {
        if (!isDate(birth)) {
            birth = "2000" + birth;
        }
        if (!isDate(birth)) {
            return "";
        }
        int month = Integer.parseInt(birth.substring(birth.indexOf("-") + 1,
                birth.lastIndexOf("-")));
        int day = Integer.parseInt(birth.substring(birth.lastIndexOf("-") + 1));
        String s = "魔羯水瓶双鱼牡羊金牛双子巨蟹狮子处女天秤天蝎射手魔羯";
        int[] arr = {20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22};
        int start = month * 2 - (day < arr[month - 1] ? 2 : 0);
        return s.substring(start, start + 2) + "座";
    }

    /**
     * @param date YYYY-mm-dd
     * @return
     */
    public static boolean isDate(String date) {
        StringBuffer reg = new StringBuffer(
                "^((\\d{2}(([02468][048])|([13579][26]))-?((((0?");
        reg.append("[13578])|(1[02]))-?((0?[1-9])|([1-2][0-9])|(3[01])))");
        reg.append("|(((0?[469])|(11))-?((0?[1-9])|([1-2][0-9])|(30)))|");
        reg.append("(0?2-?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][12");
        reg.append("35679])|([13579][01345789]))-?((((0?[13578])|(1[02]))");
        reg.append("-?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))");
        reg.append("-?((0?[1-9])|([1-2][0-9])|(30)))|(0?2-?((0?[");
        reg.append("1-9])|(1[0-9])|(2[0-8]))))))");
        Pattern p = Pattern.compile(reg.toString());
        return p.matcher(date).matches();
    }

    public static Date nextMonth(Date date, int months) {
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }
        cal.add(Calendar.MONTH, months);
        return cal.getTime();
    }

    public static Date nextDay(Date date, int day) {
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }
        cal.add(Calendar.DAY_OF_YEAR, day);
        return cal.getTime();
    }


    public static String nextDay(int day, String format) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, day);
        return dateToString(cal.getTime(), format);
    }


    public static Date nextWeek(Date date, int week) {
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }
        cal.add(Calendar.WEEK_OF_MONTH, week);
        return cal.getTime();
    }


    public static String currDay() {
        return DateUtils.dateToString(new Date(), DateUtils.LONG_DATE_FORMAT);
    }


    public static String befoDay() {
        return befoDay(DateUtils.LONG_DATE_FORMAT);
    }


    public static String befoDay(String format) {
        return DateUtils.dateToString(DateUtils.nextDay(new Date(), -1), format);
    }


    public static String afterDay() {
        return DateUtils.dateToString(DateUtils.nextDay(new Date(), 1),
                DateUtils.LONG_DATE_FORMAT);
    }


    public static int getDayNum() {
        int daynum = 0;
        GregorianCalendar gd = new GregorianCalendar();
        Date dt = gd.getTime();
        GregorianCalendar gd1 = new GregorianCalendar(1900, 1, 1);
        Date dt1 = gd1.getTime();
        daynum = (int) ((dt.getTime() - dt1.getTime()) / (24 * 60 * 60 * 1000));
        return daynum;
    }

    public static Date getDateByNum(int day) {
        GregorianCalendar gd = new GregorianCalendar(1900, 1, 1);
        Date date = gd.getTime();
        date = nextDay(date, day);
        return date;
    }


    public static String getYmdDateCN(String datestr) {
        if (datestr == null)
            return "";
        if (datestr.length() < 10)
            return "";
        StringBuffer buf = new StringBuffer();
        buf.append(datestr.substring(0, 4)).append(datestr.substring(5, 7))
                .append(datestr.substring(8, 10));
        return buf.toString();
    }


    public static String getFirstDayOfMonth(String format) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, 1);
        return dateToString(cal.getTime(), format);
    }


    public static String getLastDayOfMonth(String format) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, 1);
        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.DATE, -1);
        return dateToString(cal.getTime(), format);
    }


    public static String addzero(int sourceDate, int formatLength) {
     /*
      * 0 指前面补充零
      * formatLength 字符总长度为 formatLength
      * d 代表为正数。
      */
        String newString = String.format("%0" + formatLength + "d", sourceDate);
        return newString;
    }


    public static String getWeek() {
        Calendar cal = Calendar.getInstance();
        return dayNames[cal.get(Calendar.DAY_OF_WEEK)];
    }

    public static String getCurrentTimeBy12_24(Context context) {
        if (android.text.format.DateFormat.is24HourFormat(context)) {
            return getCurrDate(LONG_TIME_FORMAT);
        } else {
            StringBuilder sb = new StringBuilder();
            int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            int minute = Calendar.getInstance().get(Calendar.MINUTE);
            if (hour >= 12) {
                sb.append(hour - 12).append(":").append(minute).append(" PM");
            } else {
                sb.append(hour).append(":").append(minute).append(" AM");
            }
            return sb.toString();
        }
    }
}