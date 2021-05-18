package com.montnets.shaoguanga.config;

import org.springframework.context.annotation.Configuration;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

/**
 * @Author WJH
 * @Description
 * @date 2021/4/9 8:50
 * @Email ibytecode2020@gmail.com
 */
@Configuration
public class HttpClientConfig {
    @Value("${http.maxTotal}")
    private Integer maxTotal;
    @Value("${http.defaultMaxPerRoute}")
    private Integer defaultMaxPerRout;
    @Value("${http.connectionTimeout}")
    private Integer connectionTimeout;
    @Value("${http.connectionRequestTimeout}")
    private Integer connectionRequestTimeout;
    @Value("${http.socketTimeout}")
    private Integer socketTimeout;

    /**
     * 实例化连接池
     * @return 连接池
     */
    @Bean("poolingHttpClientConnectionManager")
    public PoolingHttpClientConnectionManager poolingHttpClientConnectionManager() {
        PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
        manager.setMaxTotal(maxTotal);
        manager.setDefaultMaxPerRoute(defaultMaxPerRout);
        return manager;
    }

    /**
     * 将HTTPClientBuilder放入ico容器
     * @param manager
     * @return HTTPClientBuilder
     */
    @Bean(name = "httpClientBuilder")
    public HttpClientBuilder httpClientBuilder(@Qualifier("poolingHttpClientConnectionManager") PoolingHttpClientConnectionManager manager) {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        httpClientBuilder.setConnectionManager(manager);
        return httpClientBuilder;
    }

    /**
     * 构造HTTPClient
     * @param httpClientBuilder
     * @return HTTPClient
     */
    @Bean(name = "httpClient")
    public CloseableHttpClient closeableHttpClient(@Qualifier("httpClientBuilder") HttpClientBuilder httpClientBuilder) {
        return httpClientBuilder.build();
    }

    /**
     * 设置请求相关配置
     * @return RequestConfig.Builder
     */
    @Bean(name = "builder")
    public RequestConfig.Builder builder() {
        return RequestConfig.custom()
                .setConnectTimeout(connectionTimeout)
                .setConnectionRequestTimeout(connectionRequestTimeout)
                .setSocketTimeout(socketTimeout);
    }

    /**
     * 设置请求相关配置
     * @param builder
     * @return requestConfig
     */
    @Bean(name = "requestConfig")
    public RequestConfig requestConfig(@Qualifier("builder") RequestConfig.Builder builder) {
        return builder.build();
    }
}
