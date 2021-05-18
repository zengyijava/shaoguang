package com.montnets.emp.rms.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;

import com.alibaba.fastjson.JSON;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.rms.commontempl.entity.FrameParam;
import com.montnets.emp.rms.commontempl.entity.LfTemplate;
import com.montnets.emp.rms.vo.Param;

/**
 * @ClassName: ExcelTool
 * @Description: Excel操作工具类
 * @author xuty
 * @date 2018-4-3 上午9:54:33
 * 
 */
@SuppressWarnings("deprecation")
public class ExcelTool {
	private static BaseBiz baseBiz = new BaseBiz();
	public static HSSFWorkbook crateExcel(Long tmId,List<List<Param>>  list,boolean isUpdateExtJson) {
		//创建一个Excel文件
		HSSFWorkbook wkb = null;
		//记录所有帧数和参数拼接到List<FrameParam> 里面 存入数据库  EXLJSON 字段中
		List<FrameParam> frameList = new ArrayList<FrameParam>();
		//每一帧参数对象
		FrameParam frameParam =  null;
		try {
			//创建HSSFWorkbook对象(excel的文档对象)
			wkb = new HSSFWorkbook();

			//建立新的sheet对象（excel的表单）
			HSSFSheet sheet = wkb.createSheet("手机号码文件");
			//表格样式
			HSSFCellStyle cellStyle = wkb.createCellStyle();
			//设置背景色：

//			cellStyle.setFillForegroundColor((short) 13);// 设置背景色
//			cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

			//设置边框:

			cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
			cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
			cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
			cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框

			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
			cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中

			//在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
			HSSFRow row1 = sheet.createRow(0);
			HSSFRow row2 = sheet.createRow(1);
			HSSFRow row3 = sheet.createRow(2);
			HSSFCell cell1_1 = row1.createCell(0);
			cell1_1.setCellValue("手机号码");
			cell1_1.setCellStyle(cellStyle);
			//创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个
			//合并第一列三行
			sheet.addMergedRegion(new CellRangeAddress(0, 2, 0, 0));

			// 解析后的号码参数list为空直接返回false
			if (null == list) {
				return null;
			}
			// 循环遍历号码参数List，组装EXCEL的行列
			// 列位置
			int row1cellIndex = 0;
			int cellIndex = 0;
			//记录有效帧
			int h = 0;
			StringBuffer paramValueBugf  = null;
			int totalIndex = 0;
			for (int i = 0; i < list.size(); i++) {
				h ++;
				//每一帧参数对象
				frameParam = new FrameParam();
				totalIndex = totalIndex +list.get(i).size();
				paramValueBugf = new StringBuffer();
				// //设置单元格内容
				// 合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列

				List<Param> plist = list.get(i);
				if(plist.isEmpty()){
					h--;
					continue;
				}
				frameParam.setFrameIndex(i+1);
//System.out.println("第"+(i+1)+"页");
//System.out.println("------------------------");
				//合并单元格开始位置
				int startIndex =0 ;
				//合并单元格结束位置
				int endIndex =0;
				if(i == 0 ){
					startIndex = i+1;
					endIndex= plist.size();
				}else{
					startIndex = row1cellIndex;
					endIndex =  row1cellIndex+plist.size()-1;
				}


				int row2Count = 0;
				int textParamCount = 0;
				int chartPieParamCount = 0;
				int chartLineParamCount = 0;
				int chartBarParamCount = 0;
				StringBuffer typeBuf = new StringBuffer();
				for (int j=0 ;j < plist.size(); j++) {
					//类型
					Param p = plist.get(j);
					Integer type = p.getType();
					typeBuf.append(type).append(",");
					// 文本
					if (type == 1) {
						textParamCount ++;
					}
					// 图表-饼图
					if (type == 3 ) {
						chartPieParamCount++;
					}
					// 图表-柱状图
					if (type == 4 ) {
						chartBarParamCount++;
					}
					// 图表-折线图
					if (type == 5 ) {
						chartLineParamCount ++;
					}
				}

				for (int j=0 ;j < plist.size(); j++) {//每一个参数
					cellIndex++;
					row2Count++;
					Param p = plist.get(j);
//System.out.println(p.getType()+"##"+p.getValue());
					//类型
					Integer type = p.getType();
					HSSFCell row2cell = row2.createCell(cellIndex);
					HSSFCell row3cell = row3.createCell(cellIndex);
					// 文本
					if (type == 1) {
						row2cell.setCellValue("文本");
						frameParam.setType(0);
					}
					// 图文
					if (type == 2 ) {
						row2cell.setCellValue("图文");
						frameParam.setType(0);
						frameParam.setColor(p.getColor());
					}
					// 图表-饼图
					if (type == 3 ) {
						row2cell.setCellValue("报表（饼图）");
						frameParam.setType(1);
						frameParam.setCharType(type);
						//i+1 为页面的帧数
						int row2Index = 1;
						//若果一帧里面混有文参，需减去文参个数进行合并单元格
						if(typeBuf.toString().startsWith("1,")&& typeBuf.toString().contains("3,")){
							if(i==0){
								row2Index = textParamCount + 1;
								int row2EndIndex = list.get(i).size();
								//合并第二行
								sheet.addMergedRegion(new CellRangeAddress(1, 1, row2Index, row2EndIndex));
							}else{
								row2Index = totalIndex - list.get(i).size()+ textParamCount+1;
								int row2EndIndex = row2Index +list.get(i).size() - textParamCount-1 ;
								//合并第二行
								sheet.addMergedRegion(new CellRangeAddress(1, 1, row2Index, row2EndIndex));
							}

						}else if(typeBuf.toString().endsWith("1,")){
							if(i == 0){//第一帧
								row2Index = 1;
								int row2EndIndex = list.get(i).size()- textParamCount;
								sheet.addMergedRegion(new CellRangeAddress(1, 1, row2Index, row2EndIndex));
							}else{
								row2Index = totalIndex - list.get(i).size()+1;
								int row2EndIndex = row2Index + list.get(i).size()- textParamCount-1 ;
								sheet.addMergedRegion(new CellRangeAddress(1, 1, row2Index, row2EndIndex));
							}
						}else{//纯饼状图
							row2Index = totalIndex - list.get(i).size() + 1;
							int row2EndIndex = row2Index + list.get(i).size()-1 ;
							sheet.addMergedRegion(new CellRangeAddress(1, 1, row2Index, row2EndIndex));
						}
						frameParam.setType(1);
						frameParam.setCharType(type);
						frameParam.setColor(p.getColor());
					}
					// 图表-柱状图
					if (type == 4 ) {
						row2cell.setCellValue("报表（柱状图）");
						//i+1 为页面的帧数
						int row2Index = 1;
						//若果一帧里面混有文参，需减去文参个数进行合并单元格
						if(typeBuf.toString().startsWith("1,") && typeBuf.toString().contains("4,")){
							if(i==0){
								row2Index = textParamCount + 1;
								int row2EndIndex = list.get(i).size();
								//合并第二行
								sheet.addMergedRegion(new CellRangeAddress(1, 1, row2Index, row2EndIndex));
							}else{
								row2Index = totalIndex - list.get(i).size()+ textParamCount+1;
								int row2EndIndex = row2Index +list.get(i).size() - textParamCount-1 ;
								//合并第二行
								sheet.addMergedRegion(new CellRangeAddress(1, 1, row2Index, row2EndIndex));
							}


						}else if(typeBuf.toString().endsWith("1,")) {
							if(i == 0){//第一帧
								row2Index = 1;
								int row2EndIndex = list.get(i).size()- textParamCount;
								sheet.addMergedRegion(new CellRangeAddress(1, 1, row2Index, row2EndIndex));
							}else{
								row2Index = totalIndex - list.get(i).size()+1;
								int row2EndIndex = row2Index + list.get(i).size()- textParamCount-1 ;
								sheet.addMergedRegion(new CellRangeAddress(1, 1, row2Index, row2EndIndex));
							}

						}else{//纯柱状图
							row2Index = totalIndex - list.get(i).size() + 1;
							int row2EndIndex = row2Index + list.get(i).size()-1 ;
							sheet.addMergedRegion(new CellRangeAddress(1, 1, row2Index, row2EndIndex));
						}
						frameParam.setType(1);
						frameParam.setCharType(type);
						frameParam.setColor(p.getColor());
					}
					// 图表-折线图
					if (type == 5 ) {
						row2cell.setCellValue("报表（折线图）");
						//i+1 为页面的帧数
						int row2Index = 1;
						//若果一帧里面混有文参，需减去文参个数进行合并单元格
						if(typeBuf.toString().startsWith("1,") && typeBuf.toString().contains("5,")){
							if(i==0){
								row2Index = textParamCount + 1;
								int row2EndIndex = list.get(i).size();
								//合并第二行
								sheet.addMergedRegion(new CellRangeAddress(1, 1, row2Index, row2EndIndex));
							}else{
								row2Index = totalIndex - list.get(i).size()+ textParamCount+1;
								int row2EndIndex = row2Index +list.get(i).size() - textParamCount-1 ;
								//合并第二行
								sheet.addMergedRegion(new CellRangeAddress(1, 1, row2Index, row2EndIndex));
							}

						}else if(typeBuf.toString().endsWith("1,")){
							if(i == 0){//第一帧
								row2Index = 1;
								int row2EndIndex = list.get(i).size()- textParamCount;
								sheet.addMergedRegion(new CellRangeAddress(1, 1, row2Index, row2EndIndex));
							}else{
								row2Index = totalIndex - list.get(i).size()+1;
								int row2EndIndex = row2Index + list.get(i).size()- textParamCount-1 ;
								sheet.addMergedRegion(new CellRangeAddress(1, 1, row2Index, row2EndIndex));
							}
						}else{//纯折线图
							row2Index = totalIndex - list.get(i).size() + 1;
							int row2EndIndex = row2Index + list.get(i).size()-1 ;
							sheet.addMergedRegion(new CellRangeAddress(1, 1, row2Index, row2EndIndex));
						}
						frameParam.setType(1);
						frameParam.setCharType(type);
						frameParam.setColor(p.getColor());
					}
					// 图表-工资条
					if (type == 6 ) {
						row2cell.setCellValue("报表（工资条）");
						frameParam.setType(1);
						frameParam.setCharType(type);
					}
					// 图表-表格
					if (type == 7 ) {
						row2cell.setCellValue("报表（表格）");
						frameParam.setType(1);
						frameParam.setCharType(type);
					}
					row3cell.setCellValue(p.getValue());

					//为每帧参数对象设置值，用于存储数据库EXLJSON字段
					if(null != p.getColNum()){
						frameParam.setColNum(p.getColNum());
					}else{
						frameParam.setColNum(0);
					}

					if(null != p.getRowNum()){
						frameParam.setRowNum(p.getRowNum());
					}else{
						frameParam.setRowNum(0);
					}

					if(null != p.getDynamicType() && 0!= p.getDynamicType()){
						frameParam.setDynamicType(p.getDynamicType());
					}else if(null != p.getDynamicType() && 0== p.getDynamicType()){
						frameParam.setDynamicType(0);
					}

					paramValueBugf.append(p.getValue()).append(",");
					//设置样式
					row2cell.setCellStyle(cellStyle);
					row3cell.setCellStyle(cellStyle);
				}

				//第一行逻辑开始-------
				int titleCellIndex = 1;
				if(i == 0 || ((i == h) &&list.get(h).size() == 0) ){//i== 0 为第一页、(i == h) &&list.get(h).size() == 0 代表当前页中没有参数
					titleCellIndex = 1;

				} else{
					titleCellIndex = cellIndex - plist.size() +1;
				}
				row1cellIndex = titleCellIndex;
				int row1EndIndex = row1cellIndex + plist.size()-1;
				HSSFCell cell = row1.createCell(row1cellIndex);
				cell.setCellValue("第"+(i+1)+"页");
				cell.setCellStyle(cellStyle);
				sheet.addMergedRegion(new CellRangeAddress(0, 0, row1cellIndex,row1EndIndex));
				//每一帧的总参数个数
				frameParam.setParamCount(plist.size());
				// 删除最后一个逗号
				if(paramValueBugf.toString().endsWith(",")){
					String str = paramValueBugf.toString().substring(0, paramValueBugf.length()-1);
					//每一帧所有参数值，每一个参数以英文逗号分隔
					frameParam.setParamValue(str);
				}else{
					frameParam.setParamValue(paramValueBugf.toString());
				}
				frameList.add(frameParam);
			}
//System.out.println("totalIndex:"+totalIndex);
			// 更新数据库LF_TEMPLATE  phoneParam 字段
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("totalFrame", list.size());
			map.put("frameList", frameList);
			String excleJson = JSON.toJSONString(map);
//System.out.println(excleJson);
			//更新表字段参数JSON
			LfTemplate lfTemplate = new LfTemplate();
			lfTemplate.setTmid(tmId);
			lfTemplate.setExljson(excleJson);
			if(isUpdateExtJson){//下载号码文件是不需再更新，生成模板调用时需要更新EXTJSON字段
				boolean updateObj = baseBiz.updateObj(lfTemplate);
			}



			// 输出Excel文件
////			String excelName = FMT.format(new Date());
////			output = new FileOutputStream("d:" + excelName + "_phone.xls");
//			wkb.write(output);
//			output.flush();
		} catch (Exception e) {
			EmpExecutionContext.error(e, "手机号码文件生成Excel出现异常！");
		}
		return wkb;
	}

	public static void main(String[] args) throws Exception {
		ParamTool paramTool = 	new ParamTool(); 
//		List<List<Param>> createList = paramTool.convertParam("<div class=\"editor-content J-editor-content\"><div class=\"editor-keyframe J-keyframe active\" data-type=\"image\"><div class=\"keyframe-content J-keyframe-content\"><div class=\"editor-img J-editor-img\" data-size=\"353177\" data-type=\"image\"><img class=\"J-main-img\" src=\"ueditor/jsp/upload/image/20180403/1522726941965082346.jpg\" /><div class=\"J-module-html\"><div id=\"TEXT_MODULE_3_show\" class=\"J-add-module\" style=\"position: absolute; top: 95px; left: 76px; z-index: 39; transform-origin: center center 0px; transform: rotate(0deg);\"><div id=\"TEXT_MODULE_3_inner_show\" class=\"editor-resize-text\" style=\"font-family: 黑体; font-size: 23px; line-height: 23px; font-weight: 400; font-style: normal; text-decoration: none; text-align: left; color: rgb(255, 255, 255); background-color: rgb(25, 170, 0); width: 232px;\">         {#图参1#}{#图参2#}</div></div><div id=\"TEXT_MODULE_4_show\" class=\"J-add-module\" style=\"position: absolute; top: 36px; left: 65px; z-index: 39; transform-origin: center center 0px; transform: rotate(0deg);\"><div id=\"TEXT_MODULE_4_inner_show\" class=\"editor-resize-text\" style=\"font-family: 黑体; font-size: 23px; line-height: 23px; font-weight: 400; font-style: normal; text-decoration: none; text-align: left; color: rgb(86, 225, 187); background-color: rgb(255, 255, 255); width: 232px;\">         {#图参1#}{#图参2#}{#图参3#}1</div></div></div><div class=\"img-edit-layer J-img-edit-layer\"><div class=\"edit-bg\"></div><div class=\"eidt-content\"><div class=\"btn edit-btn J-img-edit\"></div><div class=\"btn dele-btn J-img-dele\"></div></div></div></div><div class=\"editor-text J-edit-text\" contenteditable=\"true\">      {#参数1#}{#参数2#}{#参数3#}{#参数1#}</div></div></div><div class=\"editor-keyframe J-keyframe\" data-type=\"text\"><div class=\"keyframe-content J-keyframe-content\"><div class=\"editor-text J-edit-text\" contenteditable=\"true\">      {#参数1#}{#参数2#}{#参数3#}{#参数5#}</div></div></div></div>");
		List<List<Param>> createList = paramTool.convertParam("<div class=\"editor-content J-editor-content\"><div class=\"editor-keyframe J-keyframe\" data-type=\"text\"><div class=\"keyframe-content J-keyframe-content\"><div class=\"editor-text J-edit-text\" contenteditable=\"true\">文本{#参数1#}{#参数2#}</div></div></div><div class=\"editor-keyframe J-keyframe active\" data-type=\"image\"><div class=\"keyframe-content J-keyframe-content\"><div class=\"editor-img J-editor-img\" data-size=\"44115\" data-type=\"image\"><img class=\"J-main-img\" src=\"ueditor/jsp/upload/image/20180403/1522751638832039118.jpg\"><div class=\"J-module-html\"><div id=\"TEXT_MODULE_0_show\" class=\"J-add-module\" style=\"position: absolute; top: 24px; left: 25px; z-index: 39; transform-origin: center center 0px; transform: rotate(0deg);\"><div id=\"TEXT_MODULE_0_inner_show\" class=\"editor-resize-text\" style=\"font-family: 黑体; font-size: 23px; line-height: 23px; font-weight: 400; font-style: normal; text-decoration: none; text-align: left; color: rgb(86, 225, 187); background-color: rgb(255, 255, 255); width: 230px;\">请双击输入文字{#图参1#}{#图参2#}</div></div></div></div><div class=\"editor-text J-edit-text\" contenteditable=\"true\">图文参数{#参数1#}{#参数6#}</div></div><div class=\"keyframe-edit-content J-keyframe-edit-content\"><div class=\"edit-btn J-edit-keyframe-btn\" data-type=\"dele\"><p class=\"edit-icon J-edit-icon dele\"></p></div><div class=\"edit-btn J-edit-keyframe-btn\" data-type=\"up\"><p class=\"edit-icon J-edit-icon up\"></p></div><div class=\"edit-btn J-edit-keyframe-btn\" data-type=\"down\"><p class=\"edit-icon J-edit-icon down\"></p></div><div class=\"edit-btn J-edit-keyframe-btn\" data-type=\"params\">参数</div><div class=\"edit-btn J-edit-keyframe-btn\" data-type=\"text\">配文</div></div></div><div class=\"editor-keyframe J-keyframe\" data-type=\"chart\"><div class=\"keyframe-content J-keyframe-content\"><div class=\"editor-img J-editor-img\" data-size=\"17116\" data-type=\"chart\"><img src=\"pythonPicture/201804/pie03183620235.png\"><div class=\"J-chart-data\" style=\"display:none;\">{\"chartType\":\"1\",\"chartTitle\":\"标题\",\"ptType\":\"3\",\"color\":\"#BCEE68,#B0E2FF,#D15FEE,#CD950C,#98FB98,#AB82FF,#836FFF,#CAFF70\",\"rowValue\":\"1,1,1,1\",\"pictureUrl\":\"pythonPicture/201804/pie03183620235.png\",\"pictureSize\":17116,\"barColName\":\"{#行标题1#},{#行标题2#},{#行标题3#},{#行标题4#}\",\"parmValue\":\"#行标题1#,#1行1列#,#行标题2#,#2行1列#,#行标题3#,#3行1列#,#行标题4#,#4行1列#\"}</div></div></div></div><div class=\"editor-keyframe J-keyframe\" data-type=\"chart\"><div class=\"keyframe-content J-keyframe-content\"><div class=\"editor-img J-editor-img\" data-size=\"14601\" data-type=\"chart\"><img src=\"pythonPicture/201804/line03183656992.png\"><div class=\"J-chart-data\" style=\"display:none;\">{\"chartType\":\"3\",\"chartTitle\":\"标题\",\"ptType\":\"2\",\"pictureUrl\":\"pythonPicture/201804/line03183656992.png\",\"pictureSize\":14601,\"barRowName\":\"列标题1,列标题2,列标题3\",\"barColName\":\"行标题1,行标题2,行标题3\",\"barValue\":\"1,1,1@1,1,1@1,1,1\",\"barTableVal\":\",列标题1,列标题2,列标题3@行标题1,1,1,1@行标题2,1,1,1@行标题3,1,1,1\",\"parmValue\":\"#1行1列#,#1行2列#,#1行3列#,#2行1列#,#2行2列#,#2行3列#,#3行1列#,#3行2列#,#3行3列#\"}</div></div></div></div></div><div class=\"editor-content J-editor-content\"><div class=\"editor-keyframe J-keyframe\" data-type=\"text\"><div class=\"keyframe-content J-keyframe-content\"><div class=\"editor-text J-edit-text\" contenteditable=\"true\">文本{#参数1#}{#参数2#}</div></div></div><div class=\"editor-keyframe J-keyframe active\" data-type=\"image\"><div class=\"keyframe-content J-keyframe-content\"><div class=\"editor-img J-editor-img\" data-size=\"44115\" data-type=\"image\"><img class=\"J-main-img\" src=\"ueditor/jsp/upload/image/20180403/1522751638832039118.jpg\"><div class=\"J-module-html\"><div id=\"TEXT_MODULE_0_show\" class=\"J-add-module\" style=\"position: absolute; top: 24px; left: 25px; z-index: 39; transform-origin: center center 0px; transform: rotate(0deg);\"><div id=\"TEXT_MODULE_0_inner_show\" class=\"editor-resize-text\" style=\"font-family: 黑体; font-size: 23px; line-height: 23px; font-weight: 400; font-style: normal; text-decoration: none; text-align: left; color: rgb(86, 225, 187); background-color: rgb(255, 255, 255); width: 230px;\">请双击输入文字{#图参1#}{#图参2#}</div></div></div></div><div class=\"editor-text J-edit-text\" contenteditable=\"true\">图文参数{#参数1#}{#参数6#}</div></div><div class=\"keyframe-edit-content J-keyframe-edit-content\"><div class=\"edit-btn J-edit-keyframe-btn\" data-type=\"dele\"><p class=\"edit-icon J-edit-icon dele\"></p></div><div class=\"edit-btn J-edit-keyframe-btn\" data-type=\"up\"><p class=\"edit-icon J-edit-icon up\"></p></div><div class=\"edit-btn J-edit-keyframe-btn\" data-type=\"down\"><p class=\"edit-icon J-edit-icon down\"></p></div><div class=\"edit-btn J-edit-keyframe-btn\" data-type=\"params\">参数</div><div class=\"edit-btn J-edit-keyframe-btn\" data-type=\"text\">配文</div></div></div><div class=\"editor-keyframe J-keyframe\" data-type=\"chart\"><div class=\"keyframe-content J-keyframe-content\"><div class=\"editor-img J-editor-img\" data-size=\"17116\" data-type=\"chart\"><img src=\"pythonPicture/201804/pie03183620235.png\"><div class=\"J-chart-data\" style=\"display:none;\">{\"chartType\":\"1\",\"chartTitle\":\"标题\",\"ptType\":\"3\",\"color\":\"#BCEE68,#B0E2FF,#D15FEE,#CD950C,#98FB98,#AB82FF,#836FFF,#CAFF70\",\"rowValue\":\"1,1,1,1\",\"pictureUrl\":\"pythonPicture/201804/pie03183620235.png\",\"pictureSize\":17116,\"barColName\":\"{#行标题1#},{#行标题2#},{#行标题3#},{#行标题4#}\",\"parmValue\":\"#行标题1#,#1行1列#,#行标题2#,#2行1列#,#行标题3#,#3行1列#,#行标题4#,#4行1列#\"}</div></div></div></div><div class=\"editor-keyframe J-keyframe\" data-type=\"chart\"><div class=\"keyframe-content J-keyframe-content\"><div class=\"editor-img J-editor-img\" data-size=\"14601\" data-type=\"chart\"><img src=\"pythonPicture/201804/line03183656992.png\"><div class=\"J-chart-data\" style=\"display:none;\">{\"chartType\":\"3\",\"chartTitle\":\"标题\",\"ptType\":\"2\",\"pictureUrl\":\"pythonPicture/201804/line03183656992.png\",\"pictureSize\":14601,\"barRowName\":\"列标题1,列标题2,列标题3\",\"barColName\":\"行标题1,行标题2,行标题3\",\"barValue\":\"1,1,1@1,1,1@1,1,1\",\"barTableVal\":\",列标题1,列标题2,列标题3@行标题1,1,1,1@行标题2,1,1,1@行标题3,1,1,1\",\"parmValue\":\"#1行1列#,#1行2列#,#1行3列#,#2行1列#,#2行2列#,#2行3列#,#3行1列#,#3行2列#,#3行3列#\"}</div></div></div></div></div>");
		
//		for(List<Param> plist: createList){
//			
//			for(Param p:plist){
//				System.out.println(p.getType()+"--"+p.getValue());
//			}
//		}
//		crateExcel(createList);
		
	}

}
