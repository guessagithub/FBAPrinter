package com.fbaprinter.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetProtection;

public class UtilExcel {
	
	/**
	 * 创建excel文件
	 * 
	 * @param headers		表头名称
	 * @param dataNames		每一列对应的属性名
	 * @param editables		每一列是否可编辑
	 * @param columnWidths	每一列的宽度
	 * @param dataList		数据列表
	 * @return
	 */
	public static XSSFWorkbook createPrintDataExcel(List<String[]> headers, String[] dataNames, boolean[] editables, int[] columnWidths, List<Map<?, ?>> dataList){
		
		//创建一个workBook，并以上述header为表头
		XSSFWorkbook workBook = createWorkBook(headers);
		XSSFSheet sheet = workBook.getSheetAt(0);
		//设置列宽
		for(int i=0;i<columnWidths.length;i++){
			sheet.setColumnWidth(i, columnWidths[i]);
		}
		
		if(dataList!=null && !dataList.isEmpty()){
			//导出报表要用的样式
			XSSFCellStyle unlockStyle = unlockCellStyle(workBook);
			XSSFCellStyle unlockCellStyleGrey = unlockCellStyleGrey(workBook);
			XSSFCellStyle unlockStyleCenter = unlockCellStyleCenter(workBook);
			XSSFCellStyle unlockCellStyleGreyCenter = unlockCellStyleGreyCenter(workBook);
			
			//当前行index
			int curRowIndex = headers.size();
			//将数据装载到workBook
			XSSFRow dataRow;
			//需要居中的字段
			Map<String, String> centerMap = new HashMap<String, String>();
			centerMap.put("box", "");
			centerMap.put("country", "");
			centerMap.put("count", "");
			centerMap.put("tabFile", "");
			centerMap.put("pdfFile", "");
			
			for(Map<?, ?> data : dataList){
				dataRow = sheet.createRow(curRowIndex);
				dataRow.setHeight((short) 500);
				int boxNum = Integer.valueOf((String) data.get("box"));
						
				//导出列
				int colIndex = 0;
				for(int j=0;j<dataNames.length;j++){
					if("tabFile".equals(dataNames[j]) || "pdfFile".equals(dataNames[j])){
						if(boxNum%2==1) {
							createHyperLink(workBook, unlockCellStyleGreyCenter, dataRow, String.valueOf(data.get(dataNames[j])==null?"":data.get(dataNames[j])), colIndex++, dataNames[j], (String) data.get("count"));
						}else {
							createHyperLink(workBook, unlockStyleCenter, dataRow, String.valueOf(data.get(dataNames[j])==null?"":data.get(dataNames[j])), colIndex++, dataNames[j], (String) data.get("count"));
						}
					}else{
						if(boxNum%2==1) {
							if(centerMap.containsKey(dataNames[j])) {
								createCell(unlockCellStyleGreyCenter, dataRow, String.valueOf(data.get(dataNames[j])==null?"":data.get(dataNames[j])), colIndex++);
							}else {
								createCell(unlockCellStyleGrey, dataRow, " "+String.valueOf(data.get(dataNames[j])==null?"":data.get(dataNames[j])), colIndex++);
							}
						}else {
							if(centerMap.containsKey(dataNames[j])) {
								createCell(unlockStyleCenter, dataRow, String.valueOf(data.get(dataNames[j])==null?"":data.get(dataNames[j])), colIndex++);
							}else {
								createCell(unlockStyle, dataRow, " "+String.valueOf(data.get(dataNames[j])==null?"":data.get(dataNames[j])), colIndex++);
							}
						}
					}
				}
				curRowIndex++;
			}
		}
		addLock2Sheet(sheet);
		
		return workBook;		
	}

	/**
	 * 创建指定标题sheet的workBook
	 * 
	 * @param headers
	 * @return
	 */
	private static XSSFWorkbook createWorkBook(List<String[]> headers){
		XSSFWorkbook workBook = new XSSFWorkbook();
		XSSFSheet sheet = workBook.createSheet("sheet1");
		XSSFCellStyle headCellStyle = headCellStyle(workBook);
		//行号
		int rowIndex = 0;
		for(String[] header : headers){
			XSSFRow row = sheet.createRow(rowIndex++);
			row.setHeight((short) 600);
			int colIndex = 0;
			for(String columnName : header){
				XSSFCell cell = row.createCell(colIndex++);
				cell.setCellValue(columnName);
				cell.setCellStyle(headCellStyle);
			}
		}
		
		return workBook;
	}
	
	private static XSSFCellStyle headCellStyle(XSSFWorkbook workBook){
		XSSFCellStyle cellStyle = workBook.createCellStyle();
		//数据居中
		cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		//上下居中
		cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		XSSFFont font = workBook.createFont();
		font.setFontHeightInPoints((short) 13);// 设置字体大小
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		font.setColor(HSSFColor.WHITE.index);
		cellStyle.setFont(font);
		//设置边框
		addBorderLine(cellStyle);
		//设置背景色及填充方式  
		XSSFColor color = new XSSFColor(new java.awt.Color(161, 0, 0));
		cellStyle.setFillForegroundColor(color);
		cellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		
		return cellStyle;
	}
	
	private static void addBorderLine(XSSFCellStyle cellStyle){
		cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
	}
	
	@SuppressWarnings("unused")
	private static XSSFCellStyle lockCellStyle(XSSFWorkbook workBook){
		XSSFCellStyle cellStyle = workBook.createCellStyle();
		//数据靠左
		cellStyle.setAlignment(XSSFCellStyle.ALIGN_LEFT);
		addBorderLine(cellStyle);
		
		return cellStyle;
	}
	

	private static XSSFCellStyle unlockCellStyleCenter(XSSFWorkbook workBook){
		XSSFCellStyle cellStyle = workBook.createCellStyle();
		
		//加底色
		XSSFColor color = new XSSFColor(new java.awt.Color(255, 250, 250));
		cellStyle.setFillForegroundColor(color);
		cellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		
		//垂直居中
		cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		//数据对齐方式
		cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		addBorderLine(cellStyle);
		cellStyle.setLocked(false);
		
		return cellStyle;
	}
	
	private static XSSFCellStyle unlockCellStyle(XSSFWorkbook workBook){
		XSSFCellStyle cellStyle = workBook.createCellStyle();
		
		//加底色
		XSSFColor color = new XSSFColor(new java.awt.Color(255, 250, 250));
		cellStyle.setFillForegroundColor(color);
		cellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		
		//垂直居中
		cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		//数据靠左
		cellStyle.setAlignment(XSSFCellStyle.ALIGN_LEFT);
		addBorderLine(cellStyle);
		cellStyle.setLocked(false);
		
		return cellStyle;
	}
	
	private static XSSFCellStyle unlockCellStyleGreyCenter(XSSFWorkbook workBook){
		XSSFCellStyle cellStyle = workBook.createCellStyle();
		//XSSFColor color = new XSSFColor(new java.awt.Color(235, 235, 235));
		XSSFColor color = new XSSFColor(new java.awt.Color(238, 229, 222));
		cellStyle.setFillForegroundColor(color);
		cellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		//垂直居中
		cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		//数据对齐方式
		cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		addBorderLine(cellStyle);
		cellStyle.setLocked(false);
		
		return cellStyle;
	}
	
	private static XSSFCellStyle unlockCellStyleGrey(XSSFWorkbook workBook){
		XSSFCellStyle cellStyle = workBook.createCellStyle();
		//XSSFColor color = new XSSFColor(new java.awt.Color(235, 235, 235));
		XSSFColor color = new XSSFColor(new java.awt.Color(238, 229, 222));
		cellStyle.setFillForegroundColor(color);
		cellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		//垂直居中
		cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		//数据靠左
		cellStyle.setAlignment(XSSFCellStyle.ALIGN_LEFT);
		addBorderLine(cellStyle);
		cellStyle.setLocked(false);
		
		return cellStyle;
	}
	
	private static void createCell(XSSFCellStyle dataRowStyle, XSSFRow dataRow, String value, int colIndex){
		XSSFCell cell = dataRow.createCell(colIndex);
		cell.setCellValue(value);
		cell.setCellStyle(dataRowStyle);
	}
	
	private static void createHyperLink(XSSFWorkbook workBook, XSSFCellStyle dataRowStyle, XSSFRow dataRow, String value, int colIndex, String fileType, String count) {
		CreationHelper createHelper = workBook.getCreationHelper();
		//CellStyle hlink_style = workBook.createCellStyle();
		//Font hlink_font = workBook.createFont();
		//hlink_font.setUnderline(Font.U_SINGLE);
		//hlink_font.setColor(IndexedColors.BLUE.getIndex());
		//hlink_style.setFont(hlink_font);
		
		Hyperlink link = null;
		XSSFCell cell = dataRow.createCell(colIndex);
		if("tabFile".equals(fileType)){
			cell.setCellValue("打印标签");
		}
		if("pdfFile".equals(fileType)){
			cell.setCellValue("打印条码 " + count + " 个");
		}
		
		link = createHelper.createHyperlink(Hyperlink.LINK_FILE);
		link.setAddress("file:///" + value.replaceAll("\\\\", "/"));
		//link.setAddress("file:///D:/FBA_PRINTER/PDF标签文件/20181231/US_6寸0号(本)_X000Y1VK0P_1546247392020.pdf");
		cell.setHyperlink(link);
		cell.setCellStyle(dataRowStyle);
		//cell.setCellStyle(hlink_style);
	}
	
	private static void addLock2Sheet(XSSFSheet sheet){
		sheet.enableLocking();
		CTSheetProtection sheetProtection = sheet.getCTWorksheet().getSheetProtection();
		sheetProtection.setSelectLockedCells(false);
		sheetProtection.setSelectUnlockedCells(false);
		sheetProtection.setFormatCells(true);
		sheetProtection.setFormatColumns(false);//是否可鼠标拖拽列宽
		sheetProtection.setFormatRows(true);
		sheetProtection.setInsertColumns(true);
		sheetProtection.setInsertRows(true);
		sheetProtection.setInsertHyperlinks(true);
		sheetProtection.setDeleteColumns(true);
		sheetProtection.setDeleteRows(true);
		sheetProtection.setSort(false);
		sheetProtection.setAutoFilter(false);
		sheetProtection.setPivotTables(true);
		sheetProtection.setObjects(true);
		sheetProtection.setScenarios(true);
	}
	
	
	
	
	

	
	public static void main(String[] args) throws IOException {
		
//		String filePath = "D:\\FBA_SYSTEM\\test\\test.xlsx";
//		
//		File file = new File(filePath);
//		if(file.exists()){
//			file.delete();
//		}
//
//		//创建表头
//		String[] head = new String[]{"商品类别二级","商品类别一级","商品类别名称","创建人"};
//		String[] dataNames = new String[]{"code","codeUpper","name","fcu"};//每一列在map中的名称
//		boolean[] editables = new boolean[]{false,true,true,false};//每一列是否可编辑
//		int[] columnWidths = new int[]{8000, 6000, 6000, 6000};//每一列的宽
//
//		List<String[]> headers = new ArrayList<String[]>();
//		headers.add(head);
		
//		//模拟数据
//		List<GoodsSort> orgList = new ArrayList<GoodsSort>();//原始数据
//		GoodsSort goodsSort1 = new GoodsSort("AAAAAAAA", "11111111", "商品分类1");
//		GoodsSort goodsSort2 = new GoodsSort("BBBBBBBB", "22222222", "商品分类2");
//		GoodsSort goodsSort3 = new GoodsSort("CCCCCCCC", "33333333", "商品分类3");
//		orgList.add(goodsSort1);
//		orgList.add(goodsSort2);
//		orgList.add(goodsSort3);
//		
//		//转成需要的数据
//		List<Map<?, ?>> dataList = UtilConvert.listToMapList(orgList);
//		
//		FileOutputStream fos = new FileOutputStream(filePath);
//		createPrintDataExcel(headers, dataNames, editables, columnWidths, dataList).write(fos);
//		fos.close();
	}
	
}
