/**
 * @description  
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-11-14 上午09:41:39
 */
package com.montnets.emp.servmodule.ydcx.constant;


/**
 * @description 
 * @project emp_std_189
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-11-14 上午09:41:39
 */

public class ServerInof
{
	private static String serverName = "";

    public static String getServerName() {
        return serverName;
    }

    /**
	 * 获取WEB服务器名称
	 * @description    
	 * @param ServerInfo       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-11-13 下午03:18:00
	 */
	public void setServerName(String ServerInfo)
	{
		if("".equals(serverName))
		{
			if(ServerInfo != null && !"".equals(ServerInfo))
			{
				if(ServerInfo.indexOf("Tomcat") > -1)
				{
					serverName = "Tomcat";
				}
				else if(ServerInfo.indexOf("WebLogic") > -1)
				{
					serverName = "WebLogic";
				}
				else if(ServerInfo.indexOf("WebSphere") > -1)
				{
					serverName = "WebSphere";
				}
				else
				{
					serverName = "Tomcat";
				}
			}
		}
	}
}
