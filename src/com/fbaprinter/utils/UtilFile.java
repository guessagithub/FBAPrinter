package com.fbaprinter.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.fbaprinter.pojo.PdfFile;
import com.fbaprinter.pojo.ShowRecord;

public class UtilFile {
	
	public static Map<String, PdfFile> codeMap = null;
	public static List<String> errorList = null;
	
	public static List<ShowRecord> showRecordList = null;
	public static String showFileName = "";
	

	public static Map<String, ShowRecord> showRecordMap = null;
	
	public static String autoPrint = "N";	// 自动打印标记
	
	public static void init() throws IOException{
		codeMap = new HashMap<String, PdfFile>();
		errorList = new ArrayList<String>();
		String pdfFilePath = UtilConfig.getString("pdfUrl");
		scanFile(pdfFilePath);
	}

	private static void scanFile(String filePath){
		File folders = new File(filePath);
		if(!folders.exists()){
			errorList.add("目录【"+filePath+"】不存在");
		}else{
			File[] listFile = folders.listFiles();
			for(File file : listFile){
				if(file.isDirectory()){
					scanFile(file.getAbsolutePath());
				}else{
					String fileName = file.getName();
					if(fileName.indexOf(".pdf")<0){
						errorList.add("文件【"+file.getAbsolutePath()+"】命名不规范【不是pdf文件】");
					}else{
						String str[] = fileName.split("_");
						if(str.length!=3){
							errorList.add("文件【"+file.getAbsolutePath()+"】命名不规范【下划线分隔】");
						}else{
							if(codeMap.get(str[0]+"_"+str[2])==null){
								codeMap.put(str[0]+"_"+str[2].replaceAll(".pdf", ""), new PdfFile(str[0], str[2].replaceAll(".pdf", ""), str[1], file.getAbsolutePath()));
							}else{
								errorList.add("文件【"+file.getAbsolutePath()+"】命名重复");
							}
						}
					}
				}
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
		init();
		Set<Entry<String, PdfFile>> entrySet = codeMap.entrySet();
		for(Entry<String, PdfFile> entry : entrySet){
			PdfFile fbaPdfFile = entry.getValue();
			System.out.println(fbaPdfFile.getCountry()+" - "+fbaPdfFile.getCode()+" - "+fbaPdfFile.getName()+" - "+fbaPdfFile.getPath());
		}
	}
}
