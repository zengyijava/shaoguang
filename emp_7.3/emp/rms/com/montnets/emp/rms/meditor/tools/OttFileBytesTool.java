package com.montnets.emp.rms.meditor.tools;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.montnets.emp.common.context.EmpExecutionContext;

/**
 * @Description: OTT 文件获取字节工具类
 * @Auther:xuty
 * @Date: 2018/7/31 16:51
 */
public class OttFileBytesTool {
	/**
	 * 读取模板目标文件夹下的所有资源文件信息
	 * 
	 * @param tmId
	 */
	public void readBytes(Long tmId) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		// Map<文件名, String>
		Map<String, String> map = new HashMap<String, String>();

	}

	/**
	 * filePath: 资源文件存储位置 title ： 模板标题 list ： 资源文件集合 获取OTT文件相关字节流
	 * 
	 * @return
	 */
	public byte[] getOttFileBytes(String filePath, String title,Map<String, byte[]> map) {
		byte[] resultBytes = null;
		try {
			// 最终返回的字节数组

			// 用于记录ott总长度的位置
			int ottBgIndex = 0;
			// 记录每次bytes[]下标位置
			int index = 0;
			// 4 数据总长度 指接下来中数据总长度，含smil/mrcsl控制文件和子文件.直接将整型数4字节以2进制形式存储，为网络字节序(下同)

			// 设置一个，每次 装载信息的容器
			byte[] bytes = new byte[1024 * 1024 * 8];

			// 1 版本 0x01 表示V1.0
			bytes[index] = (byte) 01;// 版本:01 表示V1.0
			index++;

			// 1 文件编码格式 0:明文,其他:待定,如是否有加密,是否有压缩等
			bytes[index] = (byte) 00;// 文件编码格式:0:明文
			index++;
			
			// 文件总长度
			//ott文件总长度
			int ottTotalLen = 0;
			ottBgIndex = index;
			index +=4;
			// 1 总文件数 包含后面的所有文件数
			int fileCount = map.size();
			System.arraycopy(int2bytes(fileCount), 3, bytes, index, 1);
			index += 1;
			ottTotalLen= ottTotalLen +1;
			
			Set<Entry<String, byte[]>> entrySet = map.entrySet();
			for (Entry<String, byte[]> entry : entrySet) {// 获取mrcsl文件放在第一个位置
				if (entry.getKey().contains(".mrcsl")) {
					// 1 文件类型
					int type = getFileType(entry.getKey()); // 8:mrcsl(montnets
															// rcs language)
															// 卡片协议文件
					System.arraycopy(int2bytes(type), 3, bytes, index, 1);
					index += 1;
					ottTotalLen= ottTotalLen +1;
					// 4 MRCSL内容字节长度
					byte[] content = entry.getValue();
					int mrcslContLength = content.length;
					System.arraycopy(int2bytes(mrcslContLength), 0, bytes,index, 4);
					index += 4;
					ottTotalLen= ottTotalLen +4;
					// MRCSL 文件内容
					System.arraycopy(content, 0, bytes, index, mrcslContLength);
					index += mrcslContLength;
					ottTotalLen= ottTotalLen + mrcslContLength;
				} else {
					continue;
				}
			}
			for (Entry<String, byte[]> entry : entrySet) {
				if (entry.getKey().contains(".mrcsl")) {
					continue;
				}
				// 1 文件类型 2:image 4:video 5:audio,8:mrcsl(montnets rcs language)
				int fileType = getFileType(entry.getKey());
				System.arraycopy(int2bytes(fileType), 3, bytes, index, 1);
				index++;
				ottTotalLen= ottTotalLen + 1;
				// 1 文件名的字节长度
				String fileName = entry.getKey();
				int rfileNameLegth = fileName.getBytes().length;
				System.arraycopy(int2bytes(rfileNameLegth), 3, bytes, index, 1);
				index++;
				ottTotalLen= ottTotalLen + 1;
				// 文件名
				System.arraycopy(fileName.getBytes(), 0, bytes, index, rfileNameLegth);
				index+=rfileNameLegth;
				ottTotalLen= ottTotalLen + rfileNameLegth;
				// 4 内容长度
				byte[] content = entry.getValue();
				int contLenth = content.length;
				System.arraycopy(int2bytes(contLenth), 0, bytes, index, 4);
				index += 4;
				ottTotalLen= ottTotalLen + 4;
				// 文件内容
				System.arraycopy(content, 0, bytes, index, contLenth);
				index += contLenth;
				ottTotalLen= ottTotalLen + contLenth;
				
			}
			//最后写入ott总长度
			System.arraycopy(int2bytes(ottTotalLen), 0, bytes, ottBgIndex, 4);
			resultBytes = new byte[index];
			System.arraycopy(bytes, 0, resultBytes, 0, index);
			
		} catch (Exception e) {
			EmpExecutionContext.error(e,"组MRCSL协议文件出现异常");
		}
		return resultBytes;
	}

	// Map<序号 ,Map<文件名, byte[]>>
	public Map<String, byte[]> getOttFileList(String filePath) {
		Map<String, byte[]> map = new LinkedHashMap<String, byte[]>();
		File file = new File(filePath);
		if (!file.isDirectory()) {
			EmpExecutionContext.info(filePath + "路径不是一个文件夹");
			return null;
		}
		File[] tempList1 = file.listFiles();
		List<File> tempList = Arrays.asList(tempList1);
		if (tempList == null || tempList.size() <= 0) {
			EmpExecutionContext.info(filePath + "src文件夹 目录为空");
			return null;
		}
		for (int i = 0; i < tempList.size(); i++) {
			File tempFile = tempList.get(i);
			String finlName = tempFile.getName();
			byte[] tempBytes = getFileBytes(tempFile);
			map.put(finlName, tempBytes);
		}
		return map;
	}

	/**
	 * 读取 src 目录下的资源文件 字节
	 * 
	 * @param tempFile
	 * @return
	 */
	public byte[] getFileBytes(File tempFile) {
		FileInputStream fis = null;
		ByteArrayOutputStream outStream = null;
		try {
			fis = new FileInputStream(tempFile);
			outStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = -1;
			while ((len = fis.read(buffer)) != -1) {
				outStream.write(buffer, 0, len);
			}

		} catch (Exception e) {
			EmpExecutionContext.error(e, "读取文件字节流出现异常！");
		} finally {
			try {
				if (null != outStream) {
					outStream.close();
				}
				if (null != fis) {
					fis.close();
				}
			} catch (Exception e) {
				EmpExecutionContext.error(e, "读取src目录文件关闭流出现异常");
			}

		}
		return outStream==null?null:outStream.toByteArray();
	}

	/**
	 * 整型转字节
	 * 
	 * @param num
	 *            ：待转整型数据
	 * @return
	 */
	private byte[] int2bytes(int num) {
		byte[] b = new byte[4];
		for (int i = 0; i < 4; i++) {
			b[i] = (byte) (num >>> (24 - i * 8));
		}
		return b;
	}
	
	/**
	 * 根据文件名返回文件类型
	 * @param fileName
	 * @return
	 */
	private int getFileType(String fileName) {
		int type = 0;
		if (fileName.contains(".mp3")) {// 5.audio
			type = 5;
		}
		if (fileName.contains(".mp4")||fileName.contains(".avi")) {// 4:video
			type = 4;
		}
		if (fileName.contains(".png") || fileName.contains(".jpg")) {// 2.img
			type = 2;
		}
		if (fileName.contains(".mrcsl")) {// 8.mrcsl
			type = 8;
		}
		return type;
	}
}
