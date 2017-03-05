// StoredFileHandler.java
package filestorage.filehandle;

public class StoredFileHandler
{
	private String sortField; // Строка сортировки
	private boolean invertedSort; // Логический индикатор инверсии при сортировке
	
	// Конструктор
	public StoredFileHandler()
	{
		sortField = "FileName_Sort";
		invertedSort = false;
	}
	
	//-- Методы для записи/получения данных в поля/из полей этого класса --//
	// Строка сортировки
	public String getSortField()
	{
		return sortField;
	}
	
	// Логический индикатор инверсии при сортировке
	public boolean getInvertedSort()
	{
		return invertedSort;
	}
	
	// Запись полей класса соответствующими параметрами сортировки
	public void setSortFields(String str_sortField, String s_invertedSort)
	{
		// Параметры сортировки
		invertedSort = false;
		if (s_invertedSort.equals("1"))
			invertedSort = true;
		if (str_sortField != null && str_sortField.length() != 0)
			sortField = str_sortField;
		else
			sortField = "FileName_Sort";
	}
	
	//-- Метод получения массива записей файлов с учетом полей сортировки --//
	public StoredFile[] getSortedStoredFileList()
	{
		// Получаем неотсортированный список файлов с помощью сканирования папки хранилища
		StoredFile[] fileArray = FileUploadServlet.createStoredFilesList();
		// Сортируем методом пузырька
		boolean swapped = true;
		while (swapped)
		{
			swapped = false;
			for (int i = 0; i < fileArray.length - 1; i++)
			{
				int compareValue = compare(fileArray[i], fileArray[i+1], sortField, invertedSort);
				if (compareValue > 0) // Первый элемент больше второго
				{
					// Меняем
					StoredFile tmp = fileArray[i];
					fileArray[i] = fileArray[i+1];
					fileArray[i+1] = tmp;
					swapped = true; // Остаемся в цикле
				}
			}
		}
		// Возвращаем отсортированный массив
		return fileArray;
	}
	
	/* Сравнение (А, Б, sortFieldParam, invertedParam):
	 - Возвращает 1, если А > Б.
	 - Возвращает -1, если А < Б.
	 - Возвращает 0, если А == Б.
	*/
	private static int compare(StoredFile file1, StoredFile file2, String sortFieldParam, boolean invertedParam)
	{
		int result = 0;
		if (sortFieldParam.equals("FileExt_Sort"))
		{
			String ext1 = file1.getExtension();
			String ext2 = file2.getExtension();
			if (ext1.compareTo(ext2) > 0) // А > Б
			{
				if (invertedParam) // Надо наоборот
					result = -1; // Типа А < Б
				else
					result = 1;
			}
			else if (ext1.compareTo(ext2) < 0) // А < Б
			{
				if (invertedParam) // Надо наоборот
					result = 1; // Типа А > Б
				else
					result = -1;
			}
			else // if (ext1.compareTo(ext2) == 0) // А == Б
			{
				// Сравниваем по имени файла - оно уникально
				result = compare(file1, file2, "FileName_Sort", invertedParam);
			}
		}
		else if (sortFieldParam.equals("FileSize_Sort"))
		{
			long size1 = file1.length();
			long size2 = file2.length();
			if (size1 > size2) // А > Б
			{
				if (invertedParam) // Надо наоборот
					result = -1; // Типа А < Б
				else
					result = 1;
			}
			else if (size1 < size2) // А < Б
			{
				if (invertedParam) // Надо наоборот
					result = 1; // Типа А > Б
				else
					result = -1;
			}
			else // if (size1 == size2) // А == Б
			{
			    // Сравниваем по имени файла - оно уникально
				result = compare(file1, file2, "FileName_Sort", invertedParam);
			}
		}
		else if (sortFieldParam.equals("FileModified_Sort"))
		{
			long modified1 = file1.lastModified();
			long modified2 = file2.lastModified();
			if (modified1 > modified2) // А > Б
			{
				if (invertedParam) // Надо наоборот
					result = -1; // Типа А < Б
				else
					result = 1;
			}
			else if (modified1 < modified2) // А < Б
			{
				if (invertedParam) // Надо наоборот
					result = 1; // Типа А > Б
				else
					result = -1;
			}
			else // if (modified1 == modified2) // А == Б
			{
				// Сравниваем по имени файла - оно уникально
				result = compare(file1, file2, "FileName_Sort", invertedParam);
			}
		}
		else // if (sortFieldParam.equals("FileName_Sort"))
		{
			String name1 = file1.getName();
			String name2 = file2.getName();
			if (name1.compareTo(name2) > 0) // А > Б
			{
				if (invertedParam) // Надо наоборот
					result = -1; // Типа А < Б
				else
					result = 1;
			}
			else if (name1.compareTo(name2) < 0) // А < Б
			{
				if (invertedParam) // Надо наоборот
					result = 1; // Типа А > Б
				else
					result = -1;
			}
		}
		return result;
	}
}
