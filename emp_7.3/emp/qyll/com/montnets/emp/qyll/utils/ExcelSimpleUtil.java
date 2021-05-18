package com.montnets.emp.qyll.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

/**
 * @author Jason Huang
 * @date 2017年11月7日 上午9:06:58
 */

public class ExcelSimpleUtil {
	/**
	 * 导出Excel到指定路径
	 * @param title  标题数组
	 * @param list    内容list
	 * @param path 存储路径
	 * @throws IOException
	 */
	public static void export(String[] title, List<String[]> list, String path) throws IOException {
		// keep 100 rows in memory,exceeding rows will be flushed to disk
		Workbook wb = new SXSSFWorkbook(100);
		Sheet sh = wb.createSheet();
		// Title部分
		Row titleRow = sh.createRow(0);
		for (int cellnum = 0; cellnum < title.length; cellnum++) {
			Cell cell = titleRow.createCell(cellnum);
			cell.setCellValue(title[cellnum]);
		}
		// Body部分
		for (int rownum = 1; rownum <= list.size(); rownum++) {
			Row row = sh.createRow(rownum);
			for (int cellnum = 0; cellnum < title.length; cellnum++) {
				Cell cell = row.createCell(cellnum);
				cell.setCellValue(list.get(rownum - 1)[cellnum]);
			}
		}
		FileOutputStream out = new FileOutputStream(path);
		wb.write(out);
		out.close();
	}
}
