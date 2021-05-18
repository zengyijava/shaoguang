package com.montnets.emp.common.biz;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SelectUserInfoDao;
import com.montnets.emp.common.vo.GroupInfoVo;
import com.montnets.emp.common.vo.GrpupInfoDto;
import com.montnets.emp.common.vo.LfCustFieldValueVo;
import com.montnets.emp.entity.client.LfClient;
import com.montnets.emp.entity.client.LfClientDep;
import com.montnets.emp.entity.employee.LfEmployee;
import com.montnets.emp.entity.employee.LfEmployeeDep;
import com.montnets.emp.entity.group.LfUdgroup;
import com.montnets.emp.entity.segnumber.PbServicetype;
import com.montnets.emp.util.PageInfo;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang.StringUtils;

import java.util.*;

public class SelectUserInfoBiz extends SuperBiz{
    private BaseBiz baseBiz = new BaseBiz();
    private SelectUserInfoDao dao = new SelectUserInfoDao();
    /**
     * 通过员工机构id查找树
     * @param userId 操作员Id
     * @param depId 机构Id
     * @param corpCode 企业编码
     * @return
     */
    public List<LfEmployeeDep> getEmpSecondDepTreeByUserIdorDepId(String userId, String depId, String corpCode) {
        List<LfEmployeeDep> deps = null;
        try{
            //调用Dao查询数据
            deps = dao.getEmpSecondDepTreeByUserIdorDepId(userId,depId,corpCode);
        }catch (Exception e) {
            EmpExecutionContext.error(e,"移动彩讯获取员工机构出现异常！");
        }
        return deps;
    }

    /**
     * 通过客户机构id查找树
     * @param userId 操作员Id
     * @param depId 机构Id
     * @param corpCode 企业编码
     * @return
     */
    public List<LfClientDep> getCliSecondDepTreeByUserIdorDepId(String userId, String depId, String corpCode) {
        List<LfClientDep> deps = null;
        try{
            //调用DAO查询数据
            deps = dao.getCliSecondDepTreeByUserIdorDepId(userId,depId,corpCode);
        }catch (Exception e) {
            EmpExecutionContext.error(e,"移动彩讯获取客户机构出现异常！");
        }
        return deps;
    }

    public List<GrpupInfoDto> getGroupInfoListByGroupId(String groupType,String userId, String corpCode)throws Exception{
        List<LfUdgroup> lfUdgroupList;
        //存储对象
        List<GrpupInfoDto> grpupInfoDtos = new ArrayList<GrpupInfoDto>();
        LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
        LinkedHashMap<String,String> orderbyMap = new LinkedHashMap<String,String>();
        //登录的用户ID
        conditionMap.put("receiver", userId);
        //群组Id
        conditionMap.put("gpAttribute",groupType);
        //排序
        orderbyMap.put("udgId", StaticValue.ASC);
        //群组列表
        lfUdgroupList = baseBiz.getByCondition(LfUdgroup.class, conditionMap, orderbyMap);
        if(lfUdgroupList != null && lfUdgroupList.size()>0) {
            StringBuilder udgIds = new StringBuilder();
            for (LfUdgroup udg : lfUdgroupList) {
                udgIds.append(udg.getGroupid().toString()).append(",");
            }
            //获取群组id字符串
            udgIds = new StringBuilder(udgIds.substring(0, udgIds.length() - 1));
            //群组类型 1是员工群组  2是客户群组
            Integer groupFlag = "0".endsWith(groupType)? 1 : 2;
            Map<String, String> countMap = new GroupBiz().getGroupMemberCount(udgIds.toString(), groupFlag, corpCode);
            for (LfUdgroup lfUdgroup : lfUdgroupList) {
                GrpupInfoDto grpupInfoDto = new GrpupInfoDto();
                //共享类型 0为独有  1为共享
                Integer shareType = lfUdgroup.getSharetype();
                grpupInfoDto.setShareType(StringUtils.defaultIfEmpty(shareType.toString(), "0"));
                //群组人数
                String groupCount = countMap.get(lfUdgroup.getGroupid().toString());
                grpupInfoDto.setGroupCount(StringUtils.defaultIfEmpty(groupCount, "0"));
                //groupType  1 是员工群组  2是客户群组
                grpupInfoDto.setGroupType("0".equals(groupType)?1:2);
                grpupInfoDto.setIsDep(3);
                grpupInfoDto.setGroupId(lfUdgroup.getGroupid());
                grpupInfoDto.setUdgId(lfUdgroup.getUdgId());
                grpupInfoDto.setGroupName(lfUdgroup.getUdgName());
                grpupInfoDtos.add(grpupInfoDto);
            }
        }
        return grpupInfoDtos;
    }

    /**
     * 根据groupId查询群组用户
     * @param groupId 群组Id
     * @param pageInfo 分页对象
     * @param type 群组类型
     * @return
     */
    public List<GroupInfoVo> getGroupUser(Long groupId, PageInfo pageInfo, String type) {
        List<GroupInfoVo> groupVosList = new ArrayList<GroupInfoVo>();
        try {
            if("1".equals(type)){
                //查询员工群组
                groupVosList = dao.findGroupUserByIds(groupId,pageInfo);
            }else if("2".equals(type)){
                //查询客户群组
                groupVosList = dao.findGroupClientByIds(groupId, pageInfo);
            }
        } catch (Exception e){
            EmpExecutionContext.error(e,"移动彩讯获取群组信息出现异常！");
        }
        return groupVosList;
    }

    /**
     *    查询客户机构人员
     * @param clientDep	当前机构对象
     * @param containType	是否包含  1包含   2不包含
     * @param pageInfo	分页
     * @return
     * @throws Exception
     */
    public List<DynaBean> getClientsByDepId(LfClientDep clientDep, Integer containType, PageInfo pageInfo) throws Exception{
        List<DynaBean> beanList = new ArrayList<DynaBean>();
        try{
            beanList = dao.findClientsByDepId(clientDep, containType, pageInfo);
        }catch (Exception e) {
            EmpExecutionContext.error(e,"查询客户机构人员出现异常！");
        }
        return beanList;
    }

    public List<DynaBean> findClientByName(String name, String corpCode) {
        List<DynaBean> beanList = new ArrayList<DynaBean>();
        try{
            beanList = dao.findClientByName(name, corpCode);
        }catch (Exception e) {
            EmpExecutionContext.error(e,"根据名字查询客户机构人员出现异常！");
        }
        return beanList;
    }

    public List<GroupInfoVo> findGroupByName(String name, Long lguserid, String chooseType,String udgId) {
        List<GroupInfoVo> groupInfoVos = new ArrayList<GroupInfoVo>();
        try {
            if("3".equals(chooseType)){
                groupInfoVos = dao.findAllGroupByName(name, lguserid,udgId);
            }else if("4".equals(chooseType)){
                groupInfoVos = dao.findEmployeeGroupByName(name, lguserid,udgId);
            }else if("5".equals(chooseType)){
                groupInfoVos = dao.findClientGroupByName(name, lguserid,udgId);
            }
        }catch (Exception e) {
            EmpExecutionContext.error(e,"根据名字查询群组人员出现异常！");
        }
        return groupInfoVos;
    }

    /**
     *   获取机构客户 人数
     * @param clientDep	机构对象
     * @param containType 1包含  2是不包含
     * @return 人数
     * @throws Exception
     */
    public Integer getDepClientCount(LfClientDep clientDep, Integer containType) throws Exception{
        Integer count = 0;
        try{
            count = dao.findClientsCountByDepId(clientDep, containType);
        }catch (Exception e) {
            EmpExecutionContext.error(e,"获取机构客户 人数出现异常！");
        }
        return count;
    }

    public int getClientCountByFieldRef(String corpCode, String fieldRef) throws Exception{
        int count = 0;
        List<LfClient> clientList = null;
        try {
            // 获取客户信息
            clientList = dao.findClientByFieldRef(corpCode, fieldRef);
            count = clientList == null ? 0 : clientList.size();
        } catch (Exception e) {
            EmpExecutionContext.error(e,"获取客户信息失败！");
            // 异常处理
            throw e;
        }
        return count;
    }

    public List<LfCustFieldValueVo> getCustVos(LfCustFieldValueVo lfCustFieldValueVo) throws Exception{
        List<LfCustFieldValueVo> dataVosList;
        try {
            dataVosList = dao.findLfCustFieldValueVo(lfCustFieldValueVo);

        } catch (Exception e) {
            EmpExecutionContext.error(e,"获取属性值失败！");
            throw e;
        }
        return dataVosList;
    }

    public Map<String,String> getSignClientMemberCount(String tcCodes, String corpCode) {
        //定义一个Map  key群组ID value群组对应的成员数量
        HashMap<String, String> countMap = new HashMap<String, String>();
        try {
            //查询出一个动态bean
            List<DynaBean>  groupMenberCountList = dao.getsignClientMemberCount(tcCodes,corpCode);
            //动态bean不为空并且动态bean的size大于0，则进行循环，否则countMap就为空的map了。
            if(groupMenberCountList != null && groupMenberCountList.size()>0) {
                for(DynaBean groupMenberCount:groupMenberCountList) {
                    //key为群组ID,Value为群组成员数量
                    countMap.put(String.valueOf(groupMenberCount.get("taocancode")),String.valueOf(groupMenberCount.get("membercount")));
                }
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "查询出套餐的成员数量异常！tcCodes:" + tcCodes );
        }
        return countMap;
    }

    public List<DynaBean> getSignClientMember(String searchName, String tcCode, PageInfo pageInfo) {
        List<DynaBean> beans = new ArrayList<DynaBean>();
        try {
            beans = dao.findSignClientMember(searchName,tcCode,pageInfo);
        } catch (Exception e) {
            EmpExecutionContext.error(e,"获取属性值失败！");
        }
        return beans;
    }

    public Map<String,String> getMemberCountByContractIds(String contractIDs) {
        HashMap<String, String> contractMap = new HashMap<String, String>();
        try{
            List<DynaBean> contractBeanList = dao.getAccountNoByContractIds(contractIDs);
            if(contractBeanList != null && contractBeanList.size()>0) {

                for(DynaBean contractBean:contractBeanList) {
                    contractMap.put(String.valueOf(contractBean.get("contract_id")),contractBean.get("acct_no") == null ? "":contractBean.get("acct_no").toString());
                }
            }
        }catch(Exception e){
            EmpExecutionContext.error(e, "根据签约ID获取签约ID和签约账号的MAP集合失败！");
        }
        return contractMap;
    }

    public Integer getClientCountByCusField(String corpCode, LfCustFieldValueVo fieldValueVo) {
        return dao.getClientCountByCusField(corpCode, fieldValueVo);
    }

    /**
     * 获取号段
     * @return
     */
    public String[] getHaoduan() throws Exception{
        String[] haoDuans = new String[3];
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        try {
            List<PbServicetype> haoduanList = empDao.findListByCondition(PbServicetype.class, conditionMap, null);
            for (PbServicetype pbSer : haoduanList) {
                switch (pbSer.getSpisuncm()){
                    case 0:haoDuans[0] = pbSer.getServiceno();
                    case 1:haoDuans[1] = pbSer.getServiceno();
                    case 21:haoDuans[2] = pbSer.getServiceno();
                }
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "获取号段异常！");
            throw e;
        }
        return haoDuans;
    }


    public  List<LfEmployee> getClientOrEmployeeByName(String name,String corpCode,String pageIndex){
        List<LfEmployee> lfEmployees = null;
        try {
            //lfEmployees =  empDao.findListBySymbolsCondition(LfEmployee.class, conditionMap, orderByMap);
            lfEmployees =  dao.getClientOrEmployeeByName(name, corpCode,pageIndex);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "获取人员信息异常！");
        }
        return lfEmployees;
    }
}
