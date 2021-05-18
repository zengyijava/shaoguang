package com.montnets.emp.spbalance.servlet;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.tools.SysuserUtil;
import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.ParamsEncryptOrDecrypt;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.entity.pasgroup.Userfee;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.servmodule.txgl.biz.PageFieldConfigBiz;
import com.montnets.emp.servmodule.txgl.entity.LfPageField;
import com.montnets.emp.servmodule.txgl.entity.LfSpFeeAlarm;
import com.montnets.emp.servmodule.txgl.entity.LfSpFeeLog;
import com.montnets.emp.spbalance.biz.SpBalanceBiz;
import com.montnets.emp.spbalance.vo.LfSpFeeAlarmVo;
import com.montnets.emp.spbalance.vo.UserfeeVo;
import com.montnets.emp.util.CheckUtil;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.SuperOpLog;

/**
 * SP账号充值回收管理
 * 
 * @功能概要：
 * @项目名称： p_txgl
 * @初创作者： liuxiangheng <xiaokanrensheng1012@126.com>
 * @公司名称： ShenZhen Montnets Technology CO.,LTD.
 * @创建时间： 2016-10-11 下午03:37:24
 *        <p>
 *        修改记录1：
 *        </p>
 * 
 *        <pre>
 *      修改日期：
 *      修改人：
 *      修改内容：
 * </pre>
 */
public class spb_spBalanceMgrSvt extends BaseServlet
{

	private final String				empRoot				= "txgl";

	private final String				base				= "/spbalance";

	private static final long	serialVersionUID	= 9206855342339116239L;

	final BaseBiz						baseBiz				= new BaseBiz();

	final SuperOpLog					spLog				= new SuperOpLog();

	
	
	/**
	 * 解密处理
	 * @param request
	 * @param udgId
	 * @return
	 */
	public String getDecryptValue(HttpServletRequest request,String udgId){
				//-----增加解密处理----
			//加密对象
			ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
			String uid="";
			//加密对象不为空
			if(encryptOrDecrypt != null)
			{
				//解密
				uid = encryptOrDecrypt.decrypt(udgId);
				if(uid == null)
				{
					EmpExecutionContext.error("SP账号充值回收参数解密码失败，keyId:"+uid);
					return "";
				}
			}
			else
			{
				EmpExecutionContext.error("SP账号充值回收从session中获取加密对象为空！");
				return "";
			}
			return uid;
	}
	
	
	/**
	 * 获取加密对象
	 * @description    
	 * @param request
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-10-26 下午07:29:28
	 */
	public ParamsEncryptOrDecrypt getParamsEncryptOrDecrypt(HttpServletRequest request)
	{
		try
		{
			ParamsEncryptOrDecrypt encryptOrDecrypt = null;
			//加密对象
			Object encrypOrDecrypttobject = (ParamsEncryptOrDecrypt)request.getSession(false).getAttribute("decryptObj");
			//加密对象不为空
			if(encrypOrDecrypttobject != null)
			{
				//强转类型
				encryptOrDecrypt=(ParamsEncryptOrDecrypt)encrypOrDecrypttobject;
			}
			else
			{
				EmpExecutionContext.error("从session获取加密对象为空。");
			}
			return encryptOrDecrypt;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "从session获取加密对象异常。");
			return null;
		}
	}
	
	
	public void find(HttpServletRequest request, HttpServletResponse response)
	{
		long begin_time = System.currentTimeMillis();
		try
		{
			// SP账号
			String spuser = request.getParameter("spuser");
			// 定义sp账号业务类
			SpBalanceBiz spbalanceBiz = new SpBalanceBiz();
			// 获取界面登录操作员企业编码
			String corpcode = "";
			//获取登录sysuser
			LfSysuser sysuser =spbalanceBiz.getCurrenUser(request);
			if(sysuser==null){
				EmpExecutionContext.error("SP账号充值回收,session中获取当前登录对象出现异常");
				return;
			}
			corpcode=sysuser.getCorpCode();
			//判断企业编码获取
			if(corpcode==null||"".equals(corpcode)){
				EmpExecutionContext.error("SP账号充值回收,session中获取企业编码出现异常");
				return;
			}
			//判断SP账号是否是属于本企业的
			if(spuser != null && !"".equals(spuser.trim()))
			{
				//多企业才处理
				if(StaticValue.getCORPTYPE() ==1){
					boolean checkFlag=new CheckUtil().checkSysuserInCorp(sysuser, corpcode, spuser, null);
					if(!checkFlag){
						EmpExecutionContext.error("SP账号充值回收,检查操作员，企业编码，发送账号不通过，spuserid："+spuser+",corpcode="+corpcode);
						return;
					}
				}
			}
				
			// 信息类型 1 短信 2 彩信
			String accounttype = request.getParameter("accounttype");
			// 账号类型 应用 接入
			String sptype = request.getParameter("sptype");
			//判断是否为返回
			String operatePageReturn = request.getParameter("operatePageReturn");
			// 分页对象
			PageInfo pageInfo = new PageInfo();
			// 定义查询条件对象
			UserfeeVo ufvo = new UserfeeVo();
			if("true".equals(operatePageReturn)){
				ufvo = (UserfeeVo) request.getSession(false).getAttribute("userfeevo");
				pageInfo = (PageInfo) request.getSession(false).getAttribute("spbalancepf");
			}else{
				// 设值信息类型 0 短信 1 彩信 查询条件
				ufvo.setAccounttype(accounttype);
				// 设值账号类型 应用 接入 查询条件
				ufvo.setSptype(sptype);
				// 设值 账号 查询条件
				ufvo.setSpuser(spuser);
				// 设置企业编码
				ufvo.setCorpcode(corpcode);	
			}
			// 初始化分页信息
			pageSet(pageInfo, request);
			// 查询预付费充值相关账号信息列表
			List<DynaBean> ufdynlist = spbalanceBiz.getSpBalanceByYff(ufvo, pageInfo);
			// SP账号余额查询,增加SP账号控制,只能查看该操作员绑定的SP账号
			//如果是标准版
			if (StaticValue.getCORPTYPE() == 0) {
				if (!"2".equals(sysuser.getUserId().toString())) {
					ufdynlist = spbalanceBiz.getDynaBeanList(ufdynlist, sysuser.getUserId());
				}
			}
			//如果是托管版
			else if (StaticValue.getCORPTYPE() == 1) {
				if (!"3".equals(sysuser.getUserId().toString())) {
					ufdynlist = spbalanceBiz.getDynaBeanList(ufdynlist, sysuser.getUserId());
				}
			}
			// 保存分页对象至request
			request.setAttribute("pageInfo", pageInfo);
			// 保存查询结果
			request.setAttribute("ufdynlist", ufdynlist);
			ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
			HashMap<String,String> encryptmap =new HashMap<String,String>();
			//未知机构客户设置标识
			if(ufdynlist != null && ufdynlist.size()>0)
			{
				//加密对象不为空
				if(encryptOrDecrypt != null)
				{
					//加密处理
					String spuserjmstr="";
					for(DynaBean depobj:ufdynlist) 
					{
						String useridysstr=depobj.get("userid")!=null?depobj.get("userid").toString():"";
						spuserjmstr = encryptOrDecrypt.encrypt(useridysstr);
							//通过实际值，加密值
						encryptmap.put(useridysstr, spuserjmstr);
					}
				}
				else
				{
					EmpExecutionContext.error("SP账号充值回收，从session中获取加密对象为空！");
				}
			}
			request.setAttribute("encryptmap", encryptmap);
			// 获取所有账号对应的通知人和号码
			Map<String, List<LfSpFeeAlarmVo>> userAlarmMap = spbalanceBiz.getSpFeeAlarmUserMap(corpcode);
			// 获取系统定义的短彩类型值
			PageFieldConfigBiz gcBiz = new PageFieldConfigBiz();
			List<LfPageField> pagefileds = gcBiz.getPageFieldById("100002");
			request.setAttribute("pagefileds", pagefileds);
			request.setAttribute("userAlarmMap", userAlarmMap);
			request.setAttribute("ufvorq", ufvo);
			request.getSession(false).setAttribute("userfeevo", ufvo);
			request.getSession(false).setAttribute("spbalancepf", pageInfo);
			// 增加查询日志
			long end_time = System.currentTimeMillis();

			// 日志开始时间
			SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
			if(pageInfo != null)
			{
				String opContent = "查询开始时间：" + format.format(begin_time) + ",耗时:" + (end_time - begin_time) + "ms，数量：" + pageInfo.getTotalRec();
				opSucLog(request, "SP账号充值回收列表信息", opContent, "GET");
			}
			request.getRequestDispatcher(this.empRoot + this.base + "/spb_spBalanceMgr.jsp").forward(request, response);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "SP账号充值回收管理页面跳转出现异常！");
			request.setAttribute("findresult", "-1");
			try
			{
				request.getRequestDispatcher(this.empRoot + base + "/spb_spBalanceMgr.jsp").forward(request, response);
			}
			catch (ServletException e1)
			{
				EmpExecutionContext.error(e, "SP账号充值回收管理页面跳转出现异常！");
			}
			catch (IOException e1)
			{
				EmpExecutionContext.error(e, "SP账号充值回收管理页面跳转出现异常！");
			}
		}

	}

	/**
	 * 充值
	 * 
	 * @Title: addBalance
	 * @Description: TODO
	 * @param request
	 * @param response
	 * @return void
	 */
	public void addBalance(HttpServletRequest request, HttpServletResponse response)
	{
		// 充值条数
		String countStr = request.getParameter("count");
		// 充值sp账号
		String useridstr = request.getParameter("useridstr");
		try
		{
			// 数字类型的充值条数
			Long count = null;
			// 判断充值条数合法性
			if(countStr != null && !"".equals(countStr.trim()))
			{
				count = Long.parseLong(countStr);
			}
			else
			{
				EmpExecutionContext.error("充值条数不合法，SP账号充值失败，count：" + count);
				response.getWriter().write("-1");
				return;
			}
			// 判断sp账号合法性
			if(useridstr == null || "".equals(useridstr.trim()))
			{
				EmpExecutionContext.error("sp账号不合法，SP账号充值失败，useridstr：" + useridstr);
				response.getWriter().write("-1");
				return;
			}
			// sp账号充值biz
			SpBalanceBiz balanceLogBiz = new SpBalanceBiz();
			// 取得当前登录信息
			Long lgguid=null;
			// 获取界面登录操作员企业编码
			String corpcode = "";
			//获取登录sysuser
			LfSysuser sysuser =balanceLogBiz.getCurrenUser(request);
			if(sysuser==null){
				EmpExecutionContext.error("SP账号充值回收,充值操作，session中获取当前登录对象出现异常");
				return;
			}
			corpcode=sysuser.getCorpCode();
			//判断企业编码获取
			if(corpcode==null||"".equals(corpcode)){
				EmpExecutionContext.error("SP账号充值回收,充值操作，session中获取企业编码出现异常");
				return;
			}
			lgguid = sysuser.getGuId();
			//判断企业编码获取
			if(lgguid==null){
				EmpExecutionContext.error("SP账号充值回收,充值操作，session中获取GUID出现异常,corpcode:"+corpcode);
				return;
			}
			
			//判断SP账号是否是属于本企业的
			if(useridstr != null && !"".equals(useridstr.trim()))
			{
				//解密
				useridstr=getDecryptValue(request, useridstr);
				//多企业才处理
				if(StaticValue.getCORPTYPE() ==1){
					boolean checkFlag=new CheckUtil().checkSysuserInCorp(sysuser, corpcode, useridstr, null);
					if(!checkFlag){
						EmpExecutionContext.error("SP账号充值回收,充值操作，检查操作员，企业编码，发送账号不通过，spuserid："+useridstr+",corpcode="+corpcode);
						return;
					}
				}
			}
			

			// 备注
			String addMark = request.getParameter("addMark");
			if(addMark == null || "".equals(addMark))
			{
				addMark = " ";
			}
			
			Integer returnmsg = 0;
			returnmsg = balanceLogBiz.addSpUserBalance(sysuser, useridstr, count);
			LfSpFeeLog lfspfeelog = new LfSpFeeLog();
			// 企业编码
			lfspfeelog.setCorpcode(sysuser.getCorpCode());
			// 充值数量
			lfspfeelog.setIcount(count);
			// 备注
			lfspfeelog.setMemo(addMark);
			// 充值目的SPuser 充值SP账号
			lfspfeelog.setSpuser(useridstr);
			// 当前登录操作员id
			lfspfeelog.setUserid(sysuser.getUserId());
			// 设值充值结果
			if(returnmsg == 0)
			{
				lfspfeelog.setResult(0);
			}
			else
			{
				lfspfeelog.setResult(1);
			}
			//如果存储充值操作日志失败
			if(!balanceLogBiz.addBalanceLog(lfspfeelog)){
				EmpExecutionContext.error("SP账号充值日志记录保存失败！useridstr："+useridstr);
			}
			response.getWriter().write(String.valueOf(returnmsg));
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "SP账号充值异常！useridstr："+useridstr);
		}
	}

	/**
	 * 回收
	 * 
	 * @Title: recBalance
	 * @Description: TODO
	 * @param request
	 * @param response
	 * @return void
	 */
	public void recBalance(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			// 回收条数
			String countStr = request.getParameter("count");
			// 回收sp账号
			String useridstr = request.getParameter("useridstr");
			// 数字类型的回收条数
			Long count = null;
			// 判断回收条数合法性
			if(countStr != null && !"".equals(countStr.trim()))
			{
				count = Long.parseLong(countStr);
			}
			else
			{
				EmpExecutionContext.error("回收条数不合法，SP账号回收失败，count：" + count);
				response.getWriter().write("-1");
				return;
			}
			// 判断sp账号合法性
			if(useridstr == null || "".equals(useridstr.trim()))
			{
				EmpExecutionContext.error("sp账号不合法，SP账号回收失败，useridstr：" + useridstr);
				response.getWriter().write("-1");
				return;
			}
			
			// sp账号充值biz
			SpBalanceBiz balanceLogBiz = new SpBalanceBiz();
			// 取得当前登录信息
			Long lgguid=null;
			// 获取界面登录操作员企业编码
			String corpcode = "";
			//获取登录sysuser
			LfSysuser sysuser =balanceLogBiz.getCurrenUser(request);
			if(sysuser==null){
				EmpExecutionContext.error("SP账号充值回收,回收操作，session中获取当前登录对象出现异常");
				return;
			}
			corpcode=sysuser.getCorpCode();
			//判断企业编码获取
			if(corpcode==null||"".equals(corpcode)){
				EmpExecutionContext.error("SP账号充值回收,回收操作，session中获取企业编码出现异常");
				return;
			}
			lgguid = sysuser.getGuId();
			//判断企业编码获取
			if(lgguid==null){
				EmpExecutionContext.error("SP账号充值回收,回收操作，session中获取GUID出现异常,corpcode:"+corpcode);
				return;
			}
			
			//判断SP账号是否是属于本企业的
			if(useridstr != null && !"".equals(useridstr.trim()))
			{
				//解密
				useridstr=getDecryptValue(request, useridstr);
				//多企业才处理
				if(StaticValue.getCORPTYPE() ==1){
					boolean checkFlag=new CheckUtil().checkSysuserInCorp(sysuser, corpcode, useridstr, null);
					if(!checkFlag){
						EmpExecutionContext.error("SP账号充值回收,回收操作，检查操作员，企业编码，发送账号不通过，spuserid："+useridstr+",corpcode="+corpcode);
						return;
					}
				}
			}
			
			
			// 备注
			String addMark = request.getParameter("addMark");
			if(addMark == null || "".equals(addMark))
			{
				addMark = " ";
			}

			Integer returnmsg = 0;
			returnmsg = balanceLogBiz.recSpuserBalance(sysuser, useridstr, count);
			LfSpFeeLog lfspfeelog = new LfSpFeeLog();
			// 企业编码
			lfspfeelog.setCorpcode(sysuser.getCorpCode());
			// 充值数量
			lfspfeelog.setIcount(count * -1);
			// 备注
			lfspfeelog.setMemo(addMark);
			// 充值目的SPuser 充值SP账号
			lfspfeelog.setSpuser(useridstr);
			// 当前登录操作员id
			lfspfeelog.setUserid(sysuser.getUserId());
			// 设值充值结果
			if(returnmsg == 0)
			{
				lfspfeelog.setResult(0);
			}
			else
			{
				lfspfeelog.setResult(1);
			}
			//判断存储充值日志失败
			if(!balanceLogBiz.addBalanceLog(lfspfeelog)){
				EmpExecutionContext.error("回收日志记录保存失败！useridstr:"+useridstr);
			}
			response.getWriter().write(String.valueOf(returnmsg));
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "回收记录保存失败！");
		}
	}

	

	/**
	 * 批量充值
	 * 
	 * @Title: AddBalanceAll
	 * @Description: TODO
	 * @param request
	 * @param response
	 * @throws IOException
	 * @return void
	 * @author duanjl <duanjialin28827@163.com>
	 * @datetime 2015-11-24 上午11:02:43
	 */
	public void addBalanceAll(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		// sp账号 逗号隔开字符串
		String useridstrs = request.getParameter("useridstrs");
		// 当前操作员
		String lguserId = "";
		String lgguid = "";
		// 充值数量
		String addCount = request.getParameter("addCount");
		//充值类型  1充值  2回收
		String bltype = request.getParameter("bltype");
		String bltypestr="";
		try
		{
			
			if(bltype == null || "".equals(bltype))
			{
				response.getWriter().print("-1");
				EmpExecutionContext.error("SP账号批量操作失败，批量操作类型为空。bltype:" + bltype);
				return;
			}
			if("1".equals(bltype)){
				bltypestr="充值";
			}else if("2".equals(bltype)){
				bltypestr="回收";
			}else{
				response.getWriter().print("-1");
				EmpExecutionContext.error("SP账号批量操作失败，批量操作类型有误。bltype:" + bltype);
				return;
			}
			//判断传入的批量逗号隔开的sp账号是否为空
			if(useridstrs == null || "".equals(useridstrs))
			{
				response.getWriter().print("-1");
				EmpExecutionContext.error("SP账号批量"+bltypestr+"失败，批量sp账号字符串为空。useridstrs:" + useridstrs);
				return;
			}
			String[] useridstrarray = useridstrs.split(",");
			if(useridstrarray.length==0){
				response.getWriter().print("-1");
				EmpExecutionContext.error("SP账号批量"+bltypestr+"失败，批量sp账号字符串为空。useridstrs:" + useridstrs);
				return;
			}
			//解密
			for(int i=0;i<useridstrarray.length;i++){
				//解密
				useridstrarray[i]=getDecryptValue(request, useridstrarray[i]);
			}
			
			Long addcountl=0l;
			if(addCount == null || "".equals(addCount))
			{
				response.getWriter().print("-1");
				EmpExecutionContext.error("SP账号"+bltypestr+"失败，批量sp账号字符串为空。useridstrs:" + useridstrs);
				return;
			}
			try{
				addcountl=Long.parseLong(addCount);
			}catch (Exception e) {
				response.getWriter().print("-1");
				EmpExecutionContext.error("SP账号"+bltypestr+"失败，批量"+bltypestr+"数转换异常。addCount:" + addCount);
				return;
			}
			
			// sp账号充值biz
			SpBalanceBiz balanceBiz = new SpBalanceBiz();
			// 获取界面登录操作员企业编码
			String corpcode = "";
			//获取登录sysuser
			LfSysuser sysuser =balanceBiz.getCurrenUser(request);
			if(sysuser==null){
				EmpExecutionContext.error("SP账号充值回收,批量充值/回收操作，session中获取当前登录对象出现异常");
				return;
			}
			corpcode=sysuser.getCorpCode();
			//判断企业编码获取
			if(corpcode==null||"".equals(corpcode)){
				EmpExecutionContext.error("SP账号充值回收,批量充值/回收操作，session中获取企业编码出现异常");
				return;
			}
			lgguid = sysuser.getGuId()+"";
			//判断企业编码获取
			if(lgguid==null){
				EmpExecutionContext.error("SP账号充值回收,批量充值/回收操作，session中获取GUID出现异常,corpcode:"+corpcode);
				return;
			}
			lguserId=sysuser.getUserId()+"";
			//判断当前操作员ID获取
			if(lguserId==null||"".equals(lguserId)){
				EmpExecutionContext.error("SP账号充值回收,批量充值/回收操作，session中获取企业编码出现异常");
				return;
			}
			
			// 多企业才处理
			if(StaticValue.getCORPTYPE() == 1)
			{
				//循环遍历
				for (String useridstr : useridstrarray)
				{
					// 判断SP账号是否是属于本企业的
					if(useridstr != null && !"".equals(useridstr.trim()))
					{
						
							boolean checkFlag = new CheckUtil().checkSysuserInCorp(sysuser, corpcode, useridstr, null);
							if(!checkFlag)
							{
								EmpExecutionContext.error("SP账号充值回收,批量充值/回收，检查操作员，企业编码，发送账号不通过，spuserid：" + useridstr + ",corpcode=" + corpcode);
								return;
							}
					}
				}
			}
			
			
			String opContent = "";
			// 充值
			int returnNum = new SpBalanceBiz().addordellBalanceAll(sysuser, useridstrarray, addcountl, bltype);
			if(returnNum < 0)
			{
				opContent = "批量"+bltypestr+"失败，数量" + addcountl + "条。";
				// 添加操作日志
				spLog.logFailureString(sysuser.getName(), "SP账号充值/回收", StaticValue.OTHER, opContent, null, sysuser.getCorpCode());
				EmpExecutionContext.error("批量SP账号"+bltypestr+"失败！总条数:" + addcountl + "操作员:" + lguserId + "," + sysuser.getUserName() + "SPUSER:" + useridstrs 
						+ ",企业编码：" + sysuser.getCorpCode());
			}
			else
			{
				// 添加操作日志
				opContent = "批量"+bltypestr+"成功，数量" + addcountl + "条。";
				spLog.logSuccessString(sysuser.getName(), "SP账号充值/回收", StaticValue.OTHER, opContent, sysuser.getCorpCode());
				EmpExecutionContext.info("批量SP账号"+bltypestr+"成功,总条数:" + addcountl + " 操作员：" + lguserId + "," + sysuser.getUserName() + "SPUSER:" + useridstrs 
						+ "企业编码：" + sysuser.getCorpCode());
			}
			response.getWriter().print(returnNum);
		}
		catch (Exception e)
		{
			response.getWriter().print("-1");
			EmpExecutionContext.error(e, "批量"+bltypestr+"异常!lguserId: "+lguserId);
		}

	}

	/**
	 * 打开设值阀值页面
	 * 
	 * @param request
	 * @param response
	 */
	public void toSetAlarm(HttpServletRequest request, HttpServletResponse response) throws IOException
	{

		try
		{
			// Sp账号
			String useridstr = request.getParameter("useridstr");
			List<DynaBean> spfeedynlist = null;
			if(useridstr != null && !"".equals(useridstr))
			{
				//解密
				useridstr=getDecryptValue(request, useridstr);
				// sp账号充值biz
				SpBalanceBiz balanceLogBiz = new SpBalanceBiz();
				// 获取界面登录操作员企业编码
				String corpcode = "";
				//获取登录sysuser
				LfSysuser sysuser =balanceLogBiz.getCurrenUser(request);
				if(sysuser==null){
					EmpExecutionContext.error("SP账号充值回收,打开设值阀值操作，session中获取当前登录对象出现异常");
					return;
				}
				corpcode=sysuser.getCorpCode();
				//判断企业编码获取
				if(corpcode==null||"".equals(corpcode)){
					EmpExecutionContext.error("SP账号充值回收,打开设值阀值操作，session中获取企业编码出现异常");
					return;
				}
				
				//多企业才处理
				if(StaticValue.getCORPTYPE() ==1){
					boolean checkFlag=new CheckUtil().checkSysuserInCorp(sysuser, corpcode, useridstr, null);
					if(!checkFlag){
						EmpExecutionContext.error("SP账号充值回收,打开设值阀值，检查操作员，企业编码，发送账号不通过，spuserid："+useridstr+",corpcode="+corpcode);
						return;
					}
				}
				
				
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				conditionMap.put("userId", useridstr);
				List<Userfee> userfees = new BaseBiz().getByCondition(Userfee.class, conditionMap, null);
				Userfee userfee = null;
				if(userfees != null && userfees.size() > 0)
				{
					userfee = userfees.get(0);
					SpBalanceBiz blbiz = new SpBalanceBiz();
					// 获取Sp账号告警通知人集合
					spfeedynlist = blbiz.getSpfeealearmBySpuser(userfee.getUserId());
					request.setAttribute("userfee", userfee);
					request.setAttribute("spfeedynlist", spfeedynlist);
					request.getRequestDispatcher(this.empRoot + base + "/spb_setAlarm.jsp").forward(request, response);
				}
				else
				{
					request.setAttribute("userfee", null);
				}

			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "打开设置阀值页面异常！");
		}
	}

	/**
	 * 跳转到批量充值页面
	 * 
	 * @description
	 * @param request
	 * @param response
	 * @throws IOException
	 * @author liuxiangheng <xiaokanrensheng1012@126.com>
	 * @datetime 2016-10-14 下午04:06:45
	 */
	public void toAddBalanceAll(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		// 获取需要充值的 sp账号字符串
		String useridstrs = request.getParameter("useridstrs");
		try
		{
			if(useridstrs == null || "".equals(useridstrs.trim()))
			{
				EmpExecutionContext.error("批量充值选中的SP账号为空!useridstrs =" + useridstrs);
				return;
			}
			
			String[] useridstrarray = useridstrs.split(",");
			if(useridstrarray.length==0){
				response.getWriter().print("-1");
				EmpExecutionContext.error("SP账号进入批量失败，批量sp账号字符串为空。useridstrs:" + useridstrs);
				return;
			}
			//解密串
			String jmuseridstr="";
			//解密
			for(int i=0;i<useridstrarray.length;i++){
				if(i<useridstrarray.length-1){
					//解密
					jmuseridstr=jmuseridstr+getDecryptValue(request, useridstrarray[i])+",";
				}else{
					jmuseridstr=jmuseridstr+getDecryptValue(request, useridstrarray[i]);
				}
			}
			// 查询选中的相关账号信息列表
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("userId&in", jmuseridstr);
			List<Userdata> userdatas = baseBiz.getByCondition(Userdata.class, conditionMap, null);

			request.setAttribute("userdatas", userdatas);

			request.getRequestDispatcher(this.empRoot + base + "/spb_spBalanceAll.jsp").forward(request, response);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "跳转到批量充值页面异常");
		}
	}

	/**
	 * 设置短彩告警阀值
	 * 
	 * @Title: setAlarm
	 * @Description: TODO
	 * @param request
	 * @param response
	 * @throws IOException
	 * @return void
	 */
	public void setAlarm(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		// sp账号
		String useridstr = request.getParameter("useridstr");
		// 告警阀值
		String thresholdstr = request.getParameter("threshold");
		// 通知人姓名 通知人号码
		String arrNoticeDetailVal = request.getParameter("arrNoticeDetailVal");
		// 企业编码
		String lgcorpcode = request.getParameter("lgcorpcode");
		// 当前登录的操作员id
		//String lguserid = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String lguserid = SysuserUtil.strLguserid(request);

		int ucount = 0;
		try
		{
			// 判断传入sP账号合法性
			if(useridstr == null || "".equals(useridstr.trim()))
			{
				EmpExecutionContext.error("设置SP账号阀值时获取SP账号发生异常！useridstr=" + useridstr);
				return;
			}

			// 告警阀值
			Integer threshold;
			if(thresholdstr == null || "".equals(thresholdstr.trim()))
			{
				threshold = 0;
			}
			else
			{
				threshold = Integer.parseInt(thresholdstr);
			}
			
			Long lguserlong = 0l;
			// 定义SP账号充值管理biz
			SpBalanceBiz spbab = new SpBalanceBiz();
			// 获取界面登录操作员企业编码
			String corpcode = "";
			//获取登录sysuser
			LfSysuser sysuser =spbab.getCurrenUser(request);
			if(sysuser==null){
				EmpExecutionContext.error("SP账号充值回收,设置阀值，session中获取当前登录对象出现异常");
				return;
			}
			corpcode=sysuser.getCorpCode();
			//判断企业编码获取
			if(corpcode==null||"".equals(corpcode)){
				EmpExecutionContext.error("SP账号充值回收,设置阀值，session中获取企业编码出现异常");
				return;
			}
			lguserlong = sysuser.getUserId();
			//判断企业编码获取
			if(lguserlong==null){
				EmpExecutionContext.error("SP账号充值回收,设置阀值，session中获取GUID出现异常,corpcode:"+corpcode);
				return;
			}
			// 判断SP账号是否是属于本企业的
			if(useridstr != null && !"".equals(useridstr.trim()))
			{
				//解密
				useridstr=getDecryptValue(request, useridstr);
				if(StaticValue.getCORPTYPE() == 1)
				{
					boolean checkFlag = new CheckUtil().checkSysuserInCorp(sysuser, corpcode, useridstr, null);
					if(!checkFlag)
					{
						EmpExecutionContext.error("SP账号充值回收,设置阀值，检查操作员，企业编码，发送账号不通过，spuserid：" + useridstr + ",corpcode=" + corpcode);
						return;
					}
				}
			}
			
			// 获取告警次数
			Integer alermcount = spbab.getAlermCountBySpuser(useridstr);
			//如果告警次数大于0 则需判断阀值是否已经小于余额了
			if(alermcount>0){
				//查出sp账号余额
				long balancecount=spbab.getSendBalanceBySpuser(useridstr);
				//如果余额大于阀值，则将告警次数清零
				if(balancecount>threshold){
					alermcount=0;
				}
			}
			List<LfSpFeeAlarm> spfeealarms = new ArrayList<LfSpFeeAlarm>();
			LfSpFeeAlarm spfeealarm = null;
			if(arrNoticeDetailVal != null && !"".equals(arrNoticeDetailVal))
			{
				String[] simrow = arrNoticeDetailVal.split("@");
				for (String srow : simrow)
				{
					// 实例化通知人信息
					spfeealarm = new LfSpFeeAlarm();
					// 设置告警次数
					spfeealarm.setAlarmedcount(alermcount);
					// 企业编码
					spfeealarm.setCorpcode(lgcorpcode);
					// 更新记录的操作员userid
					spfeealarm.setModiuserid(lguserlong);
					// 设值sp账号
					spfeealarm.setSpuser(useridstr);
					// 创建时间
					spfeealarm.setCreatetime(new Timestamp(System.currentTimeMillis()));
					// 更新时间
					spfeealarm.setUpdatetime(new Timestamp(System.currentTimeMillis()));
					String[] coln = srow.split(",");
					if(coln.length == 2)
					{
						if(coln[1] != null && coln[1].length() != 0)
						{
							// 通知人电话
							spfeealarm.setAlarmphone(coln[1]);
						}
						else
						{
							EmpExecutionContext.error("设置SP账号阀值时获取通知人信息发生异常！arrNoticeDetailVal:" + arrNoticeDetailVal);
							return;
						}
						if(coln[0] != null && coln[0].trim().length() > 0)
						{
							// 通知人姓名
							spfeealarm.setNoticename(coln[0]);
						}
						else
						{
							// 通知人姓名
							spfeealarm.setNoticename(" ");
						}
						spfeealarms.add(spfeealarm);
					}
					else
					{
						EmpExecutionContext.error("设置SP账号阀值时获取通知人信息发生异常！arrNoticeDetailVal:" + arrNoticeDetailVal);
						return;
					}
				}
			}
			else
			{
				EmpExecutionContext.error("设置SP账号阀值时获取通知人信息为空！arrNoticeDetailVal:" + arrNoticeDetailVal);
				return;
			}
			// 通知人信息LIST判断是否为空
			if(spfeealarms.size() == 0)
			{
				EmpExecutionContext.error("设置SP账号阀值时获取通知人信息为空！arrNoticeDetailVal:" + arrNoticeDetailVal);
				return;
			}
			ucount = spbab.setAlarmAndNotice(useridstr, lgcorpcode, threshold, spfeealarms);
			if(ucount > 0)
			{
				if(getSessionSysUser(request) != null)
				{
					EmpExecutionContext.info("SP账号充值/回收", lgcorpcode, lguserid + "", getSessionSysUser(request).getUserName(), "设置SP账号阀值成功（SP账号，阀值，通知人姓名号码串）（" + useridstr + "，" + thresholdstr + "，" + arrNoticeDetailVal + "）", "OTHER");
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置SP账号阀值出现异常！");
			ucount = 0;
		}
		finally
		{
			response.getWriter().print(ucount);
		}
	}


	private LfSysuser getSessionSysUser(HttpServletRequest request)
	{
		LfSysuser lfSysuser = null;
		try
		{
			Object obj = request.getSession(false).getAttribute("loginSysuser");
			if(obj != null)
			{
				lfSysuser = (LfSysuser) obj;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "记录操作日志异常,session为空！");
		}
		return lfSysuser;
	}

	/**
	 * @description 记录操作成功日志
	 * @param request
	 * @param modName
	 *        模块名称
	 * @param opContent
	 *        操作详情
	 * @param opType
	 *        操作类型 ADD UPDATE DELETE GET OTHER
	 * @datetime 2015-3-3 上午11:29:50
	 */
	public void opSucLog(HttpServletRequest request, String modName, String opContent, String opType)
	{
		LfSysuser lfSysuser = null;
		try
		{
			Object obj = request.getSession(false).getAttribute("loginSysuser");
			if(obj == null) return;
			lfSysuser = (LfSysuser) obj;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "记录操作日志异常,session为空！");
		}
		if(lfSysuser!=null){
			EmpExecutionContext.info(modName, lfSysuser.getCorpCode(), String.valueOf(lfSysuser.getUserId()), lfSysuser.getUserName(), opContent, opType);
		}
	}

}
