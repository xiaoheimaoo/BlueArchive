package cn.mcfun.api;

import cn.mcfun.Main;
import cn.mcfun.entity.UserInfo;
import cn.mcfun.utils.Gzip;
import cn.mcfun.utils.HashMatching;
import cn.mcfun.utils.HttpClientPool;
import cn.mcfun.utils.Md5;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static cn.mcfun.utils.Hikari.getConnection;

public class UserCreate {
    ContentType strContent = ContentType.APPLICATION_OCTET_STREAM;

    public void transcode_verify(UserInfo userInfo) {
        userInfo.setDeviceId(UUID.randomUUID().toString().toUpperCase());
        String result;
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("uid", userInfo.getUid()));
        params.add(new BasicNameValuePair("transcode", userInfo.getTranscode()));
        result = HttpClientPool.sendPost(userInfo, "https://ba-jp-sdk.bluearchive.jp/user/transcode_verify", params);
        JSONObject jsonObject = null;
        try {
            jsonObject = JSONObject.parseObject(result);
        } catch (Exception e) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=?";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
        if (result.contains("\"result\":0")) {
            userInfo.setToken(jsonObject.getString("token"));
            Connection conn2 = getConnection();
            String sql2 = "update `order` set `token`=?,`message`='生成新的token' where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, userInfo.getToken());
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

        } else {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
    }

    public void userCreate(UserInfo userInfo) {
        userInfo.setDeviceId(UUID.randomUUID().toString().toUpperCase());
        String result;
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("channelId", "appstore"));
        params.add(new BasicNameValuePair("deviceId", userInfo.getDeviceId()));
        result = HttpClientPool.sendPost0(userInfo, "https://ba-jp-sdk.bluearchive.jp/user/create", params);
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (result.contains("\"result\":0")) {
            userInfo.setUid(jsonObject.getString("uid"));
            userInfo.setToken(jsonObject.getString("token"));
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='生成新的uid和token' where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        } else {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
    }

    public void userLogin(UserInfo userInfo) {
        String geetest = HttpClientPool.sendGet(userInfo);
        JSONObject js = JSONObject.parseObject(geetest);
        while(!js.getJSONObject("data").getJSONObject("code").getString("result").equals("success")){
            geetest = HttpClientPool.sendGet(userInfo);
            js = JSONObject.parseObject(geetest);
        }
        String result;
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("deviceId", userInfo.getDeviceId()));
        params.add(new BasicNameValuePair("uid", userInfo.getUid()));
        params.add(new BasicNameValuePair("token", userInfo.getToken()));
        params.add(new BasicNameValuePair("platform", "ios"));
        params.add(new BasicNameValuePair("captcha_output", js.getJSONObject("data").getJSONObject("code").getString("captcha_output")));
        params.add(new BasicNameValuePair("gen_time", js.getJSONObject("data").getJSONObject("code").getString("gen_time")));
        params.add(new BasicNameValuePair("captcha_id", js.getJSONObject("data").getJSONObject("code").getString("captcha_id")));
        params.add(new BasicNameValuePair("lot_number", js.getJSONObject("data").getJSONObject("code").getString("lot_number")));
        params.add(new BasicNameValuePair("pass_token", js.getJSONObject("data").getJSONObject("code").getString("pass_token")));
        result = HttpClientPool.sendPost0(userInfo, "https://ba-jp-sdk.bluearchive.jp/user/login", params);
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (result.contains("\"result\":0")) {
            userInfo.setAccessToken(jsonObject.getString("accessToken"));
            Connection conn2 = getConnection();
            String sql2 = "update `order` set `uid`=?,`accessToken`=?,message='生成accessToken' where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, userInfo.getUid());
                ps2.setString(2, userInfo.getAccessToken());
                ps2.setString(3, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        } else {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
    }

    public void getTicket(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());
        String packet = "{\"Protocol\":50000,\"YostarUID\":" + userInfo.getUid() + ",\"YostarToken\":\"" + userInfo.getAccessToken() + "\",\"WaitingTicket\":\"\",\"ClientVersion\":\"" + Main.ClientVersion + "\",\"Resendable\":true}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart0(userInfo, "https://prod-gateway.bluearchiveyostar.com:5100/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (result.contains("EnterTicket")) {
            JSONObject js = JSONObject.parseObject(jsonObject.getString("packet"));
            userInfo.setEnterTicket(js.getString("EnterTicket"));
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='生成EnterTicket' where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        } else {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
    }

    public void checkYostar(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());
        String packet = "{\"Protocol\":1009,\"EnterTicket\":\"" + userInfo.getEnterTicket() + "\",\"Cookie\":\"3065181BCC_" + Md5.getMd5(userInfo.getUid()) + "_" + Md5.getMd5(userInfo.getEnterTicket()) + "\",\"Hash\":4333622001665}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (result.contains("SessionKey")) {
            JSONObject js = JSONObject.parseObject(jsonObject.getString("packet"));
            userInfo.setSessionKey(js.getString("SessionKey"));
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='生成SessionKey' where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

        } else {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
    }

    public void accountAuth(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());
        String packet = "{\"Protocol\":1002,\"Version\":0,\"DevId\":null,\"IMEI\":0,\"AccessIP\":\"127.0.0.1\",\"MarketId\":\"aas\",\"UserType\":null,\"AdvertisementId\":null,\"OSType\":\"I\",\"OSVersion\":\"iPadOS 16.2\",\"DeviceUniqueId\":\"" + userInfo.getDeviceId() + "\",\"DeviceModel\":\"iPad11,1\",\"DeviceSystemMemorySize\":2923,\"ClientUpTime\":0,\"Resendable\":true,\"Hash\":4303557230594,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":0}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (result.contains("packet")) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='尝试登陆账号' where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        } else {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
    }

    public void ProofToken_RequestQuestion(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());
        String packet = "{\"Protocol\":37000,\"ClientUpTime\":27,\"Resendable\":true,\"Hash\":158913789952007,\"IsTest\":false,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        try {
            jsonObject = JSONObject.parseObject(result);
        } catch (Exception e) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=?";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
        if (result.contains("packet")) {
            JSONObject js = JSONObject.parseObject(jsonObject.getString("packet"));
            if (js.containsKey("SessionKey")) {
                userInfo.setSessionKey(js.getString("SessionKey"));
                userInfo.setHint(js.getString("Hint"));
                userInfo.setQuestion(js.getString("Question"));
            }
            Connection conn2 = getConnection();
            String sql2 = "update `order` set `SessionKey`=?,`message`='获取Question' where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, userInfo.getSessionKey());
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        } else {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
    }

    public void ProofToken_Submit(UserInfo userInfo) {
        userInfo.setAnswer(HashMatching.solve(userInfo.getHint(),userInfo.getQuestion()));
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());
        String packet = "{\"Answer\":"+userInfo.getAnswer()+",\"Protocol\":37001,\"ClientUpTime\":0,\"Resendable\":true,\"Hash\":158918084919306,\"IsTest\":false,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        try {
            jsonObject = JSONObject.parseObject(result);
        } catch (Exception e) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=?";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
        if (result.contains("packet")) {
            JSONObject js = JSONObject.parseObject(jsonObject.getString("packet"));
            if (js.containsKey("SessionKey")) {
                userInfo.setSessionKey(js.getString("SessionKey"));
            }
            Connection conn2 = getConnection();
            String sql2 = "update `order` set `SessionKey`=?,`message`='提交Answer' where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, userInfo.getSessionKey());
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        } else {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
    }

    public void accountCreate(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());
        String packet = "{\"Protocol\":1000,\"DevId\":null,\"Version\":0,\"IMEI\":0,\"AccessIP\":null,\"MarketId\":null,\"UserType\":null,\"AdvertisementId\":null,\"OSType\":null,\"OSVersion\":null,\"ClientUpTime\":0,\"Resendable\":true,\"Hash\":4294967296003,\"IsTest\":false,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":0}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (result.contains("SessionKey")) {
            JSONObject js = JSONObject.parseObject(jsonObject.getString("packet"));
            userInfo.setSessionKey(js.getString("SessionKey"));
            userInfo.setAccountId(js.getLong("AccountId"));
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='生成AccountId' where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        } else {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
    }

    public void nickname(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());
        String packet = "{\"Protocol\":1001,\"Nickname\":\"アカウ\",\"ClientUpTime\":27,\"Resendable\":true,\"Hash\":4299262263320,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        if (!result.contains("Error")) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='设置昵称1' where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        } else {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
    }

    public void callname(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());
        String packet = "{\"Protocol\":1010,\"CallName\":\"アカウ\",\"ClientUpTime\":14,\"Resendable\":true,\"Hash\":4337916968985,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        if (!result.contains("Error")) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='设置昵称2' where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        } else {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
    }

    public void accountAuth2(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());
        String packet = "{\"Protocol\":1002,\"Version\":0,\"DevId\":null,\"IMEI\":0,\"AccessIP\":\"127.0.0.1\",\"MarketId\":\"aas\",\"UserType\":null,\"AdvertisementId\":null,\"OSType\":\"I\",\"OSVersion\":\"iPadOS 16.2\",\"DeviceUniqueId\":\"" + userInfo.getDeviceId() + "\",\"DeviceModel\":\"iPad11,1\",\"DeviceSystemMemorySize\":2923,\"ClientUpTime\":0,\"Resendable\":true,\"Hash\":4303557230594,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (result.contains("packet")) {
            JSONObject js = JSONObject.parseObject(jsonObject.getString("packet"));
            if (js.containsKey("SessionKey")) {
                userInfo.setSessionKey(js.getString("SessionKey"));
            }
            Connection conn2 = getConnection();
            String sql2 = "update `order` set `deviceId`=?,`SessionKey`=?,message='设置SessionKey' where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, userInfo.getDeviceId());
                ps2.setString(2, userInfo.getSessionKey());
                ps2.setString(3, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        } else {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
    }

    public void ProofToken_RequestQuestion2(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());
        String packet = "{\"Protocol\":37000,\"ClientUpTime\":27,\"Resendable\":true,\"Hash\":158913789952007,\"IsTest\":false,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        try {
            jsonObject = JSONObject.parseObject(result);
        } catch (Exception e) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=?";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
        if (result.contains("packet")) {
            JSONObject js = JSONObject.parseObject(jsonObject.getString("packet"));
            if (js.containsKey("SessionKey")) {
                userInfo.setSessionKey(js.getString("SessionKey"));
                userInfo.setHint(js.getString("Hint"));
                userInfo.setQuestion(js.getString("Question"));
            }
            Connection conn2 = getConnection();
            String sql2 = "update `order` set `SessionKey`=?,`message`='获取Question' where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, userInfo.getSessionKey());
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        } else {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
    }

    public void accountLoginsync(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());
        String packet = "{\"Protocol\":1017,\"SyncProtocols\":[20000,1003,2000,3000,4000,5000,12000,6000,22001,17018,21000,28001,33000,19000,10006,39006,44000,29002,30041],\"ClientUpTime\":20,\"Resendable\":true,\"Hash\":4367981740041,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = null;
        try {
            jsonObject = JSONObject.parseObject(result);
        } catch (Exception e) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=?";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
        if (result.contains("packet")) {
            JSONObject js = JSONObject.parseObject(jsonObject.getString("packet"));
            userInfo.setEchelonDBs(js.getJSONObject("EchelonListResponse").getJSONArray("EchelonDBs").getJSONObject(0));
            userInfo.setCharacterDBs(js.getJSONObject("CharacterListResponse").getJSONArray("CharacterDBs"));
            for (int i = 0; i < userInfo.getCharacterDBs().size(); i++) {
                if (userInfo.getCharacterDBs().getJSONObject(i).getString("StarGrade").equals("3")) {
                    userInfo.setStarNum(userInfo.getStarNum() + 1);
                }
            }
            userInfo.setGem(js.getJSONObject("AccountCurrencySyncResponse").getJSONObject("AccountCurrencyDB").getJSONObject("CurrencyDict").getInteger("Gem"));
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='获取loginsync' where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        } else {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
    }

    public void ProofToken_Submit2(UserInfo userInfo) {
        userInfo.setAnswer(HashMatching.solve(userInfo.getHint(),userInfo.getQuestion()));
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());
        String packet = "{\"Answer\":"+userInfo.getAnswer()+",\"Protocol\":37001,\"ClientUpTime\":0,\"Resendable\":true,\"Hash\":158918084919306,\"IsTest\":false,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        try {
            jsonObject = JSONObject.parseObject(result);
        } catch (Exception e) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=?";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
        if (result.contains("packet")) {
            JSONObject js = JSONObject.parseObject(jsonObject.getString("packet"));
            if (js.containsKey("SessionKey")) {
                userInfo.setSessionKey(js.getString("SessionKey"));
            }
            Connection conn2 = getConnection();
            String sql2 = "update `order` set `SessionKey`=?,`message`='提交Answer' where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, userInfo.getSessionKey());
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        } else {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
    }

    //流程1
    public void Account_GetTutorial(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());
        String packet = "{\"Protocol\":1005,\"ClientUpTime\":0,\"Resendable\":true,\"Hash\":4316442132493,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        try {
            jsonObject = JSONObject.parseObject(result);
        } catch (Exception e) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=?";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
        if (result.contains("packet")) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='跳过引导教程' where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

        } else {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
    }

    public void Mission_List(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());
        String packet = "{\"Protocol\":8000,\"EventContentId\":null,\"ClientUpTime\":0,\"Resendable\":true,\"Hash\":34359738368014,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        try {
            jsonObject = JSONObject.parseObject(result);
        } catch (Exception e) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=?";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
        if (result.contains("packet")) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='获取任务列表' where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

        } else {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
    }

    public void Mission_List2(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());
        String packet = "{\"Protocol\":8000,\"EventContentId\":822,\"ClientUpTime\":0,\"Resendable\":true,\"Hash\":34359738368015,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        try {
            jsonObject = JSONObject.parseObject(result);
        } catch (Exception e) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=?";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
        if (result.contains("packet")) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='获取任务列表' where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

        } else {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
    }

    public void Scenario_Skip(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());
        String packet = "{\"Protocol\":19003,\"ScriptGroupId\":11000,\"SkipPointScriptCount\":36,\"ClientUpTime\":124,\"Resendable\":true,\"Hash\":81617263525905,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        try {
            jsonObject = JSONObject.parseObject(result);
        } catch (Exception e) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=?";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
        if (result.contains("packet")) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='跳过剧情1' where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

        } else {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
    }

    public void Account_SetTutorial2(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());
        String packet = "{\"Protocol\":1006,\"TutorialIds\":[1],\"ClientUpTime\":67,\"Resendable\":true,\"Hash\":4320737099794,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        try {
            jsonObject = JSONObject.parseObject(result);
        } catch (Exception e) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=?";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
        if (result.contains("packet")) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='跳过引导' where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

        } else {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
    }

    public void Scenario_Skip2(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());

        String packet = "{\"Protocol\":19003,\"ScriptGroupId\":11001,\"SkipPointScriptCount\":3,\"ClientUpTime\":17,\"Resendable\":true,\"Hash\":81617263525907,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        try {
            jsonObject = JSONObject.parseObject(result);
        } catch (Exception e) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=?";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
        if (result.contains("packet")) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='跳过剧情2' where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

        } else {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
    }

    public void Scenario_Skip3(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());

        String packet = "{\"Protocol\":19003,\"ScriptGroupId\":11002,\"SkipPointScriptCount\":4,\"ClientUpTime\":6,\"Resendable\":true,\"Hash\":81617263525908,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        try {
            jsonObject = JSONObject.parseObject(result);
        } catch (Exception e) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=?";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
        if (result.contains("packet")) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='跳过剧情3' where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

        } else {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
    }

    public void Scenario_Skip4(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());

        String packet = "{\"Protocol\":19003,\"ScriptGroupId\":11007,\"SkipPointScriptCount\":6,\"ClientUpTime\":57,\"Resendable\":true,\"Hash\":81617263525909,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        try {
            jsonObject = JSONObject.parseObject(result);
        } catch (Exception e) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=?";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
        if (result.contains("packet")) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='跳过剧情4' where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

        } else {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
    }

    public void Scenario_Skip5(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());

        String packet = "{\"Protocol\":19003,\"ScriptGroupId\":11003,\"SkipPointScriptCount\":2,\"ClientUpTime\":23,\"Resendable\":true,\"Hash\":81617263525910,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        try {
            jsonObject = JSONObject.parseObject(result);
        } catch (Exception e) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=?";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
        if (result.contains("packet")) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='跳过剧情5' where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

        } else {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
    }

    public void Scenario_Skip6(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());

        String packet = "{\"Protocol\":19003,\"ScriptGroupId\":11004,\"SkipPointScriptCount\":2,\"ClientUpTime\":10,\"Resendable\":true,\"Hash\":81617263525911,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        try {
            jsonObject = JSONObject.parseObject(result);
        } catch (Exception e) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=?";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
        if (result.contains("packet")) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='跳过剧情6' where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

        } else {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
    }

    public void Account_SetTutorial3(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());

        String packet = "{\"Protocol\":1006,\"TutorialIds\":[1,2],\"ClientUpTime\":39,\"Resendable\":true,\"Hash\":4320737099800,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        try {
            jsonObject = JSONObject.parseObject(result);
        } catch (Exception e) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=?";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
        if (result.contains("packet")) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='跳过引导2' where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

        } else {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
    }

    public void Scenario_Skip7(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());

        String packet = "{\"Protocol\":19003,\"ScriptGroupId\":11005,\"SkipPointScriptCount\":2,\"ClientUpTime\":7,\"Resendable\":true,\"Hash\":81617263525913,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        try {
            jsonObject = JSONObject.parseObject(result);
        } catch (Exception e) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=?";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
        if (result.contains("packet")) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='跳过剧情7' where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

        } else {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
    }

    public void Scenario_Skip8(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());

        String packet = "{\"Protocol\":19003,\"ScriptGroupId\":11008,\"SkipPointScriptCount\":2,\"ClientUpTime\":6,\"Resendable\":true,\"Hash\":81617263525914,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        try {
            jsonObject = JSONObject.parseObject(result);
        } catch (Exception e) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=?";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
        if (result.contains("packet")) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='跳过剧情8' where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

        } else {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
    }

    //流程2
    public void OpenCondition_EventList(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());

        String packet = "{\"Protocol\":15002,\"ConquestEventIds\":[822],\"WorldRaidSeasonAndGroupIds\":{\"821\":821602},\"ClientUpTime\":1656,\"Resendable\":true,\"Hash\":64433099374606,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        try {
            jsonObject = JSONObject.parseObject(result);
        } catch (Exception e) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=?";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
        if (result.contains("packet")) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='获取eventlist' where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

        } else {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
    }

    public void Notification_EventContentReddotCheck(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());

        String packet = "{\"Protocol\":36001,\"ClientUpTime\":0,\"Resendable\":true,\"Hash\":154623117623311,\"IsTest\":false,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        try {
            jsonObject = JSONObject.parseObject(result);
        } catch (Exception e) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=?";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
        if (result.contains("packet")) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='获取eventcontentreddotcheck' where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

        } else {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
    }

    public void Mail_Check(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());
        String packet = "{\"Protocol\":7001,\"ClientUpTime\":1,\"Resendable\":true,\"Hash\":30069066039312,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        try {
            jsonObject = JSONObject.parseObject(result);
        } catch (Exception e) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=?";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
        if (result.contains("packet")) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='检查邮件' where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

        } else {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
    }

    public void Event_RewardIncrease(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());

        String packet = "{\"Protocol\":25003,\"ClientUpTime\":0,\"Resendable\":true,\"Hash\":107387067301905,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        try {
            jsonObject = JSONObject.parseObject(result);
        } catch (Exception e) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=?";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
        if (result.contains("packet")) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='事件奖励' where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

        } else {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
    }

    public void Clan_Check(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());

        String packet = "{\"Protocol\":28019,\"ClientUpTime\":0,\"Resendable\":true,\"Hash\":120340688666642,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        try {
            jsonObject = JSONObject.parseObject(result);
        } catch (Exception e) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=?";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
        if (result.contains("packet")) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='检查clan' where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

        } else {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
    }

    public void Billing_PurchaseListByYostar(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());
        String packet = "{\"Protocol\":29002,\"ClientUpTime\":578,\"Resendable\":true,\"Hash\":124562641518612,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        try {
            jsonObject = JSONObject.parseObject(result);
        } catch (Exception e) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=?";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
        if (result.contains("packet")) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='获取purchaselistbyyostar' where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

        } else {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
    }

    public void Shop_BeforehandGachaRun(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());

        String packet = "{\"Protocol\":10011,\"ShopUniqueId\":3,\"GoodsId\":1,\"ClientUpTime\":8,\"Resendable\":true,\"Hash\":42996917600280,\"IsTest\":false,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        try {
            jsonObject = JSONObject.parseObject(result);
        } catch (Exception e) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=?";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
        if (result.contains("packet")) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='新手抽卡' where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

        } else {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
    }
    public void Shop_BeforehandGachaPick(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());

        String packet = "{\"Protocol\":10013,\"ShopUniqueId\":3,\"GoodsId\":1,\"TargetIndex\":0,\"ClientUpTime\":38,\"Resendable\":true,\"Hash\":43005507534876,\"IsTest\":false,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        try {
            jsonObject = JSONObject.parseObject(result);
        } catch (Exception e) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=?";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
        if (result.contains("packet")) {
            JSONObject js = JSONObject.parseObject(jsonObject.getString("packet"));
            JSONArray GachaResults = js.getJSONArray("GachaResults");
            for (int i = 0; i < GachaResults.size(); i++) {
                if (GachaResults.getJSONObject(i).containsKey("Character") && GachaResults.getJSONObject(i).getJSONObject("Character").getString("StarGrade").equals("3")) {
                    userInfo.setStarNum(userInfo.getStarNum() + 1);
                }
            }
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='新手抽卡' where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

        } else {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
    }

    public void Account_SetTutorial(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());

        String packet = "{\"Protocol\":1006,\"TutorialIds\":[1,2,3],\"ClientUpTime\":0,\"Resendable\":true,\"Hash\":4320737099798,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        try {
            jsonObject = JSONObject.parseObject(result);
        } catch (Exception e) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=?";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
        if (result.contains("packet")) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='跳过引导3' where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

        } else {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
    }

    public void Campaign_List(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());
        String packet = "{\"Protocol\":6000,\"ClientUpTime\":385,\"Resendable\":true,\"Hash\":25769803776023,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        try {
            jsonObject = JSONObject.parseObject(result);
        } catch (Exception e) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=?";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
        if (result.contains("packet")) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='获取campaign列表' where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

        } else {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
    }

    public void Event_RewardIncrease2(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());
        String packet = "{\"Protocol\":25003,\"ClientUpTime\":1,\"Resendable\":true,\"Hash\":107387067301912,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        try {
            jsonObject = JSONObject.parseObject(result);
        } catch (Exception e) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=?";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
        if (result.contains("packet")) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='获取事件奖励' where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

        } else {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
    }

    public void Mail_Check2(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());
        String packet = "{\"Protocol\":7001,\"ClientUpTime\":118,\"Resendable\":true,\"Hash\":30069066039321,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        try {
            jsonObject = JSONObject.parseObject(result);
        } catch (Exception e) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=?";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
        if (result.contains("packet")) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='邮箱检查' where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

        } else {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
    }

    public void Campaign_EnterMainStage(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());

        String packet = "{\"Protocol\":6001,\"StageUniqueId\":1011101,\"ClientUpTime\":1,\"Resendable\":true,\"Hash\":25774098743322,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        try {
            jsonObject = JSONObject.parseObject(result);
        } catch (Exception e) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=?";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
        if (result.contains("packet")) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='获取entermainstage' where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

        } else {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
    }

    public void Echelon_List(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());
        String packet = "{\"Protocol\":5000,\"ClientUpTime\":146,\"Resendable\":true,\"Hash\":21474836480027,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        try {
            jsonObject = JSONObject.parseObject(result);
        } catch (Exception e) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=?";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
        if (result.contains("packet")) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='获取echelon列表' where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

        } else {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
    }

    public void Echelon_PresetList(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());
        String packet = "{\"Protocol\":5002,\"ClientUpTime\":1,\"Resendable\":true,\"Hash\":21483426414620,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        try {
            jsonObject = JSONObject.parseObject(result);
        } catch (Exception e) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=?";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
        if (result.contains("packet")) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='获取礼物盒列表' where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

        } else {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
    }

    public void Campaign_DeployEchelon(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());

        String packet = "{\"Protocol\":6003,\"StageUniqueId\":1011101,\"DeployedEchelons\":[{\"EntityId\":1,\"HpInfos\":null,\"DyingInfos\":{},\"BuffInfos\":null,\"ActionCountMax\":0,\"ActionCount\":0,\"Mobility\":0,\"StrategySightRange\":0,\"Id\":-1,\"Rotate\":{\"x\":0.0,\"y\":0.0,\"z\":0.0},\"Location\":{\"x\":-2,\"y\":2,\"z\":0},\"AIDestination\":{\"x\":0,\"y\":0,\"z\":0},\"IsActionComplete\":false,\"IsPlayer\":true,\"IsFixedEchelon\":false,\"MovementOrder\":0,\"MaxTSSCount\":0,\"RemainTSSCount\":0,\"RewardParcelInfosWithDropTacticEntityType\":null,\"SkillCardHand\":null,\"PlayAnimation\":false}],\"ClientUpTime\":170,\"Resendable\":true,\"Hash\":25782688677917,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        try {
            jsonObject = JSONObject.parseObject(result);
        } catch (Exception e) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=?";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
        if (result.contains("packet")) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='获取deployechelon' where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

        } else {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
    }

    public void Campaign_ConfirmTutorialStage(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());

        String packet = "{\"Protocol\":6015,\"StageUniqueId\":1011101,\"ClientUpTime\":85,\"Resendable\":true,\"Hash\":25834228285470,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        try {
            jsonObject = JSONObject.parseObject(result);
        } catch (Exception e) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=?";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
        if (result.contains("packet")) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='获取confirmtutorialstage' where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

        } else {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
    }

    public void Campaign_MapMove(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());

        String packet = "{\"Protocol\":6005,\"StageUniqueId\":1011101,\"EchelonEntityId\":1,\"DestPosition\":{\"x\":-1,\"y\":1,\"z\":0},\"ClientUpTime\":59,\"Resendable\":true,\"Hash\":25791278612511,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        try {
            jsonObject = JSONObject.parseObject(result);
        } catch (Exception e) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=?";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
        if (result.contains("packet")) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='移动位置' where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

        } else {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
    }

    public void Campaign_EnterTactic(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());

        String packet = "{\"Protocol\":6007,\"StageUniqueId\":1011101,\"EchelonIndex\":1,\"EnemyIndex\":10008,\"ClientUpTime\":3,\"Resendable\":true,\"Hash\":25799868547104,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        try {
            jsonObject = JSONObject.parseObject(result);
        } catch (Exception e) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=?";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
        if (result.contains("packet")) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='移动位置' where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            
        } else {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
    }

    public void Campaign_TacticResult(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());
        String packet = "{\"Protocol\":6008,\"PassCheckCharacter\":false,\"Summary\":{\"HashKey\":0,\"IsBossBattle\":false,\"BattleType\":1,\"StageId\":1011101,\"GroundId\":101110101,\"Winner\":\"Group01\",\"EndType\":4,\"EndFrame\":1038,\"Group01Summary\":{\"TeamId\":1,\"LeaderEntityId\":{\"uniqueId\":16777217},\"Heroes\":[{\"ServerId\":" + userInfo.getEchelonDBs().getJSONArray("MainSlotServerIds").getString(0) + ",\"BattleEntityId\":{\"uniqueId\":16777217},\"HeroId\":13010,\"Grade\":2,\"Level\":1,\"ExSkillLevel\":1,\"PublicSkillLevel\":1,\"PassiveSkillLevel\":1,\"ExtraPassiveSkillLevel\":-1,\"StatSnapshotCollection\":[{\"Stat\":1,\"Start\":3398,\"End\":3398},{\"Stat\":2,\"Start\":131,\"End\":131},{\"Stat\":3,\"Start\":30,\"End\":34},{\"Stat\":4,\"Start\":1624,\"End\":1624},{\"Stat\":5,\"Start\":100,\"End\":100},{\"Stat\":7,\"Start\":1440,\"End\":1440},{\"Stat\":9,\"Start\":200,\"End\":200},{\"Stat\":12,\"Start\":20000,\"End\":20000},{\"Stat\":30,\"Start\":10000,\"End\":10000},{\"Stat\":34,\"Start\":10000,\"End\":10000},{\"Stat\":42,\"Start\":700,\"End\":700},{\"Stat\":44,\"Start\":0,\"End\":0}],\"HPRateBefore\":10000,\"HPRateAfter\":7683,\"CrowdControlCount\":0,\"CrowdControlDuration\":0,\"EvadeCount\":73,\"DamageImmuneCount\":0,\"CrowdControlImmuneCount\":0,\"DeadFrame\":-1,\"TacticEntityType\":1,\"GivenNumericLogs\":[{\"EntityType\":\"Character\",\"Category\":1,\"Source\":1,\"CalculatedSum\":2934,\"AppliedSum\":626,\"Count\":13,\"CriticalMultiplierMax\":15000,\"CriticalCount\":2,\"CalculatedMin\":206,\"CalculatedMax\":258,\"AppliedMin\":0,\"AppliedMax\":122},{\"EntityType\":\"Character\",\"Category\":1,\"Source\":3,\"CalculatedSum\":12832,\"AppliedSum\":314,\"Count\":18,\"CriticalMultiplierMax\":15000,\"CriticalCount\":3,\"CalculatedMin\":634,\"CalculatedMax\":774,\"AppliedMin\":0,\"AppliedMax\":80}],\"TakenNumericLogs\":[{\"EntityType\":\"Character\",\"Category\":1,\"Source\":1,\"CalculatedSum\":3651,\"AppliedSum\":788,\"Count\":39,\"CriticalMultiplierMax\":15000,\"CriticalCount\":5,\"CalculatedMin\":42,\"CalculatedMax\":103,\"AppliedMin\":13,\"AppliedMax\":30}],\"SkillCount\":{\"PublicSkill01\":2},\"KillLog\":{\"16777221\":526,\"16777225\":761,\"16777228\":959}},{\"ServerId\":" + userInfo.getEchelonDBs().getJSONArray("MainSlotServerIds").getString(1) + ",\"BattleEntityId\":{\"uniqueId\":16777218},\"HeroId\":16003,\"Grade\":1,\"Level\":1,\"ExSkillLevel\":1,\"PublicSkillLevel\":1,\"PassiveSkillLevel\":-1,\"ExtraPassiveSkillLevel\":-1,\"StatSnapshotCollection\":[{\"Stat\":1,\"Start\":2532,\"End\":2532},{\"Stat\":2,\"Start\":204,\"End\":204},{\"Stat\":3,\"Start\":21,\"End\":21},{\"Stat\":4,\"Start\":1499,\"End\":1499},{\"Stat\":5,\"Start\":690,\"End\":690},{\"Stat\":7,\"Start\":788,\"End\":788},{\"Stat\":9,\"Start\":197,\"End\":197},{\"Stat\":12,\"Start\":20000,\"End\":20000},{\"Stat\":30,\"Start\":10000,\"End\":10000},{\"Stat\":34,\"Start\":10000,\"End\":10000},{\"Stat\":42,\"Start\":700,\"End\":700},{\"Stat\":44,\"Start\":0,\"End\":0}],\"HPRateBefore\":10000,\"HPRateAfter\":10000,\"CrowdControlCount\":0,\"CrowdControlDuration\":0,\"EvadeCount\":0,\"DamageImmuneCount\":0,\"CrowdControlImmuneCount\":0,\"DeadFrame\":-1,\"TacticEntityType\":1,\"GivenNumericLogs\":[{\"EntityType\":\"Character\",\"Category\":1,\"Source\":1,\"CalculatedSum\":4088,\"AppliedSum\":1039,\"Count\":11,\"CriticalMultiplierMax\":15000,\"CriticalCount\":2,\"CalculatedMin\":332,\"CalculatedMax\":406,\"AppliedMin\":34,\"AppliedMax\":132},{\"EntityType\":\"Character\",\"Category\":1,\"Source\":3,\"CalculatedSum\":8296,\"AppliedSum\":84,\"Count\":10,\"CriticalMultiplierMax\":15000,\"CriticalCount\":1,\"CalculatedMin\":746,\"CalculatedMax\":918,\"AppliedMin\":0,\"AppliedMax\":76}],\"SkillCount\":{\"PublicSkill01\":1},\"KillLog\":{\"16777220\":221,\"16777223\":285,\"16777224\":761,\"16777229\":817,\"16777226\":1037}},{\"ServerId\":" + userInfo.getEchelonDBs().getJSONArray("MainSlotServerIds").getString(2) + ",\"BattleEntityId\":{\"uniqueId\":16777219},\"HeroId\":13003,\"Grade\":2,\"Level\":1,\"ExSkillLevel\":1,\"PublicSkillLevel\":1,\"PassiveSkillLevel\":1,\"ExtraPassiveSkillLevel\":-1,\"StatSnapshotCollection\":[{\"Stat\":1,\"Start\":2620,\"End\":2620},{\"Stat\":2,\"Start\":389,\"End\":389},{\"Stat\":3,\"Start\":21,\"End\":21},{\"Stat\":4,\"Start\":1619,\"End\":1619},{\"Stat\":5,\"Start\":897,\"End\":897},{\"Stat\":7,\"Start\":199,\"End\":199},{\"Stat\":9,\"Start\":199,\"End\":199},{\"Stat\":12,\"Start\":20000,\"End\":28806},{\"Stat\":30,\"Start\":10000,\"End\":10000},{\"Stat\":34,\"Start\":10000,\"End\":10000},{\"Stat\":42,\"Start\":700,\"End\":700},{\"Stat\":44,\"Start\":0,\"End\":0}],\"HPRateBefore\":10000,\"HPRateAfter\":10000,\"CrowdControlCount\":0,\"CrowdControlDuration\":0,\"EvadeCount\":0,\"DamageImmuneCount\":0,\"CrowdControlImmuneCount\":0,\"DeadFrame\":-1,\"TacticEntityType\":1,\"GivenNumericLogs\":[{\"EntityType\":\"Character\",\"Category\":1,\"Source\":1,\"CalculatedSum\":714,\"AppliedSum\":192,\"Count\":4,\"CriticalMultiplierMax\":23806,\"CriticalCount\":1,\"CalculatedMin\":170,\"CalculatedMax\":192,\"AppliedMin\":0,\"AppliedMax\":157}],\"SkillCount\":{\"PublicSkill01\":2},\"KillLog\":{\"16777222\":457,\"16777227\":861}}],\"Supporters\":[{\"ServerId\":" + userInfo.getEchelonDBs().getJSONArray("SupportSlotServerIds").getString(0) + ",\"BattleEntityId\":{\"uniqueId\":1073741825},\"HeroId\":26000,\"Grade\":1,\"Level\":1,\"ExSkillLevel\":1,\"PublicSkillLevel\":1,\"PassiveSkillLevel\":-1,\"ExtraPassiveSkillLevel\":-1,\"StatSnapshotCollection\":[{\"Stat\":1,\"Start\":2515,\"End\":2515},{\"Stat\":2,\"Start\":122,\"End\":122},{\"Stat\":3,\"Start\":25,\"End\":25},{\"Stat\":4,\"Start\":2399,\"End\":2399},{\"Stat\":5,\"Start\":99,\"End\":99},{\"Stat\":7,\"Start\":1093,\"End\":1093},{\"Stat\":9,\"Start\":198,\"End\":198},{\"Stat\":12,\"Start\":20000,\"End\":20000},{\"Stat\":30,\"Start\":10000,\"End\":10000},{\"Stat\":34,\"Start\":10000,\"End\":10000},{\"Stat\":42,\"Start\":700,\"End\":700},{\"Stat\":44,\"Start\":0,\"End\":0}],\"HPRateBefore\":0,\"HPRateAfter\":0,\"CrowdControlCount\":0,\"CrowdControlDuration\":0,\"EvadeCount\":0,\"DamageImmuneCount\":0,\"CrowdControlImmuneCount\":0,\"DeadFrame\":-1,\"TacticEntityType\":1}],\"UseAutoSkill\":false,\"TssUseCount\":0,\"SkillCostSummary\":{\"InitialCost\":0.0,\"CostPerFrameSnapshots\":[{\"Frame\":61,\"Regen\":0.009333333}],\"CostAddSnapshots\":[],\"CostUseSnapshots\":[]}},\"Group02Summary\":{\"TeamId\":10008,\"LeaderEntityId\":{\"uniqueId\":0},\"Heroes\":[{\"ServerId\":0,\"BattleEntityId\":{\"uniqueId\":16777220},\"HeroId\":7000001,\"DeadFrame\":221,\"TacticEntityType\":2},{\"ServerId\":0,\"BattleEntityId\":{\"uniqueId\":16777221},\"HeroId\":7000011,\"DeadFrame\":526,\"TacticEntityType\":2},{\"ServerId\":0,\"BattleEntityId\":{\"uniqueId\":16777222},\"HeroId\":7000011,\"DeadFrame\":457,\"TacticEntityType\":2},{\"ServerId\":0,\"BattleEntityId\":{\"uniqueId\":16777223},\"HeroId\":7000001,\"DeadFrame\":285,\"TacticEntityType\":2},{\"ServerId\":0,\"BattleEntityId\":{\"uniqueId\":16777224},\"HeroId\":7000001,\"DeadFrame\":761,\"TacticEntityType\":2},{\"ServerId\":0,\"BattleEntityId\":{\"uniqueId\":16777225},\"HeroId\":7000001,\"DeadFrame\":761,\"TacticEntityType\":2},{\"ServerId\":0,\"BattleEntityId\":{\"uniqueId\":16777226},\"HeroId\":7000011,\"DeadFrame\":1037,\"TacticEntityType\":2},{\"ServerId\":0,\"BattleEntityId\":{\"uniqueId\":16777227},\"HeroId\":7000011,\"DeadFrame\":861,\"TacticEntityType\":2},{\"ServerId\":0,\"BattleEntityId\":{\"uniqueId\":16777228},\"HeroId\":7000011,\"DeadFrame\":959,\"TacticEntityType\":2},{\"ServerId\":0,\"BattleEntityId\":{\"uniqueId\":16777229},\"HeroId\":7000001,\"DeadFrame\":817,\"TacticEntityType\":2}],\"UseAutoSkill\":false,\"TssUseCount\":0,\"SkillCostSummary\":{\"InitialCost\":0.0,\"CostPerFrameSnapshots\":[],\"CostAddSnapshots\":[],\"CostUseSnapshots\":[]}},\"ElapsedRealtime\":26.5865326},\"Hand\":{\"Cost\":9.127959,\"SkillCardsInHand\":[{\"CharacterId\":16003,\"HandIndex\":0,\"SkillId\":-1520051293,\"RemainCoolTime\":0},{\"CharacterId\":13010,\"HandIndex\":1,\"SkillId\":945851505,\"RemainCoolTime\":0},{\"CharacterId\":13003,\"HandIndex\":2,\"SkillId\":-12458155,\"RemainCoolTime\":0},{\"CharacterId\":26000,\"HandIndex\":3,\"SkillId\":1922876793,\"RemainCoolTime\":0}]},\"SkipSummary\":null,\"ClientUpTime\":40,\"Resendable\":true,\"Hash\":25804163514401,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        try {
            jsonObject = JSONObject.parseObject(result);
        } catch (Exception e) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=?";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
        if (result.contains("packet")) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='获取战斗结果' where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            
        } else {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
    }

    public void Campaign_EndTurn(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());

        String packet = "{\"Protocol\":6006,\"StageUniqueId\":1011101,\"ClientUpTime\":865,\"Resendable\":true,\"Hash\":25795573579810,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        try {
            jsonObject = JSONObject.parseObject(result);
        } catch (Exception e) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=?";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
        if (result.contains("packet")) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='跳转下一关卡' where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

        } else {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
    }

    public void Campaign_EndTurn2(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());

        String packet = "{\"Protocol\":6006,\"StageUniqueId\":1011101,\"ClientUpTime\":2,\"Resendable\":true,\"Hash\":25795573579811,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        try {
            jsonObject = JSONObject.parseObject(result);
        } catch (Exception e) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=?";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
        if (result.contains("packet")) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='跳转下一关卡' where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

        } else {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
    }

    public void Campaign_MapMove2(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());

        String packet = "{\"Protocol\":6005,\"StageUniqueId\":1011101,\"EchelonEntityId\":1,\"DestPosition\":{\"x\":0,\"y\":0,\"z\":0},\"ClientUpTime\":118,\"Resendable\":true,\"Hash\":25791278612516,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        try {
            jsonObject = JSONObject.parseObject(result);
        } catch (Exception e) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=?";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
        if (result.contains("packet")) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='移动角色' where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

        } else {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
    }

    public void Campaign_EnterTactic2(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());

        String packet = "{\"Protocol\":6007,\"StageUniqueId\":1011101,\"EchelonIndex\":1,\"EnemyIndex\":10009,\"ClientUpTime\":2,\"Resendable\":true,\"Hash\":25799868547109,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        try {
            jsonObject = JSONObject.parseObject(result);
        } catch (Exception e) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=?";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
        if (result.contains("packet")) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='获取战斗结果' where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

        } else {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
    }

    public void Campaign_TacticResult2(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());

        String packet = "{\"Protocol\":6008,\"PassCheckCharacter\":false,\"Summary\":{\"HashKey\":0,\"IsBossBattle\":true,\"BattleType\":1,\"StageId\":1011101,\"GroundId\":101110102,\"Winner\":\"Group01\",\"EndType\":4,\"EndFrame\":2135,\"Group01Summary\":{\"TeamId\":1,\"LeaderEntityId\":{\"uniqueId\":16777217},\"Heroes\":[{\"ServerId\":" + userInfo.getEchelonDBs().getJSONArray("MainSlotServerIds").getString(0) + ",\"BattleEntityId\":{\"uniqueId\":16777217},\"HeroId\":13010,\"Grade\":2,\"Level\":1,\"ExSkillLevel\":1,\"PublicSkillLevel\":1,\"PassiveSkillLevel\":1,\"ExtraPassiveSkillLevel\":-1,\"StatSnapshotCollection\":[{\"Stat\":1,\"Start\":3398,\"End\":3398},{\"Stat\":2,\"Start\":131,\"End\":131},{\"Stat\":3,\"Start\":30,\"End\":34},{\"Stat\":4,\"Start\":1624,\"End\":1624},{\"Stat\":5,\"Start\":100,\"End\":100},{\"Stat\":7,\"Start\":1440,\"End\":1440},{\"Stat\":9,\"Start\":200,\"End\":200},{\"Stat\":12,\"Start\":20000,\"End\":20000},{\"Stat\":30,\"Start\":10000,\"End\":10000},{\"Stat\":34,\"Start\":10000,\"End\":10000},{\"Stat\":42,\"Start\":700,\"End\":700},{\"Stat\":44,\"Start\":0,\"End\":0}],\"HPRateBefore\":7680,\"HPRateAfter\":1895,\"CrowdControlCount\":0,\"CrowdControlDuration\":0,\"EvadeCount\":163,\"DamageImmuneCount\":0,\"CrowdControlImmuneCount\":0,\"DeadFrame\":-1,\"TacticEntityType\":1,\"GivenNumericLogs\":[{\"EntityType\":\"Character\",\"Category\":1,\"Source\":1,\"CalculatedSum\":7326,\"AppliedSum\":1897,\"Count\":32,\"CriticalMultiplierMax\":15000,\"CriticalCount\":4,\"CalculatedMin\":204,\"CalculatedMax\":258,\"AppliedMin\":0,\"AppliedMax\":122},{\"EntityType\":\"Character\",\"Category\":1,\"Source\":3,\"CalculatedSum\":21718,\"AppliedSum\":942,\"Count\":31,\"CriticalMultiplierMax\":15000,\"CriticalCount\":3,\"CalculatedMin\":622,\"CalculatedMax\":778,\"AppliedMin\":0,\"AppliedMax\":84},{\"EntityType\":\"Obstacle\",\"Category\":1,\"Source\":1,\"CalculatedSum\":59,\"AppliedSum\":38,\"Count\":1,\"CriticalMultiplierMax\":10000,\"CriticalCount\":0,\"CalculatedMin\":59,\"CalculatedMax\":59,\"AppliedMin\":38,\"AppliedMax\":38}],\"TakenNumericLogs\":[{\"EntityType\":\"Character\",\"Category\":1,\"Source\":1,\"CalculatedSum\":9410,\"AppliedSum\":1967,\"Count\":78,\"CriticalMultiplierMax\":15000,\"CriticalCount\":10,\"CalculatedMin\":42,\"CalculatedMax\":211,\"AppliedMin\":13,\"AppliedMax\":61}],\"SkillCount\":{\"PublicSkill01\":4,\"ExSkill01\":1},\"KillLog\":{\"16777221\":266,\"16777223\":434,\"16777225\":764,\"16777227\":973,\"16777228\":1042,\"16777231\":1528,\"16777232\":1690}},{\"ServerId\":" + userInfo.getEchelonDBs().getJSONArray("MainSlotServerIds").getString(1) + ",\"BattleEntityId\":{\"uniqueId\":16777218},\"HeroId\":16003,\"Grade\":1,\"Level\":1,\"ExSkillLevel\":1,\"PublicSkillLevel\":1,\"PassiveSkillLevel\":-1,\"ExtraPassiveSkillLevel\":-1,\"StatSnapshotCollection\":[{\"Stat\":1,\"Start\":2532,\"End\":2532},{\"Stat\":2,\"Start\":204,\"End\":204},{\"Stat\":3,\"Start\":21,\"End\":21},{\"Stat\":4,\"Start\":1499,\"End\":1499},{\"Stat\":5,\"Start\":690,\"End\":690},{\"Stat\":7,\"Start\":788,\"End\":788},{\"Stat\":9,\"Start\":197,\"End\":197},{\"Stat\":12,\"Start\":20000,\"End\":20000},{\"Stat\":30,\"Start\":10000,\"End\":10000},{\"Stat\":34,\"Start\":10000,\"End\":10000},{\"Stat\":42,\"Start\":700,\"End\":700},{\"Stat\":44,\"Start\":0,\"End\":0}],\"HPRateBefore\":10000,\"HPRateAfter\":10000,\"CrowdControlCount\":0,\"CrowdControlDuration\":0,\"EvadeCount\":0,\"DamageImmuneCount\":0,\"CrowdControlImmuneCount\":0,\"DeadFrame\":-1,\"TacticEntityType\":1,\"GivenNumericLogs\":[{\"EntityType\":\"Character\",\"Category\":1,\"Source\":1,\"CalculatedSum\":11880,\"AppliedSum\":2766,\"Count\":32,\"CriticalMultiplierMax\":15000,\"CriticalCount\":3,\"CalculatedMin\":324,\"CalculatedMax\":406,\"AppliedMin\":0,\"AppliedMax\":198},{\"EntityType\":\"Obstacle\",\"Category\":1,\"Source\":1,\"CalculatedSum\":80,\"AppliedSum\":52,\"Count\":1,\"CriticalMultiplierMax\":10000,\"CriticalCount\":0,\"CalculatedMin\":80,\"CalculatedMax\":80,\"AppliedMin\":52,\"AppliedMax\":52},{\"EntityType\":\"Character\",\"Category\":1,\"Source\":3,\"CalculatedSum\":16860,\"AppliedSum\":451,\"Count\":20,\"CriticalMultiplierMax\":15000,\"CriticalCount\":1,\"CalculatedMin\":758,\"CalculatedMax\":946,\"AppliedMin\":0,\"AppliedMax\":92}],\"SkillCount\":{\"PublicSkill01\":2},\"KillLog\":{\"16777220\":213,\"16777222\":431,\"16777224\":495,\"16777226\":841,\"16777230\":1483,\"16777233\":1579}},{\"ServerId\":" + userInfo.getEchelonDBs().getJSONArray("MainSlotServerIds").getString(3) + ",\"BattleEntityId\":{\"uniqueId\":16777219},\"HeroId\":13003,\"Grade\":2,\"Level\":1,\"ExSkillLevel\":1,\"PublicSkillLevel\":1,\"PassiveSkillLevel\":1,\"ExtraPassiveSkillLevel\":-1,\"StatSnapshotCollection\":[{\"Stat\":1,\"Start\":2620,\"End\":2620},{\"Stat\":2,\"Start\":389,\"End\":389},{\"Stat\":3,\"Start\":21,\"End\":21},{\"Stat\":4,\"Start\":1619,\"End\":1619},{\"Stat\":5,\"Start\":897,\"End\":897},{\"Stat\":7,\"Start\":199,\"End\":199},{\"Stat\":9,\"Start\":199,\"End\":199},{\"Stat\":12,\"Start\":20000,\"End\":22800},{\"Stat\":30,\"Start\":10000,\"End\":10000},{\"Stat\":34,\"Start\":10000,\"End\":10000},{\"Stat\":42,\"Start\":700,\"End\":700},{\"Stat\":44,\"Start\":0,\"End\":0}],\"HPRateBefore\":10000,\"HPRateAfter\":10000,\"CrowdControlCount\":0,\"CrowdControlDuration\":0,\"EvadeCount\":0,\"DamageImmuneCount\":0,\"CrowdControlImmuneCount\":0,\"DeadFrame\":-1,\"TacticEntityType\":1,\"GivenNumericLogs\":[{\"EntityType\":\"Character\",\"Category\":1,\"Source\":1,\"CalculatedSum\":1829,\"AppliedSum\":849,\"Count\":10,\"CriticalMultiplierMax\":23806,\"CriticalCount\":1,\"CalculatedMin\":170,\"CalculatedMax\":194,\"AppliedMin\":0,\"AppliedMax\":183},{\"EntityType\":\"Obstacle\",\"Category\":1,\"Source\":1,\"CalculatedSum\":193,\"AppliedSum\":386,\"Count\":1,\"CriticalMultiplierMax\":10000,\"CriticalCount\":0,\"CalculatedMin\":193,\"CalculatedMax\":193,\"AppliedMin\":386,\"AppliedMax\":386},{\"EntityType\":\"Character\",\"Category\":1,\"Source\":2,\"CalculatedSum\":1001,\"AppliedSum\":165,\"Count\":1,\"CriticalMultiplierMax\":10000,\"CriticalCount\":0,\"CalculatedMin\":1001,\"CalculatedMax\":1001,\"AppliedMin\":165,\"AppliedMax\":165}],\"SkillCount\":{\"PublicSkill01\":1,\"ExSkill01\":1},\"KillLog\":{\"16777229\":1145,\"16777234\":2134}}],\"Supporters\":[{\"ServerId\":" + userInfo.getEchelonDBs().getJSONArray("SupportSlotServerIds").getString(0) + ",\"BattleEntityId\":{\"uniqueId\":1073741825},\"HeroId\":26000,\"Grade\":1,\"Level\":1,\"ExSkillLevel\":1,\"PublicSkillLevel\":1,\"PassiveSkillLevel\":-1,\"ExtraPassiveSkillLevel\":-1,\"StatSnapshotCollection\":[{\"Stat\":1,\"Start\":2515,\"End\":2515},{\"Stat\":2,\"Start\":122,\"End\":122},{\"Stat\":3,\"Start\":25,\"End\":25},{\"Stat\":4,\"Start\":2399,\"End\":2399},{\"Stat\":5,\"Start\":99,\"End\":99},{\"Stat\":7,\"Start\":1093,\"End\":1093},{\"Stat\":9,\"Start\":198,\"End\":198},{\"Stat\":12,\"Start\":20000,\"End\":20000},{\"Stat\":30,\"Start\":10000,\"End\":10000},{\"Stat\":34,\"Start\":10000,\"End\":10000},{\"Stat\":42,\"Start\":700,\"End\":700},{\"Stat\":44,\"Start\":0,\"End\":0}],\"HPRateBefore\":0,\"HPRateAfter\":0,\"CrowdControlCount\":0,\"CrowdControlDuration\":0,\"EvadeCount\":0,\"DamageImmuneCount\":0,\"CrowdControlImmuneCount\":0,\"DeadFrame\":-1,\"TacticEntityType\":1,\"SkillCount\":{\"PublicSkill01\":1}}],\"UseAutoSkill\":false,\"TssUseCount\":0,\"SkillCostSummary\":{\"InitialCost\":9.127959,\"CostPerFrameSnapshots\":[{\"Frame\":61,\"Regen\":0.009333333}],\"CostAddSnapshots\":[],\"CostUseSnapshots\":[{\"Frame\":2062,\"Used\":5.0,\"CharId\":13003,\"Level\":1},{\"Frame\":2124,\"Used\":3.0,\"CharId\":13010,\"Level\":1}]}},\"Group02Summary\":{\"TeamId\":10009,\"LeaderEntityId\":{\"uniqueId\":0},\"Heroes\":[{\"ServerId\":0,\"BattleEntityId\":{\"uniqueId\":16777220},\"HeroId\":7000001,\"DeadFrame\":213,\"TacticEntityType\":2},{\"ServerId\":0,\"BattleEntityId\":{\"uniqueId\":16777221},\"HeroId\":7000001,\"DeadFrame\":266,\"TacticEntityType\":2},{\"ServerId\":0,\"BattleEntityId\":{\"uniqueId\":16777222},\"HeroId\":7000011,\"DeadFrame\":431,\"TacticEntityType\":2},{\"ServerId\":0,\"BattleEntityId\":{\"uniqueId\":16777223},\"HeroId\":7000011,\"DeadFrame\":434,\"TacticEntityType\":2},{\"ServerId\":0,\"BattleEntityId\":{\"uniqueId\":16777224},\"HeroId\":7000011,\"DeadFrame\":495,\"TacticEntityType\":2},{\"ServerId\":0,\"BattleEntityId\":{\"uniqueId\":16777225},\"HeroId\":7000001,\"DeadFrame\":764,\"TacticEntityType\":2},{\"ServerId\":0,\"BattleEntityId\":{\"uniqueId\":16777226},\"HeroId\":7000001,\"DeadFrame\":841,\"TacticEntityType\":2},{\"ServerId\":0,\"BattleEntityId\":{\"uniqueId\":16777227},\"HeroId\":7000011,\"DeadFrame\":973,\"TacticEntityType\":2},{\"ServerId\":0,\"BattleEntityId\":{\"uniqueId\":16777228},\"HeroId\":7000011,\"DeadFrame\":1042,\"TacticEntityType\":2},{\"ServerId\":0,\"BattleEntityId\":{\"uniqueId\":16777229},\"HeroId\":7000011,\"DeadFrame\":1145,\"TacticEntityType\":2},{\"ServerId\":0,\"BattleEntityId\":{\"uniqueId\":16777230},\"HeroId\":7000001,\"DeadFrame\":1483,\"TacticEntityType\":2},{\"ServerId\":0,\"BattleEntityId\":{\"uniqueId\":16777231},\"HeroId\":7000001,\"DeadFrame\":1528,\"TacticEntityType\":2},{\"ServerId\":0,\"BattleEntityId\":{\"uniqueId\":16777232},\"HeroId\":7000011,\"DeadFrame\":1690,\"TacticEntityType\":2},{\"ServerId\":0,\"BattleEntityId\":{\"uniqueId\":16777233},\"HeroId\":7000011,\"DeadFrame\":1579,\"TacticEntityType\":2},{\"ServerId\":0,\"BattleEntityId\":{\"uniqueId\":16777234},\"HeroId\":7000013,\"Grade\":1,\"Level\":1,\"ExSkillLevel\":-1,\"PublicSkillLevel\":-1,\"PassiveSkillLevel\":-1,\"ExtraPassiveSkillLevel\":-1,\"StatSnapshotCollection\":[{\"Stat\":1,\"Start\":4050,\"End\":4050},{\"Stat\":2,\"Start\":211,\"End\":211},{\"Stat\":3,\"Start\":80,\"End\":80},{\"Stat\":4,\"Start\":1400,\"End\":1400},{\"Stat\":5,\"Start\":100,\"End\":100},{\"Stat\":7,\"Start\":100,\"End\":100},{\"Stat\":9,\"Start\":250,\"End\":250},{\"Stat\":12,\"Start\":20000,\"End\":20000},{\"Stat\":30,\"Start\":10000,\"End\":10000},{\"Stat\":34,\"Start\":10000,\"End\":10000},{\"Stat\":42,\"Start\":0,\"End\":0},{\"Stat\":44,\"Start\":0,\"End\":0}],\"HPRateBefore\":10000,\"HPRateAfter\":0,\"CrowdControlCount\":0,\"CrowdControlDuration\":0,\"EvadeCount\":0,\"DamageImmuneCount\":0,\"CrowdControlImmuneCount\":0,\"DeadFrame\":2134,\"TacticEntityType\":8,\"GivenNumericLogs\":[{\"EntityType\":\"Character\",\"Category\":1,\"Source\":1,\"CalculatedSum\":3165,\"AppliedSum\":655,\"Count\":15,\"CriticalMultiplierMax\":15000,\"CriticalCount\":2,\"CalculatedMin\":211,\"CalculatedMax\":211,\"AppliedMin\":41,\"AppliedMax\":61}],\"TakenNumericLogs\":[{\"EntityType\":\"Character\",\"Category\":1,\"Source\":1,\"CalculatedSum\":8764,\"AppliedSum\":3231,\"Count\":30,\"CriticalMultiplierMax\":15000,\"CriticalCount\":2,\"CalculatedMin\":179,\"CalculatedMax\":404,\"AppliedMin\":65,\"AppliedMax\":183},{\"EntityType\":\"Character\",\"Category\":1,\"Source\":3,\"CalculatedSum\":6224,\"AppliedSum\":654,\"Count\":9,\"CriticalMultiplierMax\":10000,\"CriticalCount\":0,\"CalculatedMin\":622,\"CalculatedMax\":776,\"AppliedMin\":65,\"AppliedMax\":82},{\"EntityType\":\"Character\",\"Category\":1,\"Source\":2,\"CalculatedSum\":1001,\"AppliedSum\":165,\"Count\":1,\"CriticalMultiplierMax\":10000,\"CriticalCount\":0,\"CalculatedMin\":1001,\"CalculatedMax\":1001,\"AppliedMin\":165,\"AppliedMax\":165}]}],\"UseAutoSkill\":false,\"TssUseCount\":0,\"SkillCostSummary\":{\"InitialCost\":0.0,\"CostPerFrameSnapshots\":[],\"CostAddSnapshots\":[],\"CostUseSnapshots\":[]}},\"ElapsedRealtime\":59.25093},\"Hand\":{\"Cost\":2.671988,\"SkillCardsInHand\":[{\"CharacterId\":16003,\"HandIndex\":0,\"SkillId\":-1520051293,\"RemainCoolTime\":0},{\"CharacterId\":13003,\"HandIndex\":1,\"SkillId\":-12458155,\"RemainCoolTime\":0},{\"CharacterId\":26000,\"HandIndex\":2,\"SkillId\":1922876793,\"RemainCoolTime\":0},{\"CharacterId\":13010,\"HandIndex\":3,\"SkillId\":945851505,\"RemainCoolTime\":0}]},\"SkipSummary\":null,\"ClientUpTime\":72,\"Resendable\":true,\"Hash\":25804163514406,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        try {
            jsonObject = JSONObject.parseObject(result);
        } catch (Exception e) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=?";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
        if (result.contains("packet")) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='获取战斗结果' where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

        } else {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
    }

    public void Account_SetTutorial4(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());

        String packet = "{\"Protocol\":1006,\"TutorialIds\":[1,2,3,4],\"ClientUpTime\":1,\"Resendable\":true,\"Hash\":4320737099815,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        try {
            jsonObject = JSONObject.parseObject(result);
        } catch (Exception e) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=?";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
        if (result.contains("packet")) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='跳过引导' where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

        } else {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
    }

    public void Account_SetTutorial5(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());

        String packet = "{\"Protocol\":1006,\"TutorialIds\":[1,2,3,4,5],\"ClientUpTime\":174,\"Resendable\":true,\"Hash\":4320737099825,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        try {
            jsonObject = JSONObject.parseObject(result);
        } catch (Exception e) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=?";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
        if (result.contains("packet")) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='eventlist' where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

        } else {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
    }

    public void Account_SetTutorial6(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());

        String packet = "{\"Protocol\":1006,\"TutorialIds\":[1,2,3,4,5,22],\"ClientUpTime\":14,\"Resendable\":true,\"Hash\":4320737099832,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        try {
            jsonObject = JSONObject.parseObject(result);
        } catch (Exception e) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=?";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
        if (result.contains("packet")) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='eventlist' where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

        } else {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
    }

    public void transcode_request(UserInfo userInfo) {
        String result;
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("accessToken", userInfo.getAccessToken()));
        result = HttpClientPool.sendPost(userInfo, "https://ba-jp-sdk.bluearchive.jp/user/transcode_request", params);
        JSONObject jsonObject = null;
        try {
            jsonObject = JSONObject.parseObject(result);
        } catch (Exception e) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=?";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
        if (result.contains("\"result\":0")) {
            userInfo.setTranscode(jsonObject.getString("transcode"));
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='生成新的引继码' where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

        } else {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
    }

    public void Scenario_Skip9(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());

        String packet = "{\"Protocol\":19003,\"ScriptGroupId\":700,\"SkipPointScriptCount\":6,\"ClientUpTime\":53,\"Resendable\":true,\"Hash\":81617263525955,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        try {
            jsonObject = JSONObject.parseObject(result);
        } catch (Exception e) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=?";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
        if (result.contains("packet")) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='跳过任务页面动画' where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

        } else {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
    }

    public void missionMultiplereward(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());

        String packet = "{\"Protocol\":8002,\"MissionCategory\":5,\"GuideMissionSeasonId\":null,\"EventContentId\":null,\"ClientUpTime\":118,\"Resendable\":true,\"Hash\":34368328302661,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        try {
            jsonObject = JSONObject.parseObject(result);
        } catch (Exception e) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=?";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
        if (result.contains("packet")) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='领取任务奖励' where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

        } else {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
    }

    public void mailList(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());
        String packet = "{\"Protocol\":7000,\"IsReadMail\":false,\"PivotTime\":\"2023-03-09T00:52:19.6397241\",\"PivotIndex\":-1,\"IsDescending\":true,\"ClientUpTime\":16,\"Resendable\":true,\"Hash\":30064771072076,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        try {
            jsonObject = JSONObject.parseObject(result);
        } catch (Exception e) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=?";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
        if (result.contains("packet")) {
            JSONObject js = JSONObject.parseObject(jsonObject.getString("packet"));
            for (int i = 0; i < js.getJSONArray("MailDBs").size(); i++) {
                if (js.getJSONArray("MailDBs").getJSONObject(i).getJSONArray("ParcelInfos").getJSONObject(0).getJSONObject("Key").getString("Id").equals("3") || js.getJSONArray("MailDBs").getJSONObject(i).getJSONArray("ParcelInfos").getJSONObject(0).getJSONObject("Key").getString("Id").equals("6999") || js.getJSONArray("MailDBs").getJSONObject(i).getJSONArray("ParcelInfos").getJSONObject(0).getJSONObject("Key").getString("Id").equals("1")) {
                    userInfo.getMail().add(js.getJSONArray("MailDBs").getJSONObject(i).getLong("ServerId"));
                }
            }
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='获取邮件列表' where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

        } else {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
    }

    public void mailReceive(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());
        String packet = "{\"Protocol\":7002,\"MailServerIds\":" + userInfo.getMail().toJSONString() + ",\"ClientUpTime\":253,\"Resendable\":true,\"Hash\":30073361006672,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        try {
            jsonObject = JSONObject.parseObject(result);
        } catch (Exception e) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=?";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
        if (result.contains("packet")) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='领取邮件奖励' where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

        } else {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, result);
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
        }
    }

}
