package com.ai.ssa.web.common.bean;
/**
 * 缓存对象
 *
 */
public class Cache {
	// 缓存标识
	private String key;

	// 缓存数据
	private Object data;
	// 更新时间
	private long updateTime;
	// 是否过期
	private boolean expired;
	
	public Cache(String key, Object data) {
		this.key = key;
		this.data = data;
	}
	
	

	public Cache(String key, Object data, long updateTime, boolean expired) {
		this.key = key;
		this.data = data;
		this.updateTime = updateTime;
		this.expired = expired;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public boolean isExpired() {
		return expired;
	}

	public void setExpired(boolean expired) {
		this.expired = expired;
	}

}
