package BT1_String;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Client extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Socket socket;
	private JTextField textField;
	private JTextArea textArea;
    private BufferedReader in;
    private PrintWriter out;

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
		setBounds(100, 100, 474, 336);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textArea = new JTextArea();
		textArea.setBounds(59, 72, 339, 197);
		contentPane.add(textArea);
		
		textField = new JTextField();
		textField.setBounds(57, 41, 243, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton = new JButton("send");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String text=textField.getText();
				if(!text.isEmpty()) {
					out.println(text);
					try {
					    StringBuilder response = new StringBuilder();
					    String line;
					    
					    while ((line = in.readLine()) != null && !line.isEmpty()) {
					        response.append(line).append("\n");
					    }
					    textArea.append(response.toString());
					}catch(IOException ex){
						ex.printStackTrace();
					}
				}
			}
		});
		btnNewButton.setBounds(332, 38, 66, 23);
		contentPane.add(btnNewButton);
		ConnectToServer();
	}
	public void ConnectToServer() {
		try {
			socket=new Socket("localhost",6867);
			in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out=new PrintWriter(socket.getOutputStream(),true);		
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
}
