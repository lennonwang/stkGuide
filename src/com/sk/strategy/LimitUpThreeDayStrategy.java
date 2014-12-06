package com.sk.strategy;

import com.sk.bean.DayStock;
import com.sk.bean.Stock;
import com.sk.service.BollService;
import com.sk.service.DayStockService;

/***
 * 三星夺阳
 * 1. 前面一定是要一个涨停板后的调整；60
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
 
	public int initResult = 60;
	public int dropSmallPoint = -5;
	public int dropBigPoint = -10;
	public int dropHighPoint = -20;
	public int extraSmallPoint = 5;
	public int extraBigPoint = 10;
	public int extraHighPoint = 20;
			
	
	 public   int conformLimitUpThreeDayStrategy (Stock stock,DayStock dayStock){ 
	        int conformValue =  isLimitUpThreeDayStrategy(stock,dayStock,3); 
	        //大于长期均线
	 		if(conformValue>30){
	        boolean stockPriceDownMa120And250 = dayStock.getClosePrice()<dayStock.getDayStockMa().getPriceMa120() 
					|| dayStock.getClosePrice()<dayStock.getDayStockMa().getPriceMa250() 	  ;
				if(stockPriceDownMa120And250){
					 return 0;
				} 
	 		}
	        return    conformValue;
	    }

	    public   int isLimitUpThreeDayStrategy(Stock stock,DayStock dayStock){
	        return isLimitUpThreeDayStrategy(stock,dayStock,3);
	    }
	    
	    /**
	     * 
	图形特征
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
	    public   int isLimitUpThreeDayStrategy(Stock stock,DayStock dayStock,int preDayNum){
	    	int currentDayIndex = dayStock.getIndex();
	    	int result = 0; 
	    	DayStock preThreeDayStock =  null;
	    	if(currentDayIndex-preDayNum >0 &&   stock.getDayStockList().get(currentDayIndex-preDayNum)!=null){
	    		 preThreeDayStock =   stock.getDayStockList().get(currentDayIndex-preDayNum);
	    		if(!DayStockService.isLimitUp(preThreeDayStock)){
	    			return 0;
	    		}  
	    		result =60; //初始化60分
	    		//涨停板前两天最好不要有涨停板，最好是缓慢上涨的红十字星；
	    		if(preThreeDayStock.getYesterdayStock()!=null ){
	    			if( DayStockService.isLimitUp(preThreeDayStock.getYesterdayStock())) {
	    				result = result +  dropBigPoint  ;
	    			} else  if( DayStockService.isBigRedStock(preThreeDayStock.getYesterdayStock())) {
	    				result = result + dropSmallPoint ;
		    		}  else if (DayStockService.isRedStarStock(preThreeDayStock) && preThreeDayStock.getRiseRateDouble()<3){
		    			result = result + extraSmallPoint ;
		    		}
	    			DayStock tempDayStock =   stock.getDayStockList().get(currentDayIndex-preDayNum-2);
	    			 if( DayStockService.isBigRedStock(tempDayStock)) {
	    				 result = result + dropSmallPoint ;
		    		} 
	    		} 
	    		//不能放量下跌
    			if(preThreeDayStock.getNextOneStock()!=null && preThreeDayStock.getNextOneStock().getRiseRateDouble() < -0.01){
    				if(preThreeDayStock.getNextOneStock().getVol()> preThreeDayStock.getVol()){
    					result = result + dropSmallPoint ;
    				}
    			} 
    			//不能放量下跌
    			if(dayStock.getYesterdayStock()!=null && dayStock.getYesterdayStock().getRiseRateDouble() < -0.01){
    				if(dayStock.getYesterdayStock().getVol()> preThreeDayStock.getVol()){
    					result = result + dropSmallPoint ;
    				}
    			} 
    			//不能放量下跌
    			if( dayStock.getRiseRateDouble() < -0.01){
    				if(dayStock.getVol()> preThreeDayStock.getVol()){
    					result = result + dropSmallPoint ;
    				}
    			}
    			//调整期，没有大红柱 
    			if(DayStockService.isBigRedStock(dayStock) 
    					|| DayStockService.isBigRedStock(dayStock.getYesterdayStock())  
    					|| DayStockService.isBigRedStock(preThreeDayStock.getNextOneStock())) {
	    				result = result + dropSmallPoint ; 
	    		}
	    		if( (dayStock.getClosePrice() / preThreeDayStock.getClosePrice() ) >1.22) {
	    			result = result + dropBigPoint + dropSmallPoint ;
	    		} else if( (dayStock.getClosePrice() / preThreeDayStock.getClosePrice() )>1.15){
	    			result = result + dropBigPoint ;
	    		} else if( (dayStock.getClosePrice() / preThreeDayStock.getClosePrice() )>1.08){
	    			result = result + dropSmallPoint ;
	    		}
	    		//基本要素：不能跌破涨停之前的价格
	    		if(dayStock.getMinPrice() < preThreeDayStock.getMinPrice()){
	    			result = result + dropHighPoint ;
	    		} 
	    		//基本要素：不能跌破5日线
	    		if(dayStock.getClosePrice()  < dayStock.getDayStockMa().getPriceMa5() ){ 
	    		 	result -= 5;
	    		} 
	    		if( dayStock.getYesterdayStock().getClosePrice()  < dayStock.getDayStockMa().getPriceMa5()){ 
	    		 	result -= 5;
	    		}
	    		//加分项目：如果有上升缺口没有补更好
	    		double gap = this.checkGapNoClose(stock, dayStock, preDayNum);
	    		if(gap >0 ){
	    			result = result + extraSmallPoint ;
	    			if(gap>0.5&& gap<2){ 
		    			result = result + extraSmallPoint ;
	    			}
	    		}
	    		//加分项目：最后有一个2个点的回调
	    		if( (dayStock.getRiseRateDouble() >-3 && dayStock.getRiseRateDouble() <-1)
	    			|| (dayStock.getYesterdayStock().getRiseRateDouble() >-3 && dayStock.getYesterdayStock().getRiseRateDouble() <-1 ) ) {
	    			double d =  (100.0*(dayStock.getClosePrice()-preThreeDayStock.getClosePrice()))/preThreeDayStock.getClosePrice();
	    			if( d>-3 && d<3){
	    				result = result + extraSmallPoint ;
	    			}
	    		}
	    		//boll
	    		//减分项目：如果涨停当天都没有突破boll的上轨。
	    		if(preThreeDayStock.getClosePrice() < preThreeDayStock.getDayStockIndex().getBollUpper()){ 
	    			result = result + dropBigPoint ;
	    		}
	    		if(new BollService().checkBollOpenMouth(stock, preThreeDayStock)){
	    			result = result + extraSmallPoint ;
	    		}
	    	}
	    	return result; 
	    }
	    
	    /**
	     * 
	     * @param stock
	     * @param dayStock
	     * @param preDayNum
	     */
	    public double checkGapNoClose(Stock stock,DayStock dayStock,int preDayNum) {
	    	int currentDayIndex = dayStock.getIndex();
	    	DayStock preThreeDayStock =  null;
	    	if(currentDayIndex-preDayNum >0 &&   stock.getDayStockList().get(currentDayIndex-preDayNum)!=null){
	    		 preThreeDayStock =   stock.getDayStockList().get(currentDayIndex-preDayNum);
	    		 double preThreeDayStockClosePrice =preThreeDayStock.getClosePrice();
	    		 double minPrice = dayStock.getMinPrice();
	    		 for(int i=0;i<preDayNum;i++){
	    			 DayStock item =   stock.getDayStockList().get(preThreeDayStock.getIndex()+i+1); 
	    			 minPrice = Math.min(minPrice, item.getMinPrice());
	    		 }
	    		 if(minPrice > preThreeDayStockClosePrice){
	    			 return  (100d* (minPrice-preThreeDayStockClosePrice))/dayStock.getClosePrice();
	    		 }
	    	 }
	    	return -1;
	    }
}
