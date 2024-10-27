package BT3_Chat;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;

public class Client extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField;
    private JTextArea textArea;
    private JLabel lblNewLabel_1;
    private JTextField txtPort;
    protected Socket socket;
    protected PrintWriter writer;
    protected BufferedReader reader;
    private static int PORT = 6888;
    private static String ClientName;
    private JLabel lblNewLabel_2;
    private JTextField txtClientName;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Client frame = new Client();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Client() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 537, 399);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setBounds(73, 136, 369, 135);
        contentPane.add(textArea);

        JButton btnSend = new JButton("Send");
        btnSend.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    sendMessage();

            }
        });
        btnSend.setFont(new Font("Tahoma", Font.PLAIN, 14));
        btnSend.setBounds(209, 328, 89, 34);
        contentPane.add(btnSend);

        textField = new JTextField();
        textField.setFont(new Font("Tahoma", Font.PLAIN, 12));
        textField.setBounds(73, 289, 369, 28);
        contentPane.add(textField);
        textField.setColumns(10);

        JLabel lblNewLabel = new JLabel("Client");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblNewLabel.setBounds(235, 11, 63, 14);
        contentPane.add(lblNewLabel);

        lblNewLabel_1 = new JLabel("PORT");
        lblNewLabel_1.setBounds(181, 44, 49, 14);
        contentPane.add(lblNewLabel_1);

        txtPort = new JTextField();
        txtPort.setColumns(10);
        txtPort.setBounds(235, 41, 96, 20);
        contentPane.add(txtPort);

        lblNewLabel_2 = new JLabel("Client Name");
        lblNewLabel_2.setBounds(142, 75, 74, 14);
        contentPane.add(lblNewLabel_2);

        txtClientName = new JTextField();
        txtClientName.setColumns(10);
        txtClientName.setBounds(235, 72, 96, 20);
        contentPane.add(txtClientName);
        txtPort.setText(String.valueOf(PORT));
        
        JButton btnConnect = new JButton("Connect");
        btnConnect.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
                PORT = Integer.parseInt(txtPort.getText());
                ClientName = txtClientName.getText().trim();
                if (ClientName.isEmpty()) {
                    textArea.append("Please enter a valid client name.\n");
                    return;
                }
                connectToServer();
                startReceivingMessages();
        	}
        });
        btnConnect.setFont(new Font("Tahoma", Font.PLAIN, 14));
        btnConnect.setBounds(235, 97, 89, 28);
        contentPane.add(btnConnect);
    }

    private void sendMessage() {
        String message = textField.getText();
        if (!message.isEmpty()) {
            textArea.append("Me: " + message + "\n");
            writer.println(ClientName + ": " + message);
            textField.setText("");
        }
    }

    private void connectToServer() {
        try {
            socket = new Socket("localhost", PORT);
            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            textArea.append("Connected to the server on port " + PORT + ".\n");
        } catch (IOException e) {
            textArea.append("Could not connect to server: " + e.getMessage() + "\n");
        }
    }

    public void startReceivingMessages() {
        new Thread(() -> {
            try {
                String response;
                while ((response = reader.readLine()) != null) {
                    updateTextArea(response);
                }
            } catch (IOException e) {
                textArea.append("Connection lost: " + e.getMessage() + "\n");
            }
        }).start();
    }

    private void updateTextArea(String message) {
        if (!message.startsWith(ClientName)) {
            textArea.append(message + "\n");
        }
    }
}
