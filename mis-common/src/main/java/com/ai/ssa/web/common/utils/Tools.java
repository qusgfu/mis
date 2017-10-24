package com.ai.ssa.web.common.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;


public class Tools {

	public static boolean isNull(Object obj) {
		if (obj == null)
			return true;
		if (obj.toString().trim().equals(""))
			return true;
		return false;
	}

	public static int StrToInt(String str) {
		return new Integer(str).intValue();
	}

	public static List<String> arrayToList(String[] ar) {
		List<String> list = new ArrayList();
		for (String s : ar) {
			list.add(s);
		}
		return list;
	}

	public static void setFormBean(HttpServletRequest request, Object bean) {
		Method[] methods = bean.getClass().getMethods();
		for (int i = 0; i < methods.length; i++) {
			Method m = methods[i];
			setMethod(request, m, bean);
		}
	}

	public static void setMethod(HttpServletRequest request, Method m, Object bean) {
		String name = m.getName();
		if (isStartStr(name, "set")) {
			String paramName = getParamName(name);
			String param = request.getParameter(paramName);
			Object p = param;
			if (!Tools.isNull(param)) {
				try {
					String ptype = m.getParameterTypes()[0].getName();
					if (ptype.equals("int") || ptype.equals("java.lang.Integer")) {
						p = Tools.pareInt(param);
					}
					m.invoke(bean, new Object[] { p });
				} catch (Exception e) {
					System.out.println(m.getName() + "-----" + param);
					e.printStackTrace();
				}
			}
		}
	}

	public static Object getObjValue(Object bean, String param) {
		Method[] methods = bean.getClass().getMethods();
		Object result = null;
		for (int i = 0; i < methods.length; i++) {
			Method m = methods[i];
			String name = m.getName();
			String methodName = getMethodName("get", param);
			if (methodName.equals(name)) {
				try {
					result = m.invoke(bean);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	public static boolean isStartStr(String name, String str) {
		String a = name.substring(0, str.length());
		return a.equals(str);
	}

	public static String getParamName(String name) {
		String para = name.replaceFirst("set", "");
		String start = para.substring(0, 1);
		name = start.toLowerCase() + para.substring(1);
		return name;
	}

	public static String getMethodName(String method, String name) {
		String start = name.substring(0, 1);
		String methodName = method + start.toUpperCase() + name.substring(1);
		return methodName;
	}

	public static String getNowDate(String fmt) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(fmt);
		LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
		String dateStr = formatter.format(now);
		return dateStr;
	}

	public static String getMapsIds(List<HashMap> maps, String id) {
		String ids = "";
		for (int i = 0, len = maps.size(); i < len; i++) {
			if (i != 0)
				ids += ",";
			ids += maps.get(i).get(id);
		}
		return ids;
	}

	public static String setSqlParam(String sql, Object param, String addSql) {
		if (!Tools.isNull(param))
			sql += addSql;
		return sql;
	}

	public static String setSqlParam(String sql, boolean flag, String addSql) {
		if (flag)
			sql += addSql;
		return sql;
	}

	public static String setSqlParam(String sql, boolean flag, String addSql, String elseSql) {
		if (flag)
			sql += addSql;
		else
			sql += elseSql;
		return sql;
	}

	public static String getNowDate() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
		String dateStr = formatter.format(now);
		return dateStr;
	}

	public static Date strToDate(String dateStr, String fmt) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(fmt);
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		Date date = null;
		try {
			date = dateFormat.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static LocalDateTime strToLocalDateTime(String dateStr, String fmt) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(fmt);
		LocalDateTime date = null;
		date = LocalDateTime.from(formatter.parse(dateStr));
		return date;
	}

	public static java.sql.Date strToSqlDate(String dateStr, String fmt) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(fmt);
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		Date date = null;
		try {
			date = dateFormat.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		java.sql.Date sqlDate = new java.sql.Date(date.getTime());
		return sqlDate;
	}

	public static int getWeekOfDate(Date dt) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 7;
		return w;
	}

	public static String getDay(Date dt, int diff) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dt);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + diff);// 让日期加1
		String dateStr = dateToStr(calendar.getTime(), "yyyy-MM-dd");
		return dateStr;
	}

	public static String dateToStr(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		String dateStr = dateFormat.format(date);
		return dateStr;
	}

	public static String dateToStr(Date date, String fmt) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(fmt);
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		String dateStr = dateFormat.format(date);
		return dateStr;
	}

	public static String dateToStr(LocalDateTime date, String fmt) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(fmt);
		String dateStr = formatter.format(date);
		return dateStr;
	}

	public static String conactValue(List list, String key) {
		return conactValue(list, key, ",");
	}

	public static String conactValue(List list, String key, String tip) {
		HashMap tmp = new HashMap();
		StringBuffer s = new StringBuffer();
		int a = 0;
		for (int i = 0, len = list.size(); i < len; i++) {
			HashMap map = (HashMap) list.get(i);
			Object value = map.get(key);
			if (!Tools.isNull(value) && !tmp.containsKey(value)) {
				if (a != 0)
					s.append(tip);
				tmp.put(value, "");
				s.append(value);
				a++;
			}
		}
		return s.toString();
	}

	public static String nvl(Object str, String repStr) {
		if (isNull(str))
			return repStr;
		return str.toString();
	}

	public static String nvl(String str, String repStr) {
		if (isNull(str))
			return repStr;
		return str;
	}

	public static HashMap getMapByListName(List<HashMap> list, String name) {
		HashMap map = new HashMap();
		for (int i = 0; i < list.size(); i++) {
			HashMap m = list.get(i);
			String n = m.get(name).toString();
			map.put(n, m);
		}
		return map;
	}

	public static HashMap mapByType(List<HashMap> list, String itemKey, String itemKey2) {
		HashMap<String, HashMap> dataMap = new HashMap<>();
		for (int i = 0; i < list.size(); i++) {
			HashMap d = list.get(i);
			String key = d.get(itemKey).toString();
			HashMap l = dataMap.get(key);
			if (l == null) {
				l = new HashMap();
				dataMap.put(key, l);
			}
			l.put(d.get(itemKey2), d);
		}
		return dataMap;
	}

	public static HashMap getMapByData(HashMap dataMap, List resultList, String itemKey) {
		HashMap map = (HashMap) dataMap.get(itemKey);
		if (map == null) {
			map = new HashMap();
			dataMap.put(itemKey, map);
			resultList.add(map);
		}
		return map;
	}

	public static List getListByData(Map<String, List> dataMap, String itemKey) {
		List list = dataMap.get(itemKey);
		if (list == null) {
			list = new ArrayList<>();
			dataMap.put(itemKey, list);
		}
		return list;
	}

	public static List getListByMapName(HashMap<String, List> map, String name) {
		List list = map.get(name);
		if (list == null) {
			list = new ArrayList();
			map.put(name, list);
		}
		return list;
	}

	public static int getMaxDay(String month) {
		Date date = Tools.strToDate(month, "yyyy-MM");
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		return maxDay;
	}

	public static String readFile(File file) {
		BufferedReader reader = null;
		StringBuffer sb = new StringBuffer();
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "GBK"));

			String tempString = null;
			int line = 1;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				// 显示行号
				sb.append(tempString).append("\n");
				// System.out.println("line " + line + ": " + tempString);
				line++;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return sb.toString();
	}

	public static void wirteFile(File file, String content) throws Exception {
		OutputStreamWriter writerStream = new OutputStreamWriter(new FileOutputStream(file));
		BufferedWriter br = new BufferedWriter(writerStream);
		br.write(content);
		br.close();
		writerStream.close();

	}

	public static String getMd5(String str) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			byte b[] = md.digest();

			int i;

			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			// 32位加密
			return buf.toString();
			// 16位的加密
			// return buf.toString().substring(8, 24);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static String[] getMatchStrArr(String sql, String regex) {
		StringBuffer sb = new StringBuffer();
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(sql);
		int a = 0;
		while (matcher.find()) {
			if (a != 0)
				sb.append(";");
			sb.append(matcher.group(0));
			System.out.println(matcher.group(0));
			a++;
		}
		if (sb.length() > 0)
			return sb.toString().split(";");
		return null;
	}

	public static int getMonthDays(String month) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, Integer.parseInt(month.substring(0, 4)));
		cal.set(Calendar.MONTH, Integer.parseInt(month.substring(5, 7)) - 1);
		int day = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		return day;
	}

	public static HashMap ListToMap(List<HashMap> list, String key) {
		HashMap result = new HashMap();
		for (int i = 0; i < list.size(); i++) {
			HashMap m = list.get(i);
			Object mkey = getMapVal(m, key);
			result.put(mkey, m);
		}
		return result;
	}

	public static String getMapVal(HashMap m, String key) {
		Object o = m.get(key);
		if (o == null)
			return "";
		return o.toString();
	}

	public static String join(List<HashMap> list, String key) {

		StringBuffer o = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			HashMap t = list.get(i);
			if (i != 0)
				o.append(",");
			o.append(t.get(key));
		}

		return o.toString();
	}

	public static HashMap<String, String> newHashMap(String[] args) {
		HashMap<String, String> m = new HashMap();
		for (int i = 0, len = args.length; i < len; i++) {
			m.put(args[i], args[i + 1]);
			i++;
		}
		return m;
	}

	public static byte[] FileTobyte(String filePath) {
		byte[] buffer = null;
		try {
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] b = new byte[1024];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}

	public static Integer pareInt(String val) {
		return Integer.valueOf(val);
	}

	public static HashMap getParamMap(HttpServletRequest request) {
		Map<String, String[]> pm = request.getParameterMap();
		HashMap hm = new HashMap<>();
		for (String key : pm.keySet()) {
			hm.put(key, request.getParameter(key).toString());
		}
		return hm;
	}

	public static String imgToBase64(InputStream inputStream) {
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		byte[] data = null;
		Base64 base64 = new Base64();
		try {
			data = new byte[inputStream.available()];
			inputStream.read(data);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		String imgsrc = base64.encodeBase64String(data);
		return imgsrc;
	}

	public static byte[] getByteByFile(String filePath) {
		File file = new File(filePath);
		byte[] body = null;
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			body = new byte[is.available()];
			is.read(body);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return body;
	}
	
	public static boolean StrObjIsNull(Object obj) {
		if (obj == null)
			return true;
		if (obj.toString().trim().equals(""))
			return true;
		return false;
	}


}
