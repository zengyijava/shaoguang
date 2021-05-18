package com.montnets.emp.rms.tools;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.montnets.emp.common.context.EmpExecutionContext;
import org.apache.commons.codec.binary.Base64;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * RMS文件解析类
 * @author shouc
 *
 */
public class AnalysisRMS {
	private static final ResourceBundle bundle = ResourceBundle.getBundle("SystemGlobals");
	private static final String operatingSystem = bundle.getString("montnets.rms.videoEncoder.system");
	/**
	 * 读取RMS文件将其解析成对应的smil以及文件
	 * @param fileInUrl
	 * @param fileOutDir
	 * @return
	 */
	public RmsEntity analysisRMS(String fileInUrl, String fileOutDir) {
		File fileIn = null;
		FileInputStream fileInputStream =null;
		RmsEntity rmsEntity = new RmsEntity();
		try {
			fileIn = new File(fileInUrl);
			fileInputStream =new FileInputStream(fileIn);
			
			byte[] b = getByte(1, fileInputStream);
			int identification = Integer.valueOf(binary(b, 10));
			//EmpExecutionContext.info("RMS文件标识"+identification);
//			if(identification!=162){
//				return null;
//			}
			rmsEntity.setIdentification(identification);
			
			b = getByte(1, fileInputStream);
			int version = Integer.valueOf(binary(b, 10));
			//EmpExecutionContext.info("RMS文件版本："+version);
//			System.out.println("RMS文件版本："+version);
			rmsEntity.setVersion(version);
			
			b = getByte(1, fileInputStream);
			int coding = Integer.valueOf(binary(b, 10));
//			EmpExecutionContext.info("RMS文件编码格式："+coding);
//			System.out.println("RMS文件编码格式："+coding);
			rmsEntity.setCoding(coding);
			
			b = getByte(4, fileInputStream);
			Integer fileSize = Integer.valueOf(binary(b, 10));
			//EmpExecutionContext.info("RMS数据文件总长度：" + fileSize);
//			System.out.println("RMS数据文件总长度：" + fileSize);
			rmsEntity.setFileSize(fileSize);

			b = getByte(2, fileInputStream);
			int fileCount = Integer.valueOf(binary(b, 10));
//			EmpExecutionContext.info("RMS包含的文件数目：" + fileCount);
			rmsEntity.setFileCount(fileCount);
			
			b = getByte(1, fileInputStream);
			int titleSize = Integer.valueOf(binary(b, 10));
//			EmpExecutionContext.info("RMS文件标题的字节长度：" + titleSize);
			rmsEntity.setTitleSize(titleSize);

			b = getByte(titleSize, fileInputStream);
			String title = new String(b);
//			EmpExecutionContext.info("RMS文件标题：" +title);
			rmsEntity.setTitle(title);
			List<String> listUrl = new ArrayList<String>();
			for (int i = 0; i < fileCount; i++) {
				b = getByte(1, fileInputStream);
				String fileType = new String(b);
//				EmpExecutionContext.info("RMS第" + i + "个文件类型：" + fileType);
				
				b = getByte(1, fileInputStream);
				int fileNameSize = Integer.valueOf(binary(b, 10));
//				EmpExecutionContext.info("RMS第" + i + "个文件名的字节长度：" + fileNameSize);

				b = getByte(fileNameSize, fileInputStream);
				String fileName = new String(b);
//				EmpExecutionContext.info("RMS第" + i + "个文件的文件名：" + fileName);

				b = getByte(4, fileInputStream);
				int fileContentsize = Integer.valueOf(binary(b, 10));
//				EmpExecutionContext.info("RMS第" + i + "个文件的内容长度：" + fileContentsize);
				
				b = getByte(fileContentsize, fileInputStream);
				File dir = new File(fileOutDir);
				if (!dir.exists()) {
					dir.mkdirs();
		        } 
				
				File fileout = new File(fileOutDir + fileName);
				listUrl.add(fileout.getPath());
				if (!fileout.exists()) {
                    boolean flag = fileout.createNewFile();
                    if (!flag) {
                        EmpExecutionContext.error("创建文件失败！");
                    }
				}
				writeFileContent(fileout, b);
			}
			rmsEntity.setFileUrl(listUrl);
		} catch (Exception e) {
			rmsEntity = null;
			EmpExecutionContext.error(e,"解析RMS文件出错"+ e.getMessage());
		}finally{
			try {
				fileInputStream.close();
			} catch (IOException e) {
 				EmpExecutionContext.error(e,e.getMessage());
			}
		}
		return rmsEntity;
	}
	
	/**
	 * 将字节流转换成对应的进制
	 * @param bytes
	 * @param radix
	 * @return
	 */
	public String binary(byte[] bytes, int radix) {
		return new BigInteger(1, bytes).toString(radix);// 这里的1代表正数
	}
	
	/**
	 * 读取指定的字节流长度
	 * @param size
	 * @param fileInputStream
	 * @return
	 */
	public byte[] getByte(int size, FileInputStream fileInputStream) {
		byte[] b = new byte[size];
		try {
			int c=fileInputStream.read(b);
		} catch (IOException e) {
			EmpExecutionContext.error(e,"字节流读取出错"+ e.getMessage());
		}
		return b;
	}

	/**
	 * byte[]写入文件方法
	 * @param fileout
	 * @param content
	 * @throws IOException
	 */
	public void writeFileContent(File fileout, byte[] content)throws IOException {
		OutputStream out = null;
		try{
			out = new FileOutputStream(fileout);
			out.write(content, 0, content.length);
		}finally{
			if(out != null){
				out.close();
			}
		}
	}
	
	/**
	 * 将smil文件解析成HTML文件字符串
	 * @param smilDir
	 * @param pathSvt
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public StringBuffer smilToHtml(String smilDir, String pathSvt, RmsEntity rmsEntity){
		StringBuffer sb = new StringBuffer();
		SAXBuilder saxBuilder = new SAXBuilder();
		try {
//			sb.append("<p style=\"width: 224;text-align: center;\">");
//			sb.append(rmsEntity.getTitle());
//			sb.append("</p>");
			Document document = saxBuilder.build(new InputStreamReader(new FileInputStream(rmsEntity.getFileUrl().get(0)),"utf-8"));
			Element element = document.getRootElement();
			List<Element> bodyList = element.getChildren("body");
			for (Element body : bodyList) {
				List<Element> parList = body.getChildren("par");
				int i = 1;
				for (Element par : parList) {
					List<Element> htmlEments = par.getChildren();
					//EmpExecutionContext.info(par.getContent());
					//判断当前<par> 下含有
					String tagName = "";
					sb.append("<div class=\"editor-keyframe J-keyframe\" data-type=\"#tagName#\">");
					sb.append("<div class=\"keyframe-content J-keyframe-content\">");
					
					for (Element htmlEment : htmlEments) {
						if(tagName.equals("")){
							if(htmlEment.getName().equals("text")){
								tagName = "text";
							}else if(htmlEment.getName().equals("img")){
								tagName = "image";
							}else if(htmlEment.getName().equals("audio")){
								tagName = "audio";
							}else if(htmlEment.getName().equals("video")){
								tagName = "video";
							}
						}
						
						if(tagName.equals("text")){
							if(htmlEment.getName().equals("text")){
								sb.append("<div class=\"editor-text J-edit-text\" contenteditable=\"true\">");
								BufferedReader br = null;
								InputStreamReader isr = null;
								try {
									String line = "";
//									 br= new BufferedReader(new FileReader
//									(new File(smilDir+"/"+htmlEment.getAttributeValue("src"))));
//									while ((line = br.readLine())!=null) {
//										sb.append("<p>");
//										sb.append(line);
//										sb.append("</p>");
//									}
									  isr = new InputStreamReader(new FileInputStream(smilDir+"/"+htmlEment.getAttributeValue("src")), "UTF-8");
								      br = new BufferedReader(isr);
								      while ((line = br.readLine()) != null) {
								    	    sb.append("<p>");
											sb.append(line);
											sb.append("</p>");
								      }
									
								}finally{
									if(br!=null){
										br.close();
									}
									if(isr != null){
										isr.close();
									}
								}
								sb.append("</div>");
							}else if(htmlEment.getName().equals("img")){
								sb.append("<div class=\"editor-img J-editor-img\" data-size=\""+rmsEntity.getDataSize().get(i)+"\">");
								sb.append("<img src=\"");
								sb.append(pathSvt+htmlEment.getAttributeValue("src"));
								sb.append("\" />");
								sb.append("</div>");
							}
							
						}else if(tagName.equals("image")){
							if(htmlEment.getName().equals("img")){
								sb.append("<div class=\"editor-img J-editor-img\" data-size=\""+rmsEntity.getDataSize().get(i)+"\">");
								sb.append("<img src=\"");
								sb.append(pathSvt+htmlEment.getAttributeValue("src"));
								sb.append("\" />");
								sb.append("</div>");
							}else if(htmlEment.getName().equals("text")){
								sb.append("<div class=\"editor-text J-edit-text\" contenteditable=\"true\">");
								BufferedReader br = null;
								InputStreamReader isr = null;
								try {
								     String line = "";
//									 br= new BufferedReader(new FileReader
//									(new File(smilDir+"/"+htmlEment.getAttributeValue("src"))));
//									while ((line = br.readLine())!=null) {
//										sb.append("<p>");
//										sb.append(line);
//										sb.append("</p>");
//									}
//									
									  isr = new InputStreamReader(new FileInputStream(smilDir+"/"+htmlEment.getAttributeValue("src")), "UTF-8");
								      br = new BufferedReader(isr);
								      while ((line = br.readLine()) != null) {
								    	    sb.append("<p>");
											sb.append(line);
											sb.append("</p>");
								      }
								}finally{
									if(br!=null){
										br.close();
									}
									if(isr != null){
										isr.close();
									}
								}
								sb.append("</div>");
							}
						}else if(tagName.equals("audio")){
							sb.append("<audio class=\"J-audio\"  src=\"");
							//sb.append(htmlEment.getName());
							sb.append(pathSvt+htmlEment.getAttributeValue("src") +"\" controls=\"controls\" data-size=\""+rmsEntity.getDataSize().get(i)+"\">您的浏览器不支持视频播放，请升级到ie9以上或使用其他最新版浏览器</audio>");
						}else if(tagName.equals("video")){
							sb.append("<video class=\"J-video\"  src=\"");
							//sb.append(htmlEment.getName());
							sb.append(pathSvt+htmlEment.getAttributeValue("src") +"\" controls=\"controls\" data-size=\""+rmsEntity.getDataSize().get(i)+"\">您的浏览器不支持视频播放，请升级到ie9以上或使用其他最新版浏览器</video>");
						}
						
						i++;
					}
					
					sb.replace(sb.indexOf("#tagName#"),sb.indexOf("#tagName#")+9, tagName);	
					sb.append("</div>");
					sb.append("</div>");
				}
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"生成html文件出现异常");
		}
		return sb;
	}
	
	/**
	 * 在指定目录创建HTML文件
	 * @param htmlDir
	 * @param pathSvt
	 */
	public void createHtml(String htmlDir, String pathSvt, RmsEntity rmsEntity){
		
		File dir = new File(htmlDir);
		if(!dir.exists()){
			dir.mkdirs();
		}
		BufferedWriter bw = null;
		FileOutputStream fos = null;
		FileOutputStream fos2 = null;
		try {
			File file = new File(dir.getParent()+"/fuxin.html");
			File file2 = new File(dir.getParent()+"/firstframe.jsp");
			AnalysisRMS analysisRMS = new AnalysisRMS();
			StringBuffer sb = analysisRMS.smilToHtml(htmlDir,pathSvt,rmsEntity);
			if(!file.exists()){
                boolean flag = file.createNewFile();
                if (!flag) {
                    EmpExecutionContext.error("创建文件失败！");
                }
			}
			if(!file2.exists()){
                boolean flag = file2.createNewFile();
                if (!flag) {
                    EmpExecutionContext.error("创建文件失败！");
                }
			}
//			bw= new BufferedWriter(new FileWriter(file));
//			bw.write(sb.toString());
//			bw.flush();
			fos = new FileOutputStream(file.getAbsoluteFile());
			fos2 = new FileOutputStream(file2.getAbsoluteFile());
			fos.write(sb.toString().getBytes("UTF-8"));
			//<%@ page language=\"java\" pageEncoding=\"UTF-8\"%>\r\n
			String firstFrameStr = sb.insert(0, "<%@ page language=\"java\" pageEncoding=\"UTF-8\"%>\r\n").toString();
			fos2.write(firstFrameStr.getBytes("UTF-8"));
			
		} catch (Exception e) {
			EmpExecutionContext.error(e,"写入fuxin.html文件出现异常");
		}finally{
			try {
				if(fos!=null){
					fos.close();
				}
				if(fos2!=null){
					fos2.close();
				}
			} catch (IOException e) {
				EmpExecutionContext.error(e,"关闭IO流出现异常");
			}
		}
	}

	/**
	 * 生成预览的html
	 * @param htmlDir
	 * @param pathSvt
	 * @param title
	 * @param smilFileUrl
	 * @param isDiffSend
	 */
	public void createPreviewHtml(String htmlDir, String pathSvt, String title, String smilFileUrl, Boolean isDiffSend){
		//当前系统分隔符
		String sepa = java.io.File.separator;
		File file;
		File dir = new File(htmlDir);
		if(!dir.exists()){
			dir.mkdirs();
		}
		BufferedWriter bw = null;
		FileOutputStream fos =null;
		try {
			if(isDiffSend){
				file = new File(dir.getParent().replace("src" + sepa + "diffSend","")+"diffSendPreview.html");
			}else {
				file = new File(dir.getParent()+ sepa + "fuxinPreview.html");
			}
			AnalysisRMS analysisRMS = new AnalysisRMS();
			StringBuffer sb = analysisRMS.smilToHtmlForPreview(htmlDir,pathSvt, title,  smilFileUrl);
			if(!file.exists()){
                boolean flag = file.createNewFile();
                if (!flag) {
                    EmpExecutionContext.error("创建文件失败！");
                }
			}
			
			fos = new FileOutputStream(file.getAbsoluteFile());
			fos.write(sb.toString().getBytes("UTF-8"));
			
//			bw= new BufferedWriter(new FileWriter(file));
//			bw.write(sb.toString());
//			bw.flush();
		} catch (Exception e) {
			EmpExecutionContext.error(e,"写入fuxin.html文件出现异常");
		}finally{
			try {
				//bw.close();
				if(fos!=null){
					fos.close();	
				}
			} catch (IOException e) {
				EmpExecutionContext.error(e,"关闭IO流出现异常");
			}
		}
	}
	
	/**
	 * @param filePath
	 * @return
	 */
	public RmsEntity Parse(HttpServletRequest request, String filePath, String fileOutDir, Integer fileByteSize, List<Excel2JsonDto> dtoList)
    {
		fileOutDir = new File(fileOutDir).getPath()+"/";
//EmpExecutionContext.info("fileOutDir:"+fileOutDir);		
		RmsEntity rmsEntity = new RmsEntity();
		InputStream in = null;
        try
        {
        	IMGTool imgTool = new IMGTool();
            if( !new File(filePath).exists() )
            {
                throw new IOException("文件不存在");
            }
            in = new FileInputStream(filePath);
            byte[] bytes = new byte[fileByteSize];
            int c=in.read(bytes);
            // 文件标识
            int index = 0;
            int fileMark = getUnsignedByte(bytes[index]);
            if(fileMark != 162)
            {
            	throw new IOException("文件标识不是rms");
            }
            rmsEntity.setIdentification(fileMark);
            // 版本
            index = 1;
            
            // 文件编码格式
            index = 2;
            
            // RMS数据文件总长度 index范围3-6
            index = 3;
            // RMS数据文件总长度，占4字节
            byte[] fileLengthByte = new byte[4];
            System.arraycopy(bytes, index, fileLengthByte, 0, 4);
            int fileSize = byte4ToInt(fileLengthByte);
//            EmpExecutionContext.info("文件长度："+fileSize);
            rmsEntity.setFileCount(fileSize);
            // 包含的文件数目 index范围7-8
            index = 7;
            byte[] fileCountByte = new byte[2];
            System.arraycopy(bytes, index, fileCountByte, 0, 2);
            int fileCount = byte2ToShort(fileCountByte);
//            EmpExecutionContext.info("文件数量："+fileCount);
            rmsEntity.setFileCount(fileCount);
            // 标题的字节长度
            index = 9;
            int titleSize = getUnsignedByte(bytes[9]);
//            EmpExecutionContext.info("标题的字节长度："+titleSize);
            rmsEntity.setTitleSize(titleSize);
            // 标题开始
            index = 10;
            if(titleSize!=0){
            	String titleName = new String(bytes, index, titleSize, "utf-8");
//            	EmpExecutionContext.info("标题："+titleName);
            	rmsEntity.setTitle(titleName);
            	index += titleSize;
            }
            
            if ((7 + fileSize) != fileByteSize) {
				rmsEntity = new RmsEntity();
				rmsEntity.setTitle("<p style='color: red;'>文件长度与实际长度不符,解析失败</p>");
				// EmpExecutionContext.info("文件长度与实际长度不符");
				return rmsEntity;
			}
            
            
            List<String> urls = new ArrayList<String>();
            int count = 0;
            for (int i = 0; i < fileCount; i++) {
				Excel2JsonDto dto = null;
                // 文件类型	1:smil 2:image 3:text 4:video 5:audio 6:图文  7:报表
                int fileType = getUnsignedByte(bytes[index]);
//                EmpExecutionContext.info("文件类型："+fileType);
                index += 1;
                //--------------增加文件类型6、7逻辑判断 开始--------------
             // 图文类型特殊处理
				if (fileType == 6) {
					byte[] paramContextByte = new byte[4];
					System.arraycopy(bytes, index, paramContextByte, 0, 4);
					int paramContextInt = byte4ToInt(paramContextByte);
					//EmpExecutionContext.info("控制参数信息长度：" + paramContextInt);
					index += 4;

					byte[] paramByte = new byte[paramContextInt];
					System.arraycopy(bytes, index, paramByte, 0, paramContextInt);
					String paramContext = new String(bytes, index, paramContextInt, "utf-8");
					index += paramContextInt;

					Integer backgroundNameLength = getUnsignedByte(bytes[index]);
					index += 1;

					byte[] backgroundNameByte = new byte[backgroundNameLength];
					System.arraycopy(bytes, index, paramByte, 0, backgroundNameLength);
					String backgroundName = new String(bytes, index, backgroundNameLength, "utf-8");
					index += backgroundNameLength;

					// 文件长度
					byte[] fileContextByte = new byte[4];
					System.arraycopy(bytes, index, fileContextByte, 0, 4);
					int fileContextInt = byte4ToInt(fileContextByte);
					//EmpExecutionContext.info("文件长度：" + fileContextInt);
					index += 4;
					count += fileContextInt;
					if (count >= fileSize) {
						rmsEntity = new RmsEntity();
						rmsEntity.setTitle("<p style='color: red;'>文件长度超过总文件长度,解析失败</p>");
						//EmpExecutionContext.info("文件长度超过总文件长度");
						return rmsEntity;
					}
					
					String backgroundUrl = fileOutDir + backgroundName;
					// 文件内容
					File fileout = new File(backgroundUrl);
					// listUrl.add(fileout.getPath());
					if (!fileout.exists()) {
                        boolean flag = fileout.createNewFile();
                        if (!flag) {
                            EmpExecutionContext.error("创建文件失败！");
                        }
					}
					urls.add(fileout.getPath());
					byte[] fileByte = new byte[fileContextInt];
					System.arraycopy(bytes, index, fileByte, 0, fileContextInt);
					writeFileContent(fileout, fileByte);
					index += fileContextInt;

					JsonParser parser = new JsonParser();
					JsonObject jsonObject = parser.parse(paramContext).getAsJsonObject();

					if(dtoList != null){
						//遍历，找出该文件属于哪一帧
						for(Excel2JsonDto jsonDto:dtoList){
							if(jsonDto.getParamFileName().contains(jsonObject.get("fina").getAsString())){
								dto = jsonDto;
							}
						}
					}

					String ctrinfos = jsonObject.get("ctrinfos").toString();
					String desc = fileOutDir+jsonObject.get("fina").getAsString();
//EmpExecutionContext.info("desc:"+desc);				
					JsonArray jsonArray = parser.parse(ctrinfos).getAsJsonArray();
					int j = 0;
					for (Object ctrinfoOb : jsonArray) {
						JsonObject ctrinfo = (JsonObject) ctrinfoOb;
						j++;
	                    String ctrinfoStr = ctrinfo.get("ctrinfo").getAsString();
	                    String[] params = ctrinfoStr.split("-");
	                    String cont = new String(Base64.decodeBase64(params[0].getBytes()),"UTF-8");
	                    //图参替换
						if(dto != null){
							Pattern pattern = Pattern.compile("(#P_\\d+#)");
							Matcher matcher = pattern.matcher(cont);
							int k = 1;
							while (matcher.find()){
								String val = matcher.group(1);
								cont = cont.replaceAll(val,URLDecoder.decode((String)dto.getImg().get("p" + k++),"utf-8"));
							}
						}
	                    String x_y = params[1];
	                    String w_h = params[2];
	                    String ffmt = params[3];
	                    String fcol = params[4];
	                    String fbgcol = params[5];
	                    String fsize = params[6];
	                    String fbold = params[7];
	                    String fagen = params[8];
	                    String rotate = params[9];
	                    String[] wh = w_h.split("\\*");
						String outfile = fileOutDir + "tw" + i + j + ".png";

						imgTool.convertCmd(imgTool.fontOperation(cont, ffmt, fsize, fcol, wh[0], fbgcol, fbold, fagen, outfile));
						
						String[] xy = x_y.split("\\*");
						imgTool.convertCmd(imgTool.compositeOperation(desc, outfile, desc, xy[0], xy[1]));
					}
					continue;
				
				}

				if(fileType == 7){
                    String colorType = "#37a2da,#ff9f7e,#66e1e3,#ffdb5c,#fb7293,#97bfff,#e162af,#9fe7b9,#e791d1,#e7bdf3,#32c5e9,#9d97f5,#8378eb";
                    byte[] paramContextByte = new byte[4];
					System.arraycopy(bytes, index, paramContextByte, 0, 4);
					int paramContextInt = byte4ToInt(paramContextByte);
					// EmpExecutionContext.info("控制参数信息长度：" + paramContextInt);
					index += 4;

					byte[] paramByte = new byte[paramContextInt];
					System.arraycopy(bytes, index, paramByte, 0, paramContextInt);
					String paramContext = new String(bytes, index, paramContextInt, "utf-8");
					index += paramContextInt;
					ReportFormUtil reportFormUtil = new ReportFormUtil();
					String color =  "#BCEE68,#B0E2FF,#D15FEE,#CD950C,#98FB98,#AB82FF,#836FFF,#CAFF70";
					JsonParser jsonParser = new JsonParser();
					JsonObject jsonObject = jsonParser.parse(paramContext).getAsJsonObject();

					if(jsonObject.get("datacor")!=null){
						color = jsonObject.get("datacor").getAsString();
					}
					//判断是否为不同内容发送的预览
					//不同内容发送预览方法时需要替换报表参数
					if(dtoList != null){
						//遍历，找出该文件属于哪一帧
						for(Excel2JsonDto jsonDto:dtoList){
							if(jsonDto.getParamFileName().contains(jsonObject.get("fina").getAsString())){
								dto = jsonDto;
							}
						}
					}

					String chartType = jsonObject.get("type").getAsString();
					String nameVariable = jsonObject.get("fina").getAsString().split(".png")[0];
					//标题解析
					String tiinfo = jsonObject.get("tiinfo").getAsString();
					tiinfo = tiinfo.substring(1).substring(0, tiinfo.length() - 2);
					String[] tiinfos = tiinfo.split(",");
					String tcont = new String(Base64.decodeBase64(tiinfos[0].getBytes("utf-8")),"UTF-8");
					String tfmt = tiinfos[1];
					String tcol = tiinfos[2];
					String tsize = tiinfos[3];
					String tunit = tiinfos[4];
					String tbold = tiinfos[5];
					String titalic = tiinfos[6];
					String tunderline = tiinfos[7];
					String tagen = tiinfos[8];

					//行解析
					String rownas = jsonObject.get("rownas").getAsString();
					String[] rowna = rownas.split(",");
					Integer rmax = Integer.valueOf(rowna[0]);
					String rfmt = rowna[1];
					String rcol = rowna[2];
					String rsize = rowna[3];
					String runit = rowna[4];
					String rbold = rowna[5];
					String ritalic = rowna[6];
					String rudline = rowna[7];
					String rtypeset = rowna[8];
					String cutlpos = rowna[9];
					
					//列解析
					String colnas  = jsonObject.get("colnas").getAsString();
					String[] colna = colnas.split(",");
					Integer cmax = Integer.valueOf(colna[0]);
					String cfmt  = colna[1];
					String ccol = colna[2];
					String csize = colna[3];
					String cunit = colna[4];
					String cbold = colna[5];
					String citalic = colna[6];
					String cudline = colna[7];
					String ctypeset = colna[8];
					
					StringBuffer barRowName = new StringBuffer();
					StringBuffer rowValue = new StringBuffer();
					
					StringBuffer barColName = new StringBuffer();
					StringBuffer barValue = new StringBuffer();
					
					String colattr = jsonObject.get("colattr").getAsString();
					String rowattr = jsonObject.get("rowattr").getAsString();

					//-1代表没有行或列 1表示全值动态
					if(rowattr.equals("1")||rowattr.equals("-1")){
                    	if(dto != null){
							//行表题的替换
							JSONObject rs = (JSONObject) dto.getImg().get("rs");
							if(rs != null){
								for (int k = 1,j = rs.size(); k < j + 1; k++) {
									barRowName.append(URLDecoder.decode(rs.getString("r" + k), "utf-8")).append(",");
								}
							}else {
								//预防报ArrayIndexOutOfBoundsException
								barRowName.append(",");
							}
							//预防报ArrayIndexOutOfBoundsException
							rowValue.append("1,");
						}else {
                    		//原有逻辑
							for (int k = 1; k < rmax+1; k++) {
								barRowName.append("{#行标题").append(k).append("#},");
								rowValue.append("1,");
							}
						}
					}else{
					    //0表示数值动态，从rms取行名列名
						String rowinfo = jsonObject.get("rowinfo").getAsString();
						String[] rowNames = rowinfo.split(",");
						for (String rowName : rowNames) {
							barRowName.append(new String(Base64.decodeBase64(rowName.getBytes("UTF-8")), "UTF-8")).append(",");
							rowValue.append("1,");
                        }
					}

                    //去掉最后一个字符串
					barRowName.deleteCharAt(barRowName.length()-1);
					rowValue.deleteCharAt(rowValue.length()-1);

					if(colattr.equals("1")||colattr.equals("-1")){
						if(dto != null){
							JSONObject cs = (JSONObject) dto.getImg().get("cs");
							if(cs != null){
								for (int k = 1,l = cs.size(); k < l + 1; k++) {
									barColName.append(URLDecoder.decode(cs.getString("c" + k), "utf-8")).append(",");
								}
							}else {
                                //预防报ArrayIndexOutOfBoundsException
                                barColName.append(",");
                            }
							//预防报ArrayIndexOutOfBoundsException
							barValue.append("@");
						}else {
							for (int k = 1; k < cmax+1; k++) {
								barColName.append("{#列标题").append(k).append("#},");
								barValue.append(rowValue.toString()).append("@");
							}
						}
					}else{
						String colinfo = jsonObject.get("colinfo").getAsString();
						String[] colNames = colinfo.split(",");
						for (String colName : colNames) {
							barColName.append(new String(Base64.decodeBase64(colName.getBytes("UTF-8")), "UTF-8")).append(",");
							barValue.append(rowValue.toString()).append("@");
                        }
					}

                    //去掉最后一个字符串
					barColName.deleteCharAt(barColName.length()-1);
					barValue.deleteCharAt(barValue.length()-1);
					if(fileOutDir.startsWith("/")){
		        		fileOutDir = fileOutDir.substring(1, fileOutDir.length());
		    		}
					if(dto != null){
						//替换报表标题
						if(dto.getTitle() != null){
							tcont = tcont.replaceAll("#P_1#",URLDecoder.decode(dto.getTitle().get("p1"),"utf-8"));
						}
						//替换数据
						JSONObject datas = (JSONObject) dto.getImg().get("datas");
						//如果是饼图则需要用到 rowValue color
                        if("1".equals(chartType)){
                            StringBuilder colorStr = new StringBuilder();
                            String[] strings = colorType.split(",");
                            barValue.setLength(0);
                            for(int o = 1,p = datas.size() + 1;o < p; o++){
                                barValue.append(datas.getString("r" + o)).append(",");
                                colorStr.append(strings[o - 1]).append(",");
                            }
                            color = colorStr.deleteCharAt(colorStr.length()-1).toString();
                            barValue.deleteCharAt(barValue.length()-1);
                        }else {
                            barValue.setLength(0);
                            Integer realCol = Integer.parseInt(dto.getImg().get("cscnt").toString());
                            StringBuilder res = new StringBuilder();
                            for(int x = 0;x < realCol;x++){
								res.setLength(0);
								for(int o = 1,p = datas.size() + 1;o < p; o++){
									res.append(datas.getString("r" + o).split(",")[x]).append(",");
								}
								res.deleteCharAt(res.length()-1);
								barValue.append(res).append("@");
							}
                            barValue.deleteCharAt(barValue.length()-1);
                        }
					}
//System.out.println("----解析chartType："+chartType);
//System.out.println("----解析tcont："+tcont);					
//System.out.println("----解析nameVariable："+nameVariable);					
//System.out.println("----解析barRowName："+barRowName);					
//System.out.println("----解析rowValue："+rowValue);					
//System.out.println("----解析barColName："+barColName);					
//System.out.println("----解析barValue："+barValue);					
					reportFormUtil.createPicture(request,chartType, tcont,nameVariable,fileOutDir , color,barColName.toString(),rowValue.toString(),barRowName.toString(),barValue.toString());
					//reportFormUtil.createPicture(request,chartType, tcont,nameVariable,fileOutDir , color, barColName.toString(), barValue.toString(), barRowName.toString(), rowValue.toString());
//					reportFormUtil.createPicture(chartType, tcont,nameVariable,fileOutDir , color, barColName.toString(), rowValue.toString(), barRowName.toString(), barValue.toString());
					continue;
				}
                //--------------增加文件类型6、7逻辑判断结束--------------
                //文件标题长度
                int fileNameSize= getUnsignedByte(bytes[index]);
//                EmpExecutionContext.info("第"+i+"个文件的文件名的字节长度"+fileNameSize);
                index += 1;
                
                //文件标题
                String fileName = new String(bytes, index, fileNameSize, "utf-8");
//                EmpExecutionContext.info("第"+i+"个文件的文件名："+fileName);
                index+=fileNameSize;
                
                //文件长度
                byte[] fileContextByte = new byte[4];
                System.arraycopy(bytes, index, fileContextByte, 0, 4);
                int fileContextInt= byte4ToInt(fileContextByte);
//                EmpExecutionContext.info("文件长度："+fileContextInt);
                index +=4;
                count += fileContextInt;
				if (count >= fileSize) {
					rmsEntity = new RmsEntity();
					rmsEntity.setTitle("<p style='color: red;'>文件长度超过总文件长度,解析失败</p>");
					// EmpExecutionContext.info("文件长度超过总文件长度");
					return rmsEntity;
				}
//               / rmsEntity.setDataSize(i,fileContextInt);
                //文件内容
                File fileDir = new File(fileOutDir);
                File fileout = new File(fileOutDir + fileName);
                if(!fileDir.exists()){
                	fileDir.mkdirs();
                }
//			 
				if (!fileout.exists()) {
                    boolean flag = fileout.createNewFile();
                    if (!flag) {
                        EmpExecutionContext.error("创建文件失败！");
                    }
				}
				urls.add(fileout.getPath());
				byte [] fileByte = new byte[fileContextInt];
				System.arraycopy(bytes, index, fileByte, 0, fileContextInt);
                //替换文本
                if(dtoList != null){
					Pattern pattern = Pattern.compile("(#P_\\d+#)");
                    //遍历，找出该文件属于哪一帧
					Boolean flag = true;
                    for(Excel2JsonDto jsonDto:dtoList){
                    	//TODO 处理静态图表带标题参数的情况,因为静态报表与标题在一张图片里，无法替换标题。直接跳过
                    	if(jsonDto.getTitle() != null && fileName.endsWith(".jpg")){
                    		break;
						}
						//判断是否为图参替换
						Boolean isImgParam = false;
                        if(jsonDto.getParamFileName().contains(fileName)){
                        	flag = false;
                            String txtContent = new String(fileByte,"utf-8");
							Matcher matcher = pattern.matcher(txtContent);
							int k = 1;
							while (matcher.find()){
								//匹配到就说明为图参替换
								isImgParam = true;
								String val = matcher.group(1);
								txtContent = txtContent.replaceAll(val,URLDecoder.decode(jsonDto.getTxt().get("p" + k++),"utf-8"));
							}
							if(isImgParam){
								writeFileContent(fileout, txtContent.getBytes("utf-8"));
							}else {
								writeFileContent(fileout, fileByte);
							}
                        }
                    }
                    if(flag){
						writeFileContent(fileout, fileByte);
					}
                }else {
                    writeFileContent(fileout, fileByte);
                }
                index+=fileContextInt;
			}
            rmsEntity.setFileUrl(urls);
            return rmsEntity;
        }
        catch( Exception e )
        {
        	EmpExecutionContext.error(e, "解析rms文件出现异常");
        	return rmsEntity;
        }
        finally{
        	if(in!=null){
        		try {
					in.close();
				} catch (IOException e) {
					EmpExecutionContext.error(e, e.getMessage());
				}
        	}
        }
    }
	
	/**
     * <pre>
     * 将长度为4的8位byte数组转换为32位int.
     * </pre>
     * 
     * @param arr
     * @return
     */
    public static int byte4ToInt(byte[] arr) {
        if (arr == null || arr.length != 4) {
            throw new IllegalArgumentException("byte数组必须不为空,并且是4位!");
        }
        return (int) (((arr[0] & 0xff) << 24) | ((arr[1] & 0xff) << 16) | ((arr[2] & 0xff) << 8) | ((arr[3] & 0xff)));
    }

    /**
     * 
     * <pre>
     * 长度为2的8位byte数组转换为一个16位short数字.
     * </pre>
     * 
     * @param arr
     * @return
     */
    public static short byte2ToShort(byte[] arr) {
        if (arr != null && arr.length != 2) {
            throw new IllegalArgumentException("byte数组必须不为空,并且是2位!");
        }
        return arr==null?null:(short) (((short) arr[0] << 8) | ((short) arr[1] & 0xff));
    }
    
    public int getUnsignedByte (byte data){      //将data字节型数据转换为0~255 (0xFF 即BYTE)。
    	return data&0x0FF; // 部分编译器会把最高位当做符号位，因此写成0x0FF.
    }
	
    /**
	 * 将smil文件解析成HTML文件字符串
	 * @param smilDir
	 * @param pathSvt
	 * @return
	 */
    @SuppressWarnings("unchecked")
	public StringBuffer smilToHtmlForPreview(String smilDir, String pathSvt, String title, String smilFileUrl){
    	StringBuffer sb = new StringBuffer();
		SAXBuilder saxBuilder = new SAXBuilder();
		try {
//			sb.append("<div style='width: 224;text-align: center;' >");
//			sb.append(title);
//			sb.append("</div>");
			Document document = saxBuilder.build(new InputStreamReader(new FileInputStream(smilFileUrl),"utf-8"));
			Element element = document.getRootElement();
			List<Element> bodyList = element.getChildren("body");
			for (Element body : bodyList) {
				List<Element> parList = body.getChildren("par");
				for (Element par : parList) {
					List<Element> htmlEments = par.getChildren();
					//EmpExecutionContext.info(par.getContent());
					for (Element htmlEment : htmlEments) {
						if(htmlEment.getName().equals("text")){
							BufferedReader br = null;
							InputStreamReader isr = null;
							sb.append("<div style='word-break: break-all;word-wrap:break-word;'>");
							try {
								String line = "";
//								 br= new BufferedReader(new FileReader
//								(new File(smilDir+"/"+htmlEment.getAttributeValue("src"))));
//								while ((line = br.readLine())!=null) {
//									sb.append(line);
//									sb.append("<br/>");
//								}
								  isr = new InputStreamReader(new FileInputStream(smilDir+"/"+htmlEment.getAttributeValue("src")), "UTF-8");
							      br = new BufferedReader(isr);
							      while ((line = br.readLine()) != null) {
							    		sb.append(line);
										sb.append("<br/>");
							      }
								
								
							}finally{
								if(br!=null){
									br.close();
								}
								if(isr != null){
									isr.close();
								}
							}
							sb.append("</div>");
						}else if(htmlEment.getName().equals("video")||htmlEment.getName().equals("audio")){
							sb.append("<");
							sb.append(htmlEment.getName());
							sb.append(" style='width: 100%;' controls='controls' src='");
							sb.append(pathSvt+htmlEment.getAttributeValue("src"));
							sb.append("'>");
							sb.append("您的浏览器不支持 video,audio 标签。");
							sb.append("</");
							sb.append(htmlEment.getName());
							sb.append(">");
						}
						else{
							sb.append("<");
							sb.append(htmlEment.getName());
							sb.append(" width='100%' src='");
							sb.append(pathSvt+htmlEment.getAttributeValue("src"));
							sb.append("' />");
						}
					}
				}
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"生成预览html出现异常");
		}
		return sb;
	}
    /**
     * V1.0 rms 文件解析
     * @param filePath
     * @param fileOutDir
     * @param fileByteSize
     * @return
     */
    public RmsEntity Parse(String filePath, String fileOutDir, Integer fileByteSize)
    {
		RmsEntity rmsEntity = new RmsEntity();
		InputStream in = null;
        try
        {
            if( !new File(filePath).exists() )
            {
                throw new IOException("文件不存在");
            }
            in = new FileInputStream(filePath);
            byte[] bytes = new byte[fileByteSize];
            int c=in.read(bytes);
            // 文件标识
            int index = 0;
            int fileMark = getUnsignedByte(bytes[index]);
            if(fileMark != 162)
            {
            	throw new IOException("文件标识不是rms");
            }
            rmsEntity.setIdentification(fileMark);
            /*if( bytes[0] != (byte)162 )
            {
                throw new IOException("文件标识不是rms");
            }*/
            
            
            // 版本
            index = 1;
            
            // 文件编码格式
            index = 2;
            
            // RMS数据文件总长度 index范围3-6
            index = 3;
            // RMS数据文件总长度，占4字节
            byte[] fileLengthByte = new byte[4];
            System.arraycopy(bytes, index, fileLengthByte, 0, 4);
            int fileSize = byte4ToInt(fileLengthByte);
            EmpExecutionContext.info("文件长度："+fileSize);
            rmsEntity.setFileCount(fileSize);
            // 包含的文件数目 index范围7-8
            index = 7;
            byte[] fileCountByte = new byte[2];
            System.arraycopy(bytes, index, fileCountByte, 0, 2);
            int fileCount = byte2ToShort(fileCountByte);
            EmpExecutionContext.info("文件数量："+fileCount);
            rmsEntity.setFileCount(fileCount);
            // 标题的字节长度
            index = 9;
            int titleSize = getUnsignedByte(bytes[9]);
            EmpExecutionContext.info("标题的字节长度："+titleSize);
            rmsEntity.setTitleSize(titleSize);
            // 标题开始
            index = 10;
            if(titleSize!=0){
            	String titleName = new String(bytes, index, titleSize, "utf-8");
            	EmpExecutionContext.info("标题："+titleName);
            	rmsEntity.setTitle(titleName);
            	index += titleSize;
            }
            
            List<String> urls = new ArrayList<String>();
            for (int i = 0; i < fileCount; i++) {
                // 文件类型	1:smil 2:image 3:text 4:video 5:audio
                int fileType = getUnsignedByte(bytes[index]);
                EmpExecutionContext.info("文件类型："+fileType);
                index += 1;
                
                //文件标题长度
                int fileNameSize= getUnsignedByte(bytes[index]);
                EmpExecutionContext.info("第"+i+"个文件的文件名的字节长度"+fileNameSize);
                index += 1;
                
                //文件标题
                String fileName = new String(bytes, index, fileNameSize, "utf-8");
                EmpExecutionContext.info("第"+i+"个文件的文件名："+fileName);
                index+=fileNameSize;
                
                //文件长度
                byte[] fileContextByte = new byte[4];
                System.arraycopy(bytes, index, fileContextByte, 0, 4);
                int fileContextInt= byte4ToInt(fileContextByte);
                EmpExecutionContext.info("文件长度："+fileContextInt);
                index +=4;
                
                rmsEntity.setDataSize(i,fileContextInt);
                //文件内容
                File fileDir = new File(fileOutDir);
                File fileout = new File(fileOutDir + fileName);
                if(!fileDir.exists()){
                	fileDir.mkdirs();
                }
				if (!fileout.exists()) {
                    boolean flag = fileout.createNewFile();
                    if (!flag) {
                        EmpExecutionContext.error("创建文件失败！");
                    }
				}
				urls.add(fileout.getPath());
				byte [] fileByte = new byte[fileContextInt];
				System.arraycopy(bytes, index, fileByte, 0, fileContextInt);
                writeFileContent(fileout, fileByte);
                index+=fileContextInt;
			}
            rmsEntity.setFileUrl(urls);
            return rmsEntity;
        }
        catch( Exception e )
        {
        	EmpExecutionContext.error(e, "解析rms文件出现异常");
        	return rmsEntity;
        }
        finally{
        	if(in!=null){
        		try {
					in.close();
				} catch (IOException e) {
					EmpExecutionContext.error(e, e.getMessage());
				}
        	}
        }
    }
  
    /**
     * V1.0
     * @param htmlDir
     * @param pathSvt
     * @param title
     * @param smilFileUrl
     */
    public void createPreviewHtml(String htmlDir, String pathSvt, String title, String smilFileUrl){
		
		File dir = new File(htmlDir);
		if(!dir.exists()){
			dir.mkdirs();
		}
		BufferedWriter bw = null;
		FileOutputStream fos =null;
		try {
			File file = new File(dir.getParent()+"/fuxinPreview.html");
			AnalysisRMS analysisRMS = new AnalysisRMS();
			StringBuffer sb = analysisRMS.smilToHtmlForPreview(htmlDir,pathSvt, title,  smilFileUrl);
			if(!file.exists()){
                boolean flag = file.createNewFile();
                if (!flag) {
                    EmpExecutionContext.error("创建文件失败！");
                }
			}
			
			fos = new FileOutputStream(file.getAbsoluteFile());
			fos.write(sb.toString().getBytes("UTF-8"));
			
//			bw= new BufferedWriter(new FileWriter(file));
//			bw.write(sb.toString());
//			bw.flush();
		} catch (Exception e) {
			EmpExecutionContext.error(e,"写入fuxin.html文件出现异常");
		}finally{
			try {
				//bw.close();
				if(fos!=null){
					fos.close();
				}
			} catch (IOException e) {
				EmpExecutionContext.error(e,"关闭IO流出现异常");
			}
		}
	}
	
	
}
