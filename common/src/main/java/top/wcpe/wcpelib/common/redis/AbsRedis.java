package top.wcpe.wcpelib.common.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public abstract class AbsRedis {
    protected JedisPool jedisPool;
    protected RedisConfig redisConfig;

    public AbsRedis(RedisConfig redisConfig) {
        this.redisConfig = redisConfig;
        this.jedisPool = redisConfig.build();
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public RedisConfig getRedisConfig() {
        return redisConfig;
    }
    public ResourceProxy getResourceProxy() {
        return getResourceProxy(redisConfig.getIndex());
    }
    public ResourceProxy getResourceProxy(int index) {
        if (jedisPool == null) {
            throw new IllegalStateException("jedisPool is not initialized");
        }

        Jedis jedis = jedisPool.getResource();
        if (jedis == null) {
            throw new RuntimeException("Failed to get Jedis resource");
        }

        try {
            jedis.select(index); // 切换数据库
            return new ResourceProxy(jedis, redisConfig.getExpire());
        } catch (Exception e) {
            jedis.close(); // 发生异常时关闭连接
            throw e;
        }
    }
}
