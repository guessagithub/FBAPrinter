package com.fbaprinter.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class UtilConfig {
	
	private static Properties config = null;
	
	public static String getString(String key) throws IOException{
		if(config==null){
			init();
		}
		return config.getProperty(key);
	}
	
	private static void init() throws IOException {
		config = new Properties();
		InputStream in = UtilConfig.class.getResourceAsStream("/config.properties");
		config.load(in);
	}

}
