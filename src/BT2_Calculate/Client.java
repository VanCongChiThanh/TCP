package BT2_Calculate;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.awt.event.ActionEvent;

public class Client extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtPort;
	private JTextField txt_String;
	private JTextArea textArea;
	private int PORT=1233;
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Client frame = new Client();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Client() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 545, 396);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblPort = new JLabel("Port:");
		lblPort.setBounds(127, 20, 49, 14);
		contentPane.add(lblPort);
		
		txtPort = new JTextField("");
		txtPort.setBounds(186, 17, 96, 20);
		contentPane.add(txtPort);
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
	                PORT = Integer.parseInt(txtPort.getText());
	                connectToServer(PORT);
	            } catch (NumberFormatException ex) {
	                textArea.append("Please enter a valid port number.\n");
	            }
				
			}
		});
		btnConnect.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnConnect.setBounds(292, 11, 100, 28);
		contentPane.add(btnConnect);
		
		JLabel lblQuery = new JLabel("Enter Expression:");
		lblQuery.setBounds(65, 65, 111, 14);
		contentPane.add(lblQuery);
		
		txt_String = new JTextField();
		txt_String.setColumns(10);
		txt_String.setBounds(168, 62, 224, 20);
		contentPane.add(txt_String);
		
		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendRequest(txt_String.getText());
			}
		});
		btnSend.setBounds(411, 61, 70, 23);
		contentPane.add(btnSend);
		
		textArea = new JTextArea();
		textArea.setBounds(57, 109, 424, 226);
		contentPane.add(textArea);
		txtPort.setText(String.valueOf(PORT));
	}
    private void connectToServer(int port) {
         try {
			socket=new Socket("localhost",PORT);
            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            textArea.append("Connected to server on port " + port + ".\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    private void sendRequest(String request) {
    	try {
			writer.println(request);
			String res;
			while((res=reader.readLine())!=null) {
                if (res.isEmpty()) {  
                    break;
                }
                textArea.append("Server:"+request+"="+res + "\n");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
    }
}
