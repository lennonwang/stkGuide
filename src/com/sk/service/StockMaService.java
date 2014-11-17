package com.sk.service;

import java.util.*;

import com.sk.bean.DayStock;
import com.sk.bean.DayStockMa;
import com.sk.bean.Stock;
import com.sk.util.MaUtil;

public class StockMaService {
 
	 
	/**
	 * 初始化股票的平均值
	 * @param stock
	 */
	public static void initStockMa(Stock stock) { 
		if(stock!=null && stock.getDayStockList()!=null) { 
			for(DayStock dayStock :stock.getDayStockList()) { 
				String dayDateString = dayStock.getDate();   
				Integer indexInList = dayStock.getIndex();
				
				DayStockMa dsm = new DayStockMa();
				dsm.setDate(dayDateString);
				dsm.setId(dayStock.getId());
				Long volSum5 = dayStock.getVol();
				Long volSum10 =dayStock.getVol(); 
				Long volSum34 =dayStock.getVol();
				double k5 = dayStock.getClosePrice();
				double k10 = dayStock.getClosePrice();
				double k20 = dayStock.getClosePrice();
				double k30 = dayStock.getClosePrice();
				double k60 = dayStock.getClosePrice();
				double k120 = dayStock.getClosePrice();
				double k250 = dayStock.getClosePrice();

                Map<Integer,Double> ema5Map  = StockMaService.emaStockClose(stock, 5);
                Map<Integer,Double> ema7Map  = StockMaService.emaStockClose(stock, 7);
                Map<Integer,Double> ema20Map  = StockMaService.emaStockClose(stock, 20);
				if(indexInList!=null){
						if(indexInList>5){
							for(int i=0;i<4;i++){
								DayStock ds = stock.getDayStockList().get(indexInList-(i+1));
								volSum5 = volSum5 + ds.getVol();
								k5 = k5 + ds.getClosePrice();
							} 
							dsm.setVolMa5(volSum5/5);  
							dsm.setPriceMa5(k5/5d);
						} 
						if(indexInList>10){
							for(int i=0;i<9;i++){
								DayStock ds = stock.getDayStockList().get(indexInList-(i+1)); 
								volSum10 = volSum10 + ds.getVol(); 
								k10 = k10 + ds.getClosePrice();
							}  
							dsm.setVolMa10(volSum10/10);
							dsm.setPriceMa10(k10/10d);
						}
						if(indexInList>20){
							for(int i=0;i<19;i++){
								DayStock ds = stock.getDayStockList().get(indexInList-(i+1));  
								k20 = k20 + ds.getClosePrice();
							}
							dsm.setPriceMa20(k20/20d);
						}
						if(indexInList>30){
							for(int i=0;i<29;i++){
								DayStock ds = stock.getDayStockList().get(indexInList-(i+1));  
								k30 = k30 + ds.getClosePrice();
							}
							dsm.setPriceMa30(k30/30d);
						}
						if(indexInList>60){
							for(int i=0;i<59;i++){
								DayStock ds = stock.getDayStockList().get(indexInList-(i+1));  
								k60 = k60 + ds.getClosePrice();
							}
							dsm.setPriceMa60(k60/60d);
						}
						if(indexInList>120){
							for(int i=0;i<119;i++){
								DayStock ds = stock.getDayStockList().get(indexInList-(i+1));  
								k120 = k120 + ds.getClosePrice();
							}
							dsm.setPriceMa120(k120/120d);
						}
						if(indexInList>250){
							for(int i=0;i<249;i++){
								DayStock ds = stock.getDayStockList().get(indexInList-(i+1));  
								k250 = k250 + ds.getClosePrice();
							}
							dsm.setPriceMa250(k250/250d);
						}
						if(indexInList>34){
							for(int i=0;i<33;i++){
								DayStock ds = stock.getDayStockList().get(indexInList-(i+1)); 
								volSum34 = volSum34 + ds.getVol();
							}
							dsm.setVolMa34(volSum34/34);
						}
						if(ema5Map.get(indexInList)!=null){
                            dsm.setPriceEma5(ema5Map.get(indexInList));
                        }
                        if(ema7Map.get(indexInList)!=null){
                            dsm.setPriceEma7(ema7Map.get(indexInList));
                        }
                        if(ema20Map.get(indexInList)!=null){
                            dsm.setPriceEma20(ema20Map.get(indexInList));
                        }
                        dayStock.setDayStockMa(dsm);
						stock.getDayStockMap().put(dayStock.toString(), dayStock);
					//	System.out.println("dayStock:"+dayStock.toString()+"\t"+dayStock.getVol()+"\t"+dayStock.getVolMa5()+"\t"+dayStock.getVolMa10()); 
				}
			}
		}
	}

    /**
     * 求X的N日指数平滑移动平均。
     算法是：若Y=EMA(X，N)，则Y=〔2*X+(N-1)*Y’〕/(N+1)，
     其中Y’表示上一周期的Y值。
     2 是平滑系数，表示今天的权重是2.
     公式含义为。今天值乘以权重2，加上历史积累值 除以 天数加1, 因为当天权重加了1.
     //〔2*X+(N-1)*Y’〕/(N+1)
     */
    public static Map<Integer,Double> emaStockClose(Stock stock,int num) {
        Map<Integer,Double> emaMap = new HashMap<Integer,Double>();
        if(stock!=null && stock.getDayStockList()!=null) {
            for(DayStock dayStock :stock.getDayStockList()) {
                Integer indexInList = dayStock.getIndex();
                double dClose = dayStock.getClosePrice();
                double emaStockClose = 0d;
                List<Double> doubleList =new ArrayList<Double>();
                doubleList.add(dClose);
                //	System.out.println("-----------------------------------");
                if(indexInList==0){
                    emaStockClose = dClose;
                    emaMap.put(0, emaStockClose);
                } else {
                    double pre = emaMap.get(indexInList-1);
                    emaStockClose = MaUtil.countEma(dClose, pre, num);
                    emaMap.put(indexInList, emaStockClose);
                }
                //	System.out.println("ema="+dayStock+"\t"+emaStockClose);
            }
        }
        return emaMap;
    }



    /**
     * 3. SMA(X，N，M)　X的M日加权移动平均，
     M为权重，如Y=(X*M+Y'*(N-M))/N
     英文含义不知道，中文有的说是算术平均值。 我看还是加权平均值比较好。
     SMA 就是把EMA(X,N) 中的权重2， 变成了一个可自己定义的变数。要求 M < N;
     还是加权平均的意思。
     * @param stock,@param num
     */

    public static void smaStockClose(Stock stock,int num) {
        if(stock!=null && stock.getDayStockList()!=null) {
            Map<Integer,Double> emaMap = new HashMap<Integer,Double>();
            for(DayStock dayStock :stock.getDayStockList()) {
                String dayDateString = dayStock.getDate();
                Integer indexInList = stock.getDayStockIndexMap().get(dayStock.toString());
                double dClose = dayStock.getClosePrice();
                double emaStockClose = 0d;
                List<Double> doubleList =new ArrayList<Double>();
                doubleList.add(dClose);
                int maxPre=stock.getDayStockList().size();
                System.out.println("-----------------------------------");
                System.out.println("maxPre="+maxPre+"\t"+indexInList+"\t"+dayDateString);
                if(indexInList==0){
                    emaStockClose = dClose;
                    emaMap.put(0, emaStockClose);
                } else {
                    double pre = emaMap.get(indexInList-1);
                    emaStockClose = MaUtil.countEma(dClose, pre, num);
                    emaMap.put(indexInList, emaStockClose);
                }
                System.out.println("ema="+dayStock+"\t"+emaStockClose);
            }
        }
    }

}
