package com.montnets.emp.common.biz;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IGenericDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * 处理SP账号的Biz类（整合方法）
 * @author cg
 * @date 2018-12-27 10:24:29
 */
public class SpUserBiz {
    private final IGenericDAO genericDAO = new DataAccessDriver().getGenericDAO();
    /**
     * 获取SP账号
     * @param userId 操作员Id
     * @return SP账号
     */
    
    public String getAllSpUser(String userId){
        if(StringUtils.isEmpty(userId) && !",".equals(userId)){
            return userId;
        }
        //去掉最后的逗号
        if(userId.endsWith(",")) {
            userId = userId.substring(0, userId.lastIndexOf(","));
        }
        //获取操作员对应的SP账号
        String[] userIdArray = userId.split(",");
        List<String> spUserList = new ArrayList<String>();
        for(String tempUserId : userIdArray){
            List<DynaBean> tempSpUserList = getSpUser(tempUserId);
            if(tempSpUserList != null && tempSpUserList.size()>0){
                handleDynaBeanList(tempSpUserList, spUserList);
            }
        }
        //获取操作员接入账号的SP账号
        List<DynaBean> tempExtraSpList = getExtraSPUserByUid(userId);
        if(tempExtraSpList != null && tempExtraSpList.size()>0){
            handleDynaBeanList(tempExtraSpList, spUserList);
        }
        //List去重
        HashSet tempSpSet = new HashSet(spUserList);
        spUserList.clear();
        spUserList.addAll(tempSpSet);

        //将List转为String字符串
        StringBuilder tempSpUserSb = new StringBuilder();
        for(String tempSpUser : spUserList){
            //需要加上单引号
            tempSpUserSb.append("'").append(tempSpUser).append("'").append(",");
        }
        return tempSpUserSb.deleteCharAt(tempSpUserSb.lastIndexOf(",")).toString();
    }

    /**
     * 获取非接入账号SP账号
     * @param userId 操作员Id
     * @return sp账号集合
     */
    public List<DynaBean> getSpUser(String userId){
        List<DynaBean> dynaBeanList;
        //查询用户绑定的SP账号
        dynaBeanList = getSPUserByUid(userId);
        //如果该账号未绑定SP账号
        if(dynaBeanList == null ||dynaBeanList.size() == 0){
            //查询操作员当前机构绑定的SP账号
            dynaBeanList = getDepSPUserByUid(userId);
        }
        if(dynaBeanList == null ||dynaBeanList.size() == 0){
            //查询操作员所有父级机构绑定的SP账号
            dynaBeanList = getSuperDepUserById(userId);
        }
        if(dynaBeanList == null ||dynaBeanList.size() == 0){
            //公共SP账号
            dynaBeanList = getCommonSPUserByUid();
        }
        return dynaBeanList;
    }

    /**
     * 查询未绑定的SP账号
     * @return sp账号集合
     */
    private List<DynaBean> getCommonSPUserByUid(){
        List<DynaBean> dynaBeanList = null;
        try{
            String sql = "SELECT USERID  AS SPUSERID FROM USERDATA usrdata LEFT JOIN LF_ACCOUNT_BIND bind ON usrdata.USERID = bind.SPUSERID WHERE SPUSERID IS NULL AND usrdata.SPTYPE <> 2";
            dynaBeanList = getDynaBeanBySql(sql);
        }catch(Exception e){
            EmpExecutionContext.error(e,"查询未绑定的SP账号失败");
        }
        return dynaBeanList;
    }

    /**
     * 根据userId查询上级机构绑定的SP账号
     * @param userId 操作员Id
     * @return sp账号集合
     */
    private List<DynaBean> getSuperDepUserById(String userId){
        List<DynaBean> dynaBeanList = null;
        try{
            String sql = "SELECT dep.DEP_PATH AS DEPPATH FROM LF_SYSUSER sysuser INNER JOIN LF_DEP dep ON sysuser.DEP_ID = dep.DEP_ID WHERE USER_ID = " + userId;
            dynaBeanList = getDynaBeanBySql(sql);
            if (dynaBeanList != null && dynaBeanList.size()>0){
                String depPath = dynaBeanList.get(0).get("deppath").toString();
                String [] superPathArr = depPath.split("/");
                List<DynaBean> tempDynaBean;
                //从后面到前面循环,因为越后面的越接近当前机构
                for(int i =superPathArr.length-1;i>=0;i--){
                    //根据DEPID查询绑定的SP账号
                    tempDynaBean = getDepSpUserByDepId(superPathArr[i]);
                    if(tempDynaBean !=null && tempDynaBean.size()>0){
                        dynaBeanList = tempDynaBean;
                        break;
                    }else{
                        dynaBeanList = null;
                    }
                }
            }
        }catch(Exception e){
            EmpExecutionContext.error(e,"根据操作员ID获取上级机构绑定的SP账号");
        }
        return dynaBeanList;
    }

    private List<DynaBean> getDepSpUserByDepId(String depId){
        List<DynaBean> dynaBeanList = null;
        try{
            String sql = "SELECT SPUSERID FROM LF_ACCOUNT_BIND WHERE DEP_CODE = "+ depId;
            dynaBeanList = getDynaBeanBySql(sql);
        }catch(Exception e){
            EmpExecutionContext.error(e,"根据机构ID获取绑定的SP账号");
        }
        return dynaBeanList;
    }

    /**
     * 根据userId查询当前机构绑定的SP账号
     * @param userId 操作员Id
     * @return sp账号集合
     */
    private List<DynaBean> getDepSPUserByUid(String userId){
        List<DynaBean> dynaBeanList = null;
        try{
            String sql = "SELECT SPUSERID FROM LF_ACCOUNT_BIND WHERE DEP_CODE = (SELECT dep.DEP_ID FROM LF_SYSUSER sysuser"+
                    " INNER JOIN LF_DEP dep ON sysuser.DEP_ID = dep.DEP_ID WHERE USER_ID ="+userId+") ";
            dynaBeanList = getDynaBeanBySql(sql);
        }catch(Exception e){
            EmpExecutionContext.error(e,"根据操作员ID获取当前切的绑定SP账号");
        }
        return dynaBeanList;
    }

    /**
     * 根据userId查询用户绑定的SP账号
     * @param userId 操作员Id
     * @return sp账号集合
     */
    private List<DynaBean> getSPUserByUid(String userId){
        List<DynaBean> dynaBeanList = null;
        try{
            String sql = "SELECT SPUSERID FROM LF_ACCOUNT_BIND WHERE SYS_GUID = (SELECT GUID FROM LF_SYSUSER WHERE USER_ID = "+userId+")";
            dynaBeanList = getDynaBeanBySql(sql);
        }catch(Exception e){
            EmpExecutionContext.error(e,"根据guId获取该操作员绑定的SP账号信息失败");
        }
        return dynaBeanList;
    }

    /**
     * 查询接入账号的SP账号
     * @param userIdStr 操作员Id
     * @return sp账号集合
     */
    private List<DynaBean> getExtraSPUserByUid(String userIdStr){
        List<DynaBean> dynaBeanList = null;
        try{
            String sql = "SELECT SPUSERID FROM LF_MT_PRI WHERE USER_ID IN(" + userIdStr + ")";
            dynaBeanList = getDynaBeanBySql(sql);
        }catch(Exception e){
            EmpExecutionContext.error(e,"查询接入账号的SP账号");
        }
        return dynaBeanList;
    }

    private void handleDynaBeanList(List<DynaBean> dynaBeans, List<String> spUserList){
        for(DynaBean dynaBean : dynaBeans){
            if(dynaBean.get("spuserid")!=null){
                //将查询所得的SP账号添加入集合中
                spUserList.add(dynaBean.get("spuserid").toString());
            }
        }
    }

    private List<DynaBean> getDynaBeanBySql(String sql){
        List<DynaBean> dynaBeanList = null;
        try{
            dynaBeanList = genericDAO.findDynaBeanBySql(sql);
        }catch(Exception e){
            EmpExecutionContext.error(e,"查询未绑定的SP账号失败");
        }
        return dynaBeanList;
    }
    
    /**
     * 根据sp账号获取sp账号信息
     * @param userId
     * @return
     */
    public List<DynaBean> getUserData(String userId){

        List<DynaBean> dynaBeanList = null;
        try{
            String sql = "SELECT * FROM userdata WHERE USERID = '" + userId + "'";
            dynaBeanList = getDynaBeanBySql(sql);
        }catch(Exception e){
            EmpExecutionContext.error(e,"查询接入账号的SP账号");
        }
        return dynaBeanList;
    }
}
