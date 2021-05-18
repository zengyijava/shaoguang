package com.montnets.emp.rms.report.biz;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FontUnderline;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.montnets.EMPException;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.rms.report.dao.OperatorDegreeRptDao;
import com.montnets.emp.rms.report.vo.MtDataReportVo;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.TxtFileUtil;
import com.montnets.emp.util.ZipUtil;

public class OperatorDegreeRptBiz {
    /**
     * 日报表
     */
    private final int DAY_REPORT = 0;
    /**
     * 月报表
     */
    private final int MONTH_REPORT = 1;
    /**
     * 年报表
     */
    private final int YEAR_REPORT = 2;
    private final OperatorDegreeRptDao dao = new OperatorDegreeRptDao();
    /**
     * 查询档位统计报表
     *
     * @param vo vo对象
     * @param pageInfo 分页对象
     * @return 包含结果的集合
     */
    public List<MtDataReportVo> getDegreeRpt(MtDataReportVo vo, PageInfo pageInfo) {
        List<MtDataReportVo> lfRmsReportVos = new ArrayList<MtDataReportVo>();
        try {
            setTimeCondition(vo);
            lfRmsReportVos = dao.findOperatorDegreeRpt(vo,pageInfo);
            for (MtDataReportVo rpt:lfRmsReportVos){
                //设置Y与IMONTH
                Integer iymd = rpt.getIymd();
                rpt.setY(Integer.parseInt(iymd.toString().substring(0,4)));
                rpt.setImonth(Integer.parseInt(iymd.toString().substring(4,6)));
            }
        }catch (Exception e){
            EmpExecutionContext.error(e,"企业富信>数据查询>档位报表统计查询异常");
        }
        return lfRmsReportVos;
    }
    /**
     * 档位统计报表导出功能
     * @param reportList vo的List对象
     * @return 包含文件名字与文件路径的Map
     */
    public HashMap<String, String> createRptExcelFile(List<MtDataReportVo> reportList, String corpCode, Boolean isDes, String isRptFlag, Map<String, String> languageMap) throws Exception{
        //excel名字
        String excelName = "RmsDegreeRpt";
        String baseDir = new TxtFileUtil().getWebRoot() + "rms/report/file";
        HashMap<String, String> resultMap = new HashMap<String, String>(16);
        // 当前每页分页条数
        int rowsOfPage = 500000;
        // 生成的工作薄个数
        int sheetSize = (reportList.size() % rowsOfPage == 0) ? (reportList.size() / rowsOfPage) : (reportList.size() / rowsOfPage + 1);
        // 产生报表文件的存储路径
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date();
        //实际生成文件路径
        String voucherFilePath = baseDir + File.separator + "download" + File.separator+"degreeRpt"+File.separator + sdf.format(curDate);
        //模板文件路径
        String voucherTemplatePath = baseDir + File.separator + "temp" + File.separator + "rms_degreeRecord.xlsx";
        File fileTemp = new File(voucherFilePath);
        if (!fileTemp.exists()) {
            boolean mkdirs = fileTemp.mkdirs();
            if(!mkdirs){
                throw new EMPException("企业富信>数据查询>档位统计报表>导出功能实现异常，无法创建对应文件夹！");
            }
        }
        // 创建只读的Excel工作薄的对象, 此为模板文件
        File file = new File(voucherTemplatePath);
        //无论是否存在，直接删除
        if(file.exists()){
            if(!file.delete()){
                throw new EMPException("企业富信>数据查询>档位统计报表>导出功能实现异常，无法删除对应文件夹！");
            }
        }
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
        // 竖直对齐
        cellStyle2.setVerticalAlignment(VerticalAlignment.CENTER);
        // 水平对齐
        cellStyle2.setAlignment(HorizontalAlignment.CENTER);
        cellStyle2.setWrapText(true);
        // 创建Excel的sheet的一行
        XSSFRow xrow = xsheet.createRow(0);
        XSSFCell[] cell = new XSSFCell[11];
        for (int i = 0; i < cell.length; i++) {
            // 创建一个Excel的单元格
            cell[i]=xrow.createCell(i);
            // 设置单元格样式
            cell[i].setCellStyle(cellStyle2);
        }

        // 给Excel的单元格设置样式和赋值
        //此处需要判断如果为10W企业则显示企业编码与企业名称
        cell[0].setCellValue(languageMap.get("time"));
        if("100000".equals(corpCode) && !isDes){
            cell[1].setCellValue(languageMap.get("corcode"));
            cell[2].setCellValue(languageMap.get("corname"));
            cell[3].setCellValue(languageMap.get("spaccount"));
            cell[4].setCellValue(languageMap.get("operator"));
            cell[5].setCellValue(languageMap.get("degree"));
            cell[6].setCellValue(languageMap.get("submits"));
            cell[7].setCellValue(languageMap.get("sends"));
            cell[8].setCellValue(languageMap.get("receives"));
            //if("3".equals(isRptFlag)){
            //	cell[9].setCellValue(languageMap.get("downloads"));
            //}
        }else {
            cell[1].setCellValue(languageMap.get("spaccount"));
            cell[2].setCellValue(languageMap.get("operator"));
            cell[3].setCellValue(languageMap.get("degree"));
            cell[4].setCellValue(languageMap.get("submits"));
            cell[5].setCellValue(languageMap.get("sends"));
            cell[6].setCellValue(languageMap.get("receives"));
            //if("3".equals(isRptFlag)){
            //    cell[7].setCellValue(languageMap.get("downloads"));
            //}
        }
        FileOutputStream xos = new FileOutputStream(voucherTemplatePath);
        wb.write(xos);
        xos.close();

        // 报表文件名
        String fileName = "";
        String zipName = null;
        String filePath = null;
        OutputStream os = null;
        XSSFWorkbook xssfWorkbook;
        SXSSFWorkbook sxssfWorkbook;
        for(int i = 0;i< sheetSize;i++){
            //文件名
            fileName = excelName +"_"+ sdf.format(curDate) + "_" + (i+1) + "_"+ StaticValue.getServerNumber() +".xlsx";
            //读取模板
            InputStream in = new FileInputStream(file);
            // 工作表
            xssfWorkbook = new XSSFWorkbook(in);
            sxssfWorkbook = new SXSSFWorkbook(xssfWorkbook,10000);
            Sheet sheet = sxssfWorkbook.getSheetAt(i);
            sxssfWorkbook.setSheetName(i, excelName);
            // 表格样式
            XSSFCellStyle cellStyle = setCellStyle(xssfWorkbook);
            in.close();
            // 读取模板工作表
            Row row;
            Cell[] cells;
            if("100000".equals(corpCode) && !isDes){
                cells = new Cell[10];
            }else {
                cells = new Cell[8];
            }
            Integer k = 0;
            Long icountSum = 0L;
            Long rsuccSum = 0L;
            Long rfail1Sum = 0L;
            Long dusuccSum = 0L;
            for(MtDataReportVo vo:reportList){
                row = sheet.createRow(k++ + 1);
                for(int j = 0;j < cells.length;j++){
                    cells[j] = row.createCell(j);
                    cells[j].setCellStyle(cellStyle);
                }
                Integer icount = vo.getIcount() == null ? 0:vo.getIcount();
                Integer rsucc = vo.getRsucc() == null ? 0:vo.getRsucc();
                Long rnret = vo.getRnret() == null ? 0:vo.getRnret();
                Integer rfail1 = vo.getRecfail() == null ? 0:vo.getRecfail();
                //Integer dwsucc = vo.getIcount() == null ? 0:vo.getDwsucc();
                Integer chgrade = vo.getChgrade()== null ? 0:vo.getChgrade();
                icountSum += icount;
                rsuccSum += rsucc + rnret;
                rfail1Sum += rfail1;
                //dusuccSum += dwsucc;
                cells[0].setCellValue(StringUtils.defaultIfEmpty(vo.getShowTime(),""));
                if("100000".equals(corpCode) && !isDes){
                    cells[1].setCellValue(StringUtils.defaultIfEmpty(vo.getCorpCode(),""));
                    cells[2].setCellValue(StringUtils.defaultIfEmpty(vo.getCorpName(),""));
                    cells[3].setCellValue(StringUtils.defaultIfEmpty(vo.getUserId(),""));
                    cells[4].setCellValue(StringUtils.defaultIfEmpty(vo.getSpisuncmName(),""));
                    cells[5].setCellValue(chgrade + "档");
                    cells[6].setCellValue(icount);
                    //成功数加未返数
                    cells[7].setCellValue((double)rsucc+rnret);
                    cells[8].setCellValue(rfail1);
                    //if("3".equals(isRptFlag)){
                    //    cells[9].setCellValue(dwsucc);
                    //}
                }else {
                    cells[1].setCellValue(StringUtils.defaultIfEmpty(vo.getUserId(),""));
                    cells[2].setCellValue(StringUtils.defaultIfEmpty(vo.getSpisuncmName(),""));
                    cells[3].setCellValue(chgrade + "档");
                    cells[4].setCellValue(icount);
                    //成功数加未返数
                    cells[5].setCellValue((double)rsucc+rnret);
                    cells[6].setCellValue(rfail1);
                    //if("3".equals(isRptFlag)){
                    //    cells[7].setCellValue(dwsucc);
                    //}
                }
            }
            //增加合计项
            row = sheet.createRow(k + 1);
            for(int j = 0;j < cells.length;j++){
                cells[j] = row.createCell(j);
                cells[j].setCellStyle(cellStyle2);
            }
            //合并单元格(起始行号，终止行号， 起始列号，终止列号)
            sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum(), sheet.getLastRowNum(), 0, cells.length - 5));
            cells[0].setCellValue("合计：");
            cells[cells.length - 4].setCellValue(icountSum);
            cells[cells.length - 3].setCellValue(rsuccSum);
            cells[cells.length - 2].setCellValue(rfail1Sum);
            //if("3".equals(isRptFlag)){
            //    cells[cells.length - 1].setCellValue(dusuccSum);
            //}
            os = new FileOutputStream(voucherFilePath + File.separator + fileName);
            // 写入Excel对象
            sxssfWorkbook.write(os);
        }
        zipName  = "档位统计报表" + sdf.format(curDate) + "_"+ StaticValue.getServerNumber() + ".zip";
        filePath = baseDir + File.separator + "download" + File.separator + "degreeRpt" + File.separator + zipName;
        ZipUtil.compress(voucherFilePath, filePath);
        //递归删除文件夹
        boolean flag = deleteDir(fileTemp);
        if (!flag) {
            EmpExecutionContext.error("刪除文件失敗！");
        }
        //关闭资源
       if(os != null){
           os.close();
       }
        sxssfWorkbook = null;
        xssfWorkbook = null;
        resultMap.put("fileName", zipName);
        resultMap.put("filePath", filePath);
        return resultMap;
    }

    /**
     * 递归删除
     * @param dir
     * @return
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] subFiles = dir.list();
            for (String file:subFiles) {
                boolean success = deleteDir(new File(dir, file));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }
    /**
     * 将设置单元格属性提取出来成一个方法
      * @param workbook excel对象
     * @return 单元格属性
     */
    public XSSFCellStyle setCellStyle(XSSFWorkbook workbook) {
        // 表格样式
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        // 字体名称
        font.setFontName("TAHOMA");
        // 下环线
        font.setUnderline(FontUnderline.NONE);
        // 粗体
        font.setBold(false);
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
    /**
     * 根据查询报表类型设置时间
     * @param vo
     */
    private void setTimeCondition(MtDataReportVo vo) throws Exception{

        Integer type = vo.getReportType();
        String startTime = vo.getStartTime();

        String endTime = vo.getEndTime();
        //当前年月日
        String yyyyMMdd = FastDateFormat.getInstance("yyyy-MM-dd").format(new Date());
        //当前年月日的一号
        String yyyyMM01 = yyyyMMdd.substring(0,yyyyMMdd.length() - 1) + "01";
        //获取当前的月份
        String month = yyyyMMdd.substring(5,7);
        //获取当前的年份
        String year = yyyyMMdd.substring(0,4);

        switch (type){
            case DAY_REPORT:
                if(StringUtils.isEmpty(startTime) && StringUtils.isEmpty(endTime)){
                    vo.setStartTime(yyyyMM01);
                    vo.setEndTime(yyyyMMdd);
                }else if(StringUtils.isEmpty(startTime) && StringUtils.isNotEmpty(endTime)){
                    vo.setStartTime(endTime.length() == 10 ? endTime.substring(0,8) + "01":yyyyMM01);
                }else if(StringUtils.isNotEmpty(startTime) && StringUtils.isEmpty(endTime)){
                    String monthStr = startTime.substring(5,7);
                    if(month.length() > 2){
                        monthStr = monthStr.substring(1);
                    }
                    String yearStr = startTime.substring(0,4);
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, Integer.parseInt(yearStr));
                    calendar.set(Calendar.MONTH, Integer.parseInt(monthStr));
                    Integer dayNum = calendar.getActualMaximum(Calendar.DATE);
                    vo.setEndTime(startTime.length() == 10 ? startTime.substring(0,8) + dayNum:yyyyMMdd);
                }
                break;
            case MONTH_REPORT:
                if(StringUtils.isNotEmpty(startTime)){
                    year = startTime.substring(0,4);
                    month = startTime.substring(5,7);
                }
                vo.setY(Integer.parseInt(year));
                vo.setImonth(Integer.parseInt(month));
                break;
            case YEAR_REPORT:
                if(StringUtils.isNotEmpty(startTime)){
                    year = startTime.length() == 4 ? startTime : year;
                }
                vo.setY(Integer.parseInt(year));
                break;
            default:
                throw new EMPException("企业富信>数据查询>档位报表统计查询，从LfRmsReportVo获取查询类型异常");
        }
    }
}
