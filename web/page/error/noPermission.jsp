<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
  <head>
   
    <title>缺少权限!</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<link rel="icon" href="<%=request.getContextPath()%>/image/favicon/favicon32.ico" type="image/x-icon"/> 
	<link rel="shortcut icon" href="<%=request.getContextPath()%>/image/favicon/favicon16.ico" type="image/x-icon"/>
	<style type="text/css">
		#no_permission
		{
			position: relative;
			width: 400px;
			height: 50px;
			top: 0;
			left: 0;
			margin: 150px auto 0;
			color: red;
			font-size: 25px;
			font-family: Microsoft YaHei;
			font-weight: 600;
			line-height: 50px;
			text-align: center;
		}
	</style>

  </head>
  
  <body>
	
		<div id="no_permission">
			你无权访问当前页面!
		</div>

  </body>
</html>
