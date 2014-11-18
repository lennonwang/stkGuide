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
    
    
    /**
     * 
     * @param d1
     * @param d2
     * @param minRise
     * @param maxRise
     * @return
     */
    public static boolean checkRiseRange(double d1,double d2,int minRise,int maxRise){
    	double rise = (100*(d1-d2)/d2);
    	if(rise>minRise && rise <maxRise){
    		return true;
    	}
    	return false;
    }
 
}
