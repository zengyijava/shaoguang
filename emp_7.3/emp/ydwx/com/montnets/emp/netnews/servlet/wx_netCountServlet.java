package com.montnets.emp.netnews.servlet;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.netnews.biz.WxNetCountBiz;
import com.montnets.emp.netnews.dao.Wx_netVisitDao;
import com.montnets.emp.netnews.daoImpl.Wx_netCountDaoImpl;
import com.montnets.emp.netnews.entity.LfWXBASEINFO;
import com.montnets.emp.netnews.entity.LfWXData;
import com.montnets.emp.netnews.entity.LfWXDataChos;
import com.montnets.emp.netnews.vo.TRUSTDATAvo;
import com.montnets.emp.netnews.vo.WxDataBlVo;
import com.montnets.emp.netnews.vo.WxDataVo;
import com.montnets.emp.util.DownloadFile;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.TxtFileUtil;
import jxl.Workbook;
import org.apache.commons.beanutils.DynaBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @project montnets_emp
 * @author Vincent <vincent1219@21cn.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-11-4
 * @description 网讯统计
 */

@SuppressWarnings("serial")
public class wx_netCountServlet extends BaseServlet
{
	private final Wx_netCountDaoImpl	ncdao	= new Wx_netCountDaoImpl();

	private final String				empRoot	= "ydwx";

	private final BaseBiz		baseBiz	= new BaseBiz();

	public void find(HttpServletRequest request, HttpServletResponse response)
	{
		// 登录操作员id
		//String lguserid = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String lguserid = SysuserUtil.strLguserid(request);

		// 企业编码
		String lgcorpcode = request.getParameter("lgcorpcode");
		String skip=request.getParameter("skip");
		PageInfo pageInfo = new PageInfo();
		Wx_netVisitDao visitDao = new Wx_netVisitDao();
		//日志开始时间
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		long begin_time=System.currentTimeMillis();
		boolean isFirstEnter;
		try
		{
			isFirstEnter = pageSet(pageInfo, request);
			
			String busid = "";
			String busname = "";
			String title = "";
			String hudtype = "";
			String hudcode = "";
			String hudname = "";
			String serchuserid = "";
			String begintime = "";
			String endtime = "";
			// 判断是否第一次访问
			if(!isFirstEnter)
			{
				// 网讯编号
				busid = request.getParameter("busid");
				// 网讯名称
				busname = request.getParameter("busname");
				// 网讯主题
				title = request.getParameter("title");
				// 互动类别
				hudtype = request.getParameter("hudtype");
				// 互动编码
				hudcode = request.getParameter("hudcode");
				// 互动名称
				hudname = request.getParameter("hudname");
				// 查询条件操作员
				serchuserid = request.getParameter("douserd");
				// 时间查询条件
				begintime = request.getParameter("sendtime");
				endtime = request.getParameter("recvtime");
			}
			if("true".equals(skip)){
				pageInfo=(PageInfo)request.getSession(false).getAttribute("netCountpageInfo");
				busid=(String)request.getSession(false).getAttribute("busid");
				busname=(String)request.getSession(false).getAttribute("busname");
				title=(String)request.getSession(false).getAttribute("title");
				hudtype=(String)request.getSession(false).getAttribute("hudtype");
				hudcode=(String)request.getSession(false).getAttribute("hudcode");
				hudname=(String)request.getSession(false).getAttribute("hudname");
				serchuserid=(String)request.getSession(false).getAttribute("serchuserid");
				begintime=(String)request.getSession(false).getAttribute("begintime");
				endtime=(String)request.getSession(false).getAttribute("endtime");
			}
			// 查询回复统计主题list
			List<TRUSTDATAvo> list1 = ncdao.gethuMttaskes(busid, busname, title, hudtype, hudcode, hudname, serchuserid, begintime, endtime, lguserid, lgcorpcode, pageInfo);
			List<TRUSTDATAvo> list = new ArrayList<TRUSTDATAvo>();
			for (int i = 0; i < list1.size(); i++)
			{
				TRUSTDATAvo t = list1.get(i);
				// 接受号码数
				int fpepoplecount = visitDao.getVisitCount(t.getNETID(), t.getTaskid() != null ? t.getTaskid().toString() : "-1", null, 5);
				// 访问人数
				t.setScount(fpepoplecount);
				// 回复人数
				t.setPcount(ncdao.getReplyCount(t.getTABLENAME(), t.getColname(), null, 1, t.getTaskid()));
				// 回复次数
				t.setCount(ncdao.getReplyCount(t.getTABLENAME(), t.getColname(), null, 0, t.getTaskid()));
				list.add(t);
			}
			// 操作员列表
			List<LfSysuser> lisuser = visitDao.getSysusers(lguserid, lgcorpcode);
			// 获取互动类别list
			List<DynaBean> wdts = ncdao.getWxDatatype(lgcorpcode);

			request.getSession(false).setAttribute("busname", busname);
			request.getSession(false).setAttribute("title", title);
			request.getSession(false).setAttribute("hudcode", hudcode);
			request.getSession(false).setAttribute("hudname", hudname);
			
			request.getSession(false).setAttribute("busid", busid);
			request.getSession(false).setAttribute("hudtype", hudtype);
			request.getSession(false).setAttribute("begintime", begintime);
			
			request.getSession(false).setAttribute("endtime", endtime);
			request.getSession(false).setAttribute("serchuserid", serchuserid);
			
			request.getSession(false).setAttribute("netCountpageInfo", pageInfo);
			
			request.setAttribute("n_busname", busname);
			request.setAttribute("n_title", title);
			request.setAttribute("n_hudcode", hudcode);
			request.setAttribute("n_hudname", hudname);
			request.setAttribute("n_busid", busid);
			request.setAttribute("n_hudtype", hudtype);
			request.setAttribute("n_begintime", begintime);
			request.setAttribute("n_endtime", endtime);
			request.setAttribute("n_serchuserid", serchuserid);
			request.setAttribute("skip", skip);
			
			request.setAttribute("wdts", wdts);
			request.setAttribute("userlist", lisuser);
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("netReplyList", list);
			//增加查询日志
			if(pageInfo!=null){
				long end_time=System.currentTimeMillis();
				String opContent ="查询开始时间："+format.format(begin_time)+",耗时:"+(end_time-begin_time)+"ms，查询总数："+pageInfo.getTotalRec();
				setLog(request, "网讯统计回复", opContent, "GET");
			}
			request.getRequestDispatcher(this.empRoot + "/report/netReplyCount.jsp").forward(request, response);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "查询网讯统计回复失败!");
		}
	}

	/**
	 * 回复统计导出
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void getReplyExport(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String busid = "";
		String busname = "";
		String title = "";
		String hudtype = "";
		String hudcode = "";
		String hudname = "";
		String serchuserid = "";
		String begintime = "";
		String endtime = "";
		//日志开始时间
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		long begin_time=System.currentTimeMillis();
		
		Wx_netVisitDao visitDao = new Wx_netVisitDao();
		// 查询条件操作员
		//String lguserid = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String lguserid = SysuserUtil.strLguserid(request);


		// 企业编码
		String lgcorpcode = request.getParameter("lgcorpcode");
		PageInfo pageInfo = new PageInfo();
		
		try{
			// 网讯名称
			busname = (String) request.getSession(false).getAttribute("busname");
			// 网讯主题
			title = (String) request.getSession(false).getAttribute("title");
			// 互动编码
			hudcode = (String) request.getSession(false).getAttribute("hudcode");
			// 互动名称
			hudname = (String) request.getSession(false).getAttribute("hudname");
		}catch (Exception e) {
			EmpExecutionContext.error(e,"从Session取出信息出现异常！");
		}
		
		// 网讯编码
		busid = request.getParameter("busid");
		// 互动类别
		hudtype = request.getParameter("hudtype");
		// 查询条件操作员
		serchuserid = request.getParameter("douserd");
		// 时间查询条件
		begintime = request.getParameter("begintime");
		endtime = request.getParameter("endtime");
		pageInfo.setPageSize(500000);
		List<TRUSTDATAvo> list1 = ncdao.gethuMttaskes(busid, busname, title, hudtype, hudcode, hudname, serchuserid, begintime, endtime, lguserid, lgcorpcode, pageInfo);
		List<TRUSTDATAvo> list = new ArrayList<TRUSTDATAvo>();
		for (int i = 0; i < list1.size(); i++)
		{
			TRUSTDATAvo t = list1.get(i);
			// 发送条数
			int fpepoplecount = visitDao.getVisitCount(t.getNETID(), t.getTaskid() != null ? t.getTaskid().toString() : "-1", null, 5);
			// 发送人数
			t.setScount(fpepoplecount);
			// 回复人数
			t.setPcount(ncdao.getReplyCount(t.getTABLENAME(), t.getColname(), null, 1, t.getTaskid()));
			// 回复次数
			t.setCount(ncdao.getReplyCount(t.getTABLENAME(), t.getColname(), null, 0, t.getTaskid()));
			list.add(t);
		}
		try
		{
			// 封装到EXCEL中
			int row = 0;
			response.reset();
//			response.setContentType("application/vnd.ms-excel ");
//			response.addHeader("Content-Disposition", new String(("attachment; filename=回复统计报表.xls").getBytes("GBK"), "ISO-8859-1"));
			// 产生报表文件的存储路径
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			String voucherFilePath = new TxtFileUtil().getWebRoot()+"ydwx/report/file/" + File.separator + "download"+File.separator+"replyreport";
			String fileName=MessageUtils.extractMessage("ydwx","ydwx_jsp_out_42",request)+"_"+sdf.format(System.currentTimeMillis())+"_"+ StaticValue.getServerNumber() +".xls";
			File fileTemp = new File(voucherFilePath);
			//判断文件是否存在
			if(!fileTemp.exists()){
				fileTemp.mkdirs();
			}
			String filePath=voucherFilePath+File.separator+fileName;
			jxl.write.WritableWorkbook wwb = Workbook.createWorkbook(new File(filePath));
			jxl.write.WritableSheet ws = wwb.createSheet(MessageUtils.extractMessage("ydwx","ydwx_jsp_out_42",request), 0);
			for (int i = 0; i < list.size(); i++)
			{
				TRUSTDATAvo t = list.get(i);
				if(row == 0)
				{
					jxl.write.Label labelA = new jxl.write.Label(0, 0, MessageUtils.extractMessage("ydwx","ydwx_jsp_out_43",request));
					jxl.write.Label labelB = new jxl.write.Label(1, 0, MessageUtils.extractMessage("ydwx","ydwx_jsp_out_44",request));
					jxl.write.Label labelC = new jxl.write.Label(2, 0, MessageUtils.extractMessage("ydwx","ydwx_jsp_out_45",request));
					jxl.write.Label labelD = new jxl.write.Label(3, 0, MessageUtils.extractMessage("ydwx","ydwx_jsp_out_46",request));
					jxl.write.Label labelE = new jxl.write.Label(4, 0, MessageUtils.extractMessage("ydwx","ydwx_jsp_out_47",request));
					jxl.write.Label labelF = new jxl.write.Label(5, 0, MessageUtils.extractMessage("ydwx","ydwx_jsp_out_48",request));
					jxl.write.Label labelG = new jxl.write.Label(6, 0, MessageUtils.extractMessage("ydwx","ydwx_jsp_out_49",request));
					jxl.write.Label labelH = new jxl.write.Label(7, 0, MessageUtils.extractMessage("ydwx","ydwx_jsp_out_50",request));
					jxl.write.Label labelI = new jxl.write.Label(8, 0, MessageUtils.extractMessage("ydwx","ydwx_jsp_out_51",request));
					jxl.write.Label labelJ = new jxl.write.Label(9, 0, MessageUtils.extractMessage("ydwx","ydwx_jsp_out_52",request));
					jxl.write.Label labelK = new jxl.write.Label(10, 0, MessageUtils.extractMessage("ydwx","ydwx_jsp_out_53",request));
					jxl.write.Label labelL = new jxl.write.Label(11, 0, MessageUtils.extractMessage("ydwx","ydwx_jsp_out_54",request));
					jxl.write.Label labelM = new jxl.write.Label(12, 0, MessageUtils.extractMessage("ydwx","ydwx_jsp_out_55",request));
					jxl.write.Label labelN = new jxl.write.Label(13, 0, MessageUtils.extractMessage("ydwx","ydwx_jsp_out_56",request));
					ws.addCell(labelA);
					ws.addCell(labelB);
					ws.addCell(labelC);
					ws.addCell(labelD);
					ws.addCell(labelE);
					ws.addCell(labelF);
					ws.addCell(labelG);
					ws.addCell(labelH);
					ws.addCell(labelI);
					ws.addCell(labelJ);
					ws.addCell(labelK);
					ws.addCell(labelL);
					ws.addCell(labelM);
					ws.addCell(labelN);
				}
				jxl.write.Label labelA = new jxl.write.Label(0, row + 1, t.getUNAME());
				jxl.write.Label labelB = new jxl.write.Label(1, row + 1, t.getUDEP());
				jxl.write.Label labelC = new jxl.write.Label(2, row + 1, t.getTitle());
				jxl.write.Label labelD = new jxl.write.Label(3, row + 1, t.getNETID() == null ? "" : t.getNETID().toString());
				jxl.write.Label labelE = new jxl.write.Label(4, row + 1, t.getNETNAME());
				jxl.write.Label labelF = new jxl.write.Label(5, row + 1, t.getNETMSG() == null ? "" : t.getNETMSG().replaceAll("#[pP]_(\\d+)#", MessageUtils.extractMessage("ydwx","ydwx_jsp_out_32",request)));
				jxl.write.Label labelG = new jxl.write.Label(6, row + 1, t.getNAMETYPE());
				jxl.write.Label labelH = new jxl.write.Label(7, row + 1, t.getCODE());
				jxl.write.Label labelI = new jxl.write.Label(8, row + 1, t.getNAME());

				String sendedTime = "-";
				if(t.getReState() == 2)
				{// 审批不通过 （发送时间为空）
					sendedTime = "-";
				}
				else
					if(t.getSubState() == 3)
					{// 撤销任务（空）
						sendedTime = "-";
					}
					else
						if(t.getSendstate() == 5)
						{// 超时未发送（空）
							sendedTime = "-";
						}
						else
							if(t.getTimerStatus() == 0 && t.getReState() == -1)
							{// 未定时未审批（待审批）（空）
								sendedTime = "-";
							}
							else
								if(t.getTimerStatus() == 1)
								{// 定时了
									sendedTime = t.getSENDDATE() == null ? "-" : t.getSENDDATE().toString().substring(0, t.getSENDDATE().toString().lastIndexOf("."));
									if(t.getSendstate() == 0)
									{
										sendedTime = sendedTime + "("+MessageUtils.extractMessage("ydwx","ydwx_jsp_out_dingshizhong",request)+")";
									}
								}
								else
									if(t.getSendstate() == 1 || t.getSendstate() == 2)
									{// 发送成功或者发送失败
										sendedTime = t.getSENDDATE() == null ? "-" : t.getSENDDATE().toString().substring(0, t.getSENDDATE().toString().lastIndexOf("."));
									}
									else
									{
										sendedTime = t.getSENDDATE() == null ? "-" : t.getSENDDATE().toString().substring(0, t.getSENDDATE().toString().lastIndexOf("."));
										// 这里面的情况就是sendstate=4(发送中)
									}

				jxl.write.Label labelJ = new jxl.write.Label(9, row + 1, sendedTime);

				jxl.write.Label labelK = new jxl.write.Label(10, row + 1, String.valueOf(t.getSpcount()));
				jxl.write.Label labelL = new jxl.write.Label(11, row + 1, String.valueOf(t.getScount()));
				jxl.write.Label labelM = new jxl.write.Label(12, row + 1, String.valueOf(t.getPcount()));
				jxl.write.Label labelN = new jxl.write.Label(13, row + 1, String.valueOf(t.getCount()));
				ws.addCell(labelA);
				ws.addCell(labelB);
				ws.addCell(labelC);
				ws.addCell(labelD);
				ws.addCell(labelE);
				ws.addCell(labelF);
				ws.addCell(labelG);
				ws.addCell(labelH);
				ws.addCell(labelI);
				ws.addCell(labelJ);
				ws.addCell(labelK);
				ws.addCell(labelL);
				ws.addCell(labelM);
				ws.addCell(labelN);
				row++;
			}
			//增加查询日志
			if(list!=null){
				long end_time=System.currentTimeMillis();
				String opContent = "导出开始时间：" +format.format(begin_time)+",耗时:"+(end_time-begin_time)+"ms，导出"+ list.size() + "条成功 ";
				setLog(request, "网讯回复统计", opContent, StaticValue.GET);
			}
			wwb.write();
			wwb.close();
			PrintWriter out = response.getWriter();
			if(list!=null&&list.size()>0){
				request.getSession(false).setAttribute("getReplyExport", fileName+"@@"+filePath);
				out.print("true");
			}else{
				out.print("false");
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "导出回复统计异常!");
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
			  EmpExecutionContext.error(e, "excel文件导出异常!");
		}
	    }
	
	// 根据日期查询网讯页面访问信息
	public void getReplyQueList(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		// 网讯编号
		String busId = request.getParameter("busId");
		// 互动id
		String did = request.getParameter("did");
		// 电话号码
		String phone = request.getParameter("phone");
		// 姓名
		String name = request.getParameter("name");
		// 回复内容
		String serchcolname = request.getParameter("colname");
		// 回复开始时间
		String begintime = request.getParameter("sendtime");
		// 回复结束时间
		String endtime = request.getParameter("recvtime");
		// 任务id
		String taskid = request.getParameter("taskid");
		//日志开始时间
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		long begin_time=System.currentTimeMillis();
		PageInfo pageInfo = new PageInfo();
		try
		{
			pageSet(pageInfo, request);
			// 表名
			String tablename = "";
			// 列名
			String colname = "";
			if(busId != null && !"".equals(busId))
			{
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				conditionMap.put("NETID", busId);
				Object loginSysuserObj =  request.getSession(false).getAttribute("loginSysuser");
				if(loginSysuserObj!=null){
					LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
					String lgcorpcode=loginSysuser.getCorpCode();
					if(lgcorpcode!=null&&!"".equals(lgcorpcode)){
						conditionMap.put("CORPCODE", lgcorpcode);
					}
				}
				// 网讯list
				List<LfWXBASEINFO> wxbase = baseBiz.getByCondition(LfWXBASEINFO.class, conditionMap, null);
				if(wxbase != null && wxbase.size() > 0)
				{
					LfWXBASEINFO wx = wxbase.get(0);
					tablename = wx.getDataTableName();
				}
			}
			String wxquest = "";
			// 互动项
			if(did != null && !"".equals(did))
			{
				// 获取互动项类
				LfWXData wxda = baseBiz.getById(LfWXData.class, did);
				if(wxda != null)
				{
					colname = wxda.getColName();
					wxquest = wxda.getQuesContent();
				}
			}
			List<DynaBean> beans = ncdao.getReplyPageCountList(pageInfo, tablename, colname, phone, name, serchcolname, begintime, endtime, taskid);
			List<WxDataVo> wdvs = new ArrayList<WxDataVo>();
			StringBuffer phoneStr = new StringBuffer("");
			;
			for (int i = 0; i < beans.size(); i++)
			{
				DynaBean dyb = beans.get(i);
				WxDataVo wdv = new WxDataVo();
				// 号码
				wdv.setPhone(dyb.get("phone") != null ? dyb.get("phone").toString() : "");
				phoneStr.append("'").append(dyb.get("phone") != null ? dyb.get("phone").toString() : "").append("',");
				// wdv.setName(dyb.get("name")!=null?dyb.get("name").toString():"");
				// String
				// hfcontent=ncdao.getHfCountList(tablename,colname,wdv.getPhone());
				// 回复内容
				wdv.setHfcontent(dyb.get(colname.toLowerCase()) != null ? dyb.get(colname.toLowerCase()).toString() : "");
				// 回复时间
				wdv.setHfdate(dyb.get("date_time") != null ? dyb.get("date_time").toString() : "");
				wdvs.add(wdv);
			}
			if(phoneStr.indexOf(",") != -1)
			{
				// 拼接号码用来寻找客户与员工的姓名
				phoneStr.deleteCharAt(phoneStr.lastIndexOf(","));
			}
			WxNetCountBiz netbiz = new WxNetCountBiz();
			//获取企业编码
			String lgcorpcode=getCorpCode(request);
			// 通过号码获取人员和客户的map
			Map<String, String> mapphone = netbiz.getEmpCliMobileNames_V1(phoneStr.toString(), lgcorpcode);
			request.setAttribute("phoneMap", mapphone);
			request.getSession(false).setAttribute("name", name);
			request.getSession(false).setAttribute("serchcolname", serchcolname);
			request.setAttribute("wxquest", wxquest);
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("replyPageList", wdvs);
			//增加查询日志
			if(pageInfo!=null){
				long end_time=System.currentTimeMillis();
				String opContent ="查询开始时间："+format.format(begin_time)+",耗时:"+(end_time-begin_time)+"ms，查询总数："+pageInfo.getTotalRec();
				setLog(request, "网讯回复统计", opContent, "GET");
			}
			request.getRequestDispatcher(this.empRoot + "/report/replyPageCount.jsp").forward(request, response);

		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "网讯回复统计查询异常!");
		}

	}

	/**
	 * 详情导出
	 * 
	 * @param request
	 * @param response
	 */
	public void replyPagesExportOut(HttpServletRequest request, HttpServletResponse response)
	{
		// 网讯编号
		String busId = request.getParameter("busId");
		// 互动id
		String did = request.getParameter("did");
		// 电话号码
		String phone = request.getParameter("phone");

		// 回复开始时间
		String begintime = request.getParameter("begintime");
		// 回复结束时间
		String endtime = request.getParameter("endtime");
		// 任务id
		String taskid = request.getParameter("taskid");

		PageInfo pageInfo = new PageInfo();
		//日志开始时间
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		long begin_time=System.currentTimeMillis();
		PrintWriter out=null;
		try
		{
			// 姓名 由于设置session为false，防止产生异常，放到try代码块中了
			String name = (String) request.getSession(false).getAttribute("name");
			// 回复内容
			String serchcolname = (String) request.getSession(false).getAttribute("serchcolname");
			pageInfo.setPageSize(500000);
			String tablename = "";
			String colname = "";
			// 通过网讯编号获取网讯对象 初始化表名
			if(busId != null && !"".equals(busId))
			{
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				conditionMap.put("NETID", busId);
				Object loginSysuserObj =  request.getSession(false).getAttribute("loginSysuser");
				if(loginSysuserObj!=null){
					LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
					String lgcorpcode=loginSysuser.getCorpCode();
					if(lgcorpcode!=null&&!"".equals(lgcorpcode)){
						conditionMap.put("CORPCODE", lgcorpcode);
					}
				}
				List<LfWXBASEINFO> wxbase = baseBiz.getByCondition(LfWXBASEINFO.class, conditionMap, null);
				if(wxbase != null && wxbase.size() > 0)
				{
					LfWXBASEINFO wx = wxbase.get(0);
					tablename = wx.getDataTableName();
				}
			}
			String wxquest = "";
			// 互动项id
			if(did != null && !"".equals(did))
			{
				LfWXData wxda = baseBiz.getById(LfWXData.class, did);
				colname = wxda.getColName();
				wxquest = wxda.getQuesContent();
			}
			// 获取详情list
			List<DynaBean> beans = ncdao.getReplyPageCountList(pageInfo, tablename, colname, phone, name, serchcolname, begintime, endtime, taskid);
			List<WxDataVo> wdvs = new ArrayList<WxDataVo>();
			StringBuffer phoneStr = new StringBuffer("");
			;
			for (int i = 0; i < beans.size(); i++)
			{
				DynaBean dyb = beans.get(i);
				WxDataVo wdv = new WxDataVo();
				// 号码
				wdv.setPhone(dyb.get("phone") != null ? dyb.get("phone").toString() : "");
				phoneStr.append("'").append(dyb.get("phone") != null ? dyb.get("phone").toString() : "").append("',");
				// wdv.setName(dyb.get("name")!=null?dyb.get("name").toString():"");
				// String
				// hfcontent=ncdao.getHfCountList(tablename,colname,wdv.getPhone());
				wdv.setHfcontent(dyb.get(colname.toLowerCase()) != null ? dyb.get(colname.toLowerCase()).toString() : "");
				wdv.setHfdate(dyb.get("date_time") != null ? dyb.get("date_time").toString() : "");
				wdvs.add(wdv);
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
//			response.addHeader("Content-Disposition", new String(("attachment; filename=回复页面统计报表.xls").getBytes("GBK"), "ISO-8859-1"));
//			jxl.write.WritableWorkbook wwb = Workbook.createWorkbook(response.getOutputStream());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			String voucherFilePath = new TxtFileUtil().getWebRoot()+"ydwx/report/file/" + File.separator + "download"+File.separator+"replyreport";
			String fileName=MessageUtils.extractMessage("ydwx","ydwx_jsp_out_57",request)+"_"+sdf.format(System.currentTimeMillis())+"_"+ StaticValue.getServerNumber() +".xls";
			File fileTemp = new File(voucherFilePath);
			//判断文件是否存在
			if(!fileTemp.exists()){
				fileTemp.mkdirs();
			}
			String filePath=voucherFilePath+File.separator+fileName;
			jxl.write.WritableWorkbook wwb = Workbook.createWorkbook(new File(filePath));
			jxl.write.WritableSheet ws = wwb.createSheet(MessageUtils.extractMessage("ydwx","ydwx_jsp_out_57",request), 0);
			WxDataVo t = null;
			for (int i = 0; i < wdvs.size(); i++)
			{
				t = wdvs.get(i);
				if(row == 0)
				{
					jxl.write.Label labelA = new jxl.write.Label(0, 0, MessageUtils.extractMessage("ydwx","ydwx_jsp_out_58",request));
					jxl.write.Label labelB = new jxl.write.Label(1, 0, wxquest);
					jxl.write.Label labelC = new jxl.write.Label(2, 0, "");
					jxl.write.Label labelD = new jxl.write.Label(3, 0, "");

					ws.addCell(labelA);
					ws.addCell(labelB);
					ws.addCell(labelC);
					ws.addCell(labelD);
					jxl.write.Label labelE = new jxl.write.Label(0, 1, MessageUtils.extractMessage("ydwx","ydwx_jsp_out_59",request));
					jxl.write.Label labelF = new jxl.write.Label(1, 1, MessageUtils.extractMessage("ydwx","ydwx_jsp_out_60",request));
					jxl.write.Label labelG = new jxl.write.Label(2, 1, MessageUtils.extractMessage("ydwx","ydwx_jsp_out_61",request));
					jxl.write.Label labelH = new jxl.write.Label(3, 1, MessageUtils.extractMessage("ydwx","ydwx_jsp_out_62",request));

					ws.addCell(labelE);
					ws.addCell(labelF);
					ws.addCell(labelG);
					ws.addCell(labelH);
				}

				jxl.write.Label labelA = new jxl.write.Label(0, row + 2, t.getPhone());
				jxl.write.Label labelB = new jxl.write.Label(1, row + 2, mapphone.get(t.getPhone()));
				jxl.write.Label labelC = new jxl.write.Label(2, row + 2, t.getHfcontent());
				jxl.write.Label labelD = new jxl.write.Label(3, row + 2, t.getHfdate() != null ? t.getHfdate().substring(0, t.getHfdate().length() - 2) : "");

				ws.addCell(labelA);
				ws.addCell(labelB);
				ws.addCell(labelC);
				ws.addCell(labelD);

				row++;
			}
			if(wdvs!=null){
				long end_time=System.currentTimeMillis();
	//			String opContent = "导出网讯回复统计详情导：" + wdvs.size() + "条成功 ";
				String opContent ="导出开始时间："+format.format(begin_time)+",耗时:"+(end_time-begin_time)+"ms，查询总数："+wdvs.size();
				setLog(request, "回复统计详情", opContent, StaticValue.GET);
			}
			wwb.write();
			wwb.close();
			
			out = response.getWriter();
			if(wdvs!=null&&wdvs.size()>0){
				request.getSession(false).setAttribute("replyPagesExportOut",fileName+"@@"+filePath);
				out.print("true");
			}else{
				out.print("false");
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "导出网讯访问统计信息异常!");
			if(out!=null){
			out.print("false");
			}
		}
	}

	// 根据日期查询网讯页面访问信息
	public void getTjDetailQueList(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		try
		{
			// 网讯主题
			String title = "";

			String mtid = request.getParameter("mtid");
			// 发送时间
			String senddate = request.getParameter("senddate");
			// 接收人数
			String jspcount = request.getParameter("jspcount");
			// 网讯编号
			String busId = request.getParameter("busId");
			// 互动id
			String did = request.getParameter("did");

			// 表名
			String tablename = "";
			// 列名
			String colname = "";
			// 网讯名称
			String busname = "";
			// 互动名称
			String hdname = "";
			// 互动内容
			String hdcontent = "";
			
			//日志开始时间
			SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
			long begin_time=System.currentTimeMillis();
			Long taskid = null;
			// 通过任务表的mtid获取任务信息
			if(mtid != null && !"".equals(mtid))
			{
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				conditionMap.put("mtId", mtid);
				List<LfMttask> mttasks = baseBiz.getByCondition(LfMttask.class, conditionMap, null);
				if(mttasks != null && mttasks.size() > 0)
				{
					LfMttask mttask = mttasks.get(0);
					title = mttask.getTitle();
					taskid = mttask.getTaskId();
				}
			}
			// 通过网讯编号获取网讯对象
			if(busId != null && !"".equals(busId))
			{
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				conditionMap.put("NETID", busId);
				Object loginSysuserObj =  request.getSession(false).getAttribute("loginSysuser");
				if(loginSysuserObj!=null){
					LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
					String lgcorpcode=loginSysuser.getCorpCode();
					if(lgcorpcode!=null&&!"".equals(lgcorpcode)){
						conditionMap.put("CORPCODE", lgcorpcode);
					}
				}
				List<LfWXBASEINFO> wxbase = baseBiz.getByCondition(LfWXBASEINFO.class, conditionMap, null);
				if(wxbase != null && wxbase.size() > 0)
				{
					LfWXBASEINFO wx = wxbase.get(0);
					tablename = wx.getDataTableName();
					busname = wx.getNAME();
				}
			}
			List<LfWXDataChos> wxdcs = new ArrayList<LfWXDataChos>();
			// 通过互动项id获取互动项列表
			if(did != null && !"".equals(did))
			{
				LfWXData wxda = baseBiz.getById(LfWXData.class, did);
				if(wxda != null)
				{
					// 列名
					colname = wxda.getColName();
					// 互动名称
					hdname = wxda.getName();
					// 互动内容
					hdcontent = wxda.getQuesContent();
					LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
					conditionMap.put("dId", wxda.getDId() + "");
					wxdcs = baseBiz.getByCondition(LfWXDataChos.class, conditionMap, null);
				}
			}
			List<WxDataBlVo> wdvs = new ArrayList<WxDataBlVo>();
			// 回复总次数
			int htcount = ncdao.getReplyCount(tablename, colname, null, 0, taskid);
			// 回复总人数
			int htpcount = ncdao.getReplyCount(tablename, colname, null, 1, taskid);
			for (int i = 0; i < wxdcs.size(); i++)
			{
				LfWXDataChos dyb = wxdcs.get(i);
				WxDataBlVo wdv = new WxDataBlVo();
				wdv.setSname(dyb.getName());
				// 回复次数
				int hcount = ncdao.getReplyCount(tablename, colname, dyb.getName(), 2, taskid);
				// 回复人数
				int hpcount = ncdao.getReplyCount(tablename, colname, dyb.getName(), 3, taskid);
				// 回复次数
				wdv.setHcount(hcount);
				wdv.setHpcount(hpcount);
				wdv.setHbl(new DecimalFormat("#.0000").format(htcount == 0 ? 0f : hcount * 100f / htcount) + "%");
				wdv.setHpbl(new DecimalFormat("#.0000").format(htpcount == 0 ? 0f : hpcount * 100f / htpcount) + "%");
				wdvs.add(wdv);
			}
			// 网讯标题
			request.setAttribute("title", title);
			request.setAttribute("senddate", senddate);
			request.setAttribute("jspcount", jspcount);
			request.setAttribute("busname", busname);
			request.setAttribute("hdcontent", hdcontent);
			request.setAttribute("hdname", hdname);
			request.setAttribute("replyPageList", wdvs);
			//增加查询日志
			long end_time=System.currentTimeMillis();
			int record_total=0;
			if(wdvs!=null&&wdvs.size()>0){
				record_total=wdvs.size();
			}
			getCorpCode(request);
			String opContent ="查询开始时间："+format.format(begin_time)+",耗时:"+(end_time-begin_time)+"ms，查询总数："+record_total;
			setLog(request, "网讯回复统计", opContent, "GET");
			request.getRequestDispatcher(this.empRoot + "/report/netReplyDetailCount.jsp").forward(request, response);

		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "查询网讯回复详细异常!");
		}
	}

	/**
	 *网讯回复页面统计报表导出
	 * 
	 * @param request
	 * @param response
	 */
	public void getTjDQueExportOut(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			// 网讯编号
			String busId = request.getParameter("busId");
			// 互动id
			String did = request.getParameter("did");
			String mtid = request.getParameter("mtid");
			String tablename = "";
			String colname = "";
			//日志开始时间
			SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
			long begin_time=System.currentTimeMillis();
			Long taskid = null;
			if(mtid != null && !"".equals(mtid))
			{
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				conditionMap.put("mtId", mtid);
				List<LfMttask> mttasks = baseBiz.getByCondition(LfMttask.class, conditionMap, null);
				if(mttasks != null && mttasks.size() > 0)
				{
					LfMttask mttask = mttasks.get(0);
					taskid = mttask.getTaskId();
				}
			}

			if(busId != null && !"".equals(busId))
			{
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				conditionMap.put("NETID", busId);
				Object loginSysuserObj =  request.getSession(false).getAttribute("loginSysuser");
				if(loginSysuserObj!=null){
					LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
					String lgcorpcode=loginSysuser.getCorpCode();
					if(lgcorpcode!=null&&!"".equals(lgcorpcode)){
						conditionMap.put("CORPCODE", lgcorpcode);
					}
				}
				List<LfWXBASEINFO> wxbase = baseBiz.getByCondition(LfWXBASEINFO.class, conditionMap, null);
				if(wxbase != null && wxbase.size() > 0)
				{
					LfWXBASEINFO wx = wxbase.get(0);
					tablename = wx.getDataTableName();
				}
			}
			List<LfWXDataChos> wxdcs = new ArrayList<LfWXDataChos>();
			if(did != null && !"".equals(did))
			{
				LfWXData wxda = baseBiz.getById(LfWXData.class, did);
				colname = wxda.getColName();
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				conditionMap.put("dId", wxda.getDId() + "");
				wxdcs = baseBiz.getByCondition(LfWXDataChos.class, conditionMap, null);
			}
			List<WxDataBlVo> wdvs = new ArrayList<WxDataBlVo>();
			int htcount = ncdao.getReplyCount(tablename, colname, null, 0, taskid);
			int htpcount = ncdao.getReplyCount(tablename, colname, null, 1, taskid);
			for (int i = 0; i < wxdcs.size(); i++)
			{
				LfWXDataChos dyb = wxdcs.get(i);
				WxDataBlVo wdv = new WxDataBlVo();
				wdv.setSname(dyb.getName());
				int hcount = ncdao.getReplyCount(tablename, colname, dyb.getName(), 2, taskid);
				int hpcount = ncdao.getReplyCount(tablename, colname, dyb.getName(), 3, taskid);
				// 回复次数
				wdv.setHcount(hcount);
				wdv.setHpcount(hpcount);
				wdv.setHbl(new DecimalFormat("#.0000").format(htcount == 0 ? 0f : hcount * 100f / htcount) + "%");
				wdv.setHpbl(new DecimalFormat("#.0000").format(htpcount == 0 ? 0f : hpcount * 100f / htpcount) + "%");
				wdvs.add(wdv);
			}
			// 封装到EXCEL中
			int row = 0;
			response.reset();
//			response.setContentType("application/vnd.ms-excel ");
//			response.addHeader("Content-Disposition", new String(("attachment; filename=回复页面统计报表比例.xls").getBytes("GBK"), "ISO-8859-1"));
//			jxl.write.WritableWorkbook wwb = Workbook.createWorkbook(response.getOutputStream());
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			String voucherFilePath = new TxtFileUtil().getWebRoot()+"ydwx/report/file/" + File.separator + "download"+File.separator+"replyreport";
			String fileName=MessageUtils.extractMessage("ydwx","ydwx_jsp_out_63",request)+"_"+sdf.format(System.currentTimeMillis())+"_"+ StaticValue.getServerNumber() +".xls";
			File fileTemp = new File(voucherFilePath);
			//判断文件是否存在
			if(!fileTemp.exists()){
				fileTemp.mkdirs();
			}
			String filePath=voucherFilePath+File.separator+fileName;
			jxl.write.WritableWorkbook wwb = Workbook.createWorkbook(new File(filePath));
			jxl.write.WritableSheet ws = wwb.createSheet(MessageUtils.extractMessage("ydwx","ydwx_jsp_out_63",request), 0);
			WxDataBlVo t = null;
			for (int i = 0; i < wdvs.size(); i++)
			{
				t = wdvs.get(i);
				if(row == 1)
				{
					jxl.write.Label labelA = new jxl.write.Label(0, 0, MessageUtils.extractMessage("ydwx","ydwx_wxcxtj_hftj_xuanxiangs",request));
					jxl.write.Label labelB = new jxl.write.Label(1, 0, MessageUtils.extractMessage("ydwx","ydwx_wxcxtj_hftj_hfrshbl",request));
					jxl.write.Label labelC = new jxl.write.Label(2, 0, MessageUtils.extractMessage("ydwx","ydwx_wxcxtj_hftj_hfcshbl",request));
					ws.addCell(labelA);
					ws.addCell(labelB);
					ws.addCell(labelC);
				}
				jxl.write.Label labelA = new jxl.write.Label(0, row + 1, t.getSname());
				jxl.write.Label labelB = new jxl.write.Label(1, row + 1, t.getHpcount() + "(" + t.getHpbl() + ")");
				jxl.write.Label labelC = new jxl.write.Label(2, row + 1, t.getHcount() + "(" + t.getHbl() + ")");

				ws.addCell(labelA);
				ws.addCell(labelB);
				ws.addCell(labelC);
				row++;
			}
			//增加查询日志
			if(wdvs!=null){
				long end_time=System.currentTimeMillis();
				String opContent ="导出开始时间："+format.format(begin_time)+",耗时:"+(end_time-begin_time)+"ms，导出总数："+ wdvs.size()+ "条";
				setLog(request, "网讯回复统计详情", opContent, StaticValue.GET);
			}
			wwb.write();
			wwb.close();
			
			PrintWriter out = response.getWriter();
			if(wdvs!=null&&wdvs.size()>0){
				request.getSession(false).setAttribute("getTjDQueExportOut", fileName+"@@"+filePath);
				out.print("true");
			}else{
				out.print("false");
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "导出网讯回复统计失败!");
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
