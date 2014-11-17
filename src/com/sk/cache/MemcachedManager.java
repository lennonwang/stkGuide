/**
 * $Id$
 * All Rights Reserved.
 */
package com.sk.cache;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;
 

 

/**
 * memcache缓存实例管理类
 
 */

public class MemcachedManager{  
	
    private static MemcachedClient c;  
    public static synchronized MemcachedClient getInstance(){  
        if(c==null){  
            try {  
                c=new MemcachedClient(  
                        AddrUtil.getAddresses("127.0.0.1:11211"));  
            } catch (IOException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            }  
        }  
        return c;  
    }  
}  
