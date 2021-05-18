package com.montnets.emp.samemms.servlet;


import com.montnets.emp.common.atom.AddrBookAtom;
import com.montnets.emp.common.biz.*;
import com.montnets.emp.common.constant.EMPException;
import com.montnets.emp.common.constant.ErrorCodeInfo;
import com.montnets.emp.common.constant.IErrorCode;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.common.vo.GroupInfoVo;
import com.montnets.emp.entity.client.LfClientDep;
import com.montnets.emp.entity.employee.LfEmployee;
import com.montnets.emp.entity.employee.LfEmployeeDep;
import com.montnets.emp.entity.group.LfUdgroup;
import com.montnets.emp.entity.pasroute.LfMmsAccbind;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.template.LfTemplate;
import com.montnets.emp.entity.ydcx.LfDfadvanced;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.mmstask.biz.MmsTaskBiz;
import com.montnets.emp.samemms.biz.*;
import com.montnets.emp.samemms.biz.SameMmsBiz;
import com.montnets.emp.samemms.vo.LfTemplateVo;
import com.montnets.emp.security.blacklist.BlackListAtom;
import com.montnets.emp.security.keyword.KeyWordAtom;
import com.montnets.emp.security.wgmsgconfig.WgMsgConfigBiz;
import com.montnets.emp.servmodule.ydcx.constant.ServerInof;
import com.montnets.emp.util.*;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 *   静态发送彩信
 * @author Administrator
 *
 */
public class smm_sameMmsSvt  extends BaseServlet {

	private static final long serialVersionUID = 3732460853974462274L;
	private final TxtFileUtil txtfileutil = new TxtFileUtil();
	private final String dirUrl = txtfileutil.getWebRoot();
	private final CreateTmsFile mpb = new CreateTmsFile();
	private static final Integer SIZE = 50;
	private final BadWordsFilterBiz badFilter = new BadWordsFilterBiz();
	private final SameMmsBiz staMmsBiz = new SameMmsBiz();
	//写文件时候要的换行符
	private static final String line = StaticValue.systemProperty.getProperty(StaticValue.LINE_SEPARATOR);
	private final BlackListAtom blackListBiz = new BlackListAtom();
	private final BalanceLogBiz biz = new BalanceLogBiz();
	private final WgMsgConfigBiz msgConfigBiz = new WgMsgConfigBiz();
	private final KeyWordAtom keyWordAtom = new KeyWordAtom();
	private final AddrBookAtom addrBookAtom = new AddrBookAtom();
	private final PhoneUtil phoneUtil = new PhoneUtil();
	
	private static final String opModule = StaticValue.MMS_BOX;
	//private String opType = null;
	//private String opContent = null;
	private final String opSper = StaticValue.OPSPER;
	private final String empRoot = "ydcx";
	private final String basePath = "/samemms";
	private final int MAX_SIZE=5000000;
	private final  BaseBiz baseBiz = new BaseBiz();
	
	/**
	 *  查询方法
	 * @param request
	 * @param response
	 */
	public void find(HttpServletRequest request, HttpServletResponse response) {
		try {
			EmpExecutionContext.logRequestUrl(request,"<静态彩信发送页面跳转>");
			String lguserid = "";
			String corpCode = "";
			String userName = "";
			Long guId = 0L;

			LfSysuser sysUser= null;
			try{
				//获取当前操作员的GUID
				//lguserid = request.getParameter("lguserid");
				//漏洞修复 session里获取操作员信息
				lguserid = SysuserUtil.strLguserid(request);

				if(lguserid==null||"".equals(lguserid.trim())||"undefined".equals(lguserid.trim())){
					EmpExecutionContext.error("静态彩信发送获取lguserid参数异常！lguserid="+lguserid+"。改成从Session获取。");
					LfSysuser loginSysuser=(LfSysuser) request.getSession(false).getAttribute("loginSysuser");
					lguserid=String.valueOf(loginSysuser.getUserId());
				}
				//获取当前对象
				sysUser = baseBiz.getById(LfSysuser.class, lguserid);
				//获取企业编码
				corpCode = sysUser.getCorpCode();
				//获取操作员用户ID
				//获取当前操作员用户名
				userName = sysUser.getUserName();
				//获取当前操作员的GUID
				guId = sysUser.getGuId();
			}catch (Exception e) {
				EmpExecutionContext.error(e,"静态彩信发送获取当前操作员对象失败！");
			}
			//获取任务ID
			Long taskId = GlobalVariableBiz.getInstance().getValueByKey("taskId", 1L);
			request.setAttribute("taskId", taskId);
			
			//查询彩信静态模板
			LfTemplate template = new LfTemplate();
			//操作员用户ID
			template.setUserId(Long.valueOf(lguserid));
			//查询静态模板
			template.setDsflag(0L);
			//查询启用的模板
			template.setTmState(1L);
			List<LfTemplate> mmsList = staMmsBiz.getMMSTemplateByUserId(template);
			
			
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("userId", userName);
			//获取该操作员的审批信息
			/*List<LfFlow> flowList = baseBiz.getByCondition(LfFlow.class,conditionMap, null);
			if (flowList != null && flowList.size() > 0) {
				request.setAttribute("isFlow", "true");
			} else {
				request.setAttribute("isFlow", "false");
			}*/
			
			//conditionMap.clear();
			//conditionMap.put("isValidate", "1");
			//conditionMap.put("corpCode", corpCode);
			
			//获取彩信发送账号
			//List<LfMmsAccbind> mmsAccbinds = baseBiz.getByCondition(LfMmsAccbind.class, conditionMap, null);
			LfMmsAccbind lfMmsAccbind=new LfMmsAccbind();
			lfMmsAccbind.setCorpCode(corpCode);
			List<LfMmsAccbind> mmsAccbinds=staMmsBiz.getMmsSpUser(lfMmsAccbind);
			request.setAttribute("mmsAccbinds", mmsAccbinds);
			request.setAttribute("mmsTempList", mmsList);
			request.setAttribute("guId", guId);
			
			//获取高级设置默认信息
			conditionMap.clear();
			conditionMap.put("userid", lguserid);
			//9：静态彩讯发送
			conditionMap.put("flag", "9");
			LinkedHashMap<String, String> orderMap = new LinkedHashMap<String, String>();
			orderMap.put("id", StaticValue.DESC);
			List<LfDfadvanced> lfDfadvancedList = baseBiz.getByCondition(LfDfadvanced.class, conditionMap, orderMap);
			LfDfadvanced lfDfadvanced = null;
			if(lfDfadvancedList != null && lfDfadvancedList.size() > 0)
			{
				lfDfadvanced = lfDfadvancedList.get(0);
			}
			request.setAttribute("lfDfadvanced", lfDfadvanced);
			//设置服务器名称
			new ServerInof().setServerName(getServletContext().getServerInfo());			
			request.getRequestDispatcher(empRoot + basePath +"/smm_sameMms.jsp")
			.forward(request, response);
		}catch (Exception e) {
		   EmpExecutionContext.error(e,"静态彩信发送页面跳转失败！");
		}
		
	}
	
	/**
	 *   上传彩信文件
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void uploadMmsFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Long userId = null;
		String returnMsg = "";
		try {
			//获取用户ID
			//String lguserid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);


			String old = request.getParameter("old");
			try{
					userId = Long.valueOf(lguserid);
			}catch (Exception e) {
				EmpExecutionContext.error(e,"上传彩信文件操作员lguserid转化出现异常！");
			}
			String mmsFileName = "";
			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setSizeThreshold(1024 * 1024);
			factory.setRepository(new File(dirUrl + File.separator + "fileUpload" + File.separator + "mms" + File.separator + "mttasks"));
			ServletFileUpload upload = new ServletFileUpload(factory);
			List<FileItem> items = upload.parseRequest(request);
			Iterator<FileItem> iter = items.iterator();
			//号码文件名称
			String fileName = "4_"+ (int) (userId - 0)+ "_"+ (new SimpleDateFormat("yyyyMMddHHmmss")).format(Calendar.getInstance().getTime());
			//判断是上传号码文件还是TMS文件   TRUE 为号码文件
			while (iter.hasNext()) {
			  FileItem fileItem = (FileItem) iter.next();
			  if(!fileItem.isFormField() && fileItem.getName().length() > 0 && ("smmFile").equals(fileItem.getFieldName())){
				  //判断彩信文件大小是否小于80k
				  if(fileItem.getSize() - (80*1024) > 0){
					  returnMsg = "{oversize:'oversize'}";
				    	break;
			    	}
					mmsFileName = fileName + ".tms";
					
					//获取系统时间
					Calendar calendar = Calendar.getInstance();
					//获取当前年，月
					String yearstr = String.valueOf(calendar.get(Calendar.YEAR));
					int month = calendar.get(Calendar.MONTH) + 1;
					//处理小于10的月份
					String monthstr = month > 9 ? String.valueOf(month)
							: "0" + String.valueOf(month);
					String uploadPath=StaticValue.MMS_MTTASKS+yearstr+"/"+monthstr+"/";
						try
						{
							new File(txtfileutil.getWebRoot()+uploadPath).mkdirs();
						} catch (Exception e)
						{
							EmpExecutionContext.error(e, "彩信写文件失败！");
						}
					
					fileItem.write(new File(dirUrl + uploadPath + mmsFileName));
					
					String lgcorpcode=request.getParameter("lgcorpcode");
					int isOk = parseTms( uploadPath + mmsFileName, lgcorpcode);
			    	if(isOk==-1){
			    		returnMsg="{typeNotMatch:'typeNotMatch'}";
			    		break;
			    	}else if(isOk==-2){
			    		returnMsg="{tmsNotMatch:'tmsNotMatch'}";
			    		break;
			    	}
					
					String mms = mpb.getTmsFileInfo(uploadPath + mmsFileName);
					if(mms != null && !"".equals(mms))
					{
						mms = mms.replace("\r\n","&lt;BR/&gt;");   
						mms = mms.replace("\n","&lt;BR/&gt;"); 
					}
					returnMsg = "{mmsFileName:'" + uploadPath + mmsFileName + "',mmsinfomation:'"+mms+"'}";
					//删除原先上传的文件
					if (old != null && !"".equals(old)) {
                        boolean delete = new File(dirUrl + old).delete();
                        if (!delete) {
                            EmpExecutionContext.error("删除文件失败！");
                        }
                    }
				}
			}
			response.getWriter().print(returnMsg);
		}catch (Exception e) {
			response.getWriter().print("");
			EmpExecutionContext.error(e,"上传彩信文件出现异常！");
		}
	}
	
	public int parseTms(String filePath,String lgcorpcode) {
		String fileName;
		filePath = dirUrl + filePath;
		String tmsFileName = filePath.substring(filePath.lastIndexOf("/")+1);
		ParseTmsFile parseTms = new ParseTmsFile();
		List<TmsSmilItem> tmsSmilItem = parseTms.Parse(filePath);
		if(tmsSmilItem==null){
			return -2;
		}
		int isOk = 1;
		try{
			String corpcodedir = lgcorpcode +"/";
			new TxtFileUtil().makeDir(dirUrl + StaticValue.MMS_TEMPRESOURCE + corpcodedir);
			List<TmsSmilParItem> parItemsList = null;
			for(int i = 0; i < tmsSmilItem.size(); i++){
				parItemsList = tmsSmilItem.get(i).getParItemsList();
				TmsSmilParItem parItem = null;
				String type = "";
				for(int j = 0; j < parItemsList.size(); j++){
					parItem = parItemsList.get(j);
					String src = parItem.getSrc().substring(parItem.getSrc().lastIndexOf(":")+1);
					fileName = StaticValue.MMS_TEMPRESOURCE + corpcodedir + tmsFileName + "_" + src;
					type = fileName.substring(fileName.lastIndexOf(".")+1).toLowerCase();
					if(parItem.getType().equals("image")){
						if(!"jpg".equals(type) && !"jpeg".equals(type) && !"gif".equals(type)){
							isOk = -1;
							break;
						}
					}else if(parItem.getType().equals("sound")){
						if(!"mid".equals(type) && !"midi".equals(type) && !"amr".equals(type)){
							isOk = -1;
							break;
						}
					}
				}
			}
		}catch(Exception e){
			EmpExecutionContext.error(e,"解析tms文件出现异常！");
			return -2;
		}
		return isOk;
	}
	
	/**
	 *  删除彩信文件
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void delMmsFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try{
			String mmsUrl = request.getParameter("mmsUrl");
			//能查询w文件 就删除，没有的就不做删除
			if (mmsUrl != null && !"".equals(mmsUrl)) {
                boolean delete = new File(dirUrl + mmsUrl).delete();
                if (!delete) {
                    EmpExecutionContext.error("删除文件失败！");
                }
            }
			response.getWriter().print("1");
		}catch (Exception e) {
			response.getWriter().print("1");
			EmpExecutionContext.error(e,"删除彩信文件出现异常！");
		}
	}
	
	
	
	
	
	/**
	 *   处理点预览时  查询彩信文件信息
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void getTmMsg(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		try
		{
			String tmUrl=request.getParameter("tmUrl");
			String isokDownload = "";
			//判断是否使用集群   以及如果不存在该文件
			if(StaticValue.getISCLUSTER() == 1 && !txtfileutil.checkFile(tmUrl))
			{
				CommonBiz commBiz = new CommonBiz();
				//下载到本地
				if(!"success".equals(commBiz.downloadFileFromFileCenter(tmUrl))){
					isokDownload = "notmsfile";
				}
			}
			
			if("".equals(isokDownload)){
				if(tmUrl == null || "".equals(tmUrl))
				{
					response.getWriter().print("");
				}else{
					String mms = mpb.getTmsFileInfo(tmUrl);
					if(mms != null && !"".equals(mms))
					{
						mms = mms.replace("\r\n","&lt;BR/&gt;");   
						mms = mms.replace("\n","&lt;BR/&gt;"); 
					}
					response.getWriter().print(mms);
				}
			}else{
				response.getWriter().print("");
			}
		}catch (Exception e)
		{
			response.getWriter().print("");
			EmpExecutionContext.error(e,"获取彩信文件信息出现异常！");
		}
	}
	
	
	/**
	 *   过滤号码是否合法
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws Exception
	 */
	public void filterPhone(HttpServletRequest request,
			HttpServletResponse response) throws IOException{
		try{
			String[] haoduan = msgConfigBiz.getHaoduan();
			String tmp = request.getParameter("tmp");
			if (phoneUtil.checkMobile(tmp, haoduan) != 1) {
				response.getWriter().print("2");
			} else {
				response.getWriter().print("1");
			}
		}catch (Exception e) {
			response.getWriter().print("");
			EmpExecutionContext.error(e,"过滤号码出现异常！");
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~处理静态彩信发送选择对象
	/**
	 *   查询员工 机构下的员工，不包含
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void getEmployeeByDepId(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> orderByMap = new LinkedHashMap<String, String>();
			StringBuffer sb = new StringBuffer();
			String depId = request.getParameter("depId");
			String pageIndex = request.getParameter("pageIndex");
			PageInfo pageInfo = new PageInfo();
			pageSet(pageInfo,request);
			pageInfo.setPageSize(SIZE);
			pageInfo.setPageIndex(Integer.valueOf(pageIndex));
			orderByMap.put("employeeId",StaticValue.ASC);
			conditionMap.put("depId", depId);
			List<LfEmployee> lfEmployeeList = baseBiz.getByCondition(LfEmployee.class, null, conditionMap, orderByMap, pageInfo);
			if (lfEmployeeList != null && lfEmployeeList.size() > 0) {
				sb.append(pageInfo.getTotalPage()).append(",").append(pageInfo.getTotalRec()).append("#");
				for (LfEmployee user : lfEmployeeList) {
					sb.append("<option value='").append(user.getGuId()).append("' isdep='4' et='' moblie='"+user.getMobile()+"'>");
					sb.append(user.getName().trim().replace("<","&lt;").replace(">","&gt;")).append("</option>");
				}
			}
			response.getWriter().print(sb.toString());
		} catch (Exception e)
		{
			response.getWriter().print("");
			EmpExecutionContext.error(e,"静态彩信获取员工机构下员工出现异常！");
		}
	}
	
	
	/**
	 *   查询客户机构下的客户，不包含
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void getClientByDepId(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		try
		{
		//	LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		//	LinkedHashMap<String, String> orderByMap = new LinkedHashMap<String, String>();
			StringBuffer sb = new StringBuffer();
			String depId = request.getParameter("depId");
			String pageIndex = request.getParameter("pageIndex");
			PageInfo pageInfo = new PageInfo();
			pageSet(pageInfo,request);
			pageInfo.setPageSize(SIZE);
			pageInfo.setPageIndex(Integer.valueOf(pageIndex));
			LfClientDep clientDep = baseBiz.getById(LfClientDep.class, depId);
			List<DynaBean> beanList = staMmsBiz.getClientsByDepId(clientDep, 2, pageInfo);
			if (beanList != null && beanList.size() > 0) {
				sb.append(pageInfo.getTotalPage()).append(",").append(pageInfo.getTotalRec()).append("#");
				for (DynaBean bean : beanList) {
					sb.append("<option value='").append(String.valueOf(bean.get("guid")));
					sb.append("' isdep='5' et=''  moblie='"+String.valueOf(bean.get("mobile"))+"'>");
					sb.append(String.valueOf(bean.get("name")).replace("<","&lt;").replace(">","&gt;")).append("</option>");
				}
			}
			
		/*	conditionMap.put("depId", depId);
			orderByMap.put("clientId",StaticValue.ASC);
			List<LfClient> lfClientList = baseBiz.getByCondition(LfClient.class, null, conditionMap, orderByMap, pageInfo);
			if (lfClientList != null && lfClientList.size() > 0) {
				sb.append(pageInfo.getTotalPage()).append(",").append(pageInfo.getTotalRec()).append("#");
				for (LfClient client : lfClientList) {
					sb.append("<option value='").append(client.getGuId()).append("' isdep='5' et=''  moblie='"+client.getMobile()+"'>");
					sb.append(client.getName().trim().replace("<","&lt;").replace(">","&gt;")).append("</option>");
				}
			}*/
			response.getWriter().print(sb.toString());
		} catch (Exception e)
		{
			response.getWriter().print("");
			EmpExecutionContext.error(e,"静态彩信获取客户机构下客户出现异常！");
		}
	}
	
	
	/**
	 *   通过群组ID查询出员工/客户群组 中的群组人员信息，分页
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void getGroupUserByGroupId(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		try
		{
			StringBuffer sb = new StringBuffer();
			String groupId = request.getParameter("depId");
			String pageIndex = request.getParameter("pageIndex");
			String type = request.getParameter("type");
			PageInfo pageInfo = new PageInfo();
			pageSet(pageInfo,request);
			pageInfo.setPageSize(SIZE);
			pageInfo.setPageIndex(Integer.valueOf(pageIndex));
			List<GroupInfoVo> groupInfoList = null;
			groupInfoList = staMmsBiz.getGroupUser(Long.valueOf(groupId), pageInfo,type);
			if (groupInfoList != null && groupInfoList.size() > 0) {
				sb.append(pageInfo.getTotalPage()).append(",").append(pageInfo.getTotalRec()).append("#");
				for (GroupInfoVo user : groupInfoList) {
					//默认员工
					String l2gtype = "0";
					if(user.getL2gType() == 0){
						//员工
						 l2gtype = "4";
					}else if(user.getL2gType() == 1){
						//客户
						 l2gtype = "5";
					}else if(user.getL2gType() == 2){
						//自定义
						 l2gtype = "6";
					}
					if(user.getName() != null){
						sb.append("<option value='").append(user.getGuId()).append("' isdep='"+l2gtype+"' et='' moblie='"+user.getMobile()+"'>");
						sb.append(user.getName().trim().replace("<","&lt;").replace(">","&gt;")).append("</option>");
					}else{
						continue;
					}
					
				}
			}
			response.getWriter().print(sb.toString());
		} catch (Exception e)
		{
			response.getWriter().print("");
			EmpExecutionContext.error(e,"静态彩信获取群组下成员出现异常！");
		}
	}
	
	
	
	/**
	 *   获取彩信发送时所对应的所有员工群组
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void getStaMMSGroup(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
			try
			{	
				LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
				String corpCode = lfSysuser.getCorpCode();
				//用户ID
				String userId = request.getParameter("userId");
				//1代表的是查询完美通知中的群组   2代表的是查询彩信发送中的群组 
				String grouptype = request.getParameter("grouptype");
				/*try{
					if(userId == null || "".equals(userId)){
						userId = String.valueOf(getUserId());
					}
				}catch (Exception e) {
					EmpExecutionContext.error(e);
				}*/
			
				List<LfUdgroup> lfUdgroupList = new ArrayList<LfUdgroup>();
				LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
				LinkedHashMap<String,String> orderbyMap = new LinkedHashMap<String,String>();
				StringBuffer sb = new StringBuffer("<select select-one name='groupList' id='groupList' size ='16'  onclick='a();' style='width: 245px;border:0 solid #000000;font-size: 12px;color: black; padding:4px;vertical-align:middle;margin:-4px -5px;'>");
				//登录的用户ID
				conditionMap.put("receiver", userId);
				//员工群组
				conditionMap.put("gpAttribute","0");
				//排序
				orderbyMap.put("udgId",StaticValue.ASC);
				//群组列表
				lfUdgroupList = baseBiz.getByCondition(LfUdgroup.class, conditionMap, orderbyMap);
				if(lfUdgroupList != null && lfUdgroupList.size()>0)
				{
					String udgIds = "";
					for(LfUdgroup udg : lfUdgroupList)
					{
						udgIds += udg.getGroupid().toString()+",";
					}
					//获取群组id字符串
					udgIds = udgIds.substring(0,udgIds.length()-1);
					//Map<String,String> countMap = addrBookAtom.getGroupCount(udgIds, "1");
					Map<String,String> countMap =new GroupBiz().getGroupMemberCount(udgIds, 1,corpCode);
					for(LfUdgroup lfUdgroup :lfUdgroupList)
					{
						String shareType = "0";
						if(lfUdgroup.getSharetype() == 1){
							//共享
							shareType = "1";
						}
						String mcount = countMap.get(lfUdgroup.getGroupid().toString());
						mcount = mcount == null ? "0" : mcount;
						//shareType表示是0个人 1共享   groupType1 是员工群组  2是客户群组 
						sb.append("<option gcount='"+mcount+"' isdep='3' sharetype ='"+shareType+"' gtype='1' value='"+lfUdgroup.getGroupid()+"' udgid='"+lfUdgroup.getUdgId()+"' style='padding-left: 5px;'>");
						sb.append(lfUdgroup.getUdgName().replace("<","&lt;").replace(">","&gt;"));
						if(lfUdgroup.getSharetype() == 0){
							sb.append(" [员工/个人]");
						}else if(lfUdgroup.getSharetype() == 1){
							sb.append(" [员工/共享]");
						}
						sb.append("</option>");
					}
				}
				
				//将群组列表设为null
				lfUdgroupList = null;
				//查询出客户群组
				conditionMap.clear();
				//彩信才需要查询出客户群组，完美通知不需要 
				if("2".equals(grouptype)){
					//conditionMap.put("userId",userId);
					//登录的用户ID
					conditionMap.put("receiver", userId);
					conditionMap.put("gpAttribute","1");
					lfUdgroupList = baseBiz.getByCondition(LfUdgroup.class, conditionMap, orderbyMap);
					if(lfUdgroupList != null && lfUdgroupList.size()>0)
					{
						String udgIds = "";
						for(LfUdgroup udg : lfUdgroupList)
						{
							//udgIds += udg.getUdgId().toString()+",";
							udgIds += udg.getGroupid().toString()+",";
						}
						//获取群组id字符串
						udgIds = udgIds.substring(0,udgIds.length()-1);
						//Map<String,String> countMap = staMmsBiz.getGroupCount(udgIds, "2");
						Map<String,String> countMap = new GroupBiz().getGroupMemberCount(udgIds,2,corpCode);
						for(LfUdgroup lfUdgroup :lfUdgroupList)
						{
							String shareType = "0";
							if(lfUdgroup.getSharetype() != null && lfUdgroup.getSharetype() == 1){
								//共享
								shareType = "1";
							}
							
							//String mcount = countMap.get(lfUdgroup.getUdgId().toString());
							String mcount = countMap.get(lfUdgroup.getGroupid().toString());
							mcount = mcount == null ? "0" : mcount;
							//shareType表示是0个人 1共享   groupType1 是员工群组  2是客户群组 
							sb.append("<option  gcount='"+mcount+"' isdep='3' sharetype ='"+shareType+"' gtype='2'  value='"+lfUdgroup.getGroupid()+"' udgid='"+lfUdgroup.getUdgId()+"'  style='padding-left: 5px;'>")
							.append(lfUdgroup.getUdgName().replace("<","&lt;").replace(">","&gt;"));
							if(lfUdgroup.getSharetype() == 0){
								sb.append(" [客户/个人]");
							}else if(lfUdgroup.getSharetype() == 1){
								sb.append(" [客户/共享]");
							}
							sb.append("</option>");
						}
						
					}
				}
				sb.append("</select>");
				response.getWriter().print(sb.toString());
			} catch (Exception e)
			{
				EmpExecutionContext.error(e,"静态彩信获取群组列表出现异常！");
	 		}
			
	}
	
	
	/**
	 *   这里是检测点击员工机构是否被选择了的机构包含
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void isEmpDepContained(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		try
		{
			//点击机构的ID
			String depId = request.getParameter("depId");
			//已经选择好的机构ID
			String empDepIds=request.getParameter("empDepIds");
			//解析IDS
			String[] depIds= empDepIds.split(",");
			//机构集合
			List<LfEmployeeDep> lfEmployeeDepList= null;
			//处理是否包含机构ID的集合
			LinkedHashSet<Long> depIdsSet = new LinkedHashSet<Long>();
			//查询条件集合
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			//机构对象
			LfEmployeeDep dep = null;
			//循环
			for(int i=0;i<depIds.length;i++)
			{
				String id = depIds[i];
				//如果包含了，则说明该机构包含子机构
				if(id == null || "".equals(id)){
					continue;
				}
				//如果相等，则眺出
				if(id.equals(depId))
				{
					response.getWriter().print("depExist");
					return;
				//遇到包含子机构的机构处理操作
				}else if(id.contains("e")){
					Long temp = Long.valueOf(id.substring(1));
					conditionMap.clear();
					dep = null;
					lfEmployeeDepList= null;
					dep = baseBiz.getById(LfEmployeeDep.class, temp);
					if(dep != null){
						conditionMap.put("deppath&like",dep.getDeppath());
						conditionMap.put("corpCode", dep.getCorpCode());
						lfEmployeeDepList = baseBiz.getByCondition(LfEmployeeDep.class, conditionMap, null);
						if(lfEmployeeDepList != null && lfEmployeeDepList.size()>0){
							for(int j=0;j<lfEmployeeDepList.size();j++){
								depIdsSet.add(lfEmployeeDepList.get(j).getDepId());
							}
						}
					}
				//单个机构，不包含子机构
				}else{
					dep = null;
					dep = baseBiz.getById(LfEmployeeDep.class, Long.valueOf(id));
					if(dep != null){
						depIdsSet.add(dep.getDepId());
					}
				}
			}
			boolean isFlag = false;
			//判断是否包含该机构
			if(depIdsSet.size()>0){
				Long tempDepId = Long.valueOf(depId);
				isFlag = depIdsSet.contains(tempDepId);
			}
			//返回
			if(isFlag){
				response.getWriter().print("depExist");
				return;
			}else{
				response.getWriter().print("noExist");
				return;
			}
		} catch (Exception e)
		{
			response.getWriter().print("");
			EmpExecutionContext.error(e,"静态彩信发送处理机构包含出现异常！");
		}
	}
	
	
	/**
	 *   判断是否 该机构包含选择了的子机构，并且把子机构删除掉
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void isDepContaineDeps(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		try{
			//是否包含关系 0不包含   1包含
			String ismut = request.getParameter("ismut");
			//机构ID
			String depId = request.getParameter("depId");
			//不包含子机构
			if("0".equals(ismut))
			{
				//查询出单个机构下 （不包含子机构人员）的个数
				String number = addrBookAtom.getEmployeeCountByDepId(depId);
				if(number != null && !"".equals(number)){
					if("0".equals(number)){
						response.getWriter().print("nobody");
					}else{
						response.getWriter().print(number);
					}
				}else{
					response.getWriter().print("nobody");
				}
				return;
			}
			//该机构的包含子机构
			List<LfEmployeeDep> lfEmployeeDepList= null;
			LinkedHashSet<Long> depIdSet = new LinkedHashSet<Long>();
			//条件查询
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			//已选好的机构
			String empDepIds=request.getParameter("empDepIds");
			String[] depIds = empDepIds.split(",");
			List<Long> depIdExistList = new ArrayList<Long>(); 
			//循环
			for(int j=0;j<depIds.length;j++)
			{
				String id = depIds[j];
				if(id!=null && !"".equals(id))
				{
					if(id.indexOf("e")>-1)
					{
						if(!"".equals(id.substring(1)))
						{
							depIdExistList.add(Long.valueOf(id.substring(1)));
						}
					}else {
						depIdExistList.add(Long.valueOf(id));
					}
				}
			}
			//查找出要添加的机构的所有子机构，放在一个set里面
			LfEmployeeDep dep = baseBiz.getById(LfEmployeeDep.class, Long.valueOf(depId));
			if(dep != null){
				conditionMap.put("deppath&like",dep.getDeppath());
				conditionMap.put("corpCode", dep.getCorpCode());
				lfEmployeeDepList = baseBiz.getByCondition(LfEmployeeDep.class, conditionMap, null);
				if(lfEmployeeDepList != null && lfEmployeeDepList.size()>0){
					for(int i=0;i<lfEmployeeDepList.size();i++)
					{
						depIdSet.add(lfEmployeeDepList.get(i).getDepId());
					}
					//这里是把包含的机构的ID选择出来
					List<Long> depIdListTemp = new ArrayList<Long>();
					for(int a=0;a<depIdExistList.size();a++)
					{
						if(depIdSet.contains(depIdExistList.get(a)))
						{
							depIdListTemp.add(depIdExistList.get(a));
						}
					}
					//如果确实有几个已经存在的机构是要添加机构的子机构，那么就从新生成select的html
					String depids = depIdSet.toString();
					depids = depids.substring(1,depids.length()-1);
					//计算机构人数
					String countttt = addrBookAtom.getEmployeeCountByDepId(depids);
					if(countttt != null && !"".equals(countttt)){
						if("0".equals(countttt)){
							response.getWriter().print("nobody");
							return;
						}else if(depIdListTemp.size()>0)
						{
							String tempDeps = depIdListTemp.toString();
							tempDeps = tempDeps.substring(1,tempDeps.length()-1);
							response.getWriter().print(countttt+","+tempDeps);
							return;
						}else{
							response.getWriter().print("notContains"+"&"+countttt);
							return;
						}
					}else{
						response.getWriter().print("nobody");
						return;
					}
				}
			}
		}catch (Exception e) {
			EmpExecutionContext.error(e,"静态彩信发送处理机构包含出现异常！");
			response.getWriter().print("errer");
		}
	}
	
	
	/**
	 *   获取员工机构的人员
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void getEmpDepCount(HttpServletRequest request,HttpServletResponse response) throws IOException{
		try{
			//是否包含关系 0不包含   1包含
			String ismut = request.getParameter("ismut");
			//机构ID
			String depId = request.getParameter("depId");
			//不包含子机构
			if("0".equals(ismut))
			{
				//查询出单个机构下 （不包含子机构人员）的个数
				String number = addrBookAtom.getEmployeeCountByDepId(depId);
				if(number != null && !"".equals(number)){
					if("0".equals(number)){
						response.getWriter().print("nobody");
						return;
					}else{
						response.getWriter().print(number);
						return;
					}
				}else{
					response.getWriter().print("nobody");
					return;
				}
			}else if("1".equals(ismut)){
				LfEmployeeDep dep = baseBiz.getById(LfEmployeeDep.class, Long.valueOf(depId));
				if(dep != null){
					LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String, String>();
					conditionMap.put("deppath&like",dep.getDeppath());
					conditionMap.put("corpCode", dep.getCorpCode());
					List<LfEmployeeDep> lfEmployeeDepList = baseBiz.getByCondition(LfEmployeeDep.class, conditionMap, null);
					if(lfEmployeeDepList != null && lfEmployeeDepList.size()>0){
						String idStr = "";
						for(int i=0;i<lfEmployeeDepList.size();i++)
						{
							idStr += lfEmployeeDepList.get(i).getDepId().toString()+",";
						}
						if(!"".equals(idStr) && idStr.length()>0){
							idStr = idStr.substring(0,idStr.length()-1);
							//计算机构人数
							String countttt = addrBookAtom.getEmployeeCountByDepId(idStr);
							response.getWriter().print(countttt);
							return;
						}
					}
				}
			}
			response.getWriter().print("nobody");
			return;
		}catch (Exception e) {
			response.getWriter().print("errer");
			EmpExecutionContext.error(e,"验证员工机构下人数出现异常！");
		}
	}

	
	/**
	 *   这里是检测点击客户机构是否被选择了的机构包含
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void isClientDepContained(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		try
		{
			//点击机构的ID
			String depId = request.getParameter("depId");
			//已经选择好的机构ID
			String clientDepIds=request.getParameter("cliDepIds");
			//解析IDS
			String[] depIds= clientDepIds.split(",");
			//机构集合
			List<LfClientDep> lfClientDepList= null;
			//处理是否包含机构ID的集合
			LinkedHashSet<Long> depIdsSet = new LinkedHashSet<Long>();
			//查询条件集合
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			//机构对象
			LfClientDep dep = null;
			//循环
			for(int i=0;i<depIds.length;i++)
			{
				String id = depIds[i];
				//如果包含了，则说明该机构包含子机构
				if(id == null || "".equals(id)){
					continue;
				}
				//如果相等，则眺出
				if(id.equals(depId))
				{
					response.getWriter().print("depExist");
					return;
				//遇到包含子机构的机构处理操作
				}else if(id.contains("e")){
					Long temp = Long.valueOf(id.substring(1));
					conditionMap.clear();
					dep = null;
					lfClientDepList= null;
					dep = baseBiz.getById(LfClientDep.class, temp);
					if(dep != null){
						conditionMap.put("deppath&like",dep.getDeppath());
						conditionMap.put("corpCode", dep.getCorpCode());
						lfClientDepList = baseBiz.getByCondition(LfClientDep.class, conditionMap, null);
						if(lfClientDepList != null && lfClientDepList.size()>0){
							for(int j=0;j<lfClientDepList.size();j++){
								depIdsSet.add(lfClientDepList.get(j).getDepId());
							}
						}
					}
				//单个机构，不包含子机构
				}else{
					dep = null;
					dep = baseBiz.getById(LfClientDep.class, Long.valueOf(id));
					if(dep != null){
						depIdsSet.add(dep.getDepId());
					}
				}
			}
			boolean isFlag = false;
			//判断是否包含该机构
			if(depIdsSet.size()>0){
				Long tempDepId = Long.valueOf(depId);
				isFlag = depIdsSet.contains(tempDepId);
			}
			//返回
			if(isFlag){
				response.getWriter().print("depExist");
				return;
			}else{
				response.getWriter().print("noExist");
				return;
			}
		} catch (Exception e)
		{
			response.getWriter().print("");
			EmpExecutionContext.error(e,"静态彩信发送处理客户机构包含出现异常！");
		}
	}
	
	
	
	/**
	 *   判断是否 该机客户构包含选择了的子机构，并且把子机构删除掉
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void isClientDepContaineDeps(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		try{
			//是否包含关系 0不包含   1包含
			String ismut = request.getParameter("ismut");
			//机构ID
			String depId = request.getParameter("depId");
			//不包含子机构
			if("0".equals(ismut))
			{
				//查询出单个机构下 （不包含子机构人员）的个数
				//String number = smsBiz.getClientCountByDepId(depId);
				LfClientDep clientDep = baseBiz.getById(LfClientDep.class, depId);
				if(clientDep == null){
					response.getWriter().print("nobody");
					return;
				}
				String number = staMmsBiz.getDepClientCount(clientDep, 2).toString();
				if(number != null && !"".equals(number)){
					if("0".equals(number)){
						response.getWriter().print("nobody");
					}else{
						response.getWriter().print(number);
					}
				}else{
					response.getWriter().print("nobody");
				}
				return;
			}
			//该机构的包含子机构
			List<LfClientDep> lfClientDepList= null;
			LinkedHashSet<Long> depIdSet = new LinkedHashSet<Long>();
			//条件查询
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			//已选好的机构
			String cliDepIds=request.getParameter("cliDepIds");
			String[] depIds = cliDepIds.split(",");
			List<Long> depIdExistList = new ArrayList<Long>(); 
			//循环
			for(int j=0;j<depIds.length;j++)
			{
				String id = depIds[j];
				if(id!=null && !"".equals(id))
				{
					if(id.indexOf("e")>-1)
					{
						if(!"".equals(id.substring(1)))
						{
							depIdExistList.add(Long.valueOf(id.substring(1)));
						}
					}else {
						depIdExistList.add(Long.valueOf(id));
					}
				}
			}
			//查找出要添加的机构的所有子机构，放在一个set里面
			LfClientDep dep = baseBiz.getById(LfClientDep.class, Long.valueOf(depId));
			if(dep != null){
				conditionMap.put("deppath&like",dep.getDeppath());
				conditionMap.put("corpCode", dep.getCorpCode());
				lfClientDepList = baseBiz.getByCondition(LfClientDep.class, conditionMap, null);
				if(lfClientDepList != null && lfClientDepList.size()>0){
					for(int i=0;i<lfClientDepList.size();i++)
					{
						depIdSet.add(lfClientDepList.get(i).getDepId());
					}
					//这里是把包含的机构的ID选择出来
					List<Long> depIdListTemp = new ArrayList<Long>();
					for(int a=0;a<depIdExistList.size();a++)
					{
						if(depIdSet.contains(depIdExistList.get(a)))
						{
							depIdListTemp.add(depIdExistList.get(a));
						}
					}
					
					//如果确实有几个已经存在的机构是要添加机构的子机构，那么就从新生成select的html
					String depids = depIdSet.toString();
					depids = depids.substring(1,depids.length()-1);
					//计算机构人数
					//String countttt = smsBiz.getClientCountByDepId(depids);
					
					String countttt = staMmsBiz.getDepClientCount(dep, 1).toString();
					
					if(countttt != null && !"".equals(countttt)){
						if("0".equals(countttt)){
							response.getWriter().print("nobody");
							return;
						}else if(depIdListTemp.size()>0)
						{
							String tempDeps = depIdListTemp.toString();
							tempDeps = tempDeps.substring(1,tempDeps.length()-1);
							response.getWriter().print(countttt+","+tempDeps);
							return;
						}else{
							response.getWriter().print("notContains"+"&"+countttt);
							return;
						}
					}else{
						response.getWriter().print("nobody");
						return;
					}
				}
			}
		}catch (Exception e) {
			EmpExecutionContext.error(e,"静态彩信发送获取客户机构人数出现异常！");
			response.getWriter().print("errer");
		}
	}
	
	
	
	/**
	 *   获取客户机构的人员
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void getClientDepCount(HttpServletRequest request,HttpServletResponse response) throws IOException{
		try{
			//是否包含关系 0不包含   1包含
			String ismut = request.getParameter("ismut");
			//机构ID
			String depId = request.getParameter("depId");
			//不包含子机构
			if("0".equals(ismut))
			{
				//这里返回机构的总人数
				//查询出单个机构下 （不包含子机构人员）的个数
				//String number = smsBiz.getClientCountByDepId(depId);
				LfClientDep clientDep = baseBiz.getById(LfClientDep.class, depId);
				if(clientDep == null){
					response.getWriter().print("nobody");
					return;
				}
				String number = staMmsBiz.getDepClientCount(clientDep, 2).toString();
				if(number != null && !"".equals(number)){
					if("0".equals(number)){
						response.getWriter().print("nobody");
						return;
					}else{
						response.getWriter().print(number);
						return;
					}
				}else{
					response.getWriter().print("nobody");
					return;
				}
			}else if("1".equals(ismut)){
				LfClientDep dep = baseBiz.getById(LfClientDep.class, Long.valueOf(depId));
				if(dep != null){
					String number = staMmsBiz.getDepClientCount(dep, 1).toString();
					response.getWriter().print(number);
					return;
					/*LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String, String>();
					conditionMap.put("deppath&like",dep.getDeppath());
					conditionMap.put("corpCode", dep.getCorpCode());
					List<LfClientDep> lfClientDepList = baseBiz.getByCondition(LfClientDep.class, conditionMap, null);
					if(lfClientDepList != null && lfClientDepList.size()>0){
						String idStr = "";
						for(int i=0;i<lfClientDepList.size();i++)
						{
							idStr += lfClientDepList.get(i).getDepId().toString()+",";
						}
						if(!"".equals(idStr) && idStr.length()>0){
							idStr = idStr.substring(0,idStr.length()-1);
							//计算机构人数
							String countttt = smsBiz.getClientCountByDepId(idStr);
							response.getWriter().print(countttt);
							return;
						}
					}*/
				}
			}
			response.getWriter().print("nobody");
			return;
		}catch (Exception e) {
			response.getWriter().print("errer");
			EmpExecutionContext.error(e," 静态彩信发送获取客户机构的人员出现异常！");
		}
	}

	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~静态页面进行预览前后的操作 检测
	/**
	 *  预览前，对标题的黑名单   服务器时间  以及上传的彩信文件进行检测
	 * @throws IOException 
	 */
	public void staMMSCheckTimeBlackFile(HttpServletRequest request,HttpServletResponse response) throws IOException{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try{
			//彩信标题
			String tmName = request.getParameter("tmName");
			//企业编码
			String corpCode = request.getParameter("corpCode");
			//彩信文件类型  1选择彩信模板   2上传彩信文件
			String mmsFileType = request.getParameter("mmsFileType");
			//彩信模板ID
			String mmsfileId = request.getParameter("mmsfileId");
			//上传彩信名称
			String mmsFileName = request.getParameter("mmsFileName");
			//发送类型   0即时 还是   1定时
			String sendtype = request.getParameter("sendtype");
			StringBuffer returnMsg = new StringBuffer();
			String dirUrl = txtfileutil.getWebRoot();	
			
			//处理关键字
			String message = tmName.toUpperCase();
			List<String> kwsList = keyWordAtom.getKwInUsed(corpCode);
			if (kwsList != null && kwsList.size() > 0 && !badFilter.checkTextString(message, kwsList)) {
				String c = badFilter.checkText(message);
				returnMsg.append("stage1&").append(c);
				response.getWriter().print(returnMsg);
				return;
			}
			//处理是否文件存在
			if("1".equals(mmsFileType)){
				//选择彩信模板
				LfTemplate template = baseBiz.getById(LfTemplate.class, Long.valueOf(mmsfileId));
				if(template != null){
					String fileUrl = template.getTmMsg();
					String isokDownload = "";
					//判断是否使用集群   以及如果不存在该文件
					if(StaticValue.getISCLUSTER() == 1 && !txtfileutil.checkFile(fileUrl))
					{
						CommonBiz commBiz = new CommonBiz();
						//下载到本地
						if(!"success".equals(commBiz.downloadFileFromFileCenter(fileUrl))){
							isokDownload = "notmsfile";
						}
					}
					if("".equals(isokDownload)){
						File mmsfile = new File(dirUrl+fileUrl);
						if(!mmsfile.exists()){
							returnMsg.append("stage2&nommsfile");
							response.getWriter().print(returnMsg);
							return;
						}
						//是否禁用文件
						if(template.getTmState()!=null && template.getTmState()!=1L)
						{
							returnMsg.append("stage2&noeff");
							response.getWriter().print(returnMsg);
							return;
						}
						//判断彩信tms文件中的关键字
						String templateMsg= new CreateTmsFile().getTmsText(fileUrl);
						if(templateMsg!=null && !"".equals(templateMsg))
						{
							String tmsKeyWord = keyWordAtom.checkText(templateMsg, corpCode);
							if(tmsKeyWord!=null &&!"".equals(tmsKeyWord))
							{
								returnMsg.append("stage4&").append(tmsKeyWord);
								response.getWriter().print(returnMsg);
								return;
							}
						}
					}else{
						returnMsg.append("stage2&nommsfile");
						response.getWriter().print(returnMsg);
						return;
					}
				}else{
					returnMsg.append("stage2&nommsfile");
					response.getWriter().print(returnMsg);
					return;
				}
			}else if("2".equals(mmsFileType)){
				//上传彩信文件
				File mmsfile = new File(dirUrl+mmsFileName);
				if(!mmsfile.exists()){
					returnMsg.append("stage2&nommsfile");
					response.getWriter().print(returnMsg);
					return;
				}
			}
			//定时发送 获取服务器时间返回
			if("1".equals(sendtype)){
				Date date = new Date();
				String serverTime = format.format(date);
				returnMsg.append("stage3&"+serverTime);
				response.getWriter().print(returnMsg);
				return;
			}else{
				response.getWriter().print("success");
				return;
			}
		}catch (Exception e) {
			response.getWriter().print("");
			EmpExecutionContext.error(e,"静态彩信发送提交前验证出现异常！");
		}
	}
	
	
	
	
	/**
	 *   静态彩信页面发送进行预览操作
	 * @param request
	 * @param response
	 */
	public void upNumber(HttpServletRequest request,HttpServletResponse response)throws Exception{
		String result="";
		String langName=request.getParameter("langName");
		try{
			//返回值
			//彩信发送账号
			String spuser = "";
			//员工机构IDS
			String empDepIds = "";
			//客户机构IDS
			String cliDepIds = "";
			//群组IDS
			String groupIds = "";
			//选择对象中的号码串
			String usermoblieStr = "";
			//手工输入的号码STR
			String phoneStr = "";
			//有效号码数
			int effCount = 0;
			//提交总数
			Long subCount = 0L;
			//不符合条件数
			Long badCount = 0L;
			//格式错误数
			Long badModeCount = 0L;
			//重复数
			Long repeatCount = 0L;
			//黑名单数据
			Long blackCount = 0L;
			Date time = Calendar.getInstance().getTime();
			//写合法号码的str
			StringBuffer contentSb = new StringBuffer();
			//写错误号码的str
			StringBuffer badContentSb = new StringBuffer();
			//超过文件大小
			boolean isOverSize = false;
			//上传文件总兆数
			long maxSize = 100L*1024L*1024L;
			//所有文件兆数
			long allFileSize = 0L;
			try{
				//号码文件
				String[] haoduan=msgConfigBiz.getHaoduan();
				//当前登录操作员id
				//Long lguserid= Long.parseLong(request.getParameter("lguserid"));
				//漏洞修复 session里获取操作员信息
				Long lguserid = SysuserUtil.longLguserid(request);

				//当前登录企业
				String lgcorpcode =  null;
				//获取彩信号码文件路径
				String[] url = staMmsBiz.getSaveUrl((int)(lguserid-0), time);
				//判断是否重复list
				HashSet<Long> aa = new HashSet<Long>();
				DiskFileItemFactory factory = new DiskFileItemFactory();
				File Newfile = null;
				factory.setSizeThreshold(1024 * 1024);
				//文件路径前缀
				String temp = url[0].substring(0,url[0].lastIndexOf("/"));
				factory.setRepository(new File(temp));
				ServletFileUpload upload = new ServletFileUpload(factory);
				//表单元素集合
				List<FileItem> fileList = null;
				try
				{
					//以文件方式解析表单
					fileList = upload.parseRequest(request);
				} catch (FileUploadException e)
				{
					EmpExecutionContext.error(e,"彩信静态发送预览解析表单出现异常！");
				}
				
				Iterator<FileItem> it = fileList.iterator();
				List<BufferedReader> readerList = new ArrayList<BufferedReader>();
				FileItem fileItem=null;
				
				//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~循环表单信息 
				//此变量是为了区分上传多个文件时临时文件名称重复的问题
				Integer fileCount=0;
				while (it.hasNext())
				{
					fileItem = (FileItem) it.next();
					String fileName = fileItem.getFieldName();
				//	System.out.println(fileName+"----"+fileItem.getString("UTF-8").toString());
					if (fileName.equals("allinputphone"))
					{
						//电话号码
						phoneStr = fileItem.getString("UTF-8").toString();
					}else if(fileName.equals("empDepIds"))
					{
						//员工机构ids
						empDepIds =fileItem.getString("UTF-8").toString() ;
					}else if(fileName.equals("cliDepIds"))
					{
						//客户机构ids
						cliDepIds =fileItem.getString("UTF-8").toString() ;
					}else if (fileName.equals("groupIds"))
					{
						//群组IDS
						groupIds = fileItem.getString("UTF-8").toString();
					}
					else if(fileName.equals("userMoblieStr")){
						//号码串
						usermoblieStr=fileItem.getString("UTF-8").toString();
					}else if(fileName.equals("lgcorpcode")){
						//企业编码
						lgcorpcode=fileItem.getString("UTF-8").toString();
					}else if(fileName.equals("mmsUser")){
						//企业编码
						spuser=fileItem.getString("UTF-8").toString();
					}else if (!("smmFile").equals(fileName)&&!fileItem.isFormField()&& fileItem.getName().length() > 0 && !"mmsFile".equals(fileItem.getFieldName())){
						//id='smmFile'的type="file" ，谷歌不兼容，将其作为判断
						//上传号码文件
						//判断所有上传文件大小，太大的话就不允许发送
						allFileSize +=fileItem.getSize();
						if(allFileSize > maxSize)
						{
							fileItem.delete();
							fileItem = null;
							isOverSize = true;
							break;
						}
						//将改文件 设null
						Newfile = null;
						//此变量是为了区分上传多个文件时临时文件名称重复的问题
						fileCount++;
						//上传的文件名称
						String fileCurName = fileItem.getName();
						//上传的文件类型格式
						String fileType = fileCurName.substring(fileCurName.lastIndexOf(".")).toLowerCase();
						//检验文件类型的合法性
						if(!fileType.equals(".rar") && !fileType.equals(".zip") && !fileType.equals(".xls")
								&& !fileType.equals(".et") && !fileType.equals(".xlsx") && !fileType.equals(".txt")){
							// 文件类型不合法
							EmpExecutionContext.error("相同内容预览，文件上传失败，文件类型不合法。userId："+ lguserid +"，errCode:" + ErrorCodeInfo.V10003);
							throw new EMPException(ErrorCodeInfo.V10003);
						}
						if(fileType.equals(".xls") || fileType.equals(".et")){
							//如果上传文件是excel2003或者wps表格
							//取的是物理路径
							String FileStr = url[0];
							//将该文件路径由xxx.txt改为xxx_tempX.txt格式
							String FileStrTemp = FileStr.substring(0, FileStr.indexOf(".txt"))+"_temp"+"_"+fileCount+".txt";
							//生成新文件
							Newfile = new File(FileStrTemp);
							FileOutputStream fos=new FileOutputStream(Newfile);  
							OutputStreamWriter osw=new OutputStreamWriter(fos, "GBK");    
							BufferedWriter  bw=new BufferedWriter(osw);
							String phoneNum="";
							String Context="";
							try
							{
								HSSFWorkbook workbook = new HSSFWorkbook(fileItem.getInputStream());
								//循环每张表
								for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
									 HSSFSheet sheet = workbook.getSheetAt(sheetNum);
									 // 循环每一行
									 for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++)
									 {
											HSSFRow row = sheet.getRow(rowNum);
											if (row == null) {
												continue;
											}
											//得到第一列的电话号码
					                        phoneNum = staMmsBiz.getHCellFormatValue(row.getCell(0));
					                        Context="";
					                        //循环每一列（内容以,分隔开来）
											for (int k = 1; k < row.getLastCellNum(); k++) {   
							                        HSSFCell cell = row.getCell(k); 
							                        if(cell !=null && cell.toString().length() >0)
							                        {
													  Context +=","+staMmsBiz.getHCellFormatValue(cell);
							                        }
													
											 }	
											//一行一行的将内容写入到txt文件中。
											bw.write(phoneNum+Context+line);
									}
								}
						        bw.close();   
						        osw.close();    
						        fos.close();
						        FileInputStream fis = new FileInputStream(Newfile);
								BufferedReader br = new BufferedReader(
										new InputStreamReader(fis,"GBK"));
								readerList.add(br);	
							}catch (FileNotFoundException e) {
								EmpExecutionContext.error(e,"彩信文件找不到出现异常！");
							} catch (IOException e) {
								EmpExecutionContext.error(e,"彩信io流出现异常！");
							}catch(Exception e)
							{
								EmpExecutionContext.error(e,"彩信文件读取号码文件出现异常！");
							}
						}else if(fileType.equals(".xlsx")){
							String FileStr = url[0];
							String FileStrTemp = FileStr.substring(0, FileStr.indexOf(".txt"))+"_temp"+"_"+fileCount+".txt";
							Newfile = new File(FileStrTemp);
							FileOutputStream fos=new FileOutputStream(Newfile);  
							OutputStreamWriter osw=new OutputStreamWriter(fos, "GBK");    
							BufferedWriter  bw=new BufferedWriter(osw);
							String phoneNum="";
							String Context="";
							try {
								XSSFWorkbook workbook = new XSSFWorkbook(fileItem.getInputStream());
								//循环每张表
								for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
									XSSFSheet sheet = workbook.getSheetAt(sheetNum);
									// 循环每一行
									for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++)
									{
										XSSFRow row = sheet.getRow(rowNum);
										if (row == null) {
											continue;
										}
				                        phoneNum =  staMmsBiz.getXCellFormatValue(row.getCell(0));
				                        Context="";
										for (int k = 1; k < row.getLastCellNum(); k++) {   
						                        XSSFCell cell = row.getCell(k); 
						                        if(cell !=null && cell.toString().length() >0)
						                        {
												  Context +=","+staMmsBiz.getXCellFormatValue(cell);		
						                        }
										 }	
										bw.write(phoneNum+Context+line);
									}
								}
								bw.close();   
						        osw.close();    
						        fos.close();
						        FileInputStream fis = new FileInputStream(Newfile);
								BufferedReader br = new BufferedReader(new InputStreamReader(fis,"GBK"));
								readerList.add(br);	
							} catch (FileNotFoundException e) {
								EmpExecutionContext.error(e,"彩信文件找不到出现异常！");
							} catch (IOException e) {
								EmpExecutionContext.error(e,"彩信io流出现异常！");
							} catch(Exception e)
							{
								EmpExecutionContext.error(e,"彩信文件读取号码文件出现异常！");
							}
						}else{
							InputStream instream = fileItem.getInputStream();
							String charset = staMmsBiz.get_charset(instream);
							BufferedReader reader = new BufferedReader(new InputStreamReader(fileItem.getInputStream(),charset));
							if(charset.startsWith("UTF-"))
							{
								reader.read(new char[1]);
							}
							readerList.add(reader);
						}
					}
				}
				
				//登录操作员信息
				LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
				//登录操作员信息为空
				if(lfSysuser == null)
				{
					EmpExecutionContext.error("静态彩信预览，从session获取登录操作员信息异常。lfSysuser为null，errCode："+IErrorCode.V10001);
					return;
				}
				//调用彩信的相关方法         操作员、企业编码、SP账号检查
				boolean checkFlag = new CheckUtil().checkMmsSysuserInCorp(lfSysuser, lgcorpcode, spuser, null);
				if(!checkFlag)
				{
					EmpExecutionContext.error("静态彩信预览，检查操作员、企业编码、发送账号不通过，"
							+ "，corpCode:"+lgcorpcode
							+ "，userid："+lfSysuser.getUserId()
							+ "，spUser："+spuser
							+ "，errCode:"+ IErrorCode.V10001);
							return;	
				}
				
			  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ 循环结束
			  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ 解析号码文件	
			    //解析号码文件
				String tmp = "";
				String mobile = "";
				BufferedReader reader = null;
				try {
					for (int r = 0; r < readerList.size(); r++) 
					{
						//如果上传号码大于50w就不允许发送
						
						if (effCount > MAX_SIZE) 
						{
							break;
						}
						reader = readerList.get(r);
						while ((tmp = reader.readLine()) != null) 
						{
							//统计号码的总数量
							subCount++;
							tmp = tmp.trim();
							mobile = StringUtils.parseMobile(tmp);
							if(phoneUtil.checkMobile(mobile, haoduan) != 1)
							{
								//badContentSb.append("格式非法：").append(tmp).append(line);
								if(StaticValue.ZH_HK.equals(langName)){
									badContentSb.append("Illegal format:").append(tmp).append(line);
								}else if(StaticValue.ZH_TW.equals(langName)){
									badContentSb.append("格式非法：").append(tmp).append(line);
								}else{
									badContentSb.append("格式非法：").append(tmp).append(line);
								}

								badModeCount++;
								badCount++;
								continue;
							}else if (blackListBiz.checkMmsBlackList(lgcorpcode, mobile)){
								//badContentSb.append("黑名单号码：").append(tmp).append(line);
								if(StaticValue.ZH_HK.equals(langName)){
									badContentSb.append("Black list number:").append(tmp).append(line);
								}else if(StaticValue.ZH_TW.equals(langName)){
									badContentSb.append("黑名單號碼：").append(tmp).append(line);
								}else{
									badContentSb.append("黑名单号码：").append(tmp).append(line);
								}
								blackCount++;
								badCount++;
								continue;
							}else if (!staMmsBiz.checkRepeat(aa, mobile)){
								//badContentSb.append("重复号码：").append(tmp).append(line);
								if(StaticValue.ZH_HK.equals(langName)){
									badContentSb.append("Repeat number:").append(tmp).append(line);
								}else if(StaticValue.ZH_TW.equals(langName)){
									badContentSb.append("重複號碼：").append(tmp).append(line);
								}else{
									badContentSb.append("重复号码：").append(tmp).append(line);
								}
								repeatCount++;
								badCount++;
								continue;
							} else{
								contentSb.append(mobile).append(line);
								effCount++;
							}
							//1W条存一次
							if (effCount % 10000 == 0 && effCount >= 10000) {
								txtfileutil.writeToTxtFile(url[0], contentSb.toString());
								contentSb = null;
								contentSb = new StringBuffer();
							}
							//1K条存一次
							if (badCount % 1000 == 0 && badCount > 1000) {
								txtfileutil.writeToTxtFile(url[2], badContentSb.toString());
								badContentSb = null;
								badContentSb = new StringBuffer();
							}
						}
						reader.close();
					}
				} catch (Exception e) {
					txtfileutil.deleteFile(url[0]);
					EmpExecutionContext.error(e,"静态彩信发送读取号码文件出现异常！");
					throw e;
				} finally {
					try{
						IOUtils.closeReaders(getClass(), readerList);
					}catch(IOException e){
						EmpExecutionContext.error(e, "");
					}
					readerList.clear();
					readerList = null;
					String FileStr = url[0];
					String fileStr2;
					File file = null;
					//删除临时文件
					for(int j =0;j<fileCount;j++)
					{
						//删除没有经过压缩的excel的临时文件
						Integer b = j+1;
						fileStr2 =  FileStr.substring(0, FileStr.indexOf(".txt"))+"_temp_"+b+".txt";
						file = new File(fileStr2);
						if(file.exists())
						{
                            boolean delete = file.delete();
                            if (!delete) {
                                EmpExecutionContext.error("删除文件失败！");
                            }
                        }
						file = null;
					}
					if(Newfile != null)
					{
                        boolean delete = Newfile.delete();
                        if (!delete) {
                            EmpExecutionContext.error("删除文件失败");
                        }
					}
				}
				//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~处理单个号码以及通讯录
				if (isOverSize) 
				{
					//超出上传文件大小
					result = "overSize";
				}else if(effCount>MAX_SIZE){
					//超过50w，删除上传的文件
					for(int i=0;i<url.length;i++)
					{
						txtfileutil.deleteFile(url[i]);
					}
					result="overstep";
				}else{
					
					//解析号码字符串
					/*	//处理员工str		----保留该方法，处理员工 客户 以及外部人员的的手机号码
					 * 						staMmsBiz.getUserByGuIdStr(employeeIds, "1"); 2代表客户  3 代表外部人员 
					*/
					//这里是将选择对象中的 用户的手机号码  与 主界面所输入的手机号码 想连起来
					if(!"".equals(usermoblieStr) && usermoblieStr.length()>0){
						
						phoneStr = phoneStr + usermoblieStr;
					}
					//处理机构字段
					if(!"".equals(empDepIds) && empDepIds.length()>0)
					{
						phoneStr = staMmsBiz.getEmployeePhoneSrrByDepId(phoneStr, empDepIds,lgcorpcode);
					}
					//处理客户机构
					if(!"".equals(cliDepIds) && cliDepIds.length()>0){
						phoneStr = staMmsBiz.getClientPhoneStrByDepId(phoneStr, cliDepIds,lgcorpcode);
					}
					//处理群组机构
					if(!"".equals(groupIds) && groupIds.length()>0){
						phoneStr = staMmsBiz.getGroupPhoneStrById(phoneStr, groupIds);
					}
					if(phoneStr.length()>0)
					{
						String[] phones = phoneStr.split(",");
						for (String num : phones) 
						{
							num = num.trim();
							if(num.length()==0){
								continue;
								}
							subCount++;
							if(phoneUtil.checkMobile(num, haoduan) != 1)
							{
								
								//badContentSb.append("格式非法：").append(num).append(line);
								if(StaticValue.ZH_HK.equals(langName)){
									badContentSb.append("Illegal format:").append(num).append(line);
								}else if(StaticValue.ZH_TW.equals(langName)){
									badContentSb.append("格式非法：").append(num).append(line);
								}else{
									badContentSb.append("格式非法：").append(num).append(line);
								}
								badModeCount++;
								badCount++;
								continue;
							}else if(blackListBiz.checkMmsBlackList(lgcorpcode, num)){
								//badContentSb.append("黑名单号码：").append(num).append(line);
								if(StaticValue.ZH_HK.equals(langName)){
									badContentSb.append("Black list number:").append(num).append(line);
								}else if(StaticValue.ZH_TW.equals(langName)){
									badContentSb.append("黑名單號碼：").append(num).append(line);
								}else{
									badContentSb.append("黑名单号码：").append(num).append(line);
								}
								blackCount++;
								badCount++;
								continue;
							} else if (!staMmsBiz.checkRepeat(aa, num)){
								//badContentSb.append("重复号码：").append(num).append(line);
								if(StaticValue.ZH_HK.equals(langName)){
									badContentSb.append("Repeat number:").append(num).append(line);
								}else if(StaticValue.ZH_TW.equals(langName)){
									badContentSb.append("重複號碼：").append(num).append(line);
								}else{
									badContentSb.append("重复号码：").append(num).append(line);
								}
								repeatCount++;
								badCount++;
								continue;
							} 
							contentSb.append(num).append(line);
							effCount++;
							if (effCount % 10000 == 0 && effCount >= 10000) {
								txtfileutil.writeToTxtFile(url[0], contentSb.toString());
								contentSb = null;
								contentSb = new StringBuffer();
							}
							//1K条存一次
							if (badCount % 1000 == 0 && badCount > 1000) {
								txtfileutil.writeToTxtFile(url[2], badContentSb.toString());
								badContentSb = null;
								badContentSb = new StringBuffer();
							}	
						}
					}
					if(!"".equals(contentSb.toString()) && contentSb.toString().length()>0){
						txtfileutil.writeToTxtFile(url[0], contentSb.toString());
					}
					if(!"".equals(badContentSb.toString()) && badContentSb.toString().length()>0){
						txtfileutil.writeToTxtFile(url[2], badContentSb.toString());
					}
					
					//如果超过了50W，
					if(effCount>MAX_SIZE){
						result="overstep";
					}else{
						Long maxcount =0L;
						//判断是否计费  1默认计费  2不计费
						String isChargings = "2";
						String dateStr = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
						LfSysuser curSysuser = baseBiz.getLfSysuserByUserId(lguserid);//当前登录操作员对象
						if(curSysuser != null){
							EmpExecutionContext.debug("date:"+dateStr+"    lfsysuser.getuserid:"+curSysuser.getUserId()+";lfsysuser.username:"+curSysuser.getUserName());
						}
						
						String spFeeResult = biz.checkGwFee(spuser, effCount,curSysuser.getCorpCode(),false,2);
						if(biz.IsChargings(lguserid) && ("koufeiSuccess".equals(spFeeResult)||"notneedtocheck".equals(spFeeResult))){
							isChargings = "1";
							//(还需判断计费机制是否开启,只有当开启的情况下才进行下面的判断)这里需要添加一个判断发送总条数是否大于当前机构的可发送的最大条数. add by chenhong 2012.06.12
							EmpExecutionContext.debug("date:"+dateStr+"    lfsysuser.corpCode:"+curSysuser.getCorpCode()+";lfCorp.corpCode:"+lgcorpcode);
							//提供一个可获取最大可发送条数的方法.
							maxcount=biz.getAllowMmsAmount(curSysuser);
							EmpExecutionContext.debug("date:"+dateStr +"     余额:"+maxcount);
							if(maxcount == null){
								maxcount=0L;
							}
							EmpExecutionContext.debug("date:"+dateStr +"     预发送条数:"+effCount);
						}
						
						if(subCount - 0 == 0)
						{
							result="noPhone";
						}else{
							result= String.valueOf(subCount)
							+"&"+String.valueOf(effCount)
							+"&"+String.valueOf(badModeCount)
							+"&"+String.valueOf(repeatCount)
							+"&"+String.valueOf(blackCount)
							+"&"+url[1]
							+"&"+String.valueOf(effCount)
							+"&"+String.valueOf(maxcount)
							+"&"+isChargings+"&"+spFeeResult;
						}
					}
				}
			}catch (Exception e) {
				EmpExecutionContext.error(e,"静态彩信发送预览出现异常！");
			}
		}catch (Exception e) {
			EmpExecutionContext.error(e,"静态彩信发送预览出现异常！");
		} finally{
			request.setAttribute("result", result);
			request.getRequestDispatcher(empRoot + basePath +"/smm_sameMmsPre.jsp").forward(request, response);
		}
	}
	
	
	 /**
	  *   静态彩信发送
	  * @param request
	  * @param response
	  * @throws Exception
	  */
	public void add(HttpServletRequest request,HttpServletResponse response)throws Exception{

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try{
			String result = "";
			String langName = (String) request.getSession().getAttribute(StaticValue.LANG_KEY);
			//System.out.println("静态彩信页面发送！");
			//发送类型  定时还是即时
			String sendType = request.getParameter("sendtype");
			//发送时间
			String sendTime = request.getParameter("sendtime");
			//时间判断
			//特殊处理，如果sendType为空时，则为及时发送。
			if(sendType==null||"".equals(sendType)){
				sendType="0";
			}
			if("1".equals(sendType)){
				//这个代码在linux下不兼容
//				Date date = new Date();
//				SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");
//				String serverTime = format1.format(date);
//				DateFormat df = DateFormat.getDateTimeInstance();
//				sendTime = sendTime+":00";
//				if(!df.parse(serverTime).before(df.parse(sendTime)) && !df.parse(serverTime).equals(df.parse(sendTime))){
//					response.getWriter().print("timeout");
//					return;
//				}
				Date date = Calendar.getInstance().getTime();
				SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");
				String serverTime = format1.format(date);
				sendTime = sendTime+":00";
				if(!format1.parse(serverTime).before(format1.parse(sendTime)) && !format1.parse(serverTime).equals(format1.parse(sendTime))){
					response.getWriter().print("timeout");
					return;
				}
			}
			//任务ID
			String taskId = request.getParameter("taskId");
			//号码文件地址 
			String phoneFileUrl = request.getParameter("phoneFileUrl");
			//用户ID
			//String lguserid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);

			//企业编码
			String corpCode = request.getParameter("lgcorpcode");
			//彩信发送账号
			String mmsSpuser = request.getParameter("mmsSpuser");
			//模板类型   1选择  2上传
			String mmsTemplateType = request.getParameter("mmsTemplateType");
			//彩信文件url
			String mmsFileUrl = request.getParameter("mmsFileUrl");
			//彩信选择模板文件的ID
			String mmsTemplateId = request.getParameter("mmsTemplateId");
			//彩信标题
			String mmsTitle = request.getParameter("mmsTitle");
			//特殊符号处理，•存入数据库乱码
			mmsTitle = mmsTitle.replaceAll("•", "．");
			//彩信主题
			String mmsTaskName= request.getParameter("mmsTaskName");
			//主题为默认时,直接返回(防止重发)
			/*不作为短信内容发送*/
			if(mmsTaskName != null && MessageUtils.extractMessage("ydcx","ydcx_common_text_1",request).equals(mmsTaskName.trim()))
			{
				EmpExecutionContext.error("静态彩讯发送获取参数异常，" + "mmsTaskName:" + mmsTaskName +"，taskId："+taskId);
				response.getWriter().print(ErrorCodeInfo.getInstance(langName).getErrorInfo(IErrorCode.V10001));
				return;
			}
			//特殊符号处理，•存入数据库乱码
			mmsTaskName = mmsTaskName.replaceAll("•", "．");
		    //提交总数
			String subCount=request.getParameter("counts");
			//有效号码数
			String effCount=request.getParameter("effs");
			String opUser ="";
			
			String opType = null;
			String opContent = null;
			
			try {
				//登录操作员信息
				LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
				//登录操作员信息为空
				if(lfSysuser == null)
				{
					EmpExecutionContext.error("静态彩信发送，从session获取登录操作员信息异常。lfSysuser为null，errCode："+IErrorCode.V10001);
					response.getWriter().print(ErrorCodeInfo.getInstance(langName).getErrorInfo(IErrorCode.V10001));
					return;
				}
				//调用彩信的相关方法         操作员、企业编码、SP账号检查
				boolean checkFlag = new CheckUtil().checkMmsSysuserInCorp(lfSysuser, corpCode, mmsSpuser, null);
				if(!checkFlag)
				{
					EmpExecutionContext.error("静态彩信发送，检查操作员、企业编码、发送账号不通过，taskid:"+taskId
							+ "，corpCode:"+corpCode
							+ "，userid："+lfSysuser.getUserId()
							+ "，spUser："+mmsSpuser
							+ "，errCode:"+ IErrorCode.V10001);
					        response.getWriter().print(ErrorCodeInfo.getInstance(langName).getErrorInfo(IErrorCode.V10001));
							return;	
				}

				LfSysuser sysuser = baseBiz.getById(LfSysuser.class, lguserid);
				opUser = sysuser==null?"":sysuser.getUserName();
				
				opType = StaticValue.ADD;
				opContent = "创建彩信任务（任务名称：" +mmsTaskName+ "）";
				
				//创建LfMttask
				LfMttask lfMttask = new LfMttask();
				//是否重发 1-已重发 0-未重发
				lfMttask.setIsRetry(0);
				//信息类型（1-短信 ;2-彩信）
				lfMttask.setMsType(2);
				// 任务说明
				lfMttask.setTaskName(mmsTaskName);
				// 彩信标题
				lfMttask.setTitle(mmsTitle);
				// 彩信账户
				lfMttask.setSpUser(mmsSpuser);
				//填写彩信类型  10普通彩信,11静态模板彩信,12动态模板彩信
				if(mmsTemplateType.equals("1")){
					//静态模板彩信
					lfMttask.setBmtType(11);
					//彩信平台模板ID
					LfTemplate lfTemplate=baseBiz.getById(LfTemplate.class, Long.parseLong(mmsTemplateId));
					lfMttask.setMsg(String.valueOf(lfTemplate.getSptemplid()));
					//设置模板路径
					lfMttask.setTmplPath(lfTemplate.getTmMsg());
				}else{
					//普通彩信
					lfMttask.setBmtType(10);
					//彩信文件URL
					lfMttask.setMsg(mmsFileUrl);
				}
				// 提交状态(创建中1，提交2，取消3)
				lfMttask.setSubState(2); 
				// 下行状态(0-代表新消息（未发送）；1-已发送成功;2-失败;3-未使用;4-发送中)
				lfMttask.setSendstate(0); 
				//MOBILE_TYPE 号码类型（文件上传1或手工输入0）
				lfMttask.setMobileType(1);
				// 号码文件的URL
				lfMttask.setMobileUrl(phoneFileUrl);
				//发送总数
				lfMttask.setSubCount(Long.parseLong(subCount));
				//有效号码数
				lfMttask.setEffCount(Long.parseLong(effCount));
				//操作员ID
				lfMttask.setUserId(Long.parseLong(lguserid));
				//企业编码
				lfMttask.setCorpCode(corpCode); 
				//提交时间
				lfMttask.setSubmitTime(Timestamp.valueOf(new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss").format(new Date())));
				//是否定时发送 1定时 0即时
				lfMttask.setTimerStatus(Integer.parseInt(sendType));
				// 短信定时发送时间
				if ("1".equals(sendType)) {
					lfMttask.setTimerTime(new Timestamp(sdf.parse(sendTime)
							.getTime()));
				}
				//设置taskID
				lfMttask.setTaskId(Long.parseLong(taskId));
				MmsTaskBiz mt = new MmsTaskBiz();
				
				String re = "";
				//判断是否使用集群
				if(StaticValue.getISCLUSTER() ==1 )
				{
					CommonBiz commBiz = new CommonBiz();
					//上传文件到文件服务器
//					if("success".equals(commBiz.uploadFileToFileCenter(phoneFileUrl)))
//					{
//						//删除本地文件
//						//commBiz.deleteFile(phoneFileUrl);
//					}else
//					{
//						re = "uploadFileFail";
//					}
					//设置URI
					String phoneFileUrlUri=commBiz.uploadFileToFileServer(phoneFileUrl);
					lfMttask.setFileuri(phoneFileUrlUri);
					if("".equals(re) && lfMttask.getBmtType() == 10){
						//如果是上传的tms 文件发送 
//						if("success".equals(commBiz.uploadFileToFileCenter(lfMttask.getMsg()))){
//							//删除本地文件
//							//commBiz.deleteFile(lfMttask.getMsg());
//						}else{
//							re = "uploadTmsFail";
//						}
						String msgUri=commBiz.uploadFileToFileServer(lfMttask.getMsg());
						//如果模板和号码文件返回的URI一样，则使用返回的URI；如果不一样，则使用本节点URI
						if(phoneFileUrlUri.equals(msgUri))
						{
							lfMttask.setFileuri(phoneFileUrlUri);
						}else
						{
							lfMttask.setFileuri(StaticValue.BASEURL);
						}
					}
				}else
				{
					//使用本节点地址
					lfMttask.setFileuri(StaticValue.BASEURL);
				}
				if("".equals(re)){
					re = mt.addMmsLfMttask(lfMttask);
				}
				if ("createSuccess".equals(re)) {
					//result = "创建彩信任务及提交到审批流成功！";

					if(StaticValue.ZH_HK.equals(langName)){
						result = "Create MMS tasks and submitted to the approval flow success!";
					}else if(StaticValue.ZH_TW.equals(langName)){
						result = "創建彩信任務及提交到審批流成功！";
					}else{
						result = "创建彩信任务及提交到审批流成功！";
					}
					new SuperOpLog().logSuccessString(opUser, opModule, opType, opContent,
							corpCode);
				} else if ("saveSuccess".equals(re)) {
					//result = "存草稿成功！";
					
					if(StaticValue.ZH_HK.equals(langName)){
						result = "Saved draft success!";
					}else if(StaticValue.ZH_TW.equals(langName)){
						result = "存草稿成功！";
					}else{
						result = "存草稿成功！";
					}
					new SuperOpLog().logSuccessString(opUser, opModule, opType, opContent,
							corpCode);
				} else if ("000".equals(re)) {
					//result = "创建彩信任务及发送到网关成功！";
					result = "000&" + taskId; //创建彩信任务及发送到网关成功
					new SuperOpLog().logSuccessString(opUser, opModule, opType, opContent,
							corpCode);
				} else if ("timerSuccess".equals(re)) {
					//result = "创建彩信任务及定时任务添加成功！";
					
					if(StaticValue.ZH_HK.equals(langName)){
						result = "Create MMS tasks and timing tasks to add success!";
					}else if(StaticValue.ZH_TW.equals(langName)){
						result = "創建彩信任務及定時任務添加成功！";
					}else{
						result = "创建彩信任务及定时任务添加成功！";
					}
					new SuperOpLog().logSuccessString(opUser, opModule, opType, opContent,
							corpCode);
				} else if ("timerFail".equals(re)) {
					//result = "创建定时任务失败，取消任务创建！";
					
					if(StaticValue.ZH_HK.equals(langName)){
						result = "Create a scheduled task failed, cancel the task to create!";
					}else if(StaticValue.ZH_TW.equals(langName)){
						result = "創建定時任務失敗，取消任務創建！";
					}else{
						result = "创建定时任务失败，取消任务创建！";
					}
					new SuperOpLog().logFailureString(opUser, opModule, opType, opContent
							+ opSper, null, corpCode);
				} else if ("timeError".equals(re)) {
					//result = "创建定时任务失败，定时时间不得小于当前时间！";
					
					if(StaticValue.ZH_HK.equals(langName)){
						result = "Failed to create a scheduled task, timing time shall not be less than the current time!";
					}else if(StaticValue.ZH_TW.equals(langName)){
						result = "創建定時任務失敗，定時時間不得小於當前時間！";
					}else{
						result = "创建定时任务失败，定时时间不得小于当前时间！";
					}
					new SuperOpLog().logFailureString(opUser, opModule, opType, opContent
							+ opSper, null, corpCode);
				} else if ("mmsyuebuzu".equals(re)) {
					//result = "彩信余额不足！";
					
					if(StaticValue.ZH_HK.equals(langName)){
						result = "MMS insufficient balance!";
					}else if(StaticValue.ZH_TW.equals(langName)){
						result = "彩信餘額不足！";
					}else{
						result = "彩信余额不足！";
					}
					new SuperOpLog().logFailureString(opUser, opModule, opType, opContent
							+ opSper, null, corpCode);
				} else if ("subMmsErrer".equals(re)) {
					//result = "提交彩信任务失败！";
					
					if(StaticValue.ZH_HK.equals(langName)){
						result = "Failed to submit MMS task!";
					}else if(StaticValue.ZH_TW.equals(langName)){
						result = "提交彩信任務失敗！";
					}else{
						result = "提交彩信任务失败！";
					}
					new SuperOpLog().logFailureString(opUser, opModule, opType, opContent + opSper, null, corpCode);
				}else {
					result = re;
					new SuperOpLog().logFailureString(opUser, opModule, opType, opContent
							+ opSper, null, corpCode);
				}
				
				//增加操作日志
				Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
				if(loginSysuserObj!=null){
					LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
					String contnet=opContent+"，创建结果："+result;
					EmpExecutionContext.info("静态彩信发送", loginSysuser.getCorpCode(), String.valueOf(loginSysuser.getUserId()), loginSysuser.getUserName(), contnet, "OTHER");
				}
				
			}catch(EMPException ex)
			{
				ErrorCodeInfo info = ErrorCodeInfo.getInstance(langName);
				String desc = info.getErrorInfo(ex.getMessage());
				result = desc;
				EmpExecutionContext.error(ex,"静态彩信发送提交出现异常！");
			}catch (Exception ex) {
				result = "error";
				new SuperOpLog().logFailureString(opUser, opModule, opType,
						opContent + opSper, ex, corpCode);
				EmpExecutionContext.error(ex,"静态彩信发送提交出现异常！");
			} finally {
				response.getWriter().print(result);
			}
		}catch (Exception e) {
			EmpExecutionContext.error(e,"静态彩信发送提交出现异常！");
		}
	}
	
	
	
	
	
	/**
	 * 检测文件是否存在
	 * @param request
	 * @param response
	 */
	public void goToFile(HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		//文件地址url
		String url = request.getParameter("url");
		try
		{
			response.getWriter().print(txtfileutil.checkFile(url));
		} catch (Exception e)
		{
			//异常处理
			response.getWriter().print("");
			EmpExecutionContext.error(e,"静态彩信发送检测文件是否存在出现异常！");
		}
	}
	
	
	/**
	 *   获取机构的 数，只 限查子级
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void getEmpSecondDepJson(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		try
		{
			String depId = request.getParameter("depId");
			//从请求获取到当前登录操作员ID，现在不使用
			//String lguserid = request.getParameter("lguserid");
			//从Session中获取当前登录操作员
			LfSysuser loginSysuser=(LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			//从Session中获取当前登录操作员ID
			//String lguserid=String.valueOf(loginSysuser.getUserId());
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);

			//从Session中获取当前登录操作员企业编码
			String corpCode=loginSysuser.getCorpCode();
			List<LfEmployeeDep> empDepList = staMmsBiz.getEmpSecondDepTreeByUserIdorDepId(lguserid,depId,corpCode);
			LfEmployeeDep dep = null;
			StringBuffer tree = new StringBuffer("");
			if(empDepList != null && empDepList.size()>0){
				tree.append("[");
				for (int i = 0; i < empDepList.size(); i++) {
					dep = empDepList.get(i);
					tree.append("{");
					tree.append("id:'").append(dep.getDepId()+"'");
					tree.append(",name:'").append(dep.getDepName()).append("'");
					tree.append(",pId:'").append(dep.getParentId()+"'");
					tree.append(",depcodethird:'").append(dep.getDepcodethird()+"'");
					//tree.append(",dlevel:").append(dep.getDepLevel());
					//tree.append(",depCode:'").append(dep.getDepCode()+"'");
					tree.append(",isParent:").append(true);
					tree.append("}");
					if(i != empDepList.size()-1){
						tree.append(",");
					}
				}
				tree.append("]");
			}
			response.getWriter().print(tree.toString());
		} catch (Exception e)
		{
			response.getWriter().print("");
			EmpExecutionContext.error(e,"获取机构树出现异常！");
		}
	}
	
	
	/**
	 *   获取客户机构的 数，只 限查子级
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void getClientSecondDepJson(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		try
		{
			String depId = request.getParameter("depId");
			//从请求获取到当前登录操作员ID，现在不使用
			//String lguserid = request.getParameter("lguserid");
			//从Session中获取当前登录操作员
			LfSysuser loginSysuser=(LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			//从Session中获取当前登录操作员ID
			//String lguserid=String.valueOf(loginSysuser.getUserId());
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);

			//从Session中获取当前登录操作员企业编码
			String corpCode=loginSysuser.getCorpCode();
			//此方法只查询两级机构
			List<LfClientDep> clientDepList = staMmsBiz.getCliSecondDepTreeByUserIdorDepId(lguserid, depId,corpCode);
			LfClientDep dep = null;
			StringBuffer tree = new StringBuffer("");
			if(clientDepList != null && clientDepList.size()>0){
				tree.append("[");
				for (int i = 0; i < clientDepList.size(); i++) {
					dep = clientDepList.get(i);
					tree.append("{");
					tree.append("id:").append(dep.getDepId()+"");
					tree.append(",name:'").append(dep.getDepName()).append("'");
					tree.append(",depcodethird:'").append(dep.getDepcodethird()).append("'");
					//树数据中加入父机构id
					if(dep.getParentId()-0==0){
						tree.append(",pId:").append(0);
					}else{
						tree.append(",pId:").append(dep.getParentId());
					}
					tree.append(",isParent:").append(true);
					tree.append("}");
					if(i != clientDepList.size()-1){
						tree.append(",");
					}
				}
				tree.append("]");
			}
			response.getWriter().print(tree.toString());
		} catch (Exception e)
		{
			response.getWriter().print("");
			EmpExecutionContext.error(e,"获取客户机构树出现异常！");
		}
	}
	
	
	
	/**
	 *   查询彩信模板
	 * @param request
	 * @param response
	 */
	public void getLfTemplateByMms(HttpServletRequest request, HttpServletResponse response)
	{
		EmpExecutionContext.logRequestUrl(request,"<静态彩信发送查询彩信模板>");
		//查询条件vo
		LfTemplateVo lfTemplateVo = new LfTemplateVo();
		List<LfTemplateVo> templateList = null;
		try {
			PageInfo pageInfo = new PageInfo();
			pageSet(pageInfo,request);
			//模板名称
			String tmName = request.getParameter("tmName");
			//0是静态  1是动态访问
			String tmpltype = request.getParameter("tmpltype");
			
			//String userIdStr = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String userIdStr = SysuserUtil.strLguserid(request);

			if(userIdStr==null||"".equals(userIdStr.trim())||"undefined".equals(userIdStr.trim())){
				EmpExecutionContext.error("静态彩信发送查询彩信模板获取登录操作员ID异常！lguserid="+userIdStr+"。改成从Session获取。");
				LfSysuser loginSysuser=(LfSysuser) request.getSession(false).getAttribute("loginSysuser");
				userIdStr=String.valueOf(loginSysuser.getUserId());
			}
			//当前登录用户id
			Long lguserid = Long.valueOf(userIdStr);
			if (tmName != null)
			{
				tmName = tmName.trim();
				lfTemplateVo.setTmName(tmName);
			}
			Long type = 0L;
			if("1".equals(tmpltype)){
				type = 1L;
			}
			
			//用户用户ID
			//lfTemplateVo.setUserId(Long.valueOf(lguserid));
			//查询静态模板
			lfTemplateVo.setDsflag(type);
			//查询启用的模板
			lfTemplateVo.setTmState(1L);
			//模板（3-短信模板;4-彩信模板）
			lfTemplateVo.setTmpType(4);
			//网关提交状态
			//lfTemplateVo.setSubmitstatus(1);
			lfTemplateVo.setAuditstatus(1);
			pageInfo.setPageSize(10);
			//条件查询结果集
			templateList = staMmsBiz.getTemplateByCondition(lguserid, lfTemplateVo, pageInfo);
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("lfTemplateVo", lfTemplateVo);
			request.setAttribute("temList", templateList);
			request.setAttribute("tmpltype", tmpltype);
			//设置服务器名称
			new ServerInof().setServerName(getServletContext().getServerInfo());
			request.getRequestDispatcher(empRoot + basePath +"/smm_sameMmsTmpl.jsp")
				.forward(request, response);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"查询彩信模板出现异常！");
			request.getSession(false).setAttribute("error", e);
		}
	}
	
	
	// 彩信重新发送

	public void reSend(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		//获取彩信ID
	
		LfMttask oldLfMttask= null;
		LfMttask newLfMttask= new LfMttask();
		try {
			Long mtId = Long.parseLong(request.getParameter("mtId"));
			
			oldLfMttask = new BaseBiz().getById(LfMttask.class, mtId);
			newLfMttask = oldLfMttask;
			newLfMttask.setSubmitTime(new Timestamp(System.currentTimeMillis()));
			newLfMttask.setTimerStatus(0);
			// 重发设置为不定时
			newLfMttask.setSubState(2);
			// 设置为提交中
			newLfMttask.setSendstate(0);
			newLfMttask.setMtId(0L);
			// 设置为未发送
			//产生taskId
			Long taskId = GlobalVariableBiz.getInstance().getValueByKey("taskId", 1L);
			newLfMttask.setTaskId(taskId);
			String returnStr = new MmsTaskBiz().addMmsLfMttask(newLfMttask);
			oldLfMttask.setIsRetry(1);
			//是否重发 1-已重发 0-未重发
			baseBiz.updateObj(oldLfMttask);
			
			String opType = null;
			
			opType = StaticValue.ADD; 
			// "新建"
			//String tempLogCxt = "重新提交彩信任务（任务名称：" + newLfMttask.getTitle() + "）";

			String result = "";
			try {
				if ("createSuccess".equals(returnStr)) {
					result = "创建彩信任务及提交到审核流成功！";
				} else if ("saveSuccess".equals(returnStr)) {
					result = "存草稿成功！";
				} else if ("000".equals(returnStr)) {
					result = "创建彩信任务及发送到网关成功！";
				} else if ("timerSuccess".equals(returnStr)) {
					result = "创建彩信任务及定时任务添加成功！";
				} else if ("timerFail".equals(returnStr)) {
					result = "创建定时任务失败，取消任务创建！";
				} else if ("timeError".equals(returnStr)) {
					result = "创建定时任务失败，定时时间不得小于当前时间！";
					oldLfMttask.setIsRetry(0);
					//是否重发 1-已重发 0-未重发
					baseBiz.updateObj(oldLfMttask);
				} else if ("mmsyuebuzu".equals(returnStr)) {
					result = "彩信余额不足！";
					oldLfMttask.setIsRetry(0);
					//是否重发 1-已重发 0-未重发
					baseBiz.updateObj(oldLfMttask);
				}else if ("subMmsErrer".equals(returnStr)) {
					result = "提交彩信任务失败！";
				} 
				else {
					result = returnStr;
				}
				response.getWriter().print(result);
			} catch (Exception e) {
				EmpExecutionContext.error(e, "彩信重新发送异常！");
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "彩信重新发送异常！");
		}

	}
	
	/**
	 * 高级设置存为默认
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void setDefault(HttpServletRequest request, HttpServletResponse response) throws IOException
	{

		//返回信息
		String result = "fail";
		try {
		//	String lguserid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);

			String lgcorpcode = request.getParameter("lgcorpcode");
			String spUser = request.getParameter("spUser");
			String flag = request.getParameter("flag");
			if(flag == null || "".equals(flag))
			{
				EmpExecutionContext.error("静态彩讯发送高级设置存为默认参数异常！flag:" + flag);
				response.getWriter().print(result);
				return;
			}
			if(lguserid == null || "".equals(lguserid))
			{
				EmpExecutionContext.error("静态彩讯发送高级设置存为默认参数异常！lguserid："+lguserid);
				response.getWriter().print(result);
				return;
			}
			if(spUser == null || "".equals(spUser))
			{
				EmpExecutionContext.error("静态彩讯发送高级设置存为默认参数异常！spUser："+spUser);
				response.getWriter().print(result);
				return;
			}

			//原记录删除条件
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("userid", lguserid);
			conditionMap.put("flag", flag);
			
			//更新对象
			LfDfadvanced lfDfadvanced = new LfDfadvanced();
			lfDfadvanced.setUserid(Long.parseLong(lguserid));
			lfDfadvanced.setSpuserid(spUser);
			lfDfadvanced.setFlag(Integer.parseInt(flag));
			lfDfadvanced.setCreatetime(new Timestamp(System.currentTimeMillis()));

			result = staMmsBiz.setDefault(conditionMap, lfDfadvanced);
			
			//操作结果
			String opResult ="静态彩讯发送高级设置存为默认失败。";
			if(result != null && "seccuss".equals(result))
			{
				opResult = "静态彩讯发送高级设置存为默认成功。";
			}
			//操作员信息
			LfSysuser sysuser = baseBiz.getById(LfSysuser.class, lguserid);
			//操作员姓名
			String opUser = sysuser==null?"":sysuser.getUserName();
			//操作日志信息
			StringBuffer content = new StringBuffer();
			content.append("[SP账号](").append(spUser).append(")");
			
			//操作日志
			EmpExecutionContext.info("静态彩讯发送", lgcorpcode, lguserid, opUser, opResult + content.toString(), "OTHER");
			
			response.getWriter().print(result);
			
		} catch (Exception e) {
			EmpExecutionContext.error(e, "静态彩讯发送高级设置存为默认异常！");
			response.getWriter().print(result);
		}
	}	
	
	
	
}


