package com.montnets.emp.appmage.biz;

import com.montnets.emp.appmage.vo.MttaskDetailVo;
import com.montnets.emp.appmage.vo.MttaskSelectVo;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.util.ZipUtil;
import org.apache.poi.ss.usermodel.FontUnderline;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MttaskSelectExcelTool {

	// 报表模板目录
	private	 String BASEDIR = "";

	// 操作员报表文件路径
	private String voucherTemplatePath = "";

	// 发送账号下行报表文件路径
	private final String spvoucherTemplatePath = "";

	// 产生下行报表路径
	private final String voucherPath = "download";

	private final String SAMPLES = "samples";

	private final String TEMP = "temp";

	public MttaskSelectExcelTool(String context) {
		BASEDIR = context;
	}

	/**
	 * @description：生成发送详情Excel
	 * @param
	 *
	 * @param mtdList
	 *            报表数据
	 * @return Map 生成文件成后，返回文件名和文件路径 throws 生成文件失败
	 * @author 
	 * @see  2012-1-9 创建
	 */

	public Map<String, String> createSmsMtReportExcel(List<MttaskDetailVo> mtdList,String msgtype,HttpServletRequest request) throws Exception
	{
		String execlName = "App_sendRecord";
		voucherTemplatePath = BASEDIR + File.separator + TEMP + File.separator + "app_sendRecord_" + request.getSession().getAttribute(StaticValue.LANG_KEY) +".xlsx";
		Map<String, String> resultMap = new HashMap<String, String>();
		// 当前每页分页条数
		int intRowsOfPage = 500000;
		// 当前每页显示条数
		int intPagesCount = (mtdList.size() % intRowsOfPage == 0) ? (mtdList.size() / intRowsOfPage) : (mtdList.size() / intRowsOfPage + 1);
		int size = intPagesCount; // 生成的工作薄个数
		java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		EmpExecutionContext.info("工作表个数：" + size);
		if(size == 0)
		{
			EmpExecutionContext.info("无统计详情数据！");
			throw new Exception("无统计详情数据！");
		}
		// 产生报表文件的存储路径
		Date curDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddH24mmss");
		String voucherFilePath = BASEDIR + File.separator + voucherPath + File.separator + "groupHisDetail" + File.separator + sdf.format(curDate);
		File fileTemp = new File(voucherFilePath);
		// 报表文件名
		String fileName = null;
		String filePath = null;
		XSSFWorkbook workbook = null;
		FileInputStream in = null;
		FileOutputStream os = null;
		try
		{
			if(!fileTemp.exists()) {
                fileTemp.mkdirs();
            }
			// 创建只读的Excel工作薄的对象, 此为模板文件
			File file = new File(voucherTemplatePath);
			for (int j = 1; j <= size; j++)
			{
				fileName = execlName + "_" + sdf.format(curDate) + "_[" + j + "]_.xlsx";
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
				int intCnt = mtdList.size() < j * intRowsOfPage ? mtdList.size() : j * intRowsOfPage;
				XSSFCell[] cell = null;
				for (int k = (j - 1) * intRowsOfPage; k < intCnt; k++)
				{
					MttaskDetailVo vo = (MttaskDetailVo) mtdList.get(k);
					// 用户APP账号
					String appuseracount = vo.getAppuseraccount() != null ? vo.getAppuseraccount() : "--";
					// 用户昵称
					String appusername = vo.getAppusername() != null ? vo.getAppusername() : "--";
					// 发送主题
					String title = vo.getTitle() != null ? vo.getTitle() : "";
					// 内容
					String content = "";
					if(!"0".equals(msgtype))
					{
						if(!"4".equals(msgtype))
						{
							content = MessageUtils.extractMessage("appmage", "appmage_javacode_text_1", request);
						}
					}
					else
					{
						content = vo.getContent() == null || "".equals(vo.getContent()) ? "" : vo.getContent();
					}
					
					// 发送状态
					String sendstate = "";
					 //发送状态
					if(vo.getRptstate()!= null&&"0".equals(vo.getRptstate())){ 
						sendstate = MessageUtils.extractMessage("appmage", "appmage_javacode_text_2", request);  
					}else if(vo.getRptstate()!=null&&"1".equals(vo.getRptstate())){
						sendstate = MessageUtils.extractMessage("appmage", "appmage_javacode_text_3", request);
					}else{ 
						sendstate = MessageUtils.extractMessage("appmage", "appmage_javacode_text_4", request);
					}
					//发送时间
					//String createtime=vo.getCreatetime()==null&&!"".equals(vo.getCreatetime())?"":df.format(vo.getCreatetime());  //findbugs
					String createtime=vo.getCreatetime()==null?"":df.format(vo.getCreatetime());
					//回执状态
					String rptstate = "";
					
					if(vo.getRptstate() != null && "0".equals(vo.getRptstate())){
						rptstate = MessageUtils.extractMessage("appmage", "appmage_javacode_text_5", request);
					}else if(vo.getRptstate() != null && "1".equals(vo.getRptstate())){
						rptstate = MessageUtils.extractMessage("appmage", "appmage_javacode_text_6", request);
					}else{
						rptstate = MessageUtils.extractMessage("appmage", "appmage_javacode_text_7", request);
					}
					
					
					//发送时间
					//String recrpttime=vo.getRecrpttime()==null&&!"".equals(vo.getRecrpttime())?"":df.format(vo.getRecrpttime());  //findbugs
					String recrpttime=vo.getRecrpttime()==null?"":df.format(vo.getRecrpttime());
					cell = new XSSFCell[8];
					row = sheet.createRow(k + 1);
					for (int i = 0; i < cell.length; i++)
					{
						cell[i] = row.createCell(i);
						cell[i].setCellStyle(cellStyle);
					}

					cell[0].setCellValue(appuseracount);
					cell[1].setCellValue(appusername);
					cell[2].setCellValue(title);
					cell[3].setCellValue(content);
					cell[4].setCellValue(sendstate);
					cell[5].setCellValue(createtime);
					cell[6].setCellValue(rptstate);
					cell[7].setCellValue(recrpttime);
				}
			}
			os = new FileOutputStream(voucherFilePath + File.separator + fileName);
			// 写入Excel对象
			workbook.write(os);
			fileName = MessageUtils.extractMessage("appmage", "appmage_javacode_text_8", request) + sdf.format(curDate) + ".zip";
			filePath = BASEDIR + File.separator + voucherPath + File.separator + "groupHisDetail" + File.separator + fileName;

			ZipUtil.compress(voucherFilePath, filePath);
			boolean status = deleteDir(fileTemp);
			if (!status) {
			    EmpExecutionContext.error("刪除文件失敗！");
            }
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "群发历史详情导出失败");
		}
		finally
		{
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
	 * 生成app发送任务查询execl
	 * 
	 * @param mtdList
	 *        导出数据list
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> createMtReportExcel(List<MttaskSelectVo> mtdList, HttpServletRequest request)
			throws Exception {
		//excel名称
		String execlName = "AppTaskRecord";
		//excel路径
		voucherTemplatePath = BASEDIR + File.separator + TEMP+ File.separator + "app_mttaskselect_" + request.getSession().getAttribute(StaticValue.LANG_KEY) + ".xlsx";
		java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map<String, String> resultMap = new HashMap<String, String>();
		// 当前每页分页条数
		int intRowsOfPage = 500000;
		// 当前每页显示条数
		int intPagesCount = (mtdList.size() % intRowsOfPage == 0) ? (mtdList.size() / intRowsOfPage) : (mtdList.size() / intRowsOfPage + 1);
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
						
				XSSFCell[] cell = new XSSFCell[11];

				for (int k = 0; k < intCnt; k++) {

					MttaskSelectVo mt = (MttaskSelectVo) mtdList.get(k);
					// 发送时间
					String sendtime = mt.getBigintime() == null ? "" : df.format(mt.getBigintime());
					// 发送标题
					String title = mt.getTitle() == null ? "" : mt.getTitle();
					// 发送内容
					String msg = "";
					// 内容类型
					String msgtypen = mt.getMsgtype() != null ? mt.getMsgtype().toString() : "";
					if(!msgtypen.equals("0")){
						if(!msgtypen.equals("4")){
							msg = MessageUtils.extractMessage("appmage", "appmage_javacode_text_1", request);
						}
					}else{ 
						msg = mt.getMsg();
					}

					if(msgtypen.equals("0"))
					{
						msgtypen = MessageUtils.extractMessage("appmage", "appmage_javacode_text_9", request);
					}
					else if(msgtypen.equals("1"))
					{
						msgtypen = MessageUtils.extractMessage("appmage", "appmage_javacode_text_10", request);
					}
					else if(msgtypen.equals("2"))
					{
						msgtypen = MessageUtils.extractMessage("appmage", "appmage_javacode_text_11", request);
					}
					else if(msgtypen.equals("3"))
					{
						msgtypen = MessageUtils.extractMessage("appmage", "appmage_javacode_text_12", request);
					}
					else
					{
						msgtypen = "--";
					}
					// 发送状态
					String sendState = mt.getSendstate().toString();
					if(sendState.equals("1"))
					{
						sendState = MessageUtils.extractMessage("appmage", "appmage_javacode_text_2", request);
					}
					else if(sendState.equals("2"))
					{
						sendState = MessageUtils.extractMessage("appmage", "appmage_javacode_text_3", request);
					}
					else
					{
						sendState = "--";
					}
					// 发送总数
					String subcount = mt.getSubcount() != null ? mt.getSubcount().toString() : "0";
					// 发送成功数
					String succount = mt.getSuccount() != null ? mt.getSuccount().toString() : "0";
					// 发送失败数
					String faicount =  mt.getFaicount() != null ? mt.getFaicount().toString() : "0";
					// 已读用户数
					String readcount = mt.getReadcount() != null ? mt.getReadcount().toString() : "0";
					// 未读用户数
					String unreadcount =  mt.getUnreadcount() != null ? mt.getUnreadcount().toString() : "0";
					// 操作员
					String username = mt.getUsername() != null ? mt.getUsername() : "";
					if(mt.getUserstate() == 2)
					{
						username = username + MessageUtils.extractMessage("appmage", "appmage_smstask_text_36", request);
					}
					row = sheet.createRow(k+1);
					for(int i =0;i<cell.length;i++){
						cell[i] = row.createCell(i);
						cell[i].setCellStyle(cellStyle);
					}
					cell[0].setCellValue(sendtime);
					cell[1].setCellValue(title);
					cell[2].setCellValue(msg);
					cell[3].setCellValue(msgtypen);
					cell[4].setCellValue(sendState);
					cell[5].setCellValue(subcount);
					cell[6].setCellValue(succount);
					cell[7].setCellValue(faicount);
					cell[8].setCellValue(readcount);
					cell[9].setCellValue(unreadcount);
					cell[10].setCellValue(username);
				}
				os = new FileOutputStream(voucherFilePath + File.separator + fileName);
				// 写入Excel对象
				workbook.write(os);
			}
			fileName = "APP" + sdf.format(curDate) + ".zip";
			filePath = BASEDIR + File.separator + voucherPath + File.separator
					+ "groupHistory" + File.separator + fileName;

			ZipUtil.compress(voucherFilePath, filePath);
            boolean status = deleteDir(fileTemp);
            if (!status) {
                EmpExecutionContext.error("刪除文件失敗！");
            }
		} catch (Exception e) {
			EmpExecutionContext.error(e,"APP发送任务导出出现异常");
		}finally{
			// 清除对象
			SysuserUtil.closeStream(in);
			SysuserUtil.closeStream(os);
			workbook = null;
		}
		resultMap.put("FILE_NAME", fileName);
		resultMap.put("FILE_PATH", filePath);
		return resultMap;
	}
	
	// 将设置单元格属性提取出来成一个方法，方便其他模块调用
	private XSSFCellStyle setCellStyle(XSSFWorkbook workbook) {
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
