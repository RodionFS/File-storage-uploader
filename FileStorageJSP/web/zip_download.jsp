<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html; charset=windows-1251"
import="filestorage.filehandle.ZipDownloader"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1251"/>
		<title>Создание и загрузка ZIP-архива</title>
		<link type="text/css" rel="stylesheet" href="css/jdeveloper.css"/>
		<style type="text/css">
		</style>
    </head>
    <body>
		<h3>Создание и загрузка ZIP-архива</h3>
		<hr>
		<jsp:useBean id="zipDownloader"
			class="filestorage.filehandle.ZipDownloader"
			scope="page"/>
		<%
			String zipName = "download.zip";
			String msg = zipDownloader.createZipArchive(zipName);
		%>
		<%=	msg %>
		<br><br>
		<%
			out.println("<a href=\"files/zip/" + zipName + "\">Скачать</a>");
		%>
    </body>
</html>
