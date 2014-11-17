package com.sk.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.sk.bean.DayStock;
import com.sk.bean.Stock;
import com.sk.service.BuildStockService;

public class MaUtilBak {

	public void ma(){
		
	}
	
	/**
	 * 求X的N日指数平滑移动平均。
算法是：若Y=EMA(X，N)，则Y=〔2*X+(N-1)*Y’〕/(N+1)，
其中Y’表示上一周期的Y值。
2 是平滑系数，表示今天的权重是2.
公式含义为。今天值乘以权重2，加上历史积累值 除以 天数加1, 因为当天权重加了1.

EMA引用函数在计算机上使用递归算法很容易实现，但不容易理解。
例举分析说明EMA函数。
X是变量，每天的X值都不同，从远到近地标记，它们分别记为X1，X2，X3，….，Xn
如果N=1，则EMA(X，1)=〔2*X1+(1-1)*Y’〕/(1+1)=X1
如果N=2，则EMA(X，2)=〔2*X2+(2-1)*Y’〕/(2+1)=(2*X2+X1)/3
如果N=3，则EMA(X，3)=〔2*X3+(3-1)*Y’〕/(3+1)= (2*X3+2/3*(2*X2+*X1))/4=(3*X3+2*X2+X1)/6
如果N=4，则EMA(X，4)=〔2*X4+(4-1)*Y’〕/(4+1)= (4*X4+3*X3+2*X2+X1)/10
这么神奇，找到规律了吗？
EMA(x,5) = (2*x5 + 4*y')/6 = (5*x5+4*x4+3*x3+2*x2+x1)/15;
任何时候， 分子的系数之后等于分母。 越靠近当前，系数越大。
它考虑的是当前的值要有较大的优先权，越远的值，贡献越小。

举例：
有一组数据（收盘价为）：1,2,3,4,5,6,7，求其ma(c,5), EMA（c，5）
解答：对应上面数据，X1，X2，X3，X4，X5分别对应 3、4、5、6、7
MA(c,5)=(3+4+5+6+7)/5=5
EMA（c，5）＝(5*X5+4*X4+3*X3+2*X2+1*X1)/15=5.67

	 * EMA(X，N)
	 */
	public static void emaStockClose(Stock stock,int num) {
		int maxPre= 100+num;
		if(stock!=null && stock.getDayStockList()!=null) {
			for(DayStock dayStock :stock.getDayStockList()) {
				String dayDateString = dayStock.getDate();   
				Integer indexInList = stock.getDayStockIndexMap().get(dayStock.toString()); 
				double dClose = dayStock.getClosePrice();
				//〔2*X+(N-1)*Y’〕/(N+1)
				List<Double> doubleList =new ArrayList<Double>();
				doubleList.add(dClose);
				if(indexInList>maxPre){
					for(int i=0;i<maxPre-1;i++){
						DayStock ds = stock.getDayStockList().get(indexInList-(i+1));
						doubleList.add(ds.getClosePrice());
					} 
					double ema = emaStockCloseDiGui(doubleList,indexInList,num);
					System.out.println("ema="+dayStock+"\t"+ema);
				} 
			}
		}
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
	
	/**
	 * stockList:股价从今天、昨天、前天、以此排列；
	 * @param stockList
	 * @param index
	 * @param num
	 * @return
	 */
	public static double emaStockCloseDiGui(List<Double> stockList,int index ,int num) {
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
			if(i==beginIndex){
				for(int x=0;x<num;x++){
					tmpList.add(stockList.get(i+x));
				}
				 emaValue = emaStockCloseInit(tmpList,num);
				emaList.add( emaValue); 
				// System.out.println("first emaValue=="+emaValue+"\t"+tmpList);
				m =  m+1;
			}else {
				for(int x=0;x<num;x++){
					Double emaInMap = emaMap.get(i+x);
					if(emaInMap==null){  
					//	System.out.println(" emaValue no map=="+stockList.get(i+x)+"\t"+(i+x));
						tmpList.add(stockList.get(i+x));
					}else { 
					//	System.out.println(" emaValue in map=="+emaInMap+"\t"+(i+x));
						tmpList.add(emaInMap);
					}
				}
				// tmpList.add(emaList.get(m-1));
				 emaValue = emaStockCloseInit(tmpList,num);
				emaList.add(emaValue); 
				System.out.println("next emaValue=="+emaValue);
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
	
	public static void main(String[] args) {
		Stock stock = null;
		//	stock = ExportStock.exportStock("002066");
		  // stock = ExportStock.exportStock("SH601801");
		 //  stock = ExportStock.exportStock("SH600217");
		//   stock = ExportStock.exportStock("300001");
		//   checkStockVol(stock);
		  stock = BuildStockService.exportStock("002057");
		//  countStockRsi(stock,6);
		  MaUtilBak.emaStockClose(stock, 3); 
		 
	}
}
