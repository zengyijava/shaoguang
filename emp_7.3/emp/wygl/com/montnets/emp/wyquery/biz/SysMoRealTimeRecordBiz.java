package com.montnets.emp.wyquery.biz;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.util.*;
import com.montnets.emp.wyquery.dao.GenericMoMtSpgateVoDAO;
import com.montnets.emp.wyquery.vo.SysMoMtSpgateVo;
import com.montnets.emp.wyquery.vo.SystemMtTaskVo;
import org.apache.commons.beanutils.DynaBean;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Administrator
 *
 */
public class SysMoRealTimeRecordBiz {

	
    public List<SysMoMtSpgateVo> getDownHisVos(Long curLongedUserId,
                                               SysMoMtSpgateVo sysMoMtSpVo) throws Exception
	{
		List<SysMoMtSpgateVo> downHisVosList;
		try
		{			
			downHisVosList = new GenericMoMtSpgateVoDAO().findSysMoMtSpVoVo(sysMoMtSpVo);
			
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"获取网优下行记录异常");
			throw e;
		}
		return downHisVosList;
	}	
	

	
	/**
	 * 下行记录中的实时记录导出方法
	 * 
	 * @param moTask
	 * @param pageInfo
	 * @return
	 */
	
    public Map<String, String> createMtTaskTimeExcel(String langName, String userId, SystemMtTaskVo mtTaskVo,
                                                     PageInfo pageInfo, Integer isHidePhone) {
		String BASEDIR=new TxtFileUtil().getWebRoot()+"wygl/wyquery/file";
		String voucherPath = "download";
		
		//String voucherTemplatePath = BASEDIR + File.separator + "temp"
		//		+ File.separator + "wy_mtRecord.xlsx";// excel模板地址

		String voucherTemplatePath = null;
		if(StaticValue.ZH_HK.equals(langName)){
			voucherTemplatePath = BASEDIR + File.separator + "temp"
			+ File.separator + "wy_mtRecord-zh_HK.xlsx";// excel模板地址
		}else if(StaticValue.ZH_TW.equals(langName)){
			voucherTemplatePath = BASEDIR + File.separator + "temp"
			+ File.separator + "wy_mtRecord-zh_TW.xlsx";// excel模板地址
		}else{
			voucherTemplatePath = BASEDIR + File.separator + "temp"
			+ File.separator + "wy_mtRecord.xlsx";// excel模板地址
		}

		Map<String, String> resultMap = new HashMap<String, String>();

		String filePath = null;
		String fileName = null;// 文件名

		int size = 1; // 生成的工作薄个数
		EmpExecutionContext.info("网优下行记录工作表个数：" + size);

		XSSFWorkbook workbook = null;

		try {
			pageInfo.setPageSize(10000);// 设置每个excel文件的行数
			// 获取下行记录实时记录的方法
			SysMtRealTimeRecordBiz downBiz = new SysMtRealTimeRecordBiz();
			List<DynaBean> mtTaskList = downBiz.getDownMtTaskVos(Long.parseLong(userId),mtTaskVo, pageInfo);

			if (mtTaskList == null || mtTaskList.size() == 0) {
				return null;
			}

			// 计算出文件数
			int fileCount = pageInfo.getTotalPage();

			Date curDate = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddH24mmss");
			// 文件的存储路径
			String voucherFilePath = BASEDIR + File.separator + voucherPath
					+ File.separator + "wy_mtRecord" + File.separator
					+ sdf.format(curDate);

			File file1 = new File(voucherFilePath);
			file1.mkdirs();

			int curIndex = 0;// 当前clientvosList的索引

			File file = new File(voucherTemplatePath);
			/*
			 * if (!file.exists()) { file.mkdirs(); }
			 */

			for (int f = 0; f < fileCount; f++) {
				fileName = "wy_mtRecord_" + sdf.format(curDate) + "_[" + (f+1)
						+ "].xlsx";
				// 读取模板
				InputStream in = new FileInputStream(file);
				// 工作表
				workbook = new XSSFWorkbook(in);
				// 表格样式
				XSSFCellStyle cellStyle =new ExcelTool(). setCellStyle(workbook);

				XSSFSheet sheet = workbook.cloneSheet(0);
				workbook.removeSheetAt(0);
				//workbook.setSheetName(0, "网优下行记录");
				if(StaticValue.ZH_HK.equals(langName)){
					workbook.setSheetName(0, "NetworkExcellentDownlinkRecords");
				}else if(StaticValue.ZH_TW.equals(langName)){
					workbook.setSheetName(0, "網優下行記錄");
				}else{
					workbook.setSheetName(0, "网优下行记录");
				}

				// 读取模板工作表
				XSSFRow row = null;
				EmpExecutionContext.debug("网优下行记录工作薄创建与模板移除成功!");
				pageInfo.setPageIndex(f + 2);// 定位下一页
				
				XSSFCell[] cell = new XSSFCell[9];

				for (int k = 0; k < mtTaskList.size(); k++) {





					row = sheet.createRow(k + 1);
					// 生成四个单元格
					cell[0] = row.createCell(0);
					cell[1] = row.createCell(1);
					cell[2] = row.createCell(2);
					cell[3] = row.createCell(3);
					cell[4] = row.createCell(4);
					cell[5] = row.createCell(5);
					cell[6] = row.createCell(6);
					cell[7] = row.createCell(7);
					cell[8] = row.createCell(8);

					// 设置单元格样式
					cell[0].setCellStyle(cellStyle);
					cell[1].setCellStyle(cellStyle);
					cell[2].setCellStyle(cellStyle);
					cell[3].setCellStyle(cellStyle);
					cell[4].setCellStyle(cellStyle);
					cell[5].setCellStyle(cellStyle);
					cell[6].setCellStyle(cellStyle);
					cell[7].setCellStyle(cellStyle);
					cell[8].setCellStyle(cellStyle);
					
					// 设置单元格内容
					String taskid=mtTaskList.get(k).get("taskid")+"";
					if(!"0".equals(taskid)&&!"null".equals(taskid)){
						cell[0].setCellValue(taskid);
					}else{
						cell[0].setCellValue("--");
					}
					String rmessage=mtTaskList.get(k).get("message")+""; 
					cell[1].setCellValue(rmessage);
					String format=mtTaskList.get(k).get("sendtime")+"";
					if(format.indexOf(".")>-1){
						format=format.substring(0, format.lastIndexOf("."));
					}
					cell[2].setCellValue(format);
					String recvtime=mtTaskList.get(k).get("recvtime")+"";
					String phone=mtTaskList.get(k).get("phone")+"";
					if(recvtime.indexOf(".")>-1){
						recvtime=recvtime.substring(0, recvtime.lastIndexOf("."));
					}
					cell[3].setCellValue(recvtime);
					cell[4].setCellValue(phone);
					String rerrorcode=mtTaskList.get(k).get("errorcode")+"";
					if("".equals(rerrorcode.trim())){
						cell[5].setCellValue("-");
					}else if("DELIVRD".equals(rerrorcode.trim())||"0".equals(rerrorcode.trim())){
					    //cell[5].setCellValue("成功");
						if(StaticValue.ZH_HK.equals(langName)){
						    cell[5].setCellValue("success");
						}else if(StaticValue.ZH_TW.equals(langName)){
						    cell[5].setCellValue("成功");
						}else{
						    cell[5].setCellValue("成功");
						}
					}else {
						//cell[5].setCellValue("失败["+rerrorcode.trim()+"]");
							if(StaticValue.ZH_HK.equals(langName)){
								cell[5].setCellValue("failure["+rerrorcode.trim()+"]");
							}else if(StaticValue.ZH_TW.equals(langName)){
								cell[5].setCellValue("失敗["+rerrorcode.trim()+"]");
							}else{
								cell[5].setCellValue("失败["+rerrorcode.trim()+"]");
							}
						}
					String userid=mtTaskList.get(k).get("userid")+"";
					if("null".equals(userid)){
						userid="--";
					}
					cell[6].setCellValue(userid);
					String spgate=mtTaskList.get(k).get("spgate")+"";
					String cpno=mtTaskList.get(k).get("cpno")+"";
					if("null".equals(spgate)){
						spgate="";
					}
					if("null".equals(cpno)){
						cpno="";
					}
					
					cell[7].setCellValue(spgate+cpno);
					String gatename=mtTaskList.get(k).get("gatename")+"";
					if("null".equals(gatename)){
						gatename="--";
					}
					cell[8].setCellValue(gatename);
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
				mtTaskList.clear();
				mtTaskList = null;
			}

			//fileName = "网优下行记录" + sdf.format(curDate) + ".zip";

			if(StaticValue.ZH_HK.equals(langName)){
				fileName = "NetworkExcellentDownlinkRecords" + sdf.format(curDate) + ".zip";
			}else if(StaticValue.ZH_TW.equals(langName)){
				fileName = "網優下行記錄" + sdf.format(curDate) + ".zip";
			}else{
				fileName = "网优下行记录" + sdf.format(curDate) + ".zip";
			}
			filePath = BASEDIR + File.separator + voucherPath + File.separator
					+ "wy_mtRecord" + File.separator + fileName;

			ZipUtil.compress(voucherFilePath, filePath);
            boolean flag = FileUtils.deleteDir(file1);
            if (!flag) {
                EmpExecutionContext.error("刪除文件失敗！");
            }
		} catch (Exception e) {
			EmpExecutionContext.error(e,"网优下行记录中的实时记录导出异常");
		}
		resultMap.put("FILE_NAME", fileName);
		resultMap.put("FILE_PATH", filePath);
		int record=0;
		if(pageInfo!=null){
			record=pageInfo.getTotalRec();
		}
		resultMap.put("SIZE",record+"");
		return resultMap;
	}
}
