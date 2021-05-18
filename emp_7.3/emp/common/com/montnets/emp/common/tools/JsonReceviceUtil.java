package com.montnets.emp.common.tools;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.montnets.emp.common.context.EmpExecutionContext;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

/**
*@ClassName: JsonReceviceUtil
* @Description: 读取 HttpServletRequest流转换为JSON 工具类
* @author xuty 
* @date 2018-10-19 下午03:12:22
* @version V1.0 
*/
public class JsonReceviceUtil {
	/**
	 *  获取Request 中的请求参数
	 * @param request
	 * @return
	 */
	public static String getReqString(HttpServletRequest request) {
		StringBuffer subf = null;
		BufferedReader reader = null;
		try {
			if (null != request) {
				subf = new StringBuffer();
				String line = null;
				reader = request.getReader();
				while ((line = reader.readLine()) != null) {
					subf.append(line);
				}
			}
		} catch (IOException e) {
			EmpExecutionContext.error(e, "读取request流出现异常");
			return null;
		} finally {
			if (null != reader) {
				try {
					reader.close();
				} catch (IOException e) {
					EmpExecutionContext.error(e, "关闭BufferedReader流出现异常");
				}
			}
		}
		return subf == null ? "" : subf.toString();
	}
	/**
	 * 字符串转换为JSONObject
	 * @param request
	 * @return
	 */
	public static JSONObject parseToJson(HttpServletRequest request) {
		JSONObject jsonObject = null;
		StringBuffer subf = null;
		BufferedReader reader = null;
		try {
			if (null != request) {
				subf = new StringBuffer();
				String line = null;
				reader = request.getReader();
				while ((line = reader.readLine()) != null) {
					subf.append(line);
				}
				EmpExecutionContext.info("--前端报文:"+subf.toString());
				jsonObject = (JSONObject) JSONObject.parse(subf.toString());
			}
		} catch (IOException e) {
			jsonObject = null;
			EmpExecutionContext.error(e, "读取request流出现异常");
		} finally {
			if (null != reader) {
				try {
					reader.close();
				} catch (IOException e) {
					EmpExecutionContext.error(e, "关闭BufferedReader流出现异常");
				}
			}
		}
		return jsonObject;
	}
	/**
	 * 字符串转换为JSONObject
	 * @param request
	 * @return
	 */
	public static JSONArray parseToJsonArray(HttpServletRequest request) {
		JSONArray jsonArray = null;
		StringBuffer subf = null;
		BufferedReader reader = null;
		try {
			if (null != request) {
				subf = new StringBuffer();
				String line = null;
				reader = request.getReader();
				while ((line = reader.readLine()) != null) {
					subf.append(line);
				}
				jsonArray = (JSONArray) JSONArray.parse(subf.toString());
			}
		} catch (IOException e) {
			jsonArray = null;
			EmpExecutionContext.error(e, "读取request流出现异常");
		} finally {
			if (null != reader) {
				try {
					reader.close();
				} catch (IOException e) {
					EmpExecutionContext.error(e, "关闭BufferedReader流出现异常");
				}
			}
		}
		return jsonArray;
	}
	/**
	 * 字符串转JsonArray
	 * @param jsonArrStr
	 * @return
	 */
	public static JSONArray parseToJsonArray(String jsonArrStr) {
		JSONArray jsonArray = null;
		if(null != jsonArrStr){
			jsonArray = (JSONArray) JSONArray.parse(jsonArrStr);
		}
		return jsonArray;
	}
}
