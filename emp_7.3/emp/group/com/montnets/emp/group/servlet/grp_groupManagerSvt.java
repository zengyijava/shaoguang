package com.montnets.emp.group.servlet;

import com.alibaba.fastjson.JSONObject;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.DepBiz;
import com.montnets.emp.common.biz.GlobalVariableBiz;
import com.montnets.emp.common.biz.ParamsEncryptOrDecrypt;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.DepDAO;
import com.montnets.emp.common.servlet.util.Excel2007Reader;
import com.montnets.emp.common.servlet.util.Excel2007VO;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.common.vo.GroupInfoVo;
import com.montnets.emp.entity.employee.LfEmployee;
import com.montnets.emp.entity.employee.LfEmployeeDep;
import com.montnets.emp.entity.group.LfList2gro;
import com.montnets.emp.entity.group.LfMalist;
import com.montnets.emp.entity.group.LfUdgroup;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.group.biz.GroupManagerBiz;
import com.montnets.emp.group.vo.LfList2groVo;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.util.ChangeCharset;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.PhoneUtil;
import com.montnets.emp.util.SuperOpLog;
import com.montnets.emp.util.TxtFileUtil;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 个人群组管理（员工）
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class grp_groupManagerSvt extends grp_baseManagerSvt {

    private final GroupManagerBiz biz = new GroupManagerBiz();

	private final GroupManagerBiz groupManagerBiz = new GroupManagerBiz();

	public boolean isNumeric(String str){ 
		Pattern pattern = Pattern.compile("[0-9]*"); 
		return pattern.matcher(str).matches(); 
	} 

	
	/**
	 * 解密处理
	 * @param request
	 * @param udgId
	 * @return
	 */
	public String getDecryptValue(HttpServletRequest request, String udgId){
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
					EmpExecutionContext.error("员工群组参数解密码失败，keyId:"+uid);
					return "";
				}
			}
			else
			{
				EmpExecutionContext.error("员工群组从session中获取加密对象为空！");
				return "";
			}
			return uid;
	}
	
	/**
	 * @param request
	 * @param response
	 */
	public void getTable(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		
		Long groupId = -1L;
		List<LfList2groVo> userVos = new ArrayList<LfList2groVo>();
		String udgid = request.getParameter("udgid");
		String searchStr = request.getParameter("sysName");
		String sysPhone = request.getParameter("sysPhone");
		//new add split tool bar
		PageInfo pageInfo = new PageInfo();
		try {
			long startTime = System.currentTimeMillis();
			if(udgid == null || "".equals(udgid.trim())){
				EmpExecutionContext.error("员工群组详情获取群组id异常！id:"+udgid);
				return;
			}
			//加密对象
			udgid=getDecryptValue( request, udgid);
			LfUdgroup udgroup = new BaseBiz().getById(LfUdgroup.class, Long.valueOf(udgid));
			List<LfList2groVo> vos = new ArrayList<LfList2groVo>();
			LfList2groVo list2groVo = new LfList2groVo();

			if(searchStr!=null && searchStr.length()>0)
			{
				list2groVo.setName(searchStr);
			}

			if(sysPhone != null && sysPhone.length()>0){
				list2groVo.setMobile(sysPhone);
			}
			if(udgroup != null){
				if(udgroup.getSharetype() == 0){
					groupId = Long.valueOf(udgid);
				}else if(udgroup.getSharetype() == 1){
					groupId = udgroup.getGroupid();
				}
				//new add split tool bar
				String pageIndex = request.getParameter("pageIndex");
				pageInfo.setPageIndex(StringUtils.isNotBlank(pageIndex)?Integer.parseInt(pageIndex):1);
				pageInfo.setPageSize(10);
//				vos = biz.getList2groVos(list2groVo,groupId);
				vos = biz.getList2groVosByPageInfo(list2groVo,pageInfo,groupId);
			}else{
				EmpExecutionContext.error("员工群组详情获取群组对象为null！");
				return;
			}

			if(vos != null && vos.size()>0){
				for(int i=0;i<vos.size();i++){
					LfList2groVo vo = vos.get(i);
					if((udgroup.getSharetype() == 1 && vo.getL2gtype() == 2) || vo.getSharetype() == 1){
						String phone = vo.getMobile();
						phone = phone.substring(0,3)+"*****"+phone.substring(8);
						vo.setMobile(phone);
					}
					userVos.add(vo);
				}
			}

			//操作日志信息
			String opContent = "详情查询："+sdf.format(startTime)+"，耗时："+(System.currentTimeMillis()-startTime)/1000+"秒，群组ID:"+groupId+"，总数：" + userVos.size();
			LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			EmpExecutionContext.info(opModule, lfSysuser.getCorpCode(), String.valueOf(lfSysuser.getUserId()), lfSysuser.getUserName(), opContent, "GET");

		} catch (Exception e) {
			EmpExecutionContext.error(e,"获取员工群组信息出现异常！");
		}finally {
			request.setAttribute("groupId",groupId);
			request.setAttribute("name", searchStr);
			request.setAttribute("sysPhone", sysPhone);
			request.setAttribute("vos", userVos);
			request.setAttribute("pageInfo",pageInfo);
			request.getRequestDispatcher(this.empRoot + basePath +"/grp_groupMembers.jsp").forward(request, response);
		}
	}

	public LinkedHashMap<String,String> getGroupMap(Integer gpAttribute, Long userid)throws Exception
	{
		LinkedHashMap<String,String> groupMap = new LinkedHashMap<String,String>();
		List<LfUdgroup> lfUdgroupList = new ArrayList<LfUdgroup>();
		LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
		LinkedHashMap<String,String> orderbyMap = new LinkedHashMap<String,String>();
		try
		{
			conditionMap.put("userId", String.valueOf(userid));
			conditionMap.put("gpAttribute",gpAttribute.toString());
			orderbyMap.put("udgId","asc" );

		//lirj add sqlserver time
			lfUdgroupList = new BaseBiz().getByCondition(LfUdgroup.class, conditionMap, orderbyMap);

			if(null != lfUdgroupList && 0 != lfUdgroupList.size())
			{
				for(LfUdgroup lfUdgroup :lfUdgroupList)
				{
					groupMap.put(lfUdgroup.getUdgId().toString(), lfUdgroup.getUdgName());
				}
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"获取员工群组中群组信息出现异常！");
		}
		return groupMap;
	}

	/**
	 * @param request
	 * @param response
	 */
	public void delete(HttpServletRequest request, HttpServletResponse response)
	{
		String lgcorpcode = request.getParameter("lgcorpcode");
		String ids = null == request.getParameter("ids")?" ":request.getParameter("ids");
		Integer delnum = null;
		String opType = StaticValue.DELETE;
		String opContent = "删除群组记录";
		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("udgId&in", ids);
			List<LfList2gro> list = baseBiz.getByCondition(LfList2gro.class, conditionMap, null);
			delnum = biz.delGroupMembers(ids);
			//增加操作日志
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null)
			{
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				//日志字段
				String field = "[群组id]";
				String flag = delnum > 0?"成功":"失败";
				if(list!=null&&list.size()>0)
				{
					for(int i =0;i<list.size();i++)
					{
						String opContent2 = "删除群组成员"+flag+"。"+field+"("+list.get(i).getL2gId()+")";
						EmpExecutionContext.info(opModule, loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), opContent2, "delete");
					}
				}
			}
		    response.getWriter().print(delnum);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"删除员工群组记录失败！");
		}
	}

	/**
	 *
	 * @param request
	 * @param response
	 */
	public void updateGroupInfo(HttpServletRequest request, HttpServletResponse response)
	{
		String lgcorpcode = request.getParameter("lgcorpcode");
		String l2gId = request.getParameter("l2gId");
		String udgId = request.getParameter("udgId");
		boolean updateok = false;
		String opType = StaticValue.UPDATE;
		String opContent = "修改所属群组";
	    try
		{
			updateok  = biz.updateGroupInfo(l2gId, udgId);
			//增加操作日志
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null)
			{
				String flag = updateok?"成功":"失败";
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				EmpExecutionContext.info(opModule, loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), "修改所属群组"+flag+"。[群组ID，所属ID]("+udgId+"，"+l2gId+")", "update");
			}
			response.getWriter().print(updateok);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"修改员工群组失败！");
		}

	}

	/**
	 * @param request
	 * @param response
	 */
	public void delGroup(HttpServletRequest request, HttpServletResponse response)
	{
		String udgId = request.getParameter("udgId");
		Integer delnum = 0;
		String opType = StaticValue.DELETE;
		String opContent = "删除群组";
		try
		{
			delnum = biz.delGroupsAllInfo(udgId);
			//增加操作日志
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null)
			{
				String flag = delnum > 0?"成功":"失败";
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				String opContent2 = "删除群组信息"+flag+"。[群组ID]("+udgId+")";
				EmpExecutionContext.info(opModule, loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), opContent2, "update");
			}
			response.getWriter().print(delnum);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"删除员工群组失败！");
		}

	}
	/**
	 * @param request
	 * @param response
	 */
	public void checkMember(HttpServletRequest request, HttpServletResponse response)
	{
		String udgId = request.getParameter("udgId");
		boolean op = false;
		try
		{
			op = MemberExists(udgId);
			response.getWriter().print(op);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"判断员工是否存在群组异常！");
		}


	}
	/**
	 *   判断该群组里存在该用户        并且加上该群组是否存在该手机
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void checkGrhaveMember(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//String lguserid = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String lguserid = SysuserUtil.strLguserid(request);

		Long userid = null;
		try{
				userid = Long.valueOf(lguserid);
		}catch (Exception e) {
			EmpExecutionContext.error(e,"员工群组当前操作员lguserid转化异常！");
		}

		Integer returnMsg = 0;
		String udgId = request.getParameter("udgId");
		String personId = request.getParameter("personId");
		String moblie = request.getParameter("moblie");
		boolean op = false;
		List<GroupInfoVo> groupInfoVo = null;
		try
		{
			op = biz.checkGrMember(udgId, personId);			//返回TRUE说明存在该人      返回FALSE说明不存在
			if(op){	//存在该人
				returnMsg = 1;
			}else{
				HashSet<String> repeatList = new HashSet<String>();
				groupInfoVo = biz.getGroupVoInfo(Long.valueOf(udgId), userid);
				if(groupInfoVo != null && groupInfoVo.size()>0){
					GroupInfoVo info = null;
					for(int i=0;i<groupInfoVo.size();i++){
						info = groupInfoVo.get(i);
						if(info != null && !"".equals(info.getMobile()) && info.getMobile()!= null){
							repeatList.add(info.getMobile());
						}
					}
					if(repeatList.size()>0 && repeatList.contains(moblie)){
						returnMsg = 3; 			//该群组里包含这个手机号码
					}else{
						returnMsg = 2; 			//	该群组没有包含此手机号码，可以转群组
					}
				}else{
					returnMsg = 2;     		//该群组里没有人
				}
			}

			response.getWriter().print(returnMsg);
		} catch (Exception e)
		{
			response.getWriter().print(0);
			EmpExecutionContext.error(e,"处理群组中是否存在该成员出现异常 ！");
		}


	}

	/**
	 * @param udgId
	 * @return
	 * @throws Exception
	 */
	public boolean MemberExists(String udgId)throws Exception
	{
		boolean exists = false;
		try
		{
			exists = biz.groupMemberExists(udgId);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"判断员工群组成员是否存在出现异常");
		}
		return exists ;
	}

	/**
	 *
	 * @param request
	 *
	 * @param response
	 *
	 */
	public void checkGpName(HttpServletRequest request, HttpServletResponse response){
		//String lguserid = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String lguserid = SysuserUtil.strLguserid(request);


		Long userid = null;
		try{
				userid = Long.valueOf(lguserid);
		}catch (Exception e) {
			EmpExecutionContext.error(e,"员工群组当前操作员lguserid转化异常！");
		}


		String udgName = request.getParameter("udgName");
		String udgId = request.getParameter("udgId");
		Integer groupMap=0;
		LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
		List<LfUdgroup> lfUdgroupList = new ArrayList<LfUdgroup>();
		conditionMap.put("udgName", udgName);
		conditionMap.put("udgId&<>",udgId);
		conditionMap.put("gpAttribute", groupMap.toString());
		conditionMap.put("userId", String.valueOf(userid));
		try
		{
			PageInfo pageInfoGroup = new PageInfo();
			pageInfoGroup.setPageSize(10000);
			pageInfoGroup.setPageIndex(1);
			lfUdgroupList = new BaseBiz().getByCondition(LfUdgroup.class,userid, conditionMap, null, pageInfoGroup);

			if(lfUdgroupList.size()>=1)
			{
				response.getWriter().print("true");
			}else
			{
				response.getWriter().print("false");
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"处理员工群组名字重复出现异常！");
		}
	}
	/**
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void addMemberToGroup(HttpServletRequest request, HttpServletResponse response) throws IOException
	{

		String lfcorpcode = request.getParameter("lgcorpcode");
		//String lguserid = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String lguserid = SysuserUtil.strLguserid(request);


		Long userid = null;
		String opUser ="";
		try{
			userid = Long.valueOf(lguserid);
			LfSysuser sysuser = new BaseBiz().getById(LfSysuser.class, lguserid);
			if(sysuser==null)
			{
				sysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			}
			opUser = sysuser==null?"":sysuser.getUserName();
		}catch (Exception e) {
			EmpExecutionContext.error(e,"员工群组获取当前操作员对象出现异常 ！");
		}


		String udgId = request.getParameter("udgId");
		String items = request.getParameter("items");
		String l2gType = request.getParameter("addrType");
		//Integer addNum = 0;
		String returnMsg = "";
		String opType = StaticValue.ADD;
		String opContent = "添加成员到群组";
		try
		{
			if(null != l2gType && 0 != l2gType.length() &&
					null != items && 0 != items.length())
			{
				String personId[] = items.split(",");
				//addNum = biz.addList2gro(udgId, personId, l2gType);
				returnMsg = biz.addList2groCheckMoblie(udgId, personId, l2gType, userid);
			}
			//this.writer.print(addNum);
			String flag= "";
			if(!returnMsg.equals("&-1"))
			{
				flag="成功";
			}
			else
			{
				flag="失败";
			}
			//增加操作日志
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null)
			{
				//日志字段
				String field = "[群组id，成员]";
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				String opContent2 = "添加成员到群组"+flag+"。"+field+"("+udgId+"，"+items+")";
				EmpExecutionContext.info(opModule, loginSysuser.getCorpCode(),loginSysuser.getUserId()+"",loginSysuser.getUserName(), opContent2, "add");
			}
			response.getWriter().print(returnMsg);
		} catch (Exception e)
		{
			response.getWriter().print("");
			EmpExecutionContext.error(e,"员工群组添加成员失败！");
			new SuperOpLog().logFailureString(opUser, opModule, opType, opContent, e,lfcorpcode);
		}finally{
			new SuperOpLog().logSuccessString(opUser, opModule, opType, opContent,lfcorpcode);
		}
	}

	public void getDepAndEmpTree1(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		try
		{
			LinkedHashMap<String, String> orderByMap = new LinkedHashMap<String, String>();
			String pageIndex = request.getParameter("pageIndex");
			PageInfo pageInfo = new PageInfo();
			pageSet(pageInfo,request);
			pageInfo.setPageSize(SIZE);
			pageInfo.setPageIndex(Integer.parseInt(pageIndex));
			orderByMap.put("employeeId",StaticValue.ASC);
			BaseBiz baseBiz = new BaseBiz();
			String epno = request.getParameter("epno");
			String epname = request.getParameter("epname");
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			String depId = request.getParameter("depId");

	        if(depId!=null&&!"".equals(depId)){
	        	conditionMap.put("depId", depId);
	        }

	        if(epname!=null&&!"".equals(epname)){
	        	conditionMap.put("name&like", epname);
	        }

	        if((depId==null||"".equals(depId))&&(epname==null||"".equals(epname)))
	        {
	        	response.getWriter().print("");
				return;
	        }

			StringBuffer sb = new StringBuffer();
			String lgcorpcode = request.getParameter("lgcorpcode");//当前登录企业
			conditionMap.put("corpCode", lgcorpcode);
			//conditionMap.put("employeeNo&like", epno);


			//List<LfEmployee> lfEmployeeList = baseBiz.getByCondition(LfEmployee.class, conditionMap, null);
			List<LfEmployee> lfEmployeeList = baseBiz.getByCondition(LfEmployee.class, null, conditionMap, orderByMap, pageInfo);

			if (lfEmployeeList != null && lfEmployeeList.size() > 0) {
				//获取员工的机构id并存入集合，key为depId
				Map<Long,String> depIdMap = new HashMap<Long,String>();
				for (LfEmployee user : lfEmployeeList)
				{
					depIdMap.put(user.getDepId(), "");
				}

				StringBuffer bufDepId = new StringBuffer();
				//循环遍历所有机构id，格式为id1,id2,id3
				Set<Long> keys = depIdMap.keySet();
		        for (Iterator<Long> it = keys.iterator(); it.hasNext();)
		        {
		        	bufDepId.append(it.next()).append(",");
		        }

		        String strDepId = null;
		        if(bufDepId != null && bufDepId.length() != 0)
		        {
		        	//截掉多余的,号
		        	strDepId = bufDepId.substring(0, bufDepId.length()-1);
		        }

		        //查询出机构对象
		        conditionMap.clear();
		        conditionMap.put("depId&in", strDepId);
		        List<LfEmployeeDep> empDepsList = baseBiz.getByCondition(LfEmployeeDep.class, conditionMap, null);
		        if (lfEmployeeList != null && lfEmployeeList.size() > 0) {
					sb.append(pageInfo.getTotalPage()).append(",").append(pageInfo.getTotalRec()).append("#");
					for (LfEmployee user : lfEmployeeList) {
							sb.append("<option value='").append(""+user.getGuId())
									.append("' etype='1' mobile='").append(user.getMobile()).append("'>");
							sb.append(user.getName().trim());

							for(LfEmployeeDep empDep : empDepsList)
							{
								if(user.getDepId().equals(empDep.getDepId()))
								{
									//带上机构名称
									sb.append(" [").append(empDep.getDepName()).append("]");
								}
							}
							sb.append("</option>");
					}
		        }
			}
			response.getWriter().print(sb.toString());

		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"获取员工群组中员工机构树出现异常！");
		}
	}

	/**
	 * 获取群组员工
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void getGroupMember(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		try
		{
			String epno = request.getParameter("epno");
			String epname = request.getParameter("epname");
			String udgId = request.getParameter("udgId");
			String udgid1 = request.getParameter("udgid1");
			String udgid2 = request.getParameter("udgid2");
			//String userId = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String userId = SysuserUtil.strLguserid(request);

			BaseBiz baseBiz = new BaseBiz();
			udgId = (udgId != null && udgId.length() > 0) ? udgId.substring(0,udgId.length()-1) : "";
			udgid1 = (udgid1 != null && udgid1.length() > 0) ? udgid1.substring(0,udgid1.length()-1) : "";
			udgid2 = (udgid2 != null && udgid2.length() > 0) ? udgid2.substring(0,udgid2.length()-1) : "";

			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("employeeNo&like", epno);
			conditionMap.put("name&like", epname);

			List<LfEmployee> lfEmployeeList = biz.getEmployeeByGuidForGroup(udgId, userId, conditionMap);
			List<LfMalist> malistList1 = biz.getMalistListByUdgId(udgId, userId, conditionMap,null);

			conditionMap.clear();
			conditionMap.put("userId", userId);
			List<LfMalist> malistList2 = baseBiz.getByCondition(LfMalist.class, conditionMap, null);


			StringBuffer sb = new StringBuffer();
			if(udgId != null && udgId.length() > 0)
			{
				if (lfEmployeeList != null && lfEmployeeList.size() > 0)
				{
					for (LfEmployee user : lfEmployeeList) {
						sb.append("<option value='").append(""+user.getGuId().toString())
								.append("' etype='1 ' mobile='").append(user.getMobile()).append("'>");
						sb.append(user.getName().trim()).append("["+ MessageUtils.extractMessage("group","group_ydbg_xzqz_text_employee",request)+"]</option>");
					}
				}
			}

			String ids = ",";
			if (malistList2 != null && malistList2.size() > 0)
			{
				for (LfMalist user : malistList2) {
					ids += user.getGuId().toString() + ",";
				}
			}
			if (malistList1 != null && malistList1.size() > 0)
			{
				for (LfMalist user : malistList1) {
					if(ids.indexOf(","+user.getGuId().toString()+",") > -1)
					{
						sb.append("<option value='").append(""+user.getGuId().toString())
							.append("' etype='2' mobile='").append(user.getMobile()).append("'>");
						sb.append(user.getName().trim()).append("["+MessageUtils.extractMessage("group","group_ydbg_xzqz_text_selfbuilt",request)+"]</option>");
					}else
					{
						sb.append("<option value='").append(""+user.getGuId().toString())
							.append("' etype='4' mobile='").append(user.getMobile()).append("'>");
						sb.append(user.getName().trim()).append("["+MessageUtils.extractMessage("group","group_ydbg_xzqz_text_shared",request)+"]</option>");
					}

				}
			}

			/*List<LfMalist> malistList2 = new AddrBookBiz().getMalistListByUdgId(udgid1, userId, conditionMap,1);
			if (malistList2 != null && malistList2.size() > 0)
			{
				for (LfMalist user : malistList2)
				{
					ids += user.getGuId().toString() + ",";
					sb.append("<option value='").append(""+user.getGuId().toString())
							.append("' etype='4' mobile='").append(user.getMobile()).append("'>");
					sb.append(user.getName().trim()).append("[共享]</option>");
				}
			}
			if(udgid2 != null && udgid2.length() > 0)
			{
				List<LfMalist> malistList3 = new AddrBookBiz().getMalistListByUdgId(udgid2, userId, conditionMap,null);
				if (malistList3 != null && malistList3.size() > 0)
				{
					for (LfMalist user : malistList3)
					{
						if(ids.indexOf(","+user.getGuId().toString()+",") > -1)
						{
							continue;
						}
						sb.append("<option value='").append(""+user.getGuId().toString())
								.append("' etype='4' mobile='").append(user.getMobile()).append("'>");
						sb.append(user.getName().trim()).append("[共享]</option>");
					}
				}
			}*/
			response.getWriter().print(sb.toString());

		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"获取员工群组中群组成员出现异常！");
		}
	}

	/**
	 * 跳转新增群组或修改群组页面
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void doEditGroupInfo(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String udgId = request.getParameter("udgId");
		String upFlag = request.getParameter("upFlag");
		String returnJson = null;
		//直接跳转新增操作
		if("0".equals(udgId)){
			request.getRequestDispatcher(this.empRoot + basePath +"/grp_editGroupInfo.jsp")
					.forward(request, response);
			return;
		}
		LfUdgroup udg = null;
		//群组中的员工信息
		List<LfEmployee> lfEmployeeList = new ArrayList<LfEmployee>();
		//群组中非来自共享的自建信息
		List<LfMalist> malistList1 = new ArrayList<LfMalist>();
		//群组中来自共享的自建信息
		List<LfMalist> malistList2 = new ArrayList<LfMalist>();
		try {
			if(udgId == null || "".equals(udgId.trim()) ||"0".equals(udgId)){
				EmpExecutionContext.error("编辑员工群组获取群组id异常！id:"+udgId);
				request.setAttribute("isDel", "1");
				return;
			}
			//-----增加解密处理----
			//加密对象
			String uid=getDecryptValue( request, udgId);
			BaseBiz baseBiz = new BaseBiz();
			udg = baseBiz.getById(LfUdgroup.class, uid);
			if(udg == null){
				EmpExecutionContext.error("编辑员工群组查询群组对象为null！");
				request.setAttribute("isDel", "1");
				return;
			}
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			lfEmployeeList = biz.getEmployeeByGuidForGroup(uid, null, conditionMap);//员工
			malistList1 = biz.getMalistListByUdgId(uid, null, conditionMap,0);//自建
			malistList2 = biz.getMalistListByUdgId(uid, null, conditionMap,1);//共享
			String employeeStr =  JSONObject.toJSONString(lfEmployeeList);
			String selfMakeStr =  JSONObject.toJSONString(malistList1);
			String shareStr =  JSONObject.toJSONString(malistList2);
			Map<String, String> map = new HashMap<String, String>();
			map.put("employee", employeeStr);
			map.put("self", selfMakeStr);
			map.put("share", shareStr);
			returnJson = JSONObject.toJSONString(map);

		}catch (Exception e){
			EmpExecutionContext.error(e,"编辑员工群组跳转页面异常！");
		}finally {
			request.setAttribute("malList2", malistList2);
			request.setAttribute("malList1", malistList1);
			request.setAttribute("empList", lfEmployeeList);
			request.setAttribute("udg", udg);
			request.setAttribute("udgId", udgId);
			if(StringUtils.isNotEmpty(upFlag)){
				response.getWriter().print(returnJson);
			}else{
				request.getRequestDispatcher(this.empRoot + basePath +"/grp_editGroupInfo.jsp")
				.forward(request, response);
			}
		}

	}
	/**
	 * 修改群组
	 * @param request
	 * @param response
	 */
	public void editGroup(HttpServletRequest request, HttpServletResponse response){
		LfSysuser sysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
		String lgcorpcode = sysuser==null?"":sysuser.getCorpCode();
		String opUser = sysuser==null?"":sysuser.getUserName();
		Long userid = sysuser==null?0L:sysuser.getUserId();
		//解密处理
		String uid = getDecryptValue( request, request.getParameter("udgId"));
		String opType = StaticValue.UPDATE;
		String opContent = "修改群组";
		String curName = "";
		String udgName = "";
		try {
			//处理新添加的记录
			String ygStr = StringUtils.defaultIfEmpty(request.getParameter("ygStr"),"");
			String qzStr = StringUtils.defaultIfEmpty(request.getParameter("qzStr"),"");
			String gxStr = StringUtils.defaultIfEmpty(request.getParameter("gxStr"),"");
			String zjStr = StringUtils.defaultIfEmpty(request.getParameter("zjStr"),"");
			//修改的群组名称
			curName = request.getParameter("curName");
			//群组名称
			udgName = request.getParameter("udgName");

			String result = groupManagerBiz.editGroup(1, curName, udgName, ygStr, qzStr, gxStr, zjStr, userid.toString(), uid, lgcorpcode);

			response.getWriter().print(result);
			//增加操作日志
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null) {
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				EmpExecutionContext.info(opModule, loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), "修改群组", "update");
			}
			new SuperOpLog().logSuccessString(opUser, opModule, opType, opContent,lgcorpcode);
		} catch (Exception e) {
			new SuperOpLog().logFailureString(opUser, opModule, opType, opContent, e,lgcorpcode);
			EmpExecutionContext.error(e, "修改群组失败！");
		}

	}
	/**
	 * 群组共享
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void groupShare(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String depIdStr = request.getParameter("depIdStr");
		//String lgcorpcode = request.getParameter("lgcorpcode");
		//从session取值，为了防止请求中恶意攻击
		LfSysuser sysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
		String lgcorpcode =sysuser==null?"":sysuser.getCorpCode();
		String groupId = request.getParameter("groupId");
		//String lguserid = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String lguserid = SysuserUtil.strLguserid(request);


		String groupName = request.getParameter("groupName");
		//单个操作员的id
	    String userIdStr1 = request.getParameter("userIdStr");
	    //*****解码处理****
        String uid=getDecryptValue(request,groupId);
		// 解析号码字符串
	    List<Long> userIdList = new ArrayList<Long>();
	    //获取部门里的操作人员
		if (depIdStr != null && !"".equals(depIdStr)) {
			userIdList = getEmpByDepId2(depIdStr, lgcorpcode);
		}
		List<LfUdgroup> lfUdgroupList = new ArrayList<LfUdgroup>();
		LfUdgroup udgroup = null;
		if(userIdStr1 != null && !"".equals(userIdStr1)){
			String userIdArr[] = userIdStr1.split(",");
			for(int i=0;i<userIdArr.length;i++){
				if(!"".equals(userIdArr[i])&&!userIdList.contains(Long.valueOf(userIdArr[i]))){
					userIdList.add(Long.valueOf(userIdArr[i]));
				}
			}
		}
		//删除自己
		if(userIdList.contains(Long.valueOf(lguserid))){
			userIdList.remove(Long.valueOf(lguserid));
		}
		try {
		/**
		 * 	修改时允许共享为空
			if(userIdList == null || userIdList.size()==0){
				response.getWriter().print("noShareSelf");
				return ;
			}
		*/
			if(userIdList==null){userIdList = new ArrayList<Long>();}
			List<LfUdgroup> oldList = this.getSharedUserByGroupId(uid);
			List<Long> oldUserId = new ArrayList<Long>();
			List<Long> addList = new ArrayList<Long>();
			String deleteList = "";
			for(LfUdgroup lfUdgroup:oldList){
				oldUserId.add(lfUdgroup.getReceiver());
				if(!userIdList.contains(lfUdgroup.getReceiver())){
					deleteList += lfUdgroup.getUdgId()+",";
				}
			}
			if(!"".equals(deleteList)){
				deleteList = deleteList.substring(0,deleteList.lastIndexOf(","));
			}
			for(Long userid:userIdList){
				if(!oldUserId.contains(userid)){
					addList.add(userid);
				}
			}
			for (Long userid : addList) {
				//添加共享群组
					udgroup = new LfUdgroup();
					udgroup.setUserId(Long.valueOf(lguserid));
					udgroup.setUdgName(groupName);
					udgroup.setGpAttribute(0);
					udgroup.setGroupType(2);
					udgroup.setSharetype(1);
					udgroup.setGroupid(Long.valueOf(uid));
					udgroup.setReceiver(userid);
					udgroup.setSendmode(1);
					//共享状态,设置为1:已共享;因共享群组无共享状态,所以设置为3
					udgroup.setShareStatus(3);
					//共享群组的创建时间
					udgroup.setCreateTime(new Timestamp(System.currentTimeMillis()));
					lfUdgroupList.add(udgroup);
			}
			//更新共享状态标志
			LfUdgroup  temp=this.getLfUdgroupByUdgId(uid);
			if(userIdList==null||userIdList.size()==0) {
				temp.setShareStatus(0);
			}else {
				temp.setShareStatus(1);
			}
			if(lfUdgroupList.size()>0 || !"".equals(deleteList)){
//				boolean result = biz.shareGroup(lfUdgroupList, deleteList);
				boolean result = biz.shareGroup(lfUdgroupList, deleteList,temp);
				//增加操作日志
				Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
				if(loginSysuserObj!=null)
				{
					//日志字段
					String field = "[群组ID，群组名称，增加共享人数，删除共享人人数]";
					String flag = result?"成功":"失败";
					LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
					int addNum = lfUdgroupList.size();
					int deleteNum = deleteList.length()>0?deleteList.split(",").length:0;
					String editBeforeDate = "("+uid+"，"+groupName+"，"+addNum+"，"+deleteNum+")";
					String opContent = "共享群组"+flag+"。"+field+editBeforeDate;
					EmpExecutionContext.info(opModule,loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), opContent,"other");
					}
				response.getWriter().print(result);
			}else{
				response.getWriter().print("havingShare");
			}
		} catch (Exception e) {
			response.getWriter().print("false");
			EmpExecutionContext.error(e,"处理员工群组中共享群组出现异常！");
		}

	}

	//通过id字符串获取员工成员列表(改)depIds为,e1,3,10,e23,这种类型的字符串
	private List<Long> getEmpByDepId2(String depIds, String corpCode)
	{
		StringBuffer userIds= new StringBuffer();
		List<Long> userId = new ArrayList<Long>();
		BaseBiz baseBiz = new BaseBiz();
		try {
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			String[] tempDepIds = depIds.split(",");
			List<String> depIdsList = Arrays.asList(tempDepIds);
			List<String> depIdsList2= new ArrayList<String>();
			List<String> depIdsList3= new ArrayList<String>();
			for (int a = depIdsList.size() - 1; a >= 0; a--) {
				if (!"".equals(depIdsList.get(a))) {
					if (depIdsList.get(a).indexOf("e") > -1) {
						depIdsList3.add(depIdsList.get(a));// 包含子机构的
					} else {
						//不包含子机构
						depIdsList2.add(depIdsList.get(a));
					}
				}
			}
			StringBuffer buffer = new StringBuffer("");
			List<LfSysuser> sysusers =null;
			int j=0;
			//先遍历不包含子机构的
			for (int i = 0; i < depIdsList2.size(); i++) {
				if (depIdsList2.get(i) != null) {
					buffer.append(depIdsList2.get(i) + ",");
					j++;
				}
				if (j >= 999) {
					j = 0;
					conditionMap.put("depId&in", buffer.toString());
					conditionMap.put("corpCode", corpCode);
					sysusers = baseBiz.getByCondition(LfSysuser.class,
							conditionMap, null);
					if (sysusers != null && sysusers.size() > 0) {
						for (LfSysuser sysuser : sysusers) {
							userIds.append(sysuser.getUserId()).append(",");
							userId.add(sysuser.getUserId());
						}
					}
					buffer = new StringBuffer("");
				} else if (i == depIdsList2.size() - 1) {
					conditionMap.put("depId&in", buffer.toString());
					conditionMap.put("corpCode", corpCode);
					sysusers = baseBiz.getByCondition(LfSysuser.class,
							conditionMap, null);
					if (sysusers != null && sysusers.size() > 0) {
						for (LfSysuser sysuser : sysusers) {
							userIds.append(sysuser.getUserId()).append(",");
							userId.add(sysuser.getUserId());
						}
					}
				}
			}
			//再遍历包含子机构的
			DepDAO depVoDao = new DepDAO();
			GroupManagerBiz groupManagerBiz = new GroupManagerBiz();
			for (int y = 0; y < depIdsList3.size(); y++) {
				if (depIdsList3.get(y) != null
						&& !"".equals(depIdsList3.get(y))
						&& depIdsList3.get(y).indexOf("e") > -1) {
					String depIdCon =
						depVoDao.getChildUserDepByParentID(
									Long.parseLong(depIdsList3.get(y)
											.substring(1)), TableLfDep.DEP_ID);
					List<LfSysuser> sysuserList = groupManagerBiz
							.getLfSysuserListByDepIdCon(depIdCon);
					for (LfSysuser sysuser : sysuserList) {
						userIds.append(sysuser.getUserId()).append(",");
						userId.add(sysuser.getUserId());
					}
				}
			}

		} catch (Exception e) {
			EmpExecutionContext.error(e,"处理员工群组中机构包含出现异常！");
		}
		return userId;
	}

	/**
	 *   获取机构的 数，只 限查子级
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void getEmpSecondDepJson(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		try
		{
			String depId = request.getParameter("depId");
			//String lguserid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);


			LfSysuser sysuser = getLoginUser(request);
			List<LfEmployeeDep> empDepList = biz.getEmpSecondDepTreeByUserIdorDepId(sysuser.getUserId()+"",depId,sysuser.getCorpCode());
			LfEmployeeDep dep = null;
			StringBuffer tree = new StringBuffer("");
			if(empDepList != null && empDepList.size()>0){
				tree.append("[");
				for (int i = 0; i < empDepList.size(); i++) {
					dep = empDepList.get(i);
					tree.append("{");
					tree.append("id:'").append(dep.getDepId()+"'");
					tree.append(",name:'").append(dep.getDepName()).append("'");
					tree.append(",pId:'").append(dep.getParentId()+"'");
					tree.append(",depcodethird:'").append(dep.getDepcodethird()+"'");
					//tree.append(",dlevel:").append(dep.getDepLevel());
					//tree.append(",depCode:'").append(dep.getDepCode()+"'");
					tree.append(",isParent:").append(true);
					tree.append("}");
					if(i != empDepList.size()-1){
						tree.append(",");
					}
				}
				tree.append("]");
			}
			response.getWriter().print(tree.toString());
		} catch (Exception e)
		{
			response.getWriter().print("");
			EmpExecutionContext.error(e,"获取员工群组中员工机构出现异常！");
		}
	}

	/**
	 * 文件上传处理
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	public void upload(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		List<FileItem> fileList = null;
		PhoneUtil phoneUtil = new PhoneUtil();
		StringBuffer sb = new StringBuffer();
		try {
			fileList = upload.parseRequest(request);
		} catch (FileUploadException e) {
			EmpExecutionContext.error(e,"员工群组上传号码文件出现异常！");
		}
		Iterator<FileItem> it = fileList.iterator();
		HashSet<String> repeatList = new HashSet<String>();
		while (it.hasNext()) {
			Pattern pa = Pattern.compile(" {2,}");

			FileItem fileItem = it.next();
			if (!fileItem.isFormField()
					&& fileItem.getName().length() > 0) {
				BufferedReader reader = null;
				String tmp;

				try {
					String[] haoduan = biz.getHaoduan();
					String fileCurName = fileItem.getName();
					String fileType = fileCurName.substring(fileCurName.lastIndexOf("."));
					if(fileType.equalsIgnoreCase(".xls")){
                        String fileHeader = getFileHeader(fileItem.getInputStream());//文件头
                        if(!"D0CF11E0".equals(fileHeader)){//文件头判断 xls文件头为D0CF11E0
                            continue;
                        }
						Workbook workBook = Workbook.getWorkbook(fileItem.getInputStream());
						Sheet sh = workBook.getSheet(0);
						for (int k = 0; k < sh.getRows(); k++)
						{
							LfMalist malist = new LfMalist();
							Cell[] cells = sh.getRow(k);
							if(cells.length<2)
							{
								continue;
							}else
							{
								String name = cells[0].getContents();
								if(name!=null){
									name = name.replaceAll("[&|\\|]","");
								}
								malist.setName("".equals(name)?"无":name);
								malist.setMobile(cells[1].getContents());
								if (phoneUtil.getPhoneType(malist.getMobile(), haoduan) != -1
										&& checkRepeat(repeatList,malist.getName()+"#HS#"+malist.getMobile()))
								{
									sb.append(malist.getName()).append("|").append(malist.getMobile()).append("&");
								}
							}
						}
					}else if(fileType.equalsIgnoreCase(".txt"))
					{

						reader = new BufferedReader(new InputStreamReader(
								fileItem.getInputStream(), "GBK"));
						while ((tmp = reader.readLine()) != null) {
							LfMalist malist = new LfMalist();
							try{
								tmp = tmp.trim();
								Matcher ma = pa.matcher(tmp);
								tmp = ma.replaceAll(" ");
								tmp = tmp.replaceAll("[&|\\|]","");
								int index = tmp.lastIndexOf(" ");
								if(index<1){
									malist.setName("无");
									malist.setMobile(tmp);
								}else{
									malist.setName(tmp.substring(0,index));
									malist.setMobile(tmp.substring(index+1));
								}
								if (phoneUtil.getPhoneType(malist.getMobile(), haoduan) != -1
										&& checkRepeat(repeatList,malist.getName()+"#HS#"+malist.getMobile()))
								{
									sb.append(malist.getName()).append("|").append(malist.getMobile()).append("&");
								}
							}catch (Exception e) {
								EmpExecutionContext.error(e,"员工群组读取号码文件出现异常！");
							}

						}
					}else if (fileType.equalsIgnoreCase(".xlsx")) {
                    	//上传文件格式为.xlsx
                    	String uploadPath = StaticValue.FILEDIRNAME;// file/smstxt/
            			String temp = new TxtFileUtil().getWebRoot()+uploadPath;
            			Excel2007VO excel=new Excel2007Reader().fileParset(temp, fileItem.getInputStream());
            			int maxNum = 2000;
            			String[] cells;
        				reader=excel.getReader();
        				String charset = new ChangeCharset().get_charset(fileItem.getInputStream());
        				if(charset.startsWith("UTF-"))
        				{
        					reader.read(new char[1]);
        				}
        				//String tmp="";
        				//计算行数
        				int k=-1;
        				while ((tmp = reader.readLine()) != null && k <= maxNum)
        				{
        					String name="";
        					String phone="";
        					// 循环获得单元行
        					if("".equals(tmp)) {
        						continue;
        					}
        					k++;
        					cells = tmp.split(",");
        					int size = cells.length;
        					//用户名
        					if (size >1){
                                 name = cells[1];
        					}
        					//手机号
        					if (size >2){
        						 phone = cells[2];
        					}
        					//不是两个字段
        					if (size >3) {
        						continue;
        					}
                            //如果名字或手机号为空跳过
                            if(StringUtils.isEmpty(name) || StringUtils.isEmpty(phone)) continue;
                            //如果名字带有特殊符号则直接跳过
                            if(name.matches(".*[\\[\\]\\-<>!@#$%^&*_+='/?,.`~:;\"\\\\].*"))continue;
                            LfMalist malist = new LfMalist();
                            malist.setName(name);
                            malist.setMobile(phone);
                            if (phoneUtil.getPhoneType(malist.getMobile(), haoduan) != -1
									&& checkRepeat(repeatList, malist.getName() + "#HS#" + malist.getMobile())) {
								sb.append(malist.getName()).append("|").append(malist.getMobile()).append("&");
							}
        				}
					}
				} catch (Exception e) {
					EmpExecutionContext.error(e,"员工群组处理文件属性出现异常！");
				} finally {
					try {
						if(reader!=null)reader.close();
					} catch (IOException e) {
						EmpExecutionContext.error(e,"员工群组关闭文件流出现异常！");
					}
					fileItem.delete();
					request.setAttribute("masb", sb.toString());
					request.getRequestDispatcher(
							this.empRoot + basePath + "/grp_uploadgroup.jsp").forward(
							request, response);
				}
			}
		}
	}


    /**
     * 获取操作员机构树
     * @param request
     * @param response
     * @throws Exception
     */
	public void createTree2(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try{
			/*java.util.Enumeration<String> a = request.getParameterNames();
			
			while (a.hasMoreElements()) {
				String string = (String) a.nextElement();
				System.out.println(string+"    "+request.getParameter(string));
			}*/
            LfSysuser sysuser = getLoginUser(request);
			Long depId = null;
			//机构id
			String depStr = request.getParameter("depId");
			Long userId = sysuser.getUserId();

			if(depStr != null && !"".equals(depStr.trim())){
				depId = Long.parseLong(depStr);
			}
			//获取机构树字符串
			String departmentTree = getDepartmentJosnData2(depId,sysuser);
			response.getWriter().print(departmentTree);
		} catch (Exception e) {
			response.getWriter().print("");
			EmpExecutionContext.error(e,"员工群组获取操作员机构出现异常！");
		}
	}
	
	
	/**
	 * 获取操作员机构树
	 * @return
	 */
	protected String getDepartmentJosnData2(Long depId, Long userId){
		StringBuffer tree = null;
		LfSysuser sysuser = null;
		try {
			sysuser = new BaseBiz().getById(LfSysuser.class, userId);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"员工群组获取当前操作员对象出现异常！");
			tree = new StringBuffer("[]");
		}
		if(sysuser!=null&&sysuser.getPermissionType()==1)
		{
			tree = new StringBuffer("[]");
		}else
		{
			DepBiz depBiz = new DepBiz();
			List<LfDep> lfDeps;
			
			try {
				lfDeps = null;
				
				if(depId == null){
					lfDeps = new ArrayList<LfDep>();
					/*备份 pengj
					LfDep lfDep = depBiz.getAllDeps(userId).get(0);//这里需要优化
					*/
					//新增 pengj
					LfDep lfDep = depBiz.getAllDepByUserIdAndCorpCode(userId, sysuser==null?"":sysuser.getCorpCode()).get(0);//这里需要优化
					
					lfDeps.add(lfDep);
					//lfDeps.addAll(new DepBiz().getDepsByDepSuperId(lfDep.getDepId()));
				}else{
					lfDeps = new DepBiz().getDepsByDepSuperId(depId);
				}
				
				
				LfDep lfDep = null;
				tree = new StringBuffer("[");
				for (int i = 0; i < lfDeps.size(); i++) {
					lfDep = lfDeps.get(i);
					tree.append("{");
					tree.append("id:").append(lfDep.getDepId());
					tree.append(",name:'").append(lfDep.getDepName()).append("'");
					tree.append(",pId:").append(lfDep.getSuperiorId());
					tree.append(",depId:'").append(lfDep.getDepId()+"'");
					tree.append(",dlevel:'").append(lfDep.getDepLevel()+"'");
					tree.append(",isParent:").append(true);
					tree.append("}");
					if(i != lfDeps.size()-1){
						tree.append(",");
					}
				}
				tree.append("]");
		
			} catch (Exception e) {
				EmpExecutionContext.error(e,"员工群组获取操作员机构树出现异常！");
				tree = new StringBuffer("[]");
			}
		}
		return tree.toString();
	}

    /**
     * 获取当前操作员权限下 对应机构下的树
     * @param depId
     * @param sysuser
     * @return
     */
    protected String getDepartmentJosnData2(Long depId, LfSysuser sysuser){
        StringBuffer tree = null;
        if(sysuser.getPermissionType()==1)
        {
            tree = new StringBuffer("[]");
        }else
        {
            DepBiz depBiz = new DepBiz();
            List<LfDep> lfDeps;

            try {
                lfDeps = null;

                if(depId == null){
                    lfDeps = new ArrayList<LfDep>();
                    LfDep lfDep = depBiz.getAllDepByUserIdAndCorpCode(sysuser.getUserId(),sysuser.getCorpCode()).get(0);//这里需要优化
                    lfDeps.add(lfDep);
                }else{
                    lfDeps = new DepBiz().getDepsByDepSuperIdAndCorpCode(depId,sysuser.getCorpCode());
                }
                LfDep lfDep = null;
                tree = new StringBuffer("[");
                for (int i = 0; i < lfDeps.size(); i++) {
                    lfDep = lfDeps.get(i);
                    tree.append("{");
                    tree.append("id:").append(lfDep.getDepId());
                    tree.append(",name:'").append(lfDep.getDepName()).append("'");
                    tree.append(",pId:").append(lfDep.getSuperiorId());
                    tree.append(",depId:'").append(lfDep.getDepId()+"'");
                    tree.append(",dlevel:'").append(lfDep.getDepLevel()+"'");
                    tree.append(",isParent:").append(true);
                    tree.append("}");
                    if(i != lfDeps.size()-1){
                        tree.append(",");
                    }
                }
                tree.append("]");

            } catch (Exception e) {
                EmpExecutionContext.error(e,"员工群组获取操作员机构树出现异常！");
                tree = new StringBuffer("[]");
            }
        }
        return tree.toString();
    }
	//判断机构是否被包含在其它机构
//	private boolean isDepAcontainsDepB(String depIdAs, String depIdB,
//                                       String corpCode) {
//		boolean result = false;
//		LinkedHashSet<Long> depIdSet = new LinkedHashSet<Long>();
//		String[] depIdAsTemp = depIdAs.split(",");
//		try {
//			for (int a = 0; a < depIdAsTemp.length; a++) {
//				if (depIdAsTemp[a] != null && !"".equals(depIdAsTemp[a])) {
//					String deps = new DepDAO()
//							.getChildUserDepByParentID(null, Long
//									.valueOf(depIdAsTemp[a]));
//					String depArray[] = deps.split(",");
//					for (int i = 0; i < depArray.length; i++) {
//						depIdSet.add(Long.valueOf(depArray[i]));
//					}
//				}
//			}
//			result = depIdSet.contains(Long.valueOf(depIdB));
//		} catch (Exception e) {
//			EmpExecutionContext.error(e,"员工群组处理机构包含出现异常！");
//		}
//		return result;
//	}


}
