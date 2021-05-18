package com.baidu.ueditor.upload;

import com.baidu.ueditor.define.AppInfo;
import com.baidu.ueditor.define.BaseState;
import com.baidu.ueditor.define.FileType;
import com.baidu.ueditor.define.State;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.rms.tools.MediaTool;
import com.montnets.emp.util.IOUtils;

import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.MultimediaInfo;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.ResourceBundle;

public class StorageManager {
	public static final int BUFFER_SIZE = 8192;
	private static String toolPathType;
	private static String videoPath;
	static{
		ResourceBundle bundle = ResourceBundle.getBundle("SystemGlobals");
		toolPathType = bundle.getString("montnets.rms.videoEncoder.system");
		videoPath = bundle.getString("montnets.rms.cropper.videoToolPath");
	}

	public StorageManager() {
	}

	public static State saveBinaryFile(byte[] data, String path) {
		File file = new File(path);

		State state = valid(file);

		if (!state.isSuccess()) {
			return state;
		}
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(data);
		} catch (IOException ioe) {
			return new BaseState(false, AppInfo.IO_ERROR);
		}finally{
			if(null != fos){
				try {
					fos.close();
				} catch (IOException e) {
					EmpExecutionContext.error(e,"资源关闭异常");
				}
			}
			if(bos != null){
				try {
					bos.flush();
					bos.close();
				} catch (IOException e) {
					EmpExecutionContext.error(e,"关闭异常");
				}
			}
		}

		state = new BaseState(true, file.getAbsolutePath());
		state.putInfo( "size", data.length );
		state.putInfo( "title", file.getName() );
		return state;
	}

	public static State saveFileByInputStream(InputStream is, String path,
                                              long maxSize) {
		State state = null;

		File tmpFile = getTmpFile();

		byte[] dataBuf = new byte[ 2048 ];
		BufferedInputStream bis = new BufferedInputStream(is, StorageManager.BUFFER_SIZE);
		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(
					new FileOutputStream(tmpFile), StorageManager.BUFFER_SIZE);

			try {
				int count = 0;
				while ((count = bis.read(dataBuf)) != -1) {
					bos.write(dataBuf, 0, count);
				}
			}finally {
				try{
					IOUtils.closeIOs(bis, bos, null, null, StorageManager.class);
				}catch(IOException e){
					EmpExecutionContext.error(e,"IOError");
				}
			}

			if (tmpFile.length() > maxSize) {
                boolean flag = tmpFile.delete();
                if (!flag) {
                    EmpExecutionContext.error("删除文件失败！");
                }
                return new BaseState(false, AppInfo.MAX_SIZE);
			}

			state = saveTmpFile(tmpFile, path);

			if (!state.isSuccess()) {
                boolean flag = tmpFile.delete();
                if (!flag) {
                    EmpExecutionContext.error("删除文件失败！");
                }
			}else{
				int tranResult = 1;
				String type = "music";
				String tarPath ="";
				String suffixName = path.substring(path.length()-4,path.length());
				String pictureName = path.split(suffixName)[0] ;
				if((".aac").equalsIgnoreCase(suffixName)||(".m4a").equalsIgnoreCase(suffixName)||(".wma").equalsIgnoreCase(suffixName)||(".mp3").equalsIgnoreCase(suffixName)){
					tranResult =  picuterToMP4(path,type,"1");
				}else if((".mpg").equalsIgnoreCase(suffixName)||(".avi").equalsIgnoreCase(suffixName)||(".vob").equalsIgnoreCase(suffixName)||(".wmv").equalsIgnoreCase(suffixName)
						||(".3gp").equalsIgnoreCase(suffixName)||(".flv").equalsIgnoreCase(suffixName)||(".ogv").equalsIgnoreCase(suffixName)||(".swf").equalsIgnoreCase(suffixName)){
					type = "media";
					tranResult =  picuterToMP4(path,type,"1");
				}else if((".f4v").equalsIgnoreCase(suffixName)||(".m4v").equalsIgnoreCase(suffixName)||(".mkv").equalsIgnoreCase(suffixName)||(".mov").equalsIgnoreCase(suffixName)||(".mp4").equalsIgnoreCase(suffixName)){
					type = "media";
					tranResult =  picuterToMP4(path,type,"0");
				}
				if(tranResult==0){
					deleteMedia(path);
					if(("music").equalsIgnoreCase(type)){
						tarPath = pictureName+"1.MP3";
					}else{
						tarPath = pictureName+"1.MP4";
					}
					File targetFile = new File(tarPath);
					state = new BaseState(true);
					state.putInfo( "size", targetFile.length() );
					state.putInfo( "title", targetFile.getName());
				}
			}

			return state;
			
		} catch (Exception e) {
			EmpExecutionContext.error(e,"saveFileByInputStream");
		}finally{
			try {
				if(null != bos) {
                    bos.flush();
                }
			} catch (IOException e1) {
				EmpExecutionContext.error(e1,"IOError");
			}

		}
		return new BaseState(false, AppInfo.IO_ERROR);
	}

	public static State saveFileByInputStream(InputStream is, String path) {
		State state = null;

		File tmpFile = getTmpFile();

		byte[] dataBuf = new byte[ 2048 ];
		BufferedInputStream bis = new BufferedInputStream(is, StorageManager.BUFFER_SIZE);
		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(
					new FileOutputStream(tmpFile), StorageManager.BUFFER_SIZE);

			int count = 0;
			while ((count = bis.read(dataBuf)) != -1) {
				bos.write(dataBuf, 0, count);
			}
//			bos.flush();
//			bos.close();

			state = saveTmpFile(tmpFile, path);

			if (!state.isSuccess()) {
                boolean flag = tmpFile.delete();
                if (!flag) {
                    EmpExecutionContext.error("删除文件失败！");
                }
			}

			return state;
		} catch (Exception e) {
			EmpExecutionContext.error(e,"saveFileByInputStream");
		}finally{
			try {
				if(null != bos) {
                    bos.flush();
                }
			} catch (IOException e1) {
				EmpExecutionContext.error(e1,"IOError");
			}
			try{
				IOUtils.closeIOs(bis, bos, null, null, StorageManager.class);
			}catch(IOException e){
				EmpExecutionContext.error(e,"IOError");
			}
		}
		return new BaseState(false, AppInfo.IO_ERROR);
	}

	private static File getTmpFile() {
		File tmpDir = FileUtils.getTempDirectory();
		String tmpFileName = (Math.random() * 10000 + "").replace(".", "");
		return new File(tmpDir, tmpFileName);
	}

	private static State saveTmpFile(File tmpFile, String path) {
		State state = null;
		File targetFile = new File(path);

		if (targetFile.canWrite()) {
			return new BaseState(false, AppInfo.PERMISSION_DENIED);
		}
		try {
			FileUtils.moveFile(tmpFile, targetFile);
		} catch (IOException e) {
			return new BaseState(false, AppInfo.IO_ERROR);
		}

		state = new BaseState(true);
		state.putInfo( "size", targetFile.length() );
		state.putInfo( "title", targetFile.getName() );
		
		return state;
	}

	private static State valid(File file) {
		File parentPath = file.getParentFile();

		if ((!parentPath.exists()) && (!parentPath.mkdirs())) {
			return new BaseState(false, AppInfo.FAILED_CREATE_FILE);
		}

		if (!parentPath.canWrite()) {
			return new BaseState(false, AppInfo.PERMISSION_DENIED);
		}

		return new BaseState(true);
	}
	/**
	 * 新增上传的其他格式转换为MP3和MP4
	 * @param path：上传的视频或音频所在的绝对路径
	 * @param type：判断上传的文件是音频还是视频，music表示音频，media表示视频
	 * @param types：判断需要调用那种解码器方法。1表示调用 -o转码方法，0表示调用 -c -o的转码方法
	 * @return
	 */
	private static int picuterToMP4(String path,String type, String types){
		String newSuffixName =null;
		String toolPath;
		int result=1;
		if(("music").equalsIgnoreCase(type)){
			newSuffixName = "1.MP3";
		}else{
			newSuffixName = "1.MP4";
		}
		MediaTool mediaTool = new MediaTool();
		if(StringUtils.isEmpty(toolPathType)){
			toolPathType = "Linux";
		}
		toolPath = videoPath;
		String suffixName = path.substring(path.length()-4,path.length());
		String pictureName = path.split(suffixName)[0] ;
		String tarPath = pictureName+newSuffixName;
		if(("1").equals(types)){
			result=mediaTool.conversionVideoFormatDeleteC(toolPath, path, tarPath);
		}else{
			result=mediaTool.conversionVideoFormat(toolPath, path, tarPath);
		}
		return result;
	}
	/**
	 * 转码成功后删除转码前的视频或音频
	 * @param path 转码前的视频或音频的绝对路径
	 * @return
	 */
	private static boolean deleteMedia(String path){
		File file = new File(path);
        if(file.isFile() && file.exists()){
            Boolean succeedDelete = file.delete();
            if(succeedDelete){
            	EmpExecutionContext.info("删除单个文件"+path+"成功！");
                return true;
            }
            else{
            	EmpExecutionContext.error("删除单个文件"+path+"失败！");
                return true;
            }
        }else{
        	EmpExecutionContext.error("删除单个文件"+path+"失败！");
            return false;
        }
	}
	//获取上传文件的长和高
	public static void getFileAttr(File file, State state, String physicalPath) throws  Exception{
		try {
			//如果文件类型非图片需要+1
			String fileSuffix = FileType.getSuffixByFilename(physicalPath);
			String type = null;
			if((".aac").equalsIgnoreCase(fileSuffix)||(".m4a").equalsIgnoreCase(fileSuffix)||(".wma").equalsIgnoreCase(fileSuffix)||(".mp3").equalsIgnoreCase(fileSuffix)){
				type = "4";//音频
			}else if((".wmv").equalsIgnoreCase(fileSuffix)||(".3gp").equalsIgnoreCase(fileSuffix)||(".flv").equalsIgnoreCase(fileSuffix)||(".ogv").equalsIgnoreCase(fileSuffix)||(".swf").equalsIgnoreCase(fileSuffix)||(".avi").equalsIgnoreCase(fileSuffix)||(".mp4").equalsIgnoreCase(fileSuffix)
					||(".f4v").equalsIgnoreCase(fileSuffix)||(".m4v").equalsIgnoreCase(fileSuffix)||(".mkv").equalsIgnoreCase(fileSuffix)||(".mov").equalsIgnoreCase(fileSuffix)||(".mpg").equalsIgnoreCase(fileSuffix)||(".vob").equalsIgnoreCase(fileSuffix)){
				type = "3";//视频
			}else if((".jpg").equalsIgnoreCase(fileSuffix)||((".jpeg").equalsIgnoreCase(fileSuffix))||(".png").equalsIgnoreCase(fileSuffix)||(".bmp").equalsIgnoreCase(fileSuffix)||(".gif").equalsIgnoreCase(fileSuffix)) {
				type = "2";//图片
			}
			if ("4".equals(type)){
				Encoder encoder = new Encoder();
				MultimediaInfo m = encoder.getInfo(file);
				Long duration = m.getDuration();
				state.putInfo("duration", duration);
			}
			if ("3".equals(type)) {
				Encoder encoder = new Encoder();
				MultimediaInfo m = encoder.getInfo(file);
				Long duration = m.getDuration();
				state.putInfo("duration", duration);
				String ratio = String.valueOf(new BigDecimal(m.getVideo().getSize().getWidth()).divide(new BigDecimal(m.getVideo().getSize().getHeight()),2,RoundingMode.HALF_UP));
				state.putInfo("width", m.getVideo().getSize().getWidth());
				state.putInfo("height", m.getVideo().getSize().getHeight());
				state.putInfo("ratio",ratio);
			} else if ("2".equals(type)) {
				int width = 0;
				int height = 0;
				//兼容ImageIO.read读取gif图片无法解析的问题
				if(".gif".equalsIgnoreCase(fileSuffix)){
					Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName("gif");
					ImageReader reader =  readers.next();
					ImageInputStream iis = ImageIO.createImageInputStream(file);
					reader.setInput(iis, true);
					//获得图片的宽
					width = reader.getWidth(0);
					//获得图片的高
					height = reader.getHeight(0);
				}else{
					//读取图片对象
					BufferedImage img = ImageIO.read(file);
					//获得图片的宽
					width = img.getWidth();
					//获得图片的高
					height = img.getHeight();
				}
				String ratio = String.valueOf(new BigDecimal(width).divide(new BigDecimal(height),2,RoundingMode.HALF_UP));
				state.putInfo("width", width);
				state.putInfo("height", height);
				state.putInfo("ratio", ratio);
			}

		} catch (Exception e) {
			throw  e;
		}
	}
}
