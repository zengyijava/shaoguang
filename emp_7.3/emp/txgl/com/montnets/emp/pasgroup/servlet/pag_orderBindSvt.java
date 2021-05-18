package com.montnets.emp.pasgroup.servlet;

import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.ParamsEncryptOrDecrypt;
import com.montnets.emp.common.biz.SysuserBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.common.vo.LfSysuserVo;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.pasgroup.biz.OrerBindBiz;
import com.montnets.emp.pasgroup.biz.TructTypeBiz;
import com.montnets.emp.pasgroup.biz.UserDataBiz;
import com.montnets.emp.security.context.ErrorLoger;
import com.montnets.emp.servmodule.txgl.entity.AcmdRoute;
import com.montnets.emp.servmodule.txgl.entity.LfTructType;
import com.montnets.emp.servmodule.txgl.entity.Userdata;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.SuperOpLog;
/**
 * 指令路由配置
 * @project p_txgl
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2015-3-4 下午02:31:39
 * @description
 */
@SuppressWarnings("serial")
public class pag_orderBindSvt extends BaseServlet {
	final ErrorLoger errorLoger = new ErrorLoger();
	private final UserDataBiz userDataBiz = new UserDataBiz();
	//操作模块
	final String opModule=StaticValue.TEMP_MANAGE;
	//操作用户
	final String opSper = StaticValue.OPSPER;
	final BaseBiz basebiz = new BaseBiz();
	private final String empRoot = "txgl";
	private final String basePath = "/pasgroup";
	
	final SysuserBiz sysuserBiz = new SysuserBiz();
	final TructTypeBiz lfTructBiz = new TructTypeBiz(); 
    
    public final static String jdkDateTimeFormat = "yyyy-MM-dd HH:mm:ss";
    
    private final OrerBindBiz biz = new OrerBindBiz();
    
    /**
     * 指令路由配置列表查询
     * @param
     * @param response
     */
    public void find(HttpServletRequest request, HttpServletResponse response){
		long stime = System.currentTimeMillis();
    	String structcode = request.getParameter("structcode");//指令代码
		String bussysname = request.getParameter("bussysname");//业务系统名称
		
		try {
			List<Userdata> userdataList = null;
			String userId = request.getParameter("lgusercode");
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			PageInfo pageInfo=new PageInfo();
			pageSet(pageInfo, request);
			LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
			orderbyMap.put("orderTime", StaticValue.DESC);
			conditionMap.put("uid&>", "100001");
			conditionMap.put("userType", "0");
			//conditionMap.put("status&=", "0");
			conditionMap.put("loginId&=", "WBS00A");
			
			int corpType = StaticValue.getCORPTYPE();
			//企业编码
			String corpCode = request.getParameter("lgcorpcode");
			String spids=null;
			StringBuffer spidsbuff=new StringBuffer();
			if("100000".equals(corpCode))
			{
				userdataList = userDataBiz.findSpUser(conditionMap, orderbyMap,null);
			}else
			{
				if(corpType==0){
					//单企业
					userdataList = userDataBiz.findSpUser(conditionMap, orderbyMap, null);
				}else{
					//多企业
					userdataList = userDataBiz.findSpUserByCorp(corpCode,conditionMap,orderbyMap, null);
					if(userdataList!=null&&userdataList.size()>0){
						for(Userdata user:userdataList){
							if(user!=null&&user.getUid()!=null){
								spidsbuff.append(user.getUid()).append(",");
							}
						}
						if(spidsbuff!=null&&spidsbuff.toString().contains(",")){
							spids=spidsbuff.deleteCharAt(spidsbuff.length()-1).toString();
						}else{
							spids="0";
						}
					}else{
						spids="0";
					}
				}
			}
			
			
			
			List<AcmdRoute> list = biz.getMoTructVo(spids,userId,structcode,bussysname,pageInfo) ;
			//加密后的集合
			Map<Long, String> keyIdMap = new HashMap<Long, String>();
			//ID加密
			if(list != null && list.size() > 0)
			{
				//加密对象
				ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
				//加密对象不为空
				if(encryptOrDecrypt != null)
				{
					boolean result = encryptOrDecrypt.batchEncryptToMap(list, "Id", keyIdMap);
					if(!result)
					{
						EmpExecutionContext.error("查询指令路由配置列表，参数加密失败。");
						throw new Exception("查询指令路由配置列表，参数加密失败。");
					}
				}
				else
				{
					EmpExecutionContext.error("查询指令路由配置列表，从session中获取加密对象为空！");
					list.clear();
					throw new Exception("查询指令路由配置列表，获取加密对象失败。");
				}
			}
			request.setAttribute("LfMoTructVoList", list);
			request.setAttribute("keyIdMap", keyIdMap);
			request.setAttribute("userId",userId);
			request.setAttribute("structcode", structcode);
			request.setAttribute("bussysname", bussysname);
			request.setAttribute("spUserList", userdataList);
			request.setAttribute("pageInfo", pageInfo);
			request.getRequestDispatcher(empRoot  + basePath +"/pag_orderBind.jsp")
			.forward(request, response);
			DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			String sDate = sdf.format(stime);
			setLog(request,"指令路由配置","("+sDate+")查询",StaticValue.GET);
		} catch (Exception e) {
			// TODO: handle exception
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"指令路由配置列表查询异常！"));
		}
    }
    
    /**
	 * 删除一条指令绑定系信息
	 * @param request
	 * @param response
	 */
	public void delete(HttpServletRequest request, HttpServletResponse response){
		PrintWriter writer = null;
		//日志内容变量
		String opContent=null;
		//日志修改前数据
		StringBuffer oldStr=new StringBuffer("");
		try {
			//String id = request.getParameter("id");
			String id;
			String keyId = request.getParameter("keyId");
			//加密对象
			ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
			//加密对象不为空
			if(encryptOrDecrypt != null)
			{
				//解密
				id = encryptOrDecrypt.decrypt(keyId);
				if(id == null)
				{
					EmpExecutionContext.error("删除指令路由信息，参数解密码失败，keyId:"+keyId);
					throw new Exception("删除指令路由信息，参数解密码失败。");
				}
			}
			else
			{
				EmpExecutionContext.error("删除指令路由信息，从session中获取加密对象为空！");
				throw new Exception("删除指令路由信息，获取加密对象失败。");
			}
			writer = response.getWriter();
			//写日志需查数据
			AcmdRoute lab=new BaseBiz().getById(AcmdRoute.class, id);
			if(lab!=null){
				//旧字符串拼接
				 oldStr.append(id).append("，").append(lab.getName()).append("，").append(lab.getStructcode()).append("，").append(lab.getBussysname())
	    		  .append("，").append(lab.getSpid());
			}
			int result = biz.delete(id);
			writer.print(result);
			if(result>0){
				opContent = "删除指令路由成功条数"+result+"条[ID，指令名称，指令代码，业务系统名称，SP账号]（" + oldStr + "）";
			}else{
				opContent = "删除指令路由失败条数"+result+"条[ID，指令名称，指令代码，业务系统名称，SP账号]（" + oldStr + "）";
			}
			//增加操作日志
			setLog(request, "指令路由配置", opContent, StaticValue.DELETE);
			writer.flush();
		} catch (Exception e) {
			
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"删除一条指令绑定系信息异常！"));
			if(writer!=null){
				writer.print("-1");
				writer.flush();
			}
		} 
		
		
	}
    /**
	 * 打开添加上行业务指令绑定页面
	 * @param request
	 * @param response
	 */
	public void doAdd(HttpServletRequest request, HttpServletResponse response) {
		try {
			
			List<Userdata> userdataList = null;
			List<LfTructType> tructList = null;
			
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			PageInfo pageInfo=new PageInfo();
			pageSet(pageInfo,request);
			
			
			LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
			orderbyMap.put("orderTime", StaticValue.DESC);
			conditionMap.put("uid&>", "100001");
			conditionMap.put("userType", "0");
			conditionMap.put("loginId&=", "WBS00A");
			conditionMap.put("status&=", "0");
			
			int corpType = StaticValue.getCORPTYPE();
			//企业编码
			String corpCode = request.getParameter("lgcorpcode");
			
			if("100000".equals(corpCode))
			{
				userdataList = userDataBiz.findSpUser(conditionMap, orderbyMap, null);
			}else
			{
				if(corpType==0){
					//单企业
					userdataList = userDataBiz.findSpUser(conditionMap, orderbyMap, null);
				}else{
					//多企业
					userdataList = userDataBiz.findSpUserByCorp(corpCode,conditionMap,orderbyMap, null);
				}
			}
			//获取指令类型集合
			tructList = lfTructBiz.findAllTructTypes();
			
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("spUserList", userdataList);
			request.setAttribute("tructList", tructList);
			request.setAttribute("msType","0");
			
			
			request.getRequestDispatcher(empRoot  + basePath  + "/pag_addorderBind.jsp")
			.forward(request, response);
		} catch (Exception e) {
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"打开添加上行业务指令绑定页面异常！"));
		}
	}
	
	/**
	 * 添加业务指令绑定
	 * 
	 * @param request
	 * @param response
	 */
	public void add(HttpServletRequest request, HttpServletResponse response){
		//企业编码
		String lgcorpcode = request.getParameter("lgcorpcode");
		//用户ID
		//String userid = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String userid = SysuserUtil.strLguserid(request);

		SuperOpLog spLog = new SuperOpLog();
//		String userName = request.getParameter("username");
		String opType = StaticValue.ADD;
		//日志内容变量
		String opContent="";
		//日志添加的数据
		StringBuffer newStr=new StringBuffer("");
		
//		String tructid = request.getParameter("tructid");//上行业务指令绑定ID
		Long userId = null;
		
		AcmdRoute moTructVo = new AcmdRoute();
		try{
				userId = Long.valueOf(userid);
		}catch (Exception e) {
			//进入异常
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"添加业务指令绑定时用户id获取异常！"));
			//企业编码
		}
		String opUser = request.getParameter("lgusername");
		PrintWriter writer=null;
		try{	
			writer=response.getWriter();
			  LfSysuserVo user = sysuserBiz.getSysuserVoByUserId(userId);
			  String name = request.getParameter("name");
		      String structcode = request.getParameter("structcode");
		      String bussysname = request.getParameter("bussysname");
		      String spid = request.getParameter("spid");
		     // String tructtype = request.getParameter("tructtype");
		      int exitsType = checkTructIsExits(structcode);
		      if(exitsType == 0)
		      {
		    	    writer.print("isExits");
					//添加操作成功日志
					spLog.logSuccessString(opUser, opModule, opType, opContent,lgcorpcode);  
		      }
		      else if(exitsType == -1)
		      {
		    	    writer.print("error");
					//添加操作成功日志
					spLog.logSuccessString(opUser, opModule, opType, opContent,lgcorpcode); 
		      }
		      else
		      {
		    	  moTructVo.setName(name);
		    	  moTructVo.setStructcode(structcode);
		    	  moTructVo.setBussysname(bussysname);
		    	  moTructVo.setSpid(Long.valueOf(spid));
		    	  moTructVo.setTructtype("02");
		    	  moTructVo.setCreater(user.getUserName());
		    	  moTructVo.setStatus("01");
		    	  moTructVo.setCreattime(new Timestamp(System.currentTimeMillis()));
		    	  
		    	  long addserviceBindid = biz.addMoServiceBind(moTructVo);
		    	  //新添加的数据拼接
		    	  newStr.append(moTructVo.getName()).append("，").append(moTructVo.getStructcode()).append("，").append(moTructVo.getBussysname())
	    		  .append("，").append(moTructVo.getSpid());
		    	  if(addserviceBindid>0){
		    		  //新增成功
		    		  //request.setAttribute("tmresult", "true");
		    		  writer.print("true");
		    		//添加操作成功日志
		    		spLog.logSuccessString(opUser, opModule, opType, opContent,lgcorpcode);
		    		//内容拼接
		    		opContent = "新建业务指令绑定关系成功[指令名称，指令代码，业务系统名称，SP账号]（"+newStr+"）";
		    		//增加操作日志
		    		setLog(request, "指令路由配置", opContent,opType);
		    	  }else{
		    		  //新增失败
		    		  // request.setAttribute("tmresult", "false");
		    		  writer.print("false");
		    		  //添加操作成功日志
		    		  spLog.logSuccessString(opUser, opModule, opType, opContent,lgcorpcode);
		    		//内容拼接
			    	opContent = "新建业务指令绑定关系失败[指令名称，指令代码，业务系统名称，SP账号]（"+newStr+"）";
			    	//增加操作日志
			    	setLog(request, "指令路由配置", opContent,opType);
		    	  }
		      }
		      writer.flush();
		      
		}catch(Exception e1){
			//request.setAttribute("result", "false");
			//添加操作失败日志
			spLog.logFailureString(opUser, opModule, opType, opContent+opSper, e1,lgcorpcode);
			EmpExecutionContext.error(errorLoger.getErrorLog(e1,"添加业务指令绑定异常！"));
			if(writer!=null){
				writer.print("false");
				writer.flush();
			}
		}
	}
	
	/**
	 * 打开编辑上行业务指令绑定页面
	 * @param request
	 * @param response
	 */
	public void doEdit(HttpServletRequest request, HttpServletResponse response)
	{
		
		try
		{
			//业务指令绑定id
			//String bindid = request.getParameter("moServiceid");
			String bindid;
			List<LfTructType> tructList = null;
			List<Userdata> userdataList = null;
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			String keyId = request.getParameter("keyId");
			String spId = request.getParameter("spId");
			//加密对象
			ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
			//加密对象不为空
			if(encryptOrDecrypt != null)
			{
				//解密
				bindid = encryptOrDecrypt.decrypt(keyId);
				if(bindid == null)
				{
					EmpExecutionContext.error("跳转编辑上行业务指令绑定页面，参数解密码失败，keyId:"+keyId);
					throw new Exception("跳转编辑上行业务指令绑定页面，参数解密码失败。");
				}
			}
			else
			{
				EmpExecutionContext.error("跳转编辑上行业务指令绑定页面，从session中获取加密对象为空！");
				throw new Exception("跳转编辑上行业务指令绑定页面，获取加密对象失败。");
			}
			
			if (bindid != null && !"".equals(bindid))
			{
				
				LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
				orderbyMap.put("orderTime", StaticValue.DESC);
				conditionMap.put("uid&>", "100001");
				conditionMap.put("userType", "0");
				conditionMap.put("loginId&=", "WBS00A");
				conditionMap.put("status&=", "0");
				
				int corpType = StaticValue.getCORPTYPE();
				//企业编码
				//String corpCode = request.getParameter("lgcorpcode");
				//当前登录操作员信息
				LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
				//企业编码
				String corpCode = lfSysuser.getCorpCode();
				if("100000".equals(corpCode))
				{
					userdataList = userDataBiz.findSpUser(conditionMap, orderbyMap, null);
				}else
				{
					if(corpType==0){
						//单企业
						userdataList = userDataBiz.findSpUser(conditionMap, orderbyMap, null);
					}else{
						//多企业
						userdataList = userDataBiz.findSpUserByCorp(corpCode,conditionMap,orderbyMap, null);
					}
				}
				
				//获取指令类型集合
				tructList = lfTructBiz.findAllTructTypes();
				AcmdRoute lfMoTructVo = biz.findLfMoTructVoBybindid(bindid);
				request.setAttribute("lfMoTructVo", lfMoTructVo);
				request.setAttribute("tructList", tructList);
				request.setAttribute("spUserList", userdataList);
				request.setAttribute("msType","0");
				request.setAttribute("keyId", keyId);
				request.setAttribute("spId", spId);
				request.getRequestDispatcher(empRoot  + basePath +"/pag_editorderBind.jsp")
				.forward(request, response);
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"打开编辑上行业务指令绑定页面异常！"));
		}
	}
	/**
	 * 修改业务指令绑定
	 * @param request
	 * @param response
	 */
	public void update(HttpServletRequest request, HttpServletResponse response){
		String opType = StaticValue.UPDATE;
		//日志内容
		String opContent = "";
		//日志修改前的数据
		StringBuffer oldStr=new StringBuffer("");
		//日志修改后的数据
		StringBuffer newStr=new StringBuffer("");
		//企业编码
		String lgcorpcode = request.getParameter("lgcorpcode");
		//用户ID
		//String userid = request.getParameter("lguserid");
		Long userId = null;
		AcmdRoute moTructVo = new AcmdRoute();
		try {
			//当前登录操作员信息
			LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			userId = lfSysuser.getUserId();
			//userId = Long.valueOf(userid);
		} catch (Exception e) {
			// 进入异常
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"修改业务指令绑定时用户id获取异常！"));
			// 企业编码
		}
		PrintWriter writer=null;
		String opUser = request.getParameter("lgusername");
		SuperOpLog spLog = new SuperOpLog();
		try{	
			//上行业务指令绑定ID
			String tructid = request.getParameter("tructid");
//			String tructid;
			String keyId = request.getParameter("keyId");
			//加密对象
			ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
			//加密对象不为空
			if(encryptOrDecrypt != null)
			{
				//解密
				tructid = encryptOrDecrypt.decrypt(keyId);
				if(tructid == null)
				{
					EmpExecutionContext.error("修改业务指令绑定信息，参数解密码失败，keyId:"+keyId);
					throw new Exception("修改业务指令绑定信息，参数解密码失败。");
				}
			}
			else
			{
				EmpExecutionContext.error("修改业务指令绑定信息，从session中获取加密对象为空！");
				throw new Exception("修改业务指令绑定信息，获取加密对象失败。");
			}
				writer =response.getWriter();
			  LfSysuserVo user = sysuserBiz.getSysuserVoByUserId(userId);
			  String name = request.getParameter("name");
		      String structcode = request.getParameter("structcode");
		      String bussysname = request.getParameter("bussysname");
		      String spid = request.getParameter("spid");
		      //String tructtype = request.getParameter("tructtype");
		      String status = request.getParameter("status");
		      
		      moTructVo.setId(Long.valueOf(tructid));
		      moTructVo.setName(name);
		      moTructVo.setStructcode(structcode);
		      moTructVo.setBussysname(bussysname);
		      moTructVo.setSpid(Long.valueOf(spid));
		      //moTructVo.setTructtype(tructtype);
		      moTructVo.setCreater(user.getUserName());
		      moTructVo.setStatus(status);
		      moTructVo.setCreattime(new Timestamp(System.currentTimeMillis()));
		      //写日志需查数据
		      AcmdRoute lab=new BaseBiz().getById(AcmdRoute.class, moTructVo.getId());
		      if(lab!=null){
				//旧字符串拼接
				oldStr.append(lab.getId()).append("，").append(lab.getName()).append("，").append(lab.getStructcode()).append("，").append(lab.getBussysname())
		    		  .append("，").append(lab.getSpid());
			  }
		      boolean flag =  basebiz.updateObj(moTructVo);
		     
		      newStr.append(moTructVo.getId()).append("，").append(moTructVo.getName()).append("，").append(moTructVo.getStructcode()).append("，").append(moTructVo.getBussysname())
    		  .append("，").append(moTructVo.getSpid());
		      if(flag){
		    	//修改成功
					//request.setAttribute("tmresult", "true");
				    writer.print("true");
					//添加操作成功日志
					spLog.logSuccessString(opUser, opModule, opType, opContent,lgcorpcode);
					 //内容拼接
				    opContent = "修改业务指令绑定成功[ID，指令名称，指令代码，业务系统名称，SP账号]（"+oldStr+"-->"+newStr+"）";
		    		//增加操作日志
		  			setLog(request, "指令路由配置", opContent, opType);
		      }else{
		    	    //修改失败
		    	   // request.setAttribute("tmresult", "false");
		    	    writer.print("false");
					//添加操作成功日志
					spLog.logSuccessString(opUser, opModule, opType, opContent,lgcorpcode);
					 //内容拼接
				    opContent = "修改业务指令绑定失败[ID，指令名称，指令代码，业务系统名称，SP账号]（"+oldStr+"-->"+newStr+"）";
		    		//增加操作日志
		  			setLog(request, "指令路由配置", opContent, opType);
		      }
		      writer.flush();
		      
		}catch(Exception e1){
			//request.setAttribute("result", "false");
			//添加操作失败日志
			spLog.logFailureString(opUser, opModule, opType, opContent+opSper, e1,lgcorpcode);
			EmpExecutionContext.error(errorLoger.getErrorLog(e1,"修改业务指令绑定异常！"));
			if(writer!=null){
				writer.print("false");
				writer.flush();
			}
			
		}
	}
	/**
	 * 检查 指令代码是否重复
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void checkTructIsExits(HttpServletRequest request, HttpServletResponse response)throws Exception{
/*		boolean result = false;
		String  structcode = request.getParameter("structcode");
		result = biz.findLfMoTructIsExitsByStruct(structcode);
		if(result){
			response.getWriter().write("1");
		}*/
		String structcode = request.getParameter("structcode");
		response.getWriter().print(checkTructIsExits(structcode));
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
			 List<AcmdRoute> lfmotructs = biz.findAllStruct();
			 if(lfmotructs != null)
			 {
				if(lfmotructs.size() > 0)
				{
					for(AcmdRoute acmdRoute:lfmotructs)
					{
						if(structcode.equals(acmdRoute.getStructcode()))
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
	 *  检查 指令代码是否绑定上行业务
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void checkBinding(HttpServletRequest request, HttpServletResponse response)throws Exception{
		String  id = request.getParameter("id");
		if(!"".equals(id)){
			BaseBiz baseBiz=new BaseBiz();
			AcmdRoute truct = baseBiz.getById(AcmdRoute.class, Long.valueOf(id));
			if("02".equals(truct.getStatus())){
				response.getWriter().write("1");
			}
		}
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
				EmpExecutionContext.error("指令路由配置，从session获取加密对象为空。");
			}
			return encryptOrDecrypt;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "指令路由配置，从session获取加密对象异常。");
			return null;
		}
	}
}
