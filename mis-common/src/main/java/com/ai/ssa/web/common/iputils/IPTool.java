package com.ai.ssa.web.common.iputils;

import com.ai.ssa.web.common.utils.StringUtil;

public class IPTool  {
	public static String ip2area(String ipStr) {
		IPTool ipTest = new IPTool();
		String  path = StringUtil.getConfPath();
		
		System.out.println(path);
		// 指定纯真数据库的文件名，所在文件夹
		IPSeeker ip = new IPSeeker("QQWry.Dat", path);
		// 测试IP 58.20.43.13
//		System.out.println(path);
//		System.out.println(ip.getIPLocation("61.154.153.114").getCountry());
//		System.out.println(ip.getIPLocation("61.154.153.114").getArea());
//		System.out.println(ipStr);
		if("".equals(ipStr)){
			return "--";
		}else{
			return ip.getIPLocation(ipStr).getCountry();
		}
		
	}
	public static void main(String[] args) {
		System.out.println(IPTool.ip2area("61.154.153.225"));
	}
}
