/**
 * Program  : app_morecordselectSvt.java
 * Author   : zousy
 * Create   : 2014-6-12 上午11:38:16
 * company ShenZhen Montnets Technology CO.,LTD.
 *
 */

package com.montnets.emp.appmage.svt;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.tools.SysuserUtil;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.util.StringUtil;
import org.dom4j.DocumentException;
import org.json.JSONException;
import org.json.JSONObject;

import com.montnets.emp.appmage.biz.app_morequeryBiz;
import com.montnets.emp.appmage.util.FFmpegKit;
import com.montnets.emp.appwg.biz.WgMwFileBiz;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.appmage.LfAppTcMomsg;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.util.DownloadFile;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.TxtFileUtil;

/**
 * APP上行记录
 * @author   zousy <zousy999@qq.com>
 * @version  1.0.0
 * @2014-6-12 上午11:38:16
 */
public class app_morecordselectSvt extends BaseServlet
{
	private static final String empRoot="appmage";
	private static final String base="/morequery";
	private static final app_morequeryBiz morequeryBiz = new app_morequeryBiz();
	private static final BaseBiz baseBiz = new BaseBiz();
	
	public LinkedHashMap<String, String> getCondition(HttpServletRequest request,String[] cons){
		boolean isGet = request.getMethod().equalsIgnoreCase("get");
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try
		{
			for(String key:cons){
				String value = request.getParameter(key);
				if(isGet&&StringUtils.isNotBlank(value)){
					value = new String(value.getBytes("iso8859-1"),"UTF-8");
				}
				if(StringUtils.isNotBlank(value)){
					conditionMap.put(key, value);
				}
			}
		}
		catch (UnsupportedEncodingException e)
		{
			EmpExecutionContext.error(e, "get请求获取参数时编码异常！");
		}
		return conditionMap;
	}
	/**
	 * APP上行记录查询
	 * @Title: find
	 * @Description: TODO
	 * @param  
	 * @return void    返回类型
	 * @throws
	 */
	public void find(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		//查询开始时间
		long stratTime = System.currentTimeMillis();
		PageInfo pageInfo = new PageInfo();
		boolean isFirstEnter = pageSet(pageInfo,request);
		request.setAttribute("isFirstEnter", isFirstEnter);
		String skip=request.getParameter("skip");
		LinkedHashMap<String, String> conditionMap=null;
		try {
			if(!isFirstEnter){
						conditionMap = getCondition(request,
						new String[]{"fromuser","fromname","msgtype","msgtext","starttime","endtime"});
				if("true".equals(skip)){
					pageInfo=(PageInfo)request.getSession(false).getAttribute("more_pageInfo");
					conditionMap=(LinkedHashMap<String, String>)request.getSession(false).getAttribute("more_conditionMap");
				}
				List list = morequeryBiz.getMoList(conditionMap,pageInfo);
				request.setAttribute("list", list);
				if(!"true".equals(skip)&&!"1".equals(skip)){
					request.getSession(false).setAttribute("more_pageInfo",pageInfo);
					request.getSession(false).setAttribute("more_conditionMap",conditionMap);
				}
			}
			
			//当前登录用户信息
			LfSysuser sysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			String corpCode = null;
			String lguserid = null;
			String lguserName = null;
			if(sysuser != null)
			{
				corpCode = (sysuser.getCorpCode() != null && !"".equals(sysuser.getCorpCode()))?sysuser.getCorpCode():null;
				lguserid = String.valueOf(sysuser.getUserId() != null?sysuser.getUserId():null);
				lguserName = (sysuser.getUserName() != null && !"".equals(sysuser.getUserName()))?sysuser.getUserName():null;
			}
			//查询出的数据的总数量
			int totalCount = pageInfo.getTotalRec();
			//日志信息
			String opContent = "开始时间：" + sdf.format(stratTime) + " 耗时："+
			(System.currentTimeMillis()-stratTime) + "ms  数量：" + totalCount;
			
			EmpExecutionContext.info("APP上行记录查询", corpCode, lguserid, lguserName, opContent, "GET");
			
		} catch (Exception e) {
			EmpExecutionContext.error(e,"APP上行记录查询异常！");
			request.setAttribute("findresult", "-1");
		}finally{
			request.setAttribute("conditionMap", conditionMap);
			request.setAttribute("skip", skip);
			request.setAttribute("pageInfo", pageInfo);
			request.getRequestDispatcher(this.empRoot+base+"/app_morequery.jsp").forward(request,
					response); 
		}
	}
	/**
	 * APP上行记录导出
	 * @Title: exportData
	 * @Description: TODO
	 * @param  
	 * @return void    返回类型
	 * @throws
	 */
	public void exportData(HttpServletRequest request,HttpServletResponse response) {
	LinkedHashMap<String, String> conditionMap = getCondition(request,
				new String[]{"fromuser","fromname","msgtype","msgtext","starttime","endtime"});
	SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
	//导出开始时间
	long stratTime = System.currentTimeMillis();
	List list = null;
	try
	{
		list = morequeryBiz.getMoList(conditionMap,null);
	}
	catch (Exception e)
	{
		EmpExecutionContext.error(e, "APP上行记录导出查询异常！");
	}
		OutputStream o = null;
    try
	{
		//OutputStream o = response.getOutputStream();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddH24mmss");
		String dataString = sdf.format(stratTime);
		String filename = "app_moRecordDetail_" + dataString+".zip";
		String voucherFilePath =new TxtFileUtil().getWebRoot()+this.empRoot+base+"/file/download/app_moRecordDetail"+File.separator;
		File file = new File(voucherFilePath);
		file.mkdirs();
		o = new FileOutputStream(voucherFilePath+filename);

//		response.setHeader("Content-disposition", "attachment;filename="
//		      + filename);
//		response.setContentType("text/html");
		//生成临时文件的路径
		String filepath = request.getRealPath(this.empRoot+base+"/file/temp")+File.separator+"app_moRecordDetail_" + dataString+".xls";
		writeReportExcel(filepath,list, o, request);
		 // 增加操作日志
		Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
		if (loginSysuserObj != null) {
			LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;
			String opContent = "导出成功,开始时间："+ format.format(stratTime)+ 
			" 耗时：" + (System.currentTimeMillis()-stratTime) + "ms"+"数量：" + (list !=null? list.size():0);
			EmpExecutionContext.info("APP上行记录查询(导出)", loginSysuser.getCorpCode(),
					loginSysuser.getUserId().toString(), loginSysuser
							.getUserName(), opContent, "OTHER");
		}	
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("FILE_NAME", filename);
		resultMap.put("FILE_PATH", voucherFilePath+filename);
		request.getSession(false).setAttribute("app_morecordselectMap", resultMap);
		
		PrintWriter out = response.getWriter();
		out.println("true");
	}
	catch (Exception e)
	{
		EmpExecutionContext.error(e, "APP上行记录导出异常！");
	}finally {
		SysuserUtil.closeStream(o);
	}
    }
	
	/**
	 * APP上行记录查询上行内容详情异步请求处理
	 * @Title: prev
	 * @Description: TODO
	 * @param  
	 * @return void    返回类型
	 * @throws
	 */
	public void prev(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException, JSONException{
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		//String lgcorpcode = request.getParameter("lgcorpcode");
		String msgid = request.getParameter("msgid");
		LfAppTcMomsg msg = null;
		String basePath = this.getServletContext().getRealPath("/");
		if(StringUtils.isNotBlank(msgid)&&StringUtils.isNumeric(msgid)){
			try
			{
				msg = baseBiz.getById(LfAppTcMomsg.class, msgid);
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "查询对应上行记录异常！");
			}
		}
		JSONObject json = new JSONObject();
		try
		{
			if(msg != null){
				JSONObject obj = app_morequeryBiz.getJson(msg.getMsgtype(), msg.getMsgJson());
				if(obj != null){
					obj.put("msgtype",msg.getMsgtype());
					json.put("errcode", 0);
					
					//资源文件从文件服务器下载到本地服务器
					if(msg.getMsgtype()>0){
						if(obj.has("pic")){
							String pic = morequeryBiz.downloadIfNotExist(basePath,obj.getString("pic"));
							obj.put("pic",pic);
						}
						if(obj.has("url")){
							String url = morequeryBiz.downloadIfNotExist(basePath,obj.getString("url"),msg.getMsgtype());
							obj.put("url", request.getContextPath()+"/"+FFmpegKit.convertPath(url));
						}
					}
					//对文本做特殊处理 里面可能存在表情字符
					if(msg.getMsgtype() == 0){
						//对文本进行处理
						String text = msg.getMsgtext()==null?"":msg.getMsgtext();
						text = StringEscapeUtils.escapeHtml(text);
						Map<String,String> emos = app_morequeryBiz.getEmoMap(basePath);
						Iterator<String> its = emos.keySet().iterator();
						text =text.replaceAll(" ", "&nbsp;");//优先处理空格转换 以免与表情冲突
						while(its.hasNext()){
							String key = its.next();
							text = text.replaceAll(key, "<img class=\"emo\" src=\""+this.empRoot+base+"/emo/"+emos.get(key)+"\" />");
						}
						text = text.replaceAll("\\n", "<br/>");
						json.put("text", text);
					}
					json.put("msg", obj);
				}else{
					json.put("errcode", -1);
					json.put("errmsg", "解析数据异常！");
				}
			}else{
				json.put("errcode", -1);
				json.put("errmsg", "未找到相应记录！");
			}
		}
		catch (Exception e)
		{
			json.put("errcode", -1);
			json.put("errmsg", "处理数据出现错误！");
			EmpExecutionContext.error(e,"解析数据异常！");
		}finally{
			out.print(json.toString());
		}
		
	}
	/**
	 * APP上行记录导出写文件
	 * @Title: writeReportExcel
	 * @Description: TODO
	 * @param  
	 * @return void    返回类型
	 * @throws
	 */
	public void writeReportExcel(String tempFilePath,List list,OutputStream o, HttpServletRequest request) throws IOException, WriteException{
		if(list ==null || list.size()==0){
			return;
		}
		int intRowsOfPage = 50000;
		File tempFile = new File(tempFilePath);
		if(!tempFile.getParentFile().exists()){
			tempFile.getParentFile().mkdirs();
		}
		WritableWorkbook wwb = Workbook.createWorkbook(tempFile);
		WritableSheet ws = null;
		WritableCellFormat format = new WritableCellFormat();
		format.setWrap(true);
		format.setVerticalAlignment(VerticalAlignment.CENTRE);
		format.setAlignment(Alignment.CENTRE);
		
		WritableCellFormat titleFmt = new WritableCellFormat(new WritableFont(WritableFont.TIMES, 12,WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK));
		titleFmt.setAlignment(Alignment.CENTRE);
		Map<String,String> typeMap = getTypeMap(request);
		for(int i=0;i<list.size();i++){
			if(i%intRowsOfPage==0){
				
				ws = wwb.createSheet("app_moRecordDetail"+(i/intRowsOfPage+1), i/intRowsOfPage); 
				ws.addCell(new Label(0,0,MessageUtils.extractMessage("appmage", "appmage_xxfb_appsxjl_text_appuser", request),titleFmt));
				ws.addCell(new Label(1,0,MessageUtils.extractMessage("appmage", "appmage_xxfb_appsxjl_text_messagetype", request),titleFmt));
				ws.addCell(new Label(2,0,MessageUtils.extractMessage("appmage", "appmage_xxfb_appsxjl_text_mocontent", request),titleFmt));
				ws.addCell(new Label(3,0,MessageUtils.extractMessage("appmage", "appmage_xxfb_appsxjl_text_motime", request),titleFmt));
				ws.setColumnView(0, 25);
				ws.setColumnView(1, 13);
				ws.setColumnView(2, 42);
				ws.setColumnView(3, 20);
			}
			DynaBean item = (DynaBean) list.get(i);
			String fromname = item.get("fromname")==null?"-":String.valueOf(item.get("fromname"));
			String fromuser = item.get("fromuser")==null?"-":String.valueOf(item.get("fromuser"));
			String msgtype = item.get("msg_type")==null?"0":String.valueOf(item.get("msg_type"));
			String msgtext = item.get("msg_text")==null?"":String.valueOf(item.get("msg_text"));
			if(msgtext.length()==0){msgtext = typeMap.get(msgtype);}
			String createtime =  item.get("createtime")==null?"-":String.valueOf(item.get("createtime")).substring(0,19);
			ws.addCell(new Label(0,(i%intRowsOfPage+1),fromname+"("+fromuser+")",format));
			ws.addCell(new Label(1,(i%intRowsOfPage+1),typeMap.get(msgtype),format));
			ws.addCell(new Label(2,(i%intRowsOfPage+1),msgtext,format));
			ws.addCell(new Label(3,(i%intRowsOfPage+1),createtime,format));
		}
		if(list.size()>0){
			//写入到临时文件
			wwb.write(); 
		}
		//关闭Excel工作薄对象 
		wwb.close();
		BufferedInputStream bis = null;
		ZipOutputStream out = null;
		try {
			bis = new BufferedInputStream(
					new FileInputStream(tempFile));
			ZipEntry entry = new ZipEntry(tempFile.getName());
			out = new ZipOutputStream(o);
			out.putNextEntry(entry);
			int count;
			byte data[] = new byte[1024*256];
			while ((count = bis.read(data, 0, 1024)) != -1) {
				out.write(data, 0, count);
			}
			out.finish();
		}finally {
			SysuserUtil.closeStream(bis);
			SysuserUtil.closeStream(out);
		}
		//删除临时文件
        if(tempFile.exists()){
        	boolean state = tempFile.delete();
        	if(!state){
				EmpExecutionContext.error("删除失败!");
			}
        }
	}
	
	public Map<String, String> getTypeMap(HttpServletRequest request){
		Map<String,String> typeMap = new HashMap<String,String>();
		typeMap.put("0",MessageUtils.extractMessage("appmage", "appmage_javacode_text_9", request));
		typeMap.put("1",MessageUtils.extractMessage("appmage", "appmage_javacode_text_10", request));
		typeMap.put("2",MessageUtils.extractMessage("appmage", "appmage_javacode_text_11", request));
		typeMap.put("3",MessageUtils.extractMessage("appmage", "appmage_javacode_text_12", request));
		return typeMap;
	}
	
	
	/**
	 * APP上行记录查询导出
	 * @Title: downloadFile
	 * @Description: TODO
	 * @param  
	 * @return void    返回类型
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public void downloadFile(HttpServletRequest request, HttpServletResponse response) throws Exception{
		try {
			Object resultMapObj = request.getSession(false).getAttribute("app_morecordselectMap");
			if(resultMapObj != null)
			{
				Map<String, String> resultMap = (Map<String, String>) resultMapObj;
				//文件名
				String fileName=(String)resultMap.get("FILE_NAME");
				//文件路径
			    String filePath=(String)resultMap.get("FILE_PATH");
				
			    DownloadFile dfs=new DownloadFile();
			    dfs.downFile(request, response, filePath, fileName);
			    request.getSession(false).removeAttribute("app_morecordselectMap");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			EmpExecutionContext.error(e,"app上行记录查询导出异常!");
		}

	}
	
	
}

