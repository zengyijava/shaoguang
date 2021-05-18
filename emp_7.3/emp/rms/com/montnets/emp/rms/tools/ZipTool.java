package com.montnets.emp.rms.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import com.montnets.emp.common.context.EmpExecutionContext;

/**
 * 将文件或是文件夹打包压缩成zip格式
 * 
 * @ClassName: TestCreateZip
 * @Description: TODO
 * @author xuty
 * @date 2018-4-4 下午1:51:14
 * 
 */
public class ZipTool {

	private ZipTool() {
	};

	/**
	 * 创建ZIP文件
	 * 
	 * @param sourcePath
	 *            文件或文件夹路径
	 * @param zipPath
	 *            生成的zip文件存在路径（包括文件名）
	 */
	public static void createZip(String sourcePath, String zipPath) {
		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		try {
			fos = new FileOutputStream(zipPath);
			zos = new ZipOutputStream(fos);
			zos.setEncoding("gbk");// 此处修改字节码方式。
			// createXmlFile(sourcePath,"293.xml");
			writeZip(new File(sourcePath), "", zos);
		} catch (FileNotFoundException e) {
			EmpExecutionContext.error(e, "创建ZIP文件失败");
		} finally {
			try {
				if (zos != null) {
					zos.close();
				}
			} catch (IOException e) {
				EmpExecutionContext.error(e, "创建ZIP文件失败");
			}
			if(fos != null){
				try {
					fos.close();
				} catch (IOException e) {
					EmpExecutionContext.error(e, "文件关闭失败");
				}
			}

		}
	}

	private static void writeZip(File file, String parentPath,
			ZipOutputStream zos) {
		if (file.exists()) {
			if (file.isDirectory()) {// 处理文件夹
//				parentPath += file.getName() + File.separator;
				parentPath += file.getName() + "/";
				File[] files = file.listFiles();
				if (files.length != 0) {
					for (File f : files) {
						if(f.getName().contains(".zip")){
							continue;
						}
						writeZip(f, parentPath, zos);
					}
				} else { // 空目录则创建当前目录
					try {
						zos.putNextEntry(new ZipEntry(parentPath));
					} catch (IOException e) {
						EmpExecutionContext.error(e, "创建ZIP文件失败");
					}
				}
			} else {
				FileInputStream fis = null;
				try {
					fis = new FileInputStream(file);
					ZipEntry ze = new ZipEntry(parentPath + file.getName());
					zos.putNextEntry(ze);
					byte[] content = new byte[1024];
					int len;
					while ((len = fis.read(content)) != -1) {
						zos.write(content, 0, len);
						zos.flush();
					}

				} catch (FileNotFoundException e) {
					EmpExecutionContext.error(e, "创建ZIP文件失败");
				} catch (IOException e) {
					EmpExecutionContext.error(e, "创建ZIP文件失败");
				} finally {
					try {
						if (fis != null) {
							fis.close();
						}
					} catch (IOException e) {
						EmpExecutionContext.error(e, "创建ZIP文件失败");
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		ZipTool.createZip("E:\\aliyun\\1052\\", "E:\\aliyun\\1052.zip");
	}
}