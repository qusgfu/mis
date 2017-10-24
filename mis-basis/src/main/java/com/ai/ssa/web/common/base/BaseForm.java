package com.ai.ssa.web.common.base;

import java.io.Serializable;

/**
 * 分页基础类
 * 
 * @author zhaolinze
 *
 */
public class BaseForm implements Serializable{
	private Integer pageSize;
	private Integer pageIndex;
	private String sort;
	private String sort_key;
	private Integer startLimit;

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getSort_key() {
		return sort_key;
	}

	public void setSort_key(String sort_key) {
		this.sort_key = sort_key;
	}

	public Integer getStartLimit() {
		return startLimit;
	}

	public void setStartLimit(Integer startLimit) {
		this.startLimit = startLimit;
	}

}
