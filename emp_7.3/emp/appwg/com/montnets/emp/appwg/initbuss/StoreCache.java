package com.montnets.emp.appwg.initbuss;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class StoreCache{
	public final List<HashSet<String>> 	cacheList		= new ArrayList<HashSet<String>>();

	private  int		curIndex		= 0;

	private  int		lastIndex		= 0;

	private  long		start			= 0L;

	public int getCurIndex() {
		return curIndex;
	}

	public void setCurIndex(int curIndex) {
		this.curIndex = curIndex;
	}

	public int getLastIndex() {
		return lastIndex;
	}

	public void setLastIndex(int lastIndex) {
		this.lastIndex = lastIndex;
	}

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public StoreCache() {
		for (int i = 0; i < 10; i++)
		{
			cacheList.add(new HashSet<String>());
		}
	}
	
}
