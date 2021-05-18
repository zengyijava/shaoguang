package com.montnets.emp.rms.meditor.tools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.rms.meditor.config.UploadFileConfig;
import com.montnets.emp.rms.meditor.entity.*;
import com.montnets.emp.rms.tools.Excel2JsonDto;
import com.montnets.emp.rms.tools.MediaTool;
import com.montnets.emp.util.StringUtils;
import com.montnets.emp.util.TxtFileUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TemplateUtil {
    /**上传的服务器IP地址*/
    private static final String uploadPath = SystemGlobals.getValue("montnet.rms.uploadPath").trim();
    /**访问的外网地址*/
    private static final String visitPath = SystemGlobals.getValue("montnet.rms.visitPath").trim();
    private static final String uploadRootPath = SystemGlobals.getValue("montnet.rms.nginx.rootPath").trim();

    /**视频裁剪工具路径地址*/
    private static final String toolPath = SystemGlobals.getValue("montnets.rms.cropper.videoToolPath");


    private static Pattern NUMBER_PATTERN = Pattern.compile("[^0-9]+");


    public  static void uploadCropSource(TempData tmpData){
        if (tmpData == null){
            EmpExecutionContext.info("裁剪资源上传对象为null");
            return;
        }
        try {
            JSONArray pagesArr = ((JSONObject)(tmpData.getTempArr().get(0).getContent())).getJSONArray("pages");
            for(Object page:pagesArr){
                JSONObject tempPage = (JSONObject)page;
                JSONArray elementsArray = tempPage.getJSONArray("elements");
                for(Object element:elementsArray){
                    JSONObject tempElement = (JSONObject)element;
                    //1表示资源经过裁剪,需要上传
                    if ("1".equals(tempElement.getString("cropped"))){
                        //判断上传资源类型
                        //2:图片 3:视频 4：音频
                        String fType = "";
                        String bType = "2";
                        if("video".equalsIgnoreCase(tempElement.getString("types"))){
                            fType = "3";
                        }else if("image".equalsIgnoreCase(tempElement.getString("types"))){
                            fType = "2";
                        }
                        String fPath = tempElement.getString("src");
                        String src = new TxtFileUtil().getWebRoot()+fPath;
                        File file = new File(src);
                        if (!file.exists()){
                            EmpExecutionContext.error("上传文件未空");
                        }
                        String uploadUrl = String2FileUtil.uploadFile(file, fPath, bType, fType);//上传资源文件
                        uploadUrl = uploadPath + uploadRootPath + uploadUrl;
                        tempElement.put("src",uploadUrl);
                    }
                }
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e,"上传裁剪资源方法报错");
        }
    }



    public static HashMap getSmsParam(HashMap<String, String> map) {
        HashMap<String, String> param = new HashMap<String, String>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            Matcher m2 = NUMBER_PATTERN.matcher(entry.getKey());
            String all = m2.replaceAll("");
            int numberP = Integer.parseInt(all);
            try {
                param.put("P" + numberP, entry.getValue());
            } catch (Exception e) {
                EmpExecutionContext.error(e, "发现异常");
            }
        }
        return param;
    }

    public static Excel2JsonParam richMediaToString(String content, List<ParamExcel> param) throws Exception {
        //终端对象
        Excel2JsonParam excel2JsonParam = new Excel2JsonParam();
        List<Excel2JsonDto> list = new ArrayList<Excel2JsonDto>();
        JSONArray array = JSONObject.parseArray(content);
        //设置帧个数
        if (array != null) {
            excel2JsonParam.setFmcts(String.valueOf(array.size()));
        } else {
            EmpExecutionContext.info("richMediaToString方法中的content参数为空");
            return null;
        }
        //遍历
        Excel2JsonDto excel2JsonDto;
        HashMap<String, String> text;
        for (int i = 0; i < array.size(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);
            excel2JsonDto = new Excel2JsonDto();
            //设置帧序号
            excel2JsonDto.setFmno(String.valueOf(i));
            Integer numberP = 0;
            excel2JsonDto.setIspara("0");
            //遍历param,获取order值,判断是否和i值相同
            for (ParamExcel paramExcel : param) {
                if (String.valueOf(i + 1).equals(paramExcel.getOrder())) {
                    //含有参数,设置为1
                    excel2JsonDto.setIspara("1");
                    //类型
                    if ("chart".equals(jsonObject.getString("type"))) {
                        excel2JsonDto.setType("1");
                    } else {
                        excel2JsonDto.setType("0");
                    }

                    LinkedHashMap<String, String> map = paramExcel.getMap();
                    text = new LinkedHashMap();
                    //遍历key
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        //判断是否含有该参数
                        if (jsonObject.getString("text") != null && jsonObject.getString("text").indexOf(entry.getKey()) >= 0) {
                            //正则表达式取数字
                            Matcher m2 = NUMBER_PATTERN.matcher(entry.getKey());
                            String all = m2.replaceAll("");
                            numberP = Integer.parseInt(all);
                            text.put("p" + numberP, URLEncoder.encode(entry.getValue(), "UTF-8"));
                        }
                    }
                    excel2JsonDto.setTxt(text);

                    break;
                }
            }
            list.add(excel2JsonDto);
        }
        excel2JsonParam.setFminfos(list);
        return excel2JsonParam;
    }

    /**
     * 用于卡片和富媒体上传资源
     *
     * @param src /web/文件路径,ftype1:HTML 2:图片 3:视频 4：音频  5：zip,
     * @param ftype 类型
     * @return
     */
    public static String getAssetServerUrl(String src, String ftype) {
        //例E:/example/web/
        String proPath = new TxtFileUtil().getWebRoot();
        String tempSrc = src.substring(1, src.length());
        String name = tempSrc.substring(0, tempSrc.indexOf("/"));
        //文件路径
        String proName = "/" + name + "/";
        src = src.replace(proName, "");
        File file = new File(proPath + src);
        //去除文件名
        src = src.substring(0, src.lastIndexOf("/"));
        try {
            src = String2FileUtil.uploadFile(file, src, UploadFileConfig.UPLOAD_OPERA_TYPE_TEMPLATE, ftype);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "卡片和富媒体上传资源异常");
            return null;
        }
        return src;
    }

    /**
     * 将富媒体的content转为HTML字符串
     *
     * @param tempData
     * @return
     * @throws
     */
    public static String richMediaParseToHtml(TempData tempData) {
        List<SubTempData> tempArr = tempData.getTempArr();
        StringBuffer html = new StringBuffer();
        html.append("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\"/><meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"><meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\"/></head><body>");
        html.append("<div style=\"width: 322px;height: 613px;overflow: hidden;background: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAUIAAAJlCAYAAACv7PuPAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyZpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMDY3IDc5LjE1Nzc0NywgMjAxNS8wMy8zMC0yMzo0MDo0MiAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTUgKFdpbmRvd3MpIiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOkREQkJBNURCOTZEMDExRThCNjRDODg3MDQ3RkMxNTZDIiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOkREQkJBNURDOTZEMDExRThCNjRDODg3MDQ3RkMxNTZDIj4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6REQ5QUQ2Rjk5NkQwMTFFOEI2NEM4ODcwNDdGQzE1NkMiIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6REQ5QUQ2RkE5NkQwMTFFOEI2NEM4ODcwNDdGQzE1NkMiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz51Z9H4AAAX2klEQVR42uzdCZBcdZ3A8f/MZCYJR7hRYFFAOYtLEZBSbkRYWHa5bwQ5tlwREShEC3dZClFcCwICsoorcpTgEkBAUeQUlVtQYQmgxgQJSSAXCSQzmUn2/3uZFzud1z2ZkGAy8/lU/ZnQ093T/TrvO/93dKdl9OjRaRlZM49d8vhIHtvksXEea+UxIo/V8mhNAIuancfkPMbnMSaP5/J4Oo9f5zF9WfzAlqUcwo3yOCqPQ/LYQeyApagnj8fzuDWPm/KYtDyFsCWP/fM4M4+9xQ94F3Tn8fM8RuZx3zu9syH1F2y++eaLfeMXX3xx3/zl4t7ZH8C7Jdp1QO94LI/zcrseXsxu9R3Cxbyjf8hfLsvjMK8H8Hf20TwezF2KzeVzcxBfe8czwlJnZ2eaN2/eIpePHTv22Pzl23ms2tedt7W1pWHDhqWhQ4em9vb2NGTIkNTa2loMgCpz584txpw5c1J3d3fRotmzZ6eenp5mN4tddMfFDDEH8dPvf//776jqUXSoXyGsj2AO4PDe7fHT+orfyiuvXIyOjg6vKtAv5WQpJk5h1VXnz7m6urrSzJkz01tvvVWEsoE18rgt9+qq/PXsHMSuRk3r96ZxvtO185c70/zTYarvKD/oESNGpFVWWSW1tLR4NYGlKiZWa665ZlpjjTXSjBkz0ptvvtlolhgBOj2PzXK7Ds0xnNlnfBcjguvnL79sFMGI3uqrr57WX3/9otwiCCxL0ZiYdG2wwQZptdVWa9acOJj7QO9EbslDmO9gnTT/0PSWVd+P7e311luvrwcDsEyCGJOw9773vQ33/WU75nF3btnKSxTCfMOV8pe7GkVwpZVWKiLY5AEAvCubzNGiaFIDO+dx55gxYzqWZEZ4de8dLCI2gddZZx2zQGC5mR1Gk+IYRQN75XFJo29WHiyJw8/5y6caRTB2WPZXHLGJAyrxgOOwuIjCshXrXJy6FuIUlOVtnYtT696JWbNmLfKc1lprreKyOJhS4fO5bXHS9SKn1lS9xW7DPP4vj1WqNoejuvULO06Z6etUmXgh4kWJ84LK25XKIz9xWXm+Ye2TjMuHDx++0AII5WVxn+LKQA5auV7Ui/PrGm7utbYWp5rEUdZG4ejPY4j1rTy3r3bdLB9bPJaqHlQ9xojgxIkT0+TJk4v9fFVHf+NnxJg+fXpx6kz8Oe4/WrLpppsWE7JGz2nSpEkLOlFnah5b5TGhrxDGG5oPrb+wPDBS/0Pj8qjvLbfcUhzOjvMHI3hvv/128eTiQR9yyCHFA//xj3+cXnnllXT66acXTyjOCYoXKxZc+cLFAoz7i69xnfIFmDZtWvrVr36VPv7xjxcLLsSCfOaZZ9J+++33jl9oWB4DGH+fy5nTnXfemV544YUiRnGAcp999klbbrnlIn/3aycOcZu77rorffe7331H60j5GKZOnVpMiMp1MwIYDYjwxNZi+XPvvvvu9Nprr6W99947bbLJJgvFMPrw17/+NV111VXFOh+tqDrWEPcf5wuuvfbaxc8vl0fEPcJ4/vnnF52I+65/TnG7+PnlxKvOj/I4slkIP5HHvVVljiMzVbO+WBDxRH72s58VP/TVV19NTz/9dPrsZz+bJkyYUNT8gAMOKGJ34oknpo022qg41SauGy9m3P6YY44p6h4/5+KLL04vvfRSuu666xb8jAjg9773vbThhhsWQfziF79YLJRvfOMbRXhjIZ177rnFwmzwxGGF3O8V0bj11luLicZ2221XrDMxuYh1Mdbd+Pv+la98pZiklH/3Y52KdWb77bcv1sEDDzywWB/L9ayPd2gstMVWhm3UqFHphhtuSNdcc03RgjI+8fguu+yyYpJz//33F/d93nnnFbGO01t+85vfpIMPPjiddNJJC2IY6+ujjz6aHnvssTR+/PiiHfVbmuWWXoQ3WrDDDgt/nMEXvvCFtOuuuxaTrEbvgotYxvNvcCL1rpGWRvsIL6q6RZyz02jTN55wPLEjjjhiwWUnn3xyMTs8/PDDF1z2zW9+M33uc59L+++/f/HbKQIWL0z5gGOhxov34IMPFk8w/OEPfyhe+D//+c/poIMOKhbo17/+9fSTn/ykuP4uu+ySjj766HTOOeek2267LR177LFCyIARkfntb3+bbr755mJdinUqtqJiXYy/55/85CfTE088ka688spivYgVPkIUIYzrXXTRRWmrrbZK3/rWt9J3vvOdYj39zGc+02cIa2eUsU6OGzeuuI+nnnqq2AdXXifuL9bXRx55JO24447FZOeKK64ognf11VcX14tN35EjRxZbeXGf5foZ6388jjFjxhTPq2qzP74/ZcqUYjJV7l4L8fwjgPG8d9555yK4VZvfcb2Ypcb9V7ggj32qQhgX7lR/7XhyEaNmv7ViGhoPJBZMTHnXXXfd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GN2e2s0MgcEQwvoY/uiwznTEJ3rSCZe3p+ueby9mhu1tYggsv5baCdVlDIcPbUm3HNyZhrcNTSdd2Z66/i2l07bpTj2dLWIIDOwQLohhZ3zgQku67qDO1Jpj+K95ZthyRkqnbtudZs92HBkY4CFcEMOuliKG/3NAV9pk1fa0entKc3ssbGCQhPBvMUypva0lnb/bnOIyn0cIDKoQljHsnptH7+awCALLKx/VDwihRQAIIYAQAgghgBACCCGAEAIIIYAQAgxWDd9iN2zYMEsHGNwhnDRpUprnAwSBgbDp29qaWpp84EHDEK677rqWHjCgTJ48uTqUFg0w6GeMFgEghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIISAEFoE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) no-repeat 50%;\">");
        html.append("<h5 style=\"margin-top: 38px;font-size: 14px;text-align: center;color: #606266;\">预览</h5>");
        html.append("<div style=\"width: 310px;height: 540px;margin-top: 13px;margin-left: 7px;overflow-y: auto;overflow-x: hidden;\">");
        html.append("<div style=\"padding-left: 12px;position: relative;font-size: 12px;color: #8c8c8c\">");
        html.append("<p style=\"margin:0;padding: 0;font-weight: 400;\">");
        //设置日期格式
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        html.append(df.format(new Date()));
        html.append("</p></div>");
        html.append("<div style=\"width: 238px;padding: 8px;margin-top: 8px;margin-left: 38px;border: 1px solid #d6d6d6;border-radius: 16px;line-height: 1.3;min-height: 250px;\">");

        html.append("<div data-v-56a1323d=\"\" class=\"media-cont\" params=\"\">");
        if (tempArr != null && tempArr.size() > 0) {
            for (SubTempData subTempData : tempArr) {
                //只转换富媒体,11表示富媒体
                if ("11".equals(subTempData.getTmpType().toString()) && subTempData.getContent() != null) {
                    String content = subTempData.getContent().toString();
                    //因为富媒体是多帧,所以是JSONARRAY
                    JSONArray array = JSONObject.parseArray(content);
                    if (array != null && array.size() > 0) {
                        //将每一帧变为HTML
                        for (int i = 0; i < array.size(); i++) {
                            if (array.get(i) != null) {
                                html.append("<div>");
                                JSONObject jsonObject = JSONObject.parseObject(array.get(i).toString());
                                //只有text和char,image类型下有合并,并且只能text+image,text+chart
                                if ("text".equals(jsonObject.getString("type"))) {
                                    html.append("<div style=\"margin-top: 4px;line-height: 1.3;color: #606266;font-size: 14px;\">");
                                    //获取文本
                                    String text = jsonObject.getString("text");
                                    if (text != null) {
                                        html.append("<p>" + text + "</p>");
                                    }
                                    //获取图片
                                    String image = jsonObject.getString("image");
                                    if (image != null) {
                                        String src = JSONObject.parseObject(image).getString("src");
                                        if (!StringUtils.IsNullOrEmpty(src)) {
                                            src = getAssetServerUrl(src, UploadFileConfig.UPLOAD_FILE_TYPE_IMG);
                                            html.append("<div style=\"position: relative;transform-origin: left top 0px; transform: scale(0.793333); background: url('" + src + "') 0% 0% / 100% 100% no-repeat; height: 200px; width: 300px; overflow: hidden;\"></div>");
                                        }
                                    }
                                    //获取图表
                                    String chart = jsonObject.getString("chart");
                                    if (chart != null) {
                                        String src = JSONObject.parseObject(chart).getString("pictureUrl");
                                        if (!StringUtils.IsNullOrEmpty(src)) {
                                            src = getAssetServerUrl(src, UploadFileConfig.UPLOAD_FILE_TYPE_IMG);
                                            html.append("<div style=\"position: relative;transform-origin: left top 0px; transform: scale(0.793333); background: url('" + src + "') 0% 0% / 100% 100% no-repeat; height: 200px; width: 300px; overflow: hidden;\"></div>");
                                        }
                                    }
                                    html.append("</div>");
                                } else if ("video".equals(jsonObject.getString("type"))) {
                                    //获取视频源
                                    String src = jsonObject.getString("src");
                                    if (!StringUtils.IsNullOrEmpty(src)) {
                                        src = getAssetServerUrl(src, UploadFileConfig.UPLOAD_FILE_TYPE_VIDEO);
                                        html.append("<video data-v-56a1323d=\"\"src=\"" + src + "\"controls=\"controls\"style=\"margin-top: 4px;line-height: 1.3;color: #606266;font-size: 14px;width:238px;\"></video>");
                                    }
                                } else if ("audio".equals(jsonObject.getString("type"))) {
                                    //获取音频源
                                    String src = jsonObject.getString("src");
                                    if (!StringUtils.IsNullOrEmpty(src)) {
                                        src = getAssetServerUrl(src, UploadFileConfig.UPLOAD_FILE_TYPE_AUDIO);
                                        html.append("<audio data-v-56a1323d=\"\"src=\"" + src + "\"controls=\"controls\"style=\"margin-top: 4px;line-height: 1.3;color: #606266;font-size: 14px;width:238px;\"></audio>");
                                    }
                                } else if ("chart".equals(jsonObject.getString("type"))) {
                                    html.append("<div style=\"margin-top: 4px;line-height: 1.3;color: #606266;font-size: 14px;\">");
                                    //获取图表
                                    String src = jsonObject.getString("pictureUrl");
                                    if (!StringUtils.IsNullOrEmpty(src)) {
                                        src = getAssetServerUrl(src, UploadFileConfig.UPLOAD_FILE_TYPE_IMG);
                                        html.append("<div style=\"position: relative;transform-origin: left top 0px; transform: scale(0.793333); background: url('" + src + "') 0% 0% / 100% 100% no-repeat; height: 200px; width: 300px; overflow: hidden;\"></div>");
                                    }
                                    //获取文本
                                    String text = jsonObject.getString("text");
                                    if (text != null) {
                                        html.append("<p>" + text + "</p>");
                                    }
                                    html.append("</div>");
                                } else if ("image".equals(jsonObject.getString("type"))) {
                                    html.append("<div style=\"margin-top: 4px;line-height: 1.3;color: #606266;font-size: 14px;\">");
                                    //获取图片源
                                    String src = jsonObject.getString("src");
                                    if (!StringUtils.IsNullOrEmpty(src)) {
                                        src = getAssetServerUrl(src, UploadFileConfig.UPLOAD_FILE_TYPE_IMG);
                                        html.append("<div style=\"position: relative;transform-origin: left top 0px; transform: scale(0.793333); background: url('" + src + "') 0% 0% / 100% 100% no-repeat; height: 200px; width: 300px; overflow: hidden;\"></div>");
                                    }
                                    //获取文本
                                    String text = jsonObject.getString("text");
                                    if (text != null) {
                                        html.append("<p>" + text + "</p>");
                                    }
                                    html.append("</div>");
                                }
                                html.append("</div>");
                            }
                        }
                    }
                }
            }

        }
        html.append("</div></div></div></body></html>");
        return html.toString();
    }

    /**
     * 将content转为html
     *
     * @param tempContent
     * @return
     */
    public static String parseToHtml(TempContent tempContent) {
        //用于返回
        StringBuffer html = new StringBuffer();
        //设置html头
        html.append("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\"/><meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"><meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\"/></head><body>");
        //拼接固定头
        html.append("<div style=\"width: 322px;height: 613px;overflow: hidden;background: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAUIAAAJlCAYAAACv7PuPAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyZpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMDY3IDc5LjE1Nzc0NywgMjAxNS8wMy8zMC0yMzo0MDo0MiAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTUgKFdpbmRvd3MpIiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOkREQkJBNURCOTZEMDExRThCNjRDODg3MDQ3RkMxNTZDIiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOkREQkJBNURDOTZEMDExRThCNjRDODg3MDQ3RkMxNTZDIj4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6REQ5QUQ2Rjk5NkQwMTFFOEI2NEM4ODcwNDdGQzE1NkMiIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6REQ5QUQ2RkE5NkQwMTFFOEI2NEM4ODcwNDdGQzE1NkMiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz51Z9H4AAAX2klEQVR42uzdCZBcdZ3A8f/MZCYJR7hRYFFAOYtLEZBSbkRYWHa5bwQ5tlwREShEC3dZClFcCwICsoorcpTgEkBAUeQUlVtQYQmgxgQJSSAXCSQzmUn2/3uZFzud1z2ZkGAy8/lU/ZnQ093T/TrvO/93dKdl9OjRaRlZM49d8vhIHtvksXEea+UxIo/V8mhNAIuancfkPMbnMSaP5/J4Oo9f5zF9WfzAlqUcwo3yOCqPQ/LYQeyApagnj8fzuDWPm/KYtDyFsCWP/fM4M4+9xQ94F3Tn8fM8RuZx3zu9syH1F2y++eaLfeMXX3xx3/zl4t7ZH8C7Jdp1QO94LI/zcrseXsxu9R3Cxbyjf8hfLsvjMK8H8Hf20TwezF2KzeVzcxBfe8czwlJnZ2eaN2/eIpePHTv22Pzl23ms2tedt7W1pWHDhqWhQ4em9vb2NGTIkNTa2loMgCpz584txpw5c1J3d3fRotmzZ6eenp5mN4tddMfFDDEH8dPvf//776jqUXSoXyGsj2AO4PDe7fHT+orfyiuvXIyOjg6vKtAv5WQpJk5h1VXnz7m6urrSzJkz01tvvVWEsoE18rgt9+qq/PXsHMSuRk3r96ZxvtO185c70/zTYarvKD/oESNGpFVWWSW1tLR4NYGlKiZWa665ZlpjjTXSjBkz0ptvvtlolhgBOj2PzXK7Ds0xnNlnfBcjguvnL79sFMGI3uqrr57WX3/9otwiCCxL0ZiYdG2wwQZptdVWa9acOJj7QO9EbslDmO9gnTT/0PSWVd+P7e311luvrwcDsEyCGJOw9773vQ33/WU75nF3btnKSxTCfMOV8pe7GkVwpZVWKiLY5AEAvCubzNGiaFIDO+dx55gxYzqWZEZ4de8dLCI2gddZZx2zQGC5mR1Gk+IYRQN75XFJo29WHiyJw8/5y6caRTB2WPZXHLGJAyrxgOOwuIjCshXrXJy6FuIUlOVtnYtT696JWbNmLfKc1lprreKyOJhS4fO5bXHS9SKn1lS9xW7DPP4vj1WqNoejuvULO06Z6etUmXgh4kWJ84LK25XKIz9xWXm+Ye2TjMuHDx++0AII5WVxn+LKQA5auV7Ui/PrGm7utbYWp5rEUdZG4ejPY4j1rTy3r3bdLB9bPJaqHlQ9xojgxIkT0+TJk4v9fFVHf+NnxJg+fXpx6kz8Oe4/WrLpppsWE7JGz2nSpEkLOlFnah5b5TGhrxDGG5oPrb+wPDBS/0Pj8qjvLbfcUhzOjvMHI3hvv/128eTiQR9yyCHFA//xj3+cXnnllXT66acXTyjOCYoXKxZc+cLFAoz7i69xnfIFmDZtWvrVr36VPv7xjxcLLsSCfOaZZ9J+++33jl9oWB4DGH+fy5nTnXfemV544YUiRnGAcp999klbbrnlIn/3aycOcZu77rorffe7331H60j5GKZOnVpMiMp1MwIYDYjwxNZi+XPvvvvu9Nprr6W99947bbLJJgvFMPrw17/+NV111VXFOh+tqDrWEPcf5wuuvfbaxc8vl0fEPcJ4/vnnF52I+65/TnG7+PnlxKvOj/I4slkIP5HHvVVljiMzVbO+WBDxRH72s58VP/TVV19NTz/9dPrsZz+bJkyYUNT8gAMOKGJ34oknpo022qg41SauGy9m3P6YY44p6h4/5+KLL04vvfRSuu666xb8jAjg9773vbThhhsWQfziF79YLJRvfOMbRXhjIZ177rnFwmzwxGGF3O8V0bj11luLicZ2221XrDMxuYh1Mdbd+Pv+la98pZiklH/3Y52KdWb77bcv1sEDDzywWB/L9ayPd2gstMVWhm3UqFHphhtuSNdcc03RgjI+8fguu+yyYpJz//33F/d93nnnFbGO01t+85vfpIMPPjiddNJJC2IY6+ujjz6aHnvssTR+/PiiHfVbmuWWXoQ3WrDDDgt/nMEXvvCFtOuuuxaTrEbvgotYxvNvcCL1rpGWRvsIL6q6RZyz02jTN55wPLEjjjhiwWUnn3xyMTs8/PDDF1z2zW9+M33uc59L+++/f/HbKQIWL0z5gGOhxov34IMPFk8w/OEPfyhe+D//+c/poIMOKhbo17/+9fSTn/ykuP4uu+ySjj766HTOOeek2267LR177LFCyIARkfntb3+bbr755mJdinUqtqJiXYy/55/85CfTE088ka688spivYgVPkIUIYzrXXTRRWmrrbZK3/rWt9J3vvOdYj39zGc+02cIa2eUsU6OGzeuuI+nnnqq2AdXXifuL9bXRx55JO24447FZOeKK64ognf11VcX14tN35EjRxZbeXGf5foZ6388jjFjxhTPq2qzP74/ZcqUYjJV7l4L8fwjgPG8d9555yK4VZvfcb2Ypcb9V7ggj32qQhgX7lR/7XhyEaNmv7ViGhoPJBZMTHnXXXfd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GN2e2s0MgcEQwvoY/uiwznTEJ3rSCZe3p+ueby9mhu1tYggsv5baCdVlDIcPbUm3HNyZhrcNTSdd2Z66/i2l07bpTj2dLWIIDOwQLohhZ3zgQku67qDO1Jpj+K95ZthyRkqnbtudZs92HBkY4CFcEMOuliKG/3NAV9pk1fa0entKc3ssbGCQhPBvMUypva0lnb/bnOIyn0cIDKoQljHsnptH7+awCALLKx/VDwihRQAIIYAQAgghgBACCCGAEAIIIYAQAgxWDd9iN2zYMEsHGNwhnDRpUprnAwSBgbDp29qaWpp84EHDEK677rqWHjCgTJ48uTqUFg0w6GeMFgEghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIIQAQggghABCCCCEAEIIIISAEFoE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) no-repeat 50%;\">");
        html.append("<h5 style=\"margin-top: 38px;font-size: 14px;text-align: center;color: #606266;\">预览</h5>");
        html.append("<div style=\"width: 310px;height: 540px;margin-top: 13px;margin-left: 7px;overflow-y: auto;overflow-x: hidden;\">");
        html.append("<div style=\"padding-left: 12px;position: relative;font-size: 12px;color: #8c8c8c\">");
        html.append("<p style=\"margin:0;padding: 0;font-weight: 400;\">");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        html.append(df.format(new Date()));
        html.append("</p></div>");
        html.append("<div  style=\"position: relative;width: 286px;margin: 12px auto;\">");
        //拼接外层div
        html.append("<div style=\"transform-origin: left top 0px; transform: scale(1.05147); overflow: hidden;");
        //拼接bgsrc
        if (!StringUtils.IsNullOrEmpty(tempContent.getBgSrc())) {
            html.append(" background: url(&quot;");
            html.append(getAssetServerUrl(tempContent.getBgSrc(), UploadFileConfig.UPLOAD_FILE_TYPE_IMG));
            html.append("&quot;) 0% 0% / 100% 100% no-repeat;");
        }
        //拼接height
        if (tempContent.getH() != null) {
            html.append("height:");
            html.append(tempContent.getH());
            html.append(";");
        }
        //拼接width
        if (tempContent.getW() != null) {
            html.append("width:");
            html.append(tempContent.getW());
            html.append(";");
        }
        html.append("\">");

        //拼接div里面的内容
        if (tempContent.getElements() != null) {
            TempDataElement tempDataElement = tempContent.getElements();
            //拼接图片
            if (tempDataElement.getImages() != null) {
                //遍历集合
                for (TempElement tempElement : tempDataElement.getImages()) {
                    setImage(tempElement, html);
                }
            }
            //拼接二维码
            if (tempDataElement.getQrcodes() != null) {
                //遍历集合
                for (TempElement tempElement : tempDataElement.getQrcodes()) {
                    setImage(tempElement, html);
                }
            }
            //拼接音频
            if (tempDataElement.getAudios() != null) {
                //遍历集合
                for (TempElement tempElement : tempDataElement.getAudios()) {
                    //音频外层audio
                    html.append("<div style=\"margin:0px;position: absolute;overflow: hidden;");
                    setPosition(tempElement, html);
                    html.append("\">");
                    if (tempElement.getSrc() != null) {
                        //设置src
                        html.append("<audio src=\"" + getAssetServerUrl(tempElement.getSrc(), UploadFileConfig.UPLOAD_FILE_TYPE_AUDIO) + "\"></audio>");
                        //设置图标
                        html.append("<img style=\"width:30px;height:30px;vertical-align: middle;\" src=\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACIAAAAiCAYAAAA6RwvCAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyZpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMDY3IDc5LjE1Nzc0NywgMjAxNS8wMy8zMC0yMzo0MDo0MiAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTUgKFdpbmRvd3MpIiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOjEyMjc2REY2QUIzODExRTg4MEEzRkNFQzZERTY0MDE4IiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOjEyMjc2REY3QUIzODExRTg4MEEzRkNFQzZERTY0MDE4Ij4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6MTIyNzZERjRBQjM4MTFFODgwQTNGQ0VDNkRFNjQwMTgiIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6MTIyNzZERjVBQjM4MTFFODgwQTNGQ0VDNkRFNjQwMTgiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz7xmVKLAAADwUlEQVR42rSYaUhUURTHn2+kMDLKtI0ga/oQLbTTh3ZonSBoIawMQoUs0jCQiLJloC8tlCOWYUWQVrRRUdNChRJ+iDKKDKzQoi9lYbZYVKT2P/B/cp1m7rw72oEf9868+8457y7nnPfiph5rswxkAPCBWWAUGAL68FoTeAtqQAW4Ad65VRzvctxMkA8WAE+EMX3JeLAGtICbYB+ojGbAjnLdC4J8wkWgFdwCm+ncQNCDSH8GyKMDrbyngjq8OkNxmqVZDUpAT/AVBEAR+OByFvuBHJALeoFmkA3KTWZkNyijE5fACFBg4ITFsQW89yJ1lVG3K0d2gR1cY5nmZSabLozIvcs5My3U7Y/miCzHTt6wChyyuk5kWdOou4C2wjoyjHvC4kyci8GYTH+q5voFzoxFW95wjhRR0Xn2Y5EHoC70aUPkMDhLW4FQR6YzUH1WPI5FulHnCTBFM24Tbflou92RfLaF4H0nHFnKoy4OnWQb6UQ5+2+L40h/sBD8BsWGT3+cCp1o+4yxw+KxzdTcX8KNO198sOmEhPrb4KOBIxLKMzjN+5X/T4FH7OuWuQFU0bbPZgKzGLpNRIzdVQyOZb9N2YQyK+M0Oq44ucxmFhWpNnSkhcntB5d4vXLtGnONpTxoOKliO1oUDOWPVy6M7wHf2TpR8zL7c5RxUhLUsz9So6+Obao4ksgfX1w4ksdMu1H57zHbwSFjndPXW6OviW2izTW1lFYnB8BPBqX2DK45VdGkTQ1ozS48d0RyRALYGnJ6LFZnodWcSKNGn1PdfRNHXvPH8BgC2CCwhP17IbWIs1S1UQovkTfiyHP+mGjohIf1RQJPyBHl2mIlauvKRMdmjc1STmSuoSPTwGz2pS59quyZdey/AE80OuY5zsaznvzDCJsEPrl0pJqZWk7HNuV/iS2T2A9o7k+iTbEdtJmAgtzlGwxmRDb5CqXyEhmjlBC1zEWRRAJgdxbaDc46OrlCqvOUGDOv6LrKQlkS6FrwK8LYFNoS2auWAffBdR6nwk6UATY3bgaLpEhykEsTpO0OFVoup3ulstlMRByYwDBQrhmXyQquWSkZOjhSrzhQrMQHE2lU4lI4EZ1H2c9W8tE/VfxpVvEeFs9ZXVjFZ1Gnh+825dHea/x0Ro52KTgDkjvhQDKLpVLq9PPdydWbnl9ZxzSWCNu5wdxKEu95CdKpK50PafTu6+SCAKtti8dRdvod8FByhFJepvCdZjJrEx/jhMVPFDlK/WHsiMlniXAVnOvPEm6/j1SS//ah5q8AAwBVP9nXgMrQNAAAAABJRU5ErkJggg==\"></img>");
                        html.append("<span style=\"color: #606266;font-size: 14px;margin-top: 10px\">" + tempElement.getFileName() + "</span>");
                    }
                    html.append("</div>");
                }
            }
            //拼接视频
            if (tempDataElement.getVideos() != null) {
                //遍历集合
                for (TempElement tempElement : tempDataElement.getVideos()) {
                    //视频外层video
                    html.append("<video controls=\"controls\" style=\"position: absolute;overflow: hidden;");
                    setPosition(tempElement, html);
                    html.append("\"");
                    if (tempElement.getSrc() != null) {
                        //设置src
                        html.append("src=\"" + getAssetServerUrl(tempElement.getSrc(), UploadFileConfig.UPLOAD_FILE_TYPE_VIDEO) + "\"");
                    }
                    html.append(">");
                    //视频外层video
                    html.append("</video>");
                }
            }
            //拼接文本
            if (tempDataElement.getTexts() != null) {
                //遍历集合
                for (TempElement tempElement : tempDataElement.getTexts()) {
                    //视频外层audio
                    html.append("<p style=\"margin:0px;position: absolute;overflow: hidden;");
                    if ("title".equals(tempElement.getType())) {
                        html.append("font-size:14px;");
                    }
                    setPosition(tempElement, html);
                    setStyle(tempElement, html);
                    html.append("\">");
                    if (tempElement.getText() != null) {
                        //添加文本
                        html.append(tempElement.getText());
                    }
                    //文本
                    html.append("</p>");
                }
            }
            //拼接按钮
            if (tempDataElement.getButtons() != null) {
                //遍历集合
                for (TempElement tempElement : tempDataElement.getButtons()) {
                    //按钮外层button
                    html.append("<button type=\"button\" style=\"position: absolute;overflow: hidden;color: #2e95ff;background: 0 0;padding-left: 0;padding-right: 0;border-color: transparent;display: inline-block;line-height: 1;white-space: nowrap;-webkit-appearance: none;outline: 0;margin: 0;transition: .1s;font-weight: 500;box-sizing: border-box;cursor: pointer;padding: 9px 15px;");
                    setPosition(tempElement, html);
                    setStyle(tempElement, html);
                    html.append("\">");
                    if (tempElement.getText() != null) {
                        //添加文本
                        html.append(tempElement.getText());
                    }
                    //文本
                    html.append("</button>");
                }
            }
        }
        //拼接外层div尾
        html.append("</div>");
        //拼接固定尾
        html.append("</div></div></div></body></html>");
        return html.toString();
    }

    public static void setImage(TempElement tempElement, StringBuffer html) {
        //图片外层div
        html.append("<div style=\"position: absolute;overflow: hidden;margin:0px;");
        setPosition(tempElement, html);
        if (tempElement.getStyle() != null && tempElement.getStyle().getBorderRadius() != null) {
            //设置圆角
            html.append("border-radius:" + tempElement.getStyle().getBorderRadius() + ";");
        }
        //封闭div
        html.append("\">");
        //设置img
        if (!StringUtils.IsNullOrEmpty(tempElement.getSrc())) {
            html.append("<img src=\"");
            html.append(getAssetServerUrl(tempElement.getSrc(), UploadFileConfig.UPLOAD_FILE_TYPE_IMG) + "\" ");
            html.append("style=\"");
            if (tempElement.getW() != null) {
                //设置宽度
                html.append("width:" + tempElement.getW() + ";");
            }
            if (tempElement.getH() != null) {
                //设置高度
                html.append("height:" + tempElement.getH() + ";");
            }
            html.append("\">");
        }
        //图片外层div
        html.append("</div>");
    }

    public static void setStyle(TempElement tempElement, StringBuffer html) {
        if (tempElement.getStyle().getTextAlign() != null) {
            //设置Align
            html.append("text-align:" + tempElement.getStyle().getTextAlign() + ";");
        }
        if (tempElement.getStyle().getFontSize() != null) {
            //设置FontSize
            html.append("font-size:" + tempElement.getStyle().getFontSize() + ";");
        }
        if (tempElement.getStyle().getFontFamily() != null) {
            //设置Fontfamily
            html.append("font-family:" + tempElement.getStyle().getFontFamily() + ";");
        }
        if (tempElement.getStyle().getFontWeight() != null) {
            //设置Fontweight
            html.append("font-weight:" + tempElement.getStyle().getFontWeight() + ";");
        }
        if (tempElement.getStyle().getFontStyle() != null) {
            //设置Fontstyle
            html.append("font-style:" + tempElement.getStyle().getFontStyle() + ";");
        }
        if (tempElement.getStyle().getColor() != null) {
            //设置color
            html.append("color:" + tempElement.getStyle().getColor() + ";");
        }
        if (tempElement.getStyle() != null && tempElement.getStyle().getBorderRadius() != null) {
            //设置圆角
            html.append("border-radius:" + tempElement.getStyle().getBorderRadius() + ";");
        }
    }

    public static void setPosition(TempElement tempElement, StringBuffer html) {
        if (tempElement.getX() != null) {
            //设置横坐标
            html.append("left:" + tempElement.getX() + ";");
        }
        if (tempElement.getY() != null) {
            //设置纵坐标
            html.append("top:" + tempElement.getY() + ";");
        }
        if (tempElement.getW() != null) {
            //设置宽度
            html.append("width:" + tempElement.getW() + ";");
        }
        if (tempElement.getH() != null) {
            //设置高度
            html.append("height:" + tempElement.getH() + ";");
        }
    }

    /**
     * 将JSON转为富信模板类
     *
     * @param rmsData
     * @return
     */
    @SuppressWarnings("finally")
    public static TempRmsData parseToRms(String rmsData) {
        TempRmsData tempRmsData = null;
        try {
            JSONObject rmsObject = JSONObject.parseObject(rmsData);
            tempRmsData = JSONObject.toJavaObject(rmsObject, TempRmsData.class);
        } catch (Exception e) {
            // TODO: handle exception
            EmpExecutionContext.error(e, "json转为富信模板类异常");
        }
        return tempRmsData;
    }

    /**
     * 将前端json转为TempData对象集合
     *
     * @param TagData
     * @return
     */
    @SuppressWarnings("finally")
    public static List<TempData> parseToData(String TagData) {
        List<TempData> tempDataArr = new ArrayList<TempData>();
       try {
            //前端json转为对象
            JSONArray contentArray = JSONArray.parseArray(TagData);
            for (int i = 0; i < contentArray.size(); i++) {
                JSONObject contentObject = contentArray.getJSONObject(i);
                //判断是否是卡片类型
                if ("12".equals(contentObject.get("type").toString())) {
                    JSONObject elementsJson = JSONObject.parseObject(contentObject.get("content").toString());
                    contentObject.put("content", elementsJson);
                    TempCardData tempData = JSONObject.toJavaObject(contentObject, TempCardData.class);
                    tempDataArr.add(tempData);
                } else {
                    TempNonCardData tempNonCardData = JSONObject.toJavaObject(contentObject, TempNonCardData.class);
                    tempDataArr.add(tempNonCardData);
                }
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "将前端json转为TempData对象集合异常");
        }
        return tempDataArr;
    }

    /**
     * 富文本转为数据类
     *
     * @param subject
     * @param map
     * @return
     */
    public static TemplateTotalParam richTextToParam(String subject, HashMap<String, String> map) {
        TemplateTotalParam templateTotalParam = new TemplateTotalParam();
        try {
            //判断类型
            List<TemplateParam> templateParamList = new ArrayList<TemplateParam>();
            List<HashMap<String, String>> args = new ArrayList<HashMap<String, String>>();
            Iterator<Map.Entry<String, String>> entries = map.entrySet().iterator();
            HashMap paramMap = null;
            while (entries.hasNext()) {
                paramMap = new HashMap<String, String>();
                Map.Entry<String, String> entry = entries.next();
                paramMap.put(entry.getKey(), entry.getValue());
                args.add(paramMap);
            }
            TemplateParam templateParam = new TemplateParam();
            templateParam.setTag("richtext");
            templateParam.setArgs(args);
            templateParamList.add(templateParam);
            templateTotalParam.setSubject(subject);
            templateTotalParam.setParam(templateParamList);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "发现异常");
        }
        return templateTotalParam;
    }


    /***
     * 卡片转为数据类
     * @param tempData
     * @param map
     * @return
     */
    @SuppressWarnings("finally")
    public static TemplateTotalParam dataToParam(String subject, String tempData, HashMap<String, String> map) {
        List<TemplateParam> templateParam = null;
        TemplateTotalParam templateTotalParam = new TemplateTotalParam();
        try {
            //判断类型
            JSONObject jsonObject = JSONObject.parseObject(tempData);
            templateParam = cardDataToParam(jsonObject, map);
            templateTotalParam.setSubject(subject);
            templateTotalParam.setParam(templateParam);
        } catch (Exception e) {
            // TODO: handle exception
            EmpExecutionContext.error(e, "发现异常");
        }
        return templateTotalParam;
    }


    /**
     * 转为终端需要的卡片参数
     *
     * @param jsonObject
     * @param map
     * @return
     */
    public static List<TemplateParam> cardDataToParam(JSONObject jsonObject, HashMap<String, String> map) {
        try {

            //返回的list
            List<TemplateParam> list = new ArrayList<TemplateParam>();

            TempContent tempContent = JSONObject.toJavaObject(jsonObject, TempContent.class);

            StringBuffer sb = new StringBuffer();

            //因为参数只能是文本,所以遍历文本组件即可
            if (tempContent.getElements().getTexts() != null) {
                for (TempElement tempElement : tempContent.getElements().getTexts()) {
                    TemplateParam templateParam = new TemplateParam();
                    templateParam.setTag(tempElement.getTag());
                    //遍历key
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        if (tempElement.getText().indexOf(entry.getKey()) >= 0) {
                            //替换
                            String tempElementValue = "";
                            if (tempElement.getText() != null) {
                                //将input中的值替换
                                tempElementValue = getText(tempElement.getText()).replace(entry.getKey(), entry.getValue());
                            }
                            if (sb.length() == 0) {
                                sb.append(tempElementValue);
                            } else {
                                String tempText = sb.toString().replace(entry.getKey(), entry.getValue());
                                sb.setLength(0);
                                sb.append(tempText);
                            }

                        }
                    }
                    if (sb.length() > 0) {
                        templateParam.setContent(sb.toString());
                        list.add(templateParam);
                        sb.setLength(0);
                    }/* else {
                        templateParam.setContent(tempElement.getText());
                        list.add(templateParam);
                    }*/
                }
            }

            //按钮也需要添加参数
            if (tempContent.getElements().getButtons() != null) {
                for (TempElement element : tempContent.getElements().getButtons()) {
                    TemplateParam templateParam = new TemplateParam();
                    templateParam.setTag(element.getTag());

                    Button button = new Button();
                    ButtonMap buttonMap = new ButtonMap();
                    App app = new App();
                    button.setAction(element.getAction());

                    if ("2".equals(element.getAction())) {
                        button.setTarget(element.getUrl());
                    }
                    if ("3".equals(element.getAction())) {
                        //添加param参数
                        for (Map.Entry<String, String> entry : map.entrySet()) {
                            if (element.getParam() != null && element.getParam().equals(entry.getKey())) {
                                button.setTarget(entry.getValue());
                                break;
                            }
                        }
                    }
                    if ("6".equals(element.getAction())) {
                        button.setTarget(element.getTel());
                    }
                    //打开地图
                    if ("5".equals(element.getAction())) {
                        buttonMap.setPkg(element.getPkg());
                        buttonMap.setLat(element.getLat());
                        buttonMap.setLon(element.getLon());
                        buttonMap.setTitle(element.getTitle());
                        buttonMap.setAddr(element.getAddr());
                        button.setMap(buttonMap);
                    }
                    //打开APP
                    if ("4".equals(element.getAction())) {
                        app.setPkg(element.getPkg());
                        button.setApp(app);
                    }
                    //打开快应用
                    if ("7".equals(element.getAction())) {
                        app.setPkg(element.getPkg());
                        app.setPath(element.getPath());
                        app.setParam(element.getParam());
                        button.setApp(app);
                    }
                    templateParam.setEvent(button);
                    list.add(templateParam);
                }
            }

            //二维码可能也需要添加参数
            if (tempContent.getElements().getQrcodes() != null) {
                for (TempElement element : tempContent.getElements().getQrcodes()) {
                    if("1".equals(element.getCodeType())){//如果二维码有参数,则遍历
                        TemplateParam templateParam = new TemplateParam();
                        templateParam.setTag(element.getTag());
                        //遍历key,因为一个组件可能含有多个参数
                        for (Map.Entry<String, String> entry : map.entrySet()) {
                            if (element.getName().indexOf(entry.getKey()) >= 0) {
                                //替换
                                String tempElementValue = "";
                                if (element.getName() != null) {
                                    tempElementValue = element.getName().replace(entry.getKey(), entry.getValue());
                                }
                                if (sb.length() == 0) {
                                    sb.append(tempElementValue);
                                } else {
                                    String tempText = sb.toString().replace(entry.getKey(), entry.getValue());
                                    sb.setLength(0);
                                    sb.append(tempText);
                                }
                            }
                        }
                        if (sb.length() > 0) {
                            templateParam.setContent(sb.toString());
                            list.add(templateParam);
                            sb.setLength(0);
                        }
                    }
                }
            }
            return list;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "发现异常");
            return null;
        }
    }

    public static String richToString(String str, Map<String, String> map) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        if (map.size() != 0) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                str = str.replace(entry.getKey(), entry.getValue().replaceAll("\"","\\\\\""));
            }
        }
        Map richMap = JSON.parseObject(str);
        String richStr = (String) richMap.get("template");
        List<Map<String, String>> richList = new ArrayList<Map<String, String>>();
        Map<String, String> contentMap = new HashMap();
        contentMap.put("tag", "richtext");
        contentMap.put("content", richStr);
        richList.add(contentMap);
        Map<String, Object> paramMap = new HashMap();
        paramMap.put("param", richList);
        Map<String, Object> outMap = new HashMap();
        outMap.put("content", JSONObject.toJSONString(paramMap));
        List<Map<String, Object>> contentList = new ArrayList<Map<String, Object>>();
        contentList.add(outMap);

        String previewContent = JSONObject.toJSONString(contentList);
        return previewContent;
    }

    /**
     * 将富媒体参数转为卡片参数
     *
     * @param param
     * @return
     */
    public static TemplateTotalParam richMediaParamToTerminal(String subject, String tempContent, List<ParamExcel> param) {//未考虑参数在图的文本里

        TemplateTotalParam templateTotalParam = new TemplateTotalParam();
        //返回的list
        List<TemplateParam> list = new ArrayList<TemplateParam>();
        //因为只有文本中才有参数,所以只需要遍历
        JSONArray array = JSONObject.parseArray(tempContent);
        JSONObject tempObject = null;
        Integer number = 0;
        //判断是否有参数
        boolean flag = false;
        //因为array有序且顺序排列
        for (int i = 0; i < array.size(); i++) {
            //判断每一帧的类型
            tempObject = array.getJSONObject(i);
            //如果是文本
            TemplateParam templateParam;
            if ("text".equals(tempObject.getString("type"))) {
                //创建终端参数对象
                templateParam = new TemplateParam();
                number += 1;
                //遍历参数集合
                for (ParamExcel paramExcel : param) {
                    //判断参数对象是否对应这一帧,order从1开始
                    if (String.valueOf(i + 1).equals(paramExcel.getOrder())) {
                        //设置tagId
                        templateParam.setTag(String.valueOf(number));
                        LinkedHashMap<String, String> map = paramExcel.getMap();
                        //取当前帧的text值
                        String text = tempObject.getString("text");
                        //将参数替换成参数值
                        for (Map.Entry<String, String> entry : map.entrySet()) {
                            if (text.indexOf(entry.getKey()) >= 0) {
                                text = text.replace(entry.getKey(), entry.getValue());
                                flag = true;
                            }
                        }
                        if (flag) {
                            //将参数独享添加进入集合
                            templateParam.setContent(text);
                            list.add(templateParam);
                            flag = false;
                        }
                        break;
                    }
                }
                //如果图片的src不为空
                if (tempObject.getString("image") != null && StringUtils.isNotEmpty(JSONObject.parseObject(tempObject.getString("image")).getString("src"))) {
                    number += 1;
                }
                if (tempObject.getString("chart") != null && StringUtils.isNotEmpty(JSONObject.parseObject(tempObject.getString("chart")).getString("pictureUrl"))) {
                    number += 1;
                }
            }

            //如果是图片
            else if ("image".equals(tempObject.getString("type"))) {
                templateParam = new TemplateParam();
                number += 1;
                //遍历参数集合
                for (ParamExcel paramExcel : param) {
                    //判断参数对象是否对应这一帧,order从1开始
                    if (String.valueOf(i + 1).equals(paramExcel.getOrder())) {
                        number += 1;
                        //设置tagId
                        templateParam.setTag(String.valueOf(number));
                        LinkedHashMap<String, String> map = paramExcel.getMap();
                        //取当前帧的text值
                        String text = tempObject.getString("text");
                        //将参数替换成参数值
                        for (Map.Entry<String, String> entry : map.entrySet()) {
                            if (text.indexOf(entry.getKey()) >= 0) {
                                text = text.replace(entry.getKey(), entry.getValue());
                                flag = true;
                            }
                        }
                        if (flag) {
                            templateParam.setContent(text);
                            //将参数独享添加进入集合
                            list.add(templateParam);
                            flag = false;
                        }
                        break;
                    }
                }
            } else {
                number += 1;
            }
        }
        templateTotalParam.setParam(list);
        templateTotalParam.setSubject(subject);
        return templateTotalParam;
    }

        /**
         * 将前端富媒体信息转为终端需要的对象
         *
         * @param tempContent
         * @return
         */
    public static MainTemplate richMediaToTerminal(String tempContent, String ottSrc,String editorWidth) {
        //终端对象
        MainTemplate mainTemplate = new MainTemplate();
        //表示彩信转卡片
        mainTemplate.setTemptype("3");
        //终端的template属性
        Template template = new  Template();
        Layout layout = new Layout();
        layout.setW(editorWidth);
        template.setLayout(layout);
        template.setType("linear");
        //tempate的child属性
        List<Children> child = new ArrayList<Children>();

        JSONArray array = JSONObject.parseArray(tempContent);
        JSONObject tempObject = null;
        Integer num = 0;
        for(int i = 0;i<array.size();i++){
            //判断每一帧的类型
            tempObject = array.getJSONObject(i);
            //如果是文本
            if("text".equals(tempObject.getString("type"))){
                //设置文本控件
                Children txtChild = new Children();
                num += 1;
                txtChild.setTag(String.valueOf(num));//设置tagId
                txtChild.setType("text");//设置类型
                Attr txtAttr = new Attr();
                txtAttr.setText(getText(tempObject.getString("text")));
                txtChild.setAttr(txtAttr);
                child.add(txtChild);

                //设置图片控件
                if(tempObject.getString("image") != null && StringUtils.isNotEmpty(JSONObject.parseObject(tempObject.getString("image")).getString("src"))){//如果图片的src不为空
                    JSONObject image = JSONObject.parseObject(tempObject.getString("image"));//获取图片的jsonObject
                    Children imgChild = new Children();
                    num += 1;
                    imgChild.setTag(String.valueOf(num));//设置tagId,因为是文本控件,所以文本一定在前面
                    imgChild.setType("image");//设置类型
                    Attr imgAttr = new Attr();
                    String ImgName = JSONObject.parseObject(tempObject.getString("image")).getString("src");//图片的src
                    if("gif".equalsIgnoreCase(ImgName.substring(ImgName.lastIndexOf(".")+1))){//图片后缀除gif之外就是jpg
                        imgAttr.setSrc(String.format("%02d", num)+".gif");//图片后缀是gif
                    }else{
                        imgAttr.setSrc(String.format("%02d", num)+".jpg");//图片后缀都是jpg
                    }
                    imgChild.setAttr(imgAttr);
                    Layout imgLay = new Layout();
                    imgLay.setH(image.getString("h"));
                    imgLay.setW(image.getString("w"));
                    imgChild.setLayout(imgLay);
                    child.add(imgChild);
                }
                //设置chart控件
                if(tempObject.getString("chart") != null && StringUtils.isNotEmpty(JSONObject.parseObject(tempObject.getString("chart")).getString("pictureUrl"))){//如果chart的pictureUrl不为空
                    JSONObject chart = JSONObject.parseObject(tempObject.getString("chart"));//获取图片的jsonObject
                    Children chartChild = new Children();
                    num += 1;
                    chartChild.setTag(String.valueOf(num));//设置tagId,因为是文本控件,所以文本一定在前面
                    chartChild.setType("image");//设置类型
                    Attr chartAttr = new Attr();
                    chartAttr.setSrc(String.format("%02d", i+2)+".jpg");//图片后缀都是jpg
                    chartChild.setAttr(chartAttr);
                    Layout chartLay = new Layout();
                    chartLay.setH(chart.getString("h"));
                    chartLay.setW(chart.getString("w"));
                    chartChild.setLayout(chartLay);
                    child.add(chartChild);
                }
            }
            //如果是图片
            if("image".equals(tempObject.getString("type"))){
                //设置图片控件
                Children imgChild = new Children();
                num += 1;
                imgChild.setTag(String.valueOf(num));//设置tagId,因为是文本控件,所以文本一定在前面
                imgChild.setType("image");//设置类型
                Attr imgAttr = new Attr();
                String ImgName = tempObject.getString("src");//图片的src
                if("gif".equalsIgnoreCase(ImgName.substring(ImgName.lastIndexOf(".")+1))){//图片后缀除gif之外就是jpg
                    imgAttr.setSrc(String.format("%02d", num)+".gif");//图片后缀是gif
                }else{
                    imgAttr.setSrc(String.format("%02d", num)+".jpg");//图片后缀都是jpg
                }
                imgChild.setAttr(imgAttr);
                Layout imgLay = new Layout();

                imgLay.setH(tempObject.getString("h"));
                imgLay.setW(tempObject.getString("w"));

                imgChild.setLayout(imgLay);
                child.add(imgChild);
                //设置文本控件
                if(StringUtils.isNotEmpty(tempObject.getString("text"))){
                    Children txtChild = new Children();
                    num += 1;
                    txtChild.setTag(String.valueOf(num));//设置tagId
                    txtChild.setType("text");//设置类型
                    Attr txtAttr = new Attr();
                    txtAttr.setText(getText(tempObject.getString("text")));
                    txtChild.setAttr(txtAttr);
                    child.add(txtChild);
                }
            }
            //如果是音频
            if("audio".equals(tempObject.getString("type"))){
                Children audioChild = new Children();
                num += 1;
                //设置tagId,因为是文本控件,所以文本一定在前面
                audioChild.setTag(String.valueOf(num));
                //设置类型
                audioChild.setType("voice");
                Attr audioAttr = new Attr();
                //音频后缀是MP3
                audioAttr.setMsrc(String.format("%02d",num)+".mp3");
                //设置时长
                audioAttr.setDuration(tempObject.getString("duration"));
                //音频展示标题
                audioAttr.setTitle(tempObject.getString("title"));
                JSONObject tempStyle = tempObject.getJSONObject("style");
                if (tempStyle != null){
                    //音频字体颜色
                    audioAttr.setColor(tempStyle.getString("color"));
                    //音频背景颜色
                    audioAttr.setBgsrc(tempStyle.getString("backgroundColor"));
                }
                audioChild.setAttr(audioAttr);

                Layout audioLay = new Layout();
                audioLay.setH(tempObject.getString("h"));
                audioLay.setW(tempObject.getString("w"));
                audioChild.setLayout(audioLay);

                child.add(audioChild);
            }
            //如果是视频
            if("video".equals(tempObject.getString("type"))){
                Children videoChild = new Children();
                num += 1;
                //设置tagId,因为是文本控件,所以文本一定在前面
                videoChild.setTag(String.valueOf(num));
                //设置类型
                videoChild.setType("video");
                Attr videoAttr = new Attr();
                //视频后缀是MP4
                videoAttr.setMsrc(String.format("%02d", num)+".mp4");
                //设置时长
                videoAttr.setDuration(tempObject.getString("duration"));
                videoChild.setAttr(videoAttr);

                Layout videoLay = new Layout();
                videoLay.setH(tempObject.getString("h"));
                videoLay.setW(tempObject.getString("w"));
                videoChild.setLayout(videoLay);

                child.add(videoChild);
            }
            //如果是图表
            if("chart".equals(tempObject.getString("type"))){
                Children chartChild = new Children();
                num += 1;
                //设置tagId,因为是文本控件,所以文本一定在前面
                chartChild.setTag(String.valueOf(num));
                //设置类型
                chartChild.setType("image");
                Attr chartAttr = new Attr();
                //图片后缀都是jpg
                chartAttr.setSrc(String.format("%02d", num)+".jpg");
                File file = new File(ottSrc+"/"+chartAttr.getSrc());

                try {
                    chartChild.setAttr(chartAttr);
                    Layout videoLay = new Layout();
                    videoLay.setH(tempObject.getString("h"));
                    videoLay.setW(tempObject.getString("w"));
                    chartChild.setLayout(videoLay);
                    child.add(chartChild);
                }catch (Exception e){
                    EmpExecutionContext.error(e, "发现异常");
                }
            }

        }
        template.setChildren(child);
        mainTemplate.setTemplate(template);
        return mainTemplate;
    }

    /**
     * 将html转为终端对象
     * @param tempData
     * @param url
     * @return
     */
    public static MainTemplate hFiveToTerminal(TempData tempData,String url,StringBuilder keyWordBuilder){
        MainTemplate mainTemplate = dataParsToTemp(tempData,keyWordBuilder);
        Event event = new Event();
        //表示打开H5链接
        event.setAction("2");
        url = url.replaceAll(uploadPath,visitPath);
        //action点击操作的目标内容，H5 url地址
        event.setTarget(url);
        Template template = mainTemplate.getTemplate();
        template.setEvent(event);
        return  mainTemplate;
    }

    /**
     * 将前端的卡片对象转为终端需要的对象
     *
     * @param tempData
     * @return
     */
    public static MainTemplate dataParsToTemp(TempData tempData,StringBuilder keyWordBuilder) {
        //后端模板
        MainTemplate mainTemplate = new MainTemplate();
        try {
            if (null != tempData) {
                //如果是12,则是卡片类型,转成终端需要的对象,传给终端
                if (tempData.getTmpType() != null && "12".equals(tempData.getTmpType().toString())) {
                    //设置消息类型,默认卡片,值为1
                    mainTemplate.setTemptype("1");
                    //给Template属性赋值
                    Template template = new Template();
                    template.setType("frame");
                    TempContent tempContent = null;
                    //获取前端tempCardData的content属性
                    for (SubTempData subTempData : tempData.getTempArr()) {
                        if ("12".equals(subTempData.getTmpType().toString())) {
                            tempContent = JSONObject.toJavaObject((JSONObject) subTempData.getContent(), TempContent.class);
                        }
                    }
                    //给template对象的attr属性赋值(卡片背景)
                    Attr attr = new Attr();
                    if (!StringUtils.IsNullOrEmpty(tempContent.getBgSrc())) {
                        attr.setBgsrc(getFileName(tempContent.getBgSrc()));
                    }
                    //给template对象的Layout属性赋值(卡片宽高)
                    Layout layout = new Layout();
                    if (tempContent.getH() != null) {
                        layout.setH(tempContent.getH().toString());
                    }
                    if (tempContent.getW() != null) {
                        layout.setW(tempContent.getW().toString());
                    }
                    //(组件)
                    List<Children> childrenList = new ArrayList<Children>();
                    TempDataElement tempDataElement = tempContent.getElements();
                    if (null != tempDataElement.getAudios() && tempDataElement.getAudios().size() > 0) {
                        for (TempElement element : tempDataElement.getAudios()) {
                            setChildrenList(childrenList, element, "voice");
                        }
                    }
                    if (null != tempDataElement.getButtons() && tempDataElement.getButtons().size() > 0) {
                        for (TempElement element : tempDataElement.getButtons()) {
                            setChildrenList(childrenList, element, "button");
                        }
                    }
                    if (null != tempDataElement.getImages() && tempDataElement.getImages().size() > 0) {
                        for (TempElement element : tempDataElement.getImages()) {
                            setChildrenList(childrenList, element, "image");
                        }
                    }
                    if (null != tempDataElement.getQrcodes() && tempDataElement.getQrcodes().size() > 0) {
                        for (TempElement element : tempDataElement.getQrcodes()) {
                            setChildrenList(childrenList, element, "qr");
                        }
                    }
                    if (null != tempDataElement.getTexts() && tempDataElement.getTexts().size() > 0) {
                        for (TempElement element : tempDataElement.getTexts()) {
                            setChildrenListV1(childrenList, element, "text",keyWordBuilder);
                        }
                    }
                    if (null != tempDataElement.getVideos() && tempDataElement.getVideos().size() > 0) {
                        for (TempElement element : tempDataElement.getVideos()) {
                            setChildrenList(childrenList, element, "video");
                        }
                    }
                    template.setAttr(attr);
                    template.setLayout(layout);
                    template.setChildren(childrenList);
                    mainTemplate.setTemplate(template);
                }
            }
            //如果是富文本
            if (tempData.getTmpType() != null && "13".equals(tempData.getTmpType().toString())) {
                //设置消息类型,默认富文本,值为2
                mainTemplate.setTemptype("2");
                //给Template属性赋值
                Template template = new Template();
                template.setType("frame");
                JSONObject richTextJson = null;
                //给template对象的Layout属性赋值(卡片位置)
                Layout layout = new Layout();
                //获取前端tempCardData的content属性
                for (SubTempData subTempData : tempData.getTempArr()) {
                    if ("13".equals(subTempData.getTmpType().toString()) && subTempData.getContent() != null) {
                        richTextJson = JSONObject.parseObject(subTempData.getContent().toString());
                        if (subTempData.getH() != null) {
                            layout.setH(subTempData.getH().toString());
                        }
                        if (subTempData.getW() != null) {
                            layout.setW(subTempData.getW().toString());
                        }
                    }
                }
                Children children = new Children();
                if (!StringUtils.IsNullOrEmpty(richTextJson.getString("template"))) {
                    children.setType("richtext");
                    children.setTag("richtext");
                    Attr attr = new Attr();
                    attr.setText(getText(richTextJson.getString("template")));
                    children.setAttr(attr);
                }
                //(组件)
                List<Children> childrenList = new ArrayList<Children>();
                childrenList.add(children);
                template.setLayout(layout);
                template.setChildren(childrenList);
                mainTemplate.setTemplate(template);
            }
            //如果是h5
            if (tempData.getTmpType() != null && "15".equals(tempData.getTmpType().toString())) {
                //设置消息类型,默认html,值为1
                mainTemplate.setTemptype("4");
                //给Template属性赋值
                Template template = new Template();
                template.setType("frame");
                TempContent tempContent = null;
                //获取前端的APP对象
                H5App h5App = tempData.getApp();
                //给template对象的Layout属性赋值(卡片宽高)
                Layout layout = new Layout();
                if (String.valueOf(h5App.getHeight()) != null) {
                    layout.setH(String.valueOf(h5App.getHeight()));
                }
                if (String.valueOf(h5App.getWidth()) != null) {
                    layout.setW(String.valueOf(h5App.getWidth()));
                }
                List<Children> childrenList = new ArrayList<Children>();
                // 获取实体类的所有属性，返回Field数组
                Field[] fields = h5App.getClass().getDeclaredFields();
                for (Field field : fields){
                    String name = field.getName();
                    String type = field.getGenericType().toString();
                    if("class com.montnets.emp.rms.meditor.entity.H5DataElement".equals(type)){
                        Method m = h5App.getClass().getMethod("get" + name.substring(0,1).toUpperCase()+name.substring(1));
                        H5DataElement h5DataElement = (H5DataElement)m.invoke(h5App);
                        setHFiveChildrenList(childrenList,h5DataElement);
                    }
                }
                template.setLayout(layout);
                template.setChildren(childrenList);
                mainTemplate.setTemplate(template);
            }
            return mainTemplate;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "前端对象转后端对象异常");
            return null;
        }
    }

    public static void setHFiveChildrenList(List<Children> childrenList,H5DataElement h5DataElement){
        Children children = new Children();
        children.setType(h5DataElement.getType());

        children.setTag(h5DataElement.getTag());
        //获取H5的style
        H5Style style = h5DataElement.getStyle();

        //设置Attr
        Attr attr = new Attr();
        attr.setText(getText(h5DataElement.getText()));

        if("text".equals(h5DataElement.getType())){
            attr.setSize(style.getFontSize());
            attr.setColor(style.getColor());
            attr.setMaxLine(String.valueOf(style.getMaxLine()));
        }
        if(StringUtils.isNotEmpty(h5DataElement.getSrc())){
            attr.setSrc(getFileName(h5DataElement.getSrc()));
            attr.setSrctype("1");//图片数据源类型：1-文件名；2-url
        }

        //设置layout
        Layout layout = new Layout();
        layout.setX(String.valueOf(style.getLeft()));
        layout.setY(String.valueOf(style.getTop()));
        layout.setW(String.valueOf(style.getWidth()));
        layout.setH(String.valueOf(style.getHeight()));


        children.setAttr(attr);
        children.setLayout(layout);
        childrenList.add(children);
    }

    public static void setChildrenList(List<Children> childrenList, TempElement element, String type) {
        try {
            Children children = new Children();
            children.setType(type);
            children.setTag(element.getTag());
            setTempAttr(children, element, type);
            setTempLayout(children, element);
            if ("button".equals(type) || "image".equals(type)) {
                setEvent(children, element, type);
            }
            childrenList.add(children);
        } catch (Exception e) {
            // TODO: handle exception
            EmpExecutionContext.error(e, "设置childrenList异常");
        }
    }

    /**
     *  此方法专门用于文本组件，keyWordBuilder 记录文本内容，进行关键字过滤
     * @param childrenList
     * @param element
     * @param type
     * @param keyWordBuilder
     */
    public static void setChildrenListV1(List<Children> childrenList, TempElement element, String type,StringBuilder keyWordBuilder) {
        try {
            Children children = new Children();
            children.setType(type);
            children.setTag(element.getTag());
            setTempAttrV1(children, element, type,keyWordBuilder);
            setTempLayout(children, element);
            if ("button".equals(type) || "image".equals(type)) {
                setEvent(children, element, type);
            }
            childrenList.add(children);
        } catch (Exception e) {
            // TODO: handle exception
            EmpExecutionContext.error(e, "设置childrenList异常");
        }
    }

    /**
     * 设置msrc
     *
     * @param tag
     * @param mSrc
     * @return
     */
    public static String getTempSrc(String tag, String mSrc) {
        try {
            String src = mSrc.substring(0, mSrc.lastIndexOf("/")) + "/" + tag + mSrc.substring(mSrc.lastIndexOf("."));
            return src;
        } catch (Exception e) {
            // TODO: handle exception
            EmpExecutionContext.error(e, "设置msrc异常");
            return null;
        }
    }

    public static void setMsrc (Attr childAttr,String filePath,String type){
        if (StringUtils.isEmpty(filePath)){
            EmpExecutionContext.info("上传视频或音频路径不能为空");
            return;
        }
        childAttr.setMsrctype("2");
        String ftype = null;
        //1表示HTML上传,2表示H5图文上传
        String btype = null;
        String fpath = null;
        String uploadUrl = null;
        if("video".equals(type)){
            //2:图片 3:视频 4：音频
            ftype = "3";
            btype = "2";
        }else if("voice".equals(type)){
            //2:图片 3:视频 4：音频
            ftype = "4";
            btype = "2";
        }
        File file = new File(new TxtFileUtil().getWebRoot()+filePath);

        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateNowStr = sdf.format(d);
        //上传保存路径
        fpath = "scene/" + dateNowStr;
        try{
            uploadUrl = String2FileUtil.uploadFile(file, fpath, btype, ftype);
            if(uploadUrl != null){
                uploadUrl = visitPath+uploadRootPath+uploadUrl;
                childAttr.setMsrc(uploadUrl);
            }
        }catch (Exception e){
            EmpExecutionContext.error(e,"卡片上传资源失败");
        }

    }

    /**
     * 设置Attr
     *
     * @param children
     * @param element
     * @return
     */
    public static void setTempAttr(Children children, TempElement element, String type) {
        try {
            Attr childAttr = new Attr();;
            //只有视频和音频才有msrc属性
            if ("video".equals(type) || "voice".equals(type)) {
                String mSrc = element.getSrc();
                if (mSrc != null) {
                    //设置音视频源,以及类型,此处有上传操作
                    setMsrc(childAttr,mSrc,type);
                    //childAttr.setMsrc(getFileName(mSrc));
                }
                if (element.getDuration() != null) {
                    childAttr.setDuration(element.getDuration().toString());
                }
                //音频会设置title(标题),bgsrc(背景颜色),color(字体颜色)
                if(StringUtils.isNotEmpty(element.getTitle())){
                    childAttr.setTitle(element.getTitle());
                }
                if(element.getStyle() !=null){
                    ElementStyle tempStyle = element.getStyle();
                    if (tempStyle != null){
                        childAttr.setColor(tempStyle.getColor());//音频字体颜色
                        childAttr.setBgsrc(tempStyle.getBackgroundColor());//音频背景颜色
                    }
                }
                //只有视频才会同时又msrc和src
				if ("video".equals(type)){
                    //视频裁剪工具,此处用于获取视频首帧图片
                    MediaTool mediaTool = new MediaTool();
                    //获取视频文件
                    File file = new File(new TxtFileUtil().getWebRoot()+mSrc);
                    //获取视频首帧名,无后缀
                    String newFileName = file.getName();
                    newFileName = newFileName.substring(0, newFileName.lastIndexOf(".")) + "_fm";
                    String filePath = new TxtFileUtil().getWebRoot() + "file/scene/firstFrame";
                    if (filePath.indexOf("/") == 0){
                        filePath = filePath.substring(1,filePath.length());
                    }
                    //创建首帧文件夹
                    File fileDir = new File(filePath);
                    if (!fileDir.exists()) {
                        fileDir.mkdirs();
                    }
                    //生成首帧图片
                    int flag = mediaTool.getFirstFrame(toolPath,file.getAbsolutePath(),filePath,newFileName);
                    //0表示获取生成首帧图片成功
                    if(0 == flag){
                        File firstFrameFile = new File(filePath + "/" + newFileName + ".png");
                        //上传首帧图片
                        try{
                            String firstFrameUrl = String2FileUtil.uploadFile(firstFrameFile, "file/scene/firstFrame", "2", "2");
                            childAttr.setSrc(visitPath+uploadRootPath+firstFrameUrl);
                            childAttr.setSrctype("2");
                        }catch (Exception e){
                            EmpExecutionContext.error(e,"卡片上传资源失败");
                        }
                    }else{
                        EmpExecutionContext.info("场景视频首帧图片生成失败");
                    }
				}
            }
            //只有图片和二维码才有src(和视频的src值不一样)
            if ("image".equals(type) || "qr".equals(type)) {
                childAttr.setSrc(getFileName(element.getSrc()));
                //只有图片才会同时又Corne和src
                if ("image".equals(type)) {
                    if (element.getStyle() != null) {
                        childAttr.setCorner(element.getStyle().getBorderRadius());
                    }
                    if (element.getBorderRadius() != null) {
                        childAttr.setCorner(String.valueOf(element.getBorderRadius()));
                    }
                }
            }
            if ("richtext".equals(type) || "text".equals(type) || "button".equals(type)) {
                childAttr.setText(getText(element.getText()));
            }
            if ("text".equals(type) || "button".equals(type)) {
                if (element.getStyle() != null) {
                    childAttr.setBgsrc(element.getStyle().getBackgroundColor());
                    childAttr.setSize(element.getStyle().getFontSize());
                    childAttr.setColor(element.getStyle().getColor());
                    childAttr.setPress(element.getStyle().getPress());
                    if("bold".equals(element.getStyle().getFontWeight())){
                        childAttr.setTstyle("b");
                    }else{
                        childAttr.setTstyle(element.getStyle().getFontWeight());
                    }
                }
            }
            children.setAttr(childAttr);
        } catch (Exception e) {
            // TODO: handle exception
            EmpExecutionContext.error(e, "设置Attr异常");
        }
    }
    public static void setTempAttrV1(Children children, TempElement element, String type,StringBuilder keyWordBuilder) {
        try {
            Attr childAttr = new Attr();//children.getAttr();
            //只有视频和音频才有msrc属性
            if ("video".equals(type) || "voice".equals(type)) {
                String mSrc = element.getSrc();
                if (mSrc != null) {
                    childAttr.setMsrc(getFileName(mSrc));
                }
                if (element.getDuration() != null) {
                    childAttr.setDuration(element.getDuration().toString());
                }
                //只有视频才会同时又msrc和src
                //src 用于缩略图，没有故不设置
				/*if ("video".equals(type)) dataParsToTemp{
					String src = getTempSrc(element.getTag(), mSrc);
					childAttr.setSrc(getFileName(src));
					if(element.getStyle() != null) {
						childAttr.setCorner(element.getStyle().getBorderRadius());
					}
				}*/
            }
            //只有图片和二维码才有src(和视频的src值不一样)
            if ("image".equals(type) || "qr".equals(type)) {
                childAttr.setSrc(getFileName(element.getSrc()));
                //只有图片才会同时又Corne和src
                if ("image".equals(type)) {
                    if (element.getStyle() != null) {
                        childAttr.setCorner(element.getStyle().getBorderRadius());
                    }
                    if (element.getBorderRadius() != null) {
                        childAttr.setCorner(String.valueOf(element.getBorderRadius()));
                    }
                }
            }
            if ("richtext".equals(type) || "text".equals(type) || "button".equals(type)) {
                childAttr.setText(getText(element.getText()));
                keyWordBuilder.append(getText(element.getText()));
            }
            if ("text".equals(type) || "button".equals(type)) {
                if (element.getStyle() != null) {
                    childAttr.setBgsrc(element.getStyle().getBackgroundColor());
                    childAttr.setSize(element.getStyle().getFontSize());
                    childAttr.setColor(element.getStyle().getColor());
                    childAttr.setPress(element.getStyle().getPress());
                    if("bold".equals(element.getStyle().getFontWeight())){
                        childAttr.setTstyle("b");
                    }else{
                        childAttr.setTstyle(element.getStyle().getFontWeight());
                    }
                }
            }
            children.setAttr(childAttr);
        } catch (Exception e) {
            // TODO: handle exception
            EmpExecutionContext.error(e, "设置Attr异常");
        }
    }


    public static void setEvent(Children children, TempElement element, String type) {
        try {
            Button button = new Button();//children.getEvent();
            ButtonMap map = new ButtonMap();
            App app = new App();
            button.setAction(element.getAction());
            if ("button".equals(type)) {
                if ("2".equals(element.getAction())) {
                    button.setTarget(element.getUrl());
                }
                if ("3".equals(element.getAction())) {
                    button.setTarget(element.getParam());
                }
                if ("6".equals(element.getAction())) {
                    button.setTarget(element.getTel());
                }
                if ("5".equals(element.getAction())) {//打开地图
                    map.setPkg(element.getPkg());
                    map.setLat(element.getLat());
                    map.setLon(element.getLon());
                    map.setTitle(element.getTitle());
                    map.setAddr(element.getAddr());
                    button.setMap(map);
                }
                if ("4".equals(element.getAction())) {//打开APP
                    app.setPkg(element.getPkg());
                    button.setApp(app);
                }
                if ("7".equals(element.getAction())) {//打开快应用
                    app.setPkg(element.getPkg());
                    app.setPath(element.getPath());
                    app.setParam(element.getParam());
                    button.setApp(app);
                }
            } else if ("image".equals(type)) {
                button.setTarget(element.getUrl());
            }
            children.setEvent(button);
        } catch (Exception e) {
            // TODO: handle exception
            EmpExecutionContext.error(e, "设置EVENT异常");
        }
    }

    /**
     * 设置layout
     *
     * @param children
     * @param element
     */
    public static void setTempLayout(Children children, TempElement element) {
        try {
            Layout childLayout = new Layout();//children.getLayout();
            //为非空才设置值
            if (element.getH() != null && !"".equals(element.getH().toString())) {
                childLayout.setH(element.getH().toString());
            }
            if (element.getW() != null && !"".equals(element.getW().toString())) {
                childLayout.setW(element.getW().toString());
            }
            if (element.getX() != null && !"".equals(element.getX().toString())) {
                childLayout.setX(element.getX().toString());
            }
            if (element.getY() != null && !"".equals(element.getY().toString())) {
                childLayout.setY(element.getY().toString());
            }
            if (element.getZ() != null && !"".equals(element.getZ().toString())) {
                childLayout.setZ(element.getZ().toString());
            }
            if (element.getStyle() != null && element.getStyle().getTextAlign() != null && !"".equals(element.getStyle().getTextAlign())) {
                childLayout.setAlign(element.getStyle().getTextAlign().substring(0, 1));
            }
            children.setLayout(childLayout);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "设置layout异常");
        }
    }


    /**
     * 根据属性名获取属性值
     *
     * @param fieldName
     * @param o
     * @return
     */
    public static Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[]{});
            Object value = method.invoke(o, new Object[]{});
            return value;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "根据属性名获取属性值失败");
            return null;
        }
    }


    /**
     * 获取属性名数组
     */
    public static String[] getFieldName(Object o) {
        try {
            Field[] fields = o.getClass().getDeclaredFields();
            String[] fieldNames = new String[fields.length];
            for (int i = 0; i < fields.length; i++) {
                fieldNames[i] = fields[i].getName();
            }
            return fieldNames;
        } catch (Exception e) {
            // TODO: handle exception
            EmpExecutionContext.error(e, "获取属性名数组失败");
            return null;
        }
    }

    /**
     * 将对象转为json字符串
     *
     * @param object
     * @return
     */
    @SuppressWarnings("finally")
    public static String parseToString(Object object) {
        String json = null;
        try {
            json = JSON.toJSON(object).toString();
        } catch (Exception e) {
            json = null;
            EmpExecutionContext.error(e, "模板对象转为json异常");
        }
        return json;
    }

    public static String showRmsMsg(HttpServletRequest request, MessageUtils.RmsMessage rmsMessage) {
        //MessageUtils.getMsg(StaticValue.ZH_HK, MessageUtils.RmsMessage.TMPLATE_DELETE);

        Object lang = request.getSession().getAttribute(StaticValue.LANG_KEY);
        if (null == lang) {
            lang = StaticValue.ZH_CN;
        }
        return MessageUtils.getMsg(String.valueOf(lang), rmsMessage);
    }

    /**
     * 获取文件名
     *
     * @param filePath 文件路径
     * @return
     */
    public static String getFileName(String filePath) {
        String fileName = "";
        //获取最后一个/后面的文件名
        fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
        return fileName;
    }


    public static String rmsToString(String rmsJson, LinkedHashMap<String, String> map) {
        if (StringUtils.isEmpty(rmsJson)) {
            return null;
        }
        if (map.size() != 0 && rmsJson != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                rmsJson = rmsJson.replace(entry.getKey(), entry.getValue());
            }
        }
        List<Map<String, String>> rmsList = new ArrayList<Map<String, String>>();
        List<HashMap> hashMaps = JSONArray.parseArray(rmsJson, HashMap.class);
        Map<String, String> contentMap;
        HashMap<String, String> hashMap;
        for (int i = 0; i < hashMaps.size(); i++) {
            contentMap = new HashMap();
            hashMap = hashMaps.get(i);
            String text = hashMap.get("text");
            if (StringUtils.isNotEmpty(text)) {
                contentMap.put("tag", hashMap.get("tag"));
                contentMap.put("content", text);
                rmsList.add(contentMap);
            }
        }
        Map<String, Object> paramMap = new HashMap();
        paramMap.put("param", rmsList);
        Map<String, Object> outMap = new HashMap();
        outMap.put("content", JSONObject.toJSONString(paramMap));
        List<Map<String, Object>> contentList = new ArrayList<Map<String, Object>>();
        contentList.add(outMap);
        String previewContent = JSONObject.toJSONString(contentList);
        return previewContent;

    }


    public static String companyShowToString(String showFrontJson, LinkedHashMap<String, String> map) {
        if (StringUtils.isEmpty(showFrontJson)) {
            return null;
        }
        if (map.size() != 0 && showFrontJson != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                showFrontJson = showFrontJson.replace(entry.getKey(), entry.getValue());
            }
        }
        List<Map<String, String>> rmsList = new ArrayList<Map<String, String>>();
        JSONObject jsonObject = JSONObject.parseObject(showFrontJson);
        JSONArray pagesArray = jsonObject.getJSONArray("pages");
        for (int i = 0; i < pagesArray.size(); i++) {//遍历pages
            JSONObject page = pagesArray.getJSONObject(i);
            JSONArray elements = page.getJSONArray("elements");
            for (int j = 0; j < elements.size(); j++) {//遍历elements
                JSONObject element = elements.getJSONObject(j);
                if ("text".equals(element.getString("type"))) {
                    String tempContent = getText(element.getString("text"));
                    Map<String, String> contentMap = new HashMap();
                    if (StringUtils.isNotEmpty(tempContent)) {
                        contentMap.put("tag", element.get("tag").toString());
                        contentMap.put("content", tempContent);
                        rmsList.add(contentMap);
                    }
                }
            }
        }
        Map<String, Object> paramMap = new HashMap();
        paramMap.put("param", rmsList);
        Map<String, Object> outMap = new HashMap();
        outMap.put("content", JSONObject.toJSONString(paramMap));
        List<Map<String, Object>> contentList = new ArrayList<Map<String, Object>>();
        contentList.add(outMap);
        String previewContent = JSONObject.toJSONString(contentList);
        return previewContent;
    }

    public static boolean checkParam(String param) {
        Pattern p = Pattern.compile("文本[0-9]{0,2}|图文[0-9]{0,2}|报表[0-9]{0,2}");
        Matcher m = p.matcher(param);
        if (m.find()) {
            return true;
        }
        return false;
    }

    /**
     * TEXT中的内容替换
     * @param text
     * @return
     */
    public static String getText(String text){
        if(StringUtils.IsNullOrEmpty(text)){
            return text;
        }
        String content = text;
        String tempContent = content;
        String regex = "\\{#参数\\d+#\\}";
        String regexInput ="(<input[^>]*>)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            String no = content.substring(matcher.start()+4, matcher.end()-2);
            tempContent = tempContent.replaceFirst(regexInput, "\\{#参数"+no+"#\\}");
            content = content.replaceFirst(regexInput, "\\{#测试"+no+"#\\}");
            if(text.equals(content)){
                EmpExecutionContext.info("TEXT文本参数替换失败，无对应匹配值");
                break;
            }
            matcher = pattern.matcher(content);
        }
        return tempContent;
    }

    /**
     * TEXT中的内容替换参数P
     * @param text
     * @return
     */
    public static String getTextP(String text){
        if(StringUtils.IsNullOrEmpty(text)){
            return text;
        }
        String content = text;
        String tempContent = content;
        String regex = "#P_\\d+#";
        String regexInput ="(<input[^>]*>)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            String no = content.substring(matcher.start()+3, matcher.end()-1);
            tempContent = tempContent.replaceFirst(regexInput, "#P_"+no+"#");
            content = content.replaceFirst(regexInput, "\\{#测试"+no+"#\\}");
            if(text.equals(content)){
                EmpExecutionContext.info("TEXT文本参数替换失败，无对应匹配值");
                break;
            }
            matcher = pattern.matcher(content);
        }
        return tempContent;
    }

    public static String getCompanyParam(String str,Map paramMap){
        StringBuffer tempAllContent = new StringBuffer();
        JSONObject jsonObject = JSONObject.parseObject(str);
        JSONArray pagesArray = jsonObject.getJSONArray("pages");
        for(int i=0;i<pagesArray.size();i++){
            JSONObject page = pagesArray.getJSONObject(i);
            JSONArray elements = page.getJSONArray("elements");
            for(int j=0;j<elements.size();j++){
                JSONObject text = elements.getJSONObject(j);
                if("text".equals(text.getString("type"))){
                    String tempContent = getText(text.getString("text"));
                    String regex = "\\{#参数\\d+#\\}";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(tempContent);
                    while (matcher.find()) {
                        String no = tempContent.substring(matcher.start()+4, matcher.end()-2);
                        String value = paramMap.get("{#参数"+no+"#}").toString();
                        tempContent = tempContent.replaceFirst(regex, value);
                        matcher = pattern.matcher(tempContent);
                    }
                    tempAllContent.append(tempContent);
                }
                tempAllContent.append(",");//每一个Text的值用,隔来
            }
        }
        return tempAllContent.toString();
    }

}
