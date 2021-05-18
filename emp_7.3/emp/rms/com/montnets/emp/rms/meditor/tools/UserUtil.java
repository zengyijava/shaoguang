package com.montnets.emp.rms.meditor.tools;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfDomination;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.rms.meditor.base.BaseConfig;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用于编辑器模块
 */
public class UserUtil extends SuperBiz {
    /**
     * 企业顶级机构map，查询到的企业顶级机构放这里，key为企业编码，value为企业的顶级机构对象。
     */
    private static ConcurrentHashMap<String,LfDep> topDepMap = new ConcurrentHashMap<String,LfDep>();

    //获取用户
    public static LfSysuser getUser(HttpServletRequest request){
        LfSysuser lfSysuser = new LfSysuser();
        if (BaseConfig.DEFAULT_USER){
            lfSysuser.setUserId(2L);
            lfSysuser.setCorpCode("100000");
            lfSysuser.setUserName("张三");
        }else {
            lfSysuser = (LfSysuser) request.getSession(false) .getAttribute(StaticValue.SESSION_USER_KEY);
        }
        return lfSysuser;
    }
    public String getPermissionUserCode(LfSysuser sysUser){
        if("100000".equals(sysUser.getCorpCode())) {
            return "";
        }
        //如果是admin管理员，则默认查全部
        else if("admin".equals(sysUser.getUserName())) {
            return "";
        }
        //如果是个人权限，则只能查自己的。权限类型 1：个人权限  2：机构权限
        else if(sysUser.getPermissionType() == 1) {
            //返回当前操作员的id
            return "" + sysUser.getUserId();
        }

        //机构权限，则需要查询出当前操作员可管辖的所有操作员。
        try {
            //如果当前操作员的管辖机构即为企业最顶级的机构，则不需要拼操作员id
            boolean domIsTopDep = checkDomIsTopDep(sysUser);
            if(domIsTopDep) {
                return "";
            }

            LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
            orderbyMap.put("userId", StaticValue.ASC);
            //查询有权限看的操作员
            List<LfSysuser> userList = empDao.findListBySymbolsCondition(sysUser.getUserId(), LfSysuser.class, null, orderbyMap);
            //没找到其他操作员，那就只能看他自己的
            if(userList == null || userList.size() < 1) {
                //返回当前操作员的id值
                return "" + sysUser.getUserId();
            }
            //超过1000，则不使用in方式拼接查询
            if(userList.size() > 1000) {
                return null;
            }

            StringBuilder sbUserCode = new StringBuilder();
            for(int i = 0;i < userList.size(); i++) {
                sbUserCode.append(userList.get(i).getUserId());
                if(i < userList.size() - 1) {
                    sbUserCode.append(",");
                }
            }
            return sbUserCode.toString();
        } catch (Exception e) {
            EmpExecutionContext.error(e, "企业富信-数据查询-发送明细查询，获取有权限看的操作员编码，异常"
                    + "。userCode=" + sysUser.getUserCode()
                    + ",userId=" + sysUser.getUserId()
                    + ",userName=" + sysUser.getUserName()
                    + ",corpCode=" + sysUser.getCorpCode()
            );
            //返回当前操作员的编码
            return null;
        }
    }
    /**
     *
     * @description 检查管辖机构是否就是企业顶级机构
     * @param sysUser 操作员对象
     * @return true：管辖的机构即为企业顶级机构；false：不是顶级机构，或者没能找到，或者异常。
     */
    private boolean checkDomIsTopDep(LfSysuser sysUser) {
        //如果当前操作员的管辖机构即为企业最顶级的机构，则不需要拼操作员编码

        //获取企业最顶级机构
        LfDep topDep = getTopDep(sysUser.getCorpCode());
        //没能找到企业顶级机构
        if(topDep == null) {
            EmpExecutionContext.error("企业富信-数据查询-发送明细查询，检查管辖机构是否就是企业顶级机构，获取不到顶级机构对象。corpCode="+sysUser.getCorpCode());
            return false;
        }
        try {
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("userId", sysUser.getUserId().toString());
            LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
            orderbyMap.put("depId", StaticValue.ASC);
            List<LfDomination> domList = empDao.findListByCondition(LfDomination.class, conditionMap, orderbyMap);
            if(domList == null || domList.size() < 1) {
                return false;
            }

            for(LfDomination lfDom : domList) {
                //管辖的机构即为企业顶级机构
                if(lfDom.getDepId().equals(topDep.getDepId())) {
                    return true;
                }
            }
            return false;
        }
        catch (Exception e) {
            EmpExecutionContext.error(e, "企业富信-数据查询-发送明细查询，检查管辖机构是否就是企业顶级机构，异常。");
            return false;
        }
    }
    /**
     *
     * @description 获取企业顶级机构
     * @param corpCode 企业编码
     * @return 企业顶级机构对象，没找到或者异常则返回null
     */
    private LfDep getTopDep(String corpCode) {
        try {
            //企业顶级机构先从内存中拿
            if(topDepMap.get(corpCode) != null) {
                return topDepMap.get(corpCode);
            }

            //获取企业最顶级机构
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("corpCode", corpCode);
            //顶级机构
            conditionMap.put("depLevel", "1");
            LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
            orderbyMap.put("depId", StaticValue.ASC);
            List<LfDep> depList = empDao.findListByCondition(LfDep.class, conditionMap, orderbyMap);
            if(depList == null || depList.size() < 1) {
                EmpExecutionContext.error("企业富信-数据查询-发送明细查询，获取企业顶级机构，查询为空。corpCode="+corpCode);
                return null;
            }

            //企业顶级机构放到内存中
            topDepMap.put(corpCode, depList.get(0));

            return depList.get(0);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "企业富信-数据查询-发送明细查询，获取企业顶级机构，异常。");
            return null;
        }
    }

}

