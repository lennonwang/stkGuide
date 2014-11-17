package com.sk.bean;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Stock implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 7727875465127847767L;

	private String id;

	private List<DayStock> dayStockList;
	

	private Map<String,DayStock> dayStockMap;
	
	private Map<String,Integer> dayStockIndexMap;

	
	public DayStock getDayStockByIndex(int index) {
		if(index<0 || index>=dayStockList.size()){
			return null;
		}
		return getDayStockList().get(index);
	}

	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public List<DayStock> getDayStockList() {
		return dayStockList;
	}


	public void setDayStockList(List<DayStock> dayStockList) {
		this.dayStockList = dayStockList;
	}


	public Map<String, DayStock> getDayStockMap() {
		return dayStockMap;
	}


	public void setDayStockMap(Map<String, DayStock> dayStockMap) {
		this.dayStockMap = dayStockMap;
	}


	public Map<String, Integer> getDayStockIndexMap() {
		return dayStockIndexMap;
	}


	public void setDayStockIndexMap(Map<String, Integer> dayStockIndexMap) {
		this.dayStockIndexMap = dayStockIndexMap;
	}
	
	 
}
