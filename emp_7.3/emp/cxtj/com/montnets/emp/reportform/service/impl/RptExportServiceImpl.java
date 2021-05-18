package com.montnets.emp.reportform.service.impl;


import com.montnets.emp.common.constant.EMPException;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.ottbase.constant.StaticValue;
import com.montnets.emp.reportform.annotation.RptExportAnnotation;
import com.montnets.emp.reportform.bean.ReportVo;
import com.montnets.emp.reportform.bean.RptExportBean;
import com.montnets.emp.reportform.service.IRptExportService;
import com.montnets.emp.reportform.util.FastJsonUtils;
import com.montnets.emp.reportform.util.IOUtils;
import com.montnets.emp.util.TxtFileUtil;
import com.montnets.emp.util.ZipUtil;
import org.apache.commons.lang.StringUtils;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 查询统计报表导出接口实现类
 * @author chenguang
 * @date 2018-12-21 10:30:00
 */
public class RptExportServiceImpl implements IRptExportService {
    /**
     * 最大导出数量
     */
    private static final Integer EXPORT_MAX_LIMIT = 500000;
    /**
     * 时间格式化
     */
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    /**
     * 导出报表主入口
     *
     * @param reportVos list集合
     * @param module    当前模块
     * @return 导出结果  导出成功则直接传回url给前端  fail 导出失败 error 导出错误 oversize 数据量过大
     */
    @Override
    public String createRptExcelByModule(List<ReportVo> reportVos, String module) {
        String result = "fail";
        try {
            if(reportVos == null || reportVos.size() > EXPORT_MAX_LIMIT){
                result = "oversize";
                throw new EMPException("导出数量超出最大限制：" + EXPORT_MAX_LIMIT);
            }
            if(StringUtils.isBlank(module)){
                result = "error";
                throw new EMPException("获取module失败");
            }
            //利用反射加自定义注解获取此次导出报表需要的字段名以及字段顺序
            List<RptExportBean> colList = getRptExcelColByReflection(module);
            //生成的excel名字 由于会生成多个文件，会自动在后面加上_N(表示第几个文件)
            String excelName = module + "_" + sdf.format(new Date()) + "_" + StaticValue.SERVER_NUMBER;
            //调用生成excel方法
            HashMap<String, String> hashMap = createRptExcelByColList(colList, reportVos, excelName, module);
            result = FastJsonUtils.collectToString(hashMap);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "-->查询统计报表导出异常 RptExportServiceImpl.createRptExcelByModule() module:" + module);
        }
        return result;
    }

    /**
     * 生成excel报表
     * @param colList 导出字段集合
     * @param reportVos 结果集合集合
     * @param excelName excel名字
     * @param module 模块名字
     * @return 包含文件名字与路径的hashMap
     * @throws Exception 抛出异常
     */
    private HashMap<String, String> createRptExcelByColList(List<RptExportBean> colList, List<ReportVo> reportVos, String excelName, String module) throws Exception{
        HashMap<String, String> resultMap = new HashMap<String, String>(16);
        String baseDir = new TxtFileUtil().getWebRoot() + "cxtj/reportform/file";
        // 创建Excel的 Workbook,对应到一个excel文档
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFCellStyle cellStyle = wb.createCellStyle();
        // 生成的工作薄个数
        int excelFileSize = (reportVos.size() % EXPORT_MAX_LIMIT == 0) ? (reportVos.size() / EXPORT_MAX_LIMIT) : (reportVos.size() / EXPORT_MAX_LIMIT + 1);
        //实际生成文件夹路径
        String voucherTemplateDirPath = baseDir + File.separator + "download" + File.separator + module + File.separator + sdf.format(new Date());
        File fileTemp = new File(voucherTemplateDirPath);
        if (!fileTemp.exists()) {
            if(!fileTemp.mkdirs()){
                throw new EMPException("查询统计>统计报表 导出功能实现异常，无法创建对应文件夹！");
            }
        }
        //创建只读的Excel工作薄的对象
        XSSFSheet sheet = wb.createSheet(module);
        //模板文件路径
        String voucherTemplatePath = baseDir + File.separator + "temp" + File.separator + module + ".xlsx";
        File file = new File(voucherTemplatePath);
        if(!file.exists()){
            //创建表头以及模板文件
            createThAndTemplateFile(wb, colList, sheet, cellStyle, voucherTemplatePath);
        }
        XSSFWorkbook xssfWorkbook;
        SXSSFWorkbook sxssfWorkbook;
        OutputStream os = null;
        for(int i = 0;i< excelFileSize; i++){
            //文件名
            String fileName = excelName + "_" + (i+1) + ".xlsx";
            // 读取模板工作表
            InputStream in = new FileInputStream(file);
            xssfWorkbook = new XSSFWorkbook(in);
            sxssfWorkbook = new SXSSFWorkbook(xssfWorkbook,10000);
            sxssfWorkbook.setSheetName(i, module);
            Sheet sheetAt = sxssfWorkbook.getSheetAt(i);
            //获取单元格样式
            cellStyle = setCellStyle(xssfWorkbook);
            in.close();
            int k = 0;
            long sendsuccSum = 0;
            long rfail2Sum = 0;
            for (ReportVo vo : reportVos){
                //每一条记录创建一行
                Row row = sheetAt.createRow(k++ + 1);
                //新建单元格
                Cell[] cells = new Cell[colList.size()];
                Class cls = vo.getClass();
                //利用反射配合 colList 赋值
                for(int j = 0; j < colList.size(); j++){
                    RptExportBean bean = colList.get(j);
                    Method getMethod = cls.getMethod(bean.getMethodName());
                    String resultStr="";
                    //判断反射结果是否为null
                    if(getMethod.invoke(vo) != null){
                        resultStr = getMethod.invoke(vo).toString();
                    }
                  
                    //遍历初始化cell与样式 并且赋值
                    cells[j] = row.createCell(j);
                    cells[j].setCellStyle(cellStyle);
                    cells[j].setCellValue(resultStr);
                }
                sendsuccSum += vo.getSendSucc();
                rfail2Sum += vo.getRfail2();
            }
            //增加合计项
            Row totalRow = sheetAt.createRow(k + 1);
            Cell[] cells = new Cell[colList.size()];
            for(int j = 0;j < cells.length;j++){
                cells[j] = totalRow.createCell(j);
                cells[j].setCellStyle(cellStyle);
            }
            //合并单元格(起始行号，终止行号， 起始列号，终止列号)
            sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum(), sheet.getLastRowNum(), 0, cells.length - 2));
            cells[0].setCellValue("合计：");
            cells[cells.length - 2].setCellValue(sendsuccSum);
            cells[cells.length - 1].setCellValue(rfail2Sum);
            os = new FileOutputStream(voucherTemplateDirPath + File.separator + fileName);
            // 写入Excel对象
            sxssfWorkbook.write(os);
        }
        //生成压缩文件
        String zipName  = module + "_" + sdf.format(new Date()) + "_"+ StaticValue.SERVER_NUMBER + ".zip";
        String filePath = baseDir + File.separator + "download" + File.separator + module + File.separator + zipName;
        ZipUtil.compress(voucherTemplateDirPath, filePath);
        //递归删除文件夹
        IOUtils.deleteDirByRecursion(new File(baseDir), "zip");
        //关闭资源
        if(os != null){
            os.close();
        }
        resultMap.put("filePath", filePath);
        resultMap.put("fileName", zipName);
        return resultMap;
    }

    private XSSFCellStyle setCellStyle(XSSFWorkbook workbook) {
        // 表格样式
        XSSFCellStyle cellStyle = workbook.createCellStyle();

        XSSFFont font = workbook.createFont();
        // 字体大小
        font.setFontHeight(12);

        //公共样式
        getCommonCellStyle(cellStyle, font);
        //应用
        cellStyle.setFont(font);

        return cellStyle;
    }


    private void createThAndTemplateFile(XSSFWorkbook wb, List<RptExportBean> colList, XSSFSheet sheet, XSSFCellStyle cellStyle, String voucherTemplatePath) throws Exception{
        XSSFFont font = wb.createFont();

        getCommonCellStyle(cellStyle, font);
        // 字体大小
        font.setFontHeight(12);
        //加粗
        font.setBold(true);
        //加重磅数
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        //应用
        cellStyle.setFont(font);
        //创建Excel的sheet的一行 即表头
        XSSFRow row = sheet.createRow(0);
        XSSFCell[] cell = new XSSFCell[colList.size()];
        for (int i = 0; i < cell.length; i++) {
            //设置宽度
            sheet.setColumnWidth(i, 20 * 256);
            // 创建一个Excel的单元格
            cell[i] = row.createCell(i);
            // 设置单元格样式
            cell[i].setCellStyle(cellStyle);
            //设置内容
            cell[i].setCellValue(colList.get(i).getThName());
        }
        //判断文件夹是否存在不存在则创建
        File fileTemp = new File(voucherTemplatePath.substring(0, voucherTemplatePath.lastIndexOf(File.separator)));
        if (!fileTemp.exists()) {
            if(!fileTemp.mkdirs()){
                throw new EMPException("查询统计>统计报表 导出功能实现异常，无法创建对应文件夹！");
            }
        }
        FileOutputStream xos = new FileOutputStream(voucherTemplatePath);
        wb.write(xos);
        xos.close();
    }

    private void getCommonCellStyle(XSSFCellStyle cellStyle, XSSFFont font) {
        // 竖直对齐
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 水平对齐
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        //自动换行
        cellStyle.setWrapText(true);
        // 字体名称
        font.setFontName("TAHOMA");
        // 下环线
        font.setUnderline(FontUnderline.NONE);
    }

    private List<RptExportBean> getRptExcelColByReflection(String module) {
        List<RptExportBean> rptExportBeans = new ArrayList<RptExportBean>();
        try {
            //获取所有的字段属性
            Field[] fields = ReportVo.class.getDeclaredFields();
            for(Field field : fields){
                RptExportBean bean = new RptExportBean();
                RptExportAnnotation annotation = field.getAnnotation(RptExportAnnotation.class);
                if(null == annotation){
                    //该字段上没有 RptExportAnnotation 注解则直接continue
                    continue;
                }
                String[] moduleName = annotation.module();
                int[] colIndex = annotation.index();
                for(int i = 0; i < moduleName.length; i++){
                    if(moduleName[i].equals(module)){
                        //包含说明该字段需要在当前模块报表导出
                        bean.setMethodName("get" + Character.toUpperCase(field.getName().charAt(0)) + field.getName().substring(1));
                        bean.setIndex(colIndex[i]);
                        bean.setThName(annotation.thName());
                        rptExportBeans.add(bean);
                    }
                }
            }
        } catch (SecurityException e) {
            EmpExecutionContext.error(e, "-->查询统计报表导出异常 RptExportServiceImpl.getRptExcelColByReflection() module:" + module);
        }
        Collections.sort(rptExportBeans, new Comparator<RptExportBean>() {
            @Override
            public int compare(RptExportBean bean1, RptExportBean bean2) {
                if(bean1.getIndex() > bean2.getIndex()){
                    return 1;
                }else if(bean1.getIndex() < bean2.getIndex()){
                    return -1;
                }else {
                    return 0;
                }
            }
        });
        return rptExportBeans;
    }
}
