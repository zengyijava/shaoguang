package com.montnets.emp.samesms.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.biz.SmsBiz;
import com.montnets.emp.common.biz.SmsSendBiz;
import com.montnets.emp.common.biz.SmsSendBiz2;
import com.montnets.emp.common.constant.ErrorCodeInfo;
import com.montnets.emp.common.constant.IErrorCode;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.biztype.LfBusManager;
import com.montnets.emp.entity.corp.LfCorpConf;
import com.montnets.emp.entity.dxzs.LfDfadvanced;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.samesms.biz.BatchSmsBiz;

@SuppressWarnings("serial")
public class ssm_sendBatchSMSSvt extends BaseServlet {

    private final BaseBiz baseBiz = new BaseBiz();
    private final SmsBiz smsBiz = new SmsBiz();
    private final CommonBiz commonBiz = new CommonBiz();

    // 写文件时候要的换行符
    private final String empRoot = "dxzs";

    public void find(HttpServletRequest request, HttpServletResponse response) {
        String lgcorpcode = null;
        Long lguserid = null;
        try {
            // 用户的请求地址
            String s = request.getRequestURI();
            // 是短信客服模块的相同内容还是短信助手模块
            String hjsp = s.substring(s.lastIndexOf("_") + 1, s.lastIndexOf("."));

            // 登录操作员信息
            LfSysuser curSysuser = (LfSysuser) request.getSession(false)
                                                      .getAttribute("loginSysuser");
            // 当前登录企业
            lgcorpcode = curSysuser.getCorpCode();
            // 当前登录操作员id
            lguserid = curSysuser.getUserId();
            // 当前登录操作员对象
            // LfSysuser curSysuser = baseBiz.getLfSysuserByUserId(lguserid);

            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();

            LinkedHashMap<String, String> conditionbusMap = new LinkedHashMap<String, String>();
            LinkedHashMap<String, String> orconp = new LinkedHashMap<String, String>();
            conditionbusMap.put("corpCode&in", "0," + lgcorpcode);
            orconp.put("corpCode", "asc");

            // 设置启用查询条件
            conditionbusMap.put("state", "0");
            // 设置查询手动和手动+触发
            conditionbusMap.put("busType&in", "0,2");

            // 根据企业编码查询业务类型
            List<LfBusManager> busList = baseBiz.getByCondition(LfBusManager.class,
                                                                conditionbusMap,
                                                                orconp);
            request.setAttribute("busList", busList);

            List<Userdata> spUserList = smsBiz.getSpUserList(curSysuser);
            request.setAttribute("sendUserList", spUserList);
            // UserdataAtom userdataAtom = new UserdataAtom();
            // //发送账户存放内存Map
            // userdataAtom.setUserdata(spUserList);

            // 获取高级设置默认信息
            conditionMap.clear();
            conditionMap.put("userid", String.valueOf(lguserid));
            // 1:相同内容群发
            conditionMap.put("flag", "1");
            LinkedHashMap<String, String> orderMap = new LinkedHashMap<String, String>();
            orderMap.put("id", StaticValue.DESC);
            List<LfDfadvanced> lfDfadvancedList = baseBiz.getByCondition(LfDfadvanced.class,
                                                                         conditionMap,
                                                                         orderMap);
            LfDfadvanced lfDfadvanced = null;
            if (lfDfadvancedList != null && lfDfadvancedList.size() > 0) {
                lfDfadvanced = lfDfadvancedList.get(0);
            }
            request.setAttribute("lfDfadvanced", lfDfadvanced);

            conditionMap.clear();
            // 短信模板
            conditionMap.put("tmpType", "3");
            // 有效
            conditionMap.put("tmState", "1");
            // 无需审核或审核通过
            conditionMap.put("isPass&in", "0,1");
            // 静态模板
            conditionMap.put("dsflag", "0");

            // ------------------------------end
            request.setAttribute("reTitle", hjsp);
            LfDep dep = baseBiz.getById(LfDep.class, curSysuser.getDepId());
            if (dep != null) {
                request.setAttribute("depSign", dep.getDepName());
            }

            // 企业是否启用分批设置
            conditionMap.clear();
            conditionMap.put("corpCode", lgcorpcode);
            conditionMap.put("paramKey", "sms.split");
            List<LfCorpConf> corpConf = baseBiz.getByCondition(LfCorpConf.class, conditionMap, null);
            if (corpConf != null && !corpConf.isEmpty()) {
                request.setAttribute("confSmsSplit", corpConf.get(0).getParamValue());
            } else {
                request.setAttribute("confSmsSplit", "0");
            }

            // 产生taskId
            Long taskId = commonBiz.getAvailableTaskId();
            // 操作日志信息
            String opContent = "获取taskid(" + taskId + ")成功";
            LfSysuser lfSysuser = (LfSysuser) request.getSession(false)
                                                     .getAttribute("loginSysuser");
            EmpExecutionContext.info("相同内容群发",
                                     lgcorpcode,
                                     String.valueOf(lguserid),
                                     lfSysuser.getUserName(),
                                     opContent.toString(),
                                     "GET");

            request.setAttribute("taskId", taskId.toString());
            request.getRequestDispatcher(this.empRoot + "/samesms/ssm_sendBatchSMS.jsp")
                   .forward(request, response);

        }
        catch (Exception e) {
            EmpExecutionContext.error(e, lguserid, lgcorpcode);
            request.setAttribute("findresult", "-1");
            try {
                request.getRequestDispatcher(this.empRoot + "/samesms/ssm_sendBatchSMS.jsp")
                       .forward(request, response);
            }
            catch (ServletException e1) {
                EmpExecutionContext.error(e1, lguserid, lgcorpcode);
            }
            catch (IOException e1) {
                EmpExecutionContext.error(e1, lguserid, lgcorpcode);
            }
        }
    }

    // 预览之后提交
	public void add(HttpServletRequest request, HttpServletResponse response) {
		try {
			// 获取分批设置
			String splitFlag = request.getParameter("splitFlag");
			String batchNodeNumStr = request.getParameter("batchNodeNum");
			batchNodeNumStr = (null != batchNodeNumStr && !"".equals(batchNodeNumStr.trim())) ? batchNodeNumStr : "0";
			int batchNodeNum = Integer.parseInt(batchNodeNumStr);
			// 未分批，走以前短信发送逻辑
			if (0 == batchNodeNum || splitFlag == null || "0".equals(splitFlag)) {
				sendSMS(request, response);
			} else if (0 < batchNodeNum && 20 >= batchNodeNum) {
				// 分批限制最多分批20
				List<String> batchNodeList = new ArrayList<String>();
				// 遍历每个批次数量
				for (int i = 0; i < batchNodeNum; i++) {
					String batchNodeValueStr = request.getParameter("batchNode" + i);
					String batchNodeTimeValueStr = request.getParameter("batchNodeTimeValue" + i);
					// batchNodeTimeValueStr = (null != batchNodeTimeValueStr &&
					// !"".equals(batchNodeTimeValueStr.trim())) ?
					// batchNodeTimeValueStr : new
					// SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
					batchNodeValueStr = (null != batchNodeValueStr && !"".equals(batchNodeValueStr.trim())) ? batchNodeValueStr : "0";
					// 每个批次数量
					Integer batchNodeValue = Integer.parseInt(batchNodeValueStr);
					batchNodeList.add(batchNodeValue + "," + batchNodeTimeValueStr);
				}
				// 分批发送
				try {
					batchSendSMS(request, response, batchNodeList);
				} catch (Exception e) {
					EmpExecutionContext.error(e, "相同内容分批发送异常！");
				}

			} else {
				// 非法分批设置，不予处理
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "相同内容分批发送异常！");
		}
	}

    private void batchSendSMS(HttpServletRequest request, HttpServletResponse response, List<String> batchNodeList) throws Exception {
		String result = "";
		// 任务主题
		String title = request.getParameter("taskname");
		// 任务ID
		String taskId = request.getParameter("taskId");
		// 当前登录操作员id
		// String lguserid = request.getParameter("lguserid");
		// 漏洞修复 session里获取操作员信息
		String lguserid = SysuserUtil.strLguserid(request);

		// 当前登录企业
		String lgcorpcode = request.getParameter("lgcorpcode");
		// 草稿箱id
		String draftId = request.getParameter("draftId");
		// 提交类型
		String bmtType = request.getParameter("bmtType");
		// 发送账号
		String spUser = request.getParameter("spUser");
		// 发送类型：1-相同内容短信 2－动态模板短信；3－文件内容短信；4-客户群组短信
		String sendType = request.getParameter("sendType");
		EmpExecutionContext.info("[OTHER] mod：相同内容群发，发送servlet接收参数，userid:" + lguserid + "，corpCode:" + lgcorpcode + "，taskId:" + taskId + "，sendType:" + sendType + "，bmtType:" + bmtType + "，spUser:" + spUser);
		// 主题为默认时,直接返回(防止重发)
		if (title != null && "不作为短信内容发送".equals(title.trim())) {
			EmpExecutionContext.error("相同内容发送获取参数异常，" + "title:" + title + "，taskId：" + taskId + "，errCode：" + IErrorCode.V10001);
			String langName = (String) request.getSession().getAttribute(StaticValue.LANG_KEY);
			result = ErrorCodeInfo.getInstance(langName).getErrorInfo(IErrorCode.V10001);
			EmpExecutionContext.error(result);
		} else {
			SmsSendBiz2 smsSendBiz = new SmsSendBiz2();
			result = smsSendBiz.send(request, response, batchNodeList);
			// 删除草稿箱，如果有的话
			smsSendBiz.dealDrafts(draftId, result, lguserid, lgcorpcode);
		}

		try {
			String s = request.getRequestURI();

			request.getSession(false).setAttribute("mcs_batchResult", result);
			// 重定向
			s = s + "?method=find&lguserid=" + lguserid + "&lgcorpcode=" + lgcorpcode + "&oldTaskId=" + taskId + "&t=" + System.currentTimeMillis();
			response.sendRedirect(s);
		} catch (Exception e) {
			EmpExecutionContext.error(e, lguserid, lgcorpcode);
		}
	}

    private void sendSMS(HttpServletRequest request, HttpServletResponse response) {
        String result = "";
        // 任务主题
        String title = request.getParameter("taskname");
        // 任务ID
        String taskId = request.getParameter("taskId");
        // 当前登录操作员id
        // String lguserid = request.getParameter("lguserid");
        // 漏洞修复 session里获取操作员信息
        String lguserid = SysuserUtil.strLguserid(request);

        // 当前登录企业
        String lgcorpcode = request.getParameter("lgcorpcode");
        // 草稿箱id
        String draftId = request.getParameter("draftId");
        // 提交类型
        String bmtType = request.getParameter("bmtType");
        // 发送账号
        String spUser = request.getParameter("spUser");
        // 发送类型：1-相同内容短信 2－动态模板短信；3－文件内容短信；4-客户群组短信
        String sendType = request.getParameter("sendType");
        // 大文件
        String bigfileid = request.getParameter("bigfileid");

        EmpExecutionContext.info("[OTHER] mod：相同内容群发，发送servlet接收参数，userid:"
                                 + lguserid
                                 + "，corpCode:"
                                 + lgcorpcode
                                 + "，taskId:"
                                 + taskId
                                 + "，sendType:"
                                 + sendType
                                 + "，bmtType:"
                                 + bmtType
                                 + "，spUser:"
                                 + spUser);
        // 主题为默认时,直接返回(防止重发)
        if (title != null && "不作为短信内容发送".equals(title.trim())) {
            EmpExecutionContext.error("相同内容发送获取参数异常，"
                                      + "title:"
                                      + title
                                      + "，taskId："
                                      + taskId
                                      + "，errCode："
                                      + IErrorCode.V10001);
            String langName = (String) request.getSession().getAttribute(StaticValue.LANG_KEY);
            result = ErrorCodeInfo.getInstance(langName).getErrorInfo(IErrorCode.V10001);
            EmpExecutionContext.error(result);
        } else {
            if (bigfileid != null && !"".equals(bigfileid)) {
                BatchSmsBiz batchSmsBiz = new BatchSmsBiz();
                // 提交发送
                result = batchSmsBiz.batchSend(request, response);

                batchSmsBiz.dealDrafts(draftId, result, lguserid, lgcorpcode);
            } else {
                SmsSendBiz smsSendBiz = new SmsSendBiz();
                // 提交发送
                result = smsSendBiz.send(request, response);

                // 删除草稿箱，如果有的话
                smsSendBiz.dealDrafts(draftId, result, lguserid, lgcorpcode);
            }
        }

        try {
            String s = request.getRequestURI();

            request.getSession(false).setAttribute("mcs_batchResult", result);
            // 重定向
            s = s
                + "?method=find&lguserid="
                + lguserid
                + "&lgcorpcode="
                + lgcorpcode
                + "&oldTaskId="
                + taskId
                + "&t="
                + System.currentTimeMillis();
            response.sendRedirect(s);
        }
        catch (Exception e) {
            EmpExecutionContext.error(e, lguserid, lgcorpcode);
        }
    }

    /**
     * 发送后跳转(暂不使用)
     * 
     * @param result
     * @param lguserid
     * @param lgcorpcode
     * @param response
     */
    private void goFind(String result,
                        String lguserid,
                        String lgcorpcode,
                        HttpServletRequest request,
                        HttpServletResponse response) {
        try {
            String s = request.getRequestURI();

            request.getSession(false).setAttribute("mcs_batchResult", result);

            s = s
                + "?method=find&lguserid="
                + lguserid
                + "&lgcorpcode="
                + lgcorpcode
                + "&t="
                + System.currentTimeMillis();
            response.sendRedirect(s);
        }
        catch (Exception e) {
            EmpExecutionContext.error(e, lguserid, lgcorpcode);
        }
    }

}
