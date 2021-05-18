package com.montnets.emp.sysuser.biz;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.FontUnderline;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.sysuser.dao.DepExportDao;
import com.montnets.emp.util.FileUtils;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.ZipUtil;

public class DepExport extends BaseServlet {

	private static final long serialVersionUID = 1L;
	private static final BaseBiz baseBiz = new BaseBiz();
	private static final DepExportDao exportDao = new DepExportDao();
	/** 导出的最大数量 */
	private static final int MAX_EXPORT_NUM = 500000;

	public Map<String, String> createDepExcel (String excelPath, String corpCode,
			Long depId, PageInfo pageInfo, HttpServletRequest request) {
		HttpSession session = request.getSession();
		String langName = (String) session.getAttribute(StaticValue.LANG_KEY);
		String voucherTemplatePath = excelPath + File.separator + "temp" + File.separator
				+ "Dep_Organization_" + langName + ".xlsx";
		String voucherPath = "voucher";

		Map<String, String> resultMap = new HashMap<String, String>();
		List<LfDep> lfList = new ArrayList<LfDep>();
		// 设置每个book(sheet)的行数
		pageInfo.setPageSize(MAX_EXPORT_NUM);
		try {
			lfList = findByDepID(corpCode, depId, pageInfo);
		} catch (Exception e1) {
			EmpExecutionContext.error(e1, "获取机构列表信息异常！");
		}

		if (lfList == null || lfList.size() == 0) {
			return null;
		}

		// 计算出文件数
		int size = pageInfo.getTotalPage();
		if (size == 0) {
			EmpExecutionContext.info("无机构数据！");
		}
		// 国际化
		String oper = com.montnets.emp.i18n.util.MessageUtils.extractMessage("user",
				"user_xtgl_czygl_text_51", request);
		String optdeptCode = com.montnets.emp.i18n.util.MessageUtils.extractMessage(
				"user", "user_xtgl_czygl_text_138", request);
		String optdeptName = com.montnets.emp.i18n.util.MessageUtils.extractMessage(
				"user", "user_xtgl_czygl_text_126", request);
		String optdeptResp = com.montnets.emp.i18n.util.MessageUtils.extractMessage(
				"user", "user_xtgl_czygl_text_127", request);

		// 产生报表文件的存储路径
		Date curDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddH24mmss");
		String voucherFilePath = excelPath + File.separator + voucherPath
				+ File.separator + "Dep_Organization" + File.separator
				+ sdf.format(curDate);
		File fileTemp = new File(voucherFilePath);
		if (!fileTemp.exists()) {
			fileTemp.mkdirs();
		}
		String filePath = null;
		String fileName = "";

		XSSFWorkbook workbook = null;
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
				font2.setBold(false);
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
				XSSFCell[] cell = new XSSFCell[8];

				xsheet.setDefaultColumnWidth(20);
				// 创建一个Excel的单元格
				cell[0] = xrow.createCell(0);
				cell[1] = xrow.createCell(1);
				cell[2] = xrow.createCell(2);

				// 设置单元格样式
				cell[0].setCellStyle(cellStyle2);
				cell[1].setCellStyle(cellStyle2);
				cell[2].setCellStyle(cellStyle2);

				// 给Excel的单元格设置样式和赋值
				cell[0].setCellValue(optdeptCode);
				cell[1].setCellValue(optdeptName);
				cell[2].setCellValue(optdeptResp);

				FileOutputStream xos = new FileOutputStream(voucherTemplatePath);
				wb.write(xos);
				if (xos != null) {
					xos.close();
				}
			}

			for (int f = 0; f < size; f++) {
				// 报表文件名
				fileName = "Dep_Organization" + sdf.format(curDate) + "[" + (f + 1)
						+ "]_" + StaticValue.getServerNumber() + ".xlsx";
				InputStream in = new FileInputStream(file);
				// 工作表
				workbook = new XSSFWorkbook(in);
				// 表格样式
				XSSFCellStyle cellStyle = setCellStyle(workbook);

				XSSFSheet sheet = workbook.cloneSheet(0);
				workbook.removeSheetAt(0);
				workbook.setSheetName(0, oper);
				// 读取模板工作表
				XSSFRow row = null;
				XSSFCell[] cell = new XSSFCell[3];

				if (f > 0)// 第一次时不需要再查询，因为前面已经查询出第一页
				{
					lfList = findByDepID(corpCode, depId, pageInfo);
				}

				pageInfo.setPageIndex(f + 2);// 定位下一页
				for (int k = 0; k < lfList.size(); k++) {

					LfDep Lf = lfList.get(k);
					// 机构编码
					String depCodeThird = Lf.getDepCodeThird();
					// 机构名称
					String depName = Lf.getDepName();
					// 机构职责
					String depResp = Lf.getDepResp();
					row = sheet.createRow(k + 1);

					cell[0] = row.createCell(0);
					cell[1] = row.createCell(1);
					cell[2] = row.createCell(2);
					// cell[3] = row.createCell(3);

					cell[0].setCellStyle(cellStyle);
					cell[1].setCellStyle(cellStyle);
					cell[2].setCellStyle(cellStyle);
					// cell[3].setCellStyle(cellStyle);

					cell[0].setCellValue(depCodeThird);
					cell[1].setCellValue(depName);
					cell[2].setCellValue(depResp);
				}
				OutputStream os = new FileOutputStream(voucherFilePath + File.separator
						+ fileName);
				// 写入Excel对象
				workbook.write(os);
				if (os != null) {
					os.close();
				}
				if (in != null) {
					in.close();
				}
				workbook = null;
			}

			fileName = oper + sdf.format(curDate) + ".zip";
			filePath = excelPath + File.separator + voucherPath + File.separator
					+ "Dep_Organization" + File.separator + fileName;
			ZipUtil.compress(voucherFilePath, filePath);
			boolean flag = FileUtils.deleteDir(fileTemp);
			if (!flag) {
				EmpExecutionContext.error("刪除文件失敗！");
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "导出机构信息失败！");
		}

		resultMap.put("FILE_NAME", fileName);
		resultMap.put("FILE_PATH", filePath);
		return resultMap;
	}
	// 将设置单元格属性提取出来成一个方法，方便其他模块调用

	public XSSFCellStyle setCellStyle (XSSFWorkbook workbook) {
		XSSFCellStyle cellStyle = workbook.createCellStyle();

		XSSFFont font = workbook.createFont();
		// 字体名称
		font.setFontName("宋体");
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
	/***
	 * 通过部门id查询出需要导出数据
	 * 
	 * @param corpCode
	 * @param depId
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<LfDep> findByDepID (String corpCode, Long depId, PageInfo pageInfo)
			throws Exception {
		List<LfDep> lfDepList;
		LfDep lfDepPath = baseBiz.getById(LfDep.class, depId);
		if (lfDepPath != null) {
			lfDepList = exportDao.findByCoreCodeAndDepPathLike(corpCode,
					lfDepPath.getDeppath(), pageInfo);
		}
		else {
			lfDepList = null;
		}
		return lfDepList;
	}

}
