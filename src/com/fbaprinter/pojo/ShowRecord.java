package com.fbaprinter.pojo;

public class ShowRecord {
	
	private String box = "";
	private String prodName = "";
	private String country = "";
	private String count = "";
	private String code = "";
	private String tabFile = "";
	private String pdfFile = "";
	
	public ShowRecord(String box, String prodName, String country, String count, String code, String tabFile, String pdfFile) {
		this.box = box;
		this.prodName = prodName;
		this.country = country;
		this.count = count;
		this.code = code;
		this.tabFile = tabFile;
		this.pdfFile = pdfFile;
	}
	
	public String getBox() {
		return box;
	}
	public void setBox(String box) {
		this.box = box;
	}
	public String getProdName() {
		return prodName;
	}
	public void setProdName(String prodName) {
		this.prodName = prodName;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getTabFile() {
		return tabFile;
	}
	public void setTabFile(String tabFile) {
		this.tabFile = tabFile;
	}
	public String getPdfFile() {
		return pdfFile;
	}
	public void setPdfFile(String pdfFile) {
		this.pdfFile = pdfFile;
	}
	
	
	

}
