package com.montnets.emp.ottbase.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.ottbase.biz.WeixBiz;
import com.montnets.emp.ottbase.constant.WXStaticValue;
import com.montnets.emp.ottbase.param.HttpReturnParams;
import com.montnets.emp.ottbase.util.TxtFileUtil;

/**
 * @description 处理HTTP 请求，包括get以及post请求。
 * @project ott
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author yejiangmin <282905282@qq.com>
 * @datetime 2013-11-12 下午03:05:46
 */
public class HttpRequestService
{
    /**
     * @description 处理HTTP请求接口
     * @param params
     *        必填 ： 1 url 设置请求地址；
     *        2 MENUCODE 请求的模块编码 用于日志记录 [不必须，但最好写。用于跟踪]；
     *        3 ReturnType JSON / XML / FILE写文件是否成功 返回消息格式 ；
     *        4 requestType GET/POST 设置请求类型 。
     *        ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *        如果 是POST请求 POST请求的内容存放在postMsg()里。
     *        并且设置 HttpPostType STR 请求的是JSON格式信息/FILE请求的是文件
     * @return 根据 ErrCode 以及 ErrMsg判断是否请求成功；
     *         返回： 成功 是 ErrCode == "000" 以及 ErrMsg == "success"；
     *         失败 返回 ErrCode == -9999/或其他 ErrMsg == 失败编码；
     *         获取请求返回的内容: 类型是[JSON] -- > getJsonObject /
     *         类型 [XML] ---> getReturnXml。
     *         类型[FILE] ---> ErrCode 以及 ErrMsg 判断 /是否写文件成功
     * @throws Exception
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-11-11 上午10:58:14
     */
    public HttpReturnParams requestOttHttp(HttpReturnParams params) throws Exception
    {
        // 请求参数为空
        if(params == null)
        {
            EmpExecutionContext.error("HttpRequestService.requestOttHttp.HttpReturnParams == NULL");
            return null;
        }
        // 默认错误编码是 -9999
        params.setErrCode("-9999");
        // 创建HttpClient实例
//        HttpClient httpclient = null;
        //TODO
        CloseableHttpClient httpclient = null;
        HttpResponse response = null;
        // 设置默认请求返回状态码
        int statusCode = 0;
        InputStream inputStream = null;
        try
        {
            // 请求地址为空
            if(params.getUrl() == null || "".equals(params.getUrl()))
            {
                params.setErrMsg("HttpRequestService.requestOttHttp.Url == null");
                params.setUrl("无");
                return params;
            }
            httpclient = new DefaultHttpClient();
            // 设置http请求中cook的策略
            httpclient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
            // 创建Get方法实例
            if("GET".equalsIgnoreCase(params.getRequestType()))
            {
                HttpGet httpgets = new HttpGet(params.getUrl());
                response = httpclient.execute(httpgets);
            }
            else if("POST".equalsIgnoreCase(params.getRequestType()))
            {
                if(params.getPostMsg() == null || "".equals(params.getPostMsg()))
                {
                    params.setErrMsg("HttpRequestService.requestOttHttp.PostMsg == null");
                    return params;
                }
                else if(params.getHttpPostType() == null || "".equals(params.getHttpPostType()))
                {
                    params.setErrMsg("HttpRequestService.requestOttHttp.HttpPostType == null");
                    return params;
                }
                HttpPost httppost = new HttpPost(params.getUrl());
                params = HandleHttpPost(params, httppost);
                response = httpclient.execute(httppost);
            }
            else
            {
                params.setErrMsg("HttpRequestService.requestOttHttp.RequestType Is Errer");
                return params;
            }
            if(response == null)
            {
                params.setErrMsg("HttpRequestService.requestOttHttp.HttpResponse == null");
                return params;
            }
            statusCode = response.getStatusLine().getStatusCode();
            // 设置请求的返回值、 默认是0
            params.setStatusCode(statusCode);
            // 请求返回状态值 成功为200
            if(statusCode != HttpStatus.SC_OK)
            {
                params.setErrMsg("HttpRequestService.requestOttHttp.StatusCode Is "+statusCode);
                return params;
            }
            // HttpEntity判断是否为空
            HttpEntity entity = response.getEntity();
            if(entity == null)
            {
                params.setErrMsg("HttpRequestService.requestOttHttp.HttpEntity == null");
                return params;
            }
            // InputStream判断是否为空
            inputStream = entity.getContent();
            if(inputStream == null)
            {
                params.setErrMsg("HttpRequestService.requestOttHttp.InputStream == null");
                return params;
            }
            // 接收文件，返回流
            if(WXStaticValue.WX_RETURNHTTP_FILE.equalsIgnoreCase(params.getReturnType()))
            {
                // 处理请求接口进行文件下载
                params = getHttpReturnFile(params, inputStream);
            }
            else if(WXStaticValue.RETURNHTTP_JSON.equalsIgnoreCase(params.getReturnType()))
            {
                // 处理请求接口返回JSON信息
                params = getHttpReturnJson(params, inputStream);
            }
            else if(WXStaticValue.WX_RETURNHTTP_XML.equalsIgnoreCase(params.getReturnType()))
            {
                // 处理请求接口返回XML信息
                params = getHttpReturnXml(params, inputStream);
            }
            else
            {
                params.setErrMsg("HttpRequestService.requestOttHttp.ReturnType Is Errer");
            }
        }
        catch (Exception e)
        {
            params.setErrMsg("HttpRequestService.requestOttHttp Is Errer");
        }
        finally
        {
            if(httpclient != null)
            {
                // 关闭连接
//                httpclient.getConnectionManager().shutdown();
            	httpclient.close();
            }
            if(inputStream != null){
                inputStream.close();
            }
            // 写入日志，记录这次http请求的请求记录
            new TxtFileUtil().writeWeixMsgToFile("http", statusCode + "，" + params.getErrMsg() + "。" + params.getUrl());

        }
        return params;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * @description 执行POST请求接口的一系列组装操作 包括 1 [MSG]请求带JSON参数的POST请求 2
     *              [FILE]上传文件到服务器
     * @param params
     *        HTTP请求参数类
     * @param httppost
     *        HTTPPOST对象
     * @return HttpReturnParams
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-11-14 下午07:14:08
     */
    private HttpReturnParams HandleHttpPost(HttpReturnParams params, HttpPost httppost)
    {
        try
        {
            // 处理带JSON格式的POST请求
            if(WXStaticValue.WX_HTTPPOSTTYPE_MSG.equals(params.getHttpPostType()))
            {
                StringEntity entity = new StringEntity(params.getPostMsg());
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");
                httppost.setEntity(entity);
            }
            else if(WXStaticValue.WX_HTTPPOSTTYPE_FILE.equals(params.getHttpPostType()))
            {
                // 处理上传文件的POST请求
                FileBody file = new FileBody(new File(params.getUpdownFileUrl()));
                MultipartEntity reqEntity = new MultipartEntity();
                reqEntity.addPart(params.getPostMsg(), file);
                httppost.setEntity(reqEntity);
            }
        }
        catch (Exception e)
        {
            params.setErrMsg("HttpRequestService.HandleHttpPost Is Errer");
            EmpExecutionContext.error(e, "HttpRequestService.HandleHttpPost Is Errer");
        }
        return params;
    }

    /**
     * @description 处理请求接口返回的XML格式
     * @param params
     *        HTTP请求参数类
     * @param inputStream
     *        获取接口返回的输入流
     * @return HttpReturnParams
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-11-14 下午06:50:48
     */
    private HttpReturnParams getHttpReturnXml(HttpReturnParams params, InputStream inputStream)
    {
        BufferedReader reader = null;
        try
        {
            StringBuffer buffer = new StringBuffer();
            // 设置编码,否则中文乱码
            reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String line = "";
            while((line = reader.readLine()) != null)
            {
                buffer.append(line);
            }
            if(buffer.length() > 0)
            {
                // 表示成功接收
                params.setErrCode("000");
                params.setErrMsg("success");
                params.setReturnXml(buffer.toString());
                // 清空BUFFER中数据
                buffer.setLength(0);
            }
            else
            {
                params.setErrMsg("HttpRequestService.getHttpReturnXml.StringBuffer Is Null");
            }
        }
        catch (Exception e)
        {
            params.setErrMsg("HttpRequestService.getHttpReturnXml Is Errer");
            EmpExecutionContext.error(e, "HttpRequestService.getHttpReturnXml Is Errer");
        }
        finally
        {
            if(reader != null)
            {
                try
                {
                    reader.close();
                }
                catch (IOException e)
                {
                    EmpExecutionContext.error(e, "HttpRequestService.getHttpReturnXml.BufferedReader.close Is Errer");
                }
            }
        }
        return params;
    }

    /**
     * @description 处理请求接口返回的Json格式
     * @param params
     *        HTTP请求参数类
     * @param inputStream
     *        获取接口返回的输入流
     * @return HttpReturnParams
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-11-14 下午06:50:48
     */
    private HttpReturnParams getHttpReturnJson(HttpReturnParams params, InputStream inputStream)
    {
        try
        {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(new BufferedReader(new InputStreamReader(inputStream, "UTF-8")));
            if(jsonObject != null)
            {
                // 表示成功接收
                params.setJsonObject(jsonObject);
                if(jsonObject.get("errcode") == null || "0".equals(jsonObject.get("errcode").toString())){
                    //表示成功
                    params.setErrCode("000");
                    params.setErrMsg("success");
                }else{
                    params.setErrCode(jsonObject.get("errcode").toString());
                    params.setErrMsg(jsonObject.get("errmsg").toString());
                }
            }
            else
            {
                params.setErrMsg("HttpRequestService.getHttpReturnJson.JSONObject Is Null");
            }
        }
        catch (Exception e)
        {
            params.setErrMsg("HttpRequestService.getHttpReturnJson Is Errer");
            EmpExecutionContext.error(e, "HttpRequestService.getHttpReturnJson Is Errer");
        }
        return params;
    }

    /**
     * @description 请求HTTP接口 获取服务器上的流 进行下载写文件
     * @param params
     *        HTTP请求参数类
     * @param inputStream
     *        获取接口返回的输入流
     * @return HttpReturnParams
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-11-14 下午06:56:51
     */
    private HttpReturnParams getHttpReturnFile(HttpReturnParams params, InputStream inputStream)
    {
        try
        {
            String returnmsg = new WeixBiz().writerFileForDownLoad(inputStream, params.getUpdownFileUrl());
            if("success".equals(returnmsg))
            {
                // 表示写文件成功
                params.setErrCode("000");
                params.setErrMsg("success");
            }
            else
            {
                params.setErrMsg("HttpRequestService.getHttpReturnFile write file Is fail");
            }
        }
        catch (Exception e)
        {
            params.setErrMsg("HttpRequestService.getHttpReturnFile Is Errer");
            EmpExecutionContext.error(e, "HttpRequestService.getHttpReturnFile Is Errer");
        }
        return params;
    }
  

}
