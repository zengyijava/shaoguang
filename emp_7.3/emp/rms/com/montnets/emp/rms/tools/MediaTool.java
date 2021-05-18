package com.montnets.emp.rms.tools;

import com.montnets.emp.common.context.EmpExecutionContext;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.util.ResourceBundle;


/**
 * @author Jason Huang
 * @date 2018年1月15日 下午3:39:47
 */

public class MediaTool {
	public static final String toolPathType;
	
	static {
		ResourceBundle bundle = ResourceBundle.getBundle("SystemGlobals");
		toolPathType = bundle.getString("montnets.rms.videoEncoder.system");
		
		if (StringUtils.isEmpty(toolPathType)) {
			System.out.println("*******操作系统类型缺少配置*******");
			EmpExecutionContext.error("*******操作系统类型缺少配置*******");
		}
	}
	
	/**
	 * 音视频格式转换方法(不进行压缩裁剪操作)
	 * @param toolPath videoEncoder工具路径
	 * @param srcPath 视频源文件(绝对路径,包含后缀名)
	 * @param targetPath 视频输出文件(绝对路径,包含后缀名)
	 * @return 0:指令执行成功
	 */
	public int conversionVideoFormat(String toolPath, String srcPath, String targetPath) {
		if (("Windows").equals(toolPathType)) {
			toolPath = "\"" + toolPath + "\"";
			srcPath = "\"" + srcPath + "\"";
			targetPath = "\"" + targetPath + "\"";
		}else if(("Linux").equals(toolPathType)){
			permitTool(toolPath);
		}
		String cmd = toolPath + " -i " + srcPath +" -c -o " + targetPath;
		EmpExecutionContext.info("音视频格式转换方法:"+cmd);
		return executeCMD(cmd);
	}
	
	/**
	 * 音视频格式转换方法(不进行压缩裁剪操作)
	 * @param toolPath videoEncoder工具路径
	 * @param srcPath 视频源文件(绝对路径,包含后缀名)
	 * @param targetPath 视频输出文件(绝对路径,包含后缀名)
	 * @return 0:指令执行成功
	 */
	public int conversionVideoFormatDeleteC(String toolPath, String srcPath, String targetPath) {
		if (("Windows").equals(toolPathType)) {
			toolPath = "\"" + toolPath + "\"";
			srcPath = "\"" + srcPath + "\"";
			targetPath = "\"" + targetPath + "\"";
		}else if(("Linux").equals(toolPathType)){
			permitTool(toolPath);
		}
		String cmd = toolPath + " -i " + srcPath +" -o " + targetPath;
		EmpExecutionContext.info("音视频格式转换方法:"+cmd);
		return executeCMD(cmd);
	}
	
	/**
	 * 音视频压缩、裁剪处理方法,处理后的音视频文件名为原音视频文件名,调用例子参考下面的main方法
	 * 注意!此方法会阻塞一定时间,用于等待音视频压缩裁剪
	 * @param toolPath     Linux下videoEncoder路径(包含文件名)
	 * @param srcPath      Linux下音视频源文件路径(包含文件名,不能使用反斜杠"\\")
	 * @param targetPath Linux下压缩、裁剪后文件存放路径(目标文件夹不存在会自动创建,不能使用反斜杠"\\",结尾处不需要"/")
	 * @param name          目标文件名(不需要文件类型扩展名)
	 * @param isEncode    是否需要转码,true:需要转码,false:不需要转码
	 * @param mode          裁剪模式,1:删除两边段(只要中间段),2:删除选中段(合并前后两段)
	 * @param rate            视频码率
	 * @param start           开始时间
	 * @param end             结束时间
	 * @param width         视频宽度
	 * @param height        视频高度
	 * @return 0:指令执行成功
	 */
	public int compressVideo(String toolPath, String srcPath, String targetPath, String name, boolean isEncode,
                             int mode, int rate, int start, int end, int width, int height) {
		String tempPath = targetPath + "/" + name + ".mp4";
		
		if (("Windows").equals(toolPathType)) {
			toolPath = "\"" + toolPath + "\"";
			srcPath = "\"" + srcPath + "\"";
			tempPath = "\"" + tempPath + "\"";
		} else {
			targetPath = "/" + targetPath;
		}
		
		// 确保目标文件夹存在
		File folder = new File(targetPath);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		
		String cmd = toolPath + " -i " + srcPath + " -s " + start + " -e " + end + " -m " + mode + " -v " + rate + " -d " + width + "x" + height + " -f 15 -a 32" + (isEncode ? " " : "-c ") + "-o " + tempPath + "";
		EmpExecutionContext.info("视频剪辑*********cmd>:"+cmd);
		return executeCMD(cmd);
	}
	
	/**
	 * 音频压缩、裁剪处理方法,处理后的音频文件名为原音频文件名,调用例子参考下面的main方法
	 * 注意!此方法会阻塞一定时间,用于等待音频压缩裁剪
	 * @param toolPath     Linux下videoEncoder路径(包含文件名)
	 * @param srcPath      Linux下音视频源文件路径(包含文件名,不能使用反斜杠"\\")
	 * @param targetPath Linux下压缩、裁剪后文件存放路径(目标文件夹不存在会自动创建,不能使用反斜杠"\\",结尾处不需要"/")
	 * @param name          目标文件名(不需要文件类型扩展名)
	 * @param isEncode    是否需要转码,true:需要转码,false:不需要转码
	 * @param rate            音频码率(最低32)
	 * @param start           开始时间
	 * @param end             结束时间
	 * @return 0:指令执行成功
	 */
	public int compressAudio(String toolPath, String srcPath, String targetPath, String name, boolean isEncode, int rate, int start, int end) {
		String tempPath = targetPath + "/" + name + ".mp3";
		
		if (("Windows").equals(toolPathType)) {
			toolPath = "\"" + toolPath + "\"";
			srcPath = "\"" + srcPath + "\"";
			tempPath = "\"" + tempPath + "\"";
		} else {
			targetPath = "/" + targetPath;
		}
		
		// 确保目标文件夹存在
		File folder = new File(targetPath);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		
		String cmd = toolPath + " -i " + srcPath + " -s " + start + " -e " + end + (isEncode ? " " : " -c 2 ") + "-a " + rate + " -o " + tempPath;
		
		return executeCMD(cmd);
	}
	
	/**
	 * 执行操作系统cmd指令
	 * @param cmd cmd指令
	 * @return 指令执行结果
	 */
	private int executeCMD(String cmd) {
		Process pos = null;
		try {
			pos = Runtime.getRuntime().exec(cmd);
			//new ProcessClearStream(pos.getInputStream(), "INFO").start();
			//new ProcessClearStream(pos.getErrorStream(), "ERROR").start();
			pos.waitFor();
		} catch (Exception e) {
			EmpExecutionContext.error(e, "执行cmd指令异常！");
		}
		return pos==null?-1:pos.exitValue();
	}
	
	/**
	 * 给工具赋予运行权限,建议在项目中用static代码块调用(只执行一次)
	 * @param toolPath Linux下videoEncoder路径(包含文件名)
	 * @return 0:指令执行成功
	 */
	public int permitTool(String toolPath) {
		String cmd = "chmod 777 " + toolPath;
		Process pos = null;
		try {
			pos = Runtime.getRuntime().exec(cmd);
			pos.waitFor();
		} catch (Exception e) {
			EmpExecutionContext.error(e, "赋予权限异常！");
		}
		return pos==null?-1:pos.exitValue();
	}

	/**
	 * 获取视频首帧图片
	 * @param toolPath
	 * @param srcPath
	 * @param targetPath
	 * @return
	 */
	public int getFirstFrame(String toolPath,String srcPath,String targetPath,String name){
		String tempPath = targetPath + "/" + name + ".png";
		if (("Windows").equals(toolPathType)) {
			toolPath = "\"" + toolPath + "\"";
			srcPath = "\"" + srcPath + "\"";
			tempPath = "\"" + tempPath + "\"";
		} else {
			targetPath = "/" + targetPath;
			srcPath = "/"+srcPath;
			tempPath = "/"+tempPath;
		}
		// 确保目标文件夹存在
		File folder = new File(targetPath);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		String cmd = toolPath + " -i " + srcPath + " -r " + " -o " + tempPath;
		EmpExecutionContext.info("获取视频首帧图片指令:"+cmd);
		return executeCMD(cmd);
	}
	

/*	public static void main(String[] args) {
		String toolPath = "D:/tools/VideoEncoder";
		String mp4Path = "D:/video/VID1.mp4";
		String mp3Path = "D:/video/HumanWorld.mp3";
		String targetPath = "D:/video";
		MediaTool videoTool = new MediaTool();
		long startTime = System.nanoTime(); // 获取开始时间
		System.out.println("ruselt = " + videoTool.compressVideo(toolPath, mp4Path, targetPath, "VID 800kbps", true, 2, 800, 5, 10, 640, 360));
		System.out.println("ruselt = " + videoTool.compressAudio(toolPath, mp3Path, targetPath, "NEWAUDIO", true, 32, 0, 287));
		long endTime = System.nanoTime(); // 获取结束时间
		System.out.println("程序运行时间：" + (endTime - startTime) / 1000000000 + "秒   " + (endTime - startTime) + "纳秒");
	}*/
	
}
