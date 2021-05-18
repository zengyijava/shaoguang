package com.montnets.emp.greport.servlet;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.GlobConfigBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.system.LfPageField;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.greport.bean.GreportParamValue;
import com.montnets.emp.greport.biz.GreportBiz;
import com.montnets.emp.greport.biz.SpuserGreportBiz;
import com.montnets.emp.greport.vo.SpuserGreportVo;
import com.montnets.emp.i18n.util.MessageUtils;

/**
 * Sp账号对比图形报表
 * @project p_greport
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-20 上午11:12:53
 * @description
 */

@SuppressWarnings("serial")
public class grpt_spuserGreportSvt extends BaseServlet
{
	// 模块名称
	private final String	empRoot		= "greport";

	// 功能文件夹名
	private final String 	base		= "/grpt";

	//Sp账号对比 biz
	private final SpuserGreportBiz	spuserGreportbiz	= new SpuserGreportBiz();
	private final GreportBiz reportBiz = new GreportBiz();
	/**
	 * Sp账号对比
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		//时分秒格式化
		SimpleDateFormat hms=new SimpleDateFormat("HH:mm:ss");
		//起始ms数
		long startl=System.currentTimeMillis();
		//开始时间
		String starthms=hms.format(startl);
		// 短彩类型
		String mstype = request.getParameter("mstype");
		//排序
		String orderby =request.getParameter("orderby");
		// 企业编码
		String corpCode = reportBiz.getCorpCode(request);
		
		// 时间
		String timeYear = (Calendar.getInstance().get(Calendar.YEAR)) + "";
		try
		{
			//排序 默认为  发送量由大到小
			if(orderby==null){
				orderby="1";
			}
			//默认为短信
			if(mstype == null)
			{
				mstype = "0";
			}
			// 查询条件对象
			String year = request.getParameter("year");
			if(year==null||"".equals(year)){
				year =timeYear;
			}
			//年份列表
			List<String> ys=spuserGreportbiz.getYears(Integer.parseInt(mstype));
			List<String> oldys=(List<String>)request.getSession(GreportParamValue.GET_SESSION_FALSE).getAttribute("ys");
			//判断是否有更新变化
			if(oldys!=null&&ys.size()<=oldys.size()){
				oldys=null;
			}else{
				request.getSession(GreportParamValue.GET_SESSION_FALSE).setAttribute("ys", ys);
			}
			//存储短彩类型
			request.setAttribute("mstype", mstype);
			//排序
			request.setAttribute("orderby", orderby);
			//年份
			request.setAttribute("year", year);
			
			// 获取数据库短彩类型数据
			GlobConfigBiz gcBiz = new GlobConfigBiz();
			List<LfPageField> pagefileds = gcBiz.getPageFieldById("100001");
			request.getSession(GreportParamValue.GET_SESSION_FALSE).setAttribute("pagefiledusers", pagefileds);
			String opContent = "SP账号发送对比加载开始："+starthms+",耗时:"+(System.currentTimeMillis()-startl)+"ms";
			setLog(request, "SP账号发送对比", opContent, StaticValue.GET);
			request.getRequestDispatcher(this.empRoot + base + "/grpt_spuserGreport.jsp?lgcorpcode=" + corpCode).forward(request, response);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"SP账号发送对比sevlet查询异常");
		}
	}

	
	
	
	
	
	
	/**
	 * 输出sp账号图形报表json
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void createBbJson(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//时分秒格式化
		SimpleDateFormat hms=new SimpleDateFormat("HH:mm:ss");
		//起始ms数
		long startl=System.currentTimeMillis();
		//开始时间
		String starthms=hms.format(startl);
		// 短彩类型
		String msType = request.getParameter("mstype");
		// 企业编码
		String corpCode = reportBiz.getCorpCode(request);
		//排序
		String orderby =request.getParameter("orderby");
		// 时间
		String timeYear = (Calendar.getInstance().get(Calendar.YEAR)) + "";
		List<DynaBean> spusergreportList = null;
		try
		{
				//短彩类型 默认为短信
				if(msType == null||"".equals(msType))
				{
					msType = "0";
				}
				
				//排序
				if(orderby == null||"".equals(orderby))
				{
					orderby = "1";
				}
				
				// 查询条件对象
				SpuserGreportVo spuserGreportVo = new SpuserGreportVo();
				//年
				String year = request.getParameter("year");
				//月
				String imonth = request.getParameter("imonth");
				if(year==null||"".equals(year)){
					year =timeYear;
				}

				//标题
				String title="";
				if(imonth==null||"".equals(imonth)){
					title=year+MessageUtils.extractMessage("cxtj", "cxtj_sjfx_zhfsdb_ytitle", request);
				}else{
					//title=year+MessageUtils.extractMessage("cxtj", "cxtj_sjfx_tips21", request)+imonth+MessageUtils.extractMessage("cxtj", "cxtj_sjfx_zhfsdb_mtitle", request);
					
					String language  = (String) request.getSession().getAttribute(StaticValue.LANG_KEY);
					if (StaticValue.ZH_HK.equals(language)) {
						title=year+MessageUtils.extractMessage("cxtj", "cxtj_sjfx_tips21", request)+getMonth(imonth)+MessageUtils.extractMessage("cxtj", "cxtj_sjfx_zhfsdb_mtitle", request);
					}else{
						title=year+MessageUtils.extractMessage("cxtj", "cxtj_sjfx_tips21", request)+imonth+MessageUtils.extractMessage("cxtj", "cxtj_sjfx_zhfsdb_mtitle", request);
					}
				
				}
				
				// 短信类型
				spuserGreportVo.setMstype(Integer.parseInt(msType));
				//排序类型
				spuserGreportVo.setOrderby(orderby);
				//年
				spuserGreportVo.setY(year);
				//月
				spuserGreportVo.setImonth(imonth);
				//判断是否多企业
				if(StaticValue.getCORPTYPE() ==1){
					spuserGreportVo.setCorpCode(corpCode);
				}
				//获取结果集
				spusergreportList = spuserGreportbiz.getSpuserGreportByVo(spuserGreportVo);
				//发送总量
				long totoal=0l;
				//计算发送总量
				for(int i=0;i<spusergreportList.size();i++){
					DynaBean agvo=spusergreportList.get(i);
					totoal=totoal+Long.parseLong(agvo.get("icount").toString());
				}
				//拼接json字符串
				StringBuffer totaljson=new StringBuffer("{\"credits\":{\"text\":\"\",\"href\":\"\"},");
				//设置标题
				totaljson.append("\"title\":{\"text\":\""+title+"\"},");
				totaljson.append("\"xAxis\":{\"categories\":[");
				//sp帐号字符串
				StringBuffer xspuser=new StringBuffer("");
				//样式拼接字符串
				StringBuffer cssjson=new StringBuffer("");
				//设置字体大小
				cssjson.append("],\"labels\":{\"rotation\":-45,\"align\":\"right\",\"style\":{\"fontSize\":\"12px\",\"fontFamily\":\"Verdana, sans-serif\"}}},");
				cssjson.append("\"yAxis\":{\"min\":0,\"title\":{\"text\":\""+MessageUtils.extractMessage("cxtj", "cxtj_sjfx_chart_subtitle", request)+"\"}},");
				//格式化发送条数
				cssjson.append("\"tooltip\":{\"pointFormat\":\""+MessageUtils.extractMessage("cxtj", "cxtj_sjfx_tips4", request)+" <b>{point.y}"+MessageUtils.extractMessage("cxtj", "cxtj_sjfx_chart_unit", request)+"</b><br />"+MessageUtils.extractMessage("cxtj", "cxtj_sjfx_tips5", request)+"<b>{point.z:.1f}%</b>\"},");
				//cssjson.append("\"plotOptions\":{\"column\":{\"dataLabels\": {\"enabled\": \"true\"},\"pointPadding\":0.2,\"borderWidth\":0}},");
				cssjson.append("\"series\":[{\"name\":\""+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_spzh", request)+"\",\"data\":[");
				StringBuffer xvalues=new StringBuffer("");
				//拼接数据结果值
				for(int i=0;i<spusergreportList.size();i++){
					//获取动态bean
					DynaBean tgvo=spusergreportList.get(i);
					if(tgvo!=null){
						xspuser.append("\""+tgvo.get("userid").toString()+"\",");
						//百分比
						String propertion=new DecimalFormat("#0.0").format((tgvo.get("icount")==null?0:Long.parseLong(tgvo.get("icount").toString()))*100f/totoal);
						//总数
						String count=tgvo.get("icount")==null?"0":tgvo.get("icount").toString();
						xvalues.append("{\"y\":"+count+",\"z\":"+propertion+"},");
					}
				}
				//去除尾部逗号
				if(xvalues.lastIndexOf(",")!=-1){
					xvalues.deleteCharAt(xvalues.lastIndexOf(","));
				}
				//去除尾部逗号
				if(xspuser.lastIndexOf(",")!=-1){
					xspuser.deleteCharAt(xspuser.lastIndexOf(","));
				}
				//帐号
				totaljson.append(xspuser);
				//样式
				totaljson.append(cssjson);
				//数据值
				totaljson.append(xvalues);
				totaljson.append("]}]}");
			String opContent = MessageUtils.extractMessage("cxtj", "cxtj_sjfx_tips6", request)+starthms+","+MessageUtils.extractMessage("cxtj", "cxtj_sjfx_tips2", request)+":"+(System.currentTimeMillis()-startl)+"ms";
			setLog(request, MessageUtils.extractMessage("cxtj", "cxtj_sjfx_tips7", request), opContent, StaticValue.GET);
			//打印json字符串
			response.getWriter().print(totaljson.toString());
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取SP账号发送对比图形报表获取json异常");
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
	public void setLog(HttpServletRequest request,String opModule,String opContent,String opType){
		try
		{
			Object loginSysuserObj=request.getSession(GreportParamValue.GET_SESSION_FALSE).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				EmpExecutionContext.info(opModule, loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), opContent, opType);
			}
		}catch(Exception e)
		{
			EmpExecutionContext.error(e,opModule+opType+opContent+"日志写入异常");
		}
	}
	

	private String getMonth(String monthNum){
		if("1".equals(monthNum)){
			return "January";
		}
		if("2".equals(monthNum)){
			return "February";
		}
		if("3".equals(monthNum)){
			return "March";
		}
		if("4".equals(monthNum)){
			return "April";
		}
		if("5".equals(monthNum)){
			return "May";
		}
		if("6".equals(monthNum)){
			return "June";
		}
		if("7".equals(monthNum)){
			return "July";
		}
		if("8".equals(monthNum)){
			return "August";
		}
		if("9".equals(monthNum)){
			return "September";
		}
		if("10".equals(monthNum)){
			return "October";
		}
		if("11".equals(monthNum)){
			return "November";
		}
		if("12".equals(monthNum)){
			return "December";
		}
		return "";
	}


}
