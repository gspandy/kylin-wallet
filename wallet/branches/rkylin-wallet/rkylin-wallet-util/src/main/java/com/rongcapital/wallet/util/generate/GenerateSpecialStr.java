package com.rongcapital.wallet.util.generate;

import java.util.Random;
import java.util.Set;

/**
 * 生产特殊字符串,比如唯一数,随机数.. Description:
 * 
 * @author: Achilles
 * @CreateDate: 2016年3月9日
 * @version: V1.0
 */
public class GenerateSpecialStr {

    /**
     * 获取指定长度的随机数
     * 
     * @param length
     * @return
     */
    public static String getRandomStr(int length) {
        String base = "QWERTYUIOPASDFGHJKLZXCVBNMabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 生成唯一数 毫秒数+线程数+随机数 在一个循环中调用的时候使用
     * 
     * @param idSet(定义在循环体外面，new一个) 在同一个循环中使用的时候，把每次生成的id放入Set,如果本次生成的已经在set中存在，则会重新生成，直到原set中不存在
     * @return
     */
    public static String getGenerateNum(Set<String> numSet, int maxLength) {
        String num = getGenerateNum(maxLength);
        if (!numSet.contains(num)) {
            numSet.add(num);
        } else {
            getGenerateNum(numSet, maxLength);
        }
        return num;
    }

    /**
     * 生成唯一数
     * 
     * @param maxLength 随机数的长度
     * @return 毫秒数+线程数+随机数
     */
    public static String getGenerateNum(int maxLength) {
        String id = "" + System.currentTimeMillis() + Thread.currentThread().getId() + getRandomNum(maxLength);
        return id;
    }

    /**
     * 根据传入的数字获取这个值长度内的随机数
     * 
     * @param maxLength
     * @return
     */
    public static int getRandomNum(int maxLength) {
        int max = getMaxInt(maxLength);
        Random random = new Random();
        int randomNumber = random.nextInt(max) % (max - 1) + 1;
        return randomNumber;
    }

    /**
     * 根据传入的位数获取这个位数最大的整数
     * 
     * @param maxLength
     * @return
     */
    public static int getMaxInt(int maxLength) {
        int max = 0;
        for (int i = maxLength; i > 0; i--) {
            max += Math.pow(10, i - 1) * 9;
        }
        return max;
    }

    /**
     * 创建指定数量的随机字符串
     * 
     * @param numberFlag 是否是数字
     * @param length
     * @return
     */
    public static String createRandom(boolean numberFlag, int length) {
        String retStr = "";
        String strTable = numberFlag ? "1234567890" : "1234567890abcdefghijkmnpqrstuvwxyz";
        int len = strTable.length();
        boolean bDone = true;
        do {
            retStr = "";
            int count = 0;
            for (int i = 0; i < length; i++) {
                double dblR = Math.random() * len;
                int intR = (int) Math.floor(dblR);
                char c = strTable.charAt(intR);
                if (('0' <= c) && (c <= '9')) {
                    count++;
                }
                retStr += strTable.charAt(intR);
            }
            if (count >= 2) {
                bDone = false;
            }
        } while (bDone);

        return retStr;
    }
}
