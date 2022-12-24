package client;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import clientUtils.DataFile;
import clientUtils.FileUtils;

public class ClientFrame extends JFrame implements ActionListener, ISocketListener {
	JTextField ipInput, portInput, searchInput;
	JButton connectButton, disconnectButton, searchButton, downLoadFile, uploadFileButton;
	JProgressBar jb;
	JList<String> list;
	ClientSocketThread clientSocketThread = null;

	/**
	 * Create the frame.
	 */
	public ClientFrame(String ip, String port) {

		// Connect Sever Form
		try {
			clientSocketThread = new ClientSocketThread(this);
			clientSocketThread.setSocket(ip, Integer.parseInt(port));
			this.showDialog("CONNECTED TO SERVER", "INFOR");
			clientSocketThread.start();
		} catch (Exception e) {

			e.printStackTrace();
		}

		// Search Form
		JLabel searchLabel = new JLabel("Search: ");
		searchInput = new JTextField();
		searchButton = new JButton();

		searchLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		searchLabel.setForeground(parseColor("#6b4f4f"));
		searchLabel.setBounds(50, 30, 150, 25);
		searchInput.setBounds(110, 30, 200, 25);
		searchButton.setBounds(290, 15, 130, 50);

		this.add(searchButton);
		this.add(searchInput);
		this.add(searchLabel);
		
		
		JLabel disconnectLabel = new JLabel("Disconnect:");
		disconnectLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		disconnectLabel.setForeground(parseColor("#6b4f4f"));
		disconnectLabel.setBounds(650, 80, 90, 20);
		disconnectButton = new JButton();
		disconnectButton.setBounds(680, 120, 50, 50);
		this.add(disconnectLabel);
		this.add(disconnectButton);

		// Result List
		list = new JList<>();
		JScrollPane listScrollPane = new JScrollPane(list);
		list.setBackground(parseColor("#fff3e4"));
		list.setForeground(parseColor("#6b4f4f"));
		listScrollPane.setBounds(50, 180, 710, 350);
		this.add(listScrollPane);

		JButton img = new JButton();
		img.setBounds(415, 8, 215, 167);
		this.add(img);
		// JB
		downLoadFile = new JButton();
		downLoadFile.setBounds(130, 60, 50, 50);

		this.add(downLoadFile);

		uploadFileButton = new JButton();
		uploadFileButton.setBounds(235, 60, 50, 50);

		this.add(uploadFileButton);

		jb = new JProgressBar(0, 100);
		jb.setBounds(50, 130, 260, 25);
		jb.setValue(48);
		jb.setStringPainted(true);

		this.add(jb);

		// Add event
		searchButton.addActionListener(this);
		downLoadFile.addActionListener(this);
		uploadFileButton.addActionListener(this);
		disconnectButton.addActionListener(this);

		setupIcon(downLoadFile, "download");
		setupIcon(searchButton, "search");
		setupIcon(uploadFileButton, "up");
		setupIcon(disconnectButton, "disconnect");
		setupIcon(img, "img");
		// setting Frame

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Client Frame");
		this.setLocationRelativeTo(null);
		this.setLayout(null);
		this.setBounds(400, 100, 780, 600);
		this.getContentPane().setBackground(parseColor("#eed6c4"));
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		 if (e.getSource() == disconnectButton) {

			String[] data = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
			list.setListData(data);
			if (clientSocketThread == null) {
				this.showDialog("Chua bat SOCKET", "INFOR");
				return;
			}
			clientSocketThread.closeSocket();
			this.showDialog("Closed socket", "INFOR");
			ClientMain clientFrameStart = new ClientMain();
			clientFrameStart.setVisible(true);
			this.dispose();
			
		} else if (e.getSource() == searchButton) {
			String search = searchInput.getText();

			if (clientSocketThread != null) {
				if (search.isEmpty())
					clientSocketThread.sendString("VIEW_ALL_FILE");
				else
					clientSocketThread.sendString("SEARCH_FILE" + "--" + search);
			}
		} else if (e.getSource() == downLoadFile) {
			if (list.getSelectedIndex() != -1) {
				String str = list.getSelectedValue();
				List<String> lists = list.getSelectedValuesList();
				clientSocketThread.sendString("DOWNLOAD_FILE" + "--" + str);
			}
		} else if (e.getSource() == uploadFileButton) {
			JFileChooser fileChooser = new JFileChooser();
			int returnVal = fileChooser.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File fileToSave = fileChooser.getSelectedFile();
				String filePath = fileToSave.getPath();
				clientSocketThread.sendFile(filePath);
			}
		}
	}

	@Override
	public void updateListFile(String[] listFile) {
		// TODO Auto-generated method stub
		list.setListData(listFile);
	}

	@Override
	public void setProgress(int n) {
		// TODO Auto-generated method stub
		jb.setValue(n);
	}

	@Override
	public void showDialog(String str, String type) {
		if (type.equals("ERROR"))
			JOptionPane.showMessageDialog(this, str, type, JOptionPane.ERROR_MESSAGE);
		else if (type.equals("INFOR"))
			JOptionPane.showMessageDialog(this, str, type, JOptionPane.INFORMATION_MESSAGE);
	}

	private void setupIcon(JButton button, String img) {
		try {
			Image icon = ImageIO.read(getClass().getResource("/resources/" + img + ".png"));
			ImageIcon imageIcon = new ImageIcon(icon);
			button.setIcon(imageIcon);
			button.setBorderPainted(false);
			button.setFocusPainted(false);
			button.setContentAreaFilled(false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Color parseColor(String colorStr) {
		return new Color(Integer.valueOf(colorStr.substring(1, 3), 16), Integer.valueOf(colorStr.substring(3, 5), 16),
				Integer.valueOf(colorStr.substring(5, 7), 16));
	}

	@Override
	public String chooserFileToSave(DataFile dataFile) {
		JFileChooser fileChooser = new JFileChooser();
		int returnVal = fileChooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File fileToSave = fileChooser.getSelectedFile();
			String filePath = fileToSave.getPath();
			try {
				dataFile.saveFile(filePath);
				JOptionPane.showMessageDialog(null, "File Saved");
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, e);
			}
		}
		return null;
	}

	@Override
	public String chooserFolderToSave(DataFile dataFile, String fileName) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Choose folder to save");
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		int returnVal = fileChooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File fileToSave = fileChooser.getSelectedFile();
			String filePath = fileToSave.getPath();

			System.out.println(filePath + "\\" + fileName);

			FileUtils fileWorker = new FileUtils();
			try {
				if (fileWorker.checkFile(fileName, filePath)) {
					System.out.println(filePath + "\\" + fileName);
					dataFile.saveFile(filePath + "\\" + fileName);
					JOptionPane.showMessageDialog(null, "File Saved");
				} else {
					showDialog("File is existed in folder", "ERROR");
				}

			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, e);
			}
		}
		return null;
	}

}
