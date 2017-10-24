package com.ai.ssa.web.common.properties;

import java.io.Serializable;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
@Component 
@ConfigurationProperties(prefix="upms")
public class UpmsProperties  extends UpmsPublicProperties implements Serializable{ 
     private String appId;
     private String type;
     private String ssoServerUrl;
     private String httpProtocol;
    public String getAppId() {
      return appId;
    }

    public void setAppId(String appId) {
      this.appId = appId;
    }

    public String getType() {
      return type;
    }

    public void setType(String type) {
      this.type = type;
    }

    public String getSsoServerUrl() {
      return ssoServerUrl;
    }

    public void setSsoServerUrl(String ssoServerUrl) {
      this.ssoServerUrl = ssoServerUrl;
    }

	public String getHttpProtocol() {
		return httpProtocol;
	}

	public void setHttpProtocol(String httpProtocol) {
		this.httpProtocol = httpProtocol;
	}

   

    
     
    
}
