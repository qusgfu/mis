package com.ai.ssa.web.service.api;

import java.util.List;

import com.ai.ssa.web.common.model.umps.UpmsPlugin;

/**
 * BaseService接口
 * Created by ZhangShuzheng on 2017/01/07.
 */
public interface BaseService<Record, Example> {

	int countByExample(Example example);

	int deleteByExample(Example example);

	int deleteByPrimaryKey(Integer id);

	int insert(Record record);
	Record insertReId(Record record);

	int insertSelective(Record record);

	List<Record> selectByExampleWithBLOBs(Example example);

	List<Record> selectByExample(Example example);

	Record selectFirstByExample(Example example);

	Record selectFirstByExampleWithBLOBs(Example example);

	Record selectByPrimaryKey(Integer id);

	int updateByExampleSelective( Record record,   Example example);

	int updateByExampleWithBLOBs(  Record record,  Example example);

	int updateByExample(  Record record,  Example example);

	int updateByPrimaryKeySelective(Record record);

	int updateByPrimaryKeyWithBLOBs(Record record);

	int updateByPrimaryKey(Record record);

	int deleteByPrimaryKeys(String ids);

	void initMapper();
 

}