package com.ai.ssa.web.common.properties;

import java.io.Serializable;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.redis")
public class RedisProperties implements Serializable {
  private static Integer database;

  private static String host;

  private static String password;

  private static Integer port;

  private static Integer timeout;
  private static Integer maxActive;
  private static Integer maxWait;
  private static Integer maxIdle;
  private static Integer minIdle;
  public static Integer getDatabase() {
    return database;
  }
  public static void setDatabase(Integer database) {
    RedisProperties.database = database;
  }
  public static String getHost() {
    return host;
  }
  public static void setHost(String host) {
    RedisProperties.host = host;
  }
  public static String getPassword() {
    return password;
  }
  public static void setPassword(String password) {
    RedisProperties.password = password;
  }
  public static Integer getPort() {
    return port;
  }
  public static void setPort(Integer port) {
    RedisProperties.port = port;
  }
  public static Integer getTimeout() {
    return timeout;
  }
  public static void setTimeout(Integer timeout) {
    RedisProperties.timeout = timeout;
  }
  public static Integer getMaxActive() {
    return maxActive;
  }
  public static void setMaxActive(Integer maxActive) {
    RedisProperties.maxActive = maxActive;
  }
  public static Integer getMaxWait() {
    return maxWait;
  }
  public static void setMaxWait(Integer maxWait) {
    RedisProperties.maxWait = maxWait;
  }
  public static Integer getMaxIdle() {
    return maxIdle;
  }
  public static void setMaxIdle(Integer maxIdle) {
    RedisProperties.maxIdle = maxIdle;
  }
  public static Integer getMinIdle() {
    return minIdle;
  }
  public static void setMinIdle(Integer minIdle) {
    RedisProperties.minIdle = minIdle;
  }

 

}
