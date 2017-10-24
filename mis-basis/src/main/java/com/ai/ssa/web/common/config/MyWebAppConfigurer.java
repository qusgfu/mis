package com.ai.ssa.web.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
@Configuration
public class MyWebAppConfigurer  extends WebMvcConfigurerAdapter {
	  @Override
	    public void addResourceHandlers(ResourceHandlerRegistry registry) {
	        registry.addResourceHandler("/favicon.ico").addResourceLocations("/favicon.ico");
	        super.addResourceHandlers(registry);
	    }

}

