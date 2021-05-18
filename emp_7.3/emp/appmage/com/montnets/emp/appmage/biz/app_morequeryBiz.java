/**
 * Program  : app_homeeditDao.java
 * Author   : zousy
 * Create   : 2014-6-13 上午08:40:09
 * company ShenZhen Montnets Technology CO.,LTD.
 *
 */

package com.montnets.emp.appmage.biz;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.montnets.emp.common.tools.SysuserUtil;
import org.apache.commons.beanutils.DynaBean;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.json.JSONException;
import org.json.JSONObject;

import com.montnets.emp.appmage.dao.app_morequeryDao;
import com.montnets.emp.appmage.util.FFmpegKit;
import com.montnets.emp.appwg.biz.WgMwFileBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.util.PageInfo;


/**
 * 
 * @author   zousy <zousy999@qq.com>
 * @version  1.0.0
 * @2014-6-13 上午08:40:09
 */
public class app_morequeryBiz extends SuperBiz
{
	private static Map<String, String> emoMap = null;
	private static String facePathString = "/appmage/morequery/emo/face_config.xml";
	app_morequeryDao morequeryDao = new app_morequeryDao();
	public List<DynaBean> getMoList(LinkedHashMap<String, String> conditionMap,PageInfo pageInfo) throws Exception{
		return morequeryDao.getMoList( conditionMap, pageInfo);
	}
	
	private static void parseXml(File file) {
		emoMap = new HashMap<String, String>();
		if(file == null || file.isDirectory() || !file.exists()){
			return ;
		}
		// 输入流
		InputStream inputStream = null;
		try
		{
			inputStream = new FileInputStream(file);
			// 通过SAX解析输入流
			SAXReader reader = new SAXReader();
			Document document = reader.read(inputStream);
			// 得到根元素
			Element root = document.getRootElement();
			// 得到statisticalList元素节点
			Element element = root.element("statisticalList");
			//得到表情列表
			List<Element> elementList = element.elements();
			
			// 遍历所有子节点
			for (Element e : elementList) {
				emoMap.put(e.attributeValue("enName"), e.attributeValue("src"));
			}
			
			// 释放资源
			inputStream.close();
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "解析表情配置文件异常！");
		}finally {
			SysuserUtil.closeStream(inputStream);
		}
	}
	
	public static synchronized Map<String, String> getEmoMap(String basePath){
		if(emoMap == null){
			parseXml(new File(basePath+facePathString));
		}
		return emoMap;
	}
	
	public static JSONObject getJson(int msgType,String json){
		Map<Integer, String> keyMap = new HashMap<Integer, String>();
		keyMap.put(0, "PMessageStyle1");//文字
		keyMap.put(1, "PMessageStyle2");//图片
		keyMap.put(2, "PMessageStyle4");//视频
		keyMap.put(3, "PMessageStyle3");//音频
		JSONObject msgObj = null;
		try
		{
			if(json != null){
				//处理换行符
				json = json.replaceAll("\r\n", "\\n").replaceAll("\n", "\\n");
				JSONObject jsonObj = new JSONObject(json);
				jsonObj = jsonObj.getJSONObject("pMessageStyles");
				msgObj = jsonObj.getJSONObject(keyMap.get(msgType));
			}
		}
		catch (JSONException e)
		{
			EmpExecutionContext.error(e, "解析json数据出现异常！");
		}
		
		return msgObj;
	}
	
	/**
	 * 下载图片
	 * @description    
	 * @param basePath
	 * @param url       			 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2014-6-25 下午03:12:16
	 */
	public String downloadIfNotExist(String basePath,String url){
		if(url == null) return null;
		url = getRelativeUrl(url);
		String savePath = "file/"+url;
		File saveFile = new File(basePath,savePath);
		if(!saveFile.getParentFile().exists()){
			saveFile.getParentFile().mkdirs();
		}
		if(!saveFile.exists()){
			new WgMwFileBiz().downloadFromMwFileSer(saveFile.getPath(), url);
		}
		return savePath;
	}
	
	/**
	 * 下载声音和视频
	 * @description    
	 * @param basePath
	 * @param url
	 * @param type       			 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2014-6-25 下午03:12:27
	 */
	public String downloadIfNotExist(String basePath,String url,int type){
		if(url != null){
			url = getRelativeUrl(url);
			String savePath = "file/"+url;
			File saveFile = new File(basePath,savePath);
			if(!saveFile.getParentFile().exists()){
				saveFile.getParentFile().mkdirs();
			}
			
			File prevFile = new File(basePath,FFmpegKit.convertPath(savePath));
			//预览文件不存在
			if(!prevFile.exists()){
				//原始文件不存在
				if(!saveFile.exists()){
					new WgMwFileBiz().downloadFromMwFileSer(saveFile.getPath(), url);
				}
				if(type == 2){
					FFmpegKit.converVideo(saveFile.getPath());
				}else if(type == 3){
					FFmpegKit.converAudio(saveFile.getPath());
				}
			}
			return savePath;
		}
		return null;
	}
	
	/**
	 * 返回app消息中文件服务器资源地址的相对路径
	 * @description    
	 * @param url
	 * @return       			 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2014-7-5 下午01:58:06
	 */
	public String getRelativeUrl(String url){
		if(url == null){return null;}
		String downloadDir = new WgMwFileBiz().getFileSvrDownSvt();
		int index = 0;
		if(url.indexOf(downloadDir)!=-1){
			index = url.indexOf(downloadDir)+downloadDir.length()+1;
		}
		
		if(url.length()-1<=index){
			return null;
		}
		return url.substring(index);
	}
	
	public static void main(String[] args) throws Exception
	{
		parseXml(new File("C:\\Users\\Administrator\\Desktop\\emo\\face_config.xml"));
	}

}

