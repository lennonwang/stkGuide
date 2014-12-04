package com.sk.test;

import com.sk.bean.DayStock;
import com.sk.bean.Stock;
import com.sk.config.Config;
import com.sk.service.BollService;
import com.sk.service.BuildStockService;
import com.sk.service.DayStockService;
import com.sk.strategy.LimitUpThreeDayStrategy;
import com.sk.strategy.NewHighStrategy;
import com.sk.util.MathUtil;

import java.io.File;
import java.util.Date;

public class TestBollAction {
 

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
			 	if(!dayStock.getDate().startsWith("2014-11-17")){
						continue;
					}
			 	// boolean isNewHigh = new FindLimitUpThreeDayStrategy().conformLimitUpThreeDayStrategy(stock, dayStock);
			  	
				  boolean isNewHigh = new BollService().checkBoll(stock, dayStock);
			  		if(checkAll){
			  			isNewHigh = true;
			  		}
				 		if( isNewHigh ) {
							System.out.println("------------------------------------------");
				  
								double checkresult5 = 0;
                                double checkresult2 = 0;
								
								if(dayStock.getNextOneStock()!=null){ 
									checkresult2 = checkresult2 + dayStock.getNextOneStock().getRise();
									checkresult5 = checkresult5 + dayStock.getNextOneStock().getRise();
								}
								if(stock.getDayStockByIndex(dayStockIndex+2)!=null){ 
									checkresult5 = checkresult5 + stock.getDayStockByIndex(dayStockIndex+2).getRise();
									checkresult2 = checkresult2 + stock.getDayStockByIndex(dayStockIndex+2).getRise();
								}
								if(stock.getDayStockByIndex(dayStockIndex+3)!=null){ 
									checkresult5 = checkresult5 + stock.getDayStockByIndex(dayStockIndex+3).getRise();
								}
								if(stock.getDayStockByIndex(dayStockIndex+4)!=null){ 
									checkresult5 = checkresult5 + stock.getDayStockByIndex(dayStockIndex+4).getRise();
								}
								if(stock.getDayStockByIndex(dayStockIndex+5)!=null){ 
									checkresult5 = checkresult5 + stock.getDayStockByIndex(dayStockIndex+5).getRise();
								} 
                                 
                                if(checkresult2*100>3){
                                        checkResultSuccess++;
                                        DayStockService.getCheckInfo(stock,dayStock);
                                        System.out.println("成功 2日" + MathUtil.format(checkresult2*100)+"%" +"\t 五日："+ MathUtil.format(checkresult5*100)+"%");
                                 } else {
                                        checkResultFail++;
                                        DayStockService.getCheckInfo(stock,dayStock);
                                        System.out.println("失败 2日"+ MathUtil.format(checkresult2*100)+"%" +"\t 五日："+ MathUtil.format(checkresult5*100)+"%");
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
