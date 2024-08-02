package cn.mcfun.api;

import cn.mcfun.Main;
import cn.mcfun.entity.StudentName;
import cn.mcfun.entity.UserInfo;
import cn.mcfun.utils.Gzip;
import cn.mcfun.utils.HashMatching;
import cn.mcfun.utils.HttpClientPool;
import cn.mcfun.utils.Md5;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import static cn.mcfun.Main.Cookie;
import static cn.mcfun.utils.Hikari.getConnection;

public class UserCreate {
    public void getTicket(UserInfo userInfo) {
        String result;
        String packet = "{\"Protocol\":50000,\"YostarUID\":" + userInfo.getUid() + ",\"YostarToken\":\"" + userInfo.getAccessToken() + "\",\"WaitingTicket\":\"\",\"ClientVersion\":\"" + Main.ClientVersion + "\",\"Resendable\":true}";
        byte[] builder = Gzip.enCrypt2(packet);
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-gateway.bluearchiveyostar.com:5100/api/gateway", builder);
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
        } else if (result.contains("Error") && result.contains("State:99")){
            Connection conn2 = getConnection();
            String sql2 = "update `order` set status=3,message=? where `order`=? and status=1";
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.setString(1, "accessToken过期账号");
                ps2.setString(2, userInfo.getOrder());
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            Thread.currentThread().stop();
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
        String packet = "{\"Protocol\":1009,\"UID\":0,\"YostarToken\":null,\"EnterTicket\":\""+userInfo.getEnterTicket()+"\",\"PassCookieResult\":true,\"Cookie\":\""+Cookie+"\",\"ClientUpTime\":0,\"Resendable\":false,\"Hash\":4333622001683,\"IsTest\":false,\"SessionKey\":null,\"AccountId\":0}";
        byte[] builder = Gzip.enCrypt2(packet);
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
        String packet = "{\"Protocol\":1002,\"Version\":0,\"DevId\":null,\"IMEI\":0,\"AccessIP\":\"127.0.0.1\",\"MarketId\":\"aas\",\"UserType\":null,\"AdvertisementId\":null,\"OSType\":\"I\",\"OSVersion\":\"iPadOS 16.2\",\"DeviceUniqueId\":\"" + userInfo.getDeviceId() + "\",\"DeviceModel\":\"iPad11,1\",\"DeviceSystemMemorySize\":2923,\"ClientUpTime\":0,\"Resendable\":true,\"Hash\":4303557230598,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        byte[] builder = Gzip.enCrypt2(packet);
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
            if (js.containsKey("SessionKey")) {
                userInfo.setSessionKey(js.getString("SessionKey"));
            }
            if (!js.getJSONArray("AttendanceBookRewards").isEmpty()) {
                for (int i = 0; i < js.getJSONArray("AttendanceBookRewards").size(); i++) {
                    userInfo.getAttendanceBookRewards().put(js.getJSONArray("AttendanceBookRewards").getJSONObject(i).getIntValue("UniqueId"),js.getJSONArray("AttendanceBookRewards").getJSONObject(i).getString("BookSize")+"-0");
                }
            }
            if (js.containsKey("AttendanceHistoryDBs")) {
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = sdf.format(date);
                for (int i = 0; i < js.getJSONArray("AttendanceHistoryDBs").size(); i++) {
                    JSONObject db = js.getJSONArray("AttendanceHistoryDBs").getJSONObject(i);
                    if (!db.getString("AttendedDay").contains(formattedDate) && userInfo.getAttendanceBookRewards().containsKey(db.getIntValue("AttendanceBookUniqueId"))) {
                        userInfo.getAttendanceBookRewards().put(db.getIntValue("AttendanceBookUniqueId"),userInfo.getAttendanceBookRewards().get(db.getIntValue("AttendanceBookUniqueId")).split("-")[0]+"-"+db.getJSONObject("AttendedDay").size());
                    }else if(db.getString("AttendedDay").contains(formattedDate) && userInfo.getAttendanceBookRewards().containsKey(db.getIntValue("AttendanceBookUniqueId"))){
                        userInfo.getAttendanceBookRewards().put(db.getIntValue("AttendanceBookUniqueId"),userInfo.getAttendanceBookRewards().get(db.getIntValue("AttendanceBookUniqueId")).split("-")[0]+"-"+userInfo.getAttendanceBookRewards().get(db.getIntValue("AttendanceBookUniqueId")).split("-")[0]);
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

    public void ProofToken_RequestQuestion(UserInfo userInfo) {
        String result;
        String packet = "{\"Protocol\":37000,\"ClientUpTime\":27,\"Resendable\":true,\"Hash\":158913789952007,\"IsTest\":false,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        byte[] builder = Gzip.enCrypt2(packet);
        do{
            result = HttpClientPool.postFileMultiPart2(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        }while(result.contains("request exceed"));
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
        String packet = "{\"Protocol\":1017,\"SyncProtocols\":[20000,1003,2000,3000,4000,5000,12000,6000,22001,17018,21000,28001,33000,19000,10006,39006,44000,29002,30041],\"ClientUpTime\":20,\"Resendable\":true,\"Hash\":4367981740041,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        byte[] builder = Gzip.enCrypt2(packet);
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

// 直接提取所需的JSON对象和数组
            JSONObject echelonDBs = js.getJSONObject("EchelonListResponse").getJSONArray("EchelonDBs").getJSONObject(0);
            JSONArray characterDBs = js.getJSONObject("CharacterListResponse").getJSONArray("CharacterDBs");
            JSONArray ShopFreeRecruitHistoryDBs = null;
            if(js.getJSONObject("ShopGachaRecruitListResponse").containsKey("ShopFreeRecruitHistoryDBs")){
                ShopFreeRecruitHistoryDBs = js.getJSONObject("ShopGachaRecruitListResponse").getJSONArray("ShopFreeRecruitHistoryDBs");
                for(int i=0;i<ShopFreeRecruitHistoryDBs.size();i++){
                    if(ShopFreeRecruitHistoryDBs.getJSONObject(i).getIntValue("UniqueId") == 6){
                        userInfo.setRecruitCount(ShopFreeRecruitHistoryDBs.getJSONObject(i).getIntValue("RecruitCount"));
                    }
                }
            }
            userInfo.setEchelonDBs(echelonDBs);
            userInfo.setCharacterDBs(characterDBs);

// 避免在循环中多次调用 getJSONObject
            for (int i = 0; i < characterDBs.size(); i++) {
                JSONObject characterDB = characterDBs.getJSONObject(i);

                // 提前获取需要的值
                String starGrade = characterDB.getString("StarGrade");
                Integer uniqueId = characterDB.getInteger("UniqueId");

                // 检查 starGrade 是否为 "3"
                if ("3".equals(starGrade)) {
                    userInfo.setStarNum(userInfo.getStarNum() + 1);

                    // 仅当需要时才调用 StudentName.getStudentName()
                    if (uniqueId != null) {
                        userInfo.getSvts().add(StudentName.getStudentName(uniqueId));
                    }
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

    public void Item_List(UserInfo userInfo,int r) {
        String result;
        String packet = "{\"Protocol\":4000,\"ClientUpTime\":0,\"Resendable\":true,\"Hash\":1717986918400"+r+",\"IsTest\":false,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        byte[] builder = Gzip.enCrypt2(packet);
        result = HttpClientPool.postFileMultiPart(userInfo, "https://prod-game.bluearchiveyostar.com:5000/api/gateway", builder);
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (result.contains("packet")) {
            if (!result.contains("Error")) {
                JSONArray js = JSONObject.parseObject(jsonObject.getString("packet")).getJSONArray("ItemDBs");
                for (int i = 0; i < js.size(); i++) {
                    if (js.getJSONObject(i).getString("UniqueId").equals("6999")) {
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
            }else{
                Item_List(userInfo, new Random().nextInt(10));
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
        String packet = "{\"Answer\":"+userInfo.getAnswer()+",\"Protocol\":37001,\"ClientUpTime\":0,\"Resendable\":true,\"Hash\":158918084919306,\"IsTest\":false,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        byte[] builder = Gzip.enCrypt2(packet);
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

    public void attendanceReward(int i, int id, int id2, UserInfo userInfo) {
        String result;
        String packet = "{\"Protocol\":9002,\"DayByBookUniqueId\":{\""+id+"\":"+id2+"},\"AttendanceBookUniqueId\":0,\"Day\":0,\"ClientUpTime\":"+i+",\"Resendable\":true,\"IsTest\":false,\"Hash\":43679"+i+"1740041,\"SessionKey\":"+userInfo.getSessionKey()+",\"AccountId\":"+userInfo.getAccountId()+"}";
        byte[] builder = Gzip.enCrypt2(packet);
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
        String packet = "{\"Protocol\":7001,\"ClientUpTime\":0,\"Resendable\":true,\"Hash\":30069066032433,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        byte[] builder = Gzip.enCrypt2(packet);
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
        String packet = "{\"Protocol\":7001,\"ClientUpTime\":12,\"Resendable\":true,\"Hash\":30069066039433,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        byte[] builder = Gzip.enCrypt2(packet);
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
        String packet = "{\"Protocol\":7000,\"IsReadMail\":false,\"PivotTime\":\"2024-03-09T00:52:19.6397241\",\"PivotIndex\":-1,\"IsDescending\":true,\"ClientUpTime\":1,\"Resendable\":true,\"Hash\":30064771072076,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        byte[] builder = Gzip.enCrypt2(packet);
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
            if (js.containsKey("MailDBs")) {
                JSONArray mailDBs = js.getJSONArray("MailDBs");
                if (mailDBs.size() > 0) {
                    for (int i = 0; i < mailDBs.size(); i++) {
                        JSONArray parcelInfos = mailDBs.getJSONObject(i).getJSONArray("ParcelInfos");
                        for (int j = 0; j < parcelInfos.size(); j++) {
                            JSONObject key = parcelInfos.getJSONObject(j).getJSONObject("Key");
                            String id = key.getString("Id");
                            if (id.equals("3") || id.equals("6999")) {
                                userInfo.getMail().add(mailDBs.getJSONObject(i).getLong("ServerId"));
                                break;
                            }
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
        String packet = "{\"Protocol\":7000,\"IsReadMail\":false,\"PivotTime\":\"2024-03-09T00:52:19.6397241\",\"PivotIndex\":-1,\"IsDescending\":true,\"ClientUpTime\":2,\"Resendable\":true,\"Hash\":30064771072076,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        byte[] builder = Gzip.enCrypt2(packet);
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
        String packet = "{\"Protocol\":7002,\"MailServerIds\":[" + id + "],\"ClientUpTime\":7,\"Resendable\":true,\"Hash\":30073361006672,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        byte[] builder = Gzip.enCrypt2(packet);
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
        String packet = "{\"Protocol\":10008,\"FreeRecruitId\":6,\"Cost\":null,\"GoodsId\":35779,\"ShopUniqueId\":50606,\"ClientUpTime\":" + id + ",\"Resendable\":true,\"Hash\":429840326984" + id + ",\"IsTest\": false,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        byte[] builder = Gzip.enCrypt2(packet);
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
        String packet = "{\"Protocol\":1001,\"Nickname\":\"アカウ\",\"ClientUpTime\":27,\"Resendable\":true,\"Hash\":4299262263320,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        byte[] builder = Gzip.enCrypt2(packet);
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
        if (result.contains(":1001,") || (result.contains("ErrorCode") && result.contains("1006"))) {
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
        String packet = "{\"Protocol\":1010,\"CallName\":\"アカウ\",\"ClientUpTime\":14,\"Resendable\":true,\"Hash\":4337916968985,\"SessionKey\":" + userInfo.getSessionKey() + ",\"AccountId\":" + userInfo.getAccountId() + "}";
        byte[] builder = Gzip.enCrypt2(packet);
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
        if (result.contains(":1010,") || (result.contains("ErrorCode") && result.contains("34000"))) {
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
