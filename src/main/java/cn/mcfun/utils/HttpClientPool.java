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
    public static String sendPost0(UserInfo userInfo, String url, List<BasicNameValuePair> params) {
        HttpHost proxy;
        proxy = new HttpHost("127.0.0.1", 8888);
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
        CredentialsProvider provider = new BasicCredentialsProvider();
        provider.setCredentials(new AuthScope(proxy), new UsernamePasswordCredentials("brd-customer-hl_9c2c7022-zone-data_center", "gd1j0yrsnz63"));
        CloseableHttpClient httpClient;
        httpClient = userInfo.getHttpClientBuilder()
                /*.setDefaultCredentialsProvider(provider)
                .setRoutePlanner(routePlanner)*/
                .setDefaultCookieStore(userInfo.getCookie()).build();
        RequestConfig defaultConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(defaultConfig);
        httpPost.setHeader("Bundle-Version", Main.BundleVersion);
        CloseableHttpResponse response;
        String result = null;
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
            response = httpClient.execute(httpPost);
            result = EntityUtils.toString(response.getEntity(), Charset.forName("utf-8"));
        } catch (IOException e) {
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
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    public static String sendPost(UserInfo userInfo, String url, List<BasicNameValuePair> params) {
        /*HttpHost proxy;
        proxy = new HttpHost("zproxy.lum-superproxy.io", 22225);
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
        CredentialsProvider provider = new BasicCredentialsProvider();
        provider.setCredentials(new AuthScope(proxy), new UsernamePasswordCredentials("brd-customer-hl_9c2c7022-zone-data_center", "gd1j0yrsnz63"));*/
        CloseableHttpClient httpClient;
        httpClient = userInfo.getHttpClientBuilder()
                /*.setDefaultCredentialsProvider(provider)
                .setRoutePlanner(routePlanner)*/
                .setDefaultCookieStore(userInfo.getCookie()).build();
        RequestConfig defaultConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(defaultConfig);
        httpPost.setHeader("Bundle-Version", Main.BundleVersion);
        CloseableHttpResponse response;
        String result = null;
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
            response = httpClient.execute(httpPost);
            result = EntityUtils.toString(response.getEntity(), Charset.forName("utf-8"));
        } catch (IOException e) {
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
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    public static String postFileMultiPart(UserInfo userInfo, String url, MultipartEntityBuilder builder) {
        HttpHost proxy;
        proxy = new HttpHost("127.0.0.1", 8888);
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
        CredentialsProvider provider = new BasicCredentialsProvider();
        provider.setCredentials(new AuthScope(proxy), new UsernamePasswordCredentials("brd-customer-hl_9c2c7022-zone-data_center", "gd1j0yrsnz63"));
        CloseableHttpClient httpClient;
        httpClient = userInfo.getHttpClientBuilder()
                /*.setDefaultCredentialsProvider(provider)
                .setRoutePlanner(routePlanner)*/
                .setDefaultCookieStore(userInfo.getCookie()).build();
        RequestConfig defaultConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(defaultConfig);
        httpPost.setHeader("Bundle-Version",Main.BundleVersion);
        CloseableHttpResponse response;
        String result = null;
        HttpEntity multipart = builder.build();
        try {
            httpPost.setEntity(multipart);
            response = httpClient.execute(httpPost);
            result = EntityUtils.toString(response.getEntity(), Charset.forName("utf-8"));
        } catch (IOException e) {
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
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

}
