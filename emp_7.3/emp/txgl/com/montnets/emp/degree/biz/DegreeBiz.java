package com.montnets.emp.degree.biz;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.degree.dao.DegreeDAO;
import com.montnets.emp.degree.vo.LfDegreeManageVo;
import com.montnets.emp.util.PageInfo;

public class DegreeBiz extends BaseBiz {
	/**
	 * 获取数据库中档位管理的信息 分页
	 * 
	 * @param lfDegreeManageVo
	 * @param pageInfo
	 * @param orderBy
	 * @throws Exception
	 */
	public List<LfDegreeManageVo> getDegreeBiz(LfDegreeManageVo lfDegreeManageVo,
			PageInfo pageInfo ,String orderBy) throws Exception {
		List<LfDegreeManageVo> degreeList = null;
		try {
			//获取数据库中档位管理的信息
			if (pageInfo != null) {
				degreeList = DegreeDAO.findLfDegreeManageVo(lfDegreeManageVo, pageInfo, orderBy);
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "业务管理查询异常。");
		}
		return degreeList;
	}
	
	/**
	 * 获取数据库中档位管理的信息 无分页
	 * 
	 * @param lfDegreeManageVo
	 * @param orderBy
	 * @throws Exception
	 */
	public List<LfDegreeManageVo> getDegreeBiz(LfDegreeManageVo lfDegreeManageVo,
			String orderBy) throws Exception {
		List<LfDegreeManageVo> degreeList = null;
		try {
			//获取数据库中档位管理的信息
			degreeList = new DegreeDAO().findLfDegreeManageVo(lfDegreeManageVo, orderBy);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "业务管理查询异常。");
		}
		return degreeList;
	}
	
	/**
	 * 容量范围检测
	 * 
	 * @param request
	 * @param response
	 * @param lfdegree
	 * @param pageInfo
	 * @param degreeBeginCheck
	 * @param degreeEndCheck
	 * @throws Exception
	 */
	public boolean degreeCheck(HttpServletRequest request, HttpServletResponse response, LfDegreeManageVo lfdegree,
			PageInfo pageInfo, String degreeBeginCheck, String degreeEndCheck) throws Exception{
		DegreeBiz degreeBiz = new DegreeBiz();
		// 检查容量范围与上档计费档位是否重叠
		List<LfDegreeManageVo> chaList = degreeBiz.getDegreeBiz(lfdegree, pageInfo,"desc");
		List<Integer> lowlist = new ArrayList<Integer>();
		List<String> lowdegree = new ArrayList<String>();
		
		if (chaList != null && chaList.size() > 0 ) {
			for(LfDegreeManageVo l: chaList){
				//上一档位最大值
				lowlist.add(Integer.parseInt(l.getDegreeEnd()));
				lowdegree.add(l.getDegree().toString());
			}	
		
		
			//提示与上一档重叠
			if (degreeBeginCheck != null && !"".equals(degreeBeginCheck)) {
				if(Integer.parseInt(degreeBeginCheck)<lowlist.get(0)){
					response.getWriter().print("lowdegree:"+lowdegree.get(0).toString());
					return false;
				}
			}
		}
		
		// 检查容量范围与下档计费档位是否重叠
		chaList = degreeBiz.getDegreeBiz(lfdegree, pageInfo,"asc");
		List<Integer> highlist = new ArrayList<Integer>();
		List<String> highdegree = new ArrayList<String>();
		
		if (chaList != null && chaList.size() > 0) {
			for (LfDegreeManageVo l : chaList) {
				// 下一档位起始值
				highlist.add(Integer.parseInt(l.getDegreeBegin()));
				highdegree.add(l.getDegree().toString());
			}
		
		
			//提示与下一档重叠
			if (degreeEndCheck != null && !"".equals(degreeEndCheck)) {
				if(Integer.parseInt(degreeEndCheck)>highlist.get(0)){
					response.getWriter().print("highdegree:"+highdegree.get(0).toString());
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * 有效开始时间是否小于有效结束时间
	 * 
	 * @param validDateBeginStr
	 * @param validDateEndStr
	 * @throws ParseException 
	 */
	public boolean timeCheck(String validDateBeginStr, String validDateEndStr) throws ParseException {
		//日期格式
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//将validDateBeginStr解析成long型数据
		long validDateBegin = sdf.parse(validDateBeginStr).getTime();
		//将validDateEndStr解析成long型数据
		long validDateEnd = sdf.parse(validDateEndStr).getTime();
		//比较有效开始时间与有效结束时间
		if(validDateBegin < validDateEnd) {
			return true;
		}
		return false;
	}
	
	/**
	 * 有效结束时间是否大于当前时间
	 * 
	 * @param validDateEndStr
	 * @throws ParseException 
	 */
	public boolean validDateEndTimeCheck(String validDateEndStr) throws ParseException {
		//获得当前时间的毫秒数
		long time = System.currentTimeMillis();
		//日期格式
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//将validDateEndStr解析成long型数据
		long validDateEnd = sdf.parse(validDateEndStr).getTime();
		//比较有效开始时间与有效结束时间
		if(time < validDateEnd) {
			return true;
		}
		return false;
	}

	/**
	 * 从session中获取当前操作员对象
	 * @param request
	 */
	public LfSysuser getCurrenUser(HttpServletRequest request) {
		try {
			Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj == null) {
				return null;
			}
			return (LfSysuser)loginSysuserObj;
		}catch(Exception e) {
			EmpExecutionContext.error("从SESSION获取操作员对象失败。");
			return null;
		}
	}
	
	/**
	 * 获取企业编码
	 * @param request
	 * @return
	 */
	public String getCorpCode(HttpServletRequest request){
		try
		{
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				if(loginSysuser!=null&&!"".equals(loginSysuser.getCorpCode())){
					return loginSysuser.getCorpCode();
				}else{
					return "-9999";
				}
			}
		}catch(Exception e)
		{
			EmpExecutionContext.error("SESSION获取企业编码失败");
			return "-9999";
		}
		return "-9999";
	}
}
