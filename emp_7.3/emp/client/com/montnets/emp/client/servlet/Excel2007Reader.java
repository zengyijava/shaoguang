
package com.montnets.emp.client.servlet;

/**
 * 2007excel读写类
 * @description
 * @project p_util
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-7-23 下午03:05:16
 */

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.util.ChangeCharset;
import com.montnets.emp.util.GetSxCount;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.*;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class Excel2007Reader extends DefaultHandler {
	private FileOutputStream fos = null;

	private OutputStreamWriter osw = null;

	private BufferedWriter bw = null;

	private StringBuffer ssc = new StringBuffer();

	private SharedStringsTable sst;

	/**
	 * 单元格内容的值
	 */
	private String lastContents;

	private boolean nextIsString;

	/**
	 * sheet索引
	 */
	private int sheetIndex = -1;

	/**
	 * 行内容集合
	 */
	private List<String> rowlist = new ArrayList<String>();

	/**
	 * 当前行
	 */
	private int curRow = 0;

	/**
	 * 当前列索引
	 */
	private int curCol = 0;

	/**
	 * 上一列列索引
	 */
	private int preCol = 0;

	// 列数
	private int rowsize = 0;

	private String line = System.getProperties().getProperty("line.separator");

	/**
	 * 解析类型 1:只获取每行第一个单元格内容;大于1:获取所有单元格内容
	 */
	private int parsetType = 1;

	/**
	 * 单元格类型
	 */
	private String cellDataType = "NUMBER";

	/**
	 * 格式化规则
	 */
	private NumberFormat format = new DecimalFormat("#.##########");
	/**
	 * 返回信息
	 */
	private Excel2007VO vo = new Excel2007VO();
	/**
	 * 第一行
	 */
	private String firstline = "";

	/**
	 * 文件解析
	 * @param ins 流对象
	 * @param fileStr 文件存在目录
	 * @author chentingsheng <cts314@163.com>
	 * @Date: 2014-8-6 上午10:22:53
	 * @throws Exception 异常
	 */
	public Excel2007VO fileParset(String fileStr, InputStream ins) throws Exception
	{
		BufferedReader br = null;
		FileInputStream fis = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddH24mmssSSS");
			Date curDate = new Date();
			
			String fileStrTemp = fileStr+sdf.format(curDate)+GetSxCount.getInstance().getCount()+"_temp"  + ".txt";
			File newfile = new File(fileStrTemp);
			if(newfile.exists()) {
				EmpExecutionContext.info("客户通讯录导入，生成xlsx重复，文件名:"+fileStrTemp);
				fileStrTemp = fileStr + sdf.format(curDate)+GetSxCount.getInstance().getCount()+"_temp"  + ".txt";
				newfile = new File(fileStrTemp);
				if(newfile.exists()) {
					EmpExecutionContext.error("客户通讯录导入，第二次生成xlsx重复，返回null，文件名:"+fileStrTemp);
					return null;
				}
			}

			fos = new FileOutputStream(newfile, true);
			osw = new OutputStreamWriter(fos, "GBK");
			bw = new BufferedWriter(osw);

			// 遍历所有sheet
			process(ins);

			// 小于1000条最后一批记录写入文件
			if(ssc.length() > 0) {
				// 内容写入到txt文件中
				bw.write(ssc.toString());
				bw.flush();
				ssc.setLength(0);
			}
			fis = new FileInputStream(newfile);
			br = new BufferedReader(new InputStreamReader(fis, "GBK"));
			vo.setCurRow(curRow);
			vo.setRowsize(rowsize);
			vo.setFirstline(firstline);
			String charset = new ChangeCharset().get_charset(ins);
			if(charset.startsWith("UTF-")) {
				int read = br.read(new char[1]);
			}
			List<String> lineContent = new ArrayList<String>();
			String tmp;
			while ((tmp = br.readLine()) != null){
				lineContent.add(tmp);
			}
			vo.setLineContent(lineContent);
			EmpExecutionContext.info("客户通讯录导入，生成xlsx，文件名:"+ fileStrTemp);
			return vo;
		}
		catch (Exception e) {
			EmpExecutionContext.error(e, "解析EXCEL2007文件异常！");
			throw e;
		}
		finally {
			if(bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					EmpExecutionContext.error(e, "解析EXCEL2007关闭字符输出流异常！");
				}
			}
			if(osw != null) {
				try {
					osw.close();
				} catch (IOException e) {
					EmpExecutionContext.error(e, "解析EXCEL2007关闭字符输出流通道异常！");
				}
			}
			if(fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					EmpExecutionContext.error(e, "解析EXCEL2007关闭文件输出流异常！");
				}
			}
			SysuserUtil.closeStream(fis);
			SysuserUtil.closeStream(br);
		}
	}

	/**
	 * 遍历excel文件的一个sheet，其中sheetId为要遍历的sheet索引，从1开始，1-3
	 * 
	 * @description
	 * @param filename
	 *        文件路径
	 * @param sheetId
	 *        sheetId
	 * @throws Exception
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-8-6 下午03:14:44
	 */
	public void processOneSheet(InputStream filename, int sheetId) throws Exception
	{
		OPCPackage pkg = null;
		InputStream sheet2 = null;
		try
		{
			pkg = OPCPackage.open(filename);
			XSSFReader r = new XSSFReader(pkg);
			SharedStringsTable sst = r.getSharedStringsTable();

			XMLReader parser = fetchSheetParser(sst);

			// 根据 rId# 或 rSheet# 查找sheet
			sheet2 = r.getSheet("rId" + sheetId);
			sheetIndex++;
			InputSource sheetSource = new InputSource(sheet2);
			parser.parse(sheetSource);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "遍历excel2007文件的单个sheet异常，sheetId:" + sheetId);
		}finally{
			if(sheet2 != null)
			{
				sheet2.close();
			}
			if(pkg != null)
			{
				// 解决文件使用后无法删除的问题
				pkg.close();
			}
		}
	}

	/**
	 * 遍历excel文件所有sheet
	 * 
	 * @description
	 * @param filename
	 * @throws Exception
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-8-6 下午03:15:33
	 */
	public void process(InputStream filename) throws Exception
	{
		OPCPackage pkg = null;
		try
		{
			pkg = OPCPackage.open(filename);
			XSSFReader xSSFReader = new XSSFReader(pkg);
			SharedStringsTable sst = xSSFReader.getSharedStringsTable();

			XMLReader parser = fetchSheetParser(sst);

			Iterator<InputStream> sheets = xSSFReader.getSheetsData();
			while(sheets.hasNext())
			{
				curRow = 0;
				sheetIndex++;
				InputStream sheet = sheets.next();
				InputSource sheetSource = new InputSource(sheet);
				parser.parse(sheetSource);
				sheet.close();
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "遍历excel2007文件所有sheet异常！");
		}finally{
			if(pkg != null)
			{
				// 解决文件使用后无法删除的问题
				pkg.close();
			}
		}
	}

	/**
	 * 以XML格式读取
	 * 
	 * @description
	 * @param sst
	 * @return
	 * @throws SAXException
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-8-6 下午03:16:43
	 */
	public XMLReader fetchSheetParser(SharedStringsTable sst) throws SAXException
	{
		XMLReader parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
		this.sst = sst;
		parser.setContentHandler(this);
		return parser;
	}

	/**
	 * 开始解析单元格
	 */
	public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException
	{
		try
		{
			// c => 单元格
			if(name.equals("c"))
			{
				// 如果下一个元素是 SST 的索引，则将nextIsString标记为true
				String cellType = attributes.getValue("t");
				String rowStr = attributes.getValue("r");
				curCol = this.getRowIndex(rowStr);
				if(cellType != null && cellType.equals("s"))
				{
					nextIsString = true;
				}
				else
				{
					nextIsString = false;
				}

				//单元格类型
				cellDataType = "NUMBER";
				if("b".equals(cellType) || "e".equals(cellType) || "inlineStr".equals(cellType) || "s".equals(cellType) || "str".equals(cellType))
				{
					cellDataType = "OTHER";
				}
			}
			// 置空
			lastContents = "";
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "excel2007开始解析单元格方法异常！");
		}
	}

	/**
	 * 解析单元格结束
	 */
	public void endElement(String uri, String localName, String name) throws SAXException
	{
		try
		{
			// 根据SST的索引值的到单元格的真正要存储的字符串
			// 这时characters()方法可能会被调用多次
			if(nextIsString)
			{
				try
				{
					int idx = Integer.parseInt(lastContents);
					lastContents = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
				}
				catch (Exception e)
				{
					// 此异常为处理一行最后的单元格的内容,如果单元格内容为非数字时转换异常,非程序异常,不打印日志
					// EmpExecutionContext.error("lastContents:" + lastContents);
				}
			}
			// v => 单元格的值，如果单元格是字符串则v标签的值为该字符串在SST中的索引
			// 将单元格内容加入rowlist中，在这之前先去掉字符串前后的空白符
			if(name.equals("v"))
			{
				String value = lastContents.trim();
				value = value.equals("") ? " " : value;

				// 当前单元格数据
				if(cellDataType != null && "NUMBER".equals(cellDataType) && !" ".equals(value))
				{
					value = extractCellValue(value);
				}

				int cols = curCol - preCol;
				if(cols > 1)
				{
					for (int i = 0; i < cols - 1; i++)
					{
						rowlist.add(preCol, "");
					}
				}
				preCol = curCol;
				rowlist.add(curCol - 1, value);
			}
			else
			{
				// 如果标签名称为 row ，这说明已到行尾，调用 optRows() 方法
				if(name.equals("row"))
				{
					int tmpCols = rowlist.size();
					/**
					 * 标题行，一般情况下为0
					 */
					int titleRow = 0;
					if(curRow > titleRow && tmpCols < this.rowsize)
					{
						for (int i = 0; i < this.rowsize - tmpCols; i++)
						{
							rowlist.add(rowlist.size(), "");
						}
					}

					try
					{
						optRows(sheetIndex, curRow, rowlist);
					}
					catch (Exception e)
					{
						EmpExecutionContext.error(e, "获取并记录excel2007行内容异常！");
					}

					if(curRow == titleRow)
					{
						this.rowsize = rowlist.size();
					}

					rowlist.clear();
					curRow++;
					curCol = 0;
					preCol = 0;
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "excel2007解析单元格结束方法异常！");
		}
	}

	/**
	 * 获取单元格内容
	 */
	public void characters(char[] ch, int start, int length) throws SAXException
	{
		// 得到单元格内容的值
		lastContents += new String(ch, start, length);
	}

	// 得到列索引，每一列c元素的r属性构成为字母加数字的形式，字母组合为列索引，数字组合为行索引，
	// 如AB45,表示为第（A-A+1）*26+（B-A+1）*26列，45行
	public int getRowIndex(String rowStr)
	{
		try
		{
			rowStr = rowStr.replaceAll("[^A-Z]", "");
			byte[] rowAbc = rowStr.getBytes();
			int len = rowAbc.length;
			float num = 0;
			for (int i = 0; i < len; i++)
			{
				num += (rowAbc[i] - 'A' + 1) * Math.pow(26.0, len - i - 1.0);
			}
			return (int) num;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取excel2007内容索引异常！");
			return 0;
		}
	}

	// excel记录行操作方法，以sheet索引，行索引和行元素列表为参数，对sheet的一行元素进行操作，元素为String类型
	private void optRows(int sheetIndex, int curRow, List<String> rowlist)
	{
		String Context = "";
		try
		{
			if(rowlist != null && rowlist.size() > 0)
			{

				for (int i = 0; i < rowlist.size(); i++)
				{
					Context += "," + rowlist.get(i);
				}
				if(curRow==0){
					firstline=Context;
				}
				ssc.append(Context+line );
				// 1000行写一次文件
				if(curRow % 10000 == 0)
				{
					// 内容写入到txt文件中
					bw.write(ssc.toString());
					ssc.setLength(0);
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取excel2007行内容写入临时TXT文件异常！");
		}
	}

	/**
	 * 格式化数字类型的单元格数据
	 * @description    
	 * @param value
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-10-24 下午07:33:42
	 */
	private String extractCellValue(String value)
	{
		String resultValue = "";
		try
		{
			resultValue = format.format(Double.valueOf(value), new StringBuffer(), new FieldPosition(0)).toString();
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "格式化excel2007数字类型的单元格数据异常！");
		}
		return resultValue;
	}
}
