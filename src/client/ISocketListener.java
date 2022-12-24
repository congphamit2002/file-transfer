package client;

import clientUtils.DataFile;

public interface ISocketListener {

	void updateListFile(String[] listFile);

	void setProgress(int n);

	void showDialog(String str, String type);

	String chooserFileToSave(DataFile dataFile);
	
	String chooserFolderToSave(DataFile dataFile,String fileName);

}
