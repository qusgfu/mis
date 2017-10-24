package com.ai.ssa.web.common.utils;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ai.ssa.web.common.properties.RedisProperties;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Redis 工具类
 */
@Component
public class RedisUtil {

	protected static ReentrantLock lockPool = new ReentrantLock();
	protected static ReentrantLock lockJedis = new ReentrantLock();
	@Autowired
	RedisProperties redisProperties;
	private static Logger _log = LoggerFactory.getLogger(RedisUtil.class);

	// Redis服务器IP
	private static String IP = RedisProperties.getHost();

	// Redis的端口号
	private static int PORT = RedisProperties.getPort();
	// 访问密码
	private static String PASSWORD = RedisProperties.getPassword();
	// 超时时间
	private static int TIMEOUT = RedisProperties.getTimeout();

	// 可用连接实例的最大数目，默认值为8；
	// 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
	private static int MAX_ACTIVE = RedisProperties.getMaxActive();

	// 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
	private static int MAX_IDLE = RedisProperties.getMaxIdle();

	// 等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
	private static int MAX_WAIT = RedisProperties.getMaxWait();

	// 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
	private static boolean TEST_ON_BORROW = false;

	private static JedisPool jedisPool = null;
	private static JedisCluster jedisCluster = null;
	/**
	 * redis过期时间,以秒为单位
	 */
	public final static int EXRP_HOUR = 60 * 60; // 一小时
	public final static int EXRP_DAY = 60 * 60 * 24; // 一天
	public final static int EXRP_MONTH = 60 * 60 * 24 * 30; // 一个月
	protected static final int DEFAULT_TIMEOUT = 2000;
	protected static final int DEFAULT_MAX_REDIRECTIONS = 5;
	private static String redisType ;
	private static JedisPoolConfig  config;
	static{
		config  = new JedisPoolConfig();
		config.setMaxTotal(MAX_ACTIVE);
		config.setMaxIdle(MAX_IDLE);
		config.setMaxWaitMillis(MAX_WAIT);
		config.setTestOnBorrow(TEST_ON_BORROW);
		String[] clusterIps = IP.split("[|]");
		if (clusterIps.length == 1) {
			redisType = "noCluster";
		}else{
			redisType = "cluster";
		}
	}
	/**
	 * 初始化Redis连接池
	 */
	private static void initialPool() {
		try { 
			
			jedisPool = new JedisPool(config, IP, PORT, TIMEOUT);

		} catch (Exception e) {
			_log.error("First create JedisPool error : " + e);
		}
	}
	
	private static void initialCluster() {
		try { 
			
			    String[] clusterIps = IP.split("[|]"); 
				Set<HostAndPort> nodes = new LinkedHashSet<HostAndPort>();
				for (String clusterIp : clusterIps) {
					String[] ipProt = clusterIp.trim().split(":");
					nodes.add(new HostAndPort(ipProt[0].trim(), Integer.valueOf(ipProt[1].trim())));
				}
				if(!StringUtils.isEmpty(PASSWORD))
				     jedisCluster = new JedisCluster(nodes, TIMEOUT, DEFAULT_TIMEOUT, DEFAULT_MAX_REDIRECTIONS, PASSWORD,config); 
				else 
				     jedisCluster = new JedisCluster(nodes, TIMEOUT, DEFAULT_TIMEOUT, DEFAULT_MAX_REDIRECTIONS, config); 
		} catch (Exception e) {
			_log.error("First create JedisPool error : " + e);
		}
	}

	/**
	 * 在多线程环境同步初始化
	 */
	private static synchronized void poolInit() {
		 
		if (null == jedisPool && "noCluster".equals(redisType)) {
			initialPool();
		}
		
		if (null == jedisCluster && "cluster".equals(redisType)) {
			initialCluster();
		}
	}

	/**
	 * 同步获取Jedis实例
	 * 
	 * @return Jedis
	 */
	public synchronized static JedisCommands getJedis() {
		poolInit();
		JedisCommands jedisCommands = null; 
		if (null != jedisPool) {
			try {
				Jedis jedis = jedisPool.getResource();
				if(!StringUtils.isEmpty(PASSWORD))jedis.auth(PASSWORD);
				jedisCommands = jedis;
			} catch (Exception e) {
				_log.error("Get jedis error : " + e);
			}
		}

		if (null != jedisCluster) {
			jedisCommands = jedisCluster;
		}

		return jedisCommands;
	}
	
	public static void close(JedisCommands jedis){
		if(null != jedisPool && "noCluster".equals(redisType)){
			((Jedis)jedis).close();
		}
	}
	/**
	 * 设置 String
	 * 
	 * @param key
	 * @param value
	 */
	public synchronized static void set(String key, String value) {
		try {
			value = StringUtils.isBlank(value) ? "" : value;
			JedisCommands jedis = getJedis();
			jedis.set(key, value); 
			close(jedis);
		} catch (Exception e) {
			_log.error("Set key error : " + e);
		}
	}

 
	/**
	 * 设置 String 过期时间
	 * 
	 * @param key
	 * @param value
	 * @param seconds
	 *            以秒为单位
	 */
	public synchronized static void set(String key, String value, int seconds) {
		try {
			value = StringUtils.isBlank(value) ? "" : value;
			JedisCommands jedis = getJedis();
			jedis.setex(key, seconds, value);
			close(jedis);
		} catch (Exception e) {
			_log.error("Set keyex error : " + e);
		}
	}

	 
	/**
	 * 获取String值
	 * 
	 * @param key
	 * @return value
	 */
	public synchronized static String get(String key) {
		JedisCommands jedis = getJedis();
		if (null == jedis) {
			return null;
		}
		String value = jedis.get(key);
		close(jedis);
		return value;
	}

 
	/**
	 * 删除值
	 * 
	 * @param key
	 */
	public synchronized static void remove(String key) {
		try {
			JedisCommands jedis = getJedis();
			jedis.del(key);
			close(jedis);
		} catch (Exception e) {
			_log.error("Remove keyex error : " + e);
		}
	}

	 

	/**
	 * lpush
	 * 
	 * @param key
	 * @param key
	 */
	public synchronized static void lpush(String key, String... strings) {
		try {
			JedisCommands jedis = RedisUtil.getJedis();
			jedis.lpush(key, strings);
			close(jedis);
		} catch (Exception e) {
			_log.error("lpush error : " + e);
		}
	}

	/**
	 * lrem
	 * 
	 * @param key
	 * @param count
	 * @param value
	 */
	public synchronized static void lrem(String key, long count, String value) {
		try {
			JedisCommands jedis = RedisUtil.getJedis();
			jedis.lrem(key, count, value);
			close(jedis);
		} catch (Exception e) {
			_log.error("lpush error : " + e);
		}
	}

	/**
	 * sadd
	 * 
	 * @param key
	 * @param value
	 * @param seconds
	 */
	public synchronized static void sadd(String key, String value, int seconds) {
		try {
			JedisCommands jedis = RedisUtil.getJedis();
			jedis.sadd(key, value);
			jedis.expire(key, seconds);
			close(jedis);
		} catch (Exception e) {
			_log.error("sadd error : " + e);
		}
	}

	public String getRedisType() {
		return redisType;
	}

	public void setRedisType(String redisType) {
		this.redisType = redisType;
	}

	
}
