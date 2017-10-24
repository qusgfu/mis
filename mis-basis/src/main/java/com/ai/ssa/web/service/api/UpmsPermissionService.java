package com.ai.ssa.web.service.api;

import com.ai.ssa.web.common.model.umps.UpmsPermission;
import com.ai.ssa.web.common.model.umps.UpmsPermissionExample;
import com.alibaba.fastjson.JSONArray;

/**
* UpmsPermissionService接口
*/
public interface UpmsPermissionService extends BaseService<UpmsPermission, UpmsPermissionExample> {

    JSONArray getTreeByRoleId(Integer roleId);

    JSONArray getTreeByUserId(Integer usereId, Byte type);

}