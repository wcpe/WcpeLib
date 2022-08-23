package top.wcpe.wcpelib.nukkit.extend.plugin

import cn.nukkit.Server
import cn.nukkit.plugin.PluginBase
import cn.nukkit.scheduler.TaskHandler

/**
 * 由 WCPE 在 2022/5/21 22:52 创建
 *
 * Created by WCPE on 2022/5/21 22:52
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.0.12-alpha-dev-1
 */
fun PluginBase.runTask(isAsynchronously: Boolean = false, runnable: Runnable): TaskHandler {
    return Server.getInstance().scheduler.scheduleTask(this, runnable, isAsynchronously)
}

inline fun <I : PluginBase> I.runTask(
    isAsynchronously: Boolean = false,
    crossinline runnable: I.() -> Unit
): TaskHandler {
    return Server.getInstance().scheduler.scheduleTask(this, { runnable(this) }, isAsynchronously)
}

fun PluginBase.runTaskLater(tick: Int, isAsynchronously: Boolean = false, runnable: Runnable): TaskHandler {
    return Server.getInstance().scheduler.scheduleDelayedTask(this, runnable, tick, isAsynchronously)
}

inline fun <I : PluginBase> I.runTaskLater(
    tick: Int,
    isAsynchronously: Boolean = false,
    crossinline runnable: I.() -> Unit
): TaskHandler {
    return Server.getInstance().scheduler.scheduleDelayedTask(this, { runnable(this) }, tick, isAsynchronously)
}

fun PluginBase.runTaskTimer(
    startTick: Int,
    repeatTick: Int = startTick,
    isAsynchronously: Boolean = false,
    runnable: Runnable
): TaskHandler {
    return Server.getInstance().scheduler.scheduleDelayedRepeatingTask(
        this, runnable,
        startTick,
        repeatTick,
        isAsynchronously
    )
}

inline fun <I : PluginBase> I.runTaskTimer(
    startTick: Int,
    repeatTick: Int = startTick,
    isAsynchronously: Boolean = false,
    crossinline runnable: I.() -> Unit
): TaskHandler {
    return Server.getInstance().scheduler.scheduleDelayedRepeatingTask(
        this,
        { runnable(this) },
        startTick,
        repeatTick,
        isAsynchronously
    )
}