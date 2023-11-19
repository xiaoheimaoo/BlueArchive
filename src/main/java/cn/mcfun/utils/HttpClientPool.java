package cn.mcfun.utils;

import cn.mcfun.Main;
import cn.mcfun.entity.UserInfo;
import org.apache.http.*;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static cn.mcfun.utils.Hikari.getConnection;

public class HttpClientPool {
    public static String sendGet(UserInfo userInfo) {
        CloseableHttpClient httpClient = HttpClients.custom().build();
        CloseableHttpResponse response = null;
        HttpPost httpPost = new HttpPost("http://192.168.1.9:8888/captcha4");
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("captcha_id", "00b06e0a4ed58bd1c2ad59f1b054ade0"));
        //params.add(new BasicNameValuePair("proxy", proxy));
        String result = null;
        Connection conn2 = getConnection();
        String sql2 = "update `order` set `message`='正在获取验证码!' where `order`=?";
        PreparedStatement ps2 = null;
        try {
            ps2 = conn2.prepareStatement(sql2);
            ps2.setString(1,userInfo.getOrder());
            ps2.executeUpdate();
            conn2.close();
            ps2.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        int num = 0;
        while(num == 0){
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(params));
                response = httpClient.execute(httpPost);
                result = EntityUtils.toString(response.getEntity(), Charset.forName("utf-8"));
                System.out.println(result);
                num = 1;
            } catch (IOException e) {
                num = 0;
            }
        }
        return result;
    }
    public static String sendPost0(UserInfo userInfo, String url, List<BasicNameValuePair> params) {
        if(userInfo.getIp() != null){
            HttpHost proxy;
            proxy = new HttpHost(userInfo.getIp().split(":")[0], Integer.parseInt(userInfo.getIp().split(":")[1]));
            DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
            if(userInfo.getHttpClientBuilder() == null){
                userInfo.setHttpClientBuilder(HttpClientBuilder.create().setDefaultCookieStore(new BasicCookieStore())
                        .setRoutePlanner(routePlanner)
                        .build());
            }
        }else{
            if(userInfo.getHttpClientBuilder() == null){
                userInfo.setHttpClientBuilder(HttpClientBuilder.create().setDefaultCookieStore(new BasicCookieStore())
                        .build());
            }
        }
        RequestConfig defaultConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(defaultConfig);
        httpPost.setHeader("Bundle-Version", Main.BundleVersion);
        httpPost.addHeader("Connection", "keep-alive");
        CloseableHttpResponse response = null;
        String result = null;
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
            response = userInfo.getHttpClientBuilder().execute(httpPost);
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
        }
        return result;
    }

    public static String sendPost(UserInfo userInfo, String url, List<BasicNameValuePair> params) {
        if(userInfo.getIp() != null){
            HttpHost proxy;
            proxy = new HttpHost(userInfo.getIp().split(":")[0], Integer.parseInt(userInfo.getIp().split(":")[1]));
            DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
            if(userInfo.getHttpClientBuilder() == null){
                userInfo.setHttpClientBuilder(HttpClientBuilder.create().setDefaultCookieStore(new BasicCookieStore())
                        .setRoutePlanner(routePlanner)
                        .build());
            }
        }else{
            if(userInfo.getHttpClientBuilder() == null){
                userInfo.setHttpClientBuilder(HttpClientBuilder.create().setDefaultCookieStore(new BasicCookieStore())
                        .build());
            }
        }
        RequestConfig defaultConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(defaultConfig);
        httpPost.addHeader("Accept-Encoding", "gzip");
        httpPost.addHeader("User-Agent", "BlueArchive/1.36.236685 CFNetwork/1410.0.3 Darwin/22.6.0");
        httpPost.addHeader("Connection", "Keep-Alive");
        httpPost.addHeader("Host", "ba-jp-sdk.bluearchive.jp");
        CloseableHttpResponse response = null;
        String result = null;
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
            response = userInfo.getHttpClientBuilder().execute(httpPost);
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
        }
        return result;
    }
    public static String postFileMultiPart0(UserInfo userInfo, String url, MultipartEntityBuilder builder) {
        if(userInfo.getIp() != null){
            HttpHost proxy;
            proxy = new HttpHost(userInfo.getIp().split(":")[0], Integer.parseInt(userInfo.getIp().split(":")[1]));
            DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
            if(userInfo.getHttpClientBuilder() == null){
                userInfo.setHttpClientBuilder(HttpClientBuilder.create().setDefaultCookieStore(new BasicCookieStore())
                        .setRoutePlanner(routePlanner)
                        .build());
            }
        }else{
            if(userInfo.getHttpClientBuilder() == null){
                userInfo.setHttpClientBuilder(HttpClientBuilder.create().setDefaultCookieStore(new BasicCookieStore())
                        .build());
            }
        }
        RequestConfig defaultConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(defaultConfig);
        if(url.equals("https://prod-gateway.bluearchiveyostar.com:5100/api/gateway")){
            httpPost.setHeader("mx","1");
        }
        httpPost.setHeader("Bundle-Version",Main.BundleVersion);
        httpPost.addHeader("Accept-Encoding", "gzip");
        httpPost.addHeader("User-Agent", "BestHTTP/2 v2.4.0");
        httpPost.addHeader("Connection", "Keep-Alive, TE");
        httpPost.addHeader("Keep-Alive", "timeout=21");
        httpPost.addHeader("TE", "identity");
        httpPost.addHeader("Host", "prod-gateway.bluearchiveyostar.com:5100");
        CloseableHttpResponse response = null;
        String result = null;
        HttpEntity multipart = builder.build();
        try {
            httpPost.setEntity(multipart);
            response = userInfo.getHttpClientBuilder().execute(httpPost);
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
        }
        return result;
    }
    public static String postFileMultiPart(UserInfo userInfo, String url, MultipartEntityBuilder builder) {
        if(userInfo.getIp() != null){
            HttpHost proxy;
            proxy = new HttpHost(userInfo.getIp().split(":")[0], Integer.parseInt(userInfo.getIp().split(":")[1]));
            DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
            if(userInfo.getHttpClientBuilder() == null){
                userInfo.setHttpClientBuilder(HttpClientBuilder.create().setDefaultCookieStore(new BasicCookieStore())
                        .setRoutePlanner(routePlanner)
                        .build());
            }
        }else{
            if(userInfo.getHttpClientBuilder() == null){
                userInfo.setHttpClientBuilder(HttpClientBuilder.create().setDefaultCookieStore(new BasicCookieStore())
                        .build());
            }
        }
        RequestConfig defaultConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(defaultConfig);
        if(url.equals("https://prod-game.bluearchiveyostar.com:5000/api/gateway")){
            httpPost.setHeader("mx","1");
        }
        httpPost.setHeader("Bundle-Version",Main.BundleVersion);
        httpPost.addHeader("Accept-Encoding", "gzip");
        httpPost.addHeader("User-Agent", "BestHTTP/2 v2.4.0");
        httpPost.addHeader("Connection", "Keep-Alive, TE");
        httpPost.addHeader("Keep-Alive", "timeout=21");
        httpPost.addHeader("TE", "identity");
        httpPost.addHeader("Host", "prod-game.bluearchiveyostar.com:5000");
        CloseableHttpResponse response = null;
        String result = null;
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        HttpEntity multipart = builder.build();
        try {
            httpPost.setEntity(multipart);
            response = userInfo.getHttpClientBuilder().execute(httpPost);
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
        }
        return result;
    }

}