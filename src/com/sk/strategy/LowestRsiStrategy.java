package com.sk.strategy;

import com.sk.bean.DayStock;
import com.sk.bean.Stock;
import com.sk.service.DayStockService;
import com.sk.util.MathUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: lennon
 * Date: 14-11-17
 * Time: 上午11:41
 * 该策略：使用超跌反弹模型。主要通过rsi的底背离来操作
 *  
 */
public class LowestRsiStrategy {


    public   boolean conformLowestRsiStrategy(Stock stock,DayStock dayStock){
        boolean result = true;
        boolean conformVol =  isLowestRsiVol(stock,dayStock,30,2);
        if(! conformVol){
            result = false;
        }
        boolean stockPriceDownMa120And250 = dayStock.getClosePrice()<dayStock.getDayStockMa().getPriceMa120() 
				|| dayStock.getClosePrice()<dayStock.getDayStockMa().getPriceMa250() 	
				  ;
		if(stockPriceDownMa120And250){
			 return false;
		}
		/*boolean stockPriceDownMa60 = (dayStock.getMaxPrice() < dayStock.getDayStockMa().getPriceMa60() 
				&& dayStock.getYesterdayStock().getMaxPrice() < dayStock.getYesterdayStock().getDayStockMa().getPriceMa60() );
		if(stockPriceDownMa60) {
			return false;
		}
        
        if(dayStock.getDayStockIndexMacd().getMacdDiff() > 0.70){
        	 return false;
        }*/
        return    result;
    }

    public   boolean isLowestRsiVol(Stock stock,DayStock dayStock){
        return isLowestRsiVol(stock,dayStock,30,2);
    }
    
    
    
    /**
     * 判断该天的Rsi是否是最低的rsi
     * 如果是一字板会过滤
     * @param stock
     * @param dayStock
     * @return
     */
    public   boolean isLowestRsiVol(Stock stock,DayStock dayStock,int maxSize,int topSize){
        double dayRsi6 = dayStock.getDayStockIndex().getRsi6(); 
        if(dayRsi6>30){
        	return false;
        }
        List<DayStock> list = new ArrayList<DayStock>(); 
        for(int i=0;i<=maxSize;i++){
            if(stock.getDayStockByIndex(dayStock.getIndex()-i)!=null){
                list.add(stock.getDayStockByIndex(dayStock.getIndex()-i));
            }
        }
        //倒序
        Collections.sort(list, new Comparator<DayStock>() {
            @Override
            public int compare(DayStock o1, DayStock o2) {
                return (int) (o1.getClosePrice() - o2.getClosePrice());

            }
        });
        boolean isLowest = false;
        for(int i=0;i<topSize;i++){
        	if(list.size()<topSize){
        		continue;
        	}
            DayStock topDayStock = list.get(i); 
            if(dayStock.getDate().equalsIgnoreCase(list.get(i).getDate()) 
            		|| dayStock.getClosePrice() < topDayStock.getClosePrice()){ 
            	isLowest =  true;
            	break;
            }
        }
        if(isLowest){
        	 List<DayStock> newList = new ArrayList<DayStock>();
             for(int i=3;i<=13;i++){
                 if(stock.getDayStockByIndex(dayStock.getIndex()-i)!=null){
                	 newList.add(stock.getDayStockByIndex(dayStock.getIndex()-i));
                 }
             }
             if(newList.size()>0){
	        	Collections.sort(newList, new Comparator<DayStock>() {
	                @Override
	                public int compare(DayStock o1, DayStock o2) {
	                    return (int) (o1.getDayStockIndex().getRsi6() - o2.getDayStockIndex().getRsi6()); 
	                }
	            }); 
	        	for(DayStock ds:newList){
	        	//	System.out.println("ds()="+ds+"\t"+ds.getDayStockIndex().getRsi6());
	        	}
	            if(dayRsi6 - newList.get(0).getDayStockIndex().getRsi6() > 2){
	         ///   	System.out.println("dayStock.getDate()=="+dayStock.getDate()+"\t"+dayStock);
	          ////  	System.out.println("dayStock2.getDate()=="+newList.get(0));
	            	return true;	
	            }
            }
        	 
        }
        return false;
    }
    
    /**
     * 增加地量的位置，在20日或者60日均线附近；
     * @param dayStock
     * @return
     */
    public   boolean checkLowestRsiPrice(DayStock dayStock){
    	if(MathUtil.checkRiseRange(dayStock.getClosePrice(), dayStock.getDayStockMa().getPriceMa60(), -2, 2)){
    		return true;
    	}
    	if(MathUtil.checkRiseRange(dayStock.getClosePrice(), dayStock.getDayStockMa().getPriceMa20(), 0, 2)){
    		return true;
    	}
    	return false; 
   }
}
