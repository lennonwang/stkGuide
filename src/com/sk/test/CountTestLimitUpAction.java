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
import java.util.HashMap;
import java.util.Map;

public class CountTestLimitUpAction {
 

    //add wangjia
	public static int checkResultSuccess =0;
	public static int checkResultFail =0;


	  
	public static void main(String[] args) {
		
		
		Stock stock = null;
		  stock = BuildStockService.exportStock("002721");

		int m = 0;
		int n = 0;
		long ctime = new Date().getTime();
		File file = new File(Config.stockFilePath);
		String test[];
		test = file.list();
		Map<Integer,Integer> map = new HashMap<Integer,Integer>();
		for (int i = 0; i < test.length; i++) { 
			stock = BuildStockService.exportStock(test[i]); 
			if (stock != null) {
				m++;
				int dayNum =   getBeforeDayNum(stock);
			
				System.out.println("num="+(dayNum) +"\t");
				if(map.get(dayNum)!=null){ 
					map.put(dayNum, map.get(dayNum)+1);
				} else {
					map.put(dayNum, 1);
				} 
			}  
		}
		System.out.println("map==="+map);
		long ctime1 =  new Date().getTime();

		System.out.println("耗时==="+(( ctime1-ctime)/1000)+"秒");
		if(n==0){n=1;} 
		 
		
	}
	
	public static int getBeforeDayNum(Stock stock){
		if(stock!=null && stock.getDayStockList()!=null && stock.getDayStockList().size()>1 ) {
				
				DayStock dayStock = stock.getDayStockList().get(stock.getDayStockList().size()-1);
				int dayStockIndex = dayStock.getIndex();
				DayStock firstStock = null;
				for(int i=dayStockIndex-1;i>0;i--){
					firstStock = stock.getDayStockList().get(i);
					if(DayStockService.isLimitUp(firstStock) && !DayStockService.isYiZiBan(firstStock)){
					//	System.out.println("firstStock="+firstStock+"\t"+firstStock.getIndex());
						break;
					}
				}
				if(firstStock!=null){ 
					int firstDayStockIndex = firstStock.getIndex();	
					DayStock secondStock = null;
					for(int i=firstDayStockIndex-1;i>0;i--){
						secondStock = stock.getDayStockList().get(i);
						if(DayStockService.isLimitUp(secondStock)  && !DayStockService.isYiZiBan(secondStock) ){
						//	System.out.println("secondStock="+secondStock+"\t"+secondStock.getIndex());
							break;
						}
					}
					if(secondStock!=null){
						Integer num = firstStock.getIndex() - secondStock.getIndex();
						if(num==1){
							System.out.println("num="+ num+ firstStock); 
						}
						return num;
					}
				} 
		}
		
		return 0;
	}
}
