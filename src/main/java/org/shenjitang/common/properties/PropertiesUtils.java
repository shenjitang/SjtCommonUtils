package org.shenjitang.common.properties;

import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesUtils {
    private String configPath = null;
    private Properties props = null;

    /**
     * 加载文件对象
     *
     * @param PROPERTIES 文件名
     * @throws IOException io
     */
    public PropertiesUtils(String PROPERTIES) throws IOException {
        InputStream in = PropertiesUtils.class.getClassLoader().getResourceAsStream(PROPERTIES);
        props = new Properties();
        props.load(in);
        in.close();
    }

    /**
     * 读取指定参数
     *
     * @param key 参数名
     * @return 参数值
     * @throws IOException io
     */
    public String readValue(String key) throws IOException {
        return props.getProperty(key);
    }

    /**
     * 读取所有参数
     *
     * @return map
     * @throws FileNotFoundException file
     * @throws IOException           io
     */
    public Map<String, String> readAllProperties() throws FileNotFoundException, IOException {
        Map<String, String> map = new HashMap<>();
        Enumeration en = props.propertyNames();
        while (en.hasMoreElements()) {
            String key = (String) en.nextElement();
            String Property = props.getProperty(key);
            map.put(key, Property);
        }
        return map;
    }

    /**
     * 给参数赋值
     *
     * @param key   参数名
     * @param value 参数值
     * @throws IOException io
     */
    public void setValue(String key, String value) throws IOException {
        Properties prop = new Properties();
        InputStream fis = new FileInputStream(this.configPath);
        prop.load(fis);
        OutputStream fos = new FileOutputStream(this.configPath);
        prop.setProperty(key, value);
        prop.store(fos, "last update");
        fis.close();
        fos.close();
    }
}
