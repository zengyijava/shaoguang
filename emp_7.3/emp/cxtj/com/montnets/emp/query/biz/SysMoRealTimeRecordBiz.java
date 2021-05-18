package com.montnets.emp.query.biz;

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

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Cell;
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

import com.montnets.emp.common.constant.CommonVariables;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.query.dao.GenericMoMtSpgateVoDAO;
import com.montnets.emp.query.dao.GenericMoTaskVoDAO;
import com.montnets.emp.query.vo.MoTaskVo;
import com.montnets.emp.query.vo.SysMoMtSpgateVo;
import com.montnets.emp.util.ExcelTool;
import com.montnets.emp.util.FileUtils;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.TxtFileUtil;
import com.montnets.emp.util.ZipUtil;

/**
 * 
 * @author Administrator
 *
 */
public class SysMoRealTimeRecordBiz
{
	/**
	 * 上行实时记录
	 * @param moTaskVo
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	
    public List<MoTaskVo> getUpInfo(MoTaskVo moTaskVo, PageInfo pageInfo, String corpcode) throws Exception {
		List<MoTaskVo> moTasksList = null;
		try{
			//判断是否需要分页
			if(pageInfo == null){
				moTasksList = new GenericMoTaskVoDAO().findMoTaskVo(moTaskVo,corpcode);
			}else{
				moTasksList = new GenericMoTaskVoDAO().findMoTaskVo(moTaskVo, pageInfo,corpcode);
			}
		}catch(Exception e){
			EmpExecutionContext.error(e,"上行实时记录异常");
			throw e;
		}
		//返回集合
		return moTasksList;
	}
	
	/**
	 * 上行获取通道号
	 */
	
    public List<SysMoMtSpgateVo> getDownHisVos(SysMoMtSpgateVo sysMoMtSpVo) throws Exception
	{
		List<SysMoMtSpgateVo> downHisVosList;
		try
		{			
			downHisVosList = new GenericMoMtSpgateVoDAO().findSysMoMtSpVoVo(sysMoMtSpVo);
			
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取通道号异常");
			throw e;
		}
		return downHisVosList;
	}	
	
	
	/**
	 * 下行记录
	 * @param curLongedUserId
	 * @param sysMoMtSpVo
	 * @return
	 * @throws Exception
	 */
	
    public List<SysMoMtSpgateVo> getSpgateVos(SysMoMtSpgateVo sysMoMtSpVo) throws Exception
	{
		List<SysMoMtSpgateVo> downHisVosList;
		try
		{			
			downHisVosList = new GenericMoMtSpgateVoDAO().findSysMtSpVoVo(sysMoMtSpVo);
			
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "下行记录获取通道号异常");
			throw e;
		}
		return downHisVosList;
	}	
	
	
	/**
	 * 上行记录中的实时记录导出方法
	 * 
	 * @param moTask
	 * @param pageInfo
	 * @return
	 */
	
    public Map<String, String> createMoTaskTimeExcel(MoTaskVo moTask,
                                                     PageInfo pageInfo, Integer isHidePhone, String corpcode, HttpServletRequest request) {
		String BASEDIR=new TxtFileUtil().getWebRoot()+"cxtj/query/file";
		String voucherPath = "download";
		
		String voucherTemplatePath = BASEDIR + File.separator + "temp"
				+ File.separator + "r_sysMoRecord.xlsx";// excel模板地址

		Map<String, String> resultMap = new HashMap<String, String>();

		//查询数据List
		List<MoTaskVo> moTaskList=null;
		//导出文件名
		String fileName = null;
		//导出文件路径
		String filePath = null;
		//读取Excel模板workbook
		XSSFWorkbook workbook1 = null;
		//写入Excel的workbook（大数据量）
		SXSSFWorkbook workbook = null;
		//定义workbook的 sheet
		Sheet sheet=null;
		//定义workbook的行
		Row row = null;
		//样式
		XSSFCellStyle cellStyle=null;
		//输出流（用于写入生成excel文件）
		OutputStream os = null;
		//输入流（用于读取模板）
		InputStream in = null;
		try {
			// 设置每个excel文件的行数
			pageInfo.setPageSize(10000);
			// 获得第一页的查询数据  初始化pageinfo
			moTaskList = getUpInfo(moTask, pageInfo,corpcode);
			//判断查询有没有结果集
			if (moTaskList == null || moTaskList.size() == 0) {
				return null;
			}
			//定义循环需要的变量值
			//每个excel的写入行记录变量
			int i =0;
			//文件个数
			int f=1;
			//定义模板输入流创建excel workbook
			// 创建只读的Excel工作薄的对象, 此为模板文件
			File file = new File(voucherTemplatePath);
			//判断模板文件是否存在
			String language  = (String) request.getSession().getAttribute(StaticValue.LANG_KEY);
			if(!(file.exists()&&StaticValue.ZH_CN.equals(language))){
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
				XSSFCell[] cell = new XSSFCell[8];
				// 创建一个Excel的单元格
				cell[0] = xrow.createCell(0);
				cell[1] = xrow.createCell(1);
				cell[2] = xrow.createCell(2);
				cell[3] = xrow.createCell(3);
				cell[4] = xrow.createCell(4);
				cell[5] = xrow.createCell(5);
				cell[6] = xrow.createCell(6);
				cell[7] = xrow.createCell(7);
				// 设置单元格样式
				cell[0].setCellStyle(cellStyle2);
				cell[1].setCellStyle(cellStyle2);
				cell[2].setCellStyle(cellStyle2);
				cell[3].setCellStyle(cellStyle2);
				cell[4].setCellStyle(cellStyle2);
				cell[5].setCellStyle(cellStyle2);
				cell[6].setCellStyle(cellStyle2);
				cell[7].setCellStyle(cellStyle2);
				// 给Excel的单元格设置样式和赋值
				//cell[0].setCellValue("SP账号");
				//cell[1].setCellValue("通道号码");
				//cell[2].setCellValue("运营商");
				//cell[3].setCellValue("手机号码");
				//cell[4].setCellValue("姓名");
				//cell[5].setCellValue("接收时间");
				//cell[6].setCellValue("短信内容");
				//cell[7].setCellValue("编码");
				cell[0].setCellValue(MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_spzh", request));
				cell[1].setCellValue(MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_tdhm", request));
				cell[2].setCellValue(MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_yys", request));
				cell[3].setCellValue(MessageUtils.extractMessage("cxtj", "cxtj_new_sjhm", request));
				cell[4].setCellValue(MessageUtils.extractMessage("cxtj", "cxtj_new_xm", request));
				cell[5].setCellValue(MessageUtils.extractMessage("cxtj", "cxtj_new_jssj", request));
				cell[6].setCellValue(MessageUtils.extractMessage("cxtj", "cxtj_new_dxnr", request));
				cell[7].setCellValue(MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtsxjl_bm", request));
				FileOutputStream xos = new FileOutputStream(voucherTemplatePath);
				wb.write(xos);
				xos.close();
			}
			//定义第一个输出流
			//获取当前时间
			Date curDate = new Date();
			//定义日期时间格式
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddH24mmss");
			// 文件的存储路径
			String voucherFilePath = BASEDIR + File.separator + voucherPath
					+ File.separator + "sysMoRecord" + File.separator
					+ sdf.format(curDate);
			//输出晚间路径定义文件
			File fileTemp = new File(voucherFilePath);
			//创建输出文件目录
			if (!fileTemp.exists()) {
				fileTemp.mkdirs();
			}
			//取得行数
			int size =moTaskList.size();
			//定义单元格
			Cell[] cell = new Cell[8];

			//循环分页数
			for (int j=0; j < pageInfo.getTotalPage(); j++) {
				
				if (j > 0)// 第一次时不需要再查询，因为前面已经查询出第一页
				{
					moTaskList = getUpInfo(moTask, pageInfo,corpcode);
					size=moTaskList.size();
				}
				
				pageInfo.setPageIndex(j + 2);// 定位下一页
				
				for(int k=0;k<size;k++){
					//如果等于0则创建一个workbook
					if(i==0){
						//读取模板文件为文件输入流
						in = new FileInputStream(file);
						//通过模板输入流创建excel workbook
						workbook1 = new XSSFWorkbook(in);
						//设置workbook sheet 名称
						//workbook1.setSheetName(0, "系统上行记录");
						workbook1.setSheetName(0, MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtsxjl_title", request));
						//通过流创建完 workbook 即可关闭输入流
						in.close();
						//定义Excel单元格样式
						cellStyle = new ExcelTool().setCellStyle(workbook1);
						//通过读取的模板文件创建的  workbook  定义一个大数据量的写入workbook
						workbook = new SXSSFWorkbook(workbook1,10000);
						//获取大数据量写入workbook的sheet
						sheet=workbook.getSheetAt(0);
						//输出文件名称
						fileName = "sysMoRecord_" + sdf.format(curDate) + "["+f+"]"+"_"+ StaticValue.getServerNumber() +".xlsx";
						f++;
					}
					MoTaskVo mv = (MoTaskVo) moTaskList.get(k);

					String userid = mv.getUserId();
					String sp = mv.getSpnumber();
					String unicom = (mv.getUnicom() - 0 == 0) ? MessageUtils.extractMessage("cxtj", "cxtj_sjcx_dxzljl_yd", request) : (mv
							.getUnicom() - 1 == 0 ? MessageUtils.extractMessage("cxtj", "cxtj_sjcx_dxzljl_lt", request)
							: (mv.getUnicom() - 21 == 0 ? MessageUtils.extractMessage("cxtj", "cxtj_sjcx_dxzljl_dx", request)  
							: (mv.getUnicom() - 5 == 0 ? MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_gw", request):"-")));
					String phone = mv.getPhone();
					if (phone != null && !"".equals(phone) && isHidePhone == 0) {
						phone = new CommonVariables().replacePhoneNumber(phone);
					}
					SimpleDateFormat s = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					String deliverTime = s.format(mv.getDeliverTime());
					String content = mv.getMsgContent();
					String msg = mv.getMsgFmt().toString();

					row = sheet.createRow(i + 1);
					// 生成四个单元格
					cell[0] = row.createCell(0);
					cell[1] = row.createCell(1);
					cell[2] = row.createCell(2);
					cell[3] = row.createCell(3);
					cell[4] = row.createCell(4);
					cell[5] = row.createCell(5);
					cell[6] = row.createCell(6);
					cell[7] = row.createCell(7);

					// 设置单元格样式
					cell[0].setCellStyle(cellStyle);
					cell[1].setCellStyle(cellStyle);
					cell[2].setCellStyle(cellStyle);
					cell[3].setCellStyle(cellStyle);
					cell[4].setCellStyle(cellStyle);
					cell[5].setCellStyle(cellStyle);
					cell[6].setCellStyle(cellStyle);
					cell[7].setCellStyle(cellStyle);
					
					
					// 设置单元格内容
					cell[0].setCellValue(userid);
					cell[1].setCellValue(sp);
					cell[2].setCellValue(unicom);
					cell[3].setCellValue(phone);
					cell[4].setCellValue(mv.getName()==null?"-": mv.getName());
					cell[5].setCellValue(deliverTime);
					cell[6].setCellValue(content);
					cell[7].setCellValue(msg);
					
					i++;// clientvosList索引加1
					//当条数达到文件最大数量值则写文件
					if(i==500000){
						// 输出到xlsx文件
						os = new FileOutputStream(voucherFilePath + File.separator
								+ fileName);
						workbook.write(os);
						os.close();
						workbook=null;
						i=0;
					}
				}
				moTaskList.clear();
				moTaskList = null;
			}				
			if(i!=0){
				// 输出到xlsx文件
				os = new FileOutputStream(voucherFilePath + File.separator
						+ fileName);
				// 写入Excel对象
				workbook.write(os);
				os.close();
				workbook=null;
			}
			// 清除对象
			fileName = MessageUtils.extractMessage("cxtj", "cxtj_new_rpt_xtsxbb", request) + sdf.format(curDate) + "_"+ StaticValue.getServerNumber() + ".zip";
			filePath = BASEDIR + File.separator + voucherPath + File.separator
					+ "sysMoRecord" + File.separator + fileName;

			ZipUtil.compress(voucherFilePath, filePath);
			boolean status = FileUtils.deleteDir(fileTemp);
			if (!status) {
			    EmpExecutionContext.error("刪除文件失敗");
            }
		} catch (Exception e) {
			EmpExecutionContext.error(e,"上行记录中的实时记录导出异常");
		}
		resultMap.put("FILE_NAME", fileName);
		resultMap.put("FILE_PATH", filePath);
		resultMap.put("SIZE", pageInfo.getTotalRec()+"");		
		return resultMap;
	}
}
