package com.montnets.emp.rms.tools;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.montnets.emp.common.context.EmpExecutionContext;
import org.apache.commons.codec.binary.Base64;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;
import java.io.File;

public class BarReportFormImpl implements ReportForm {

	@Override
    public void createReportForm(String paramContext, String fileOutDir) {
		try {
			JsonParser jsonParser = new JsonParser();
			JsonObject jsonObject = jsonParser.parse(paramContext).getAsJsonObject();
			String tiinfo = jsonObject.get("tiinfo").getAsString();
			tiinfo = tiinfo.substring(1, tiinfo.length() - 1);
			String[] tiinfos = tiinfo.split(",");
			String cont = new String(Base64.decodeBase64(tiinfos[0].getBytes()), "UTF-8");
			String fmt = tiinfos[1];
			String col = tiinfos[2];
			Integer size = Integer.valueOf(tiinfos[3]);
			String unit = tiinfos[4];
			Integer bold = Integer.valueOf(tiinfos[5]);
			Integer italic = Integer.valueOf(tiinfos[6]);
			Integer udline = Integer.valueOf(tiinfos[7]);
			Integer agen = Integer.valueOf(tiinfos[8]);
//			Font Titlefont = new Font(fmt, bold, size);
			Font Titlefont = new Font("黑体", Font.BOLD, 20);

			String colnas = jsonObject.get("rownas").getAsString();
			String[] colnas_ = colnas.split(",");
			Integer cmax = Integer.valueOf(colnas_[0]);
			fmt = colnas_[1];
			col = colnas_[2];
			size = Integer.valueOf(colnas_[3]);
			unit = colnas_[4];
			bold = Integer.valueOf(colnas_[5]);
			italic = Integer.valueOf(colnas_[6]);
			udline = Integer.valueOf(colnas_[7]);
			Integer typeset = Integer.valueOf(colnas_[8]);

			// 如果不使用Font,中文将显示不出来
//			Font cfont = new Font(fmt, bold, size);
			Font cfont = new Font("黑体", Font.BOLD, 10);
			
			String rownas = jsonObject.get("rownas").getAsString();
			String[] rownas_ = rownas.split(",");
			
			Integer rmax = Integer.valueOf(rownas_[0]);
			fmt = rownas_[1];
			col = rownas_[2];
			size = Integer.valueOf(rownas_[3]);
			unit = rownas_[4];
			bold = Integer.valueOf(rownas_[5]);
			italic = Integer.valueOf(rownas_[6]);
			udline = Integer.valueOf(rownas_[7]);
			typeset = Integer.valueOf(rownas_[8]);
			Integer cutlpos = Integer.valueOf(rownas_[9]);
//			Font rfont = new Font(fmt, bold, size);
			Font rfont = new Font("黑体", Font.BOLD, 10);

			String fileUrl = fileOutDir + jsonObject.get("fina").getAsString();

			
			// 种类数据集
			DefaultCategoryDataset ds = new DefaultCategoryDataset();
			for (int i = 0; i < cmax; i++) {
				ds.addValue((double)i+1, i+1+"类", "第一季度"); 
				ds.addValue((double)i+1, i+1+"类", "第二季度"); 
				ds.addValue((double)i+1, i+1+"类", "第三季度"); 
				ds.addValue((double)i+1, i+1+"类", "第四季度"); 
            }

			// 创建柱状图,柱状图分水平显示和垂直显示两种
			JFreeChart chart = ChartFactory.createBarChart(cont, "季度", jsonObject.get("dataunit").getAsString(), ds, PlotOrientation.VERTICAL, true, true, true);

			// 设置整个图片的标题字体
			chart.getTitle().setFont(Titlefont);

			// 设置提示条字体
			chart.getLegend().setItemFont(cfont);

			// 得到绘图区
			CategoryPlot plot = (CategoryPlot) chart.getPlot();
			// 得到绘图区的域轴(横轴),设置标签的字体
			plot.getDomainAxis().setLabelFont(cfont);

			// 设置横轴标签项字体
			plot.getDomainAxis().setTickLabelFont(cfont);

			// 设置范围轴(纵轴)字体
			plot.getRangeAxis().setLabelFont(rfont);
			plot.getRangeAxis().setRange(0, (double)rmax+1);
			// 存储成图片

			// 设置chart的背景图片
			// chart.setBackgroundImage(ImageIO.read(new
			// File("d:/sunset.bmp")));

			// plot.setBackgroundImage(ImageIO.read(new File("d:/Water.jpg")));

			plot.setForegroundAlpha(1.0f);

			ChartUtilities.saveChartAsJPEG(new File(fileUrl), chart, 400, 400);
		} catch (Exception e) {
            EmpExecutionContext.error(e, "发现异常");
		}
	}

	public static void main(String[] args) {
		ReportForm reportForm = new BarReportFormImpl();
		reportForm.createReportForm("", "");
	}

}
