package com.montnets.emp.biztype.biz;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import jxl.read.biff.BiffException;

import org.apache.commons.fileupload.FileItem;
import org.apache.cxf.common.util.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.EMPException;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.biztype.LfBusManager;
import com.montnets.emp.util.TxtFileUtil;

public class BitTypeUploadXlsBiz {

	/** 上传最大数 */
	private final static long OPT_UPLOAD_MAX = 1000;
	private final BaseBiz baseBiz = new BaseBiz();
	/** 模板文件路径xtgl/biztype/file/ */
	private final String excelPath = "xtgl/biztype/file/";
	/** 换行标识符 */
	final String line = "\r\n";

	public void uploadXlsType (HttpServletRequest request, Long userId, Long depId,
			String cropcode, Timestamp startTime, FileItem fileItem) throws Exception {
		/** 导入失败，一次最多导入1000条数据，已超过导入上限！ */
		String uploadGtMaxNumError = com.montnets.emp.i18n.util.MessageUtils
				.extractMessage("xtgl", "xtgl_cswh_ywlxgl_text5", request);
		// 必填项未填写完整
		String itemsIsEmptyError = com.montnets.emp.i18n.util.MessageUtils
				.extractMessage("xtgl", "xtgl_cswh_ywlxgl_text1", request);
		// 业务名称重复
		String nameIsRepeatError = com.montnets.emp.i18n.util.MessageUtils
				.extractMessage("xtgl", "xtgl_cswh_ywlxgl_text2", request);
		// 业务编码重复
		String codeIsRepeatError = com.montnets.emp.i18n.util.MessageUtils
				.extractMessage("xtgl", "xtgl_cswh_ywlxgl_text3", request);
		// 业务描述超过200字
		String busDescriptionIsRepeatError = com.montnets.emp.i18n.util.MessageUtils
				.extractMessage("xtgl", "xtgl_cswh_ywlxgl_text4", request);
		// 模板为空，请填写内容后继续导入！
		String tempIsEmptyError = com.montnets.emp.i18n.util.MessageUtils.extractMessage(
				"xtgl", "xtgl_cswh_ywlxgl_text6", request);
		// 业务名称超过32个字
		String nameGtNumberError = com.montnets.emp.i18n.util.MessageUtils
				.extractMessage("xtgl", "xtgl_cswh_ywlxgl_text11", request);
		// 业务编码超过32个字
		String codeGtNumberError = com.montnets.emp.i18n.util.MessageUtils
				.extractMessage("xtgl", "xtgl_cswh_ywlxgl_text12", request);
		// 第
		String erro8 = com.montnets.emp.i18n.util.MessageUtils.extractMessage("xtgl",
				"xtgl_spgl_shlcgl_d", request);
		// 行错误
		String erro9 = com.montnets.emp.i18n.util.MessageUtils.extractMessage("xtgl",
				"xtgl_cswh_ywlxgl_text13", request);
		// 业务编码只能是数字和字母
		String codeIsChineseError = com.montnets.emp.i18n.util.MessageUtils
				.extractMessage("xtgl", "xtgl_cswh_ywlxgl_text14", request);

		// 手动
		String manual = com.montnets.emp.i18n.util.MessageUtils.extractMessage("xtgl",
				"xtgl_cswh_ywlxgl_sd", request);
		// 触发
		String trigger = com.montnets.emp.i18n.util.MessageUtils.extractMessage("xtgl",
				"xtgl_cswh_ywlxgl_cf", request);
		// 手动+触发
		String manual_trigger = com.montnets.emp.i18n.util.MessageUtils.extractMessage(
				"xtgl", "xtgl_cswh_ywlxgl_sd_cf", request);
		/** 优先级 */
		String priority = com.montnets.emp.i18n.util.MessageUtils.extractMessage("xtgl",
				"xtgl_cswh_ywlxgl_wyxj", request);

		Date time = Calendar.getInstance().getTime();
		String[] url = this
				.getEmpUploadUrl((int) (Long.parseLong(userId + "") - 0), time);
		Connection conn = null;
		HSSFWorkbook workBook = null;
		// excel有效行数
		int rows = 0;
		// 导入成功条数
		int resultCount = 0;
		// 重复数
		int repeatNum = 0;
		StringBuffer contentSb = new StringBuffer();
		List<LfBusManager> lfBusManager = getListLfBus(cropcode);
		Iterator<LfBusManager> its = lfBusManager.iterator();
		// 将不能重复的机构名称和机构编码放入set的集合
		Set<String> typeName = new HashSet<String>();
		Set<String> typeCode = new HashSet<String>();
		while (its.hasNext()) {
			LfBusManager obj = its.next();
			typeName.add(obj.getBusName().toUpperCase());
			typeCode.add(obj.getBusCode().toUpperCase());
		}
		try {
			workBook = new HSSFWorkbook(fileItem.getInputStream());
			// 获得工作薄（Workbook）中工作表（Sheet）
			HSSFSheet sh = workBook.getSheetAt(0);
			for (int i = 2; i <= sh.getLastRowNum(); i++) {
				rows++;
			}
			List<LfBusManager> lfList = null;
			if (rows > OPT_UPLOAD_MAX) {
				request.setAttribute("upmax", uploadGtMaxNumError);
			}
			else if (rows <= 0) {
				request.setAttribute("upmax", tempIsEmptyError);
			}
			else {

				for (int k = 2; k <= rows + 1; k++) {
					if (lfList == null) {
						lfList = new ArrayList<LfBusManager>();
					}
					LfBusManager lfManager = new LfBusManager();
					// 获取某一行的所有单元格
					HSSFRow cells = sh.getRow(k);
					if (cells == null) {
						contentSb.append(
								erro8 + (k + 1) + erro9 + "  " + itemsIsEmptyError)
								.append(line);
						repeatNum++;
						continue;
					}
					// 获取员工的业务名称
					String busName = getCellValueHs(cells.getCell(0)).toUpperCase();
					if (StringUtils.isEmpty(busName)) {
						contentSb.append(
								erro8 + (k + 1) + erro9 + "  " + itemsIsEmptyError)
								.append(line);
						repeatNum++;
						continue;
					}
					else {
						if (busName.length() > 32) {
							contentSb.append(
									erro8 + (k + 1) + erro9 + "  " + nameGtNumberError)
									.append(line);
							repeatNum++;
							continue;
						}
						// 业务名称进行过滤
						if (typeName.contains(busName)) {
							contentSb.append(
									erro8 + (k + 1) + erro9 + "  " + nameIsRepeatError)
									.append(line);
							repeatNum++;
							continue;
						}
						lfManager.setBusName(busName);

					}
					// 获取员工的业务编码
					String busCode = getCellValueHs(cells.getCell(1)).toUpperCase();
					if (StringUtils.isEmpty(busCode)) {
						contentSb.append(
								erro8 + (k + 1) + erro9 + "  " + itemsIsEmptyError)
								.append(line);
						repeatNum++;
						continue;

					}
					else {
						// 业务编码进行过滤
						if (typeCode.contains(busCode)) {
							contentSb.append(
									erro8 + (k + 1) + erro9 + "  " + codeIsRepeatError)
									.append(line);
							repeatNum++;
							continue;
						}
						if (isContainChinese(busCode)) {
							contentSb.append(
									erro8 + (k + 1) + erro9 + "  " + codeIsChineseError)
									.append(line);
							repeatNum++;
							continue;
						}
						if (busCode.length() > 32) {
							contentSb.append(
									erro8 + (k + 1) + erro9 + "  " + codeGtNumberError)
									.append(line);
							repeatNum++;
							continue;
						}
						lfManager.setBusCode(busCode);

					}

					// 获取员工的业务类型
					String busType = getCellValueHs(cells.getCell(2));
					if (StringUtils.isEmpty(busType)) {
						contentSb.append(
								erro8 + (k + 1) + erro9 + "  " + itemsIsEmptyError)
								.append(line);
						repeatNum++;
						continue;
					}
					else {
						if (manual.equals(busType)) {
							lfManager.setBusType(0);
						}
						else if (trigger.equals(busType)) {
							lfManager.setBusType(1);
						}
						else if (manual_trigger.equals(busType)) {
							lfManager.setBusType(2);
						}
					}
					// 获取员工的优先级别
					String riseLevel = getCellValueHs(cells.getCell(3));
					if (StringUtils.isEmpty(riseLevel)) {
						lfManager.setRiseLevel(-99);
					}
					else {
						if (priority.equals(riseLevel)) {
							lfManager.setRiseLevel(-99);
						}
						else {
							lfManager.setRiseLevel(Integer.parseInt(riseLevel.substring(
									0, 1)));
						}

					}
					// 获取员工的业务描述
					String busDescription = getCellValueHs(cells.getCell(4));
					if (StringUtils.isEmpty(busDescription)) {
						lfManager.setBusDescription("");
					}
					else if (busDescription.length() <= 200) {
						lfManager.setBusDescription(busDescription);
					}
					else {
						contentSb.append(
								erro8 + (k + 1) + erro9 + "  "
										+ busDescriptionIsRepeatError).append(line);
						repeatNum++;
						continue;
					}
					lfManager.setUpdateTime(startTime);
					lfManager.setCreateTime(startTime);
					// 获取当前登录的员工
					lfManager.setUserId(userId);
					// 获取当前登录的员工的所在机构
					lfManager.setCorpCode(cropcode);
					lfManager.setDepId(depId);
					lfManager.setState(0);
					lfList.add(lfManager);
					// 如果添加成功则将set中添加该字段 ，如此确保文件中相同数据的去重
					typeName.add(busName);
					typeCode.add(busCode);
				}
				if (lfList != null && lfList.size() > 0) {
					conn = baseBiz.getConnection();
					baseBiz.beginTransaction(conn);
					// 返回的成功的条数
					resultCount += baseBiz.addList(conn, LfBusManager.class, lfList);
				}
				if (resultCount == 0) {
					request.setAttribute("result", resultCount + "");
				}
				else {
					baseBiz.commitTransaction(conn);
				}
				String pathTemp = extractTXT(url, contentSb);
				request.setAttribute("path", pathTemp);
				request.setAttribute("successNum", resultCount);
				request.setAttribute("errorNum", repeatNum);
			}

		} catch (BiffException e) {
			EmpExecutionContext.error(e, "上传员工文件出现异常！");
			request.setAttribute("result", "false");
			if (conn != null)
				baseBiz.rollBackTransaction(conn);
		}
		finally {
			if (conn != null && !conn.isClosed()) {
				baseBiz.closeConnection(conn);
			}
			if (fileItem != null) {
				fileItem.delete();
			}

		}

	}

	public void uploadXlsxType (HttpServletRequest request, Long userId, Long depId,
			String cropcode, Timestamp startTime, FileItem fileItem) throws Exception,
			IOException, SQLException {
		String uploadGtMaxNumError = com.montnets.emp.i18n.util.MessageUtils
				.extractMessage("xtgl", "xtgl_cswh_ywlxgl_text5", request);
		String itemsIsEmptyError = com.montnets.emp.i18n.util.MessageUtils
				.extractMessage("xtgl", "xtgl_cswh_ywlxgl_text1", request);
		String nameIsRepeatError = com.montnets.emp.i18n.util.MessageUtils
				.extractMessage("xtgl", "xtgl_cswh_ywlxgl_text2", request);
		String codeIsRepeatError = com.montnets.emp.i18n.util.MessageUtils
				.extractMessage("xtgl", "xtgl_cswh_ywlxgl_text3", request);
		String busDescriptionIsRepeatError = com.montnets.emp.i18n.util.MessageUtils
				.extractMessage("xtgl", "xtgl_cswh_ywlxgl_text4", request);
		String tempIsEmptyError = com.montnets.emp.i18n.util.MessageUtils.extractMessage(
				"xtgl", "xtgl_cswh_ywlxgl_text6", request);
		// 业务名称超过32个字
		String nameGtNumberError = com.montnets.emp.i18n.util.MessageUtils
				.extractMessage("xtgl", "xtgl_cswh_ywlxgl_text11", request);
		// 业务编码超过32个字
		String codeGtNumberError = com.montnets.emp.i18n.util.MessageUtils
				.extractMessage("xtgl", "xtgl_cswh_ywlxgl_text12", request);
		String erro8 = com.montnets.emp.i18n.util.MessageUtils.extractMessage("xtgl",
				"xtgl_spgl_shlcgl_d", request);
		String erro9 = com.montnets.emp.i18n.util.MessageUtils.extractMessage("xtgl",
				"xtgl_cswh_ywlxgl_text13", request);
		// 业务编码只能是数字和字母
		String codeIsChineseError = com.montnets.emp.i18n.util.MessageUtils
				.extractMessage("xtgl", "xtgl_cswh_ywlxgl_text14", request);
		// 手动
		String manual = com.montnets.emp.i18n.util.MessageUtils.extractMessage("xtgl",
				"xtgl_cswh_ywlxgl_sd", request);
		// 触发
		String trigger = com.montnets.emp.i18n.util.MessageUtils.extractMessage("xtgl",
				"xtgl_cswh_ywlxgl_cf", request);
		// 手动+触发
		String manual_trigger = com.montnets.emp.i18n.util.MessageUtils.extractMessage(
				"xtgl", "xtgl_cswh_ywlxgl_sd_cf", request);
		String priority = com.montnets.emp.i18n.util.MessageUtils.extractMessage("xtgl",
				"xtgl_cswh_ywlxgl_wyxj", request);

		Date time = Calendar.getInstance().getTime();
		String[] url = getEmpUploadUrl((int) (Long.parseLong(userId + "") - 0), time);
		Connection conn = null;
		XSSFWorkbook xssfWorkbook = null;
		// excel有效行数
		int rows = 0;
		// 导入成功条数
		int resultCount = 0;
		// 重复数
		int repeatNum = 0;
		StringBuffer contentSb = new StringBuffer();
		List<LfBusManager> lfBusManager = getListLfBus(cropcode);
		Iterator<LfBusManager> its = lfBusManager.iterator();
		// 将不能重复的机构名称和机构编码放入set的集合
		Set<String> typeName = new HashSet<String>();
		Set<String> typeCode = new HashSet<String>();
		/*
		 * typeName.add("默认业务"); typeCode.add();
		 */
		while (its.hasNext()) {
			LfBusManager obj = its.next();
			typeName.add(obj.getBusName().toUpperCase());
			typeCode.add(obj.getBusCode().toUpperCase());
		}
		// 获得工作薄（Workbook）
		try {
			xssfWorkbook = new XSSFWorkbook(fileItem.getInputStream());
			// 获得工作薄（Workbook）中工作表（Sheet）
			XSSFSheet sh = xssfWorkbook.getSheetAt(0);
			for (int i = 2; i <= sh.getLastRowNum(); i++) {
				// 获取某一行的所有单元格
				rows++;
			}
			List<LfBusManager> lfList = null;
			if (rows > OPT_UPLOAD_MAX) {
				request.setAttribute("upmax", uploadGtMaxNumError);
			}
			else if (rows <= 0) {
				request.setAttribute("upmax", tempIsEmptyError);
			}
			else {
				for (int k = 2; k <= rows + 1; k++) {
					if (lfList == null) {
						lfList = new ArrayList<LfBusManager>();
					}
					LfBusManager lfManager = new LfBusManager();
					// 获取某一行的所有单元格
					XSSFRow cells = sh.getRow(k);
					if (cells == null) {
						contentSb.append(
								erro8 + (k + 1) + erro9 + "  " + itemsIsEmptyError)
								.append(line);
						repeatNum++;
						continue;
					}
					// 获取员工的业务名称
					String busName = getCellValue(cells.getCell(0)).toUpperCase();
					if (StringUtils.isEmpty(busName)) {
						contentSb.append(
								erro8 + (k + 1) + erro9 + "  " + itemsIsEmptyError)
								.append(line);
						repeatNum++;
						continue;
					}
					else {
						// 业务名称进行过滤
						if (typeName.contains(busName)) {
							contentSb.append(
									erro8 + (k + 1) + erro9 + "  " + nameIsRepeatError)
									.append(line);
							repeatNum++;
							continue;
						}
						if (busName.length() > 32) {
							contentSb.append(
									erro8 + (k + 1) + erro9 + "  " + nameGtNumberError)
									.append(line);
							repeatNum++;
							continue;
						}
						lfManager.setBusName(busName);
					}
					// 获取员工的业务编码
					String busCode = getCellValue(cells.getCell(1)).toUpperCase();
					if (StringUtils.isEmpty(busCode)) {
						contentSb.append(
								erro8 + (k + 1) + erro9 + "  " + itemsIsEmptyError)
								.append(line);
						repeatNum++;
						continue;
					}
					else {
						// 业务编码进行过滤
						if (typeCode.contains(busCode)) {
							contentSb.append(
									erro8 + (k + 1) + erro9 + "  " + codeIsRepeatError)
									.append(line);
							repeatNum++;
							continue;

						}
						if (isContainChinese(busCode)) {
							contentSb.append(
									erro8 + (k + 1) + erro9 + "  " + codeIsChineseError)
									.append(line);
							repeatNum++;
							continue;
						}
						if (busCode.length() > 32) {
							contentSb.append(
									erro8 + (k + 1) + erro9 + "  " + codeGtNumberError)
									.append(line);
							repeatNum++;
							continue;
						}
						lfManager.setBusCode(busCode);

					}
					// 获取员工的业务类型
					String busType = getCellValue(cells.getCell(2));
					if (StringUtils.isEmpty(busType)) {
						contentSb.append(
								erro8 + (k + 1) + erro9 + "  " + itemsIsEmptyError)
								.append(line);
						repeatNum++;
						continue;
					}
					else {
						if (manual.equals(busType)) {
							lfManager.setBusType(0);
						}
						else if (trigger.equals(busType)) {
							lfManager.setBusType(1);
						}
						else if (manual_trigger.equals(busType)) {
							lfManager.setBusType(2);
						}

					}
					// 获取员工的优先级别，
					String riseLevel = getCellValue(cells.getCell(3));
					if (StringUtils.isEmpty(riseLevel)) {
						lfManager.setRiseLevel(-99);
					}
					else {
						if (priority.equals(riseLevel)) {
							lfManager.setRiseLevel(-99);
						}
						else {
							lfManager.setRiseLevel(Integer.parseInt(riseLevel.substring(
									0, 1)));
						}

					}
					// 获取员工的业务描述，字数不能大于200
					String busDescription = getCellValue(cells.getCell(4));
					if (StringUtils.isEmpty(busDescription)) {
						lfManager.setBusDescription("");
					}
					else if (busDescription.length() <= 200) {
						lfManager.setBusDescription(busDescription);
					}
					else {
						contentSb.append(
								erro8 + (k + 1) + erro9 + "  "
										+ busDescriptionIsRepeatError).append(line);
						repeatNum++;
						continue;
					}
					lfManager.setBusDescription(busDescription);
					lfManager.setUpdateTime(startTime);
					lfManager.setCreateTime(startTime);
					// 获取当前登录的员工
					lfManager.setUserId(userId);
					// 获取当前登录的员工的所在机构
					lfManager.setCorpCode(cropcode);
					lfManager.setDepId(depId);
					lfManager.setState(0);
					lfList.add(lfManager);
					typeName.add(busName);
					typeCode.add(busCode);
				}

				if (lfList != null && lfList.size() > 0) {
					conn = baseBiz.getConnection();
					baseBiz.beginTransaction(conn);
					resultCount += baseBiz.addList(conn, LfBusManager.class, lfList);
				}

				if (resultCount == 0) {
					request.setAttribute("result", "noRecord");
				}
				else {
					baseBiz.commitTransaction(conn);
					request.setAttribute("result", "upload:" + resultCount);
				}
				String pathTemp = extractTXT(url, contentSb);
				request.setAttribute("path", pathTemp);
				request.setAttribute("successNum", resultCount);
				request.setAttribute("errorNum", repeatNum);
			}
		} catch (BiffException e) {
			EmpExecutionContext.error(e, "上传员工文件出现异常！");
			request.setAttribute("result", "false");
			if (conn != null)
				baseBiz.rollBackTransaction(conn);
		}
		finally {
			if (conn != null && !conn.isClosed()) {
				baseBiz.closeConnection(conn);
			}
			if (fileItem != null) {
				fileItem.delete();
			}

		}
	}

	/**
	 * 对Excel的各个单元格的格式进行判断并转换
	 */
	private String getCellValue (XSSFCell xssfCell) {
		String cellValue = "";
		DecimalFormat df = new DecimalFormat("0");
		if (xssfCell != null) {
			switch (xssfCell.getCellType()) {
				case HSSFCell.CELL_TYPE_STRING:
					cellValue = xssfCell.getRichStringCellValue().getString().trim();
					break;
				case HSSFCell.CELL_TYPE_NUMERIC:
					cellValue = df.format(xssfCell.getNumericCellValue()).trim();
					break;
				case HSSFCell.CELL_TYPE_BOOLEAN:
					cellValue = String.valueOf(xssfCell.getBooleanCellValue()).trim();
					break;
				case HSSFCell.CELL_TYPE_FORMULA:
					cellValue = xssfCell.getCellFormula().trim();
					break;
				default:
					cellValue = "";
			}
		}
		else {
			cellValue = "";
		}
		return cellValue;
	}
	private String getCellValueHs (HSSFCell xssfCell) {
		String cellValue = "";
		DecimalFormat df = new DecimalFormat("0");
		if (xssfCell != null) {
			switch (xssfCell.getCellType()) {
				case HSSFCell.CELL_TYPE_STRING:
					cellValue = xssfCell.getRichStringCellValue().getString().trim();
					break;
				case HSSFCell.CELL_TYPE_NUMERIC:
					cellValue = df.format(xssfCell.getNumericCellValue()).trim();
					break;
				case HSSFCell.CELL_TYPE_BOOLEAN:
					cellValue = String.valueOf(xssfCell.getBooleanCellValue()).trim();
					break;
				case HSSFCell.CELL_TYPE_FORMULA:
					cellValue = xssfCell.getCellFormula().trim();
					break;
				default:
					cellValue = "";
			}
		}
		else {
			cellValue = "";
		}
		return cellValue;
	}
	/**
	 * 生成文件的路径名
	 * 
	 * @param id
	 * @param time
	 * @return
	 */
	public String[] getEmpUploadUrl (int id, Date time) {
		String uploadPath = excelPath + "upload/";
		// 存放路径的数组
		String[] url = new String[5];
		String saveName = id + "_"
				+ (new SimpleDateFormat("yyyyMMddHHmmssS")).format(time) + ".txt";
		String logicUrl;
		String physicsUrl = new TxtFileUtil().getWebRoot();
		physicsUrl = physicsUrl + uploadPath + saveName;
		logicUrl = uploadPath + saveName;
		url[0] = physicsUrl;
		url[1] = logicUrl;
		return url;
	}
	/**
	 * 数字字母的验证
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isContainChinese (String str) {
		Pattern p = Pattern.compile("^[A-Za-z0-9]+$");
		Matcher m = p.matcher(str);
		if (m.matches()) {
			return false;
		}
		return true;
	}
	/**
	 * 将错误码生成文件
	 * 
	 * @param url
	 * @param contentSb
	 * @return
	 * @throws EMPException
	 */
	private String extractTXT (String[] url, StringBuffer contentSb) throws EMPException {
		String FileStrTemp = url[0].substring(0, url[0].indexOf(".txt")) + "_emp"
				+ ".txt";
		String pathTemp = url[1].substring(0, url[1].indexOf(".txt")) + "_emp" + ".txt";
		TxtFileUtil txtFileUtil = new TxtFileUtil();
		txtFileUtil.writeToTxtFile(FileStrTemp, contentSb.toString());
		return pathTemp;
	}
	/**
	 * 获取所有业务类型
	 * 
	 * @param cropcode
	 *            企业编码
	 * @return
	 * @throws Exception
	 */
	private List<LfBusManager> getListLfBus (String cropcode) throws Exception {
		// 获取所有公司业务名称数据
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
		conditionMap.put("corpCode&in", "0,1,2," + cropcode);
		orderbyMap.put("busId", StaticValue.ASC);
		List<LfBusManager> lfBusManager = baseBiz.getByCondition(LfBusManager.class,
				conditionMap, orderbyMap);
		return lfBusManager;
	}
}
