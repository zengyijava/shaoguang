package com.montnets.emp.i18n.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import com.montnets.emp.common.tools.SysuserUtil;
import org.apache.commons.lang.StringUtils;

import com.montnets.emp.common.context.EmpExecutionContext;

public class MessageResource {
	private static final Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();

	public Map<String, String> getMap(String fileName, String lc) {
		Map<String, String> loc = null;
		if (fileName == null || "".equals(fileName)) {
			fileName = "common";
		}

		fileName += ("_" + lc);
		loc = map.get(fileName);
		if (!(loc != null && !loc.isEmpty())) {
			loc = initModule(fileName);
			if (loc != null && !loc.isEmpty()) {
				map.put(fileName, loc);
			}
		}

		return loc;
	}

	private static Map<String, String> initModule(String fileName) {
		String path = MessageResource.getInstance().getClassesPath() + "language/" + fileName + ".menu";
		Map<String, String> moduleMap = new HashMap<String, String>();

		File oldFile = new File(path);
		if (oldFile.exists()) {
			String line = null;
			String charset_name = null;
			InputStream in = null;
			BufferedReader reader = null;
			try {
				charset_name = MessageResource.getInstance().get_charset(new FileInputStream(oldFile));
				in = new BufferedInputStream(new FileInputStream(oldFile));
				reader = new BufferedReader(new InputStreamReader(in, charset_name));
				while ((line = reader.readLine()) != null) {
					String[] arr = split(line);
					if (2 == arr.length) {
						moduleMap.put(arr[0], unicode2String(arr[1]));
					}
				}
			} catch (Exception e) {
				EmpExecutionContext.error(e,"出现异常");
			}finally {
				SysuserUtil.closeStream(in);
				SysuserUtil.closeStream(reader);
			}
		} else {
			EmpExecutionContext.error("文件不存在:" + path);
		}
		return moduleMap;

	}

	private static class SingletonHolder {
		private static final MessageResource instance = new MessageResource();
	}

	private MessageResource() {
	}

	public static MessageResource getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * DESC: 判断文本文件的编码格式
	 * 
	 * @param inp
	 *            文本文件流
	 * @return 字符集编码
	 */
	private String get_charset(InputStream objInputStream) {
		/*
		 * if(null == objInputStream) { return null; }
		 */
		// 默认返回GBK
		String charset = "GBK";
		byte[] first3Bytes = new byte[3];
		try {
			boolean checked = false;
			BufferedInputStream bis = new BufferedInputStream(objInputStream);
			bis.mark(0);
			int read = bis.read(first3Bytes, 0, 3);
			if (read == -1) {
				return charset; // 文件编码为 ANSI
			} else if (first3Bytes[0] == (byte) 0xFF
					&& first3Bytes[1] == (byte) 0xFE) {
				charset = "UTF-16LE"; // 文件编码为 Unicode
				checked = true;
			} else if (first3Bytes[0] == (byte) 0xFE
					&& first3Bytes[1] == (byte) 0xFF) {
				charset = "UTF-16BE"; // 文件编码为 Unicode big endian
				checked = true;
			} else if (first3Bytes[0] == (byte) 0xEF
					&& first3Bytes[1] == (byte) 0xBB
					&& first3Bytes[2] == (byte) 0xBF) {
				charset = "UTF-8"; // 文件编码为 UTF-8
				checked = true;
			}
			bis.reset();
			if (!checked) {
				int loc = 0;
				while ((read = bis.read()) != -1) {
					loc++;
					if (read >= 0xF0)
						break;
					if (0x80 <= read && read <= 0xBF) // 单独出现BF以下的，也算是GBK
						break;
					if (0xC0 <= read && read <= 0xDF) {
						read = bis.read();
						if (0x80 <= read && read <= 0xBF)// 双字节 (0xC0 - 0xDF)
							// (0x80
							// -0xBF),也可能在GB编码内
							continue;
						else
							break;
					} else if (0xE0 <= read && read <= 0xEF) {
						read = bis.read();
						if (0x80 <= read && read <= 0xBF) {
							read = bis.read();
							if (0x80 <= read && read <= 0xBF) {
								charset = "UTF-8";
								break;
							} else
								break;
						} else
							break;
					}
				}
			}
			bis.close();
		} catch (Exception e) {
			EmpExecutionContext.error(e,"判断文本文件的编码格式");
		} finally {
			if (objInputStream != null) {
				try {
					objInputStream.close();
				} catch (IOException e) {
					EmpExecutionContext.error(e,"关闭资源出现异常");
				}
			}
		}
		return charset;
	}

	/**
	 * 解析语言配置文件中的键值对
	 * 
	 * @param str
	 * @return
	 */
	private static String[] split(String str) {
		String[] arr = new String[0];
		int index = str.indexOf("=");
		if (index != -1) {
			arr = new String[2];
			arr[0] = str.substring(0, index);
			arr[1] = str.substring(index + 1);
		}
		return arr;

	}

	private String getClassesPath() {
		String path1 = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();

		if (path1.contains("/WEB-INF/")) {
			path1 = path1.substring(0, path1.lastIndexOf("WEB-INF/"));
		}
		path1 += "WEB-INF/classes/";
		path1 = path1.replaceAll("%20", " ");
		return path1;
	}

	public static String unicode2String(String unicode) {
//		if (StringUtils.isBlank(unicode))
//			return null;
		if (null == unicode)
			return null;
		StringBuilder sb = new StringBuilder();
		int i = -1;
		int pos = 0;

		while ((i = unicode.indexOf("\\u", pos)) != -1) {
			sb.append(unicode.substring(pos, i));
			if (i + 5 < unicode.length()) {
				pos = i + 6;
				sb.append((char) Integer.parseInt(
						unicode.substring(i + 2, i + 6), 16));
			}
		}

		if("".equals(sb.toString())){
			return unicode;
		}
		return sb.toString();
	}

	public static String string2Unicode(String string) {

		if (StringUtils.isBlank(string))
			return null;
		StringBuffer unicode = new StringBuffer();

		for (int i = 0; i < string.length(); i++) {

			// 取出每一个字符
			char c = string.charAt(i);

			// 转换为unicode
			unicode.append("\\u" + Integer.toHexString(c));
		}

		return unicode.toString();
	}

}
