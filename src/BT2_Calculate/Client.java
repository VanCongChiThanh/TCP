package BT2_Calculate;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JLabel;
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
	private JTextField txt_Input;
	private JTextField txt_Result;
	private JLabel lblNhap;
	private BufferedReader reader;
	private PrintWriter writer;
	private Socket socket;

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
		setBounds(100, 100, 553, 253);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txt_Input = new JTextField();
		txt_Input.setBounds(127, 41, 259, 39);
		contentPane.add(txt_Input);
		txt_Input.setColumns(10);
		
		JButton btnCal = new JButton("Tinh toan");
		btnCal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String input = txt_Input.getText();
				writer.println(input);
				try {
					txt_Result.setText(reader.readLine());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnCal.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnCal.setBounds(407, 40, 105, 39);
		contentPane.add(btnCal);
		
		txt_Result = new JTextField();
		txt_Result.setColumns(10);
		txt_Result.setBounds(127, 124, 259, 39);
		contentPane.add(txt_Result);
		
		JLabel lblNewLabel = new JLabel("Ket qua");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel.setBounds(39, 129, 65, 27);
		contentPane.add(lblNewLabel);
		
		lblNhap = new JLabel("Nhap");
		lblNhap.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNhap.setBounds(39, 53, 65, 27);
		contentPane.add(lblNhap);
		
		ConnectToServer();
	}

	private void ConnectToServer() {
		try {
			socket = new Socket("localhost", 6666);
			writer = new PrintWriter(socket.getOutputStream(), true);
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void dispose() {
		// Đóng kết nối socket khi cửa sổ bị đóng
		try {
			writer.close();
			reader.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.dispose();
	}
}
