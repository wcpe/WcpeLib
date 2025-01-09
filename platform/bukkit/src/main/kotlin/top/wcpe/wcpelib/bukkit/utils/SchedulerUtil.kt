package top.wcpe.wcpelib.bukkit.utils

import io.ktor.utils.io.*
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import java.util.function.Supplier

/**
 * 由 WCPE 在 2025/1/8 19:13 创建
 * <p>
 * Created by WCPE on 2025/1/8 19:13
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
        Bukkit.getScheduler().runTask(plugin, task)
    }

    /**
     * 异步执行任务
     * @param plugin 插件实例
     * @param task 待执行的任务
     */
    @JvmStatic
    fun async(plugin: Plugin, task: Runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, task)
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
        Bukkit.getScheduler().runTask(plugin) {
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
        Bukkit.getScheduler().runTask(plugin) {
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