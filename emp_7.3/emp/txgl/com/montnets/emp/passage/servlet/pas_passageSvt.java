package com.montnets.emp.passage.servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.montnets.emp.common.biz.AccountInfoSyncBiz;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.ParamsEncryptOrDecrypt;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.gateway.AgwSpBind;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.passage.biz.GateBiz;
import com.montnets.emp.passage.biz.PassageBiz;
import com.montnets.emp.security.context.ErrorLoger;
import com.montnets.emp.security.wgmsgconfig.WgMsgConfigBiz;
import com.montnets.emp.servmodule.txgl.biz.PageFieldConfigBiz;
import com.montnets.emp.servmodule.txgl.entity.AgwAccount;
import com.montnets.emp.servmodule.txgl.entity.LfPageField;
import com.montnets.emp.servmodule.txgl.entity.XtGateQueue;
import com.montnets.emp.util.MD5;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.SuperOpLog;


/**
 * 通道管理
 * @project p_txgl
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2015-3-4 下午03:22:10
 * @description
 */
@SuppressWarnings("serial")
public class pas_passageSvt extends BaseServlet {
	final ErrorLoger errorLoger = new ErrorLoger();
	final WgMsgConfigBiz wb = new WgMsgConfigBiz();
	//操作模块
	final String opModule=StaticValue.GATE_CONFIG;
	//操作用户
	final String opSper = StaticValue.OPSPER;
	
	final String empRoot="txgl";
	
	final String basePath="/passage";
	final BaseBiz baseBiz = new BaseBiz();
	final int corpType = StaticValue.getCORPTYPE();
	
	final GateBiz gateBiz=new GateBiz();
	
	/**
	 * 通道管理查询
	 * 
	 * @param request
	 * @param response
	 */
	public void find(HttpServletRequest request, HttpServletResponse response)
	{
		long stime = System.currentTimeMillis();
		List<XtGateQueue> gateList = null;
		//select框显示下拉列表用
		List<XtGateQueue> xtList = null;
		PageInfo pageInfo = new PageInfo();
		boolean isFirstEnter;
		//获取系统定义的短彩类型值
		PageFieldConfigBiz gcBiz = new PageFieldConfigBiz();
		List<LfPageField> pagefileds = gcBiz.getPageFieldById("100003");
		//通道号码
		String spgate = null;
		//运营商类型
		String spisuncm = null;
		//通道类型
		String routeFlag = null;
		//通道名称
		String gatename = null;
		//签名
		String cardname = null;
		boolean isBack = request.getParameter("isback")==null?false:true;//是否返回操作
		try {
			HttpSession session = request.getSession(false);
			if(isBack){
				pageInfo = (PageInfo) session.getAttribute("pas_pageinfo");
				spgate = session.getAttribute("pas_spgate")==null?"":String.valueOf(session.getAttribute("pas_spgate"));
				spisuncm = session.getAttribute("pas_spisuncm")==null?"":String.valueOf(session.getAttribute("pas_spisuncm"));
				routeFlag= session.getAttribute("pas_routeFlag")==null?"":String.valueOf(session.getAttribute("pas_routeFlag"));
				gatename = session.getAttribute("pas_gatename")==null?"":String.valueOf(session.getAttribute("pas_gatename"));
				cardname = session.getAttribute("pas_cardname")==null?"":String.valueOf(session.getAttribute("pas_cardname"));
				request.setAttribute("spgate",spgate);
				request.setAttribute("spisuncm",spisuncm);
				request.setAttribute("routeFlag",routeFlag);
				request.setAttribute("gatename",gatename);
				request.setAttribute("cardname",cardname);
			}else{
				isFirstEnter = pageSet(pageInfo,request);
				if (!isFirstEnter) {
					spgate = request.getParameter("spgate");
					spisuncm = request.getParameter("spisuncm");
					routeFlag=request.getParameter("routeFlag");
					gatename = request.getParameter("getename");
					cardname = request.getParameter("cardname");
				}
			}
			session.setAttribute("pas_pageinfo", pageInfo);
			session.setAttribute("pas_spgate", spgate);
			session.setAttribute("pas_spisuncm", spisuncm);
			session.setAttribute("pas_routeFlag", routeFlag);
			session.setAttribute("pas_gatename", gatename);
			session.setAttribute("pas_cardname", cardname);
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			if(!isInvalidString(spgate)){
				conditionMap.put("spgate", spgate);
			}
			if(!isInvalidString(spisuncm)){
				conditionMap.put("spisuncm", spisuncm);
			}
			if(!isInvalidString(routeFlag))
			{
				conditionMap.put("gateType", routeFlag);
			}
			if(!isInvalidString(gatename)){
				conditionMap.put("gateName", gatename);
			}
			if(!isInvalidString(cardname)){
				conditionMap.put("signstr", cardname);
			}
			String corpCode =request.getParameter("lgcorpcode");
			PassageBiz passageBiz = new PassageBiz();
			if("100000".equals(corpCode) || corpType==0)
			{
				
				LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
//				orderbyMap.put("spgate",StaticValue.ASC);
				orderbyMap.put("id", StaticValue.DESC);
				xtList = baseBiz.getByCondition(XtGateQueue.class, null, null,
						orderbyMap);
				gateList = passageBiz.getByCondition(conditionMap,pageInfo);
			}else
			{
				gateList = passageBiz.getGateInfoByCorp(corpCode,conditionMap, pageInfo);
				xtList = passageBiz.getGateInfoByCorp(corpCode, null, null);
			}
			Set<String> spgaes=new HashSet<String>();
			if(xtList!=null&&xtList.size()>0){
				for(XtGateQueue xg :xtList){
					spgaes.add(xg.getSpgate());
				}
			}
			//加密后的集合
			Map<Long, String> keyIdMap = new HashMap<Long, String>();
			//ID加密
			if(gateList != null && gateList.size() > 0)
			{
				//加密对象
				ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
				//加密对象不为空
				if(encryptOrDecrypt != null)
				{
					boolean result = encryptOrDecrypt.batchEncryptToMap(gateList, "Id", keyIdMap);
					if(!result)
					{
						EmpExecutionContext.error("查询通道管理列表，参数加密失败。corpCode:"+corpCode);
						throw new Exception("查询通道管理列表，参数加密失败。");
					}
				}
				else
				{
					EmpExecutionContext.error("查询通道管理列表，从session中获取加密对象为空！");
					gateList.clear();
					throw new Exception("查询通道管理列表，获取加密对象失败。");
				}
			}
			request.setAttribute("mrXtList", spgaes);
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("gateList", gateList);
			request.setAttribute("keyIdMap", keyIdMap);
			request.getSession(false).setAttribute("pas_pagefileds", pagefileds);
			request.getRequestDispatcher(this.empRoot + this.basePath + "/pas_passage.jsp")
				.forward(request, response);
			DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			String sDate = sdf.format(stime);
			setLog(request,"通道管理","("+sDate+")查询",StaticValue.GET);
		} catch (Exception e) {
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"通道管理查询异常！"));
			request.setAttribute("findresult", "-1");
			request.setAttribute("pageInfo", pageInfo);
			try {
				request.getSession(false).setAttribute("pas_pagefileds", pagefileds);
				request.getRequestDispatcher(this.empRoot + this.basePath +"/pas_passage.jsp")
				.forward(request, response);
			} catch (ServletException e1) {				
				EmpExecutionContext.error(errorLoger.getErrorLog(e1,"通道管理查询异常！"));
			} catch (IOException e1) {				
				EmpExecutionContext.error(errorLoger.getErrorLog(e1,"通道管理查询异常！"));
			}
		}
	}
	
	/**
	 * 检查通道运营商
	 * @param request
	 * @param response
	 */
	public void checkGateNum(HttpServletRequest request, HttpServletResponse response)
	{
		String spgate = request.getParameter("spgate");
		String spiscumn = request.getParameter("spiscumn");
		String gatename = request.getParameter("gatename");
		String gatetype = request.getParameter("gatetype");
		String updateorinsert = request.getParameter("updateorinsert");
		String id = request.getParameter("id");
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			if("1".equals(updateorinsert)){
				GateBiz gb=new GateBiz();
				if(!gb.isGateExist(spgate, spiscumn,gatetype,id))
				{
					if(gb.isGateNameExist(gatename,gatetype,id))
					{
						writer.print("nameExist");
					}
					else
					{
						writer.print("false");
					}
				}else
				{
					writer.print("numExist");
				}
			}else{
				if(!wb.isGateExist(spgate, spiscumn,gatetype))
				{
					if(wb.isGateNameExist(gatename,gatetype))
					{
						writer.print("nameExist");
					}
					else
					{
						writer.print("false");
					}
				}else
				{
					writer.print("numExist");
				}
			}
			
		} catch (Exception e) {
			if(writer!=null){
				writer.print("error");
			}
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"检查通道运营商异常！"));
		}
	
	}
	
	
	

	
	/**
	 * 新建通道
	 * 
	 * @param request
	 * @param response
	 */
	public void add(HttpServletRequest request, HttpServletResponse response)
	{
		String updateorinsert = request.getParameter("updateorinsert");
		XtGateQueue gate = null;
		PrintWriter writer = null;
		String opUser = "";
		String corpcode = "";
		//日志内容
		String opContent = null;
		//修改前数据
		StringBuffer oldStr=new StringBuffer("");
		//修改后数据
		StringBuffer newStr=new StringBuffer("");
		//修改字段字符串
		String updateContent="";
		try
		{
			LfSysuser lfSysuser=(LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			corpcode=lfSysuser.getCorpCode();
			opUser=lfSysuser.getUserName();
			opUser = opUser==null?"":opUser;
			corpcode=corpcode==null?"":corpcode;
			writer = response.getWriter();
			String spgate="";
			String spisuncm="";
			String gatetype="";
			if("1".equals(updateorinsert)){
				//String id=request.getParameter("id");
				String id;
				String keyId=request.getParameter("keyId");
				//加密对象
				ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
				//加密对象不为空
				if(encryptOrDecrypt != null)
				{
					//解密
					id = encryptOrDecrypt.decrypt(keyId);
					if(id == null)
					{
						EmpExecutionContext.error("修改通道信息，参数解密码失败，keyId:"+keyId);
						throw new Exception("修改通道信息，参数解密码失败。");
					}
				}
				else
				{
					EmpExecutionContext.error("修改通道信息，从session中获取加密对象为空！");
					throw new Exception("修改通道信息，获取加密对象失败。");
				}
				gate=baseBiz.getById(XtGateQueue.class, id);
				//修改前通道类型，运营商，通道名称，通道号码，最大扩展位数，短信签名，签名位置，由运营商加签名，支持长短信，按长短信拆分，支持整条提交，单条短信字数，费率
				if(gate!=null){
					oldStr.append(gate.getGateType()).append("，").append(gate.getSpisuncm()).append("，").append(gate.getGateName()).append("，").append(gate.getSpgate())
					.append("，").append(gate.getSublen()).append("，").append(gate.getSignstr()).append("，").append(gate.getGateprivilege()).append("，")
					.append(gate.getSignDropType()).append("，").append(gate.getLongSms()).append("，").append(gate.getSplitRule())
					.append("，").append(gate.getEndSplit()).append("，").append(gate.getSingleLen()).append("，").append(gate.getFee())
					.append("，").append(gate.getEsplitmaxwd()).append("/").append(gate.getEsplitenmaxwd());
					spgate=gate.getSpgate();
					spisuncm=gate.getSpisuncm()!=null?gate.getSpisuncm().toString():"";
					gatetype=gate.getGateType()!=null?gate.getGateType().toString():"";
				}
			}else{
				gate=new XtGateQueue();
				gate.setSpgate(request.getParameter("spgate"));
			}
			gate.setGateType(Integer.valueOf(request.getParameter("gatetype")));
			gate.setGateName(request.getParameter("gatename"));
			gate.setAreaType(Integer.valueOf(request.getParameter("areaType")));
			gate.setSpisuncm(Integer.valueOf(request.getParameter("spiscumn")));
			gate.setPortType(Integer.valueOf(request.getParameter("porttype")));
			gate.setStatus(Integer.valueOf(request.getParameter("status")));
			boolean isGW = gate.getSpisuncm()-5==0;//是否国外运营商类型
			String isSupportEn = request.getParameter("isSupportEn");
			if(gate.getGateType()==1){
				String signType = request.getParameter("signtype");//1-固定长度；0-自动计算
				String signdroptype = request.getParameter("signdroptype");
				String gateprivilege=request.getParameter("gateprivilege");
				if("1".equals(updateorinsert)){
					int gp=gate.getGateprivilege();
					String bytstr=Integer.toBinaryString(gp);
					String zerostr="";
					for(int i=0;i<32-bytstr.length();i++){
						zerostr=zerostr+"0";
					}
					String gpstr=zerostr+bytstr;
					StringBuffer b=new StringBuffer(gpstr);
					b.setCharAt(29,gateprivilege.charAt(0));
					b.setCharAt(30,isSupportEn.charAt(0));//是否支持英文短信
					if(gate!=null&&gate.getGateprivilege()!=null){
						if(gate.getGateprivilege().intValue()!=Integer.valueOf(b.toString(), 2).intValue()){
							//签名位置
							String priname="";
							String upirname="";
							if("0".equals(gateprivilege)){
								priname="后置";
								upirname="前置";
							}else{
								priname="前置";
								upirname="后置";
							}
							//如果有变化则记录修改字符串
							updateContent=updateContent+" 原始签名位置："+priname+" 修改后签名位置："+upirname;
						}
					}
					gate.setGateprivilege(Integer.valueOf(b.toString(), 2));
				}else{
					String zerostr="";
					for(int i=0;i<32;i++){
						zerostr=zerostr+"0";
					}
					StringBuffer b=new StringBuffer(zerostr);
					b.setCharAt(29,gateprivilege.charAt(0));
					b.setCharAt(30,isSupportEn.charAt(0));
					gate.setGateprivilege(Integer.valueOf(b.toString(), 2));
				}
				gate.setLongSms(Long.valueOf(request.getParameter("longsms")));
				gate.setSplitRule(Integer.valueOf(request.getParameter("splitRule")));
				gate.setEndSplit(Integer.valueOf(request.getParameter("endsplit")));
				gate.setEachSign(Integer.valueOf(request.getParameter("eachSign")));
//				gate.setMaxWords(Integer.valueOf(request.getParameter("maxwords")));
				gate.setRiseLevel(Integer.valueOf(request.getParameter("riselevel")));
				
				if("1".equals(updateorinsert)){
					if(gate.getSignDropType()!=null){
						if(gate.getSignDropType()==1||gate.getSignDropType()==0){
							if(!"1".equals(signdroptype)){
								updateContent=updateContent+" 原始由运营商加签名从是修改为否 ";
							}
						}else{
							if(!"0".equals(signdroptype)){
								updateContent=updateContent+" 原始由运营商加签名从否修改为是 ";
							}
						}
					}
				}
				if("1".equals(signType))
				{
					gate.setSignDropType(0);
				}else if("1".equals(signdroptype))
				{
					gate.setSignDropType(1);
				}else if("0".equals(signdroptype))
				{
					gate.setSignDropType(2);
				}
				String singlelen = null;
				String signstr =  null;
				String signlen = null;
				if(isGW){
					singlelen = request.getParameter("cnsinglelen");
					signstr = request.getParameter("cnSign");
					signlen = request.getParameter("cnsignlen");
					if("1".equals(isSupportEn)){
						String ensinglelen = request.getParameter("ensinglelen");
						String enSign = request.getParameter("enSign");
						String ensignlen = request.getParameter("ensignlen");
						gate.setEnsinglelen(Integer.valueOf(ensinglelen));
						gate.setEnsignstr(enSign==null||"".equals(enSign)?" ":enSign);
						gate.setEnsignlen("0".equals(signType)?0:Integer.valueOf(ensignlen));
						gate.setEnmultilen1(gate.getEnsinglelen()-160==0?153:gate.getEnsinglelen()-6);
						gate.setEnmultilen2(gate.getEnmultilen1()-("0".equals(signType)?gate.getEnsignstr().trim().replaceAll("[\\[\\]\\|\\^\\{\\}\\~\\\\]", "**").length():gate.getEnsignlen()));
					}else{
						gate.setEnsinglelen(160);
						gate.setEnsignstr(" ");
						gate.setEnsignlen(10);
						gate.setEnmultilen1(153);
						gate.setEnmultilen2(143);
					}
                    gate.setMaxWords(360);      //国外通道中英文最大长度
                    gate.setEnmaxwords(720);
				}else{
					singlelen = request.getParameter("singlelen");
					signstr =  request.getParameter("signstr");
					signlen = request.getParameter("signlen");
                    gate.setMaxWords(1000);    //国内通道中英文最大长度
                    gate.setEnmaxwords(2000);
				}
				gate.setSingleLen(Integer.valueOf(singlelen));
				//由于oracle数据库短信签名为空字符串时，插入报错，这里做特殊处理将空字符串改成空格
				gate.setSignstr(signstr==null||"".equals(signstr)?" ":signstr);
				gate.setSignlen("0".equals(signType)?0:Integer.valueOf(signlen));
				gate.setMultilen1(gate.getSingleLen()-3);
				gate.setMultilen2(gate.getMultilen1()-("0".equals(signType)?gate.getSignstr().trim().length():gate.getSignlen()));
				Integer signfixlen = gate.getSignlen();
				if("0".equals(signType))
				{
//					gate.setSignlen(0);
//					gate.setEnsignlen(0);
					signfixlen = 0;
				}
				gate.setSignType(Integer.valueOf(signType));
				gate.setSignFixLen(signfixlen);
			}else{
				if(isGW){
					String signstr = request.getParameter("cnSign");
					gate.setSignstr(signstr==null||"".equals(signstr)?" ":signstr);
					String enSign = request.getParameter("enSign");
					gate.setEnsignstr(enSign==null||"".equals(enSign)?" ":enSign);
				}else{
					String signstr =  request.getParameter("signstr");
					gate.setSignstr(signstr==null||"".equals(signstr)?" ":signstr);
				}
			}
			//后拆分中文最大字数
			Integer espLitMaxWd = 360;
			if(request.getParameter("esplitmaxwd") != null &&  !"".equals(request.getParameter("esplitmaxwd")))
			{
				espLitMaxWd = Integer.parseInt(request.getParameter("esplitmaxwd"));
			}
			gate.setEsplitmaxwd(espLitMaxWd);
			//后拆分英文最大字数
			Integer espLitEnMaxWd = 720;
			if(request.getParameter("esplitenmaxwd") != null &&  !"".equals(request.getParameter("esplitenmaxwd")))
			{
				espLitEnMaxWd = Integer.parseInt(request.getParameter("esplitenmaxwd"));
			}
			gate.setEsplitenmaxwd(espLitEnMaxWd);
			String fee=request.getParameter("fee");
			if(fee==null||"".equals(fee)){
				fee="0";
			}
			gate.setFee(Double.valueOf(fee));
			String sublenStr = request.getParameter("sublen");
			gate.setSublen(sublenStr==null||"".equals(sublenStr.trim())?0:Integer.valueOf(sublenStr));
			String result="";
			//运营商类型
			String spnumname="";
			//短彩类型
			String gatetypename="";
			if(gate.getSpisuncm()!=null&&gate.getSpisuncm()==0){
				spnumname="移动";
			}else if (gate.getSpisuncm()!=null&&gate.getSpisuncm()==1){
				spnumname="联通";
			}else if (gate.getSpisuncm()!=null&&gate.getSpisuncm()==21){
				spnumname="电信";
			}else if (gate.getSpisuncm()!=null&&gate.getSpisuncm()==5){
				spnumname="国外";
			}
			
			if(gate.getGateType()!=null&&gate.getGateType()==1){
				gatetypename="短信";
			}else if(gate.getGateType()!=null&&gate.getGateType()==2){
				gatetypename="彩信";
			}
			SuperOpLog opLog = new SuperOpLog();
			if("1".equals(updateorinsert)){
				if(new GateBiz().updatespgate(gate, spgate, spisuncm, gatetype)){
					result="utrue";
					//添加成功操作日志
					//spLog.logSuccessString(opUser, opModule, opType, opContent,corpcode);
					//EmpExecutionContext.error("企业：" +corpcode+",操作员：" +opUser+opContent+"成功");
					//增加操作日志
					//修改后通道类型，运营商，通道名称，通道号码，最大扩展位数，短信签名，签名位置，由运营商加签名，支持长短信，按长短信拆分，支持整条提交，单条短信字数，费率
					newStr.append(gate.getGateType()).append("，").append(gate.getSpisuncm()).append("，").append(gate.getGateName()).append("，").append(gate.getSpgate())
					.append("，").append(gate.getSublen()).append("，").append(gate.getSignstr()).append("，").append(gate.getGateprivilege()).append("，")
					.append(gate.getSignDropType()).append("，").append(gate.getLongSms()).append("，").append(gate.getSplitRule())
					.append("，").append(gate.getEndSplit()).append("，").append(gate.getSingleLen()).append("，").append(gate.getFee())
					.append("，").append(gate.getEsplitmaxwd()).append("/").append(gate.getEsplitenmaxwd());
					opContent="修改通道成功[通道类型，运营商，通道名称，通道号码，最大扩展位数，短信签名，签名位置，由运营商加签名，支持长短信，按长短信拆分，支持整条提交，单条短信字数，费率，后拆分最大字数（中文/英文）]"
						+"（"+oldStr+"-->"+newStr+"）";
					setLog(request, "通道管理", opContent, StaticValue.UPDATE);
					opLog.logSuccessString(opUser, opModule, StaticValue.UPDATE, opContent,corpcode);
				}else{
					result="ufalse";
					//EmpExecutionContext.error("企业：" +corpcode+",操作员：" +opUser+opContent+"失败");
					opContent="修改通道失败[通道类型，运营商，通道名称，通道号码，最大扩展位数，短信签名，签名位置，由运营商加签名，支持长短信，按长短信拆分，支持整条提交，单条短信字数，费率，后拆分最大字数（中文/英文）]"
						+"（"+oldStr+"-->"+newStr+"）";
					setLog(request, "通道管理", opContent, StaticValue.UPDATE);
					opLog.logFailureString(opUser, opModule, StaticValue.UPDATE, opContent,null,corpcode);
				}
			}else{
				//opContent="新建通道（通道号："+(gate.getSpgate()!=null?gate.getSpgate():"")+",运营商类型："+spnumname+",通道类型："+gatetypename+"）新建";
				boolean isGateExist = false;
				if(gate.getSpgate() != null && gate.getSpisuncm() != null && gate.getGateType() != null)
				{
					isGateExist = gateBiz.isGateExist(gate.getSpgate(), String.valueOf(gate.getSpisuncm()),String.valueOf(gate.getGateType()));
				}
				//通道已存在
				if(isGateExist)
				{
					result="itemExists";
				}
				else
				{
					if(new BaseBiz().addObj(gate)){
						result="true";
						//EmpExecutionContext.error("企业：" +corpcode+",操作员：" +opUser+opContent+"成功");
						//增加操作日志
						newStr.append(gate.getGateType()).append("，").append(gate.getSpisuncm()).append("，").append(gate.getGateName()).append("，").append(gate.getSpgate())
						.append("，").append(gate.getSublen()).append("，").append(gate.getSignstr()).append("，").append(gate.getGateprivilege()).append("，")
						.append(gate.getSignDropType()).append("，").append(gate.getLongSms()).append("，").append(gate.getSplitRule())
						.append("，").append(gate.getEndSplit()).append("，").append(gate.getSingleLen()).append("，").append(gate.getFee())
						.append("，").append(gate.getEsplitmaxwd()).append("/").append(gate.getEsplitenmaxwd());
						opContent="新建通道成功[通道类型，运营商，通道名称，通道号码，最大扩展位数，短信签名，签名位置，由运营商加签名，支持长短信，按长短信拆分，支持整条提交，单条短信字数，费率，后拆分最大字数（中文/英文）]"
						+"（"+newStr+"）";
						setLog(request, "通道管理", opContent, StaticValue.ADD);
						opLog.logSuccessString(opUser, opModule, StaticValue.ADD, opContent,corpcode);
					}else{
						result="false";
						//EmpExecutionContext.error("企业：" +corpcode+",操作员：" +opUser+opContent+"失败");
						opContent="新建通道失败[通道类型，运营商，通道名称，通道号码，最大扩展位数，短信签名，签名位置，由运营商加签名，支持长短信，按长短信拆分，支持整条提交，单条短信字数，费率，后拆分最大字数（中文/英文）]"
							+"（"+newStr+"）";
						setLog(request, "通道管理", opContent, StaticValue.ADD);
						opLog.logFailureString(opUser, opModule, StaticValue.ADD, opContent,null,corpcode);
					}
				}
			}
			
			writer.print(result);
		}catch (Exception e) {
			writer.print("error");
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"新建通道异常！"));
		}
	}
	
	
	
	/**
	 * 修改通道
	 * @param request
	 * @param response
	 */
	public void update(HttpServletRequest request, HttpServletResponse response)
	{
		PrintWriter writer = null;
		try
		{
			writer = response.getWriter();
			String spgate=request.getParameter("uspgate");
			String signstr=request.getParameter("signstr");
			String singlelen=request.getParameter("singlelen");
			String signlen=request.getParameter("signlen");
			String signmode=request.getParameter("signmode");
			String maxword=request.getParameter("maxword");
			String spisuncm=request.getParameter("spisuncm");
			GateBiz gatebz=new GateBiz();
			gatebz.changeSinglen(spgate, signstr, singlelen, signlen, signmode, maxword, spisuncm);			
			writer.print("true");
		}catch (Exception e) {
			if(writer!=null){
				writer.print("error");
			}
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"新建通道异常！"));
		}
	}
	
	
	/**
	 * 修改通道状
	 * @param request
	 * @param response
	 */
	public void changeState(HttpServletRequest request, HttpServletResponse response)
	{
		String opContent = "修改通道状态";
		//Long id=Long.valueOf(request.getParameter("id"));
		String corpCode =request.getParameter("lgcorpcode");
		String lgusername = request.getParameter("lgusername");
		Integer statu;
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			String id;
			String keyId=request.getParameter("keyId");
			//加密对象
			ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
			//加密对象不为空
			if(encryptOrDecrypt != null)
			{
				//解密
				id = encryptOrDecrypt.decrypt(keyId);
				if(id == null)
				{
					EmpExecutionContext.error("修改通道状态，参数解密码失败，keyId:"+keyId);
					throw new Exception("修改通道状态，参数解密码失败。");
				}
			}
			else
			{
				EmpExecutionContext.error("修改通道状态，从session中获取加密对象为空！");
				throw new Exception("修改通道状态，获取加密对象失败。");
			}
			
			statu = wb.changeState(Long.parseLong(id));
			SuperOpLog spLog = new SuperOpLog();
			String opUser = lgusername==null?"":lgusername;
			if(statu==null)
			{
				spLog.logFailureString(opUser, opModule, StaticValue.OTHER, opContent+opSper, null,corpCode);
				writer.print("error");
				opContent ="修改通道状态失败";
				//增加操作日志
				setLog(request, "通道管理", opContent, StaticValue.UPDATE);
			}else
			{
				if(statu==0)
				{
					opContent ="修改通道状态为\"激活\"成功";
				}else if(statu==1)
				{
					opContent ="修改通道状态为\"失效\"成功";
				}
				spLog.logSuccessString(opUser, opModule, StaticValue.OTHER, opContent, corpCode);
				writer.print(statu);
				//增加操作日志
				setLog(request, "通道管理", opContent, StaticValue.UPDATE);
			}
		} catch (Exception e) {
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"修改通道状异常！"));
		}
	}
	
	/**
	 * 获取通道详情
	 * @param request
	 * @param response
	 */
	public void getDetail(HttpServletRequest request, HttpServletResponse response)
	{
		try{
			//String id=request.getParameter("id");
			String id;
			String keyId=request.getParameter("keyId");
			//加密对象
			ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
			//加密对象不为空
			if(encryptOrDecrypt != null)
			{
				//解密
				id = encryptOrDecrypt.decrypt(keyId);
				if(id == null)
				{
					EmpExecutionContext.error("获取通道详情，参数解密码失败，keyId:"+keyId);
					throw new Exception("获取通道详情，参数解密码失败。");
				}
			}
			else
			{
				EmpExecutionContext.error("获取通道详情，从session中获取加密对象为空！");
				throw new Exception("获取通道详情，获取加密对象失败。");
			}
			XtGateQueue gate = new BaseBiz().getById(XtGateQueue.class, id);
//			int gpvelege=gate.getGateprivilege();
//			if((gpvelege&4)==4){
//				gate.setGateprivilege(1);
//			}else{
//				gate.setGateprivilege(0);
//			}
//			String bytestr=Integer.toBinaryString(gpvelege);
//			String zerostr="";
//			for(int i=0;i<32-bytestr.length();i++){
//				zerostr=zerostr+"0";
//			}
//			String gpstr=zerostr+bytestr;
//			String gpchar=gpstr.charAt(29)+"";
//			gate.setGateprivilege(Integer.parseInt(gpchar));
			request.setAttribute("gate", gate);
			request.getRequestDispatcher(this.empRoot + this.basePath + "/pas_getPassage.jsp")
				.forward(request, response);
		} catch (Exception e) {
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"获取通道详情异常！"));
		}
	}
	
	
	
	/**
	 * 跳转至修改页面
	 * @param request
	 * @param response
	 */
	public void toEdit(HttpServletRequest request, HttpServletResponse response)
	{
		//String id=request.getParameter("id");
		try{
			String id;
			String keyId=request.getParameter("keyId");
			//加密对象
			ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
			//加密对象不为空
			if(encryptOrDecrypt != null)
			{
				//解密
				id = encryptOrDecrypt.decrypt(keyId);
				if(id == null)
				{
					EmpExecutionContext.error("跳转至修改通道页面，参数解密码失败，keyId:"+keyId);
					throw new Exception("跳转至修改通道页面，参数解密码失败。");
				}
			}
			else
			{
				EmpExecutionContext.error("跳转至修改通道页面，从session中获取加密对象为空！");
				throw new Exception("跳转至修改通道页面，获取加密对象失败。");
			}
			
			XtGateQueue gate = new BaseBiz().getById(XtGateQueue.class, id);
			
//			int gpvelege=gate.getGateprivilege();
//			if((gpvelege&4)==4){
//				gate.setGateprivilege(1);
//			}else{
//				gate.setGateprivilege(0);
//			}
//			String bytestr=Integer.toBinaryString(gpvelege);
//			String zerostr="";
//			for(int i=0;i<32-bytestr.length();i++){
//				zerostr=zerostr+"0";
//			}
//			String gpstr=zerostr+bytestr;
//			String gpchar=gpstr.charAt(29)+"";
//			gate.setGateprivilege(Integer.parseInt(gpchar));
			
			String isShowSync="0";
			//判断该通道是否有绑定后端账号，判断该通道绑定的运营商账号是否直连
			//1代表该通道有绑定后端账号并且该通道绑定的运营商账号连接梦网平台的
			//托管版暂时不支持签名同步
			if(StaticValue.getCORPTYPE() -0==0)
			{
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				conditionMap.put("gateId",String.valueOf(gate.getId()));
				List<AgwSpBind> agwSpBinds = baseBiz.findListByCondition(
					AgwSpBind.class, conditionMap, null);
				//绑定了后端账号
				if(agwSpBinds!=null&&agwSpBinds.size()==1)
				{
					AgwSpBind agwSpBind=agwSpBinds.get(0);
					//查询条件
					conditionMap.clear();
					//根据运营商账户的ID，查询运营商账户
					conditionMap.put("ptAccUid", String.valueOf(agwSpBind.getPtAccUid()));
					List<AgwAccount> AgwAccounts = baseBiz.findListByCondition(
							AgwAccount.class, conditionMap, null);
					if(AgwAccounts!=null&&AgwAccounts.size()>0)
					{
						//运营商账号连接梦网平台的
						if(AgwAccounts.get(0).getSpType().intValue()==0)
						{
							isShowSync="1";
						}else
						{
							isShowSync="0";
						}
					}else
					{
						isShowSync="0";
					}
				}else
				{
					isShowSync="0";
				}
			}
			//是否显示同步按钮，1为显示，0为不显示。
			request.setAttribute("isShowSync",isShowSync);
			request.setAttribute("keyId",keyId);
			request.setAttribute("ugate", gate);
			request.setAttribute("updateorinsert", "1");
			request.getRequestDispatcher(this.empRoot + this.basePath + "/pas_addPassage.jsp")
				.forward(request, response);
		} catch (Exception e) {
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"获取通道详情异常！"));
		}
	}
	
	
	public static boolean isInvalidString(String arg){
			
			return (arg==null || "".equals(arg)
					|| "undefined".equals(arg))?true:false;
	}
	
	
	
	/**
	 * 写日志
	 * 
	 * @param request
	 *        请求对象
	 * @param opModule
	 *        菜单名称
	 * @param opContent
	 *        操作内容
	 * @param opType
	 */
	private void setLog(HttpServletRequest request,String opModule,String opContent,String opType){
		try
		{
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				EmpExecutionContext.info(opModule, loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), opContent, opType);
			}
		}catch(Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e,opModule+opType+opContent+"日志写入异常"));
		}
	}
	
	/**
	 * 同步签名的方法
	 * @description    
	 * @param request
	 * @param response       			 
	 * @author tanglili <jack860127@126.com>
	 * @datetime 2016-1-25 下午06:41:47
	 */
	public void syncSign(HttpServletRequest request, HttpServletResponse response)
	{
		PrintWriter writer = null;
		String flag="fail";
		//通道号码
		String spageteNum=request.getParameter("spageteNum");
		//运营商
		String spiscumn=request.getParameter("spiscumn");
		//通道ID
		String gateID=request.getParameter("gateid");
		
		try
		{
			writer = response.getWriter();
			AgwAccount agwAccount=null;
			AgwSpBind agwSpBind=null;
			//查询通道与通道账户的绑定关系
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("gateId",gateID);
			List<AgwSpBind> agwSpBinds = baseBiz.findListByCondition(
					AgwSpBind.class, conditionMap, null);
			if(agwSpBinds!=null)
			{
				if(agwSpBinds.size()==1)
				{
					//查询到绑定关系
					agwSpBind=agwSpBinds.get(0);
					//查询条件
					conditionMap.clear();
					//根据通道账户的ID，查询通道账户
					conditionMap.put("ptAccUid", String.valueOf(agwSpBind.getPtAccUid()));
					List<AgwAccount> agwAccounts = baseBiz.findListByCondition(
							AgwAccount.class, conditionMap, null);
					if(agwAccounts!=null&&agwAccounts.size()>0){
						agwAccount=agwAccounts.get(0);
					}
					//根据通道ID查询通道所绑定的通道账户
					if(agwAccount!=null){
						AccountInfoSyncBiz accountInfoSyncBiz=new AccountInfoSyncBiz();
						//查询签名信息
						String[] signArr=accountInfoSyncBiz.pullAccountInfo(agwAccount.getSpAccid(), MD5.getMD5Str(agwAccount.getSpAccPwd()),spageteNum);
						if(signArr!=null&&signArr.length==9){
							//同步签名信息
							boolean result=accountInfoSyncBiz.updateGate(signArr);
							//调用同步签名的方法
							//accountInfoSyncBiz.accountInfoSync(userdata.getUserId(), MD5.getMD5Str(userdata.getUserPassword()), "1", spiscumn);
							if(result){
								flag="success";
							}else{
								flag="fail";
							}
						}else{
							//获取运营商签名失败
							flag="fail";
						}
					}
				}else if(agwSpBinds.size()==0)
				{
					//未查询到绑定关系
					flag="noBind";
				}else
				{
					//查询到绑定关系失败
					flag="fail";
				}
			}else
			{
				//查询绑定关系失败
				flag="fail";
			}
			
		}
		catch (Exception e)
		{
			flag="fail";
			EmpExecutionContext.error(e,"同步签名失败！");
		}finally{
			if(writer!=null){
				writer.print(flag);
			}
		}
		
	}
	
	/**
	 * 获取MBOSS签名信息
	 * @description    
	 * @param request
	 * @param response       			 
	 * @author tanglili <jack860127@126.com>
	 * @datetime 2016-1-30 下午04:37:49
	 */
	public void confirmSign(HttpServletRequest request, HttpServletResponse response)
	{
		PrintWriter writer = null;
		String flag="fail";
		//通道号码
		String spageteNum=request.getParameter("spageteNum");
		//运营商
		String spiscumn=request.getParameter("spiscumn");
		//通道ID
		String gateID=request.getParameter("gateid");
		
		try
		{
			writer = response.getWriter();
			AgwAccount agwAccount=null;
			AgwSpBind agwSpBind=null;
			//查询通道与通道账户的绑定关系
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("gateId",gateID);
			List<AgwSpBind> agwSpBinds = baseBiz.findListByCondition(
					AgwSpBind.class, conditionMap, null);
			if(agwSpBinds!=null)
			{
				if(agwSpBinds.size()==1)
				{
					//查询到绑定关系
					agwSpBind=agwSpBinds.get(0);
					//查询条件
					conditionMap.clear();
					//根据通道账户的ID，查询通道账户
					conditionMap.put("ptAccUid", String.valueOf(agwSpBind.getPtAccUid()));
					List<AgwAccount> agwAccounts = baseBiz.findListByCondition(
							AgwAccount.class, conditionMap, null);
					if(agwAccounts!=null&&agwAccounts.size()>0){
						agwAccount=agwAccounts.get(0);
					}
					//根据通道ID查询通道所绑定的通道账户
					if(agwAccount!=null){
						AccountInfoSyncBiz accountInfoSyncBiz=new AccountInfoSyncBiz();
						//拉取指定通道签名信息
						String[] signArr=accountInfoSyncBiz.pullAccountInfo(agwAccount.getSpAccid(), MD5.getMD5Str(agwAccount.getSpAccPwd()),spageteNum);
						if(signArr!=null&&signArr.length==9){
							String signStr=signArr[0]+"|"+signArr[1]+"|"+signArr[2]+"|"+signArr[3]+"|"+signArr[4]+"|"+signArr[5]+"|"+signArr[6]+"|"+signArr[7]+"|"+signArr[8];
							//测试字符串String signStr="J21234|10657|99|0|0|[梦网科技]|4|[aa]|1";
							flag="success"+"|"+signStr;
						}else{
							//获取运营商签名失败
							flag="getSignFail";
						}
					}
				}else if(agwSpBinds.size()==0)
				{
					//未查询到绑定关系
					flag="noBind";
				}else
				{
					//查询到绑定关系失败
					flag="fail";
				}
			}else
			{
				//查询绑定关系失败
				flag="fail";
			}
			
		}
		catch (Exception e)
		{
			flag="fail";
			EmpExecutionContext.error(e,"同步签名失败！");
		}finally{
			if(writer!=null){
				writer.print(flag);
			}
		}
		
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
			Object encrypOrDecrypttobject = request.getSession(false).getAttribute("decryptObj");
			//加密对象不为空
			if(encrypOrDecrypttobject != null)
			{
				//强转类型
				encryptOrDecrypt=(ParamsEncryptOrDecrypt)encrypOrDecrypttobject;
			}
			else
			{
				EmpExecutionContext.error("通道管理，从session获取加密对象为空。");
			}
			return encryptOrDecrypt;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "通道管理，从session获取加密对象异常。");
			return null;
		}
	}
}
