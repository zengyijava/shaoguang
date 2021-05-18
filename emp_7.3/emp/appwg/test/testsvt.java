package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.appwg.initappplat.AppSdkPackage;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.tools.SysuserUtil;

public class testsvt extends HttpServlet
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1913511716031131913L;

	/**
	 * Constructor of the object.
	 */
	public testsvt()
	{
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy()
	{
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
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
		//IWgMwFileBiz fileBiz = new WgMwFileBiz();
		//WgMwCommuteBiz wgBiz = new WgMwCommuteBiz();
		
		//String filePath = "C:\\Users\\benwork\\Desktop\\temp\\1.jpg";
		
		//String res = fileBiz.uploadToMwFileSer(filePath, "10", "sz10000");

		//String url = "D:\\MyEclipse\\Tomcat1\\webapps\\p_appmage\\mcdownload1\\personal-file\\2014\\2014-07-04\\313f00dd197178e7af2d62fea2aa379d.jpg";
		//boolean res = fileBiz.downloadFromMwFileSer("C:\\Users\\benwork\\Desktop\\temp\\3.jpg", "mcdownload1/corp/sz10000/pic/origin/2014/2014-06-22/89e96fe5fb153bc411b1e16d340247eb.jpg");
		
		
		
		//boolean res = wgBiz.testSynuser();
		
		//boolean res = false;
		
		/*String jsonStr = readTxtFile("E:/temp/j.txt");
		System.out.println(jsonStr);
		int res = wgBiz.SendCSMsg(jsonStr);*/
		/*wgBiz.testSynuser();
		
		try
		{
			Thread.sleep(30*1000);
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		/*for(int i=0;i<100;i++){
		int res = AppSdkPackage.getInstance().testUpMsg("13682324521");
		}*/
		//boolean res2 = AppSdkPackage.getInstance().testSynuser();
		//1:离线，0在线
		AppSdkPackage.getInstance().testUpMsg("13482324521","222", String.valueOf(Math.random()),0);
		//203公众消息，207个人消息
		//AppSdkPackage.getInstance().testRptMsg("13682324521", 207);
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.print("a");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
	}

	public static String readTxtFile(String filePath)
	{
		InputStreamReader read = null;
		BufferedReader bufferedReader = null;
		try
		{
			String encoding = "gbk";
			File file = new File(filePath);
			if(file.isFile() && file.exists())
			{
				read = new InputStreamReader(new FileInputStream(file), encoding);
				bufferedReader = new BufferedReader(read);
				String txt = "";
				String lineTxt = null;
				while((lineTxt = bufferedReader.readLine()) != null)
				{
					txt += lineTxt;
				}
				read.close();
				return txt;
			}
			else
			{
				System.out.println("文件不存在");
				return "";
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error("出现异常");
			return "";
		}finally {
			SysuserUtil.closeStream(read);
			SysuserUtil.closeStream(bufferedReader);
		}

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
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.print("    This is ");
		out.print(this.getClass());
		out.println(", using the POST method");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException
	{
		// Put your code here
	}
public static void main(String[] args)
{
	/*String fileUrl = "http://192.168.0.63:8984/fileserver/";
	System.out.println(fileUrl.lastIndexOf("/"));
	System.out.println(fileUrl.length());
	if(fileUrl.lastIndexOf("/")+1 != fileUrl.length()){
		System.out.println("没");
	}else{
		System.out.println("最后有斜杠");
	}*/
	
	/*String a = "高岭\"土你你\n图我你摸";
	a = testsvt.string2Json(a);
	System.out.println(a);*/
	
	Date d = new Date();
	d.setTime(1406000445557l);
	System.out.println(d);
}

public static String string2Json(String s) {
    StringBuffer sb = new StringBuffer ();     
    for (int i=0; i<s.length(); i++) {    
   
        char c = s.charAt(i);     
        switch (c) {     
        case '\"':     
            sb.append("\\\"");     
            break;   
        case '\\':     
            sb.append("\\\\");
            break;     
        case '/':     
            sb.append("\\/");
            break;     
        case '\b':     
            sb.append("\\b");
            break;     
        case '\f':     
            sb.append("\\f");
            break;     
        case '\n':     
            sb.append("\\n");
            break;     
        case '\r':     
            sb.append("\\r");
            break;     
        case '\t':     
            sb.append("\\t");
            break;  
        default:     
            sb.append(c);     
        }
    }
    return sb.toString();     
 }

}
