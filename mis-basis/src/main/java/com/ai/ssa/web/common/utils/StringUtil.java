package com.ai.ssa.web.common.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.jar.JarEntry;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
 

public class StringUtil {
  private static Logger log = LogManager.getLogger(StringUtil.class.getName());
  private final static String CONF_DIR_NAME = "ssaConf";
 
  public static String getJarPath() {
    String path = StringUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    // path = "file:/home/asap/ssa/com.ai.ssa.web.start-1.0.0.jar!/BOOT-INF/lib/common-1.0.0.jar!/";
    path = path.replaceAll("file:", "");
    if (path.indexOf("BOOT-INF") > 0) {
      path = path.substring(0, path.indexOf("BOOT-INF") - 1);
      path = path.substring(0, path.lastIndexOf("/"));
    }
    return path;
  }

  public static String getConfPath() {
    String path =
        PropertiesFileUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath(); 
    if (path.indexOf("BOOT-INF") > 0 && path.indexOf(".jar") > 0) {
      path = path.substring(0, path.indexOf("BOOT-INF") - 1);
      path = path.substring(0, path.lastIndexOf("/"));
    } else if(path.indexOf("ssaLib") > 0 && path.indexOf(".jar")>0 ){
      path = getParentPath(path,2)+"/ssaLib";
    }else{  
      path = System.getProperty("user.dir");
    } 
    path = path.replaceAll("file:", "");  
    path = path + "/"+CONF_DIR_NAME;
    return path;
  }
  public static String getParentPath(String path,int lv){
    for (int i = 0; i < lv; i++) {
      path = path.substring(0, path.lastIndexOf("/"));
    }
    return path;
  }
  public static boolean checkPath(String path) {
    File f = new File(path);
    if (!f.exists() && !f.isDirectory()) {
      return f.mkdirs();
    }
    return f.exists();
  }
  
  public static boolean checkFile(String path, String fullname) {
    log.debug("=======================" + path);
    File f = new File(path);
    String jarPath =   PropertiesFileUtil.class.getProtectionDomain().getCodeSource().getLocation().toString();
    log.debug(jarPath);
    InputStream in = null;
    try {
      if (jarPath.indexOf(".jar") > 0) {
        URL url; 
        if (jarPath.indexOf("BOOT-INF") > 0 ||  (jarPath).indexOf("jar:")==0) { 
          url = new URL( jarPath + fullname);
        } else {
          url = new URL("jar:" + jarPath + "!/" + fullname);
        }
       

        JarURLConnection conn = (JarURLConnection) url.openConnection();
        JarEntry je = conn.getJarEntry();
        in = conn.getJarFile().getInputStream(je);
      } else {
        in = Object.class.getClass().getResourceAsStream("/" + fullname);
      }

      if (!f.exists()) {
        FileUtils.copyInputStreamToFile(in, f);
      }
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }finally {
      try {
        if(in != null)in.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return true;
  }
}
