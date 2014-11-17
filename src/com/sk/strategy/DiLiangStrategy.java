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
 * 该策略：使用地量见地价的逻辑，通过扫描一段时间内成交量较低的几天，做为买入点。
 * @todo 在辅助均线或者其他支撑线;
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
}
