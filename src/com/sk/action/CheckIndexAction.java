package com.sk.action;

import java.io.BufferedReader;
import java.io.File; 
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateUtils;

import com.sk.bean.DayStock;
import com.sk.bean.Stock;
import com.sk.config.Config;
import com.sk.service.BuildStockService;
import com.sk.service.CountStockMaService;
import com.sk.service.DayStockService;
import com.sk.service.MacdService;
import com.sk.service.RsiService;
import com.sk.util.DateUtil;
import com.sk.util.FileUtil;
import com.sk.util.MathUtil;

public class CheckIndexAction {
 

    //add wangjia
	public static int checkResultSuccess =0;
	public static int checkResultFail =0;


    public static int dapan =2425;
    public static int dapan20 =2365;
    public static int dapan60 =2308;

	 
	
	/**
	 * 检查股票成交量
	 * @param stock src/com/sk/bean/DayStockMa.java
	 */
	public static int checkStockVol(Stock stock) { 
		int m=0;
		
		if(stock!=null && stock.getDayStockList()!=null) {
			for(DayStock dayStock :stock.getDayStockList()) {
				int dayStockIndex = dayStock.getIndex();
				if(dayStock!=null&& stock.getDayStockByIndex(dayStockIndex-2)!=null&& dayStock.getYesterdayStock()!=null
						&& dayStock.getYesterdayStock().getDayStockMa().getVolMa5()!=null && stock.getDayStockByIndex(dayStockIndex-2).getDayStockMa().getVolMa5()!=null){
					if(!dayStock.getDate().startsWith("2014-10-20")){
						continue;
					}
		
			 
							double d1= (double)  (dayStock.getDayStockMa().getVolMa5()*1000)/(dayStock.getYesterdayStock().getDayStockMa().getVolMa5()*1000.0);   
							double d2= (double)  (dayStock.getYesterdayStock().getDayStockMa().getVolMa5()*1000)/(stock.getDayStockByIndex(dayStockIndex-2).getDayStockMa().getVolMa5()*1000.0);  
						 	 
							double d3= (double)  (dayStock.getDayStockMa().getVolMa10()*1000)/(dayStock.getYesterdayStock().getDayStockMa().getVolMa10()*1000.0);   
						//	
					 		double ma20rise= (double)  ((dayStock.getClosePrice() - dayStock.getDayStockMa().getPriceMa20())*100)/(dayStock.getClosePrice()*1.0);   
							double ma60rise= (double)  ((dayStock.getClosePrice() - dayStock.getDayStockMa().getPriceMa60())*100)/(dayStock.getClosePrice()*1.0);   
							
							double mavol34 = (double)  (dayStock.getVol()*1000)/(dayStock.getYesterdayStock().getDayStockMa().getVolMa34()*1000.0);   
							double ma20dapan =( (dapan-dapan20)*100d/dapan);
							double ma60dapan =( (dapan-dapan60)*100d/dapan);
							
								//	System.out.println("currentRsi=="+dayStock.getDayStockIndex()+"\t"+currentRsi);
							boolean checkVolM5 = dayStock.getDayStockMa().getVolMa5()>dayStock.getYesterdayStock().getDayStockMa().getVolMa5();
							
							 boolean check1 = d1>1.10d && d1<1.40d && d2>1.00d && d2<1.40d && d3>1.00d && d1+0.05>=d2;
							 boolean check2 =  (d1>1.05 && d1<1.80d && d2<0.97 && d2>0.7 && d3>1.00d ) ;
							 boolean check3 = ((d1 >1.2 && d1<2.0d && d3>1.00d) );
							 // ||(mavol34>1.05d && mavol34<2.5)
							 boolean checkPrice120And250 = dayStock.getClosePrice()>dayStock.getDayStockMa().getPriceMa120() 
										&& dayStock.getClosePrice()>dayStock.getDayStockMa().getPriceMa250();
										
							boolean checkVolMa34= dayStock.getVol()> dayStock.getDayStockMa().getVolMa34();
							boolean checkYesterdayVolMa34= dayStock.getYesterdayStock().getVol() <= dayStock.getYesterdayStock().getDayStockMa().getVolMa34();
							
							boolean checkRiseRage = (dayStock.getRise()>-0.02 && dayStock.getRise()<0.06);
							
							boolean dayMa60And20 = ( (dayStock.getClosePrice()>dayStock.getDayStockMa().getPriceMa60() )
									&& ( dayStock.getClosePrice()*1.05>dayStock.getDayStockMa().getPriceMa20()));
							
							boolean dayMa60= dayStock.getClosePrice() * 1.1 >dayStock.getDayStockMa().getPriceMa60() 
									&& dayStock.getClosePrice() * 0.9 < dayStock.getDayStockMa().getPriceMa60() ; 
							
							boolean isDiliang = CountStockMaService.isDiLiang(stock, dayStock);
							// if(  ma20rise>ma20dapan  && ma60rise > ma60dapan && check5  && check6  &&   dayMa60And20  && check7)
							 //	if(RsiService.checkRsi(dayStock) ) {
							if(  checkVolM5 && ( check1 || check2 || check3 )   && DayStockService.checkRiseForVolBig(stock, dayStock) ) 	{
							//		if(true|| checkVolM5 && ( check1 || check2 || check3 )  & checkVolMa34  && checkYesterdayVolMa34  ) 	{
							//			if(    isDiliang &&   checkPrice120And250   && dayMa60And20 && (RsiService.checkDiLiangRsi(stock,dayStock) )  	&& (MacdService.checkDiLiangMacd (stock,dayStock) )  ) {
						  	//	 	if(  checkVolM5 && ( check1 || check2 || check3 )  & checkVolMa34  && checkYesterdayVolMa34 )  {
					 			System.out.println("------------------------------------------");
				 
								if(check1){
                                    //十日均线必须往上
									System.out.print("成交量缓慢上涨 \t");
								}
								if(check2){
									System.out.print("暴利拐头 \t");
								}
								if(check3){
									System.out.print("成交量暴涨； ");
								}  
						 
								double checkresult5 = 0;
								double checkresult2 = 0;
								
								if(dayStock.getNextOneStock()!=null){ 
									checkresult2 = checkresult2 + dayStock.getNextOneStock().getRise();
									checkresult5 = checkresult5 + dayStock.getNextOneStock().getRise();
								}
								if(stock.getDayStockByIndex(dayStockIndex+2)!=null){ 
									checkresult5 = checkresult5 + stock.getDayStockByIndex(dayStockIndex+2).getRise();
									checkresult2 = checkresult2 + stock.getDayStockByIndex(dayStockIndex+2).getRise();
								}
								if(stock.getDayStockByIndex(dayStockIndex+3)!=null){ 
									checkresult5 = checkresult5 + stock.getDayStockByIndex(dayStockIndex+3).getRise();
								}
								if(stock.getDayStockByIndex(dayStockIndex+4)!=null){ 
									checkresult5 = checkresult5 + stock.getDayStockByIndex(dayStockIndex+4).getRise();
								}
								if(stock.getDayStockByIndex(dayStockIndex+5)!=null){ 
									checkresult5 = checkresult5 + stock.getDayStockByIndex(dayStockIndex+5).getRise();
								} 
                                 
                                if(checkresult5*100>5 || checkresult2*100>3){
                                        checkResultSuccess++;
                                        DayStockService.getCheckInfo(stock,dayStock);
                                        System.out.println("成功 2日" + MathUtil.format(checkresult2*100)+"%" +"\t 五日："+ MathUtil.format(checkresult5*100)+"%");
                                 } else {
                                        checkResultFail++;
                                        DayStockService.getCheckInfo(stock,dayStock);
                                        System.out.println("失败 2日"+ MathUtil.format(checkresult2*100)+"%" +"\t 五日："+ MathUtil.format(checkresult5*100)+"%");
                                }
                                 
						 		System.out.println("");
								  m++;
							}
						}
					
				
			} 
		}
		return m;
	}
	public static void main(String[] args) {
		Stock stock = null;
			//	stock = ExportStock.exportStock("002066");
		  // stock = ExportStock.exportStock("SH601801");
		 //  stock = ExportStock.exportStock("SH600217");
		//   stock = ExportStock.exportStock("300001");
		//   checkStockVol(stock);
		  stock = BuildStockService.exportStock("002721");
		//	 checkStockVol(stock);
			 if(1==1){
			//	 return ;
			 }
		int m = 0;
		int n = 0;
		long ctime = new Date().getTime();
		File file = new File(Config.stockFilePath);
		String test[];
		test = file.list();
		for (int i = 0; i < test.length; i++) {
		
			stock = BuildStockService.exportStock(test[i]);
		 
			if (stock != null) {
				m++;
				int c = checkStockVol(stock);
				 n = n+c;
			} 
			 
		}
		long ctime1 =  new Date().getTime();

		System.out.println("耗时==="+(( ctime1-ctime)/1000)+"秒");
		if(n==0){n=1;}
		System.out.println("check "+m+"支，共计"+n+"支符合"+"\t "+checkResultSuccess+"成功，"
		+checkResultFail+"失败；成功率="+MathUtil.format((checkResultSuccess*100d/n))+"%");
		 
	}
}
