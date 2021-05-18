package com.montnets.emp.netnews.daoImpl;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.netnews.entity.LfWXSORT;
import com.montnets.emp.netnews.entity.LfWXUploadFile;


public class Wx_netAttachedDaoImpl extends BaseBiz{

	/**
	 * 获得lf_wx_sort表ID
	 * 
	 * @param filetype
	 *            文件类型
	 * @param parentid
	 *            父结点ID
	 * @param type类型
	 *            ，区分网讯模板、素材管理
	 * @return 返回lf_wx_sort表ID
	 */
	public Long getFiletypeByid(int filetype, int parentid, int type) {
		Long id = -1L;
		try {
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("FILETYPE", String.valueOf(filetype));
			conditionMap.put("parentId", String.valueOf(parentid));
			conditionMap.put("TYPE", String.valueOf(type));
			List<LfWXSORT> sorts = getByCondition(LfWXSORT.class, conditionMap, null);
			if(sorts!=null && sorts.size()>0)
			{
				id =  sorts.get(0).getID();
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"查询lf_wx_sort表");
		}
		return id;
	}


	/**
	 * 文件上传
	 */
	public boolean UploadFile(String filename, String filedes, String filedi,
			String fileURL, Long uploadUserid, Long fType, String nameTemp,
			int scfication, int usertype) throws SQLException {
		try {
			boolean result = false;
			LfWXUploadFile file = new LfWXUploadFile();
			file.setFILENAME(filename);
			file.setFILEVER(filedi);
			file.setFILEDESC(filedes);
			file.setUPLOADDATE(new Timestamp(System.currentTimeMillis()));
			file.setUPLOADUSERID(uploadUserid);
			file.setWEBURL(fileURL);
			file.setSTATUS(0);
			file.setCREATID(uploadUserid);
			file.setSORTID(fType.toString());
			file.setNAMETEMP(nameTemp);
			file.setType(String.valueOf(scfication));
			Long id = addObjReturnId(file);
			file.setID(id);
			file.setNETID(id);
			result = updateObj(file);
			return result;
		} catch (Exception e) {
			EmpExecutionContext.error(e,"文件上传");
			return false;
		}
	}



}
