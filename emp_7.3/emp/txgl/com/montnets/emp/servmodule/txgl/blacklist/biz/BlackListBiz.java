package com.montnets.emp.servmodule.txgl.blacklist.biz;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.FontUnderline;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.blacklist.PbListBlack;
import com.montnets.emp.entity.blacklist.PbListBlackDelrecord;
import com.montnets.emp.security.wgmsgconfig.WgMsgConfigBiz;
import com.montnets.emp.servmodule.txgl.blacklist.dao.BlackListDAO;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.PhoneUtil;
import com.montnets.emp.util.ZipUtil;

/**
 * 黑名单处理biz
 * @author linzhihan 2013-06-14
 *
 */
public class BlackListBiz extends SuperBiz
{

	/**
	 * 批量删除黑名单
	 * @param blList
	 * @return
	 * @throws ParseException 
	 */
	public Integer delBlacks(List<PbListBlack> blList) 
	{
		List<PbListBlack> pbListBlacks = new ArrayList<PbListBlack>();
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String,String>();
		Integer result = 0;
		Timestamp time = new Timestamp(new Date().getTime());
		Connection conn = this.empTransDao.getConnection();
		try
		{
			empTransDao.beginTransaction(conn);
			
			//遍历存储网关黑名单表
			for (PbListBlack pbBlack : blList) {
				conditionMap.clear();
				conditionMap.put("id", pbBlack.getId().toString());
				//将id置空，状态改为禁用
				pbBlack.setId(null);
				pbBlack.setOptype(0);
				pbBlack.setOptTime(time);
				pbListBlacks.add(pbBlack);
				
				//删除网关黑名单表记录
				empTransDao.delete(conn, PbListBlack.class,conditionMap);
			}
			result=empTransDao.save(conn, pbListBlacks, PbListBlack.class);
			if(result > 0)
			{
				empTransDao.commitTransaction(conn);
			}else
			{
				empTransDao.rollBackTransaction(conn);
			}
		}catch(Exception e)
		{
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e,"删除黑名单异常！");
		}finally
		{
			empTransDao.closeConnection(conn);
		}
		return result;
	}
	
	/**
	 * 批量删除黑名单,并记录黑明单的删除记录,返回Map
	 * <li>key=result,value=true|false</li>删除的结果
	 * <li>key=delCount,value=删除的条数</li>删除的条数
	 * @param blList
	 * @return  Map
	 */
	public Map<String,Object> delBlacksNew(List<PbListBlack> blList,String username) 
	{
		//返回结果
		Map<String,Object> resultMap=new HashMap<String,Object>();
		List<PbListBlack> pbListBlacks = new ArrayList<PbListBlack>();
		List<PbListBlackDelrecord> pbListBlackDelrecords = new ArrayList<PbListBlackDelrecord>();
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String,String>();
		Integer result = 0;
		Integer saveResult=0;
		PbListBlackDelrecord pbd=null;
		String taskId=UUID.randomUUID().toString();
		Timestamp time = new Timestamp(new Date().getTime());
		Connection conn = this.empTransDao.getConnection();
		try
		{
			empTransDao.beginTransaction(conn);
			
			//遍历存储网关黑名单表
			for (PbListBlack pbBlack : blList) {
				conditionMap.clear();
				conditionMap.put("id", pbBlack.getId().toString());
				//将id置空，状态改为禁用
				pbBlack.setId(null);
				pbBlack.setOptype(0);
				pbBlack.setOptTime(time);
				
				pbd=new PbListBlackDelrecord();
				pbd.setOperateId(username);//操作员
				pbd.setOptTime(time);
				pbd.setPhone(pbBlack.getPhone());
				pbd.setTaskId(taskId);
				
				pbListBlacks.add(pbBlack);
				pbListBlackDelrecords.add(pbd);
				//删除网关黑名单表记录
				empTransDao.delete(conn, PbListBlack.class,conditionMap);
			}
			result=empTransDao.save(conn, pbListBlacks, PbListBlack.class);
			
			saveResult=empTransDao.save(conn, pbListBlackDelrecords, PbListBlackDelrecord.class);
			boolean resultFlag=result > 0&&saveResult>0;
			resultMap.put("result", resultFlag);
			if(resultFlag)
			{
				//成功
				resultMap.put("delCount",result);
				empTransDao.commitTransaction(conn);
			}else
			{
				//失败
				resultMap.put("delCount",0);
				empTransDao.rollBackTransaction(conn);
			}
		}catch(Exception e)
		{
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e,"删除黑名单异常！");
		}finally
		{
			empTransDao.closeConnection(conn);
		}
		return resultMap;
	}
	
	/***
	 * 短信短信黑名单保存方法
	 * @param phones
	 * @param busCode
	 * @param corpCode
	 * @return
	 */
	public boolean  saveAll(List<String> phones,String busCode,String corpCode){

		boolean result = false;
		Connection conn = this.empTransDao.getConnection();
		try
		{
			empTransDao.beginTransaction(conn);
			Timestamp timestamp = new Timestamp(new Date().getTime());
			
			//遍历短信黑名单记录
			for (String phone : phones) {
				if(phone == null || "".equals(phone))
				{
					continue;
				}
				PbListBlack listBlack = new PbListBlack();
				listBlack.setUserId("000000");
				listBlack.setSpgate(" ");
				listBlack.setSpnumber(" ");
				listBlack.setPhone(Long.parseLong(phone.trim()));
				listBlack.setOptype(1);
				listBlack.setBlType(1);
				listBlack.setOptTime(timestamp);
				listBlack.setCorpCode(corpCode);
				listBlack.setSvrType(busCode);
				
				//保存短信黑名单记录
				result=empTransDao.save(conn, listBlack);
			}
			if(result)
			{
				empTransDao.commitTransaction(conn);
			}else
			{
				empTransDao.rollBackTransaction(conn);
			}
		}catch(Exception e)
		{
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e,"保存短信黑名单异常！");
		}finally
		{
			empTransDao.closeConnection(conn);
		}
		return result;
	}
	
    /**
     * 短信短信黑名单保存方法
     * @param phones   要入库的手机号
     * @param params   黑名单参数信息，保存如下参数
     * <li>corpCode:value</li>
     * <li>busCode:value</li>
     * <li>msg:value</li>
     * <li>username:value</li>
     * @return    保存成功返回true,否则返回false
     */
	public boolean  saveAllNew(List<String> phones,Map<String,String> params){

		boolean result = false;
		Connection conn = this.empTransDao.getConnection();
		try
		{
			empTransDao.beginTransaction(conn);
			Timestamp timestamp = new Timestamp(new Date().getTime());
			
			//遍历短信黑名单记录
			for (String phone : phones) {
				if(phone == null || "".equals(phone))
				{
					continue;
				}
				PbListBlack listBlack = new PbListBlack();
				listBlack.setUserId("000000");
				listBlack.setSpgate(" ");
				listBlack.setSpnumber(" ");
				listBlack.setPhone(Long.parseLong(phone.trim()));
				listBlack.setOptype(1);
				listBlack.setBlType(1);
				listBlack.setOptTime(timestamp);
				//企业编码
				String corpCode=params.get("corpCode");
				listBlack.setCorpCode(corpCode);
				//业务类型编码
				String busCode=params.get("busCode");
				listBlack.setSvrType(busCode);
				//备注信息
				String msg=params.get("msg");
				msg = StringUtils.defaultIfEmpty(msg, " ");
				listBlack.setMsg(msg);
				//保存短信黑名单记录
				result=empTransDao.save(conn, listBlack);
			}
			if(result)
			{
				empTransDao.commitTransaction(conn);
			}else
			{
				empTransDao.rollBackTransaction(conn);
			}
		}catch(Exception e)
		{
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e,"保存短信黑名单异常！");
		}finally
		{
			empTransDao.closeConnection(conn);
		}
		return result;
	}
	
	/***
	 * 短信短信黑名单删除记录保存方法
	 * @param phones
	 * @param busCode
	 * @param corpCode
	 * @return
	 */
	public boolean  saveAllDeleteRecord(List<String> phones){

		boolean result = false;
		Connection conn = this.empTransDao.getConnection();
		try
		{
			empTransDao.beginTransaction(conn);
			Timestamp timestamp = new Timestamp(new Date().getTime());
			
			//遍历短信黑名单记录
			for (String phone : phones) {
				if(phone == null || "".equals(phone))
				{
					continue;
				}
				PbListBlack listBlack = new PbListBlack();
				listBlack.setUserId("000000");
				listBlack.setSpgate(" ");
				listBlack.setSpnumber(" ");
				listBlack.setPhone(Long.parseLong(phone.trim()));
				listBlack.setOptype(1);
				listBlack.setBlType(1);
				listBlack.setOptTime(timestamp);
				
				//保存短信黑名单记录
				result=empTransDao.save(conn, listBlack);
			}
			if(result)
			{
				empTransDao.commitTransaction(conn);
			}else
			{
				empTransDao.rollBackTransaction(conn);
			}
		}catch(Exception e)
		{
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e,"保存短信黑名单异常！");
		}finally
		{
			empTransDao.closeConnection(conn);
		}
		return result;
	}
	
	/***
	 * 彩信黑名单保存方法
	 * @param phones
	 * @param corpCode
	 * @return
	 */
	public boolean  saveMSSBlack(List<String> phones,String corpCode){

		boolean result = false;
		Connection conn = this.empTransDao.getConnection();
		try
		{
			empTransDao.beginTransaction(conn);
			Timestamp timestamp = new Timestamp(new Date().getTime());
			
			//遍历黑名单记录
			for (String phone : phones) {
				if(phone == null || "".equals(phone))
				{
					continue;
				}
				PbListBlack listBlack = new PbListBlack();
                listBlack = new PbListBlack();
				listBlack.setUserId("000000");
				listBlack.setSpgate(" ");
				listBlack.setSpnumber(" ");
				listBlack.setOptype(1);
				listBlack.setSvrType(" ");
				listBlack.setBlType(2);
				listBlack.setPhone(Long.parseLong(phone.trim()));
				listBlack.setCorpCode(corpCode);
				listBlack.setOptTime(timestamp);
				
				//保存彩信黑名单记录
				result=empTransDao.save(conn, listBlack);
			}
			if(result)
			{
				empTransDao.commitTransaction(conn);
			}else
			{
				empTransDao.rollBackTransaction(conn);
			}
		}catch(Exception e)
		{
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e,"保存彩信黑名单异常！");
		}finally
		{
			empTransDao.closeConnection(conn);
		}
		return result;
	}
	
	
	
	/**
	 * 获取企业黑名单总数
	 * @description    
	 * @param corpCode 企业编码
	 * @return  企业黑名单总数     			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-7-31 上午09:53:25
	 */
	public Integer getCorpBlackListCount(String corpCode)
	{
		int count = 0;
		try
		{
			List<DynaBean> blackList = new BlackListDAO().getCorpBlackListCount(corpCode);
			
			if(blackList != null && blackList.size() > 0)
			{
				count = Integer.parseInt(blackList.get(0).get("mcount").toString());
			}
			return count;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取企业黑名单总数异常！");
			return count;
		}
	}
	
	/**
	 * 彩信黑名单 导出
	 * @param pageInfo
	 * @return
	 */
	public Map<String, String> createMMSBlackExcel(LinkedHashMap<String, String> conditionMap,String path,Map<String,String> busMap, PageInfo pageInfo,String langName) {

		String voucherTemplatePath = path + File.separator + "temp"
				+ File.separator + "mms_blacklist_" + langName + ".xlsx";// excel模板地址

		Map<String, String> resultMap = new HashMap<String, String>();

		String filePath = null;
		String fileName = null;// 文件名

		// int size = 1; // 生成的工作薄个数
		// EmpExecutionContext.info("工作表个数：" + size);

		/*彩信黑名单*/
		String mmsBlacklist = "zh_HK".equals(langName)?"mmsBlacklist_":"zh_TW".equals(langName)?"彩信黑名單":"彩信黑名单";

		XSSFWorkbook workbook = null;

		try {
			// 设置每个excel文件的行数
			pageInfo.setPageSize(500000);
			List<PbListBlack> pbListBlacks = new BaseBiz().getByCondition(PbListBlack.class, null, conditionMap, null, pageInfo);
			if (pbListBlacks == null || pbListBlacks.size() == 0) {
				return null;
			}
			// 计算出文件数
			int fileCount = pageInfo.getTotalPage();
			Date curDate = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddH24mmss");
			// 文件的存储路径
			String voucherFilePath = path + File.separator + "download"
					+ File.separator + "mms" + File.separator
					+ sdf.format(curDate);
			File file1 = new File(voucherFilePath);
			file1.mkdirs();
			int curIndex = 0;// 当前clientvosList的索引
			File file = new File(voucherTemplatePath);
			/*
			 * if (!file.exists()) { file.mkdirs(); }
			 */
			for (int f = 0; f < fileCount; f++) {
				fileName = "mms_" + sdf.format(curDate) + "_[" + (f+1)+ "].xlsx";
				// 读取模板
				InputStream in = new FileInputStream(file);
				// 工作表
				workbook = new XSSFWorkbook(in);
				// 表格样式
				XSSFCellStyle cellStyle = setCellStyle(workbook);

				XSSFSheet sheet = workbook.cloneSheet(0);
				workbook.removeSheetAt(0);
				workbook.setSheetName(0, mmsBlacklist);
				// 读取模板工作表
				XSSFRow row = null;
				EmpExecutionContext.debug("工作薄创建与模板移除成功!");
				if (f > 0)// 第一次时不需要再查询，因为前面已经查询出第一页
				{
					pbListBlacks =new BaseBiz().getByCondition(PbListBlack.class, null, conditionMap, null, pageInfo);
				}

				pageInfo.setPageIndex(f + 2);// 定位下一页
				
				XSSFCell[] cell = new XSSFCell[1];

				for (int k = 0;pbListBlacks!=null && k < pbListBlacks.size(); k++) {

					PbListBlack pb = (PbListBlack) pbListBlacks.get(k);
					// 手机号码
					Long mobile = pb.getPhone();

					// 所属部门
					// String depName = mvo.getDepName();
					row = sheet.createRow(k + 1);
					// 生成四个单元格
					cell[0] = row.createCell(0);

					// 设置单元格样式
					cell[0].setCellStyle(cellStyle);		
					// 设置单元格内容
					cell[0].setCellValue(mobile);
					
					curIndex++;// clientvosList索引加1
				}

				// 输出到xlsx文件
				OutputStream os = new FileOutputStream(voucherFilePath
						+ File.separator + fileName);
				// 写入Excel对象
				workbook.write(os);
				// 清除对象
				os.close();
				in.close();
				workbook = null;
				if(pbListBlacks!=null){
					pbListBlacks.clear();
				}
				pbListBlacks = null;
			}

			fileName = mmsBlacklist + sdf.format(curDate) + ".zip";
			filePath = path + File.separator + "download" + File.separator
					+ "mms" + File.separator + fileName;

			ZipUtil.compress(voucherFilePath, filePath);
			deleteDir(file1);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"彩信黑名单导出出现异常！");
		}
		resultMap.put("FILE_NAME", fileName);
		resultMap.put("FILE_PATH", filePath);
		resultMap.put("LIST_SIZE",""+pageInfo.getTotalRec());
		return resultMap;
	}
	
	/**
	 * 短信黑名单 导出
	 * @param pageInfo
	 * @return
	 */
	public Map<String, String> createBlackExcel(LinkedHashMap<String, String> conditionMap,String path,Map<String,String> busMap, PageInfo pageInfo,String langName) {

		String voucherTemplatePath = path + File.separator + "temp"
				+ File.separator + "shortmessage_blacklist_" + langName + ".xlsx";// excel模板地址

		Map<String, String> resultMap = new HashMap<String, String>();

		String filePath = null;
		String fileName = null;// 文件名

		// int size = 1; // 生成的工作薄个数
		// EmpExecutionContext.info("工作表个数：" + size);

		XSSFWorkbook workbook = null;

		/*短信黑名单*/
		String smsBlacklist = "zh_HK".equals(langName)?"smsBlacklist_":"zh_TW".equals(langName)?"短信黑名單":"短信黑名单";
		try {
			// 设置每个excel文件的行数
			pageInfo.setPageSize(500000);
			List<PbListBlack> pbListBlacks = new BaseBiz().getByCondition(PbListBlack.class,null, conditionMap, null, pageInfo);
			if (pbListBlacks == null || pbListBlacks.size() == 0) {
				return null;
			}
			// 计算出文件数
			int fileCount = pageInfo.getTotalPage();
			Date curDate = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddH24mmss");
			// 文件的存储路径
			String voucherFilePath = path + File.separator + "download"
					+ File.separator + "shortmessage" + File.separator
					+ sdf.format(curDate);
			File file1 = new File(voucherFilePath);
			file1.mkdirs();
			int curIndex = 0;// 当前clientvosList的索引
			File file = new File(voucherTemplatePath);
			/*
			 * if (!file.exists()) { file.mkdirs(); }
			 */
            PhoneUtil phoneUtil = new PhoneUtil();
            String[] haoduan=new WgMsgConfigBiz().getHaoduan();
			for (int f = 0; f < fileCount; f++) {
				fileName = "shortmessage_" + sdf.format(curDate) + "_[" + (f+1)+ "].xlsx";
				// 读取模板
				InputStream in = new FileInputStream(file);
				// 工作表
				workbook = new XSSFWorkbook(in);
				// 表格样式
				XSSFCellStyle cellStyle = setCellStyle(workbook);

				XSSFSheet sheet = workbook.cloneSheet(0);
                sheet.setColumnWidth(0,22*256);   //设置第一列为22个字节宽度 避免手机号过长而换行
				workbook.removeSheetAt(0);
				workbook.setSheetName(0, smsBlacklist);
				// 读取模板工作表
				XSSFRow row = null;
				EmpExecutionContext.debug("工作薄创建与模板移除成功!");
				if (f > 0)// 第一次时不需要再查询，因为前面已经查询出第一页
				{
					pbListBlacks =new BaseBiz().getByCondition(PbListBlack.class,null, conditionMap, null, pageInfo);
				}

				pageInfo.setPageIndex(f + 2);// 定位下一页
				
				XSSFCell[] cell = new XSSFCell[2];

				for (int k = 0; pbListBlacks!=null&& k < pbListBlacks.size(); k++) {

					PbListBlack pb = (PbListBlack) pbListBlacks.get(k);

					String busCode = " ".equals(pb.getSvrType())?"@":pb.getSvrType();
					String code=busMap.get(busCode)==null?"-":busMap.get(busCode);
					// 手机号码
					Long mobile = pb.getPhone();
                    String phone = mobile+"";
                    if(phoneUtil.getPhoneType(phone, haoduan)+1==0){
                        phone = "00"+phone;
                    }
					// 所属部门
					// String depName = mvo.getDepName();
					row = sheet.createRow(k + 1);
					// 生成四个单元格
					cell[0] = row.createCell(0);
					cell[1] = row.createCell(1);

					// 设置单元格样式
					cell[0].setCellStyle(cellStyle);
					cell[1].setCellStyle(cellStyle);			

					// 设置单元格内容
					cell[0].setCellValue(phone);
					cell[1].setCellValue(code);
					
					curIndex++;// clientvosList索引加1
				}

				// 输出到xlsx文件
				OutputStream os = new FileOutputStream(voucherFilePath
						+ File.separator + fileName);
				// 写入Excel对象
				workbook.write(os);
				// 清除对象
				os.close();
				in.close();
				workbook = null;
				if(pbListBlacks!=null){
					pbListBlacks.clear();
				}
				pbListBlacks = null;
			}

			fileName = smsBlacklist + sdf.format(curDate) + ".zip";
			filePath = path + File.separator + "download" + File.separator
					+ "shortmessage" + File.separator + fileName;

			ZipUtil.compress(voucherFilePath, filePath);
			deleteDir(file1);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"短信黑名单导出出现异常！");
		}
		resultMap.put("FILE_NAME", fileName);
		resultMap.put("FILE_PATH", filePath);
		resultMap.put("LIST_SIZE", ""+pageInfo.getTotalRec());
		return resultMap;
	}
	
	
	// 将设置单元格属性提取出来成一个方法，方便其他模块调用
	public XSSFCellStyle setCellStyle(XSSFWorkbook workbook) {
		XSSFCellStyle cellStyle = workbook.createCellStyle();

		XSSFFont font = workbook.createFont();
		// 字体名称
		font.setFontName("TAHOMA");
		// 粗体
		font.setBold(false);
		// 下环线
		font.setUnderline(FontUnderline.NONE);
		// 字体大小
		font.setFontHeight(11);
		cellStyle.setFont(font);
		// 水平对齐
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		// 竖直对齐
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //是否自动换行
		cellStyle.setWrapText(true);
		return cellStyle;
	}
	
	private static boolean deleteDir(File dir) {

		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}

    /**
     * 格式化手机字符串    去掉开头00或+ 以及0086或 +86
     * @param phoneList
     * @return
     */
    public List<String> formatPhoneList(List<String> phoneList){
        if(phoneList!=null&&phoneList.size()>0){
            for(int i=0;i<phoneList.size();i++){
                String phone = phoneList.get(i);
                phone = formatPhone(phone);
                phoneList.set(i,phone);
            }
        }
        return phoneList;
    }

    /**
     * 格式化手机字符串    去掉开头00或+ 以及0086或 +86
     * @param phone
     * @return
     */
    public String formatPhone(String phone){
        if(phone==null||"".equals(phone.trim())){
            return null;
        }
        return phone.replaceAll("^(0086|\\+86|00|\\+)","");
    }
}
