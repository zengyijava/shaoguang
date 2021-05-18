/**
 * Program  : WebexUploadDao.java
 * Author   : chensj
 * Create   : 2013-6-18 上午09:13:45
 * company ShenZhen Montnets Technology CO.,LTD.
 *
 */

package com.montnets.emp.netnews.dao;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedHashMap;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.netnews.base.DateFormatter;
import com.montnets.emp.netnews.base.GlobalMethods;
import com.montnets.emp.netnews.entity.LfWXUploadFile;


/**
 * 
 * @author   chensj <510061684@qq.com>
 * @version  1.0.0
 * @2013-6-18 上午09:13:45
 */
public class WebexUploadDao extends BaseBiz {
	
	private static final LinkedHashMap<String, String> conditionMap =
									new LinkedHashMap<String, String>();
	private static final LinkedHashMap<String, String> objectMap = 
									new LinkedHashMap<String, String>();
	
	/**
	 * 保存素材
	 * @author chensj
	 * @create 2013-6-18 上午09:14:49
	 * @since 
	 * @throws SQLException
	 */
	public boolean UploadFile(String filename, String filedes, String filedi,
			String fileURL, String uploadUserid, String sortid, String nameTemp,
			int scfication, int usertype,String type,long fileSize,String lgcorpcode)  {
		conditionMap.clear();
		objectMap.clear();
		Timestamp t =  DateFormatter.sqlDate();
		Long idLong = GlobalMethods.swicthLong(uploadUserid);
		Long sortLong = GlobalMethods.swicthLong(sortid);
		LfWXUploadFile file = new LfWXUploadFile();
		file.setFILENAME(filename);
		file.setFILEDESC(filedes);
		file.setFILEVER(filedi);
		file.setWEBURL(fileURL);
		file.setUPLOADUSERID(idLong);
		file.setUPLOADDATE(t);
		file.setSORTID(sortLong.toString());
		file.setCREATID(idLong);
		file.setCREATDATE(t);
		//去掉原因是：当文件名太长，导致报错出错，因为数据库字段长度不够用  may
		//file.setNAMETEMP(nameTemp);
		file.setType(type);
		file.setFILESIZE(fileSize);
		file.setCORP_CODE(lgcorpcode);
		try {
			Long b = addObjReturnId(file);
			objectMap.put("NETID", b+"");
			conditionMap.put("ID", b+"");
			return update(LfWXUploadFile.class, objectMap, conditionMap);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"保存对象时失败了");
		}
		return false;

	}

}

