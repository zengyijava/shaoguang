package com.montnets.emp.ydyw.ywpz.servlet;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.DepBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.impl.GenericEmpDAO;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.biztype.LfBusManager;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.template.LfTemplate;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.ydyw.ywpz.biz.ydyw_busTempConfBiz;
import com.montnets.emp.ydyw.ywpz.entity.LfBusTailTmp;
import com.montnets.emp.ydyw.ywpz.vo.LfBusTailTmpVo;

public class ydyw_busTempConfSvt extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1631881261309964580L;

	private final String empRoot="ydyw";
	private final String base="/ywpz";
	private final BaseBiz baseBiz=new BaseBiz();
	
	private final ydyw_busTempConfBiz ydywBiz = new ydyw_busTempConfBiz();
	
	public void find(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Long userId = 0L;
		String corpCode = "";
		PageInfo pageInfo = new PageInfo();
		List<LfBusTailTmpVo> busTmpList = new ArrayList<LfBusTailTmpVo>();
		
		//添加与日志相关 p
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");//设置日期格式
		long startTimeByLog = System.currentTimeMillis();  //开始时间
		
		//是否第一次进入界面
		boolean isFirst = true;
		isFirst = request.getParameter("pageIndex") == null && request.getParameter("pageSize") == null;
		
		//获取表单提交的参数值
		String busName = request.getParameter("busName");  //业务名称
		if (busName != null && !busName.equals("")) {
			busName = busName.toUpperCase().trim();
		}
		
		//业务编码
		String busCode = request.getParameter("busCode");
		if (busCode != null && !busCode.equals("")) {
			busCode = busCode.toUpperCase().trim();
		}
		
		
		String depId = request.getParameter("depId");
		String depName = request.getParameter("depNam");
		//操作员
		String userName = request.getParameter("userName");
		if (userName != null && !userName.equals("")) {
			userName = userName.trim();
		}
		
		//配置开始时间
		String startTime = request.getParameter("startTime");
		//配置结束时间
		String endTime = request.getParameter("endTime");
		//是否包含下级机构
		String isCheck = request.getParameter("isCheck");
		if (null == isCheck || "".equals(isCheck)) {
			if (isFirst) {
				isCheck = "1";
			} else {
				isCheck = "0";
			}			
		}
		
		request.setAttribute("busName", busName);
		request.setAttribute("busCode", busCode);
		request.setAttribute("depId", depId);
		request.setAttribute("depName", depName);
		request.setAttribute("userName", userName);
		request.setAttribute("startTime", startTime);
		request.setAttribute("endTime", endTime);
		request.setAttribute("isCheck", isCheck);
				
		//页码
		String pageIndex = request.getParameter("pageIndex");
		String pageSize = request.getParameter("pageSize");
		
		try {
			//获取当前登录用户
			LfSysuser sysuser = (LfSysuser)request.getSession(false).getAttribute("loginSysuser");
			//获取当前登录用户ID
			userId = sysuser.getUserId();
			
			corpCode = sysuser.getCorpCode();
			//权限
			request.setAttribute("permissionType", sysuser.getPermissionType());
			
			//设置从页面传值过来的页码
			if (pageIndex != null && !pageIndex.equals("")) {
				pageInfo.setPageIndex(Integer.valueOf(pageIndex));
				pageInfo.setPageSize(Integer.valueOf(pageSize));
			}
						
			if (null != depId && !depId.equals("")) {
				//是否包含子机构
				if (null != isCheck && !isCheck.equals("") 
						&& isCheck.equals("1")) {
					if (depId.toString().equals("1")) {
						depId = "";
					} else {
						//非顶级机构，查询本机构及其下级机构
						DepBiz depBiz = new DepBiz();
						
						List<Long> depIds = depBiz.getChildDepIds(Long.valueOf(depId), corpCode);
						depId = "";
						
						for (int i = 0; i < depIds.size(); i++) {
							depId += depIds.get(i).toString() + ",";
						}
						
						
						if (depId.indexOf(",") != -1) {
							depId = depId.substring(0, depId.lastIndexOf(","));
						}
					}
					
				} 
				
			} else {
				depId = sysuser.getDepId().toString();				
				
				//是否包含子机构
				if (null != isCheck && !isCheck.equals("") 
						&& isCheck.equals("1")) {		
					if (depId.toString().equals("1")) {
						depId = "";
					} else {
						//非顶级机构，查询本机构及其下级机构
						DepBiz depBiz = new DepBiz();
						
						List<Long> depIds = depBiz.getChildDepIds(Long.valueOf(depId), corpCode);
						depId = "";
						
						for (int i = 0; i < depIds.size(); i++) {
							depId += depIds.get(i).toString() + ",";
						}
						
						if (depId.indexOf(",") != -1) {
							depId = depId.substring(0, depId.lastIndexOf(","));
						}
					}
					
				} 				
			}
		
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("name&like", userName);
			conditionMap.put("corpCode", corpCode);	
			
			//通过用户查询时
			boolean flag = true;
			String id = "";
			if (userName != null && !"".equals(userName)) {
				//根据页面提交过来的操作员名称
				//个人权限
				if (sysuser.getPermissionType() == 1) {					
					conditionMap.put("userId", sysuser.getUserId().toString());			
				}
				
				List<LfSysuser> userList = baseBiz.getByCondition(LfSysuser.class, conditionMap, null);
				
				if (userList != null && userList.size() > 0) {
					for (LfSysuser lfSysuser : userList) {
						id += lfSysuser.getUserId().toString();
						id += ",";
					}	
					
					id += sysuser.getUserId();
				} else {
					flag = false;
				}
			} else {
				//个人权限
				if (sysuser.getPermissionType() == 1) {					
					id = sysuser.getUserId().toString();					
				}
			}
			
			if (flag) {				
				if(id.indexOf(",") != -1) {
					id = id.substring(0, id.lastIndexOf(","));
				}
				
				if ((busName == null || busName.equals("")) && 
						(busCode == null || busCode.equals("")) &&
						(userName == null || userName.equals(""))) {
					//depId = null;
				} else {						
					if (sysuser.getUserId().longValue() == 2) {
						depId = null;
					} 
				}
				
				//获取已配置的模板信息
				if (sysuser.getPermissionType() == 1) {
					id = userId.toString();
					depId = "";
				}  
				//通过条件查询业务模板配置
				busTmpList = ydywBiz.findByParams(busName, busCode, depId, id, 
						startTime, endTime, pageInfo, corpCode, sysuser.getPermissionType());
				
				
			}
			
		} catch (Exception e) {
			EmpExecutionContext.error(e,"业务模板配置查询失败！");
		} finally {
			
			//添加与日志相关 p
			long endTimeByLog = System.currentTimeMillis();  //查询结束时间
			long consumeTimeByLog = endTimeByLog - startTimeByLog;  //查询耗时
			
			try {
				//增加操作日志 p
				Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
				if(loginSysuserObj!=null){
					LfSysuser loginSysuser =(LfSysuser)loginSysuserObj;
					String opContent = "查询开始时间："+sdf.format(startTimeByLog)+"，耗时："+consumeTimeByLog+"ms"+"，查询总数："+pageInfo.getTotalRec();
					
					EmpExecutionContext.info("业务模板配置", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
							loginSysuser.getUserName(), opContent, "GET");
				}
			} catch (Exception e) {
				EmpExecutionContext.error(e, "添加操作日志异常！");
			}
			
			
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("busTmpList", busTmpList);
			request.getRequestDispatcher(this.empRoot  + base  + "/ydyw_busTempConf.jsp?lguserid=" + 
					userId + "&lgcorpcode=" + corpCode).forward(
					request, response);
		}
	}
	
	/**
	 * 配置模板，页面跳转
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void toConfig(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			//获取当前登录用户
			LfSysuser sysuser = (LfSysuser)request.getSession(false).getAttribute("loginSysuser");
			//获取企业编码
			String corpCode = sysuser.getCorpCode();
			//获取未配置过的业务类型
			String value = "select distinct BUS_ID from LF_BUS_TAILTMP where ASSOCIATE_TYPE=1";
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("busId&not in", value);
			conditionMap.put("state", "0");
			conditionMap.put("busType&in", "1,2");
			conditionMap.put("corpCode", corpCode);
			
			LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
			orderbyMap.put("busId", StaticValue.DESC);
			
			//获取业务
			List<LfBusManager> busList = baseBiz.getByCondition(LfBusManager.class, conditionMap, orderbyMap);
			
			//获取短信模板
			conditionMap.clear();
			conditionMap.put("tmpType", "3");
			conditionMap.put("tmState", "1");
			conditionMap.put("dsflag&in", "0,1");
            List<LfSysuser> userList = new ArrayList<LfSysuser>();
            if (sysuser.getPermissionType() == 1) {
				conditionMap.put("userId", sysuser.getUserId().toString());
			} else {
				//获取当前操作员所属机构的用户
				LinkedHashMap<String, String> conditionMap1 = new LinkedHashMap<String, String>();
				LinkedHashMap<String, String> orderbyMap1 = new LinkedHashMap<String, String>();
				//机构ID
				String depId = sysuser.getDepId().toString();
				String dids = "";
				
				if (!depId.toString().equals("1")) {
					//查询本机构及其下级机构
					DepBiz depBiz = new DepBiz();
					
					//获取下级机构
					List<Long> depIds = depBiz.getChildDepIds(Long.valueOf(depId), corpCode);
					
					for (int i = 0; i < depIds.size(); i++) {
						dids += depIds.get(i).toString() + ",";
					}
					
					if (dids.indexOf(",") != -1) {
						dids = dids.substring(0, dids.lastIndexOf(","));
					}
					
					conditionMap1.put("depId&in", dids);
				} 
				
				orderbyMap1.put("userId", StaticValue.DESC);
				//获取操作员ID
				userList = baseBiz.getByCondition(LfSysuser.class, conditionMap1, orderbyMap1);
				String ids = "";
				if (userList != null && userList.size() > 0) {
					for (LfSysuser ls : userList) {
						ids += ls.getUserId() + ",";
					}
				}
				
				ids += sysuser.getUserId();
				if (ids.indexOf(",") != -1) {
					ids = ids.substring(0, ids.lastIndexOf(","));
				}
				
				conditionMap.put("userId&in", ids);
			}
			
			orderbyMap.clear();
			orderbyMap.put("tmid", StaticValue.DESC);
            List<LfTemplate> tempList;
            //根据过滤条件查询对象集合,判断in(1,2,3...)里面是否大于1000个数，若太多sql会抛异常，因此需要做分隔处理
            if(userList.size() < 1000) {
                tempList = baseBiz.getByCondition(LfTemplate.class, conditionMap, orderbyMap);
            }else {
                // 分隔处理查询
                tempList = ydywBiz.getLfTemplate(conditionMap, userList, sysuser);
            }
			
			request.setAttribute("type", "1");
			request.setAttribute("busList", busList);
			request.setAttribute("tempList", tempList);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "业务配置新建页面跳转异常！");
		}
		
		request.getRequestDispatcher(this.empRoot  + base  + "/ydyw_cfg.jsp").forward(
				request, response);
	}
	
	/**
	 * 点击修改模板配置进行页面跳转
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void toEdit(HttpServletRequest request, HttpServletResponse response) throws Exception {		
		try {
			//获取当前登录用户
			LfSysuser sysuser = (LfSysuser)request.getSession(false).getAttribute("loginSysuser");
			if(sysuser == null)return;
			String corpCode = sysuser.getCorpCode();
			//业务id
			String busId = request.getParameter("busId");
			
			if (busId == null || busId.equals("0")) {
				toConfig(request, response);
				return;
			}
			
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("busId", busId);
			conditionMap.put("associateType", "1");
			
			LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
			orderbyMap.put("id", StaticValue.DESC);
			
			//获取busId对应的已配置的模板id
			List<LfBusTailTmp> taiList = baseBiz.getByCondition(LfBusTailTmp.class, conditionMap, orderbyMap);
			
			//获取模板ID
			String tmIds = "";
			if (null != taiList && taiList.size() > 0) {
				for (LfBusTailTmp lfBusTailTmp : taiList) {
					tmIds += lfBusTailTmp.getTmId();
					tmIds += ",";
				}
				
				if (tmIds.indexOf(",") != -1) {
					tmIds = tmIds.substring(0, tmIds.lastIndexOf(","));
				}
				
				conditionMap.clear();
				conditionMap.put("tmid&in", tmIds);
				
				orderbyMap.clear();
				orderbyMap.put("tmid", StaticValue.DESC);
				
				List<LfTemplate> tmpList = baseBiz.getByCondition(LfTemplate.class, conditionMap, orderbyMap);
				request.setAttribute("tmpList", tmpList);
			}
			
			conditionMap.clear();
			//获取所有的短信模板
			if (sysuser.getPermissionType() == 1) {
				conditionMap.put("userId", sysuser.getUserId().toString());
			} else {
				//获取当前操作员所属机构的用户
				LinkedHashMap<String, String> conditionMap1 = new LinkedHashMap<String, String>();
				LinkedHashMap<String, String> orderbyMap1 = new LinkedHashMap<String, String>();
				//机构ID
				String depId = sysuser.getDepId().toString();
				String dids = "";
				
				if (!depId.toString().equals("1")) {
					//查询本机构及其下级机构
					DepBiz depBiz = new DepBiz();
					
					//获取下级机构
					List<Long> depIds = depBiz.getChildDepIds(Long.valueOf(depId), corpCode);
					
					for (int i = 0; i < depIds.size(); i++) {
						dids += depIds.get(i).toString() + ",";
					}
					
					if (dids.indexOf(",") != -1) {
						dids = dids.substring(0, dids.lastIndexOf(","));
					}
					
					conditionMap1.put("depId&in", dids);
				}
							
				orderbyMap1.put("userId", StaticValue.DESC);
				//获取操作员ID
				List<LfSysuser> userList = baseBiz.getByCondition(LfSysuser.class, conditionMap1, orderbyMap1);
				String ids = "";
				if (userList != null && userList.size() > 0) {
					for (LfSysuser ls : userList) {
						ids += ls.getUserId() + ",";
					}
				}
				
				ids += sysuser.getUserId();
				if (ids.indexOf(",") != -1) {
					ids = ids.substring(0, ids.lastIndexOf(","));
				}
				
				conditionMap.put("userId&in", ids);
			}		
			
			conditionMap.put("tmpType", "3");
			conditionMap.put("tmState", "1");
			conditionMap.put("dsflag&in", "0,1");
			
			orderbyMap.clear();
			orderbyMap.put("tmid", StaticValue.DESC);
			
			List<LfTemplate> tempList = baseBiz.getByCondition(LfTemplate.class, conditionMap, orderbyMap);
			
			//获取业务
			//获取未配置过的业务类型
			conditionMap.clear();
			conditionMap.put("state", "0");
			
			orderbyMap.clear();
			orderbyMap.put("busId", StaticValue.DESC);		
			
			List<LfBusManager> busList = baseBiz.getByCondition(LfBusManager.class, null, orderbyMap);
			
			request.setAttribute("yesOrNo", "");
			request.setAttribute("type", "2");
			request.setAttribute("busId", busId);
			request.setAttribute("busList", busList);
			request.setAttribute("tempList", tempList);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "修改业务配置页面跳转异常！");
		}
		
		request.getRequestDispatcher(this.empRoot  + base  + "/ydyw_cfg.jsp").forward(
				request, response);
	}	
	
	/**
	 * 新增、修改配置
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void update(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int saveCount = 0;
		boolean flag = false;
		String type = request.getParameter("type");
		String busId = request.getParameter("busId");
		String tmIds = request.getParameter("tmIds");
		LfBusTailTmp lbtt = null;
		List<LfBusTailTmp> saveList = new ArrayList<LfBusTailTmp>();
		
		try {
			//获取当前系统时间
			Calendar cal = Calendar.getInstance();
			String toDay = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());
			
			//获取当前登录用户
			LfSysuser sysuser = (LfSysuser)request.getSession(false).getAttribute("loginSysuser");
			if(sysuser == null){return;}
			String[] ids = {};
			
			//去掉最后一个逗号
			tmIds = tmIds.substring(0, tmIds.lastIndexOf(","));
			
			if (-1 != tmIds.indexOf(",")) {
				ids = tmIds.split(",");
				
				for (int i = 0; i < ids.length; i++) {
					//
					lbtt = new LfBusTailTmp();
					lbtt.setBusId(Long.valueOf(busId));
					lbtt.setTmId(Long.valueOf(ids[i]));
					lbtt.setAssociateType(1);
					lbtt.setCorpCode(sysuser.getCorpCode());
					lbtt.setUserId(sysuser.getUserId());
					lbtt.setDepId(sysuser.getDepId());
					lbtt.setCreateTime(Timestamp.valueOf(toDay));
					lbtt.setUpdateTime(Timestamp.valueOf(toDay));
					
					saveList.add(lbtt);
				}
			} else {
				//
				lbtt = new LfBusTailTmp();
				
				lbtt.setBusId(Long.valueOf(busId));
				lbtt.setTmId(Long.valueOf(tmIds));
				lbtt.setAssociateType(1);
				lbtt.setCorpCode(sysuser.getCorpCode());
				lbtt.setUserId(sysuser.getUserId());
				lbtt.setDepId(sysuser.getDepId());
				lbtt.setCreateTime(Timestamp.valueOf(toDay));
				lbtt.setUpdateTime(Timestamp.valueOf(toDay));
				
				saveList.add(lbtt);
			}
			
			
			if (type.equals("1")) {
				//新增模板配置			
				
				saveCount = baseBiz.addList(LfBusTailTmp.class, saveList);
				
				if (saveCount > 0) {
					flag = true;
				}
				
				//增加操作日志
				Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
				if(loginSysuserObj!=null){
					LfSysuser loginSysuser =(LfSysuser)loginSysuserObj;
					String opContent1 = "新建业务模板配置"+(saveCount > 0?"成功":"失败")+"。[业务ID，模板ID，关联类型，创建人ID]" +
							"("+busId+"，["+tmIds+"]，1，"+sysuser.getUserId()+")";
					EmpExecutionContext.info("业务模板配置", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
							loginSysuser.getUserName(), opContent1, "ADD");
				}				
			} else {
				//修改模板配置
				//List<LfBusTailTmp> taiList = baseBiz.getByCondition(LfBusTailTmp.class, conditionMap, null);
				
				//查询操作之前记录
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				conditionMap.put("busId", busId);
				conditionMap.put("associateType", "1");
				List<LfBusTailTmp> bttempList = baseBiz.getByCondition(LfBusTailTmp.class, conditionMap, null);
				String oldTempIds= "";
				if(bttempList.size()>0){
					for(int i=0; i<bttempList.size();i++){
						oldTempIds += bttempList.get(i).getTmId()+",";
					}
					oldTempIds = oldTempIds.substring(0, oldTempIds.length()-1);
				}
				
				flag = ydywBiz.saveTails(busId, tmIds, saveList);
				
				//增加操作日志
				Object loginSysuserObj = request.getSession(false)!=null?request.getSession(false).getAttribute("loginSysuser"):null;
				if(loginSysuserObj!=null){
					LfSysuser loginSysuser =(LfSysuser)loginSysuserObj;
					String opContent1 = "修改业务模板配置"+(flag == true?"成功":"失败")+"。[业务ID，模板ID，关联类型，创建人ID]" +
							"("+busId+"，["+oldTempIds+"]，1，"+sysuser.getUserId()+")->("+busId+"，["+tmIds+"]，1，"+sysuser.getUserId()+")";
					EmpExecutionContext.info("业务模板配置", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
							loginSysuser.getUserName(), opContent1, "UPDATE");
				}			
				
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "新增、修改配置异常！");
		}
		
		response.getWriter().print(flag);
	}
	
	/**
	 * 单个及批量删除
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//获取当前登录用户
		//LfSysuser sysuser = (LfSysuser)request.getSession().getAttribute("loginSysuser");
		//String corpCode = sysuser.getCorpCode();
		int count = 0;
		String delType = request.getParameter("delType");
		String busId = request.getParameter("busId");		
		
		//where条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("busId", busId);
		conditionMap.put("associateType", "1");
		//conditionMap.put("corpCode", corpCode);
		
		//排序字段
		LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
		orderbyMap.put("id", StaticValue.ASC);
		
		if (delType.equals("1")) {
			//单条记录删除
			String ids = "";			
			
			List<LfBusTailTmp> btList = baseBiz.findListByCondition(LfBusTailTmp.class, conditionMap, orderbyMap);
			
			try {
				if (btList != null && btList.size() > 0) {
					LfBusTailTmp busTmp = null;
					
					for (int i = 0; i < btList.size(); i++) {
						busTmp = btList.get(i);
						ids += busTmp.getId();
						ids += ",";
					}
					
					if (ids.indexOf(",") != -1) {
						ids = ids.substring(0, ids.lastIndexOf(","));
					}
					//根据ids删除所有对象
					count = baseBiz.deleteByIds(LfBusTailTmp.class, ids);
					
					//增加操作日志
					Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
					if(loginSysuserObj!=null){
						LfSysuser loginSysuser =(LfSysuser)loginSysuserObj;
						String opContent1 = "删除业务模板配置"+(count>0?"成功":"失败")+"。[业务ID]" +
								"("+busId+")";
						EmpExecutionContext.info("业务模板配置", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
								loginSysuser.getUserName(), opContent1, "DELETE");
					}	
				}				
			} catch (Exception e) {
				count = -1;
			}
		} else {
			//批量删除
			try {
				//清除conditionMap
				conditionMap.clear();
				
				//
				conditionMap.put("busId&in", busId);
				conditionMap.put("associateType", "1");
				//conditionMap.put("corpCode", corpCode);
				String[] str = {};
				if (busId.indexOf(",") != -1) {
					busId = busId.substring(0, busId.lastIndexOf(","));
					str = busId.split(",");
				} else {
					str[0] = busId;
				}
				
				//实际删除模板配置数量
				count = baseBiz.deleteByCondition(LfBusTailTmp.class, conditionMap);
				
				//返回页面删除记录的条数
				if (count > 0) {
					count = str.length;
				}
				
				//增加操作日志
				Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
				if(loginSysuserObj!=null){
					LfSysuser loginSysuser =(LfSysuser)loginSysuserObj;
					String opContent1 = "删除业务模板配置"+(count>0?"成功":"失败")+"。[业务ID]" +
							"("+busId+")";
					EmpExecutionContext.info("业务模板配置", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
							loginSysuser.getUserName(), opContent1, "DELETE");
				}					
				
			} catch (Exception e) {
				count = -1;
			}			
		}
		
		response.getWriter().print(count);
	}
	
	// 输出机构代码数据
	public void createDeptTree(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			Long depId = null;
			Long userid = null;
			// 部门iD
			String depStr = request.getParameter("depId");
			// 操作员账号
			//String userStr = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String userStr = SysuserUtil.strLguserid(request);

			if(depStr != null && !"".equals(depStr.trim()))
			{
				depId = Long.parseLong(depStr);
			}
			if(userStr != null && !"".equals(userStr.trim()))
			{
				userid = Long.parseLong(userStr);
			}
			//从session中获取当前操作员对象
			LfSysuser lfSysuser = getLoginUser(request);
			String corpCode = lfSysuser.getCorpCode();
			String departmentTree = this.getDepartmentJosnData(depId, userid,corpCode);
			response.getWriter().print(departmentTree);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "后台异常");
		}
	}

	// 异步加载机构的主方法
	private String getDepartmentJosnData(Long depId, Long userid) {

		StringBuffer tree = null;
		try {
			// 当前登录操作员
			BaseBiz baseBiz = new BaseBiz();
			LfSysuser curUser = baseBiz.getLfSysuserByUserId(userid);
			// 判断是否个人权限
			if(curUser.getPermissionType() == 1) {
				tree = new StringBuffer("[]");
			} else {
				// 机构biz
				DepBiz depBiz = new DepBiz();
				List<LfDep> lfDeps;

				lfDeps = null;

				if(curUser.getCorpCode().equals("100000")) {
					if(depId == null) {
						LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
						LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
						// 只查询顶级机构
						conditionMap.put("superiorId", "0");
						// 查询未删除的机构
						conditionMap.put("depState", "1");
						// 排序
						orderbyMap.put("depId", StaticValue.ASC);
						orderbyMap.put("deppath", StaticValue.ASC);
						lfDeps = baseBiz.getByCondition(LfDep.class, conditionMap, orderbyMap);
					} else {
						lfDeps = new DepBiz().getDepsByDepSuperId(depId);
					}

				} else {
					if(depId == null) {
						lfDeps = new ArrayList<LfDep>();
						LfDep lfDep = depBiz.getAllDeps(userid).get(0);
						lfDeps.add(lfDep);
					} else {
						lfDeps = new DepBiz().getDepsByDepSuperId(depId);
					}
				}

				LfDep lfDep = null;
				tree = new StringBuffer("[");
				for (int i = 0; i < lfDeps.size(); i++) {
					lfDep = lfDeps.get(i);
					tree.append("{");
					tree.append("id:").append(lfDep.getDepId());
					tree.append(",name:'").append(lfDep.getDepName()).append("'");
					tree.append(",pId:").append(lfDep.getSuperiorId());
					tree.append(",depId:'").append(lfDep.getDepId() + "'");
					tree.append(",isParent:").append(true);
					tree.append("}");
					if(i != lfDeps.size() - 1) {
						tree.append(",");
					}
				}
				tree.append("]");
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "后台异常");
			tree = new StringBuffer("[]");
		}
		
		return tree.toString();
	}
	// 异步加载机构的主方法
	private String getDepartmentJosnData(Long depId, Long userid,String corpCode) {

		StringBuffer tree = null;
		try {
			// 当前登录操作员
			BaseBiz baseBiz = new BaseBiz();
			LfSysuser curUser = baseBiz.getLfSysuserByUserId(userid);
			// 判断是否个人权限
			if(curUser.getPermissionType() == 1) {
				tree = new StringBuffer("[]");
			} else {
				// 机构biz
				DepBiz depBiz = new DepBiz();
				List<LfDep> lfDeps;

				lfDeps = null;

				if(curUser.getCorpCode().equals("100000")) {
					if(depId == null) {
						LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
						LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
						// 只查询顶级机构
						conditionMap.put("superiorId", "0");
						// 查询未删除的机构
						conditionMap.put("depState", "1");
						// 排序
						orderbyMap.put("depId", StaticValue.ASC);
						orderbyMap.put("deppath", StaticValue.ASC);
						lfDeps = baseBiz.getByCondition(LfDep.class, conditionMap, orderbyMap);
					} else {
						lfDeps = new DepBiz().getDepsByDepSuperId(depId);
					}

				} else {
					if(depId == null) {
						lfDeps = new ArrayList<LfDep>();
						//LfDep lfDep = depBiz.getAllDeps(userid).get(0);
						LfDep lfDep = depBiz.getAllDepByUserIdAndCorpCode(userid, corpCode).get(0);
						lfDeps.add(lfDep);
					} else {
						//lfDeps = new DepBiz().getDepsByDepSuperId(depId);
						lfDeps = new DepBiz().getDepsByDepSuperIdAndCorpCode(depId,corpCode);
					}
				}

				LfDep lfDep = null;
				tree = new StringBuffer("[");
				for (int i = 0; i < lfDeps.size(); i++) {
					lfDep = lfDeps.get(i);
					tree.append("{");
					tree.append("id:").append(lfDep.getDepId());
					tree.append(",name:'").append(lfDep.getDepName()).append("'");
					tree.append(",pId:").append(lfDep.getSuperiorId());
					tree.append(",depId:'").append(lfDep.getDepId() + "'");
					tree.append(",isParent:").append(true);
					tree.append("}");
					if(i != lfDeps.size() - 1) {
						tree.append(",");
					}
				}
				tree.append("]");
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "后台异常");
			tree = new StringBuffer("[]");
		}
		
		return tree.toString();
	}
	
	/**
	 * 获取短信模板
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void getTempByName(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//业务ID
		String busId = request.getParameter("busId");
		//模板名称
		//String tName = new String(request.getParameter("tName").getBytes("ISO-8859-1"),"utf-8"); ;
		String tName = URLDecoder.decode(request.getParameter("tName"), "UTF-8");
		tName = URLDecoder.decode(tName, "utf-8").trim();
		//操作类型：1、新增      2、修改
		String type = request.getParameter("type");
		//已选的模板id
		String tmId = request.getParameter("tmIds");
		if (tmId != null && tmId.indexOf(",") != -1) {
			tmId = tmId.substring(0, tmId.lastIndexOf(","));
		}
		
		List<LfTemplate> tempList = new ArrayList<LfTemplate>();
		List<LfBusManager> busList = new ArrayList<LfBusManager>();
		List<LfTemplate> tmpList = new ArrayList<LfTemplate>();
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
		
		try {			
			//获取当前登录用户
			LfSysuser sysuser = (LfSysuser)request.getSession(false).getAttribute("loginSysuser");
			if(sysuser == null)return;
			String corpCode = sysuser.getCorpCode();
			
			if (null != tName && !tName.equals("")) {
				conditionMap.put("tmName&like", tName);
			}
			
			conditionMap.put("tmpType", "3");
			conditionMap.put("corpCode", corpCode);	
			conditionMap.put("tmState", "1");
			conditionMap.put("dsflag&in", "0,1");
			
			orderbyMap.put("tmid", StaticValue.DESC);
			
			try {
				//根据模板名称查询短信模板
				if (sysuser.getPermissionType() == 1) {
					conditionMap.put("userId", sysuser.getUserId().toString());
				} else {
					//获取当前操作员所属机构的用户
					LinkedHashMap<String, String> conditionMap1 = new LinkedHashMap<String, String>();
					LinkedHashMap<String, String> orderbyMap1 = new LinkedHashMap<String, String>();
					//机构ID
					String depId = sysuser.getDepId().toString();
					String dids = "";
					
					if (!depId.toString().equals("1")) {						
						//查询本机构及其下级机构
						DepBiz depBiz = new DepBiz();
						
						//获取下级机构
						List<Long> depIds = depBiz.getChildDepIds(Long.valueOf(depId), corpCode);
						
						for (int i = 0; i < depIds.size(); i++) {
							dids += depIds.get(i).toString() + ",";
						}
						
						if (dids.indexOf(",") != -1) {
							dids = dids.substring(0, dids.lastIndexOf(","));
						}
						
						conditionMap1.put("depId&in", dids);
					}
								
					orderbyMap1.put("userId", StaticValue.DESC);
					//获取操作员ID
					List<LfSysuser> userList = baseBiz.getByCondition(LfSysuser.class, conditionMap1, orderbyMap1);
					String ids = "";
					if (userList != null && userList.size() > 0) {
						for (LfSysuser ls : userList) {
							ids += ls.getUserId() + ",";
						}
					}
					
					ids += sysuser.getUserId();
					if (ids.indexOf(",") != -1) {
						ids = ids.substring(0, ids.lastIndexOf(","));
					}
					
					conditionMap.put("userId&in", ids);
				}	
				
				tempList = baseBiz.getByCondition(LfTemplate.class, conditionMap, orderbyMap);
			} catch (Exception e) {
				EmpExecutionContext.error(e, "查询短信模板异常！");
			}
			
			//当type等于2时，获取已配置的短信模板			
			if (type.equals("2")) {
				//获取所有启用的业务类型
				conditionMap.clear();
				conditionMap.put("state", "0");
				conditionMap.put("corpCode", corpCode);
				
				orderbyMap.clear();
				orderbyMap.put("busId", StaticValue.DESC);
				//获取业务类型
				busList = baseBiz.getByCondition(LfBusManager.class, conditionMap, orderbyMap);
				
				conditionMap.clear();
				conditionMap.put("busId", busId);
				conditionMap.put("associateType", "1");
				conditionMap.put("corpCode", corpCode);
				
				orderbyMap.clear();
				orderbyMap.put("id", StaticValue.DESC);
				
				//获取busId对应的已配置的模板id
				List<LfBusTailTmp> taiList = baseBiz.getByCondition(LfBusTailTmp.class, conditionMap, orderbyMap);
				
				//获取模板ID
				String tmIds = "";
				if (null != taiList && taiList.size() > 0) {
					for (LfBusTailTmp lfBusTailTmp : taiList) {
						tmIds += lfBusTailTmp.getTmId();
						tmIds += ",";
					}
					
					if(tmId != null && !tmId.equals("")) {
						tmIds = tmId;
					}
					
					/*if (tmIds.indexOf(",") != -1) {
						tmIds = tmIds.substring(0, tmIds.lastIndexOf(","));
					}*/
					
					conditionMap.clear();
					conditionMap.put("tmid&in", tmIds);
					conditionMap.put("corpCode", corpCode);
					
					orderbyMap.clear();
					orderbyMap.put("tmid", StaticValue.DESC);
					
					tmpList = baseBiz.getByCondition(LfTemplate.class, conditionMap, orderbyMap);					
				}
			} else {
				//获取所有启用的业务类型
				conditionMap.clear();
				String value = "select distinct BUS_ID from LF_BUS_TAILTMP where ASSOCIATE_TYPE=1";
				conditionMap.put("busId&not in", value);
				conditionMap.put("state", "0");
				conditionMap.put("busType&in", "1,2");
				conditionMap.put("corpCode", corpCode);
				
				orderbyMap.clear();
				orderbyMap.put("busId", StaticValue.DESC);
				//获取业务类型
				busList = baseBiz.getByCondition(LfBusManager.class, conditionMap, orderbyMap);
				
				//新增配置时，查询模板
				if (tmId != null && !tmId.equals("")) {
					conditionMap.clear();
					conditionMap.put("tmid&in", tmId);
					conditionMap.put("tmState", "1");
					conditionMap.put("dsflag&in", "0,1");
					
					orderbyMap.clear();
					orderbyMap.put("tmid", StaticValue.DESC);
					
					tmpList = baseBiz.getByCondition(LfTemplate.class, conditionMap, orderbyMap);
				}				
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "查询模板异常！");
		} finally {
			String yesOrNo = "yes";
			if (tempList != null && tempList.size() > 0) {
				yesOrNo = "no";
			} 
			
			request.setAttribute("yesOrNo", yesOrNo);
			request.setAttribute("busId", busId);
			request.setAttribute("tName", tName);
			request.setAttribute("type", type);
			request.setAttribute("tempList", tempList);
			request.setAttribute("busList", busList);
			request.setAttribute("tmpList", tmpList);
			request.getRequestDispatcher(this.empRoot  + base  + "/ydyw_cfg.jsp").forward(
					request, response);
		}
	}
	
	/**
	 * 新增业务类型
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void addBus(HttpServletRequest request, HttpServletResponse response) throws IOException {
		request.setAttribute("yesOrNo", "no");
		//业务名称
		String busName = request.getParameter("busName").toUpperCase().trim();
		//业务编码
		String busCode = request.getParameter("busCode").toUpperCase().trim();
		//当前登录企业
		String lgcorpcode = request.getParameter("lgcorpcode");
		//当前登录操作员
		//String lguserid = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String lguserid = SysuserUtil.strLguserid(request);



		//业务类型
		String busType = request.getParameter("busType");
		//业务优先级
		String riseLevel = request.getParameter("riseLevel");
		
		String corpCode = lgcorpcode;
		
		try {
			LfSysuser lfsysuser = baseBiz.getById(LfSysuser.class, lguserid);
			Long depId = lfsysuser.getDepId();
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("busName",busName);
			conditionMap.put("corpCode&in","0,1,2,"+corpCode);
			List<LfBusManager> busList = baseBiz.getByCondition(
					LfBusManager.class, conditionMap, null);
			
			//检查名称重复
			if (busList != null && busList.size() > 0) {
				response.getWriter().print("nameExists");
				return;
			}
			
			conditionMap.clear();
			conditionMap.put("busCode",busCode);
			conditionMap.put("corpCode&in","0,1,2,"+corpCode);
			busList = baseBiz.getByCondition(LfBusManager.class, conditionMap,
					null);
			//检查业务编码重复
			if (busList != null && busList.size() > 0) {
				response.getWriter().print("codeExists");
				return;
			}
			
			LfBusManager busManager = new LfBusManager();
			//都不重复可以保存入库
			busManager.setBusCode(busCode);
			busManager.setBusName(busName);
			busManager.setBusType(Integer.parseInt(busType));
			busManager.setRiseLevel(Integer.parseInt(riseLevel));
			busManager.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			busManager.setCreateTime(new Timestamp(System.currentTimeMillis()));
			busManager.setUserId(Long.parseLong(lguserid));
			busManager.setDepId(depId);
			busManager.setCorpCode(corpCode);
			busManager.setState(0);
			busManager.setBusDescription(request.getParameter("busDescription"));

			boolean result = baseBiz.addObj(busManager);
			response.getWriter().print(result);

			//增加操作日志
			Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser =(LfSysuser)loginSysuserObj;
				String opContent1 = "新建业务"+(result==true?"成功":"失败")+"。[业务名称，业务编码，业务类型，优先级别]" +
						"("+busName+"，"+busCode+"，"+busType+"，"+riseLevel+")";
				EmpExecutionContext.info("业务模板配置", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
						loginSysuser.getUserName(), opContent1, "ADD");
			}			
			
		} catch (Exception e) {
			response.getWriter().print(ERROR);
			EmpExecutionContext.error(e,"新增业务类型异常！");
		}
	}
	
	/**
	 * 获取操作员名称
	 * @description    
	 * @param request
	 * @return       			 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2014-4-2 上午09:17:49
	 */
	public String getOpUser(HttpServletRequest request){
		String opUser = "";
		try {
			LfSysuser sysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			if(sysuser != null){
				opUser = sysuser.getUserName();
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "session获取操作员名称异常！");
		}
		
		return opUser;
	}
	
}
