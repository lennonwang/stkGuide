package com.sk.service;

import com.sk.bean.DayStock;
import com.sk.bean.Stock;
import com.sk.util.MathUtil;

public class DayStockService {

	
	public static void getCheckInfo(Stock stock, DayStock dayStock){
			int dayStockIndex= dayStock.getIndex();
			System.out.println();
			System.out.println("前四天:"+DayStockService.getDayStockInfo(stock.getDayStockByIndex(dayStockIndex-4)));
			System.out.println("前三天:"+DayStockService.getDayStockInfo(stock.getDayStockByIndex(dayStockIndex-3)));
			System.out.println("前两天:"+DayStockService.getDayStockInfo(stock.getDayStockByIndex(dayStockIndex-2)));
			System.out.println("前一天:"+DayStockService.getDayStockInfo(stock.getDayStockByIndex(dayStockIndex-1)));
			System.out.println("今天:"+DayStockService.getDayStockInfo(dayStock));
			System.out.println("----"); 
			System.out.println("后一天:"+DayStockService.getDayStockInfo(stock.getDayStockByIndex(dayStockIndex+1))); 
			System.out.println("后二天:"+DayStockService.getDayStockInfo(stock.getDayStockByIndex(dayStockIndex+2))); 
			System.out.println("后三天:"+DayStockService.getDayStockInfo(stock.getDayStockByIndex(dayStockIndex+3)));
			System.out.println("后四天:"+DayStockService.getDayStockInfo(stock.getDayStockByIndex(dayStockIndex+4))); 
			System.out.println("后五天:"+DayStockService.getDayStockInfo(stock.getDayStockByIndex(dayStockIndex+5))); 
	}
	public static String getDayStockInfo(DayStock dayStock){
		String s ="";
		if(dayStock!=null){
			s = dayStock+"\t"+dayStock.getRiseRate();
			if(dayStock.getDayStockIndex()!=null){
				s = s + "\t rsi:"+dayStock.getDayStockIndex().getRsi6();
			}
			if(dayStock.getDayStockIndexMacd()!=null){
				s = s + "\t diff="+dayStock.getDayStockIndexMacd().getMacdDiff()+",dea="+dayStock.getDayStockIndexMacd().getMacdDea()
						+",macd="+dayStock.getDayStockIndexMacd().getMacd();
			}
			if(dayStock.getDayStockMa()!=null){
				double d1 = (100d*(dayStock.getClosePrice()-dayStock.getDayStockMa().getPriceMa20()))/(dayStock.getDayStockMa().getPriceMa20()+0.00001);
				double d2 = (100d*(dayStock.getClosePrice()-dayStock.getDayStockMa().getPriceMa60()))/(dayStock.getDayStockMa().getPriceMa60()+0.00001);
				s = s + "\t ma20="+MathUtil.format(d1)
						+"%,ma60="+MathUtil.format(d2)+"%"; 
			}
			if(dayStock.getDayStockMa()!=null){
				s = s + "\t vol:ma5="+MathUtil.format(1d*dayStock.getVol()/(dayStock.getDayStockMa().getVolMa5()+1))
						+"倍,ma34="+MathUtil.format(1d*dayStock.getVol()/(dayStock.getDayStockMa().getVolMa34()+1))+"倍"; 
			} 
		}
		return s;
	}
	
	public static boolean isYiZiBan(DayStock dayStock){ 
		if(  dayStock.getRise()>0.095    ){
			if( (int)(dayStock.getClosePrice()*100)== (int)(dayStock.getOpenPrice()*100) ){ 
				return true;
			}
		}
		return false;
	}
	
	
	public static boolean checkRiseForVolBig(Stock stock,DayStock dayStock){   
		DayStock preStock = dayStock.getYesterdayStock();  
		DayStock pyStock = stock.getDayStockByIndex(dayStock.getIndex()-2);
		if(dayStock.getRise()+preStock.getRise() +pyStock.getRise()<-0.02){
			System.out.println("openRise=="+dayStock+"\t"+dayStock.getRise()+"\t"+preStock.getRise()+"\t"+pyStock.getRise());
			return false;
		}
		if(dayStock.getRise() < -0.02){
			return false;
		}
		if(RsiService.checkBigVolRsi(stock, dayStock)==false ){
			   return false;
		}
		return true;
	}
	
}
