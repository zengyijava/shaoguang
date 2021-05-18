package com.montnets.emp.rms.tools;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.montnets.emp.util.StringUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Jason Huang
 * @date 2018年3月20日 上午10:51:03
 */

public class FormatFontTool {

    public List<Map<String, Object>> convertHTML(Elements outsideEles) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (Element outsideEle : outsideEles) {
            list.add(convertParagraph(outsideEle));
        }
        return list;
    }

    /**
     *  图片上的文字
     * @param jsonArray
     * @return
     */
    public List<Map<String, Object>> convertJSON(JSONArray jsonArray,StringBuilder keyWordBuilder) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i= 0;i<jsonArray.size();i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            if(object.containsKey("type") && object.getString("type").equals("text")){//图片上配文才加入
                list.add(convertParagraphJSON(object,keyWordBuilder));
            }else if(object.containsKey("type") && "image".equals(object.getString("type"))){
                list.add(convertIMGJSON(object));
            }
        }
        return list;
    }

    /**
     * Json数据中，图片类型数据封装
     * @param object
     * @return
     */
    private Map<String, Object> convertIMGJSON(JSONObject object) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("type", "image");
        map.put("isDynamic", false);
        map.put("w", object.getString("w"));
        map.put("h", object.getString("h"));
        map.put("left", object.getString("x"));
        map.put("top", object.getString("y"));
        map.put("imgPath", object.getString("src"));
        return map;
    }

    /**
     * Json数据中，文本类型数据封装
     * @param object
     * @return
     */
    private Map<String, Object> convertParagraphJSON(JSONObject object,StringBuilder keyWordBuilder) {
        StringBuilder builder = new StringBuilder("pango:");
        Map<String, String> styleMap = new HashMap<String, String>();
        Map<String, Object> map = new HashMap<String, Object>();
//		Element insideEle = outsideEle.getElementsByClass("editor-resize-text").first();

        // data-val="top:20;left:20;width:301;line-height:18;font-size:18;"
//		convertDataStyle(outsideEle.attr("data-val"), builder, styleMap);
        builder.append("<span");
        if(object.containsKey("x")){
            styleMap.put("left", object.getString("x"));
        }
        if(object.containsKey("y")){
            styleMap.put("top", object.getString("y"));
        }
        if(object.containsKey("width")){
            styleMap.put("size", object.getString("width")+"x");
        }
        if(object.containsKey("rotate")){
            styleMap.put("rotate",object.getString("rotate"));
        }

        if(object.containsKey("style")){
            JSONObject styleObject = object.getJSONObject("style");
            if(styleObject.containsKey("fontSize")){
                styleMap.put("font", styleObject.getString("fontSize"));
                builder.append(" font='").append(styleObject.getString("fontSize")).append("px'");
            }

            if(styleObject.containsKey("textAlign")){
                styleMap.put("gravity", styleObject.getString("textAlign"));
            }
            if(styleObject.containsKey("fontFamily")){
                styleMap.put("face", styleObject.getString("fontFamily"));
                builder.append(" face='").append(styleObject.getString("fontFamily")).append("'");
            }
            if(styleObject.containsKey("fontStyle")){
                styleMap.put("style", "1");
                builder.append(" style='").append(styleObject.getString("fontStyle")).append("'");
            }
            if(styleObject.containsKey("fontWeight")){
                if (Integer.parseInt(styleObject.getString("fontWeight")) > 400) {
                    styleMap.put("weight", "1");
                }
                builder.append(" weight='").append(styleObject.getString("fontWeight")).append("'");
            }
            if(styleObject.containsKey("text-decoration")){
                styleMap.put("underline", "1");
                builder.append(" underline='single'");
            }
            if(styleObject.containsKey("color")){
                String foreground = styleObject.getString("color");
                styleMap.put("foreground", foreground);
                builder.append(" foreground='").append(foreground).append("'");
            }
            if(styleObject.containsKey("backgroundColor")){
                String background = styleObject.getString("backgroundColor");
                if(StringUtils.isBlank(background)){
                    styleMap.put("background", "transparent");
                }else{
                    styleMap.put("background", background);
                }
                if (!StringUtils.isBlank(background) && !background.equals("transparent")) {
                    builder.append(" background='").append(background).append("'");
                }

            }

        }

        builder.append(">");

        String content = convertLine(object.getString("text"));
        keyWordBuilder.append(content);
        // 判断该段文字是否含有参数
        if (content.contains("{#图参")) {
            map.put("isDynamic", true);
            // 替换参数中的标志字符
            content = replaceParam(content);
        } else {
            map.put("isDynamic", false);
        }

        styleMap.put("content", content);
        builder.append(content);
        builder.append("</span>");

        map.put("pango", builder.toString());
        map.put("styleMap", styleMap);
        map.put("type", "text");
        return map;
    }

    private Map<String, Object> convertParagraph(Element outsideEle) {
        StringBuilder builder = new StringBuilder("pango:");
        Map<String, String> styleMap = new HashMap<String, String>();
        Map<String, Object> map = new HashMap<String, Object>();
        Element insideEle = outsideEle.getElementsByClass("editor-resize-text").first();

        convertDataStyle(outsideEle.attr("data-val"), builder, styleMap);
        convertOutsideStyle(outsideEle.attr("style"), styleMap);
        convertInsideStyle(insideEle.attr("style"), builder, styleMap);

        String content = convertLine(insideEle.getElementsByClass("editor-resize-text").html().replace("&nbsp;", " "));

        // 判断该段文字是否含有参数
        if (content.contains("{#图参")) {
            map.put("isDynamic", true);
            // 替换参数中的标志字符
            content = replaceParam(content);
        } else {
            map.put("isDynamic", false);
        }

        styleMap.put("content", content);
        builder.append(content);
        builder.append("</span>");

        map.put("pango", builder.toString());
        map.put("styleMap", styleMap);
        return map;
    }


    /**
     * 针对不同浏览器生成换行的html进行不同的text处理
     * @param html html字符串
     * @return 处理后的String字符串
     */
    private String convertLine(String html) {
        String text = "";
        if (html != null && !html.trim().equals("")) {
            text = StringEscapeUtils.unescapeHtml(html).replaceAll("\n", "");
            if (text.contains("<div>")) {
                text = text.replaceAll("<div> ", "\n").replaceAll("<div>", "\n").replaceAll("</div>", "").replaceAll("<br>", "");
            } else if (text.contains("<p>")) {
                text = text.replaceAll("<p>", "").replaceAll("</p>", "\n").replaceAll("<br>", "");
            } else if (text.contains("<br>")) {
                text = text.replaceAll("<br>", "\n");
            } else if (text.contains("</br>")) {
                text = text.replaceAll("</br>", "\n");
            }
        }
        return text;
    }

    private void convertDataStyle(String html, StringBuilder builder, Map<String, String> map) {
        String[] styles = html.replace(" ", "").split(";"); // 前端编辑器保证有且仅有5个值
        builder.append("<span");
        for (String style : styles) {
            String[] strs = style.split(":");
            if (strs[0].equals("left")) {
                map.put(strs[0], strs[1]);
            } else if (strs[0].equals("top")) {
                map.put(strs[0], strs[1]);
            } else if (strs[0].equals("width")) {
                map.put("size", strs[1] + "x");
            } else if (strs[0].equals("font-size")) {
                map.put("font", strs[1] + "px");
                builder.append(" font='").append(strs[1]).append("px'");
            }
        }

    }

    private void convertOutsideStyle(String html, Map<String, String> map) {
        String[] styles = html.replace(" ", "").split(";");
        for (String style : styles) {
            if (style.contains("rotate")) {
                map.put("rotate", style.substring(style.indexOf("(") + 1, style.indexOf("d")));
            }
        }
    }

    private void convertInsideStyle(String html, StringBuilder builder, Map<String, String> map) {
        String[] styles = html.replace(" ", "").split(";");
        for (String style : styles) {
            String[] strs = style.split(":");
            // text-align、width熟悉依靠编辑器保持唯一性
            if (strs[0].equals("text-align")) {
                map.put("gravity", strs[1]);
            } else if (strs[0].equals("font-family")) {
                map.put("face", strs[1]);
                builder.append(" face='").append(strs[1]).append("'");
            } else if (strs[0].equals("font-style")) {
                map.put("style", "1");
                builder.append(" style='").append(strs[1]).append("'");
            } else if (strs[0].equals("font-weight")) {
                if (Integer.parseInt(strs[1]) > 400) {
                    map.put("weight", "1");
                }
                builder.append(" weight='").append(strs[1]).append("'");
            } else if (strs[1].equals("underline")) {
                map.put("underline", "1");
                builder.append(" underline='single'");
            } else if (strs[0].equals("color")) {
                String foreground = convertRGB(strs[1]);
                map.put("foreground", foreground);
                builder.append(" foreground='").append(foreground).append("'");
            } else if (strs[0].equals("background-color")) {
                String background = convertRGB(strs[1]);
                map.put("background", background);
                if (!background.equals("transparent")) {
                    builder.append(" background='").append(background).append("'");
                }
            }
        }
        builder.append(">");
    }

    /**
     * 替换参数字符串
     * @param str 替换前的字符串
     * @return 替换后字符串
     */
    private String replaceParam(String str) {
        String regex = "\\{#图参\\d+#\\}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            String no = str.substring(matcher.start() + 4, matcher.end() - 2);
            str = str.replaceFirst(regex, "#P_" + no + "#");
            matcher = pattern.matcher(str);
        }
        return str;
    }

    private String convertRGB(String color) {
        if (color.contains("rgb")) {
            int r, g, b;
            StringBuilder builder = new StringBuilder("#");
            r = Integer.parseInt(color.substring(color.indexOf("(") + 1, color.indexOf(",")));
            g = Integer.parseInt(color.substring(color.indexOf(",") + 1, color.lastIndexOf(",")));
            b = Integer.parseInt(color.substring(color.lastIndexOf(",") + 1, color.indexOf(")")));
            builder.append(toHex(r / 16));
            builder.append(toHex(r % 16));
            builder.append(toHex(g / 16));
            builder.append(toHex(g % 16));
            builder.append(toHex(b / 16));
            builder.append(toHex(b % 16));
            return builder.toString();
        } else {
            return color;
        }
    }

    private String toHex(int i) {
        if (i == 10)
            return "A";
        else if (i == 11)
            return "B";
        else if (i == 12)
            return "C";
        else if (i == 13)
            return "D";
        else if (i == 14)
            return "E";
        else if (i == 15)
            return "F";
        else
            return String.valueOf(i);
    }

    /**
     * 图片上加图片
     * @param jsonArray
     * @return
     */
    public List<Map<String, Object>> convertImgJSONArray(JSONArray jsonArray) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i= 0;i<jsonArray.size();i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            if(object.containsKey("type") && object.getString("type").equals("image")){//图片上配文才加入

            }
        }
        return list;
    }
}
