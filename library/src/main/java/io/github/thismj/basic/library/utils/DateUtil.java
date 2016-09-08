package io.github.thismj.basic.library.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * ╭══╮　┌═════┐
 * ╭╯上车║═║老司机专用║
 * └══⊙═⊙═~----╰⊙═⊙╯
 * ----------------
 * 日期工具类
 *
 * @author tangmingjian
 * @version v1.0
 * @date 2016-09-06 11:15
 */

public class DateUtil {

    public static final String DATE_FORMAT_DEFAULT = "yyyy-MM-dd";

    /**
     * 解析日期字符串为date对象
     *
     * @param dateStr 格式化日期字符串
     * @return date对象
     */
    public static Date parse(String dateStr) {
        return parse(dateStr, DATE_FORMAT_DEFAULT);
    }

    /**
     * 解析日期字符串为date对象
     *
     * @param dateStr 格式化日期字符串
     * @param format  转换格式
     * @return date对象
     */
    public static Date parse(String dateStr, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.US);
        try {
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 格式化日期对象
     *
     * @param date 日期对象
     * @return 格式化日期字符串
     */
    public static String format(Date date) {
        return format(date, DATE_FORMAT_DEFAULT);
    }

    /**
     * 格式化日期对象
     *
     * @param date   日期对象
     * @param format 转换格式
     * @return 格式化日期字符串
     */
    public static String format(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return dateFormat.format(date);
    }

    /**
     * 日期字符串格式转换
     *
     * @param dateStr 格式化日期字符串
     * @param format1 转换前格式
     * @param format2 转换后格式
     * @return 转换后日期字符串
     */
    public static String changeFormat(String dateStr, String format1, String format2) {
        return format(parse(dateStr, format1), format2);
    }

    /**
     * 格式化日期字符串转时间戳
     *
     * @param dateString 格式化日期字符串
     * @param format     字符串格式
     * @return 时间戳
     */
    public static long parseToTimeMill(String dateString, String format) {
        Date date = parse(dateString, format);
        if (date == null) {
            return 0;
        }
        return date.getTime();
    }
}
