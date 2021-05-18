package com.montnets.emp.netnews.biz;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.biz.GlobalVariableBiz;
import com.montnets.emp.common.biz.ReviewBiz;
import com.montnets.emp.common.biz.ReviewRemindBiz;
import com.montnets.emp.common.biz.SmsBiz;
import com.montnets.emp.common.biz.SubnoManagerBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.EMPException;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.email.Aes;
import com.montnets.emp.email.MailInfo;
import com.montnets.emp.email.SendMail;
import com.montnets.emp.entity.approveflow.LfExamineSms;
import com.montnets.emp.entity.approveflow.LfFlowRecord;
import com.montnets.emp.entity.corp.LfCorpConf;
import com.montnets.emp.entity.pasroute.GtPortUsed;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.sysuser.LfFlow;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.netnews.common.FileJsp;
import com.montnets.emp.netnews.entity.LfWXBASEINFO;
import com.montnets.emp.netnews.entity.LfWXDataBind;
import com.montnets.emp.netnews.entity.LfWXPAGE;
import com.montnets.emp.security.wgmsgconfig.WgMsgConfigBiz;
import com.montnets.emp.util.PhoneUtil;
import com.montnets.emp.util.StringUtils;
import com.montnets.emp.util.SuperOpLog;

public class WXUeditorBiz extends SuperBiz{
    /**
     * 创建网讯
     *
     * @param base
     * @param page
     * @param pageList
     * @return
     * @throws Exception
     */
    
    public Long SaveUdeitor(LfWXBASEINFO base, LfWXPAGE page,
                            List<LfWXPAGE> pageList) throws Exception {

        /**
         * wx_name in varchar2, 网讯名称 ， creatId in NUMBER, 创建用户ID parenetid in
         * NUMBER , 父结点 ， netid out number 输出 ：网讯id
         */
        Connection conn = empTransDao.getConnection();
        boolean boo = false;
        boolean res = false;
        Long baseId = null;
        LfFlow flow = null;
        try {
            empTransDao.beginTransaction(conn);
            // 内容是否存在子页面链接
            if (page.getCONTENT().indexOf("javascript:mylink") > -1
                    && page.getCONTENT().indexOf("#link#") > -1) {
                boo = true;
            }
            baseId = empTransDao.saveObjectReturnID(conn, base);
            //addObjReturnId(base);
            base.setNETID(baseId);
            base.setID(baseId);
            if (base.getSTATUS() == 1) {
                //如果定稿，判断是否需要审核
                ReviewBiz rev = new ReviewBiz();
                flow = rev.checkUserFlow(base.getCREATID(), base.getCORPCODE(), "6");
                if (flow != null) {
                    res = rev.addFlowRecords(conn, baseId, base.getNAME(),
                            base.getCREATDATE(), 6, flow, base.getCREATID(), "1");
                    //addFlowRecords(conn, baseId, 6, flow);
                } else {
                    //无需审核
                    base.setSTATUS(4);
                }
            }
            empTransDao.update(conn, base);
            //updateObj(base);
            page.setCREATID(base.getCREATID());
            page.setNETID(baseId);
            //父节点保存

            String content = page.getCONTENT();
            page.setCONTENT("");//不存数据库
            Long pageid = empTransDao.saveObjectReturnID(conn, page);
//			content = new String(content.getBytes("iso8859-1"),"UTF-8");
            FileJsp.saveContent(pageid + "", content);//用于显示预览的，保存JSP页面
            page.setCONTENT(content);//此处存回去，是为了后面不影响原来的逻辑处理，

//			saveContent1(pageid);
            //addObjReturnId(page);

            // 内容是否存在子页面链接
            if (pageList != null && pageList.size() > 0) {
                LfWXPAGE pageT = null;
                for (int i = 0; i < pageList.size(); i++) {
                    pageT = pageList.get(i);
                    if (pageT.getCONTENT().indexOf("javascript:mylink") > -1 && pageT.getCONTENT().indexOf("#link#") > -1) {
                        boo = true;
                    }
                }
            }
            Map<String, Long> map = null;
            List<String> temp = null;
            if (boo) {
                map = new HashMap<String, Long>();
                temp = new ArrayList<String>();
                map.put("#link#" + page.getLink() + "#link#", pageid);
                temp.add("#link#" + page.getLink() + "#link#");
            }

            if (pageList != null && pageList.size() > 0) {
                Long id = 0L;
                LfWXPAGE pageTemp = null;
                for (int i = 0; i < pageList.size(); i++) {
                    pageTemp = (LfWXPAGE) pageList.get(i);
                    pageTemp.setNETID(baseId);// 关联网讯编号
                    pageTemp.setPARENTID(pageid); // 页面父节点
                    pageTemp.setCREATID(base.getCREATID());// 创建用户ID
                    String contentTemp = pageTemp.getCONTENT();

                    pageTemp.setCONTENT("");//不存数据库
                    id = empTransDao.saveObjectReturnID(conn, pageTemp); // addObjReturnId(pageTemp);
//					contentTemp = new String(contentTemp.getBytes("iso8859-1"),"UTF-8");
                    FileJsp.saveContent(id + "", contentTemp);//用于显示预览的，保存JSP页面
                    pageTemp.setCONTENT(contentTemp);//此处存回去，是为了后面不影响原来的逻辑处理，

                    if (boo) {
                        map.put("#link#" + pageTemp.getLink() + "#link#", id);
                        temp.add("#link#" + pageTemp.getLink() + "#link#");
                    }
                }
            }

            // 更新数据
            if (boo) {
                // 父结点用
                List<LfWXPAGE> templ = new ArrayList<LfWXPAGE>();
                for (int i = 0; i < temp.size(); i++) {
                    if (page.getCONTENT().indexOf(temp.get(i).toString()) > -1) {
                        page.setCONTENT(page.getCONTENT().replaceAll(temp.get(i).toString(), map.get(temp.get(i).toString()).toString()));
                        FileJsp.saveContent(page.getID() + "", page.getCONTENT());//用于显示预览的，保存JSP页面
                        page.setID(pageid);
                    }
                }
                LfWXPAGE page2 = null;
                for (int j = 0; j < pageList.size(); j++) {
                    page2 = pageList.get(j);
                    page2.setID(map.get("#link#" + page2.getLink() + "#link#"));
                    for (int i = 0; i < temp.size(); i++) {
                        if (page2.getCONTENT().indexOf(temp.get(i).toString()) > -1) {
                            page2.setCONTENT(page2.getCONTENT().replaceAll(temp.get(i).toString(), map.get(temp.get(i).toString()).toString()));
                            FileJsp.saveContent(page2.getID() + "", page2.getCONTENT());//用于显示预览的，保存JSP页面
                        }
                    }
                    templ.add(page2);
                }
                templ.add(page);

                getupdateByUeditor(conn, base, page, templ);
            }
            empTransDao.commitTransaction(conn);
        } catch (Exception e) {
            empTransDao.rollBackTransaction(conn);
            EmpExecutionContext.error(e, "创建网讯模板失败");
            throw e;
        } finally {
            empTransDao.closeConnection(conn);
            if (res) {
                // 获取配置文件中的信息
                String isre = SystemGlobals.getSysParam("isRemind");
                if (flow != null && "0".equals(isre) && baseId != null) {
                    LfFlowRecord record = new LfFlowRecord();
                    record.setInfoType(6);
                    record.setMtId(baseId);
                    record.setProUserCode(base.getCREATID());
                    record.setRLevelAmount(flow.getRLevelAmount());
                    record.setRLevel(0);
                    flowReviewRemind(record);
                    sendMail(record);
                    //如果是审核通过
//					base.setSTATUS(2);
//					empTransDao.update(conn, base);
                }
            }
        }
        return baseId;
    }


    /**
     * 远程上传文件
     *
     * @param pageid
     */
    public void saveContent1(Long pageid) {
        String filename = "wx_" + pageid + "_content.jsp";
        CommonBiz com = new CommonBiz();
        if (StaticValue.getISCLUSTER() == 1) {
            String result = com.uploadOneFileWhitZip(StaticValue.WX_PAGE + "/" + filename, "1");
            if ("error".equals(result)) {
                EmpExecutionContext.error("由于采用集群，远程上传文件失败！");
            }
        }
    }


    /**
     * 页面提交update网讯信息
     *
     * @param conn
     * @param baseInfo
     * @param pageTemp
     * @param list
     * @return
     * @throws Exception
     */
    
    public int getupdateByUeditor(Connection conn, LfWXBASEINFO baseInfo, LfWXPAGE pageTemp,
                                  List<LfWXPAGE> list) throws Exception {
        boolean boo = false;
        // 内容是否存在子页面链接
        Map<String, Long> map = null;
        List<String> temp = null;
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                LfWXPAGE page = list.get(i);
                if (page.getCONTENT().indexOf("javascript:mylink") > -1
                        && page.getCONTENT().indexOf("#link#") > -1) {
                    boo = true;
                    map = new HashMap<String, Long>();
                    temp = new ArrayList<String>();
                    break;
                }
            }
        }
        try {
            //empTransDao.update(conn, baseInfo);
            //updateObj(baseInfo);
            //Long netid = baseInfo.getNETID();
            if (list != null && list.size() > 0) {

                Long iws = 0L;
                for (int i = 0; i < list.size(); i++) {
                    LfWXPAGE page = list.get(i);
                    String content = page.getCONTENT();
                    page.setCONTENT("");//不存数据库

                    if (page.getID() == -1) {
                        page.setNETID(baseInfo.getNETID());
                        page.setPARENTID(baseInfo.getID());
                        page.setCREATID(baseInfo.getCREATID());
                        iws = empTransDao.saveObjectReturnID(conn, page);  //addObjReturnId(page);
                    } else {
                        page.setNETID(baseInfo.getNETID());
                        page.setMODIFYID(baseInfo.getMODIFYID());
                        empTransDao.update(conn, page);
                        //updateObj(page);
                        iws = page.getID();
                    }

                    if (boo) {
                        page.setCONTENT(content);

                        map.put("#link#" + page.getLink() + "#link#", iws);
                        temp.add("#link#" + page.getLink() + "#link#");
                    } else {
                        FileJsp.saveContent(iws + "", content);
                    }
                }
            }
            if (boo) {
                List<LfWXPAGE> temp1 = new ArrayList<LfWXPAGE>();
                for (int j = 0; j < list.size(); j++) {
                    LfWXPAGE page = list.get(j);
                    if (page.getID() == -1) {
                        page.setID(map.get("#link#" + page.getLink() + "#link#"));
                    }
                    for (int i = 0; i < temp.size(); i++) {
                        if (page.getCONTENT().indexOf(temp.get(i).toString()) > -1) {
                            page
                                    .setCONTENT(page.getCONTENT().replaceAll(
                                            temp.get(i).toString(),
                                            map.get(temp.get(i).toString())
                                                    .toString()));
                            FileJsp.saveContent(page.getID() + "", page.getCONTENT());//用于显示预览的，保存JSP页面
                        }
                    }
                    temp1.add(page);
                }

                getupdateByUeditor(conn, baseInfo, pageTemp, temp1);
            }
            return 1;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "页面提交网讯信息处理异常!");
            return -1;
        }
    }

    /**
     * 处理邮件发送审批下发 (网讯单独做处理，模板不是存在同一个位置)
     *
     * @param lfFlowRecord 审核实时记录对象
     */
    public void sendMail(LfFlowRecord lfFlowRecord) {

        LfSysuser AdminUser = null;
        LfSysuser sendUser = null;
        StringBuffer buffer = new StringBuffer();
        LfWXBASEINFO baseinfo = null;
        String msg = "";
        try {
            if (lfFlowRecord != null) {
                msg = getType(lfFlowRecord.getInfoType());
            }
            if (lfFlowRecord.getFId() != null) {
                buffer.append(" 邮件发送中审核流程ID ：").append(lfFlowRecord.getFId()).append(";");
            } else {
                buffer.append(" 邮件发送中审核流程ID ：无;");
            }
            //短信提醒内容
            String content = "";
            //创建的任务
            LfMttask mt = null;

            //根据类型判断是否 1短信发送2 彩信发送  3短信模板4彩信摸板
            if (lfFlowRecord.getInfoType() == 5) {
                mt = new CommonBiz().getLfMttaskbyTaskId(lfFlowRecord.getMtId());
                if (mt == null) {
                    EmpExecutionContext.error("任务ID为：" + lfFlowRecord.getMtId() + "邮件提醒下发失败，获取任务失败！");
                    return;
                }
            } else if (lfFlowRecord.getInfoType() == 6) {
                baseinfo = empDao.findObjectByID(LfWXBASEINFO.class, lfFlowRecord.getMtId());
            }
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            //任务创建人
            sendUser = empDao.findObjectByID(LfSysuser.class, lfFlowRecord.getProUserCode());
            conditionMap.put("userName", "admin");//admin用户
            conditionMap.put("corpCode", sendUser.getCorpCode());//当前登录用户的企业编码
            List<LfSysuser> AdminLfSysuerList = empDao.findListByCondition(LfSysuser.class, conditionMap, null);
            if (AdminLfSysuerList != null && AdminLfSysuerList.size() > 0) {
                //系统admin
                AdminUser = AdminLfSysuerList.get(0);
            } else {
                //当前审批人
                //AdminUser = empDao.findObjectByID(LfSysuser.class,lfFlowRecord.getUserCode());
                String str = buffer.toString() + " 获取该企业的admin失败。";
                dailyFlowRecord(AdminUser, str, "fail");
                return;
            }

            if (sendUser != null) {
                if (sendUser.getEMail() == null || "".equals(sendUser.getEMail())) {
                    //发送人的邮件为空 则返回
                    String str = buffer.toString() + " 发送人的邮件为空。";
                    dailyFlowRecord(AdminUser, str, "fail");
                    return;
                }
            } else {
                //当前审批人
                //AdminUser = empDao.findObjectByID(LfSysuser.class,lfFlowRecord.getUserCode());
                String str = buffer.toString() + " 获得发送人失败。";
                dailyFlowRecord(AdminUser, str, "fail");
                return;
            }
            //下一级审批记录
            List<LfFlowRecord> nextFlowRecordList = null;

            //记录下一批审批人的手机号码
            String[] phoneArr = null;

            int rlevel = lfFlowRecord.getRLevel() + 1;
            //如果是逐级审核
            if (lfFlowRecord.getRType() != null && lfFlowRecord.getRType() == 5) {
                rlevel = 1;
                boolean isLastLevel = new ReviewBiz().checkZhujiIsOver(lfFlowRecord.getPreRv().intValue(), lfFlowRecord.getProUserCode());
                if (isLastLevel) {
                    rlevel = 2;
                }
            }
            buffer.append("下一级审批级别：").append(rlevel).append(";");
            if (lfFlowRecord.getRType() != null) {
                buffer.append("审批类型：").append(lfFlowRecord.getRType()).append(";");
            } else {
                buffer.append("审批类型：未知;");
            }
            //下一级审批流程
            conditionMap.clear();

            conditionMap.put("mtId", String.valueOf(lfFlowRecord.getMtId()));
            conditionMap.put("infoType", String.valueOf(lfFlowRecord.getInfoType()));


            conditionMap.put("isComplete", "2");
            conditionMap.put("RLevel", String.valueOf(rlevel));
            nextFlowRecordList = empDao.findListByCondition(LfFlowRecord.class, conditionMap, null);
            //用户code
            StringBuffer idStr = new StringBuffer();
            //email
            StringBuffer emailStr = new StringBuffer();
            if (nextFlowRecordList != null && nextFlowRecordList.size() > 0) {
                for (LfFlowRecord record : nextFlowRecordList) {
                    idStr.append(record.getUserCode()).append(",");
//    				frIdMap.put(record.getUserCode(), record.getFrId());
                }
                String str = idStr.toString().substring(0, idStr.toString().length() - 1);
                conditionMap.clear();
                conditionMap.put("userId&in", str);
                conditionMap.put("corpCode", sendUser.getCorpCode());
                List<LfSysuser> sysuserList = empDao.findListBySymbolsCondition(LfSysuser.class, conditionMap, null);
                ;
                if (sysuserList != null && sysuserList.size() > 0) {
                    phoneArr = new String[sysuserList.size()];
                    for (int i = 0; i < sysuserList.size(); i++) {
                        LfSysuser sysuser = sysuserList.get(i);
                        phoneArr[i] = sysuser.getMobile();
                        //此处判断下是否需要有审核提醒【根据操作员中是否勾选审核提醒处理】 有四位
                        String cf = sysuser.getControlFlag();
                        if (cf != null && cf.length() > 1) {
                            String bit = cf.substring(1, 2);//判断是否勾选，如果没勾选则不发送信息
                            if ("0".equals(bit)) {
                                continue;
                            }
                        }
                        emailStr.append(sysuser.getEMail()).append(",");
//						userIdMap.put(sysuser.getEMail(), frId);
                    }
                }
            } else {
                return;
            }
            content = getRemindEmailContent(baseinfo, sendUser);
            if (content == null || "".equals(content)) {
                EmpExecutionContext.error("邮件审批提醒内容为空！");
                String str = buffer.toString() + " 邮件审批提醒内容为空。";
                dailyFlowRecord(AdminUser, str, "fail");
                return;
            }
            content = "您的信息已经完成审批，请及时确认，谢谢</br></br>" + content;
            if (emailStr != null && !"".equals(emailStr.toString())) {
                //给下级发送的邮箱地址列表
                String email = emailStr.toString().substring(0, emailStr.toString().length() - 1);
                LinkedHashMap<String, String> condition = new LinkedHashMap<String, String>();
                condition.put("corpCode", sendUser.getCorpCode());
                BaseBiz baseBiz = new BaseBiz();
                List<LfCorpConf> lfCorpConfList = baseBiz.getByCondition(LfCorpConf.class, condition, null);
                String protocol = "";
                String host = "";
                String port = "";
                String username = "";
                String pazzWd = "";
                if (lfCorpConfList != null) {
                    for (int i = 0; i < lfCorpConfList.size(); i++) {
                        LfCorpConf conf = lfCorpConfList.get(i);
                        {
                            if ("email.protocol".equals(conf.getParamKey())) {
                                protocol = conf.getParamValue();
                            }
                            if ("email.host".equals(conf.getParamKey())) {
                                host = conf.getParamValue();
                            }
                            if ("email.port".equals(conf.getParamKey())) {
                                port = conf.getParamValue();
                            }
                            if ("email.username".equals(conf.getParamKey())) {
                                username = conf.getParamValue();
                            }
                            if ("email.password".equals(conf.getParamKey())) {
                                pazzWd = conf.getParamValue();
                            }
                        }
                    }
                } else {
                    String str = "模块参数配置中相应的邮件配置不存在！";
                    EmpExecutionContext.error(str);
                    dailyFlowRecord(AdminUser, str, "fail");
                    return;
                }
                MailInfo mi = new MailInfo();
                Aes aes = new Aes();
                mi.setFrom(username);
                mi.setUsername(username);
                //解密
                mi.setPassword(aes.deString(pazzWd));
                //取的是邮件后面的部分
                mi.setHost(host);
                mi.setPort(port);
                mi.setProtocol(protocol);
                mi.setTo(email);
                mi.setSubject("【审批提醒】");
                mi.setName("");
                mi.setPriority("1");
                mi.setContent(content);

                SendMail sm = new SendMail(mi);

                try {
                    sm.sendMultipleEmail();
                } catch (Exception e) {
                    String str = " 网络原因，邮箱提醒失败！";
                    String msessage = e.getMessage();
                    if (msessage != null && msessage.indexOf("authentication failed") > -1) {
                        str = "地址无效，邮箱提醒失败！";
                    }
                    EmpExecutionContext.error(str);
                    dailyFlowRecord(AdminUser, str, "fail");
                }

            } else {
                String str = "模块类型：" + msg + "，类型：邮件提醒，发送人： " + sendUser.getUserName() + "，没有可用的E-mail。";
                EmpExecutionContext.error(str);
                dailyFlowRecord(AdminUser, str, "fail");
                return;
            }


        } catch (Exception e) {
            EmpExecutionContext.error(e, "邮件提醒下发失败！");

        }


    }

    /**
     * 审核的类型
     *
     * @param infoType
     * @return
     * @description
     * @datetime 2016-3-26 上午11:42:39
     */
    private String getType(int infoType) {
        String title = "";
        if (infoType == 1) {
            title = "短信发送";
        } else if (infoType == 2) {
            title = "彩信发送";
        } else if (infoType == 3) {
            title = "短信模板";
        } else if (infoType == 4) {
            title = "彩信模板";
        } else if (infoType == 5) {
            title = "网讯发送";
        } else if (infoType == 6) {
            title = "网讯模板";
        }
        return title;
    }

    /**
     * 保存审批提醒的记录
     *
     * @param sysuser
     * @param buffer
     * @param type
     */
    private void dailyFlowRecord(LfSysuser sysuser, String buffer, String type) {
        try {
            if ("fail".equals(type)) {
                new SuperOpLog().logFailureString(sysuser.getUserName(), "审批提醒", "其他", buffer, null, sysuser.getCorpCode());
            } else if ("success".equals(type)) {
                new SuperOpLog().logSuccessString(sysuser.getUserName(), "审批提醒", "其他", buffer, sysuser.getCorpCode());
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "审批提醒日志保存失败！");
        }
    }


    /**
     * 处理短信彩信信息审批下发
     *
     * @param lfFlowRecord 审核实时记录对象
     */
    
    public void flowReviewRemind(LfFlowRecord lfFlowRecord) {
        try {
            SmsBiz smsbiz = new SmsBiz();
            PhoneUtil phoneUtil = new PhoneUtil();

            WgMsgConfigBiz wgMsgConfigBiz = new WgMsgConfigBiz();
            //短信提醒内容
            String content = "";
            //创建的任务
            LfWXBASEINFO baseInfo = empDao.findObjectByID(LfWXBASEINFO.class, lfFlowRecord.getMtId());
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            //任务创建人
            LfSysuser lfSysuserCreate = empDao.findObjectByID(LfSysuser.class, lfFlowRecord.getProUserCode());
            conditionMap.put("userName", "admin");//admin用户
            conditionMap.put("corpCode", lfSysuserCreate.getCorpCode());//当前登录用户的企业编码
            List<LfSysuser> AdminLfSysuerList = empDao.findListByCondition(LfSysuser.class, conditionMap, null);
            LfSysuser AdminUser = null;
            if (AdminLfSysuerList != null && AdminLfSysuerList.size() > 0) {
                //系统admin
                AdminUser = AdminLfSysuerList.get(0);
            } else {
                //当前审批人
                //AdminUser = empDao.findObjectByID(LfSysuser.class,lfFlowRecord.getUserCode());
                return;
            }
            //获取SPUSER 以及 尾号
            String[] arr = new ReviewRemindBiz().getSpuserSubnoByCorpCode(lfSysuserCreate.getCorpCode(), AdminUser, lfFlowRecord);
            String spUser = "";
            String subno = "";
            if (arr != null && "success".equals(arr[0])) {
                subno = arr[1];
                spUser = arr[2];
            } else {
                if (arr != null && "fail".equals(arr[0])) {
                    EmpExecutionContext.error("审批提醒获取发送账号失败！");
                } else if (arr != null && "nosubno".equals(arr[0])) {
                    EmpExecutionContext.error("审批提醒没有可用的尾号！");
                }
                return;
            }
            //批次号(同意)
            String batchNumber = GlobalVariableBiz.getInstance().getNewNodeCode();
            //批次号(不同意)
            String disagreeNumber = GlobalVariableBiz.getInstance().getNewNodeCode();
            if (batchNumber == null || "".equals(batchNumber)) {
                EmpExecutionContext.error("审批提醒获取同意指令失败！");
                return;
            }
            if (disagreeNumber == null || "".equals(disagreeNumber)) {
                EmpExecutionContext.error("审批提醒获取不同意指令失败！");
                return;
            }


            // *******2013年-09月-09日   增加逐级审批*******
            int rlevel = lfFlowRecord.getRLevel() + 1;
            //如果是逐级审核
            if (lfFlowRecord.getRType() != null && lfFlowRecord.getRType() == 5) {
                rlevel = 1;
                boolean isLastLevel = new ReviewBiz().checkZhujiIsOver(lfFlowRecord.getPreRv().intValue(), lfFlowRecord.getProUserCode());
                if (isLastLevel) {
                    rlevel = 2;
                }
            }


            //下一级审批记录
            List<LfFlowRecord> nextFlowRecordList = null;
            //存放   USERID  KEY   FRID VALUE
            LinkedHashMap<Long, Long> frIdMap = new LinkedHashMap<Long, Long>();
            //存放   MOBLIE  KEY   FRID VALUE
            LinkedHashMap<String, Long> userIdMap = new LinkedHashMap<String, Long>();
            //记录下一批审批人的手机号码
            String[] phoneArr = null;
            //下一级审批流程
            conditionMap.clear();
            conditionMap.put("mtId", String.valueOf(lfFlowRecord.getMtId()));
            conditionMap.put("infoType", String.valueOf(lfFlowRecord.getInfoType()));
            conditionMap.put("isComplete", "2");
            conditionMap.put("RLevel", String.valueOf(rlevel));
            nextFlowRecordList = empDao.findListByCondition(LfFlowRecord.class, conditionMap, null);
            StringBuffer idStr = new StringBuffer();
            if (nextFlowRecordList != null && nextFlowRecordList.size() > 0) {
                for (LfFlowRecord record : nextFlowRecordList) {
                    idStr.append(record.getUserCode()).append(",");
                    frIdMap.put(record.getUserCode(), record.getFrId());
                }
                String str = idStr.toString().substring(0, idStr.toString().length() - 1);
                conditionMap.clear();
                conditionMap.put("userId&in", str);
                conditionMap.put("corpCode", lfSysuserCreate.getCorpCode());
                //List<LfSysuser> sysuserList = empDao.findListByCondition(LfSysuser.class, conditionMap, null);
                List<LfSysuser> sysuserList = empDao.findListBySymbolsCondition(LfSysuser.class, conditionMap, null);
                ;
                if (sysuserList != null && sysuserList.size() > 0) {
                    phoneArr = new String[sysuserList.size()];
                    for (int i = 0; i < sysuserList.size(); i++) {
                        LfSysuser sysuser = sysuserList.get(i);
                        //此处判断下是否需要有审核提醒【根据操作员中是否勾选审核提醒处理】 有四位
                        String cf = sysuser.getControlFlag();
                        if (cf != null && cf.length() > 1) {
                            String bit = cf.substring(0, 1);//判断是否勾选，如果没勾选则不发送信息
                            if ("0".equals(bit)) {
                                continue;
                            }
                        }
                        phoneArr[i] = sysuser.getMobile();
                        Long frId = frIdMap.get(sysuser.getUserId());
                        userIdMap.put(sysuser.getMobile(), frId);
                    }
                }
            } else {
                EmpExecutionContext.error("审批提醒没有获取到下一级审批信息记录！");
                return;
            }

            if (phoneArr == null || phoneArr.length == 0) {
                EmpExecutionContext.error("审批提醒没有可用的手机号码！");
                return;
            }

            content = getRemindTmpContent(baseInfo, batchNumber, disagreeNumber, lfSysuserCreate);
            if (content == null || "".equals(content)) {
                EmpExecutionContext.error("审批提醒内容为空！");
                return;
            }
            if (!"".equals(spUser) && spUser != null) {
                String[] haoduans = wgMsgConfigBiz.getHaoduan();
                //拿到当前系统配置的号段
                List<GtPortUsed> portsList = smsbiz.getPortByUserId(spUser);
                //计算条数
                StringBuffer sendBuffer = new StringBuffer("");
                int messageCount = 0;
                //是否计费
                boolean flag = SystemGlobals.isDepBilling(lfSysuserCreate.getCorpCode());
                LfExamineSms lfExamineSms = null;
                List<LfExamineSms> examineSmsList = new ArrayList<LfExamineSms>();

                //Integer arrCount = (phoneArr.length/100==0?0: phoneArr.length/100) + 1;
                Integer num = 0;
                Map<Integer, String> map = new SubnoManagerBiz().getPortUserBySpuser(portsList, spUser, subno);
                for (int a = 0; a < phoneArr.length; a++) {
                    String temp = phoneArr[a];
                    if (temp != null && !"".equals(temp) && phoneUtil.checkMobile(temp, haoduans) == 1) {
                        temp = temp.trim();
                        Long frId = userIdMap.get(temp);
                        if (frId == null) {
                            continue;
                        }
                        sendBuffer.append(temp).append(",");
                        int count = smsbiz.countAllOprSmsNumber(content, temp, haoduans, spUser);
                        messageCount = messageCount + count;
                        lfExamineSms = new LfExamineSms();
                        lfExamineSms.setBatchNumber(batchNumber);
                        lfExamineSms.setDisagreeNumber(disagreeNumber);
                        lfExamineSms.setFrId(frId);
                        lfExamineSms.setMsgContent(content);
                        lfExamineSms.setPhone(temp);
                        Integer spisuncm = 0;
                        if ("+".equals(temp.substring(0, 1)) || "00".equals(temp.substring(0, 2))) {
                            spisuncm = 5;
                        } else {
                            spisuncm = wgMsgConfigBiz.getPhoneSpisuncm(temp);
                        }
                        String spNumber = map.get(spisuncm);
                        if (spNumber == null || "".equals(spNumber)) {
                            continue;
                        }
                        lfExamineSms.setSpNumber(spNumber);

                        lfExamineSms.setSpUser(spUser);
                        lfExamineSms.setEsType(1);
                        examineSmsList.add(lfExamineSms);
                        num = num + 1;
                    }
                    if (num >= 100 && num % 100 == 0 && messageCount > 0 && examineSmsList.size() > 0) {
                        new ReviewRemindBiz().sendMsgByMoblieStr(examineSmsList, spUser, subno, messageCount,
                                flag, "0", StaticValue.VERIFYREMIND_MENUCODE, sendBuffer.toString(), content, AdminUser);
                        sendBuffer = new StringBuffer();
                        examineSmsList = new ArrayList<LfExamineSms>();
                        num = 0;
                        messageCount = 0;
                    }
                }
                if (num < 100 && messageCount > 0 && examineSmsList.size() > 0) {
                    new ReviewRemindBiz().sendMsgByMoblieStr(examineSmsList, spUser, subno, messageCount,
                            flag, "0", StaticValue.VERIFYREMIND_MENUCODE, sendBuffer.toString(), content, AdminUser);
                }
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "处理短信彩信信息审批下发异常!");
        }
    }

    /**
     * 返回下发的模板审批信息短信
     *
     * @param baseInfo
     * @param createTaskUser
     * @return
     */
    public String getRemindEmailContent(LfWXBASEINFO baseInfo, LfSysuser createTaskUser) {
        String content = "";

        try {
            content = "类型：移动网讯  </br> 提交人：" + createTaskUser.getName() + " </br> 主题：" + baseInfo.getNAME();
        } catch (Exception e) {
            EmpExecutionContext.error(e, "模板审批信息邮件异常!");
            content = "";
        }
        return content;
    }

    /**
     * 返回下发的模板审批信息短信
     *
     * @param baseInfo
     * @param batchNumber    同意指令
     * @param disagreeNumber 不同意指令
     * @param createTaskUser 提交人对象
     * @return
     */
    
    public String getRemindTmpContent(LfWXBASEINFO baseInfo, String batchNumber, String disagreeNumber, LfSysuser createTaskUser) {
        String content = "";
        try {
            content = "您有网讯模板需要审批：提交人：" + createTaskUser.getName() + "，主题：" + baseInfo.getNAME()
                    + "，网讯模板审批48小时之内回复有效，通过审批回复:" + batchNumber + "，不通过审批回复:" + disagreeNumber + "+意见";
        } catch (Exception e) {
            EmpExecutionContext.error(e, "模板审批信息短信异常!");
            content = "";
        }
        return content;
    }

    /**
     * 页面提交update网讯信息
     *
     * @param base
     * @param pageTemp
     * @param list
     * @return
     * @throws Exception
     */
    
    public int updateUeditor(LfWXBASEINFO base, LfWXPAGE pageTemp,
                             List<LfWXPAGE> list) throws Exception {
        Connection conn = empTransDao.getConnection();
        int result = 0;
        boolean res = false;
        try {
            empTransDao.beginTransaction(conn);
            if (base.getSTATUS() == 1) {
                //如果定稿，判断是否需要审核
                ReviewBiz rev = new ReviewBiz();
                LfFlow flow = rev.checkUserFlow(base.getCREATID(), base.getCORPCODE(), "6");
                if (flow != null) {
                    res = rev.addFlowRecords(conn, base.getNETID(), base.getNAME(),
                            base.getCREATDATE(), 6, flow, base.getCREATID(), "1");
                } else {
                    //无需审核
                    base.setSTATUS(4);
                }
            }
            empTransDao.update(conn, base);
            result = getupdateByUeditor(conn, base, pageTemp, list);
            empTransDao.commitTransaction(conn);
            if (res) {
                // 获取配置文件中的信息
                String isre = SystemGlobals.getSysParam("isRemind");
                if ("0".equals(isre)) {
                    LfFlowRecord record = new LfFlowRecord();
                    record.setInfoType(6);
                    record.setMtId(base.getNETID());
                    record.setProUserCode(base.getCREATID());
                    record.setRLevelAmount(1);
                    record.setRLevel(0);
                    Timestamp tt = new Timestamp(System.currentTimeMillis());
                    record.setSubmitTime(tt);

                    flowReviewRemind(record);
                    sendMail(record);
                }
            }
        } catch (Exception e) {
            empTransDao.rollBackTransaction(conn);
            EmpExecutionContext.error(e, "页面网讯信息处理异常");
            throw e;
        } finally {
            empTransDao.closeConnection(conn);
        }
        return result;


    }

    /**
     * DESC: 网讯模板-修改互动项
     *
     * @param netId  网讯模板ID
     * @param dataId 互动项ID
     * @return true = 成功  false = 失败
     */
    
    public boolean updateDataBind(Long netId, String dataId) throws EMPException {

        //1. 检验网讯模板ID为空时 则默认为修改失败
        if (netId == null || netId == 0) {
            return false;
        }
        //2. 设置删除互动项条件
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        conditionMap.put("netId", netId.toString()); //网讯模板ID

        //3. 执行事务操作
        Connection conn = empTransDao.getConnection();
        boolean result = true;
        try {

            empTransDao.beginTransaction(conn);
            //3.1 删除之前网讯模板互动项ID
            empTransDao.delete(conn, LfWXDataBind.class, conditionMap);

            if (!StringUtils.isEmpty(dataId)) {
                LfWXDataBind dataBind = null;
                String[] dataIdArray = dataId.split(",");
                //3.2  添加新网讯模板互动项ID
                for (int i = 0; i < dataIdArray.length; i++) {
                    dataBind = new LfWXDataBind();
                    dataBind.setNetId(netId);
                    dataBind.setDId(Long.valueOf(dataIdArray[i].trim()));

                    empTransDao.save(conn, dataBind);
                }
            }
            empTransDao.commitTransaction(conn);

        } catch (Exception e) {
            result = false;
            empTransDao.rollBackTransaction(conn);
            throw new EMPException("修改网讯模板互动项失败!", e);
        } finally {

            empTransDao.closeConnection(conn);
        }

        return result;
    }

}
