package com.sk.strategy;

import com.sk.bean.DayStock;
import com.sk.bean.Stock;
import com.sk.service.DayStockService;
import com.sk.service.MacdService;
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
 * 该策略：出水芙蓉
 * 要点： 
1.	这个经典的组合适用于箱体突破篇；技术要求是，
一颗标准的创了新高的中大阳线伴随着温和放大的成交量站立于各均线之上，收盘时上影线不要太长。
第二天为阳十字缩量调整，必须企稳于5天线之上，回踩有明显的做盘痕迹上佳，盘中如突破前日中大阳高点更好
。整体看均线开花，次日有攻击阻力位的目标，纵观周线能找到攻击目标位。KDJ值保持在20-50 80-100 之间，
MACD在零线之上且红柱必须明显随涨迹象
 *  
 */
public class CsfrStrategy {


    public   boolean conformCsfrStrategy(Stock stock,DayStock dayStock){
        boolean result = true;
        boolean conformVol =  isCsfr(stock,dayStock,30,2);
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

    public   boolean isCsfr(Stock stock,DayStock dayStock){
        return isCsfr(stock,dayStock,30,2);
    }
    
    
    
    /**
     *  
     * @param stock
     * @param dayStock
     * @return
     */
    public   boolean isCsfr(Stock stock,DayStock dayStock,int maxSize,int topSize){ 
    	DayStock yesterdayDayStock = dayStock.getYesterdayStock();
    	if(yesterdayDayStock==null){
    		return false;
    	}
    	boolean flag = false;
    	if(yesterdayDayStock.getRiseRateDouble()>2.2){
    		if(DayStockService.gtLongMa(yesterdayDayStock) ){
    			if(shortMaCloseForCsfr(yesterdayDayStock)){ 
	    			if(yesterdayDayStock.getVol() > yesterdayDayStock.getDayStockMa().getVolMa5()
	    					&& yesterdayDayStock.getVol() > yesterdayDayStock.getDayStockMa().getVolMa34()){
	    				flag = true;
	    			}
    			}
    		}
    	}
    	if(flag){
    		if(dayStock.getVol()<yesterdayDayStock.getVol()*1.1 && dayStock.getRiseRateDouble()<3  ){
    			if( (dayStock.getDayStockIndexMacd().getMacd()>-0.1 && dayStock.getDayStockIndexMacd().getMacdDiff()>0.1) 
    					|| (dayStock.getDayStockIndexMacd().getMacd()>-0.01)){
    				if(checkCsfrKdj(dayStock)){  
    					int macdRed = MacdService.getMacdRedDayNumber(stock, dayStock);
    					System.out.println("macdRed=="+macdRed);
        				return true;
    				}
    			}
    		}
    	}
        
        return false;
    }
    
    /**
	 * kdj的j不能是走在下降通道里
     * @param dayStock
     * @return
     */
    public   boolean checkCsfrKdj(DayStock dayStock){
    	DayStock yesterDayStock = dayStock.getYesterdayStock();
    	if(yesterDayStock==null){
    		return false;
    	}
    	DayStock yyesterDayStock = yesterDayStock.getYesterdayStock();
    	if(dayStock.getDayStockIndex().getKdjJ()>95 ||   yyesterDayStock.getDayStockIndex().getKdjJ() > 95){
    		// return false;
    	}
    	if(dayStock.getDayStockIndex().getKdjD()>40 && dayStock.getDayStockIndex().getKdjK()<80 ){
    		// return false;
    	}
    	if(dayStock.getDayStockIndex().getKdjJ() > yyesterDayStock.getDayStockIndex().getKdjJ()){
    		return true;
    	}
    	return false; 
   }
    
    /**
	 * 判断短期均线是否走向粘合
	 * @param dayStock
	 * @return
	 */
	public   boolean shortMaCloseForCsfr(DayStock dayStock){
		List<Double> list = new ArrayList<Double>();
		list.add(dayStock.getDayStockMa().getPriceMa5() );
		list.add(dayStock.getDayStockMa().getPriceMa10() );
		list.add(dayStock.getDayStockMa().getPriceMa20() );
		list.add(dayStock.getDayStockMa().getPriceMa30() );
		//升序排列
		Collections.sort(list);
		double max = Math.max( list.get(0),  list.get(3));
		double min = Math.min( list.get(0),  list.get(3));
		double ratio = max/min;
		if( ratio <1.03  && ratio>1.0){
			if(dayStock.getMinPrice() < dayStock.getDayStockMa().getPriceMa20()  || 
					dayStock.getMinPrice() < dayStock.getDayStockMa().getPriceMa30() ) {
				if(dayStock.getOpenPrice() <= max && dayStock.getClosePrice() >= max*0.98){ 
					return true;
				}
			}
		}
		return false;
	}
}
