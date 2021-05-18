package com.montnets.emp.appmage.biz;

import com.montnets.emp.appmage.dao.app_mtrecordselectDao;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.util.*;
import org.apache.commons.beanutils.DynaBean;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class app_mtrecordselectBiz extends SuperBiz
{
	/****
	 * APP发送记录查询
	 * @param corpCode
	 * @param conditionMap
	 * @param pageInfo
	 * @return
	 */
	public List<DynaBean> query(String corpCode, LinkedHashMap<String, String> conditionMap, PageInfo pageInfo)
	{
		List<DynaBean> accounts = null;
		try
		{
			LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
			return new app_mtrecordselectDao().query(corpCode, conditionMap, orderbyMap, pageInfo);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "查询APP发送记录失败");
		}
		return accounts;
	}

	
	/**
	 * APP发送记录查询导出方法
	 * 
	 * @param moTask
	 * @param pageInfo
	 * @return
	 */
	public Map<String, String> createMtHistoryExcel(String corpCode, LinkedHashMap<String, String> conditionMap, PageInfo pageInfo, String langName) {
		String BASEDIR=new TxtFileUtil().getWebRoot()+"appmage/record/file";
		String voucherPath = "download";
		String separator="/";
		String voucherTemplatePath = BASEDIR + separator + "temp"
				+ separator + "app_mtrecordselect_" + langName + ".xlsx";// excel模板地址

		Map<String, String> resultMap = new HashMap<String, String>();

		String filePath = null;
		String fileName = null;// 文件名

		int size = 1; // 生成的工作薄个数
		EmpExecutionContext.info("工作表个数：" + size);

		XSSFWorkbook workbook = null;

		try {
			pageInfo.setPageSize(10000);// 设置每个excel文件的行数
			List<DynaBean>  mtTaskList = query(corpCode, conditionMap,  pageInfo);

			if (mtTaskList == null || mtTaskList.size() == 0) {
				return null;
			}

			// 计算出文件数
			int fileCount = pageInfo.getTotalPage();

			Date curDate = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddH24mmss");
			// 文件的存储路径
			String voucherFilePath = BASEDIR + separator + voucherPath
					+ separator + "app_mtrecordselect" + separator
					+ sdf.format(curDate);

			File file1 = new File(voucherFilePath);
			file1.mkdirs();

			int curIndex = 0;// 当前clientvosList的索引

			File file = new File(voucherTemplatePath);
			/*
			 * if (!file.exists()) { file.mkdirs(); }
			 */

			for (int f = 0; f < fileCount; f++) {
				fileName = "app_mtrecordselect_" + sdf.format(curDate) + "_[" + (f+1)
						+ "].xlsx";
				// 读取模板
				InputStream in = new FileInputStream(file);
				// 工作表
				workbook = new XSSFWorkbook(in);
				// 表格样式
				XSSFCellStyle cellStyle =new ExcelTool(). setCellStyle(workbook);

				XSSFSheet sheet = workbook.cloneSheet(0);
				workbook.removeSheetAt(0);
				workbook.setSheetName(0, "APP发送记录查询");
				// 读取模板工作表
				XSSFRow row = null;
				EmpExecutionContext.debug("工作薄创建与模板移除成功!");
				

//				if (f > 0)// 第一次时不需要再查询，因为前面已经查询出第一页
//				{
//					mtTaskList = downBiz.getDownMtTaskVos(mtTask, pageInfo);
//				}

				pageInfo.setPageIndex(f + 2);// 定位下一页
				
				XSSFCell[] cell = new XSSFCell[9];

				for (int k = 0; k < mtTaskList.size(); k++) {





					row = sheet.createRow(k + 1);
					// 生成单元格
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
					String name=mtTaskList.get(k).get("name")+"";
					if(!"".equals(name)&&!"null".equals(name)){
						cell[0].setCellValue(name);
					}else{
						cell[0].setCellValue("");
					}
					String title=mtTaskList.get(k).get("title")+"";
					if(!"".equals(title)&&!"null".equals(title)){
						cell[1].setCellValue(title);
					}else{
						cell[1].setCellValue("");
					}

//					String format=mtTaskList.get(k).get("sendtime")+"";
//					if(format.indexOf(".")>-1){
//						format=format.substring(0, format.lastIndexOf("."));
//					}
					String content=mtTaskList.get(k).get("content")+"";
					
					
					if(!"".equals(content)&&!"null".equals(content)){
						cell[2].setCellValue(content);
					}else{
						cell[2].setCellValue("");
					}
					String msgtype=mtTaskList.get(k).get("msg_type")+"";
					 if("0".equals(msgtype)){
						 cell[3].setCellValue("文字");
						}else if("1".equals(msgtype)){
						cell[3].setCellValue("图片");
						}else if("2".equals(msgtype)){
						cell[3].setCellValue("视频");
						}else if("3".equals(msgtype)){
						cell[3].setCellValue("音频");
						}else if("4".equals(msgtype)){
						cell[3].setCellValue("事件推送");
						}else {
						cell[3].setCellValue("-");
					}
					 
					 String tousername=mtTaskList.get(k).get("tousername")+"";
						if(!"".equals(tousername)&&!"null".equals(tousername)){
							cell[4].setCellValue(tousername);
						}else{
							cell[4].setCellValue("");
						}

						String rptstate="";
						if(mtTaskList.get(k).get("rpt_state")!=null){
							rptstate=mtTaskList.get(k).get("rpt_state").toString();
						}
						if("0".equals(rptstate)){ 
							cell[5].setCellValue("成功");
						}else if("1".equals(rptstate)){
							cell[5].setCellValue("失败");
						}else{ 
							cell[5].setCellValue("未返");
						}

						String format="";
						if(mtTaskList.get(k).get("createtime")!=null){
							format=mtTaskList.get(k).get("createtime")+"";
							if(format.indexOf(".")>-1){
								format=format.substring(0, format.lastIndexOf("."));
							}
						}
						cell[6].setCellValue(format);
						
						if("0".equals(rptstate)){
							cell[7].setCellValue("已读");
						}else if("1".equals(rptstate)){
							cell[7].setCellValue("接收失败");
						}else {
							cell[7].setCellValue("未知");
						}
						
						String recrpttime="-";
						if(mtTaskList.get(k).get("recrpttime")!=null){
							recrpttime=mtTaskList.get(k).get("recrpttime")+"";
							if(recrpttime.indexOf(".")>-1){
								recrpttime=recrpttime.substring(0, recrpttime.lastIndexOf("."));
							}
						}
						cell[8].setCellValue(recrpttime);
					curIndex++;// clientvosList索引加1
				}

				// 输出到xlsx文件
				OutputStream os = new FileOutputStream(voucherFilePath
						+ separator + fileName);
				// 写入Excel对象
				workbook.write(os);
				// 清除对象
				os.close();
				in.close();
				workbook = null;
			}

			fileName = "app_mtrecordselect" + sdf.format(curDate) + ".zip";
			filePath = BASEDIR + separator + voucherPath + separator
					+ "app_mtrecordselect" + separator + fileName;

			ZipUtil.compress(voucherFilePath, filePath);
			boolean flag = FileUtils.deleteDir(file1);
			if (!flag) {
			    EmpExecutionContext.error("刪除文件失敗！");
            }
			resultMap.put("mtTaskList",String.valueOf(mtTaskList!=null? mtTaskList.size():""));
			//清除数据
			mtTaskList.clear();
			mtTaskList = null;
		} catch (Exception e) {
			EmpExecutionContext.error(e,"APP发送记录查询记录导出异常");
		}
		resultMap.put("FILE_NAME", fileName);
		resultMap.put("FILE_PATH", filePath);
		return resultMap;
	}
	
}
