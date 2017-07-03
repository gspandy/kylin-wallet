package com.rongcapital.wallet.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
//import org.apache.log4j.PropertyConfigurator;

import java.io.InputStream;
import java.util.Properties;

/**
 * 
 * Description:
 * 
 * @author: libi
 * @CreateDate: 2016年8月23日
 * @version: V1.0
 */
public class ConfigUtil extends Thread {
    private static final Log logger = LogFactory.getLog(ConfigUtil.class.getName());

    private static final String fileName = "/conf/rop.properties";
    private static Properties config = null;
    private static ConfigUtil instance = new ConfigUtil();

    public static ConfigUtil getInstance() {
        return instance;
    }

    private ConfigUtil() {
        config = new Properties();
        loadConfig();
       
    }

    public void loadConfig() {
        logger.debug("Reading config file.");
        synchronized (config) {
            InputStream conf = null;
            try {
                conf = getClass().getResourceAsStream(fileName);
                config.load(conf);
                // PropertyConfigurator.configure(ConfigUtil);
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
                sleep(300000);// 隔5分钟重读配置文件
            } catch (Exception ex1) {
                logger.fatal(ex1);
            }
            loadConfig();
        }
        logger.debug("Config thread is exiting...");
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
        if (value == null) {
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
        System.out.println(config.get("APP_KEY"));
    }
}
