package com.montnets.emp.shorturl.surlmanage.biz;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.shorturl.surlmanage.dao.UrlDomainDao;
import com.montnets.emp.shorturl.surlmanage.dao.UrlDomianBindDao;
import com.montnets.emp.shorturl.surlmanage.entity.LfDomain;
import com.montnets.emp.shorturl.surlmanage.entity.LfDomainCorp;
import com.montnets.emp.shorturl.surlmanage.vo.LfDomainCorpVo;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.StringUtils;

public class UrlDomianBindBiz extends SuperBiz{
	BaseBiz baseBiz=new BaseBiz();
	public List<LfDomainCorpVo> getDomainCorpVos(LinkedHashMap<String, String> conditionMap,PageInfo pageInfo){
		List<LfDomainCorpVo > urlList = null;
		try {

			urlList = new UrlDomianBindDao().getDomainCorpVos(conditionMap,pageInfo);

		} catch (Exception e) {
			EmpExecutionContext.error(e, "查询短域名绑定异常");
		}
		return urlList;
	}
	
	public boolean insert(String addcorpCode, String domainId,Long createUid,Long updateUid,String opUser){
		boolean flag = false;
		try {

			flag = new UrlDomianBindDao().insert(addcorpCode, domainId,createUid,updateUid,opUser);

		} catch (Exception e) {
			EmpExecutionContext.error(e, "查询短域名绑定异常");
		}
		return flag;
	}
	
	public boolean delete(String domainIds,String addcorpCode){
		boolean flag = false;
		try {

			flag = new UrlDomianBindDao().delete(domainIds,addcorpCode);

		} catch (Exception e) {
			EmpExecutionContext.error(e, "查询短域名绑定异常");
		}
		return flag;
	}
	
	
	public boolean updateTime(String addcorpCode,Long updateUid) {
		boolean flag = false;
		try {

			flag = new UrlDomianBindDao().updateTime(addcorpCode,updateUid);

		} catch (Exception e) {
			EmpExecutionContext.error(e, "查询短域名绑定异常");
		}
		return flag;
	}
	
	/**
	 * 根据企业编码查找域名id
	 * @param conditionMap
	 * @return
	 */
	public List<Long>  getDomainBindIds(LinkedHashMap<String, String> conditionMap){
		List<Long> list = new ArrayList<Long>();
		try {

			list = new UrlDomianBindDao().getDomainBindIds(conditionMap);

		} catch (Exception e) {
			EmpExecutionContext.error(e, "插入短域名绑定异常");
		}
		return list;
	}
	
	
	public List<LfDomain> getDomains(String ids){
		List<LfDomain> domains = null;
		try {

			domains = new UrlDomianBindDao().getDomains(ids);

		} catch (Exception e) {
			EmpExecutionContext.error(e, "插入短域名绑定异常");
		}
		return domains;
	}
	
	public boolean changeState(Long id,String corpCode,Integer state) throws Exception
	{
		boolean flag = false;
		if(id == null)
		{
			return false;
		}
//		Integer resultState = null;
//		LfDomainCorp domainCorp = null;
//		
//		//条件map 
//		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		
		Connection conn = empTransDao.getConnection();
		PreparedStatement ps=null;
		try
		{	
			String sql="UPDATE lf_domain_corp set FLAG=? where DOMAIN_ID=? AND CORP_CODE=?";
			ps=conn.prepareStatement(sql);
			ps.setInt(1, state);
			ps.setString(2, String.valueOf(id));
			ps.setString(3, corpCode);
			int count=ps.executeUpdate();
//			conditionMap.put("domainId", String.valueOf(id));
//			conditionMap.put("corpCode", corpCode);
//			domainCorp = baseBiz.getByCondition(LfDomainCorp.class, conditionMap, null).get(0);
//			
//			if(domainCorp.getFlag() == 0)
//			{
//				domainCorp.setFlag(1);
//			}
//			else if(domainCorp.getFlag() == 1)
//			{
//				domainCorp.setFlag(0);
//			}
//
//			empTransDao.beginTransaction(conn);
//			empTransDao.update(conn, domainCorp);
//
//			empTransDao.commitTransaction(conn);
			if(count>0){
				flag = true;
			}else{
				flag = false;
			}
		}
		catch (Exception e)
		{
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e, "更改域名绑定状态成功。");
			throw e;
		}
		finally
		{
			if(ps != null){
				try{
					ps.close();
				}catch(SQLException e){
					EmpExecutionContext.error(e, "PreparedStatement关闭异常");
				}
			}
			empTransDao.closeConnection(conn);
		}
		return flag;
	}
	
	public List<LfDomain> getDomainsPageinfo(String ids,PageInfo pageInfo)throws Exception{
		List<LfDomain> domains = null;
		try {

			domains = new UrlDomianBindDao().getDomainsPageinfo(ids, pageInfo);

		} catch (Exception e) {
			EmpExecutionContext.error(e, "插入短域名绑定异常");
		}
		return domains;
	}

    public List<LfDomain> getBindedDomains(String corpCode, List<LfDomain> availableList) {
        List<Long> availableIds = null;
	    if(availableList != null && availableList.size() > 0) {
	        availableIds = new ArrayList<Long>();
	        for(LfDomain domain : availableList) {
	            availableIds.add(domain.getId());
            }
        }

        String idStr = idList2Str(availableIds);

	    List<LfDomain> bindedDomains = null;
        try {
            bindedDomains = convertDynas2LfDomains(new UrlDomianBindDao().getBindedDomains(corpCode, idStr));
        } catch (Exception e) {
            EmpExecutionContext.error(e, "根据可用域名列表查找已经绑定的短域名信息失败！");
        }
        return bindedDomains;
    }

    public List<LfDomain> getAvailableDomains(String corpCode, PageInfo pageInfo) {
        List<LfDomain> availableDomains = null;
        try {
            availableDomains = convertDynas2LfDomains(new UrlDomianBindDao().getAvailableDomains(corpCode, pageInfo));
        } catch (Exception e) {
            EmpExecutionContext.error(e, "根据企业编码:" + corpCode + ",查找可用的的短域名信息失败！");
        }
        return availableDomains;
    }


    /**
     *
     * @param corpCode 当前企业编码
     * @param userid 当前操作员ID
     * @param oldList 已绑定域名ID的历史集合
     * @param newList 前端页面修改之后，要绑定域名ID的集合
     */
    public int updateBinds(String corpCode, Long userid, String username, String oldList, String newList) {
        List<LfDomain> domainList = null;
        List<LfDomainCorp> domainCorpList = null;
        int count = 0;
        try {
            if(StringUtils.isNotBlank(newList)) {
                domainList = convertDynas2LfDomains(new UrlDomainDao().getDomainByIds(newList));
            }
            domainCorpList = assembleDomainCorps(corpCode, userid, username, domainList);
            count = new UrlDomianBindDao().updateBinds(corpCode, oldList, domainCorpList);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "根据企业编码:" + corpCode + ",更新企业域名绑定关系失败");
        }
        return count;
    }

    /**
     *
     * @param corpCode
     * @param userid
     * @param username
     * @param domainList
     * @return
     */
    private List<LfDomainCorp> assembleDomainCorps(String corpCode, Long userid, String username, List<LfDomain> domainList) {
        List<LfDomainCorp> lfDomainCorps = null;
        if(domainList != null && domainList.size() > 0) {
            lfDomainCorps = new ArrayList<LfDomainCorp>();
            for(LfDomain domain : domainList) {
                Timestamp timestamp = new Timestamp(new Date().getTime());
                LfDomainCorp lfDomainCorp = new LfDomainCorp();
                lfDomainCorp.setDomainId(domain.getId());
                lfDomainCorp.setCorpCode(corpCode);
                lfDomainCorp.setFlag(0);
                lfDomainCorp.setUpdateUid(userid);
                lfDomainCorp.setCreateUid(userid);
                lfDomainCorp.setCreateUser(username);
                lfDomainCorp.setCreateTm(timestamp);
                lfDomainCorp.setUpdateTm(timestamp);
                lfDomainCorps.add(lfDomainCorp);
            }
        }
        return lfDomainCorps;
    }

    /**
     *
     * @param returnList
     * @return
     */
    private List<LfDomain> convertDynas2LfDomains(List<DynaBean> returnList) {
	    List<LfDomain> domainList = null;
	    if(returnList != null && returnList.size() > 0) {
	        domainList = new ArrayList<LfDomain>();
	        for(DynaBean bean : returnList) {
	            LfDomain lfDomain = new LfDomain();
	            lfDomain.setId(bean.get("id") != null ? Long.parseLong(bean.get("id").toString()) : null );
	            lfDomain.setDomain(bean.get("domain") != null ? bean.get("domain").toString() : null);
                lfDomain.setLenExten(bean.get("len_exten") != null ? Long.parseLong(bean.get("len_exten").toString()) : null);
                lfDomain.setDtype(bean.get("dtype") != null ? Integer.parseInt(bean.get("dtype").toString()) : null);
                domainList.add(lfDomain);
            }
        }
        return domainList;
    }

    /**
     * @func List[1,2,3,4] -> String"1,2,3,4"
     * @param availableIds
     * @return
     */
    private String idList2Str(List<Long> availableIds) {
	    StringBuffer stringBuffer = new StringBuffer();
	    if(availableIds != null && availableIds.size() > 0) {
	        for(Long id : availableIds) {
	            stringBuffer.append(id).append(",");
            }
            stringBuffer.deleteCharAt(stringBuffer.lastIndexOf(","));
        }
	    return stringBuffer.toString();
    }

    /**
     *
     * @param bindedDomains
     * @return
     */
    public String getBindedIdStrFromList(List<LfDomain> bindedDomains) {
        StringBuffer stringBuffer = null;
        if(bindedDomains != null && bindedDomains.size() > 0) {
            stringBuffer = new StringBuffer();
            for(LfDomain lfDomain : bindedDomains) {
                stringBuffer.append(lfDomain.getId()).append(",");
            }
            stringBuffer.deleteCharAt(stringBuffer.lastIndexOf(","));
            return stringBuffer.toString();
        }
        return null;
    }
}
