package com.montnets.emp.rms.tools;

import com.montnets.emp.common.context.EmpExecutionContext;
import org.apache.commons.codec.binary.Base64;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jason Huang
 * @date 2018年3月20日 上午10:51:03
 */
// TODO 保留此类是为了方便未来版本(可兼容局部字体样式)的修改,暂时无引用,如不需要可删除
public class UselessFormatFontTool {

	public static void main(String[] args) {
		UselessFormatFontTool f = new UselessFormatFontTool();
		f.convertHtml("<div id=\"TEXT_MODULE_0\" class=\"J-add-module\" style=\"position: absolute; top: 20px; left: 20px; z-index: 39; transform-origin: center center 0px; transform: rotate(0deg);\"><div id=\"TEXT_MODULE_0_inner\" class=\"editor-resize-text\" style=\"font-family: SimHei; font-size: 18px; line-height: 18px; font-weight: 400; font-style: normal; text-decoration: none; text-align: left; color: rgb(85, 85, 85); background-color: transparent; width: 180px;\">请双击输{#参数1#}入文字</div></div>");
	}

	private String convertHtml(String html) {
		StringBuilder builder = new StringBuilder("pango:");
		Map<String, String> map = new HashMap<String, String>();
		Document doc = Jsoup.parseBodyFragment(html);
		Element outsideEle = doc.body().getElementsByClass("J-add-module").first();
		Elements insideEles = outsideEle.getElementsByClass("editor-resize-text");

		convertOutsideStyle(outsideEle.attr("style"), map);

		/*
		 * String pString = outsideEle.html(); String p1 = null, p2 = null; if
		 * (pString.contains("<span")) { p1 = pString.substring(0,
		 * pString.indexOf("<")); p2 =
		 * pString.substring(pString.lastIndexOf(">") + 1); }
		 * builder.append(p1);
		 */

		// for (Element insideEle : insideEles) {
		Element insideEle = insideEles.first();
		// if (insideEle.hasAttr("style")) {
		convertInsideStyle(insideEle.attr("style"), builder, map);
		map.put("content", insideEle.text());
		builder.append(insideEle.text());
		builder.append("</span>");
		// } else {
		// map.put("content", insideEle.text());
		// builder.append("<span>");
		// builder.append(insideEle.text());
		// builder.append("</span>");
		// }
		// }
		/*
		 * builder.append(p2); builder.append("</span>");
		 */
		// IMGTool imgTool = new IMGTool();
		// imgTool.buildIMG(builder.toString(), map);
		System.out.println(buildJson(map));
		return "";

	}

	private void convertOutsideStyle(String html, Map<String, String> map) {
		String[] styles = html.replace(" ", "").split(";");
		for (String style : styles) {
			if (style.contains("rotate")) {
				map.put("rotate", style.substring(style.indexOf("(") + 1, style.indexOf("d")));
			} else {
				String[] strs = style.split(":");
				if (strs[0].equals("left")) {
					map.put(strs[0], strs[1].substring(0, strs[1].indexOf("px")));
				} else if (strs[0].equals("top")) {
					map.put(strs[0], strs[1].substring(0, strs[1].indexOf("px")));
				}
			}
		}
	}

	private void convertInsideStyle(String html, StringBuilder builder, Map<String, String> map) {
		String[] styles = html.split(";");
		builder.append("<span");
		for (String style : styles) {
			String[] strs = style.replace(" ", "").split(":");
			// text-align、width熟悉依靠编辑器保持唯一性
			if (strs[0].equals("text-align")) {
				map.put("gravity", strs[1]);
			} else if (strs[0].equals("width")) {
				map.put("size", strs[1].substring(0, strs[1].indexOf("px")) + "x");
			} else if (strs[0].equals("font-family")) {
				map.put("face", strs[1]);
				builder.append(" face='").append(strs[1]).append("'");
			} else if (strs[0].equals("font-size")) {
				map.put("font", strs[1]);
				builder.append(" font='").append(strs[1]).append("'");
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

	private String buildJson(Map<String, String> map) {
		String content64 = null;
		try {
			content64 = Arrays.toString(Base64.encodeBase64(map.get("content").getBytes("utf-8")));
		} catch (UnsupportedEncodingException e) {
            EmpExecutionContext.error(e, "发现异常");
		}
		StringBuilder builder = new StringBuilder("{\"ctrinfo\":\"");
		builder.append("content").append(content64);
		builder.append("-x_y").append(map.get("left")).append("*").append(map.get("top"));
		builder.append("-w_h").append(map.get("size")).append("*0");
		builder.append("-font_fmt").append(map.get("face"));
		builder.append("-font_size").append(map.get("font").substring(0, map.get("font").indexOf("px")));
		builder.append("-font_col").append(map.get("foreground"));
		builder.append("-font_bg_col").append(map.get("background"));
		if (map.containsKey("weight")) {
			builder.append("-font_bold").append(1);
		} else {
			builder.append("-font_bold").append(0);
		}
		if (map.get("gravity").equals("left")) {
			builder.append("-font_agen").append(0);
		} else if (map.get("gravity").equals("center")) {
			builder.append("-font_agen").append(1);
		} else {
			builder.append("-font_agen").append(2);
		}
		builder.append("-rotate").append(map.get("rotate"));
		builder.append("\"}");
		return builder.toString();
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
		if (i == 10) {
            return "A";
        } else if (i == 11) {
            return "B";
        } else if (i == 12) {
            return "C";
        } else if (i == 13) {
            return "D";
        } else if (i == 14) {
            return "E";
        } else if (i == 15) {
            return "F";
        } else {
            return String.valueOf(i);
        }
	}
}
