package com.fbaprinter.utils;

public class UtilString {

	public static boolean isEmpty(String str) {
		return str==null || "".equals(str.trim());
	}
	
	public static boolean isDigit(String str) {
		if(isEmpty(str)) {
			return false;
		}
		for(int i=0;i<str.length();i++) {
			if(!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	
	
}
