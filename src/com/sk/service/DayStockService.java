package com.sk.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sk.bean.DayStock;
import com.sk.bean.Stock;
import com.sk.util.MathUtil;

public class DayStockService {

	
	public static void getCheckInfo(Stock stock, DayStock dayStock){
			int dayStockIndex= dayStock.getIndex();
			System.out.println();
			System.out.println("前五天:"+DayStockService.getDayStockInfo(stock.getDayStockByIndex(dayStockIndex-5)));
			System.out.println("前四天:"+DayStockService.getDayStockInfo(stock.getDayStockByIndex(dayStockIndex-4)));
			System.out.println("前三天:"+DayStockService.getDayStockInfo(stock.getDayStockByIndex(dayStockIndex-3)));
			System.out.println("前两天:"+DayStockService.getDayStockInfo(stock.getDayStockByIndex(dayStockIndex-2)));
			System.out.println("前一天:"+DayStockService.getDayStockInfo(stock.getDayStockByIndex(dayStockIndex-1)));
			System.out.println("今天:"+DayStockService.getDayStockInfo(dayStock));
			System.out.println("----"); 
			if(stock.getDayStockByIndex(dayStockIndex+1)!=null){
				System.out.println("后一天:"+DayStockService.getDayStockInfo(stock.getDayStockByIndex(dayStockIndex+1))); 
			}
			if(stock.getDayStockByIndex(dayStockIndex+2)!=null){
				System.out.println("后二天:"+DayStockService.getDayStockInfo(stock.getDayStockByIndex(dayStockIndex+2))); 
			}
			if(stock.getDayStockByIndex(dayStockIndex+3)!=null){
				System.out.println("后三天:"+DayStockService.getDayStockInfo(stock.getDayStockByIndex(dayStockIndex+3)));
			}
			if(stock.getDayStockByIndex(dayStockIndex+4)!=null){
				System.out.println("后四天:"+DayStockService.getDayStockInfo(stock.getDayStockByIndex(dayStockIndex+4))); 
			}
			if(stock.getDayStockByIndex(dayStockIndex+5)!=null){
				System.out.println("后五天:"+DayStockService.getDayStockInfo(stock.getDayStockByIndex(dayStockIndex+5))); 
			}
	}

	public static String getDayStockInfo(DayStock dayStock){
		String s ="";
		if(dayStock!=null){
			s = dayStock+"\t"+dayStock.getRiseRate();
            if(dayStock.getDayStockIndex()!=null){
                s = s + "\t rsi:"+dayStock.getDayStockIndex().getRsi6() ;
            }
            if(dayStock.getDayStockIndex()!=null){
            	double currentRise =0d; 
            	if(dayStock.getDayStockIndex().getBoll()>0){
            		currentRise = 100d * (dayStock.getDayStockIndex().getBollUpper() 
        				-  dayStock.getDayStockIndex().getBoll())/ dayStock.getDayStockIndex().getBoll();
            	}
            	double nextRise =0d;
            	if( dayStock.getDayStockIndex().getNextBoll()>0){
            		nextRise = 100d * (dayStock.getDayStockIndex().getNextBollUpper() 
        				-  dayStock.getDayStockIndex().getNextBoll())/ dayStock.getDayStockIndex().getNextBoll();
            	} 
            	s = s + "\t bollRise:"+ MathUtil.format( currentRise ) +","+  MathUtil.format( nextRise );
            }
			if(dayStock.getDayStockIndexMacd()!=null){
				s = s + "\t macd("+dayStock.getDayStockIndexMacd().getMacdDiff()+","+dayStock.getDayStockIndexMacd().getMacdDea()
						+",m"+dayStock.getDayStockIndexMacd().getMacd()+")";
			}
			if(dayStock.getDayStockIndex()!=null){
                s = s + "\t kdj("+MathUtil.format(dayStock.getDayStockIndex().getKdjD())+","+dayStock.getDayStockIndex().getKdjPointNumber()+")" ;
            }
			if(dayStock.getDayStockMa()!=null){
				double d1 = (100d*(dayStock.getClosePrice()-dayStock.getDayStockMa().getPriceMa20()))/(dayStock.getDayStockMa().getPriceMa20()+0.00001);
				double d2 = (100d*(dayStock.getClosePrice()-dayStock.getDayStockMa().getPriceMa60()))/(dayStock.getDayStockMa().getPriceMa60()+0.00001);
				s = s + "\t ma20,60="+MathUtil.format(d1)
						+"%,="+MathUtil.format(d2)+"%"; 
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
	
	public static boolean isLimitUp(DayStock dayStock){ 
		if(  dayStock.getRise()>0.096    ){ 
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param dayStock
	 * @return
	 */
	public static boolean isBigRedStock(DayStock dayStock){ 
		if(  dayStock!=null && dayStock.getRise()>0.070    ){ 
			return true;
		}
		return false;
	}
	
	/**
	 * 判断是否是红十字星
	 * @param dayStock
	 * @return
	 */
	public static boolean isRedStarStock(DayStock dayStock){ 
		if(  dayStock!=null && dayStock.getMaxPrice()>dayStock.getClosePrice() && dayStock.getMinPrice()<dayStock.getClosePrice()
				&&  (dayStock.getMaxPrice()-dayStock.getClosePrice() ) > (dayStock.getClosePrice()-dayStock.getOpenPrice()) 
			){ 
			if(dayStock.getYesterdayStock()!=null && dayStock.getClosePrice()>dayStock.getYesterdayStock().getClosePrice()){ 
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
	
	
	
	/**
	 * 判断短期均线是否走向粘合
	 * @param dayStock
	 * @return
	 */
	public static boolean gtLongMa(DayStock dayStock){
		List<Double> list = new ArrayList<Double>(); 
		list.add(dayStock.getDayStockMa().getPriceMa30() );
		list.add(dayStock.getDayStockMa().getPriceMa60() );
		list.add(dayStock.getDayStockMa().getPriceMa120() );
		list.add(dayStock.getDayStockMa().getPriceMa250() );   
		Collections.sort(list);
		double max = Math.max( list.get(0),  list.get(3));
		if(dayStock.getClosePrice()>max*0.95){ 
				return true; 
		}
		return false;
	}
	
}
