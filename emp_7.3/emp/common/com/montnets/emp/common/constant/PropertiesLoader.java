package com.montnets.emp.common.constant;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.montnets.emp.common.context.EmpExecutionContext;

public class PropertiesLoader{
 
	/**
	 * 读取配置文件信息
	 * @param propertiesName
	 * @return
	 */
    public Properties getProperties(String propertiesName) {
        InputStream is = getClass().getResourceAsStream("/" + propertiesName +
                ".properties");
        Properties dbProps = new Properties();
        try {
            dbProps.load(is);
            return dbProps;
        } catch (IOException ex) {
        	EmpExecutionContext.error(ex, "读取配置文件信息异常。");
            return null;
        }
    }
}
