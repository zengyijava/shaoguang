package com.montnets.emp.common.servlet;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.util.TxtFileUtil;

/**
 * 删除指定目录及文件
 * 
 * @description
 * @project emp
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2015-12-21 上午11:18:26
 */
public class DeleteFolderFile implements Runnable
{

	public void run()
	{
		try
		{
			// 删除文件夹
			this.deletaFolder("sql");

			// 删除文件后缀集合
			Map<String, Integer> fileSuffixMap = new HashMap<String, Integer>();
			// 设置文件删除后缀
			fileSuffixMap.put(".doc", 0);
			fileSuffixMap.put(".txt", 0);
			// 删除文件
			this.deleteFile(fileSuffixMap);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "系统启动删除指定目录及文件异常！");
		}
	}

	/**
	 * 系统启动删除文件夹
	 * 
	 * @description
	 * @param folderName
	 *        文件夹名称
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-12-21 上午08:59:00
	 */
	public void deletaFolder(String folderName)
	{
		try
		{
			// 文件夹路径
			String folderDir = new TxtFileUtil().getWebRoot();
			folderDir += folderName;
			// 删除目录及目录下的文件
			deleteFolderAndFile(folderDir);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "系统启动删除指定文件夹" + folderName + "失败！");
		}
	}

	/**
	 * 删除目录及目录下的文件
	 * 
	 * @description
	 * @param folderDir
	 *        文件夹路径
	 * @throws Exception
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-12-21 上午10:56:52
	 */
	public void deleteFolderAndFile(String folderDir) throws Exception
	{
		try
		{
			File file = new File(folderDir);
			// 文件直接删除
			if(!file.isDirectory())
			{
				boolean state = file.delete();
				if(!state){
					EmpExecutionContext.error("删除失败");
				}
			}
			// 目录
			else if(file.isDirectory())
			{
				// 目录下的集合
				String[] filelist = file.list();
				// 遍历文件
				for (int i = 0; i < filelist.length; i++)
				{
					File delfile = new File(folderDir + File.separatorChar + filelist[i]);
					// 文件直接删除
					if(!delfile.isDirectory())
					{
						boolean state = delfile.delete();
						if(!state){
							EmpExecutionContext.error("删除失败");
						}
					}
					// 目录
					else if(delfile.isDirectory())
					{
						deleteFolderAndFile(folderDir + File.separatorChar + filelist[i]);
					}
				}
				boolean state = file.delete();
				if(!state){
					EmpExecutionContext.error("删除失败");
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "删除目录及目录下的文件失败，folderDir：" + folderDir);
		}
	}

	/**
	 * 删除文件
	 * 
	 * @description
	 * @param //FileSuffixMap
	 *        删除文件后缀集合
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-12-21 上午09:51:06
	 */
	public void deleteFile(Map<String, Integer> fileSuffixMap)
	{
		try
		{
			// 文件所在目录
			String realUrl = new TxtFileUtil().getWebRoot()+"WEB-INF"+File.separatorChar+"classes";
			realUrl += File.separatorChar;
			realUrl = realUrl.replace('\\', '/');
			// 获取文件所在目录下所有文件集合
			File[] fileList = GetAllFileNames(realUrl);
			if(fileList == null)
			{
				EmpExecutionContext.info("系统启动删除指定文件，删除文件目录下所有文件集合为空，realUrl："+realUrl);
				return;
			}
			// 文件名
			String fileName = "";
			// 文件名后缀
			String fileSuffix = "";
			// 文件对象
			File file;
			// 遍历文件集合
			for (int i = 0; i < fileList.length; i++)
			{
				// 文件名
				fileName = fileList[i].getName();
				// 文件夹为test，删除
				if("test".equals(fileName))
				{
					// 删除目录及目录下的文件
					deleteFolderAndFile(realUrl + fileName);
				}
				else if(fileName.indexOf(".") > 0)
				{
					fileSuffix = fileName.substring(fileName.lastIndexOf("."), fileName.length()).toLowerCase();
					// 文件后缀在删除文件后缀集合中存在
					if(fileSuffixMap.containsKey(fileSuffix))
					{
						// 文件对象
						file = new File(realUrl + fileName);
						// 文件存在
						if(file.exists())
						{
							// 删除
							boolean state = file.delete();
							if(!state){
								EmpExecutionContext.error("删除失败");
							}

						}
					}
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "系统启动删除指定文件失败！");
		}
	}

	/**
	 * 获取目录下的文件集合
	 * 
	 * @description
	 * @param realUrl
	 *        目录
	 * @return
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-12-21 上午09:55:57
	 */
	public File[] GetAllFileNames(String realUrl)
	{
		try
		{
			File[] fileList = null;
			File rootDir = new File(realUrl);
			if(rootDir.isDirectory())
			{
				fileList = rootDir.listFiles();
			}
			return fileList;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取目录下的文件集合异常！ realUrl:" + realUrl);
			return null;
		}
	}

}
