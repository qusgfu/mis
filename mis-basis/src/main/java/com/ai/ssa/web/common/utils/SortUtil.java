package com.ai.ssa.web.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class SortUtil {

  public static List sortListByPid(List<HashMap> list,String pidName){
    LinkedHashMap<String,List> result = new LinkedHashMap<>();
    List resultList = new ArrayList<>();
    for (int i = 0,len = list.size(); i < len; i++) {
      HashMap item = list.get(i);
      String pid = item.get(pidName).toString();
      List ilist =  result.get(pid);
      if(ilist == null ){
        ilist = new ArrayList<>();
        HashMap m = new HashMap();
        m.put("pid", pid);
        m.put("data", ilist);
        result.put(pid,ilist);
        resultList.add(m);
      }
      ilist.add(item);
    }
    
     
    return resultList;
  }
  
}
