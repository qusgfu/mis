package com.ai.ssa.web.common.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.MissingResourceException;
import java.util.Properties;

/**
 * 资源文件读取工具
 * 
 * @author shuzheng
 * @date 2016年10月15日
 */
public class PropertiesFileUtil {

  // 当打开多个资源文件时，缓存资源文件
  private static HashMap<String, PropertiesFileUtil> configMap =
      new HashMap<String, PropertiesFileUtil>();
  // 打开文件时间，判断超时使用
  private Date loadTime = null;
  // 资源文件
  private Properties resourceBundle = null;
  // 默认资源文件名称
  private static final String NAME = "config";
  // 缓存时间
  private static final Integer TIME_OUT = 60 * 1000;
 
  // public static void main(String[] args) {
  //
  // System.out.println(in);
  // }
  // 私有构造方法，创建单例
  private PropertiesFileUtil(String name) {
    this.loadTime = new Date();
    String filePath = getPropertiesPath(name);;
    InputStream in = null;
    InputStreamReader isr = null;
    try {
      in = new FileInputStream(filePath);
      isr = new InputStreamReader(in, "UTF-8");
      this.resourceBundle = new Properties();
      this.resourceBundle.load(isr);;
    } catch (Exception e) {
      e.printStackTrace();
    }finally{
      try {
       if(isr!=null)isr.close();
       if(in!=null) in.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public static synchronized PropertiesFileUtil getInstance() {
    return getInstance(NAME);
  }

  public static String getPropertiesPath(String name) {
    String path = StringUtil.getConfPath();
    if (StringUtil.checkPath(path)) {
      String fullname = name + ".properties";
      path = path + "/" + fullname;
      if (!StringUtil.checkFile(path, fullname)) {
        path = null;
      }
    }
    return path;

  }

 

  

  public static synchronized PropertiesFileUtil getInstance(String name) {
    PropertiesFileUtil conf = configMap.get(name);
    if (null == conf) {
      conf = new PropertiesFileUtil(name);
      configMap.put(name, conf);
    }
    // 判断是否打开的资源文件是否超时1分钟
    if ((new Date().getTime() - conf.getLoadTime().getTime()) > TIME_OUT) {
      conf = new PropertiesFileUtil(name);
      configMap.put(name, conf);
    }
    return conf;
  }

  // 根据key读取value
  public String get(String key) {
    try {
      String value = resourceBundle.getProperty(key);
      return value;
    } catch (MissingResourceException e) {
      return "";
    }
  }

  // 根据key读取value(整形)
  public Integer getInt(String key) {
    try {
      String value = resourceBundle.getProperty(key);
      return Integer.parseInt(value);
    } catch (MissingResourceException e) {
      return null;
    }
  }

  // 根据key读取value(布尔)
  public boolean getBool(String key) {
    try {
      String value = resourceBundle.getProperty(key);
      if ("true".equals(value)) {
        return true;
      }
      return false;
    } catch (MissingResourceException e) {
      return false;
    }
  }

  public Properties getResourceBundle() {
    return resourceBundle;
  }

  public Date getLoadTime() {
    return loadTime;
  }

}
