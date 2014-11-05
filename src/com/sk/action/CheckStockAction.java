package com.sk.action;

import java.io.BufferedReader;
import java.io.File; 
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateUtils;

import com.sk.bean.DayStock;
import com.sk.bean.Stock;
import com.sk.config.Config;
import com.sk.service.BuildStockService;
import com.sk.service.CountStockMaService;
import com.sk.util.DateUtil;
import com.sk.util.FileUtil;

public class CheckStockAction {
 
	
	public static int checkResultSuccess =0;
	public static int checkResultFail =0;
	
	 
	
	/**
	 * 检查股票成交量
	 * @param stock
	 */
	public static int checkStockVol(Stock stock) { 
		int m=0;
		if(stock!=null && stock.getDayStockList()!=null) {
			for(DayStock dayStock :stock.getDayStockList()) {
			 
				if(dayStock!=null&& dayStock.getBeforeYesterdayStock()!=null&& dayStock.getYesterdayStock()!=null
						&& dayStock.getYesterdayStock().getDayStockMa().getVolMa5()!=null && dayStock.getBeforeYesterdayStock().getDayStockMa().getVolMa5()!=null){
					if(!dayStock.getDate().startsWith("2014-07-07")){
						continue;
					}
					if(dayStock.getDayStockMa().getVolMa5()>dayStock.getYesterdayStock().getDayStockMa().getVolMa5()){
					//	if(dayStock.getYesterdayStock().getVolMa5()>dayStock.getBeforeYesterdayStock().getVolMa5()){
					 //	System.out.println("checkStockVol:"+dayStock.toString()+"\t"+dayStock.getVol()
					 //		+"\t"+dayStock.getVolMa5()+"\t"+dayStock.getVolMa10()); 
							
						//	System.out.println("昨天的五日成交量:"+dayStock.getYesterdayStock()+"\t"+dayStock.getYesterdayStock().getVolMa5()
					//				+"\t 前天的五日成交量:"+dayStock.getBeforeYesterdayStock()+"\t"+dayStock.getBeforeYesterdayStock().getVolMa5()); 
					
							double d1= (double)  (dayStock.getDayStockMa().getVolMa5()*1000)/(dayStock.getYesterdayStock().getDayStockMa().getVolMa5()*1000.0);   
							double d2= (double)  (dayStock.getYesterdayStock().getDayStockMa().getVolMa5()*1000)/(dayStock.getBeforeYesterdayStock().getDayStockMa().getVolMa5()*1000.0);  
						 	 
							double d3= (double)  (dayStock.getDayStockMa().getVolMa10()*1000)/(dayStock.getYesterdayStock().getDayStockMa().getVolMa10()*1000.0);   
						//	
							double d4= (double)  (dayStock.getVol()*1000)/(dayStock.getYesterdayStock().getVol()*1000.0);   
							double d5= (double)  (dayStock.getYesterdayStock().getVol()*1000)/(dayStock.getBeforeYesterdayStock().getVol()*1000.0);   
							
							double ma20rise= (double)  ((dayStock.getClosePrice() - dayStock.getDayStockMa().getPriceMa20())*100)/(dayStock.getClosePrice()*1.0);   
							double ma60rise= (double)  ((dayStock.getClosePrice() - dayStock.getDayStockMa().getPriceMa60())*100)/(dayStock.getClosePrice()*1.0);   
							
							double mavol34 = (double)  (dayStock.getVol()*1000)/(dayStock.getYesterdayStock().getDayStockMa().getVolMa34()*1000.0);   
							double ma20dapan =( (2430-2362)*100/2430d); 
							double ma60dapan =( (2430-2297)*100/2430d);
							
							 boolean check1 = d1>1.10d && d1<1.40d && d2>1.00d && d2<1.40d && d3>1.00d && d1+0.05>=d2;
							 boolean check2 =  (d1>1.05 && d1<1.80d && d2<0.97 && d2>0.7 ) ; 
							 boolean check3 = ((d1 >1.2 && d1<2.0d ) ||(mavol34>1.05d && mavol34<2.5));
							 //
							 boolean check4 = dayStock.getClosePrice()>dayStock.getDayStockMa().getPriceMa120() 
										&& dayStock.getClosePrice()>dayStock.getDayStockMa().getPriceMa250();
										
							boolean check5= dayStock.getVol()> dayStock.getDayStockMa().getVolMa34();
							boolean check6= dayStock.getYesterdayStock().getVol() <= dayStock.getYesterdayStock().getDayStockMa().getVolMa34();
							
							boolean check7 = (dayStock.getRise()>-0.01 && dayStock.getRise()<0.06);
							
							boolean dayMa60And20 = ( (dayStock.getClosePrice()>dayStock.getDayStockMa().getPriceMa60() )
									&& ( dayStock.getClosePrice()*1.03>dayStock.getDayStockMa().getPriceMa20()));
							if(  ma20rise>ma20dapan  && ma60rise > ma60dapan )
							if(   check4 && check5 && check6 && ( check1 || check2 || check3 ) &&   dayMa60And20 && check7) {
								System.out.println("------------------------------------------");
								System.out.println("check 当天:"+dayStock+"\t"+dayStock.getRiseRate() );  
								System.out.println("大盘 当天:"+String.format("%.2f", ma20dapan)+("%") +"\t"+String.format("%.2f", ma20rise)+("%") +"\t 大盘60:"+String.format("%.2f", ma60dapan)+("%") +"\t"+String.format("%.2f", ma60rise)+("%")
										);  
								if(check1){
									System.out.print("成交量缓慢上涨 \t");
								}
								if(check2){
									System.out.print("暴利拐头 \t");
								}
								if(check3){
									System.out.print("成交量暴涨；ma34:"+String.format("%.2f", mavol34)+"倍 ；五日均线今天是昨天的倍数："+String.format("%.2f", d1)+"\t");
								} 
								if(dayStock.getClosePrice() * 1.08 >dayStock.getDayStockMa().getPriceMa60() 
										&& dayStock.getClosePrice() * 0.95 < dayStock.getDayStockMa().getPriceMa60() ){
									System.out.print("60日均线附近："+ String.format("%.2f", ma60rise)+("%") +"\t");
								}  else {
									System.out.print("60日均线较远："+String.format("%.2f", ma60rise)+("%") +"\t");
								} 
								if(dayStock.getClosePrice() * 1.08 >dayStock.getDayStockMa().getPriceMa20() 
										&& dayStock.getClosePrice() * 0.95 < dayStock.getDayStockMa().getPriceMa20() ){
									System.out.print("20日均线附近："+ String.format("%.2f", ma20rise)+("%") +"\t");
								}  else {
									System.out.print("20日均线较远："+String.format("%.2f", ma20rise)+("%") +"\t");
								}
								System.out.println("success="+ check1+"\t"+check2 +"\t"+String.format("%.2f", d1) +"\t "+String.format("%.2f", d2) +"\t "+String.format("%.2f", d3)+"\t"+String.format("%.2f", d4) +"\t "+String.format("%.2f", d5));
								System.out.println("checkStockVol:"+dayStock.toString()+"\t"+dayStock.getVol()
									 	+"\t"+dayStock.getDayStockMa().getVolMa5()+"\t"+dayStock.getDayStockMa().getVolMa10());  
								System.out.println("checkStockPrice:"+dayStock.toString()+"\t"+dayStock.getClosePrice()
									 	+"\t"+dayStock.getDayStockMa().getPriceMa5()+"\t"+ String.format("%.2f", dayStock.getDayStockMa().getPriceMa60()));  
							/*	
							 System.out.println("昨天的五日成交量:"+dayStock.getYesterdayStock()+"\t"+dayStock.getYesterdayStock().getVolMa5()
										 				+"\t 前天的五日成交量:"+dayStock.getBeforeYesterdayStock()+"\t"+dayStock.getBeforeYesterdayStock().getVolMa5()); 
								System.out.println("昨天的十日成交量:"+dayStock.getYesterdayStock()+"\t"+dayStock.getYesterdayStock().getVolMa10()
												 				+"\t 前天的十日成交量:"+dayStock.getBeforeYesterdayStock()+"\t"+dayStock.getBeforeYesterdayStock().getVolMa10());
								*/
								double checkresult5 = 0;
								double checkresult2 = 0;
								if(dayStock.getNextOneStock()!=null){ 
									System.out.println("后一天:"+dayStock.getNextOneStock()+"\t"+dayStock.getNextOneStock().getRiseRate());
									checkresult5 = checkresult5 + dayStock.getNextOneStock().getRise();
									checkresult5 = checkresult5 + dayStock.getNextOneStock().getRise();
								}
								if(dayStock.getNextTwoStock()!=null){ 
									System.out.println("后二天:"+dayStock.getNextTwoStock()+"\t"+dayStock.getNextTwoStock().getRiseRate());
									checkresult5 = checkresult5 + dayStock.getNextTwoStock().getRise();
									checkresult2 = checkresult2 + dayStock.getNextTwoStock().getRise();
								}
								if(dayStock.getNextThreeStock()!=null){ 
									System.out.println("后三天:"+dayStock.getNextThreeStock()+"\t"+dayStock.getNextThreeStock().getRiseRate());
									checkresult5 = checkresult5 + dayStock.getNextThreeStock().getRise();
								}
								if(dayStock.getNextFourStock()!=null){ 
									System.out.println("后四天:"+dayStock.getNextFourStock()+"\t"+dayStock.getNextFourStock().getRiseRate()); 
									checkresult5 = checkresult5 + dayStock.getNextFourStock().getRise();
								}
								if(dayStock.getNextFiveStock()!=null){ 
									System.out.println("后五天:"+dayStock.getNextFiveStock()+"\t"+dayStock.getNextFiveStock().getRiseRate()); 
									checkresult5 = checkresult5 + dayStock.getNextFiveStock().getRise();
								}
								System.out.println("checkresult5="+checkresult5);
								if(checkresult5*100>5 || checkresult2*100>3){
									checkResultSuccess++;
									System.out.println("成功 ");
								} else {
									checkResultFail++;
									System.out.println("失败"+checkresult2*100);
								}
								System.out.println("今天五日成交量/昨天的五日成交量="+String.format("%.2f", d1) +"\t 昨天五日成交量/前天的五日成交量="+String.format("%.2f", d2)); 
								System.out.println("");
								  m++;
							}
						}
					}
				
			} 
		}
		return m;
	}
	public static void main(String[] args) {
		Stock stock = null;
			//	stock = ExportStock.exportStock("002066");
		  // stock = ExportStock.exportStock("SH601801");
		 //  stock = ExportStock.exportStock("SH600217");
		//   stock = ExportStock.exportStock("300001");
		//   checkStockVol(stock);
		  stock = BuildStockService.exportStock("600123");
		//	 checkStockVol(stock);
		int m = 0;
		int n = 0;
		File file = new File(Config.stockFilePath);
		String test[];
		test = file.list();
		for (int i = 0; i < test.length; i++) {
			stock = BuildStockService.exportStock(test[i]);
			if (stock != null) {
				m++;
				int c = checkStockVol(stock);
				 n = n+c;
			}
		}
		System.out.println("check "+m+"支股票，共计"+n+"支符合"+"\t "+checkResultSuccess+"成功，"+checkResultFail+"失败");
		 
	}
}
