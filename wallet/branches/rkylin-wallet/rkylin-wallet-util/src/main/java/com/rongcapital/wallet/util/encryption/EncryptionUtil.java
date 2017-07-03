package com.rongcapital.wallet.util.encryption;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.rongcapital.wallet.util.random.SaltUtil;

import java.util.Date;
import org.apache.commons.lang.time.FastDateFormat;

/**
 * 加密 Description:
 * 
 * @author: libi
 * @CreateDate: 2016年8月18日
 * @version: V1.0
 */
public class EncryptionUtil {

    public static String md5Encry(String password) {
        return Hashing.md5().newHasher().putString(password, Charsets.UTF_8).hash().toString();
    }

    public static String md5Encry(String password, String slat) {
        return Hashing.md5().newHasher().putString(password, Charsets.UTF_8).putString(slat, Charsets.UTF_8).hash()
                .toString();
    }
    
    public static String sha256Encry(String password) {

        return Hashing.sha256().newHasher().putString(password, Charsets.UTF_8).hash().toString();
    }

    public static String sha256Encry(String password, String slat) {

        return Hashing.sha256().newHasher().putString(password, Charsets.UTF_8).putString(password, Charsets.UTF_8)
                .hash().toString();
    }

    
    
    public static boolean validate(String password, String slat, String sha256Pwd) {
        String confirm = sha256Encry(password, slat);
        if (confirm.equals(sha256Pwd)) {
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        try {
           System.out.println(md5Encry(String.valueOf(System.currentTimeMillis()), SaltUtil.createSalt()));
        } catch (Exception e) {
         
        }
    }
}
