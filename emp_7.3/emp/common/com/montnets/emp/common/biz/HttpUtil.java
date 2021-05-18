package com.montnets.emp.common.biz;

import com.montnets.emp.common.context.EmpExecutionContext;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @功能概要：
 * @项目名称： emp_ftp
 * @初创作者： Administrator
 * @公司名称： ShenZhen Montnets Technology CO.,LTD.
 * @创建时间： 2016/3/26 16:19
 * <p>修改记录1：</p>
 * <pre>
 *      修改日期：
 *      修改人：
 *      修改内容：
 * </pre>
 */
public class HttpUtil {

    public static String post(String action, Map<String, String> parameters){
        String content = null;
        DefaultHttpClient httpclient = null;
        try {
            content = null;
            httpclient = new DefaultHttpClient();

            HttpParams params = httpclient.getParams();
            if(params==null){
                params = new BasicHttpParams();
            }
            //请求超时设置
            HttpConnectionParams.setConnectionTimeout(params, 10*1000);
            //读取超时设置
            HttpConnectionParams.setSoTimeout(params, 15*1000);

            httpclient.setParams(params);
            HttpPost httpPost = new HttpPost(action);
            if(parameters != null){
                List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                for (Map.Entry<String, String> entry : parameters.entrySet()) {
                    String name = entry.getKey();
                    if (name == null) {
                        continue;
                    }
                    String value = entry.getValue();
                    nvps.add(new BasicNameValuePair(name, value));
                }

                httpPost.setEntity(new UrlEncodedFormEntity(nvps));
            }

            HttpResponse response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                return "";
            }
            content = EntityUtils.toString(entity);
            httpclient.getConnectionManager().shutdown();
        } catch (Exception e) {
            EmpExecutionContext.error("请求异常["+action+"]"+e.getMessage());
        }finally{
            if(httpclient != null){
                httpclient.close();
            }
        }
        return content;
    }


    //检测请求状态
    public static int checkState(String action){
        int state = HttpURLConnection.HTTP_BAD_REQUEST;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(action);
            connection = (HttpURLConnection) url.openConnection();
            connection.setUseCaches(false);
            connection.setConnectTimeout(200);
            connection.connect();
            state = connection.getResponseCode();
        } catch (Exception e) {
            EmpExecutionContext.error("请求异常["+action+"]"+e.getMessage());
        }
        finally {
            if(connection != null)
            {
                connection.disconnect();
            }
        }
        return state;
    }

}
