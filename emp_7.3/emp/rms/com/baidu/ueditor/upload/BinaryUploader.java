package com.baidu.ueditor.upload;

import com.baidu.ueditor.PathFormat;
import com.baidu.ueditor.define.AppInfo;
import com.baidu.ueditor.define.BaseState;
import com.baidu.ueditor.define.FileType;
import com.baidu.ueditor.define.State;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.rms.meditor.tools.String2FileUtil;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BinaryUploader {

	public static final State save(HttpServletRequest request,
                                   Map<String, Object> conf) {
		FileItemStream fileStream = null;
		boolean isAjaxUpload = request.getHeader( "X_Requested_With" ) != null;

		if (!ServletFileUpload.isMultipartContent(request)) {
			return new BaseState(false, AppInfo.NOT_MULTIPART_CONTENT);
		}

		ServletFileUpload upload = new ServletFileUpload(
				new DiskFileItemFactory());

        if ( isAjaxUpload ) {
            upload.setHeaderEncoding( "UTF-8" );
        }

		try {
			FileItemIterator iterator = upload.getItemIterator(request);

			while (iterator.hasNext()) {
				fileStream = iterator.next();

				if (!fileStream.isFormField()) {
                    break;
                }
				fileStream = null;
			}

			if (fileStream == null) {
				return new BaseState(false, AppInfo.NOTFOUND_UPLOAD_DATA);
			}
			//从config.json中获取图片保存路径
			String savePath = (String) conf.get("savePath");
			//视频名称
			String originFileName = fileStream.getName();
			//文件后缀
			String suffix = FileType.getSuffixByFilename(originFileName);
			//文件名称
			originFileName = originFileName.substring(0,
					originFileName.length() - suffix.length());
			savePath = savePath + suffix;

			long maxSize = ((Long) conf.get("maxSize")).longValue();

			if (!validType(suffix, (String[]) conf.get("allowFiles"))) {
				return new BaseState(false, AppInfo.NOT_ALLOW_FILE_TYPE);
			}
			//保存路径
			savePath = PathFormat.parse(savePath, originFileName);
			//最终路径
			String physicalPath = (String) conf.get("rootPath") + savePath;

			InputStream is = fileStream.openStream();
			State storageState = StorageManager.saveFileByInputStream(is,
					physicalPath, maxSize);
			is.close();


			String h5url = null;
			//如果文件类型非图片需要+1
			String filePre = physicalPath.substring(0, physicalPath.lastIndexOf( "/" )+1);
			String fileName = physicalPath.substring(physicalPath.lastIndexOf( "/" )+1, physicalPath.lastIndexOf("."))+1;
			String fileSuffix = FileType.getSuffixByFilename(physicalPath);
			if(request.getParameter("type") != null) {
				try {
					File file = null;

					String type = null;
					if((".aac").equalsIgnoreCase(fileSuffix)||(".m4a").equalsIgnoreCase(fileSuffix)||(".wma").equalsIgnoreCase(fileSuffix)||(".mp3").equalsIgnoreCase(fileSuffix)){
						type = "4";//音频
						fileSuffix = ".MP3";
					}else if((".wmv").equalsIgnoreCase(fileSuffix)||(".3gp").equalsIgnoreCase(fileSuffix)||(".flv").equalsIgnoreCase(fileSuffix)||(".ogv").equalsIgnoreCase(fileSuffix)||(".swf").equalsIgnoreCase(fileSuffix)||(".avi").equalsIgnoreCase(fileSuffix)||(".mp4").equalsIgnoreCase(fileSuffix)
							||(".f4v").equalsIgnoreCase(fileSuffix)||(".m4v").equalsIgnoreCase(fileSuffix)||(".mkv").equalsIgnoreCase(fileSuffix)||(".mov").equalsIgnoreCase(fileSuffix)||(".mpg").equalsIgnoreCase(fileSuffix)||(".vob").equalsIgnoreCase(fileSuffix)){
						type = "3";//视频
						fileSuffix = ".MP4";
					}else if((".zip").equalsIgnoreCase(fileSuffix)||(".rar").equalsIgnoreCase(fileSuffix)||(".tar").equalsIgnoreCase(fileSuffix)||(".7z").equalsIgnoreCase(fileSuffix)||(".bz2").equalsIgnoreCase(fileSuffix)||(".cab").equalsIgnoreCase(fileSuffix)||(".iso").equalsIgnoreCase(fileSuffix)) {
						type = "5";//压缩
					}else if((".jpg").equalsIgnoreCase(fileSuffix)||((".jpeg").equalsIgnoreCase(fileSuffix))||(".png").equalsIgnoreCase(fileSuffix)||(".bmp").equalsIgnoreCase(fileSuffix)||(".gif").equalsIgnoreCase(fileSuffix)) {
						type = "2";//图片
					}
					//如果是图片不需要+1
					if(type.equals("2")) {
						file = new File(physicalPath);
					}else {
						file = new File(new StringBuffer().append(filePre).append(fileName).append(fileSuffix).toString());
					}

					h5url = String2FileUtil.uploadFile(file, savePath, "2", type);
				} catch (Exception e) {
                    EmpExecutionContext.error(e, "发现异常");
				}
			}

			if (storageState.isSuccess()) {
				storageState.putInfo("type", suffix);
				storageState.putInfo("original", originFileName + suffix);
				if((".aac").equalsIgnoreCase(suffix)||(".m4a").equalsIgnoreCase(suffix)||(".wma").equalsIgnoreCase(suffix)||(".mp3").equalsIgnoreCase(suffix)){
					suffix = "1.MP3";
				}else if((".wmv").equalsIgnoreCase(suffix)||(".3gp").equalsIgnoreCase(suffix)||(".flv").equalsIgnoreCase(suffix)||(".ogv").equalsIgnoreCase(suffix)||(".swf").equalsIgnoreCase(suffix)||(".avi").equalsIgnoreCase(suffix)||(".mp4").equalsIgnoreCase(suffix)
						||(".f4v").equalsIgnoreCase(suffix)||(".m4v").equalsIgnoreCase(suffix)||(".mkv").equalsIgnoreCase(suffix)||(".mov").equalsIgnoreCase(suffix)||(".mpg").equalsIgnoreCase(suffix)||(".vob").equalsIgnoreCase(suffix)){
					suffix = "1.MP4";
				}
				String suffixName = savePath.substring(savePath.length()-4,savePath.length());
				String pictureName = savePath.split(suffixName)[0] ;
				if(pictureName.endsWith(".")){
					pictureName = pictureName.substring(0,pictureName.length()-1);
				}

				savePath = pictureName + suffix;
				storageState.putInfo("url", PathFormat.format(savePath));


					File file = null;

					String type = null;
					if((".aac").equalsIgnoreCase(fileSuffix)||(".m4a").equalsIgnoreCase(fileSuffix)||(".wma").equalsIgnoreCase(fileSuffix)||(".mp3").equalsIgnoreCase(fileSuffix)){
						type = "4";//音频
						fileSuffix = ".MP3";
					}else if((".wmv").equalsIgnoreCase(fileSuffix)||(".3gp").equalsIgnoreCase(fileSuffix)||(".flv").equalsIgnoreCase(fileSuffix)||(".ogv").equalsIgnoreCase(fileSuffix)||(".swf").equalsIgnoreCase(fileSuffix)||(".avi").equalsIgnoreCase(fileSuffix)||(".mp4").equalsIgnoreCase(fileSuffix)
							||(".f4v").equalsIgnoreCase(fileSuffix)||(".m4v").equalsIgnoreCase(fileSuffix)||(".mkv").equalsIgnoreCase(fileSuffix)||(".mov").equalsIgnoreCase(fileSuffix)||(".mpg").equalsIgnoreCase(fileSuffix)||(".vob").equalsIgnoreCase(fileSuffix)){
						type = "3";//视频
						fileSuffix = ".MP4";
					}else if((".zip").equalsIgnoreCase(fileSuffix)||(".rar").equalsIgnoreCase(fileSuffix)||(".tar").equalsIgnoreCase(fileSuffix)||(".7z").equalsIgnoreCase(fileSuffix)||(".bz2").equalsIgnoreCase(fileSuffix)||(".cab").equalsIgnoreCase(fileSuffix)||(".iso").equalsIgnoreCase(fileSuffix)) {
						type = "5";//压缩
					}else if((".jpg").equalsIgnoreCase(fileSuffix)||((".jpeg").equalsIgnoreCase(fileSuffix))||(".png").equalsIgnoreCase(fileSuffix)||(".bmp").equalsIgnoreCase(fileSuffix)||(".gif").equalsIgnoreCase(fileSuffix)) {
						type = "2";//图片
					}
					//如果是图片不需要+1
					if("2".equals(type)) {
						file = new File(physicalPath);
					}else {
						file = new File(new StringBuffer().append(filePre).append(fileName).append(fileSuffix).toString());
					}
				    StorageManager.getFileAttr(file,storageState,physicalPath);
			}

			if(request.getParameter("type") != null) {
				storageState.putInfo("url", h5url);
			}
			return storageState;
		} catch (FileUploadException e) {
			return new BaseState(false, AppInfo.PARSE_REQUEST_ERROR);
		} catch (IOException e) {
		}catch (Exception e){
            return new BaseState(false, AppInfo.PARSE_REQUEST_ERROR);
        }
		return new BaseState(false, AppInfo.IO_ERROR);
	}

	private static boolean validType(String type, String[] allowTypes) {
		List<String> list = Arrays.asList(allowTypes);

		return list.contains(type);
	}
}
