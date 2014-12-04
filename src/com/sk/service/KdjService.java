package com.sk.service;

import java.io.File;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.sk.bean.DayStock;
import com.sk.bean.DayStockIndex;
import com.sk.bean.DayStockIndexMacd;
import com.sk.bean.DayStockMa;
import com.sk.bean.Stock;
import com.sk.config.Config;
import com.sk.util.MaUtil;
import com.sk.util.MathUtil;


/***
 * kdj指标
RSV:=(CLOSE-LLV(LOW,N))/(HHV(HIGH,N)-LLV(LOW,N))*100;
K:SMA(RSV,M1,1);
D:SMA(K,M2,1);
J:3*K-2*D; 
 */
public class KdjService {
	
	
	public void countStockKdj(Stock stock) {
		  countStockKdj(stock,9,3,3);
	}
	
	
	public  void countStockKdj(Stock stock,int n,int m1,int m2) { 
		if(stock!=null && stock.getDayStockList()!=null) {  

			Map<String,Double> countMap = new HashMap<String,Double>();
			for(DayStock dayStock :stock.getDayStockList()) {    
				int indexInList =dayStock.getIndex();  
				double maxHigh =dayStock.getMaxPrice();
				double minLow = dayStock.getMinPrice();
				for(int i=1;i<n;i++){
					if(indexInList-i<0){
						continue;
					}
					DayStock stockItem = stock.getDayStockList().get(indexInList-i);
					maxHigh = Math.max(maxHigh, stockItem.getMaxPrice());
					minLow = Math.min(minLow, stockItem.getMinPrice());
				}
			//	System.out.println("min="+dayStock+"\t"+minLow+"\t"+maxHigh +"\t"+dayStock.getClosePrice());
				double rsv = (dayStock.getClosePrice()-minLow)/(maxHigh-minLow)*100;
				if(maxHigh-minLow==0){
					rsv = 100;
				}
				double k = 0d;
				double d = 0d;
				double j = 0d;
				DayStockIndex dayStockIndex = dayStock.getDayStockIndex();
				if(dayStockIndex==null &&  StringUtils.isNotBlank(dayStockIndex.getId())){
					dayStockIndex = new DayStockIndex();  
					dayStockIndex.setDate(dayStock.getDate());
					dayStockIndex.setId(dayStock.getId()); 
				}
				if (indexInList == 0) { 
					k = rsv;
					d = k;
					j = 3*k - 2*d;
					dayStockIndex.setKdjRsv(rsv);
					dayStockIndex.setKdjK(k);
					dayStockIndex.setKdjD(d);
					dayStockIndex.setKdjJ(j);
					countMap.put(indexInList+"_k", k);
					countMap.put(indexInList+"_d", d);
				}else { 
					k = ( rsv + ( (m1-1)* countMap.get( (indexInList-1)+"_k")) )  / m1;
					d = ( k + ( (m2-1)*   countMap.get( (indexInList-1)+"_d")) )  / m2; 
					j = 3*k - 2*d;
					dayStockIndex.setKdjRsv(rsv);
					dayStockIndex.setKdjK(k);
					dayStockIndex.setKdjD(d);
					dayStockIndex.setKdjJ(j); 
					countMap.put(indexInList+"_k", k);
					countMap.put(indexInList+"_d", d);
				} 
		//	 	System.out.println("kdj: rsv="+rsv+"\t k="+k+"\t d="+d+"\t j="+j);
				int kdjPointNumber = getKdjBuyAfterNumber(stock,dayStock);
				dayStockIndex.setKdjPointNumber(kdjPointNumber);
		//		System.out.println("kdj: "+dayStock+" \t kdjPointNumber="+kdjPointNumber+"\t k="+k+"\t d="+d+"\t j="+j);
				dayStock.setDayStockIndex(dayStockIndex);  
			} 
		}
	}
	 
	/**
	 * 以K从下向上与D交叉为例：K上穿D是金叉，为买入信号
	 * @param stock
	 * @param dayStock
	 * @return
	 */
	public boolean checkKdjPoint(Stock stock, DayStock dayStock){ 
		if(dayStock==null){
			return false;
		}
    	DayStock yesterDayStock = stock.getDayStockByIndex(dayStock.getIndex()-1);
    	if(yesterDayStock==null){
    		return false;
    	}

    	DayStock yyesterDayStock =  stock.getDayStockByIndex(dayStock.getIndex()-2);
    	if(yyesterDayStock==null){
    		return false;
    	}
    	if(dayStock.getDayStockIndex().getKdjK() > dayStock.getDayStockIndex().getKdjD()){
    		if(yesterDayStock.getDayStockIndex().getKdjK() < yesterDayStock.getDayStockIndex().getKdjD()){
    			if(yyesterDayStock.getDayStockIndex().getKdjK() < yyesterDayStock.getDayStockIndex().getKdjD()){
    				return true;
    			}
    		}
    	}
    	return false; 
	}
	
	
	public  int getKdjBuyAfterNumber(Stock stock,DayStock dayStock){ 
		if(dayStock.getDayStockIndex().getKdjK() < dayStock.getDayStockIndex().getKdjD()){
			return -1;
		}
		if(this.checkKdjPoint(stock,dayStock)){
			return 1;
		}
		for(int i=1;i<100;i++){
			DayStock yDayStockItem = stock.getDayStockByIndex(dayStock.getIndex()-i);
			if(checkKdjPoint(stock,yDayStockItem)){
				return i+1;
			}
		}
		return 0;
	}
	
	public static void main(String[] args) {
		Stock stock = null;
		//	stock = ExportStock.exportStock("002066");
		  // stock = ExportStock.exportStock("SH601801");
		 //  stock = ExportStock.exportStock("SH600217");
		  stock = BuildStockService.exportStock("002721");
		//   checkStockVol(stock); 
		  new KdjService().countStockKdj(stock);
		//  MaUtil.emaStockClose(stock, 2);
	} 
}
