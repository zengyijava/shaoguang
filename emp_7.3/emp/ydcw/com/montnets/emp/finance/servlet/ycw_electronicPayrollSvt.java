package com.montnets.emp.finance.servlet;

import com.montnets.emp.common.biz.*;
import com.montnets.emp.common.constant.ErrorCodeParam;
import com.montnets.emp.common.constant.SMParams;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.biztype.LfBusManager;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.tailnumber.LfSubnoAllotDetail;
import com.montnets.emp.entity.template.LfTemplate;
import com.montnets.emp.finance.biz.ElecPayrollCommon;
import com.montnets.emp.finance.biz.MobFinancialBiz;
import com.montnets.emp.finance.biz.YdcwBiz;
import com.montnets.emp.finance.util.YdcwErrorStatus;
import com.montnets.emp.util.TxtFileUtil;
import org.apache.http.conn.HttpHostConnectException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 电子工资单
 * 
 * @author Jinny.Ding (djhsun@sina.com)
 * @version 1.0
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011/10/25
 */
@SuppressWarnings("serial")
public class ycw_electronicPayrollSvt extends YdcwBaseServlet {
	//业务代码
	private static final String BUSCODE = "MF0001";
	private static final String _PAYROLL = "电子工资单";
	// 随机验证码
	

	 // 移动财务异常代码备忘录(加前缀,eg:EMP-CW-D100[EMP-财务-表示层&业务层&持久层])：
	 // 1.ECWD100:号段加载失败.
	 // 2.ECWV101:文件内容为空.
	 // 3.ECWB102:短信内容写入临时文件异常,模板参数和文档参数不对,解析文档时异常.
	 // 4.ECWB103:获取鉴权码异常.
	 // 5.ECWV104:初始化财务模板或SP帐号异常.
	 // 6.ECWV105:导入表单集合时异常.
	 // 7.ECWV106:上传的文件类型错误.
	 // 8.ECWV107:电子工资单短信发送异常.
	 // 9.ECWV108:系统生成验证码失败.
	 // 10.ECWB109:网关访问连接异常.
	 //	11.ECWB110:财务短信任务创建时异常.
	 //	12.ECWB111:短信内容加密时异常.
	 //	13.ECWV112:短信发送条数超过上限.
	 //	14.ECWV113:报销提醒短信发送异常.
	 //	15.ECWV114:回款通知短信发送异常.
	 //	16.ECWV115:手工入录数据短信发送异常.
	/**
	 * 电子工资单业务
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	public void find(HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession(true);
		ElecPayrollCommon epcObject = new ElecPayrollCommon();
		YdcwBiz ydcwBiz = new YdcwBiz();
		try {
			epcObject.clearSession(session);
			//当前登录账号的企业编码
			String corpCode = request.getParameter("lgcorpcode");
			//当前登录账号的guid
			//String userIdStr = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String userIdStr = SysuserUtil.strLguserid(request);
			//获取短信模板 
			List<LfTemplate> lfTemplateList = ydcwBiz.getTemplatesList(corpCode, userIdStr, BUSCODE);
			request.setAttribute("tempsList",lfTemplateList);
			
			//当前登录账号的guid
			Long userId = Long.parseLong(userIdStr);
			LfSysuser sysuser = new BaseBiz().getById(LfSysuser.class, userId);
			//获取 发送帐号 [操作员绑定-->机构绑定-->全局发送账号]
			List<Userdata> spUserList = new SmsBiz().getSpUserList(sysuser);
			request.setAttribute("spUserList", spUserList);
			//获取业务类型
			List<LfBusManager> busList = ydcwBiz.getBusManagerList(corpCode);
			request.setAttribute("busList", busList);
			//电子工资单发送
			request.setAttribute("businessType", _PAYROLL);
			//产生taskId
			Long taskId = null;
			if(session.getAttribute("taskId") == null){
				taskId = GlobalVariableBiz.getInstance().getValueByKey("taskId", 1L);
			}else{
				taskId = Long.valueOf((String)session.getAttribute("taskId"));
			}
			
			request.setAttribute("taskId",taskId.toString());
			// 跳转至电子工资单页
			request.getRequestDispatcher(PATH + "/ycw_electronicPayroll.jsp").forward(request,response);
		} catch (Exception e) {
			EmpExecutionContext.debug("Error code:"+YdcwErrorStatus.ECWV104);
			EmpExecutionContext.error(e, "电子工资单跳转异常!");
		}
	}

	/**
	 * 导入文件数据 预览(支持 txt 和 xls)
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	@Override
    public void filePreview(HttpServletRequest request,
                            HttpServletResponse response){
		super.filePreview(request, response);
	}

	/**
	 * 电子工资单短信发送
	 * @param request
	 * @param response
	 */
	public void send(HttpServletRequest request, HttpServletResponse response) {
	
		String lgcorpcode = request.getParameter("lgcorpcode");
		//String lguserid = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String lguserid = SysuserUtil.strLguserid(request);
		String returnStr = "";
		//判断是否使用集群
		if(StaticValue.getISCLUSTER() ==1 )
		{
			HttpSession session = request.getSession(false);
			LfMttask mttask = (LfMttask) session.getAttribute("taskObj");
			CommonBiz commBiz = new CommonBiz();
			//上传文件到文件服务器
			if("success".equals(commBiz.uploadFileToFileCenter(mttask.getMobileUrl())))
			{
				//删除本地文件
				//commBiz.deleteFile(mttask.getMobileUrl());
			}else
			{
				returnStr = "上传号码文件失败，取消任务创建！";
			}
		}
		if("".equals(returnStr)){
			returnStr = super.add(request, response,_PAYROLL);
		}
		request.getSession(false).setAttribute("ycw_result", returnStr);
		// 发送完成后跳转到原来的页面
		String s = request.getRequestURI();
		s = s+"?method=find&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode;
		try {
			response.sendRedirect(s);
		} catch (IOException e) {
			EmpExecutionContext.error(e, "电子工资单短信发送异常!");
		}
		
	}
	
	/**
	 * 手工录入数据预览
	 * @param request
	 * @param response
	 * @throws ServletException
	 */
	public void manualPreview(HttpServletRequest request,
			HttpServletResponse response){
		super.preview(request, response);
	}
	
	/**
	 * 下载模板
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void down(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		InputStream in=null;
		try {
			response.reset();
			response.setCharacterEncoding("UTF-8");
			String path = request.getParameter("path");
			File file = new File(path);
			in = new FileInputStream(file);
			OutputStream os = response.getOutputStream();

			// 设置响应头
			response.setHeader("Content-Disposition", "attachment;filename="
					+ new String(file.getName().getBytes("gbk"), "iso-8859-1"));
			response.addHeader("Content-Length", file.length() + "");
			response.setContentType("application/octet-stream");
			int data = 0;
			while ((data = in.read()) != -1) {
				os.write(data);
			}
			os.close();
			in.close();
		} catch (Exception e) {
			EmpExecutionContext.error(e, "下载模板异常!");
		}finally{
			if(in!=null){
			    try {
                    in.close();
                } catch (Exception e) {
			        EmpExecutionContext.error(e, "流关闭异常！");
                }
			}
		}
	}

	/**
	 * 失败重提的方法
	 * @param request
	 * @param response
	 */
	public void retry(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String lgguid = request.getParameter("lgguid");
		String result = "";
		LinkedHashMap<String, String> objectMap = new LinkedHashMap<String,String>();
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String,String>();
		Long mtid =0L;
		BaseBiz baseBiz = new BaseBiz();
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			//mtid = Long.parseLong(request.getParameter("mtid"));
			
			String mtidStr=request.getParameter("mtid");
			//获取加密类
			ParamsEncryptOrDecrypt encryptOrDecrypt=(ParamsEncryptOrDecrypt)request.getSession(false).getAttribute("decryptObj");
			//解密mtId
			mtid=Long.parseLong(encryptOrDecrypt.decrypt(mtidStr));
			
			LfMttask lfMttask = baseBiz.getById(LfMttask.class, mtid);
			if (!checkFile(lfMttask.getMobileUrl())) {
				// 不允许重新提交.
				writer.print("nofindfile");
				return;
			}
			if (lfMttask.getIsRetry() != null
					&& lfMttask.getIsRetry() == 1) {
				writer.print("isretry");
				return;
			}
			//为了避免用户在上次请求还未返回时刷新页面.再次点失败重发造成的重复发送问题.
			//这儿加一个Session判断这种情况的发生.
			if (request.getSession(false).getAttribute("isretryMtId") != null) {
				String a = request.getSession(false).getAttribute("isretryMtId")
						.toString();
				if (a.equals(mtid.toString())) {
					writer.print("isretry");
					return;
				}
			}
			request.getSession(false).setAttribute("isretryMtId", mtid);
			Long taskId = GlobalVariableBiz.getInstance().getValueByKey("taskId", 1L);//产生一个新的taskId
			lfMttask.setTaskId(taskId);
			LfSysuser lfSysuser = baseBiz.getByGuId(LfSysuser.class, Long.parseLong(lgguid));
			if(lfMttask.getIsReply() == 1){
				//循环尾号发送,需获取新的尾号
				SMParams smParams = new SMParams();
				smParams.setCodes(taskId.toString());
				smParams.setCodeType(5);
				smParams.setCorpCode(lfSysuser.getCorpCode());
				smParams.setAllotType(1);
				smParams.setSubnoVali(true);
				smParams.setTaskId(taskId);
				smParams.setLoginId(lfSysuser.getGuId().toString());
				smParams.setDepId(lfSysuser.getDepId());
				ErrorCodeParam errorCodeParam = new ErrorCodeParam();
				LfSubnoAllotDetail subnoAllotDetail = GlobalVariableBiz.getInstance().getSubnoDetail(smParams,errorCodeParam);
				if(errorCodeParam.getErrorCode() != null && "EZHB237".equals(errorCodeParam.getErrorCode())){
					//没有可用的尾号
					writer.print("noUsedSubNo");
					return ;
				}else if(errorCodeParam.getErrorCode() != null && "EZHB238".equals(errorCodeParam.getErrorCode())){
					//获取尾号失败
					writer.print("noSubNo");
					return ;
				}
				if(subnoAllotDetail == null || subnoAllotDetail.getUsedExtendSubno()== null){
					//获取尾号失败
					writer.print("noSubNo");
					return;
				}
				lfMttask.setSubNo(subnoAllotDetail.getUsedExtendSubno());
			}
			// 重发时，定时发送变成实时发送
			if (lfMttask.getTimerStatus() == 1) {
				lfMttask.setTimerStatus(0);
			}
			lfMttask.setTimerTime(new Timestamp(System.currentTimeMillis()));
			lfMttask.setSubmitTime(new Timestamp(System.currentTimeMillis()));
			lfMttask.setSendstate(0);
			lfMttask.setMtId(null);
			result = new MobFinancialBiz().retryLfMttask(lfMttask);
			if (!"payFailure".equals(result) && !"noMoney".equals(result)) {
				// 将原来的设置 为已重新提交.
				objectMap.put("isRetry", "1");
				conditionMap.put("mtId", mtid.toString());
				baseBiz.update(LfMttask.class, objectMap, conditionMap);
			} else {
				request.getSession(false).setAttribute("isretryMtId", "");
			}
			if (result.equals("000") || result.equals("saveSuccess")
					|| result.equals("timerSuccess")
					|| result.equals("createSuccess")) {
				result =  "success";
			}
		} catch (Exception e) {
//			if (e.getClass().getName().equals(
//					"org.apache.http.conn.HttpHostConnectException")) {
			if (e.getClass().isAssignableFrom(HttpHostConnectException.class)) {
				//网关连接异常
				result = e.getLocalizedMessage();
			} else {
				result = "error";
			}
			try {
				//将原来的设置 为已重新提交.
				objectMap.put("isRetry", "1");
				conditionMap.put("mtId", mtid.toString());
				baseBiz.update(LfMttask.class, objectMap, conditionMap);
			} catch (Exception e2) {
				EmpExecutionContext.error(e2, "更新下行任务记录失败！");
				result ="error";
			}			
			EmpExecutionContext.error(e, "失败重提异常!");
		} finally {
			writer.print(result);
		}
	}
	
	
	
	/**
	 * 检测文件是否存在
	 * @param request
	 * @param response
	 */
	public void goToFile(HttpServletRequest request,
			HttpServletResponse response) {
		String url = request.getParameter("url");
		TxtFileUtil tfu = new TxtFileUtil();
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			writer.print(tfu.checkFile(url));
		} catch (Exception e) {
			// 异常处理
			EmpExecutionContext.error(e, "检测文件是否存在异常!");
		}
	}

	//验证上传的文件
	protected boolean checkFile(String fileUrl)
	{
		boolean result = false;
		try
		{
			result = new TxtFileUtil().checkFile(fileUrl);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "验证上传的文件异常!");
		}
		return result;
	}
	
}
