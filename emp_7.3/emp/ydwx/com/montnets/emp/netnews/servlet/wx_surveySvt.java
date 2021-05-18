package com.montnets.emp.netnews.servlet;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.tools.SysuserUtil;
import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.netnews.biz.TrustDataBiz;
import com.montnets.emp.netnews.common.AllUtil;
import com.montnets.emp.netnews.common.StaticValue;
import com.montnets.emp.netnews.daoImpl.Wx_ueditorDaoImpl;
import com.montnets.emp.netnews.entity.LfWXBASEINFO;
import com.montnets.emp.netnews.entity.LfWXPAGE;
import com.montnets.emp.netnews.entity.LfWXTrustCols;
import com.montnets.emp.netnews.entity.LfWXTrustData;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.SuperOpLog;
import com.montnets.emp.wxmanger.dao.WxManagerDao;



/**
 * 
 * 
 * @project emp
 * @author 
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 
 * @description 
 */
public class wx_surveySvt extends BaseServlet {
	
	
	private final String opModule=StaticValue.SURVEY_MANAGER;
	private final String opSper = StaticValue.OPSPER;
	
	private static final String PATH = "/ydwx/wxsurvey";
	
	private final WxManagerDao wxMgDao = new WxManagerDao();
	
	private final Wx_ueditorDaoImpl ueditorDao = new Wx_ueditorDaoImpl();
	
	private final BaseBiz baseBiz = new BaseBiz();
	
	private final SuperOpLog spLog = new SuperOpLog();
	
	public void find(HttpServletRequest request, HttpServletResponse response) 
	{
		
		try 
		{
			//初始化分页对象
			PageInfo pageInfo = new PageInfo();
			pageSet(pageInfo,request);
			
			//当前登录操作员id
			//String userId = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String userId = SysuserUtil.strLguserid(request);
			//当前企业编码
			//String corpCode = request.getParameter("lgcorpcode");
			//问卷名称
			String name = request.getParameter("name");
			//创建人userId
			String creatid = request.getParameter("creatid");
			//创建时间，开始
			String creatdateBegin = request.getParameter("creatdateBegin");
			//创建时间，结束
			String creatdateEnd = request.getParameter("creatdateEnd");
			
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			
			if(name != null && !"".equals(name.trim()))
			{
				conditionMap.put("name", name);
			}
			if(creatid != null && !"".equals(creatid.trim()))
			{
				conditionMap.put("creatid", creatid);
			}
			if(creatdateBegin != null && !"".equals(creatdateBegin.trim()))
			{
				conditionMap.put("creatdateBegin", creatdateBegin);
			}
			if(creatdateEnd != null && !"".equals(creatdateEnd.trim()))
			{
				conditionMap.put("creatdateEnd", creatdateEnd);
			}
			
			//执行查询
			List<DynaBean> surveyList =wxMgDao.getSurvey(userId, conditionMap, pageInfo);

			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("surveyList", surveyList);
			request.setAttribute("conditionMap", conditionMap);
			
			String sresult = request.getParameter("sresult");
			if(sresult != null && !"".equals(sresult))
			{
				request.setAttribute("sresult", sresult);
			}
			
			request.getRequestDispatcher(PATH +"/wx_survey.jsp")
				.forward(request, response);
		} 
		catch (Exception e) {
			EmpExecutionContext.error(e, "查询异常!");
			
		}
		
	}
	
	/**
	 * 跳转到新增
	 * @param request
	 * @param response
	 */
	public void toAdd(HttpServletRequest request,HttpServletResponse response)
	{
		
		try
		{
			//String lguserid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);

			String lgcorpcode = request.getParameter("lgcorpcode");
			request.setAttribute("lguserid", lguserid);
			request.setAttribute("lgcorpcode", lgcorpcode);
			request.getRequestDispatcher(PATH +"/wx_addSurvey.jsp")
				.forward(request, response);
		} 
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "跳转到新增页面异常!");
		}
	}
	
	public void add(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			//String lguserid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);


			String lgcorpcode = request.getParameter("lgcorpcode");
			
			String trResult = this.trustSave(request, response);
			if(!"succ".equals(trResult))
			{
				response.sendRedirect("wx_survey.htm?method=find&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode+"&sresult="+trResult);
				return;
			}
			
			String ueResult = this.Ueditor(request, response);
			if(!"succ".equals(ueResult))
			{
				response.sendRedirect("wx_survey.htm?method=find&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode+"&sresult="+ueResult);
				return;
			}
			
			response.sendRedirect("wx_survey.htm?method=find&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode+"&sresult="+ueResult);
		}
		catch(Exception e)
		{
			EmpExecutionContext.error(e, "保存问卷信息异常!");
		}
		
	}

	/**
	 * 创建网讯
	 * 
	 * @param request
	 * @param response
	 */
	private String Ueditor(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			//String lguserid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);


			String lgcorpcode = request.getParameter("lgcorpcode");
			String wx_netid = request.getParameter("wx_netid");
			if (null != wx_netid && !"".equals(wx_netid))
			{
				getupdateByUeditor(request, response);
			}
			else
			{
				String saveAndList = request.getParameter("saveAndList");
				wx_netid = "0";
				String str[] = request.getParameterValues("checkboxzym");
				String wx_name = request.getParameter("wx_name");
				String wx_timeType = request.getParameter("wx_timeType");
				String wx_timeOut = request.getParameter("wx_timeOut");
				//String wx_STATUS = request.getParameter("wx_STATUS");
				String wx_share = request.getParameter("wx_share");
				if (null == wx_share || "".equals(wx_share))
				{
					wx_share = "0";
				}
				List<LfWXPAGE> pageList = new ArrayList<LfWXPAGE>();
				LfWXBASEINFO base = new LfWXBASEINFO();
				base.setCORPCODE(lgcorpcode);
				if (wx_timeType == null)
				{
					wx_timeType = "0";
				}
				base.setTIMETYPE(Integer.parseInt(wx_timeType));
				if ("0".equals(wx_timeType))
				{
					// 如果有效时间为一年
					SimpleDateFormat sdf = AllUtil.getFormatDateTime();
					Calendar cal = Calendar.getInstance();
					Date date = new Date();
					cal.setTime(date);
					cal.add(Calendar.YEAR, 1); // 当前时间加1年
					wx_timeOut = sdf.format(cal.getTime());

				}
				// 0草搞，1定搞
				if(saveAndList!=null)
				{
					base.setSTATUS(1);
				}else {
					base.setSTATUS(0);
				}
				if (wx_timeOut == null)
				{
					wx_timeOut = AllUtil.datetoString(new Date());
				}
				//base.setSTATUS(Integer.parseInt(wx_STATUS));
				String wx_sms = request.getParameter("wx_sms");
				base.setTIMEOUT(AllUtil.getTimeStamp(wx_timeOut));
				base.setCREATID(Long.parseLong(lguserid));
				base.setWxSHARE(Integer.parseInt(wx_share));
				base.setNAME(wx_name);
				base.setSMS(wx_sms);
				base.setCREATDATE(new Timestamp(System.currentTimeMillis()));
				//类型为问卷
				base.setWxTYPE(2);
				base.setSORT(0l);
				LfWXPAGE pagetemp = null;
				if (str != null && str.length > 0)
				{
					for (int i = 0; i < str.length; i++)
					{
						String[] temp = str[i].split("<#!!#>");
						LfWXPAGE page = new LfWXPAGE();
						page.setNAME(temp[0]);
						page.setPARENTID(Long.parseLong(temp[1]));
						page.setCONTENT(temp[2]);
						page.setLink(temp[4]);
						page.setCREATDATE(base.getCREATDATE());
						if (temp[1].equals("0"))
						{
							// 子结点为-1 ，父结点为0
							pagetemp = page;
						} 
						else
						{
							pageList.add(page);
						}
					}
					wx_netid = ueditorDao.getSaveUdeitor(base, pagetemp, pageList) + "";
				}
			}
			return "succ";
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "创建网讯信息异常!");
			return "error";
		}
	}

	/**
	 * 根据netid获取网讯
	 * @param NETID
	 * @return 返回网讯对象
	 */
	private LfWXBASEINFO getWxByNetId(String NETID)
	{
		LfWXBASEINFO bsseInfo = null;
		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("NETID", NETID);
			List<LfWXBASEINFO> bsseInfosList = baseBiz.findListByCondition(LfWXBASEINFO.class, conditionMap, null);
			if(bsseInfosList != null && bsseInfosList.size() > 0)
			{
				bsseInfo = bsseInfosList.get(0);
			}
			return bsseInfo;
		}
		catch(Exception e)
		{
			EmpExecutionContext.error(e, "根据netid获取网讯异常!");
			return null;
		}
	}
	
	/**
	 * 修改网讯
	 * 
	 * @param request
	 * @param response
	 */
	public void getupdateByUeditor(HttpServletRequest request,
			HttpServletResponse response)
	{
		try
		{
			//String lguserid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);



			String saveAndList = request.getParameter("saveAndList");
			String str[] = request.getParameterValues("checkboxzym");
			String wx_name = request.getParameter("wx_name");
			String wx_timeType = request.getParameter("wx_timeType");
			String wx_timeOut = request.getParameter("wx_timeOut");
			String wx_sms = request.getParameter("wx_sms");
			//String wx_STATUS = request.getParameter("wx_STATUS");
			//String wx_share = request.getParameter("wx_share");
			List<LfWXPAGE> pageList = new ArrayList<LfWXPAGE>();
			
			String wx_netid = request.getParameter("wx_netid");
			LfWXBASEINFO base = this.getWxByNetId(wx_netid);
			if(base == null)
			{
				return;
			}
			
			//base.setNETID(Long.parseLong(wx_netid));
			base.setTIMETYPE(Integer.parseInt(wx_timeType));
			if ("0".equals(wx_timeType))
			{ // 如果有效时间为一年
				SimpleDateFormat sdf = AllUtil.getFormatDateTime();
				Calendar cal = Calendar.getInstance();
				Date date = new Date();
				cal.setTime(date);
				cal.add(cal.YEAR, 1); // 当前时间加1年
				wx_timeOut = sdf.format(cal.getTime());
			}
			if (wx_timeOut == null || "".equals(wx_timeOut.trim()))
			{
				wx_timeOut = AllUtil.datetoString(new Date());
			}
			//base.setCREATID(Long.parseLong(lguserid));
			base.setTIMEOUT(AllUtil.getTimeStamp(wx_timeOut));
			base.setMODIFYID(Long.parseLong(lguserid));
			base.setNAME(wx_name);
			base.setSMS(wx_sms);
			// 0草搞，1定搞
			if(saveAndList!=null)
			{
				base.setSTATUS(1);
			}else {
				base.setSTATUS(0);
			}
			//base.setSTATUS(Integer.parseInt(wx_STATUS));
			base.setWxSHARE(0);
			// 删除用
			String rmovePage = "";
			LfWXPAGE paget = null;
			if (str != null && str.length > 0)
			{
				for (int i = 0; i < str.length; i++)
				{
					LfWXPAGE page = new LfWXPAGE();
					page.setNETID(base.getNETID());
					page.setNAME(str[i].split("<#!!#>")[0]);

					Long parentid = Long.parseLong(str[i].split("<#!!#>")[1]);

					page.setPARENTID(parentid);
					page.setCONTENT(str[i].split("<#!!#>")[2]);
					page.setID(Long.parseLong(str[i].split("<#!!#>")[3]));
					page.setLink(str[i].split("<#!!#>")[4]);
					rmovePage = rmovePage
							+ Integer.parseInt(str[i].split("<#!!#>")[3]) + ",";
					if (parentid == 0)
						paget = page;
					pageList.add(page);
				}

				String netid = request.getParameter("wx_netid");
				// 删除PAGE页面
				String PageID = "";
				List<DynaBean> pages = ueditorDao.getPages(netid, lguserid); // 查询出记录，用来匹配删除
				if (pages != null && pages.size() > 0)
				{
					DynaBean bean = null;
					for (int j = 0; j < pages.size(); j++)
					{
						bean = pages.get(j);
						if(bean.get("id")!=null){
							Long id = Long.parseLong(bean.get("id").toString());
							if (rmovePage.indexOf(id + ",") < 0)
							{
								PageID = PageID + id + ",";
							}
						}
					}
				}
				if (!"".equals(PageID))
				{

					PageID = PageID.substring(0, PageID.length() - 1);
					baseBiz.deleteByIds(LfWXPAGE.class, PageID); // 删除
				}

				int i = ueditorDao.getupdateByUeditor(base, paget, pageList);
				if (i > 0)
					request.setAttribute("panduan", "true");
				else
					request.setAttribute("panduan", "false");

			}

		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "修改网讯异常!");
		}
	}
	
	
	/**
	 * 保存业务数据
	 * @param request
	 * @param response
	 */
	private String trustSave(HttpServletRequest request, HttpServletResponse response) {
		
		try 
		{
			//String lguserid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);


			String lgcorpcode = request.getParameter("lgcorpcode");
			SimpleDateFormat formatDateTime = new SimpleDateFormat("yyMMddHHmmss");
			
			LfWXTrustData trustData = new LfWXTrustData();
			
			String trustID = AllUtil.toStringValue(request.getParameter("trustID"), "0");
			String trustName = "wj_"+lguserid+"_"+formatDateTime.format(new Date());
			String trustType = AllUtil.toStringValue(request.getParameter("trustType"), "0");
			String replyType = "";
			//定稿0，草稿-1
			String status = "0";
			
			int colNum = Integer.parseInt(request.getParameter("colNum"));
			
			String tableName = "W_"+ lguserid + "_" +formatDateTime.format(new Date());
			
			if(!trustType.equals("1")){
				replyType = "";
			}
			
			trustData.setId(Long.parseLong(trustID));
			trustData.setName(trustName);
			trustData.setTableName(tableName);
			trustData.setColnum(colNum);
			trustData.setType(Integer.parseInt(trustType));
			trustData.setUrl(replyType);
			trustData.setCorpCode(lgcorpcode);
			trustData.setStatus(Integer.parseInt(status));
			trustData.setCreatId(Long.parseLong(lguserid));
			
			Long out_TrustID = null;
			if(!trustID.equals("0"))
			{
				//删除旧业务数据表
				LfWXTrustData data = baseBiz.getById(LfWXTrustData.class, Long.parseLong(trustID));
				new TrustDataBiz().delTrustTable(data.getTableName());
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				conditionMap.put("trustId", trustID);
				//删除lfwxtrustcols表中对应的数据
				baseBiz.deleteByCondition(LfWXTrustCols.class, conditionMap);
				out_TrustID = Long.parseLong(trustID);
				trustData.setModifyDate(new Timestamp(System.currentTimeMillis()));
				baseBiz.updateObj(trustData);
			}
			else
			{
				trustData.setCreatDate(new Timestamp(System.currentTimeMillis()));
				trustData.setModifyDate(new Timestamp(System.currentTimeMillis()));
				out_TrustID = baseBiz.addObjReturnId(trustData);
			}
			
			if(out_TrustID != 0)
			{
				String colValue = request.getParameter("colValue");
				String[] colValueArray = colValue.split(";");
				String[] colArray = null;
				List<LfWXTrustCols> colList = new ArrayList<LfWXTrustCols>();
				for(int i = 0 ; i < colValueArray.length ; i++){
					colArray = colValueArray[i].split(",");
					String displayName = colArray[0];
					String type = "0";
					String length = colArray[2];
					String param = "0";
					
					String colName = colArray[0];
					
					int colSize = Integer.parseInt(length);
					
					//非字符串格式默认长度为32
					if(!type.equals("0")){
						colSize = 32;
					}
					
					LfWXTrustCols trustDataCol = new LfWXTrustCols();
					
					trustDataCol.setTrustId(out_TrustID);
					trustDataCol.setName(displayName);
					trustDataCol.setColName(colName);
					trustDataCol.setColType(Integer.parseInt(type));
					trustDataCol.setColSize(colSize);
					trustDataCol.setIsParam(Integer.parseInt(param));
					
					colList.add(trustDataCol);
				}
				baseBiz.addList(LfWXTrustCols.class, colList);
				
				//创建新业务数据表
				boolean optColTable = new TrustDataBiz().trustDataTable(tableName, colList, trustType);
			}
			
			return "succ";
		} 
		catch (Exception e) 
		{
			EmpExecutionContext.error(e, "保存业务数据异常!");
			return "error";
		}
	}
	
	
	// 编辑网讯
	public void findBYid(HttpServletRequest request,
			HttpServletResponse response) {
		
		try 
		{
			//String lguserid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);


			String lgcorpcode = request.getParameter("lgcorpcode");
			String netid = request.getParameter("netid");
			request.setAttribute("lguserid", lguserid);
			request.setAttribute("lgcorpcode", lgcorpcode);
			request.setAttribute("netid", netid);
			request.getRequestDispatcher(PATH +"/wx_addSurvey.jsp")
			.forward(request, response);
		} 
		catch (Exception e) {
			EmpExecutionContext.error(e, "编辑网讯异常!");
		}

	}
	
	public void del(HttpServletRequest request,
			HttpServletResponse response) {
		 String opType = null;
		opType=com.montnets.emp.common.constant.StaticValue.UPDATE;
		 String opContent = null;
		 String opUser = "";
		try 
		{
			/*String lguserid = request.getParameter("lguserid");*/
			String lgcorpcode = request.getParameter("lgcorpcode");
			String netId = request.getParameter("netId");
			
			opContent = "删除网讯（网讯id："+netId+"）";
			
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("NETID", netId);
			baseBiz.deleteByCondition(LfWXPAGE.class, conditionMap);
			Integer flag = baseBiz.deleteByCondition(LfWXBASEINFO.class, conditionMap);
			//增加用户名称日志中
			Object obsysuser=request.getSession(false).getAttribute("loginSysuser");
			if(obsysuser!=null){
				LfSysuser user=(LfSysuser)obsysuser;
				if(user.getUserName()!=null){
					opUser=user.getUserName();
				}
			}
			/*request.setAttribute("lguserid", lguserid);
			request.setAttribute("lgcorpcode", lgcorpcode);*/
			String result = "";
			if(flag > 0)
			{
				result="succ";
				spLog.logSuccessString(opUser, opModule, opType, opContent,lgcorpcode);
			}else
			{
				result = "fail";
				spLog.logFailureString(opUser, opModule, opType, opContent+opSper, null,lgcorpcode);
			}
			response.getWriter().print(result);
		} 
		catch (Exception e) 
		{
			EmpExecutionContext.error(e, "删除网讯异常!");
			try
			{
				response.getWriter().print("error");
			} catch (IOException e1)
			{
				EmpExecutionContext.error(e1, "删除网讯异常!");
			}
		}

	}
	
}
