package com.montnets.shaoguanga.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.montnets.shaoguanga.bean.SendRpts;
import com.montnets.shaoguanga.properties.MwConfig;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author WJH
 * @Description
 * @date 2021/4/19 10:10
 * @Email ibytecode2020@gmail.com
 */
@Service
public class HttpService {
    private static final Logger log = LoggerFactory.getLogger(HttpService.class);

    @Autowired
    private RequestConfig requestConfig;
    @Autowired
    private CloseableHttpClient httpClient;

    @Autowired
    private MwConfig mwConfig;
    private static final int HTTP_OK = 200;

    /**
     * 请求边界地址
     * @param params 网关接口所需要的参数
     * @param requestUrl 网关接口URL
     * @return
     */
    public String send2GateKeeper(String params, String requestUrl) {
        // 请求边界接口参数
        Map<String, Object> requestParams = new HashMap<>(4);
        requestParams.put("username", mwConfig.getUsername());
        requestParams.put("passwd", mwConfig.getPasswd());
        requestParams.put("timeout", mwConfig.getTimeout());
        // 设置 param
        requestParams.put("params", params);
        // 发送接口地址
        requestParams.put("requestUri", requestUrl);
        if (StringUtils.isEmpty(params) || StringUtils.isEmpty(requestUrl)) {
            return null;
        }
        HttpPost httpPost = null;
        CloseableHttpResponse response = null;
        try {
            log.info("http 请求参数：{}", requestParams);
            httpPost = new HttpPost(mwConfig.getGatekeeper());
            httpPost.setConfig(requestConfig);
            List<NameValuePair> pairList = new ArrayList<>();
            for (Map.Entry<String, Object> entry : requestParams.entrySet()) {
                pairList.add(new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue())));
            }
            StringEntity entity = new UrlEncodedFormEntity(pairList, "UTF-8");
            httpPost.setEntity(entity);

            response = httpClient.execute(httpPost);
            if (response != null) {
                String res = EntityUtils.toString(response.getEntity());
                log.info("调用边界接口响应：{}", res);
                return res;
            }
            log.info("边界接口无响应");
        } catch (Exception e) {
            log.error("HTTPClient 发送请求失败：", e);
        } finally {
            if (null != response) {
                EntityUtils.consumeQuietly(response.getEntity());
            }
        }
        return null;
    }


    /**
     * 推送状态报告到对应sp账号
     * @param spUrl sp账号对应 URL
     * @param sendRptsList 状态报告报文
     * @return 响应码为200 时认为推送成功 返回true 其他情况返回false
     */
    public boolean send2SpUrl(String spUrl, List<SendRpts> sendRptsList) throws Exception {
        if (StringUtils.isEmpty(spUrl) || CollectionUtils.isEmpty(sendRptsList)) {
            return false;
        }
        HttpPost httpPost = null;
        CloseableHttpResponse response = null;
        try {
            ObjectMapper om = new ObjectMapper();
            log.info("推送地址：{}，报文：{}", spUrl, sendRptsList);
            httpPost = new HttpPost(spUrl);
            httpPost.setConfig(requestConfig);
            StringEntity requestEntity = new StringEntity(om.writeValueAsString(sendRptsList), "utf-8");
            requestEntity.setContentEncoding("UTF-8");
            httpPost.setHeader("Content-type", "application/json;charset=UTF-8");
            httpPost.setEntity(requestEntity);
            response = httpClient.execute(httpPost);
            if (null != response && response.getStatusLine().getStatusCode() == HTTP_OK) {
                log.info("推送到 {} 成功", spUrl);
                return true;
            }
            log.error("推送到 {} 失败", spUrl);
        } finally {
            if (null != response) {
                EntityUtils.consumeQuietly(response.getEntity());
            }
        }
        return false;
    }
    /**
     * 推送状态报告到对应sp账号
     * @param spUrl sp账号对应 URL
     * @param sendRptsListStr 状态报告报文
     * @return 响应码为200 时认为推送成功 返回true 其他情况返回false
     */
    public boolean send2SpUrl(String spUrl, String sendRptsListStr) throws Exception {
        if (StringUtils.isEmpty(spUrl) || StringUtils.isEmpty(sendRptsListStr)) {
            return false;
        }
        HttpPost httpPost = null;
        CloseableHttpResponse response = null;
        try {
            log.info("推送地址：{}，报文：{}", spUrl, sendRptsListStr);
            httpPost = new HttpPost(spUrl);
            httpPost.setConfig(requestConfig);
            StringEntity requestEntity = new StringEntity(sendRptsListStr, "utf-8");
            requestEntity.setContentEncoding("UTF-8");
            httpPost.setHeader("Content-type", "application/json;charset=UTF-8");
            httpPost.setEntity(requestEntity);
            response = httpClient.execute(httpPost);
            if (null != response && response.getStatusLine().getStatusCode() == HTTP_OK) {
                log.info("推送到 {} 成功", spUrl);
                return true;
            }
            log.error("推送到 {} 失败", spUrl);
        } finally {
            if (null != response) {
                EntityUtils.consumeQuietly(response.getEntity());
            }
        }
        return false;
    }

}
