package client;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import clientUtils.DataFile;

public class ClientMain extends JFrame implements ActionListener, ISocketListener {
	JTextField ipInput, portInput;
	JButton connectButton, closeButton;
	ClientSocketThread clientSocketThread = null;
	boolean isStart = false;
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					ClientMain frame = new ClientMain();

				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ClientMain() {
		// Connect Sever Form
		JLabel ipLabel = new JLabel("IP: ");
		ipInput = new JTextField("127.0.0.1");

		ipLabel.setForeground(parseColor("#6b4f4f"));
		ipLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		ipLabel.setBounds(50, 30, 150, 25);
		ipInput.setBounds(100, 30, 200, 25);

		this.add(ipLabel);
		this.add(ipInput);
		JLabel portLabel = new JLabel("PORT: ");
		portInput = new JTextField("2807");

		portLabel.setForeground(parseColor("#6b4f4f"));
		portLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		portLabel.setBounds(50, 80, 150, 25);
		portInput.setBounds(100, 80, 200, 25);

		this.add(portLabel);
		this.add(portInput);
		
		
		connectButton = new JButton("Connect");
		closeButton = new JButton("Exit");
		connectButton.setBounds(85, 130, 85, 25);
		closeButton.setBounds(200,130, 85, 25);
		
		connectButton.setForeground(parseColor("#6b4f4f"));
		closeButton.setForeground(parseColor("#6b4f4f"));
	
		
		this.add(connectButton);
		this.add(closeButton);

	
		// Add event
		connectButton.addActionListener(this);
		closeButton.addActionListener(this);
		
		
		// setting Frame

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Client Frame");
		this.setLocationRelativeTo(null);
		this.setLayout(null);
		this.setBounds(600, 280, 380, 220);
		this.getContentPane().setBackground(parseColor("#eed6c4"));
		this.setVisible(true);
	}


	public static Color parseColor(String colorStr) {
		return new Color(Integer.valueOf(colorStr.substring(1, 3), 16), Integer.valueOf(colorStr.substring(3, 5), 16),
				Integer.valueOf(colorStr.substring(5, 7), 16));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == connectButton) {
			String ip = ipInput.getText();
			String port = portInput.getText();
			System.out.println(ip + " : " + port);
			
			
			try {
				clientSocketThread = new ClientSocketThread(this);
				clientSocketThread.setSocket(ip, Integer.parseInt(port));
				clientSocketThread.start();
			} catch (NumberFormatException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (Exception e1) {
				
				e1.printStackTrace();
				return;
			}
			
			
			ClientFrame clientFrame = new ClientFrame(ip,port);
			clientFrame.setVisible(true);
			this.hide();
		} else if (e.getSource()==closeButton) {
			this.dispose();
		}
	}


	@Override
	public void showDialog(String str, String type) {
		if (type.equals("ERROR"))
			JOptionPane.showMessageDialog(this, str, type, JOptionPane.ERROR_MESSAGE);
		else if (type.equals("INFOR"))
			JOptionPane.showMessageDialog(this, str, type, JOptionPane.INFORMATION_MESSAGE);
	}

	@Override
	public void updateListFile(String[] listFile) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setProgress(int n) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String chooserFileToSave(DataFile dataFile) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String chooserFolderToSave(DataFile dataFile, String fileName) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
