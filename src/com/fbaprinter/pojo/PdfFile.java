package com.fbaprinter.pojo;

public class PdfFile {
	
	private String country = "";
	private String code = "";
	private String name = "";
	private String path = "";
	
	public PdfFile(String country, String code, String name, String path){
		this.country = country;
		this.code = code;
		this.name = name;
		this.path = path;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}

	
	
}
