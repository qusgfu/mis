package com.ai.ssa.web.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.util.WebUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class UpmsInterceptor  implements HandlerInterceptor  {
	  @Override
	    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
	            throws Exception {  
		 
			  response.setHeader("Pragma", "no-cache");
			  response.setHeader("Cache-Control", "no-cache");
			  response.setDateHeader("Expires", 0); 
	        return true;// 只有返回true才会继续向下执行，返回false取消当前请求
	    }

	    @Override
	    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
	            ModelAndView modelAndView) throws Exception {
	        
	    }

	    @Override
	    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
	            throws Exception {
	    }
}
