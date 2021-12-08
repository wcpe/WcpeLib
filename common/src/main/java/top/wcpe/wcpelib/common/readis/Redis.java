package top.wcpe.wcpelib.common.readis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Redis
 *
 * @author: WCPE 1837019522@qq.com
 * @create: 2021-05-11 10:05
 */
public class Redis {

    private JedisPool jedisPool;

    public Redis(String url, int port) {
        jedisPool = new JedisPool(url, port);
    }

    public Redis(String url, int port, int timeOut, int maxTotal, int maxIdle, int minIdle, boolean jmxEnabled, boolean testOnCreate, boolean blockWhenExhausted, int maxWaitMillis, boolean testOnBorrow, boolean testOnReturn) {
        this(url, port, timeOut, null, maxTotal, maxIdle, minIdle, jmxEnabled, testOnCreate, blockWhenExhausted, maxWaitMillis, testOnBorrow, testOnReturn);
    }

    public Redis(String url, int port, int timeOut, String password, int maxTotal, int maxIdle, int minIdle, boolean jmxEnabled, boolean testOnCreate, boolean blockWhenExhausted, int maxWaitMillis, boolean testOnBorrow, boolean testOnReturn) {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(maxTotal);
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMinIdle(minIdle);
        jedisPoolConfig.setJmxEnabled(jmxEnabled);

        jedisPoolConfig.setTestOnCreate(testOnCreate);
        jedisPoolConfig.setBlockWhenExhausted(blockWhenExhausted);
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        jedisPoolConfig.setTestOnBorrow(testOnBorrow);
        jedisPoolConfig.setTestOnReturn(testOnReturn);
        jedisPool = new JedisPool(jedisPoolConfig, url, port, timeOut, password);
    }

    public Jedis getResource() {
        return jedisPool.getResource();
    }

}
