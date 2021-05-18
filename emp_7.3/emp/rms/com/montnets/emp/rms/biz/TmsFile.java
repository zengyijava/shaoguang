/**
 * 
 */
package com.montnets.emp.rms.biz;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.util.StringUtils;
import com.montnets.emp.util.TxtFileUtil;

/**
 * @author zym
 * 
 */
public class TmsFile {

	private List<FrameItem> fileList = new ArrayList<FrameItem>();

	public void addFrame(FrameItem item) throws Exception {
		if (item == null) {
			throw new NullPointerException("加入的帧不能为null");
		}

		if (StringUtils.IsNullOrEmpty(item.getAudioSrc())
				&& StringUtils.IsNullOrEmpty(item.getImageSrc())
				&& StringUtils.IsNullOrEmpty(item.getTextSrc())) {
			throw new Exception("每一帧至少应该包含图片，文本或声音文件中的一个！");
		}

		if (item.getDelayTime() <= 0) {
			throw new Exception("帧的延时时间不能小于0！");
		}

		if (fileList.size() >= 15) {
			throw new Exception("帧的总数不能超过15！");
		}

		fileList.add(item);

	}

	public byte[] getTmsFileBytes() throws Exception {
		int index = 0;
		byte[] bytes = new byte[1024 * 1024];
		bytes[index] = (byte) 161;
		index++;

		bytes[index] = (byte) 100;
		index++;

		index += 4;

		bytes[index] = (byte) getFileCount();
		index++;

		System.arraycopy(int2bytes(1), 2, bytes, index, 2);
		index += 2;

		bytes[index] = "a".getBytes()[0];
		index++;

		byte[] smilBytes = getSmilFileBytes();
		System.arraycopy(smilBytes, 0, bytes, index, smilBytes.length);
		index += smilBytes.length;
		byte[] fileBytes;
		for (int i = 0; i < fileList.size(); i++) {
			FrameItem item = fileList.get(i);

			String imageSrc = item.getImageSrc();
			if (!StringUtils.IsNullOrEmpty(imageSrc)) {
				String fileName = String.format("%02d.%s", i,
						getFileExtends(imageSrc));
				fileBytes = getFileBytes(readFileContents(imageSrc), fileName);

				System.arraycopy(fileBytes, 0, bytes, index, fileBytes.length);
				index += fileBytes.length;
			}

			String audioSrc = item.getAudioSrc();
			if (!StringUtils.IsNullOrEmpty(audioSrc)) {
				String fileName = String.format("%02d.%s", i,
						getFileExtends(audioSrc));
				fileBytes = getFileBytes(readFileContents(audioSrc), fileName);

				System.arraycopy(fileBytes, 0, bytes, index, fileBytes.length);
				index += fileBytes.length;
			}

			String textSrc = item.getTextSrc();
			if (!StringUtils.IsNullOrEmpty(textSrc)) {
				String fileName = String.format("%02d.%s", i,
						"txt");
				byte[] textBytes=textSrc.getBytes("utf-8");
				fileBytes = getFileBytes(textBytes, fileName);

				System.arraycopy(fileBytes, 0, bytes, index, fileBytes.length);
				index += fileBytes.length;
			}
		}

		System.arraycopy(int2bytes(index), 0, bytes, 2, 4);

		byte[] results = new byte[index];
		System.arraycopy(bytes, 0, results, 0, index);

		return results;
	}

	private byte[] readFileContents(String fileName) {
		File file = new File(fileName);
		InputStream in = null;
		byte[] contentBytes = null;
		try {
			in = new FileInputStream(file);
			int length = in.available();
			contentBytes = new byte[length];
			int c=in.read(contentBytes);
		} catch (IOException e) {
			EmpExecutionContext.error(e,"获取文件内容失败！");
		} finally{
			if(in != null){
				try{
					in.close();
				}catch(Exception e){
					EmpExecutionContext.error(e, "关闭异常");
				}
			}
		}
		return contentBytes;
	}

	private byte[] getSmilFileBytes() throws UnsupportedEncodingException {

		StringBuilder sb = new StringBuilder();
		sb.append("<smil><head><layout><root-layout width=\"208\" height=\"176\" />");
		sb
				.append("<region id=\"image\" left=\"20\" top=\"20\" width=\"128\" height=\"128\"/>");
		sb
				.append("<region id=\"text\" left=\"0\" top=\"50\" width=\"128\" height=\"128\"/>");
		sb.append("</layout></head>");
		sb.append("<body>");

		for (int i = 0; i < fileList.size(); i++) {
			FrameItem item = fileList.get(i);
			sb.append(String.format("<par dur=\"%d000ms\">", item
					.getDelayTime()));
			String imageSrc = item.getImageSrc();
			if (!StringUtils.IsNullOrEmpty(imageSrc)) {
				sb.append(String.format(
						"<img src=\"cid:%02d.%s\" region=\"image\" />", i,
						getFileExtends(imageSrc)));
			}

			String audioSrc = item.getAudioSrc();
			if (!StringUtils.IsNullOrEmpty(audioSrc)) {
				sb.append(String.format("<audio src=\"cid:%02d.%s\" region=\"sound\"/>", i,
						getFileExtends(audioSrc)));
			}
			String textSrc = item.getTextSrc();
			if (!StringUtils.IsNullOrEmpty(textSrc)) {
				sb.append(String.format(
						"<text src=\"cid:%02d.%s\" region=\"text\" />", i,
						"txt"));
			}

			sb.append("</par>");
		}

		sb.append("</body></smil>");

		String smilFile = sb.toString();
		byte[] contentBytes = smilFile.getBytes("utf-8");
		String name = "tms.smil";
		byte[] results = getFileBytes(contentBytes, name);

		return results;
	}

	private byte[] getFileBytes(byte[] contentBytes, String name)
			throws UnsupportedEncodingException {
		byte[] nameBytes = name.getBytes("utf-8");

		byte[] results = new byte[1 + nameBytes.length + 3
				+ contentBytes.length];

		int index = 0;
		results[index] = (byte) nameBytes.length;
		index++;

		System.arraycopy(nameBytes, 0, results, index, nameBytes.length);
		index += nameBytes.length;

		System.arraycopy(int2bytes(contentBytes.length), 1, results, index, 3);
		index += 3;

		System.arraycopy(contentBytes, 0, results, index, contentBytes.length);
		return results;
	}

	private byte[] int2bytes(int num) {
		byte[] b = new byte[4];
		for (int i = 0; i < 4; i++) {
			b[i] = (byte) (num >>> (24 - i * 8));
		}
		return b;
	}

	private String getFileExtends(String src) {
		return src.substring(src.lastIndexOf(".") + 1);
	}

	private int getFileCount() {
		int count = 0;
		for (int i = 0; i < fileList.size(); i++) {
			FrameItem item = fileList.get(i);
			if (!StringUtils.IsNullOrEmpty(item.getAudioSrc())) {
				count++;
			}

			if (!StringUtils.IsNullOrEmpty(item.getImageSrc())) {
				count++;
			}

			if (!StringUtils.IsNullOrEmpty(item.getTextSrc())) {
				count++;
			}
		}

		count++;
		return count;
	}
	
	public String tmsToBase64(String tmsPath)
	{
		TxtFileUtil txtfileutil = new TxtFileUtil();
		String base64 = null;
		String path = txtfileutil.getWebRoot() + tmsPath;
		File file = new File(path);
		InputStream in = null;
		byte[] contentBytes = null;
		try
		{
			in = new FileInputStream(file);
			int length = in.available();
			contentBytes = new byte[length];
			int c=in.read(contentBytes);
			base64 = (new sun.misc.BASE64Encoder()).encode(contentBytes);
		} catch (IOException e)
		{
			EmpExecutionContext.error(e,"模板转成Base64失败！");
		}finally{
			if(in != null){
				try {
					in.close();
				} catch (IOException e) {
					EmpExecutionContext.error(e,"IO异常");
				}
			}
		}
		return base64;
	}
}
