/**
 * Program  : PropertiesUtil.java
 * Author   : Administrator
 * Create   : 2013-9-17 上午09:47:56
 * company ShenZhen Montnets Technology CO.,LTD.
 *
 */

package com.montnets.emp.weix.common.util;

import java.io.IOException;
import java.util.Properties;

/**
 * 读取配置文件message.properties，然后通过键值获取属性
 * @author   zousy <510061684@qq.com>
 * @version  1.0.0
 * @2013-9-17 上午09:47:56
 */
public class PropertiesUtil
{
	private static Properties p=null;
	public static void LoadProperties() throws IOException{
		if(p==null){
			p=new Properties();
			p.load(PropertiesUtil.class.getResourceAsStream("message.properties"));
		}
	}
	public static String getValue(String key) throws IOException{
		LoadProperties();
		return (String) p.get(key);
	}
}

