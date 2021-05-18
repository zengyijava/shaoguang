package com.montnets.emp.employee.biz;

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

import com.montnets.emp.common.constant.CommonVariables;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.employee.vo.LfEmployeeVo;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.ZipUtil;
/**
 * eqa_employeeAddrBook.xlsx
 * @author Administrator
 *
 */
public class EmployeeExcel {

	// 报表模板目录
	private String BASEDIR = "";

	// 操作员报表文件路径
    private String voucherTemplatePath = "";

	// 产生下行报表路径
	private final String voucherPath = "download";

	private final String SAMPLES = "samples";
	
	private final String TEMP = "temp";

	private final CommonVariables cv;

	public EmployeeExcel(String context) {

		BASEDIR = context;
		cv = new CommonVariables();
	}
	/**
	 * 员工通讯录导出
	 * @param loginUserId
	 * @param mv
	 * @param corpcode
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
    public Map<String, String> createEqaEmployeeAddrBookRar(Long loginUserId,
                                                            LfEmployeeVo mv, String corpcode, PageInfo pageInfo, HttpServletRequest request)
			throws Exception {
		// excel模板地址
		/*voucherTemplatePath = BASEDIR + File.separator + SAMPLES
				+ File.separator + "epl_employeeBook.xlsx";*/
		HttpSession session = request.getSession();
		String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
		voucherTemplatePath = BASEDIR  + File.separator + TEMP + File.separator + "epl_employeeBook_"+langName+".xlsx";
		Map<String, String> resultMap = new HashMap<String, String>();
		EmployeeBookBiz employeeBiz = new EmployeeBookBiz();
		String filePath = null;
		// 文件名
		String fileName = null;
		XSSFWorkbook workbook = null;
		InputStream in = null;
		OutputStream os = null;
		List<LfEmployeeVo> employeeBookList = null;
		
		
		try {
			pageInfo.setPageSize(50000);// 设置每个excel文件的行数
			employeeBookList = employeeBiz
					.findEmployeeVoByDepIds(loginUserId, corpcode, mv, pageInfo);
			if (employeeBookList == null || employeeBookList.size() == 0) {
				return null;
			}
			// 计算出文件数
			int fileCount = pageInfo.getTotalPage();

			Date curDate = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddH24mmss");
			// 文件的存储路径
			String voucherFilePath = BASEDIR + File.separator + voucherPath
					+ File.separator + "employeebook" + File.separator
					+ sdf.format(curDate);
			File file1 = new File(voucherFilePath);
			file1.mkdirs();
			//国际化
			String employeeNo = com.montnets.emp.i18n.util.MessageUtils.extractMessage("employee", "employee_dxzs_title_75", request);
			String name = com.montnets.emp.i18n.util.MessageUtils.extractMessage("employee", "employee_dxzs_title_76", request);
			String acclogin = com.montnets.emp.i18n.util.MessageUtils.extractMessage("employee", "employee_dxzs_title_77", request);
			String birthday = com.montnets.emp.i18n.util.MessageUtils.extractMessage("employee", "employee_dxzs_title_78", request);
			String sex = com.montnets.emp.i18n.util.MessageUtils.extractMessage("employee", "employee_dxzs_title_79", request);
			String dept = com.montnets.emp.i18n.util.MessageUtils.extractMessage("employee", "employee_dxzs_title_80", request);
			String zhiwei = com.montnets.emp.i18n.util.MessageUtils.extractMessage("employee", "employee_dxzs_title_81", request);
			String phone = com.montnets.emp.i18n.util.MessageUtils.extractMessage("employee", "employee_dxzs_title_82", request);
			//员工通讯录 
			String emplybook = com.montnets.emp.i18n.util.MessageUtils.extractMessage("employee", "employee_dxzs_title_111", request);
			String nan = com.montnets.emp.i18n.util.MessageUtils.extractMessage("employee", "employee_dxzs_title_11", request);
			String nv = com.montnets.emp.i18n.util.MessageUtils.extractMessage("employee", "employee_dxzs_title_12", request);
			String unknow = com.montnets.emp.i18n.util.MessageUtils.extractMessage("employee", "employee_dxzs_title_10", request);
			
			// 模板文件
			File file = new File(voucherTemplatePath);
			//判断模板文件是否存在
			if(!file.exists()){
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
				cell[0].setCellValue(employeeNo);
				cell[1].setCellValue(name);
				cell[2].setCellValue(acclogin);
				cell[3].setCellValue(birthday);
				cell[4].setCellValue(sex);
				cell[5].setCellValue(dept);
				cell[6].setCellValue(zhiwei);
				cell[7].setCellValue(phone);
				FileOutputStream xos = new FileOutputStream(voucherTemplatePath);
				wb.write(xos);
				xos.close();
			}
			

			for (int f = 0; f < fileCount; f++) {
				fileName = "Employeebook_" + sdf.format(curDate) + "_path_" + f
						+ "_"+ StaticValue.getServerNumber() +".xlsx";
				// 读取模板
				in = new FileInputStream(file);
				// 工作表
				workbook = new XSSFWorkbook(in);
				// 表格样式
				XSSFCellStyle cellStyle = setCellStyle(workbook);

				XSSFSheet sheet = workbook.cloneSheet(0);
				workbook.removeSheetAt(0);
				workbook.setSheetName(0, emplybook);
				// 读取模板工作表
				XSSFRow row = null;
				EmpExecutionContext.debug("工作薄创建与模板移除成功!");
				if (f > 0)// 第一次时不需要再查询，因为前面已经查询出第一页
				{
					/*employeeBookList = employeeBiz.findEmployeeVoByDepIds(
							loginUserId, corpcode, mv, pageInfo);*/
					employeeBookList = employeeBiz.findEmployeeVoByDepIds(
							loginUserId, corpcode, mv, pageInfo);
				}
				pageInfo.setPageIndex(f + 2);// 定位下一页
				LfEmployeeVo mvo = null;
				XSSFCell cell1 = null;
				XSSFCell cell2 = null;
				XSSFCell cell3 = null;
				XSSFCell cell4 = null;
				XSSFCell cell5 = null;
				XSSFCell cell6 = null;
				XSSFCell cell7 = null;
				XSSFCell cell8 = null;
				java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");

				for (int k = 0; k < employeeBookList.size(); k++) {
					mvo = employeeBookList.get(k);
					row = sheet.createRow(k + 1);
					// 生成四个单元格
					cell1 = row.createCell(0);
					cell2 = row.createCell(1);
					cell3 = row.createCell(2);
					cell4 = row.createCell(3);
					cell5 = row.createCell(4);
					cell6 = row.createCell(5);
					cell7 = row.createCell(6);
					cell8 = row.createCell(7);

					// 设置单元格样式
					cell1.setCellStyle(cellStyle);
					cell2.setCellStyle(cellStyle);
					cell3.setCellStyle(cellStyle);
					cell4.setCellStyle(cellStyle);
					cell5.setCellStyle(cellStyle);
					cell6.setCellStyle(cellStyle);
					cell7.setCellStyle(cellStyle);
					cell8.setCellStyle(cellStyle);
					// 设置单元格内容
					cell1.setCellValue(mvo.getEmployeeNo()==null?"-":mvo.getEmployeeNo());
					cell2.setCellValue(mvo.getName());
					String userName = mvo.getUserName();
					userName = userName == null || "".equals(userName)?"-":userName;
					cell3.setCellValue(userName);
					if(mvo.getBirthday() != null){
						cell4.setCellValue(df.format(mvo.getBirthday()));
			 		}else{
			 			cell4.setCellValue("-");
			 		}
					if(mvo.getSex() == 0){
						cell5.setCellValue(nv);
			 		}else if(mvo.getSex() == 1){
			 			cell5.setCellValue(nan);
			 		}else if(mvo.getSex() == 2){
			 			cell5.setCellValue(unknow);
			 		}else{
			 			cell5.setCellValue("-");
			 		}
					
					cell6.setCellValue(mvo.getDepName());
					if(!"".equals(mvo.getDutyName()) && mvo.getDutyName() != null){
						cell7.setCellValue(mvo.getDutyName().replaceAll("<","&lt;").replaceAll(">","&gt;"));
			 		}else{
			 			cell7.setCellValue("-");
			 		}
					cell8.setCellValue(mvo.getMobile());
				}
				// 输出到xlsx文件
				os = new FileOutputStream(voucherFilePath
						+ File.separator + fileName);
				// 写入Excel对象
				workbook.write(os);
			}
			fileName = emplybook + sdf.format(curDate) + ".zip";
			filePath = BASEDIR + File.separator + voucherPath + File.separator
					+ "employeebook" + File.separator + fileName;
			// 压缩文件夹
			ZipUtil.compress(voucherFilePath, filePath);
			// 删除文件夹
            boolean flag = deleteDir(file1);
            if (!flag) {
                EmpExecutionContext.error("刪除文件失敗！");
            }
		} catch (Exception e) {
			EmpExecutionContext.error(e,"员工通讯录excel出现异常！");
		} finally {
			// 清除对象
            try {
                if(os != null){
                    os.close();
                }
                if(in != null){
                    in.close();
                }
            } catch (Exception e) {
                EmpExecutionContext.error("流关闭异常！");
            }
			workbook = null;
			if (employeeBookList != null) {
				employeeBookList.clear();
				employeeBookList = null;
			}
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
