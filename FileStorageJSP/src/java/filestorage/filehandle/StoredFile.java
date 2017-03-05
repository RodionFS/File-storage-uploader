// StoredFile.java
package filestorage.filehandle;

import java.io.*;
import java.util.*;

public class StoredFile extends File
{
	protected String extension; // Расширение
	
	// Конструктор
	public StoredFile(String fullName)
	{
		super(fullName);
		setExtension();
	}
	
	// Конструктор 2
	public StoredFile(File file, String dirPath)
	{
		super(dirPath + FileUploadServlet.slash + file.getName());
		setExtension();
	}
	
	// Задать расширение
	protected void setExtension()
	{
		String fileName = this.getName();
		char[] fileNameChar = fileName.toCharArray();
		int divPointPos = 0; // Позиция точки-разделителя
		for (int i = fileNameChar.length - 1; i >= 0; i--)
		{
			if (fileNameChar[i] == '.')
			{
				divPointPos = i;
				break;
			}
		}
		if (divPointPos == 0 || divPointPos == fileNameChar.length - 1)
			extension = "";
		else
		{
			String ext = fileName.substring(divPointPos + 1);
		    int str_length = ext.length();
		    char[] c_str = new char[str_length];
		    c_str = ext.toCharArray();
		    byte[] b_str = new byte[str_length];
		    for (int i = 0; i < str_length; i++)
		    b_str[i] = (byte)c_str[i];
		    try
		    {
		        extension = new String(b_str, "Cp1251");
		    }
		    catch (Exception ex)
		    {
		        extension = ext;
		    }
			finally
			{
				if (extension.equals(ext))
					extension = extension.toUpperCase();
				else
					extension = ext;
			}
		}
	}
	
	// Получить расширение
	public String getExtension()
	{
		return extension;
	}
	
	// Получить размер в виде строки
	public String getSizeStr()
	{
		long fileSize = this.length();
		String fileSizeStr = String.valueOf(fileSize);
		char[] fileSizeChar = fileSizeStr.toCharArray();
		String result = "";
		for (int i = fileSizeChar.length - 1; i >= 0;)
		{
			String digitGroup = "";
			for (int j = 1; j <= 3; j++)
			{
				if (i < 0)
					break;
				digitGroup = fileSizeChar[i] + digitGroup;
				i--;
			}
			if (digitGroup.length() > 0)
			{
				if (result.length() == 0)
					result = digitGroup;
				else
					result = digitGroup + " " + result;
			}
			else
				break;
		}
		return result;
	}
	
	// Получить время последнего изменения в удобном виде
	public String getLastModifiedStr()
	{
		// Последнее изменение файла
		long lastModified = this.lastModified(); // В милисекундах
		Date lastModifiedDate = new Date(lastModified);
		Calendar lastModifiedCalendar = Calendar.getInstance();
		lastModifiedCalendar.setTime(lastModifiedDate);
		// День
		int lastModifiedDay = lastModifiedCalendar.get(Calendar.DATE);
		String lastModifiedDayStr = String.valueOf(lastModifiedDay);
		if (lastModifiedDay < 10)
			lastModifiedDayStr = "0" + lastModifiedDayStr;
		// Месяц
		int lastModifiedMonth = lastModifiedCalendar.get(Calendar.MONTH) + 1;
		String lastModifiedMonthStr = String.valueOf(lastModifiedMonth);
		if (lastModifiedMonth < 10)
			lastModifiedMonthStr = "0" + lastModifiedMonthStr;
		// Год
		int lastModifiedYear = lastModifiedCalendar.get(Calendar.YEAR);
		// Час
		int lastModifiedHour = lastModifiedCalendar.get(Calendar.HOUR_OF_DAY);
		String lastModifiedHourStr = String.valueOf(lastModifiedHour);
		if (lastModifiedHour < 10)
			lastModifiedHourStr = "0" + lastModifiedHourStr;
		// Минута
		int lastModifiedMinute = lastModifiedCalendar.get(Calendar.MINUTE);
		String lastModifiedMinuteStr = String.valueOf(lastModifiedMinute);
		if (lastModifiedMinute < 10)
			lastModifiedMinuteStr = "0" + lastModifiedMinuteStr;
		// Секунда
		int lastModifiedSecond = lastModifiedCalendar.get(Calendar.SECOND);
		String lastModifiedSecondStr = String.valueOf(lastModifiedSecond);
		if (lastModifiedSecond < 10)
			lastModifiedSecondStr = "0" + lastModifiedSecondStr;
		// Слить все это воедино
		String result = lastModifiedDayStr + "." + lastModifiedMonthStr + "." + lastModifiedYear + " " + lastModifiedHourStr + ":" + lastModifiedMinuteStr + ":" + lastModifiedSecondStr;
		return result;
	}
}
