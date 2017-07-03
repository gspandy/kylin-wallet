package com.rongcapital.wallet.util.prop;

import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigUtil {

    private static Logger logger = LoggerFactory.getLogger(ConfigUtil.class);

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
                conf = getClass().getResourceAsStream("/file.properties");
                config.load(conf);
            } catch (Exception ex2) {
                logger.error("Connot load configuration file. [server.properties], see the following errors...");
                logger.error("" + ex2);
            } finally {
                try {
                    if (null != conf) {
                        conf.close();
                    }
                } catch (Exception ex1) {
                    logger.error("" + ex1);
                }
            }
        }
        logger.debug("Reading config file done.");
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
            logger.error("" + e);
            return false;
        }
    }

    public static float getFloat(String property) {
        try {
            return Float.valueOf(getProperty(property)).floatValue();
        } catch (Exception e) {
            logger.info("" + e);
            return 0f;
        }
    }

    public static int getInt(String property) {
        try {
            return Integer.valueOf(getProperty(property)).intValue();
        } catch (Exception e) {
            logger.info("" + e);
            return 0;
        }
    }

    public static void main(String[] args) {
        System.out.println(config.get(""));
    }
}
