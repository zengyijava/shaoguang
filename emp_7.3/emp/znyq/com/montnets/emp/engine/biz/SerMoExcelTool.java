package com.montnets.emp.engine.biz;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.engine.vo.LfMoServiceVo;
import com.montnets.emp.util.ZipUtil;
import org.apache.poi.ss.usermodel.FontUnderline;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @project emp_std_192.169.1.81
 * @author huangzb
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2015-8-21 上午11:00:52
 * @description 上行业务记录导出excel的biz
 */
public class SerMoExcelTool 
{

	// 报表模板目录
	private String BASEDIR = "";

	// 操作员报表文件路径
    private String voucherTemplatePath = "";

	// 发送账号下行报表文件路径
    private final String spvoucherTemplatePath = "";

	// 产生下行报表路径
	//public String voucherPath = "voucher";
    private final String voucherPath = "download";

    private final String SAMPLES = "samples";

    private final String TEMP = "temp";

	public SerMoExcelTool(String context) 
	{
		BASEDIR = context;
	}

	/**
	 * 上行业务记录导出excel
	 * @param list
	 * @return
	 */
	public Map<String, String> createSerMoExcel(String langName,List<LfMoServiceVo> list)
	{
		XSSFWorkbook workbook = null;
		FileInputStream in = null;
		FileOutputStream os = null;
		try 
		{
			String execlName = "SerMoRecord";

			//voucherTemplatePath = BASEDIR + File.separator + TEMP
			//		+ File.separator + "eng_serMoRecord.xlsx";

			if(StaticValue.ZH_HK.equals(langName)){
				voucherTemplatePath = BASEDIR + File.separator + TEMP
				+ File.separator + "eng_serMoRecord-zh_HK.xlsx";
			}else if(StaticValue.ZH_TW.equals(langName)){
				voucherTemplatePath = BASEDIR + File.separator + TEMP
						+ File.separator + "eng_serMoRecord-zh_TW.xlsx";
			}else{
				voucherTemplatePath = BASEDIR + File.separator + TEMP
						+ File.separator + "eng_serMoRecord.xlsx";
			}

			
			java.text.SimpleDateFormat df = new java.text.SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");

			Map<String, String> resultMap = new HashMap<String, String>();

			// 当前每页分页条数
			int intRowsOfPage = 500000;

			// 当前每页显示条数
			int intPagesCount = (list.size() % intRowsOfPage == 0) ? (list
					.size() / intRowsOfPage) : (list.size() / intRowsOfPage + 1);

			int size = intPagesCount; // 生成的工作薄个数

			EmpExecutionContext.info("上行业务记录导出，工作表个数：" + size+"， 记录数：" + list.size());
			if (size == 0) {
				EmpExecutionContext.error("上行业务记录导出失败。无数据！");
				return null;
			}

			// 产生报表文件的存储路径
			Date curDate = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddH24mmss");
			String voucherFilePath = BASEDIR + File.separator + voucherPath+File.separator
									+"groupHistory"+File.separator+sdf.format(curDate);
			
			File fileTemp = new File(voucherFilePath);
			if(!fileTemp.exists())
			{
				fileTemp.mkdirs();
			}
			// 报表文件名
			String fileName;
			String filePath;
			
			// 创建只读的Excel工作薄的对象, 此为模板文件
			File file = new File(voucherTemplatePath);
			for (int j = 1; j <= size; j++) 
			{
				fileName =execlName +"_"+sdf.format(curDate)+"_["+ j +"]"+"_"+ StaticValue.getServerNumber() +".xlsx";
				
				in = new FileInputStream(file);
				workbook = new XSSFWorkbook(in);
				
				XSSFCellStyle cellStyle = setCellStyle(workbook);
	
				XSSFSheet sheet = workbook.cloneSheet(0);
				
				workbook.removeSheetAt(0);
				workbook.setSheetName(0, execlName);
				// 读取模板工作表
				XSSFRow row = null;
			
				// 总体流程
				// 根据记录，计算总的页数
				// 每页的处理 一页一个sheet
				// 写入页标题
				// 循环写入该页的行数，一次写一行，每页定义了多少行，就写多少数据。直到数据没有。
				// 写页尾

				int intCnt = list.size() < j * intRowsOfPage ? list
						.size() : j * intRowsOfPage;
						
				XSSFCell[] cell = new XSSFCell[10];

				for (int k = 0; k < intCnt; k++) 
				{
					LfMoServiceVo serMoTask = list.get(k);
					 
					// 发送时间
					String deliverTime = serMoTask.getDeliverTime() == null ? "" : df.format(serMoTask.getDeliverTime());
					String phone = serMoTask.getPhone();
					String clientName = serMoTask.getClientName()==null?"-":serMoTask.getClientName();
					String msgContent = serMoTask.getMsgContent()==null?"-":serMoTask.getMsgContent();
					//String replyStateStr = serMoTask.getReplyState()-1==0?"成功":serMoTask.getReplyState()-3==0?"失败":"未回复";

					String replyStateStr = null;
					if(StaticValue.ZH_HK.equals(langName)){
						replyStateStr = serMoTask.getReplyState()-1==0?"success":serMoTask.getReplyState()-3==0?"failure":"Unanswered";
					}else if(StaticValue.ZH_TW.equals(langName)){
						replyStateStr = serMoTask.getReplyState()-1==0?"成功":serMoTask.getReplyState()-3==0?"失敗":"未回复";
					}else{
						replyStateStr = serMoTask.getReplyState()-1==0?"成功":serMoTask.getReplyState()-3==0?"失败":"未回复";

					}
					
					//业务编号
					String serId = serMoTask.getSerId()==null?"-":serMoTask.getSerId().toString();
					//业务名称
					String serName = serMoTask.getSerName()==null?"-":serMoTask.getSerName();
					String orderCode = serMoTask.getOrderCode()==null?"-":serMoTask.getOrderCode();
					String createrName = serMoTask.getCreaterName()==null?"-":serMoTask.getCreaterName();
					String spUser = serMoTask.getSpUser()==null?"-":serMoTask.getSpUser();
					
					row = sheet.createRow(k+1);
					for(int i =0;i<cell.length;i++){
						cell[i] = row.createCell(i);
						cell[i].setCellStyle(cellStyle);
					}
					cell[0].setCellValue(deliverTime);
					cell[1].setCellValue(phone);
					cell[2].setCellValue(clientName);
					cell[3].setCellValue(msgContent);
					cell[4].setCellValue(replyStateStr);
					cell[5].setCellValue(serId);
					cell[6].setCellValue(serName);
					cell[7].setCellValue(orderCode);
					cell[8].setCellValue(createrName);
					cell[9].setCellValue(spUser);
				
				}
				os = new FileOutputStream(voucherFilePath + File.separator + fileName);
				// 写入Excel对象
				workbook.write(os);
			}
			//fileName = "上行业务记录" + sdf.format(curDate) +"_"+StaticValue.SERVER_NUMBER + ".zip";

			if(StaticValue.ZH_HK.equals(langName)){
				fileName = "UpstreamBusinessRecords" + sdf.format(curDate) +"_"+ StaticValue.getServerNumber() + ".zip";
			}else if(StaticValue.ZH_TW.equals(langName)){
				fileName = "上行業務記錄" + sdf.format(curDate) +"_"+ StaticValue.getServerNumber() + ".zip";
			}else{
				fileName = "上行业务记录" + sdf.format(curDate) +"_"+ StaticValue.getServerNumber() + ".zip";
			}
			
			filePath = BASEDIR + File.separator + voucherPath + File.separator
					+ "groupHistory" + File.separator + fileName;

			ZipUtil.compress(voucherFilePath, filePath);
            boolean status = deleteDir(fileTemp);
            if (!status) {
                EmpExecutionContext.error("刪除文件失敗！");
            }
			resultMap.put("FILE_NAME", fileName);
			resultMap.put("FILE_PATH", filePath);
			return resultMap;
		}
		catch (Exception e) 
		{
			EmpExecutionContext.error(e,"上行业务记录导出，异常。");
			return null;
		}
		finally
		{
			try
			{
				// 清除对象
				if(os != null)
				{
					os.close();
				}
				if(in != null)
				{
					in.close();
				}
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "上行业务记录导出，关闭资源异常。");
			}
			workbook = null;
		}
	}
	
	/**
	 * 将设置单元格属性提取出来成一个方法，方便其他模块调用
	 * @param workbook
	 * @return
	 */
	private XSSFCellStyle setCellStyle(XSSFWorkbook workbook) 
	{
		try
		{
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
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置单元格格式，异常。");
			return null;
		}
	}
	
	private static boolean deleteDir(File dir) 
	{
		try
		{
			if(dir == null)
			{
				return false;
			}
			if (dir.isDirectory()) 
			{
				String[] children = dir.list();
				for (int i = 0; i < children.length; i++) 
				{
					boolean success = deleteDir(new File(dir, children[i]));
					if (!success) 
					{
						return false;
					}
				}
			}
			return dir.delete();
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "删除文件异常。fileName="+dir.getName()+",filepath=" + dir.getPath());
			return false;
		}
	}
}
