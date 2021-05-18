package com.montnets.emp.wymanage.servlet;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.tools.SysuserUtil;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.security.wgmsgconfig.WgMsgConfigBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.wy.ASpePhone;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.PhoneUtil;
import com.montnets.emp.util.SuperOpLog;
import com.montnets.emp.util.TxtFileUtil;
import com.montnets.emp.wymanage.biz.SpePhoneManageBiz;

/**
 * 特殊号码管理
 * @description 
 * @project p_wygl
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2014-3-24 上午11:48:38
 */
public class wy_spePhoneManageSvt extends BaseServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1937276757187297993L;
	
	private final String empRoot="wygl";
	private final String base="/wymanage";
	private final String txtPath = "wygl/wymanage/file/";
	private final String opModule = "特殊号码管理";
	private final SpePhoneManageBiz phoneBiz = new SpePhoneManageBiz();
	protected final SuperOpLog spLog = new SuperOpLog();
	private final String line = StaticValue.systemProperty.getProperty(StaticValue.LINE_SEPARATOR);
	/**
	 * 特殊号码管理
	 * @description    
	 * @param request
	 * @param response       			 
	 * @author zhangmin
	 * @datetime 2014-3-24 上午11:50:21
	 */
	public void find(HttpServletRequest request, HttpServletResponse response)
	{
		long stime = System.currentTimeMillis();
		PageInfo pageInfo = new PageInfo();
		try {
			List<DynaBean> beanList = null;
			pageSet(pageInfo,request);
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			//手机号码
			String phone = request.getParameter("phone");
			//所属运营商
			String unicom = request.getParameter("unicom");
			conditionMap.put("phone", phone);
			conditionMap.put("unicom", unicom);
			beanList = phoneBiz.getSpePhone(pageInfo, conditionMap);
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("beanList", beanList);
			request.getRequestDispatcher(this.empRoot+base+"/wy_spePhoneManage.jsp").forward(request,
					response);
			DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			String sDate = sdf.format(stime);
			setLog(request,"特殊号码管理","("+sDate+")查询", StaticValue.GET);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"特殊号码管理查询异常！");
			request.setAttribute("findresult", "-1");
			request.setAttribute("pageInfo", pageInfo);
			try {
				request.getRequestDispatcher(this.empRoot+base+"/wy_spePhoneManage.jsp")
				.forward(request, response);
			} catch (ServletException e1) {				
				EmpExecutionContext.error(e1,"特殊号码管理查询异常！");
			} catch (IOException e1) {				
				EmpExecutionContext.error(e1,"特殊号码管理查询异常！");
			}
		}
	}
	
	/**
	 * 手工添加号码
	 * @description    
	 * @param request
	 * @param response       			 
	 * @author zhangmin
	 * @throws IOException 
	 * @datetime 2014-3-24 上午11:50:21
	 */
	public void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String corpCode = null;	
		String userId = null;	
		String userName = null;	
		String opContent = null;	
		String opType = "ADD";
		String OpStatus = "失败";
		String [] strArray = null;
		String opTypeSp=StaticValue.ADD;
		String opContentSp = "手动添加特殊号码";
		//操作模块
		String opSper = StaticValue.OPSPER;
		String res = "error";
		String lgusername = request.getParameter("lgusername");
		String lgcorpcode = request.getParameter("lgcorpcode");
		//String lguserid = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String lguserid = SysuserUtil.strLguserid(request);

		try {
			//手动添加号码字符串拼接
			String result = request.getParameter("result");
			List<String> phoneList = new ArrayList<String>();
			String [] phoneArray = result.split(",");
			for(int i=0;i<phoneArray.length;i++)
			{
				phoneList.add(phoneArray[i]);
			}
			strArray= checkPhone(phoneList,lguserid);
			if (strArray[0]!=null&&Integer.parseInt(strArray[0])>0)
			{
				OpStatus = "成功";
			}
			res=strArray[0]+","+strArray[1];
			spLog.logSuccessString(lgusername, opModule, opTypeSp, opContentSp,lgcorpcode);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"手动添加特殊号码失败");
			spLog.logFailureString(lgusername, opModule, opTypeSp, opContentSp+opSper, null,lgcorpcode);
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
			
			if(strArray != null){
				opContent = "手动添加特殊号码"+OpStatus+"。共导入"+strArray[0]+"条记录。";
			}else{
				opContent = "手动添加特殊号码"+OpStatus+"。";
			}
			EmpExecutionContext.info(opModule, corpCode, userId, userName, opContent, opType);
			response.getWriter().write(res);
		}
	}
	
	/**
	 * 批量上传特殊号码
	 * @description    
	 * @param request
	 * @param response
	 * @throws Exception       			 
	 * @author zhangmin
	 * @datetime 2014-3-25 上午09:53:49
	 */
	@SuppressWarnings("unchecked")
	public void uploadPhone(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		//操作模块
		String corpCode = null;	
		String userId = null;	
		String userName = null;	
		String opType = "ADD";
		String OpStatus = "失败";
		String opContent = null;
		String opSper = StaticValue.OPSPER;
		String opTypeSp = StaticValue.ADD;
		String opContentSp = "批量导入特殊号码";
		String [] strArray = null;
		//返回结果
		String result = "";
		//操作员id
		String lguserid = null;
		//企业编码
		String lgcorpcode = "";
		LfSysuser sysuser = null;
		try {
			sysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
		} catch (Exception e1) {
			EmpExecutionContext.error(e1,"session为null");
		}
		try
		{
			List<FileItem> fileList = null;
			try
			{
				//上传文件
				DiskFileItemFactory factory = new DiskFileItemFactory();
				factory.setSizeThreshold(1024 * 1024);
				
				//获取一个存放临时文件的目录
	        	String uploadPath = StaticValue.FILEDIRNAME;
	        	String temp = new TxtFileUtil().getWebRoot()+uploadPath;
				//创建上传文件的临时目录
				factory.setRepository(new File(temp));
				ServletFileUpload upload = new ServletFileUpload(factory);
			
				//判断所有上传文件最大数
				//upload.setSizeMax(2);
				// 以文件方式解析表单
				fileList = upload.parseRequest(request);
			} 
			catch (FileUploadException e)
			{
				result = "error";
				EmpExecutionContext.error(e,"特殊号码上传失败！");
				return;
			}

			List<String> phoneList = new ArrayList<String>();
			Iterator<FileItem> it = fileList.iterator();
			//临时表单控件对象
			FileItem fileItem = null;
			//文件数
			int fileCount = 0;
			//上传文件对象集合
			List<FileItem> fileItemsList = new ArrayList<FileItem>();
			//循环获取页面表单控件值
			while (it.hasNext())
			{
				fileItem = (FileItem) it.next();
				// 获取上传号码文件
				if (!fileItem.isFormField() && fileItem.getName().length() > 0)
				{
					String fileCurName = fileItem.getName();
					String fileType = fileCurName.substring(fileCurName.lastIndexOf("."));
					if ( 2*1024*1024 <fileItem.getSize())
					{
						result = "isOverSize";
						return;
					}
					//有效文件对象存放到集合
					fileItemsList.add(fileItem);
				}
				else
				{
					if (fileItem.getFieldName().equals("lguserid"))
					{
						lguserid= fileItem.getString("UTF-8").toString();
					} else if (fileItem.getFieldName().equals("lgcorpcode"))
					{
						//手机号
						lgcorpcode= fileItem.getString("UTF-8").toString();
					}
				}
			}

			// 循环解析每个上传文件对象，获取文本文件流集合
			for (int i = 0; i < fileItemsList.size(); i++)
			{
				if(phoneList!=null&&phoneList.size()>50000)
				{
					result = "countOut";
					return;
				}
				// 上传文件转换为文本文件流
				phoneList.addAll(phoneBiz.parseFile(fileItemsList.get(i),
						 fileCount));
				fileCount++;
			}

			if(phoneList!=null&&phoneList.size()>50000)
			{
				result = "countOut";
				return;
			}
			strArray= checkPhone(phoneList,sysuser.getUserId().toString());
			if (strArray[0]!=null&&Integer.parseInt(strArray[0])>0)
			{
				OpStatus = "成功";
			}
			result=strArray[0]+","+strArray[1];
			spLog.logSuccessString(sysuser.getUserName(), opModule, opTypeSp, opContentSp,sysuser.getCorpCode());
			
		}catch (Exception ex)
		{
			result = "error";
			spLog.logFailureString(sysuser.getUserName(), opModule, opTypeSp, opContentSp+opSper, null,sysuser.getCorpCode());
			EmpExecutionContext.error(ex, "批量上传特殊号码失败！");
		} finally
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
			
			if(strArray != null){
				opContent = "批量导入特殊号码"+OpStatus+"。共导入"+strArray[0]+"条记录。";
			}else{
				opContent = "批量导入特殊号码"+OpStatus+"。";
			}
			
			EmpExecutionContext.info(opModule, corpCode, userId, userName, opContent, opType);
			request.setAttribute("result", result);
			request.getRequestDispatcher(
					this.empRoot + base+"/wy_blank.jsp").forward(request,
					response);
		}
	}
	
	/**
	 * 验证手机号码
	 * @description
	 * @param phoneList
	 * @return
	 * @throws Exception       			 
	 * @author zhangmin
	 * @datetime 2014-3-25 下午02:17:20
	 */
	public String[] checkPhone(List<String> phoneList,String lguserid) throws Exception
	{
		WgMsgConfigBiz wb=new WgMsgConfigBiz();
		//获取运营商号码段
		String[] haoduan = wb.getHaoduan();
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("opttype", "0");
		List<ASpePhone> list = new BaseBiz().getByCondition(ASpePhone.class, conditionMap,null);
		HashSet<String> resultList = new HashSet<String>();
		List<ASpePhone> addList = new ArrayList<ASpePhone>();
		//过滤掉的号码
		List<String> badList = new ArrayList<String>();
		if(list!=null&&list.size()>0)
		{
			for(int i=0;i<list.size();i++)
			{
				resultList.add(list.get(i).getPhone());
			}
		}
		//手机不合法
		String conStr="";
		String repeatStr="";
		
		PhoneUtil phoneUtil = new PhoneUtil();
		for (int r = 0; r < phoneList.size(); r++)
		{
			String phone = phoneList.get(r);
			
			
			int unicom=phoneUtil.getPhoneType(phone, haoduan);
			//手机不合法
			if(unicom==-1)
			{
				badList.add(phone);
				conStr=conStr+line+phone;
				continue;
			}
			//电信
			if(unicom==2)
			{
				unicom=21;
			}
			//重复号
			if( !checkRepeat(resultList, phone))
			{
				badList.add(phone);
				repeatStr=repeatStr+line+phone;
				continue;
			}
			
			ASpePhone aspPhone = new ASpePhone();
			aspPhone.setCreatetime(new Timestamp(System.currentTimeMillis()));
			aspPhone.setOpttype(0);
			aspPhone.setPhone(phone);
			aspPhone.setUserid("000000");
			aspPhone.setUnicom(unicom);
			aspPhone.setCustid(0);
			aspPhone.setSpectype(10);
			addList.add(aspPhone);
		}
		TxtFileUtil txtFileUtil = new TxtFileUtil();
		String [] strArray = new String[2];
		strArray[0]=addList.size()+"";
		//空文件
		if(addList.size()==0)
		{
			if(badList.size()==0)
			{
				
				strArray[1]="-1";
				return strArray;
			}
		}
		else
		{
			phoneBiz.add(addList);
		}
		//存库
		// 生成文件路径用到
		Date time = Calendar.getInstance().getTime();
		// 包含文件的物理路径和相对路径
		String[] url = this.getClientUploadUrl((int) (Long.parseLong(lguserid) - 0), time);
		// 写文件
		String FileStr = url[0];
		// 临时文件路径
		String FileStrTemp = FileStr.substring(0, FileStr.indexOf(".txt"))+ "_wySpePhone" + ".txt";
		// 写文件
		txtFileUtil.writeToTxtFile(FileStrTemp, new StringBuffer("成功上传了")
				.append(addList.size()).append("条记录").append(line)
				.append("过滤号码：").append(badList.size()).append("条，过滤号码如下：").append(line).append("重复号码：")
				.append(repeatStr.length()==0?"无":repeatStr).append(line).append("非法号码：").append(conStr.length()==0?"无":conStr).toString());
		strArray[1] = url[1].substring(0, url[1].indexOf(".txt"))+"_wySpePhone"+".txt";
		return strArray;
	}
	
	public String[] getClientUploadUrl(int id, Date time)
	{
		TxtFileUtil txtFileUtil = new TxtFileUtil();
		//String uploadPath = StaticValue.CLIENT_UPLOAD;
		String uploadPath = txtPath + "download/";
		//String uploadPath ="yhgl/client/file/upload/";
		// 存放路径的数组
		String[] url = new String[5];
		String saveName = "1_" + id + "_"
				+ (new SimpleDateFormat("yyyyMMddHHmmssS")).format(time)
				+ ".txt";
		String logicUrl;
		String physicsUrl = txtFileUtil.getWebRoot();
		//提前生成目录
		txtFileUtil.makeDir(physicsUrl + uploadPath);
		physicsUrl = physicsUrl + uploadPath + saveName;
		logicUrl = uploadPath + saveName;
		url[0] = physicsUrl;
		url[1] = logicUrl;
		return url;
	}
	
	/**
	 * 过滤重复号码
	 * @description    
	 * @param aa
	 * @param ee
	 * @return       			 
	 * @author zhangmin
	 * @datetime 2014-3-25 下午02:16:29
	 */
	private boolean checkRepeat(HashSet<String> aa,String ee)
	{
		if(aa.contains(ee)){
			return false;
		}else{
			aa.add(ee);
		}
		return true;
	}
	

	/**
	 * 删除添加号码
	 * @description    
	 * @param request
	 * @param response       			 
	 * @author zhangmin
	 * @throws IOException 
	 * @datetime 2014-3-24 上午11:50:21
	 */
	public void del(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String corpCode = null;	
		String userId = null;	
		String userName = null;	
		String opContent = null;	
		String opType = "UPDATE";
		String OpStatus = "失败";
		//操作模块
		String opModule = "特殊号码管理";
		String opSper = StaticValue.OPSPER;
		String opTypeSp = StaticValue.DELETE;
		String opContentSp =  "删除特殊号码";
		String res = "error";
		String lgusername = request.getParameter("lgusername");
		String lgcorpcode = request.getParameter("lgcorpcode");
		//String lguserid = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String lguserid = SysuserUtil.strLguserid(request);

		String opStr = null;
		try {
			//手动添加号码字符串拼接
			String id = request.getParameter("id");
			//通过id查找需要删除的特殊号码信息
			opStr = phoneBiz.getSphoneList(id);
			int delcount = phoneBiz.del(id);
			if (delcount>0)
			{
				OpStatus = "成功";
			}
			res = delcount+"";
			spLog.logSuccessString(lgusername, opModule, opTypeSp, opContentSp,lgcorpcode);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"删除特殊号码失败");
			spLog.logFailureString(lgusername, opModule, opTypeSp, opContentSp+opSper, null,lgcorpcode);
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
			
			opContent = "删除特殊号码"+OpStatus+"。（"+opStr+"）";
			EmpExecutionContext.info(opModule, corpCode, userId, userName, opContent, opType);
			response.getWriter().write(res);
		}
	}
	
	/**
	 * 修改号码
	 * @description    
	 * @param request
	 * @param response       			 
	 * @author zhangmin
	 * @throws IOException 
	 * @datetime 2014-3-24 上午11:50:21
	 */
	public void eidt(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String corpCode = null;	
		String userId = null;	
		String userName = null;	
		String opContent = null;	
		String opType = "UPDATE";
		String OpStatus = "失败";
		//操作模块
	//	String opModule = "特殊号码管理";
		String opSper = StaticValue.OPSPER;
		String res = "error";
		String opTypeSp=StaticValue.UPDATE;
		String opContentSp = "修改特殊号码";
		String lgusername = request.getParameter("lgusername");
		String lgcorpcode = request.getParameter("lgcorpcode");
		//String lguserid = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String lguserid = SysuserUtil.strLguserid(request);

		String id = null;
		String oldPhone = null;
		String oldUnicom = null;
		String phone = null;
		int unicom = 0; 
		try {
			id = request.getParameter("id");
			//通过id查找原手机号码，运营商
			ASpePhone aSpePhone= new BaseBiz().getById(ASpePhone.class, id);
			oldPhone = aSpePhone.getPhone();
			oldUnicom = aSpePhone.getUnicom().toString();
			phone = request.getParameter("phone");
			opContent= "修改特殊号码（号码："+phone+"）";
			PhoneUtil phoneUtil = new PhoneUtil();
			
			
			WgMsgConfigBiz wb=new WgMsgConfigBiz();
			String[] haoduan = wb.getHaoduan();
			unicom=phoneUtil.getPhoneType(phone, haoduan);
			if(unicom==-1||unicom==3)
			{
				res = "haoduan_error";
				return;
			}
			//电信
			if(unicom==2)
			{
				unicom=21;
			}
			LinkedHashMap<String, String> conMap = new LinkedHashMap<String, String>();
			conMap.put("phone", phone);
			conMap.put("opttype", "0");
			List<ASpePhone> aspList = new BaseBiz().getByCondition(ASpePhone.class, conMap, null);
			if(aspList!=null && aspList.size()>0)
			{
				res = "exist";
				return;
			}
			phoneBiz.edit(id, unicom, phone);
			OpStatus = "成功";
			res = "success"+","+unicom;
			spLog.logSuccessString(lgusername, opModule, opTypeSp, opContentSp,lgcorpcode);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"修改特殊号码失败");
			spLog.logFailureString(lgusername, opModule, opTypeSp, opContentSp+opSper, null,lgcorpcode);
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
			if(lfSysuser!=null){
			userId = lfSysuser.getUserId().toString();
			corpCode = lfSysuser.getCorpCode();
			userName = lfSysuser.getUserName();
			}
			opContent = "修改特殊号码"+OpStatus+"。（ID："+id+"，手机号码："+oldPhone+"，所属运营商："+oldUnicom+"）-->（ID:"+id+"手机号码："+phone+"，所属运营商："+unicom+"）";
			EmpExecutionContext.info(opModule, corpCode, userId, userName, opContent, opType);
			response.getWriter().write(res);
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
