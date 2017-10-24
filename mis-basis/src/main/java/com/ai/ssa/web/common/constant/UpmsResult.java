package com.ai.ssa.web.common.constant;

import com.ai.ssa.web.common.base.BaseResult;

/**
 * upms系统常量枚举类
 */
public class UpmsResult   {
	
	 // 状态码：1成功，其他为失败
    public int code;

    // 成功为success，其他为失败原因
    public String message;

    // 数据结果集
    public Object data;
    
    public String username;

    public UpmsResult(UpmsResultConstant upmsResultConstant, Object data,String username) {
    	 this.code = upmsResultConstant.getCode();
         this.message =  upmsResultConstant.getMessage();
         this.data = data;
         this.username = username;
    }
    public UpmsResult(UpmsResultConstant upmsResultConstant, Object data) {
      this.code = upmsResultConstant.getCode();
      this.message =  upmsResultConstant.getMessage();
      this.data = data;
      this.username = "";
 }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }


    public String getUsername() {
      return username;
    }


    public void setUsername(String username) {
      this.username = username;
    }
	
    
    
    
}
