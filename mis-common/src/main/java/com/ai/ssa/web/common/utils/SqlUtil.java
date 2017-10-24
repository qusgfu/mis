package com.ai.ssa.web.common.utils;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import com.ai.ssa.web.common.base.PageResult;
import com.github.pagehelper.PageRowBounds;

public class SqlUtil {
	 private final static Logger _log = LoggerFactory.getLogger(SqlUtil.class);
	public static PageResult paginationQuery(SqlSessionTemplate sqlSessionTemplate, String sqlMethod, Map param) {
		Integer pageSize = Integer.valueOf(param.get("pageSize").toString());
		Integer pageIndex = Integer.valueOf(param.get("pageIndex").toString());
		PageRowBounds prb = new PageRowBounds((pageIndex - 1) * pageSize, pageSize);
		List data = sqlSessionTemplate.selectList(sqlMethod, param, prb);
		PageResult result = new PageResult(); 
		result.setRowData(data);
		result.setTotal(prb.getTotal());
		return result;
	}

	public static PageResult paginationQuery(SqlSessionTemplate sqlSessionTemplate, String sqlMethod, Object param) {
		Integer pageSize = Integer.valueOf(ReflectUtil.getObjValue(param, "pageSize").toString());
		Integer pageIndex = Integer.valueOf(ReflectUtil.getObjValue(param, "pageIndex").toString());
		PageRowBounds prb = new PageRowBounds((pageIndex - 1) * pageSize, pageSize);
		List data = sqlSessionTemplate.selectList(sqlMethod, param, prb);
		PageResult result = new PageResult();
		result.setRowData(data);
		result.setTotal(prb.getTotal());

		return result;
	}

	private static Long getCount(SqlSessionTemplate sqlSessionTemplate, String sqlMethod, Object param){
		String sql = SqlHelper.getNamespaceSql(sqlSessionTemplate, sqlMethod,param);
		int index = sql.toLowerCase().indexOf("from");  
		int groupIndex = sql.toLowerCase().indexOf("group by");
		if(groupIndex<0)sql = "select count(*) " + sql.substring(index);
		else sql = "select count(*) from (" + sql+") t";
		_log.debug(sql);
		Long total = sqlSessionTemplate.selectOne("common.getPageCount", sql); 
		return total;
	}

}
