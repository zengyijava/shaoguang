package com.montnets.emp.rms.meditor.dao.imp;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.rms.meditor.dao.SynRcosTemplateDao;
import com.montnets.emp.rms.vo.CorpUserVo;

import java.util.List;

/**
 * @ProjectName: EMP7.2$
 * @Package: com.montnets.emp.rms.meditor.dao.imp$
 * @ClassName: SynRcosTemplateDaoImpl$
 * @Description: java类作用描述
 * @Author: xuty
 * @CreateDate: 2018/11/21$ 8:51$
 * @UpdateUser: 更新者
 * @UpdateDate: 2018/11/21$ 8:51$
 * @UpdateRemark: 更新内容
 * @Version: 1.0
 */
public class SynRcosTemplateDaoImpl extends SuperDAO implements SynRcosTemplateDao {
    @Override
    public List<CorpUserVo> getCorps() {
        String sql = "SELECT A.USER_ID as USERID,A.CORP_CODE AS CORPCODE FROM LF_SYSUSER  A LEFT JOIN lf_corp B ON  A.CORP_CODE = B.CORP_CODE  WHERE  A.USER_NAME='admin'";
        try {
            List<CorpUserVo> users = findVoListBySQL(CorpUserVo.class,sql,StaticValue.EMP_POOLNAME);
            if(null != users && users.size() > 0){
                return  users;
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e,"企业admin 账号查询出现异常！");
        }
        return null;
    }
}
