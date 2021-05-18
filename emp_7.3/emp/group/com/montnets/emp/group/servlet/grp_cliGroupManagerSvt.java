package com.montnets.emp.group.servlet;



import com.alibaba.fastjson.JSONObject;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.ParamsEncryptOrDecrypt;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.DepDAO;
import com.montnets.emp.common.servlet.util.Excel2007Reader;
import com.montnets.emp.common.servlet.util.Excel2007VO;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.client.LfClient;
import com.montnets.emp.entity.client.LfClientDep;
import com.montnets.emp.entity.group.LfMalist;
import com.montnets.emp.entity.group.LfUdgroup;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.group.biz.GroupManagerBiz;
import com.montnets.emp.group.biz.GrpClientMgrBiz;
import com.montnets.emp.group.vo.LfList2groVo;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.util.ChangeCharset;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.PhoneUtil;
import com.montnets.emp.util.SuperOpLog;
import com.montnets.emp.util.TxtFileUtil;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 个人群组（客户）
 * @author LINZHIHAN
 *
 */
@SuppressWarnings("serial")
public class grp_cliGroupManagerSvt extends grp_baseManagerSvt {

    private final GroupManagerBiz biz = new GroupManagerBiz();
    private final GrpClientMgrBiz cliMgrBiz = new GrpClientMgrBiz();
	private final GroupManagerBiz groupManagerBiz = new GroupManagerBiz();
	public void getDepAndEmpTree1(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		try
		{
			LinkedHashMap<String, String> orderByMap = new LinkedHashMap<String, String>();
			String pageIndex = request.getParameter("pageIndex");
			PageInfo pageInfo = new PageInfo();
			pageSet(pageInfo,request);
			pageInfo.setPageSize(SIZE);
			pageInfo.setPageIndex(Integer.parseInt(pageIndex));
			orderByMap.put("clientId",StaticValue.ASC);
			BaseBiz baseBiz = new BaseBiz();
			String epname = request.getParameter("epname");
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			String depId = request.getParameter("depId");
//			String lgcorpcode = request.getParameter("lgcorpcode");//当前登录企业
			// 防止请求中修改用户信息达到修改用户的目的
			LfSysuser sysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			String lgcorpcode = sysuser==null?"": sysuser.getCorpCode();
	     /*   if(depId!=null&&!"".equals(depId)){
	        	conditionMap.put("depId", depId);
	        }
	        
	        if(epname!=null&&!"".equals(epname)){
	        	conditionMap.put("name&like", epname);
	        }
	        */
	        if((depId==null||"".equals(depId))&&(epname==null||"".equals(epname)))
	        {
	        	response.getWriter().print("");
				return;
	        }
	        
	    	LfClientDep clientDep = null;
			//有机构则带上机构id
			if(depId != null && !"".equals(depId.trim()))
			{
				clientDep = baseBiz.getById(LfClientDep.class, depId);
			}
			
			LfClient client = new LfClient();
			client.setCorpCode(lgcorpcode);
			if(epname!=null&&!"".equals(epname.trim()))
			{
				client.setName(epname);
			}
	        
			StringBuffer sb = new StringBuffer();
			List<DynaBean> LfClientList = cliMgrBiz.getClientsByDepId(clientDep, client, 2, pageInfo);
			if (LfClientList != null && LfClientList.size() > 0) {
			    LinkedHashMap<Long,String> depMap = new LinkedHashMap<Long, String>();
		        //查询出机构对象
		        conditionMap.clear();
		        conditionMap.put("corpCode", lgcorpcode);
		        List<LfClientDep> empDepsList = baseBiz.getByCondition(LfClientDep.class, conditionMap, null);
		        if(empDepsList != null && empDepsList.size()>0){
		        	for(LfClientDep dep :empDepsList){
		        		depMap.put(dep.getDepId(), dep.getDepName());
		        	}
		        }
				sb.append(pageInfo.getTotalPage()).append(",").append(pageInfo.getTotalRec()).append("#");
				for (DynaBean bean : LfClientList)
				{
					Long id = Long.valueOf(bean.get("dep_id")+"");
					sb.append("<option value='").append(String.valueOf(bean.get("guid")))
					.append("' etype='1' mobile='").append(String.valueOf(bean.get("mobile"))).append("'>")
					.append(String.valueOf(bean.get("name")));
					if(depMap.containsKey(id)){
						sb.append(" [").append(depMap.get(id)).append("]");
					}
					sb.append("</option>");
				}
			}
			response.getWriter().print(sb.toString());
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"客户群组获取员工机构树信息出现异常！");
		}
	}
    
    
	/**
	 * 文件上传处理
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException 
	 */
	public void upload(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		List<FileItem> fileList = null;
		PhoneUtil phoneUtil = new PhoneUtil();
		
		StringBuffer sb = new StringBuffer();
		try {
			fileList = upload.parseRequest(request);
		} catch (FileUploadException e) {
			EmpExecutionContext.error(e,"客户群组上传出现异常 ！");
		}
		Iterator<FileItem> it = fileList.iterator();
		HashSet<String> repeatList = new HashSet<String>();
		while (it.hasNext()) {
			Pattern pa = Pattern.compile(" {2,}");
			
			FileItem fileItem = it.next();
			if (!fileItem.isFormField()
					&& fileItem.getName().length() > 0) {
				BufferedReader reader = null;
				String tmp;

				try {
					String[] haoduan = biz.getHaoduan();
					String fileCurName = fileItem.getName();
					String fileType = fileCurName.substring(fileCurName.lastIndexOf("."));
					if(fileType.equalsIgnoreCase(".xls")) {
                        String fileHeader = getFileHeader(fileItem.getInputStream());//文件头
                        if(!"D0CF11E0".equals(fileHeader)){//文件头判断 xls文件头为D0CF11E0
                            continue;
                        }
						Workbook workBook = Workbook.getWorkbook(fileItem.getInputStream());
						Sheet sh = workBook.getSheet(0);
						for (int k = 0; k < sh.getRows(); k++) {
							LfMalist malist = new LfMalist();
							Cell[] cells = sh.getRow(k);
							if (cells.length < 2) {
								continue;
							} else {
								String name = cells[0].getContents();
								if (name != null) {
									name = name.replaceAll("[&|\\|]", "");
								}
								malist.setName("".equals(name) ? "无" : name);
								malist.setMobile(cells[1].getContents());
								if (phoneUtil.getPhoneType(malist.getMobile(), haoduan) != -1
										&& checkRepeat(repeatList, malist.getName() + "#HS#" + malist.getMobile())) {
									sb.append(malist.getName()).append("|").append(malist.getMobile()).append("&");
								}
							}
						}
					}else if(fileType.equalsIgnoreCase(".txt"))
                    {
					
						reader = new BufferedReader(new InputStreamReader(
								fileItem.getInputStream(), "GBK"));
						while ((tmp = reader.readLine()) != null) {
							LfMalist malist = new LfMalist();
							try{
								tmp = tmp.trim();
								Matcher ma = pa.matcher(tmp);
								tmp = ma.replaceAll(" ");
								tmp = tmp.replaceAll("[&|\\|]","");
								int index = tmp.lastIndexOf(" ");
								if(index<1){
									malist.setName("无");
									malist.setMobile(tmp);
								}else{
									malist.setName(tmp.substring(0,index));
									malist.setMobile(tmp.substring(index+1));
								}
								if (phoneUtil.getPhoneType(malist.getMobile(), haoduan) != -1 
										&& checkRepeat(repeatList,malist.getName()+"#HS#"+malist.getMobile())) 
								{
									sb.append(malist.getName()).append("|").append(malist.getMobile()).append("&");
								} 
							}catch (Exception e) {
								EmpExecutionContext.error(e,"客户群组读取号码文件出现异常！");
							}
	
						}
					}else if (fileType.equalsIgnoreCase(".xlsx")) {
                    	//上传文件格式为.xlsx
                    	String uploadPath = StaticValue.FILEDIRNAME;// file/smstxt/
            			String temp = new TxtFileUtil().getWebRoot()+uploadPath;
            			Excel2007VO excel=new Excel2007Reader().fileParset(temp, fileItem.getInputStream());
            			int maxNum = 2000;
            			String[] cells;
        				reader=excel.getReader();
        				String charset = new ChangeCharset().get_charset(fileItem.getInputStream());
        				if(charset.startsWith("UTF-"))
        				{
        					reader.read(new char[1]);
        				}
        				//String tmp="";
        				//计算行数
        				int k=-1;
        				while ((tmp = reader.readLine()) != null && k <= maxNum)
        				{
        					String name="";
        					String phone="";
        					// 循环获得单元行
        					if("".equals(tmp)) {
        						continue;
        					}
        					k++;
        					cells = tmp.split(",");
        					int size = cells.length;
        					//用户名
        					if (size >1){
                                 name = cells[1];
        					}
        					//手机号
        					if (size >2){
        						 phone = cells[2];
        					}
        					//不是两个字段
        					if (size >3) {
        						continue;
        					}
                            //如果名字或手机号为空跳过
                            if(StringUtils.isEmpty(name) || StringUtils.isEmpty(phone)) continue;
                            //如果名字带有特殊符号则直接跳过
                            if(name.matches(".*[\\[\\]\\-<>!@#$%^&*_+='/?,.`~:;\"\\\\].*"))continue;
                            LfMalist malist = new LfMalist();
                            malist.setName(name);
                            malist.setMobile(phone);
                            if (phoneUtil.getPhoneType(malist.getMobile(), haoduan) != -1
									&& checkRepeat(repeatList, malist.getName() + "#HS#" + malist.getMobile())) {
								sb.append(malist.getName()).append("|").append(malist.getMobile()).append("&");
							}
        				}
                    }
				} catch (Exception e) {
					EmpExecutionContext.error(e,"客户群组处理文件属性出现异常！");
				} finally {
					try {
						if(reader!=null)reader.close();
					} catch (IOException e) {
						EmpExecutionContext.error(e,"客户群组关闭文件流出现异常！");
					}
					fileItem.delete();
					request.setAttribute("climasb", sb.toString());
					request.getRequestDispatcher(
							this.empRoot + basePath + "/grp_cliUpGrp.jsp").forward(
							request, response);
				}
			}
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
		EmpExecutionContext.logRequestUrl(request,null);
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		try
		{
			String depId = request.getParameter("depId");
			LfSysuser sysuser  =getLoginUser(request);
			//此方法只查询两级机构
			List<LfClientDep> clientDepList = cliMgrBiz.getCliSecondDepTreeByUserIdorDepId(sysuser, depId);
			LfClientDep dep = null;
			StringBuffer tree = new StringBuffer("");
			if(clientDepList != null && clientDepList.size()>0){
				tree.append("[");
				for (int i = 0; i < clientDepList.size(); i++) {
					dep = clientDepList.get(i);
					tree.append("{");
					tree.append("id:'").append(dep.getDepId()+"'");
					tree.append(",name:'").append(dep.getDepName()).append("'");
					tree.append(",pId:'").append(dep.getParentId()+"'");
					tree.append(",depcodethird:'").append(dep.getDepcodethird()+"'");
					tree.append(",isParent:").append(true);
					tree.append("}");
					if(i != clientDepList.size()-1){
						tree.append(",");
					}
				}
				tree.append("]");
			}
			out.print(tree.toString());
		} catch (Exception e)
		{
			out.print("");
			EmpExecutionContext.error(e,"获取客户群组中客户机构树出现异常！");
		}
	}
	
	/**
	 *  进入修改 群组信息
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void doEditGroupInfo(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String udgId = request.getParameter("udgId");
		String upFlag = request.getParameter("upFlag");
		String returnJson = null;
		LfUdgroup udg = null;
		//群组中的员工信息
		List<LfClient> lfClientList = new ArrayList<LfClient>();
		//群组中非来自共享的自建信息
		List<LfMalist> malistList1 = new ArrayList<LfMalist>();
		//群组中来自共享的自建信息
		List<LfMalist> malistList2 = new ArrayList<LfMalist>();
		try {
			if(udgId == null || "".equals(udgId.trim())){
				EmpExecutionContext.error("编辑客户群组获取群组id异常！id:"+udgId);
				request.setAttribute("isDel", "1");
				return;
			}
			//-----增加解密处理----
			//加密对象
			String uid=getDecryptValue( request, udgId);
			BaseBiz baseBiz = new BaseBiz();
			udg = baseBiz.getById(LfUdgroup.class, uid);
			if(udg == null){
				EmpExecutionContext.error("编辑客户群组查询群组对象为null！");
				request.setAttribute("isDel", "1");
				return;
			}
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			lfClientList = cliMgrBiz.getClientByGuidForGroup(uid, null, conditionMap);
			malistList1 = biz.getMalistListByUdgId(uid, null, conditionMap,0);
			malistList2 = biz.getMalistListByUdgId(uid, null, conditionMap,1);
			String employeeStr =  JSONObject.toJSONString(lfClientList);
			String selfMakeStr =  JSONObject.toJSONString(malistList1);
			String shareStr =  JSONObject.toJSONString(malistList2);
			Map<String, String> map = new HashMap<String, String>();
			map.put("employee", employeeStr);
			map.put("self", selfMakeStr);
			map.put("share", shareStr);
			returnJson = JSONObject.toJSONString(map);
		}catch (Exception e){
			EmpExecutionContext.error(e,"编辑客户群组跳转页面异常！");
		}finally {
			request.setAttribute("malList2", malistList2);
			request.setAttribute("malList1", malistList1);
			request.setAttribute("clientList", lfClientList);
			request.setAttribute("udg", udg);
			request.setAttribute("udgId", udgId);
			if(StringUtils.isNotEmpty(upFlag)){
				response.getWriter().print(returnJson);
			}else{
				request.getRequestDispatcher(this.empRoot + basePath +"/grp_cliEditMember.jsp")
				.forward(request, response);
			}
		}
	}
	
	/**
	 * 解密处理
	 * @param request
	 * @param udgId
	 * @return
	 */
	public String getDecryptValue(HttpServletRequest request, String udgId){
				//-----增加解密处理----
			//加密对象
			ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
			String uid="";
			//加密对象不为空
			if(encryptOrDecrypt != null)
			{
				//解密
				uid = encryptOrDecrypt.decrypt(udgId);
				if(uid == null)
				{
					EmpExecutionContext.error("客户群组参数解密码失败，keyId:"+uid);
					return "";
				}
			}
			else
			{
				EmpExecutionContext.error("客户群组从session中获取加密对象为空！");
				return "";
			}
			return uid;
	}
	
	/**
	 *  修改群组提交
	 * @param request
	 * @param response
	 */
	public void editGroup(HttpServletRequest request, HttpServletResponse response){
		LfSysuser sysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
		String lgcorpcode = sysuser==null?"":sysuser.getCorpCode();
		String opUser = sysuser==null?"":sysuser.getUserName();
		Long userid = sysuser==null?0L:sysuser.getUserId();
		
		//解密处理
		String uid=getDecryptValue( request, request.getParameter("udgId"));
		
		String opType = StaticValue.UPDATE;
		String opContent = "修改群组";
		String curName = "";
		String udgName = "";
		try {
			//处理新添加的记录
			String ygStr = StringUtils.defaultIfEmpty(request.getParameter("ygStr"),"");
			String qzStr = StringUtils.defaultIfEmpty(request.getParameter("qzStr"),"");
			String gxStr = StringUtils.defaultIfEmpty(request.getParameter("gxStr"),"");
			String zjStr = StringUtils.defaultIfEmpty(request.getParameter("zjStr"),"");
			//修改的群组名称
			curName = request.getParameter("curName");
			//群组名称
			udgName = request.getParameter("udgName");

			String result = groupManagerBiz.editGroup(2, curName, udgName, ygStr, qzStr, gxStr, zjStr, userid.toString(), uid, lgcorpcode);

			response.getWriter().print(result);

			//增加操作日志
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null) {
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				EmpExecutionContext.info(opModule, loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), "修改客户群组", "update");
			}
			new SuperOpLog().logSuccessString(opUser, opModule, opType, opContent,lgcorpcode);
		} catch (Exception e) {
			new SuperOpLog().logFailureString(opUser, opModule, opType, opContent, e,lgcorpcode);
			EmpExecutionContext.error(e, "修改群组失败！");
		}
	}


	/**
	 *  共享群组处理
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void groupShare(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String depIdStr = request.getParameter("depIdStr");
//		String lgcorpcode = request.getParameter("lgcorpcode");
		//从session取值，为了防止请求中恶意攻击
		LfSysuser sysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
		String lgcorpcode =sysuser==null?"":sysuser.getCorpCode();
		String groupId = request.getParameter("groupId");
		//String lguserid = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String lguserid = SysuserUtil.strLguserid(request);

		String groupName = request.getParameter("groupName");
		//单个操作员的id
	    String userIdStr1 = request.getParameter("userIdStr");
	    //*****解码处理****
        String uid=getDecryptValue(request,groupId);
		// 解析号码字符串
	    List<Long> userIdList = new ArrayList<Long>();
		if (depIdStr != null && !"".equals(depIdStr)) {
			userIdList = getEmpByDepId2(depIdStr, lgcorpcode);
		}
		List<LfUdgroup> lfUdgroupList = new ArrayList<LfUdgroup>();
		LfUdgroup udgroup = null;
		if(userIdStr1 != null && !"".equals(userIdStr1)){
			String userIdArr[] = userIdStr1.split(",");
			for(int i=0;i<userIdArr.length;i++){
				if(!"".equals(userIdArr[i])&&!userIdList.contains(Long.valueOf(userIdArr[i]))){
					userIdList.add(Long.valueOf(userIdArr[i]));
				}
			}
		}
		if(userIdList.contains(Long.valueOf(lguserid))){
			userIdList.remove(Long.valueOf(lguserid));
		}
		try {
			/**
			 * 	修改时允许共享为空
				if(userIdList == null || userIdList.size()==0){
				response.getWriter().print("noShareSelf");
				return ;
			}
			*/
				if(userIdList==null){userIdList = new ArrayList<Long>();}
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("groupid", uid);
			conditionMap.put("sharetype", "1");
			List<LfUdgroup> oldList = baseBiz.getByCondition(LfUdgroup.class, conditionMap, null);
			List<Long> oldUserId = new ArrayList<Long>();
			List<Long> addList = new ArrayList<Long>();
			String deleteList = "";
			for(LfUdgroup lfUdgroup:oldList){
				oldUserId.add(lfUdgroup.getReceiver());
				if(!userIdList.contains(lfUdgroup.getReceiver())){
					deleteList += lfUdgroup.getUdgId()+",";
				}
			}
			if(!"".equals(deleteList)){
				deleteList = deleteList.substring(0,deleteList.lastIndexOf(","));
			}
			for(Long userid:userIdList){
				if(!oldUserId.contains(userid)){
					addList.add(userid);
				}
			}
			for (Long userid : addList) {
					udgroup = new LfUdgroup();
					udgroup.setUserId(Long.valueOf(lguserid));
					udgroup.setUdgName(groupName);
					udgroup.setGpAttribute(1);
					udgroup.setGroupType(2);
					udgroup.setSharetype(1);
					udgroup.setGroupid(Long.valueOf(uid));
					udgroup.setReceiver(userid);
					udgroup.setSendmode(1);
					//共享状态,设置为1:已共享因共享群组无共享状态,所以设置为3
					udgroup.setShareStatus(3);
					//共享群组的创建时间
					udgroup.setCreateTime(new Timestamp(System.currentTimeMillis()));
					lfUdgroupList.add(udgroup);
			}
			//更新共享状态标志
			LfUdgroup  temp=this.getLfUdgroupByUdgId(uid);
			if(userIdList==null||userIdList.size()==0) {
				temp.setShareStatus(0);
			}else {
				temp.setShareStatus(1);
			}
			if(lfUdgroupList.size()>0 || !"".equals(deleteList)){
//				boolean result = biz.shareGroup(lfUdgroupList, deleteList);
				boolean result = biz.shareGroup(lfUdgroupList, deleteList,temp);
				//增加操作日志
				Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
				if(loginSysuserObj!=null)
				{
					//日志字段
					String field = "[群组ID，群组名称，增加共享人数，删除共享人人数]";
					LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
					int addNum = lfUdgroupList.size();
					String flag = result?"成功":"失败";
					int deleteNum = deleteList.length()>0?deleteList.split(",").length:0;
					String editBeforeDate = "("+uid+"，"+groupName+"，"+addNum+"，"+deleteNum+")";
					String opContent = "共享群组"+flag+"。"+field+editBeforeDate;
					EmpExecutionContext.info(opModule,loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), opContent,"other");
				}
				response.getWriter().print(result);
			}else{
				response.getWriter().print("havingShare");
			}
			
		} catch (Exception e) {
			response.getWriter().print("false");
			EmpExecutionContext.error(e,"客户群组处理共享群组出现异常！");
		}
		
	}

	
	//通过id字符串获取员工成员列表(改)depIds为,e1,3,10,e23,这种类型的字符串
	private List<Long> getEmpByDepId2(String depIds, String corpCode)
	{
		StringBuffer userIds= new StringBuffer();
		List<Long> userId = new ArrayList<Long>();
		BaseBiz baseBiz = new BaseBiz();
		try {
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			String[] tempDepIds = depIds.split(",");
			List<String> depIdsList = Arrays.asList(tempDepIds);
			List<String> depIdsList2= new ArrayList<String>();
			List<String> depIdsList3= new ArrayList<String>();
			for (int a = depIdsList.size() - 1; a >= 0; a--) {
				if (!"".equals(depIdsList.get(a))) {
					if (depIdsList.get(a).indexOf("e") > -1) {
						depIdsList3.add(depIdsList.get(a));// 包含子机构的
					} else {
						//不包含子机构
						depIdsList2.add(depIdsList.get(a));
					}
				}
			}
			StringBuffer buffer = new StringBuffer("");
			List<LfSysuser> sysusers =null;
			int j=0;
			//先遍历不包含子机构的
			for (int i = 0; i < depIdsList2.size(); i++) {
				if (depIdsList2.get(i) != null) {
					buffer.append(depIdsList2.get(i) + ",");
					j++;
				}
				if (j >= 999) {
					j = 0;
					conditionMap.put("depId&in", buffer.toString());
					conditionMap.put("corpCode", corpCode);
					sysusers = baseBiz.getByCondition(LfSysuser.class,
							conditionMap, null);
					if (sysusers != null && sysusers.size() > 0) {
						for (LfSysuser sysuser : sysusers) {
							userIds.append(sysuser.getUserId()).append(",");
							userId.add(sysuser.getUserId());
						}
					}
					buffer = new StringBuffer("");
				} else if (i == depIdsList2.size() - 1) {
					conditionMap.put("depId&in", buffer.toString());
					conditionMap.put("corpCode", corpCode);
					sysusers = baseBiz.getByCondition(LfSysuser.class,
							conditionMap, null);
					if (sysusers != null && sysusers.size() > 0) {
						for (LfSysuser sysuser : sysusers) {
							userIds.append(sysuser.getUserId()).append(",");
							userId.add(sysuser.getUserId());
						}
					}
				}
			}
			//再遍历包含子机构的
			DepDAO depVoDao = new DepDAO();
			//GroupManagerBiz groupManagerBiz = new GroupManagerBiz();
			for (int y = 0; y < depIdsList3.size(); y++) {
				if (depIdsList3.get(y) != null
						&& !"".equals(depIdsList3.get(y))
						&& depIdsList3.get(y).indexOf("e") > -1) {
					String depIdCon =
						depVoDao.getChildUserDepByParentID(
									Long.parseLong(depIdsList3.get(y)
											.substring(1)), TableLfDep.DEP_ID);
					List<LfSysuser> sysuserList = biz
							.getLfSysuserListByDepIdCon(depIdCon);
					for (LfSysuser sysuser : sysuserList) {
						userIds.append(sysuser.getUserId()).append(",");
						userId.add(sysuser.getUserId());
					}
				}
			}
			
		} catch (Exception e) {
			EmpExecutionContext.error(e,"客户群组处理操作员机构失败！");
		}
		return userId;
	}
	
    
    /**
     *  查询群组 详情
     * @param request
     * @param response
     */
	public void showGrpMember(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Long groupId = -1L;
		List<LfList2groVo> userVos = new ArrayList<LfList2groVo>();
		String udgid = request.getParameter("udgid");
		String searchStr = request.getParameter("sysName");
		String sysPhone = request.getParameter("sysPhone");
		PageInfo pageInfo = new PageInfo();
		try {
			if(udgid == null || "".equals(udgid.trim())){
				EmpExecutionContext.error("客户群组详情获取群组id异常！id:"+udgid);
				return;
			}
			//解码处理
			String uid=getDecryptValue( request, udgid);
			LfList2groVo list2groVo = new LfList2groVo();
			if(searchStr!=null && searchStr.length()>0)
			{
				list2groVo.setName(searchStr);
			}

			if(sysPhone != null && sysPhone.length()>0){
				list2groVo.setMobile(sysPhone);
			}
				LfUdgroup udgroup = baseBiz.getById(LfUdgroup.class, Long.valueOf(uid));
				if(udgroup != null){
					if(udgroup.getSharetype() == 0){
						groupId = Long.valueOf(uid);
					}else if(udgroup.getSharetype() == 1){
						groupId = udgroup.getGroupid();
					}
					String pageIndex = request.getParameter("pageIndex");
					pageInfo.setPageIndex(StringUtils.isNotBlank(pageIndex)?Integer.parseInt(pageIndex):1);
					pageInfo.setPageSize(10);
					List<LfList2groVo> vos = cliMgrBiz.getClientShowMember(pageInfo,list2groVo,groupId);
					if(vos != null && vos.size()>0){
						for(int i=0;i<vos.size();i++){
							LfList2groVo vo = vos.get(i);
							if((udgroup.getSharetype() == 1 && vo.getL2gtype() == 2) || vo.getSharetype() == 1){
								String phone = vo.getMobile();
								phone = phone.substring(0,3)+"*****"+phone.substring(8);
								vo.setMobile(phone);
							}
							userVos.add(vo);
						}
					}
				}else{
					EmpExecutionContext.error("客户群组详情获取群组对象为null！");
					return;
				}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"客户群组查询客户群组详情出现异常！");
		}finally {
			request.setAttribute("groupId",groupId);
			request.setAttribute("name", searchStr);
			request.setAttribute("sysPhone", sysPhone);
			request.setAttribute("vos", userVos);
			request.setAttribute("pageInfo",pageInfo);
			request.getRequestDispatcher(
					this.empRoot + basePath +"/grp_cliShowMember.jsp").forward(
					request, response);
		}
	}
}
