package com.montnets.emp.sysuser.biz;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.sysuser.bean.OptUpload;

public class DepUploadXls {

	/** 机构管理上传最大数量 */
	private final static long OPT_UPLOAD_MAXNUM = 1000;
	/** 顶级机构的机构编码 */
	private final static String ROOT_DEP_CODE = "0000000000000000";
	/** 文件中定义上一级机构编码为 空 */
	private final static String UPPER_LEVEL_DEP_CODE = "空";
	BaseBiz baseBiz = new BaseBiz();
	private final DepPriBiz depPriBiz = new DepPriBiz();

	/**
	 * 上传.xls的文件类型
	 * 
	 * @param request
	 * @param deppath
	 *            当前登录管理员所在部门的部门路径
	 * @param opt
	 *            记录上传成功数，失败数，总数的类
	 * @param erroMap
	 *            记录错误的集合
	 * @param loginSysuser
	 *            当前登录的管理员
	 * @param fileItem
	 * @throws Exception
	 */
	public void depUploadXls (HttpServletRequest request, String deppath, OptUpload opt,
			Map<Integer, String> erroMap, LfSysuser loginSysuser, FileItem fileItem)
			throws Exception {
		// 机构编码为空
		String codeIsEmptyError = com.montnets.emp.i18n.util.MessageUtils.extractMessage(
				"user", "user_xtgl_czygl_text_169", request);
		// 机构名称为空
		String nameIsEmptyError = com.montnets.emp.i18n.util.MessageUtils.extractMessage(
				"user", "user_xtgl_czygl_text_170", request);
		// 上一级机构编码为空
		String rootCodeIsEmptyError = com.montnets.emp.i18n.util.MessageUtils
				.extractMessage("user", "user_xtgl_czygl_text_171", request);
		// 只能导入本机机构以下的数据
		String lackOfAuthorityError = com.montnets.emp.i18n.util.MessageUtils
				.extractMessage("user", "user_xtgl_czygl_text_173", request);
		// 机构编码重复
		String codeIsRepeatError = com.montnets.emp.i18n.util.MessageUtils
				.extractMessage("user", "user_xtgl_czygl_text_174", request);
		// 机构名称重复
		String nameIsRepeatError = com.montnets.emp.i18n.util.MessageUtils
				.extractMessage("user", "user_xtgl_czygl_text_175", request);
		// 上一级机构编码不存在
		String rootCodeNotExistError = com.montnets.emp.i18n.util.MessageUtils
				.extractMessage("user", "user_xtgl_czygl_text_176", request);
		// 机构职责超过250个字
		String depRespGtNumberError = com.montnets.emp.i18n.util.MessageUtils
				.extractMessage("user", "user_xtgl_czygl_text_184", request);
		// 机构名称超过20个字
		String nameGtNumberError = com.montnets.emp.i18n.util.MessageUtils
				.extractMessage("user", "user_xtgl_czygl_text_185", request);
		// 机构编码超过32
		String codeGtNumberError = com.montnets.emp.i18n.util.MessageUtils
				.extractMessage("user", "user_xtgl_czygl_text_186", request);
		String uplodeNumGtMaxError = com.montnets.emp.i18n.util.MessageUtils
				.extractMessage("user", "user_xtgl_czygl_text_182", request);
		String uplodeNumLtMinError = com.montnets.emp.i18n.util.MessageUtils
				.extractMessage("user", "user_xtgl_czygl_text_183", request);
		String codeIsChineseError = com.montnets.emp.i18n.util.MessageUtils
				.extractMessage("user", "user_xtgl_czygl_text_192", request);
		// 系统未知错误
		String unknowError = com.montnets.emp.i18n.util.MessageUtils.extractMessage(
				"user", "user_xtgl_czygl_text_151", request);
		// 改行记录为空
		String cellIsEmpty = com.montnets.emp.i18n.util.MessageUtils.extractMessage(
				"user", "user_xtgl_czygl_text_201", request);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Long startTime = System.currentTimeMillis();
		int rows = 0;
		HSSFWorkbook workBook = null;
		// 失败数
		int fail = 0;
		// 成功数
		int succNum = 0;
		int total = 0;
		opt.setTotal(total);
		Boolean flag = true;
		String coreCode = loginSysuser.getCorpCode();
		try {
			Set<String> deptCordThirdSet = getListLfDep(coreCode);
			workBook = new HSSFWorkbook(fileItem.getInputStream());
			HSSFSheet sh = workBook.getSheetAt(0);
			for (int i = 2; i <= sh.getLastRowNum(); i++) {
				rows++;
			}
			if (rows > OPT_UPLOAD_MAXNUM) {
				request.setAttribute("jsresult", uplodeNumGtMaxError);
			}
			else if (rows <= 0) {
				request.setAttribute("jsresult", uplodeNumLtMinError);
			}
			else {
				opt.setTotal(rows);
				for (int k = 2; k <= rows + 1; k++) {
					LfDep lfDep = new LfDep();
					HSSFRow cell = sh.getRow(k);
					if (cell == null) {
						erroMap.put(k + 1, cellIsEmpty);
						fail++;
						continue;
					}
					// 获取机构名称
					String depName = getCellValueHssf(cell.getCell(0));
					List<LfDep> lfs = null;
					if (StringUtils.isEmpty(depName)) {
						erroMap.put(k + 1, nameIsEmptyError);
						fail++;
						continue;
					}
					else {
						if (depName.length() > 20) {
							erroMap.put(k + 1, nameGtNumberError);
							fail++;
							continue;
						}
						LinkedHashMap<String, String> conditionMap2 = getCondition(
								coreCode, depName, null);
						// 查询出数据库中相同的机构名且状态为为使用中的状态
						lfs = baseBiz.getByCondition(LfDep.class, conditionMap2, null);

					}

					// 判断该相同机构名称是否和新添加的机构名称在同一级目录下
					if (lfs != null && lfs.size() > 0) {
						// 获取上一级机构编码
						String upCodes = getCellValueHssf(cell.getCell(3)).toUpperCase();
						if (upCodes.equals(UPPER_LEVEL_DEP_CODE)) {
							upCodes = ROOT_DEP_CODE;
						}
						String dataCode = null;
						// 如果数据库中有这个机构名称，判断是否是同一级的下的机构，如果是同一级，则记录错误，如果不是同一级则正常插入
						for (LfDep lfDep2 : lfs) {
							LfDep lfde = baseBiz.getById(LfDep.class,
									lfDep2.getSuperiorId());
							dataCode = lfde.getDepCodeThird().toUpperCase();
							if (dataCode.equals(upCodes)) {
								flag = false;
								erroMap.put(k + 1, nameIsRepeatError);
								fail++;
								break;
							}
						}
					}
					if (flag) {
						lfDep.setDepName(depName);
					}
					else {
						flag = true;
						continue;
					}
					// 获取机构编码
					String depCodeThird = getCellValueHssf(cell.getCell(1)).toUpperCase();
					if (StringUtils.isEmpty(depCodeThird)) {
						erroMap.put(k + 1, codeIsEmptyError);
						fail++;
						continue;
					}
					else {
						if (deptCordThirdSet.contains(depCodeThird)) {
							erroMap.put(k + 1, codeIsRepeatError);
							fail++;
							continue;
						}
						// 机构编码不能包含中文
						if (isContainChinese(depCodeThird)) {
							erroMap.put(k + 1, codeIsChineseError);
							fail++;
							continue;
						}
						if (depCodeThird.length() > 32) {
							erroMap.put(k + 1, codeGtNumberError);
							fail++;
							continue;
						}
						lfDep.setDepCodeThird(depCodeThird);
					}
					// 获取机构职责
					String depResp = getCellValueHssf(cell.getCell(2));
					if (StringUtils.isEmpty(depResp)) {
						lfDep.setDepResp("");
					}
					else if (depResp.length() <= 250) {
						lfDep.setDepResp(depResp);
					}
					else {
						erroMap.put(k + 1, depRespGtNumberError);
						fail++;
						continue;
					}
					// 获取上一级机构编码
					String upCode = getCellValueHssf(cell.getCell(3)).toUpperCase();
					if (StringUtils.isEmpty(upCode)) {
						erroMap.put(k + 1, rootCodeIsEmptyError);
						fail++;
						continue;
					}
					else {
						// 如果上一级编码不为空，查找上一级编码的深度，当前的深度为上一级深度加一
						if (upCode.equals(UPPER_LEVEL_DEP_CODE)) {
							lfDep.setDepDirect(0l);
							// 如果上一级机构编码为空，则，深度为2，路径为1/+插入的当前的id序号
							lfDep.setDepLevel(2);
							upCode = ROOT_DEP_CODE;
						}
						LinkedHashMap<String, String> conditionMap = getCondition(
								coreCode, null, upCode);
						// 查询出数据库中相同机构编码的数据
						List<LfDep> lf = baseBiz.getByCondition(LfDep.class,
								conditionMap, null);
						if (lf != null && lf.size() > 0) {
							LfDep lfGetOne = lf.get(0);
							// 路径为上一级机构编码的路径，加上当前插入的序号id，用一个事务的方式，先插入数据，在修改这个值
							lfDep.setDeppath(lfGetOne.getDeppath());
							String nowDepPath = lfGetOne.getDeppath();
							// 判断当前登录的用户是否是跨部门插入
							if (nowDepPath.startsWith(deppath)) {
								// 深度为上一级机构编码深度加一
								lfDep.setDepLevel(lfGetOne.getDepLevel() + 1);
								// 路径为上一级机构编码的路径，加上当前插入的序号id
								lfDep.setDeppath(lfGetOne.getDeppath());
								lfDep.setSuperiorId(lfGetOne.getDepId());
							}
							else {
								erroMap.put(k + 1, lackOfAuthorityError);
								fail++;
								continue;
							}
						}
						else {
							erroMap.put(k + 1, rootCodeNotExistError);
							fail++;
							continue;
						}

					}
					lfDep.setCorpCode(coreCode);
					lfDep.setDepState(1);

					Boolean result = depPriBiz.addDep(lfDep);
					if (result == true) {
						deptCordThirdSet.add(depCodeThird);
						succNum++;
					}
					else {
						fail++;
						erroMap.put(k + 1, unknowError);
						EmpExecutionContext.error("机构管理，数据库插入异常！");
						continue;
					}
				}

			}
		} catch (BiffException e) {
			EmpExecutionContext.error(e, "上传部门机构文件出现异常！");
		} catch (IOException e) {
			EmpExecutionContext.error(e, "数据库操作出现异常！");
		}
		finally {
			opt.setSuccess(succNum);
			opt.setFail(fail);
			opt.setTime(sdf.format(startTime));
			if (fileItem != null) {
				fileItem.getInputStream().close();
				fileItem.delete();
			}
		}

	}

	/**
	 * 上传.xlsx文件
	 * 
	 * @param request
	 * @param deppath当前登录管理员所在部门的部门路径
	 * @param opt记录上传成功数
	 *            ，失败数，总数的类
	 * @param erroMap记录错误的集合
	 * @param loginSysuser当前登录的管理员
	 * @param fileItem
	 * @throws Exception
	 */

	public void depUploadXlsx (HttpServletRequest request, String deppath, OptUpload opt,
			Map<Integer, String> erroMap, LfSysuser loginSysuser, FileItem fileItem)
			throws Exception {
		// 机构编码为空
		String codeIsEmptyError = com.montnets.emp.i18n.util.MessageUtils.extractMessage(
				"user", "user_xtgl_czygl_text_169", request);
		// 机构名称为空
		String nameIsEmptyError = com.montnets.emp.i18n.util.MessageUtils.extractMessage(
				"user", "user_xtgl_czygl_text_170", request);
		// 上一级机构编码为空
		String rootCodeIsEmptyError = com.montnets.emp.i18n.util.MessageUtils
				.extractMessage("user", "user_xtgl_czygl_text_171", request);
		// 只能导入本机机构以下的数据
		String lackOfAuthorityError = com.montnets.emp.i18n.util.MessageUtils
				.extractMessage("user", "user_xtgl_czygl_text_173", request);
		// 机构编码重复
		String codeIsRepeatError = com.montnets.emp.i18n.util.MessageUtils
				.extractMessage("user", "user_xtgl_czygl_text_174", request);
		// 机构名称重复
		String nameIsRepeatError = com.montnets.emp.i18n.util.MessageUtils
				.extractMessage("user", "user_xtgl_czygl_text_175", request);
		// 上一级机构编码不存在
		String rootCodeNotExistError = com.montnets.emp.i18n.util.MessageUtils
				.extractMessage("user", "user_xtgl_czygl_text_176", request);
		// 机构职责超过250个字
		String depRespGtNumberError = com.montnets.emp.i18n.util.MessageUtils
				.extractMessage("user", "user_xtgl_czygl_text_184", request);

		// 机构名称超过32个字
		String nameGtNumberError = com.montnets.emp.i18n.util.MessageUtils
				.extractMessage("user", "user_xtgl_czygl_text_185", request);
		// 机构编码超过20
		String codeGtNumberError = com.montnets.emp.i18n.util.MessageUtils
				.extractMessage("user", "user_xtgl_czygl_text_186", request);
		// 上传数量超过1000条数据
		String uplodeNumGtMaxError = com.montnets.emp.i18n.util.MessageUtils
				.extractMessage("user", "user_xtgl_czygl_text_182", request);
		// 模板内容为空
		String uplodeNumLtMinError = com.montnets.emp.i18n.util.MessageUtils
				.extractMessage("user", "user_xtgl_czygl_text_183", request);
		// 机构编码不能包含中文
		String codeIsChineseError = com.montnets.emp.i18n.util.MessageUtils
				.extractMessage("user", "user_xtgl_czygl_text_192", request);
		// 系统未知错误
		String unknowError = com.montnets.emp.i18n.util.MessageUtils.extractMessage(
				"user", "user_xtgl_czygl_text_151", request);
		// 改行记录为空
		String cellIsEmpty = com.montnets.emp.i18n.util.MessageUtils.extractMessage(
				"user", "user_xtgl_czygl_text_201", request);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Long startTime = System.currentTimeMillis();
		int rows = 0;
		XSSFWorkbook hssfWorkbook = null;
		// 失败数
		int fail = 0;
		// 成功数
		int succNum = 0;
		int total = 0;
		opt.setTotal(total);
		Boolean flag = true;
		String coreCode = loginSysuser.getCorpCode();
		try {
			Set<String> deptCordThirdSet = getListLfDep(coreCode);
			// 获得工作簿
			hssfWorkbook = new XSSFWorkbook(fileItem.getInputStream());
			// 获得表格
			XSSFSheet sh = hssfWorkbook.getSheetAt(0);

			for (int i = 2; i <= sh.getLastRowNum(); i++) {
				rows++;
			}
			if (rows > OPT_UPLOAD_MAXNUM) {
				request.setAttribute("jsresult", uplodeNumGtMaxError);
			}
			else if (rows <= 0) {
				request.setAttribute("jsresult", uplodeNumLtMinError);
			}
			else {
				opt.setTotal(rows);
				for (int k = 2; k <= rows + 1; k++) {
					LfDep lfDep = new LfDep();
					XSSFRow cell = sh.getRow(k);
					if (cell == null) {
						erroMap.put(k + 1, cellIsEmpty);
						fail++;
						continue;
					}

					// 获取机构名称
					String depName = getCellValue(cell.getCell(0));
					List<LfDep> lfs = null;
					if (StringUtils.isEmpty(depName)) {
						erroMap.put(k + 1, nameIsEmptyError);
						fail++;
						continue;
					}
					else {
						if (depName.length() > 20) {
							erroMap.put(k + 1, nameGtNumberError);
							fail++;
							continue;
						}
						LinkedHashMap<String, String> conditionMap2 = getCondition(
								coreCode, depName, null);
						lfs = baseBiz.getByCondition(LfDep.class, conditionMap2, null);

					}
					// 判断该相同机构名称是否和新添加的机构名称在同一级目录下
					if (lfs != null && lfs.size() > 0) {
						String upCodes = getCellValue(cell.getCell(3)).toUpperCase();
						if (upCodes.equals(UPPER_LEVEL_DEP_CODE)) {
							upCodes = ROOT_DEP_CODE;
						}
						String dataCode = null;
						// 如果两个父级的机构编码相同，则该机构名称记录错误
						for (LfDep lfDep2 : lfs) {
							LfDep lfde = baseBiz.getById(LfDep.class,
									lfDep2.getSuperiorId());
							dataCode = lfde.getDepCodeThird().toUpperCase();
							if (dataCode.equals(upCodes)) {
								flag = false;
								erroMap.put(k + 1, nameIsRepeatError);
								fail++;
								break;
							}
						}
					}
					if (flag) {
						lfDep.setDepName(depName);
					}
					else {
						flag = true;
						continue;
					}

					// 获取机构编码
					String depCodeThird = getCellValue(cell.getCell(1)).toUpperCase();
					if (StringUtils.isEmpty(depCodeThird)) {
						erroMap.put(k + 1, codeIsEmptyError);
						fail++;
						continue;
					}
					else {
						if (deptCordThirdSet.contains(depCodeThird)) {
							erroMap.put(k + 1, codeIsRepeatError);
							fail++;
							continue;
						}
						if (isContainChinese(depCodeThird)) {
							erroMap.put(k + 1, codeIsChineseError);
							fail++;
							continue;
						}
						if (depCodeThird.length() > 32) {
							erroMap.put(k + 1, codeGtNumberError);
							fail++;
							continue;
						}
						lfDep.setDepCodeThird(depCodeThird);
					}
					// 获取机构职责
					String depResp = getCellValue(cell.getCell(2));
					if (StringUtils.isEmpty(depResp)) {
						lfDep.setDepResp("");
					}
					else if (depResp.length() <= 250) {
						lfDep.setDepResp(depResp);
					}
					else {
						erroMap.put(k + 1, depRespGtNumberError);
						fail++;
						continue;
					}
					// 获取上一级机构编码
					String upCode = getCellValue(cell.getCell(3)).toUpperCase();
					if (StringUtils.isEmpty(upCode)) {
						erroMap.put(k + 1, rootCodeIsEmptyError);
						fail++;
						continue;
					}
					else {

						// 如果上一级编码不为空，查找上一级编码的深度，当前的深度为上一级深度加一
						if (upCode.equals(UPPER_LEVEL_DEP_CODE)) {
							lfDep.setDepDirect(0l);
							// 如果上一级机构编码为空，则，深度为2，路径为1/+插入的当前的id序号
							lfDep.setDepLevel(2);
							upCode = ROOT_DEP_CODE;
						}
						LinkedHashMap<String, String> conditionMap = getCondition(
								coreCode, null, upCode);// new
						// 查询出数据库中相同机构编码的数据
						List<LfDep> lf = baseBiz.getByCondition(LfDep.class,
								conditionMap, null);
						if (lf != null && lf.size() > 0) {
							LfDep lfGetOne = lf.get(0);
							// 路径为上一级机构编码的路径，加上当前插入的序号id，用一个事务的方式，先插入数据，在修改这个值
							lfDep.setDeppath(lfGetOne.getDeppath());
							String nowDepPath = lfGetOne.getDeppath();
							if (nowDepPath.startsWith(deppath)) {
								// 深度为上一级机构编码深度加一
								lfDep.setDepLevel(lfGetOne.getDepLevel() + 1);
								// 路径为上一级机构编码的路径，加上当前插入的序号id
								lfDep.setDeppath(lfGetOne.getDeppath());
								lfDep.setSuperiorId(lfGetOne.getDepId());
							}
							else {
								erroMap.put(k + 1, lackOfAuthorityError);
								fail++;
								continue;
							}
						}
						else {
							erroMap.put(k + 1, rootCodeNotExistError);
							fail++;
							continue;
						}

					}
					// 获取当前登录人员机构编号
					lfDep.setCorpCode(coreCode);
					lfDep.setDepState(1);
					// 插入数据库,单个插入
					Boolean result = depPriBiz.addDep(lfDep);
					if (result == true) {
						deptCordThirdSet.add(depCodeThird);
						succNum++;
					}
					else {
						fail++;
						erroMap.put(k + 1, unknowError);
						EmpExecutionContext.error("机构管理，数据库插入异常！");
						continue;
					}
				}

			}
		} catch (BiffException e) {
			EmpExecutionContext.error(e, "上传部门机构文件出现异常！");
		} catch (IOException e) {
			EmpExecutionContext.error(e, "数据库操作出现异常！");
		}
		finally {
			opt.setSuccess(succNum);
			opt.setFail(fail);
			opt.setTime(sdf.format(startTime));
			if (fileItem != null) {
				fileItem.getInputStream().close();
				fileItem.delete();
			}
		}
	}

	/**
	 * 对Excel的各个单元格的格式进行判断并转换
	 */
	private String getCellValue (XSSFCell xssfCell) {
		String cellValue = null;
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
	 * 对Excel的各个单元格的格式进行判断并转换
	 */
	private String getCellValueHssf (HSSFCell xssfCell) {
		String cellValue = null;
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
	 * 判断英文和数字的正则
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isContainChinese (String str) {
		Pattern p = Pattern.compile("^[A-Za-z0-9]+$");
		// 如果为true，匹配正确
		Matcher m = p.matcher(str);
		if (m.matches()) {
			return false;
		}
		return true;
	}
	/**
	 * 查询条件
	 * 
	 * @param coreCode
	 *            企业编码
	 * @param depName
	 *            机构名称
	 * @param depCodeThird
	 *            机构编码
	 * @return
	 */
	private LinkedHashMap<String, String> getCondition (String coreCode, String depName,
			String depCodeThird) {
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		if (!StringUtils.isEmpty(depName)) {
			conditionMap.put("depName", depName);
		}
		if (!StringUtils.isEmpty(coreCode)) {
			conditionMap.put("corpCode", coreCode);
		}
		if (!StringUtils.isEmpty(depCodeThird)) {
			conditionMap.put("depCodeThird", depCodeThird);
		}
		conditionMap.put("depState", "1");
		return conditionMap;
	}
	/**
	 * 查出数据库中当前企业的所有机构编码，并过滤重复
	 * 
	 * @param coreCode
	 * @return deptCordThirdSet集合
	 * @throws Exception
	 */
	private Set<String> getListLfDep (String coreCode) throws Exception {
		LinkedHashMap<String, String> conditionMaps = getCondition(coreCode, null, null);
		List<LfDep> lfDepList = baseBiz.getByCondition(LfDep.class, conditionMaps, null);
		Iterator<LfDep> itsDep = lfDepList.iterator();
		Set<String> deptCordThirdSet = new HashSet<String>();
		while (itsDep.hasNext()) {
			LfDep obj = itsDep.next();
			deptCordThirdSet.add(obj.getDepCodeThird().toUpperCase());
		}
		return deptCordThirdSet;
	}

}
