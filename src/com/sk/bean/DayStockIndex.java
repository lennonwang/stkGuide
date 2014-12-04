package com.sk.bean;

import java.io.Serializable;

public class DayStockIndex implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7210201390455888411L;
	private String date;
	private String id;
	private String type ="day"; 
	
	private double rsi6; 
	
	private double exp7; 

	private double kdjRsv;
	private double kdjK;
	private double kdjD;
	private double kdjJ;
	private int kdjPointNumber;
	
	
	private double bollMid;
    private double bollVart1;
    private double bollVart2;
    private double bollVart3;


    private double boll;
    private double bollUpper;
    private double bollLower;
	
	/*
	 * RSV:=(CLOSE-LLV(LOW,N))/(HHV(HIGH,N)-LLV(LOW,N))*100;
K:SMA(RSV,M1,1);
D:SMA(K,M2,1);
J:3*K-2*D;
	 */
	
	public double getExp7() {
		return exp7;
	}



	public void setExp7(double exp7) {
		this.exp7 = exp7;
	}


	public String getDate() {
		return date;
	}



	public void setDate(String date) {
		this.date = date;
	}



	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
	}



	public double getRsi6() {
		return rsi6;
	}



	public void setRsi6(double rsi6) {
		this.rsi6 = rsi6;
	}

    public double getBollVart3() {
        return bollVart3;
    }

    public void setBollVart3(double bollVart3) {
        this.bollVart3 = bollVart3;
    }

    public double getBollMid() {
        return bollMid;
    }

    public void setBollMid(double bollMid) {
        this.bollMid = bollMid;
    }

    public double getBollVart1() {
        return bollVart1;
    }

    public void setBollVart1(double bollVart1) {
        this.bollVart1 = bollVart1;
    }

    public double getBollVart2() {
        return bollVart2;
    }

    public void setBollVart2(double bollVart2) {
        this.bollVart2 = bollVart2;
    }

    public double getBollUpper() {
        return bollUpper;
    }

    public void setBollUpper(double bollUpper) {
        this.bollUpper = bollUpper;
    }

    public double getBoll() {
		return boll;
	}



	public void setBoll(double boll) {
		this.boll = boll;
	}



	public double getBollLower() {
        return bollLower;
    }

    public void setBollLower(double bollLower) {
        this.bollLower = bollLower;
    }

    
    public double getKdjRsv() {
		return kdjRsv;
	}



	public void setKdjRsv(double kdjRsv) {
		this.kdjRsv = kdjRsv;
	}



	public double getKdjK() {
		return kdjK;
	}



	public void setKdjK(double kdjK) {
		this.kdjK = kdjK;
	}



	public double getKdjD() {
		return kdjD;
	}



	public void setKdjD(double kdjD) {
		this.kdjD = kdjD;
	}



	public int getKdjPointNumber() {
		return kdjPointNumber;
	}



	public void setKdjPointNumber(int kdjPointNumber) {
		this.kdjPointNumber = kdjPointNumber;
	}



	public double getKdjJ() {
		return kdjJ;
	}



	public void setKdjJ(double kdjJ) {
		this.kdjJ = kdjJ;
	}



	@Override
	public String toString() {		
		return "index_"+id+"_"+date;
	}
}

