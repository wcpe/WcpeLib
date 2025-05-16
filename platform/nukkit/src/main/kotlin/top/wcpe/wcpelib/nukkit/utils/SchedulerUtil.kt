package top.wcpe.wcpelib.nukkit.utils

import cn.nukkit.Server
import cn.nukkit.plugin.Plugin
import cn.nukkit.scheduler.AsyncTask
import io.ktor.utils.io.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import java.util.function.Supplier

/**
 * 由 WCPE 在 2025/5/13 13:16 创建
 * <p>
 * Created by WCPE on 2025/5/13 13:16
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 */
@Suppress("unused")
object SchedulerUtil {

    /**
     * 同步执行任务
     * @param plugin 插件实例
     * @param task 待执行的任务
     */
    @JvmStatic
    fun sync(plugin: Plugin, task: Runnable) {
        Server.getInstance().scheduler.scheduleTask(plugin, task)
    }

    /**
     * 异步执行任务
     * @param plugin 插件实例
     * @param task 待执行的任务
     */
    @JvmStatic
    fun async(plugin: Plugin, task: Runnable) {
        Server.getInstance().scheduler.scheduleTask(plugin, task, true)
    }

    /**
     * 异步执行任务
     * @param plugin 插件实例
     * @param task 待执行的任务
     */
    @JvmStatic
    fun async(plugin: Plugin, task: AsyncTask) {
        Server.getInstance().scheduler.scheduleAsyncTask(plugin, task)
    }

    /**
     * 同步到主线程执行任务并等待完成
     * @param plugin 插件实例
     * @param task 待执行的任务
     * @return 任务执行结果
     * @throws CancellationException 任务被取消
     * @throws ExecutionException 执行出现异常
     * @throws InterruptedException 线程被中断
     */
    @JvmStatic
    @Throws(
        CancellationException::class,
        ExecutionException::class,
        InterruptedException::class
    )
    fun <T> awaitSync(plugin: Plugin, task: Supplier<T>): T? {
        val future = CompletableFuture<T>()
        Server.getInstance().scheduler.scheduleTask(plugin) {
            try {
                val result = task.get()
                future.complete(result)
            } catch (e: Exception) {
                future.completeExceptionally(e)
            }
        }

        return future.get()
    }


    /**
     * 异步到主线程执行任务并等待完成
     * @param plugin 插件实例
     * @param task 待执行的任务
     * @return 任务执行结果
     * @throws CancellationException 任务被取消
     * @throws ExecutionException 执行出现异常
     * @throws InterruptedException 线程被中断
     */
    @JvmStatic
    @Throws(
        CancellationException::class,
        ExecutionException::class,
        InterruptedException::class
    )
    fun <T> awaitSync(
        plugin: Plugin,
        timeout: Long,
        unit: TimeUnit,
        task: Supplier<T>,
        timeoutCallback: Runnable = Runnable { },
    ): T? {
        val future = CompletableFuture<T>()
        Server.getInstance().scheduler.scheduleTask(plugin) {
            try {
                val result = task.get()
                future.complete(result)
            } catch (e: Exception) {
                future.completeExceptionally(e)
            }
        }

        return try {
            future.get(timeout, unit)
        } catch (_: TimeoutException) {
            timeoutCallback.run()
            null
        }
    }

}