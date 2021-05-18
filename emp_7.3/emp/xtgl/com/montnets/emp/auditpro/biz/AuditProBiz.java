package com.montnets.emp.auditpro.biz;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.auditpro.dao.AuditProDAO;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.sysuser.LfFlow;
import com.montnets.emp.entity.sysuser.LfFlowBindObj;
import com.montnets.emp.entity.sysuser.LfFlowBindType;
import com.montnets.emp.entity.sysuser.LfReviewer2level;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.util.PageInfo;
/**
 *  审核流程管理逻辑类 
 * @author Administrator
 *
 */
public class AuditProBiz extends SuperBiz {
	
	/**
	 * 查询根据列表条件所对应的流程模板列表
	 * @description    
	 * @param conditionMap
	 * @param lfsysuser
	 * @param pageInfo
	 * @return       			 
	 * @author liuxiangheng <xiaokanrensheng1012@126.com>
	 * @datetime 2016-9-8 下午05:50:41
	 */
	public List<DynaBean> findFlows(LinkedHashMap<String, String> conditionMap,LfSysuser lfsysuser,PageInfo pageInfo) {
		List<DynaBean> beanList = null;
		try{
			beanList = new AuditProDAO().findFlowsbyMap(conditionMap,lfsysuser,pageInfo);
		}catch (Exception e) {
			EmpExecutionContext.error(e,"查询根据列表条件所对应的流程模板列表出现异常！");
			beanList = null;
		}
		return beanList;
	}
	
	
	public Long getFIDnumber(){
		Long c = Long.valueOf(String.valueOf(System.currentTimeMillis()).substring(5));
		return c;
	}
	
	
	/**
	 *  创建审核流程
	 * @param flowtask	审核流程名称
	 * @param audstr	审核对象
	 * @param items	审核范围
	 * @param comment	备注
	 * @param pathtype	判断操作界面， 是新   创建  还是上一步
	 * @param updatelfflow	需要修改审核流程
	 * @param addbindtypestr	需要修改审核流程时增加的审核范围
	 * @param cutbindtypestr	需要修改审核流程时减少的审核范围
	 * @return
	 */
	public String addAuditPro(String flowtask,String audstr,String items,String comment,String divlevel,String pathtype,
			LfFlow updatelfflow,LfSysuser lfsysuser,String addbindtypestr,String cutbindtypestr,List<LfFlowBindObj> flowbindList) {
		Connection conn = empTransDao.getConnection();
		String returnmsg = "";
		String returnId = "";
		try{
			empTransDao.beginTransaction(conn);
			Long f_id = null;
			//新建操作
			if("1".equals(pathtype)){
				//创建审核流程的基本信息
				LfFlow lfflow = new LfFlow();
				lfflow.setFTask(flowtask);
				lfflow.setCorpCode(lfsysuser.getCorpCode());
				lfflow.setComments(comment);
				lfflow.setCreateUserId(lfsysuser.getUserId());
				lfflow.setCreateTime(new Timestamp(System.currentTimeMillis()));
				lfflow.setUpdateTime(new Timestamp(System.currentTimeMillis()));
				lfflow.setRLevelAmount(Integer.valueOf(divlevel));
				//f_id = getFIDnumber();
				//empTransDao.save(conn, lfflow);
				lfflow.setFlowshow("  ");
				lfflow.setFType(0);
				lfflow.setMsgAccount(0L);
				lfflow.setRemindState(0);
				lfflow.setUserId("  ");
				lfflow.setFlowState(1);
				f_id = empTransDao.saveObjProReturnID(conn, lfflow);
				lfflow.setFId(f_id);
			//删除操作
			}else if("2".equals(pathtype)){
				//修改审核对应 
				empTransDao.update(conn, updatelfflow);
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				conditionMap.put("FId",String.valueOf(updatelfflow.getFId()));
				empTransDao.delete(conn, LfFlowBindType.class, conditionMap);
				empTransDao.delete(conn, LfReviewer2level.class, conditionMap);
				if(cutbindtypestr != null && !"".equals(cutbindtypestr)){
					cutbindtypestr = cutbindtypestr.substring(0, cutbindtypestr.length()-1);
					conditionMap.clear();
					conditionMap.put("infoType",cutbindtypestr);
					conditionMap.put("corpCode",lfsysuser.getCorpCode());
					conditionMap.put("FId",String.valueOf(updatelfflow.getFId()));
					empTransDao.delete(conn, LfFlowBindObj.class, conditionMap);
				}
				f_id = updatelfflow.getFId();
			}
			returnId = f_id +"";
			//审核流程绑定类型（lf_flowBindtype）
			//信息类型。3：短信模板；4：彩信模板；1：短信发送；2：彩信发送；
			List<LfFlowBindType> flowtypelist = new ArrayList<LfFlowBindType>(); 
			if(!"".equals(items)){
				String[] audittype = items.split(",");
				for(String temp:audittype){
					if(temp != null && !"".equals(temp)){
						try{
							LfFlowBindType flowbindtype = new LfFlowBindType();
							flowbindtype.setFId(f_id);
							flowbindtype.setUpdateTime(new Timestamp(System.currentTimeMillis()));
							flowbindtype.setCreateTime(new Timestamp(System.currentTimeMillis()));
							flowbindtype.setInfoType(Integer.valueOf(temp));
							flowtypelist.add(flowbindtype);
						}catch (Exception e) {
							EmpExecutionContext.error(e,"审核流程绑定类型新增记录出现异常！");
						}
					}else{
						continue;
					}
				}
			}
			if(flowtypelist != null && flowtypelist.size()>0){
				empTransDao.save(conn, flowtypelist, LfFlowBindType.class);
			}
			List<LfReviewer2level> reviewerList = new ArrayList<LfReviewer2level>(); 
			String[] auditobj = audstr.split("#");
			for(int i=0;i<auditobj.length;i++){
				String obj = auditobj[i];
				if(obj != null && !"".equals(obj)){
					String[] objarr = obj.split("_");
					//标识的是索引 1是操作员 2逐级  3机构
					String selindex = objarr[0];
					//是全部审核还是单一个ok
					String selcheck = objarr[2];
					//所对应的对象
					String selobj = objarr[1];
					// 1 3 标识的是操作员以及机构
					if("1".equals(selindex) || "3".equals(selindex)){
						Integer audittype = 0;
						if("1".equals(selindex)){
							audittype = 1;
						}else if("3".equals(selindex)){
							audittype = 4;
						}
						String[] arr = selobj.split(",");
						for(int j=0;j<arr.length;j++){
							String temp = arr[j];
							if(temp != null && !"".equals(temp)){
								LfReviewer2level level = new LfReviewer2level();
								level.setFId(f_id);
								//审核人级别
								level.setRLevel(i+1);
								//审核类型，1-操作员，2-群组，3-上级，4-机构审核员,5-逐级审核
								level.setRType(audittype);
								//1全部通过生效2第一人审核生效
								level.setRCondition(Integer.valueOf(selcheck));
								level.setUserId(Long.valueOf(temp));
								reviewerList.add(level);
							}
						}
					
					//标识的是逐级审核
					}else if("2".equals(selindex)){
						LfReviewer2level level = new LfReviewer2level();
						level.setFId(f_id);
						//审核人级别
						level.setRLevel(i+1);
						//审核类型，1-操作员，2-群组，3-上级，4-机构审核员,5-逐级审核
						level.setRType(5);
						//1全部通过生效2第一人审核生效
						level.setRCondition(Integer.valueOf(selcheck));
						level.setUserId(0L);
						reviewerList.add(level);
					}
				}
			}
			if(reviewerList != null && reviewerList.size()>0){
				empTransDao.save(conn, reviewerList, LfReviewer2level.class);
			}
			List<LfFlowBindObj> bingobjList = new ArrayList<LfFlowBindObj>();
			if(pathtype != null && "2".equals(pathtype)){
				//String cutbindtypestr,List<LfFlowBindObj> flowbindList
				LfFlowBindObj flowobj = null;
				if(addbindtypestr != null && !"".equals(addbindtypestr)){
					//addbindtypestr = addbindtypestr.substring(0, addbindtypestr.length()-1);
					String[] arr = addbindtypestr.split(",");
					if(arr != null && arr.length>0){
						for(String temp:arr){
							if(flowbindList != null && flowbindList.size()>0){
								for(LfFlowBindObj obj:flowbindList){
									flowobj = new LfFlowBindObj();
									flowobj.setFId(f_id);
									flowobj.setObjType(obj.getObjType());
									flowobj.setObjCode(obj.getObjCode());
									flowobj.setInfoType(Integer.valueOf(temp));
									flowobj.setCreateTime(new Timestamp(System.currentTimeMillis()));
									flowobj.setUpdateTime(new Timestamp(System.currentTimeMillis()));
									flowobj.setCorpCode(lfsysuser.getCorpCode());
									bingobjList.add(flowobj);
								}
							}
						}
					}
				}
			}
			//新增审核范围，把该审核流程下的审核对象全部赋予该审核范围
			if(bingobjList != null && bingobjList.size()>0){
				empTransDao.save(conn, bingobjList, LfFlowBindObj.class);
			}
			empTransDao.commitTransaction(conn);
			returnmsg = "success#"+returnId;
		}catch (Exception e) {
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e,"创建审核流程出现异常！");
			returnmsg = "fail#"+returnId;
			
		}finally{
			empTransDao.closeConnection(conn);
		}
		return returnmsg;
	}


    /**
     * 添加或修改被审核对象
     * @param depidstr 机构ids
     * @param useridstr 操作员ids
     * @param flowid 审核流程id
     * @param pathtype 1 创建审核流程的下一步  2是完成的上一步
     * @param bindtypebuffer 审核范围
     * @param lfsysuser 操作员对象
     * @return
     */
	public String addupdateAuditObj(String depidstr,String useridstr,String flowid,String pathtype,String bindtypebuffer,LfSysuser lfsysuser){
		String returnmsg = "fail";
		Connection conn = empTransDao.getConnection();
		try{	
			String[] arr = null;
			//String buffer = "";
			if(bindtypebuffer != null && bindtypebuffer.length()>0){
				arr = bindtypebuffer.split(",");
			//	buffer = bindtypebuffer.substring(0, bindtypebuffer.length()-1);
			}
			if(arr == null){
				return "fail";
			}
			empTransDao.beginTransaction(conn);
			LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String, String>();
			//如果是修改，则先删除该流程绑定的原审核对象
			if("2".equals(pathtype)){
				conditionMap.put("FId",flowid);
				conditionMap.put("corpCode", lfsysuser.getCorpCode());
				empTransDao.delete(conn, LfFlowBindObj.class, conditionMap);
			}
		/*	//删除选择操作员在该审核范围内的绑定关系
			if(useridstr != null && !"".equals(useridstr)){
				useridstr = useridstr.substring(0, useridstr.length()-1);
				conditionMap.clear();
				conditionMap.put("infoType",buffer);
				conditionMap.put("ObjType","1");
				conditionMap.put("ObjCode",useridstr);
				empTransDao.delete(conn, LfFlowBindObj.class, conditionMap);
			}
			//删除选择机构在该审核范围内的绑定关系
			if(depidstr != null && !"".equals(depidstr)){
				depidstr = depidstr.substring(0, depidstr.length()-1);
				conditionMap.clear();
				conditionMap.put("infoType",buffer);
				conditionMap.put("ObjType","2");
				conditionMap.put("ObjCode",depidstr);
				empTransDao.delete(conn, LfFlowBindObj.class, conditionMap);
			}*/
			List<LfFlowBindObj> bingobjList = new ArrayList<LfFlowBindObj>();
			LfFlowBindObj flowobj = null;
			//新增选择的 在该审核范围内的绑定关系
			if(depidstr != null && !"".equals(depidstr)){
				String[] deparr = depidstr.split(",");
				for(int i=0;i<deparr.length;i++){
					if(deparr[i] != null && !"".equals(deparr[i])){
						for(String temp:arr){
							if(temp != null && !"".equals(temp)){
								flowobj = new LfFlowBindObj();
								flowobj.setFId(Long.valueOf(flowid));
								flowobj.setObjType(2);
								flowobj.setObjCode(deparr[i]);
								flowobj.setInfoType(Integer.valueOf(temp));
								flowobj.setCreateTime(new Timestamp(System.currentTimeMillis()));
								flowobj.setUpdateTime(new Timestamp(System.currentTimeMillis()));
								flowobj.setCorpCode(lfsysuser.getCorpCode());
								bingobjList.add(flowobj);
							}
						}
					}
				}
			}
			if(useridstr != null && !"".equals(useridstr)){
				String[] userarr = useridstr.split(",");
				for(int i=0;i<userarr.length;i++){
					if(userarr[i] != null && !"".equals(userarr[i])){
						for(String temp:arr){
							if(temp != null && !"".equals(temp)){
								flowobj = new LfFlowBindObj();
								flowobj.setFId(Long.valueOf(flowid));
								flowobj.setObjType(1);
								flowobj.setObjCode(userarr[i]);
								flowobj.setInfoType(Integer.valueOf(temp));
								flowobj.setCreateTime(new Timestamp(System.currentTimeMillis()));
								flowobj.setUpdateTime(new Timestamp(System.currentTimeMillis()));
								flowobj.setCorpCode(lfsysuser.getCorpCode());
								bingobjList.add(flowobj);
							}
						}	
					}
				}
			}
			if(bingobjList != null && bingobjList.size()>0){
				empTransDao.save(conn,bingobjList,LfFlowBindObj.class);
			}
			
			empTransDao.commitTransaction(conn);
			returnmsg = "success";
		}catch (Exception e) {
			empTransDao.rollBackTransaction(conn);
			returnmsg = "fail";
			EmpExecutionContext.error(e,"操作审核对象出现异常！");
		}finally{
			empTransDao.closeConnection(conn);
		}
		return returnmsg;
	}

    /**
     * 添加或修改被审核对象
     * @param depidstr 机构ids
     * @param depcontainstr 机构包含关系
     * @param useridstr 操作员ids
     * @param flowid 审核流程id
     * @param pathtype 1 创建审核流程的下一步  2是完成的上一步
     * @param bindtypebuffer 审核范围
     * @param lfsysuser 操作员对象
     * @return
     */
    public String addupdateAuditObj(String depidstr,String depcontainstr,String useridstr,String flowid,String pathtype,String bindtypebuffer,LfSysuser lfsysuser){
        String returnmsg = "fail";
        Connection conn = empTransDao.getConnection();
        try{
            String[] arr = null;
            if(bindtypebuffer != null && bindtypebuffer.length()>0){
                arr = bindtypebuffer.split(",");
            }
            if(arr == null){
                return "fail";
            }
            empTransDao.beginTransaction(conn);
            LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String, String>();
            //如果是修改，则先删除该流程绑定的原审核对象
            if("2".equals(pathtype)){
                conditionMap.put("FId",flowid);
                conditionMap.put("corpCode", lfsysuser.getCorpCode());
                empTransDao.delete(conn, LfFlowBindObj.class, conditionMap);
            }

            List<LfFlowBindObj> bingobjList = new ArrayList<LfFlowBindObj>();
            LfFlowBindObj flowobj = null;
            //新增选择的 在该审核范围内的绑定关系
            if(depidstr != null && !"".equals(depidstr)){
                String[] deparr = depidstr.split(",");
                String[] containarr = depcontainstr.split(",");
                for(int i=0;i<deparr.length;i++){
                    if(deparr[i] != null && !"".equals(deparr[i])){
                        for(String temp:arr){
                            if(temp != null && !"".equals(temp)){
                                flowobj = new LfFlowBindObj();
                                flowobj.setFId(Long.valueOf(flowid));
                                flowobj.setObjType(2);
                                flowobj.setObjCode(deparr[i]);
                                flowobj.setCtsubDep(Integer.parseInt(containarr[i]));
                                flowobj.setInfoType(Integer.valueOf(temp));
                                flowobj.setCreateTime(new Timestamp(System.currentTimeMillis()));
                                flowobj.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                                flowobj.setCorpCode(lfsysuser.getCorpCode());
                                bingobjList.add(flowobj);
                            }
                        }
                    }
                }
            }
            if(useridstr != null && !"".equals(useridstr)){
                String[] userarr = useridstr.split(",");
                for(int i=0;i<userarr.length;i++){
                    if(userarr[i] != null && !"".equals(userarr[i])){
                        for(String temp:arr){
                            if(temp != null && !"".equals(temp)){
                                flowobj = new LfFlowBindObj();
                                flowobj.setFId(Long.valueOf(flowid));
                                flowobj.setObjType(1);
                                flowobj.setObjCode(userarr[i]);
                                flowobj.setCtsubDep(0);
                                flowobj.setInfoType(Integer.valueOf(temp));
                                flowobj.setCreateTime(new Timestamp(System.currentTimeMillis()));
                                flowobj.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                                flowobj.setCorpCode(lfsysuser.getCorpCode());
                                bingobjList.add(flowobj);
                            }
                        }
                    }
                }
            }
            if(bingobjList != null && bingobjList.size()>0){
                empTransDao.save(conn,bingobjList,LfFlowBindObj.class);
            }

            empTransDao.commitTransaction(conn);
            returnmsg = "success";
        }catch (Exception e) {
            empTransDao.rollBackTransaction(conn);
            returnmsg = "fail";
            EmpExecutionContext.error(e,"操作审核对象出现异常！");
        }finally{
            empTransDao.closeConnection(conn);
        }
        return returnmsg;
    }
	
	/**
	 *   删除该审核流程 
	 * @param flowid
	 * @param lfSysuser
	 * @return
	 */
	public String deleteAuditPro(String flowid,LfSysuser lfSysuser) {
		Connection conn = empTransDao.getConnection();
		String msg = "fail";
		try{
			empTransDao.beginTransaction(conn);
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("FId",flowid);
			empTransDao.delete(conn, LfFlowBindType.class, conditionMap);
			empTransDao.delete(conn, LfReviewer2level.class, conditionMap);
			conditionMap.put("corpCode", lfSysuser.getCorpCode());
			empTransDao.delete(conn, LfFlowBindObj.class, conditionMap);
			empTransDao.delete(conn, LfFlow.class,conditionMap);
			empTransDao.commitTransaction(conn);
			msg = "success";
		}catch (Exception e) {
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e,"删除审核流程出现异常！");
		}finally{
			empTransDao.closeConnection(conn);
		}
			return msg;
	}

	
	
	
	
	
	
	
}
