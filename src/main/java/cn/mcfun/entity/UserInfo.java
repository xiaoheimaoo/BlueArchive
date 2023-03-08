package cn.mcfun.entity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.impl.client.BasicCookieStore;

public class UserInfo {
    String order;
    BasicCookieStore cookie;
    String deviceId;
    String uid;
    String token;
    String accessToken;
    String EnterTicket;
    String MxToken;
    Long AccountId;
    String SessionKey;
    String transcode;
    Long ServerId1;
    Long ServerId2;
    Long ServerId3;
    Long ServerId4;
    JSONObject EchelonDBs = new JSONObject();
    JSONArray CharacterDBs = new JSONArray();
    JSONArray AttendanceBookRewards = new JSONArray();
    JSONArray AttendanceHistoryDBs = new JSONArray();
    JSONArray mail = new JSONArray();

    public JSONArray getAttendanceHistoryDBs() {
        return AttendanceHistoryDBs;
    }

    public void setAttendanceHistoryDBs(JSONArray attendanceHistoryDBs) {
        AttendanceHistoryDBs = attendanceHistoryDBs;
    }

    public JSONArray getAttendanceBookRewards() {
        return AttendanceBookRewards;
    }

    public void setAttendanceBookRewards(JSONArray attendanceBookRewards) {
        AttendanceBookRewards = attendanceBookRewards;
    }

    public JSONArray getMail() {
        return mail;
    }

    public void setMail(JSONArray mail) {
        this.mail = mail;
    }

    public JSONArray getCharacterDBs() {
        return CharacterDBs;
    }

    public void setCharacterDBs(JSONArray characterDBs) {
        CharacterDBs = characterDBs;
    }

    public JSONObject getEchelonDBs() {
        return EchelonDBs;
    }

    public void setEchelonDBs(JSONObject echelonDBs) {
        EchelonDBs = echelonDBs;
    }

    public Long getServerId1() {
        return ServerId1;
    }

    public void setServerId1(Long serverId1) {
        ServerId1 = serverId1;
    }

    public Long getServerId2() {
        return ServerId2;
    }

    public void setServerId2(Long serverId2) {
        ServerId2 = serverId2;
    }

    public Long getServerId3() {
        return ServerId3;
    }

    public void setServerId3(Long serverId3) {
        ServerId3 = serverId3;
    }

    public Long getServerId4() {
        return ServerId4;
    }

    public void setServerId4(Long serverId4) {
        ServerId4 = serverId4;
    }

    public String getTranscode() {
        return transcode;
    }

    public void setTranscode(String transcode) {
        this.transcode = transcode;
    }

    public String getSessionKey() {
        return SessionKey;
    }

    public void setSessionKey(String sessionKey) {
        SessionKey = sessionKey;
    }

    public Long getAccountId() {
        return AccountId;
    }

    public void setAccountId(Long accountId) {
        AccountId = accountId;
    }

    public String getMxToken() {
        return MxToken;
    }

    public void setMxToken(String mxToken) {
        MxToken = mxToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getEnterTicket() {
        return EnterTicket;
    }

    public void setEnterTicket(String enterTicket) {
        EnterTicket = enterTicket;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public BasicCookieStore getCookie() {
        return cookie;
    }

    public void setCookie(BasicCookieStore cookie) {
        this.cookie = cookie;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
