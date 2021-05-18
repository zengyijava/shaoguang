package com.montnets.emp.query.servlet;

import com.alibaba.fastjson.JSON;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.constant.EMPException;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.biztype.LfBusManager;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.query.biz.ReportMtBiz;
import com.montnets.emp.query.biz.SysMtRecordBiz;
import com.montnets.emp.util.*;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 下行报表导出记录查询
 * @date 20181204
 * @author yangbo
 */
public class que_mtRptRecordSvt extends BaseServlet {

    private final String empRoot = "cxtj";

    private static final String BASE_DIR = "cxtj/query/file/report";

    private final ReportMtBiz iReportMtBiz = new ReportMtBiz();
    /**
     * 第一次进入页面
     * @param request
     * @param response
     */
    public void find(HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            String corpCode = getCorpCode(request);

            //设置业务类型，页面条件查询下拉框用
            getAndSetBus(corpCode, request);

            //设置通道号和发送账号，页面条件查询下拉框用
            getAndSetSp(corpCode, request);

            PageInfo pageInfo = new PageInfo();
            request.setAttribute("pageInfo", pageInfo);
            request.setAttribute("isFirstEnter", true);
            request.getRequestDispatcher(empRoot + "/query/que_sysMtRptRecord.jsp").forward(request,response);
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e,"跳转下行记录导出页面失败！");
        }
    }

    /**
     * 查询报表导出记录
     * @param request
     * @param response
     */
    public void findPageList(HttpServletRequest request, HttpServletResponse response)
    {
        LfSysuser sysUser = null;
        try
        {
            sysUser = super.getLoginUser(request);
            if(sysUser==null)
            {
                EmpExecutionContext.error("导出记录查询,find方法session中获取当前登录对象出现异常");
                return;
            }
            //分页对象
            PageInfo pageInfo = new PageInfo();
            pageSet(pageInfo, request);
            // 查询条件
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();

            //操作员名称
            String name = request.getParameter("name");
            //文件名称
            String fileName = request.getParameter("fileName");
            //状态
            String fileStatus = request.getParameter("fileStatus");
            //开始时间
            String startDate = request.getParameter("startDate");
            //结束时间
            String endDate = request.getParameter("endDate");

            conditionMap.put("name", name);
            conditionMap.put("fileName", fileName);
            conditionMap.put("fileStatus", fileStatus);
            conditionMap.put("startDate", startDate);
            conditionMap.put("endDate", endDate);
            conditionMap.put("permissionType", sysUser.getPermissionType().toString());

            request.setAttribute("resultList", JSON.parseArray(dynaBeans2Json(iReportMtBiz.findPageList(conditionMap,pageInfo,sysUser))));
            request.setAttribute("pageInfo",pageInfo);
            request.setAttribute("isFirstEnter", false);
            request.setAttribute("conditionMap", conditionMap);
            request.setAttribute("spuser",sysUser.getName());
            //设置业务类型，页面条件查询下拉框用
            getAndSetBus(sysUser.getCorpCode(), request);

            //设置通道号和发送账号，页面条件查询下拉框用
            getAndSetSp(sysUser.getCorpCode(), request);
            request.getRequestDispatcher(this.empRoot + "/query/que_sysMtRptRecord.jsp").forward(request, response);
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "系统下行导出记录查询，异常。");
            request.setAttribute("result", "-1");
        }


    }

    /**
     * 查询刷新文件状态的文件列表
     * @param request
     * @param response
     */
    public void getFlushFileList(HttpServletRequest request, HttpServletResponse response)
    {
        LfSysuser sysUser = null;
        PrintWriter print = null;
        try
        {
            sysUser = super.getLoginUser(request);
            if(sysUser==null)
            {
                EmpExecutionContext.error("导出记录查询,find方法session中获取当前登录对象出现异常");
                return;
            }
            //分页对象
            PageInfo pageInfo = new PageInfo();
            pageSet(pageInfo, request);
            // 查询条件
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();

            //操作员名称
            String name = request.getParameter("name");
            //文件名称
            String fileName = request.getParameter("fileName");
            //状态
            String fileStatus = request.getParameter("fileStatus");
            //开始时间
            String startDate = request.getParameter("startDate");
            //结束时间
            String endDate = request.getParameter("endDate");

            conditionMap.put("name", name);
            conditionMap.put("fileName", fileName);
            conditionMap.put("fileStatus", fileStatus);
            conditionMap.put("startDate", startDate);
            conditionMap.put("endDate", endDate);

            print = response.getWriter();
            print.print(dynaBeans2Json(iReportMtBiz.findPageList(conditionMap,pageInfo,sysUser)));
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "下载下行导出文件，异常。");
            if(print != null){
                print.print("-1");
            }
        }
        finally
        {
            if(print != null){
                print.close();
            }
        }

    }

    /**
     * List<DynaBean>转json处理
     * @param list
     * @return
     */
    private String dynaBeans2Json(List<DynaBean> list)
    {
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(DynaBean dynaBean : list)
        {
            DynaProperty[] properties = dynaBean.getDynaClass().getDynaProperties();
            map = new HashMap<String, Object>();
            for (DynaProperty property : properties)
            {
                String key = property.getName();
                Object value = dynaBean.get(key);
                if(value instanceof Timestamp)
                {
                    value = sdf.format((Timestamp) value);
                }else if (value instanceof Date)
                {
                    value = sdf.format((Date) value);
                }
                map.put(key, value);
            }
            mapList.add(map);
        }
        return JSON.toJSON(mapList).toString();
    }

    public void getDownloadTimeById(HttpServletRequest request, HttpServletResponse response){
        PrintWriter print = null;
        try {
            String id = request.getParameter("id");
            String fileName = request.getParameterValues("fileName")[0];
            if (StringUtils.isEmpty(id) || StringUtils.isEmpty(fileName)) {
                throw new EMPException("下载下行导出文件时文件id或名字为空。");
            }
            fileName = URLDecoder.decode(fileName,"UTF-8");
            //前端URLENCODE2次，需要解码2次
            fileName = URLDecoder.decode(fileName,"UTF-8");
            String filePath = new TxtFileUtil().getWebRoot() + BASE_DIR + File.separator + id + File.separator + fileName + ".csv";
            File file = new File(filePath);
            String relativeFilePath=BASE_DIR + "/" + id + "/" + fileName + ".csv";

            //对文件名称进行MD5编码
            String md5FileName =MD5.getMD5Str(fileName);
            String md5FilePath = new TxtFileUtil().getWebRoot() + BASE_DIR + File.separator + id + File.separator + md5FileName + ".csv";
            File md5NameFile = new File(md5FilePath);
            String md5RelativeFilePath=BASE_DIR + "/" + id + "/" + md5FileName + ".csv";

            //不集群的情况下
            if(StaticValue.getISCLUSTER() != 1){
                //md5编码文件标题的文件不存在
                if(!md5NameFile.exists()){
                    //明文文件标题的文件存在，则将文件标题改名为md5文件标题
                    if(file.exists()){
                        file.renameTo(md5NameFile);
                        EmpExecutionContext.info("系统下行记录导出，未集群，文件在本地，文件名称由"+filePath+"修改为"+md5FilePath);
                    }else{
                        EmpExecutionContext.error("系统下行记录导出，未集群，md5标题的文件和明文标题的文件都不在本地，用户不能下载。文件路径:"+filePath);
                    }
                }else{
                    EmpExecutionContext.info("系统下行记录导出，未集群，md5标题的文件在本地，可以下载。文件路径:"+filePath);
                }
            }else{
                //集群情况下
                //md5编码文件标题的文件不存在
                if(!md5NameFile.exists()){
                    //明文文件标题的文件存在，则将文件标题改名为md5文件标题
                    if(file.exists()){
                        file.renameTo(md5NameFile);
                        EmpExecutionContext.info("系统下行记录导出，集群，文件在本地，文件名称由"+filePath+"修改为"+md5FilePath);
                    }else{
                        //明文文件标题的文件不存在，则需要去文件服务器下载
                        //先下载md5编码文件标题的文件
                        String downloadRes=downloadFileFromFileCenterTryThreeTimes(md5RelativeFilePath);
                        //如果下载md5编码文件标题的文件，不成功
                        if(!"success".equals(downloadRes)){
                            //下载明文文件标题的文件
                            downloadRes=downloadFileFromFileCenterTryThreeTimes(relativeFilePath);
                            //如果下载明文文件标题的文件成功，则需要改名
                            if("success".equals(downloadRes)){
                                file.renameTo(md5NameFile);
                                EmpExecutionContext.info("系统下行记录导出，集群，文件不在本地，文件从文件服务器下载成功，文件名称由"+filePath+"修改为"+md5FilePath);
                            }else{
                                EmpExecutionContext.error("系统下行记录导出，集群，md5标题的文件和明文标题的文件都不在本地，从文件服务器都下载失败，用户不能下载。文件路径:"+filePath);
                            }
                        }else{
                            EmpExecutionContext.info("系统下行记录导出，集群，文件不在本地，从文件服务器下载md5标题的文件成功，可以下载。文件路径:"+filePath);
                        }
                    }
                }else{
                    EmpExecutionContext.info("系统下行记录导出，集群，md5编码标题的文件在本地，可以下载。文件路径:"+filePath);
                }
            }

//            //如果本地文件不存在，且是集群环境，就去文件服务器下载到本地
//            if(!file.exists() && StaticValue.getISCLUSTER() == 1) {
//                CommonBiz comBiz = new CommonBiz();
//                String downloadRes = "error";
//                //最大尝试次数
//                int retryTime = 3;
//                while (!"success".equals(downloadRes) && retryTime-- >0)
//                {
//                    downloadRes = comBiz.downloadFileFromFileCenter(filePath);
//                }
//                if(!"success".equals(downloadRes)) {
//                    EmpExecutionContext.error("下行导出文件从文件服务器下载失败。");
//                }
//            }
            iReportMtBiz.updateRptRecord(id,"0",false,true);
            print = response.getWriter();
            print.write(iReportMtBiz.getDownLoadTimeById(id));
        } catch (Exception e) {
            EmpExecutionContext.error(e, "下载下行导出文件，异常。");
        }finally {
            if(print != null){
                print.flush();
                print.close();
            }
        }
    }

    /**
     * 从文件服务器下载文件，尝试3次
     * @param filePath
     * @return
     */
    private String downloadFileFromFileCenterTryThreeTimes(String filePath){
        CommonBiz comBiz = new CommonBiz();
        String downloadRes = "error";
        //最大尝试次数3次
        int retryTime = 0;
        while (!"success".equals(downloadRes) && retryTime<3)
        {
            retryTime=retryTime+1;
            downloadRes = comBiz.downloadFileFromFileCenter(filePath);

        }
        if(!"success".equals(downloadRes)) {
            EmpExecutionContext.error("下行导出文件从文件服务器下载失败。文件路径:"+filePath);
        }
        return downloadRes;
    }

    public void downloadFile(HttpServletRequest request, HttpServletResponse response) {
        try {
            String fileid = request.getParameter("fileid");
            //request.getParameter("fileName")会被处理xss和sql注入的过滤器给转义了，没办法，只能用getParameterValues了。
            String fileName = request.getParameterValues("fileName")[0];
            if (StringUtils.isEmpty(fileid) || StringUtils.isEmpty(fileName)) {
                EmpExecutionContext.error("下载下行导出文件时文件id或者文件名称为空。");
            } else {
                fileName = URLDecoder.decode(fileName,"UTF-8");
                //前端URLENCODE2次，需要解码2次
                fileName = URLDecoder.decode(fileName,"UTF-8");
                //对文件名进行MD5编码，用于下载
                String md5FileName=MD5.getMD5Str(fileName);
                String filePath = new TxtFileUtil().getWebRoot() + BASE_DIR + File.separator + fileid + File.separator + md5FileName + ".csv";

                iReportMtBiz.updateRptRecord(fileid,"0",false,true);
                CSVUtils.downloadFile(response,request,filePath,fileName+ ".csv");
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "下载下行导出文件，异常。");
        }
    }

    /**
     * 获取并设置业务类型到request里
     * @param lgcorpcode 企业编码
     * @param request 请求对象
     * @return 成功返回true
     */
    private boolean getAndSetBus(String lgcorpcode, HttpServletRequest request)
    {
        try
        {
            // 查询条件
            LinkedHashMap<String, String> conditionMMap = new LinkedHashMap<String, String>();
            // 获取业务类型
            if(!"100000".equals(lgcorpcode))
            {
                // 只显示自定义业务
                conditionMMap.put("corpCode&in", "0," + lgcorpcode);
            }
            else
            {
                conditionMMap.put("corpCode&not in", "1,2");
            }

            BaseBiz baseBiz	= new BaseBiz();
            List<LfBusManager> busList = baseBiz.getByCondition(LfBusManager.class, null, conditionMMap, null);
            LinkedHashMap<String, String> busMap = new LinkedHashMap<String, String>();
            if(busList != null && busList.size() > 0)
            {
                for (LfBusManager bus : busList)
                {
                    busMap.put(bus.getBusCode(), bus.getBusName());
                }
            }
            request.setAttribute("busMap", busMap);
            return true;
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "系统下行记录，获取并设置业务类型，异常。");
            return false;
        }
    }

    /**
     * 获取并设置通道和发送账号
     * @param lgcorpcode 企业编码
     * @param request 请求对象
     * @return 成功返回true
     */
    private boolean getAndSetSp(String lgcorpcode, HttpServletRequest request)
    {
        try
        {
            SysMtRecordBiz mtRecordBiz = new SysMtRecordBiz();
            List<DynaBean> spList = mtRecordBiz.getSpgateList(lgcorpcode);
            request.setAttribute("spList", JSON.parseArray(dynaBeans2Json(spList)));

            // 页面SP账号查询条件
            List<String> lfsp = mtRecordBiz.getSpUserList(lgcorpcode);
            request.setAttribute("mrUserList", lfsp);
            return true;
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "系统下行记录，获取并设置通道和发送账号，异常。");
            return false;
        }
    }
}
