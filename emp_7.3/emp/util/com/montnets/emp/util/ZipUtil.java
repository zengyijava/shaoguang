package com.montnets.emp.util;

import com.montnets.emp.common.context.EmpExecutionContext;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;

/**
 * 压缩通用方法
 * @author Administrator
 *
 */
public class ZipUtil
{
	public static final String EXT = ".zip";

	private static final String BASE_DIR = "";

	private static final String PATH = "/";

	private static final int BUFFER = 4096;

	public static void compress(String srcPath, String destPath)
			throws Exception
	{
		File srcFile = new File(srcPath);
		compress(srcFile, destPath);
	}

	public static void compress(String srcPath) throws Exception
	{
		File srcFile = new File(srcPath);
		compress(srcFile);
	}

	public static void compress(File srcFile) throws Exception
	{
		String name = srcFile.getName();
		String basePath = srcFile.getParent() + "/";
		String destPath = basePath + name + EXT;
		compress(srcFile, destPath);
	}

	public static void compress(File srcFile, File destFile) throws Exception
	{
		CheckedOutputStream cos = new CheckedOutputStream(new FileOutputStream(
				destFile), new CRC32());
		ZipOutputStream zos = new ZipOutputStream(cos);
		compress(srcFile, zos, BASE_DIR);
		zos.closeEntry();
		zos.flush();
		zos.close();
	}

	public static void compress(File srcFile, String destPath) throws Exception
	{
		compress(srcFile, new File(destPath));
	}

	private static void compress(File srcFile, ZipOutputStream zos,
			String basePath) throws Exception
	{
		if (srcFile.isDirectory())
		{
			compressDir(srcFile, zos, basePath);
		} else
		{
			compressFile(srcFile, zos, basePath);
		}
	}
	/**
	 * @description 压缩文件目录    
	 * @param srcFile 原文件
	 * @param destPath 需要压缩的文件集合
	 * @param webroot 物理路径
	 * @throws Exception       			 
	 * @author linzhihan <zhihanking@163.com>
	 * @datetime 2013-10-17 上午09:38:10
	 */
	public static void compress(File srcFile, String[] destPath,String webroot) throws Exception
	{
		CheckedOutputStream cos = null;
		ZipOutputStream zos = null;
		FileOutputStream fileOut = null;
		try
		{
			if(!srcFile.getParentFile().exists())
			{
				srcFile.getParentFile().mkdirs();
			}
			if(!srcFile.exists())
			{
                boolean flag = srcFile.createNewFile();
                if (!flag) {
                    EmpExecutionContext.error("创建文件失败！");
                }
			}
			fileOut = new FileOutputStream(srcFile);
			cos = new CheckedOutputStream(fileOut, new CRC32());
			zos = new ZipOutputStream(cos);
			//遍历
			for(int i=0;i<destPath.length;i++)
			{
				File ff = new File(webroot + destPath[i]);
				String dirs = destPath[i].substring(0,destPath[i].lastIndexOf("/")+1);
				//添加压缩文件
				compressFile(ff, zos, dirs);
			}
		
		}catch(Exception e)
		{
			EmpExecutionContext.error(e, "压缩文件目录异常！");
			throw e;
		}finally
		{
			if(zos != null)
			{
				zos.closeEntry();
				zos.flush();
				zos.close();
			}
			if(cos != null)
			{
				cos.flush();
				cos.close();
			}
			if(fileOut != null){
				fileOut.close();
			}
		}
	}
	/**
	 * @description 压缩文件目录    
	 * @param srcFile 原文件
	 * @param destPath 目录，对此目录下的文件进行压缩
	 * @throws Exception       			 
	 * @author linzhihan <zhihanking@163.com>
	 * @datetime 2013-10-17 上午09:39:31
	 */
	public static void compressDir(File srcFile, String destPath) throws Exception
	{
		CheckedOutputStream cos = null;
		ZipOutputStream zos = null;
		FileOutputStream fileOut = null;
		try
		{
			String filename = srcFile.getName();
			//filename = filename.substring(0,filename.indexOf("."));
			fileOut = new FileOutputStream(srcFile);
			cos = new CheckedOutputStream(fileOut, new CRC32());
			zos = new ZipOutputStream(cos);
			File dirFile = new File(destPath);
			//获取目录下的所有文件
			File[] files = dirFile.listFiles();
			for(int i=0;i<files.length;i++)
			{
				if(files[i].getName().equals(filename))
				{
					continue;
				}
				compress(files[i], zos, "");
			}
		
		}catch(Exception e)
		{
			EmpExecutionContext.error(e, "压缩文件目录异常！");
			throw e;
		}finally
		{
			if(zos != null)
			{
				zos.closeEntry();
				zos.flush();
				zos.close();
			}
			if(cos != null)
			{
				cos.flush();
				cos.close();
			}
			if(fileOut != null){
				fileOut.close();
			}
		}
	}
   /**
    * 创建目录
    * @param dir
    * @param zos
    * @param basePath
    * @throws Exception
    */
	private static void compressDir(File dir, ZipOutputStream zos,
			String basePath) throws Exception
	{
		for (File file : dir.listFiles())
		{
			compress(file, zos, basePath + dir.getName() + PATH);
		}
	}
    /**
     * 创建文件
     * @param file
     * @param zos
     * @param dir
     * @throws Exception
     */
	private static void compressFile(File file, ZipOutputStream zos, String dir)
			throws Exception
	{
		FileInputStream fileInput = null;
		BufferedInputStream bis = null;
		try{
			ZipEntry entry = new ZipEntry(dir + file.getName());
			zos.putNextEntry(entry);
			fileInput = new FileInputStream(file);
			bis = new BufferedInputStream(fileInput);
			int count;
			byte data[] = new byte[BUFFER];
			while ((count = bis.read(data, 0, BUFFER)) != -1)
			{
				zos.write(data, 0, count);
			}
		}finally{
			IOUtils.closeInputStream(ZipUtil.class, bis,fileInput);
		}
	}
	
	/**
	 * 删除目录
	 * @param folderPath
	 * @return
	 */
	private static boolean delFloder(String folderPath)
	{
		String filePath = folderPath;
		java.io.File myFilePath = new java.io.File(filePath);
		return myFilePath.delete();
	}
	/**
	 * 删除所有的文件
	 * @param path
	 * @return
	 */
	public static boolean delAllFile(String path)
	{
		boolean flag = false;
		File file = new File(path);
		if(!file.exists())
		{
			return flag;
		}
		if(!file.isDirectory()){
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for(int i = 0;i<tempList.length;i++)
		{
			if(path.endsWith(File.separator))
			{
				temp = new File(path+tempList[i]);
			}else{
				temp = new File(path+File.separator+tempList[i]);
			}
			if(temp.isFile())
			{
                boolean status = temp.delete();
                if (!status) {
                    EmpExecutionContext.error("删除文件失败！");
                }
			}
			if(temp.isDirectory())
			{
	 
				delAllFile(path+"/"+tempList[i]);
				
				flag = true;
			}
		}
		
		if(delFloder(path))
		{
			flag = true;
		}

		return flag;
	}
	
	/**
	 * @description  解压缩    
	 * @param srcFile zip文件地址
	 * @param resltFile 解压的目标目录
	 * @return true-成功，fasle-失败
	 * @author linzhihan <zhihanking@163.com>
	 * @datetime 2013-10-18 上午11:42:01
	 */
	public static boolean upZipFile(String srcFile, String resltFile) 
	{
		ZipFile zfile = null;
		boolean result = true;
		try
		{
			zfile = new ZipFile(srcFile);
			Enumeration zList = zfile.getEntries();
			ZipEntry ze = null;
			byte[] buf = new byte[1024];
			if(!resltFile.endsWith("/"))
			{
				resltFile = resltFile + "/";
			}
			String physicsUrl =  resltFile;
			File physicsDir = new File(physicsUrl);
			if(!physicsDir.exists())
			{
				physicsDir.mkdirs();
			}
			while(zList.hasMoreElements() && result)
			{
				ze = (ZipEntry) zList.nextElement();
				//如果是目录，则创建目录
				if(ze.isDirectory())
				{
					File f = new File(physicsUrl + ze.getName());
					f.mkdir();
					continue;
				}
				//压缩文件的解压物理路径
				File physicsFile = new File(physicsUrl + ze.getName());
				//创建文件目录
				if(!physicsFile.getParentFile().exists())
				{
					physicsFile.getParentFile().mkdirs();
				}
				//创建新文件
				if(!physicsFile.exists())
				{
                    boolean flag = physicsFile.createNewFile();
                    if (!flag) {
                        EmpExecutionContext.error("创建文件失败！");
                    }
				}
				// 如果当前解压缩文件的父级文件夹没有创建的话，则创建好父级文件夹
				if (!physicsFile.getParentFile().exists()) {
					physicsFile.getParentFile().mkdirs();
	             }
				OutputStream os = null;
				InputStream is = null;
				FileOutputStream fos = null;
				try
				{
					fos = new FileOutputStream(physicsFile);
					os = new BufferedOutputStream(fos);
					is = new BufferedInputStream(zfile.getInputStream(ze));
					int readLen = 0;
					while((readLen = is.read(buf, 0, 1024)) != -1)
					{
						os.write(buf, 0, readLen);
					}
				}catch(Exception e)
				{
					EmpExecutionContext.error(e, "解压文件异常！");
					throw e;
				}finally
				{
					if(is != null)
					{
						is.close();
					}
					if(os != null)
					{
						os.close();
					}
					if(fos != null)
					{
						fos.close();
					}
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"解压文件异常！");
			result = false;
		}
		finally
		{
			if(zfile != null)
			{
				try
				{
					zfile.close();
				}
				catch (IOException e)
				{
					EmpExecutionContext.error(e,"解压文件时关闭压缩文件流异常！");
				}
			}
		}
		return result;
	}
		/**
	 * @description  解压缩   (网讯使用)
	 * @param srcFile zip文件地址
	 * @param resltFile 解压的目标目录
	 * @return true-成功，fasle-失败
	 * @author linzhihan <zhihanking@163.com>
	 * @datetime 2013-10-18 上午11:42:01
	 */
	public static boolean upZipFileWX(String srcFile, String resltFile) 
	{
		ZipFile zfile = null;
		boolean result = true;
		try
		{
		    zfile = new ZipFile(srcFile);
			Enumeration zList = zfile.getEntries();
			ZipEntry ze = null;
			
			if(!resltFile.endsWith("/"))
			{
				resltFile = resltFile + "/";
			}
			String physicsUrl =  resltFile;
			File physicsDir = new File(physicsUrl);
			if(!physicsDir.exists())
			{
				physicsDir.mkdirs();
			}
			while(zList.hasMoreElements() && result)
			{
				ze = (ZipEntry) zList.nextElement();
				//如果是目录，则创建目录
				if(ze.isDirectory())
				{
					File f = new File(physicsUrl + ze.getName());
					f.mkdir();
					continue;
				}
				//压缩文件的解压物理路径
				File physicsFile = new File(physicsUrl + ze.getName());
				//创建文件目录
				if(!physicsFile.getParentFile().exists())
				{
					physicsFile.getParentFile().mkdirs();
				}
				//创建新文件
				if(!physicsFile.exists())
				{
                    boolean flag = physicsFile.createNewFile();
                    if (!flag) {
                        EmpExecutionContext.error("创建文件失败！");
                    }
				}
				// 如果当前解压缩文件的父级文件夹没有创建的话，则创建好父级文件夹
				if (!physicsFile.getParentFile().exists()) {
					physicsFile.getParentFile().mkdirs();
	             }
				OutputStream os = null;
				BufferedReader br = null;
				 String tempString = null;
				 FileOutputStream fos = null;
				try
				{
					fos = new FileOutputStream(physicsFile);
					os = new BufferedOutputStream(fos);
					 br =new BufferedReader(  
				                new InputStreamReader(new BufferedInputStream(zfile.getInputStream(ze)),"UTF-8"));
		            while ((tempString = br.readLine()) != null) {
		                // 显示行号
		            	os.write(tempString.getBytes());
		            	os.write("\r\n".getBytes());
		            }
			        
				}catch(Exception e)
				{
					EmpExecutionContext.error(e, "解压文件异常！");
					throw e;
				}finally
				{
					if(br != null)
					{
						br.close();
					}
					if(os != null)
					{
						os.close();
					}
					if(fos != null)
					{
						fos.close();
					}

				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"解压文件异常！");
			result = false;
		}
		finally
		{
			if(zfile != null)
			{
				try
				{
					zfile.close();
				}
				catch (IOException e)
				{
					EmpExecutionContext.error(e,"解压文件时关闭压缩文件流异常！");
				}
			}
		}
		return result;
	}
}
