package com.sk.test;

import net.spy.memcached.MemcachedClient;

import com.sk.cache.MemcachedManager;

public class TestSpymemcached extends Thread {
	 private int count;  
     
	    public static void main(String[] args) throws Exception{  
	        // TODO Auto-generated method stub  
	        System.out.println("begin:"+System.currentTimeMillis());  
	        for(int i=0 ; i<100;i++){  
	        	TestSpymemcached test = new TestSpymemcached(i);  
	            test.start();  
	        }  
	        System.out.println("end:"+System.currentTimeMillis());  
	    }  
	  
	    public  TestSpymemcached(int i){  
	        count = i;  
	    }  
	      
	    public void run(){  
	                System.out.println(count+"start:"+System.currentTimeMillis());  
	                MemcachedClient c= MemcachedManager.getInstance();  
	                for(int i=0 ; i<1000;i++){  
	                    // Store a value (async) for one hour  
	                    c.set(count+"000"+i, 3600, "Hello World "+count+"000"+i+"!");  
	                    // Retrieve a value (synchronously).  
	                    Object myObject=c.get(count+"000"+i);  
	                    System.out.println("myObject=="+myObject);
	                }  
	                System.out.println(count+"end:"+System.currentTimeMillis());  
	                  
	    }  
}
