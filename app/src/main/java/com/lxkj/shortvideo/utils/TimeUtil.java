package com.lxkj.shortvideo.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class TimeUtil {

    public final static long MS_OF_DAY = 24 * 3600 * 1000;
    public static final int MINUTE = 60 * 1000;
    public final static String DATE_FORMAT = "yyyy-MM-dd";
    public final static String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public final static String DATE_TIME_FORMAT2 = "yyyy/MM/dd HH:mm:ss";
    public final static String DATE_DAY_FORMAT = "yyyy-MM-dd HH:mm";
    public final static String DATE_FORMAT2 = "yyyy/MM/dd";

    static final private SimpleDateFormat sFormat = new SimpleDateFormat("yyMMddHHmmssSSS");
    static final private SimpleDateFormat sLogFormat = new SimpleDateFormat("MM-dd HH:mm:ss.SSS");

    static public String getNowTimeStr() {
        return sFormat.format(new Date());
    }

    static public String getLogStr() {
        return sLogFormat.format(new Date());
    }

    private final static String[] weekCN = {
            "周日", "周一", "周二", "周三", "周四", "周五", "周六",
    };

    public static String chineseWeekDayToday() {
        return chineseWeek(dayOfWeekToday());
    }

    public static String chineseWeekDay(String dateStr) {
        return chineseWeek(dayOfWeek(dateStr));
    }

    private static String chineseWeek(int dayOfWeek) {
        if (dayOfWeek < 0) {
            dayOfWeek = 0;
        } else if (dayOfWeek >= weekCN.length) {
            dayOfWeek = weekCN.length - 1;
        }
        return weekCN[dayOfWeek];
    }

    /**
     * @return [0, 6]:[周日,周六]
     */
    private static int dayOfWeekToday() {
        return dayOfWeek(new Date());
    }

    public static Date String2Data(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String d = format.format(time);
        Date date = null;
        try {
            date = format.parse(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date String2Data(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private static int dayOfWeek(String dateStr) {
        return dayOfWeek(dateFormString(dateStr));
    }

    private static int dayOfWeek(final Date date) {
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        c.setTime(date);
        return c.get(Calendar.DAY_OF_WEEK) - 1;
    }


    /*
     * 将时间转换为时间戳
     */
    public static String dateToStamp(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    /*
     * 将时间转换为时间戳
     */
    public static String dateToStamp(String s, String format) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s, String dateFormat) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /**
     * 毫秒转化成时间
     *
     * @param
     * @return 该毫秒数转换为 * days * hours * minutes * seconds 后的格式
     * @author fy.zhang
     */
    public static String formatDuring(long mss) {
        long days = mss / (1000 * 60 * 60 * 24);
        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (mss % (1000 * 60)) / 1000;
        if (days > 0)
            return 24 + "小时" + 00 + "分"
                    + 00 + "秒";
        else
            return hours + "小时" + minutes + "分"
                    + seconds + "秒";
    }

    /**
     * 毫秒转化成时间
     *
     * @param
     * @return 该毫秒数转换为  * hours * minutes * seconds 后的格式
     * @author fy.zhang
     */
    public static List<Integer> formatDuring1(long mss) {
        List<Integer> times = new ArrayList<>();
        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (mss % (1000 * 60)) / 1000;
        times.add((int) hours);
        times.add((int) minutes);
        times.add((int) seconds);
        return times;
    }

    /**
     * 毫秒转化成剩余时间
     *
     * @param
     * @return 该毫秒数转换为 * days * hours * minutes * seconds 后的格式
     * @author fy.zhang
     */
    public static String formatSurplus(long mss) {
        long days = mss / (1000 * 60 * 60 * 24);
        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        if (days > 0)
            return 24 + "小时" + 00 + "分";
        else
            return hours + "小时" + minutes + "分";
    }

    public static int countPassDay(final String date) {
        return countPassDay(dateFormString(date), Integer.MAX_VALUE);
    }

    public static int countFutureDay(final Date date, final int max) {
        if (date.getTime() - new Date().getTime() > max * MS_OF_DAY || date.getTime() - new Date().getTime() < 0) {
            return max;
        }
        final Calendar c1 = Calendar.getInstance();
        c1.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        c1.setTime(new Date());
        int year1 = c1.get(Calendar.YEAR);
        int day1 = c1.get(Calendar.DAY_OF_YEAR);

        final Calendar c2 = Calendar.getInstance();
        c2.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        c2.setTime(date);
        int year2 = c2.get(Calendar.YEAR);
        int day2 = c2.get(Calendar.DAY_OF_YEAR);

        int pass = 0;
        if (year1 == year2) {
            pass = day2 - day1;
        } else {
            pass = 365 - day2 + day1;
        }
        return pass >= 0 ? pass : 0;
    }

    public static int countPassDay(final Date date, final int max) {
        if (new Date().getTime() - date.getTime() > max * MS_OF_DAY) {
            return max;
        }
        final Calendar c1 = Calendar.getInstance();
        c1.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        c1.setTime(new Date());
        int year1 = c1.get(Calendar.YEAR);
        int day1 = c1.get(Calendar.DAY_OF_YEAR);

        final Calendar c2 = Calendar.getInstance();
        c2.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        c2.setTime(date);
        int year2 = c2.get(Calendar.YEAR);
        int day2 = c2.get(Calendar.DAY_OF_YEAR);

        int pass = 0;
        if (year1 == year2) {
            pass = day1 - day2;
        } else {
            pass = 365 - day2 + day1;
        }
        return pass >= 0 ? pass : 0;
    }

    public static int dayOfMonth(final String date) {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        c.setTime(dateFormString(date));
        return c.get(Calendar.DAY_OF_MONTH);
    }

    public static int monthOfYear(final String date) {
        return monthOfYear(dateFormString(date));
    }

    public static int monthOfYear(final Date date) {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        c.setTime(date);
        return 1 + c.get(Calendar.MONTH);
    }

    public static String friendlyFurtureTime(final String date) {
        return friendlyFurtureTime(dateFormString(date));
    }

    public static String friendlyPassTime(final Date date) {
        switch (TimeUtil.countPassDay(date, 3)) {
            case 0:
                return "今天 " + formatDate(date, "HH:mm");
            case 1:
                return "昨天 " + formatDate(date, "HH:mm");
            case 2:
                return "前天 " + formatDate(date, "HH:mm");
            default:
                return formatDate(date, "yyyy年MM月dd日 HH:mm");
        }
    }

    public static String friendlyFurtureTime(final Date date) {
        switch (TimeUtil.countFutureDay(date, 3)) {
            case 0:
                return formatDate(date, "HH:mm");
            case 1:
                return "明天 " + formatDate(date, "HH:mm");
            case 2:
                return "后天 " + formatDate(date, "HH:mm");
            default:
                return formatDate(date, "yyyy年MM月dd日 HH:mm");
        }
    }

    @SuppressWarnings("deprecation")
    private static String recentTime(long passed) {
        if (passed < 60 * 1000) {
            return "刚刚";
        }
        passed /= 60 * 1000;
        if (passed < 60) {
            return passed + "分钟前";
        }
        passed /= 60;
        if (passed < 24) {
            return passed + "小时前";
        }
        passed /= 24;
        if (passed < 30) {
            return passed + "天前";
        }
        passed /= 30;
        if (passed < 12) {
            return passed + "个月前";
        }
        return passed / 12 + "年前";
    }


    public static String recentTime(final String date) {

        return recentTime(System.currentTimeMillis() - msOfTime(date));

    }

    public static String recentTime1(final long date) {
        if (System.currentTimeMillis() - date < 3600000)
            return recentTime(System.currentTimeMillis() - date);
        else if (System.currentTimeMillis() - date < 86400000)
            return stampToDate(date + "", "HH:mm");
        else
            return stampToDate(date + "", "yyyy-MM-dd");

    }

    public static boolean isToday(String date) {
        return null != date && date.startsWith(getDateString(DATE_FORMAT));
    }

    public static int countWeekFrom(String startDate) {
        final Calendar c1 = Calendar.getInstance();
        c1.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        c1.setTime(new Date());
        int year1 = c1.get(Calendar.YEAR);
        int week1 = c1.get(Calendar.WEEK_OF_YEAR);

        final Calendar c2 = Calendar.getInstance();
        c2.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        c2.setTime(dateFormString(startDate));
        int year2 = c2.get(Calendar.YEAR);
        int week2 = c2.get(Calendar.WEEK_OF_YEAR);

        int pass = 0;
        if (year1 == year2) {
            pass = week1 - week2;
        } else {
            pass = 52 - week2 + week1;
        }
        return pass >= 0 ? pass : 0;
    }

    public static String getCurrentTime() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
        String date = sDateFormat.format(new Date());
        return date;
    }

    public static String getDateString() {
        return getDateString(DATE_TIME_FORMAT);
    }

    public static String getCurDayDateString() {
        return getDateString(DATE_FORMAT);
    }

    public static String getDateString(final String format) {
        return formatDate(new Date(), format);
    }

    public static String formatTime(final long t) {
        return formatTime(t, DATE_TIME_FORMAT);
    }

    public static String formatTime(final long t, final String format) {
        if (t < 0) {
            return null;
        } else {
            return new SimpleDateFormat(format)
                    .format(new Date(t));
        }
    }

    public static String formatDate(final Date date) {
        return formatDate(date, DATE_TIME_FORMAT);
    }

    public static String formatDate(final Date date, final String format) {
        if (null == date) {
            return "";
        } else {
            return new SimpleDateFormat(format).format(date);
        }
    }

    public static Date dateFormString(final String timeStr, final String format) {
        if (null != timeStr) {
            try {
                return new SimpleDateFormat(format).parse(timeStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new Date();
    }

    public static Date dateFormString(final String timeStr) {
        if (null != timeStr) {
            String format = DATE_FORMAT;
            if (timeStr.contains(" ") && timeStr.contains(":")) {
                format = DATE_TIME_FORMAT;
            }
            return dateFormString(timeStr, format);
        }
        return new Date();
    }

    public static long msOfTime(final String time) {
        try {
            String format = DATE_FORMAT;
            if (time.contains(" ") && time.contains(":")) {
                format = DATE_TIME_FORMAT;
            }
            return new SimpleDateFormat(format).parse(time).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int parseDaysFrom(final String time) {
        return (int) ((System.currentTimeMillis() - msOfTime(time)) / (24 * 3600 * 1000));
    }

    public static String convertDate(final String dateStr, final String fromFormat, final String toFormat) {
        if (TextUtils.isEmpty(dateStr) || TextUtils.isEmpty(fromFormat) || TextUtils.isEmpty(toFormat)) {
            return "";
        }
        Date date = null;
        try {
            date = new SimpleDateFormat(fromFormat).parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formatDate(date, toFormat);
    }

    public static String dayAfter(int addDay) {
        try {
            Calendar cd = Calendar.getInstance();
            cd.add(Calendar.DAY_OF_MONTH, addDay);//增加一天
            return formatDate(cd.getTime(), "MM/dd");
        } catch (Exception e) {
            return null;
        }
    }

    public static String getMd(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return formatDate(sdf.parse(dateStr), "MM月dd日");
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    final static int S_IN_MINUTE = 60;
    final static int M_IN_HOUR = 60;
    final static int H_IN_DAY = 24;
    final static int D_IN_MONTH = 30;
    final static int Mth_IN_YEAR = 12;
    final static int S_IN_HOUR = M_IN_HOUR * S_IN_MINUTE;
    final static int S_IN_DAY = H_IN_DAY * S_IN_HOUR;
    final static int S_IN_MONTH = D_IN_MONTH * S_IN_DAY;
    final static int S_IN_YEAR = Mth_IN_YEAR * S_IN_MONTH;

    public static String friendlyCostTime(final int second) {
        if (second < S_IN_MINUTE) {
            return String.format(Locale.getDefault(), "%d秒", second);
        }
        if (second < 2 * S_IN_MINUTE) {
            return String.format(Locale.getDefault(), "%d分钟%d秒",
                    second / S_IN_MINUTE, second % S_IN_MINUTE);
        }
        if (second < S_IN_HOUR) {
            return String.format(Locale.getDefault(), "%d分钟",
                    second / S_IN_MINUTE);
        }
        if (second < S_IN_DAY) {
            return String.format(Locale.getDefault(), "%d小时%d分钟",
                    second / S_IN_HOUR, (second / S_IN_MINUTE) % M_IN_HOUR);
        }
        if (second < S_IN_MONTH) {
            return String.format(Locale.getDefault(), "%d天%d小时",
                    second / S_IN_DAY, (second / S_IN_HOUR) % H_IN_DAY);
        }
        if (second < S_IN_YEAR) {
            return String.format(Locale.getDefault(), "%d个月%d天",
                    second / S_IN_DAY, (second / S_IN_HOUR) % H_IN_DAY);
        }
        return String.format(Locale.getDefault(), "%.1f年", ((float) second) / S_IN_YEAR);
    }

}
