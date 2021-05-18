package com.montnets.emp.netnews.biz;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
import com.montnets.emp.common.vo.SendedMttaskVo;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.netnews.vo.LfMttaskVo;
import com.montnets.emp.util.ZipUtil;


public class SmstaskExcelTool {

	// 报表模板目录
	private static String BASEDIR = "";

	// 操作员报表文件路径
    private static String voucherTemplatePath = "";

    private static final String voucherPath = "download";

    private static final String TEMP = "temp";

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
			Integer isHidePhone) throws Exception {
		
		//String execlName = "群发历史详情";
		String execlName = "smt_smsTaskRecord";

		if ("true".equals(IsexportAll)) {
			voucherTemplatePath = BASEDIR + File.separator
					+ TEMP + File.separator
					+ "smt_smsTaskRecord.xlsx";
		} else {
			voucherTemplatePath = BASEDIR + File.separator
					+ TEMP + File.separator
					+ "smt_smsTaskRecordOnlyPhone.xlsx";
		}

		Map<String, String> resultMap = new HashMap<String, String>();

		// 当前每页分页条数
		int intRowsOfPage = 500000;

		// 当前每页显示条数
		int intPagesCount = (mtdList.size() % intRowsOfPage == 0) ? (mtdList
				.size() / intRowsOfPage) : (mtdList.size() / intRowsOfPage + 1);

		int size = intPagesCount; // 生成的工作薄个数

		EmpExecutionContext.info("网讯发送详情的excel导出，工作表个数：" + size);
		if (size == 0) {
			EmpExecutionContext.info("网讯发送详情的excel导出无数据！");
			throw new Exception("网讯发送详情的excel导出无数据！");
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
				EmpExecutionContext.info("网讯发送详情的excel导出，工作薄创建与模板报表移除成功!");

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
							unicomString = "移动";
						}else if(unicom-1==0){
						    unicomString ="联通";
						}else if(unicom-21==0){
							 unicomString ="电信";
						}else if(unicom-5==0){
							unicomString ="国外";
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
								errorcode = "发送成功";
							} else if (errorcode.trim().length() > 0) {
								errorcode = "失败[" + errorcode + "]";
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
						
						cell[0].setCellValue(k+1D);
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
			fileName = "网讯发送详情" + sdf.format(curDate) + ".zip";
			filePath = BASEDIR + File.separator + voucherPath + File.separator
					+ "groupHisDetail" + File.separator + fileName;

			ZipUtil.compress(voucherFilePath, filePath);
            boolean status = deleteDir(fileTemp);
            if (!status) {
                EmpExecutionContext.error("刪除文件失敗！");
            }
		} catch (Exception e) {
			EmpExecutionContext.error(e,"网讯发送详情导出失败");
		}finally{
			// 清除对象
            try {
                if(os != null){
                    os.close();
                }
                if(in != null){
                    in.close();
                }
            } catch (Exception e) {
                EmpExecutionContext.error("流关闭异常！");
            }
			workbook = null;
		}
		resultMap.put("FILE_NAME", fileName);
		resultMap.put("FILE_PATH", filePath);
		return resultMap;

	}
	
	/**
	 * 网讯发送统计详情导出excel
	 * @param mtdList 导出数据
	 * @param IsexportAll 是否导出详细信息
	 * @param isHidePhone 是否隐藏手机号
	 * @return 返回文件名和路径map集合
	 */
	public Map<String, String> createSmsMtDetailExcel(List<SendedMttaskVo> mtdList, String IsexportAll, Integer isHidePhone) 
	{
		if(mtdList == null || mtdList.size() == 0)
		{
			EmpExecutionContext.error("网讯发送统计详情导出，生成excel，下行记录对象集合为空。");
			return null;
		}

		if ("true".equals(IsexportAll)) 
		{
			voucherTemplatePath = BASEDIR + File.separator
					+ TEMP + File.separator
					+ "smt_smsTaskRecord.xlsx";
		}
		else 
		{
			voucherTemplatePath = BASEDIR + File.separator
					+ TEMP + File.separator
					+ "smt_smsTaskRecordOnlyPhone.xlsx";
		}

		// 当前每页分页条数
		int intRowsOfPage = 500000;

		// 当前每页显示条数
		int intPagesCount = (mtdList.size() % intRowsOfPage == 0) ? (mtdList
				.size() / intRowsOfPage) : (mtdList.size() / intRowsOfPage + 1);

		// 生成的工作薄个数
		int size = intPagesCount;

		// 产生报表文件的存储路径
		Date curDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddH24mmss");
		String voucherFilePath = BASEDIR + File.separator + voucherPath+File.separator+"groupHisDetail"+File.separator+sdf.format(curDate);
		File fileTemp = new File(voucherFilePath);
		
		// 报表文件名
		String fileName;
		String filePath;
		XSSFWorkbook workbook;
		FileInputStream in = null;
		FileOutputStream os = null;
		try 
		{
			if(!fileTemp.exists())
			{
				fileTemp.mkdirs();
			}
			// 创建只读的Excel工作薄的对象, 此为模板文件
			File file = new File(voucherTemplatePath);
			String execlName = "smt_smsTaskRecord";
			for (int j = 1; j <= size; j++) 
			{
				fileName = execlName + "_" + sdf.format(curDate) +"_[" + j + "]"+"_"+ StaticValue.getServerNumber() +".xlsx";
				in = new FileInputStream(file);
				workbook = new XSSFWorkbook(in);
			    
				XSSFCellStyle cellStyle = setCellStyle(workbook);
				XSSFSheet sheet = workbook.cloneSheet(0);
				workbook.removeSheetAt(0);
				workbook.setSheetName(0, execlName);
				// 读取模板工作表
				XSSFRow row;

				// 总体流程
				// 根据记录，计算总的页数
				// 每页的处理 一页一个sheet
				// 写入页标题
				// 循环写入该页的行数，一次写一行，每页定义了多少行，就写多少数据。直到数据没有。
				// 写页尾

				int intCnt = mtdList.size() < j * intRowsOfPage ? mtdList.size() : j * intRowsOfPage;
						
				XSSFCell[] cell;

				if ("true".equals(IsexportAll)) 
				{
					for (int k = (j - 1) * intRowsOfPage; k < intCnt; k++) 
					{
						SendedMttaskVo mt = mtdList.get(k);
						//运营商
						Long unicom = mt.getUnicom();
						String unicomString;
						if(unicom == 0)
						{
							unicomString = "移动";
						}
						else if(unicom ==1)
						{
						    unicomString ="联通";
						}
						else if(unicom == 21)
						{
							 unicomString ="电信";
						}
						else if(unicom == 5)
						{
							unicomString ="国外";
						}
						else
						{
							unicomString ="-";
						}
						// 手机号
						String phone = mt.getPhone() != null ? mt.getPhone() : "";
						if (phone != null && phone.length() > 0 && isHidePhone == 0) 
						{
							phone = cv.replacePhoneNumber(phone);
						}
						// 短信文件
						String msg = mt.getMessage() != null ? mt.getMessage() : "";
						// 分条
						String icount = mt.getPknumber() + "/" + mt.getPktotal();
						String errorcode = mt.getErrorcode();
						if(errorcode == null)
						{
							errorcode = "-";
						}
						else if ("DELIVRD".equals(errorcode) || "0".equals(errorcode.trim())) 
						{
							errorcode = "发送成功";
						} 
						else if (errorcode.trim().length() > 0) 
						{
							errorcode = "失败[" + errorcode + "]";
						} 
						else 
						{
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
						
						cell[0].setCellValue(k+1D);
						cell[1].setCellValue(unicomString);
						cell[2].setCellValue(phone);
						cell[3].setCellValue(msg);
						cell[4].setCellValue(icount);
						cell[5].setCellValue(errorcode);
					}
				} 
				else 
				{
					for (int k = (j - 1) * intRowsOfPage; k < intCnt; k++) 
					{
						SendedMttaskVo mt = mtdList.get(k);

						// 手机号
						String phone = mt.getPhone() != null ? mt.getPhone() : "";
						if (phone != null && phone.length() > 0 && isHidePhone == 0) 
						{
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
			fileName = "网讯发送详情" + sdf.format(curDate) +"_"+ StaticValue.getServerNumber() + ".zip";
			filePath = BASEDIR + File.separator + voucherPath + File.separator
					+ "groupHisDetail" + File.separator + fileName;

			ZipUtil.compress(voucherFilePath, filePath);
            boolean status = deleteDir(fileTemp);
            if (!status) {
                EmpExecutionContext.error("刪除文件失敗！");
            }
			
			Map<String, String> resultMap = new HashMap<String, String>();
			resultMap.put("FILE_NAME", fileName);
			resultMap.put("FILE_PATH", filePath);
			return resultMap;
		} 
		catch (Exception e) 
		{
			EmpExecutionContext.error(e,"网讯发送统计详情导出，生成excel，异常。");
			return null;
		}
		finally
		{
			// 清除对象
			try
			{
				if(os != null)
				{
					os.close();
				}
				if(in != null)
				{
					in.close();
				}
			}
			catch (IOException e)
			{
				EmpExecutionContext.error(e, "网讯发送统计详情导出，生成excel，关闭资源异常。");
			}
			workbook = null;
		}
	}
	
	/**
	 * @description：生成群发历史的excel
	 * @param fileName
	 *            文件名
	 * @param mtdList
	 *            报表数据
	 * @return Map 生成文件成后，返回文件名和文件路径 throws 生成文件失败
	 * @author songdejun
	 * @see songdejun 2012-1-9 创建
	 */

	
    public Map<String, String> createMtReportExcel(List<LfMttaskVo> mtdList)
			throws Exception {
		
		//String execlName = "群发历史";
		String execlName = "SmsTaskRecord";

		voucherTemplatePath = BASEDIR + File.separator + TEMP
				+ File.separator + "smt_currentSmsTaskRecord.xlsx";
		java.text.SimpleDateFormat df = new java.text.SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");

		Map<String, String> resultMap = new HashMap<String, String>();

		// 当前每页分页条数
		int intRowsOfPage = 500000;

		// 当前每页显示条数
		int intPagesCount = (mtdList.size() % intRowsOfPage == 0) ? (mtdList
				.size() / intRowsOfPage) : (mtdList.size() / intRowsOfPage + 1);

		int size = intPagesCount; // 生成的工作薄个数

		EmpExecutionContext.info("群发历史查询的excel导出，工作表个数：" + size);
		if (size == 0) {
			EmpExecutionContext.info("群发历史查询的excel导出无数据！");
			throw new Exception("群发历史查询的excel导出无数据！");
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
			
				fileName =execlName +"_"+sdf.format(curDate)+"_["+ j +"]"+"_"+ StaticValue.getServerNumber() +".xlsx";
				
				in = new FileInputStream(file);
				workbook = new XSSFWorkbook(in);
				
				XSSFCellStyle cellStyle = setCellStyle(workbook);
	
				XSSFSheet sheet = workbook.cloneSheet(0);
				
				workbook.removeSheetAt(0);
				workbook.setSheetName(0, execlName);
				// 读取模板工作表
				XSSFRow row = null;
			
				
				EmpExecutionContext.info("群发历史查询的工作薄创建与模板报表移除成功!");
				// 总体流程
				// 根据记录，计算总的页数
				// 每页的处理 一页一个sheet
				// 写入页标题
				// 循环写入该页的行数，一次写一行，每页定义了多少行，就写多少数据。直到数据没有。
				// 写页尾

				int intCnt = mtdList.size() < j * intRowsOfPage ? mtdList
						.size() : j * intRowsOfPage;
						
				XSSFCell[] cell = new XSSFCell[15];

				for (int k = 0; k < intCnt; k++) {

					LfMttaskVo mt = (LfMttaskVo) mtdList.get(k);

					// 操作员
					String username = mt.getName();
					if (mt.getUserState() != null && mt.getUserState() == 2) {
						username += "(已注销)";
					}
					// 所属机构
					String depname = mt.getDepName();
					// 发送账号
					String spuser = mt.getSpUser();
					// 任务标题
					String title = mt.getTitle() == null ? "-" : mt.getTitle();
					
					// 任务批次
					Long taskid=mt.getTaskId()!=null?mt.getTaskId():0l;

					String staskid =taskid-0!=0?(taskid+""):"--";
					
					// 网讯编号
					String netid = mt.getNetid() == null ? "-" : mt.getNetid();
					// 网讯名称
					String netname = mt.getNetname() == null ? "-" : mt.getNetname();
					// 发送时间
					String submittime = mt.getTimerTime() == null ? "" : df
							.format(mt.getTimerTime());
					// 发送状态
					String sendState = mt.getSendstate().toString();
					String error = "";
					if (sendState.equals("0")) {
						sendState = "未发送";
					} else if (sendState.equals("1")) {
						sendState = "网关接收成功";
					} else if (sendState.equals("2")) {
						sendState = "失败";
						if (mt.getErrorCodes() != null) {
							error = "[" + mt.getErrorCodes() + "]";
						}
					} else if (sendState.equals("4")) {
						sendState = "EMP发送中";
					} else if(sendState.equals("6")){
				    	sendState = "终止发送";
				    } else if(sendState.equals("3"))
			    	{
						sendState = "网关处理完成";
			    	} else {
			    		sendState = "未使用";
					}
					String sendStateAnderror = sendState + error;
					// 有效号码个数
					String effcount = mt.getEffCount();
					// 提交信息总数
					String icounts = mt.getSendstate()==2?(mt.getIcount()==null?"0":mt.getIcount()):(mt.getIcount2()==null?"-":mt.getIcount2());
					String succount="-";	
					// 发送成功总数
					String fail_count=(mt.getFaiCount()==null?"0":mt.getFaiCount());
					String icount=(mt.getIcount2()==null?"0":mt.getIcount2());
					
					//提交总数
					Long icount1=Long.parseLong(icount);
					//提交失败总数
					Long fail=Long.parseLong(fail_count);
					long suc=icount1-fail;
					if(mt.getSendstate()==2)
					{
						succount="0";
					}
					else if(mt.getIcount2()==null)
					{
						succount="-";
					}
					else
					{
						succount=suc+"";
					}

				// 发送失败总数
					String failcount = mt.getSendstate()==2?(mt.getIcount()==null?"0":mt.getIcount()):(mt.getIcount2()==null?"-":mt.getFaiCount());

					// 接收失败总数
					String recivefailcount = mt.getSendstate()==2?"0":(mt.getIcount2()==null?"-":(mt.getRFail2()==null?"-":mt.getRFail2().toString()));
					

					// 短信文件
					String msgContent;
					if (mt.getMsg() == null || "".equals(mt.getMsg())) {
						msgContent = "详见文件";
					} else {
						msgContent = mt.getMsg().replaceAll("#[pP]_(\\d+)#","{#参数$1#}");
					}
					row = sheet.createRow(k+1);
					for(int i =0;i<cell.length;i++){
						cell[i] = row.createCell(i);
						cell[i].setCellStyle(cellStyle);
					}
					
					cell[0].setCellValue(username);
					cell[1].setCellValue(depname);
					cell[2].setCellValue(spuser);
					cell[3].setCellValue(title);
					cell[4].setCellValue(staskid);
					cell[5].setCellValue(netid);
					cell[6].setCellValue(netname);
					cell[7].setCellValue(submittime);
					cell[8].setCellValue(sendStateAnderror);
					cell[9].setCellValue(effcount);
					cell[10].setCellValue(icounts);
					cell[11].setCellValue(succount);
					cell[12].setCellValue(failcount);
					cell[13].setCellValue(recivefailcount);
					cell[14].setCellValue(msgContent);
				}
				os = new FileOutputStream(voucherFilePath + File.separator + fileName);
				// 写入Excel对象
				workbook.write(os);
			}
			fileName = "网讯" + sdf.format(curDate) +"_"+ StaticValue.getServerNumber() + ".zip";
			filePath = BASEDIR + File.separator + voucherPath + File.separator
					+ "groupHistory" + File.separator + fileName;

			ZipUtil.compress(voucherFilePath, filePath);
            boolean status = deleteDir(fileTemp);
            if (!status) {
                EmpExecutionContext.error("刪除文件失敗！");
            }
		} catch (Exception e) {
			EmpExecutionContext.error(e,"网讯群发历史查询导出异常");
		}finally{
			// 清除对象
            try {
                if(os != null){
                    os.close();
                }
                if(in != null){
                    in.close();
                }
            } catch (Exception e) {
                EmpExecutionContext.error("流关闭异常！");
            }
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
	
    public Map<String, String> createSmsSendedBoxExcel(List<LfMttaskVo> mtdList)
			throws Exception {
		
		//String execlName = "网讯发送查询";
		String execlName = "WebexSendquery";

		voucherTemplatePath = BASEDIR + File.separator + TEMP
				+ File.separator + "wx_WebexSendquery.xlsx";
		java.text.SimpleDateFormat df = new java.text.SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");

		Map<String, String> resultMap = new HashMap<String, String>();
		// 当前每页分页条数
		int intRowsOfPage = 500000;

		// 当前每页显示条数
		int intPagesCount = (mtdList.size() % intRowsOfPage == 0) ? (mtdList
				.size() / intRowsOfPage) : (mtdList.size() / intRowsOfPage + 1);

		int size = intPagesCount; // 生成的工作薄个数

		EmpExecutionContext.info("群发任务查看导出，工作表个数：" + size);
		if (size == 0) {
			EmpExecutionContext.info("群发任务查看导出无数据！");
			throw new Exception("群发任务查看导出无数据！");
		}

		// 产生报表文件的存储路径
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddH24mmss");
		Date curDate = new Date();
		String voucherFilePath = BASEDIR + File.separator + voucherPath+File.separator+"groupWebex"+File.separator+sdf.format(curDate);
		
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
				fileName = execlName+"_"+ sdf.format(curDate) + "_[" + (f+1) + "]"+"_"+ StaticValue.getServerNumber() +".xlsx";
				//读取模板
				in = new FileInputStream(file);
				
				workbook = new XSSFWorkbook(in);
				
				XSSFCellStyle cellStyle = setCellStyle(workbook);
				
				XSSFSheet sheet = workbook.cloneSheet(0);
				workbook.removeSheetAt(0);
				workbook.setSheetName(0, execlName);
				// 读取模板工作表
				XSSFRow row = null;
				EmpExecutionContext.debug("群发任务查看的工作薄创建与模板移除成功!");
				
				
				XSSFCell[] cell = new XSSFCell[11];

				for (int k = 0; k < mtdList.size(); k++) {
					LfMttaskVo mt = (LfMttaskVo) mtdList.get(k);
					// 操作员
					String username = mt.getName();
					if (mt.getUserState() != null && mt.getUserState() == 2) {
						username = username + "(已注销)";
					}
					// 所属机构
					String depname = mt.getDepName();
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
							sendedTime += "(定时中)";
						}
					} else if (mt.getSendstate() == 1 || mt.getSendstate() == 2) {// 发送成功或者发送失败
						sendedTime = df.format(mt.getTimerTime());
					} else {
						sendedTime = mt.getTimerTime() == null ? "-" : df
								.format(mt.getTimerTime());// 这里面的情况就是sendstate=4(发送中)
					}

					// 有效号码个数
					String effcount = mt.getEffCount();

					// 短信文件
					String msgContent;
					if (mt.getMsg() == null || "".equals(mt.getMsg())) {
						msgContent = "详见文件";
					} else {
						msgContent = mt.getMsg().replaceAll("#[pP]_(\\d+)#","{#参数$1#}");
					}

					// 任务状态
					Integer sendState = mt.getSendstate();
					Integer subState = mt.getSubState();
					String state = new String();
					if (subState == 2 && mt.getReState() == -1) {
						state = "待审批";
					} else if (subState == 2 && mt.getReState() == 2) {
						state = "审批不通过";
					} else if (subState == 4) {
						state = "已冻结";
					} else if (subState == 3) {
						state = "已撤销";
					} else if (sendState == 5) {
						state = "超时未发送";
					} else if (sendState != 0) {
						state = "已发送";
					} else if (subState == 2) {
						if (mt.getReState() == -1) {
                            state = "待审批";
                        } else if (mt.getReState() == 2) {
                            state = "审批不通过";
                        } else {
                            state = "待发送";
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
					
					cell[0].setCellValue(username);
					cell[1].setCellValue(depname);
					cell[2].setCellValue(spuser);
					cell[3].setCellValue(title);
					cell[4].setCellValue(mt.getNetid()==null?"":mt.getNetid());
					cell[5].setCellValue(mt.getNetname()==null?"":mt.getNetname());
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
			fileName = "网讯发送查询" + sdf.format(curDate) +"_"+ StaticValue.getServerNumber() + ".zip";
			filePath = BASEDIR + File.separator + voucherPath + File.separator
					+ "groupWebex" + File.separator + fileName;

			ZipUtil.compress(voucherFilePath, filePath);
            boolean status = deleteDir(fileTemp);
            if (!status) {
                EmpExecutionContext.error("刪除文件失敗！");
            }
		} catch (Exception e) {
			EmpExecutionContext.error(e,"群发任务查看的导出失败");
		}finally{
			// 清除对象
            try {
                if(os != null){
                    os.close();
                }
                if(in != null){
                    in.close();
                }
            } catch (Exception e) {
                EmpExecutionContext.error("流关闭异常！");
            }
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
	/**
	 * 网讯发送统计详情导出excel全球化
	 * @param mtdList 导出数据
	 * @param IsexportAll 是否导出详细信息
	 * @param isHidePhone 是否隐藏手机号
	 * @return 返回文件名和路径map集合
	 */
	public Map<String, String> createSmsMtDetailExcel(List<SendedMttaskVo> mtdList, String IsexportAll, Integer isHidePhone,HttpServletRequest request) 
	{
		if(mtdList == null || mtdList.size() == 0)
		{
			EmpExecutionContext.error("网讯发送统计详情导出，生成excel，下行记录对象集合为空。");
			return null;
		}
		//语言格式
		String empLangName = request.getParameter("empLangName");
		if ("true".equals(IsexportAll)) 
		{
			voucherTemplatePath = BASEDIR + File.separator
					+ TEMP + File.separator
					+ "smt_smsTaskRecord_"+empLangName+".xlsx";
		}
		else 
		{
			voucherTemplatePath = BASEDIR + File.separator
					+ TEMP + File.separator
					+ "smt_smsTaskRecordOnlyPhone_"+empLangName+".xlsx";
		}

		// 当前每页分页条数
		int intRowsOfPage = 500000;

		// 当前每页显示条数
		int intPagesCount = (mtdList.size() % intRowsOfPage == 0) ? (mtdList
				.size() / intRowsOfPage) : (mtdList.size() / intRowsOfPage + 1);

		// 生成的工作薄个数
		int size = intPagesCount;

		// 产生报表文件的存储路径
		Date curDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddH24mmss");
		String voucherFilePath = BASEDIR + File.separator + voucherPath+File.separator+"groupHisDetail"+File.separator+sdf.format(curDate);
		File fileTemp = new File(voucherFilePath);
		
		// 报表文件名
		String fileName;
		String filePath;
		XSSFWorkbook workbook;
		FileInputStream in = null;
		FileOutputStream os = null;
		try 
		{
			if(!fileTemp.exists())
			{
				fileTemp.mkdirs();
			}
			// 创建只读的Excel工作薄的对象, 此为模板文件
			File file = new File(voucherTemplatePath);
			String execlName = "smt_smsTaskRecord";
			for (int j = 1; j <= size; j++) 
			{
				fileName = execlName + "_" + sdf.format(curDate) +"_[" + j + "]"+"_"+ StaticValue.getServerNumber() +".xlsx";
				in = new FileInputStream(file);
				workbook = new XSSFWorkbook(in);
			    
				XSSFCellStyle cellStyle = setCellStyle(workbook);
				XSSFSheet sheet = workbook.cloneSheet(0);
				workbook.removeSheetAt(0);
				workbook.setSheetName(0, execlName);
				// 读取模板工作表
				XSSFRow row;

				// 总体流程
				// 根据记录，计算总的页数
				// 每页的处理 一页一个sheet
				// 写入页标题
				// 循环写入该页的行数，一次写一行，每页定义了多少行，就写多少数据。直到数据没有。
				// 写页尾

				int intCnt = mtdList.size() < j * intRowsOfPage ? mtdList.size() : j * intRowsOfPage;
						
				XSSFCell[] cell;

				if ("true".equals(IsexportAll)) 
				{
					for (int k = (j - 1) * intRowsOfPage; k < intCnt; k++) 
					{
						SendedMttaskVo mt = mtdList.get(k);
						//运营商
						Long unicom = mt.getUnicom();
						String unicomString;
						if(unicom == 0)
						{
							unicomString = MessageUtils.extractMessage("ydwx","ydwx_jsp_out_10",request);
						}
						else if(unicom ==1)
						{
						    unicomString =MessageUtils.extractMessage("ydwx","ydwx_jsp_out_11",request);
						}
						else if(unicom == 21)
						{
							 unicomString =MessageUtils.extractMessage("ydwx","ydwx_jsp_out_12",request);
						}
						else if(unicom == 5)
						{
							unicomString =MessageUtils.extractMessage("ydwx","ydwx_jsp_out_14",request);
						}
						else
						{
							unicomString ="-";
						}
						// 手机号
						String phone = mt.getPhone() != null ? mt.getPhone() : "";
						if (phone != null && phone.length() > 0 && isHidePhone == 0) 
						{
							phone = cv.replacePhoneNumber(phone);
						}
						// 短信文件
						String msg = mt.getMessage() != null ? mt.getMessage() : "";
						// 分条
						String icount = mt.getPknumber() + "/" + mt.getPktotal();
						String errorcode = mt.getErrorcode();
						if(errorcode == null)
						{
							errorcode = "-";
						}
						else if ("DELIVRD".equals(errorcode) || "0".equals(errorcode.trim())) 
						{
							errorcode = MessageUtils.extractMessage("ydwx","ydwx_add_jsp_23",request);
						} 
						else if (errorcode.trim().length() > 0) 
						{
							errorcode = MessageUtils.extractMessage("ydwx","ydwx_add_jsp_24",request)+"[" + errorcode + "]";
						} 
						else 
						{
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
						  
						cell[0].setCellValue(k+1D);
						cell[1].setCellValue(unicomString);
						cell[2].setCellValue(phone);
						cell[3].setCellValue(msg);
						cell[4].setCellValue(icount);
						cell[5].setCellValue(errorcode);
					}
				} 
				else 
				{
					for (int k = (j - 1) * intRowsOfPage; k < intCnt; k++) 
					{
						SendedMttaskVo mt = mtdList.get(k);

						// 手机号
						String phone = mt.getPhone() != null ? mt.getPhone() : "";
						if (phone != null && phone.length() > 0 && isHidePhone == 0) 
						{
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
			fileName = MessageUtils.extractMessage("ydwx","ydwx_jsp_out_74",request) + sdf.format(curDate) +"_"+ StaticValue.getServerNumber() + ".zip";
			filePath = BASEDIR + File.separator + voucherPath + File.separator
					+ "groupHisDetail" + File.separator + fileName;

			ZipUtil.compress(voucherFilePath, filePath);
            boolean status = deleteDir(fileTemp);
            if (!status) {
                EmpExecutionContext.error("刪除文件失敗！");
            }
			
			Map<String, String> resultMap = new HashMap<String, String>();
			resultMap.put("FILE_NAME", fileName);
			resultMap.put("FILE_PATH", filePath);
			return resultMap;
		} 
		catch (Exception e) 
		{
			EmpExecutionContext.error(e,"网讯发送统计详情导出，生成excel，异常。");
			return null;
		}
		finally
		{
			// 清除对象
			try
			{
				if(os != null)
				{
					os.close();
				}
				if(in != null)
				{
					in.close();
				}
			}
			catch (IOException e)
			{
				EmpExecutionContext.error(e, "网讯发送统计详情导出，生成excel，关闭资源异常。");
			}
			workbook = null;
		}
	}

	/**
	 * 生成群发历史的excel国际化语言
	 * @param mtdList
	 * @param request
	 * @return
	 * @throws Exception
	 */

	
    public Map<String, String> createMtReportExcel(List<LfMttaskVo> mtdList, HttpServletRequest request)
			throws Exception {
		
		//String execlName = "群发历史";
		String execlName = "SmsTaskRecord";
		//语言格式
		String empLangName = request.getParameter("empLangName");
		voucherTemplatePath = BASEDIR + File.separator + TEMP
				+ File.separator + "smt_currentSmsTaskRecord_"+empLangName+".xlsx";
		java.text.SimpleDateFormat df = new java.text.SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");

		Map<String, String> resultMap = new HashMap<String, String>();

		// 当前每页分页条数
		int intRowsOfPage = 500000;

		// 当前每页显示条数
		int intPagesCount = (mtdList.size() % intRowsOfPage == 0) ? (mtdList
				.size() / intRowsOfPage) : (mtdList.size() / intRowsOfPage + 1);

		int size = intPagesCount; // 生成的工作薄个数

		EmpExecutionContext.info("群发历史查询的excel导出，工作表个数：" + size);
		if (size == 0) {
			EmpExecutionContext.info("群发历史查询的excel导出无数据！");
			throw new Exception("群发历史查询的excel导出无数据！");
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
			
				fileName =execlName +"_"+sdf.format(curDate)+"_["+ j +"]"+"_"+ StaticValue.getServerNumber() +".xlsx";
				
				in = new FileInputStream(file);
				workbook = new XSSFWorkbook(in);
				
				XSSFCellStyle cellStyle = setCellStyle(workbook);
	
				XSSFSheet sheet = workbook.cloneSheet(0);
				
				workbook.removeSheetAt(0);
				workbook.setSheetName(0, execlName);
				// 读取模板工作表
				XSSFRow row = null;
			
				
				EmpExecutionContext.info("群发历史查询的工作薄创建与模板报表移除成功!");
				// 总体流程
				// 根据记录，计算总的页数
				// 每页的处理 一页一个sheet
				// 写入页标题
				// 循环写入该页的行数，一次写一行，每页定义了多少行，就写多少数据。直到数据没有。
				// 写页尾

				int intCnt = mtdList.size() < j * intRowsOfPage ? mtdList
						.size() : j * intRowsOfPage;
						
				XSSFCell[] cell = new XSSFCell[15];

				for (int k = 0; k < intCnt; k++) {

					LfMttaskVo mt = (LfMttaskVo) mtdList.get(k);

					// 操作员
					String username = mt.getName();
					if (mt.getUserState() != null && mt.getUserState() == 2) {
						username += "("+MessageUtils.extractMessage("ydwx","ydwx_jsp_out_yizhuxiao",request)+")";
					}
					// 所属机构
					String depname = mt.getDepName();
					// 发送账号
					String spuser = mt.getSpUser();
					// 任务标题
					String title = mt.getTitle() == null ? "-" : mt.getTitle();
					
					// 任务批次
					Long taskid=mt.getTaskId()!=null?mt.getTaskId():0l;

					String staskid =taskid-0!=0?(taskid+""):"--";
					
					// 网讯编号
					String netid = mt.getNetid() == null ? "-" : mt.getNetid();
					// 网讯名称
					String netname = mt.getNetname() == null ? "-" : mt.getNetname();
					// 发送时间
					String submittime = mt.getTimerTime() == null ? "" : df
							.format(mt.getTimerTime());
					// 发送状态
					String sendState = mt.getSendstate().toString();
					String error = "";
					if (sendState.equals("0")) {
						sendState = MessageUtils.extractMessage("ydwx","ydwx_jsp_out_weifasong",request);
					} else if (sendState.equals("1")) {
						sendState = MessageUtils.extractMessage("ydwx","ydwx_jsp_out_wgjshchg",request);
					} else if (sendState.equals("2")) {
						sendState = MessageUtils.extractMessage("ydwx","ydwx_jsp_out_shibai",request);
						if (mt.getErrorCodes() != null) {
							error = "[" + mt.getErrorCodes() + "]";
						}
					} else if (sendState.equals("4")) {
						sendState = MessageUtils.extractMessage("ydwx","ydwx_jsp_out_EMPfszh",request);
					} else if(sendState.equals("6")){
				    	sendState = MessageUtils.extractMessage("ydwx","ydwx_jsp_out_zhzhfs",request);
				    } else if(sendState.equals("3"))
			    	{
						sendState = MessageUtils.extractMessage("ydwx","ydwx_jsp_out_wgchlwc",request);
			    	} else {
			    		sendState = MessageUtils.extractMessage("ydwx","ydwx_jsp_out_weishiyong",request);
					}
					String sendStateAnderror = sendState + error;
					// 有效号码个数
					String effcount = mt.getEffCount();
					// 提交信息总数
					String icounts = mt.getSendstate()==2?(mt.getIcount()==null?"0":mt.getIcount()):(mt.getIcount2()==null?"-":mt.getIcount2());
					String succount="-";	
					// 发送成功总数
					String fail_count=(mt.getFaiCount()==null?"0":mt.getFaiCount());
					String icount=(mt.getIcount2()==null?"0":mt.getIcount2());
					
					//提交总数
					Long icount1=Long.parseLong(icount);
					//提交失败总数
					Long fail=Long.parseLong(fail_count);
					long suc=icount1-fail;
					if(mt.getSendstate()==2)
					{
						succount="0";
					}
					else if(mt.getIcount2()==null)
					{
						succount="-";
					}
					else
					{
						succount=suc+"";
					}

				// 发送失败总数
					String failcount = mt.getSendstate()==2?(mt.getIcount()==null?"0":mt.getIcount()):(mt.getIcount2()==null?"-":mt.getFaiCount());

					// 接收失败总数
					String recivefailcount = mt.getSendstate()==2?"0":(mt.getIcount2()==null?"-":(mt.getRFail2()==null?"-":mt.getRFail2().toString()));
					

					// 短信文件
					String msgContent;
					if (mt.getMsg() == null || "".equals(mt.getMsg())) {
						msgContent = MessageUtils.extractMessage("ydwx","ydwx_jsp_out_xiangjianwenjian",request);
					} else {
						msgContent = mt.getMsg().replaceAll("#[pP]_(\\d+)#",MessageUtils.extractMessage("ydwx","ydwx_jsp_out_32",request));
					}
					row = sheet.createRow(k+1);
					for(int i =0;i<cell.length;i++){
						cell[i] = row.createCell(i);
						cell[i].setCellStyle(cellStyle);
					}
					
					cell[0].setCellValue(username);
					cell[1].setCellValue(depname);
					cell[2].setCellValue(spuser);
					cell[3].setCellValue(title);
					cell[4].setCellValue(staskid);
					cell[5].setCellValue(netid);
					cell[6].setCellValue(netname);
					cell[7].setCellValue(submittime);
					cell[8].setCellValue(sendStateAnderror);
					cell[9].setCellValue(effcount);
					cell[10].setCellValue(icounts);
					cell[11].setCellValue(succount);
					cell[12].setCellValue(failcount);
					cell[13].setCellValue(recivefailcount);
					cell[14].setCellValue(msgContent);
				}
				os = new FileOutputStream(voucherFilePath + File.separator + fileName);
				// 写入Excel对象
				workbook.write(os);
			}
			fileName = MessageUtils.extractMessage("ydwx","ydwx_jsp_out_75",request) + sdf.format(curDate) +"_"+ StaticValue.getServerNumber() + ".zip";
			filePath = BASEDIR + File.separator + voucherPath + File.separator
					+ "groupHistory" + File.separator + fileName;

			ZipUtil.compress(voucherFilePath, filePath);
            boolean status = deleteDir(fileTemp);
            if (!status) {
                EmpExecutionContext.error("刪除文件失敗！");
            }
		} catch (Exception e) {
			EmpExecutionContext.error(e,"网讯群发历史查询导出异常");
		}finally{
			// 清除对象
            try {
                if(os != null){
                    os.close();
                }
                if(in != null){
                    in.close();
                }
            } catch (Exception e) {
                EmpExecutionContext.error("流关闭异常！");
            }
			workbook = null;
		}
		resultMap.put("FILE_NAME", fileName);
		resultMap.put("FILE_PATH", filePath);
		return resultMap;
	}
	
	/**
	 * 群发任务查看导出功能实现国际化语言
	 * @param mtdList
	 * @return
	 * @throws Exception
	 */
	
    public Map<String, String> createSmsSendedBoxExcel(List<LfMttaskVo> mtdList, HttpServletRequest request)
			throws Exception {
		
		//String execlName = "网讯发送查询";
		String execlName = "WebexSendquery";
		String empLangName = request.getParameter("empLangName");
		voucherTemplatePath = BASEDIR + File.separator + TEMP
				+ File.separator + "wx_WebexSendquery_"+empLangName+".xlsx";
		java.text.SimpleDateFormat df = new java.text.SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");

		Map<String, String> resultMap = new HashMap<String, String>();
		// 当前每页分页条数
		int intRowsOfPage = 500000;

		// 当前每页显示条数
		int intPagesCount = (mtdList.size() % intRowsOfPage == 0) ? (mtdList
				.size() / intRowsOfPage) : (mtdList.size() / intRowsOfPage + 1);

		int size = intPagesCount; // 生成的工作薄个数

		EmpExecutionContext.info("群发任务查看导出，工作表个数：" + size);
		if (size == 0) {
			EmpExecutionContext.info("群发任务查看导出无数据！");
			throw new Exception("群发任务查看导出无数据！");
		}

		// 产生报表文件的存储路径
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddH24mmss");
		Date curDate = new Date();
		String voucherFilePath = BASEDIR + File.separator + voucherPath+File.separator+"groupWebex"+File.separator+sdf.format(curDate);
		
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
				fileName = execlName+"_"+ sdf.format(curDate) + "_[" + (f+1) + "]"+"_"+ StaticValue.getServerNumber() +".xlsx";
				//读取模板
				in = new FileInputStream(file);
				
				workbook = new XSSFWorkbook(in);
				
				XSSFCellStyle cellStyle = setCellStyle(workbook);
				
				XSSFSheet sheet = workbook.cloneSheet(0);
				workbook.removeSheetAt(0);
				workbook.setSheetName(0, execlName);
				// 读取模板工作表
				XSSFRow row = null;
				EmpExecutionContext.debug("群发任务查看的工作薄创建与模板移除成功!");
				
				
				XSSFCell[] cell = new XSSFCell[11];

				for (int k = 0; k < mtdList.size(); k++) {
					LfMttaskVo mt = (LfMttaskVo) mtdList.get(k);
					// 操作员
					String username = mt.getName();
					if (mt.getUserState() != null && mt.getUserState() == 2) {
						username = username + "("+MessageUtils.extractMessage("ydwx","ydwx_jsp_out_yizhuxiao",request)+")";
					}
					// 所属机构
					String depname = mt.getDepName();
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
					} else if (mt.getTimerStatus() == 0 && mt.getReState() == -1) {// 未定时未审批（待审批）（空）
						sendedTime = "-";
					} else if (mt.getTimerStatus() == 1) {// 定时了
						sendedTime = df.format(mt.getTimerTime());
						if (mt.getSendstate() == 0) {
							sendedTime += "("+MessageUtils.extractMessage("ydwx","ydwx_jsp_out_dingshizhong",request)+")";
						}
					} else if (mt.getSendstate() == 1 || mt.getSendstate() == 2) {// 发送成功或者发送失败
						sendedTime = df.format(mt.getTimerTime());
					} else {
						sendedTime = mt.getTimerTime() == null ? "-" : df.format(mt.getTimerTime());// 这里面的情况就是sendstate=4(发送中)
					}

					// 有效号码个数
					String effcount = mt.getEffCount();

					// 短信文件
					String msgContent;
					if (mt.getMsg() == null || "".equals(mt.getMsg())) {
						msgContent = MessageUtils.extractMessage("ydwx","ydwx_jsp_out_xiangjianwenjian",request);
					} else {
						msgContent = mt.getMsg().replaceAll("#[pP]_(\\d+)#","{#"+MessageUtils.extractMessage("common","common_parameter",request)+"$1#}");
					}

					// 任务状态
					Integer sendState = mt.getSendstate();
					Integer subState = mt.getSubState();
					String state = new String();
					if (subState == 2 && mt.getReState() == -1) {
						state = MessageUtils.extractMessage("ydwx","ydwx_jsp_out_25",request);
					} else if (subState == 2 && mt.getReState() == 2) {
						state = MessageUtils.extractMessage("ydwx","ydwx_jsp_out_18",request);
					} else if (subState == 4) {
						state = MessageUtils.extractMessage("ydwx","ydwx_jsp_out_76",request);
					} else if (subState == 3) {
						state = MessageUtils.extractMessage("ydwx","ydwx_jsp_out_77",request);
					} else if (sendState == 5) {
						state = MessageUtils.extractMessage("ydwx","ydwx_jsp_out_78",request);
					} else if (sendState != 0) {
						state = MessageUtils.extractMessage("ydwx","ydwx_jsp_out_79",request);
					} else if (subState == 2) {
						if (mt.getReState() == -1) {
                            state = MessageUtils.extractMessage("ydwx","ydwx_jsp_out_25",request);
                        } else if (mt.getReState() == 2) {
                            state = MessageUtils.extractMessage("ydwx","ydwx_jsp_out_18",request);
                        } else {
                            state = MessageUtils.extractMessage("ydwx","ydwx_jsp_out_80",request);
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
					
					cell[0].setCellValue(username);
					cell[1].setCellValue(depname);
					cell[2].setCellValue(spuser);
					cell[3].setCellValue(title);
					cell[4].setCellValue(mt.getNetid()==null?"":mt.getNetid());
					cell[5].setCellValue(mt.getNetname()==null?"":mt.getNetname());
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
			fileName = MessageUtils.extractMessage("ydwx","ydwx_jsp_out_81",request) + sdf.format(curDate) +"_"+ StaticValue.getServerNumber() + ".zip";
			filePath = BASEDIR + File.separator + voucherPath + File.separator
					+ "groupWebex" + File.separator + fileName;

			ZipUtil.compress(voucherFilePath, filePath);
            boolean status = deleteDir(fileTemp);
            if (!status) {
                EmpExecutionContext.error("刪除文件失敗！");
            }
		} catch (Exception e) {
			EmpExecutionContext.error(e,"群发任务查看的导出失败");
		}finally{
			// 清除对象
            try {
                if(os != null){
                    os.close();
                }
                if(in != null){
                    in.close();
                }
            } catch (Exception e) {
                EmpExecutionContext.error("流关闭异常！");
            }
			workbook = null;
		}
		resultMap.put("FILE_NAME", fileName);
		resultMap.put("FILE_PATH", filePath);
		return resultMap;
	}
}
