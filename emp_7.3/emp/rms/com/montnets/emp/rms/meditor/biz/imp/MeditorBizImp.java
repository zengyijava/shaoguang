package com.montnets.emp.rms.meditor.biz.imp;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.template.LfShortTemp;
import com.montnets.emp.entity.template.LfTemplate;
import com.montnets.emp.entity.template.LfTmplRela;
import com.montnets.emp.rms.commontempl.entity.LfIndustryUse;
import com.montnets.emp.rms.dao.UserDataDAO;
import com.montnets.emp.rms.meditor.biz.MeditorBiz;
import com.montnets.emp.rms.meditor.biz.TemplateBiz;
import com.montnets.emp.rms.meditor.config.LfTemplateConfig;
import com.montnets.emp.rms.meditor.config.UploadFileConfig;
import com.montnets.emp.rms.meditor.dao.MeditorDao;
import com.montnets.emp.rms.meditor.dao.imp.MeditorDaoImp;
import com.montnets.emp.rms.meditor.dto.DetailInfo;
import com.montnets.emp.rms.meditor.dto.ListDtoData;
import com.montnets.emp.rms.meditor.dto.PageListDto;
import com.montnets.emp.rms.meditor.dto.TempDetailDto;
import com.montnets.emp.rms.meditor.dto.TempListDto;
import com.montnets.emp.rms.meditor.entity.*;
import com.montnets.emp.rms.meditor.table.TableLfSubTemplate;
import com.montnets.emp.rms.meditor.tools.*;
import com.montnets.emp.rms.meditor.vo.LfFodderVo;
import com.montnets.emp.rms.meditor.vo.TempHFiveDetailVo;
import com.montnets.emp.rms.meditor.vo.TempHFiveVo;
import com.montnets.emp.rms.meditor.vo.TempsVo;
import com.montnets.emp.rms.rmsapi.biz.RMSApiBiz;
import com.montnets.emp.rms.rmsapi.biz.impl.IRMSApiBiz;
import com.montnets.emp.rms.rmsapi.model.TempParams;
import com.montnets.emp.rms.templmanage.biz.RmsShortTemplateBiz;
import com.montnets.emp.rms.tools.IMGTool;
import com.montnets.emp.rms.tools.ZipTool;
import com.montnets.emp.rms.vo.LfShortTemplateVo;
import com.montnets.emp.rms.vo.LfTemplateChartVo;
import com.montnets.emp.rms.vo.UserDataVO;
import com.montnets.emp.security.keyword.KeyWordAtom;
import com.montnets.emp.table.template.TableLfShortTemp;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.StringUtils;
import com.montnets.emp.util.TxtFileUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MeditorBizImp extends BaseBiz implements MeditorBiz {
    // 模板路径
    private static final String TEMPLATEPATH = "file/templates/";
    //上传的服务器IP地址
    private static final String uploadPath = SystemGlobals.getValue("montnet.rms.uploadPath").trim();
    private static final String rootPath = SystemGlobals.getValue("montnet.rms.nginx.rootPath").trim();
    MeditorDao meditorDao = new MeditorDaoImp();
    ZipDownUtil zipDownUtil = new ZipDownUtil();
    private final RmsShortTemplateBiz rstlBiz = new RmsShortTemplateBiz();
    UserUtil userUtil = new UserUtil();
    private final KeyWordAtom keyWordAtom = new KeyWordAtom();

    @Override
    public void getTemps(TempsVo tempsVo, HttpServletRequest request,
                         HttpServletResponse response) {

        if (null == tempsVo) {
            JsonReturnUtil.fail(99, TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.PARAM_RECEIVE_EXCEPTION), request, response);
            return;
        }
        PageListDto pageListDto = null;
        try {
            pageListDto = meditorDao.getTemps(tempsVo, request, response);
        } catch (Exception e) {
            JsonReturnUtil.fail(99, TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.SYSTEM_EXCEPTION), request, response);
            EmpExecutionContext.error(e.getMessage());
        }

        // 获取分页数据
        PageInfo pageInfo = pageListDto.getPageInfo();
        // 获取列表数据
        List<TempsVo> tempsVos = (List<TempsVo>) pageListDto.getOb();

        // 组装返回前端数据格式
        List<TempListDto> tempListDtos = new ArrayList<TempListDto>();
        // 遍历查询出的列表数据组装成返回前端数据格式
        for (TempsVo tempsV : tempsVos) {
            DetailInfo detailInfo = new DetailInfo();
            TempListDto tempListDto = new TempListDto();
            if (null != tempsV.getTmName()) {
                detailInfo.setTmName(tempsV.getTmName());
                tempListDto.setTmName(tempsV.getTmName());
            }
            if (null != tempsV.getTmId()) {
                detailInfo.setTmId(tempsV.getTmId());
                tempListDto.setTmId(tempsV.getTmId());
            }
            if (null != tempsV.getUsecount()) {
                detailInfo.setUsecounts(tempsV.getUsecount());
            }
            if (null != tempsV.getDegreeSize()) {
                detailInfo.setDegreeSize(tempsV.getDegreeSize());
            }
            if (null != tempsV.getDegree()) {
                detailInfo.setDegree(tempsV.getDegree());
            }
            if (null != tempsV.getDsFlag()) {
                detailInfo.setDsFlag(tempsV.getDsFlag());
            }
            if (null != tempsV.getUserId()) {
                // 根据用户id获取用户信息
                LfSysuser currUser = null;
                try {
                    currUser = empDao.findObjectByID(LfSysuser.class,
                            tempsV.getUserId());
                } catch (Exception e) {
                    EmpExecutionContext.error(e.getMessage());
                }
                if (null != currUser.getName()) {
                    detailInfo.setCreateUser(currUser.getName());
                }
                if (null != currUser.getCorpCode()) {
                    if(StaticValue.getCORPTYPE() == 1){//只有托管版才有企业编码
                        detailInfo.setCorpCode(currUser.getCorpCode());
                    }else{//标准版没有企业编码
                        detailInfo.setCorpCode("");
                    }
                }
                // 获取机构名称
                if (null != currUser.getDepId()) {
                    LfDep lfDep = null;
                    try {
                        lfDep = empDao.findObjectByID(LfDep.class,
                                currUser.getDepId());
                    } catch (Exception e) {
                        EmpExecutionContext.error(e.getMessage());
                    }
                    if (null != lfDep.getDepName()) {
                        detailInfo.setDepName(lfDep.getDepName());
                    }
                }
                // 根据快捷场景表是否有关联数据判断是否是快捷场景
                LfShortTemplateVo lfShortTemplateVo = new LfShortTemplateVo();
                lfShortTemplateVo.setUserId(tempsV.getUserId());
                LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
                LfSysuser lfSysuser = UserUtil.getUser(request);
                conditionMap.put(TableLfShortTemp.USERID_ENTITY, String.valueOf(lfSysuser.getUserId()));
                conditionMap.put(TableLfShortTemp.TEMPID_ENTITY, String.valueOf(tempsV.getTmId()));
                List<LfShortTemp> lfShortTemp = null;
                try {
                    lfShortTemp = empDao.findListByCondition(LfShortTemp.class,
                            conditionMap, null);
                } catch (Exception e) {
                    EmpExecutionContext.error(e.getMessage());
                }
                if (lfShortTemp.size() > 0) {
                    tempListDto.setIsShortTmp(LfTemplateConfig.IS_SHORT_TEMP);
                } else {
                    tempListDto.setIsShortTmp(LfTemplateConfig.IS_NOT_SHORT_TEMP);
                }
                //判断模板是否是别人共享的
                boolean isShare = isShare(tempListDto.getTmId(), lfSysuser);
                if (isShare) {
                    tempListDto.setIsShare(1);
                } else {
                    tempListDto.setIsShare(0);
                }
            }
            if (null != tempsV.getAddTime()) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                    long time = sdf.parse(tempsV.getAddTime().toString()).getTime();
                    detailInfo.setAddTime(time);
                    tempListDto.setAddTime(time);
                } catch (ParseException e) {
                    EmpExecutionContext.error("场景时间格式化异常：" + e);
                }
            }
            if (null != tempsV.getTmState()) {
                detailInfo.setTmState(tempsV.getTmState());
                tempListDto.setTmState(tempsV.getTmState());
            }
            if (null != tempsV.getAuditStatus()) {
                detailInfo.setAuditStatus(tempsV.getAuditStatus());
                tempListDto.setAuditStatus(tempsV.getAuditStatus());
            }

            if (null != tempsV.getTmpType()) {
                tempListDto.setTmpType(tempsV.getTmpType());
            }
            if (null != tempsV.getSptemplid()) {
                tempListDto.setSptemplid(tempsV.getSptemplid());
                detailInfo.setSptemplid(tempsV.getSptemplid());
            }
            if (null != tempsV.getContent()) {
                tempListDto.setContent(tempsV.getContent());
            }
            if (null != tempsV.getH5Type()) {
                tempListDto.setH5Type(tempsV.getH5Type());
            }
            if (null != tempsV.getH5Url()) {
                tempListDto.setH5Url(tempsV.getH5Url());
            }

            List<String> listStr = null;
            try {
                listStr = meditorDao.getTempContents(String.valueOf(tempsV.getTmId()), "1", String.valueOf(tempsV.getTmpType()));
            } catch (Exception e) {
                EmpExecutionContext.error(e.getMessage());
                JsonReturnUtil.fail(TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.SYSTEM_EXCEPTION), request, response);
                return;
            }
            String strResult = StringUtils.listToStr(listStr);
            tempListDto.setContent(strResult);

            List<String> listStrAppCont = null;
            try {
                listStrAppCont = meditorDao.getTempContents(String.valueOf(tempsV.getTmId()), "3", String.valueOf(tempsV.getTmpType()));
            } catch (Exception e) {
                EmpExecutionContext.error(e.getMessage());
                JsonReturnUtil.fail(TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.SYSTEM_EXCEPTION), request, response);
                return;
            }
            String listStrAppContResult = StringUtils.listToStr(listStrAppCont);
            tempListDto.setApp(listStrAppContResult);

            if (null != tempsV.getIndustryId()) {
                tempListDto.setIndustryId(tempsV.getIndustryId());
            }
            if (null != tempsV.getUseId()) {
                tempListDto.setUseId(tempsV.getUseId());
            }
            if (null != tempsV.getIsPublic()) {
                tempListDto.setIsPublic(tempsV.getIsPublic());
            }
            if (null != tempsV.getFileUrl()) {
                tempListDto.setFileUrl(tempsV.getFileUrl());
            }
            //模板资源文件同步
            if (StringUtils.isNotBlank(tempsV.getTmMsg())) {
                // V2.0 V文件路径
                String relativePath = tempsV.getTmMsg();
                if ("V2.0".equals(tempsV.getVer())) {
                    String[] pathArray = relativePath.split("/");
                    String fileName = pathArray[pathArray.length - 2];
                    relativePath = relativePath.substring(0, relativePath.lastIndexOf("/")) + fileName + ".zip";
                }
                boolean result = false;
                try {
                    if (relativePath.contains("/rms/")) {
                        result = true;
                    } else {//V3.0的才进行下载
                        result = zipDownUtil.tempalteDown(relativePath,tempsV.getTmState());
                    }
                } catch (Exception e) {
                    EmpExecutionContext.error(e, "模板资源文件下载失败:" + tempsV.getTmId());
                }
                /*if (!result) {
                    EmpExecutionContext.error("文件下载失败:id=" + tempsV.getTmId() + ";tmMsg=:" + tempsV.getTmMsg());
                }*/
            }
            tempListDto.setDetailInfo(detailInfo);
            tempListDtos.add(tempListDto);
        }
        ListDtoData tempListBtoData = new ListDtoData();
        tempListBtoData.setList(tempListDtos);
        tempListBtoData.setTotalPage(pageInfo.getTotalPage());
        tempListBtoData.setTotalRecord(pageInfo.getTotalRec());
        // 返回
        JsonReturnUtil.success(tempListBtoData, TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.SELECT_SUCCESS), request, response);
    }

    @Override
    public void deleteTemp(TempsVo tempsVo, HttpServletRequest request,
                           HttpServletResponse response) {

        Connection connection = empTransDao.getConnection();

        if (null == tempsVo || null == tempsVo.getTmId()) {
            JsonReturnUtil.fail(TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.TEMPLATE_NOT_FIND), request, response);
            return;

        }
        try {
            LfTemplate lfTemplate = empDao.findObjectByID(LfTemplate.class, tempsVo.getTmId());
            if (null == lfTemplate) {
                JsonReturnUtil.fail(TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.TEMPLATE_NOT_FIND), request, response);
                return;
            }
        } catch (Exception e) {
            JsonReturnUtil.fail(TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.SYSTEM_EXCEPTION), request, response);
            EmpExecutionContext.error(e, "删除模板中查询模板异常");
        }
        Long tmId = tempsVo.getTmId();
        Integer delCount = null;
        try {
            // 事物控制-开始
            empTransDao.beginTransaction(connection);
            LinkedHashMap<String, String> conditionMapShortTemp = new LinkedHashMap<String, String>();
            conditionMapShortTemp.put("tempId", String.valueOf(tmId));

            /*LinkedHashMap<String, String> conditionMapTempParam = new LinkedHashMap<String, String>();
            conditionMapTempParam.put("tmId", String.valueOf(tmId));*/

           /* LinkedHashMap<String, String> conditionMapSubTemplate = new LinkedHashMap<String, String>();
            conditionMapSubTemplate.put("tmId", String.valueOf(tmId));*/

            empTransDao.delete(connection, LfShortTemp.class, conditionMapShortTemp);
            //empTransDao.delete(connection, LfTempParam.class, conditionMapTempParam);
            //empTransDao.delete(connection, LfSubTemplate.class, conditionMapSubTemplate);
            //delCount = empTransDao.delete(connection, LfTemplate.class,
            //       String.valueOf(tmId));
            //假删除
            LfTemplate lfTemplate = empDao.findObjectByID(LfTemplate.class, tmId);
            lfTemplate.setTmState(3L);//删除状态
            if (null == lfTemplate.getSptemplid() || lfTemplate.getSptemplid() == 0){
                lfTemplate.setSptemplid(null);
            }
            boolean updateTem= empTransDao.update(connection, lfTemplate);


            // 事物提交
            empTransDao.commitTransaction(connection);
            // 事物控制-结束
            // 返回
            /*if (null != delCount && delCount > 0) {
                JsonReturnUtil.success(request, response);
                return;
            } else {
                JsonReturnUtil.fail(TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.TEMPLATE_DELETE_FAIL), request, response);
                return;
            }*/
            if (updateTem) {
                JsonReturnUtil.success(request, response);
                return;
            } else {
                JsonReturnUtil.fail(TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.TEMPLATE_DELETE_FAIL), request, response);
                return;
            }

        } catch (Exception e) {
            JsonReturnUtil.fail(99, TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.TEMPLATE_DELETE_FAIL), request, response);
            empTransDao.rollBackTransaction(connection);
            EmpExecutionContext.error(e, "模板删除异常");
        } finally {
            empTransDao.closeConnection(connection);
        }

    }

    @Override
    public void changeTmState(TempsVo tempsVo, HttpServletRequest request,
                              HttpServletResponse response) {
        if (null == tempsVo) {
            JsonReturnUtil.fail(TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.PARAM_RECEIVE_EXCEPTION), request, response);
            return;
        }
        if (null != tempsVo.getTmId()) {
            LfTemplate lfTemplate = null;
            try {
                lfTemplate = empDao.findObjectByID(LfTemplate.class,
                        tempsVo.getTmId());
            } catch (Exception e) {
                EmpExecutionContext.error(e.getMessage());
            }
            if (null == lfTemplate) {
                JsonReturnUtil.fail(TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.TEMPLATE_NOT_FIND), request, response);
                return;
            }
            lfTemplate.setTmState(tempsVo.getTmState());
            boolean res = false;
            try {
                res = empDao.update(lfTemplate);
            } catch (Exception e) {
                EmpExecutionContext.error(e.getMessage());
            }
            if (res) {
                JsonReturnUtil.success(lfTemplate, request, response);
                return;
            } else {
                JsonReturnUtil.fail(TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.SYSTEM_EXCEPTION), request, response);
                return;
            }
        }
    }

    @Override
    public void getTempDetail(TempsVo tempsVo, HttpServletRequest request,
                              HttpServletResponse response) {
        if (null == tempsVo) {
            JsonReturnUtil.fail(TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.PARAM_RECEIVE_EXCEPTION), request, response);
            return;
        }
        if (null == tempsVo.getTmId()) {
            JsonReturnUtil.fail(TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.TEMPLATE_NOT_FIND), request, response);
            return;
        }

        TempDetailDto tempDetailDto = new TempDetailDto();
        LfTemplate lfTemplate = null;
        try {
            lfTemplate = empDao.findObjectByID(LfTemplate.class,
                    tempsVo.getTmId());
        } catch (Exception e) {
            EmpExecutionContext.error(e.getMessage());
            JsonReturnUtil.fail(TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.SYSTEM_EXCEPTION), request, response);
            return;
        }
        if (null == lfTemplate) {
            JsonReturnUtil.fail(TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.TEMPLATE_NOT_FIND), request, response);
            return;
        }
        if (lfTemplate.getTmState() == 3 || lfTemplate.getRcosTmpState() == 2){
            JsonReturnUtil.fail(TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.TEMPLATE_NOT_FIND), request, response);
        }
        // 如果不是公共场景，则只能查询自己或下属建的模板
        if (LfTemplateConfig.IS_NOT_PUBLIC == lfTemplate.getIsPublic()) {
            LfSysuser sysuser = UserUtil.getUser(request);
            // 权限验证,查询自己和下属的id
            String ids = userUtil.getPermissionUserCode(sysuser);
            boolean containId = false;
            //若创建模板的用户id包含在其中，则可以查看
            if (StringUtils.isNotBlank(ids)) {
                String[] idsArr = ids.split(",");
                for (String ele : idsArr) {
                    if (ele.equals(String.valueOf(lfTemplate.getUserId()))) {
                        containId = true;
                        break;
                    }
                }
                boolean isShare = isShare(lfTemplate.getTmid(), sysuser);
                if (!containId && !isShare) {
                    JsonReturnUtil.fail(TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.PERMISSION_DENIED), request, response);
                    return;
                }
            }

        }
        if (null != lfTemplate.getUseid()) {
            tempDetailDto.setUseId(lfTemplate.getUseid());
        }
        if (null != lfTemplate.getIndustryid()) {
            tempDetailDto.setIndustryId(lfTemplate.getIndustryid());
        }
        /*if (null != lfTemplate.getTmpType()) {
            tempDetailDto.setTmpType(lfTemplate.getTmpType());
        }*/
        if (null != lfTemplate.getSptemplid()) {
            tempDetailDto.setSptemplid(lfTemplate.getSptemplid());
        }
        if (null != lfTemplate.getTmName()) {
            tempDetailDto.setTmName(lfTemplate.getTmName());
        }
        if (null != lfTemplate.getDsflag()) {
            tempDetailDto.setDsflag(lfTemplate.getDsflag());
        }
        if (null != lfTemplate.getVer()) {
            tempDetailDto.setVer(lfTemplate.getVer());
        }

        //模板资源文件同步
        if (StringUtils.isNotBlank(lfTemplate.getTmMsg())) {
            // V2.0 文件路径
            String relativePath = lfTemplate.getTmMsg();
            if ("V2.0".equals(lfTemplate.getVer())) {
                String[] pathArray = relativePath.split("/");
                String fileName = pathArray[pathArray.length - 2];
                relativePath = relativePath.substring(0, relativePath.lastIndexOf("/")) + fileName + ".zip";
            }
            boolean result = false;
            try {
                if (relativePath.contains("/rms/")) {
                    result = true;
                } else {//V3.0的才进行下载
                    result = zipDownUtil.tempalteDown(relativePath,lfTemplate.getTmState());
                }
            } catch (Exception e) {
                EmpExecutionContext.error(e, "模板资源文件下载失败:" + lfTemplate.getTmid());
            }
            if (!result) {
                EmpExecutionContext.error("文件下载失败:id=" + lfTemplate.getTmid() + ";tmMsg=:" + lfTemplate.getTmMsg());
            }
        }

        //模板子表查询
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        if (null != tempsVo.getPreviewType()) {
            // 只查询出主数据
            if (LfTemplateConfig.PREVIEW_TYPE_ONE == tempsVo.getPreviewType()) {
                conditionMap.put(TableLfSubTemplate.PRIORITY_ENTITY,
                        LfTemplateConfig.SUB_TEMP_PRIORITY.toString());
            }
        }
        conditionMap.put(TableLfSubTemplate.TM_ID_ENTITY, tempsVo.getTmId()
                .toString());
        try {
            List<LfSubTemplate> lfSubTemplates = empDao
                    .findListByCondition(LfSubTemplate.class, conditionMap,
                            null);
            for (LfSubTemplate lfSubTemplate : lfSubTemplates) {
                List<String> listStr = meditorDao.getTempContents(String.valueOf(lfSubTemplate.getTmId()), "1", String.valueOf(lfSubTemplate.getTmpType()));
                String strResult = StringUtils.listToStr(listStr);
                lfSubTemplate.setContent(strResult);

                List<String> listStrAppCont = meditorDao.getTempContents(String.valueOf(lfSubTemplate.getTmId()), "3", String.valueOf(lfSubTemplate.getTmpType()));
                String listStrAppContResult = StringUtils.listToStr(listStrAppCont);
                lfSubTemplate.setApp(listStrAppContResult);
            }
            tempDetailDto.setList(lfSubTemplates);
            for (LfSubTemplate lfSubTemplate : lfSubTemplates) {
                if (LfTemplateConfig.PREVIEW_TYPE_ONE == lfSubTemplate.getPriority()) {
                    tempDetailDto.setTmpType(lfSubTemplate.getTmpType());
                }
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e.getMessage());
            JsonReturnUtil.fail(TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.SYSTEM_EXCEPTION), request, response);
            return;
        }
        //参数表查询
        LinkedHashMap<String, String> conditionMapPar = new LinkedHashMap<String, String>();
        conditionMapPar.put(TableLfSubTemplate.TM_ID_ENTITY, tempsVo.getTmId()
                .toString());

        try {
            List<LfTempParam> lfTempParams = empDao.findListByCondition(LfTempParam.class, conditionMapPar,
                    null);
            tempDetailDto.setParamArr(lfTempParams);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "发现异常");
        }
        JsonReturnUtil.success(tempDetailDto, request, response);
        return;
    }

    @Override
    public void createPicture(LfTemplateChartVo templateChartVo,
                              HttpServletRequest request, HttpServletResponse response) {
        // TODO Auto-generated method stub
        // Gson json = new Gson();
        try {
            // 获取年月以及随机数
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
            Date date = new Date();
            String YandM = sdf.format(date);
            SimpleDateFormat sdfs = new SimpleDateFormat("ddHHmmssSSS");
            String nameVariable = sdfs.format(date);

            // 图形类型 （1：饼状图，2：柱状图，3：折线图，4：工资条，5：表格，默认是1）
            String chartType = templateChartVo.getChartType();
            // 图形标题
            String chartTitle = templateChartVo.getChartTitle();
            // 数据类型（1：静态，2：数值动态,3：全动态，默认是1）
            String ptType = templateChartVo.getPtType();
            // 饼状图颜色
            String color = templateChartVo.getColor();
            // 饼状图第二列数值
            String rowValue = templateChartVo.getRowValue();
            // 柱状图折线图行名
            String barRowName = templateChartVo.getBarRowName();
            // 饼状图、柱状图、折线图列名
            String barColName = templateChartVo.getBarColName();
            // 柱状图折线图数值(以行为单位，用“,”隔开，换行则用“@”隔开)
            String barValue = templateChartVo.getBarValue();
            // 柱状图折线图表格所有数据，用于回显(以行为单位，用“,”隔开，换行则用“@”隔开)
            String barTableVal = templateChartVo.getBarTableVal();
            // 动态参数的值
            String parmValue = templateChartVo.getParmValue();
            // 行数
            String rowNum = templateChartVo.getRowNum();
            // 列数
            String colNum = templateChartVo.getColNum();

            /*
             * //图形类型 （1：饼状图，2：柱状图，3：折线图，4：工资条，5：表格，默认是1） String chartType =
             * request.getParameter("chartType"); //图形标题 String chartTitle =
             * request.getParameter("chartTitle");
             * //数据类型（1：静态，2：数值动态,3：全动态，默认是1） String ptType =
             * request.getParameter("ptType"); //饼状图颜色 String color =
             * request.getParameter("color"); //饼状图第二列数值 String rowValue =
             * request.getParameter("rowValue"); //柱状图折线图行名 String barRowName =
             * request.getParameter("barRowName"); //饼状图、柱状图、折线图列名 String
             * barColName = request.getParameter("barColName");
             * //柱状图折线图数值(以行为单位，用“,”隔开，换行则用“@”隔开) String barValue =
             * request.getParameter("barValue");
             * //柱状图折线图表格所有数据，用于回显(以行为单位，用“,”隔开，换行则用“@”隔开) String barTableVal =
             * request.getParameter("barTableVal"); //动态参数的值 String parmValue =
             * request.getParameter("parmValue"); //行数 String rowNum =
             * request.getParameter("rowNum"); //列数 String colNum =
             * request.getParameter("colNum");
             */

            // 创建图片生成位置
            String deletePath = request.getSession().getServletContext()
                    .getRealPath("/pythonPicture/" + YandM)
                    .replaceAll("%20", " ");
            File f = new File(deletePath);
            if (!f.exists()) {
                f.mkdirs();
                EmpExecutionContext.info("程序在" + deletePath
                        + "目录下新建了pythonPicture的文件夹用于存放生成的图片");
            }

            // 图片保存的位置
            String pictureUrl = null;
            String colors = null;
            String rowNameVal = null;
            String[] args1 = null;
            String barRowNames = null;
            String barColNames = null;
            String barValues = null;
            String pictureUrls = null;
            if (("1").equals(chartType)) {
                pictureUrl = request.getSession().getServletContext()
                        .getRealPath("/pythonPicture/" + YandM)
                        .replaceAll("%20", " ")
                        + "/" + nameVariable + ".png";
                // 拼装python脚本所需变量
                colors = CreatePictureUtil.AssembleStr(color);
                // 拼装rowName
                barRowNames = CreatePictureUtil.AssembleRowName(barRowName,
                        rowValue, ptType);
                rowNameVal = CreatePictureUtil.AssembleStr(barRowNames);
                args1 = new String[]{
                        "python",
                        request.getSession().getServletContext()
                                .getRealPath("/rms/mbgl/pythonScript/pie.py")
                                .replaceAll("%20", " "), rowNameVal, rowValue,
                        colors, chartTitle, nameVariable, deletePath};
                pictureUrls = "pythonPicture/" + YandM + "/" + nameVariable
                        + ".png";
            } else if (("2").equals(chartType)) {
                // 柱状图折线图表列
                barRowNames = CreatePictureUtil.AssembleStr(barRowName);
                // 柱状图表行
                barColNames = CreatePictureUtil.AssembleStr(barColName);
                // 柱状图数值(以行为单位，用“,”隔开，换行则用“@”隔开)
                barValues = CreatePictureUtil.AssembleBarStr(barValue);
                pictureUrl = request.getSession().getServletContext()
                        .getRealPath("/pythonPicture/" + YandM)
                        .replaceAll("%20", " ")
                        + "/" + nameVariable + ".png";
                args1 = new String[]{
                        "python",
                        request.getSession().getServletContext()
                                .getRealPath("/rms/mbgl/pythonScript/bar.py")
                                .replaceAll("%20", " "), barRowNames,
                        barValues, barColNames, chartTitle, nameVariable,
                        deletePath};
                pictureUrls = "pythonPicture/" + YandM + "/" + nameVariable
                        + ".png";
            } else if (("3").equals(chartType)) {
                // 柱状图折线图表列
                barRowNames = CreatePictureUtil.AssembleStr(barRowName);
                // 柱状图表行
                barColNames = CreatePictureUtil.AssembleStr(barColName);
                // 柱状图数值(以行为单位，用“,”隔开，换行则用“@”隔开)
                barValues = CreatePictureUtil.AssembleBarStr(barValue);
                pictureUrl = request.getSession().getServletContext()
                        .getRealPath("/pythonPicture/" + YandM)
                        .replaceAll("%20", " ")
                        + "/" + nameVariable + ".png";
                args1 = new String[]{
                        "python",
                        request.getSession().getServletContext()
                                .getRealPath("/rms/mbgl/pythonScript/line.py")
                                .replaceAll("%20", " "), barRowNames,
                        barValues, barColNames, chartTitle, nameVariable,
                        deletePath};
                pictureUrls = "pythonPicture/" + YandM + "/" + nameVariable
                        + ".png";
            }
            Process pr = Runtime.getRuntime().exec(args1);
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    pr.getInputStream()));
            in.close();
            pr.waitFor();
            // 组装需要的bean数据并转换为json返回
            File file = new File(pictureUrl);
            if (!file.exists()) {
            } else {
                // 图片大小
                long pictureSize = file.length();
                LfTemplateChartVo bean = new LfTemplateChartVo();
                bean.setChartType(chartType);
                bean.setChartTitle(chartTitle);
                bean.setColor(color);
                bean.setPtType(ptType);
                bean.setRowValue(rowValue);
                bean.setBarColName(barColName);
                bean.setBarRowName(barRowName);
                bean.setBarTableVal(barTableVal);
                bean.setBarValue(barValue);
                bean.setPictureUrl(pictureUrls);
                bean.setPictureSize(pictureSize);
                bean.setParmValue(parmValue);
                bean.setRowNum(rowNum);
                bean.setColNum(colNum);
                /*
                 * pw.write(json.toJson(bean)); pw.flush();
                 */
                JsonReturnUtil.success(bean, request, response);
            }
        } catch (Exception e) {
            JsonReturnUtil.fail("生成图片失败", request, response);
            EmpExecutionContext.error(e, "生成图片出现异常！");
        }
    }

    @Override
    public void addIndustryUse(String name, String type, String tmpType,
                               HttpServletRequest request, HttpServletResponse response) {
        // dao层
        //MeditorDao meditorDao = new MeditorDaoImp();
        // TODO Auto-generated method stub
        /*
         * LfSysuser loginSysuser = (LfSysuser) request.getSession(false)
         * .getAttribute(StaticValue.SESSION_USER_KEY);
         */

        // 类型 0-行业，1-用途,默认-1 未知

        //
        /* Long userId = loginSysuser.getUserId(); */

        LfIndustryUse lfIndustryUse = new LfIndustryUse();
        lfIndustryUse.setName(name);
        LfSysuser loginSysuser = UserUtil.getUser(request);
        lfIndustryUse.setOperator(Long.valueOf(loginSysuser.getCorpCode()));
        lfIndustryUse.setType(Integer.parseInt(type));
        lfIndustryUse.setTmpType(Integer.parseInt(tmpType));
        lfIndustryUse.setCreatetm(new Timestamp(System.currentTimeMillis()));
        lfIndustryUse.setUpdatetm(new Timestamp(System.currentTimeMillis()));
        StringBuffer result = new StringBuffer();
        try {
            Long saveReturnId = 0L;
            // 判断添加的行业或用途是否存在
            LfIndustryUse lf = new LfIndustryUse();
            // 根据行业-用途名称 查询出唯一的一条记录
            lf.setName(name);
            lf.setTmpType(Integer.parseInt(tmpType));
            lf.setType(Integer.parseInt(type));// 区分行业还是用途-行业、用途的名称一样时，也可添加
            List<LfIndustryUse> list = meditorDao.getIndustryUseList(lf);
            if (null != list && list.size() > 0) {
                result.append("添加名称已存在，请重新输入！");
            } else {
                saveReturnId = addObjReturnId(lfIndustryUse);
                if (saveReturnId > 0) {
                    list = meditorDao.getIndustryUseList(lf);
                    JsonReturnUtil.success(list.get(0), request, response);
                } else {
                    JsonReturnUtil.fail(request, response);
                }
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "行业-用途Svt层添加出现异常！");
        }
    }

    @Override
    public void deleteIndustryUse(String id, HttpServletRequest request,
                                  HttpServletResponse response) {
        try {
            // TODO Auto-generated method stub.
            if (deleteByIds(LfIndustryUse.class, id) != null) {
                // 删除成功
                JsonReturnUtil.success(request, response);
            } else {
                JsonReturnUtil.fail(request, response);
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "发现异常");
        }
    }

    @Override
    public void getIndustryUses(String name, String type, String tmpType,
                                HttpServletRequest request, HttpServletResponse response) {
        try {
            List<LfIndustryUse> industryUseList = new ArrayList<LfIndustryUse>();
            LfIndustryUse lfIndustryUse = new LfIndustryUse();
            // 行业-用途名称
            if (!StringUtils.IsNullOrEmpty(name)) {
                lfIndustryUse.setName(name);
            }
            if (!StringUtils.IsNullOrEmpty(type)) {
                lfIndustryUse.setType(Integer.parseInt(type));
            }
            if (!StringUtils.IsNullOrEmpty(tmpType)) {
                lfIndustryUse.setTmpType(Integer.parseInt(tmpType));
            }
            industryUseList = meditorDao.getIndustryUseList(lfIndustryUse);
            if (industryUseList != null) {
                JsonReturnUtil.success(industryUseList, request, response);
            } else {
                JsonReturnUtil.fail(request, response);
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.SELECT_FAIL));
            JsonReturnUtil.fail(TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.SELECT_FAIL), request, response);
        }
    }


    @Override
    public void addTemplate(TempData tmpData, HttpServletRequest request,
                            HttpServletResponse response) {
        Locale locale = com.montnets.emp.i18n.util.MessageUtils.getLocale(request);
        Connection conn = empTransDao.getConnection();
        //获取当前语言
        String langName = (String) request.getSession(false).getAttribute(StaticValue.LANG_KEY);
        //模板ID
        Long tmid = 0L;
        //全局执行成功定义
        boolean result = false;
        //编辑标识-为 true 时，提交审核失败后，不能删除原来暂存的模板
        boolean editFlag = false;

        String h5URL = " ";
        //根据此-字段来决定走具体版本的上传模板协议
        String version = SystemGlobals.getValue("montnets.rms.editor.version", "V3.0");
        //是否创建卡片相关的资源文件
        String createOtt = SystemGlobals.getValue("montnets.rms.editor.createOtt", "false").trim();
        //操作信息返回前端Map
        Map<String, String> map2 = new HashMap<String, String>();
        StringBuilder keyWordBuilder = new StringBuilder();
        //异常处理
        if (StringUtils.isBlank(tmpData.getTmName()) && tmpData.getSubType() == 1) {
            JsonReturnUtil.fail(99, TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.TEMPLATE_NAME_CANNOT_BE_BLANK), request, response);
            return;
        }

        try {
            String corpCode = "";
            LfSysuser sysuser = UserUtil.getUser(request);
            corpCode = sysuser.getCorpCode();
            // 2.根据type 字段 判断是哪一类模板JSON
            //模板表实体类
            LfTemplate template = new LfTemplate();
            List<SubTempData> subDatas = tmpData.getTempArr();
            // 字模板集合
            List<LfSubTemplate> subTempList = new ArrayList<LfSubTemplate>();
            //参数实体类
            List<LfTempParam> paramList = new ArrayList<LfTempParam>();
            //子模板权重
            int priority = 0;
            String mcrslUrl = "";
            //短信内容
            String smContent = "";
            //短信参数正则
            String smParamReg = "";
            //前端JSON
            String frontJson = "";
            //appContent H5封面JSON
            String appContent = "";
            //组装模板表对象
            template = combineLFtemplate(sysuser, tmpData);
            //新建模板
            if (null == tmpData.getTmid() || tmpData.getTmid() == 0) {
                // 事物控制-开始
                empTransDao.beginTransaction(conn);
                // 主表入库生成TM_ID
                tmid = empTransDao.saveObjectReturnID(conn, template);
                result = true;
            } else {//编辑、复制
                tmid = tmpData.getTmid();
            }
            //参数表实体类赋值
            combineParam(tmid, tmpData, paramList);
            int degree = 0;
            int degreeSize = 0;
            //提交审核平台报文实体集合
            List<TempParams> subGwlist = new ArrayList<TempParams>();
            //主题加入关键字过滤
            if(null != template && StringUtils.isNotBlank(template.getTmName())){
                //主题含有关键字
                String keyWords = checkBadWord(template.getTmName().trim(), corpCode);
                if (!"".equals(keyWords)) {
                    //1表示主题
                    String msg = mutilKeyWordLanguage(keyWords, locale, 1);
                    JsonReturnUtil.fail(99, msg, request, response);
                    return;
                }
            }
            for (SubTempData subData : subDatas) {
                priority++;
                //组网关所需的byte[]
                if (null != subData) {
                    Integer type = subData.getTmpType();
                    switch (type) {
                        case 11:
                            Map<String, Object> richMap = analysisRichMedia(tmpData, subData, tmid, sysuser, priority, langName, paramList, subTempList, subGwlist, version, createOtt, keyWordBuilder);
                            //获取主方式的档位和容量
                            if (11 == tmpData.getTmpType()) {
                                degree = (Integer) richMap.get("degree");
                                degreeSize = (Integer) richMap.get("degreeSize");
                            }
                            break;
                        // 卡片
                        case 12:
                            //转换终端JSON
                            Map<String, Object> cardMap = analysisCard(tmpData, subData, tmid, sysuser, priority, langName, paramList, subTempList, subGwlist, version, createOtt, keyWordBuilder);
                            //获取主方式的档位和容量
                            if (12 == tmpData.getTmpType()) {
                                degree = (Integer) cardMap.get("degree");
                                degreeSize = (Integer) cardMap.get("degreeSize");
                            }
                            break;
                        // 富文本
                        case 13:
                            Map<String, Object> richTextMap = analysisRichText(tmpData, subData, tmid, sysuser, priority, langName, paramList, subTempList, subGwlist, version, createOtt, keyWordBuilder);
                            degree = (Integer) richTextMap.get("degree");
                            degreeSize = (Integer) richTextMap.get("degreeSize");
                            break;
                        case 14:
                            // 短信
                            Map<String, Object> smsMap = analysisSms(tmpData, subTempList, subData, tmid, priority, paramList, version, keyWordBuilder);
                            if (null != smsMap && smsMap.size() == 2) {
                                smContent = (String) smsMap.get("smContent");
                                smParamReg = (String) smsMap.get("smParamReg");
                            }
                            break;

                        case 15:
                            //H5
                            Map<String, Object> h5Map = analysisH5(tmpData, subData, tmid, sysuser, priority, paramList, subTempList, subGwlist, version, createOtt, keyWordBuilder);
                            smContent = (String) h5Map.get("smContent");
                            h5URL = (String) h5Map.get("h5URL");
                            appContent = (String) h5Map.get("appContent");
                            degree = (Integer) h5Map.get("degree");
                            degreeSize = (Integer) h5Map.get("degreeSize");
                            break;
                        default:
                            break;
                    }

                }

            }
            //关键字校验
            String keyWords = "";
            if(StringUtils.isNotEmpty(keyWordBuilder.toString().trim())){
                 keyWords = checkBadWord(keyWordBuilder.toString().trim().replace("\r\n", "").replace(" ", ""), corpCode);
            }
            //内容含有关键字
            if (!"".equals(keyWords)) {
                String msg = mutilKeyWordLanguage(keyWords, locale, 2);
                JsonReturnUtil.fail(99, msg, request, response);
                return;
            }
            //入库子表 和 参数表
            //新建模板
            if (null == tmpData.getTmid() || tmpData.getTmid() == 0) {
                int subCount = 0;
                if (tmid > 0) {
                    // 入库主表成功
                    for (com.montnets.emp.rms.meditor.entity.LfSubTemplate LfSubTemplate : subTempList) {
                        LfSubTemplate.setTmId(tmid);
                        LfSubTemplate.setCardHtml(" ");

                        String frontCont = LfSubTemplate.getFrontJson();
                        String endCont = LfSubTemplate.getEndJson();
                        // 模板子表入库
                        //子表的这三个字段因为模板结构字符串超过数据库该字段的实际存储结构，改为存储到 LF_TEMPCONTENT 拆分表中
                        LfSubTemplate.setFrontJson(" ");
                        LfSubTemplate.setContent(" ");
                        LfSubTemplate.setEndJson(" ");
                        LfSubTemplate.setH5Url(h5URL);
                        boolean save = empTransDao.save(conn, LfSubTemplate);
                        // 模板结构JSON拆分存储表入库
                        if (save) {
                            subCount++;
                            saveLftempContent(frontCont, endCont, LfSubTemplate, tmid, conn, appContent);
                        }
                    }

                }
                if (tmid > 0 && subCount == subTempList.size()) {
                    // 主表、子表都入库成功
                    if (null != paramList && paramList.size() > 0) {
                        empTransDao.save(conn, paramList, LfTempParam.class);
                    }
                    //暂存草稿
                    if (0 == tmpData.getSubType()) {
                        map2.put("tmpId", String.valueOf(tmid));
                        result = true;
                    }
                } else {
                    result = false;
                }
                // 事物提交
                empTransDao.commitTransaction(conn);
            } else {
                //编辑模板
                editFlag = true;
                tmid = tmpData.getTmid();
                int subCount = 0;
                //更新子表
                for (com.montnets.emp.rms.meditor.entity.LfSubTemplate LfSubTemplate : subTempList) {
                    LfSubTemplate.setTmId(tmid);
                    // 取消自动提交方式
                    empTransDao.beginTransaction(conn);
                    //修改筛选条件
                    LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
                    map.put("tmId", tmid.toString());
                    map.put("tmpType", LfSubTemplate.getTmpType().toString());
                    List<com.montnets.emp.rms.meditor.entity.LfSubTemplate> list = empDao.findListByCondition(com.montnets.emp.rms.meditor.entity.LfSubTemplate.class, map, null);
                    com.montnets.emp.rms.meditor.entity.LfSubTemplate lt = null;
                    //插入参数表
                    LinkedHashMap<String, String> paramMap = new LinkedHashMap<String, String>();
                    paramMap.put("tmId", String.valueOf(tmid));
                    List<LfTempParam> lfTempParams = empDao.findListByCondition(LfTempParam.class, paramMap, null);
                    if (!lfTempParams.isEmpty()) {

                        empTransDao.delete(conn, LfTempParam.class, paramMap);
                    }
                    // 模板子表入库
                    if (null != paramList && paramList.size() > 0) {
                        empTransDao.save(conn, paramList, LfTempParam.class);
                    }
                    if (null != list && list.size() > 0) {
                        lt = list.get(0);
                        LfSubTemplate.setId(lt.getId());
                        String frontCont = LfSubTemplate.getFrontJson();
                        String endContent = LfSubTemplate.getEndJson();
                        //子表的这三个字段因为模板结构字符串超过数据库该字段的实际存储结构，改为存储到 LF_TEMPCONTENT 拆分表中
                        LfSubTemplate.setFrontJson(" ");
                        LfSubTemplate.setContent(" ");
                        LfSubTemplate.setEndJson(" ");
                        LfSubTemplate.setH5Url(h5URL);
                        // 模板子表入库
                        //更新子表
                        boolean save = empTransDao.update(conn, LfSubTemplate);
                        // 模板结构JSON拆分存储表入库
                        if (save) {
                            //删除拆分存储里面的源JSON
                            //插入参数表
                            LinkedHashMap<String, String> contMap = new LinkedHashMap<String, String>();
                            contMap.put("tmId", String.valueOf(tmid));
                            contMap.put("tmpType", LfSubTemplate.getTmpType().toString());
                            List<LfTempContent> lfContents = empDao.findListByCondition(LfTempContent.class, contMap, null);
                            if (!lfContents.isEmpty()) {
                                int delete = empTransDao.delete(conn, LfTempContent.class, contMap);
                            }
                            //更新新的拆分后的模板结构JSON
                            saveLftempContent(frontCont, endContent, LfSubTemplate, tmid, conn, appContent);
                            subCount++;
                        }
                    }
                }
                // 事物提交
                empTransDao.commitTransaction(conn);
                // 事物控制-结束
                // 子表更新成功
                if (subCount == subTempList.size()) {
                    //type = 0 - 暂存草稿
                    if (tmpData.getSubType() == 0) {
                        map2.put("tmpId", String.valueOf(tmid));
                    }
                    result = true;
                } else {
                    result = false;
                }
            }
            //入库EXTJSON
            tmpData.setTmid(tmid);
            ParamTool.saveExcleJson(tmpData, locale);

            //压缩资源文件
            String sourcePath = new TxtFileUtil().getWebRoot() + TEMPLATEPATH + corpCode + "/" + tmid;
            String zipPath = new File(sourcePath + ".zip").getPath();
            //压缩 绝对路径
            ZipTool.createZip(sourcePath, zipPath);
            String fileCenterPath = TEMPLATEPATH + corpCode + "/" + tmid + ".zip";
            boolean uploadFlag = FileOperatorTool.uploadFileCenter(fileCenterPath);

            //用来存储错误描述的
            Map<String, String> resultMap=new HashMap<String, String>();
            if (1 == tmpData.getSubType()) {
                //1-提交审核平台
                //短信主题
                String title = (null == tmpData.getTmName()) ? "" : tmpData.getTmName();
                String templVer = "3";
                String spTmpID = "";
                if ("V2.0".equalsIgnoreCase(version)) {
                    spTmpID = submGwCenterV2(corpCode, subGwlist,resultMap);
                } else {
                    spTmpID = submGwCenterV3(corpCode, subGwlist, smContent, smParamReg, title, templVer,resultMap);
                }
                if (!StringUtils.IsNullOrEmpty(spTmpID)) {
                    //更新本地模板表
                    LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
                    map.put("tmid", tmid.toString());
                    List<LfTemplate> list = empDao.findListByCondition(LfTemplate.class, map, null);
                    if (null != list && list.size() > 0) {
                        LfTemplate lfTemplate = list.get(0);
                        lfTemplate.setTmid(tmid);
                        //模板状态为启用
                        lfTemplate.setTmState(1L);
                        lfTemplate.setTmMsg(fileCenterPath);
                        lfTemplate.setSptemplid(Long.parseLong(spTmpID));
                        lfTemplate.setTmName(null == tmpData.getTmName() ? "" : tmpData.getTmName());
                        lfTemplate.setDegree(degree);
                        lfTemplate.setDegreeSize(Long.parseLong(String.valueOf(degreeSize)));
                        lfTemplate.setParamsnum(Integer.parseInt(lfTemplate.getDsflag().toString()));
                        //提交时候审核直接将状态改为：3-审核中
                        lfTemplate.setAuditstatus(3);
                        boolean update = empTransDao.update(conn, lfTemplate);
                        if (update) {
                            map2.put("tmpId", String.valueOf(tmid));
                            map2.put("spTmpId", spTmpID);
                        }
                    }
                    result = true;
                } else {
                    result = false;
                }
            } else {
                LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
                map.put("tmid", tmid.toString());
                List<LfTemplate> list = empDao.findListByCondition(LfTemplate.class, map, null);
                if (null != list && list.size() > 0) {
                    LfTemplate uplfTemplate = list.get(0);
                    uplfTemplate.setTmid(tmid);
                    //静动态模板标识 0 -静态，1-动态
                    long dsFlag = 0L;
                    //参数个数
                    int paramCnt = 0;
                    if (null != tmpData.getParamArr() && tmpData.getParamArr().size() > 0) {
                        //动态模板
                        dsFlag = 1L;
                        paramCnt = tmpData.getParamArr().size();
                    } else {
                        //静态模板
                        dsFlag = 0L;
                        paramCnt = 0;

                    }
                    uplfTemplate.setParamcnt(paramCnt);
                    uplfTemplate.setDsflag(dsFlag);
                    uplfTemplate.setDegree(degree);
                    uplfTemplate.setTmMsg(fileCenterPath);
                    uplfTemplate.setDegreeSize(Long.parseLong(String.valueOf(degreeSize)));
                    uplfTemplate.setParamsnum(Integer.parseInt(uplfTemplate.getDsflag().toString()));
                    //保存的模板此审核ID值给NULL,不给默认的0
                    uplfTemplate.setSptemplid(null);
                    empTransDao.update(conn, uplfTemplate);

                }
            }
            // 操作成功
            if (result) {
                //提交审核
                if (1 == tmpData.getSubType().intValue()) {
                    JsonReturnUtil.success(map2, TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.SUBMIT_SUCCESS), request, response);
                    return;
                }
                //暂存草稿
                if (0 == tmpData.getSubType().intValue()) {
                    JsonReturnUtil.success(map2, TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.SAVE_SUCCESS), request, response);
                    return;
                }
            }
            //出现任何异常，都将该记录删除
            if (!result) {
                if (!editFlag) {
                    empTransDao.beginTransaction(conn);
                    //删除主表记录
                    LinkedHashMap<String, String> tempMap = new LinkedHashMap<String, String>();
                    tempMap.put("tmid", String.valueOf(tmid));
                    empTransDao.delete(conn, LfTemplate.class, tempMap);

                    //删除子表记录
                    LinkedHashMap<String, String> subMap = new LinkedHashMap<String, String>();
                    subMap.put("tmId", String.valueOf(tmid));
                    empTransDao.delete(conn, LfSubTemplate.class, subMap);

                    //删除参数表记录
                    LinkedHashMap<String, String> paramMap = new LinkedHashMap<String, String>();
                    paramMap.put("tmId", String.valueOf(tmid));
                    empTransDao.delete(conn, LfTempParam.class, paramMap);

                    //删除JSON结构拆分表记录
                    LinkedHashMap<String, String> tempContmMap = new LinkedHashMap<String, String>();
                    paramMap.put("tmId", String.valueOf(tmid));
                    empTransDao.delete(conn, LfTempContent.class, tempContmMap);

                    empTransDao.commitTransaction(conn);
                }
                //提交审核
                if (1 == tmpData.getSubType().intValue()) {
                    if(resultMap!=null&&resultMap.size()>0) {
                        String returnResult = resultMap.get("result");
                        if (returnResult != null) {
                            returnResult = returnResult.trim();
                        }
                        String returnDesc = resultMap.get("desc");
                        if (returnDesc != null) {
                            returnDesc = returnDesc.trim();
                        }
                        //result和desc有一个不为空，则返回错误码和原因
                        if ((returnDesc != null && !"".equals(returnDesc))
                                ||(returnResult != null && !"".equals(returnResult))){
                            JsonReturnUtil.fail(99, "提交审核平台失败！错误码：" + returnResult + "，原因：" + returnDesc, request, response);
                        }else{
                            JsonReturnUtil.fail(99, TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.SUBMIT_FAIL), request, response);
                        }
                    }else{
                        JsonReturnUtil.fail(99, TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.SUBMIT_FAIL), request, response);
                    }
                    return;
                }
                //暂存草稿
                if (0 == tmpData.getSubType().intValue()) {
                    JsonReturnUtil.fail(99, TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.TEMPLATE_SAVE_FAIL), request, response);
                    return;
                }
            }
        } catch (Exception e) {
            empTransDao.rollBackTransaction(conn);
            EmpExecutionContext.error(e, "模板表入库出现异常");
            JsonReturnUtil.fail(99, TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.SYSTEM_EXCEPTION), request, response);
            return;
        } finally {
            empTransDao.closeConnection(conn);
        }
    }


    /**
     * 入库报文JSON结构拆分表
     *
     * @param frontCont
     * @param endCont
     * @param lfSubTemplate
     */
    public void saveLftempContent(String frontCont, String endCont, LfSubTemplate lfSubTemplate, Long tmid, Connection conn, String appContent) {
        List<String> frontContList = StringUtils.strToList(frontCont);
        List<String> endContList = StringUtils.strToList(endCont);
        //前端JSON入库LF_TempContent 表
        try {
            if(frontContList != null){
                for (String content : frontContList) {
                    LfTempContent LftempContent = new LfTempContent();
                    LftempContent.setTmId(tmid);
                    LftempContent.setTmpType(lfSubTemplate.getTmpType());
                    LftempContent.setTmpContent(content);
                    //1-前端JSON
                    LftempContent.setContType(1);
                    empTransDao.save(conn, LftempContent);
                }
            }
            if(endContList != null){
                for (String content : endContList) {
                    LfTempContent LftempContent = new LfTempContent();
                    LftempContent.setTmId(tmid);
                    LftempContent.setTmpType(lfSubTemplate.getTmpType());
                    LftempContent.setTmpContent(content);
                    //2-终端JSON
                    LftempContent.setContType(2);
                    empTransDao.save(conn, LftempContent);
                }
            }
            if ("15".equals(lfSubTemplate.getTmpType().toString())) {
                List<String> appContList = StringUtils.strToList(appContent);
                if(appContList != null){
                    for (String content : appContList) {
                        LfTempContent LftempContent = new LfTempContent();
                        LftempContent.setTmId(tmid);
                        LftempContent.setTmpType(lfSubTemplate.getTmpType());
                        LftempContent.setTmpContent(content);
                        //3-appContent H5 封面JSON
                        LftempContent.setContType(3);
                        empTransDao.save(conn, LftempContent);
                    }
                }
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "入库LF_TEMPCONTENT 表出现异常.");
        }
    }

    /**
     * H5编辑前端报文解析
     *
     * @param tmpData
     * @param subData
     * @param tmid
     * @param sysuser
     * @param priority
     * @param paramList
     * @param subTempList
     * @param subGwlist
     * @param version
     * @param createOtt
     */
    private Map<String, Object> analysisH5(TempData tmpData, SubTempData subData, Long tmid, LfSysuser sysuser, int priority, List<LfTempParam> paramList, List<LfSubTemplate> subTempList, List<TempParams> subGwlist, String version, String createOtt, StringBuilder keyWordBuilder) {
        byte[] ottBytes = null;
        int degree = 0;
        int degreeSize = 0;
        int pnum = 0;
        int subType = null == tmpData.getSubType() ? 1 : tmpData.getSubType();
        String h5Root = SystemGlobals.getValue("montnet.rms.nginx.rootPath", "web/");
        Map<String, Object> h5Map = new HashMap<String, Object>();
        try {
            //TemplateUtil.uploadCropSource(tmpData);
            //H5编辑器的tempArr 只有一种编辑方式，短信补充方式由 标题+ 链接 自动生成
            String corpCode = sysuser.getCorpCode();
            JSONObject object = (JSONObject) tmpData.getTempArr().get(0).getContent();
            String srcPath = new TxtFileUtil().getWebRoot() + TEMPLATEPATH + sysuser.getCorpCode() + "/" + tmid + "/ott/";
            //todo H5模板路径过长后续修改
            String h5path = new TxtFileUtil().getWebRoot() + "templates/" + corpCode;
//                            //HTML存储本地
            new JsonParseResourceTool().parseH5Html(object, h5path + "/" + tmid + ".html", keyWordBuilder);
            // 上传资源服务器，返回url
            File h5File = new File(h5path + "/" + tmid + ".html");
            //1-上传模板
            String btype = "2";
            //上传的文件格式：1-html
            String ftype = "1";
            String h5URL = String2FileUtil.uploadFile(h5File, "templates/" + corpCode, btype, ftype);
            h5URL = uploadPath + rootPath + h5URL;

          //  tmpData.getTempArr().get(0).getContent();

            //前端H5报文 转为手机终端需要的对象
            MainTemplate dataParsToTemp = TemplateUtil.hFiveToTerminal(tmpData, h5URL, keyWordBuilder);
            dataParsToTemp.setSid(UUID.randomUUID().toString().replaceAll("-", ""));
            String endJson = JSONObject.toJSONString(dataParsToTemp);
            //写ott卡片模板结构文件
            String mcrslUrl = new TxtFileUtil().getWebRoot() + TEMPLATEPATH + corpCode + "/" + tmid + "/ott/src/ott.mrcsl";
            new FileOperatorTool().writeFile(mcrslUrl, endJson);

            // 存储源文件
            String frontJson = JSONObject.toJSONString(subData.getContent());
            //存储资源文件
            String appContent = JSONObject.toJSONString(tmpData.getApp());
            //H5 替换的是appContent 中封面的src
            Map<String, Object> resultMap = new JsonParseResourceTool().storeH5ResourceFile(corpCode, tmid, tmpData, appContent, subType);
            appContent = (String) resultMap.get("frontJson");
            String fileUrl = TEMPLATEPATH + corpCode + "/" + tmid + "/ott/fuxin.mrcsl";
            //组子表数据

            combineSubLftemplate(tmpData, subData, subTempList, priority, frontJson, fileUrl, keyWordBuilder,endJson);
            // 读取模板源文件字节流
            srcPath = new TxtFileUtil().getWebRoot() + TEMPLATEPATH + corpCode + "/" + tmid + "/ott/";
            Map<String, byte[]> map = new OttFileBytesTool().getOttFileList(srcPath + "src/");
            //提交审核 才进行组流文件操作
            if (tmpData.getSubType() == 1) {
                ottBytes = new OttFileBytesTool().getOttFileBytes("", "", map);
            } else {
                ottBytes = " ".getBytes();

            }
            //组提交网关的资源文件流
            degree = null == subData.getDegree() ? 0 : subData.getDegree();
            degreeSize = ottBytes.length;
            pnum = paramList.size();
            new GwOperatorTool().getSubmitTmpList(3, ottBytes, degree, degreeSize, pnum, subGwlist);
            //在本地生成流文件
            String fuxinMrcsl = new TxtFileUtil().getWebRoot() + TEMPLATEPATH + corpCode + "/" + tmid + "/ott/fuxin.mrcsl";
            new FileOperatorTool().writeFile(fuxinMrcsl, ottBytes);
            //根据 标题 、链接 生成短信---- 标题，详情请点击+链接
            String titleTxt = tmpData.getApp().getTitle().getText();
            String smContent = titleTxt + "，详情请点击 " + h5URL;
            h5Map.put("degree", degree);
            h5Map.put("degreeSize", degreeSize);
            h5Map.put("smContent", smContent);
            h5Map.put("h5URL", h5URL);
            h5Map.put("appContent", appContent);
            //将H5[企业秀]的标题，描述都放在关键字中进行校验
            String describleContent = tmpData.getApp().getDescription().getText();
            keyWordBuilder.append(titleTxt).append(describleContent);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "H5编辑前端报文解析-上传H5资源中心出现异常");
        }
        return h5Map;
    }

    /**
     * 短信补充方式报文分析
     *
     * @param tmpData
     * @param subTempList
     * @param subData
     * @param tmid
     * @param priority
     * @param paramList
     * @param version
     * @return
     */
    private Map<String, Object> analysisSms(TempData tmpData, List<LfSubTemplate> subTempList, SubTempData subData, Long tmid, int priority, List<LfTempParam> paramList, String version, StringBuilder keyWordBuilder) {
        String smContent = "";
        String smParamReg = "";
        Map<String, Object> smsMap = new HashMap<String, Object>();
        if (version.equals("V3.0")) {
            String frontJson = JSONObject.toJSONString(subData.getContent());
            combineSubLftemplate(tmpData, subData, subTempList, priority, frontJson, " ", keyWordBuilder," ");
            Map<String, String> textMap = JSONObject.parseObject(subData.getContent().toString(), Map.class);
            smContent = RegParamTool.replaceParam(textMap.get("template"));
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(smContent);
            smContent = m.replaceAll("");
            smContent = smContent.replace("\"", "\\\"");
            StringBuilder paramBuilder = new StringBuilder("{");
            for (int i = 0; i < paramList.size(); i++) {
                LfTempParam param = paramList.get(i);
                String enCodeRegParam = new String(Base64.encodeBase64(param.getRegContent().getBytes()));
                paramBuilder.append("\\\"").append("P" + (i + 1) + "\\\"").append(":\\\"").append(enCodeRegParam).append("\\\",");
            }
            if (paramBuilder.toString().endsWith(",")) {
                smParamReg = paramBuilder.toString().substring(0, paramBuilder.length() - 1) + "}";
            }
        }
        keyWordBuilder.append(smContent);
        //替换文本内容中的特殊字符
        smContent = new JsonParseResourceTool().replaceHtmlSpecialSign(smContent);
        smsMap.put("smContent", smContent);
        smsMap.put("smParamReg", smParamReg);
        return smsMap;
    }

    /**
     * 富文本补充方式前端报文解析
     *
     * @param tmpData
     * @param subData
     * @param tmid
     * @param sysuser
     * @param priority
     * @param langName
     * @param paramList
     * @param subTempList
     * @param subGwlist
     * @param version
     * @param createOtt
     */
    private Map<String, Object> analysisRichText(TempData tmpData, SubTempData subData, Long tmid, LfSysuser sysuser, int priority, String langName, List<LfTempParam> paramList, List<LfSubTemplate> subTempList, List<TempParams> subGwlist, String version, String createOtt, StringBuilder keyWordBuilder) {
        byte[] ottBytes = null;
        int degree = 0;
        int degreeSize = 0;
        int pnum = 0;
        Map<String, Object> richTextMap = new HashMap<String, Object>();
        //转换终端JSON
        String frontJson = JSONObject.toJSONString(subData.getContent());
        String fileUrl = TEMPLATEPATH + sysuser.getCorpCode() + "/" + tmid + "/ott/fuxin.mrcsl";
        combineSubLftemplate(tmpData, subData, subTempList, priority, frontJson, fileUrl, keyWordBuilder," ");
        MainTemplate dataParsToTemp = TemplateUtil.dataParsToTemp(tmpData, keyWordBuilder);
        dataParsToTemp.setSid(UUID.randomUUID().toString().replaceAll("-", ""));
        String endJson = JSONObject.toJSONString(dataParsToTemp);
        endJson = new JsonParseResourceTool().replaceDiv(endJson);
        //将前端JSON放进关键字校验builder中
        keyWordBuilder.append(frontJson);
        //写ott卡片模板结构文件
        String mcrslUrl = new TxtFileUtil().getWebRoot() + TEMPLATEPATH + sysuser.getCorpCode() + "/" + tmid + "/ott/src/ott.mrcsl";
        new FileOperatorTool().writeFile(mcrslUrl, endJson);
        //读取模板源文件字节流
        String srcPath = new TxtFileUtil().getWebRoot() + TEMPLATEPATH + sysuser.getCorpCode() + "/" + tmid + "/ott/src/";
        Map<String, byte[]> map = new OttFileBytesTool().getOttFileList(srcPath);
        if (tmpData.getSubType() == 1) {//提交审核 才进行组流文件操作
            ottBytes = new OttFileBytesTool().getOttFileBytes("", "", map);
        } else {
            ottBytes = " ".getBytes();

        }
        //组装网关
        degree = null == subData.getDegree() ? 0 : subData.getDegree();
        degreeSize = ottBytes.length;
        pnum = paramList.size();
        new GwOperatorTool().getSubmitTmpList(3, ottBytes, degree, degreeSize, pnum, subGwlist);
        String fuxinMrcsl = new TxtFileUtil().getWebRoot() + TEMPLATEPATH + sysuser.getCorpCode() + "/" + tmid + "/ott/fuxin.mrcsl";
        new FileOperatorTool().writeFile(fuxinMrcsl, ottBytes);
        richTextMap.put("degree", degree);
        richTextMap.put("degreeSize", degreeSize);
        return richTextMap;
    }

    /**
     * 卡片前端报文解析
     *
     * @param tmpData
     * @param subData
     * @param tmid
     * @param sysuser
     * @param priority
     * @param langName
     * @param paramList
     * @param subTempList
     * @param subGwlist
     * @param version
     * @param createOtt
     */
    private Map<String, Object> analysisCard(TempData tmpData, SubTempData subData, Long tmid, LfSysuser sysuser, int priority, String langName, List<LfTempParam> paramList, List<LfSubTemplate> subTempList, List<TempParams> subGwlist, String version, String createOtt, StringBuilder keyWordBuilder) {
        //前端JSON
        byte[] rmsBytes = null;
        byte[] ottBytes = null;
        int degree = 0;
        int degreeSize = 0;
        int pnum = 0;
        boolean subGwFlag = false;
        Map<String, Object> cardMap = new HashMap<String, Object>();
        int subType = null == tmpData.getSubType() ? 1 : tmpData.getSubType();
        MainTemplate dataParsToTemp = TemplateUtil.dataParsToTemp(tmpData, keyWordBuilder);
        dataParsToTemp.setSid(UUID.randomUUID().toString().replaceAll("-", ""));
        String endJson = JSONObject.toJSONString(dataParsToTemp);
        endJson = new JsonParseResourceTool().replaceDiv(endJson);
        //写ott卡片模板结构文件
        String mcrslUrl = new TxtFileUtil().getWebRoot() + TEMPLATEPATH + sysuser.getCorpCode() + "/" + tmid + "/ott/src/ott.mrcsl";
        new FileOperatorTool().writeFile(mcrslUrl, endJson);

        // 存储源文件
        TempContent tempContent = JSONObject.parseObject(subData.getContent().toString(), TempContent.class);
        String frontJson = JSONObject.toJSONString(subData.getContent());
        Map<String, Object> resultMap = new JsonParseResourceTool().storeOttResourceFile(sysuser.getCorpCode(), tmid, tempContent, frontJson, subType);
        String json = (String) resultMap.get("frontJson");
        String fileUrl = TEMPLATEPATH + sysuser.getCorpCode() + "/" + tmid + "/ott/fuxin.mrcsl";
        combineSubLftemplate(tmpData, subData, subTempList, priority, json, fileUrl, keyWordBuilder,endJson);
        // 读取模板源文件字节流
        String srcPath = new TxtFileUtil().getWebRoot() + TEMPLATEPATH + sysuser.getCorpCode() + "/" + tmid + "/ott/";
        Map<String, byte[]> map = new OttFileBytesTool().getOttFileList(srcPath + "src/");
        if (tmpData.getSubType() == 1) {//提交审核 才进行组流文件操作
            ottBytes = new OttFileBytesTool().getOttFileBytes("", "", map);
        } else {
            ottBytes = " ".getBytes();

        }
        //组装网关
        degree = null == subData.getDegree() ? 0 : subData.getDegree();
        degreeSize = ottBytes.length;
        pnum = paramList.size();
        subGwFlag = new GwOperatorTool().getSubmitTmpList(3, ottBytes, degree, degreeSize, pnum, subGwlist);
        String fuxinMrcsl = new TxtFileUtil().getWebRoot() + TEMPLATEPATH + sysuser.getCorpCode() + "/" + tmid + "/ott/fuxin.mrcsl";
        new FileOperatorTool().writeFile(fuxinMrcsl, ottBytes);
        //上传H5资源服务器
        if (tmpData.getTmpType() == 12) {
//                                h5Html = TemplateUtil.parseToHtml(tempContent);
//                                new FileOperatorTool().writeFile(srcPath +"/"+tmid+".html",h5Html);
//                                File h5File = new File(srcPath +"/"+tmid+".html");
//                                String btype = "2";//1-上传模板
//                                String ftype ="1";//上传的文件格式：1-html
//                                String h5URL  = String2FileUtil.uploadFile(h5File, TEMPLATEPATH + corpCode + "/" + tmid + "/ott", btype, ftype);
//                                //组H5相关字节
//System.out.println("12-h5URL:"+h5URL);
//                                degreeSize = h5URL.getBytes().length;
//                                new GwOperatorTool().getSubmitTmpList(2, h5URL.getBytes(), 0, degreeSize, 0, subGwlist);
        }
        cardMap.put("degree", degree);
        cardMap.put("degreeSize", degreeSize);
        return cardMap;
    }

    /**
     * 富媒体补充方式前端报文解析
     *
     * @param tmpData
     * @param subData
     * @param tmid
     * @param sysuser
     * @param priority
     * @param langName
     * @param paramList
     * @param subTempList
     * @param subGwlist
     * @param version
     * @param createOtt
     */
    private Map<String, Object> analysisRichMedia(TempData tmpData, SubTempData subData, long tmid, LfSysuser sysuser, int priority, String langName, List<LfTempParam> paramList, List<LfSubTemplate> subTempList, List<TempParams> subGwlist, String version, String createOtt, StringBuilder keyWordBuilder) {
        //前端JSON
        String frontJson = "";
        byte[] rmsBytes = null;
        byte[] ottBytes = null;
        int degree = 0;
        int degreeSize = 0;
        int pnum = 0;
        boolean subGwFlag = false;
        String mcrslUrl = "";
        //操作类型-0：保存，1-提交审核
        int subType = tmpData.getSubType();
        Map<String, Object> richMap = new HashMap<String, Object>();
        //存储资源文件
        JSONArray jsonArray = (JSONArray) subData.getContent();
        String projectPath = String2FileUtil.getWebRoot();
        String targetPath = projectPath + TEMPLATEPATH + sysuser.getCorpCode() + "/" + tmid + "/rms";
        frontJson = JSONObject.toJSONString(subData.getContent());
        Map<String, Object> resultMap = new JsonParseResourceTool().storeRmsResourceFile(targetPath, projectPath, jsonArray, frontJson, langName, subType, keyWordBuilder);
        String json = (String) resultMap.get("fontJson");
        String fileUrl = TEMPLATEPATH + sysuser.getCorpCode() + "/" + tmid + "/rms/fuxin.rms";
        List<Map<String, String>> list = (List<Map<String, String>>) resultMap.get("list");
        String title = (null == tmpData.getTmName()) ? "" : tmpData.getTmName();
        //subType =0,暂存草稿，1-提交审核 才进行组流文件操作
        if (tmpData.getSubType() == 1) {
            rmsBytes = new RmsFileBytesTool().getRmsFileBytes(targetPath + "/src", title, list);
        } else {
            rmsBytes = " ".getBytes();
        }
        //组装网关
        degree = null == subData.getDegree() ? 0 : subData.getDegree();
        degreeSize = rmsBytes.length;
        pnum = paramList.size();
        subGwFlag = new GwOperatorTool().getSubmitTmpList(1, rmsBytes, degree, degreeSize, pnum, subGwlist);
        String fuxinRms = new TxtFileUtil().getWebRoot() + TEMPLATEPATH + sysuser.getCorpCode() + "/" + tmid + "/rms/fuxin.rms";
        new FileOperatorTool().writeFile(fuxinRms, rmsBytes);
        //上传H5资源服务器
        mcrslUrl = TEMPLATEPATH + sysuser.getCorpCode() + "/" + tmid + "/rms/src/fuxin.rms";
        //富媒体编辑器的时候需要同时生成卡片的模板资源(V3.0才是这个逻辑)
        if (tmpData.getTmpType() == 11 && version.equals("V3.0") && "true".equals(createOtt)) {
//                                h5Html  = TemplateUtil.richMediaParseToHtml(tmpData);
//                                new FileOperatorTool().writeFile(targetPath +"/"+tmid+".html",h5Html);
//                                File h5File = new File(targetPath +"/"+tmid+".html");
//                                String btype = "2";//1-上传模板
//                                String ftype ="1";//上传的文件格式：1-html
//                                String h5URL  = String2FileUtil.uploadFile(h5File, TEMPLATEPATH + corpCode + "/" + tmid + "/rms", btype, ftype);
//                                //组H5相关字节
// System.out.println("11-h5URL:"+h5URL);
//                                degreeSize = h5URL.getBytes().length;
//                                new GwOperatorTool().getSubmitTmpList(2, h5URL.getBytes(), 0, degreeSize, 0, subGwlist);

            // 生成卡片需要的资源
            String rmsSrc = targetPath + "/src";
            String ottSrc = targetPath.replace("rms", "ott") + "/src";
            //将富媒体下的资源复制到OTT的src 目录下，.txt,.smil 文件跳过
            new FileOperatorTool().copySrcFile(rmsSrc, ottSrc);
            String tempCont = JSONObject.toJSONString(subData.getContent());
            String editorWidth = tmpData.getEditorWidth() == null ? "260" : tmpData.getEditorWidth().toString();
            MainTemplate dataParsToTemp = TemplateUtil.richMediaToTerminal(tempCont, ottSrc, editorWidth);
            dataParsToTemp.setSid(UUID.randomUUID().toString().replaceAll("-", ""));
            String endJson = JSONObject.toJSONString(dataParsToTemp);
            //写ott卡片模板结构文件
            mcrslUrl = new TxtFileUtil().getWebRoot() + TEMPLATEPATH + sysuser.getCorpCode() + "/" + tmid + "/ott/src/ott.mrcsl";
            new FileOperatorTool().writeFile(mcrslUrl, endJson);
            //读取资源文件
            Map<String, byte[]> ottmap = new OttFileBytesTool().getOttFileList(ottSrc);
            //提交审核 才进行组流文件操作
            if (tmpData.getSubType() == 1) {
                ottBytes = new OttFileBytesTool().getOttFileBytes("", "", ottmap);
            } else {
                ottBytes = "".getBytes();

            }
            //组装网关
            degree = null == subData.getDegree() ? 0 : subData.getDegree();
            degreeSize = ottBytes.length;
            pnum = paramList.size();
            new GwOperatorTool().getSubmitTmpList(3, ottBytes, degree, degreeSize, pnum, subGwlist);
            String fuxinMrcsl = new TxtFileUtil().getWebRoot() + TEMPLATEPATH + sysuser.getCorpCode() + "/" + tmid + "/ott/fuxin.mrcsl";
            new FileOperatorTool().writeFile(fuxinMrcsl, ottBytes);
        }
        Float tempSize= (float)rmsBytes.length/1024;
        tempSize = tempSize*100;
        degreeSize = tempSize.intValue();
        subData.setDegreeSize(degreeSize);
        combineSubLftemplate(tmpData, subData, subTempList, priority, json, fileUrl, keyWordBuilder, " ");
        richMap.put("degree", degree);
        richMap.put("degreeSize", degreeSize);
        return richMap;
    }


    /**
     * 组合参数表字段参数
     *
     * @param tmpData
     * @param paramList
     */
    private void combineParam(long tmid, TempData tmpData, List<LfTempParam> paramList) {
        if (null != tmpData && null != tmpData.getParamArr()) {
            List<TempDataParam> paramArr = tmpData.getParamArr();
            for (TempDataParam param : paramArr) {
                LfTempParam lfTempParam = new LfTempParam();
                //参数类型
                int type = 1;//默认1：纯数字类型
                if (null != param.getType()) {
                    type = param.getType();
                }
                //参数名
                String name = "";
                if (null != param.getName()) {
                    name = param.getName();
                }
                //是否定长 0可变 1固定
                int lengthRestrict = 0;
                if (null != param.getLengthRestrict()) {
                    lengthRestrict = param.getLengthRestrict();
                }
                //固定长度
                int fixLength = 0;
                if (null != param.getFixLength()) {
                    fixLength = param.getFixLength();
                }
                //最小长度
                int minLength = 0;
                if (null != param.getMinLength()) {
                    minLength = param.getMinLength();
                }
                //最大长度
                int maxLength = 0;
                if (null != param.getMaxLength()) {
                    maxLength = param.getMaxLength();
                }
                // 是否有参数长度限制 0-false ,1-true
                int hasLength = 0;
                if (null != param.getHasLength()) {
                    hasLength = param.getHasLength();
                }
                lfTempParam.setTmId(tmid);
                lfTempParam.setType(type);
                lfTempParam.setName(name);
                lfTempParam.setFixLength(fixLength);
                lfTempParam.setLengthRestrict(lengthRestrict);
                lfTempParam.setMinLength(minLength);
                lfTempParam.setMaxLength(maxLength);
                lfTempParam.setRegContent(RegParamTool.parseParam2Reg(lfTempParam.getType(), lfTempParam.getLengthRestrict(), lfTempParam.getMinLength(), lfTempParam.getMaxLength(), lfTempParam.getFixLength()));
                //是否有参数限制
                lfTempParam.setHasLength(hasLength);
                paramList.add(lfTempParam);
            }
        }
    }

    /**
     * 组装模板表字段参数
     *
     * @param sysuser
     * @param tmpData
     * @return
     */
    private LfTemplate combineLFtemplate(LfSysuser sysuser, TempData tmpData) {
        LfTemplate template = new LfTemplate();
        // 模板实体类赋值
        template.setTmCode(" ");
        // 是否审核(无需审核-0，未审核-1，同意1，拒绝2)
        template.setIsPass(-1);
        // 模板状态（0无效，1有效，2草稿）
        if (1 == tmpData.getSubType()) {
            template.setTmState(1L);
        } else {
            //暂存草稿
            template.setTmState(2L);
        }
        // 添加时间
        template.setAddtime(Timestamp.valueOf(new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss").format(new Date())));
        // 模板内容-添加的时候还未生成，直接填空字符串
        template.setTmMsg(" ");
        // 操作员ID
        template.setUserId(sysuser.getUserId());
        template.setCorpCode(sysuser.getCorpCode());
        // 模板（3-短信模板;4-彩信模板；11-富信模板）-->//富文本、富媒体、卡片、H5的类型主表都改为11,真正的类型放在LF_SUB_TEMPLATE 表中进行区别
        template.setTmpType(11);
        // 参数实际个数
        int paramCnt = 0;
        // 静动态模板 0-静态，1-动态
        long dsFlag = 0L;
        //动态
        if (null != tmpData.getParamArr() && tmpData.getParamArr().size() > 0) {
            paramCnt = tmpData.getParamArr().size();
            dsFlag = 1L;
        } else {// 静态模板
            paramCnt = 0;
            dsFlag = 0L;
        }
        template.setParamcnt(paramCnt);
        // 静态模板0、动态模板1
        template.setDsflag(dsFlag);
        //是否公共场景 0-我的场景，1-公共场景
        if (null != tmpData.getIsPublic() && 1 == tmpData.getIsPublic()) {
            template.setIsPublic(1);
        } else {
            template.setIsPublic(0);
        }
        //模板使用次数
        template.setUsecount(0L);
        //V2.0 时给模板发送时使用的校验参数 JSON串
        template.setExlJson(" ");
        //网关审核状态 -1：未审批，0：无需审批，1：同意，2：拒绝，3：审核中
        template.setAuditstatus(-1);
        //网关彩信模板状态（0：正常，可用	1：锁定，暂时不可用	2：永久锁定，永远不可用）
        template.setTmplstatus(0);

        //用途ID,默认-2
        int useID = -2;
        if (null != tmpData.getUseId()) {
            useID = tmpData.getUseId();
        }
        template.setUseid(useID);

        //行业ID,默认-1
        int industryID = -1;
        if (null != tmpData.getIndustryId()) {
            industryID = tmpData.getIndustryId();
        }
        template.setIndustryid(industryID);

        //版本号
        template.setVer("V3.0");
        //提交状态 	0：未提交	1：提交成功	2：提交失败	3；提交中 --彩信时就有的字段，我也不知道干啥用的
        template.setSubmitstatus(0);
        //是否快捷场景 0-否
        template.setIsShortTemp(0);
        //模板主题名
        String tmName = StringUtils.isBlank(tmpData.getTmName()) ? " " : tmpData.getTmName();
        template.setTmName(tmName);
        //1-托管版EMP,2-标准版EMP,3-RCOS ,4-富信模板接口,5-其它
        int source = 0;
        //托管版
        if (StaticValue.getCORPTYPE() == 1) {
            source = 1;
        } else {
            source = 2;
        }
        template.setSource(source);
        return template;

    }

    /**
     * 组装子模板表参数值
     *
     * @param tmpData
     * @param subData
     * @param subTempList
     */
    private void combineSubLftemplate(TempData tmpData, SubTempData subData,
                                      List<LfSubTemplate> subTempList, int priority, String frontJson, String filrUrl, StringBuilder keyWordBuilder,String endJson) {
        // 子表实体类赋值
        LfSubTemplate subTemplate = new LfSubTemplate();
        subTemplate.setTmpType(subData.getTmpType());
        subTemplate.setAddTime(Timestamp.valueOf(new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss").format(new Date())));
        subTemplate.setContent(frontJson);
        subTemplate.setEndJson(endJson);
        subTemplate.setFileUrl(filrUrl);
        subTemplate.setFrontJson(frontJson);
        subTemplate.setIndustryId(null == tmpData.getIndustryId() ? -1 : tmpData.getIndustryId());
        subTemplate.setUseId(null == tmpData.getUseId() ? -2 : tmpData.getUseId());
        if (tmpData.getTmpType() == subData.getTmpType()) {
            subTemplate.setPriority(LfTemplateConfig.SUB_TEMP_PRIORITY);
        } else {
            subTemplate.setPriority(priority);
        }
        subTemplate.setStatus(1);
        String cardHtml = StringUtils.isBlank(subData.getCardHtml()) ? " " : subData.getCardHtml();
        subTemplate.setCardHtml(cardHtml);
        subTemplate.setDegree(null == subData.getDegree() ? 0 : subData.getDegree());
        subTemplate.setDegreeSize(null == subData.getDegreeSize() ? 1 : subData.getDegreeSize() + 1);
        subTemplate.setH5Type(null == subData.getH5Type() ? 0 : subData.getH5Type());
        subTempList.add(subTemplate);

    }

    /**
     * 组装
     *
     * @param subData
     * @return
     */
    private String combineTempFilePath(SubTempData subData) {
        String fileName = TEMPLATEPATH;
        Integer type = subData.getTmpType();
        switch (type) {
            case 11:
                fileName = fileName + "/fuxin.rms";
                break;
            case 12:
                // 卡片
                fileName = fileName + "/fuxin.mrcsl";
                break;
            case 13:
                // 富文本
                fileName = fileName + "/fuxin.h5";
                break;
            case 14:
                //短信
                fileName = fileName + "/fuxin.sms";
                break;
            default:
                break;
        }
        return fileName;
    }

    @Override
    public void setShotCutTem(TempsVo tempsVo, HttpServletRequest request,
                              HttpServletResponse response) {
        if (null == tempsVo) {
            JsonReturnUtil.fail("参数接收异常", request, response);
            return;
        }
        if (null == tempsVo.getTmId()) {
            JsonReturnUtil.fail("请传入模板id", request, response);
            return;
        }
        if (null == tempsVo.getIsShortTemp()) {
            JsonReturnUtil.fail("请传入设置参数", request, response);
            return;
        }
        if (LfTemplateConfig.IS_SHORT_TEMP != tempsVo.getIsShortTemp()
                && LfTemplateConfig.IS_NOT_SHORT_TEMP != tempsVo
                .getIsShortTemp()) {
            JsonReturnUtil.fail(TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.TEMPLATE_NOT_APPROVE), request, response);
            return;
        }
        // 获取模板信息.
        LfTemplate lfTemplate = null;
        try {
            lfTemplate = empDao.findObjectByID(LfTemplate.class,
                    tempsVo.getTmId());
        } catch (Exception e) {
            EmpExecutionContext.error(e, "快捷场景设置中获取模板信息异常");
        }
        if (null == lfTemplate) {
            JsonReturnUtil.fail("未找到相关模板，请检查模板id", request, response);
            return;
        }
        // 权限验证
        // LfSysuser sysuser =
        // (LfSysuser)request.getSession(false).getAttribute("loginSysuser");
        // String corpCode = sysuser.getCorpCode();
        LfSysuser lfSysuser = UserUtil.getUser(request);
        String corpCode = lfSysuser.getCorpCode();
        /*if (!corpCode.equals(lfTemplate.getCorpCode())) {
            JsonReturnUtil.fail("权限异常，该模板不属于当前企业", request, response);
            return;
        }
        if (lfTemplate.getAuditstatus() != LfTemplateConfig.AUDITSTATUS_APPROVE) {
            JsonReturnUtil.fail("权限异常，该模板不属于当前企业", request, response);
            return;
        }*/
        lfTemplate.setIsShortTemp(tempsVo.getIsShortTemp());
        Connection connection = empTransDao.getConnection();
        try {
            empTransDao.beginTransaction(connection);
            empTransDao.update(connection, lfTemplate);

            LfShortTemp lfShortTemp = new LfShortTemp();
            lfShortTemp.setTempId(lfTemplate.getTmid());
            if (null != lfTemplate.getTmName()) {
                lfShortTemp.setTempName(lfTemplate.getTmName());
            } else {
                lfShortTemp.setTempName("");
            }
            lfShortTemp.setAddTime(new Timestamp(System.currentTimeMillis()));
            /*if (null != lfTemplate.getTmName()) {
                lfShortTemp.setCorpCode(lfTemplate.getCorpCode());
            } else {
                lfShortTemp.setCorpCode(corpCode);
            }*/
            lfShortTemp.setCorpCode(corpCode);
            lfShortTemp.setUserId(lfSysuser.getUserId());

            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("tempId", String.valueOf(lfTemplate.getTmid()));
            conditionMap.put("userId", String.valueOf(lfSysuser.getUserId()));
            List<LfShortTemp> lfShortTempDbs = empDao.findListByCondition(LfShortTemp.class, conditionMap, null);

            //调用7.2前的设为快捷场景逻辑
            TemplateBiz templateBiz = new TemplateBizImp();

            //如果是设置快捷场景
            if (LfTemplateConfig.IS_SHORT_TEMP == tempsVo.getIsShortTemp()) {
                //如果快捷场景表已有改模板数据，则不新增
                if (lfShortTempDbs.size() <= 0) {
                    if (lfShortTempDbs.size() > 14) {
                        EmpExecutionContext.info("快捷模板新增超过十五个！");
                        JsonReturnUtil.fail("快捷模板新增超过十五个！", request, response);
                        return;
                    }
                    empTransDao.save(connection, lfShortTemp);
                    templateBiz.addShotCutTem(String.valueOf(lfTemplate.getTmid()), lfTemplate.getTmName(), request, response);
                }
                //如果是取消快捷场景
            } else if (LfTemplateConfig.IS_NOT_SHORT_TEMP == tempsVo.getIsShortTemp()) {
                if (lfShortTempDbs.size() > 0) {
                    empTransDao.delete(connection, LfShortTemp.class, conditionMap);
                    templateBiz.delShotCutTem(String.valueOf(lfTemplate.getTmid()), request, response);
                }
            }

            empTransDao.commitTransaction(connection);
            JsonReturnUtil.success(request, response);
            return;
        } catch (Exception e) {
            empTransDao.rollBackTransaction(connection);
            EmpExecutionContext.error(e, "快捷场景设置中更新模板信息异常");
            JsonReturnUtil.fail("系统异常", request, response);
            return;
        } finally {
            empTransDao.closeConnection(connection);
        }

    }


    @Override
    public void getUse(String tmpType, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // TODO Auto-generated method stub
        try {
            // dao层
            //MeditorDao meditorDao = new MeditorDaoImp();
            // TODO Auto-generated method stub
            List<LfIndustryUse> industryUseList = new ArrayList<LfIndustryUse>();
            LfIndustryUse lfIndustryUse = new LfIndustryUse();

            if (!StringUtils.IsNullOrEmpty(tmpType)) {
                lfIndustryUse.setTmpType(Integer.parseInt(tmpType));
            }
            industryUseList = meditorDao.getIndustryUseList(lfIndustryUse);
            if (industryUseList != null) {
                JsonReturnUtil.success(industryUseList, request, response);
            } else {
                JsonReturnUtil.fail(request, response);
            }

        } catch (Exception e) {
            // TODO: handle exception
            EmpExecutionContext.error(e, "获取行业用途异常!");
        }
    }

    @Override
    public void addH5(TempHFiveVo tempHFiveVo, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String rootPath = request.getSession().getServletContext().getRealPath("");
        LfHfive lfHfive = new LfHfive();
        //标题不能为空,为空则返回
        if (StringUtils.IsNullOrEmpty(tempHFiveVo.getTitle())) {
            JsonReturnUtil.fail("标题不能为空", request, response);
            return;
        } else {
            lfHfive.setTitle(tempHFiveVo.getTitle());
            //根据标题查询
            LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
            map.put("title", tempHFiveVo.getTitle());
            List<LfHfive> list = getByCondition(LfHfive.class, map, null);
            if (list.size() > 0) {
                JsonReturnUtil.fail("标题不能相同", request, response);
                return;
            }
        }

        if (StringUtils.IsNullOrEmpty(tempHFiveVo.getTitle())) {
            JsonReturnUtil.fail("创建者不能为空", request, response);
            return;
        } else {
            lfHfive.setAuthor(tempHFiveVo.getAuthor());
        }

        //内容不能为空
        if (StringUtils.IsNullOrEmpty(tempHFiveVo.getContent())) {
            JsonReturnUtil.fail("内容不能为空", request, response);
            return;
        } else {
            //将h5内容上传到文件服务器
            //String url = "html\\"+String.valueOf(new Timestamp(System.currentTimeMillis()))+".txt";
            String fileName = String2FileUtil.getTimeString(String.valueOf(new Timestamp(System.currentTimeMillis()))) + ".html";
            String url = rootPath + "\\html\\" + fileName;
            File file = String2FileUtil.string2File(tempHFiveVo.getContent(), url);
            rootPath = "emp_7.2/html";
            String h5url = String2FileUtil.uploadFile(file, rootPath, "2", "1");
            lfHfive.setUrl(h5url);
            if (!StringUtils.IsNullOrEmpty("h5url")) {
                //删除本地文件
                String2FileUtil.delLocalFile(url);
            }
        }

        lfHfive.setCreateTime(new Timestamp(System.currentTimeMillis()));
        lfHfive.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        //新增时候设置初始时间
        lfHfive.setCommitTime(new Timestamp(System.currentTimeMillis()));
        lfHfive.setUseTime(new Timestamp(System.currentTimeMillis()));

        //创建时为草稿
        lfHfive.setStaus(2);

        Long saveReturnId = 0L;
        saveReturnId = addObjReturnId(lfHfive);
        if (saveReturnId > 0) {
            lfHfive = getById(LfHfive.class, saveReturnId);
            //给响应设置值
            TempHFiveDetailVo tempHFiveDetailVo = new TempHFiveDetailVo();

            tempHFiveDetailVo.sethId(lfHfive.getHId());
            tempHFiveDetailVo.setTitle(lfHfive.getTitle());
            tempHFiveDetailVo.setAuthor(lfHfive.getAuthor());
            tempHFiveDetailVo.setCommitTime(lfHfive.getCommitTime());
            tempHFiveDetailVo.setCreateTime(lfHfive.getCreateTime());
            tempHFiveDetailVo.setStaus(lfHfive.getStaus());
            tempHFiveDetailVo.setUseTime(lfHfive.getUseTime());
            tempHFiveDetailVo.setUpdateTime(lfHfive.getUpdateTime());
            tempHFiveDetailVo.setUrl(lfHfive.getUrl());
            tempHFiveDetailVo.setContent(String2FileUtil.downloadFile(lfHfive.getUrl()));
            tempHFiveDetailVo.setBodyContent(String2FileUtil.getBodyContent(tempHFiveDetailVo.getContent()));
            JsonReturnUtil.success(tempHFiveDetailVo, request, response);
        } else {
            JsonReturnUtil.fail(request, response);
        }


    }

    @Override
    public void getH5s(TempHFiveVo tempHFiveVo, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        PageInfo pageinfo = new PageInfo();
        //MeditorDao meditorDao = new MeditorDaoImp();
        if (tempHFiveVo != null) {
            if (StringUtils.IsNullOrEmpty(tempHFiveVo.getPageSize()) || StringUtils.IsNullOrEmpty(tempHFiveVo.getCurrentPage())) {
                JsonReturnUtil.fail("页码不许为空", request, response);
                return;
            }
            List<LfHfive> lfHfiveList = meditorDao.getH5s(tempHFiveVo, pageinfo);
            JsonReturnUtil.success(lfHfiveList, request, response, pageinfo.getTotalPage(), pageinfo.getTotalRec());
        } else {
            JsonReturnUtil.fail("查询失败", request, response);
            return;
        }
    }

    @Override
    public void deleteH5(String hId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (StringUtils.IsNullOrEmpty(hId)) {
            JsonReturnUtil.fail("H5模板的ID为空", request, response);
            return;
        }
        //根据ID查询表
        LfHfive lfHfive = getById(LfHfive.class, hId);
        //获得表的url
        String url = lfHfive.getUrl();
        //根据URL获取资源服务器上的H5文件
        String content = String2FileUtil.downloadFile(url);
        String fpath = UploadFileConfig.UPLOAD_FILE_SAVEPATH;
        //删除H5的HTML
        String2FileUtil.delFile(url, fpath, "2", "1");
        //遍历HTML的img
        List<String> list = String2FileUtil.getImageSrc(content);
        //批量删除H5资源服务器的图片
        if (list != null && list.size() > 0) {
            String2FileUtil.delFile(list.get(0), fpath, "2", "2");
        }

        if (deleteByIds(LfHfive.class, hId) > 0) {
            JsonReturnUtil.success(request, response);
        } else {
            JsonReturnUtil.fail("删除失败", request, response);
        }
    }

    @Override
    public void updateH5(TempHFiveVo tempHFiveVo, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        String rootPath = request.getSession().getServletContext().getRealPath("");

        if (StringUtils.IsNullOrEmpty(tempHFiveVo.gethId())) {
            JsonReturnUtil.fail("H5模板ID不能为空", request, response);
            return;
        }

        if (StringUtils.IsNullOrEmpty(tempHFiveVo.getContent())) {
            JsonReturnUtil.fail("H5模板内容不能为空", request, response);
            return;
        }


        if (StringUtils.IsNullOrEmpty(tempHFiveVo.getTitle())) {
            JsonReturnUtil.fail("标题不能为空", request, response);
            return;
        } else {
            objectMap.put("title", tempHFiveVo.getTitle());
        }
        if (StringUtils.IsNullOrEmpty(tempHFiveVo.getAuthor())) {
            JsonReturnUtil.fail("作者不能为空", request, response);
            return;
        } else {
            objectMap.put("author", tempHFiveVo.getAuthor());
        }
        objectMap.put("staus", "2");

        objectMap.put("updateTime", String.valueOf(new Timestamp(System.currentTimeMillis())));
        //设置url
        String fileName = String2FileUtil.getTimeString(String.valueOf(new Timestamp(System.currentTimeMillis()))) + ".html";
        String url = rootPath + "\\html\\" + fileName;
        File file = String2FileUtil.string2File(tempHFiveVo.getContent(), url);
        rootPath = "emp_7.2/html";
        String h5url = String2FileUtil.uploadFile(file, rootPath, "2", "1");
        if (!StringUtils.IsNullOrEmpty("h5url")) {
            //删除本地文件
            String2FileUtil.delLocalFile(url);
        }
        objectMap.put("url", h5url);

        conditionMap.put("hId", tempHFiveVo.gethId());

        if (update(LfHfive.class, objectMap, conditionMap)) {
            LfHfive lfHfive = getById(LfHfive.class, tempHFiveVo.gethId());

            //给响应设置值
            TempHFiveDetailVo tempHFiveDetailVo = new TempHFiveDetailVo();

            tempHFiveDetailVo.sethId(lfHfive.getHId());
            tempHFiveDetailVo.setTitle(lfHfive.getTitle());
            tempHFiveDetailVo.setAuthor(lfHfive.getAuthor());
            tempHFiveDetailVo.setCommitTime(lfHfive.getCommitTime());
            tempHFiveDetailVo.setCreateTime(lfHfive.getCreateTime());
            tempHFiveDetailVo.setStaus(lfHfive.getStaus());
            tempHFiveDetailVo.setUseTime(lfHfive.getUseTime());
            tempHFiveDetailVo.setUpdateTime(lfHfive.getUpdateTime());
            tempHFiveDetailVo.setUrl(lfHfive.getUrl());
            tempHFiveDetailVo.setContent(String2FileUtil.downloadFile(lfHfive.getUrl()));
            tempHFiveDetailVo.setBodyContent(String2FileUtil.getBodyContent(tempHFiveDetailVo.getContent()));

            JsonReturnUtil.success(tempHFiveDetailVo, request, response);
            return;
        } else {
            JsonReturnUtil.fail("修改失败", request, response);
            return;
        }
    }

    @Override
    public void getH5Detail(String hId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        map.put("hId", hId);
        List<LfHfive> list = getByCondition(LfHfive.class, map, null);
        //给响应设置值
        TempHFiveDetailVo tempHFiveDetailVo = new TempHFiveDetailVo();

        tempHFiveDetailVo.sethId(list.get(0).getHId());
        tempHFiveDetailVo.setTitle(list.get(0).getTitle());
        tempHFiveDetailVo.setAuthor(list.get(0).getAuthor());
        tempHFiveDetailVo.setCommitTime(list.get(0).getCommitTime());
        tempHFiveDetailVo.setCreateTime(list.get(0).getCreateTime());
        tempHFiveDetailVo.setStaus(list.get(0).getStaus());
        tempHFiveDetailVo.setUseTime(list.get(0).getUseTime());
        tempHFiveDetailVo.setUpdateTime(list.get(0).getUpdateTime());
        tempHFiveDetailVo.setUrl(list.get(0).getUrl());
        tempHFiveDetailVo.setContent(String2FileUtil.downloadFile(list.get(0).getUrl()));
        tempHFiveDetailVo.setBodyContent(String2FileUtil.getBodyContent(tempHFiveDetailVo.getContent()));

        if (list != null && list.size() > 0) {
            JsonReturnUtil.success(tempHFiveDetailVo, request, response);
            return;
        } else {
            JsonReturnUtil.fail("获取详情失败", request, response);
            return;
        }
    }


    @Override
    public void uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!ServletFileUpload.isMultipartContent(request)) {
            EmpExecutionContext.info("上传内容错误");
            return;
        }
        //获取登录信息
        LfSysuser lfSysuser = UserUtil.getUser(request);
        // 创建一个新的文件上传处理程序
        ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
        List<LfFodder> list = String2FileUtil.saveFile(upload, request);//资源服务器的URL
        List<String> pathList = new ArrayList<String>();//响应路径
        String uploadUrl = null;
        if (list != null && list.size() > 0) {//如果上传成功
            for (LfFodder lfFodder : list) {
                uploadUrl = uploadPath + rootPath + lfFodder.getUrl();
                pathList.add(uploadUrl);
            }
            JsonReturnUtil.success(pathList, request, response);
        } else {
            JsonReturnUtil.fail(request, response);
        }

    }

    @Override
    public void saveFodder(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!ServletFileUpload.isMultipartContent(request)) {
            EmpExecutionContext.info("上传内容错误");
            return;
        }
        //获取登录信息
        LfSysuser lfSysuser = UserUtil.getUser(request);
        // 创建一个新的文件上传处理程序
        ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
        List<LfFodder> list = String2FileUtil.saveFile(upload, request);//上传到文件服务器
        LfFodder lfFodder = null;
        if (list != null && list.size() > 0) {
            lfFodder = list.get(0);
        } else {
            EmpExecutionContext.info("上传文件失败");
            JsonReturnUtil.fail(request, response);
            return;
        }
        //进行存库操作
        Long userId = lfSysuser.getUserId();
        lfFodder.setUserId(userId);
        Long id = addObjReturnId(lfFodder);
        if (id > 0) {
            lfFodder.setId(id);
            lfFodder.setUrl(uploadPath + rootPath + lfFodder.getUrl());
            if (StringUtils.isNotEmpty(lfFodder.getFistFramePath())) {//如果首帧不为null
                lfFodder.setFistFramePath(uploadPath + rootPath + lfFodder.getFistFramePath());
            }
            JsonReturnUtil.success(lfFodder, request, response);
        } else {
            JsonReturnUtil.fail(request, response);
        }
    }

    @Override
    public void deleteFodder(List<Integer> fodderIds, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        for (Integer fodderId : fodderIds) {
            LfFodder lfFodder = empDao.findObjectByID(LfFodder.class, fodderId);
            if (null == lfFodder) {
                continue;
            }
            if (StringUtils.isBlank(lfFodder.getUrl())) {
                continue;
            }
            //String filePath = lfFodder.getUrl();
            /*
            注释于2018/10/16,对素材做'假删除',页面删除操作为启禁用素材状态.
             */
            //String delFileResult = String2FileUtil.deleteFile(filePath);
           /* LinkedHashMap<String, String> condition = new LinkedHashMap<String, String>();
            condition.put("id", String.valueOf(fodderId));
            condition.put("userId", String.valueOf(UserUtil.getUser(request).getUserId()));
            if ("1".equals(delFileResult)) {
                int delCount = empTransDao.delete(getConnection(), LfFodder.class, condition);
                if (delCount > 0) {
                    map.put(TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.SUCCESS), lfFodder.toString());
                } else {
                    map.put(TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.FAIL), lfFodder.toString());
                }
            } else {
                EmpExecutionContext.info("文件删除失败:" + filePath);
                map.put(TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.FAIL), lfFodder.toString());
            }*/

            LfFodder lfFodder1 = empDao.findObjectByID(LfFodder.class, fodderId);
            lfFodder1.setStatus(0);
            boolean updateResult = empTransDao.update(getConnection(), lfFodder1);
            if (updateResult) {
                map.put(TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.SUCCESS), lfFodder.toString());
            } else {
                map.put(TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.FAIL), lfFodder.toString());
            }
        }
        JsonReturnUtil.success(map, TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.DELETE_SUCCESS), request, response);

    }

    @Override
    public void getFodder(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {

            JSONObject jsonObject = String2JsonTool.parseToJson(request);
            String userId = jsonObject.getString("userId");
            String page = jsonObject.getString("page") == null ? "1" : jsonObject.getString("page");
            String size = jsonObject.getString("size") == null ? "20" : jsonObject.getString("size");
            String type = jsonObject.getString("type");

            LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
            if (StringUtils.isEmpty(userId)) {
                map.put("userId", String.valueOf(UserUtil.getUser(request).getUserId()));
            } else {
                map.put("userId", userId);
            }
            if (StringUtils.isNotEmpty(type)) {
                map.put("foType", type);
            }
            map.put("status", "1");
            PageInfo pageInfo = new PageInfo();
            pageInfo.setPageSize(Integer.parseInt(size));
            pageInfo.setPageIndex(Integer.parseInt(page));

            LinkedHashMap<String, String> orderMap = new LinkedHashMap<String, String>();
            orderMap.put("id", StaticValue.DESC);

            List<LfFodder> fodderList = empDao.findPageListByCondition(UserUtil.getUser(request).getUserId(), LfFodder.class, map, orderMap, pageInfo);
            for (LfFodder lfFodder : fodderList) {
                lfFodder.setUrl(uploadPath + rootPath + lfFodder.getUrl());
                if (StringUtils.isNotEmpty(lfFodder.getFistFramePath())) {
                    lfFodder.setFistFramePath(uploadPath + rootPath + lfFodder.getFistFramePath());
                }
            }

            //返回值容器
            LfFodderVo lfFodderVo = new LfFodderVo();
            lfFodderVo.setList(fodderList);
            lfFodderVo.setTotalPage(pageInfo.getTotalPage());
            lfFodderVo.setTotalRecord(pageInfo.getTotalRec());
            JsonReturnUtil.success(lfFodderVo, request, response);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "获取素材列异常");
            JsonReturnUtil.fail("获取素材列表异常", request, response);
        }
    }


    /**
     * 上传审核平台
     * V2.0 版本时调用的方法
     *
     * @return tmpid 审核平台返回的模板ID
     */
    public String submGwCenterV2(String loginOrgCode, List<TempParams> list, Map<String, String> resultMap) {
        String tmpid = "";
        RMSApiBiz rmsBiz = new IRMSApiBiz();
        try {
            // 通过企业编号去查询数据库中可使用的一个账号密码
        	UserDataDAO userDataDAO = new UserDataDAO();
            UserDataVO userData = userDataDAO.getSPUser(loginOrgCode);
            if (null != userData) {
                //  将账账号密码设置成数据库中查询出来的值
                Map<String, String> tmplMap = rmsBiz.subTemplate(userData.getUserId().toUpperCase(), userData.getPassWord(), list);
                if ((null != tmplMap) && (null != tmplMap.get("result")) && ("0".equals(tmplMap.get("result")))) {
                    tmpid = tmplMap.get("tmplid");
                    resultMap.putAll(tmplMap);
                }else{
                    //返回的tmplMap需要有值，才加入到resultMap中去
                    if(tmplMap!=null&&tmplMap.size()>0) {
                        resultMap.putAll(tmplMap);
                    }
                }
            } else {
                resultMap.put("result", "-1");
                resultMap.put("desc","没有SP账号！");
                EmpExecutionContext.error("没有发送网关的SP账号.....");
            }
        } catch (Exception e) {
            resultMap.put("result", "-1");
            resultMap.put("desc",e.getMessage());
            EmpExecutionContext.error(e,"提交富信模板失败:" + e.toString());
        }
        return tmpid;
    }

    /**
     * @param loginOrgCode
     * @param list
     * @param smContent
     * @param smParamReg
     * @param title
     * @param templVer     V3.0 提交模板时调用的方法
     * @return
     */
    public String submGwCenterV3(String loginOrgCode, List<TempParams> list, String smContent, String smParamReg, String title, String templVer, Map<String, String> resultMap) {
        String tmpid = "";
        RMSApiBiz rmsBiz = new IRMSApiBiz();
        try {
            // 通过企业编号去查询数据库中可使用的一个账号密码
        	UserDataDAO userDataDAO = new UserDataDAO();
            UserDataVO userData = userDataDAO.getSPUser(loginOrgCode);
            if (null != userData) {
                //  将账账号密码设置成数据库中查询出来的值
                Map<String, String> tmplMap = rmsBiz.subTemplate(userData.getUserId().toUpperCase(), userData.getPassWord(), list, smContent, smParamReg, title, templVer);
                if ((null != tmplMap) && (null != tmplMap.get("result")) && ("0".equals(tmplMap.get("result")))) {
                    tmpid = tmplMap.get("tmplid");
                    resultMap.putAll(tmplMap);
                }else{
                    //返回的tmplMap需要有值，才加入到resultMap中去
                    if(tmplMap!=null&&tmplMap.size()>0) {
                        resultMap.putAll(tmplMap);
                    }
                }
            } else {
                resultMap.put("result", "-1");
                resultMap.put("desc","没有SP账号！");
                EmpExecutionContext.error("没有发送网关的SP账号.....");
            }
        } catch (Exception e) {
            resultMap.put("result", "-1");
            resultMap.put("desc",e.getMessage());
            EmpExecutionContext.error(e,"提交富信模板失败:" + e.toString());
        }
        return tmpid;
    }

    @Override
    public void updateIndustryUse(LfIndustryUse lfIndustryUse, HttpServletRequest request, HttpServletResponse response) {
        if (null == lfIndustryUse) {
            JsonReturnUtil.fail(TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.PARAM_RECEIVE_EXCEPTION), request, response);
            EmpExecutionContext.error("参数接收异常");
            return;
        }
        if (null == lfIndustryUse.getId()) {
            JsonReturnUtil.fail(TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.PARAM_RECEIVE_EXCEPTION), request, response);
            EmpExecutionContext.error("参数接收异常");
            return;
        }
        try {

            LfIndustryUse lfIndustryUseDb = empDao.findObjectByID(LfIndustryUse.class, lfIndustryUse.getId());
            if (null != lfIndustryUse.getType()) {
                lfIndustryUseDb.setType(lfIndustryUse.getType());
            }
            if (StringUtils.isNotEmpty(lfIndustryUse.getName())) {
                lfIndustryUseDb.setName(lfIndustryUse.getName());
            }
            lfIndustryUseDb.setUpdatetm(new Timestamp(System.currentTimeMillis()));
            lfIndustryUse.setOperator(Long.valueOf(UserUtil.getUser(request).getCorpCode()));
            empDao.update(lfIndustryUseDb);
            JsonReturnUtil.success(TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.SUCCESS), request, response);
            return;
        } catch (Exception e) {
            JsonReturnUtil.fail(TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.SYSTEM_EXCEPTION), request, response);
            EmpExecutionContext.error("系统异常");
            return;
        }
    }

    /**
     * 验证关键字及参数格式是否正确的方法
     *
     * @param context
     * @param corpCode
     * @throws IOException
     */
    public String checkBadWord(String context, String corpCode) {
        String words = "";
        try {
            return words = keyWordAtom.checkText(context, corpCode);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "关键字校验出现异常");
        }
        return words;
    }

    /**
     * 关键字多语言
     * @param keyWords 关键字
     * @param locale 多语言
     * @param flag 1为主题  2位内容
     * @return 返回错误提示
     */
    private String mutilKeyWordLanguage(String keyWords, Locale locale, int flag) {
        String msg = "";
        String errMsg;
        if (null != locale) {
            if ("cn".equalsIgnoreCase(locale.getCountry())) {
                errMsg = flag == 1 ? "模板主题" : "内容";
                msg = "您编辑的" + errMsg + "包含关键字[" + keyWords + "]";
            }
            if ("tw".equalsIgnoreCase(locale.getCountry())) {
                errMsg = flag == 1 ? "模板主題" : "内容";
                msg = "您編輯的" + errMsg + "包含關鍵字[" + keyWords + "]";
            }
            if ("en".equalsIgnoreCase(locale.getLanguage())) {
                errMsg = flag == 1 ? "The subject of template" : "The content";
                msg = errMsg + " contains keywords[" + keyWords + "]";
            }
        }
        return msg;
    }

    @Override
    public void getTempById(Long tmId, HttpServletRequest request, HttpServletResponse response) {
        LfTemplate lfTemplate = null;
        try {
            lfTemplate = empDao.findObjectByID(LfTemplate.class, tmId);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "根据模板自增id查询模板信息异常 id:" + tmId);
        }
        JsonReturnUtil.success(lfTemplate, request, response);
    }

    @Override
    public String updateShareTemp(String depidstr, String useridstr, String tempId, String InfoType, LfSysuser lfsysuser) {
        String returnmsg = "fail";
        Connection conn = empTransDao.getConnection();
        try {
            if (tempId == null || "".equals(tempId) || InfoType == null || "".equals(InfoType)) {
                return returnmsg;
            }
            empTransDao.beginTransaction(conn);
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            //先删除原有的共享对象
            conditionMap.put("templId", tempId);
            conditionMap.put("templType", InfoType);
            conditionMap.put("shareType", "1");
            conditionMap.put("corpCode", lfsysuser.getCorpCode());
            empTransDao.delete(conn, LfTmplRela.class, conditionMap);
            List<LfTmplRela> binObjList = new ArrayList<LfTmplRela>();
            LfTmplRela tempObj = null;
            //添加选中的共享机构
            if (depidstr != null && !"".equals(depidstr)) {
                String[] deparr = depidstr.split(",");
                for (int i = 0; i < deparr.length; i++) {
                    if (deparr[i] != null && !"".equals(deparr[i])) {
                        tempObj = new LfTmplRela();
                        tempObj.setTemplId(Long.valueOf(tempId));
                        tempObj.setToUserType(1);
                        tempObj.setShareType(1);
                        tempObj.setCreaterId(lfsysuser.getUserId());
                        tempObj.setToUser(Long.valueOf(deparr[i]));
                        tempObj.setTemplType(Integer.valueOf(InfoType));
                        tempObj.setCreateTime(new Timestamp(System.currentTimeMillis()));
                        tempObj.setCorpCode(lfsysuser.getCorpCode());
                        binObjList.add(tempObj);
                    }
                }
            }
            //添加共享的操作员
            if (useridstr != null && !"".equals(useridstr)) {
                String[] userarr = useridstr.split(",");
                for (int i = 0; i < userarr.length; i++) {
                    if (userarr[i] != null && !"".equals(userarr[i])) {
                        tempObj = new LfTmplRela();
                        tempObj.setTemplId(Long.valueOf(tempId));
                        tempObj.setToUserType(2);
                        tempObj.setShareType(1);
                        tempObj.setCreaterId(lfsysuser.getUserId());
                        tempObj.setToUser(Long.valueOf(userarr[i]));
                        tempObj.setTemplType(Integer.valueOf(InfoType));
                        tempObj.setCreateTime(new Timestamp(System.currentTimeMillis()));
                        tempObj.setCorpCode(lfsysuser.getCorpCode());
                        binObjList.add(tempObj);
                    }
                }
            }
            if (binObjList != null && binObjList.size() > 0) {
                empTransDao.save(conn, binObjList, LfTmplRela.class);
            }

            empTransDao.commitTransaction(conn);
            returnmsg = "success";
        } catch (Exception e) {
            empTransDao.rollBackTransaction(conn);
            returnmsg = "fail";
            EmpExecutionContext.error(e, "更新共享对象出现异常！");
        } finally {
            empTransDao.closeConnection(conn);
        }
        return returnmsg;
    }

    @Override
    public boolean isShare(Long tmId, LfSysuser lfSysuser) {
        List<LfTmplRela> lfTmplRelas = meditorDao.findSharTmp(tmId, lfSysuser);

        if (null != lfTmplRelas && lfTmplRelas.size() > 0) {

            LfTemplate lfTemplate = null;
            try {
                lfTemplate = empDao.findObjectByID(LfTemplate.class, tmId);
            } catch (Exception e) {
                EmpExecutionContext.error(e, "主模板表查询异常 tmId:"+tmId);
            }
            String tempUserId = String.valueOf(lfTemplate==null?"":lfTemplate.getUserId());
            String ids = userUtil.getPermissionUserCode(lfSysuser);
            if (StringUtils.isBlank(ids)){
                return false;
            }else {
                String[] idStrs = ids.split(",");
                for (String idStr:idStrs){
                    if (tempUserId.equals(idStr)){
                        return false;
                    }
                }
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public void compoundImageText(JSONObject jsonObject, HttpServletRequest request, HttpServletResponse response) {
        IMGTool imgTool = new IMGTool();
        JSONArray jsonArray = jsonObject.getJSONArray("textEditable");
        String src = jsonObject.getString("src");
        String absoluteSrc = String2FileUtil.getWebRoot()+src;
        String imgName = "01";
        Integer width = jsonObject.getInteger("width");
        Integer height = jsonObject.getInteger("height");
        StringBuilder keyWordBuilder = new StringBuilder();
        Map<String,String> resultMap = imgTool.buildCompositeECIMG(jsonArray,absoluteSrc,imgName,width,height,keyWordBuilder);
        if (resultMap == null || resultMap.size() == 0){
            JsonReturnUtil.fail("图文合成失败",request,response);
            return;
        }
        String srcImg = resultMap.get("srcImg");
        File imageFile = new File(srcImg);
        if (!imageFile.exists()){
            JsonReturnUtil.fail("图文合成的文件不存在",request,response);
            return;
        }
        Long compoundSize = imageFile.length();
        JSONObject responseJson = new JSONObject();
        responseJson.put("compoundSize",compoundSize);
        JsonReturnUtil.success(responseJson,request,response);
    }
}
