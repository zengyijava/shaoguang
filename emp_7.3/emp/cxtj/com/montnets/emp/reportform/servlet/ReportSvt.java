package com.montnets.emp.reportform.servlet;

import com.montnets.emp.common.constant.EMPException;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.query.biz.QueryBiz;
import com.montnets.emp.report.bean.RptStaticValue;
import com.montnets.emp.reportform.bean.ReportVo;
import com.montnets.emp.reportform.bean.Request;
import com.montnets.emp.reportform.cxtjenum.JumpPathEnum;
import com.montnets.emp.reportform.service.IReportService;
import com.montnets.emp.reportform.service.IRptExportService;
import com.montnets.emp.reportform.service.impl.ReportServiceImpl;
import com.montnets.emp.reportform.service.impl.RptExportServiceImpl;
import com.montnets.emp.reportform.util.FastJsonUtils;
import com.montnets.emp.reportform.util.IOUtils;
import com.montnets.emp.util.DownloadFile;
import com.montnets.emp.util.PageInfo;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.List;
import java.util.Map;


/**
 * 查询统计报表 controller
 *
 * @author lianghuageng && chenguang
 * @date 2018/12/10 17:31
 */
@SuppressWarnings("ALL")
public class ReportSvt extends BaseServlet {
    private final IReportService reportService = new ReportServiceImpl();
    private final IRptExportService exportService = new RptExportServiceImpl();
    private final QueryBiz queryBiz = new QueryBiz();

    /**
     * 公共跳转页面方法
     *
     * @param request  request对象
     * @param response response对象
     */
    public void find(HttpServletRequest request, HttpServletResponse response) {
        try {
            // 获取模块
            String module = getModule(request);
            request.setAttribute("module", module);
            request.getRequestDispatcher("cxtj/reportform/report.jsp").forward(request, response);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "--> ReportSvt.find(HttpServletRequest request, HttpServletResponse response)查询报表失败");
        }
    }

    /**
     * 获取初始化数据
     *
     * @param request  request对象
     * @param response response对象
     */
    public void getInitData(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter printWriter = null;
        String result = null;
        try {
            printWriter = response.getWriter();
            String module = request.getParameter("module");
            if (StringUtils.isBlank(module)) {
                throw new EMPException("模块参数异常");
            }
            //当前操作员
            LfSysuser user = queryBiz.getCurrentUser(request);
            // 从缓存中取值
            if (StaticValue.getCORPTYPE() == 0) {
                result = reportService.getInitDataByCache(user, module);
            }
            if (null == result) {
                HttpSession session = request.getSession(RptStaticValue.GET_SESSION_FALSE);
                String langName = (String) session.getAttribute(StaticValue.LANG_KEY);
                //按钮权限Map
                Map<String, String> btnMap = (Map<String, String>) session.getAttribute("btnMap");
                //菜单权限
                Map<String, String> titleMap = (Map<String, String>) session.getAttribute("titleMap");
                //corpCode
                String corpCode = queryBiz.getCorpCode(request);

                result = reportService.getInitData(langName, btnMap, titleMap, corpCode, user, module);
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "--> ReportSvt.getInitData(HttpServletRequest request, HttpServletResponse response) 获取初始化数据失败");
        } finally {
            IOUtils.flushAndClose(printWriter, result);
        }
    }

    /**
     * 懒加载树
     *
     * @param request
     * @param response
     */
    public void getTree(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter printWriter = null;
        String result = null;
        try {
            String depId = request.getParameter("code");
            String operation = request.getParameter("operation");
            if (StringUtils.isBlank(depId) || StringUtils.isBlank(operation)) {
                throw new EMPException("数据异常!");
            }
            printWriter = response.getWriter();
            //入口为机构还是操作员
            boolean flag = !"dept".equals(operation);
            //获取结果
            if(1 == StaticValue.getCORPTYPE()){
                //托管版采用懒加载模式
                result = reportService.getDeptOrUserTreeByLazy(depId.replace("org_", ""), flag);
            }else {
                result = reportService.getDeptOrUserTree(depId, flag);
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "--> ReportSvt.getTree() 加载机构树异常");
        } finally {
            IOUtils.flushAndClose(printWriter, result);
        }
    }

    /**
     * 查询方法
     *
     * @param request  request对象
     * @param response response对象
     */
    public void query(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter printWriter = null;
        String result = null;
        try {
            printWriter = response.getWriter();
            //获取操作员
            LfSysuser user = queryBiz.getCurrentUser(request);
            //获取自定义request对象
            Request requestEntity = getEntity(request, response);
            //校验自定义Request
            checkRequest(requestEntity);
            // 获取 reportVo 对象
            ReportVo queryEntity = requestEntity.getReport();
            // 获取分页对象
            PageInfo page = requestEntity.getPage();
            // 获取模块
            String module = requestEntity.getModule();

            //获取报表数据主入口
            List<ReportVo> reportVos = reportService.findMtDataRptByModuleName(user, queryEntity, module, page);
            //加上总条数转换为json传给前端
            result = reportService.handleReportVoList2Json(reportVos, page.getTotalRec());
        } catch (Exception e) {
            EmpExecutionContext.error(e, "--> ReportSvt.query(HttpServletRequest request, HttpServletResponse response) 查询异常");
        } finally {
            IOUtils.flushAndClose(printWriter, result);
        }
    }

    /**
     * 查询详情
     *
     * @param request  request对象
     * @param response response对象
     */
    public void details(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter printWriter = null;
        String result = null;
        try {
            printWriter = response.getWriter();
            //获取操作员
            LfSysuser user = queryBiz.getCurrentUser(request);
            //获取自定义request对象
            Request requestEntity = getEntity(request, response);
            //校验自定义Request
            checkRequest(requestEntity);
            //获取详情报表数据主入口
            List<ReportVo> reportVos = reportService.findMtDataRptDetail(user, requestEntity.getReport(), requestEntity);
            //加上总条数转换为json传给前端
            result = reportService.handleReportVoList2Json(reportVos, requestEntity.getPage().getTotalRec());
        } catch (Exception e) {
            EmpExecutionContext.error(e, "--> ReportSvt.details(HttpServletRequest request, HttpServletResponse response) 查询异常");
        } finally {
            IOUtils.flushAndClose(printWriter, result);
        }
    }

    /**
     * 导出功能接口
     *
     * @param request  request对象
     * @param response response对象
     */
    public void downloadFile(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter printWriter = null;
        String result = null;
        try {
            //获取操作员
            LfSysuser user = queryBiz.getCurrentUser(request);
            printWriter = response.getWriter();
            Request requestEntity = getEntity(request, response);
            //校验自定义Request
            checkRequest(requestEntity);
            // 获取模块
            String module = requestEntity.getModule();
            // 是否是详情页面
            boolean isDetails = requestEntity.isDetails();
            List<ReportVo> reportVos;
            if (isDetails) {
                //获取详情报表数据主入口
                reportVos = reportService.findMtDataRptDetail(user, requestEntity.getReport(), requestEntity);
            } else {
                //获取主页面报表数据主入口
                reportVos = reportService.findMtDataRptByModuleName(user, requestEntity.getReport(), module, null);
            }
            result = exportService.createRptExcelByModule(reportVos, module);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "--> ReportSvt.query(HttpServletRequest request, HttpServletResponse response) 查询异常");
            result = "error";
        } finally {
            IOUtils.flushAndClose(printWriter, result);
        }
    }

    /**
     * 下载文件方法
     *
     * @param request
     * @param response
     */
    public void download(HttpServletRequest request, HttpServletResponse response) {
        String fileName = request.getParameter("fileName");
        String filePath = request.getParameter("filePath");
        // 弹出下载页面。
        DownloadFile dfs = new DownloadFile();
        dfs.downFile(request, response, filePath, fileName);
    }

    /**
     * 从前端传递参数中获取查询的参数
     *
     * @param request  request对象
     * @param response response对象
     * @return 自定义Request对象
     */
    private Request getEntity(HttpServletRequest request, HttpServletResponse response) {
        BufferedReader br = null;
        InputStreamReader inputStreamReader = null;
        try {
            request.setCharacterEncoding("UTF-8");
            response.setHeader("content-type", "text/html;charset=UTF-8");
            response.setHeader("Access-Control-Allow-Origin", "*");
            /* 星号表示所有的异域请求都可以接受， */
            response.setHeader("Access-Control-Allow-Methods", "GET,POST");
            // 读取请求内容
            inputStreamReader = new InputStreamReader(request.getInputStream(), "utf-8");
            br = new BufferedReader(inputStreamReader);
            String line;
            StringBuilder sb = new StringBuilder();
            while (null != (line = br.readLine())) {
                sb.append(line);
            }
            //将json字符串转换为对象
            Request requestEntity = (Request) FastJsonUtils.convertJsonToObject(sb.toString(), Request.class);
            return requestEntity;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "--> ReportSvt.getEntity(HttpServletRequest request, HttpServletResponse response) 转化JSON对象异常");
        } finally {
            try {
                if (null != br) {
                    br.close();
                }
                if (null != inputStreamReader) {
                    inputStreamReader.close();
                }
            } catch (IOException e) {
                EmpExecutionContext.error(e, "--> ReportSvt.getEntity(HttpServletRequest request, HttpServletResponse response) 流关闭异常");
            }
        }
        return null;
    }

    /**
     * 获取模块名称
     *
     * @param request request对象
     * @return 模块名字
     */
    private String getModule(HttpServletRequest request) {
        String pathUrl = request.getRequestURI();
        return reportService.getJumpModulePath(pathUrl);
    }

    /**
     * 校验对象
     *
     * @param requestEntity 自定义Request对象
     */
    private void checkRequest(Request requestEntity) throws EMPException {
        if (null == requestEntity) {
            throw new EMPException("自定义Request校验失败，获取页面返回实体对象异常！");
        }
        if (StringUtils.isEmpty(requestEntity.getModule())) {
            throw new EMPException("自定义Request校验失败，获取module异常！");
        }
        if (requestEntity.getReport() == null) {
            throw new EMPException("自定义Request校验失败，获取页面返回实体查询ReportVO对象异常！");
        }
        //自定义参数报表一定要选一个
        if (JumpPathEnum.dynParamReport.getUrl().equals(requestEntity.getModule()) && StringUtils.isEmpty(requestEntity.getReport().getParamName())) {
            throw new EMPException("自定义Request校验失败，查询" + JumpPathEnum.dynParamReport.getName() + "时获取页面参数名字异常！");
        }
    }
}
