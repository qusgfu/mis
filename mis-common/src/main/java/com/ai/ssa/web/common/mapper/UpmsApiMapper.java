package com.ai.ssa.web.common.mapper;


import java.util.HashMap;
import java.util.List;

import com.ai.ssa.web.common.model.umps.UpmsLog;
import com.ai.ssa.web.common.model.umps.UpmsPermission;
import com.ai.ssa.web.common.model.umps.UpmsRole;
import com.ai.ssa.web.common.model.umps.UpmsUser;
import com.ai.ssa.web.common.model.umps.VMenus;

/**
 * 用户VOMapper
 */
public interface UpmsApiMapper {

	// 根据用户id获取所拥有的权限
	List<VMenus> selectUpmsPermissionByUpmsUserId(Integer upmsUserId);

	// 根据用户id获取所属的角色
	List<UpmsRole> selectUpmsRoleByUpmsUserId(Integer upmsUserId);
	List<VMenus> selectUpmsPermissionByAdmin();
	UpmsUser selectUserById(Integer upmsUserId);
	
	int insertLog(UpmsLog log);

  HashMap selectMenuByModule(String module);

Integer checkUserName(String userName);

List<VMenus> selectUpmsPermissionByUpmsRoleName(String roleName);
}