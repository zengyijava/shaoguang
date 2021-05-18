package com.montnets.emp.rms.rmsapi.comm;

import com.alibaba.fastjson.JSONObject;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.rms.rmsapi.constant.RMSHttpConstant;
import com.montnets.emp.rms.rmsapi.util.HttpUtil;
import com.montnets.emp.util.StringUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName HttpSendUtils
 * @Description http发送工具
 * @Author lianghuageng
 * @Date 2019/5/29 11:10
 * @ModifyDate 2019/5/29 11:10
 * @Version 1.0
 */
public final class HttpSendUtils {

    private static final String PREFIX_URL = "http://";

    private HttpSendUtils() {
    }

    /**
     * 发送Post请求
     *
     * @param ip      ip
     * @param port    端口
     * @param httpUrl 请求地址
     * @param obj     参数
     * @param reSend  异常处理次数
     * @return 返回结果
     */
    public static String sendPost(String ip, int port, String httpUrl, Object obj, int reSend) throws Exception {
        String result = "";
        try {
            // 获取 url
            String url = httpUrl;
            if (!httpUrl.contains("http:")) {
                // 拼接地址：
                url = PREFIX_URL + ip + ":" + port + httpUrl;
            }
            // 要打印日志的字符串
            String params = JSONObject.toJSONString(obj);
            EmpExecutionContext.info("url为："+ url +" 网关发送接口信息:" + params);
            // 返回报文结果
            JSONObject response = HttpUtil.doPost(url, params);
			EmpExecutionContext.info("url为："+ url +"  网关接收接口信息:" + response);
            result = handlerResponse(response);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "发送http请求异常");
            if (reSend >= RMSHttpConstant.RESEND_DONE) {
                EmpExecutionContext.error(e, "http 发送异常，现在进行重发。目前进行第" + (reSend - 1) + "次重发");
                result = sendPost(ip, port, httpUrl, obj, reSend - 1);
                if (StringUtils.isNotBlank(result)) {
                    return result;
                }
            }
        }
        return result;
    }

    /**
     * 上传模板专用请求
     *
     * @param ip      ip
     * @param port    端口
     * @param httpUrl 请求地址
     * @param obj     参数
     * @param reSend  异常处理次数
     * @return 返回结果
     */
    public static String sendPostSubTemplate(String ip, int port, String httpUrl, Object obj, int reSend) throws Exception {
        String result = "";
        try {
            // 获取 url
            String url = httpUrl;
            if (!httpUrl.contains("http:")) {
                // 拼接地址：
                url = "http://" + ip + ":" + port + httpUrl;
            }
            List<BasicNameValuePair> basicNameValuePairs = new ArrayList<BasicNameValuePair>();
            // 解析参数
            parseParam(obj, basicNameValuePairs);
            String info = JSONObject.toJSON(obj).toString();
            EmpExecutionContext.info("url为："+ url +"  网关发送接口信息:" + info);
            // 解析成 JSON
            String params = parseParam(basicNameValuePairs);
            JSONObject response = HttpUtil.doPost(url, params);
			EmpExecutionContext.info("url为："+ url +"  网关接收接口信息1:" + response);
            result = handlerResponse(response);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "发送http请求异常");
            if (reSend >= RMSHttpConstant.RESEND_DONE) {
                EmpExecutionContext.error(e, "http 发送异常，现在进行重发。目前进行第" + (reSend - 1) + "次重发");
                result = sendPostSubTemplate(ip, port, httpUrl, obj, reSend - 1);
                if (StringUtils.isNotBlank(result)) {
                    return result;
                }
            }
        }
        return result;
    }

    /**
     * 发送Post to MBoss
     *
     * @param ip      ip
     * @param port    端口
     * @param httpUrl 请求地址
     * @param obj     参数
     * @param reSend  异常处理次数
     * @return 返回结果
     */
    public static String sendPostToMBoss(String ip, int port, String httpUrl, Object obj, int reSend) {
        String result = "";
        try {
            // 获取 url
            String url = httpUrl;
            if (!httpUrl.contains("http:")) {
                // 拼接地址：
                url = "http://" + ip + ":" + port + httpUrl;
            }
            List<BasicNameValuePair> basicNameValuePairs = new ArrayList<BasicNameValuePair>();
            // 解析参数
            parseParam(obj, basicNameValuePairs);
            String info = JSONObject.toJSON(obj).toString();
            EmpExecutionContext.info("url为："+ url +" 网关发送接口信息:" + info);
            String params = URLEncodedUtils.format(basicNameValuePairs, RMSHttpConstant.UTF8_ENCODE);
            EmpExecutionContext.info("企业富信-数据查询-发送明细查询。请求报文：" + basicNameValuePairs.toString());
            JSONObject response = HttpUtil.doPost(url, params);
			EmpExecutionContext.info("url为："+ url +"  网关接收接口信息:" + response);
            if (null != response) {
                // 获取效应码
                Integer code = (Integer) response.get("code");
                // 获取返回结果集
                String data = (String) response.get("data");
                // 如果是正常请求
                if (code == RMSHttpConstant.HTTP_SUCCESS_CODE) {
                    // 如果相应内容不为空
                    if (StringUtils.isNotBlank(data)) {
                        // 请求成功，能获取到响应内容
                        result = data;
                        EmpExecutionContext.info("企业富信-数据查询-发送明细查询。正确响应, 响应码为:" + code
                                + "响应报文:" + result);
                    }
                } else { // 如果不是 200 响应码
                    // 如果相应内容不为空
                    if (StringUtils.isNotBlank(data)) {
                        result = data;
                        EmpExecutionContext.info("企业富信-数据查询-发送明细查询。错误响应, 响应码为:" + code
                                + "响应报文:" + result);
                        EmpExecutionContext.error("错误响应, 响应码为:" + code + ", 响应报文:" + result);
                    }
                }
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "企业富信-数据查询-发送明细查询。发送http请求异常");
            if (reSend >= RMSHttpConstant.RESEND_DONE) {
                EmpExecutionContext.error(e, "企业富信-数据查询-发送明细查询异常。" +
                        ", 现在进行重发。 当前进行第" + (reSend - 1) + "次重发");
                result = sendPostToMBoss(ip, port, httpUrl, obj, reSend - 1);
                if (StringUtils.isNotBlank(result)) {
                    return result;
                }
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private static void parseParam(Object obj, List<BasicNameValuePair> params) throws NoSuchMethodException,
            IllegalAccessException, InvocationTargetException {
        // 设置请求的参数
        String fieldName;
        String fieldNameUpper;
        Method getMethod;
        Class cls = obj.getClass();
        Field[] fields = cls.getDeclaredFields();
        Object value;
        for (Field field : fields) {
            fieldName = field.getName();
            if (!"serialVersionUID".equals(fieldName)) {
                fieldNameUpper = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
                getMethod = cls.getMethod("get" + fieldNameUpper);
                value = getMethod.invoke(obj);
                if (null != value) {
                    params.add(new BasicNameValuePair(fieldName, String.valueOf(value)));
                }
            }
        }
    }

    private static String parseParam(Iterable<BasicNameValuePair> params) {
        Map<String, Object> map = new HashMap<String, Object>();
        StringBuilder sb = new StringBuilder();
        String name;
        String value;
        sb.append("{");
        for (BasicNameValuePair basicNameValuePair : params) {
            name = basicNameValuePair.getName();
            value = basicNameValuePair.getValue();
            if ("content".equals(name)) {
                if (value == null || "".equals(value) || !value.contains("[")) {
                    //content为非集合
                    sb.append("\"").append(name).append("\":");
                    sb.append("\"").append(value).append("\",");
                } else {
                    //content为集合
                    sb.append("\"").append(name).append("\":");
                    sb.append(value).append(",");
                }
            } else {
                sb.append("\"").append(name).append("\":");
                sb.append("\"").append(value).append("\",");
            }
            map.put(name, value);
        }
        sb.delete(sb.length() - 1, sb.length());
        sb.append("}");
        EmpExecutionContext.error("请求报文：" + sb.toString());
        return sb.toString();
    }

    /**
     * 处理返回报文
     *
     * @param response 返回报文
     */
    private static String handlerResponse(JSONObject response) {
        String result = "";
        if (null != response) {
            // 获取效应码
            Integer code = (Integer) response.get("code");
            // 获取返回结果集
            String data = (String) response.get("data");
            // 如果是正常请求
            if (code == RMSHttpConstant.HTTP_SUCCESS_CODE) {
                // 如果相应内容不为空
                if (StringUtils.isNotBlank(data)) {
                    // 请求成功，能获取到响应内容
                    result = data;
                    EmpExecutionContext.error("正确响应, 响应码为:" + code + ", 响应报文:" + result);
                }
            } else { // 如果不是 200 响应码
                // 如果相应内容不为空
                if (StringUtils.isNotBlank(data)) {
                    result = data;
                    EmpExecutionContext.error("错误响应, 响应码为:" + code + ", 响应报文:" + result);
                }
            }
        }
        return result;
    }

}
