package com.montnets.emp.rms.meditor.tools;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.template.LfTemplate;
import com.montnets.emp.rms.meditor.biz.SynTemplateBiz;
import com.montnets.emp.rms.meditor.biz.imp.MeditorBizImp;
import com.montnets.emp.rms.meditor.biz.imp.SynTemplateBizImp;
import com.montnets.emp.rms.meditor.biz.imp.UserBizImp;
import com.montnets.emp.rms.meditor.entity.LfSubTemplate;
import com.montnets.emp.rms.meditor.entity.LfTempContent;
import com.montnets.emp.rms.meditor.entity.LfTempImportDetails;
import com.montnets.emp.rms.rmsapi.model.TempParams;
import com.montnets.emp.rms.rmsapi.util.DegreeCountUtil;
import com.montnets.emp.rms.tools.ZipTool;
import com.montnets.emp.util.IOUtils;
import com.montnets.emp.util.StringUtils;
import com.montnets.emp.util.TxtFileUtil;

import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ProjectName: (TG)EMP7.2$
 * @Package: com.montnets.emp.rms.meditor.tools$
 * @ClassName: HmParseRmsTool$
 * @Description: 解析通过文件导入生成的对象生成RMSi
 * @Author: xuty
 * @CreateDate: 2018/11/9$ 10:52$
 * @UpdateUser: 更新者
 * @UpdateDate: 2018/11/9$ 10:52$
 * @UpdateRemark: 更新内容
 * @Version: 1.0
 */
public class HmParseRmsTool {
    private static final String TEMPLATEPATH = "file/templates/";
    private String targetPath, projectPath, resourcePath;
    private final SynTemplateBiz templateBiz = new SynTemplateBizImp();
    // rms 最大值 默认 1.9M = 1992294Byte
    private static final int rmsMaxSize = SystemGlobals.getIntValue("montnets.rms.maxsize",1992294);

    /**
     * 解析杭马上传的文件资源
     *
     * @param details
     * @param corpCode
     * @return
     */
    public Map<String, String> analysisRichMedia(LfTempImportDetails details, String corpCode) {
        //转换异常结果集Map
        Map<String, String> reultMap = new HashMap<String, String>();
        try {
            if (0 == StaticValue.getCORPTYPE()) {
                corpCode = "100001";//标准版默认企业为100001
            }
            //根据企业获取当前企业的admin 账号
            LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("corpCode",corpCode);
            conditionMap.put("userName","admin");
            LfSysuser sysuser = new UserBizImp().getUserByCondition(conditionMap);
            if(null == sysuser){
                reultMap.put("sptempId", "");
                reultMap.put("status", "-99");
                reultMap.put("msg", "当前未配置admin管理员，请先配置admin管理员！");
                return  reultMap;
            }

            //1.入库
            LfTemplate template = combineLFtemplate(sysuser,details);
            long tmId = templateBiz.addTemplate(template);
            List<TempParams> subGwlist = new ArrayList<TempParams>();
            List<LfSubTemplate> subTempList = new ArrayList<LfSubTemplate>();
            String fileUrl = "";

            //组装网关参数
            int degree = 1;
            String srcPath = new TxtFileUtil().getWebRoot() + TEMPLATEPATH + sysuser.getCorpCode() + "/" + tmId + "/rms";
            //存储模板资源文件
            Map<String, Object> objectMap = storeRmsResourceFile(details, srcPath);
            if("fail".equalsIgnoreCase(objectMap.get("result").toString())){
                deleteErrorTemplate(tmId);
                reultMap.put("sptempId", "");
                reultMap.put("status", "-99");
                reultMap.put("msg", objectMap.get("cause").toString());
                return  reultMap;
            }
            List<Map<String, String>> list = (List<Map<String, String>>) objectMap.get("list");
            //组装模板资源字节流数组
            byte[] rmsBytes = new RmsFileBytesTool().getRmsFileBytes(srcPath + "/src", " ", list);
            int degreeSize = rmsBytes.length;
            //超过默认rms 尺寸大小，直接返回
            if(degreeSize > rmsMaxSize){
                deleteErrorTemplate(tmId);
                reultMap.put("sptempId", "");
                reultMap.put("status", "-99");
                reultMap.put("msg", "模板文件大小超过RMS文件配置默认大小"+rmsMaxSize +" byte");
                return  reultMap;
            }
            //本次为静态模板
            int pnum = 0;
            //固定为3
            String tempVer = "3";
            degree = getDegree(degreeSize);
            new GwOperatorTool().getSubmitTmpList(1, rmsBytes, degree, degreeSize, pnum, subGwlist);
            //用来存储错误描述的
            Map<String, String> resultMap=new HashMap<String, String>();
            //模板提交RSC，返回模板ID
            String spTempId = new MeditorBizImp().submGwCenterV3(sysuser.getCorpCode(), subGwlist, "", "", "", tempVer,resultMap);

            if(StringUtils.isNotBlank(spTempId)){
                String smilPath = TEMPLATEPATH + sysuser.getCorpCode() + "/" + tmId + "/rms/src/00.smil";

                String smilContent = new SmilFile2FrontJsonTool().getFileContent(smilPath);

                String frontJson = new SmilFile2FrontJsonTool().parseV3Rms(smilContent, smilPath.substring(0, smilPath.lastIndexOf("/") + 1));
                fileUrl = TEMPLATEPATH + sysuser.getCorpCode() + "/" + tmId + "/rms/fuxin.rms";
                //写rms 文件，用于导出
                String fuxinRms = new TxtFileUtil().getWebRoot() + fileUrl;
                new FileOperatorTool().writeFile(fuxinRms, rmsBytes);
                //组子表数据
                combineSubLftemplate(subTempList, frontJson, fileUrl, degree, degreeSize);
                //入库LF_SUB_TEMPLATE,LF_TEMPCONTENT,LF_TEMPPARAM,
                if (tmId > 0) {
                    int subCount = 0;
                    for (com.montnets.emp.rms.meditor.entity.LfSubTemplate LfSubTemplate : subTempList) {
                        LfSubTemplate.setTmId(tmId);
                        LfSubTemplate.setCardHtml(" ");
                        String frontCont = LfSubTemplate.getFrontJson();
                        // 模板子表入库
                        //子表的这三个字段因为模板结构字符串超过数据库该字段的实际存储结构，改为存储到 LF_TEMPCONTENT 拆分表中
                        LfSubTemplate.setFrontJson(" ");
                        LfSubTemplate.setContent(" ");
                        LfSubTemplate.setEndJson(" ");
                        LfSubTemplate.setH5Url(" ");
                        LfSubTemplate.setTmId(tmId);
                        templateBiz.addLfSubTemplate(LfSubTemplate);

                        //增加富媒体补充方式- 短信
                        LfSubTemplate SubTemplate = new LfSubTemplate();
                        SubTemplate.setH5Url(" ");
                        SubTemplate.setH5Type(0);
                        SubTemplate.setAddTime(new Timestamp(new Date().getTime()));
                        SubTemplate.setApp(" ");
                        SubTemplate.setCardHtml(" ");
                        SubTemplate.setContent(" ");
                        SubTemplate.setDegree(0);
                        SubTemplate.setDegreeSize(0);
                        SubTemplate.setFileUrl(" ");
                        SubTemplate.setPriority(1);
                        SubTemplate.setTmpType(14);
                        SubTemplate.setStatus(0);
                        SubTemplate.setIndustryId(-1);
                        SubTemplate.setUseId(-2);
                        SubTemplate.setTmId(tmId);
                        templateBiz.addLfSubTemplate(SubTemplate);
                        // 模板结构JSON拆分存储表入表 LF_TEMPCONTENT
                        saveLftempContent(frontCont, LfSubTemplate, tmId);
                    }
                }
                //压缩资源文件
                String sourcePath = new TxtFileUtil().getWebRoot() + TEMPLATEPATH + sysuser.getCorpCode() + "/" + tmId;
                String zipPath = new File(sourcePath + ".zip").getPath();
                //压缩 绝对路径
                ZipTool.createZip(sourcePath, zipPath);
                String fileCenterPath = TEMPLATEPATH + sysuser.getCorpCode() + "/" + tmId + ".zip";
                //上传本地资源到文件服务器
                boolean uploadFlag = FileOperatorTool.uploadFileCenter(fileCenterPath);
                // 更新模板表LF_TEMPLATE TM_MSG 字段
                updateLfTemplate(String.valueOf(tmId), fileCenterPath, degree, degreeSize, spTempId);
                reultMap.put("sptempId", spTempId);
                reultMap.put("status", "200");
                reultMap.put("msg", "提交审核平台成功！");
            }else{//提交模板失败
                deleteErrorTemplate(tmId);
                reultMap.put("sptempId", "");
                reultMap.put("status", "-99");
                reultMap.put("msg", "提交审核平台失败，请检查是否配置SP账号！");
                return  reultMap;
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e,"批量导入模板出现异常！");
            reultMap.put("sptempId", "");
            reultMap.put("status", "-99");
            reultMap.put("msg", "批量导入模板出现异常！");
            return  reultMap;
        }
        return reultMap;
    }

    /**
     * 删除模板记录
     * @param tmId
     */
    public void deleteErrorTemplate(long tmId){
        try {
            new BaseBiz().deleteByIds(LfTemplate.class,String.valueOf(tmId));
        } catch (Exception e) {
            EmpExecutionContext.error(e,"模板删除出现异常！");
        }
    }

    /**
     * 根据容量获取档位
     *
     * @return
     */
    public int getDegree(int degreeSize) {
        int degree = 0;
        try {
            degreeSize = degreeSize % 1024 == 0 ? degreeSize / 1024 : degreeSize / 1024 + 1;
            DegreeCountUtil countUtil = DegreeCountUtil.getInstance();
            Map<String, String> map = countUtil.queryDegree();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                int beginSize = Integer.parseInt(value.split("-")[0]);
                int endSize = Integer.parseInt(value.split("-")[1]);
                if (((degreeSize > beginSize) || (degreeSize == beginSize)) && (degreeSize < endSize)) {
                    degree = Integer.parseInt(key);
                }
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "获取档位容量出现异常");
        }
        return degree;
    }

    /**
     * 入库报文JSON结构拆分表
     *
     * @param frontCont
     * @param lfSubTemplate
     */
    public void saveLftempContent(String frontCont, LfSubTemplate lfSubTemplate, Long tmid) {
        List<String> frontContList = StringUtils.strToList(frontCont);
        SynTemplateBiz synTemplateBiz = new SynTemplateBizImp();
        //前端JSON入库LF_TempContent 表
        try {
            for (String content : frontContList) {
                LfTempContent LftempContent = new LfTempContent();
                LftempContent.setTmId(tmid);
                LftempContent.setTmpType(lfSubTemplate.getTmpType());
                LftempContent.setTmpContent(content);
                LftempContent.setContType(1);//1-前端JSON
                synTemplateBiz.addLfTempContent(LftempContent);

                //富媒体的入库一条默认的短信补充方式 - 目的是兼容前端编辑器
                LftempContent = new LfTempContent();
                LftempContent.setTmId(tmid);
                LftempContent.setTmpType(14);
                LftempContent.setTmpContent("{\"template\":\"  \"}");
                LftempContent.setContType(1);//1-前端JSON
                synTemplateBiz.addLfTempContent(LftempContent);
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "入库LF_TEMPCONTENT 表出现异常.");
        }
    }

    /**
     * 组装子模板表参数值
     *
     * @param subTempList
     */
    private void combineSubLftemplate(List<LfSubTemplate> subTempList, String frontJson, String filrUrl, int degree, int degreeSize) {
        // 子表实体类赋值
        LfSubTemplate subTemplate = new LfSubTemplate();
        subTemplate.setTmpType(11);
        subTemplate.setAddTime(Timestamp.valueOf(new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss").format(new Date())));
        subTemplate.setContent(frontJson);
        subTemplate.setEndJson(" ");
        subTemplate.setFileUrl(filrUrl);
        subTemplate.setFrontJson(frontJson);
        subTemplate.setIndustryId(-1);
        subTemplate.setUseId(-2);
        subTemplate.setPriority(0);
        subTemplate.setStatus(1);
        subTemplate.setCardHtml(" ");
        subTemplate.setDegree(degree);
        degreeSize = degreeSize % 1024 == 0 ? degreeSize / 1024 : degreeSize / 1024 + 1;
        subTemplate.setDegreeSize(degreeSize * 100);
        subTemplate.setH5Type(0);
        subTempList.add(subTemplate);

    }

    /**
     * 更新模板表LF_TEMPLATE TM_MSG 字段
     *
     * @param tmMsg
     */
    public void updateLfTemplate(String tmId, String tmMsg, int degree, int degreeSize, String spTempId) {
        SynTemplateBiz synTemplateBiz = new SynTemplateBizImp();
        LfTemplate lfTemplate = synTemplateBiz.getTemplateByTmid(tmId);
        lfTemplate.setTmMsg(tmMsg);
        lfTemplate.setDegree(degree);
        degreeSize = degreeSize % 1024 == 0 ? degreeSize / 1024 : degreeSize / 1024 + 1;
        lfTemplate.setDegreeSize(Long.parseLong(String.valueOf(degreeSize)));
        lfTemplate.setSptemplid(Long.parseLong(spTempId));
        synTemplateBiz.updateTemplate(lfTemplate);

    }

    /**
     * 组装模板表字段参数
     *
     * @param sysuser
     * @return
     */
    private LfTemplate combineLFtemplate(LfSysuser sysuser,LfTempImportDetails details) {
        LfTemplate template = new LfTemplate();
        // 模板实体类赋值
        template.setTmCode(" ");
        // 是否审核(无需审核-0，未审核-1，同意1，拒绝2)
        template.setIsPass(-1);
        // 模板状态（0无效，1有效，2草稿）
        template.setTmState(1L);
        // 添加时间
        template.setAddtime(Timestamp.valueOf(new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss").format(new Date())));
        // 模板内容
        template.setTmMsg(" ");// 添加的时候还未生成，直接填空字符串
        // 操作员ID
        template.setUserId(sysuser.getUserId());
        template.setCorpCode(sysuser.getCorpCode());
        // 模板（3-短信模板;4-彩信模板；11-富信模板）
        template.setTmpType(11);//富文本、富媒体、卡片、H5的类型主表都改为11
        //参数个数
        template.setParamcnt(0);
        template.setDsflag(0L);
        //是否公共场景 0-我的场景，1-公共场景
        template.setIsPublic(0);
        //模板使用次数
        template.setUsecount(0L);
        //V2.0 时给模板发送时使用的校验参数 JSON串
        template.setExlJson(" ");
        // 网关审核状态 -1：未审批，0：无需审批，1：同意，2：拒绝，3：审核中
        template.setAuditstatus(-1);
        ////网关彩信模板状态（0：正常，可用	1：锁定，暂时不可用	2：永久锁定，永远不可用）
        template.setTmplstatus(0);

        //用途ID 默认-2
        template.setUseid(-2);

        //行业ID
        template.setIndustryid(-1);

        //版本号
        template.setVer("V3.0");
        //提交状态 	0：未提交	1：提交成功	2：提交失败	3；提交中 --彩信时就有的字段，我也不知道干啥用的
        template.setSubmitstatus(0);
        //是否快捷场景 0-否
        template.setIsShortTemp(0);
        //模板主题名
        String tmName = StringUtils.isBlank(details.getTmName())?" ":details.getTmName();
        template.setTmName(tmName);
        template.setSptemplid(0L);
        template.setAuditstatus(3);
        template.setSource(1);//1-托管版EMP,2-标准版EMP
        return template;

    }


    /**
     * 存储资源文件到 src 目录
     *
     * @param details
     * @param targetPath
     * @return
     */
    public Map<String, Object> storeRmsResourceFile(LfTempImportDetails details, String targetPath) {
        this.targetPath = targetPath + "/src/";
        resourcePath = targetPath + "/resource/";
        projectPath = SystemGlobals.getValue("hm.baseurl","");
        buildFolder();// 创建src文件夹
        buildResouceFolder();//创建 resouce 文件夹
        int x = 1;// 命名计数
        String smilEnd = "</body></smil>";
        String parEnd = "</par>";
        StringBuilder smil = new StringBuilder("<smil><head><layout><region id=\"Text\" width=\"100%\" height=\"120\" fit=\"scroll\"/><region id=\"Image\" width=\"100%\" height=\"100%\" fit=\"meet\" /><region id=\"Video\" width=\"100%\" height=\"100%\" fit=\"meet\"/><region id=\"Audio\" width=\"100%\" height=\"100%\" fit=\"meet\"/></layout></head><body>");
        StringBuilder textTag = new StringBuilder("<text src=\".txt\" region=\"text\"/>");
        StringBuilder imgTag = new StringBuilder("<img src=\".jpg\" region=\"image\"/>");
        StringBuilder gifTag = new StringBuilder("<img src=\".gif\" region=\"image\"/>");
        StringBuilder pngTag = new StringBuilder("<img src=\".png\" region=\"image\"/>");
        StringBuilder videoTag = new StringBuilder("<video src=\".mp4\" region=\"video\"/>");
        StringBuilder audioTag = new StringBuilder("<audio src=\".mp3\" region=\"audio\"/>");
        StringBuilder parBuilder = new StringBuilder("<par dur=\"1200s\">");
        StringBuilder textBuilder = new StringBuilder();

        Map<String, Object> objectMap = new HashMap<String, Object>();
        //模板资源结构List
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();

        if (details != null) {
            //第1帧 文本
            if (StringUtils.isNotBlank(details.getCause())) {
//                String name = String.format("%02d", x++);
//                String text = details.getCause();
//                textBuilder.append(text);
//                parBuilder.append(new StringBuilder(textTag).insert(11, name));
//                buildText(name + ".txt", text);
//                Map<String, String> parMap = new HashMap<String, String>();
//                parMap.put("fileType", "3");
//                list.add(parMap);
            }

            if (StringUtils.isNotEmpty(details.getImageSrc()) && StringUtils.isNotEmpty(details.getVideoSrc())){
                projectPath = projectPath.endsWith("/")?projectPath:projectPath+"/";
                String imgsrc = projectPath + details.getImageSrc();
                String vediosrc = projectPath + details.getVideoSrc();
                File imgFile = new File(imgsrc);
                File vedioFile = new File(vediosrc);
                if(!imgFile.exists()){
                    objectMap.put("result", "fail");
                    objectMap.put("cause", "图片资源不存在！");
                    return objectMap;
                }
                if(!vedioFile.exists()){
                    objectMap.put("result", "fail");
                    objectMap.put("cause", "视频资源不存在！");
                    return objectMap;
                }
            }else{
                objectMap.put("result", "fail");
                objectMap.put("cause", "EXCLE填写的资源路径不存在！");
                return objectMap;
            }

            if (StringUtils.isNotBlank(details.getImageSrc())) {
                //第2帧 图片
                String name = String.format("%02d", x++);
                Map<String, String> parMap = new HashMap<String, String>();
                parBuilder.append(new StringBuilder(imgTag).insert(10, name)).append(parEnd);
                buildFile(name + ".jpg", details.getImageSrc());
                parMap = new HashMap<String, String>();
                parMap.put("fileType", "2");
                list.add(parMap);
                smil.append(parBuilder);
            }
            if (StringUtils.isNotBlank(details.getVideoSrc())) {
                //第3帧 视频
                parBuilder = new StringBuilder("<par dur=\"1200s\">");
                String name = String.format("%02d", x++);
                parBuilder.append(new StringBuilder(videoTag).insert(12, name)).append(parEnd);
                buildFile(name + ".mp4", details.getVideoSrc());
                Map<String, String> parMap = new HashMap<String, String>();
                parMap.put("fileType", "4");
                list.add(parMap);
                smil.append(parBuilder);
            }

        }
        smil.append(smilEnd);
        // 创建RMS文件
        buildText("00.smil", smil.toString());
        objectMap.put("result", "success");
        objectMap.put("list", list);
        return objectMap;

    }

    /**
     * 生成文件夹
     */
    private void buildFolder() {
        File folder = new File(targetPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    private void buildResouceFolder() {
        File folder = new File(resourcePath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }


    /**
     * 复制文件
     *
     * @param newPath
     * @param oldPath
     */
    private void copyFile(String newPath, String oldPath) {
    	InputStream inStream = null;
    	FileOutputStream foutStream = null;
        try {
            int byteread = 0;
            File newfile = new File(newPath);
            File oldfile = new File(oldPath);
            if (oldfile.exists()) {
                inStream = new FileInputStream(oldPath);
                File folder = new File(newfile.getParent());
                if (!folder.exists()) {//新文件
                    folder.mkdirs();
                }
                foutStream = new FileOutputStream(newPath);
                byte[] buffer = new byte[1024];
                while ((byteread = inStream.read(buffer)) != -1) {
                    foutStream.write(buffer, 0, byteread);
                }
//                inStream.close();
//                foutStream.close();
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "文件复制异常！");
        } finally{
        	try {
				IOUtils.closeIOs(inStream, foutStream, null, null, getClass());
			} catch (IOException e) {
				EmpExecutionContext.error(e, "文件关闭异常！");
			}
        }
    }

    /**
     * /**
     * 生成文件
     *
     * @param name
     * @param src
     */
    private boolean buildFile(String name, String src) {
        boolean flag = true;
        projectPath = projectPath.endsWith("/")?projectPath:projectPath+"/";
        src = projectPath + src;
        File srcFile = new File(src);
        if(srcFile.isFile() && srcFile.exists()){
            String target = targetPath + name;
            // 如果路径相同,不允许自己复制自己
            if (!target.equals(src)) {
                copyFile(target, src);
            }
        } else{
            flag = false;
        }
        return  flag;
    }

    /**
     * 生成文本文件
     *
     * @param name
     * @param string
     */
    private void buildText(String name, String string) {
        File file = new File(targetPath, name);
        FileOutputStream fos = null;
        try {
            if (!file.exists()) {
                boolean flag = file.createNewFile();
                if (!flag) {
                    EmpExecutionContext.error("创建文件失败！");
                }
            }
            fos = new FileOutputStream(file.getAbsoluteFile());
            fos.write(string.getBytes("UTF-8"));
//            fos.close();
        } catch (IOException e) {
            EmpExecutionContext.error(e, "文本文件生成异常！");
        }finally{
        	try {
				IOUtils.closeIOs(null, fos, null, null, getClass());
			} catch (IOException e) {
				EmpExecutionContext.error(e, "文件关闭异常！");
			}
        }
    }
}
