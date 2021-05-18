/**
 * Program  : SUCAtable.java
 * Author   : chensj
 * Create   : 2013-6-14 上午10:49:01
 * company ShenZhen Montnets Technology CO.,LTD.
 *
 */

package com.montnets.emp.netnews.bean;

import java.util.Date;

import com.montnets.emp.netnews.base.DateFormatter;


/**
 * 
 * @author   chensj <510061684@qq.com>
 * @version  1.0.0
 * @2013-6-14 上午10:49:01
 */
public class SUCAtable implements Comparable<SUCAtable>{
	
	private Integer id;//id

	private String fileName;//文件名称
	
	private String fileDescripbe;//文件描述
	
	private String fileType; //文件类型
	
	private String uploadDate; //文件上传日期
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileDescripbe() {
		return fileDescripbe;
	}
	public void setFileDescripbe(String fileDescripbe) {
		this.fileDescripbe = fileDescripbe;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getUploadDate() {
		return uploadDate;
	}
	public void setUploadDate(String uploadDate) {
		this.uploadDate = uploadDate;
	}
	public int compareTo(SUCAtable o) {
		// TODO Auto-generated method stub
		Date thisdate = DateFormatter.swicthString(uploadDate);
		Date otherdate = DateFormatter.swicthString(o.getUploadDate());
		return otherdate.compareTo(thisdate);
	}
	
	//注释一下：测试工具测试这个地方有问题，原因是：
	//最好重写hashCode方法，因为你定义的这个类可能在以后被（自己/其他程序员）放入诸如HashMap等数据结构中
	//hashcode是有契约的，要求equal的对象彼此hashcode也相同，才认为是同一个对象,
	//所以此处，返回的是ID的hashcode。
	@Override
	public int hashCode() {
		return id.hashCode(); 
	}

	@Override
    public boolean equals(Object obj) {
        if(obj instanceof SUCAtable){
        	SUCAtable stu=(SUCAtable)obj;
            if((id!=null&&id.equals(stu.getId()))&&(fileName.equals(stu.fileName))&&(uploadDate==stu.uploadDate)){
                return true;
            }else
                return false;
        }else{
            return false;
        }
    }

}

