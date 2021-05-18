package com.montnets.emp.rms.meditor.biz.imp;

import com.alibaba.fastjson.JSONObject;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.sysuser.LfPrivilege;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.template.LfTemplate;
import com.montnets.emp.rms.meditor.biz.TemplateBiz;
import com.montnets.emp.rms.rmsapi.biz.impl.IRMSApiBiz;
import com.montnets.emp.rms.templmanage.biz.RmsShortTemplateBiz;
import com.montnets.emp.rms.vo.LfShortTemplateVo;
import com.montnets.emp.util.TxtFileUtil;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

public class TemplateBizImp extends BaseBiz implements TemplateBiz {
    // 模板路径
    private static final String TEMPLATEPATH = "file/templates/";
    private final RmsShortTemplateBiz rstlBiz = new RmsShortTemplateBiz();

    @Override
    public void addShotCutTem(String tmId, String tmName, HttpServletRequest request, HttpServletResponse response) {
        // 企业编码
        String lgcorpcode = ((LfSysuser) request.getSession(false).getAttribute(StaticValue.SESSION_USER_KEY)).getCorpCode();
        //企业ID
        long userId = ((LfSysuser) request.getSession(false).getAttribute(StaticValue.SESSION_USER_KEY)).getUserId();
        String tempId = tmId;
        String tempName = tmName;
        if (tempName != null && !tempName.equals("")) {
            try {
                tempName = URLDecoder.decode(tempName, "utf-8");
            } catch (Exception e) {
                EmpExecutionContext.error(e, "快捷模板名称乱码处理失败！");
            }
        }
        LfShortTemplateVo bean = new LfShortTemplateVo();
        bean.setCorpCode(lgcorpcode);
        bean.setUserId(userId);
        bean.setTempId(Long.valueOf(tempId));
        bean.setTempName(tempName);
        //修改tf_template表中isshorttemp字段的值（1表示新增，0表示取消）
        int param = 1;
//				flag=rstlBiz.updateLfTemplate(Long.valueOf(tempId),param);
        //根据menusite = '/rms_templateMana.htm'获取Lf_Privilege表中的PRIVILEGE_ID用于对比
        String menusite = "/rms_templateMana.htm";
        String resourceId = rstlBiz.getPrivilegeId(menusite, bean);
        long resourceIds = resourceId == null ? 93L : Long.valueOf(resourceId);
        List<LfPrivilege> list = new ArrayList<LfPrivilege>();
        LfPrivilege lfPrivilege = new LfPrivilege();
				/*LfShortTemplateVo lfShortTemplate = rstlBiz.getLfShortTemplate(bean);
				lfPrivilege.setMenuCode(String.valueOf(lfShortTemplate.getId()));*/
        lfPrivilege.setMenuCode(tempId);
        lfPrivilege.setMenuName(tempName);
        lfPrivilege.setMenuSite("/rms_rmsSameMms.htm?method=shortCut2Send&tempId=" + tempId);
        lfPrivilege.setModName("我的快捷场景");
        lfPrivilege.setZhHkModName("My shortcut");
        lfPrivilege.setZhTwModName("我的快捷場景");
        lfPrivilege.setResourceId(resourceIds);
        list.add(lfPrivilege);
        @SuppressWarnings("unchecked")
        Map<String, List<LfPrivilege>> priMap = (Map<String, List<LfPrivilege>>) request.getSession(false).getAttribute("priMap");
        Set<String> keys = priMap.keySet();
        for (String key : keys) {
            if (key.equals(String.valueOf(resourceIds))) {
                priMap.get(key).addAll(list);
            }
            if (!priMap.containsKey(String.valueOf(resourceIds))){
                priMap.put(String.valueOf(resourceIds),list);
                break;
            }
        }
        request.getSession(false).setAttribute("priMap", priMap);
    }

    @Override
    public void delShotCutTem(String tmId, HttpServletRequest request, HttpServletResponse response) {
        String tempId = tmId;
        String lgcorpcode = ((LfSysuser) request.getSession(false).getAttribute(StaticValue.SESSION_USER_KEY)).getCorpCode();
        //企业ID
        long userId = ((LfSysuser) request.getSession(false).getAttribute(StaticValue.SESSION_USER_KEY)).getUserId();
        LfShortTemplateVo bean = new LfShortTemplateVo();
        bean.setCorpCode(lgcorpcode);
        bean.setUserId(userId);
        bean.setTempId(Long.valueOf(tempId));
        @SuppressWarnings("unchecked")
        Map<String, List<LfPrivilege>> priMap = (Map<String, List<LfPrivilege>>) request.getSession(false).getAttribute("priMap");
        Map<String, List<LfPrivilege>> priMaps = new HashMap<String, List<LfPrivilege>>();
        Set<String> keys = priMap.keySet();   //此行可省略，直接将map.keySet()写在for-each循环的条件中
        for (String key : keys) {
            for (int i = 0; i < priMap.get(key).size(); i++) {
                if (priMap.get(key).get(i).getMenuCode().equals(tempId)) {
                    priMap.get(key).remove(i);
                }
                priMaps.put(key, priMap.get(key));
            }
        }
        request.getSession(false).setAttribute("priMap", priMaps);
    }

    /**
     * 模板同步
     */
    @Override
    public void TempSynch() {

        //1 模板下载
        IRMSApiBiz irmsApiBiz = new IRMSApiBiz();
        String userid = "";
        String pass = "";
        String tempIds = "";
        Map<String, Object> downLoadRes = new LinkedHashMap<String, Object>();

        try {
            downLoadRes = irmsApiBiz.getTemplate(userid, pass, tempIds);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "模板同步中模板下载失败!");
        }
        if (!Boolean.valueOf(String.valueOf(downLoadRes.get("result")))) {
            EmpExecutionContext.error("模板同步中模板下载失败:" + String.valueOf(downLoadRes.get("result")));
        }

        //2 解析成相应文件
        LfTemplate lfTemplate = lfTemplateInit();//初始化一个模板对象，赋初始值
        String contentOut = (String) downLoadRes.get("content");//外层content
        String contentStr = (String) JSONObject.parseObject(contentOut).get("content");//里层content

        try {
            byte[] contentIn = new BASE64Decoder().decodeBuffer(contentStr);//里层content解码
            String tempBasePath = new TxtFileUtil().getWebRoot() + TEMPLATEPATH + "temporary";//临时目录
            //生成rms目录
            rmsFileCreate(tempBasePath, contentIn, lfTemplate);
            //生成ott目录
            ottFileCreate(tempBasePath, contentIn, lfTemplate);
        } catch (IOException e) {
            EmpExecutionContext.error(e, "base解析为byte[]时异常!");
        }

        //3 解析出入库数据
        //3.1主表


        //4 模板主表入库，返回自增id，生成存放目录
        try {
            Long lfTemplatePriId = empDao.saveObjectReturnID(lfTemplate);
            lfTemplate.setTmid(lfTemplatePriId);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "同步资源文件到emp时主表入库异常!");
        }


        //5 将文件从临时目录迁移到存放目录

        //6 设置src路径，删除临时目录

        //7 模板子表、关联表入库

        //8 文件上传到文件服务器

    }

    @Override
    public boolean rmsFileCreate(String basePath, byte[] contentIn) {
        return rmsFileCreate(basePath, contentIn, null);
    }

    @Override
    public boolean ottFileCreate(String basePath, byte[] contentIn) {
        return ottFileCreate(basePath, contentIn, null);
    }

    @Override
    public boolean rmsFileCreate(String basePath, byte[] contentIn, LfTemplate lfTemplate) {

        return false;
    }

    @Override
    public boolean ottFileCreate(String basePath, byte[] contentIn, LfTemplate lfTemplate) {

        return false;
    }

    public LfTemplate lfTemplateInit() {
        LfTemplate lfTemplate = new LfTemplate();
        lfTemplate.setUserId(0L);//用户id
        lfTemplate.setTmName("");//模板名称 //TODO 对应接口文档中的标题
        lfTemplate.setTmMsg("");//模板内容 //TODO  .rms文件路径
        lfTemplate.setDsflag(0L);//模板类型 //TODO 根据参数数量判断，0则为静态
        lfTemplate.setTmState(1L);//模板状态  默认启用
        lfTemplate.setAddtime(Timestamp.valueOf(new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss").format(new Date())));//创建时间
        lfTemplate.setIsPass(1);//审批状态  默认审批通过
        lfTemplate.setTmpType(11);//模板类型 默认11
        lfTemplate.setBizCode("");//业务编码 //TODO ？？？
        lfTemplate.setCorpCode("");//企业编号 //TODO ？？？
        lfTemplate.setSptemplid(0L);//平台模板ID
        lfTemplate.setAuditstatus(1);//网关审核状态
        lfTemplate.setMmstmplid(0L);//关联彩信模板表MMS_TEMPLATE的Id字段 //TODO
        lfTemplate.setTmplstatus(0);//网关彩信模板状态 //TODO
        lfTemplate.setParamcnt(0);//模板参数个数 //TODO
        lfTemplate.setSubmitstatus(0);//提交状态
        lfTemplate.setEmptemplid("");//EMP系统模块ID //TODO
        lfTemplate.setErrorcode(0);//网关错误编码 //TODO
        lfTemplate.setTmCode("");//模块编码 //TODO
        lfTemplate.setDegree(0);//档位 //TODO
        lfTemplate.setDegreeSize(0L);//容量 //TODO
        lfTemplate.setIndustryid(0);//行业id
        lfTemplate.setIsPublic(1);//是否公共场景
        lfTemplate.setUseid(0);//用途id //TODO
        lfTemplate.setUsecount(0L);//使用次数
        lfTemplate.setExlJson("");//动态模板生成excel的表头json //TODO 杨东磊 动态模板表格校验
        lfTemplate.setIsShortTemp(0);//是否是快捷场景
        lfTemplate.setVer("3.0");//富信模板版本 默认3.0
        lfTemplate.setParamsnum(0);//富信发送接口中参数个数 //TODO 动态1 静态0
        return lfTemplate;
    }
}
