package com.montnets.emp.tailnumber.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.SysuserBiz;
import com.montnets.emp.security.wgmsgconfig.WgMsgConfigBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.tailnumber.LfSubnoAllot;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.tailnumber.biz.SubnoBiz;
import com.montnets.emp.tailnumber.vo.dao.LfSnoAllotVo;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.SuperOpLog;


@SuppressWarnings("serial")
public class tai_subnoSvt extends BaseServlet {

	//操作模块
	private final String opModule=StaticValue.PARAM_PRESERVE;
	//操作用户
	private final String opSper = StaticValue.OPSPER;
	private final SubnoBiz biz = new SubnoBiz();
	private final SysuserBiz userBiz = new SysuserBiz();
	private final BaseBiz baseBiz=new BaseBiz();
	private final SuperOpLog spLog=new SuperOpLog();

	public void find(HttpServletRequest request, HttpServletResponse response)
	{
		List<LfSubnoAllot> allotList = null;
		//条件查询map
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		//排序map
		LinkedHashMap<String, String> ordebyMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> userMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> loginIdMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> menuMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> sysMap = new LinkedHashMap<String, String>();
		//日志开始时间
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		long begin_time=System.currentTimeMillis();
		PageInfo pageInfo=new PageInfo();
		//是否第一次打开
		boolean isFirstEnter = false;
		try{
			//尾号状态 3:模块  2:操作员  其余是全部
			String state=request.getParameter("state");
			String guName=request.getParameter("name");
			String subnoquery=request.getParameter("subnoquery");
			//起始时间
			String startTime = request.getParameter("sendtime");
			//结束时间
			String endTime = request.getParameter("recvtime");
			guName = guName==null?"":guName.trim();
			List<LfSysuser> sysList=null;
			String guids="";
			//如果是模块 state 3:模块  2:操作员  其余是全部
			if("3".equals(state))
			{
				conditionMap.put("loginId&is null", "isnull");
			}else if("2".equals(state))
			{
				conditionMap.put("menuCode&is null", "isnull");
				conditionMap.put("busCode&is null", "isnull");
			}
			if(subnoquery!=null && subnoquery.trim().length()>0){
				conditionMap.put("usedExtendSubno&like", subnoquery.trim());
			}
			if("2".equals(state)  && !"".equals(guName))
			{
				//通过名称去lfsysuser表把对应的操作员的guid找出来
				sysMap.put("name&like", guName);
				sysList = baseBiz.getByCondition(LfSysuser.class, sysMap, null);
				if(sysList!=null&&sysList.size()>0)
				{
					for(LfSysuser sys : sysList)
					{
						guids+=sys.getGuId().toString()+",";
					}
				}
				else
				{
					guids="-1";
				}
				if(guids.length()>0)
				{
					if(!"-1".equals(guids))
					{
						guids=guids.substring(0,guids.length()-1);
					}
					conditionMap.put("loginId&in", guids);
				}
			}
			if(startTime != null && !"".equals(startTime))
			{
				conditionMap.put("createTime&>=", startTime);
			}
			if(endTime != null && !"".equals(endTime))
			{
				conditionMap.put("createTime&<=", endTime);
			}
			//当前登录企业
			String lgcorpcode = request.getParameter("lgcorpcode");
			String corpCode = lgcorpcode;
			
			isFirstEnter=pageSet(pageInfo, request);
			
			conditionMap.put("corpCode",corpCode);
			conditionMap.put("isValid", "1");
			//获取所有的有效的尾号记录
			allotList = baseBiz.getByCondition(LfSubnoAllot.class, null, conditionMap, ordebyMap, pageInfo);
			
			//上行业务管理
			String serno_mcode = StaticValue.MOBUSCODE;			
			//短信提醒
			String remind_mcode =	StaticValue.VERIFYREMIND_MENUCODE;	
			//短信助手
			String im_mcode =	StaticValue.QYKKCODE;						
			//menuMap.put(serno_mcode, "上行业务管理");
			//menuMap.put(remind_mcode, "审批提醒");
			//menuMap.put(im_mcode, "短信助手");
			menuMap.put(serno_mcode, MessageUtils.extractMessage("xtgl", "xtgl_cswh_whgl_sxywgl", request));
			menuMap.put(remind_mcode, MessageUtils.extractMessage("xtgl", "xtgl_cswh_whgl_sptx", request));
			menuMap.put(im_mcode, MessageUtils.extractMessage("xtgl", "xtgl_cswh_whgl_dxzs", request));
			conditionMap.clear();
			conditionMap.put("corpCode", corpCode);
			//获取当前企业所有的操作员
			List<LfSysuser> userList =baseBiz.getByCondition(LfSysuser.class, conditionMap, null);
			
			LfSysuser user = null;
			if(userList != null){
				for(int i=0;i<userList.size();i++){
					user = userList.get(i);
					userMap.put(user.getGuId().toString(), user.getName());
					//将操作员的guid和用户名对应起来放在map
					loginIdMap.put(user.getGuId().toString(), user.getUserName());
				}
			}
			LfSnoAllotVo allotVo = null;
			LfSubnoAllot allot = null;
			List<LfSnoAllotVo> map = new ArrayList<LfSnoAllotVo>();
			for(int j=0;j<allotList.size();j++){
				allot = allotList.get(j);
				allotVo = new LfSnoAllotVo();
				allotVo.setSuId(allot.getSuId());
				//是自动还是固定
				allotVo.setAllotType(allot.getAllotType());
				//企业编码
				allotVo.setMenuCode(allot.getMenuCode());
				//操作员id
				allotVo.setLoginId(allot.getLoginId());
				//尾号
				allotVo.setUsedExtendSubno(allot.getUsedExtendSubno());
				//更新时间
				allotVo.setUpdateTime(allot.getUpdateTime());
				//创建时间
				allotVo.setCreateTime(allot.getCreateTime());
				//企业编码
				allotVo.setCorpCode(corpCode);
				//有效期
				allotVo.setValidity(allot.getValidity());
				if(allot.getLoginId() != null && !"".equals(allot.getLoginId())){
					//根据id从map中获取操作员的名字
					String name= userMap.get(allot.getLoginId().toString());
					String username = loginIdMap.get(allot.getLoginId().toString());
					if(name != null && !"".equals(name)){
						allotVo.setName(name);
						allotVo.setUsername(username);
						map.add(allotVo);
					}else{
						allotVo.setName("-");
						allotVo.setUsername("-");
						map.add(allotVo);
					}
				}else if(allot.getMenuCode() != null && !"".equals(allot.getMenuCode())){
					String menuName = menuMap.get(allot.getMenuCode());
					if(menuName != null && !"".equals(menuName)){
						allotVo.setName(menuName);
						map.add(allotVo);
					}else{
                        allotVo.setName("-");
                        map.add(allotVo);
                    }
				}else{
					allotVo.setName("-");
					map.add(allotVo);
				}
				
			}
			//获取尾号的位数
			Integer digit = userBiz.getSubnoDidit(lgcorpcode);
			request.setAttribute("digit", digit);
			request.setAttribute("allotList", map);
			request.setAttribute("isFirstEnter", isFirstEnter);
			//增加查询日志
			if(pageInfo!=null){
				long end_time=System.currentTimeMillis();
				String opContent ="查询开始时间："+format.format(begin_time)+",耗时:"+(end_time-begin_time)+"ms，数量："+pageInfo.getTotalRec();
				opSucLog(request, "尾号管理", opContent, "GET");
			}
			
		} catch (Exception e) {
			EmpExecutionContext.error(e, "尾号svt的find方法异常。");
		}finally{
			request.setAttribute("findresult", "-1");
			request.setAttribute("isFirstEnter", true);//回到页面第一次加载时的状态
			request.setAttribute("pageInfo", pageInfo);
			try {
				request.getRequestDispatcher("xtgl/tailnumber/tai_subno.jsp")
				.forward(request, response);
			} catch (ServletException e) {
				EmpExecutionContext.error(e, "尾号svt的find方法的跳转异常。");
			} catch (IOException e) {
				EmpExecutionContext.error(e, "尾号svt的find方法的跳转异常。");
			}
		}
	}
	public void delSubno(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		 //删除尾号
			String msg  = "";
			try{
				
				//尾号id
				String id = request.getParameter("subnoId");//用户ID
				//当前登录企业
				String lgcorpcode = request.getParameter("lgcorpcode");
				
				String corpCode = lgcorpcode;
				
				//查询操作之前记录
				String chgContent = "";
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				conditionMap.put("loginId", id);
				List<LfSubnoAllot> subnoList = baseBiz.getByCondition(LfSubnoAllot.class, conditionMap, null);
				if(subnoList.size()>0){
					chgContent = subnoList.get(0).getLoginId()+"，操作员，"+subnoList.get(0).getUsedExtendSubno();	
				}
				
				//获取操作员对象
				LfSysuser user = baseBiz.getByGuId(LfSysuser.class, Long.valueOf(id));
				msg= userBiz.delSubno(user, Long.valueOf(id), corpCode);
				response.getWriter().print(msg);
				
				//增加操作日志
				Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
				if(loginSysuserObj!=null){
					LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
					String opContent1 = "删除尾号"+("1".equals(msg)?"成功":"失败")+"。[操作员/模块，类型，当前尾号]" +
							"("+chgContent+")";
					EmpExecutionContext.info("尾号管理", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
							loginSysuser.getUserName(), opContent1, "DELETE");
				}
				
			}catch (Exception e) {
				EmpExecutionContext.error(e, "尾号svt的删除方法异常。");
				response.getWriter().print("errer");
			}
	}
	public void updateSubno(HttpServletRequest request,HttpServletResponse response) throws IOException{
		//更新操作员尾号
		String msg  = "";
		try{
			String updateId = request.getParameter("updateId");
			//操作员guid
			String guid = request.getParameter("guid");
			String type = request.getParameter("type");
			//当前登录企业
			String lgcorpcode = request.getParameter("lgcorpcode");
			String corpCode = lgcorpcode;
			
			//查询操作之前记录
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			String chgContent = "";
			if(null!=type && "1".equals(type)){
				conditionMap.put("loginId", guid);
				List<LfSubnoAllot> subnoList = baseBiz.getByCondition(LfSubnoAllot.class, conditionMap, null);
				if(subnoList.size()>0){
					chgContent = subnoList.get(0).getLoginId()+"，操作员，"+subnoList.get(0).getUsedExtendSubno();	
				}
			}else{
				conditionMap.put("menuCode", guid);
				List<LfSubnoAllot> subnoList = baseBiz.getByCondition(LfSubnoAllot.class, conditionMap, null);
				if(subnoList.size()>0){
					chgContent = subnoList.get(0).getMenuCode()+"，模块，"+subnoList.get(0).getUsedExtendSubno();
				}
			}
			
			//更新
			msg = userBiz.updateSubnoAllot(guid, updateId, corpCode, type);
			response.getWriter().print(msg);
			
			//增加操作日志
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				String opContent1 = "修改尾号"+("1".equals(msg)?"成功":"失败")+"。[操作员/模块，类型，当前尾号]" +
						"("+chgContent+")->("+guid+"，"+(null!=type && "1".equals(type)?"操作员":"模块")+"，"+updateId+")";
				EmpExecutionContext.info("尾号管理", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
						loginSysuser.getUserName(), opContent1, "UPDATE");
			}			
		}catch (Exception e) {
			EmpExecutionContext.error(e, "尾号svt的更新尾号方法异常。");
			response.getWriter().print("errer");
		}
	}

	public void delete(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		//尾号id
		String suId = request.getParameter("suId");
		//当前登录企业
		String lgcorpcode = request.getParameter("lgcorpcode");
		//操作内容
		String opContent ="";
		//操作类型
		String opType = null;
		String opUser=request.getParameter("lgusername");
		
		opContent = "删除子号关系绑定";
		opType = StaticValue.DELETE;
		try
		{
			//查询操作之前记录
			LfSubnoAllot subnoList = baseBiz.getById(LfSubnoAllot.class, suId);
			String chgContent = "";
			if(null!=subnoList.getLoginId() && !"".equals(subnoList.getLoginId())){
				chgContent = subnoList.getLoginId()+"，操作员，"+subnoList.getUsedExtendSubno();	
			}else{
				chgContent = subnoList.getMenuCode()+"，模块，"+subnoList.getUsedExtendSubno();
			}
				
			//根据尾号id删除记录
			String result = biz.delSubnoAllot(suId)?"true":"false";
			response.getWriter().print(result);
			spLog.logSuccessString(opUser, opModule, opType, opContent,lgcorpcode);
			
			//增加操作日志
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				String opContent1 = "删除尾号"+("true".equals(result)?"成功":"失败")+"。[操作员/模块，类型，当前尾号]" +
						"("+chgContent+")";
				EmpExecutionContext.info("尾号管理", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
						loginSysuser.getUserName(), opContent1, "DELETE");
			}		
			
		}catch(Exception e){
			response.getWriter().print(ERROR);
			spLog.logFailureString(opUser, opModule, opType, opContent+opSper, e,lgcorpcode);

			EmpExecutionContext.error(e, "尾号svt的删除方法异常。");
		}
	}

	//查询尾号是否被使用
	public void checkSubnoIsUsed(HttpServletRequest request, HttpServletResponse response) throws IOException 
	{
		
		//尾号长度
		String  subnoLength = request.getParameter("subNumber");
		//发送账号
		String spUser = request.getParameter("userId");
		try
		{
			if(biz.checkSubno(spUser, Integer.valueOf(subnoLength))){
		    	response.getWriter().print("true");
			}else{
		    	response.getWriter().print("false");
			}
		}catch(Exception e){
			response.getWriter().print("false");
			EmpExecutionContext.error(e, "尾号svt查询尾号是否被使用异常。");
		}
	}
	
	/**
	 * @description  记录操作成功日志  
	 * @param request
	 * @param modName 模块名称
	 * @param opContent 操作详情    	
	 * @param opType 操作类型 ADD UPDATE DELETE GET OTHER		 
	 * @datetime 2015-3-3 上午11:29:50
	 */
	public void opSucLog(HttpServletRequest request,String modName,String opContent,String opType){
		LfSysuser lfSysuser = null;
		try {
			Object obj = request.getSession(false).getAttribute("loginSysuser");
			if(obj == null) return;
			lfSysuser = (LfSysuser)obj;
		} catch (Exception e) {
			EmpExecutionContext.error(e,"记录操作日志异常,session为空！");
		}
		if(lfSysuser!=null){
			EmpExecutionContext.info(modName,lfSysuser.getCorpCode(),String.valueOf(lfSysuser.getUserId()),lfSysuser.getUserName(),opContent,opType);
		}else{
			EmpExecutionContext.info(modName,"","","",opContent,opType);
		}
	}
}
