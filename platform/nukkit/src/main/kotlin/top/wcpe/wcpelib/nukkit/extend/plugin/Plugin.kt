package top.wcpe.wcpelib.nukkit.extend.plugin

import cn.nukkit.Server
import cn.nukkit.plugin.Plugin
import cn.nukkit.plugin.PluginBase
import cn.nukkit.scheduler.TaskHandler
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeout
import top.wcpe.wcpelib.nukkit.utils.SchedulerUtil
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.function.Supplier

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
    crossinline runnable: I.() -> Unit,
): TaskHandler {
    return Server.getInstance().scheduler.scheduleTask(this, { runnable(this) }, isAsynchronously)
}

fun PluginBase.runTaskLater(tick: Int, isAsynchronously: Boolean = false, runnable: Runnable): TaskHandler {
    return Server.getInstance().scheduler.scheduleDelayedTask(this, runnable, tick, isAsynchronously)
}

inline fun <I : PluginBase> I.runTaskLater(
    tick: Int,
    isAsynchronously: Boolean = false,
    crossinline runnable: I.() -> Unit,
): TaskHandler {
    return Server.getInstance().scheduler.scheduleDelayedTask(this, { runnable(this) }, tick, isAsynchronously)
}

fun PluginBase.runTaskTimer(
    startTick: Int,
    repeatTick: Int = startTick,
    isAsynchronously: Boolean = false,
    runnable: Runnable,
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
    crossinline runnable: I.() -> Unit,
): TaskHandler {
    return Server.getInstance().scheduler.scheduleDelayedRepeatingTask(
        this,
        { runnable(this) },
        startTick,
        repeatTick,
        isAsynchronously
    )
}

suspend fun <T> PluginBase.runTaskAndWait(task: () -> T?): T? {
    return suspendCancellableCoroutine { continuation ->
        val bukkitTask = server.scheduler.scheduleTask(this) {
            continuation.resumeWith(Result.runCatching {
                task()
            })
        }
        continuation.invokeOnCancellation {
            bukkitTask.cancel()
        }
    }
}

@Throws(TimeoutCancellationException::class)
suspend fun <T> PluginBase.runTaskAndWait(timeoutMillis: Long, task: () -> T?): T? {
    return withTimeout(timeoutMillis) {
        runTaskAndWait(task)
    }
}


/**
 * 同步执行任务
 * @param task 待执行的任务
 */
fun Plugin.sync(task: Runnable) {
    Server.getInstance().scheduler.scheduleTask(this, task)
}

/**
 * 异步执行任务
 * @param task 待执行的任务
 */
fun Plugin.async(task: Runnable) {
    Server.getInstance().scheduler.scheduleTask(this, task, true)
}

/**
 * 同步到主线程执行任务并等待完成
 * @param task 待执行的任务
 * @return 任务执行结果
 * @throws CancellationException 任务被取消
 * @throws ExecutionException 执行出现异常
 * @throws InterruptedException 线程被中断
 */
@Throws(
    CancellationException::class,
    ExecutionException::class,
    InterruptedException::class
)
fun <T> Plugin.awaitSync(task: Supplier<T>): T? {
    return SchedulerUtil.awaitSync(this, task)
}


/**
 * 异步到主线程执行任务并等待完成
 * @param task 待执行的任务
 * @return 任务执行结果
 * @throws CancellationException 任务被取消
 * @throws ExecutionException 执行出现异常
 * @throws InterruptedException 线程被中断
 */
@Throws(
    CancellationException::class,
    ExecutionException::class,
    InterruptedException::class
)
fun <T> Plugin.awaitSync(
    timeout: Long,
    unit: TimeUnit,
    task: Supplier<T>,
    timeoutCallback: Runnable = Runnable { },
): T? {
    return SchedulerUtil.awaitSync(this, timeout, unit, task, timeoutCallback)
}

