package top.wcpe.wcpelib.bukkit.extend.plugin

import io.ktor.utils.io.*
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeout
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask
import top.wcpe.wcpelib.bukkit.utils.SchedulerUtil
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.function.Supplier

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

inline fun <I : JavaPlugin> I.runTask(
    isAsynchronously: Boolean = false, crossinline runnable: I.() -> Unit,
): BukkitTask {
    return if (isAsynchronously) {
        Bukkit.getScheduler().runTaskAsynchronously(this) { runnable(this) }
    } else {
        Bukkit.getScheduler().runTask(this) { runnable(this) }
    }
}


fun JavaPlugin.runTaskLater(tick: Long, isAsynchronously: Boolean = false, runnable: Runnable): BukkitTask {
    return if (isAsynchronously) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(this, runnable, tick)
    } else {
        Bukkit.getScheduler().runTaskLater(this, runnable, tick)
    }
}

inline fun <I : JavaPlugin> I.runTaskLater(
    tick: Long, isAsynchronously: Boolean = false, crossinline runnable: I.() -> Unit,
): BukkitTask {
    return if (isAsynchronously) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(this, { runnable(this) }, tick)
    } else {
        Bukkit.getScheduler().runTaskLater(this, { runnable(this) }, tick)
    }
}

fun JavaPlugin.runTaskTimer(
    startTick: Long, repeatTick: Long = startTick, isAsynchronously: Boolean = false, runnable: Runnable,
): BukkitTask {
    return if (isAsynchronously) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, runnable, startTick, repeatTick)
    } else {
        Bukkit.getScheduler().runTaskTimer(this, runnable, startTick, repeatTick)
    }
}

inline fun <I : JavaPlugin> I.runTaskTimer(
    startTick: Long,
    repeatTick: Long = startTick,
    isAsynchronously: Boolean = false,
    crossinline runnable: I.() -> Unit,
): BukkitTask {
    return if (isAsynchronously) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, { runnable(this) }, startTick, repeatTick)
    } else {
        Bukkit.getScheduler().runTaskTimer(this, { runnable(this) }, startTick, repeatTick)
    }
}

suspend fun <T> Plugin.runTaskAndWait(task: () -> T?): T? {
    return suspendCancellableCoroutine { continuation ->
        val bukkitTask = server.scheduler.runTask(this) {
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
suspend fun <T> Plugin.runTaskAndWait(timeoutMillis: Long, task: () -> T?): T? {
    return withTimeout(timeoutMillis) {
        runTaskAndWait(task)
    }
}

/**
 * 同步执行任务
 * @param task 待执行的任务
 */
fun Plugin.sync(task: Runnable) {
    Bukkit.getScheduler().runTask(this, task)
}

/**
 * 异步执行任务
 * @param task 待执行的任务
 */
fun Plugin.async(task: Runnable) {
    Bukkit.getScheduler().runTaskAsynchronously(this, task)
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
