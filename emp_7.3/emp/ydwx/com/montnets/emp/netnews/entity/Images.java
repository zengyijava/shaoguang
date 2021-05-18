package com.montnets.emp.netnews.entity;
import java.io.Serializable;
public class Images implements Serializable{
	private Long id;
    private String name;
    private String url;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Images() {
	}
	public Images(Long id, String name, String url) {
		super();
		this.id = id;
		this.name = name;
		this.url = url;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
}


