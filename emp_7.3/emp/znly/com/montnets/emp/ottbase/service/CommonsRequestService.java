package com.montnets.emp.ottbase.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.ottbase.param.BaiduMapParams;
import com.montnets.emp.ottbase.util.TxtFileUtil;
/**
 * 
 * @description     处理commons下的http请求
 * @project ott
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author yejiangmin <282905282@qq.com>
 * @datetime 2013-11-30 下午04:36:21
 */
public class CommonsRequestService
{
    /**
     * 
     * @description     处理http请求，POST请求，参数为KEY/VALUE
     * @param baiduparams
     * @return  errcode 000 errmsg 'success' 表示成功   其他表示失败
     * @throws Exception       			 
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-11-30 下午05:00:18
     */
    public BaiduMapParams requestCommonsHttp(BaiduMapParams baiduparams) throws Exception{
        //客户端请求
        HttpClient httpClient = null;
        //输入流
        InputStream inputStream = null;
        //默认请求返回状态值
        int statusCode = -1 ;
        try{
            if(baiduparams.getUrl() == null || "".equals(baiduparams.getUrl())){
                baiduparams.setErrMsg("CommonsRequestService.requestCommonsHttp.UrlIsNull");
                return baiduparams;
            }
            if(baiduparams.getParamsMap() == null || baiduparams.getParamsMap().size() == 0){
                baiduparams.setErrMsg("CommonsRequestService.requestCommonsHttp.ParamsMapIsNull");
                return baiduparams;
            }
            //POST的URL
            PostMethod postMethod = new PostMethod(baiduparams.getUrl());
            //获取迭代器
            Iterator<Map.Entry<String, String>> iter = baiduparams.getParamsMap().entrySet().iterator();
            Map.Entry<String, String> iternext = null;
            NameValuePair pair = null;
            //循环获取MAP中的内容
            while (iter.hasNext())
            {
                iternext = iter.next();
                pair = new NameValuePair(iternext.getKey(),iternext.getValue());
                postMethod.addParameter(pair);
                pair = null;
            }   
            //判断是否添加进去内容
            if(postMethod.getParameters() == null || postMethod.getParameters().length == 0){
                baiduparams.setErrMsg("CommonsRequestService.requestCommonsHttp.postMethod.getParameters() == null");
                return baiduparams;
            }
            //初始化
            httpClient = new HttpClient();
            //设置参数编码为UTF-8
            postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,"UTF-8");
            //取消打印台信息 警告
            postMethod.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
            //请求连接
            httpClient.executeMethod(postMethod);
            //返回状态
            statusCode = postMethod.getStatusCode();
            if(statusCode == HttpStatus.SC_OK){//如果状态码为200,就是正常返回
                inputStream  =  postMethod.getResponseBodyAsStream();
                if(inputStream == null){
                    baiduparams.setErrMsg("CommonsRequestService.requestCommonsHttp.InputStreamIsNull");
                    return baiduparams;
                }
                JSONObject jsonObject = (JSONObject) new JSONParser().parse(new BufferedReader(new InputStreamReader(inputStream, "UTF-8")));
                if(jsonObject == null){
                    baiduparams.setErrMsg("CommonsRequestService.requestCommonsHttp.JSONObjectIsNull");
                    return baiduparams;
                }else{
                    baiduparams.setJsonObj(jsonObject);
                    baiduparams.setErrMsg("success");
                    baiduparams.setErrCode("000");
                }
            }
        }catch (Exception e) {
            baiduparams.setErrMsg("CommonsRequestService.requestCommonsHttp.errer");
            EmpExecutionContext.error(e, "CommonsRequestService.requestCommonsHttp出现异常");
        } finally
        {
            if(inputStream != null){
                inputStream.close();
            }
            // 写入日志，记录这次http请求的请求记录
           new TxtFileUtil().writeSendResult(0l,  statusCode + "，" + baiduparams.getErrMsg() + "。" + baiduparams.getUrl());
        }
        return baiduparams;
    }

    
    
    
}
