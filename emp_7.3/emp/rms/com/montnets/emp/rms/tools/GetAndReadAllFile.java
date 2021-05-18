package com.montnets.emp.rms.tools;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.util.IOUtils;
  
  
/**
 * 获取指定文件夹下的所有txt文件内容
 * @author xuty
 *
 */
public class GetAndReadAllFile {  
  
  
    /** 
     * 获取文件的扩展名 
     *  
     * @param filename 
     * @param defExt 
     * @return 
     */  
    public static String getExtension(String filename, String defExt) {  
        if ((filename != null) && (filename.length() > 0)) {  
            int i = filename.lastIndexOf('.');  
  
  
            if ((i > -1) && (i < (filename.length() - 1))) {  
                return filename.substring(i + 1);  
            }  
        }  
        return defExt;  
    }  
  
  
    public static String getExtension(String filename) {  
        return getExtension(filename, "");  
    }  
  
  
    /** 
     * 获取一个文件夹下的所有文件 要求：后缀名为txt (可自己修改) 
     *  
     * @param file 
     * @return 
     */  
    public static List<String> getFileList(File file) {  
        List<String> result = new ArrayList<String>();  
        if (!file.isDirectory()) {  
            result.add(file.getAbsolutePath());  
        } else {  
            // 内部匿名类，用来过滤文件类型  
            File[] directoryList = file.listFiles(new FileFilter() {  
                public boolean accept(File file) {  
                    if (file.isFile() && file.getName().indexOf("txt") > -1) {  
                        return true;  
                    } else {  
                        return false;  
                    }  
                }  
            });  
            for (int i = 0; i < directoryList.length; i++) {  
                result.add(directoryList[i].getAbsolutePath());  
            }  
        }  
        return result;  
    }  
  
  
    /** 
     * 以UTF-8编码方式读取文件内容 
     *  
     * @param path 
     * @return 
     * @throws IOException 
     */  
    public static String getContentByLocalFile(File path){  
    	StringBuffer sbuf  = new StringBuffer();
    	InputStream input = null; 
    	InputStreamReader reader = null;
    	BufferedReader br = null;
        try {
			input = new FileInputStream(path);
			reader = new InputStreamReader(input, "UTF-8");
			br = new BufferedReader(reader);
			String lineTxt = null;
			while ((lineTxt = br.readLine()) != null) {
				sbuf.append(lineTxt);
			}
//			  br.close();
		} catch (Exception e) {
			 EmpExecutionContext.error(e,"");
		} finally{
			try {
				IOUtils.closeReaders(null, br,reader);
			} catch (IOException e) {
				EmpExecutionContext.error(e, "IO异常");
			}
			if(input != null){
				try {
					input.close();
				} catch (IOException e) {
					EmpExecutionContext.error(e, "IO异常");
				}
			}
		}
		return sbuf.toString(); 
    }  
  
  
    /** 
     * 去掉文件的扩展名 
     *  
     * @param filename 
     * @return 
     */  
    public static String trimExtension(String filename) {  
        if ((filename != null) && (filename.length() > 0)) {  
            int i = filename.lastIndexOf('.');  
            if ((i > -1) && (i < (filename.length()))) {  
                return filename.substring(0, i);  
            }  
        }  
        return filename;  
    }  
  
  
    /** 
     * @param args 
     */  
    public static void main(String[] args) throws IOException {  
        // Test get and read all file  
        List<String> fileList = getFileList(new File("D:\\apache-tomcat-6.0.35(8080)\\webapps\\p_rms\\file\\rms\\templates\\100001\\683\\src"));  
        String fileContent = null;  
        for (String s : fileList) {  
            // 文件内容  
            fileContent = getContentByLocalFile(new File(s));  
            // 打印文件内容  
            System.out.println(fileContent);  
        }  
  
    }  
}  