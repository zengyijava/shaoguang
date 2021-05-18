package com.montnets.emp.gateway.biz;

import com.montnets.emp.common.biz.ParamsEncryptOrDecrypt;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.gateway.AcmdQueue;
import com.montnets.emp.entity.gateway.AgwParamValue;
import com.montnets.emp.entity.passage.XtGateQueue;
import com.montnets.emp.entity.system.LfGlobalVariable;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.gateway.dao.GatewayDAO;
import com.montnets.emp.security.context.ErrorLoger;
import com.montnets.emp.servmodule.txgl.dao.SuperTxglDAO;
import com.montnets.emp.servmodule.txgl.entity.*;
import com.montnets.emp.table.pasroute.TableGtPortUsed;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.SuperOpLog;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang.StringEscapeUtils;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 通道账户管理biz
 *
 * @author zhangmin
 * @description
 * @project p_txgl
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-10-16 下午04:53:59
 */
public class GatewayBiz extends SuperBiz {

    ErrorLoger errorLoger = new ErrorLoger();

    /**
     * 添加通道账户是保存userdata表并返回uid
     *
     * @param userdata
     * @return uid
     * @description
     * @author zhangmin
     * @datetime 2013-10-16 下午04:59:26
     */
    public Long addUserdataReturnId(Userdata userdata) {
        Long uid = 0L;
        try {
            if (StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE) {
                uid = new SuperTxglDAO().saveObjectReturnIDWithTri(userdata);
            } else {
                uid = empDao.saveObjectReturnID(userdata);
            }
        } catch (Exception e) {
            EmpExecutionContext.error(errorLoger.getErrorLog(e, "添加通道账户biz异常！"));
        }
        return uid;
    }

    /**
     * 修改通道账户
     *
     * @param request
     * @return
     * @description
     * @author zhangmin
     * @datetime 2013-12-19 下午02:16:05
     */
    public Boolean editUserdata(HttpServletRequest request) {
        Connection conn = empTransDao.getConnection();
        boolean result;

        String corpcode = "";
        String opUser = "";
        SuperOpLog spLog = new SuperOpLog();
        //日志内容
        String opContent = "";
        //修改前日志数据
        StringBuffer oldStr = new StringBuffer("");
        //修改后日志数据
        StringBuffer newStr = new StringBuffer("");

        try {
            LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
            corpcode = lfSysuser.getCorpCode();
            opUser = lfSysuser.getUserName();
            //String userid = request.getParameter("userid");
            String staffname = request.getParameter("staffname");
            //String uid = request.getParameter("uid");
            String uid;
            String keyId = request.getParameter("keyId");
            //加密对象
            ParamsEncryptOrDecrypt encryptOrDecrypt = (ParamsEncryptOrDecrypt) request.getSession(false).getAttribute("decryptObj");
            //加密对象不为空
            if (encryptOrDecrypt != null) {
                //解密
                uid = encryptOrDecrypt.decrypt(keyId);
                if (uid == null) {
                    EmpExecutionContext.error("修改通道账户，参数解密码失败，keyId:" + keyId + "，企业:" + corpcode + ",操作员:" + opUser + "，spUser:" + request.getParameter("userid"));
                    throw new Exception("修改通道账户，参数解密码失败。");
                }
            } else {
                EmpExecutionContext.error("修改通道账户，从session中获取加密对象为空！企业:" + corpcode + ",操作员:" + opUser + "，spUser:" + request.getParameter("userid"));
                throw new Exception("修改通道账户，获取加密对象失败。");
            }
            LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            //更新对象
            Userdata updateUserdata=null;

            objectMap.put("userPassword", request.getParameter("userpassword"));
            objectMap.put("staffName", staffname);
            objectMap.put("status", request.getParameter("status"));
            objectMap.put("accability", request.getParameter("accability"));

            conditionMap.put("uid", uid);
            //旧账户数据查询
            List<Userdata> oldUds = empDao.findListByCondition(Userdata.class, conditionMap, null);
            if (oldUds != null && oldUds.size() > 0) {
                Userdata oldud = oldUds.get(0);
                //获取对象
                updateUserdata=oldUds.get(0);
                if (oldud != null) {
                    //通道账号，通道账户名称，账户密码，账户状态
                    oldStr.append(oldud.getUid()).append("，").append(oldud.getStaffName()).append("，").append(oldud.getUserPassword())
                            .append("，").append(oldud.getStatus());
                }
            }

            //设置要更新的值
            updateUserdata.setUserPassword(objectMap.get("userPassword"));
            updateUserdata.setStaffName(objectMap.get("staffName"));
            updateUserdata.setStatus(Integer.parseInt(objectMap.get("status")));
            updateUserdata.setAccability(Integer.parseInt(objectMap.get("accability")));

            empTransDao.beginTransaction(conn);
            //empTransDao.update(conn, Userdata.class, objectMap, conditionMap);
            //用对象直接更新，对象直接更新可以防止SQL注入。
            empTransDao.update(conn,updateUserdata);
            newStr.append(conditionMap.get("uid")).append("，").append(objectMap.get("staffName")).append("，").append(objectMap.get("userPassword"))
                    .append("，").append(objectMap.get("status"));
            String feeUrl = request.getParameter("feeUrl");
            //技术合作商
            String spType = request.getParameter("spType");
            //账户计费类型
            String spFeeFlag = request.getParameter("spFeeFlag");
            //技术合作商为其他时,计费类型为2:后付费
            if ("10".equals(spType)) {
                spFeeFlag = "2";
            }
            //将转义的字符进行回转处理
            String spUserpassword = request.getParameter("SPACCPWD");//可能包含'''等符号
            spUserpassword = StringEscapeUtils.unescapeHtml(spUserpassword);
            if (StaticValue.getCORPTYPE() == 0) {
                objectMap.clear();
                conditionMap.clear();
                objectMap.put("spUser", request.getParameter("SPACCID"));
                objectMap.put("spUserpassword", spUserpassword);
                objectMap.put("spFeeUrl", (feeUrl == null || feeUrl.equals("")) ? " " : feeUrl);
                objectMap.put("spFeeFlag", spFeeFlag);
                conditionMap.put("sfId", request.getParameter("sfID"));
                empTransDao.update(conn, LfSpFee.class, objectMap, conditionMap);
            } else {
                //托管版更新LF_SPFEE表
                //获取后端账号绑定的所有SP账号
                List<DynaBean> userids = new GatewayDAO().getUseridByGwAccount(uid, request.getParameter("accountType"));
                if (userids != null && userids.size() > 0) {
                    String useridStr = "";
                    for (int i = 0; i < userids.size(); i++) {
                        useridStr += ((String) userids.get(i).get(TableGtPortUsed.USER_ID.toLowerCase())) + ",";
                    }
                    useridStr = useridStr.substring(0, useridStr.length() - 1);

                    objectMap.clear();
                    conditionMap.clear();
                    objectMap.put("spFeeUrl", (feeUrl == null || feeUrl.equals("")) ? " " : feeUrl);
                    conditionMap.put("spUser", useridStr);
                    conditionMap.put("accountType", request.getParameter("accountType"));
                    empTransDao.update(conn, LfSpFee.class, objectMap, conditionMap);
                }
            }
            //从请求中获取查询运营商余额备URL
            StringBuffer bakFeeUrl = new StringBuffer();
            String reqBakFeeUrl = "";
            for (int i = 1; i < 6; i++) {
                reqBakFeeUrl = request.getParameter("bakFeeUrl" + i);
                if (reqBakFeeUrl == null || reqBakFeeUrl.trim().length() < 1) {
                    EmpExecutionContext.info("修改通道账号信息，备运营商余额查看URL参数bakFeeUrl" + i + "为空，企业:" + corpcode + ",操作员:" + opUser + "，spUser:" + request.getParameter("userid"));
                    continue;
                }
                //必须以http开头
                if (reqBakFeeUrl.trim().startsWith("http")) {
                    bakFeeUrl.append(reqBakFeeUrl.trim()).append("@");
                } else {
                    EmpExecutionContext.info("修改通道账号信息，备运营商余额查看URL参数设置错误，Url:" + reqBakFeeUrl.trim() + "企业:" + corpcode + ",操作员:" + opUser + "，spUser:" + request.getParameter("userid"));
                    continue;
                }
            }
            //去掉最后一个拼接符
            if (bakFeeUrl.length() > 0) {
                bakFeeUrl.deleteCharAt(bakFeeUrl.lastIndexOf("@"));
            } else {
                bakFeeUrl.append(" ");
                EmpExecutionContext.info("修改通道账号信息，所有备运营商余额查看URL地址为空，企业:" + corpcode + ",操作员:" + opUser + "，spUser:" + request.getParameter("userid"));
            }
            //更新查询运营商余额备URL
            conditionMap.clear();
            conditionMap.put("globalKey", "BALANCEBAKURL");
            objectMap.clear();
            objectMap.put("globalStrValue", bakFeeUrl.toString());
            empTransDao.update(conn, LfGlobalVariable.class, objectMap, conditionMap);

            //含有主备信息的ip端口字符串
            String zbIP = request.getParameter("zbIP");
            if (zbIP != null && !"".equals(zbIP)) {
                if ("1".equals(request.getParameter("accountType"))) {
                    /*
                     * 将运营商ip端口  存于连路表
                     */
                    String resultstr = this.editGWGateconninfo(zbIP, request.getParameter("userid"), uid);
                    if (!"true".equals(resultstr)) {
                        opContent = "修改通道账户（通道账户账号：" + request.getParameter("userid") + "，运营商账户ID：" + request.getParameter("SPACCID") + "）运营商IP及端口传入的参数处理异常 zbIP:" + zbIP;
                        EmpExecutionContext.error("企业：" + corpcode + ",操作员：" + opUser + opContent);
                        return false;
                    }
                }
            } else {
                //保存日志
                opContent = "修改通道账户（通道账户账号：" + request.getParameter("userid") + "，运营商账户ID：" + request.getParameter("SPACCID") + "）运营商IP及端口未填";
                EmpExecutionContext.error("企业：" + corpcode + ",操作员：" + opUser + opContent);
                return false;
            }


            conditionMap.clear();
            conditionMap.put("ptAccUid", uid);
            List<AgwAccount> agwAccountList = empDao.findListByCondition(AgwAccount.class, conditionMap, null);
            if (agwAccountList != null && agwAccountList.size() > 0) {
                objectMap.clear();
                String ptIp = request.getParameter("ptIp");
                String ptPort = request.getParameter("ptPort");
                objectMap.put("ptAccName", staffname);
                objectMap.put("spAccid", request.getParameter("SPACCID"));
                objectMap.put("ptAccpwd", request.getParameter("userpassword"));
                objectMap.put("spAccPwd", spUserpassword);
                objectMap.put("serviceType", request.getParameter("serviceType"));
                objectMap.put("feeUserType", request.getParameter("feeUserType"));
                objectMap.put("ptIp", ptIp);
                objectMap.put("ptPort", ptPort);
                objectMap.put("spPort", request.getParameter("spPort"));
                objectMap.put("spIp", request.getParameter("spIp"));
                objectMap.put("speedLimit", request.getParameter("speedLimit"));
                objectMap.put("protocolCode", request.getParameter("protocolCode"));
                objectMap.put("feeUrl", (feeUrl == null || feeUrl.equals("")) ? " " : feeUrl);
                objectMap.put("spFeeFlag", spFeeFlag);
                String byIp = request.getParameter("byIp");
                String protocolParam = request.getParameter("protocolParam");
                /**
                 * 处理过滤器的漏洞问题：将<li>&#59;</li>转回为<li>;</li>
                 */
                protocolParam = StringEscapeUtils.unescapeHtml(protocolParam);
                //设置通讯协议参数
                protocolParam = setProtocolParam(protocolParam, byIp);
                if (protocolParam == null) {
                    empTransDao.rollBackTransaction(conn);
                    EmpExecutionContext.error("修改通道账号信息，通讯协议参数为空。企业:" + corpcode + ",操作员:" + opUser + "，spUser:" + request.getParameter("userid"));
                    return false;
                }
                objectMap.put("protocolParam", protocolParam);
                objectMap.put("spId", request.getParameter("spid"));
                objectMap.put("spType", spType);

                //网关集群地址数组
                String[] clusterAddrs = request.getParameterValues("clusterAddr[]");

                String ptNode = getPtNode(ptIp, ptPort, clusterAddrs);

                objectMap.put("ptNode", ptNode);
                switch (StaticValue.DBTYPE) {
                    case 1://oracle
                        objectMap.put("updateTime", " SYSDATE ");
                        break;
                    case 2://sqlserver
                        objectMap.put("updateTime", " GetDate() ");
                        break;
                    case 3:// mysql
                        objectMap.put("updateTime", "'" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "'");
                        break;
                    case 4://db2
                        objectMap.put("updateTime", " CURRENT TIMESTAMP ");
                        break;
                }

                //修改前就数据日志字符串
                AgwAccount oldAcc = agwAccountList.get(0);
                if (oldAcc != null) {
                    //，EMP网关IP地址，EMP网关IP地址端口，运营商账户ID，运营商账户密码，运营商IP地址，运营商IP端口
                    //，业务类型，SP企业代码，计费用户类型，技术合作商，通讯协议，通讯协议参数，账户计费类型，余额查看URL
                    oldStr.append("，").append(oldAcc.getPtIp()).append("，").append(oldAcc.getPtPort()).append("，").append(oldAcc.getSpAccid())
                            .append("，").append(oldAcc.getSpAccPwd()).append("，").append(oldAcc.getSpIp()).append("，").append(oldAcc.getSpPort())
                            .append("，").append(oldAcc.getServiceType()).append("，").append(oldAcc.getSpId()).append("，").append(oldAcc.getFeeUserType())
                            .append("，").append(oldAcc.getSpType()).append("，").append(oldAcc.getProtocolCode()).append("，").append(oldAcc.getProtocolParam())
                            .append("，").append(oldAcc.getSpFeeFlag()).append("，").append(oldAcc.getFeeUrl());
                }

                result = empTransDao.update(conn, AgwAccount.class, objectMap, conditionMap);
                //屏蔽代码，监控模块查询数据库获取
				/*if(result)
				{
					//同步更新监控系统用到的账号map缓存
					if("1".equals(request.getParameter("accouttype")))
					{
						String [] strs = new String[2];
						//账号名称
						strs [0]=staffname;
						//付费类型
						strs [1]=request.getParameter("spFeeFlag");
						synchronized (StaticValue.GATEACCOUTN_INFO)
						{
							StaticValue.GATEACCOUTN_INFO.put(request.getParameter("userid"), strs);
						}
					}
				}*/
                //保存日志
                //opContent="修改通道账户（通道账户账号："+request.getParameter("userid")+"）";
                opContent = "修改通道账户[通道账号，通道账户名称，账户密码，账户状态，EMP网关IP地址，EMP网关IP地址端口，运营商账户ID，运营商账户密码，运营商IP地址，运营商IP端口，业务类型，SP企业代码，计费用户类型，技术合作商，通讯协议，通讯协议参数，账户计费类型，余额查看URL]";
                newStr.append("，").append(objectMap.get("ptIp")).append("，").append(objectMap.get("ptPort")).append("，").append(objectMap.get("spAccid"))
                        .append("，").append(objectMap.get("ptAccpwd")).append("，").append(objectMap.get("spIp")).append("，").append(objectMap.get("spPort"))
                        .append("，").append(objectMap.get("serviceType")).append("，").append(objectMap.get("spId")).append("，").append(objectMap.get("feeUserType"))
                        .append("，").append(objectMap.get("spType")).append("，").append(objectMap.get("spType")).append("，").append(objectMap.get("protocolCode"))
                        .append("，").append(objectMap.get("spFeeFlag")).append("，").append(objectMap.get("feeUrl"));
                opContent = opContent + "（" + oldStr + "-->" + newStr + "）";
                String sucname = "";
                if (result) {
                    sucname = "成功";
                } else {
                    sucname = "失败";
                }
                setLog(request, "通道账户管理", opContent + sucname, StaticValue.UPDATE);
                spLog.logSuccessString(opUser, "网关配置", StaticValue.UPDATE, opContent, corpcode);
                empTransDao.commitTransaction(conn);
                return result;
            } else {
                AgwAccount account = new AgwAccount();

                account.setPtAccUid(Integer.valueOf(uid.toString()));
                account.setPtAccName(staffname);
                account.setPtAccId(request.getParameter("userid"));
                //account.setSpAccid(request.getParameter("SPACCID").toUpperCase());
                account.setSpAccid(request.getParameter("SPACCID"));
                account.setSpAccPwd(spUserpassword);
                account.setPtAccpwd(request.getParameter("userpassword"));
                account.setServiceType(request.getParameter("serviceType"));
                account.setFeeUserType(Integer.valueOf(request.getParameter("feeUserType")));
                account.setPtIp(request.getParameter("ptIp"));
                account.setPtPort(Integer.valueOf(request.getParameter("ptPort")));
                account.setSpPort(Integer.valueOf(request.getParameter("spPort")));
                account.setSpIp(request.getParameter("spIp"));
                account.setSpeedLimit(Integer.valueOf(request.getParameter("speedLimit")));
                account.setProtocolCode(Integer.valueOf(request.getParameter("protocolCode")));
                account.setSpId(request.getParameter("spid"));
                account.setSpType(Integer.valueOf(spType));
                account.setSpFeeFlag(Integer.valueOf(spFeeFlag));
                account.setFeeUrl((feeUrl == null || feeUrl.equals("")) ? " " : feeUrl);
                account.setBalance(0l);
                account.setBalanceTh(0l);
                account.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                String byIp = request.getParameter("byIp");
                String protocolParam = request.getParameter("protocolParam");
                //设置通讯协议参数
                protocolParam = setProtocolParam(protocolParam, byIp);
                if (protocolParam == null) {
                    empTransDao.rollBackTransaction(conn);
                    EmpExecutionContext.error("修改通道账号信息，通讯协议参数为空。企业:" + corpcode + ",操作员:" + opUser + "，spUser:" + request.getParameter("userid"));
                    return false;
                }
                account.setProtocolParam(protocolParam);
                //网关集群地址数组
                String[] clusterAddrs = request.getParameterValues("clusterAddr[]");
                String ptNode = getPtNode(account.getPtIp(), account.getPtPort() + "", clusterAddrs);
                account.setPtNode(ptNode);
                Integer gwNo = 100;
                LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
                orderbyMap.put("gwNo", StaticValue.DESC);
                List<AgwAccount> accountList = empDao.findListByCondition(AgwAccount.class, null, orderbyMap);
                if (accountList != null && accountList.size() > 0) {
                    gwNo = accountList.get(0).getGwNo() + 1;
                }

                account.setGwNo(gwNo);
                result = empTransDao.save(conn, account);
                //屏蔽代码，监控模块查询数据库获取
				/*if(result)
				{
					//同步更新监控系统用到的账号map缓存
					if("1".equals(request.getParameter("accouttype")))
					{
						String [] strs = new String[2];
						//账号名称
						strs [0]=staffname;
						//付费类型
						strs [1]=request.getParameter("spFeeFlag");
						synchronized (StaticValue.GATEACCOUTN_INFO)
						{
							StaticValue.GATEACCOUTN_INFO.put(request.getParameter("userid"), strs);
						}
					}
					
				}*/

                //保存日志
                opContent = "修改通道账户(AgwAccount无记录需新增)[通道账号，通道账户名称，账户密码，账户状态，EMP网关IP地址，EMP网关IP地址端口，运营商账户ID，运营商账户密码，运营商IP地址，运营商IP端口，业务类型，SP企业代码，计费用户类型，技术合作商，通讯协议，通讯协议参数，账户计费类型，余额查看URL]";
                newStr.append("，").append(objectMap.get("ptIp")).append("，").append(objectMap.get("ptPort")).append("，").append(objectMap.get("spAccid"))
                        .append("，").append(objectMap.get("ptAccpwd")).append("，").append(objectMap.get("spIp")).append("，").append(objectMap.get("spPort"))
                        .append("，").append(objectMap.get("serviceType")).append("，").append(objectMap.get("spId")).append("，").append(objectMap.get("feeUserType"))
                        .append("，").append(objectMap.get("spType")).append("，").append(objectMap.get("spType")).append("，").append(objectMap.get("protocolCode"))
                        .append("，").append(objectMap.get("spFeeFlag")).append("，").append(objectMap.get("feeUrl"));
                opContent = opContent + "（" + oldStr + "-->" + newStr + "）";
                String sucname = "";
                if (result) {
                    sucname = "成功";
                } else {
                    sucname = "失败";
                }
                setLog(request, "通道账户管理", opContent + sucname, StaticValue.UPDATE);
                spLog.logSuccessString(opUser, "网关配置", StaticValue.UPDATE, opContent, corpcode);
                empTransDao.commitTransaction(conn);
                return result;
            }

        } catch (Exception e) {
            //保存日志
            opContent = "修改通道账户（通道账户账号：" + request.getParameter("userid") + "）";
            spLog.logFailureString(opUser, "网关配置", StaticValue.UPDATE, opContent, e, corpcode);
            empTransDao.rollBackTransaction(conn);
            EmpExecutionContext.error(errorLoger.getErrorLog(e, "企业：" + corpcode + ",操作员：" + opUser + "，通道账户管理修改通道账户biz层异常！"));
            return false;
        } finally {
            empTransDao.closeConnection(conn);
        }
    }


    /**
     * 写日志
     *
     * @param request   请求对象
     * @param opModule  菜单名称
     * @param opContent 操作内容
     * @param opType
     */
    public void setLog(HttpServletRequest request, String opModule, String opContent, String opType) {
        try {
            Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
            if (loginSysuserObj != null) {
                LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;
                EmpExecutionContext.info(opModule, loginSysuser.getCorpCode(), loginSysuser.getUserId() + "", loginSysuser.getUserName(), opContent, opType);
            }
        } catch (Exception e) {
            EmpExecutionContext.error(errorLoger.getErrorLog(e, opModule + opType + opContent + "日志写入异常"));
        }
    }


    public String getXtGateQueueStrByGateID(String gateids) throws Exception {
        String addXgqStr = "";
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        conditionMap.put("id&" + StaticValue.IN, gateids);
        List<XtGateQueue> xgqs = empDao.findListBySymbolsCondition(XtGateQueue.class, conditionMap, null);
        XtGateQueue xgq = null;
        String yys = "";
        for (int i = 0; i < xgqs.size(); i++) {
            xgq = xgqs.get(i);
            if (xgq.getSpisuncm() == 0) {
                yys = "移动";
            } else if (xgq.getSpisuncm() == 1) {
                yys = "联通";
            } else if (xgq.getSpisuncm() == 21) {
                yys = "电信";
            } else {
                yys = "国外";
            }
            addXgqStr += xgqs.get(i).getSpgate() + "(" + yys + ")、";
        }

        addXgqStr = addXgqStr.length() > 0 ? addXgqStr.substring(0, addXgqStr.length() - 1) : "";
        return addXgqStr;
    }


    /**
     * 查询通道账号
     *
     * @param conditionMap 条件map
     * @param pageinfo     分页信息
     * @return
     * @throws Exception
     * @description
     * @author zhangmin
     * @datetime 2014-4-24 下午05:05:21
     */
    public List<Userdata> getUserList(LinkedHashMap<String, String> conditionMap, PageInfo pageinfo) throws Exception {

        return new GatewayDAO().getUserList(conditionMap, pageinfo);
    }

    /**
     * 后端账号查询
     *
     * @param conditionMap
     * @param pageinfo
     * @return
     * @throws Exception
     */
    public List<DynaBean> getAccountList(LinkedHashMap<String, String> conditionMap, PageInfo pageinfo) throws Exception {

        return new GatewayDAO().getAccountList(conditionMap, pageinfo);
    }

    /**
     * 设置通讯协议参数
     *
     * @param protocolParam 通讯协议参数
     * @param byIp          运营商IP地址及端口
     * @return
     * @description
     * @author chentingsheng <cts314@163.com>
     * @datetime 2014-7-17 下午04:03:37
     */
    public String setProtocolParam(String protocolParam, String byIp) {
        try {
            if (protocolParam == null || protocolParam.length() == 0) {
                protocolParam = " ";
            }
            String[] proArr = protocolParam.split(";");
            String proStr = "";
            for (int i = 0; i < proArr.length; i++) {
                if (proArr[i].indexOf("backupip=") == -1) {
                    proStr = proStr + proArr[i] + ";";
                }
            }
            if (byIp != null && byIp.length() > 0) {
                protocolParam = proStr + "backupip=" + byIp.substring(0, byIp.lastIndexOf(","));
            } else if (proStr.length() > 0) {
                protocolParam = proStr.substring(0, proStr.length() - 1);
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "设置通讯协议参数异常!");
            return null;
        }
        return protocolParam;
    }

    /**
     * 拼接主备节点信息
     *
     * @param ptIp         主节点ip
     * @param ptPort       主节点端口
     * @param clusterAddrs
     * @return
     */
    public String getPtNode(String ptIp, String ptPort, String[] clusterAddrs) {
        StringBuffer sb = new StringBuffer();
        sb.append("{0|0").append(":" + ptIp).append(":" + ptPort + "}");
        if (clusterAddrs != null) {
            for (int i = 0; i < clusterAddrs.length; i++) {
                sb.append(",");
                sb.append("{").append(i + 1).append("|0").append(":").append(clusterAddrs[i]).append("}");
            }
        }
        return "node=" + sb.toString();
    }

    /**
     * 新增网关后端账号
     *
     * @return
     */
    public boolean addGWAccount(AgwAccount agwAccount) {
        Connection connection = null;
        boolean result = false;
        try {
            connection = empTransDao.getConnection();
            empTransDao.beginTransaction(connection);
            empTransDao.save(connection, agwAccount);
            GWCluSpBind gwCluSpBind = new GWCluSpBind();
            gwCluSpBind.setGweight(0);
            gwCluSpBind.setGwno(agwAccount.getGwNo());
            gwCluSpBind.setPtaccuid(agwAccount.getPtAccUid());
            gwCluSpBind.setUpdtime(new Timestamp(System.currentTimeMillis()));
            empTransDao.save(connection, gwCluSpBind);

            //网关状态表
            GwCluStatus gwCluStatus = new GwCluStatus();
            gwCluStatus.setGweight(0);
            gwCluStatus.setGwNo(agwAccount.getGwNo());
            gwCluStatus.setGwType(3000);
            gwCluStatus.setPriGwNo(agwAccount.getGwNo());
            gwCluStatus.setRunweight(0);
            gwCluStatus.setUpdtime(new Timestamp(System.currentTimeMillis()));
            gwCluStatus.setRunstatus(0);

            empTransDao.save(connection, gwCluStatus);

            //集群主用权限决策表初始化数据
            new GatewayDAO().initGwCludecision(connection, gwCluStatus);

            empTransDao.commitTransaction(connection);
            result = true;

        } catch (Exception e) {
            empTransDao.rollBackTransaction(connection);
            EmpExecutionContext.error(e, "新增网关后端账号失败！");
        } finally {
            if (connection != null) {
                empTransDao.closeConnection(connection);
            }
        }
        return result;
    }

    public boolean editGWAccount(AgwAccount agwAccount) {
        Connection connection = null;
        boolean result = false;
        try {
            connection = empTransDao.getConnection();
            empTransDao.beginTransaction(connection);
            empTransDao.save(connection, agwAccount);
            GWCluSpBind gwCluSpBind = new GWCluSpBind();
            gwCluSpBind.setGweight(0);
            gwCluSpBind.setGwno(agwAccount.getGwNo());
            gwCluSpBind.setPtaccuid(agwAccount.getPtAccUid());
            gwCluSpBind.setUpdtime(new Timestamp(System.currentTimeMillis()));
            empTransDao.save(connection, gwCluSpBind);
            empTransDao.commitTransaction(connection);
            result = true;

        } catch (Exception e) {
            EmpExecutionContext.error(e, "新增网关后端账号失败！");
        } finally {
            if (connection != null) {
                empTransDao.closeConnection(connection);
            }
        }
        return result;
    }

    /**
     * 添加主备链路 信息
     *
     * @param zbIP
     * @param spType
     * @param userid
     * @return
     * @description
     * @author liuxiangheng <xiaokanrensheng1012@126.com>
     * @datetime 2017-4-19 下午05:39:44
     */
    public String addGWGateconninfo(String zbIP, String spType, String userid) {
        try {
            List<GwGateconninfo> gwgcinfolist = new ArrayList<GwGateconninfo>();
            String[] zbIps = zbIP.split(",");
            for (int i = 0; i < zbIps.length; i++) {
                String byipstr = zbIps[i];
                if (byipstr != null && byipstr.length() > 0 && byipstr.contains(":")) {
                    String[] coninfos = byipstr.split(":");
                    if (coninfos.length == 3) {
                        GwGateconninfo gwgcinfo = new GwGateconninfo();
                        //关联sp账号
                        gwgcinfo.setPtaccid(userid);
                        //主用/备用
                        gwgcinfo.setLinklevel(Integer.parseInt(coninfos[0]));
                        gwgcinfo.setIp(coninfos[1]);
                        gwgcinfo.setPort(Integer.parseInt(coninfos[2]));
                        if ("0".equals(spType)) {
                            //主备多链路同时连接
                            gwgcinfo.setConntype(1);
                            gwgcinfo.setKeepconn(1);
                            //gwgcinfo.setLinkcnt(3);
                        } else {
                            //单链路连接
                            gwgcinfo.setConntype(0);
                            gwgcinfo.setKeepconn(0);
                            //当合作商为“其它”时，默认只有主用是“已启用”状态
                            if (gwgcinfo.getLinklevel() == 1) {
                                //停用
                                gwgcinfo.setLinkstatus(2);
                            } else {
                                //启用
                                gwgcinfo.setLinkstatus(0);
                            }
                        }
                        gwgcinfolist.add(gwgcinfo);
                    }
                }
            }
            if (gwgcinfolist != null && gwgcinfolist.size() > 0) {
                this.empDao.save(gwgcinfolist, GwGateconninfo.class);
            }
            return "true";
        } catch (Exception e) {
            EmpExecutionContext.error(e, "新增主备链路ip端口失败");
            return "nobyip";
        }
    }


    /**
     * 修改主备链路 信息
     *
     * @param zbIp
     * @param spType
     * @param userid
     * @return
     * @description
     * @author liuxiangheng <xiaokanrensheng1012@126.com>
     * @datetime 2017-4-19 下午05:39:44
     */
    public String editGWGateconninfo(String zbIp, String userid, String uid) {
        Connection connection = null;
        try {
            List<GwGateconninfo> gwgcinfolist = new ArrayList<GwGateconninfo>();
            LinkedHashMap<String, String> conditionmap = new LinkedHashMap<String, String>();
            conditionmap.put("ptaccid", userid);
            List<GwGateconninfo> gwgates = this.empDao.findListByCondition(GwGateconninfo.class, conditionmap, null);
            // 模板配置
            GwGateconninfo mbgate = null;
            if (gwgates != null && gwgates.size() > 0) {
                mbgate = gwgates.get(0);
            }
            String[] zbips = zbIp.split(",");
            for (int i = 0; i < zbips.length; i++) {
                String byipstr = zbips[i];
                if (byipstr != null && byipstr.length() > 0 && byipstr.contains(":")) {
                    String[] coninfos = byipstr.split(":");
                    if (coninfos.length == 4) {
                        GwGateconninfo gwgcinfo = new GwGateconninfo();
                        if (mbgate != null) {
                            // 拷贝原有数据属性
                            BeanUtils.copyProperties(gwgcinfo, mbgate);
                        }
                        // 关联sp账号
                        gwgcinfo.setPtaccid(userid);
                        // 主用/备用
                        gwgcinfo.setLinklevel(Integer.parseInt(coninfos[0]));
                        gwgcinfo.setIp(coninfos[1]);
                        gwgcinfo.setPort(Integer.parseInt(coninfos[2]));
                        gwgcinfo.setLinkstatus(Integer.parseInt(coninfos[3]));
                        gwgcinfolist.add(gwgcinfo);
                    }
                }
            }
            connection = empTransDao.getConnection();
            empTransDao.beginTransaction(connection);
            empTransDao.delete(connection, GwGateconninfo.class, conditionmap);
            if (gwgcinfolist != null && gwgcinfolist.size() > 0) {
                this.empTransDao.save(connection, gwgcinfolist, GwGateconninfo.class);
            }
            conditionmap.clear();
            conditionmap.put("ptaccuid", uid);
            List<GWCluSpBind> spbs = this.empDao.findListByCondition(GWCluSpBind.class, conditionmap, null);
            if (spbs != null && spbs.size() > 0) {
                List<AcmdQueue> cmdq = new ArrayList<AcmdQueue>();
                for (GWCluSpBind gcs : spbs) {
                    AcmdQueue aq = new AcmdQueue();
                    aq.setGwNo(gcs.getGwno());
                    aq.setGwType(3000);
                    aq.setCmdType(3000);
                    cmdq.add(aq);
                }
                if (cmdq != null && cmdq.size() > 0) {
                    this.empTransDao.save(connection, cmdq, AcmdQueue.class);
                }
            }

            empTransDao.commitTransaction(connection);

            return "true";
        } catch (Exception e) {
            EmpExecutionContext.error(e, "修改主备链路ip端口失败,zbIp:" + zbIp);
            return "nobyip";
        } finally {
            if (connection != null) {
                empTransDao.closeConnection(connection);
            }
        }
    }

    /**
     * 添加备用网关
     *
     * @param uid
     * @return
     */
    public boolean addBak(String uid) {
        return new GatewayDAO().addBak(uid);
    }

    /**
     * 获取集群网关运行状态信息
     *
     * @return
     */
    public List<DynaBean> getGWCluStatus() {
        return new GatewayDAO().getGWCluStatus();
    }

    /**
     * 保存网关并且初始化网关参数
     *
     * @param gwCluStatus    网关信息
     * @param paramValueList 网关参数
     * @return
     * @description
     * @author tanglili <jack860127@126.com>
     * @datetime 2016-4-30 下午12:01:48
     */
    public boolean addGW(GwCluStatus gwCluStatus, List<AgwParamValue> paramValueList) {
        boolean isSuccess = false;
        Connection conn = null;
        try {
            //获取连接
            conn = empTransDao.getConnection();
            //开始事务
            empTransDao.beginTransaction(conn);
            //执行事务
            empTransDao.save(conn, gwCluStatus);
            empTransDao.save(conn, paramValueList, AgwParamValue.class);
            //提交事务
            empTransDao.commitTransaction(conn);
            isSuccess = true;
        } catch (Exception e) {
            isSuccess = false;
            //回滚事务
            empTransDao.rollBackTransaction(conn);
            EmpExecutionContext.error(e, "新建网关并且初始化网关参数失败！");
        } finally {
            //关闭连接
            empTransDao.closeConnection(conn);
        }
        return isSuccess;
    }
}
