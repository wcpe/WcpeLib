package top.wcpe.wcpelib.common.redisson

import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import top.wcpe.wcpelib.common.redis.RedisConfig
import java.io.Closeable

/**
 * 由 WCPE 在 2024/11/19 20:03 创建
 * <p>
 * Created by WCPE on 2024/11/19 20:03
 * <p>
 * <p>
 * GitHub  : <a href="https://github.com/wcpe">wcpe 's GitHub</a>
 * <p>
 * QQ      : 1837019522
 * @author : WCPE
 */
class RedissonInstance(
    redisConfig: RedisConfig,
) : Closeable {

    val redissonClient: RedissonClient

    init {
        val url = redisConfig.url
        val port = redisConfig.port
        val password = redisConfig.password
        val timeOut = redisConfig.timeOut
        val index = redisConfig.index
        val minIdle = redisConfig.minIdle
        val maxTotal = redisConfig.maxTotal
        val config = Config()
        config.useSingleServer().setAddress("redis://${url}:$port").setPassword(password).setTimeout(timeOut)
            .setDatabase(index).setConnectionMinimumIdleSize(minIdle).setConnectionPoolSize(maxTotal)

        redissonClient = Redisson.create(config)
    }

    override fun close() {
        redissonClient.shutdown()
    }

}