package com.montnets.emp.finance.servlet;

import com.montnets.emp.common.biz.BalanceLogBiz;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.GlobalVariableBiz;
import com.montnets.emp.common.constant.*;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.servlet.CommonSvt;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.pasroute.GtPortUsed;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.tailnumber.LfSubnoAllot;
import com.montnets.emp.entity.tailnumber.LfSubnoAllotDetail;
import com.montnets.emp.finance.biz.ElecPayrollCommon;
import com.montnets.emp.finance.biz.SendFinancialSMS;
import com.montnets.emp.finance.biz.SendFinancialSMSService;
import com.montnets.emp.finance.biz.YdcwBiz;
import com.montnets.emp.finance.util.FinanceBasicData;
import com.montnets.emp.finance.util.YdcwErrorCode;
import com.montnets.emp.util.*;
import jxl.Sheet;
import jxl.Workbook;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.poi.POIXMLException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @project emp
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-3-9 下午04:29:58
 */

public class YdcwBaseServlet extends BaseServlet
{

	private static final String UTF_8_NO_BOM = "UTF-8_NO_BOM";

	private final ChangeCharset charsetUtil = new ChangeCharset();

	private static final long		serialVersionUID			= -4906192663471241287L;

//	private static  String				result_succ_nohavetime_desc	= "短信任务发送成功！";
//
//	public static   String				result_succ_havetime_desc	= "短信定时任务创建成功！";

	private final String mobile_finance = "移动财务短信";

//	public static   String				error_code					= "错误代码：";

//	public static   String				manaulString				= "请输入参数数据行!";
//
//	public static   String				max_code_string				= "短信发送条数超过上限！";
//
//	public static   String				repeat_submit_stirng		= "短信任务发送失败!";								// "请勿重复提交!本次操作失败!";

    // 移动财务最大短信条数上限
    private final int max_rows = 500000;

    protected static final String PATH = "/ydcw/finance";

	/**
	 * 手工录入数据发送
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 */
	public void preview(HttpServletRequest request, HttpServletResponse response)
	{
        // 日期格式化
        SimpleDateFormat fmt = new SimpleDateFormat("MMddHHmmssSSSS");
		Map<String,String> ErrorCode = YdcwErrorCode.getErrorStr(request);
		HttpSession session = request.getSession(true);
		String langName = (String) session.getAttribute(StaticValue.LANG_KEY);
		String result = "";
		String errorString = "";
		String isReply = request.getParameter("isReply");
		String subNo = request.getParameter("subNo");
		String taskId = request.getParameter("taskId");
		String busCode = request.getParameter("busCode");
		String paraValue = StringEscapeUtils.unescapeHtml(request.getParameter("paraValue"));
		// 模板内容
		String template = request.getParameter("textarea");
		// 是否定时
		String isCheckTime = request.getParameter("isCheckTime");
		// 定时的时间内容
		String determineTime = request.getParameter("deterTime");
		determineTime = determineTime + ":00";
		// 发送账号
		String spAccount = request.getParameter("spUser");
		// 列数
		String cell = request.getParameter("textCell");
		// 行数
		String corpCode = request.getParameter("lgcorpcode");
		//String userId = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String userId = SysuserUtil.strLguserid(request);
		// 循环尾号
		String circleSubNo = request.getParameter("circleSubNo1");
		request.setAttribute("circleSubNo", circleSubNo);
		try
		{
			// 发送临时短信文件存储地址
			String[] url = new String[1];
			TxtFileUtil txtFileUtil = new TxtFileUtil();
			String strNYR = txtFileUtil.getCurNYR();
			url[0] = txtFileUtil.getWebRoot() + StaticValue.YDCW + strNYR;
			// 创建目录
			txtFileUtil.makeDir(url[0]);
			String[] paraValueArr = paraValue.split("&");
			// 将参数重新组合成字符串并装载到List中
			String paras = "";
			List<String> paraValueList = new ArrayList<String>();
			for (int i = 0; i < paraValueArr.length; i++)
			{
				if((i + 1) % Integer.parseInt(cell) == 0)
				{
					// 每行结束
					paras += paraValueArr[i];
					paraValueList.add(paras);
					paras = "";
				}
				else
				{
					paras += paraValueArr[i] + "&";
				}
			}
			ElecPayrollCommon epcObject = new ElecPayrollCommon();
			// 初始化验证码
			String verificationCode = epcObject.getRandomPassword();
			if(verificationCode == null || "".equals(verificationCode))
			{
				errorString = ErrorCode.get("ECWV108");
				return;
			}
			
			//登录操作员信息
			LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			//登录操作员信息为空
			if(lfSysuser == null)
			{
				EmpExecutionContext.error("移动财务预览，从session获取登录操作员信息异常。lfSysuser为null，errCode："+IErrorCode.V10001);
				ErrorCodeInfo info = ErrorCodeInfo.getInstance(langName);
				result = info.getErrorInfo(IErrorCode.V10001);
				return;
			}
			//操作员、企业编码、SP账号检查
			boolean checkFlag = new CheckUtil().checkSysuserInCorp(lfSysuser, corpCode, spAccount, null);
			if(!checkFlag)
			{
				EmpExecutionContext.error("移动财务预览，检查操作员、企业编码、发送账号不通过，，taskid:"+taskId
						+ "，corpCode:"+corpCode
						+ "，userid："+userId
						+ "，spUser："+spAccount
						+ "，errCode:"+ IErrorCode.B20007);
						ErrorCodeInfo info = ErrorCodeInfo.getInstance(langName);
						result = info.getErrorInfo(IErrorCode.B20007);
						return;
			}
			
			FinanceBasicData fData = new FinanceBasicData();
			fData.setEpcObject(epcObject);
			fData.setIsCheckTime(isCheckTime);
			fData.setFileItem(null);
			fData.setList(paraValueList);
			fData.setTemplate(template);
			fData.setSpAccount(spAccount);
			fData.setUrl(url[0]);
			fData.setDetermineTime(determineTime);
			fData.setCorpCode(corpCode);
			fData.setVerifycode(verificationCode);
			fData.setUserId(userId);
			fData.setBusCode(busCode);
			fData.setSmsTitle(mobile_finance);
			fData.setSession(session);
			fData.setTemplateIds(null);
			fData.setTextOrExcel(3);
			fData.setSubNo(subNo);
			
			GetSxCount sx = GetSxCount.getInstance();
			// 临时文件存储路径
			Date dt = new Date();
			String tempFileName = fmt.format(dt) + "_"+sx.getCount();
			String filePath = StaticValue.YDCW + strNYR + tempFileName + ".txt";
			fData.setTempFileName(tempFileName);
			fData.setFilePath(filePath);
			fData.setIsReply(Integer.parseInt(isReply));
			fData.setTaskId(Long.valueOf(taskId));
			session.setAttribute("fileUrl", url[0] + File.separator + tempFileName + ".txt");

			// 短信发送业务预处理
			SendFinancialSMSService sfsObject = new SendFinancialSMSService();
			boolean resultBool = sfsObject.sendFinancialSMS(fData,langName);
			if(!resultBool)
			{
				if(session.getAttribute("ErrorReport") != null)
				{
					errorString = ErrorCode.get("error_code") + ErrorCode.get(String.valueOf(session.getAttribute("ErrorReport")));
				}
				if(session.getAttribute("ErrorAll") != null)
				{
					errorString = ErrorCode.get(String.valueOf(session.getAttribute("ErrorAll")));
				}
			}
			else
			{
				result = getSessionObj(request, response, fData);
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "手工录入数据 发送异常!");
			// 清楚session里面的错误日志
			if(session.getAttribute("ErrorReport") != null)
			{
				errorString = ErrorCode.get(String.valueOf(session.getAttribute("ErrorReport")));
			}
			else
			{
				errorString = ErrorCode.get("ECWB111");
			}
		}
		finally
		{
			request.setAttribute("errorString", errorString);
			request.setAttribute("result", result.replace("\\", "\\\\"));
			try
			{
				request.getRequestDispatcher(PATH + "/ycw_preview.jsp").forward(request, response);
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "手工录入数据 发送异常!");
			}
		}
	}

	/**
	 * 获取Session对象
	 * 
	 * @throws Exception
	 * @return void
	 * @author Jinny.Ding
	 * @date May 13, 2012
	 */
	@SuppressWarnings("unchecked")
	public String getSessionObj(HttpServletRequest request, HttpServletResponse response, FinanceBasicData fData) throws Exception
	{
		HttpSession session = request.getSession(true);
		try
		{
			//String lguserid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);

			if(lguserid == null)
			{
				//lguserid = (String) request.getAttribute("lguserid");
				//漏洞修复 session里获取操作员信息
				lguserid = SysuserUtil.strLguserid(request);
			}
			LfSysuser curSysuser = new BaseBiz().getLfSysuserByUserId(lguserid);// 当前登录操作员对象
			// Map<String, String> previewMap = new HashMap<String, String>();
			// 记录预览数据
			List<String[]> previewList = new ArrayList<String[]>();
			// 记录统计数据
			Map<Integer, String> countMap = new HashMap<Integer, String>();
			String countStr = "";
			String preStr = "";
			String writerStr = "";
			String task = "";
			String typeStr = "";
			if(session.getAttribute("countMap") != null)
			{
				countMap = (Map<Integer, String>) session.getAttribute("countMap");
				String badFilePath = (String) session.getAttribute("badFilePath");
				// 总数
				countStr = countMap.get(1)
				// 有效数
				+ "cw0Pre" + countMap.get(2)
				// 模拟网关实发数
				+ "cw0Pre" + countMap.get(3)
				// 黑名单数
				+ "cw0Pre" + countMap.get(4)
				// 重复数
				+ "cw0Pre" + countMap.get(5)
				// 含关键字数
				+ "cw0Pre" + countMap.get(6)
				// 非法格式数
				+ "cw0Pre" + countMap.get(7)
				// 无效号码文件路径
				+ "cw0Pre" + badFilePath;
				// 根据是否计费加入余额
				BalanceLogBiz biz = new BalanceLogBiz();
				Long ct = 0L;
				if(biz.IsChargings(Long.parseLong(lguserid)))
				{
					ct = biz.getAllowSmsAmount(curSysuser);
					countStr = countStr + "cw0Pre" + ct + "cw0Pre" + "true";
				}
				else
				{
					countStr = countStr + "cw0Pre0cw0Prefalse";
				}
				
				String spUser = fData.getSpAccount();
				//sp账号计费类型
				long spChargetype = biz.getSpUserFeeFlag(spUser,1);
				
				//检查sp余额
				Long spFee = 0L;
				//sp预付费
				if(spChargetype==1){
					//sp账号余额查询
					spFee = biz.getSpUserAmount(spUser);
					EmpExecutionContext.info("sp账号可用余额:"+spFee+",spuser:"+spUser);
				}//后付费账号
				else if(spChargetype==2){
					EmpExecutionContext.info("spuser:"+spUser+",该账号为后付费账号");
				}
				else{
					//获取sp账号计费类型失败
					EmpExecutionContext.error("获取sp账号计费类型失败,spuser:"+spUser);
				}
				String spFeeResult = biz.checkGwFee(fData.getSpAccount(), Integer.parseInt(countMap.get(2)), curSysuser.getCorpCode(), false, 1);
				countStr = countStr + "cw0Pre" + spFeeResult+"cw0Pre" +spChargetype+"cw0Pre" +(spFee==null?0:spFee);
			}
			if(session.getAttribute("previewList") != null)
			{
				Map<String, String> btnMap = (Map<String, String>) session.getAttribute("btnMap");// 按钮权限Map
				CommonVariables commVa = new CommonVariables();
				previewList = (ArrayList<String[]>) session.getAttribute("previewList");
				String mobile = "";
				if(previewList.size() != 0)
				{
					for (int i = 0; i < previewList.size(); i++)
					{
						String[] argsArr = previewList.get(i);
						mobile = argsArr[0];
						if(btnMap.get(StaticValue.PHONE_LOOK_CODE) == null && !"".equals(mobile))
						{
							// 无号码的查看权限，需替换手机号码的星号
							mobile = commVa.replacePhoneNumber(mobile);
						}
						String entryString = mobile + "cw1Pre" + argsArr[1];
						if("".equals(preStr))
						{
							preStr = entryString;
						}
						else
						{
							preStr = preStr + "cw2Pre" + entryString;
						}
					}
				}
			}

			if(session.getAttribute("taskObj") != null)
			{
				task = "taskObj";
			}
			if(session.getAttribute("txtOrExcel") != null)
			{
				typeStr = String.valueOf(session.getAttribute("txtOrExcel"));
			}
			writerStr = countStr + "cw3Pre" + preStr + "cw3Pre" + task + "cw3Pre" + typeStr;

			return writerStr;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "处理发送号码信息异常!");
			return null;
		}
	}

	/**
	 * 导入文件数据 (支持 txt 和 xls)
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	@SuppressWarnings("unchecked")
	public void filePreview(HttpServletRequest request, HttpServletResponse response)
	{
        SimpleDateFormat fmt = new SimpleDateFormat("MMddHHmmssSSSS");
		Map<String,String> ErrorCode = YdcwErrorCode.getErrorStr(request);
		HttpSession session = request.getSession(true);
		String langName = (String) session.getAttribute(StaticValue.LANG_KEY);
		FileItem fileItem = null;
		List<FileItem> fileList = null;
		BufferedReader reader = null;
		String result = "";
		String errorString = "";
		try
		{
			// 定时时间
			String determineTime = "";
			String isCheckTime = "false";
			// 发送账号
			String spAccount = "";
			// 模板
			String template = "";
			// 文件类型
			String fileType = "";
			// 模板下标
			String templateIds = "";
			String isReply = "";
			String busCode = "";
			String taskId = "";
			String subNo = "";
			String userId = "";
			String corpCode = "";
			String[] url = new String[1];
			// 获取文件临时存储目录
			TxtFileUtil txtFileUtil = new TxtFileUtil();
			String strNYR = txtFileUtil.getCurNYR();
			url[0] = txtFileUtil.getWebRoot() + StaticValue.YDCW + strNYR;
			// 创建目录
			txtFileUtil.makeDir(url[0]);
			String temp = url[0];
			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setSizeThreshold(1024 * 1024);
			factory.setRepository(new File(temp));
			// 创建文件上传对象
			ServletFileUpload upload = new ServletFileUpload(factory);
			try
			{
				// 获取file内容
				fileList = upload.parseRequest(request);
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e,"导入文件数据解析异常");
				throw e;
			}
			String uploadFileName="";
			Iterator<FileItem> it2 = fileList.iterator();
			while(it2.hasNext())
			{
				// 封装表单属性
				fileItem = (FileItem) it2.next();
				String name = fileItem.getName();
				// 控件name属性value
				String fileName = fileItem.getFieldName();
				if(fileName.equals("textarea"))
				{
					// 短信模板
					template = fileItem.getString("UTF-8").toString();
				}
				else
				if(fileName.equals("isCheckTime"))
				{
					// 是否定时
					isCheckTime = fileItem.getString("UTF-8").toString();
				}
				else
				if(fileName.equals("deterTime"))
				{
					// 发送时间
					determineTime = fileItem.getString("UTF-8").toString();
					determineTime = determineTime + ":00";
				}
				else
				if(fileName.equals("spAccount"))
				{
					// 发送账号
					spAccount = fileItem.getString("UTF-8").toString();
				}
				else
				if(fileName.equals("templateIds"))
				{
					// 参数下标字符串
					templateIds = fileItem.getString("UTF-8").toString();
				}
				else
				if(fileName.equals("isReply"))
				{
					// 是否需要回复
					isReply = fileItem.getString("UTF-8").toString();
				}
				else
				if(fileName.equals("busCode1"))
				{
					// 业务类型
					busCode = fileItem.getString("UTF-8").toString();
				}
				else
				if(fileName.equals("taskId"))
				{
					// taskid
					taskId = fileItem.getString("UTF-8").toString();
				}
				else
				if(fileName.equals("subNo"))
				{
					// 尾号
					subNo = fileItem.getString("UTF-8").toString();
				}
				else
				if(fileName.equals("lguserid"))
				{
					// 当前登录账号的userid
					userId = fileItem.getString("UTF-8").toString();
				}
				else
				if(fileName.equals("lgcorpcode"))
				{
					// 当前登录账号的企业编码
					corpCode = fileItem.getString("UTF-8").toString();
				}
				else
				// 支持.txt , .xls 格式文档
				// 判断表单是否为文件(file)类型
				if(!fileItem.isFormField() && fileItem.getName().length() > 0)
				{
					uploadFileName=fileItem.getName();
					EmpExecutionContext.info("[移动财务]发送上传文件名称为："+fileItem.getName());
					// 文件类型
					fileType = name.substring(name.lastIndexOf(".") + 1);
					if(!(fileType.toLowerCase().equals("txt")) && !(fileType.toLowerCase().equals("xls")) && !(fileType.toLowerCase().equals("xlsx")))
					{
						errorString = ErrorCode.get("ECWV106");
						return;
					}
					int rows = 0;
					// 判断是否为TXT类型文件
					ElecPayrollCommon epcObject = new ElecPayrollCommon();
					if(fileType.toLowerCase().equals("txt"))
					{
						// 编码处理
						/*
						 * String charset =
						 * FileUtils.get_charset(fileItem.getInputStream());
						 */
						String charset = charsetUtil.get_charset(fileItem.getInputStream());
						boolean isUtf8NoBom = UTF_8_NO_BOM.equals(charset);
						if(isUtf8NoBom){
							charset = ChangeCharset.UTF_8;
						}
						reader = new BufferedReader(new InputStreamReader(fileItem.getInputStream(), charset));
						if(charset.startsWith("UTF-")&& !isUtf8NoBom)
						{
							reader.read(new char[1]);
						}
						String line = "";
						if((line =reader.readLine()) == null)
						{
							errorString = ErrorCode.get("ECWV101");
							return;
						}
						// 统计参数数据条数
						reader = new BufferedReader(new InputStreamReader(fileItem.getInputStream(), charset));
						if(charset.startsWith("UTF-")&& !isUtf8NoBom)
						{
							reader.read(new char[1]);
						}
						while((line =reader.readLine()) != null)
						{
							rows++;
						}
					}
					// 判断是否为XLS类型文件
					if(fileType.toLowerCase().equals("xls"))
					{
						Workbook wb = Workbook.getWorkbook(fileItem.getInputStream());
						Sheet sh = wb.getSheet(0);
						if(sh.getRows() == 0)
						{
							errorString = ErrorCode.get("ECWV101");
							return;
						}
						// 统计参数数据条数
						rows = sh.getRows();
					}
					// 判断是否为XLSX类型文件
					if(fileType.toLowerCase().equals("xlsx"))
					{
						//Workbook wb = Workbook.getWorkbook(fileItem.getInputStream());
						XSSFWorkbook wb = new XSSFWorkbook(fileItem.getInputStream());
						XSSFSheet  sh = wb.getSheetAt(0);
						if(sh.getPhysicalNumberOfRows() == 0)
						{
							errorString = ErrorCode.get("ECWV101");
							return;
						}
						// 统计参数数据条数
						rows = sh.getPhysicalNumberOfRows();
					}
					// 计算最大条数是否超过上限
					if(rows > max_rows)
					{
						errorString = ErrorCode.get("ECWV112");
						return;
					}
					// 初始化验证码
					String verificationCode = epcObject.getRandomPassword();

					if(verificationCode == null || "".equals(verificationCode))
					{
						errorString = ErrorCode.get("ECWV108");
						return;
					}
					
					
					//登录操作员信息
					LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
					//登录操作员信息为空
					if(lfSysuser == null)
					{
						EmpExecutionContext.error("移动财务预览，从session获取登录操作员信息异常。lfSysuser为null，errCode："+IErrorCode.V10001);
						ErrorCodeInfo info = ErrorCodeInfo.getInstance(langName);
						String desc = info.getErrorInfo(IErrorCode.V10001);
						result = desc;
						return;
					}
					//操作员、企业编码、SP账号检查
					boolean checkFlag = new CheckUtil().checkSysuserInCorp(lfSysuser, corpCode, spAccount, null);
					if(!checkFlag)
					{
						EmpExecutionContext.error("移动财务预览，检查操作员、企业编码、发送账号不通过，，taskid:"+taskId
								+ "，corpCode:"+corpCode
								+ "，userid："+userId
								+ "，spUser："+spAccount
								+ "，errCode:"+ IErrorCode.V10001);
								ErrorCodeInfo info = ErrorCodeInfo.getInstance(langName);
								result = info.getErrorInfo(IErrorCode.V10001);
								return;	
					}
					
					// 封装业务处理对象
					FinanceBasicData fData = new FinanceBasicData();
					fData.setEpcObject(epcObject);
					fData.setIsCheckTime(isCheckTime);
					fData.setFileItem(fileItem);
					fData.setTemplate(template);
					fData.setSpAccount(spAccount);
					fData.setUrl(url[0]);
					fData.setDetermineTime(determineTime);
					fData.setCorpCode(corpCode);
					fData.setVerifycode(verificationCode);
					fData.setUserId(userId);
					fData.setBusCode(busCode);
					fData.setSmsTitle(mobile_finance);
					fData.setSession(session);
					fData.setTemplateIds(templateIds);
					fData.setIsReply(Integer.valueOf(isReply));
					fData.setSubNo(subNo);
					fData.setTaskId(Long.valueOf(taskId));
					// 临时文件存储路径
					Date dt = new Date();
					GetSxCount sx = GetSxCount.getInstance();
					String tempFileName = fmt.format(dt)+"_"+sx.getCount();
					String filePath = StaticValue.YDCW + strNYR + tempFileName + ".txt";
					fData.setTempFileName(tempFileName);
					fData.setFilePath(filePath);
					session.setAttribute("fileUrl", url[0] + File.separator + tempFileName + ".txt");
					if(fileType.toLowerCase().equals("txt"))
					{
						fData.setTextOrExcel(1);
					}

					if(fileType.toLowerCase().equals("xls"))
					{
						fData.setTextOrExcel(2);
					}
					if(fileType.toLowerCase().equals("xlsx"))
					{
						fData.setTextOrExcel(4);
					}
					// 进入发送业务预处理
					SendFinancialSMSService sfsObject = new SendFinancialSMSService();
					boolean resultBool = sfsObject.sendFinancialSMS(fData,langName);
					if(!resultBool)
					{
						// 业务预处理失败
						if(session.getAttribute("ErrorReport") != null)
						{
							errorString = ErrorCode.get(String.valueOf(session.getAttribute("ErrorReport")));
						}
						if(session.getAttribute("ErrorAll") != null)
						{
							errorString = ErrorCode.get(String.valueOf(session.getAttribute("ErrorAll")));
						}
					}
					else
					{
						request.setAttribute("lguserid", userId);
						result = getSessionObj(request, response, fData);
					}
				}
				// end if
			}
			EmpExecutionContext.info("[移动财务]操作员ID为:"+userId+",企业编码为："+corpCode+",发送上传文件名称为："+uploadFileName);
			// end while
		}
		catch (FileUploadException e1)
		{
			// 文件上传异常
			EmpExecutionContext.error(e1, "文件上传异常!");
			errorString = ErrorCode.get("ECWV105");
			if(fileItem != null)
			{
				fileItem.delete();
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "导入文件数据异常");
			// 清除session里面的错误日志

            if((errorString == null || errorString.length() == 0) && e instanceof POIXMLException){
                errorString = ErrorCode.get("ECWV106");
            }else{
			if(session.getAttribute("ErrorReport") != null)
			{
				errorString = ErrorCode.get(String.valueOf(session.getAttribute("ErrorReport")));
			}
			else
			{
				errorString = ErrorCode.get("ECWV107");
			}
            }


			if(fileItem != null)
			{
				fileItem.delete();
			}
		}
		finally
		{
			if(fileItem != null)
			{
				fileItem.delete();
			}
			// 清除session里面的错误日志
			if(session.getAttribute("ErrorReport") != null)
			{
				session.removeAttribute("ErrorReport");
			}
			if(session.getAttribute("GatewayReport") != null)
			{
				session.removeAttribute("GatewayReport");
			}
			request.setAttribute("errorString", errorString);
			request.setAttribute("result", result.replace("\\", "\\\\"));
			try
			{
				request.getRequestDispatcher(PATH + "/ycw_preview.jsp").forward(request, response);
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "导入文件跳转页面异常!");
			}
		}

	}

	public String add(HttpServletRequest request, HttpServletResponse response, String moduleName)
	{
		Map<String,String> ErrorCode = YdcwErrorCode.getErrorStr(request);
		String opModule = moduleName;
		String corpCode = null;	
		String userId = null;	
		String userName = null;	
		String opContent = null;	
		String opType = "OTHER";
		Object obj = request.getSession(false).getAttribute("loginSysuser");
		LfSysuser lfSysuser = (LfSysuser)obj;
		userId = lfSysuser.getUserId().toString();
		corpCode = lfSysuser.getCorpCode();
		userName = lfSysuser.getUserName();
		HttpSession session = request.getSession(false);
		int result = 0;
		String isCheckTime = "";
		String returnStr = "";
		// 2.获取LfMttask对象
		SendFinancialSMS sendSms = new SendFinancialSMS();
		LfMttask task = (LfMttask) session.getAttribute("taskObj");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");
		String dateStr = sdf.format(new Date());
		if(task != null && task.getTimerStatus().intValue() == 1 && (task.getTimerTime().getTime() < Timestamp.valueOf(dateStr).getTime()))
		{
			returnStr = "您输入的定时时间小于当前时间，请重新输入！";
		}
		else
		{
			// 3.发送短信
			boolean repSub = false;
			// 重复提交,该对象为NULL值
			if(task != null)
			{
				try
				{
					result = sendSms.sendSms(session, task);
				}
				catch (Exception e)
				{
					EmpExecutionContext.error(e,ErrorCode.get("ECWV107"));
					request.setAttribute("msg", ErrorCode.get("ECWV107"));
				}
			}
			else
			{
				repSub = true;
			}
			// 是否定时
			if(session.getAttribute("isCheckTime") != null)
			{
				isCheckTime = (String) session.getAttribute("isCheckTime");
			}
			// 清除Session
			ElecPayrollCommon epcObject = new ElecPayrollCommon();
			try
			{
				epcObject.clearSession(session);
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e,"清除session异常");
			}
			// 处理短信发送反馈信息
			if(result == 1)
			{
				
				// 非定时
				if(isCheckTime.equals("false"))
				{
					//发送成功返回"000"，用于发送成功后跳转查看发送记录
					returnStr = "000";
				}
				else
				{
					// 定时
					returnStr = moduleName + ErrorCode.get("SMSTimingTask");
				}
			}
			else if(result == 0)
			{
				// 发送失败,网关返回状态报告
				if(session.getAttribute("GatewayReport") != null)
				{
					returnStr = String.valueOf(session.getAttribute("GatewayReport"));
				}
				// 发送失败,处理TXT&Excel文档业务异常
				if(session.getAttribute("ErrorReport") != null)
				{
					returnStr = ErrorCode.get(String.valueOf(session.getAttribute("ErrorReport")));
				}
				if(repSub)
				{
					returnStr = ErrorCode.get("repeat_submit_stirng");
				}
			}
			else if(result == 2)
			{
				returnStr = moduleName + ErrorCode.get("SMSBalance");
			}
			else if(result == 4)
			{
				returnStr = moduleName + ErrorCode.get("ExpandedTail");
			}
			else if(result == 5)
			{
				returnStr = moduleName + ErrorCode.get("GetOperatorBalance");
			}
			else if(result == 6)
			{
				returnStr = moduleName + ErrorCode.get("OperatorBalance");
			}
			else if(result == 7){
				returnStr = moduleName + ErrorCode.get("SPBalance");
			}
		}
		String OpStatus = result==1?"成功":"失败"; 
		//添加操作日志
		opContent = "短信发送操作"+OpStatus+"。（任务ID："+task.getTaskId()+"，网关返回状态："+task.getErrorCodes()+"）";
		EmpExecutionContext.info(opModule, corpCode, userId, userName, opContent, opType);
		return returnStr;
	}

	/**
	 * DESC: 获取短信尾号
	 * 
	 * @param request
	 * @param response
	 */
	@SuppressWarnings("unchecked")
	public void getSubNo(HttpServletRequest request, HttpServletResponse response)
	{
		String spUser = request.getParameter("spUser");
		String isReply = request.getParameter("isReply");
		String circleSubNo = request.getParameter("circleSubNo");
		String taskId = request.getParameter("taskId");
		String subNo = "";
		//String lguserid = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String lguserid = SysuserUtil.strLguserid(request);


		LfSubnoAllotDetail subnoAllotDetail = null;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		PrintWriter writer = null;
		LfSysuser lfSysuser = null;
		BaseBiz baseBiz = new BaseBiz();
		try
		{
			writer = response.getWriter();
			lfSysuser = baseBiz.getById(LfSysuser.class, lguserid);
			if("0".equals(isReply))
			{
				// 0表示不需要回复
				subNo = "";
			}
			else
				if("1".equals(isReply))
				{
					// 1表示本次任务
					// 获取循环尾号，避免产生大量没有使用的尾号，只获取一次
					if(circleSubNo != null && !"".equals(circleSubNo))
					{
						subNo = circleSubNo;
					}
					else
					{
						SMParams smParams = new SMParams();
						// 编码（0模块编码1业务编码2产品编码3机构id4操作员guid,5任务id）
						smParams.setCodes(taskId.toString());
						// 编码类别
						smParams.setCodeType(5);
						smParams.setCorpCode(lfSysuser.getCorpCode());
						// (分配类型0固定1自动有效期7天，null表是不设有效期)
						smParams.setAllotType(1);
						// 尾号是否确定插入表
						smParams.setSubnoVali(false);
						smParams.setTaskId(Long.parseLong(taskId));
						smParams.setLoginId(lfSysuser.getGuId().toString());
						smParams.setDepId(lfSysuser.getDepId());
						ErrorCodeParam errorCodeParam = new ErrorCodeParam();
						subnoAllotDetail = GlobalVariableBiz.getInstance().getSubnoDetail(smParams, errorCodeParam);
						if(errorCodeParam.getErrorCode() != null && "EZHB237".equals(errorCodeParam.getErrorCode()))
						{
							// 没有可用的尾号
							writer.print("noUsedSubNo");
							return;
						}
						else
							if(errorCodeParam.getErrorCode() != null && "EZHB238".equals(errorCodeParam.getErrorCode()))
							{
								// 获取尾号失败
								writer.print("noSubNo");
								return;
							}
						subNo = subnoAllotDetail != null ? subnoAllotDetail.getUsedExtendSubno() : null;
						if(subNo == null || "".equals(subNo))
						{
							// 获取尾号失败
							writer.print("noSubNo");
							return;
						}
					}
				}
				else
					if("2".equals(isReply))
					{
						// 2表示我的尾号
						// 获取操作员固定尾号
						conditionMap.put("loginId", lfSysuser.getGuId().toString());
						conditionMap.put("corpCode", lfSysuser.getCorpCode());
						conditionMap.put("menuCode&is null", "isnull");
						// conditionMap.put("taskId&is null", "isnull");
						conditionMap.put("busCode&is null", "isnull");
						List<LfSubnoAllot> allots = baseBiz.getByCondition(LfSubnoAllot.class, conditionMap, null);
						LfSubnoAllot allot = allots != null && allots.size() > 0 ? allots.get(0) : null;
						if(allot == null || allot.getUsedExtendSubno() == null || "".equals(allot.getUsedExtendSubno()))
						{
							writer.print("noSubNo");
							return;
						}
						subNo = allot.getUsedExtendSubno();
					}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取尾号处理异常!");
		}
		conditionMap.clear();
		conditionMap.put("userId", spUser);
		LinkedHashMap<String, String> orderByMap = new LinkedHashMap<String, String>();
		orderByMap.put("spisuncm", StaticValue.ASC);
		JSONObject jsonObject = null;
		int strLen = 0;
		String yd = "true";
		String lt = "true";
		String dx = "true";
		try
		{
			List<GtPortUsed> gtPortUseds = baseBiz.getByCondition(GtPortUsed.class, conditionMap, orderByMap);
			GtPortUsed gtPortUsed = null;
			jsonObject = new JSONObject();
			for (int i = 0; i < gtPortUseds.size(); i++)
			{
				gtPortUsed = gtPortUseds.get(i);
				// 拓展尾号长度
				int cpnoLen = gtPortUsed.getCpno() != null ? gtPortUsed.getCpno().trim().length() : 0;
				// 通道号+拓展尾号+尾号 的长度
				strLen = gtPortUsed.getSpgate().length() + cpnoLen + subNo.length();
				// 判断各运营商的通道号+拓展尾号+尾号是否大于20，如果大于20
				// 则前台需提示XX运营商通道号+尾号长度大于20，不允许发送
				if(gtPortUsed.getSpisuncm() == 0 && strLen > 20)
				{
					yd = "false";
				}
				else
					if(gtPortUsed.getSpisuncm() == 1 && strLen > 20)
					{
						lt = "false";
					}
					else
						if(gtPortUsed.getSpisuncm() == 21 && strLen > 20)
						{
							dx = "false";
						}

			}
			// 是否可以进行发送的标志
			jsonObject.put("sendFlag", yd + "&" + lt + "&" + dx);
			jsonObject.put("subNo", subNo);// 尾号

			writer.print(jsonObject.toString());

		}
		catch (Exception e)
		{
			writer.print("error");
			EmpExecutionContext.error(e, "获取尾号处理异常!");
		}
	}

	/**
	 * DESC: 通过模板ID获取模板内容信息
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 */
	public void getTmMsg(HttpServletRequest request, HttpServletResponse response) throws ServletException
	{
		Map<String,String> ErrorCode = YdcwErrorCode.getErrorStr(request);
		// 模板id
		String tmId = request.getParameter("tmId");
		String result = "";
		PrintWriter writer = null;
		ElecPayrollCommon epcObject = new ElecPayrollCommon();
		try
		{
			writer = response.getWriter();

			if(!StringUtils.isEmpty(tmId))
			{
				result = epcObject.getTemplateMsg(tmId);
                //解决移动财务&nbsp;显示为空格的问题
				//result = CommonSvt.unescape(result);
			}

		}
		catch (Exception e)
		{
			result = "error";
			EmpExecutionContext.error(e, "获取模板内容异常!");
			request.setAttribute("msg", ErrorCode.get("ECWV104"));
		}
		finally
		{
			if(writer != null){
				writer.print("@" + result);
				writer.close();
			}
			
		}
	}
	
	public void opSucLog(HttpServletRequest request,String modName,String opContent){
		LfSysuser lfSysuser = null;
		Object obj = request.getSession(false).getAttribute("loginSysuser");
		if(obj == null) {
            return;
        }
		lfSysuser = (LfSysuser)obj;
		EmpExecutionContext.info("模块名称："+modName+"，企业："+lfSysuser.getCorpCode()+"，操作员："+lfSysuser.getUserId()+"["+lfSysuser.getUserName()+"]，"+opContent+"成功。");
	}
}
