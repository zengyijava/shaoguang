package com.montnets.emp.rms.report.servlet;

import com.alibaba.fastjson.JSONObject;
import com.montnets.EMPException;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.corp.LfCorp;
import com.montnets.emp.entity.pasroute.LfSpDepBind;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.rms.degree.biz.DegreeBiz;
import com.montnets.emp.rms.degree.vo.LfDegreeManageVo;
import com.montnets.emp.rms.report.bean.RptStaticValue;
import com.montnets.emp.rms.report.biz.OperatorDegreeRptBiz;
import com.montnets.emp.rms.report.vo.MtDataReportVo;
import com.montnets.emp.util.CheckUtil;
import com.montnets.emp.util.DownloadFile;
import com.montnets.emp.util.PageInfo;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

public class rep_degreeReportSvt extends BaseServlet {

	private static final long serialVersionUID = 251822385448096190L;
	private static final String empCorpCode="100000";
	/**
	 * 档位查询
	 */
	private final DegreeBiz degreeBiz = new DegreeBiz();
	/**
	 * 模块名称
	 */
	private final String empRoot = "rms";
	/**
	 * 日报表
	 */
	private final int DAY_REPORT = 0;
	/**
	 * 月报表
	 */
	private final int MONTH_REPORT = 1;
	/**
	 * 年报表
	 */
	private final int YEAR_REPORT = 2;

	/**
	 * 功能文件夹名
	 */
	private final String base = "/report";

	/**
	 * 模块名称
	 */
	private final String modName = "rms";
	private static final String MODNAMES = "企业富信";
	private static final String OPNAME = "数据查询-档位报表统计";
	private final BaseBiz baseBiz = new BaseBiz();
	private final OperatorDegreeRptBiz degreeRptBiz = new OperatorDegreeRptBiz();

	protected int getIntParameter(String param, int defaultValue, HttpServletRequest request) {
			try
			{
				if(request.getParameter(param)!=null && !"".equals(request.getParameter(param)))
				{
					return Integer.parseInt(request.getParameter(param));
				}
				else
				{
					return defaultValue;
				}
			}
			catch (NumberFormatException e)
			{
				EmpExecutionContext.error(e,"获取分页信息异常");
				return defaultValue;
			}
		}

	/**
	 * 档位报表查询方法
	 *
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void find(HttpServletRequest request, HttpServletResponse response){
		List<LfSpDepBind> spUserList = new ArrayList<LfSpDepBind>();
		List<LfDegreeManageVo> lfDegreeManageVos = new ArrayList<LfDegreeManageVo>();
		List<Integer> degreeList = new ArrayList<Integer>();
		String corpCode = "";
		PageInfo pageInfo = new PageInfo();
		try {
			LfSysuser loginSysUser = degreeBiz.getCurrenUser(request);

			corpCode = loginSysUser.getCorpCode();
			// 查询档位
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();

			//如果是梦网侧查询所有企业发送账号
			String empCorpCode = "100000";
			if(StaticValue.getCORPTYPE() == 1 && !empCorpCode.equals(corpCode)){
				conditionMap.put("corpCode",corpCode);
			}

			spUserList = baseBiz.getByCondition(LfSpDepBind.class, conditionMap, null);

			//获取所有档位层数
			lfDegreeManageVos = degreeBiz.getDegreeBiz(new LfDegreeManageVo(),"");
			for(LfDegreeManageVo degree : lfDegreeManageVos ){
				if(degree.getDegree() != null){
					degreeList.add(degree.getDegree());
				}
			}
			Collections.sort(degreeList);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"企业富信-数据查询-档位报表统计-首次进入页面异常");
		}finally {
			request.setAttribute("spList", spUserList);
			request.setAttribute("degreeList", degreeList);
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("corpCode", corpCode);
			request.setAttribute("isRptFlag", isRptFlag(request));
			try {
				request.getRequestDispatcher( modName + base + "/rep_degreeReport.jsp").forward(request, response);
			} catch (Exception e) {
				EmpExecutionContext.error(e, "企业富信-数据查询-档位报表统计-跳转页面异常！");
			}
		}
	}

	/**
	 * 根据条件查询报表
	 * @param request
	 * @param response
	 */
	public void findDegreeRpt(HttpServletRequest request, HttpServletResponse response){
		//记录耗时
		Long start = System.currentTimeMillis();
		// 档位
		String degree = request.getParameter("degree");
		//运营商
		String operator = request.getParameter("operator");
		//SP账号
		String spUser = request.getParameter("spUser");
		//报表类型
		String reportType = request.getParameter("reportType");
		//统计时间
		String startTime = request.getParameter("startTime");
		//结束时间
		String endTime = request.getParameter("endTime");
		//企业名称
		String cropName = request.getParameter("cropName");
		//企业编号
		String corpCodeInput = request.getParameter("corpCode");
		//分页对象
		PageInfo pageInfo = new PageInfo();
		//企业编码
		String corpCode = "";
		// sp账号查询结果集
		List<LfSpDepBind>  spUserList = new ArrayList<LfSpDepBind>();
		//档位
		List<Integer> degreeList = new ArrayList<Integer>();
		//操作员Id
		String lguserid = null;
		//操作员名字
		String lgusername = null;
		// 查询条件对象
		MtDataReportVo rptVo = new MtDataReportVo();
		//报表结果集
		List<MtDataReportVo> reportList = null;
		String reportTypeStr = "";
		String operatorStr = "";
		try {
			pageSet(pageInfo,request);
			//如果不指定查询类型则默认为日报表
			reportType = StringUtils.defaultIfEmpty(reportType,"0");

			Integer intVal = Integer.parseInt(reportType);
			reportTypeStr = getReportTypeStr(intVal);

			//记录运营商日志
            operatorStr = getSpisuncmName(Integer.parseInt(StringUtils.defaultIfEmpty(operator,"-1")));

			LfSysuser loginSysUser = degreeBiz.getCurrenUser(request);
			corpCode = loginSysUser.getCorpCode();
			lguserid = loginSysUser.getUserId().toString();
			lgusername = loginSysUser.getName();
			//判断SP账号是否是属于本企业的
			if(StringUtils.isNotEmpty(spUser)) {
				//多企业才处理
				if(StaticValue.getCORPTYPE() == 1 && !"100000".equals(corpCode)){
					boolean checkFlag = new CheckUtil().checkSysuserInCorp(loginSysUser, corpCode, spUser.trim(), null);
					if(!checkFlag){
						throw new EMPException(MODNAMES + "," + OPNAME + ",该SP账号" + spUser + "不属于本企业！");
					}
				}
			}
			// 查询档位
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();

			//如果是梦网侧查询所有企业发送账号
			String empCorpCode = "100000";
			if(!empCorpCode.equals(corpCode) && StaticValue.getCORPTYPE() == 1){
				conditionMap.put("corpCode",corpCode);
			}

			spUserList = baseBiz.getByCondition(LfSpDepBind.class, conditionMap, null);

			//获取所有档位层数
			List<LfDegreeManageVo> lfDegreeManageVos = degreeBiz.getDegreeBiz(new LfDegreeManageVo(),"");
			for(LfDegreeManageVo vo : lfDegreeManageVos ){
				if(vo.getDegree() != null){
					degreeList.add(vo.getDegree());
				}
			}
			Collections.sort(degreeList);

			//组装查询对象
			rptVo.setSpisuncm(StringUtils.isEmpty(operator) ? null :Integer.parseInt(operator));
			rptVo.setChgrade(StringUtils.isEmpty(degree) ? null :Integer.parseInt(degree));
			rptVo.setUserId(spUser);
			rptVo.setCorpCode(corpCodeInput);
			rptVo.setCorpName(cropName);
			rptVo.setReportType(intVal);
			rptVo.setStartTime(startTime);
			rptVo.setEndTime(endTime);
			rptVo.setLoginCorpCode(corpCode);
			//将vo对象存到cookie中，方便导出功能与详情页面点击返回读取数据
			String rptVoJson = URLEncoder.encode(JSONObject.toJSONString(rptVo),"utf-8");
			Cookie degreeRptVo = new Cookie("degreeRptVo",rptVoJson);
			response.addCookie(degreeRptVo);
			//将当前分页信息也存到cookie中，方便详情页面点击返回操作时取值
			Cookie pageInfoCookie = new Cookie("degree_pageInfo",pageInfo.getPageSize() + "&" + pageInfo.getPageIndex());
			response.addCookie(pageInfoCookie);
			//结果集
			reportList = degreeRptBiz.getDegreeRpt(rptVo, pageInfo);
			//处理结果集
			for(MtDataReportVo rpt : reportList){
				rpt.setDetailInfo(reportType + "," + rpt.getChgrade() + "," + rpt.getSpisuncm() + "," + corpCode + "," + rpt.getUserId() + "," + startTime + "," + endTime);
                rpt.setSpisuncmName(getSpisuncmName(rpt.getSpisuncm()));
				switch (intVal){
					case DAY_REPORT:
						String showStartTime = startTime.replaceFirst("-","年").replaceFirst("-","月").replaceFirst("-","日");
						String showEndTime = endTime.replaceFirst("-","年").replaceFirst("-","月").replaceFirst("-","日");
						rpt.setShowTime(showStartTime + "至" + showEndTime);
						break;
					case MONTH_REPORT:
						rpt.setShowTime(rpt.getY() + "年" + rpt.getImonth() + "月");
						break;
					case YEAR_REPORT:
						rpt.setShowTime(rpt.getY() + "年");
						break;
					default:
						throw new EMPException(MODNAMES + "," + OPNAME + "查询档位统计报表异常,查询类型参数错误！");
				}
			}
		}catch (Exception e){
			EmpExecutionContext.error(e,MODNAMES + "-" + OPNAME + "查询档位统计报表异常！");
		}finally {
			Integer countSize = reportList == null ? 0:reportList.size();
			String conditionStr = "查询类型=" + reportTypeStr + ",SP账号=" + rptVo.getUserId()
					+ ",运营商=" + operatorStr + ",档位=" + rptVo.getChgrade()
					+ "档,查询开始时间=" + rptVo.getStartTime() + ",查询结束时间=" + rptVo.getEndTime()
					+ ",企业名称=" + rptVo.getCorpName() + ",企业编码=" + rptVo.getCorpCode() +
					",查询总数=" + countSize;
			request.setAttribute("spList", spUserList);
			request.setAttribute("degreeList", degreeList);
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("rptVo", rptVo);
			request.setAttribute("reportList", reportList);
			request.setAttribute("countSize", countSize);
			request.setAttribute("corpCode", corpCode);
			request.setAttribute("isRptFlag", isRptFlag(request));
			//记录日志
			String queryTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(start);
			String opContent = MODNAMES + "-" + MODNAMES + " totalcount:" + countSize + "条 ,查询时间："+ queryTime +",耗时:"+(System.currentTimeMillis() - start)+"ms,条件:"+ conditionStr;
			EmpExecutionContext.info(MODNAMES, corpCode, lguserid, lgusername, opContent, StaticValue.GET);
			try {
				request.getRequestDispatcher(modName + base + "/rep_degreeReport.jsp").forward(request, response);
			} catch (Exception e) {
				EmpExecutionContext.error(e, MODNAMES + "," + MODNAMES + ",跳转页面异常！");
			}
		}
	}

	/**
	 * 获取运营商名字
	 * @param num
	 * @return
	 */
	private String getSpisuncmName(Integer num){
	    String spisuncmName;
	    switch (num){
            case 0:
                spisuncmName = "移动";break;
            case 1:
                spisuncmName = "联通";break;
            case 21:
                spisuncmName = "电信";break;
            case 5:
                spisuncmName = "国外";break;
			case -1:
				spisuncmName = "全部";break;
            default:
                spisuncmName = "未知";
        }
        return spisuncmName;
    }

	/**
	 * 获取报表名字
	 * @param num
	 * @return
	 */
	private String getReportTypeStr(Integer num){
		String reportTypeStr = "";
		if(num == DAY_REPORT){
			reportTypeStr = "日报表";
		}else if(num == MONTH_REPORT){
			reportTypeStr = "月报表";
		}else if(num == YEAR_REPORT){
			reportTypeStr = "年报表";
		}
		return reportTypeStr;
	}

	/**
	 * 点击详情
	 * @param request
	 * @param response
	 */
	public void showDetailsInfo(HttpServletRequest request, HttpServletResponse response) {
		//起始时间
		long start = System.currentTimeMillis();
		String detailInfo = "";
		String degree = "";
		String spisuncm = "";
		String loginCorpCode = "";
		String userId = "";
		String startTime = "";
		String endTime = "";
		Integer reportType = null;
		String reportTypeStr = "";
		String operatorStr = "";
		//报表结果集
		List<MtDataReportVo> reportList = null;
		// 查询条件对象
		MtDataReportVo rptVo = new MtDataReportVo();
		//分页对象
		PageInfo pageInfo = new PageInfo();
		try {

			pageSet(pageInfo,request);

			Cookie[] cookies = request.getCookies();
			for(Cookie cookie:cookies){
				if("degreeRpt_detailInfo".equals(cookie.getName())){
					detailInfo = URLDecoder.decode(cookie.getValue(),"utf-8");
				}
			}

			if(StringUtils.isEmpty(detailInfo)){
				throw new EMPException(MODNAMES + "," + OPNAME + "点击详情查询异常！detailInfo对象为空！");
			}
			String[] detailInfoArr = detailInfo.split(",");
			//当为年报表或月报表时endTime为空
			for(int i = 0,j = detailInfoArr.length - 1;i < j;i++){
				if(StringUtils.isEmpty(detailInfoArr[i])){
					throw new EMPException(MODNAMES + "," + OPNAME + "点击详情查询异常！对应获取参数为空！");
				}
			}
			reportType = Integer.parseInt(detailInfoArr[0]);
			degree = detailInfoArr[1];
			spisuncm = detailInfoArr[2];
			loginCorpCode = detailInfoArr[3];
			userId = detailInfoArr[4];
			startTime = detailInfoArr[5];
			if(reportType == 0){
				endTime = detailInfoArr[6];
			}

			operatorStr = getSpisuncmName(Integer.parseInt(spisuncm));

			reportTypeStr = getReportTypeStr(reportType);

			//组装查询对象
			rptVo.setSpisuncm(Integer.parseInt(spisuncm));
			rptVo.setChgrade(Integer.parseInt(degree));
			rptVo.setUserId(userId);
			rptVo.setReportType(reportType);
			rptVo.setStartTime(startTime);
			rptVo.setEndTime(endTime);
			rptVo.setLoginCorpCode(loginCorpCode);
			rptVo.setDes(true);
			//将vo对象存到cookie中，方便导出功能读取数据
			String rptVoJson = URLEncoder.encode(JSONObject.toJSONString(rptVo),"utf-8");
			Cookie detailInfoRptVo = new Cookie("detailInfoRptVo",rptVoJson);
			response.addCookie(detailInfoRptVo);
			//结果集
			reportList = degreeRptBiz.getDegreeRpt(rptVo, pageInfo);
			//处理结果集
			for(MtDataReportVo rpt : reportList){
				rpt.setSpisuncmName(getSpisuncmName(rpt.getSpisuncm()));

				if(DAY_REPORT == reportType || MONTH_REPORT == reportType){
					//月报表与日报表都显示 XXXX年XX月XX日
					String showTime = rpt.getIymd().toString().substring(0,4) + "年" + rpt.getIymd().toString().substring(4,6) + "月" + rpt.getIymd().toString().substring(6) + "日";
					rpt.setShowTime(showTime);
				}else {
					String showTime = rpt.getY() + "年" + rpt.getImonth() + "月";
					rpt.setShowTime(showTime);
				}
			}
		}catch (EMPException empEx){
			EmpExecutionContext.error(empEx,empEx.getMessage());
		}catch (Exception e){
			EmpExecutionContext.error(e,MODNAMES + "," + OPNAME + "点击详情查询异常！");
		}finally {
			LfSysuser loginSysUser = degreeBiz.getCurrenUser(request);
			Integer countSize = reportList == null ? 0:reportList.size();
			String conditionStr = "查询类型=" + reportTypeStr + ",SP账号=" + userId
					+ ",运营商=" + operatorStr + ",档位=" + degree
					+ "档,开始时间=" + startTime + ",查询结束时间=" + endTime +
					",查询总数=" + countSize;
			request.setAttribute("reportList", reportList);
			request.setAttribute("countSize", countSize);
			request.setAttribute("corpCode", loginCorpCode);
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("isRptFlag", isRptFlag(request));
			//记录日志
			String queryTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(start);
			String opContent = MODNAMES + "-" + MODNAMES + "点击详情， totalcount:" + countSize + "条 ,查询时间："+ queryTime +",耗时:"+(System.currentTimeMillis() - start)+"ms,条件:"+ conditionStr;
			EmpExecutionContext.info(MODNAMES, loginCorpCode, loginSysUser.getUserId().toString(), loginSysUser.getName(), opContent, StaticValue.GET);
			try {
				request.getRequestDispatcher(modName + base + "/rep_showDetailsInfo.jsp").forward(request, response);
			} catch (Exception e) {
				EmpExecutionContext.error(e, MODNAMES + "-" + MODNAMES + ",点击详情，跳转页面异常！");
			}
		}
	}

	/**
	 * 档位统计报表导出
	 * @param request
	 * @param response
	 */
	public void getRptExcel(HttpServletRequest request, HttpServletResponse response){
		//记录日志
		LfSysuser loginSysUser = degreeBiz.getCurrenUser(request);
		// 查询条件对象
		MtDataReportVo rptVo = null;
		//起始ms数
		long start = System.currentTimeMillis();
		//返回结果
		String result = "false";
		//报表结果集
		List<MtDataReportVo> reportList = null;
		//是否是点击详情下载
		boolean isDes;
		try {
			String flag = request.getParameter("isDes");
			isDes = "1".equals(flag);
			//从cookie中取值
			Cookie[] cookies = request.getCookies();
			for(Cookie cookie : cookies){
				if("degreeRptVo".equals(cookie.getName())){
					String degreeRptVo = cookie.getValue();
					String rptVoStr = URLDecoder.decode(degreeRptVo,"utf-8");
					rptVo = JSONObject.parseObject(rptVoStr, MtDataReportVo.class);
				}else if("detailInfoRptVo".equals(cookie.getName()) && isDes){
					String degreeRptVo = cookie.getValue();
					String rptVoStr = URLDecoder.decode(degreeRptVo,"utf-8");
					rptVo = JSONObject.parseObject(rptVoStr, MtDataReportVo.class);
				}
			}
			if(rptVo == null){
				throw new EMPException(MODNAMES + "-" + OPNAME  + "，档位报表导出功能异常，不存在对应的COOKIE值！");
			}
			//结果集
			reportList = degreeRptBiz.getDegreeRpt(rptVo, null);
			//处理结果集
			for(MtDataReportVo rpt : reportList){
				rpt.setSpisuncmName(getSpisuncmName(rpt.getSpisuncm()));
				if(rptVo.getDes()){
					if(DAY_REPORT == rptVo.getReportType() || MONTH_REPORT == rptVo.getReportType()){
						//月报表与日报表都显示 XXXX年XX月XX日
						String showTime = rpt.getIymd().toString().substring(0,4) + "年" + rpt.getIymd().toString().substring(4,6) + "月" + rpt.getIymd().toString().substring(6) + "日";
						rpt.setShowTime(showTime);
					}else {
						//年报表 则显示 XXXX年XX月
						String showTime = rpt.getY() + "年" + rpt.getImonth() + "月";
						rpt.setShowTime(showTime);
					}
				}else {
					switch (rptVo.getReportType()){
						case DAY_REPORT:
							String showStartTime = rptVo.getStartTime().replaceFirst("-","年").replaceFirst("-","月").replaceFirst("-","日");
							String showEndTime = rptVo.getEndTime().replaceFirst("-","年").replaceFirst("-","月").replaceFirst("-","日");
							rpt.setShowTime(showStartTime + "至" + showEndTime);
							break;
						case MONTH_REPORT:
							rpt.setShowTime(rpt.getY() + "年" + rpt.getImonth() + "月");
							break;
						case YEAR_REPORT:
							rpt.setShowTime(rpt.getY() + "年");
							break;
						default:
							throw new EMPException(MODNAMES + "," + OPNAME + "查询档位统计报表异常,查询类型参数错误！");
					}
				}
			}
			if(reportList.size() > 0 && reportList.size() < 500000){
			    Map<String, String> languageMap = new HashMap<String, String>();
                languageMap.put("time", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_fxapp_degreerep_time", request), "时间"));
			    languageMap.put("corcode", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_degree_report_corcode", request), "企业编码"));
                languageMap.put("corname", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_degree_report_corname", request), "企业名称"));
                languageMap.put("spaccount", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_fxapp_fsmx_spzh", request), "SP账号"));
                languageMap.put("operator", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_fxapp_fsmx_operator2", request), "运营商"));
                languageMap.put("degree", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_fxapp_degreerep_range", request), "档位"));
                languageMap.put("submits", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_fxapp_degreerep_tjhms", request), "提交号码数"));
                languageMap.put("sends", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_fxapp_degreerep_fscgs", request), "发送成功数"));
                languageMap.put("receives", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_fxapp_degreerep_jssbs", request), "接收失败数"));
                languageMap.put("downloads", StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_degree_report_downloads", request), "下载成功数"));
				HashMap<String,String> map = degreeRptBiz.createRptExcelFile(reportList,loginSysUser.getCorpCode(),rptVo.getDes(), isRptFlag(request), languageMap);
				request.getSession(false).setAttribute("degree_excel_map",map);
				result = "true";
			}
		}catch (EMPException empEx){
			EmpExecutionContext.error(empEx,empEx.getMessage());
		} catch (Exception e){
			EmpExecutionContext.error(e,MODNAMES + "-" + OPNAME + ",档位报表导出功能异常！");
		}finally {

			String queryTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(start);
			String opContent = MODNAMES + "-" + MODNAMES + ",档位统计报表导出,导出时间："+ queryTime +",耗时:"+(System.currentTimeMillis() - start)+"ms";
			EmpExecutionContext.info("档位报表导出功能", loginSysUser.getCorpCode(), loginSysUser.getUserId().toString(), loginSysUser.getName(), opContent, StaticValue.GET);
			try {
				response.getWriter().write(result);
			} catch (Exception e) {
				EmpExecutionContext.error(e, MODNAMES + "," + MODNAMES + ",档位报表导出功能异常,跳转页面异常！");
			}
		}
	}
	/**
	 * 档位报表下载文件
	 * @param request
	 * @param response
	 */
	public void downloadFile(HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession(RptStaticValue.GET_SESSION_FALSE);
		Object obj = session.getAttribute("degree_excel_map");
		session.removeAttribute("degree_excel_map");
		if(obj != null){
			// 弹出下载页面。
			DownloadFile dfs = new DownloadFile();
			Map<String, String> resultMap = (Map<String, String>) obj;
			String filePath = resultMap.get("filePath");
			String fileName = resultMap.get("fileName");
			dfs.downFile(request, response, filePath, fileName);
		}
	}
	/**
	 * 判断当前企业是否需要下载状态字段
	 */
	public String isRptFlag(HttpServletRequest request){

		//加载排序条件 
		LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
		//查询条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		PageInfo pageInfo=new PageInfo();
		try {

			//登录操作员信息
			LfSysuser loginSysuser = (LfSysuser)request.getSession(false).getAttribute("loginSysuser");
			String code=loginSysuser.getCorpCode();
			if(empCorpCode.equals(code)){
				return "3";//10万号
			}else{
				if(code != null && !"".equals(code))
				{
					code = URLDecoder.decode(code, "UTF-8");
					conditionMap.put("corpCode", code);
				}
				List<LfCorp> list = baseBiz.getByConditionNoCount(LfCorp.class, null, conditionMap,
						orderbyMap, pageInfo);
				if(list.size()>0){
					// 0:表示不需要;1:需要通知状态报告;2:需要下载状态报告;3:通知、下载状态报告都要;(默认通知状态报告必须)
					return list.get(0).getRptflag().toString();
				}
			}
		} catch (Exception e) {
            EmpExecutionContext.error(e, "发现异常");
		}
		return "";
	}
}
	

