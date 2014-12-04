package com.sk.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.math.RandomUtils;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<Integer> list = new ArrayList<Integer>();
		list.add(333);
		list.add(1304 );
		list.add(4);
		list.add(619 );
		

		list.add(4317);
		list.add(3507 );
		list.add(3182);
		list.add(27 );
		

		list.add(1892);
		list.add(961 );
		list.add(7); 
		   
		Collections.sort(list);
		Set<Integer> iSet = new HashSet<Integer>();
		for(int i=0;i<5;i++){
			Integer nextInt = RandomUtils.nextInt(7);
			while(true){
				if(iSet.contains(nextInt)){
					nextInt = RandomUtils.nextInt(7);
				}
				break;
			}
			list.get(RandomUtils.nextInt(7));
		}
		System.out.println("list=="+list);
	}

}
