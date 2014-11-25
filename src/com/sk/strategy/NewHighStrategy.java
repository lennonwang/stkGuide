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
 * 建议使用范围：短期惯性上涨，或者等待低吸机会；
 */
public class NewHighStrategy {

	private Logger logger = Logger.getLogger(NewHighStrategy.class);
	
    public   boolean conformNewHighStrategy(Stock stock,DayStock dayStock){
    
        boolean conformVol =  isNewHighClosePrice(stock,dayStock,250,1);
        boolean checkNewHighRsi =  checkNewHighRsi(stock,dayStock);
        if(  conformVol && checkNewHighRsi ){
            //过滤出现顶背离的股票
            boolean filterDeviation = checkDeviation(stock,dayStock);
            if(!filterDeviation){
        	  return  true;
            }
        }
        return  false;
    }

    /**
     * 判断该天的收盘价（最高价）是否是最近一段时间的最高价
     * @param stock
     * @param dayStock
     * @return
     */
    public   boolean isNewHighClosePrice(Stock stock,DayStock dayStock,int maxSize,int topSize){

        if(DayStockService.isYiZiBan(dayStock)){
            return false;
        }
        List<DayStock> list = new ArrayList<DayStock>();
        for(int i=0;i<=maxSize;i++){
            if(stock.getDayStockByIndex(dayStock.getIndex()-i)!=null){
                list.add(stock.getDayStockByIndex(dayStock.getIndex()-i));
            }
        }
        //按照收盘价大小，做倒序排列；收盘价最高的，排在最前方
        Collections.sort(list, new Comparator<DayStock>() {
            @Override
            public int compare(DayStock o1, DayStock o2) {
                return (int) ( ( o2.getClosePrice() * 1000)- (o1.getClosePrice()*1000) );

            }
        });
       if(list.size()<topSize){
    	   return false;
       }
       boolean isTop = false;
       for(int i=0;i<topSize;i++){
            DayStock topDayStock = list.get(i); 
            if(dayStock.getDate().equalsIgnoreCase(list.get(i).getDate())){ 
                isTop =  true;
                continue;
            }
        } 
        return false;
    }
    
    
	public   boolean checkNewHighRsi(Stock stock,DayStock dayStock){ 
		int dayStockIndex = dayStock.getIndex();
		double currentRsi = dayStock.getDayStockIndex().getRsi6(); 
		double preDayRsi = 0;
		if(dayStock.getYesterdayStock()!=null){
			preDayRsi = dayStock.getYesterdayStock().getDayStockIndex().getRsi6(); 
		}
		double beforePreDayRsi = 0;
		if( stock.getDayStockByIndex(dayStockIndex-2)!=null){
			beforePreDayRsi = stock.getDayStockByIndex(dayStockIndex-2).getDayStockIndex().getRsi6(); 
		}
		double beforeThreeRsi = 0;
		if( stock.getDayStockByIndex(dayStockIndex-3)!=null){
			beforeThreeRsi = stock.getDayStockByIndex(dayStockIndex-3).getDayStockIndex().getRsi6(); 
		}
		double beforeFourRsi = 0;
		if( stock.getDayStockByIndex(dayStockIndex-4)!=null){
			beforeFourRsi = stock.getDayStockByIndex(dayStockIndex-4).getDayStockIndex().getRsi6(); 
		}
		boolean flag =  true;  
	   if(currentRsi<50 && preDayRsi<50 && beforePreDayRsi<50){
		   return false;
	   }  
	   if(currentRsi + preDayRsi  +  beforePreDayRsi + beforeThreeRsi +  beforeFourRsi > 75*5){
		   return false;
	   }   
		return flag;
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
