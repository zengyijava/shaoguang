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
    // ????????????
    private static final String TEMPLATEPATH = "file/templates/";
    //??????????????????IP??????
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

        // ??????????????????
        PageInfo pageInfo = pageListDto.getPageInfo();
        // ??????????????????
        List<TempsVo> tempsVos = (List<TempsVo>) pageListDto.getOb();

        // ??????????????????????????????
        List<TempListDto> tempListDtos = new ArrayList<TempListDto>();
        // ???????????????????????????????????????????????????????????????
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
                // ????????????id??????????????????
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
                    if(StaticValue.getCORPTYPE() == 1){//?????????????????????????????????
                        detailInfo.setCorpCode(currUser.getCorpCode());
                    }else{//???????????????????????????
                        detailInfo.setCorpCode("");
                    }
                }
                // ??????????????????
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
                // ?????????????????????????????????????????????????????????????????????
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
                //????????????????????????????????????
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
                    EmpExecutionContext.error("??????????????????????????????" + e);
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
            //????????????????????????
            if (StringUtils.isNotBlank(tempsV.getTmMsg())) {
                // V2.0 V????????????
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
                    } else {//V3.0??????????????????
                        result = zipDownUtil.tempalteDown(relativePath,tempsV.getTmState());
                    }
                } catch (Exception e) {
                    EmpExecutionContext.error(e, "??????????????????????????????:" + tempsV.getTmId());
                }
                /*if (!result) {
                    EmpExecutionContext.error("??????????????????:id=" + tempsV.getTmId() + ";tmMsg=:" + tempsV.getTmMsg());
                }*/
            }
            tempListDto.setDetailInfo(detailInfo);
            tempListDtos.add(tempListDto);
        }
        ListDtoData tempListBtoData = new ListDtoData();
        tempListBtoData.setList(tempListDtos);
        tempListBtoData.setTotalPage(pageInfo.getTotalPage());
        tempListBtoData.setTotalRecord(pageInfo.getTotalRec());
        // ??????
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
            EmpExecutionContext.error(e, "?????????????????????????????????");
        }
        Long tmId = tempsVo.getTmId();
        Integer delCount = null;
        try {
            // ????????????-??????
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
            //?????????
            LfTemplate lfTemplate = empDao.findObjectByID(LfTemplate.class, tmId);
            lfTemplate.setTmState(3L);//????????????
            if (null == lfTemplate.getSptemplid() || lfTemplate.getSptemplid() == 0){
                lfTemplate.setSptemplid(null);
            }
            boolean updateTem= empTransDao.update(connection, lfTemplate);


            // ????????????
            empTransDao.commitTransaction(connection);
            // ????????????-??????
            // ??????
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
            EmpExecutionContext.error(e, "??????????????????");
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
        // ?????????????????????????????????????????????????????????????????????
        if (LfTemplateConfig.IS_NOT_PUBLIC == lfTemplate.getIsPublic()) {
            LfSysuser sysuser = UserUtil.getUser(request);
            // ????????????,????????????????????????id
            String ids = userUtil.getPermissionUserCode(sysuser);
            boolean containId = false;
            //????????????????????????id?????????????????????????????????
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

        //????????????????????????
        if (StringUtils.isNotBlank(lfTemplate.getTmMsg())) {
            // V2.0 ????????????
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
                } else {//V3.0??????????????????
                    result = zipDownUtil.tempalteDown(relativePath,lfTemplate.getTmState());
                }
            } catch (Exception e) {
                EmpExecutionContext.error(e, "??????????????????????????????:" + lfTemplate.getTmid());
            }
            if (!result) {
                EmpExecutionContext.error("??????????????????:id=" + lfTemplate.getTmid() + ";tmMsg=:" + lfTemplate.getTmMsg());
            }
        }

        //??????????????????
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        if (null != tempsVo.getPreviewType()) {
            // ?????????????????????
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
        //???????????????
        LinkedHashMap<String, String> conditionMapPar = new LinkedHashMap<String, String>();
        conditionMapPar.put(TableLfSubTemplate.TM_ID_ENTITY, tempsVo.getTmId()
                .toString());

        try {
            List<LfTempParam> lfTempParams = empDao.findListByCondition(LfTempParam.class, conditionMapPar,
                    null);
            tempDetailDto.setParamArr(lfTempParams);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "????????????");
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
            // ???????????????????????????
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
            Date date = new Date();
            String YandM = sdf.format(date);
            SimpleDateFormat sdfs = new SimpleDateFormat("ddHHmmssSSS");
            String nameVariable = sdfs.format(date);

            // ???????????? ???1???????????????2???????????????3???????????????4???????????????5?????????????????????1???
            String chartType = templateChartVo.getChartType();
            // ????????????
            String chartTitle = templateChartVo.getChartTitle();
            // ???????????????1????????????2???????????????,3????????????????????????1???
            String ptType = templateChartVo.getPtType();
            // ???????????????
            String color = templateChartVo.getColor();
            // ????????????????????????
            String rowValue = templateChartVo.getRowValue();
            // ????????????????????????
            String barRowName = templateChartVo.getBarRowName();
            // ???????????????????????????????????????
            String barColName = templateChartVo.getBarColName();
            // ????????????????????????(????????????????????????,???????????????????????????@?????????)
            String barValue = templateChartVo.getBarValue();
            // ???????????????????????????????????????????????????(????????????????????????,???????????????????????????@?????????)
            String barTableVal = templateChartVo.getBarTableVal();
            // ??????????????????
            String parmValue = templateChartVo.getParmValue();
            // ??????
            String rowNum = templateChartVo.getRowNum();
            // ??????
            String colNum = templateChartVo.getColNum();

            /*
             * //???????????? ???1???????????????2???????????????3???????????????4???????????????5?????????????????????1??? String chartType =
             * request.getParameter("chartType"); //???????????? String chartTitle =
             * request.getParameter("chartTitle");
             * //???????????????1????????????2???????????????,3????????????????????????1??? String ptType =
             * request.getParameter("ptType"); //??????????????? String color =
             * request.getParameter("color"); //???????????????????????? String rowValue =
             * request.getParameter("rowValue"); //???????????????????????? String barRowName =
             * request.getParameter("barRowName"); //??????????????????????????????????????? String
             * barColName = request.getParameter("barColName");
             * //????????????????????????(????????????????????????,???????????????????????????@?????????) String barValue =
             * request.getParameter("barValue");
             * //???????????????????????????????????????????????????(????????????????????????,???????????????????????????@?????????) String barTableVal =
             * request.getParameter("barTableVal"); //?????????????????? String parmValue =
             * request.getParameter("parmValue"); //?????? String rowNum =
             * request.getParameter("rowNum"); //?????? String colNum =
             * request.getParameter("colNum");
             */

            // ????????????????????????
            String deletePath = request.getSession().getServletContext()
                    .getRealPath("/pythonPicture/" + YandM)
                    .replaceAll("%20", " ");
            File f = new File(deletePath);
            if (!f.exists()) {
                f.mkdirs();
                EmpExecutionContext.info("?????????" + deletePath
                        + "??????????????????pythonPicture???????????????????????????????????????");
            }

            // ?????????????????????
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
                // ??????python??????????????????
                colors = CreatePictureUtil.AssembleStr(color);
                // ??????rowName
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
                // ????????????????????????
                barRowNames = CreatePictureUtil.AssembleStr(barRowName);
                // ???????????????
                barColNames = CreatePictureUtil.AssembleStr(barColName);
                // ???????????????(????????????????????????,???????????????????????????@?????????)
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
                // ????????????????????????
                barRowNames = CreatePictureUtil.AssembleStr(barRowName);
                // ???????????????
                barColNames = CreatePictureUtil.AssembleStr(barColName);
                // ???????????????(????????????????????????,???????????????????????????@?????????)
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
            // ???????????????bean??????????????????json??????
            File file = new File(pictureUrl);
            if (!file.exists()) {
            } else {
                // ????????????
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
            JsonReturnUtil.fail("??????????????????", request, response);
            EmpExecutionContext.error(e, "???????????????????????????");
        }
    }

    @Override
    public void addIndustryUse(String name, String type, String tmpType,
                               HttpServletRequest request, HttpServletResponse response) {
        // dao???
        //MeditorDao meditorDao = new MeditorDaoImp();
        // TODO Auto-generated method stub
        /*
         * LfSysuser loginSysuser = (LfSysuser) request.getSession(false)
         * .getAttribute(StaticValue.SESSION_USER_KEY);
         */

        // ?????? 0-?????????1-??????,??????-1 ??????

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
            // ??????????????????????????????????????????
            LfIndustryUse lf = new LfIndustryUse();
            // ????????????-???????????? ??????????????????????????????
            lf.setName(name);
            lf.setTmpType(Integer.parseInt(tmpType));
            lf.setType(Integer.parseInt(type));// ????????????????????????-????????????????????????????????????????????????
            List<LfIndustryUse> list = meditorDao.getIndustryUseList(lf);
            if (null != list && list.size() > 0) {
                result.append("??????????????????????????????????????????");
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
            EmpExecutionContext.error(e, "??????-??????Svt????????????????????????");
        }
    }

    @Override
    public void deleteIndustryUse(String id, HttpServletRequest request,
                                  HttpServletResponse response) {
        try {
            // TODO Auto-generated method stub.
            if (deleteByIds(LfIndustryUse.class, id) != null) {
                // ????????????
                JsonReturnUtil.success(request, response);
            } else {
                JsonReturnUtil.fail(request, response);
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "????????????");
        }
    }

    @Override
    public void getIndustryUses(String name, String type, String tmpType,
                                HttpServletRequest request, HttpServletResponse response) {
        try {
            List<LfIndustryUse> industryUseList = new ArrayList<LfIndustryUse>();
            LfIndustryUse lfIndustryUse = new LfIndustryUse();
            // ??????-????????????
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
        //??????????????????
        String langName = (String) request.getSession(false).getAttribute(StaticValue.LANG_KEY);
        //??????ID
        Long tmid = 0L;
        //????????????????????????
        boolean result = false;
        //????????????-??? true ???????????????????????????????????????????????????????????????
        boolean editFlag = false;

        String h5URL = " ";
        //?????????-???????????????????????????????????????????????????
        String version = SystemGlobals.getValue("montnets.rms.editor.version", "V3.0");
        //???????????????????????????????????????
        String createOtt = SystemGlobals.getValue("montnets.rms.editor.createOtt", "false").trim();
        //????????????????????????Map
        Map<String, String> map2 = new HashMap<String, String>();
        StringBuilder keyWordBuilder = new StringBuilder();
        //????????????
        if (StringUtils.isBlank(tmpData.getTmName()) && tmpData.getSubType() == 1) {
            JsonReturnUtil.fail(99, TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.TEMPLATE_NAME_CANNOT_BE_BLANK), request, response);
            return;
        }

        try {
            String corpCode = "";
            LfSysuser sysuser = UserUtil.getUser(request);
            corpCode = sysuser.getCorpCode();
            // 2.??????type ?????? ????????????????????????JSON
            //??????????????????
            LfTemplate template = new LfTemplate();
            List<SubTempData> subDatas = tmpData.getTempArr();
            // ???????????????
            List<LfSubTemplate> subTempList = new ArrayList<LfSubTemplate>();
            //???????????????
            List<LfTempParam> paramList = new ArrayList<LfTempParam>();
            //???????????????
            int priority = 0;
            String mcrslUrl = "";
            //????????????
            String smContent = "";
            //??????????????????
            String smParamReg = "";
            //??????JSON
            String frontJson = "";
            //appContent H5??????JSON
            String appContent = "";
            //?????????????????????
            template = combineLFtemplate(sysuser, tmpData);
            //????????????
            if (null == tmpData.getTmid() || tmpData.getTmid() == 0) {
                // ????????????-??????
                empTransDao.beginTransaction(conn);
                // ??????????????????TM_ID
                tmid = empTransDao.saveObjectReturnID(conn, template);
                result = true;
            } else {//???????????????
                tmid = tmpData.getTmid();
            }
            //????????????????????????
            combineParam(tmid, tmpData, paramList);
            int degree = 0;
            int degreeSize = 0;
            //????????????????????????????????????
            List<TempParams> subGwlist = new ArrayList<TempParams>();
            //???????????????????????????
            if(null != template && StringUtils.isNotBlank(template.getTmName())){
                //?????????????????????
                String keyWords = checkBadWord(template.getTmName().trim(), corpCode);
                if (!"".equals(keyWords)) {
                    //1????????????
                    String msg = mutilKeyWordLanguage(keyWords, locale, 1);
                    JsonReturnUtil.fail(99, msg, request, response);
                    return;
                }
            }
            for (SubTempData subData : subDatas) {
                priority++;
                //??????????????????byte[]
                if (null != subData) {
                    Integer type = subData.getTmpType();
                    switch (type) {
                        case 11:
                            Map<String, Object> richMap = analysisRichMedia(tmpData, subData, tmid, sysuser, priority, langName, paramList, subTempList, subGwlist, version, createOtt, keyWordBuilder);
                            //?????????????????????????????????
                            if (11 == tmpData.getTmpType()) {
                                degree = (Integer) richMap.get("degree");
                                degreeSize = (Integer) richMap.get("degreeSize");
                            }
                            break;
                        // ??????
                        case 12:
                            //????????????JSON
                            Map<String, Object> cardMap = analysisCard(tmpData, subData, tmid, sysuser, priority, langName, paramList, subTempList, subGwlist, version, createOtt, keyWordBuilder);
                            //?????????????????????????????????
                            if (12 == tmpData.getTmpType()) {
                                degree = (Integer) cardMap.get("degree");
                                degreeSize = (Integer) cardMap.get("degreeSize");
                            }
                            break;
                        // ?????????
                        case 13:
                            Map<String, Object> richTextMap = analysisRichText(tmpData, subData, tmid, sysuser, priority, langName, paramList, subTempList, subGwlist, version, createOtt, keyWordBuilder);
                            degree = (Integer) richTextMap.get("degree");
                            degreeSize = (Integer) richTextMap.get("degreeSize");
                            break;
                        case 14:
                            // ??????
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
            //???????????????
            String keyWords = "";
            if(StringUtils.isNotEmpty(keyWordBuilder.toString().trim())){
                 keyWords = checkBadWord(keyWordBuilder.toString().trim().replace("\r\n", "").replace(" ", ""), corpCode);
            }
            //?????????????????????
            if (!"".equals(keyWords)) {
                String msg = mutilKeyWordLanguage(keyWords, locale, 2);
                JsonReturnUtil.fail(99, msg, request, response);
                return;
            }
            //???????????? ??? ?????????
            //????????????
            if (null == tmpData.getTmid() || tmpData.getTmid() == 0) {
                int subCount = 0;
                if (tmid > 0) {
                    // ??????????????????
                    for (com.montnets.emp.rms.meditor.entity.LfSubTemplate LfSubTemplate : subTempList) {
                        LfSubTemplate.setTmId(tmid);
                        LfSubTemplate.setCardHtml(" ");

                        String frontCont = LfSubTemplate.getFrontJson();
                        String endCont = LfSubTemplate.getEndJson();
                        // ??????????????????
                        //?????????????????????????????????????????????????????????????????????????????????????????????????????????????????? LF_TEMPCONTENT ????????????
                        LfSubTemplate.setFrontJson(" ");
                        LfSubTemplate.setContent(" ");
                        LfSubTemplate.setEndJson(" ");
                        LfSubTemplate.setH5Url(h5URL);
                        boolean save = empTransDao.save(conn, LfSubTemplate);
                        // ????????????JSON?????????????????????
                        if (save) {
                            subCount++;
                            saveLftempContent(frontCont, endCont, LfSubTemplate, tmid, conn, appContent);
                        }
                    }

                }
                if (tmid > 0 && subCount == subTempList.size()) {
                    // ??????????????????????????????
                    if (null != paramList && paramList.size() > 0) {
                        empTransDao.save(conn, paramList, LfTempParam.class);
                    }
                    //????????????
                    if (0 == tmpData.getSubType()) {
                        map2.put("tmpId", String.valueOf(tmid));
                        result = true;
                    }
                } else {
                    result = false;
                }
                // ????????????
                empTransDao.commitTransaction(conn);
            } else {
                //????????????
                editFlag = true;
                tmid = tmpData.getTmid();
                int subCount = 0;
                //????????????
                for (com.montnets.emp.rms.meditor.entity.LfSubTemplate LfSubTemplate : subTempList) {
                    LfSubTemplate.setTmId(tmid);
                    // ????????????????????????
                    empTransDao.beginTransaction(conn);
                    //??????????????????
                    LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
                    map.put("tmId", tmid.toString());
                    map.put("tmpType", LfSubTemplate.getTmpType().toString());
                    List<com.montnets.emp.rms.meditor.entity.LfSubTemplate> list = empDao.findListByCondition(com.montnets.emp.rms.meditor.entity.LfSubTemplate.class, map, null);
                    com.montnets.emp.rms.meditor.entity.LfSubTemplate lt = null;
                    //???????????????
                    LinkedHashMap<String, String> paramMap = new LinkedHashMap<String, String>();
                    paramMap.put("tmId", String.valueOf(tmid));
                    List<LfTempParam> lfTempParams = empDao.findListByCondition(LfTempParam.class, paramMap, null);
                    if (!lfTempParams.isEmpty()) {

                        empTransDao.delete(conn, LfTempParam.class, paramMap);
                    }
                    // ??????????????????
                    if (null != paramList && paramList.size() > 0) {
                        empTransDao.save(conn, paramList, LfTempParam.class);
                    }
                    if (null != list && list.size() > 0) {
                        lt = list.get(0);
                        LfSubTemplate.setId(lt.getId());
                        String frontCont = LfSubTemplate.getFrontJson();
                        String endContent = LfSubTemplate.getEndJson();
                        //?????????????????????????????????????????????????????????????????????????????????????????????????????????????????? LF_TEMPCONTENT ????????????
                        LfSubTemplate.setFrontJson(" ");
                        LfSubTemplate.setContent(" ");
                        LfSubTemplate.setEndJson(" ");
                        LfSubTemplate.setH5Url(h5URL);
                        // ??????????????????
                        //????????????
                        boolean save = empTransDao.update(conn, LfSubTemplate);
                        // ????????????JSON?????????????????????
                        if (save) {
                            //??????????????????????????????JSON
                            //???????????????
                            LinkedHashMap<String, String> contMap = new LinkedHashMap<String, String>();
                            contMap.put("tmId", String.valueOf(tmid));
                            contMap.put("tmpType", LfSubTemplate.getTmpType().toString());
                            List<LfTempContent> lfContents = empDao.findListByCondition(LfTempContent.class, contMap, null);
                            if (!lfContents.isEmpty()) {
                                int delete = empTransDao.delete(conn, LfTempContent.class, contMap);
                            }
                            //????????????????????????????????????JSON
                            saveLftempContent(frontCont, endContent, LfSubTemplate, tmid, conn, appContent);
                            subCount++;
                        }
                    }
                }
                // ????????????
                empTransDao.commitTransaction(conn);
                // ????????????-??????
                // ??????????????????
                if (subCount == subTempList.size()) {
                    //type = 0 - ????????????
                    if (tmpData.getSubType() == 0) {
                        map2.put("tmpId", String.valueOf(tmid));
                    }
                    result = true;
                } else {
                    result = false;
                }
            }
            //??????EXTJSON
            tmpData.setTmid(tmid);
            ParamTool.saveExcleJson(tmpData, locale);

            //??????????????????
            String sourcePath = new TxtFileUtil().getWebRoot() + TEMPLATEPATH + corpCode + "/" + tmid;
            String zipPath = new File(sourcePath + ".zip").getPath();
            //?????? ????????????
            ZipTool.createZip(sourcePath, zipPath);
            String fileCenterPath = TEMPLATEPATH + corpCode + "/" + tmid + ".zip";
            boolean uploadFlag = FileOperatorTool.uploadFileCenter(fileCenterPath);

            //???????????????????????????
            Map<String, String> resultMap=new HashMap<String, String>();
            if (1 == tmpData.getSubType()) {
                //1-??????????????????
                //????????????
                String title = (null == tmpData.getTmName()) ? "" : tmpData.getTmName();
                String templVer = "3";
                String spTmpID = "";
                if ("V2.0".equalsIgnoreCase(version)) {
                    spTmpID = submGwCenterV2(corpCode, subGwlist,resultMap);
                } else {
                    spTmpID = submGwCenterV3(corpCode, subGwlist, smContent, smParamReg, title, templVer,resultMap);
                }
                if (!StringUtils.IsNullOrEmpty(spTmpID)) {
                    //?????????????????????
                    LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
                    map.put("tmid", tmid.toString());
                    List<LfTemplate> list = empDao.findListByCondition(LfTemplate.class, map, null);
                    if (null != list && list.size() > 0) {
                        LfTemplate lfTemplate = list.get(0);
                        lfTemplate.setTmid(tmid);
                        //?????????????????????
                        lfTemplate.setTmState(1L);
                        lfTemplate.setTmMsg(fileCenterPath);
                        lfTemplate.setSptemplid(Long.parseLong(spTmpID));
                        lfTemplate.setTmName(null == tmpData.getTmName() ? "" : tmpData.getTmName());
                        lfTemplate.setDegree(degree);
                        lfTemplate.setDegreeSize(Long.parseLong(String.valueOf(degreeSize)));
                        lfTemplate.setParamsnum(Integer.parseInt(lfTemplate.getDsflag().toString()));
                        //??????????????????????????????????????????3-?????????
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
                    //????????????????????? 0 -?????????1-??????
                    long dsFlag = 0L;
                    //????????????
                    int paramCnt = 0;
                    if (null != tmpData.getParamArr() && tmpData.getParamArr().size() > 0) {
                        //????????????
                        dsFlag = 1L;
                        paramCnt = tmpData.getParamArr().size();
                    } else {
                        //????????????
                        dsFlag = 0L;
                        paramCnt = 0;

                    }
                    uplfTemplate.setParamcnt(paramCnt);
                    uplfTemplate.setDsflag(dsFlag);
                    uplfTemplate.setDegree(degree);
                    uplfTemplate.setTmMsg(fileCenterPath);
                    uplfTemplate.setDegreeSize(Long.parseLong(String.valueOf(degreeSize)));
                    uplfTemplate.setParamsnum(Integer.parseInt(uplfTemplate.getDsflag().toString()));
                    //????????????????????????ID??????NULL,???????????????0
                    uplfTemplate.setSptemplid(null);
                    empTransDao.update(conn, uplfTemplate);

                }
            }
            // ????????????
            if (result) {
                //????????????
                if (1 == tmpData.getSubType().intValue()) {
                    JsonReturnUtil.success(map2, TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.SUBMIT_SUCCESS), request, response);
                    return;
                }
                //????????????
                if (0 == tmpData.getSubType().intValue()) {
                    JsonReturnUtil.success(map2, TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.SAVE_SUCCESS), request, response);
                    return;
                }
            }
            //??????????????????????????????????????????
            if (!result) {
                if (!editFlag) {
                    empTransDao.beginTransaction(conn);
                    //??????????????????
                    LinkedHashMap<String, String> tempMap = new LinkedHashMap<String, String>();
                    tempMap.put("tmid", String.valueOf(tmid));
                    empTransDao.delete(conn, LfTemplate.class, tempMap);

                    //??????????????????
                    LinkedHashMap<String, String> subMap = new LinkedHashMap<String, String>();
                    subMap.put("tmId", String.valueOf(tmid));
                    empTransDao.delete(conn, LfSubTemplate.class, subMap);

                    //?????????????????????
                    LinkedHashMap<String, String> paramMap = new LinkedHashMap<String, String>();
                    paramMap.put("tmId", String.valueOf(tmid));
                    empTransDao.delete(conn, LfTempParam.class, paramMap);

                    //??????JSON?????????????????????
                    LinkedHashMap<String, String> tempContmMap = new LinkedHashMap<String, String>();
                    paramMap.put("tmId", String.valueOf(tmid));
                    empTransDao.delete(conn, LfTempContent.class, tempContmMap);

                    empTransDao.commitTransaction(conn);
                }
                //????????????
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
                        //result???desc????????????????????????????????????????????????
                        if ((returnDesc != null && !"".equals(returnDesc))
                                ||(returnResult != null && !"".equals(returnResult))){
                            JsonReturnUtil.fail(99, "???????????????????????????????????????" + returnResult + "????????????" + returnDesc, request, response);
                        }else{
                            JsonReturnUtil.fail(99, TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.SUBMIT_FAIL), request, response);
                        }
                    }else{
                        JsonReturnUtil.fail(99, TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.SUBMIT_FAIL), request, response);
                    }
                    return;
                }
                //????????????
                if (0 == tmpData.getSubType().intValue()) {
                    JsonReturnUtil.fail(99, TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.TEMPLATE_SAVE_FAIL), request, response);
                    return;
                }
            }
        } catch (Exception e) {
            empTransDao.rollBackTransaction(conn);
            EmpExecutionContext.error(e, "???????????????????????????");
            JsonReturnUtil.fail(99, TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.SYSTEM_EXCEPTION), request, response);
            return;
        } finally {
            empTransDao.closeConnection(conn);
        }
    }


    /**
     * ????????????JSON???????????????
     *
     * @param frontCont
     * @param endCont
     * @param lfSubTemplate
     */
    public void saveLftempContent(String frontCont, String endCont, LfSubTemplate lfSubTemplate, Long tmid, Connection conn, String appContent) {
        List<String> frontContList = StringUtils.strToList(frontCont);
        List<String> endContList = StringUtils.strToList(endCont);
        //??????JSON??????LF_TempContent ???
        try {
            if(frontContList != null){
                for (String content : frontContList) {
                    LfTempContent LftempContent = new LfTempContent();
                    LftempContent.setTmId(tmid);
                    LftempContent.setTmpType(lfSubTemplate.getTmpType());
                    LftempContent.setTmpContent(content);
                    //1-??????JSON
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
                    //2-??????JSON
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
                        //3-appContent H5 ??????JSON
                        LftempContent.setContType(3);
                        empTransDao.save(conn, LftempContent);
                    }
                }
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "??????LF_TEMPCONTENT ???????????????.");
        }
    }

    /**
     * H5????????????????????????
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
            //H5????????????tempArr ???????????????????????????????????????????????? ??????+ ?????? ????????????
            String corpCode = sysuser.getCorpCode();
            JSONObject object = (JSONObject) tmpData.getTempArr().get(0).getContent();
            String srcPath = new TxtFileUtil().getWebRoot() + TEMPLATEPATH + sysuser.getCorpCode() + "/" + tmid + "/ott/";
            //todo H5??????????????????????????????
            String h5path = new TxtFileUtil().getWebRoot() + "templates/" + corpCode;
//                            //HTML????????????
            new JsonParseResourceTool().parseH5Html(object, h5path + "/" + tmid + ".html", keyWordBuilder);
            // ??????????????????????????????url
            File h5File = new File(h5path + "/" + tmid + ".html");
            //1-????????????
            String btype = "2";
            //????????????????????????1-html
            String ftype = "1";
            String h5URL = String2FileUtil.uploadFile(h5File, "templates/" + corpCode, btype, ftype);
            h5URL = uploadPath + rootPath + h5URL;

          //  tmpData.getTempArr().get(0).getContent();

            //??????H5?????? ?????????????????????????????????
            MainTemplate dataParsToTemp = TemplateUtil.hFiveToTerminal(tmpData, h5URL, keyWordBuilder);
            dataParsToTemp.setSid(UUID.randomUUID().toString().replaceAll("-", ""));
            String endJson = JSONObject.toJSONString(dataParsToTemp);
            //???ott????????????????????????
            String mcrslUrl = new TxtFileUtil().getWebRoot() + TEMPLATEPATH + corpCode + "/" + tmid + "/ott/src/ott.mrcsl";
            new FileOperatorTool().writeFile(mcrslUrl, endJson);

            // ???????????????
            String frontJson = JSONObject.toJSONString(subData.getContent());
            //??????????????????
            String appContent = JSONObject.toJSONString(tmpData.getApp());
            //H5 ????????????appContent ????????????src
            Map<String, Object> resultMap = new JsonParseResourceTool().storeH5ResourceFile(corpCode, tmid, tmpData, appContent, subType);
            appContent = (String) resultMap.get("frontJson");
            String fileUrl = TEMPLATEPATH + corpCode + "/" + tmid + "/ott/fuxin.mrcsl";
            //???????????????

            combineSubLftemplate(tmpData, subData, subTempList, priority, frontJson, fileUrl, keyWordBuilder,endJson);
            // ??????????????????????????????
            srcPath = new TxtFileUtil().getWebRoot() + TEMPLATEPATH + corpCode + "/" + tmid + "/ott/";
            Map<String, byte[]> map = new OttFileBytesTool().getOttFileList(srcPath + "src/");
            //???????????? ???????????????????????????
            if (tmpData.getSubType() == 1) {
                ottBytes = new OttFileBytesTool().getOttFileBytes("", "", map);
            } else {
                ottBytes = " ".getBytes();

            }
            //?????????????????????????????????
            degree = null == subData.getDegree() ? 0 : subData.getDegree();
            degreeSize = ottBytes.length;
            pnum = paramList.size();
            new GwOperatorTool().getSubmitTmpList(3, ottBytes, degree, degreeSize, pnum, subGwlist);
            //????????????????????????
            String fuxinMrcsl = new TxtFileUtil().getWebRoot() + TEMPLATEPATH + corpCode + "/" + tmid + "/ott/fuxin.mrcsl";
            new FileOperatorTool().writeFile(fuxinMrcsl, ottBytes);
            //?????? ?????? ????????? ????????????---- ????????????????????????+??????
            String titleTxt = tmpData.getApp().getTitle().getText();
            String smContent = titleTxt + "?????????????????? " + h5URL;
            h5Map.put("degree", degree);
            h5Map.put("degreeSize", degreeSize);
            h5Map.put("smContent", smContent);
            h5Map.put("h5URL", h5URL);
            h5Map.put("appContent", appContent);
            //???H5[?????????]???????????????????????????????????????????????????
            String describleContent = tmpData.getApp().getDescription().getText();
            keyWordBuilder.append(titleTxt).append(describleContent);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "H5????????????????????????-??????H5????????????????????????");
        }
        return h5Map;
    }

    /**
     * ??????????????????????????????
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
        //????????????????????????????????????
        smContent = new JsonParseResourceTool().replaceHtmlSpecialSign(smContent);
        smsMap.put("smContent", smContent);
        smsMap.put("smParamReg", smParamReg);
        return smsMap;
    }

    /**
     * ???????????????????????????????????????
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
        //????????????JSON
        String frontJson = JSONObject.toJSONString(subData.getContent());
        String fileUrl = TEMPLATEPATH + sysuser.getCorpCode() + "/" + tmid + "/ott/fuxin.mrcsl";
        combineSubLftemplate(tmpData, subData, subTempList, priority, frontJson, fileUrl, keyWordBuilder," ");
        MainTemplate dataParsToTemp = TemplateUtil.dataParsToTemp(tmpData, keyWordBuilder);
        dataParsToTemp.setSid(UUID.randomUUID().toString().replaceAll("-", ""));
        String endJson = JSONObject.toJSONString(dataParsToTemp);
        endJson = new JsonParseResourceTool().replaceDiv(endJson);
        //?????????JSON?????????????????????builder???
        keyWordBuilder.append(frontJson);
        //???ott????????????????????????
        String mcrslUrl = new TxtFileUtil().getWebRoot() + TEMPLATEPATH + sysuser.getCorpCode() + "/" + tmid + "/ott/src/ott.mrcsl";
        new FileOperatorTool().writeFile(mcrslUrl, endJson);
        //??????????????????????????????
        String srcPath = new TxtFileUtil().getWebRoot() + TEMPLATEPATH + sysuser.getCorpCode() + "/" + tmid + "/ott/src/";
        Map<String, byte[]> map = new OttFileBytesTool().getOttFileList(srcPath);
        if (tmpData.getSubType() == 1) {//???????????? ???????????????????????????
            ottBytes = new OttFileBytesTool().getOttFileBytes("", "", map);
        } else {
            ottBytes = " ".getBytes();

        }
        //????????????
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
     * ????????????????????????
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
        //??????JSON
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
        //???ott????????????????????????
        String mcrslUrl = new TxtFileUtil().getWebRoot() + TEMPLATEPATH + sysuser.getCorpCode() + "/" + tmid + "/ott/src/ott.mrcsl";
        new FileOperatorTool().writeFile(mcrslUrl, endJson);

        // ???????????????
        TempContent tempContent = JSONObject.parseObject(subData.getContent().toString(), TempContent.class);
        String frontJson = JSONObject.toJSONString(subData.getContent());
        Map<String, Object> resultMap = new JsonParseResourceTool().storeOttResourceFile(sysuser.getCorpCode(), tmid, tempContent, frontJson, subType);
        String json = (String) resultMap.get("frontJson");
        String fileUrl = TEMPLATEPATH + sysuser.getCorpCode() + "/" + tmid + "/ott/fuxin.mrcsl";
        combineSubLftemplate(tmpData, subData, subTempList, priority, json, fileUrl, keyWordBuilder,endJson);
        // ??????????????????????????????
        String srcPath = new TxtFileUtil().getWebRoot() + TEMPLATEPATH + sysuser.getCorpCode() + "/" + tmid + "/ott/";
        Map<String, byte[]> map = new OttFileBytesTool().getOttFileList(srcPath + "src/");
        if (tmpData.getSubType() == 1) {//???????????? ???????????????????????????
            ottBytes = new OttFileBytesTool().getOttFileBytes("", "", map);
        } else {
            ottBytes = " ".getBytes();

        }
        //????????????
        degree = null == subData.getDegree() ? 0 : subData.getDegree();
        degreeSize = ottBytes.length;
        pnum = paramList.size();
        subGwFlag = new GwOperatorTool().getSubmitTmpList(3, ottBytes, degree, degreeSize, pnum, subGwlist);
        String fuxinMrcsl = new TxtFileUtil().getWebRoot() + TEMPLATEPATH + sysuser.getCorpCode() + "/" + tmid + "/ott/fuxin.mrcsl";
        new FileOperatorTool().writeFile(fuxinMrcsl, ottBytes);
        //??????H5???????????????
        if (tmpData.getTmpType() == 12) {
//                                h5Html = TemplateUtil.parseToHtml(tempContent);
//                                new FileOperatorTool().writeFile(srcPath +"/"+tmid+".html",h5Html);
//                                File h5File = new File(srcPath +"/"+tmid+".html");
//                                String btype = "2";//1-????????????
//                                String ftype ="1";//????????????????????????1-html
//                                String h5URL  = String2FileUtil.uploadFile(h5File, TEMPLATEPATH + corpCode + "/" + tmid + "/ott", btype, ftype);
//                                //???H5????????????
//System.out.println("12-h5URL:"+h5URL);
//                                degreeSize = h5URL.getBytes().length;
//                                new GwOperatorTool().getSubmitTmpList(2, h5URL.getBytes(), 0, degreeSize, 0, subGwlist);
        }
        cardMap.put("degree", degree);
        cardMap.put("degreeSize", degreeSize);
        return cardMap;
    }

    /**
     * ???????????????????????????????????????
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
        //??????JSON
        String frontJson = "";
        byte[] rmsBytes = null;
        byte[] ottBytes = null;
        int degree = 0;
        int degreeSize = 0;
        int pnum = 0;
        boolean subGwFlag = false;
        String mcrslUrl = "";
        //????????????-0????????????1-????????????
        int subType = tmpData.getSubType();
        Map<String, Object> richMap = new HashMap<String, Object>();
        //??????????????????
        JSONArray jsonArray = (JSONArray) subData.getContent();
        String projectPath = String2FileUtil.getWebRoot();
        String targetPath = projectPath + TEMPLATEPATH + sysuser.getCorpCode() + "/" + tmid + "/rms";
        frontJson = JSONObject.toJSONString(subData.getContent());
        Map<String, Object> resultMap = new JsonParseResourceTool().storeRmsResourceFile(targetPath, projectPath, jsonArray, frontJson, langName, subType, keyWordBuilder);
        String json = (String) resultMap.get("fontJson");
        String fileUrl = TEMPLATEPATH + sysuser.getCorpCode() + "/" + tmid + "/rms/fuxin.rms";
        List<Map<String, String>> list = (List<Map<String, String>>) resultMap.get("list");
        String title = (null == tmpData.getTmName()) ? "" : tmpData.getTmName();
        //subType =0,???????????????1-???????????? ???????????????????????????
        if (tmpData.getSubType() == 1) {
            rmsBytes = new RmsFileBytesTool().getRmsFileBytes(targetPath + "/src", title, list);
        } else {
            rmsBytes = " ".getBytes();
        }
        //????????????
        degree = null == subData.getDegree() ? 0 : subData.getDegree();
        degreeSize = rmsBytes.length;
        pnum = paramList.size();
        subGwFlag = new GwOperatorTool().getSubmitTmpList(1, rmsBytes, degree, degreeSize, pnum, subGwlist);
        String fuxinRms = new TxtFileUtil().getWebRoot() + TEMPLATEPATH + sysuser.getCorpCode() + "/" + tmid + "/rms/fuxin.rms";
        new FileOperatorTool().writeFile(fuxinRms, rmsBytes);
        //??????H5???????????????
        mcrslUrl = TEMPLATEPATH + sysuser.getCorpCode() + "/" + tmid + "/rms/src/fuxin.rms";
        //??????????????????????????????????????????????????????????????????(V3.0??????????????????)
        if (tmpData.getTmpType() == 11 && version.equals("V3.0") && "true".equals(createOtt)) {
//                                h5Html  = TemplateUtil.richMediaParseToHtml(tmpData);
//                                new FileOperatorTool().writeFile(targetPath +"/"+tmid+".html",h5Html);
//                                File h5File = new File(targetPath +"/"+tmid+".html");
//                                String btype = "2";//1-????????????
//                                String ftype ="1";//????????????????????????1-html
//                                String h5URL  = String2FileUtil.uploadFile(h5File, TEMPLATEPATH + corpCode + "/" + tmid + "/rms", btype, ftype);
//                                //???H5????????????
// System.out.println("11-h5URL:"+h5URL);
//                                degreeSize = h5URL.getBytes().length;
//                                new GwOperatorTool().getSubmitTmpList(2, h5URL.getBytes(), 0, degreeSize, 0, subGwlist);

            // ???????????????????????????
            String rmsSrc = targetPath + "/src";
            String ottSrc = targetPath.replace("rms", "ott") + "/src";
            //?????????????????????????????????OTT???src ????????????.txt,.smil ????????????
            new FileOperatorTool().copySrcFile(rmsSrc, ottSrc);
            String tempCont = JSONObject.toJSONString(subData.getContent());
            String editorWidth = tmpData.getEditorWidth() == null ? "260" : tmpData.getEditorWidth().toString();
            MainTemplate dataParsToTemp = TemplateUtil.richMediaToTerminal(tempCont, ottSrc, editorWidth);
            dataParsToTemp.setSid(UUID.randomUUID().toString().replaceAll("-", ""));
            String endJson = JSONObject.toJSONString(dataParsToTemp);
            //???ott????????????????????????
            mcrslUrl = new TxtFileUtil().getWebRoot() + TEMPLATEPATH + sysuser.getCorpCode() + "/" + tmid + "/ott/src/ott.mrcsl";
            new FileOperatorTool().writeFile(mcrslUrl, endJson);
            //??????????????????
            Map<String, byte[]> ottmap = new OttFileBytesTool().getOttFileList(ottSrc);
            //???????????? ???????????????????????????
            if (tmpData.getSubType() == 1) {
                ottBytes = new OttFileBytesTool().getOttFileBytes("", "", ottmap);
            } else {
                ottBytes = "".getBytes();

            }
            //????????????
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
     * ???????????????????????????
     *
     * @param tmpData
     * @param paramList
     */
    private void combineParam(long tmid, TempData tmpData, List<LfTempParam> paramList) {
        if (null != tmpData && null != tmpData.getParamArr()) {
            List<TempDataParam> paramArr = tmpData.getParamArr();
            for (TempDataParam param : paramArr) {
                LfTempParam lfTempParam = new LfTempParam();
                //????????????
                int type = 1;//??????1??????????????????
                if (null != param.getType()) {
                    type = param.getType();
                }
                //?????????
                String name = "";
                if (null != param.getName()) {
                    name = param.getName();
                }
                //???????????? 0?????? 1??????
                int lengthRestrict = 0;
                if (null != param.getLengthRestrict()) {
                    lengthRestrict = param.getLengthRestrict();
                }
                //????????????
                int fixLength = 0;
                if (null != param.getFixLength()) {
                    fixLength = param.getFixLength();
                }
                //????????????
                int minLength = 0;
                if (null != param.getMinLength()) {
                    minLength = param.getMinLength();
                }
                //????????????
                int maxLength = 0;
                if (null != param.getMaxLength()) {
                    maxLength = param.getMaxLength();
                }
                // ??????????????????????????? 0-false ,1-true
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
                //?????????????????????
                lfTempParam.setHasLength(hasLength);
                paramList.add(lfTempParam);
            }
        }
    }

    /**
     * ???????????????????????????
     *
     * @param sysuser
     * @param tmpData
     * @return
     */
    private LfTemplate combineLFtemplate(LfSysuser sysuser, TempData tmpData) {
        LfTemplate template = new LfTemplate();
        // ?????????????????????
        template.setTmCode(" ");
        // ????????????(????????????-0????????????-1?????????1?????????2)
        template.setIsPass(-1);
        // ???????????????0?????????1?????????2?????????
        if (1 == tmpData.getSubType()) {
            template.setTmState(1L);
        } else {
            //????????????
            template.setTmState(2L);
        }
        // ????????????
        template.setAddtime(Timestamp.valueOf(new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss").format(new Date())));
        // ????????????-???????????????????????????????????????????????????
        template.setTmMsg(" ");
        // ?????????ID
        template.setUserId(sysuser.getUserId());
        template.setCorpCode(sysuser.getCorpCode());
        // ?????????3-????????????;4-???????????????11-???????????????-->//?????????????????????????????????H5????????????????????????11,?????????????????????LF_SUB_TEMPLATE ??????????????????
        template.setTmpType(11);
        // ??????????????????
        int paramCnt = 0;
        // ??????????????? 0-?????????1-??????
        long dsFlag = 0L;
        //??????
        if (null != tmpData.getParamArr() && tmpData.getParamArr().size() > 0) {
            paramCnt = tmpData.getParamArr().size();
            dsFlag = 1L;
        } else {// ????????????
            paramCnt = 0;
            dsFlag = 0L;
        }
        template.setParamcnt(paramCnt);
        // ????????????0???????????????1
        template.setDsflag(dsFlag);
        //?????????????????? 0-???????????????1-????????????
        if (null != tmpData.getIsPublic() && 1 == tmpData.getIsPublic()) {
            template.setIsPublic(1);
        } else {
            template.setIsPublic(0);
        }
        //??????????????????
        template.setUsecount(0L);
        //V2.0 ?????????????????????????????????????????? JSON???
        template.setExlJson(" ");
        //?????????????????? -1???????????????0??????????????????1????????????2????????????3????????????
        template.setAuditstatus(-1);
        //???????????????????????????0??????????????????	1???????????????????????????	2????????????????????????????????????
        template.setTmplstatus(0);

        //??????ID,??????-2
        int useID = -2;
        if (null != tmpData.getUseId()) {
            useID = tmpData.getUseId();
        }
        template.setUseid(useID);

        //??????ID,??????-1
        int industryID = -1;
        if (null != tmpData.getIndustryId()) {
            industryID = tmpData.getIndustryId();
        }
        template.setIndustryid(industryID);

        //?????????
        template.setVer("V3.0");
        //???????????? 	0????????????	1???????????????	2???????????????	3???????????? --??????????????????????????????????????????????????????
        template.setSubmitstatus(0);
        //?????????????????? 0-???
        template.setIsShortTemp(0);
        //???????????????
        String tmName = StringUtils.isBlank(tmpData.getTmName()) ? " " : tmpData.getTmName();
        template.setTmName(tmName);
        //1-?????????EMP,2-?????????EMP,3-RCOS ,4-??????????????????,5-??????
        int source = 0;
        //?????????
        if (StaticValue.getCORPTYPE() == 1) {
            source = 1;
        } else {
            source = 2;
        }
        template.setSource(source);
        return template;

    }

    /**
     * ???????????????????????????
     *
     * @param tmpData
     * @param subData
     * @param subTempList
     */
    private void combineSubLftemplate(TempData tmpData, SubTempData subData,
                                      List<LfSubTemplate> subTempList, int priority, String frontJson, String filrUrl, StringBuilder keyWordBuilder,String endJson) {
        // ?????????????????????
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
     * ??????
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
                // ??????
                fileName = fileName + "/fuxin.mrcsl";
                break;
            case 13:
                // ?????????
                fileName = fileName + "/fuxin.h5";
                break;
            case 14:
                //??????
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
            JsonReturnUtil.fail("??????????????????", request, response);
            return;
        }
        if (null == tempsVo.getTmId()) {
            JsonReturnUtil.fail("???????????????id", request, response);
            return;
        }
        if (null == tempsVo.getIsShortTemp()) {
            JsonReturnUtil.fail("?????????????????????", request, response);
            return;
        }
        if (LfTemplateConfig.IS_SHORT_TEMP != tempsVo.getIsShortTemp()
                && LfTemplateConfig.IS_NOT_SHORT_TEMP != tempsVo
                .getIsShortTemp()) {
            JsonReturnUtil.fail(TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.TEMPLATE_NOT_APPROVE), request, response);
            return;
        }
        // ??????????????????.
        LfTemplate lfTemplate = null;
        try {
            lfTemplate = empDao.findObjectByID(LfTemplate.class,
                    tempsVo.getTmId());
        } catch (Exception e) {
            EmpExecutionContext.error(e, "?????????????????????????????????????????????");
        }
        if (null == lfTemplate) {
            JsonReturnUtil.fail("???????????????????????????????????????id", request, response);
            return;
        }
        // ????????????
        // LfSysuser sysuser =
        // (LfSysuser)request.getSession(false).getAttribute("loginSysuser");
        // String corpCode = sysuser.getCorpCode();
        LfSysuser lfSysuser = UserUtil.getUser(request);
        String corpCode = lfSysuser.getCorpCode();
        /*if (!corpCode.equals(lfTemplate.getCorpCode())) {
            JsonReturnUtil.fail("?????????????????????????????????????????????", request, response);
            return;
        }
        if (lfTemplate.getAuditstatus() != LfTemplateConfig.AUDITSTATUS_APPROVE) {
            JsonReturnUtil.fail("?????????????????????????????????????????????", request, response);
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

            //??????7.2??????????????????????????????
            TemplateBiz templateBiz = new TemplateBizImp();

            //???????????????????????????
            if (LfTemplateConfig.IS_SHORT_TEMP == tempsVo.getIsShortTemp()) {
                //?????????????????????????????????????????????????????????
                if (lfShortTempDbs.size() <= 0) {
                    if (lfShortTempDbs.size() > 14) {
                        EmpExecutionContext.info("????????????????????????????????????");
                        JsonReturnUtil.fail("????????????????????????????????????", request, response);
                        return;
                    }
                    empTransDao.save(connection, lfShortTemp);
                    templateBiz.addShotCutTem(String.valueOf(lfTemplate.getTmid()), lfTemplate.getTmName(), request, response);
                }
                //???????????????????????????
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
            EmpExecutionContext.error(e, "?????????????????????????????????????????????");
            JsonReturnUtil.fail("????????????", request, response);
            return;
        } finally {
            empTransDao.closeConnection(connection);
        }

    }


    @Override
    public void getUse(String tmpType, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // TODO Auto-generated method stub
        try {
            // dao???
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
            EmpExecutionContext.error(e, "????????????????????????!");
        }
    }

    @Override
    public void addH5(TempHFiveVo tempHFiveVo, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String rootPath = request.getSession().getServletContext().getRealPath("");
        LfHfive lfHfive = new LfHfive();
        //??????????????????,???????????????
        if (StringUtils.IsNullOrEmpty(tempHFiveVo.getTitle())) {
            JsonReturnUtil.fail("??????????????????", request, response);
            return;
        } else {
            lfHfive.setTitle(tempHFiveVo.getTitle());
            //??????????????????
            LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
            map.put("title", tempHFiveVo.getTitle());
            List<LfHfive> list = getByCondition(LfHfive.class, map, null);
            if (list.size() > 0) {
                JsonReturnUtil.fail("??????????????????", request, response);
                return;
            }
        }

        if (StringUtils.IsNullOrEmpty(tempHFiveVo.getTitle())) {
            JsonReturnUtil.fail("?????????????????????", request, response);
            return;
        } else {
            lfHfive.setAuthor(tempHFiveVo.getAuthor());
        }

        //??????????????????
        if (StringUtils.IsNullOrEmpty(tempHFiveVo.getContent())) {
            JsonReturnUtil.fail("??????????????????", request, response);
            return;
        } else {
            //???h5??????????????????????????????
            //String url = "html\\"+String.valueOf(new Timestamp(System.currentTimeMillis()))+".txt";
            String fileName = String2FileUtil.getTimeString(String.valueOf(new Timestamp(System.currentTimeMillis()))) + ".html";
            String url = rootPath + "\\html\\" + fileName;
            File file = String2FileUtil.string2File(tempHFiveVo.getContent(), url);
            rootPath = "emp_7.2/html";
            String h5url = String2FileUtil.uploadFile(file, rootPath, "2", "1");
            lfHfive.setUrl(h5url);
            if (!StringUtils.IsNullOrEmpty("h5url")) {
                //??????????????????
                String2FileUtil.delLocalFile(url);
            }
        }

        lfHfive.setCreateTime(new Timestamp(System.currentTimeMillis()));
        lfHfive.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        //??????????????????????????????
        lfHfive.setCommitTime(new Timestamp(System.currentTimeMillis()));
        lfHfive.setUseTime(new Timestamp(System.currentTimeMillis()));

        //??????????????????
        lfHfive.setStaus(2);

        Long saveReturnId = 0L;
        saveReturnId = addObjReturnId(lfHfive);
        if (saveReturnId > 0) {
            lfHfive = getById(LfHfive.class, saveReturnId);
            //??????????????????
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
                JsonReturnUtil.fail("??????????????????", request, response);
                return;
            }
            List<LfHfive> lfHfiveList = meditorDao.getH5s(tempHFiveVo, pageinfo);
            JsonReturnUtil.success(lfHfiveList, request, response, pageinfo.getTotalPage(), pageinfo.getTotalRec());
        } else {
            JsonReturnUtil.fail("????????????", request, response);
            return;
        }
    }

    @Override
    public void deleteH5(String hId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (StringUtils.IsNullOrEmpty(hId)) {
            JsonReturnUtil.fail("H5?????????ID??????", request, response);
            return;
        }
        //??????ID?????????
        LfHfive lfHfive = getById(LfHfive.class, hId);
        //????????????url
        String url = lfHfive.getUrl();
        //??????URL???????????????????????????H5??????
        String content = String2FileUtil.downloadFile(url);
        String fpath = UploadFileConfig.UPLOAD_FILE_SAVEPATH;
        //??????H5???HTML
        String2FileUtil.delFile(url, fpath, "2", "1");
        //??????HTML???img
        List<String> list = String2FileUtil.getImageSrc(content);
        //????????????H5????????????????????????
        if (list != null && list.size() > 0) {
            String2FileUtil.delFile(list.get(0), fpath, "2", "2");
        }

        if (deleteByIds(LfHfive.class, hId) > 0) {
            JsonReturnUtil.success(request, response);
        } else {
            JsonReturnUtil.fail("????????????", request, response);
        }
    }

    @Override
    public void updateH5(TempHFiveVo tempHFiveVo, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        String rootPath = request.getSession().getServletContext().getRealPath("");

        if (StringUtils.IsNullOrEmpty(tempHFiveVo.gethId())) {
            JsonReturnUtil.fail("H5??????ID????????????", request, response);
            return;
        }

        if (StringUtils.IsNullOrEmpty(tempHFiveVo.getContent())) {
            JsonReturnUtil.fail("H5????????????????????????", request, response);
            return;
        }


        if (StringUtils.IsNullOrEmpty(tempHFiveVo.getTitle())) {
            JsonReturnUtil.fail("??????????????????", request, response);
            return;
        } else {
            objectMap.put("title", tempHFiveVo.getTitle());
        }
        if (StringUtils.IsNullOrEmpty(tempHFiveVo.getAuthor())) {
            JsonReturnUtil.fail("??????????????????", request, response);
            return;
        } else {
            objectMap.put("author", tempHFiveVo.getAuthor());
        }
        objectMap.put("staus", "2");

        objectMap.put("updateTime", String.valueOf(new Timestamp(System.currentTimeMillis())));
        //??????url
        String fileName = String2FileUtil.getTimeString(String.valueOf(new Timestamp(System.currentTimeMillis()))) + ".html";
        String url = rootPath + "\\html\\" + fileName;
        File file = String2FileUtil.string2File(tempHFiveVo.getContent(), url);
        rootPath = "emp_7.2/html";
        String h5url = String2FileUtil.uploadFile(file, rootPath, "2", "1");
        if (!StringUtils.IsNullOrEmpty("h5url")) {
            //??????????????????
            String2FileUtil.delLocalFile(url);
        }
        objectMap.put("url", h5url);

        conditionMap.put("hId", tempHFiveVo.gethId());

        if (update(LfHfive.class, objectMap, conditionMap)) {
            LfHfive lfHfive = getById(LfHfive.class, tempHFiveVo.gethId());

            //??????????????????
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
            JsonReturnUtil.fail("????????????", request, response);
            return;
        }
    }

    @Override
    public void getH5Detail(String hId, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        map.put("hId", hId);
        List<LfHfive> list = getByCondition(LfHfive.class, map, null);
        //??????????????????
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
            JsonReturnUtil.fail("??????????????????", request, response);
            return;
        }
    }


    @Override
    public void uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!ServletFileUpload.isMultipartContent(request)) {
            EmpExecutionContext.info("??????????????????");
            return;
        }
        //??????????????????
        LfSysuser lfSysuser = UserUtil.getUser(request);
        // ??????????????????????????????????????????
        ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
        List<LfFodder> list = String2FileUtil.saveFile(upload, request);//??????????????????URL
        List<String> pathList = new ArrayList<String>();//????????????
        String uploadUrl = null;
        if (list != null && list.size() > 0) {//??????????????????
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
            EmpExecutionContext.info("??????????????????");
            return;
        }
        //??????????????????
        LfSysuser lfSysuser = UserUtil.getUser(request);
        // ??????????????????????????????????????????
        ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
        List<LfFodder> list = String2FileUtil.saveFile(upload, request);//????????????????????????
        LfFodder lfFodder = null;
        if (list != null && list.size() > 0) {
            lfFodder = list.get(0);
        } else {
            EmpExecutionContext.info("??????????????????");
            JsonReturnUtil.fail(request, response);
            return;
        }
        //??????????????????
        Long userId = lfSysuser.getUserId();
        lfFodder.setUserId(userId);
        Long id = addObjReturnId(lfFodder);
        if (id > 0) {
            lfFodder.setId(id);
            lfFodder.setUrl(uploadPath + rootPath + lfFodder.getUrl());
            if (StringUtils.isNotEmpty(lfFodder.getFistFramePath())) {//??????????????????null
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
            ?????????2018/10/16,????????????'?????????',??????????????????????????????????????????.
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
                EmpExecutionContext.info("??????????????????:" + filePath);
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

            //???????????????
            LfFodderVo lfFodderVo = new LfFodderVo();
            lfFodderVo.setList(fodderList);
            lfFodderVo.setTotalPage(pageInfo.getTotalPage());
            lfFodderVo.setTotalRecord(pageInfo.getTotalRec());
            JsonReturnUtil.success(lfFodderVo, request, response);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "?????????????????????");
            JsonReturnUtil.fail("????????????????????????", request, response);
        }
    }


    /**
     * ??????????????????
     * V2.0 ????????????????????????
     *
     * @return tmpid ???????????????????????????ID
     */
    public String submGwCenterV2(String loginOrgCode, List<TempParams> list, Map<String, String> resultMap) {
        String tmpid = "";
        RMSApiBiz rmsBiz = new IRMSApiBiz();
        try {
            // ?????????????????????????????????????????????????????????????????????
        	UserDataDAO userDataDAO = new UserDataDAO();
            UserDataVO userData = userDataDAO.getSPUser(loginOrgCode);
            if (null != userData) {
                //  ?????????????????????????????????????????????????????????
                Map<String, String> tmplMap = rmsBiz.subTemplate(userData.getUserId().toUpperCase(), userData.getPassWord(), list);
                if ((null != tmplMap) && (null != tmplMap.get("result")) && ("0".equals(tmplMap.get("result")))) {
                    tmpid = tmplMap.get("tmplid");
                    resultMap.putAll(tmplMap);
                }else{
                    //?????????tmplMap???????????????????????????resultMap??????
                    if(tmplMap!=null&&tmplMap.size()>0) {
                        resultMap.putAll(tmplMap);
                    }
                }
            } else {
                resultMap.put("result", "-1");
                resultMap.put("desc","??????SP?????????");
                EmpExecutionContext.error("?????????????????????SP??????.....");
            }
        } catch (Exception e) {
            resultMap.put("result", "-1");
            resultMap.put("desc",e.getMessage());
            EmpExecutionContext.error(e,"????????????????????????:" + e.toString());
        }
        return tmpid;
    }

    /**
     * @param loginOrgCode
     * @param list
     * @param smContent
     * @param smParamReg
     * @param title
     * @param templVer     V3.0 ??????????????????????????????
     * @return
     */
    public String submGwCenterV3(String loginOrgCode, List<TempParams> list, String smContent, String smParamReg, String title, String templVer, Map<String, String> resultMap) {
        String tmpid = "";
        RMSApiBiz rmsBiz = new IRMSApiBiz();
        try {
            // ?????????????????????????????????????????????????????????????????????
        	UserDataDAO userDataDAO = new UserDataDAO();
            UserDataVO userData = userDataDAO.getSPUser(loginOrgCode);
            if (null != userData) {
                //  ?????????????????????????????????????????????????????????
                Map<String, String> tmplMap = rmsBiz.subTemplate(userData.getUserId().toUpperCase(), userData.getPassWord(), list, smContent, smParamReg, title, templVer);
                if ((null != tmplMap) && (null != tmplMap.get("result")) && ("0".equals(tmplMap.get("result")))) {
                    tmpid = tmplMap.get("tmplid");
                    resultMap.putAll(tmplMap);
                }else{
                    //?????????tmplMap???????????????????????????resultMap??????
                    if(tmplMap!=null&&tmplMap.size()>0) {
                        resultMap.putAll(tmplMap);
                    }
                }
            } else {
                resultMap.put("result", "-1");
                resultMap.put("desc","??????SP?????????");
                EmpExecutionContext.error("?????????????????????SP??????.....");
            }
        } catch (Exception e) {
            resultMap.put("result", "-1");
            resultMap.put("desc",e.getMessage());
            EmpExecutionContext.error(e,"????????????????????????:" + e.toString());
        }
        return tmpid;
    }

    @Override
    public void updateIndustryUse(LfIndustryUse lfIndustryUse, HttpServletRequest request, HttpServletResponse response) {
        if (null == lfIndustryUse) {
            JsonReturnUtil.fail(TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.PARAM_RECEIVE_EXCEPTION), request, response);
            EmpExecutionContext.error("??????????????????");
            return;
        }
        if (null == lfIndustryUse.getId()) {
            JsonReturnUtil.fail(TemplateUtil.showRmsMsg(request, MessageUtils.RmsMessage.PARAM_RECEIVE_EXCEPTION), request, response);
            EmpExecutionContext.error("??????????????????");
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
            EmpExecutionContext.error("????????????");
            return;
        }
    }

    /**
     * ???????????????????????????????????????????????????
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
            EmpExecutionContext.error(e, "???????????????????????????");
        }
        return words;
    }

    /**
     * ??????????????????
     * @param keyWords ?????????
     * @param locale ?????????
     * @param flag 1?????????  2?????????
     * @return ??????????????????
     */
    private String mutilKeyWordLanguage(String keyWords, Locale locale, int flag) {
        String msg = "";
        String errMsg;
        if (null != locale) {
            if ("cn".equalsIgnoreCase(locale.getCountry())) {
                errMsg = flag == 1 ? "????????????" : "??????";
                msg = "????????????" + errMsg + "???????????????[" + keyWords + "]";
            }
            if ("tw".equalsIgnoreCase(locale.getCountry())) {
                errMsg = flag == 1 ? "????????????" : "??????";
                msg = "????????????" + errMsg + "???????????????[" + keyWords + "]";
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
            EmpExecutionContext.error(e, "??????????????????id???????????????????????? id:" + tmId);
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
            //??????????????????????????????
            conditionMap.put("templId", tempId);
            conditionMap.put("templType", InfoType);
            conditionMap.put("shareType", "1");
            conditionMap.put("corpCode", lfsysuser.getCorpCode());
            empTransDao.delete(conn, LfTmplRela.class, conditionMap);
            List<LfTmplRela> binObjList = new ArrayList<LfTmplRela>();
            LfTmplRela tempObj = null;
            //???????????????????????????
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
            //????????????????????????
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
            EmpExecutionContext.error(e, "?????????????????????????????????");
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
                EmpExecutionContext.error(e, "???????????????????????? tmId:"+tmId);
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
            JsonReturnUtil.fail("??????????????????",request,response);
            return;
        }
        String srcImg = resultMap.get("srcImg");
        File imageFile = new File(srcImg);
        if (!imageFile.exists()){
            JsonReturnUtil.fail("??????????????????????????????",request,response);
            return;
        }
        Long compoundSize = imageFile.length();
        JSONObject responseJson = new JSONObject();
        responseJson.put("compoundSize",compoundSize);
        JsonReturnUtil.success(responseJson,request,response);
    }
}
