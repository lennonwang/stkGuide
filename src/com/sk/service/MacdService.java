package com.sk.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sk.bean.DayStock;
import com.sk.bean.DayStockIndexMacd;
import com.sk.bean.Stock;
import com.sk.util.MaUtil;
import com.sk.util.MathUtil;


/**
EMA（12）= 前一日EMA（12）×11/13＋今日收盘价×2/13
EMA（26）= 前一日EMA（26）×25/27＋今日收盘价×2/27
DIFF=今日EMA（12）- 今日EMA（26）
DIFF=EMA（SHORT）－EMA（LONG）
DEA（MACD）= 前一日DEA×8/10＋今日DIF×2/10 
BAR=2×(DIFF－DEA)


DIF:EMA(CLOSE,SHORT)-EMA(CLOSE,LONG);
DEA:EMA(DIF,MID);
MACD:(DIF-DEA)*2,COLORSTICK;

dif dea macd

 */
public class MacdService {
	
	
	public static void countStockMacd(Stock stock) {
		countStockMacd(stock,12,26,10);
	}
	
	/**
	 * 
	 * @param dayStock
	 * @return
	 */
	public static boolean checkMacd(Stock stock,DayStock dayStock){
		double diff = dayStock.getDayStockIndexMacd().getMacdDiff();
		double dea = dayStock.getDayStockIndexMacd().getMacdDea();
		double macd = dayStock.getDayStockIndexMacd().getMacd();
		
		double pre_diff = dayStock.getYesterdayStock().getDayStockIndexMacd().getMacdDiff();
		double pre_dea = dayStock.getYesterdayStock().getDayStockIndexMacd().getMacdDea();
		double pre_macd = dayStock.getYesterdayStock().getDayStockIndexMacd().getMacd();

		double before_pre_diff=0d;
		double before_pre_dea=0d;
		double before_pre_macd=0d;
		if(stock.getDayStockByIndex(dayStock.getIndex()-2)!=null){
			  before_pre_diff =stock.getDayStockByIndex(dayStock.getIndex()-2).getDayStockIndexMacd().getMacdDiff();
			  before_pre_dea = stock.getDayStockByIndex(dayStock.getIndex()-2).getDayStockIndexMacd().getMacdDea();
			  before_pre_macd = stock.getDayStockByIndex(dayStock.getIndex()-2).getDayStockIndexMacd().getMacd();
		}
	//	if(diff>dea && diff >-0.02   && ( pre_diff <pre_dea || before_pre_diff <before_pre_dea )){
			if(diff>dea && diff >-0.02   && ( pre_diff <pre_dea || before_pre_diff <before_pre_dea )){
			 return true;
		}
		return false;
	}
	
	
	public static boolean checkMacdRed(DayStock dayStock){
		double diff = dayStock.getDayStockIndexMacd().getMacdDiff();
		double dea = dayStock.getDayStockIndexMacd().getMacdDea();
		double macd = dayStock.getDayStockIndexMacd().getMacd();
		

		double pre_diff = dayStock.getYesterdayStock().getDayStockIndexMacd().getMacdDiff();
		double pre_dea = dayStock.getYesterdayStock().getDayStockIndexMacd().getMacdDea();
		double pre_macd = dayStock.getYesterdayStock().getDayStockIndexMacd().getMacd();
		
	//		if(macd>0 && macd> pre_macd && diff>0 ){
		if(  macd > pre_macd && diff>-0.1 && diff >dea ){ 
		//	 if(  macd > pre_macd && diff>-0.1 && diff >dea ){ 
			return true;
		}
		return false;
	}
	
	
	public static boolean checkDiLiangMacd (Stock stock,DayStock dayStock){
		double diff = dayStock.getDayStockIndexMacd().getMacdDiff();
		double dea = dayStock.getDayStockIndexMacd().getMacdDea();
		double macd = dayStock.getDayStockIndexMacd().getMacd();
		

		double pre_diff = dayStock.getYesterdayStock().getDayStockIndexMacd().getMacdDiff();
		double pre_dea = dayStock.getYesterdayStock().getDayStockIndexMacd().getMacdDea();
		double pre_macd = dayStock.getYesterdayStock().getDayStockIndexMacd().getMacd();
		
		
		double before_pre_diff=0d;
		double before_pre_dea=0d;
		double before_pre_macd=0d;
		if(stock.getDayStockByIndex(dayStock.getIndex()-2)!=null){
			  before_pre_diff =stock.getDayStockByIndex(dayStock.getIndex()-2).getDayStockIndexMacd().getMacdDiff();
			  before_pre_dea = stock.getDayStockByIndex(dayStock.getIndex()-2).getDayStockIndexMacd().getMacdDea();
			  before_pre_macd = stock.getDayStockByIndex(dayStock.getIndex()-2).getDayStockIndexMacd().getMacd();
		}
	//		if(macd>0 && macd> pre_macd && diff>0 ){
		if(  macd <0 && before_pre_macd<0 && pre_macd > macd &&  before_pre_macd > pre_macd ){ 
		//	 if(  macd > pre_macd && diff>-0.1 && diff >dea ){ 
			return false;
		}
		return true;
	}
	
	public static void countStockMacd(Stock stock,int shortNum,int longNum,int deaNum) { 
		if(stock!=null && stock.getDayStockList()!=null) { 
			Map<Integer,Double> shortEmaMap  = StockMaService.emaStockClose(stock, shortNum);
			Map<Integer,Double> longNumEmaMap  = StockMaService.emaStockClose(stock, longNum);
			
			Map<Integer,DayStockIndexMacd> macdMap = new HashMap<Integer,DayStockIndexMacd>();  
			List<Double> diffList  = new ArrayList<Double>();
			for(DayStock dayStock :stock.getDayStockList()) {    
				DayStockIndexMacd indexMacd = new  DayStockIndexMacd(); 
				Integer indexInList = stock.getDayStockIndexMap().get(dayStock.toString());    
				indexMacd.setDate(dayStock.getDate());
				indexMacd.setId(dayStock.getId()); 
				if (indexInList == 0) { 
					indexMacd.setMacd(0d);
					indexMacd.setMacdDea(0d);
					indexMacd.setMacdDiff(0d);	
					indexMacd.setMacdDiff(0);
					diffList.add(0d);  
					macdMap.put(indexInList, indexMacd); 
				//	System.out.println("macdDiff=="+dayStock+"\t"+0d+"\t"+dayStock.getClosePrice()+"\t"+dayStock.getClosePrice());
				}else {  
					double emaShort = shortEmaMap.get(indexInList); 
					double emaLong = longNumEmaMap.get(indexInList);  
					double macdDiff = (emaShort - emaLong) ;  
					indexMacd.setMacdDiff(macdDiff);
					diffList.add(macdDiff);
					macdMap.put(indexInList, indexMacd);
				//	System.out.println("macdDiff=="+dayStock+"\t"+macdDiff+"\t"+emaShort+"\t"+MathUtil.format(emaShort)+"\t"+emaLong);
				} 
				dayStock.setDayStockIndexMacd(indexMacd); 
			 //	System.out.println("dayStock rsi:"+dayStock+"\t rsi"+rsiNum+"="+rsiMap.get(indexInList));
			} 
			Map<Integer,Double> emaEmaMap  = MaUtil.emaStockClose(diffList, deaNum);
			for(DayStock dayStock :stock.getDayStockList()) {    
				DayStockIndexMacd indexMacd = dayStock.getDayStockIndexMacd(); 
				Integer indexInList = stock.getDayStockIndexMap().get(dayStock.toString());    
				double macdDea = emaEmaMap.get(indexInList);
				indexMacd.setMacdDea(macdDea);
				double macd =2*(indexMacd.getMacdDiff()-macdDea);
				indexMacd.setMacd(MathUtil.format(macd));
				indexMacd.setMacdDea(MathUtil.format(macdDea));
				indexMacd.setMacdDiff(MathUtil.format(indexMacd.getMacdDiff())); 
			//	System.out.println("dayStock"+dayStock+" diff:"+indexMacd.getMacdDiff()+"\t dea:"+indexMacd.getMacdDea()+"; macd="+indexMacd.getMacd());
			}
		}
	}
	 
	
	public static void main(String[] args) {
		Stock stock = null;
		//	stock = ExportStock.exportStock("002066");
		  // stock = ExportStock.exportStock("SH601801");
		 //  stock = BuildStockService.exportStock("SH600217");
		   stock = BuildStockService.exportStock("603288");
		//   checkStockVol(stock); 
		  countStockMacd(stock);
		//  MaUtil.emaStockClose(stock, 2);
	}
}
