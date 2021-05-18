package com.montnets.emp.shorturl.report.biz;

import com.montnets.emp.common.constant.CommonVariables;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.shorturl.report.vo.*;
import com.montnets.emp.util.ZipUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SurlTaskReportExcelTool {

	private final SimpleDateFormat timeStandard = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	// 报表模板目录
	protected String BASEDIR = "";

	// 操作员报表文件路径
	protected String voucherTemplatePath = "";

	// 发送账号下行报表文件路径
	protected String spvoucherTemplatePath = "";

	// 产生下行报表路径
	//public String voucherPath = "voucher";
	protected String voucherPath = "download";
	
	protected String TEMP = "temp";

	private final CommonVariables cv;

	public SurlTaskReportExcelTool(String context) {

		BASEDIR = context;
		cv = new CommonVariables();

	}

	/**
	 * 生成群发历史的excel

	 * @param mtdList
	 *            报表数据
	 * @return Map 生成文件成后，返回文件名和文件路径 throws 生成文件失败
	 */

	public Map<String, String> createMtReportExcel(List<LfMttaskVo> mtdList, HttpServletRequest request) throws Exception {

		String execlName = "SurlTaskRecord";
		HttpSession session = request.getSession();
		String langName = (String)session.getAttribute(StaticValue.LANG_KEY);

		voucherTemplatePath = BASEDIR + File.separator + TEMP + File.separator + "Surl_BatchSendTaskRecord_"+langName+".xlsx";
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Map<String, String> resultMap = new HashMap<String, String>();

		// 当前每页分页条数
		int intRowsOfPage = 500000;

		// 当前每页显示条数
		int intPagesCount = (mtdList.size() % intRowsOfPage == 0) ? (mtdList.size() / intRowsOfPage) : (mtdList.size() / intRowsOfPage + 1);

		int size = intPagesCount; // 生成的工作薄个数
		//中文内容国际化 标识变量
		//操作员
		String czy = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_146", request);
		//隶属机构
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

		EmpExecutionContext.info("工作表个数：" + size);
		if (size == 0) {
			EmpExecutionContext.info("无统计详情数据！");
			throw new Exception("无统计详情数据！");
		}

		// 产生报表文件的存储路径
		Date curDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddH24mmss");
		String voucherFilePath = BASEDIR + File.separator + voucherPath+File.separator+"SurlBatchSend"+File.separator+sdf.format(curDate);
		File fileTemp = new File(voucherFilePath);
		if(!fileTemp.exists()) {
            fileTemp.mkdirs();
        }

		// 报表文件名
		String fileName = null;
		String filePath=null;
		XSSFWorkbook workbook1 = null;
		SXSSFWorkbook workbook = null;
		FileInputStream in = null;
		FileOutputStream os = null;
		try {
			 //滞留数
            StringBuilder taskids = new StringBuilder();
			if(mtdList != null && mtdList.size()>0){
                for(LfMttaskVo mttaskVo:mtdList){
                    taskids.append(",").append(mttaskVo.getTaskId());
                }
            }
            Map<Long, Long> taskRemains = new HashMap<Long, Long>();
            if(taskids.length()>0){
                taskids.delete(0, 1);
                taskRemains = new SurlReportBiz().getTaskRemains(taskids.toString());
            }
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
				XSSFCell[] cell = new XSSFCell[14];

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
					Cell[] cell = new Cell[14];
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

				int intCnt = mtdList.size() < j * intRowsOfPage ? mtdList
						.size() : j * intRowsOfPage;

				Cell[] cell = new Cell[15];
				
				for (int k = 0; k < intCnt; k++) {

					LfMttaskVo mt =  mtdList.get(k);

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
					String recivefailcount = mt.getSendstate() == 2 ? "0" : (mt.getrFail2() == null ? "0" : mt.getrFail2().toString());


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

					//将内容中的#P_1#替换为{#参数1#}
					String param = MessageUtils.extractMessage("common","common_parameter",request);
					String replacement = "{#"+param+"$1#}";
					msgContent = msgContent.replaceAll("#P_(\\d+)#",replacement);
					long remains = (taskRemains==null||taskRemains.get(mt.getTaskId())==null)?0L:taskRemains.get(mt.getTaskId()) ;
					cell[13].setCellValue(remains);
					cell[14].setCellValue(msgContent);

				}
				os = new FileOutputStream(voucherFilePath + File.separator + fileName);
				// 写入Excel对象
				workbook.write(os);
			}

			fileName = "企业短链批次发送统计" + sdf.format(curDate) + "_"+ StaticValue.getServerNumber() +".zip";

			filePath = BASEDIR + File.separator + voucherPath + File.separator
					+ "SurlBatchSend" + File.separator + fileName;

			ZipUtil.compress(voucherFilePath, filePath);
			boolean flag = deleteDir(fileTemp);
			if (!flag) {
			    EmpExecutionContext.error("刪除文件失敗！");
            }
		} catch (Exception e) {
			EmpExecutionContext.error(e,"生成企业短链-批次发送统计的excel异常！");
		}finally{
			if(os!=null){
				// 清除对象
				os.close();
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

    public Map<String,String> createSendDetailExcel(List<SendDetailMttaskVo> mttaskVoList, String IsexportAll, int isHidePhone, HttpServletRequest request) throws Exception {
		String langName = (String) request.getSession(false).getAttribute(StaticValue.LANG_KEY);
		String execlName = "Surl_SendDetailRecord";
		if ("true".equals(IsexportAll)) {
			voucherTemplatePath = BASEDIR + File.separator
					+ TEMP + File.separator
					+ execlName +langName+".xlsx";
		} else {
			voucherTemplatePath = BASEDIR + File.separator
					+ TEMP + File.separator
					+ execlName + "OnlyPhone_"+langName+".xlsx";
		}
		Map<String, String> resultMap = new HashMap<String, String>();

		// 当前每页分页条数
		int intRowsOfPage = 500000;

		// 当前每页显示条数
		int intPagesCount = (mttaskVoList.size() % intRowsOfPage == 0) ? (mttaskVoList.size() / intRowsOfPage) : (mttaskVoList.size() / intRowsOfPage + 1);

		int size = intPagesCount; // 生成的工作薄个数

		EmpExecutionContext.info("企业短链-批次发送统计-发送详情报表导出，工作表个数：" + size);

		if (size == 0) {
			EmpExecutionContext.info("企业短链-批次发送统计-发送详情报表导出，无统计详情数据！");
			throw new Exception("企业短链-批次发送统计-发送详情报表导出，无统计详情数据！");
		}

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

		Date curDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddH24mmss");
		String voucherFilePath = BASEDIR + File.separator + voucherPath + File.separator + "batchSendDetail" + File.separator + sdf.format(curDate);
		File fileTemp = new File(voucherFilePath);
		// 报表文件名
		String fileName = null;
		String filePath = null;
		XSSFWorkbook workbook1 = null;
		SXSSFWorkbook workbook = null;
		FileInputStream in = null;
		FileOutputStream os = null;
		try {
			if(!fileTemp.exists()){
				fileTemp.mkdirs();
			}
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
				if ("true".equals(IsexportAll)) {
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
				}else {
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

				// 读取模板工作表
				Row row = null;
				// 总体流程
				// 根据记录，计算总的页数
				// 每页的处理 一页一个sheet
				// 写入页标题
				// 循环写入该页的行数，一次写一行，每页定义了多少行，就写多少数据。直到数据没有。
				// 写页尾

				int intCnt = mttaskVoList.size() < j * intRowsOfPage ? mttaskVoList.size() : j * intRowsOfPage;

				Cell[] cell = null;


				if ("true".equals(IsexportAll)) {
					for (int k = (j - 1) * intRowsOfPage; k < intCnt; k++) {

						SendDetailMttaskVo mt = mttaskVoList.get(k);
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
						if (phone != null && !"".equals(phone) && isHidePhone == 0) {
							phone = cv.replacePhoneNumber(phone);
						}
						// 短信文件
						String msg = mt.getMessage() != null ? mt.getMessage() : "";
						// 分条
						String icount = mt.getPknumber() + "/" + mt.getPktotal();
						// 状态
						String errorcode = mt.getErrorcode();
						if (errorcode != null) {
							if (errorcode.equals("DELIVRD") || errorcode.trim().equals("0")) {
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

						cell[0].setCellValue((double)k+1-((j - 1) * intRowsOfPage));
						cell[1].setCellValue(unicomString);
						cell[2].setCellValue(phone);
						cell[3].setCellValue(msg);
						cell[4].setCellValue(icount);
						cell[5].setCellValue(errorcode);
					}
				} else {
					for (int k = (j - 1) * intRowsOfPage; k < intCnt; k++) {

						SendDetailMttaskVo mt = mttaskVoList.get(k);

						// 手机号
						String phone = mt.getPhone() != null ? mt.getPhone()
								: "";
						if (phone != null && !"".equals(phone) && isHidePhone == 0) {
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
			fileName = "企业短链批次发送号码详情查看" + sdf.format(curDate) + ".zip";
			filePath = BASEDIR + File.separator + voucherPath + File.separator
					+ "batchSendDetail" + File.separator + fileName;

			ZipUtil.compress(voucherFilePath, filePath);
            boolean flag = deleteDir(fileTemp);
            if (!flag) {
                EmpExecutionContext.error("刪除文件失敗！");
            }
		}catch (Exception e){
			EmpExecutionContext.error(e,"企业短链-批次发送统计-发送详情报表导出异常！");
		}finally{
			if(os!=null){
				// 清除对象
				os.close();
			}
			workbook = null;
		}
		resultMap.put("FILE_NAME", fileName);
		resultMap.put("FILE_PATH", filePath);
		return resultMap;
    }

    public Map<String,String> createSendReplyDetailExcel(List<ReplyDetailVo> replyDetailVos, HttpServletRequest request) throws Exception {
        String langName = (String) request.getSession(false).getAttribute(StaticValue.LANG_KEY);
        //表格前缀名
        String execlName = "Surl_ReplyDetailRecord";
        voucherTemplatePath = BASEDIR + File.separator + TEMP + File.separator + execlName + "_" +langName+".xlsx";
        Map<String, String> resultMap = new HashMap<String, String>();

        // 当前每页分页条数
        int intRowsOfPage = 500000;

        // 当前每页显示条数
        int intPagesCount = (replyDetailVos.size() % intRowsOfPage == 0) ? (replyDetailVos.size() / intRowsOfPage) : (replyDetailVos.size() / intRowsOfPage + 1);

        int size = intPagesCount; // 生成的工作薄个数

        EmpExecutionContext.info("企业短链-批次发送统计-回复详情报表导出，工作表个数：" + size);

        if (size == 0) {
            EmpExecutionContext.info("企业短链-批次发送统计-发送详情报表导出，无统计详情数据！");
            throw new Exception("企业短链-批次发送统计-回复详情报表导出，无统计详情数据！");
        }
        Date curDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddH24mmss");
        String voucherFilePath = BASEDIR + File.separator + voucherPath + File.separator + "batchReplyDetail" + File.separator + sdf.format(curDate);
        File fileTemp = new File(voucherFilePath);
        // 报表文件名
        String fileName = null;
        String filePath = null;
        XSSFWorkbook workbook1 = null;
        SXSSFWorkbook workbook = null;
        FileInputStream in = null;
        FileOutputStream os = null;
        try{
            if(!fileTemp.exists()) {
                fileTemp.mkdirs();
            }
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

                XSSFCell[] cell = new XSSFCell[6];
                for (int i = 0; i < cell.length; i++) {
                    // 创建一个Excel的单元格
                    cell[i]=xrow.createCell(i);
                    // 设置单元格样式
                    cell[i].setCellStyle(cellStyle2);
                }
                cell[0].setCellValue("手机号码");
                cell[1].setCellValue("姓名");
                cell[2].setCellValue("回复内容");
                cell[3].setCellValue("回复时间");

                FileOutputStream xos = new FileOutputStream(voucherTemplatePath);
                wb.write(xos);
                xos.close();
            }
            for (int j = 1; j <= size; j++) {
                //文件名
                fileName = execlName + "_" + sdf.format(curDate) +"_[" + j + "]_"+ StaticValue.getServerNumber() +".xlsx";

                XSSFCellStyle cellStyle=null;
                Sheet sheet=null;

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

                // 读取模板工作表
                Row row = null;
                // 总体流程
                // 根据记录，计算总的页数
                // 每页的处理 一页一个sheet
                // 写入页标题
                // 循环写入该页的行数，一次写一行，每页定义了多少行，就写多少数据。直到数据没有。
                // 写页尾

                int intCnt = replyDetailVos.size() < j * intRowsOfPage ? replyDetailVos.size() : j * intRowsOfPage;

                Cell[] cell = null;

                for (int k = (j - 1) * intRowsOfPage; k < intCnt; k++) {

                    ReplyDetailVo replyDetailVo = replyDetailVos.get(k);
                    //姓名
                    String name = replyDetailVo.getReplyName();

                    // 手机号
                    String phone = replyDetailVo.getPhone() != null ? replyDetailVo.getPhone() : "";
                    /*
                    if (phone != null && !"".equals(phone) && isHidePhone == 0) {
                        phone = cv.replacePhoneNumber(phone);
                    }
                    */
                    // 回复内容
                    String msg = replyDetailVo.getMsgContent() != null ? replyDetailVo.getMsgContent() : "";

                    // 回复时间
                    String time = replyDetailVo.getDeliverTime() == null ? "" : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(replyDetailVo.getDeliverTime().getTime());

                    cell = new Cell[4];
                    row = sheet.createRow(k+1-((j - 1) * intRowsOfPage));
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
                    cell[2].setCellValue(msg);
                    cell[3].setCellValue(time);
                }
                os = new FileOutputStream(voucherFilePath + File.separator + fileName);
                // 写入Excel对象
                workbook.write(os);
                fileName = "企业短链批次发送回复详情查看" + sdf.format(curDate) + ".zip";
                filePath = BASEDIR + File.separator + voucherPath + File.separator
                        + "batchReplyDetail" + File.separator + fileName;

                ZipUtil.compress(voucherFilePath, filePath);
                boolean flag = deleteDir(fileTemp);
                if (!flag) {
                    EmpExecutionContext.error("刪除文件失敗！");
                }
            }
        }catch (Exception e){
            EmpExecutionContext.error(e,"企业短链-批次发送统计-回复详情报表导出异常！");
        }finally{
        	if(os!=null){
                // 清除对象
                os.close();
        	}
            workbook = null;
        }
        resultMap.put("FILE_NAME", fileName);
        resultMap.put("FILE_PATH", filePath);
        return resultMap;
	}

	public Map<String,String> createBatchVisitExcel(List<BatchVisitVo> batchVisitVos, HttpServletRequest request) throws Exception {
		String langName = (String) request.getSession(false).getAttribute(StaticValue.LANG_KEY);
		//表格前缀名
		String execlName = "Surl_BatchVisitRecord";
		voucherTemplatePath = BASEDIR + File.separator + TEMP + File.separator + execlName + "_" +langName+".xlsx";
		Map<String, String> resultMap = new HashMap<String, String>();

		// 当前每页分页条数
		int intRowsOfPage = 500000;

		// 当前每页显示条数
		int intPagesCount = (batchVisitVos.size() % intRowsOfPage == 0) ? (batchVisitVos.size() / intRowsOfPage) : (batchVisitVos.size() / intRowsOfPage + 1);

		int size = intPagesCount; // 生成的工作薄个数

		EmpExecutionContext.info("企业短链-批次发送统计-回复详情报表导出，工作表个数：" + size);

		if (size == 0) {
			EmpExecutionContext.info("企业短链-批次访问统计，无统计详情数据！");
			throw new Exception("企业短链-批次访问统计，无统计详情数据！");
		}
		Date curDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddH24mmss");
		String voucherFilePath = BASEDIR + File.separator + voucherPath + File.separator + "batchVisit" + File.separator + sdf.format(curDate);
		File fileTemp = new File(voucherFilePath);
		// 报表文件名
		String fileName = null;
		String filePath = null;
		XSSFWorkbook workbook1 = null;
		SXSSFWorkbook workbook = null;
		FileInputStream in = null;
		FileOutputStream os = null;
		try {
			if (!fileTemp.exists()) {
				fileTemp.mkdirs();
			}
			// 创建只读的Excel工作薄的对象, 此为模板文件
			File file = new File(voucherTemplatePath);
			//判断模板文件是否存在
			if (!file.exists()) {
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

				XSSFCell[] cell = new XSSFCell[11];
				for (int i = 0; i < cell.length; i++) {
					// 创建一个Excel的单元格
					cell[i] = xrow.createCell(i);
					// 设置单元格样式
					cell[i].setCellStyle(cellStyle2);
				}
				cell[0].setCellValue("操作员");
				cell[1].setCellValue("隶属机构");
				cell[2].setCellValue("发送主题");
				cell[3].setCellValue("任务批次");
				cell[4].setCellValue("信息内容");
				cell[5].setCellValue("长地址");
				cell[6].setCellValue("发送时间");
				cell[7].setCellValue("有效时间");
				cell[8].setCellValue("号码个数");
				cell[9].setCellValue("访问人数");
				cell[10].setCellValue("访问次数");

				FileOutputStream xos = new FileOutputStream(voucherTemplatePath);
				wb.write(xos);
				xos.close();
			}
			for (int j = 1; j <= size; j++) {
				//文件名
				fileName = execlName + "_" + sdf.format(curDate) +"_[" + j + "]_"+ StaticValue.getServerNumber() +".xlsx";

				XSSFCellStyle cellStyle=null;
				Sheet sheet=null;

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

				// 读取模板工作表
				Row row = null;
				// 总体流程
				// 根据记录，计算总的页数
				// 每页的处理 一页一个sheet
				// 写入页标题
				// 循环写入该页的行数，一次写一行，每页定义了多少行，就写多少数据。直到数据没有。
				// 写页尾

				int intCnt = batchVisitVos.size() < j * intRowsOfPage ? batchVisitVos.size() : j * intRowsOfPage;

				Cell[] cell = null;

				for (int k = (j - 1) * intRowsOfPage; k < intCnt; k++) {

					BatchVisitVo vo = batchVisitVos.get(k);
					//操作员
					String userName = vo.getUserName() != null ? vo.getUserName():"";
					if("".equals(userName) && vo.getUserState() == 2){
						userName += "(已注销)";
					}

					//隶属机构
					String depName = vo.getDepName() != null ? vo.getDepName() : "";

					// 发送主题
					String title = vo.getTitle() != null ? vo.getTitle() : "";

					// 任务批次
					String taskId = vo.getTaskId() == null ? "" : vo.getTaskId().toString();

					//信息内容
					String msg = vo.getMsg() != null ? vo.getMsg() : "";

					//长地址
					String longUrl = vo.getNetUrl() != null ? vo.getNetUrl() : "";

					//发送时间
					String sendTime = vo.getPlanTime() == null ? "" : timeStandard.format(vo.getPlanTime().getTime());

					//有效时间
					String effectiveTime = vo.getInvalidTm() == null ? "" : timeStandard.format(vo.getInvalidTm().getTime());

					//号码个数
					String phoneAmount = vo.getEffCount() != null ? vo.getEffCount() : "";

					//访问人数
					Integer visitorCount = vo.getVisitorCount() == null ? 0 : vo.getVisitorCount();

					//访问次数
					Integer visitCount = vo.getVisitCount() == null ? 0 : vo.getVisitCount();

					cell = new Cell[11];
					row = sheet.createRow(k+1-((j - 1) * intRowsOfPage));

					for(int i = 0;i < cell.length;i++){
						cell[i] = row.createCell(i);
						cell[i].setCellStyle(cellStyle);
					}

					cell[0].setCellValue(userName);
					cell[1].setCellValue(depName);
					cell[2].setCellValue(title);
					cell[3].setCellValue(taskId);
					cell[4].setCellValue(msg);
					cell[5].setCellValue(longUrl);
					cell[6].setCellValue(sendTime);
					cell[7].setCellValue(effectiveTime);
					cell[8].setCellValue(phoneAmount);
					cell[9].setCellValue(visitorCount);
					cell[10].setCellValue(visitCount);

				}
				os = new FileOutputStream(voucherFilePath + File.separator + fileName);
				// 写入Excel对象
				workbook.write(os);
				fileName = "企业短链批次访问查看" + sdf.format(curDate) + ".zip";
				filePath = BASEDIR + File.separator + voucherPath + File.separator
						+ "batchVisit" + File.separator + fileName;

				ZipUtil.compress(voucherFilePath, filePath);
                boolean flag = deleteDir(fileTemp);
                if (!flag) {
                    EmpExecutionContext.error("刪除文件失敗！");
                }
			}
		}catch (Exception e){
			EmpExecutionContext.error(e,"企业短链-批次访问统计报表导出异常！");
		}finally{
			if(os!=null){
				// 清除对象
				os.close();
			}
			workbook = null;
		}
		resultMap.put("FILE_NAME", fileName);
		resultMap.put("FILE_PATH", filePath);
		return resultMap;
	}

	public Map<String,String> createBatchVisitDetailExcel(List<VstDetailVo> vstDetailVoList, String isExportAll, HttpServletRequest request) throws Exception {
		String langName = (String) request.getSession(false).getAttribute(StaticValue.LANG_KEY);
		String execlName = "Surl_PhoneDetailRecord";
		if ("true".equals(isExportAll)) {
			voucherTemplatePath = BASEDIR + File.separator + TEMP + File.separator + execlName +langName+".xlsx";
		} else {
			voucherTemplatePath = BASEDIR + File.separator + TEMP + File.separator + "Surl_SendDetailRecordOnlyPhone_"+langName+".xlsx";
		}
		Map<String, String> resultMap = new HashMap<String, String>();

		// 当前每页分页条数
		int intRowsOfPage = 500000;

		// 当前每页显示条数
		int intPagesCount = (vstDetailVoList.size() % intRowsOfPage == 0) ? (vstDetailVoList.size() / intRowsOfPage) : (vstDetailVoList.size() / intRowsOfPage + 1);

		int size = intPagesCount; // 生成的工作薄个数

		EmpExecutionContext.info("企业短链-批次访问统计-号码详情查看报表导出，工作表个数：" + size);

		if (size == 0) {
			EmpExecutionContext.info("企业短链-批次访问统计-号码详情查看报表导出，无统计详情数据！");
			throw new Exception("企业短链-批次访问统计-号码详情查看报表导出，无统计详情数据！");
		}
		Date curDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddH24mmss");
		String voucherFilePath = BASEDIR + File.separator + voucherPath + File.separator + "batchVisitDetail" + File.separator + sdf.format(curDate);
		File fileTemp = new File(voucherFilePath);
		// 报表文件名
		String fileName = null;
		String filePath = null;
		XSSFWorkbook workbook1 = null;
		SXSSFWorkbook workbook = null;
		FileInputStream in = null;
		FileOutputStream os = null;
		try {
			if(!fileTemp.exists()){
				fileTemp.mkdirs();
			}
			// 创建只读的Excel工作薄的对象, 此为模板文件
			File file = new File(voucherTemplatePath);

			//判断模板文件是否存在
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
				if ("true".equals(isExportAll)) {
					XSSFCell[] cell = new XSSFCell[6];
					for (int i = 0; i < cell.length; i++) {
						// 创建一个Excel的单元格
						cell[i]=xrow.createCell(i);
						// 设置单元格样式
						cell[i].setCellStyle(cellStyle2);
					}
					cell[0].setCellValue("手机号码");
					cell[1].setCellValue("区域");
					cell[2].setCellValue("访问状态");
					cell[3].setCellValue("访问时间");
					cell[4].setCellValue("访问IP");
				}else {
					XSSFCell[] cell= new XSSFCell[1];
					for (int i = 0; i < cell.length; i++) {
						// 创建一个Excel的单元格
						cell[i]=xrow.createCell(i);
						// 设置单元格样式
						cell[i].setCellStyle(cellStyle2);
					}
					cell[0].setCellValue("手机号");
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

				// 读取模板工作表
				Row row = null;
				// 总体流程
				// 根据记录，计算总的页数
				// 每页的处理 一页一个sheet
				// 写入页标题
				// 循环写入该页的行数，一次写一行，每页定义了多少行，就写多少数据。直到数据没有。
				// 写页尾

				int intCnt = vstDetailVoList.size() < j * intRowsOfPage ? vstDetailVoList.size() : j * intRowsOfPage;

				Cell[] cell = null;


				if ("true".equals(isExportAll)) {
					for (int k = (j - 1) * intRowsOfPage; k < intCnt; k++) {
						VstDetailVo vo = vstDetailVoList.get(k);
						// 手机号
						String phone = vo.getPhone() != null ? vo.getPhone() : "";
						/*
						if (phone != null && !"".equals(phone) && isHidePhone == 0) {
							phone = cv.replacePhoneNumber(phone);
						}
						*/
						// 区域
						String areaName = vo.getAreaName() != null ? vo.getAreaName() : "";

						// 访问状态
						String visitStatus = vo.getVisitStatus() != null ? vo.getVisitStatus() : "";

						// 访问时间
						String visitTime = vo.getVsttm() != null ? vo.getVsttm() : "";
						if(visitTime != null && visitTime.indexOf(".")>0){
							visitTime=visitTime.substring(0, visitTime.indexOf("."));
						}

						//访问IP
						String visitIP = vo.getVisitIP() != null ? vo.getVisitIP() : "";

						cell = new Cell[5];
						row = sheet.createRow(k+1-((j - 1) * intRowsOfPage));
						for (int i = 0; i < cell.length; i++) {
							// 创建一个Excel的单元格
							cell[i] = row.createCell(i);
							// 设置单元格样式
							cell[i].setCellStyle(cellStyle);
						}

						cell[0].setCellValue(phone);
						cell[1].setCellValue(areaName);
						cell[2].setCellValue((visitStatus != null && !"".equals(visitStatus)) ? "1".equals(visitStatus) ? "已访问":"未访问":"");
						cell[3].setCellValue(visitTime);
						cell[4].setCellValue(visitIP);
					}
				} else {
					for (int k = (j - 1) * intRowsOfPage; k < intCnt; k++) {
						VstDetailVo vstDetailVo = vstDetailVoList.get(k);
						// 手机号
						String phone = vstDetailVo.getPhone() != null ? vstDetailVo.getPhone() : "";
						/*
						if (phone != null && !"".equals(phone) && isHidePhone == 0) {
							phone = cv.replacePhoneNumber(phone);
						}
						*/
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
			fileName = "企业短链批次访问号码详情查看" + sdf.format(curDate) + ".zip";
			filePath = BASEDIR + File.separator + voucherPath + File.separator
					+ "batchVisitDetail" + File.separator + fileName;

			ZipUtil.compress(voucherFilePath, filePath);
            boolean flag = deleteDir(fileTemp);
            if (!flag) {
                EmpExecutionContext.error("刪除文件失敗！");
            }
		}catch (Exception e){
			EmpExecutionContext.error(e,"企业短链-批次发送统计-发送详情报表导出异常！");
		}finally{
			if(os!=null){
				// 清除对象
				os.close();
			}
			workbook = null;
		}
		resultMap.put("FILE_NAME", fileName);
		resultMap.put("FILE_PATH", filePath);
		return resultMap;
	}
	
	
	
	/**
	 * 链接报表详情导出
	 * @param linkReportList
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> createLinkReportExcel(List<LinkReportVo> linkReportList, HttpServletRequest request) throws Exception {

		String execlName = "SurlLinkReportRecord";
		HttpSession session = request.getSession();
		String langName = (String) session.getAttribute(StaticValue.LANG_KEY);

		voucherTemplatePath = BASEDIR + File.separator + TEMP + File.separator + "Surl_LinkReportRecord_" + langName + ".xlsx";

		Map<String, String> resultMap = new HashMap<String, String>();

		// 当前每页分页条数
		int intRowsOfPage = 500000;

		// 当前每页显示条数
		int intPagesCount = (linkReportList.size() % intRowsOfPage == 0) ? (linkReportList.size() / intRowsOfPage) : (linkReportList.size() / intRowsOfPage + 1);

		int size = intPagesCount; // 生成的工作薄个数
		// 中文内容国际化 标识变量
		// 操作员
		String czy = "长链接";
		// 隶属机构
		String lsjg = "号码个数";
		String spzh = "发送成功号码数";
		String fslx = "访问人数";
		String fszt = "访问次数";

		EmpExecutionContext.info("工作表个数：" + size);
		if (size == 0) {
			EmpExecutionContext.info("无统计详情数据！");
			throw new Exception("无统计详情数据！");
		}

		// 产生报表文件的存储路径
		Date curDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddH24mmss");
		String voucherFilePath = BASEDIR + File.separator + voucherPath + File.separator + "SurlBatchSend" + File.separator + sdf.format(curDate);
		File fileTemp = new File(voucherFilePath);
		if (!fileTemp.exists()) {
            fileTemp.mkdirs();
        }

		// 报表文件名
		String fileName = null;
		String filePath = null;
		XSSFWorkbook workbook1 = null;
		SXSSFWorkbook workbook = null;
		FileInputStream in = null;
		FileOutputStream os = null;
		try {
			// 创建只读的Excel工作薄的对象, 此为模板文件
			File file = new File(voucherTemplatePath);

			// 判断模板文件是否存在
			if (!file.exists()) {

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
				XSSFCell[] cell = new XSSFCell[14];

				for (int i = 0; i < cell.length; i++) {
					// 创建一个Excel的单元格
					cell[i] = xrow.createCell(i);
					// 设置单元格样式
					cell[i].setCellStyle(cellStyle2);
				}

				// 给Excel的单元格设置样式和赋值
				cell[0].setCellValue(czy);
				cell[1].setCellValue(lsjg);
				cell[2].setCellValue(spzh);
				cell[3].setCellValue(fslx);
				cell[4].setCellValue(fszt);
				FileOutputStream xos = new FileOutputStream(voucherTemplatePath);
				wb.write(xos);
				xos.close();
			}

			for (int j = 1; j <= size; j++) {
				// 文件名
				fileName = execlName + "_" + sdf.format(curDate) + "_[" + j + "]_" + StaticValue.getServerNumber() + ".xlsx";

				XSSFCellStyle cellStyle = null;
				Sheet sheet = null;
				// 如果文件存在
				if (file.exists()) {
					// 读取模板
					in = new FileInputStream(file);
					// 工作表
					workbook1 = new XSSFWorkbook(in);
					workbook1.setSheetName(0, execlName);
					EmpExecutionContext.info("工作薄创建与模板报表移除成功!");
					workbook = new SXSSFWorkbook(workbook1, 10000);
					sheet = workbook.getSheetAt(0);
					// 表格样式
					cellStyle = setCellStyle(workbook1);
					in.close();
				} else {
					// 文件不存在，则创建
					workbook = new SXSSFWorkbook();
					sheet = workbook.createSheet(execlName);

					Row topRow = sheet.createRow(0);
					Cell[] cell = new Cell[14];
					for (int i = 0; i < cell.length; i++) {
						cell[i] = topRow.createCell(i);
						cell[i].setCellStyle(cellStyle);
					}
					cell[0].setCellValue(czy);
					cell[1].setCellValue(lsjg);
					cell[2].setCellValue(spzh);
					cell[3].setCellValue(fslx);
					cell[4].setCellValue(fszt);
				}
				// 读取模板工作表
				Row row = null;
				// 总体流程
				// 根据记录，计算总的页数
				// 每页的处理 一页一个sheet
				// 写入页标题
				// 循环写入该页的行数，一次写一行，每页定义了多少行，就写多少数据。直到数据没有。
				// 写页尾

				int intCnt = linkReportList.size() < j * intRowsOfPage ? linkReportList.size() : j * intRowsOfPage;

				Cell[] cell = new Cell[14];

				for (int k = 0; k < intCnt; k++) {

					LinkReportVo mt = (LinkReportVo) linkReportList.get(k);
					row = sheet.createRow(k + 1);
					for (int i = 0; i < cell.length; i++) {
						cell[i] = row.createCell(i);
						cell[i].setCellStyle(cellStyle);
					}
					cell[0].setCellValue(mt.getUrl() == null ? "" : mt.getUrl());
					cell[1].setCellValue(mt.getTotalNum() == null ? "0" : mt.getTotalNum() + "");
					cell[2].setCellValue(mt.getEffCount() == null ? "0" : mt.getEffCount() + "");
					cell[3].setCellValue(mt.getVisitCount() == null ? "0" : mt.getVisitCount() + "");
					cell[4].setCellValue(mt.getVisitNum() == null ? "0" : mt.getVisitNum() + "");

				}
				os = new FileOutputStream(voucherFilePath + File.separator + fileName);
				// 写入Excel对象
				workbook.write(os);
			}
			fileName = "链接访问统计" + sdf.format(curDate) + "_" + StaticValue.getServerNumber() + ".zip";
			filePath = BASEDIR + File.separator + voucherPath + File.separator + "SurlBatchSend" + File.separator + fileName;

			ZipUtil.compress(voucherFilePath, filePath);
            boolean flag = deleteDir(fileTemp);
            if (!flag) {
                EmpExecutionContext.error("刪除文件失敗！");
            }
		} catch (Exception e) {
			EmpExecutionContext.error(e, "生成企业短链-企业链接访问统计的excel异常！");
		} finally {
			if(os!=null){
				// 清除对象
				os.close();
			}
			workbook = null;
		}
		resultMap.put("FILE_NAME", fileName);
		resultMap.put("FILE_PATH", filePath);
		return resultMap;
	}
	
	/**
	 * 访问详情报表导出
	 * @param linkReportList
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> createLinkDetailExcel(List<LinkDetailVo> linkReportList, HttpServletRequest request) throws Exception {

		String execlName = "SurlLinkDetailRecord";
		HttpSession session = request.getSession();
		String langName = (String) session.getAttribute(StaticValue.LANG_KEY);

		voucherTemplatePath = BASEDIR + File.separator + TEMP + File.separator + "Surl_LinkDetailRecord_" + langName + ".xlsx";

		Map<String, String> resultMap = new HashMap<String, String>();

		// 当前每页分页条数
		int intRowsOfPage = 500000;

		// 当前每页显示条数
		int intPagesCount = (linkReportList.size() % intRowsOfPage == 0) ? (linkReportList.size() / intRowsOfPage) : (linkReportList.size() / intRowsOfPage + 1);

		int size = intPagesCount; // 生成的工作薄个数
		// 中文内容国际化 标识变量
		// 操作员
		String czy = "手机号码";
		// 隶属机构
		String lsjg = "区域";
		String spzh = "访问状态";
		String fslx = "访问次数";
		String fszt = "末次访问时间";

		EmpExecutionContext.info("工作表个数：" + size);
		if (size == 0) {
			EmpExecutionContext.info("无统计详情数据！");
			throw new Exception("无统计详情数据！");
		}

		// 产生报表文件的存储路径
		Date curDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddH24mmss");
		String voucherFilePath = BASEDIR + File.separator + voucherPath + File.separator + "SurlBatchSend" + File.separator + sdf.format(curDate);
		File fileTemp = new File(voucherFilePath);
		if (!fileTemp.exists()) {
            fileTemp.mkdirs();
        }

		// 报表文件名
		String fileName = null;
		String filePath = null;
		XSSFWorkbook workbook1 = null;
		SXSSFWorkbook workbook = null;
		FileInputStream in = null;
		FileOutputStream os = null;
		try {
			// 创建只读的Excel工作薄的对象, 此为模板文件
			File file = new File(voucherTemplatePath);

			// 判断模板文件是否存在
			if (!file.exists()) {

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
				XSSFCell[] cell = new XSSFCell[14];

				for (int i = 0; i < cell.length; i++) {
					// 创建一个Excel的单元格
					cell[i] = xrow.createCell(i);
					// 设置单元格样式
					cell[i].setCellStyle(cellStyle2);
				}

				// 给Excel的单元格设置样式和赋值
				cell[0].setCellValue(czy);
				cell[1].setCellValue(lsjg);
				cell[2].setCellValue(spzh);
				cell[3].setCellValue(fslx);
				cell[4].setCellValue(fszt);
				cell[5].setCellValue("末次访问IP");
				cell[6].setCellValue("任务批次");
				cell[7].setCellValue("发送主题");
				cell[8].setCellValue("发送时间");
				FileOutputStream xos = new FileOutputStream(voucherTemplatePath);
				wb.write(xos);
				xos.close();
			}

			for (int j = 1; j <= size; j++) {
				// 文件名
				fileName = execlName + "_" + sdf.format(curDate) + "_[" + j + "]_" + StaticValue.getServerNumber() + ".xlsx";

				XSSFCellStyle cellStyle = null;
				Sheet sheet = null;
				// 如果文件存在
				if (file.exists()) {
					// 读取模板
					in = new FileInputStream(file);
					// 工作表
					workbook1 = new XSSFWorkbook(in);
					workbook1.setSheetName(0, execlName);
					EmpExecutionContext.info("工作薄创建与模板报表移除成功!");
					workbook = new SXSSFWorkbook(workbook1, 10000);
					sheet = workbook.getSheetAt(0);
					// 表格样式
					cellStyle = setCellStyle(workbook1);
					in.close();
				} else {
					// 文件不存在，则创建
					workbook = new SXSSFWorkbook();
					sheet = workbook.createSheet(execlName);

					Row topRow = sheet.createRow(0);
					Cell[] cell = new Cell[14];
					for (int i = 0; i < cell.length; i++) {
						cell[i] = topRow.createCell(i);
						cell[i].setCellStyle(cellStyle);
					}
					cell[0].setCellValue(czy);
					cell[1].setCellValue(lsjg);
					cell[2].setCellValue(spzh);
					cell[3].setCellValue(fslx);
					cell[4].setCellValue(fszt);
					cell[5].setCellValue("末次访问IP");
					cell[6].setCellValue("任务批次");
					cell[7].setCellValue("发送主题");
					cell[8].setCellValue("发送时间");

				}
				// 读取模板工作表
				Row row = null;
				// 总体流程
				// 根据记录，计算总的页数
				// 每页的处理 一页一个sheet
				// 写入页标题
				// 循环写入该页的行数，一次写一行，每页定义了多少行，就写多少数据。直到数据没有。
				// 写页尾

				int intCnt = linkReportList.size() < j * intRowsOfPage ? linkReportList.size() : j * intRowsOfPage;

				Cell[] cell = new Cell[14];

				for (int k = 0; k < intCnt; k++) {

					LinkDetailVo mt = (LinkDetailVo) linkReportList.get(k);
					row = sheet.createRow(k + 1);
					for (int i = 0; i < cell.length; i++) {
						cell[i] = row.createCell(i);
						cell[i].setCellStyle(cellStyle);
					}
					cell[0].setCellValue(mt.getPhone() == null ? "" : mt.getPhone());
					cell[1].setCellValue("***");
					cell[2].setCellValue((mt.getVisitCount() != null && mt.getVisitCount() > 0) ? "已访问" : "未访问");
					cell[3].setCellValue(mt.getVisitCount() == null ? "0" : mt.getVisitCount() + "");
					cell[4].setCellValue(mt.getLastVisitTime() == null ? "-" : timeStandard.format(mt.getLastVisitTime()) + "");
					cell[5].setCellValue(mt.getLastIP() == null ? "-" : mt.getLastIP());
					cell[6].setCellValue(mt.getTaskId() == null ? "-" : mt.getTaskId());
					cell[7].setCellValue(mt.getTitle() == null ? "-" : mt.getTitle());
					cell[8].setCellValue(mt.getSendTime() == null ? "-" : timeStandard.format(mt.getSendTime()) + "");

				}
				os = new FileOutputStream(voucherFilePath + File.separator + fileName);
				// 写入Excel对象
				workbook.write(os);
			}
			fileName = "链接访问号码详情" + sdf.format(curDate) + "_" + StaticValue.getServerNumber() + ".zip";
			filePath = BASEDIR + File.separator + voucherPath + File.separator + "SurlBatchSend" + File.separator + fileName;

			ZipUtil.compress(voucherFilePath, filePath);
            boolean flag = deleteDir(fileTemp);
            if (!flag) {
                EmpExecutionContext.error("刪除文件失敗！");
            }
		} catch (Exception e) {
			EmpExecutionContext.error(e, "生成企业短链-链接访问号码详情的excel异常！");
		} finally {
			if(os!=null){
				// 清除对象
				os.close();
			}
			workbook = null;
		}
		resultMap.put("FILE_NAME", fileName);
		resultMap.put("FILE_PATH", filePath);
		return resultMap;
	}
}
