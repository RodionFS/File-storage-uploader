// FileUploadServlet.java
package filestorage.filehandle;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class FileUploadServlet extends HttpServlet implements Serializable
{
	public static final String slash = (String)System.getProperty("file.separator"); // '\\';
	public static final String filesStorageDirPath = getStorageRoot();
			//"D:" + slash + "WebServices" + slash + "FileStorageJSP" + slash + "web" + slash + "files";
	
	private static String getStorageRoot()
	{
		String result = "";
		BufferedReader in = null;
		try
		{
			System.out.println("App path: " + new File(".").getAbsolutePath());
			in = new BufferedReader(new FileReader("." + slash + "storage_root.txt"));
			result = in.readLine();
		}
		catch (Exception ex)
		{
			System.err.println("No config found: " + ex.getMessage());
		}
		finally
		{
			try
			{
				if (in != null)
					in.close();
			}
			catch (Exception ex)
			{
			}
			System.out.println("slash: " + slash);
			System.out.println("filesStorageDirPath: " + result);
		}
		return result;
	}
	
	@Override
	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		//doPost_SingleFile(request, response);
		try
		{
			// Process only if its multipart content
			if (ServletFileUpload.isMultipartContent(request))
			{
				List<FileItem> multiparts = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
				if (multiparts.size() < 1)
					throw new Exception("Вы не выбрали ни одного файла!");
				for (FileItem item : multiparts)
				{
					if (!item.isFormField())
					{
						String name = new File(item.getName()).getName();
						item.write(new File(filesStorageDirPath + slash + name));
					}
				}
			}
			else
				throw new Exception("Ошибка при чтении файлов: Servlet only handles file upload request");
			// Redirect
			jump("/index.jsp", request, response);
		}
		catch (Exception ex)
		{
			// Сообщаем об исключении на странице ошибки
			jumpError("Похоже, вы забыли указать файлы.\n" + ex.toString(), request, response);
		}
	}
	
	public void doPost_SingleFile(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		try
		{
			// Считывание
			String contentType = request.getContentType();
			DataInputStream dis = new DataInputStream(request.getInputStream());
			int length = request.getContentLength();
			byte[] array = new byte[length];
			int dataRead = 0, totalRead = 0, totalRemained = length;
			while (totalRemained > 0)
			{
				dataRead = dis.read(array, totalRead, totalRemained);
				totalRemained -= dataRead;
				totalRead += dataRead;
			}
			dis.close();
			// Обработка имени файла - FileName
			String data = new String(array);
			int lastIndex = contentType.lastIndexOf("=");
			String boundary = contentType.substring (lastIndex + 1, contentType.length());
			int position = data.indexOf("filename=\"");
			int pz_s = position + 10;
			position = data.indexOf("\n", position) + 1;
			int pz_e = position - 4;
			String fileName = "";
			if ((pz_s > 0) && (pz_s < pz_e))
			{
				fileName = "moved";
				char[] c = new char[pz_e - pz_s + 1];
				for (int i = pz_s; i <= pz_e; i++)
					c[i - pz_s] = data.charAt(i);
				fileName = String.valueOf(c, 0, c.length);
				pz_s = fileName.indexOf(slash);
				while (pz_s > 0)
				{
					fileName = fileName.substring (pz_s + 1, fileName.length());
					pz_s = fileName.indexOf(slash);
				}
			}
			// Запись файла
			if (fileName.length() > 0)
			{
				position = data.indexOf("\n", position) + 1;
				String fileData = data.substring(position, data.indexOf(boundary, position) - 4);
				if (fileData.length() > 0)
				{
					// Сперва проверим, существует ли файл с таким именем, чтобы не затереть
					if (fileExists(fileName)) // Файл существует
						throw new Exception("Ошибка при закачке файла: файл с таким именем уже существует в хранилище");
					else // Файл не существует
					{
						FileOutputStream fos = new FileOutputStream(filesStorageDirPath + slash + fileName);
						fos.write(array, position + 2, fileData.length() - 2);
						fos.close();
					}
				}
				else
					throw new Exception("Ошибка при чтении файла: файл неопределен");
			}
			else
				throw new Exception("Ошибка при чтении файла: файл неопределен");
			//
			// Перенаправление
			jump("/index.jsp", request, response);
		}
		catch (Exception ex)
		{
			// Сообщаем об исключении на странице ошибки
			//ex.printStackTrace();
			jumpError(ex.toString(), request, response);
		}
	}
	
	// Определить, существует ли файл
	private static boolean fileExists(String fileName)
	{
		String path = filesStorageDirPath + slash + fileName;
		File file = new File(path);
		return file.exists();
	}
	
	// Удалить файл; вернуть true, если файл успешно удален, в противном случае вернуть false
	public static boolean eraseFile(String fileName)
	{
		String path = filesStorageDirPath + slash + fileName;
		File file = new File(path);
		return file.delete();
	}
	
	// Создать список файлов в хранилище (неотсортированный)
	public static StoredFile[] createStoredFilesList()
	{
		ArrayList<StoredFile> storedFiles = new ArrayList<StoredFile>();
		// Просканировать папку хранилица на предмет файлов
		File filesStorageDir = new File(filesStorageDirPath);
		File[] storedObjects = filesStorageDir.listFiles();
		for (int i = 0; i < storedObjects.length; i++)
		{
			if (storedObjects[i].isFile()) // Данный объект - файл
			{
				// Записываем его в список
				StoredFile fileToAdd = new StoredFile(storedObjects[i], filesStorageDirPath);
				storedFiles.add(fileToAdd);
			}
		}
		// Переводим в обычный массив
		int storedFilesQuantity = storedFiles.size();
		StoredFile[] storedFilesArray = new StoredFile[storedFilesQuantity];
		for (int i = 0; i < storedFilesQuantity; i++)
			storedFilesArray[i] = storedFiles.get(i);
		storedFiles.clear();
		//
		return storedFilesArray;
	}
	
	// Переход на указанную jsp-страницу
	protected void jump(String url, HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		RequestDispatcher rd = getServletContext().getRequestDispatcher(url);
		rd.forward(request, response);
	}
	
	// Переход на страницу ошибки
	protected void jumpError(String errorMessage, HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		request.setAttribute("ErrorMessage", errorMessage);
		jump("/error.jsp", request, response);
	}
	
	// Освобождение ресурсов сервлета
	@Override
	public void destroy()
	{
		super.destroy();
	}
}
