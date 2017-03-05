<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html; charset=windows-1251" isErrorPage="true"
import="filestorage.*"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=windows-1251"/>
		<title>Ошибка</title>
		<link type="text/css" rel="stylesheet" href="css/jdeveloper.css"/>
		<style type="text/css">
		</style>
	</head>
	<body>
		<h3>Ошибка</h3>
		<hr>
		<%
			String errorMesasge = (String)request.getAttribute("ErrorMessage");
		%>
		<%=
			(errorMesasge != null) ?
				StringMsgCorrector.RemoveJavaLangExceptionPrefix(errorMesasge) :
				(
					(exception != null) ?
						StringMsgCorrector.RemoveJavaLangExceptionPrefix(exception.toString()) :
						"Неизвестная ошибка"
				)
		%>
	</body>
</html>