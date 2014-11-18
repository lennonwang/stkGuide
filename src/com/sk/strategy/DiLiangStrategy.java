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
 * 该策略：使用地量见地价的逻辑，通过扫描一段时间内成交量较低的几天，做为买入点。
 * 在辅助均线或者其他支撑线;考虑买入为低吸位置，因此增加了closePrice的位置在20日或者60日两个中期均线附近；
 * @todo 考虑macd的指标协助
 * 建议使用范围：验证周期在至少5天以上，建议10天左右；
 */
public class DiLiangStrategy {


    public   boolean conformDiLiangStrategy(Stock stock,DayStock dayStock){
        boolean result = true;
        boolean conformVol =  isDiLiangVol(stock,dayStock,30,2);
        if(! conformVol){
            result = false;
        }
        boolean stockPriceDownMa120And250 = dayStock.getClosePrice()<dayStock.getDayStockMa().getPriceMa120() 
				|| dayStock.getClosePrice()<dayStock.getDayStockMa().getPriceMa250() 	
				|| dayStock.getDayStockMa().getPriceMa120()  < dayStock.getDayStockMa().getPriceMa250()  ;
		if(stockPriceDownMa120And250){
			 return false;
		}
		boolean stockPriceDownMa60 = (dayStock.getMaxPrice() < dayStock.getDayStockMa().getPriceMa60() 
				&& dayStock.getYesterdayStock().getMaxPrice() < dayStock.getYesterdayStock().getDayStockMa().getPriceMa60() );
		if(stockPriceDownMa60) {
			return false;
		}
        
        if(dayStock.getDayStockIndexMacd().getMacdDiff() > 0.70){
        	 return false;
        }
        return    result;
    }

    public   boolean isDiLiangVol(Stock stock,DayStock dayStock){
        return isDiLiangVol(stock,dayStock,30,2);
    }
    
    
    
    /**
     * 判断该天的成交量是否是地量成交量
     * 如果是一字板会过滤
     * @param stock
     * @param dayStock
     * @return
     */
    public   boolean isDiLiangVol(Stock stock,DayStock dayStock,int maxSize,int topSize){
        long dayVol = dayStock.getVol();
        if(DayStockService.isYiZiBan(dayStock)){
            return false;
        }
        List<DayStock> list = new ArrayList<DayStock>();
        for(int i=1;i<=maxSize;i++){
            if(stock.getDayStockByIndex(dayStock.getIndex()-i)!=null){
                list.add(stock.getDayStockByIndex(dayStock.getIndex()-i));
            }
        }
        //倒序
        Collections.sort(list, new Comparator<DayStock>() {
            @Override
            public int compare(DayStock o1, DayStock o2) {
                return (int) (o1.getVol() - o2.getVol());

            }
        });
        for(int i=0;i<topSize;i++){
            DayStock topDayStock = list.get(i);

            if(dayStock.getDate().equalsIgnoreCase(list.get(i).getDate())){
                //		System.out.println("isDiLiang="+topDayStock+"\t"+topDayStock.getVol());
                return true;
            }
            if(dayVol<topDayStock.getVol()){
                //		System.out.println("isDiLiang="+topDayStock+"\t"+topDayStock.getVol());
                return true;
            }
        }
        return false;
    }
    
    /**
     * 增加地量的位置，在20日或者60日均线附近；
     * @param dayStock
     * @return
     */
    public   boolean checkDiLiangPrice(DayStock dayStock){
    	if(MathUtil.checkRiseRange(dayStock.getClosePrice(), dayStock.getDayStockMa().getPriceMa60(), -2, 2)){
    		return true;
    	}
    	if(MathUtil.checkRiseRange(dayStock.getClosePrice(), dayStock.getDayStockMa().getPriceMa20(), 0, 2)){
    		return true;
    	}
    	return false; 
   }
}
