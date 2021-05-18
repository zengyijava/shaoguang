package com.montnets.emp.rms.meditor.servlet;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.template.LfTemplate;
import com.montnets.emp.rms.meditor.base.BaseServlet;
import com.montnets.emp.rms.meditor.entity.CardCommon;
import com.montnets.emp.rms.meditor.entity.LfSubTemplate;
import com.montnets.emp.rms.meditor.tools.JsonReturnUtil;
import com.montnets.emp.rms.meditor.tools.String2JsonTool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

public class TempalteManageServlet extends BaseServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1423041091614714800L;

    /**
     * 新增模板：
     */
    public void addTemplate(HttpServletRequest request,HttpServletResponse response) {
    	//模板ID  
    	Long tmid  = 0L;
    	  
        BaseBiz baseBiz = new BaseBiz();
        //模板实体类
        LfTemplate template = new LfTemplate();

        //模板子表实体类
        LfSubTemplate subTemplate = new LfSubTemplate();

        //字模板集合
        List<LfSubTemplate> subTempList = new ArrayList<LfSubTemplate>();
        
        JSONObject object = String2JsonTool.parseToJson(request);
        if(null == object){
        	try {
				JsonReturnUtil.fail("请求参数不能为空！", request, response);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				  EmpExecutionContext.error(e,"请求参数addTemplate");
			}
        }
        if(object==null){
        	object=new JSONObject();
        }
        //{"tmpArr":{"type":""}}
        EmpExecutionContext.error("前端添加模板传递JSON:"+ object.toJSONString());
        //1.获取前端模板JSON数组 [{"type":"11",...},{"type":"12",...},{"type":"13",...}]
        JSONArray tmpArr = String2JsonTool.parseToJsonArray(object.getString("tempArr"));
        //公共信息---------------
        //选择模板方式
        String tmpType =  object.getString("tmpType");
        //富信主题
		template.setTmpType(Integer.parseInt(tmpType));
        //模板名称
        String tmName = object.getString("tmName");
        //富信主题
		template.setTmName(tmName);
        //用途ID
        String useId = object.getString("useId");
        template.setUseid(Integer.parseInt(useId));
//        //行业ID
        String industryId = object.getString("industryId");
        template.setIndustryid(Integer.parseInt(industryId));
        //提交类型-0：保存，1-提交审核
        String subType = object.getString("subType");
        template.setTmState(Long.parseLong(subType));
        
        //参数
        String param = object.getString("param");
        
        //模板状态(1.启用，0.禁用,2.草稿)
//        String tmState = object.getString("tmState");
        
        //2.根据type 字段 判断是哪一类模板JSON
        for(int i = 0 ;i< tmpArr.size();i++){
            JSONObject jsonObject = tmpArr.getJSONObject(i);
            if(null != jsonObject){
                if("11".equals(jsonObject.getString("tmpType"))){//11富媒体、12卡片、13富文本、14短信
                    //模板表实体类
                    parseRmsJson(template,subTemplate,subTempList,jsonObject);
                }
                if("12".equals(jsonObject.getString("tmpType"))){
                    //模板表实体类
                    parseOttJson(template,subTemplate,subTempList,jsonObject);

                }
                if("13".equals(jsonObject.getString("tmpType"))){
                    parseRichTextJson(template,subTemplate,subTempList,jsonObject);
                }
                if("14".equals(jsonObject.getString("tmpType"))){
                    parseSmsTextJson(template,subTemplate,subTempList,jsonObject);
                }
            }

        }
        //5.入库生成模板自增ID TM_ID
            //主表入库生成TM_ID
        try {
            tmid = baseBiz.addObjReturnId(template);
            if( tmid > 0 ){//入库主表成功
                for(LfSubTemplate LfSubTemplate : subTempList){
                	LfSubTemplate.setTmId(tmid);
                    //模板子表入库
                    baseBiz.addObjReturnId(LfSubTemplate);
                }
            }

        } catch (Exception e) {
            EmpExecutionContext.error(e,"模板表入库出现异常");
        }


        //3.解析各类模板JSON，生成资源文件，放在src目录下
            //富媒体JSON

            //卡片JSON

            //富文本JSON

            //短信JSON


        //4.根据资源文件，按照rms\mrcsl 协议 组装bytes[]
           //富媒体读取生成bytes

           //卡片读取生成bytes

           //富文本读取生成bytes

           //短信读取生成bytes



        //6.压缩模板相关资源文件

        //7.上传压缩文件至文件服务器

        //8.上传压缩文件至阿里云

        //9.上传模板文件 bytes 审核平台、返回sp_templateID

        //10.根据TM_ID 更新 sp_templateID


        //11.返回前端 添加模板状态信息： 成功?失败--失败具体信息
        Map<String,String> map = new HashMap<String,String> ();
        map.put("tmpId", String.valueOf(tmid));
        try {
			JsonReturnUtil.success(map, request, response);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"");
		}
    }

    /**
     * 组装短信入库模板实体类
     * @param template
     * @param subTemplate
     * @param subTempList
     * @param jsonObject
     */
    private void parseSmsTextJson(LfTemplate template, LfSubTemplate subTemplate, List<LfSubTemplate> subTempList, JSONObject jsonObject) {

    }

    /**
     * 组装富文本入库模板实体类
     * @param template
     * @param subTemplate
     * @param subTempList
     * @param jsonObject
     */
    private void parseRichTextJson(LfTemplate template, LfSubTemplate subTemplate, List<LfSubTemplate> subTempList, JSONObject jsonObject) {
    }

    /**
     * 组装富媒体入库模板实体类
     * @param template
     * @param jsonObject
     * @return
     */
    private void parseRmsJson(LfTemplate template,LfSubTemplate subTemplate, List<LfSubTemplate> subTempList,  JSONObject jsonObject) {

    }

    /**
     * 组装OTT卡片入库模板实体类
     * @param template
     * @param jsonObject
     */
    @SuppressWarnings("unused")
	private void parseOttJson(LfTemplate template,LfSubTemplate subTemplate, List<LfSubTemplate> subTempList, JSONObject jsonObject) {
        //模板类型
        String type = jsonObject.getString("tmpType");

        //模板内容
        String tmContent = jsonObject.getString("content");

        //行业id

        String industryId = jsonObject.getString("industryId");
        //用途id

        String useId = jsonObject.getString("useId");

        if(null == template){
            template = new LfTemplate();
        }
        if(null == subTemplate){
        	subTemplate = new LfSubTemplate();
        }
        //模板实体类赋值
		template.setTmCode(" ");
		// 是否审核(无需审核-0，未审核-1，同意1，拒绝2)
		template.setIsPass(-1);
		// 模板状态（0无效，1有效，2草稿）
		template.setTmState(1L);
		// 添加时间
		template.setAddtime(Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
		// 模板内容
		template.setTmMsg(" ");//添加的时候还未生成，直接填空字符串
		// 操作员ID
		template.setUserId(1L);
		template.setCorpCode("10001");
		// 模板（3-短信模板;4-彩信模板；11-富信模板）
		template.setTmpType(new Integer("11"));
		if(1 > 0){//动态模板
			template.setParamcnt(1);
			template.setDsflag(1L);
		}else{//静态模板
			template.setParamcnt(0);
			// 静态模板0、动态模板1
			template.setDsflag(0L);
		}
		template.setIsPublic(1);
		template.setUsecount(0L);
		template.setExlJson(" ");
		// 网关审核状态
		template.setAuditstatus(-1);//-1：未审批，0：未审核，1：同意，2：拒绝，3：审核中
		// 网关彩信模板状态
		template.setTmplstatus(0);
		//rms文件版本号
		template.setVer("V3.0");
		template.setSubmitstatus(0);

        //子表实体类赋值
		subTemplate.setTmpType(Integer.parseInt(type));
        subTemplate.setFrontJson(tmContent);
        subTemplate.setAddTime(Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
        subTemplate.setContent(tmContent);
        subTemplate.setEndJson(tmContent);
        subTemplate.setFileUrl("E://ott.mcrsl");
        subTemplate.setFrontJson(tmContent);
        subTemplate.setIndustryId(-1);
        subTemplate.setUseId(-2);
        subTemplate.setPriority(1);
        subTemplate.setStatus(1);
        subTemplate.setCardHtml("<html></html>");
        subTempList.add(subTemplate);
    }

    /**
     * 获取模板的字节数组
     * @return
     */
//	private byte[] getTempLateBytes() {
//
//
//	}

    /**
     * 获取前端传来的模板JSON，获取模板内的资源文件
     */
    public void parseJson2Object() {


    }

    /**
     * 生成卡片终端需要的JSON
     *
     * @return
     */
    public String createEndJson(CardCommon cardCommon) {
        Gson gson = new Gson();
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        //模板ID
        map.put("id", cardCommon.getId());
        //结构ID
        map.put("sid", cardCommon.getSid());
        ////  1:普通卡片 2：html富文本
//		map.put("",);
        //
        return gson.toJson(map).toString();
    }


}
