<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html; charset=windows-1251" errorPage="/error.jsp"
import="filestorage.filehandle.*, filestorage.*"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=windows-1251"/>
		<title>Erase file</title>
		<link type="text/css" rel="stylesheet" href="css/jdeveloper.css"/>
		<style type="text/css">
			body { background-color: #e1ffe1; }
		</style>
	</head>
	<body>
		<h3>Error</h3>
		<a href="index.jsp">Back</a>
		<hr>
		<%
			String errorMessage = "";
			boolean errorRedirect = false;
			String fileName = null;
			// Read from request
			try
			{
				fileName = request.getParameter("filename");
				if (fileName == null)
				{
					errorRedirect = true;
					response.sendRedirect("index.jsp");
				}
				else
					fileName = StringMsgCorrector.ChangeTo_Cp1251(fileName);
			}
			catch (Exception ex)
			{
				errorRedirect = true;
				response.sendRedirect("index.jsp");
			}
			// Erasing code
			if (!errorRedirect)
			{
				if (FileUploadServlet.eraseFile(fileName)) // File erased
					response.sendRedirect("index.jsp");
				else
					errorMessage = "Error while erasing the file. Check this file on disk manually.";
			}
		%>
		<font color="Red">
			<%= errorMessage %>
		</font>
	</body>
</html>
