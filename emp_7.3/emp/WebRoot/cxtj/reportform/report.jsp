<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="emp" uri="http://www.montnets.com/emp/i18n/tags/simple" %>
<%@include file="/common/common.jsp" %>
<%
    String iPath = request.getRequestURI().substring(0, request.getRequestURI().lastIndexOf("/"));
    String inheritPath = iPath.substring(0, iPath.lastIndexOf("/"));
    String commonPath = inheritPath.substring(0, inheritPath.lastIndexOf("/"));
    String langName = (String) session.getAttribute(StaticValue.LANG_KEY);
    String skinPath = session.getAttribute("stlyeSkin") == null ? "default" : (String) session.getAttribute("stlyeSkin");
    String version = skinPath.contains("frame3.0") ? "v3" : skinPath.contains("frame4.0") ? "v4" : "v2";
    int index = skinPath.lastIndexOf("/");
    String skin = version + "-" + skinPath.substring(index + 1, skinPath.length());
    String module = (String) request.getAttribute("module");
%>
<!doctype html>
<html lang="en">
<head>
    <title>统计报表
    </title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link href="<%=commonPath%>/common/css/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet"
          type="text/css"/>
    <link href="<%=skinPath %>/frame.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet" type="text/css"/>
    <link href="<%=commonPath%>/common/css/vue/index.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet"
          type="text/css"/>
    <link href="<%=commonPath%>/common/css/vue/index<%="-" + skin%>.css?V=<%=StaticValue.getJspImpVersion() %>"
          rel="stylesheet"
          type="text/css"/>
    <link href="<%=commonPath%>/common/css/vue/iview.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet"
          type="text/css"/>
    <link href="<%=iPath%>/css/report.css?V=<%=StaticValue.getJspImpVersion() %>" rel="stylesheet"
          type="text/css"/>
</head>
<body class="rep_areaReport">
<div id="container" class="container" v-cloak>
    <el-scrollbar style="height: 100%">
        <input id="module" hidden value="<%=module%>" title="module">
        <span v-html="position"></span>
        <div v-if="!access && request.module == 'sysDepReport'"
             style="font-size: 1rem; margin-top: 16rem; text-align: center">
            <h1>您没有机构权限，不能操作该模块，如有疑问请联系管理员！</h1>
        </div>
        <div id="rContent" class="rContent"
             v-if="access && loadSucc"
             style="margin-left:1.5rem;width: 95%">
            <div class="top_btn">
                <div class="top_left_btn">
                    <el-button type="primary" icon="el-icon-download" size="mini" @click="downloadFile">导出
                    </el-button>
                </div>
                <div class="top_right_btn">
                    <el-switch
                            v-if="!isDetails"
                            v-model="isSearch"
                            active-color="#13ce66"
                            inactive-color="#ff4949"
                            active-text="显示搜索"
                            inactive-text="隐藏搜索">
                    </el-switch>
                    <el-button type="primary" @click="isBack" v-if="isDetails" icon="el-icon-back">返回</el-button>
                </div>
            </div>
            <el-card class="box-card" style="margin-bottom: 0.8rem" v-if="isSearch" shadow="hover">

                <el-form :inline="true" ref="request.report" :model="request.report" label-width="90px"
                         class="reportForm" style="width: 890px; margin-left: 2%; float: left"
                         size="mini"
                         label-position="left">
                    <el-form-item label="信息类型:" v-if="menuItem.messageType">
                        <el-select v-model="request.report.mstype" placeholder="请选择" @change="changeMsType"
                                   class="defaultWidth">
                            <el-option
                                    v-for="item in MessageType"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="运营商账户ID:" v-if="menuItem.msType">
                        <el-select v-model="request.report.spId" placeholder="请选择"
                                   class="defaultWidth" filterable>
                            <el-option
                                    key="-1"
                                    label="全部"
                                    value="-1">
                            </el-option>
                            <el-option
                                    v-for="(item,index) in (request.report.mstype == 0 ? responseData.smsuserList : responseData.mmsuserList)"
                                    :key="item"
                                    :label="item"
                                    :value="item">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="区域:" v-if="menuItem.provinces"
                                  :class="menuFocus && request.module != 'areaReport' ? 'focus' : ''">

                        <%--区域查询条件修改--%>
                        <el-select v-model="request.report.provinces" filterable placeholder="请选择" class="defaultWidth"
                                   clearable>
                            <el-option
                                    v-for="item in responseData.provinceAndCity"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value">
                            </el-option>
                        </el-select>


                        <%-- <el-cascader
                                 :clearable="true"
                                 class="defaultWidth"
                                 v-model="request.report.provinces"
                                 :options="responseData.provinceAndCity"
                                 change-on-select
                         ></el-cascader>--%>
                    </el-form-item>
                    <el-form-item label="业务类型:" v-if="menuItem.busType"
                                  :class="menuFocus && request.module != 'busReport' ? 'focus' : ''">
                        <el-select v-model="request.report.busCode" placeholder="全部"
                                   class="defaultWidth">
                            <el-option
                                    v-for="item in responseData.busList"
                                    :key="item.busCode"
                                    :label="item.busName + (item.busCode != -1 && item.busCode !=0 ? '(' + item.busCode + ')' : '')"
                                    :value="item.busCode">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="账号类型:" v-if="menuItem.spType">
                        <el-select v-model="request.report.sptype" placeholder="全部" @change="changeSendType"
                                   class="defaultWidth">
                            <el-option
                                    v-for="item in responseData.accTypeList"
                                    :key="item.sendType"
                                    :label="item.sendName"
                                    :value="item.sendType">
                            </el-option>
                        </el-select>
                    </el-form-item>

                    <el-form-item label="机构:" v-if="menuItem.org" style="overflow: hidden"
                                  :class="menuFocus && request.module != 'sysDepReport' && request.module != 'sysUserReport' ? 'focus' : ''">
                        <el-button
                                style="width: 150px;text-align: left"
                                plain
                                @click="selectOrg = true">
                            {{orgName}}
                        </el-button>
                    </el-form-item>

                    <el-form-item label="操作员:" v-if="menuItem.user && request.module != 'sysDepReport'"
                                  style="overflow: hidden"
                                  :class="menuFocus && request.module != 'sysUserReport' ? 'focus' : ''">
                        <el-button
                                style="width: 150px;text-align: left"
                                plain
                                @click="selectOperator = true">
                            {{operator}}
                        </el-button>
                    </el-form-item>

                    <el-form-item label="运营商:" v-if="menuItem.spisuncm"
                                  :class="menuFocus && request.module == 'areaReport' ? 'focus' : ''">
                        <el-select v-model="request.report.spisuncm" placeholder="全部" class="defaultWidth">
                            <el-option
                                    v-for="item in operatorLabel"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="报表类型:" v-if="menuItem.paramStr">
                        <el-select v-model="request.report.paramStr" placeholder="请选择" @change="paramColumn"
                                   class="defaultWidth">
                            <el-option
                                    v-for="item in paramLabel"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="发送类型:" v-if="menuItem.sendType">
                        <el-select v-model="request.report.sendtype" placeholder="请选择" class="defaultWidth">
                            <el-option
                                    v-for="item in sendTypeLabel"
                                    :key="item.sendType"
                                    :label="item.sendName"
                                    :value="item.sendType">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="sp账号:" v-if="menuItem.spuserId"
                                  :class="menuFocus && request.module != 'spMtReport' ? 'focus' : ''">
                        <el-select v-model="request.report.spUserId" placeholder="请选择" @change="changeDate"
                                   class="defaultWidth" filterable>
                            <el-option
                                    key=""
                                    label="全部"
                                    value="">
                            </el-option>
                            <el-option
                                    v-for="item in (request.report.mstype === '0' ? responseData.smsSpUser : responseData.mmsSpUser)"
                                    :key="item"
                                    :label="item"
                                    :value="item">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="账户名称:" v-if="menuItem.staffname">
                        <el-input v-model="request.report.staffname" style="width:150px;"></el-input>
                    </el-form-item>
                    <!--------------------------------------------- 换行操作 --------------------------------------------->
                    <br>
                    <el-form-item label="报表类型:" v-if="menuItem.reportType">
                        <el-select v-model="request.report.reportType" placeholder="请选择" @change="changeDate"
                                   class="defaultWidth">
                            <el-option
                                    v-for="item in reportTypeLabel"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="统计时间:" v-if="isDays&&(menuItem.reportType || menuItem.paramStr)">
                        <el-date-picker
                                :clearable="false"
                                v-model="startTime"
                                ref="startTime"
                                align="right"
                                type="date"
                                placeholder="选择开始日期"
                                @change="changeStartTime"
                                :picker-options="pickerOptions">
                        </el-date-picker>
                    </el-form-item>
                    <el-form-item label="统计年月:" v-if="!isDays&&menuItem.reportType">
                        <el-date-picker
                                :clearable="false"
                                align="right"
                                ref="queryTime"
                                v-model="request.report.queryTime"
                                :type="dateType"
                                :placeholder="datePlaceholder"
                                :value-format="simpleDateFormat"
                                :picker-options="pickerOptions2"
                        >
                        </el-date-picker>
                    </el-form-item>
                    <el-form-item label="至: " v-if="isDays&&(menuItem.reportType || menuItem.paramStr)">
                        <el-date-picker
                                :clearable="false"
                                v-model="endTime"
                                ref="endTime"
                                align="right"
                                type="date"
                                placeholder="选择结束日期"
                                @change="changeEndTime"
                                :picker-options="pickerOptions">
                        </el-date-picker>
                    </el-form-item>
                </el-form>
                <el-container>
                    <el-header style="padding: 14px 0 5px 0" height="70px">
                        <el-button style="float:left;font-size: 1rem;margin-right: 3rem; margin-top: -0.3rem"
                                   type="text"
                                   @click="isMoreDimensions = true"
                                   v-if="request.module != 'dynParamReport' && request.module != 'spisuncmMtReport'">
                            更多维度
                        </el-button>
                        <el-button style="float: left;" type="primary" @click="getQuery" size="mini"
                                   icon="el-icon-search">查询
                        </el-button>
                    </el-header>
                </el-container>
            </el-card>
            <!--------------------------------------------- 表格代码 --------------------------------------------->
            <el-card class="box-card" shadow="hover">
                <el-table
                        :summary-method="getSummaries"
                        show-summary
                        size="mini"
                        :empty-text="emptyText"
                        :data="tableData"
                        v-loading="loading"
                        style="width: 100%;">
                    <el-table-column
                            align="center"
                            v-for="(item,index) in tableColumns"
                            :key="index"
                            :prop="item.prop"
                            :sortable="item.sortable"
                            :label="request.module == 'dynParamReport' && item.prop == 'paramName' ? request.report.paramName : item.label"
                            :min-width="item.width"
                    >
                        <template slot-scope="scope">
                            <!-- 已注销的标红 -->
                            <span>{{scope.row[item.prop]}}</span>
                            <span v-if="item.prop == 'name' && scope.row.userState == 2"
                                  style="color: red">
                                (已注销)
                            </span>
                        </template>
                    </el-table-column>
                    <el-table-column
                            v-if=" !isDetails && request.module !='dynParamReport'"
                            align="center"
                            label="详情"
                            min-width="15%"
                    >
                        <template slot-scope="scope">
                            <el-button
                                    size="mini"
                                    type="text"
                                    @click="getDetails(scope.$index, scope.row)">查看
                            </el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </el-card>
            <el-dialog title="机构选择" :visible.sync="selectOrg" width="600px">
                <el-checkbox
                        v-if="request.module != 'sysUserReport'"
                        v-model="request.report.containSubDep"
                        style="margin-bottom: 10px;">是否包含子机构
                </el-checkbox>
                <el-tree
                        ref="tree"
                        :props="defaultProps"
                        :load="loadDeptNode"
                        lazy
                        accordion
                        node-key="id"
                        :highlight-current="true"
                >
                </el-tree>
                <div slot="footer" class="dialog-footer">
                    <el-button @click="cancelOrg" size="mini">清 空</el-button>
                    <el-button type="primary" @click="confirmOrg" size="mini">确 定</el-button>
                </div>
            </el-dialog>
            <el-dialog title="操作员选择" :visible.sync="selectOperator" width="600px">
                <el-tree
                        show-checkbox
                        ref="operatorTree"
                        :props="defaultProps"
                        :load="loadUserNode"
                        :check-strictly="true"
                        lazy
                        accordion
                        node-key="id"
                        :highlight-current="true"
                >
                </el-tree>
                <div slot="footer" class="dialog-footer">
                    <el-button @click="cancelOperator" size="mini">清 空</el-button>
                    <el-button type="primary" @click="confirmOperator" size="mini">确 定</el-button>
                </div>
            </el-dialog>
            <!-- 更多维度弹出框-->
            <el-dialog top="150px" title="更多维度" :visible.sync="isMoreDimensions" width="600px" :show-close="false">
                <div style="margin-left: 1.5rem; border: 1px solid rgba(186,204,225,0.42); padding: 20px; background-color: #F7FBFD">
                    <el-checkbox :indeterminate="isIndeterminate" v-model="checkAll"
                                 @change="handleCheckAllChange">
                        全选
                    </el-checkbox>
                    <div style="margin: 15px 0;"></div>
                    <el-checkbox-group v-model="checkedData" @change="handleCheckedData">
                        <el-checkbox
                                v-for="item in moreDimensionsData"
                                :label="item.key"
                                :key="item.key"
                                v-if="item.access.indexOf(request.module) != -1">{{item.label}}
                        </el-checkbox>
                    </el-checkbox-group>
                </div>
                <div slot="footer" class="dialog-footer">
                    <el-button @click="cancelMoreDimensions" size="mini">取 消</el-button>
                    <el-button type="primary" @click="moreDimensions" size="mini">确 定</el-button>
                </div>
            </el-dialog>
            <div class="bottom">
                <el-pagination
                        @size-change="handlePageSize"
                        @current-change="handlePage"
                        :current-page.sync="request.page.pageIndex"
                        :page-sizes="request.page.pageSizeOpts"
                        :page-size="request.page.pageSize"
                        layout="total, sizes, prev, pager, next, jumper"
                        :total="request.page.totalRec">
                </el-pagination>
            </div>
        </div>
    </el-scrollbar>
</div>
<!-- 解决 IE 兼容性问题-->
<script type="text/javascript">
    document.write("<scr" + "ipt src=\"<%=commonPath%>/common/js/myjquery-a.js?V=<%=StaticValue.getJspImpVersion() %>\"></sc" + "ript>")
</script>
<script type="text/javascript">
    document.write("<scr" + "ipt src=\"<%=iPath %>/js/reportUtil.js?V=<%=StaticValue.getJspImpVersion() %>\"></sc" + "ript>")
</script>
<script src="<%=commonPath%>/common/i18n/i18nUtil.js"></script>
<script type="text/javascript">
    if (browserData() !== -1) {
        document.write("<scr" + "ipt src=\"<%=commonPath%>/common/js/vue/babel.min.js?V=<%=StaticValue.getJspImpVersion() %>\"></sc" + "ript>")
    }
</script>
<script type="text/javascript">
    if (browserData() !== -1) {
        document.write("<scr" + "ipt src=\"<%=commonPath%>/common/js/vue/browser-polyfill.min.js?V=<%=StaticValue.getJspImpVersion() %>\"></sc" + "ript>")
    }
</script>
<script type="text/javascript">
    document.write("<scr" + "ipt src=\"<%=commonPath%>/common/js/vue/vue.js?V=<%=StaticValue.getJspImpVersion() %>\"></sc" + "ript>")
</script>
<%--<script type="text/javascript">--%>
<%--document.write("<scr" + "ipt src=\"<%=commonPath%>/common/js/vue/iview.min.js?V=<%=StaticValue.getJspImpVersion() %>\"></sc" + "ript>")--%>
<%--</script>--%>
<script type="text/javascript">
    document.write("<scr" + "ipt src=\"<%=commonPath%>/common/js/vue/index.js?V=<%=StaticValue.getJspImpVersion() %>\"></sc" + "ript>")
</script>
<script src="<%=commonPath %>/common/js/common.js?V=<%=StaticValue.getJspImpVersion() %>"></script>
<script src="<%=commonPath%>/common/i18n/<%=langName%>/cxtj_<%=langName%>.js"></script>
<script type="text/javascript">
    if (browserData() === -1) {
        document.write("<scr" + "ipt src=\"<%=iPath %>/js/report.js?V=<%=StaticValue.getJspImpVersion() %>\"></sc" + "ript>")
    }
</script>
<script src="<%=iPath %>/js/report.js?V=<%=StaticValue.getJspImpVersion() %>"
        type="text/babel"></script>
</body>
</html>

