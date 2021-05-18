package com.montnets.emp.rms.rmsapi.util;

import com.alibaba.fastjson.JSONObject;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.rms.rmsapi.constant.RMSHttpConstant;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @ClassName HttpUtil
 * @Description http 连接工具类
 * @Author lianghuageng
 * @Date 2019/5/29 11:15
 * @ModifyDate 2019/5/29 11:15
 * @Version 1.0
 */
public class HttpUtil {

    public static void main(String[] args) {
        String url = "http://localhost:8080/";
        JSONObject params = new JSONObject();
        params.put("type", 1);
        params.put("value", true);
        System.out.println(doGet(url, params));
    }

    /**
     * POST请求
     */
    public static JSONObject doPost(String url, String params) {
        JSONObject result = new JSONObject();
        result.put("success", true);
        result.put("data", null);
        result.put("code", 200);
        result.put("msg", null);
        OutputStreamWriter out = null;
        InputStream in = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        BufferedWriter bw = null;
        try {
            URLConnection urlConnection = new URL(url).openConnection();
            HttpURLConnection httpUrlConnection = (HttpURLConnection) urlConnection;
            // 设置连接超时时间
            httpUrlConnection.setConnectTimeout(10 * 1000);
            // 设置读取超时时间
            httpUrlConnection.setReadTimeout(10 * 1000);

            // 设置是否向httpUrlConnection输出，post请求，参数要放在http正文内，因此需要设为true,
            // 默认情况下是false;
            httpUrlConnection.setDoOutput(true);
            // 设置是否从httpUrlConnection读入，默认情况下是true;
            httpUrlConnection.setDoInput(true);
            // 忽略缓存
            httpUrlConnection.setUseCaches(false);
            // 设定请求的方法为"POST"，默认是GET
            httpUrlConnection.setRequestMethod("POST");
            //设定 请求格式 json，也可以设定xml格式的
            httpUrlConnection.setRequestProperty("Content-Type", " application/json");
            //设置编码语言
            httpUrlConnection.setRequestProperty("Accept-Charset", "UTF-8");
            httpUrlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            httpUrlConnection.setRequestProperty("accept", "application/json");
            httpUrlConnection.setRequestProperty("Charset", "UTF-8");
            // 是否开启长连接
            if (RMSHttpConstant.IS_KEEP_ALIVE) {
                httpUrlConnection.setRequestProperty("Connection", "Keep-Alive");
            }
            out = new OutputStreamWriter(httpUrlConnection.getOutputStream(), "UTF-8");
            bw = new BufferedWriter(out);
            bw.write(params);
            bw.flush();
            bw.close();
            // 获得响应状态
            int responseCode = httpUrlConnection.getResponseCode();
            if (HttpURLConnection.HTTP_OK == responseCode) {
                byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                in = httpUrlConnection.getInputStream();
                while ((len = in.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, len);
                    byteArrayOutputStream.flush();
                }

                result.put("success", true);
                result.put("data", byteArrayOutputStream.toString("UTF-8"));
                result.put("code", 200);
                result.put("msg", "请求成功");
            } else {
                result.put("success", false);
                result.put("code", responseCode);
                result.put("msg", "请求异常");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("code", 500);
            result.put("msg",
                    "请求异常，异常信息：" + e.getClass() + "->" + e.getMessage());
        } finally {
            closeAll(result, out, bw, in, byteArrayOutputStream);
        }
        return result;
    }

    /**
     * GET请求
     */
    public static JSONObject doGet(String url, JSONObject params) {

        JSONObject result = new JSONObject();
        result.put("success", true);
        result.put("data", null);
        result.put("code", 200);
        result.put("msg", null);

        InputStream in = null;
        ByteArrayOutputStream byteArrayOutputStream = null;

        try {
            // URL传入参数
            StringBuilder queryString = new StringBuilder();
            if (params != null) {
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    queryString.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue().toString(),
                            "UTF-8")).append("&");
                }
            }
            if (queryString.length() > 0) {
                queryString = new StringBuilder(queryString
                        .substring(0, queryString.length() - 1));

                url = url + "?" + queryString;
            }

            URLConnection urlConnection = new URL(url).openConnection();
            HttpURLConnection httpUrlConnection = (HttpURLConnection) urlConnection;
            // 设置超时时间
            httpUrlConnection.setConnectTimeout(5 * 1000);
            // 设置是否向httpUrlConnection输出，post请求，参数要放在http正文内，因此需要设为true,
            // 默认情况下是false;
            httpUrlConnection.setDoOutput(true);
            // 设置是否从httpUrlConnection读入，默认情况下是true;
            httpUrlConnection.setDoInput(true);
            // 忽略缓存
            httpUrlConnection.setUseCaches(false);
            // 设定请求的方法为"POST"，默认是GET
            httpUrlConnection.setRequestMethod("GET");
            httpUrlConnection.setRequestProperty("Connection", "Keep-Alive");
            httpUrlConnection.setRequestProperty("Charset", "UTF-8");
            httpUrlConnection.connect();

            // 获得响应状态
            int responseCode = httpUrlConnection.getResponseCode();

            if (HttpURLConnection.HTTP_OK == responseCode) {
                byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len;
                in = httpUrlConnection.getInputStream();
                while ((len = in.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, len);
                    byteArrayOutputStream.flush();
                }
                result.put("success", true);
                result.put("data", byteArrayOutputStream.toString("UTF-8"));
                result.put("code", 200);
                result.put("msg", "请求成功");
            } else {
                result.put("success", false);
                result.put("code", responseCode);
                result.put("msg", "请求异常");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("code", 500);
            result.put("msg",
                    "请求异常，异常信息：" + e.getClass() + "->" + e.getMessage());
        } finally {
            closeAll(result, byteArrayOutputStream, in);
        }
        return result;
    }

    /**
     * 关闭流
     *
     * @param result 返回报文实体
     * @param cs     流集合
     */
    public static void closeAll(JSONObject result, Closeable... cs) {
        try {
            if (null != cs) {
                for (Closeable c : cs) {
                    if (null != c) {
                        c.close();
                    }
                }
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("code", 500);
            result.put("msg",
                    "请求异常，异常信息：" + e.getClass() + "->" + e.getMessage());
            EmpExecutionContext.error(e, "关闭流异常!");
        }
    }
}
