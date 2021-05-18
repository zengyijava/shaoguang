package com.montnets.emp.securectrl.vo.view;

import com.montnets.emp.table.securectrl.TableLfMacIp;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.table.sysuser.TableLfSysuser;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author wuxiaotao <819475589@qq.com>
 * @project emp
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-2-12 上午10:43:18
 * @description
 */

public class ViewLfMacIpVo {

    protected static final Map<String, String> columns = new LinkedHashMap<String, String>();

    /**
     * 加载字段
     */
    static {
        columns.put("lmiid", TableLfMacIp.LMIID);
        columns.put("depName", TableLfDep.DEP_NAME);
        columns.put("guid", TableLfSysuser.GUID);
        columns.put("name", TableLfSysuser.NAME);
        columns.put("userName", TableLfSysuser.USER_NAME);
        columns.put("ipaddr", TableLfMacIp.IP_ADDR);
        columns.put("macaddr", TableLfMacIp.MAC_ADDR);
        columns.put("creatorName", TableLfMacIp.CREATOR_NAME);
        columns.put("creatTime", TableLfMacIp.CREAT_TIME);
        columns.put("type", TableLfMacIp.TYPE);
        columns.put("dtpwd", TableLfMacIp.DT_PASS);
    }

    ;

    /**
     * 返回实体类字段与数据库字段实体类映射的map集合
     *
     * @return
     */
    public static Map<String, String> getORM() {
        return columns;
    }
}
