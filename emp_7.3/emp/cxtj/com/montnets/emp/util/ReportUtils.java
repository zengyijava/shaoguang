package com.montnets.emp.util;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.sysuser.LfSysuser;
import org.apache.commons.beanutils.DynaBean;

import java.util.List;

/**
 * @Title: ReportUtils
 * @project emp_7.3
 * @author: lianghuageng
 * @Date: 2018/12/7 15:36
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @Description:
 */
public class ReportUtils {

    private ReportUtils() {
    }

    /**
     * 判断操作员是否有权限
     *
     * @param user
     * @return
     */
    public static boolean HasPermission(LfSysuser user) {
        if (null == user || 2 != user.getPermissionType()) {
            return false;
        }
        return true;
    }

    /**
     * 根据 userId 查询当前机构绑定的 SP 账号
     *
     * @param userId
     * @return
     */
    public List<DynaBean> getDepSPUserByUid(String userId) {
        List<DynaBean> dynaBeanList = null;
        try {
            String sql = "SELECT SPUSERID FROM LF_ACCOUNT_BIND WHERE DEP_CODE = (SELECT dep.DEP_ID FROM LF_SYSUSER sysuser" +
                    " INNER JOIN LF_DEP dep ON sysuser.DEP_ID = dep.DEP_ID WHERE USER_ID = " + userId.trim() + ") ";
            dynaBeanList = new DataAccessDriver().getGenericDAO().findDynaBeanBySql(sql);
        } catch (Exception e) {
            EmpExecutionContext.error(e, " --> getDepSPUserByUid() 根据guId获取操作员当前机构的SP账号信息失败");
        }
        return dynaBeanList;
    }

    
}
