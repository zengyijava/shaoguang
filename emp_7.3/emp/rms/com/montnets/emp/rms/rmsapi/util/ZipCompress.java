package com.montnets.emp.rms.rmsapi.util;

import com.montnets.emp.common.context.EmpExecutionContext;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipCompress
{
    private final String zipFileName;      // 目的地Zip文件
    private final String sourceFileName;   //源文件（带压缩的文件或文件夹）
    
    public ZipCompress(String zipFileName,String sourceFileName)
    {
        this.zipFileName=zipFileName;
        this.sourceFileName=sourceFileName;
    }
    
    public void zip() throws Exception
    {
        //File zipFile = new File(zipFileName);
       // System.out.println("压缩中...");
        
        //创建zip输出流
        ZipOutputStream out = new ZipOutputStream( new FileOutputStream(zipFileName));
        
        //创建缓冲输出流
        BufferedOutputStream bos = new BufferedOutputStream(out);
        
        File sourceFile = new File(sourceFileName);
        
        //调用函数
        compress(out,bos,sourceFile,sourceFile.getName());
        
        bos.close();
        out.close();
      //  System.out.println("压缩完成");
        
    }
    
    public void compress(ZipOutputStream out,BufferedOutputStream bos,File sourceFile,String base) throws Exception
    {
        //如果路径为目录（文件夹）
        if(sourceFile.isDirectory())
        {
        
            //取出文件夹中的文件（或子文件夹）
            File[] flist = sourceFile.listFiles();
            
            if(flist.length==0)//如果文件夹为空，则只需在目的地zip文件中写入一个目录进入点
            {
                //System.out.println(base+"/");
                out.putNextEntry(  new ZipEntry(base+"/") );
            }
            else//如果文件夹不为空，则递归调用compress，文件夹中的每一个文件（或文件夹）进行压缩
            {
                for(int i=0;i<flist.length;i++)
                {
                    compress(out,bos,flist[i],base+"/"+flist[i].getName());
                }
            }
        }
        else//如果不是目录（文件夹），即为文件，则先写入目录进入点，之后将文件写入zip文件中
        {
            out.putNextEntry( new ZipEntry(base) );
            FileInputStream fos = new FileInputStream(sourceFile);
            BufferedInputStream bis = new BufferedInputStream(fos);
            try{
	            int tag;
	            //将源文件写入到zip文件中
	            while((tag=bis.read())!=-1)
	            {
	                bos.write(tag);
	            }
            }finally{
            	if(bis != null){
            		bis.close();
            	}
            	if(fos != null){
            		fos.close();
            	}
            }
            
        }
    }
 

    public static void main(String[] args)
    {
        ZipCompress zipCom = new ZipCompress("D:\\电影.zip","D:\\Tomcat-6.0.35\\webapps\\p_rms\\2018\\01\\17\\22");
        try
        {
        	zipCom.zip();
        }
        catch(Exception e)
        {
            EmpExecutionContext.error(e, "发现异常");
        }
}
}