package com.sk.service;

import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;

import com.sk.bean.DayStock;
import com.sk.bean.Stock;
import com.sk.config.Config;
import com.sk.util.DateUtil;
import com.sk.util.FileUtil;

public class BuildStockService {


	
 
	/***
	 * 通过StockId获取Stock对象和每天DayStock的List信息
	 * @param stockId
	 * @return
	 */
	public static Stock exportStock(String stockId){
		String szOrSh="";
		if(stockId.startsWith("6")){
			 szOrSh="SH";
		}else if(stockId.startsWith("0")||stockId.startsWith("3")){
			 szOrSh="SZ";
		}
		String s = Config.stockFilePath.concat(File.separator).concat(szOrSh).concat(stockId);
		if(!stockId.contains(".txt")){
			s = s.concat(".txt");
		}
		StringBuffer sb = new StringBuffer();
		 Stock stock = null;
		try {  
			File f = new File(s);
			if(!f.exists()){
				return null;
			}
			BufferedReader d = FileUtil.getFileContent(f);
			String line = "";  
			stock = new Stock();
			List dayStockList = new ArrayList<DayStock>();
			Map dayStockMap = new HashMap<String,DayStock>();
			Map<String,Integer> dayStockIndexMap = new HashMap<String,Integer>();
			Integer index = 0;
			while ((line = d.readLine()) != null) {
				String[] items = line.split(",");
				if(items!=null && items.length >= 5){ 
					DayStock dayStock = new DayStock();
					dayStock.setId(stockId);
					Date tmpDate = DateUtil.stringToDate(items[0], "MM/dd/yyyy");
					String dayDate = DateUtil.dateToString(tmpDate,"yyyy-MM-dd") ;
					dayStock.setDate(dayDate); 
					if(dayDate.compareTo("2012-10")<= 0) {
					 	continue; 
					}  
					dayStock.setOpenPrice(NumberUtils.toDouble(items[1]));
					dayStock.setMaxPrice(NumberUtils.toDouble(items[2]));
					dayStock.setMinPrice(NumberUtils.toDouble(items[3]));
					dayStock.setClosePrice(NumberUtils.toDouble(items[4]));
					dayStock.setVol(NumberUtils.toLong(items[5]));
					dayStock.setAmount(NumberUtils.toLong(items[6]));
					dayStockList.add(index,dayStock);
					dayStockMap.put(dayStock.toString(),dayStock);
					dayStockIndexMap.put(dayStock.toString(),index); 
					index  =   index+1;  
				//	System.out.println("line=="+line);
					sb.append(line + "\n");
				}
			}
			d.close();    
			stock.setId(stockId);
			stock.setDayStockList(dayStockList);
			stock.setDayStockIndexMap(dayStockIndexMap);  
			stock.setDayStockMap(dayStockMap); 
			initStock(stock);
			return stock;
		} catch (Exception e) { 
			e.printStackTrace();
			return null;
		} finally { 
		} 
	}
	 
	public static void initStock(Stock stock) { 
		if(stock!=null && stock.getDayStockList()!=null) { 
			
			
			CountStockMaService.initStockMa(stock) ;
			
			//初始化昨天和前天的，每天Stock情况
			for(DayStock dayStock :stock.getDayStockList()) {
				String dayDateString = dayStock.getDate(); 
				Date dayDate = DateUtil.stringSimpleToDate(dayDateString); 
				Integer indexInList = stock.getDayStockIndexMap().get(dayStock.toString());
				int maxSize =  stock.getDayStockIndexMap().keySet().size();
				if(indexInList>1){
					DayStock yesterdayStock = stock.getDayStockList().get(indexInList-(1));
					if(yesterdayStock!=null){ 
						dayStock.setYesterdayStock(stock.getDayStockMap().get(yesterdayStock.toString()));
					} 
				}
				if(indexInList>2){
					DayStock beforeYesterdayStock = stock.getDayStockList().get(indexInList-(2));
					if(beforeYesterdayStock!=null){ 
						dayStock.setBeforeYesterdayStock(stock.getDayStockMap().get(beforeYesterdayStock.toString()));
					}   
				}
				if(indexInList<maxSize){
					if(indexInList+1<maxSize){ 
						DayStock nextOneStock = stock.getDayStockList().get(indexInList+1);
						dayStock.setNextOneStock(stock.getDayStockMap().get(nextOneStock.toString()));
					}
					if(indexInList+2<maxSize){ 
						DayStock tmpStock = stock.getDayStockList().get(indexInList+2);
						dayStock.setNextTwoStock(stock.getDayStockMap().get(tmpStock.toString()));
					}
					if(indexInList+3<maxSize){ 
						DayStock tmpStock = stock.getDayStockList().get(indexInList+3);
						dayStock.setNextThreeStock(stock.getDayStockMap().get(tmpStock.toString()));
					}
					if(indexInList+4<maxSize){ 
						DayStock tmpStock = stock.getDayStockList().get(indexInList+4);
						//System.out.println("[setNextFourStock]:"+dayStock.toString()+tmpStock.toString()+"\t"+indexInList+"\t"+maxSize);
						dayStock.setNextFourStock(stock.getDayStockMap().get(tmpStock.toString()));
					}
					if(indexInList+5<maxSize){ 
						DayStock tmpStock = stock.getDayStockList().get(indexInList+5);
						dayStock.setNextFiveStock(stock.getDayStockMap().get(tmpStock.toString()));
					}
				}
				
			}
		}
		
		for(DayStock dayStock :stock.getDayStockList()) {
			if(dayStock.getYesterdayStock()!=null){
				if(dayStock.getYesterdayStock().getDayStockMa().getVolMa5()==null){
					dayStock.setYesterdayStock(stock.getDayStockMap().get(dayStock.getYesterdayStock().toString()));
				}
			}
			if(dayStock.getBeforeYesterdayStock()!=null){
				if(dayStock.getBeforeYesterdayStock().getDayStockMa().getVolMa5()==null){
					dayStock.setBeforeYesterdayStock(stock.getDayStockMap().get(dayStock.getBeforeYesterdayStock().toString()));
				}
			}
		}
	}
	 
}
