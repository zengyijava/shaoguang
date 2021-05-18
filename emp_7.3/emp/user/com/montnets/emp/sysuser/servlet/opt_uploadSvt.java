package com.montnets.emp.sysuser.servlet;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.GlobalVariableBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfRoles;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.security.wgmsgconfig.WgMsgConfigBiz;
import com.montnets.emp.sysuser.bean.OptUpload;
import com.montnets.emp.sysuser.biz.SysuserPriBiz;
import com.montnets.emp.sysuser.dao.DepExportDao;
import com.montnets.emp.util.MD5;
import com.montnets.emp.util.PhoneUtil;

public class opt_uploadSvt extends BaseServlet {
	private static final long OPT_UPLOAD_MAX = 1000;
	private static final String PATH = "/user/operator";
	
	private final BaseBiz baseBiz = new BaseBiz();
	private final WgMsgConfigBiz msgConfigBiz = new WgMsgConfigBiz();
	private final PhoneUtil phoneUtil = new PhoneUtil();
	private static GlobalVariableBiz globalBiz = GlobalVariableBiz.getInstance();
	private final DepExportDao depExportDao = new DepExportDao();
	
	/**
	 * @param request
	 * @param response
	 */
	//上传操作员文件
	public void uploadOpt(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 获取错误码的map
		LinkedHashMap<String, String> errorCodeMap = getErrorCodeMap(request);
		
		// 添加与日志相关
		// 设置日期格式
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		// 解析上传文件开始时间
		long startTimeByLog = System.currentTimeMillis();  
		
		// 导入总数
		int total = 0;
		// 导入成功条数
		int success = 0;
		// 导入失败条数
		int fail = 0;
		OptUpload optUpload = new OptUpload();
		optUpload.setTotal(total);
		optUpload.setSuccess(success);
		optUpload.setFail(fail);
		
		// 创建DiskFileItemFactory
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// 创建文件上传解析器
		ServletFileUpload upload = new ServletFileUpload(factory);
		List<FileItem> fileList = null;
		try {
			//解析上传数据
			fileList = upload.parseRequest(request);
			
			// 添加日志，上传文件有时会出现“FileUploadException:Read timed out”文件上传超时的异常，因此添加日志记录解析上传文件的耗时，看看耗时多久会出现超时的情况，从而进一步排查问题
			// 解析上传文件结束时间
			long endTimeByLog = System.currentTimeMillis();  
			// 解析上传文件耗时
			long consumeTimeByLog = endTimeByLog - startTimeByLog;  
			
			Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
			if (loginSysuserObj!=null) {
				LfSysuser loginSysuser = (LfSysuser)loginSysuserObj;
				String opContent1 = "上传操作员文件，上传文件解析开始时间："+sdf.format(startTimeByLog)+"，耗时："+consumeTimeByLog+"ms";
				
				EmpExecutionContext.info("操作员导入", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
						loginSysuser.getUserName(), opContent1, "GET");
			}
		} catch (FileUploadException e) {
			// 添加日志，上传文件有时会出现“FileUploadException:Read timed out”文件上传超时的异常，因此添加日志记录解析上传文件的耗时，看看耗时多久会出现超时的情况，从而进一步排查问题
			long endTimeByLog = System.currentTimeMillis();  //解析上传文件结束时间
			long consumeTimeByLog = endTimeByLog - startTimeByLog;  //解析上传文件耗时
			String opContent1 = "上传操作员文件出现异常 ！，上传文件解析开始时间："+sdf.format(startTimeByLog)+"，耗时："+consumeTimeByLog+"ms";
			
			EmpExecutionContext.error(e, opContent1);
		}
		Iterator<FileItem> it = fileList.iterator();
		// 获取企业编码
		String lgcorpcode = "";
		
		// 获取登录sysuser
		LfSysuser sysUser = getLoginUser(request);
		if(sysUser == null){
			EmpExecutionContext.error("操作员管理,uploadOpt方法session中获取当前登录对象出现异常");
			return;
		}
		lgcorpcode = sysUser.getCorpCode();
		// 判断企业编码获取
		if(lgcorpcode == null||"".equals(lgcorpcode.trim())){
			EmpExecutionContext.error("操作员管理,uploadOpt方法session中获取企业编码出现异常");
			return;
		}
		
		// 上传失败记录
		LinkedHashMap<Integer, String> errorMap = new LinkedHashMap<Integer, String>();
		
		while (it.hasNext()) {
			FileItem fileItem = it.next();
			
			// 对上传文件进行处理
			if(!fileItem.isFormField() && fileItem.getName().length() > 0) {
				try {
					// 上传文件名称
					String fileCurName = fileItem.getName();
					// 文件类型
					String fileType = fileCurName.substring(fileCurName.lastIndexOf("."));
					// 如果是.xls文件或者.xlsx文件
					if(fileType.equals(".xls")) {
						parsexls(request, lgcorpcode, fileItem, errorCodeMap, errorMap, optUpload, sysUser);
					} else if (fileType.equals(".xlsx")) {
						parsexlsx(request, lgcorpcode, fileItem, errorCodeMap, errorMap, optUpload, sysUser);
					}
				} catch (Exception e) {
					EmpExecutionContext.error(e, "上传操作员文件出现异常！");
					request.setAttribute("result", "false");
				} finally {
					// 删除文件
					if (fileItem != null) {						
						fileItem.delete();
					}
					request.setAttribute("errorMap", errorMap);
					
					SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					optUpload.setTime(ymd.format(new Date()));
					request.setAttribute("optUpload", optUpload);
					request.getRequestDispatcher(PATH +"/opt_importSysuser.jsp")
						.forward(request, response);
				}
			}
		}
	}
	
	/**
	 * 获取操作员userid
	 * @description    
	 * @param request
	 * @return       			 
	 * @datetime 2018-12-5 上午09:00:00
	 */
	public Long getOpUserId(HttpServletRequest request) {
		Long opUserId = 0L;
		try {
			LfSysuser sysuser = (LfSysuser)  request.getSession(false).getAttribute("loginSysuser");
			if(sysuser != null){
				opUserId = sysuser.getUserId();
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "session获取操作员userid异常，session为空！");
		}
		return opUserId;
	}
	
	/**
	 * 获取操作员所属机构
	 * @description    
	 * @param request
	 * @return       			 
	 * @datetime 2018-12-15 19:00:00
	 */
	public String getDepPath(HttpServletRequest request) {
		String depPath = "";
		try {
			LfSysuser sysuser = (LfSysuser)  request.getSession(false).getAttribute("loginSysuser");
			if(sysuser != null) {
				Long depId = sysuser.getDepId();
				LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
				map.put("depId", "" + depId);
				List<LfDep> lfDepList = baseBiz.findListByCondition(LfDep.class, map, null);
				if (lfDepList != null && lfDepList.size() > 0) {
					depPath = lfDepList.get(0).getDeppath();
				}
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "session获取操作员userid异常，session为空！");
		}
		return depPath;
	}
	
	/**
	 * 解析xls文件
	 * @param request
	 * @param lgcorpcode
	 * @param workBook
	 * @param fileItem
	 * @param errorCodeMap
	 * @param errorMap
	 * @param optUpload
	 * @param sysUser
	 * @throws Exception
	 */
	public void parsexls(HttpServletRequest request, String lgcorpcode, FileItem fileItem, LinkedHashMap<String, String> errorCodeMap, 
			LinkedHashMap<Integer, String> errorMap,OptUpload optUpload,LfSysuser sysUser) throws Exception {
		int success = optUpload.getSuccess();
		int fail = optUpload.getFail();
		int total = optUpload.getTotal();
		//excel有效行数
		int rows = 0;
		// 操作员登录账号
		Set<String> usernameSet = new HashSet<String>();
		// 操作员编码
		Set<String> userCodeSet = new HashSet<String>();
		
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("corpCode", lgcorpcode);
		List<LfSysuser> list = baseBiz.findListByCondition(LfSysuser.class, conditionMap, null);
		// 遍历数据库中的操作员信息
		if (list != null && list.size() > 0) {
			for (int i=0; i<list.size(); i++) {
				LfSysuser user = list.get(i);
				if(user.getUserName() != null && user.getUserName().trim().length() > 0) {					
					usernameSet.add(user.getUserName());
				}
				if (user.getUserCode() != null && user.getUserCode().trim().length() > 0) {					
					userCodeSet.add(user.getUserCode());
				}
			}
		}
		
		Long userId = getOpUserId(request);
		
		List<LfDep> depList = depExportDao.findByUserId(userId);
		// 获取操作员数据权限所在机构路径
		String depPath = "";
		if (depList != null && depList.size() > 0) {
			depPath = depList.get(0).getDeppath();
		}
		// 查看当前登录操作员是机构权限或者个人权限
		int permissionType = 1;
		
		if (sysUser.getPermissionType() != null) {
			permissionType = sysUser.getPermissionType();
		}
		
		// 解析xls文件
		HSSFWorkbook hssfWorkBook = new HSSFWorkbook(fileItem.getInputStream());
		HSSFSheet hssfSheet = hssfWorkBook.getSheetAt(0);
		rows = hssfSheet.getLastRowNum() - 1;
		
		// 一次导入的操作员超过最大数目
		if (rows < 1) {
			request.setAttribute("result", "no");			
		} else if (rows > OPT_UPLOAD_MAX) {
			request.setAttribute("result", "maxopt");
		} else {
			for (int j=2; j<rows+2; j++) {
				HSSFRow row = hssfSheet.getRow(j);
				// 总数+1
				total ++;
				optUpload.setTotal(total);
				// 判断该行是否为空
				if (row == null) {
					errorMap.put(j+1, errorCodeMap.get("lineIsEmpty"));
					fail ++;
					optUpload.setFail(fail);
					continue;
				}
				
				LfSysuser lfsysuser = new LfSysuser();
				// 登录账号
				String username = ((String)getHSSFCellFormatValue(row.getCell(0))).trim();
				// 操作员名称
				String name = ((String)getHSSFCellFormatValue(row.getCell(1))).trim();
				// 操作员编码
				String userCode = ((String)getHSSFCellFormatValue(row.getCell(2))).trim();
				// 所属机构
				String depCodeThird = ((String)getHSSFCellFormatValue(row.getCell(3))).trim();
				// 性别
				String sex = ((String)getHSSFCellFormatValue(row.getCell(4))).trim();
				// 手机
				String mobile = ((String)getHSSFCellFormatValue(row.getCell(5))).trim();
				// 角色
				String role = ((String)getHSSFCellFormatValue(row.getCell(6))).trim();
				
				// 验证登录账号
				// 1.登录账号为空
				if (username == null || username.length() == 0) {
					errorMap.put(j+1, errorCodeMap.get("userNameUnfilled"));
					fail ++;
					optUpload.setFail(fail);
					continue;
				}
				// 2.判断登录账号是否重复
				if (usernameSet.contains(username)) {
					errorMap.put(j+1, errorCodeMap.get("userNameRepeat"));
					fail ++;
					optUpload.setFail(fail);
					continue;
				}
				// 3.登录账号超长（超过15位）
				int userNameLength = 15;
				if (username.length() > userNameLength) {
					errorMap.put(j+1, errorCodeMap.get("userNameFormatErr"));
					fail ++;
					optUpload.setFail(fail);
					continue;
				}
				// 4.登录账号是否为数字字母下划线
				Pattern pat = Pattern.compile("^[A-Za-z0-9_]+$");
				//如果为true，匹配正确
				boolean isTrue = pat.matcher(username).matches();
				if (!isTrue) {
					errorMap.put(j+1, errorCodeMap.get("userNameFormatErr"));
					fail ++;
					optUpload.setFail(fail);
					continue;
				}
				lfsysuser.setUserName(username);
				
				// 验证操作员名称
				// 1.验证操作员名称为空
				if (name == null || name.length() == 0) {
					errorMap.put(j+1, errorCodeMap.get("nameUnfilled"));
					fail ++;
					optUpload.setFail(fail);
					continue;
				}
				// 2.验证操作员名称超长（超过30位）
				int nameLength = 30;
				if (name.length() > nameLength) {
					errorMap.put(j+1, errorCodeMap.get("nameFormatErr"));
					fail ++;
					optUpload.setFail(fail);
					continue;
				}
				lfsysuser.setName(name);
				
				// 验证操作员编码
				// 1.判断操作员编码是否为空
				if (userCode == null || userCode.length() == 0) {
					errorMap.put(j+1, errorCodeMap.get("userCodeUnfilled"));
					fail ++;
					optUpload.setFail(fail);
					continue;
				}
				// 2.判断操作员编码是否重复
				if (userCodeSet.contains(userCode)) {
					errorMap.put(j+1, errorCodeMap.get("userCodeRepeat"));
					fail ++;
					optUpload.setFail(fail);
					continue;
				}
				// 3.判断操作员编码是否超过20位
				int userCodeLength = 20;
				if (userCode.length() > userCodeLength) {
					errorMap.put(j+1, errorCodeMap.get("userCodeFormatErr"));
					fail ++;
					optUpload.setFail(fail);
					continue;
				}
				// 4.判断操作员编码是否为数字或者字母
				Pattern p = Pattern.compile("^[A-Za-z0-9]+$");
				//如果为true，匹配正确
				boolean boo = p.matcher(userCode).matches();
				if(!boo) {
					errorMap.put(j+1, errorCodeMap.get("userCodeFormatErr"));
					fail ++;
					optUpload.setFail(fail);
					continue;
				}
				lfsysuser.setUserCode(userCode);	
					
				// 验证机构编码
				// 1.机构编码为空
				if (depCodeThird == null || depCodeThird.length() == 0) {
					errorMap.put(j+1, errorCodeMap.get("depCodeUnfilled"));
					fail ++;
					optUpload.setFail(fail);
					continue;
				}
				// 获取机构id
				LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
				map.put("depCodeThird", depCodeThird);
				map.put("corpCode", lgcorpcode);
				map.put("depState", "1");
				List<LfDep> lfDepList = baseBiz.findListByCondition(LfDep.class, map, null);
				
				// 2.判断机构编码是否存在
				if (lfDepList == null || lfDepList.size() == 0) {
					errorMap.put(j+1, errorCodeMap.get("depCodeNotExist"));
					fail ++;
					optUpload.setFail(fail);
					continue;
				}
				// 3.判断机构编码有无导入权限
				if (lfDepList.get(0).getDepState() == 1 && lgcorpcode.equals(lfDepList.get(0).getCorpCode().trim())
						&& depPath.length() > 0 && permissionType != 1 && lfDepList.get(0).getDeppath().startsWith(depPath)) {							
					lfsysuser.setDepId(lfDepList.get(0).getDepId());
				} else {
					errorMap.put(j+1, errorCodeMap.get("depCodePowerErr"));
					fail ++;
					optUpload.setFail(fail);
					continue;
				}
				
				// 验证性别
				// 1.性别是否为空
				if (sex == null || sex.length() == 0) {
					errorMap.put(j+1, errorCodeMap.get("sexUnfilled"));
					fail ++;
					optUpload.setFail(fail);
					continue;
				}
				// 判断性别
				String female = com.montnets.emp.i18n.util.MessageUtils.extractMessage("user", "user_xtgl_czygl_text_11", request);
				if (female.equals(sex)) {											
					lfsysuser.setSex(0);
				} else {
					lfsysuser.setSex(1);
				}
				
				// 验证电话号码
				// 1.验证电话号码是否为空
				if (mobile == null || mobile.length() == 0) {
					errorMap.put(j+1, errorCodeMap.get("phoneUnfilled"));
					fail ++;
					optUpload.setFail(fail);
					continue;
				}
				// 获取运营商号码段
				String[] haoduan = msgConfigBiz.getHaoduan();
				// 2.验证电话号码是否非法
				if (phoneUtil.getPhoneType(mobile, haoduan) < 0) {
					errorMap.put(j+1, errorCodeMap.get("phoneFormatErr"));
					fail ++;
					optUpload.setFail(fail);
					continue;
				}
				lfsysuser.setMobile(mobile);
			
				// 验证角色
				// 1.判断角色是否为空
				if (role == null || role.length() == 0) {
					errorMap.put(j+1, errorCodeMap.get("roleUnfilled"));
					fail ++;
					optUpload.setFail(fail);
					continue;
				}
				// 2.判断角色填写是否非法
				String roleSplit[] = role.split(",");
				String roles[] = arrayRemoveSameElements(roleSplit);
				StringBuffer srolesid = new StringBuffer();
				// 操作员赋权
				LinkedHashMap<String, String> roleMap = new LinkedHashMap<String, String>();
				for (int i=0; i<roles.length; i++) {
					roleMap.clear();
					roleMap.put("corpCode", lgcorpcode);
					roleMap.put("roleName", roles[i].trim());
					List<LfRoles> rolesList = baseBiz.findListByCondition(LfRoles.class, roleMap, null);
					if (rolesList != null && rolesList.size() > 0) {
						srolesid.append(rolesList.get(0).getRoleId()).append(",");
					}
				}
				if (srolesid == null || srolesid.length() == 0) {
					errorMap.put(j+1, errorCodeMap.get("roleFormatErr"));
					fail ++;
					optUpload.setFail(fail);
					continue;
				}
				
				// 设置导入操作员信息的默认值
				lfsysuser.setIsExistSubNo(2);
				String password = username;
				lfsysuser.setControlFlag("" + 0000);
				lfsysuser.setShowNum(0);
				lfsysuser.setOph("");
				lfsysuser.setQq("");
				lfsysuser.setEMail("");
				lfsysuser.setFax("");
				lfsysuser.setGuId(globalBiz.getValueByKey("guid", 1L));
				lfsysuser.setUserState(1);
				lfsysuser.setHolder(sysUser.getName());
				lfsysuser.setCorpCode(lgcorpcode);
				lfsysuser.setPermissionType(1);
				lfsysuser.setMsn("");
				lfsysuser.setDuties("");
				lfsysuser.setComments("");
				lfsysuser.setIsReviewer(2);
				lfsysuser.setIsAudited(2);
				lfsysuser.setClientState(0);
				lfsysuser.setEmployeeId(0l);
				lfsysuser.setManualInput(0);
				lfsysuser.setOnLine(0);
				lfsysuser.setPid(0);
				lfsysuser.setPostId(0l);
				lfsysuser.setUdgId(0);
				lfsysuser.setUpGuId(0l);
				lfsysuser.setUserType(0);
				lfsysuser.setWorkState(0);
				lfsysuser.setPassword(MD5.getMD5Str(password + username.toLowerCase()));
				lfsysuser.setPwdupdatetime(new Timestamp(System.currentTimeMillis()));
				// 获取角色的roleid
				String rolesid[] = srolesid.substring(0, srolesid.length()-1).toString().split(",");
				Long roleIds[] = new Long[rolesid.length];
				for (int i=0; i<roleIds.length; i++) {
					roleIds[i] = Long.valueOf(rolesid[i]);
				}
				Long opUserId = getOpUserId(request);
				// 操作员导入数据库操作
				boolean result = new SysuserPriBiz().add(null,roleIds,lfsysuser,null,null,null,null,opUserId);
				if (result) {
					// 成功
					success ++;
					optUpload.setSuccess(success);
					// 添加成功，将操作员登录账号和操作员编码加入set中
					usernameSet.add(username);
					userCodeSet.add(userCode);
				}else {	
					// 失败 
					errorMap.put(j+1, errorCodeMap.get("unknownErr"));
					fail ++;
					optUpload.setFail(fail);
					continue;
				}
			}
		}
	}
	
	

	/**
	 * 解析xlsx文件
	 * @param request
	 * @param lgcorpcode
	 * @param fileItem
	 * @param errorCodeMap
	 * @param errorMap
	 * @param optUpload
	 * @param sysUser
	 */
	public void parsexlsx(HttpServletRequest request, String lgcorpcode, FileItem fileItem, LinkedHashMap<String, String> errorCodeMap,
			LinkedHashMap<Integer, String> errorMap, OptUpload optUpload, LfSysuser sysUser) throws Exception {
		int success = optUpload.getSuccess();
		int fail = optUpload.getFail();
		int total = optUpload.getTotal();
		// excel有效行数
		int rows = 0;
		// 操作员登录账号
		Set<String> usernameSet = new HashSet<String>();
		// 操作员编码
		Set<String> userCodeSet = new HashSet<String>();
		
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("corpCode", lgcorpcode);
		List<LfSysuser> list = baseBiz.findListByCondition(LfSysuser.class, conditionMap, null);
		// 遍历数据库中的操作员信息
		if (list != null && list.size() > 0) {
			for (int i=0; i<list.size(); i++) {
				LfSysuser user = list.get(i);
				if(user.getUserName() != null && user.getUserName().trim().length() > 0) {					
					usernameSet.add(user.getUserName());
				}
				if (user.getUserCode() != null && user.getUserCode().trim().length() > 0) {					
					userCodeSet.add(user.getUserCode());
				}
			}
		}
		
		Long userId = getOpUserId(request);
		
		List<LfDep> depList = depExportDao.findByUserId(userId);
		// 获取操作员数据权限所在机构路径
		String depPath = "";
		if (depList != null && depList.size() > 0) {
			depPath = depList.get(0).getDeppath();
		}
		// 查看当前登录操作员是机构权限或者个人权限
		int permissionType = 1;
		
		if (sysUser.getPermissionType() != null) {
			permissionType = sysUser.getPermissionType();
		}
		
		// 解析xlsx文件
		XSSFWorkbook xssfWorkBook = new XSSFWorkbook(fileItem.getInputStream());
		XSSFSheet xssfSheet = xssfWorkBook.getSheetAt(0);
		rows = xssfSheet.getLastRowNum() - 1;
		
		// 一次导入的操作员超过最大数目
		if (rows < 1) {
			request.setAttribute("result", "no");			
		} else if (rows > OPT_UPLOAD_MAX) {
			request.setAttribute("result", "maxopt");
		} else {
			for (int j=2; j<rows+2; j++) {
				XSSFRow row = xssfSheet.getRow(j);
				// 总数+1
				total ++;
				optUpload.setTotal(total);
				// 判断该行是否为空
				if (row == null) {
					errorMap.put(j+1, errorCodeMap.get("lineIsEmpty"));
					fail ++;
					optUpload.setFail(fail);
					continue;
				}
				
				LfSysuser lfsysuser = new LfSysuser();
				// 登录账号
				String username = ((String)getXSSFCellFormatValue(row.getCell(0))).trim();
				// 操作员名称
				String name = ((String)getXSSFCellFormatValue(row.getCell(1))).trim();
				// 操作员编码
				String userCode = ((String)getXSSFCellFormatValue(row.getCell(2))).trim();
				// 所属机构
				String depCodeThird = ((String)getXSSFCellFormatValue(row.getCell(3))).trim();
				// 性别
				String sex = ((String)getXSSFCellFormatValue(row.getCell(4))).trim();
				// 手机
				String mobile = ((String)getXSSFCellFormatValue(row.getCell(5))).trim();
				// 角色
				String role = ((String)getXSSFCellFormatValue(row.getCell(6))).trim();
				
				// 验证登录账号
				// 1.登录账号为空
				if (username == null || username.length() == 0) {
					errorMap.put(j+1, errorCodeMap.get("userNameUnfilled"));
					fail ++;
					optUpload.setFail(fail);
					continue;
				}
				// 2.判断登录账号是否重复
				if (usernameSet.contains(username)) {
					errorMap.put(j+1, errorCodeMap.get("userNameRepeat"));
					fail ++;
					optUpload.setFail(fail);
					continue;
				}
				// 3.登录账号超长（超过15位）
				int userNameLength = 15;
				if (username.length() > userNameLength) {
					errorMap.put(j+1, errorCodeMap.get("userNameFormatErr"));
					fail ++;
					optUpload.setFail(fail);
					continue;
				}
				// 4.登录账号是否为数字字母下划线
				Pattern pat = Pattern.compile("^[A-Za-z0-9_]+$");
				//如果为true，匹配正确
				boolean isTrue = pat.matcher(username).matches();
				if (!isTrue) {
					errorMap.put(j+1, errorCodeMap.get("userNameFormatErr"));
					fail ++;
					optUpload.setFail(fail);
					continue;
				}
				lfsysuser.setUserName(username);
				
				// 验证操作员名称
				// 1.验证操作员名称为空
				if (name == null || name.length() == 0) {
					errorMap.put(j+1, errorCodeMap.get("nameUnfilled"));
					fail ++;
					optUpload.setFail(fail);
					continue;
				}
				// 2.验证操作员名称超长（超过30位）
				int nameLength = 30;
				if (name.length() > nameLength) {
					errorMap.put(j+1, errorCodeMap.get("nameFormatErr"));
					fail ++;
					optUpload.setFail(fail);
					continue;
				}
				lfsysuser.setName(name);
				
				// 验证操作员编码
				// 1.判断操作员编码是否为空
				if (userCode == null || userCode.length() == 0) {
					errorMap.put(j+1, errorCodeMap.get("userCodeUnfilled"));
					fail ++;
					optUpload.setFail(fail);
					continue;
				}
				// 2.判断操作员编码是否重复
				if (userCodeSet.contains(userCode)) {
					errorMap.put(j+1, errorCodeMap.get("userCodeRepeat"));
					fail ++;
					optUpload.setFail(fail);
					continue;
				}
				// 3.判断操作员编码是否超过20位
				int userCodeLength = 20;
				if (userCode.length() > userCodeLength) {
					errorMap.put(j+1, errorCodeMap.get("userCodeFormatErr"));
					fail ++;
					optUpload.setFail(fail);
					continue;
				}
				// 4.判断操作员编码是否为数字或者字母
				Pattern p = Pattern.compile("^[A-Za-z0-9]+$");
				//如果为true，匹配正确
				boolean boo = p.matcher(userCode).matches();
				if(!boo) {
					errorMap.put(j+1, errorCodeMap.get("userCodeFormatErr"));
					fail ++;
					optUpload.setFail(fail);
					continue;
				}
				lfsysuser.setUserCode(userCode);	
					
				// 验证机构编码
				// 1.机构编码为空
				if (depCodeThird == null || depCodeThird.length() == 0) {
					errorMap.put(j+1, errorCodeMap.get("depCodeUnfilled"));
					fail ++;
					optUpload.setFail(fail);
					continue;
				}
				// 获取机构id
				LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
				map.put("depCodeThird", depCodeThird);
				map.put("corpCode", lgcorpcode);
				List<LfDep> lfDepList = baseBiz.findListByCondition(LfDep.class, map, null);
				
				// 2.判断机构编码是否存在
				if (lfDepList == null || lfDepList.size() == 0) {
					errorMap.put(j+1, errorCodeMap.get("depCodeNotExist"));
					fail ++;
					optUpload.setFail(fail);
					continue;
				}
				// 3.判断机构编码有无导入权限
				if (lfDepList.get(0).getDepState() == 1 && lgcorpcode.equals(lfDepList.get(0).getCorpCode().trim())
						&& depPath.length() > 0 && permissionType != 1 && lfDepList.get(0).getDeppath().startsWith(depPath)) {							
					lfsysuser.setDepId(lfDepList.get(0).getDepId());
				} else {
					errorMap.put(j+1, errorCodeMap.get("depCodePowerErr"));
					fail ++;
					optUpload.setFail(fail);
					continue;
				}
				
				// 验证性别
				// 1.性别是否为空
				if (sex == null || sex.length() == 0) {
					errorMap.put(j+1, errorCodeMap.get("sexUnfilled"));
					fail ++;
					optUpload.setFail(fail);
					continue;
				}
				// 判断性别
				String female = com.montnets.emp.i18n.util.MessageUtils.extractMessage("user", "user_xtgl_czygl_text_11", request);
				if (female.equals(sex)) {											
					lfsysuser.setSex(0);
				} else {
					lfsysuser.setSex(1);
				}
				
				// 验证电话号码
				// 1.验证电话号码是否为空
				if (mobile == null || mobile.length() == 0) {
					errorMap.put(j+1, errorCodeMap.get("phoneUnfilled"));
					fail ++;
					optUpload.setFail(fail);
					continue;
				}
				// 获取运营商号码段
				String[] haoduan = msgConfigBiz.getHaoduan();
				// 2.验证电话号码是否非法
				if (phoneUtil.getPhoneType(mobile, haoduan) < 0) {
					errorMap.put(j+1, errorCodeMap.get("phoneFormatErr"));
					fail ++;
					optUpload.setFail(fail);
					continue;
				}
				lfsysuser.setMobile(mobile);
				
				// 验证角色
				// 1.判断角色是否为空
				if (role == null || role.length() == 0) {
					errorMap.put(j+1, errorCodeMap.get("roleUnfilled"));
					fail ++;
					optUpload.setFail(fail);
					continue;
				}
				// 2.判断角色填写是否非法
				String roleSplit[] = role.split(",");
				String roles[] = arrayRemoveSameElements(roleSplit);
				StringBuffer srolesid = new StringBuffer();
				// 操作员赋权
				LinkedHashMap<String, String> roleMap = new LinkedHashMap<String, String>();
				for (int i=0; i<roles.length; i++) {
					roleMap.clear();
					roleMap.put("corpCode", lgcorpcode);
					roleMap.put("roleName", roles[i].trim());
					List<LfRoles> rolesList = baseBiz.findListByCondition(LfRoles.class, roleMap, null);
					if (rolesList != null && rolesList.size() > 0) {
						srolesid.append(rolesList.get(0).getRoleId()).append(",");
					}
				}
				if (srolesid == null || srolesid.length() == 0) {
					errorMap.put(j+1, errorCodeMap.get("roleFormatErr"));
					fail ++;
					optUpload.setFail(fail);
					continue;
				}
				
				// 设置导入操作员信息的默认值
				lfsysuser.setIsExistSubNo(2);
				String password = username;
				lfsysuser.setControlFlag("" + 0000);
				lfsysuser.setShowNum(0);
				lfsysuser.setOph("");
				lfsysuser.setQq("");
				lfsysuser.setEMail("");
				lfsysuser.setFax("");
				lfsysuser.setGuId(globalBiz.getValueByKey("guid", 1L));
				lfsysuser.setUserState(1);
				lfsysuser.setHolder(sysUser.getName());
				lfsysuser.setCorpCode(lgcorpcode);
				lfsysuser.setPermissionType(1);
				lfsysuser.setMsn("");
				lfsysuser.setDuties("");
				lfsysuser.setComments("");
				lfsysuser.setIsReviewer(2);
				lfsysuser.setIsAudited(2);
				lfsysuser.setClientState(0);
				lfsysuser.setEmployeeId(0l);
				lfsysuser.setManualInput(0);
				lfsysuser.setOnLine(0);
				lfsysuser.setPid(0);
				lfsysuser.setPostId(0l);
				lfsysuser.setUdgId(0);
				lfsysuser.setUpGuId(0l);
				lfsysuser.setUserType(0);
				lfsysuser.setWorkState(0);
				lfsysuser.setPassword(MD5.getMD5Str(password + username.toLowerCase()));
				lfsysuser.setPwdupdatetime(new Timestamp(System.currentTimeMillis()));
				
				String rolesid[] = srolesid.substring(0, srolesid.length()-1).toString().split(",");
				Long roleIds[] = new Long[rolesid.length];
				for (int i=0; i<roleIds.length; i++) {
					roleIds[i] = Long.valueOf(rolesid[i]);
				}
				Long opUserId = getOpUserId(request);
				boolean result = new SysuserPriBiz().add(null,roleIds,lfsysuser,null,null,null,null,opUserId);
				if (result) {
					success ++;
					optUpload.setSuccess(success);
					// 添加成功，将操作员登录账号和操作员编码加入set中
					usernameSet.add(username);
					userCodeSet.add(userCode);
				}else {	
					// 失败 
					errorMap.put(j+1, errorCodeMap.get("unknownErr"));
					fail ++;
					optUpload.setFail(fail);
					continue;
				}
			}
		}
	}
	
	/**
	 * 获取xlsx文件单元格的值
	 * @param cell
	 * @return
	 */
	public static Object getXSSFCellFormatValue(XSSFCell cell) {
        Object cellValue = null;
        if (cell != null) {
            // 判断cell类型
            switch (cell.getCellType()) {
	            case XSSFCell.CELL_TYPE_NUMERIC: {
	                // cellValue = String.valueOf(cell.getNumericCellValue());
	            	DataFormatter dataFormatter = new DataFormatter();
	            	cellValue = dataFormatter.formatCellValue(cell);
	                break;
	            }
	            case XSSFCell.CELL_TYPE_FORMULA: {
	                // 判断cell是否为日期格式
	                if(DateUtil.isCellDateFormatted(cell)) {
	                    // 转换为日期格式YYYY-mm-dd
	                    cellValue = cell.getDateCellValue();
	                } else {
	                    // 数字
	                    cellValue = String.valueOf(cell.getNumericCellValue());
	                }
	                break;
	            }
	            case XSSFCell.CELL_TYPE_STRING: {
	                cellValue = cell.getRichStringCellValue().getString();
	                break;
	            }
	            default:
	                cellValue = "";
	        }
        } else {
            cellValue = "";
        }
        return cellValue;
    }
	
	/**
	 * 获取xls文件单元格的值
	 * @param cell
	 * @return
	 */
	public String getHSSFCellFormatValue(HSSFCell cell) {
		Object cellValue = null;
        if (cell != null) {
            // 判断cell类型
            switch (cell.getCellType()) {
	            case HSSFCell.CELL_TYPE_NUMERIC: {
	            	// cellValue = String.valueOf(cell.getNumericCellValue());
	            	DataFormatter dataFormatter = new DataFormatter();
	            	cellValue = dataFormatter.formatCellValue(cell);
	                break;
	            }
	            case HSSFCell.CELL_TYPE_FORMULA: {
	                // 判断cell是否为日期格式
	                if(DateUtil.isCellDateFormatted(cell)) {
	                    // 转换为日期格式YYYY-mm-dd
	                    cellValue = cell.getDateCellValue();
	                } else {
	                    // 数字
	                    cellValue = String.valueOf(cell.getNumericCellValue());
	                }
	                break;
	            }
	            case HSSFCell.CELL_TYPE_STRING: {
	                cellValue = cell.getRichStringCellValue().getString();
	                break;
	            }
	            default:
	                cellValue = "";
	        }
        } else {
            cellValue = "";
        }
        return cellValue.toString();
	}
	
	/*
	 * 数组去重
	 */
	public String[] arrayRemoveSameElements(String[] arr) {
        // 实例化一个set集合
        Set<String> set = new HashSet<String>();
        // 遍历数组并存入集合,如果元素已存在则不会重复存入
        for (int i = 0; i < arr.length; i++) {
            set.add(arr[i]);
        }
       
        Object[] obj = set.toArray();
        String[] str = new String[obj.length];
        // 拷贝数组
        System.arraycopy(obj, 0, str, 0, obj.length);
        
        return str;
    }
	
	private LinkedHashMap<String, String> getErrorCodeMap(HttpServletRequest request) {
		LinkedHashMap<String, String> errorCodeMap = new LinkedHashMap<String, String>();
		// 登录账号重复
		String userNameRepeat = com.montnets.emp.i18n.util.MessageUtils.extractMessage("user", "user_xtgl_czygl_text_145", request);
		// 操作员编码重复
		String userCodeRepeat = com.montnets.emp.i18n.util.MessageUtils.extractMessage("user", "user_xtgl_czygl_text_147", request);
		// 电话号码格式非法
		String phoneFormatErr = com.montnets.emp.i18n.util.MessageUtils.extractMessage("user", "user_xtgl_czygl_text_148", request);
		// 所属机构编码不存在
		String depCodeNotExist = com.montnets.emp.i18n.util.MessageUtils.extractMessage("user", "user_xtgl_czygl_text_149", request);
		// 角色导入格式非法
		String roleFormatErr = com.montnets.emp.i18n.util.MessageUtils.extractMessage("user", "user_xtgl_czygl_text_150", request);
		// 系统未知错误
		String unknownErr = com.montnets.emp.i18n.util.MessageUtils.extractMessage("user", "user_xtgl_czygl_text_151", request);
		// 登录账号只能由1~15位字母数字下划线组成
		String userNameFormatErr = com.montnets.emp.i18n.util.MessageUtils.extractMessage("user", "user_xtgl_czygl_text_188", request);
		// 操作员名称超过30个字
		String nameFormatErr = com.montnets.emp.i18n.util.MessageUtils.extractMessage("user", "user_xtgl_czygl_text_189", request);
		// 操作员编码只能由1~20位字母数字组成
		String userCodeFormatErr = com.montnets.emp.i18n.util.MessageUtils.extractMessage("user", "user_xtgl_czygl_text_190", request);
		// 登录账号未填写完整
		String userNameUnfilled = com.montnets.emp.i18n.util.MessageUtils.extractMessage("user", "user_xtgl_czygl_text_191", request);
		// 操作员名称未填写完整
		String nameUnfilled = com.montnets.emp.i18n.util.MessageUtils.extractMessage("user", "user_xtgl_czygl_text_193", request);
		// 操作员编码未填写完整
		String userCodeUnfilled = com.montnets.emp.i18n.util.MessageUtils.extractMessage("user", "user_xtgl_czygl_text_194", request);
		// 所属机构编码未填写完整
		String depCodeUnfilled = com.montnets.emp.i18n.util.MessageUtils.extractMessage("user", "user_xtgl_czygl_text_195", request);
		// 性别未填写完整
		String sexUnfilled = com.montnets.emp.i18n.util.MessageUtils.extractMessage("user", "user_xtgl_czygl_text_196", request);
		// 手机号未填写完整
		String phoneUnfilled = com.montnets.emp.i18n.util.MessageUtils.extractMessage("user", "user_xtgl_czygl_text_197", request);
		// 角色未填写完整
		String roleUnfilled = com.montnets.emp.i18n.util.MessageUtils.extractMessage("user", "user_xtgl_czygl_text_198", request);
		// 只能导入本级以下的机构编码
		String depCodePowerErr = com.montnets.emp.i18n.util.MessageUtils.extractMessage("user", "user_xtgl_czygl_text_199", request);
		// 该行内容为空
		String lineIsEmpty = com.montnets.emp.i18n.util.MessageUtils.extractMessage("user", "user_xtgl_czygl_text_201", request);
		
		errorCodeMap.put("userNameRepeat", userNameRepeat);
		errorCodeMap.put("userCodeRepeat", userCodeRepeat);
		errorCodeMap.put("phoneFormatErr", phoneFormatErr);
		errorCodeMap.put("depCodeNotExist", depCodeNotExist);
		errorCodeMap.put("roleFormatErr", roleFormatErr);
		errorCodeMap.put("unknownErr", unknownErr);
		errorCodeMap.put("userNameFormatErr", userNameFormatErr);
		errorCodeMap.put("nameFormatErr", nameFormatErr);
		errorCodeMap.put("userCodeFormatErr", userCodeFormatErr);
		errorCodeMap.put("userNameUnfilled", userNameUnfilled);
		errorCodeMap.put("nameUnfilled", nameUnfilled);
		errorCodeMap.put("userCodeUnfilled", userCodeUnfilled);
		errorCodeMap.put("depCodeUnfilled", depCodeUnfilled);
		errorCodeMap.put("sexUnfilled", sexUnfilled);
		errorCodeMap.put("phoneUnfilled", phoneUnfilled);
		errorCodeMap.put("roleUnfilled", roleUnfilled);
		errorCodeMap.put("depCodePowerErr", depCodePowerErr);
		errorCodeMap.put("lineIsEmpty", lineIsEmpty);
		
		return errorCodeMap;
	}
       
}
