package clientUtils;

import java.io.File;

public class FileUtils {
	public FileUtils() {
	}
	
	public boolean checkFile(String fileNameReceived,String path) {
		File file = new File(path);
		String[] files = file.list();
		for (String file1 : files)
			if (file1.equals(fileNameReceived))
				return false;
		return true;
	}
	
}
