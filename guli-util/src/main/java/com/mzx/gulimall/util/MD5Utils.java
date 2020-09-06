package com.mzx.gulimall.util;

import java.security.MessageDigest;

/**
 * @author ZhenXinMa
 * @date 2020/8/24 22:08
 */
public class MD5Utils {

    /**
     * 16位加密算法.
     * @param inStr
     * @return
     */
    public static String digest16(String inStr) {

        return digest(inStr, 16);
    }

    /**
     * 32位加密算法.
     * @param inStr
     * @return
     */
    public static String digest(String inStr) {

        return digest(inStr, 32);
    }  // 32位加密算法

    private static String digest(String inStr, int rang) {

        MessageDigest md5 = null;
        if (inStr == null || inStr == "") {

            return "";
        }

        try {

            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {

            e.printStackTrace();
            return "";
        }

        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];
        for (int i = 0; i < charArray.length; i++) {

            byteArray[i] = (byte) charArray[i];
        }

        byte[] md5Bytes = md5.digest(byteArray);
        StringBuilder hexValue = new StringBuilder();
        for (int i = 0; i < md5Bytes.length; i++) {

            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16){

                hexValue.append("0");
            }

            hexValue.append(Integer.toHexString(val));
        }

        if (rang == 32) {

            return hexValue.toString();
        } else {

            return hexValue.toString().substring(8, 24);
        }
    }
}