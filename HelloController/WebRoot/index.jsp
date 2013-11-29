<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>Login</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" type="text/css" href="style/style.css">
	<script src="http://code.jquery.com/jquery-1.9.0.js"></script>
  </head>
  
  <body>
  <div>
  	    <form action="Login" method="post">
  	    <div class="login">
	  	    <table>
	  	    	<tr>
	  	    		<td>
	  	    			<span>User:</span>
	  	    		</td>
	  	    		<td>
	  	    			<input name="user" type="text"/>
	  	    		</td>
	  	    	</tr>
	  	    	<tr>
	  	    		<td>
	  	    			<span>PassWord:</span>
	  	    		</td>
	  	    		<td>
	  	    			<input name="pwd" type="password"/>
	  	    		</td>
	  	    	</tr>
	  	    	<tr>
	  	    		<td colspan='2'>
	  	    			<input name="login" type="submit" value='Login' style='float:right'/>
	  	    		</td>
	  	    	</tr>
	  	    </table>
  	    </div>	
    </form>
  </div>
  </body>
</html>
