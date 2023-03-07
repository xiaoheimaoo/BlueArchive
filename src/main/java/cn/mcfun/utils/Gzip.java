package cn.mcfun.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Gzip {
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
}
