<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<%@ page contentType="text/html; charset=windows-1251" errorPage="/error.jsp"
import="java.util.*, filestorage.filehandle.*"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=windows-1251"/>
		<title>���������� �������� ���������</title>
		<link type="text/css" rel="stylesheet" href="css/jdeveloper.css"/>
		<style type="text/css">
			body { background-color: #e1ffe1; }
		</style>
	</head>
	<body>
		<h2><strong><font color="#008080">
			���������� �������� ���������
		</font></strong></h2>
		<%
			Calendar calendar = Calendar.getInstance();
			int currentDay = calendar.get(Calendar.DATE);
			int currentMonth = calendar.get(Calendar.MONTH) + 1;
			int currentYear = calendar.get(Calendar.YEAR);
			String currentDayStr = String.valueOf(currentDay);
			if (currentDay < 10)
				currentDayStr = "0" + currentDayStr;
			String currentMonthStr = String.valueOf(currentMonth);
			if (currentMonth < 10)
				currentMonthStr = "0" + currentMonthStr;
			String currentServerDate = currentDayStr + " / " + currentMonthStr + " / " + currentYear;
		%>
		<font size="3">
			<b>���� �� �������: </b>
			<%=	currentServerDate %>
		</font>
		<h3><font color="#008080">
			����� ������
		</font></h3>
		<form action="FileUploadServlet" method="POST" enctype="multipart/form-data" style="background-color:rgb(255,255,225);">
			<br>
			<h3>
				&nbsp;&nbsp;
				�� ������ ���������� � ��������� ����� �����.
			</h3>
			&nbsp;&nbsp;
			<input type="file" name="NewFileNameInput" size="180" multiple="multiple"/>
			<br><br>
			&nbsp;&nbsp;
			<input type="submit" value="��������"/>
			<br><br>
		</form>
		<jsp:useBean id="filesHandler"
			class="filestorage.filehandle.StoredFileHandler"
			scope="page"/>
		<%
			String sortField = "";
			String inverted = "";
			StoredFile[] fileArray = null;
			// ��������� ��������� ����������
			try
			{
				sortField = request.getParameter("SortField");
			}
			catch (Exception ex)
			{
				sortField = "";
			}
			if (sortField == null)
				sortField = "";
			try
			{
				inverted = request.getParameter("Inverted");
			}
			catch (Exception ex)
			{
				inverted = "";
			}
			if (inverted == null)
				inverted = "";
			// ��������� ������� ���� ��������
			filesHandler.setSortFields(sortField, inverted);
			// �������� ��������������� ������ ������
			fileArray = filesHandler.getSortedStoredFileList();
		%>
		<br>
		<a href="index.jsp">��������</a>
		<br><br>
		<table cellspacing="2" cellpadding="3" border="1" width="100%">
			<tr valign="top">
				<td width="44%"><h4>��� �����</h4></td>
				<td width="12%"><h4>����������</h4></td>
				<td width="18%"><h4>������ � ������</h4></td>
				<td width="20%"><h4>����� ���������� ��������� / ��������</h4></td>
				<td width="6%">&nbsp;</td>
			</tr>
			<tr>
				<td width="44%">
					<div align="center">
						<a href="index.jsp?SortField=FileName_Sort&Inverted=0">
							<img src="png/Sort_Button.png" alt="���������� �� �����������" height="15" width="15"/>
						</a>
						<a href="index.jsp?SortField=FileName_Sort&Inverted=1">
							<img src="png/SortDesc_Button.png" alt="���������� �� ��������" height="15" width="15"/>
						</a>
					</div>
				</td>
				<td width="12%">
					<div align="center">
						<a href="index.jsp?SortField=FileExt_Sort&Inverted=0">
							<img src="png/Sort_Button.png" alt="���������� �� �����������" height="15" width="15"/>
						</a>
						<a href="index.jsp?SortField=FileExt_Sort&Inverted=1">
							<img src="png/SortDesc_Button.png" alt="���������� �� ��������" height="15" width="15"/>
						</a>
					</div>
				</td>
				<td width="18%">
					<div align="center">
						<a href="index.jsp?SortField=FileSize_Sort&Inverted=0">
							<img src="png/Sort_Button.png" alt="���������� �� �����������" height="15" width="15"/>
						</a>
						<a href="index.jsp?SortField=FileSize_Sort&Inverted=1">
							<img src="png/SortDesc_Button.png" alt="���������� �� ��������" height="15" width="15"/>
						</a>
					</div>
				</td>
				<td width="20%">
					<div align="center">
						<a href="index.jsp?SortField=FileModified_Sort&Inverted=0">
							<img src="png/Sort_Button.png" alt="���������� �� �����������" height="15" width="15"/>
						</a>
						<a href="index.jsp?SortField=FileModified_Sort_Sort&Inverted=1">
							<img src="png/SortDesc_Button.png" alt="���������� �� ��������" height="15" width="15"/>
						</a>
					</div>
				</td>
				<td width="6%">&nbsp;
				</td>
			</tr>
			<%
				if (fileArray != null)
				{
					if (fileArray.length < 1)
					{
						out.println("<tr>");
						out.println("<td width=\"100%\">��� ������ ��� �����������</td>");
						out.println("</tr>");
					}
					else
					{
						for (int i = 0; i < fileArray.length; i++)
						{
							out.println("<tr>");
							out.println("<td width=\"44%\">" + fileArray[i].getName() + "</td>");
							out.println("<td width=\"12%\">" + fileArray[i].getExtension() + "</td>");
							out.println("<td width=\"18%\">" + fileArray[i].getSizeStr() + "</td>");
							out.println("<td width=\"20%\">" + fileArray[i].getLastModifiedStr() + "</td>");
							out.println("<td width=\"6%\">" +
								"<a href=\"files/" + fileArray[i].getName() +
								"\">�������</a>" +
								"<br>" +
								"<a href=\"erase_file.jsp" +
								"?filename=" + fileArray[i].getName() +
								"\" onclick=\"return confirm('���� " + fileArray[i].getName() + " ����� ������������ ������! �� �������?');" +
								"\">�������</a>" +
								"</td>");
							out.println("</tr>");
						}
					}
				}
				else
				{
					out.println("<tr>");
					out.println("<td width=\"100%\">������ ����������� ��� ��� ������ ��� �����������</td>");
					out.println("</tr>");
				}
			%>
		</table>
		<%
			if (fileArray != null && fileArray.length > 0)
				out.println("<b><u>�����:</u> " + fileArray.length + "</b>");
		%>
		&nbsp;&nbsp;&nbsp;&nbsp;
		<%
			if (fileArray != null && fileArray.length > 0)
				out.println("<a href=\"zip_download.jsp\">������� ZIP-�������</a>");
		%>
	</body>
</html>
