package com.rongcapital.wallet.util.file;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文件流操作 Description:
 * 
 * @author: Administrator
 * @CreateDate: 2016年1月8日
 * @version: V1.0
 */
public class FileUtil {

    /**
     * 获取文件流
     * 
     * @param f
     * @return
     * @throws Exception
     */
//    public static byte[] getBytesFromFile(File f) throws Exception {
//        FileInputStream fileInputStream = null;
//        try {
//            fileInputStream = new FileInputStream(f);
//            byte[] b = new byte[fileInputStream.available()];         
//            fileInputStream.read(b);          
//            return b;
//
//        } catch (Exception e) {
//            throw e;
//        } finally {
//            try {
//                if (fileInputStream != null) {
//                    fileInputStream.close();
//                }
//            } catch (IOException e) {
//            }
//        }
//       
//    }

    /**
     * 写文件
     * 
     * @param savePath
     * @param data
     * @throws Exception
     */
    public static void wirteDataToFile(String savePath, byte[] data) throws Exception {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(savePath);
            fileOutputStream.write(data);
            fileOutputStream.flush();
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
            }
        }
    }

    /**
     * 字节转对象
     * 
     * @param bytes
     * @return
     */
    public static Object byte2Object(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try {
            ObjectInputStream inputStream;
            inputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));
            Object obj = inputStream.readObject();
            return obj;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 对象转字节
     * 
     * @param value
     * @return
     */
    public static byte[] object2Bytes(Object value) {
        if (value == null) {
            return null;
        }

        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream outputStream;
        try {
            outputStream = new ObjectOutputStream(arrayOutputStream);

            outputStream.writeObject(value);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                arrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return arrayOutputStream.toByteArray();
    }

    public static byte[] getByte(String key) {
        return key.getBytes();
    }

}
