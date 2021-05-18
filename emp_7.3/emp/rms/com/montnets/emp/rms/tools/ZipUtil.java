package com.montnets.emp.rms.tools;

import com.montnets.emp.common.context.EmpExecutionContext;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import java.io.*;
import java.util.Enumeration;

/** 
 *  解压Zip文件工具类 
 * @author zhangyongbo 
 * 
 */  
public class ZipUtil
{
    private static final int buffer = 2048;  
    
  /** 
   * 解压Zip文件 
   * @param path 文件目录 
   */  
  public static void unZip(String path)  
      {  
       int count = -1;  
       String savepath = "";  

       File file = null;  
       InputStream is = null;  
       FileOutputStream fos = null;  
       BufferedOutputStream bos = null;  
       savepath = path.substring(0, path.lastIndexOf("/")) + "/"; //保存解压文件目录  
       new File(savepath).mkdir(); //创建保存目录  
       ZipFile zipFile = null;  
       try  
       {  
           zipFile = new ZipFile(path,"gbk"); //解决中文乱码问题  
           Enumeration<?> entries = zipFile.getEntries();  

           while(entries.hasMoreElements())  
           {  
               byte buf[] = new byte[buffer];  

               ZipEntry entry = (ZipEntry)entries.nextElement();  

               String filename = entry.getName();  
               boolean ismkdir = false;  
               if(filename.lastIndexOf("\\") != -1){ //检查此文件是否带有文件夹  
                  ismkdir = true;  
               }  
               filename = savepath + filename;  

               if(entry.isDirectory()){ //如果是文件夹先创建  
                  file = new File(filename);  
                  file.mkdirs();  
                   continue;  
               }  
               file = new File(filename);  
               if(!file.exists()){ //如果是目录先创建  
                  if(ismkdir){  
                  new File(filename.substring(0, filename.lastIndexOf("\\"))).mkdirs(); //目录先创建  
                  }  
               }
               boolean flag = file.createNewFile();
               if (!flag) {
                   EmpExecutionContext.error("创建文件失败！");
               }
               
               try{
	               is = zipFile.getInputStream(entry);  
	               fos = new FileOutputStream(file);  
	               bos = new BufferedOutputStream(fos, buffer);  
	
	               while((count = is.read(buf)) > -1)  
	               {  
	                   bos.write(buf, 0, count);  
	               }  
	               bos.flush();  
	               bos.close();  
	               fos.close();
	               is.close();
               }finally{
            	   try{ 
	                   if(bos != null){  
	                       bos.close();  
	                   }  
	                   if(fos != null) {  
	                       fos.close();  
	                   }  
	                   if(is != null){  
	                       is.close();  
	                   } 
                   }catch(Exception e) {  
                 	  EmpExecutionContext.error(e,"IO流关闭异常"); 
                   }  
               }
           }  

       }catch(IOException e){  
          EmpExecutionContext.error(e,"压缩文件IO流关闭异常");
       }finally{  
              try{  
              if(bos != null){  
                  bos.close();  
              }  
              if(fos != null) {  
                  fos.close();  
              }  
              if(is != null){  
                  is.close();  
              }  
              if(zipFile != null){  
                  zipFile.close();  
              }  
              }catch(Exception e) {  
            	  EmpExecutionContext.error(e,"压缩文件IO流关闭异常"); 
              }  
          }  
      }  

 

public static void main1(String[] args)  
    {  
        unZip("/D:/apache-tomcat-6.0.35(8080)/webapps/p_rms/file/rms/templates/100001/582/fuxin.zip"); 
        String f = "D:\\apache-tomcat-6.0.35(8080)\\webapps\\p_rms\\file\\rms\\templates\\100001\\582\\";
        File file = new File(f);
        String[] test=file.list();
        for(int i=0;i<test.length;i++){
            System.out.println(test[i]);
        }
        
        System.out.println("------------------");
        
        String fileName = "";
        
        File[] tempList = file.listFiles();
        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
                System.out.println("文     件："+tempList[i]);
                
                fileName = tempList[i].getName();
                
                System.out.println("文件名："+fileName);
            }
            if (tempList[i].isDirectory()) {
                System.out.println("文件夹："+tempList[i]);
            }
        }
    } 

public static void main(String[] args) {
	//File f = new File("E:/aliyun/fuxin.zip");
	new ZipUtil().unZip("D:/Program Files/Tomcat-6.0.35/webapps/p_rms7.1/file/rms/templates/100000/1063.zip");
	//System.out.println(f.isFile());
}
}
