package com.montnets.emp.netnews.util;

import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;

import java.io.*;
import java.util.List;

public class wx_FileUtil {
	/**
	 * 读取文件内容
	 * 
	 * @param filePath
	 * @return
	 */
	public String read(String filePath) {
		BufferedReader br = null;
		String line = null;
		StringBuffer buf = new StringBuffer();
		FileInputStream fi = null;
		InputStreamReader ir = null;
		try {
			// 根据文件路径创建缓冲输入流
			//br = new BufferedReader(new FileReader(filePath));

			fi = new FileInputStream(filePath);// 实例了文件读入流，参数是文件输出路径
			ir = new InputStreamReader(fi, "utf-8");// 在读字符流的时候做编码格式转化，以免乱码！
			br = new BufferedReader(ir);// 读入文本字符流

			
			// 循环读取文件的每一行, 对需要修改的行进行修改, 放入缓冲对象中
			while ((line = br.readLine()) != null) {
				buf.append(line);
				buf.append(System.getProperty("line.separator"));
			}
		} catch (Exception e) {
            EmpExecutionContext.error(e, "发现异常！");
		} finally {
			// 关闭流
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					br = null;
				}
			}
			// 关闭流
			if (ir != null) {
				try {
					ir.close();
				} catch (IOException e) {
					ir = null;
				}
			}
			// 关闭流
			if (fi != null) {
				try {
					fi.close();
				} catch (IOException e) {
					fi = null;
				}
			}
		}
		String context=buf.toString();
		CommonBiz biz=new CommonBiz();
		String[] file=biz.getALiveFileServer();

		String cont="";
		if(file!=null&&file.length>0){
			List<String> out= StaticValue.getFileServerOuterQueue();
			if(out!=null&&out.size()>0){
				for(int k=0;k<out.size();k++){
					String url=out.get(k);
					context=context.replaceAll(url, file[1]);
					
				}
			}
		}
		return context;
	}
	
	/**
	 * 将内容回写到文件中
	 * 
	 * @param filePath
	 * @param content
	 */
	public void write(String filePath, String content) {
		BufferedWriter bw = null;
		FileOutputStream fs = null;
		OutputStreamWriter ow = null;
		try {
			// 根据文件路径创建缓冲输出流
			//bw = new BufferedWriter(new FileWriter(filePath));

			fs = new FileOutputStream(filePath);// 实例了文件输出流，参数是文件输出路径
			ow = new OutputStreamWriter(fs, "utf-8");// 在写输出流的时候做编码格式转化，以免乱码！
			bw = new BufferedWriter(ow);// 将文本写入字符输出流，缓冲各个字符，从而提供单个字符、数组和字符串的高效写入

			// 将内容写入文件中
			bw.write(content);
		} catch (Exception e) {
            EmpExecutionContext.error(e, "发现异常！");
		} finally {
			// 关闭流
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					bw = null;
				}
			}
			// 关闭流
			if (ow != null) {
				try {
					ow.close();
				} catch (IOException e) {
					ow = null;
				}
			}
			// 关闭流
			if (fs != null) {
				try {
					fs.close();
				} catch (IOException e) {
					fs = null;
				}
			}
		}
	}
	
}
