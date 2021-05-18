package com.montnets.emp.common.biz;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.cache.CommDataDealStorage;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.constant.WebgatePropInfo;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.CommDataDealDAO;
import com.montnets.emp.common.dao.DepSpecialDAO;
import com.montnets.emp.common.dao.SmsSpecialDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.GenericEmpDAO;
import com.montnets.emp.common.entity.LfNodeBaseInfo;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.system.LfGlobalVariable;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfDomination;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.security.wgmsgconfig.WgMsgConfigBiz;
import com.montnets.emp.util.GetSxCount;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.TxtFileUtil;
import com.montnets.emp.util.ZipUtil;

/**
 * @author Administrator
 */
public class CommonBiz extends SuperBiz
{

	SmsSpecialDAO	smsSpecialDAO	= new SmsSpecialDAO();

    CommDataDealDAO commDataDao = new CommDataDealDAO();

    private static ExecutorService exec = null;

	/**
	 * 根据guid获取一个对象
	 * 
	 * @param <T>
	 * @param entityClass
	 * @param guId
	 * @return
	 */
	public <T> T getObjByGuid(Class<T> entityClass, Long guId)
	{

		if(entityClass == null || guId == null)
		{
			return null;
		}

		T obj = null;
		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("guId", String.valueOf(guId));
			List<T> objsList = empDao.findListByCondition(entityClass, conditionMap, null);
			if(objsList != null && objsList.size() > 0)
			{
				obj = objsList.get(0);
			}

		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "根据guid获取一个对象异常。");
		}

		return obj;
	}

	/**
	 * 获取发送账号信息
	 * 
	 * @param spUserId
	 * @return
	 * @throws Exception
	 */
	public String getSpPwdBySpUserId(String spUserId) throws Exception
	{

		WgMsgConfigBiz wgBiz = new WgMsgConfigBiz();
		Userdata spUser = wgBiz.getSmsUserdataByUserid(spUserId);

		return spUser.getUserPassword();
	}

	/**
	 * 上传文件到文件服务器
	 * 
	 * @param filePath
	 *        文件相对路径
	 * @return
	 */
	public String uploadFileToFileCenter(String filePath)
	{
		return uploadFileToFileCenter(filePath, "", true);
	}

	/**
	 * 上传文件到文件服务器，且不删除本地文件
	 * 
	 * @param filePath
	 *        文件相对路径
	 * @return
	 */
	public String uploadFileToFileCenter(String filePath,boolean isDel)
	{
		return uploadFileToFileCenter(filePath, "", isDel);
	}

	/**
	 * 从文件服务器下载指定文件（文件服务器不存在会遍历emp节点查找）
	 * 
	 * @param fileUrl 支持绝对、相对路径
	 *        需下载的文件相对路径
	 * @return success-成功，error-失败
	 */
	public String downloadFileFromFileCenter(String fileUrl)
	{
        String result = "";

        if(fileUrl == null)
        {
            return "error";
        }

        //绝对地址URL
        if(fileUrl.startsWith("http:"))
        {
            result = downloadFileWithUrl(fileUrl);
            return result;
        }

        //相对地址URL
        String httpUrl = checkServerFile(fileUrl);
        if(httpUrl == null)
        {
            return "error";
        }

        result = downloadFileWithUrl(httpUrl+fileUrl);
        return result;
	}

	/**
	 * 从文件服务器下载指定文件的压缩文件
	 * 
	 * @param fileUrl
	 *        需下载的文件相对路径
	 * @return success-成功，error-失败
	 * @author linzhihan <zhihanking@163.com>
	 * @datetime 2013-10-17 上午10:52:03
	 */
	public String downFileFromFileCenWhitZip(String fileUrl)
	{
		// 物理路径
		String webroot = new TxtFileUtil().getWebRoot();
		// 文件夹目录
		String filePath = webroot + fileUrl.substring(0, fileUrl.lastIndexOf("/"));
		// 定义压缩文件名称
		String zipFileUrl = fileUrl + ".zip";
		// 文件http请求地址
		String url = "";
		File filePathF = new File(filePath);
		if(!filePathF.exists())
		{
			filePathF.mkdirs();
		}
		String result = "";

        String httpUrl = "";
        String[] httpUrls = getALiveFileServer();
        try {
        	if(httpUrls != null){
				url = httpUrls[0] + fileUrl;
				// 执行下载
				result = writerFileOfOther(url, webroot + zipFileUrl);
				// 下载成功后
				if("success".equals(result)) {
					//执行解压
					ZipUtil.upZipFile(webroot + zipFileUrl, filePath);
				}
			}
        } catch (Exception e) {
            EmpExecutionContext.error(e,"从文件服务器["+httpUrl+"]下载文件异常！");
        }

		return result;
	}
	
	/**
	 * 从文件服务器下载指定文件的压缩文件
	 * 
	 * @param fileUrl
	 *        需下载的文件相对路径
	 * @return success-成功，error-失败
	 * @author linzhihan <zhihanking@163.com>
	 * @datetime 2013-10-17 上午10:52:03
	 */
	public String downFileFromFileCenWhitZipWX(String fileUrl)
	{
		// 物理路径
		String webroot = new TxtFileUtil().getWebRoot();
		// 文件夹目录
		String filePath = webroot + fileUrl.substring(0, fileUrl.lastIndexOf("/"));
		// 定义压缩文件名称
		String zipFileUrl = fileUrl;
		// 文件http请求地址
		String url ;
		File filePathF = new File(filePath);
		if(!filePathF.exists())
		{
			filePathF.mkdirs();
		}
		String result = "";

        String httpUrl = "";
        String[] httpUrls = getALiveFileServer();
        try {
            url = httpUrls[0] + fileUrl;
            // 执行下载
            result = writerFileOfWX(url, webroot + zipFileUrl);
            // 下载成功后
//            if("success".equals(result))
//            {
//                //执行解压
//                if(ZipUtil.upZipFileWX(webroot + zipFileUrl, filePath))
//                {
//                    //解压完成后删除压缩文件
//                    //this.deleteFile(zipFileUrl);
//                }
//            }
        } catch (Exception e) {
            EmpExecutionContext.error(e,"从文件服务器["+httpUrl+"]下载文件异常！");
        }

		return result;
	}

    /**
     * 从文件url路径直接下载文件到本地
     * @param url 为相对路径时 从文件服务器下载
     * @return
     */
    public String downloadFileWithUrl(String url)
    {
        String result = "";

        try
        {
            if(url == null)
            {
                return "error";
            }
            //url中包含cxtj/query/file/report/，则是系统下行记录导出 modify by tanglili 20200114
			String fileUrl="";
            if(!url.contains("cxtj/query/file/report/")) {
				fileUrl = url.replaceFirst("(.*)?/file/", "file/");
			}else{
				fileUrl=url.replaceFirst("(.*)?/cxtj/", "cxtj/");
			}
            // 物理路径
            String webroot = new TxtFileUtil().getWebRoot();

            //若文件存在 则直接返回成功
            if(new File(webroot,fileUrl).exists())
            {
                return "success";
            }

            // 文件夹目录
            String filePath = webroot + fileUrl.substring(0, fileUrl.lastIndexOf("/"));

            File filePathF = new File(filePath);
            if(!filePathF.exists())
            {
                filePathF.mkdirs();
            }

            // 文件类型
            String fileType = fileUrl.substring(fileUrl.lastIndexOf(".") + 1).toLowerCase();
            // 文件http请求地址
            if(!"txt".equals(fileType))
            {
                result = writerFileOfOther(url, webroot + fileUrl);
            }
            else
            {
                result = writerFileOfTxt(url, webroot + fileUrl);
            }
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e,"从URL["+url+"]下载文件异常！");
        }


        return result;
    }

	/**
	 * 删除本地文件
	 * 
	 * @param filePath
	 *        文件相对路径
	 */
	public void deleteFile(String filePath)
	{
		filePath = new TxtFileUtil().getWebRoot() + filePath;
		File file = new File(filePath);
		// 如果存在则删除
		if(file.exists())
		{
            //文件节点来源
            String fileOrgi = new TxtFileUtil().smsFileServerNum(filePath);
            //发送文件不再删除
            if(!StaticValue.getServerNumber().equals(fileOrgi))
            {
                boolean delSuccess = file.delete();
                if(!delSuccess){
                	EmpExecutionContext.error("删除文件失败");
				}
            }

		}
	}

	/**
	 * 发送复制文件的指令给文件服务器
	 * 
	 * @param filePath1
	 *        原文件
	 * @param filePath2
	 *        目标文件
	 * @return
	 */
	public String copyFile(String filePath1, String filePath2)
	{
		// 文件中心http地址
		//String httpUrl = StaticValue.FILE_SERVER_URL;
		String httpUrl = StaticValue.getFileServerUrl();
		// 本机http地址
		String method = "POST";
		String destUrl = httpUrl + "checkFile";
		// 参数项
		String objstr = "type=copy&filePath1=" + filePath1 + "&filePath2=" + filePath2;
		String uploadResult = requestHttp(method, destUrl, objstr);
		return uploadResult;
	}

	/**
	 * @description 使用压缩文件上传到文件服务器
	 * @param srcFile
	 *        目录
	 * @param fileNames
	 *        需要压缩的文件集合，值srcFile目录下为文件名称
	 * @return 上传结果：success-成功，error-失败
	 * @author linzhihan <zhihanking@163.com>
	 * @datetime 2013-10-17 上午08:52:03
	 */
	public String uploadFileWhitZip(String srcFile, String[] fileNames)
	{
		if(!srcFile.endsWith("/"))
		{
			srcFile = srcFile + "/";
		}

		TxtFileUtil txtFileUtil = new TxtFileUtil();
		// 物理路径
		String webroot = txtFileUtil.getWebRoot();
		// 压缩文件名
		String fileName = "zipfiles" + GetSxCount.getInstance().getCount() + ".zip";
		// 压缩文件完整物理路径
		String filePath = webroot + srcFile + fileName;
		File zipFile = new File(filePath);
		try
		{
			ZipUtil.compress(zipFile, fileNames, webroot + srcFile);
			// 定义是否解压的参数
			String otherObj = "&unzip=1";
			// 上传文件
			return uploadFileToFileCenter(srcFile + fileName, otherObj, false);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "上传文件到服务器时，添加压缩文件失败！");
			return "false";
		}
		finally
		{
			// 判断是否生成了压缩文件
			if(zipFile.exists())
			{
				// 存在则删除
				boolean delSuccess = zipFile.delete();
				if(!delSuccess){
					EmpExecutionContext.error("删除文件失败");
				}
			}
		}
	}

	/**
	 * @description 使用压缩文件上传单个文件到文件服务器
	 * @param destFile
	 *        需要上传的文件
	 * @param unDel
	 *        上传完成后是否删除压缩文件，1-不删除，0-删除
	 * @return 上传结果：success-成功，error-失败
	 * @author linzhihan <zhihanking@163.com>
	 * @datetime 2013-10-17 上午15:52:03
	 */
	public String uploadOneFileWhitZip(String destFile, String unDel)
	{
		// 压缩文件存放路径
		String srcFile = destFile.substring(0, destFile.lastIndexOf("/") + 1);

		TxtFileUtil txtFileUtil = new TxtFileUtil();
		// 物理路径
		String webroot = txtFileUtil.getWebRoot();
		// 需上传的文件名称
		String destName = destFile.substring(destFile.lastIndexOf("/") + 1);
		// 压缩文件名
		String fileName = destName + ".zip";
		// 压缩文件完整的物理路径
		String filePath = webroot + srcFile + fileName;
		File zipFile = new File(filePath);
		try
		{
			String[] fileNames = new String[] {destName};
			// 压缩文件
			ZipUtil.compress(zipFile, fileNames, webroot + srcFile);
			// 定义是否解压及是否删除原文件的参数
			String otherObj = "&unzip=1&undel=" + unDel;
			// 上传文件
			return uploadFileToFileCenter(srcFile + fileName, otherObj, false);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "上传文件到服务器时，添加压缩文件失败！");
			return "false";
		}
		finally
		{
			// 判断是否生成了压缩文件
			if(zipFile.exists())
			{
				// 存在则删除
				boolean delSuccess = zipFile.delete();
				if(!delSuccess){
					EmpExecutionContext.error("删除文件失败");
				}
			}
		}
	}

	/**
	 * @description 使用压缩文件上传到文件服务器
	 * @param
	 *
	 * @param fileNames
	 *        需要压缩的文件集合
	 * @return 上传结果：success-成功，error-失败
	 * @author linzhihan <zhihanking@163.com>
	 * @datetime 2013-10-17 上午08:52:03
	 */
	public String uploadFileWhitZip(String[] fileNames)
	{
		// 压缩文件直接存放在项目根目录下
		String srcFile = "";
		TxtFileUtil txtFileUtil = new TxtFileUtil();
		// 物理路径
		String webroot = txtFileUtil.getWebRoot();
		// 压缩文件名称
		String fileName = "zipfiles" + GetSxCount.getInstance().getCount() + ".zip";
		// 压缩文件完整的物理路径
		String filePath = webroot + srcFile + fileName;
		File zipFile = new File(filePath);
		try
		{
			// 压缩文件
			ZipUtil.compress(zipFile, fileNames, webroot);
			// 定义是否解压参数
			String otherObj = "&unzip=1";
			// 上传文件
			return uploadFileToFileCenter(srcFile + fileName, otherObj, false);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "上传文件到服务器时，添加压缩文件失败！");
			return "false";
		}
		finally
		{
			// 判断是否生成了压缩文件
			if(zipFile.exists())
			{
				// 存在则删除
				boolean delSuccess = zipFile.delete();
				if(!delSuccess){
					EmpExecutionContext.error("删除文件失败");
				}
			}
		}
	}

	/**
	 * @description 指定目录下的所有文件，使用压缩文件上传到文件服务器
	 * @param srcFile
	 *        目录。本目录下所有文件将会压缩上传
	 * @return 上传结果：success-成功，error-失败
	 * @author linzhihan <zhihanking@163.com>
	 * @datetime 2013-10-17 上午08:53:15
	 */
	public String uploadFileWhitZip(String srcFile)
	{
		// 需要压缩的文件所在目录
		String dirName = srcFile;
		if(srcFile.endsWith("/"))
		{
			dirName = dirName.substring(0, dirName.length() - 1);
		}
		else
		{
			srcFile = srcFile + "/";
		}
		TxtFileUtil txtFileUtil = new TxtFileUtil();
		// 物理路径
		String webroot = txtFileUtil.getWebRoot();
		// 压缩文件名称
		String fileName = dirName.substring(dirName.lastIndexOf("/") + 1) + ".zip";
		// 压缩文件所在物理路径
		String filePath = webroot + srcFile + fileName;
		File zipFile = new File(filePath);
		try
		{
			// 执行压缩
			ZipUtil.compress(zipFile, webroot + dirName);
			// 定义参数
			String otherObj = "&unzip=1";
			//上传
			return uploadFileToFileCenter(srcFile + fileName, otherObj, false);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "上传文件到服务器时，添加压缩文件失败！");
			return "false";
		}
		finally
		{
			// 判断是否生成了压缩文件
			if(zipFile.exists())
			{
				// 存在则删除
				boolean delSuccess = zipFile.delete();
				if(!delSuccess){
					EmpExecutionContext.error("删除文件失败");
				}
			}
		}
	}

	/**
	 * @description 上传文件到服务器
	 * @param filePath
	 *        待上传的文件路径
	 * @param otherObj
	 *        其他动态参数
	 * @param isDel
	 *        是否删除原文件
	 * @return success - 上传成功，error - 上传失败
	 * @author linzhihan <zhihanking@163.com>
	 * @datetime 2013-10-16 下午06:44:27
	 */
	private String uploadFileToFileCenter(String filePath, String otherObj, boolean isDel)
	{
		// 本机http地址
		String thisUrl = StaticValue.BASEURL;
		final String method = "POST";

        String uploadResult = "";

        final int maxRetry = 3;
        try {
            String[] httpUrls = getALiveFileServer();
            String httpUrl = null;

            if(httpUrls != null)
            {
                httpUrl = httpUrls[0];
                String destUrl = httpUrl + "filew";

                // 参数项
                String objstr = "fileName=" + filePath + "&url=" + thisUrl + filePath + otherObj;

                //最多上传三次
                for(int i = 0; i < maxRetry; i++)
                {
                    uploadResult = requestHttp(method, destUrl, objstr);
                    if("success".equals(uploadResult))
                    {
                        break;
                    }
                    Thread.sleep(300L);
                }
            }

            if("success".equals(uploadResult))
            {
                EmpExecutionContext.info("上传文件["+filePath+"]到服务器["+httpUrl+"]成功！");
            }else{
                //上传文件服务器失败 则上传到其他emp节点
                uploadFileToOtherNodes(StaticValue.BASEURL+filePath);
                if(httpUrl == null)
                {
                    httpUrl = "";
                }
                EmpExecutionContext.error("上传文件["+filePath+"]到服务器["+httpUrl+"]失败！");
            }

            uploadResult = "success";

        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e,"上传文件["+filePath+"]到服务器异常!");
            uploadResult = "error";
        }

		return uploadResult;
	}

    /**
     * 上传发送文件 上传文件服务器失败时分发至其他emp节点
     * @param filePath
     * @return 返回文件服务器地址 或者 本节点地址
     */
    public String uploadFileToFileServer(String filePath)
    {
        String httpUrl = null;
        try {
            //上传本地文件到文件服务器
            String result = uploadNodeFileToFileCenter(filePath,StaticValue.BASEURL);

            if(!"success".equals(result))
            {
                //本机http地址
                httpUrl = StaticValue.BASEURL;
            }
            else
            {
               // httpUrl = StaticValue.FILE_SERVER_URL;
				httpUrl = StaticValue.getFileServerUrl();
            }
        } catch (Exception e)
        {
            EmpExecutionContext.error(e,"上传文件["+filePath+"]到服务器异常!");
            httpUrl = StaticValue.BASEURL;
        }

        return httpUrl;
    }


    /**
     *上传指定节点下的文件到文件服务器
     * @param filePath
     * @param nodeUrl
     * @return
     */
    public String uploadNodeFileToFileCenter(String filePath, String nodeUrl)
    {
        final String method = "POST";

        String uploadResult = "";

        boolean isSucc = false;

        final int maxRetry = 1;
        try {
            String[] httpUrls = getALiveFileServer();
            String httpUrl = null;
            if(httpUrls != null)
            {
                httpUrl = httpUrls[0];
                String destUrl = httpUrl + "filew";

                // 参数项
                String objstr = "fileName=" + filePath + "&url=" + nodeUrl + filePath;

                //最多上传三次
                for(int i = 0; i < maxRetry; i++)
                {
                    uploadResult = requestHttp(method, destUrl, objstr);
                    if("success".equals(uploadResult))
                    {
                        isSucc = true;
                        break;
                    }
                    Thread.sleep(300L);
                }
            }


            if("success".equals(uploadResult))
            {
                EmpExecutionContext.info("上传文件url:"+filePath+"到服务器["+httpUrl+"]成功！");
            }else{
                //上传文件服务器失败 则上传到其他emp节点
                uploadFileToOtherNodes(nodeUrl+filePath);
                if(httpUrl == null)
                {
                    httpUrl = "";
                }
                EmpExecutionContext.error("上传文件url:"+filePath+"到服务器["+httpUrl+"]失败！");
            }

        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e,"上传文件到服务器异常!");
        }
        return isSucc?"success":"error";
    }

	/**
	 * 发送HTTP请求
	 * 
	 * @param method
	 * @param destUrl
	 * @param objstr
	 * @return
	 */
	public String requestHttp(String method, String destUrl, String objstr)
	{

		HttpURLConnection huc = null;
		URL url = null;
		String result = "";
		InputStream in = null;
		try
		{
			// 建立链接
			url = new URL(destUrl);
			// 开启一个url连接
			huc = (HttpURLConnection) url.openConnection();
			// 设置属性
			huc.setDoInput(true);
			huc.setDoOutput(true);
			huc.setConnectTimeout(StaticValue.getFileHttpRequestTimeout());
			huc.setReadTimeout(180*1000);

			if("POST".equals(method))
			{
				huc.setUseCaches(false);
			}
			huc.setRequestMethod(method);
			huc.connect();
			// 设置参数值
			// 设置输入流
			String param = objstr;
			if(param != null && !"".equals(param))
			{
				huc.getOutputStream().write(param.getBytes("utf-8"));
				huc.getOutputStream().flush();
				huc.getOutputStream().close();
			}
			// int code = huc.getResponseCode();
			// 获取页面内容
			in = huc.getInputStream();
			BufferedReader breader = new BufferedReader(new InputStreamReader(in, "utf-8"));

			StringBuffer str = new StringBuffer();
			String line = null;
			// 读出输出流
			while((line = breader.readLine()) != null)
			{
				str.append(line);
			}
			result = str.toString().trim();

			// 关闭连接
			huc.disconnect();
		}
        catch (ConnectException ce)
        {
            result = "refused";
        }
		catch (Exception e)
		{
			// 异常处理
			EmpExecutionContext.error(e, "发送HTTP请求异常。");
		}
		finally
		{
			// 关闭连接
			if(huc != null)
			{
				huc.disconnect();
			}
		}
		// 返回结果
		return result;
	}
	/**
	 * @param url
	 * @param projectpath
	 * @return
	 */
	private String writerFileOfWX(String url, String projectpath)
	{

		url = url.replace("%20", " ");//
		projectpath = projectpath.replace("%20", " ");
		File file = new File(projectpath);
		FileOutputStream os = null;
		BufferedInputStream bufferIs = null;
		try
		{
			URL urls = new URL(url);
			bufferIs = new BufferedInputStream(urls.openConnection().getInputStream());
			String top="<%@page language=\"java\" import=\"java.util.*\" pageEncoding=\"UTF-8\"%>";
			os = new FileOutputStream(file);
			byte buffer[] = new byte[4 * 1024];
			int size = 0;
			os.write(top.getBytes());
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
				if(os != null){
					try{
						os.close();
					}catch(Exception e){
						EmpExecutionContext.error(e, "关闭资源异常");
					}
				}
				if(bufferIs != null){
					try{
						bufferIs.close();
					}catch(Exception e){
						EmpExecutionContext.error(e, "关闭资源异常");
					}
				}
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "关闭文件流异常。");
			}
		}
		return "success";
	}
	
	
	/**
	 * @param url
	 * @param projectpath
	 * @return
	 */
	private String writerFileOfOther(String url, String projectpath)
	{

		url = url.replace("%20", " ");//
		projectpath = projectpath.replace("%20", " ");
		File file = new File(projectpath);
		FileOutputStream os = null;
		BufferedInputStream bufferIs = null;
		try
		{
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
				if(os != null){
					os.close();
				}
				if(bufferIs != null){
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

	/**
	 * @param url
	 * @param projectpath
	 * @return
	 */
	private String writerFileOfTxt(String url, String projectpath)
	{
		String line = System.getProperties().getProperty("line.separator");
		url = url.replace("%20", " ");//

		BufferedReader br = null;
		try
		{
			projectpath = projectpath.replace("%20", " ");
			File file = new File(projectpath);
//			if(!file.exists())
//			{
//				file.createNewFile();
//			}
			URL urls = new URL(url);
			br = new BufferedReader(new InputStreamReader(urls.openConnection().getInputStream(), "GBK"));
			StringBuffer content = new StringBuffer();
			String temp = "";
			int a = 0;
			while((temp = br.readLine()) != null)
			{
				a++;
				content.append(temp).append(line);
				if(a >= 5000)
				{
					this.writeToTxtFile(file, content.toString());
					content.setLength(0);
					a = 0;
				}
			}
			if(a > 0)
			{
				this.writeToTxtFile(file, content.toString());
				content.setLength(0);
				a = 0;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "生成并写文件异常。");
			return "error";
		}
		finally
		{
			if(br != null)
			{
				try
				{
					br.close();
				}
				catch (IOException e)
				{
					EmpExecutionContext.error(e, "关闭文件异常。");
				}
			}
		}
		return "success";
	}

	/**
	 * @description 写入文本文件    
	 * @param filee 文件对象
	 * @param content 写入内容
	 * @return true- 成功,false-失败
	 * @throws Exception       			 
	 * @author linzhihan <zhihanking@163.com>
	 * @datetime 2013-10-18 下午01:57:52
	 */
	private boolean writeToTxtFile(File filee, String content) throws Exception
	{
		FileOutputStream foss = null;
		try
		{
			foss = new FileOutputStream(filee.getPath(), true);
			ByteArrayInputStream baInput = new ByteArrayInputStream(content.getBytes("GBK"));

			byte[] buffer = new byte[8192];
			int ch = 0;
			while((ch = baInput.read(buffer)) != -1)
			{
				foss.write(buffer, 0, ch);
			}
			baInput.close();
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "写文件异常。");
			throw e;
		}
		finally
		{
			if(foss != null)
			{
				try
				{
					foss.close();
				}
				catch (Exception e)
				{
					EmpExecutionContext.error(e, "关闭文件异常。");
				}
			}
		}
		return true;

	}

	/**
	 * 获取可用的taskid
	 * 
	 * @return 返回taskid
	 */
	public Long getAvailableTaskId()
	{
		try
		{
			Long taskId = GlobalVariableBiz.getInstance().getValueByKey("taskId", 1L);
			boolean result = smsSpecialDAO.checkTaskIdNotUse(taskId);
			if(result)
			{
				return taskId;
			}
			// taskid不可用，则循环找，限制找1000次
			for (int i = 0; i < 1000; i++)
			{
				taskId = GlobalVariableBiz.getInstance().getValueByKey("taskId", 1L);
				result = smsSpecialDAO.checkTaskIdNotUse(taskId);
				if(result)
				{
					EmpExecutionContext.info("获取taskid已被使用，重新获取"+i+"次后成功，获取的taskid:"+taskId);
					return taskId;
				}
			}
			EmpExecutionContext.error("获取taskid失败，已获取1000次均被使用！");
			return null;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取可用的taskid异常。");
			return null;
		}
	}

	/**
	 * 创建年月日层级目录
	 * 
	 * @param physicsUrl
	 *        绝对路径
	 * @param c
	 *        日历对象
	 * @return
	 */
	public String createDir(String physicsUrl, Calendar c)
	{
		// 文件操作类
		TxtFileUtil util = new TxtFileUtil();
		// 年
		int year = c.get(Calendar.YEAR);
		// 月
		int month = c.get(Calendar.MONTH) + 1;
		//
		int day = c.get(Calendar.DAY_OF_MONTH);

		String dirPath = year + "/" + month + "/" + day + "/";
		// 生成目录
		util.makeDir(physicsUrl + dirPath);
		return dirPath;
	}

	/**
	 * 创建年月日层级目录
	 * 
	 * @param physicsUrl
	 *        绝对路径
	 * @param
	 *
	 * @return
	 */
	public String createDir(String physicsUrl)
	{
		// 文件操作类
		Calendar c = Calendar.getInstance();
		return this.createDir(physicsUrl, c);
	}

	/**
	 * 验证是否升级了历史表
	 * 先按新的历史表名去找，若新的历史表不存在，则返回老的历史表名
	 * 
	 * @param tableName
	 *        新的历史表名
	 * @return
	 * @throws Exception
	 */
	public String getTableName(String tableName) throws Exception
	{
		try
		{

			String sql = "";
			String sql1 = "";
			String tableNameOle = tableName.substring(0, 6) + tableName.substring(10);
			switch (StaticValue.DBTYPE)
			{
				case 1:
					// oracle
					sql = "select count(*) ICOUNT from all_tables where table_name=upper('" + tableName + "') and OWNER=upper('" + SystemGlobals.getValue("montnets.emp.user") + "')";
					sql1 = "select count(*) ICOUNT from all_tables where table_name=upper('" + tableNameOle + "') and OWNER=upper('" + SystemGlobals.getValue("montnets.emp.user") + "')";
					break;
				case 2:
					// sqlserver2005
					sql = "select count(*) ICOUNT from sysobjects where id = object_id('" + tableName + "') and type = 'u'";
					sql1 = "select count(*) ICOUNT from sysobjects where id = object_id('" + tableNameOle + "') and type = 'u'";
					break;
				case 3:
					// MYSQL
					sql = "select count(*) ICOUNT from INFORMATION_SCHEMA.TABLES where TABLE_SCHEMA='" + SystemGlobals.getValue("montnets.emp.databaseName") + "' and TABLE_NAME=upper('" + tableName + "')";
					sql1 = "select count(*) ICOUNT from INFORMATION_SCHEMA.TABLES where TABLE_SCHEMA='" + SystemGlobals.getValue("montnets.emp.databaseName") + "' and TABLE_NAME=upper('" + tableNameOle + "')";
					break;
				case 4:
					// DB2
					sql = "select count(*) ICOUNT from syscat.tables where tabname=upper('" + tableName + "')";
					sql1 = "select count(*) ICOUNT from syscat.tables where tabname=upper('" + tableNameOle + "')";
					break;
				default:
					break;
			}
			List<DynaBean> bbs = new SuperDAO().getListDynaBeanBySql(sql);
			List<DynaBean> bbs1 = new SuperDAO().getListDynaBeanBySql(sql1);
			int i = 0;
			int j = 0;
			for (DynaBean db : bbs)
			{
				if(db.get("icount") != null)
				{
					String count = db.get("icount").toString();
					if(!"0".equals(count))
					{
						i = 1;
					}
				}
			}

			for (DynaBean db : bbs1)
			{
				if(db.get("icount") != null)
				{
					String count = db.get("icount").toString();
					if(!"0".equals(count))
					{
						j = 1;
					}
				}
			}

			if(i == 0 && j == 0)
			{
				return "";
			}
			else
				if(i == 1)
				{
					return tableName;
				}
				else
					if(i == 0 && j == 1)
					{
						return tableNameOle;
					}

		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取历史表异常。");
			throw e;
		}
		return tableName;
	}

	/**
	 * 上传文件到文件服务器
	 * 
	 * @param mobileUrl
	 *        本地文件相对路径
	 * @return
	 */
	public boolean upFileToFileServer(String mobileUrl)
	{
		try
		{
			// 判断是否使用集群
			if(StaticValue.getISCLUSTER() == 1)
			{
				CommonBiz commBiz = new CommonBiz();
				// 上传文件到文件服务器
				if(!"success".equals(commBiz.uploadFileToFileCenter(mobileUrl , false)))
				{
					return false;
				}
			}
			return true;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "上传文件到文件服务器失败！");
			return false;
		}
	}

	/**
	 * 验证Map
	 * 
	 * @param infoMap
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> checkMapNull(Map<String, String> infoMap, Long userid, String corpCode)
	{
		try
		{
			if(infoMap == null)
			{
				infoMap = new HashMap<String, String>();
			}
			// 用户编码
			if(infoMap.get("userCode") == null)
			{
				// 获取用户对象
				LfSysuser sysuser = empDao.findObjectByID(LfSysuser.class, userid);
				infoMap.put("userCode", sysuser.getUserCode());
			}
			// 计费开关
			if(infoMap.get("feeFlag") == null)
			{
				int isDepCharging = SystemGlobals.isDepCharging(corpCode);
				if(isDepCharging == 1)
				{
					infoMap.put("feeFlag", "true");
				}
				else
					if(isDepCharging == 0)
					{
						infoMap.put("feeFlag", "false");
					}
					else
					{
						return null;
					}
			}

			return infoMap;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "验证发送缓存信息失败！");
			return null;
		}
	}

	/**
	 *   
	 * @description    通过taskid去查询lfmttask表记录
	 * @param taskId	任务ID
	 * @return       	lfmttask对象			 
	 * @author yejiangmin <282905282@qq.com>
	 * @datetime 2013-10-18 上午09:40:11
	 */
	public LfMttask getLfMttaskbyTaskId(Long taskId){
		LfMttask mttask = null;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try{
			conditionMap.put("taskId", String.valueOf(taskId));
			List<LfMttask> mttaskList = empDao.findListByCondition(LfMttask.class, conditionMap, null);
			if(mttaskList != null && mttaskList.size()>0){
				mttask = mttaskList.get(0);
			}
		}catch (Exception e) {
			mttask = null;
			EmpExecutionContext.error(e, "通过任务ID查询lfmttask表出现异常！");
		}
		return mttask;
	}

	
	/**
	 * 查询机构员工信息
	 * @description    
	 * @param epname 员工姓名
	 * @param depId 机构编号
	 * @param lgcorpcode 企业编号
	 * @param pageinfo 分页
	 * @return       机构员工信息  			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-5-22 下午01:11:39
	 */
	public List<DynaBean> getDepLoyee(String epname, String depId, String lgcorpcode,PageInfo pageinfo)
	{
		List<DynaBean> beanList = null;
		
		try
		{
			beanList = new DepSpecialDAO().findDepLoyee(epname, depId, lgcorpcode, pageinfo);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "查询机构员工异常！depId:" + depId+"，epname："+epname+"，lgcorpcode：" +lgcorpcode);
		}
		return beanList;
	}
	
	/**
	 * 设置发送信息至session
	 * @description    
	 * @param request
	 * @param response       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-7-25 下午05:55:46
	 */
	public void setSendInfo(HttpServletRequest request, HttpServletResponse response, String corpCode, Long userId)
	{
		try
		{
			HttpSession session = request.getSession(false);
			// 获取当前登录的对象
			Map<String, String>infoMap = new HashMap<String, String>();
			// 用户账号
			String userCode = new SysuserBiz().getUserCode(userId);
			if(userCode != null)
			{
				infoMap.put("userCode", userCode);
			}
			// 机构是否计费
			infoMap.put("feeFlag", String.valueOf(SystemGlobals.isDepBilling(corpCode)));
			// 存放session
			session.setAttribute("infoMap", infoMap);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置发送信息至SESSION异常!");
		}
	}
	
	/**
	 * 是否配置数据库信息
	 * @return 0：未配置数据库；1：已配置数据库；-1：异常
	 */
	public int isConfigDB(){
		try
		{
			ResourceBundle rb = ResourceBundle.getBundle("SystemGlobals");
			String connectDataBaseFlag=rb.getString("ConnectDataBaseFlag");
			//未配置数据库
			if("0".equals(connectDataBaseFlag)){
				return 0;
			}
			else{
				return 1;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "是否没配置数据库信息异常。");
			return -1;
		}
	}

	
	/**
	 * 通过KEY获取全局参数对象
	 * @description    
	 * @param key  键值
	 * @return  全局参数对象 ，异常或无数据返回null   			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-10-15 上午11:26:45
	 */
	public LfGlobalVariable getGlobalVariable(String key)
	{
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		LfGlobalVariable lfGlobalVariable= null;
		try{
			conditionMap.put("globalKey", key);
			List<LfGlobalVariable> lfGlobalVariableList = empDao.findListByCondition(LfGlobalVariable.class, conditionMap, null);
			if(lfGlobalVariableList != null && lfGlobalVariableList.size() > 0)
			{
				lfGlobalVariable = lfGlobalVariableList.get(0);
			}
		}
		catch (Exception e)
		{
			lfGlobalVariable = null;
			EmpExecutionContext.error(e, "通过KEY获取全局参数对象异常！key："+key);
		}
		return lfGlobalVariable;
	}

    /**
     * 获取用户对应发送功能保存的草稿箱
     * @param condMap
     * @param pageInfo
     * @return
     */
    public List<DynaBean> getUserDrafts(LinkedHashMap<String,String> condMap,PageInfo pageInfo){
        return smsSpecialDAO.getUserDrafts(condMap,pageInfo);
    }

    /**
     * 来自短链的草稿箱
     * @param condMap
     * @param pageInfo
     * @return
     */
    public List<DynaBean> getUserDraftsFromShortUrl(LinkedHashMap<String,String> condMap,PageInfo pageInfo) {
        return smsSpecialDAO.getUserDraftsFromShortUrl(condMap,pageInfo);
    }


    //切换文件服务器
    public static synchronized void switchFileServer(int index)
    {
        //if(index != StaticValue.FILE_SERVER_INDEX)
		if(index != StaticValue.getFileServerIndex())
        {
            //切换到下一个
           // StaticValue.FILE_SERVER_INDEX = index;
            //StaticValue.FILE_SERVER_URL = StaticValue.FILE_SERVER_QUEUE.get(index);
            //StaticValue.FILE_SERVER_VIEWURL = StaticValue.FILE_SERVER_OUTER_QUEUE.get(index);
			StaticValue.setFileServerIndex(index);
			StaticValue.setFileServerUrl(StaticValue.getFileServerQueue().get(index));
			StaticValue.setFileServerViewurl(StaticValue.getFileServerOuterQueue().get(index));

            //网关参数文件服务器地址调整
            Map prop1 = WebgatePropInfo.getProp();
            String[] props = (String[]) prop1.get("webgateProp");
            if(props.length >0)
            {
               // props[0] = StaticValue.FILE_SERVER_URL;
                props[0] = StaticValue.getFileServerUrl();

            }
            //EmpExecutionContext.info("切换到文件服务器["+StaticValue.FILE_SERVER_URL+"]");
            EmpExecutionContext.info("切换到文件服务器["+StaticValue.getFileServerUrl()+"]");

        }

    }

    /**
     * 切换文件服务器（外网）
     * @description    
     * @param index       			 
     * @author chentingsheng <cts314@163.com>
     * @datetime 2016-11-29 上午09:18:37
     */
    public static synchronized void switchFileServerOut(int index)
    {
        try
		{
        	//不是上一个使用的文件服务器地址
			//if(index != StaticValue.FILE_SERVER_OUTER_INDEX)
			if(index != StaticValue.getFileServerOuterIndex())
			{
			    //切换到下一个
			    //StaticValue.FILE_SERVER_OUTER_INDEX = index;
			    //重新设置可使用的外网服务器地址
			   // StaticValue.FILE_SERVER_OUTER_URL = StaticValue.FILE_SERVER_OUTER_QUEUE.get(index);
			    //EmpExecutionContext.info("切换到外网文件服务器["+StaticValue.FILE_SERVER_OUTER_URL+"]");
				StaticValue.setFileServerOuterIndex(index);
				StaticValue.setFileServerOuterUrl(StaticValue.getFileServerOuterQueue().get(index));
				EmpExecutionContext.info("切换到外网文件服务器["+StaticValue.getFileServerOuterUrl()+"]");
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "切换外网文件服务器异常！");
		}

    }
    
    /**
     * 获取可用当前的文件服务器地址（若没有可用的 则返回主文件服务器地址）
     * @return 数组 0：服务器内网地址   1：服务器外网地址
     */
    public static String[] getALiveFileServer()
    {
        //检测间隔在周期之内 直接返回 上次检测结果
        if(StaticValue.getFileServerCheckAliveTime() > 0 && System.currentTimeMillis() - StaticValue.getFileServerCheckAliveTime() < StaticValue.FILE_SERVER_CHECK_ALIVE_PERIOD)
        {
            //return StaticValue.FILE_SERVER_CHECK_ALIVE_RESULT;
			return StaticValue.getFileServerCheckAliveResult();
        }

        int len = StaticValue.getFileServerSize();
        //文件服务器索引数组
        int[] indexs = new int[len];
        for (int i = 0;i<len;i++)
        {
            indexs[i] = i;
        }
       // int index = StaticValue.FILE_SERVER_INDEX;
		int index = StaticValue.getFileServerIndex();

        //如果当前文件服务器为备用服务器 且不为第一个备用地址 则交换两者位置 判断顺序为 主服务器>当前服务器>其他服务器
        if(index > 1)
        {
            int tmp = indexs[index];
            indexs[index] = indexs[1];
            indexs[1] = tmp;
        }

        //每个文件服务器最多检测次数
        int tryTimes = 1;
        int _index = index;
        //外部循环控制
        int i = 0;
        String httpUrl;
        boolean isPass = false;
        while (!isPass && i < len)
        {
            httpUrl = StaticValue.getFileServerQueue().get(indexs[i]);
            for(int j = 0;j < tryTimes;j++)
            {
                if(HttpUtil.checkState(httpUrl) < 400)
                {
                    _index = indexs[i];
                    isPass = true;
                    break;
                }
            }
            i++;
        }

        switchFileServer(_index);

        StaticValue.setFileServerCheckAliveTime(System.currentTimeMillis());
        //若文件服务器全部连接不上则返回空
        if(!isPass)
        {
            //StaticValue.FILE_SERVER_CHECK_ALIVE_RESULT = null;
			StaticValue.setFileServerCheckAliveResult(null);
        }
        else
        {
            //StaticValue.FILE_SERVER_CHECK_ALIVE_RESULT = new String[]{StaticValue.FILE_SERVER_URL,StaticValue.FILE_SERVER_VIEWURL};
			StaticValue.setFileServerCheckAliveResult(new String[]{StaticValue.getFileServerUrl(),StaticValue.getFileServerViewurl()});
        }

       // return StaticValue.FILE_SERVER_CHECK_ALIVE_RESULT;
        return StaticValue.getFileServerCheckAliveResult();

    }

    /**
     * 获取可用当前的外网文件服务器地址
     * @description    
     * @return       			 
     * @author chentingsheng <cts314@163.com>
     * @datetime 2016-11-29 上午08:49:22
     */
    public static String getALiveFileServerOut(String url)
    {
        try
		{
			//检测间隔在周期之内 直接返回 上次检测结果
			if(StaticValue.getFileServerOuterCheckAliveTime() > 0 && System.currentTimeMillis() - StaticValue.getFileServerOuterCheckAliveTime() < StaticValue.FILE_SERVER_CHECK_ALIVE_PERIOD)
			{
			    //return StaticValue.FILE_SERVER_OUTER_CHECK_ALIVE_RESULT;
				return StaticValue.getFileServerOuterCheckAliveResult();
			}

			int len = StaticValue.getFileServerOuterQueue().size();
			//文件服务器索引数组
			int[] indexs = new int[len];
			for (int i = 0;i<len;i++)
			{
			    indexs[i] = i;
			}
			//int index = StaticValue.FILE_SERVER_OUTER_INDEX;
			int index = StaticValue.getFileServerOuterIndex();

			//如果当前文件服务器为备用服务器 且不为第一个备用地址 则交换两者位置
			if(index > 1)
			{
			    int tmp = indexs[index];
			    indexs[index] = indexs[1];
			    indexs[1] = tmp;
			}

			//每个文件服务器最多检测次数
			int tryTimes = 1;
			int _index = index;
			//外部循环控制
			int i = 0;
			String httpUrl;
			boolean isPass = false;
			while (!isPass && i < len)
			{
				//遍历外网文件服务器
			    httpUrl = StaticValue.getFileServerOuterQueue().get(indexs[i]);
			    for(int j = 0;j < tryTimes;j++)
			    {
			    	//文件服务器文件是否存在
			        if(HttpUtil.checkState(httpUrl+url) < 400)
			        {
			            _index = indexs[i];
			            isPass = true;
			            break;
			        }
			    }
			    i++;
			}

			switchFileServerOut(_index);

			StaticValue.setFileServerOuterCheckAliveTime(System.currentTimeMillis());
			//若文件服务器全部连接不上则返回空
			if(!isPass)
			{
			    //StaticValue.FILE_SERVER_OUTER_CHECK_ALIVE_RESULT = null;
				StaticValue.setFileServerOuterCheckAliveResult(null);
			}
			else
			{
			   // StaticValue.FILE_SERVER_OUTER_CHECK_ALIVE_RESULT = StaticValue.FILE_SERVER_OUTER_URL;
				StaticValue.setFileServerOuterCheckAliveResult(StaticValue.getFileServerOuterUrl());
			}

			//return StaticValue.FILE_SERVER_OUTER_CHECK_ALIVE_RESULT;
			return StaticValue.getFileServerOuterCheckAliveResult();
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取可用当前的外网文件服务器地址失败。");
			return null;
		}
    }

    /**
     *
     * @description 异步入库
     * @param obj 实体类对象
     * @return 成功返回true
     */
    public boolean asyncSaveEntity(Object obj)
    {
        String sql = commDataDao.getEntityInsertSQL(obj);
        if(sql == null || sql.trim().length() < 1)
        {
            return false;
        }
        CommDataDealStorage dataStorage = CommDataDealStorage.getInstance();
        return dataStorage.produce(sql);
    }

    /**
     * 检测文件是否存在与文件服务器或者EMP节点 并返回对应的节点地址
     * @param url 发送文件路径相对地址
     * @return 存在文件节点的地址
     */
    public String checkServerFile(String url)
    {
        boolean result = false;
        TxtFileUtil tfu = new TxtFileUtil();
        String httpUrl = StaticValue.BASEURL;
        try {
            //非集群判断本地文件存在
            if(StaticValue.getISCLUSTER() == 0)
            {
                //本地是否存在该文件
                result = tfu.checkFile(url);
            }//集群 优先从文件服务器获取
            else
            {
                String[] httpUrls = CommonBiz.getALiveFileServer();

                if(httpUrls != null)
                {
                    httpUrl = httpUrls[0];
                    if( HttpUtil.checkState(httpUrl+url) < 400)
                    {
                        result = true;
                    }
                }

                if(!result)
                {
                    httpUrl = checkFile(url);
                    if(httpUrl != null)
                    {
                        result = true;
                    }
                }
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e,"检测定时任务发送文件存在异常！");
        }

        return result?httpUrl:null;
    }

    /**
     * 上传文件到其他emp节点
     * @param url
     * @return
     */
    public String uploadFileToOtherNodes(String url)
    {
        final String _url = url;
        String result = "success";
        getExecutorService().execute(new Runnable() {
            public void run() {
                try {
                    List<LfNodeBaseInfo> nodeInfoList = new GenericEmpDAO().findListByCondition(LfNodeBaseInfo.class, null, null);
                    if(nodeInfoList != null && nodeInfoList.size() >0)
                    {
                        String base = null;
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("url",_url);
                        map.put("method","download");
                        String nodeId = null;
                        //上传失败数
                        int errors = 0;
                        //文件节点来源
                        String fileOrgi = new TxtFileUtil().smsFileServerNum(_url);
                        for (LfNodeBaseInfo nodeBaseInfo : nodeInfoList)
                        {
                            nodeId = nodeBaseInfo.getNodeId();
                            if(nodeId.equals(fileOrgi))
                            {
                                continue;
                            }
                            base = nodeBaseInfo.getSerLocalURL();
                            if("success".equals(HttpUtil.post(base+"emp_tz.hts",map)))
                            {
                                EmpExecutionContext.info("同步文件["+_url+"]到节点"+nodeId+"成功！");
                            }
                            else
                            {
                                errors++;
                                EmpExecutionContext.error("同步文件["+_url+"]到节点"+nodeId+"失败！");
                            }
                        }

                        String filePath = _url.replaceFirst("(.*)?/file/","file/");
                        if(errors > 0)
                        {
                            EmpExecutionContext.error("同步文件到其他节点未能全部成功，启用存库策略！");

                            saveFile2DB(filePath);
                        }
                        else//所有节点分发成功 若数据库存在该文件则删除
                        {
                            deleteFileFromDB(filePath);
                        }
                    }
                }
                catch (Exception e)
                {
                    EmpExecutionContext.error(e,"同步文件["+_url+"]到其他emp节点异常！");
                }
            }
        });

        return result;
    }


    /**
     * 保存文件到数据库
     * @param fileUrl
     * @return
     */
    public boolean saveFile2DB(String fileUrl)
    {
        boolean result = false;
        Connection connection = null;
        FileInputStream fin = null;
        ByteArrayOutputStream out = null;
        PreparedStatement ps = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            connection = empTransDao.getConnection();
            out = new ByteArrayOutputStream();
            st = connection.createStatement();

            ps = connection.prepareStatement("SELECT count(*) FROM LF_FILEDATA WHERE FILEURL = ?");
            ps.setString(1,fileUrl);
            rs = ps.executeQuery();

			//rs = st.executeQuery("SELECT count(*) FROM LF_FILEDATA WHERE FILEURL = '"+fileUrl+"'");
            if(rs.next())
            {
                if(rs.getLong(1) > 0)
                {
                    EmpExecutionContext.info("文件["+fileUrl+"]已在数据库中！无需同步！");
                    return true;
                }
            }
            ps = connection.prepareStatement("INSERT INTO LF_FILEDATA(FILEURL,FSDATA,NODEID) VALUES (?,?,?)");
            ps.setString(1, fileUrl);
            String url = new TxtFileUtil().getPhysicsUrl(fileUrl);
            File file = new File(url);

            if(!file.exists())
            {
                return false;
            }

            fin = new FileInputStream(file);
            int size = 0;
            byte[] buffer = new byte[1024];
            while((size=fin.read(buffer))!=-1){
                out.write(buffer, 0, size);
            }

            ps.setBytes(2, out.toByteArray());
            ps.setString(3, StaticValue.getServerNumber());
            ps.executeUpdate();
            ps.clearParameters();
            result = true;
            EmpExecutionContext.info("文件["+fileUrl+"]同步入库成功！");
        } catch (Exception e)
        {
            EmpExecutionContext.error(e,"保存文件["+fileUrl+"]二进制到数据库异常！");
        }
        finally
        {
            if(ps != null)
            {
                try {
                    ps.close();
                } catch (SQLException e) {
                    EmpExecutionContext.error(e, "关闭预编译sql对象异常。");
                }
            }
            if(out != null)
            {
                try {
                    out.close();
                } catch (IOException e) {
                    EmpExecutionContext.error(e, "关闭输出流异常。");
                }
            }
            if(fin != null)
            {
                try {
                    fin.close();
                } catch (IOException e) {
                    EmpExecutionContext.error(e, "关闭输入流异常。");
                }
            }
			if(st != null){
				try{
					st.close();
				}catch(Exception e){
					EmpExecutionContext.error(e, "关闭资源异常");
				}
			}
			if(rs != null){
				try{
					rs.close();
				}catch(Exception e){
					EmpExecutionContext.error(e, "关闭资源异常");
				}
			}
            empTransDao.closeConnection(connection);
        }
        return result;
    }

    /**
     * 从数据库读取文件信息 并保存到当前节点
     * @param fileUrl
     * @return
     */
    public boolean loadFileFromDB(String fileUrl)
    {
        Connection connection = null;
       PreparedStatement ps = null;
        ResultSet resultSet = null;
        BufferedInputStream bis = null;
        FileOutputStream os = null;
        boolean result = false;
        try
        {
            connection = empTransDao.getConnection();
            String sql = "SELECT FSDATA FROM  LF_FILEDATA WHERE FILEURL = ? ";
            ps = connection.prepareStatement(sql);
            ps.setString(1,fileUrl);
            resultSet = ps.executeQuery();
            if(resultSet.next())
            {
                String url = new TxtFileUtil().getPhysicsUrl(fileUrl);
                File file = new File(url);
                File parent = file.getParentFile();
                if(!parent.exists())
                {
                    parent.mkdirs();
                }
                bis = new BufferedInputStream(resultSet.getBlob(1).getBinaryStream());
                os = new FileOutputStream(file);
                byte buffer[] = new byte[4 * 1024];
                int size = 0;
                while((size = bis.read(buffer)) != -1)
                {
                    os.write(buffer, 0, size);
                }
                os.flush();
                result = true;
            }
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e,"数据库读取文件并保存异常！");
        }
        finally
        {
            if(os != null)
            {
                try {
                    os.close();
                } catch (IOException e) {
                    EmpExecutionContext.error(e, "关闭输出流异常");
                }
            }
            if(bis != null)
            {
                try {
                    bis.close();
                } catch (IOException e) {
                    EmpExecutionContext.error(e, "关闭输入流异常。");
                }
            }
            if(resultSet != null)
            {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    EmpExecutionContext.error(e, "关闭结果集对象异常。");
                }
            }
            if(ps != null)
            {
                try {
                    ps.close();
                } catch (SQLException e) {
                    EmpExecutionContext.error(e, "关闭预处理对象异常。");
                }
            }
            empTransDao.closeConnection(connection);
        }
        return result;
    }

    /**
     * 从数据库删除文件
     * @param fileUrl
     * @return
     */
    public boolean deleteFileFromDB(String fileUrl)
    {
        boolean result = false;
        Connection connection = empTransDao.getConnection();
        String sql = "DELETE FROM LF_FILEDATA WHERE FILEURL  = '"+fileUrl+"'";
        try
        {
            result = empTransDao.executeBySQLReturnCount(connection,sql) > 0;
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e,"数据库删除文件["+fileUrl+"]异常！");
        }finally{
			//关闭数据库连接
			empTransDao.closeConnection(connection);
		}

        if(result)
        {
            EmpExecutionContext.info("数据库删除文件["+fileUrl+"]成功！");
        }

        return result;
    }


    /**
     * 从非文件服务器以外的地方检查文件 并返回存在的节点地址
     * @param url
     * @return
     */
    public String checkFile(String url)
    {
        String httpUrl = null;
        boolean result = false;
        String errNodes = "文件服务器";
        try {
            //文件服务器下载失败 则从EMP节点获取文件
            LinkedHashMap<String,String> orderMap = new LinkedHashMap<String, String>();
            orderMap.put("nodeId",StaticValue.ASC);
            List<LfNodeBaseInfo> nodeInfoList = new GenericEmpDAO().findListByCondition(LfNodeBaseInfo.class, null, orderMap);
            Map<String,String> nodeInfoMap = new LinkedHashMap<String, String>();
            if(nodeInfoList != null && nodeInfoList.size() >0)
            {
                for (LfNodeBaseInfo nodeBaseInfo : nodeInfoList)
                {
                    nodeInfoMap.put(nodeBaseInfo.getNodeId(),nodeBaseInfo.getSerLocalURL());
                }
                //文件节点来源
                String fileOrgi = new TxtFileUtil().smsFileServerNum(url);
                String key = null;
                if(fileOrgi != null)
                {
                    key = fileOrgi;
                    httpUrl = nodeInfoMap.get(key);
                    if(httpUrl != null && HttpUtil.checkState(httpUrl+url) < 400)
                    {
                        result = true;
                    }
                }
                if(!result)
                {
                    if(key != null)
                    {
                        errNodes += "、"+key;
                    }
                    Iterator<String> its = nodeInfoMap.keySet().iterator();
                    while (!result && its.hasNext())
                    {
                        key = its.next();
                        if(key.equals(fileOrgi))
                        {continue;}
                        httpUrl = nodeInfoMap.get(key);
                        if(HttpUtil.checkState(httpUrl+url) < 400)
                        {
                            result = true;
                        }
                        else
                        {
                            errNodes += "、"+key;
                        }
                    }
                }


                //节点未找到 则从数据库查找
                if(!result)
                {
                    key = "数据库";
                    if(loadFileFromDB(url))
                    {
                        httpUrl = StaticValue.BASEURL;
                        int max = 20;
                        while (max-- > 0 && HttpUtil.checkState(httpUrl+url) >= 400)
                        {
                            Thread.sleep(500);
                        }
                        if(max > -1)
                        {
                            result = true;
                        }
                    }
                    else
                    {
                        errNodes += "、"+key;
                    }
                }


                //在EMP节点找到文件 则上传到文件服务器
                if(result)
                {
                    //上传到文件服务器
                    uploadNodeFileToFileCenter(url,httpUrl);
                }
                EmpExecutionContext.error("文件["+url+"]依次在"+errNodes+"未找到！"+(result?(key+"查找成功！"):""));
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e,"从非文件服务器查找文件["+url+"]失败！");
        }
        return result?httpUrl:null;
    }

    synchronized public ExecutorService getExecutorService() {
        if (exec == null) {
            /**
             * corePoolSize： 线程池维护线程的最少数量
             maximumPoolSize：线程池维护线程的最大数量
             keepAliveTime： 线程池维护线程所允许的空闲时间
             unit： 线程池维护线程所允许的空闲时间的单位
             workQueue： 线程池所使用的缓冲队列
             */
            exec = new ThreadPoolExecutor(1, 20, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        }
        return exec;
    }
    
    
	/**
	 * 
	 * @description 获取有权限看的操作员编码
	 * @param sysUser 操作员对象
	 * @return 返回有权限看的操作员编码，格式为code1,code2；不需要考虑权限则返回空字符串，目前只有100000企业、admin和管辖范围是顶级机构不需要考虑权限；编码超过1000个、异常返回null
	 * @author huangzb <huangzb@126.com>
	 * @datetime 2016-1-18 下午03:22:57
	 */
	public String getPermissionUserCode(LfSysuser sysUser)
	{
		if("100000".equals(sysUser.getCorpCode()))
		{
			return "";
		}
		//如果是admin管理员，则默认查全部
		else if("admin".equals(sysUser.getUserName()))
		{
			return "";
		}
		//如果是个人权限，则只能查自己的。权限类型 1：个人权限  2：机构权限
		else if(sysUser.getPermissionType() == 1)
		{
			//返回当前操作员的编码
			return "'" + sysUser.getUserCode() + "'";
		}
		
		//机构权限，则需要查询出当前操作员可管辖的所有操作员。
		try
		{
			//如果当前操作员的管辖机构即为企业最顶级的机构，则不需要拼操作员编码
			boolean domIsTopDep = checkDomIsTopDep(sysUser);
			if(domIsTopDep)
			{
				return "";
			}
			
			LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
			orderbyMap.put("userCode", StaticValue.ASC);
			//查询有权限看的操作员
			List<LfSysuser> userList = empDao.findListBySymbolsCondition(sysUser.getUserId(), LfSysuser.class, null, orderbyMap);
			//没找到其他操作员，那就只能看他自己的
			if(userList == null || userList.size() < 1)
			{
				//返回当前操作员的编码
				return "'" + sysUser.getUserCode() + "'"; 
			}
			//操作员编码超过1000，则不使用in方式拼接查询
			if(userList.size() > 1000)
			{
				return null;
			}
			
			StringBuffer sbUserCode = new StringBuffer();
			for(int i = 0;i < userList.size(); i++)
			{
				sbUserCode.append("'").append(userList.get(i).getUserCode()).append("'");
				if(i < userList.size() - 1)
				{
					sbUserCode.append(",");
				}
			}
			
			return sbUserCode.toString();
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "系统下行记录，获取有权限看的操作员编码，异常"
					+ "。userCode=" + sysUser.getUserCode()
					+ ",userId=" + sysUser.getUserId() 
					+ ",userName=" + sysUser.getUserName()
					+ ",corpCode=" + sysUser.getCorpCode()
					);
			//返回当前操作员的编码
			return null;
		}
	}
	
	/**
	 * 
	 * @description 检查管辖机构是否就是企业顶级机构
	 * @param sysUser 操作员对象
	 * @return true：管辖的机构即为企业顶级机构；false：不是顶级机构，或者没能找到，或者异常。
	 * @author huangzb <huangzb@126.com>
	 * @datetime 2016-1-18 下午02:56:42
	 */
	private boolean checkDomIsTopDep(LfSysuser sysUser)
	{
		//如果当前操作员的管辖机构即为企业最顶级的机构，则不需要拼操作员编码
		
		//获取企业最顶级机构
		LfDep topDep = getTopDep(sysUser.getCorpCode());
		//没能找到企业顶级机构
		if(topDep == null)
		{
			EmpExecutionContext.error("系统下行记录，检查管辖机构是否就是企业顶级机构，获取不到顶级机构对象。corpCode="+sysUser.getCorpCode());
			return false;
		}
		
		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("userId", sysUser.getUserId().toString());
			LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
			orderbyMap.put("depId", StaticValue.ASC);
			List<LfDomination> domList = empDao.findListByCondition(LfDomination.class, conditionMap, orderbyMap);
			if(domList == null || domList.size() < 1)
			{
				return false;
			}
			
			for(LfDomination lfDom : domList)
			{
				//管辖的机构即为企业顶级机构
				if(lfDom.getDepId() == topDep.getDepId())
				{
					return true;
				}
			}
			return false;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "系统下行记录，检查管辖机构是否就是企业顶级机构，异常。");
			return false;
		}
	}
	
	/**
	 * 企业顶级机构map，查询到的企业顶级机构放这里，key为企业编码，value为企业的顶级机构对象。
	 */
	private static ConcurrentHashMap<String,LfDep> topDepMap = new ConcurrentHashMap<String,LfDep>();
	
	/**
	 * 
	 * @description 获取企业顶级机构
	 * @param corpCode 企业编码
	 * @return 企业顶级机构对象，没找到或者异常则返回null
	 * @author huangzb <huangzb@126.com>
	 * @datetime 2016-1-18 下午02:49:50
	 */
	private LfDep getTopDep(String corpCode)
	{
		try
		{
			//企业顶级机构先从内存中拿
			if(topDepMap.get(corpCode) != null)
			{
				return topDepMap.get(corpCode);
			}
			
			//获取企业最顶级机构
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("corpCode", corpCode);
			//顶级机构
			conditionMap.put("depLevel", "1");
			LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
			orderbyMap.put("depId", StaticValue.ASC);
			List<LfDep> depList = empDao.findListByCondition(LfDep.class, conditionMap, orderbyMap);
			if(depList == null || depList.size() < 1)
			{
				EmpExecutionContext.error("系统下行记录，获取企业顶级机构，查询为空。corpCode="+corpCode);
				return null;
			}
			
			//企业顶级机构放到内存中
			topDepMap.put(corpCode, depList.get(0));
			
			return depList.get(0);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "系统下行记录，获取企业顶级机构，异常。");
			return null;
		}
	}
}
