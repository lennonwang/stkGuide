package com.sk.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.sk.bean.DayStock;
import com.sk.bean.Stock;
import com.sk.service.BuildStockService;

public class MaUtil {

	public void ma(){
		
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
				Integer indexInList = stock.getDayStockIndexMap().get(dayStock.toString()); 
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
					emaStockClose =emaStockClose (dClose,pre,num);
					emaMap.put(indexInList, emaStockClose);
				} 
			//	System.out.println("ema="+dayStock+"\t"+emaStockClose); 
			}
		}
		return emaMap;
	}

	
	public static Map<Integer,Double> emaStockClose(List<Double> doubleList,int num) { 
		Map<Integer,Double> emaMap = new HashMap<Integer,Double>();
		if(doubleList!=null ) { 
			for(int i=0;i<doubleList.size();i++) {  
				double item = doubleList.get(i);  
			//	System.out.println("-----------------------------------"); 
				if(i==0){ 
					emaMap.put(0, item);
				} else {
					double pre = emaMap.get(i-1);
					double currentValue = emaStockClose (item,pre,num);
					emaMap.put(i, currentValue);
				}   
			}
		}
		return emaMap;
	}


	private static double emaStockClose(double taday,double preEma ,int num) {
		double d = ( 2* taday + (num-1) * preEma) /(num+1);
		return (d);
	}
	
	private static double smaStockClose(double taday,double preEma ,int num,int m) {
		double d = ( m* taday + (num-m) * preEma) /(num);
		return (d);
	}
	
	
	/**
	 * 3. SMA(X，N，M)　X的M日加权移动平均，
M为权重，如Y=(X*M+Y'*(N-M))/N
英文含义不知道，中文有的说是算术平均值。 我看还是加权平均值比较好。
SMA 就是把EMA(X,N) 中的权重2， 变成了一个可自己定义的变数。要求 M < N;
还是加权平均的意思。 
	 * @param args
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
					emaStockClose =emaStockClose (dClose,pre,num);
					emaMap.put(indexInList, emaStockClose);
				} 
				System.out.println("ema="+dayStock+"\t"+emaStockClose); 
			}
		}
	}
	
	public static void main(String[] args) {
		Stock stock = null;
		//	stock = ExportStock.exportStock("002066");
		  // stock = ExportStock.exportStock("SH601801");
		 //  stock = ExportStock.exportStock("SH600217");
		//   stock = ExportStock.exportStock("300001");
		//   checkStockVol(stock);
		  stock = BuildStockService.exportStock("002066");
		//  countStockRsi(stock,6);
		  MaUtil.emaStockClose(stock, 7);  
	}
}
