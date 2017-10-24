package com.ai.ssa.web.common.base;

import java.io.Serializable;

/**
 * 统一返回结果类
 */
public class BaseResult implements Serializable{

	private Object data;
	private String message;
	private boolean state = true;

 
	public boolean getState() {
		return state;
	}
	public void setState(boolean state) {
		this.state = state;
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

}
