package com.montnets.emp.samemms.biz;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.vo.GroupInfoVo;
import com.montnets.emp.entity.client.LfClient;
import com.montnets.emp.entity.client.LfClientDep;
import com.montnets.emp.entity.employee.LfEmployee;
import com.montnets.emp.entity.employee.LfEmployeeDep;
import com.montnets.emp.entity.group.LfList2gro;
import com.montnets.emp.entity.group.LfMalist;
import com.montnets.emp.entity.pasroute.LfMmsAccbind;
import com.montnets.emp.entity.template.LfTemplate;
import com.montnets.emp.entity.ydcx.LfDfadvanced;
import com.montnets.emp.samemms.dao.SameMmsDao;
import com.montnets.emp.samemms.vo.LfTemplateVo;
import com.montnets.emp.table.client.TableLfClient;
import com.montnets.emp.table.employee.TableLfEmployee;
import com.montnets.emp.table.group.TableLfList2gro;
import com.montnets.emp.table.group.TableLfMalist;
import com.montnets.emp.util.GetSxCount;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.TxtFileUtil;


/**
 *   彩信BIZ
 * @author Administrator
 *
 */
public class SameMmsBiz extends SuperBiz  {
	
	/**
	 *   获取X单元格
	 * @param cell
	 * @return
	 */
	public static String getXCellFormatValue(XSSFCell cell)
    {
        String cellvalue = "";
        if (cell != null) 
         {
            // 判断当前Cell的Type
            switch (cell.getCellType()) 
            {
               // 如果当前Cell的Type为NUMERIC
               case XSSFCell.CELL_TYPE_NUMERIC: 
               case XSSFCell.CELL_TYPE_FORMULA: 
               {
                  // 判断当前的cell是否为Date
                  if (DateUtil.isCellDateFormatted(cell)) 
                  {
                     // 如果是Date类型则，取得该Cell的Date值
                     Date date = cell.getDateCellValue();
                     // 把Date转换成本地格式的字符串
                     Calendar c = Calendar.getInstance();
                     c.setTime(date);	                     
                     if(c.get(Calendar.HOUR)==0 && c.get(Calendar.MINUTE)==0 && c.get(Calendar.SECOND) ==0){
                     	cellvalue = new SimpleDateFormat("yyyy-MM-dd").format(date);
                      }else {
                     	 cellvalue = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
 					 }
                  }
                  // 如果是纯数字
                  else
                  {
                     // 取得当前Cell的数值
                	  // 是否有小数部分（分开处理）
                	  if(Math.floor(cell.getNumericCellValue())==cell.getNumericCellValue())
                	  {
                		  cellvalue=String.valueOf((long)cell.getNumericCellValue());
                	  }else {
                		  cellvalue = cell.getRawValue();
					  }
                   
                  }
                  break;
               }
               // 如果当前Cell的Type为STRIN
               case XSSFCell.CELL_TYPE_STRING:
                  // 取得当前的Cell字符串
                  cellvalue = cell.getStringCellValue();
                  break;
               // 默认的Cell值
               default:
                  cellvalue = " ";
            }
         }
         else 
         {
            cellvalue = "";
         }
        return cellvalue;
    }
	
	/**
	 *   获取H单元格
	 * @param cell
	 * @return
	 */
	public static String getHCellFormatValue(HSSFCell cell)
    {
        String cellvalue = "";
        if (cell != null) 
         {
            // 判断当前Cell的Type
            switch (cell.getCellType()) 
            {
	            case HSSFCell.CELL_TYPE_NUMERIC: // 数字   
	                // 判断当前的cell是否为Date
	                  if (DateUtil.isCellDateFormatted(cell)) 
	                  {
	                     // 如果是Date类型则，取得该Cell的Date值
	                     Date date = cell.getDateCellValue();
	                     // 把Date转换成本地格式的字符串
	                     Calendar c = Calendar.getInstance();
	                     c.setTime(date);	                     
	                     if(c.get(Calendar.HOUR)==0 && c.get(Calendar.MINUTE)==0 && c.get(Calendar.SECOND) ==0){
	                    	cellvalue = new SimpleDateFormat("yyyy-MM-dd").format(date);
	                     }else {
	                    	 cellvalue = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
						 }
	                  }
	                  // 如果是纯数字
	                  else
	                  {
	                     // 是否有小数部分（分开处理）
	                	  if(Math.floor(cell.getNumericCellValue())==cell.getNumericCellValue())
	                	  {
	                		  cellvalue=String.valueOf((long)cell.getNumericCellValue());
	                	  }else {
	                		  cellvalue = String.valueOf(cell.getNumericCellValue());
						  }
	                	  //System.out.println(cellvalue);
	                  }
	                break;   
	            case HSSFCell.CELL_TYPE_STRING: // 字符串   
	                cellvalue = cell.getStringCellValue() ; 	                       
	                break;    
	            case HSSFCell.CELL_TYPE_FORMULA: // 公式   
	                cellvalue = cell.getCellFormula();   
	                break;   
	            case HSSFCell.CELL_TYPE_BLANK: // 空值   
	            	cellvalue = " "; 
	                break;   
	            case HSSFCell.CELL_TYPE_ERROR: // 故障   
	            	cellvalue = " ";
	                break;   
	            default:   
	            	cellvalue = " ";
	                break;  
            }
         }
         else 
         {
            cellvalue = "";
         }
        return cellvalue;
    }
	
	
	/**
	 * 判断txt文本编码
	 * @param inp
	 * @return
	 */
	public String get_charset( InputStream inp ) {   
        String charset = "GBK";   
        byte[] first3Bytes = new byte[3];   
        try {   
            BufferedInputStream bis = new BufferedInputStream( inp );   
            bis.mark( 0 );   
            int read = bis.read(first3Bytes, 0, 3 );   
            if ( read == -1 ) return charset; 
            if ( first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE ) {   
                charset = "Unicode";   
            }   
            else if ( first3Bytes[0] == (byte) 0xFE && first3Bytes[1] == (byte) 0xFF ) {   
                charset = "UTF-16BE";   
            }   
            else if ( first3Bytes[0] == (byte) 0xEF && first3Bytes[1] == (byte) 0xBB && first3Bytes[2] == (byte) 0xBF ) {   
                charset = "UTF-8";   
            }   
            bis.reset();   
        }catch(Exception e)
        {
        	EmpExecutionContext.error(e, "编码格式转化失败！");
        }
        return charset;
	}
	
	/**
	 *  过滤重复号码
	 * @param aa
	 * @param ee
	 * @return
	 */
	public boolean checkRepeat(HashSet<Long> aa,String ee)
	{
		try{
			Long number = Long.parseLong(ee);
			if(aa.contains(number)){
				return false;
			}else{
				aa.add(number);
			}
			return true;
		}catch (Exception e) {
			EmpExecutionContext.error(e,"移动彩讯过滤重复号码出现异常！");
			return false;
		}

	}
	
	/**
	 *   彩信存放号码文件的地址
	 * @param id
	 * @param time
	 * @return
	 */
	public String[] getSaveUrl(int id, Date time)
	{
		TxtFileUtil txtFileUtil = new TxtFileUtil();
		//这里先存放的是彩信用短信的号码文件地址/稍后做修改
		String uploadPath = StaticValue.MMS_MTTASKS;
		
		//获取系统时间
		Calendar calendar = Calendar.getInstance();
		//获取当前年，月
		String yearstr = String.valueOf(calendar.get(Calendar.YEAR));
		int month = calendar.get(Calendar.MONTH) + 1;
		//处理小于10的月份
		String monthstr = month > 9 ? String.valueOf(month)
				: "0" + String.valueOf(month);
		uploadPath=uploadPath+yearstr+"/"+monthstr+"/";
			try
			{
				new File(txtFileUtil.getWebRoot()+uploadPath).mkdirs();
			} catch (Exception e)
			{
				EmpExecutionContext.error(e, "彩信写文件失败！");
			} 
		GetSxCount sx = GetSxCount.getInstance();
		String sxcount = sx.getCount();
		//存放路径的数组
		String[] url = new String[5];
		String saveName = "1_" + id + "_"
				+ (new SimpleDateFormat("yyyyMMddHHmmssS")).format(time)+"_"+sxcount+ ".txt";
		String logicUrl;
		String physicsUrl = txtFileUtil.getWebRoot();
		physicsUrl = physicsUrl + uploadPath + saveName;
		logicUrl = uploadPath + saveName;
		url[0] = physicsUrl;
		url[1] = logicUrl;
		url[2] = url[0].replace(".txt", "_bad.txt");
		//预览文件路径
		url[3] = url[0].replace(".txt", "_view.txt");
		url[4] = url[1].replace(".txt", "_view.txt");
		return url;
	}
	
	/**
	 *   通过guid str去获取对应的员工 客户 外部人员的电话号码
	 * @param idStr	guid串
	 * @param type	类型  1是员工   2是客户  3是外部人员
	 * @return
	 */
	public String getUserByGuIdStr(String idStr ,String type){
		List<String[]> userList = null;
		String str = "";
		try{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			String columName = "";
			conditionMap.put("guId&in",idStr);
			if("1".equals(type)){
				columName = TableLfEmployee.MOBILE;
				userList = empDao.findListByConditionByColumName(LfEmployee.class,conditionMap, columName,null);
			}else if("2".equals(type)){
				columName = TableLfClient.MOBILE;
				userList = empDao.findListByConditionByColumName(LfClient.class,conditionMap, columName,null);
			}else if("3".equals(type)){
				columName = TableLfMalist.MOBILE;
				userList = empDao.findListByConditionByColumName(LfMalist.class,conditionMap, columName,null);
			}
			if(userList != null && userList.size() > 0)
			{
				for(int i=0;i<userList.size();i++)
				{
					str = str + userList.get(i)[0] + ",";
				}
			}
		}catch (Exception e) {
			str = "";
			EmpExecutionContext.error(e,"移动彩讯获取人员信息出现异常！");
		}
		return str;
	}
	
	
	/**
	 *   处理客户机构获取客户手机号码
	 * @param depIdStr
	 * @param depPathList
	 * @return
	 */
	public String getClientPhoneByDepIds(String depIdStr ,List<String> depPathList){
		//存放手机号码的list
		//List<String> returnList = null;
		String phoneStr ="";
		try{
			phoneStr = new SameMmsDao().getClientPhoneByDepIds(depIdStr, depPathList);
		}catch (Exception e) {
			phoneStr = "";
			EmpExecutionContext.error(e,"移动彩讯处理客户机构获取客户手机号码出现异常！");
		}
		//返回
		return phoneStr;
	}
	
	/**
	 *   处理客户机构获取客户手机号码
	 * @param depIdStr
	 * @param depPathList
	 * @return
	 */
	public String getClientPhoneByDepIds(String depIdStr ,List<String> depPathList,String corpCode){
		//存放手机号码的list
		//List<String> returnList = null;
		String phoneStr ="";
		try{
			phoneStr = new SameMmsDao().getClientPhoneByDepIds(depIdStr, depPathList,corpCode);
		}catch (Exception e) {
			phoneStr = "";
			EmpExecutionContext.error(e,"移动彩讯处理客户机构获取客户手机号码出现异常！");
		}
		//返回
		return phoneStr;
	}
	
	/**
	 *   处理员工机构查询其员工号码
	 * @param depIdStr
	 * @param depPathList
	 * @return
	 */
	public String getEmployeePhoneByDepIds(String depIdStr ,List<String> depPathList){
		//存放手机号码的list
		String phoneStr ="";
		try{
			phoneStr = new SameMmsDao().findEmployeePhoneByDepIds(depIdStr, depPathList);
		}catch (Exception e) {
			phoneStr = "";
			EmpExecutionContext.error(e,"移动彩讯处理员工机构获取员工手机号码出现异常！");
		}
		//返回
		return phoneStr;
	}
	
	/**
	 *   处理员工机构查询其员工号码(重载方法)
	 * @param depIdStr
	 * @param depPathList
	 * @return
	 */
	public String getEmployeePhoneByDepIds(String depIdStr ,List<String> depPathList,String corpCode){
		//存放手机号码的list
		String phoneStr ="";
		try{
			phoneStr = new SameMmsDao().findEmployeePhoneByDepIds(depIdStr, depPathList,corpCode);
		}catch (Exception e) {
			phoneStr = "";
			EmpExecutionContext.error(e,"移动彩讯处理员工机构获取员工手机号码出现异常！");
		}
		//返回
		return phoneStr;
	}
	/**
	 *   获取群组中的人员的手机号码LIST
	 * @param groupId
	 * @return
	 */
	public String getGroupUserPhoneByIds(String groupId){
		//存放手机号码的list
		//List<String> returnList = null;
		String phoneStr ="";
		try{
			phoneStr = new SameMmsDao().getClientGroupUserById(groupId);
		}catch (Exception e) {
			phoneStr = "";
			EmpExecutionContext.error(e,"移动彩讯获取群组中的人员的手机号码出现异常！");
		}
		//返回
		return phoneStr;
	}
	
	
	/**
	 *   serlvet中处理员工机构的整合
	 * @param phoneStr
	 * @param empDepIds
	 * @return
	 */
	public String getEmployeePhoneSrrByDepId(String phoneStr,String empDepIds){
		try{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			//通过员工机构id查找电话
			String[] empDepArr = empDepIds.split(",");
			List<String> empDepIdsList = Arrays.asList(empDepArr);
			String noContainIds = "";
			String containIds = "";
			List<String> empdepPathList = new ArrayList<String>();
			for(int a=0;a<empDepIdsList.size();a++)
			{
				String id = empDepIdsList.get(a);
				if(!"".equals(id))
				{
					if(id.contains("e"))
					{
						//处理包含子机构 
						id = id.substring(1,id.length());
						containIds = containIds + id +",";
					}else {
						//处理不包含子机构
						noContainIds = noContainIds + id+",";
					}
				}
			}
			if(!"".equals(containIds) && containIds.length()>0){
				containIds = containIds.substring(0,containIds.length()-1);
				conditionMap.clear();
				conditionMap.put("depId&in", containIds);
				List<LfEmployeeDep> empDepList  = empDao.findListBySymbolsCondition(LfEmployeeDep.class, conditionMap, null);
				if(empDepList != null && empDepList.size()>0){
					for(int b=0;b<empDepList.size();b++){
						empdepPathList.add(empDepList.get(b).getDeppath());
					}
				}
			}
			String returnEmpPhones = this.getEmployeePhoneByDepIds(noContainIds, empdepPathList);
			phoneStr = phoneStr + returnEmpPhones;
		}catch (Exception e) {
			EmpExecutionContext.error(e,"移动彩讯操作员工机构出现异常！");
		}
		return phoneStr;
	}
	
	/**
	 *   serlvet中处理员工机构的整合(重载方法)
	 * @param phoneStr
	 * @param empDepIds
	 * @return
	 */
	public String getEmployeePhoneSrrByDepId(String phoneStr,String empDepIds,String corpCode){
		try{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			//通过员工机构id查找电话
			String[] empDepArr = empDepIds.split(",");
			List<String> empDepIdsList = Arrays.asList(empDepArr);
			String noContainIds = "";
			String containIds = "";
			List<String> empdepPathList = new ArrayList<String>();
			for(int a=0;a<empDepIdsList.size();a++)
			{
				String id = empDepIdsList.get(a);
				if(!"".equals(id))
				{
					if(id.contains("e"))
					{
						//处理包含子机构 
						id = id.substring(1,id.length());
						containIds = containIds + id +",";
					}else {
						//处理不包含子机构
						noContainIds = noContainIds + id+",";
					}
				}
			}
			if(!"".equals(containIds) && containIds.length()>0){
				containIds = containIds.substring(0,containIds.length()-1);
				conditionMap.clear();
				conditionMap.put("depId&in", containIds);
				List<LfEmployeeDep> empDepList  = empDao.findListBySymbolsCondition(LfEmployeeDep.class, conditionMap, null);
				if(empDepList != null && empDepList.size()>0){
					for(int b=0;b<empDepList.size();b++){
						empdepPathList.add(empDepList.get(b).getDeppath());
					}
				}
				else{
					EmpExecutionContext.error("静态彩信发送选择员工机构发送，不存在此机构，机构ID为:"+containIds);
					//返回原始手机号码字符串
					return phoneStr;
				}
			}
			String returnEmpPhones = this.getEmployeePhoneByDepIds(noContainIds, empdepPathList,corpCode);
			phoneStr = phoneStr + returnEmpPhones;
		}catch (Exception e) {
			EmpExecutionContext.error(e,"移动彩讯操作员工机构出现异常！");
		}
		return phoneStr;
	}
	
	/**
	 *   serlvet中处理客户机构的整合
	 * @param phoneStr
	 * @param empDepIds
	 * @return
	 */
	public String getClientPhoneStrByDepId(String phoneStr,String cliDepIds){
		try{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			String[] clientDepArr = cliDepIds.split(",");
			List<String> clientDepIdsList = Arrays.asList(clientDepArr);
			String noContainIds = "";
			String containIds = "";
			List<String> clientdepPathList = new ArrayList<String>();
			for(int a=0;a<clientDepIdsList.size();a++)
			{
				String id = clientDepIdsList.get(a);
				if(!"".equals(id))
				{
					if(id.contains("e"))
					{
						//处理包含子机构 
						id = id.substring(1,id.length());
						containIds = containIds + id +",";
					}else {
						//处理不包含子机构
						noContainIds = noContainIds + id+",";
					}
				}
			}
			if(!"".equals(containIds) && containIds.length()>0){
				containIds = containIds.substring(0,containIds.length()-1);
				conditionMap.clear();
				conditionMap.put("depId&in", containIds);
				List<LfClientDep> clientDepList  = empDao.findListBySymbolsCondition(LfClientDep.class, conditionMap, null);
				if(clientDepList != null && clientDepList.size()>0){
					for(int b=0;b<clientDepList.size();b++){
						clientdepPathList.add(clientDepList.get(b).getDeppath());
					}
				}
			}
			String returnClinetPhones = this.getClientPhoneByDepIds(noContainIds, clientdepPathList);
			phoneStr = phoneStr + returnClinetPhones;
		}catch (Exception e) {
			EmpExecutionContext.error(e,"移动彩讯操作客户机构出现异常！");
		}
		return phoneStr;
	}
	
	/**
	 *   serlvet中处理客户机构的整合
	 * @param phoneStr
	 * @param empDepIds
	 * @return
	 */
	public String getClientPhoneStrByDepId(String phoneStr,String cliDepIds,String corpCode){
		try{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			String[] clientDepArr = cliDepIds.split(",");
			List<String> clientDepIdsList = Arrays.asList(clientDepArr);
			String noContainIds = "";
			String containIds = "";
			List<String> clientdepPathList = new ArrayList<String>();
			for(int a=0;a<clientDepIdsList.size();a++)
			{
				String id = clientDepIdsList.get(a);
				if(!"".equals(id))
				{
					if(id.contains("e"))
					{
						//处理包含子机构 
						id = id.substring(1,id.length());
						containIds = containIds + id +",";
					}else {
						//处理不包含子机构
						noContainIds = noContainIds + id+",";
					}
				}
			}
			if(!"".equals(containIds) && containIds.length()>0){
				containIds = containIds.substring(0,containIds.length()-1);
				conditionMap.clear();
				conditionMap.put("depId&in", containIds);
				List<LfClientDep> clientDepList  = empDao.findListBySymbolsCondition(LfClientDep.class, conditionMap, null);
				if(clientDepList != null && clientDepList.size()>0){
					for(int b=0;b<clientDepList.size();b++){
						clientdepPathList.add(clientDepList.get(b).getDeppath());
					}
				}
				else{
					EmpExecutionContext.error("静态彩信发送选择客户机构发送，不存在此机构，机构ID为:"+containIds);
					//返回原始手机号码字符串
					return phoneStr;
				}
			}
			String returnClinetPhones = this.getClientPhoneByDepIds(noContainIds, clientdepPathList,corpCode);
			phoneStr = phoneStr + returnClinetPhones;
		}catch (Exception e) {
			EmpExecutionContext.error(e,"移动彩讯操作客户机构出现异常！");
		}
		return phoneStr;
	}
	
	/**
	 *   serlvet中处理群组的整合
	 * @param phoneStr
	 * @param empDepIds
	 * @return
	 */
	public String getGroupPhoneStrById(String phoneStr,String groupIds){
		try{
			groupIds = groupIds.substring(0,groupIds.length()-1);
			String[] arr = groupIds.split(",");
			List<String> groupIdsList = Arrays.asList(arr);
			for(int a=0;a<groupIdsList.size();a++){
				String tempPhoneStr = this.getGroupUserPhoneByIds(groupIdsList.get(a));
				phoneStr = phoneStr + tempPhoneStr;
			}
		}catch (Exception e) {
			EmpExecutionContext.error(e,"移动彩讯处理群组出现异常！");
		}
		return phoneStr;
	}
	
	
	
	
	/**
	 *   查询静态彩信模板/动态彩信模板
	 * @param lfTemplate
	 * @return
	 */
	public List<LfTemplate> getMMSTemplateByUserId(LfTemplate lfTemplate){
		List<LfTemplate> lfTemplateList = null;
		try{
			lfTemplateList = new SameMmsDao().getMMSTemplate(lfTemplate);
		}catch (Exception e) {
			EmpExecutionContext.error(e,"查询彩信模板出现异常！");
		}
		return lfTemplateList;
	}
	
	/**
	 *  彩信获取spuser
	 * @param lfMmsAccbind
	 * @return
	 */
	public List<LfMmsAccbind> getMmsSpUser(LfMmsAccbind lfMmsAccbind){
		List<LfMmsAccbind> lfMmsAccbinds=null;
		try
		{
			lfMmsAccbinds=new SameMmsDao().getMmsSpUser(lfMmsAccbind);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"移动彩讯获取SPUSER出现异常！");
		}
		return lfMmsAccbinds;
	}
	
	/**
	 * 通过员工机构id查找树
	 * @param lfSysuser
	 * @param depIds 机构Id集合字符串
	 * @return
	 * @throws Exception
	 */
	public List<LfEmployeeDep> getEmpSecondDepTreeByUserIdorDepId(String userId,String depId) throws Exception {
		List<LfEmployeeDep> deps = null;
		try{
			deps = new SameMmsDao().getEmpSecondDepTreeByUserIdorDepId( userId,depId);
		}catch (Exception e) {
			EmpExecutionContext.error(e,"移动彩讯获取员工机构出现异常！");
		}
		return deps;
	}
	
	/**
	 * 通过员工机构id查找树(重载方法)
	 * @param lfSysuser
	 * @param depIds 机构Id集合字符串
	 * @param corpCode 企业编码
	 * @return
	 * @throws Exception
	 */
	public List<LfEmployeeDep> getEmpSecondDepTreeByUserIdorDepId(String userId,String depId,String corpCode) throws Exception {
		List<LfEmployeeDep> deps = null;
		try{
			//调用Dao查询数据
			deps = new SameMmsDao().getEmpSecondDepTreeByUserIdorDepId( userId,depId,corpCode);
		}catch (Exception e) {
			EmpExecutionContext.error(e,"移动彩讯获取员工机构出现异常！");
		}
		return deps;
	}
	
	
	/**
	 * 通过客户机构id查找树
	 * @param lfSysuser
	 * @param depIds 机构Id集合字符串
	 * @return
	 * @throws Exception
	 */
	public List<LfClientDep> getCliSecondDepTreeByUserIdorDepId(String userId,String depId) throws Exception {
		List<LfClientDep> deps = null;
		try{
			deps = new SameMmsDao().getCliSecondDepTreeByUserIdorDepId(userId,depId);
		}catch (Exception e) {
			EmpExecutionContext.error(e,"移动彩讯获取客户机构出现异常！");
		}
		return deps;
	}
	
	/**
	 * 通过客户机构id查找树(重载方法)
	 * @param lfSysuser
	 * @param depIds 机构Id集合字符串
	 * @return
	 * @throws Exception
	 */
	public List<LfClientDep> getCliSecondDepTreeByUserIdorDepId(String userId,String depId,String corpCode) throws Exception {
		List<LfClientDep> deps = null;
		try{
			//调用DAO查询数据
			deps = new SameMmsDao().getCliSecondDepTreeByUserIdorDepId(userId,depId,corpCode);
		}catch (Exception e) {
			EmpExecutionContext.error(e,"移动彩讯获取客户机构出现异常！");
		}
		return deps;
	}
	
	/**
	 * 处理群组中分页选择人员  
	 * @param groupId	群组ID
	 * @param pageInfo	分页信息
	 * @param type	员工  /客户  群组
	 * @return
	 * @throws Exception
	 */
	public List<GroupInfoVo> getGroupUser(Long groupId,PageInfo pageInfo,String type) throws Exception
	{
		List<GroupInfoVo> groupVosList = null;
		try
		{
			//查询员工群组 
			if("1".equals(type)){
				groupVosList = new SameMmsDao().findGroupUserByIds(groupId,pageInfo);
			}else if("2".equals(type)){
			//查询客户群组 
				groupVosList = new SameMmsDao().findGroupClientByIds(groupId, pageInfo);
			}
		} catch (Exception e){
			groupVosList = null;
			EmpExecutionContext.error(e,"移动彩讯获取群组信息出现异常！");
		}
		return groupVosList;
	}
	
	
	
	/**
	 * 根据条件获取该操作员的彩信素材信息
	 * 
	 * @param curLoginedUserId
	 * @param lfTemplateVo
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<LfTemplateVo> getTemplateByCondition(Long curLoginedUserId,
			LfTemplateVo lfTemplateVo, PageInfo pageInfo) throws Exception
	{
		//当前登录操作员id
		if (curLoginedUserId == null)
		{
			//返回
			return null;
		}
		//模板对象集合
		List<LfTemplateVo> templateVosList = null;
		try
		{
			//分页不为空，需要分页
			if (pageInfo != null)
			{
				//按条件获取记录，分页
				templateVosList = new SameMmsDao()
						.findLfTemplateVoList(curLoginedUserId, lfTemplateVo,
								pageInfo);
			} else
			{
				//按条件获取所有记录
				templateVosList = new SameMmsDao()
						.findLfTemplateVoList(curLoginedUserId, lfTemplateVo);
			}
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e,"移动彩讯获取彩信素材信息出现异常！");
			throw e;
		}
		//返回结果
		return templateVosList;
	}
	
	
	
	/**
	 * 计算员工/客户群组中的人数
	 * @param udgIds  群组IDS
	 * @param type	群组类型  1是员工   2是客户群组
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> getGroupCount(String udgIds,String type) throws Exception
	{
		HashMap<String, String> countMap = new HashMap<String, String>();
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		//群组类型   1是员工   2是客户群组
		if("1".equals(type)){
			// 添加类型(员工0,客户1,手工2)
			conditionMap.put("l2gType&in", "0,2");
		}else if("2".equals(type)){
			// 添加类型(员工0,客户1,手工2)
			conditionMap.put("l2gType&in", "1,2");
		}
		//条件
		conditionMap.put("udgId&in", udgIds);
		//GROUPBY
		String groupColum = TableLfList2gro.UDG_ID;
		//查询的列
		String columName = "count("+TableLfList2gro.L2G_ID+"),"+TableLfList2gro.UDG_ID;
		//查询
		List<String[]> countList = empDao.findListByConditionByColumNameWithGroupBy(LfList2gro.class, 
				conditionMap, columName, null,groupColum);
		if(countList != null && countList.size() > 0)
		{
			for(int i=0;i<countList.size();i++)
			{
				String[] countArr = countList.get(i);
				countMap.put(countArr[1], countArr[0]);
			}
		}
		return countMap;
	}
	
	/**
	 *    查询客户机构人员
	 * @param clientDep	当前机构对象
	 * @param containType	是否包含  1包含   2不包含
	 * @param pageInfo	分页
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getClientsByDepId(LfClientDep clientDep,Integer containType,PageInfo pageInfo) throws Exception{
		List<DynaBean> beanList = null;
		try{
			beanList = new SameMmsDao().findClientsByDepId(clientDep, containType, pageInfo);
		}catch (Exception e) {
			beanList = null;
			EmpExecutionContext.error(e,"查询客户机构人员出现异常！");
		}
		return beanList;
	}
	/**
	 *   获取机构客户 人数
	 * @param clientDep	机构对象
	 * @param containType 1包含  2是不包含
	 * @return
	 * @throws Exception
	 */
	public  Integer getDepClientCount(LfClientDep clientDep,Integer containType) throws Exception{
		Integer count = 0;
		try{
			count = new SameMmsDao().findClientsCountByDepId(clientDep, containType);
		}catch (Exception e) {
			EmpExecutionContext.error(e,"获取机构客户 人数出现异常！");
		}
		return count;
	}
	
	
	/**
	 * 高级设置存为默认
	 * @param conditionMap 删除原来设置条件
	 * @param lfDfadvanced 更新默认高级设置对象
	 * @return
	 */
	public String setDefault(LinkedHashMap<String, String> conditionMap, LfDfadvanced lfDfadvanced){
		String result = "fail";
		Connection conn = null;
		try {
			conn = empTransDao.getConnection();
			empTransDao.beginTransaction(conn);
			
			//删除原有的设置
			empTransDao.delete(conn, LfDfadvanced.class, conditionMap);
			
			//新增默认高级设置信息
			boolean saveResult = empTransDao.save(conn, lfDfadvanced);
			
			//成功
			if(saveResult){
				result = "seccuss";
				empTransDao.commitTransaction(conn);
			}
			else{
				empTransDao.rollBackTransaction(conn);
			}
			return result;
		} catch (Exception e) {
			EmpExecutionContext.error(e, "高级设置存为默认异常！");
			empTransDao.rollBackTransaction(conn);
			return result;
		}
		finally{
			// 关闭连接
			if(conn != null){
				empTransDao.closeConnection(conn);
			}
		}
	}
	
	
	
}
