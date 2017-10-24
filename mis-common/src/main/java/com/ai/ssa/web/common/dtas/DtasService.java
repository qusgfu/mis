package com.ai.ssa.web.common.dtas;

import java.io.File;
import java.io.InputStream;

import org.apache.http.Header;
import org.apache.http.HttpResponse;

public interface DtasService {
	// 测试
	String test() throws Exception;

	// 注册
	String register() throws Exception;

	// 注销
	String unregister() throws Exception;

	// 检查是否复样
	String checkDuplicate(String fileSHA1) throws Exception;

	// 上传样本

	String upload(String APIKey, InputStream inputStream, String fileSHA1, String fileType) throws Exception;

	// 获取报告
	String getReport() throws Exception;

}
