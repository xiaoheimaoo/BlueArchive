package cn.mcfun.utils;

import cn.mcfun.api.UserCreate;
import cn.mcfun.entity.UserInfo;
import com.alibaba.fastjson.JSONArray;
import org.apache.http.impl.client.BasicCookieStore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static cn.mcfun.utils.Hikari.getConnection;

public class OrderExecute implements Runnable{

    private UserInfo userInfo;

    public OrderExecute(UserInfo userInfo){
        this.userInfo = userInfo;
    }

    @Override
    public void run() {
        try {
            userInfo.setCookie(new BasicCookieStore());
            login();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("-----------------");
            System.out.println(userInfo.getOrder());
            System.out.println("-----------------");
            Connection conn2 = getConnection();
            String sql2 = "update `order` set message='未知错误请重启订单！',status=3 where `order`="+userInfo.getOrder();
            PreparedStatement ps2 = null;
            try {
                ps2 = conn2.prepareStatement(sql2);
                ps2.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }finally {
                try {
                    conn2.close();
                    ps2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }

    public void login() {
        String ip = null;
        try {
            Connection conn = getConnection();
            String sql = "select ip from `proxy` order by rand() LIMIT 1";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ip = rs.getString("ip");
            }
            conn.close();
            rs.close();
            ps.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if(ip != null && !ip.equals("127.0.0.1")){
            userInfo.setIp(ip);
        }
        Connection conn2 = getConnection();
        String sql2 = "update `order` set `status`=1 where `order`=?";
        PreparedStatement ps2 = null;
        try {
            ps2 = conn2.prepareStatement(sql2);
            ps2.setString(1, userInfo.getOrder());
            ps2.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            try {
                conn2.close();
                ps2.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        UserCreate uc = new UserCreate();
        //登录
        uc.accountAuth(userInfo);
        //uc.requestQuestion(userInfo);
        //uc.academyGetinfo(userInfo);
        uc.accountLoginsync(userInfo);
        //uc.networktimeSync(userInfo);
        if(userInfo.getAttendanceBookRewards().size() >= 1){
            for(int i=0;i<userInfo.getAttendanceBookRewards().size();i++){
                uc.attendanceReward(Integer.parseInt((String) userInfo.getAttendanceBookRewards().get(i)),1,userInfo);
            }
        }
        if(userInfo.getAttendanceHistoryDBs().size() >= 1){
            for(int i=0;i<userInfo.getAttendanceHistoryDBs().size();i++){
                uc.attendanceReward(Integer.parseInt(userInfo.getAttendanceHistoryDBs().get(i).toString().split("-")[0]),Integer.parseInt(userInfo.getAttendanceHistoryDBs().get(i).toString().split("-")[1])+1,userInfo);
            }
        }
        uc.mailCheck1(userInfo);
        uc.mailCheck2(userInfo);
        uc.mailList(userInfo);
        for(int i=0;i<userInfo.getMail().size();i++){
            uc.mailReceive(userInfo.getMail().getInteger(i),userInfo);
        }
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = format.format(date);
        Connection conn3 = getConnection();
        String sql3 = "update `order` set `status`=2,`message`=?,`StarNum`=?,`Gem`=?,`svts`=?,`complete`=? where `order`=?";
        PreparedStatement ps3 = null;
        try {
            ps3 = conn3.prepareStatement(sql3);
            ps3.setString(1, "订单已完成");
            ps3.setInt(2, userInfo.getStarNum());
            ps3.setInt(3, userInfo.getGem());
            ps3.setString(4, userInfo.getSvts().toJSONString());
            ps3.setString(5, dateStr);
            ps3.setString(6, userInfo.getOrder());
            ps3.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                conn3.close();
                ps3.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

}
