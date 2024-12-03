package top.wcpe.wcpelib.common.utils

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * 由 WCPE 在 2024/11/30 12:00 创建
 * <p>
 * Created by WCPE on 2024/11/30 12:00
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 */
object DateTimeUtil {

    /**
     * 格式化 LocalDateTime 对象为字符串
     * @param localDateTime LocalDateTime 对象
     * @param format 格式化字符串
     * @return 格式化后的字符串
     */
    @JvmStatic
    @JvmOverloads
    fun formatLocalDateTime(localDateTime: LocalDateTime, format: String = "yyyy-MM-dd HH:mm:ss"): String {
        val formatter = DateTimeFormatter.ofPattern(format)
        return localDateTime.format(formatter)
    }


    /**
     * 将时间戳转换为 LocalDateTime 对象
     * @param timestampMillis 时间戳（毫秒）
     * @return [LocalDateTime] 对象
     */
    @JvmStatic
    fun timestampMillisToLocalDateTime(timestampMillis: Long?): LocalDateTime? {
        if (timestampMillis == null) {
            return null
        }
        return timestampMillisToLocalDateTime(timestampMillis)
    }

    /**
     * 将时间戳转换为 LocalDateTime 对象
     * @param timestampMillis 时间戳（毫秒）
     * @return [LocalDateTime] 对象
     */
    @JvmStatic
    fun timestampMillisToLocalDateTime(timestampMillis: Long): LocalDateTime {
        // 将时间戳转换为Instant对象，Instant表示时间线上的一个瞬时点
        val instant = Instant.ofEpochMilli(timestampMillis)
        // 获取系统默认时区的ZoneId
        val zoneId = ZoneId.systemDefault()
        // 将Instant对象转换为指定时区的LocalDateTime对象
        return LocalDateTime.ofInstant(instant, zoneId)
    }


    /**
     * 将 Date 对象转换为 LocalDateTime 对象
     * @param date Date 对象
     * @return [LocalDateTime] 对象
     */
    @JvmStatic
    fun dateToLocalDateTime(date: Date): LocalDateTime {
        // 将Date对象转换为Instant对象，Instant表示时间线上的一个瞬时点
        val instant = date.toInstant()
        // 获取系统默认时区的ZoneId
        val zoneId = ZoneId.systemDefault()
        // 将Instant对象转换为指定时区的LocalDateTime对象
        return LocalDateTime.ofInstant(instant, zoneId)
    }

    /**
     * 将 LocalDateTime 对象转换为时间戳（毫秒）
     * @param localDateTime LocalDateTime 对象
     * @return 时间戳（毫秒）
     */
    @JvmStatic
    fun localDateTimeToTimestampMillis(localDateTime: LocalDateTime): Long {
        // 获取系统默认时区的ZoneId
        val zoneId = ZoneId.systemDefault()
        // 将LocalDateTime对象转换为指定时区的Instant对象
        val instant = localDateTime.atZone(zoneId).toInstant()
        // 将Instant对象转换为时间戳（毫秒）
        return instant.toEpochMilli()
    }
}