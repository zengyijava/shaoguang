package com.montnets.emp.degree.servlet;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.degree.biz.DegreeBiz;
import com.montnets.emp.degree.entity.LfDegree;
import com.montnets.emp.degree.vo.LfDegreeManageVo;
import com.montnets.emp.util.PageInfo;

public class rms_degreeManagerSvt extends BaseServlet {
	private static final long serialVersionUID = -831755093183717690L;

	/**
	 * 计费档位管理查询
	 * 
	 * @param request
	 * @param response
	 * 
	 */
	
	public void find(HttpServletRequest request, HttpServletResponse response) {		
		DegreeBiz degreeBiz = new DegreeBiz();
		//页面信息
		PageInfo pageInfo = new PageInfo();
		
		List<LfDegreeManageVo> chaVoList = new ArrayList<LfDegreeManageVo>();
		LfDegreeManageVo degreeVo = new LfDegreeManageVo();
		//是否第一次进入
		boolean isFirstEnter = false;
		//操作员名称
		String userName = null;
		
		// 日志开始时间
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		long begin_time = System.currentTimeMillis();
		try {
			// 是否第一次打开
			isFirstEnter = pageSet(pageInfo, request);
			DegreeBiz qbiz=new DegreeBiz();
			//获取企业编码
			String corpCode = qbiz.getCorpCode(request);
			degreeVo.setCorpCode(corpCode);

			List<LfDegreeManageVo> chaVoList1 = new ArrayList<LfDegreeManageVo>();
			LfDegreeManageVo degreeVo1 = new LfDegreeManageVo();
			chaVoList1 = degreeBiz.getDegreeBiz(degreeVo1,"");
			
			//获取档位层数
			List<Integer> list = new ArrayList<Integer>();
			if(chaVoList1!=null){
				for(LfDegreeManageVo lfdegree : chaVoList1 ){
					if(lfdegree.getDegree() != null){
						   list.add(lfdegree.getDegree());  
					}
				}
			}
			Collections.sort(list);
			Integer[] str = list.toArray(new Integer[list.size()]); 
			list.clear();
			for (int i=0; i<str.length; i++) {    
				if(!list.contains(str[i])) {    
					list.add(str[i]);    
			    }    
			}
			request.setAttribute("chaDegree", list);
			
			// 分页设置
			if (!isFirstEnter) {
				//计费档位
				String degreeStr = request.getParameter("degree");
				if (degreeStr != null && !"".equals(degreeStr)) {
					int degree = Integer.parseInt(degreeStr);
					degreeVo.setDegree(degree);
				}
				//操作员名称
				userName = request.getParameter("userName").trim();
				if(userName != null && !"".equals(userName)){
					degreeVo.setUserName(userName);
				}
				//状态
				String statusStr = request.getParameter("status");
				if(statusStr!= null && !"".equals(statusStr)){
					int status = Integer.parseInt(statusStr);
					degreeVo.setStatus(status);
				}
				chaVoList = degreeBiz.getDegreeBiz(degreeVo, pageInfo,"");
			}

			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("chaVoList", chaVoList);
			request.setAttribute("LfDegreeManageVo",degreeVo);
			request.setAttribute("isFirstEnter", isFirstEnter);

			// 增加查询日志
			long end_time = System.currentTimeMillis();
			if (pageInfo != null) {
				String opContent = "查询开始时间：" + format.format(begin_time) + ",耗时:"
						+ (end_time - begin_time) + "ms，数量："
						+ pageInfo.getTotalRec();
				opSucLog(request, "计费档位", opContent, "GET");
			}
			request.getRequestDispatcher("txgl/degree/rms_degreeManager.jsp")
					.forward(request, response);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "计费档位查询失败！");
		}
	}
	
	
	
	/**
	 * 增加计费档位
	 * 
	 * @param request
	 * @param response
	 * 
	 */
	public void addDegree(HttpServletRequest request, HttpServletResponse response) {
		DegreeBiz degreeBiz = new DegreeBiz();
		LfDegreeManageVo degreeVo = new LfDegreeManageVo();
		//设置查询条件
		LfDegree adddgree = new LfDegree();
		adddgree.setStatus(0);
		degreeVo.setStatus(0);
		//操作员id
		LfSysuser lfSysuser = (LfSysuser)request.getSession(false).getAttribute("loginSysuser");
		Long userId = lfSysuser.getUserId();
		if(userId != null){
			adddgree.setUserId(userId);
			degreeVo.setUserId(userId);
		}else{
			adddgree.setUserId(2L);
			degreeVo.setUserId(2L);
		}
		
		//计费档位
		String degreeStr = request.getParameter("addDegree");
		if ( degreeStr != null && !"".equals(degreeStr)) {
			int degree = Integer.parseInt(degreeStr);
			degreeVo.setDegree(degree);
			adddgree.setDegree(degree);
		}
		
		//容量开始
		String degreeBegin = request.getParameter("addDegreeBegin");
		if (degreeBegin != null && !"".equals(degreeBegin)) {
			try {
				//去掉容量开始前面多余的0
				degreeBegin = Integer.parseInt(degreeBegin) + "";
			} catch (Exception e) {			
				EmpExecutionContext.error(e, "档位开始容量解析出错！");
			}
			degreeVo.setDegreeBegin(degreeBegin);
			adddgree.setDegreeBegin(degreeBegin);
		}
		
		//容量结束
		String degreeEnd = request.getParameter("addDegreeEnd");
		if (degreeEnd != null && !"".equals(degreeEnd)) {
			
			
			try {
				//去掉容量前面多余的0
				degreeEnd = Integer.parseInt(degreeEnd) + "";
			} catch (Exception e) {			
				EmpExecutionContext.error(e, "档位结束容量解析出错！");
			}
			
			degreeVo.setDegreeEnd(degreeEnd);
			adddgree.setDegreeEnd(degreeEnd);
		}
		
		//创建时间
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String createTime = format.format(date);
		degreeVo.setCreateTime(Timestamp.valueOf(createTime));
		adddgree.setCreateTime(Timestamp.valueOf(createTime));
		
		//有效开始时间
		String validDateBegin = request.getParameter("addValidDateBegin");
		if(validDateBegin != null && !"".equals(validDateBegin)){
			degreeVo.setValidDateBegin(Timestamp.valueOf(validDateBegin));
			adddgree.setValidDateBegin(Timestamp.valueOf(validDateBegin));
		}
		
		//有效截止时间
		String validDateEnd = request.getParameter("addValidDateEnd");
		if(validDateEnd != null && !"".equals(validDateEnd)){
			degreeVo.setValidDateEnd(Timestamp.valueOf(validDateEnd));
			adddgree.setValidDateEnd(Timestamp.valueOf(validDateEnd));
		}
			
		try {
			DegreeBiz qbiz = new DegreeBiz();
			//获取企业编码
			String corpCode = qbiz.getCorpCode(request);
			LfDegreeManageVo lfdegree = new LfDegreeManageVo();
			if(corpCode != null && !"".equals(corpCode)){				
				lfdegree.setCorpCode(corpCode);
			}
			//容量档位
			if(degreeStr != null && !"".equals(degreeStr)){				
				lfdegree.setDegree(Integer.parseInt(degreeStr));
			}
			lfdegree.setStatus(0);
			
			//页面信息
			PageInfo pageInfo = new PageInfo();
			List<LfDegreeManageVo> chaList = degreeBiz.getDegreeBiz(lfdegree, pageInfo,"");
			
			// 检查计费档位是否存在
			if (chaList != null && chaList.size() > 0 ) {
				response.getWriter().print("DegreeExists");
				return;
			}
			
			//判断有效开始时间是否小于有效结束时间
			if (!degreeBiz.timeCheck(validDateBegin, validDateEnd)) {
				response.getWriter().print("TimeError");
				return;
			}
			
			//判断有效结束时间是否大于当前时间
			if (!degreeBiz.validDateEndTimeCheck(validDateEnd)) {
				response.getWriter().print("EndTimeError");
				return;
			}
			
			//检查容量范围是否合理
			if(!degreeBiz.degreeCheck(request, response, lfdegree, pageInfo, degreeBegin, degreeEnd)){
				return;
			}
			
			//提示容量结束大于容量开始
			if(Integer.parseInt(degreeEnd)<=Integer.parseInt(degreeBegin)){
				response.getWriter().print("widthError");
				return;
			}
			
			//都不重叠 满足条件
			//添加值
			
			Long result = degreeBiz.addObjReturnId(adddgree);
			response.getWriter().print(result>0);

			// 增加操作日志
			Object loginSysuserObj = request.getSession(false).getAttribute(
					"loginSysuser");
			if (loginSysuserObj != null) {
				LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;
				String opContent1 = "新建计费档位" + (result >0 ? "成功" : "失败")
						;
				EmpExecutionContext.info("新建计费档位", "100000", "2",
						"mwadmin", opContent1, "ADD");
			}
		} catch (Exception e) {
			try {
				response.getWriter().print(ERROR);
			} catch (IOException e1) {			
				EmpExecutionContext.error(e1, "新建计费档位失败！");
			}
			EmpExecutionContext.error(e, "新建计费档位失败！");
		}

	}
	
	 
	/**
	 * 更新 计费档位
	 * 
	 * @param request
	 * @param response
	 * 
	 */
	public void updateDegree(HttpServletRequest request, HttpServletResponse response) {
		DegreeBiz degreeBiz = new DegreeBiz();
		//主键
		String idStr =request.getParameter("id");
		
		//档位
		String degreeStr = request.getParameter("degree");
		
		//档位容量开始
		String degreeBeginUpdate = request.getParameter("degreeBegin");
		if (degreeBeginUpdate != null && !"".equals(degreeBeginUpdate)) {
			try {
				//去掉容量开始前面多余的0
				degreeBeginUpdate = Integer.parseInt(degreeBeginUpdate) + "";
			} catch (Exception e) {			
				EmpExecutionContext.error(e, "档位开始容量解析出错！");
			}
		}
		
		//档位容量结束
		String degreeEndUpdate = request.getParameter("degreeEnd");
		if (degreeEndUpdate != null && !"".equals(degreeEndUpdate)) {
			try {
				//去掉容量结束前面多余的0
				degreeEndUpdate = Integer.parseInt(degreeEndUpdate) + "";
			} catch (Exception e) {			
				EmpExecutionContext.error(e, "档位结束容量解析出错！");
			}
		}
		
		
		//有效开始时间
		String validDateBeginUpdate = request.getParameter("validDateBegin");
		
		//有效截止时间
		String validDateEndUpdate = request.getParameter("validDateEnd");
		
		//状态
		String statusStr = request.getParameter("status");
		
		try {
			//提示容量结束大于容量开始
			if(Integer.parseInt(degreeBeginUpdate)>=Integer.parseInt(degreeEndUpdate)){
				response.getWriter().print("widthError");
				return;
			}
			
			//判断有效开始时间是否小于有效结束时间
			if (!degreeBiz.timeCheck(validDateBeginUpdate, validDateEndUpdate)) {
				response.getWriter().print("TimeError");
				return;
			}
			
			//判断有效结束时间是否大于当前时间
			if (!degreeBiz.validDateEndTimeCheck(validDateEndUpdate)) {
				response.getWriter().print("EndTimeError");
				return;
			}
			
			//查询更新之前的数据
			LfDegree beLfDegree = degreeBiz.getById(LfDegree.class, Long.parseLong(idStr));
			int statusBefore = beLfDegree.getStatus();
			//当状态为启用时
			if(Integer.parseInt(statusStr)==0){
				LfDegreeManageVo lfdegree = new LfDegreeManageVo();
				DegreeBiz qbiz = new DegreeBiz();
				//获取企业编码
				String corpCode = qbiz.getCorpCode(request);
				if(corpCode != null && !"".equals(corpCode)){					
					lfdegree.setCorpCode(corpCode);
				}
				
				//获取档位
				if(degreeStr != null && !"".equals(degreeStr)){					
					lfdegree.setDegree(Integer.parseInt(degreeStr));
				}
				lfdegree.setStatus(0);
				
				//获取页面信息
				PageInfo pageInfo = new PageInfo();
				List<LfDegreeManageVo> chaList = degreeBiz.getDegreeBiz(lfdegree, pageInfo,"");
				
				// 检查计费档位是否存在
				if(statusBefore != 0){
					if (chaList != null && chaList.size() > 0 ) {
						response.getWriter().print("DegreeExists");
						return;
					}
				}
				
				//容量范围检测
				if(!degreeBiz.degreeCheck(request, response, lfdegree, pageInfo, degreeBeginUpdate, degreeEndUpdate)){
					return;
				}
			}
			//获取操作之前的信息
			String beLfDegreeString = beLfDegree.getDegreeBegin()+","+beLfDegree.getDegreeEnd()+","
										+beLfDegree.getValidDateBegin()+","+beLfDegree.getValidDateEnd()+","
										+beLfDegree.getStatus();
			
			beLfDegree.setDegreeBegin(degreeBeginUpdate);
			beLfDegree.setDegreeEnd(degreeEndUpdate);
			beLfDegree.setValidDateBegin(Timestamp.valueOf(validDateBeginUpdate));
			beLfDegree.setValidDateEnd(Timestamp.valueOf(validDateEndUpdate));
			beLfDegree.setStatus(Integer.parseInt(statusStr));
			
			// 异步返回更新结果
			boolean result = degreeBiz.updateObj(beLfDegree);
			response.getWriter().print(result);

			// 增加操作日志
			Object loginSysuserObj = request.getSession(false).getAttribute(
					"loginSysuser");
			if (loginSysuserObj != null) {
//				LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;
				String opContent1 = "更新计费档位"
//						+ ("true".equals(result) ? "成功" : "失败")
						+ (result ? "成功" : "失败")
						+ "。[档位容量开始，档位容量结束，有效开始时间，有效截止时间，状态]" + "(" + beLfDegreeString
						+ ")";
				EmpExecutionContext.info("计费档位", "100000", "2",
						"mwadmin", opContent1, "UPDATE");
			}
		} catch (Exception e) {
			try {
				response.getWriter().print(ERROR);
			} catch (IOException e1) {				
				EmpExecutionContext.error(e1, "计费档位修改失败！");
			}
			EmpExecutionContext.error(e, "计费档位修改失败！");
		}
		
	}

	/**
	 * @description 记录操作成功日志
	 * @param request
	 * @param modName
	 *            模块名称
	 * @param opContent
	 *            操作详情
	 * @param opType
	 *            操作类型 ADD UPDATE DELETE GET OTHER
	 * @author zengy2
	 * @datetime 
	 */
	public void opSucLog(HttpServletRequest request, String modName,
			String opContent, String opType) {
		LfSysuser lfSysuser = null;
		try {
			Object obj = request.getSession(false).getAttribute("loginSysuser");
			if (obj == null)
				return;
			lfSysuser = (LfSysuser) obj;
		} catch (Exception e) {
			EmpExecutionContext.error(e, "记录操作日志异常，session为空！");
		}
		EmpExecutionContext.info(modName, "100000", String
				.valueOf(2), "mwadmin",
				opContent, opType);
	}
}
