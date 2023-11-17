package cn.mcfun.utils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashMatching {
    private static final String B32_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";

    public static String base32Encode(byte[] bs) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        int b = 0;
        int bLen = 0;

        while (i < bs.length) {
            b = (b << 8) | (bs[i++] & 0xFF);
            bLen += 8;

            while (bLen >= 5) {
                sb.append(B32_ALPHABET.charAt((b >> (bLen - 5)) & 31));
                bLen -= 5;
            }
        }

        if (bLen > 0) {
            sb.append(B32_ALPHABET.charAt((b << (5 - bLen)) & 31));
        }

        return sb.toString();
    }
    public static String solve(String Hint,String Question) {
        BigInteger startNumber = new BigInteger(Hint);
        String targetHash = Question;
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        long iterationCount = 0;
        while (true) {
            // Convert the number to a byte array using UTF-16LE encoding
            byte[] numberBytes = startNumber.toString().getBytes(StandardCharsets.UTF_16LE);

            // Calculate MD5 hash
            byte[] md5Hash = md.digest(numberBytes);

            // Encode the hash in Base32
            String base32Result = base32Encode(md5Hash);
            // Check if the hash matches the target
            if (base32Result.equals(targetHash)) {
                break;
            }

            // Increment the number and the iteration count
            startNumber = startNumber.add(BigInteger.ONE);
            iterationCount++;
        }
        return String.valueOf(startNumber);
    }
}