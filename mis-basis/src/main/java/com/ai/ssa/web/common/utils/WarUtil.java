package com.ai.ssa.web.common.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream; 

public class WarUtil {
	 public static void main(String[] args) throws Exception {
	        try {  
	              // readZipFile("D:\\myWork\\SSA\\com.ai.ssa.web\\com.ai.ssa.web.demo\\target\\demo-1.0.0.war");  
	           } catch (Exception e) {  
	               // TODO Auto-generated catch block  
	               e.printStackTrace();  
	           }  
	    }
	    
	    public static Properties readZipFile(String zipfile,String readFile) throws Exception {  
	           ZipFile zf = new ZipFile(zipfile);  
	           InputStream in = new BufferedInputStream(new FileInputStream(zipfile));  
	           ZipInputStream zin = new ZipInputStream(in);  
	           ZipEntry ze;  
	           Properties prop = new Properties();  
	           while ((ze = zin.getNextEntry()) != null) {  
	               if (ze.isDirectory()) {
	            	   
	               } else {  
	                   long size = ze.getSize();   
	                   if (ze.getName().equals(readFile)) {  
	                       prop.load(new InputStreamReader(zf.getInputStream(ze),"UTF-8")); 
	                   }   
	               }  
	           }  
	           zin.closeEntry(); 
	           return prop;
	       }  
	    
//	    public static Properties readZipFile(FileInputStream zipfile,String readFile) throws Exception {  
//	      File file = new File(zipfile);
//          ZipFile zf = new ZipFile(zipfile);  
//          InputStream in = new BufferedInputStream(zipfile);  
//          ZipInputStream zin = new ZipInputStream(in);  
//          ZipEntry ze;  
//          Properties prop = new Properties();  
//          while ((ze = zin.getNextEntry()) != null) {  
//              if (ze.isDirectory()) {
//                  
//              } else {  
//                  long size = ze.getSize();   
//                  if (ze.getName().equals(readFile)) {  
//                      prop.load(new InputStreamReader(new ZipFileInputStream(ze),"UTF-8")); 
//                  }   
//              }  
//          }  
//          zin.closeEntry(); 
//          return prop;
//      } 
	    
}
