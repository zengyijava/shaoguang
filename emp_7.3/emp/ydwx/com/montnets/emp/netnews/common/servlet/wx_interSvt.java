package com.montnets.emp.netnews.common.servlet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.netnews.biz.WXSendBiz;
import com.montnets.emp.netnews.common.CompressEncodeing;
import com.montnets.emp.netnews.daoImpl.Wx_ueditorDaoImpl;
import com.montnets.emp.netnews.entity.LfWXBASEINFO;
import com.montnets.emp.netnews.entity.LfWXData;

public class wx_interSvt extends BaseServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Wx_ueditorDaoImpl ueditorDao = new Wx_ueditorDaoImpl();
	/**
	 *wap互动信息
	 * 
	 * @param request
	 * @param response
	 */
	public void interaction(HttpServletRequest request,
			HttpServletResponse response) {
		
		try {
			String msg ="";
			if(request.getParameter("pageId") == null)
			{
				EmpExecutionContext.error("获取不到pageId");
				return;
			}
			String pageId = request.getParameter("pageId");
			
			String phone = null;
			if(request.getParameter("phone") != null){
				phone = request.getParameter("phone");
			}
			
			String taskId = null;
			if(request.getParameter("taskId") != null){
				taskId = request.getParameter("taskId");
			}
			
			
			//网讯信息对象
			LfWXBASEINFO base =  ueditorDao.getNetInfoByPageId(pageId);
			
			List<LfWXData> dataList = ueditorDao.getDataByNetId(base.getNETID().toString());
			if(dataList == null || dataList.size() == 0){
				EmpExecutionContext.error("获取不到互动数据");
				return;
			}
			String netInfo = new WXSendBiz().sendMsgInfo(base.getNETID().toString(), taskId);
			if(netInfo == null || netInfo.trim().length() == 0)
			{
				EmpExecutionContext.error("获取网讯模板编码失败！");
				return;
			}
			String tempPhone="";
			String regx="";//如果不符合这两种特殊的情况就不做处理
			//通过截取部分传输过去后，再做处理
			if(phone != null)
			{

				// 注明：该字段是为了处理网讯转换回10进制处理pageid，taskid时候解密处理
				// 解密长度随着这个两个参数的值变化而变化的
				int netInfoLeng = netInfo.length();
				 tempPhone = phone;
				if(tempPhone.length() > 2)
				{
					if(tempPhone.indexOf(" ") > -1)
					{
						tempPhone=tempPhone.replace(" ", "+");
						String first = tempPhone.substring(1, 2);// 取加号后面一位数，避免数字太长了，导致转换不了
						tempPhone = tempPhone.substring(2);
						regx = "*0" + first + netInfoLeng;// 组装之后的字符串 （*0表示加号开头,*1表示00开头）
					}
					else if(tempPhone.indexOf("00") > -1)
					{
						String first = tempPhone.substring(0, 2);// 取加号后面一位数，避免数字太长了，导致转换不了
						if("00".equals(first))
						{
							tempPhone = tempPhone.substring(2);
							regx = "*1" + netInfoLeng;// 组装之后的字符串（*0表示加号开头,*1表示00开头）
						}
					}
				}
			}
			Map<String,String[]> answerMap = new HashMap<String,String[]>();
			String[] answer = null;
			for(int i=0;i<dataList.size();i++)
			{
				answer = request.getParameterValues(dataList.get(i).getColName());
				answerMap.put(dataList.get(i).getColName(), answer);
			}
			boolean resultInsert=ueditorDao.insertData(base.getNETID().toString(), pageId, phone, answerMap, base.getDataTableName(), taskId);
			
			if(resultInsert){
				//增加操作日志
				Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
				if(loginSysuserObj!=null){
					LfSysuser sysuser=(LfSysuser)loginSysuserObj;
					EmpExecutionContext.info("网讯预览", sysuser.getCorpCode(), sysuser.getUserId()+"", sysuser.getUserName(), "网讯手机互动项提交成功。[手机号码，网讯ID，存入数据库的表名，任务ID,pageId]（"+phone+"，"+base.getNETID().toString()+"，"+base.getDataTableName()+"，"+taskId+"，"+pageId+"）", "ADD");
				}
				
				msg="提交成功！";
			}else{
				//增加操作日志
				Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
				if(loginSysuserObj!=null){
					LfSysuser sysuser=(LfSysuser)loginSysuserObj;
					EmpExecutionContext.info("网讯预览", sysuser.getCorpCode(), sysuser.getUserId()+"", sysuser.getUserName(), "网讯手机互动项提交失败。[手机号码，网讯ID，存入数据库的表名，任务ID,pageId]（"+phone+"，"+base.getNETID().toString()+"，"+base.getDataTableName()+"，"+taskId+"，"+pageId+"）", "ADD");
				}
			}
			
			request.setAttribute("msg", msg);
			//修正编码规则
			request.setAttribute("w", netInfo+CompressEncodeing.JieMPhone(tempPhone)+regx);
			
			request.getRequestDispatcher("/ydwx/wap/wx_info.jsp").forward(request,
					response);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"wap互动信息");
		}

	}


}
