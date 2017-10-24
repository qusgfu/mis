package com.ai.ssa.web.common.base;

import java.io.Serializable;
import java.util.List;

public class PageResult implements Serializable{
	private Long total;
	private List rowData;

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public List getRowData() {
		return rowData;
	}

	public void setRowData(List rowData) {
		this.rowData = rowData;
	}

}
