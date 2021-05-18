package com.montnets.emp.group.biz;

import com.montnets.emp.common.biz.GlobalVariableBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.AddrBookSpecialDAO;
import com.montnets.emp.common.vo.GroupInfoVo;
import com.montnets.emp.entity.group.LfList2gro;
import com.montnets.emp.entity.group.LfMalist;
import com.montnets.emp.entity.group.LfUdgroup;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.group.dao.GenericGroupInfoVoDAO;
import com.montnets.emp.group.dao.GroupManagerDao;
import com.montnets.emp.group.vo.LfList2groVo;
import com.montnets.emp.table.group.TableLfList2gro;
import com.montnets.emp.util.PageInfo;

import java.sql.Connection;
import java.util.*;

public class GrpBaseMgrBiz extends SuperBiz{

    private GenericGroupInfoVoDAO groupInfoVoDAO;
    protected AddrBookSpecialDAO  addrbookSpecialDAO ;

    public GrpBaseMgrBiz()
    {
        groupInfoVoDAO = new GenericGroupInfoVoDAO();
        addrbookSpecialDAO = new AddrBookSpecialDAO();
    }
    /**
     * 保存群组
     * @param group
     * @param infos 群组来源信息以及企业编码
     * @return
     */
    public boolean saveGroup(LfUdgroup group, String[] infos){
        Connection connection = empTransDao.getConnection();
        try {
            empTransDao.beginTransaction(connection);
            Long gpId = empTransDao.saveObjectReturnID(connection, group);
            group.setUdgId(gpId);
            group.setGroupid(gpId);

            empTransDao.update(connection, group);
            Long userid = group.getUserId();
            //企业编码
            String lgcorpcode = infos[0];
            //客户来源
            String ygStr = infos[1];
            //群组来源
            String qzStr = infos[2];
            //共享来源
            String gxStr = infos[3];
            //自建
            String zjStr = infos[4];

            //解析
            List<LfList2gro> l2gList = new ArrayList<LfList2gro>();
            String[] ygArry = ygStr.split(",");
            for(int y=0;y<ygArry.length;y++)
            {
                if(ygArry[y] != null && ygArry[y].length() > 0)
                {
                    LfList2gro l2g = new LfList2gro();
                    l2g.setGuId(Long.valueOf(ygArry[y]));
                    l2g.setUdgId(Integer.valueOf(gpId.toString()));
                    l2g.setL2gType(group.getGpAttribute());
                    l2g.setSharetype(0);
                    l2gList.add(l2g);
                }
            }

            //解析来自共享群组的记录
            String[] gxArry = gxStr.split(",");
            for(int y=0;y<gxArry.length;y++)
            {
                if(gxArry[y] != null && gxArry[y].length() > 0)
                {
                    LfList2gro l2g = new LfList2gro();
                    l2g.setGuId(Long.valueOf(gxArry[y]));
                    l2g.setUdgId(Integer.valueOf(gpId.toString()));
                    l2g.setL2gType(2);
                    l2g.setSharetype(1);

                    l2gList.add(l2g);
                }
            }

            //解析来自自建群组的记录
            String[] qzArry = qzStr.split(",");
            for(int y=0;y<qzArry.length;y++)
            {
                if(qzArry[y] != null && qzArry[y].length() > 0)
                {
                    LfList2gro l2g = new LfList2gro();
                    l2g.setGuId(Long.valueOf(qzArry[y]));
                    l2g.setUdgId(Integer.valueOf(gpId.toString()));
                    l2g.setL2gType(2);
                    l2g.setSharetype(0);

                    l2gList.add(l2g);
                }
            }

            //解析自建群组记录
            String[] zjArry = zjStr.split(",");
            GlobalVariableBiz globalBiz = GlobalVariableBiz.getInstance();
            List<LfMalist> maLists = new ArrayList<LfMalist>();
            int len = zjArry.length;
            Long curValue = globalBiz.getValueByKey("guid", len);
            for(int y=0;y<len;y++)
            {
                if(zjArry[y] != null && zjArry[y].length() > 0)
                {
                    String[] infoArry = zjArry[y].split("[|]");
                    LfMalist mal = new LfMalist();
                    Long guid = curValue - y;
                    mal.setCorpCode(lgcorpcode);
                    mal.setUserId(userid);
                    mal.setName(infoArry[0]);
                    mal.setMobile(infoArry[1]);
                    mal.setGuId(guid);

                    maLists.add(mal);

                    LfList2gro l2g = new LfList2gro();
                    l2g.setGuId(guid);
                    l2g.setUdgId(Integer.valueOf(gpId.toString()));
                    l2g.setL2gType(2);
                    l2g.setSharetype(0);

                    l2gList.add(l2g);
                }
            }
            empTransDao.save(connection, maLists, LfMalist.class);
            empTransDao.save(connection, l2gList, LfList2gro.class);
            empTransDao.commitTransaction(connection);
            return true;
        } catch (Exception e) {
            empTransDao.rollBackTransaction(connection);
            EmpExecutionContext.error(e,"保存群组信息异常！");
            return false;
        }finally {
            empTransDao.closeConnection(connection);
        }
    }

    /**
     * 获取群组数目
     * @param udgIds 群组ids
     * @return
     * @throws Exception
     */
    public Map<String,String> getGrpCount(String udgIds) throws Exception
    {
        HashMap<String, String> countMap = new HashMap<String, String>();
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();

        conditionMap.put("udgId&in", udgIds);
//        conditionMap.put("l2gType&in", "1,2");
        String groupColum = TableLfList2gro.UDG_ID;
        String columName = "count("+TableLfList2gro.L2G_ID+"),"+TableLfList2gro.UDG_ID;
        List<String[]> countList = empDao.findListByConditionByColumNameWithGroupBy(LfList2gro.class,
                conditionMap, columName, null,groupColum);

        if(countList != null && countList.size() > 0)
        {
            for(int i=0;i<countList.size();i++)
            {
                String[] countArr = countList.get(i);
                countMap.put(countArr[1], countArr[0]);
            }
        }
        return countMap;
    }

    /**
     * 员工群组中按条件分页查询详情
     * @param groupId
     * @param pageInfo
     * @param name
     * @return
     * @throws Exception
     */
    public List<GroupInfoVo> getGroupUser(Long groupId, PageInfo pageInfo, String name) throws Exception
    {
        List<GroupInfoVo> groupVosList = null;
        try
        {
            groupVosList =groupInfoVoDAO.findGroupUserByIds(groupId,pageInfo,name);
        } catch (Exception e){
            groupVosList = null;
            EmpExecutionContext.error(e,"员工群组获取群组成员失败！");
        }
        return groupVosList;
    }

    /**
     * 员工群组中按条件分页查询详情
     * @param groupId
     * @param pageInfo
     * @param name
     * @param corpCode
     * @return
     * @throws Exception
     */
    public List<GroupInfoVo> getGroupUser(Long groupId, PageInfo pageInfo, String name,String corpCode) throws Exception
    {
        List<GroupInfoVo> groupVosList = null;
        try
        {
            groupVosList =groupInfoVoDAO.findGroupUserByIds(groupId,pageInfo,name,corpCode);
        } catch (Exception e){
            groupVosList = null;
            EmpExecutionContext.error(e,"员工群组获取群组成员失败！");
        }
        return groupVosList;
    }

    /**
     * 客户群组中按条件分页查询详情
     * @param groupId
     * @param pageInfo
     * @param name
     * @return
     * @throws Exception
     */
    public List<GroupInfoVo> getGroupClient(Long groupId, PageInfo pageInfo, String name) throws Exception
    {
        List<GroupInfoVo> groupVosList = null;
        try
        {
            groupVosList =groupInfoVoDAO.findGroupCliByIds(groupId, pageInfo, name);
        } catch (Exception e){
            groupVosList = null;
            EmpExecutionContext.error(e,"客户群组获取群组成员失败！");
        }
        return groupVosList;
    }

    /**
     * 客户群组中按条件分页查询详情
     * @param groupId
     * @param pageInfo
     * @param name
     * @param corpCode
     * @return
     * @throws Exception
     */
    public List<GroupInfoVo> getGroupClient(Long groupId, PageInfo pageInfo, String name,String corpCode) throws Exception
    {
        List<GroupInfoVo> groupVosList = null;
        try
        {
            groupVosList =groupInfoVoDAO.findGroupCliByIds(groupId, pageInfo, name, corpCode);
        } catch (Exception e){
            groupVosList = null;
            EmpExecutionContext.error(e,"客户群组获取群组成员失败！");
        }
        return groupVosList;
    }

    /**
     * 根据特殊的查询条件获取操作员
     * @param depIdCon 查询条件
     * @return
     * @throws Exception
     */
    public List<LfSysuser> getLfSysuserListByDepIdCon(String depIdCon) throws Exception{
        return new GroupManagerDao().getLfSysuserListByDepIdCon(depIdCon);
    }

    /**
     * 获取群组成员
     * @param vo 查询条件
     * @param udgid 群组Id
     * @return
     * @throws Exception
     */
    public List<LfList2groVo> getList2groVos(LfList2groVo vo, Long udgid) throws Exception
    {
        List<LfList2groVo> groupVosList = null;
        try
        {
            groupVosList = groupInfoVoDAO.findGroupInfoVo(vo,udgid);

        } catch (Exception e)
        {
            EmpExecutionContext.error(e,"获取群组成员异常！");
        }
        return groupVosList;
    }
    /**
     * 获取群组成员
     * @param vo 查询条件
     * @param udgid 群组Id
     * @return
     * @throws Exception
     */
    public List<LfList2groVo> getList2groVosByPageInfo(LfList2groVo vo,PageInfo pageInfo, Long udgid) throws Exception
    {
    	List<LfList2groVo> groupVosList = null;
    	try
    	{
    		groupVosList = groupInfoVoDAO.findGroupInfoVoByPageInfo(vo,pageInfo,udgid);
    		
    	} catch (Exception e)
    	{
    		EmpExecutionContext.error(e,"获取群组成员异常！");
    	}
    	return groupVosList;
    }

    /**
     * 删除个人群组
     * @param udgIdStr	群组ID
     * @param loginUserId	登录者ID
     * @return
     */
    public boolean delPersonGroup(String udgIdStr, String loginUserId) {
        //个人群组
        StringBuffer perGroupStr = new StringBuffer();
        //我的共享群组
        StringBuffer mineGxGroupStr = new StringBuffer();
        boolean isFlag = false;
        try{
            String[] arr = udgIdStr.split(",");
            LfUdgroup udgGroup = null;
            for(int i=0;i<arr.length;i++){
                String id = arr[i];
                if(id == null || "".equals(id)){
                    continue;
                }
                //获取群组Id对应实体类对象
                udgGroup = empDao.findObjectByID(LfUdgroup.class, Long.parseLong(id));
                if(udgGroup != null){
                    Integer groupType = udgGroup.getSharetype();
                    if(groupType == 0){
                        perGroupStr.append(id).append(",");
                    }else if(groupType == 1){
                        mineGxGroupStr.append(id).append(",");
                    }
                }
            }
            //对于他人共享的群组 只需删除对应LfUdgroup表数据 自己创建的 需要删除所有共享给他人的数据 还有 自定义表绑定数据
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            //群组分类操作
            String str1 = "";
            String str2 = "";
            if(perGroupStr != null && perGroupStr.length()>0){
                str1 = perGroupStr.toString().substring(0, perGroupStr.length()-1);
            }
            if(mineGxGroupStr != null && mineGxGroupStr.length()>0){
                str2 = mineGxGroupStr.toString().substring(0, mineGxGroupStr.length()-1);
            }
            Connection conn = empTransDao.getConnection();

            try
            {
                //开始事务
                empTransDao.beginTransaction(conn);
                if(!"".equals(str1)){
                    conditionMap.put("udgId", str1);
                    //删除群组成员
                    empTransDao.delete(conn, LfList2gro.class, conditionMap);
                    conditionMap.clear();
                    conditionMap.put("groupid", str1);
                    //删除共享群组
                    empTransDao.delete(conn, LfUdgroup.class, conditionMap);
                }
                if(!"".equals(str2)){
                    conditionMap.clear();
                    conditionMap.put("udgId", str2);
                    conditionMap.put("receiver", loginUserId);
                    empTransDao.delete(conn, LfUdgroup.class, conditionMap);
                }
                //提交事务
                empTransDao.commitTransaction(conn);
                isFlag = true;
            } catch (Exception e)
            {
                //异常处理
                EmpExecutionContext.error(e,"删除个人群组失败！");
                //回滚事务
                empTransDao.rollBackTransaction(conn);
                isFlag = false;
            } finally
            {
                //关闭连接
                empTransDao.closeConnection(conn);
            }
        }catch (Exception e) {
            EmpExecutionContext.error(e,"删除个人群组出现异常！");
            isFlag = false;
        }
        return isFlag;
    }



}
