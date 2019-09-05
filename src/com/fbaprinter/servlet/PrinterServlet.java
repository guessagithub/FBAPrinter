package com.fbaprinter.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.fbaprinter.pojo.PdfFile;
import com.fbaprinter.pojo.ShowRecord;
import com.fbaprinter.utils.UtilConfig;
import com.fbaprinter.utils.UtilConvert;
import com.fbaprinter.utils.UtilExcel;
import com.fbaprinter.utils.UtilFile;
import com.fbaprinter.utils.UtilPDF;
import com.fbaprinter.utils.UtilPrinter;
import com.fbaprinter.utils.UtilString;


public class PrinterServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4498800223175460213L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String type = req.getParameter("type");
		List<String> errorList = new ArrayList<String>();
		String forward = "";
		if("index".equals(type)){
			//加载所有的PDF条码
			UtilFile.init();
			errorList = UtilFile.errorList;
			forward = "/index.jsp";
		} else if ("upload".equals(type)) {
			UtilFile.showRecordList = null;
			UtilFile.showRecordMap = null;
			UtilFile.showFileName = "";
			List<ShowRecord> showRecordList = new ArrayList<ShowRecord>();
			Map<String, ShowRecord> showRecordMap = new HashMap<String, ShowRecord>();
			try {
				String filePath = ""; //上传后的文件全称
				// 上传配置
				int MEMORY_THRESHOLD = 1024 * 1024 * 3; // 3MB
				int MAX_FILE_SIZE = 1024 * 1024 * 40; // 40MB
				int MAX_REQUEST_SIZE = 1024 * 1024 * 50; // 50MB
				// 配置上传参数
				DiskFileItemFactory factory = new DiskFileItemFactory();
				// 设置内存临界值 - 超过后将产生临时文件并存储于临时目录中
				factory.setSizeThreshold(MEMORY_THRESHOLD);
				// 设置临时存储目录
				factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
				ServletFileUpload upload = new ServletFileUpload(factory);
				// 设置最大文件上传值
				upload.setFileSizeMax(MAX_FILE_SIZE);
				// 设置最大请求值 (包含文件和表单数据)
				upload.setSizeMax(MAX_REQUEST_SIZE);
				// 中文处理
				upload.setHeaderEncoding("UTF-8");
				// 解析请求的内容提取文件数据
				List<FileItem> formItems = upload.parseRequest(req);
				if (formItems != null && formItems.size() > 0) {
					// 迭代表单数据
					for (FileItem item : formItems) {
						// 处理不在表单中的字段
						if (!item.isFormField()) {
							String fileName = new File(item.getName()).getName();
							UtilFile.showFileName = fileName.replace(".xlsx", "");
							filePath = UtilConfig.getString("excelUrl") + File.separator + fileName.replace(".xlsx", "_"+(new Date().getTime())+".xlsx");
							File storeFile = new File(filePath);
							// 在控制台输出文件的上传路径
							System.out.println(filePath);
							// 保存文件到硬盘
							item.write(storeFile);
						}
					}
				}
				//解析Excel
				InputStream is = new FileInputStream(filePath);
	            // 构造 XSSFWorkbook 对象，strPath 传入文件路径 
	            XSSFWorkbook xwb = new XSSFWorkbook(is); 
	            // 读取第一章表格内容 
	            XSSFSheet sheet = xwb.getSheetAt(0); 
	            // 定义 row、cell 
	            XSSFRow row; 
	            
	            String box = "";
	            String prodName = "";
	            String country = "";
	            String count = "";
	            String code = "";
	            
	            // 循环输出表格中的内容 
	            for (int i = sheet.getFirstRowNum()+1; i < sheet.getPhysicalNumberOfRows(); i++) {
	            	try{
		                row = sheet.getRow(i); 	      
		                if(!UtilString.isEmpty(row.getCell(0).toString())){box=removePoint(row.getCell(0).toString().trim());}
		                if(!UtilString.isEmpty(row.getCell(1).toString())){prodName=row.getCell(1).toString().trim();}
		                if(!UtilString.isEmpty(row.getCell(2).toString())){country=row.getCell(2).toString().trim();}
		                if(!UtilString.isEmpty(row.getCell(3).toString())){count=removePoint(row.getCell(3).toString().trim());}
		                if(!UtilString.isEmpty(row.getCell(10).toString())){code=row.getCell(10).toString().trim();}
		                //row.getPhysicalNumberOfCells()
		                // 验证是否存在条码的PDF文件
		                String tabFile = "错误";
		                String pdfFile = "错误";
		                if(UtilFile.codeMap.containsKey(country+"_"+code)){
		                	PdfFile obj = UtilFile.codeMap.get(country+"_"+code);
		                	pdfFile = obj.getPath();
		                	tabFile = UtilPDF.createMark(obj, count, box);
		                }else{
		                	errorList.add("找不到对应的条码PDF文件 ：    第"+box+"箱“"+prodName+"”,编码为："+code);
		                }
		                ShowRecord sr = new ShowRecord(box, prodName, country, count, code, tabFile, pdfFile);
		                showRecordList.add(sr);
		                showRecordMap.put(country+"_"+code+"_"+box, sr);
	            	}catch(Exception ex){
	    				errorList.add("解析文件失败: 第"+(i+1)+"行！");
	    				ex.printStackTrace();
	            	}
	            }
			} catch (Exception e) {
				errorList.add("上传文件失败: " + e.getMessage());
			}
			UtilFile.showRecordList = showRecordList;
			req.setAttribute("showRecordList", showRecordList);
			req.setAttribute("autoPrint", UtilFile.autoPrint);

			UtilFile.showRecordMap = showRecordMap;
			//req.setAttribute("showRecordMap", showRecordMap);
			
			forward = "/upload.jsp";
		}else if("download".equals(type)){
			try {
				String fileName = UtilFile.showFileName + "_可直接打印";
				resp.setContentType("application/vnd.ms-excel;charset=utf-8");
				
				String userAgent = req.getHeader("user-agent");
				if(userAgent!=null && (userAgent.indexOf("Firefox")>=0 || userAgent.indexOf("Chrome")>=0 || userAgent.indexOf("Safari")>=0)){
					fileName = new String(fileName.getBytes("UTF-8"),"ISO8859-1");
				}else{
					fileName = URLEncoder.encode(fileName, "UTF8");//其他浏览器
				}
				fileName = fileName + ".xlsx";
				resp.setHeader("Content-Disposition", "attachment; filename=" + fileName);
				
				OutputStream out = resp.getOutputStream();
				//生成workBook
				String[] head = new String[]{"箱号","产品名称","国家","数量","编码","标签地址","条码地址","箱号"};
				String[] dataNames = new String[]{"box","prodName","country","count","code","tabFile","pdfFile","box"};//每一列在map中的名称
				boolean[] editables = new boolean[]{true,true,true,true,true,true,true,true};//每一列是否可编辑
				int[] columnWidths = new int[]{3000, 7000, 3000, 3000, 4000, 4000, 4000, 3000};//每一列的宽

				List<String[]> headers = new ArrayList<String[]>();
				headers.add(head);
				//转成需要的数据
				List<Map<?, ?>> dataList = UtilConvert.listToMapList(UtilFile.showRecordList);
				XSSFWorkbook workBook = UtilExcel.createPrintDataExcel(headers, dataNames, editables, columnWidths, dataList);
				workBook.write(out);
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else if("print".equals(type)){
			try {
				String printType = req.getParameter("printType");
				String key = req.getParameter("key");
				ShowRecord showRecord = UtilFile.showRecordMap.get(key);

				// 如果是全自动打印，要判断自动打印标记
				if("auto".equals(printType)) {
					if("Y".equals(UtilFile.autoPrint)) {
						System.out.print(key + "  -  ");
						UtilPrinter.PDFprint(new File(showRecord.getTabFile()), 1);
						System.out.print(key + "  -  ");
						UtilPrinter.PDFprint(new File(showRecord.getPdfFile()), Integer.valueOf(showRecord.getCount()));
						resp.getWriter().write("success");
					}else {
						resp.getWriter().write("fail");
					}
				}else {
					// 打印标签
					if("tab".equals(printType) || "all".equals(printType) ) {
						System.out.print(key + "  -  ");
						UtilPrinter.PDFprint(new File(showRecord.getTabFile()), 1);
					}
					// 打印条码
					if("code".equals(printType) || "all".equals(printType) ) {
						System.out.print(key + "  -  ");
						UtilPrinter.PDFprint(new File(showRecord.getPdfFile()), Integer.valueOf(showRecord.getCount()));
					}
					resp.getWriter().write("success");
				}
			} catch (Exception e) {
				e.printStackTrace();
				UtilFile.autoPrint = "N";
				System.out.println("======== autoPrint ： " + UtilFile.autoPrint);
				resp.getWriter().write("fail");
			}
		}else if("autoPrint".equals(type)){
			try {
				String autoPrint = req.getParameter("autoPrint");
				UtilFile.autoPrint = autoPrint;
				System.out.println("======== autoPrint ： " + UtilFile.autoPrint);
				resp.getWriter().write(UtilFile.autoPrint);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		req.setAttribute("autoPrint", UtilFile.autoPrint);
		if(!"download".equals(type) && !"print".equals(type) && !"autoPrint".equals(type)){
			req.setAttribute("errorList", errorList);
			req.getRequestDispatcher(forward).forward(req, resp);
		}
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	private String removePoint(String value){
		Double d = Double.valueOf(value);
		return String.valueOf(d.intValue());
	}
	
	
}
