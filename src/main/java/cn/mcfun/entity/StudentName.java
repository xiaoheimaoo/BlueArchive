package cn.mcfun.entity;

import com.alibaba.fastjson.JSONArray;
import java.io.*;

public class StudentName {
    static String jsonStr = null;
    public static String getStudentName(int id){
        if(jsonStr == null){
            try {
                File jsonFile = new File("students.json");
                FileReader fileReader = new FileReader(jsonFile);
                Reader reader = new InputStreamReader(new FileInputStream(jsonFile),"utf-8");
                int ch = 0;
                StringBuffer sb = new StringBuffer();
                while ((ch = reader.read()) != -1) {
                    sb.append((char) ch);
                }
                fileReader.close();
                reader.close();
                jsonStr = sb.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        JSONArray a = JSONArray.parseArray(jsonStr);
        String b = null;
        for(int i=0;i<a.size();i++){
            if(a.getJSONObject(i).getInteger("Id") == id){
                b =  a.getJSONObject(i).getString("Name");
                break;
            }
        }
        if(b == null){
            b = String.valueOf(id);
        }
        return b;
    }
}