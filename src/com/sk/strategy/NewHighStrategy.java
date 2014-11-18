package com.sk.strategy;

import com.sk.bean.DayStock;
import com.sk.bean.Stock;
import com.sk.service.DayStockService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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


    public   boolean conformNewHighStrategy(Stock stock,DayStock dayStock){
        boolean result = true;
        boolean conformVol =  isNewHighClosePrice(stock,dayStock,100,1);
        if(! conformVol){
            result = false;
        }
        return  result;
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
        for(int i=1;i<=maxSize;i++){
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
        for(int i=0;i<topSize;i++){
            DayStock topDayStock = list.get(i);
            if(dayStock.getDate().equalsIgnoreCase(list.get(i).getDate())){
            	System.out.println("isNewHigh="+topDayStock+"\t"+topDayStock.getVol());
                return true;
            }
        }
        return false;
    }
}
