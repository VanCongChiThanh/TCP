package Vidu_Binary;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.awt.event.ActionEvent;

public class BinaryClient extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txt_input;
	private JTextField txt_result;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BinaryClient frame = new BinaryClient();
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
	public BinaryClient() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 543, 392);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Client");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblNewLabel.setBounds(214, 26, 92, 35);
		contentPane.add(lblNewLabel);
		
		txt_input = new JTextField();
		txt_input.setBounds(230, 112, 159, 35);
		contentPane.add(txt_input);
		txt_input.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Nhập số thập phân");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNewLabel_1.setBounds(38, 122, 129, 25);
		contentPane.add(lblNewLabel_1);
		
		JButton btnNewButton = new JButton("Gửi lên server chuyển sang nhị phân");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					Socket socket=new Socket("localhost",6666);
					InputStream is=socket.getInputStream();
					OutputStream os=socket.getOutputStream();
					
					//input from keyboard
					String inputStr=txt_input.getText();
					
					//push data to server
					byte [] inputByte=inputStr.getBytes();
					os.write(inputByte);
					
					//get result from server
					byte[] resultByte=new byte[500];
					int n=is.read(resultByte);
				
					String resultStr=new String(resultByte,0,n);
					txt_result.setText(resultStr);
					
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnNewButton.setBounds(194, 171, 244, 43);
		contentPane.add(btnNewButton);
		
		JLabel lblNewLabel_1_1 = new JLabel("Kết quả server trả về");
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNewLabel_1_1.setBounds(38, 256, 159, 25);
		contentPane.add(lblNewLabel_1_1);
		
		txt_result = new JTextField();
		txt_result.setColumns(10);
		txt_result.setBounds(230, 246, 159, 35);
		contentPane.add(txt_result);
	}
}
