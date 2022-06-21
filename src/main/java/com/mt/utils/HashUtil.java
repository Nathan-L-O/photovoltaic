package com.mt.utils;

import com.mt.exception.MengTuException;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * 字符串数字摘要工具类
 *
 * @author 过昊天
 * @version 1.0 @ 2022/6/7 11:04
 */
public class HashUtil {

    /**
     * Hash(MD5)
     *
     * @param input 输入字符串
     * @return
     */
    public static String md5(String input) {
        AssertUtil.assertStringNotBlank(input);
        char[] md5String = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

        try {
            byte[] byteInput = input.getBytes();

            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(byteInput);

            byte[] md5 = messageDigest.digest();

            int j = md5.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (byte byte0 : md5) {
                str[k++] = md5String[byte0 >>> 4 & 0xf];
                str[k++] = md5String[byte0 & 0xf];
            }

            return new String(str);

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Hash(SHA256)
     *
     * @param input 输入字符串
     * @return
     */
    public static String sha256(String input) {
        AssertUtil.assertStringNotBlank(input);
        MessageDigest messageDigest;
        String encodeString;

        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(input.getBytes(StandardCharsets.UTF_8));
            encodeString = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new MengTuException(e);
        }

        return encodeString.toUpperCase();
    }

    /**
     * 将byte转为16进制
     *
     * @param bytes 数组
     * @return
     */
    private static String byte2Hex(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        String temp;
        for (byte aByte : bytes) {
            temp = Integer.toHexString(aByte & 0xFF);
            if (temp.length() == 1) {
                stringBuilder.append("0");
            }
            stringBuilder.append(temp);
        }
        return stringBuilder.toString();
    }


    //加密
    public static String KL(String inStr) {
// String s = new String(inStr);
        char[] a = inStr.toCharArray();
        for (int i = 0; i < a.length; i++) {
            a[i] = (char) (a[i] ^ 't');
        }
        String s = new String(a);
        return s;
    }
    // 加密后解密
    public static String JM(String inStr) {
        char[] a = inStr.toCharArray();
        for (int i = 0; i < a.length; i++) {
            a[i] = (char) (a[i] ^ 't');
        }
        String k = new String(a);
        return k;
    }

    // 加密
    public static String encrypt(byte[] dataSource, String password){
        try {
            SecureRandom random = new SecureRandom();
            DESKeySpec desKeySpec = new DESKeySpec(password.getBytes());
            //创建一个密匙工厂，然后用它把DESKeySpec转换成
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = secretKeyFactory.generateSecret(desKeySpec);
            //Cipher对象实际完成加密操作
            Cipher cipher = Cipher.getInstance("DES");
            //用密匙初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, random);
            //正式执行加密操作
            return Base64.encodeBase64String(cipher.doFinal(dataSource));
        } catch (Throwable e) {
            e.printStackTrace();
        } return null;
    }

    // 解密
    public static String decrypt(String src, String password) throws Exception{
        // DES算法要求有一个可信任的随机数源
        SecureRandom random = new SecureRandom();
        // 创建一个DESKeySpec对象
        DESKeySpec desKeySpec = new DESKeySpec(password.getBytes());
        // 创建一个密匙工厂
        SecretKeyFactory keyFactory= SecretKeyFactory.getInstance("DES");
        // 将DESKeySpec对象转换成SecretKey对象
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance("DES");
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, secretKey, random);
        // 真正开始解密操作
        return new String(cipher.doFinal(Base64.decodeBase64(src)));
    }

}
