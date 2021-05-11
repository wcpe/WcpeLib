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

    public Redis(String url, int port, int maxTotal, int maxIdle, int minIdle, int maxWaitMillis, boolean testOnBorrow, boolean testOnReturn) {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(maxTotal);
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMinIdle(minIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        jedisPoolConfig.setTestOnBorrow(testOnBorrow);
        jedisPoolConfig.setTestOnReturn(testOnReturn);
        jedisPool = new JedisPool(jedisPoolConfig, url, port);
    }

    public Jedis getResource() {
        return jedisPool.getResource();
    }
    public  void returnBrokenResource(Jedis jedis){
        jedisPool.returnBrokenResource(jedis);
    }

    public  void returnResource(Jedis jedis){
        jedisPool.returnResource(jedis);
    }
}
