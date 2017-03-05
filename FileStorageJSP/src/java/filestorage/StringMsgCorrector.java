// StringMsgCorrector.java
package filestorage;
/*
	Данный класс представляет собой набор статических методов, корректирующих
сообщение об исключении в зависимости от типа.
	Данный класс представляет собой набор методов-валидаторов для очистки
введенных строк в текстовых полях.
*/

public class StringMsgCorrector
{
	//-- Методы работы со строками --//////////////////////////////////////////
	// Проверка целого числа и очистка его от лишнего
	public static String ProcessIntegerNumberInput(String numberInput)
	{
		String result = "";
		try
		{
			int number = Integer.parseInt(numberInput);
			result = String.valueOf(number);
		}
		catch (Exception ex)
		{
			result = "Exception raised";
		}
		return result;
	}
	
	// Проверка введенной строки на пробелы и "лишние" спецсимволы - остаются только буквы и цифры в первом слове
	public static String ProcessWordStringInput(String input)
	{
		String result = "";
		byte[] inputBytes = input.getBytes();
		for (int i = 0; i < inputBytes.length; i++)
		{
			if ((inputBytes[i] >= 48 && inputBytes[i] <= 57) ||
				(inputBytes[i] >= 65 && inputBytes[i] <= 90) ||
				(inputBytes[i] >= 97 && inputBytes[i] <= 122)) // Символ - английская буква или цифра
			{
				char currentChar = (char)inputBytes[i];
				result = result.concat(String.valueOf(currentChar));
			}
			else // Запрещенный символ
				break;
		}
		return result;
	}
	
	// То же самое, но еще допускаются русские буквы
	public static String ProcessWordStringInput_Rus(String input)
	{
		String result = "";
		byte[] inputBytes = input.getBytes();
		int outputBytesLen = 0, i;
		for (i = 0; i < inputBytes.length; i++)
		{
			if ((inputBytes[i] >= 48 && inputBytes[i] <= 57) ||
				(inputBytes[i] >= 65 && inputBytes[i] <= 90) ||
				(inputBytes[i] >= 97 && inputBytes[i] <= 122) ||
				inputBytes[i] == -88 || inputBytes[i] == -72 ||
				(inputBytes[i] >= -64 && inputBytes[i] <= -33) ||
				(inputBytes[i] >= -32 && inputBytes[i] <= -1)) // Символ - английская/русская буква или цифра
			{
				outputBytesLen++;
			}
			else // Запрещенный символ
				break;
		}
		byte[] outputBytes = new byte[outputBytesLen];
		for (i = 0; i < inputBytes.length; i++)
		{
			if ((inputBytes[i] >= 48 && inputBytes[i] <= 57) ||
				(inputBytes[i] >= 65 && inputBytes[i] <= 90) ||
				(inputBytes[i] >= 97 && inputBytes[i] <= 122) ||
				inputBytes[i] == -88 || inputBytes[i] == -72 ||
				(inputBytes[i] >= -64 && inputBytes[i] <= -33) ||
				(inputBytes[i] >= -32 && inputBytes[i] <= -1)) // Символ - английская/русская буква или цифра
			{
				outputBytes[i] = inputBytes[i];
			}
			else // Запрещенный символ
				break;
		}
		try
		{
			result = new String(outputBytes, "Cp1251");
		}
		catch (Exception ex)
		{
			return null;
		}
		return result;
	}
	
	// Усечение пробелов в начале и конце строки
	public static String CutSpaces(String strInput)
	{
		byte[] inputBytes = strInput.getBytes();
		int startIndex = 0;
		for (; startIndex < inputBytes.length; startIndex++)
		{
			if (inputBytes[startIndex] != 32) // Если это не пробел
				break;
		}
		int endIndex = inputBytes.length - 1;
		for (; endIndex >= 0; endIndex--)
		{
			if (inputBytes[endIndex] != 32) // Если это не пробел
				break;
		}
		if (startIndex > endIndex)
			return "";
		// Генерация выходного массива байтов
		int outputBytesLength = endIndex - startIndex + 1;
		byte[] outputBytes = new byte[outputBytesLength];
		int i = 0;
		for (; i < outputBytesLength; i++)
			outputBytes[i] = inputBytes[startIndex + i];
		String strOutput = "";
		try
		{
			strOutput = new String(outputBytes, "Cp1251");
		}
		catch (Exception ex)
		{
			return null;
		}
		return strOutput;
	}
	
	// Сменить кодировку строки на Cp1251
	public static String ChangeTo_Cp1251(String str)
	{
		String result = "";
		int str_length = str.length();
		char[] c_str = new char[str_length];
		c_str = str.toCharArray();
		byte[] b_str = new byte[str_length];
		for (int i = 0; i < str_length; i++)
		b_str[i] = (byte)c_str[i];
		try
		{
			result = new String(b_str, "Cp1251");
		}
		catch (Exception ex)
		{
			return null;
		}
		return result;
	}
	
	// Найти кавычку " и вставить &quot;
	public static String ProcessDoubleQuotes(String str)
	{
		try
		{
			return str.replace("\"", "&quot;");
		}
		catch (Exception ex)
		{
			return null;
		}
	}
	
	//-- Методы работы с сообщениями --////////////////////////////////////////
	// Если NullPointerException, то опустить отображение
	private static String NullPointerExCorrect(String message)
	{
		try
		{
			if (message != null && message.equals("java.lang.NullPointerException"))
				return "";
			return message;
		}
		catch (Exception ex)
		{
			return message;
		}
	}
	
	// Выбираем подстроку, заключенную в кавычки
	private static String NumberFormatExGetInput(String message)
	{
		String result = "";
		try
		{
			int firstBracketIndex = message.indexOf(34); // 34 - ascii-код кавычки (")
			int lastBracketIndex = message.lastIndexOf(34);
			result = message.substring(firstBracketIndex + 1, lastBracketIndex);
			return result;
		}
		catch (Exception ex)
		{
			return result;
		}
	}
	
	// Если NumberFormatException, то отображаем сообщение "по-русски"
	private static String NumberFormatExCorrect(String message)
	{
		try
		{
			if (message != null && message.contains("java.lang.NumberFormatException") && message.contains("For input string:"))
			{
				String input = NumberFormatExGetInput(message);
				if (input.length() == 0)
					return "Заполните пустые поля.";
				else
					return "Не удается преобразовать введенный текст \"" + input + "\" в соответствующем поле в целочисленное положительное значение.";
			}
			return message;
		}
		catch (Exception ex)
		{
			return message;
		}
	}
	
	// Если просто Exception, то убираем java.lang.Exception
	public static String RemoveJavaLangExceptionPrefix(String message)
	{
		try
		{
			if (message != null && message.contains("java.lang.Exception:"))
			{
				byte[] messageBytes = message.getBytes();
				int beginIndex = "java.lang.Exception:".length(); // 20
				while (messageBytes[beginIndex] == 32) // 32 - пробел
					beginIndex++;
				message = message.substring(beginIndex);
			}
			return message;
		}
		catch (ArrayIndexOutOfBoundsException ex)
		{
			return "";
		}
		catch (Exception ex)
		{
			return message;
		}
	}
	
	// Общая обработка вышеуказанными методами
	public static String ExMsgAnalyzeAndCorrect(String message)
	{
		if (message != null)
		{
			message = NullPointerExCorrect(message);
			if (!message.equals(""))
				message = NumberFormatExCorrect(message);
			if (message.contains("java.lang.Exception:"))
				message = RemoveJavaLangExceptionPrefix(message);
		}
		else // null
			message = "";
		return message;
	}
}
