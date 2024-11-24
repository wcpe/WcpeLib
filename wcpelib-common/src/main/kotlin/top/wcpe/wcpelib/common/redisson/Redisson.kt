package top.wcpe.wcpelib.common.redisson

import org.redisson.api.RedissonClient

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
    @JvmStatic
    lateinit var redissonInstance: RedissonInstance
        private set

    @JvmStatic
    lateinit var client: RedissonClient
        private set


    @JvmStatic
    fun init(redissonInstance: RedissonInstance): Redisson {
        this.redissonInstance = redissonInstance
        this.client = redissonInstance.redissonClient
        return this
    }


}