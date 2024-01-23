package cn.mcfun.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

public class StudentName {
    private static JSONArray studentsArray = null;

    public static String getStudentName(int id) {
        if (studentsArray == null) {
            try {
                String path = "students.json";
                String content = new String(Files.readAllBytes(Paths.get(path)), "UTF-8");
                studentsArray = JSON.parseArray(content);
            } catch (IOException e) {
                e.printStackTrace();
                return String.valueOf(id); // 或者抛出异常
            }
        }

        for (int i = 0; i < studentsArray.size(); i++) {
            JSONObject student = studentsArray.getJSONObject(i);
            if (student.getInteger("Id").equals(id)) {
                return student.getString("Name");
            }
        }

        return String.valueOf(id); // 未找到ID时的默认行为
    }
}