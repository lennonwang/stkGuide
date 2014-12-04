package com.sk.strategy;

import com.sk.bean.DayStock;
import com.sk.bean.Stock;
import com.sk.service.DayStockService;

/***
 * 三星夺阳
 * 1. 前面一定是要一个涨停板后的调整；
2. 第二天往往是一个冲高回落；并在3日内回调至5日均线；
3. 成交量缩量；强势的未必缩量；
4. 往往kdj接近死叉，但没有死叉。第四天反身向上；
5.  3日回调，绝对不能跌破第一个涨停板的底部；
6. 尽量调整3日、 macd和kdj都是依旧向上的；
7. 概率较大的图形，有跳空缺口，且不补。 
8. 涨停板是最后是突破筹码密集区；
9. boll线最后开口，并且沿着boll逐步往上走
使用范围：在震荡行情，或者上升行情中。
 * @author lennon
 *
 */
public class LimitUpThreeDayStrategy {

	
	 public   boolean conformLimitUpThreeDayStrategy (Stock stock,DayStock dayStock){
	        boolean result = true;
	        boolean conformVol =  isLimitUpThreeDayStrategy(stock,dayStock,3);
	        if(! conformVol){
	            result = false;
	        }
	        boolean stockPriceDownMa120And250 = dayStock.getClosePrice()<dayStock.getDayStockMa().getPriceMa120() 
					|| dayStock.getClosePrice()<dayStock.getDayStockMa().getPriceMa250() 	  ;
			if(stockPriceDownMa120And250){
				 return false;
			} 
	        return    result;
	    }

	    public   boolean isLimitUpThreeDayStrategy(Stock stock,DayStock dayStock){
	        return isLimitUpThreeDayStrategy(stock,dayStock,3);
	    }
	    
	    /**
	     * 图形特征
1. 前面一定是要一个涨停板后的调整；
2. 第二天往往是一个冲高回落；并在3日内回调至5日均线；
3. 成交量缩量；强势的未必缩量；
4. 往往kdj接近死叉，但没有死叉。第四天反身向上；
5.  3日回调，绝对不能跌破第一个涨停板的底部；
6. 尽量调整3日、 macd和kdj都是依旧向上的；
7. 概率较大的图形，有跳空缺口，且不补。 
	     *  
	     * @param stock
	     * @param dayStock
	     * @return
	     */
	    public   boolean isLimitUpThreeDayStrategy(Stock stock,DayStock dayStock,int preDayNum){
	    	int currentDayIndex = dayStock.getIndex();
	    	DayStock preThreeDayStock =  null;
	    	if(currentDayIndex-preDayNum >0 &&   stock.getDayStockList().get(currentDayIndex-preDayNum)!=null){
	    		 preThreeDayStock =   stock.getDayStockList().get(currentDayIndex-preDayNum);
	    		if(!DayStockService.isLimitUp(preThreeDayStock)){
	    			return false;
	    		}
	    		if(preThreeDayStock.getYesterdayStock()!=null ){
	    			if( DayStockService.isLimitUp(preThreeDayStock.getYesterdayStock())) {
	    				return false;
	    			}
	    			
	    		}
	    		//不能放量下跌
    			if(preThreeDayStock.getNextOneStock()!=null && preThreeDayStock.getNextOneStock().getRiseRateDouble() < -0.01){
    				if(preThreeDayStock.getNextOneStock().getVol()> preThreeDayStock.getVol()){
    					return false;
    				}
    			} 
    			//不能放量下跌
    			if(dayStock.getYesterdayStock()!=null && dayStock.getYesterdayStock().getRiseRateDouble() < -0.01){
    				if(dayStock.getYesterdayStock().getVol()> preThreeDayStock.getVol()){
    					return false;
    				}
    			} 
    			//不能放量下跌
    			if( dayStock.getRiseRateDouble() < -0.01){
    				if(dayStock.getVol()> preThreeDayStock.getVol()){
    					return false;
    				}
    			}
    			//调整期，没有大红柱
	    		if(DayStockService.isBigRedStock(dayStock) || DayStockService.isBigRedStock(dayStock.getYesterdayStock()) 
	    				|| DayStockService.isBigRedStock(preThreeDayStock.getNextOneStock())  ){
	    			return false;
	    		}
	    		if(preThreeDayStock.getNextOneStock().getRiseRateDouble() 
	    				+ preThreeDayStock.getNextOneStock().getNextOneStock().getRiseRateDouble()
	    				+ dayStock.getRiseRateDouble()
	    				>10){
	    			return false;
	    		}
	    		//不能跌破涨停之前的价格
	    		if(dayStock.getMinPrice() < preThreeDayStock.getMinPrice()){
	    			return false;
	    		} 
	    		//不能跌破5日线
	    		if(dayStock.getClosePrice()  < dayStock.getDayStockMa().getPriceMa5() 
	    			&& dayStock.getYesterdayStock().getClosePrice()  < dayStock.getDayStockMa().getPriceMa5()){
	    		 	System.out.println("dayStock.getPriceMa5 =="+dayStock);
	    		 	return false;
	    		}
	    		return true;
	    	}
	    	return false;
	    	
	    }
}
