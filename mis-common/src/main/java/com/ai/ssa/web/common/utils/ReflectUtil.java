package com.ai.ssa.web.common.utils;

import java.lang.reflect.Method;

public class ReflectUtil {
	 public static Object getObjValue(Object bean,String param){
		 return getObjValue(bean,param,false);
	  }
	 
	 public static Object getObjPrivateValue(Object bean,String param){
		 return getObjValue(bean,param,true);
	 }
	 
	 public static Object getObjValue(Object bean,String param,Boolean flag){
	  	   Method[] methods = bean.getClass().getMethods();
	  	   Object result = null;
	         for(int i = 0; i<methods.length; i++){
	             Method m = methods[i];
	             m.setAccessible(flag);
	             String name =m.getName();
	             String methodName = getMethodName("get",param);
	             if(methodName.equals(name)){
		               try {
							  result = m.invoke(bean);
						} catch (Exception e) { 
							e.printStackTrace();
						}  
	             }
	         }
	        return result; 
		   }
	 
	
	 
	   public static String getMethodName(String method,String name){ 
	        String start = name.substring(0,1);
	        String methodName = method+start.toUpperCase()+name.substring(1);
	        return methodName;
	    }
}
