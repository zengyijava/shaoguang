package com.montnets.emp.greport.servlet;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.GlobConfigBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.biztype.LfBusManager;
import com.montnets.emp.entity.system.LfPageField;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.greport.bean.GreportParamValue;
import com.montnets.emp.greport.biz.BusGreportBiz;
import com.montnets.emp.greport.biz.GreportBiz;
import com.montnets.emp.greport.vo.BusGreportVo;
import com.montnets.emp.i18n.util.MessageUtils;

/**
 * 业务类型发送对比图形报表
 * @project p_greport
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-19 上午10:37:36
 * @description
 */

@SuppressWarnings("serial")
public class grpt_busGreportSvt extends BaseServlet
{
	// 模块名称
	private final String	empRoot		= "greport";

	// 功能文件夹名
	private final String base		= "/grpt";

	//业务类型发送对比 biz
	private final BusGreportBiz	busGreportbiz	= new BusGreportBiz();
	private final GreportBiz reportBiz = new GreportBiz();


	/**
	 * 业务类型发送对比
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
			//年份查询
			List<String> ys=busGreportbiz.getYears(Integer.parseInt(mstype));
			List<String> s=(List<String>)request.getSession(GreportParamValue.GET_SESSION_FALSE).getAttribute("ys");
			//判断年份是否有变化
			if(s!=null&&ys.size()<=s.size()){
				s=null;
			}else{
				if(ys.size()==0){
					ys.add(year);
				}
				request.getSession(GreportParamValue.GET_SESSION_FALSE).setAttribute("ys", ys);
			}
			//查询业务类型条件
			LinkedHashMap<String, String> conditionbusMap = new LinkedHashMap<String, String>();
			//查询业务类型排序
			LinkedHashMap<String, String> orconp = new LinkedHashMap<String, String>();
			conditionbusMap.put("corpCode&in", "0," + corpCode);
			orconp.put("corpCode", "asc");
			BaseBiz basebiz=new BaseBiz();
			//根据企业编码查询业务类型
			List<LfBusManager> busList = basebiz.getByCondition(
					LfBusManager.class, conditionbusMap, orconp);
			//存储业务类型
			request.getSession(GreportParamValue.GET_SESSION_FALSE).setAttribute("buslist", busList);
			//存储短彩类型
			request.setAttribute("mstype", mstype);
			request.setAttribute("year", year);
			
			// 获取数据库短彩类型数据
			GlobConfigBiz gcBiz = new GlobConfigBiz();
			List<LfPageField> pagefileds = gcBiz.getPageFieldById("100001");
			request.getSession(GreportParamValue.GET_SESSION_FALSE).setAttribute("pagefiledusers", pagefileds);
			
			String opContent = "业务类型发送对比加载开始："+starthms+",耗时:"+(System.currentTimeMillis()-startl)+"ms";
			setLog(request, "业务类型发送对比", opContent, StaticValue.GET);
			
			request.getRequestDispatcher(this.empRoot + base + "/grpt_busGreport.jsp?lgcorpcode=" + corpCode).forward(request, response);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"业务类型发送对比sevlet查询异常");
		}
	}

	
	
	
	
	
	
	/**
	 *  输出业务类型报表json数据
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void createBbJson_bak(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		// 短彩类型
		String msType = request.getParameter("mstype");
		// 企业编码
		String corpCode = reportBiz.getCorpCode(request);
		// 时间
		String timeYear = (Calendar.getInstance().get(Calendar.YEAR)) + "";
		//获取动态bean结果集map
		Map<String, DynaBean> busgreportList = null;
		try
		{
				//短彩类型
				if(msType == null||"".equals(msType))
				{
					msType = "0";
				}

				// 查询条件对象
				BusGreportVo busGreportVo = new BusGreportVo();
				//年
				String year = request.getParameter("year");
				//月
				String imonth = request.getParameter("imonth");
				//业务类型编码
				String buscode = request.getParameter("buscode");
				//年
				if(year==null||"".equals(year)){
					year =timeYear;
				}

				// 短信类型
				busGreportVo.setMstype(Integer.parseInt(msType));
				//年
				busGreportVo.setY(year);
				//月数
				int size=12;
				//业务类型个数
				int mutcount=1;
				List<LfBusManager> busls=(List<LfBusManager>)request.getSession(GreportParamValue.GET_SESSION_FALSE).getAttribute("buslist");
				if(busls!=null){
					//设置业务类型个数
					mutcount=busls.size();
				}
				//选了年
				if(imonth==null||"".equals(imonth)){
					// 月
					busGreportVo.setReporttype(0);
				}else{
					//选了月
					busGreportVo.setImonth(imonth);
					busGreportVo.setReporttype(1);
					Calendar time=Calendar.getInstance(); 
					time.clear(); 
					time.set(Calendar.YEAR,Integer.parseInt(year)); //year年
					time.set(Calendar.MONTH,Integer.parseInt(imonth)-1);//Calendar对象默认一月为0,month月
					//日子的个数
					size=time.getActualMaximum(Calendar.DAY_OF_MONTH);//
				}
				//业务类型字符串
				StringBuffer buscodes=new StringBuffer("");
				
				//解析业务类型
				if(buscode!=null&&!"".equals(buscode)){
					String[] bus=buscode.split(",");
					mutcount=bus.length;
					for(String bcode:bus){
						buscodes.append("'"+bcode+"',");
					}
					buscodes.deleteCharAt(buscodes.lastIndexOf(","));
				}
				//设置业务类型条件
				busGreportVo.setSvrtype(buscodes.toString());
				//设置企业编码条件
				busGreportVo.setCorpCode(corpCode);
				//获取结果集
				busgreportList = busGreportbiz.getBusGreportByVo(busGreportVo);
				//拼接json字符串
				StringBuffer busjson=new StringBuffer("{\"JSChart\" : {\"datasets\" : [");
				//业务类型条数
				int j=0;
				for(int d=0;d<mutcount;d++){
					j++;
					busjson.append("{\"id\" : \"line_"+j+"\",\"type\" : \"line\",\"data\" : [");
					//月
					int yormonth=0;
					//业务类型
					String busstr="";
					if(busls.get(d)!=null){
						busstr=busls.get(d).getBusName().replace("\"", "");
					}
					//循环设业务类型对应的数据值
					for(int i=0;i<size;i++){
						yormonth++;
						//拼接map中的key值
						String keystr="";
						if(yormonth<10){
							keystr="0"+yormonth;
						}else{
							keystr=yormonth+"";
						}
						keystr=busstr+keystr;
						//通过map的key值获取动态bean对象
						DynaBean tgvo=busgreportList.get(keystr);
						//设置值
						if(tgvo!=null){
							String count=new DecimalFormat("#0.00").format((tgvo.get("icount")==null?0:Long.parseLong((tgvo.get("icount").toString())))/10000f);
							busjson.append("{\"unit\" : \""+yormonth+"\",\"value\" : \""+count+"\"},");
						}else{
							busjson.append("{\"unit\" : \""+yormonth+"\",\"value\" : \"0\"},");
						}
					}
					//去除尾部逗号
					busjson.deleteCharAt(busjson.lastIndexOf(","));
					busjson.append("]},");
				}
				busjson.deleteCharAt(busjson.lastIndexOf(","));
				//拼接图形的属性
				busjson.append("],\"optionset\" : [{\"set\" : \"setAxisColor\",\"value\" : \"'#b5b5b5'\"},{\"set\" : \"setAxisNameColor\",\"value\" : \"'#4A4A4A'\"");
				busjson.append("},{\"set\" : \"setAxisNameFontSize\",\"value\" : \"12\"},");
				busjson.append("{\"set\" : \"setAxisPaddingLeft\",\"value\" : \"60\"},");
				busjson.append("{\"set\" : \"setAxisPaddingRight\",\"value\" : \"30\"},");
				busjson.append("{\"set\" : \"setAxisPaddingTop\",\"value\" : \"50\"},");
				busjson.append("{\"set\" : \"setTextPaddingBottom\",\"value\" : \"15\"},");
				busjson.append("{\"set\" : \"setTextPaddingLeft\",\"value\" : \"15\"},");
				busjson.append("{\"set\" : \"setAxisValuesColor\",\"value\" : \"'#949494'\"},");
				//设置线条颜色
				busjson.append("{\"set\" : \"setLineColor\",\"value\" : \"'#509ACD'\"},");
				//设置标题颜色
				busjson.append("{\"set\" : \"setTitleColor\",\"value\" : \"'#000'\"},");
				//设置大小
				busjson.append("{\"set\" : \"setSize\",\"value\" : \"1000, 500\"},");
				busjson.append("{\"set\" : \"setFlagRadius\",\"value\" : \"6\"},");
				busjson.append("{\"set\" : \"setBackgroundImage\",\"value\" : \"'chart_bg.jpg'\"}");
				busjson.append("]}}");
				
			response.getWriter().print(busjson.toString());
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取业务类型对比图形报表获取json异常");
		}
	}

	
	
	
	/**
	 *  输出业务类型报表json数据
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
		// 时间
		String timeYear = (Calendar.getInstance().get(Calendar.YEAR)) + "";
		Map<String, DynaBean> busgreportList = null;
		try
		{
				//短彩类型
				if(msType == null||"".equals(msType))
				{
					msType = "0";
				}

				// 查询条件对象
				BusGreportVo busGreportVo = new BusGreportVo();
				//年
				String year = request.getParameter("year");
				//月
				String imonth = request.getParameter("imonth");
				//业务类型编码
				String buscode = request.getParameter("buscode");
				if(year==null||"".equals(year)){
					year =timeYear;
				}

				// 短信类型
				busGreportVo.setMstype(Integer.parseInt(msType));
				//年
				busGreportVo.setY(year);
				//月份个数
				int size=12;
				//业务类型个数
				int mutcount=1;
				List<LfBusManager> busls=(List<LfBusManager>)request.getSession(GreportParamValue.GET_SESSION_FALSE).getAttribute("buslist");
				Map<String,	String> busmap=new HashMap<String, String>();
				//将业务编码 和业务名称组成键值对存入map
				if(busls!=null&&busls.size()>0){
					for(LfBusManager bus:busls){
						busmap.put(bus.getBusCode(), bus.getBusName());
					}
				}
				if(busls!=null){
					mutcount=busls.size();
				}
				//标题
				String titile="";
				//单位
				String unit="";
				//没选月
				if(imonth==null||"".equals(imonth)){
					titile=year+MessageUtils.extractMessage("cxtj", "cxtj_sjfx_chart_ytitle", request);
					unit=MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_m", request);
					// 月
					busGreportVo.setReporttype(0);
				}else{
					//选了月
					
					//titile=year+MessageUtils.extractMessage("cxtj", "cxtj_sjfx_tips21", request)+imonth+MessageUtils.extractMessage("cxtj", "cxtj_sjfx_chart_mtitle", request);
					//unit=MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_d", request);

					String language  = (String) request.getSession().getAttribute(StaticValue.LANG_KEY);
					if (StaticValue.ZH_HK.equals(language)) {
						titile=year+MessageUtils.extractMessage("cxtj", "cxtj_sjfx_tips21", request)+getMonth(imonth)+MessageUtils.extractMessage("cxtj", "cxtj_sjfx_chart_mtitle", request);
					}else{
						titile=year+MessageUtils.extractMessage("cxtj", "cxtj_sjfx_tips21", request)+imonth+MessageUtils.extractMessage("cxtj", "cxtj_sjfx_chart_mtitle", request);
						unit=MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_d", request);
					}
					
					busGreportVo.setImonth(imonth);
					busGreportVo.setReporttype(1);
					//获取年份
					Calendar time=Calendar.getInstance(); 
					time.clear(); 
					time.set(Calendar.YEAR,Integer.parseInt(year)); //year年
					time.set(Calendar.MONTH,Integer.parseInt(imonth)-1);//Calendar对象默认一月为0,month月        
					//月份的天数
					size=time.getActualMaximum(Calendar.DAY_OF_MONTH);
				}
				//业务类型编码串
				StringBuffer buscodes=new StringBuffer("");
				//业务类型编码list
				List<String> bcodelist=new ArrayList<String>();
				//解析业务类型字符串
				if(buscode!=null&&!"".equals(buscode)){
					String[] bus=buscode.split(",");
					//业务类型个数
					mutcount=bus.length;
					for(String bcode:bus){
						buscodes.append("'"+bcode+"',");
						bcodelist.add(bcode);
					}
					//去除尾部逗号
					buscodes.deleteCharAt(buscodes.lastIndexOf(","));
				}
				//设置业务类型条件
				busGreportVo.setSvrtype(buscodes.toString());
				//判断是否多企业
				if(StaticValue.getCORPTYPE() ==1){
					busGreportVo.setCorpCode(corpCode);
				}
				//获取结果集
				busgreportList = busGreportbiz.getBusGreportByVo(busGreportVo);
				//拼接json字符串
				StringBuffer busjoson = new StringBuffer("");
				busjoson.append("{\"credits\": {\"text\": \"\",\"href\": \"\"},");
				//标题
				busjoson.append("\"title\": {\"text\": \""+titile+"\",\"x\": -20},");
				busjoson.append("\"xAxis\": {\"categories\": [");
				int monthorday=0;
				//数据拼接
				while(monthorday<size)
				{
					monthorday++;
					//busjoson.append("\""+monthorday+unit+"\",");

					//设置横坐标 几月 或几日
					String language  = (String) request.getSession().getAttribute(StaticValue.LANG_KEY);
					if(StaticValue.ZH_HK.equals(language)&&(imonth == null || "".equals(imonth))){
						busjoson.append("\""+getMonth(""+monthorday)+"\",");
					}else{
						busjoson.append("\""+monthorday+unit+"\",");
					}
				}
				if(size!=0){
					busjoson.deleteCharAt(busjoson.lastIndexOf(","));
				}
				busjoson.append("]");
				busjoson.append("},");
				busjoson.append("\"yAxis\": {\"title\": {\"text\": \""+MessageUtils.extractMessage("cxtj", "cxtj_sjfx_chart_subtitle", request)+"\"},\"min\":0,");
				//样式设置
				busjoson.append("\"plotLines\": [{\"value\": 0,\"width\": 1,\"color\": \"#808080\"}]},");
				//格式显示
				busjoson.append("\"tooltip\": {\"valueSuffix\": \""+MessageUtils.extractMessage("cxtj", "cxtj_sjfx_chart_unit", request)+"\"},\"legend\": {\"layout\": \"vertical\",\"align\": \"right\",\"verticalAlign\": \"middle\",\"borderWidth\": 0},");
				busjoson.append("\"series\": [");
				int j = 0;
				for (int d = 0; d < mutcount; d++)
				{
					j++;
					int yormonth=0;
					String busstr="";
					//线条名称填充业务类型
					String linetext="";
					if(buscode!=null&&!"".equals(buscode)){
						busstr=bcodelist.get(d);
						//线条名称
						linetext=busmap.get(bcodelist.get(d));
					}else{
						linetext=busls.get(d).getBusName();
						busstr=busls.get(d).getBusCode();
					}
					//去除特殊字符
					linetext=linetext.replace("\"","&quot;").replace("<","&lt;").replace(">","&gt;");
				busjoson.append("{\"name\": \""+linetext+"\",\"data\": [");
				//循环填充数据
				for (int i = 0; i < size; i++)
				{
					yormonth++;
					//拼接mapkey值
					String keystr="";
					if(yormonth<10){
						keystr="0"+yormonth;
					}else{
						keystr=yormonth+"";
					}
					//key值设置
					keystr=busstr+keystr;
					//通过key值获取数据对象
					DynaBean tgvo=busgreportList.get(keystr);
					if(tgvo != null)
					{
						//格式化成功数
						String count = new DecimalFormat("#0.00").format((tgvo.get("icount") == null ? 0 : Long.parseLong(tgvo.get("icount").toString())));
						busjoson.append("" + count + ",");
					}
					else
					{
						busjoson.append("0,");
					}

				}
				//去除尾部逗号
				busjoson.deleteCharAt(busjoson.lastIndexOf(","));
				busjoson.append("]},");
			}
			//去除尾部逗号
			busjoson.deleteCharAt(busjoson.lastIndexOf(","));
			busjoson.append("]}");
			
			String opContent = MessageUtils.extractMessage("cxtj", "cxtj_sjfx_tips1", request)+starthms+","+MessageUtils.extractMessage("cxtj", "cxtj_sjfx_tips2", request)+":"+(System.currentTimeMillis()-startl)+"ms";
			setLog(request, MessageUtils.extractMessage("cxtj", "cxtj_sjfx_tips3", request), opContent, StaticValue.GET);
			
			//打印json格式字符串
			response.getWriter().print(busjoson.toString());
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取业务类型对比图形报表获取json异常");
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
