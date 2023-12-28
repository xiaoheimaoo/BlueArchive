package cn.mcfun;

import cn.mcfun.entity.UserInfo;
import cn.mcfun.utils.HttpClientPool;
import cn.mcfun.utils.OrderExecute;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static cn.mcfun.utils.Hikari.getConnection;


public class Main{
    public static List<String> lines = new ArrayList<>();
    public static String ClientVersion = "1.28.196922";
    public static String BundleVersion = "8qOm7FS6jd";
    private static Main main;
    private boolean running;
    private final ThreadPoolExecutor executor;
    private static Queue<Runnable> taskList = new LinkedList<>();
    private static Queue<UserInfo> orders = new LinkedList<>();
    private static int CORE_POOL_SIZE = 300;//同时执行线程数
    public Main(){
        running = true;
        executor = new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                CORE_POOL_SIZE,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>()
        );
    }

    public static void main(String[] args) {
        File file = new File("config.properties");
        FileWriter writer;
        if (!file.exists()) {
            try {
                file.createNewFile();
                writer = new FileWriter(file, false);
                writer.append("#同时执行线程数\nthreads=300\n#数据库连接地址\nurl=localhost\n#数据库端口\nport=3306\n#库名\ndatabase=\n#数据库用户名\nuser=root\n#数据库密码\npassword=\n#游戏版本号\nClientVersion=1.28.196922\nBundleVersion=8qOm7FS6jd\n");
                writer.flush();
                writer.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        Properties props = new Properties();
        try {
            props.load(new FileInputStream("config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        CORE_POOL_SIZE = Integer.parseInt(props.getProperty("threads"));
        ClientVersion = props.getProperty("ClientVersion");
        BundleVersion = props.getProperty("BundleVersion");
        main = new Main();
        //查询并缓存需要执行的订单
        main.loadOrders();
        while (!orders.isEmpty()){
            synchronized (orders) {
                main.executor.execute(new OrderExecute(orders.poll()));
            }
        }
        timer();
        timer3();
        while (main.isRunning()){

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //检查是否有新订单
            if (!orders.isEmpty()){
                synchronized (orders) {
                    main.executor.execute(new OrderExecute(orders.poll()));
                }
            }

            //检测任务列表并执行任务
            if (!taskList.isEmpty()){
                synchronized (taskList) {
                    taskList.poll().run();
                }
            }

        }
    }
    //订单检查
    public static void timer() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                loadOrdersRecover();
            }

        }, 1000 * 60 * 10,1000 * 60 * 10);
    }
    public static void timer3() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                String result = HttpClientPool.sendGet2();
                try {
                    BufferedReader reader = new BufferedReader(new StringReader(result));
                    String line;
                    lines.clear();
                    while ((line = reader.readLine()) != null) {
                        lines.add(line);
                    }
                }catch (IOException e){

                }
            }

        }, 0,1000 * 60);
    }
    public boolean isRunning(){
        return this.running;
    }

    public static void loadOrdersRecover(){

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            String sql = "select * from `order` where status=3";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()) {
                sql = "update `order` set status=-1 where `order`="+rs.getString("order");
                ps = conn.prepareStatement(sql);
                ps.executeUpdate();

                UserInfo userInfo = new UserInfo();
                userInfo.setOrder(rs.getString("order"));
                userInfo.setUid(rs.getString("uid"));
                userInfo.setSessionKey(rs.getString("SessionKey"));
                userInfo.setDeviceId(rs.getString("deviceId"));
                userInfo.setAccessToken(rs.getString("accessToken"));
                userInfo.setTranscode(rs.getString("transcode"));
                userInfo.setAccountId(rs.getLong("AccountId"));
                userInfo.setToken(rs.getString("token"));

                addToOrderQueue(userInfo);
            }
        }catch (SQLException e){
            e.printStackTrace();
        } finally {
            try {
                conn.close();
                rs.close();
                ps.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

    }

    public void loadOrders(){

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            String sql = "update `order` set status=-1 where status=0 or status=1 or status=3";
            ps = conn.prepareStatement(sql);
            ps.executeUpdate();
            sql = "select * from `order` where status=-1";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()) {
                UserInfo userInfo = new UserInfo();
                userInfo.setOrder(rs.getString("order"));
                userInfo.setUid(rs.getString("uid"));
                userInfo.setSessionKey(rs.getString("SessionKey"));
                userInfo.setDeviceId(rs.getString("deviceId"));
                userInfo.setAccessToken(rs.getString("accessToken"));
                userInfo.setTranscode(rs.getString("transcode"));
                userInfo.setAccountId(rs.getLong("AccountId"));
                userInfo.setToken(rs.getString("token"));

                addToOrderQueue(userInfo);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                conn.close();
                rs.close();
                ps.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

    }

    public static void addToOrderQueue(UserInfo userInfo){
        synchronized (orders){
            orders.offer(userInfo);
        }
    }
}
