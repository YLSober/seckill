package com.test.seckill;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisPoolUtil {
	private static volatile JedisPool jedisPool = null;

	private JedisPoolUtil() {
	}

	public static JedisPool getJedisPoolInstance() {
		if (null == jedisPool) {
			synchronized (JedisPoolUtil.class) {
				if (null == jedisPool) {
					JedisPoolConfig poolConfig = new JedisPoolConfig();
					poolConfig.setMaxTotal(200);// 最大连接数
					poolConfig.setMaxIdle(32); // 最大空闲数
					poolConfig.setBlockWhenExhausted(true);// 当出现连接不足的情况下，阻塞等待
					poolConfig.setMaxWaitMillis(100 * 1000);// 连接不足等待时间为100秒
															// 配合上一个一起使用
					poolConfig.setTestOnBorrow(true); // 在从连接池中拿连接的时候，是否进行连接验证，如果返回true。则证明连接可用。
					jedisPool = new JedisPool(poolConfig, "192.168.17.128",
							6379, 60000); // 60秒连接时间，超过60秒则池子崩溃
				}
			}
		}
		return jedisPool;
	}

	public static void release(JedisPool jedisPool, Jedis jedis) {
		if (null != jedis) {
			jedisPool.returnResource(jedis);
		}
	}

}
