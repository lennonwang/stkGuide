package com.sk.bean;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Stock {


	private String id;

	private List<DayStock> dayStockList;
	

	private Map<String,DayStock> dayStockMap;
	
	private Map<String,Integer> dayStockIndexMap;


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
