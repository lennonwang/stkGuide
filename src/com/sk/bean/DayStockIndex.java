package com.sk.bean;

import java.io.Serializable;

public class DayStockIndex implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7210201390455888411L;
	private String date;
	private String id;
	private String type ="day"; 
	
	private double rsi6; 
	
	private double exp7; 
	
	private double macdDiff;
	private double macdDea;
	
	  
	
	
	
	public double getExp7() {
		return exp7;
	}



	public void setExp7(double exp7) {
		this.exp7 = exp7;
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



	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
	}



	public double getRsi6() {
		return rsi6;
	}



	public void setRsi6(double rsi6) {
		this.rsi6 = rsi6;
	}



	@Override
	public String toString() {		
		return "index_"+id+"_"+date;
	}
}

