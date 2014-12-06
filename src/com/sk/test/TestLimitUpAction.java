package com.sk.test;

import com.sk.bean.DayStock;
import com.sk.bean.Stock;
import com.sk.config.Config;
import com.sk.service.BuildStockService;
import com.sk.service.DayStockService;
import com.sk.strategy.LimitUpThreeDayStrategy;
import com.sk.strategy.NewHighStrategy;
import com.sk.util.MathUtil;

import java.io.File;
import java.util.Date;

public class TestLimitUpAction {
 

    //add wangjia
	public static int checkResultSuccess =0;
	public static int checkResultFail =0;


	 
	
	/**
	 * 检查股票成交量
	 * @param stock src/com/sk/bean/DayStockMa.java
	 */
	public static int checkStrategy(Stock stock,boolean checkAll) {
		int m=0;
		
		if(stock!=null && stock.getDayStockList()!=null) {
			for(DayStock dayStock :stock.getDayStockList()) {
				int dayStockIndex = dayStock.getIndex();
				if(!dayStock.getDate().startsWith("2014-11-21")){
					continue;
				}
				if(!dayStock.getId().contains("300377")){
					 // continue;
				}
			 	// boolean isNewHigh = new FindLimitUpThreeDayStrategy().conformLimitUpThreeDayStrategy(stock, dayStock);
			 
				  		int checkValue = new LimitUpThreeDayStrategy().conformLimitUpThreeDayStrategy(stock, dayStock);
			  	 
				 		if( checkValue>=55 ) {
							System.out.println("------------------------------------------");
							System.out.println("checkValue="+checkValue);
								double checkresult3 = 0;
                                double checkresult1 = 0;
                                double checkresultmaxprice = 0;
								
								if(dayStock.getNextOneStock()!=null){ 
									checkresult1 = dayStock.getNextOneStock().getRise();
									checkresultmaxprice =  (dayStock.getNextOneStock().getMaxPrice()-dayStock.getClosePrice())/dayStock.getClosePrice();
								}
								if(stock.getDayStockByIndex(dayStockIndex+2)!=null){ 
									checkresult3 = (stock.getDayStockByIndex(dayStockIndex+3).getClosePrice()-dayStock.getClosePrice())/dayStock.getClosePrice();
									
								}
                                 
                                if(checkresult1*100>2.5 || checkresultmaxprice *100 >4 || checkresult3 *100 >5){
                                        checkResultSuccess++;
                                        DayStockService.getCheckInfo(stock,dayStock);
                                        System.out.println("成功 " + MathUtil.format(checkresult1*100)+"%，max:"  + MathUtil.format(checkresultmaxprice*100)+"%" +"\t 3日："+ MathUtil.format(checkresult3*100)+"%");
                                 }  else {
                                        DayStockService.getCheckInfo(stock,dayStock);
                                 }
                                 
						 		System.out.println("");
								  m++;
							}
						}
					
				
			 
		}
		return m;
	}
	public static void main(String[] args) {
		Stock stock = null;
		  stock = BuildStockService.exportStock("002721");

		int m = 0;
		int n = 0;
		long ctime = new Date().getTime();
		File file = new File(Config.stockFilePath);
		String test[];
		test = file.list();
		for (int i = 0; i < test.length; i++) { 
			stock = BuildStockService.exportStock(test[i]); 
			if (stock != null) {
				m++;
				int c = checkStrategy(stock,false);
				 n = n+c;
			}  
		}
		long ctime1 =  new Date().getTime();

		System.out.println("耗时==="+(( ctime1-ctime)/1000)+"秒");
		if(n==0){n=1;}
		System.out.println("check "+m+"支，共计"+n+"支符合"+"\t "+checkResultSuccess+"成功，"
		+checkResultFail+"失败；成功率="+MathUtil.format((checkResultSuccess*100d/n))+"%");
		 
	}
}
