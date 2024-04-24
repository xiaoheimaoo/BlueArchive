package cn.mcfun.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
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
    public static String enCrypt(byte[] str){
        String raw4 = Tools.BytePrintAsString(str);
        String head = raw4.substring(8,16);
        System.out.println(Tools.byteArrayToInt(getXor(Tools.hexToByteArray(head))));
        String hex2 = Tools.BytePrintAsString(getXor(Tools.hexToByteArray(raw4.substring(16))));
        String hex4 = Crc32.getCrc32(4000,hex2);
        return hex4;
    }
    public static byte[] enCrypt2(String str){
        int Protocol;
        if(str.contains("{\"Protocol\":1002,")){
            Protocol = 1002;
        }else if(str.contains("{\"Protocol\":1001,")){
            Protocol = 1001;
        }else if(str.contains("{\"Protocol\":1009,")){
            Protocol = 1009;
        }else if(str.contains("{\"Protocol\":1010,")){
            Protocol = 1010;
        }else if(str.contains("{\"Protocol\":10008,")){
            Protocol = 10008;
        }else if(str.contains("{\"Protocol\":1017,")){
            Protocol = 1017;
        }else if(str.contains("{\"Protocol\":4000,")){
            Protocol = 4000;
        }else if(str.contains("{\"Protocol\":7000,")){
            Protocol = 7000;
        }else if(str.contains("{\"Protocol\":7001,")){
            Protocol = 7001;
        }else if(str.contains("{\"Protocol\":7002,")){
            Protocol = 7002;
        }else if(str.contains("{\"Protocol\":9002,")){
            Protocol = 9002;
        }else if(str.contains("{\"Protocol\":37000,")){
            Protocol = 37000;
        }else if(str.contains("\"Protocol\":37001,")){
            Protocol = 37001;
        }else if(str.contains("{\"Protocol\":50000,")){
            Protocol = 50000;
        }else {
            Protocol = 0;
        }
        byte[] raw2 = str.getBytes(StandardCharsets.UTF_8);
        byte[] raw3 = GZip(raw2);
        String raw4 = Tools.BytePrintAsString(raw3);
        raw4 = raw4.substring(raw4.length()-8)+raw4;
        //raw4 = "bb0000001f8b0800a579116600ff0dcc4b6ec2301000d0bb78cd623cb6c7712416a52a2d1252406929aca2b163930f6014a26eaadebd1ce0bd5fb19bf29c43be885202b88578bbcd71faecc31867518ac1dc3aeae9fd5ac1714a3fdf2737ac98ebaf6e7fcff9ccdaafb787aeae5e36435e8a8578cd79ece3d34947242538c40688a3230385241792f796b85546270b8ac82a6c0c326288be953169a6c25b13900ba7a54bb64df6f97ef0a313a5564a11228024327fffe314ab39bb000000";
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
