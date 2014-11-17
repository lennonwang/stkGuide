package com.sk.bean;

import java.io.Serializable;

public class DayStockIndexMacd implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1897509704990603641L;
	private String date;
	private String id; 
	
	private double macdDiff;
	private double macdDea; 
	private double macd; 
	  
	 


	public String getDate() {
		return date;
	}




	public void setDate(String date) {
		this.date = date;
	}




	public String getId() {
		return id;
	}




	public void setId(String id) {
		this.id = id;
	}




	public double getMacdDiff() {
		return macdDiff;
	}




	public void setMacdDiff(double macdDiff) {
		this.macdDiff = macdDiff;
	}




	public double getMacdDea() {
		return macdDea;
	}




	public void setMacdDea(double macdDea) {
		this.macdDea = macdDea;
	}




	public double getMacd() {
		return macd;
	}




	public void setMacd(double macd) {
		this.macd = macd;
	}




	@Override
	public String toString() {		
		return "macd_"+id+"_"+date;
	}
}

