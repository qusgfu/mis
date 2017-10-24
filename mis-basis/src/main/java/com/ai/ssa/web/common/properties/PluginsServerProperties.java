package com.ai.ssa.web.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "plugins.server")
public class PluginsServerProperties {
  private static String user;
  private static String password;
  private static String fileinstallDirs;
  public static String getUser() {
    return user;
  }
  public static void setUser(String user) {
    PluginsServerProperties.user = user;
  }
  public static String getPassword() {
    return password;
  }
  public static void setPassword(String password) {
    PluginsServerProperties.password = password;
  }
  public static String getFileinstallDirs() {
    return fileinstallDirs;
  }
  public static void setFileinstallDirs(String fileinstallDirs) {
    PluginsServerProperties.fileinstallDirs = fileinstallDirs;
  }


}
