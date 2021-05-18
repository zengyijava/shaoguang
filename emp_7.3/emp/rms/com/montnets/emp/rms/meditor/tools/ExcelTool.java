package com.montnets.emp.rms.meditor.tools;


import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.rms.commontempl.entity.LfTemplate;
import com.montnets.emp.rms.meditor.entity.LfTempParam;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class ExcelTool {

    private static final BaseBiz baseBiz = new BaseBiz();

    /**
     * @param tmId
     * @param map
     * @param excleJson
     * @param paramList
     * @return
     */
    public static HSSFWorkbook crateExcel(Long tmId, LinkedHashMap<String, ArrayList<String>> map, String excleJson, List<LfTempParam> paramList, Locale locale) {
        //创建一个Excel文件
        HSSFWorkbook wkb = null;
        //如果没有参数直接返回
        if (map == null) {
            return wkb;
        }
        //参数个数
        int paramSize = 0;
        for (String s : map.keySet()) {
            List<String> list = map.get(s);
            paramSize += list.size();
        }
        //记录每行已创建到那一列
        int row1num = 0;
        int row2num = 0;
        int row3num = 0;
        try {
            //创建HSSFWorkbook对象(excel的文档对象)
            wkb = new HSSFWorkbook();
            //建立新的sheet对象（excel的表单）
            HSSFSheet sheet = wkb.createSheet(MessageUtils.getWord("common", locale, "common_send_2"));

            HSSFRow row0 = sheet.createRow(0);
            HSSFRow row1 = sheet.createRow(1);
            HSSFRow row2 = sheet.createRow(2);
            HSSFRow row3 = sheet.createRow(3);
            //表格样式
            HSSFCellStyle cellStyle = wkb.createCellStyle();
            cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
            cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
            cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
            cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
            cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
            cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
            //提示文字样式
            HSSFCellStyle style = wkb.createCellStyle();
            style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
            style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
            style.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
            style.setFillPattern(CellStyle.SOLID_FOREGROUND);
            style.setWrapText(true);
            row0.setHeight((short) 1300);
            //文本格式
            HSSFCellStyle textStyle = wkb.createCellStyle();
            HSSFDataFormat format = wkb.createDataFormat();
            textStyle.setDataFormat(format.getFormat("@"));


            //插入表格说明

            String bgsm = MessageUtils.getWord("common", locale, "common_send_1_1") + "\n" +
                    MessageUtils.getWord("common", locale, "common_send_1_2") + "\n" +
                    MessageUtils.getWord("common", locale, "common_send_1_3") + "\n" +
                    MessageUtils.getWord("common", locale, "common_send_1_4");

            HSSFCell cell_0 = row0.createCell(0);
            cell_0.setCellValue(bgsm);
            cell_0.setCellStyle(style);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 15));
            //表头
            HSSFCell cell_1 = row1.createCell(0);
            cell_1.setCellValue(MessageUtils.getWord("common", locale, "common_send_3"));
            cell_1.setCellStyle(cellStyle);
            sheet.addMergedRegion(new CellRangeAddress(1, 3, 0, 0));
            HSSFCell hssfCell = row3.createCell(0);
            hssfCell.setCellStyle(cellStyle);
            sheet.setDefaultColumnWidth(17);
            row1num += 1;
            row2num += 1;
            row3num += 1;

            for (String key : map.keySet()) {
                List<String> list = map.get(key);
                int listSize = list.size();
                if (listSize != 0) {
                    //参数类型表头
                    HSSFCell row1Cell = row1.createCell(row1num);
                    row1Cell.setCellValue(key);
                    row1Cell.setCellStyle(cellStyle);
                    sheet.addMergedRegion(new CellRangeAddress(1, 1, row1num, row1num + listSize - 1));
                    for (int i = row1num + 1; i < row1num + listSize; i++) {
                        HSSFCell cell = row1.createCell(i);
                        cell.setCellStyle(cellStyle);
                    }
                    for (int i = 0; i < listSize; i++) {
                        //参数插入
                        HSSFCell row2Cell = row2.createCell(row2num);
                        String s = list.get(i);
                        row2Cell.setCellValue(s);
                        row2Cell.setCellStyle(cellStyle);
                        row2num += 1;
                        //参数类型插入
                        HSSFCell row3Cell = row3.createCell(row3num);
                        String tpye = getParaTpye(s, paramList, locale);
                        row3Cell.setCellValue(tpye);
                        row3Cell.setCellStyle(cellStyle);
                        sheet.setColumnWidth(row3num, tpye.length() * 430);
                        row3num += 1;

                    }
                    row1num += listSize;
                }
            }
            //将单元格格式设置为文本，避免日前时间自动转换引发的错误
            HSSFRow rowt = null;
            HSSFCell cellt = null;
            for (int i = 4; i < 10000; i++) {
                rowt = sheet.createRow(i);
                for (int j = 0; j <= paramSize; j++) {
                    cellt = rowt.createCell(j);
                    cellt.setCellStyle(textStyle);
                    cellt.setCellType(HSSFCell.CELL_TYPE_STRING);
                }
            }

            //更新表字段参数JSON
            LfTemplate lfTemplate = new LfTemplate();
            lfTemplate.setTmid(tmId);
            lfTemplate.setExljson(excleJson);
            boolean updateObj = baseBiz.updateObj(lfTemplate);

        } catch (Exception e) {
            EmpExecutionContext.error(e, "发现异常！");
        }
        return wkb;
    }

    /**
     * 参数值校验
     *
     * @param key       上传的参数
     * @param value     上传的参数值
     * @param paramList 提交模板时的参数集合
     * @return 检验成功返回true
     * @throws Exception
     */
    public static boolean checkValue(String key, String value, List<LfTempParam> paramList) throws Exception {

        //截取参数，例将{#参数1#}截取为参数1
        String substring = key.substring(2, key.lastIndexOf("#}"));
        //遍历提交模板时的参数集合
        for (LfTempParam lfTempParam : paramList) {
            //参数相同进行比较
            if (substring.equals(lfTempParam.getName())) {
                //参数类型
                int type = lfTempParam.getType();
                //最小长度
                Integer minL = lfTempParam.getMinLength();
                //最大长度
                Integer maxL = lfTempParam.getMaxLength();
                //固定长度
                Integer fixLength = lfTempParam.getFixLength();
                //1为固定长度标记
                Integer lengthRestrict = lfTempParam.getLengthRestrict();
                if (lengthRestrict == 1) {
                    //当参数长度固定时最大最小长度相同
                    minL = fixLength;
                    maxL = fixLength;
                }
                if (1 == type) {
                    //纯数字
                    Pattern pattern = Pattern.compile("^[0-9]{" + minL + "," + maxL + "}");
                    return pattern.matcher(value).matches();
                } else if (2 == type) {
                    //字母数字
                    Pattern pattern = Pattern.compile("^[0-9a-zA-Z]{" + minL + "," + maxL + "}");
                    return pattern.matcher(value).matches();
                } else if (3 == type) {
                    //金额
                    Pattern pattern = Pattern.compile("[.0-9]+");
                    return pattern.matcher(value).matches() && minL <= value.length() && value.length() <= maxL;
                } else if (4 == type) {
                    //任意字符
                    return minL <= value.length() && value.length() <= maxL;
                } else if (5 == type) {
                    //验证日期是yyyy-MM-dd
                    Pattern p = Pattern.compile("^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$");
                    return p.matcher(value).matches();
                } else if (6 == type) {
                    //验证日期是yyyy/MM/dd
                    Pattern p = Pattern.compile("^(?:(?!0000)[0-9]{4}/(?:(?:0[1-9]|1[0-2])/(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])/(?:29|30)|(?:0[13578]|1[02])/31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)/02/29)$");
                    return p.matcher(value).matches();
                } else if (7 == type) {
                    //MM-dd
                    Pattern pattern = Pattern.compile("^(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[2469]|11)-(0[1-9]|[12][0-9]|30)))$");
                    return pattern.matcher(value).matches();
                } else if (8 == type) {
                    //MM/dd
                    Pattern pattern = Pattern.compile("^(((0[13578]|1[02])/(0[1-9]|[12][0-9]|3[01]))|((0[2469]|11)/(0[1-9]|[12][0-9]|30)))$");
                    return pattern.matcher(value).matches();
                } else if (9 == type) {
                    //验证日期是yyyy-MM-dd HH:mm:ss
                    Pattern p = Pattern.compile("^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29) ([0-1][0-9]|(2[0-3])):([0-5][0-9]):([0-5][0-9])$");
                    return p.matcher(value).matches();
                } else if (10 == type) {
                    //验证日期是yyyy/MM/dd HH:mm:ss
                    Pattern p = Pattern.compile("^(?:(?!0000)[0-9]{4}/(?:(?:0[1-9]|1[0-2])/(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])/(?:29|30)|(?:0[13578]|1[02])/31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)/02/29) ([0-1][0-9]|(2[0-3])):([0-5][0-9]):([0-5][0-9])$");
                    return p.matcher(value).matches();
                } else if (11 == type) {
                    //MM-dd HH:mm:ss
                    Pattern pattern = Pattern.compile("^(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[2469]|11)-(0[1-9]|[12][0-9]|30))) ([0-1][0-9]|(2[0-3])):([0-5][0-9]):([0-5][0-9])$");
                    return pattern.matcher(value).matches();
                } else if (12 == type) {
                    //MM/dd HH:mm:ss
                    Pattern pattern = Pattern.compile("^(((0[13578]|1[02])/(0[1-9]|[12][0-9]|3[01]))|((0[2469]|11)/(0[1-9]|[12][0-9]|30))) ([0-1][0-9]|(2[0-3])):([0-5][0-9]):([0-5][0-9])$");
                    return pattern.matcher(value).matches();
                } else if (13 == type) {
                    //验证时间HH:mm:ss的正则表达式
                    Pattern p = Pattern.compile("([0-1][0-9]|(2[0-3])):([0-5][0-9]):([0-5][0-9])$");
                    return p.matcher(value).matches();
                } else if (14 == type) {
                    //验证二维码URL
                    Pattern p = Pattern.compile("^(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]$");
                    //return p.matcher(value).matches() && value.length() <= maxL;
                    return p.matcher(value).matches() && value.length() <= 500;
                }
            }
        }
        return false;
    }

    /***
     *
     * @param str 需要对比的参数
     * @param paramList 提交模板时的参数集合
     * @return
     */
    /***
     *
     * @param str 需要对比的参数
     * @param paramList 提交模板时的参数集合
     * @return
     */
    public static String getParaTpye(String str, List<LfTempParam> paramList, Locale locale) {
        for (LfTempParam lfTempParam : paramList) {
            String substring = str.substring(2, str.lastIndexOf("#}"));
            if (substring.equals(lfTempParam.getName())) {
                int type = lfTempParam.getType();
                Integer minLength = lfTempParam.getMinLength();
                Integer maxLength = lfTempParam.getMaxLength();
                //固定长度
                Integer fixLength = lfTempParam.getFixLength();
                Integer lengthRestrict = lfTempParam.getLengthRestrict();
                //1为固定长度标记
                if (lengthRestrict == 1) {
                    //当参数长度固定时最大最小长度相同
                    minLength = fixLength;
                    maxLength = fixLength;
                }
                switch (type) {
                    case 1:
                        return MessageUtils.getWord("common", locale, "common_send_4") + minLength + "-" + maxLength;
                    case 2:
                        return MessageUtils.getWord("common", locale, "common_send_5") + minLength + "-" + maxLength;
                    case 3:
                        return MessageUtils.getWord("common", locale, "common_send_6") + minLength + "-" + maxLength;
                    case 4:
                        return MessageUtils.getWord("common", locale, "common_send_7") + minLength + "-" + maxLength;
                    case 5:
                        return MessageUtils.getWord("common", locale, "common_send_8") + "（YYYY-MM-DD）";
                    case 6:
                        return MessageUtils.getWord("common", locale, "common_send_8") + "（YYYY/MM/DD）";
                    case 7:
                        return MessageUtils.getWord("common", locale, "common_send_8") + "（MM-DD）";
                    case 8:
                        return MessageUtils.getWord("common", locale, "common_send_8") + "（MM/DD）";
                    case 9:
                        return MessageUtils.getWord("common", locale, "common_send_9") + "（YYYY-MM-DD hh:mm:ss）";
                    case 10:
                        return MessageUtils.getWord("common", locale, "common_send_9") + "（YYYY/MM/DD hh:mm:ss）";
                    case 11:
                        return MessageUtils.getWord("common", locale, "common_send_9") + "（MM-DD hh:mm:ss）";
                    case 12:
                        return MessageUtils.getWord("common", locale, "common_send_9") + "（MM/DD hh:mm:ss）";
                    case 13:
                        return MessageUtils.getWord("common", locale, "common_send_10") + "（hh:mm:ss）";
                    case 14:
                        return MessageUtils.getWord("common", locale, "common_send_11") + "0-500";
                }
            }
        }
        return MessageUtils.getWord("common", locale, "common_send_12");
    }
}
