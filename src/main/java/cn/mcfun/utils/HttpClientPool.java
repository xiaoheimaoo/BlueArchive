package cn.mcfun.utils;

import cn.mcfun.Main;
import cn.mcfun.entity.UserInfo;
import org.apache.http.*;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import java.io.*;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static cn.mcfun.utils.Hikari.getConnection;

public class HttpClientPool {

    public static String sendPost(UserInfo userInfo, String url, List<BasicNameValuePair> params) {
        HttpHost proxy;
        proxy = new HttpHost(userInfo.getIp().split(":")[0], Integer.parseInt(userInfo.getIp().split(":")[1]));
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
        if(userInfo.getHttpClientBuilder() == null){
            userInfo.setHttpClientBuilder(HttpClientBuilder.create().setDefaultCookieStore(new BasicCookieStore())
                    .setRoutePlanner(routePlanner)
                    .build());
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
    public static String postFileMultiPart(UserInfo userInfo, String url, byte[] builder) {
        if(userInfo.getIp() != null && !userInfo.getIp().equals("")){
            HttpHost proxy;
            proxy = new HttpHost(userInfo.getIp().split(":")[0], Integer.parseInt(userInfo.getIp().split(":")[1]));
            DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
            if(userInfo.getHttpClientBuilder() == null){
                userInfo.setHttpClientBuilder(HttpClientBuilder.create().setDefaultCookieStore(new BasicCookieStore())
                        .setRoutePlanner(routePlanner)
                        .build());
            }
        }else {
            if(userInfo.getHttpClientBuilder() == null){
                userInfo.setHttpClientBuilder(HttpClientBuilder.create().setDefaultCookieStore(new BasicCookieStore())
                        .build());
            }
        }
        RequestConfig defaultConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(defaultConfig);
        String boundary = Gzip.genRandomNum();
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
        httpPost.addHeader("Content-Type", "multipart/form-data; boundary=BestHTTP_HTTPMultiPartForm_"+boundary);
        // 拼接请求体
        StringBuilder requestBody1 = new StringBuilder();
        requestBody1.append("--BestHTTP_HTTPMultiPartForm_").append(boundary).append("\r\n");
        requestBody1.append("Content-Disposition: form-data; name=\"mx\"; filename=\"mx.dat\"").append("\r\n");
        requestBody1.append("Content-Type: application/octet-stream\r\n");
        requestBody1.append("Content-Length: ").append(builder.length).append("\r\n\r\n");
        // 转换为字节数组
        byte[] requestBodyBytes = new byte[0];
        try {
            requestBodyBytes = requestBody1.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringBuilder requestBody2 = new StringBuilder();
        requestBody2.append("\r\n--BestHTTP_HTTPMultiPartForm_").append(boundary).append("--");
        // 转换为字节数组
        byte[] requestBodyBytes2 = new byte[0];
        try {
            requestBodyBytes2 = requestBody2.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            outputStream.write(requestBodyBytes);
            outputStream.write(builder);
            outputStream.write(requestBodyBytes2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 设置请求体
        ByteArrayEntity byteArrayEntity = new ByteArrayEntity(outputStream.toByteArray());
        httpPost.setEntity(byteArrayEntity);
        CloseableHttpResponse response = null;
        String result = null;
        try {
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
