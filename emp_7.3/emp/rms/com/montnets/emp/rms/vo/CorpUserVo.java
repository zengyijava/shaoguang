package com.montnets.emp.rms.vo;

/**
 * @ProjectName: EMP7.2$
 * @Package: com.montnets.emp.rms.vo$
 * @ClassName: CorpUserVo$
 * @Description: java类作用描述
 * @Author: xuty
 * @CreateDate: 2018/11/21$ 13:36$
 * @UpdateUser: 更新者
 * @UpdateDate: 2018/11/21$ 13:36$
 * @UpdateRemark: 更新内容
 * @Version: 1.0
 */
public class CorpUserVo {
    private Long userId;
    private String corpCode;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCorpCode() {
        return corpCode;
    }

    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }

    @Override
    public String toString() {
        return "CorpUserVo{" +
                "userId=" + userId +
                ", corpCode='" + corpCode + '\'' +
                '}';
    }
}
