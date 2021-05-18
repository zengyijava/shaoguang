package com.montnets.emp.rms.dao;

import java.util.ArrayList;
import java.util.List;

import com.montnets.emp.common.constant.EMPException;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.rms.vo.UserDataVO;

/**
 * @author Jason Huang
 * @date 2018年1月19日 下午4:52:30
 */

public class UserDataDAO extends SuperDAO{

	public List<UserDataVO> getSPUserList(String cropCode) {
		List<UserDataVO> userDataList = null;
		String sql1 = "SELECT DISTINCT(USERDATA.USERID),USERDATA.USERPASSWORD FROM LF_ACCOUNT_BIND INNER JOIN USERDATA ON LF_ACCOUNT_BIND.SPUSERID = USERDATA.USERID WHERE USERDATA.STATUS = 0 AND USERDATA.ACCABILITY&0x00000004=4 AND ACCOUNTTYPE = 1 AND LF_ACCOUNT_BIND.CORP_CODE = "
				+ cropCode;
		String sql2 = "SELECT USERDATA.USERID,USERDATA.USERPASSWORD FROM LF_SP_DEP_BIND INNER JOIN USERDATA ON LF_SP_DEP_BIND.SPUSER = USERDATA.USERID WHERE USERDATA.STATUS = 0 AND USERDATA.ACCABILITY&0x00000004=4 AND ACCOUNTTYPE = 1 AND LF_SP_DEP_BIND.CORP_CODE = "
				+ cropCode;
		try {
			userDataList = findVoListBySQL(UserDataVO.class, sql1, StaticValue.EMP_POOLNAME);
			if (userDataList.size() <= 0) {
				userDataList = findVoListBySQL(UserDataVO.class, sql2, StaticValue.EMP_POOLNAME);
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "DAO层-UserData数据查询异常!");
		}
		return userDataList;
	}

	/**
	 * 因为标准版SP账号与MBOSS无关，所以提交模板审核需要MBOSS开出的运营商账号审核
	 * 托管版SP账号也是由MBOSS开出，所以提交模板审核用SP账号
	 * @param cropCode 企业编码
	 */
	public UserDataVO getSPUser(String cropCode) {
		List<UserDataVO> userDataList = new ArrayList<UserDataVO>();
		String sql2;
		String sql1;
		try {
			if (0 == StaticValue.getCORPTYPE()) {
				String sql = "SELECT B.SPACCID AS USERID, B.SPACCPWD AS USERPASSWORD FROM USERDATA A, A_GWACCOUNT B WHERE A.USERID = B.PTACCID AND A.STATUS=0 AND A.ACCOUNTTYPE != 2 AND B.SPTYPE = 0 ORDER BY	B.UPDATETIME DESC";
				userDataList = findVoListBySQL(UserDataVO.class, sql, StaticValue.EMP_POOLNAME);
			} else {
				// 兼容不同数据库 1 oracle ; 2 SQL Server2005 ; 3 MySQL ; 4 DB2
				switch (StaticValue.DBTYPE) {
					case 1:
						sql1 = "SELECT USERDATA.USERID,USERDATA.USERPASSWORD FROM LF_ACCOUNT_BIND INNER JOIN USERDATA "
								+ "ON LF_ACCOUNT_BIND.SPUSERID = USERDATA.USERID WHERE USERDATA.STATUS = 0 AND USERDATA.ACCABILITY&0x00000004=4 AND ACCOUNTTYPE = 1 "
								+ "AND LF_ACCOUNT_BIND.CORP_CODE = "
								+ cropCode
								+ " AND ROWNUM <= 1";
						sql2 = "SELECT USERDATA.USERID,USERDATA.USERPASSWORD FROM LF_SP_DEP_BIND INNER JOIN USERDATA "
								+ "ON LF_SP_DEP_BIND.SPUSER = USERDATA.USERID WHERE USERDATA.STATUS = 0 AND USERDATA.ACCABILITY&0x00000004=4 AND ACCOUNTTYPE = 1 "
								+ "AND LF_SP_DEP_BIND.CORP_CODE = "
								+ cropCode
								+ " AND ROWNUM <= 1";
						break;
					case 2:
						sql1 = "SELECT TOP 1 USERDATA.USERID,USERDATA.USERPASSWORD FROM LF_ACCOUNT_BIND INNER JOIN USERDATA "
								+ "ON LF_ACCOUNT_BIND.SPUSERID = USERDATA.USERID WHERE USERDATA.STATUS = 0 AND USERDATA.ACCABILITY&0x00000004=4 AND ACCOUNTTYPE = 1 AND LF_ACCOUNT_BIND.CORP_CODE = "
								+ cropCode;
						sql2 = "SELECT TOP 1 USERDATA.USERID,USERDATA.USERPASSWORD FROM LF_SP_DEP_BIND INNER JOIN USERDATA "
								+ "ON LF_SP_DEP_BIND.SPUSER = USERDATA.USERID WHERE USERDATA.STATUS = 0 AND USERDATA.ACCABILITY&0x00000004=4 AND ACCOUNTTYPE = 1 AND LF_SP_DEP_BIND.CORP_CODE = "
								+ cropCode;
						break;
					case 3:
						sql1 = "SELECT USERDATA.USERID,USERDATA.USERPASSWORD FROM LF_ACCOUNT_BIND INNER JOIN USERDATA "
								+ "ON LF_ACCOUNT_BIND.SPUSERID = USERDATA.USERID WHERE USERDATA.STATUS = 0 AND USERDATA.ACCABILITY&0x00000004=4 AND ACCOUNTTYPE = 1 "
								+ "AND LF_ACCOUNT_BIND.CORP_CODE = "
								+ cropCode
								+ " LIMIT 0,1";
						sql2 = "SELECT USERDATA.USERID,USERDATA.USERPASSWORD FROM LF_SP_DEP_BIND INNER JOIN USERDATA "
								+ "ON LF_SP_DEP_BIND.SPUSER = USERDATA.USERID WHERE USERDATA.STATUS = 0 AND USERDATA.ACCABILITY&0x00000004=4 AND ACCOUNTTYPE = 1 "
								+ "AND LF_SP_DEP_BIND.CORP_CODE = "
								+ cropCode
								+ " LIMIT 0,1";
						break;
					case 4:
						sql1 = "SELECT USERID,USERPASSWORD "
								+ "FROM (SELECT USERDATA.USERID,USERDATA.USERPASSWORD,ROW_NUMBER() OVER(ORDER BY UID DESC) AS ROWNUM "
								+ "FROM LF_ACCOUNT_BIND INNER JOIN USERDATA ON "
								+ "LF_ACCOUNT_BIND.SPUSERID = USERDATA.USERID WHERE "
								+ "USERDATA.STATUS = 0 AND USERDATA.ACCABILITY&0x00000004=4 AND ACCOUNTTYPE = 1 "
								+ "AND LF_ACCOUNT_BIND.CORP_CODE = " + cropCode
								+ ")" + "WHERE ROWNUM <= 1";
						sql2 = "SELECT USERID,USERPASSWORD "
								+ "FROM (SELECT USERDATA.USERID,USERDATA.USERPASSWORD,ROW_NUMBER() OVER(ORDER BY UID DESC) AS ROWNUM "
								+ "FROM LF_SP_DEP_BIND INNER JOIN USERDATA ON "
								+ "LF_SP_DEP_BIND.SPUSER = USERDATA.USERID WHERE "
								+ "USERDATA.STATUS = 0 AND USERDATA.ACCABILITY&0x00000004=4 AND ACCOUNTTYPE = 1 "
								+ "AND LF_SP_DEP_BIND.CORP_CODE = " + cropCode
								+ ")" + "WHERE ROWNUM <= 1";
						break;
					default:
						throw new EMPException("DAO层-UserData数据查询异常,无法识别的数据库类型！");
				}
				userDataList = findVoListBySQL(UserDataVO.class, sql1, StaticValue.EMP_POOLNAME);
				if (userDataList.size() <= 0) {
					userDataList = findVoListBySQL(UserDataVO.class, sql2, StaticValue.EMP_POOLNAME);
				}
			}
			if (userDataList == null || userDataList.size() <= 0) {
				return null;
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "DAO层-UserData数据查询异常!");
		}
		return userDataList.get(0);
	}
	/**
	 * 同步RCOS的公共模板没有SP账号(不属于任何企业)，使用MBOSS开出的运营商账号
	 */
	public UserDataVO getCommonSPUser() {
		List<UserDataVO> userDataList = new ArrayList<UserDataVO>();
		try {
			// STATSUS = 0 已启用，1-已失效 ，USERTYPE = 1 为后端账号，ACCOUNTTYPE = 2 为彩信账号
			String sql = "SELECT B.SPACCID AS USERID, B.SPACCPWD AS USERPASSWORD FROM USERDATA A, A_GWACCOUNT B WHERE A.USERID = B.PTACCID AND A.STATUS=0 AND A.USERTYPE =1 AND A.ACCOUNTTYPE != 2 ORDER BY	B.UPDATETIME DESC";
			userDataList = findVoListBySQL(UserDataVO.class, sql, StaticValue.EMP_POOLNAME);
			if(userDataList == null || userDataList.size() <= 0){
				return null;
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "DAO层-UserData数据查询异常!");
		}
		return userDataList.get(0);
	}

}
