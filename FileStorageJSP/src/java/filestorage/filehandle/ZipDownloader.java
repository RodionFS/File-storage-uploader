// ZipDownloader.java
package filestorage.filehandle;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import javax.ejb.Stateful;
import javax.ejb.LocalBean;
import org.apache.tools.zip.*;

@Stateful
@LocalBean
public class ZipDownloader
{
	private static boolean isBusy = false;
	public static final String zipFolder = FileUploadServlet.filesStorageDirPath + 
			FileUploadServlet.slash + "zip";
	
	// Создать zip-архив из файлов в хранилище
	// Возвращает информационное сообщение
	public String createZipArchive(String archiveName)
	{
		String errorMessage = "ZIP-архив создан!";
		StoredFile[] filesNames = FileUploadServlet.createStoredFilesList();
		if (isBusy)
			errorMessage = "Другой пользователь пожелал тот же архив.\nЗайдите попозже!";
		else if (archiveName == null || archiveName.length() == 0)
			errorMessage = "Ошибка: Не указано имя zip-архива.";
		else if (filesNames == null || filesNames.length == 0)
			errorMessage = "Ошибка: Не указаны файлы для архивации.";
		else
		{
			FileOutputStream fout = null;
			ZipOutputStream zout = null;
			isBusy = true;
			try // Здесь создаем zip-архив
			{
				fout = new FileOutputStream(zipFolder + FileUploadServlet.slash + archiveName);
				zout = new ZipOutputStream(fout);
				zout.setLevel(9);
				zout.setEncoding("CP866");
				for (int i = 0; i < filesNames.length; i++)
				{
					String currentFile =
							FileUploadServlet.filesStorageDirPath +
							FileUploadServlet.slash +
							filesNames[i].getName();
					ZipEntry ze = new ZipEntry(filesNames[i].getName());
					zout.putNextEntry(ze);
					// Добавляем файл
					InputStream in = new FileInputStream(currentFile);
					try
					{
						byte[] buffer = new byte[10485760]; // 10 Мегабайт
						while (true)
						{
							int readCount = in.read(buffer);
							if (readCount < 0)
								break;
							else if (readCount >= 0 && readCount < 10485760)
							{
								byte[] buffer2 = new byte[readCount];
								for (int j = 0; j < readCount; j++)
									buffer2[j] = buffer[j];
								zout.write(buffer2); // Собственно добавление
							}
							else
								zout.write(buffer); // Собственно добавление
						}
					}
					finally
					{
						in.close();
					}
					//
					zout.closeEntry();
				}
			}
			catch (Exception ex)
			{
				errorMessage = ex.getMessage();
			}
			// Close
			try
			{
				if (zout != null)
					zout.close();
			}
			catch (Exception ex)
			{
			}
			try
			{
				if (fout != null)
					fout.close();
			}
			catch (Exception ex)
			{
			}
			//
			isBusy = false;
		}
		System.out.println("filestorage.filehandle.ZipDownloader.createZipArchive(" + archiveName + "): " + errorMessage);
		return errorMessage;
	}
}
