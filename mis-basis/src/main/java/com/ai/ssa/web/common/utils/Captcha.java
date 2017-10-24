package com.ai.ssa.web.common.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream; 
import java.util.Random; 
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;

public class Captcha {
	public static Color getRandColor(int fc,int bc){
		Random random = new Random();
		if(fc>255) fc=255;
		if(bc>255) bc=255;
		int r=fc+random.nextInt(bc-fc);
		int g=fc+random.nextInt(bc-fc);
		int b=fc+random.nextInt(bc-fc);
		return new Color(r,g,b);
	 }
	
   public static String getPinImgBaseCode(HttpServletRequest request) throws IOException{
 
	   HttpSession session = request.getSession(); 
	   int width=78, height=40;
	   BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); 
	   Graphics g = image.getGraphics(); 
	   Random random = new Random(); 
	   g.setColor(new Color(4,19,40));
	   g.fillRect(0, 0, width, height); 
	   g.setFont(new Font("Times New Roman",Font.PLAIN,22));
	    
	   g.setColor(getRandColor(160,200));
	   for (int i=0;i<10;i++)
	   {
	   int x = random.nextInt(width);
	   int y = random.nextInt(height);
	   int xl = random.nextInt(12);
	   int yl = random.nextInt(12); 

	   g.drawLine(x,y,x+xl,y+yl);
	   } 
	   String sRand="";
	   String[] selectChar = {"2","3","4","5","6","7","8","9","B","D","F","G","J","M","W","S","X","Z"};
	   for (int i=0;i<4;i++){
	   String rand=selectChar[(random.nextInt(18))];
	   sRand+=rand; 
	   g.setColor(new Color(200+random.nextInt(55),200+random.nextInt(55),200+random.nextInt(55))); 
	   g.drawString(rand,17*i+5,30);
	   } 
	   session.setAttribute("rand",sRand); 
	  // System.out.println("------------rand="+sRand);

	   ByteArrayOutputStream bs = new ByteArrayOutputStream();

		// 创建编码对象
	   Base64 base64 = new Base64();  
	   ImageIO.write(image,"JPG",bs); 
	   String imgsrc = base64.encodeBase64String(bs.toByteArray()); 
	   bs.close();
	   return imgsrc;
   }
   
   
   private static String getPinImgBaseCode() throws IOException{
	    
	   int width=78, height=40;
	   BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); 
	   Graphics g = image.getGraphics(); 
	   Random random = new Random(); 
	   g.setColor(getRandColor(200,250));
	   g.fillRect(0, 0, width, height); 
	   g.setFont(new Font("Times New Roman",Font.PLAIN,22));
	    
	   g.setColor(getRandColor(160,200));
	   for (int i=0;i<10;i++)
	   {
	   int x = random.nextInt(width);
	   int y = random.nextInt(height);
	   int xl = random.nextInt(12);
	   int yl = random.nextInt(12); 

	   g.drawLine(x,y,x+xl,y+yl);
	   } 
	   String sRand="";
	   String[] selectChar = {"2","3","4","5","6","7","8","9","B","D","F","G","J","M","W","S","X","Z"};
	   for (int i=0;i<4;i++){
	   String rand=selectChar[(random.nextInt(18))];
	   sRand+=rand; 
	   g.setColor(new Color(20+random.nextInt(110),20+random.nextInt(110),20+random.nextInt(110))); 
	   g.drawString(rand,17*i+5,30);
	   }  
	 //  System.out.println("------------rand="+sRand);

	   ByteArrayOutputStream bs = new ByteArrayOutputStream();

		// 创建编码对象
	   Base64 base64 = new Base64();  
	   ImageIO.write(image,"JPG",bs); 
	   String imgsrc = base64.encodeBase64String(bs.toByteArray()); 
	   bs.close();
	   return imgsrc;
   }
   
   public static void main(String[] args) throws IOException {
	System.out.println(getPinImgBaseCode());
   }
}

