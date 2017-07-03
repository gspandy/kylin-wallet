package com.rongcapital.wallet.util.date;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateUtil {

    public static final String YYYY_MM_DD_HHMMSS = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MM_DD_HHMMSSSSS = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYYMMDD = "yyyyMMdd";
    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    private static final Map<String, DateTimeFormatter> mapFmt = new HashMap<>();

    /*---------------初始化jodatime----------------*/
    static {
        mapFmt.put(YYYY_MM_DD_HHMMSS, DateTimeFormat.forPattern(YYYY_MM_DD_HHMMSS));
        mapFmt.put(YYYY_MM_DD_HHMMSSSSS, DateTimeFormat.forPattern(YYYY_MM_DD_HHMMSSSSS));
        mapFmt.put(YYYY_MM_DD, DateTimeFormat.forPattern(YYYY_MM_DD));
        mapFmt.put(YYYYMMDD, DateTimeFormat.forPattern(YYYYMMDD));
        mapFmt.put(YYYYMMDDHHMMSS, DateTimeFormat.forPattern(YYYYMMDDHHMMSS));
    }

    public static DateTimeFormatter getFmt(String format) {
        if (mapFmt.containsKey(format)) {
            return mapFmt.get(format);
        } else {
            mapFmt.put(format, DateTimeFormat.forPattern(format));
            return DateTimeFormat.forPattern(format);
        }

    }

    /**
     * Discription:格式转换
     * 
     * @param date
     * @param format
     * @return Date
     */
    // public static Date getDateFormat(String format, String date) {
    // SimpleDateFormat sdf = getSdf(format);
    // Date dateForm = null;
    // try {
    // dateForm = sdf.parse(date);
    // } catch (Exception e) {
    // e.getMessage();
    // }
    // return dateForm;
    // }

    /**
     * Discription:格式转换
     * 
     * @param date
     * @param format
     * @return Date
     */
    public static Date getDateFormat(String format, String date) {
        try {
            return getFmt(format).parseDateTime(date).toDate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前时间,的转换格式 Discription:
     * 
     * @param format
     * @return String
     * @author Achilles
     * @since 2016年3月9日
     */
    // public static String getCurrentStrDateFormat(String format) {
    // SimpleDateFormat sdf = getSdf(format);
    // return sdf.format(new Date());
    // }

    /**
     * 获取当前时间,的转换格式 Discription:
     * 
     * @param format
     * @return String
     * @author Achilles
     * @since 2016年3月9日
     */
    public static String getCurrentStrDateFormat(String format) {
        return getFmt(format).print(new Date().getTime());
    }

    /**
     * 格式转换
     * 
     * @param format
     * @param date
     * @return
     */
    // public static String getStrDateFormat(String format, Date date) {
    // SimpleDateFormat sdf = getSdf(format);
    // return sdf.format(date);
    // }

    public static String getStrDateFormat(String format, Date date) {
        return getFmt(format).print(date.getTime());
    }

    /**
     * 转换时间格式为毫秒数
     * 
     * @param format
     * @param date
     * @return
     */
    // public static long getMillisByStrDate(String format, Date date) {
    // SimpleDateFormat sdf = getSdf(format);
    // long milliSecond = 0;
    // try {
    // milliSecond = sdf.parse(sdf.format(date)).getTime();
    // } catch (ParseException e) {
    // e.printStackTrace();
    // }
    // return milliSecond;
    // }

    public static long getMillisByStrDate(String format, Date date) {
        DateTimeFormatter fmt = getFmt(format);
        long milliSecond = 0;
        try {
            milliSecond = fmt.parseMillis(getFmt(format).print(date.getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return milliSecond;
    }

    /**
     * Discription:判断当前时间是否大于传入时间date max毫秒
     * 
     * @param date
     * @param maxLock
     */
    public static boolean compareDate(Date date, long max) {
        long last = System.currentTimeMillis() - getMillisByStrDate(DateUtil.YYYY_MM_DD_HHMMSSSSS, date) - max;
        if (last >= 0) {
            return true;
        }
        return false;
    }


    public static boolean checkDateFormatAndValite(String str) {

        DateTimeFormatter format = getFmt(YYYY_MM_DD_HHMMSS);

        boolean result = false;
        try {
            format.parseDateTime(str);
            result = true;
        } catch (Exception e) {

            result = false;
        }

        return result;
    }

    /**
     * 转换日期str－> date
     * 
     * @param
     * @return
     */
    /*
     * public static Date stringToDate(String str) { // DateFormat format = new SimpleDateFormat(YYYY_MM_DD_HHMMSS);
     * DateFormat format = getSdf(YYYY_MM_DD_HHMMSS); Date date = null; try { date = format.parse(str); } catch
     * (ParseException e) { throw new AccountException("WH1135"); }
     * 
     * return date; }
     */

    public static Date stringToDate(String str) {
        try {
            return getFmt(YYYY_MM_DD_HHMMSS).parseDateTime(str).toDate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isYYYYMMDD(String dateStr) {
        if (dateStr == null) {
            return false;
        }
        String eL =
                "^(20[0-3][0-9]-(0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|(20[0-3][0-9]-(0[2469]|11)-(0[1-9]|[12][0-9]|30))$";
        Pattern p = Pattern.compile(eL);
        Matcher m = p.matcher(dateStr);
        boolean dateFlag = m.matches();
        return dateFlag;
    }

    /**
     * 校验日期格式是否是：yyyy-MM-dd hh:mm:ss
     * 
     * @param dateStr
     * @return
     */
    public static boolean isYYYYMMDDHHMMSS(String dateStr) {
        if (dateStr == null) {
            return false;
        }
        String eL =
                "^(((20[0-3][0-9]-(0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|(20[0-3][0-9]-(0[2469]|11)-(0[1-9]|[12][0-9]|30))) (20|21|22|23|[0-1][0-9]):[0-5][0-9]:[0-5][0-9])$";
        Pattern p = Pattern.compile(eL);
        Matcher m = p.matcher(dateStr);
        boolean dateFlag = m.matches();
        return dateFlag;

    }

    public static void main(String[] args) {
        // System.out.println(DateUtil.getCurrentStrDateFormat(DateUtil.YYYY_MM_DD_HHMMSS));
        // ;
        // Date d = getDateFormat(DateUtil.YYYYMMDD, "20160913");
        // System.out.println(d);
        System.out.println(getMillisByStrDate(DateUtil.YYYYMMDD, new Date()));

    }
}
