package cn.mcfun.entity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.util.HashMap;
import java.util.Map;

public class UserInfo {
    private String Hint;
    private String Question;
    private String Answer;
    private String ip;
    private String order;
    private BasicCookieStore cookie;
    private String deviceId;
    private String uid;
    private String token;
    private String accessToken;
    private String EnterTicket;
    private String MxToken;
    private Long AccountId;
    private String SessionKey;
    private String transcode;
    private Long ServerId1;
    private Long ServerId2;
    private Long ServerId3;
    private Long ServerId4;
    private JSONObject EchelonDBs = new JSONObject();
    private JSONArray CharacterDBs = new JSONArray();
    private JSONArray svts = new JSONArray();
    private Map<Integer,String> AttendanceBookRewards = new HashMap<>();
    private JSONArray mail = new JSONArray();
    private int StarNum = 0;
    private int Gem = 0;
    private int Ticket = 0;
    private int RecruitCount = 0;
    private CloseableHttpClient httpClientBuilder = null;
    private CloseableHttpClient httpClientBuilder2 = null;
    public UserInfo() {
    }

    public CloseableHttpClient getHttpClientBuilder2() {
        return httpClientBuilder2;
    }

    public void setHttpClientBuilder2(CloseableHttpClient httpClientBuilder2) {
        this.httpClientBuilder2 = httpClientBuilder2;
    }

    public int getRecruitCount() {
        return RecruitCount;
    }

    public void setRecruitCount(int recruitCount) {
        RecruitCount = recruitCount;
    }

    public String getHint() {
        return Hint;
    }

    public void setHint(String hint) {
        Hint = hint;
    }

    public String getAnswer() {
        return Answer;
    }

    public void setAnswer(String answer) {
        Answer = answer;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public int getTicket() {
        return Ticket;
    }

    public void setTicket(int ticket) {
        Ticket = ticket;
    }

    public CloseableHttpClient getHttpClientBuilder() {
        return httpClientBuilder;
    }

    public void setHttpClientBuilder(CloseableHttpClient httpClientBuilder) {
        this.httpClientBuilder = httpClientBuilder;
    }

    public JSONArray getSvts() {
        return svts;
    }

    public void setSvts(JSONArray svts) {
        this.svts = svts;
    }

    public int getGem() {
        return Gem;
    }

    public void setGem(int gem) {
        Gem = gem;
    }

    public int getStarNum() {
        return StarNum;
    }

    public void setStarNum(int starNum) {
        StarNum = starNum;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Map<Integer, String> getAttendanceBookRewards() {
        return AttendanceBookRewards;
    }

    public void setAttendanceBookRewards(Map<Integer, String> attendanceBookRewards) {
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
