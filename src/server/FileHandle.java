package server;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileHandle {

	public static String URL_FOLDER = "D:\\LocalServerFilePBL4";

	public String[] getAllFileName() {
		File file = new File(URL_FOLDER);
		String[] files = file.list();

		return files;
	}

	public String[] searchFile(String keyword) {
		File file = new File(URL_FOLDER);
		String[] files = file.list();
		ArrayList<String> fileSearches = new ArrayList<String>();
		for (String file1 : files)
			if (searchStringInFile(URL_FOLDER + "\\" + file1, keyword) || searchStringinFileName(file1, keyword))
				fileSearches.add(file1);
		for (int i = 0; i < fileSearches.size(); i++)
			System.out.println("File searches : " + fileSearches.get(i));

		String[] result = new String[fileSearches.size()];
		result = fileSearches.toArray(result);
		return result;
	}
	
	boolean searchStringinFileName(String fileName,String keyword) {
		return fileName.contains(keyword);
	}

	boolean searchStringInFile(String fileName, String keyword) {
		File file = new File(fileName);

		try {
			Scanner scanner = new Scanner(file);

			// now read the file line by line...
			int lineNum = 0;
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				lineNum++;
				if (line.contains(keyword)) {
					return true;
				}
			}
		} catch (FileNotFoundException e) {
		}
		return false;
	}

	public boolean checkFile(String fileNameReceived) {
		// TODO Auto-generated method stub
		File file = new File(URL_FOLDER);
		String[] files = file.list();
		for (String file1 : files)
			if (file1.equals(fileNameReceived))
				return false;
		return true;
	}

	public String getFileName(String str) {
		String result = "";
		int len = str.length();
		for (int i = len - 1; i > 0; i--)
			if (str.charAt(i) == '\\')
				return (new StringBuilder(result)).reverse().toString();
			else
				result += str.charAt(i);

		return null;
	}
}
