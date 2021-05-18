package com.montnets.emp.ottbase.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.entity.wxgl.LfWeiUserInfo;
import com.montnets.emp.ottbase.biz.WeixBiz;
import com.montnets.emp.ottbase.constant.WXStaticValue;
import com.montnets.emp.ottbase.constant.WeixHttpUrl;
import com.montnets.emp.ottbase.param.HttpReturnParams;
import com.montnets.emp.ottbase.util.DownloadFile;
import com.montnets.emp.ottbase.util.TxtFileUtil;
import com.montnets.emp.table.wxgl.TableLfWeiUserInfo;

/**
 * @description 处理HTTPS 请求 ，主要微信接口调用
 * @project ott
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author yejiangmin <282905282@qq.com>
 * @datetime 2013-11-12 上午11:09:09
 */
public class HttpsRequestService
{    

	
    /**
     * @description 调用HTTPS请求方法
     * @param params
     *        处理https请求的，微信接口调用
     * @return 返回 null returnParams.getErrCode() == -9999 失败 / 000成功 ErrMsg 错误信息
     *         ：成功 是 success /其他
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-11-5 下午07:12:32
     */
    public HttpReturnParams HandleHttpsRequest(HttpReturnParams params) throws Exception
    {
        if(params == null)
        {
            EmpExecutionContext.error("HttpsRequestService.HandleHttpsRequest.HttpReturnParams Is Null");
            return null;
        }
       // HttpsURLConnection httpsURLConn = null;
//        HttpClient client = null;
        //TODO
        CloseableHttpClient client = null;
        
        InputStream inputStream = null;
        try
        {
            // 默认错误编码是 -9999
            // 请求的http地址
            if(params.getUrl() == null || "".equals(params.getUrl()))
            {
                params.setErrMsg("HttpsRequestService.HandleHttpsRequest.Url Is Null");
                params.setUrl("无");
                return params;
            }
            if(params.getRequestType() == null || "".equals(params.getRequestType()))
            {
                params.setErrMsg("HttpsRequestService.HandleHttpsRequest.RequestType Is Null");
                return params;
            }
            // 类 URL 代表一个统一资源定位符
            //URL console = new URL(params.getUrl());
            
            // 创建HttpClient对象
			client = new DefaultHttpClient();
			// 设置连接超时时间
			client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 60*1000);
			// 设置Socket超时时间
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 36600);
			//HttpPost httppost = new HttpPost(params.getUrl());
			
			HttpPost post = new HttpPost(params.getUrl());  
		    
            /*// 使用指定的 URL 创建 HttpsURLConnection。
            httpsURLConn = (HttpsURLConnection) console.openConnection();
            // 获取为安全 https URL 连接创建套接字时使用的 SSL 套接字工厂。
            httpsURLConn.setSSLSocketFactory(getSSLContent().getSocketFactory());
            // 设置此实例的 HostnameVerifier。
            httpsURLConn.setHostnameVerifier(new TrustAnyHostnameVerifier());

            // 设置 URL 请求的方法GET POST HEAD OPTIONS PUT DELETE TRACE
            httpsURLConn.setRequestMethod(params.getRequestType());
            // URL 连接可用于输入和/或输出。
            httpsURLConn.setDoInput(params.isDoInput());
            // URL 连接可用于输入和/或输出。
            httpsURLConn.setDoOutput(params.isDoOutput());
            if("POST".equals(params.getRequestType()))
            {
                httpsURLConn.setUseCaches(false);
            }
            // 请求超时，默认1分钟
            httpsURLConn.setConnectTimeout(60 * 1000);
            // 建立连接
            httpsURLConn.connect();*/
            // 判断是否需要带上json格式的信息做请求操作
            if(params.isDoOutput() && params.getPostMsg() != null && !"".equals(params.getPostMsg()))
            {
            	StringEntity postingString = new StringEntity(params.getPostMsg(),"UTF-8");// json传递  
    		    post.setEntity(postingString);  
    		    post.setHeader("Content-type", "application/json");  
    		    
                /*OutputStream outputStream = null;
                try
                {
                    outputStream = httpsURLConn.getOutputStream();
                    outputStream.write(params.getPostMsg().getBytes("UTF-8"));
                }
                catch (Exception e)
                {
                    params.setErrMsg("HttpsRequestService.HandleHttpsRequest.OutputStream Is Errer");
                    EmpExecutionContext.error(e, "HttpsRequestService.HandleHttpsRequest.OutputStream Is Errer");
                    return params;
                }
                finally
                {
                    if(outputStream != null)
                    {
                        outputStream.flush();
                        outputStream.close();
                    }
                }*/
            }
            // 判断是否操作OutputStream类出现异常 true false
            try
            {
            	HttpResponse response = client.execute(post);  
    		    //String content = EntityUtils.toString(response.getEntity());  
    		    
                inputStream = response.getEntity().getContent();
                if(inputStream == null)
                {
                    params.setErrMsg("HttpsRequestService.HandleHttpsRequest.InputStream Is Null");
                    return params;
                }
                if(WXStaticValue.WX_RETURNHTTP_FILE.equalsIgnoreCase(params.getReturnType()))
                {
                    // 处理文件下载
                    params = getHttpsReturnFile(params, inputStream);
                }
                else if(WXStaticValue.RETURNHTTP_JSON.equalsIgnoreCase(params.getReturnType()))
                {
                    // 处理JSON返回格式
                    params = getHttpsReturnJson(params, inputStream);
                }
            }
            catch (Exception e)
            {
                params.setErrMsg("HttpsRequestService.HandleHttpsRequest.InputStream Is Errer");
                EmpExecutionContext.error(e, "HttpsRequestService.HandleHttpsRequest.InputStream Is Errer");
            }
        }
        catch (Exception e)
        {
            params.setErrMsg("HttpsRequestService.HandleHttpsRequest Is Errer");
            EmpExecutionContext.error(e, "HttpsRequestService.HandleHttpsRequest Is Errer");
        }
        finally
        {
        	if(client != null){
//        		client.getConnectionManager().shutdown();
        		client.close();
        	}
            // 关闭HTTPS连接
            /*if(httpsURLConn != null)
            {
                httpsURLConn.disconnect();
            }*/
            // 关闭输入流
            if(inputStream != null)
            {
                inputStream.close();
            }
            // 写入日志，记录这次http请求的请求记录
            new TxtFileUtil().writeWeixMsgToFile("http", params.getMenuCode() + "，" + params.getErrMsg() + "。" + params.getUrl());
        }
        return params;
    }

    /**
     * @description 处理HTTPS的JSON返回格式
     * @param params
     *        HTTP请求传递参数类
     * @param inputStream
     *        输入流
     * @return
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-11-22 下午04:35:12
     */
    private HttpReturnParams getHttpsReturnJson(HttpReturnParams params, InputStream inputStream)
    {
        try
        {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(new BufferedReader(new InputStreamReader(inputStream, "UTF-8")));
            if(jsonObject != null)
            {
                params.setJsonObject(jsonObject);
                if(jsonObject.get("errcode") == null || "0".equals(jsonObject.get("errcode").toString()))
                {
                    // 表示成功
                    params.setErrCode("000");
                    params.setErrMsg("success");
                }
                else
                {
                    params.setErrCode(jsonObject.get("errcode").toString());
                    params.setErrMsg(jsonObject.get("errmsg").toString());
                }
            }
            else
            {
                params.setErrMsg("HttpsRequestService.getHttpsReturnJson Is Null");
            }
        }
        catch (Exception e)
        {
            params.setErrMsg("HttpsRequestService.getHttpsReturnJson Is Errer");
            EmpExecutionContext.error(e, "HttpsRequestService.getHttpsReturnJson Is Errer");
        }
        return params;
    }

    /**
     * @description 请求HTTPS接口 获取服务器上的流 进行下载写文件
     * @param params
     *        HTTP请求参数类
     * @param inputStream
     *        获取接口返回的输入流
     * @return HttpReturnParams
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-11-14 下午06:56:51
     */
    private HttpReturnParams getHttpsReturnFile(HttpReturnParams params, InputStream inputStream)
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
                params.setErrMsg("HttpsRequestService.getHttpsReturnFile writer file is fail");
            }
        }
        catch (Exception e)
        {
            params.setErrMsg("HttpsRequestService.getHttpsReturnFile Is Errer");
            EmpExecutionContext.error(e, "HttpsRequestService.getHttpsReturnFile Is Errer");
        }
        return params;
    }

    /**
     * 证书信任管理器（用于https请求） 这个证书管理器的作用就是让它信任我们指定的证书，上面的代码意味着信任所有证书，不管是否权威机构颁发。
     * 
     * @description
     * @project p_weix
     * @company ShenZhen Montnets Technology CO.,LTD.
     * @author fangyt <foyoto@gmail.com>
     * @datetime 2013-10-21 上午10:25:00
     */
    private static class TrustAnyTrustManager implements X509TrustManager
    {

        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException
        {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException
        {
        }

        public X509Certificate[] getAcceptedIssuers()
        {
            return new X509Certificate[] {};
        }
    }

    private class TrustAnyHostnameVerifier implements HostnameVerifier
    {
        public boolean verify(String hostname, SSLSession session)
        {
//            return true;
        	//TODO
			return hostname.equalsIgnoreCase(session.getPeerHost());
        }
    }

    private SSLContext getSSLContent() throws NoSuchAlgorithmException, KeyManagementException
    {
        // 创建SSLContext对象，并使用我们指定的信任管理器初始化
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, new TrustManager[] {new TrustAnyTrustManager()}, new java.security.SecureRandom());
        return sc;
    }

    public List<LfWeiUserInfo> addWeixUserInfo(List<String> openids, String accesstoken, Long aid, String corpCode,Long gId) throws Exception
    {
        //HttpsURLConnection httpsURLConn = null;
        InputStream inputStream = null;
        String url = "";
        //URL console = null;
//        HttpClient httpClient = null;
        //TODO
        CloseableHttpClient httpClient = null;
        List<LfWeiUserInfo> userInfoList = new ArrayList<LfWeiUserInfo>();
        ////System.out.println("~~~~~~开始~~~~~~~~"+new Timestamp(System.currentTimeMillis()));
        try
        {
            /*TrustAnyHostnameVerifier verifier = new TrustAnyHostnameVerifier();
            SSLSocketFactory factory = getSSLContent().getSocketFactory();*/
            JSONObject obj = null;
            LfWeiUserInfo userInfo = null;
            JSONParser jsonParser = new JSONParser();
            String openid = "";

            Long wcid = new SuperDAO().getIdByPro(Integer.parseInt(TableLfWeiUserInfo.SEQUENCE), openids.size());
            DownloadFile downloadFile = new DownloadFile();
            for (int i = 0; i < openids.size(); i++)
            {
                try
                {
                    openid = openids.get(i);
                    url = WeixHttpUrl.WX_GETUSERINFO_URL + "access_token=" + accesstoken + "&openid=" + openid;
                    
                    httpClient = new DefaultHttpClient();
                 // 设置连接超时时间
                    httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30*1000);
        			// 设置Socket超时时间
                    httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30*1000);
                    HttpPost post = new HttpPost(url);
                    
                    //StringEntity postingString = new StringEntity(json);// json传递  
                    //post.setEntity(postingString);  
                    //post.setHeader("Content-type", "application/json");  
                    HttpResponse response = httpClient.execute(post);  
                    //String content = EntityUtils.toString(response.getEntity()); 
                    
                    // 类 URL 代表一个统一资源定位符
                    /*console = new URL(url);
                    // 使用指定的 URL 创建 HttpsURLConnection。
                    httpsURLConn = (HttpsURLConnection) console.openConnection();
                    // 获取为安全 https URL 连接创建套接字时使用的 SSL 套接字工厂。
                    httpsURLConn.setSSLSocketFactory(factory);
                    // 设置此实例的 HostnameVerifier。
                    httpsURLConn.setHostnameVerifier(verifier);
                    // 设置 URL 请求的方法GET POST HEAD OPTIONS PUT DELETE TRACE
                    httpsURLConn.setRequestMethod("GET");
                    // URL 连接可用于输入和/或输出。
                    httpsURLConn.setDoInput(true);
                    // 请求超时，默认1分钟
                    httpsURLConn.setConnectTimeout(30 * 1000);*/
                    // 判断是否需要带上json格式的信息做请求操作
                    // 判断是否操作OutputStream类出现异常 true false
                    inputStream = response.getEntity().getContent();
                    if(inputStream != null)
                    {
                        obj = (JSONObject) jsonParser.parse(new BufferedReader(new InputStreamReader(inputStream, "UTF-8")));
                        if(obj.get("openid") != null && !"".equals(obj.get("openid")) && obj.get("subscribe") != null)
                        {
                            String subscribe = obj.get("subscribe").toString();
                            // 用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息
                            if("1".equals(subscribe))
                            {
                                userInfo = new LfWeiUserInfo();
                                userInfo.setSubscribe(subscribe);
                                if(obj.get("nickname") != null)
                                {
                                    userInfo.setNickName(obj.get("nickname").toString());
                                }
                                if(obj.get("sex") != null)
                                {
                                    userInfo.setSex(obj.get("sex").toString());
                                }
                                if(obj.get("language") != null)
                                {
                                    userInfo.setLanguage(obj.get("language").toString());
                                }
                                if(obj.get("city") != null)
                                {
                                    userInfo.setCity(obj.get("city").toString());
                                }
                                if(obj.get("province") != null)
                                {
                                    userInfo.setProvince(obj.get("province").toString());
                                }
                                if(obj.get("country") != null)
                                {
                                    userInfo.setCountry(obj.get("country").toString());
                                }
                                if(obj.get("headimgurl") != null && !obj.get("headimgurl").toString().equals(""))
                                {
                                    String headimgurl = obj.get("headimgurl").toString();
                                    String localimgrul = new TxtFileUtil().getWebRoot() + WXStaticValue.WEIX_USER + openid + ".jpg";
                                    userInfo.setHeadImgUrl(headimgurl);
                                    String result = downloadFile.downloadFileFromHttp(headimgurl, localimgrul);
                                    if(null != result && result.equals("success"))
                                    {
                                        userInfo.setLocalImgUrl(WXStaticValue.WEIX_USER + openid + ".jpg");
                                    }
                                    else
                                    {
                                        EmpExecutionContext.error("下载用户头像图片到本地失败！");
                                    }
                                }
                                if(obj.get("subscribe_time") != null)
                                {
                                    userInfo.setSubscribeTime(new Timestamp(Long.valueOf(obj.get("subscribe_time").toString())));
                                }
                                userInfo.setWcId(i + wcid - openids.size() + 1);
                                userInfo.setCorpCode(corpCode);
                                userInfo.setCreatetime(new Timestamp(System.currentTimeMillis()));
                                userInfo.setAId(aid);
                                userInfo.setOpenId(openid);
                                //如果gId同步过来的值为空，则将gId设置为0
                                if(gId==null){
                                    userInfo.setGId(0L);   
                                }else{
                                    userInfo.setGId(gId);     
                                }
                                
                                userInfoList.add(userInfo);
                                userInfo = null;
                                openid = "";
                            }
                            else
                            {
                                userInfo = null;
                                EmpExecutionContext.error("WeixBiz.addWeixUserInfo.LfWeiUserInfo.subscribe==0");
                            }
                        }
                    }
                
                }
                catch (Exception e)
                {
                    EmpExecutionContext.error(e, "WeixBiz.addWeixUserInfo openids is error");
                }finally{
                	if(httpClient != null){
//                		httpClient.getConnectionManager().shutdown();
                		httpClient.close();
                	}
                    // 关闭HTTPS连接
                    /*if(httpsURLConn != null)
                    {
                        httpsURLConn.disconnect();
                    }*/
                    // 关闭输入流
                    if(inputStream != null)
                    {
                        inputStream.close();
                    }
                }
            }
        }
        catch (Exception e)
        {
            userInfoList.clear();
            EmpExecutionContext.error(e, "HttpsRequestService.addWeixUserInfo Is Errer");
        }
        ////System.out.println("~~~~结束~~~~~~~~~~"+new Timestamp(System.currentTimeMillis()));
        

        return userInfoList;
    }
    
    
    
    public Map<String,LinkedHashMap<String, Object>> updateWeixUserInfo(Object[] intersectionStrings, String accesstoken, Long aid, String corpCode) throws Exception
    {
        //HttpsURLConnection httpsURLConn = null;
        InputStream inputStream = null;
        String url = "";
        //URL console = null;
        Map<String,LinkedHashMap<String, Object>> mapresult = new LinkedHashMap<String, LinkedHashMap<String,Object>>();
        //System.out.println("~~~~开始~~~~~~~~~~"+new Timestamp(System.currentTimeMillis()));
        try
        {
            /*TrustAnyHostnameVerifier verifier = new TrustAnyHostnameVerifier();
            SSLSocketFactory factory = getSSLContent().getSocketFactory();*/
            JSONObject obj = null;
            JSONParser jsonParser = new JSONParser();
            LinkedHashMap<String, Object> objectMap = null;
            DownloadFile downloadFile = new DownloadFile();
            String openid = "";
//            HttpClient httpClient = null;
            //TODO
            CloseableHttpClient httpClient = null;
            for (int i = 0; i < intersectionStrings.length; i++)
            {
                try
                {
                    objectMap = new LinkedHashMap<String, Object>();
                    openid = intersectionStrings[i].toString();
                    url = WeixHttpUrl.WX_GETUSERINFO_URL + "access_token=" + accesstoken + "&openid=" + openid;
                    
                    httpClient = new DefaultHttpClient();
				// 设置连接超时时间
				   httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30*1000);
				// 设置Socket超时时间
				   httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30*1000);
				   HttpPost post = new HttpPost(url);
				   
				   //StringEntity postingString = new StringEntity(json);// json传递  
				   //post.setEntity(postingString);  
				   //post.setHeader("Content-type", "application/json");  
				   HttpResponse response = httpClient.execute(post);
                    
                    /*// 类 URL 代表一个统一资源定位符
                    console = new URL(url);
                    // 使用指定的 URL 创建 HttpsURLConnection。
                    httpsURLConn = (HttpsURLConnection) console.openConnection();a
                    // 获取为安全 https URL 连接创建套接字时使用的 SSL 套接字工厂。
                    httpsURLConn.setSSLSocketFactory(factory);
                    // 设置此实例的 HostnameVerifier。
                    httpsURLConn.setHostnameVerifier(verifier);
                    // 设置 URL 请求的方法GET POST HEAD OPTIONS PUT DELETE TRACE
                    httpsURLConn.setRequestMethod("GET");
                    // URL 连接可用于输入和/或输出。
                    httpsURLConn.setDoInput(true);
                    // 请求超时，默认1分钟
                    httpsURLConn.setConnectTimeout(30 * 1000);
                    // 判断是否需要带上json格式的信息做请求操作
                    // 判断是否操作OutputStream类出现异常 true false
                    inputStream = httpsURLConn.getInputStream();*/
				   inputStream = response.getEntity().getContent();
                    if(inputStream != null)
                    {
                        obj = (JSONObject) jsonParser.parse(new BufferedReader(new InputStreamReader(inputStream, "UTF-8")));
                        
                        if(obj.get("openid") != null && !"".equals(obj.get("openid")) && obj.get("subscribe") != null)
                        {
                            String subscribe = String.valueOf(obj.get("subscribe"));
                            // 用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息
                            if("1".equals(subscribe))
                            {
                                objectMap.put("subscribe", subscribe);
                                if(obj.get("nickname") != null)
                                {
                                    objectMap.put("nickName", obj.get("nickname").toString());
                                }
                                if(obj.get("sex") != null)
                                {
                                    objectMap.put("sex", obj.get("sex").toString());
                                }
                                if(obj.get("language") != null)
                                {
                                    objectMap.put("language", obj.get("language").toString());
                                }
                                if(obj.get("city") != null)
                                {
                                    objectMap.put("city", obj.get("city").toString());
                                }
                                if(obj.get("province") != null)
                                {
                                    objectMap.put("province", obj.get("province").toString());
                                }
                                if(obj.get("country") != null)
                                {
                                    objectMap.put("country", obj.get("country").toString());
                                }
                                if(obj.get("headimgurl") != null && !obj.get("headimgurl").toString().equals(""))
                                {
                                    String headimgurl = obj.get("headimgurl").toString();
                                    String localimgrul = new TxtFileUtil().getWebRoot() + WXStaticValue.WEIX_USER + openid + ".jpg";
                                    objectMap.put("headimgurl", headimgurl);
                                    String result = downloadFile.downloadFileFromHttp(headimgurl, localimgrul);
                                    if(null != result && result.equals("success"))
                                    {
                                        objectMap.put("localImgUrl", WXStaticValue.WEIX_USER + openid + ".jpg");
                                    }
                                    else
                                    {
                                        EmpExecutionContext.error("下载用户头像图片到本地失败！");
                                    }
                                }
                                if(obj.get("subscribe_time") != null)
                                {
                                     objectMap.put("subscribe_time",obj.get("subscribe_time").toString());
                                }
                                mapresult.put(openid, objectMap);
                            }
                            else
                            {
                                EmpExecutionContext.error("WeixBiz.updateWeixUserInfo.LfWeiUserInfo.subscribe==0");
                            }
                        }
                    }
                
                }
                catch (Exception e)
                {
                    EmpExecutionContext.error(e, "WeixBiz.updateWeixUserInfo openids is error");
                }finally{
                	if(httpClient != null){
//                		httpClient.getConnectionManager().shutdown();
                		httpClient.close();
                	}
                    // 关闭HTTPS连接
                    /*if(httpsURLConn != null)
                    {
                        httpsURLConn.disconnect();
                    }*/
                    // 关闭输入流
                    if(inputStream != null)
                    {
                        inputStream.close();
                    }
                }
            }
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "HttpsRequestService.updateWeixUserInfo Is Error");
        }
        //System.out.println("~~~~结束~~~~~~~~~~"+new Timestamp(System.currentTimeMillis()));
        return mapresult;
    }

}
