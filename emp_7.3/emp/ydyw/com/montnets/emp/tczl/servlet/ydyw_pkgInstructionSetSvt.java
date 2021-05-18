package com.montnets.emp.tczl.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.pasgroup.AcmdRoute;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.ydyw.LfBusTaoCan;
import com.montnets.emp.security.context.ErrorLoger;
import com.montnets.emp.tczl.biz.ydyw_pkgInstructionSetBiz;
import com.montnets.emp.tczl.entity.LfTaocanCmd;
import com.montnets.emp.tczl.vo.LfTaocanCmdVo;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.SuperOpLog;

/**
 *<p>project name p_ydyw</p>
 *<p>Title: ydyw_pkgInstructionSetSvt</p>
 *<p>Description: </p>
 *<p>Company: Montnets Technology CO.,LTD.</p>
 * @author dingzx
 * @date 2015-1-19下午01:55:44
 */
@SuppressWarnings("serial")
public class ydyw_pkgInstructionSetSvt extends BaseServlet
{
	private final ErrorLoger errorLoger = new ErrorLoger();
	//操作模块
	private final String opModule="套餐指令设置";
	//操作用户
	private final String opSper = StaticValue.OPSPER;
	private final BaseBiz baseBiz=new BaseBiz();
	private final ydyw_pkgInstructionSetBiz pkgInstructionSetBiz = new ydyw_pkgInstructionSetBiz();  
	
	/**
	 * FunName:查询方法
	 * Description:
	 * @param 
	 * @retuen void
	 */
	public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		List<LfTaocanCmdVo> taocanVoList = null;
		LfTaocanCmdVo taocanVo = new LfTaocanCmdVo();
		LfTaocanCmdVo taocanVotemp = null;
		//套餐名称
		String taocanName = "";
		//套餐编号
		String taocanCode = "";
		//指令
		String structcode = "";
		//指令类型
		String structType = "";
		//SP账号
		String spUser = "";
		//套餐状态 (0启用 1禁用)
//		String state = "";
		//操作员名称
		String name = "";
		//计费类型
		String  taocanType = "";
		//当前登录账号id
		String  lguserid = "";
		List<Userdata> userdataList = null;
		List<LfBusTaoCan> busTaocanList = null;
		PageInfo pageInfo=new PageInfo();
		
		
		//添加与日志相关 p
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");//设置日期格式
		long startTimeByLog = System.currentTimeMillis();  //开始时间
		
		try
		{
			//lguserid =request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			lguserid = SysuserUtil.strLguserid(request);

			//通过操作员id查询操作员
			LfSysuser lfsysuser = baseBiz.getById(LfSysuser.class, lguserid);
			/**
			 * 查询数据权限 个人权限只显示自己创建的记录，permissionType=1
			 * 机构权限则根据所在机构节点及以下机构的记录 permissionType=2
			 */
			if (lfsysuser.getPermissionType()==1)
			{
				taocanVo.setUserId(lfsysuser.getUserId());
			}else{
				//根据机构id查询出所有子机构id
				//只能查看所在机构节点及以下机构的记录
				taocanVo.setDepId(lfsysuser.getDepId());
			}
			//是否第一次打开
			boolean isFirstEnter = false;
			
			isFirstEnter=pageSet(pageInfo, request);
			
			if (!isFirstEnter)
			{
				//套餐名称
				taocanName = request.getParameter("taocanName");
				if(taocanName != null && !"".equals(taocanName)){
					taocanName = taocanName.trim(); 
					taocanVo.setTaocanName(taocanName);
				}
				//套餐编号
				taocanCode = request.getParameter("taocanCode");
				if(taocanCode != null && !"".equals(taocanCode)){
					taocanCode = taocanCode.trim(); 
					taocanVo.setTaocanCode(taocanCode);
				}
				//指令
				structcode = request.getParameter("structcode");
				if(structcode != null && !"".equals(structcode)){
					structcode = structcode.trim();
					taocanVo.setStructcode(structcode);
				}
				//指令类型
				structType = request.getParameter("structType");
				if(structType != null && !"".equals(structType)){
					taocanVo.setStructType(Integer.parseInt(structType));
				}
				//SP账号
				spUser = request.getParameter("Spid");
				if(spUser != null && !"".equals(spUser)){
					taocanVo.setSpUser(spUser);
				}
				//操作员名称
				name = request.getParameter("name");
				if(name != null && !"".equals(name)){
					name = name.trim();
					taocanVo.setName(name);
				}
				//计费类型
				taocanType = request.getParameter("taocanType");
				if(taocanType != null && !"".equals(taocanType)){
					taocanVo.setTaocanType(Integer.parseInt(taocanType));
				}
				//创建起始时间
				String startSubmitTime = request.getParameter("sendtime");
				if(startSubmitTime != null && !"".equals(startSubmitTime)){
					taocanVo.setStartSubmitTime(startSubmitTime);
				}
				//创建结束时间
				String endSubmitTime = request.getParameter("recvtime");
				if(endSubmitTime != null && !"".equals(endSubmitTime)){
					taocanVo.setEndSubmitTime(endSubmitTime);
				}
			}
			//企业编码
			String corpCode = request.getParameter("lgcorpcode");
			taocanVo.setCorpCode(corpCode);
			//查询结果
			taocanVoList = pkgInstructionSetBiz.getLfTaocanCmdVo(taocanVo, pageInfo);
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
			orderbyMap.put("orderTime", StaticValue.DESC);
			conditionMap.put("userType", "0");
			//conditionMap.put("uid&>", "100001");
			conditionMap.put("loginId&=", "WBS00A");
			conditionMap.put("status&=", "0");
			conditionMap.put("accouttype", "1");
			
			int corpType = StaticValue.getCORPTYPE();
			if("100000".equals(corpCode))
			{
				userdataList = pkgInstructionSetBiz.findSpUser(conditionMap, orderbyMap,null);
			}else
			{
				if(corpType==0){
					//单企业
					userdataList = pkgInstructionSetBiz.findSpUser(conditionMap, orderbyMap, null);
				}else{
					//多企业,设置uid条件,其他情况在拼接SQL时直接拼接,解决1 =1 SQL方式
					conditionMap.put("uid&>", "100001");
					//多企业
					userdataList = pkgInstructionSetBiz.findSpUserByCorp(corpCode,conditionMap,orderbyMap, null);
				}
			}
			conditionMap.clear();
			orderbyMap.clear();
			conditionMap.put("state&=", "0");
			conditionMap.put("corp_code&=", corpCode);
			
			//查询所有已启用套餐
			busTaocanList = baseBiz.getByCondition(LfBusTaoCan.class, null,conditionMap, orderbyMap);
			//获取全局退订指令和发送账号
			List<LfTaocanCmdVo> taocanVoListtemp = null;
			taocanVotemp = new LfTaocanCmdVo();
			taocanVotemp.setCorpCode(corpCode);
			//3全局退订
			taocanVotemp.setStructType(3);
			taocanVoListtemp = pkgInstructionSetBiz.getLfTaocanCmdVo(taocanVotemp, null);
			if (taocanVoListtemp!=null&&taocanVoListtemp.size()>0)
			{
				taocanVotemp = taocanVoListtemp.get(0);
			}else{
				taocanVotemp = null;
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"业务类型查询失败！");
		}
		
		
		//添加与日志相关 p
		long endTimeByLog = System.currentTimeMillis();  //查询结束时间
		long consumeTimeByLog = endTimeByLog - startTimeByLog;  //查询耗时
		
		try {
			//增加操作日志 p
			Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser =(LfSysuser)loginSysuserObj;
				String opContent = "查询开始时间："+sdf.format(startTimeByLog)+"，耗时："+consumeTimeByLog+"ms"+"，查询总数："+pageInfo.getTotalRec();
				
				EmpExecutionContext.info("套餐指令设置", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
						loginSysuser.getUserName(), opContent, "GET");
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "添加操作日志异常！");
		}
		
		
		request.setAttribute("pageInfo", pageInfo);
		request.setAttribute("spUserList", userdataList);
		request.setAttribute("busTaocanList", busTaocanList);
		request.setAttribute("taocanVoList", taocanVoList);
		request.setAttribute("taocanVo", taocanVo);
		request.setAttribute("taocanVotemp", taocanVotemp);
		request.getRequestDispatcher("ydyw/tczl/ydyw_pkgInstructionSet.jsp").forward(
				request, response);
	}
	
	/**
	 * 新增套餐指令
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void add(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		//企业编码
		String lgcorpcode = request.getParameter("lgcorpcode");
		//用户ID
		//String userid = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String userid = SysuserUtil.strLguserid(request);

		SuperOpLog spLog = new SuperOpLog();
//		String userName = request.getParameter("username");
		String opType = StaticValue.ADD;
		String opContent = "新建套餐指令设置";
//		String tructid = request.getParameter("tructid");//上行业务指令绑定ID
		Long userId = null;
		
		AcmdRoute moTructorderVo = null;
		AcmdRoute moTructexitVo = null;
		LfTaocanCmd taocanCmdorder =null;
		LfTaocanCmd taocanCmdexit =null;
		try{
			userId = Long.valueOf(userid);
		}catch (Exception e) {
		//进入异常
		EmpExecutionContext.error(errorLoger.getErrorLog(e,"添加套餐指令设置时用户id获取异常！"));
		//企业编码
		}
		String opUser = request.getParameter("lgusername");
		PrintWriter writer=null;
		try{	
			  writer=response.getWriter();
			  LfSysuser lfsysuser = baseBiz.getById(LfSysuser.class, userid);
			  //套餐编号
			  String taocanCode = request.getParameter("taocanCode");
			  //套餐名称
			  String taocanname = request.getParameter("taocanname");
			  //套餐计费
			  String taocanType = request.getParameter("taocantype");
			  //套餐金额
			  String taocanMoney = request.getParameter("taocanmoney");
			  //订购指令
			  String orderCmd = request.getParameter("orderCmd");
			  //退订指令
			  String exitCmd = request.getParameter("exitCmd");
			  
//			  String name = request.getParameter("name");
			  
		     // String structcode = request.getParameter("structcode");
		      //String bussysname = request.getParameter("bussysname");
		      String spid = request.getParameter("spid");
		      //检查指令是否重复
		      String  result =checkRepeat(orderCmd,exitCmd);
		      //检查指令是否重复
		      if(result.equals("orderisExits"))
		      {
		    	    writer.print("orderisExits");
					//添加操作成功日志
					spLog.logSuccessString(opUser, opModule, opType, opContent,lgcorpcode);  
		      }
		      else if(result.equals("exitisExits"))
		      {
		    	    writer.print("exitisExits");
					//添加操作成功日志
					spLog.logSuccessString(opUser, opModule, opType, opContent,lgcorpcode); 
		      }
		      else if(result.equals("error"))
		      {
		    	  writer.print("error");
		    	  //添加操作成功日志
		    	  spLog.logSuccessString(opUser, opModule, opType, opContent,lgcorpcode); 
		      }
		      else
		      {
		    	  if(orderCmd != null && !"".equals(orderCmd)){
		    		  moTructorderVo = new AcmdRoute(); 
		    		  moTructorderVo.setName(taocanname+"(订购)");
		    		  moTructorderVo.setStructcode(orderCmd);
		    		  moTructorderVo.setBussysname(taocanname+"(订购)");
		    		  moTructorderVo.setSpid(Long.valueOf(spid));
		    		  moTructorderVo.setTructtype("03");
		    		  moTructorderVo.setCreater(lfsysuser.getUserName());
		    		  moTructorderVo.setStatus("01");
		    		  moTructorderVo.setCmdType(0);
		    		  moTructorderVo.setCreattime(new Timestamp(System.currentTimeMillis()));
		    		  //新增套餐指令
		    		  taocanCmdorder = new LfTaocanCmd();
		    		  taocanCmdorder.setStructcode(orderCmd);
		    		  taocanCmdorder.setTaocanCode(taocanCode);
		    		  if (taocanType != null && !"".equals(taocanType))
		    		  {
		    			  taocanCmdorder.setTaocanType(Integer.parseInt(taocanType));
		    		  }
		    		  if (taocanMoney != null && !"".equals(taocanMoney))
		    		  {
		    			  taocanCmdorder.setTaocanMoney(Long.parseLong(taocanMoney));
		    		  }
		    		  taocanCmdorder.setStructType(0);
		    		  taocanCmdorder.setCreateTime(new Timestamp(System.currentTimeMillis()));
		    		  taocanCmdorder.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		    		  taocanCmdorder.setDepId(lfsysuser.getDepId());
		    		  taocanCmdorder.setUserId(lfsysuser.getUserId());
		    		  taocanCmdorder.setCorpCode(lgcorpcode);
		    	  }
		    	  if(exitCmd != null && !"".equals(exitCmd)){
		    		  moTructexitVo = new AcmdRoute(); 
		    		  moTructexitVo.setName(taocanname+"(退订)");
		    		  moTructexitVo.setStructcode(exitCmd);
		    		  moTructexitVo.setBussysname(taocanname+"(退订)");
		    		  moTructexitVo.setSpid(Long.valueOf(spid));
		    		  moTructexitVo.setTructtype("03");
		    		  moTructexitVo.setCreater(lfsysuser.getUserName());
		    		  moTructexitVo.setStatus("01");
		    		  moTructexitVo.setCmdType(0);
		    		  moTructexitVo.setCreattime(new Timestamp(System.currentTimeMillis()));
		    		//新增套餐指令
		    		  taocanCmdexit = new LfTaocanCmd();
		    		  taocanCmdexit.setStructcode(exitCmd);
		    		  taocanCmdexit.setTaocanCode(taocanCode);
		    		  if (taocanType != null && !"".equals(taocanType))
		    		  {
		    			  taocanCmdexit.setTaocanType(Integer.parseInt(taocanType));
		    		  }
		    		  if (taocanMoney != null && !"".equals(taocanMoney))
		    		  {
		    			  taocanCmdexit.setTaocanMoney(Long.parseLong(taocanMoney));
		    		  }
		    		  taocanCmdexit.setStructType(1);
		    		  taocanCmdexit.setCreateTime(new Timestamp(System.currentTimeMillis()));
		    		  taocanCmdexit.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		    		  taocanCmdexit.setDepId(lfsysuser.getDepId());
		    		  taocanCmdexit.setUserId(lfsysuser.getUserId());
		    		  taocanCmdexit.setCorpCode(lgcorpcode);
		    	  }
		    	  //MobileBusStaticValue.orderDelimiter;
		    	  long addserviceBindid = pkgInstructionSetBiz.addMoServiceBind(moTructorderVo,moTructexitVo,taocanCmdorder,taocanCmdexit);
		    	  
		    	  if(addserviceBindid>0){
		    		  //新增成功
		    		  //request.setAttribute("tmresult", "true");
		    		  writer.print("true");
		    		  //添加操作成功日志
		    		  spLog.logSuccessString(opUser, opModule, opType, opContent,lgcorpcode);
		    		  
		    	  }else{
		    		  //新增失败
		    		  // request.setAttribute("tmresult", "false");
		    		  writer.print("false");
		    		  //添加操作成功日志
		    		  spLog.logSuccessString(opUser, opModule, opType, opContent,lgcorpcode);
		    	  }
		    	  
					//增加操作日志
					Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
					if(loginSysuserObj!=null){
						LfSysuser loginSysuser =(LfSysuser)loginSysuserObj;
						String opContent1 = "新建套餐指令设置"+(addserviceBindid>0?"成功":"失败")+"。[套餐名称，计费类型，SP账号，订购指令，退订指令]" +
								"("+taocanname+"，"+taocanType+"，"+spid+"，"+orderCmd+"，"+exitCmd+")";
						EmpExecutionContext.info("套餐指令设置", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
								loginSysuser.getUserName(), opContent1, "ADD");
					}	
		      }
		      writer.flush();
		      
		}catch(Exception e1){
			writer.print("false");
			//添加操作失败日志
			spLog.logFailureString(opUser, opModule, opType, opContent+opSper, e1,lgcorpcode);
			EmpExecutionContext.error(errorLoger.getErrorLog(e1,"添加套餐指令设置异常！"));
			writer.flush();
		}
	}
	/**
	 * 全局退订指令设置
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void exitAll(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		boolean flag =false;
		//企业编码
		String lgcorpcode = request.getParameter("lgcorpcode");
		//用户ID
		//String userid = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String userid = SysuserUtil.strLguserid(request);
		
		String spidAll = request.getParameter("spidAll");
		//全局退订指令
		String exitAllCmdAdd = request.getParameter("exitAllCmdAdd");
		SuperOpLog spLog = new SuperOpLog();
		String opType = StaticValue.ADD;
		String opContent = "全局退订指令设置";
		Long userId = null;
		AcmdRoute cmdRoute = null;
		LfTaocanCmd taocanCmd = null;
		
		try{
			userId = Long.valueOf(userid);
		}catch (Exception e) {
			//进入异常
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"添加全局退订指令设置时用户id获取异常！"));
			//企业编码
		}
		String opUser = request.getParameter("lgusername");
		PrintWriter writer=null;
		try{	
			
			writer=response.getWriter();
			if(spidAll==null||"".equals(spidAll)||exitAllCmdAdd==null||"".equals(exitAllCmdAdd))
			{
				EmpExecutionContext.error("添加全局退订指令设置时获取参数异常！");
				writer.print("false");
				return;
			}
			LfSysuser lfsysuser = baseBiz.getById(LfSysuser.class, userid);
			
			//String spid = request.getParameter("spid");
			//检查系统是否含有全局指令
			//获取全局退订指令和发送账号
			List<LfTaocanCmdVo> taocanVoListtemp = null;
			LfTaocanCmdVo taocanVotemp = new LfTaocanCmdVo();
			taocanVotemp.setCorpCode(lgcorpcode);
			//3全局退订
			taocanVotemp.setStructType(3);
			taocanVoListtemp = pkgInstructionSetBiz.getLfTaocanCmdVo(taocanVotemp, null);
			if (taocanVoListtemp!=null&&taocanVoListtemp.size()>0)
			{
				taocanVotemp = taocanVoListtemp.get(0);
			}else{
				taocanVotemp = null;
			}
			if (taocanVotemp!=null)
			{
				//修改
				//指令路由id
				Long aId = taocanVotemp.getAcId();
				//查找重复
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				conditionMap.put("structcode&in", exitAllCmdAdd.toLowerCase()+","+exitAllCmdAdd.toUpperCase());
				conditionMap.put("id&<>", aId.toString());
				List<AcmdRoute> cmdList = baseBiz.getByCondition(
						AcmdRoute.class, conditionMap, null);
				//检查指令在指令路由表是否重复
				if (cmdList != null && cmdList.size() > 0)
				{
					writer.print("isExits");
					return;
				}
				//设置指令路由
				cmdRoute = baseBiz.getById(AcmdRoute.class, aId);
				cmdRoute.setSpid(Long.parseLong(spidAll));
				cmdRoute.setStructcode(exitAllCmdAdd);
				//设置套餐指令
				Long cmdId = taocanVotemp.getId();
				taocanCmd = baseBiz.getById(LfTaocanCmd.class, cmdId);
				taocanCmd.setStructcode(exitAllCmdAdd);
				taocanCmd.setDepId(lfsysuser.getDepId());
				taocanCmd.setUserId(Long.parseLong(userid));
				taocanCmd.setUpdateTime(new Timestamp(System.currentTimeMillis()));
				taocanCmd.setCorpCode(lgcorpcode);
				  
				flag = pkgInstructionSetBiz.updateTaocanCmd(cmdRoute,taocanCmd);
				
			}else{
				//新增
				//检查指令是否存在
				int exitsType = checkTructIsExits(exitAllCmdAdd);
				if(exitsType == 0)
				{
		    	    writer.print("isExits");
					//添加操作成功日志
					spLog.logSuccessString(opUser, opModule, opType, opContent,lgcorpcode);  
					return ;
				}
			    else if(exitsType == -1)
			    {
		    	    writer.print("error");
					//添加操作成功日志
					spLog.logSuccessString(opUser, opModule, opType, opContent,lgcorpcode); 
			    }else{
		    		  cmdRoute = new AcmdRoute(); 
		    		  cmdRoute.setName("全局退订指令");
		    		  cmdRoute.setStructcode(exitAllCmdAdd);
		    		  cmdRoute.setBussysname("全局退订指令");
		    		  cmdRoute.setSpid(Long.valueOf(spidAll));
		    		  cmdRoute.setTructtype("03");
		    		  cmdRoute.setCreater(lfsysuser.getUserName());
		    		  cmdRoute.setStatus("01");
		    		  cmdRoute.setCmdType(0);
		    		  cmdRoute.setCreattime(new Timestamp(System.currentTimeMillis()));
		    		  //新增套餐指令
		    		  taocanCmd = new LfTaocanCmd();
		    		  taocanCmd.setStructcode(exitAllCmdAdd);
		    		  //taocanCmdorder.setTaocanCode(taocanCode);
//		    		  if (taocanType != null && !"".equals(taocanType))
//		    		  {
//		    			  taocanCmdorder.setTaocanType(Integer.parseInt(taocanType));
//		    		  }
//		    		  if (taocanMoney != null && !"".equals(taocanMoney))
//		    		  {
//		    			  taocanCmdorder.setTaocanMoney(Long.parseLong(taocanMoney));
//		    		  }
		    		  taocanCmd.setStructType(3);
		    		  taocanCmd.setCreateTime(new Timestamp(System.currentTimeMillis()));
		    		  taocanCmd.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		    		  taocanCmd.setDepId(lfsysuser.getDepId());
		    		  taocanCmd.setUserId(lfsysuser.getUserId());
		    		  taocanCmd.setCorpCode(lgcorpcode);
		    		  
		    		  long addserviceBindid = pkgInstructionSetBiz.addMoServiceBind(cmdRoute,null,taocanCmd,null);
		    		  
		    		  if (addserviceBindid>0)
		    		  {
						flag = true;
		    		  }
				  }
			}
			if(flag){
				writer.print("true");
				spLog.logSuccessString(opUser, opModule, opType, opContent,lgcorpcode);
			}else{
				writer.print("false");
				spLog.logSuccessString(opUser, opModule, opType, opContent,lgcorpcode);
			}
			
			//增加操作日志
			Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser =(LfSysuser)loginSysuserObj;
				String opContent1 = "全局套餐指令设置"+(flag==true?"成功":"失败")+"。[SP账号，全局退订指令]" +
						"("+spidAll+"，"+exitAllCmdAdd+")";
				EmpExecutionContext.info("套餐指令设置", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
						loginSysuser.getUserName(), opContent1, "UPDATE");
			}
			
		}catch(Exception e1){
			if(writer!=null){
			writer.print("false");
			writer.flush();
			}
			//添加操作失败日志
			spLog.logFailureString(opUser, opModule, opType, opContent+opSper, e1,lgcorpcode);
			EmpExecutionContext.error(errorLoger.getErrorLog(e1,"添加套餐指令设置异常！"));
		}
	}
	
	
	
	/**
	 * 检查 指令代码是否重复,MYSQL数据库查询不区分大小写
	 * @description    
	 * @param structcode
	 * @return  1 不重复;0:重复;-1:异常    			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-10-27 下午06:12:01
	 */
	public int checkTructIsExits(String structcode){
		int result = 1;
		try
		{
			 List<AcmdRoute> lfmotructs = pkgInstructionSetBiz.findAllStruct();
			 if(lfmotructs != null)
			 {
				if(lfmotructs.size() > 0)
				{
					for(AcmdRoute acmdRoute:lfmotructs)
					{
						if(structcode.equalsIgnoreCase(acmdRoute.getStructcode()))
						{
							result = 0;
							break;
						}
					}
				}
				else
				{
					result = 1;
				}
			 }
			 else
			 {
				 result = -1;
			 }
			 return result;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "查询所有指令代码异常!");
			return -1;
		}

	}
	/**
	 * 检查 指令代码是否重复,MYSQL数据库查询不区分大小写
	 * @description
	 * @return  1 不重复;0:重复;-1:异常    			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-10-27 下午06:12:01
	 */
	public String checkRepeat(String order,String exit){
		String result =""; 
		if(order != null && !"".equals(order)){
			 int exitsType = checkTructIsExits(order);
			 if(exitsType == 0)
		      {
				 result="orderisExits";
				 return result; 
					//添加操作成功日志
		      }
		      else if(exitsType == -1)
		      {
		    	  result ="error";
		    	  return result; 
		      }
		      else
		      {
		    	  result ="true";
		      }
		}
		if(exit != null && !"".equals(exit)){
			 int exitType = checkTructIsExits(exit);
			 if(exitType == 0)
		      {
				 result="exitisExits";
				 return result; 
		      }
		      else if(exitType == -1)
		      {
		    	    result="error";
		    	    return result; 
					//添加操作成功日志
		      }
		      else
		      {
		    	  result ="true";
		      }
		}
		return result;
	}
	
	/**
	 * 修改套餐指令
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void update(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		//指令路由ID
		String aId = request.getParameter("aId");
		//套餐指令ID
		String cmdId = request.getParameter("cmdId");
		//spid
		String spid = request.getParameter("spid");
		//指令
		String structCode = request.getParameter("structCode");
		//当前登录操作员
		//String lguserid = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String lguserid = SysuserUtil.strLguserid(request);

		//企业编码
		String lgcorpcode = request.getParameter("lgcorpcode");
		boolean flag = false;
		
		PrintWriter writer=null;
		try
		{
			writer = response.getWriter();
			//验证参数不能为空
			if(structCode == null || "".equals(structCode) || spid == null || "".equals(spid) || aId == null || "".equals(aId) || cmdId == null || "".equals(cmdId)){
				writer.print("paramError");
				return;
			}
			LfSysuser lfsysuser = baseBiz.getById(LfSysuser.class, lguserid);
			Long depId = lfsysuser.getDepId();
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("structcode&in", structCode.toLowerCase()+","+structCode.toUpperCase());
			conditionMap.put("id&<>", aId);
			List<AcmdRoute> cmdList = baseBiz.getByCondition(
					AcmdRoute.class, conditionMap, null);
			//检查指令在指令路由表是否重复
			if (cmdList != null && cmdList.size() > 0)
			{
				writer.print("structcodeExists");
				return;
			}
			//conditionMap.remove("busName");
//			conditionMap.clear();
//			conditionMap.put("id", cmdId);
//			conditionMap.put("structcode", structCode);
//			List<LfTaocanCmd> taoCanCmdList = baseBiz.getByCondition(LfTaocanCmd.class, conditionMap,null);
//			//指令套餐表是否重复
//			if (taoCanCmdList != null && taoCanCmdList.size() > 0)
//			{
//				writer.print("structcodeExists");
//				return;
//			}
			//设置指令路由
			AcmdRoute cmdRoute = baseBiz.getById(AcmdRoute.class, aId);
			
			//查询操作之前记录
			String befchgCont = aId+"，"+cmdId+cmdRoute.getSpid()+"，"+cmdRoute.getStructcode();
			
			cmdRoute.setSpid(Long.parseLong(spid));
			cmdRoute.setStructcode(structCode);
			//设置套餐指令
			LfTaocanCmd taocanCmd = baseBiz.getById(LfTaocanCmd.class, cmdId);
			
			taocanCmd.setStructcode(structCode);
			taocanCmd.setDepId(depId);
			taocanCmd.setUserId(Long.parseLong(lguserid));
			taocanCmd.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			taocanCmd.setCorpCode(lgcorpcode);
			  
			flag = pkgInstructionSetBiz.updateTaocanCmd(cmdRoute,taocanCmd);
			if (flag)
			{
				writer.print("true");
			}else{
				writer.print("false");
			}
			//增加操作日志
			Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser =(LfSysuser)loginSysuserObj;
				String opContent1 = "新建套餐指令设置"+(flag==true?"成功":"失败")+"。[指令路由ID，套餐指令ID，SP账号，指令代码]" +
						"("+befchgCont+")->("+aId+"，"+cmdId+"，"+spid+"，"+structCode+")";
				EmpExecutionContext.info("套餐指令设置", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
						loginSysuser.getUserName(), opContent1, "UPDATE");
			}				
			writer.flush();
		} catch (Exception e)
		{
			response.getWriter().print(ERROR);
			EmpExecutionContext.error(e,"修改套餐指令设置异常！");
		}
	}
	
	/**
	 * 删除套餐指令
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		boolean flag = false;
		//套餐指令ID
		String cmdId = request.getParameter("busId");
		String aId = request.getParameter("aId");
		try
		{
			if (cmdId!=null &&!"".equals(cmdId))
			{
//				cmdRoute = baseBiz.getById(AcmdRoute.class, aId);
			}
			if (aId!=null && !"".equals(aId))
			{
//				taocanCmd = baseBiz.getById(LfTaocanCmd.class, cmdId);
			}
			if(cmdId!=null &&!"".equals(cmdId) && aId!=null && !"".equals(aId)){
				flag =pkgInstructionSetBiz.deleteTaocanCmd(cmdId,aId);
			}
//			if (cmdRoute!=null && taocanCmd!=null)
//			{
//				flag = pkgInstructionSetBiz.deleteTaocanCmd(cmdRoute,taocanCmd);
//				if (flag)
//				{
//					response.getWriter().print("true");
//				}else{
//					response.getWriter().print("false");
//				}
//			}else{
//				response.getWriter().print("false");
//			}
			//异步返回删除结果
			//response.getWriter().print(baseBiz.deleteByIds(LfBusManager.class, busId) > 0 ? "true": "false");
			if (flag)
			{
				response.getWriter().print("true");
			}else{
				response.getWriter().print("false");
			}
			
			//增加操作日志
			Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser =(LfSysuser)loginSysuserObj;
				String opContent1 = "删除套餐指令设置"+(flag==true?"成功":"失败")+"。[指令路由ID，套餐指令ID]" +
						"("+aId+"，"+cmdId+")";
				EmpExecutionContext.info("套餐指令设置", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
						loginSysuser.getUserName(), opContent1, "DELETE");
			}	
		} catch (Exception e)
		{
			response.getWriter().print(ERROR);
			EmpExecutionContext.error(e,"删除业务类型异常！");
		}
	}
	
}
