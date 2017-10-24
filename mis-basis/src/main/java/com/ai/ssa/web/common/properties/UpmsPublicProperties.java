package com.ai.ssa.web.common.properties;

import java.io.Serializable;

import org.springframework.boot.context.properties.ConfigurationProperties;
@ConfigurationProperties(prefix="upms")
public class UpmsPublicProperties implements Serializable{  
    private static String loginUrl = "/sso/login.do";   
    private static Integer sessionTimeout ;
	
	public Integer getSessionTimeout() {
		return sessionTimeout;
	}

	public void setSessionTimeout(Integer sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}

	 

	public String getLoginUrl() {
		return loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}
 
 

	 
	 
    
    
}
