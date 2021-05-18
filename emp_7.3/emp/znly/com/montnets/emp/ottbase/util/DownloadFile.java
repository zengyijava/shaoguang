package com.montnets.emp.ottbase.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
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
			
			EmpExecutionContext.error("字符集编码错误！");
			
		} catch (IOException e) {
			
			EmpExecutionContext.error("文件流输出异常！");
			
		} finally {
			if (fis!=null) try {fis.close();} catch(Exception e){EmpExecutionContext.error(e,"关闭文件输入流异常！");}
			if (os!=null) try {os.close();} catch(Exception e){EmpExecutionContext.error(e,"关闭文件输出流异常！");}
		}
	}
	
	/**
	 * 下载http地址文件
	 * @description    
	 * @param url  http文件地址
	 * @param projectpath 存放的项目绝对路径
	 * @return       success - 成功，error-失败			 
	 * @author linzhihan <zhihanking@163.com>
	 * @datetime 2013-12-17 下午03:43:19
	 */
	public String downloadFileFromHttp(String url,String projectpath)
    {
        // 文件夹目录
        // 文件http请求地址
        url = url.replace("%20", " ");//
        projectpath = projectpath.replace("%20", " ");
        File file = new File(projectpath);
        FileOutputStream os = null;
        BufferedInputStream bufferIs = null;
        try
        {
            
            String userImgDir = projectpath.substring(0, projectpath.lastIndexOf("/"));
            
            File uf = new File(userImgDir);
            // 没有路径时创建目录，防止上传文件失败
            if(!uf.exists())
            {
                uf.mkdirs();
            }
            
            URL urls = new URL(url);
            bufferIs = new BufferedInputStream(urls.openConnection().getInputStream());
            os = new FileOutputStream(file);
            byte buffer[] = new byte[4 * 1024];
            int size = 0;
            while((size = bufferIs.read(buffer)) != -1)
            {
                os.write(buffer, 0, size);
            }
            os.flush();
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "写文件路径异常。");
            return "error";
        }
        finally
        {
            try
            {
                if(null != os)
                {
                    os.close();
                }
                if(null != bufferIs)
                {
                    bufferIs.close();                    
                }
            }
            catch (Exception e)
            {
                EmpExecutionContext.error(e, "关闭文件流异常。");
            }
        }
        return "success";

    }
}
