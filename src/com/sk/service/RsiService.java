package com.sk.service;

import java.io.File;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import com.sk.bean.DayStock;
import com.sk.bean.DayStockIndex;
import com.sk.bean.DayStockMa;
import com.sk.bean.Stock;
import com.sk.config.Config;
import com.sk.util.MaUtil;
import com.sk.util.MathUtil;


/**
 * RSI＝[上升平均数÷(上升平均数＋下跌平均数)]×100 
 
RSI = 100 - 100/(1 + RS*)
 * *Where RS = Average of x days' up closes / Average of x days' down closes.
 * @author lennon
 * 
LC:=REF(CLOSE,1);
RSI1:SMA(MAX(CLOSE-LC,0),N1,1)/SMA(ABS(CLOSE-LC),N1,1)*100;
RSI2:SMA(MAX(CLOSE-LC,0),N2,1)/SMA(ABS(CLOSE-LC),N2,1)*100;
RSI3:SMA(MAX(CLOSE-LC,0),N3,1)/SMA(ABS(CLOSE-LC),N3,1)*100;

 */
public class RsiService {
	
	
	public static void countStockRsi(Stock stock) {
		  countStockRsi(stock,6);
	}
	
	
	public static void countStockRsi(Stock stock,int rsiNum) {
		rsiNum = (rsiNum<=0)?6:rsiNum;
		if(stock!=null && stock.getDayStockList()!=null) { 
			Map<Integer,Double> rsiMap = new HashMap<Integer,Double>(); 
			Map<String,Double> countMapMap = new HashMap<String,Double>();
			for(DayStock dayStock :stock.getDayStockList()) {    
				Integer indexInList = stock.getDayStockIndexMap().get(dayStock.toString());   
				DayStockIndex dayStockIndex = new DayStockIndex(); 
				dayStockIndex.setDate(dayStock.getDate());
				dayStockIndex.setId(dayStock.getId()); 
				if (indexInList == 0) {
					countMapMap.put(indexInList+"_up", 0d);
					countMapMap.put(indexInList+"_all", 0d);
				}else if (indexInList == 1) { 
					if(dayStock.getRise()>0){ 
						countMapMap.put(indexInList+"_up", dayStock.getLc());
						rsiMap.put(indexInList, 100d);
					} else { 
						countMapMap.put(indexInList+"_up", 0d);
						rsiMap.put(indexInList, 0d);
					}  
					countMapMap.put(indexInList+"_all", Math.abs(dayStock.getLc()));
				}else { 
					double dsClose = dayStock.getLc();
					double dsUpClose = Math.max(dsClose, 0);
					double dsAllClose = Math.abs(dsClose);  
					double preUp = countMapMap.get((indexInList-1)+"_up"); 
					double preAll = countMapMap.get((indexInList-1)+"_all");
					double currentUp =   (dsUpClose +(rsiNum-1)*preUp) /rsiNum    ;   
					double currentAll =   (dsAllClose +(rsiNum-1)*preAll) /rsiNum    ;  
					double currentRsi =  0;
					if(currentAll>0){
						currentRsi = MathUtil.format((currentUp/currentAll)*100) ; 
					}else{
						// System.out.println("dayStock currentAll-0:"+dayStock+"\t indexInList"+indexInList);
					}
					rsiMap.put(indexInList, currentRsi);
					
					countMapMap.put(indexInList+"_up", currentUp);
					countMapMap.put(indexInList+"_all", currentAll);   
				}
				if(rsiMap.get(indexInList)!=null){
					dayStockIndex.setRsi6(rsiMap.get(indexInList));
				}
				dayStock.setDayStockIndex(dayStockIndex);
				stock.getDayStockMap().put(dayStock.toString(), dayStock);
			 //	System.out.println("dayStock rsi:"+dayStock+"\t rsi"+rsiNum+"="+rsiMap.get(indexInList));
			} 
		}
	}
	 
	
	public static void main(String[] args) {
		Stock stock = null;
		//	stock = ExportStock.exportStock("002066");
		  // stock = ExportStock.exportStock("SH601801");
		 //  stock = ExportStock.exportStock("SH600217");
		  stock = BuildStockService.exportStock("002721");
		//   checkStockVol(stock); 
		  countStockRsi(stock,6);
		//  MaUtil.emaStockClose(stock, 2);
	}
	
	/**
	 * 
	 * @param dayStock
	 * @return
	 */
	public static boolean checkRsi(Stock stock,DayStock dayStock){ 
		int dayStockIndex = dayStock.getIndex();
		double currentRsi = dayStock.getDayStockIndex().getRsi6(); 
		double preDayRsi = dayStock.getYesterdayStock().getDayStockIndex().getRsi6(); 
		double beforePreDayRsi = stock.getDayStockByIndex(dayStockIndex-2).getDayStockIndex().getRsi6(); 
		boolean flag =  false;
	//	flag = currentRsi<80 && currentRsi>71  && preDayRsi >70 && beforePreDayRsi>65 && preDayRsi>beforePreDayRsi ;

	  flag = currentRsi<80 && currentRsi>72
						&& preDayRsi < 80 && preDayRsi >65 && beforePreDayRsi>60 && preDayRsi>beforePreDayRsi ;
		double lastThree =  dayStock.getRiseRateDouble()+dayStock.getYesterdayStock().getRiseRateDouble()+stock.getDayStockByIndex(dayStockIndex-2).getRiseRateDouble();
		if(flag) { 
		
			if(dayStock.getRiseRateDouble() >0.2 && dayStock.getRiseRateDouble()<3.5 && lastThree<6.3){
				System.out.println("dayStock.getRiseRateDouble():"+dayStock+"\t"+dayStock.getRiseRateDouble());
				flag = true;
			} else {
				flag = false;
			}
		}
	//	System.out.println("昨天rsi:"+currentRsi+"\t"+preDayRsi+"\t"+beforePreDayRsi);
		return flag;
	}
	
	
	/**
	 * 
	 * @param dayStock
	 * @return
	 */
	public static boolean checkDiLiangRsi(Stock stock,DayStock dayStock){ 
		int dayStockIndex = dayStock.getIndex();
		double currentRsi = dayStock.getDayStockIndex().getRsi6(); 
		double preDayRsi = dayStock.getYesterdayStock().getDayStockIndex().getRsi6(); 
		double beforePreDayRsi = stock.getDayStockByIndex(dayStockIndex-2).getDayStockIndex().getRsi6(); 
		boolean flag =  false; 

	   if(currentRsi<50 && preDayRsi<50 && beforePreDayRsi<50){
		   return false;
	   }  
	   if(currentRsi + preDayRsi  +  beforePreDayRsi > 213){
		   return false;
	   }  
		return true;
	}
	
	public static boolean checkBigVolRsi(Stock stock,DayStock dayStock){ 
		int dayStockIndex = dayStock.getIndex();
		double currentRsi = dayStock.getDayStockIndex().getRsi6(); 
		double preDayRsi = dayStock.getYesterdayStock().getDayStockIndex().getRsi6(); 
		double beforePreDayRsi = stock.getDayStockByIndex(dayStockIndex-2).getDayStockIndex().getRsi6(); 
		boolean flag =  true;  
	   if(currentRsi<50 && preDayRsi<50 && beforePreDayRsi<50){
		   return false;
	   }  
	   if(currentRsi + preDayRsi  +  beforePreDayRsi < 210){
		   return false;
	   }  
	   if(currentRsi + preDayRsi  +  beforePreDayRsi > 230){
		   return false;
	   }  
		return flag;
	}
	
	/**
	 * 
	 * @param dayStock
	 * @return
	 */
	public static boolean checkSmallRsi(Stock stock,DayStock dayStock){ 
		int dayStockIndex = dayStock.getIndex();
		double currentRsi = dayStock.getDayStockIndex().getRsi6(); 
		double preDayRsi = dayStock.getYesterdayStock().getDayStockIndex().getRsi6(); 
		double beforePreDayRsi = stock.getDayStockByIndex(dayStockIndex-2).getDayStockIndex().getRsi6(); 
		boolean flag =  false;
	//	flag = currentRsi<80 && currentRsi>71  && preDayRsi >70 && beforePreDayRsi>65 && preDayRsi>beforePreDayRsi ;

	  flag = currentRsi<81 && currentRsi>70
						&& preDayRsi < 80 && preDayRsi >65 && beforePreDayRsi>60  ;
		double lastThree =  dayStock.getRiseRateDouble()+dayStock.getYesterdayStock().getRiseRateDouble()+stock.getDayStockByIndex(dayStockIndex-2).getRiseRateDouble();
		if(flag) { 
		/*
			if(dayStock.getRiseRateDouble() >0.2 && dayStock.getRiseRateDouble()<3.5 && lastThree<6.3){
				System.out.println("dayStock.getRiseRateDouble():"+dayStock+"\t"+dayStock.getRiseRateDouble());
				flag = true;
			} else {
				flag = false;
			}
			*/
		}
	//	System.out.println("昨天rsi:"+currentRsi+"\t"+preDayRsi+"\t"+beforePreDayRsi);
		return flag;
	}
}
