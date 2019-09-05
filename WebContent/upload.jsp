<%@page import="com.fbaprinter.pojo.ShowRecord"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'index.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	
	<style type="text/css">
		
		body{
			background-color: #FFFAFA;
			margin: 15px;
		}
		
		a:link{
			color: #ffffff;
			text-decoration:none;
			font-size: 22px;
			font-weight: bold;
		}
		a:visited{
			color: #ffffff;
			text-decoration:none;
			font-size: 22px;
			font-weight: bold;
		}
		a:hover{
			color: #ffff00;
			text-decoration: underline;
		}
		
		.table_data{
			width: 100%;
			border: 1px solid #510000;
		}
		
		.tr_title{
    	}
		.tr_title td{
			background-color: #a10000;
			text-align: center;
			line-height: 35px;
			color: #ffffff;
			font-size: 16;
			font-weight: bold;
			border-bottom: 1px solid #000000;
			border-right: 1px solid #000000;
		}
		.tr_data{}
		.tr_data td{
			line-height: 35px;
			background-color: #EEE5DE;
			border-bottom: 1px solid #510000;
			/*
			border-right: 1px solid #510000;
			*/
		}
		.tr_data2{}
		.tr_data2 td{
			line-height: 35px;
			background-color: #FFFAFA;
			border-bottom: 1px solid #510000;
			/*
			border-right: 1px solid #510000;
			*/
		}
		
		.print_over{
			background-color: #ffff00;
			color: #ffffff;
			cursor: pointer;
			font-weight: bold;
		}
		.print_out{
			color: #000000;
		}
		.print_end{
			color: #FF4500;
			cursor: pointer;
		}
		
	</style>
	<script type="text/javascript" src="js/jquery-3.4.1.js"></script>
  </head>
  
  <body>
    
    <%
		List<String> errorList = (List<String>)request.getAttribute("errorList");
		
		if(errorList!=null && errorList.size()>0){
			%>
				<table style="width: 100%;">
					<tr style="background-color: #a10000;color: #ffffff;line-height: 60px;font-size: 22px;font-weight: bold;">
						<td colspan="2">
							&nbsp;&nbsp;&nbsp;上传文件出错：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							[&nbsp;<a href="/FBAPrinter/printer?type=index">首页</a>&nbsp;]
						</td>
					</tr>
			<%
			for(String str : errorList){
				%>
					<tr style="color: #ff0000;line-height: 30px;">
						<td width="30px;"></td>
						<td>◆&nbsp;<%=str%></td>
					</tr>
				<%
			}
			%>
				</table>
			<%
		}else{
			%>
			
			  <!--  
			    <table style="width: 100%;" cellpadding="0" cellspacing="0">
			    	<tr style="background-color: #a10000;color: #ffffff;line-height: 60px;">
						下面那一段
					</tr>
				</table>
				<span style="border: 0px;line-height: 20px;">&nbsp;</span>
			  -->
					<%
						List<ShowRecord> showRecordList = (List<ShowRecord>)request.getAttribute("showRecordList");
						if(showRecordList!=null && showRecordList.size()>0){
							%>
						    <table class="table_data" cellpadding="0" cellspacing="0" id="dataTable">
						    
						    
						    
						    
						    
						    
						    
			    	<tr style="background-color: #a10000;color: #ffffff;line-height: 60px;">
						<td colspan="8" width="180px;" colspan="7" style="font-size: 22px;font-weight: bold;border-bottom: 1px solid #FFD700;" align="center">
							&nbsp;&nbsp;&nbsp;FBA发货明细：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							[&nbsp;<a href="/FBAPrinter/printer?type=index">首页</a>&nbsp;] &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							[&nbsp;<a href="/FBAPrinter/printer?type=download">下载</a>&nbsp;]&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<%
								String autoPrint = (String)request.getAttribute("autoPrint");
								if("Y".equals(autoPrint)){
									%><label id="autoPrintStop" style=""><%
								}else{
									%><label id="autoPrintStop" style="display: none;"><%
								}
							%>
							[&nbsp;<a style="cursor:pointer;" onclick="javascript:autoPrintStop()">停止自动打印</a>&nbsp;] </label> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						</td>
					</tr>
					
					
					
					
					
					
					
								<tr class="tr_title">
									<td>箱号</td>
									<td width="35%">产品名称</td>
									<td>快捷打印</td>
									<td>国家</td>
									<td>数量</td>
									<td>编码</td>
									<td>箱号</td>
									<td>全自动打印</td>
									<!--  
									<td>打印备注</td>
									<td>打印条码</td>
									-->
								</tr>
							<%
							for(ShowRecord record : showRecordList){
								String className = "";
								int boxInt = Integer.valueOf(record.getBox());
								if(boxInt%2==1){
									className = "tr_data";
								}else{
									className = "tr_data2";
								}
								%>
									<tr class="<%=className%>">
										<td align="center"><%= record.getBox() %> 箱</td>
										<td>&nbsp;&nbsp;<%= record.getProdName() %> </td>
										<td align="center"  class="print_out" onmouseover="printSwitch(this)" onmouseout="printSwitch(this)" style="" name="all" onclick="print('all', '<%= record.getCountry() %>_<%= record.getCode() %>_<%= record.getBox() %>', this)">打印<input type="hidden" value="<%=record.getCountry()%>_<%=record.getCode()%>_<%=record.getBox()%>" /></td>				
										<td align="center"><%= record.getCountry() %> </td>
										<td align="center"><%= record.getCount() %> </td>
										<td>&nbsp;&nbsp;<%= record.getCode() %> </td>
										<td align="center"><%= record.getBox() %> 箱</td>
										<td align="center"  class="print_out" onmouseover="printSwitch(this)" onmouseout="printSwitch(this)" style="" value="autoPrintButton" onclick="autoPrint(this)">全自动打印</td>				
										
										<!--
										<td align="center"  class="print_out" onmouseover="printSwitch(this)" onmouseout="printSwitch(this)" style="" onclick="print('tab', '<%= record.getCountry() %>_<%= record.getCode() %>_<%= record.getBox() %>', this)">打印</td>
										<td align="center"  class="print_out" onmouseover="printSwitch(this)" onmouseout="printSwitch(this)" style="" onclick="print('code', '<%= record.getCountry() %>_<%= record.getCode() %>_<%= record.getBox() %>', this)">打印</td>
										-->
									</tr>
								<%
							}
							%>
								</table>
							<%
						}
					%>
			<%
		}
    %>
    
    <script type="text/javascript">

		var returnStrAutoPrint = "success";
		var allTd = null;
		var first = true;
		var timer;
		
		
		document.onkeydown=function(event){
            var e = event || window.event || arguments.callee.caller.arguments[0];
            if(e && e.keyCode==16){
            	var tempAllTd = null;
            	if(allTd==null){
        			// 获取第一个
        			tempAllTd = $("#dataTable tr:eq(2)").children('td').eq(2);
        		}else{
        			// 获取下一个tr
        			tempAllTd = $(allTd).parent("tr").next("tr").children('td').eq(2);
        		}
            	
            	var html = tempAllTd.html();
            	if(html==undefined){
        			alert("打印完了，没有了！！！！！！");
            	}else{
            		var key = tempAllTd.children('input').val();
            		if(key==undefined){
            			alert("出错了！！！");
            		}else{
            			var msg = "开始打印  【    "+tempAllTd.children('input').val().replace(/_/g,"  -  ")+"箱    】？ ";
                    	if(confirm(msg)==true){
                    		allTd = tempAllTd;
                    		print("all", allTd.children('input').val(), allTd[0]);
                    	}
            		}
            	}
            } 
        }; 
		
		
		
		
    	function init(){
        	returnStrAutoPrint = "success";
        	allTd = null;
        	first = true;
        	timer;
    	}
    	// 传入对象为“全自动打印”所在的td
    	function autoPrint(obj){

    		// 先改全自动打印标记
            $.ajax({
                //请求方式
                type : "POST",
                async: false,
                //请求的媒体类型
                contentType: "application/json;charset=UTF-8",
                //请求地址
                url : "/FBAPrinter/printer?type=autoPrint&autoPrint=Y",
                //数据，json字符串
                data : {
            		"type":"print",
            		"autoPrint":"Y"
            	},
                //请求成功
                success : function(result) {
                	if("Y"==result){
                		// 显示“停止自动打印”按钮
                		var autoPrintStop = document.getElementById("autoPrintStop");
                		autoPrintStop.style.display = "";
						
                		init();
                		
                		var trObj = $(obj.parentNode);
                		allTd = trObj.children('td').eq(2);
            			timer = setInterval(autoPrintTimer, 2000);
            			
                	}else{
                		alert("请求失败！");
                	}
                },
                //请求失败，包含具体的错误信息
                error : function(e){
                    console.log(e.status);
                    console.log(e.responseText);
                }
            });
    	}

    	function autoPrintTimer(){
    		if(returnStrAutoPrint == "success"){
    			if(first){
    				first = false;
    			}else{
        			// 获取下一个tr
        			allTd = $(allTd).parent("tr").next("tr").children('td').eq(2);
    			}
        		autoPrintXunhuan();
    		}else{
    			clearInterval(timer);
    		}
    	}
    	
    	// 传入对象为“快捷对象”所在的td
    	function autoPrintXunhuan(){
    		var returnStr = "";
    		var key = allTd.children('input').val();
    		returnStr = print("auto", key, allTd[0]);
    		returnStrAutoPrint = returnStr;
    		return returnStr;
    	}
    	
    	function printSwitch(obj){
    		var className = obj.className;
    		if("print_out"==className){
    			obj.className = "print_over";
    			obj.style.backgroundColor="#a10000";
    		}
    		if("print_over"==className){
    			obj.className = "print_out";
    			obj.style.backgroundColor="";
    		}
    	}
    	
    	function autoPrintStop(){
            $.ajax({
                //请求方式
                type : "POST",
                async: false,
                //请求的媒体类型
                contentType: "application/json;charset=UTF-8",
                //请求地址
                url : "/FBAPrinter/printer?type=autoPrint&autoPrint=N",
                //数据，json字符串
                data : {
            		"type":"print",
            		"autoPrint":"N"
            	},
                //请求成功
                success : function(result) {
                	if("N"==result){
                		// 显示“停止自动打印”按钮
                		var autoPrintStop = document.getElementById("autoPrintStop");
                		autoPrintStop.style.display = "none";
                	}else{
                		alert("请求失败！");
                	}
                },
                //请求失败，包含具体的错误信息
                error : function(e){
                    console.log(e.status);
                    console.log(e.responseText);
                }
            });
    	}
    
    	function print(printType, key, obj){
    		var returnStr = "";
    		//var printForm = document.getElementById("printForm");
    		//printForm.printType.value = printType;
    		//printForm.key.value = key;
    		//printForm.submit();
    		
    		allTd = obj;
    		
            $.ajax({
                //请求方式
                type : "POST",
                async: false,
                //请求的媒体类型
                contentType: "application/json;charset=UTF-8",
                //请求地址
                url : "/FBAPrinter/printer?type=print&printType="+printType+"&key="+key,
                //数据，json字符串
                data : {
            		"type":"print",
            		"printType":printType,
            		"key":key
            	},
                //请求成功
                success : function(result) {
                	if("success"==result){
                		returnStr = result;
                		obj.className = "print_end";
            			obj.style.backgroundColor="";
                		obj.innerHTML = "已打印";
                	}else{
                		alert("打印失败！");
                	}
                },
                //请求失败，包含具体的错误信息
                error : function(e){
                    console.log(e.status);
                    console.log(e.responseText);
                }
            });
            
            return returnStr;
    	}
    	

    </script>
    
    <form action="/FBAPrinter/printer" id="printForm" method="post">
    	<input type="hidden" name="type" value="print" id="hht">
    	<input type="hidden" name="printType" value="">
    	<input type="hidden" name="key" value="">
    </form>
    
    
    
  </body>
</html>
