package com.montnets.emp.shorturl.surlmanage.util;

import org.apache.http.client.HttpClient;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
/**
 * 连接池构造Httpclient实例
  * @ClassName: PoolManagerImpl
  * @Description: TODO
  * @Copyright: Copyright (c) 2016 
  * @Company: 深圳市梦网科技股份有限公司
  * @author hxq
  * @date 2016-8-9 下午04:49:33
  * @version V1.0
 */
public class PoolManagerImpl{
    private int maxTotal=20000;
    private int maxPerRoute=5000;
    public PoolManagerImpl(){
/*        String maxTotalStr=HttpClientHandlerFactory.getConfProValue("maxTotal");
        String maxPerRouteStr=HttpClientHandlerFactory.getConfProValue("maxPerRoute");
        //如果有配置文件
        if(maxTotalStr!=null){
            maxTotal=Integer.valueOf(maxTotalStr);
            System.out.println("连接池总最大连接数："+maxTotal);
        }
        if(maxPerRouteStr!=null){
            maxPerRoute=Integer.valueOf(maxPerRouteStr);
            System.out.println("连接池单个ip最大连接数："+maxPerRoute);
        }*/
    }
    public PoolManagerImpl(int maxTotal,int maxPerRoute){
        this.maxPerRoute=maxPerRoute;
        this.maxTotal=maxTotal;

        System.out.println("总最大连接数："+maxTotal);
        System.out.println("单个ip最大连接数："+maxPerRoute);
    }
    
    public HttpClient buildHttpClient() {
        ConnectionSocketFactory plainsf = PlainConnectionSocketFactory.getSocketFactory();
        LayeredConnectionSocketFactory sslsf = SSLConnectionSocketFactory.getSocketFactory();
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", plainsf)
                .register("https", sslsf)
                .build();
       PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
        // 最大连接总数
        cm.setMaxTotal(maxTotal);
        // 将每个路由(ip)连接
        cm.setDefaultMaxPerRoute(maxPerRoute);
        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        httpClientBuilder.setConnectionManager(cm);
        return httpClientBuilder.build();
    }
    
    
    
    
    

    
}
