package com.montnets.emp.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;

import com.montnets.emp.common.context.EmpExecutionContext;

public class FileUtils {
	
	private static final int DEFAULT_BUFFER_SIZE = 1024;

	/**
	 * 将字符串写入指定文件(当指定的父路径中文件夹不存在时，会最大限度去创建，以保证保存成功！)
	 * 
	 * @param res
	 *            原字符串
	 * @param filePath
	 *            文件路径
	 * @return 成功标记
	 */
	public static boolean string2File(String res, String filePath) {
		boolean flag = true;
		BufferedReader bufferedReader = null;
		BufferedWriter bufferedWriter = null;
		try {
			File distFile = new File(filePath);
			if (!distFile.getParentFile().exists())
				distFile.getParentFile().mkdirs();
			bufferedReader = new BufferedReader(new StringReader(res));
			bufferedWriter = new BufferedWriter(new FileWriter(distFile));
			char buf[] = new char[1024]; // 字符缓冲区
			int len;
			while ((len = bufferedReader.read(buf)) != -1) {
				bufferedWriter.write(buf, 0, len);
			}
			bufferedWriter.flush();
			bufferedReader.close();
//			bufferedWriter.close();
		} catch (IOException e) {
			EmpExecutionContext.error(e,"将字符串写入指定文件异常！");
			flag = false;
			return flag;
		} finally {
			if(bufferedWriter != null){
				try {
					bufferedWriter.close();
				} catch (IOException e) {
					EmpExecutionContext.error(e,"关闭文件流异常！");
				}
			}
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					EmpExecutionContext.error(e,"关闭文件流异常！");
				}
			}
		}
		return flag;
	}

	/**
	 * 文本文件转换为指定编码的字符串
	 * 
	 * @param file
	 *            文本文件
	 * @param encoding
	 *            编码类型
	 * @return 转换后的字符串
	 * @throws IOException
	 */
	public static String file2String(File file, String encoding) {
		InputStreamReader reader = null;
		StringWriter writer = new StringWriter();
		try {
			if (encoding == null || "".equals(encoding.trim())) {
				reader = new InputStreamReader(new FileInputStream(file),
						encoding);
			} else {
				reader = new InputStreamReader(new FileInputStream(file));
			}
			// 将输入流写入输出流
			char[] buffer = new char[DEFAULT_BUFFER_SIZE];
			int n = 0;
			while (-1 != (n = reader.read(buffer))) {
				writer.write(buffer, 0, n);
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"文本文件转换为指定编码的字符串异常！");
			return null;
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
					EmpExecutionContext.error(e,"关闭文件流异常！");
				}
		}
		// 返回转换结果
//		if (writer != null)
		return writer.toString();
//		else
//			return null;
	}
	
	/**
	 * 删除目录及目录下的文件
	 * @param dir
	 * @return
	 */
	public static boolean deleteDir(File dir) {

		if (dir.isDirectory())
		{
			//获取目录下的文件及目录
			String[] children = dir.list();
			//遍历删除
			for (int i = 0; i < children.length; i++) 
			{
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}
	
	/**
	 * DESC: 判断文本文件的编码格式
	 * @param inp 文本文件流
	 * @return 字符集编码
	 */
	public static String get_charset(InputStream objInputStream)
	{
		if(null == objInputStream)
		{
			return null;
		}
		// 默认返回GBK
		String charset = "GBK";
		
		try
		{
			byte[] first3Bytes = new byte[3];
			
			BufferedInputStream objbufInStream = new BufferedInputStream(objInputStream);
			objbufInStream.mark(0);
			// 通过读取文件流的开头标示判断格式
			int read = objbufInStream.read(first3Bytes, 0, 3);
			if(read == -1)
				return charset;
			if(first3Bytes[0] == (byte) 0xFF 
					&& first3Bytes[1] == (byte) 0xFE)
			{
				charset = "Unicode";
			}
			else if(first3Bytes[0] == (byte) 0xFE 
					&& first3Bytes[1] == (byte) 0xFF)
			{
				charset = "UTF-16BE";
			}
			else if(first3Bytes[0] == (byte) 0xEF 
					&& first3Bytes[1] == (byte) 0xBB 
					&& first3Bytes[2] == (byte) 0xBF)
			{
				charset = "UTF-8";
			}
			
			objbufInStream.reset();
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "判断文本文件编码格式异常！");
		}
		
		return charset;
	}

}
