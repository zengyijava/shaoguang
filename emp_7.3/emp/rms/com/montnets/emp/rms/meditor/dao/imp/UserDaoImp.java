package com.montnets.emp.rms.meditor.dao.imp;

import com.alibaba.fastjson.JSON;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IGenericDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.rms.meditor.dao.UserDao;
import com.montnets.emp.rms.meditor.entity.TempAuthority;
import org.apache.commons.beanutils.DynaBean;

import java.util.ArrayList;
import java.util.List;

public class UserDaoImp extends SuperDAO implements UserDao {
    IGenericDAO genericDAO = new DataAccessDriver().getGenericDAO();

    @Override
    public Integer hasCreatePublicTemp(Long userId) {
        String tableSql = "SELECT COUNT(*) num FROM LF_USER2ROLE ur JOIN LF_IMPOWER im ON ur.ROLE_ID=im.ROLE_ID JOIN LF_PRIVILEGE pr ON im.PRIVILEGE_ID=pr.PRIVILEGE_ID ";
        StringBuffer contionSql = new StringBuffer();
        //contionSql.append("WHERE ur.USER_ID=").append(userId).append(" AND pr.MENUNAME='公共场景' AND pr.COMMENTS='新建'");
        contionSql.append("WHERE ur.USER_ID=").append(userId).append(" AND pr.PRIV_CODE='5100-1350-1'");//1表示有创建权限
        List<DynaBean> counts = genericDAO.findDynaBeanBySql(tableSql+contionSql);

        if (counts !=null && counts.get(0) !=null && Integer.parseInt(counts.get(0).get("num").toString())>0){
            return 1;
        }else {
            return 0;
        }
    }

    @Override
    public String getAuthority(Long userId){
        String tableSql = "SELECT pr.MENUCODE,pr.PRIV_CODE FROM LF_USER2ROLE ur JOIN LF_IMPOWER im ON ur.ROLE_ID=im.ROLE_ID  JOIN LF_PRIVILEGE pr ON im.PRIVILEGE_ID=pr.PRIVILEGE_ID ";
        StringBuffer contionSql = new StringBuffer();
        contionSql.append("WHERE ur.USER_ID=").append(userId).append(" AND pr.MENUCODE in('5100-1350','5100-1300','5100-1351') GROUP BY pr.MENUCODE,pr.PRIV_CODE ORDER BY pr.PRIV_CODE");
        List<DynaBean> list = genericDAO.findDynaBeanBySql(tableSql+contionSql);
        TempAuthority tempAuthority_My = new TempAuthority();//默认值为0
        TempAuthority tempAuthority_Model = new TempAuthority();
        TempAuthority tempAuthority_Rcos = new TempAuthority();
        if(list !=null && list.size()>0){///多循环两次,可优化
            tempAuthority_My.setType("my");
            for (DynaBean dynaBean:list){
                if("5100-1300".equals(dynaBean.get("menucode").toString())){
                    if("0".equals(dynaBean.get("priv_code").toString().replace("5100-1300-",""))){//查询
                        tempAuthority_My.setQuery("1");
                    }
                    if("1".equals(dynaBean.get("priv_code").toString().replace("5100-1300-",""))){//新建
                        tempAuthority_My.setAdd("1");
                    }
                    if("2".equals(dynaBean.get("priv_code").toString().replace("5100-1300-",""))){//删除
                        tempAuthority_My.setDel("1");
                    }
                    if("3".equals(dynaBean.get("priv_code").toString().replace("5100-1300-",""))){//修改
                        tempAuthority_My.setUpdate("1");
                    }
                    if("4".equals(dynaBean.get("priv_code").toString().replace("5100-1300-",""))){//审核状态
                        tempAuthority_My.setAuth("1");
                    }
                    if("5".equals(dynaBean.get("priv_code").toString().replace("5100-1300-",""))){//立即发送
                        tempAuthority_My.setSend("1");
                    }
                    if("6".equals(dynaBean.get("priv_code").toString().replace("5100-1300-",""))){//预览
                        tempAuthority_My.setPreview("1");
                    }
                    if("7".equals(dynaBean.get("priv_code").toString().replace("5100-1300-",""))){//复制
                        tempAuthority_My.setCopy("1");
                    }
                    if("8".equals(dynaBean.get("priv_code").toString().replace("5100-1300-",""))){//导出
                        tempAuthority_My.setExport("1");
                    }
                    if("9".equals(dynaBean.get("priv_code").toString().replace("5100-1300-",""))){//详情
                        tempAuthority_My.setDetail("1");
                    }
                    if("10".equals(dynaBean.get("priv_code").toString().replace("5100-1300-",""))){//复制链接
                        tempAuthority_My.setLink("1");
                    }
                    if("11".equals(dynaBean.get("priv_code").toString().replace("5100-1300-",""))){//启用禁用
                        tempAuthority_My.setState("1");
                    }
                    if("12".equals(dynaBean.get("priv_code").toString().replace("5100-1300-",""))){//快捷场景
                        tempAuthority_My.setShortcut("1");
                    }
                    if("13".equals(dynaBean.get("priv_code").toString().replace("5100-1300-",""))){//模板共享
                        tempAuthority_My.setShare("1");
                    }
//                    if("13".equals(dynaBean.get("priv_code").toString().replace("5100-1300-",""))){//行业用途
//                        tempAuthority_My.setIndustryAndUse("1");
//                    }
                }
            }
            tempAuthority_Model.setType("common");
            for (DynaBean dynaBean:list){
                if("5100-1350".equals(dynaBean.get("menucode").toString())){
                    if("0".equals(dynaBean.get("priv_code").toString().replace("5100-1350-",""))){//查询
                        tempAuthority_Model.setQuery("1");
                    }
                    if("1".equals(dynaBean.get("priv_code").toString().replace("5100-1350-","")) ){//新建
                        if (1== StaticValue.getCORPTYPE() && "2".equals(String.valueOf(userId))) {//多企业版(托管版),多企业版只有100000号才有创建公共模板的权限
                            tempAuthority_Model.setAdd("1");
                        }
                        if (0== StaticValue.getCORPTYPE()) {//单企业版(标准版)
                            tempAuthority_Model.setAdd("1");
                        }
                    }
//                    if("2".equals(dynaBean.get("priv_code").toString().replace("5100-1350-",""))){//删除
//                        tempAuthority_Model.setDel("1");
//                    }
                    if("3".equals(dynaBean.get("priv_code").toString().replace("5100-1350-",""))){//修改
                        tempAuthority_Model.setUpdate("1");
                    }
                    if("4".equals(dynaBean.get("priv_code").toString().replace("5100-1350-",""))){//审核状态
                        tempAuthority_Model.setAuth("1");
                    }
                    if("5".equals(dynaBean.get("priv_code").toString().replace("5100-1350-",""))){//立即发送
                        tempAuthority_Model.setSend("1");
                    }
                    if("6".equals(dynaBean.get("priv_code").toString().replace("5100-1350-",""))){//预览
                        tempAuthority_Model.setPreview("1");
                    }
                    if("7".equals(dynaBean.get("priv_code").toString().replace("5100-1350-",""))){//复制
                        if (1== StaticValue.getCORPTYPE() && "2".equals(String.valueOf(userId))) {//多企业版(托管版),多企业版只有100000号
                            tempAuthority_Model.setCopy("1");
                        }else{
                            tempAuthority_Model.setCopy("0");
                        }
                    }
                    if("8".equals(dynaBean.get("priv_code").toString().replace("5100-1350-",""))){//导出
                        tempAuthority_Model.setExport("1");
                    }
                    if("9".equals(dynaBean.get("priv_code").toString().replace("5100-1350-",""))){//详情
                        tempAuthority_Model.setDetail("1");
                    }
                    if("10".equals(dynaBean.get("priv_code").toString().replace("5100-1350-",""))){//复制链接
                        tempAuthority_Model.setLink("1");
                    }
                    if("11".equals(dynaBean.get("priv_code").toString().replace("5100-1350-",""))){//启用禁用
                        tempAuthority_Model.setState("1");
                    }
                    if("12".equals(dynaBean.get("priv_code").toString().replace("5100-1350-",""))){//快捷场景
                        tempAuthority_Model.setShortcut("1");
                    }
                    if (1== StaticValue.getCORPTYPE() && "2".equals(String.valueOf(userId))) {//多企业版(托管版),多企业版只有100000号才有行业用途
                        tempAuthority_Model.setIndustryAndUse("1");
                    }
                }
            }
            tempAuthority_Rcos.setType("rcos");
            for (DynaBean dynaBean:list){
                    if("5100-1351".equals(dynaBean.get("menucode").toString())){
                        if("0".equals(dynaBean.get("priv_code").toString().replace("5100-1351-",""))){//查询
                            tempAuthority_Rcos.setQuery("1");
                        }
                        if("1".equals(dynaBean.get("priv_code").toString().replace("5100-1351-",""))){//新建
                            tempAuthority_Rcos.setAdd("1");
                        }
                        if("2".equals(dynaBean.get("priv_code").toString().replace("5100-1351-",""))){//删除
                            tempAuthority_Rcos.setDel("1");
                        }
                        if("3".equals(dynaBean.get("priv_code").toString().replace("5100-1351-",""))){//修改
                            tempAuthority_Rcos.setUpdate("1");
                        }
                        if("4".equals(dynaBean.get("priv_code").toString().replace("5100-1351-",""))){//审核状态
                            tempAuthority_Rcos.setAuth("1");
                        }
                        if("5".equals(dynaBean.get("priv_code").toString().replace("5100-1351-",""))){//立即发送
                            tempAuthority_Rcos.setSend("1");
                        }
                        if("6".equals(dynaBean.get("priv_code").toString().replace("5100-1351-",""))){//预览
                            tempAuthority_Rcos.setPreview("1");
                        }
                        if("7".equals(dynaBean.get("priv_code").toString().replace("5100-1351-",""))){//复制
                            tempAuthority_Rcos.setCopy("1");
                        }
                        if("8".equals(dynaBean.get("priv_code").toString().replace("5100-1351-",""))){//导出
                            tempAuthority_Rcos.setExport("1");
                        }
                        if("9".equals(dynaBean.get("priv_code").toString().replace("5100-1351-",""))){//详情
                            tempAuthority_Rcos.setDetail("1");
                        }
                        if("10".equals(dynaBean.get("priv_code").toString().replace("5100-1351-",""))){//复制链接
                            tempAuthority_Rcos.setLink("1");
                        }
                        if("11".equals(dynaBean.get("priv_code").toString().replace("5100-1351-",""))){//启用禁用
                            tempAuthority_Rcos.setState("1");
                        }
                        if("12".equals(dynaBean.get("priv_code").toString().replace("5100-1351-",""))){//快捷场景
                            tempAuthority_Rcos.setShortcut("1");
                        }
//                    if("13".equals(dynaBean.get("priv_code").toString().replace("5100-1351-",""))){//行业用途
//                        tempAuthority_Rcos.setIndustryAndUse("1");
//                    }
                    }
                }
        }else{
            EmpExecutionContext.info("权限查询为空");
        }
        List<TempAuthority> authorityList = new ArrayList<TempAuthority>();
        authorityList.add(tempAuthority_My);
        authorityList.add(tempAuthority_Model);
        authorityList.add(tempAuthority_Rcos);
        String jsonArray = JSON.toJSONString(authorityList);
        return jsonArray;
    }
}
