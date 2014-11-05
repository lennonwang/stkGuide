package com.sk.bean;

import java.util.Arrays;
import java.util.Date;

public class DayStock {

	private String date;
	private String id;
	private double openPrice;
	private double closePrice;
	private double maxPrice;
	private double minPrice;
	
	private DayStockMa dayStockMa;

	private double rise; 
	private double openRise; 
	
	public double getRise(){
		if(yesterdayStock!=null && yesterdayStock.getClosePrice()!=null && closePrice>0){
			double d =  (closePrice-yesterdayStock.getClosePrice())/yesterdayStock.getClosePrice();
			return d;
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
	
	
	public double getOpenRise() { 
		double d =  (closePrice-openPrice)/openPrice;
		return d; 
	}
	
	private Long vol;
	private Long amount; 

	private DayStock nextOneStock ;
	private DayStock nextTwoStock ;
	private DayStock nextThreeStock ;
	private DayStock nextFourStock ;
	private DayStock nextFiveStock ;

	private DayStock yesterdayStock ;
	private DayStock beforeYesterdayStock ;
	
	 
	
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
	public DayStock getBeforeYesterdayStock() {
		return beforeYesterdayStock;
	}
	public void setBeforeYesterdayStock(DayStock beforeYesterdayStock) {
		this.beforeYesterdayStock = beforeYesterdayStock;
	}
	
	
	
	
	public DayStock getNextOneStock() {
		return nextOneStock;
	}

	public void setNextOneStock(DayStock nextOneStock) {
		this.nextOneStock = nextOneStock;
	}

	public DayStock getNextTwoStock() {
		return nextTwoStock;
	}

	public void setNextTwoStock(DayStock nextTwoStock) {
		this.nextTwoStock = nextTwoStock;
	}

	public DayStock getNextThreeStock() {
		return nextThreeStock;
	}

	public void setNextThreeStock(DayStock nextThreeStock) {
		this.nextThreeStock = nextThreeStock;
	}

	public DayStock getNextFourStock() {
		return nextFourStock;
	}

	public void setNextFourStock(DayStock nextFourStock) {
		this.nextFourStock = nextFourStock;
	}

	public DayStock getNextFiveStock() {
		return nextFiveStock;
	}

	public void setNextFiveStock(DayStock nextFiveStock) {
		this.nextFiveStock = nextFiveStock;
	}

	@Override
	public String toString() {		
		return id+"_"+date+"_"+this.closePrice;
	}
}
