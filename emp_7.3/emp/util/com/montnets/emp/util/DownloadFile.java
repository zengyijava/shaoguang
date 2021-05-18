package com.montnets.emp.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.context.EmpExecutionContext;

/**
 * 文件下载
 * @author Administrator
 *
 */
public class DownloadFile {
	/**
	   * @description：下载文件
	   *@param filePath 下载文件全路径
	   * @param fileName 下载文件文件名
	   * @throws Exception
	   * @return void
	     */

	public void downFile(HttpServletRequest request,
			HttpServletResponse response, String filePath, String fileName) {
		
		OutputStream os = null;
		FileInputStream fis = null;
		
		try {
			if (response != null) {
				
				response.reset();							
				String utf8title = URLEncoder.encode(fileName, "UTF-8");				
				response.setHeader("Content-Disposition", "attachment; filename="+ utf8title);// 设定输出文件头				
				response.setContentType("application/vnd.ms-excel");	// 定义输出类型
				os = response.getOutputStream();
				fis = new FileInputStream(filePath);
				byte[] b = new byte[1024];
				int i = 0;
				while ((i = fis.read(b)) > 0) {
					os.write(b, 0, i);
				}
				os.flush();
			}
			
		} catch (UnsupportedEncodingException e) {
			
			EmpExecutionContext.error(e, "字符集编码错误！");
			
		} catch (IOException e) {
			
			EmpExecutionContext.error(e, "文件流输出异常！");
			
		} finally {
			if (fis!=null) try {fis.close();} catch(Exception e){EmpExecutionContext.error(e, "关闭文件输入流异常！");}
			if (os!=null) try {os.close();} catch(Exception e){EmpExecutionContext.error(e, "关闭文件输出流异常！");}
		}
	}
}
