package com.ai.ssa.web.service.api;

import java.util.Map;

import com.ai.ssa.web.common.base.BaseResult;
import com.ai.ssa.web.common.model.umps.UpmsPlugin;
import com.ai.ssa.web.common.model.umps.UpmsPluginExample;
import com.ai.ssa.web.common.utils.PropertiesFileUtil;

public interface UpmsPluginService extends BaseService<UpmsPlugin, UpmsPluginExample>{
	//public list<Plugin> getPluginList();
	int insert(Map record);
	UpmsPlugin selectByPluginId(UpmsPlugin pluginId);
	int insertPermission(UpmsPlugin record);
	int deleteByPrimaryKey(Map record);
    int deletePermissionByPlugin(Map params);
    BaseResult installPlugin(Map params);
}
