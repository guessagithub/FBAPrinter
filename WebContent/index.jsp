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
		
		body{background-color: #FFFAFA;}
	</style>
  </head>
  
  <body>
    
    <%
		List<String> errorList = (List<String>)request.getAttribute("errorList");
		
		if(errorList!=null && errorList.size()>0){
			%>
				<table style="width: 100%;">
					<tr style="background-color: #a10000;color: #ffffff;line-height: 60px;">
						<td colspan="2">
							&nbsp;&nbsp;&nbsp;初始化数据失败：
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
			<form action="/FBAPrinter/printer?type=upload" method="post" enctype="multipart/form-data">
			    <table style="width: 100%;font-size: 22px;font-weight: bold;" cellpadding="0" cellspacing="0">
			    	<tr style="background-color: #a10000;color: #ffffff;line-height: 60px;">
						<td width="180px;">&nbsp;&nbsp;&nbsp;FBA发货明细：</td>
						<td width="500px;">
							<input type="file" name="uploadFile" size="60">
						</td>
						<td>
							<input type="submit" value="上传">
						</td>
					</tr>
				</table>
			</form>
			<%
		}
    %>
    
    
    
    
    
    
  </body>
</html>
