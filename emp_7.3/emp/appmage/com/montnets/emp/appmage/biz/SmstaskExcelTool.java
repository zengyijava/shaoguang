package com.montnets.emp.appmage.biz;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.FontUnderline;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.montnets.emp.common.constant.CommonVariables;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.common.vo.LfMttaskVo;
import com.montnets.emp.common.vo.SendedMttaskVo;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.util.ZipUtil;


public class SmstaskExcelTool{

	// 报表模板目录
	private String BASEDIR = "";

	// 操作员报表文件路径
	private String voucherTemplatePath = "";

	private final String voucherPath = "download";

	private final String TEMP = "temp";

	private final CommonVariables cv;

	public SmstaskExcelTool(String context) {

		BASEDIR = context;
		cv = new CommonVariables();

	}

	/**
	 * @description：生成发送详情Excel
	 * @param fileName
	 *            文件名
	 * @param mtdList
	 *            报表数据
	 * @return Map 生成文件成后，返回文件名和文件路径 throws 生成文件失败
	 * @author 
	 * @see zhangmin 2012-1-9 创建
	 */

	
    public Map<String, String> createSmsMtReportExcel(
			List<SendedMttaskVo> mtdList, String IsexportAll,
			Integer isHidePhone, HttpServletRequest request) throws Exception {
		
		//String execlName = "群发历史详情";
		String execlName = "app_smsTaskRecord";

		if ("true".equals(IsexportAll)) {
			voucherTemplatePath = BASEDIR + File.separator
					+ TEMP + File.separator
					+ "app_smsTaskRecord_"  + request.getSession().getAttribute(StaticValue.LANG_KEY) + ".xlsx";
		} else {
			voucherTemplatePath = BASEDIR + File.separator
					+ TEMP + File.separator
					+ "app_smsTaskRecordOnlyPhone_" + request.getSession().getAttribute(StaticValue.LANG_KEY) + ".xlsx";
		}

		Map<String, String> resultMap = new HashMap<String, String>();

		// 当前每页分页条数
		int intRowsOfPage = 500000;

		// 当前每页显示条数
		int intPagesCount = (mtdList.size() % intRowsOfPage == 0) ? (mtdList
				.size() / intRowsOfPage) : (mtdList.size() / intRowsOfPage + 1);

		int size = intPagesCount; // 生成的工作薄个数

		EmpExecutionContext.info("工作表个数：" + size);
		if (size == 0) {
			EmpExecutionContext.info("无统计详情数据！");
			throw new Exception("无统计详情数据！");
		}

		// 产生报表文件的存储路径
		Date curDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddH24mmss");
		String voucherFilePath = BASEDIR + File.separator + voucherPath+File.separator+"groupHisDetail"+File.separator+sdf.format(curDate);
		File fileTemp = new File(voucherFilePath);
		
		
		// 报表文件名
		String fileName = null;
		String filePath = null;
		XSSFWorkbook workbook = null;
		FileInputStream in = null;
		FileOutputStream os = null;
		try {
			if(!fileTemp.exists()) {
                fileTemp.mkdirs();
            }
			// 创建只读的Excel工作薄的对象, 此为模板文件
			File file = new File(voucherTemplatePath);

			for (int j = 1; j <= size; j++) {
				fileName = execlName + "_" + sdf.format(curDate) +"_[" + j + "]_.xlsx";
				in = new FileInputStream(file);
				workbook = new XSSFWorkbook(in);
			    
				XSSFCellStyle cellStyle = setCellStyle(workbook);
				XSSFSheet sheet = workbook.cloneSheet(0);
				workbook.removeSheetAt(0);
				workbook.setSheetName(0, execlName);
				// 读取模板工作表
				XSSFRow row = null;
				EmpExecutionContext.info("工作薄创建与模板报表移除成功!");

				// 总体流程
				// 根据记录，计算总的页数
				// 每页的处理 一页一个sheet
				// 写入页标题
				// 循环写入该页的行数，一次写一行，每页定义了多少行，就写多少数据。直到数据没有。
				// 写页尾

				int intCnt = mtdList.size() < j * intRowsOfPage ? mtdList
						.size() : j * intRowsOfPage;
						
				XSSFCell[] cell = null;


				if ("true".equals(IsexportAll)) {
					for (int k = (j - 1) * intRowsOfPage; k < intCnt; k++) {

						SendedMttaskVo mt = (SendedMttaskVo) mtdList.get(k);
						//运营商
						Long unicom = mt.getUnicom();
						String unicomString = null;
						if(unicom-0==0){
							unicomString = MessageUtils.extractMessage("appmage", "appmage_javacode_text_13", request);
						}else if(unicom-1==0){
						    unicomString = MessageUtils.extractMessage("appmage", "appmage_javacode_text_14", request);
						}else if(unicom-21==0){
							 unicomString = MessageUtils.extractMessage("appmage", "appmage_javacode_text_15", request);
						}
						// 手机号
						String phone = mt.getPhone() != null ? mt.getPhone()
								: "";
						if (phone != null && !"".equals(phone)
								&& isHidePhone == 0) {
							phone = cv.replacePhoneNumber(phone);
						}
						// 短信文件
						String msg = mt.getMessage() != null ? mt.getMessage()
								: "";
						// 分条
						String icount = mt.getPknumber() + "/"
								+ mt.getPktotal();
						// 状态
						// String errorcode =
						// (mt.getErrorcode().equals("DELIVRD")||mt.getErrorcode().equals("0"))?"发送成功":mt.getErrorcode();
						String errorcode = mt.getErrorcode();
						if (errorcode != null) {
							if (errorcode.equals("DELIVRD")
									|| errorcode.trim().equals("0")) {
								errorcode = MessageUtils.extractMessage("appmage", "appmage_javacode_text_16", request);
							} else if (errorcode.trim().length() > 0) {
								errorcode = MessageUtils.extractMessage("appmage", "appmage_javacode_text_17", request) + "[" + errorcode + "]";
							} else {
								errorcode = "-";
							}
						} else {
							errorcode = "-";
						}

						cell = new XSSFCell[6];
						row = sheet.createRow(k+1);
						cell[0] = row.createCell(0); 
						cell[1] = row.createCell(1); 
						cell[2] = row.createCell(2); 
						cell[3] = row.createCell(3); 
						cell[4] = row.createCell(4); 
						cell[5] = row.createCell(5); 
						
						cell[0].setCellStyle(cellStyle);
						cell[1].setCellStyle(cellStyle);
						cell[2].setCellStyle(cellStyle);
						cell[3].setCellStyle(cellStyle);
						cell[4].setCellStyle(cellStyle);
						cell[5].setCellStyle(cellStyle);
//						String[] value = {String.valueOf(k+1),phone,msg,icount,errorcode};
						
						cell[0].setCellValue(k+1.0);
						cell[1].setCellValue(unicomString);
						cell[2].setCellValue(phone);
						cell[3].setCellValue(msg);
						cell[4].setCellValue(icount);
						cell[5].setCellValue(errorcode);
					}
				} else {
					for (int k = (j - 1) * intRowsOfPage; k < intCnt; k++) {

						SendedMttaskVo mt = (SendedMttaskVo) mtdList.get(k);

						// 手机号
						String phone = mt.getPhone() != null ? mt.getPhone()
								: "";
						if (phone != null && !"".equals(phone)
								&& isHidePhone == 0) {
							phone = cv.replacePhoneNumber(phone);
						}
						
						cell = new XSSFCell[1];
						row = sheet.createRow(k+1);
						
						cell[0] = row.createCell(0); 
						
						cell[0].setCellStyle(cellStyle);
						
						cell[0].setCellValue(phone);
					}
				}
				
				os = new FileOutputStream(voucherFilePath + File.separator + fileName);
				// 写入Excel对象
				workbook.write(os);
			}
			fileName = MessageUtils.extractMessage("appmage", "appmage_javacode_text_18", request) + sdf.format(curDate) + ".zip";
			filePath = BASEDIR + File.separator + voucherPath + File.separator
					+ "groupHisDetail" + File.separator + fileName;

			ZipUtil.compress(voucherFilePath, filePath);
			boolean flag = deleteDir(fileTemp);
			if (!flag) {
			    EmpExecutionContext.error("刪除文件失敗！");
            }
		} catch (Exception e) {
			EmpExecutionContext.error(e,"生成发送详情Excel异常！");
		}finally{
			// 清除对象
			SysuserUtil.closeStream(os);
			SysuserUtil.closeStream(in);
			workbook = null;
		}
		resultMap.put("FILE_NAME", fileName);
		resultMap.put("FILE_PATH", filePath);
		return resultMap;

	}
	
	/**
	 * @description：生成群发历史的excel
	 * @param mtdList
	 *            报表数据
	 * @return Map 生成文件成后，返回文件名和文件路径 throws 生成文件失败
	 * @author songdejun
	 * @see  2012-1-9 创建
	 */

	
    public Map<String, String> createMtReportExcel(List<LfMttaskVo> mtdList, HttpServletRequest request)
			throws Exception {
		
		//String execlName = "群发历史";
		String execlName = "SmsTaskRecord";

		voucherTemplatePath = BASEDIR + File.separator + TEMP
				+ File.separator + "app_currentSmsTaskRecord_" + request.getSession().getAttribute(StaticValue.LANG_KEY) + ".xlsx";
		java.text.SimpleDateFormat df = new java.text.SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");

		Map<String, String> resultMap = new HashMap<String, String>();

		// 当前每页分页条数
		int intRowsOfPage = 500000;

		// 当前每页显示条数
		int intPagesCount = (mtdList.size() % intRowsOfPage == 0) ? (mtdList
				.size() / intRowsOfPage) : (mtdList.size() / intRowsOfPage + 1);

		int size = intPagesCount; // 生成的工作薄个数

		EmpExecutionContext.info("工作表个数：" + size);
		if (size == 0) {
			EmpExecutionContext.info("无统计详情数据！");
			throw new Exception("无统计详情数据！");
		}

		// 产生报表文件的存储路径
		Date curDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddH24mmss");
		String voucherFilePath = BASEDIR + File.separator + voucherPath+File.separator+"groupHistory"+File.separator+sdf.format(curDate);
		File fileTemp = new File(voucherFilePath);
		if(!fileTemp.exists()) {
            fileTemp.mkdirs();
        }
		
		// 报表文件名
		String fileName = null;
		String filePath=null;
		XSSFWorkbook workbook = null;
		FileInputStream in = null;
		FileOutputStream os = null;
		try {
			// 创建只读的Excel工作薄的对象, 此为模板文件
			File file = new File(voucherTemplatePath);
			for (int j = 1; j <= size; j++) {
			
				fileName =execlName +"_"+sdf.format(curDate)+"_["+ j +"].xlsx";
				
				in = new FileInputStream(file);
				workbook = new XSSFWorkbook(in);
				
				XSSFCellStyle cellStyle = setCellStyle(workbook);
	
				XSSFSheet sheet = workbook.cloneSheet(0);
				
				workbook.removeSheetAt(0);
				workbook.setSheetName(0, execlName);
				// 读取模板工作表
				XSSFRow row = null;
			
				
				EmpExecutionContext.info("工作薄创建与模板报表移除成功!");
				// 总体流程
				// 根据记录，计算总的页数
				// 每页的处理 一页一个sheet
				// 写入页标题
				// 循环写入该页的行数，一次写一行，每页定义了多少行，就写多少数据。直到数据没有。
				// 写页尾

				int intCnt = mtdList.size() < j * intRowsOfPage ? mtdList
						.size() : j * intRowsOfPage;
						
				XSSFCell[] cell = new XSSFCell[14];

				for (int k = 0; k < intCnt; k++) {

					LfMttaskVo mt = (LfMttaskVo) mtdList.get(k);

					// 操作员
					String username = mt.getName();
					if (mt.getUserState() != null && mt.getUserState() == 2) {
						username += MessageUtils.extractMessage("appmage", "appmage_smstask_text_36", request);
					}
					// 所属机构
					String depname = mt.getDepName();
					// 发送账号
					String spuser = mt.getSpUser();
					//发送类型
					String taskType="-";
					//if(mt.getTaskType()!=null&&!"".equals(mt.getTaskType())){  //findbugs
					if(mt.getTaskType()!=null){ 
						taskType=mt.getTaskType()==1?MessageUtils.extractMessage("appmage", "appmage_javacode_text_19", request)
								:MessageUtils.extractMessage("appmage", "appmage_javacode_text_20", request);					}
					
					//任务批次
					String taskid=mt.getTaskId()==0?"-":String.valueOf(mt.getTaskId());
					
					// 任务标题
					String title = mt.getTitle() == null ? "-" : mt.getTitle();
					// 发送时间
					String submittime = mt.getTimerTime() == null ? "" : df
							.format(mt.getTimerTime());
					// 发送状态
					String sendState = mt.getSendstate().toString();
					String error = "";
					if (sendState.equals("0")) {
						sendState = MessageUtils.extractMessage("appmage", "appmage_javacode_text_21", request);
					} else if (sendState.equals("1")) {
						sendState = MessageUtils.extractMessage("appmage", "appmage_javacode_text_22", request);
					} else if (sendState.equals("2")) {
						sendState = MessageUtils.extractMessage("appmage", "appmage_javacode_text_23", request);
						if (mt.getErrorCodes() != null && !"".equals(mt.getErrorCodes())) {
							error = "[" + mt.getErrorCodes() + "]";
						}
					} else if (sendState.equals("4")) {
						sendState = MessageUtils.extractMessage("appmage", "appmage_javacode_text_24", request);
					} else if(sendState.equals("6")){
				    	sendState = MessageUtils.extractMessage("appmage", "appmage_javacode_text_25", request);
				    } else if(sendState.equals("3"))
			    	{
						sendState = MessageUtils.extractMessage("appmage", "appmage_javacode_text_26", request);
			    	} else {
			    		sendState = MessageUtils.extractMessage("appmage", "appmage_javacode_text_27", request);
					}
					String sendStateAnderror = sendState + error;
					// 有效号码个数
					String effcount ="-";
					if(mt.getTaskType()==1){
						effcount= mt.getEffCount();
					}
					
					// 提交信息总数
					String icounts = mt.getSendstate() == 2 ? (mt.getIcount() == null ? "0"
							: mt.getIcount())
							: (mt.getIcount2() == null ? "-" : mt.getIcount2());
					// 发送成功总数
					String fail_count = (mt.getFaiCount() == null ? "0" : mt
							.getFaiCount());
					String icount = (mt.getIcount2() == null ? "0" : mt
							.getIcount2());

					// 提交总数
					Long icount1 = Long.parseLong(icount);
					// 提交失败总数
					Long fail = Long.parseLong(fail_count);
					long suc = icount1 - fail;
					if (mt.getSendstate() == 2) {
						suc = 0;
					}
					String succount = suc + "";
					// 发送失败总数
					String failcount = mt.getSendstate() == 2 ? (mt.getIcount() == null ? "0"
							: mt.getIcount())
							: (mt.getFaiCount() == null ? "0" : mt
									.getFaiCount());

					// 接收失败总数
					String recivefailcount = mt.getSendstate() == 2 ? "0" : (mt
							.getRFail2() == null ? "0" : mt.getRFail2()
							.toString());
					
						
					// 短信文件
					String msgContent;
					if (mt.getMsg() == null || "".equals(mt.getMsg())) {
						if(mt.getTaskType()==1){
							msgContent = MessageUtils.extractMessage("appmage", "appmage_javacode_text_28", request);
						}else{
							msgContent = "-";
						}
					} else {
						msgContent = mt.getMsg();
					}
					row = sheet.createRow(k+1);
					for(int i =0;i<cell.length;i++){
						cell[i] = row.createCell(i);
						cell[i].setCellStyle(cellStyle);
					}
					if("-".equals(icounts))
					{
						succount = "-";
						failcount = "-";
						recivefailcount = "-";
					}
					cell[0].setCellValue(username);
					cell[1].setCellValue(depname);
					cell[2].setCellValue(spuser);
					cell[3].setCellValue(taskType);
					cell[4].setCellValue(title);
					cell[5].setCellValue(taskid);
					cell[6].setCellValue(submittime);
					cell[7].setCellValue(sendStateAnderror);
					cell[8].setCellValue(effcount);
					cell[9].setCellValue(icounts);
					cell[10].setCellValue(succount);
					cell[11].setCellValue(failcount);
					cell[12].setCellValue(recivefailcount);
					cell[13].setCellValue(msgContent);

				
				}
				os = new FileOutputStream(voucherFilePath + File.separator + fileName);
				// 写入Excel对象
				workbook.write(os);
			}
			fileName = MessageUtils.extractMessage("appmage", "appmage_javacode_text_29", request) + sdf.format(curDate) + ".zip";
			filePath = BASEDIR + File.separator + voucherPath + File.separator
					+ "groupHistory" + File.separator + fileName;

			ZipUtil.compress(voucherFilePath, filePath);
            boolean flag = deleteDir(fileTemp);
            if (!flag) {
                EmpExecutionContext.error("刪除文件失敗！");
            }
		} catch (Exception e) {
			EmpExecutionContext.error(e,"生成群发历史的excel异常！");
		}finally{
			// 清除对象
			SysuserUtil.closeStream(os);
			SysuserUtil.closeStream(in);
			workbook = null;
		}
		resultMap.put("FILE_NAME", fileName);
		resultMap.put("FILE_PATH", filePath);
		return resultMap;
	}
	
	/**
	 * 群发任务查看导出功能实现
	 * @param mtdList
	 * @return
	 * @throws Exception
	 */
	
    public Map<String, String> createSmsSendedBoxExcel(List<LfMttaskVo> mtdList, HttpServletRequest request)
			throws Exception {
		
		//String execlName = "群发任务";
		String execlName = "SmsSendedBox";

		voucherTemplatePath = BASEDIR + File.separator + TEMP
				+ File.separator + "app_AllSmsSendedBox_" + request.getSession().getAttribute(StaticValue.LANG_KEY) + ".xlsx";
		java.text.SimpleDateFormat df = new java.text.SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");

		Map<String, String> resultMap = new HashMap<String, String>();
		// 当前每页分页条数
		int intRowsOfPage = 500000;

		// 当前每页显示条数
		int intPagesCount = (mtdList.size() % intRowsOfPage == 0) ? (mtdList
				.size() / intRowsOfPage) : (mtdList.size() / intRowsOfPage + 1);

		int size = intPagesCount; // 生成的工作薄个数

		EmpExecutionContext.info("工作表个数：" + size);
		if (size == 0) {
			EmpExecutionContext.info("无统计详情数据！");
			throw new Exception("无统计详情数据！");
		}

		// 产生报表文件的存储路径
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddH24mmss");
		Date curDate = new Date();
		String voucherFilePath = BASEDIR + File.separator + voucherPath+File.separator+"groupSms"+File.separator+sdf.format(curDate);
		
		File fileTemp = new File(voucherFilePath);

		if (!fileTemp.exists()) {
			fileTemp.mkdirs();
		}
		
		
		// 报表文件名
		String fileName = null;
		String filePath = null;
		XSSFWorkbook workbook = null;
		
		InputStream in = null;
		OutputStream os = null;
		
		
		try {
			// 创建只读的Excel工作薄的对象, 此为模板文件
			File file = new File(voucherTemplatePath);
			
			for(int f = 0; f< size;f++){
				fileName = execlName+"_"+ sdf.format(curDate) + "_[" + (f+1) + "].xlsx";
				//读取模板
				in = new FileInputStream(file);
				
				workbook = new XSSFWorkbook(in);
				
				XSSFCellStyle cellStyle = setCellStyle(workbook);
				
				XSSFSheet sheet = workbook.cloneSheet(0);
				workbook.removeSheetAt(0);
				workbook.setSheetName(0, execlName);
				// 读取模板工作表
				XSSFRow row = null;
				EmpExecutionContext.debug("工作薄创建与模板移除成功!");
				
				
				XSSFCell[] cell = new XSSFCell[11];

				for (int k = 0; k < mtdList.size(); k++) {
					LfMttaskVo mt = (LfMttaskVo) mtdList.get(k);
					// 操作员
					String username = mt.getName();
					if (mt.getUserState() != null && mt.getUserState() == 2) {
						username = username + MessageUtils.extractMessage("appmage", "appmage_smstask_text_36", request);
					}
					// 所属机构
					String depname = mt.getDepName();
					String taskType="-";
					//if(mt.getTaskType()!=null&&!"".equals(mt.getTaskType())){  //findbugs
					if(mt.getTaskType()!=null){  
						taskType=mt.getTaskType()==1?MessageUtils.extractMessage("appmage", "appmage_javacode_text_19", request)
								:MessageUtils.extractMessage("appmage", "appmage_javacode_text_20", request);
					}
					
					//任务批次
					String taskid=mt.getTaskId()==0?"-":String.valueOf(mt.getTaskId());
					
					// 发送账号
					String spuser = mt.getSpUser();
					// 任务标题
					String title = mt.getTitle() == null ? "" : mt.getTitle();
					// 创建时间
					String createtime = mt.getSubmitTime() == null ? "-" : df
							.format(mt.getSubmitTime());

					// 发送时间
					String sendedTime = "-";
					if (mt.getReState() == 2) {// 审批不通过 （发送时间为空）
						sendedTime = "-";
					} else if (mt.getSubState() == 3) {// 撤销任务（空）
						sendedTime = "-";
					} else if (mt.getSendstate() == 5) {// 超时未发送（空）
						sendedTime = "-";
					} else if (mt.getTimerStatus() == 0
							&& mt.getReState() == -1) {// 未定时未审批（待审批）（空）
						sendedTime = "-";
					} else if (mt.getTimerStatus() == 1) {// 定时了
						sendedTime = df.format(mt.getTimerTime());
						if (mt.getSendstate() == 0) {
							sendedTime += MessageUtils.extractMessage("appmage", "appmage_javacode_text_30", request);
						}
					} else if (mt.getSendstate() == 1 || mt.getSendstate() == 2) {// 发送成功或者发送失败
						sendedTime = df.format(mt.getTimerTime());
					} else {
						sendedTime = mt.getTimerTime() == null ? "-" : df
								.format(mt.getTimerTime());// 这里面的情况就是sendstate=4(发送中)
					}

					// 有效号码个数
					// 有效号码个数
					String effcount ="-";
					if(mt.getTaskType()==1){
						effcount= mt.getEffCount();
					}

					// 短信文件
					String msgContent;
					if (mt.getMsg() == null || "".equals(mt.getMsg())) {
						if(mt.getTaskType()==1){
							msgContent = MessageUtils.extractMessage("appmage", "appmage_javacode_text_31", request);
						}else{
							msgContent = "-";
						}
					} else {
						msgContent = mt.getMsg();
					}

					// 任务状态
					Integer sendState = mt.getSendstate();
					Integer subState = mt.getSubState();
					String state = new String();
					if (subState == 2 && mt.getReState() == -1) {
						state = MessageUtils.extractMessage("appmage", "appmage_javacode_text_32", request);
					} else if (subState == 2 && mt.getReState() == 2) {
						state = MessageUtils.extractMessage("appmage", "appmage_javacode_text_33", request);
					} else if (subState == 4) {
						state = MessageUtils.extractMessage("appmage", "appmage_javacode_text_34", request);
					} else if (subState == 3) {
						state = MessageUtils.extractMessage("appmage", "appmage_javacode_text_35", request);
					} else if (sendState == 5) {
						state = MessageUtils.extractMessage("appmage", "appmage_javacode_text_36", request);
					} else if (sendState != 0) {
						state = MessageUtils.extractMessage("appmage", "appmage_javacode_text_37", request);
					} else if (subState == 2) {
						if (mt.getReState() == -1) {
                            state = MessageUtils.extractMessage("appmage", "appmage_javacode_text_32", request);
                        } else if (mt.getReState() == 2) {
                            state = MessageUtils.extractMessage("appmage", "appmage_javacode_text_33", request);
                        } else {
                            state = MessageUtils.extractMessage("appmage", "appmage_javacode_text_38", request);
                        }
				   }
					row = sheet.createRow(k+1);
					
					cell[0] = row.createCell(0);
					cell[1] = row.createCell(1);
					cell[2] = row.createCell(2);
					cell[3] = row.createCell(3);
					cell[4] = row.createCell(4);
					cell[5] = row.createCell(5);
					cell[6] = row.createCell(6);
					cell[7] = row.createCell(7);
					cell[8] = row.createCell(8);
					cell[9] = row.createCell(9);
					cell[10] = row.createCell(10);
					
					
					cell[0].setCellStyle(cellStyle);
					cell[1].setCellStyle(cellStyle);
					cell[2].setCellStyle(cellStyle);
					cell[3].setCellStyle(cellStyle);
					cell[4].setCellStyle(cellStyle);
					cell[5].setCellStyle(cellStyle);
					cell[6].setCellStyle(cellStyle);
					cell[7].setCellStyle(cellStyle);
					cell[8].setCellStyle(cellStyle);
					cell[9].setCellStyle(cellStyle);
					cell[10].setCellStyle(cellStyle);
					
					cell[0].setCellValue(username);
					cell[1].setCellValue(depname);
					cell[2].setCellValue(spuser);
					cell[3].setCellValue(taskType);
					cell[4].setCellValue(title);
					cell[5].setCellValue(taskid);
					cell[6].setCellValue(createtime);
					cell[7].setCellValue(sendedTime);
					cell[8].setCellValue(effcount);
					cell[9].setCellValue(msgContent);
					cell[10].setCellValue(state);
				}
				os = new FileOutputStream(voucherFilePath + File.separator + fileName);
				// 写入Excel对象
				workbook.write(os);
			}
			fileName = MessageUtils.extractMessage("appmage", "appmage_javacode_text_39", request) + sdf.format(curDate) + ".zip";
			filePath = BASEDIR + File.separator + voucherPath + File.separator
					+ "groupSms" + File.separator + fileName;

			ZipUtil.compress(voucherFilePath, filePath);
            boolean flag = deleteDir(fileTemp);
            if (!flag) {
                EmpExecutionContext.error("刪除文件失敗！");
            }
		} catch (Exception e) {
			EmpExecutionContext.error(e,"群发任务查看导出异常！");
		}finally{
			// 清除对象
			SysuserUtil.closeStream(os);
			SysuserUtil.closeStream(in);
			workbook = null;
		}
		resultMap.put("FILE_NAME", fileName);
		resultMap.put("FILE_PATH", filePath);
		return resultMap;
	}
	
	// 将设置单元格属性提取出来成一个方法，方便其他模块调用
	
    public XSSFCellStyle setCellStyle(XSSFWorkbook workbook) {
		XSSFCellStyle cellStyle = workbook.createCellStyle();

		XSSFFont font = workbook.createFont();
		// 字体名称
		font.setFontName("TAHOMA");
		// 粗体
		font.setBold(false);
		// 下环线
		font.setUnderline(FontUnderline.NONE);
		// 字体大小
		font.setFontHeight(11);
		cellStyle.setFont(font);
		// 水平对齐
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		// 竖直对齐
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle.setWrapText(true);

		return cellStyle;
	}
	
	private static boolean deleteDir(File dir) {

		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}
}
