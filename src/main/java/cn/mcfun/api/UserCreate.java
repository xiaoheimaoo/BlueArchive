package cn.mcfun.api;

import cn.mcfun.Main;
import cn.mcfun.entity.StudentName;
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

import static cn.mcfun.utils.Hikari.getConnection;

public class UserCreate {
    ContentType strContent = ContentType.create("text/plain", Charset.forName("UTF-8"));
    public void accountAuth(UserInfo userInfo) {
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
    public void requestQuestion(UserInfo userInfo) {
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
    public void academyGetinfo(UserInfo userInfo) {
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
                    userInfo.getSvts().add(StudentName.getStudentName(userInfo.getCharacterDBs().getJSONObject(i).getInteger("UniqueId")));
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
    public void networktimeSync(UserInfo userInfo) {
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
            if(js.containsKey("MailDBs") && js.getJSONArray("MailDBs").size() > 0){
                for(int i=0;i<js.getJSONArray("MailDBs").size();i++){
                    if(js.getJSONArray("MailDBs").getJSONObject(i).getJSONArray("ParcelInfos").getJSONObject(0).getJSONObject("Key").getString("Id").equals("4020") ||
                            js.getJSONArray("MailDBs").getJSONObject(i).getJSONArray("ParcelInfos").getJSONObject(0).getJSONObject("Key").getString("Id").equals("4030") ||
                            js.getJSONArray("MailDBs").getJSONObject(i).getJSONArray("ParcelInfos").getJSONObject(0).getJSONObject("Key").getString("Id").equals("4050") ||
                            js.getJSONArray("MailDBs").getJSONObject(i).getJSONArray("ParcelInfos").getJSONObject(0).getJSONObject("Key").getString("Id").equals("6999") ||
                            js.getJSONArray("MailDBs").getJSONObject(i).getJSONArray("ParcelInfos").getJSONObject(0).getJSONObject("Key").getString("Id").equals("101001") ||
                            js.getJSONArray("MailDBs").getJSONObject(i).getJSONArray("ParcelInfos").getJSONObject(0).getJSONObject("Key").getString("Id").equals("102001") ||
                            js.getJSONArray("MailDBs").getJSONObject(i).getJSONArray("ParcelInfos").getJSONObject(0).getJSONObject("Key").getString("Id").equals("1") ||
                            js.getJSONArray("MailDBs").getJSONObject(i).getJSONArray("ParcelInfos").getJSONObject(0).getJSONObject("Key").getString("Id").equals("3") ||
                            js.getJSONArray("MailDBs").getJSONObject(i).getJSONArray("ParcelInfos").getJSONObject(0).getJSONObject("Key").getString("Id").equals("5") ||
                            js.getJSONArray("MailDBs").getJSONObject(i).getJSONArray("ParcelInfos").getJSONObject(0).getJSONObject("Key").getString("Id").equals("10") ||
                            js.getJSONArray("MailDBs").getJSONObject(i).getJSONArray("ParcelInfos").getJSONObject(0).getJSONObject("Key").getString("Id").equals("11")){
                        userInfo.getMail().add(js.getJSONArray("MailDBs").getJSONObject(i).getLong("ServerId"));
                    }
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
