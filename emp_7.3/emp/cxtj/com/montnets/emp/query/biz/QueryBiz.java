package com.montnets.emp.query.biz;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.entity.pasroute.LfMmsAccbind;
import com.montnets.emp.entity.pasroute.LfSpDepBind;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.query.dao.GenericSystemMtTaskVoDAO;
import com.montnets.emp.report.bean.RptStaticValue;
import com.montnets.emp.security.context.ErrorLoger;

/**
 * 基础查询 Biz
 *
 * @author lianghuageng
 * @date 2019/1/04 17:31
 * 重写, 遵循编程规范;
 */
public class QueryBiz extends BaseBiz  {

    private final ErrorLoger errorLoger = new ErrorLoger();

    /**
     * 获取sp账号
     *
     * @param type    type
     * @param userIds userIds
     * @return List<Userdata>
     * @throws Exception Exception
     */
    
    public List<Userdata> getAllUserdata(int type, String userIds) throws Exception {
        //条件map
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        //排序map
        LinkedHashMap<String, String> orderMap = new LinkedHashMap<String, String>();
        try {
            conditionMap.put("uid&>", "100001");
            //只查询短信发送账号
            conditionMap.put("accouttype", "1");
            if (StringUtils.isBlank(userIds)) {
                conditionMap.put("userId", "-1");
            } else {
                conditionMap.put("userId&in", userIds);
            }
            if (1 == type) {
                conditionMap.put("userType", "0");
            } else if (2 == type) {
                conditionMap.put("userType", "1");
            }
            orderMap.put("userId", StaticValue.ASC);
            //调用查询方法
            List<Userdata> userDataList = empDao.findListBySymbolsCondition(Userdata.class, conditionMap, orderMap);
            //返回查询结果
            return userDataList;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "获取SP账号异常");
            //抛出异常
            throw e;
        }
    }

    /**
     * 通过userCode获取操作员对象
     *
     * @param userCode 操作员编码
     * @return 操作员对象
     * @throws Exception Exception
     */
    
    public String getSysUserByNameOrUserName(String userCode) throws Exception {
        StringBuffer p1Buffer = new StringBuffer();
        String p1 = "";
        try {
            List<LfSysuser> sysuserVoList = new GenericSystemMtTaskVoDAO().findSysuserByNameorUserName(userCode);
            if (null != sysuserVoList && sysuserVoList.size() > 0) {
                for (LfSysuser tmp : sysuserVoList) {
                    p1Buffer.append("'").append(tmp.getUserCode()).append("',");
                }
                p1 = p1Buffer.substring(0, p1Buffer.toString().length() - 1);
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "通过name或者username获取操作员对象异常");
            throw e;
        }
        return p1;
    }


    /**
     * 短信彩SP帐号
     *
     * @param msType   0短信 1彩信
     * @param corpCode 企业编码
     * @param corpType 0 单企业 1多企业
     * @return 短信彩SP帐号
     * @throws Exception Exception
     */
    
    public String getSpUsers(String msType, String corpCode, Integer corpType) throws Exception {
        // 短信帐号
        StringBuffer userBuffer = new StringBuffer();
        if (corpCode == null) {
            return userBuffer.toString();
        }
        //多企业
        if (1 == corpType) {
            LinkedHashMap<String, String> conditionMMap = new LinkedHashMap<String, String>();
            //不是十万的企业
            if (!"100000".equals(corpCode)) {
                // 如果是梦网则查询所有企业发送账号
                conditionMMap.put("corpCode", corpCode);
            }
            if ("0".equals(msType)) {
                //短信
                List<LfSpDepBind> lfSp = getByCondition(LfSpDepBind.class, conditionMMap, null);
                if (null != lfSp) {
                    for (int i = 0; i < lfSp.size(); i++) {
                        LfSpDepBind tmp = lfSp.get(i);
                        if (i == lfSp.size() - 1) {
                            userBuffer = userBuffer.append("'" + tmp.getSpUser().toUpperCase() + "'");
                        } else {
                            userBuffer = userBuffer.append("'" + tmp.getSpUser().toUpperCase() + "'" + ",");
                        }
                    }
                }
            } else if ("1".equals(msType)) {
                //彩信
                List<LfMmsAccbind> lfMmsSp = getByCondition(LfMmsAccbind.class, conditionMMap, null);
                if (lfMmsSp != null) {
                    for (int i = 0; i < lfMmsSp.size(); i++) {
                        LfMmsAccbind tmp = lfMmsSp.get(i);
                        if (i == lfMmsSp.size() - 1) {
                            userBuffer = userBuffer.append("'" + tmp.getMmsUser().toUpperCase() + "'");
                        } else {
                            userBuffer = userBuffer.append("'" + tmp.getMmsUser().toUpperCase() + "'" + ",");
                        }
                    }
                }
            }
        } else {
            //条件map
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("uid&>", "100001");
            String accountType = "1";
            if ("1".equals(msType)) {
                accountType = "2";
            }
            //只查询短信发送账号
            conditionMap.put("accouttype", accountType);
            conditionMap.put("userType", "0");
            //排序map
            LinkedHashMap<String, String> orderMap = new LinkedHashMap<String, String>();
            orderMap.put("userId", StaticValue.ASC);
            //单企业
            List<Userdata> userSp = empDao.findListBySymbolsCondition(Userdata.class, conditionMap, orderMap);
            if (userSp != null) {
                for (int i = 0; i < userSp.size(); i++) {
                    Userdata tmp = userSp.get(i);
                    if (i == userSp.size() - 1) {
                        userBuffer = userBuffer.append("'").append(tmp.getUserId().toUpperCase()).append("'");
                    } else {
                        userBuffer = userBuffer.append("'").append(tmp.getUserId().toUpperCase()).append("'").append(",");
                    }
                }
            }
        }
        return userBuffer.toString();
    }


    /**
     * 短彩SP账号List
     *
     * @param msType   0短信 1彩信
     * @param corpCode 企业编码
     * @param corpType 0 单企业 1多企业
     * @return
     * @throws Exception
     */
    
    public List<String> getSpUserList(String msType, String corpCode, Integer corpType) throws Exception {
        // 大写发送账号
        List<String> user_List = new ArrayList<String>();
        if (corpCode == null) {
            return user_List;
        }
        //多企业
        if (corpType == 1) {
            LinkedHashMap<String, String> conditionMMap = new LinkedHashMap<String, String>();
            //不是十万的企业
            if (!"100000".equals(corpCode)) {
                // 如果是梦网则查询所有企业发送账号
                conditionMMap.put("corpCode", corpCode);
            }
            if ("0".equals(msType)) {
                //短信
                List<LfSpDepBind> lfSp = getByCondition(LfSpDepBind.class, conditionMMap, null);
                if (null != lfSp) {
                    for (LfSpDepBind tmp : lfSp) {
                        user_List.add(tmp.getSpUser().toUpperCase());
                    }
                }
            } else if ("1".equals(msType)) {
                //彩信
                List<LfMmsAccbind> lfMmsSp = getByCondition(LfMmsAccbind.class, conditionMMap, null);
                if (null != lfMmsSp) {
                    for (LfMmsAccbind tmp : lfMmsSp) {
                        user_List.add(tmp.getMmsUser().toUpperCase());
                    }
                }
            }
        } else {
            //条件map
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("uid&>", "100001");
            String accountType = "1";
            // 当 msType == 0 时, accountType 为 1 , msType == 1 时, accountType 为 2
            if ("1".equals(msType)) {
                accountType = "2";
            }
            //只查询短信发送账号
            conditionMap.put("accouttype", accountType);
            conditionMap.put("userType", "0");
            //排序map
            LinkedHashMap<String, String> orderMap = new LinkedHashMap<String, String>();
            orderMap.put("userId", StaticValue.ASC);
            //单企业
            List<Userdata> userSp = empDao.findListBySymbolsCondition(Userdata.class, conditionMap, orderMap);
            if (null != userSp) {
                for (Userdata tmp : userSp) {
                    user_List.add(tmp.getUserId().toUpperCase());
                }
            }
        }
        return user_List;


    }


    /**
     * 获取操作员ID
     *
     * @param request
     * @return
     */
    
    public String getUserId(HttpServletRequest request) {
        try {
            Object loginSysUserObj = request.getSession(RptStaticValue.GET_SESSION_FALSE).getAttribute("loginSysuser");
            if (null != loginSysUserObj) {
                LfSysuser loginSysuser = (LfSysuser) loginSysUserObj;
                if (null != loginSysuser && null != loginSysuser.getUserId()) {
                    return loginSysuser.getUserId().toString();
                } else {
                    return "-9999";
                }
            }
        } catch (Exception e) {
            EmpExecutionContext.error("SESSION获取操作员失败");
            return "-9999";
        }
        return "-9999";
    }


    /**
     * 获取操作员GUID
     *
     * @param request request
     * @return String
     */
    
    public String getGuid(HttpServletRequest request) {
        try {
            Object loginSysUserObj = request.getSession(RptStaticValue.GET_SESSION_FALSE).getAttribute("loginSysuser");
            if (null != loginSysUserObj) {
                LfSysuser loginSysuser = (LfSysuser) loginSysUserObj;
                if (null != loginSysuser && null != loginSysuser.getGuId()) {
                    return loginSysuser.getGuId().toString();
                } else {
                    return "-9999";
                }
            }
        } catch (Exception e) {
            EmpExecutionContext.error("SESSION获取GUID失败");
            return "-9999";
        }
        return "-9999";
    }


    /**
     * 获取企业编码
     *
     * @param request request
     * @return 企业编码
     */
    
    public String getCorpCode(HttpServletRequest request) {
        try {
            Object loginSysUserObj = request.getSession(RptStaticValue.GET_SESSION_FALSE).getAttribute("loginSysuser");
            if (null != loginSysUserObj) {
                LfSysuser loginSysUser = (LfSysuser) loginSysUserObj;
                if (StringUtils.isNotBlank(loginSysUser.getCorpCode())) {
                    return loginSysUser.getCorpCode();
                } else {
                    return "-9999";
                }
            }
        } catch (Exception e) {
            EmpExecutionContext.error("SESSION获取企业编码失败");
            return "-9999";
        }
        return "-9999";
    }

    /**
     * 获取操作员对象
     *
     * @param request request
     * @return 操作员对象
     */
    
    public LfSysuser getCurrentUser(HttpServletRequest request) {
        try {
            Object loginSysUserObj = request.getSession(RptStaticValue.GET_SESSION_FALSE).getAttribute("loginSysuser");
            if (null == loginSysUserObj) {
                return null;
            }
            return (LfSysuser) loginSysUserObj;
        } catch (Exception e) {
            EmpExecutionContext.error("SESSION获取操作员对象失败。");
        }
        return null;
    }

    /**
     * 获取操作员编码
     *
     * @param request request
     * @return 操作员编码
     */
    
    public String getCurrentCorpCode(HttpServletRequest request) {
        try {
            Object loginSysUserObj = request.getSession(RptStaticValue.GET_SESSION_FALSE).getAttribute("loginSysuser");
            if (null == loginSysUserObj) {
                return null;
            }
            LfSysuser sysUser = (LfSysuser) loginSysUserObj;
            return sysUser.getCorpCode();
        } catch (Exception e) {
            EmpExecutionContext.error("SESSION获取企业编码失败。");
        }
        return null;
    }


    /**
     * 写日志
     *
     * @param request   请求对象
     * @param opModule  菜单名称
     * @param opContent 操作内容
     * @param opType    opType
     */
    
    public void setLog(HttpServletRequest request, String opModule, String opContent, String opType) {
        try {
            Object loginSysUserObj = request.getSession(RptStaticValue.GET_SESSION_FALSE).getAttribute("loginSysuser");
            if (null != loginSysUserObj) {
                LfSysuser loginSysUser = (LfSysuser) loginSysUserObj;
                EmpExecutionContext.info(opModule, loginSysUser.getCorpCode(),
                        String.valueOf(loginSysUser.getUserId()), loginSysUser.getUserName(),
                        opContent, opType);
            }
        } catch (Exception e) {
            EmpExecutionContext.error(errorLoger.getErrorLog(e, opModule + opType + opContent + "日志写入异常"));
        }
    }


}
