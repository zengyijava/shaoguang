package com.montnets.emp.perfect.biz;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
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

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.perfect.LfPerfectNoticUp;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.ZipUtil;

public class PrefectExcel{

	// 报表模板目录
	protected String BASEDIR = "";

	// 操作员报表文件路径
	protected String voucherTemplatePath = "";

	// 发送账号下行报表文件路径
	protected String spvoucherTemplatePath = "";

	// 产生下行报表路径
	//public String voucherPath = "voucher";
	protected final String voucherPath = "download";

	protected final String SAMPLES = "samples";

	protected final String TEMP = "temp";

	private PrefectNoticeBiz prefectNoticeBiz;


	public PrefectExcel(String context) {
		BASEDIR = context;
		prefectNoticeBiz = new PrefectNoticeBiz();
	}
	/**
	 * 完美通知 导出
	 * @param userName
	 * @param phone
	 * @param remsg
	 * @param isReAttr
	 * @param isGeAttr
	 * @param sendCount
	 * @param pageInfo
	 * @return
	 */
	public Map<String, String> createPrefectExcel(String userName, String phone, String remsg, Long taskid, String isReAttr, String isGeAttr, String sendCount, PageInfo pageInfo, Integer isPhoneHide, HttpServletRequest request) {
		voucherTemplatePath = BASEDIR + File.separator + TEMP
				+ File.separator + "per_prefect_"+ MessageUtils.extractMessage("common","common_empLangName",request)+".xlsx";// excel模板地址

		Map<String, String> resultMap = new HashMap<String, String>();

		String filePath = null;
		String fileName = null;// 文件名

		// int size = 1; // 生成的工作薄个数
		// EmpExecutionContext.info("工作表个数：" + size);

		XSSFWorkbook workbook = null;

		try {
			// 设置每个excel文件的行数
			pageInfo.setPageSize(500000);
			List<LfPerfectNoticUp> noticUps = prefectNoticeBiz.getPerfectNoticeUpByNoticeId(userName, phone, remsg,
					taskid, isReAttr, isGeAttr, sendCount, pageInfo);
			if (noticUps == null || noticUps.size() == 0) {
				return null;
			}
			// 计算出文件数
			int fileCount = pageInfo.getTotalPage();
			Date curDate = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddH24mmss");
			// 文件的存储路径
			String voucherFilePath = BASEDIR + File.separator + voucherPath
					+ File.separator + "perfect" + File.separator
					+ sdf.format(curDate);
			File file1 = new File(voucherFilePath);
			file1.mkdirs();
			int curIndex = 0;// 当前clientvosList的索引
			File file = new File(voucherTemplatePath);
			/*
			 * if (!file.exists()) { file.mkdirs(); }
			 */
			for (int f = 0; f < fileCount; f++) {
				fileName = "perfect_" + sdf.format(curDate) + "_[" + (f+1)+ "].xlsx";
				// 读取模板
				InputStream in = new FileInputStream(file);
				// 工作表
				workbook = new XSSFWorkbook(in);
				// 表格样式
				XSSFCellStyle cellStyle = setCellStyle(workbook);

				XSSFSheet sheet = workbook.cloneSheet(0);
				workbook.removeSheetAt(0);
				/*完美通知*/
				workbook.setSheetName(0, MessageUtils.extractMessage("dxzs","dxzs_common_perfectNotice",request));
				// 读取模板工作表
				XSSFRow row = null;
				EmpExecutionContext.debug("完美通知详情导出，工作薄创建与模板移除成功!");

				if (f > 0)// 第一次时不需要再查询，因为前面已经查询出第一页
				{
					noticUps = prefectNoticeBiz.getPerfectNoticeUpByNoticeId(
							userName, phone, taskid, pageInfo);
				}

				pageInfo.setPageIndex(f + 2);// 定位下一页

				XSSFCell[] cell = new XSSFCell[7];

				for (int k = 0; k < noticUps.size(); k++) {

					LfPerfectNoticUp mvo = (LfPerfectNoticUp) noticUps.get(k);

					// 接收人
					String recieveName = mvo.getName().toString() == null ? "-"
							: ("".equals(mvo.getName()) ? "-" : mvo.getName());
					// 回复状态
					String isReply = null;

					if (mvo.getIsReply() == 0) {
						/*已回复*/
						isReply = MessageUtils.extractMessage("dxzs","dxzs_xtnrqf_title_122",request);
					} else if (mvo.getIsReply() == 1) {
						/*未回复*/
						isReply = MessageUtils.extractMessage("dxzs","dxzs_xtnrqf_title_123",request);
					} else {
						isReply = "-";
					}
					// 回执状态
					String receiveState = null;
					if (mvo.getIsReceive() == 0) {
						if("4".equals(mvo.getIsAtrred())){
							/*接收失败*/
							receiveState = MessageUtils.extractMessage("dxzs","dxzs_xtnrqf_title_180",request);
						}
						else{
							/*已接收*/
							receiveState = MessageUtils.extractMessage("dxzs","dxzs_xtnrqf_title_125",request);
						}
					} else if (mvo.getIsReceive() == 1) {
						/*未接收*/
						receiveState = MessageUtils.extractMessage("dxzs","dxzs_xtnrqf_title_126",request);
					} else if (mvo.getIsReceive() == 2) {
						/*未发送成功*/
						receiveState = MessageUtils.extractMessage("dxzs","dxzs_xtnrqf_title_127",request);
					} else if (mvo.getIsReceive() == 3) {
						/*手机号异常*/
						receiveState = MessageUtils.extractMessage("dxzs","dxzs_errorInfo_text_1",request);
					} else if (mvo.getIsReceive() == 4) {
						/*手机号为空*/
						receiveState = MessageUtils.extractMessage("dxzs","dxzs_errorInfo_text_2",request);
					} else if (mvo.getIsReceive() == 6) {
						/*短信余额不足*/
						receiveState = MessageUtils.extractMessage("dxzs","dxzs_errorInfo_text_3",request);
					} else if (mvo.getIsReceive() == 7) {
						/*短信扣费失败*/
						receiveState = MessageUtils.extractMessage("dxzs","dxzs_errorInfo_text_4",request);
					} else if (mvo.getIsReceive() == 8) {
						/*运营商不符合*/
						receiveState = MessageUtils.extractMessage("dxzs","dxzs_errorInfo_text_5",request);
					} else if (mvo.getIsReceive() == 9) {
						/*所属机构未充值*/
						receiveState = MessageUtils.extractMessage("dxzs","dxzs_errorInfo_text_6",request);
					} else if (mvo.getIsReceive() == -2) {
						/*号码重复*/
						receiveState = MessageUtils.extractMessage("dxzs","dxzs_errorInfo_text_7",request);
					} else if (mvo.getIsReceive() == -1) {
						/*已接收*/
						receiveState = MessageUtils.extractMessage("dxzs","dxzs_xtnrqf_title_125",request);
					} else if (mvo.getIsReceive() == 5) {
						/*黑名单*/
						receiveState = MessageUtils.extractMessage("dxzs","dxzs_errorInfo_text_8",request);
					} else if (mvo.getIsReceive() == -4) {
						/*重复人员*/
						receiveState = MessageUtils.extractMessage("dxzs","dxzs_errorInfo_text_9",request);
					} else {
						/*出现异常*/
						receiveState = MessageUtils.extractMessage("dxzs","dxzs_errorInfo_text_10",request);
					}
					// 已发次数
					String receiveCount = null;
					if (mvo.getIsReply() == 2) {
						receiveCount = "-";
					} else {
						/*次*/
						receiveCount = mvo.getReceiveCount() + MessageUtils.extractMessage("dxzs","dxzs_xtnrqf_title_115",request);
					}
					// 回复时间
					String sendTime = null;
					Timestamp time = mvo.getSendTime();
					SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					if (time != null) {
						sendTime = sdfTime.format(mvo.getSendTime());
					} else {
						sendTime = "-";
					}
					// 回复内容
					String receiveMsg = mvo.getContent();
					if (receiveMsg == null || "".equals(receiveMsg)) {
						receiveMsg = "-";
					}

					// 手机号码
					String mobile = mvo.getMobile();
//						mvo.getMobile() == null ? "-" : (""
//							.equals(mvo.getMobile()) ? "-" : mvo.getMobile());
					//如果号码隐藏
					if(isPhoneHide-1 != 0){
						Integer len = mobile.length();
						if(len == 11){
							mobile = mobile.substring(0,3)+ "*****" + mobile.substring(len-3,len);
						}else{
							mobile="-";
						}
					}
					// 所属部门
					// String depName = mvo.getDepName();
					row = sheet.createRow(k + 1);
					// 生成四个单元格
					cell[0] = row.createCell(0);
					cell[1] = row.createCell(1);
					cell[2] = row.createCell(2);
					cell[3] = row.createCell(3);
					cell[4] = row.createCell(4);
					cell[5] = row.createCell(5);
					cell[6] = row.createCell(6);

					// 设置单元格样式
					cell[0].setCellStyle(cellStyle);
					cell[1].setCellStyle(cellStyle);
					cell[2].setCellStyle(cellStyle);
					cell[3].setCellStyle(cellStyle);
					cell[4].setCellStyle(cellStyle);
					cell[5].setCellStyle(cellStyle);
					cell[6].setCellStyle(cellStyle);


					// 设置单元格内容
					cell[0].setCellValue(recieveName);
					cell[1].setCellValue(mobile);
					cell[2].setCellValue(receiveCount);
					cell[3].setCellValue(receiveState);
					cell[4].setCellValue(isReply);
					cell[5].setCellValue(sendTime);
					cell[6].setCellValue(receiveMsg);

					curIndex++;// clientvosList索引加1
				}

				// 输出到xlsx文件
				OutputStream os = new FileOutputStream(voucherFilePath
						+ File.separator + fileName);
				// 写入Excel对象
				workbook.write(os);
				// 清除对象
				os.close();
				in.close();
				workbook = null;

				noticUps.clear();
				noticUps = null;
			}
			/*完美通知*/
			fileName = MessageUtils.extractMessage("dxzs","dxzs_common_perfectNotice",request) + sdf.format(curDate) + ".zip";
			filePath = BASEDIR + File.separator + voucherPath + File.separator
					+ "perfect" + File.separator + fileName;

			ZipUtil.compress(voucherFilePath, filePath);
			deleteDir(file1);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"完美通知详情导出出现异常！");
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
