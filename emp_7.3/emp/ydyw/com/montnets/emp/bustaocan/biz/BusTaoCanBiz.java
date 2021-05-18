package com.montnets.emp.bustaocan.biz;

import java.sql.Connection;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.bustaocan.dao.BusTaoCanDAO;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IEmpTransactionDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.ydyw.LfBusPkgeTaoCan;
import com.montnets.emp.entity.ydyw.LfBusTaoCan;
import com.montnets.emp.entity.ydyw.LfContractTaocan;
import com.montnets.emp.entity.ydyw.LfProCharges;
import com.montnets.emp.util.PageInfo;



public class BusTaoCanBiz extends SuperBiz {
	
	BusTaoCanDAO dao=new BusTaoCanDAO();
	IEmpTransactionDAO empTransactionDAO=new DataAccessDriver().getEmpTransDAO();
	/**
	 * 根据条件查询列表
	 * @param conditionMap
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getTaoCanRecord(LinkedHashMap<String, String> conditionMap,PageInfo pageInfo) throws Exception{
		return dao.getTaoCanRecord(conditionMap,pageInfo);
	}
	
	
	/**
	 *  保存业务套餐，保存三个表
	 * @param lfBusTaoCan 套餐实体	
	 * @param lfProCharges 规则实体
	 * @param  packageids 包ID
	 * @return
	 */
	public boolean save(LfBusTaoCan lfBusTaoCan,LfProCharges lfProCharges,String packagecodes){
		boolean ret=false;
		Connection conn =null;
		long  dep_id=0;
		try {
			LfSysuser user=new BaseBiz().getLfSysuserByUserId(lfBusTaoCan.getUser_id()+"") ;
			if(user!=null){
				dep_id=user.getDepId();
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取用户信息异常。");
		}
		conn= empTransactionDAO.getConnection();

		try {
			empTransactionDAO.beginTransaction(conn);
			lfBusTaoCan.setDep_id(dep_id);
			empTransactionDAO.save(conn, lfBusTaoCan);
			lfProCharges.setDepid(dep_id);
			ret=empTransactionDAO.save(conn, lfProCharges);
			
			if(packagecodes!=null&&!"".equals(packagecodes)){
				String[] ids=packagecodes.split(",");
				for(int i=0;i<ids.length;i++){
					if("".equals(ids[i])){
						continue;
					}
				LfBusPkgeTaoCan relation=new LfBusPkgeTaoCan();
				//套餐编码
				relation.setTaocanCode(lfBusTaoCan.getTaocan_code());
				//关联类型（0：套餐对应业务包；1：业务包对应业务）
				relation.setAssociateType(0);
				relation.setCorpCode(lfBusTaoCan.getCorp_code()+"");
				relation.setCreateTime(new Timestamp(System.currentTimeMillis()));
				//业务包编码
				relation.setPackageCode(ids[i]);
				ret=empTransactionDAO.save(conn, relation);	
				}
			}
			empTransactionDAO.commitTransaction(conn);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"保存套餐记录异常!");
			empTransactionDAO.rollBackTransaction(conn);
			return ret;
		}finally{
			empTransactionDAO.closeConnection(conn);

		}
		return ret;
	}
	
	/**
	 *  保存业务套餐，保存三个表
	 * @param lfBusTaoCan 套餐实体	
	 * @param lfProCharges 规则实体
	 * @param  packageids 包ID
	 * @return
	 */
	public boolean update(LfBusTaoCan lfBusTaoCan,LfProCharges lfProCharges,String packagecodes){
		boolean ret=false;
		Connection conn =null;
		long  dep_id=0;
		try {
			LfSysuser user=new BaseBiz().getLfSysuserByUserId(lfBusTaoCan.getUser_id()+"") ;
			if(user!=null){
				dep_id=user.getDepId();
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取用户信息异常。");
		}
		//获取签约客户套餐对应关系表
			List<LfContractTaocan> list=null;
		try {
			LinkedHashMap<String, String> Map = new LinkedHashMap<String, String>();
			Map.put("taocancode", lfBusTaoCan.getTaocan_code());
			Map.put("isvalid", "0");// 有效标识，0-正常 1-禁用
			list=new BaseBiz().getByCondition(LfContractTaocan.class, Map, null);
		} catch (Exception e1) {
			EmpExecutionContext.error(e1, "获取用户信息异常。");
		}
		
		conn= empTransactionDAO.getConnection();

		try {
			empTransactionDAO.beginTransaction(conn);
			lfBusTaoCan.setDep_id(dep_id);
			empTransactionDAO.update(conn, lfBusTaoCan);
			lfProCharges.setDepid(dep_id);
			ret=empTransactionDAO.update(conn, lfProCharges);
			//同时修改签约客户套餐对应关系表的扣费时间的天上面的数字
			if(list!=null){
				int buckledate=lfProCharges.getBuckledate();
				for(int k=0;k<list.size();k++){
					LfContractTaocan cont=list.get(k);
					Timestamp time= cont.getBucklefeetime();
					//列如：2015-01-22 16:54:19:68500000 修改天数
					String[] strtime=(time+"").split(" ");
					if(strtime.length==2){
						//取得年月
						String yearMonth=strtime[0].substring(0, strtime[0].lastIndexOf("-"));
						//16:54:19:68500000 去掉毫秒
						String hms=strtime[1].substring(0, strtime[1].indexOf("."));
						//如果日期是一位需要前面加个0
						String strbuckledate=buckledate+"";
						if(strbuckledate.length()==1){
							strbuckledate="0"+strbuckledate;
						}
						//形成新的时间
                        //为了解决在jdk1.7下 TimeStamp.valueOf("2019-03-00 00:00:00")转换时间出错的问题
                        if("00".equals(strbuckledate)) {
                            String realDay = yearMonth + "-" + "01" + " " + hms;
                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date date = dateFormat.parse(realDay);
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(date);
                            calendar.add(Calendar.DAY_OF_MONTH, -1);
                            Timestamp timestamp = new Timestamp(calendar.getTime().getTime());
                            cont.setBucklefeetime(timestamp);
                        } else {
                            String newDate=yearMonth+"-"+strbuckledate+" "+hms;//增加一个空格
						    cont.setBucklefeetime(Timestamp.valueOf(newDate));
                        }
						empTransactionDAO.update(conn, cont);
					}
					
				}
			}
			
			
			//先删除关联关系
			if(packagecodes!=null&&!"".equals(packagecodes)){
				String[] ids=packagecodes.split(",");
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				conditionMap.put("taocanCode", lfBusTaoCan.getTaocan_code());
				empTransactionDAO.delete(conn, LfBusPkgeTaoCan.class, conditionMap);
				for(int i=0;i<ids.length;i++){
					if("".equals(ids[i])){
						continue;
					}
				LfBusPkgeTaoCan relation=new LfBusPkgeTaoCan();
				//套餐编码
				relation.setTaocanCode(lfBusTaoCan.getTaocan_code());
				//关联类型（0：套餐对应业务包；1：业务包对应业务）
				relation.setAssociateType(0);
				relation.setCorpCode(lfBusTaoCan.getCorp_code()+"");
				relation.setCreateTime(new Timestamp(System.currentTimeMillis()));
				//业务包编码
				relation.setPackageCode(ids[i]);
				ret=empTransactionDAO.save(conn, relation);	
				}
			}else {
				return true;
			}
			empTransactionDAO.commitTransaction(conn);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"保存套餐记录异常!");
			empTransactionDAO.rollBackTransaction(conn);
			return false;
		}finally{
			empTransactionDAO.closeConnection(conn);

		}
		return ret;
	}
	
	
	/***
	 * 获得套餐列表
	 * @return
	 */
	public List<DynaBean> getTCList(String name){
		return dao.getTCList(name);
	}
	
	/***
	 * 通过code查询关联关系，然后查询出已经绑定的套餐
	 * @param code
	 * @return
	 */
	public List<DynaBean> getListByCode(String code){
		return dao.getListByCode(code);
	}
	
}