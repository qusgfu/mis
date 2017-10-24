package com.ai.ssa.web.common.base;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.ai.ssa.web.common.model.umps.UpmsUser; 

/**
 * 控制器基类 Created by ZhangShuzheng on 2017/2/4.
 */
public abstract class BaseController {

	private final static Logger _log = LoggerFactory.getLogger(BaseController.class);
 
	/**
	 * 统一异常处理
	 * 
	 * @param request
	 * @param response
	 * @param exception
	 */
	@ExceptionHandler
	public BaseResult exceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception exception) {
		_log.error("统一异常处理：", exception);
		request.setAttribute("ex", exception);
		if (null != request.getHeader("X-Requested-With")
				&& request.getHeader("X-Requested-With").equalsIgnoreCase("XMLHttpRequest")) {
			request.setAttribute("requestHeader", "ajax");
		}
		BaseResult br = new BaseResult();
		br.setState(false);
		br.setMessage(exception.getClass().getName()+":"+exception.getMessage());
		br.setData(null);
		// shiro没有权限异常
		if (exception instanceof UnauthorizedException) {
			br.setData(101);
			br.setMessage("shiro没有权限异常");

		}
		// shiro会话已过期异常
		if (exception instanceof InvalidSessionException) {
			br.setData(102);
			br.setMessage("shiro会话已过期异常");
		}

		return br;
	}

	public HashMap getUploadFiles(HttpServletRequest request) {
		String ajaxUpdateResult = "";
		HashMap map = new HashMap<>();
		List fileList = new ArrayList();
		try {
			List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
			for (FileItem item : items) {
				if (!item.isFormField()) {
					//String fileName = item.getName();
					fileList.add(item);
				} else {
					map.put(item.getFieldName(), item.getString("UTF-8"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.put("up_fileList", fileList);

		return map;
	}

	public UpmsUser getLoginUser() {
		Subject subject = SecurityUtils.getSubject();
		UpmsUser user = (UpmsUser) subject.getPrincipal();
		return user;
	}

	public List<String> uploadFile(HttpServletRequest request) {
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		List<String> pathList = new ArrayList<String>();
		if (isMultipart) {
			String realPath = request.getSession().getServletContext().getRealPath("/");
			UpmsUser user = this.getLoginUser();
			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setSizeThreshold(1024 * 4);
			factory.setRepository(new File(realPath + File.separator + "uploadTemp"));
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setHeaderEncoding("UTF-8");
			upload.setSizeMax(1024 * 1024 * 100); // 100M
			List<FileItem> fileItems;
			HashMap uploadParamMap = new HashMap();
			try {
				fileItems = upload.parseRequest(request);
				Iterator<FileItem> iter = fileItems.iterator();
				while (iter.hasNext()) {
					FileItem item = (FileItem) iter.next();
					if (!item.isFormField()) {
						pathList.add(uploadFileByItem(item, realPath, user, uploadParamMap));
					} else {
						uploadParamMap.put(item.getFieldName(), item.getString());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return pathList;
	}

	private String uploadFileByItem(FileItem item, String realPath, UpmsUser user, HashMap uploadParamMap) {
		String uploadFilePath = "";
		try {

			// 如果item是文件上传表单域
			// 获得文件名及路径
			String fileName = item.getName();
			fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
			uploadParamMap.put("fileName", fileName);
			if (fileName != null) {
				String webPath = getUserWebPath();
				String rootPath = realPath + "/" + webPath;
				Date now = new Date();
				String realFileName = System.currentTimeMillis() + "_" + fileName;
				File fileOnServer = new File(rootPath + "/" + realFileName);
				if (!fileOnServer.exists()) {
					item.write(fileOnServer);
					// uploadFilePath = fileOnServer.getPath();
					uploadFilePath = webPath + "/" + realFileName;
					System.out.println("文件" + uploadFilePath + "上传成功");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			item.delete();
		}
		return uploadFilePath;
	}
	
	
	public String getServerUrl(HttpServletRequest req) {
	  String serverUrl="";
    try {
      URL url = new URL(req.getRequestURL().toString());
       serverUrl = url.getProtocol()+"://"+url.getHost()+":"+url.getPort();
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
    
      return serverUrl;
  }
	
	public String getUserWebPath() throws IOException {
		Date date = new Date();
		String result = "upload";
		File dir = new File(result);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return result;
	}

}
