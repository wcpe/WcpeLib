package top.wcpe.wcpelib.common.utils

import java.util.concurrent.ConcurrentHashMap

/**
 * 由 WCPE 在 2024/11/7 14:03 创建
 * <p>
 * Created by WCPE on 2024/11/7 14:03
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 */
@Suppress("unused")
object CoolDownUtil {
    @JvmStatic
    val coolDownMap = ConcurrentHashMap<String, Long>()

    /**
     * 检查是否冷却中
     * @param key 键
     * @return 是否冷却中
     */
    @JvmStatic
    fun checkCoolDown(key: String): Boolean {
        val lng = coolDownMap[key]
        if (lng == null) {
            return false
        }
        val currentTimeMillis = System.currentTimeMillis()
        if (lng > currentTimeMillis) {
            return true
        } else {
            coolDownMap.remove(key)
            return false
        }
    }

    /**
     * 设置冷却时间
     * @param key 键
     * @param secondsMillis 冷却时间 毫秒
     */
    @JvmStatic
    fun setCoolDown(key: String, secondsMillis: Long) {
        coolDownMap[key] = System.currentTimeMillis() + secondsMillis
    }

    /**
     * 获取剩余冷却时间
     * @param key 键
     * @return 剩余冷却时间 毫秒
     */
    @JvmStatic
    fun getRemainingCoolDown(key: String): Long {
        val lng = coolDownMap[key]
        if (lng == null) {
            return -1L
        }
        val currentTimeMillis = System.currentTimeMillis()
        return lng - currentTimeMillis

    }

    /**
     * 清除冷却时间
     * @param key 键
     */
    @JvmStatic
    fun clearCoolDown(key: String) {
        coolDownMap.remove(key)
    }

    /**
     * 清除所有冷却时间
     */
    @JvmStatic
    fun clearAllCoolDown() {
        coolDownMap.clear()
    }

    /**
     * 获取所有冷却时间
     * @return 所有冷却时间
     */
    @JvmStatic
    fun getAllCoolDown(): Map<String, Long> {
        return coolDownMap

    }


    @JvmStatic
    inline fun coolDown(
        key: String,
        secondsMillis: Long,
        onSuccess: () -> Unit,
        onCooling: (remainingCoolDown: Long) -> Unit,
    ) {
        val remainingCoolDown = getRemainingCoolDown(key)
        if (remainingCoolDown < 0) {
            setCoolDown(key, secondsMillis)
            onSuccess()
        } else {
            onCooling(remainingCoolDown)
        }
    }
}