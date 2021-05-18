package com.montnets.emp.netnews.common.servlet;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.netnews.common.AllUtil;
import com.montnets.emp.netnews.common.CompressEncodeing;
import com.montnets.emp.netnews.common.FileJsp;
import com.montnets.emp.netnews.entity.LfWXPAGE;
import com.montnets.emp.netnews.entity.LfWxVisitLog;
import com.montnets.emp.netnews.util.wx_FileUtil;
import com.montnets.emp.security.wgmsgconfig.WgMsgConfigBiz;
import com.montnets.emp.util.PhoneUtil;
import com.montnets.emp.util.TxtFileUtil;

/**
 * @project montnets_emp
 * @author Vincent <vincent1219@21cn.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-11-8
 * @description 生成网讯Wap页面
 */
public class wx_getNetServlet extends HttpServlet
{

	String empRoot="ydwx";
	
	String basePath="/wap";
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{

		this.doPost(request, response);
	}
	/**
	 * 获得IP地址
	 * @description    
	 * @param request
	 * @return  地址信息    			 
	 * @datetime 2016-3-2 下午04:58:01
	 */
	public String getIpAddr(HttpServletRequest request)
	{
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
		{
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
		{
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
		{
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	
	/**
	 * 通过页面手机上的连接地址
	 * 跳转到要访问的页面
	 * @description    
	 * @param request
	 * @return  地址信息    			 
	 * @datetime 2016-3-2 下午04:58:01
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		String ipAddr = getIpAddr(request);
		Enumeration<String> headers = request.getHeaderNames();
		String userAgent = "";

		while (headers.hasMoreElements())
		{
			String head = headers.nextElement();
			if ("user-agent".equals(head))
			{
				userAgent = request.getHeader(head);
			}
		}

		String meno = "";
		String pageIDAndTaskID="";//处理pageID，TaskID的长度，为了给CompressEncodeing.UnJieM(w) 使用
		try
		{
			String w = request.getParameter("w");
			//判断下是否支持国际号码
			String phonerex="";//电话号码前面部分
			String phonerexjm="";
			if(w.indexOf("*0")>-1){//表示+开头的 （*0表示加号开头,*1表示00开头）
				phonerexjm=w.substring(w.indexOf("*0"));
				w=w.replace("*0", "");
				String numer="";
				if(phonerexjm!=null&&phonerexjm.length()>3){
					 numer=phonerexjm.substring(2, 3);
				}
				
				phonerex="+"+numer;//处理截取的数字
				pageIDAndTaskID=w.substring(w.length()-1, w.length());
				w=w.substring(0, w.length()-2);//去掉最后一位
			}else if(w.indexOf("*1")>-1){//表示+开头的
				phonerexjm=w.substring(w.indexOf("*1"));
				w=w.replace("*1", "");
				pageIDAndTaskID=w.substring(w.length()-1, w.length());//取最后一位
				w=w.substring(0, w.length()-1);//去掉最后一位
				phonerex="00";
			}
			if (w != null && !"".equals(w))
			{
				w = w.replace("${h}", "");
				if (w.indexOf("%5B") > -1)
				{
					w = w.substring(0, w.indexOf("%5B"));
				}
				if (w.indexOf("[") > -1)
				{
					w = w.substring(0, w.indexOf("["));
				}
				if (w.indexOf("?") > -1)
				{
					w = w.substring(0, w.indexOf("?"));
				}
				w = w.trim();
			}

			String p = "";
			String h = "";
			String t = "";
			String pagephone="";
			if (w != null)
			{
				Map<String, String> pdph ;
				//国际号码处理
				if("".equals(pageIDAndTaskID)){
					pdph=CompressEncodeing.UnJieMForSend(w);
				}else{
					pdph= CompressEncodeing.UnJieM(w,pageIDAndTaskID);
				}
				//一般号码处理

				if (pdph != null)
				{
					p = pdph.get("p");
					h = pdph.get("h");
					t = pdph.get("t");
					pagephone=h;
					h=phonerex+h;//加上前缀
					request.setAttribute("phone", h);
					request.setAttribute("pageId", p);
					request.setAttribute("taskId", t);
				}
				/*if (w.length() < 6)
				{
					p = CompressEncodeing.UnCompressNumber(w) + "";
				}*/
			}
			if (p == null || "".equals(p))
			{
				p = "0";
			}

			// 判断手机是否为空，如果为空，直接访问，不扣费
			if (h != null && !"".equals(h))
			{

				//boolean b = AllUtil.isMobileNO(h);
				//判断号码是否合法
				PhoneUtil util=new PhoneUtil();
				String[] haoduan = new WgMsgConfigBiz().getHaoduan();
				int re = util.getPhoneType(h, haoduan);
				//boolean area=util.isAreaCode(h);//增加是否是国际号码的验证
				int size=0;
				if(re!=-1&&!"0".equals(p)){
					//判断任务id是否存在
					BaseBiz basebiz=new BaseBiz();
					LinkedHashMap<String, String> conditionMap=new LinkedHashMap<String, String>();
					conditionMap.put("taskId", t);
					conditionMap.put("msType", "6");
					List<LfMttask> mts= basebiz.getByCondition(LfMttask.class, conditionMap, null);
					size=mts!=null?mts.size():0;
					//判断任务id和pageid是否同属一个网讯
					if(size>0){
						if(mts.get(0)!=null&&mts.get(0).getTempid()!=null&&!"0".equals(p)){
							try{
								LfWXPAGE wxpage=basebiz.getById(LfWXPAGE.class, p);
								if(wxpage!=null&&wxpage.getNETID()!=null){
									if(mts.get(0).getTempid().longValue()!=wxpage.getNETID().longValue()){
										size=0;
									}
								}else{
									size=0;
								}
							}catch(Exception e){
								size=0;
							}
						}else{
							size=0;
						}
					}
				}

				// 插入VisitLog
				LfWxVisitLog visi = new LfWxVisitLog();
				visi.setUserAgent("");
				if(ipAddr!=null&&!"".equals(ipAddr)){
					if(ipAddr.length()>12){
						ipAddr=ipAddr.substring(0, 11);
					}
				}
				visi.setIpAddr(ipAddr);

				/**
				 * 判断网讯文件是否存在
				 */
				
				visi.setVisitdate(Timestamp.valueOf(AllUtil.datetoString(new Date())));
				
				if(t != null && t.trim().length() != 0){
					String temp=t;
					if(temp.length()>19){
						temp=temp.substring(0, 18);
					}
					visi.setTaskid(Long.valueOf(temp));
				}
				
				String filename = "wx_" + p + ".jsp";
				String basePath = new TxtFileUtil().getWebRoot()
						+ com.montnets.emp.common.constant.StaticValue.WX_PAGE + "/" + filename;
				File file = new File(basePath);
				boolean isExit=false;
				if(file != null && file.exists()){
					isExit=true;
				}else {
					//分布式 ：如果存在就下载该文件到本地服务器上
					if(com.montnets.emp.common.constant.StaticValue.getISCLUSTER() ==1){
							//获取JSP相对路径
							String strRelativeSrc = "file/wx/PAGE/wx_" + p + ".jsp";
							CommonBiz biz=new CommonBiz();
							String result=biz.downloadFileFromFileCenter(strRelativeSrc);
							if("success".equals(result)){
								isExit=true;
							}
						}
				}

				if (isExit)
				{ // 判断网讯文件是否存在

					// ----------------------------------------------
					boolean boo = true;
					if (p.length() < 8)
					{
						boo = FileJsp.isBeOverdue(Integer.parseInt(p)); // 判断网讯是否过期
					}
					if (boo)
					{
						meno = "访问网讯已过期,或访问参数有误";
						request.setAttribute("meno", meno);

						//跳转到异常页
						this.goErrorPage(request, response);
						return;

					}
					// -----------------------------------------------
					// 判断访问同一手机访问同一页面次数
					boolean nv = FileJsp.phoneVisitMaxCount(h, p);
					if (nv)
					{
						meno = "超出每日最大访问次数，请明天再访问！！！";
						request.setAttribute("meno", meno);
						
						//跳转到异常页
						this.goErrorPage(request, response);
						return;
					}
					// }

					request.setAttribute("p", CompressEncodeing.CompressNumber(
							Long.valueOf(p), 6)); // URL地址用
					//----
					request.setAttribute("h", CompressEncodeing.JieMPhone(pagephone)+phonerexjm);
					request.setAttribute("pd", p); // 传的参数 不加密
					request.setAttribute("ph", h);
					request.setAttribute("t", CompressEncodeing.CompressNumber(Long.valueOf(t), 6));
					

					// 判断手机号是否存在，如果不存目前只有查看，所以下面的扣费操作。
					if (h == null || "".equals(h))
					{
						//跳转访问网讯页面
						toVisit(p, request, response);
						return;
					}
					// ------------------------------------------------
					
					//netid
					String  pval=p;
					if(pval!=null&&!"".equals(pval)){
						if(pval.length()>19){
							pval=pval.substring(0, 18);
						}
					}
					visi.setRefid(Long.parseLong(pval));
					
					String hval=h;
					if(hval!=null&&!"".equals(hval)){
						if(hval.length()>21){
							hval=hval.substring(0, 21);
						}
					}
					visi.setPhone(hval);
					visi.setVisitstatus(0);
					meno = "wx_" + p + ".jsp";
					if(meno!=null&&!"".equals(meno)){
						if(meno.length()>36){
							meno=meno.substring(0, 35);
						}
					}
					visi.setMemo(meno);
					if ((re==-1) || "0".equals(p)||size==0)
					{
						
					}else{
						FileJsp.inListLogs(visi);
					}
					
					//跳转访问网讯页面
					toVisit(p, request, response);
					return;
				} 
				else
				{
					visi.setVisitstatus(1);
					//数据库中存的是字符  
					meno = "此网讯：wx_" + p + "不存在！";
					if(meno!=null&&!"".equals(meno)){
						if(meno.length()>36){
							meno=meno.substring(0, 35);
						}
					}
					visi.setMemo(meno);
					request.setAttribute("meno", meno);
					//netid
					String  pval=p;
					if(pval!=null&&!"".equals(pval)){
						if(pval.length()>19){
							pval=pval.substring(0, 18);
						}
					}
					String hval=h;
					if(hval!=null&&!"".equals(hval)){
						if(hval.length()>30){
							hval=hval.substring(0, 29);
						}
					}
					visi.setRefid(Long.parseLong(pval));
					visi.setPhone(hval);
					FileJsp.inListLogs(visi);
					
					//跳转到异常页
					this.goErrorPage(request, response);
					return;
				}
			} else
			{
				//跳转访问网讯页面
				toVisit(p, request, response);
			}
		}
		catch (Exception e)
		{
			meno = "访问网讯失败！";
			EmpExecutionContext.error(e,"访问网讯");
		}
	}
	
	
	/**
	 * 跳转到访问页面
	 * @description    
	 * @param p
	 * @param request
	 * @param response       			 
	 * @author zhangsan <zhangsan@126.com>
	 * @datetime 2016-3-2 下午04:59:18
	 */
	private void toVisit(String p, HttpServletRequest request, HttpServletResponse response){
		try{
			String filename = "wx_" + p + ".jsp";
			String basePath = "/" + com.montnets.emp.common.constant.StaticValue.WX_PAGE + "/" + filename;
			String filepath=new TxtFileUtil().getWebRoot()+basePath;
			//*****处理文件地址切换的问题
			wx_FileUtil obj = new wx_FileUtil();
			File file=new File(filepath);
			if(file.exists()){
				String conetxt=obj.read(filepath);
				obj.write(filepath, conetxt);
			}
			request.getRequestDispatcher(basePath).forward(
					request, response);
		}catch(Exception e)
		{
			EmpExecutionContext.error(e,"跳转页面");
		}
	}
	
	/**
	 * 跳转到手机端异常页
	 * @param request
	 * @param response
	 */
	private void goErrorPage(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			//增加了新的显示页面 may 
			request.getRequestDispatcher(this.empRoot + this.basePath +"/404.jsp")
			.forward(request, response);
		}
		catch(Exception e)
		{
			EmpExecutionContext.error(e,"手机访问网讯跳转");
		}
		
	}

}
