package com.sk.util;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MathUtil {

	
	static DecimalFormat  df = new DecimalFormat(".00");
	 /** 
     * 使用DecimalFormat,保留小数点后两位 
     */  
    public static String formatToString(double value) {   
        df.setRoundingMode(RoundingMode.HALF_UP);  
        return df.format(value);  
    }  
    
    /** 
     * 在中间计算过程中，尽量不用format，只在最后展示时，显示format
     * 使用DecimalFormat,保留小数点后两位 
     */  
    public static double format(double value) {   
        df.setRoundingMode(RoundingMode.HALF_UP);  
        String s=  df.format(value);  
        return Double.parseDouble(s);
      //  return 0;
    }  
    
    

	/*
	 *  求X的N日指数平滑移动平均。
算法是：若Y=EMA(X，N)，则Y=〔2*X+(N-1)*Y’〕/(N+1)，
其中Y’表示上一周期的Y值。
2 是平滑系数，表示今天的权重是2.
公式含义为。今天值乘以权重2，加上历史积累值 除以 天数加1, 因为当天权重加了1.

EMA（c，5）＝(5*X5+4*X4+3*X3+2*X2+1*X1)/15=5.67
	 */
	public static double emaStockCloseInit(List<Double> stockList,int num) { 
		double x =MathUtil.format( stockList.get(stockList.size()-num));
	//	System.out.println("x="+x+"\t"+num);
		if(num==1){
			return x;
		} 
		double s = (2*x+(num-1)*emaStockCloseInit(stockList,num-1))/(num+1);
		return MathUtil.format(s);
	}
	
	public static double emaStockCloseByEma(double dayClose,double beforeDayEmaClose,int num) { 
		double s = (2*dayClose+(num-1)*beforeDayEmaClose)/(num+1); 
		return MathUtil.format(s);
	}
	
	public static double emaStockCloseDiGui(List<Double> stockList ,int num,int index) {
		double value =0d;
		System.out.println("stockList emaStockCloseDiGui="+stockList+"\t index="+index);
		for(int i=0;i<stockList.size();i++){
			Double cValue  = stockList.get(i);
			if(index==0){
				System.out.println("index= 0 "+i+"\t"+cValue);
				return stockList.get(stockList.size()-1);
			}else {
				double d = ( 2* cValue + (num-1) *  emaStockCloseDiGui(stockList,num,index-1)) /(num+1);
				System.out.println("value:"+d+"\t index:"+index+"\t"+cValue);
				value = d;
				return d;
			}
		}
		return value;
	}
	
	
	/**
	 * stockList:股价从今天、昨天、前天、以此排列；
	 * @param stockList
	 * @param index
	 * @param num
	 * @return
	 */
	public static double emaStockClose(List<Double> stockList,int index ,int num) {
		int maxStockSize = stockList.size();
		int beginIndex = maxStockSize-num;
		System.out.println("------------------------");
		System.out.println("maxStockSize="+maxStockSize+"\t"+stockList);
		List<Double> emaList = new ArrayList<Double>();//用来保存ema的值
		Map<Integer,Double> emaMap = new HashMap<Integer,Double>();
		int m=0;
		for(int i=beginIndex ;i>=0;i--){
			System.out.println("i="+i+"\t"+num+"\t beginIndex="+beginIndex);
			List<Double> tmpList = new ArrayList<Double>(); 
			double emaValue  = 0d;
			double currentValue = stockList.get(i);
			if(i==beginIndex){
				for(int x=0;x<num;x++){
					tmpList.add(stockList.get(i+x));
				}
				 emaValue = emaStockCloseInit(tmpList,num);
				emaList.add( emaValue); 
				// System.out.println("first emaValue=="+emaValue+"\t"+tmpList);
				m =  m+1;
			}else { 
					Double emaInMap = emaMap.get(i+1);
					if(emaInMap!=null){     
						 emaValue =  emaStockCloseByEma(currentValue,emaInMap,num);
						 System.out.println("emaStockCloseByEma="+currentValue+"\t"+emaInMap+"\t"+emaValue);
					 
					} 
				emaList.add(emaValue);  
				m =  m+1;
			}
			emaMap.put(i, emaValue); 
			System.out.println("i="+i+"\t tmpList="+tmpList+"\t emaList="+emaList+"\t emaMap="+emaMap);
		}
		System.out.println("x="+emaMap+"\t"+num);
	//	double x =MathUtil.format( stockList.get(stockList.size()-num));
	//	double s = (2*x+(num-1)*emaStockClose(stockList,num-1))/(num+1);
	//	return MathUtil.format(s);
		return 0;
	}
	
 
	
	
	/**
	 * 
	 *  SMA(X，N，M)　X的M日加权移动平均，
		M为权重，如Y=(X*M+Y'*(N-M))/N 
	 */
	public static double sma(double x,int n,int m){ 
		if(n<=m){
			System.out.println("sma defalut:"+m+"\t"+n);
			return x;
		}
		double s =   (x * m +  sma(x,(n-1),m) *(n-m) )/n;
		System.out.println("sma next:"+m+"\t"+n+"\t"+s);
		return s;
	}
}
