package com.ai.ssa.web.common.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Component;

import com.ai.ssa.web.common.bean.Cache;
import com.ai.ssa.web.common.bean.CacheConstant;

/**
 * 
 * 缓存工具类
 *
 */
@Component
public class CacheUtils {

	private static HashMap cacheMap = new HashMap();

	private CacheUtils() {

	}

	/**
	 * 更加key 判断数据是否缓存
	 * 
	 * @param key
	 * @return
	 */
	public static boolean isCached(String key) {
		return cacheMap.containsKey(key);
	}

	/**
	 * 设置缓存信息
	 * 
	 * @param key
	 *            //唯一标识
	 * @param cache
	 */
	public synchronized static void putCache(String key, Object cache) {
		cacheMap.put(key, cache);
	}

	/**
	 * 清除指定缓存
	 * 
	 * @param key
	 */
	public synchronized static void clearCache(String key) {
		cacheMap.remove(key);
	}

	/**
	 * 清除所有缓存
	 */
	public synchronized static void clearAll() {
		cacheMap.clear();

	}

	/**
	 * 获取缓存信息
	 */

	public static Object getCache(String key) {

		return cacheMap.get(key);
	}

	public static HashMap getAreaById(String areaId) {
		HashMap areaData = (HashMap) CacheUtils.getCache(CacheConstant.SYS_AREA);
		Cache areaCache = (Cache) areaData.get(areaId);
		HashMap area = (HashMap) areaCache.getData();
		return area;
	}

	public static HashMap getEventGradeByGradeId(String gradeId) {
		HashMap eventGradeData = (HashMap) CacheUtils.getCache(CacheConstant.EVENT_GRADE);
		Cache eventGradeCache = (Cache) eventGradeData.get(gradeId);
		HashMap eventGrade = (HashMap) eventGradeCache.getData();
		return eventGrade;
	}

	public static HashMap getEventThreatTypeByThreatType(String threatType) {
		HashMap eventThreatTypeData = (HashMap) CacheUtils.getCache(CacheConstant.EVENT_THREAT_TYPE);
		Cache eventThreatTypeCache = (Cache) eventThreatTypeData.get(threatType);
		HashMap eventThreatType = (HashMap) eventThreatTypeCache.getData();
		return eventThreatType;
	}

	public static HashMap getEventTypeById(String id) {
		HashMap eventTypeData = (HashMap) CacheUtils.getCache(CacheConstant.EVENT_TYPE);
		Cache eventTypeCache = (Cache) eventTypeData.get(id);
		HashMap eventType = (HashMap) eventTypeCache.getData();
		return eventType;

	}

	public static HashMap getSysAssetByIpAndOrg(String ipOrg) {
		HashMap assetData = (HashMap) CacheUtils.getCache(CacheConstant.SYS_ASSET);
		Cache assetCache = (Cache) assetData.get(ipOrg);
		HashMap asset = (HashMap) assetCache.getData();
		return assetData;
	}

	public static HashMap getProductTypeById(String productType) {
		HashMap productTypeData = (HashMap) CacheUtils.getCache(CacheConstant.PRODUCT_TYPE);
		Cache productTypeOne = (Cache) productTypeData.get(productType);
		HashMap pt = (HashMap) productTypeOne.getData();
		return pt;
	}

	public static void reloadSysAsset(List<HashMap> list) {
		HashMap data = new HashMap<>();
		for (HashMap asset : list) {
			if (!Tools.isNull(asset.get("ip")) && !Tools.isNull(asset.get("org_id"))) {
				Cache assetCache = new Cache(asset.get("ip").toString() + "|" + asset.get("org_id").toString(), asset);
				data.put(assetCache.getKey(), assetCache);
			}

		}
		CacheUtils.putCache(CacheConstant.SYS_ASSET, data);

	}

}
