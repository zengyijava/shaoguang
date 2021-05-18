/**
 * @description  
 * @author Administrator <foyoto@gmail.com>
 * @datetime 2013-12-28 下午03:54:54
 */
package com.montnets.emp.ottbase.constant;

import java.util.HashMap;

/**
 * 微站配置类
 * @author Administrator <foyoto@gmail.com>
 * @datetime 2013-12-28 下午03:54:54
 */

public class SiteParams
{
    protected final static HashMap<String, String[]> normalMap = new HashMap();
    static {
        normalMap.put("normal_page1", new String[]{"normal_head","normal_link","normal_list","normal_menu"});
        normalMap.put("normal_page2", new String[]{"normal_tab"});
        normalMap.put("normal_page3", new String[]{"normal_content"});
        normalMap.put("normal_page4", new String[]{"normal_scontent","normal_list"});
    }
    
    public static String[] getStypeMap(String stype)
    {
        return normalMap.get(stype);
    }
}
