package com.montnets.emp.report.servlet;

import com.montnets.emp.common.biz.BalanceLogBiz;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.sms.MtTaskC;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.query.biz.QueryBiz;
import com.montnets.emp.query.biz.SysMoRealTimeRecordBiz;
import com.montnets.emp.query.vo.SysMoMtSpgateVo;
import com.montnets.emp.report.bean.RptStaticValue;
import com.montnets.emp.report.biz.RemainMtRecordBiz;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.SuperOpLog;
import org.apache.commons.beanutils.DynaBean;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 短信滞留信息查看
 *
 * @author liaojirong <ljr0300@163.com>
 * @project p_cxtj
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-22 下午02:48:09
 * @description
 */
@SuppressWarnings("serial")
public class rep_remainMtRecordSvt extends BaseServlet {

    private final String empRoot = "cxtj";
    private final String basePath = "/report";
    private final BaseBiz baseBiz = new BaseBiz();
    //操作模块
    public static final String auditModule = "数据查询";
    //操作用户
    private final String opSper = StaticValue.OPSPER;
    private final SuperOpLog spLog = new SuperOpLog();
    //时分秒格式化
    private final SimpleDateFormat hms = new SimpleDateFormat("HH:mm:ss");
    private final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //起始ms数
        long startl = System.currentTimeMillis();
        //开始时间
        String starthms = hms.format(startl);
        boolean isFirstEnter = true;
        try {
            PageInfo pageInfo = new PageInfo();
            isFirstEnter = pageSet(pageInfo, request);
            //发送账号查询条件
            String spUser = request.getParameter("spUser");
            String spgate = request.getParameter("spgate");
            String phone = request.getParameter("phone");
            String startTime = request.getParameter("sendtime");
            String endTime = request.getParameter("recvtime");
            String msg = request.getParameter("msg");
            String taskid = request.getParameter("taskid");
            //企业编码
            String corpCode = request.getParameter("lgcorpcode");
            QueryBiz qbiz = new QueryBiz();
            //当企业编码传入为空则从session中取
            if (corpCode == null || "".equals(corpCode) || "null".equals(corpCode)) {
                corpCode = qbiz.getCorpCode(request);
            }

            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            LinkedHashMap<String, String> orderByMap = new LinkedHashMap<String, String>();
            //获取sp账号
            SysMoMtSpgateVo sysMoMtSpgateVo = new SysMoMtSpgateVo();
            //企业编号
            sysMoMtSpgateVo.setCropCode(corpCode);

            List<SysMoMtSpgateVo> spList = new SysMoRealTimeRecordBiz().getDownHisVos(sysMoMtSpgateVo);
            //发送账户
            String spUsers = "";
            //获取绑定的sp账号
            spUsers = qbiz.getSpUsers("0", corpCode, StaticValue.getCORPTYPE());
            conditionMap.clear();
            List<MtTaskC> taskCList = new ArrayList<MtTaskC>();
            conditionMap.put("sendStatus", "208");
            if (!isFirstEnter) {

                orderByMap.clear();
                orderByMap.put("sendTime", StaticValue.DESC);

                if (startTime != null && !"".equals(startTime)) {
                    conditionMap.put("sendTime&>=", startTime);
                }
                if (endTime != null && !"".equals(endTime)) {
                    conditionMap.put("sendTime&<=", endTime);
                }
                if (phone != null && !"".equals(phone)) {
                    conditionMap.put("shouji&like", phone);
                }
                if (spgate != null && !"".equals(spgate)) {
                    conditionMap.put("spgate", spgate);
                }

                if (taskid != null && !"".equals(taskid)) {
                    Integer taskidl = 0;
                    if ("0".equals(taskid)) {
                        taskidl = -1;
                    } else {
                        try {
                            taskidl = Integer.parseInt(taskid);
                        } catch (Exception e) {
                            EmpExecutionContext.error(e, "获取taskid转换异常");
                            taskidl = -1;
                        }
                    }
                    conditionMap.put("taskId", taskidl.toString());
                }

                if (spUser != null && !"".equals(spUser)) {
                    //如果是DB2或者oracle 则做兼容小写处理
                    if ("1".equals(SystemGlobals.getSysParam("SPUSER_ISLWR")) && (StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE
                            || StaticValue.DBTYPE == StaticValue.DB2_DBTYPE)) {
                        conditionMap.put("userId&in", spUser.toUpperCase() + "," + spUser.toLowerCase());
                    } else {
                        conditionMap.put("userId", spUser);
                    }
                } else {
                    if (spUsers != null && !"".equals(spUsers)) {
                        //如果是DB2或者oracle 则做兼容小写处理
                        if ("1".equals(SystemGlobals.getSysParam("SPUSER_ISLWR")) && (StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE
                                || StaticValue.DBTYPE == StaticValue.DB2_DBTYPE)) {
                            String spstrs = spUsers.replaceAll("'", "");
                            conditionMap.put("userId&in", spstrs.toUpperCase() + "," + spstrs.toLowerCase());
                        } else {
                            conditionMap.put("userId&in", spUsers.replaceAll("'", ""));
                        }
                    }
                }

                if (msg != null && !"".equals(msg)) {
                    conditionMap.put("message&like", msg);
                }

                taskCList = baseBiz.getByCondition(MtTaskC.class, null, conditionMap, null, pageInfo);
            }


            request.getSession(RptStaticValue.GET_SESSION_FALSE).setAttribute("remainCondition", conditionMap);
            request.setAttribute("isFirstEnter", isFirstEnter);//是否第一次进入标示
            List<String> userList = qbiz.getSpUserList("0", corpCode, StaticValue.getCORPTYPE());
            request.setAttribute("sendUserList", userList);
            request.setAttribute("pageInfo", pageInfo);
            request.setAttribute("spList", spList);
            request.setAttribute("taskCList", taskCList);

            long count = 0l;
            //从pageinfo中获取查询总条数
            if (pageInfo != null) {
                count = pageInfo.getTotalRec();
            }
            // 写日志
            String opContent = "短信滞留记录查询：" + count + "条 开始：" + starthms + ",耗时:" + (System.currentTimeMillis() - startl) + "ms";
            new QueryBiz().setLog(request, "短信滞留记录", opContent, StaticValue.GET);

            request.getRequestDispatcher(empRoot + basePath + "/rep_remainMtRecord.jsp").forward(request, response);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "短信滞留记录查询servlet异常");
        }
    }

    /**
     * 滞留记录重发和删除方法
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //操作类型
        String opType = null;
        //操作内容
        String opContent = null;
        //获取选中的滞留记录id
        String ids = request.getParameter("ids");
        //操作类型  send：重发   del:删除
        String optype = request.getParameter("optype");
        //String lguserid=request.getParameter("lguserid");
        //漏洞修复 session里获取操作员信息
        String lguserid = SysuserUtil.strLguserid(request);

        LfSysuser lfsysuser = null;
        //找到余额不足的账号，格式为：xxxxxx,xxxxxx,xxxxxx
        String noFeeSpUser = "";
        try {
            SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMddHHmmss");
            lfsysuser = baseBiz.getById(LfSysuser.class, lguserid);
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
            //短信条数
            int count = 0;
            //如果是重发、删除单挑或选中的记录
            if (ids != null && !"".equals(ids)) {
                conditionMap.put("id&in", ids);
                //包含","说明是多条记录，不包含则是单条
                if (ids.indexOf(",") != -1) {
                    count = ids.split(",").length;
                } else {
                    count = 1;
                }
            }
            //重发、删除查询出的所有记录
            else {
                conditionMap = (LinkedHashMap<String, String>) request.getSession(RptStaticValue.GET_SESSION_FALSE).getAttribute("remainCondition");
                String countStr = request.getParameter("count");
                if (countStr != null && !"".equals(countStr)) {
                    count = Integer.parseInt(countStr);
                }
            }

            //当天时间
            Calendar c1 = Calendar.getInstance();
            //重发操作
            if ("send".equals(optype)) {
                //计算超时时间点
                Calendar curTime = Calendar.getInstance();
                //距离当前时间24小时前的时间点
                curTime.add(Calendar.HOUR, -StaticValue.MTTASKC_OVERDUE);
                //只要大于这个时间点，都是有效
                conditionMap.put("sendTime&>", df.format(curTime.getTime()));

                //这里去掉这个条件，免得没来得及刷新也行，这个条件还在
                conditionMap.remove("userId&not in");
                //找到余额不足的账号，格式为：xxxxxx,xxxxxx,xxxxxx
                noFeeSpUser = checkSpFee(conditionMap, lfsysuser.getCorpCode());
                //有余额不足的账号则加入条件中，使该账号的记录不能重发，只重发余额够的账号的滞留记录
                if (noFeeSpUser != null && noFeeSpUser.length() > 0) {
                    conditionMap.put("userId&not in", noFeeSpUser);
                } else {
                    //这里去掉这个条件，免得没来得及刷新也行，这个条件还在
                    conditionMap.remove("userId&not in");
                }

                opType = StaticValue.UPDATE;
                opContent = "重新发送滞留短信";
                //当前时间增加一天
                c1.add(Calendar.DAY_OF_MONTH, 1);
            } else if ("del".equals(optype)) {
                c1.add(Calendar.DAY_OF_MONTH, -1);
                opType = StaticValue.DELETE;
                opContent = "删除滞留短信";
            }
            //网关发送状态太，0.设置为已处理
            objectMap.put("sendStatus", "0");
            objectMap.put("validTime", df2.format(c1.getTime()));
            int resultCount = baseBiz.updateBySymbolsCondition(MtTaskC.class, objectMap, conditionMap);

            spLog.logSuccessString(lfsysuser.getUserName(), auditModule, opType, opContent, lfsysuser.getCorpCode());

            //无符合发送条件的记录
            if (resultCount == 0) {
                response.getWriter().print("nocount");
            }
            //有余额不足而不发的账号，则返回到前台
            else if (noFeeSpUser != null && noFeeSpUser.length() > 0) {
                //返回加上余额不足未发送的账号，格式为：true&xxxxxx,xxxxxx
                response.getWriter().print("true&" + noFeeSpUser);
            } else {
                response.getWriter().print(true);
            }

        } catch (Exception e) {
            EmpExecutionContext.error(e, "短信滞留记录修改异常");
            if (lfsysuser != null) {
                spLog.logFailureString(lfsysuser.getUserName(), auditModule, opType, opContent + opSper, e, lfsysuser.getCorpCode());
            }
            response.getWriter().print(false);
        }
    }

    /**
     * 检查待重发的记录运营商余额够不够，返回不够的发送账号
     *
     * @param conditionMap 查询条件
     * @param corpCode     企业编码
     * @return 返回余额不够的发送账号
     */
    private String checkSpFee(LinkedHashMap<String, String> conditionMap, String corpCode) {
        RemainMtRecordBiz remainbiz = new RemainMtRecordBiz();
        List<DynaBean> spuserCountList = remainbiz.getMtTaskCount(conditionMap);
        if (spuserCountList == null || spuserCountList.size() == 0) {
            return null;
        }
        //余额不足的账号
        String noFeeSpUsers = "";
        BalanceLogBiz balanceBiz = new BalanceLogBiz();
        for (int i = 0; i < spuserCountList.size(); i++) {
            Integer count = Integer.valueOf(spuserCountList.get(i).get("count").toString());
            String result = balanceBiz.checkGwFee(spuserCountList.get(i).get("userid").toString(), count, corpCode, false, 1);
            if ("koufeiSuccess".equals(result) || "notneedtocheck".equals(result)) {
                //余额充足或不需要判断余额
                continue;
            } else {
                //记录余额不足的账号
                noFeeSpUsers += spuserCountList.get(i).get("userid").toString() + ",";
                continue;
            }
        }
        if (noFeeSpUsers != null && noFeeSpUsers.length() > 0) {
            noFeeSpUsers = noFeeSpUsers.substring(0, noFeeSpUsers.length() - 1);
        }
        return noFeeSpUsers;
    }


}





