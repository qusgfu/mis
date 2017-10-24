package com.ai.ssa.web.service.api;

import java.util.List;
import java.util.Map;

/**
 * DE设备接口服务
 * 
 * @author zhaolinze
 *
 */
public interface DeepEdgeService {

	boolean check(Map headers);

	int getClientNum(Map headers);

	void registerClient(Map headers);

	void unregisterClient(Map headers);

	List<Map<String, Object>> getBlackIpList();

	void updateBlackIpStatus(List idList);

}
