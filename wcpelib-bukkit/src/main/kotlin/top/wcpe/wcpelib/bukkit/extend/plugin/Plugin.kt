package top.wcpe.wcpelib.bukkit.extend.plugin

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask

/**
 * 由 WCPE 在 2022/5/21 22:30 创建
 *
 * Created by WCPE on 2022/5/21 22:30
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.0.12-alpha-dev-1
 */
fun JavaPlugin.runTask(isAsynchronously: Boolean = false, runnable: Runnable): BukkitTask {
    return if (isAsynchronously) {
        Bukkit.getScheduler().runTaskAsynchronously(this, runnable)
    } else {
        Bukkit.getScheduler().runTask(this, runnable)
    }
}

fun JavaPlugin.runTaskLater(tick: Long, isAsynchronously: Boolean = false, runnable: Runnable): BukkitTask {
    return if (isAsynchronously) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(this, runnable, tick)
    } else {
        Bukkit.getScheduler().runTaskLater(this, runnable, tick)
    }
}

fun JavaPlugin.runTaskTimer(
    startTick: Long,
    repeatTick: Long = startTick,
    isAsynchronously: Boolean = false,
    runnable: Runnable
): BukkitTask {
    return if (isAsynchronously) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, runnable, startTick, repeatTick)
    } else {
        Bukkit.getScheduler().runTaskTimer(this, runnable, startTick, repeatTick)
    }
}