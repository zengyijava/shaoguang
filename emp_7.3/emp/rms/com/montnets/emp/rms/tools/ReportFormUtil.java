package com.montnets.emp.rms.tools;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.math.BigDecimal;

public class ReportFormUtil {
	/**
	 * createPicture：根据请求参数生成python的饼状图
	 */
	public void createPicture(HttpServletRequest request, String chartType,
			String chartTitle, String nameVariable, String deletePath,
			String color, String barColName, String rowValue,
			String barRowName, String barValue) {
		try {
			String val  = null;
			if(null != request){
				val = request.getParameter("val");
			}
			// 图片保存的位置
			String colors = null;
			String rowNameVal = null;
			String[] args1 = null;
			String barRowNames = null;
			String barColNames = null;
			String barValues = null;
			String pyDir = new File(ReportFormUtil.class.getResource("/").getPath().replaceAll("%20", " ")).getPath();
			pyDir = pyDir.substring(0,pyDir.indexOf("WEB-INF"))+"/rms/mbgl/pythonScript";
//			String pyDir = request.getSession().getServletContext()
//					.getRealPath("/file/pythonScript").replaceAll("%20", " ");
			if (("1").equals(chartType)) {
				// 拼装python脚本所需变量
				colors = AssembleStr(color);
				// 拼装rowName
				if(StringUtils.isNotEmpty(val)){
					barColNames = AssembleRowName(barRowName,barValue,"1");
				}else{
					barColNames = AssembleRowName(barRowName,barValue,"2");
				}
				rowNameVal = AssembleStr(barColNames);
				args1 = new String[] { "python", pyDir + "/pie.py", rowNameVal,
						barValue, colors, chartTitle,
						nameVariable, deletePath };
			} else if (("2").equals(chartType)) {
				// 柱状图折线图表列
				barRowNames = AssembleStr(barRowName);
				// 柱状图表行
				barColNames = AssembleStr(barColName);
				// 柱状图数值(以行为单位，用“,”隔开，换行则用“@”隔开)
				barValues = AssembleBarStr(barValue);
				args1 = new String[] { "python", pyDir + "/bar.py",
						barRowNames, barValues, barColNames, chartTitle,
						nameVariable, deletePath };
			} else if (("3").equals(chartType)) {
				// 柱状图折线图表列
				barRowNames = AssembleStr(barRowName);
				// 柱状图表行
				barColNames = AssembleStr(barColName);
				// 柱状图数值(以行为单位，用“,”隔开，换行则用“@”隔开)
				barValues = AssembleBarStr(barValue);
				args1 = new String[] { "python", pyDir + "/line.py",
						barRowNames, barValues, barColNames, chartTitle,
						nameVariable, deletePath };
			}
			Process pr = Runtime.getRuntime().exec(args1);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					pr.getInputStream()));
			in.close();
			pr.waitFor();
			// 组装需要的bean数据并转换为json返回
		} catch (Exception e) {
			EmpExecutionContext.error(e,"图表生成出现异常");
		} finally {

		}
	}

	private String AssembleBarStr(String barValue) {
		String[] arrBar = barValue.split("@");
		String returnStr = "[";
		for (int i = 0; i < arrBar.length; i++) {
			returnStr = returnStr + "[" + arrBar[i] + "]" + ",";
		}
		String str = "";
		if (barValue.contains("@")) {
			str = returnStr.substring(0, returnStr.length() - 1) + "]";
		} else {
			String barValues[] = barValue.split(",");
			for (int i = 0; i < barValues.length; i++) {
				str = str + "'" + barValues[i] + "',";
			}
			str = str.substring(0, str.length() - 1);
		}
		return str;
	}

	public String AssembleRowName(String str1, String str2, String str3) {
		String resultStr = "";
		String[] arr1 = str1.split(",");
		String[] arr2 = str2.split(",");
		for (int i = 0; i < arr1.length; i++) {
			if (("1").equals(str3)) {
				if (Integer.parseInt(arr2[i]) > 1000000) {
					arr2[i] = BigDecimal.valueOf(
							(float) Integer.parseInt(arr2[i]) / 10000)
							.setScale(1, BigDecimal.ROUND_HALF_UP)
							.doubleValue()
							+ "万";
				}
				resultStr += arr1[i] + "(" + arr2[i] + ")" + ",";
			} else {
				resultStr += arr1[i] + "(n)" + ",";
			}
		}
		return resultStr.substring(0, resultStr.length() - 1);
	}

	public String AssembleStr(String str) {
		String resultStr = "";
		String[] arr = str.split(",");
		for (int i = 0; i < arr.length; i++) {
			if (!str.contains(",")) {
				resultStr += arr[i] + ",";
			} else {
				resultStr += "'" + arr[i] + "',";
			}
		}
		return resultStr.substring(0, resultStr.length() - 1);
	}

	// 删除文件夹中的图片
	public void deleteTmp(String path) {
		// 获得该路径
		File f = new File(path);
		// 获得该文件夹下的所有文件
		String[] list = f.list();
		File temp;
		// 删除结果
		boolean b = true;
		// 循环删除
		for (String s : list) {
			temp = new File(path + "/" + s);
			b = temp.delete();
			if (!b) {
				EmpExecutionContext.error("存在的图片删除不成功");
			} else {
				EmpExecutionContext.info("存在的图片删除成功");
			}
		}
	}
}
