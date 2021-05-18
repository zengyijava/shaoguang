package com.montnets.emp.rms.tools;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.montnets.emp.common.context.EmpExecutionContext;
import org.apache.commons.codec.binary.Base64;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import java.awt.*;
import java.io.File;

/**
 * 饼图报表类
 * 
 * @author shouc
 *
 */
public class PieReportFormImpl implements ReportForm {

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

			String colnas = jsonObject.get("colnas").getAsString();
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
//			Font font = new Font(fmt, bold, size);
			Font font = new Font("黑体", Font.BOLD, 20);

			String fileUrl = fileOutDir + jsonObject.get("fina").getAsString();

			DefaultPieDataset pds = new DefaultPieDataset();
			for (int i = 0; i < cmax; i++) {
				pds.setValue("test" + i+1, 100);
			}
			/**
			 * 生成一个饼图的图表
			 * 
			 * 分别是:显示图表的标题、需要提供对应图表的DateSet对象、是否显示图例、是否生成贴士以及是否生成URL链接
			 */
			JFreeChart chart = ChartFactory.createPieChart(cont, pds, true, false, true);
			// 设置图片标题的字体
			chart.getTitle().setFont(Titlefont);

			// 得到图块,准备设置标签的字体
			PiePlot plot = (PiePlot) chart.getPlot();

			// 设置标签字体
			plot.setLabelFont(font);

			// 设置图例项目字体
			chart.getLegend().setItemFont(font);

			/**
			 * 设置开始角度(弧度计算)
			 * 
			 * 度 0° 30° 45° 60° 90° 120° 135° 150° 180° 270° 360° 弧度 0 π/6 π/4
			 * π/3 π/2 2π/3 3π/4 5π/6 π 3π/2 2π
			 */
			plot.setStartAngle(3.14f / 2f);

			// 设置背景图片,设置最大的背景
			// Image img = ImageIO.read(new File("d:/sunset.jpg"));
			// chart.setBackgroundImage(img);

			// 设置plot的背景图片
			// img = ImageIO.read(new File("d:/Water.jpg"));
			// plot.setBackgroundImage(img);

			// 设置plot的前景色透明度
			plot.setForegroundAlpha(0.7f);

			// 设置plot的背景色透明度
			plot.setBackgroundAlpha(0.0f);

			// 设置标签生成器(默认{0})
			// {0}:key {1}:value {2}:百分比 {3}:sum
			plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}({1})/{2}"));

			// 将内存中的图片写到本地硬盘
			ChartUtilities.saveChartAsJPEG(new File(fileUrl), chart, 400, 400);
		} catch (Exception e) {
            EmpExecutionContext.error(e, "发现异常");
		}
	}
}
