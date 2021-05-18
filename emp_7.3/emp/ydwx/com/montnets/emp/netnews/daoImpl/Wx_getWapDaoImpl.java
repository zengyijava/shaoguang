package com.montnets.emp.netnews.daoImpl;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.netnews.common.AllUtil;
import com.montnets.emp.netnews.common.CompressEncodeing;
import com.montnets.emp.netnews.common.FileJsp;
import com.montnets.emp.netnews.entity.LfWXBASEINFO;
import com.montnets.emp.netnews.entity.LfWXPAGE;

public class Wx_getWapDaoImpl extends BaseBiz{

	ResourceBundle bundle = ResourceBundle.getBundle("resourceBundle");

	/**
	 * 网讯审核生成网讯访问页面
	 * 
	 * @param netId
	 * @param iphORhe
	 * @return Map
	 */
	public void getNetByJsp(String netId, String iphORhe,String status,String replaceSrc,String contextPath) 
	{
		try {
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("NETID",netId);
			List<LfWXPAGE> pages = getByCondition(LfWXPAGE.class, conditionMap, null);
			List<LfWXBASEINFO> basesList = getByCondition(LfWXBASEINFO.class, conditionMap, null);
			if(basesList == null || basesList.size()==0){
				EmpExecutionContext.error("网讯id为空！");
				return;
			}
			LfWXBASEINFO base=basesList.get(0);
			String outurl="";
			CommonBiz biz=new CommonBiz();
			String[] filePath=biz.getALiveFileServer();
			if(filePath!=null&&filePath.length>1){
				outurl=filePath[1];
			}
			
			Long pageid = null;
			//String basePath = "";
			//File file= null;
			if(pages!=null && pages.size()>0){
			for(LfWXPAGE page:pages){
				
				pageid = page.getID();
				/*filename="wx_"+pageid+".jsp";
				basePath = new TxtFileUtil().getWebRoot()+StaticValue.UPLOAD_JSP+filename;
				file=new File(basePath);
				if(base.getSTATUS().equals(1) && file!=null && !file.exists()) { 
					//判断网讯文件是否存在
					continue;
				}*/
				
				String cont=FileJsp.getContent(pageid+"");
				
				//page.getCONTENT()
				String content = cont.replaceAll(">？</td>","></td>").replaceAll(">\\?</td>", "></td>");
				// 替换超链接页面保存是onclick，手机不能用
				// <a
				// href="javascript:mylink(&quot;/netmsg/wx.nms?w=w7${h}&quot;,&quot;2055&quot;,&quot;My_Custom&quot;);"
				// target="_self">
				content=content.replaceAll("'", "&#39;");
				int link_end = content.indexOf("My_Custom&#39;);");
				if (link_end > -1) {
					content = AllUtil.link(content);
				}
				/**
				 * 互动动态数据处理 开始
				 */
				int i = content.indexOf("</form>");
				if (i > 0) {
					//String content=  "sdfsdf22222<form method=\"post\">33333111111ssdfsdf";
					int formsta = content.indexOf("<form");  //获取  method=\"post\">33333111111ssdfsdf
					String fosta = content.split("<form")[1];//获取  method=\"post\"
					String form = content.substring(formsta, formsta+fosta.indexOf(">")+6);  //获取form method=\"post\">
					content = form+content.replace(form, "");//转换成<form method=\"post\">sdfsdf2222233333111111ssdfsdf
					
					String form_end = content.substring(content.indexOf("</form>"), content.indexOf("</form>")+7);
					content = content.replace(form_end,"");
					
					content = content + form_end;
					content = content.replace(
							"</form>",
							"<input type='hidden' name='w' value='"
									+ CompressEncodeing.CompressNumber(
											pageid, 6)
									+ "${h}' />" +
									"</form>");
					
					
					content = content.replaceAll(
							"type=\"button\" value=\"确定\" name=\"inter\"",
							"value=\"确定\" type=\"submit\"");
					content = content.replaceAll(
							"type=\"button\" name=\"inter\" value=\"确定\"",
							"value=\"确定\" type=\"submit\"");
					content = content.replaceAll(
							"type=\"button\" value=\"确定\"",
							"value=\"确定\" type=\"submit\"");
					content = content
							.replaceAll("type=\"button\" name=\"inter\"",
									"type=\"submit\"");
				}
				
				List<String> li = new ArrayList<String>();
				/**
				 * 互动动态数据处理 结束
				 */
				if (content.indexOf("interaction") > -1) {
					content=content.replaceAll("interaction", "interaction&phone=<%=phone%>&taskId=<%=taskId%>&pageId=<%=pageId%>");
					if (content.indexOf("#P_") == -1 && content.indexOf("#p_") == -1){
						String info = "<%@include file=\"../../../ydwx/wap/wx_infomap.jsp\" %>";
						li.add(info);
					}
				}
				
				if (content.indexOf("<embed") > -1) {
					String smsgpath = bundle.getString("montnets.httpUrl"); // ###视频用。本机地址，用来被替换##生成文件地址

					content = AllUtil.video(content, smsgpath, iphORhe);
				}
				
				//去掉了多余的路径，txt文件上传时候用到，may 2013-08-28

				String picUrl = "<%=wxPath %>"+StaticValue.WX_MATER;
				// *************集群处理 may add******************************
				if(StaticValue.getISCLUSTER() ==1){
					picUrl=outurl+StaticValue.WX_MATER;

				}else{
					   String regEx2="src=\""+contextPath+"/"+StaticValue.WX_MATER;//如果出现/p_ydwx/file/wx/mater/ 情况，去掉 p_ydwx 
			           Pattern p2=Pattern.compile(regEx2);
			           Matcher m2=p2.matcher(content);
			           if(m2.find()){
			        	   content = content.replaceAll("src=\""+contextPath+"/"+StaticValue.WX_MATER, "src=\""+picUrl);
			           }else{
			        	   content = content.replaceAll(StaticValue.WX_MATER, picUrl);
			           }
			           
				}
				//如果是暂存的时候，对路径进行处理
//				if ("0".equals(status)) {
					if(content!=null&&content.indexOf("src=\"file/wx/mater")>-1){
						// *************集群处理 may add******************************
						if(StaticValue.getISCLUSTER() ==1){
							content=content.replaceAll("src=\"file/wx/mater", "src=\""+outurl+"/file/wx/mater");
						}else{
							content=content.replaceAll("src=\"file/wx/mater", "src=\"<%=wxPath %>/file/wx/mater");
						}
					}
//					}
				//IE9不支持相对路径，所以需要处理
				if(content!=null&&content.indexOf("href=\"file/wx/mater")>-1){
					// *************集群处理 may add******************************
					if(StaticValue.getISCLUSTER() ==1){
						content=content.replaceAll("href=\"file/wx/mater", "href=\""+outurl+"/file/wx/mater");
					}else{
						content=content.replaceAll("href=\"file/wx/mater", "href=\"<%=wxPath %>/file/wx/mater");
					}
				}
				//下载按钮的处理
				if(content!=null&&content.indexOf("src=\"ydwx/images/downfile.jpg\"")>-1){
						content=content.replaceAll("src=\"ydwx/images/downfile.jpg\"", "src=\"<%=wxPath %>ydwx/images/downfile.jpg\"");
				}
				//404图片也要做处理
					if(content!=null&&content.indexOf("href=\"ydwx/wap/404.jsp\"")>-1){
						content=content.replaceAll("href=\"ydwx/wap/404.jsp\"", "href=\"<%=wxPath %>ydwx/wap/404.jsp\"");
					}
				String cod = bundle.getString("montnets.wx.cod");
				li.add(cod);
				if (content!=null&&content.indexOf("#P_") > -1) {
					//content = content.replace("#p_", "#P_");
					String params = base.getParams();
					String[] paramsArray = params.split(",");
					//占位符
					String zwf = "";
					for(int j =0;j<paramsArray.length;j++){
						
						zwf = this.findStrInput(content, paramsArray[j]);
						if(zwf==null || zwf.length()==0){
							continue;
						}
						content = content.replace(zwf, "<%=infoMap.get("+paramsArray[j]+")==null?\"\":infoMap.get("+paramsArray[j]+") %>");
					}
					String info = "<%@include file=\"../../../ydwx/wap/wx_infomap.jsp\" %>";
					li.add(info);
				}

				String head = bundle.getString("montnets.wx.head");
				String end = bundle.getString("montnets.wx.end");
				//由于村的文件除了下载文件按钮以外的连接都多余的加了ip+项目名，所以要去掉（暂存状态时候）
				if("0".equals(status)||"copy".equals(status)){
					//处理下载（图片）对应的，多余的地址,下载按钮的图片 不做处理
//					   String regEx1="href=\"(.*?)<%=wxPath %>/file/wx/mater";
//			           Pattern p1=Pattern.compile(regEx1);
//			           Matcher m1=p1.matcher(content);
//			           if(m1.find()){
//			        	   content =  content.replaceAll(regEx1,"href=\"<%=wxPath %>/file/wx/mater");
//			           }
					if(StaticValue.getISCLUSTER() ==1){
				           //处理其他图片的情况
						   String regEx2=replaceSrc+outurl;
				           Pattern p2=Pattern.compile(regEx2);
				           Matcher m2=p2.matcher(content);
				           if(m2.find()&& content!=null){
				        	   content =  content.replaceAll(regEx2,outurl);
				           }
					}else{
			           //处理其他图片的情况
					   String regEx2=replaceSrc+"<%=wxPath %>";
			           Pattern p2=Pattern.compile(regEx2);
			           Matcher m2=p2.matcher(content);
			           if(m2.find()&& content!=null){
			        	   content =  content.replaceAll(regEx2,"<%=wxPath %>");
			           }
					}
					
				}
				
				li.add(head);
				li.add(content);
				li.add(end);
				
				FileJsp.sortAndSave(pageid + "", li);
				String filename = "wx_" + pageid + ".jsp";
				CommonBiz com=new CommonBiz();
				if(StaticValue.getISCLUSTER() ==1){
//					boolean isFTPUpload=com.upFileToFileServer(StaticValue.WX_PAGE +"/"+ filename);
					
					String result=new CommonBiz().uploadFileToFileCenter(StaticValue.WX_PAGE +"/"+filename);
//					String result=com.uploadOneFileWhitZip(StaticValue.WX_PAGE +"/"+filename, "1");
//					com.uploadFileWhitZip(StaticValue.WX_PAGE +"/", filearr);
//					if(!isFTPUpload){
//						msg="由于采用集群，远程上传文件失败！";
//					}
					if("error".equals(result)){
						EmpExecutionContext.error("由于采用集群，远程上传文件失败！");
					}
				}
			}
		}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"生成网讯预览页面");
		} 
	}

	/**
	 * 查找替换内容
	 * @param content
	 * @param paramNo
	 * @return
	 */
	private String findStrInput(String content, String paramNo){
		try{
			//查找内容是否有该序号的参数
			int strMark = content.indexOf("#P_"+paramNo+"#");
			if(strMark == -1){
				return "";
			}
			
			//以typeend="#P_1#"为中心，向前和向后找，把要替换的内容先大概找出来
			String typeend = "typeend=\"#P_"+paramNo+"#\"";
			int curIndex = content.indexOf(typeend);
			if(curIndex == -1){
				return "";
			}
			//先获取中心词前面200个字符的位置，如果超过，则直接从0开始
			int begi = (curIndex-200)<0?0:(curIndex-200);
			//前面一节字符
			String strFront = content.substring(begi, curIndex);
			//获取中心词后面10个字符的位置，如果超过，则直接取内容最后的位置
			int endi = (curIndex + typeend.length() +10)>content.length()?content.length():(curIndex + typeend.length() +10);
			//后面一节字符
			String strBehind = content.substring(curIndex, endi);
			//拼成整个字符
			String strAll = strFront + strBehind;
			
			//截取真正要替换的内容
			int begin = strAll.indexOf("<input");
			int end = strAll.lastIndexOf("/>")+2;
			String replaceStr = strAll.substring(begin, end);
			return replaceStr;
		}catch(Exception e){
			EmpExecutionContext.error(e,"查找替换内容");
			return null;
		}
		
	}
	
	public static String getSql(String a) {

		String sql = "<sql:query dataSource=\"${db}\" var=\""
				+ a
				+ "\" sql=\"select * from "
				+ a
				+ " where C0_SHOUJIHAOMA='${ph}'\"  scope='page' startRow='0'  maxRows='1'></sql:query>";

		return sql;
	}

	// 递归替换
	public static StringBuffer geta(StringBuffer sb) {

		if (sb.indexOf("@{") > -1) {
			String[] t = sb.toString().split("@");

			int ct = t[0].lastIndexOf("<input");// 找到位置
			int cts = sb.indexOf("@{");

			sb.delete(ct, cts);

			sb.insert(ct, "<b style=\"color:blue;\">");
			t = sb.toString().split("@");

			ct = t[1].indexOf("}");

			cts = t[1].indexOf("/>");
			ct += t[0].length();
			cts += t[0].length();
			sb.delete(ct + 2, cts + 3);
			sb.insert(ct + 2, "</b>");
			int num = sb.indexOf("@");
			sb.delete(num, (num + 1));
			sb.insert(num, "$");

			geta(sb);

		}
		return sb;
	}

	// 递归替换
	public static StringBuffer getb(StringBuffer sb) {

		if (sb.indexOf("@{") > -1) {
			String[] t = sb.toString().split("@");

			int ct = t[0].lastIndexOf("<input");// 找到位置
			int cts = sb.indexOf("@{");

			sb.delete(ct, cts);

			t = sb.toString().split("@");

			ct = t[1].indexOf("{");

			cts = t[1].indexOf("/>");
			ct += t[0].length();
			cts += t[0].length();
			sb.delete(ct + 1, cts + 3);

			int num = sb.indexOf("@");
			sb.delete(num, (num + 1));

			getb(sb);

		}
		return sb;
	}

}
