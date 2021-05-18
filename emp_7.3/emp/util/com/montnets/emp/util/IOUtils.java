package com.montnets.emp.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.List;

import com.montnets.emp.common.context.EmpExecutionContext;

public class IOUtils
{
	private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

	public static int copy(InputStream input, OutputStream output)
			throws IOException
	{
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		int count = 0;
		int n = 0;
		while (-1 != (n = input.read(buffer)))
		{
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}

	public static void closeQuietly(Closeable closeable)
	{
		try
		{
			if (closeable != null)
			{
				closeable.close();
			}
		} catch (IOException ioe)
		{
			EmpExecutionContext.error(ioe, "关闭流异常！");
		}
	}
	
	/**
	 * IO关闭工具类
	 * @param input
	 * @param ouput
	 * @param reader
	 * @param writer
	 * @param clazz 具体是指哪个类，方便回溯查询异常来源
	 * @throws IOException
	 */
	public static void closeIOs(
		InputStream input,
		OutputStream ouput,
		BufferedReader reader,
		BufferedWriter writer,
		Class<?> clazz) throws IOException{
		boolean isError = false;
		StringBuffer errMsg = new StringBuffer();
		if(input != null){
			try{
				input.close();
			}catch(IOException e){
				isError = true;
				errMsg.append("[InputStream关闭异常：+e.getMessage()] - ");
			}
		}
		if(ouput != null){
			try {
				ouput.close();
			}catch(IOException e){
				isError = true;
				errMsg.append("[OutputStream关闭异常：+e.getMessage()] - ");
			}
		}
		if(reader != null){
			try {
				reader.close();
			} catch (IOException e) {
				isError = true;
				errMsg.append("[BufferedReader关闭异常：+e.getMessage()] - ");
			}
		}
		if(writer != null){
			try {
				writer.close();
			} catch (IOException e) {
				isError = true;
				errMsg.append("[BufferedWriter关闭异常：+e.getMessage()] - ");
			}
		}
		if(isError){
			String className = clazz != null?clazz.getName():"";
			throw new IOException("["+className+"]IO关闭出现异常，详情："+errMsg.toString());
		}
	}
	
	public static void closeIOs(
			InputStream ins,
			BufferedInputStream bins,
			OutputStream outs,
			BufferedOutputStream bouts,
			Class<?> clazz
			) throws IOException{
		boolean isError = false;
		try{
			closeIOs(ins, outs, null, null, clazz);
		}catch(IOException e){
			isError = true;
		}
		try{
			closeIOs(bins, bouts, null, null, clazz);
		}catch(IOException e){
			isError = true;
		}
		if(isError){
			throw new IOException("IO关闭出现异常");
		}
	}
	
	public static void closeReaders(Class<?> clazz,List<BufferedReader> readers) throws IOException{
//		int errorCnt = 0;
//		String errMsg = "";
//		if(null ==readers || readers.size()==0){
//			return;
//		}
//		for(Reader reader:readers){
//			if(reader != null){
//				try {
//					reader.close();
//				} catch (IOException e) {
//					errorCnt++;
//					errMsg = "["+e.getMessage()+"]";
//				}
//			}
//		}
//		if(errorCnt>0){
//			String className = clazz != null?clazz.getName():"";
//			throw new IOException("["+className+"]Reader关闭出现异常，[IO数量："+readers.size()+"，关闭异常数："+errorCnt+"]，异常内容："+errMsg.toString());
//		}
	}
	
	public static void closeReaders(Class<?> clazz,Reader...readers) throws IOException{
		boolean isError = false;
		StringBuffer errMsg = new StringBuffer();
		if(null == readers){
			return;
		}
		for(Reader reader:readers){
			if(reader != null){
				try {
					reader.close();
				} catch (IOException e) {
					isError = true;
					errMsg.append("[reader关闭异常：+e.getMessage()] - ");
				}
			}
		}
		if(isError){
			String className = clazz != null?clazz.getName():"";
			throw new IOException("["+className+"]IO关闭出现异常，详情："+errMsg.toString());
		}
	}
	
	public static void closeWriters(Class<?> clazz,Writer...writers) throws IOException{
		boolean isError = false;
		StringBuffer errMsg = new StringBuffer();
		if(null == writers){
			return;
		}
		for(Writer writer:writers){
			if(writer != null){
				try {
					writer.close();
				} catch (IOException e) {
					isError = true;
					errMsg.append("[writer关闭异常：+e.getMessage()] - ");
				}
			}
		}
		if(isError){
			String className = clazz != null?clazz.getName():"";
			throw new IOException("["+className+"]IO关闭出现异常，详情："+errMsg.toString());
		}
	}
	
	
	public static void closeInputStream(Class<?> clazz,InputStream...inputs) throws IOException{
		boolean isError = false;
		StringBuffer errMsg = new StringBuffer();
		if(null == inputs){
			return;
		}
		for(InputStream input:inputs){
			if(input != null){
				try {
					input.close();
				} catch (IOException e) {
					isError = true;
					errMsg.append("[input关闭异常：+e.getMessage()] - ");
				}
			}
		}
		if(isError){
			String className = clazz != null?clazz.getName():"";
			throw new IOException("["+className+"]IO关闭出现异常，详情："+errMsg.toString());
		}
	}
}
