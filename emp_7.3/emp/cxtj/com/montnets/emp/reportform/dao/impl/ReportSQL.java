package com.montnets.emp.reportform.dao.impl;

public class ReportSQL {
    /**
     * 报表sql
     */
    private String sql;
    /**
     * 报表总数sql
     */
    private String countSql;

    public ReportSQL(String sql, String countSql) {
        this.sql = sql;
        this.countSql = countSql;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getCountSql() {
        return countSql;
    }

    public void setCountSql(String countSql) {
        this.countSql = countSql;
    }
}
