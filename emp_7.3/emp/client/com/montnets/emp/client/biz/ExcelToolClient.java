package com.montnets.emp.client.biz;

import com.montnets.emp.client.dao.GenericLfClientVoDAO;
import com.montnets.emp.client.vo.LfClientVo;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.util.FileUtils;
import com.montnets.emp.util.GetSxCount;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.ZipUtil;
import org.apache.commons.beanutils.DynaBean;
import org.apache.poi.ss.usermodel.FontUnderline;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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


public class ExcelToolClient{

	public static final String SAMPLES = "samples";
	private String BASEDIR = "";
	private final GenericLfClientVoDAO lfClientVoDAO;
	private String voucherTemplatePath = "";
	public static final String voucherPath = "voucher";
	
	public ExcelToolClient(String context) {
		BASEDIR = context;
		lfClientVoDAO = new GenericLfClientVoDAO();
	}

	public String getBASEDIR() {
		return BASEDIR;
	}

	public void setBASEDIR(String BASEDIR) {
		this.BASEDIR = BASEDIR;
	}

	public String getVoucherTemplatePath() {
		return voucherTemplatePath;
	}

	public void setVoucherTemplatePath(String voucherTemplatePath) {
		this.voucherTemplatePath = voucherTemplatePath;
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

	// 将设置单元格属性提取出来成一个方法，方便其他模块调用
	
    public XSSFCellStyle[] setLastCellStyle(XSSFWorkbook workbook) {

		XSSFCellStyle cellStyle1 = workbook.createCellStyle();
		XSSFCellStyle cellStyle2 = workbook.createCellStyle();

		XSSFFont font1 = workbook.createFont();
		XSSFFont font2 = workbook.createFont();
		// 字体名称
		font1.setFontName("TAHOMA");
		font2.setFontName("TAHOMA");
		// 粗体
		font1.setBold(false);
		font2.setBold(true);
		// 字体大小
		font1.setFontHeight(11);
		font2.setFontHeight(11);
		cellStyle1.setFont(font1);
		cellStyle2.setFont(font2);
		// 水平对齐
		cellStyle1.setAlignment(HorizontalAlignment.CENTER);
		cellStyle2.setAlignment(HorizontalAlignment.CENTER);
		// 竖直对齐
		cellStyle1.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle2.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle1.setWrapText(true);
		cellStyle2.setWrapText(true);

		return new XSSFCellStyle[] { cellStyle1, cellStyle2 };
	}

	/**
	 * 批量导出客户通讯录excel，单个excel文件数量为50万条，压缩成zip包
	 * 
	 * @param loginUserId
	 *            当前登录操作员
	 * @param clientVo
	 *            查询条件
	 * @param corpcode
	 *            企业编码
	 * @param pageInfo
	 *            分页信息
	 * @return 返回文件名和文件路径map
	 * @throws Exception
	 */
	private final ClientAddrBookBiz clientBiz = new ClientAddrBookBiz();
	
    public Map<String, String> createClientExcelRar(Long loginUserId,
                                                    LfClientVo clientVo, String corpcode, PageInfo pageInfo, HttpServletRequest request)
			throws Exception {
		HttpSession session = request.getSession();
		String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
		
		String khtxl = com.montnets.emp.i18n.util.MessageUtils.extractMessage("client", "client_khtxlgl_kftxl_text_addressbook", request);
		
		// excel模板地址
		voucherTemplatePath = BASEDIR+ File.separator +"temp"+File.separator+ "a_clientAddrBook_"+langName+".xlsx";
		Map<String, String> resultMap = new HashMap<String, String>();
		String sx = GetSxCount.getInstance().getCount();
		String filePath = null;
		// 文件名
		String fileName = null;
		int size = 1; // 生成的工作薄个数
		XSSFWorkbook workbook = null;
		InputStream in = null;
		OutputStream os = null;
		int totalCount=0;
		try {
			// 当前没页显示条数
			int intRowsOfPage = 50000;
			
			List<DynaBean> clientvosList = clientBiz.getClientVocd(loginUserId,
					clientVo, corpcode, null);
			if (clientvosList == null || clientvosList.size() == 0) {
				return null;
			}
			size = (clientvosList.size() % intRowsOfPage == 0) ? (clientvosList
					.size() / intRowsOfPage) : (clientvosList.size() / intRowsOfPage + 1); 
			totalCount=clientvosList.size();
			EmpExecutionContext.info("工作表个数：" + size);

			Date curDate = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddH24mmss");
			// 文件的存储路径
			String voucherFilePath = BASEDIR + File.separator + voucherPath
					+ File.separator + "clientaddrbook" + File.separator
					+ sdf.format(curDate)+"_"+sx;
			File file1 = new File(voucherFilePath);
			if(!file1.exists()) {
                file1.mkdirs();
            }
			// 模板文件
			File file = new File(voucherTemplatePath);
			if(!file.exists()){
				writeExcel(voucherTemplatePath,request);
				file = new File(voucherTemplatePath);
			}
			for (int f = 0; f < size; f++) {
				fileName = "ClientAddrbook_" + sdf.format(curDate) +"_"+sx+ "_[" + (f+1)
						+ "].xlsx";
				// 读取模板
				in = new FileInputStream(file);
				// 工作表
				workbook = new XSSFWorkbook(in);
				// 表格样式
				XSSFCellStyle cellStyle = setCellStyle(workbook);

				XSSFSheet sheet = workbook.cloneSheet(0);
				workbook.removeSheetAt(0);
				workbook.setSheetName(0, khtxl);
				// 读取模板工作表
				XSSFRow row = null;
				EmpExecutionContext.debug("工作薄创建与模板移除成功!");
				DynaBean client = null;
				XSSFCell cell1 = null;
				XSSFCell cell2 = null;
				XSSFCell cell3 = null;
				XSSFCell cell4 = null;
				int intCnt = clientvosList.size() < (f+1) * intRowsOfPage ? clientvosList
						.size() : (f+1) * intRowsOfPage;
				for (int k = f * intRowsOfPage; k < intCnt; k++) {
					
					client =  clientvosList.get(k);
					row = sheet.createRow((k + 1)-f*intRowsOfPage);
					// 生成四个单元格
					cell1 = row.createCell(0);
					cell2 = row.createCell(1);
					cell3 = row.createCell(2);
					cell4 = row.createCell(3);

					// 设置单元格样式
					cell1.setCellStyle(cellStyle);
					cell2.setCellStyle(cellStyle);
					cell3.setCellStyle(cellStyle);
					cell4.setCellStyle(cellStyle);
					// 设置单元格内容
					
					if(client.get("client_code") != null && !"".equals(client.get("client_code"))){
						cell1.setCellValue(client.get("client_code").toString());
					}else{
						cell1.setCellValue("-");
					}
					
					cell2.setCellValue(client.get("name")!=null?client.get("name").toString():"-");
					cell3.setCellValue(client.get("mobile")!=null?client.get("mobile").toString():"-");
					cell4.setCellValue(client.get("dep_name")!=null?client.get("dep_name").toString():"-");
				}
				// 输出到xlsx文件
				os = new FileOutputStream(voucherFilePath + File.separator + fileName);
				// 写入Excel对象
				workbook.write(os);
			}
			// 清除对象
			clientvosList.clear();
			clientvosList = null;
			fileName = khtxl + sdf.format(curDate) +"_"+sx+ ".zip";
			filePath = BASEDIR + File.separator + voucherPath + File.separator
					+ "clientaddrbook" + File.separator + fileName;
			// 压缩文件夹
			ZipUtil.compress(voucherFilePath, filePath);
			// 删除文件夹
            boolean status = FileUtils.deleteDir(file1);
            if (!status) {
                EmpExecutionContext.error("刪除文件失敗！");
            }
		} catch (Exception e) {
			EmpExecutionContext.error(e,"客户导出出现异常！");
		} finally {
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
		}
		resultMap.put("FILE_NAME", fileName);
		resultMap.put("FILE_PATH", filePath);
		resultMap.put("totalCount", String.valueOf(totalCount));
		return resultMap;
	}
	
	/**
	 * 批量导出客户通讯录excel，单个excel文件数量为50万条，压缩成zip包
	 * 
	 * @param loginUserId
	 *            当前登录操作员
	 * @param clientVo
	 *            查询条件
	 * @param corpcode
	 *            企业编码
	 *  @param isExitAPP
	 *            判断是否存在APP模块
	 * @return 返回文件名和文件路径map
	 * @throws Exception
	 */
	
    public Map<String, String> createClientExcelRar(Long loginUserId,
                                                    LfClientVo clientVo, String corpcode, PageInfo pageInfo, LinkedHashMap<String, String> conditionMap, HttpServletRequest request)
			throws Exception {
		
		HttpSession session = request.getSession();
		String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
		
		String khtxl = com.montnets.emp.i18n.util.MessageUtils.extractMessage("client", "client_khtxlgl_kftxl_text_addressbook", request);
		// excel模板地址
		voucherTemplatePath = BASEDIR+ File.separator +"temp"+File.separator+ "a_clientAddrAppBook_"+langName+".xlsx";
		Map<String, String> resultMap = new HashMap<String, String>();
		//为了进一步区别文件
		String sx = GetSxCount.getInstance().getCount();
		String filePath = null;
		// 文件名
		String fileName = null;
		int size = 1; // 生成的工作薄个数
		XSSFWorkbook workbook = null;
		InputStream in = null;
		OutputStream os = null;
		int totalCount=0;
		try {
			// 当前没页显示条数
			int intRowsOfPage = 50000;
			
			List<DynaBean> clientvosList = clientBiz.getClientVocd(loginUserId,
					clientVo, corpcode, null,conditionMap);
			if (clientvosList == null || clientvosList.size() == 0) {
				return null;
			}
			size = (clientvosList.size() % intRowsOfPage == 0) ? (clientvosList
					.size() / intRowsOfPage) : (clientvosList.size() / intRowsOfPage + 1); 
			totalCount=clientvosList.size();
			EmpExecutionContext.info("客户通讯录工作表个数：" + size);

			Date curDate = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddH24mmss");
			// 文件的存储路径
			String voucherFilePath = BASEDIR + File.separator + voucherPath
					+ File.separator + "clientaddrAppbook" + File.separator
					+ sdf.format(curDate)+"_"+sx;
			File file1 = new File(voucherFilePath);
			if(!file1.exists()) {
                file1.mkdirs();
            }
			// 模板文件
			File file = new File(voucherTemplatePath);
			if(!file.exists()){
				writeAPPExcel(voucherTemplatePath,request);
				file = new File(voucherTemplatePath);
			}
			for (int f = 0; f < size; f++) {
				fileName = "ClientAddApprbook_" + sdf.format(curDate) +"_"+sx+ "_[" + (f+1)
						+ "].xlsx";
				// 读取模板
				in = new FileInputStream(file);
				// 工作表
				workbook = new XSSFWorkbook(in);
				// 表格样式
				XSSFCellStyle cellStyle = setCellStyle(workbook);

				XSSFSheet sheet = workbook.cloneSheet(0);
				workbook.removeSheetAt(0);
				workbook.setSheetName(0, khtxl);
				// 读取模板工作表
				XSSFRow row = null;
				EmpExecutionContext.debug("客户通讯录工作薄创建与模板移除成功!");
				DynaBean client = null;
				XSSFCell cell1 = null;
				XSSFCell cell2 = null;
				XSSFCell cell3 = null;
				XSSFCell cell4 = null;
				XSSFCell cell5 = null;
				int intCnt = clientvosList.size() < (f+1) * intRowsOfPage ? clientvosList
						.size() : (f+1) * intRowsOfPage;
				for (int k = f * intRowsOfPage; k < intCnt; k++) {
					
					client =  clientvosList.get(k);
					row = sheet.createRow((k + 1)-f*intRowsOfPage);
					// 生成四个单元格
					cell1 = row.createCell(0);
					cell2 = row.createCell(1);
					cell3 = row.createCell(2);
					cell4 = row.createCell(3);
					cell5 = row.createCell(4);

					// 设置单元格样式
					cell1.setCellStyle(cellStyle);
					cell2.setCellStyle(cellStyle);
					cell3.setCellStyle(cellStyle);
					cell4.setCellStyle(cellStyle);
					cell5.setCellStyle(cellStyle);
					// 设置单元格内容
					
					if(client.get("client_code") != null && !"".equals(client.get("client_code"))){
						cell1.setCellValue(client.get("client_code").toString());
					}else{
						cell1.setCellValue("-");
					}
					
					cell2.setCellValue(client.get("name")!=null?client.get("name").toString():"-");
					cell3.setCellValue(client.get("mobile")!=null?client.get("mobile").toString():"-");
					cell4.setCellValue(client.get("app_code")!=null?client.get("app_code").toString():"未注册");
					cell5.setCellValue(client.get("dep_name")!=null?client.get("dep_name").toString():"-");
				}
				// 输出到xlsx文件
				os = new FileOutputStream(voucherFilePath + File.separator + fileName);
				// 写入Excel对象
				workbook.write(os);
			}
			// 清除对象
			clientvosList.clear();
			clientvosList = null;
			fileName = khtxl + sdf.format(curDate)+"_"+sx + ".zip";
			filePath = BASEDIR + File.separator + voucherPath + File.separator
					+ "clientaddrAppbook" + File.separator + fileName;
			// 压缩文件夹
			ZipUtil.compress(voucherFilePath, filePath);
			// 删除文件夹
            boolean status = FileUtils.deleteDir(file1);
            if (!status) {
                EmpExecutionContext.error("刪除文件失敗！");
            }
		} catch (Exception e) {
			EmpExecutionContext.error(e,"客户导出出现异常！");
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
		}
		resultMap.put("FILE_NAME", fileName);
		resultMap.put("FILE_PATH", filePath);
		resultMap.put("totalCount", String.valueOf(totalCount));
		return resultMap;
	}
	
	
	
	// 根据客户属性生成excel模板
	public  void writeAPPExcel(String path,HttpServletRequest request)
	{
		OutputStream os = null;
		try
		{
			String khh = com.montnets.emp.i18n.util.MessageUtils.extractMessage("client", "client_khtxlgl_kftxl_text_clientnumber", request);
			String name = com.montnets.emp.i18n.util.MessageUtils.extractMessage("client", "client_khtxlgl_kftxl_text_name", request);
			String phone = com.montnets.emp.i18n.util.MessageUtils.extractMessage("client", "client_khtxlgl_kftxl_text_phone", request);
			String ssjg = com.montnets.emp.i18n.util.MessageUtils.extractMessage("client", "client_khtxlgl_kftxl_text_affiliation", request);
			String appzh = com.montnets.emp.i18n.util.MessageUtils.extractMessage("client", "client_khtxlgl_kftxl_text_appaccount", request);
			os = new FileOutputStream(path);
			//工作区   
			XSSFWorkbook wb = new XSSFWorkbook(); 
			setBoldCellStyle(wb);
			//创建第一个sheet   
			XSSFSheet sheet= wb.createSheet("客户上传模板");   
			//生成第一行   
			XSSFRow row = sheet.createRow(0);   
			//给这一行的第一列赋值   
			row.createCell(0).setCellValue(khh);
			row.createCell(1).setCellValue(name);   
			row.createCell(2).setCellValue(phone);
			row.createCell(3).setCellValue(appzh);
			row.createCell(4).setCellValue(ssjg);

			
			//生成第二行   
			XSSFRow row2 = sheet.createRow(1);
			row2.createCell(0).setCellValue("");    
			row2.createCell(1).setCellValue("");   
			row2.createCell(2).setCellValue("");
			row2.createCell(3).setCellValue("");
			row2.createCell(4).setCellValue("");
			//写文件   
			wb.write(os);   
			//关闭输出流   
			os.close();  

		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"生成excel模板出现异常！");
		}finally {
			SysuserUtil.closeStream(os);
		}
}
	
	// 根据客户属性生成excel模板
	public  void writeExcel(String path,HttpServletRequest request)
	{
		OutputStream os = null;
		try
		{
			String khh = com.montnets.emp.i18n.util.MessageUtils.extractMessage("client", "client_khtxlgl_kftxl_text_clientnumber", request);
			String name = com.montnets.emp.i18n.util.MessageUtils.extractMessage("client", "client_khtxlgl_kftxl_text_name", request);
			String phone = com.montnets.emp.i18n.util.MessageUtils.extractMessage("client", "client_khtxlgl_kftxl_text_phone", request);
			String ssjg = com.montnets.emp.i18n.util.MessageUtils.extractMessage("client", "client_khtxlgl_kftxl_text_affiliation", request);
			
			os = new FileOutputStream(path);
			//工作区   
			XSSFWorkbook wb = new XSSFWorkbook(); 
			setBoldCellStyle(wb);
			//创建第一个sheet   
			XSSFSheet sheet= wb.createSheet("客户上传模板");   
			//生成第一行   
			XSSFRow row = sheet.createRow(0);   
			//给这一行的第一列赋值   
			row.createCell(0).setCellValue(khh);
			row.createCell(1).setCellValue(name);   
			row.createCell(2).setCellValue(phone);
			row.createCell(3).setCellValue(ssjg);

			
			//生成第二行   
			XSSFRow row2 = sheet.createRow(1);
			row2.createCell(0).setCellValue("");    
			row2.createCell(1).setCellValue("");   
			row2.createCell(2).setCellValue("");
			row2.createCell(3).setCellValue("");
			//写文件   
			wb.write(os);   
			//关闭输出流   
			os.close();  

		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"生成excel模板出现异常！");
		}finally {
			SysuserUtil.closeStream(os);
		}
}
	// 将设置单元格属性提取出来成一个方法，方便其他模块调用
	public XSSFCellStyle setBoldCellStyle(XSSFWorkbook workbook) {
		XSSFCellStyle cellStyle = workbook.createCellStyle();

		XSSFFont font = workbook.createFont();
		// 字体名称
		font.setFontName("TAHOMA");
		// 粗体
		font.setBold(true);
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
}