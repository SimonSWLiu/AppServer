package com.onemenu.server.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;


/**
 * 读取properties文件工具类
 */
public class PropertiesUtils {

    private static final Logger LOGGER = Logger.getLogger(PropertiesUtils.class);

    /**
     * Get classpath Object.class.getResource("/"); Object.class.getResourceAsStream("/*.*");
     * ClassLoader.getSystemResource(""); ClassLoader.getSystemResourceAsStream("*.*");
     */

    /**
     * 获取指定路径下property文件中的某个字段(没有默认值)
     * 
     * @param key 字段
     * @param classpath下 fileName 属性文件
     * @return 与key对应的value
     * @throws FileNotFoundException if property file doesn't exists
     * @throws IOException if there is some exception when load from property file
     */
    public static String getProperties(String key, String fileName) {

        InputStream is = PropertiesUtils.class.getClassLoader().getResourceAsStream(fileName);
        Properties props = new Properties();
        try {
            props.load(is);
        } catch (IOException e) {
        	LOGGER.error("", e);
        }
        return props.getProperty(key);
    }

    /**
     * 获取指定路径下property文件中的某个字段(可以设置默认值)
     * 
     * @param key 字段
     * @param value 默认值
     * @param classpath下 fileName 属性文件
     * @return 与key对应的value
     * @throws FileNotFoundException if property file doesn't exists
     * @throws IOException if there is some exception when load from property file
     */
    public static String getProperties(String key, String defaultValue, String fileName) {

        InputStream is = PropertiesUtils.class.getClassLoader().getResourceAsStream(fileName);
        Properties props = new Properties();
        try {
            props.load(is);
        } catch (IOException e) {
        	LOGGER.error("", e);
        }
        return props.getProperty(key, defaultValue);
    }
}
