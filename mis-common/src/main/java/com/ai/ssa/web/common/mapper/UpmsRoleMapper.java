package com.ai.ssa.web.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ai.ssa.web.common.model.umps.UpmsRole;
import com.ai.ssa.web.common.model.umps.UpmsRoleExample;

public interface UpmsRoleMapper {
    long countByExample(UpmsRoleExample example);

    int deleteByExample(UpmsRoleExample example);

    int deleteByPrimaryKey(Integer roleId);

    int insert(UpmsRole record);

    int insertSelective(UpmsRole record);

    List<UpmsRole> selectByExample(UpmsRoleExample example);

    UpmsRole selectByPrimaryKey(Integer roleId);

    int updateByExampleSelective(@Param("record") UpmsRole record, @Param("example") UpmsRoleExample example);

    int updateByExample(@Param("record") UpmsRole record, @Param("example") UpmsRoleExample example);

    int updateByPrimaryKeySelective(UpmsRole record);

    int updateByPrimaryKey(UpmsRole record);
}