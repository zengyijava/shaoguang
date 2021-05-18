package com.montnets.emp.greport.servlet;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

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
import com.montnets.emp.greport.biz.TotalGreportBiz;
import com.montnets.emp.greport.vo.TotalGreportVo;
import com.montnets.emp.i18n.util.MessageUtils;

/**
 * 总体发送趋势图形报表
 * 
 * @project p_greport
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-19 上午10:37:36
 * @description
 */

@SuppressWarnings("serial")
public class grpt_totalGreportSvt extends BaseServlet
{
	// 模块名称
	private final String	empRoot			= "greport";

	// 功能文件夹名
	private final String	base			= "/grpt";

	// 总体发送趋势 biz
	private final TotalGreportBiz	totalGreportbiz	= new TotalGreportBiz();
	
	private final GreportBiz reportBiz = new GreportBiz();
	/**
	 * 总体发送趋势
	 * 
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
		// 企业编码
		String corpCode = reportBiz.getCorpCode(request);
		
		// 时间
		String timeYear = (Calendar.getInstance().get(Calendar.YEAR)) + "";
		try
		{
			// 默认为短信
			if(mstype == null)
			{
				mstype = "0";
			}
			// 查询条件对象
			String year = request.getParameter("year");
			if(year == null || "".equals(year))
			{
				year = timeYear;
			}
			//年
			List<String> ys = totalGreportbiz.getYears(Integer.parseInt(mstype));
			//月
			List<String> s = (List<String>) request.getSession(GreportParamValue.GET_SESSION_FALSE).getAttribute("ys");
			//判断年份是否有更新
			if(s != null && ys.size() <= s.size())
			{
				s = null;
			}
			else
			{
				request.getSession(GreportParamValue.GET_SESSION_FALSE).setAttribute("ys", ys);
			}
			//短彩类型
			request.setAttribute("mstype", mstype);
			//年份
			request.setAttribute("year", year);

			// 获取数据库短彩类型数据
			GlobConfigBiz gcBiz = new GlobConfigBiz();
			List<LfPageField> pagefileds = gcBiz.getPageFieldById("100001");
			request.getSession(GreportParamValue.GET_SESSION_FALSE).setAttribute("pagefiledusers", pagefileds);
			String opContent = MessageUtils.extractMessage("cxtj", "cxtj_sjfx_tips10", request)+starthms+","+MessageUtils.extractMessage("cxtj", "cxtj_sjfx_tips2", request)+":"+(System.currentTimeMillis()-startl)+"ms";
			setLog(request, MessageUtils.extractMessage("cxtj", "cxtj_sjfx_tips11", request), opContent, StaticValue.GET);
			request.getRequestDispatcher(this.empRoot + base + "/grpt_totalGreport.jsp?lgcorpcode=" + corpCode).forward(request, response);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "总图形报表趋势sevlet查询异常");
		}
	}

	/**
	 * 输出总趋势json数据
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void createBbJson_bak(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		// 短彩类型
		String msType = request.getParameter("mstype");
		// 是否是对比型图形报表
		String ismuti = request.getParameter("ismuti");
		// 企业编码
		String corpCode = reportBiz.getCorpCode(request);
		// 时间
		String timeYear = (Calendar.getInstance().get(Calendar.YEAR)) + "";
		Map<String, DynaBean> totaltreportList = null;
		try
		{
			//短彩类型
			if(msType == null || "".equals(msType))
			{
				msType = "0";
			}
			//是否多条
			if(ismuti == null || "".equals(ismuti))
			{
				ismuti = "0";
			}

			// 查询条件对象
			TotalGreportVo totalGreportVo = new TotalGreportVo();
			//年
			String year = request.getParameter("year");
			//月
			String imonth = request.getParameter("imonth");
			if(year == null || "".equals(year))
			{
				year = timeYear;
			}

			// 短信类型
			totalGreportVo.setMstype(Integer.parseInt(msType));
			// 年
			totalGreportVo.setY(year);
			//月份数
			int size = 12;
			//月份对比条数
			int mutcount = 1;
			//旧的年份
			List<String> s = (List<String>) request.getSession(GreportParamValue.GET_SESSION_FALSE).getAttribute("ys");
			//没选月份
			if(imonth == null || "".equals(imonth))
			{
				// 月
				totalGreportVo.setImonth(null);
				totalGreportVo.setReporttype(0);
				//判断是否多条
				if("1".equals(ismuti))
				{
					totalGreportVo.setY(null);
					if(s != null)
					{
						mutcount = s.size();
					}
				}
			}
			else
			{
				//选了月份
				totalGreportVo.setImonth(imonth);
				totalGreportVo.setReporttype(1);
				//获取月份天数
				Calendar time = Calendar.getInstance();
				time.clear();
				time.set(Calendar.YEAR, Integer.parseInt(year)); // year年
				time.set(Calendar.MONTH, Integer.parseInt(imonth) - 1);// Calendar对象默认一月为0,month月
				//天数
				size = time.getActualMaximum(Calendar.DAY_OF_MONTH);//
				//是否多条
				if("1".equals(ismuti))
				{
					totalGreportVo.setImonth(null);
					mutcount = 12;
					size = 31;
				}
			}
			//设置条件
			totalGreportVo.setIsmuti(Integer.parseInt(ismuti));
			//企业编码
			totalGreportVo.setCorpCode(corpCode);
			//获取结果集
			totaltreportList = totalGreportbiz.getTotalGreportByVo(totalGreportVo);
			
			//拼接json格式
			StringBuffer totaljson = new StringBuffer("{\"JSChart\" : {\"datasets\" : [");
			int j = 0;
			for (int d = 0; d < mutcount; d++)
			{
				j++;
				//线条名称设置
				totaljson.append("{\"id\" : \"line_" + j + "\",\"type\" : \"line\",\"data\" : [");
				//月份
				int yormonth = 0;
				//年份
				String stryear = "";
				//判断是否有多条
				if("1".equals(ismuti))
				{
					//年份则取月
					if(size == 12)
					{
						stryear = s.get(d);
					}
					else
					{
						//月份则取年月
						if(j < 10)
						{
							stryear = "0" + j;
						}
						else
						{
							stryear = j + "";
						}
					}
				}
				else
				{
					//月
					if(totalGreportVo.getReporttype() == 0)
					{
						stryear = year;
					}
					else
					{
						//年
						if(imonth!=null && imonth.length() == 1)
						{
							stryear = "0" + imonth;
						}
						else
						{//月
							stryear = imonth;
						}
					}
				}
				//循环设置值
				for (int i = 0; i < size; i++)
				{
					yormonth++;
					//拼接key值
					String keystr = "";
					if(yormonth < 10)
					{
						//小于十月的前面加零
						keystr = "0" + yormonth;
					}
					else
					{
						keystr = yormonth + "";
					}
					//key值=年+月
					keystr = stryear + keystr;
					//通过key值去动态bean对象
					DynaBean tgvo = totaltreportList.get(keystr);
					if(tgvo != null)
					{
						//格式化数据
						String count = new DecimalFormat("#0.00").format((tgvo.get("icount") == null ? 0 : Long.parseLong(tgvo.get("icount").toString())) / 10000f);
						totaljson.append("{\"unit\" : \"" + yormonth + "\",\"value\" : \"" + count + "\"},");
					}
					else
					{
						totaljson.append("{\"unit\" : \"" + yormonth + "\",\"value\" : \"0\"},");
					}
				}
				//去除尾部逗号
				totaljson.deleteCharAt(totaljson.lastIndexOf(","));
				totaljson.append("]},");
			}
			//去除尾部逗号
			totaljson.deleteCharAt(totaljson.lastIndexOf(","));
			totaljson.append("],\"optionset\" : [{\"set\" : \"setAxisColor\",\"value\" : \"'#b5b5b5'\"},{\"set\" : \"setAxisNameColor\",\"value\" : \"'#4A4A4A'\"");
			totaljson.append("},{\"set\" : \"setAxisNameFontSize\",\"value\" : \"12\"},");
			//设置左右边距
			totaljson.append("{\"set\" : \"setAxisPaddingLeft\",\"value\" : \"60\"},");
			totaljson.append("{\"set\" : \"setAxisPaddingRight\",\"value\" : \"30\"},");
			totaljson.append("{\"set\" : \"setAxisPaddingTop\",\"value\" : \"50\"},{\"set\" : \"setAxisPaddingBottom\",\"value\" : \"50\"},");
			totaljson.append("{\"set\" : \"setTextPaddingBottom\",\"value\" : \"15\"},");
			totaljson.append("{\"set\" : \"setTextPaddingLeft\",\"value\" : \"15\"},");
			//设置颜色
			totaljson.append("{\"set\" : \"setAxisValuesColor\",\"value\" : \"'#949494'\"},");
			totaljson.append("{\"set\" : \"setLineColor\",\"value\" : \"'#509ACD'\"},");
			totaljson.append("{\"set\" : \"setTitleColor\",\"value\" : \"'#000'\"},");
			//设置大小
			totaljson.append("{\"set\" : \"setSize\",\"value\" : \"1000, 500\"},");
			totaljson.append("{\"set\" : \"setFlagRadius\",\"value\" : \"6\"},");
			totaljson.append("{\"set\" : \"setBackgroundImage\",\"value\" : \"'chart_bg.jpg'\"}");
			totaljson.append("]}}");

			response.getWriter().print(totaljson.toString());
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取系统发送趋势图图形报表获取json异常");
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
	/**
	 * 输出总趋势json数据
	 * 
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
		// 是否是对比型图形报表
		String stempval = request.getParameter("stempval");
		// 企业编码
		String corpCode = reportBiz.getCorpCode(request);
		// 时间
		String timeYear = (Calendar.getInstance().get(Calendar.YEAR)) + "";
		Map<String, DynaBean> totaltreportList = null;
		try
		{
			//短彩类型 
			if(msType == null || "".equals(msType))
			{
				//默认为零
				msType = "0";
			}

			// 查询条件对象
			TotalGreportVo totalGreportVo = new TotalGreportVo();
			//年
			String year = request.getParameter("year");
			//月
			String imonth = request.getParameter("imonth");
			//年
			if(year == null || "".equals(year))
			{
				year = timeYear;
			}

			// 短信类型
			totalGreportVo.setMstype(Integer.parseInt(msType));
			// 年
			totalGreportVo.setY(year);
			//定义月份数
			int size = 12;
			//定义对比条数 默认为1条
			int mutcount = 1;
			//标题
			String titile="";
			//单位
			String unit="";
			//没有选择月份
			if(imonth == null || "".equals(imonth))
			{
				titile=year+MessageUtils.extractMessage("cxtj", "cxtj_sjfx_tips12", request);
				unit=MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_m", request);
				// 月
				totalGreportVo.setImonth(null);
				totalGreportVo.setReporttype(0);
			}
			else
			{
				String language  = (String) request.getSession().getAttribute(StaticValue.LANG_KEY);
				if (StaticValue.ZH_HK.equals(language)) {
					//选择了月份
					titile=year+MessageUtils.extractMessage("cxtj", "cxtj_sjfx_tips21", request)+getMonth(imonth)+MessageUtils.extractMessage("cxtj", "cxtj_sjfx_tips13", request);
				}else{
					//选择了月份
					titile=year+MessageUtils.extractMessage("cxtj", "cxtj_sjfx_tips21", request)+imonth+MessageUtils.extractMessage("cxtj", "cxtj_sjfx_tips13", request);
					unit=MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_d", request);
				}
				totalGreportVo.setImonth(imonth);
				totalGreportVo.setReporttype(1);
				//获取月份的天数
				Calendar time = Calendar.getInstance();
				time.clear();
				time.set(Calendar.YEAR, Integer.parseInt(year)); // year年
				time.set(Calendar.MONTH, Integer.parseInt(imonth) - 1);// Calendar对象默认一月为0,month月
				size = time.getActualMaximum(Calendar.DAY_OF_MONTH);//
				//如果是对比的那么直接将月份天数设为31
				if(stempval!=null&&!"".equals(stempval)){
					size=31;
				}
				
			}
			//年份list定义
			List<String> yearlist=new ArrayList<String>();
			//如果是对比的图形报表则重新设置
			if(stempval!=null&&!"".equals(stempval)){
				//解析选择对比的年份月份
				StringBuffer yab=new StringBuffer(stempval);
				yab.deleteCharAt(yab.lastIndexOf("a"));
				stempval=yab.toString();
				String[] yeararray=stempval.split("a");
				//设置年
				totalGreportVo.setY(stempval);
				//月份设置为空
				totalGreportVo.setImonth(null);
				//对比条数
				mutcount=yeararray.length;
				//循环获取对比年月
				for(String yomonth:yeararray){
					if(imonth == null || "".equals(imonth))
					{
						String[] yomontharray=yomonth.split(",");
						//月份
						if(yomontharray.length==2){
							yearlist.add(yomontharray[0]);
						}
					}else{
						yearlist.add(yomonth);
					}
				}
				//对比则将标题修改
				titile=MessageUtils.extractMessage("cxtj", "cxtj_sjfx_tips14", request);
			}
			//判断是否多企业
			if(StaticValue.getCORPTYPE() ==1){
				totalGreportVo.setCorpCode(corpCode);
			}
			//获取结果集
			totaltreportList = totalGreportbiz.getTotalGreportByVo(totalGreportVo);
			//拼接json字符串
			StringBuffer totaljson = new StringBuffer("");
			totaljson.append("{\"credits\": {\"text\": \"\",\"href\": \"\"},");
			//标题
			totaljson.append("\"title\": {\"text\": \""+titile+"\",\"x\": -20},");
			totaljson.append("\"xAxis\": {\"categories\": [");
			//月份个数或者天数个数
			int monthorday=0;
			while(monthorday<size)
			{
				monthorday++;
				//设置横坐标 几月 或几日
				String language  = (String) request.getSession().getAttribute(StaticValue.LANG_KEY);
				if(StaticValue.ZH_HK.equals(language)&&(imonth == null || "".equals(imonth))){
					totaljson.append("\""+getMonth(""+monthorday)+"\",");
				}else{
					totaljson.append("\""+monthorday+unit+"\",");
				}
			}
			//去除尾部逗号
			if(size!=0){
				totaljson.deleteCharAt(totaljson.lastIndexOf(","));
			}
			totaljson.append("]");
			totaljson.append("},");
			//纵坐标的文字
			totaljson.append("\"yAxis\": {\"title\": {\"text\": \""+MessageUtils.extractMessage("cxtj", "cxtj_sjfx_chart_subtitle", request)+"\"},\"min\":0,");
			//设置样式
			totaljson.append("\"plotLines\": [{\"value\": 0,\"width\": 1,\"color\": \"#808080\"}]},");
			totaljson.append("\"tooltip\": {\"valueSuffix\": \""+MessageUtils.extractMessage("cxtj", "cxtj_sjfx_chart_unit", request)+"\"},\"legend\": {\"layout\": \"vertical\",\"align\": \"right\",\"verticalAlign\": \"middle\",\"borderWidth\": 0},");
			totaljson.append("\"series\": [");
			int j = 0;
			//循环填充数据
			for (int d = 0; d < mutcount; d++)
			{
				j++;
				//年或者月
				int yormonth = 0;
				//年
				String stryear = "";
				//对比项图例文字
				String linetext="";
				//判断是否与对比
				if(stempval!=null&&!"".equals(stempval))
				{
					//没选择月份
					if(imonth == null || "".equals(imonth))
					{
						//年份字符串
						stryear = yearlist.get(d);
						linetext= yearlist.get(d)+MessageUtils.extractMessage("cxtj", "cxtj_sjfx_tips21", request);
					}
					else
					{
						//选择了月份
						String[] ym=yearlist.get(d).split(",");
						String mth="0"+ym[1];
						if(mth.length()>2){
							mth=mth.substring(1);
						}
						stryear = ym[0]+mth;
						//图例拼接年月
						String language  = (String) request.getSession().getAttribute(StaticValue.LANG_KEY);
						if (StaticValue.ZH_HK.equals(language)) {
							linetext=ym[0]+MessageUtils.extractMessage("cxtj", "cxtj_sjfx_tips21", request)+ getMonth(ym[1]);
						}else{
							linetext=ym[0]+MessageUtils.extractMessage("cxtj", "cxtj_sjfx_tips21", request)+ ym[1]+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_m", request);
						}
					}
				}
				else
				{
					//没有对比年月
					//没选月份
					if(totalGreportVo.getReporttype() == 0)
					{
						stryear = year;
						linetext= year+MessageUtils.extractMessage("cxtj", "cxtj_sjfx_tips21", request);
					}
					else
					{
						//选了月份
						if(imonth!=null&&imonth.length() == 1)
						{
							stryear = year+"0" + imonth;
						}
						else
						{
							stryear =year+ imonth;
						}
						//拼接图例字符串
						String language  = (String) request.getSession().getAttribute(StaticValue.LANG_KEY);
						if (StaticValue.ZH_HK.equals(language)) {
							linetext= year+MessageUtils.extractMessage("cxtj", "cxtj_sjfx_tips21", request)+" "+getMonth(imonth);
						}else{
							linetext= year+MessageUtils.extractMessage("cxtj", "cxtj_sjfx_tips21", request)+imonth+MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_m", request);
						}
					}
				}
			totaljson.append("{\"name\": \""+linetext+"\",\"data\": [");
			//拼接数据值
			for (int i = 0; i < size; i++)
			{
				yormonth++;
				//定义获取数据的mapkey值
				String keystr = "";
				if(yormonth < 10)
				{
					//小于十月份前面加零
					keystr = "0" + yormonth;
				}
				else
				{
					//大于十月份
					keystr = yormonth + "";
				}
				//key值拼接
				keystr = stryear + keystr;
				//通过key值获取对应的数据值对象
				DynaBean tgvo = totaltreportList.get(keystr);
				if(tgvo != null)
				{
					//成功数格式化
					String count = tgvo.get("icount") == null ? "0" : tgvo.get("icount").toString();
					totaljson.append("" + count + ",");
				}
				else
				{
					totaljson.append("0,");
				}

			}
			//去除尾部逗号
			totaljson.deleteCharAt(totaljson.lastIndexOf(","));
			totaljson.append("]},");
		}
		//去除尾部逗号
		totaljson.deleteCharAt(totaljson.lastIndexOf(","));
		totaljson.append("]}");
		
		String opContent = MessageUtils.extractMessage("cxtj", "cxtj_sjfx_tips10", request)+starthms+","+MessageUtils.extractMessage("cxtj", "cxtj_sjfx_tips2", request)+":"+(System.currentTimeMillis()-startl)+"ms";
		setLog(request, MessageUtils.extractMessage("cxtj", "cxtj_sjfx_tips11", request), opContent, StaticValue.GET);

		//打印json字符串
		response.getWriter().print(totaljson.toString());
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取图形报表获取json异常");
		}
		
		//String ss="{\"credits\":{\"text\":\"\",\"href\":\"\"},\"title\":{\"text\":\"11111\",\"x\":-20},\"subtitle\":{\"text\":\"22222\",\"x\":-20},\"xAxis\":{\"categories\":[\"Jan\",\"Feb\",\"Mar\",\"Apr\",\"May\",\"Jun\",\"Jul\",\"Aug\",\"Sep\",\"Oct\",\"Nov\",\"Dec\"]},\"yAxis\":{\"title\":{\"text\":\"Temperature (\uc9f8C)\"},\"plotLines\":[{\"value\":0,\"width\":1,\"color\":\"#808080\"}]},\"tooltip\":{\"valueSuffix\":\"\uc9f8C\"},\"legend\":{\"layout\":\"vertical\",\"align\":\"right\",\"verticalAlign\":\"middle\",\"borderWidth\":0},\"series\":[{\"name\":\"\u7b2c\u4e00\u6761\u6570\u636e\",\"data\":[7,14,12,18,14,11,9,13,2,16,7,12]},{\"name\":\"\u7b2c\u4e8c\u6761\u6570\u636e\",\"data\":[17,114,2,18,4,1,9,13,112,16,7,100]}]}";
		//response.getWriter().print(ss);
		
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
}
