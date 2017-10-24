package com.ai.ssa.web.common.utils;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.collections.bidimap.DualHashBidiMap;
import org.mybatis.spring.SqlSessionTemplate;

public class SysDataUtil {
	/**
	 * 
	 * @param sql
	 *            sqlsession
	 * @param sqlMethod
	 *            调用查询方法
	 * @param params
	 *            查询参数
	 * @param key
	 *            数据 key 字段
	 * @param value
	 *            数据 value 字段
	 * @return
	 */
	public static DualHashBidiMap getSysData(SqlSessionTemplate sql, String sqlMethod, HashMap params, String key,
			String value) {
		DualHashBidiMap dbm = new DualHashBidiMap();
		List<HashMap> list = sql.selectList(sqlMethod, params);

		if (!Tools.isNull(list)) {
			for (HashMap map : list) {
				dbm.put(map.get(key), map.get(value));
			}

		}

		return dbm;

	}

}
