package Vidu_Binary;

import java.awt.EventQueue;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;

public class BinaryServer extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BinaryServer frame = new BinaryServer();
					frame.setVisible(true);
					ServerSocket serverSocket=new ServerSocket(6666);
					System.out.print("Da khoi tao server");
					System.out.print("Dang cho client ket noi");
					while(true) {
						Socket socket=serverSocket.accept();
						System.out.print("welcome socket:"+socket.getPort());
						while(true) {
							
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public BinaryServer() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(43, 10, 349, 229);
		contentPane.add(textArea);
	}

}
