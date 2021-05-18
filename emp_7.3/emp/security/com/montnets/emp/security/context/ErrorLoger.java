/**
 * @description
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-5-29 上午09:03:20
 */
package com.montnets.emp.security.context;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * @description
 * @project emp_std
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-5-29 上午09:03:20
 */

public class ErrorLoger
{
	private static String				newline		= System.getProperties().getProperty("line.separator");

	public String getErrorLog(Exception e, String message)
	{
		Writer writer = new StringWriter();
		e.printStackTrace(new PrintWriter(writer));
		// 拼接异常日志信息
		String logInfo = message + newline + writer.toString();
		return logInfo;
	}
}
