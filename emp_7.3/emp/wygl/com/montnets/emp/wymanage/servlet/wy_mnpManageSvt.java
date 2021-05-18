/**
 * Program  : wy_mnpManageSvt.java
 * Author   : zousy
 * Create   : 2014-3-24 上午11:46:57
 * company ShenZhen Montnets Technology CO.,LTD.
 *
 */

package com.montnets.emp.wymanage.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.constant.*;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.montnets.emp.security.numsegment.OprNumSegmentBiz;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.wy.AMnp;
import com.montnets.emp.entity.wy.AMnperrcode;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.wymanage.biz.MnpManageBiz;

/**
 * 
 * @author   zousy <zousy999@qq.com>
 * @version  1.0.0
 * @2014-3-24 上午11:46:57
 */
@SuppressWarnings("serial")
public class wy_mnpManageSvt  extends BaseServlet
{
	private final String empRoot="wygl";
	private final String base="/wymanage";
	private final MnpManageBiz mnpManageBiz = new MnpManageBiz();
	private final OprNumSegmentBiz phoneBiz = new OprNumSegmentBiz();
	private final int fileMaxSize = 5*1024*1024; 
	private final String opModule ="携号转网号码管理";
	/**
	 * 列表查询
	 * @description    
	 * @param request
	 * @param response       			 
	 * @author zousy <zousy999@qq.com>
	 * @throws IOException 
	 * @throws ServletException 
	 * @datetime 2014-3-24 上午11:55:08
	 */
	public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		long stime = System.currentTimeMillis();
		PageInfo pageInfo = new PageInfo();
		try {
			String phoneTypeStr = request.getParameter("phoneType");
			String addTypeStr = request.getParameter("addType");
			String phone = request.getParameter("phone");
			pageSet(pageInfo,request);
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			//号码类型
			if(phoneTypeStr!=null){
				Long unicom = -1L;
				Long phoneType = -1L;
				if(phoneTypeStr.length()==4){
					unicom = Long.valueOf(phoneTypeStr.substring(0, 2));
					phoneType = Long.valueOf(phoneTypeStr.substring(2));
					conditionMap.put("unicom", String.valueOf(unicom));
					conditionMap.put("phoneType", String.valueOf(phoneType));
				}else if("-1".equals(phoneTypeStr)){//未知
					conditionMap.put("unknown", "255");
				}
			}
			//添加类型
			if(addTypeStr!=null&&!"".equals(addTypeStr)){
				conditionMap.put("addType", addTypeStr);
			}
			//手机号码
			if(phone!=null&&!"".equals(phone)){
				conditionMap.put("phone", phone);
			}
			List<AMnp> list = mnpManageBiz.getMnpList(pageInfo, conditionMap);
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("list", list);
			DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			String sDate = sdf.format(stime);
			setLog(request,"携号转网号码管理","("+sDate+")查询", StaticValue.GET);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"携号转网号码管理查询异常！");
			request.setAttribute("findresult", "-1");
			request.setAttribute("pageInfo", pageInfo);
		}finally{
			request.getRequestDispatcher(this.empRoot+base+"/wy_mnpManage.jsp").forward(request,
					response); 
		}
	}
	
	/**
	 * 号码转网删除
	 * @description    
	 * @param request
	 * @param response
	 * @throws IOException       			 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2014-3-25 下午03:15:52
	 */
	public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException{
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String ids = request.getParameter("id");
		String corpCode = null;	
		String userId = null;	
		String userName = null;	
		String opContent = null;	
		String opType = "DELETE";
		String opStr = null;
		String OpStatus = "失败";
		if(ids==null||ids.length()<1){
			out.print("error");
			return;
		}
		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("id&in", ids);
			List<AMnp> aMnps = new BaseBiz().getByCondition(AMnp.class, conditionMap, null);
			opStr = mnpManageBiz.getOpStr(aMnps);
			mnpManageBiz.delete(ids);
			OpStatus = "成功";
			out.print("success");
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "号码转网删除发生异常！");
			out.print("exception");
		}
		finally
		{
			//增加操作日志
			Object obj = null;
			try {
				obj = request.getSession(false).getAttribute("loginSysuser");
			} catch (Exception e) {
				EmpExecutionContext.error(e,"session为null");
			}
			LfSysuser lfSysuser = (LfSysuser)obj;
			if(lfSysuser!=null && lfSysuser.getUserId()!=null){
			userId = lfSysuser.getUserId().toString();
			corpCode = lfSysuser.getCorpCode();
			userName = lfSysuser.getUserName();
			}
			
			opContent = "删除携号转网号码"+OpStatus+"。（"+opStr+"）";
			EmpExecutionContext.info(opModule, corpCode, userId, userName, opContent, opType);
		}
	}
	/**
	 * 手动添加
	 * @description    
	 * @param request
	 * @param response       			 
	 * @author zousy <zousy999@qq.com>
	 * @throws IOException 
	 * @datetime 2014-3-25 下午02:05:09
	 */
	public void add(HttpServletRequest request, HttpServletResponse response) throws IOException{
		Long unicom = null ,phoneType = null;
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String phoneTypeStr = request.getParameter("phoneType");
		String corpCode = null;	
		String userId = null;	
		String userName = null;	
		String opContent = null;	
		String opType = "ADD";
		String OpStatus = "失败";
		String phoneStr = null;
		int size = 0;
		try
		{
			if(!phoneTypeStr.matches("^[0-2]{4}$")){
				out.print("error");
				return;
			}
			unicom = Long.valueOf(phoneTypeStr.substring(0, 2));
			phoneType = Long.valueOf(phoneTypeStr.substring(2));
			phoneStr = request.getParameter("phoneStr");
			List<AMnp> lists = new ArrayList<AMnp>();
			Pattern pattern = Pattern.compile("(^|[，, 、\\n]+)(1\\d{10})($|[，, 、\\n]+)"); 
			Matcher matcher = pattern.matcher(phoneStr); 
			int regionEnd = matcher.regionEnd();
			while(matcher.find()){
				String phone = matcher.group(2);
				matcher.region(matcher.end(2), regionEnd);
				//不合法
				if(phoneBiz.getphoneType(phone)+1==0){
					continue;
				}
				AMnp mnp = new AMnp();
				mnp.setAddType(0L);
				mnp.setPhone(phone);
				mnp.setCreateTime(new Timestamp(System.currentTimeMillis()));
				mnp.setPhoneType(phoneType);
				mnp.setUnicom(unicom);
				lists.add(mnp);
			}
			if(lists.size()==0){
				out.print("nophone");
				return;
			}
			size = mnpManageBiz.saveMnpList(lists);
			if(size>0){
				OpStatus = "成功";
				out.print("success["+size+"]");
			}else{
				out.print("nophone");
			}
			
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "手动添加号码转网发生异常！");
			out.print("exception");
		}finally{
			//增加操作日志
			Object obj = null;
			try {
				obj = request.getSession(false).getAttribute("loginSysuser");
			} catch (Exception e) {
				EmpExecutionContext.error(e,"session为null");
			}
			LfSysuser lfSysuser = (LfSysuser)obj;
			if(lfSysuser!=null && lfSysuser.getUserId()!=null){
			userId = lfSysuser.getUserId().toString();
			corpCode = lfSysuser.getCorpCode();
			userName = lfSysuser.getUserName();
			}
			
			opContent = "手动添加携号转网号码"+OpStatus+"。共导入"+size+"条记录。";
			EmpExecutionContext.info(opModule, corpCode, userId, userName, opContent, opType);
		}
	}
	
	/**
	 * 号码转网修改
	 * @description    
	 * @param request
	 * @param response
	 * @throws IOException       			 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2014-3-25 下午03:30:26
	 */
	public void update(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String corpCode = null;	
		String userId = null;	
		String userName = null;	
		String opContent = null;	
		String opType = "UPDATE";
		String OpStatus = "失败";
		Long unicom = null ,phoneType = null;
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		Long id = null;
		String oldType = null;
		String oldPhone = null;
		String phoneStr = null;
		String phoneTypeStr = request.getParameter("phoneType");
		try
		{
			id = Long.valueOf(request.getParameter("id"));
			//通过id查找原号码类型、手机号码
			AMnp oldMnp = new BaseBiz().getById(AMnp.class, id);
			oldType = oldMnp.getUnicom().toString()+oldMnp.getPhoneType();
			oldPhone = oldMnp.getPhone();
			if(!phoneTypeStr.matches("^[0-2]{4}$")){
				out.print("error");
				return;
			}
			unicom = Long.valueOf(phoneTypeStr.substring(0, 2));
			phoneType = Long.valueOf(phoneTypeStr.substring(2));
			phoneStr = request.getParameter("phone");
//			String oldPhone = request.getParameter("oldPhone");
			if(phoneBiz.getphoneType(oldPhone)+1==0){
				out.print("error");
				return;
			}
			if(phoneBiz.getphoneType(phoneStr)+1==0){
				out.print("phoneError");
				return;
			}
			AMnp mnp = new AMnp(id, phoneStr, unicom, phoneType);
			int count = mnpManageBiz.updateMnp(mnp);
			if(count>0)
			{
				OpStatus = "成功";
				out.print("success");
				
			}else if(count==0){
				out.print("phoneRepeat");
			}else if(count==-1){
				out.print("notExist");
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "号码转网修改发生异常！");
			out.print("exception");
		}
		finally
		{
			//增加操作日志
			Object obj = null;
			try {
				obj = request.getSession(false).getAttribute("loginSysuser");
			} catch (Exception e) {
				EmpExecutionContext.error(e,"session为null");
			}
			LfSysuser lfSysuser = (LfSysuser)obj;
			if(lfSysuser!=null && lfSysuser.getUserId()!=null){
			userId = lfSysuser.getUserId().toString();
			corpCode = lfSysuser.getCorpCode();
			userName = lfSysuser.getUserName();
			}
			
			opContent = "修改携号转网号码"+OpStatus+"。（id："+id+"，类型："+oldType+"，手机号码"+oldPhone+"）-->（id："+id+"，类型："+unicom+""+phoneType+"，手机号码"+phoneStr+"）";
			EmpExecutionContext.info(opModule, corpCode, userId, userName, opContent, opType);
		}
	}
	
	/***
	 * 携号转网上传文件
	* @Description: TODO 上传文件
	* @param @param request 请求
	* @param @param response 
	* @param @throws IOException
	* @return void
	 */
	@SuppressWarnings("unchecked")
	public void importFile(HttpServletRequest request, HttpServletResponse response) throws IOException{
		Long unicom = 255L ,phoneType = 255L;
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		List<FileItem> fileList = null;
		String corpCode = null;	
		String userId = null;	
		String userName = null;	
		String opContent = null;	
		String opType = "ADD";
		String OpStatus = "失败";
		int size = 0;
		try
		{
			//上传文件
			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setSizeThreshold(1024 * 1024);
			ServletFileUpload upload = new ServletFileUpload(factory);
			//判断所有上传文件最大数
			upload.setSizeMax(fileMaxSize);
			// 以文件方式解析表单
			fileList = upload.parseRequest(request);
		} 
		catch(SizeLimitExceededException e)
		{
			out.print("outMaxSize");
			//捕获到文件超出最大数限制的异常
			EmpExecutionContext.error("携号转网上传文件大小超过系统限制！");
			return;
		}
		catch (FileUploadException e)
		{
			out.print("uploadError");
			EmpExecutionContext.error(e, "携号转网上传文件异常！");
			return;
		}

		Iterator<FileItem> it = fileList.iterator();
		//临时表单控件对象
		FileItem fileItem = null;
		//循环获取页面表单控件值
		while (it.hasNext())
		{
			FileItem item = (FileItem) it.next();
			String fileName = item.getName();
			// 获取上传号码文件
			if (!item.isFormField() &&fileName!=null&& fileName.toLowerCase().matches(".*\\.(txt|xls|xlsx|zip|et)$"))
			{
				fileItem = item;
				break;
			}
		}
		if(fileItem == null){
			out.print("noFile");
			return;
		}
		try
		{
			Map<String,String> phoneMap = mnpManageBiz.parseFile(fileItem,1);
			if(phoneMap.size()>20000){
				out.print("overMax");
				return;
			}
			List<AMnp> lists = new ArrayList<AMnp>();
			for(Entry<String, String> entry:phoneMap.entrySet()){
				AMnp mnp = new AMnp();
				mnp.setAddType(0L);
				mnp.setPhone(entry.getKey());
				mnp.setCreateTime(new Timestamp(System.currentTimeMillis()));
				String entryValue = entry.getValue();
				if(entryValue!=null&&entryValue.length()==4){
					unicom = mnpManageBiz.getUnicom(entryValue.substring(0,2));
					phoneType = mnpManageBiz.getUnicom(entryValue.substring(2));
				}else{
					unicom = phoneType = 255L;
				}
				mnp.setPhoneType(phoneType);
				mnp.setUnicom(unicom);
				lists.add(mnp);
			}
			if(lists.size()==0){
				out.print("nophone");
				return;
			}
			size = mnpManageBiz.saveMnpList(lists);
			if(size>0){
				OpStatus = "成功";
				out.print("success["+size+"]");
			}else{
				out.print("nophone");
			}
		}
		catch (Exception e)
		{
			out.print("exception");
			EmpExecutionContext.error(e, " 解析上传文件发生异常！");
		}finally
		{
			//增加操作日志
			Object obj = null;
			try {
				obj = request.getSession(false).getAttribute("loginSysuser");
			} catch (Exception e) {
				EmpExecutionContext.error(e,"session为null");
			}
			LfSysuser lfSysuser = (LfSysuser)obj;
			if(lfSysuser!=null && lfSysuser.getUserId()!=null){
			userId = lfSysuser.getUserId().toString();
			corpCode = lfSysuser.getCorpCode();
			userName = lfSysuser.getUserName();
			}
			
			opContent = "文件导入携号转网号码"+OpStatus+"。共导入"+size+"条记录。";
			EmpExecutionContext.info(opModule, corpCode, userId, userName, opContent, opType);
		}
	}

	private void setLog(HttpServletRequest request,String opModule,String opContent,String opType){
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
}

