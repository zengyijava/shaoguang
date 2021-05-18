package com.montnets.emp.smstask.biz;

import com.montnets.emp.common.constant.CommonVariables;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.vo.LfMttaskVo;
import com.montnets.emp.common.vo.SendedMttaskVo;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.util.ZipUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FontUnderline;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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


public class SmstaskExcelTool{

	// 报表模板目录
	private String BASEDIR = "";
	// 操作员报表文件路径
	private String voucherTemplatePath = "";
	// 产生下行报表路径
	private String voucherPath = "download";
	
	private String TEMP = "temp";

	private CommonVariables cv;

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
			Integer isHidePhone,HttpServletRequest request) throws Exception {
		
		//String execlName = "群发历史详情";
		String execlName = "smt_smsTaskRecord";
		
		HttpSession session = request.getSession();
		String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
		if ("true".equals(IsexportAll)) {
			voucherTemplatePath = BASEDIR + File.separator
					+ TEMP + File.separator
					+ "smt_smsTaskRecord_"+langName+".xlsx";
		} else {
			voucherTemplatePath = BASEDIR + File.separator
					+ TEMP + File.separator
					+ "smt_smsTaskRecordOnlyPhone_"+langName+".xlsx";
		}

		Map<String, String> resultMap = new HashMap<String, String>();

		// 当前每页分页条数
		int intRowsOfPage = 500000;

		// 当前每页显示条数
		int intPagesCount = (mtdList.size() % intRowsOfPage == 0) ? (mtdList
				.size() / intRowsOfPage) : (mtdList.size() / intRowsOfPage + 1);

		int size = intPagesCount; // 生成的工作薄个数
		//中文国际化 变量
		String yd = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_21", request);
		String lt = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_22", request);
		String dx = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_23", request);
		String gw = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_24", request);

		String fscg = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_184", request);
		String sb = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_185", request);
		String qflsxq = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_224", request);

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
		XSSFWorkbook workbook1 = null;
		SXSSFWorkbook workbook = null;
		FileInputStream in = null;
		FileOutputStream os = null;
		try {
			if(!fileTemp.exists())
				fileTemp.mkdirs();
			// 创建只读的Excel工作薄的对象, 此为模板文件
			File file = new File(voucherTemplatePath);

			for (int j = 1; j <= size; j++) {
				//文件名
				fileName = execlName + "_" + sdf.format(curDate) +"_[" + j + "]_.xlsx";
				// 读取模板
				in = new FileInputStream(file);
				// 工作表
				workbook1 = new XSSFWorkbook(in);
				workbook1.setSheetName(0, execlName);
				EmpExecutionContext.info("工作薄创建与模板报表移除成功!");
				workbook = new SXSSFWorkbook(workbook1,10000);
				Sheet sheet=workbook.getSheetAt(0);
				// 表格样式
				XSSFCellStyle cellStyle = setCellStyle(workbook1);
				in.close();
				// 读取模板工作表
				Row row = null;
				// 总体流程
				// 根据记录，计算总的页数
				// 每页的处理 一页一个sheet
				// 写入页标题
				// 循环写入该页的行数，一次写一行，每页定义了多少行，就写多少数据。直到数据没有。
				// 写页尾

				int intCnt = mtdList.size() < j * intRowsOfPage ? mtdList
						.size() : j * intRowsOfPage;

				Cell[] cell = null;


				if ("true".equals(IsexportAll)) {
					for (int k = (j - 1) * intRowsOfPage; k < intCnt; k++) {

						SendedMttaskVo mt = (SendedMttaskVo) mtdList.get(k);
						//运营商
						Long unicom = mt.getUnicom();
						String unicomString = null;
						if(unicom-0==0){
							unicomString = yd;
						}else if(unicom-1==0){
						    unicomString =lt;
						}else if(unicom-21==0){
							 unicomString =dx;
						}else if(unicom-5==0){
							unicomString =gw;
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
								errorcode = fscg;
							} else if (errorcode.trim().length() > 0) {
								errorcode = sb+"[" + errorcode + "]";
							} else {
								errorcode = "-";
							}
						} else {
							errorcode = "-";
						}

						cell = new Cell[6];
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

						cell[0].setCellValue((double)k+1);
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

						cell = new Cell[1];
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
			fileName = qflsxq + sdf.format(curDate) + ".zip";
			filePath = BASEDIR + File.separator + voucherPath + File.separator
					+ "groupHisDetail" + File.separator + fileName;

			ZipUtil.compress(voucherFilePath, filePath);
			deleteDir(fileTemp);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"生成发送详情Excel异常！");
		}finally{
			// 清除对象
			if(os != null){
				try{
					os.close();
				}catch(Exception e){
					EmpExecutionContext.error(e,"关闭资源异常");
				}
			}
			workbook = null;
		}
		resultMap.put("FILE_NAME", fileName);
		resultMap.put("FILE_PATH", filePath);
		return resultMap;

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
			Integer isHidePhone,String exportType,HttpServletRequest request) throws Exception {

		//String execlName = "群发历史详情";
		String execlName = "smt_smsTaskRecord";


		HttpSession session = request.getSession();
		String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
		if ("true".equals(IsexportAll)) {
			voucherTemplatePath = BASEDIR + File.separator
					+ TEMP + File.separator
					+ "smt_smsTaskRecord_"+langName+".xlsx";
		} else {
			voucherTemplatePath = BASEDIR + File.separator
					+ TEMP + File.separator
					+ "smt_smsTaskRecordOnlyPhone_"+langName+".xlsx";
		}


		Map<String, String> resultMap = new HashMap<String, String>();

		// 当前每页分页条数
		int intRowsOfPage = 500000;

		// 当前每页显示条数
		int intPagesCount = (mtdList.size() % intRowsOfPage == 0) ? (mtdList
				.size() / intRowsOfPage) : (mtdList.size() / intRowsOfPage + 1);

		int size = intPagesCount; // 生成的工作薄个数

		//中文国际化 变量
		String yd = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_21", request);
		String lt = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_22", request);
		String dx = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_23", request);
		String gw = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_24", request);

		String fscg = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_184", request);
		String sb = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_185", request);

		String xh = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_182", request);
		String yys = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_178", request);
		String sjh = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_119", request);
		String nr = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_172", request);
		String ft = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_183", request);
		String zt = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_103", request);

		String qflsxq = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_224", request);
		String ydcwfsxq = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_225", request);
		String ygsrzf = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_226", request);
		String khsrzf = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_227", request);
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
		XSSFWorkbook workbook1 = null;
		SXSSFWorkbook workbook = null;
		FileInputStream in = null;
		FileOutputStream os = null;
		try {
			if(!fileTemp.exists())
				fileTemp.mkdirs();
			// 创建只读的Excel工作薄的对象, 此为模板文件
			File file = new File(voucherTemplatePath);

			//判断模板文件是否存在
			if(!file.exists()){
				// 创建Excel的工作书册 Workbook,对应到一个excel文档
				XSSFWorkbook wb = new XSSFWorkbook();
				// 创建Excel的工作sheet,对应到一个excel文档的tab
				XSSFSheet xsheet = wb.createSheet("sheet1");
				XSSFCellStyle cellStyle2 = wb.createCellStyle();
				XSSFFont font2 = wb.createFont();
				// 字体名称
				font2.setFontName("TAHOMA");
				font2.setBold(true);
				// 字体大小
				font2.setFontHeight(11);
				cellStyle2.setFont(font2);
				// 水平对齐
				cellStyle2.setAlignment(HorizontalAlignment.CENTER);
				// 竖直对齐
				cellStyle2.setVerticalAlignment(VerticalAlignment.CENTER);
				cellStyle2.setWrapText(true);
				// 创建Excel的sheet的一行
				XSSFRow xrow = xsheet.createRow(0);
				if ("true".equals(IsexportAll))
				{
					XSSFCell[] cell = new XSSFCell[6];
					for (int i = 0; i < cell.length; i++) {
						// 创建一个Excel的单元格
						cell[i]=xrow.createCell(i);
						// 设置单元格样式
						cell[i].setCellStyle(cellStyle2);
					}
					cell[0].setCellValue(xh);
					cell[1].setCellValue(yys);
					cell[2].setCellValue(sjh);
					cell[3].setCellValue(nr);
					cell[4].setCellValue(ft);
					cell[5].setCellValue(zt);
				}else
				{
					XSSFCell[] cell= new XSSFCell[1];
					for (int i = 0; i < cell.length; i++) {
						// 创建一个Excel的单元格
						cell[i]=xrow.createCell(i);
						// 设置单元格样式
						cell[i].setCellStyle(cellStyle2);
					}
					cell[0].setCellValue(sjh);
				}
				FileOutputStream xos = new FileOutputStream(voucherTemplatePath);
				wb.write(xos);
				xos.close();
			}

			for (int j = 1; j <= size; j++) {
				//文件名
				fileName = execlName + "_" + sdf.format(curDate) +"_[" + j + "]_"+ StaticValue.getServerNumber() +".xlsx";

				XSSFCellStyle cellStyle=null;
				Sheet sheet=null;
				//如果文件存在
				if(file.exists())
				{
				// 读取模板
				in = new FileInputStream(file);
				// 工作表
				workbook1 = new XSSFWorkbook(in);
				workbook1.setSheetName(0, execlName);
				EmpExecutionContext.info("工作薄创建与模板报表移除成功!");
				workbook = new SXSSFWorkbook(workbook1,10000);
				sheet=workbook.getSheetAt(0);
				// 表格样式
				cellStyle = setCellStyle(workbook1);
				in.close();
				}else
				{
					//文件不存在，则创建
					workbook = new SXSSFWorkbook();
					sheet=workbook.createSheet(execlName);

					Row topRow=sheet.createRow(0);
					if ("true".equals(IsexportAll))
					{
						Cell[] cell = new Cell[6];
						for(int i =0;i<cell.length;i++){
							cell[i] = topRow.createCell(i);
							cell[i].setCellStyle(cellStyle);
						}
						cell[0].setCellValue(xh);
						cell[1].setCellValue(yys);
						cell[2].setCellValue(sjh);
						cell[3].setCellValue(nr);
						cell[4].setCellValue(ft);
						cell[5].setCellValue(zt);
					}else
					{
						Cell[] cell = new Cell[1];
						for(int i =0;i<cell.length;i++){
							cell[i] = topRow.createCell(i);
							cell[i].setCellStyle(cellStyle);
						}
						cell[0].setCellValue(sjh);
					}
				}
				// 读取模板工作表
				Row row = null;
				// 总体流程
				// 根据记录，计算总的页数
				// 每页的处理 一页一个sheet
				// 写入页标题
				// 循环写入该页的行数，一次写一行，每页定义了多少行，就写多少数据。直到数据没有。
				// 写页尾

				int intCnt = mtdList.size() < j * intRowsOfPage ? mtdList
						.size() : j * intRowsOfPage;

				Cell[] cell = null;


				if ("true".equals(IsexportAll)) {
					for (int k = (j - 1) * intRowsOfPage; k < intCnt; k++) {

						SendedMttaskVo mt = (SendedMttaskVo) mtdList.get(k);
						//运营商
						Long unicom = mt.getUnicom();
						String unicomString = null;
						if(unicom-0==0){
							unicomString = yd;
						}else if(unicom-1==0){
						    unicomString =lt;
						}else if(unicom-21==0){
							 unicomString =dx;
						}else if(unicom-5==0){
							unicomString =gw;
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
								errorcode = fscg;
							} else if (errorcode.trim().length() > 0) {
								errorcode =sb+ "[" + errorcode + "]";
							} else {
								errorcode = "-";
							}
						} else {
							errorcode = "-";
						}

						cell = new Cell[6];
						row = sheet.createRow(k+1-((j - 1) * intRowsOfPage));
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

						cell[0].setCellValue((double)k+1-((j - 1) * intRowsOfPage));
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

						cell = new Cell[1];
						row = sheet.createRow(k+1-((j - 1) * intRowsOfPage));

						cell[0] = row.createCell(0);

						cell[0].setCellStyle(cellStyle);

						cell[0].setCellValue(phone);
					}
				}

				os = new FileOutputStream(voucherFilePath + File.separator + fileName);
				// 写入Excel对象
				workbook.write(os);
			}
			if("1".equals(exportType)){
				fileName = qflsxq + sdf.format(curDate) + "_"+ StaticValue.getServerNumber() +".zip";
			}else if("2".equals(exportType)){
				fileName = ydcwfsxq + sdf.format(curDate) + "_"+ StaticValue.getServerNumber() +".zip";
			}else if("3".equals(exportType)){
				fileName = ygsrzf + sdf.format(curDate) + "_"+ StaticValue.getServerNumber() +".zip";
			}else{
				fileName = khsrzf + sdf.format(curDate) + "_"+ StaticValue.getServerNumber() +".zip";
			}
			filePath = BASEDIR + File.separator + voucherPath + File.separator
					+ "groupHisDetail" + File.separator + fileName;

			ZipUtil.compress(voucherFilePath, filePath);
			deleteDir(fileTemp);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"生成发送详情Excel异常！");
		}finally{
			// 清除对象
			if(os != null){
				try{
					os.close();
				}catch(Exception e){
					EmpExecutionContext.error(e,"关闭资源异常");
				}
			}
			workbook = null;
		}
		resultMap.put("FILE_NAME", fileName);
		resultMap.put("FILE_PATH", filePath);
		return resultMap;

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

	public Map<String, String> createMtReportExcel(List<LfMttaskVo> mtdList,HttpServletRequest request)
			throws Exception {

		//String execlName = "群发历史";
		String execlName = "SmsTaskRecord";

		HttpSession session = request.getSession();
		String langName = (String)session.getAttribute(StaticValue.LANG_KEY);

		voucherTemplatePath = BASEDIR + File.separator + TEMP
				+ File.separator + "smt_currentSmsTaskRecord_"+langName+".xlsx";
		java.text.SimpleDateFormat df = new java.text.SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");

		Map<String, String> resultMap = new HashMap<String, String>();

		// 当前每页分页条数
		int intRowsOfPage = 500000;

		// 当前每页显示条数
		int intPagesCount = (mtdList.size() % intRowsOfPage == 0) ? (mtdList
				.size() / intRowsOfPage) : (mtdList.size() / intRowsOfPage + 1);

		int size = intPagesCount; // 生成的工作薄个数
		//中文内容国际化 标识变量
		String yzx = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_139", request);
		String empfs = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_156", request);
		String httpjr = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_157", request);
		String wfs = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_201", request);
		String wgjscg = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_189", request);
		String sb = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_190", request);
		String fsz = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_202", request);
		String zzfs = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_203", request);
		String wgclwc = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_188", request);
		String wsy = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_204", request);
		String xjwj = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_205", request);
		String qfls = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_228", request);


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
		if(!fileTemp.exists())
			fileTemp.mkdirs();

		// 报表文件名
		String fileName = null;
		String filePath=null;
		XSSFWorkbook workbook1 = null;
		SXSSFWorkbook workbook = null;
		FileInputStream in = null;
		FileOutputStream os = null;
		try {
			// 创建只读的Excel工作薄的对象, 此为模板文件
			File file = new File(voucherTemplatePath);
			for (int j = 1; j <= size; j++) {
				//文件名
				fileName =execlName +"_"+sdf.format(curDate)+"_["+ j +"].xlsx";
				// 读取模板
				in = new FileInputStream(file);
				// 工作表
				workbook1 = new XSSFWorkbook(in);
				workbook1.setSheetName(0, execlName);
				EmpExecutionContext.info("工作薄创建与模板报表移除成功!");
				workbook = new SXSSFWorkbook(workbook1,10000);
				Sheet sheet=workbook.getSheetAt(0);
				// 表格样式
				XSSFCellStyle cellStyle = setCellStyle(workbook1);
				in.close();
				// 读取模板工作表
				Row row = null;
				// 总体流程
				// 根据记录，计算总的页数
				// 每页的处理 一页一个sheet
				// 写入页标题
				// 循环写入该页的行数，一次写一行，每页定义了多少行，就写多少数据。直到数据没有。
				// 写页尾

				int intCnt = mtdList.size() < j * intRowsOfPage ? mtdList
						.size() : j * intRowsOfPage;

				Cell[] cell = new Cell[14];

				for (int k = 0; k < intCnt; k++) {

					LfMttaskVo mt = (LfMttaskVo) mtdList.get(k);

					// 操作员
					String username = mt.getName();
					if (mt.getUserState() != null && mt.getUserState() == 2) {
						username += "("+yzx+")";
					}
					// 所属机构
					String depname = mt.getDepName();
					// 发送账号
					String spuser = mt.getSpUser();
					//发送类型
					String taskType="-";
					if(mt.getTaskType()!=null){
						taskType=mt.getTaskType()==1?empfs:httpjr;
					}

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
						sendState = wfs;
					} else if (sendState.equals("1")) {
						sendState = wgjscg;
					} else if (sendState.equals("2")) {
						sendState = sb;
						if (mt.getErrorCodes() != null && !"".equals(mt.getErrorCodes())) {
							error = "[" + mt.getErrorCodes() + "]";
						}
					} else if (sendState.equals("4")) {
						sendState = fsz;
					} else if(sendState.equals("6")){
				    	sendState = zzfs;
				    } else if(sendState.equals("3"))
			    	{
						sendState = wgclwc;
			    	} else {
			    		sendState = wsy;
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
							msgContent = xjwj;
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
			fileName = qfls + sdf.format(curDate) + ".zip";
			filePath = BASEDIR + File.separator + voucherPath + File.separator
					+ "groupHistory" + File.separator + fileName;

			ZipUtil.compress(voucherFilePath, filePath);
			deleteDir(fileTemp);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"生成群发历史的excel异常！");
		}finally{
			// 清除对象
			if(os != null){
				try{
					os.close();
				}catch(Exception e){
					EmpExecutionContext.error(e,"关闭资源异常！");
				}
			}

			workbook = null;
		}
		resultMap.put("FILE_NAME", fileName);
		resultMap.put("FILE_PATH", filePath);
		return resultMap;
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

	public Map<String, String> createMtReportExcel(List<LfMttaskVo> mtdList,String exportType,HttpServletRequest request)
			throws Exception {

		//String execlName = "群发历史";
		String execlName = "SmsTaskRecord";
		HttpSession session = request.getSession();
		String langName = (String)session.getAttribute(StaticValue.LANG_KEY);

		voucherTemplatePath = BASEDIR + File.separator + TEMP
				+ File.separator + "smt_currentSmsTaskRecord_"+langName+".xlsx";
		java.text.SimpleDateFormat df = new java.text.SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");

		Map<String, String> resultMap = new HashMap<String, String>();

		// 当前每页分页条数
		int intRowsOfPage = 500000;

		// 当前每页显示条数
		int intPagesCount = (mtdList.size() % intRowsOfPage == 0) ? (mtdList
				.size() / intRowsOfPage) : (mtdList.size() / intRowsOfPage + 1);

		int size = intPagesCount; // 生成的工作薄个数
		//中文内容国际化 标识变量
		String czy = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_146", request);
		String lsjg = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_143", request);
		String spzh = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_99", request);
		String fslx = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_158", request);
		String fszt = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_2", request);
		String rwpc = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_159", request);
		String fssj = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_25", request);
		String fsstat = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_187", request);
		String yxhmgs = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_45", request);
		String tjxxzs = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_46", request);
		String fscg = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_193", request);
		String tjsb = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_194", request);
		String jssb = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_195", request);
		String zls = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_196", request);
		String xxnr = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_12", request);

		String yzx = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_139", request);
		String empfs = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_156", request);
		String httpjr = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_157", request);
		String wfs = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_201", request);
		String wgjscg = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_189", request);
		String sb = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_190", request);
		String fsz = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_202", request);
		String zzfs = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_203", request);
		String wgclwc = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_188", request);
		String wsy = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_204", request);
		String xjwj = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_205", request);

		String qfls = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_228", request);
		String ydcwfsxq = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_225", request);
		String ygsrzf = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_226", request);
		String khsrzf = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_227", request);

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
		if(!fileTemp.exists())
			fileTemp.mkdirs();

		// 报表文件名
		String fileName = null;
		String filePath=null;
		XSSFWorkbook workbook1 = null;
		SXSSFWorkbook workbook = null;
		FileInputStream in = null;
		FileOutputStream os = null;

		//导出excel列数
		int cellLen = 15;
		try {
			// 创建只读的Excel工作薄的对象, 此为模板文件
			File file = new File(voucherTemplatePath);

			//判断模板文件是否存在
			if(!file.exists()){
				// 创建Excel的工作书册 Workbook,对应到一个excel文档
				XSSFWorkbook wb = new XSSFWorkbook();
				// 创建Excel的工作sheet,对应到一个excel文档的tab

				XSSFSheet xsheet = wb.createSheet("sheet1");
				XSSFCellStyle cellStyle2 = wb.createCellStyle();
				XSSFFont font2 = wb.createFont();
				// 字体名称
				font2.setFontName("TAHOMA");
				font2.setBold(true);
				// 字体大小
				font2.setFontHeight(11);
				cellStyle2.setFont(font2);
				// 水平对齐
				cellStyle2.setAlignment(HorizontalAlignment.CENTER);
				// 竖直对齐
				cellStyle2.setVerticalAlignment(VerticalAlignment.CENTER);
				cellStyle2.setWrapText(true);
				// 创建Excel的sheet的一行
				XSSFRow xrow = xsheet.createRow(0);
				XSSFCell[] cell = new XSSFCell[cellLen];

				for (int i = 0; i < cell.length; i++) {
					// 创建一个Excel的单元格
					cell[i]=xrow.createCell(i);
					// 设置单元格样式
					cell[i].setCellStyle(cellStyle2);
				}

				// 给Excel的单元格设置样式和赋值
				cell[0].setCellValue(czy);
				cell[1].setCellValue(lsjg);
				cell[2].setCellValue(spzh);
				cell[3].setCellValue(fslx);
				cell[4].setCellValue(fszt);
				cell[5].setCellValue(rwpc);
				cell[6].setCellValue(fssj);
				cell[7].setCellValue(fsstat);
				cell[8].setCellValue(yxhmgs);
				cell[9].setCellValue(tjxxzs);
				cell[10].setCellValue(fscg);
				cell[11].setCellValue(tjsb);
				cell[12].setCellValue(jssb);
				cell[13].setCellValue(zls);
				cell[14].setCellValue(xxnr);
				FileOutputStream xos = new FileOutputStream(voucherTemplatePath);
				wb.write(xos);
				xos.close();
			}

			for (int j = 1; j <= size; j++) {
				//文件名
				fileName =execlName +"_"+sdf.format(curDate)+"_["+ j +"]_"+ StaticValue.getServerNumber() +".xlsx";

				XSSFCellStyle cellStyle=null;
				Sheet sheet=null;
				//如果文件存在
				if(file.exists())
				{
				// 读取模板
				in = new FileInputStream(file);
				// 工作表
				workbook1 = new XSSFWorkbook(in);
				workbook1.setSheetName(0, execlName);
				EmpExecutionContext.info("工作薄创建与模板报表移除成功!");
				workbook = new SXSSFWorkbook(workbook1,10000);
				sheet=workbook.getSheetAt(0);
				// 表格样式
				cellStyle = setCellStyle(workbook1);
				in.close();
				}else
				{
					//文件不存在，则创建
					workbook = new SXSSFWorkbook();
					sheet=workbook.createSheet(execlName);

					Row topRow=sheet.createRow(0);
					Cell[] cell = new Cell[cellLen];
					for(int i =0;i<cell.length;i++){
						cell[i] = topRow.createCell(i);
						cell[i].setCellStyle(cellStyle);
					}
					cell[0].setCellValue(czy);
					cell[1].setCellValue(lsjg);
					cell[2].setCellValue(spzh);
					cell[3].setCellValue(fslx);
					cell[4].setCellValue(fszt);
					cell[5].setCellValue(rwpc);
					cell[6].setCellValue(fssj);
					cell[7].setCellValue(fsstat);
					cell[8].setCellValue(yxhmgs);
					cell[9].setCellValue(tjxxzs);
					cell[10].setCellValue(fscg);
					cell[11].setCellValue(tjsb);
					cell[12].setCellValue(jssb);
					cell[13].setCellValue(zls);
					cell[14].setCellValue(xxnr);

				}
				// 读取模板工作表
				Row row = null;
				// 总体流程
				// 根据记录，计算总的页数
				// 每页的处理 一页一个sheet
				// 写入页标题
				// 循环写入该页的行数，一次写一行，每页定义了多少行，就写多少数据。直到数据没有。
				// 写页尾

				// 任务滞留数
				StringBuffer taskids = new StringBuffer();
				if(mtdList!=null&&mtdList.size()>0){
					for(LfMttaskVo mttaskVo:mtdList){
						taskids.append(","+mttaskVo.getTaskId());
					}
				}
				Map<Long, Long> taskRemains = new HashMap<Long, Long>();
				if(taskids.length()>0){
					taskids.delete(0, 1);
					taskRemains = new SmstaskBiz().getTaskRemains(taskids.toString());
				}

				int intCnt = mtdList.size() < j * intRowsOfPage ? mtdList
						.size() : j * intRowsOfPage;

				Cell[] cell = new Cell[cellLen];

				for (int k = 0; k < intCnt; k++) {

					LfMttaskVo mt = (LfMttaskVo) mtdList.get(k);

					// 操作员
					String username = mt.getName();
					if (mt.getUserState() != null && mt.getUserState() == 2) {
						username += "("+yzx+")";
					}
					// 所属机构
					String depname = mt.getDepName();
					// 发送账号
					String spuser = mt.getSpUser();
					//发送类型
					String taskType="-";
					if(mt.getTaskType()!=null){
						taskType=mt.getTaskType()==1?empfs:httpjr;
					}

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
						sendState = wfs;
					} else if (sendState.equals("1")) {
						sendState = wgjscg;
					} else if (sendState.equals("2")) {
						sendState = sb;
						if (mt.getErrorCodes() != null && !"".equals(mt.getErrorCodes())) {
							error = "[" + mt.getErrorCodes() + "]";
						}
					} else if (sendState.equals("4")) {
						sendState = fsz;
					} else if(sendState.equals("6")){
				    	sendState = zzfs;
				    } else if(sendState.equals("3"))
			    	{
						sendState = wgclwc;
			    	} else {
			    		sendState = wsy;
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
							msgContent = xjwj;
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
					// 滞留数
					Long delaycount = (taskRemains==null||taskRemains.get(mt.getTaskId())==null)?0L:taskRemains.get(mt.getTaskId());

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
					cell[13].setCellValue(delaycount);

					//将内容中的#P_1#替换为{#参数1#}
					String param = MessageUtils.extractMessage("common","common_parameter",request);
					String replacement = "{#"+param+"$1#}";
					msgContent = msgContent.replaceAll("#P_(\\d+)#",replacement);

					cell[14].setCellValue(msgContent);


				}
				os = new FileOutputStream(voucherFilePath + File.separator + fileName);
				// 写入Excel对象
				workbook.write(os);
			}
			if("1".equals(exportType)){
				fileName = qfls + sdf.format(curDate) + "_"+ StaticValue.getServerNumber() +".zip";
			}else if("2".equals(exportType)){
				fileName = ydcwfsxq + sdf.format(curDate) + "_"+ StaticValue.getServerNumber() +".zip";
			}else if("3".equals(exportType)){
				fileName = ygsrzf + sdf.format(curDate) + "_"+ StaticValue.getServerNumber() +".zip";
			}else{
				fileName = khsrzf + sdf.format(curDate) + "_"+ StaticValue.getServerNumber() +".zip";
			}
			filePath = BASEDIR + File.separator + voucherPath + File.separator
					+ "groupHistory" + File.separator + fileName;

			ZipUtil.compress(voucherFilePath, filePath);
			deleteDir(fileTemp);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"生成群发历史的excel异常！");
		}finally{
			// 清除对象
			if(os != null){
				try{
					os.close();
				}catch(Exception e){
					EmpExecutionContext.error(e,"关闭资源异常！");
				}
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
	public Map<String, String> createSmsSendedBoxExcel(List<LfMttaskVo> mtdList,HttpServletRequest request)
			throws Exception {

		//String execlName = "群发任务";
		String execlName = "SmsSendedBox";

		HttpSession session = request.getSession();
		String langName = (String)session.getAttribute(StaticValue.LANG_KEY);

		voucherTemplatePath = BASEDIR + File.separator + TEMP
				+ File.separator + "smt_AllSmsSendedBox_"+langName+".xlsx";

		java.text.SimpleDateFormat df = new java.text.SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");

		Map<String, String> resultMap = new HashMap<String, String>();
		// 当前每页分页条数
		int intRowsOfPage = 500000;

		// 当前每页显示条数
		int intPagesCount = (mtdList.size() % intRowsOfPage == 0) ? (mtdList
				.size() / intRowsOfPage) : (mtdList.size() / intRowsOfPage + 1);

		int size = intPagesCount; // 生成的工作薄个数
		//
		String yzx = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_139", request);
		String empfs = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_156", request);
		String httpjr = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_157", request);
		String dsz = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_166", request);
		String xjwj = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_205", request);

		String dsp = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_149", request);
	    String spbtg = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_150", request);
	    String ydj = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_154", request);
	    String ycx = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_152", request);
	    String cswfs = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_155", request);
	    String yfs = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_153", request);
	    String dfs = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_151", request);
	    String qfrw = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_229", request);

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
		XSSFWorkbook workbook1 = null;
		SXSSFWorkbook workbook = null;

		InputStream in = null;
		OutputStream os = null;


		try {
			// 创建只读的Excel工作薄的对象, 此为模板文件
			File file = new File(voucherTemplatePath);

			for(int f = 0; f< size;f++){
				//文件名
				fileName = execlName+"_"+ sdf.format(curDate) + "_[" + (f+1) + "].xlsx";
				//读取模板
				in = new FileInputStream(file);
				// 工作表
				workbook1 = new XSSFWorkbook(in);
				EmpExecutionContext.debug("工作薄创建与模板移除成功!");
				workbook = new SXSSFWorkbook(workbook1,10000);
				Sheet sheet=workbook.getSheetAt(0);
				workbook.setSheetName(0, execlName);
				// 表格样式
				XSSFCellStyle cellStyle = setCellStyle(workbook1);
				in.close();
				// 读取模板工作表
				Row row = null;

				Cell[] cell = new Cell[11];

				for (int k = 0; k < mtdList.size(); k++) {
					LfMttaskVo mt = (LfMttaskVo) mtdList.get(k);
					// 操作员
					String username = mt.getName();
					if (mt.getUserState() != null && mt.getUserState() == 2) {
						username = username + "("+yzx+")";
					}
					// 所属机构
					String depname = mt.getDepName();
					String taskType="-";
					if(mt.getTaskType()!=null){
						taskType=mt.getTaskType()==1?empfs:httpjr;
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
							sendedTime += "("+dsz+")";
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
							msgContent = xjwj;
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
						state = dsp;
					} else if (subState == 2 && mt.getReState() == 2) {
						state = spbtg;
					} else if (subState == 4) {
						state = ydj;
					} else if (subState == 3) {
						state = ycx;
					} else if (sendState == 5) {
						state = cswfs;
					} else if (sendState != 0) {
						state = yfs;
					} else if (subState == 2) {
						if (mt.getReState() == -1)
							state = dsp;
						else if (mt.getReState() == 2)
							state = spbtg;
						else
							state = dfs;
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
			fileName = qfrw + sdf.format(curDate) + ".zip";
			filePath = BASEDIR + File.separator + voucherPath + File.separator
					+ "groupSms" + File.separator + fileName;

			ZipUtil.compress(voucherFilePath, filePath);
			deleteDir(fileTemp);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"群发任务查看导出异常！");
		}finally{
			// 清除对象
			if(os != null){
				try{
					os.close();
				}catch(Exception e){
					EmpExecutionContext.error(e,"关闭资源异常！");
				}
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
	public Map<String, String> createSmsSendedBoxExcel(List<LfMttaskVo> mtdList,String exportType,HttpServletRequest request)
			throws Exception {

		//String execlName = "群发任务";
		String execlName = "SmsSendedBox";

		HttpSession session = request.getSession();
		String langName = (String)session.getAttribute(StaticValue.LANG_KEY);

		voucherTemplatePath = BASEDIR + File.separator + TEMP
				+ File.separator + "smt_AllSmsSendedBox_"+langName+".xlsx";
		java.text.SimpleDateFormat df = new java.text.SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");

		Map<String, String> resultMap = new HashMap<String, String>();
		// 当前每页分页条数
		int intRowsOfPage = 500000;

		// 当前每页显示条数
		int intPagesCount = (mtdList.size() % intRowsOfPage == 0) ? (mtdList
				.size() / intRowsOfPage) : (mtdList.size() / intRowsOfPage + 1);

		int size = intPagesCount; // 生成的工作薄个数
		//中文内容国际化 标识变量
		String czy = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_146", request);
		String lsjg = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_143", request);
		String spzh = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_99", request);
		String fslx = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_158", request);
		String fszt = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_2", request);
		String rwpc = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_159", request);
		String fssj = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_25", request);
		String rwzt = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_148", request);
		String yxhmgs = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_45", request);
		String cjsj = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_160", request);
		String xxnr = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_12", request);
		String tjxxs = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_192", request);

		String yzx = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_139", request);
		String empfs = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_156", request);
		String httpjr = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_157", request);
		String dsz = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_166", request);
		String xjwj = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_205", request);

		String dsp = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_149", request);
	    String spbtg = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_150", request);
	    String ydj = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_154", request);
	    String ycx = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_152", request);
	    String cswfs = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_155", request);
	    String yfs = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_153", request);
	    String dfs = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_151", request);
	    String qfrw = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_229", request);

	    String ydcwfsrw = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_230", request);
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
		XSSFWorkbook workbook1 = null;
		SXSSFWorkbook workbook = null;

		InputStream in = null;
		OutputStream os = null;


		try {
			// 创建只读的Excel工作薄的对象, 此为模板文件
			File file = new File(voucherTemplatePath);

			//判断模板文件是否存在
			if(!file.exists()){
				// 创建Excel的工作书册 Workbook,对应到一个excel文档
				XSSFWorkbook wb = new XSSFWorkbook();
				// 创建Excel的工作sheet,对应到一个excel文档的tab
				XSSFSheet xsheet = wb.createSheet("sheet1");
				XSSFCellStyle cellStyle2 = wb.createCellStyle();
				XSSFFont font2 = wb.createFont();
				// 字体名称
				font2.setFontName("TAHOMA");
				font2.setBold(true);
				// 字体大小
				font2.setFontHeight(11);
				cellStyle2.setFont(font2);
				// 水平对齐
				cellStyle2.setAlignment(HorizontalAlignment.CENTER);
				// 竖直对齐
				cellStyle2.setVerticalAlignment(VerticalAlignment.CENTER);
				cellStyle2.setWrapText(true);
				// 创建Excel的sheet的一行
				XSSFRow xrow = xsheet.createRow(0);
				XSSFCell[] cell = new XSSFCell[12];

				for (int i = 0; i < cell.length; i++) {
					// 创建一个Excel的单元格
					cell[i]=xrow.createCell(i);
					// 设置单元格样式
					cell[i].setCellStyle(cellStyle2);
				}

				// 给Excel的单元格设置样式和赋值
				cell[0].setCellValue(czy);
				cell[1].setCellValue(lsjg);
				cell[2].setCellValue(spzh);
				cell[3].setCellValue(fslx);
				cell[4].setCellValue(fszt);
				cell[5].setCellValue(rwpc);
				cell[6].setCellValue(cjsj);
				cell[7].setCellValue(fssj);
				cell[8].setCellValue(yxhmgs);
				cell[9].setCellValue(tjxxs);
				cell[10].setCellValue(xxnr);
				cell[11].setCellValue(rwzt);
				FileOutputStream xos = new FileOutputStream(voucherTemplatePath);
				wb.write(xos);
				xos.close();
			}

			for(int f = 0; f< size;f++){
				//文件名
				fileName = execlName+"_"+ sdf.format(curDate) + "_[" + (f+1) + "]_"+ StaticValue.getServerNumber() +".xlsx";
				//读取模板
				in = new FileInputStream(file);
				// 工作表
				workbook1 = new XSSFWorkbook(in);
				EmpExecutionContext.debug("工作薄创建与模板移除成功!");
				workbook = new SXSSFWorkbook(workbook1,10000);
				Sheet sheet=workbook.getSheetAt(0);
				workbook.setSheetName(0, execlName);
				// 表格样式
				XSSFCellStyle cellStyle = setCellStyle(workbook1);
				in.close();
				// 读取模板工作表
				Row row = null;

				Cell[] cell = new Cell[12];

				for (int k = 0; k < mtdList.size(); k++) {
					LfMttaskVo mt = (LfMttaskVo) mtdList.get(k);
					// 操作员
					String username = mt.getName();
					if (mt.getUserState() != null && mt.getUserState() == 2) {
						username = username + "("+yzx+")";
					}
					// 所属机构
					String depname = mt.getDepName();
					String taskType="-";
					if(mt.getTaskType()!=null){
						taskType=mt.getTaskType()==1?empfs:httpjr;
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
							sendedTime += "("+dsz+")";
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

					//提交信息数
					String icount ="-";
					if(mt.getTaskType()==1){
						icount= mt.getIcount();
					}
					
					// 短信文件
					String msgContent;
					if (mt.getMsg() == null || "".equals(mt.getMsg())) {
						if(mt.getTaskType()==1){
							msgContent = xjwj;
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
						state = dsp;
					} else if (subState == 2 && mt.getReState() == 2) {
						state = spbtg;
					} else if (subState == 4) {
						state = ydj;
					} else if (subState == 3) {
						state = ycx;
					} else if (sendState == 5) {
						state = cswfs;
					} else if (sendState != 0) {
						state = yfs;
					} else if (subState == 2) {
						if (mt.getReState() == -1)
							state = dsp;
						else if (mt.getReState() == 2)
							state = spbtg;
						else
							state = dfs;
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
					cell[11] = row.createCell(11);


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
					cell[11].setCellStyle(cellStyle);

					cell[0].setCellValue(username);
					cell[1].setCellValue(depname);
					cell[2].setCellValue(spuser);
					cell[3].setCellValue(taskType);
					cell[4].setCellValue(title);
					cell[5].setCellValue(taskid);
					cell[6].setCellValue(createtime);
					cell[7].setCellValue(sendedTime);
					cell[8].setCellValue(effcount);
					cell[9].setCellValue(icount);
					String param = MessageUtils.extractMessage("common","common_parameter",request);
					String replacement = "{#"+param+"$1#}";
					msgContent = msgContent.replaceAll("#P_(\\d+)#",replacement);

					cell[10].setCellValue(msgContent);
					cell[11].setCellValue(state);
				}
				os = new FileOutputStream(voucherFilePath + File.separator + fileName);
				// 写入Excel对象
				workbook.write(os);
			}
			if("1".equals(exportType)){
				fileName = qfrw + sdf.format(curDate) + "_"+ StaticValue.getServerNumber() +".zip";
			}else{
				fileName = ydcwfsrw + sdf.format(curDate) + "_"+ StaticValue.getServerNumber() +".zip";
			}
			filePath = BASEDIR + File.separator + voucherPath + File.separator
					+ "groupSms" + File.separator + fileName;

			ZipUtil.compress(voucherFilePath, filePath);
			deleteDir(fileTemp);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"群发任务查看导出异常！");
		}finally{
			// 清除对象
			if(os != null){
				try{
					os.close();
				}catch(Exception e){
					EmpExecutionContext.error(e,"关闭资源异常！");
				}
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
	 * 短信回复记录导出
	 * @param ReplyParamsList 导出数据列表
	 * @param isHidePhone	号码是否可见
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> createSmsReplyReportExcel(List<ReplyParams> ReplyParamsList, Integer isHidePhone,HttpServletRequest request)
			throws Exception {

		String execlName = "SmsReplyRecord";

		HttpSession session = request.getSession();
		String langName = (String)session.getAttribute(StaticValue.LANG_KEY);

		voucherTemplatePath = BASEDIR + File.separator + TEMP + File.separator
				+ "smt_ReplyRecord_"+langName+".xlsx";

		Map<String, String> resultMap = new HashMap<String, String>();
		// 当前每页分页条数
		int intRowsOfPage = 500000;

		// 当前每页显示条数
		int intPagesCount = (ReplyParamsList.size() % intRowsOfPage == 0) ? (ReplyParamsList
				.size() / intRowsOfPage) : (ReplyParamsList.size() / intRowsOfPage + 1);

		int size = intPagesCount; // 生成的工作薄个数

		// 群发任务
		String qfrwdxhf = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_231", request);
		
		EmpExecutionContext.info("工作表个数：" + size);
		if (size == 0) {
			EmpExecutionContext.info("群发任务短信回复无数据！");
			throw new Exception("群发任务短信回复无数据！");
		}

		// 产生报表文件的存储路径
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddH24mmss");
		Date curDate = new Date();
		String voucherFilePath = BASEDIR + File.separator + voucherPath
				+ File.separator + "smsReply" + File.separator
				+ sdf.format(curDate);

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

			for (int f = 0; f < size; f++) {
				fileName = execlName + "_" + sdf.format(curDate) + "_["
						+ (f + 1) + "].xlsx";
				// 读取模板
				in = new FileInputStream(file);

				workbook = new XSSFWorkbook(in);

				XSSFCellStyle cellStyle = setCellStyle(workbook);

				XSSFSheet sheet = workbook.cloneSheet(0);
				workbook.removeSheetAt(0);
				workbook.setSheetName(0, execlName);
				// 读取模板工作表
				XSSFRow row = null;
				EmpExecutionContext.debug("群发任务短信回复工作薄创建与模板移除成功!");

				XSSFCell[] cell = new XSSFCell[4];

				for (int k = 0; k < ReplyParamsList.size(); k++) {
					ReplyParams replyParams = (ReplyParams) ReplyParamsList.get(k);
					//手机号码
					String phone = replyParams.getPhone();
					if (phone != null && !"".equals(phone) && isHidePhone == 0) {
						phone = cv.replacePhoneNumber(phone);
					}
					
					//姓名
					String name = replyParams.getName();
					
					//回复内容
					String content = replyParams.getContent();
					
					//回复时间
					String time = replyParams.getTime();
					if(StringUtils.isNotBlank(time)){
						time = time.substring(0, 19);
					}
					row = sheet.createRow(k + 1);

					cell[0] = row.createCell(0);
					cell[1] = row.createCell(1);
					cell[2] = row.createCell(2);
					cell[3] = row.createCell(3);
					
					cell[0].setCellStyle(cellStyle);
					cell[1].setCellStyle(cellStyle);
					cell[2].setCellStyle(cellStyle);
					cell[3].setCellStyle(cellStyle);
					
					cell[0].setCellValue(phone);
					cell[1].setCellValue(name);
					cell[2].setCellValue(content);
					cell[3].setCellValue(time);

				}
				os = new FileOutputStream(voucherFilePath + File.separator
						+ fileName);
				// 写入Excel对象
				workbook.write(os);
			}
			fileName = qfrwdxhf + sdf.format(curDate) + ".zip";
			filePath = BASEDIR + File.separator + voucherPath + File.separator
					+ "smsReply" + File.separator + fileName;

			ZipUtil.compress(voucherFilePath, filePath);
			deleteDir(fileTemp);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "群发任务短信回复导出异常！");
		} finally {
			// 清除对象
			if(os != null){
				try{
					os.close();
				}catch(Exception e){
					EmpExecutionContext.error(e,"关闭资源异常！");
				}
			}
			if(in != null){
				try{
					in.close();
				}catch(Exception e){
					EmpExecutionContext.error(e,"关闭资源异常！");
				}
			}
			workbook = null;
		}
		resultMap.put("FILE_NAME", fileName);
		resultMap.put("FILE_PATH", filePath);
		return resultMap;
	}
}
