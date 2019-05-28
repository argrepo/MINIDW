package com.anvizent.minidw.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MemoryTest {

    static Map<Integer, NewObject> map = new HashMap<Integer, NewObject>();

    public static void main(String[] args){
        System.out.println("KB: " + (double) (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024);
        fillMemory(25);
        System.out.println("KB: " + (double) (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024);
    }

    static int j=0;
    public static void fillMemory(int i){

        for(int k=0; k< 3000; k++)
            map.put(j++, new NewObject());

    }


    public static class NewObject{
        long i = 0L;
        long j = 0L;
        long k = 0L;
        public NewObject() {
        	for(int i=0; i<25;i++) {
        		new ArrayList<>();
        	}
		}
    }

}