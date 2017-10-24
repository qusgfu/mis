package com.ai.ssa.web.common.utils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Component;

@Component
public class SolrUtil {
	private static String BASEURL;
	private static String url1, url2, url3;
	private static String collection1, collection2, collection3;
	private static String time;// 传入时间
	private static String collectTime;// solr时间格式
	private static Integer pageIndex;
	private static Integer pageSize;
	private static long old_total;
	private static String sort;
	private static String sort_key;
	private static String[] strs={"linux_20170805","AUSPUSH","linux_20170806","linux_20170807","linux_20170808","linux_20170801","linux_20170802","linux_20170803","linux_20170804","UnKnow_20170726","UnKnow_20170727","UnKnow_20170725","nginx_20170808","linux_20170816","nginx_20170807","linux_20170817","linux_20170818","linux_20170819","nginx_20170809","linux_20170812","linux_20170813","linux_20170814","linux_20170815","linux_20170810","nginx_20170802","nginx_20170801","linux_20170811","nginx_20170804","nginx_20170803","nginx_20170806","nginx_20170805","waf_20170805","waf_20170806","waf_20170807","waf_20170808","waf_20170801","waf_20170802","waf_20170803","waf_20170804","waf_20170809","linux_20170809","tomcat_20170823","tomcat_20170824","tomcat_20170825","tomcat_20170826","tomcat_20170827","tomcat_20170828","tomcat_20170829","tomcat_20170820","tomcat_20170821","tomcat_20170822","tomcat_20170713","tomcat_20170714","tomcat_20170715","tomcat_20170716","tomcat_20170717","tomcat_20170718","tomcat_20170719","tomcat_20170711","tomcat_20170712","tda_20170630","tomcat_20170801","tomcat_20170802","tomcat_20170803","cdn_20170712","tomcat_20170804","tomcat_20170805","tomcat_20170806","tomcat_20170807","tomcat_20170808","tda_20170628","tda_20170629","tda_20170626","tda_20170627","tomcat_20170809","tomcat_20170812","tomcat_20170813","tomcat_20170814","tomcat_20170815","tomcat_20170816","tomcat_20170817","tomcat_20170818","tomcat_20170819","tda_20170729","tomcat_20170810","tomcat_20170811","tda_20170730","tda_20170731","sw_20170628","waf_20170630","tda_20170718","tda_20170719","tda_20170727","tda_20170728","tda_20170725","tda_20170726","mysql_20170714","tda_20170723","juniper_20170731","mysql_20170713","tda_20170724","juniper_20170730","mysql_20170712","tda_20170721","tda_20170722","tda_20170720","tda_20170709","tda_20170707","tda_20170828","tda_20170708","tda_20170829","juniper_20170724","tda_20170716","juniper_20170723","tda_20170717","juniper_20170726","tda_20170714","tda_20170715","juniper_20170725","tda_20170712","juniper_20170720","tda_20170713","juniper_20170722","tda_20170710","tda_20170711","juniper_20170721","mysql_20170721","tda_20170830","juniper_20170728","juniper_20170727","juniper_20170729","tomcat_20170724","tomcat_20170725","tomcat_20170726","tomcat_20170727","tomcat_20170728","tomcat_20170729","linux_20170628","waf_20170731","juniper_20170630","tomcat_20170720","tda_20170819","tomcat_20170721","tomcat_20170722","tda_20170817","waf_20170730","tda_20170818","tomcat_20170723","tda_20170826","tda_20170705","tda_20170706","tda_20170827","tda_20170703","tda_20170824","tda_20170704","tda_20170825","tda_20170701","nginx_20170730","tda_20170822","tda_20170702","tda_20170823","tda_20170820","tda_20170821","nginx_20170731","tomcat_20170730","tomcat_20170731","tda_20170808","tda_20170809","tda_20170806","tda_20170807","tda_20170815","waf_20170629","tda_20170816","tda_20170813","tda_20170814","tda_20170811","waf_20170626","tda_20170812","waf_20170627","waf_20170628","tda_20170810","juniper_20170629","linux_20170728","nginx_20170719","linux_20170729","linux_20170724","linux_20170725","linux_20170726","linux_20170727","linux_20170720","waf_20170830","nginx_20170712","waf_20170710","nginx_20170711","linux_20170721","linux_20170722","waf_20170711","nginx_20170714","linux_20170723","waf_20170712","nginx_20170713","nginx_20170716","nginx_20170715","nginx_20170718","nginx_20170717","tda_20170804","waf_20170717","juniper_20170812","waf_20170718","tda_20170805","juniper_20170811","waf_20170719","tda_20170802","juniper_20170814","tda_20170803","juniper_20170813","waf_20170713","waf_20170714","tda_20170801","juniper_20170810","waf_20170715","waf_20170716","nginx_20170830","juniper_20170819","juniper_20170816","juniper_20170815","juniper_20170818","juniper_20170817","nginx_20170723","linux_20170731","waf_20170720","waf_20170721","nginx_20170722","nginx_20170725","waf_20170722","waf_20170723","nginx_20170724","nginx_20170727","nginx_20170726","nginx_20170729","nginx_20170728","linux_20170730","waf_20170728","juniper_20170801","waf_20170729","juniper_20170803","juniper_20170802","waf_20170724","waf_20170725","nginx_20170721","waf_20170726","nginx_20170720","waf_20170727","juniper_20170809","juniper_20170808","juniper_20170805","juniper_20170804","juniper_20170807","juniper_20170806","linux_20170706","nginx_20170819","linux_20170827","linux_20170707","nginx_20170818","linux_20170828","linux_20170708","linux_20170829","linux_20170709","linux_20170823","linux_20170824","linux_20170825","linux_20170705","linux_20170826","nginx_20170811","linux_20170820","nginx_20170810","waf_20170810","nginx_20170813","linux_20170821","waf_20170811","linux_20170822","nginx_20170812","nginx_20170815","nginx_20170814","nginx_20170817","nginx_20170816","juniper_20170713","waf_20170816","juniper_20170712","waf_20170817","juniper_20170715","waf_20170818","collection1","juniper_20170714","waf_20170819","waf_20170812","juniper_20170830","fw_20170627","fw_20170626","waf_20170813","waf_20170814","fw_20170629","juniper_20170711","fw_20170628","juniper_20170710","waf_20170815","juniper_20170717","juniper_20170716","juniper_20170719","juniper_20170718","linux_20170717","linux_20170718","nginx_20170829","linux_20170719","linux_20170713","linux_20170714","linux_20170715","linux_20170716","auscommon201708","linux_20170830","nginx_20170822","waf_20170820","nginx_20170821","linux_20170710","auscommon201706","linux_20170711","nginx_20170824","waf_20170821","auscommon201707","waf_20170701","linux_20170712","waf_20170822","nginx_20170823","nginx_20170826","nginx_20170825","nginx_20170828","nginx_20170827","juniper_20170702","waf_20170706","juniper_20170823","waf_20170827","juniper_20170701","juniper_20170822","waf_20170828","waf_20170707","waf_20170708","juniper_20170825","juniper_20170704","waf_20170829","waf_20170709","juniper_20170824","juniper_20170703","waf_20170702","waf_20170823","waf_20170703","waf_20170824","waf_20170704","waf_20170825","nginx_20170820","juniper_20170821","waf_20170705","juniper_20170820","waf_20170826","juniper_20170709","juniper_20170827","juniper_20170706","juniper_20170705","juniper_20170826","juniper_20170708","juniper_20170829","juniper_20170828","juniper_20170707"};
	private static String Zkurl;
	@Resource
	private SqlSessionTemplate sql;
	private static SqlSessionTemplate mysql;

	@PostConstruct
	public void init() {
		mysql = this.sql;
	}

	/**
	 * SolrTool.query:从solr查询数据，返回HashMap
	 * 
	 * @param pageIndex(当前页数1开始),pageSize(页面显示数量),time(day,week,month,时间段：2016-04-03/2017-04-06),sort(desc或asc,可为null),sort_key(排序值,可为null)--默认必备参数.其余如sourceType可以自行添加到map中传入,如果参数含有多个值，key使用other,value使用HashMap<String,String>，如[{"eventType","1,2,3"},{"eventGrade","2,4"}]
	 * @param HashMap(返回格式-total：总数，rowData：；列表展示数据)
	 * @throws IOException
	 * fqstr="-eventType_s:901 AND -dstIp_s:111.30.61.13 AND -srcIp_s:111.30.61.13 AND -msg_s:\"DNS response resolves to dead IP address\"";
	 */

	public static HashMap query(HashMap<String, Object> param) throws Exception {

		// setBaseUrl(servletContext);
		setSolrBaseUrl();
		HashMap result = new HashMap();
		HttpSolrServer solrCore;
		StringBuffer qStr = new StringBuffer();
		try {
			SolrQuery query = new SolrQuery();// 查询
			qStr.append("*:*");
			for (Map.Entry<String, Object> entry : param.entrySet()) {

				if (entry.getKey().equals("time")) {// 时间格式化
					time = (String) entry.getValue();
					if (time == null || "null/null".equals(time)) {
						time = "month";
					}
					setTime(time);
					qStr.append(" AND logTime_dt:" + collectTime);
				} else if (entry.getKey().equals("pageIndex")) {// 页面设置
					pageIndex = Integer.valueOf((String) entry.getValue());

				} else if (entry.getKey().equals("pageSize")) {// 页面大小设置
					pageSize = Integer.valueOf((String) entry.getValue());
				} else if (entry.getKey().equals("sort_key")) {// 排序值
					if ((String) entry.getValue() != null && !"".equals((String) entry.getValue())) {
						sort_key = (String) entry.getValue();
					}
				} else if (entry.getKey().equals("sort")) {
					sort = (String) entry.getValue();
				} else if (entry.getKey().equals("fqstr")) {
					query.setFilterQueries((String) entry.getValue());
				} else if (entry.getKey().equals("old_total")) {
					old_total = Long.valueOf((String) entry.getValue());
				} else if (entry.getKey().equals("other")) {// 数组类型参数

					HashMap<String, String> map = (HashMap<String, String>) entry.getValue();
					for (Map.Entry<String, String> entry2 : map.entrySet()) {

						String[] sourceStrArray = entry2.getValue().split(",");
						for (int i = 0; i < sourceStrArray.length; i++) {
							String sourceStr = "\"";
							sourceStr+=sourceStrArray[i]+"\"";
							if(i==0){	
								qStr.append(" AND (" + entry2.getKey() + ":" + sourceStr);
							}else{
								qStr.append(" OR " + entry2.getKey() + ":" + sourceStr);
							}
							
							if(i==sourceStrArray.length-1){
								qStr.append(" )");	
							}
						}

					}

				} else {// 其他必要查询参数
					if ((String) entry.getValue() != null && !"".equals(entry.getValue())) {
						qStr.append(" AND " + entry.getKey() + ":" + (String) entry.getValue());
					}
				}

			}

			if (sort_key != null && !"".equals(sort_key) && sort != null && !"".equals(sort)) {
				query.set("sort", sort_key + " " + sort);
			}

			Integer start = (pageIndex - 1) * pageSize;
			query.setStart(start);// 设置开始位数
			query.setRows(pageSize); // 设置个数

			query.setQuery(qStr.toString());
			

			
			


//			    url1="http://127.0.0.1:9191/solr/linux_20170805";
				solrCore = new HttpSolrServer(url1);

				
	

			System.out.println("query:" + query);
			SolrDocumentList docs = solrCore.query(query).getResults();

			long new_total= docs.getNumFound();

		    result.put("total", docs.getNumFound());

			
			List data = new ArrayList();
			for (SolrDocument sd : docs) {
				HashMap child = new HashMap();

				Collection<String> fieldNames = sd.getFieldNames();
				for (String fieldName : fieldNames) {

					if (fieldName.equals("logTime_dt")) {
//						SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss 'CST' yyyy",
//								Locale.US);
//						Date date = dateFormat.parse(sd.getFirstValue(fieldName).toString());
//						SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//						child.put("logTime_dt", dateFormat1.format(date));
						
						
						SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
						Date date = (Date) sdf.parse(sd.getFirstValue(fieldName).toString());	
						Calendar Cal=Calendar.getInstance();    
						Cal.setTime(date);    
						Cal.add(Calendar.HOUR_OF_DAY,-8);
						String formatStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Cal.getTime());
						child.put("logTime_dt", formatStr);
						
					} else {
						child.put(fieldName, sd.getFirstValue(fieldName).toString());
					}

				}

				data.add(child);
			}
			result.put("rowData", data);
			System.out.println(result);
			solrCore.shutdown();
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	
	/**
	 * SolrTool.cloudSolrQuery:从solr查询数据，返回HashMap，(集群版查询)
	 * 
	 * @param pageIndex(当前页数1开始),pageSize(页面显示数量),time(day,week,month,时间段：2016-04-03/2017-04-06),sort(desc或asc,可为null),sort_key(排序值,可为null)--默认必备参数.其余如sourceType可以自行添加到map中传入,如果参数含有多个值，key使用other,value使用HashMap<String,String>，如[{"eventType","1,2,3"},{"eventGrade","2,4"}]
	 * @param HashMap(返回格式-total：总数，rowData：；列表展示数据)
	 * fqstr="-eventType_s:901 AND -dstIp_s:111.30.61.13 AND -srcIp_s:111.30.61.13 AND -msg_s:\"DNS response resolves to dead IP address\"";
	 * @throws IOException
	 * 
	 */

	public static HashMap cloudSolrQuery(HashMap<String, Object> param) throws Exception {
 
		setZkurl();
    	CloudSolrClient solr = new CloudSolrClient(Zkurl);
		ModifiableSolrParams params = new ModifiableSolrParams();
		StringBuffer qStr = new StringBuffer();
		HashMap result = new HashMap();

		try {
			SolrQuery query = new SolrQuery();// 查询
			qStr.append("*:*");
			for (Map.Entry<String, Object> entry : param.entrySet()) {

				if (entry.getKey().equals("time")) {// 时间格式化
					time = (String) entry.getValue();
					if (time == null || "null/null".equals(time)) {
						time = "month";
					}
					setTime(time);
					qStr.append(" AND logTime_dt:" + collectTime);
				} else if (entry.getKey().equals("pageIndex")) {// 页面设置
					pageIndex = Integer.valueOf((String) entry.getValue());

				} else if (entry.getKey().equals("pageSize")) {// 页面大小设置
					pageSize = Integer.valueOf((String) entry.getValue());
				} else if (entry.getKey().equals("sort_key")) {// 排序值
					if ((String) entry.getValue() != null && !"".equals((String) entry.getValue())) {
						sort_key = (String) entry.getValue();
					}
				} else if (entry.getKey().equals("sort")) {
					sort = (String) entry.getValue();
				} else if (entry.getKey().equals("fqstr")) {
					query.setFilterQueries((String) entry.getValue());
				} else if (entry.getKey().equals("old_total")) {
					old_total = Long.valueOf((String) entry.getValue());
				} else if (entry.getKey().equals("other")) {// 数组类型参数

					HashMap<String, String> map = (HashMap<String, String>) entry.getValue();
					for (Map.Entry<String, String> entry2 : map.entrySet()) {

						String[] sourceStrArray = entry2.getValue().split(",");
						for (int i = 0; i < sourceStrArray.length; i++) {
							String sourceStr = "\"";
							sourceStr+=sourceStrArray[i]+"\"";
							if(i==0){	
								qStr.append(" AND (" + entry2.getKey() + ":" + sourceStr);
							}else{
								qStr.append(" OR " + entry2.getKey() + ":" + sourceStr);
							}
							
							if(i==sourceStrArray.length-1){
								qStr.append(" )");	
							}
						}

					}

				} else {// 其他必要查询参数
					if ((String) entry.getValue() != null && !"".equals(entry.getValue())) {
						qStr.append(" AND " + entry.getKey() + ":" + (String) entry.getValue());
					}
				}

			}

			if (sort_key != null && !"".equals(sort_key) && sort != null && !"".equals(sort)) {
				query.set("sort", sort_key + " " + sort);
			}

			params.set("defType", "edismax");
			Integer start =  (pageIndex - 1) * pageSize;
			// 设置开始位数
			params.set("start", start);
			// 设置个数
			params.set("rows", pageIndex);
			params.set("q", qStr.toString());
//			传入参数，声明忽略宕机的服务器，否则当集群中 的某一服务器宕机时，会查询报错
			params.set("shards.tolerant", true);
//			params.add(filterQuery);
			List<String> idxList=filterZkIndex(getZkIndex(),time);
			if (idxList.size()<1) {
				List noData = new ArrayList();
				result.put("total", 0);
				result.put("rowData", noData);
				return result;
			}else {
				String idxScope=idxList.toString();
				params.set("collection",idxScope.substring(1, idxScope.length()-1).replaceAll(",\\s+", ","));
			}
			
			QueryResponse response;
			response = solr.query(params);
			SolrDocumentList docs = response.getResults();
			long new_total= docs.getNumFound();
			if (new_total==0){
				List noData = new ArrayList();
				result.put("total", 0);
				result.put("rowData", noData);
				return result;
			}
			if(new_total>old_total){
				result.put("total",old_total);
			}else{
				result.put("total", docs.getNumFound());
			}
			
			List data = new ArrayList();			
			for (SolrDocument sd : docs) {
				HashMap child = new HashMap();

				Collection<String> fieldNames = sd.getFieldNames();
				for (String fieldName : fieldNames) {

					if (fieldName.equals("logTime_dt")) {
//						SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss 'CST' yyyy",
//								Locale.US);
//						Date date = dateFormat.parse(sd.getFirstValue(fieldName).toString());
//						SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//						child.put("logTime_dt", dateFormat1.format(date));
						
						
						SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
						Date date = (Date) sdf.parse(sd.getFirstValue(fieldName).toString());	
						Calendar Cal=Calendar.getInstance();    
						Cal.setTime(date);    
						Cal.add(Calendar.HOUR_OF_DAY,-8);
						String formatStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Cal.getTime());
						child.put("logTime_dt", formatStr);
						
					} else {
						child.put(fieldName, sd.getFirstValue(fieldName).toString());
					}

				}

				data.add(child);
			}
			result.put("rowData", data);
			solr.close();
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	
	
	public static HashMap queryByGroup(HashMap<String, Object> param) throws Exception {

		// setBaseUrl(servletContext);
//		setSolrBaseUrl();
		HashMap result = new HashMap();
		HttpSolrServer solrCore;
		String facet_field = null;
		StringBuffer qStr = new StringBuffer();
		try {
			SolrQuery query = new SolrQuery();// 查询
			qStr.append("*:*");
			for (Map.Entry<String, Object> entry : param.entrySet()) {

				if (entry.getKey().equals("time")) {// 时间格式化
					time = (String) entry.getValue();
					if (time == null || "null/null".equals(time)) {
						time = "month";
					}
					setTime(time);
					qStr.append(" AND collectTime:" + collectTime);
				} else if (entry.getKey().equals("pageIndex")) {// 页面设置
					pageIndex = Integer.valueOf((String) entry.getValue());

				} else if (entry.getKey().equals("pageSize")) {// 页面大小设置
					pageSize = Integer.valueOf((String) entry.getValue());
				} else if (entry.getKey().equals("sort_key")) {// 排序值
					if ((String) entry.getValue() != null && !"".equals((String) entry.getValue())) {
						sort_key = (String) entry.getValue();
					}
				} else if (entry.getKey().equals("sort")) {
					sort = (String) entry.getValue();
				} else if (entry.getKey().equals("facet.field")) {
					facet_field = (String) entry.getValue();
				} else if (entry.getKey().equals("other")) {// 数组类型参数

					HashMap<String, String> map = (HashMap<String, String>) entry.getValue();
					for (Map.Entry<String, String> entry2 : map.entrySet()) {

						String[] sourceStrArray = entry2.getValue().split(",");
						for (int i = 0; i < sourceStrArray.length; i++) {
							String sourceStr = sourceStrArray[i];
							qStr.append(" OR " + entry2.getKey() + ":" + sourceStr);
						}

					}

				} else {// 其他必要查询参数
					if ((String) entry.getValue() != null && !"".equals(entry.getValue())) {
						qStr.append(" AND " + entry.getKey() + ":" + (String) entry.getValue());
					}
				}

			}

			if (sort_key != null && !"".equals(sort_key) && sort != null && !"".equals(sort)) {
				query.set("sort", sort_key + " " + sort);
			}

			Integer start = (pageIndex - 1) * pageSize;
			query.setStart(start);// 设置开始位数
			query.setRows(pageSize); // 设置个数

			query.setQuery(qStr.toString());

			if (setUrl(time)) {
				// url1="http://192.168.1.192:8181/solr/collection1_20170415";
				// collection1="collection1_20170415";

				if (!getSolrStatus(url1)) {
					List noData = new ArrayList();
					result.put("total", 0);
					result.put("rowData", noData);
					return result;
				}

				solrCore = new HttpSolrServer(url1);
				String collection = null;

				if (getSolrStatus(url2)) {
					collection = collection1 + "," + collection2;

				}

				if (url3 != null && getSolrStatus(url3)) {
					collection = "," + collection3;
				}

				query.setParam("collection", collection);// 参数可以传多个string对象
			} else {
				solrCore = new HttpSolrServer(url1);
			}

			query.setParam("facet", "true");
			query.setParam("facet.sort", "count");
			query.setParam("facet.limit", pageSize.toString());
			query.setParam("facet.field", facet_field);

			System.out.println("query:" + query);

			List<Count> returnList = solrCore.query(query).getFacetField(facet_field).getValues();// 这里要和addFacetField相对应
			List data = new ArrayList();
			for (Count count : returnList) {
				HashMap child = new HashMap();
				if (count.getCount() > 0) {
					child.put("name", count.getName());
					child.put("count", (int) count.getCount());
					data.add(child);
				}

			}

			result.put("rowData", data);
			solrCore.shutdown();
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public static boolean setUrl(String time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());

		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));// 让日期加1
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		// int day =calendar.get(Calendar.DAY_OF_MONTH);

		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(new Date());

		if (time.equals("day")) {
			calendar2.set(Calendar.DAY_OF_MONTH, calendar2.get(Calendar.DAY_OF_MONTH));// 让日期加1
		} else if (time.equals("week")) {
			calendar2.set(Calendar.DAY_OF_MONTH, calendar2.get(Calendar.DAY_OF_MONTH) - 7);// 让日期加1
		} else if (time.equals("month")) {
			calendar2.set(Calendar.DAY_OF_MONTH, calendar2.get(Calendar.DAY_OF_MONTH) - 30);// 让日期加1
		} else {
			String[] sourceStrArray = time.split("/");
			String time1 = sourceStrArray[0];
			String time2 = sourceStrArray[1];
			String[] childStrArray = time1.split("-");
			String[] childStrArray2 = time2.split("-");
			url1 = BASEURL;
			url1 += childStrArray[0];
			url1 += childStrArray[1];
			url2 = BASEURL;
			url2 += childStrArray2[0];
			url2 += childStrArray2[1];
			String[] sourceStr = url1.split("/");
			collection1 = sourceStr[sourceStr.length - 1];

			String[] sourceStr2 = url2.split("/");
			collection2 = sourceStr2[sourceStr2.length - 1];

			if ((Integer.parseInt(childStrArray2[1]) - Integer.parseInt(childStrArray[1])) == 2) {
				url3 = BASEURL;
				url3 += childStrArray[0];
				url3 += Integer.parseInt(childStrArray2[1]) - 1;
				String[] sourceStr3 = url3.split("/");
				collection3 = sourceStr3[sourceStr3.length - 1];
			}

			if (!childStrArray[1].equals(childStrArray2[1])) {
				return true;
			} else {
				return false;
			}
		}

		int year2 = calendar2.get(Calendar.YEAR);
		int month2 = calendar2.get(Calendar.MONTH) + 1;
		// int day2 =calendar2.get(Calendar.DAY_OF_MONTH);

		url1 = BASEURL;
		url1 += year;
		if (month < 10) {
			url1 += "0";
			url1 += month;
		} else {
			url1 += month;
		}

		url1 += "/";

		System.out.println(url1);

		url2 = BASEURL;
		url2 += year2;
		if (month2 < 10) {
			url2 += "0";
			url2 += month2;
		} else {
			url2 += month2;
		}

		url2 += "/";

		System.out.println(url2);

		if ((month - month2) == 2) {
			int month3 = month - 1;
			url3 = BASEURL;
			url3 += year;
			if (month3 < 10) {
				url3 += "0";
				url3 += month3;
			} else {
				url3 += month3;
			}

			url3 += "/";

			System.out.println(url3);
			String[] sourceStrArray3 = url3.split("/");
			collection3 = sourceStrArray3[sourceStrArray3.length - 1];
		}

		String[] sourceStrArray = url1.split("/");
		collection1 = sourceStrArray[sourceStrArray.length - 1];

		String[] sourceStrArray2 = url2.split("/");
		collection2 = sourceStrArray2[sourceStrArray2.length - 1];

		if (month != month2) {
			return true;
		} else {
			return false;
		}

	}

	private static boolean getSolrStatus(String url) {
		SolrQuery query = new SolrQuery();// 查询
		query.setStart(0);// 设置开始位数
		query.setRows(5); // 设置个数
		query.setQuery("*:*");

		try {
			// QueryResponse response = server.query(params);
			HttpSolrServer solrCore = new HttpSolrServer(url);
			SolrDocumentList docs = solrCore.query(query).getResults();
			return true;
		} catch (Exception e) {
//			e.printStackTrace();
			return false;
		}

	}

	public static void setTime(String time) throws ParseException {

		// 获取当月第一天和最后一天
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cale = Calendar.getInstance();
		Calendar cale2 = Calendar.getInstance();

		if (time.equals("week")) {
			cale.set(Calendar.DAY_OF_MONTH, cale.get(Calendar.DAY_OF_MONTH) - 7);// 让日期加1
		} else if (time.equals("month")) {
			cale.set(Calendar.DAY_OF_MONTH, cale.get(Calendar.DAY_OF_MONTH) - 30);// 让日期加1
		}

		cale.set(Calendar.HOUR_OF_DAY, 0);
		cale.set(Calendar.MINUTE, 0);
		cale.set(Calendar.SECOND, 0);

		cale2.set(Calendar.HOUR_OF_DAY, 23);
		cale2.set(Calendar.MINUTE, 59);
		cale2.set(Calendar.SECOND, 59);
		// 获取前月的最后一天

		if (!time.equals("day") && !time.equals("week") && !time.equals("month")) {
			String[] sourceStrArray = time.split("/");
			String time1 = sourceStrArray[0];
			String time2 = sourceStrArray[1];
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date1 = sdf.parse(time1);
			Date date2 = sdf.parse(time2);
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			collectTime = "[" + sdf2.format(date1) + " TO " + sdf2.format(date2) + "]";
			System.out.println("collectTime:" + collectTime);
		} else {
			// System.out.println(cale.getTime());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			collectTime = "[" + sdf.format(cale.getTime()) + " TO " + sdf.format(cale2.getTime()) + "]";
			System.out.println("collectTime:" + collectTime);
		}

	}

	public static void setZkurl() {
		HashMap urlMap = mysql.selectOne("sysCfg.getZkUrl");
		if(!Tools.StrObjIsNull(urlMap.get("value"))){
			Zkurl = urlMap.get("value").toString();
		}else{
			Zkurl="192.168.1.190:2184,192.168.1.192:2184,192.168.1.193:2184";
		}
		
	}
	
	public static void setSolrBaseUrl() {
		HashMap urlMap = mysql.selectOne("sysCfg.getCveSolrUrl");
		if(!Tools.StrObjIsNull(urlMap.get("value"))){
			url1 = urlMap.get("value").toString();
		}else{
			url1="http://192.168.1.190:8181/solr/cnnvd_20170825";
		}
		
	}


	
	
	public static List<String> filterZkIndex(List<String> zkCnfIdx,String timeflag) {
		List<String> solrIdx=new ArrayList<String>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		if ("day".equals(timeflag)){
			cal.add(Calendar.DATE, -1);
		}else if("week".equals(timeflag)){
			cal.add(Calendar.DATE, -7);
		}else if("month".equals(timeflag)){
			cal.add(Calendar.DATE, -30);
		}else{
			if (timeflag.indexOf('/')>0){
				String[] timeScope=timeflag.split("/");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date1=null;
				try {
					date1 = sdf.parse(timeScope[0]);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				cal.setTime(date1);
				cal.add(Calendar.DATE, -1);
			}
		}
		
		
	
		Date idxTimestamp=null;
		Iterator<String> it = zkCnfIdx.iterator();
		while (it.hasNext()) {
			  String solrColl= (String) it.next();
			  String regEx="([0-9]{6,})";   
			  Pattern p = Pattern.compile(regEx);   
			  Matcher m = p.matcher(solrColl);
			  if (m.find()){
				  String idxTm=m.group(1);

				  SimpleDateFormat sdf=null;
				  if (idxTm.length()==6){
					  sdf = new SimpleDateFormat("yyyyMM");
				  }else if(idxTm.length()==8){
					  sdf = new SimpleDateFormat("yyyyMMdd");
				  }
				  try {
					  if (sdf!=null){
						idxTimestamp=sdf.parse(idxTm);
					  }
					  if (idxTimestamp!=null && idxTimestamp.compareTo(cal.getTime())>0 ){
						  solrIdx.add(solrColl);
					  }
					} catch (ParseException e) {
						return solrIdx;
				}				  
			  }
		}
		return solrIdx;
	}
	public static List<String> getZkIndex() {
		 // 创建一个与服务器的连接
		List<String> solrIdx=new ArrayList<String>();
		try{
	    	 
	    	 
			 ZooKeeper zk = new ZooKeeper(Zkurl,3000, new Watcher() { 
			            // 监控所有被触发的事件
			            public void process(WatchedEvent event) { 
			                System.out.println("已经触发了" + event.getType() + "事件！"); 
			            } 
			        }); 
			 // 取出子目录节点列表
			 solrIdx=zk.getChildren("",true);
			 zk.close(); 
		 System.out.println(solrIdx);
		}catch(Exception e){
			return solrIdx;
		} 
		return solrIdx;
	}


}
