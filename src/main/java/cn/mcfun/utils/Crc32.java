package cn.mcfun.utils;

import cn.mcfun.protocol.*;

public class Crc32 {

    public static int genCrc32(byte[] data,int offset, int length){
        byte i;
        //int crc = 0xffffffff;  // Initial value
        int crc = 0;  // Initial value
        length += offset;
        for(int j=offset;j<length;j++) {
            crc ^= data[j] << 24;
            for (i = 0; i < 8; ++i)
            {
                if ( (crc & 0x80000000) != 0)
                    crc = (crc << 1) ^ 0x04C11DB7;
                else
                    crc <<= 1;
            }
        }
        return crc;
    }

    public static String getCrc32(int Protocol,String hex) {
        byte b1[] = Tools.hexToByteArray(hex);
        int c1 = genCrc32(b1,0,b1.length);
        String head = Tools.BytePrintAsString(Tools.intToByteArray(c1));
        int p;
        if(Protocol == 1000){
            p = Protocol_1000.switch_1(c1);
        }else if(Protocol == 1001){
            p = Protocol_1001.switch_1(c1);
        }else if(Protocol == 1002){
            p = Protocol_1002.switch_1(c1);
        }else if(Protocol == 1005){
            p = Protocol_1005.switch_1(c1);
        }else if(Protocol == 1006){
            p = Protocol_1006.switch_1(c1);
        }else if(Protocol == 1009){
            p = Protocol_1009.switch_1(c1);
        }else if(Protocol == 1010){
            p = Protocol_1010.switch_1(c1);
        }else if(Protocol == 1017){
            p = Protocol_1017.switch_1(c1);
        }else if(Protocol == 5000){
            p = Protocol_5000.switch_1(c1);
        }else if(Protocol == 5002){
            p = Protocol_5002.switch_1(c1);
        }else if(Protocol == 6000){
            p = Protocol_6000.switch_1(c1);
        }else if(Protocol == 6001){
            p = Protocol_6001.switch_1(c1);
        }else if(Protocol == 6003){
            p = Protocol_6003.switch_1(c1);
        }else if(Protocol == 6005){
            p = Protocol_6005.switch_1(c1);
        }else if(Protocol == 6006){
            p = Protocol_6006.switch_1(c1);
        }else if(Protocol == 6007){
            p = Protocol_6007.switch_1(c1);
        }else if(Protocol == 6008){
            p = Protocol_6008.switch_1(c1);
        }else if(Protocol == 6015){
            p = Protocol_6015.switch_1(c1);
        }else if(Protocol == 7000){
            p = Protocol_7000.switch_1(c1);
        }else if(Protocol == 7001){
            p = Protocol_7001.switch_1(c1);
        }else if(Protocol == 7002){
            p = Protocol_7002.switch_1(c1);
        }else if(Protocol == 8000){
            p = Protocol_8000.switch_1(c1);
        }else if(Protocol == 8002){
            p = Protocol_8002.switch_1(c1);
        }else if(Protocol == 9002){
            p = Protocol_9002.switch_1(c1);
        }else if(Protocol == 10008){
            p = Protocol_10008.switch_1(c1);
        }else if(Protocol == 10011){
            p = Protocol_10011.switch_1(c1);
        }else if(Protocol == 10013){
            p = Protocol_10013.switch_1(c1);
        }else if(Protocol == 15002){
            p = Protocol_15002.switch_1(c1);
        }else if(Protocol == 19003){
            p = Protocol_19003.switch_1(c1);
        }else if(Protocol == 25003){
            p = Protocol_25003.switch_1(c1);
        }else if(Protocol == 28019){
            p = Protocol_28019.switch_1(c1);
        }else if(Protocol == 29002){
            p = Protocol_29002.switch_1(c1);
        }else if(Protocol == 36001){
            p = Protocol_36001.switch_1(c1);
        }else if(Protocol == 37000){
            p = Protocol_37000.switch_1(c1);
        }else if(Protocol == 37001){
            p = Protocol_37001.switch_1(c1);
        }else if(Protocol == 43010){
            p = Protocol_43010.switch_1(c1);
        }else if(Protocol == 50000){
            p = Protocol_50000.switch_1(c1);
        }else{
            p = 0;
        }
        String head2 = Tools.BytePrintAsString(Tools.intToByteArray(p));
        return  head+head2;
    }
}
