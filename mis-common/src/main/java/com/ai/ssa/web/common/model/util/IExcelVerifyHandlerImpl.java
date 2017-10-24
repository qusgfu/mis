package com.ai.ssa.web.common.model.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jeecgframework.poi.excel.entity.result.ExcelVerifyHanlderResult;
import org.jeecgframework.poi.handler.inter.IExcelVerifyHandler;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Component;

@Component
public class IExcelVerifyHandlerImpl implements IExcelVerifyHandler {
	Logger log = LogManager.getLogger(IExcelVerifyHandlerImpl.class);
	@Resource
	private SqlSessionTemplate sql;

	@Override
	public ExcelVerifyHanlderResult verifyHandler(Object obj) {
		Map objectMap = new HashMap();
		try {
			objectMap = transforObjectToMap(obj);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String, String> regexs = getRegexs(obj);
		// List<String> fieldList = getClassFields(obj.getClass());

		ExcelVerifyHanlderResult result = new ExcelVerifyHanlderResult();
		result.setSuccess(true);
		StringBuilder sb = new StringBuilder();
		for (String fieldName : regexs.keySet()) {
			Pattern p = Pattern.compile((String) regexs.get(fieldName));
			Matcher matcher = p.matcher((CharSequence) objectMap.get(fieldName));
			if (!matcher.matches()) {
				log.info(fieldName+":"+objectMap.get(fieldName));
				result.setSuccess(false);
				sb.append(objectMap.get(fieldName) + "：格式错误，");
			}
		}
		if (!result.isSuccess()) {
			result.setMsg(sb.substring(0, sb.length() - 1) + "！");
		}

		return result;
	}

	/**
	 * 把javaBean对象转换为Map键值对
	 * 
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	private Map<String, String> transforObjectToMap(Object bean) throws Exception {
		Class<? extends Object> type = bean.getClass();
		Map<String, String> returnMap = new HashMap<String, String>();
		BeanInfo beanInfo = Introspector.getBeanInfo(type);
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (int i = 0; i < propertyDescriptors.length; i++) {
			PropertyDescriptor descriptor = propertyDescriptors[i];
			String propertyName = descriptor.getName();
			if (!propertyName.equals("class")) {
				Method readMethod = descriptor.getReadMethod();
				Object result = readMethod.invoke(bean, new Object[0]);
				if (result != null) {
					returnMap.put(propertyName, result.toString());
				}
			}
		}
		return returnMap;
	}

	/**
	 * 获取类对应的正则
	 * 
	 * @param bean
	 * @return
	 */
	public Map<String, String> getRegexs(Object bean) {
		String className = bean.getClass().getName();
		className = className.substring(className.lastIndexOf(".") + 1);
		Map<String, String> regex = sql.selectOne("assetMgmt.getRegexs", className);
		String name = (String) regex.get("name");
		String content = (String) regex.get("value");
		String[] kvrs = content.split("\\|\\|");
		Map<String, String> regexMap = new HashMap();
		if (kvrs != null && kvrs.length > 0) {
			for (String kvr : kvrs) {
				String[] kvrArra = kvr.split("::");
				if (kvrArra != null && kvrArra.length > 0) {
					regexMap.put(kvrArra[0], kvrArra[1]);
				}
			}
		}
		return regexMap;
	}

	/**
	 * 获取class的 包括父类的
	 * 
	 * @param clazz
	 * @return
	 */
	public static List<String> getClassFields(Class<?> clazz) {
		List<String> list = new ArrayList<String>();
		Field[] fields;
		fields = clazz.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			list.add(fields[i].getName());
		}
		list.remove("errorMsg");
		return list;
	}
}
