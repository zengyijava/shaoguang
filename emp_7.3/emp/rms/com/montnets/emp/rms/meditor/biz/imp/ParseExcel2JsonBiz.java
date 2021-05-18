package com.montnets.emp.rms.meditor.biz.imp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.montnets.EMPException;
import com.montnets.emp.common.constant.PreviewParams;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.rms.commontempl.entity.FrameParam;
import com.montnets.emp.rms.meditor.tools.ExcelTool;
import com.montnets.emp.rms.tools.Excel2JsonDto;
import com.montnets.emp.rms.wbs.util.StringUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author:yangdl
 * @Data:Created in 15:19 2018.8.8 008
 */
public class ParseExcel2JsonBiz {


    /**
     * Ecxel校验
     * <p>
     * 1.第一行，第一列[A1:A2]的单元格必须为手机号
     * 2.第一行除手机号之后只能包含文本，图文，报表
     * 3.模板参数个数应该等于选择模板的参数个数（直接用[B2-Bn]所有单元格的字符串与数据库字段比对）
     *
     * @param fileList  文件参数list
     * @param preParams 预览参数对象
     * @param exlJson   当前模板生成的excel的表头信息
     * @return 校验成功返回true，失败返回false
     * @throws Exception 异常对象
     */
    public Boolean checkExcel(List<FileItem> fileList, PreviewParams preParams, String exlJson, String tempType) throws Exception {
        Workbook workBook = null;
        Boolean isCheck = false;
        String fileName = "";
        ArrayList<Excel2JsonDto> excel2JsonList = new ArrayList<Excel2JsonDto>();
        HashMap<String, String> invalidFileMap = new HashMap<String, String>();
        List<FrameParam> list = new ArrayList<FrameParam>();
        try {
            Pattern patt = Pattern.compile("\"frameList\":(\\[.*?])");
            Matcher math = patt.matcher(exlJson);
            if (math.find()) {
                String listJson = math.group(1);
                //每一帧信息组成的List集合
                list = JSON.parseArray(listJson, FrameParam.class);
            }
            //获取表头信息
            HashMap frameParamMap = JSON.parseObject(exlJson, new TypeReference<HashMap<String, Object>>() {
            });
            //总帧数
            String totalFrame = frameParamMap.get("totalFrame") + "";

            if (fileList != null && fileList.size() > 0) {
                Iterator<FileItem> itemIterator = fileList.iterator();
                while (itemIterator.hasNext()) {
                    try {
                        FileItem fileItem = itemIterator.next();
                        if (fileItem.getSize() == 0) {
                            itemIterator.remove();
                            continue;
                        }
                        fileName = fileItem.getName();
                        String fileType = fileName.substring(fileName.lastIndexOf("."));

                        //如果是.xls文件
                        //获得工作薄（Workbook）
                        if (fileType.equals(".xls")) {
                            workBook = new HSSFWorkbook(fileItem.getInputStream());
                        } else if (fileType.equals(".xlsx")) {
                            workBook = new HSSFWorkbook(fileItem.getInputStream());
                        } else {
                            throw new EMPException("不支持的excel格式");
                        }
                        //存储每一页的起始于终止col的Index
                        LinkedHashMap<String, List<Integer>> pageInfo = new LinkedHashMap<String, List<Integer>>();
                        Integer sheetInt = workBook.getNumberOfSheets();
                        if (sheetInt == 0) {
                            throw new EMPException("excel中没有有效的Sheet");
                        }
                        //遍历所有sheet
                        for (int i = 0; i < sheetInt; i++) {
                            //获得工作薄（Workbook）中工作表（Sheet）
                            Sheet sheet = workBook.getSheetAt(i);
                            if (sheet == null) {
                                continue;
                            }
                            //1.
                            //获取所有的合并单元格
                            int mergedRegions = sheet.getNumMergedRegions();
                       /*     for (int j = 0; j < mergedRegions; j++) {
                                //取得合并单元格的行号与列号
                                CellRangeAddress ca = sheet.getMergedRegion(i);
                                int _firstColumn = ca.getFirstColumn();
                                int _lastColumn = ca.getLastColumn();
                                int _firstRow = ca.getFirstRow();
                                int _lastRow = ca.getLastRow();
                                if (_firstColumn == 0 && _lastColumn == 0 && _firstRow == 0 && _lastRow == 1) {
                                    //获取[A1:A2]
                                    Row fRow = sheet.getRow(_firstRow);
                                    Cell fCell = fRow.getCell(_firstColumn);
                                    String cellValue = getCellValue(fCell, true);
                                    String sjhm = ExcelTool.SJHM;
                                    if (!getCellValue(fCell, true).equals(ExcelTool.SJHM)) {
                                        throw new EMPException("[A1:A2]表头不为手机号");
                                    }
                                }
                            }*/
                            //2,5
                            Integer maxPageIndex = 0;
                            List<String> cellValList = getCellByRowIndex(sheet, 0, pageInfo);
                            Pattern p = Pattern.compile("文本[0-9]{0,2}|图文[0-9]{0,2}|报表[0-9]{0,2}");
                        /*    for (String str : cellValList) {
                                Matcher m = p.matcher(str.replace(ExcelTool.YHYC,"").trim());
                                if (!m.find()) {
                                    throw new EMPException("标题不合格");
                                }
                            }*/
                            StringBuilder resultVal = new StringBuilder();
                            for (FrameParam frameParam : list) {
                                String paramValue = frameParam.getParamValue();
                                resultVal.append(paramValue).append(",");
                            }
                            String resultValStr = resultVal.toString();
                            resultValStr = resultValStr.substring(0, resultValStr.lastIndexOf(","));
                            //获取第二行所有的内容（除掉手机）
                            String thirdRowVal = getThirdRowValue(sheet);
                            if (!resultValStr.equals(thirdRowVal)) {
                                throw new EMPException("上传excel文件与模板excel文件不符");
                            }
                        }
                        isCheck = true;
                    } catch (EMPException empEx) {
                        //如果为无效文件，记录并从fileList中删除
                        invalidFileMap.put(fileName, empEx.getMessage());
                        itemIterator.remove();
                        isCheck = false;
                    }
                }
            }
            if (invalidFileMap.size() > 0) {
                preParams.setInvalidFileName(invalidFileMap);
            }
            return isCheck;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "企业短链-不同内容发送-校验上传excel模板文件异常");
            throw e;
        }
    }

    /**
     * 获取第一行的分页信息
     *
     * @param sheet
     * @param rowIndex
     * @param pageInfo
     * @return
     */
    private List<String> getCellByRowIndex(Sheet sheet, int rowIndex, LinkedHashMap<String, List<Integer>> pageInfo) {
        if (pageInfo == null) {
            pageInfo = new LinkedHashMap<String, List<Integer>>();
        }
        //读取首行
        Iterator<Cell> cellIterator = sheet.getRow(rowIndex).cellIterator();

        List<String> rowValList = new ArrayList<String>();

        Boolean flag = true;
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            if (flag) {
                flag = false;
                continue; //直接跳过第一列(手机号)
            }
            //取得当前单元格所在合并单元格的首行首列，末行末列，不是合并单元格则返回null
            String[] isMergedRow = isMergedRow(sheet, cell);
            if (isMergedRow != null) {
                //是合并单元格则取第一个cell的值,剩余的则continue
                if (cell.getColumnIndex() > Integer.parseInt(isMergedRow[1])
                        && cell.getColumnIndex() <= Integer.parseInt(isMergedRow[3])) {
                    continue;
                } else {
                    List<Integer> list = new ArrayList<Integer>();
                    list.add(Integer.parseInt(isMergedRow[1]));
                    list.add(Integer.parseInt(isMergedRow[3]));
                    pageInfo.put(isMergedRow[4], list);
                    rowValList.add(isMergedRow[4]);
                }
            } else {
                List<Integer> list = new ArrayList<Integer>();
                //不是合并单元格直接记录到List中
                list.add(cell.getColumnIndex());
                list.add(cell.getColumnIndex());
                pageInfo.put(getCellValue(cell, true), list);
                rowValList.add(getCellValue(cell, true));
            }
        }
        return rowValList;
    }


    private String getThirdRowValue(Sheet sheet) {
        StringBuilder result = new StringBuilder();
        Row row = sheet.getRow(1);
        short lastCellNum = row.getLastCellNum();
        for (int i = 1; i <= lastCellNum - 1; i++) {
            Cell cell = row.getCell(i);
            result.append(getCellValue(cell, true)).append(",");
        }
        return result.deleteCharAt(result.length() - 1).toString();
    }


    /**
     * 获取指定行的所有内容（即手机号+参数内容）
     *
     * @param sheet
     * @param rowIndex
     * @return
     * @throws Exception
     */
    public String getRowValueByRowId(Sheet sheet, int rowIndex, int tempParamCount) throws Exception {
        StringBuilder content = new StringBuilder();
        try {
            //判断是不是为全空
            Boolean isAllNull = true;
            Row row = sheet.getRow(rowIndex);
            String cellVal;
            for (int j = 0; j <= tempParamCount; j++) {
                cellVal = getCellValue(row.getCell(j), true);
                if ("".equals(cellVal)) {
                    content.append("[#NULL#]").append("[#MW#]");
                } else {
                    isAllNull = false;
                    content.append(cellVal).append("[#MW#]");
                }
            }
            Iterator<Cell> cellIterator = row.cellIterator();
            //去掉最后一个分隔符
            if (isAllNull) {
                return "";
            } else {
                return content.substring(0, content.lastIndexOf("[#MW#]"));
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "企业短链-不同内容发送-按行获取excel文件异常");
            throw e;
        }
    }


    /**
     * 取得当前单元格所在合并单元格的首行首列，末行末列，不是合并单元格则返回null
     *
     * @param sheet
     * @param cell
     * @return
     */
    private String[] isMergedRow(Sheet sheet, Cell cell) {
        String[] mergedRowInfo = new String[5];
        Boolean isMergedRow = false;
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if (cell.getColumnIndex() >= firstColumn && cell.getColumnIndex() <= lastColumn
                    && cell.getRowIndex() >= firstRow && cell.getRowIndex() <= lastRow) {
                mergedRowInfo[0] = firstRow + "";
                mergedRowInfo[1] = firstColumn + "";
                mergedRowInfo[2] = lastRow + "";
                mergedRowInfo[3] = lastColumn + "";
                //将合并单元格的值放在数组第五位
                Row fRow = sheet.getRow(firstRow);
                Cell fCell = fRow.getCell(firstColumn);
                mergedRowInfo[4] = getCellValue(fCell, true);
                isMergedRow = true;
                break;
            }
        }
        if (isMergedRow) {
            return mergedRowInfo;
        } else {
            return null;
        }
    }

    /**
     * 取单元格的值
     *
     * @param cell       单元格对象
     * @param treatAsStr 为true时，当做文本来取值 (取到的是文本，不会把“1”取成“1.0”)
     * @return
     */
    public String getCellValue(Cell cell, boolean treatAsStr) {
        if (cell == null) {
            return "";
        }

        if (treatAsStr) {
            // 虽然excel中设置的都是文本，但是数字文本还被读错，如“1”取成“1.0”
            // 加上下面这句，临时把它当做文本来读取
            cell.setCellType(Cell.CELL_TYPE_STRING);
        }

        if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            return String.valueOf(cell.getNumericCellValue());
        } else {
            return String.valueOf(cell.getStringCellValue());
        }
    }
}
