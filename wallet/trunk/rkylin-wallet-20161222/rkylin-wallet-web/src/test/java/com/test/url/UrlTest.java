package com.test.url;

import com.rongcapital.wallet.util.json.JsonUtil;
import com.test.util.ConfigUtil;
import com.test.util.Parm;

import java.io.*;
import java.util.*;

/**
 * Created by Administrator on 2016-11-11.
 */
public class UrlTest {

    private static String FILE_NAME = "E:\\url.txt";
    private static String HTTP_URL_LOCAL = "http://localhost:8080";
    private static String HTTP_URL_DEV = "http://121.40.68.47:8165/rkylin-wallet-web"; // 开发
    private static String HTTP_URL_TEST = "http://120.26.203.116/rkylin-wallet-web"; // 测试
    private static String HTTP_URL_UAT = "http://uatrsjf.rongcapital.com.cn:18165/rkylin-wallet-web"; // UAT
    private static String HTTP_URL_LINE = "https://op81.rongcapital.com.cn:18165/rkylin-wallet-web:"; // 线上
 
    private static String token = "&token=014e3a3eb1bb361c0262055490dab7f2";
    private static String userId = "&userId=201609021546470001";

    public static void writeUrl(String httpUrl) throws Exception {

        File file = new File(FILE_NAME);

        // if file doesnt exists, then create it
        if (file.exists()) {
            file.delete();
        }

        file.createNewFile();

        FileOutputStream fos = new FileOutputStream(file);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

        LinkedHashSet set = (LinkedHashSet) ConfigUtil.config.keySet();
        if (set.size() > 0) {
            System.out.println("共有:" + ConfigUtil.config.size() + "数据");

            for (Object obj : set) {

                String key = (String) obj;
                System.out.println(obj);
                String a = ConfigUtil.config.getProperty(key);

                String[] b = a.split("_");

                if (b.length > 1) {
                    String v_desc = b[0];
                    String v_parm = b[1];
                    String v_url = b[2];
                    for (String kk : Parm.map_parm.keySet()) {
                        v_url = v_url.replace(kk, Parm.map_parm.get(kk));
                       
                    }
                    v_url=v_url+token+userId;
                    bw.write(v_desc);
                    bw.newLine();
                    bw.write(v_parm);
                    bw.newLine();
                    bw.write(httpUrl + v_url);
                    bw.newLine();
                    bw.newLine();

                } else {
                    bw.newLine();
                    bw.newLine();
                    bw.newLine();
                    bw.newLine();
                    bw.write("#####" + b[0] + "#####");
                    bw.newLine();
                    bw.newLine();
                }

            }

            bw.close();
        }

    }

    public static void main(String[] args) throws Exception {
        writeUrl(HTTP_URL_DEV);
    }
}
