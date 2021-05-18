package com.montnets.emp.report.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.selfparam.LfWgParmDefinition;
import com.montnets.emp.query.biz.QueryBiz;
import com.montnets.emp.report.bean.RptStaticValue;
import com.montnets.emp.report.biz.DynParamReportBiz;
import com.montnets.emp.report.vo.DynParmReportVo;
import com.montnets.emp.util.DownloadFile;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.TxtFileUtil;
/**
 * 动态参数报表
 * @project p_cxtj
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-22 下午02:47:58
 * @description
 */
public class rep_dynParamReportSvt extends BaseServlet {

	/**
	 * 
	 */
	private final String empRoot="cxtj";
	private final String base="/report";
	//时分秒格式化
	private final SimpleDateFormat hms=new SimpleDateFormat("HH:mm:ss");
	
	private static final long serialVersionUID = 2763516751863892733L;
	//动态参数报表biz
	protected final String  excelPath = new TxtFileUtil().getWebRoot()+"file/excel";
	protected int getIntParameter(String param, int defaultValue,HttpServletRequest request)
	{
		try
		{
			if(request.getParameter(param)!=null&&!"".equals(request.getParameter(param))){
				return Integer.parseInt(request.getParameter(param));
			}else{
				return defaultValue;
			}
		} catch (NumberFormatException e)
		{
			EmpExecutionContext.error(e,"获取分页信息异常");
			return defaultValue;
		}
	}
	
	/**
	 * 查询方法
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void find(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//起始ms数
		long startl=System.currentTimeMillis();
		//开始时间
		String starthms=hms.format(startl);
		HttpSession session = request.getSession(RptStaticValue.GET_SESSION_FALSE);

		//String corpCode = lfSysuser.getCorpCode();
		//企业编码
		String corpCode = request.getParameter("lgcorpcode");

		//开始时间
		String begintime = null == request.getParameter("begintime") ? ""
				: request.getParameter("begintime").toString().trim();// 开始时间

		//结束时间
		String endtime = null == request.getParameter("endtime") ? "" : request
				.getParameter("endtime").toString().trim();// 结束时间

		//动态参数vo
		DynParmReportVo dynParmReportVo = new DynParmReportVo();

		//开始时间
		dynParmReportVo.setSendTime(begintime);

		//结束时间
		dynParmReportVo.setEndTime(endtime);

		List<LfWgParmDefinition> paraList = new ArrayList<LfWgParmDefinition>();

		//动态参数报表volist
		List<DynParmReportVo> mtreportList = new ArrayList<DynParmReportVo>();

		//参数
		String paramNames = request.getParameter("paramName");

		//参数标题
		String paramTitle = "";

		String[] paramTemp = new String[3];

		String paramType = null;

		Integer paramSubNum = null;

		if (null != paramNames && 0 != paramNames.length()
				&& paramNames.contains("&")) {
			paramTemp = paramNames.split("&");

			paramType = paramTemp[0];

			paramSubNum = Integer.parseInt(paramTemp[1]);

			paramTitle = paramTemp[2];

		}
        long[] sum=null;

		PageInfo pageInfo = new PageInfo();

		boolean isFirstEnter = pageSet(pageInfo,request);
		try {
            DynParamReportBiz dpReportBiz = new DynParamReportBiz();
            if(!isFirstEnter)
            {
                if (null != paramType && 0 != paramType.length()) {
                    mtreportList = dpReportBiz.getMtDataReportList(dynParmReportVo,
                            paramType, paramSubNum, pageInfo);
                }

                if (null != paramType && 0 != paramType.length()
                        && null != paramSubNum) {
                    sum = dpReportBiz.findSumCount(dynParmReportVo, paramType,
                            paramSubNum, pageInfo);
                }
            }

            paraList = dpReportBiz.getAllParamConfList(corpCode);
            //统计合计
            session.setAttribute("dyn_sumArray", sum);
			//参数标题
			session.setAttribute("paramTitle", paramTitle);
			//参数列表
			request.setAttribute("paraList", paraList);
			//分页对象
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("mtreportList", mtreportList);
            request.setAttribute("isFirstEnter", isFirstEnter);
			//request.setAttribute("error", "error");
			long count=0l;
			//从pageinfo中获取查询总条数
			if(pageInfo!=null){
				count=pageInfo.getTotalRec();
			}
			// 写日志
			String opContent = "自定义参数报表：" + count + "条 成功开始："+starthms+",耗时:"+(System.currentTimeMillis()-startl)+"ms";
			new QueryBiz().setLog(request, "自定义参数报表", opContent, StaticValue.GET);

		} catch (Exception e) {
			//异常处理
			request.setAttribute("findresult", "-1");
			request.setAttribute("pageInfo", pageInfo);
			EmpExecutionContext.error(e,"自定义参数报表servlet查询异常");

		}finally{
			request.getRequestDispatcher(
					this.empRoot +base+ "/rep_dynParamReport.jsp").forward(
					request, response);
		}

	}

	/**
	 * 自定参数统计报表导出
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	// excel导出全部数据
	public void r_dpRptExportExcel(HttpServletRequest request,HttpServletResponse response) throws Exception {
		//起始ms数
		long startl=System.currentTimeMillis();
		//开始时间
		String starthms=hms.format(startl);
		List<DynParmReportVo> mdlist = new ArrayList<DynParmReportVo>();
		HttpSession session = request.getSession(RptStaticValue.GET_SESSION_FALSE);

		try {
			DynParmReportVo dynParmReportVo = new DynParmReportVo();

			String paramType = request.getParameter("paramType");

			Integer paramSubNum = null == request.getParameter("paramSubNum") ? null: Integer.parseInt(request.getParameter("paramSubNum"));

			String paramTitle = null == session.getAttribute("paramTitle") ? "未知": (String) session.getAttribute("paramTitle");
			// 开始时间
			String begintime = null == request.getParameter("begintime") ? "": request.getParameter("begintime").toString().trim();
			// 结束时间
			String endtime = null == request.getParameter("endtime") ? "" : request.getParameter("endtime").toString().trim();

			dynParmReportVo.setSendTime(begintime);

			dynParmReportVo.setEndTime(endtime);

			DynParamReportBiz dpReportBiz = new DynParamReportBiz();
			if (null != paramType && 0 != paramType.length()) {
				mdlist = dpReportBiz.getMtDataReportList_unpage(
						dynParmReportVo, paramType, paramSubNum);
			}

			long[] sumArray = (long[]) session.getAttribute("dyn_sumArray");

			String showTime = session.getAttribute("dyn_showTime").toString();
			response.setContentType("html/text");
			PrintWriter out = response.getWriter();
			if (mdlist != null && mdlist.size() > 0) {
				Map<String, String> resultMap = dpReportBiz.createDynParamReportExcel(
						mdlist, sumArray, showTime, paramTitle,request);
				String fileName = resultMap.get("FILE_NAME");
				// 写日志
				String opContent = "导出自定参数统计报表：" + mdlist.size() + "条成功，文件名："+fileName+",开始："+starthms+",耗时:"+(System.currentTimeMillis()-startl)+"ms";
				new QueryBiz().setLog(request, "自定参数统计报表", opContent, StaticValue.GET);
				mdlist.clear();
				mdlist=null;
				session.setAttribute("dyn_export_map",resultMap);
				out.print("true");
//				String filePath = (String) resultMap.get("FILE_PATH");
//				DownloadFile df = new DownloadFile();
//				df.downFile(request, response, filePath, fileName);
			}else{
				out.print("false");
			}
		} catch (Exception e) {
            //异常处理
			EmpExecutionContext.error(e,"自定义参数报表导出servlet异常");
		}
	}
	
	
	/**
	 * 下载导出文件 动态参数导出下载
	 * @param request
	 * @param response
	 */
    public void downloadFile(HttpServletRequest request, HttpServletResponse response)   {
        HttpSession session = request.getSession(RptStaticValue.GET_SESSION_FALSE);
        Object obj = session.getAttribute("dyn_export_map");
        session.removeAttribute("dyn_export_map");
        if(obj != null){
            // 弹出下载页面。
            DownloadFile dfs = new DownloadFile();
            Map<String, String> resultMap = (Map<String, String>) obj;
            String fileName = resultMap.get("FILE_NAME");
            String filePath = resultMap.get("FILE_PATH");
            dfs.downFile(request, response, filePath, fileName);
        }
    }
	

	


}
