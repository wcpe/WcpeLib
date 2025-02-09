package top.wcpe.wcpelib.common.tools

/**
 * 由 WCPE 在 2025/2/9 19:30 创建
 * <p>
 * Created by WCPE on 2025/2/9 19:30
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 */
@Suppress("unused")
data class SingleCoolDownTool(
    private var cooldownEndTime: Long = -1L,
) {

    /**
     * 检查是否冷却中
     * @return 是否冷却中
     */
    fun checkCoolDown(): Boolean {
        return System.currentTimeMillis() < cooldownEndTime
    }

    /**
     * 设置冷却时间
     * @param milliSecond 冷却时间 毫秒
     */
    fun setCoolDown(milliSecond: Long) {
        cooldownEndTime = System.currentTimeMillis() + milliSecond
    }

    /**
     * 获取剩余冷却时间
     * @return 剩余冷却时间 毫秒
     */
    fun getRemainingCoolDown(): Long {
        return cooldownEndTime - System.currentTimeMillis()
    }

    /**
     * 清除冷却时间
     */
    fun clearCoolDown() {
        cooldownEndTime = -1L
    }

    /**
     * 冷却
     * @param secondsMillis 冷却时间 毫秒
     * @param onSuccess 冷却成功回调
     */
    inline fun coolDown(
        secondsMillis: Long,
        onSuccess: () -> Unit,
    ) {
        val remainingCoolDown = getRemainingCoolDown()
        if (remainingCoolDown < 0) {
            setCoolDown(secondsMillis)
            onSuccess()
        }

    }

    /**
     * 冷却
     * @param secondsMillis 冷却时间 毫秒
     * @param onSuccess 冷却成功回调
     * @param onCooling 冷却中回调
     */
    inline fun coolDown(
        secondsMillis: Long,
        onSuccess: () -> Unit,
        onCooling: (remainingCoolDown: Long) -> Unit,
    ) {
        val remainingCoolDown = getRemainingCoolDown()
        if (remainingCoolDown < 0) {
            setCoolDown(secondsMillis)
            onSuccess()
        } else {
            onCooling(remainingCoolDown)
        }
    }
}