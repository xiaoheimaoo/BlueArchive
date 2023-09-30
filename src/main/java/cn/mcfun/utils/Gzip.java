package cn.mcfun.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Random;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Gzip {
    public static String genRandomNum(){
        int  maxNum = 36;
        int i;
        int count = 0;
        char[] str = { 'A', 'B', 'C', 'D', 'E', 'F', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
        StringBuffer pwd = new StringBuffer("");
        Random r = new Random();
        while(count < 8){
            i = Math.abs(r.nextInt(maxNum));
            if (i >= 0 && i < str.length) {
                pwd.append(str[i]);
                count ++;
            }
        }
        return pwd.toString();
    }
    public static String deCrypt(String str){
        byte[] a = Base64.getDecoder().decode(str);
        byte[] bytes = new byte[a.length-4];
        System.arraycopy(a,4,bytes,0,a.length-4);
        return new String(unGZip(bytes));
    }
    public static String enCrypt(String str){
        byte[] raw2 = str.getBytes();
        byte[] raw3 = GZip(raw2);
        byte[] bytes2 = new byte[raw3.length+4];
        System.arraycopy(intToBytes(raw2.length),0,bytes2,0,4);
        System.arraycopy(raw3,0,bytes2,4,raw3.length);
        return Base64.getEncoder().encodeToString(bytes2);
    }
    public static byte[] enCrypt2(String str){
        int Protocol;
        if(str.contains("{\"Protocol\":1002,")){
            Protocol = 1002;
        }else if(str.contains("{\"Protocol\":1001,")){
            Protocol = 1001;
        }else if(str.contains("{\"Protocol\":1010,")){
            Protocol = 1010;
        }else if(str.contains("{\"Protocol\":10008,")){
            Protocol = 10008;
        }else if(str.contains("{\"Protocol\":1017,")){
            Protocol = 1017;
        }else if(str.contains("{\"Protocol\":7000,")){
            Protocol = 7000;
        }else if(str.contains("{\"Protocol\":7001,")){
            Protocol = 7001;
        }else if(str.contains("{\"Protocol\":7002,")){
            Protocol = 7002;
        }else if(str.contains("{\"Protocol\":9002,")){
            Protocol = 9002;
        }else if(str.contains("{\"Protocol\":50000,")){
            Protocol = 50000;
        }else {
            Protocol = 0;
        }
        byte[] raw2 = str.getBytes();
        byte[] raw3 = GZip(raw2);
        String raw4 = Tools.BytePrintAsString(raw3);
        raw4 = raw4.substring(raw4.length()-8)+raw4;
        String hex2 = Tools.BytePrintAsString(getXor(Tools.hexToByteArray(raw4)));
        String hex4 = Crc32.getCrc32(Protocol,hex2) + hex2;
        return Tools.hexToByteArray(hex4);
    }
    public static byte[] unGZip(byte[] data) {
        if (data == null || data.length == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        try {
            GZIPInputStream ungzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n;
            while ((n = ungzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }
    public static byte[] GZip(byte[] str) {
        if (str == null || str.length == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip;
        try {
            gzip = new GZIPOutputStream(out);
            gzip.write(str);
            gzip.close();
        } catch ( Exception e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }
    public static byte[] intToBytes(int value ){
        byte[] src = new byte[4];
        src[0] =  (byte) (value & 0xFF);
        src[1] =  (byte) ((value>>8) & 0xFF);
        src[2] =  (byte) ((value>>16) & 0xFF);
        src[3] =  (byte) ((value>>24) & 0xFF);
        return src;
    }
    public static byte[] getXor(byte[] datas){

        for (int i = 0; i <datas.length; i++) {
            datas[i] ^= 0xD9;
        }

        return datas;
    }
}
