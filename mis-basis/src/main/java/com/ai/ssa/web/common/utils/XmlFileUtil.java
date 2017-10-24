package com.ai.ssa.web.common.utils;

import org.springframework.stereotype.Component;


public class XmlFileUtil {
 
  
  public static boolean checkXml(String name) {
    String path = StringUtil.getConfPath();
    if (StringUtil.checkPath(path)) {
      String fullname = name + ".xml";
      path = path + "/" + fullname;
      return StringUtil.checkFile(path, fullname) ;
    }

    return true;
  }
}
