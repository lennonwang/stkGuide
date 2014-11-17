package com.sk.action;

import java.io.File;

import com.sk.util.MathUtil;

import com.sk.bean.DayStock;
import com.sk.bean.Stock;
import com.sk.config.Config;
import com.sk.service.BuildStockService;
import com.sk.service.DayStockService;

public class CheckStockAction {
 

    //add wangjia
	public static int checkResultSuccess =0;
	public static int checkResultFail =0;


    public static int dapan =2473;
    public static int dapan20 =2373;
    public static int dapan60 =2316;

	 
	
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
					if(!dayStock.getDate().startsWith("2014-11-11")){
						continue;
					}
					if(dayStock.getDayStockMa().getVolMa5()>dayStock.getYesterdayStock().getDayStockMa().getVolMa5()){
					
							double d1= (double)  (dayStock.getDayStockMa().getVolMa5()*1000)/(dayStock.getYesterdayStock().getDayStockMa().getVolMa5()*1000.0);   
							double d2= (double)  (dayStock.getYesterdayStock().getDayStockMa().getVolMa5()*1000)/(stock.getDayStockByIndex(dayStockIndex-2).getDayStockMa().getVolMa5()*1000.0);  
						 	 
							double d3= (double)  (dayStock.getDayStockMa().getVolMa10()*1000)/(dayStock.getYesterdayStock().getDayStockMa().getVolMa10()*1000.0);   
						//	
							double d4= (double)  (dayStock.getVol()*1000)/(dayStock.getYesterdayStock().getVol()*1000.0);   
							double d5= (double)  (dayStock.getYesterdayStock().getVol()*1000)/(stock.getDayStockByIndex(dayStockIndex-2).getVol()*1000.0);   
							
							double ma20rise= (double)  ((dayStock.getClosePrice() - dayStock.getDayStockMa().getPriceMa20())*100)/(dayStock.getClosePrice()*1.0);   
							double ma60rise= (double)  ((dayStock.getClosePrice() - dayStock.getDayStockMa().getPriceMa60())*100)/(dayStock.getClosePrice()*1.0);   
							
							double mavol34 = (double)  (dayStock.getVol()*1000)/(dayStock.getYesterdayStock().getDayStockMa().getVolMa34()*1000.0);   
							double ma20dapan =( (dapan-dapan20)*100d/dapan);
							double ma60dapan =( (dapan-dapan60)*100d/dapan);
							
							 boolean check1 = d1>1.10d && d1<1.40d && d2>1.00d && d2<1.40d && d3>1.00d && d1+0.05>=d2;
							 boolean check2 =  (d1>1.05 && d1<1.80d && d2<0.97 && d2>0.7 && d3>1.00d ) ;
							 boolean check3 = ((d1 >1.2 && d1<2.0d && d3>1.00d) );
							 // ||(mavol34>1.05d && mavol34<2.5)
							 boolean check4 = dayStock.getClosePrice()>dayStock.getDayStockMa().getPriceMa120() 
										&& dayStock.getClosePrice()>dayStock.getDayStockMa().getPriceMa250();

							boolean check5= dayStock.getVol()> dayStock.getDayStockMa().getVolMa34();
							boolean check6= dayStock.getYesterdayStock().getVol() <= dayStock.getYesterdayStock().getDayStockMa().getVolMa34();
							
							boolean check7 = (dayStock.getRise()>-0.01 && dayStock.getRise()<0.06);
							
							boolean dayMa60And20 = ( (dayStock.getClosePrice()>dayStock.getDayStockMa().getPriceMa60() )
									&& ( dayStock.getClosePrice()*1.03>dayStock.getDayStockMa().getPriceMa20()));
							if( check4 && ( check1 || check2 || check3 ) ) {
								System.out.println("------------------------------------------");
								System.out.println("check 当天:"+dayStock+"\t"+dayStock.getRiseRate() );  
								System.out.println("大盘 当天:"+String.format("%.2f", ma20dapan)+("%") +"\t"+String.format("%.2f", ma20rise)+("%") +"\t 大盘60:"+String.format("%.2f", ma60dapan)+("%") +"\t"+String.format("%.2f", ma60rise)+("%")
										);  
								if(check1){
                                    //十日均线必须往上
									System.out.print("成交量缓慢上涨 \t");
								}
								if(check2){
									System.out.print("暴利拐头 \t");
								}
								if(check3){
									System.out.print("成交量暴涨；ma34:"+String.format("%.2f", mavol34)+"倍 ；五日均线今天是昨天的倍数："+String.format("%.2f", d1)+"\t");
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
                                    System.out.println("成功 2日" + MathUtil.format(checkresult2 * 100)+"%" +"\t 五日："+ MathUtil.format(checkresult5*100)+"%");
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
			 checkStockVol(stock);
			 if(1==1){
				// return ;
			 }
		int m = 0;
		int n = 0;
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
		System.out.println("check "+m+"支股票，共计"+n+"支符合"+"\t "+checkResultSuccess+"成功，"+checkResultFail+"失败");
		 
	}
}
