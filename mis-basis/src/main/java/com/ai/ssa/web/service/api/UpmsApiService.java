package com.ai.ssa.web.service.api;

import java.util.HashMap;
import java.util.List;

import com.ai.ssa.web.common.base.BaseResult;
import com.ai.ssa.web.common.model.umps.UpmsLog;
import com.ai.ssa.web.common.model.umps.UpmsOrganization;
import com.ai.ssa.web.common.model.umps.UpmsOrganizationExample;
import com.ai.ssa.web.common.model.umps.UpmsRole;
import com.ai.ssa.web.common.model.umps.UpmsRolePermission;
import com.ai.ssa.web.common.model.umps.UpmsSystem;
import com.ai.ssa.web.common.model.umps.UpmsSystemExample;
import com.ai.ssa.web.common.model.umps.UpmsUser;
import com.ai.ssa.web.common.model.umps.UpmsUserPermission;
import com.ai.ssa.web.common.model.umps.VMenus;

/**
 * upms系统接口
 */
public interface UpmsApiService {

    /**
     * 根据用户id获取所拥有的权限(用户和角色权限合集)
     * @param upmsUserId
     * @return
     */
    List<VMenus> selectUpmsPermissionByUpmsUserId(Integer upmsUserId);

    /**
     * 根据用户id获取所属的角色
     * @param upmsUserId
     * @return
     */
    List<UpmsRole> selectUpmsRoleByUpmsUserId(Integer upmsUserId);

    /**
     * 根据角色id获取所拥有的权限
     * @param upmsRoleId
     * @return
     */
    List<UpmsRolePermission> selectUpmsRolePermisstionByUpmsRoleId(Integer upmsRoleId);

    /**
     * 根据用户id获取所拥有的权限
     * @param upmsUserId
     * @return
     */
    List<UpmsUserPermission> selectUpmsUserPermissionByUpmsUserId(Integer upmsUserId);

    /**
     * 根据条件获取系统数据
     * @param upmsSystemExample
     * @return
     */
    List<UpmsSystem> selectUpmsSystemByExample(UpmsSystemExample upmsSystemExample);

    /**
     * 根据条件获取组织数据
     * @param upmsOrganizationExample
     * @return
     */
    List<UpmsOrganization> selectUpmsOrganizationByExample(UpmsOrganizationExample upmsOrganizationExample);

    /**
     * 根据username获取UpmsUser
     * @param username
     * @return
     */
    UpmsUser selectUpmsUserByUsername(String username);

    int insertLog(UpmsLog log);
    /**
     * 写入操作日志
     * @param record
     * @return
     */
    int insertUpmsLogSelective(UpmsLog record);

    HashMap selectMenuByModule(String module);
    Boolean checkUserName(String userName);

    BaseResult insertUser(UpmsUser user);

	List<VMenus> selectUpmsPermissionByUpmsRoleName(String string);
}
