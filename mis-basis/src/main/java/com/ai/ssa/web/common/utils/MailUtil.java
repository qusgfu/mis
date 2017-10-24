package com.ai.ssa.web.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
 

public class MailUtil {
	
	public static Email sendMail(Map<String, Object> mailInfo,List<Map<String, Object>> config) throws EmailException { 
		Map<String, String> mailConfig = _getMailConfig(config);
		 
		List<String> toUrlList = (List) mailInfo.get("toUrlList");
		String msg =   mailInfo.get("msg").toString();
		HtmlEmail builder = new HtmlEmail();
		builder.setCharset("UTF-8");
		builder.setHostName(mailConfig.get("mailhost"));
		builder.setSmtpPort(Integer.parseInt(mailConfig.get("mailport")));
		builder.setAuthentication(mailConfig.get("mailuser"), mailConfig.get("mailpwd"));
		builder.setFrom(mailConfig.get("mailuser"));
		builder.setSubject(String.valueOf(mailInfo.get("title")));
		
		for (String toUrl : toUrlList) {
			builder.addTo(toUrl);
		} 
		builder.setMsg(msg);
		builder.send();
		// String.format(format, args)
		return builder;

	}
	
	private static Map<String, String> _getMailConfig(List<Map<String, Object>> list) {

		HashMap<String, String> config = new HashMap<String, String>();
		for (Map<String, Object> item : list) {
			String name = String.valueOf(item.get("name"));
			String value = String.valueOf(item.get("value"));
			config.put(name, value);
		}
		return config;
	}
	
	 
}
