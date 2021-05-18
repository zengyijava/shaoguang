package com.montnets.emp.common.security.constant;

/**
 * @author liangHuaGeng
 * @Title: PatternConst
 * @ProjectName emp_7.3
 * @Description: TODO
 * @date 2019/1/1610:04
 */
public interface PatternConst {

    String SQL_REG = "(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|"
            + "(\\b(select|update|union|and|or|delete|insert|trancate|char|into|substr|ascii|declare|exec|count|master|into|drop|execute)\\b)";

    String SIMPLE_COMPILE = "simpleCompile";

    String COMPLEX_COMPILE = "complexCompile";

    String DELETE = "delete";

    String UPDATE = "update";

    String INSERT = "insert";

    String AND = "and";

    String INTO = "into";

    String DROP = "drop";
}
