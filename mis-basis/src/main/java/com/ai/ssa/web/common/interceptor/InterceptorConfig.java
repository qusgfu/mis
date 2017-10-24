package com.ai.ssa.web.common.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

 

@Configuration
public class InterceptorConfig  extends WebMvcConfigurerAdapter{
	@Override
    public void addInterceptors(InterceptorRegistry registry) { 
        registry.addInterceptor(new UpmsInterceptor()).addPathPatterns("/**/*.do");  
        super.addInterceptors(registry);
    }
}
