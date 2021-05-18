package com.montnets.emp.qyll.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import com.montnets.emp.common.context.EmpExecutionContext;

public class ExcelExportUtil {
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/** 
     * 将多个Excel打包成zip文件 
     * @param srcfile 
     * @param zipfile 
     */  
    public boolean zipFiles(List<File> srcfile, File zipfile) {    
        byte[] buf = new byte[1024];    
        try {    
            // Create the ZIP file    
        	FileOutputStream fos = new FileOutputStream(zipfile);
            ZipOutputStream out = new ZipOutputStream(fos);    
            // Compress the files    
            for (int i = 0; i < srcfile.size(); i++) { 
            	FileInputStream in = null;
            	try{
	                File file = srcfile.get(i);    
	                in = new FileInputStream(file);    
	                // Add ZIP entry to output stream.    
	                out.putNextEntry(new ZipEntry(file.getName()));    
	                // Transfer bytes from the file to the ZIP file    
	                int len;    
	                while ((len = in.read(buf)) > 0) {    
	                    out.write(buf, 0, len);    
	                }    
	                // Complete the entry    
//	                out.closeEntry();    
//	                in.close();
            	}finally{
            		if(null != fos){
            			fos.close();
            		}
            		if(out != null){
            			try{
            				out.closeEntry();
            			}catch(Exception e){
            				EmpExecutionContext.error("[ExcelExportUtil.zipFiles()]Entry关闭异常"+e.getMessage());
            			}
            		}
            		if(in != null){
            			try{
            				in.close();
            			}catch(Exception e){
            				EmpExecutionContext.error("[ExcelExportUtil.zipFiles()]FileInputStream关闭异常"+e.getMessage());
            			}
            		}
            	}
            }    
            // Complete the ZIP file    
//            out.close();   
        } catch (IOException e) {   
        	EmpExecutionContext.error(e,"系统下行记录中将文件夹Mt_Info转为zip包失败");
        	return false; 
        }
		return true;    
    }
    // 删除服务器端保存的xls文件
 	public void deleteTmpExcel(String path) {
 		// 获得该路径
 		File f = new File(path);
 		// 获得该文件夹下的所有文件
 		String[] list = f.list();
 		File temp;

 		// 删除结果
 		boolean b = true;
 		// 循环删除
 		for (String s : list) {
 			temp = new File(path + "/" + s);
 			b = temp.delete();
 			if (!b) {
 				EmpExecutionContext.error("存在的Excel删除不成功");	
 			}else{
 				EmpExecutionContext.info("存在的Excel删除成功");	
 			}
 		}
 	}
}
