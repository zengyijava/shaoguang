package com.montnets.emp.netnews.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IDataAccessDriver;
import com.montnets.emp.common.dao.IEmpDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.netnews.biz.WxManagerBiz;
import com.montnets.emp.netnews.entity.LfWxVisitLog;
import com.montnets.emp.util.TxtFileUtil;

public class FileJsp
{

	private static ResourceBundle bundle = ResourceBundle
			.getBundle("resourceBundle");

	private static String uploadpath = bundle.getString("montnets.wx.newjsp");

	private static int maxVISTIS = Integer.parseInt(bundle
			.getString("montnets.wx.maxVistis"));

	private static MemCached cache = MemCached.getInstance();
	
	private static IDataAccessDriver dataAccessDriver=new DataAccessDriver();
	private static IEmpDAO empDao = dataAccessDriver.getEmpDAO();

	private static int statc;
	private static String page;
	private static WxManagerBiz wxManagerBiz = new WxManagerBiz();
	
	public static boolean sortAndSave(String pageid, List data)
	{
		// destFile="D:/workspace/montnets_emp/WebRoot/website/standard/nl/ueditor/server/uploadfiles/text.txt";
		String filename = "wx_" + pageid + ".jsp";
		uploadpath = com.montnets.emp.common.constant.StaticValue.WX_PAGE;
		uploadpath = new TxtFileUtil().getWebRoot() + uploadpath + "/";
		String basePath = uploadpath + filename;
		FileOutputStream fs = null;
		OutputStreamWriter ow = null;
		BufferedWriter writer = null;
		try
		{
			// writer = new BufferedWriter(new FileWriter(basePath));
			fs = new FileOutputStream(basePath);// 实例了文件输出流，参数是文件输出路径
			ow = new OutputStreamWriter(fs, "utf-8");// 在写输出流的时候做编码格式转化，以免乱码！
			writer = new BufferedWriter(ow);// 将文本写入字符输出流，缓冲各个字符，从而提供单个字符、数组和字符串的高效写入

			for (int i = 0; i < data.size(); i++)
			{
				writer.write(data.get(i).toString());
				writer.newLine();
			}

			writer.flush();
			return true;
		} catch (IOException e)
		{
			EmpExecutionContext.error(e,"写文件数据");
			return false;
		}finally{
			
			if(writer!=null){
				try {
					writer.close();
				} catch (IOException e) {
					EmpExecutionContext.error(e,"写文件数据,文件流关闭");
				}
			}
			if(ow!=null){
				try {
					ow.close();
				} catch (IOException e) {
					EmpExecutionContext.error(e,"写文件数据,文件流关闭");
				}
			}
			if(fs!=null){
				try {
					fs.close();
				} catch (IOException e) {
					EmpExecutionContext.error(e,"写文件数据,文件流关闭");
				}
			}
		}
	}

	
	//获得内容
	public static String getContent(String pageid){
		String content="";
		String filename = "wx_" + pageid + "_content.jsp";
		uploadpath = com.montnets.emp.common.constant.StaticValue.WX_PAGE;
		uploadpath = new TxtFileUtil().getWebRoot() + uploadpath + "/";
		String basePath = uploadpath + filename;
		StringBuffer st=new StringBuffer();
		BufferedReader reader = null;
		try {
			File file = new File(basePath);
			if(file != null && file.exists()){
				//不做处理
			}else {
				//分布式 ：如果存在就下载该文件到本地服务器上
				if(com.montnets.emp.common.constant.StaticValue.getISCLUSTER() ==1){
						//获取JSP相对路径
						CommonBiz biz=new CommonBiz();
						biz.downloadFileFromFileCenter("file/wx/PAGE/"+filename);
					}
			}
			
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(basePath)), "utf-8"));
			String tempString="";
			try {
				while((tempString = reader.readLine()) != null){
					st.append(tempString);
				}
			} catch (IOException e) {
				EmpExecutionContext.error(e,"读取文件时候IO异常");
			}
			content=st.toString();
			return content;
		} catch (UnsupportedEncodingException e) {
			EmpExecutionContext.error(e,"不支持该文件编码格式："+filename);
			content=st.toString();
			return content;
		} catch (FileNotFoundException e) {
			EmpExecutionContext.error(e,"该文件找不到："+filename);
			content=st.toString();
			return content;
		}finally{
			if(reader!=null){
				try {
					reader.close();
				} catch (IOException e) {
					EmpExecutionContext.error(e,"文件流关闭异常："+filename);
				}
			}
		}
		
		
	}
	
	/***
	 * 
	 * @param pageid
	 * @param data
	 * @return
	 */
	public  static boolean saveContent(String pageid, String content)
	{

		// destFile="D:/workspace/montnets_emp/WebRoot/website/standard/nl/ueditor/server/uploadfiles/text.txt";
		String filename = "wx_" + pageid + "_content.jsp";
		uploadpath = com.montnets.emp.common.constant.StaticValue.WX_PAGE;
		uploadpath = new TxtFileUtil().getWebRoot() + uploadpath + "/";
		statc= com.montnets.emp.common.constant.StaticValue.getISCLUSTER();
		String basePath = uploadpath + filename;
		page=com.montnets.emp.common.constant.StaticValue.WX_PAGE;
		FileOutputStream fs =null;
		OutputStreamWriter ow =null;
		BufferedWriter writer = null;
		try
		{
			// writer = new BufferedWriter(new FileWriter(basePath));
			fs = new FileOutputStream(basePath);// 实例了文件输出流，参数是文件输出路径
			ow = new OutputStreamWriter(fs, "utf-8");// 在写输出流的时候做编码格式转化，以免乱码！
			writer = new BufferedWriter(ow);// 将文本写入字符输出流，缓冲各个字符，从而提供单个字符、数组和字符串的高效写入

			writer.write(content);
			writer.newLine();
			writer.flush();
			writer.close();
			
//			if(statc==1){
//				CommonBiz com=new CommonBiz();
//				String result=com.uploadFileToFileCenter(page +"/"+filename);
//				if("error".equals(result)){
//					EmpExecutionContext.error("由于采用集群，远程上传文件失败！");
//				}
//			}
			
			return true;
		} catch (IOException e)
		{
			EmpExecutionContext.error(e,"写文件数据");
			return false;
		}finally{
			if(writer != null){
				try {
					writer.close();
				} catch (IOException e) {
					EmpExecutionContext.error(e,"写文件数据,关闭文件流异常");
				}
			}
			if(ow != null){
				try {
					ow.close();
				} catch (IOException e) {
					EmpExecutionContext.error(e,"写文件数据,关闭文件流异常");
				}
			}
			if(fs != null){
				try {
					fs.close();
				} catch (IOException e) {
					EmpExecutionContext.error(e,"写文件数据,关闭文件流异常");
				}
			}
		}
		
	}
	
	
	
	// private static List logs = new ArrayList(); //网讯访问日志记录

	// private static HashMap ndtimeMap = new HashMap();//网讯id 有效时间
	// private static HashMap ndpdMap = new HashMap(); //网讯id 页面ID集合(12,13,)
	// private static Date da = new Date(); //保存访问日志记录存库时间，

	public synchronized static void inListLogs(LfWxVisitLog visi)
	{
		try
		{
			List<LfWxVisitLog> visitLogsList = null;
			if (cache.get(StaticValue.SMS_NETMSG_VISIT_LOG) != null)
			{
				visitLogsList = (List<LfWxVisitLog>) cache.get(StaticValue.SMS_NETMSG_VISIT_LOG);
			} else
			{
				visitLogsList = new ArrayList<LfWxVisitLog>();
				cache.add(StaticValue.SMS_NETMSG_VISIT_LOG, visitLogsList);
			}
			visitLogsList.add(visi);
			cache.replace(StaticValue.SMS_NETMSG_VISIT_LOG, visitLogsList);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"保存访问日志");
		}
	}

	public static void main(String[] sa)
	{

	}

	// private static Map<String,String> phoneMap = new
	// HashMap<String,String>(); //键：手机号 值 :pageid,访问次数;pageid,访问次数;
	/**
	 * 用来判断同一手机号对同一页面，一天能访问多少次 如果超出最大次数(maxVISTIS)，则返回false
	 * 如果没有超出最大次数(maxVISTIS)，则返回true 最大次数通过读取配置文件
	 * 
	 * @param phone
	 *            手机号
	 * @param pageid
	 *            页面ID
	 * @return
	 */
	public static boolean phoneVisitMaxCount(String phone, String pageid)
	{
		boolean boo = false;
		try
		{
			String sDate = null;
			HashMap<String, String> phoneMap = null;
			if (cache.get(StaticValue.SMS_PHONE_VISIT_LIST) == null)
			{
				cache.replace(StaticValue.SMS_PHONE_VISIT_LIST,
						new HashMap<String, String>());
			}

			phoneMap = (HashMap) cache.get(StaticValue.SMS_PHONE_VISIT_LIST);

			if (cache.get(StaticValue.SMS_DATA_MAX__COUNT) == null)
			{
				cache.add(StaticValue.SMS_DATA_MAX__COUNT, AllUtil
						.getCurrDate()
						+ " 00:00:00");
			}
			sDate = (String) cache.get(StaticValue.SMS_DATA_MAX__COUNT);
			Date d1 = new Date();
			if (sDate != null)
				d1 = AllUtil.getStringtoda(sDate); // 得到更新phoneMap日期
			boolean boc = d1.before(AllUtil.Stringtodate(AllUtil
					.datetoString(new Date()))); // 如果有效时间大于当前日期，返回FALSE
			// 如果是true，则清空phoneMap集合，给sDate附新的值
			if (boc)
			{
				// 得到明天的时间
				cache.replace(StaticValue.SMS_DATA_MAX__COUNT, AllUtil
						.getdatoString(addDayDate())
						+ " 00:00:00");

				cache.replace(StaticValue.SMS_PHONE_VISIT_LIST,
						new HashMap<String, String>());

			}

			if (phoneMap != null && phoneMap.size() > 0)
			{
				// 判断手机是否存在Mmp中，如果不存在，则保存到Map。如果存在，则找出pagdid是否存在
				String value = phoneMap.get(phone);
				if (value != null && !"".equals(value))
				{
					String tmp = "";
					String newValue = "";
					if (value.indexOf(pageid) > -1)
					{
						String[] pdNum = value.split(";");
						if (pdNum != null && pdNum.length > 0)
						{
							for (int i = 0; i < pdNum.length; i++)
							{
								if (pdNum[i].indexOf(pageid) > -1)
								{
									tmp = pdNum[i];
									int max = Integer
											.parseInt(tmp.split(",")[1]);
									if (max > maxVISTIS)
									{ // 如果次数大于最大次数
//										System.out.println(phone + "手机号"
//												+ pageid + "页面次数：" + max
//												+ ",true");
										boo = true;
										newValue = pageid + "," + max;
										break;
									} else
									{
										max = max + 1;
										newValue = pageid + "," + max;
//										System.out.println(phone + "手机号"
//												+ pageid + "页面次数：" + max
//												+ ",false");
										break;
									}
								}
							}
						}
						value = value.replaceAll(tmp, newValue); // 保存新的记数
						phoneMap.put(phone, value);
						cache.replace(StaticValue.SMS_PHONE_VISIT_LIST,
								phoneMap);

					} else
					{
						value = value + pageid + "," + 1 + ";";
						phoneMap.put(phone, value);
						cache.replace(StaticValue.SMS_PHONE_VISIT_LIST,
								phoneMap);
					}

				} else
				{
					phoneMap.put(phone, pageid + "," + 1 + ";");
					cache.replace(StaticValue.SMS_PHONE_VISIT_LIST, phoneMap);
				}
			} else
			{

				phoneMap = new HashMap<String, String>();
				phoneMap.put(phone, pageid + "," + 1 + ";");
				cache.replace(StaticValue.SMS_PHONE_VISIT_LIST, phoneMap);
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error("得到最大访问限制错误:" + e);
		}
		return boo;
	}

	/**
	 * 注意wait的用法，必须在loop中，必须在拥有锁的代码块中。 前者是当被notify的时候要重新进行条件判断，后者是为了释放锁。
	 * 定时调用方法，如果缓存记录多于20条则保存到数据库
	 * 
	 * @param name
	 * @param num
	 */
	public static void mainTimer(boolean boo)
	{
		limer(boo);
	}

	public static void limer(boolean boo)
	{
		try
		{
			if (cache.get(StaticValue.SMS_TIMING_DATE) == null)
			{
				cache.add(StaticValue.SMS_TIMING_DATE, new Date());
			}
			Date da = (Date) cache.get(StaticValue.SMS_TIMING_DATE);
			if (da == null)
			{
				da = new Date();
				cache.add(StaticValue.SMS_TIMING_DATE, da);
			}
			
			List<LfWxVisitLog> visitLogsList = (List<LfWxVisitLog>) cache.get(StaticValue.SMS_NETMSG_VISIT_LOG);
			
			if (visitLogsList != null && visitLogsList.size() > 0)
			{
				//原代码这里每次只保存20条记录
				int result = empDao.save(visitLogsList, LfWxVisitLog.class);
				// 保存当前插入时间
				cache.replace(StaticValue.SMS_TIMING_DATE, addDate());
				
				if(result > 0)
				{
					visitLogsList.clear();
					cache.replace(StaticValue.SMS_NETMSG_VISIT_LOG, visitLogsList);
				}
				
			}
			
		} 
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"定时调用方法");
		}
	}

	/**
	 * 如果保存时间加5分钟大于当前时间，保存日志访问记录，同时重新把当前时间加5分钟
	 * 
	 * @return 当前时间加5分钟的时间
	 */
	public static Date addDate()
	{
		SimpleDateFormat sdf = AllUtil.getFormatDateTime();
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(cal.SECOND, 300); // 当前时间加5分钟
		Date dd = cal.getTime();
		return dd;

	}

	/**
	 * 如果保存时间加1天大于当前时间，保存日志访问记录，同时重新把当前时间加1天
	 * 
	 * @return 当前时间加1天的时间
	 */
	public static Date addDayDate()
	{
		SimpleDateFormat sdf = AllUtil.getFormatDateTime();
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(cal.DATE, 1); // 当前时间加5分钟
		Date dd = cal.getTime();
		return dd;

	}

	/**
	 * 判断网讯是否过期
	 * 
	 * @param key
	 *            页面ID
	 * @return
	 */
	public synchronized static boolean isBeOverdue(int key)
	{
		// 循环查找页面ID是否存在
		boolean boo = false;
		try
		{

			HashMap<Long, String> ndpdMap = (HashMap<Long, String>) cache
					.get(StaticValue.SMS_NETMSG_NET_PGEE);
			
			if (ndpdMap != null && ndpdMap.size() > 0)
			{
				Set<Long> set = ndpdMap.keySet();
				boolean ne = false;
				Long netid = 0l;
				Iterator<Long> iterator = set.iterator();
				while (iterator.hasNext())
				{
					netid = iterator.next();
					if ((ndpdMap.get(netid).toString().indexOf(key + ",") > -1))
					{ // 如果存在，则获取有效时间
						ne = true;
						break;
					}
				}
				if (ne)
				{
					String strdate = (String) cache.get(netid + "");
					Date date = AllUtil.Stringtodate(strdate);
					boo = date.before(new Date()); // 如果有效时间大于当前日期，则没有过期，返回FALSE

				} 
				else
				{ 
					// 如果不存在，则查询数据库，找出有效时间，同时把相关信息保存到集合里
					String strdate = wxManagerBiz.getPdByTimeOut(key);
					if(strdate != null && !"".equals(strdate.trim()))
					{
						Date date = AllUtil.Stringtodate(strdate);
						// 如果有效时间大于当前日期，则没有过期，返回FALSE
						boo = date.before(new Date());
					}
					else
					{
						boo = true;
					}
					
				}
			}
			else
			{
				// ndpdMap不存在，相当初始化
				String strdate = wxManagerBiz.getPdByTimeOut(key);
				if(strdate != null && !"".equals(strdate.trim()))
				{
					Date date = AllUtil.Stringtodate(strdate);
					// 如果有效时间大于当前日期，则没有过期，返回FALSE
					boo = date.before(new Date());
				}
				else
				{
					boo = true;
				}
			}
		} 
		catch (Exception e)
		{
			EmpExecutionContext.error("判断网讯是否过期出错:" + e);
		}
		return boo;
	}

	/**
	 * 保存 网讯id 和 有效时间 键值对
	 * 
	 * @param key
	 * @return
	 */
	public synchronized static void savendtimeMap(Long key, String date)
	{
		if (cache.get(key + "") == null)
			cache.add(key + "", date);
		else
			cache.replace(key + "", date);
	}

	/**
	 * 保存 网讯id 页面ID集合(12,13,) 键值对,如果网讯ID存在，则添加到键值对
	 * 
	 * @param key
	 * @return
	 */
	public synchronized static void savendpdMap(Long key, String pageid)
	{
		try
		{
			HashMap<Long, String> ndpdMap = (HashMap) cache.get(StaticValue.SMS_NETMSG_NET_PGEE);
			if (ndpdMap != null)
			{
				if (ndpdMap.get(key) != null)
				{
					String pdList = (String) ndpdMap.get(key);
					if (pdList.indexOf(pageid + ",") < 0)
					{
						pdList = pageid + "," + pdList;

						ndpdMap.put(key, pdList);

						cache.replace(StaticValue.SMS_NETMSG_NET_PGEE, ndpdMap);
					}

				} else
				{
					ndpdMap.put(key, pageid + ",");

					cache.replace(StaticValue.SMS_NETMSG_NET_PGEE, ndpdMap);
				}
			} else
			{
				ndpdMap = new HashMap<Long, String>();
				cache.add(StaticValue.SMS_NETMSG_NET_PGEE, ndpdMap);
			}

		} catch (Exception e)
		{
			EmpExecutionContext.error("保存 网讯id出错:" + e);
		}
	}

}
