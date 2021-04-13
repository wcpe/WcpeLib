package top.wcpe.wcpelib.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Date;

/**
 * 功能描述：关于时间的工具类
 *
 * @Author: WCPE
 * @Date: 2021/2/17 3:45
 */
public class TimeUtil {

    /**
     * 获取距离明天还有多久时间
     * @return
     */
    public static long getNextDayStamp() {
        return getStamp(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(0, 0))) - System.currentTimeMillis();
    }

    /**
     * LocalDateTime转换成时间戳
     *
     * @param time
     * @return
     */
    public static long getStamp(LocalDateTime time) {
        return time.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    /**
     * 将时间转换为时间戳
     *
     * @param time 时间戳
     */
    public static Long timeToStamp(String time) {
        return timeToStamp(time, null);
    }

    /**
     * 将时间转换为时间戳
     *
     * @param time   时间戳
     * @param format 格式
     */
    public static Long timeToStamp(String time, String format) {
        format = format == null ? "yyyy-MM-dd HH:mm:ss" : format;
        try {
            return new SimpleDateFormat(format).parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将时间戳按格式转换
     *
     * @param date 时间戳
     */
    public static String stampToTime(long date) {
        return stampToTime(date, null);
    }

    /**
     * 将时间戳按格式转换
     *
     * @param date   时间戳
     * @param format 格式
     */
    public static String stampToTime(long date, String format) {
        format = format == null ? "yyyy-MM-dd HH:mm:ss" : format;
        return String.valueOf(new SimpleDateFormat(format).format(new Date(date)));
    }

    /**
     * 将时间戳转换为多少x秒x分x时
     *
     * @param date 时间戳
     */
    public static String formatDate(long date) {
        return formatDate(date, null, null, null, null);
    }

    /**
     * 将时间戳转换为多少x秒x分x时
     *
     * @param date 时间戳
     * @param s    秒
     * @param m    分
     * @param h    时
     * @param d    天
     */
    public static String formatDate(long date, String s, String m, String h, String d) {
        s = s == null ? "秒" : s;
        m = m == null ? "分" : m;
        h = h == null ? "时" : h;
        d = s == null ? "天" : d;
        long sec = date / 1000L;
        if (sec < 60L)
            return sec + s;
        if (sec < 3600L)
            return (sec / 60L) + m + (sec % 60L) + s;
        if (sec < 86400L) {
            return (sec / 3600L) + h + (sec % 3600L / 60L) + m + (sec % 3600L % 60L) + s;
        }
        return (sec / 86400L) + d + (sec % 86400L / 3600L) + h + (sec % 86400L % 3600L / 60L) + m
                + (sec % 86400L % 60L) + s;
    }

    public enum TimeType {
        S, M, H, D
    }

    /**
     * 将秒转化为 分 时 天
     *
     * @param sec      时间戳
     * @param timeType 转化的类型
     */
    public static Long sToOther(long sec, TimeType timeType) {
        if (timeType == null) return null;
        if (timeType == TimeType.S)
            return sec;
        if (timeType == TimeType.M)
            return sec / 60L;
        if (timeType == TimeType.H)
            return sec / 3600L;
        if (timeType == TimeType.D)
            return sec / 86400L;

        return null;
    }
}
