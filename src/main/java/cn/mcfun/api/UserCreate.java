package cn.mcfun.api;

import cn.mcfun.Main;
import cn.mcfun.entity.UserInfo;
import cn.mcfun.utils.Gzip;
import cn.mcfun.utils.HttpClientPool;
import cn.mcfun.utils.Md5;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static cn.mcfun.utils.Hikari.getConnection;

public class UserCreate {
    ContentType strContent = ContentType.create("text/plain", Charset.forName("UTF-8"));
    public void usercreate1(UserInfo userInfo) {
        userInfo.setDeviceId(UUID.randomUUID().toString().toUpperCase());
        String result;
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("channelId", "appstore"));
        params.add(new BasicNameValuePair("deviceId", userInfo.getDeviceId()));
        result = HttpClientPool.sendPost(userInfo, "https://ba-jp-sdk.bluearchive.jp/user/create", params);
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
    public void usercreate2(UserInfo userInfo) {
        String result;
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("deviceId", userInfo.getDeviceId()));
        params.add(new BasicNameValuePair("uid", userInfo.getUid()));
        params.add(new BasicNameValuePair("token", userInfo.getToken()));
        params.add(new BasicNameValuePair("platform", "ios"));
        result = HttpClientPool.sendPost(userInfo, "https://ba-jp-sdk.bluearchive.jp/user/login", params);
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
            userInfo.setAccessToken(jsonObject.getString("accessToken"));
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='生成accessToken' where `order`=? and status=1";
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
    public void usercreate3(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("protocol", "OZDJK2P2KZJ7GLQLOCAGCL6WXA", strContent);
        builder.addTextBody("encode", "True", strContent);
        String packet = "{\"Protocol\":50000,\"YostarUID\":"+userInfo.getUid()+",\"YostarToken\":\""+userInfo.getAccessToken()+"\",\"MakeStandby\":false,\"PassCheck\":false,\"PassCheckYostar\":false,\"WaitingTicket\":\"\",\"ClientVersion\":\""+ Main.ClientVersion+"\",\"ClientUpTime\":0,\"Resendable\":true,\"Hash\":0,\"SessionKey\":null,\"AccountId\":0}";
        builder.addTextBody("packet", Gzip.enCrypt(packet), strContent);
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-gateway.bluearchiveyostar.com:5100/api/queuing/getticket", builder);
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
    public void usercreate4(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("protocol", "PFJRUKK5IXXQR2XFFCMNXEIKXM", strContent);
        builder.addTextBody("encode", "True", strContent);
        String packet = "{\"Protocol\":1009,\"UID\":0,\"YostarToken\":null,\"EnterTicket\":\""+userInfo.getEnterTicket()+"\",\"PassCookieResult\":false,\"Cookie\":\"2164046E9B_"+ Md5.getMd5(userInfo.getUid()) +"_"+Md5.getMd5(userInfo.getEnterTicket())+"\",\"ClientUpTime\":0,\"Resendable\":false,\"Hash\":4333622001665,\"SessionKey\":null,\"AccountId\":0}";
        builder.addTextBody("packet", Gzip.enCrypt(packet), strContent);
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/account/checkyostar", builder);
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
    public void usercreate5(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("protocol", "AHTQLQI65FWJGGGK4KL5AEF57A", strContent);
        builder.addTextBody("encode", "True", strContent);
        String packet = "{\"Protocol\":1002,\"Version\":0,\"DevId\":null,\"IMEI\":0,\"AccessIP\":\"127.0.0.1\",\"MarketId\":\"aas\",\"UserType\":null,\"AdvertisementId\":null,\"OSType\":\"I\",\"OSVersion\":\"iPadOS 16.2\",\"DeviceUniqueId\":\""+userInfo.getDeviceId()+"\",\"DeviceModel\":\"iPad11,1\",\"DeviceSystemMemorySize\":2923,\"ClientUpTime\":0,\"Resendable\":true,\"Hash\":4303557230594,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":0}";
        builder.addTextBody("packet", Gzip.enCrypt(packet), strContent);
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/account/auth", builder);
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
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='获取uuid' where `order`=? and status=1";
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
    public void usercreate6(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("protocol", "IEBSUG64USQI3S2TVYQ57QUYQ4", strContent);
        builder.addTextBody("encode", "True", strContent);
        String packet = "{\"Protocol\":1000,\"DevId\":null,\"Version\":0,\"IMEI\":0,\"AccessIP\":null,\"MarketId\":null,\"UserType\":null,\"AdvertisementId\":null,\"OSType\":null,\"OSVersion\":null,\"ClientUpTime\":0,\"Resendable\":true,\"Hash\":4294967296003,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":0}";
        builder.addTextBody("packet", Gzip.enCrypt(packet), strContent);
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/account/create", builder);
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
    public void usercreate7(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("protocol", "GVDSNCHUMIOS44NDTZM6ZPDDVQ", strContent);
        builder.addTextBody("encode", "True", strContent);
        String packet = "{\"Protocol\":1001,\"Nickname\":\"アカウン\",\"ClientUpTime\":0,\"Resendable\":true,\"Hash\":4299262263299,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        builder.addTextBody("packet", Gzip.enCrypt(packet), strContent);
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/account/nickname", builder);
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
    public void usercreate8(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("protocol", "K3SF6XS7VK7JFL7T2M2WFELUSY", strContent);
        builder.addTextBody("encode", "True", strContent);
        String packet = "{\"Protocol\":1010,\"CallName\":\"アカウン\",\"ClientUpTime\":0,\"Resendable\":true,\"Hash\":4337916968965,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        builder.addTextBody("packet", Gzip.enCrypt(packet), strContent);
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/account/callname", builder);
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
    public void usercreate9(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("protocol", "AHTQLQI65FWJGGGK4KL5AEF57A", strContent);
        builder.addTextBody("encode", "True", strContent);
        String packet = "{\"Protocol\":1002,\"Version\":0,\"DevId\":null,\"IMEI\":0,\"AccessIP\":\"127.0.0.1\",\"MarketId\":\"aas\",\"UserType\":null,\"AdvertisementId\":null,\"OSType\":\"I\",\"OSVersion\":\"iPadOS 16.2\",\"DeviceUniqueId\":\""+userInfo.getDeviceId()+"\",\"DeviceModel\":\"iPad11,1\",\"DeviceSystemMemorySize\":2923,\"ClientUpTime\":0,\"Resendable\":true,\"Hash\":4303557230598,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        builder.addTextBody("packet", Gzip.enCrypt(packet), strContent);
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/account/auth", builder);
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
            userInfo.setSessionKey(js.getString("SessionKey"));
            if(!js.getJSONArray("AttendanceBookRewards").isEmpty()){
                for(int i=0;i<js.getJSONArray("AttendanceBookRewards").size();i++){
                    userInfo.getAttendanceBookRewards().add(js.getJSONArray("AttendanceBookRewards").getJSONObject(i).getString("UniqueId"));
                }
            }
            if(js.containsKey("AttendanceHistoryDBs")){
                for(int i=0;i<js.getJSONArray("AttendanceHistoryDBs").size();i++){
                    userInfo.getAttendanceHistoryDBs().add(js.getJSONArray("AttendanceHistoryDBs").getJSONObject(i).getString("AttendanceBookUniqueId")+"-"+js.getJSONArray("AttendanceHistoryDBs").getJSONObject(i).getJSONObject("AttendedDay").size());
                }
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
    public void usercreate10(UserInfo userInfo) {
        String result;
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("protocol", "LVOYXX5UG6A2L77RVYWDHWUVPA"));
        params.add(new BasicNameValuePair("encode", "True"));
        String packet = "{\"Protocol\":37000,\"ClientUpTime\":0,\"Resendable\":true,\"Hash\":158913789952007,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        params.add(new BasicNameValuePair("packet", Gzip.enCrypt(packet)));
        result = HttpClientPool.sendPost(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/prooftoken/requestquestion", params);
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
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='获取requestquestion' where `order`=? and status=1";
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
    public void usercreate11(UserInfo userInfo) {
        String result;
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("protocol", "KKVAEXLS5QIIB36RQW27XRTJGI"));
        params.add(new BasicNameValuePair("encode", "True"));
        String packet = "{\"Protocol\":24000,\"ClientUpTime\":0,\"Resendable\":true,\"Hash\":103079215104008,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        params.add(new BasicNameValuePair("packet", Gzip.enCrypt(packet)));
        result = HttpClientPool.sendPost(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/academy/getinfo", params);
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
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='获取getinfo' where `order`=? and status=1";
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
    public void usercreate12(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("protocol", "P3OMQZD2FXV7XXEGA7ETCXIJ4Q", strContent);
        builder.addTextBody("encode", "True", strContent);
        String packet = "{\"Protocol\":1017,\"SyncProtocols\":[20000,1003,2000,3000,4000,5000,12000,6000,22001,17018,21000,28001,33000,19000,10006,39006,44000,29002,30041],\"ClientUpTime\":0,\"Resendable\":true,\"Hash\":4367981740041,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        builder.addTextBody("packet", Gzip.enCrypt(packet), strContent);
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/account/loginsync", builder);
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
            for(int i=0;i<userInfo.getCharacterDBs().size();i++){
                if(userInfo.getCharacterDBs().getJSONObject(i).getString("StarGrade").equals("3")){
                    userInfo.setStarNum(userInfo.getStarNum()+1);
                }
            }
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
    public void usercreate14(UserInfo userInfo) {
        String result;
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("protocol", "QIRA2IPFGIJ5T56RHONAUNGUJA"));
        params.add(new BasicNameValuePair("encode", "True"));
        String packet = "{\"Protocol\":3,\"ClientUpTime\":1,\"Resendable\":true,\"Hash\":12884901898,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        params.add(new BasicNameValuePair("packet", Gzip.enCrypt(packet)));
        result = HttpClientPool.sendPost(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/networktime/sync", params);
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
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='获取networktime' where `order`=? and status=1";
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
    public void usercreate15(UserInfo userInfo) {
        String result;
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("protocol", "A5HZKUZQNB2GVPTZL5ATLZLLQ4"));
        params.add(new BasicNameValuePair("encode", "True"));
        String packet = "{\"Protocol\":26000,\"ClientUpTime\":0,\"Resendable\":true,\"Hash\":111669149696011,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        params.add(new BasicNameValuePair("packet", Gzip.enCrypt(packet)));
        result = HttpClientPool.sendPost(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/contentsave/get", params);
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
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='获取contentsave' where `order`=? and status=1";
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
    public void usercreate16(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("protocol", "V6AG4L2DS7ANFAUVHAKRFELRAQ", strContent);
        builder.addTextBody("encode", "True", strContent);
        String packet = "{\"Answer\":8641980201137421426,\"Protocol\":37001,\"ClientUpTime\":0,\"Resendable\":true,\"Hash\":158918084919308,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        builder.addTextBody("packet", Gzip.enCrypt(packet), strContent);
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/prooftoken/submit", builder);
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
            userInfo.setSessionKey(js.getString("SessionKey"));
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='设置SessionKey' where `order`=? and status=1";
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
    public void usercreate17(UserInfo userInfo) {
        String result;
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("protocol", "TE4UJH3A6CSOJSPVNXXIBGYIIA"));
        params.add(new BasicNameValuePair("encode", "True"));
        String packet = "{\"Protocol\":1005,\"ClientUpTime\":0,\"Resendable\":true,\"Hash\":4316442132493,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        params.add(new BasicNameValuePair("packet", Gzip.enCrypt(packet)));
        result = HttpClientPool.sendPost(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/account/gettutorial", params);
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
    public void usercreate18(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("protocol", "5S4LD3Y34GQ2GPKJ75FIIFQWCQ", strContent);
        builder.addTextBody("encode", "True", strContent);
        String packet = "{\"Protocol\":8000,\"EventContentId\":null,\"ClientUpTime\":0,\"Resendable\":true,\"Hash\":34359738368014,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        builder.addTextBody("packet", Gzip.enCrypt(packet), strContent);
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/mission/list", builder);
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
    public void usercreate19(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("protocol", "5S4LD3Y34GQ2GPKJ75FIIFQWCQ", strContent);
        builder.addTextBody("encode", "True", strContent);
        String packet = "{\"Protocol\":8000,\"EventContentId\":822,\"ClientUpTime\":0,\"Resendable\":true,\"Hash\":34359738368015,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        builder.addTextBody("packet", Gzip.enCrypt(packet), strContent);
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/mission/list", builder);
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
    public void usercreate20(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("protocol", "63AWLKA74BAHU6K3JNGSV7X65M", strContent);
        builder.addTextBody("encode", "True", strContent);
        String packet = "{\"Protocol\":19003,\"ScriptGroupId\":11000,\"SkipPointScriptCount\":36,\"ClientUpTime\":124,\"Resendable\":true,\"Hash\":81617263525905,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        builder.addTextBody("packet", Gzip.enCrypt(packet), strContent);
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/scenario/skip", builder);
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
    public void usercreate21(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("protocol", "3KUKWHN3VDLAXVS4YDYBTTVZ2Y", strContent);
        builder.addTextBody("encode", "True", strContent);
        String packet = "{\"Protocol\":1006,\"TutorialIds\":[1],\"ClientUpTime\":67,\"Resendable\":true,\"Hash\":4320737099794,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        builder.addTextBody("packet", Gzip.enCrypt(packet), strContent);
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/account/settutorial", builder);
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
    public void usercreate22(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("protocol", "63AWLKA74BAHU6K3JNGSV7X65M", strContent);
        builder.addTextBody("encode", "True", strContent);
        String packet = "{\"Protocol\":19003,\"ScriptGroupId\":11001,\"SkipPointScriptCount\":3,\"ClientUpTime\":17,\"Resendable\":true,\"Hash\":81617263525907,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        builder.addTextBody("packet", Gzip.enCrypt(packet), strContent);
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/scenario/skip", builder);
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
    public void usercreate23(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("protocol", "63AWLKA74BAHU6K3JNGSV7X65M", strContent);
        builder.addTextBody("encode", "True", strContent);
        String packet = "{\"Protocol\":19003,\"ScriptGroupId\":11002,\"SkipPointScriptCount\":4,\"ClientUpTime\":6,\"Resendable\":true,\"Hash\":81617263525908,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        builder.addTextBody("packet", Gzip.enCrypt(packet), strContent);
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/scenario/skip", builder);
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
    public void usercreate24(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("protocol", "63AWLKA74BAHU6K3JNGSV7X65M", strContent);
        builder.addTextBody("encode", "True", strContent);
        String packet = "{\"Protocol\":19003,\"ScriptGroupId\":11007,\"SkipPointScriptCount\":6,\"ClientUpTime\":57,\"Resendable\":true,\"Hash\":81617263525909,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        builder.addTextBody("packet", Gzip.enCrypt(packet), strContent);
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/scenario/skip", builder);
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
    public void usercreate25(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("protocol", "63AWLKA74BAHU6K3JNGSV7X65M", strContent);
        builder.addTextBody("encode", "True", strContent);
        String packet = "{\"Protocol\":19003,\"ScriptGroupId\":11003,\"SkipPointScriptCount\":2,\"ClientUpTime\":23,\"Resendable\":true,\"Hash\":81617263525910,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        builder.addTextBody("packet", Gzip.enCrypt(packet), strContent);
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/scenario/skip", builder);
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
    public void usercreate26(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("protocol", "63AWLKA74BAHU6K3JNGSV7X65M", strContent);
        builder.addTextBody("encode", "True", strContent);
        String packet = "{\"Protocol\":19003,\"ScriptGroupId\":11004,\"SkipPointScriptCount\":2,\"ClientUpTime\":10,\"Resendable\":true,\"Hash\":81617263525911,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        builder.addTextBody("packet", Gzip.enCrypt(packet), strContent);
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/scenario/skip", builder);
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
    public void usercreate27(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("protocol", "3KUKWHN3VDLAXVS4YDYBTTVZ2Y", strContent);
        builder.addTextBody("encode", "True", strContent);
        String packet = "{\"Protocol\":1006,\"TutorialIds\":[1,2],\"ClientUpTime\":39,\"Resendable\":true,\"Hash\":4320737099800,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        builder.addTextBody("packet", Gzip.enCrypt(packet), strContent);
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/account/settutorial", builder);
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
    public void usercreate28(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("protocol", "63AWLKA74BAHU6K3JNGSV7X65M", strContent);
        builder.addTextBody("encode", "True", strContent);
        String packet = "{\"Protocol\":19003,\"ScriptGroupId\":11005,\"SkipPointScriptCount\":2,\"ClientUpTime\":7,\"Resendable\":true,\"Hash\":81617263525913,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        builder.addTextBody("packet", Gzip.enCrypt(packet), strContent);
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/scenario/skip", builder);
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
    public void usercreate29(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("protocol", "63AWLKA74BAHU6K3JNGSV7X65M", strContent);
        builder.addTextBody("encode", "True", strContent);
        String packet = "{\"Protocol\":19003,\"ScriptGroupId\":11008,\"SkipPointScriptCount\":2,\"ClientUpTime\":6,\"Resendable\":true,\"Hash\":81617263525914,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        builder.addTextBody("packet", Gzip.enCrypt(packet), strContent);
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/scenario/skip", builder);
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
    public void usercreate30(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("protocol", "4QJN3NKE73EJOHZJLD3OLU2XNU", strContent);
        builder.addTextBody("encode", "True", strContent);
        String packet = "{\"Protocol\":15002,\"ConquestEventIds\":[822],\"WorldRaidSeasonAndGroupIds\":{\"821\":821602},\"ClientUpTime\":1656,\"Resendable\":true,\"Hash\":64433099374606,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        builder.addTextBody("packet", Gzip.enCrypt(packet), strContent);
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/opencondition/eventlist", builder);
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
    public void usercreate31(UserInfo userInfo) {
        String result;
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("protocol", "RMS5YFA2GTTTWMYK2IHUP2QSYE"));
        params.add(new BasicNameValuePair("encode", "True"));
        String packet = "{\"Protocol\":36001,\"ClientUpTime\":0,\"Resendable\":true,\"Hash\":154623117623311,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        params.add(new BasicNameValuePair("packet", Gzip.enCrypt(packet)));
        result = HttpClientPool.sendPost(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/notification/eventcontentreddotcheck", params);
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
    public void usercreate32(UserInfo userInfo) {
        String result;
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("protocol", "A6XU5ZVL4DWGSYCQFZKU5ILQVY"));
        params.add(new BasicNameValuePair("encode", "True"));
        String packet = "{\"Protocol\":7001,\"ClientUpTime\":1,\"Resendable\":true,\"Hash\":30069066039312,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        params.add(new BasicNameValuePair("packet", Gzip.enCrypt(packet)));
        result = HttpClientPool.sendPost(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/mail/check", params);
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
    public void usercreate33(UserInfo userInfo) {
        String result;
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("protocol", "ME6ZY2KZLN5UN4DF7BT3TKJKHI"));
        params.add(new BasicNameValuePair("encode", "True"));
        String packet = "{\"Protocol\":25003,\"ClientUpTime\":0,\"Resendable\":true,\"Hash\":107387067301905,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        params.add(new BasicNameValuePair("packet", Gzip.enCrypt(packet)));
        result = HttpClientPool.sendPost(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/event/rewardincrease", params);
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
    public void usercreate34(UserInfo userInfo) {
        String result;
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("protocol", "QXX6AAJZW2CU4X57SL7FCM72XA"));
        params.add(new BasicNameValuePair("encode", "True"));
        String packet = "{\"Protocol\":28019,\"ClientUpTime\":0,\"Resendable\":true,\"Hash\":120340688666642,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        params.add(new BasicNameValuePair("packet", Gzip.enCrypt(packet)));
        result = HttpClientPool.sendPost(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/clan/check", params);
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
    public void usercreate35(UserInfo userInfo) {
        String result;
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("protocol", "TAGMSXJIONDD5CVTS52LJXSDNQ"));
        params.add(new BasicNameValuePair("encode", "True"));
        String packet = "{\"Protocol\":43010,\"ClientUpTime\":0,\"Resendable\":true,\"Hash\":184726543400979,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        params.add(new BasicNameValuePair("packet", Gzip.enCrypt(packet)));
        result = HttpClientPool.sendPost(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/friend/check", params);
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
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='好友检查' where `order`=? and status=1";
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
    public void usercreate36(UserInfo userInfo) {
        String result;
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("protocol", "UXIHQC2AXRZCEBTL2NPBMBBI6I"));
        params.add(new BasicNameValuePair("encode", "True"));
        String packet = "{\"Protocol\":29002,\"ClientUpTime\":578,\"Resendable\":true,\"Hash\":124562641518612,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        params.add(new BasicNameValuePair("packet", Gzip.enCrypt(packet)));
        result = HttpClientPool.sendPost(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/billing/purchaselistbyyostar", params);
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
    public void usercreate37(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("protocol", "KDEPBLQNV632RPNTYVZBRI5QHQ", strContent);
        builder.addTextBody("encode", "True", strContent);
        String packet = "{\"Protocol\":10008,\"FreeRecruitId\":0,\"Cost\":{\"ParcelInfos\":[{\"Key\":{\"Type\":2,\"Id\":4},\"Amount\":0,\"Multiplier\":{\"rawValue\":10000},\"Probability\":{\"rawValue\":10000}}],\"Currency\":{\"currencyValue\":{\"Values\":{},\"Tickets\":{},\"Property\":{},\"Gold\":0,\"Gem\":0,\"GemBonus\":0,\"GemPaid\":0,\"ActionPoint\":0,\"ArenaTicket\":0,\"RaidTicket\":0,\"WeekDungeonChaserATicket\":0,\"WeekDungeonChaserBTicket\":0,\"WeekDungeonChaserCTicket\":0,\"WeekDungeonFindGiftTicket\":0,\"WeekDungeonBloodTicket\":0,\"AcademyTicket\":0,\"SchoolDungeonATicket\":0,\"SchoolDungeonBTicket\":0,\"SchoolDungeonCTicket\":0,\"TimeAttackDungeonTicket\":0,\"MasterCoin\":0,\"WorldRaidTicketA\":0,\"WorldRaidTicketB\":0,\"WorldRaidTicketC\":0,\"IsEmpty\":true},\"Gold\":0,\"Gem\":0,\"GemBonus\":0,\"GemPaid\":0,\"ActionPoint\":0,\"ArenaTicket\":0,\"RaidTicket\":0,\"WeekDungeonChaserATicket\":0,\"WeekDungeonChaserBTicket\":0,\"WeekDungeonChaserCTicket\":0,\"WeekDungeonFindGiftTicket\":0,\"WeekDungeonBloodTicket\":0,\"AcademyTicket\":0,\"SchoolDungeonATicket\":0,\"SchoolDungeonBTicket\":0,\"SchoolDungeonCTicket\":0,\"TimeAttackDungeonTicket\":0,\"MasterCoin\":0,\"WorldRaidTicketA\":0,\"WorldRaidTicketB\":0,\"WorldRaidTicketC\":0},\"EquipmentDBs\":[],\"ItemDBs\":[],\"FurnitureDBs\":[],\"ConsumeCondition\":0},\"GoodsId\":1,\"ShopUniqueId\":2,\"ClientUpTime\":9,\"Resendable\":true,\"Hash\":42984032698389,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        builder.addTextBody("packet", Gzip.enCrypt(packet), strContent);
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/shop/buygacha3", builder);
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
            JSONArray GachaResults = js.getJSONArray("GachaResults");
            for(int i=0;i<GachaResults.size();i++){
                if(GachaResults.getJSONObject(i).getJSONObject("Character").getString("StarGrade").equals("3")){
                    userInfo.setStarNum(userInfo.getStarNum()+1);
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
    public void usercreate38(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("protocol", "3KUKWHN3VDLAXVS4YDYBTTVZ2Y", strContent);
        builder.addTextBody("encode", "True", strContent);
        String packet = "{\"Protocol\":1006,\"TutorialIds\":[1,2,3],\"ClientUpTime\":0,\"Resendable\":true,\"Hash\":4320737099798,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        builder.addTextBody("packet", Gzip.enCrypt(packet), strContent);
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/account/settutorial", builder);
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
    public void usercreate39(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("protocol", "3KUKWHN3VDLAXVS4YDYBTTVZ2Y", strContent);
        builder.addTextBody("encode", "True", strContent);
        String packet = "{\"Protocol\":1006,\"TutorialIds\":[1,2,3],\"ClientUpTime\":0,\"Resendable\":true,\"Hash\":4320737099798,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        builder.addTextBody("packet", Gzip.enCrypt(packet), strContent);
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/account/settutorial", builder);
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
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='跳过引导4' where `order`=? and status=1";
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
    public void usercreate40(UserInfo userInfo) {
        String result;
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("protocol", "ZJSFR7GZ6IM2NYYNJKECP42SRI"));
        params.add(new BasicNameValuePair("encode", "True"));
        String packet = "{\"Protocol\":6000,\"ClientUpTime\":385,\"Resendable\":true,\"Hash\":25769803776023,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        params.add(new BasicNameValuePair("packet", Gzip.enCrypt(packet)));
        result = HttpClientPool.sendPost(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/campaign/list", params);
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
    public void usercreate41(UserInfo userInfo) {
        String result;
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("protocol", "ME6ZY2KZLN5UN4DF7BT3TKJKHI"));
        params.add(new BasicNameValuePair("encode", "True"));
        String packet = "{\"Protocol\":25003,\"ClientUpTime\":1,\"Resendable\":true,\"Hash\":107387067301912,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        params.add(new BasicNameValuePair("packet", Gzip.enCrypt(packet)));
        result = HttpClientPool.sendPost(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/event/rewardincrease", params);
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
    public void usercreate42(UserInfo userInfo) {
        String result;
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("protocol", "A6XU5ZVL4DWGSYCQFZKU5ILQVY"));
        params.add(new BasicNameValuePair("encode", "True"));
        String packet = "{\"Protocol\":7001,\"ClientUpTime\":118,\"Resendable\":true,\"Hash\":30069066039321,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        params.add(new BasicNameValuePair("packet", Gzip.enCrypt(packet)));
        result = HttpClientPool.sendPost(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/mail/check", params);
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
    public void usercreate43(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("protocol", "6CSZXRZ3CJRG5UW5JTKVFERYW4", strContent);
        builder.addTextBody("encode", "True", strContent);
        String packet = "{\"Protocol\":6001,\"StageUniqueId\":1011101,\"ClientUpTime\":1,\"Resendable\":true,\"Hash\":25774098743322,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        builder.addTextBody("packet", Gzip.enCrypt(packet), strContent);
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/campaign/entermainstage", builder);
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
    public void usercreate44(UserInfo userInfo) {
        String result;
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("protocol", "ED653MDBZL75ADU4XKIFERNVVI"));
        params.add(new BasicNameValuePair("encode", "True"));
        String packet = "{\"Protocol\":5000,\"ClientUpTime\":146,\"Resendable\":true,\"Hash\":21474836480027,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        params.add(new BasicNameValuePair("packet", Gzip.enCrypt(packet)));
        result = HttpClientPool.sendPost(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/echelon/list", params);
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
    public void usercreate45(UserInfo userInfo) {
        String result;
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("protocol", "3ESJVMBCMCM57PS7JI6PVS6QA4"));
        params.add(new BasicNameValuePair("encode", "True"));
        String packet = "{\"Protocol\":5002,\"ClientUpTime\":1,\"Resendable\":true,\"Hash\":21483426414620,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        params.add(new BasicNameValuePair("packet", Gzip.enCrypt(packet)));
        result = HttpClientPool.sendPost(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/echelon/presetlist", params);
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
    public void usercreate46(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("protocol", "RAZ6TXVA7X4L73PJR2WB2MM46U", strContent);
        builder.addTextBody("encode", "True", strContent);
        String packet = "{\"Protocol\":6003,\"StageUniqueId\":1011101,\"DeployedEchelons\":[{\"EntityId\":1,\"HpInfos\":null,\"DyingInfos\":{},\"BuffInfos\":null,\"ActionCountMax\":0,\"ActionCount\":0,\"Mobility\":0,\"StrategySightRange\":0,\"Id\":-1,\"Rotate\":{\"x\":0.0,\"y\":0.0,\"z\":0.0},\"Location\":{\"x\":-2,\"y\":2,\"z\":0},\"AIDestination\":{\"x\":0,\"y\":0,\"z\":0},\"IsActionComplete\":false,\"IsPlayer\":true,\"IsFixedEchelon\":false,\"MovementOrder\":0,\"MaxTSSCount\":0,\"RemainTSSCount\":0,\"RewardParcelInfosWithDropTacticEntityType\":null,\"SkillCardHand\":null,\"PlayAnimation\":false}],\"ClientUpTime\":170,\"Resendable\":true,\"Hash\":25782688677917,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        builder.addTextBody("packet", Gzip.enCrypt(packet), strContent);
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/campaign/deployechelon", builder);
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
    public void usercreate47(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("protocol", "RL4MMUF2L7GQFJ7HNX2TP5CZJY", strContent);
        builder.addTextBody("encode", "True", strContent);
        String packet = "{\"Protocol\":6015,\"StageUniqueId\":1011101,\"ClientUpTime\":85,\"Resendable\":true,\"Hash\":25834228285470,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        builder.addTextBody("packet", Gzip.enCrypt(packet), strContent);
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/campaign/confirmtutorialstage", builder);
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
    public void usercreate48(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("protocol", "ZTCUIQPH2SEPANUMZRRTEP3SLQ", strContent);
        builder.addTextBody("encode", "True", strContent);
        String packet = "{\"Protocol\":6005,\"StageUniqueId\":1011101,\"EchelonEntityId\":1,\"DestPosition\":{\"x\":-1,\"y\":1,\"z\":0},\"ClientUpTime\":59,\"Resendable\":true,\"Hash\":25791278612511,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        builder.addTextBody("packet", Gzip.enCrypt(packet), strContent);
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/campaign/mapmove", builder);
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
    public void usercreate49(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("protocol", "DUH7ZPYNVFR6CY4P5QPG3N3Y5U", strContent);
        builder.addTextBody("encode", "True", strContent);
        String packet = "{\"Protocol\":6007,\"StageUniqueId\":1011101,\"EchelonIndex\":1,\"EnemyIndex\":10008,\"ClientUpTime\":3,\"Resendable\":true,\"Hash\":25799868547104,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        builder.addTextBody("packet", Gzip.enCrypt(packet), strContent);
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/campaign/entertactic", builder);
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
    public void usercreate50(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("protocol", "6HEBFF6B7SZIYWHU4FAWDOLIQE", strContent);
        builder.addTextBody("encode", "True", strContent);
        String packet = "{\"Protocol\":6008,\"PassCheckCharacter\":false,\"Summary\":{\"HashKey\":0,\"IsBossBattle\":false,\"BattleType\":1,\"StageId\":1011101,\"GroundId\":101110101,\"Winner\":\"Group01\",\"EndType\":4,\"EndFrame\":1038,\"Group01Summary\":{\"TeamId\":1,\"LeaderEntityId\":{\"uniqueId\":16777217},\"Heroes\":[{\"ServerId\":"+userInfo.getEchelonDBs().getJSONArray("MainSlotServerIds").getString(0)+",\"BattleEntityId\":{\"uniqueId\":16777217},\"HeroId\":13010,\"Grade\":2,\"Level\":1,\"ExSkillLevel\":1,\"PublicSkillLevel\":1,\"PassiveSkillLevel\":1,\"ExtraPassiveSkillLevel\":-1,\"StatSnapshotCollection\":[{\"Stat\":1,\"Start\":3398,\"End\":3398},{\"Stat\":2,\"Start\":131,\"End\":131},{\"Stat\":3,\"Start\":30,\"End\":34},{\"Stat\":4,\"Start\":1624,\"End\":1624},{\"Stat\":5,\"Start\":100,\"End\":100},{\"Stat\":7,\"Start\":1440,\"End\":1440},{\"Stat\":9,\"Start\":200,\"End\":200},{\"Stat\":12,\"Start\":20000,\"End\":20000},{\"Stat\":30,\"Start\":10000,\"End\":10000},{\"Stat\":34,\"Start\":10000,\"End\":10000},{\"Stat\":42,\"Start\":700,\"End\":700},{\"Stat\":44,\"Start\":0,\"End\":0}],\"HPRateBefore\":10000,\"HPRateAfter\":7683,\"CrowdControlCount\":0,\"CrowdControlDuration\":0,\"EvadeCount\":73,\"DamageImmuneCount\":0,\"CrowdControlImmuneCount\":0,\"DeadFrame\":-1,\"TacticEntityType\":1,\"GivenNumericLogs\":[{\"EntityType\":\"Character\",\"Category\":1,\"Source\":1,\"CalculatedSum\":2934,\"AppliedSum\":626,\"Count\":13,\"CriticalMultiplierMax\":15000,\"CriticalCount\":2,\"CalculatedMin\":206,\"CalculatedMax\":258,\"AppliedMin\":0,\"AppliedMax\":122},{\"EntityType\":\"Character\",\"Category\":1,\"Source\":3,\"CalculatedSum\":12832,\"AppliedSum\":314,\"Count\":18,\"CriticalMultiplierMax\":15000,\"CriticalCount\":3,\"CalculatedMin\":634,\"CalculatedMax\":774,\"AppliedMin\":0,\"AppliedMax\":80}],\"TakenNumericLogs\":[{\"EntityType\":\"Character\",\"Category\":1,\"Source\":1,\"CalculatedSum\":3651,\"AppliedSum\":788,\"Count\":39,\"CriticalMultiplierMax\":15000,\"CriticalCount\":5,\"CalculatedMin\":42,\"CalculatedMax\":103,\"AppliedMin\":13,\"AppliedMax\":30}],\"SkillCount\":{\"PublicSkill01\":2},\"KillLog\":{\"16777221\":526,\"16777225\":761,\"16777228\":959}},{\"ServerId\":"+userInfo.getEchelonDBs().getJSONArray("MainSlotServerIds").getString(1)+",\"BattleEntityId\":{\"uniqueId\":16777218},\"HeroId\":16003,\"Grade\":1,\"Level\":1,\"ExSkillLevel\":1,\"PublicSkillLevel\":1,\"PassiveSkillLevel\":-1,\"ExtraPassiveSkillLevel\":-1,\"StatSnapshotCollection\":[{\"Stat\":1,\"Start\":2532,\"End\":2532},{\"Stat\":2,\"Start\":204,\"End\":204},{\"Stat\":3,\"Start\":21,\"End\":21},{\"Stat\":4,\"Start\":1499,\"End\":1499},{\"Stat\":5,\"Start\":690,\"End\":690},{\"Stat\":7,\"Start\":788,\"End\":788},{\"Stat\":9,\"Start\":197,\"End\":197},{\"Stat\":12,\"Start\":20000,\"End\":20000},{\"Stat\":30,\"Start\":10000,\"End\":10000},{\"Stat\":34,\"Start\":10000,\"End\":10000},{\"Stat\":42,\"Start\":700,\"End\":700},{\"Stat\":44,\"Start\":0,\"End\":0}],\"HPRateBefore\":10000,\"HPRateAfter\":10000,\"CrowdControlCount\":0,\"CrowdControlDuration\":0,\"EvadeCount\":0,\"DamageImmuneCount\":0,\"CrowdControlImmuneCount\":0,\"DeadFrame\":-1,\"TacticEntityType\":1,\"GivenNumericLogs\":[{\"EntityType\":\"Character\",\"Category\":1,\"Source\":1,\"CalculatedSum\":4088,\"AppliedSum\":1039,\"Count\":11,\"CriticalMultiplierMax\":15000,\"CriticalCount\":2,\"CalculatedMin\":332,\"CalculatedMax\":406,\"AppliedMin\":34,\"AppliedMax\":132},{\"EntityType\":\"Character\",\"Category\":1,\"Source\":3,\"CalculatedSum\":8296,\"AppliedSum\":84,\"Count\":10,\"CriticalMultiplierMax\":15000,\"CriticalCount\":1,\"CalculatedMin\":746,\"CalculatedMax\":918,\"AppliedMin\":0,\"AppliedMax\":76}],\"SkillCount\":{\"PublicSkill01\":1},\"KillLog\":{\"16777220\":221,\"16777223\":285,\"16777224\":761,\"16777229\":817,\"16777226\":1037}},{\"ServerId\":"+userInfo.getEchelonDBs().getJSONArray("MainSlotServerIds").getString(2)+",\"BattleEntityId\":{\"uniqueId\":16777219},\"HeroId\":13003,\"Grade\":2,\"Level\":1,\"ExSkillLevel\":1,\"PublicSkillLevel\":1,\"PassiveSkillLevel\":1,\"ExtraPassiveSkillLevel\":-1,\"StatSnapshotCollection\":[{\"Stat\":1,\"Start\":2620,\"End\":2620},{\"Stat\":2,\"Start\":389,\"End\":389},{\"Stat\":3,\"Start\":21,\"End\":21},{\"Stat\":4,\"Start\":1619,\"End\":1619},{\"Stat\":5,\"Start\":897,\"End\":897},{\"Stat\":7,\"Start\":199,\"End\":199},{\"Stat\":9,\"Start\":199,\"End\":199},{\"Stat\":12,\"Start\":20000,\"End\":28806},{\"Stat\":30,\"Start\":10000,\"End\":10000},{\"Stat\":34,\"Start\":10000,\"End\":10000},{\"Stat\":42,\"Start\":700,\"End\":700},{\"Stat\":44,\"Start\":0,\"End\":0}],\"HPRateBefore\":10000,\"HPRateAfter\":10000,\"CrowdControlCount\":0,\"CrowdControlDuration\":0,\"EvadeCount\":0,\"DamageImmuneCount\":0,\"CrowdControlImmuneCount\":0,\"DeadFrame\":-1,\"TacticEntityType\":1,\"GivenNumericLogs\":[{\"EntityType\":\"Character\",\"Category\":1,\"Source\":1,\"CalculatedSum\":714,\"AppliedSum\":192,\"Count\":4,\"CriticalMultiplierMax\":23806,\"CriticalCount\":1,\"CalculatedMin\":170,\"CalculatedMax\":192,\"AppliedMin\":0,\"AppliedMax\":157}],\"SkillCount\":{\"PublicSkill01\":2},\"KillLog\":{\"16777222\":457,\"16777227\":861}}],\"Supporters\":[{\"ServerId\":"+userInfo.getEchelonDBs().getJSONArray("SupportSlotServerIds").getString(0)+",\"BattleEntityId\":{\"uniqueId\":1073741825},\"HeroId\":26000,\"Grade\":1,\"Level\":1,\"ExSkillLevel\":1,\"PublicSkillLevel\":1,\"PassiveSkillLevel\":-1,\"ExtraPassiveSkillLevel\":-1,\"StatSnapshotCollection\":[{\"Stat\":1,\"Start\":2515,\"End\":2515},{\"Stat\":2,\"Start\":122,\"End\":122},{\"Stat\":3,\"Start\":25,\"End\":25},{\"Stat\":4,\"Start\":2399,\"End\":2399},{\"Stat\":5,\"Start\":99,\"End\":99},{\"Stat\":7,\"Start\":1093,\"End\":1093},{\"Stat\":9,\"Start\":198,\"End\":198},{\"Stat\":12,\"Start\":20000,\"End\":20000},{\"Stat\":30,\"Start\":10000,\"End\":10000},{\"Stat\":34,\"Start\":10000,\"End\":10000},{\"Stat\":42,\"Start\":700,\"End\":700},{\"Stat\":44,\"Start\":0,\"End\":0}],\"HPRateBefore\":0,\"HPRateAfter\":0,\"CrowdControlCount\":0,\"CrowdControlDuration\":0,\"EvadeCount\":0,\"DamageImmuneCount\":0,\"CrowdControlImmuneCount\":0,\"DeadFrame\":-1,\"TacticEntityType\":1}],\"UseAutoSkill\":false,\"TssUseCount\":0,\"SkillCostSummary\":{\"InitialCost\":0.0,\"CostPerFrameSnapshots\":[{\"Frame\":61,\"Regen\":0.009333333}],\"CostAddSnapshots\":[],\"CostUseSnapshots\":[]}},\"Group02Summary\":{\"TeamId\":10008,\"LeaderEntityId\":{\"uniqueId\":0},\"Heroes\":[{\"ServerId\":0,\"BattleEntityId\":{\"uniqueId\":16777220},\"HeroId\":7000001,\"DeadFrame\":221,\"TacticEntityType\":2},{\"ServerId\":0,\"BattleEntityId\":{\"uniqueId\":16777221},\"HeroId\":7000011,\"DeadFrame\":526,\"TacticEntityType\":2},{\"ServerId\":0,\"BattleEntityId\":{\"uniqueId\":16777222},\"HeroId\":7000011,\"DeadFrame\":457,\"TacticEntityType\":2},{\"ServerId\":0,\"BattleEntityId\":{\"uniqueId\":16777223},\"HeroId\":7000001,\"DeadFrame\":285,\"TacticEntityType\":2},{\"ServerId\":0,\"BattleEntityId\":{\"uniqueId\":16777224},\"HeroId\":7000001,\"DeadFrame\":761,\"TacticEntityType\":2},{\"ServerId\":0,\"BattleEntityId\":{\"uniqueId\":16777225},\"HeroId\":7000001,\"DeadFrame\":761,\"TacticEntityType\":2},{\"ServerId\":0,\"BattleEntityId\":{\"uniqueId\":16777226},\"HeroId\":7000011,\"DeadFrame\":1037,\"TacticEntityType\":2},{\"ServerId\":0,\"BattleEntityId\":{\"uniqueId\":16777227},\"HeroId\":7000011,\"DeadFrame\":861,\"TacticEntityType\":2},{\"ServerId\":0,\"BattleEntityId\":{\"uniqueId\":16777228},\"HeroId\":7000011,\"DeadFrame\":959,\"TacticEntityType\":2},{\"ServerId\":0,\"BattleEntityId\":{\"uniqueId\":16777229},\"HeroId\":7000001,\"DeadFrame\":817,\"TacticEntityType\":2}],\"UseAutoSkill\":false,\"TssUseCount\":0,\"SkillCostSummary\":{\"InitialCost\":0.0,\"CostPerFrameSnapshots\":[],\"CostAddSnapshots\":[],\"CostUseSnapshots\":[]}},\"ElapsedRealtime\":26.5865326},\"Hand\":{\"Cost\":9.127959,\"SkillCardsInHand\":[{\"CharacterId\":16003,\"HandIndex\":0,\"SkillId\":-1520051293,\"RemainCoolTime\":0},{\"CharacterId\":13010,\"HandIndex\":1,\"SkillId\":945851505,\"RemainCoolTime\":0},{\"CharacterId\":13003,\"HandIndex\":2,\"SkillId\":-12458155,\"RemainCoolTime\":0},{\"CharacterId\":26000,\"HandIndex\":3,\"SkillId\":1922876793,\"RemainCoolTime\":0}]},\"SkipSummary\":null,\"ClientUpTime\":40,\"Resendable\":true,\"Hash\":25804163514401,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        builder.addTextBody("packet", Gzip.enCrypt(packet), strContent);
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/campaign/tacticresult", builder);
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
    public void usercreate51(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("protocol", "K3DJMATAXEEDVJDTQH4UUXQ46Q", strContent);
        builder.addTextBody("encode", "True", strContent);
        String packet = "{\"Protocol\":6006,\"StageUniqueId\":1011101,\"ClientUpTime\":865,\"Resendable\":true,\"Hash\":25795573579810,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        builder.addTextBody("packet", Gzip.enCrypt(packet), strContent);
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/campaign/endturn", builder);
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
    public void usercreate52(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("protocol", "K3DJMATAXEEDVJDTQH4UUXQ46Q", strContent);
        builder.addTextBody("encode", "True", strContent);
        String packet = "{\"Protocol\":6006,\"StageUniqueId\":1011101,\"ClientUpTime\":2,\"Resendable\":true,\"Hash\":25795573579811,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        builder.addTextBody("packet", Gzip.enCrypt(packet), strContent);
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/campaign/endturn", builder);
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
    public void usercreate53(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("protocol", "ZTCUIQPH2SEPANUMZRRTEP3SLQ", strContent);
        builder.addTextBody("encode", "True", strContent);
        String packet = "{\"Protocol\":6005,\"StageUniqueId\":1011101,\"EchelonEntityId\":1,\"DestPosition\":{\"x\":0,\"y\":0,\"z\":0},\"ClientUpTime\":118,\"Resendable\":true,\"Hash\":25791278612516,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        builder.addTextBody("packet", Gzip.enCrypt(packet), strContent);
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/campaign/mapmove", builder);
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
    public void usercreate54(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("protocol", "DUH7ZPYNVFR6CY4P5QPG3N3Y5U", strContent);
        builder.addTextBody("encode", "True", strContent);
        String packet = "{\"Protocol\":6007,\"StageUniqueId\":1011101,\"EchelonIndex\":1,\"EnemyIndex\":10009,\"ClientUpTime\":2,\"Resendable\":true,\"Hash\":25799868547109,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        builder.addTextBody("packet", Gzip.enCrypt(packet), strContent);
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/campaign/entertactic", builder);
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
    public void usercreate55(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("protocol", "6HEBFF6B7SZIYWHU4FAWDOLIQE", strContent);
        builder.addTextBody("encode", "True", strContent);
        String packet = "{\"Protocol\":6008,\"PassCheckCharacter\":false,\"Summary\":{\"HashKey\":0,\"IsBossBattle\":true,\"BattleType\":1,\"StageId\":1011101,\"GroundId\":101110102,\"Winner\":\"Group01\",\"EndType\":4,\"EndFrame\":2135,\"Group01Summary\":{\"TeamId\":1,\"LeaderEntityId\":{\"uniqueId\":16777217},\"Heroes\":[{\"ServerId\":"+userInfo.getEchelonDBs().getJSONArray("MainSlotServerIds").getString(0)+",\"BattleEntityId\":{\"uniqueId\":16777217},\"HeroId\":13010,\"Grade\":2,\"Level\":1,\"ExSkillLevel\":1,\"PublicSkillLevel\":1,\"PassiveSkillLevel\":1,\"ExtraPassiveSkillLevel\":-1,\"StatSnapshotCollection\":[{\"Stat\":1,\"Start\":3398,\"End\":3398},{\"Stat\":2,\"Start\":131,\"End\":131},{\"Stat\":3,\"Start\":30,\"End\":34},{\"Stat\":4,\"Start\":1624,\"End\":1624},{\"Stat\":5,\"Start\":100,\"End\":100},{\"Stat\":7,\"Start\":1440,\"End\":1440},{\"Stat\":9,\"Start\":200,\"End\":200},{\"Stat\":12,\"Start\":20000,\"End\":20000},{\"Stat\":30,\"Start\":10000,\"End\":10000},{\"Stat\":34,\"Start\":10000,\"End\":10000},{\"Stat\":42,\"Start\":700,\"End\":700},{\"Stat\":44,\"Start\":0,\"End\":0}],\"HPRateBefore\":7680,\"HPRateAfter\":1895,\"CrowdControlCount\":0,\"CrowdControlDuration\":0,\"EvadeCount\":163,\"DamageImmuneCount\":0,\"CrowdControlImmuneCount\":0,\"DeadFrame\":-1,\"TacticEntityType\":1,\"GivenNumericLogs\":[{\"EntityType\":\"Character\",\"Category\":1,\"Source\":1,\"CalculatedSum\":7326,\"AppliedSum\":1897,\"Count\":32,\"CriticalMultiplierMax\":15000,\"CriticalCount\":4,\"CalculatedMin\":204,\"CalculatedMax\":258,\"AppliedMin\":0,\"AppliedMax\":122},{\"EntityType\":\"Character\",\"Category\":1,\"Source\":3,\"CalculatedSum\":21718,\"AppliedSum\":942,\"Count\":31,\"CriticalMultiplierMax\":15000,\"CriticalCount\":3,\"CalculatedMin\":622,\"CalculatedMax\":778,\"AppliedMin\":0,\"AppliedMax\":84},{\"EntityType\":\"Obstacle\",\"Category\":1,\"Source\":1,\"CalculatedSum\":59,\"AppliedSum\":38,\"Count\":1,\"CriticalMultiplierMax\":10000,\"CriticalCount\":0,\"CalculatedMin\":59,\"CalculatedMax\":59,\"AppliedMin\":38,\"AppliedMax\":38}],\"TakenNumericLogs\":[{\"EntityType\":\"Character\",\"Category\":1,\"Source\":1,\"CalculatedSum\":9410,\"AppliedSum\":1967,\"Count\":78,\"CriticalMultiplierMax\":15000,\"CriticalCount\":10,\"CalculatedMin\":42,\"CalculatedMax\":211,\"AppliedMin\":13,\"AppliedMax\":61}],\"SkillCount\":{\"PublicSkill01\":4,\"ExSkill01\":1},\"KillLog\":{\"16777221\":266,\"16777223\":434,\"16777225\":764,\"16777227\":973,\"16777228\":1042,\"16777231\":1528,\"16777232\":1690}},{\"ServerId\":"+userInfo.getEchelonDBs().getJSONArray("MainSlotServerIds").getString(1)+",\"BattleEntityId\":{\"uniqueId\":16777218},\"HeroId\":16003,\"Grade\":1,\"Level\":1,\"ExSkillLevel\":1,\"PublicSkillLevel\":1,\"PassiveSkillLevel\":-1,\"ExtraPassiveSkillLevel\":-1,\"StatSnapshotCollection\":[{\"Stat\":1,\"Start\":2532,\"End\":2532},{\"Stat\":2,\"Start\":204,\"End\":204},{\"Stat\":3,\"Start\":21,\"End\":21},{\"Stat\":4,\"Start\":1499,\"End\":1499},{\"Stat\":5,\"Start\":690,\"End\":690},{\"Stat\":7,\"Start\":788,\"End\":788},{\"Stat\":9,\"Start\":197,\"End\":197},{\"Stat\":12,\"Start\":20000,\"End\":20000},{\"Stat\":30,\"Start\":10000,\"End\":10000},{\"Stat\":34,\"Start\":10000,\"End\":10000},{\"Stat\":42,\"Start\":700,\"End\":700},{\"Stat\":44,\"Start\":0,\"End\":0}],\"HPRateBefore\":10000,\"HPRateAfter\":10000,\"CrowdControlCount\":0,\"CrowdControlDuration\":0,\"EvadeCount\":0,\"DamageImmuneCount\":0,\"CrowdControlImmuneCount\":0,\"DeadFrame\":-1,\"TacticEntityType\":1,\"GivenNumericLogs\":[{\"EntityType\":\"Character\",\"Category\":1,\"Source\":1,\"CalculatedSum\":11880,\"AppliedSum\":2766,\"Count\":32,\"CriticalMultiplierMax\":15000,\"CriticalCount\":3,\"CalculatedMin\":324,\"CalculatedMax\":406,\"AppliedMin\":0,\"AppliedMax\":198},{\"EntityType\":\"Obstacle\",\"Category\":1,\"Source\":1,\"CalculatedSum\":80,\"AppliedSum\":52,\"Count\":1,\"CriticalMultiplierMax\":10000,\"CriticalCount\":0,\"CalculatedMin\":80,\"CalculatedMax\":80,\"AppliedMin\":52,\"AppliedMax\":52},{\"EntityType\":\"Character\",\"Category\":1,\"Source\":3,\"CalculatedSum\":16860,\"AppliedSum\":451,\"Count\":20,\"CriticalMultiplierMax\":15000,\"CriticalCount\":1,\"CalculatedMin\":758,\"CalculatedMax\":946,\"AppliedMin\":0,\"AppliedMax\":92}],\"SkillCount\":{\"PublicSkill01\":2},\"KillLog\":{\"16777220\":213,\"16777222\":431,\"16777224\":495,\"16777226\":841,\"16777230\":1483,\"16777233\":1579}},{\"ServerId\":"+userInfo.getEchelonDBs().getJSONArray("MainSlotServerIds").getString(3)+",\"BattleEntityId\":{\"uniqueId\":16777219},\"HeroId\":13003,\"Grade\":2,\"Level\":1,\"ExSkillLevel\":1,\"PublicSkillLevel\":1,\"PassiveSkillLevel\":1,\"ExtraPassiveSkillLevel\":-1,\"StatSnapshotCollection\":[{\"Stat\":1,\"Start\":2620,\"End\":2620},{\"Stat\":2,\"Start\":389,\"End\":389},{\"Stat\":3,\"Start\":21,\"End\":21},{\"Stat\":4,\"Start\":1619,\"End\":1619},{\"Stat\":5,\"Start\":897,\"End\":897},{\"Stat\":7,\"Start\":199,\"End\":199},{\"Stat\":9,\"Start\":199,\"End\":199},{\"Stat\":12,\"Start\":20000,\"End\":22800},{\"Stat\":30,\"Start\":10000,\"End\":10000},{\"Stat\":34,\"Start\":10000,\"End\":10000},{\"Stat\":42,\"Start\":700,\"End\":700},{\"Stat\":44,\"Start\":0,\"End\":0}],\"HPRateBefore\":10000,\"HPRateAfter\":10000,\"CrowdControlCount\":0,\"CrowdControlDuration\":0,\"EvadeCount\":0,\"DamageImmuneCount\":0,\"CrowdControlImmuneCount\":0,\"DeadFrame\":-1,\"TacticEntityType\":1,\"GivenNumericLogs\":[{\"EntityType\":\"Character\",\"Category\":1,\"Source\":1,\"CalculatedSum\":1829,\"AppliedSum\":849,\"Count\":10,\"CriticalMultiplierMax\":23806,\"CriticalCount\":1,\"CalculatedMin\":170,\"CalculatedMax\":194,\"AppliedMin\":0,\"AppliedMax\":183},{\"EntityType\":\"Obstacle\",\"Category\":1,\"Source\":1,\"CalculatedSum\":193,\"AppliedSum\":386,\"Count\":1,\"CriticalMultiplierMax\":10000,\"CriticalCount\":0,\"CalculatedMin\":193,\"CalculatedMax\":193,\"AppliedMin\":386,\"AppliedMax\":386},{\"EntityType\":\"Character\",\"Category\":1,\"Source\":2,\"CalculatedSum\":1001,\"AppliedSum\":165,\"Count\":1,\"CriticalMultiplierMax\":10000,\"CriticalCount\":0,\"CalculatedMin\":1001,\"CalculatedMax\":1001,\"AppliedMin\":165,\"AppliedMax\":165}],\"SkillCount\":{\"PublicSkill01\":1,\"ExSkill01\":1},\"KillLog\":{\"16777229\":1145,\"16777234\":2134}}],\"Supporters\":[{\"ServerId\":"+userInfo.getEchelonDBs().getJSONArray("SupportSlotServerIds").getString(0)+",\"BattleEntityId\":{\"uniqueId\":1073741825},\"HeroId\":26000,\"Grade\":1,\"Level\":1,\"ExSkillLevel\":1,\"PublicSkillLevel\":1,\"PassiveSkillLevel\":-1,\"ExtraPassiveSkillLevel\":-1,\"StatSnapshotCollection\":[{\"Stat\":1,\"Start\":2515,\"End\":2515},{\"Stat\":2,\"Start\":122,\"End\":122},{\"Stat\":3,\"Start\":25,\"End\":25},{\"Stat\":4,\"Start\":2399,\"End\":2399},{\"Stat\":5,\"Start\":99,\"End\":99},{\"Stat\":7,\"Start\":1093,\"End\":1093},{\"Stat\":9,\"Start\":198,\"End\":198},{\"Stat\":12,\"Start\":20000,\"End\":20000},{\"Stat\":30,\"Start\":10000,\"End\":10000},{\"Stat\":34,\"Start\":10000,\"End\":10000},{\"Stat\":42,\"Start\":700,\"End\":700},{\"Stat\":44,\"Start\":0,\"End\":0}],\"HPRateBefore\":0,\"HPRateAfter\":0,\"CrowdControlCount\":0,\"CrowdControlDuration\":0,\"EvadeCount\":0,\"DamageImmuneCount\":0,\"CrowdControlImmuneCount\":0,\"DeadFrame\":-1,\"TacticEntityType\":1,\"SkillCount\":{\"PublicSkill01\":1}}],\"UseAutoSkill\":false,\"TssUseCount\":0,\"SkillCostSummary\":{\"InitialCost\":9.127959,\"CostPerFrameSnapshots\":[{\"Frame\":61,\"Regen\":0.009333333}],\"CostAddSnapshots\":[],\"CostUseSnapshots\":[{\"Frame\":2062,\"Used\":5.0,\"CharId\":13003,\"Level\":1},{\"Frame\":2124,\"Used\":3.0,\"CharId\":13010,\"Level\":1}]}},\"Group02Summary\":{\"TeamId\":10009,\"LeaderEntityId\":{\"uniqueId\":0},\"Heroes\":[{\"ServerId\":0,\"BattleEntityId\":{\"uniqueId\":16777220},\"HeroId\":7000001,\"DeadFrame\":213,\"TacticEntityType\":2},{\"ServerId\":0,\"BattleEntityId\":{\"uniqueId\":16777221},\"HeroId\":7000001,\"DeadFrame\":266,\"TacticEntityType\":2},{\"ServerId\":0,\"BattleEntityId\":{\"uniqueId\":16777222},\"HeroId\":7000011,\"DeadFrame\":431,\"TacticEntityType\":2},{\"ServerId\":0,\"BattleEntityId\":{\"uniqueId\":16777223},\"HeroId\":7000011,\"DeadFrame\":434,\"TacticEntityType\":2},{\"ServerId\":0,\"BattleEntityId\":{\"uniqueId\":16777224},\"HeroId\":7000011,\"DeadFrame\":495,\"TacticEntityType\":2},{\"ServerId\":0,\"BattleEntityId\":{\"uniqueId\":16777225},\"HeroId\":7000001,\"DeadFrame\":764,\"TacticEntityType\":2},{\"ServerId\":0,\"BattleEntityId\":{\"uniqueId\":16777226},\"HeroId\":7000001,\"DeadFrame\":841,\"TacticEntityType\":2},{\"ServerId\":0,\"BattleEntityId\":{\"uniqueId\":16777227},\"HeroId\":7000011,\"DeadFrame\":973,\"TacticEntityType\":2},{\"ServerId\":0,\"BattleEntityId\":{\"uniqueId\":16777228},\"HeroId\":7000011,\"DeadFrame\":1042,\"TacticEntityType\":2},{\"ServerId\":0,\"BattleEntityId\":{\"uniqueId\":16777229},\"HeroId\":7000011,\"DeadFrame\":1145,\"TacticEntityType\":2},{\"ServerId\":0,\"BattleEntityId\":{\"uniqueId\":16777230},\"HeroId\":7000001,\"DeadFrame\":1483,\"TacticEntityType\":2},{\"ServerId\":0,\"BattleEntityId\":{\"uniqueId\":16777231},\"HeroId\":7000001,\"DeadFrame\":1528,\"TacticEntityType\":2},{\"ServerId\":0,\"BattleEntityId\":{\"uniqueId\":16777232},\"HeroId\":7000011,\"DeadFrame\":1690,\"TacticEntityType\":2},{\"ServerId\":0,\"BattleEntityId\":{\"uniqueId\":16777233},\"HeroId\":7000011,\"DeadFrame\":1579,\"TacticEntityType\":2},{\"ServerId\":0,\"BattleEntityId\":{\"uniqueId\":16777234},\"HeroId\":7000013,\"Grade\":1,\"Level\":1,\"ExSkillLevel\":-1,\"PublicSkillLevel\":-1,\"PassiveSkillLevel\":-1,\"ExtraPassiveSkillLevel\":-1,\"StatSnapshotCollection\":[{\"Stat\":1,\"Start\":4050,\"End\":4050},{\"Stat\":2,\"Start\":211,\"End\":211},{\"Stat\":3,\"Start\":80,\"End\":80},{\"Stat\":4,\"Start\":1400,\"End\":1400},{\"Stat\":5,\"Start\":100,\"End\":100},{\"Stat\":7,\"Start\":100,\"End\":100},{\"Stat\":9,\"Start\":250,\"End\":250},{\"Stat\":12,\"Start\":20000,\"End\":20000},{\"Stat\":30,\"Start\":10000,\"End\":10000},{\"Stat\":34,\"Start\":10000,\"End\":10000},{\"Stat\":42,\"Start\":0,\"End\":0},{\"Stat\":44,\"Start\":0,\"End\":0}],\"HPRateBefore\":10000,\"HPRateAfter\":0,\"CrowdControlCount\":0,\"CrowdControlDuration\":0,\"EvadeCount\":0,\"DamageImmuneCount\":0,\"CrowdControlImmuneCount\":0,\"DeadFrame\":2134,\"TacticEntityType\":8,\"GivenNumericLogs\":[{\"EntityType\":\"Character\",\"Category\":1,\"Source\":1,\"CalculatedSum\":3165,\"AppliedSum\":655,\"Count\":15,\"CriticalMultiplierMax\":15000,\"CriticalCount\":2,\"CalculatedMin\":211,\"CalculatedMax\":211,\"AppliedMin\":41,\"AppliedMax\":61}],\"TakenNumericLogs\":[{\"EntityType\":\"Character\",\"Category\":1,\"Source\":1,\"CalculatedSum\":8764,\"AppliedSum\":3231,\"Count\":30,\"CriticalMultiplierMax\":15000,\"CriticalCount\":2,\"CalculatedMin\":179,\"CalculatedMax\":404,\"AppliedMin\":65,\"AppliedMax\":183},{\"EntityType\":\"Character\",\"Category\":1,\"Source\":3,\"CalculatedSum\":6224,\"AppliedSum\":654,\"Count\":9,\"CriticalMultiplierMax\":10000,\"CriticalCount\":0,\"CalculatedMin\":622,\"CalculatedMax\":776,\"AppliedMin\":65,\"AppliedMax\":82},{\"EntityType\":\"Character\",\"Category\":1,\"Source\":2,\"CalculatedSum\":1001,\"AppliedSum\":165,\"Count\":1,\"CriticalMultiplierMax\":10000,\"CriticalCount\":0,\"CalculatedMin\":1001,\"CalculatedMax\":1001,\"AppliedMin\":165,\"AppliedMax\":165}]}],\"UseAutoSkill\":false,\"TssUseCount\":0,\"SkillCostSummary\":{\"InitialCost\":0.0,\"CostPerFrameSnapshots\":[],\"CostAddSnapshots\":[],\"CostUseSnapshots\":[]}},\"ElapsedRealtime\":59.25093},\"Hand\":{\"Cost\":2.671988,\"SkillCardsInHand\":[{\"CharacterId\":16003,\"HandIndex\":0,\"SkillId\":-1520051293,\"RemainCoolTime\":0},{\"CharacterId\":13003,\"HandIndex\":1,\"SkillId\":-12458155,\"RemainCoolTime\":0},{\"CharacterId\":26000,\"HandIndex\":2,\"SkillId\":1922876793,\"RemainCoolTime\":0},{\"CharacterId\":13010,\"HandIndex\":3,\"SkillId\":945851505,\"RemainCoolTime\":0}]},\"SkipSummary\":null,\"ClientUpTime\":72,\"Resendable\":true,\"Hash\":25804163514406,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        builder.addTextBody("packet", Gzip.enCrypt(packet), strContent);
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/campaign/tacticresult", builder);
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
    public void usercreate56(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("protocol", "3KUKWHN3VDLAXVS4YDYBTTVZ2Y", strContent);
        builder.addTextBody("encode", "True", strContent);
        String packet = "{\"Protocol\":1006,\"TutorialIds\":[1,2,3,4],\"ClientUpTime\":1,\"Resendable\":true,\"Hash\":4320737099815,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        builder.addTextBody("packet", Gzip.enCrypt(packet), strContent);
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/account/settutorial", builder);
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
    public void attendanceReward(int id,int id2,UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("protocol", "AZELQARARQP5L57ZML5KM3BRJ4", strContent);
        builder.addTextBody("encode", "True", strContent);
        String packet = "{\"Protocol\":9002,\"DayByBookUniqueId\":{\""+id+"\":"+id2+"},\"AttendanceBookUniqueId\":0,\"Day\":0,\"ClientUpTime\":"+id+",\"Resendable\":true,\"Hash\":38663"+id+"95598633,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        builder.addTextBody("packet", Gzip.enCrypt(packet), strContent);
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/attendance/reward", builder);
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
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='领取签到奖励到邮件' where `order`=? and status=1";
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
    public void usercreate59(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("protocol", "3KUKWHN3VDLAXVS4YDYBTTVZ2Y", strContent);
        builder.addTextBody("encode", "True", strContent);
        String packet = "{\"Protocol\":1006,\"TutorialIds\":[1,2,3,4,5],\"ClientUpTime\":174,\"Resendable\":true,\"Hash\":4320737099825,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        builder.addTextBody("packet", Gzip.enCrypt(packet), strContent);
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/account/settutorial", builder);
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
    public void usercreate60(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("protocol", "3KUKWHN3VDLAXVS4YDYBTTVZ2Y", strContent);
        builder.addTextBody("encode", "True", strContent);
        String packet = "{\"Protocol\":1006,\"TutorialIds\":[1,2,3,4,5,22],\"ClientUpTime\":14,\"Resendable\":true,\"Hash\":4320737099832,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        builder.addTextBody("packet", Gzip.enCrypt(packet), strContent);
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/account/settutorial", builder);
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
    public void usercreate61(UserInfo userInfo) {
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
    public void usercreate62(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("protocol", "63AWLKA74BAHU6K3JNGSV7X65M", strContent);
        builder.addTextBody("encode", "True", strContent);
        String packet = "{\"Protocol\":19003,\"ScriptGroupId\":700,\"SkipPointScriptCount\":6,\"ClientUpTime\":53,\"Resendable\":true,\"Hash\":81617263525955,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        builder.addTextBody("packet", Gzip.enCrypt(packet), strContent);
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/scenario/skip", builder);
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("protocol", "FF3K76PH2LLL6OPCYVKKENNHGE", strContent);
        builder.addTextBody("encode", "True", strContent);
        String packet = "{\"Protocol\":8002,\"MissionCategory\":5,\"GuideMissionSeasonId\":null,\"EventContentId\":null,\"ClientUpTime\":118,\"Resendable\":true,\"Hash\":34368328302661,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        builder.addTextBody("packet", Gzip.enCrypt(packet), strContent);
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/mission/multiplereward", builder);
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("protocol", "URZSRU73NZ7TFCFVHVORFXNFSE"));
        params.add(new BasicNameValuePair("encode", "True"));
        String packet = "{\"Protocol\":7000,\"IsReadMail\":false,\"PivotTime\":\"2023-03-09T00:52:19.6397241\",\"PivotIndex\":-1,\"IsDescending\":true,\"ClientUpTime\":16,\"Resendable\":true,\"Hash\":30064771072076,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        params.add(new BasicNameValuePair("packet", Gzip.enCrypt(packet)));
        result = HttpClientPool.sendPost(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/mail/list", params);
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
            for(int i=0;i<js.getJSONArray("MailDBs").size();i++){
                if(js.getJSONArray("MailDBs").getJSONObject(i).getJSONArray("ParcelInfos").getJSONObject(0).getJSONObject("Key").getString("Id").equals("3") || js.getJSONArray("MailDBs").getJSONObject(i).getJSONArray("ParcelInfos").getJSONObject(0).getJSONObject("Key").getString("Id").equals("6999") || js.getJSONArray("MailDBs").getJSONObject(i).getJSONArray("ParcelInfos").getJSONObject(0).getJSONObject("Key").getString("Id").equals("1")){
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("protocol", "X3CP3SJNLNSPJAUT24VJ6UZAJA"));
        params.add(new BasicNameValuePair("encode", "True"));
        String packet = "{\"Protocol\":7002,\"MailServerIds\":"+userInfo.getMail().toJSONString()+",\"ClientUpTime\":253,\"Resendable\":true,\"Hash\":30073361006672,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        params.add(new BasicNameValuePair("packet", Gzip.enCrypt(packet)));
        result = HttpClientPool.sendPost(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/mail/receive", params);
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
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
