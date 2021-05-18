package com.montnets.emp.authen.servlet;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
/**
 * 生成验证码
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class YanzhengmaServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public YanzhengmaServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			doPost(request,response);
	}
	Color getColor(int a, int b) {
	  int n = b - a;
	  Random rd = new Random();
	  int cr = a + rd.nextInt(n);
	  int cg = a + rd.nextInt(n);
	  int cb = a + rd.nextInt(n);
	  return new Color(255, 255, 255);
	 }

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		try {			
		     response.setContentType("image/jpeg "); 
			 response.setHeader("Pragma", "No-cache");
			 response.setHeader("Cache-Control", "no-cache");
			 response.setDateHeader("Expires", 0);

			 int width = 106, height = 40;
			 BufferedImage image = new BufferedImage(width, height,
			   BufferedImage.TYPE_INT_RGB);

			 Graphics g = image.getGraphics();

			 Random random = new Random();

			 g.setColor(new Color(255, 255, 255));
			 g.fillRect(0, 0, 106, 40);

			 //g.setFont(new Font("Arial", Font.PLAIN, 5));
			 g.setColor(new Color(189, 189, 189));
			 g.drawRect(0, 0, 105, 39);

			 
			 for (int i = 0; i <6; i++) {
				 if(i%2==0){
					 g.setColor(new Color(61,61, 61));
				 }else{
					 g.setColor(new Color(0,0, 0));
				 }
			  int x = random.nextInt(width);
			  int y = random.nextInt(height);
			  int xl = random.nextInt(40);
			  int yl = random.nextInt(40);
			  g.drawLine(x, y, x + xl, y + yl);
			 }
			 for (int i = 0; i <30; i++) {
				 g.setColor(new Color(61,61, 61));
				  int x = random.nextInt(width);
				  int y = random.nextInt(height);
				  int xl = random.nextInt(2);
				  int yl = random.nextInt(2);
				  g.drawLine(x, y, x + xl, y + yl);
				 }
             //产生一个四位验证码
			 String randStr = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
			 int len = randStr.length();
			 String sRand = "";
			 for (int i = 0; i < 4; i++) {
			  String rand = String.valueOf(randStr.charAt(random.nextInt(len)));
			  sRand += rand;
			  g.setColor(new Color(0, 0, 0));
			  g.setFont(new Font("Segoe Print", Font.ITALIC, 35));
			  g.drawString(rand, 20 * i + 13, 34);
			 }
             //将产生的验证码存入session中
			 request.getSession(true).setAttribute("yzm", sRand);
			 g.dispose();
			  try{
				 // ImageIO.write(image, "jpeg", response.getOutputStream());
				  
				  JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(response.getOutputStream());				  
				  encoder.encode(image); 

			  }
			  catch(Exception e){
				  EmpExecutionContext.error(e,"输出验证码图片异常！");
			  }
			  
		} catch (Exception e) {
			EmpExecutionContext.error(e,"获取验证码异常！");
		}
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
