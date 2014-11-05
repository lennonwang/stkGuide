package com.sk.service;

import com.sk.bean.DayStock;
import com.sk.bean.DayStockMa;
import com.sk.bean.Stock;

public class CountStockMaService {
 
	 
	/**
	 * 初始化股票的平均值
	 * @param stock
	 */
	public static void initStockMa(Stock stock) { 
		if(stock!=null && stock.getDayStockList()!=null) { 
			for(DayStock dayStock :stock.getDayStockList()) { 
				String dayDateString = dayStock.getDate();   
				Integer indexInList = stock.getDayStockIndexMap().get(dayStock.toString());
				
				DayStockMa dsm = new DayStockMa();
				dsm.setDate(dayDateString);
				dsm.setId(dayStock.getId());
				Long volSum5 = dayStock.getVol();
				Long volSum10 =dayStock.getVol(); 
				Long volSum34 =dayStock.getVol();
				double k5 = dayStock.getClosePrice();
				double k10 = dayStock.getClosePrice();
				double k20 = dayStock.getClosePrice();
				double k30 = dayStock.getClosePrice();
				double k60 = dayStock.getClosePrice();
				double k120 = dayStock.getClosePrice();
				double k250 = dayStock.getClosePrice();
				
				if(indexInList!=null){
						if(indexInList>5){
							for(int i=0;i<4;i++){
								DayStock ds = stock.getDayStockList().get(indexInList-(i+1));
								volSum5 = volSum5 + ds.getVol();
								k5 = k5 + ds.getClosePrice();
							} 
							dsm.setVolMa5(volSum5/5);  
							dsm.setPriceMa5(k5/5d);
						} 
						if(indexInList>10){
							for(int i=0;i<9;i++){
								DayStock ds = stock.getDayStockList().get(indexInList-(i+1)); 
								volSum10 = volSum10 + ds.getVol(); 
								k10 = k10 + ds.getClosePrice();
							}  
							dsm.setVolMa10(volSum10/10);
							dsm.setPriceMa10(k10/10d);
						}
						if(indexInList>20){
							for(int i=0;i<19;i++){
								DayStock ds = stock.getDayStockList().get(indexInList-(i+1));  
								k20 = k20 + ds.getClosePrice();
							}
							dsm.setPriceMa20(k20/20d);
						}
						if(indexInList>30){
							for(int i=0;i<29;i++){
								DayStock ds = stock.getDayStockList().get(indexInList-(i+1));  
								k30 = k30 + ds.getClosePrice();
							}
							dsm.setPriceMa30(k30/30d);
						}
						if(indexInList>60){
							for(int i=0;i<59;i++){
								DayStock ds = stock.getDayStockList().get(indexInList-(i+1));  
								k60 = k60 + ds.getClosePrice();
							}
							dsm.setPriceMa60(k60/60d);
						}
						if(indexInList>120){
							for(int i=0;i<119;i++){
								DayStock ds = stock.getDayStockList().get(indexInList-(i+1));  
								k120 = k120 + ds.getClosePrice();
							}
							dsm.setPriceMa120(k120/120d);
						}
						if(indexInList>250){
							for(int i=0;i<249;i++){
								DayStock ds = stock.getDayStockList().get(indexInList-(i+1));  
								k250 = k250 + ds.getClosePrice();
							}
							dsm.setPriceMa250(k250/250d);
						}
						if(indexInList>34){
							for(int i=0;i<33;i++){
								DayStock ds = stock.getDayStockList().get(indexInList-(i+1)); 
								volSum34 = volSum34 + ds.getVol();
							}
							dsm.setVolMa34(volSum34/34);
						}
						
						dayStock.setDayStockMa(dsm);
						stock.getDayStockMap().put(dayStock.toString(), dayStock);
					//	System.out.println("dayStock:"+dayStock.toString()+"\t"+dayStock.getVol()+"\t"+dayStock.getVolMa5()+"\t"+dayStock.getVolMa10()); 
				}
			}
		}
	}
	 
}
