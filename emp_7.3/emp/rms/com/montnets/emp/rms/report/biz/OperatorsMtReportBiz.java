package com.montnets.emp.rms.report.biz;

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

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.rms.report.dao.GenericOperatorsMtDataReportVoDAO;
import com.montnets.emp.rms.report.vo.LfRmsReportVo;
import com.montnets.emp.util.ExcelTool;
import com.montnets.emp.util.FileUtils;
import com.montnets.emp.util.GlobalConst;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.TxtFileUtil;
import com.montnets.emp.util.ZipUtil;

/**
 * 档位统计报表
 * 
 * @project p_rltc
 * @author lvxin
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2018-1-13
 * @description
 */

public class OperatorsMtReportBiz  {
	/**
	 * 通过查询条件获取档位统计报表集合 月报表
	 * 
	 * @param mtDataReportVo
	 *            查询条件
	 * @param pageInfo
	 *            分页对象
	 * @return 返回档位统计报表集合
	 * @throws Exception
	 */
	
    public List<LfRmsReportVo> getReportInfosByMonth(
			LfRmsReportVo mtDataReportVo, PageInfo pageInfo) throws Exception {

		// 定义档位统计报表集合
		List<LfRmsReportVo> mtDRVosList;

		try {
			// 判断是否需要分页
			if (pageInfo == null) {
				mtDRVosList = new GenericOperatorsMtDataReportVoDAO()
						.findOperatorsMtDataReportVoByMonth(mtDataReportVo);
			} else {
				mtDRVosList = new GenericOperatorsMtDataReportVoDAO()
						.findOperatorsMtDataReportVoByMonth(mtDataReportVo,
								pageInfo);
			}

		} catch (Exception e) {
			EmpExecutionContext.error(e, "查询档位报表出错");
			throw e;
		}
		// 返回数据集合
		return mtDRVosList;
	}

	/**
	 * 通过查询条件获取档位统计报表集合 年报表
	 * 
	 * @param mtDataReportVo
	 *            查询条件
	 * @param pageInfo
	 *            分页对象
	 * @return 返回档位统计报表集合
	 * @throws Exception
	 */
	
    public List<LfRmsReportVo> getReportInfosByYear(
			LfRmsReportVo mtDataReportVo, PageInfo pageInfo) throws Exception {

		// 定义档位统计报表集合
		List<LfRmsReportVo> mtDRVosList;

		try {
			// 判断是否需要分页
			if (pageInfo == null) {
				mtDRVosList = new GenericOperatorsMtDataReportVoDAO()
						.findOperatorsMtDataReportVoByYear(mtDataReportVo);
			} else {
				mtDRVosList = new GenericOperatorsMtDataReportVoDAO()
						.findOperatorsMtDataReportVoByYear(mtDataReportVo,
								pageInfo);
			}

		} catch (Exception e) {
			EmpExecutionContext.error(e, "通过查询条件获取档位统计报表集合 年报表异常");
			throw e;
		}
		// 返回数据集合
		return mtDRVosList;
	}

	/**
	 * 通过查询条件获取档位统计报表集合 日报表
	 * 
	 * @param rltcDataReportVo
	 *            查询条件
	 * @param pageInfo
	 *            分页对象
	 * @return 返回档位统计报表集合
	 * @throws Exception
	 */
	
    public List<LfRmsReportVo> getReportInfosByDays(
			LfRmsReportVo mtDataReportVo, PageInfo pageInfo) throws Exception {

		// 定义档位统计报表集合
		List<LfRmsReportVo> mtDRVosList;

		try {
			// 判断是否需要分页
			if (pageInfo == null) {
				mtDRVosList = new GenericOperatorsMtDataReportVoDAO()
						.findOperatorsMtDataReportVoByDays(mtDataReportVo);
			} else {
				mtDRVosList = new GenericOperatorsMtDataReportVoDAO()
						.findOperatorsMtDataReportVoByDays(mtDataReportVo,
								pageInfo);
			}

		} catch (Exception e) {
			EmpExecutionContext.error(e, "查询档位报表出错");
			throw e;
		}
		// 返回数据集合
		return mtDRVosList;
	}

	/**
	 * 通过查询条件获取档位统计报表集合 年报表
	 * 
	 * @param mtDataReportVo
	 *            查询条件
	 * @param pageInfo
	 *            分页对象
	 * @return 返回档位统计报表集合
	 * @throws Exception
	 */
	
    public List<LfRmsReportVo> getListByMsType(int type) throws Exception {

		// 定义档位统计报表集合
		List<LfRmsReportVo> mtDRVosList = new ArrayList<LfRmsReportVo>();
		String smssql = "select t.spid from mt_datareport t group by t.spid";
		String mmssql = "select t.spid from MMS_DATAREPORT t group by t.spid";

		try {
			// 判断短彩类型
			if (type == 0) {
				mtDRVosList = new SuperDAO().findEntityListBySQL(
						LfRmsReportVo.class, smssql, null);
			} else {
				mtDRVosList = new SuperDAO().findEntityListBySQL(
						LfRmsReportVo.class, mmssql, null);
			}

		} catch (Exception e) {
			EmpExecutionContext.error(e, "通过查询条件获取档位统计报表集合 年报表异常");
			throw e;
		}
		// 返回数据集合
		return mtDRVosList;
	}

	/**
	 * 获取总合计
	 * 
	 * @param operatorsMtDataReportVo
	 * @return
	 * @throws Exception
	 */
	
    public long[] findSumCount(LfRmsReportVo operatorsMtDataReportVo)
			throws Exception {
		// 获取总合计
		long[] count = new GenericOperatorsMtDataReportVoDAO()
				.findSumCount(operatorsMtDataReportVo);
		// 返回合计总数数组
		return count;
	}

	/**
	 * 生成档位报表Excel
	 * 
	 * @param mtdList
	 * @param queryTime
	 * @param count
	 * @param fail
	 * @param showTime
	 * @return
	 * @throws Exception
	 */
	
    public Map<String, String> createSpReportExcel(List<LfRmsReportVo> mtdList,
                                                   String countTime, String tCount, String tFail,
                                                   LfRmsReportVo operatorsMtDataReportVo) throws Exception {
		String BASEDIR = new TxtFileUtil().getWebRoot() + "cxtj/report/file";
		String voucherPath = "download";
		String reportFileName = "Report";
		// String reportName = "档位统计报表";
		String reportName = "SpMtReport";

		String voucherTemplatePath = BASEDIR + File.separator
				+ GlobalConst.SAMPLES + File.separator + "spMtReport.xlsx";

		Map<String, String> resultMap = new HashMap<String, String>();

		// 当前每页分页条数
		int intRowsOfPage = 500000;

		// 当前每页显示条数
		int intPagesCount = (mtdList.size() % intRowsOfPage == 0) ? (mtdList
				.size() / intRowsOfPage) : (mtdList.size() / intRowsOfPage + 1);

		int size = intPagesCount; // 生成的工作薄个数
		EmpExecutionContext.info("档位报表导出工作表个数：" + size);
		if (size == 0) {
			EmpExecutionContext.info("无档位报表数据！");
			throw new Exception("无档位报表数据！");
		}
		Date curDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddH24mmss");
		// 产生报表文件的存储路径
		String voucherFilePath = BASEDIR + File.separator + voucherPath
				+ File.separator + reportFileName + File.separator
				+ sdf.format(curDate);

		// 报表文件名
		String fileName = null;
		String filePath = null;
		XSSFWorkbook workbook = null;
		OutputStream os = null;
		InputStream in = null;
		try {

			File fileTemp = new File(voucherFilePath);
			if (!fileTemp.exists()) {
				fileTemp.mkdirs();
			}

			// 创建只读的Excel工作薄的对象, 此为模板文件
			File file = new File(voucherTemplatePath);
			int reportType = operatorsMtDataReportVo.getReporttype();
			boolean isDes = operatorsMtDataReportVo.getIsDes();
			String startTime = operatorsMtDataReportVo.getStartTime();
			String endTime = operatorsMtDataReportVo.getEndTime();

			SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy年M月d日");
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
			// DateFormat df = DateFormat.getDateInstance();
			String timestr = "";
			if (reportType == 2 && !isDes) {
				String startDate = "";
				String endDate = "";
				if (!"".equals(startTime) && null != startTime
						&& 0 != startTime.length()) {
					String btemp[] = startTime.split("-");
					startDate = btemp[0] + "年" + btemp[1] + "月" + btemp[2]
							+ "日";
				}

				if (!"".equals(endTime) && null != endTime
						&& 0 != endTime.length()) {
					String etemp[] = endTime.split("-");
					endDate = etemp[0] + "年" + etemp[1] + "月" + etemp[2] + "日";
				}
				timestr = startDate + " 至 " + endDate;
				// timestr =
				// sdf0.format(df.parse(startTime))+"至"+sdf0.format(df.parse(endTime));
			}
			// 读取模板
			in = new FileInputStream(file);
			for (int f = 0; f < size; f++) {
				fileName = reportName + "_" + sdf.format(curDate) + "_["
						+ (f + 1) + "]" + "_" + StaticValue.getServerNumber()
						+ ".xlsx";

				// 工作表
				workbook = new XSSFWorkbook(in);
				// 表格样式
				XSSFCellStyle[] cellStyle = new ExcelTool()
						.setLastCellStyle(workbook);
				// ------------------------------------------------------------

				// ------------------------------------------------------------

				XSSFSheet sheet = workbook.cloneSheet(0);
				workbook.removeSheetAt(0);
				workbook.setSheetName(0, reportName);
				// 读取模板工作表
				XSSFRow row = null;
				EmpExecutionContext.debug("工作薄创建与模板移除成功!");

				// 总体流程
				// 根据记录，计算总的页数
				// 每页的处理 一页一个sheet
				// 写入页标题
				// 循环写入该页的行数，一次写一行，每页定义了多少行，就写多少数据。直到数据没有。
				// 写页尾

				int intCnt = mtdList.size() < (f + 1) * intRowsOfPage ? mtdList
						.size() : (f + 1) * intRowsOfPage;
				int index = 0;

				XSSFCell[] cell1 = new XSSFCell[5];
				XSSFCell[] cell2 = new XSSFCell[5];
				int k;
				for (k = f * intRowsOfPage; k < intCnt; k++) {
					LfRmsReportVo mt = (LfRmsReportVo) mtdList.get(k);

					// 时间

					if (!isDes) {
						if (reportType == 2) {
						} else if (reportType == 1) {
							timestr = mt.getY() + "年";
						} else if (reportType == 0) {
							timestr = mt.getY() + "年" + mt.getImonth() + "月";
						} else {
							timestr = "";
						}
					} else {
						if (reportType == 2) {
							timestr = sdf0.format(sdf1.parse(mt.getIymd()));
						} else if (reportType == 1) {
							timestr = mt.getY() + "年" + mt.getImonth() + "月";
						} else if (reportType == 0) {
							timestr = sdf0.format(sdf1.parse(mt.getIymd()));
						} else {
							timestr = "";
						}
					}
					String sendTime = timestr;
					// sp账号
					String spId = mt.getSpID() != null ? (!"".equals(mt
							.getSpID().trim()) ? mt.getSpID() : "未知") : "未知";
					// 发送总数
					String icount = mt.getIcount().toString();
					// 接收失败数
					String rfail = mt.getRfail().toString();
					String spisuncm = "";
					if (mt.getSpisuncm() - 0 == 0) {
						spisuncm = "移动";
					} else if (mt.getSpisuncm() - 1 == 0) {
						spisuncm = "联通";
					} else if (mt.getSpisuncm() - 21 == 0) {
						spisuncm = "电信";
					} else if (mt.getSpisuncm() - 5 == 0) {
						spisuncm = "国外";
					}
					row = sheet.createRow(k + 1);
					// 生成四个单元格
					cell1[0] = row.createCell(0);
					cell1[1] = row.createCell(1);
					cell1[2] = row.createCell(2);
					cell1[3] = row.createCell(3);
					cell1[4] = row.createCell(4);

					// 设置单元格样式
					cell1[0].setCellStyle(cellStyle[0]);
					cell1[1].setCellStyle(cellStyle[0]);
					cell1[2].setCellStyle(cellStyle[0]);
					cell1[3].setCellStyle(cellStyle[0]);
					cell1[4].setCellStyle(cellStyle[0]);
					// 设置单元格内容
					cell1[0].setCellValue(sendTime);
					cell1[1].setCellValue(spId);
					cell1[2].setCellValue(spisuncm);
					cell1[3].setCellValue(icount);
					cell1[4].setCellValue(rfail);

					// 一页里的行数
					index++;
				}

				row = sheet.createRow(k + 1);
				// 生成四个单元格
				cell2[0] = row.createCell(0);
				cell2[1] = row.createCell(1);
				cell2[2] = row.createCell(2);
				cell2[3] = row.createCell(3);
				cell2[4] = row.createCell(4);

				// 设置单元格样式
				cell2[0].setCellStyle(cellStyle[1]);
				cell2[1].setCellStyle(cellStyle[1]);
				cell2[2].setCellStyle(cellStyle[1]);
				cell2[3].setCellStyle(cellStyle[1]);
				cell2[4].setCellStyle(cellStyle[1]);

				// 设置单元格内容
				cell2[0].setCellValue("");
				cell2[1].setCellValue("");
				cell2[2].setCellValue("合计:");
				cell2[3].setCellValue(tCount);
				cell2[4].setCellValue(tFail == "" ? "0" : tFail);

			}
			// 输出到xlsx文件
			os = new FileOutputStream(voucherFilePath + File.separator
					+ fileName);
			// 写入Excel对象
			workbook.write(os);

			fileName = "档位统计报表" + sdf.format(curDate) + "_"
					+ StaticValue.getServerNumber() + ".zip";
			filePath = BASEDIR + File.separator + voucherPath + File.separator
					+ reportFileName + File.separator + fileName;
			// 压缩文件夹
			ZipUtil.compress(voucherFilePath, filePath);
			// 删除文件夹
            boolean flag = FileUtils.deleteDir(fileTemp);
            if (!flag) {
                EmpExecutionContext.error("刪除文件失敗！");
            }
		} catch (Exception e) {
			EmpExecutionContext.error(e, "档位统计报表生成excel导出异常");
		} finally {
			// 清除对象
			workbook = null;
            if(os!=null){
                try {
                    os.close();
                } catch (Exception e) {
                    EmpExecutionContext.error(e, "流关闭异常！");
                }
            }
            if(in!=null){
                try {
                    in.close();
                } catch (Exception e) {
                    EmpExecutionContext.error(e, "流关闭异常！");
                }
            }
			
			
		}
		resultMap.put("FILE_NAME", fileName);
		resultMap.put("FILE_PATH", filePath);
		return resultMap;

	}

	/**
	 * 生成档位报表Excel
	 * 
	 * @param mtdList
	 * @param queryTime
	 * @param totalIcount
	 * @param totalRfail
	 * @param totalRsucc
	 * @param showTime
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> createSpReportExcel(List<LfRmsReportVo> mtdList,
			String countTime, long totalIcount, long totalRfail,
			long totalRsucc, LfRmsReportVo operatorsMtDataReportVo)
			throws Exception {
		String BASEDIR = new TxtFileUtil().getWebRoot() + "rms/report/file";
		String voucherPath = "download";
		String reportFileName = "Report";
		String reportName = "rltcReport";

		String voucherTemplatePath = BASEDIR + File.separator
				+ GlobalConst.SAMPLES + File.separator + "rltcReport.xlsx";

		Map<String, String> resultMap = new HashMap<String, String>();

		// 当前每页分页条数
		int intRowsOfPage = 500000;

		// 当前每页显示条数

		int intPagesCount = (mtdList.size() % intRowsOfPage == 0) ? (mtdList
				.size() / intRowsOfPage) : (mtdList.size() / intRowsOfPage + 1);

		int size = intPagesCount; // 生成的工作薄个数
		EmpExecutionContext.info("档位报表导出工作表个数：" + size);
		if (size == 0) {
			EmpExecutionContext.info("无档位报表数据！");
			throw new Exception("无档位报表数据！");
		}
		Date curDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddH24mmss");
		// 产生报表文件的存储路径
		String voucherFilePath = BASEDIR + File.separator + voucherPath
				+ File.separator + reportFileName + File.separator
				+ sdf.format(curDate);

		// 报表文件名
		String fileName = null;
		String filePath = null;
		XSSFWorkbook workbook = null;
		SXSSFWorkbook sworkbook = null;
		OutputStream os = null;
		InputStream in = null;
		try {

			File fileTemp = new File(voucherFilePath);
			if (!fileTemp.exists()) {
				fileTemp.mkdirs();
			}

			// 创建只读的Excel工作薄的对象, 此为模板文件
			File file = new File(voucherTemplatePath);
			int reportType = operatorsMtDataReportVo.getReporttype();
			String startTime = operatorsMtDataReportVo.getStartTime();
			String endTime = operatorsMtDataReportVo.getEndTime();
			boolean isDes = operatorsMtDataReportVo.getIsDes();

			SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy年M月d日");
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
			String timestr = "";
			if (reportType == 2 && !isDes) {
				String startDate = "";
				String endDate = "";
				if (!"".equals(startTime) && null != startTime
						&& 0 != startTime.length()) {
					String btemp[] = startTime.split("-");
					startDate = btemp[0] + "年" + btemp[1] + "月" + btemp[2]
							+ "日";
				}

				if (!"".equals(endTime) && null != endTime
						&& 0 != endTime.length()) {
					String etemp[] = endTime.split("-");
					endDate = etemp[0] + "年" + etemp[1] + "月" + etemp[2] + "日";
				}
				timestr = startDate + " 至 " + endDate;
				// timestr =
				// sdf0.format(df.parse(startTime))+"至"+sdf0.format(df.parse(endTime));
			}

			for (int f = 0; f < size; f++) {

				fileName = reportName + "_" + sdf.format(curDate) + "_["
						+ (f + 1) + "]" + "_" + StaticValue.getServerNumber()
						+ ".xlsx";
				// 读取模板
				in = new FileInputStream(file);
				// 工作表
				workbook = new XSSFWorkbook(in);
				workbook.setSheetName(0, reportName);
				// 表格样式
				XSSFCellStyle[] cellStyle = new ExcelTool()
						.setLastCellStyle(workbook);

				// 获取模板第一个工作簿
				Sheet sheetTemp = workbook.getSheetAt(0);
				// 获取模板第一行标题
				Row rowTitle = sheetTemp.getRow(0);

				// 标题单元格索引，从2开始，因为已经有3个单元格
				int cellTitleIndex = 2;

				cellTitleIndex++;
				if (rowTitle.getCell(cellTitleIndex) == null) {
					Cell ctitle = rowTitle.createCell(cellTitleIndex);
					sheetTemp.setColumnWidth(cellTitleIndex, 3000);
					// 设置单元格样式
					ctitle.setCellStyle(cellStyle[1]);
				}

				EmpExecutionContext.debug("工作薄创建与模板移除成功!");
				sworkbook = new SXSSFWorkbook(workbook, 10000);
				Sheet sheet = workbook.getSheetAt(0);

				if (in != null) {
					in.close();
				}
				// 读取模板工作表
				Row row = null;

				// 总体流程
				// 根据记录，计算总的页数
				// 每页的处理 一页一个sheet
				// 写入页标题
				// 循环写入该页的行数，一次写一行，每页定义了多少行，就写多少数据。直到数据没有。
				// 写页尾

				int intCnt = mtdList.size() < (f + 1) * intRowsOfPage ? mtdList
						.size() : (f + 1) * intRowsOfPage;
				int index = 0;

				Cell[] cell1 = new Cell[3 + mtdList.size()];
				Cell[] cell2 = new Cell[3 + mtdList.size()];

				int k;
				for (k = f * intRowsOfPage; k < intCnt; k++) {
					LfRmsReportVo mt = (LfRmsReportVo) mtdList.get(k);

					// 时间
					// 时间
					if (!isDes) {
						if (reportType == 2) {
						} else if (reportType == 1) {
							timestr = mt.getY() + "年";
						} else if (reportType == 0) {
							timestr = mt.getY() + "年" + mt.getImonth() + "月";
						} else {
							timestr = "";
						}
					} else {
						if (reportType == 2) {
							timestr = sdf0.format(sdf1.parse(mt.getIymd()));
						} else if (reportType == 1) {
							timestr = mt.getY() + "年" + mt.getImonth() + "月";
						} else if (reportType == 0) {
							timestr = sdf0.format(sdf1.parse(mt.getIymd()));
						} else {
							timestr = "";
						}
					}
					String sendTime = timestr;

					// sp账号
					String spId = mt.getSpID() != null ? (!"".equals(mt
							.getSpID().trim()) ? mt.getSpID() : "未知") : "未知";
					String spisuncm = "";
					if (mt.getSpisuncm() - 0 == 0) {
						spisuncm = "移动";
					} else if (mt.getSpisuncm() - 1 == 0) {
						spisuncm = "联通";
					} else if (mt.getSpisuncm() - 21 == 0) {
						spisuncm = "电信";
					} else if (mt.getSpisuncm() - 5 == 0) {
						spisuncm = "国外";
					}

					// 提交总数
					long icount = mt.getIcount() != null ? mt.getIcount() : 0;
					// 接收成功数
					long rsucc = mt.getRsucc() != null ? mt.getRsucc() : 0;
					// 接收失败数
					long rfail = mt.getRfail() != null ? mt.getRfail() : 0;
					// 档位数
					Integer degree = mt.getDegree() != null ? mt.getDegree()
							: 0;

					row = sheet.createRow(k + 1);

					// 生成6个单元格
					cell1[0] = row.createCell(0);
					cell1[1] = row.createCell(1);
					cell1[2] = row.createCell(2);
					cell1[3] = row.createCell(3);
					cell1[4] = row.createCell(4);
					cell1[5] = row.createCell(5);
					// 设置单元格样式
					cell1[0].setCellStyle(cellStyle[0]);
					cell1[1].setCellStyle(cellStyle[0]);
					cell1[2].setCellStyle(cellStyle[0]);
					cell1[3].setCellStyle(cellStyle[0]);
					cell1[4].setCellStyle(cellStyle[0]);
					cell1[5].setCellStyle(cellStyle[0]);
					// 设置单元格内容
					cell1[0].setCellValue(sendTime);
					cell1[1].setCellValue(degree);
					cell1[2].setCellValue(spisuncm);
					cell1[3].setCellValue(icount);
					cell1[4].setCellValue(rsucc);
					cell1[5].setCellValue(rfail);

				}

				row = sheet.createRow(k + 1);
				// 生成6个单元格
				cell2[0] = row.createCell(0);
				cell2[1] = row.createCell(1);
				cell2[2] = row.createCell(2);
				cell2[3] = row.createCell(3);
				cell2[4] = row.createCell(4);
				cell2[5] = row.createCell(5);
				// 设置单元格样式
				cell2[0].setCellStyle(cellStyle[1]);
				cell2[1].setCellStyle(cellStyle[1]);
				cell2[2].setCellStyle(cellStyle[1]);
				cell2[3].setCellStyle(cellStyle[1]);
				cell2[4].setCellStyle(cellStyle[1]);
				cell2[5].setCellStyle(cellStyle[1]);
				// 设置单元格内容
				cell2[0].setCellValue("");
				cell2[1].setCellValue("");
				cell2[2].setCellValue("合计：");
				cell2[3].setCellValue(totalIcount);
				cell2[4].setCellValue(totalRsucc);
				cell2[5].setCellValue(totalRfail);

			}

			// 输出到xlsx文件
			os = new FileOutputStream(voucherFilePath + File.separator
					+ fileName);
			// 写入Excel对象
			sworkbook.write(os);

			fileName = "档位统计报表" + sdf.format(curDate) + "_"
					+ StaticValue.getServerNumber() + ".zip";
			filePath = BASEDIR + File.separator + voucherPath + File.separator
					+ reportFileName + File.separator + fileName;
			// 压缩文件夹
			ZipUtil.compress(voucherFilePath, filePath);
			// 删除文件夹
            boolean flag = FileUtils.deleteDir(fileTemp);
            if (!flag) {
                EmpExecutionContext.error("刪除文件失敗！");
            }

		} catch (Exception e) {
			EmpExecutionContext.error(e, "档位表生成excel导出异常");
		} finally {
			// 清除对象
			workbook = null;
            if(os != null){
                try {
                    os.close();
                } catch (Exception e) {
                    EmpExecutionContext.error(e, "流关闭异常！");
                }
            }
            if(in!=null){
                try {
                    in.close();
                } catch (Exception e) {
                    EmpExecutionContext.error(e, "流关闭异常！");
                }
            }

		}
		resultMap.put("FILE_NAME", fileName);
		resultMap.put("FILE_PATH", filePath);
		return resultMap;

	}

}
