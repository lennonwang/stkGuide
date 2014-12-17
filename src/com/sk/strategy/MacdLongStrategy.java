package com.sk.strategy;

import com.sk.bean.DayStock;
import com.sk.bean.Stock;
import com.sk.service.DayStockService; 

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: lennon
 * Date: 14-11-17
 * Time: 上午11:41
 * 该策略：创新高交易策略 
 * @todo 考虑rsi macd的指标协助
 * macd第一次金叉
 */
public class MacdLongStrategy {

	private Logger logger = Logger.getLogger(MacdLongStrategy.class);
	
    public   boolean conformMacdLongStrategy(Stock stock,DayStock dayStock){
     
        boolean checkMacdLongMacd =  checkMacdLongMacd(stock,dayStock);
        if(   checkMacdLongMacd ){
            //过滤出现顶背离的股票
            boolean filterDeviation = checkDeviation(stock,dayStock);
            if(!filterDeviation){
        	//  return  true;
            }
            return true;
        }
        return  false;
    }

    /** 
     * 
     * @param stock
     * @param dayStock
     * @return
     */
    public   boolean checkMacdLongMacd(Stock stock,DayStock dayStock){ 
        List<DayStock> list = new ArrayList<DayStock>();
        return     checkMacdLongMacd(stock,dayStock,10); 
    }
    
    
	public   boolean checkMacdLongMacd(Stock stock,DayStock dayStock,int maxSize){ 
		int dayStockIndex = dayStock.getIndex();
		if(dayStock.getYesterdayStock()==null){
			return false;
		}
		// if(dayStock.getDayStockIndexMacd().getMacdDiff() > dayStock.getDayStockIndexMacd().getMacdDea()){
		if(dayStock.getDayStockIndexMacd().getMacd() > dayStock.getYesterdayStock().getDayStockIndexMacd().getMacd()){
			if(dayStock.getYesterdayStock().getDayStockIndexMacd().getMacdDiff() < dayStock.getYesterdayStock().getDayStockIndexMacd().getMacdDea()){
				double minMacd = dayStock.getDayStockIndexMacd().getMacd();
				double minDif = dayStock.getDayStockIndexMacd().getMacdDiff();
				for(int i=1;i<11;i++){
					DayStock dayStockItem =  stock.getDayStockList().get(dayStockIndex-(i+1));
					if(dayStockItem.getDayStockIndexMacd().getMacdDiff() > dayStockItem.getDayStockIndexMacd().getMacdDea()){
						minDif = Math.min(minDif, dayStockItem.getDayStockIndexMacd().getMacdDiff()); 
						minMacd = Math.min(minMacd, dayStockItem.getDayStockIndexMacd().getMacd());
						return false;
					}
					if(minDif>0 || minMacd>-0.3){
						return false;
					}
				} 
				return true;
			} 
		}
		return false;
	}


    /**
     * 判断有没有出现顶背离    ; true= 如果出现顶背离，
      * @param stock
     * @param dayStock
     * @return  true= 如果出现顶背离，
     */
		public boolean checkDeviation (Stock stock,DayStock dayStock){
            List<DayStock> list = new ArrayList<DayStock>();
            for(int i=3;i<=30;i++){
                if(stock.getDayStockByIndex(dayStock.getIndex()-i)!=null){
                    list.add(stock.getDayStockByIndex(dayStock.getIndex()-i));
                }
            }
            if(list.size()>0){
                //按照收盘价大小，做倒序排列；收盘价最高的，排在最前方
                Collections.sort(list, new Comparator<DayStock>() {
                    @Override
                    public int compare(DayStock o1, DayStock o2) {
                        return (int) ( ( o2.getDayStockIndexMacd().getMacdDiff() * 1000)- (o1.getDayStockIndexMacd().getMacdDiff() *1000) );

                    }
                });

                DayStock topDiffDayStock = list.get(0);
                double topClosePrice = topDiffDayStock.getClosePrice();
                 if((topDiffDayStock.getDayStockIndexMacd().getMacdDiff()*100 > dayStock.getDayStockIndexMacd().getMacdDiff()*108) 
                		&& dayStock.getClosePrice()/topClosePrice >1.1 ) {
                	 logger.info("filterCheckDeviation:"+topDiffDayStock+"\t"+dayStock);
                     return false;
                 }
            }
			return true;
		}

    //  && (  (dayStock.getClosePrice()*100)/(topDiffDayStock.getClosePrice()*100) >1.05
}
