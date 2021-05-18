package com.montnets.emp.rms.meditor.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 前端组件json类
 * @author moll
 *
 */
public class TempElement implements Serializable{
	/**
	 *
	 */
	private static final long serialVersionUID = -3171639409116058896L;
	//模板ID
	private String tag;
	//文本，图片，二维码的路径,视频，音频路径
	private String src;
	private String currentSrc;
	//持续时间（视频音频）
	private Integer duration;
	//文本内容
	private String text;
	//横坐标
	private Integer x;
	//纵坐标
	private Integer y;
	//宽
	private Integer w;
	//高
	private Integer h;
	//组件的层级
	private Integer z;
	//点击操作的行为动作：1.打开大图、2.打开链接、3.复制、4.打开APP、5.打开地图、6.拨打电话、7.打开快应用
	private String action;
	//action点击操作的目标内容，可以表示为：大图url地址、网页url地址、电话号码、需要复制的内容
	private String target;
	//应用包名，这里可以指定使用那个地图
	private String pkg;
	//地图纬度，double类型
	private String lat;
	//地图经度，double类型
	private String lon;
	//地图显示的title
	private String title;
	//位置坐标的地址
	private String addr;
	private String path;
	//圆角
	private Integer borderRadius;

	private String url;

	private String tel;

	private String param;

	private String type;

	private String fileName;
	//二维码参数,0表示静态二维码,1表示动态二维码
	private String codeType;
	//二维码参数
	private String name;

	public String getCodeType() {
		return codeType;
	}

	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public Integer getBorderRadius() {
		return borderRadius;
	}

	public void setBorderRadius(Integer borderRadius) {
		this.borderRadius = borderRadius;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	//组件样式
	private ElementStyle style;


	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getPkg() {
		return pkg;
	}
	public void setPkg(String pkg) {
		this.pkg = pkg;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLon() {
		return lon;
	}
	public void setLon(String lon) {
		this.lon = lon;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		this.src = src;
	}
	public String getCurrentSrc() {
		return currentSrc;
	}
	public void setCurrentSrc(String currentSrc) {
		this.currentSrc = currentSrc;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public ElementStyle getStyle() {
		return style;
	}
	public void setStyle(ElementStyle style) {
		this.style = style;
	}
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	public Integer getX() {
		return x;
	}
	public void setX(Integer x) {
		this.x = x;
	}
	public Integer getY() {
		return y;
	}
	public void setY(Integer y) {
		this.y = y;
	}
	public Integer getW() {
		return w;
	}
	public void setW(Integer w) {
		this.w = w;
	}
	public Integer getH() {
		return h;
	}
	public void setH(Integer h) {
		this.h = h;
	}
	public Integer getZ() {
		return z;
	}
	public void setZ(Integer z) {
		this.z = z;
	}


}
