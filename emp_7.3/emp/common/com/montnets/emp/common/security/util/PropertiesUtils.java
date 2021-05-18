package com.montnets.emp.common.security.util;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.security.constant.SecurityConst;
import com.montnets.emp.datasource.servlet.OrderedProperties;
import com.montnets.emp.util.TxtFileUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * @author liangHuaGeng
 * @Title: PropertieUtils
 * @ProjectName emp_7.3
 * @Description: TODO
 * @date 2019/1/1611:13
 */
public class PropertiesUtils {

    private PropertiesUtils() {
    }

    static public Properties getProperties(String fileName) {
        // 属性集合对象
        OrderedProperties prop = new OrderedProperties();
        TxtFileUtil fileUtil = new TxtFileUtil();
        String basePath13 = fileUtil.getWebRoot();
        FileInputStream fis;
        try {
            fis = new FileInputStream(basePath13 + SecurityConst.FILE_BASE_URL + fileName);
            try {
                prop.load(fis);
            } catch (IOException e) {
                EmpExecutionContext.error(e, "PropertiesUtils.getProperties() 加载配置文件出现异常！");
            }// 将属性文件流装载到Properties对象中
            try {
                fis.close();
            } catch (IOException e) {
                EmpExecutionContext.error(e, "PropertiesUtils.getProperties() 文件加载完成异常！");
            }// 关闭流
        } catch (FileNotFoundException e) {
            EmpExecutionContext.error(e, "PropertiesUtils.getProperties() 文件不存在！");
        }// 属性文件输入流
        return prop;
    }
}
