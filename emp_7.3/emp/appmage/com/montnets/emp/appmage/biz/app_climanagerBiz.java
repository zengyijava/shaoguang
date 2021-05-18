package com.montnets.emp.appmage.biz;

import com.montnets.emp.appmage.dao.app_climanagerDao;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.appmage.LfAppMwGpmem;
import com.montnets.emp.entity.appmage.LfAppMwGroup;
import com.montnets.emp.util.*;
import org.apache.commons.beanutils.DynaBean;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.*;

public class app_climanagerBiz extends SuperBiz
{
	app_climanagerDao dao=new app_climanagerDao();
	/**
	 * 查询APP客户
	 * @param corpCode
	 * @param conditionMap
	 * @param pageInfo
	 * @return
	 */
	public List<DynaBean> query(String corpCode, LinkedHashMap<String, String> conditionMap, PageInfo pageInfo)
	{
		List<DynaBean> accounts = null;
		try
		{
			return dao.query(corpCode, conditionMap, pageInfo);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "查询APP客户失败");
		}
		return accounts;
	}
	
	
	
	/**
	 * 根据群组ID查询APP客户
	 * @param corpCode
	 * @param conditionMap
	 * @param pageInfo
	 * @return
	 */
	public List<DynaBean> findByGroup(String corpCode, LinkedHashMap<String, String> conditionMap, PageInfo pageInfo)
	{
		List<DynaBean> accounts = null;
		try
		{
			if(conditionMap.get("groupid")!=null&&"-1".equals(conditionMap.get("groupid"))){
				return dao.findByNotGroup(corpCode, conditionMap, pageInfo);
			}else {
				return dao.findByGroup(corpCode, conditionMap, pageInfo);
			}
			
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "查询APP客户失败");
		}
		return accounts;
	}
	
	/****
	 * 查询群组信息
	 * @return
	 */
	public List<DynaBean> querygroup(){
		
		return dao.querygroup();
	}
	
	/****
	 * 查询未分群组信息
	 * @return
	 */
	public int  queryNotAtGroup(String corpcode){
		List<DynaBean> list=dao.queryNotAtGroup(corpcode);
		return list.size();
	}
	
	/****
	 * 删除群组
	 * @param conditionMap
	 * @return
	 */
	public boolean delDep(LinkedHashMap<String, String> conditionMap){
		boolean flag=false;
		Connection conn = empTransDao.getConnection();
		LinkedHashMap<String, String> condition = new LinkedHashMap<String, String>();
		try {
			empTransDao.beginTransaction(conn);
			empTransDao.delete(conn, LfAppMwGroup.class, conditionMap);
			condition.put("gid", conditionMap.get("gid"));
			empTransDao.delete(conn, LfAppMwGpmem.class, condition);
			empTransDao.commitTransaction(conn);
			flag=true;
		} catch (Exception e) {
			flag=false;
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e, "删除群组失败");
		}
		finally{
			empTransDao.closeConnection(conn);
		}
		return flag;
	}

	
	/****
	 * 退出群组
	 * @param conditionMap
	 * @return
	 */
	public boolean delLink(LinkedHashMap<String, String> conditionMap){
		boolean flag=false;
		Connection conn = empTransDao.getConnection();
		LinkedHashMap<String, String> condition = new LinkedHashMap<String, String>();
		try {
			empTransDao.beginTransaction(conn);
			condition.put("gid", conditionMap.get("gid"));
			condition.put("gmuser", conditionMap.get("guid"));
			empTransDao.delete(conn, LfAppMwGpmem.class, condition);
			empTransDao.commitTransaction(conn);
			flag=true;
		} catch (Exception e) {
			flag=false;
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e, "退出群组失败");
		}
		finally{
			empTransDao.closeConnection(conn);
		}
		return flag;
	}
	
	/****
	 * 退出群组
	 * @param conditionMap
	 * @return
	 */
	public boolean delAllLink(LinkedHashMap<String, String> conditionMap){
		boolean flag=false;
		Connection conn = empTransDao.getConnection();
		LinkedHashMap<String, String> condition = new LinkedHashMap<String, String>();
		try {
			//由于多选择时候，人员与群组是一一对应的关系，所以只需判断人员ID即可
			empTransDao.beginTransaction(conn);
			String hiddenValue=conditionMap.get("hiddenValue")+"";
			hiddenValue=hiddenValue.substring(0,hiddenValue.length()-1);
			String[] hidden=hiddenValue.split(",");
			
			String groupValue=conditionMap.get("groupValue")+"";
			groupValue=groupValue.substring(0,groupValue.length()-1);
			String[] group=groupValue.split(",");
			for(int k=0;k<hidden.length;k++){
				condition.put("gid",group[k]);
				condition.put("gmuser", hidden[k]);
				empTransDao.delete(conn, LfAppMwGpmem.class, condition);
			}
			empTransDao.commitTransaction(conn);
			flag=true;
		} catch (Exception e) {
			flag=false;
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e, "退出群组失败");
		}
		finally{
			empTransDao.closeConnection(conn);
		}
		return flag;
	}
	
	/***
	 * 分配群组
	 */
	public boolean changeGroup(LinkedHashMap<String, String> conditionMap,LinkedHashMap<String, String> objectMap){
		boolean flag=false;
		// 获取连接
		BaseBiz baseBiz = new BaseBiz();
		ArrayList<String> same=new ArrayList<String>();
		String value=objectMap.get("value");
		value=","+value;
//		String updateClient=objectMap.get("value");
		 String selectedid=conditionMap.get("selected");
		 LinkedHashMap<String, String> condition=new  LinkedHashMap<String, String>();
		 condition.put("apptype", "0");//梦网的
		Connection conn = empTransDao.getConnection();
		try {//查询出所有的信息
			List<LfAppMwGpmem> gplist=baseBiz.findListByCondition(LfAppMwGpmem.class, condition, null);
			empTransDao.beginTransaction(conn);
			String selectarray=selectedid.substring(0, selectedid.length()-1);
			String[] select=selectarray.split(";");
			for(int k=0;k<select.length;k++){
				
			for(int i=0;i<gplist.size();i++){
				LfAppMwGpmem gp=gplist.get(i);
				if(select[k].equals(gp.getGid()+"")){//处理当前选择的群组
					String user=","+gp.getGmuser()+",";
					if(value.indexOf(user)>-1){//在数据库中存在，在字符串中去掉
						value=value.replace(user, ","); //使用【,123,】方式匹配，替换
						same.add(gp.getGmuser()+"");//为了统一变为有效的
					}
				}
//				else if(value.indexOf(gp.getGmuser()+",")>-1&&!selectedid.equals(gp.getGid())){//如果这人存在其他群组
					//把其他群组的那个人变更为无效
//					 LinkedHashMap<String, String> condit=new  LinkedHashMap<String, String>();
//					 condit.put("gmuser", gp.getGmuser()+"");
//					 LinkedHashMap<String, String> obje=new  LinkedHashMap<String, String>();
//					 obje.put("gmstate", "0");
//					empTransDao.update(conn, LfAppMwGpmem.class,obje , condit);
					
					
//				}
			}
			
			if(!"".equals(value)&&!",".equals(value)&&value!=null){
				String[] newid=value.substring(0, value.length()-1).split(","); 
				if(newid.length>1){//由于第一个是空的，逗号，不做处理
				for(int i=1;i<newid.length;i++){
					LfAppMwGpmem gpvo=new LfAppMwGpmem();
					gpvo.setApptype(0);
					gpvo.setGmstatu(1);
					gpvo.setGid(Integer.parseInt(select[k]));
					
					gpvo.setGmuser(Integer.parseInt(newid[i]));
					empTransDao.saveObjProReturnID(conn, gpvo);
				}
				
			}
			}
			}
//			//把用户表中群组值也要修改
//			if(updateClient!=null&&!"".equals(updateClient)){
//				String[] client=updateClient.substring(0, updateClient.length()-1).split(","); 
//				for(int i=0;i<client.length;i++){
//					 LinkedHashMap<String, String> condit=new  LinkedHashMap<String, String>();
//					 condit.put("guid", client[i]);
//					 LinkedHashMap<String, String> obje=new  LinkedHashMap<String, String>();
//					 obje.put("gid", selectedid);
//					 empTransDao.update(conn, LfAppMwClient.class, obje, condit);
//				}
//			}

			
			empTransDao.commitTransaction(conn);
		} catch (Exception e1) {
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e1, "分配群组失败");
		}finally{
			flag=true;
			empTransDao.closeConnection(conn);
		}

		return flag;
	}
	/**
	 * APP发送记录查询导出方法
	 * 
	 * @param moTask
	 * @param pageInfo
	 * @return
	 */
	public Map<String, String> createMtHistoryExcel(String corpCode, LinkedHashMap<String, String> conditionMap, PageInfo pageInfo, String langName) {
		String BASEDIR=new TxtFileUtil().getWebRoot()+"appmage/contact/file";
		String voucherPath = "download";
		String separator="/";
		String voucherTemplatePath = BASEDIR + separator + "temp"
				+ separator + "app_climanager_" + langName + ".xlsx";// excel模板地址

		Map<String, String> resultMap = new HashMap<String, String>();

		String filePath = null;
		String fileName = null;// 文件名

		int size = 1; // 生成的工作薄个数
		EmpExecutionContext.info("工作表个数：" + size);

		XSSFWorkbook workbook = null;
		List<DynaBean>  mtTaskList=null;
		try {
			pageInfo.setPageSize(10000);// 设置每个excel文件的行数
			mtTaskList = query(corpCode, conditionMap,  pageInfo);

			if (mtTaskList == null || mtTaskList.size() == 0) {
				return null;
			}

			// 计算出文件数
			int fileCount = pageInfo.getTotalPage();

			Date curDate = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddH24mmss");
			// 文件的存储路径
			String voucherFilePath = BASEDIR + separator + voucherPath
					+ separator + "app_climanager" + separator
					+ sdf.format(curDate);

			File file1 = new File(voucherFilePath);
			file1.mkdirs();

			int curIndex = 0;// 当前clientvosList的索引

			File file = new File(voucherTemplatePath);
			/*
			 * if (!file.exists()) { file.mkdirs(); }
			 */

			for (int f = 0; f < fileCount; f++) {
				fileName = "app_climanager_" + sdf.format(curDate) + "_[" + (f+1)
						+ "].xlsx";
				// 读取模板
				InputStream in = new FileInputStream(file);
				// 工作表
				workbook = new XSSFWorkbook(in);
				// 表格样式
				XSSFCellStyle cellStyle =new ExcelTool(). setCellStyle(workbook);

				XSSFSheet sheet = workbook.cloneSheet(0);
				workbook.removeSheetAt(0);
				workbook.setSheetName(0, "APP客户管理");
				// 读取模板工作表
				XSSFRow row = null;
				EmpExecutionContext.debug("工作薄创建与模板移除成功!");
				

//				if (f > 0)// 第一次时不需要再查询，因为前面已经查询出第一页
//				{
//					mtTaskList = downBiz.getDownMtTaskVos(mtTask, pageInfo);
//				}

				pageInfo.setPageIndex(f + 2);// 定位下一页
				
				XSSFCell[] cell = new XSSFCell[7];

				for (int k = 0; k < mtTaskList.size(); k++) {
					row = sheet.createRow(k + 1);
					// 生成四个单元格
					cell[0] = row.createCell(0);
					cell[1] = row.createCell(1);
					cell[2] = row.createCell(2);
					cell[3] = row.createCell(3);
					cell[4] = row.createCell(4);
					cell[5] = row.createCell(5);
					cell[6] = row.createCell(6);

					// 设置单元格样式
					cell[0].setCellStyle(cellStyle);
					cell[1].setCellStyle(cellStyle);
					cell[2].setCellStyle(cellStyle);
					cell[3].setCellStyle(cellStyle);
					cell[4].setCellStyle(cellStyle);
					cell[5].setCellStyle(cellStyle);
					cell[6].setCellStyle(cellStyle);
					
					// 设置单元格内容
					String app_code=mtTaskList.get(k).get("app_code")+"";
					if(!"".equals(app_code)&&!"null".equals(app_code)){
						cell[0].setCellValue(app_code);
					}else{
						cell[0].setCellValue("");
					}
					String nick_name=mtTaskList.get(k).get("nick_name")+"";
					if(!"".equals(nick_name)&&!"null".equals(nick_name)){
						cell[1].setCellValue(nick_name);
					}else{
						cell[1].setCellValue("");
					}

					String phone=mtTaskList.get(k).get("phone")+"";
					if(!"".equals(phone)&&!"null".equals(phone)){
						cell[2].setCellValue(phone);
					}else{
						cell[2].setCellValue("");
					}
					String sex=mtTaskList.get(k).get("sex")+"";
					 if("1".equals(sex)){
						 cell[3].setCellValue("男");
						}else if("2".equals(sex)){
						cell[3].setCellValue("女");
						}else if("0".equals(sex)){
						cell[3].setCellValue("未知");
						}else {
						cell[3].setCellValue("-");
					}
					 
					 String age=mtTaskList.get(k).get("age")+"";
						if(!"".equals(age)&&!"null".equals(age)){
							cell[4].setCellValue(age);
						}else{
							cell[4].setCellValue("");
						}

					String mwgroupname=mtTaskList.get(k).get("mwgroupname")+"";
					if(!"".equals(mwgroupname)&&!"null".equals(mwgroupname)){
						cell[5].setCellValue(mwgroupname);
					}else{
						cell[5].setCellValue("");
					}

					String format="-";
					if(mtTaskList.get(k).get("verifytime")!=null){
						format=mtTaskList.get(k).get("verifytime")+"";
					}
					if(format.indexOf(".")>-1){
						format=format.substring(0, format.lastIndexOf("."));
					}
					cell[6].setCellValue(format);

					curIndex++;// clientvosList索引加1
				}

				// 输出到xlsx文件
				OutputStream os = new FileOutputStream(voucherFilePath
						+ separator + fileName);
				// 写入Excel对象
				workbook.write(os);
				// 清除对象
				os.close();
				in.close();
				workbook = null;
			}

			fileName = "app_climanager" + sdf.format(curDate) + ".zip";
			filePath = BASEDIR + separator + voucherPath + separator
					+ "app_climanager" + separator + fileName;

			ZipUtil.compress(voucherFilePath, filePath);
            boolean flag = FileUtils.deleteDir(file1);
            if (!flag) {
                EmpExecutionContext.error("刪除文件失敗！");
            }
		} catch (Exception e) {
			EmpExecutionContext.error(e,"APP客户管理导出异常");
		}
		resultMap.put("FILE_NAME", fileName);
		resultMap.put("FILE_PATH", filePath);
		resultMap.put("mtTaskList",String.valueOf(mtTaskList!=null? mtTaskList.size():""));
		//清除数据
		mtTaskList.clear();
		mtTaskList = null;
		return resultMap;
	}
}
