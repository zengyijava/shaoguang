package com.montnets.emp.appwg.cache;

import java.util.Date;

import com.montnets.emp.appwg.bean.WgMessage;

public class Window {
	private String sequenceId;
	private Date time;
	private WgMessage message;

	public Window(String sequenceId, WgMessage message, Date time) {
		this.sequenceId = sequenceId;
		this.time = time;
		this.message = message;
	}

	public String getSequenceId() {
		return sequenceId;
	}

	public void setSequenceId(String sequenceId) {
		this.sequenceId = sequenceId;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}
	
	

	public WgMessage getMessage()
	{
		return message;
	}

	public void setMessage(WgMessage message)
	{
		this.message = message;
	}

	@Override
	public boolean equals(Object obj) {
		if(null == obj)
			return false;
		if(obj instanceof Window){
			Window w = (Window)obj;
			if(w.getSequenceId().equals(this.sequenceId))
				return true;
		}else{
			return false;
		}
		return false;
	}

	@Override
	public int hashCode(){
		return super.hashCode();
	}
}