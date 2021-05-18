/**
 * 
 */
package com.montnets.emp.netnews.servlet;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.netnews.biz.WxNetCountBiz;
import com.montnets.emp.netnews.dao.Wx_netVisitDao;
import com.montnets.emp.netnews.entity.LfWXBASEINFO;
import com.montnets.emp.netnews.entity.LfWXPAGE;
import com.montnets.emp.netnews.vo.PageNameVo;
import com.montnets.emp.netnews.vo.VisitDATAvo;
import com.montnets.emp.netnews.vo.VisitTrustVo;
import com.montnets.emp.util.DownloadFile;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.TxtFileUtil;
import jxl.Workbook;
import org.apache.commons.beanutils.DynaBean;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author Administrator
 */
public class wx_netVisitServlet extends BaseServlet
{

	private final Wx_netVisitDao	visitDao	= new Wx_netVisitDao();

	private final String					empRoot		= "ydwx";

	private static final Logger		logger		= Logger.getLogger("wx_netVisitServlet");

	private final BaseBiz					baseBiz		= new BaseBiz();
	
	/**
	 * 网讯访问统计查询
	 * @description    
	 * @param request
	 * @param response       			 
	 * @author zhangsan <zhangsan@126.com>
	 * @datetime 2016-3-23 下午02:57:27
	 */
	public void find(HttpServletRequest request, HttpServletResponse response)
	{
		// 登录id
		//String lguserid = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String lguserid = SysuserUtil.strLguserid(request);

		// 企业编码
		String lgcorpcode = request.getParameter("lgcorpcode");
		//日志开始时间
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		long begin_time=System.currentTimeMillis();
		
		PageInfo pageInfo = new PageInfo();
		boolean isFirstEnter;
		try
		{
			// 网讯编号
			String busid = "";
			// 网讯名称
			String busname = "";
			// 查询条件操作员id
			String douserd = "";
			// 是否第一次访问
			isFirstEnter = pageSet(pageInfo, request);
			if(!isFirstEnter)
			{
				// 获取查询条件
				busid = request.getParameter("busid");
				busname = request.getParameter("busname");
				douserd = request.getParameter("douserd");
			}
			// 类型
			String type = request.getParameter("type");
			// 时间条件
			String begintime = request.getParameter("sendtime");
			String endtime = request.getParameter("recvtime");
			if(type == null)
			{
				type = "1";
			}
			List<VisitDATAvo> list = new ArrayList<VisitDATAvo>();
			// 访问次数
			int count;
			// 访问成功数
			int succcount;
			// 访问人数
			int phonecount;
			// 发送成功数
			long sendsucc;
			// 发送失败数
			long sendfail;
			// 网讯基本信息
			LfWXBASEINFO t = null;
			if("1".equals(type))
			{
				// 按网讯统计
				// 通过条件查询网讯list
				List<LfWXBASEINFO> datas = visitDao.getVisitDatas(busid, busname, lguserid, lgcorpcode, begintime, endtime, pageInfo);
				// 循环填充页面显示集合的对象
				for (int i = 0; i < datas.size(); i++)
				{
					VisitDATAvo date = new VisitDATAvo();
					t = datas.get(i);
					// 网讯id
					date.setID(t.getID());
					// 网讯编号
					date.setNETID(t.getNETID());
					// 网讯内容
					date.setNETMSG(t.getNAME());
					// 网讯名称
					date.setNETNAME(t.getNAME());
					// 创建日期
					date.setCREATDATE(t.getCREATDATE());
					// 有效时间
					date.setTIMEOUT(t.getTIMEOUT());
					// 访问次数
					count = visitDao.getVisitCount(t.getNETID(), null, null, 0);
					// 访问成功数
					succcount = visitDao.getVisitCount(t.getNETID(), null, null, 2);
					// 访问人数
					phonecount = visitDao.getVisitCount(t.getNETID(), null, null, 1);

					// 发送成功数
					sendsucc = visitDao.getVisitSendCount(t.getNETID(), 0);
					// 未反数
					sendfail = visitDao.getVisitSendCount(t.getNETID(), 1);
					// 成功+未反=接收号码数
					date.setVisitsendcount(sendsucc + sendfail);
					// 访问次数
					date.setVisitcount(count /* + vHistorycount */);
					// 访问人数
					date.setVisitpelple(phonecount /* + vHistoryPcount */);
					// 访问成功数
					date.setVisitsucc(succcount /* + vHistoryScccount */);
					// 访问失败数
					date.setVisitfail(count /* + vHistoryScccount */- succcount
					/*- vHistoryScccount*/);
					list.add(date);
				}

			}
			else
			{
				// 按批次统计
				List<VisitDATAvo> mttasks = visitDao.getMtTasks(busid, busname, douserd, lguserid, lgcorpcode, begintime, endtime, pageInfo);
				// 获取操作员查询条件
				List<LfSysuser> lisuser = visitDao.getSysusers(lguserid, lgcorpcode);
				request.setAttribute("userlist", lisuser);
				for (int i = 0; i < mttasks.size(); i++)
				{
					VisitDATAvo date = mttasks.get(i);
					// 访问次数
					count = visitDao.getVisitCount(date.getNETID(), date.getTaskid() != null ? date.getTaskid().toString() : "-1", null, 3);
					// 访问成功数
					succcount = visitDao.getVisitCount(date.getNETID(), date.getTaskid() != null ? date.getTaskid().toString() : "-1", null, 4);
					// 访问人数
					phonecount = visitDao.getVisitCount(date.getNETID(), date.getTaskid() != null ? date.getTaskid().toString() : "-1", null, 5);
					// 访问次数
					date.setVisitcount(count);
					// 访问人数
					date.setVisitpelple(phonecount);
					// 访问成功数
					date.setVisitsucc(succcount);
					// 访问失败数
					date.setVisitfail(count - succcount);
					list.add(date);
				}
			}
			// 存储网讯名称查询条件
			request.getSession(false).setAttribute("busname", busname);
			request.setAttribute("netVisitList", list);
			request.setAttribute("pageInfo", pageInfo);
			//增加查询日志
			if(pageInfo!=null){
				long end_time=System.currentTimeMillis();
				String opContent ="查询开始时间："+format.format(begin_time)+",耗时:"+(end_time-begin_time)+"ms，查询总数："+pageInfo.getTotalRec();
				String show_type="";
				if("1".equals(type))
				{
					show_type="(按网讯统计)";
				}else{
					show_type="(按批次统计)";
				}
				setLog(request, "网讯访问统计"+show_type, opContent, "GET");
			}
			// 根据类型跳转
			if("1".equals(type))
			{
				request.getRequestDispatcher(this.empRoot + "/report/netVisitCount.jsp").forward(request, response);
			}
			else
			{
				request.getRequestDispatcher(this.empRoot + "/report/netVisitCount2.jsp").forward(request, response);
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "网讯访问统计查询失败");
		}
	}

	/***
	 * 导出网讯访问统计报表
	 * @description    
	 * @param request
	 * @param response
	 * @throws Exception       			 
	 * @author zhangsan <zhangsan@126.com>
	 * @datetime 2016-3-23 下午02:56:13
	 */
	public void getvisitExport(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		PageInfo pageInfo = new PageInfo();
		try
		{
			// 登录操作员id
			//String lguserid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);


			// 企业编码
			String lgcorpcode = request.getParameter("lgcorpcode");
			SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
			long begin_time=System.currentTimeMillis();
			// 导出全部设置每页五十万
			pageInfo.setPageSize(500000);
			String busid = "";
			String busname = "";
			String douserd = "";
			// 网讯编码
			busid = request.getParameter("busid");
			// 网讯名称
			busname = (String) request.getSession(false).getAttribute("busname");
			// 查询条件操作员id
			douserd = request.getParameter("douserd");
			// 类型
			String type = request.getParameter("type");
			// 时间查询条件
			String begintime = request.getParameter("begintime");
			String endtime = request.getParameter("endtime");
			if(type == null)
			{
				type = "1";
			}
			// new一个页面显示数据的集合
			List<VisitDATAvo> list = new ArrayList<VisitDATAvo>();
			// 声明一个页面显示行数据的对象
			VisitDATAvo date = null;
			LfSysuser user;
			int count;
			int succcount;
			int phonecount;
			long sendsucc;
			long sendfail;
			LfWXBASEINFO t = null;
			if("1".equals(type))
			{
				// 按网讯统计
				List<LfWXBASEINFO> datas = visitDao.getVisitDatas(busid, busname, lguserid, lgcorpcode, begintime, endtime, pageInfo);
				// 循环填充对象
				for (int i = 0; i < datas.size(); i++)
				{
					date = new VisitDATAvo();
					t = datas.get(i);
					user = baseBiz.getLfSysuserByUserId(t.getCREATID());
					if(null != user)
					{
						date.setUNAME(user.getName());
					}
					else
					{
						date.setUNAME(String.valueOf(t.getCREATID()));
					}
					date.setID(t.getID());
					date.setNETID(t.getNETID());
					date.setNETMSG(t.getNAME());
					date.setNETNAME(t.getNAME());
					date.setCREATDATE(t.getCREATDATE());
					date.setTIMEOUT(t.getTIMEOUT());

					count = visitDao.getVisitCount(t.getNETID(), null, null, 0);
					succcount = visitDao.getVisitCount(t.getNETID(), null, null, 2);
					phonecount = visitDao.getVisitCount(t.getNETID(), null, null, 1);

					sendsucc = visitDao.getVisitSendCount(t.getNETID(), 0);
					sendfail = visitDao.getVisitSendCount(t.getNETID(), 1);

					date.setVisitsendcount(sendsucc + sendfail);
					date.setVisitcount(count /* + vHistorycount */);
					date.setVisitpelple(phonecount /* + vHistoryPcount */);
					date.setVisitsucc(succcount /* + vHistoryScccount */);
					date.setVisitfail(count /* + vHistoryScccount */- succcount
					/*- vHistoryScccount*/);
					list.add(date);
				}
			}
			else
			{
				// 按批次统计
				List<VisitDATAvo> mttasks = visitDao.getMtTasks(busid, busname, douserd, lguserid, lgcorpcode, begintime, endtime, pageInfo);
				List<LfSysuser> lisuser = visitDao.getSysusers(lguserid, lgcorpcode);
				request.setAttribute("userlist", lisuser);
				for (int i = 0; i < mttasks.size(); i++)
				{
					date = mttasks.get(i);
					// 访问次数
					count = visitDao.getVisitCount(date.getNETID(), date.getTaskid() != null ? date.getTaskid().toString() : "-1", null, 3);
					// 访问成功数
					succcount = visitDao.getVisitCount(date.getNETID(), date.getTaskid() != null ? date.getTaskid().toString() : "-1", null, 4);
					// 访问人数
					phonecount = visitDao.getVisitCount(date.getNETID(), date.getTaskid() != null ? date.getTaskid().toString() : "-1", null, 5);

					date.setVisitcount(count);
					date.setVisitpelple(phonecount);
					date.setVisitsucc(succcount);
					// 访问失败数
					date.setVisitfail(count - succcount);

					list.add(date);
				}
			}
			// 封装到EXCEL中
			int row = 0;
			response.reset();
//			response.setContentType("application/vnd.ms-excel ");
//			response.addHeader("Content-Disposition", new String(("attachment; filename=网讯访问统计报表.xls").getBytes("GBK"), "ISO-8859-1"));
			// 产生报表文件的存储路径
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			String voucherFilePath = new TxtFileUtil().getWebRoot()+"ydwx/report/file/" + File.separator + "download"+File.separator+"visitreport";
			String fileName="";
			if("1".equals(type))
			{
				/*网讯访问统计报表_按网讯统计*/
				fileName=MessageUtils.extractMessage("ydwx","ydwx_excel_1",request)+"_"+sdf.format(System.currentTimeMillis())+"_"+ StaticValue.getServerNumber() +".xls";
			}
			else
			{
				/*网讯访问统计报表_按批次统计*/
				fileName=MessageUtils.extractMessage("ydwx","ydwx_excel_2",request)+"_"+sdf.format(System.currentTimeMillis())+"_"+ StaticValue.getServerNumber() +".xls";
			}
			File fileTemp = new File(voucherFilePath);
			//判断文件是否存在
			if(!fileTemp.exists()){
				fileTemp.mkdirs();
			}
			String filePath=voucherFilePath+File.separator+fileName;
			jxl.write.WritableWorkbook wwb = Workbook.createWorkbook(new File(filePath));
			jxl.write.WritableSheet ws = null;
			if("1".equals(type))
			{
				/*网讯访问统计报表_按网讯统计*/
				ws = wwb.createSheet(MessageUtils.extractMessage("ydwx","ydwx_excel_1",request), 0);
			}
			else
			{
				/*网讯访问统计报表_按批次统计*/
				ws = wwb.createSheet(MessageUtils.extractMessage("ydwx","ydwx_excel_2",request), 0);
			}
			VisitDATAvo t1 = null;
			for (int i = 0; i < list.size(); i++)
			{
				t1 = list.get(i);
				if("1".equals(type))
				{
					if(row == 0)
					{
						/*网讯编号*/
						jxl.write.Label labelA = new jxl.write.Label(0, 0, MessageUtils.extractMessage("ydwx","ydwx_jsp_out_46",request));
						/*网讯名称*/
						jxl.write.Label labelB = new jxl.write.Label(1, 0, MessageUtils.extractMessage("ydwx","ydwx_jsp_out_47",request));
						/*创建时间*/
						jxl.write.Label labelC = new jxl.write.Label(2, 0, MessageUtils.extractMessage("ydwx","ydwx_common_time_chuangjianshijian",request));
						/*有效时间*/
						jxl.write.Label labelD = new jxl.write.Label(3, 0, MessageUtils.extractMessage("ydwx","ydwx_common_time_youxiaoshijian",request));
						/*接收人数*/
						jxl.write.Label labelE = new jxl.write.Label(4, 0, MessageUtils.extractMessage("ydwx","ydwx_wxcxtj_fwtj_jshrsh",request));
						/*访问次数*/
						jxl.write.Label labelF = new jxl.write.Label(5, 0, MessageUtils.extractMessage("ydwx","ydwx_wxcxtj_fwtj_fwcsh",request));
						/*访问成功数*/
//						jxl.write.Label labelG = new jxl.write.Label(6, 0, 访问成功数);
						/*访问失败数*/
//						jxl.write.Label labelH = new jxl.write.Label(7, 0, 访问失败数);
						/*访问人数*/
						jxl.write.Label labelI = new jxl.write.Label(6, 0, MessageUtils.extractMessage("ydwx","ydwx_wxcxtj_fwtj_fwrsh",request));
						ws.addCell(labelA);
						ws.addCell(labelB);
						ws.addCell(labelC);
						ws.addCell(labelD);
						ws.addCell(labelE);
						ws.addCell(labelF);
//						ws.addCell(labelG);
//						ws.addCell(labelH);
						ws.addCell(labelI);
					}
					jxl.write.Label labelA = new jxl.write.Label(0, row + 1, t1.getNETID() == null ? "" : t1.getNETID().toString());
					jxl.write.Label labelB = new jxl.write.Label(1, row + 1, t1.getNETNAME());
					jxl.write.Label labelC = new jxl.write.Label(2, row + 1, t1.getCREATDATE() == null ? "" : t1.getCREATDATE().toString().substring(0, t1.getCREATDATE().toString().lastIndexOf(".")));
					jxl.write.Label labelD = new jxl.write.Label(3, row + 1, t1.getTIMEOUT() == null ? "" : t1.getTIMEOUT().toString().substring(0, t1.getTIMEOUT().toString().lastIndexOf(".")));
					jxl.write.Label labelE = new jxl.write.Label(4, row + 1, t1.getVisitsendcount() + "");
//					jxl.write.Label labelF = new jxl.write.Label(5, row + 1, t1.getVisitsucc() + "");
//					jxl.write.Label labelG = new jxl.write.Label(6, row + 1, t1.getVisitfail() + "");
					jxl.write.Label labelH = new jxl.write.Label(5, row + 1, t1.getVisitcount() + "");
					jxl.write.Label labelI = new jxl.write.Label(6, row + 1,  t1.getVisitpelple()+ "");
					ws.addCell(labelA);
					ws.addCell(labelB);
					ws.addCell(labelC);
					ws.addCell(labelD);
					ws.addCell(labelE);
//					ws.addCell(labelF);
//					ws.addCell(labelG);
					ws.addCell(labelH);
					ws.addCell(labelI);
				}
				else
				{
					if(row == 0)
					{
						/*操作员*/
						jxl.write.Label labelA = new jxl.write.Label(0, 0, MessageUtils.extractMessage("ydwx","ydwx_wxcxtj_fstj_czy",request));
						/*隶属机构*/
						jxl.write.Label labelB = new jxl.write.Label(1, 0, MessageUtils.extractMessage("ydwx","ydwx_wxcxtj_fstj_lshjg",request));
						/*网讯编号*/
						jxl.write.Label labelC = new jxl.write.Label(2, 0, MessageUtils.extractMessage("ydwx","ydwx_jsp_out_46",request));
						/*网讯名称*/
						jxl.write.Label labelD = new jxl.write.Label(3, 0, MessageUtils.extractMessage("ydwx","ydwx_jsp_out_47",request));
						/*短信消息*/
						jxl.write.Label labelE = new jxl.write.Label(4, 0, MessageUtils.extractMessage("ydwx","ydwx_wxcxtj_fwtj_dxxx",request));
						/*发送时间*/
						jxl.write.Label labelF = new jxl.write.Label(5, 0, MessageUtils.extractMessage("ydwx","ydwx_common_time_fasongshijian",request));
						/*有效时间*/
						jxl.write.Label labelG = new jxl.write.Label(6, 0, MessageUtils.extractMessage("ydwx","ydwx_common_time_youxiaoshijian",request));
						/*访问次数*/
						jxl.write.Label labelH = new jxl.write.Label(7, 0, MessageUtils.extractMessage("ydwx","ydwx_wxcxtj_fwtj_fwcsh",request));
//						jxl.write.Label labelI = new jxl.write.Label(8, 0, "访问成功数");
//						jxl.write.Label labelJ = new jxl.write.Label(9, 0, "访问失败数");
						/*访问人数*/
						jxl.write.Label labelK = new jxl.write.Label(8, 0, MessageUtils.extractMessage("ydwx","ydwx_wxcxtj_fwtj_fwrsh",request));
						ws.addCell(labelA);
						ws.addCell(labelB);
						ws.addCell(labelC);
						ws.addCell(labelD);
						ws.addCell(labelE);
						ws.addCell(labelF);
						ws.addCell(labelG);
						ws.addCell(labelH);
//						ws.addCell(labelI);
//						ws.addCell(labelJ);
						ws.addCell(labelK);
					}
					jxl.write.Label labelA = new jxl.write.Label(0, row + 1, t1.getUNAME() == null ? "" : t1.getUNAME().toString());
					jxl.write.Label labelB = new jxl.write.Label(1, row + 1, t1.getUDEP() == null ? "" : t1.getUDEP().toString());
					jxl.write.Label labelC = new jxl.write.Label(2, row + 1, t1.getNETID() == null ? "" : t1.getNETID().toString());
					jxl.write.Label labelD = new jxl.write.Label(3, row + 1, t1.getNETNAME() == null ? "" : t1.getNETNAME());
					//当使用String中的replaceAll方法时，如果替换的值中包含有$符号时，在进行替换操作时会出现如下错误。
					//替换操作前后分别对替换值中的$符号进行encode和decode操作
					String replacement = MessageUtils.extractMessage("ydwx","ydwx_jsp_out_32",request);
					//replacement = replacement.replaceAll("\\$", "RDS_CHAR_DOLLAR");// encode replacement;
					String temp = t1.getNETMSG().replaceAll("#[pP]_(\\d+)#",replacement);
					//temp = temp.replaceAll("RDS_CHAR_DOLLAR", "\\$");// decode replacement;
					jxl.write.Label labelE = new jxl.write.Label(4, row + 1, t1.getNETMSG() == null ? "" : temp);

					String sendedTime = "-";
					if(t1.getReState() == 2)
					{// 审批不通过 （发送时间为空）
						sendedTime = "-";
					}
					else
						if(t1.getSubState() == 3)
						{// 撤销任务（空）
							sendedTime = "-";
						}
						else
							if(t1.getSendstate() == 5)
							{// 超时未发送（空）
								sendedTime = "-";
							}
							else
								if(t1.getTimerStatus() == 0 && t1.getReState() == -1)
								{// 未定时未审批（待审批）（空）
									sendedTime = "-";
								}
								else
									if(t1.getTimerStatus() == 1)
									{// 定时了
										sendedTime = t1.getCREATDATE() == null ? "-" : t1.getCREATDATE().toString().substring(0, t1.getCREATDATE().toString().lastIndexOf("."));
										if(t1.getSendstate() == 0)
										{
											sendedTime = sendedTime + "("+MessageUtils.extractMessage("ydwx","ydwx_jsp_out_dingshizhong",request)+")";
										}
									}
									else
										if(t1.getSendstate() == 1 || t1.getSendstate() == 2)
										{// 发送成功或者发送失败
											sendedTime = t1.getCREATDATE() == null ? "-" : t1.getCREATDATE().toString().substring(0, t1.getCREATDATE().toString().lastIndexOf("."));
										}
										else
										{
											sendedTime = t1.getCREATDATE() == null ? "-" : t1.getCREATDATE().toString().substring(0, t1.getCREATDATE().toString().lastIndexOf("."));
											// 这里面的情况就是sendstate=4(发送中)
										}

					jxl.write.Label labelF = new jxl.write.Label(5, row + 1, sendedTime);
					jxl.write.Label labelG = new jxl.write.Label(6, row + 1, t1.getTIMEOUT() == null ? "" : t1.getTIMEOUT().toString().substring(0, t1.getTIMEOUT().toString().lastIndexOf(".")));
//					jxl.write.Label labelH = new jxl.write.Label(7, row + 1, t1.getVisitsucc() + "");
//					jxl.write.Label labelI = new jxl.write.Label(8, row + 1, t1.getVisitfail() + "");
					jxl.write.Label labelJ = new jxl.write.Label(7, row + 1,t1.getVisitcount() + "");
					jxl.write.Label labelK = new jxl.write.Label(8, row + 1,t1.getVisitpelple()+ "");
					ws.addCell(labelA);
					ws.addCell(labelB);
					ws.addCell(labelC);
					ws.addCell(labelD);
					ws.addCell(labelE);
					ws.addCell(labelF);
					ws.addCell(labelG);
//					ws.addCell(labelH);
//					ws.addCell(labelI);
					ws.addCell(labelJ);
					ws.addCell(labelK);
				}
				row++;
			}
			//增加查询日志
			if(list!=null){
				long end_time=System.currentTimeMillis();
				String opContent ="导出开始时间："+format.format(begin_time)+",耗时:"+(end_time-begin_time)+"ms，导出总数："+ list.size()+ "条";
				setLog(request, "网讯访问统计", opContent, StaticValue.GET);
			}
			wwb.write();
			wwb.close();
			
			PrintWriter out = response.getWriter();
			if(list!=null&&list.size()>0){
				if("1".equals(type))
				{
					request.getSession(false).setAttribute("getvisitExport1", fileName+"@@"+filePath);
				}else{
					request.getSession(false).setAttribute("getvisitExport2", fileName+"@@"+filePath);	
				}

				out.print("true");
			}else{
				out.print("false");
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "网讯访问统计报表导出失败");
		}
	}
	
	/**
	 * excel文件导出
	 * @param request
	 * @param response
	 */
	   public void downloadFile(HttpServletRequest request, HttpServletResponse response)   {
		   try{
		   String down_session=request.getParameter("down_session");
		    HttpSession session = request.getSession(false);
		    Object obj = session.getAttribute(down_session);
	        if(obj != null){
	            String result = (String) obj;
	            if(result.indexOf("@@")>-1){
	            	String[] file=result.split("@@");
		            // 弹出下载页面。
		            DownloadFile dfs = new DownloadFile();
		            dfs.downFile(request, response, file[1], file[0]);
		            session.removeAttribute(down_session);
	            }
	        }
		   }catch (Exception e) {
			   EmpExecutionContext.error(e,"excel文件导出异常！");
		}
	    }
	

	/**
	 * 页面详情
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	// 根据日期查询网讯页面访问信息
	public void getVisitQueList(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		// 网讯编码
		String busId = request.getParameter("busId");
		// 任务id（批次id）
		String taskid = request.getParameter("taskid");
		// 类型 1为按网讯统计 2为按批次统计
		String type = request.getParameter("type");
		PageInfo pageInfo = new PageInfo();
		pageSet(pageInfo, request);
		try
		{
			// 判断类型是否为空默认为俺网讯统计
			if(type == null)
			{
				type = "1";
			}


			// 通过网讯编号获取页面list
			List<LfWXPAGE> list = visitDao.getWxPagesByNetId(busId, pageInfo);
			List<PageNameVo> reList = new ArrayList<PageNameVo>();
			// 判断页面list是否为空
			if(list != null && list.size() > 0)
			{
				for (LfWXPAGE lw : list)
				{
					String webPageName = lw.getNAME();
					if(webPageName.matches("Default web page|默认网讯页面|默認網訊頁面")){
						webPageName = MessageUtils.extractMessage("ydwx","ydwx_survey_7",request);
					}
					PageNameVo vo = new PageNameVo();
					vo.setId(lw.getID());
					vo.setPageName(webPageName);
					if("2".equals(type))
					{
						// 按批次统计
						// 访问次数
						int count = visitDao.getVisitCount(lw.getNETID(), taskid, lw.getID(), 3);
						// 访问人数
						int phonecount = visitDao.getVisitCount(lw.getNETID(), taskid, lw.getID(), 5);
						vo.setVisitcount(count /* + vHistorycount */);
						vo.setVisitpep(phonecount /* + vHistoryPcount */);
					}
					else
					{
						// 按网讯统计
						// 访问次数
						int count = visitDao.getVisitCount(lw.getNETID(), taskid, lw.getID(), 0);
						// 访问人数
						int phonecount = visitDao.getVisitCount(lw.getNETID(), taskid, lw.getID(), 1);
						vo.setVisitcount(count /* + vHistorycount */);
						vo.setVisitpep(phonecount /* + vHistoryPcount */);
					}
					reList.add(vo);
				}
			}
			getCorpCode(request);
			request.setAttribute("pageInfo", pageInfo);
			request.getSession(false).setAttribute("busId", busId);
			request.setAttribute("visitPageName", reList);
			request.getRequestDispatcher(this.empRoot + "/report/visitPageCount.jsp").forward(request, response);

		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "号码详情查询失败");
		}

	}

	/**
	 * 页面详情导出
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void visitPagesExportOut(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		// 网讯编码
		String busId = request.getParameter("busId");
		// 批次id
		String taskid = request.getParameter("taskid");
		// 类型
		String type = request.getParameter("type");
		
		//日志开始时间
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		long begin_time=System.currentTimeMillis();
		PageInfo pageInfo = new PageInfo();
		try
		{
			// 设置默认类型
			if(type == null)
			{
				type = "1";
			}
			pageInfo.setPageSize(500000);
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("NETID", busId);
			// 通过网讯编号查询页面列表
			List<LfWXPAGE> list = baseBiz.getByCondition(LfWXPAGE.class, conditionMap, null);
			List<PageNameVo> reList = new ArrayList<PageNameVo>();
			if(list != null && list.size() > 0)
			{
				for (LfWXPAGE lw : list)
				{
					PageNameVo vo = new PageNameVo();
					vo.setId(lw.getID());
					vo.setPageName(lw.getNAME());
					if("2".equals(type))
					{
						// 按批次统计
						// 访问次数
						int count = visitDao.getVisitCount(lw.getNETID(), taskid, lw.getID(), 3);
						// 访问人数
						int phonecount = visitDao.getVisitCount(lw.getNETID(), taskid, lw.getID(), 5);
						vo.setVisitcount(count /* + vHistorycount */);
						vo.setVisitpep(phonecount /* + vHistoryPcount */);
					}
					else
					{
						// 按网讯统计
						// 访问次数
						int count = visitDao.getVisitCount(lw.getNETID(), taskid, lw.getID(), 0);
						// 访问人数
						int phonecount = visitDao.getVisitCount(lw.getNETID(), taskid, lw.getID(), 1);
						vo.setVisitcount(count /* + vHistorycount */);
						vo.setVisitpep(phonecount /* + vHistoryPcount */);
					}
					reList.add(vo);
				}
			}
			// 封装到EXCEL中
			int row = 0;
			response.reset();
//			response.setContentType("application/vnd.ms-excel ");
//
//			response.addHeader("Content-Disposition", new String(("attachment; filename=页面访问统计报表.xls").getBytes("GBK"), "ISO-8859-1"));
//			jxl.write.WritableWorkbook wwb = Workbook.createWorkbook(response.getOutputStream());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			String voucherFilePath = new TxtFileUtil().getWebRoot()+"ydwx/report/file/" + File.separator + "download"+File.separator+"visitreport";
			String fileName=MessageUtils.extractMessage("ydwx","ydwx_jsp_out_67",request)+"_"+sdf.format(System.currentTimeMillis())+"_"+ StaticValue.getServerNumber() +".xls";
			File fileTemp = new File(voucherFilePath);
			//判断文件是否存在
			if(!fileTemp.exists()){
				fileTemp.mkdirs();
			}
			String filePath=voucherFilePath+File.separator+fileName;
			jxl.write.WritableWorkbook wwb = Workbook.createWorkbook(new File(filePath));
			jxl.write.WritableSheet ws = wwb.createSheet(MessageUtils.extractMessage("ydwx","ydwx_jsp_out_67",request), 0);
			PageNameVo t = null;
			for (int i = 0; i < reList.size(); i++)
			{
				t = reList.get(i);
				t.setPageName(t.getPageName().replaceAll("Default web page|默认网讯页面|默認網訊頁面",MessageUtils.extractMessage("ydwx","ydwx_survey_7",request)));
				if(row == 0)
				{
					jxl.write.Label labelA = new jxl.write.Label(0, 0, MessageUtils.extractMessage("ydwx","ydwx_jsp_out_68",request));
					jxl.write.Label labelB = new jxl.write.Label(1, 0, MessageUtils.extractMessage("ydwx","ydwx_jsp_out_54",request));
					jxl.write.Label labelC = new jxl.write.Label(2, 0, MessageUtils.extractMessage("ydwx","ydwx_jsp_out_69",request));
					ws.addCell(labelA);
					ws.addCell(labelB);
					ws.addCell(labelC);
				}
				jxl.write.Label labelA = new jxl.write.Label(0, row + 1, t.getPageName());
				jxl.write.Label labelB = new jxl.write.Label(1, row + 1, t.getVisitpep() + "");
				jxl.write.Label labelC = new jxl.write.Label(2, row + 1, t.getVisitcount() + "");
				ws.addCell(labelA);
				ws.addCell(labelB);
				ws.addCell(labelC);
				row++;
			}
			if(reList!=null){
				long end_time=System.currentTimeMillis();
				String opContent ="导出开始时间："+format.format(begin_time)+",耗时:"+(end_time-begin_time)+"ms，导出总数："+ reList.size()+ "条";
				setLog(request, "访问统计页面详情", opContent, StaticValue.GET);
			}
			wwb.write();
			wwb.close();
			
			PrintWriter out = response.getWriter();
			if(list!=null&&list.size()>0){
				request.getSession(false).setAttribute("visitPagesExportOut", fileName+"@@"+filePath);
				out.print("true");
			}else{
				out.print("false");
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "页面详情导出失败");
		}
	}

	/**
	 * 号码详情
	 * 
	 * @param request
	 * @param response
	 */
	public void visittrustView(HttpServletRequest request, HttpServletResponse response)
	{
		// 网讯编号
		String busId = request.getParameter("busId");
		//日志开始时间
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		long begin_time=System.currentTimeMillis();
		PageInfo pageInfo = new PageInfo();
		pageSet(pageInfo, request);
		try
		{
			// 姓名
			String name = request.getParameter("name");
			// 页面名称
			String pagename = request.getParameter("pagename");
			// 电话号码
			String phone = request.getParameter("phone");
			// 开始时间
			String begintime = request.getParameter("sendtime");
			// 结束时间
			String endtime = request.getParameter("recvtime");
			// 批次id
			String taskid = request.getParameter("taskid");
			List<DynaBean> beans = visitDao.getVisittrustBean(busId, name, phone, pagename, begintime, endtime, pageInfo, taskid);
			List<VisitTrustVo> reList = new ArrayList<VisitTrustVo>();
			StringBuffer phoneStr = new StringBuffer("");
			if(beans != null && beans.size() > 0)
			{
				for (DynaBean bean : beans)
				{
					VisitTrustVo vo = new VisitTrustVo();
					vo.setNetid(bean.get("netid"));
					vo.setPagename(bean.get("pagename"));
					vo.setPhone(bean.get("phone") != null ? bean.get("phone").toString() : "");
					phoneStr.append("'").append(bean.get("phone") != null ? bean.get("phone").toString() : "").append("',");
					vo.setNetName(bean.get("netname"));
					vo.setHistorytime(bean.get("visitdate"));
					reList.add(vo);
				}
			}
			if(phoneStr.indexOf(",") != -1)
			{
				phoneStr.deleteCharAt(phoneStr.lastIndexOf(","));
			}
			WxNetCountBiz netbiz = new WxNetCountBiz();
			//获取企业编码
			String lgcorpcode=getCorpCode(request);
			Map<String, String> mapphone = netbiz.getEmpCliMobileNames_V1(phoneStr.toString(), lgcorpcode);
			request.setAttribute("phoneMap", mapphone);
			request.getSession(false).setAttribute("sername", name);
			request.getSession(false).setAttribute("pagename", pagename);
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("busId", busId);
			request.setAttribute("visittrustView", reList);
			//增加查询日志
			if(pageInfo!=null){
				long end_time=System.currentTimeMillis();
				String opContent ="查询开始时间："+format.format(begin_time)+",耗时:"+(end_time-begin_time)+"ms，查询总数："+pageInfo.getTotalRec();
				setLog(request, "网讯访问号码详情", opContent, "GET");
			}
			request.getRequestDispatcher(this.empRoot + "/report/visitPagePhone.jsp").forward(request, response);
		}
		catch (ServletException e)
		{
			EmpExecutionContext.error(e, "查询失败");
		}
		catch (Exception e)
		{
			logger.info("动态bean取数据异常");
			EmpExecutionContext.error(e, "动态bean取数据异常");
		}
	}

	/**
	 * 号码详情导出
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void replyPagesExportOut(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String busId = request.getParameter("busId");
		PageInfo pageInfo = new PageInfo();
		//日志开始时间
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		long begin_time=System.currentTimeMillis();
		try
		{
			pageInfo.setPageSize(500000);
			String name = (String) request.getSession(false).getAttribute("sername");
			String pagename = (String) request.getSession(false).getAttribute("pagename");
			String phone = request.getParameter("phone");
			String begintime = request.getParameter("begintime");
			String endtime = request.getParameter("recvtime");
			String taskid = request.getParameter("taskid");
			List<DynaBean> beans = visitDao.getVisittrustBean(busId, name, phone, pagename, begintime, endtime, pageInfo, taskid);
			List<VisitTrustVo> reList = new ArrayList<VisitTrustVo>();
			StringBuffer phoneStr = new StringBuffer("");
			if(beans != null && beans.size() > 0)
			{
				for (DynaBean bean : beans)
				{
					VisitTrustVo vo = new VisitTrustVo();
					vo.setNetid(bean.get("netid"));
					vo.setPagename(bean.get("pagename"));
					vo.setPhone(bean.get("phone") != null ? bean.get("phone").toString() : "");
					phoneStr.append("'").append(bean.get("phone") != null ? bean.get("phone").toString() : "").append("',");
					vo.setNetName(bean.get("netname"));
					vo.setHistorytime(bean.get("visitdate"));
					reList.add(vo);
				}
			}
			if(phoneStr.indexOf(",") != -1)
			{
				phoneStr.deleteCharAt(phoneStr.lastIndexOf(","));
			}
			WxNetCountBiz netbiz = new WxNetCountBiz();
			//获取企业编码
			String lgcorpcode=getCorpCode(request);
			
			Map<String, String> mapphone = netbiz.getEmpCliMobileNames_V1(phoneStr.toString(), lgcorpcode);
			// 封装到EXCEL中
			int row = 0;
			response.reset();
//			response.setContentType("application/vnd.ms-excel ");
//			response.addHeader("Content-Disposition", new String(("attachment; filename=访问详情统计报表.xls").getBytes("GBK"), "ISO-8859-1"));
//			jxl.write.WritableWorkbook wwb = Workbook.createWorkbook(response.getOutputStream());
			// 产生报表文件的存储路径
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			String voucherFilePath = new TxtFileUtil().getWebRoot()+"ydwx/report/file/" + File.separator + "download"+File.separator+"visitreport";
			String fileName=MessageUtils.extractMessage("ydwx","ydwx_jsp_out_70",request)+"_"+sdf.format(System.currentTimeMillis())+"_"+ StaticValue.getServerNumber() +".xls";
			File fileTemp = new File(voucherFilePath);
			//判断文件是否存在
			if(!fileTemp.exists()){
				fileTemp.mkdirs();
			}
			String filePath=voucherFilePath+File.separator+fileName;
			jxl.write.WritableWorkbook wwb = Workbook.createWorkbook(new File(filePath));
			
			jxl.write.WritableSheet ws = wwb.createSheet(MessageUtils.extractMessage("ydwx","ydwx_jsp_out_70",request), 0);
			VisitTrustVo t = null;
			for (int i = 0; i < reList.size(); i++)
			{
				t = reList.get(i);
				if(row == 0)
				{
					jxl.write.Label labelA = new jxl.write.Label(0, 0, MessageUtils.extractMessage("ydwx","ydwx_jsp_out_71",request));
					jxl.write.Label labelB = new jxl.write.Label(1, 0, MessageUtils.extractMessage("ydwx","ydwx_jsp_out_59",request));
					jxl.write.Label labelC = new jxl.write.Label(2, 0, MessageUtils.extractMessage("ydwx","ydwx_jsp_out_60",request));
					jxl.write.Label labelD = new jxl.write.Label(3, 0, MessageUtils.extractMessage("ydwx","ydwx_jsp_out_72",request));
					jxl.write.Label labelE = new jxl.write.Label(4, 0, MessageUtils.extractMessage("ydwx","ydwx_jsp_out_73",request));
					jxl.write.Label labelF = new jxl.write.Label(5, 0, MessageUtils.extractMessage("ydwx","ydwx_jsp_out_68",request));

					ws.addCell(labelA);
					ws.addCell(labelB);
					ws.addCell(labelC);
					ws.addCell(labelD);
					ws.addCell(labelE);
					ws.addCell(labelF);
				}
				jxl.write.Label labelA = new jxl.write.Label(0, row + 1, t.getHistorytime() != null ? t.getHistorytime().toString().substring(0, t.getHistorytime().toString().lastIndexOf(".")) : "");
				jxl.write.Label labelB = new jxl.write.Label(1, row + 1, t.getPhone() != null ? t.getPhone() : "");
				jxl.write.Label labelC = new jxl.write.Label(2, row + 1, mapphone.get(t.getPhone() != null ? t.getPhone() : ""));
				jxl.write.Label labelD = new jxl.write.Label(3, row + 1, t.getNetid() != null ? t.getNetid().toString() : "");
				jxl.write.Label labelE = new jxl.write.Label(4, row + 1, t.getNetName() != null ? t.getNetName().toString() : "");
				jxl.write.Label labelF = new jxl.write.Label(5, row + 1, t.getPagename() != null ? t.getPagename().toString() : "");

				ws.addCell(labelA);
				ws.addCell(labelB);
				ws.addCell(labelC);
				ws.addCell(labelD);
				ws.addCell(labelE);
				ws.addCell(labelF);
				row++;
			}
			//增加查询日志
			if(reList!=null){
				long end_time=System.currentTimeMillis();
				String opContent ="导出开始时间："+format.format(begin_time)+",耗时:"+(end_time-begin_time)+"ms，导出总数："+ reList.size()+ "条";
				setLog(request, "网讯访问号码详情", opContent, StaticValue.GET);
			}
			wwb.write();
			wwb.close();
			
			PrintWriter out = response.getWriter();
			if(reList!=null&&reList.size()>0){
				request.getSession(false).setAttribute("replyPagesExportOut", fileName+"@@"+filePath);
				out.print("true");
			}else{
				out.print("false");
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "详情导出失败");
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
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				EmpExecutionContext.info(opModule, loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), opContent, opType);
			}
		}catch(Exception e)
		{
			EmpExecutionContext.error(e,opModule+opType+opContent+"日志写入异常");
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
					request.setAttribute("lguserid", loginSysuser.getUserId());
					request.setAttribute("lgcorpcode", loginSysuser.getCorpCode());
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
