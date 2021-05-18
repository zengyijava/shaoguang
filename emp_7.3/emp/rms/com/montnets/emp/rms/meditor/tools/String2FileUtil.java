package com.montnets.emp.rms.meditor.tools;

import com.alibaba.fastjson.JSONObject;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.rms.meditor.config.UploadFileConfig;
import com.montnets.emp.rms.meditor.entity.LfFodder;
import com.montnets.emp.rms.tools.IMGTool;
import com.montnets.emp.rms.tools.MediaTool;
import com.montnets.emp.util.StringUtils;
import com.montnets.emp.util.TxtFileUtil;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.MultimediaInfo;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * H5工具类(将String转为File,获取图片路径)
 * @author dell
 *
 */
public class String2FileUtil {

	//上传的服务器IP地址
	private static final String uploadPath = SystemGlobals.getValue("montnet.rms.uploadPath").trim();
	//视频裁剪工具路径地址
	private static final String toolPath = SystemGlobals.getValue("montnets.rms.cropper.videoToolPath");

	private static final String rootPath = SystemGlobals.getValue("montnet.rms.nginx.rootPath");

	private static final String operatingSystem = SystemGlobals.getValue("montnets.rms.videoEncoder.system");


	/**
	 * 将html写入到本地文件并上传到文件服务器
	 * @param html
	 * @param corpCode
	 * @return
	 */
	public static String uploadHFive(String html,String corpCode) {
		File file = null;
		String uploadUrl = null;
		try {
			String path = new TxtFileUtil().getWebRoot();//windows环境下,path是/开头
			String filePath = null;
			//设置路径
			Date d = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String dateNowStr = sdf.format(d);
			filePath = path +"upload/"+dateNowStr+"/";
			File fileDir=new File(filePath);
			if(!fileDir.exists()) {//如果文件夹不存在
				fileDir.mkdirs();//创建文件夹
			}
			filePath = filePath+System.currentTimeMillis() / 1000 +".html";
			//创建本地文件
			file = new File(filePath);
			if(!file.exists()){
                boolean flag = file.createNewFile();
                if (!flag) {
                    EmpExecutionContext.error("创建文件失败！");
                }
			}
			//将字符串写入到本地file中
			FileOutputStream fos = null;
			try{
				fos= new FileOutputStream(filePath);
				fos.write(html.getBytes());
			}finally{
				if(fos != null){
					fos.close();
				}
			}

			//将本地文件上传到文件服务器
			//web/yyyymmdd/id.html  标准版生成规则
			//web/corpcode/yyyymmdd/id.html   托管版生成规则
			if(file.exists()){
				String fpath = null;
				if(StringUtils.isNotEmpty(corpCode)){
					fpath = corpCode+"/"+dateNowStr;
				}else{
					fpath = dateNowStr;
				}
				String btype = "2";//2表示H5图文上传
				String ftype = "1";//1表示html文件
				uploadUrl = uploadFile(file,fpath,btype,ftype);
				if (StringUtils.isNotEmpty(uploadUrl)){//删除本地文件
                    boolean flag = file.delete();
                    if (!flag) {
                        EmpExecutionContext.error("删除文件失败！");
                    }
				}
			}
		} catch (Exception e) {
            EmpExecutionContext.error(e, "发现异常！");
		}
		return uploadUrl;
	}


	public static List<LfFodder> saveFile(ServletFileUpload upload, HttpServletRequest request) {
		File file = null;
		String uploadUrl = null;
		List<LfFodder> list = new ArrayList<LfFodder>();
		upload.setHeaderEncoding("UTF-8");
		String path = new TxtFileUtil().getWebRoot();//windows环境下,path是/开头

		MediaTool mediaTool = new MediaTool();//视频裁剪工具,此处用于获取视频首帧图片
		//获取登录信息
		LfSysuser lfSysuser = UserUtil.getUser(request);
		try {
			// 解析请求，获取文件项
			List fileItems = upload.parseRequest(request);
			// 处理上传的文件项
			Iterator i = fileItems.iterator();
			while (i.hasNext()) {
				FileItem fileStream = (FileItem) i.next();
				//单个上传
				if (!fileStream.isFormField()) {
					LfFodder lfFodder = new LfFodder();
					// 获取上传文件的参数
					String fileName = fileStream.getName();
					String suffix = fileName.substring(fileName.lastIndexOf("."));
					String contentType = fileStream.getContentType();
					boolean isInMemory = fileStream.isInMemory();
					long sizeInBytes = fileStream.getSize();//%1024%1024
					String ftype = null;
					String btype = null;//1表示HTML上传,2表示H5图文上传
					String fpath = null;
					if (StringUtils.isNotEmpty(contentType)) {
						if (contentType.indexOf("video") >= 0) {
							ftype = "3";//2:图片 3:视频 4：音频
							btype = "2";
						} else if (contentType.indexOf("audio") >= 0) {
							ftype = "4";//2:图片 3:视频 4：音频
							btype = "2";
						} else if (contentType.indexOf("image") >= 0) {
							ftype = "2";//2:图片 3:视频 4：音频
							btype = "2";
						}
					}
					//设置路径
					Date d = new Date();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
					String dateNowStr = sdf.format(d);
					String filePath = path + "upload/" + dateNowStr + "/";
					File fileDir = new File(filePath);
					if (!fileDir.exists()) {//如果文件夹不存在
						fileDir.mkdirs();//创建文件夹
					}
					String writeFileName = String.valueOf(System.currentTimeMillis());
					file = new File(filePath + writeFileName + suffix);//+"upload/"+dateNowStr+"/"
					fileStream.write(file);
					fpath = "upload/" + dateNowStr;//上传保存路径
					if(!"3".equals(ftype)){
						uploadUrl = uploadFile(file, fpath, btype, ftype);//上传资源文件
					}
					if ("2".equals(ftype)) {
						//生成缩略图..
						IMGTool imgTool = new IMGTool();
						String newFileName = file.getName();
						newFileName = newFileName.substring(0, newFileName.lastIndexOf(".")) + "_small"+newFileName.substring(newFileName.lastIndexOf("."), newFileName.length());
						String newFilePath = file.getPath().substring(0, file.getPath().lastIndexOf(File.separator) + 1) + newFileName;
						imgTool.createThumbnail(120, 120, file.getPath(), newFilePath);
						File newFile = new File(newFilePath);
						if (newFile.exists()) {
							String newUplodUrl = uploadFile(newFile, fpath, btype, ftype);
							if (StringUtils.isNotEmpty(newUplodUrl)) {
                                boolean flag = newFile.delete();
                                if (!flag) {
                                    EmpExecutionContext.error("删除文件失败！");
                                }
							}
						}
					}
					if ("4".equals(ftype)) {
						Encoder encoder = new Encoder();
						MultimediaInfo m = encoder.getInfo(file);
						Long duration = m.getDuration();
						lfFodder.setDuration(duration);
					}
					if ("3".equals(ftype)) {
						//判断是否是MP4格式,不是MP4格式需要转码
						if(!".MP4".equalsIgnoreCase(suffix)){
							if((".mpg").equalsIgnoreCase(suffix)||(".avi").equalsIgnoreCase(suffix)||(".vob").equalsIgnoreCase(suffix)||(".wmv").equalsIgnoreCase(suffix)
									||(".3gp").equalsIgnoreCase(suffix)||(".flv").equalsIgnoreCase(suffix)||(".ogv").equalsIgnoreCase(suffix)||(".swf").equalsIgnoreCase(suffix)){
								String tarPath = file.getAbsolutePath().replaceAll(suffix,".MP4");
								int tranResult = mediaTool.conversionVideoFormatDeleteC(toolPath, file.getAbsolutePath(), tarPath);
								if(0 == tranResult){
									if (file != null) {//删除本地文件
                                        boolean flag = file.delete();
                                        if (!flag) {
                                            EmpExecutionContext.error("删除文件失败！");
                                        }
									}
									//设置转换后的文件
									file = new File(tarPath);
								}
							}else if((".f4v").equalsIgnoreCase(suffix)||(".m4v").equalsIgnoreCase(suffix)||(".mkv").equalsIgnoreCase(suffix)||(".mov").equalsIgnoreCase(suffix)||(".mp4").equalsIgnoreCase(suffix)){
								String tarPath = file.getAbsolutePath().replaceAll(suffix,".MP4");
								int tranResult = mediaTool.conversionVideoFormat(toolPath, file.getAbsolutePath(), tarPath);
								if (0 == tranResult){
									if (file != null) {//删除本地文件
                                        boolean flag = file.delete();
                                        if (!flag) {
                                            EmpExecutionContext.error("删除文件失败！");
                                        }
									}
									//设置转换后的文件
									file = new File(tarPath);
								}
							}
						}
						uploadUrl = uploadFile(file, fpath, btype, ftype);//上传资源文件
						Encoder encoder = new Encoder();
						MultimediaInfo m = encoder.getInfo(file);
						Long duration = m.getDuration();
						lfFodder.setDuration(duration);
						String ratio = String.valueOf(new BigDecimal(m.getVideo().getSize().getWidth()).divide(new BigDecimal(m.getVideo().getSize().getHeight()), 2, RoundingMode.HALF_UP));
						lfFodder.setWidth((long) m.getVideo().getSize().getWidth());
						lfFodder.setHeight((long) m.getVideo().getSize().getHeight());
						lfFodder.setRadio(ratio);
						//生成视频首帧图片并上传
						//图片生成路径
						String targetPath =fileDir.getPath();//文件夹路径
						//图片名,首帧图片命名规则,视频名+1
						String name = writeFileName+1;
						int flag = mediaTool.getFirstFrame(toolPath,file.getAbsolutePath(),targetPath,name);//根据返回值判断是否获取成功了
						String firstFrameUrl = null;//上传之后的返回路径
						File fisrFrameFile = null;
						if(0 == flag){//0表示获取生成首帧图片成功
							fisrFrameFile = new File(targetPath + "/" + name + ".png");
							//将图片上传
							firstFrameUrl = uploadFile(fisrFrameFile, fpath, "2", "2");//上传首帧图片
						}
						//如果不为空,表示上传成功,则设置lfFodder
						if(StringUtils.isNotEmpty(firstFrameUrl)){
							lfFodder.setFistFramePath(firstFrameUrl);

							if (StringUtils.isNotEmpty(fisrFrameFile==null?"":fisrFrameFile.getPath())) {//删除本地文件
                                boolean status = fisrFrameFile==null?false:fisrFrameFile.delete();
                                if (!status) {
                                    EmpExecutionContext.error("删除文件失败！");
                                }
							}
						}

					} else if ("2".equals(ftype)) {
						//读取图片对象
						BufferedImage img = ImageIO.read(file);
						//获得图片的宽
						int width = img.getWidth();
						//获得图片的高
						int height = img.getHeight();
						String ratio = String.valueOf(new BigDecimal(width).divide(new BigDecimal(height), 2, RoundingMode.HALF_UP));
						lfFodder.setWidth((long) width);
						lfFodder.setHeight((long) height);
						lfFodder.setRadio(ratio);
					}
					lfFodder.setOriginal(fileName.substring(0,fileName.lastIndexOf(".")));
					lfFodder.setFoSize(file.length());
					lfFodder.setUrl(uploadUrl);
					lfFodder.setFoType(Long.parseLong(ftype));
					if (StringUtils.isNotEmpty(uploadUrl)) {//删除本地文件
                        boolean flag = file.delete();
                        if (!flag) {
                            EmpExecutionContext.error("删除文件失败！");
                        }
					}
					list.add(lfFodder);
				}
			}
		} catch (Exception ex) {
			EmpExecutionContext.error(ex,"saveFile上传文件失败");
		}
		return list;
	}

	public static String getBodyContent(String totalHtml){
		String start = "<body>";
		String end = "</body>";
		String content = totalHtml.substring(totalHtml.indexOf(start)+start.length(),totalHtml.indexOf(end));
		for(int i=0;i<2;i++) {
			content = content.substring(content.indexOf("</div>")+"</div>".length(),content.length());
		}
		return content;
	}

	public static String downloadFile(String filePath)throws Exception{
		//根据网络文件地址创建URL
		URL url = new URL(filePath);
		//获取此路径的连接
		URLConnection conn = url.openConnection();
		BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
		String s; // 依次循环，至到读的值为空
		StringBuilder sb = new StringBuilder();
		while ((s = reader.readLine()) != null) {
			sb.append(s);
		}
		reader.close();
		String str = sb.toString();
		return str;
	}

	public static void downloadAV(String filePath){
		try {
			URL url = new URL(filePath);
			InputStream inputStream = url.openStream();
			String localFilePath = new TxtFileUtil().getWebRoot()+filePath.substring(filePath.indexOf(rootPath),filePath.length());
			if(("windows").equalsIgnoreCase(operatingSystem) && localFilePath.indexOf("/") == 0){
				localFilePath = localFilePath.substring(1);
			}
			//判断对应文件夹是否存在
			String localDirPath = localFilePath.substring(0,localFilePath.lastIndexOf("/"));
			File fileDir = new File(localDirPath);
			if (!fileDir.exists()){
				fileDir.mkdirs();
			}
			File file = new File(localFilePath);// FileOutputStream继承自抽象类OutPutStream 实例化了write方法
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			// 接收缓冲
			byte[] bytes = new byte[0x400];
			int count = inputStream.read(bytes);
			do { // 从bytes的第0个字节开始,把count个字节写到文件里
				fileOutputStream.write(bytes, 0, count);
				// 把数据从url读出count个字节,保存在bytes
				count = inputStream.read(bytes);
			}
			while (count != -1);
			// 如果inputStream中的数据全部读完了,则count为-1
			inputStream.close();
			fileOutputStream.close();
		} catch (IOException e) {
			EmpExecutionContext.error(e,"下载文件失败");
		}
	}

	/**
	 * 删除单个文件
	 * @param   sPath    被删除文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public static boolean delLocalFile(String sPath){
		boolean flag = false;
		File file = new File(sPath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
            boolean status = file.delete();
            if (!status) {
                EmpExecutionContext.error("删除文件失败！");
            }
			flag = true;
		}
		if(flag){
			EmpExecutionContext.info( "删除单个文件"+sPath+"成功");
		}else{
			EmpExecutionContext.info( "删除单个文件"+sPath+"失败");
		}
		return flag;
	}

	public static String deleteFile(String fpath)throws Exception{
		//用于加密
		String secretkey=UploadFileConfig.MD5_SECRETKEY;
		String sysid="1000";
		//10位的long类型（用来校验请求时间前后24小时）
		Long currTime = System.currentTimeMillis() / 1000;
		String timestamp=String.valueOf(currTime);//

		String ftype = "1";
		String btype = "1";

		//获取文件名及后缀
		String fname = fpath.substring(fpath.lastIndexOf("/")+1);
		fpath = fpath.replace("/"+fname,"");
		//md5加密的sign值
		String sign=MD5Utils.MD5deal(sysid,timestamp,btype,ftype,fpath,fname,secretkey);

		String url = uploadPath+"uploadWeb/DeleteFile.htm?sysid="+sysid+"&sign="+sign
				+"&timestamp="+timestamp+"&btype="+btype+"&ftype="+ftype+"&fpath="+fpath+"&fname="+fname;;

		String res = postFile(url,null);
		JSONObject jsonObject=JSONObject.parseObject(res);
		String code = jsonObject.getString("code");//1成功 0失败
		if("1".equals(code)){//删除缩略图
			fname = "small_"+fname;
			sign = MD5Utils.MD5deal(sysid,timestamp,btype,ftype,fpath,fname,secretkey);
			url=uploadPath+"uploadWeb/DeleteFile.htm?sysid="+sysid+"&sign="+sign+"&timestamp="+timestamp+"&btype="+btype+"&ftype="+ftype+"&fpath="+fpath+"&fname="+fname;
			res = postFile(url,null);
		}
		return code;
	}

	/**
	 *
	 * @param filePath
	 * @param fpath 1:HTML 2:图片 3:视频 4：音频  5：zip
	 * @param btype 1:模板文件上传 2：H5图文上传
	 * @param ftype
	 * @return
	 * @throws Exception
	 */
	public static String delFile(String filePath,String fpath,String btype,String ftype)throws Exception{
		//用于加密
		String secretkey=UploadFileConfig.MD5_SECRETKEY;
		String sysid="1000";
		//10位的long类型（用来校验请求时间前后24小时）
		Long currTime = System.currentTimeMillis() / 1000;
		String timestamp=String.valueOf(currTime);//


		//获取文件名及后缀
		String fname = filePath.substring(filePath.lastIndexOf(File.separator)+1, filePath.length());

		//md5加密的sign值
		String sign=MD5Utils.MD5deal(sysid,timestamp,btype,ftype,fpath,fname,secretkey);

		String url = uploadPath+"uploadWeb/DeleteFile.htm?sysid="+sysid+"&sign="+sign
				+"&timestamp="+timestamp+"&btype="+btype+"&ftype="+ftype+"&fpath="+fpath+"&fname="+fname;;

		String res = postFile(url,null);
		JSONObject jsonObject=JSONObject.parseObject(res);
		String code = jsonObject.getString("code");//0成功 1失败
		return code;
	}

	/**
	 * html上传
	 * @param file
	 * @param fpath
	 * @param btype
	 * @param ftype
	 * @param cropCode
	 * @return
	 * @throws Exception
	 */
	public static String htmlUpoadFile(File file,String fpath,String btype,String ftype,String cropCode)throws Exception{
		//根据btype设置fpath
		if("1".equals(btype)){//如果是html,则需要按照标准来
			if(StaticValue.getCORPTYPE() == 0){//如果是单企业
				Date d = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				String dateNowStr = sdf.format(d);
				fpath =dateNowStr;
			}else if(StaticValue.getCORPTYPE() == 1){//如果是多企业
				Date d = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				String dateNowStr = sdf.format(d);
				fpath = cropCode+"/"+dateNowStr;
			}
		}
		String url = uploadFile(file, fpath, btype, ftype);
		return url;
	}

	public static String uploadFile(File file,String fpath,String btype,String ftype) throws Exception {
		String sysid="1000";
		//10位的long类型（用来校验请求时间前后24小时）
		Long currTime = System.currentTimeMillis() / 1000;
		String timestamp=String.valueOf(currTime);

		String fname = file.getName();
		String secretkey=UploadFileConfig.MD5_SECRETKEY;

		//md5加密的sign值
		String sign=MD5Utils.MD5deal(sysid,timestamp,btype,ftype,fpath,fname,secretkey);

		String url = uploadPath+"uploadWeb/UploadFile.htm?sysid="+sysid+"&sign="+sign
				+"&timestamp="+timestamp+"&btype="+btype+"&ftype="+ftype+"&fpath="+fpath+"&fname="+fname;;


		String res = postFile(url,file);
		JSONObject jsonObject=JSONObject.parseObject(res);
		String fileUrl = jsonObject.getString("url");
		return fileUrl;
	}

	/**
	 * 获取html中的image路径
	 * @return
	 */
	public static List<String> getImageSrc(String content){
		List<String> srcList = new ArrayList<String>(); //用来存储获取到的图片地址
		Pattern p = Pattern.compile("<(img|IMG)(.*?)(>|></img>|/>)");//匹配字符串中的img标签
		Matcher matcher = p.matcher(content);
		boolean hasPic = matcher.find();
		if(hasPic == true)//判断是否含有图片
		{
			while(hasPic) //如果含有图片，那么持续进行查找，直到匹配不到
			{
				String group = matcher.group(2);//获取第二个分组的内容，也就是 (.*?)匹配到的
				Pattern srcText = Pattern.compile("(src|SRC)=(\"|\')(.*?)(\"|\')");//匹配图片的地址
				Matcher matcher2 = srcText.matcher(group);
				if( matcher2.find() )
				{
					srcList.add( matcher2.group(3) );//把获取到的图片地址添加到列表中
				}
				hasPic = matcher.find();//判断是否还有img标签
			}

		}
		System.out.println("匹配到的内容："+srcList);
		return srcList;
	}

	public static Date getDateFormat(String time){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return df.parse(time);
		} catch (Exception e) {
            EmpExecutionContext.error(e, "发现异常！");
		}
		return null;
	}

	/**
	 * 将日期格式转为文件名
	 * @param time
	 * @return
	 */
	public static String getTimeString(String time){
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(getDateFormat(time));
		return getTimeString(calendar);
	}


	private static String getTimeString(Calendar calendar) {
		StringBuffer sb = new StringBuffer();
		sb.append(String.valueOf(calendar.get(Calendar.YEAR)))
				.append(valueOfString(String.valueOf(calendar.get(Calendar.MONTH) + 1),2))
				.append(valueOfString(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)),2))
				.append(valueOfString(String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)),2))
				.append(valueOfString(String.valueOf(calendar.get(Calendar.MINUTE)),2))
				.append(valueOfString(String.valueOf(calendar.get(Calendar.SECOND)),2))
				.append(valueOfString(String.valueOf((int)(Math.random()*900 + 100)),3));
		return sb.toString();
	}

	private static String valueOfString(String str, int len) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < len - str.length(); i++) {
			sb.append("0");
		}
		return (sb.length() == 0) ? (str) : (sb.toString() + str);
	}

	/**
	 * 将字符串写入指定文件(当指定的父路径中文件夹不存在时，会最大限度去创建，以保证保存成功！) 
	 *
	 * @param res            原字符串 
	 * @param filePath 文件路径 
	 * @return 成功标记
	 */
	public static File string2File(String res,String filePath) {
		BufferedReader bufferedReader = null;
		BufferedWriter bufferedWriter = null;
		File distFile = null;
		try {
			distFile = new File(filePath);
			if (!distFile.getParentFile().exists()) {
                distFile.getParentFile().mkdirs();
            }
			bufferedReader = new BufferedReader(new StringReader(res));
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(distFile), "UTF-8"));
			char buf[] = new char[1024];         //字符缓冲区 
			int len;
			while ((len = bufferedReader.read(buf)) != -1) {
				bufferedWriter.write(buf, 0, len);
			}
			bufferedWriter.flush();
//			bufferedReader.close();
//			bufferedWriter.close();
		} catch (IOException e) {
            EmpExecutionContext.error(e, "发现异常！");
			distFile = null;
			return distFile;
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
                    EmpExecutionContext.error(e, "发现异常！");
				}
			}
			if(bufferedWriter != null){
				try {
					bufferedWriter.close();
				} catch (IOException e) {
                    EmpExecutionContext.error(e, "发现异常！");
				}
			}
		}
		return distFile;
	}


	public static  String postFile(String url, File file) throws ClientProtocolException, IOException {
		String res = null;
		CloseableHttpClient httpClient = null;
		try{
			httpClient= HttpClients.createDefault();
			HttpPost httppost = new HttpPost(url);
			if(file != null) {
				httppost.setEntity(getMutipartEntry(file));
			}
			CloseableHttpResponse response = httpClient.execute(httppost);
			HttpEntity entity = response.getEntity();
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				res = EntityUtils.toString(entity, "UTF-8");
				response.close();
			} else {
				res = EntityUtils.toString(entity, "UTF-8");
				response.close();
				throw new IllegalArgumentException(res);
			}
		}finally{
			if(httpClient != null){
				try{
					httpClient.close();
				}catch(IOException e){
					EmpExecutionContext.error(e, "网络关闭异常");
				}
			}
		}
		return res;
	}


	private static MultipartEntity getMutipartEntry(File file) throws UnsupportedEncodingException {
		if (file == null) {
			throw new IllegalArgumentException("文件不能为空");
		}
		FileBody fileBody = new FileBody(file);
		FormBodyPart filePart = new FormBodyPart("file", fileBody);
		MultipartEntity multipartEntity = new MultipartEntity();
		multipartEntity.addPart(filePart);
		return multipartEntity;
	}

	/**
	 * 获取项目所在路径
	 * @return 项目路径
	 */
	public static String getWebRoot(){
		String webRoot = new TxtFileUtil().getWebRoot();
		if (System.getProperty("os.name").toLowerCase().contains("windows")){
			webRoot =  webRoot.substring(1);
		}
		return webRoot;
	}
}
