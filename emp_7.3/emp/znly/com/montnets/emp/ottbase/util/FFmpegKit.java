/**
 * Program  : FFmpegKit.java
 * Author   : zousy
 * Create   : 2014-6-25 上午11:04:17
 * company ShenZhen Montnets Technology CO.,LTD.
 *
 */

package com.montnets.emp.ottbase.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.util.TxtFileUtil;

/**
 * 
 * @author   zousy <zousy999@qq.com>
 * @version  1.0.0
 * @2014-6-25 上午11:04:17
 */
public class FFmpegKit
{
	private static String rootClassPath;
	private static String startTime = "00:00:00";
	private static String totalTime = "30";//s
	private static HashMap<String, String> relativeMap = new HashMap<String, String>();
	static{
		relativeMap.put("3gp", "flv");
		relativeMap.put("amr", "mp3");
		relativeMap.put("caf", "mp3");
	}
	
	private static String getCmd(){
		Properties props=System.getProperties();
		String os= props.getProperty("os.name");
		if(os!=null&&os.startsWith("Windows")){
			return getRootClassPath()+"/ffmpeg/ffmpeg.exe";
		}
		return "ffmpeg";
	}
	
	private static int checkFileType(String filename) { 
		String type = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase(); 
		// ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等） 
		if (type.equals("avi")) { 
			return 0; 
		} else if (type.equals("mpg")) { 
			return 0; 
		} else if (type.equals("wmv")) { 
			return 0; 
		} else if (type.equals("3gp")) { 
			return 0; 
		} else if (type.equals("mov")) { 
			return 0; 
		} else if (type.equals("mp4")) { 
			return 0; 
		} else if (type.equals("asf")) { 
			return 0; 
		} else if (type.equals("asx")) { 
			return 0; 
		} else if (type.equals("flv")) { 
			return 0; 
		} else if(type.equals("amr")){
			return 1;
		} else if(type.equals("caf")){
			return 1;
		}
		
		return -1; 
	} 
	
	private static boolean processFLV(String filepath) { 
		File f = new File(filepath);
		if (!f.exists()||f.isDirectory()) { 
			return false; 
		} 
		List<String> commend = new ArrayList<String>();
		commend.add(getCmd()); 
		commend.add("-i");
		commend.add(filepath); 
		commend.add("-ab"); 
		commend.add("64"); 
		commend.add("-ac"); 
		commend.add("2"); 
		commend.add("-ar"); 
		commend.add("22050"); 
		commend.add("-r"); 
		commend.add("15"); 
		commend.add("-y");
		commend.add("-ss");
		commend.add(startTime);
		commend.add("-t");
		commend.add(totalTime);
		commend.add(convertPath(f.getPath())); 
		Process p  = null;
		try { 
			ProcessBuilder builder = new ProcessBuilder(); 
			p = builder.command(commend).redirectErrorStream(true).start();
			InputStreamReader isr = new InputStreamReader(p.getInputStream(), "GBK"); 
			BufferedReader br = new BufferedReader(isr); 
			String line; 
			while ((line = br.readLine()) != null) { 
				//System.out.println(line); 
			} 
			p.waitFor();
			return true;
		} catch (Exception e) {
			EmpExecutionContext.error(e, "视频预览转换异常！");
			return false; 
		}finally{
			if(p != null){
				p.destroy();
			}
		} 
	} 
	
	private static boolean processMP3(String filepath) { 
		File f = new File(filepath);
		if (!f.exists()||f.isDirectory()) { 
			return false; 
		} 
		List<String> commend = new ArrayList<String>();
		commend.add(getCmd()); 
		commend.add("-i"); 
		commend.add(filepath);
		commend.add("-y");
		commend.add("-acodec"); 
		commend.add("libmp3lame"); 
		commend.add("-ac"); 
		commend.add("2");
		commend.add("-ab");
		commend.add("128");
		commend.add("-ar"); 
		commend.add("22050"); 
		commend.add(convertPath(f.getPath())); 
		Process p  = null;
		try { 
			ProcessBuilder builder = new ProcessBuilder(); 
			p = builder.command(commend).redirectErrorStream(true).start();
			InputStreamReader isr = new InputStreamReader(p.getInputStream(), "GBK"); 
			BufferedReader br = new BufferedReader(isr); 
			String line; 
			while ((line = br.readLine()) != null) { 
				//System.out.println(line); 
			} 
			p.waitFor();
			return true;
		} catch (Exception e) {
			EmpExecutionContext.error(e, "音频预览转换异常！");
			return false; 
		}finally{
			if(p != null){
				p.destroy();
			}
		} 
	} 
	
	
	public static boolean converVideo(String filePath){
		File file =  new File(filePath);
		if(!file.exists()||file.isDirectory()){
			return false;
		}
		 if(checkFileType(file.getName()) == 0){
			return processFLV(filePath);
		 }
		 return false;
	}
	
	public static boolean converAudio(String filePath){
		File file =  new File(filePath);
		if(!file.exists()||file.isDirectory()){
			return false;
		}
		 if(checkFileType(file.getName()) == 1){
			return processMP3(filePath);
		 }
		 return false;
	}
	
	private static String getRootClassPath() {
		if (rootClassPath == null) {
			try {
				//String path = FFmpegKit.class.getClassLoader().getResource("").toURI().getPath();
				//rootClassPath = new File(path).getAbsolutePath();
				rootClassPath = new TxtFileUtil().getWebRoot()+"WEB-INF/classes";
			}
			catch (Exception e) {
				String path = FFmpegKit.class.getClassLoader().getResource("").getPath();
				rootClassPath = new File(path).getAbsolutePath();
			}
		}
		return rootClassPath;
	}
	public static String convertPath(String path){
		Pattern pat = Pattern.compile("\\.(3gp|amr|caf)$",Pattern.CASE_INSENSITIVE);
		Matcher mat = pat.matcher(path);
		if(mat.find()){
			String extname = mat.group(1).toLowerCase();
			return path.replaceAll("(?i)"+extname+"$", relativeMap.get(extname));
		}
		return path;
	}
	
	public static void main(String[] args)
	{
//		converVideo("D:\\ffmpeg\\demo.3gp");
		converAudio("D:\\ffmpeg\\eacbc4eb12237abbf0261bb845290e52.caf");
	}
}
