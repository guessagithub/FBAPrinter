package com.fbaprinter.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanMap;


public class UtilConvert {
	
    public static Map<?, ?> objectToMap(Object obj) {  
        if(obj == null)  
            return null;
        return new BeanMap(obj);  
    }
    
    public static List<Map<?, ?>> listToMapList(List<?> list){
    	List<Map<?, ?>> mapList = new ArrayList<Map<?,?>>();
    	if(list!=null && !list.isEmpty() ){
        	for(Object obj : list){
        		mapList.add(new BeanMap(obj));
        	}
    	}
    	return mapList;
    }
    
}
