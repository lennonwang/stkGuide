package com.sk.bean;

import java.io.Serializable;


public class DayStockMa implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8101933112318269772L;
	private String date;
	private String id; 
	
	
	private long volMa5;
	private long volMa10;
	private long volMa34;
	
	private double priceMa5;
	private double priceMa10; 
	private double priceMa20; 
	private double priceMa30; 
	private double priceMa60; 
	private double priceMa120; 
	private double priceMa250; 

	
	
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



	public Long getVolMa5() {
		return volMa5;
	}



	public void setVolMa5(Long volMa5) {
		this.volMa5 = volMa5;
	}



	public Long getVolMa10() {
		return volMa10;
	}



	public void setVolMa10(Long volMa10) {
		this.volMa10 = volMa10;
	}



	public Long getVolMa34() {
		return volMa34;
	}



	public void setVolMa34(Long volMa34) {
		this.volMa34 = volMa34;
	}



	public double getPriceMa5() {
		return priceMa5;
	}



	public void setPriceMa5(double priceMa5) {
		this.priceMa5 = priceMa5;
	}



	public double getPriceMa10() {
		return priceMa10;
	}



	public void setPriceMa10(double priceMa10) {
		this.priceMa10 = priceMa10;
	}



	public double getPriceMa20() {
		return priceMa20;
	}



	public void setPriceMa20(double priceMa20) {
		this.priceMa20 = priceMa20;
	}



	public double getPriceMa30() {
		return priceMa30;
	}



	public void setPriceMa30(double priceMa30) {
		this.priceMa30 = priceMa30;
	}



	public double getPriceMa60() {
		return priceMa60;
	}



	public void setPriceMa60(double priceMa60) {
		this.priceMa60 = priceMa60;
	}



	public double getPriceMa120() {
		return priceMa120;
	}



	public void setPriceMa120(double priceMa120) {
		this.priceMa120 = priceMa120;
	}



	public double getPriceMa250() {
		return priceMa250;
	}



	public void setPriceMa250(double priceMa250) {
		this.priceMa250 = priceMa250;
	}



	@Override
	public String toString() {		
		return "ma_"+id+"_"+date;
	}
}
