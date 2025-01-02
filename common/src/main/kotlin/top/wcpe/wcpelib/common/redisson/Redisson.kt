package top.wcpe.wcpelib.common.redisson

import org.redisson.api.RedissonClient
import top.wcpe.wcpelib.common.exception.RedissonInstanceNotFoundException

/**
 * 由 WCPE 在 2024/11/19 20:21 创建
 * <p>
 * Created by WCPE on 2024/11/19 20:21
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 */
@Suppress("unused")
object Redisson {
    private var redissonInstance: RedissonInstance? = null

    /**
     * 获取 RedissonInstance 实例
     */
    @JvmStatic
    fun getInstance(): RedissonInstance {
        val current = redissonInstance
        if (current == null) {
            throw RedissonInstanceNotFoundException("Redisson 未找到请尝试重载 WcpeLib 或者重启服务器")
        }
        return current
    }

    /**
     * 获取 Redisson 客户端实例
     */
    @JvmStatic
    fun getClient(): RedissonClient {
        return getInstance().client
    }

    @JvmStatic
    fun init(redissonInstance: RedissonInstance): Redisson {
        this.redissonInstance?.shutdown()
        this.redissonInstance = redissonInstance
        return this
    }


}