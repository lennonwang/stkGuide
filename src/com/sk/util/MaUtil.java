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
					double currentValue = countEma (item,pre,num);
					emaMap.put(i, currentValue);
				}   
			}
		}
		return emaMap;
	}


	public static double countEma(double taday,double preEma ,int num) {
		double d = ( 2* taday + (num-1) * preEma) /(num+1);
		return (d);
	}

    public static double countSma(double taday,double preEma ,int num,int m) {
		double d = ( m* taday + (num-m) * preEma) /(num);
		return (d);
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
		 // MaUtil.emaStockClose(stock, 7);
	}
}
