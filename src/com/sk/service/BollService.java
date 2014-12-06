package com.sk.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sk.bean.DayStock;
import com.sk.bean.Stock;
import com.sk.config.Config;
import com.sk.util.MaUtil;
import com.sk.util.MathUtil;


/**
通达信的系统BOLL-M
{参数 N: 2  250  20 }
MID:=MA(C,N);
VART1:=POW((C-MID),2);
VART2:=MA(VART1,N);
VART3:=SQRT(VART2);
UPPER:=MID+2*VART3;
LOWER:=MID-2*VART3;
BOLL:REF(MID,1),COLORFFFFFF;
UB:REF(UPPER,1),COLOR00FFFF;
LB:REF(LOWER,1),COLORFF00FF;  
 */
public class BollService {
	
	
	public static void countStockBoll(Stock stock) {
		countStockBoll(stock,20);
	}
	
	public static void countStockBoll(Stock stock,int countNum) {
		if(stock!=null && stock.getDayStockList()!=null) {
            for(DayStock dayStock :stock.getDayStockList()) {
                  int dayStockIndex = dayStock.getIndex();
                  double mid = dayStock.getDayStockMa().getPriceMa20();
                  double vart1 = Math.pow(dayStock.getClosePrice()-mid,2);
                    dayStock.getDayStockIndex().setBollMid(mid);
                    dayStock.getDayStockIndex().setBollVart1(vart1);

                  if(dayStockIndex>countNum){
                       double sumVart1 = vart1;
                      for(int i=1;i<countNum;i++){
                          sumVart1 = sumVart1 + stock.getDayStockList().get(dayStockIndex-i).getDayStockIndex().getBollVart1() ;
                      }
                      double vart2 =    sumVart1/    countNum;
                      dayStock.getDayStockIndex().setBollVart2(vart2);
                      double vart3 =   Math.sqrt(vart2) ;
                      dayStock.getDayStockIndex().setBollVart3(vart3);
                      
                      double boll = MathUtil.format(dayStock.getYesterdayStock().getDayStockIndex().getBollMid() );
                      double upper= MathUtil.format(boll + 2* dayStock.getYesterdayStock().getDayStockIndex().getBollVart3() ); 
                      double lower= MathUtil.format(boll - 2* dayStock.getYesterdayStock().getDayStockIndex().getBollVart3() ) ; 
                   
                      double nextBoll = MathUtil.format(dayStock.getDayStockIndex().getBollMid() );
                      double nextUpper= MathUtil.format(nextBoll + 2* dayStock.getDayStockIndex().getBollVart3() ); 
                      double nextLower= MathUtil.format(nextBoll - 2* dayStock.getDayStockIndex().getBollVart3() ) ; 
                   
                      //  double lower  = mid - 2*vart3;
                      dayStock.getDayStockIndex().setBollUpper(upper);
                      dayStock.getDayStockIndex().setBollLower(lower);
                      dayStock.getDayStockIndex().setBoll(boll);
                      

                      dayStock.getDayStockIndex().setNextBollUpper(nextUpper);
                      dayStock.getDayStockIndex().setNextBollLower(nextLower);
                      dayStock.getDayStockIndex().setNextBoll(nextBoll);
               //      System.out.println("boll upper="+upper+"\t var3="+
               //     		 MathUtil.format( vart3 ) +"\t"+DayStockService.getDayStockInfo(dayStock));
                  }
            }
         }
	}
	
	
	/**
	 * 
	 * @param dayStock
	 * @return
	 */
	public  boolean checkBollOpenMouth(Stock stock,DayStock dayStock){ 
		if(dayStock.getDayStockIndex().getBoll()<= 0){
			return false;
		}
		double currentBollVart3 = dayStock.getDayStockIndex().getBollVart3();
		double currentRise =100d * (dayStock.getDayStockIndex().getBollUpper() 
				-  dayStock.getDayStockIndex().getBoll())/ dayStock.getDayStockIndex().getBoll();
		if(dayStock.getYesterdayStock()==null){
			return false;
		} 
		double yesterdayRise =100d * (dayStock.getYesterdayStock().getDayStockIndex().getBollUpper() 
				-  dayStock.getYesterdayStock().getDayStockIndex().getBoll())/ dayStock.getDayStockIndex().getBoll();
		
		double nextRise =100d * (dayStock.getNextOneStock().getDayStockIndex().getBollUpper() 
				-  dayStock.getNextOneStock().getDayStockIndex().getBoll())/ dayStock.getNextOneStock().getDayStockIndex().getBoll();
		
		double yesterdayBollVart3 = dayStock.getYesterdayStock().getDayStockIndex().getBollVart3();
	//	System.out.println("currentRise="+ MathUtil.format( currentRise ) +"\t yesterdayRise="+  MathUtil.format( yesterdayRise) 
//				 +"\t nextRise="+  MathUtil.format( nextRise));
	//	System.out.println("dayStock="+ DayStockService.getDayStockInfo(dayStock) );
		
		if(nextRise/yesterdayRise>1.3){
			return true;
		}
		return false;
	}
	 
	
	public static void main(String[] args) {
		Stock stock = null;
		//	stock = ExportStock.exportStock("002066");
		  // stock = ExportStock.exportStock("SH601801");
		 //  stock = BuildStockService.exportStock("SH600217"); 
		//   checkStockVol(stock); 
		   
			File file = new File(Config.stockFilePath);
			String test[];
			test = file.list();
			for (int i = 0; i < test.length; i++) { 
				stock = BuildStockService.exportStock(test[i]); 
				if(stock!=null) {
					for(DayStock dayStock :stock.getDayStockList()) {
						int dayStockIndex = dayStock.getIndex();
						if(!dayStock.getDate().startsWith("2014-12-05")){
							continue;
						}
						 new BollService().checkBollOpenMouth(stock,dayStock);
					}
				}
			}
			
		//  MaUtil.emaStockClose(stock, 2);
	}
}
