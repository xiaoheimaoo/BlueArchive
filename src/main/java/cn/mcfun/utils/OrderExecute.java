package cn.mcfun.utils;

import cn.mcfun.api.UserCreate;
import cn.mcfun.entity.UserInfo;
import org.apache.http.impl.client.BasicCookieStore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
        userInfo.setIp(ip);
        Connection conn2 = getConnection();
        String sql2 = "update `order` set status=1 where `order`="+userInfo.getOrder()+" and status!=1";
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
        UserCreate uc = new UserCreate();
        uc.usercreate1(userInfo);
        uc.usercreate2(userInfo);
        uc.usercreate3(userInfo);
        uc.usercreate4(userInfo);
        uc.usercreate5(userInfo);
        uc.usercreate6(userInfo);
        uc.usercreate7(userInfo);
        uc.usercreate8(userInfo);
        //登录
        uc.usercreate9(userInfo);
        uc.usercreate10(userInfo);
        uc.usercreate11(userInfo);
        uc.usercreate12(userInfo);
        uc.usercreate14(userInfo);

        uc.usercreate15(userInfo);
        uc.usercreate16(userInfo);
        uc.usercreate17(userInfo);
        uc.usercreate18(userInfo);
        uc.usercreate19(userInfo);
        uc.usercreate20(userInfo);
        uc.usercreate21(userInfo);
        uc.usercreate22(userInfo);
        uc.usercreate23(userInfo);
        uc.usercreate24(userInfo);
        uc.usercreate25(userInfo);
        uc.usercreate26(userInfo);
        uc.usercreate27(userInfo);
        uc.usercreate28(userInfo);
        uc.usercreate29(userInfo);
        uc.usercreate30(userInfo);
        uc.usercreate31(userInfo);
        uc.usercreate32(userInfo);
        uc.usercreate33(userInfo);
        uc.usercreate34(userInfo);
        uc.usercreate35(userInfo);
        uc.usercreate36(userInfo);
        uc.usercreate37(userInfo);
        uc.usercreate38(userInfo);
        uc.usercreate39(userInfo);
        uc.usercreate40(userInfo);
        uc.usercreate41(userInfo);
        uc.usercreate42(userInfo);
        uc.usercreate43(userInfo);
        uc.usercreate44(userInfo);
        uc.usercreate45(userInfo);
        uc.usercreate46(userInfo);
        uc.usercreate47(userInfo);
        uc.usercreate48(userInfo);
        uc.usercreate49(userInfo);
        uc.usercreate50(userInfo);
        uc.usercreate51(userInfo);
        uc.usercreate52(userInfo);
        uc.usercreate53(userInfo);
        uc.usercreate54(userInfo);
        uc.usercreate55(userInfo);
        uc.usercreate56(userInfo);
        if(userInfo.getAttendanceBookRewards().size() >= 1){
            for(int i=0;i<userInfo.getAttendanceBookRewards().size();i++){
                uc.attendanceReward(Integer.parseInt((String) userInfo.getAttendanceBookRewards().get(i)),1,userInfo);
            }
        }
        if(userInfo.getAttendanceHistoryDBs().size() >= 1){
            for(int i=0;i<userInfo.getAttendanceHistoryDBs().size();i++){
                uc.attendanceReward(Integer.parseInt(userInfo.getAttendanceHistoryDBs().get(i).toString().split("-")[0]),Integer.parseInt(userInfo.getAttendanceHistoryDBs().get(i).toString().split("-")[1]),userInfo);
            }
        }
        uc.usercreate59(userInfo);
        uc.usercreate60(userInfo);
        uc.usercreate61(userInfo);
        uc.usercreate62(userInfo);
        uc.missionMultiplereward(userInfo);
        uc.mailList(userInfo);
        if(!userInfo.getMail().toJSONString().equals("[]")){
            uc.mailReceive(userInfo);
        }
        Connection conn3 = getConnection();
        String sql3 = "update `order` set uid=?,transcode=?,AccountId=?,`status`=2,`message`=?,`StarNum`=? where `order`=? and status=1";
        PreparedStatement ps3 = null;
        try {
            ps3 = conn3.prepareStatement(sql3);
            ps3.setString(1, userInfo.getUid());
            ps3.setString(2, userInfo.getTranscode());
            ps3.setString(3, userInfo.getAccountId().toString());
            ps3.setString(4, "订单已完成");
            ps3.setInt(5, userInfo.getStarNum());
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
