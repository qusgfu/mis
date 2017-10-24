package com.ai.ssa.web.common.utils;

import com.ai.ssa.web.common.base.BaseResult;

public class ResultUtil {
	
 public static BaseResult getInstance(Object data,String msg,Boolean state){
	 BaseResult r = getInstance(data,msg);
	 r.setState(state);
	 return r;
 }	
	
 public static BaseResult getInstance(Object data,String msg){
		 BaseResult r = getInstance(data); 
		 r.setMessage(msg);
		 return r;
 }	
 
 public static BaseResult getInstance(Object data){
	 BaseResult r = new BaseResult();
	 r.setData(data);
	 return r;
 }
}
