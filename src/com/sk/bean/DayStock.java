package com.sk.bean;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Date;

public class DayStock implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -255554101435612630L;
	private String date;
	private String id;
	private int  index; //索引
	private double openPrice;
	private double closePrice;
	private double maxPrice;
	private double minPrice;


	private DayStockMa dayStockMa;
	private DayStockIndex dayStockIndex;
	private DayStockIndexMacd dayStockIndexMacd;

	private double rise; 
	private double openRise; 
	
	public double getLc(){
		if(yesterdayStock!=null && yesterdayStock.getClosePrice()!=null && closePrice>0){
			double d =  (closePrice-yesterdayStock.getClosePrice());
			DecimalFormat df = new DecimalFormat(".00");
			return Double.parseDouble( df.format(d) );
		}
		return 0;
	}
	
	public double getRise(){
		if(yesterdayStock!=null && yesterdayStock.getClosePrice()!=null && closePrice>0){
			double d =  (closePrice-yesterdayStock.getClosePrice())/yesterdayStock.getClosePrice();
			DecimalFormat df = new DecimalFormat(".0000");
			rise =  Double.parseDouble( df.format(d) );
			return rise;
		}
		return 0;
	} 
	
	public String getRiseRate(){
		if(yesterdayStock!=null && yesterdayStock.getClosePrice()!=null && closePrice>0){
			double d =  (100.0*(closePrice-yesterdayStock.getClosePrice()))/yesterdayStock.getClosePrice();
			return String.format("%.2f", d)+("%");
		}
		return "";
	} 
	
	public Double getRiseRateDouble(){
		if(yesterdayStock!=null && yesterdayStock.getClosePrice()!=null && closePrice>0){
			double d =  (100.0*(closePrice-yesterdayStock.getClosePrice()))/yesterdayStock.getClosePrice();
			return d;
		}
		return 0d;
	} 
	
	public double getOpenRise() { 
		openRise =  (closePrice-openPrice)/openPrice;
		return openRise; 
	}
	
	private Long vol;
	private Long amount; 

	private DayStock nextOneStock ; 
	private DayStock yesterdayStock ; 
	
	 
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Double getOpenPrice() {
		return openPrice;
	}
	public void setOpenPrice(Double openPrice) {
		this.openPrice = openPrice;
	}
	public Double getClosePrice() {
		return closePrice;
	}
	public void setClosePrice(Double closePrice) {
		this.closePrice = closePrice;
	}
	public Double getMaxPrice() {
		return maxPrice;
	}
	public void setMaxPrice(Double maxPrice) {
		this.maxPrice = maxPrice;
	}
	public Double getMinPrice() {
		return minPrice;
	}
	public void setMinPrice(Double minPrice) {
		this.minPrice = minPrice;
	}
	public Long getVol() {
		return vol;
	}
	public void setVol(Long vol) {
		this.vol = vol;
	}
	public Long getAmount() {
		return amount;
	}
	public void setAmount(Long amount) {
		this.amount = amount;
	} 
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
 

	public DayStockMa getDayStockMa() {
		return dayStockMa;
	}

	public void setDayStockMa(DayStockMa dayStockMa) {
		this.dayStockMa = dayStockMa;
	}

	public void setRise(double rise) {
		this.rise = rise;
	}

	public void setOpenRise(double openRise) {
		this.openRise = openRise;
	}

	public DayStock getYesterdayStock() {
		return yesterdayStock;
	}
	public void setYesterdayStock(DayStock yesterdayStock) {
		this.yesterdayStock = yesterdayStock;
	} 
	
	
	public DayStockIndex getDayStockIndex() {
		return dayStockIndex;
	}

	public void setDayStockIndex(DayStockIndex dayStockIndex) {
		this.dayStockIndex = dayStockIndex;
	}

	public DayStock getNextOneStock() {
		return nextOneStock;
	}

	public void setNextOneStock(DayStock nextOneStock) {
		this.nextOneStock = nextOneStock;
	}
 
	public DayStockIndexMacd getDayStockIndexMacd() {
		return dayStockIndexMacd;
	}

	public void setDayStockIndexMacd(DayStockIndexMacd dayStockIndexMacd) {
		this.dayStockIndexMacd = dayStockIndexMacd;
	}

	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public String toString() {		
		return id+"_"+date+"_"+this.closePrice;
	}
}
