package com.ai.ssa.web.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.util.Base64Utils;

import com.ai.ssa.web.common.constant.SFTPConstants;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;


public class SftpUtil {
	private static Logger logger = LogManager.getLogger(SftpUtil.class.getName());
	 
	
	@SuppressWarnings({"unchecked","rawtypes"})
	public static Map getSftpParams(SqlSessionTemplate sql){
		List<HashMap> list = (List) sql.selectList("common.getSysCfg", "INFOCENTER_REPOSITORY");
		Map map = new HashMap();
		if(list!=null&&list.size()>0) {
			for(Map m : list){
				String name = (String) m.get("name");
				String value = (String) m.get("value");
				if(name.equals("password"))value = AESUtil.AESDecode(value);
				map.put(name.toLowerCase(), value); 
			}
		}
		return map;
	}
	
	public static Map getSftpChannel(Map<String, String> sftpDetails, int timeout, Channel channel, Session session) throws JSchException {
		String ftpHost = sftpDetails.get(SFTPConstants.SFTP_REQ_HOST);
		String port = sftpDetails.get(SFTPConstants.SFTP_REQ_PORT);
		String ftpUserName = sftpDetails.get(SFTPConstants.SFTP_REQ_USERNAME);
		String ftpPassword = sftpDetails.get(SFTPConstants.SFTP_REQ_PASSWORD); 
		int ftpPort = SFTPConstants.SFTP_DEFAULT_PORT;
		if (port != null && !port.equals("")) {
			ftpPort = Integer.valueOf(port);
		}

		JSch jsch = new JSch(); // 创建JSch对象
		session = jsch.getSession(ftpUserName, ftpHost, ftpPort); // 根据用户名，主机ip，端口获取一个Session对象
		logger.info("Session created.");
		if (ftpPassword != null) {
			session.setPassword(ftpPassword); // 设置密码
		}
		Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config); // 为Session对象设置properties
		session.setTimeout(timeout); // 设置timeout时间
		session.connect(); // 通过Session建立链接
		logger.info("Session connected.");

		logger.info("Opening Channel.");
		channel = session.openChannel("sftp"); // 打开SFTP通道
		channel.connect(); // 建立SFTP通道的连接
		logger.info("Connected successfully to ftpHost = " + ftpHost + ",as ftpUserName = " + ftpUserName
				+ ", returning: " + channel);
		Map map = new HashMap();
		map.put("session", session);
		map.put("channel", channel);
		return map;
	}
	public static boolean uploadFile(Map<String, String> sftpDetails,  String filePath, String fileName, InputStream is){
		return uploadFile(sftpDetails, 6000, filePath, fileName, is);
	}
	
	
	public static boolean uploadFile(Map<String, String> sftpDetails, int timeout, String filePath, String fileName, InputStream is)
		{
		Session session = null;
		Channel channel = null;
		
		boolean flag = true;
		try {
			
			Map map = getSftpChannel(sftpDetails, timeout, channel, session);
			channel = (Channel) map.get("channel");
			session = (Session) map.get("session");
			ChannelSftp sftp = (ChannelSftp) channel;
			try {
				sftp.cd(filePath);
			} catch (Exception e) {
				sftp.cd(filePath.substring(0, filePath.lastIndexOf("/")+1));
				sftp.mkdir(filePath.substring(filePath.lastIndexOf("/")+1));
			} 
			sftp.put(is, filePath+"/"+fileName, ChannelSftp.OVERWRITE);
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (channel != null) {
				channel.disconnect();
			}
			if (session != null) {
				session.disconnect();
			}
		}
		return flag;

	}

	public static byte[] downloadFile(Map<String, String> sftpDetails, int timeout, String filePath) throws Exception {
		Session session = null;
		Channel channel = null;
		Map map = getSftpChannel(sftpDetails, timeout, channel, session);
		channel = (Channel) map.get("channel");
		session = (Session) map.get("session");

		InputStream is = null;
		byte[] result = null;
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		byte[] buffer = new byte[4096];
		int n = 0;
		try {
			is = ((ChannelSftp) channel).get(filePath);
			while (-1 != (n = is.read(buffer))) {
				output.write(buffer, 0, n);
			}
			result = output.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (is != null) {
				is.close();
			}
			if (channel != null) {
				channel.disconnect();
			}
			if (session != null) {
				session.disconnect();
			}
		}
		return result;
	}

	public static String getFileName() {
		// 取当前时间的长整形值包含毫秒
		long millis = System.currentTimeMillis();
		// long millis = System.nanoTime();
		// 加上三位随机数
		Random random = new Random();
		int end3 = random.nextInt(999);
		// 如果不足三位前面补0
		String str = millis + String.format("%03d", end3);

		return str;
	}
}
