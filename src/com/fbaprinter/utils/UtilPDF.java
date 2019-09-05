package com.fbaprinter.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fbaprinter.pojo.PdfFile;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

public class UtilPDF {
	
	@SuppressWarnings("unused")
	public static String createMark(PdfFile pdfFile, String count, String box) throws DocumentException, IOException{
		Date dateCurrent = new Date();
		String dateStr = new SimpleDateFormat("yyyyMMdd").format(dateCurrent);
		//创建文件夹
		String folderPath = UtilConfig.getString("tabUrl")+"\\"+dateStr;
		File folder = new File(folderPath);
		if(!folder.exists()){
			folder.mkdirs();
		}
		//生成文件名
		String fileName = folderPath + "\\" + pdfFile.getCountry() + "_" + pdfFile.getName() + "_" + pdfFile.getCode() + "_" + dateCurrent.getTime() + ".pdf";
		int countInt = Double.valueOf((count==null?"0":count).trim()).intValue();
		// 1.新建document对象
		Rectangle rectangle = new Rectangle((float)141.7234, (float)85.0662);//设置页面长宽
		Document document = new Document(rectangle, 3, 0, -5, 0);//左右上下边距
		// 2.建立一个书写器（Writer）与
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(fileName));
		// 3.打开文档
		document.open();
		// 4.添加一个内容段落
		BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);
		//BaseFont bfChinese = BaseFont.createFont("c://windows//fonts//SIMHEI.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED); //黑体
		//BaseFont bfChinese = BaseFont.createFont("c://windows//fonts//simkai.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);//楷体字

		String name = pdfFile.getName();
		if(name.length()<=7){
			Font font0 = new Font(bfChinese, 23, Font.NORMAL);
			document.add(new Paragraph(name, font0));
			Font font2 = new Font(bfChinese, 16, Font.NORMAL);
			document.add(new Paragraph("[ "+box+" ] - "+pdfFile.getCountry()+"  "+countInt+"个", font2));
		}else{
			Font font0 = new Font(bfChinese, 20, Font.NORMAL);
			document.add(new Paragraph(name.substring(0,8), font0));

			Font font1 = new Font(bfChinese, 20, Font.NORMAL);
			document.add(new Paragraph(name.substring(8,name.length()), font1));

			Font font2 = new Font(bfChinese, 10, Font.NORMAL);
			document.add(new Paragraph("[ "+box+" ] - "+pdfFile.getCountry()+"  "+countInt+"个", font2));
		}
		// 5.关闭文档
		document.close();	
		
		return fileName;
	}
	
	
	
	public static void main(String[] args) throws DocumentException, IOException {
		createMark(new PdfFile("US", "ABCDEFGHIJK", "4杆2钩方形", "ddddddd"), "25", "1");
	}
	
}
