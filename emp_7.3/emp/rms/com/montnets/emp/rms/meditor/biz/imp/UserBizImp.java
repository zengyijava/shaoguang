package com.montnets.emp.rms.meditor.biz.imp;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.client.LfClientDep;
import com.montnets.emp.entity.corp.LfWebconfig;
import com.montnets.emp.entity.employee.LfEmployeeDep;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.rms.entity.LfDfadvanced;
import com.montnets.emp.rms.meditor.biz.UserBiz;
import com.montnets.emp.rms.meditor.dao.UserDao;
import com.montnets.emp.rms.meditor.dao.imp.UserDaoImp;
import com.montnets.emp.rms.dao.SameMmsDao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class UserBizImp extends BaseBiz implements UserBiz {
    UserDao userDao = new UserDaoImp();
    @Override
    public boolean hasCreatePublicTemp(Long userId, HttpServletRequest request, HttpServletResponse response) {
        if (null == userId){
            EmpExecutionContext.error("获取用户id异常");
            return false;
        }

        Integer count = userDao.hasCreatePublicTemp(userId);
        if (null != count && count > 0 ){
            return true;
        }
        return false;
    }
    @Override
    public String getAuthority(Long userId, HttpServletRequest request, HttpServletResponse response){
        if (null == userId){
            EmpExecutionContext.error("获取用户id异常");
            return null;
        }
        String authority = userDao.getAuthority(userId);
        return authority;
    }

    @Override
    public String getModulePer(Long userId, HttpServletRequest request, String corpCode, String configName) {
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        conditionMap.put("configName", configName);
        conditionMap.put("corpCode", corpCode);
        List<LfWebconfig> lfWebconfigs = null;
        String modulePer ="";
        try {
            lfWebconfigs = empDao.findListByCondition(LfWebconfig.class, conditionMap, null);
        } catch (Exception e) {
            EmpExecutionContext.error(e,"LF_WEBCONFIG");
        }
        if(null != lfWebconfigs && lfWebconfigs.size() > 0){
            modulePer = lfWebconfigs.get(0).getJsonConfig();
        }
        return modulePer;
    }

    @Override
    public LfSysuser getUserByCondition(LinkedHashMap<String, String> conditionMap) {
        LfSysuser sysuser = null;
        try {
            List<LfSysuser> users = empDao.findListByCondition(LfSysuser.class, conditionMap, null);
            if(null != users && users.size() > 0){
               sysuser = users.get(0);
            }
        } catch (Exception e) {
          EmpExecutionContext.error(e,"查询用户表出现异常！");
        }
        return  sysuser;
    }

    /**
     * 处理员工机构的整合
     *
     * @param phoneStr   手机号
     * @param empDepIds  员工机构Id集合
     * @param lgcorpcode 企业编码
     * @return 手机号字符串
     */
    @Override
    public String getEmployeePhoneSrrByDepId(String phoneStr, String empDepIds, String lgcorpcode) {
        try{
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            //通过员工机构id查找电话
            String[] empDepArr = empDepIds.split(",");
            StringBuilder noContainIds = new StringBuilder();
            StringBuilder containIds = new StringBuilder();
            List<String> empdepPathList = new ArrayList<String>();
            for (String id : empDepArr) {
                if (!"".equals(id)) {
                    if (id.contains("e")) {
                        //处理包含子机构
                        id = id.substring(1, id.length());
                        containIds.append(id).append(",");
                    } else {
                        //处理不包含子机构
                        noContainIds.append(id).append(",");
                    }
                }
            }
            if(!"".equals(containIds.toString()) && containIds.length()>0){
                containIds = new StringBuilder(containIds.substring(0, containIds.length() - 1));
                conditionMap.clear();
                conditionMap.put("depId&in", containIds.toString());
                List<LfEmployeeDep> empDepList  = empDao.findListBySymbolsCondition(LfEmployeeDep.class, conditionMap, null);
                if(empDepList != null && empDepList.size()>0){
                    for (LfEmployeeDep anEmpDepList : empDepList) {
                        empdepPathList.add(anEmpDepList.getDeppath());
                    }
                }
                else{
                    EmpExecutionContext.error("企业富信相同内容发送选择员工机构发送，不存在此机构，机构ID为:"+ containIds);
                    //返回原始手机号码字符串
                    return phoneStr;
                }
            }
            String returnEmpPhones = this.getEmployeePhoneByDepIds(noContainIds.toString(), empdepPathList,lgcorpcode);
            phoneStr = phoneStr + returnEmpPhones;
        }catch (Exception e) {
            EmpExecutionContext.error(e,"企业富信相同内容发送操作员工机构出现异常！");
        }
        return phoneStr;
    }

    /**
     * 处理客户机构的整合
     *
     * @param phoneStr   手机号
     * @param cliDepIds  客户机构Id集合
     * @param lgcorpcode 企业编码
     * @return 手机号字符串
     */
    @Override
    public String getClientPhoneStrByDepId(String phoneStr, String cliDepIds, String lgcorpcode) {
        try{
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            String[] clientDepArr = cliDepIds.split(",");
            StringBuilder noContainIds = new StringBuilder();
            StringBuilder containIds = new StringBuilder();
            List<String> clientdepPathList = new ArrayList<String>();
            for (String id : clientDepArr) {
                if (!"".equals(id)) {
                    if (id.contains("e")) {
                        //处理包含子机构
                        id = id.substring(1, id.length());
                        containIds.append(id).append(",");
                    } else {
                        //处理不包含子机构
                        noContainIds.append(id).append(",");
                    }
                }
            }
            if(!"".equals(containIds.toString()) && containIds.length()>0){
                containIds = new StringBuilder(containIds.substring(0, containIds.length() - 1));
                conditionMap.clear();
                conditionMap.put("depId&in", containIds.toString());
                List<LfClientDep> clientDepList  = empDao.findListBySymbolsCondition(LfClientDep.class, conditionMap, null);
                if(clientDepList != null && clientDepList.size()>0){
                    for (LfClientDep lfClientDep : clientDepList) {
                        clientdepPathList.add(lfClientDep.getDeppath());
                    }
                }
                else{
                    EmpExecutionContext.error("静态彩信发送选择客户机构发送，不存在此机构，机构ID为:"+containIds);
                    //返回原始手机号码字符串
                    return phoneStr;
                }
            }
            String returnClinetPhones = this.getClientPhoneByDepIds(noContainIds.toString(), clientdepPathList,lgcorpcode);
            phoneStr = phoneStr + returnClinetPhones;
        }catch (Exception e) {
            EmpExecutionContext.error(e,"移动彩讯操作客户机构出现异常！");
        }
        return phoneStr;
    }

    /**
     * 处理群组的整合
     *
     * @param phoneStr 手机号
     * @param groupIds 群组
     * @return 手机号字符串
     */
    @Override
    public String getGroupPhoneStrById(String phoneStr, String groupIds) {
        try{
            groupIds = groupIds.substring(0,groupIds.length() - 1);
            String[] groupIdsList = groupIds.split(",");
            StringBuilder phoneStrBuilder = new StringBuilder(phoneStr);
            for (String s : groupIdsList) {
                String tempPhoneStr = this.getGroupUserPhoneByIds(s);
                phoneStrBuilder.append(tempPhoneStr);
            }
            phoneStr = phoneStrBuilder.toString();
        }catch (Exception e) {
            EmpExecutionContext.error(e,"移动彩讯处理群组出现异常！");
        }
        return phoneStr;
    }
    /**
     *   处理员工机构查询其员工号码
     * @param depIdStr 员工机构
     * @param depPathList 机构路径
     * @return 手机号
     */
    private String getEmployeePhoneByDepIds(String depIdStr ,List<String> depPathList, String corpCode){
        //存放手机号码的list
        String phoneStr;
        try{
            phoneStr = new SameMmsDao().findEmployeePhoneByDepIds(depIdStr, depPathList,corpCode);
        }catch (Exception e) {
            phoneStr = "";
            EmpExecutionContext.error(e,"企业富信相同内容发送处理员工机构获取员工手机号码出现异常！");
        }
        //返回
        return phoneStr;
    }
    /**
     *   处理客户机构获取客户手机号码
     * @param depIdStr 客户机构
     * @param depPathList 机构路径
     * @return 手机号
     */
    private String getClientPhoneByDepIds(String depIdStr ,List<String> depPathList,String corpCode){
        //存放手机号码的list
        String phoneStr;
        try{
            phoneStr = new SameMmsDao().getClientPhoneByDepIds(depIdStr, depPathList,corpCode);
        }catch (Exception e) {
            phoneStr = "";
            EmpExecutionContext.error(e,"企业富信相同内容发送处理客户机构获取客户手机号码出现异常！");
        }
        //返回
        return phoneStr;
    }
    /**
     *   获取群组中的人员的手机号码LIST
     * @param groupId 群组Id
     * @return 手机号
     */
    private String getGroupUserPhoneByIds(String groupId){
        //存放手机号码的list
        String phoneStr;
        try{
            //替换下单引号，解决sql注入问题
            phoneStr = new SameMmsDao().getClientGroupUserById(groupId.replaceAll("'",""));
        }catch (Exception e) {
            phoneStr = "";
            EmpExecutionContext.error(e,"企业富信相同内容发送获取群组中的人员的手机号码出现异常！");
        }
        //返回
        return phoneStr;
    }

    /**
     * 高级设置存为默认
     * @param conditionMap 删除原来设置条件
     * @param lfDfadvanced 更新默认高级设置对象
     * @return 成功为"success"，失败为"fail"
     */
    @Override
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
                result = "success";
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
