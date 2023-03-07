package cn.mcfun.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import static cn.mcfun.utils.Hikari.getConnection;

public class OrderType {
    public void type(String questId, String s, String userId, String order) {
        String deviceid = null;
        String battleId = "";
        Connection conn = getConnection();
        String sql = "select dataVer,usk,deviceid,questId,channel,battleId from `order` where `order`=? and status=1";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, order);
            rs = ps.executeQuery();
            while (rs.next()) {
                deviceid = rs.getString("deviceid");
                battleId = rs.getString("battleId");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                conn.close();
                rs.close();
                ps.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        //TODO
    }
}
