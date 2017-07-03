package com.test.util;

import com.itextpdf.text.log.SysoCounter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
//import org.apache.log4j.PropertyConfigurator;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashSet;
import java.util.Properties;


/**
 *
 * Description:
 * @author: bihf
 * @CreateDate: 2016年1月11日
 * @version: V1.0
 */
public class ConfigUtil extends Thread {
    private static final Log logger = LogFactory.getLog(ConfigUtil.class.getName());

    private static final String fileName="/url.properties";
    public static OrderedProperties config = null;
    private static ConfigUtil instance = new ConfigUtil();
//	private static DBCon dbcon = null;

    public static ConfigUtil getInstance() {
        return instance;
    }

    private ConfigUtil() {
        config = new OrderedProperties();
        loadConfig();
//		start();  //TODO 上线时需取消注释，启动线程
    }

    public void loadConfig() {
        logger.debug("Reading config file.");
        synchronized (config) {
            InputStream conf = null;
            try {

                conf = getClass().getResourceAsStream(fileName);
                BufferedReader bf = new BufferedReader(new InputStreamReader(conf));
                config.load(bf);
                // PropertyConfigurator.configure(CfcaConfig);
            } catch (Exception ex2) {
                logger.fatal("Connot load configuration file. [server.properties], see the following errors...");
                logger.fatal(ex2);
            } finally {
                try {
                    conf.close();
                } catch (Exception ex1) {
                    logger.fatal(ex1);
                }
            }
        }
        logger.debug("Reading config file done.");
    }

    public void run() {
        while (!config.getProperty("ServerStop").equals("0")) {
            try {
                sleep(300000);//隔5分钟重读配置文件
            } catch (Exception ex1) {
                logger.fatal(ex1);
            }
            loadConfig();
        }
        logger.debug("config thread is exiting...");
    }


    public static String getProperty(String propName) {
        synchronized (config) {
            if (config == null)
                return null;

            return config.getProperty(propName);
        }
    }

    public static String getProperty(String propName, String defaultValue) {
        String value = getProperty(propName);
        if(value == null) {
            value = defaultValue;
        }

        return value;
    }

    public static boolean getBoolean(String property) {
        try {
            return Boolean.valueOf(getProperty(property)).booleanValue();
        } catch (Exception e) {
            logger.fatal(e);
            return false;
        }
    }

    public static float getFloat(String property) {
        try {
            return Float.valueOf(getProperty(property)).floatValue();
        } catch (Exception e) {
            logger.fatal(e);
            return 0f;
        }
    }

    public static int getInt(String property) {
        try {
            return Integer.valueOf(getProperty(property)).intValue();
        } catch (Exception e) {
            logger.fatal(e);
            return 0;
        }
    }

    public static void main(String[] args) {

        LinkedHashSet set=(LinkedHashSet) config.keySet();
        System.out.println(set.size());
    }
}
