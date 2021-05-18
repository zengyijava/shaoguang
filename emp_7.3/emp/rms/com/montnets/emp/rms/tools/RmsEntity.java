package com.montnets.emp.rms.tools;

import java.util.ArrayList;
import java.util.List;
/**
 * RMS解析实体
 * @author shouc
 *
 */
public class RmsEntity {
	//文件标识
	private Integer identification;
	//版本
	private Integer version;
	//文件类型
	private Integer coding;
	//Rms数据文件总长度
	private Integer fileSize;
	//包含的文件数目
	private Integer fileCount;
	//主题的字节长度
	private Integer titleSize;
	//标题
	private String title;
	//文件地址
	private List<String> fileUrl;
	
	//文件大小
	private List<Integer> dataSize;
	
	public Integer getIdentification() {
		return identification;
	}
	public void setIdentification(Integer identification) {
		this.identification = identification;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public Integer getCoding() {
		return coding;
	}
	public void setCoding(Integer coding) {
		this.coding = coding;
	}
	public Integer getFileSize() {
		return fileSize;
	}
	public void setFileSize(Integer fileSize) {
		this.fileSize = fileSize;
	}
	public Integer getFileCount() {
		return fileCount;
	}
	public void setFileCount(Integer fileCount) {
		this.fileCount = fileCount;
	}
	public Integer getTitleSize() {
		return titleSize;
	}
	public void setTitleSize(Integer titleSize) {
		this.titleSize = titleSize;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<String> getFileUrl() {
		return fileUrl;
	}
	public void setFileUrl(List<String> fileUrl) {
		this.fileUrl = fileUrl;
	}
	public List<Integer> getDataSize() {
		return dataSize;
	}
	public void setDataSize(List<Integer> dataSize) {
		this.dataSize = dataSize;
	}
	
	public void setDataSize(int i,int dataSize) {
		if(this.dataSize==null){
			this.dataSize = new ArrayList<Integer>();
		}
		this.dataSize.add(i, dataSize);
	}
}
