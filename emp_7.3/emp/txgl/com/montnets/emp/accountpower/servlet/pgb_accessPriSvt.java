package com.montnets.emp.accountpower.servlet;

import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.accountpower.biz.AccessPriBiz;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.ParamsEncryptOrDecrypt;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.accountpower.LfMtPri;
import com.montnets.emp.entity.pasroute.LfSpDepBind;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.tailnumber.LfSubnoAllot;
import com.montnets.emp.pasgrpbind.biz.PasGroupbindBiz;
import com.montnets.emp.security.context.ErrorLoger;
import com.montnets.emp.servmodule.txgl.entity.Userdata;
import com.montnets.emp.util.PageInfo;

public class pgb_accessPriSvt  extends BaseServlet{
	
	//private WgMsgConfigBiz wgMsgBiz = new WgMsgConfigBiz();
	private final AccessPriBiz accessPriBiz = new AccessPriBiz();
	//BaseServlet baseServlet = new BaseServlet();
	private final String empRoot = "txgl";
	private final String basePath = "/accountpower";
	final ErrorLoger errorLoger = new ErrorLoger();
	/**
	 * SP账户绑定查询
	 * @param request
	 * @param response
	 */
	public void find(HttpServletRequest request, HttpServletResponse response) {
		long stime = System.currentTimeMillis();
		boolean isFirstEnter;
		PageInfo pageInfo = new PageInfo();
		BaseBiz baseBiz = new BaseBiz();
		try {
			isFirstEnter = pageSet(pageInfo,request);
			//企业编码
			String corpCode=request.getParameter("lgcorpcode");
			LinkedHashMap<String,String> orderbyMap = new LinkedHashMap<String,String>();
			LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
			//
			conditionMap.put("platFormType", "2");
			conditionMap.put("corpCode", corpCode);
			conditionMap.put("isValidate", "1");
			orderbyMap.put("spUser", StaticValue.ASC);
			List<LfSpDepBind> userdataList = accessPriBiz.getSpDepBindWhichUserdataIsOk(conditionMap, orderbyMap, null);
			//if(StaticValue.isContainKKSp==0){
			if(StaticValue.getIsContainKKSp()==0){
				List<Integer> l=new ArrayList<Integer>();
				LinkedHashMap<String,String> conditionMapsub = new LinkedHashMap<String,String>();
				conditionMapsub.put("menuCode", StaticValue.QYKKCODE);
				List<LfSubnoAllot> subnolist=baseBiz.getByCondition(LfSubnoAllot.class, conditionMapsub, null);
				if(subnolist!=null&&subnolist.size()>0&&userdataList!=null&&userdataList.size()>0){
					for (int i=0; i<userdataList.size();i++) {
						LfSpDepBind spb=userdataList.get(i);
						if(spb==null){
							continue;
						}
						if(spb.getSpUser()==null||"".equals(spb.getSpUser())){
							continue;
						}
						for (LfSubnoAllot lfSubnoAllot : subnolist) {
							if(lfSubnoAllot==null){
								continue;
							}
							if(lfSubnoAllot.getSpUser()==null||"".equals(lfSubnoAllot.getSpUser())){
								continue;
							}
							if(spb.getSpUser().equals(lfSubnoAllot.getSpUser())){
								l.add(i);
							}
						}
					}
					for (Integer i : l) {
						int ii=(int)i;
						userdataList.remove(ii);
					}
					
				}
			}
			request.setAttribute("userdataList", userdataList);
			
			conditionMap.clear();
			
			if(!isFirstEnter)
			{
				conditionMap.put("spuserid&like", request.getParameter("userid").toUpperCase());
			}
			orderbyMap.clear();
			orderbyMap.put("spuserid", StaticValue.ASC);
			orderbyMap.put("id", StaticValue.ASC);
			conditionMap.put("corpcode", corpCode);
			List<LfMtPri> accountList = baseBiz.getByConditionNoCount(LfMtPri.class,null, conditionMap, orderbyMap,pageInfo);
			//加密后的集合
			Map<Long, String> keyIdMap = new HashMap<Long, String>();
			//ID加密
			if(accountList != null && accountList.size() > 0)
			{
				//加密对象
				ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
				//加密对象不为空
				if(encryptOrDecrypt != null)
				{
					boolean result = encryptOrDecrypt.batchEncryptToMap(accountList, "Id", keyIdMap);
					if(!result)
					{
						EmpExecutionContext.error("查询SP账户绑定列表，参数加密失败。corpCode:"+corpCode);
						throw new Exception("查询SP账户绑定列表，参数加密失败。");
					}
				}
				else
				{
					EmpExecutionContext.error("查询SP账户绑定列表，从session中获取加密对象为空！");
					accountList.clear();
					throw new Exception("查询SP账户绑定列表，获取加密对象失败。");
				}
			}
			
			conditionMap.clear();
			//conditionMap.put("spUser&not in", spUser.toString());
			conditionMap.put("uid&>", "100001");
			conditionMap.put("userType", "0");
			conditionMap.put("accouttype", "1");
			//conditionMap.put("userId&not in", spUser.toString());
			List<Userdata> userList = baseBiz.getByCondition(Userdata.class, conditionMap, null);
			
			LinkedHashMap<String,Userdata> userMap = new LinkedHashMap<String,Userdata>();
			for(Userdata user : userList ){
				userMap.put(user.getUserId(), user);
			}
			
			conditionMap.clear();
			//conditionMap.put("guId&in", guids.toString());
			//如果是单企业，则userID>1;
			int corptype = StaticValue.getCORPTYPE();
			if(corptype == 0)
			{
				conditionMap.put("userId&>", "1");
			}
			//如果是多企业，则userID>2;
			if(corptype == 1)
			{
				conditionMap.put("userId&>", "2");
			}
			conditionMap.put("corpCode", corpCode);
			List<LfSysuser> sysUserList = baseBiz.getByCondition(LfSysuser.class, conditionMap, null);
			
			
			LinkedHashMap<String,LfSysuser> sysUserMap = new LinkedHashMap<String,LfSysuser>();
			for(LfSysuser sysuser : sysUserList ){
				sysUserMap.put(sysuser.getUserId().toString(),sysuser);
			}
			
			conditionMap.clear();
			request.setAttribute("sysUserMap", sysUserMap);
			request.setAttribute("userMap", userMap);
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("accountList", accountList);
			request.setAttribute("keyIdMap", keyIdMap);
			
			request.setAttribute("uri", request.getRequestURI());
			request.getRequestDispatcher(empRoot + basePath + "/pgb_access.jsp").forward(
					request, response);
			DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			String sDate = sdf.format(stime);
			setLog(request,"SP账号绑定","("+sDate+")查询",StaticValue.GET);
		} catch (Exception e) {
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"SP账户绑定查询异常！"));
		}
		
	}
	
	
	/**
	 * 输出人员代码数据
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void createUserTree(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try
		{
			Long superiorDepId = null;
			//企业编码
			String corpCode=request.getParameter("lgcorpcode");
			//机构id
			String depStr = request.getParameter("depId");
			if(depStr != null && !"".equals(depStr.trim())){
				superiorDepId = Long.parseLong(depStr);
			}
			String deptUserTree = getDeptUserJosnDataNew(superiorDepId,corpCode);		
			response.getWriter().print(deptUserTree);
		}
		catch (Exception e) 
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"输出人员代码数据异常！"));
		}
	}
	
	/**
	 * 获取人员代码数据
	 * @param superiorDepId
	 * @param corpCode
	 * @return
	 */
	public String getDeptUserJosnDataNew(Long superiorDepId,String corpCode) {
		List<LfDep> lfDeps;
		List<LfSysuser> lfSysusers = null;
		StringBuffer tree = null;
		/*ReportBiz reportBiz = new ReportBiz();*/
		PasGroupbindBiz pasGroupbindBiz = new PasGroupbindBiz();
		try {
			// lfDeps = reportBiz.getAllDeps(getUserId());
			// 查询出所有的机构
			if(null == superiorDepId){
				lfDeps = new ArrayList<LfDep>();
				/*LfDep lfDep = reportBiz.getAllDepsByCorpCode(corpCode).get(0);*/
				LfDep lfDep = pasGroupbindBiz.getAllDepsByCorpCode(corpCode).get(0);
				lfDeps.add(lfDep);
			}else{
				/*lfDeps = reportBiz.getAllDepsByCorpCode(corpCode,superiorDepId);*/
				lfDeps = pasGroupbindBiz.getAllDepsByCorpCode(corpCode,superiorDepId);
			}
			LfDep lfDep = null;
			
			tree = new StringBuffer("[");
			for (int i = 0; i < lfDeps.size(); i++) {
				lfDep = lfDeps.get(i);
				tree.append("{");
				tree.append("id:").append(lfDep.getDepId());
				tree.append(",name:'").append(lfDep.getDepName()).append("'");
				tree.append(",pId:").append(lfDep.getSuperiorId());
				tree.append(",depId:'").append(lfDep.getDepId()).append("'");
				tree.append(",isParent:").append(true);
				tree.append(",nocheck:").append(true);
				tree.append("}");
				if (i != lfDeps.size() - 1) {
					tree.append(",");
				}
			}

			if(null != superiorDepId){
				/*lfSysusers = reportBiz.getAllSysusersWhitoutDelByCorpCode(corpCode, superiorDepId);*/
				lfSysusers = pasGroupbindBiz.getAllSysusersWhitoutDelByCorpCode(corpCode, superiorDepId);
			}
				//lfSysusers = reportBiz.getAllSysusersByCorpCode(corpCode);
			LfSysuser lfSysuser = null;
			if(lfSysusers != null && !lfSysusers.isEmpty()){
				if(!tree.toString().equals("[")){
					tree.append(",");
				}		
				for (int i = 0; i < lfSysusers.size(); i++) {
					lfSysuser = lfSysusers.get(i);
					tree.append("{");
					tree.append("id:'u").append(lfSysuser.getUserId()).append("'");
					tree.append(",name:'").append(lfSysuser.getName()).append("'");
					tree.append(",pId:").append(lfSysuser.getDepId());
					tree.append(",depId:'").append(lfSysuser.getDepId()+"'");
					tree.append(",isParent:").append(false);
					tree.append("}");
					if(i != lfSysusers.size()-1){
						tree.append(",");
					}
				}
			}
			
			
//			tree.append(",{");
//			tree.append("id:'u").append("-1'");
//			tree.append(",name:'").append("未知操作员'");
//			tree.append(",pId:").append("1");
//			tree.append(",isParent:").append(false);
//			tree.append("}");

			tree.append("]");
		} catch (Exception e) {
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"sp账户绑定模块获取人员代码数据异常！"));
		}
		return tree==null?"":tree.toString();
	}
	
	
	/**
	 * 绑定sp账户
	 * @param request
	 * @param response
	 */
	public void doBind(HttpServletRequest request, HttpServletResponse response) {
		BaseBiz baseBiz = new BaseBiz();
		PrintWriter writer = null;
		String opContent = null;
		try {
			writer = response.getWriter();
			LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
			List<LfMtPri> accountList = new ArrayList<LfMtPri>();
			
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			long createuserid=0l;
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				createuserid=loginSysuser.getUserId();
			}
			String idorcodes = request.getParameter("idorcodes");
			//SP账号
			String spUserStr = request.getParameter("spUserStr");
			//企业编码
			String corpCode=request.getParameter("lgcorpcode");
			idorcodes = idorcodes.substring(0,idorcodes.lastIndexOf(","));
			spUserStr = spUserStr.substring(0,spUserStr.lastIndexOf(","));
			String[] spUsers = spUserStr.split(",");
			String[] codes = idorcodes.split(",");
			//绑定类型

			for(int i=0; i< spUsers.length;i ++)
			{
				String spUser = spUsers[i];
				String curcodes = "";
				conditionMap.put("spuserid", spUser);
				conditionMap.put("userid&in", idorcodes);
				List<LfMtPri> accountList2 = baseBiz.getByCondition(LfMtPri.class, conditionMap, null);
				if(accountList2 != null )
				{
					for(LfMtPri accountBind : accountList2)
					{
						curcodes +="&" + accountBind.getUserid() + "&";
					}
				}
				for(int j=0;j < codes.length;j ++)
				{
					if(curcodes.indexOf("&"+codes[j]+"&") == -1)
					{
						LfMtPri lfAccountBind = new LfMtPri();
						lfAccountBind.setSpuserid(spUser);
						lfAccountBind.setCorpcode(corpCode);
						lfAccountBind.setUserid(Long.valueOf(codes[j]));
						lfAccountBind.setCreatetime(new Timestamp(System.currentTimeMillis()));
						lfAccountBind.setCreateuserid(createuserid);
						accountList.add(lfAccountBind);
					}
				}
			}
			
			Integer count = baseBiz.addList(LfMtPri.class, accountList);
			if (count > 0) {
				writer.print("true");
				//增加操作日志
				opContent="操作员与SP账户绑定成功(账户："+spUserStr+"与操作员ID："+idorcodes+")进行了绑定";
				setLog(request, "SP账户绑定", opContent, StaticValue.ADD);
			} else {
				writer.print("noCount");
				opContent="操作员与SP账户绑定失败(账户："+spUserStr+"与操作员ID："+idorcodes+")进行了绑定";
				setLog(request, "SP账户绑定", opContent, StaticValue.ADD);
			}
		} catch (Exception e) {
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"绑定sp账户异常！"));
			writer.print("error");
		}
	}
	
	/**
	 * 删除绑定关系
	 * @param request
	 * @param response
	 */
	public void delete(HttpServletRequest request, HttpServletResponse response) {
		PrintWriter writer = null;
		//日志内容变量
		String opContent=null;
		//日志修改前数据
		StringBuffer oldStr=new StringBuffer("");
		try
		{
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
					EmpExecutionContext.error("删除SP账户绑定信息，参数解密码失败，keyId:"+keyId);
					throw new Exception("删除SP账户绑定信息，参数解密码失败。");
				}
			}
			else
			{
				EmpExecutionContext.error("删除SP账户绑定信息，从session中获取加密对象为空！");
				throw new Exception("删除SP账户绑定信息，获取加密对象失败。");
			}
			writer = response.getWriter();
			//写日志需查数据
			LfMtPri lab=new BaseBiz().getById(LfMtPri.class, id);
			if(lab!=null){
				//旧字符串拼接
				oldStr.append(id).append(",").append(lab.getSpuserid()).append(",")
				.append(lab.getUserid());
			}
			int result = new BaseBiz().deleteByIds(LfMtPri.class, id);
			if(result > 0)
			{
				writer.print("true");
				//增加操作日志
				opContent = "删除SP绑定关系成功[ID，SP账号，绑定操作员，绑定类型]（"+oldStr+"）";
				setLog(request, "SP账户绑定", opContent, StaticValue.DELETE);
			}
			else
			{
				writer.print("false");
				//增加操作日志
				opContent = "删除SP绑定关系失败[ID，SP账号，绑定操作员，绑定类型]（"+oldStr+"）";
				setLog(request, "SP账户绑定", opContent, StaticValue.DELETE);
			}

		}catch(Exception e)
		{
			if(writer!=null){
				writer.print("error");
			}
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"删除sp账户绑定关系异常！"));
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
				EmpExecutionContext.error("SP绑定关系，从session获取加密对象为空。");
			}
			return encryptOrDecrypt;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "SP绑定关系，从session获取加密对象异常。");
			return null;
		}
	}
}
