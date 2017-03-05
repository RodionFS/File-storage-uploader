<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html; charset=windows-1251" errorPage="/error.jsp"
import="filestorage.filehandle.*, filestorage.*"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=windows-1251"/>
		<title>��������</title>
		<link type="text/css" rel="stylesheet" href="css/jdeveloper.css"/>
		<style type="text/css">
			body { background-color: #e1ffe1; }
		</style>
	</head>
	<body>
		<h3>������</h3>
		<a href="index.jsp">�����</a>
		<hr>
		<%
			String errorMessage = "";
			boolean errorRedirect = false;
			String fileName = null;
			// ��������� �� ������
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
			// �������
			if (!errorRedirect)
			{
				if (FileUploadServlet.eraseFile(fileName)) // ���� ������
					response.sendRedirect("index.jsp");
				else
					errorMessage = "�� ����� �������� ����� ��������� ������. ��������� ������� ����� �� ����� �������.";
			}
		%>
		<font color="Red">
			<%= errorMessage %>
		</font>
	</body>
</html>