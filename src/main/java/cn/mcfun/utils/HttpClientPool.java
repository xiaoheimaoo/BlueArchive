package cn.mcfun.utils;

import cn.mcfun.Main;
import cn.mcfun.entity.UserInfo;
import org.apache.http.*;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static cn.mcfun.utils.Hikari.getConnection;

public class HttpClientPool {
    static HttpClientBuilder httpClientBuilder = HttpClients.custom();
    public HttpClientPool() {
        SSLConnectionSocketFactory sslFactory = new SSLConnectionSocketFactory(SSLContexts.createSystemDefault(),
                new String[]{"TLSv1.2"},
                null,
                SSLConnectionSocketFactory.getDefaultHostnameVerifier());

        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https",sslFactory)
                .build();
        PoolingHttpClientConnectionManager pool = new PoolingHttpClientConnectionManager(registry);
        pool.setMaxTotal(1000);
        pool.setDefaultMaxPerRoute(1000);
        httpClientBuilder.setConnectionManager(pool);
        httpClientBuilder.setConnectionManagerShared(true);
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(1000 * 90) // 创建链接 （TCP协议的三次握手）超时时间
                .setSocketTimeout(1000 * 90) // 响应 获取响应内容 超时时间
                .setConnectionRequestTimeout(1000 * 30) // 从链接池 获取链接的超时时间
                .build();

        httpClientBuilder.setDefaultRequestConfig(requestConfig);
    }
    public static String sendPost(UserInfo userInfo, String url, List<BasicNameValuePair> params) {
        CloseableHttpClient httpClient;
        if(userInfo.getIp() != null && !userInfo.getIp().equals("")){
            HttpHost proxy;
            proxy = new HttpHost(userInfo.getIp().split(":")[0], Integer.parseInt(userInfo.getIp().split(":")[1]));
            DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
            CredentialsProvider provider = new BasicCredentialsProvider();
            provider.setCredentials(new AuthScope(proxy), new UsernamePasswordCredentials("dsa", "dsa"));
            httpClient = httpClientBuilder
                    .setDefaultCredentialsProvider(provider)
                    .setRoutePlanner(routePlanner)
                    .setDefaultCookieStore(userInfo.getCookie()).build();
        }else{
            httpClient = httpClientBuilder
                    .setDefaultCookieStore(userInfo.getCookie()).build();
        }
        RequestConfig defaultConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(defaultConfig);
        httpPost.setHeader("Bundle-Version",Main.BundleVersion);
        httpPost.addHeader("Accept-Encoding", "gzip");
        httpPost.addHeader("User-Agent", "BestHTTP/2 v2.4.0");
        httpPost.addHeader("Connection", "Keep-Alive, TE");
        httpPost.addHeader("Keep-Alive", "timeout=21");
        httpPost.addHeader("TE", "identity");
        httpPost.addHeader("Host", "prod-game.bluearchiveyostar.com:5000");
        CloseableHttpResponse response = null;
        String result = null;
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
            response = httpClient.execute(httpPost);
            result = EntityUtils.toString(response.getEntity(), Charset.forName("utf-8"));
        } catch (Exception e) {
            try {
                Connection conn2 = getConnection();
                String sql2 = "update `order` set `message`='网络异常，正在重试!',`status`=0 where `order`=?";
                PreparedStatement ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1,userInfo.getOrder());
                ps2.executeUpdate();
                conn2.close();
                ps2.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            Thread.currentThread().stop();
        }finally {
            try {
                httpPost.releaseConnection();
                if(response != null){
                    response.close();
                }
                if(httpClient != null){
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    public static String postFileMultiPart(UserInfo userInfo, String url, MultipartEntityBuilder builder) {
        CloseableHttpClient httpClient;
        if(userInfo.getIp() != null && !userInfo.getIp().equals("")){
            HttpHost proxy;
            proxy = new HttpHost(userInfo.getIp().split(":")[0], Integer.parseInt(userInfo.getIp().split(":")[1]));
            DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
            CredentialsProvider provider = new BasicCredentialsProvider();
            provider.setCredentials(new AuthScope(proxy), new UsernamePasswordCredentials("dsa", "dsa"));
            httpClient = httpClientBuilder
                    .setDefaultCredentialsProvider(provider)
                    .setRoutePlanner(routePlanner)
                    .setDefaultCookieStore(userInfo.getCookie()).build();
        }else{
            httpClient = httpClientBuilder
                    .setDefaultCookieStore(userInfo.getCookie()).build();
        }
        RequestConfig defaultConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(defaultConfig);
        httpPost.setHeader("Bundle-Version",Main.BundleVersion);
        httpPost.addHeader("Accept-Encoding", "gzip");
        httpPost.addHeader("User-Agent", "BestHTTP/2 v2.4.0");
        httpPost.addHeader("Connection", "Keep-Alive, TE");
        httpPost.addHeader("Keep-Alive", "timeout=21");
        httpPost.addHeader("TE", "identity");
        httpPost.addHeader("Host", "prod-game.bluearchiveyostar.com:5000");
        CloseableHttpResponse response = null;
        String result = null;
        HttpEntity multipart = builder.build();
        try {
            httpPost.setEntity(multipart);
            response = httpClient.execute(httpPost);
            result = EntityUtils.toString(response.getEntity(), Charset.forName("utf-8"));
        } catch (Exception e) {
            try {
                Connection conn2 = getConnection();
                String sql2 = "update `order` set message='网络异常，正在重试!',status=0 where `order`=? and status=1";
                PreparedStatement ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1,userInfo.getOrder());
                ps2.executeUpdate();
                conn2.close();
                ps2.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            Thread.currentThread().stop();
        }finally {
            try {
                httpPost.releaseConnection();
                if(response != null){
                    response.close();
                }
                if(httpClient != null){
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

}
