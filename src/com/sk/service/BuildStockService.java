package com.sk.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.spy.memcached.MemcachedClient;

import org.apache.commons.lang.math.NumberUtils;

import com.sk.bean.DayStock;
import com.sk.bean.Stock;
import com.sk.cache.MemcachedManager;
import com.sk.config.Config;
import com.sk.util.DateUtil;
import com.sk.util.FileUtil;

public class BuildStockService {


	public static final int EXPIRE_FOUR_HOUR = 4 * 60 * 60;
	
	public static void writeBuffered(Stock test, String fileName) throws IOException {
		  ObjectOutputStream objectOutputStream = null;
		  try {
		    RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
		    FileOutputStream fos = new FileOutputStream(raf.getFD());
		    objectOutputStream = new ObjectOutputStream(fos);
		    objectOutputStream.writeObject(test);
		  } finally {
		    if (objectOutputStream != null) {
		      objectOutputStream.close();
		    }      
		}
	}
	public static Stock  readBuffered(File cacheFile ) throws IOException {
		 ObjectInputStream ois=null;
		 Stock stock = null;
		 try {
	         FileInputStream fis = new FileInputStream(cacheFile);
	        ois = new ObjectInputStream(fis);
	           stock = (Stock) ois.readObject(); 
	         ois.close();
		} catch (Exception ex) {
			         ex.printStackTrace();
		} finally {
		    if (ois != null) {
		    	ois.close();
			    }      
		} 
         return stock;
	 }
 
	/***
	 * 通过StockId获取Stock对象和每天DayStock的List信息
	 * @param stockId
	 * @return
	 */
	public static Stock exportStock(String stockId){
		
		StringBuffer sb = new StringBuffer();
		 Stock stock = null;
		try {  
//			String cacheFileName = getCachePath(stockId);
//			File cacheFile = new File(cacheFileName);
//			if(cacheFile.exists()){  
//				stock = readBuffered(cacheFile);
//				if(stock!=null){ 
//				//	System.out.println("f.exists() from cache"+ cacheFileName);
//					return stock;
//				}
//			}
			String mKey = "m_"+DateUtil.getDate("yyyyMMdd")+stockId;
			 MemcachedClient c= MemcachedManager.getInstance();  
			 Stock stockFromCache = (Stock)c.get(mKey);
			 if(stockFromCache!=null) {
			//	 System.out.println("stock from memcache"+ stockFromCache);
				 return stockFromCache;
			 }
			File f = new File(getFilePath(stockId));
			if(!f.exists()){
				System.out.println("f.exists() false"+getFilePath(stockId));
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
					if(dayDate.compareTo("2014-01")<= 0) {
					 	continue; 
					}  
					dayStock.setIndex(index);
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
			 c.set(mKey, EXPIRE_FOUR_HOUR, stock);
		 //	writeBuffered(stock,cacheFileName);
			return stock;
		} catch (Exception e) { 
			e.printStackTrace();
			return null;
		} finally { 
		} 
	}
	
	public static String getCachePath(String stockId){
		String name = Config.stockCacheFilePath.concat(File.separator).concat(stockId).concat(".cache");
		return name;
	}
	
	public static String getFilePath(String stockId){ 
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
			return s;
	}
	 
	public static void initStock(Stock stock) { 
		if(stock!=null && stock.getDayStockList()!=null) {  
			StockMaService.initStockMa(stock) ;
			//初始化昨天和前天的，每天Stock情况
			for(DayStock dayStock :stock.getDayStockList()) {
				String dayDateString = dayStock.getDate();  
				Integer indexInList = stock.getDayStockIndexMap().get(dayStock.toString());
				int maxSize =  stock.getDayStockIndexMap().keySet().size();
				if(indexInList>1){
					DayStock yesterdayStock = stock.getDayStockList().get(indexInList-(1));
					if(yesterdayStock!=null){ 
						dayStock.setYesterdayStock(stock.getDayStockMap().get(yesterdayStock.toString()));
					} 
				} 
				if(indexInList<maxSize){
					if(indexInList+1<maxSize){ 
						DayStock nextOneStock = stock.getDayStockList().get(indexInList+1);
						dayStock.setNextOneStock(stock.getDayStockMap().get(nextOneStock.toString()));
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
		} 
		RsiService.countStockRsi(stock);
		MacdService.countStockMacd(stock);
	}
	 
}
