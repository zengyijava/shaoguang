/**
 * Program : TrustSSL.java
 * Author : Administrator
 * Create : 2013-9-13 下午05:03:13
 * company ShenZhen Montnets Technology CO.,LTD.
 */

package com.montnets.emp.weix.common.util;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.net.ssl.*;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * https 请求接口调用
 *
 * @author zousy <zousy999@qq.com>
 * @project p_weix
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-24 上午10:22:23
 * @description
 */
public class TrustSSL {
    private SSLContext getSSLContent() throws NoSuchAlgorithmException, KeyManagementException {
        // 创建SSLContext对象，并使用我们指定的信任管理器初始化
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, new TrustManager[]{new TrustAnyTrustManager()}, new java.security.SecureRandom());
        return sc;
    }

    /**
     * 获取凭证
     *
     * @param url
     * @param appId
     * @param appSecret
     * @return {"access_token":"ACCESS_TOKEN","expires_in":7200}
     * @throws Exception
     * @author Administrator
     * @create 2013-9-13 下午05:20:16
     * @since
     */
    public JSONObject getRemoteAccessToken(String url, String appId, String appSecret) throws Exception {
        URL console = new URL(url + "&appid=" + appId + "&secret=" + appSecret);
        HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
        conn.setSSLSocketFactory(getSSLContent().getSocketFactory());
        conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
        conn.connect();
        InputStream is = conn.getInputStream();
        JSONObject json = (JSONObject) new JSONParser().parse(new InputStreamReader(is));
        return json;
    }

    /**
     * 发布菜单
     *
     * @param url
     * @param jsonMenu
     * @param token
     * @return
     * @throws Exception
     * @author Administrator
     * @create 2013-9-14 上午09:51:19
     * @since
     */
    public JSONObject createRemoteMenu(String url, String jsonMenu, String token) throws Exception {
        URL console = new URL(url + "?access_token=" + token);
        HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
        conn.setSSLSocketFactory(getSSLContent().getSocketFactory());
        conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        OutputStream out = conn.getOutputStream();
        if (jsonMenu != null) {
            // 编码
            out.write(jsonMenu.getBytes("UTF-8"));
        }
        out.flush();
        out.close();
        JSONObject jsonObject = (JSONObject) new JSONParser().parse(new InputStreamReader(conn.getInputStream()));
        return jsonObject;
    }

    /**
     * 判断当前token是否可用
     *
     * @param token      当前token
     * @param accessTime token生成时间
     * @return
     * @description 当前时间c在上次获取token的时间100分钟前，表明token没有过期
     * @author zousy <zousy999@qq.com>
     * @datetime 2013-9-27 上午10:22:13
     */
    public boolean isAlive(String token, Timestamp accessTime) {
        if (!GlobalMethods.isInvalidString(token) && accessTime != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(accessTime);
            cal.add(Calendar.MINUTE, 100);
            Calendar c = Calendar.getInstance();
            if (c.before(cal)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 证书信任管理器（用于https请求） 这个证书管理器的作用就是让它信任我们指定的证书，上面的代码意味着信任所有证书，不管是否权威机构颁发。
     *
     * @author fangyt <foyoto@gmail.com>
     * @description
     * @project p_weix
     * @company ShenZhen Montnets Technology CO.,LTD.
     * @datetime 2013-10-21 上午10:25:00
     */
    private static class TrustAnyTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }

    private class TrustAnyHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
//            return true;
        	//TODO
			return hostname.equalsIgnoreCase(session.getPeerHost());
        }
    }
}
