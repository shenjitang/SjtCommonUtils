package org.shenjitang.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 用于操作日期的类
 * </p>
 *
 * @author 雷钦
 */
public class DateUtils {

    private static ThreadLocal<Map<String, DateFormat>> local = new ThreadLocal<Map<String, DateFormat>>() {

        protected Map<String, DateFormat> initialValue() {
            return new HashMap<String, DateFormat>();
        }

        ;
    };
    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static String DATE_FORMAT_STR = "yyyy-MM-dd HH:mm:ss";
    /**
     * yyyyMMddHHmmss
     */
    public static String DATE_FORMAT_STR2 = "yyyyMMddHHmmss";
    /**
     * yyyyMMdd
     */
    public static String DATE_FORMAT_STR3 = "yyyyMMdd";
    /**
     * yyyy-MM-dd
     */
    public static String DATE_FORMAT_STR4 = "yyyy-MM-dd";
    /**
     * yyyy年MM月dd日
     */
    public static String DATE_FORMAT_STR5 = "yyyy年MM月dd日";
    /**
     * yyyyMM
     */
    public static String DATE_FORMAT_STR6 = "yyyyMM";
    /**
     * yy-MM-dd
     */
    public static String DATE_FORMAT_STR7 = "yy-MM-dd";
    /**
     * yyyy-M-d
     */
    public static String DATE_FORMAT_STR8 = "yyyy-M-d";
    public static String DATE_FORMAT_STR9 = "M-d";
    public static String DATE_FORMAT_STR10 = "yyyyMMddHH";
    /**
     * yyyy
     */
    public static String DATE_FORMAT_STR11 = "yyyy";

    private DateUtils() {
    }

    /**
     * <p>
     * 将日期按指定格式格式化
     * </p>
     *
     * @param date   將要格式化的日期
     * @param format 格式化的格式
     * @return 格式化后的字符串
     */
    public static String format(Date date, String format) {
        DateFormat formater = getThreadSaveDateFormat(format);
        return formater.format(date);
    }

    public static String formatTime(Date date) {
        DateFormat formater = getThreadSaveDateFormat(DATE_FORMAT_STR);
        return formater.format(date);
    }

    public static String formatDate(Date date) {
        DateFormat formater = getThreadSaveDateFormat(DATE_FORMAT_STR4);
        return formater.format(date);
    }

    public static String formatBlankForNull(Date date, String format) {
        if (date == null) return "";
        return format(date, format);
    }

    /**
     * @param strDate
     * @param format
     * @return
     */
    public static Date strToDate(String strDate, DateFormat format) {
        Date date = null;
        try {
            date = format.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * <p>
     * 将日期按指定格式格式化
     * </p>
     *
     * @param calendar 將要格式化的日期
     * @param format   格式化的格式
     * @return 格式化后的字符串
     */
    public static String format(Calendar calendar, String format) {
        return format(calendar.getTime(), format);
    }

    /**
     * <p>
     * 将字符串按指定格式解析为日期
     * </p>
     *
     * @param source 將要解析的字符串
     * @param format 解析的格式
     * @return 解析后的日期
     */
    public static Date parse(String source, String format) {
        DateFormat formater = getThreadSaveDateFormat(format);
        Date date = null;
        try {
            date = formater.parse(source);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return date;
    }

    /**
     * <p>
     * 按指定的单位(java.util.Calendar中的常量YEAR、MONTH、DAY_OF_MONTH、
     * HOUR_OF_DAY、MINUTE、SECOND)截取日期，小于该单位的值会被清零
     * </p>
     *
     * @param date  要截取的日期
     * @param field 要截取的单位(java.util.Calendar中的常量)
     * @return 截取后的日期
     */
    public static Date truncate(Date date, int field) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar = truncate(calendar, field);
        return calendar.getTime();
    }

    /**
     * <p>
     * 按指定的单位(java.util.Calendar中的常量YEAR、MONTH、DAY_OF_MONTH、
     * HOUR_OF_DAY、MINUTE、SECOND)截取日期，小于该单位的值会被清零
     * </p>
     *
     * @param calendar 要截取的日期
     * @param field    要截取的单位(java.util.Calendar中的常量)
     * @return 截取后的日期
     */
    public static Calendar truncate(Calendar calendar, int field) {
        switch (field) {
            case Calendar.YEAR:
                calendar = clearMonth(calendar);
                break;
            case Calendar.MONTH:
                calendar = clearDay(calendar);
                break;
            case Calendar.DAY_OF_MONTH:
            case Calendar.DAY_OF_WEEK:
            case Calendar.DAY_OF_WEEK_IN_MONTH:
            case Calendar.DAY_OF_YEAR:
                calendar = clearHour(calendar);
                break;
            case Calendar.HOUR_OF_DAY:
            case Calendar.HOUR:
                calendar = clearMinute(calendar);
                break;
            case Calendar.MINUTE:
                calendar = clearSecond(calendar);
                break;
            case Calendar.SECOND:
                calendar = clearMillesecond(calendar);
                break;
        }
        calendar.get(Calendar.YEAR);
        return calendar;
    }

    /**
     * <p>
     * 将给定的日历字段(java.util.Calendar中的常量)设置为给定值
     * </p>
     *
     * @param date  要设定的日期对象
     * @param field 给定的日历字段(java.util.Calendar中的常量)
     * @param value 给定日历字段所要设置的值
     * @return 修改后的日期对象
     */
    public static Date set(Date date, int field, int value) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(field, value);
        return calendar.getTime();
    }

    /**
     * <p>
     * 根据日历的规则，为给定的日历字段添加或减去指定的时间量
     * </p>
     *
     * @param date   要设定的日期对象
     * @param field  给定的日历字段(java.util.Calendar中的常量)
     * @param amount 给定日历字段所要设置的值
     * @return 修改后的日期对象
     */
    public static Date add(Date date, int field, int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(field, amount);
        return calendar.getTime();
    }

    /**
     * <p>
     * 返回给定日历字段的值
     * </p>
     *
     * @param date  要设定的日期对象
     * @param field 给定的日历字段(java.util.Calendar中的常量)
     * @return 给定日历字段的值
     */
    public static int get(Date date, int field) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(field);
    }

    /**
     * <p>
     * 将java.util.Date日期转换成java.sql.Date
     * </p>
     *
     * @param date 要转换的日期
     * @return 转换后的日期
     */
    public static java.sql.Date toSqlDate(Date date) {
        return new java.sql.Date(date.getTime());
    }

    /**
     * <p>
     * 将java.util.Calendar日期转换成java.sql.Date
     * </p>
     *
     * @param calendar 要转换的日期
     * @return 转换后的日期
     */
    public static java.sql.Date toSqlDate(Calendar calendar) {
        return toSqlDate(calendar.getTime());
    }

    /**
     * <p>
     * 将java.util.Date日期转换成java.sql.Time
     * </p>
     *
     * @param date 要转换的日期
     * @return 转换后的日期
     */
    public static java.sql.Time toSqlTime(Date date) {
        return new java.sql.Time(date.getTime());
    }

    /**
     * <p>
     * 将java.util.Calendar日期转换成java.sql.Time
     * </p>
     *
     * @param calendar 要转换的日期
     * @return 转换后的日期
     */
    public static java.sql.Time toSqlTime(Calendar calendar) {
        return toSqlTime(calendar.getTime());
    }

    /**
     * <p>
     * 将java.util.Date日期转换成java.sql.Timestamp
     * </p>
     *
     * @param date 要转换的日期
     * @return 转换后的日期
     */
    public static java.sql.Timestamp toSqlTimestamp(Date date) {
        return new java.sql.Timestamp(date.getTime());
    }

    /**
     * <p>
     * 获取当天的0时
     * </p>
     *
     * @return 转换后的日期
     */
    public static Date getTodayHourAdd(int hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int y = calendar.get(Calendar.YEAR);
        int m = calendar.get(Calendar.MONTH);
        int d = calendar.get(Calendar.DATE);
        calendar.clear();
        calendar.set(Calendar.YEAR, y);
        calendar.set(Calendar.MONTH, m);
        calendar.set(Calendar.DATE, d);
        calendar.add(Calendar.HOUR, hour);
        return calendar.getTime();
    }

    /**
     * <p>
     * 将java.util.Calendar日期转换成java.sql.Timestamp
     * </p>
     *
     * @param calendar 要转换的日期
     * @return 转换后的日期
     */
    public static java.sql.Timestamp toSqlTimestamp(Calendar calendar) {
        return toSqlTimestamp(calendar.getTime());
    }

    private static Calendar clearMonth(Calendar calendar) {
        calendar.set(Calendar.MONTH, 0);
        clearDay(calendar);
        return calendar;
    }

    private static Calendar clearDay(Calendar calendar) {
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        clearHour(calendar);
        return calendar;
    }

    private static Calendar clearHour(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        clearMinute(calendar);
        return calendar;
    }

    private static Calendar clearMinute(Calendar calendar) {
        calendar.set(Calendar.MINUTE, 0);
        clearSecond(calendar);
        return calendar;
    }

    private static Calendar clearSecond(Calendar calendar) {
        calendar.set(Calendar.SECOND, 0);
        clearMillesecond(calendar);
        return calendar;
    }

    private static Calendar clearMillesecond(Calendar calendar) {
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    private static DateFormat getThreadSaveDateFormat(String format) {
        DateFormat result = local.get().get(format);
        if (result == null) {
            result = new SimpleDateFormat(format, Locale.ENGLISH);
            local.get().put(format, result);
        }
        return result;
    }

    /**
     * 获得日期指定格式（format）形式的字符串
     * add by shw on 2011-8-18
     *
     * @param date 输入的日期
     * @return format 指定的格式
     */
    public static String parseDateToInput(Date date, String format) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
        return sdf.format(date);
    }

    /**
     * 比较时间大小
     * add by shw on 2011-8-18
     *
     * @param firstDate
     * @param secondDate
     * @return 1:大于，0:等于，-1:小于
     */
    public static int compareDate(Date firstDate, Date secondDate) {
        //Calendar fDate = Calendar.getInstance();
        //Calendar sDate = Calendar.getInstance();
        //fDate.setTime(firstDate);
        //sDate.setTime(secondDate);
        //long resultTime = fDate.getTimeInMillis() - sDate.getTimeInMillis();
        long resultTime = firstDate.getTime() - secondDate.getTime();
        if (resultTime > 0) {
            return 1;
        } else if (resultTime == 0) {
            return 0;
        } else {
            return -1;
        }
    }


    public static String getWeekDayEng(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int index = c.get(Calendar.DAY_OF_WEEK);
        String day = "";
        switch (index) {
            case 0:
                day = "SAT";
                break;
            case 1:
                day = "SUN";
                break;
            case 2:
                day = "MON";
                break;
            case 3:
                day = "THE";
                break;
            case 4:
                day = "WED";
                break;
            case 5:
                day = "THU";
                break;
            case 6:
                day = "FRI";
                break;
        }
        return day;
    }

    public static boolean isWeekend(Date date) {
        String day = getWeekDayEng(date);
        return "SAT".equals(day) || "SUN".equals(day);
    }

    public static Date valueOf(int year, int month, int date) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, date, 0, 0, 0);
        return calendar.getTime();
    }

    /**
     * 获取昨天的日期
     *
     * @return
     */
    public static Date getYesterday() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date date = cal.getTime();
        return date;
    }

    /**
     * 得到某年某周的第一天
     *
     * @param year
     * @param week
     * @return
     */
    public static Date getFirstDayOfWeek(int year, int week) {
        week = week - 1;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DATE, 1);

        Calendar cal = (Calendar) calendar.clone();
        cal.add(Calendar.DATE, week * 7);

        return getFirstDayOfWeek(cal.getTime());
    }

    /**
     * 得到某年某周的最后一天
     *
     * @param year
     * @param week
     * @return
     */
    public static Date getLastDayOfWeek(int year, int week) {
        week = week - 1;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DATE, 1);
        Calendar cal = (Calendar) calendar.clone();
        cal.add(Calendar.DATE, week * 7);

        return getLastDayOfWeek(cal.getTime());
    }

    /**
     * 取得当前日期所在周的第一天
     *
     * @param date
     * @return
     */
    public static Date getFirstDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK,
                calendar.getFirstDayOfWeek()); // Sunday
        return calendar.getTime();
    }

    /**
     * 取得当前日期所在周的最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK,
                calendar.getFirstDayOfWeek() + 6); // Saturday
        return calendar.getTime();
    }

    /**
     * 取得当前日期所在周的前一周最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfLastWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return getLastDayOfWeek(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.WEEK_OF_YEAR) - 1);
    }

    /**
     * 返回指定日期的月的第一天
     *
     * @param date
     * @return
     */
    public static Date getFirstDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), 1);
        return calendar.getTime();
    }

    /**
     * 返回指定年月的月的第一天
     *
     * @param year
     * @param month
     * @return
     */
    public static Date getFirstDayOfMonth(Integer year, Integer month) {
        Calendar calendar = Calendar.getInstance();
        if (year == null) {
            year = calendar.get(Calendar.YEAR);
        }
        if (month == null) {
            month = calendar.get(Calendar.MONTH);
        }
        calendar.set(year, month, 1);
        return calendar.getTime();
    }

    /**
     * 返回指定日期的月的最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), 1);
        calendar.roll(Calendar.DATE, -1);
        return calendar.getTime();
    }

    /**
     * 返回指定年月的月的最后一天
     *
     * @param year
     * @param month
     * @return
     */
    public static Date getLastDayOfMonth(Integer year, Integer month) {
        Calendar calendar = Calendar.getInstance();
        if (year == null) {
            year = calendar.get(Calendar.YEAR);
        }
        if (month == null) {
            month = calendar.get(Calendar.MONTH);
        }
        calendar.set(year, month, 1);
        calendar.roll(Calendar.DATE, -1);
        return calendar.getTime();
    }

    /**
     * 返回指定日期的上个月的最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfLastMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) - 1, 1);
        calendar.roll(Calendar.DATE, -1);
        return calendar.getTime();
    }

    /**
     * 返回指定日期的季的第一天
     *
     * @param date
     * @return
     */
    public static Date getFirstDayOfQuarter(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return getFirstDayOfQuarter(calendar.get(Calendar.YEAR),
                getQuarterOfYear(date));
    }

    /**
     * 返回指定年季的季的第一天
     *
     * @param year
     * @param quarter
     * @return
     */
    public static Date getFirstDayOfQuarter(Integer year, Integer quarter) {
        Calendar calendar = Calendar.getInstance();
        Integer month = new Integer(0);
        if (quarter == 1) {
            month = 1 - 1;
        } else if (quarter == 2) {
            month = 4 - 1;
        } else if (quarter == 3) {
            month = 7 - 1;
        } else if (quarter == 4) {
            month = 10 - 1;
        } else {
            month = calendar.get(Calendar.MONTH);
        }
        return getFirstDayOfMonth(year, month);
    }

    /**
     * 返回指定日期的季的最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfQuarter(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return getLastDayOfQuarter(calendar.get(Calendar.YEAR),
                getQuarterOfYear(date));
    }

    /**
     * 返回指定年季的季的最后一天
     *
     * @param year
     * @param quarter
     * @return
     */
    public static Date getLastDayOfQuarter(Integer year, Integer quarter) {
        Calendar calendar = Calendar.getInstance();
        Integer month = new Integer(0);
        if (quarter == 1) {
            month = 3 - 1;
        } else if (quarter == 2) {
            month = 6 - 1;
        } else if (quarter == 3) {
            month = 9 - 1;
        } else if (quarter == 4) {
            month = 12 - 1;
        } else {
            month = calendar.get(Calendar.MONTH);
        }
        return getLastDayOfMonth(year, month);
    }

    /**
     * 返回指定日期的上一季的最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfLastQuarter(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return getLastDayOfLastQuarter(calendar.get(Calendar.YEAR),
                getQuarterOfYear(date));
    }

    /**
     * 返回指定年季的上一季的最后一天
     *
     * @param year
     * @param quarter
     * @return
     */
    public static Date getLastDayOfLastQuarter(Integer year, Integer quarter) {
        Calendar calendar = Calendar.getInstance();
        Integer month = new Integer(0);
        if (quarter == 1) {
            month = 12 - 1;
        } else if (quarter == 2) {
            month = 3 - 1;
        } else if (quarter == 3) {
            month = 6 - 1;
        } else if (quarter == 4) {
            month = 9 - 1;
        } else {
            month = calendar.get(Calendar.MONTH);
        }
        return getLastDayOfMonth(year, month);
    }

    /**
     * 返回指定日期的季度
     *
     * @param date
     * @return
     */
    public static int getQuarterOfYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) / 3 + 1;
    }

    /**
     * 获取指定时间上一个半年的第一天
     *
     * @return
     */
    public static Date getFirstDayOfHalfAYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH) + 1;
        String startDate = "";
        String years = format(date, DATE_FORMAT_STR11);
        int years_value = Integer.parseInt(years);
        if (month <= 6) {
            years_value--;
            startDate = years_value + "-7-1";
        } else {
            startDate = years_value + "-1-1";
        }
        return parse(startDate, DATE_FORMAT_STR4);
    }

    /**
     * 获取指定时间上一个半年的最后一天
     *
     * @return
     */
    public static Date getLastDayOfHalfAYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH) + 1;
        String endDate = "";
        String years = format(date, DATE_FORMAT_STR11);
        int years_value = Integer.parseInt(years);
        if (month <= 6) {
            years_value--;
            endDate = years_value + "-12-31";
        } else {
            endDate = years_value + "-6-30";
        }
        return parse(endDate, DATE_FORMAT_STR4);
    }

    /**
     * 获取指定时间去年的第一天
     *
     * @return
     */
    public static Date getFirstDayOfYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String years = format(date, DATE_FORMAT_STR11);
        int years_value = Integer.parseInt(years);
        years_value--;
        return parse(years_value + "-1-1", DATE_FORMAT_STR4);
    }

    /**
     * 获取指定时间去年的最后一天
     *
     * @return
     */
    public static Date getLastDayOfYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String years = format(date, DATE_FORMAT_STR11);
        int years_value = Integer.parseInt(years);
        years_value--;
        return parse(years_value + "-12-31", DATE_FORMAT_STR4);
    }

}
