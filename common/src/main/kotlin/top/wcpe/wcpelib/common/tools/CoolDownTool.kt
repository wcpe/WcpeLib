package top.wcpe.wcpelib.common.tools

import java.util.concurrent.ConcurrentHashMap

/**
 * 由 WCPE 在 2025/2/9 19:29 创建
 * <p>
 * Created by WCPE on 2025/2/9 19:29
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 */
@Suppress("unused")
class CoolDownTool {
    private val coolDownMap = ConcurrentHashMap<String, Long>()

    /**
     * 检查是否冷却中
     * @param key 键
     * @return 是否冷却中
     */
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
     * @param milliSecond 冷却时间 毫秒
     */
    fun setCoolDown(key: String, milliSecond: Long) {
        coolDownMap[key] = System.currentTimeMillis() + milliSecond
    }

    /**
     * 获取剩余冷却时间
     * @param key 键
     * @return 剩余冷却时间 毫秒
     */
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
    fun clearCoolDown(key: String) {
        coolDownMap.remove(key)
    }

    /**
     * 清除所有冷却时间
     */
    fun clearAllCoolDown() {
        coolDownMap.clear()
    }

    /**
     * 获取所有冷却时间
     * @return 所有冷却时间
     */
    fun getAllCoolDown(): Map<String, Long> {
        return coolDownMap

    }

    inline fun coolDown(
        key: String,
        secondsMillis: Long,
        onSuccess: () -> Unit,
    ) {
        val remainingCoolDown = getRemainingCoolDown(key)
        if (remainingCoolDown < 0) {
            setCoolDown(key, secondsMillis)
            onSuccess()
        }

    }

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