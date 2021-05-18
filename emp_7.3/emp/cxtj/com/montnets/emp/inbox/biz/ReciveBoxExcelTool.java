package com.montnets.emp.inbox.biz;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FontUnderline;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.montnets.emp.common.constant.CommonVariables;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.inbox.vo.LfMotaskVo1;
import com.montnets.emp.util.ZipUtil;


/**
 * 个人收件箱导出工具类
 * @author Administrator
 *
 */
public class ReciveBoxExcelTool {

	// 报表模板目录
	protected  String BASEDIR = "";

	// 操作员报表文件路径
	protected  String voucherTemplatePath = "";

	// 发送账号下行报表文件路径
	protected  String spvoucherTemplatePath = "";

	// 产生下行报表路径
	//public String voucherPath = "voucher";
	protected  String voucherPath = "download";

	protected String SAMPLES = "samples";

	protected String TEMP = "temp";

	private final CommonVariables cv;

	public ReciveBoxExcelTool(String context) {

		BASEDIR = context;
		cv = new CommonVariables();

	}

	/**
	 * 个人收件箱导出Excel
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 */
    public Map<String, String> createReciveBoxExcel(List<LfMotaskVo1> mtdList,
                                                    LinkedHashMap<Long, String> depMap, Integer isHidePhone, String corpCode, HttpServletRequest request)
			throws Exception {
		//String execlName = "个人收件箱";
		String execlName = "r_receive";
		
		String filePath = null;
		
		String langName = (String)request.getSession().getAttribute(StaticValue.LANG_KEY);
		// 个人收件箱导出模板
		voucherTemplatePath = BASEDIR + File.separator + TEMP
				+ File.separator + "inb_receive_"+langName+".xlsx";

		Map<String, String> resultMap = new HashMap<String, String>();

		// 当前每页分页条数
		int intRowsOfPage = 500000;

		// 当前每页显示条数
		int intPagesCount = (mtdList.size() % intRowsOfPage == 0) ? (mtdList
				.size() / intRowsOfPage) : (mtdList.size() / intRowsOfPage + 1);

		int size = intPagesCount; // 生成的工作薄个数

		EmpExecutionContext.info("个人收件箱导出工作表个数：" + size);
		if (size == 0) {
			EmpExecutionContext.info("无个人收件箱数据！");
			throw new Exception("无个人收件箱数据！");
		}

		// 产生报表文件的存储路径
		Date curDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddH24mmss");
		String voucherFilePath = BASEDIR + File.separator + voucherPath+File.separator+"receiveBox"+File.separator+sdf.format(curDate);
		File fileTemp = new File(voucherFilePath);
		if(!fileTemp.exists()) {
            fileTemp.mkdirs();
        }
		
		
		XSSFWorkbook workbook1 = null;
		SXSSFWorkbook workbook = null;
		
		InputStream in = null;
		OutputStream os = null;
		
		String fileName = null;
		
		try {
			// 创建只读的Excel工作薄的对象, 此为模板文件
			File file = new File(voucherTemplatePath);
			
			for(int f = 0; f< size;f++){
				//文件名
				fileName = execlName+"_"+ sdf.format(curDate) + "_[" + (f+1) + "]"+"_"+ StaticValue.getServerNumber() +".xlsx";
				//读取模板
				in = new FileInputStream(file);
				// 工作表
				workbook1 = new XSSFWorkbook(in);
				workbook1.setSheetName(0, execlName);
				EmpExecutionContext.debug("工作薄创建与模板移除成功!");
				workbook = new SXSSFWorkbook(workbook1,10000);
				Sheet sheet=workbook.getSheetAt(0);
				// 表格样式
				XSSFCellStyle cellStyle = setCellStyle(workbook1);
				in.close();
				// 读取模板工作表
				Row row = null;
				int index = 0;
				
				Map<String, String> phoneNameMap = new LinkedHashMap<String, String>();
				if(mtdList!=null && mtdList.size()>0)
				{
					StringBuffer bf = new StringBuffer();
					for(int i = 0 ;i<mtdList.size();i++)
					{
						bf.append("'"+mtdList.get(i).getPhone()+"'");
						if(i!=mtdList.size()-1)
						{
							bf.append(",");
						}
					}
//					LinkedHashMap<String, String> conditionMap2 = new LinkedHashMap<String, String>();
//					conditionMap2.put("mobile&in", bf.toString());
//					
//					List<LfClient> liclients =  new BaseBiz().getByCondition(LfClient.class, conditionMap2, null);
//					if(liclients!=null && liclients.size()>0){
//						for (int j = 0; j < liclients.size(); j++) 
//						{
//							phoneNameMap.put(liclients.get(j).getMobile(), liclients.get(j).getName());
//						}
//					}
					ReciveBoxBiz mtBiz=new ReciveBoxBiz();
					mtBiz.getEmpMapByMobiles(phoneNameMap,bf.toString(),corpCode);
					mtBiz.getCliMapByMobiles(phoneNameMap,bf.toString(),corpCode);
				}
				
				Cell[] cell = new Cell[9];
				
				for (int k = 0; k < mtdList.size(); k++) {

					LfMotaskVo1 mt = (LfMotaskVo1) mtdList.get(k);

					// 操作员
					String userName = mt.getName();
					// 机构
					String depName = depMap == null ? "-" : depMap.get(mt
							.getSysdepId());
					// SP账号
					String spUser = mt.getSpUser();
					// 通道账号
					String spNumber = mt.getSpnumber();
					// 手机号
					String phone = mt.getPhone() != null ? mt.getPhone() : "-";

					if (phone != null && !"-".equals(phone) && isHidePhone == 0) {
						phone = cv.replacePhoneNumber(phone);
					}
					// 发送时间
					SimpleDateFormat df = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					String sendTime = df.format(mt.getDeliverTime());
					// 短信文件
					String msg = mt.getMsgContent() != null ? mt
							.getMsgContent() : "-";
					// 内容格式
					String format = mt.getMsgFmt() == null ? "-"
							: mt.getMsgFmt().toString();
					
					row = sheet.createRow(k+1);
					
					//姓名
					String nameStr = "-";
					
					if(mt.getEmployeeName()!=null && !"".equals(mt.getEmployeeName())){
						nameStr = mt.getEmployeeName();
					}else if(phoneNameMap != null && phoneNameMap.size()>0){
						String str=phoneNameMap.get(mt.getPhone());
						if(str!=null && !"".equals(str))
						{
							nameStr = str;
						}
					}
					
					cell[0] = row.createCell(0);
					cell[1] = row.createCell(1);
					cell[2] = row.createCell(2);
					cell[3] = row.createCell(3);
					cell[4] = row.createCell(4);
					cell[5] = row.createCell(5);
					cell[6] = row.createCell(6);
					cell[7] = row.createCell(7);
					cell[8] = row.createCell(8);
					
					
					cell[0].setCellStyle(cellStyle);
					cell[1].setCellStyle(cellStyle);
					cell[2].setCellStyle(cellStyle);
					cell[3].setCellStyle(cellStyle);
					cell[4].setCellStyle(cellStyle);
					cell[5].setCellStyle(cellStyle);
					cell[6].setCellStyle(cellStyle);
					cell[7].setCellStyle(cellStyle);
					cell[8].setCellStyle(cellStyle);
					
					cell[0].setCellValue(userName);
					cell[1].setCellValue(depName);
					cell[2].setCellValue(spUser);
					cell[3].setCellValue(spNumber);
					cell[4].setCellValue(phone);
					cell[5].setCellValue(nameStr);
					cell[6].setCellValue(sendTime);
					cell[7].setCellValue(msg);
					cell[8].setCellValue(format);
					// 一页里的行数
					index++;
				}
				os = new FileOutputStream(voucherFilePath + File.separator + fileName);
				// 写入Excel对象
				workbook.write(os);
				phoneNameMap.clear();
				phoneNameMap=null;
			}
			fileName = MessageUtils.extractMessage("cxtj", "cxtj_new_rpt_grsjx", request) + sdf.format(curDate)+"_"+ StaticValue.getServerNumber() + ".zip";
			filePath = BASEDIR + File.separator + voucherPath + File.separator
					+ "receiveBox" + File.separator + fileName;

			ZipUtil.compress(voucherFilePath, filePath);
            boolean flag = deleteDir(fileTemp);
            if (!flag) {
                EmpExecutionContext.error("刪除文件失敗！");
            }
		} catch (Exception e) {
			EmpExecutionContext.error(e,"个人收件箱导出异常");
		}finally{
			// 清除对象
			if(os != null){
				try{
					os.close();
				}catch(Exception e){
					EmpExecutionContext.error(e,"关闭资源异常");
				}
			}
			workbook = null;
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
