/**
 * Program : GlobalMethods.java
 * Author : chensj
 * Create : 2013-6-9 上午08:52:39
 * company ShenZhen Montnets Technology CO.,LTD.
 */

package com.montnets.emp.ottbase.util;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.entity.wxgl.LfWeiMenu;
import com.montnets.emp.ottbase.constant.WXStaticValue;

/**
 * @author chensj <510061684@qq.com>
 * @version 1.0.0
 * @2013-6-9 上午08:52:39
 *           全局共用的方法
 */
public class GlobalMethods
{

	private static final Logger	logger	= Logger.getLogger("GlobalMethods");

	private GlobalMethods()
	{
	}

	/**
	 * 判断是否为无效的字符串
	 * 
	 * @author chensj
	 * @create 2013-6-9 上午08:54:56
	 * @since undefineds null "" -->true
	 * @param arg
	 * @return
	 */
	public static boolean isInvalidString(String arg)
	{
		return (arg == null || "".equals(arg) || "undefined".equals(arg)) ? true : false;
	}

	/**
	 * 判断一个对象是否为空
	 * 
	 * @param o
	 *        任意的java对象
	 * @return true || false
	 */

	public static Long strToLong(String s)
	{
		if(!isInvalidString(s))
		{
			return Long.parseLong(s);
		}
		return null;
	}

	public static Integer strToInteger(String s)
	{
		if(!isInvalidString(s))
		{
			return Integer.parseInt(s);
		}
		return null;
	}

	public static boolean deleteFile(String url)
	{
		boolean b = false;
		if(!isInvalidString(url))
		{
			String basePath = new TxtFileUtil().getWebRoot();
			java.io.File file = new java.io.File(basePath + url);
			logger.info(basePath + url);
			if(file.exists())
			{
				b = file.delete();
			}
			else
			{
				logger.info(basePath + url + "改路径下的文件不存在！");
			}
		}
		return b;
	}
	/**
	 * @description    判断集合类是否为空或null
	 * @param cet
	 * @return       			 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2013-9-26 下午02:51:49
	 */
	public static boolean isNullList(Collection<LfWeiMenu> cet)
	{
		return (cet != null && cet.size() > 0) ? false : true;
	}
	
	/**
     * @description    判断集合类是否为空或null
     * @param cet
     * @return                   
     * @author zousy <zousy999@qq.com>
     * @datetime 2013-9-26 下午02:51:49
     */
    public static boolean isNotNullList(List listObj)
    {
        return (listObj == null || listObj.size() > 0) ?  true : false;
    }
    
	/**
	 * @description  判断数组是否为空或null  
	 * @param obj
	 * @return       			 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2013-9-26 下午02:52:21
	 */
	public static boolean isNullStrArray(String[] obj)
	{
		return (obj != null && obj.length > 0) ? false : true;
	}

	/**
	 * @description   获取随机的长整形数据 
	 * @return       			 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2013-9-26 下午02:53:10
	 */
	public static long getRoundLong()
	{
		return Math.round(Math.random() * 8999 + 1000);
	}

	/**
	 * 将<![CDATA[subscribe]]>格式的标签转化成 < ![CDATA[subscribe]] >格式，
	 * 否则微信接口那边的weixBiz.getParamsXmlMap(xxx)方法会报错
	 * 
	 * @param content
	 * @return
	 */
	public static String handleSpecialTag(String content)
	{
		if(content == null)
			return "";
		if(content.contains("]]>"))
		{
			content = content.trim().replace("]]>", "]] >");
		}
		return content;
	}

	/**
	 * 根据本地测试机的IP，找到对应的外网IP
	 * msgXML中的{basePath}都需要替换成这个值
	 * 这个地方很重要，不然手机上的图片无法显示或者是链接到EMP地址无效
	 * 目前只有WeixCommSvt中用到
	 * 
	 * @param basePath
	 * @return
	 */
	public static String getWeixBasePath()
	{
		String basePath = SystemGlobals.getValue("wx.pageurl");
		if(!basePath.endsWith("/"))
		{
			basePath = basePath + "/";
		}
		return basePath;
	}

	
	/**
	 * 文件服务器地址
	 * @description    
	 * @param basePath
	 * @return       			 
	 * @author Administrator <foyoto@gmail.com>
	 * @datetime 2013-10-16 下午05:28:29
	 */
	public static String getWeixFilePath()
	{
		String filePath ="";
		if(WXStaticValue.ISCLUSTER ==1){
			 String[] httpUrls = CommonBiz.getALiveFileServer();
			filePath = httpUrls[1];//WXStaticValue.FILE_SERVER_VIEWURL;
		}else{
			filePath = getWeixBasePath();
		}
		return filePath;
	}
}
