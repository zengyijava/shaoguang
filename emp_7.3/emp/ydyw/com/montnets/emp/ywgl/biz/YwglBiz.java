package com.montnets.emp.ywgl.biz;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IEmpTransactionDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.ydyw.LfBusPackage;
import com.montnets.emp.entity.ydyw.LfBusPkgeTaoCan;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.ywgl.dao.YwglDao;

/**
 * 业务包BIZ
 * @todo TODO
 * @project	emp
 * @author WANGRUBIN
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2015-1-13 下午03:22:25
 * @description
 */
public class YwglBiz extends BaseBiz  {

	private YwglDao dao=new YwglDao();
	private IEmpTransactionDAO empTransactionDAO=new DataAccessDriver().getEmpTransDAO();
	
	/**
	 * 查询业务
	 */
	public List<DynaBean> getBusList(String searStr) throws Exception {
		// TODO Auto-generated method stub
		return dao.getBusList(searStr);
	}

	/**
	 * 新增业务包
	 */
	public boolean doAdd(LfBusPackage pkg,String bussids) throws Exception {
		boolean add=false;
		int depId=0;
		try {
			LfSysuser user=new BaseBiz().getLfSysuserByUserId( pkg.getUserId()+"") ;
			if(user!=null){
				depId=user.getDepId().intValue();
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取用户信息异常。");
		}
		String[] ids=bussids.split(",");
		Connection conn =null;
		conn= empTransactionDAO.getConnection();
		try {
			empTransactionDAO.beginTransaction(conn);
			pkg.setDepId(depId);
			Long pkgId=empTransactionDAO.saveObjectReturnID(conn, pkg);
			for(int i=0;i<ids.length;i++){
				if(!"".equals(ids[i])){
					LfBusPkgeTaoCan taocan = new LfBusPkgeTaoCan();
					taocan.setPackageCode(pkg.getPackageCode());
					taocan.setBusCode(ids[i]);
					taocan.setAssociateType(1);
					taocan.setCorpCode(pkg.getCorpCode());
					taocan.setCreateTime(new Timestamp(System.currentTimeMillis()));
					add=empTransactionDAO.save(conn, taocan);
				}
			}
			empTransactionDAO.commitTransaction(conn);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"保存业务包信息异常!");
			empTransactionDAO.rollBackTransaction(conn);
			return false;
		}finally{
			empTransactionDAO.closeConnection(conn);
			
		}	
		return add;
	}
	
	/**
	 * 新增业务包
	 */
	public boolean doEdit(LfBusPackage pkg,String bussids) throws Exception {
		boolean add=false;
		
		String[] ids=bussids.split(",");
		
		Connection conn =null;
		conn= empTransactionDAO.getConnection();
		try {
			empTransactionDAO.beginTransaction(conn);
			
			//更新业务包记录
			empTransactionDAO.update(conn, pkg);
			
			Integer pkgId=pkg.getPackageId();
			
			//删除子表记录
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("packageCode", pkg.getPackageCode());
			empTransactionDAO.delete(conn, LfBusPkgeTaoCan.class, conditionMap);
			
			//重新添加子表记录
			for(int i=0;i<ids.length;i++){
				if(!"".equals(ids[i])){
					LfBusPkgeTaoCan taocan = new LfBusPkgeTaoCan();
					taocan.setPackageCode(pkg.getPackageCode());
					taocan.setBusCode(ids[i]);
					taocan.setAssociateType(1);
					taocan.setCorpCode(pkg.getCorpCode());
					taocan.setCreateTime(new Timestamp(System.currentTimeMillis()));
					add=empTransactionDAO.save(conn, taocan);
				}
			}
			empTransactionDAO.commitTransaction(conn);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"保存业务包信息异常!");
			empTransactionDAO.rollBackTransaction(conn);
			return false;
		}finally{
			empTransactionDAO.closeConnection(conn);
			
		}	
		return add;
	}	

	/**
	 * 业务包查询
	 */
	public List<DynaBean> getPkgList(LinkedHashMap<String, String> conditionMap,PageInfo pageInfo) throws Exception {
		// TODO Auto-generated method stub
		return dao.getPkgList(conditionMap,pageInfo);
	}

	/**
	 * 业务信息查询
	 */
	public List<DynaBean> getBusInfoList(String packCode) throws Exception {
		// TODO Auto-generated method stub
		return dao.getBusInfoList(packCode);
	}
	
	

}
