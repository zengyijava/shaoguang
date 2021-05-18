package com.montnets.emp.wxgl.biz;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.beanutils.DynaBean;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.wxgl.LfWeiRimg;
import com.montnets.emp.ottbase.biz.BaseBiz;
import com.montnets.emp.ottbase.constant.OttException;
import com.montnets.emp.ottbase.constant.WXStaticValue;
import com.montnets.emp.ottbase.param.WeixParams;
import com.montnets.emp.ottbase.service.WeixService;
import com.montnets.emp.ottbase.util.GlobalMethods;
import com.montnets.emp.ottbase.util.StringUtils;
import com.montnets.emp.ottbase.util.TxtFileUtil;
import com.montnets.emp.wxgl.dao.QunfaDao;

public class QunfaBiz extends BaseBiz
{

    QunfaDao qunfaDao = new QunfaDao();

    // 图文-上传微信服务器图片媒体资源
    public LinkedHashMap<String, LfWeiRimg> uploadImagesToWeixinServer(String aid, ArrayList<LfWeiRimg> otWeiRimgs, String accessToken)
    {
        if(null == aid || "".equals(aid) || null == otWeiRimgs || otWeiRimgs.size() < 1)
        {
            return null;
        }

        LinkedHashMap<String, LfWeiRimg> mediaRimgMap = new LinkedHashMap<String, LfWeiRimg>();
        try
        {
            WeixParams weixParams = new WeixParams();
            weixParams.setAccess_token(accessToken);

            WeixService weixService = new WeixService();

            for (LfWeiRimg otWeiRimg : otWeiRimgs)
            {
                weixParams.setFilename(otWeiRimg.getTitle());
                weixParams.setFileUrl(otWeiRimg.getPicurl());
                weixParams.setMsgtype("image");
                weixParams = weixService.uploadWeixResource(weixParams);
                // 如果上传图片媒体成功，执行下面的操作
                if(weixParams != null && "000".equals(weixParams.getErrCode()))
                {
                    // 获取JSON中的数据
                    JSONObject objResult = weixParams.getJsonObj();
                    mediaRimgMap.put((String) objResult.get("media_id"), otWeiRimg);
                }
                else
                {
                    EmpExecutionContext.error("Quanfa.uploadImagesToWeixinServer Is Error " + (weixParams!=null?weixParams.getErrMsg():""));
                    return null;
                }
            }
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "Quanfa.uploadImages Is Error");
            return null;
        }
        return mediaRimgMap;
    }

    // 高级群发接口-生成上传图文消息素材提交格式
    @SuppressWarnings("unchecked")
    public String convertToArticlesJson(LinkedHashMap<String, LfWeiRimg> mediaRimgMap)
    {
        JSONObject articlesJson = new JSONObject();
        JSONArray articleArray = new JSONArray();
        @SuppressWarnings("rawtypes")
        Iterator iterator = mediaRimgMap.entrySet().iterator();
        while(iterator.hasNext())
        {
            JSONObject articleJson = new JSONObject();
            @SuppressWarnings("rawtypes")
            Entry entry = (Entry) iterator.next();
            LfWeiRimg value = (LfWeiRimg) entry.getValue();
            Object key = entry.getKey();
            articleJson.put("thumb_media_id", String.valueOf(key));
            articleJson.put("author", "");
            articleJson.put("title", value.getTitle());
            String weixBasePath = GlobalMethods.getWeixFilePath();
            // articleJson.put("content_source_url", weixBasePath +
            // value.getPicurl());
            articleJson.put("content_source_url", "");
            articleJson.put("content", value.getDescription());
            articleJson.put("digest", value.getSummary() == null ? "" : value.getSummary());
            articleArray.add(articleJson);
        }
        articlesJson.put("articles", articleArray);
        return articlesJson.toString();
    }

    // 高级群发接口-上传微信服务器图文
    public String uploadArticleToWeixinServer(String aid, String articlesJson, String accessToken)
    {
        WeixParams weixParams = new WeixParams();
        if(null == aid || "".equals(aid) || null == articlesJson || "".equals(articlesJson))
        {
            return null;
        }
        weixParams.setAccess_token(accessToken);
        weixParams.setData(articlesJson);
        WeixService weixService = new WeixService();
        weixParams = weixService.uploadArticleToWeixinServer(weixParams);
        if(weixParams != null && "000".equals(weixParams.getErrCode()))
        {
            JSONObject resultObj = weixParams.getJsonObj();
            return (String) resultObj.get("media_id");
        }
        else
        {
            return null;
        }
    }

    /**
     * 将图文转换成需要下发的JOSN格式数据
     * 
     * @description
     * @param otWeiRimgs
     * @return
     * @author foyoto <foyoto@gmail.com>
     * @datetime 2014年6月6日 下午5:31:29
     */
    public JSONArray convertToCustomerArticles(List<LfWeiRimg> otWeiRimgs)
    {
        JSONArray articleArray = new JSONArray();
        for (LfWeiRimg item : otWeiRimgs)
        {
            JSONObject articleJson = new JSONObject();

            articleJson.put("title", item.getTitle());
            articleJson.put("description", item.getSummary() == null ? "" : item.getSummary());
            String weixBasePath = GlobalMethods.getWeixFilePath();
            if("1".equals(item.getSourceUrl())&&!GlobalMethods.isInvalidString(item.getLink()))
            {
                articleJson.put("url", item.getLink()); 
            }
            else
            {
                articleJson.put("url", weixBasePath + "weix_imgDetail.hts?rimgid=" + String.valueOf(item.getRimgId()));
            }
            articleJson.put("picurl", weixBasePath + item.getPicurl());
            articleArray.add(articleJson);
        }
        return articleArray;
    }

    /**
     * 获取国家，省，城市
     * 
     * @description
     * @param corpCode
     * @param conditionMap
     * @return
     * @author foyoto <foyoto@gmail.com>
     * @datetime 2014年6月6日 下午6:04:32
     */
    public JSONObject getAreas(String corpCode, LinkedHashMap<String, String> conditionMap)
    {
        JSONObject resultJson = new JSONObject();
        List<String> resultList = null;
        try
        {
            resultList = qunfaDao.getAreas(corpCode, conditionMap);

            if("country".equals(conditionMap.get("tp")))
            {
                resultJson.put("", "国家");
            }
            else if("province".equals(conditionMap.get("tp")))
            {
                resultJson.put("", "全省");
            }
            else if("city".equals(conditionMap.get("tp")))
            {
                resultJson.put("", "全部城市");
            }
            for (String item : resultList)
            {
                if(item!=null&&!"".equals(item)){
                   resultJson.put(item, item);
                }
            }
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "获取地区位置失败！-tp=" + conditionMap.get("tp"));
        }
        return resultJson;
    }

    /**
     * 获取国家，省，城市
     * 
     * @description
     * @param area
     * @param province
     * @param city
     * @return
     * @author foyoto <foyoto@gmail.com>
     * @datetime 2014年6月6日 下午6:04:49
     */
    public LinkedHashMap<String, String> getAreaTpAndValue(String area, String province, String city)
    {
        LinkedHashMap tpAndValue = new LinkedHashMap();
        if(StringUtils.isNotBlank(area) && StringUtils.isBlank(province) && StringUtils.isBlank(city))
        {
            tpAndValue.put("tp", "country");
            tpAndValue.put("tpvalue", area);
        }
        else if(StringUtils.isNotBlank(area) && StringUtils.isNotBlank(province) && StringUtils.isBlank(city))
        {
            tpAndValue.put("tp", "province");
            tpAndValue.put("tpvalue", province);
        }
        else if(StringUtils.isNotBlank(area) && StringUtils.isNotBlank(province) && StringUtils.isNotBlank(city))
        {
            tpAndValue.put("tp", "city");
            tpAndValue.put("tpvalue", city);
        }
        return tpAndValue;
    }

    /**
     * 通过地区获取关注用户列表
     * 
     * @description
     * @param corpCode
     * @param aid
     * @param areaid
     * @param province
     * @param city
     * @return
     * @author foyoto <foyoto@gmail.com>
     * @datetime 2014年6月6日 下午6:05:00
     */
    @SuppressWarnings("unchecked")
    public JSONArray getOpenIdByArea(String corpCode, String aid, String areaid, String province, String city)
    {
        JSONArray result = new JSONArray();
        try
        {
            HashMap areaTpandvalue = new HashMap();
            areaTpandvalue = this.getAreaTpAndValue(areaid, province, city);

            if(null != areaTpandvalue && !areaTpandvalue.entrySet().isEmpty())
            {
                LinkedHashMap conditionMap = new LinkedHashMap();
                conditionMap.put("AId", aid);
                conditionMap.put("tp", areaTpandvalue.get("tp"));
                conditionMap.put("tpvalue", areaTpandvalue.get("tpvalue"));
                result = qunfaDao.getOpenIdByArea(corpCode, conditionMap);
            }
            else
            {
                return null;
            }

        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "QunfaBiz#getOpenIdByArea error message is " + e.getMessage());
        }
        return result;
    }

    /**
     * 
     * @description 获取当前组的微信用户openid列表   
     * @param corpCode
     * @param aId
     * @param gId
     * @return       			 
     * @author foyoto <foyoto@gmail.com>
     * @datetime 2014年7月9日 下午2:12:08
     */
    public JSONArray getOpenIdByGroup(String corpCode,String aId,String gId){
        JSONArray result = new JSONArray();
        try{
        	//在线用户组
        	if("0".equals(gId)){
        		// 3天前的时间+15分钟
                Calendar timebeforeThereDays = Calendar.getInstance();
                timebeforeThereDays.add(Calendar.DAY_OF_MONTH, -3);
                timebeforeThereDays.add(Calendar.MINUTE, 15);
                String timeCondition = StringUtils.timeFormat(timebeforeThereDays.getTime());
        		 result = qunfaDao.getOpenIdByOnLineGroup(aId, timeCondition);
        	}
        	//普通群组
        	else{
        		
        		result = qunfaDao.getOpenIdByGroup(corpCode, aId,gId);
        	}
        }catch(Exception e){
            EmpExecutionContext.error(e, "QunfaBiz#getOpenIdByGroup error message is " + e.getMessage());
        }
        return result;
    }
    
    /**
     * 
     * @description 获取当前组的微信用户openid列表   
     * @param corpCode
     * @param aId
     * @param gId
     * @return                   
     * @author foyoto <foyoto@gmail.com>
     * @datetime 2014年7月9日 下午2:12:08
     */
    public String getOpenIdByGroup(String dataStr){
        JSONArray result = new JSONArray();
        try{
            JSONObject data = StringUtils.parsJsonObj(dataStr);
            String corpCode = (String)data.get("lgcorpcode");
            String aId = (String)data.get("aid");
            String gId = (String)data.get("gid");
            if(corpCode!=null&&aId!=null&&gId!=null){
                result = qunfaDao.getOpenIdByGroup(corpCode, aId,gId);
            }
        }catch(Exception e){
            EmpExecutionContext.error(e, "QunfaBiz#getOpenIdByGroup error message is " + e.getMessage());
        }
        return result.toString();
    }

    /**
     * 群组群发-获取文本和图文需要提交的消息格式
     * 
     * @description
     * @param weixParams
     * @return
     * @author foyoto <foyoto@gmail.com>
     * @datetime 2014年6月6日 下午6:05:09
     */
    public String getGroupSendData(WeixParams weixParams)
    {
        String data = "";
        if("text".equals(weixParams.getMsgtype()))
        { // 文本
            data = getGroupTextMsg(weixParams.getGroupId(), weixParams.getContent());
        }
        else if("image".equals(weixParams.getMsgtype()))
        {
            // 图片
            data = getGroupImgMsg(weixParams.getGroupId(), weixParams.getMedia_id());
        }
        else if("mpnews".equals(weixParams.getMsgtype()))
        {
            // 图文
            data = getGroupImgTextMsg(weixParams.getGroupId(), weixParams.getMedia_id());
        }
        return data;
    }

    /**
     * 群组群发-获取文本和图文需要提交的消息格式
     * 
     * @description
     * @param openIds
     * @param weixParams
     * @return
     * @author foyoto <foyoto@gmail.com>
     * @datetime 2014年6月6日 下午6:05:18
     */
    public String getOpenIdSendData(JSONArray openIds, WeixParams weixParams)
    {
        String data = "";
        if("text".equals(weixParams.getMsgtype()))
        { // 文本
            data = getOpenIdTextMsg(openIds, weixParams.getContent());
        }
        else if("mpnews".equals(weixParams.getMsgtype()))
        {
            // 图文
            data = getOpenIdTextImg(openIds, weixParams.getMedia_id());
        }
        return data;
    }

    /**
     * 查询（本日、本周、本月）群发数
     */
    public int getCount(String startTime, String endTime, String aid, String corpCode)
    {
        int result = 0;
        try
        {
            result = qunfaDao.getCount(startTime, endTime, aid, corpCode);
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "查询用户增加情况失败！");
        }
        return result;
    }

    public List<DynaBean> getOpenIdByThereDaysBefore(String aid, String timeCondition)
    {
        List<DynaBean> chatUsers = new ArrayList<DynaBean>();
        try
        {
            chatUsers = qunfaDao.getChatUserInfos(aid, timeCondition);
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "QunfaBiz#getOpenIdByThereDaysBefore查询失败." + e.getMessage());
        }
        return chatUsers;
    }

    /**
     * 根据OpenID列表群发-发送图文消息
     * 
     * @description
     * @param openIds
     * @param media_id
     * @return
     * @author foyoto <foyoto@gmail.com>
     * @datetime 2014年6月6日 下午6:05:32
     */
    public String getOpenIdTextImg(JSONArray openIds, String media_id)
    {
        JSONObject msg = new JSONObject();
        msg.put("touser", openIds);
        msg.put("mpnews", convertToJSONObject("media_id", media_id));
        msg.put("msgtype", "mpnews");
        return msg.toString();
    }

    /**
     * 根据OpenID列表群发-发送文本消息
     * 
     * @description
     * @param openIds
     * @param content
     * @return
     * @author foyoto <foyoto@gmail.com>
     * @datetime 2014年6月6日 下午6:05:39
     */
    public String getOpenIdTextMsg(JSONArray openIds, String content)
    {
        JSONObject msg = new JSONObject();
        msg.put("touser", openIds);
        msg.put("msgtype", "text");
        msg.put("text", convertToJSONObject("content", content));
        return msg.toString();
    }
    
    /**
     * 根据OpenID列表群发-发送图片消息
     * @description    
     * @param openIds
     * @param media_id
     * @return       			 
     * @author foyoto <foyoto@gmail.com>
     * @datetime 2014年7月9日 下午5:22:14
     */
    public String getOpenIdImageMsg(JSONArray openIds,String media_id){
        JSONObject msg = new JSONObject();
        msg.put("touser", openIds);
        msg.put("msgtype", "image");
        msg.put("image", convertToJSONObject("media_id", media_id));
        return msg.toString();
    }
    
    /**
     * 群组群发-发送图文消息
     * 
     * @description
     * @param group_id
     * @param media_id
     * @return
     * @author foyoto <foyoto@gmail.com>
     * @datetime 2014年6月6日 下午6:05:45
     */
    @SuppressWarnings("unchecked")
    public String getGroupImgTextMsg(String group_id, String media_id)
    {
        JSONObject msg = new JSONObject();
        msg.put("filter", convertToJSONObject("group_id", group_id));
        msg.put("mpnews", convertToJSONObject("media_id", media_id));
        msg.put("msgtype", "mpnews");
        return msg.toString();
    }

    /**
     * 群组群发-发送文本
     * 
     * @description
     * @param group_id
     * @param content
     * @return
     * @author foyoto <foyoto@gmail.com>
     * @datetime 2014年6月6日 下午6:05:54
     */
    @SuppressWarnings("unchecked")
    public String getGroupTextMsg(String group_id, String content)
    {
        JSONObject msg = new JSONObject();
        msg.put("filter", convertToJSONObject("group_id", group_id));
        msg.put("text", convertToJSONObject("content", content));
        msg.put("msgtype", "text");
        return msg.toString();
    }

    /**
     * 群组群发-发送图片
     * 
     * @description
     * @param group_id
     * @param media_id
     * @return
     * @author foyoto <foyoto@gmail.com>
     * @datetime 2014年6月6日 下午6:06:01
     */
    @SuppressWarnings("unchecked")
    public String getGroupImgMsg(String group_id, String media_id)
    {
        JSONObject msg = new JSONObject();
        msg.put("filter", convertToJSONObject("group_id", group_id));
        msg.put("image", convertToJSONObject("media_id", media_id));
        msg.put("msgtype", "image");
        return msg.toString();
    }

    /**
     * 群发-将键值对转换成JSONObject对象格式
     * 
     * @description
     * @param key
     * @param value
     * @return
     * @author foyoto <foyoto@gmail.com>
     * @datetime 2014年6月6日 下午6:06:11
     */
    @SuppressWarnings("unchecked")
    private JSONObject convertToJSONObject(String key, String value)
    {
        JSONObject obj = new JSONObject();
        obj.put(key, value);
        return obj;
    }
    
    /**
     * 将群发的日志写到文件中
     * @description    
     * @param fileName
     * @param content       			 
     * @author foyoto <foyoto@gmail.com>
     * @datetime 2014年7月9日 下午7:06:47
     */
    public void writeToTextFile(String data){
        TxtFileUtil txtFileUtil = new TxtFileUtil();
        String filePath = txtFileUtil.getWebRoot() + WXStaticValue.QUNFA_REQUEST_URL;
        try
        {
            File uf = new File(filePath);
            // 更改文件的保存路径，以防止文件重名的现象出现
            if(!uf.exists())
            {
                uf.mkdirs();
            }
            
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            int day = cal.get(Calendar.DAY_OF_MONTH);
            String fileName = filePath + String.valueOf(year)+String.valueOf(month)+String.valueOf(day)+".txt";
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = Timestamp.valueOf(df.format(new Date())).toString();
            txtFileUtil.writeToTxtFile(fileName, data + ":-" + time);
        }
        catch (OttException e)
        {
            EmpExecutionContext.error(e,"群发日志写入文件失败！"+e.getMessage());
        }
    }
}
