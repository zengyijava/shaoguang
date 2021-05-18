package com.montnets.emp.rms.vo.view;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ProjectName: EMP7.2$
 * @Package: com.montnets.emp.rms.vo.view$
 * @ClassName: ViewCorpUserVo$
 * @Description: java类作用描述
 * @Author: xuty
 * @CreateDate: 2018/11/21$ 13:38$
 * @UpdateUser: 更新者
 * @UpdateDate: 2018/11/21$ 13:38$
 * @UpdateRemark: 更新内容
 * @Version: 1.0
 */
public class ViewCorpUserVo {
	protected static final Map<String, String> columns = new LinkedHashMap<String, String>();
    static {
        columns.put("userId", "USERID");
        columns.put("corpCode", "CORPCODE");
    }

    public static Map<String, String> getORM() {
        return columns;
    }
}
