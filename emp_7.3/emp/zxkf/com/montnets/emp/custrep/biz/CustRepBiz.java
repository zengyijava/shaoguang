package com.montnets.emp.custrep.biz;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.util.PageInfo;
import com.montnets.emp.custrep.dao.CustRepDao;

public class CustRepBiz
{

    public List<DynaBean> getEvaluates(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo)
    {
        CustRepDao custDao = new CustRepDao();
        return custDao.getEvaluates(conditionMap, pageInfo);
    }
    
    public List<DynaBean> getServerTime(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo)
    {
        CustRepDao custDao = new CustRepDao();
        return custDao.getServerTime(conditionMap, pageInfo);
    }
}
