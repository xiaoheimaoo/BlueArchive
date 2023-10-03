package cn.mcfun.api;

import cn.mcfun.entity.StudentName;
import cn.mcfun.entity.UserInfo;
import cn.mcfun.utils.Gzip;
import cn.mcfun.utils.HttpClientPool;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static cn.mcfun.utils.Hikari.getConnection;

public class UserCreate {
    ContentType strContent = ContentType.APPLICATION_OCTET_STREAM;

    public void accountAuth(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());
        String packet = "{\"Protocol\":1002,\"Version\":0,\"DevId\":null,\"IMEI\":0,\"AccessIP\":\"127.0.0.1\",\"MarketId\":\"aas\",\"UserType\":null,\"AdvertisementId\":null,\"OSType\":\"I\",\"OSVersion\":\"iPadOS 16.2\",\"DeviceUniqueId\":\"" + userInfo.getDeviceId() + "\",\"DeviceModel\":\"iPad11,1\",\"DeviceSystemMemorySize\":2923,\"ClientUpTime\":0,\"Resendable\":true,\"Hash\":4303557230598,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (!jsonObject.getString("protocol").equals("Error")) {
            JSONObject js = JSONObject.parseObject(jsonObject.getString("packet"));
            if (js.containsKey("SessionKey")) {
                userInfo.setSessionKey(js.getString("SessionKey"));
            }
            if (!js.getJSONArray("AttendanceBookRewards").isEmpty()) {
                for (int i = 0; i < js.getJSONArray("AttendanceBookRewards").size(); i++) {
                    userInfo.getAttendanceBookRewards().add(js.getJSONArray("AttendanceBookRewards").getJSONObject(i).getString("UniqueId"));
                }
            }
            if (js.containsKey("AttendanceHistoryDBs")) {
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = sdf.format(date);
                for (int i = 0; i < js.getJSONArray("AttendanceHistoryDBs").size(); i++) {
                    if(!js.getJSONArray("AttendanceHistoryDBs").getJSONObject(i).getString("AttendedDay").contains(formattedDate)){
                        userInfo.getAttendanceHistoryDBs().add(js.getJSONArray("AttendanceHistoryDBs").getJSONObject(i).getString("AttendanceBookUniqueId") + "-" + js.getJSONArray("AttendanceHistoryDBs").getJSONObject(i).getJSONObject("AttendedDay").size());
                    }
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

    public void accountLoginsync(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());
        String packet = "{\"Protocol\":1017,\"SyncProtocols\":[20000,1003,2000,3000,4000,5000,12000,6000,22001,17018,21000,28001,33000,19000,10006,39006,44000,29002,30041],\"ClientUpTime\":20,\"Resendable\":true,\"Hash\":4367981740041,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (!jsonObject.getString("protocol").equals("Error")) {
            JSONObject js = JSONObject.parseObject(jsonObject.getString("packet"));
            userInfo.setEchelonDBs(js.getJSONObject("EchelonListResponse").getJSONArray("EchelonDBs").getJSONObject(0));
            userInfo.setCharacterDBs(js.getJSONObject("CharacterListResponse").getJSONArray("CharacterDBs"));
            for (int i = 0; i < userInfo.getCharacterDBs().size(); i++) {
                if (userInfo.getCharacterDBs().getJSONObject(i).getString("StarGrade").equals("3")) {
                    userInfo.setStarNum(userInfo.getStarNum() + 1);
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

    public void Item_List(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());
        String packet = "{\"Protocol\":4000,\"ClientUpTime\":0,\"Resendable\":true,\"Hash\":17179869184006,\"IsTest\":false,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (!jsonObject.getString("protocol").equals("Error")) {
            JSONArray js = JSONObject.parseObject(jsonObject.getString("packet")).getJSONArray("ItemDBs");
            for(int i=0;i<js.size();i++){
                if(js.getJSONObject(i).getString("UniqueId").equals("6999")){
                    userInfo.setTicket(js.getJSONObject(i).getIntValue("StackCount"));
                    break;
                }
            }
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='获取十连券信息' where `order`=? and status=1";
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

    public void attendanceReward(int i, int id, int id2, UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());
        String packet = "{\"Protocol\":9002,\"DayByBookUniqueId\":{\"" + id + "\":" + id2 + "},\"AttendanceBookUniqueId\":0,\"Day\":0,\"ClientUpTime\":" + i + ",\"Resendable\":true,\"Hash\":38663" + id + "95598633,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (!jsonObject.getString("protocol").equals("Error")) {
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

    public void mailCheck1(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());
        String packet = "{\"Protocol\":7001,\"ClientUpTime\":0,\"Resendable\":true,\"Hash\":30069066032433,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (!jsonObject.getString("protocol").equals("Error")) {
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

    public void mailCheck2(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());
        String packet = "{\"Protocol\":7001,\"ClientUpTime\":12,\"Resendable\":true,\"Hash\":30069066039433,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (!jsonObject.getString("protocol").equals("Error")) {
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

    public void mailList(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());
        String packet = "{\"Protocol\":7000,\"IsReadMail\":false,\"PivotTime\":\"2024-03-09T00:52:19.6397241\",\"PivotIndex\":-1,\"IsDescending\":true,\"ClientUpTime\":1,\"Resendable\":true,\"Hash\":30064771072076,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (!jsonObject.getString("protocol").equals("Error")) {
            JSONObject js = JSONObject.parseObject(jsonObject.getString("packet"));
            if (js.containsKey("MailDBs") && js.getJSONArray("MailDBs").size() > 0) {
                for (int i = 0; i < js.getJSONArray("MailDBs").size(); i++) {
                    for (int j = 0; j < js.getJSONArray("MailDBs").getJSONObject(i).getJSONArray("ParcelInfos").size(); j++) {
                        if ((js.getJSONArray("MailDBs").getJSONObject(i).getJSONArray("ParcelInfos").getJSONObject(j).getJSONObject("Key").getString("Id").equals("3") ||
                                js.getJSONArray("MailDBs").getJSONObject(i).getJSONArray("ParcelInfos").getJSONObject(j).getJSONObject("Key").getString("Id").equals("6999"))) {
                            userInfo.getMail().add(js.getJSONArray("MailDBs").getJSONObject(i).getLong("ServerId"));
                            break;
                        }
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

    public void mailList2(UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());
        String packet = "{\"Protocol\":7000,\"IsReadMail\":false,\"PivotTime\":\"2024-03-09T00:52:19.6397241\",\"PivotIndex\":-1,\"IsDescending\":true,\"ClientUpTime\":2,\"Resendable\":true,\"Hash\":30064771072076,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (!jsonObject.getString("protocol").equals("Error")) {
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

    public void mailReceive(long id, UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());
        String packet = "{\"Protocol\":7002,\"MailServerIds\":[" + id + "],\"ClientUpTime\":7,\"Resendable\":true,\"Hash\":30073361006672,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (!jsonObject.getString("protocol").equals("Error")) {
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

    public void buyGacha3(int id, UserInfo userInfo) {
        String result;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create().setBoundary("BestHTTP_HTTPMultiPartForm_" + Gzip.genRandomNum());
        String packet = "{\"Protocol\":10008,\"FreeRecruitId\":4,\"Cost\":null,\"GoodsId\":35525,\"ShopUniqueId\":50384,\"ClientUpTime\":" + id + ",\"Resendable\":true,\"Hash\":429840326984"+id+",\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        InputStream stream = new ByteArrayInputStream(Gzip.enCrypt2(packet));
        builder.addBinaryBody("mx", stream, strContent, "mx.dat");
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (!jsonObject.getString("protocol").equals("Error")) {
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='抽卡中' where `order`=? and status=1";
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
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (!jsonObject.getString("protocol").equals("Error")) {
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
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (!jsonObject.getString("protocol").equals("Error")) {
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

}
